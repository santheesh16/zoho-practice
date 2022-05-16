package com.zoho.booktickets.service;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.text.MessageFormat;
import java.text.ParseException;

import javax.naming.ldap.*;
import com.zoho.booktickets.model.ADUser;
import com.zoho.booktickets.service.*;
import java.text.*;
import java.util.Date;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

public class ADService {

    private static final String DOMAIN_NAME = "zoho-pt5387.com";
    private static final String DOMAIN_ROOT = "DC=zoho-pt5387,dc=com";
    private static final String DOMAIN_URL = "LDAP://192.168.56.2:389";
    private static final String ADMIN_NAME = "CN=Administrator,CN=Users,DC=zoho-pt5387,DC=com";
    private static final String ADMIN_PASS = "Zoho@2022";
    private static final String MEMBER_OF = "memberOf";
    private int UF_ACCOUNTENABLE = 0x0001;
    private int UF_ACCOUNTDISABLE = 0x0002;
    private int UF_PASSWD_NOTREQD = 0x0020;
    private int UF_PASSWD_CANT_CHANGE = 0x0040;
    private int UF_NORMAL_ACCOUNT = 0x0200;
    private int UF_DONT_EXPIRE_PASSWD = 0x10000;
    private static final String AD_ATTR_NAME_USER_EMAIL = "mail";
    private DirContext context;
    private String dn = "";
    String dnBase = ",CN=users, DC=zoho-pt5387,dc=com";

