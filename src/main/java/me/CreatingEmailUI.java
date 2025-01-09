package me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class CreatingEmailUI {
    BorderPane layout;
    Node rightmail_copy,originalValue;
    VBox answer;
    Stage mainStage;
    User x;
    currMailSend template=null;
    currMailView ogMail=null;
    int fwdok=0;
    public CreatingEmailUI(BorderPane l,Node n,Stage ms,User us,Node OG){
        layout=l;
        rightmail_copy=n;
        mainStage=ms;
        x=us;
        originalValue=OG;
    }
    public void setReplytoMail(currMailView x){
        ogMail=x;
    }
    public void setTemplate(currMailSend x){
        template=x;
    }
    // public CreatingEmailUI(BorderPane l,Node n,Stage ms,User us,Node OG,){
    //     layout=l;
    //     rightmail_copy=n;
    //     mainStage=ms;
    //     x=us;
    //     originalValue=OG;
    // }
    public void run(){
        execute();
    }
    private void execute(){
        // Stage UI=new Stage();
        // UI.setTitle("Compose Email - "+x.getEmail());
        
        TextField toField=new TextField("");
        toField.setPromptText("");
        TextField CCField=new TextField("");
        CCField.setPromptText("");
        TextField BCCField=new TextField("");
        BCCField.setPromptText("");
        TextField Subject=new TextField("");
        Subject.setPromptText("");
        TextArea messBody=new TextArea();
        messBody.setPromptText("");
        if (template!=null){

        //     try{                
        // MimeMessageParser parser=new MimeMessageParser(template.mess).parse();
        //     if (template.mess.getRecipients(RecipientType.TO)!=null){
        //         for (jakarta.mail.Address i:parser.getTo()){
                    
        //             toField.setText(i.toString());
        //         }
        // }
        //     toField.setEditable(false);
        //     if (template.mess.getRecipients(RecipientType.CC)!=null){
        //         for (jakarta.mail.Address i:parser.getCc()){
        //             toField.setText(i.toString());
        //         }
        //         // CCField.setText(template.mess.getRecipients(RecipientType.CC).toString());
        //     }
        //     CCField.setEditable(false);
        //     if (template.mess.getRecipients(RecipientType.BCC)!=null){
        //         for (jakarta.mail.Address i:parser.getBcc()){
        //             toField.setText(i.toString());
        //         }
        //         // BCCField.setText(template.mess.getRecipients(RecipientType.BCC).toString());
        //     }
        //     BCCField.setEditable(false);
        //     Subject.setText(template.mess.getSubject());
        //     Subject.setEditable(false);
        //     }
        //     catch(IOException e){
        //         e.printStackTrace();
        //     }
        //     catch (MessagingException e){
        //         e.printStackTrace();
        //     }
        }
        Button sendButton=new Button("Send"),cancelButton=new Button("Cancel"),
        attachButton=new Button("Attach files");
        GridPane grid=new GridPane();
        List<File>attachments=new ArrayList<>();
        // grid.setPadding(new Insets(10));
        // grid.setHgap(10);
        // grid.setVgap(10);
        // grid.add(new Label("To: "),0,0);
        // grid.add(toField,1,0);
        // grid.add(new Label("CC: "),0,1);
        // grid.add(CCField,1,1);
        // grid.add(new Label("BCC: "),0,2);
        // grid.add(BCCField,1,2);
        // grid.add(new Label("Subject: "),0,3);
        // grid.add(Subject,1,3);
        // grid.add(new Label("Message Body: "),0,4);
        // grid.add(messBody,1,4);
        VBox UI=new VBox();
        VBox.setVgrow(Subject,Priority.NEVER);
        VBox.setVgrow(BCCField,Priority.NEVER);
        VBox.setVgrow(CCField,Priority.NEVER);
        VBox.setVgrow(toField,Priority.NEVER);
        VBox.setVgrow(messBody,Priority.ALWAYS);
        messBody.autosize();
        if (template==null)
        UI.getChildren().addAll(new Label("To: "),toField,
        new Label("CC: "),CCField,
        new Label("BCC: "),BCCField,
        new Label("Subject: "),Subject,
        new Label("Message Body: "),messBody);
        else
        UI.getChildren().addAll(new Label("Message Body: "),messBody);
        HBox buttonBox=new HBox(10,attachButton,sendButton,cancelButton);
        ListView<File> attachmentListView=new ListView<>();
        attachmentListView.setPrefHeight(0);
        VBox.setVgrow(attachmentListView,Priority.NEVER);
        VBox.setVgrow(buttonBox,Priority.NEVER);
        // attachmentListView.autosize();
        grid.add(buttonBox,1,5);
        attachButton.setOnAction(event->{
            FileChooser filechooser=new FileChooser();
            filechooser.setTitle("Choose files to attach");
            filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files","*.*"));
            List<File> selectedFiles=filechooser.showOpenMultipleDialog(mainStage);
            if (selectedFiles!=null){
                // ListView
                attachments.addAll(selectedFiles);
                attachmentListView.setPrefHeight(35*attachments.size());
                attachmentListView.getItems().addAll(selectedFiles);
            }
        });
        UI.getChildren().addAll(attachmentListView,buttonBox);
        sendButton.setOnAction(event ->{
            if (template!=null){
                String Mess=messBody.getText();
                x.createEmail(template.mess,ogMail,Mess,attachments);
            }
            else if (fwdok==1){
                String to=toField.getText();
                String CC=CCField.getText();
                String BCC=BCCField.getText();
                String Subj=Subject.getText();
                String Mess=messBody.getText();
                if (!to.isBlank() || !CC.isBlank() || !BCC.isBlank()){
                    x.createEmailFWD(x.email,to,CC,BCC,Subj,Mess,attachments,ogMail);
                    // x.createEmail(x.email,to,CC,BCC,Subj,Mess,attachments);        
                }
            }
            else{
            String to=toField.getText();
            String CC=CCField.getText();
            String BCC=BCCField.getText();
            String Subj=Subject.getText();
            String Mess=messBody.getText();
            if (!to.isBlank() || !CC.isBlank() || !BCC.isBlank()){
            x.createEmail(x.email,to,CC,BCC,Subj,Mess,attachments);
            // toBeSend=x.currMail;
        }
            else{
                Stage errorMess=new Stage();
                Label errorL=new Label("Insert at least one receiver");
                errorL.setAlignment(Pos.CENTER);
                Scene sc=new Scene(errorL,200,100);
                errorMess.setScene(sc);
                errorMess.initModality(Modality.APPLICATION_MODAL);
                errorMess.show();
            }
        }
            int sent=x.sendMail();
            if (sent==0){
                Stage succespage=new Stage();
                Label succesL=new Label("Succesfully sent mail");
                succesL.setAlignment(Pos.CENTER);
                Scene sc=new Scene(succesL,200,100);
                succespage.setScene(sc);
                succespage.initModality(Modality.APPLICATION_MODAL);
                succespage.show();
            }
            layout.setRight(originalValue);
        });
        cancelButton.setOnAction(event ->{
            VBox x=(VBox)(layout.getRight());
            System.out.println(x);
            if (template==null){
                layout.setRight(originalValue);
            }
            else
            x.getChildren().removeLast();
        });
        answer=UI;
        // return UI;
        // Scene scene=new Scene(grid,400,400);
        // return scene;
        // mainStage.setScene(scene);
        // mainStage.show();
    }
}

