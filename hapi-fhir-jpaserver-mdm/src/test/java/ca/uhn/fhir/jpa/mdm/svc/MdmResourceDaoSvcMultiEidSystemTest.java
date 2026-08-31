package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Golden resource lookup by EID must match on the (system, value) pair. With more than one EID system
 * configured for a resource type, matching on value alone would conflate an MRN with an NPI that
 * happens to read the same.
 */
@TestPropertySource(properties = {"module.mdm.config.script.file=classpath:mdm/mdm-rules-multi-eid-systems.json"})
// Created by claude-opus-5
public class MdmResourceDaoSvcMultiEidSystemTest extends BaseMdmR4Test {

	@Autowired
	IMdmResourceDaoSvc myResourceDaoSvc;

	@Test
	public void searchGoldenResourcesByEIDs_valueCollidesAcrossSystems_returnsOnlyThePairMatch() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		Patient mrnGolden = addExternalEID(createGoldenPatient(), mrnSystem, "123");
		myPatientDao.update(mrnGolden, mySrd);
		Patient npiGolden = addExternalEID(createGoldenPatient(), npiSystem, "123");
		myPatientDao.update(npiGolden, mySrd);

		List<IAnyResource> found = myResourceDaoSvc.searchGoldenResourcesByEIDs(
			List.of(new CanonicalEID(mrnSystem, "123", null)), "Patient", null);

		assertThat(found).hasSize(1);
		assertThat(found.get(0).getIdElement().toUnqualifiedVersionless().getValue())
			.isEqualTo(mrnGolden.getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void searchGoldenResourcesByEIDs_twoEidsResolvingToDifferentGoldenResources_returnsBoth() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		Patient mrnGolden = addExternalEID(createGoldenPatient(), mrnSystem, "mrn-1");
		myPatientDao.update(mrnGolden, mySrd);
		Patient npiGolden = addExternalEID(createGoldenPatient(), npiSystem, "npi-9");
		myPatientDao.update(npiGolden, mySrd);

		List<IAnyResource> found = myResourceDaoSvc.searchGoldenResourcesByEIDs(
			List.of(new CanonicalEID(mrnSystem, "mrn-1", null), new CanonicalEID(npiSystem, "npi-9", null)),
			"Patient", null);

		assertThat(found).hasSize(2);
	}

	@Test
	public void searchGoldenResourcesByEIDs_bothEidsOnTheSameGoldenResource_returnsItOnce() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		Patient golden = addExternalEID(createGoldenPatient(), mrnSystem, "mrn-1");
		addExternalEID(golden, npiSystem, "npi-9");
		myPatientDao.update(golden, mySrd);

		List<IAnyResource> found = myResourceDaoSvc.searchGoldenResourcesByEIDs(
			List.of(new CanonicalEID(mrnSystem, "mrn-1", null), new CanonicalEID(npiSystem, "npi-9", null)),
			"Patient", null);

		assertThat(found).hasSize(1);
	}

	/**
	 * Two golden resources carrying the same EID is still corruption, and must still be reported.
	 */
	@Test
	public void searchGoldenResourcesByEIDs_onePairResolvingToTwoGoldenResources_throws() {
		String mrnSystem = patientEidSystems().get(0);

		myPatientDao.update(addExternalEID(createGoldenPatient(), mrnSystem, "mrn-1"), mySrd);
		myPatientDao.update(addExternalEID(createGoldenPatient(), mrnSystem, "mrn-1"), mySrd);

		assertThatThrownBy(() -> myResourceDaoSvc.searchGoldenResourcesByEIDs(
			List.of(new CanonicalEID(mrnSystem, "mrn-1", null)), "Patient", null))
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining(Msg.code(737));
	}

	@Test
	public void searchGoldenResourcesByEIDs_noEidsGiven_returnsEmpty() {
		assertThat(myResourceDaoSvc.searchGoldenResourcesByEIDs(List.of(), "Patient", null)).isEmpty();
	}
}
