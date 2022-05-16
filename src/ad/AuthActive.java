
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

public class AuthActive
{
  static DirContext ldapContext;
  public static void main (String[] args) throws NamingException
  {
    try
    {
      System.out.println("Début du test Active Directory");

      Hashtable<String, String> environment = new Hashtable<String, String>();
    //CN=RID Set,CN=WIN-A48VMHIPEK4,OU=Domain Controllers,DC=zoho-pt5387,DC=com
    environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    environment.put(Context.PROVIDER_URL, "ldap://192.168.56.2:389");
    environment.put(Context.SECURITY_AUTHENTICATION, "simple");
    environment.put(Context.SECURITY_PRINCIPAL, "cn=ldap-user ,cn=users, dc=zoho-pt5387,dc=com");
    environment.put(Context.SECURITY_CREDENTIALS, "Zoho@2022");
      //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
     //environment.put(Context.SECURITY_PROTOCOL, "simple");
     environment.put("java.naming.ldap.version", "3");
      ldapContext = new InitialDirContext(environment);

      // Create the search controls         
      SearchControls searchCtls = new SearchControls();

      //Specify the attributes to return
      String returnedAtts[]={"sn","givenName", "memberOf"};
      searchCtls.setReturningAttributes(returnedAtts);

      //Specify the search scope
      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      //specify the LDAP search filter
      String searchFilter = "(&(objectClass=user))";

      //Specify the Base for the search
      String searchBase = "DC=zoho-pt5387,dc=com";
      //initialize counter to total the results
      int totalResults = 0;

      // Search for objects using the filter
      NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);


      System.out.println("Total results: " + totalResults);
      ldapContext.close();
    }
    catch (Exception e)
    {
      System.out.println(" Search error: " + e);
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
//ou=ourldap,dc=zoho-pt5387,dc=com
//WIN-A48VMHIPEK4\Administrator
//WIN-A48VMHIPEK4	instance1	ADAM_instance1	Running	Automatic
