package ca.uhn.fhir.empi.provider;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiProviderLoader {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myResourceProviderFactory.addSupplier(() -> new EmpiProviderDstu3(myEmpiMatchFinderSvc));
				break;
			case R4:
				myResourceProviderFactory.addSupplier(() -> new EmpiProviderR4(myEmpiMatchFinderSvc));
				break;
			default:
				throw new ConfigurationException("LiveBundle not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}
	}
}

