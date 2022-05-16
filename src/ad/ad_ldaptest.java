import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

public class ad_ldaptest {
  public static void main(String a[]) {
    // set the LDAP authentication method
    String auth_method  = "simple";
    // set the LDAP client Version
    String ldap_version = "3";
    // This is our LDAP Server's IP
    String ldap_host    = "192.168.56.2";
    // This is our LDAP Server's Port
    String ldap_port    = "389";
    // This is our access ID
    String ldap_dn      = "ldap";
   // This is our access PW
    String ldap_pw      = "Zoho@2022";
    // This is our base DN
    String base_dn      = "DC=zoho-pt5387,dc=com";

    DirContext ctx      = null;
    Hashtable env       = new Hashtable();

    // Here we store the returned LDAP object data
    String dn           = "";
    // This will hold the returned attribute list
    Attributes attrs;

    env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL,"ldap://" + ldap_host + ":" + ldap_port);
    env.put(Context.SECURITY_AUTHENTICATION, auth_method);
    env.put(Context.SECURITY_PRINCIPAL, ldap_dn);
    env.put(Context.SECURITY_CREDENTIALS, ldap_pw);
    env.put("java.naming.ldap.version", ldap_version);

    try{
      System.out.println("Connecting to host " + ldap_host + " at port " + ldap_port + "...");
      System.out.println();

      ctx = new InitialDirContext(env);
      System.out.println("LDAP authentication successful!");

      // Specify the attribute list to be returned
      String[] attrIDs = { "memberOf" };

      SearchControls ctls = new SearchControls();
      ctls.setReturningAttributes(attrIDs);
      ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      // Specify the search filter to match
      String filter = "(&(objectClass=user))";

      // Search the subtree for objects using the given filter
      NamingEnumeration answer = ctx.search(base_dn, filter, ctls);

      // Print the answer
      //Search.printSearchEnumeration(answer);

      while (answer.hasMoreElements()) {
        SearchResult sr = (SearchResult)answer.next();
        dn = sr.getName();
        attrs = sr.getAttributes();

        System.out.println("Found Object: " + dn + "," + base_dn);
        if (attrs != null) {
          // we have some attributes for this object
          NamingEnumeration ae = attrs.getAll();
          while (ae.hasMoreElements()) {
            Attribute attr = (Attribute)ae.next();
            String attrId = attr.getID();
            System.out.println("Found Attribute: " + attrId);
            Enumeration vals = attr.getAll();
            while (vals.hasMoreElements()) {
              String attr_val = (String)vals.nextElement();
              System.out.println(attrId + ": " + attr_val);
            }
          }
        }
     }

      // Close the context when we're done
      ctx.close();
    } catch (AuthenticationException authEx) {
      authEx.printStackTrace();
      System.out.println("LDAP authentication failed!");
    } catch (NamingException namEx) {
      System.out.println("LDAP connection failed!");
      namEx.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
   }
  }
}

/*
The directory server has failed to create the AD LDS serviceConnectionPoint object in Active Directory Lightweight Directory Services. This operation will be retried. 
 
Additional Data 
SCP object DN:
CN={ded04790-7086-4dde-95d5-ce52323a42c0},CN=WIN-A48VMHIPEK4,OU=Domain Controllers,DC=zoho-pt5387,DC=com 
Error value:
5 Access is denied. 
Server error:
00000005: SecErr: DSID-031528D2, problem 4003 (INSUFF_ACCESS_RIGHTS), data 0
 
Internal ID:
33903d2 
AD LDS service account:
NT AUTHORITY\NETWORK SERVICE 
 
User Action 
If AD LDS is running under a local service account, it will be unable to update the data in Active Directory Lightweight Directory Services. Consider changing the AD LDS service account to either NetworkService or a domain account. 
 
If AD LDS is running under a domain user account, make sure this account has sufficient rights to create the serviceConnectionPoint object. 
 
ServiceConnectionPoint object publication can be disabled for this instance by setting msDS-DisableForInstances attribute on the SCP publication configuration object.

The security of this directory server can be significantly enhanced by configuring the server to reject SASL (Negotiate, Kerberos, NTLM, or Digest) LDAP binds that do not request signing (integrity verification) and LDAP simple binds that are performed on a clear text (non-SSL/TLS-encrypted) connection.  Even if no clients are using such binds, configuring the server to reject them will improve the security of this server. 
 
Some clients may currently be relying on unsigned SASL binds or LDAP simple binds over a non-SSL/TLS connection, and will stop working if this configuration change is made.  To assist in identifying these clients, if such binds occur this directory server will log a summary event once every 24 hours indicating how many such binds occurred.  You are encouraged to configure those clients to not use such binds.  Once no such events are observed for an extended period, it is recommended that you configure the server to reject such binds. 
 
For more details and information on how to make this configuration change to the server, please see http://go.microsoft.com/fwlink/?LinkID=87923. 
 
You can enable additional logging to log an event each time a client makes such a bind, including information on which client made the bind.  To do so, please raise the setting for the "LDAP Interface Events" event logging category to level 2 or higher.
*/