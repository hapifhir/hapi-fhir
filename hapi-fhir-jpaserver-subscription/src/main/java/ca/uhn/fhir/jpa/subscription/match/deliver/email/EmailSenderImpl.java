package ca.uhn.fhir.jpa.subscription.match.deliver.email;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.rest.server.mail.IMailSvc;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.simplejavamail.api.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class EmailSenderImpl implements IEmailSender {

	private static final Logger ourLog = LoggerFactory.getLogger(EmailSenderImpl.class);

	private final IMailSvc myMailSvc;

	public EmailSenderImpl(@Nonnull IMailSvc theMailSvc) {
		Validate.notNull(theMailSvc);
		myMailSvc = theMailSvc;
	}

	@Override
	public void send(EmailDetails theDetails) {
		StopWatch stopWatch = new StopWatch();

		ourLog.info("Sending email for subscription {} from [{}] to recipients: [{}]", theDetails.getSubscriptionId(), theDetails.getFrom(), theDetails.getTo());

		Email email = theDetails.toEmail();

		myMailSvc.sendMail(email,
			() -> ourLog.info("Done sending email for subscription {} from [{}] to recipients: [{}] (took {}ms)",
				theDetails.getSubscriptionId(), theDetails.getFrom(), theDetails.getTo(), stopWatch.getMillis()),
			(e) -> {
				ourLog.error("Error sending email for subscription {} from [{}] to recipients: [{}] (took {}ms)",
					theDetails.getSubscriptionId(), theDetails.getFrom(), theDetails.getTo(), stopWatch.getMillis());
				ourLog.error("Error sending email", e);
			});
	}

}
