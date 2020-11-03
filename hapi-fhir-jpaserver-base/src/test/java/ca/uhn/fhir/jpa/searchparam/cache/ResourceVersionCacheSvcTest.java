package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.jpa.cache.IResourceVersionMap;
import ca.uhn.fhir.jpa.cache.ResourceVersionCacheSvc;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceVersionCacheSvcTest extends BaseJpaR4Test {
	@Autowired
	ResourceVersionCacheSvc myResourceVersionCacheSvc;

	@Test
	public void testGetVersionLookup() throws IOException {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId();
		IResourceVersionMap versionMap = myResourceVersionCacheSvc.getVersionLookup("Patient", Patient.class, SearchParameterMap.newSynchronous());
		assertEquals(1, versionMap.size());
		assertEquals(patientId.getVersionIdPartAsLong(), versionMap.getVersion(patientId));
	}
}
