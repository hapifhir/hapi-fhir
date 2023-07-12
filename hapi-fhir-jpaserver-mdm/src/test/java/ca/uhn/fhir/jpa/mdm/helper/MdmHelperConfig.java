package ca.uhn.fhir.jpa.mdm.helper;

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class MdmHelperConfig {
	@Bean
	public MdmHelperR4 mdmHelperR4() {
		return new MdmHelperR4();
	}

	@Value("${mdm.prevent_eid_updates:false}")
	boolean myPreventEidUpdates;

	@Value("${mdm.prevent_multiple_eids:true}")
	boolean myPreventMultipleEids;

	@Primary
	@Bean
	IMdmSettings mdmSettings(MdmRuleValidator theMdmRuleValidator) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("mdm/mdm-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);

		// Set Enabled to true, and set strict mode.
		return new MdmSettings(theMdmRuleValidator)
			.setEnabled(true)
			.setScriptText(json)
			.setPreventEidUpdates(myPreventEidUpdates)
			.setPreventMultipleEids(myPreventMultipleEids);
	}
}
