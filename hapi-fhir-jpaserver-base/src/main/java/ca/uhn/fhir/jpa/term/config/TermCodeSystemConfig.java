package ca.uhn.fhir.jpa.term.config;

import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.api.TermCodeSystemSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TermCodeSystemConfig {

	@Bean
	public ITermCodeSystemSvc termCodeSystemService() {
		return new TermCodeSystemSvc();
	}
}
