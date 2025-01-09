package me;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
public class BoxTest {
    @Test
    void testnoargconstruct(){
        Box test=new Box();
        assertNotEquals(null,test,"object not created");
    }
    @Test
    void testactualbox(){
        User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
         Session session;
        Properties prop=new Properties();
        // prop.put("mail.store.protocol","imap");
        prop.put("mail.imap.host",user.hostImap);
        prop.put("mail.imap.port",user.portImap);
        prop.put("mail.imap.ssl.enable","true");
        prop.put("mail.imap.auth","true");
        session=Session.getInstance(prop,new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(user.email,user.password);
            }
        });
        Box test=new Box(user,session,prop,"[Gmail]/Starred","Starred");
        test.loadMoreItems();
        assertEquals(0,test.length(),"bad box creation custom");
    }
}
