package domein;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper {
	
	public static void verstuurMailSessieAankondiging(String van, String vanPasswoord, List<String> naar,
			SessieAankondiging sa) {
		String title = "", inhoud = "";
		if (sa != null) {
			title = String.format("Aankondiging bij de sessie : %s", sa.getSessie().getNaam());
			inhoud = String.format("<h1>Er is een aankondiging bij een sessie waar je bent ingeschreven</h1>"
					+ "<h2>Gepost om %s</h2>"
					+ "<p>%s</p>", DatumEnTijdHelper.dateTimeFormat(sa.getGepost()), sa.getInhoud());
		}
		verstuurMail(van, vanPasswoord, naar, title, inhoud);
	}
	
	public static void verstuurMailAankondiging(String van, String vanPasswoord, List<String> naar,
			Aankondiging a) {
		String title = "", inhoud = "";
		if (a != null) {
			title = String.format("Algemene aankonding ivm het It-Lab");
			inhoud = String.format("<h2>Er is een algemene aankondiging</h2>"
					+ "<h3>Gepost om %s</h3>"
					+ "<p>%s</p>", DatumEnTijdHelper.dateTimeFormat(a.getGepost()), a.getInhoud());
		}
		verstuurMail(van, vanPasswoord, naar, title, inhoud);
	}
	
	
	
	private static void verstuurMail(String van, String vanPasswoord, List<String> naar, String title, String inhoud) {
		Properties prop = System.getProperties();

		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "m.outlook.com");
		prop.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(van, vanPasswoord);
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(van));

			// Set To: header field of the header.
			InternetAddress[] naarEmails = new InternetAddress[naar.size()];
			for (int i = 0; i < naar.size(); i++) {
				naarEmails[i]=new InternetAddress(naar.get(i));
			}
			message.setRecipients(Message.RecipientType.TO, naarEmails);

			// Set Subject: header field
			message.setSubject(title);

			// Now set the actual message
			message.setContent(
		              inhoud,
		             "text/html");

			// Send message
			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
