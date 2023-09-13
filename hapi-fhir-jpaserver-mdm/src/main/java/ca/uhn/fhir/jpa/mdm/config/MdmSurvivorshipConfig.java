package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.svc.MdmSurvivorshipSvcImpl;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdmSurvivorshipConfig {

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	protected DaoRegistry myDaoRegistry;

	@Autowired
	private IMdmSettings myMdmSettings;

	@Autowired
	private EIDHelper myEIDHelper;

	@Autowired
	private MdmPartitionHelper myMdmPartitionHelper;

	@Autowired
	private IMdmLinkQuerySvc myMdmLinkQuerySvc;

	@Autowired
	private IIdHelperService<?> myIIdHelperService;

	@Bean
	public IMdmSurvivorshipService mdmSurvivorshipService() {
		return new MdmSurvivorshipSvcImpl(
				myFhirContext, goldenResourceHelper(), myDaoRegistry, myMdmLinkQuerySvc, myIIdHelperService);
	}

	@Bean
	public GoldenResourceHelper goldenResourceHelper() {
		// do not make this depend on IMdmSurvivorshipService
		return new GoldenResourceHelper(myFhirContext, myMdmSettings, myEIDHelper, myMdmPartitionHelper);
	}
}
