package com.zoho.booktickets.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.zoho.booktickets.model.Booking;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.Theater;

public class EmailService {

    static final String username = "santheesh16@gmail.com";
    static final String password = "S@nth1609";
    static String from = "santheesh16@gmail.com";
    static String subject = "OTP Verification";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);

    public static Session authSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.stmp.user", username);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        session.setDebug(true);
        return session;
    }

    public void generatedPDF(long bookingId) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream(
                            "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\BookMyTicket-1\\resources\\BookTicket.pdf"));
            document.open();
            addBooking(document, bookingId);
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void generatedCancelPDF(long bookingId) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
                    "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/BookMyTicket/resources/BookTicket.pdf"));
            document.open();
            addCancelBooking(document, bookingId);
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendPDFMail(String to) {
        Session session = authSession();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("BookTicket");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Verification");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String filename = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\BookMyTicket-1\\resources\\BookTicket.pdf";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addCancelBooking(Document document, long bookingId) throws DocumentException {
        document.add(new Paragraph("BookMyTicket", catFont));
        Booking booking = new BookingService().read(bookingId);
        Theater theater = new TheaterService().read(booking.getTheaterId());
        Movie movie = theater.getMovies().get((int) booking.getMovieId() - 1);
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase("Booking Id"));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(bookingId)));
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("Theater Name");
        table.addCell(theater.getTheaterName());
        table.addCell("ScreenNo");
        table.addCell(String.valueOf(booking.getScreenId()));
        table.addCell("Address");
        table.addCell((theater.getAddress()).toString());
        table.addCell("MovieName");
        table.addCell(movie.getMovieName());
        table.addCell("BookedSeats");
        table.addCell(Arrays.toString(booking.getNoOfBookedSeats()).replace("[", "").replace("]", ""));
        table.addCell("Show Time & Date");
        table.addCell(booking.getShowDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));
        table.addCell("Booked Time & Date");
        table.addCell(booking.getBookedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));

        table.setSpacingBefore(20);
        table.setSpacingAfter(50);
        document.add(new Paragraph("Successfully Cancelled", catFont));
        document.add(table);

    }

    private static void addBooking(Document document, long bookingId) throws DocumentException {
        document.add(new Paragraph("BookMyTicket", catFont));
        Booking booking = new BookingService().read(bookingId);
        Theater theater = new TheaterService().read(booking.getTheaterId());
        Movie movie = theater.getMovies().get((int) booking.getMovieId() - 1);
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase("BookingId"));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.valueOf(bookingId)));
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("Theater Name");
        table.addCell(theater.getTheaterName());
        table.addCell("ScreenNo");
        table.addCell(String.valueOf(booking.getScreenId()));
        table.addCell("Address");
        table.addCell((theater.getAddress()).toString());
        table.addCell("MovieName");
        table.addCell(movie.getMovieName());
        table.addCell("BookedSeats");
        table.addCell(Arrays.toString(booking.getNoOfBookedSeats()).replace("[", "").replace("]", ""));
        table.addCell("Show Time & Date");
        table.addCell(booking.getShowDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));
        table.addCell("Booked Time & Date");
        table.addCell(booking.getBookedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));

        table.setSpacingBefore(20);
        table.setSpacingAfter(50);
        document.add(new Paragraph("Successfully Booked", catFont));
        document.add(table);
    }

    public static void sendOTPMail(String to, String generatedOTP) {

        Session session = authSession();
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            msg.setSubject(subject);

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlTemplateOTP(generatedOTP), "text/html");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", 465, username, password);
            Transport.send(msg);
            System.out.println("fine!!");
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    public static void sendExpiryAlert(String to, String expiryDays) {

        Session session = authSession();
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            msg.setSubject("Account Expiry Alert");

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlTemplateExpiry(expiryDays), "text/html");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", 465, username, password);
            Transport.send(msg);
            System.out.println("fine!!");
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    public static void main(String[] args) {
        EmailService.sendExpiryAlert("santheesh62@gmail.com", "2");
        // sendPDFMail("santheesh11@gmail.com");
    }

    public static String htmlTemplateExpiry(String expiryDays) {
        return new StringBuilder(
                "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">")
                .append("<div style=\"margin:50px auto;width:70%;padding:20px 0\">")
                .append("<div style=\"border-bottom:1px solid #eee\">")
                .append("<a style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">BookMyShow</a></div>")
                .append("<p style=\"font-size:1.1em\">Hi,</p>")
                .append("<p>Your Account Password going expiry in </p>")
                .append("<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">")
                .append(expiryDays).append(" days")
                .append("</h2>")
                .append("Please change your before password expiry!!</div></div>").toString();
    }

    public static String htmlTemplateOTP(String generatedOTP) {
        return new StringBuilder(
                "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">")
                .append("<div style=\"margin:50px auto;width:70%;padding:20px 0\">")
                .append("<div style=\"border-bottom:1px solid #eee\">")
                .append("<a style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">BookMyTicket</a></div>")
                .append("<p style=\"font-size:1.1em\">Hi,</p>")
                .append("<p>Use the following OTP to complete your Sign Up procedures</p>")
                .append("<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">")
                .append(generatedOTP)
                .append("</h2></div></div>").toString();
    }

}