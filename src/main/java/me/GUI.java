package me;
//to run
// mvn compile
//mvn javafx:run

// import java.util.Observable;
import java.awt.desktop.UserSessionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeoutException;

import javafx.scene.control.*;

import org.w3c.dom.*;

import java.util.Map;
import java.util.HashMap;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.swing.text.Document;
import javax.xml.parsers.*;

import com.sun.jdi.IntegerValue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javafx.scene.control.ChoiceBox;
import jakarta.activation.DataSource;
import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
public class GUI extends Application{
    User user;
    BorderPane layout=new BorderPane();
    BorderPane inboxLayout_copy;
    Task<Void> boxObtainer;
    Node centerlist_copy,
    originalValue,
    rightmail_copy;
    int defaultUser=0;
    private int checkStrInt(String x){
        int answer=0;
        try {
            answer=Integer.valueOf(x);
            answer=0;
        } catch (NumberFormatException e) {
            answer=1;
            System.out.println("cannot format %s into integer\ncontinuing with no arguments".formatted(x));
        }
        return answer;
    }
    @Override
    public void start(Stage primaryStage){
        List<String> args=getParameters().getRaw();
        if (!args.isEmpty()){
        if(checkStrInt(args.get(0))==0){
            try {
                defaultUser=Integer.valueOf(args.get(0));
            } catch (NumberFormatException e) {
                defaultUser=0;
            }
        }
    }
    else defaultUser=0;
        File configfd=new File("mail_config");
        // if (configfd.canRead()==true){
        //     System.out.println("yes");
        // }
        // System.out.println("YESYES");
        if (configfd.exists()==false){
            try{
            configfd.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        ArrayList<User> users=new ArrayList<User>();
        try{
            // DocumentBuilderFactory dbfac=DocumentBuilderFactory.newInstance();
            // DocumentBuilder dbB=dbfac.newDocumentBuilder();
            // Document d=dbB.newDocument();
            FileInputStream configfis=new FileInputStream(configfd);
            InputDevice id=new InputDevice(configfis);
            
            String s;
            while ((s=id.read())!=""){
                String[] splitter=s.split(",");
                if (splitter.length==7){
                User x=new User(splitter[0],splitter[1],splitter[2],splitter[3],splitter[4],splitter[5],Integer.valueOf(splitter[6]));
                if (x.connect()==0){
                    users.add(x);
                        String URL="jdbc:mysql://localhost:3306/JAVAPROJECT";
                        String dbuser="java";
                        // String dbpassword="6-4FLnJZeUX2gFs";
                        String dbpassword="java";
                        // String insertQuery="INSERT INTO CONTACTS (name, lastname,phonenumber, business, description, priorityvalue, emailID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        String searchQuery="SELECT emailID, emailaddress from USERS where emailaddress='"+splitter[0]+"'";
                        Connection conn=null;
                        int userid=0;
                        try {
                            conn=DriverManager.getConnection(URL,dbuser,dbpassword);
                            Statement statm=conn.createStatement();
                            ResultSet rs=statm.executeQuery(searchQuery);
                        if (rs.next()==false){
                        String insertst="insert into USERS (emailaddress)"+"values ('"+splitter[0]+"')";
                        PreparedStatement ps=conn.prepareStatement(insertst);
                        // ps.setString(1,user.email);
                        int rows=ps.executeUpdate();
                        rs=statm.executeQuery(searchQuery);
                        if (rs.next()!=false)
                        userid=rs.getInt("emailID");
                    // System.out.printf("%d added\n",rows);
                        }
                        else{
                            userid=rs.getInt("emailID");
                    // System.out.println(rs.getString("emailaddress"));
                        }
                        String queryconf="select portsent, hostsent, portimap, hostimap from CONFIG where mailID="+String.valueOf(userid);
                        Statement qcstat=conn.createStatement();
                        ResultSet confrs=qcstat.executeQuery(queryconf);
                        if (confrs.next()==false){
                            String insertconf="insert into CONFIG (portsent, hostsent, portimap, hostimap) values "+
                            "('"+splitter[3]+"','"+splitter[2]+"','"+splitter[5]+"','"+splitter[4]+"')";
                            PreparedStatement ps=conn.prepareStatement(insertconf);
                // // ps.setString(1,user.email);
                int rows=ps.executeUpdate();
                System.out.println("succes "+String.valueOf(rows));
                // rs=statm.executeQuery(searchQuery);
                        }
                        else{
                            // String portsent=confrs.getString("portsent"),
                            // hostsent=confrs.getString("hostsent"),
                            // portimap=confrs.getString("portimap"),
                            // hostimap=confrs.getString("hostimap");
                            // User t=new User(splitter[0],splitter[1],hostsent,portsent,portimap,hostimap,10);
                        }
                // String insertcontact="insert into CONTACTS (name, lastname, phonenumber, business, description, priorityvalue, mailaddress,emailID)"+
                // " values ('"+contact.getName()+"','"+contact.getFamilyName()+"','"+contact.getPhoneNumber()+"','"+contact.getBusiness()+"','"+contact.getDescription()+"',"+String.valueOf(contact.getPriority())+
                // ",'"+contact.getEmail()+"','"+String.valueOf(id)+"')";
                // System.out.println(insertcontact);
                // PreparedStatement ps=conn.prepareStatement(insertcontact);
                // int rsw=ps.executeUpdate();
                // System.out.printf("written %d lines to db contacts",rsw);
                // conn.close();   
            }
            // finally{
            //   }
            catch (SQLException e){
                e.printStackTrace();
            }
                }
                else{
                    Stage couldNotConnectScreen;
                    BorderPane sc=new BorderPane();
                    Label luser=new Label("Could not connect: "+splitter[0]);
                    sc.setCenter(luser);
                    Scene toShowErrorConnection=new Scene(sc,300,200);
                    couldNotConnectScreen=new Stage();
                    couldNotConnectScreen.setScene(toShowErrorConnection);
                    // couldNotConnectScreen.initModality(Modality.APPLICATION_MODAL);
                    couldNotConnectScreen.show();
                }
            }
            else if (splitter.length>=2){
                String URL="jdbc:mysql://localhost:3306/JAVAPROJECT";
                String dbuser="java";
                // String dbpassword="6-4FLnJZeUX2gFs";
                String dbpassword="java";
                // String insertQuery="INSERT INTO CONTACTS (name, lastname,phonenumber, business, description, priorityvalue, emailID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                String searchQuery="SELECT emailID, emailaddress from USERS where emailaddress='"+splitter[0]+"'";
                Connection conn=null;
                int userid=0;
                try {
                    conn=DriverManager.getConnection(URL,dbuser,dbpassword);
                    Statement statm=conn.createStatement();
                    ResultSet rs=statm.executeQuery(searchQuery);
                if (rs.next()==false){
                String insertst="insert into USERS (emailaddress)"+"values ('"+user.email+"')";
                PreparedStatement ps=conn.prepareStatement(insertst);
                // ps.setString(1,user.email);
                int rows=ps.executeUpdate();
                rs=statm.executeQuery(searchQuery);
                if (rs.next()!=false)
                userid=rs.getInt("emailID");
            // System.out.printf("%d added\n",rows);
                }
                else{
                    userid=rs.getInt("emailID");
            // System.out.println(rs.getString("emailaddress"));
                }
                String queryconf="select portsent, hostsent, portimap, hostimap from CONFIG where mailID="+String.valueOf(userid);
                Statement qcstat=conn.createStatement();
                ResultSet confrs=qcstat.executeQuery(queryconf);
                if (confrs.next()==false){

                }
                else{
                    String portsent=confrs.getString("portsent"),
                    hostsent=confrs.getString("hostsent"),
                    portimap=confrs.getString("portimap"),
                    hostimap=confrs.getString("hostimap");
                    User t=new User(splitter[0],splitter[1],hostsent,portsent,hostimap,portimap,10);
                    // System.out.println("yes");
                    if (t.connect()==0){
                        // System.out.println("yes");
                        users.add(t);
                    }
                }
        // String insertcontact="insert into CONTACTS (name, lastname, phonenumber, business, description, priorityvalue, mailaddress,emailID)"+
        // " values ('"+contact.getName()+"','"+contact.getFamilyName()+"','"+contact.getPhoneNumber()+"','"+contact.getBusiness()+"','"+contact.getDescription()+"',"+String.valueOf(contact.getPriority())+
        // ",'"+contact.getEmail()+"','"+String.valueOf(id)+"')";
        // System.out.println(insertcontact);
        // PreparedStatement ps=conn.prepareStatement(insertcontact);
        // int rsw=ps.executeUpdate();
        // System.out.printf("written %d lines to db contacts",rsw);
        // conn.close();   
    }
    // finally{
    //   }
    catch (SQLException e){
        e.printStackTrace();
    }
            }
                // users.add()
                // System.out.println(splitter[0]+"\n"+splitter[1]);
            }
            Collections.sort(users);
            // users.sort();
            // System.out.println(id.read());
            // System.out.println(id.read());
            // System.out.println(id.read());
        }
        catch(FileNotFoundException e){
            try{
            configfd.createNewFile();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        primaryStage.setTitle("Email Application");
        // System.out.println(args.get(0));
        if (users.isEmpty() || (!args.isEmpty() && args.get(0).equals("login"))){
        GridPane gridPane=new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label userNameLabel=new Label("email:");
        TextField userNameField=new TextField();
        gridPane.add(userNameLabel,0,0);
        gridPane.add(userNameField,1,0);
        Label passwordLabel=new Label("password:");
        PasswordField passwordField=new PasswordField();
        gridPane.add(passwordLabel,0,1);
        gridPane.add(passwordField,1,1);
        Label hostImapLabel=new Label("Inbox Server:");
        TextField hostImap=new TextField();
        gridPane.add(hostImapLabel,0,2);
        gridPane.add(hostImap,1,2);
        Label portImapLabel=new Label("Port Inbox Server:");
        TextField portImap=new TextField();
        gridPane.add(portImapLabel,0,3);
        gridPane.add(portImap,1,3);
        Label hostSentLabel=new Label("Sending server:");
        TextField hostSent=new TextField();
        gridPane.add(hostSentLabel,0,4);
        gridPane.add(hostSent,1,4);
        Label portSentLabel=new Label("Port Sending Server:");
        TextField portSent=new TextField();
        gridPane.add(portSentLabel,0,5);
        gridPane.add(portSent,1,5);
        Button loginButton=new Button("Login");
        gridPane.add(loginButton,1,6);
        Label errorMessage=new Label();
        
        // errorMessage.setTextFill(Color.RED);
        // errorMessage.setTextFill(Color.RED);
        gridPane.add(errorMessage,1,7);
        loginButton.setOnAction(e->{    
            String username=userNameField.getText();
            String password=passwordField.getText();
            String hostImapserver=hostImap.getText();
            String hostSendserver=hostSent.getText();
            user=new User(username,password,hostSendserver,"576",hostImapserver,"993",2147483647);
            switch (user.connect()) {
            case 0:
                int connStatus=user.connect();
                if (connStatus==0){
                    users.add(user);
                    DefaultUI(primaryStage,users);
                    // StackPane mainLayout=new StackPane();
                }
                // JOptionPane.showMessageDialog(frame,"login succes!");
                break;
            case 1:
                errorMessage.setText("Credentials are not correct!");
                // JOptionPane.showMessageDialog(frame, "Credentials are incorrect!");
                break;
            case 2:
                errorMessage.setText("Check server settings");
                // JOptionPane.showMessageDialog(frame, "Check server settings :/");
                break;
            default:
                throw new AssertionError();
                }
                // else JOptionPane.showMessageDialog(frame,"Try again!");
        });
        Scene scene=new Scene(gridPane,300,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        }
        else{
            // GridPane gridPane=new GridPane();
            // Label Loading=new Label("Loading...");
            // gridPane.add(Loading,0,0);
            // Scene scene=new Scene(gridPane,300,200);
            // primaryStage.setScene(scene);
            // primaryStage.show();
            DefaultUI(primaryStage,users);
        }
    }
    // public Task<Void> createEmailFetchTask(User x){
    //     return new Task<Void>(){
    //         @Override
    //         protected Void call()throws Exception{
    //             Platform.runLater(()->{
    //             // x.inbox=x.getInbox();
    //         });
    //             return null;
    //         }
    //     };
    // }
    public void DefaultUI(Stage mainStage, ArrayList<User> UserList){
        // User x=UserList.get(0);
        Stage UI=new Stage();
        UI.setTitle("Penguin Mail");
        // Label 
        
        while (defaultUser<0){
            defaultUser+=UserList.size();
        }
        if (defaultUser>UserList.size()){
            defaultUser=defaultUser%UserList.size();
        }
        Map<String,Integer> quickId=new HashMap();
        ChoiceBox<String> chooseEmail=new ChoiceBox<>();
        for (int i=0; i<UserList.size(); i++){
            User t=UserList.get(i);
            chooseEmail.getItems().add(t.getEmail());
            quickId.put(t.getEmail(),i);
        }
        System.out.println("YES");
        chooseEmail.setValue(UserList.get(defaultUser).getEmail());
        // layout.getChildren().add(chooseEmail);   
chooseEmail.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{     
        User x=UserList.get(quickId.get(newValue));
        x.getFolders();
        Button compose=new Button("Compose"),
        refresh_mail=new Button("Refresh"),
        settingsoption=new Button("Settings"),
        contactsbutton=new Button("Contacts"),
        aboutProgram=new Button("About"),
        exitButton=new Button("Exit");
        compose.setPrefWidth(100);
        compose.setPrefHeight(50);
        refresh_mail.setPrefWidth(100);
        refresh_mail.setPrefHeight(50);
        settingsoption.setPrefWidth(100);
        settingsoption.setPrefHeight(50);
        exitButton.setPrefWidth(100);
        exitButton.setPrefHeight(50);
        // VBox TopBar=new VBox(10);
        contactsbutton.setPrefWidth(100);
        contactsbutton.setPrefHeight(50);
        aboutProgram.setPrefWidth(100);
        aboutProgram.setPrefHeight(50);
        
        HBox menu=new HBox(50,chooseEmail,compose,refresh_mail,settingsoption
        ,contactsbutton,aboutProgram,exitButton);
        // HBox listViewOptions=new HBox(10);
        // TopBar.getChildren().add(menu);
        layout.setTop(menu);
        
            System.out.println(x.getEmail());
            VBox emailMenu=new VBox(10);
        Label email=new Label(x.getEmail());
        email.setTextAlignment(TextAlignment.CENTER);
        email.setStyle("-fx-font-weight: bold;");
        emailMenu.getChildren().addAll(email);
        // email.setEditable(false);
        Button 
        // compose=new Button("Compose"),
        inbox=new Button("Inbox"),
        sentbox=new Button("Sent"),
        spambox=new Button("Spam"),
        // refresh_mail=new Button("Refresh"),
        draftbox=new Button("Drafts");
        // settingsoption=new Button("Settings");
        // compose.setPrefWidth(100);
        // compose.setPrefHeight(50);
        // refresh_mail.setPrefWidth(100);
        // refresh_mail.setPrefHeight(50);
        // settingsoption.setPrefWidth(100);
        // settingsoption.setPrefHeight(50);
        inbox.setPrefWidth(100);
        sentbox.setPrefWidth(100);
        spambox.setPrefWidth(100);
        draftbox.setPrefWidth(100);
        // emailMenu.getChildren().addAll(inbox,sentbox,spambox,draftbox);
        // int index=0;
        VBox email_options=new VBox();
        for (int i=0; i<x.boxFolders.size(); i++){
            Box box=x.boxFolders.get(i);
            System.out.println(box.getFolderName());
            Button newOption=new Button(box.getFolderName());
            newOption.setPrefWidth(150);
            newOption.setOnAction(event ->{
                layout.setCenter(null);
            // layout.setLeft(null);
            layout.setBottom(null);
            // layout.setTop(null);
            // layout.setRight(null);
            // Task<Void> BoxObtainer;
            if (boxObtainer!=null && boxObtainer.isRunning()){
                boxObtainer.cancel();
            }
            boxObtainer=new Task<Void>(){
            @Override
            public Void call(){
                if (isCancelled())
                    return null;
                BoxUI boxuiExecute=new BoxUI(mainStage, box,layout);
                boxuiExecute.setOwner(x);
                if (isCancelled())
                    return null;
                boxuiExecute.run();
                return null;
            }
            };
            boxObtainer.setOnCancelled(WorkerStateEvent->{
                System.out.println("Cancelled");
            });
            boxObtainer.setOnSucceeded(WorkerStateEvent->{
                System.out.println("Succes");
            });
            boxObtainer.run();
            // BorderPane inboxLayout=boxuiExecute.answer;
            // Node centerlist=inboxLayout.getLeft(),
            // rightmail=inboxLayout.getCenter();
            // originalValue=rightmail;
            // layout.setCenter(centerlist);
            // layout.setRight(rightmail);
            });
            email_options.getChildren().add(newOption);
            // index=index+1;
        }        
        // (10,inbox,sentbox,draftbox,spambox);
        email_options.setPrefWidth(100);
        emailMenu.getChildren().addAll(email_options);
        layout.setLeft(email_options);
        // System.out.println("Tring box ui");
        BoxUI bui=new BoxUI(mainStage,x.boxFolders.get(0),layout);
        bui.setOwner(x);
        // System.out.println("Tried box ui");
        bui.run();
        // inboxLayout_copy=bui.answer;
        // // inboxLayout_copy=InBoxUI(mainStage, x);
        // centerlist_copy=inboxLayout_copy.getLeft();
        // rightmail_copy=inboxLayout_copy.getCenter();
        // layout.setCenter(centerlist_copy);
        // layout.setRight(rightmail_copy);
        exitButton.setOnAction(event->Platform.exit());

        settingsoption.setOnAction(event ->{
            optionsMenu(mainStage,UserList);
        });
        compose.setOnAction(event->{
            layout.setBottom(null);
            originalValue=layout.getRight();
            // System.out.println(originalValue);
            CreatingEmailUI ceUI=new CreatingEmailUI(layout,rightmail_copy,mainStage, x,originalValue);
            ceUI.run();
            VBox ce=ceUI.answer;
            layout.setRight(ce);
        });
        contactsbutton.setOnAction(event->{
            contactsMenu cmenu=new contactsMenu();
            cmenu.user=x;
            cmenu.run();
            BorderPane copy=cmenu.answer;
            layout.setCenter(copy.getLeft());
            layout.setRight(copy.getCenter());
        });
        layout.setLeft(email_options);
        // Scene scene=new Scene(layout,600,400); 
        // mainStage.setScene(scene);
        // mainStage.show();
    });
    // BorderPane layout_first=new Bor;
    User x=UserList.get(defaultUser);
    x.getFolders();
        Button compose=new Button("Compose"),
        refresh_mail=new Button("Refresh"),
        settingsoption=new Button("Settings"),
        contactsbutton=new Button("Contacts"),
        aboutProgram=new Button("About"),
        exitButton=new Button("Exit");
        compose.setPrefWidth(100);
        compose.setPrefHeight(50);
        refresh_mail.setPrefWidth(100);
        refresh_mail.setPrefHeight(50);
        settingsoption.setPrefWidth(100);
        settingsoption.setPrefHeight(50);
        exitButton.setPrefWidth(100);
        exitButton.setPrefHeight(50);
        contactsbutton.setPrefWidth(100);
        contactsbutton.setPrefHeight(50);
        aboutProgram.setPrefWidth(100);
        aboutProgram.setPrefHeight(50);
        HBox menu=new HBox(50,chooseEmail,compose,refresh_mail,settingsoption
        ,contactsbutton,aboutProgram,exitButton);
        layout.setTop(menu);
        System.out.println(x.getEmail());
        VBox emailMenu=new VBox(10);
        Label email=new Label(x.getEmail());
        email.setTextAlignment(TextAlignment.CENTER);
        email.setStyle("-fx-font-weight: bold;");
        emailMenu.getChildren().addAll(email);
        // email.setEditable(false);
        // Button 
        // compose=new Button("Compose"),
        // inbox=new Button("Inbox"),
        // sentbox=new Button("Sent"),
        // spambox=new Button("Spam"),
        // draftbox=new Button("Drafts");
        // inbox.setPrefWidth(100);
        // sentbox.setPrefWidth(100);
        // spambox.setPrefWidth(100);
        // draftbox.setPrefWidth(100);
        // emailMenu.getChildren().addAll(inbox,sentbox,spambox,draftbox);
        VBox email_options=new VBox(0);
        
        for (int i=0; i<x.boxFolders.size(); i++){
            Box box=x.boxFolders.get(i);
            System.out.println(box.getFolderName());
            Button newOption=new Button(box.getFolderName());
            newOption.setPrefWidth(150);
            newOption.setOnAction(event ->{
                layout.setCenter(null);
            // layout.setLeft(null);
            layout.setBottom(null);
            // layout.setTop(null);
            // layout.setRight(null);
            BoxUI boxuiExecute=new BoxUI(mainStage, box,layout);
            boxuiExecute.setOwner(x);
            boxuiExecute.run();
            // BorderPane inboxLayout=boxuiExecute.answer;
            // Node centerlist=inboxLayout.getLeft(),
            // rightmail=inboxLayout.getCenter();
            // layout.setCenter(centerlist);
            // layout.setRight(rightmail);
            // originalValue=rightmail;
            });
            email_options.getChildren().add(newOption);
            // index=index+1;
        }
        email_options.setPrefWidth(100);
        emailMenu.getChildren().addAll(email_options);
        layout.setLeft(email_options);
        BoxUI bui=new BoxUI(mainStage,x.boxFolders.get(0),layout);
        bui.setOwner(x);
        bui.run();
        // System.out.println("YES");
        // inboxLayout_copy=bui.answer;
        // // inboxLayout_copy=InBoxUI(mainStage, x);
        // centerlist_copy=inboxLayout_copy.getLeft();
        // rightmail_copy=inboxLayout_copy.getCenter();
        // layout.setCenter(centerlist_copy);
        // layout.setRight(rightmail_copy);
        compose.setOnAction(event->{
            // layout.setCenter(null);
            // layout.setLeft(null);
            layout.setBottom(null);
            // layout.setTop(null);
            // layout.setRight(null);
            
            originalValue=layout.getRight();
            // System.out.println(originalValue);
            CreatingEmailUI ceUI=new CreatingEmailUI(layout,rightmail_copy, mainStage, x,originalValue);
            ceUI.run();
            VBox ce=ceUI.answer;
            layout.setRight(ce);
        });
        contactsbutton.setOnAction(event->{
            contactsMenu cmenu=new contactsMenu();
            cmenu.user=x;
            cmenu.run();
            BorderPane copy=cmenu.answer;
            layout.setCenter(copy.getLeft());
            layout.setRight(copy.getCenter());
        });
        exitButton.setOnAction(event->Platform.exit());
        // inbox.setOnAction(event->{
        //     layout.setCenter(null);
        //     // layout.setLeft(null);
        //     layout.setBottom(null);
        //     // layout.setTop(null);
        //     // layout.setRight(null);
        //     BorderPane inboxLayout=InBoxUI(mainStage, x);
        //     Node centerlist=inboxLayout.getLeft(),
        //     rightmail=inboxLayout.getCenter();
        //     layout.setCenter(centerlist);
        //     layout.setRight(rightmail);
        // });
        settingsoption.setOnAction(event ->{
            optionsMenu(mainStage,UserList);
        });
        // sentbox.setOnAction(event->{
        //     layout.setCenter(null);
        //     layout.setBottom(null);
        //     BorderPane sentlayout;
            
        //     // layout.setRight(null);
        // });
        layout.setLeft(email_options);
        Scene scene=new Scene(layout,1000,800); 
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();
    }
    public void optionsMenu(Stage MainStage, ArrayList<User> UserList){
        Stage optionsMenu=new Stage();
            optionsMenu.setTitle("Settings");
            optionsMenu.initModality(Modality.APPLICATION_MODAL);
            System.out.println(UserList.size());
            VBox leftOptions=new VBox(10);
            Button addUser=new Button("Add User");
            ListView<User> usersListView=new ListView<>(FXCollections.observableArrayList(UserList));
        usersListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>(){
            @Override
            public ListCell<User> call(ListView<User> listView){
                return new ListCell<User>(){
                @Override
                protected void updateItem(User user,boolean empty){
                    super.updateItem(user,empty);
                    if (user!=null && !empty){
                        VBox toDisplay=new VBox(0);
                        Label email=new Label(user.email);
                        toDisplay.getChildren().add(email);
                        setGraphic(toDisplay);
                    }else{
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            }
        });
        VBox userOptionsBox=new VBox(0);
        usersListView.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldvalue,selectedUser) -> {
                
            if (selectedUser!=null){
                Button save=new Button("Save");
                Label email=new Label("Email:");
                TextField emailcont=new TextField(selectedUser.email);
                emailcont.setPrefHeight(30);
                Label password=new Label("Password:");
                PasswordField passwordcont=new PasswordField();
                Label hortImap=new Label("Incoming mail server:");
                TextField hortImapcont=new TextField(selectedUser.getHostImap());
                Label portImap=new Label("Incoming mail Port");
                TextField portImapcont=new TextField(selectedUser.getportImap());
                Label hostSent=new Label("Incoming mail server:");
                TextField hostSentcont=new TextField(selectedUser.getHostSent());
                Label portSent=new Label("Incoming mail Port");
                TextField portSentcont=new TextField(selectedUser.getportSent());
                Label priorityLabel=new Label("Priority of appereance");
                TextField prioritycont=new TextField(String.valueOf(selectedUser.getPriority()));
                // passwordcont.setPrefHeight(30);
                // hortImapcont.setPrefHeight(30);
                // portImapcont.setPrefHeight(30);
                // hostSentcont.setPrefHeight(30);
                // portSentcont.setPrefHeight(30);
                passwordcont.setText(selectedUser.password);
                userOptionsBox.getChildren().clear();
                userOptionsBox.getChildren().addAll(email,emailcont,password,
                passwordcont,hortImap,hortImapcont,portImap
                ,portImapcont,hostSent,hostSentcont,portSent,portSentcont,
                priorityLabel,prioritycont,save);
                System.out.println("("+emailcont.getText().toString()+")");
                save.setOnAction(event->{
                    try{
                        
                System.out.println("("+emailcont.getText().toString()+")");
                if (emailcont.getText().equals("")||passwordcont.getText().equals("")
            ||hostSentcont.getText().equals("")||portSentcont.getText().equals("")
            ||hortImapcont.getText().equals("")||portImapcont.getText().equals("")){
                throw new emptyAccountException();
            }
                User testConn=new User(emailcont.getText(),passwordcont.getText(),hostSentcont.getText(),
                portSentcont.getText(),hortImapcont.getText(),portImapcont.getText(),
                Integer.valueOf(prioritycont.getText()));
                // if (testConn.getEmail().equals("")){
                //     throw new emptyAccountException();
                // }
                if (testConn.connect()==0){
                    // selectedUser=testConn;
                    selectedUser.setEmail(testConn.getEmail());
                    selectedUser.setpasskey(testConn.getpasskey());
                    selectedUser.setHostImap(testConn.getHostImap());
                    selectedUser.setportImap(testConn.getportImap());
                    selectedUser.sethostSent(testConn.getHostSent());
                    selectedUser.setportSent(testConn.getportSent());
                    selectedUser.setPriority(testConn.getPriority());
                    System.out.println("can connect");
                    File fd=new File("mail_config");
                    try{
                    FileWriter writer=new FileWriter(fd);
                    for (User x: UserList){
                        writer.write(x.email+","+x.password+","+x.hostSent+","+x.portSent+","+x.hostImap
                        +","+x.portImap+","+x.getPriority()+'\n');
                    }
                    Stage succes=new Stage();
                    Label succesL=new Label("Succesfully saved account");
                    BorderPane succesbp=new BorderPane();
                    succesbp.setCenter(succesL);
                    Scene succesScene=new Scene(succesbp,200,200);
                    succes.setScene(succesScene);
                    succes.show();
                    writer.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                }
                else{
                    System.out.println("cant connect");
                }
            }
                catch(emptyAccountException e){
                    Stage warning=new Stage();
                    Label warningL=new Label("All fields must be filled!");
                    BorderPane warningbp=new BorderPane();
                    warningbp.setCenter(warningL);
                    Scene warningScene=new Scene(warningbp,200,200);
                    warning.setScene(warningScene);
                    warning.show();
                }
                });
            
                // bp_scene.setRight(user_optionsBox);
            }
            else {
                userOptionsBox.getChildren().clear();
            }
        });
        addUser.setOnAction(event->{
            CreatingUserUI cuUI=new CreatingUserUI(MainStage, UserList);
            cuUI.run();
            // createUserUI(MainStage,UserList);
            usersListView.setItems(FXCollections.observableArrayList(UserList));
        });
        leftOptions.getChildren().addAll(addUser,usersListView);
        BorderPane bp_scene=new BorderPane();
        bp_scene.setLeft(leftOptions);
        bp_scene.setRight(userOptionsBox);
        // bp_scene.setRight(userOptionsBox);
        Scene options_scene=new Scene(bp_scene,500,500);
        optionsMenu.setScene(options_scene);
        optionsMenu.show();
            // bp_scene.setCenter();
            // Scene options_scene=new Scene(bp_scene,300,300);
            // optionsMenu.setScene(options_scene);
            // optionsMenu.show();
    }   
    public static void execute(String[] args) {
        launch(args);
    }
}