package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.svc.EmpiRulesSvc;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@ComponentScan()
public class TestEmpiConfig {
	@Bean
	EmpiRulesSvc empiRulesRegistry() throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		EmpiRulesJson rules = JsonUtil.deserialize(json, EmpiRulesJson.class);
		return new EmpiRulesSvc(rules);
	}
}
