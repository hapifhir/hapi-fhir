package ca.uhn.fhir.jpa.subscription.match.deliver.email;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class JavaMailEmailSender implements IEmailSender {

	private static final Logger ourLog = LoggerFactory.getLogger(JavaMailEmailSender.class);
	private String mySmtpServerHostname;
	private int mySmtpServerPort = 25;
	private JavaMailSenderImpl mySender;
	private String mySmtpServerUsername;
	private String mySmtpServerPassword;

	public String getSmtpServerHostname() {
		return mySmtpServerHostname;
	}

	/**
	 * Set the SMTP server host to use for outbound mail
	 */
	public void setSmtpServerHostname(String theSmtpServerHostname) {
		mySmtpServerHostname = theSmtpServerHostname;
	}

	public String getSmtpServerPassword() {
		return mySmtpServerPassword;
	}

	public void setSmtpServerPassword(String theSmtpServerPassword) {
		mySmtpServerPassword = theSmtpServerPassword;
	}

	public int getSmtpServerPort() {
		return mySmtpServerPort;
	}

	/**
	 * Set the SMTP server port to use for outbound mail
	 */
	public void setSmtpServerPort(int theSmtpServerPort) {
		mySmtpServerPort = theSmtpServerPort;
	}

	public String getSmtpServerUsername() {
		return mySmtpServerUsername;
	}

	public void setSmtpServerUsername(String theSmtpServerUsername) {
		mySmtpServerUsername = theSmtpServerUsername;
	}

	@Override
	public void send(EmailDetails theDetails) {
		String subscriptionId = theDetails.getSubscription().toUnqualifiedVersionless().getValue();
		StopWatch sw = new StopWatch();

		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.TEXT);

		SpringStandardDialect dialect = new SpringStandardDialect();
		dialect.setEnableSpringELCompiler(true);

		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setDialect(dialect);
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver);

		Context context = new Context();

		String body = engine.process(theDetails.getBodyTemplate(), context);
		String subject = engine.process(theDetails.getSubjectTemplate(), context);

		MimeMessage email = mySender.createMimeMessage();

		String from = trim(theDetails.getFrom());
		ourLog.info("Sending email for subscription {} from [{}] to recipients: [{}]", subscriptionId, from, theDetails.getTo());

		try {
			email.setFrom(from);
			email.setRecipients(Message.RecipientType.TO, toTrimmedCommaSeparatedString(theDetails.getTo()));
			email.setSubject(subject);
			email.setText(body);
			email.setSentDate(new Date());
			email.addHeader("X-FHIR-Subscription", subscriptionId);
		} catch (MessagingException e) {
			throw new InternalErrorException("Failed to create email message", e);
		}

		mySender.send(email);

		ourLog.info("Done sending email (took {}ms)", sw.getMillis());
	}

	@PostConstruct
	public void start() {
		Validate.notBlank(mySmtpServerHostname, "No SMTP host defined");

		mySender = new JavaMailSenderImpl();
		mySender.setHost(getSmtpServerHostname());
		mySender.setPort(getSmtpServerPort());
		mySender.setUsername(getSmtpServerUsername());
		mySender.setPassword(getSmtpServerPassword());
		mySender.setDefaultEncoding(Constants.CHARSET_UTF8.name());
	}

	private static String toTrimmedCommaSeparatedString(List<String> theTo) {
		List<String> to = new ArrayList<>();
		for (String next : theTo) {
			if (isNotBlank(next)) {
				to.add(next);
			}
		}

		return StringUtils.join(to, ",");
	}
}
