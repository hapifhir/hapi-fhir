package ca.uhn.fhir.jpa.empi.helper;

import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.empi.rules.config.EmpiPropertiesImpl;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
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

	@Primary
	@Bean
   IEmpiProperties empiProperties() throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		// Set Enabled to true
		return new EmpiPropertiesImpl().setEnabled(true).setScriptText(json);
	}
}
