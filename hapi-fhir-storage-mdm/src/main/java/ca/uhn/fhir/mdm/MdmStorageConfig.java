package ca.uhn.fhir.mdm;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.svc.MdmChannelSubmitterSvcImpl;
import ca.uhn.fhir.mdm.svc.MdmLinkDeleteSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkExpandSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MdmStorageConfig {
	@Bean
	MdmRuleValidator mdmRuleValidator(FhirContext theFhirContext, ISearchParamRegistry theSearchParamRetriever) {
		return new MdmRuleValidator(theFhirContext, theSearchParamRetriever);
	}

	@Bean
	@Lazy
	IMdmChannelSubmitterSvc mdmChannelSubmitterSvc(FhirContext theFhirContext, IChannelFactory theChannelFactory) {
		return new MdmChannelSubmitterSvcImpl(theFhirContext, theChannelFactory);
	}

	@Bean
	MdmLinkDeleteSvc mdmLinkDeleteSvc() {
		return new MdmLinkDeleteSvc();
	}

	@Bean
	public IMdmLinkExpandSvc mdmLinkExpandSvc() {
		return new MdmLinkExpandSvc();
	}
}
