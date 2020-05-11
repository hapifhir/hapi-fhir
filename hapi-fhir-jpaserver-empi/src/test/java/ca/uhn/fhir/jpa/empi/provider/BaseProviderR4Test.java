package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.provider.EmpiProviderR4;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.validation.IResourceLoader;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseProviderR4Test extends BaseEmpiR4Test {
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	@Autowired
	private IEmpiPersonMergerSvc myPersonMergerSvc;
	@Autowired
	private IEmpiLinkUpdaterSvc myEmpiLinkUpdaterSvc;
	@Autowired
	private IResourceLoader myResourceLoader;

	EmpiProviderR4 myEmpiProviderR4;

	@Before
	public void before() {
		myEmpiProviderR4 = new EmpiProviderR4(myFhirContext, myEmpiMatchFinderSvc, myPersonMergerSvc, myEmpiLinkUpdaterSvc, myResourceLoader);
	}
}
