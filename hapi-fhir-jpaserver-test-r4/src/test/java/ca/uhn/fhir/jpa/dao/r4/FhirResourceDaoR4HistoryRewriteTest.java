package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR4HistoryRewriteTest extends BaseJpaR4Test {
	private static final String TEST_SYSTEM_NAME = "testHistoryRewrite";
	private static final String TEST_FAMILY_NAME = "Johnson";
	private static final String TEST_GIVEN_NAME = "Dan";
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4Test.class);

	@BeforeEach
	public void setUp() {
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(true);
	}

	@AfterEach
	public void tearDown() {
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(false);
		when(mySrd.getHeader(eq(Constants.HEADER_REWRITE_HISTORY))).thenReturn("");
	}

	@Test
	public void testHistoryRewriteNonCurrentVersion() {
		String systemNameModified = "testHistoryRewriteDiff";
		String testFamilyNameModified = "Jackson";

		// setup
		IIdType id = createPatientWithHistory();

		// execute updates
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(testFamilyNameModified);
		p.setId("Patient/" + id.getIdPart() + "/_history/2");

		Patient history2 = myPatientDao.read(id.withVersion("2"));
		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		ourLog.debug("Patient history 2: {}", history2);
		myPatientDao.update(p, mySrd);
		String versionAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		assertEquals(versionBeforeUpdate, versionAfterUpdate);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(systemNameModified);
		p.setId("Patient/" + id.getIdPart() + "/_history/1");

		Patient history1 = myPatientDao.read(id.withVersion("1"));
		ourLog.debug("Patient history 1: {}", history1);
		versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		myPatientDao.update(p, mySrd);
		versionAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		assertEquals(versionBeforeUpdate, versionAfterUpdate);

		Patient h2 = myPatientDao.read(id.withVersion("2"), mySrd);
		assertEquals(testFamilyNameModified, h2.getName().get(0).getFamily());
		assertThat(h2.getIdElement().toString()).endsWith("/_history/2");
		assertTrue(Math.abs(h2.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);

		Patient h1 = myPatientDao.read(id.withVersion("1"), mySrd);
		assertEquals(systemNameModified, h1.getIdentifier().get(0).getValue());
		assertThat(h1.getIdElement().toString()).endsWith("/_history/1");
		assertTrue(Math.abs(h1.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);
	}

	@Test
	public void testHistoryRewriteCurrentVersion() {
		String testFamilyNameModified = "Jackson";
		String testGivenNameModified = "Randy";

		// setup
		IIdType id = createPatientWithHistory();
		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(testFamilyNameModified).setGiven(List.of(new StringType(testGivenNameModified)));
		p.setId("Patient/" + id.getIdPart() + "/_history/3");

		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		myPatientDao.update(p, mySrd);
		String versionAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		assertEquals(versionBeforeUpdate, versionAfterUpdate);

		int resourceVersionsSizeAfterUpdate = myResourceHistoryTableDao.findAll().size();

		Patient lPatient = myPatientDao.read(id.toVersionless(), mySrd);
		assertEquals(testFamilyNameModified, lPatient.getName().get(0).getFamily());
		assertEquals(testGivenNameModified, lPatient.getName().get(0).getGiven().get(0).getValue());
		assertEquals(resourceVersionsSizeInit, resourceVersionsSizeAfterUpdate);
		assertThat(lPatient.getIdElement().toString()).endsWith("/_history/3");
		assertTrue(Math.abs(lPatient.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);
	}

	@Test
	public void testHistoryRewriteNoCustomHeader() {
		String testFamilyNameModified = "Jackson";

		// setup
		IIdType id = createPatientWithHistory();

		// execute update
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(testFamilyNameModified);
		p.setId("Patient/" + id.getIdPart() + "/_history/2");

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("but this is not the current version");
		}
	}

	@Test
	public void testHistoryRewriteNonExistingId() {
		String testFamilyNameModified = "Jackson";

		// setup
		IIdType id = createPatientWithHistory();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(testFamilyNameModified);
		p.setId("Patient/WrongId");

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("Doesn't exist");
		}
	}

	@Test
	public void testHistoryRewriteNonExistingVersion() {
		String testFamilyNameModified = "Jackson";

		// setup
		IIdType id = createPatientWithHistory();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(testFamilyNameModified);
		p.setId("Patient/" + id.getIdPart() + "/_history/4");

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("Doesn't exist");
		}
	}

	@Test
	public void testHistoryRewriteNoHistoryVersion() {
		String testFamilyNameModified = "Jackson";

		// setup
		IIdType id = createPatientWithHistory();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(testFamilyNameModified);
		p.setId("Patient/" + id.getIdPart());

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Invalid resource ID, ID must contain a history version");
		}
	}

	@Test
	void testHistoryRewriteDeletedVersion() {
		String TEST_FAMILY_NAME_MODIFIED = "Jackson"; // This was refactored to a constant in another ticket
		String TEST_GIVEN_NAME_MODIFIED = "Randy"; // This was refactored to a constant in another ticket

		// setup
		IIdType id = createPatientWithHistory(); // Name of this was changed to createPatientWithHistoryThreeVersions() in another ticket

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED).setGiven(List.of(new StringType(TEST_GIVEN_NAME_MODIFIED)));
		p.setId("Patient/" + id.getIdPart() + "/_history/4");

		myPatientDao.delete(id, mySrd);
		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();

		Patient patientBeforeRewrite = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd, true);
		String versionBeforeUpdate = patientBeforeRewrite.getIdElement().getVersionIdPart();
		Date lastUpdatedBeforeUpdate = patientBeforeRewrite.getMeta().getLastUpdated();

		myPatientDao.update(p, mySrd);
		String versionAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd, true).getIdElement().getVersionIdPart();
		assertEquals(versionBeforeUpdate, versionAfterUpdate);

		int resourceVersionsSizeAfterUpdate = myResourceHistoryTableDao.findAll().size();

		Patient lPatient = myPatientDao.read(id.toVersionless(), mySrd, true);
		assertEquals(TEST_FAMILY_NAME_MODIFIED, lPatient.getName().get(0).getFamily());
		assertEquals(TEST_GIVEN_NAME_MODIFIED, lPatient.getName().get(0).getGiven().get(0).getValue());
		assertEquals(resourceVersionsSizeInit, resourceVersionsSizeAfterUpdate);
		assertThat(lPatient.getIdElement().toString()).endsWith("/_history/4");
		assertFalse(lPatient.isDeleted());

		assertTrue(lPatient.getMeta().getLastUpdated().after(lastUpdatedBeforeUpdate));

		try {
			// We initially created three versions (v1-v3)
			// The logical (HTTP) delete will create a new version (v4)
			// The update with history-rewrite on v4 should probably rewrite and undelete v4 (may require design)
			myPatientDao.read(id.withVersion("5"), mySrd, true);
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("HAPI-0979: Version \"5\" is not valid for resource Patient");
		}

		//TODO - the actual version stored on the resource table is 5
		// and there are only 4 entries on the history table
		// they should _probably_ both be 4 (feels like update rewrite should update and undelete in place w/o version increment -
		// but better to run this through design)
		List<ResourceTable> allResources = myResourceTableDao.findAll();
		assertThat(allResources).hasSize(1);
		long versionOnResourceTable = allResources.get(0).getVersion();
		assertThat(versionOnResourceTable).isEqualTo(4);

		List<ResourceHistoryTable> allResourceHistories = myResourceHistoryTableDao.findAll();
		assertThat(allResourceHistories).hasSize(4);
		Optional<ResourceHistoryTable> mostRecentEntryInHistoryTable = allResourceHistories.stream().max(Comparator.comparing(ResourceHistoryTable::getVersion));
		long mostRecentVersionInHistoryTable = mostRecentEntryInHistoryTable.get().getVersion();
		assertThat(mostRecentVersionInHistoryTable).isEqualTo(4);
		assertThat(versionOnResourceTable).isEqualTo(mostRecentVersionInHistoryTable);
	}

	@Nonnull
	private IIdType createPatientWithHistory() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME);
		p.setId("Patient/" + id.getIdPart());

		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		myPatientDao.update(p, mySrd);
		String versionAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		assertThat(versionAfterUpdate).isNotEqualTo(versionBeforeUpdate);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME).setGiven(List.of(new StringType(TEST_GIVEN_NAME)));
		p.setId("Patient/" + id.getIdPart());

		versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		myPatientDao.update(p, mySrd);
		versionAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		assertThat(versionAfterUpdate).isNotEqualTo(versionBeforeUpdate);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertEquals(TEST_FAMILY_NAME, p.getName().get(0).getFamily());
		assertThat(p.getIdElement().toString()).endsWith("/_history/3");
		return id;
	}

}
