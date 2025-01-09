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

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
public class contactsMenu {
    Stage mainStage;
    BorderPane answer;
    ArrayList<Contact>contactList;
    User user;
    public void run(){
        execute();
        // writeToJson();
    }
    private void readFromDB(){
        contactList=new ArrayList<>();
        String URL="jdbc:mysql://localhost:3306/JAVAPROJECT";
        String dbuser="java";
        // String dbpassword="6-4FLnJZeUX2gFs";
        String dbpassword="java";
        // String insertQuery="INSERT INTO CONTACTS (name, lastname,phonenumber, business, description, priorityvalue, emailID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String searchQuery="SELECT emailID, emailaddress from USERS where emailaddress='"+user.email+"'";
        // searchQuery=searchQuery.replace("?",user.email);
        Connection conn=null;
        try {
            conn=DriverManager.getConnection(URL,dbuser,dbpassword);
            Statement statm=conn.createStatement();
            ResultSet rs=statm.executeQuery(searchQuery);
            if (rs.next()==false){
                String insertst="insert into USERS (emailaddress)"+"values ('"+user.email+"')";
                PreparedStatement ps=conn.prepareStatement(insertst);
                // ps.setString(1,user.email);
                int rows=ps.executeUpdate();
                System.out.printf("%d added\n",rows);
            }
            else{
                int id=rs.getInt("emailID");
                String sqcontacts="SELECT name, lastname, phonenumber, business, description, emailID, mailaddress, priorityvalue from CONTACTS where emailID="+String.valueOf(id);
                Statement sm=conn.createStatement();
                ResultSet resultscontacts=sm.executeQuery(sqcontacts);
                while (resultscontacts.next()!=false){
                    String name=resultscontacts.getString("name"),
                    lastname=resultscontacts.getString("lastname"),
                    phonenumber=resultscontacts.getString("phonenumber"),
                    business=resultscontacts.getString("business"),
                    description=resultscontacts.getString("description"),
                    mailaddress=resultscontacts.getString("mailaddress");
                    int priorityvalue=resultscontacts.getInt("priorityvalue");
                    contactList.add(new Contact(mailaddress, lastname, name, phonenumber, business, description, priorityvalue));
                }
                // System.out.println(rs.getString("emailaddress"));
            }
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
    }
    private void writeToJson(){
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw=new FileWriter("contacts.json")){
            gson.toJson(contactList,fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (contactList==null){
            contactList=new ArrayList<Contact>();
        }
    }
    private void execute(){
        readFromDB();
        BorderPane bp=new BorderPane();
        ListView<Contact> lv=new ListView<>();
        VBox contactContentBox=new VBox(0);
        lv.setCellFactory(new Callback<ListView<Contact>,ListCell<Contact>>(){
                @Override
                public ListCell<Contact> call(ListView<Contact> listview){
                    return new ListCell<Contact>(){
                        @Override
                        protected void updateItem(Contact contact,boolean empty){
                            super.updateItem(contact,empty);
                            if (contact!=null && !empty){
                                VBox toDisplay=new VBox(0);
                                Label completeName=new Label(contact.getName()+" "+contact.getFamilyName());
                                completeName.setTextAlignment(TextAlignment.CENTER);
                                completeName.setStyle("-fx-font-weight: bold;");
                                Label emailL=new Label(contact.getEmail());
                                Label phoneL=new Label(contact.getPhoneNumber());
                                toDisplay.getChildren().addAll(completeName,
                                emailL,phoneL);
                                setGraphic(toDisplay);
                            }
                            else{
                                setText(null);
                                setGraphic(null);
                            }
                        }
                    };
                }
            });
            lv.getSelectionModel().selectedItemProperty().addListener((observable,oldvalue,selectedContact)->{
            if (selectedContact!=null){
                Button deletebutton=new Button("Remove contact");
                VBox contactInfo=new VBox(0);
                Label name=new Label("Full name: "+selectedContact.getName()+" "+selectedContact.getFamilyName());
                Label Description=new Label("Description: "+selectedContact.getDescription());
                Label Business=new Label("Business: "+selectedContact.getBusiness());
                contactInfo.getChildren().addAll(name,Description,Business);
                VBox contactsArea=new VBox(0);
                Label contactL=new Label("Contact:");
                Text emailT=new Text(selectedContact.getEmail());
                emailT.setStyle("-fx-fill: blue; -fx-underline: true;");
                emailT.setOnMouseClicked(event->{

                });
                Label phoneL=new Label(selectedContact.getPhoneNumber());
                contactsArea.getChildren().addAll(contactL,emailT,phoneL);
                contactContentBox.getChildren().clear();
                contactContentBox.getChildren().addAll(deletebutton,contactInfo,contactsArea);                
            }
            else{
                contactContentBox.getChildren().clear();
            }
        });
        Button createContact=new Button("Create contact");
        // lv=
        if (contactList==null || contactList.isEmpty()==true){
            // lv=new ListView<>();
            createContact.setOnAction(event->{
                createContactMenu ccm=new createContactMenu();
                ccm.user=user;
                ccm.run();
                contactList=ccm.contactList;
                ObservableList<Contact> newcontacts=FXCollections.observableArrayList(contactList);
                lv.setItems(newcontacts);
                // contacts(contactList);
            });
        }
        else{
            ObservableList<Contact> contacts=FXCollections.observableArrayList(contactList);
            // lv=new ListView<>(contacts);
            lv.setItems(contacts);
            createContact.setOnAction(event->{
                createContactMenu ccm=new createContactMenu();
                ccm.contactList=contactList;
                ccm.user=user;
                ccm.run();
                contactList=ccm.contactList;
                ObservableList<Contact> newcontacts=FXCollections.observableArrayList(contactList);
                lv.setItems(newcontacts);
                // lv.getItems().clear();
                // lv.getItems().addAll(contactList);
                // contacts(contactList);
            });
        }
        
        VBox contactSpace=new VBox(0);
        VBox.setVgrow(lv,Priority.ALWAYS);
        contactSpace.getChildren().addAll(createContact,lv);
        lv.autosize();
        BorderPane layout=new BorderPane();
        layout.setLeft(contactSpace);
        layout.setCenter(contactContentBox);
        answer=layout;
    }
}
