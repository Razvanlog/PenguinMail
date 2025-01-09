package me;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;

import jakarta.activation.DataSource;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
public class currMailView extends Mail implements mailViewable{
    
    public currMailView(Message t){
        super(t);
    }
    // public currMailView(){
    //     this.id=null;
    //     this.mess=null;
    //     this.od=null;
    //     this.receivers=null;
    //     this.sender=null;
    //     this.subject=null;
    //     this.mess=null;
    //     this.plainText=null;
    //     this.htmlContent=null;
    //     this.attachments=null;
    // }
    // public currMailView(currMailView t){
    //     this.id=t.id;
    //     this.mess=t.mess;
    //     this.od=t.od;
    //     this.receivers=t.receivers;
    //     this.sender=t.sender;
    //     this.subject=t.subject;
    //     this.mess=t.mess;
    //     this.date=t.date;
    //     this.plainText=t.plainText;
    //     this.htmlContent=t.htmlContent;
    //     this.attachments=t.attachments;
    //     // this.
    // }
    public Message reply(boolean t)throws MessagingException{
        return mess.reply(t);
    }
    @Override
    public String getSender(){
        return sender;
    }
    @Override
    public String getReceivers(){
        String answer="";
        for (Address i: receivers){
            answer=answer+" "+i.toString();
        }
        return answer;
        // return receivers;
    }
    @Override
    public String getSubject(){
        return subject;
    }
    @Override
    public String getContent(){
        String answer="";
        answer=answer+plainText;
        return answer;
        // return mess.getContent();
    }
    public String getHtml(){
        String answer=htmlContent;
        return answer;
    }
    @Override
    public List<DataSource> getAttachments(){
        return attachments;
    }
    @Override
    public String getDate(){
        return date;
    }
    @Override
    public String getCC(){
        String answer="";
        try{
            answer=mess.getRecipients(RecipientType.CC).toString();
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
        return answer;
    }
    private String extractContent() throws Exception {
        // if (mess.isMimeType("text/plain")) {
        //     return mess.getContent().toString();
        // } else if (mess.isMimeType("text/html")) {
        //     return Jsoup.parse(mess.getContent().toString()).text(); // Use Jsoup to strip HTML
        // }
        // return mess.getContent().toString();
        if (htmlContent!=null){
            return Jsoup.parse(htmlContent).text();
        }
        if (plainText!=null)
            return plainText;
        return "Unsupported content type.";
    }
    public String getQuotedContent(){
        String[] lines=null;
        try{
        lines=extractContent().split("\n");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String answer="On ".concat(StylisedSendDate()).concat(" ").concat(sender).concat(" wrote:\n");
        if (lines!=null){
        for (String i: lines){
            answer=answer.concat(i).concat("\n");
        }
        }
        return answer;
    }
    public String StylisedSendDate(){
        String answer="";
        Date currentTime=new Date(System.currentTimeMillis());
        
        // LocalDate ct=currentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // int yearc=ct.getYear();
        // int monthc=ct.getMonthValue();
        // int dayc=ct.getDayOfMonth();
        LocalDate st=sendDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int years=st.getYear();
        int months=st.getMonthValue();
        int days=st.getDayOfMonth();
        // System.out.println(st.getDayOfWeek());
        DayOfWeek dow=st.getDayOfWeek();
        LocalDateTime time=sendDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        answer=answer.concat(st.getDayOfWeek().toString().substring(0,3).toLowerCase()).concat(", ")
        .concat(String.valueOf(days)).concat(" ").concat(Month.of(months).toString().toLowerCase()).concat(" ")
        .concat(String.valueOf(years)).concat(", ").concat(String.valueOf(time.getHour())).concat(":").concat(String.valueOf(time.getMinute()));
        return answer;
    }
    // public void setinputstreamAttList(List<DataSource> v){
        
    // }
    // @Override
    // public int compare(currMailView a,currMailView b){
    //     Date ad=new Date(),bd=new Date();
    //     try{
    //     ad=a.mess.getSentDate();
    //     bd=b.mess.getSentDate();
    //     }
    //     catch(MessagingException e){
    //         e.printStackTrace();
    //     }
    //     if (ad.compareTo(bd)<0)
    //         return 1;
    //     return 0;
    // }
    // @Override
    // public String toString(){
    //     return this.subject;
    // }
}
