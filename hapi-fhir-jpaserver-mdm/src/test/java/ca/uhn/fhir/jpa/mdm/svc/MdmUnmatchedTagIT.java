package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the mdm-unmatched tag over the broker, where MDM runs on its own thread against the resource carried in
 * the message rather than one the test handed it. The synchronous tests in {@link MdmMatchLinkSvcTest} cannot show
 * whether that payload still carries the tag, which is what decides if the tag can be cleared at all.
 */
public class MdmUnmatchedTagIT extends BaseMdmR4Test {

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	private int myOriginalCandidateSearchLimit;

	@AfterEach
	public void restoreCandidateSearchLimit() {
		myMdmSettings.setCandidateSearchLimit(myOriginalCandidateSearchLimit);
	}

	@Test
	public void asyncResubmission_ofAResourceThatNowMatches_clearsTheUnmatchedTag() throws InterruptedException {
		myOriginalCandidateSearchLimit = myMdmSettings.getCandidateSearchLimit();

		// setup - one jane already in the repository to act as a candidate, then a limit low enough that the
		// next one cannot be narrowed down
		Patient firstJane = buildJanePatient();
		firstJane.setActive(true);
		myMdmHelper.createWithLatch(firstJane);

		myMdmSettings.setCandidateSearchLimit(1);

		Patient jane = buildJanePatient();
		jane.setActive(true);
		IIdType id = myMdmHelper.createWithLatch(jane).getDaoMethodOutcome()
				.getId()
				.toUnqualifiedVersionless();
		assertTrue(MdmResourceUtil.resourceHasTagWithSystem(
				myPatientDao.read(id, new SystemRequestDetails()), MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE));

		// test - with the limit restored, resubmit her and let the broker drive the MDM pass
		myMdmSettings.setCandidateSearchLimit(myOriginalCandidateSearchLimit);
		Patient resubmitted = myPatientDao.read(id, new SystemRequestDetails());
		// an update with identical content is a no-op, and a no-op is never submitted to MDM. Gender is neither a
		// candidate search param nor a match field in the test rules, so this changes the resource without
		// changing who it matches.
		resubmitted.setGender(Enumerations.AdministrativeGender.FEMALE);
		myMdmHelper.updateWithLatch(resubmitted);

		// verify
		assertFalse(MdmResourceUtil.resourceHasTagWithSystem(
				myPatientDao.read(id, new SystemRequestDetails()), MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE));
	}
}
