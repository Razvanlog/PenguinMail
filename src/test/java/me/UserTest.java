package me;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
public class UserTest {
    @Test
    public void testUserCreation(){
        User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
        assertEquals(user.email, "webemailt@gmail.com","bad email");
        assertEquals(user.password, "touijxfxyvhebkdt","bad password");
        assertEquals(user.hostSent,"smtp.gmail.com", "bad host sent");
        assertEquals(user.portSent,"465","bad port sent");
        assertEquals(user.hostImap,"imap.gmail.com","bad host imap");
        assertEquals(user.portImap, "993","bad port imap");
    }
    @Test
    public void testUserConnect(){
        User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
        assertEquals(0, user.connect(),"bad connection function");
        // assertEquals(5,6,"test wrong answer");
    }
    @Test
    public void testfolderObtainer(){
        User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
        user.getFolders();
        // user.boxFolders.get(0);
        String[] answers=new String[]{"INBOX","All Mail","Drafts","Important","Sent Mail","Spam","Starred","Trash"};
        for (int i=0; i<answers.length; i++){
            assertEquals(user.boxFolders.get(i).getFolderName(),answers[i]);   
        }
    }
    @Test
    public void testCreateEmailAndSend(){
        User user=new User("webemailt@gmail.com","touijxfxyvhebkdt","smtp.gmail.com","465","imap.gmail.com","993",0);
        // assertEquals()
        user.createEmail("webemailt@gmail.com","webemailt@gmail.com","","","Test","Test",null);
        assertEquals(0,user.sendMail(),"bad send email function");
    }
}
