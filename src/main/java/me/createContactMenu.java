package me;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import com.mysql.cj.PreparedQuery;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.PreparableStatement;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class createContactMenu{
    Stage mainStage;
    BorderPane answer;
    ArrayList<Contact> contactList;
    User user;
    public createContactMenu(){
        // contactList=cl;
        // readFromJson();
    }
    void run(){
        // writeToDB();
        execute();
        // writeToJson();
    }
    private void writeToDB(Contact contact){
        String URL="jdbc:mysql://localhost:3306/JAVAPROJECT";
        String dbuser="java";
        // String dbpassword="6-4FLnJZeUX2gFs";
        String dbpassword="java";
        // String insertQuery="INSERT INTO CONTACTS (name, lastname,phonenumber, business, description, priorityvalue, emailID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String searchQuery="SELECT emailID, emailaddress from USERS where emailaddress='"+user.email+"'";
        // searchQuery=searchQuery.replace("?",user.email);
        Connection conn=null;
        int id=0;
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
                id=rs.getInt("emailID");
                // System.out.printf("%d added\n",rows);
            }
            else{
                id=rs.getInt("emailID");
                // System.out.println(rs.getString("emailaddress"));
            }
            String insertcontact="insert into CONTACTS (name, lastname, phonenumber, business, description, priorityvalue, mailaddress,emailID)"+
            " values ('"+contact.getName()+"','"+contact.getFamilyName()+"','"+contact.getPhoneNumber()+"','"+contact.getBusiness()+"','"+contact.getDescription()+"',"+String.valueOf(contact.getPriority())+
            ",'"+contact.getEmail()+"','"+String.valueOf(id)+"')";
            System.out.println(insertcontact);
            PreparedStatement ps=conn.prepareStatement(insertcontact);
            int rsw=ps.executeUpdate();
            System.out.printf("written %d lines to db contacts",rsw);
            conn.close();   
        }
        // finally{
        //   }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    private void readFromJson(){
        Gson gson=new Gson();
        Type listType=new TypeToken<ArrayList<Contact>>() {}.getType();
        // contactList=new ArrayList<>(); 
        contactList=null;
        try(FileReader read=new FileReader("contacts.json")){
            // gson.toJson(contactList,read);
            contactList=gson.fromJson(read,listType);
        }
        catch(FileNotFoundException e){
            try{
            File fd=new File("contacts.json");
            fd.createNewFile();
            }
            catch(SecurityException ex){
                System.out.println("cannot open %s".formatted("contacts.json"));
                ex.printStackTrace();
                return;
            }
            catch(IOException ex){
                ex.printStackTrace();
                return;
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return;
        }
        if (contactList==null){
            contactList=new ArrayList<Contact>();
        }
    }
    private void writeToJson(){
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        System.out.println(contactList);
        try (FileWriter fw=new FileWriter("contacts.json")){
            gson.toJson(contactList,fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void execute(){
        mainStage=new Stage();
        VBox boxes=new VBox();
        Scene scene;
        answer=new BorderPane();
        Label emailL,
        nameL,familyL,
        phoneL,busL,
        desL,errorMess;
        emailL=new Label("*Email:");
        nameL=new Label("name:");
        familyL=new Label("Family:");
        phoneL=new Label("Phone number:");
        busL=new Label("Business:");
        desL=new Label("Description:");
        errorMess=new Label();
        TextField emailF,nameF,familyF,phoneF,busF,desF;
        emailF=new TextField();
        nameF=new TextField();
        familyF=new TextField();
        phoneF=new TextField();
        busF=new TextField();
        desF=new TextField();
        Button createContact=new Button("Create");
        Button cancel=new Button("Cancel");
        createContact.setOnAction(event->{
            String email=emailF.getText(),
            name=nameF.getText(),
            family=familyF.getText(),
            phone=phoneF.getText(),
            bus=busF.getText(),
            des=desF.getText();
            try{
            for (Contact i:contactList){
                if (i.getEmail().equals(email)){
                    throw new entityAlreadyExistsException();
                }
            }
            if (email.isBlank()){
                throw new emptyAccountException("Must include email");
                // errorMess.setText("Must include email");
            }
            else if (name.isBlank()){
                throw new emptyAccountException("Must include name");
                // errorMess.setText("Must include name");
            }
            else{
            Contact toAdd=new Contact(email,name,family,phone,bus,des,0);
            contactList.add(toAdd);
            // writeToJson();
            writeToDB(toAdd);
            mainStage.close();
            }
            }
            catch(entityAlreadyExistsException e){
                errorMess.setText("contact with this email already exists");
            }
            catch(emptyAccountException e){
                errorMess.setText(e.getMessage());
            }
        });
        boxes.getChildren().addAll(emailL,emailF,nameL,nameF,familyL,familyF,
        phoneL,phoneF,busL,busF,desL,desF,createContact,errorMess);
        answer=new BorderPane();
        answer.setCenter(boxes);
        scene=new Scene(answer,300,500);
        mainStage.setScene(scene);
        mainStage.show();
    }
}
