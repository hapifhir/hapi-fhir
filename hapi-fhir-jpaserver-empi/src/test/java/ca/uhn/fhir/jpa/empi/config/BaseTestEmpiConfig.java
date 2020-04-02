package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.empi.rules.config.EmpiConfigImpl;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public abstract class BaseTestEmpiConfig {
	@Bean
	IEmpiConfig empiConfig() throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		return new EmpiConfigImpl().setEnabled(false).setScriptText(json);
	}
}
