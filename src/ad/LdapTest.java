import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.List;

public class LdapTest {


public List<String> getListing() {

    LdapTemplate template = getTemplate();

    List<String> children = template.list("DC=zoho-pt5387,dc=com");

   return children;
}


private LdapTemplate getTemplate(){

    LdapContextSource contextSource = new LdapContextSource();
    contextSource.setUrl("ldap://192.168.56.2:50000");
    contextSource.setUserDn("cn=Santheesh");
    contextSource.setPassword("Zoho@2022");

    try {
        contextSource.afterPropertiesSet();
    } catch (Exception ex) {
        ex.printStackTrace();
    }


    LdapTemplate template = new LdapTemplate();

    template.setContextSource(contextSource);

    return template;

}


public static void main(String[] args){


    LdapTest sClient = new LdapTest();
    List<String> children = sClient.getListing();

    for  (String child :children) {
        System.out.println(child);
    }

}

}