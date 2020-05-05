package ca.uhn.fhir.jpa.empi.helper;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.config.EmpiSettingsImpl;
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

	@Value("${empi.strict_mode:false}")
	boolean myStrictMode;

	@Primary
	@Bean
	IEmpiSettings empiProperties() throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);

		// Set Enabled to true, and set strict mode.
		return new EmpiSettingsImpl()
			.setEnabled(true)
			.setScriptText(json)
			.setStrictEidMode(myStrictMode);
	}
}
