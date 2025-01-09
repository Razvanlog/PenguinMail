package me;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.*;
import javafx.scene.control.ListView;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.SortTerm;

import java.util.List;

import jakarta.mail.Flags;
// import jakarta.mail.;
public class Box{
    // private User;
    // List<InputStream> isattlist;
    private int start;
    private SortTerm[] sortTerms;
    private ArrayList<currMailView> storage;
    private String email,password;
    private Session session;
    private Properties prop;
    private String folderType;
    private String name;
    public ArrayList<currMailView> getStorage(){
        return storage;
    }
    public Session getSession(){
        return session;
    }
    public Properties getProp(){
        return prop;
    }
    public String getFolderName(){
        return name;
    }
    // public Box bySubj(){
    //     Box answer=new Box();
    //     answer.session=this.session;
    //     answer.prop=this.prop;
    //     answer.folderType=this.folderType;
    //     Store store=null;
    //     IMAPFolder inbox=null;
    //     // answer.session=this.session;
    //     try {
    //         store=session.getStore("imap");
    //         // System.out.println(a.email+a.password);
    //         store.connect(email,password);
    //         if (store.isConnected()){
    //         FetchProfile fp=new FetchProfile();
    //         fp.add(FetchProfile.Item.ENVELOPE);
    //         fp.add(FetchProfile.Item.CONTENT_INFO);
    //         fp.add(FetchProfile.Item.FLAGS);
    //         // fp.add("")
    //         // System.out.println("Succes");
    //         inbox=(IMAPFolder)store.getFolder(folderType);
    //         inbox.open(Folder.READ_WRITE);
    //         answer.storage=new ArrayList<>();
    //         Message[] buff;
    //         SortTerm[] st;
    //         st=new SortTerm[1];
    //         st[0]=SortTerm.SUBJECT;
    //         // st={SortTerm.ARRIVAL};
    //         buff=inbox.getSortedMessages(st);
    //         // Message[] buff;
    //         // inbox.getSortedMessages
    //         // buff=inbox.getMessages(1,this.length());
    //         // if (inbox.getMessageCount()<20)
    //         // buff=inbox.getMessages(inbox.getMessageCount()-,inbox.getMessageCount());
    //         // else buff=inbox.getMessages(inbox.getMessageCount()-20+1,inbox.getMessageCount());
    //         // inbox.fetch(buff,fp);
    //         // for (Message i : buff){
    //             // System.out.println(i.getContent());
    //             // if (i!=null)
    //                 // answer.storage.add(new currMailView(i));
    //         // }
    //         // answer.storage
    //         // Collections.sort(answer.storage,(a,b)-> a.subject.toLowerCase().compareTo(b.subject.toLowerCase()));
    //         // storage=storage.reversed();
    //         // answer.storage=(ArrayList<currMailView>)answer.storage.reversed();
    //         // Collections.sort(storage);
    //         // storage.sort(Comparator.comparing((currMailView m)->{
    //         //     try {
    //         //         return m.mess.getSentDate();
    //         //     } catch (Exception e) {
    //         //         e.printStackTrace();
    //         //         return null;
    //         //     }
    //         // }).reversed());
    //         System.out.println("Succes");
    //         inbox.close(false);
    //         store.close();
    //         }
    //         else{
    //             System.out.println("FAIL");
    //         }
    //     } catch (MessagingException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     return answer;
    // }
    // Box reversed(){
    //     Box answer=new Box();
    //     answer.session=this.session;
    //     answer.prop=this.prop;
    //     answer.folderType=this.folderType;
    //     Store store=null;
    //     Folder inbox=null;
    //     // answer.session=this.session;
    //     try {
    //         store=session.getStore("imap");
    //         // System.out.println(a.email+a.password);
    //         store.connect(email,password);
    //         if (store.isConnected()){
    //         FetchProfile fp=new FetchProfile();
    //         fp.add(FetchProfile.Item.ENVELOPE);
    //         fp.add(FetchProfile.Item.CONTENT_INFO);
    //         fp.add(FetchProfile.Item.FLAGS);
    //         // fp.add("")
    //         // System.out.println("Succes");
    //         inbox=store.getFolder(folderType);
    //         inbox.open(Folder.READ_WRITE);
    //         answer.storage=new ArrayList<>();
    //         Message[] buff;
    //         buff=inbox.getMessages(1,this.length());
    //         // if (inbox.getMessageCount()<20)
    //         // buff=inbox.getMessages(inbox.getMessageCount()-,inbox.getMessageCount());
    //         // else buff=inbox.getMessages(inbox.getMessageCount()-20+1,inbox.getMessageCount());
    //         inbox.fetch(buff,fp);
    //         for (Message i : buff){
    //             // System.out.println(i.getContent());
    //             currMailView toadd=new currMailView(i);
    //             if (toadd!=null)
    //                 answer.storage.add(toadd);
    //         }
    //         Collections.sort(answer.storage);
    //         // storage.sort(Comparator.comparing((currMailView m)->{
    //         //     try {
    //         //         return m.mess.getSentDate();
    //         //     } catch (Exception e) {
    //         //         e.printStackTrace();
    //         //         return null;
    //         //     }
    //         // }).reversed());
    //         System.out.println("Succes");
    //         inbox.close(false);
    //         store.close();
    //         }
    //         else{
    //             System.out.println("FAIL");
    //         }
    //     } catch (MessagingException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     return answer;
    // }
    public Box(){

    }
    public Box(User a,Session S,Properties P,String fd,String foldername){
        // SortTerm.ARRIVAL
        sortTerms=new SortTerm[3];
        sortTerms[0]=SortTerm.ARRIVAL;
        name=foldername;
        email=a.email;
        password=a.password;
        session=S;
        folderType=fd;
        prop=P;
        Store store=null;
        Folder inbox=null;
        storage=new ArrayList<currMailView>();
        // try {
        //     store=session.getStore("imap");
        //     // System.out.println(a.email+a.password);
        //     store.connect(email,password);
        //     if (store.isConnected()){
        //     FetchProfile fp=new FetchProfile();
        //     fp.add(FetchProfile.Item.ENVELOPE);
        //     fp.add(FetchProfile.Item.CONTENT_INFO);
        //     fp.add(FetchProfile.Item.FLAGS);
        //     // fp.add("")
        //     // System.out.println("Succes");
        //     inbox=store.getFolder(fd);
        //     inbox.open(Folder.READ_WRITE);
        //     storage=new ArrayList<currMailView>();
        //     Message[] buff;
        //     if (inbox.getMessageCount()<20)
        //     buff=inbox.getMessages(1,inbox.getMessageCount());
        //     else buff=inbox.getMessages(inbox.getMessageCount()-20+1,inbox.getMessageCount());
        //     System.out.printf("%d %d\n",Integer.max(inbox.getMessageCount()-storage.size()-20,0),inbox.getMessageCount()-storage.size()-1);
        //     inbox.fetch(buff,fp);
        //     for (Message i : buff){
        //         // System.out.println(i.getContent());
        //         currMailView toadd=new currMailView(i);
        //         if (!storage.contains(toadd))
        //             storage.add(toadd);
        //     }
        //     System.out.println("YES");
        //     Collections.sort(storage);
        //     // storage=(ArrayList<currMailView>)storage.reversed();
        //     // storage=new ArrayList(storage.reversed());
        //     // storage.sort(Comparator.comparing((currMailView m)->{
        //     //     try {
        //     //         return m.mess.getSentDate();
        //     //     } catch (Exception e) {
        //     //         e.printStackTrace();
        //     //         return null;
        //     //     }
        //     // }).reversed());
        //     System.out.println("Succes");
        //     inbox.close(false);
        //     store.close();
        //     }
        //     else{
        //         System.out.println("FAIL");
        //     }
        // } catch (MessagingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
    public int length(){
        return storage.size();
    }
    public void loadMoreItems(){
        int l=length();
        Store store=null;
        IMAPFolder inbox=null;
        try {
            store=session.getStore("imap");
            // System.out.println(a.email+a.password);
            store.connect(email,password);
            if (store.isConnected()){
            FetchProfile fp=new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            fp.add(FetchProfile.Item.FLAGS);
            // fp.add("")
            // System.out.println("Succes");
            inbox=(IMAPFolder)store.getFolder(folderType);
            inbox.open(Folder.READ_WRITE);
            // storage=new ArrayList<currMailView>();
            Message[] buff;
            if (inbox.getMessageCount()>0){
                System.out.println(inbox.getMessageCount());
            System.out.println(l);
            System.out.println();
            if (inbox.getMessageCount()-storage.size()<20)
            buff=inbox.getMessages(1,inbox.getMessageCount()-storage.size());
            else buff=inbox.getMessages(Integer.max(inbox.getMessageCount()-storage.size()-20,1),Integer.max(inbox.getMessageCount()-storage.size(),1));
            System.out.printf("%d %d\n",Integer.max(inbox.getMessageCount()-storage.size()-20,1),Integer.max(inbox.getMessageCount()-storage.size(),1));
            inbox.fetch(buff,fp);
            for (Message i : buff){
                currMailView toadd=new currMailView(i);
                // System.out.println(i.getContent());
                if (i!=null && !storage.contains(toadd)){
                    toadd.setUID(inbox.getUID(i));
                    storage.add(toadd);
                    
                    // toadd.retrieveData();
                    // list.getItems().add(toadd);
                }
            }
            Collections.sort(storage);
            // storage.sort(Comparator.comparing((currMailView m)->{
            //     try {
            //         return m.mess.getSentDate();
            //     } catch (Exception e) {
            //         e.printStackTrace();
            //         return null;
            //     }
            // }));
        }
            System.out.println("Succes");
            inbox.close(false);
            store.close();
            }
            else{
                System.out.println("FAIL");
            }
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public ArrayList<currMailView> searchKeyphrase(String kp){
        ArrayList <currMailView> answer=new ArrayList<currMailView>();
        Store store=null;
        IMAPFolder inbox=null;
        try{
            store=session.getStore("imap");
            // System.out.println(a.email+a.password);
            store.connect(email,password);
            inbox=(IMAPFolder)store.getFolder(folderType);
            inbox.open(Folder.READ_WRITE);
            if (store.isConnected()){
                SearchTerm searchTerm=new BodyTerm(kp);
                Message[] answerList=inbox.search(searchTerm);
                for (Message i:answerList){
                    answer.add(new currMailView(i));
                }
                Collections.sort(answer);
                return answer;
            }
        }
        catch (SearchException e){
            e.printStackTrace();
        }
        catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    } 
    public void loadMoreItems(ListView<currMailView> list){
        int l=length();
        Store store=null;
        Folder inbox=null;
        try {
            store=session.getStore("imap");
            // System.out.println(a.email+a.password);
            store.connect(email,password);
            if (store.isConnected()){
            FetchProfile fp=new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            fp.add(FetchProfile.Item.FLAGS);
            // fp.add()
            // fp.add("")
            // System.out.println("Succes");
            inbox=store.getFolder(folderType);
            inbox.open(Folder.READ_WRITE);
            // storage=new ArrayList<currMailView>();
            Message[] buff;
            if (inbox.getMessageCount()>list.getItems().size()){
                System.out.println(inbox.getMessageCount());
            System.out.println(l);
            System.out.println();
            if (inbox.getMessageCount()-storage.size()<20)
            buff=inbox.getMessages(1,inbox.getMessageCount()-storage.size());
            else buff=inbox.getMessages(Integer.max(inbox.getMessageCount()-storage.size()-20,1),Integer.max(inbox.getMessageCount()-storage.size(),1));
            System.out.printf("%d %d\n",Integer.max(inbox.getMessageCount()-storage.size()-20,1),Integer.max(inbox.getMessageCount()-storage.size(),1));
            inbox.fetch(buff,fp);
            for (Message i : buff){
                currMailView toadd=new currMailView(i);
                // System.out.println(i.getContent());
                if (i!=null && !storage.contains(toadd)){
                    storage.add(toadd);
                    list.getItems().add(toadd);
                }
            }
            Collections.sort(storage);
            // storage.sort(Comparator.comparing((currMailView m)->{
            //     try {
            //         return m.mess.getSentDate();
            //     } catch (Exception e) {
            //         e.printStackTrace();
            //         return null;
            //     }
            // }));
        }
            System.out.println("Succes");
            inbox.close(false);
            store.close();
            }
            else{
                System.out.println("FAIL");
            }
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // public currMailSend getReplyMail(currMailView mail,boolean type){
    //     currMailSend answer=null;
    //     Store store=null;
    //     IMAPFolder inbox=null;
    //     try {
    //         store=session.getStore("imap");
    //         // System.out.println(a.email+a.password);
    //         store.connect(email,password);
    //         if (store.isConnected()){
    //             // mail.retrieveData();
    //             inbox=(IMAPFolder)store.getFolder(folderType);
    //             inbox.open(Folder.READ_WRITE);
    //             Message fetched=inbox.getMessageByUID(mail.UID);
    //             Message reply;
    //             mail.retrieveData(fetched);
    //             reply=fetched.reply(type);
    //             System.out.println(fetched);
    //             // mail.retrieveData(fetched);
    //             // fetched.getContent();
    //             answer=new currMailSend(reply);
    //             // System.out.println(mail.UID);
    //             // mail=new currMailView(fetched);
    //             System.out.println(mail.receivers);
    //             System.out.println("reply mailed returned");
    //         }
    //         if (inbox!=null){
    //         inbox.close();
    //         }
    //         store.close();
    //     }catch(MessagingException e){
    //         e.printStackTrace();
    //     }
    //     return answer;
    // }
    public void retrieveData(currMailView mail){
        Store store=null;
        IMAPFolder inbox=null;
        try {
            store=session.getStore("imap");
            // System.out.println(a.email+a.password);
            store.connect(email,password);
            if (store.isConnected()){
                // mail.retrieveData();
                inbox=(IMAPFolder)store.getFolder(folderType);
                inbox.open(Folder.READ_ONLY);
                Message fetched=inbox.getMessageByUID(mail.UID);
                // System.out.println(mail.UID);
                // mail=new currMailView(fetched);
                mail.retrieveData(fetched);
                System.out.println(mail.receivers);
                System.out.println("RETRIEVED");
            }
            if (inbox!=null){
            inbox.close();
            }
            store.close();
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }
    public void markSeen(currMailView mail){
        IMAPStore store=null;
        IMAPFolder inbox=null;
        try {
            store=(IMAPStore)session.getStore("imap");
            // System.out.println(a.email+a.password);
            store.connect(email,password);
            if (store.isConnected()){
                // mail.retrieveData();
                inbox=(IMAPFolder)store.getFolder(folderType);
                inbox.open(Folder.READ_WRITE);
                Message fetched=inbox.getMessageByUID(mail.UID);
                if (fetched!=null){
                mail.mess.setFlag(Flags.Flag.SEEN,true);
                fetched.setFlag(Flags.Flag.SEEN,true);
                inbox.setFlags(new int[]{(int)mail.UID},new Flags(Flags.Flag.SEEN),true);
                }
                // fetched.setFlag(jakarta.mail.Flags.Flag.SEEN,true);
                // System.out.println("set as seen");
                // System.out.println(mail.UID);
                // mail=new currMailView(fetched);
                // mail.retrieveData(fetched);
                // System.out.println(mail.receivers);
                // System.out.println("RETRIEVED");
            }
            if (inbox!=null){
            inbox.close();
            }
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void markForDelete(currMailView mail){
        IMAPStore store=null;
        IMAPFolder inbox=null;
        try {
            store=(IMAPStore)session.getStore("imap");
            // System.out.println(a.email+a.password);
            store.connect(email,password);
            if (store.isConnected()){
                // mail.retrieveData();
                inbox=(IMAPFolder)store.getFolder(folderType);
                inbox.open(Folder.READ_WRITE);
                Message fetched=inbox.getMessageByUID(mail.UID);
                if (fetched!=null){
                mail.mess.setFlag(Flags.Flag.DELETED,true);
                fetched.setFlag(Flags.Flag.DELETED,true);
                inbox.setFlags(new int[]{(int)mail.UID},new Flags(Flags.Flag.DELETED),true);
                storage.remove(mail);
                }
                // fetched.setFlag(jakarta.mail.Flags.Flag.SEEN,true);
                // System.out.println("set as seen");
                // System.out.println(mail.UID);
                // mail=new currMailView(fetched);
                // mail.retrieveData(fetched);
                // System.out.println(mail.receivers);
                // System.out.println("RETRIEVED");
            }
            if (inbox!=null){
            inbox.close(true);
            }
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void printTerminal(){
        if (storage==null){
            System.out.println("no inbox");
        }
        else{
        //     try{
        //     for (currMailView message :storage){
        //         System.out.println("Subject: "+message.sender);
        //         System.out.println("From: "+message.receivers);
        //         System.out.println("Send date: "+message.);
        //         System.out.println("Content: "+message.getContent());
        //         System.out.println("");
        //     }
        // }catch(Exception e){
        //     e.printStackTrace();
        // }
        }
    }
}
