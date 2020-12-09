package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.jpa.empi.helper.EmpiLinkHelper;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public abstract class BaseTestEmpiConfig {

	@Value("${empi.prevent_eid_updates:true}")
	boolean myPreventEidUpdates;

	@Value("${empi.prevent_multiple_eids:true}")
	boolean myPreventMultipleEids;

	@Bean
    IEmpiSettings empiSettings(EmpiRuleValidator theEmpiRuleValidator) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		return new EmpiSettings(theEmpiRuleValidator)
			.setEnabled(false)
			.setScriptText(json)
			.setPreventEidUpdates(myPreventEidUpdates)
			.setPreventMultipleEids(myPreventMultipleEids);
	}

	@Bean
	EmpiLinkHelper empiLinkHelper() {
		return new EmpiLinkHelper();
	}
}
