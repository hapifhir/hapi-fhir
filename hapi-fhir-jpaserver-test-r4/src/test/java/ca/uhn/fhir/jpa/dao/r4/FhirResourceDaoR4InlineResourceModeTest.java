package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4InlineResourceModeTest extends BaseJpaR4Test {

	@BeforeEach
	public void beforeSetDao() {
		myDaoConfig.setInlineResourceTextBelowSize(5000);
	}

	@AfterEach
	public void afterResetDao() {
		myDaoConfig.setInlineResourceTextBelowSize(new DaoConfig().getInlineResourceTextBelowSize());
	}

	@Test
	public void testCreateWithInlineResourceTextStorage() {
		Patient patient = new Patient();
		patient.setActive(true);
		Long resourceId = myPatientDao.create(patient).getId().getIdPartAsLong();

		patient = new Patient();
		patient.setId("Patient/" + resourceId);
		patient.setActive(false);
		myPatientDao.update(patient);

		runInTransaction(() -> {
			// Version 1
			ResourceHistoryTable entity = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(resourceId, 1);
			assertNull(entity.getResource());
			assertThat(entity.getResourceTextVc(), containsString("\"active\":true"));
			// Version 2
			entity = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(resourceId, 2);
			assertNull(entity.getResource());
			assertThat(entity.getResourceTextVc(), containsString("\"active\":false"));
		});

		patient = myPatientDao.read(new IdType("Patient/" + resourceId));
		assertFalse(patient.getActive());

		patient = (Patient) myPatientDao.search(SearchParameterMap.newSynchronous()).getAllResources().get(0);
		assertFalse(patient.getActive());

	}


	@Test
	public void testDontUseInlineAboveThreshold() {
		String veryLongFamilyName = StringUtils.leftPad("", 6000, 'a');

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addName().setFamily(veryLongFamilyName);
		Long resourceId = myPatientDao.create(patient).getId().getIdPartAsLong();

		runInTransaction(() -> {
			// Version 1
			ResourceHistoryTable entity = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(resourceId, 1);
			assertNotNull(entity.getResource());
			assertNull(entity.getResourceTextVc());
		});

		patient = myPatientDao.read(new IdType("Patient/" + resourceId));
		assertEquals(veryLongFamilyName, patient.getNameFirstRep().getFamily());
	}


	@Test
	public void testNopOnUnchangedUpdate() {
		Patient patient = new Patient();
		patient.setActive(true);
		Long resourceId = myPatientDao.create(patient).getId().getIdPartAsLong();

		patient = new Patient();
		patient.setId("Patient/" + resourceId);
		patient.setActive(true);
		DaoMethodOutcome updateOutcome = myPatientDao.update(patient);
		assertEquals("1", updateOutcome.getId().getVersionIdPart());
		assertTrue(updateOutcome.isNop());

	}


}
