package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.util.ResourceProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class EmpiProviderLoader {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;
	@Autowired
	ApplicationContext myApplicationContext;

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myResourceProviderFactory.addSupplier(() -> myApplicationContext.getBean(EmpiProviderDstu3.class));
				break;
			case R4:
				myResourceProviderFactory.addSupplier(() -> myApplicationContext.getBean(EmpiProviderR4.class));
				break;
			default:
				throw new ConfigurationException("LiveBundle not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}
	}
}

