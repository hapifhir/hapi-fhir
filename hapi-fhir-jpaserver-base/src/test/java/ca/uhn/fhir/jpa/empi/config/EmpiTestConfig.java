package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.EmpiResourceComparator;
import ca.uhn.fhir.empi.rules.EmpiRulesJson;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class EmpiTestConfig {
	@Autowired
	FhirContext myFhirContext;

	private DefaultResourceLoader myResourceLoader = new DefaultResourceLoader();

	@Bean
	public EmpiResourceComparator empiResourceComparator() throws IOException {
		Resource resource = myResourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		EmpiRulesJson rules = JsonUtil.deserialize(json, EmpiRulesJson.class);
		return new EmpiResourceComparator(myFhirContext, rules);
	}
}
