package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiBatchService;
import ca.uhn.fhir.empi.api.IEmpiResetSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.provider.EmpiProviderR4;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.validation.IResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseProviderR4Test extends BaseEmpiR4Test {
	EmpiProviderR4 myEmpiProviderR4;
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	@Autowired
	private IEmpiPersonMergerSvc myPersonMergerSvc;
	@Autowired
	private IEmpiLinkUpdaterSvc myEmpiLinkUpdaterSvc;
	@Autowired
	private IEmpiLinkQuerySvc myEmpiLinkQuerySvc;
	@Autowired
	private IResourceLoader myResourceLoader;
	@Autowired
	private IEmpiResetSvc myEmpiExpungeSvc;
	@Autowired
	private IEmpiBatchService myEmpiBatchSvc;

	@BeforeEach
	public void before() {
		myEmpiProviderR4 = new EmpiProviderR4(myFhirContext, myEmpiMatchFinderSvc, myPersonMergerSvc, myEmpiLinkUpdaterSvc, myEmpiLinkQuerySvc, myResourceLoader, myEmpiExpungeSvc, myEmpiBatchSvc);
	}
}
