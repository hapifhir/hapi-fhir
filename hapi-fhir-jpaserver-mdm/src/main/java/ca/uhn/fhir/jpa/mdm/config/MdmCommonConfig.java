package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.mdm.MdmBatchJobSubmitterFactoryImpl;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDeleteSvc;
import ca.uhn.fhir.jpa.interceptor.MdmSearchExpandingInterceptor;
import ca.uhn.fhir.mdm.api.IMdmBatchJobSubmitterFactory;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MdmCommonConfig {
	@Bean
	MdmRuleValidator mdmRuleValidator(FhirContext theFhirContext, ISearchParamRegistry theSearchParamRetriever) {
		return new MdmRuleValidator(theFhirContext, theSearchParamRetriever);
	}
	@Bean
	@Lazy
	public MdmSearchExpandingInterceptor mdmSearchExpandingInterceptor() {
		return new MdmSearchExpandingInterceptor();
	}

	@Bean
	IMdmBatchJobSubmitterFactory mdmBatchJobSubmitterFactory() {
		return new MdmBatchJobSubmitterFactoryImpl();
	}

	@Bean
	MdmLinkDeleteSvc mdmLinkDeleteSvc() {
		return new MdmLinkDeleteSvc();
	}

}
