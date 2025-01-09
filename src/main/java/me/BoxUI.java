package me;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
public class BoxUI{
    public BorderPane answer;
    User user;
    BorderPane layout;
    Stage mainStage;
    Box box;
    currMailView mailinView;
    Task<Void> mailFetcherTask;
    int activeTypeofView;
    int index;
    public BoxUI(Stage mS,Box x,BorderPane l){
        // answer=execute(mainStage, X);
        // user=us;
        activeTypeofView=-1;
        answer=null;
        mainStage=mS;
        box=x;
        layout=l;
        // index=ind;
    }
    public void setOwner(User us){
        user=us;
    }
    public void run(){
        // System.out.println("executing %s".formatted(box.getFolderName()));
        execute();
    }
    private void execute(){
        box.loadMoreItems();
        ObservableList<currMailView> inbox=FXCollections.observableArrayList(box.getStorage());
        ListView <currMailView> emailListView=new ListView<>(inbox);
        TextArea emailSubject=new TextArea();
        TextArea emailSender=new TextArea();
        TextArea emailReceiver=new TextArea();
        TextArea Date=new TextArea();
        TextArea CCReceiver=new TextArea();
        // TextArea 
        Date.setEditable(false);
        CCReceiver.setEditable(false);
        emailReceiver.setEditable(false);
        emailSender.setEditable(false);
        emailSubject.setEditable(false);
        Button replyButton=new Button("Reply"),
                forwardButton=new Button("Forward"),
                replyAllButton=new Button("Reply all"),
                markasSpamButton=new Button("Mark as Spam"),
                deleteButton=new Button("Delete");
        // Label emailLabel=new Label("Email Content:");
        TextArea emailPlainText=new TextArea();
        WebView webView=new WebView();
        TextArea ta=new TextArea();
        WebEngine webEngine=webView.getEngine();
        // int htmlcontent=1;
        emailPlainText.setEditable(false);
        VBox emailContentBox=new VBox(0);   
        // emailContentBox.getChildren();
        emailListView.setCellFactory(new Callback<ListView<currMailView>, ListCell<currMailView>>(){
            @Override
            public ListCell<currMailView> call(ListView<currMailView> listView){
                return new ListCell<currMailView>(){
                @Override
                protected void updateItem(currMailView email,boolean empty){
                    super.updateItem(email,empty);
                    if (email!=null && !empty){
                        VBox toDisplay=new VBox(0);
                        Label emailL=new Label(email.getSender());
                        emailL.setTextAlignment(TextAlignment.CENTER);
                        emailL.setStyle("-fx-font-weight: bold;");
                        HBox secondRow=new HBox(80);
                        Label subjL=new Label(email.getSubject());
                        subjL.setTextAlignment(TextAlignment.CENTER);
                        Label seenValue=new Label();
                        // seenValue.setEditable(false);
                        try{
                        if (email.mess.getFlags().contains(jakarta.mail.Flags.Flag.SEEN)){
                            seenValue.setText("seen");
                        }
                        else seenValue.setText("not seen");
                        }
                        catch(MessagingException e){
                            e.printStackTrace();
                        }
                        secondRow.getChildren().addAll(subjL,seenValue);
                        Label time=new Label();
                        String t=email.StylisedSendDate();
                        time.setText(t);
                        toDisplay.getChildren().addAll(emailL,secondRow,time);
                        setGraphic(toDisplay);
                        // setText(email.getSubject()+"\nFrom "+email.getSender()+'\n'+email.getDate());
                    }
                    else{
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            }
        });
            emailListView.skinProperty().addListener((obs,oldvalue,newvalue)->{
                ScrollBar sb=(ScrollBar)emailListView.lookup(".scroll-bar:vertical");
                if (sb!=null){
                    sb.valueProperty().addListener((observable,ovalue,nvalue)->{
                        if (nvalue.doubleValue()==sb.getMax()){
                            // System.out.println("yes");
                            box.loadMoreItems(emailListView);
                        };
                    });
                }
            });
            HBox mailOptions=new HBox();
            
            HBox attachmentsBox=new HBox();
            List<Button> downloadStorage=new ArrayList<>();
            emailListView.setOnMouseClicked(event->{
                if (mailFetcherTask!=null && mailFetcherTask.isRunning()){
                    System.out.println("CANCELLED MAILFETCHER");
                    mailFetcherTask.cancel();
                }
                mailFetcherTask=new Task<>()
                {
                    @Override
                    protected Void call() {
                        activeTypeofView=0;
                currMailView selectedItem=emailListView.getSelectionModel().getSelectedItem();
                mailinView=selectedItem;
                if (isCancelled()){
                    return null;
                }
                box.retrieveData(mailinView);
                if (isCancelled()){
                    return null;
                }
                deleteButton.setOnAction(event->{
                    box.markForDelete(mailinView);
                    emailListView.getItems().remove(mailinView);
                    System.out.println("Marked for delete");
                    emailSender.setText("deleted mail");
                    emailReceiver.setText("");
                    emailSubject.setText("");
                    Date.setText("");
                    webEngine.load("");
                    ta.setText("");
                    attachmentsBox.getChildren().clear();

                    System.out.println(mailinView);
                });
                replyButton.setOnAction(event->{
                    // if (activeTypeofView>0){
                    //     // activeTypeofView=0;
                    //     layout.getChildren().removeLast();
                    // }
                    currMailSend mess=null;
                    activeTypeofView=1;
                    try{
                    mess=new currMailSend(mailinView.mess.reply(false));
                    System.out.println(mailinView.getQuotedContent());
                    // String ogContent=getTextFromMessage(mailinView.mess);
                    mess.mess.setContent(mailinView.mess.getContent(),mailinView.mess.getContentType());
                    // emailContentBox.getChildren().remove(InstanceOf BorderPane);
                    // BorderPane maskReply=new BorderPane();
                    Node rightmc=null;
                    Node originalValue=layout.getRight();
                    CreatingEmailUI mailui=new CreatingEmailUI(layout, rightmc, mainStage, user, originalValue);
                    mailui.setReplytoMail(mailinView);
                    mailui.setTemplate(mess);
                    mailui.run();
                    if (mailui.answer!=null)
                    emailContentBox.getChildren().addLast(mailui.answer);
                    else System.out.println("did not work mailui.answer");
                    // System.out.println(emailContentBox.getChildren());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    catch(MessagingException e){
                        e.printStackTrace();
                    }
                        // mess=box.getReplyMail(mailinView,false);
                    System.out.println(mess);
                    // layout.getChildren().removeLast();
                    activeTypeofView=0;
                    // mess=new currMailSend(mailinView.reply(false));
                    if(layout.getRight().getClass()==VBox.class){
                        System.out.println("YES");
                    }
                    });
                replyAllButton.setOnAction(event->{
                    // if (activeTypeofView>0){
                    //     // activeTypeofView=0;
                    //     layout.getChildren().removeLast();
                    // }
                    activeTypeofView=2;
                    currMailSend mess=null;
                    try{
                    mess=new currMailSend(mailinView.mess.reply(true));
                    // String ogContent=getTextFromMessage(mailinView.mess);
                    mess.mess.setContent(mailinView.mess.getContent(),mailinView.mess.getContentType());
                    // emailContentBox.getChildren().remove(InstanceOf BorderPane);
                    // BorderPane maskReply=new BorderPane();
                    Node rightmc=null;
                    Node originalValue=layout.getRight();
                    CreatingEmailUI mailui=new CreatingEmailUI(layout, rightmc, mainStage, user, originalValue);
                    mailui.setReplytoMail(mailinView);
                    mailui.setTemplate(mess);
                    mailui.run();
                    if (mailui.answer!=null)
                    emailContentBox.getChildren().addLast(mailui.answer);
                    else System.out.println("did not work mailui.answer");
                    // System.out.println(emailContentBox.getChildren());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    catch(MessagingException e){
                        e.printStackTrace();
                    }
                        // mess=box.getReplyMail(mailinView,false);
                    System.out.println(mess);
                    // layout.getChildren().removeLast();
                    activeTypeofView=0;
                    // mess=new currMailSend(mailinView.reply(false));
                    if(layout.getRight().getClass()==VBox.class){
                        System.out.println("YES");
                    }
                });
                forwardButton.setOnAction(event->{
                    // if (activeTypeofView>0){
                    //     // activeTypeofView=0;
                    //     layout.getChildren().removeLast();
                    // }
                    activeTypeofView=3;
                    try {
                        Node rightmc=null;
                    Node originalValue=layout.getRight();
                    CreatingEmailUI mailui=new CreatingEmailUI(layout, rightmc, mainStage, user, originalValue);
                    mailui.setReplytoMail(mailinView);
                    mailui.fwdok=1;
                    // mailui.setTemplate(mess);
                    mailui.run();
                    if (mailui.answer!=null)
                    emailContentBox.getChildren().addLast(mailui.answer);
                    else System.out.println("did not work mailui.answer");
                    // System.out.println(emailContentBox.getChildren());
                    // layout.getChildren().removeLast();
                    activeTypeofView=0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                box.markSeen(mailinView);
                emailSubject.setText("Subject: "+mailinView.getSubject());
                emailSubject.setPrefHeight(50);
                Date.setText("Date: "+mailinView.getDate());
                Date.setPrefHeight(50);
                emailReceiver.setText("To: "+mailinView.getReceivers());
                emailReceiver.setPrefHeight(50);
                emailSender.setText("From: "+mailinView.getSender());
                emailSender.setPrefHeight(50);
                String plainText,htmlstr;
                htmlstr=mailinView.getHtml();
                if (htmlstr==null){
                plainText=mailinView.getContent();
                // htmlstr=plainText;
                htmlstr=null;
                // htmlstr="<html><body><pre>"+plainText.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                // .replace("\"","&quot;").replace("'","&#39;")+"</pre></body></html>";
                // emailContentBox.getChildren().addAll();
                }
                else if (htmlstr.equals("")){
                    plainText=mailinView.getContent();
                    htmlstr=null;
                    // htmlstr=plainText;
                    // htmlstr=plainText;                    
                // htmlstr="<html><body><pre>"+plainText.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                // .replace("\"","&quot;").replace("'","&#39;")+"</pre></body></html>";
                }
                // attachmentsBox.getChildren().removeIf(node -> node instanceof Button);
                System.out.println(mailinView.attachments.size());
                // downloadStorage=new ArrayList<Button>();
                downloadStorage.clear();
                int it=0;
                for (jakarta.activation.DataSource att:mailinView.attachments){
                    if (att!=null){
                        int itt=it;
                        // System.out.println(mailinView.isattlist.get(it));
                    Button downloadButton=new Button("Download: "+att.getName());
                    downloadButton.setOnAction(e->{
                        File file=new File("downloads/"+att.getName());
                        file.getParentFile().mkdir();
                        try (FileOutputStream fos=new FileOutputStream(file)){
                            // InputStream is=mailinView.isattlist.get(itt);
                            InputStream is=att.getInputStream();
                            byte[]buff=new byte[4096];
                            int bread;
                            while ((bread=is.read(buff))!=-1){
                                fos.write(buff,0,bread);
                            }
                            fos.close();
                        }
                        catch(IOException IOex){
                            IOex.printStackTrace();
                        } 
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    System.out.println(att.getName());
                    downloadStorage.add(downloadButton);
                    // attachmentsBox.getChildren().add(downloadButton);
                }
                it++;
                }
                // if (mailinView!=null)
                // webEngine.loadContent(mailinView.htmlContent);
                return null;
            }
            };
            mailFetcherTask.setOnCancelled(WorkerStateEvent->{
                //TODO
                System.out.println("Cancelled");
            });
            mailFetcherTask.setOnSucceeded(workerStateEvent->{
                if (mailinView!=null)
                Platform.runLater(()->{
                    if (mailinView.htmlContent!=null){
                        // emailContentBox.getChildren()
                        Platform.runLater(()->{
                            emailContentBox.getChildren().add(webView);
                            emailContentBox.getChildren().add(attachmentsBox);
                        });
                        SpamFilterApp app=new SpamFilterApp(mailinView.htmlContent);
                        app.execute();
                        System.out.printf("Message answer is %d\n",app.answer);
                        if (app.answer==1){
                            webEngine.loadContent("Content is spam :(");
                        }
                        else{
                        webEngine.loadContent(mailinView.htmlContent);
                        }
                    }
                    else{
                        Platform.runLater(()->{
                            ta.setText(mailinView.plainText);
                            ta.setEditable(false);
                            emailContentBox.getChildren().add(ta);
                            emailContentBox.getChildren().add(attachmentsBox);
                        });
                    }
                    attachmentsBox.getChildren().clear();
                    for (Button i:downloadStorage){
                        attachmentsBox.getChildren().add(i);
                    }
                    // emailContentBox.getChildren().add(attachmentsBox);
                }
                );
            });
            mailFetcherTask.setOnRunning(workerStateEvent->{
                emailSender.setText("Loading...");
                emailSubject.setText("");
                // emailContentBox.getChildren().removeIf(node -> node instanceof Button);
                emailReceiver.setText("");
                Date.setText("");
                Platform.runLater(()->{
                    emailContentBox.getChildren().clear();
                    mailOptions.getChildren().clear();
                    mailOptions.getChildren().addAll(replyButton,forwardButton,replyAllButton,markasSpamButton,deleteButton);
            emailContentBox.getChildren().add(0,mailOptions);
            emailContentBox.getChildren().addAll(emailSender,emailReceiver,emailSubject,Date);
            // emailContentBox.getChildren().add(webView);
            // emailContentBox.getChildren().add();
            // emailContentBox.getChildren().add(attachmentsBox);
            
                    webEngine.loadContent("");
                    attachmentsBox.getChildren().clear();
                    // for (Button i:downloadStorage){
                    //     attachmentsBox.getChildren().add(i);
                    // }
            });
            });
            mailFetcherTask.setOnFailed(workerStateEvent->{
                //TODO
            });
            new Thread(mailFetcherTask).start();
            });
            // emailContentBox.getChildren().add(attachmentsBox);
            ScrollPane rightEmailBox=new ScrollPane();
            rightEmailBox.setContent(emailContentBox);
            rightEmailBox.setFitToHeight(true);
            // rightEmailBox
            // layout.setRight(emailContentBox);
            layout.setRight(rightEmailBox);
                // }
                // System.out.println(htmlstr);
                
                // emailContentBox.getChildren().clear();
                // if (htmlstr.equals("")){
                //     htmlcontent=0;
                // }
            
            // HBox mailOptions=new HBox();
            // replyButton.setOnAction(event->{
            // currMailSend mess;
            // try{
            // mess=new currMailSend(mailinView.reply(false));
            
            // }
            // catch(MessagingException e){
            //     e.printStackTrace();
            // }
            // });
            // emailListView.getSelectionModel().selectedItemProperty().addListener((observable,oldvalue,selectedEmail)->{
            // if (selectedEmail!=null){
            //     // selectedEmail.retrieveData();
            // mailinView=selectedEmail;
            // box.retrieveData(selectedEmail);
            // box.markSeen(selectedEmail);
            // System.out.println(selectedEmail.receivers);
            // mailOptions.getChildren().addAll(replyButton,forwardButton,replyAllButton,markasSpamButton,deleteButton);
            // emailContentBox.getChildren().clear();
            // emailContentBox.getChildren().add(0,mailOptions);
            // emailContentBox.getChildren().addAll(emailSender,emailReceiver,emailSubject,Date);
            // emailContentBox.getChildren().add(webView);
            //     emailSubject.setText("Subject: "+selectedEmail.getSubject());
            //     Date.setText("Date: "+selectedEmail.getDate());
            //     emailReceiver.setText("To: "+selectedEmail.getReceivers());
            //     emailSender.setText("From: "+selectedEmail.getSender());
            //     // CCReceiver.setText(selectedEmail.getCC());
            //     String plainText,htmlstr;
            //     htmlstr=selectedEmail.getHtml();
            //     if (htmlstr==null){
            //     plainText=selectedEmail.getContent();
            //     htmlstr=plainText;
            //     // emailContentBox.getChildren().addAll();
            //     }
            //     else if (htmlstr.equals("")){
            //         plainText=selectedEmail.getContent();
            //         htmlstr=plainText;
            //     }
            //     emailContentBox.getChildren().removeIf(node -> node instanceof Button);
            //     for (DataSource att:selectedEmail.attachments){
            //         if (!att.equals(null)){
            //         Button downloadButton=new Button("Download: "+att.getName());
            //         downloadButton.setOnAction(e->{
            //             File file=new File("downloads/"+att.getName());
            //             file.getParentFile().mkdir();
            //             try {
            //                 InputStream inputStream=att.getInputStream();
            //                 OutputStream outputStream=new FileOutputStream(file);
            //                 byte[] buffer=new byte[4096];
            //                 int bytesRead;
            //                 while ((bytesRead=inputStream.read(buffer))!=-1){
            //                     outputStream.write(buffer,0,bytesRead);
            //                 }
            //                 outputStream.close();
            //             } catch (Exception exception) {
            //                 exception.printStackTrace();
            //             }
            //         });
            //         try{
            //         selectedEmail.mess.setFlag(Flags.Flag.SEEN,true);
            //         }
            //         catch(MessagingException e){
            //             e.printStackTrace();
            //         }
            //         emailContentBox.getChildren().add(downloadButton);
            //     }
            //     }
            //     // System.out.println(layout.getRight());
            //     // if (layout.getRight()==null){
            //         // System.out.println("NULL");
            //         layout.setRight(emailContentBox);
            //     // }
            //     // System.out.println(htmlstr);
            //     webEngine.loadContent(htmlstr);
            //     // emailContentBox.getChildren().clear();
            //     // if (htmlstr.equals("")){
            //     //     htmlcontent=0;
            //     // }
            // }
            // else {
            //     emailContentBox.getChildren().clear();
            // }
        // });
        
    // });
        TextField searchBar=new TextField();
        searchBar.setPromptText("...");
        HBox.setHgrow(searchBar,Priority.ALWAYS);
        Button searchButton= new Button("Search");
        searchButton.setOnAction(event->{
        //     ObservableList<currMailView> inbox=FXCollections.observableArrayList(box.getStorage());
        //     ListView <currMailView> emailListView=new ListView<>(inbox);
            ObservableList<currMailView> searchedKeyWordList=FXCollections.observableArrayList(box.searchKeyphrase(searchBar.getText()));
            emailListView.setItems(searchedKeyWordList);

            // emailListView.setItems(box.searchKeyphrase(searchBar.getText()));
        });
        // searchButton.setStyle("-fx-font-family: 'Noto Color Emoji'; -fx-font-size: 20;");
        HBox searchBox=new HBox(5,searchBar,searchButton);
        HBox sortingOptions=new HBox(0);
        Button DateOrder=new Button("Sort: by Date ");
        Button DateSubject=new Button("Sort: by Subject");
        sortingOptions.getChildren().addAll(DateOrder,DateSubject);
        // DateOrder.setOnAction(event->{
        //     DateOrder.setText("Sort: by Date ↑");
        //     ObservableList<currMailView> reversed_inbox=FXCollections.observableArrayList(x.getInbox().getStorage().reversed());
        //     emailListView.setItems(reversed_inbox);
        // });
        // DateSubject.setOnAction(event->{
        //     DateSubject.setText("Sort: by Subject ↑");
        //     ObservableList<currMailView> reversed_inbox=FXCollections.observableArrayList(x.getInbox().bySubj().getStorage().reversed());
        //     emailListView.setItems(reversed_inbox);
        // });
        VBox emailSpace=new VBox(0);
        // emailListView.setPrefHeight(400);
        VBox.setVgrow(emailListView, Priority.ALWAYS);
        emailSpace.getChildren().addAll(searchBox,sortingOptions,emailListView);
        emailListView.autosize();
        // BorderPane layout=new BorderPane();
        layout.setCenter(emailSpace);
        layout.setRight(emailContentBox);
        answer=layout;
        // return;
        // layout.setCenter
        // Scene scene=new Scene(layout,600,400);
        // mainStage.setScene(scene);
        // timeline.play();
        // mainStage.show();
    }
    // private static String getTextFromMessage(Message message) throws IOException, MessagingException {
    //     if (message.isMimeType("text/plain")) {
    //         return (String) message.getContent();
    //     } else if (message.isMimeType("text/html")) {
    //         // If the message is HTML, you can parse or strip the HTML tags if necessary
    //         return org.jsoup.Jsoup.parse((String) message.getContent()).text(); // Using JSoup to extract plain text
    //     } else if (message.isMimeType("multipart/*")) {
    //         Multipart multipart = (Multipart) message.getContent();
    //         for (int i = 0; i < multipart.getCount(); i++) {
    //             BodyPart part = multipart.getBodyPart(i);
    //             if (part.isMimeType("text/plain")) {
    //                 return (String) part.getContent();
    //             } else if (part.isMimeType("text/html")) {
    //                 return org.jsoup.Jsoup.parse((String) part.getContent()).text();
    //             }
    //         }
    //     }
    //     return ""; // Default fallback if no text content is found
    // }
}