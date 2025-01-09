package me;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class currMailSend extends Mail implements mailSendable{
    // public currMailSend(MimeMessage t){
    //     super(t);
    // }
    public currMailSend(Message t){
        super(t);
    }
//     public currMailSend(Session session,MimeMessage t){
//         super(t);
//         MimeMessage newMessage = new MimeMessage(session);
//         try{
// newMessage.setContent(t.getContent(), t.getContentType());
// newMessage.setFrom(t.getFrom()[0]);
// newMessage.setRecipients(Message.RecipientType.TO, t.getRecipients(Message.RecipientType.TO));
// newMessage.setSubject(t.getSubject());
//         }
//         catch(IOException e){
//             e.printStackTrace();
//         }
//         catch(MessagingException e){
//             e.printStackTrace();
//         }
//     }
    public currMailSend(Session session,Message t){
        // super(t);
        MimeMessage newMessage = new MimeMessage(session);
        try{
// newMessage.setContent(t.getContent(), t.getContentType());
// newMessage.setFrom(t.getFrom()[0]);
// System.out.println(t.getFrom().toString());
// newMessage.setFrom(t.getFrom();
Enumeration<jakarta.mail.Header>headers=t.getAllHeaders();
while (headers.hasMoreElements()){
    jakarta.mail.Header head=headers.nextElement();
    newMessage.setHeader(head.getName(),head.getValue());
}

// if (t.getRecipients(Message.RecipientType.TO)!=null){
// newMessage.setRecipients(Message.RecipientType.TO, t.getRecipients(Message.RecipientType.TO));
// }
// if (t.getRecipients(Message.RecipientType.CC)!=null){
//     newMessage.setRecipients(Message.RecipientType.CC, t.getRecipients(Message.RecipientType.CC));
//     }
//     if (t.getRecipients(Message.RecipientType.BCC)!=null){
//         newMessage.setRecipients(Message.RecipientType.BCC, t.getRecipients(Message.RecipientType.BCC));
//         }
        
    
// newMessage.setRecipients(Message.RecipientType.TO, t.getRecipients(Message.RecipientType.TO));

newMessage.setSubject(t.getSubject());
subject=t.getSubject();
        }
        // catch(IOException e){
        //     e.printStackTrace();
        // }
        catch(MessagingException e){
            e.printStackTrace();
        }
        mess=newMessage;
    }
    public currMailSend(Session session,Properties prop,String user_mail,
    String ToAddress,String CCAddress,String BCCAddress,
    String subj, String Textmess, List<File>Attachments){
        sender=user_mail;
        // id=new InputDevice();
        // od=new OutputDevice();
        mess=new MimeMessage(session);
        try {
            mess.setFrom(sender);
            // String recipients_list="razvan.diaconescu04@e-uvt.ro";
            ToAddress=ToAddress.replace(" ",",");
            CCAddress=CCAddress.replace(" ",",");
            BCCAddress=BCCAddress.replace(" ",",");
            InternetAddress[] ToList=InternetAddress.parse(ToAddress);
            InternetAddress[] CCList=InternetAddress.parse(CCAddress);
            InternetAddress[] BCCList=InternetAddress.parse(BCCAddress);
            // System.out.println(recipients_listt[0]);
            mess.setRecipients(Message.RecipientType.TO,ToList);
            mess.setRecipients(Message.RecipientType.CC,CCList);
            mess.setRecipients(Message.RecipientType.BCC,BCCList);
            // String subj="Java project test";
            mess.setSubject(subj);
            subject=subj;
            // String Textmess="I am a test! :)";
            MimeBodyPart textMess=new MimeBodyPart();
            textMess.setText(Textmess);
            plainText=Textmess;
            Multipart multipart=new MimeMultipart();
            multipart.addBodyPart(textMess);
            if (Attachments!=null){
                for (File att: Attachments){
                    MimeBodyPart attpart=new MimeBodyPart();
                    // FileDataSource fd=new FileDataSource(att);
                    // attpart.setDataHandler(new DataHan));
                    attpart.attachFile(att);
                    attpart.setFileName(att.getName());
                    multipart.addBodyPart(attpart);
                }
            }
            mess.setContent(multipart);
            // mess.setText(Textmess);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    @Override
    public int sendMail(){
        try{
            // pos=0;
            Transport.send(mess);
            return 0;
        }
        // catch(SendFailedException e){
        //     // e.getInvalidAddresses()
        //     VBox errorBox=new VBox();
        //     Label errorL=new Label();
        //     errorL.setAlignment(Pos.CENTER);
        //     errorL.setText("mail cannot be sent to: ");
        //     errorBox.getChildren().addAll(errorL);
        //     if (e.getInvalidAddresses()!=null){
        //     for (jakarta.mail.Address i: e.getInvalidAddresses()){
        //         Label addressL=new Label(i.toString()+" is not a valid email address");
        //         errorBox.getChildren().add(addressL);
        //     }
        // }
        //     if (e.getValidUnsentAddresses()!=null){
        //     for (jakarta.mail.Address i: e.getValidUnsentAddresses()){
        //         Label adressL=new Label(i.toString()+" is valid but was not sent");
        //         errorBox.getChildren().add(adressL);
        //     }
        // }
        //     Stage errorMess=new Stage();    
        //     Scene sc=new Scene(errorBox,200,100);
        //     errorMess.setScene(sc);
        //     errorMess.initModality(Modality.APPLICATION_MODAL);
        //     errorMess.show();
        //     return 1;
        // }
        catch(MessagingException e){
            e.printStackTrace();
            VBox errorBox=new VBox();
            Label errorL=new Label();
            errorL.setAlignment(Pos.CENTER);
            errorL.setText("mail could not be sent");
            errorBox.getChildren().addAll(errorL);
            Stage errorMess=new Stage();    
            Scene sc=new Scene(errorBox,200,100);
            errorMess.setScene(sc);
            errorMess.initModality(Modality.APPLICATION_MODAL);
            errorMess.show();
            return 2;
            // System.err.println(e);
        }
        // return 2;
    }
}
