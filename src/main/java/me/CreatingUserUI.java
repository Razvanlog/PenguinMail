package me;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreatingUserUI {
    Stage mainStage;
    ArrayList<User> UserList;
    public CreatingUserUI(Stage ms,ArrayList<User> ul){
        mainStage=ms;
        UserList=ul;
    }
    public void run(){
        execute();
    }
    public void execute(){
        Stage loginst=new Stage();
        loginst.setTitle("Create Account");
        BorderPane ui=new BorderPane();
        Button Create=new Button("Create");
                Label email=new Label("Email:");
                TextField emailcont=new TextField();
                // emailcont.setPrefHeight(30);
                Label password=new Label("Password:");
                PasswordField passwordcont=new PasswordField();
                Label hortImap=new Label("Incoming mail server:");
                TextField hortImapcont=new TextField();
                Label portImap=new Label("Incoming mail Port");
                TextField portImapcont=new TextField();
                Label hostSent=new Label("Sending mail server:");
                TextField hostSentcont=new TextField();
                Label portSent=new Label("Sending mail Port");
                TextField portSentcont=new TextField();
                Label priorityLabel=new Label("Priority of appereance");
                TextField prioritycont=new TextField();
        VBox infoList=new VBox(10);
        Create.setOnAction(event->{
            try{
            if (emailcont.getText().equals("")||passwordcont.getText().equals("")
            ||hostSentcont.getText().equals("")||portSentcont.getText().equals("")
            ||hortImapcont.getText().equals("")||portImapcont.getText().equals("")){
                throw new emptyAccountException();
            }
            for (User u:UserList){
                if (u.getEmail().equals(emailcont.getText())){
                    throw new entityAlreadyExistsException();
                }
            }
            int prValue=Integer.valueOf(prioritycont.getText());
            if (prValue<0){
                throw(new NumberFormatException());
            }
            User createConn=new User(emailcont.getText(),passwordcont.getText(),hostSentcont.getText(),
                portSentcont.getText(),hortImapcont.getText(),portImapcont.getText(),
                prValue);
                if (createConn.connect()==0){
                try(FileWriter fw=new FileWriter("mail_config",true)){
                    fw.write(createConn.email+","+createConn.password+","
                    +createConn.hostSent+","+createConn.portSent+","+createConn.hostImap
                    +","+createConn.portImap+","+createConn.getPriority()+'\n');
                }
                catch(IOException e){
                    VBox errorBox=new VBox();
                    Label errorL=new Label();
                    errorL.setAlignment(Pos.CENTER);
                    errorL.setText("User could not be saved");
                    errorBox.getChildren().addAll(errorL);
                    Stage errorMess=new Stage();    
                    Scene sc=new Scene(errorBox,300,100);
                    errorMess.setScene(sc);
                    errorMess.initModality(Modality.APPLICATION_MODAL);
                    errorMess.show();
                    e.printStackTrace();
                }
                UserList.add(createConn);
                loginst.close();
                }
                else{
                    VBox errorBox=new VBox();
                    Label errorL=new Label();
                    errorL.setAlignment(Pos.CENTER);
                    errorL.setText("User with specified details cannot be used\ncheck credentials");
                    errorBox.getChildren().addAll(errorL);
                    Stage errorMess=new Stage();    
                    Scene sc=new Scene(errorBox,300,100);
                    errorMess.setScene(sc);
                    errorMess.initModality(Modality.APPLICATION_MODAL);
                    errorMess.show();
                }
                
            // Platform.exit();
            }
            catch(NumberFormatException e){
                VBox errorBox=new VBox();
            Label errorL=new Label();
            errorL.setAlignment(Pos.CENTER);
            errorL.setText("priority order must be\n a positive number");
            errorBox.getChildren().addAll(errorL);
            Stage errorMess=new Stage();    
            Scene sc=new Scene(errorBox,300,100);
            errorMess.setScene(sc);
            errorMess.initModality(Modality.APPLICATION_MODAL);
            errorMess.show();
            }
            catch(entityAlreadyExistsException e){
                Stage emptyFieldWarning=new Stage();
                Label l=new Label("User already exists");
                BorderPane warningbp=new BorderPane();
                warningbp.setCenter(l);
                Scene warningScene=new Scene(warningbp,200,200);
                emptyFieldWarning.setScene(warningScene);
                emptyFieldWarning.show();
                // e.printStackTrace();
            }
            catch(emptyAccountException e){
                Stage emptyFieldWarning=new Stage();
                Label l=new Label("Complete all fields!");
                BorderPane warningbp=new BorderPane();
                warningbp.setCenter(l);
                Scene warningScene=new Scene(warningbp,200,200);
                emptyFieldWarning.setScene(warningScene);
                emptyFieldWarning.show();
                // e.printStackTrace();
            }
        });
        infoList.getChildren().addAll(email,emailcont,password,passwordcont,hortImap,
        hortImapcont,portImap,portImapcont,hostSent,hostSentcont,portSent,portSentcont,priorityLabel,
        prioritycont,Create);
        Scene createacc=new Scene(infoList,500,400);
        loginst.setScene(createacc);
        loginst.show();
    }
}




