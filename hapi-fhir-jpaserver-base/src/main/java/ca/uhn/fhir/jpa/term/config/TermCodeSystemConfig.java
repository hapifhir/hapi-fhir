package ca.uhn.fhir.jpa.term.config;

import ca.uhn.fhir.jpa.term.TermConceptDaoSvc;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.TermCodeSystemDeleteJobSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TermCodeSystemConfig {

	@Bean
	public ITermCodeSystemDeleteJobSvc termCodeSystemService() {
		return new TermCodeSystemDeleteJobSvc();
	}

	@Bean
	public ITermDeferredStorageSvc termDeferredStorageSvc() {
		return new TermDeferredStorageSvcImpl();
	}

	@Bean
	public TermConceptDaoSvc termConceptDaoSvc() {
		return new TermConceptDaoSvc();
	}
}
