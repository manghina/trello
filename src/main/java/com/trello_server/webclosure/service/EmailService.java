package com.trello_server.webclosure.service;

import com.trello_server.webclosure.domain.Email;
import com.trello_server.webclosure.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailService {
    @Autowired
    EmailRepository repository;

    @Value("${spring.application.email.protocol}")
    private String emailProtocol;
    @Value("${spring.application.email.host}")
    private String emailHost;

    @Value("${spring.application.email.port}")
    private String emailPort;

    @Value("${spring.application.email.username}")
    private String emailUsername;

    @Value("${spring.application.email.password}")
    private String emailPassword;

    public String getMessageContent(Message message) throws MessagingException, IOException {
        if (message == null)
            return "";
        String body = "";
        Multipart multipart = (Multipart) message.getContent();

        for (int x = 0; x < multipart.getCount(); x++) {
            BodyPart bodyPart = multipart.getBodyPart(x);

            String disposition = bodyPart.getDisposition();

            if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                System.out.println("Mail have some attachment : ");

                DataHandler handler = bodyPart.getDataHandler();
                System.out.println("file name : " + handler.getName());
            } else {
                Object b = bodyPart.getContent();
                body = b.toString();
            }
        }
        return body;
    }

    public void save(List<Email> emails) {
        for(Email email : emails) {
            repository.save((email));
        }
    }

    public List<Email> downloadEmails() {
        Properties properties = getServerProperties();
        Session session = Session.getDefaultInstance(properties);
        List<Email> emails = new ArrayList<>();
        try {
            // connects to the message store
            Store store = session.getStore(emailProtocol);
            store.connect(emailUsername, emailPassword);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();

            for (int i = 0; i < messages.length; i++) {
                Email email = new Email();
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg
                    .getRecipients(Message.RecipientType.TO));
                String ccList = parseAddresses(msg
                    .getRecipients(Message.RecipientType.CC));
                String sentDate = msg.getSentDate().toString();
                Istant date = Instant.parse(sentDate);

                String contentType = msg.getContentType();
                String messageContent = "";

                if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    try {
                        Object content = msg.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }
                }
                if (contentType.contains("multipart")) {
                    messageContent = getMessageContent( msg);
                }
                email.setFrom(from);
                email.setSubject(subject);
                email.setBody(messageContent);
               // email.setTimestamp(date);

                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t Type: " + contentType);
                System.out.println("\t From: " + from);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
                emails.add(email);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + emailProtocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emails;
    }

    private Properties getServerProperties() {
        Properties properties = new Properties();
        properties.put(String.format("mail.%s.host", emailProtocol), emailHost);
        properties.put(String.format("mail.%s.port", emailProtocol), emailPort);
        properties.setProperty( String.format("mail.%s.socketFactory.class", emailProtocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty( String.format("mail.%s.socketFactory.fallback", emailProtocol), "false");
        properties.setProperty( String.format("mail.%s.socketFactory.port", emailProtocol),
            String.valueOf(emailPort));
        return properties;
    }



    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }

}
