package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The "prevent multiple EIDs" safeguard once a resource type is identified by more than one EID system.
 */
@ContextConfiguration(classes = {MdmHelperConfig.class})
@TestPropertySource(properties = {
	"mdm.rules.file=mdm/mdm-rules-multi-eid-systems.json",
	"mdm.prevent_multiple_eids=true",
	"mdm.prevent_eid_updates=true"
})
// Created by claude-opus-5
public class MdmStorageInterceptorMultiEidSystemIT extends BaseMdmR4Test {

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	private String mrnSystem() {
		return patientEidSystems().get(0);
	}

	private String npiSystem() {
		return patientEidSystems().get(1);
	}

	/**
	 * The point of the feature: one MRN and one NPI on a single Patient must be accepted at the default
	 * setting, without asking operators to switch the safeguard off wholesale.
	 */
	@Test
	public void oneEidPerConfiguredSystem_isAccepted() throws InterruptedException {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(patient, npiSystem(), "npi-9");

		myMdmHelper.createWithLatch(patient);

		assertThat(myEIDHelper.getExternalEid(patient)).hasSize(2);
	}

	@Test
	public void twoEidsFromTheSameSystem_isRejected() {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(patient, mrnSystem(), "mrn-2");

		assertThatThrownBy(() -> myMdmHelper.doCreateResource(patient, true))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining("HAPI-0766")
			.hasMessageContaining("at most one EID per system")
			.hasMessageContaining(mrnSystem());
	}

	/**
	 * The guard counts identifiers, not distinct system/value pairs, so a repeated EID is rejected just as
	 * it was before the safeguard became per-system. Worth pinning separately from the differing-values
	 * case: de-duplicating first - as {@code eidMatchExists} and {@code addCanonicalEidsToGoldenResourceIfAbsent}
	 * legitimately do - would let this through while still rejecting two different values.
	 */
	@Test
	public void sameEidValueRepeatedInOneSystem_isRejected() {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(patient, mrnSystem(), "mrn-1");

		assertThatThrownBy(() -> myMdmHelper.doCreateResource(patient, true))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining("HAPI-0766");
	}

	/**
	 * "Prevent EID updates" is scoped per EID system: an EID may not be swapped within a system that
	 * already had one, even when an EID from another system is left in place.
	 */
	@Test
	public void preventEidUpdates_eidOfOneSystemSwappedWhileAnotherIsKept_isRejected() throws InterruptedException {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(patient, npiSystem(), "npi-9");
		MdmHelperR4.OutcomeAndLogMessageWrapper outcome = myMdmHelper.createWithLatch(patient);
		patient.setId(outcome.getDaoMethodOutcome().getId());

		patient.getIdentifier().removeIf(identifier -> npiSystem().equals(identifier.getSystem()));
		addExternalEID(patient, npiSystem(), "npi-7");

		assertThatThrownBy(() -> myMdmHelper.doUpdateResource(patient, true))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining("HAPI-0763");
	}

	/**
	 * Removing an EID is a modification too: dropping the NPI while keeping the MRN is rejected.
	 */
	@Test
	public void preventEidUpdates_eidOfOneSystemRemovedWhileAnotherIsKept_isRejected() throws InterruptedException {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(patient, npiSystem(), "npi-9");
		MdmHelperR4.OutcomeAndLogMessageWrapper outcome = myMdmHelper.createWithLatch(patient);
		patient.setId(outcome.getDaoMethodOutcome().getId());

		patient.getIdentifier().removeIf(identifier -> npiSystem().equals(identifier.getSystem()));

		assertThatThrownBy(() -> myMdmHelper.doUpdateResource(patient, true))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining("HAPI-0763");
	}

	/**
	 * Gaining an EID from a system the resource did not previously use is an addition, not an update, and
	 * remains accepted - it is how a record learns its second identifier.
	 */
	@Test
	public void preventEidUpdates_eidOfANewSystemAdded_isAccepted() throws InterruptedException {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		MdmHelperR4.OutcomeAndLogMessageWrapper outcome = myMdmHelper.createWithLatch(patient);
		patient.setId(outcome.getDaoMethodOutcome().getId());

		addExternalEID(patient, npiSystem(), "npi-9");
		myMdmHelper.updateWithLatch(patient);

		assertThat(myEIDHelper.getExternalEid(patient)).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(mrnSystem() + "|mrn-1", npiSystem() + "|npi-9");
	}

}
