package com.b07.store;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;

public class Email {

	public static void emailWishlist(List<String> wishlistItems, List<String> emailAddresses, String customerName) {

		final String username = "mojoeonlinestore@gmail.com";
		final String password = "CSCB07PROJECT";
		
		String delim = "\n";
		String items = String.join(delim, wishlistItems);
		
		String delim1 = ",";
		String addresses = String.join(delim1, emailAddresses);
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
			message.setSubject("MoJo Online Store --- Wishlist");
			message.setText("Dear Friend," + "\n\n A wishlist has been sent to you from " + customerName + "\n\n" + items);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}