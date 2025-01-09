package me;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.mail2.jakarta.util.MimeMessageParser;

import jakarta.activation.DataSource;
import jakarta.mail.Address;
import jakarta.mail.Header;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
public abstract class Mail implements Comparable<Mail>{
    protected long UID;
    protected ArrayList<InputStream> isattlist;
    protected MimeMessage mess;
    protected String sender,subject,date;
    protected String plainText;
    protected Date sendDate;
    protected String htmlContent;
    protected List<DataSource> attachments;
    protected List<Address> receivers;
    protected InputDevice id;
    protected OutputDevice od;
    public Mail(){
        mess=null;
        sender="";
        subject="";
        // receivers="";
        id=null;
        od=null;
    }
    public Mail(Message t){
        try{
        if (t.getFrom()!=null){
        sender=t.getFrom()[0].toString();
        }
        else sender="";
        subject=t.getSubject();
        // receivers=t.getRecipients;
        // receivers="";
        // for (Address i : t.getAllRecipients()){
        //     receivers=receivers.concat(i.toString());
        // }
        Session session=null;
        mess=new MimeMessage(session);
        if (t.getFrom()!=null){
        mess.setFrom(t.getFrom()[0]);
        }
        else mess.setFrom("");
        mess.setRecipients(Message.RecipientType.TO,t.getRecipients(Message.RecipientType.TO));
        mess.setRecipients(Message.RecipientType.CC,t.getRecipients(Message.RecipientType.CC));
        mess.setRecipients(Message.RecipientType.BCC,t.getRecipients(Message.RecipientType.BCC));
        mess.setSubject(t.getSubject());
        mess.setSentDate(t.getSentDate());
        // MimeMessageParser parser=new MimeMessageParser(mess).parse();
        UID=t.getMessageNumber();
        // if 
        // sender=t.getFrom()[0].toString();
        subject=t.getSubject();
        sendDate=t.getSentDate();
        if (t.getSentDate()!=null)
        date=t.getSentDate().toString().substring(0,16);
        else date=null;
        // try{
        // mess.setContent(t.getContent(),t.getContentType());
        // mess.setFrom(t.getFrom()[0]);
        // mess.setRecipients(Message.RecipientType.TO,t.getRecipients(Message.RecipientType.TO));
        // mess.setSubject(t.getSubject());
        // mess.setSentDate(t.getSentDate());
        // for (Enumeration<Header> headers=t.getAllHeaders(); headers.hasMoreElements();){
        //     Header header=headers.nextElement();
        //     mess.setHeader(header.getName(),header.getValue());
        // }
        // MimeMessageParser parser=new MimeMessageParser(mess).parse();
        // UID=t.getMessageNumber();
        // sender=parser.getFrom();
        // subject=parser.getSubject();
        // plainText=parser.getPlainContent();
        // htmlContent=parser.getHtmlContent();
        // attachments=parser.getAttachmentList();
        // receivers=parser.getTo();
        // // t.getSentDate().toString
        // sendDate=t.getSentDate();
        // date=t.getSentDate().toString().substring(0,16);
        // }
        // catch(IOException e){   
        //     e.printStackTrace();
        // }
        // String emailstring=t;
        // InputStream targetStream=new ByteArrayInputStream(emailstring.getBytes());
        // Session puppetsess=null;
        // try {
        //     mess=new MimeMessage(puppetsess,targetStream);
        //     MimeMessageParser parser= new MimeMessageParser(mess).parse();
        //     // parser.parse();
        //     sender=parser.getFrom();
        //     subject=parser.getSubject();
        //     plainText=parser.getPlainContent();
        //     htmlContent=parser.getHtmlContent();
        //     attachments=parser.getAttachmentList();
        //     receivers=parser.getTo();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // mess.addFrom(t.getFrom());
        // mess.addRecipients(Message.RecipientType.TO,t.getFrom());
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
    }
    @Override
    public int compareTo(Mail b){
        int val=this.sendDate.compareTo(b.sendDate);
        if (val==0){
            val=this.sender.compareTo(b.sender);
            return val;
        }
        return -val;
    }
    public void setUID(long i){
        UID=i;
    }
    public Mail(MimeMessage t){
        // id=new InputDevice();
        // od=new OutputDevice();
        mess=t;
        try{
        MimeMessageParser parser=new MimeMessageParser(t).parse();
        sender=parser.getFrom();
        subject=parser.getSubject();
        plainText=parser.getPlainContent();
        htmlContent=parser.getHtmlContent();
        attachments=parser.getAttachmentList();
        receivers=parser.getTo();
        UID=t.getMessageNumber();
        System.out.println(sender);
        System.out.println(subject);
        System.out.println(plainText);
        System.out.println(htmlContent);
        System.out.println();
        }
        catch(IOException e){
            sender="";
            subject="";
            plainText="";
            htmlContent="";
            attachments=null;
            receivers=null;
        }
        catch (MessagingException e){
            sender="";
            subject="";
            plainText="";
            htmlContent="";
            attachments=null;
            receivers=null;
        }
        // sender="";
        // subject="";
        // receivers="";
        // Address[] rec={};
        // try {
        //     rec=t.getAllRecipients();
        // } catch (MessagingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // for (Address address:rec){
        //     receivers.concat(" "+address.toString());
        // }
        // try {
        //     rec=t.getFrom();
        // } catch (MessagingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // sender=rec[0].toString();
    }
    public void retrieveData(Message t){
        try{
            isattlist=new ArrayList<>();
            System.out.println("RETRIEVING");
        mess.setContent(t.getContent(),t.getContentType());
        mess.setFrom(t.getFrom()[0]);
        mess.setRecipients(Message.RecipientType.TO,t.getRecipients(Message.RecipientType.TO));
        mess.setSubject(t.getSubject());
        mess.setSentDate(t.getSentDate());
        for (Enumeration<Header> headers=t.getAllHeaders(); headers.hasMoreElements();){
            Header header=headers.nextElement();
            mess.setHeader(header.getName(),header.getValue());
        }
        MimeMessageParser parser=new MimeMessageParser(mess).parse();
        UID=t.getMessageNumber();
        sender=parser.getFrom();
        subject=parser.getSubject();
        plainText=parser.getPlainContent();
        htmlContent=parser.getHtmlContent();
        attachments=parser.getAttachmentList();
        receivers=parser.getTo();
        for (DataSource i:attachments){
            try{
            isattlist.add(i.getInputStream());
            }
            catch(IOException E){
                E.printStackTrace();
            }
        }
        // System.out.println(parser.getTo());
        // t.getSentDate().toString
        sendDate=t.getSentDate();
        date=t.getSentDate().toString().substring(0,16);
        }
        catch(IOException e){   
            e.printStackTrace();
        }
        catch(MessagingException e){
        e.printStackTrace();
        }
        // String emailstring=t;
        // InputStream targetStream=new ByteArrayInputStream(emailstring.getBytes());
        // Session puppetsess=null;
        // try {
        //     mess=new MimeMessage(puppetsess,targetStream);
        //     MimeMessageParser parser= new MimeMessageParser(mess).parse();
        //     // parser.parse();
        //     sender=parser.getFrom();
        //     subject=parser.getSubject();
        //     plainText=parser.getPlainContent();
        //     htmlContent=parser.getHtmlContent();
        //     attachments=parser.getAttachmentList();
        //     receivers=parser.getTo();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        
        // mess.addFrom(t.getFrom());
        // mess.addRecipients(Message.RecipientType.TO,t.getFrom());
    
    }
}
