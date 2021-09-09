package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.batch.core.explore.JobExplorer;
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

	@Bean
	IMdmSettings mdmSettings(MdmRuleValidator theMdmRuleValidator) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("mdm/mdm-rules.json");
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

	@Bean
	BatchJobHelper batchJobHelper(JobExplorer theJobExplorer) {
		return new BatchJobHelper(theJobExplorer);
	}
}
