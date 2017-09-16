package ca.uhn.fhir.jpa.subscription.email;

public interface IEmailSender {

	void send(EmailDetails theDetails);

}
