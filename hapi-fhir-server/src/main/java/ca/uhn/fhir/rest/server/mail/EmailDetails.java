package ca.uhn.fhir.rest.server.mail;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailDetails {
	protected final SpringTemplateEngine myTemplateEngine;
	protected final Context myContext;

	private String mySubjectTemplate;
	private String myBodyTemplate;
	private List<String> myTo;
	private String myFrom;


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
				.withHeaders(Collections.unmodifiableMap(getHeaders()))
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

	protected Map<String, List<String>> getHeaders(){
		return new HashMap<>();
	}

	public String getDetails(){
		return String.format("from [%s] to recipients: [%s]", getFrom(), getTo());
	}

}
