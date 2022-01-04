package ca.uhn.fhir.rest.server.mail;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.commons.lang3.Validate;
import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.api.mailer.AsyncResponse;
import org.simplejavamail.api.mailer.AsyncResponse.ExceptionConsumer;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class MailSvc implements IMailSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(MailSvc.class);

	private final MailConfig myMailConfig;
	private final Mailer myMailer;

	public MailSvc(@Nonnull MailConfig theMailConfig) {
		Validate.notNull(theMailConfig);
		myMailConfig = theMailConfig;
		myMailer = makeMailer(myMailConfig);
	}

	@Override
	public void sendMail(@Nonnull List<Email> theEmails) {
		Validate.notNull(theEmails);
		theEmails.forEach(theEmail -> send(theEmail, new OnSuccess(theEmail), new ErrorHandler(theEmail)));
	}

	@Override
	public void sendMail(@Nonnull Email theEmail) {
		send(theEmail, new OnSuccess(theEmail), new ErrorHandler(theEmail));
	}

	@Override
	public void sendMail(@Nonnull Email theEmail,
								@Nonnull Runnable theOnSuccess,
								@Nonnull ExceptionConsumer theErrorHandler) {
		send(theEmail, theOnSuccess, theErrorHandler);
	}

	private void send(@Nonnull Email theEmail,
							@Nonnull Runnable theOnSuccess,
							@Nonnull ExceptionConsumer theErrorHandler) {
		Validate.notNull(theEmail);
		Validate.notNull(theOnSuccess);
		Validate.notNull(theErrorHandler);
		try {
			final AsyncResponse asyncResponse = myMailer.sendMail(theEmail, true);
			if (asyncResponse != null) {
				asyncResponse.onSuccess(theOnSuccess);
				asyncResponse.onException(theErrorHandler);
			}
		} catch (MailException e) {
			theErrorHandler.accept(e);
		}
	}

	@Nonnull
	private Mailer makeMailer(@Nonnull MailConfig theMailConfig) {
		ourLog.info("SMTP Mailer config Hostname:[{}] | Port:[{}] | Username:[{}] | TLS:[{}]",
			theMailConfig.getSmtpHostname(), theMailConfig.getSmtpPort(),
			theMailConfig.getSmtpUsername(), theMailConfig.isSmtpUseStartTLS());
		return MailerBuilder
			.withSMTPServer(
				theMailConfig.getSmtpHostname(),
				theMailConfig.getSmtpPort(),
				theMailConfig.getSmtpUsername(),
				theMailConfig.getSmtpPassword())
			.withTransportStrategy(theMailConfig.isSmtpUseStartTLS() ? TransportStrategy.SMTP_TLS : TransportStrategy.SMTP)
			.buildMailer();
	}

	@Nonnull
	private String makeMessage(@Nonnull Email theEmail) {
		return " with subject [" + theEmail.getSubject() + "] and recipients ["
			+ theEmail.getRecipients().stream().map(Recipient::getAddress).collect(Collectors.joining(",")) + "]";
	}

	private class OnSuccess implements Runnable {
		private final Email myEmail;

		private OnSuccess(@Nonnull Email theEmail) {
			myEmail = theEmail;
		}

		@Override
		public void run() {
			ourLog.info("Email sent" + makeMessage(myEmail));
		}
	}

	private class ErrorHandler implements ExceptionConsumer {
		private final Email myEmail;

		private ErrorHandler(@Nonnull Email theEmail) {
			myEmail = theEmail;
		}

		@Override
		public void accept(Exception t) {
			ourLog.error("Email not sent" + makeMessage(myEmail), t);
		}
	}
}
