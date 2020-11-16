package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmpiResourceDaoSvcTest extends BaseEmpiR4Test {
	private static final String TEST_EID = "TEST_EID";
	@Autowired
	EmpiResourceDaoSvc myResourceDaoSvc;

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testSearchPatientByEidExcludesNonGoldenPatients() {
		Patient goodSourcePatient = addExternalEID(createGoldenPatient(), TEST_EID);

		myPatientDao.update(goodSourcePatient);


		Patient badSourcePatient = addExternalEID(createRedirectedGoldenPatient(new Patient()), TEST_EID);
		EmpiUtil.setGoldenResourceRedirected(badSourcePatient);
		myPatientDao.update(badSourcePatient);

		Optional<IAnyResource> foundPerson = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient");
		assertTrue(foundPerson.isPresent());
		assertThat(foundPerson.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchPersonByEidExcludesNonEmpiManaged() {
		Patient goodSourcePatient = addExternalEID(createGoldenPatient(), TEST_EID);
		myPatientDao.update(goodSourcePatient);

		Patient badSourcePatient = addExternalEID(createPatient(new Patient()), TEST_EID);
		myPatientDao.update(badSourcePatient);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient");
		assertTrue(foundSourcePatient.isPresent());
		assertThat(foundSourcePatient.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
	}
}