// public void createUserUI(Stage MainStage,ArrayList<User> UserList){
//     Stage loginst=new Stage();
//     loginst.setTitle("Create Account");
//     BorderPane ui=new BorderPane();
//     Button Create=new Button("Create");
//             Label email=new Label("Email:");
//             TextField emailcont=new TextField();
//             // emailcont.setPrefHeight(30);
//             Label password=new Label("Password:");
//             PasswordField passwordcont=new PasswordField();
//             Label hortImap=new Label("Incoming mail server:");
//             TextField hortImapcont=new TextField();
//             Label portImap=new Label("Incoming mail Port");
//             TextField portImapcont=new TextField();
//             Label hostSent=new Label("Incoming mail server:");
//             TextField hostSentcont=new TextField();
//             Label portSent=new Label("Incoming mail Port");
//             TextField portSentcont=new TextField();
//             Label priorityLabel=new Label("Priority of appereance");
//             TextField prioritycont=new TextField();
//     VBox infoList=new VBox(10);
//     Create.setOnAction(event->{
//         try{
//         if (emailcont.getText().equals("")||passwordcont.getText().equals("")
//         ||hostSentcont.getText().equals("")||portSentcont.getText().equals("")
//         ||hortImapcont.getText().equals("")||portImapcont.getText().equals("")){
//             throw new emptyAccountException();
//         }
//         User createConn=new User(emailcont.getText(),passwordcont.getText(),hostSentcont.getText(),
//             portSentcont.getText(),hortImapcont.getText(),portImapcont.getText(),
//             Integer.valueOf(prioritycont.getText()));
//             if (createConn.connect()==0){
//             try(FileWriter fw=new FileWriter("mail_config",true)){
//                 fw.write(createConn.email+","+createConn.password+","
//                 +createConn.hostSent+","+createConn.portSent+","+createConn.hostImap
//                 +","+createConn.portImap+","+createConn.getPriority()+'\n');
//             }
//             catch(IOException e){
//                 e.printStackTrace();
//             }
//         UserList.add(createConn);
//             }
//             loginst.close();
//         // Platform.exit();
//         }
//         catch(emptyAccountException e){
//             Stage emptyFieldWarning=new Stage();
//             Label l=new Label("Complete all fields!");
//             BorderPane warningbp=new BorderPane();
//             warningbp.setCenter(l);
//             Scene warningScene=new Scene(warningbp,200,200);
//             emptyFieldWarning.setScene(warningScene);
//             emptyFieldWarning.show();
//             e.printStackTrace();
//         }
//     });
//     infoList.getChildren().addAll(email,emailcont,password,passwordcont,hortImap,
//     hortImapcont,portImap,portImapcont,hostSent,hostSentcont,portSent,portSentcont,priorityLabel,
//     prioritycont,Create);
//     Scene createacc=new Scene(infoList,500,400);
//     loginst.setScene(createacc);
//     loginst.show();
// }