package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import jakarta.persistence.Id;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * External provenance entities are deprecated, but we make sure we
 * still read them when configured to do so.
 */
public class FhirResourceDaoR5ExternalProvenanceEntityTest extends BaseJpaR5Test {

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testRead(boolean theEnableExternalRead) {
		// Setup
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(theEnableExternalRead);
		createPatientWithExternalProvenanceEntity();

		// Test
		Patient actual = myPatientDao.read(new IdType("Patient/P"), mySrd);

		// Verify
		if (theEnableExternalRead) {
			assertEquals("http://foo#bar", actual.getMeta().getSource());
		} else {
			assertNull(actual.getMeta().getSource());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch(boolean theEnableExternalRead) {
		// Setup
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(theEnableExternalRead);
		createPatientWithExternalProvenanceEntity();

		// Test
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		Patient actual = (Patient) outcome.getResources(0, 1).get(0);

		// Verify
		if (theEnableExternalRead) {
			assertEquals("http://foo#bar", actual.getMeta().getSource());
		} else {
			assertNull(actual.getMeta().getSource());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHistory(boolean theEnableExternalRead) {
		// Setup
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(theEnableExternalRead);
		createPatientWithExternalProvenanceEntity();

		// Test
		IBundleProvider outcome = myPatientDao.history(null, null, null, mySrd);
		Patient actual = (Patient) outcome.getResources(0, 1).get(0);

		// Verify
		if (theEnableExternalRead) {
			assertEquals("http://foo#bar", actual.getMeta().getSource());
		} else {
			assertNull(actual.getMeta().getSource());
		}
	}

	private void createPatientWithExternalProvenanceEntity() {
		Patient p = new Patient();
		p.setId("P");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		runInTransaction(()->{
			List<ResourceHistoryTable> versions = myResourceHistoryTableDao.findAll();
			for (var version : versions) {
				ResourceHistoryProvenanceEntity provenance = new ResourceHistoryProvenanceEntity();
				provenance.setResourceTable(version.getResourceTable());
				provenance.setResourceHistoryTable(version);
				provenance.setSourceUri("http://foo");
				provenance.setRequestId("bar");
				myResourceHistoryProvenanceDao.save(provenance);
			}
		});
	}
}
