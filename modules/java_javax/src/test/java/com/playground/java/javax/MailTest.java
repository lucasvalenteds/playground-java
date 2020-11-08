package com.playground.java.javax;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MailTest {

    private final static String login = "login";

    private final static String password = "password";

    private final static ServerSetup serverSetup = ServerSetupTest.SMTP;

    private final static GreenMail greenMail = new GreenMail(serverSetup);

    private final static Properties properties = new Properties();

    private final static Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(login, password);
        }
    };

    @BeforeAll
    public static void beforeAll() {
        properties.putAll(Map.ofEntries(
            Map.entry("mail.smtp.auth", true),
            Map.entry("mail.smtp.host", serverSetup.getBindAddress()),
            Map.entry("mail.smtp.port", serverSetup.getPort())
        ));

        greenMail.setUser(login, password);
        greenMail.start();
    }

    @AfterAll
    public static void afterAll() {
        greenMail.stop();
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    void testSendingTextHtml() throws Exception {
        String from = "john.smith@example.com";
        String to = "mary.jane@example.com";
        String subject = "Greeting e-mail";

        MimeBodyPart text = new MimeBodyPart();
        text.setContent("Hello <b>John Smith</b>!", "text/html");
        text.setDisposition(MimeMessage.INLINE);

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(text);

        MimeMessage message = new MimeMessage(Session.getInstance(properties, authenticator));
        message.setFrom(InternetAddress.parse(from)[0]);
        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(to)[0]);
        message.setSubject(subject);
        message.setContent(multipart);

        Transport.send(message);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals(from, receivedMessages[0].getFrom()[0].toString());
        assertEquals(to, receivedMessages[0].getAllRecipients()[0].toString());
        assertEquals(subject, receivedMessages[0].getSubject());

        MimeMultipart content = (MimeMultipart) receivedMessages[0].getContent();
        assertEquals(1, content.getCount());
        assertEquals("Hello <b>John Smith</b>!", content.getBodyPart(0).getContent().toString());
    }

    @Test
    void testSendingAttachment() throws Exception {
        Path receipt = Paths.get(ClassLoader.getSystemResource("file.txt").toURI());
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.attachFile(receipt.toFile());
        attachment.setFileName("file-copy.txt");
        attachment.setDisposition(MimeMessage.ATTACHMENT);

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(attachment);

        MimeMessage message = new MimeMessage(Session.getInstance(properties, authenticator));
        message.setFrom(InternetAddress.parse("someone@email.com")[0]);
        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse("someone.else@email.com")[0]);
        message.setSubject("Some e-mail");
        message.setContent(multipart);

        Transport.send(message);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        MimeMultipart content = (MimeMultipart) receivedMessages[0].getContent();
        assertEquals(1, content.getCount());
        assertEquals("file-copy.txt", content.getBodyPart(0).getFileName());
        assertEquals("Hello World", content.getBodyPart(0).getContent().toString());
    }
}
