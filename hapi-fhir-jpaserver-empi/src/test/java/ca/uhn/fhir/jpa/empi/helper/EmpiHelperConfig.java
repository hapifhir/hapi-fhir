package ca.uhn.fhir.jpa.empi.helper;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class EmpiHelperConfig {
	@Bean
	public EmpiHelperR4 empiHelperR4() {
		return new EmpiHelperR4();
	}

	@Value("${empi.prevent_eid_updates:false}")
	boolean myPreventEidUpdates;

	@Value("${empi.prevent_multiple_eids:true}")
	boolean myPreventMultipleEids;

	@Primary
	@Bean
	IEmpiSettings empiSettings(EmpiRuleValidator theEmpiRuleValidator) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);

		// Set Enabled to true, and set strict mode.
		return new EmpiSettings(theEmpiRuleValidator)
			.setEnabled(true)
			.setScriptText(json)
			.setPreventEidUpdates(myPreventEidUpdates)
			.setPreventMultipleEids(myPreventMultipleEids);
	}
}
