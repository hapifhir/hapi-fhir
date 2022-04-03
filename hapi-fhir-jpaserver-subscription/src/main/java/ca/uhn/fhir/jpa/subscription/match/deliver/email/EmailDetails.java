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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class EmailDetails {
	private final SpringTemplateEngine myTemplateEngine;
	private final Context myContext;

	private String mySubjectTemplate;
	private String myBodyTemplate;
	private List<String> myTo;
	private String myFrom;
	private IIdType mySubscription;

	public EmailDetails() {
		myTemplateEngine = makeTemplateEngine();
		myContext = new Context();
	}

	public String getBody() {
		return myTemplateEngine.process(myBodyTemplate, myContext);
	}

	public void setBodyTemplate(String theBodyTemplate) {
		myBodyTemplate = theBodyTemplate;
	}

	public String getFrom() {
		return StringUtils.trim(myFrom);
	}

	public void setFrom(String theFrom) {
		myFrom = theFrom;
	}

	public String getSubject() {
		return myTemplateEngine.process(mySubjectTemplate, myContext);
	}

	public void setSubjectTemplate(String theSubjectTemplate) {
		mySubjectTemplate = theSubjectTemplate;
	}

	public String getSubscriptionId() {
		return mySubscription.toUnqualifiedVersionless().getValue();
	}

	public void setSubscription(IIdType theSubscription) {
		mySubscription = theSubscription;
	}

	public String getTo() {
		return myTo.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
	}

	public void setTo(List<String> theTo) {
		myTo = theTo;
	}

	public Email toEmail() {
		try {
			return EmailBuilder.startingBlank()
				.from(getFrom())
				.to(getTo())
				.withSubject(getSubject())
				.withPlainText(getBody())
				.withHeader("X-FHIR-Subscription", getSubscriptionId())
				.buildEmail();
		} catch (IllegalArgumentException e) {
			throw new InternalErrorException(Msg.code(3) + "Failed to create email message", e);
		}
	}

	@Nonnull
	private SpringTemplateEngine makeTemplateEngine() {
		StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
		stringTemplateResolver.setTemplateMode(TemplateMode.TEXT);

		SpringStandardDialect springStandardDialect = new SpringStandardDialect();
		springStandardDialect.setEnableSpringELCompiler(true);

		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		springTemplateEngine.setDialect(springStandardDialect);
		springTemplateEngine.setEnableSpringELCompiler(true);
		springTemplateEngine.setTemplateResolver(stringTemplateResolver);

		return springTemplateEngine;
	}

}