    public ADService() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ADMIN_NAME);
        env.put(Context.SECURITY_CREDENTIALS, ADMIN_PASS);
        env.put(Context.PROVIDER_URL, DOMAIN_URL);
        try {
            this.context = new InitialDirContext(env);
        } catch (NamingException e) {
            System.err.println("Problem creating object: ");
            e.printStackTrace();
        }

    }

    public static LocalDate nanoToDate(String nanoTime) {
        long fileTime = (Long.parseLong(nanoTime) / 10000L) - +11644473600000L;

        Date inputDate = new Date(fileTime);
        LocalDate localDate = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(2);
        return localDate;
    }

    public static String dateToNano(String dateString) {
        String outputDate = null;
        try {
            LocalDate localDate = LocalDate.now().plusDays(Integer.parseInt(dateString));
            Date date = Date.from(localDate.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long fileTime = (date.getTime() + 11644473600000L) * 10000L;
            outputDate = Long.toString(fileTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static long expiryDays(LocalDate dateBefore) {
        long noOfDaysBetween = 0L;
        try {
            noOfDaysBetween = ChronoUnit.DAYS.between(LocalDate.now(), dateBefore);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfDaysBetween;
    }

    private NamingEnumeration<SearchResult> getUserDataBysAMAccountName(String username)
            throws Exception {
        String filter = "(&(&(objectClass=person) (objectCategory=user))(sAMAccountName=" + username + "))";
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = null;
        try {
            answer = context.search(DOMAIN_ROOT, filter, searchCtls);
        } catch (Exception e) {

            throw e;
        }

        return answer;
    }

    public String getUserMailFromSearchResults(NamingEnumeration<SearchResult> userData)
            throws Exception {
        try {
            String mail = null;
            // getting only the first result if we have more than one
            if (userData.hasMoreElements()) {
                SearchResult sr = userData.nextElement();
                Attributes attributes = sr.getAttributes();
                mail = attributes.get(AD_ATTR_NAME_USER_EMAIL).get().toString();
                System.out.println("found email " + mail);
            }

            return mail;
        } catch (Exception e) {

            throw e;
        }
    }

    public void checkExpiry() throws NamingException {
        long EXPIRY_LIMIT_DATE = 5;
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {

                        String searchFilter = "(objectClass=user)";
                        String[] reqAtt = { "cn", "sn", "sAMAccountName", "accountExpires", "mail" };
                        SearchControls controls = new SearchControls();
                        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                        controls.setReturningAttributes(reqAtt);

                        NamingEnumeration users = context.search(DOMAIN_ROOT, searchFilter, controls);

                        SearchResult result = null;
                        while (users.hasMoreElements()) {
                            SearchResult sr = (SearchResult) users.next();
                            Attributes attrs = sr.getAttributes();
                            long expiryDays = expiryDays(nanoToDate(attrs.get("accountExpires").get().toString()));
                            if (expiryDays < EXPIRY_LIMIT_DATE) {
                                System.out
                                        .println("Account Expiry below 2 days UsersName Mail is " + attrs.get("mail"));
                                EmailService.sendExpiryAlert(attrs.get("mail").get().toString(),
                                        String.valueOf(expiryDays));
                            }
                        }
                        Thread.sleep(1000 * 60); // 1 minute
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }

    public void getMail(String userName) throws Exception {
        String searchFilter = "((cn=Test3))"; // or condition
        String[] reqAtt = { "cn", "sn", "uid" };
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(reqAtt);

        NamingEnumeration users = context.search("cn=users," + DOMAIN_ROOT, searchFilter, controls);

        SearchResult result = null;
        while (users.hasMore()) {
            result = (SearchResult) users.next();
            Attributes attr = result.getAttributes();
            String name = attr.get("cn").get(0).toString();
            // deleteUserFromGroup(name,"Administrators");
            System.out.println(attr.get("cn"));
            System.out.println(attr.get("sn"));
            System.out.println(attr.get("uid"));
        }
    }

    public static boolean authenticate(String user, String securityToken) throws Exception {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, DOMAIN_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + DOMAIN_NAME);
        env.put(Context.SECURITY_CREDENTIALS, securityToken);
        try {
            InitialDirContext ctx = new InitialDirContext(env);
            System.out.println("LDAP authentication sucess!");
            return true;
        } catch (AuthenticationException authEx) {
            authEx.printStackTrace();
            System.out.println("LDAP authentication failed!");
            return false;
        }
    }

    public void addUser(ADUser aduser)
            throws NamingException {
        try {
            String newQuotedPassword = "\"" + aduser.getPassword() + "\"";
            byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");
            Attributes container = new BasicAttributes();
            Attribute objClasses = new BasicAttribute("objectClass");
            objClasses.add("top");
            objClasses.add("person");
            objClasses.add("organizationalPerson");
            objClasses.add("user");

            // Assign the username, first name, and last name
            String cnValue = aduser.getFirstName() + " " + aduser.getLastName();
            Attribute cn = new BasicAttribute("cn", cnValue);
            Attribute sn = new BasicAttribute("sn", aduser.getLastName());
            Attribute sAMAccountName = new BasicAttribute("sAMAccountName", aduser.getUserName());
            Attribute principalName = new BasicAttribute("userPrincipalName", aduser.getUserName() + "@" + DOMAIN_NAME);
            Attribute givenName = new BasicAttribute("givenName", aduser.getFirstName());
            Attribute uid = new BasicAttribute("uid", aduser.getUserName());
            Attribute userPassword = new BasicAttribute("userPassword", aduser.getPassword());
            Attribute unicodePwd = new BasicAttribute("unicodePwd", getPassword(aduser.getPassword()));
            Attribute description = new BasicAttribute("description", aduser.getDescription());
            Attribute Office = new BasicAttribute("physicalDeliveryOfficeName", aduser.getOffice());
            Attribute mail = new BasicAttribute("mail", aduser.getPassword());
            Attribute mobileNumber = new BasicAttribute("telephoneNumber", aduser.getMobileNumber());
            Attribute userControl = new BasicAttribute("userAccountControl",
                    Integer.toString(UF_NORMAL_ACCOUNT + UF_ACCOUNTENABLE  + UF_PASSWD_NOTREQD));
            Attribute accountExpires = new BasicAttribute("accountExpires", dateToNano(aduser.getAccountExpires()));
            // Add these to the container
            container.put(objClasses);
            container.put(sAMAccountName);
            container.put(principalName);
            container.put(cn);
            container.put(sn);
            container.put(givenName);
            container.put(uid);
            container.put(userPassword);
            container.put(description);
            container.put(Office);
            container.put(mail);
            container.put(mobileNumber);
            container.put(accountExpires);
            container.put(userControl);
            // container.put(unicodePwd);
            context.createSubcontext("cn=" + cnValue + ",cn= users," + DOMAIN_ROOT, container);
            System.out.println(aduser.getUserName() + " user succcessfully added");
        } catch (Exception e) {
            context.close();
            e.printStackTrace();

        }
    }

    public void addBulkUser(List<ADUser> adusers) throws NamingException {
        for (ADUser aduser : adusers) {
            addUser(aduser);
        }
    }

    public void getAllUsers() throws NamingException {
        String searchFilter = "(objectClass=user)";
        String[] reqAtt = { "cn", "sn", "sAMAccountName", "accountExpires", "maxPwdAge" };
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(reqAtt);

        NamingEnumeration users = context.search(DOMAIN_ROOT, searchFilter, controls);

        SearchResult result = null;

        while (users.hasMoreElements()) {
            SearchResult sr = (SearchResult) users.next();
            dn = sr.getName();
            Attributes attrs = sr.getAttributes();

            System.out.println("Found Object: " + dn + "," + DOMAIN_ROOT);
            if (attrs != null) {
                // we have some attributes for this object
                NamingEnumeration ae = attrs.getAll();
                while (ae.hasMoreElements()) {
                    Attribute attr = (Attribute) ae.next();
                    String attrId = attr.getID();
                    System.out.println("Found Attribute: " + attrId);
                    if (attrId.equals("accountExpires")) {
                        Enumeration vals = attr.getAll();
                        while (vals.hasMoreElements()) {
                            String attr_val = (String) vals.nextElement();
                            System.out.println(attrId + ": " + nanoToDate(attr_val));
                            System.out.println("Rem Expiry Days: " + expiryDays(nanoToDate(attr_val)));
                        }
                    } else {
                        Enumeration vals = attr.getAll();
                        while (vals.hasMoreElements()) {
                            String attr_val = (String) vals.nextElement();
                            System.out.println(attrId + ": " + attr_val);
                        }
                    }
                }
            }
        }
    }

    private byte[] getPassword(String newPass) {
        String quotedPassword = "\"" + newPass + "\"";
        // return quotedPassword.getBytes("UTF-16LE");
        char unicodePwd[] = quotedPassword.toCharArray();
        byte pwdArray[] = new byte[unicodePwd.length * 2];
        for (int i = 0; i < unicodePwd.length; i++) {
            pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
            pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
        }
        return pwdArray;
    }

    public void updateUserDetail(ADUser aduser) {
        try {
            ModificationItem[] mods = new ModificationItem[8];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("sn", aduser.getLastName()));
            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("givenName", aduser.getFirstName()));
            mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("displayName", aduser.getFirstName() + " " + aduser.getLastName()));
            mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("description", aduser.getDescription()));
            mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("physicalDeliveryOfficeName", aduser.getOffice()));
            mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("telephoneNumber", aduser.getMobileNumber()));
            mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mail", aduser.getMail()));
            mods[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("url", aduser.getUrl()));

            context.modifyAttributes("CN=" + aduser.getUserName() + dnBase, mods);// try to form DN dynamically
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("failed: " + e.getMessage());
        }
    }

    public void updateUserPassword(String userName, String password) {

        try {
            ModificationItem[] mods = new ModificationItem[1];

            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
            new BasicAttribute("userPassword", password));

            // mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
            //         Integer.toString(UF_NORMAL_ACCOUNT)));
            context.modifyAttributes("CN=" + userName + dnBase, mods);
            context.close();
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("failed: " + e.getMessage());
        }
    }

    public void deleteUser(String userName) {
        try {
            context.destroySubcontext("cn=" + userName + ",cn=users,DC=zoho-pt5387,dc=com");
            System.out.println("success");
        } catch (NamingException e) {
            System.out.println(userName + " user failed to delete");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        // ADUser aduser = new ADUser("Test", "Test", "Test", "ldap",
        // "Zoho@2022", "santheesh11@gmail.com", "Check User Update", "+917904188021",
        // "Zoho Corp", "test.zoho-pt5387.com", "4");
        // new ADService().addUser(aduser);

        // new ADService().getAllUsers();
        // new ADService().getUserMailFromSearchResults(new
        // ADService().getUserDataBysAMAccountName("Test3"));
        // new ADService().checkExpiry();

        // ADUser aduser = new ADUser("Test", "Test", "Test-Four", "ldap",
        // "Zoho@2022",
        // "test2@gmail.com", "Check User Update", "+917904188021", "Zoho Corp",
        // "test2.zoho-pt5387.com");

        // new ADService().updateUserDetail(aduser);

        new ADService().updateUserPassword("Test", "Santh@2022");
        // System.out.println(ADService.authenticate("Test", "Zoho@2022"));
        // System.out.println(new ADService().authenticate("Test1","Santh@2022"));
        // new ADService().deleteUser("Test1");
        // new ADService().getAllUsers();

        // List<ADUser> adusers = new ArrayList<ADUser>() {
        //     {
        //         add(new ADUser("Test4", "Test4", "Test-Four", "ldap", "Zoho@2022",
        //                 "test4@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //         add(new ADUser("Test5", "Test5", "Test-Five", "ldap", "Zoho@2022",
        //                 "test5@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //         add(new ADUser("Test6", "Test6", "Test-Six", "ldap", "Zoho@2022",
        //                 "test6@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //         add(new ADUser("Test7", "Test7", "Test-Seven", "ldap", "Zoho@2022",
        //                 "test7@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //         add(new ADUser("Test8", "Test8", "Test-eight", "ldap", "Zoho@2022",
        //                 "test8@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //         add(new ADUser("Test9", "Test9", "Test-nine", "ldap", "Zoho@2022",
        //                 "test9@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //         add(new ADUser("Test10", "Test10", "Test-ten", "ldap", "Zoho@2022",
        //                 "test10@gmail.com", "Check User Update", "+917904188021",
        //                 "Zoho Corp", "test4.zoho-pt5387.com"));
        //     }
        // };
        // new ADService().addBulkUser(adusers);

    }
}