// private VBox CreatingEmailUIx(Stage mainStage,User x){
//     // Stage UI=new Stage();
//     // UI.setTitle("Compose Email - "+x.getEmail());
//     TextField toField=new TextField("");
//     toField.setPromptText("");
//     TextField CCField=new TextField("");
//     CCField.setPromptText("");
//     TextField BCCField=new TextField("");
//     BCCField.setPromptText("");
//     TextField Subject=new TextField("");
//     Subject.setPromptText("");
//     TextArea messBody=new TextArea();
//     messBody.setPromptText("");
//     Button sendButton=new Button("Send"),cancelButton=new Button("Cancel"),
//     attachButton=new Button("Attach files");
//     GridPane grid=new GridPane();
//     List<File>attachments=new ArrayList<>();
//     // grid.setPadding(new Insets(10));
//     // grid.setHgap(10);
//     // grid.setVgap(10);
//     // grid.add(new Label("To: "),0,0);
//     // grid.add(toField,1,0);
//     // grid.add(new Label("CC: "),0,1);
//     // grid.add(CCField,1,1);
//     // grid.add(new Label("BCC: "),0,2);
//     // grid.add(BCCField,1,2);
//     // grid.add(new Label("Subject: "),0,3);
//     // grid.add(Subject,1,3);
//     // grid.add(new Label("Message Body: "),0,4);
//     // grid.add(messBody,1,4);
//     VBox UI=new VBox();
//     UI.getChildren().addAll(new Label("To: "),toField,
//     new Label("CC: "),CCField,
//     new Label("BCC: "),BCCField,
//     new Label("Subject: "),Subject,
//     new Label("Message Body: "),messBody);
//     HBox buttonBox=new HBox(10,attachButton,sendButton,cancelButton);
//     ListView<File> attachmentListView=new ListView();
//     grid.add(buttonBox,1,5);
//     UI.getChildren().addAll(attachmentListView,buttonBox);
//     attachButton.setOnAction(event->{
//         FileChooser filechooser=new FileChooser();
//         filechooser.setTitle("Choose files to attach");
//         filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files","*.*"));
//         List<File> selectedFiles=filechooser.showOpenMultipleDialog(mainStage);
//         if (selectedFiles!=null){
//             attachments.addAll(selectedFiles);
//             attachmentListView.getItems().addAll(selectedFiles);
//         }
//     });
//     sendButton.setOnAction(event ->{
//         String to=toField.getText();
//         String CC=CCField.getText();
//         String BCC=BCCField.getText();
//         String Subj=Subject.getText();
//         String Mess=messBody.getText();
//         x.sendCurrEmail(x.email,to,CC,BCC,Subj,Mess,attachments);
//         layout.setRight(rightmail_copy);
//     });
//     cancelButton.setOnAction(event ->{
//         layout.setRight(null);
//     });
//     return UI;
//     // Scene scene=new Scene(grid,400,400);
//     // return scene;
//     // mainStage.setScene(scene);
//     // mainStage.show();
// }