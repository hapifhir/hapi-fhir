package ca.uhn.fhir.narrative;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

public class ThymeleafResolver implements ITemplateResolver {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ThymeleafResolver.class);

	@Override
	public String getName() {
		return "";
	}

	@Override
	public Integer getOrder() {
		return 0;
	}

	@Override
	public void initialize() {
		// nothing
	}

	@Override
	public TemplateResolution resolveTemplate(TemplateProcessingParameters theTemplateProcessingParameters) {

		String templateName = theTemplateProcessingParameters.getTemplateName();
		ourLog.info("Resolving template: {}", templateName);

		return null;
	}

}
