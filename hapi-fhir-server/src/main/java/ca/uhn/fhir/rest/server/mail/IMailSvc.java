package ca.uhn.fhir.rest.server.mail;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.AsyncResponse;

import javax.annotation.Nonnull;
import java.util.List;

public interface IMailSvc {
	void sendMail(@Nonnull List<Email> theEmails);

	void sendMail(@Nonnull Email theEmail);

	void sendMail(@Nonnull Email theEmail,
					  @Nonnull Runnable theOnSuccess,
					  @Nonnull AsyncResponse.ExceptionConsumer theErrorHandler);

}
