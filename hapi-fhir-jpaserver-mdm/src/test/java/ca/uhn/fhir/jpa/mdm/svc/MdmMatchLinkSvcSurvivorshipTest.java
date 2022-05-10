package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmMatchLinkSvcSurvivorshipTest extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmMatchLinkSvcSurvivorshipTest.class);

	@Autowired
	IMdmLinkSvc myMdmLinkSvc;

	@SpyBean
	IMdmSurvivorshipService myMdmSurvivorshipService;

	@Autowired
	private GoldenResourceHelper myGoldenResourceHelper;

	@Captor
	ArgumentCaptor<Patient> myPatientCaptor;
	@Captor
	ArgumentCaptor<MdmTransactionContext> myContext;

	@Test
	public void testSurvivorshipIsCalledOnMatchingToTheSameGoldenResource() {
		// no candidates
		createPatientAndUpdateLinks(buildJanePatient());
		verifySurvivorshipCalled(1);

		// single candidate
		createPatientAndUpdateLinks(buildJanePatient());
		verifySurvivorshipCalled(2);

		// multiple candidates matching to the same golden record
		createPatientAndUpdateLinks(buildJanePatient());
		verifySurvivorshipCalled(3);
	}

	private void verifySurvivorshipCalled(int theNumberOfTimes) {
		Mockito.verify(myMdmSurvivorshipService, times(theNumberOfTimes)).applySurvivorshipRulesToGoldenResource(myPatientCaptor.capture(), myPatientCaptor.capture(), myContext.capture());
	}
}
