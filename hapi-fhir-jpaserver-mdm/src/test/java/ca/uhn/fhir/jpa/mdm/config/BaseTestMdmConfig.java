package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public abstract class BaseTestMdmConfig {

	@Value("${mdm.prevent_eid_updates:true}")
	boolean myPreventEidUpdates;

	@Value("${mdm.prevent_multiple_eids:true}")
	boolean myPreventMultipleEids;

	/**
	 * We might not want the same file for every test.
	 * See ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvcTest
	 * for an example.
	 */
	@Value("${module.mdm.config.script.file}")
	Resource myRulesFile;

	@Bean
	IMdmSettings mdmSettings(MdmRuleValidator theMdmRuleValidator) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = (myRulesFile == null || !myRulesFile.exists()) ?
			resourceLoader.getResource("mdm/mdm-rules.json") : myRulesFile;
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		return new MdmSettings(theMdmRuleValidator)
			.setEnabled(false)
			.setScriptText(json)
			.setPreventEidUpdates(myPreventEidUpdates)
			.setPreventMultipleEids(myPreventMultipleEids);
	}

	@Bean
	MdmLinkHelper mdmLinkHelper() {
		return new MdmLinkHelper();
	}
}
