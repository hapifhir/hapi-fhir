package ca.uhn.fhir.narrative;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.Context;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.TemplateResolver;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class ThymeleafNarrativeGenerator implements INarrativeGenerator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ThymeleafNarrativeGenerator.class);
	private TemplateEngine myTemplateEngine;
	private HashMap<String, String> myProfileToNarrativeTemplate;

	public ThymeleafNarrativeGenerator() throws IOException {
		myProfileToNarrativeTemplate = new HashMap<String, String>();

		Properties file = new Properties();
		InputStream resource = loadResource("classpath:ca/uhn/fhir/narrative/narratives.properties");
		file.load(resource);
		for (Object nextKeyObj : file.keySet()) {
			String nextKey = (String) nextKeyObj;
			if (nextKey.endsWith(".profile")) {
				String name = nextKey.substring(0, nextKey.indexOf(".profile"));
				if (isBlank(name)) {
					continue;
				}

				String narrativeName = file.getProperty(name + ".narrative");
				if (isBlank(narrativeName)) {
					continue;
				}

				String narrative = IOUtils.toString(loadResource(narrativeName));
				myProfileToNarrativeTemplate.put(file.getProperty(nextKey), narrative);
			}
		}
		myTemplateEngine = new TemplateEngine();

		TemplateResolver resolver = new TemplateResolver();
		resolver.setResourceResolver(new IResourceResolver() {
			
			@Override
			public InputStream getResourceAsStream(TemplateProcessingParameters theTemplateProcessingParameters, String theResourceName) {
				String template = myProfileToNarrativeTemplate.get(theResourceName);
				return new ReaderInputStream(new StringReader(template));
			}
			
			@Override
			public String getName() {
				return getClass().getCanonicalName();
			}
		});
		myTemplateEngine.setTemplateResolver(resolver);
		myTemplateEngine.setDialect(new StandardDialect());

		myTemplateEngine.initialize();
	}

	private InputStream loadResource(String name) {
		if (name.startsWith("classpath:")) {
			String cpName = name.substring("classpath:".length());
			InputStream resource = ThymeleafNarrativeGenerator.class.getResourceAsStream(cpName);
			if (resource == null) {
				resource = ThymeleafNarrativeGenerator.class.getResourceAsStream("/"+cpName);
				if (resource == null) {
					throw new ConfigurationException("Can not find '" + cpName + "' on classpath");
				}
			}
			return resource;
		} else {
			throw new IllegalArgumentException("Invalid resource name: '" + name + "'");
		}
	}

	public String generateString(Patient theValue) {

		Context context = new Context();
		context.setVariable("resource", theValue);
		String result = myTemplateEngine.process("ca/uhn/fhir/narrative/Patient.html", context);

		ourLog.info("Result: {}", result);

		return result;
	}

	@Override
	public NarrativeDt generateNarrative(String theProfile, IResource theResource) {
		Context context = new Context();
		context.setVariable("resource", theResource);
		String result = myTemplateEngine.process(theProfile, context);
		return new NarrativeDt(new XhtmlDt(result), NarrativeStatusEnum.GENERATED);
	}

}
