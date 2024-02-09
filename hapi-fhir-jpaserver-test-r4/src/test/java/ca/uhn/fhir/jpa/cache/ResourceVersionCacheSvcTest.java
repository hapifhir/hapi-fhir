package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceVersionCacheSvcTest extends BaseJpaR4Test {
	@Autowired
	IResourceVersionSvc myResourceVersionCacheSvc;

	@Test
	public void testGetVersionMap() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId();
		ResourceVersionMap versionMap = myResourceVersionCacheSvc.getVersionMap("Patient", SearchParameterMap.newSynchronous());
		assertThat(versionMap.size()).isEqualTo(1);
		assertThat(versionMap.getVersion(patientId)).isEqualTo(1L);

		patient.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.update(patient);
		versionMap = myResourceVersionCacheSvc.getVersionMap("Patient", SearchParameterMap.newSynchronous());
		assertThat(versionMap.size()).isEqualTo(1);
		assertThat(versionMap.getVersion(patientId)).isEqualTo(2L);
	}
}
