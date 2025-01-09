package me;

import java.io.IOException;
import java.util.List;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;

public interface mailViewable {
    public String getSender();
    public String getReceivers();
    public String getSubject();
    public String getContent() throws IOException, MessagingException;
    public List<DataSource> getAttachments();
    public String getDate();
    public String getCC();
    // public void retrieveData();
}
