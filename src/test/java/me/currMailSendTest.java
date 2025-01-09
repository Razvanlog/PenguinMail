package me;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class currMailSendTest {
    @Test
    void constructortestMessage() {
        Message m=null;
        User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imap");
            props.put("mail.imap.host", "imap.gmail.com");  // Change this for other providers
            props.put("mail.imap.port", "993");
            props.put("mail.imap.ssl.enable", "true");
            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(user.email, user.password);

            // Open the INBOX folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            if (inbox.getMessageCount()>0)
                m=inbox.getMessage(1);
        currMailSend test=new currMailSend(m);
        assertNotEquals(null, test,"could not get message");
        inbox.close();
        store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void contructorFWMess(){
        currMailSend test=new currMailSend(null,null);
        assertNotEquals(null, test);
    }
    @Test
    void constructorfromScrathMessage(){
        // User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
        currMailSend test=new currMailSend(null,null,"webemailt@gmail.com",
        "webemailt@gmail.com","","","Test","Test",null);
        assertEquals("webemailt@gmail.com",test.sender,"bad creation");
        assertEquals("Test",test.subject,"bad creation");
        assertEquals("Test",test.plainText,"bad plain Text");
    }
}
