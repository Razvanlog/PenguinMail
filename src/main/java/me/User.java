package me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
public class User implements Comparable<User>, mailSendable, personSetable{
    String name,familyname;
    String email;
    String password;
    currMailSend currMail;
    // ArrayList<currMailSend> drafts;
    int priorityValue=0;
    // Box inbox;
    ArrayList<Box> boxFolders;
    ArrayList<Contact> contactList;
    // Drafts drafts;
    String portSent="576",portImap="993";
    String hostSent="smtp.gmail.com";
    String hostImap="imap.gmail.com";
    @Override
    public String getEmail(){
        return email;
    }
    @Override
    public void setEmail(String x){
        email=x;
    }
    public String getpasskey(){
        return password;
    }
    public void setpasskey(String x){
        password=x;
    }
    public String getportSent(){
        return portSent;
    }
    public void setportSent(String x){
        portSent=x;
    }
    public void setportImap(String x){
        portImap=x;
    }
    public String getportImap(){
        return portImap;
    }
    public void sethostSent(String x){
        hostSent=x;
    }
    public String getHostSent(){
        return hostSent;
    }
    public void setHostImap(String x){
        hostImap=x;
    }
    public String getHostImap(){
        return hostImap;
    }
    @Override
    public void setPriority(int x){
        priorityValue=x;
    }
    @Override
    public int getPriority(){
        return priorityValue;
    }
    // Properties prop;
    // Session session;
    // String receiver;
    @Override
    public int compareTo(User other){
        int answer=this.getPriority()-other.getPriority();
        if (answer==0){
            answer=this.email.compareTo(other.email);
        }
        return answer;
        // return 0;
    }
    public User(String E,String P,String hs, String ps,String hI,String pi,int p) {
        boxFolders=new ArrayList<>();
        email=E;
        priorityValue=p;
        password=P;
        hostSent=hs;
        hostImap=hI;
        portSent=ps;
        portImap=pi;
    }
    public void getFolders(){
        Session session;
        Properties prop=new Properties();
        // prop.put("mail.store.protocol","imap");
        prop.put("mail.imap.host",hostImap);
        prop.put("mail.imap.port",portImap);
        prop.put("mail.imap.ssl.enable","true");
        prop.put("mail.imap.auth","true");
        session=Session.getInstance(prop,new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(email,password);
            }
        });
        try {
            // session=Session.getInstance(prop);
            Store store=session.getStore("imap");
            store.connect(email,password);
            Folder defaultFolder=store.getDefaultFolder();
            // Folder[] subFolders=defaultFolder.list();
            // System.out.println(email);
            listFolders(defaultFolder,session,prop);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.printf("Succesfully created %s\n",email);
        // prop=new Properties();
        // // prop.put("mail.smtp.host",);
        // prop.put("mail.smtp.connectiontimeout","10000");
        // prop.put("mail.smtp.timeout","10000");
        // prop.put("mail.smtp.writetimeout","10000");
        // prop.put("mail.smtp.host","smtp.gmail.com");
        // prop.put("mail.smtp.port","587");
        // prop.put("mail.smtp.auth","true");
        // prop.put("mail.smtp.starttls.enable","true");
        // // prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        // session=Session.getInstance(prop,new Authenticator(){
        //     @Override
        //     protected PasswordAuthentication getPasswordAuthentication(){
        //     return new PasswordAuthentication(email,password);
        // }});
        // MimeMessage msg=new MimeMessage(session);
        // try {
        //     msg.setFrom(new InternetAddress(email));
        //     // 
        //     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("razvan.diaconescu04@e-uvt.ro"));
        //     msg.setSubject("test email");
        //     msg.setText("This is a jakarta test");
        //     System.out.println("YES");
        //     Transport.send(msg);
        //     System.out.println("succes");
        // } catch (AddressException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (MessagingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
    private void listFolders(Folder folder,Session sess,Properties prop) throws MessagingException{
        // System.out.println(folder.getFullName());
        if ((folder.getType() & Folder.HOLDS_FOLDERS)!=0){
            Folder[] subf=folder.list();
            for (Folder sf :subf){
                listFolders(sf,sess,prop);
            }
        }
        if ((folder.getType()&Folder.HOLDS_MESSAGES)!=0){
            // System.out.println("YES");
            Box newBox=new Box(this,sess,prop,folder.getFullName(),folder.getName());
            // System.out.printf("%s %s\n",folder.getFullName(),folder.getName());
            boxFolders.add(newBox);
            // boxFolders.add
        }
    }
    public int connect(){
        Session session;
        Properties prop=new Properties();
        prop.put("mail.store.protocol","imap");
        prop.put("mail.imap.host",hostImap);
        prop.put("mail.imap.port",portImap);
        prop.put("mail.imap.ssl.enable","true");
        prop.put("mail.imap.auth","true");
        session=Session.getInstance(prop,new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(email,password);
            }
        });
        try {
            Store store=session.getStore("imap");
            store.connect(this.email,this.password);
            if (store.isConnected())
            return 0;
        } catch (AuthenticationFailedException e) {
            return 1;
        }
        catch(MessagingException e){
            return 2;
        }
        return 2;
    }
    // @Override
    public void createEmailFWD(String user_mail,
    String ToAddress,String CCAddress,String BCCAddress,
    String subj, String Textmess, List<File>Attachments,currMailView OGMail){
        Session session;
        Properties prop=new Properties();
        if (portSent.equals("587")){
            prop.put("mail.smtp.connectiontimeout","10000");
            prop.put("mail.transport.protocol","smtp");
            prop.put("mail.smtp.starttls.enable","true");
            prop.put("mail.smtp.timeout","10000");
            prop.put("mail.smtp.writetimeout","10000");
            prop.put("mail.smtp.host",hostSent);
            prop.put("mail.smtp.port",portSent);
            prop.put("mail.smtp.auth","true");
        }
        else if (portSent.equals("465")){
        prop.put("mail.smtp.host", hostSent);
        prop.put("mail.smtp.port", portSent);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", portSent);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        session=Session.getInstance(prop,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(email,password);
        }});
        try {
            // Message
        // m.setFrom(new InternetAddress(email));
        currMail=new currMailSend(session,prop,email,ToAddress,CCAddress,BCCAddress,
        subj,Textmess,Attachments);
        //TODO make sure old context is added
        Multipart multip=new MimeMultipart();
        // MimeBodyPart ogmail=new MimeBodyPart();
        // ogmail.setContent(m,"message/rfc822");
        // ogmail.setDataHandler(m.getDataHandler());
        // multip.addBodyPart(ogmail);
        // Multipart multip=(Multipart)m.getContent();
        MimeBodyPart messbody=new MimeBodyPart();
        multip.addBodyPart(messbody);
        Textmess=Textmess.concat("\n".concat(OGMail.getQuotedContent()));
        System.out.println(Textmess);
        messbody.setText(Textmess);
        currMail.plainText=Textmess;
        // currMail.mess.setText(Textmess);
        // currMail.plainText=(Textmess);
        for (File i:Attachments){
            MimeBodyPart att=new MimeBodyPart();
            att.attachFile(i);
            multip.addBodyPart(att);
        }
        // currMail.mess.add
        currMail.mess.setContent(multip);
        System.out.println("Succes");
        // currMail.mess.setContent();
        // currMail=new currMailSend(session,prop,email,ToAddress,CCAddress,BCCAddress,
        // subj,Textmess,Attachments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createEmail(String user_mail,
    String ToAddress,String CCAddress,String BCCAddress,
    String subj, String Textmess, List<File>Attachments){
        // System.setProperty("https.protocols", "TLSv1.2");
        // currMailSend curr_mail;
        Session session;
        Properties prop=new Properties();
        if (portSent.equals("587")){
            prop.put("mail.smtp.connectiontimeout","10000");
            prop.put("mail.transport.protocol","smtp");
            prop.put("mail.smtp.starttls.enable","true");
            prop.put("mail.smtp.timeout","10000");
            prop.put("mail.smtp.writetimeout","10000");
            prop.put("mail.smtp.host",hostSent);
            prop.put("mail.smtp.port",portSent);
            prop.put("mail.smtp.auth","true");
        }
        else if (portSent.equals("465")){
        prop.put("mail.smtp.host", hostSent);
        prop.put("mail.smtp.port", portSent);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", portSent);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        session=Session.getInstance(prop,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(email,password);
        }});
        try {
        currMail=new currMailSend(session,prop,email,ToAddress,CCAddress,BCCAddress,
        subj,Textmess,Attachments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createEmail(Message m,currMailView OGMail,String Textmess, List<File>Attachments){
        Session session;
        Properties prop=new Properties();
        if (portSent.equals("587")){
            prop.put("mail.smtp.connectiontimeout","10000");
            prop.put("mail.transport.protocol","smtp");
            prop.put("mail.smtp.starttls.enable","true");
            prop.put("mail.smtp.timeout","10000");
            prop.put("mail.smtp.writetimeout","10000");
            prop.put("mail.smtp.host",hostSent);
            prop.put("mail.smtp.port",portSent);
            prop.put("mail.smtp.auth","true");
        }
        else if (portSent.equals("465")){
        prop.put("mail.smtp.host", hostSent);
        prop.put("mail.smtp.port", portSent);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", portSent);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        session=Session.getInstance(prop,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(email,password);
        }});
        try {
            // Message
        m.setFrom(new InternetAddress(email));
        currMail=new currMailSend(session,m);
        //TODO make sure old context is added
        Multipart multip=new MimeMultipart();
        // MimeBodyPart ogmail=new MimeBodyPart();
        // ogmail.setContent(m,"message/rfc822");
        // ogmail.setDataHandler(m.getDataHandler());
        // multip.addBodyPart(ogmail);
        // Multipart multip=(Multipart)m.getContent();
        MimeBodyPart messbody=new MimeBodyPart();
        multip.addBodyPart(messbody);
        Textmess=Textmess.concat("\n".concat(OGMail.getQuotedContent()));
        messbody.setText(Textmess);
        for (File i:Attachments){
            MimeBodyPart att=new MimeBodyPart();
            att.attachFile(i);
            multip.addBodyPart(att);
        }
        // currMail.mess.add
        // if (multip)
        currMail.mess.setContent(multip);
        System.out.println("Succes");
        // currMail.mess.setContent();
        // currMail=new currMailSend(session,prop,email,ToAddress,CCAddress,BCCAddress,
        // subj,Textmess,Attachments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int sendMail(){
        return currMail.sendMail();
        // currMailSend x=drafts.get(pos);
        // drafts.remove(pos);
        // x.sendMail(pos);
    }
}
