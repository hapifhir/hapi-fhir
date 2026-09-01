package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Linking behaviour where a resource type is identified by several EID systems <i>and</i> the single-EID
 * safeguard is switched off, so a resource may carry several EIDs from the same system as well as one from
 * each configured system.
 * <p>
 * The two settings are independent, and the combination reaches code paths neither
 * {@link MdmMatchLinkSvcMultipleEidModeTest} (several EIDs, one system) nor
 * {@link MdmMatchLinkSvcMultiEidSystemTest} (one EID per system, safeguard on) covers.
 * </p>
 */
@TestPropertySource(properties = {
	"module.mdm.config.script.file=classpath:mdm/mdm-rules-multi-eid-systems.json",
	"mdm.prevent_multiple_eids=false"
})
// Created by claude-opus-5
public class MdmMatchLinkSvcMultiEidSystemMultipleEidModeTest extends BaseMdmR4Test {

	private String mrnSystem() {
		return patientEidSystems().get(0);
	}

	private String npiSystem() {
		return patientEidSystems().get(1);
	}

	/**
	 * The payload the per-system safeguard would reject if it were on: two EIDs from one system alongside
	 * one from another. With the safeguard off all three are accepted and all three reach the golden
	 * resource.
	 */
	@Test
	public void severalEidsFromOneSystemPlusOneFromAnother_allReachTheGoldenResource() {
		Patient patient = buildJanePatient();
		addExternalEID(patient, mrnSystem(), "mrn-1");
		addExternalEID(patient, mrnSystem(), "mrn-2");
		addExternalEID(patient, npiSystem(), "npi-9");

		Patient created = createPatientAndUpdateLinks(patient);

		assertLinksMatchResult(MATCH);
		assertThat(myEIDHelper.getExternalEid(getGoldenResourceFromTargetResource(created)))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(
				mrnSystem() + "|mrn-1", mrnSystem() + "|mrn-2", npiSystem() + "|npi-9");
	}

	/**
	 * Any one of the EIDs links, including the second value within a system rather than only the first.
	 */
	@Test
	public void patientSharingOnlyTheSecondMrn_linksToTheSameGoldenResource() {
		Patient first = buildJanePatient();
		addExternalEID(first, mrnSystem(), "mrn-1");
		addExternalEID(first, mrnSystem(), "mrn-2");
		addExternalEID(first, npiSystem(), "npi-9");
		first = createPatientAndUpdateLinks(first);

		Patient second = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), mrnSystem(), "mrn-2"));

		assertLinksMatchResult(MATCH, MATCH);
		assertLinksMatchedByEid(false, true);
		mdmAssertThat(first).is_MATCH_to(second);
	}

	/**
	 * The update-path top-up is scoped per EID system and does not read {@code prevent_multiple_eids}, so
	 * turning the safeguard off changes nothing here: an EID from a system the Golden Resource has no EID
	 * in is merged either way.
	 */
	@Test
	public void eidFromASecondSystemAddedOnUpdate_isMergedIntoTheGoldenResource() {
		Patient patient = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), mrnSystem(), "mrn-u"));

		addExternalEID(patient, npiSystem(), "npi-u");
		patient = updatePatientAndUpdateLinks(patient);

		assertLinksMatchResult(MATCH);
		assertThat(myEIDHelper.getExternalEid(getGoldenResourceFromTargetResource(patient)))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(mrnSystem() + "|mrn-u", npiSystem() + "|npi-u");
	}

	/**
	 * With the safeguard off the update path merges on system and value, exactly as the create path does,
	 * so a second EID within a system the Golden Resource already uses is merged too.
	 */
	@Test
	public void eidAddedWithinAnAlreadyPresentSystemOnUpdate_isMerged() {
		Patient patient = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1"));

		addExternalEID(patient, mrnSystem(), "mrn-2");
		patient = updatePatientAndUpdateLinks(patient);

		assertThat(myEIDHelper.getExternalEid(getGoldenResourceFromTargetResource(patient)))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(mrnSystem() + "|mrn-1", mrnSystem() + "|mrn-2");
	}

}
