package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.jpa.empi.util.EmpiHelperR4;
import org.springframework.context.annotation.Bean;

public class TestEmpiConfigR4 extends BaseTestEmpiConfig {
	@Bean
	EmpiHelperR4 empiHelper() {
		return EmpiHelperR4.newAndStartInterceptor();
	}
}
