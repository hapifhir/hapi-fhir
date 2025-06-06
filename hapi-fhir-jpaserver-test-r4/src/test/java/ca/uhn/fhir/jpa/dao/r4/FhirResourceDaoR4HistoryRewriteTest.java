package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
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
	public static final String TEST_FAMILY_NAME_MODIFIED = "Jackson";
	public static final String TEST_GIVEN_NAME_MODIFIED = "Randy";
	public static final String TEST_BIRTHDATE_MODIFIED = "2025-05-22";
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
	void testHistoryRewriteNonCurrentVersion() {
		String systemNameModified = "testHistoryRewriteDiff";

		// setup
		IIdType id = createPatientWithHistoryThreeVersions();

		// execute updates
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED);
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
		assertEquals(TEST_FAMILY_NAME_MODIFIED, h2.getName().get(0).getFamily());
		assertThat(h2.getIdElement().toString()).endsWith("/_history/2");
		assertTrue(Math.abs(h2.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);

		Patient h1 = myPatientDao.read(id.withVersion("1"), mySrd);
		assertEquals(systemNameModified, h1.getIdentifier().get(0).getValue());
		assertThat(h1.getIdElement().toString()).endsWith("/_history/1");
		assertTrue(Math.abs(h1.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);
	}

	@Test
	void testHistoryRewriteCurrentVersion() {
		// setup
		IIdType id = createPatientWithHistoryThreeVersions();
		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED).setGiven(List.of(new StringType(TEST_GIVEN_NAME_MODIFIED)));
		p.setId("Patient/" + id.getIdPart() + "/_history/3");

		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless()).getIdElement().getVersionIdPart();
		myPatientDao.update(p, mySrd);

		assertPatientNameWasModified(id.toVersionless(), "3");
		assertResourceVersionHasNotIncremented(id, versionBeforeUpdate, resourceVersionsSizeInit);
	}

	@Test
	void testHistoryRewriteNoCustomHeader() {
		// setup
		IIdType id = createPatientWithHistoryThreeVersions();

		// execute update
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED);
		p.setId("Patient/" + id.getIdPart() + "/_history/2");

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("but this is not the current version");
		}
	}

	@Test
	void testHistoryRewriteNonExistingId() {
		// setup
		createPatientWithHistoryThreeVersions();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED);
		p.setId("Patient/WrongId");

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("Doesn't exist");
		}
	}

	@Test
	void testHistoryRewriteNonExistingVersion() {
		// setup
		IIdType id = createPatientWithHistoryThreeVersions();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED);
		p.setId("Patient/" + id.getIdPart() + "/_history/4");

		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("Doesn't exist");
		}
	}

	@Test
	void testHistoryRewriteNoHistoryVersion() {
		// setup
		IIdType id = createPatientWithHistoryThreeVersions();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED);
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
		// setup
		IIdType id = createPatientWithHistoryThreeVersions();

		// execute update
		when(mySrd.isRewriteHistory()).thenReturn(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(TEST_SYSTEM_NAME);
		p.addName().setFamily(TEST_FAMILY_NAME_MODIFIED).setGiven(List.of(new StringType(TEST_GIVEN_NAME_MODIFIED)));
		p.setId("Patient/" + id.getIdPart() + "/_history/4");

		myPatientDao.delete(id, mySrd);
		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();

		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd, true).getIdElement().getVersionIdPart();
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
		// they should _probably_ both be 4
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






		//todo jdjd fails here due to incrementing history
		// since it was version 3, delete = v4, update = v5
		// request by versionless should be v5??
		// try manual patient-d v1, del = v2, update(rewrite) on v2 = v3 --> yup it does go to v3 but when querying instance/_history it doesn't return all versions
		// looking at the history table, it has resource versions v1,v2 BUT the resource table the resource version is set to 3
		// so v5 is a valid version somehow but the response is v4

		// check tables for regular delete/update --> patient-e
		// create patient-e, delete patient-e, update patient-e --> patient-e/_history = returns 3
		// in the history table:  has 3 entries, the res_text is null for the deleted resource
		// in the resource table:  resource version is 3

		// in the case of rewrite history additional version (1452) --> when you create, delete, update(rewrite), update(regular) --> increments version to 4, but 3 is inaccessible (not valid version of resource)
		// so what happens is the we apply the patch on the entity of the old deleted version
		// does this change null to an actual body res_text_vc in history table --> impossible because it happens with update as well
		// could it be update history doUpdateWithHistoryRewrite()  -->  updateHistoryEntity()
		// problem with design maybe?
		// --> when it is deleted, and you rewrite history, there is nothing to rewrite because it is a deleted version
		// --> since it tries to undelete, it this force creates a new version in the resource table
		// --> but then because it's history rewrite, we forcefully do not make a new version in the history table and it desyncs??
		assertTrue(Math.abs(lPatient.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);
	}

	@Test
	void testPatchHistoryRewrite_onCurrentVersion_succeeds() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();
		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();
		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getIdElement().getVersionIdPart();

		// When
		when(mySrd.isRewriteHistory()).thenReturn(true);
		Parameters patch = createPatchReplaceNamesParams();

		IIdType idHistoryV3 = id.withVersion(versionBeforeUpdate);
		Date lastUpdatedBeforeRewriteV3 =  myPatientDao.read(idHistoryV3, mySrd).getMeta().getLastUpdated();
		runInTransaction(() -> myPatientDao.patch(idHistoryV3, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd));

		// Then
		assertResourceVersionHasNotIncremented(id, versionBeforeUpdate, resourceVersionsSizeInit);
		assertPatientNameWasModified(id.toVersionless(), "3");
		assertLastUpdatedWasModifiedOnVersion(idHistoryV3, lastUpdatedBeforeRewriteV3);
	}

	@Test
	void testPatchHistoryRewrite_withNonCurrentVersion_succeeds() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();
		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();

		// When
		when(mySrd.isRewriteHistory()).thenReturn(true);
		Parameters patchName = createPatchReplaceNamesParams();

		String latestVersionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getIdElement().getVersionIdPart();
		IIdType idHistoryV2 = id.withVersion("2");
		Date lastUpdatedBeforeRewriteV2 =  myPatientDao.read(idHistoryV2, mySrd).getMeta().getLastUpdated();
		runInTransaction(() -> myPatientDao.patch(idHistoryV2, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patchName, mySrd));

		// Then
		assertResourceVersionHasNotIncremented(id, latestVersionBeforeUpdate, resourceVersionsSizeInit);
		assertPatientNameWasModified(idHistoryV2, idHistoryV2.getVersionIdPart());
		assertLastUpdatedWasModifiedOnVersion(idHistoryV2, lastUpdatedBeforeRewriteV2);

		// When: try on the first version as well
		Parameters patchBirthDate = createPatchAddBirthDateParams();

		IIdType idHistoryV1 = id.withVersion("1");
		Date lastUpdatedBeforeRewriteV1 =  myPatientDao.read(idHistoryV2, mySrd).getMeta().getLastUpdated();
		runInTransaction(() -> myPatientDao.patch(idHistoryV1, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patchBirthDate, mySrd));

		// Then
		assertResourceVersionHasNotIncremented(id, latestVersionBeforeUpdate, resourceVersionsSizeInit);
		assertPatientBirthDateWasModified(idHistoryV1, idHistoryV1.getVersionIdPart());
		assertLastUpdatedWasModifiedOnVersion(idHistoryV1, lastUpdatedBeforeRewriteV1);
	}

	@Test
	void testPatchHistoryRewrite_withNoRewriteHeader_failsWithVersionConflict() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();
		// When
		Parameters patch = createPatchReplaceNamesParams();
		// Then
		doPatchAndAssertResourceConflict(patch, id);
	}

	@Test
	void testPatchHistoryRewrite_withRewriteDisabled_failsWithVersionConflict() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();

		// When
		Parameters patch = createPatchReplaceNamesParams();
		when(mySrd.isRewriteHistory()).thenReturn(true);
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(false);

		// Then
		doPatchAndAssertResourceConflict(patch, id);
	}

	private void doPatchAndAssertResourceConflict(Parameters thePatchParams, IIdType theIdToPatch) {
		IIdType idHistoryV2 = theIdToPatch.withVersion("2");
		try {
			myPatientDao.patch(idHistoryV2, null, PatchTypeEnum.FHIR_PATCH_JSON, null, thePatchParams, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("HAPI-0974: Version 2 is not the most recent version of this resource, unable to apply patch");
		}

		IIdType idHistoryV1 = theIdToPatch.withVersion("1");
		try {
			myPatientDao.patch(idHistoryV1, null, PatchTypeEnum.FHIR_PATCH_JSON, null, thePatchParams, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("HAPI-0974: Version 1 is not the most recent version of this resource, unable to apply patch");
		}
	}

	@Test
	void testPatchHistoryRewrite_withNonExistingId_failsWithNotFound() {
		// Given
		createPatientWithHistoryThreeVersions();

		// When
		when(mySrd.isRewriteHistory()).thenReturn(true);
		Parameters patch = createPatchReplaceNamesParams();

		try {
			myPatientDao.patch(new IdType("Patient/WrongId/_history/1"), null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// Then
			assertThat(e.getMessage()).contains("HAPI-2001: Resource Patient/WrongId is not known");
		}
	}

	@Test
	void testPatchHistoryRewrite_withNonExistingVersion_failsWithNotFound() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();

		// When
		when(mySrd.isRewriteHistory()).thenReturn(true);
		Parameters patch = createPatchReplaceNamesParams();

		IIdType idV4 = id.withVersion("4");
		try {
			myPatientDao.patch(idV4, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// Then
			assertThat(e.getMessage()).contains("HAPI-0979: Version \"4\" is not valid for resource Patient");
		}
	}

	@Test
	void testPatchHistoryRewrite_requestHasNoHistoryVersion_failsWithBadRequest() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();

		// When
		when(mySrd.isRewriteHistory()).thenReturn(true);
		Parameters patch = createPatchReplaceNamesParams();

		try {
			myPatientDao.patch(id.toVersionless(), null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			// Then
			assertThat(e.getMessage()).contains("Invalid resource ID for rewrite history: ID must contain a history version");
		}
	}

	@Test
	void testPatchHistoryRewrite_onDeletedResourceVersion_failsWithResourceGone() {
		// Given
		IIdType id = createPatientWithHistoryThreeVersions();

		String versionBeforeDelete = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getIdElement().getVersionIdPart();
		myPatientDao.delete(id, mySrd);

		int resourceVersionsSizeInit = myResourceHistoryTableDao.findAll().size();
		String versionBeforeUpdate = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd, true).getIdElement().getVersionIdPart();

		IIdType idHistoryV4 = id.withVersion(versionBeforeUpdate);
		Date lastUpdatedBeforeRewriteV4 = myPatientDao.read(idHistoryV4, mySrd, true).getMeta().getLastUpdated();

		// When
		when(mySrd.isRewriteHistory()).thenReturn(true);
		Parameters patch = createPatchReplaceNamesParams();

		try {
			myPatientDao.patch(idHistoryV4, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// Then
			Patient patientAfterUpdate = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd, true);
			assertTrue(patientAfterUpdate.isDeleted());

			assertResourceVersionHasNotIncremented(id, versionBeforeUpdate, resourceVersionsSizeInit);
			Patient lPatient = myPatientDao.read(id.withVersion(versionBeforeDelete), mySrd, true);

			assertEquals(TEST_FAMILY_NAME, lPatient.getName().get(0).getFamily());
			assertEquals(TEST_GIVEN_NAME, lPatient.getName().get(0).getGiven().get(0).getValue());

			assertThat(lPatient.getIdElement().toString()).endsWith("/_history/3");

			assertEquals(lastUpdatedBeforeRewriteV4, myPatientDao.read(idHistoryV4, mySrd, true).getMeta().getLastUpdated());
		}
	}

	private void assertLastUpdatedWasModifiedOnVersion(IIdType theId, Date theLastUpdatedBeforeHistoryRewrite) {
		Patient patient = myPatientDao.read(theId, mySrd);
		assertTrue(patient.getMeta().getLastUpdated().after(theLastUpdatedBeforeHistoryRewrite));
		assertTrue(patient.getMeta().getLastUpdated().before(new Date()));
	}

	private void assertPatientNameWasModified(IIdType theId, String theExpectedVersion) {
		Patient lPatient = myPatientDao.read(theId, mySrd, false);
		assertEquals(FhirResourceDaoR4HistoryRewriteTest.TEST_FAMILY_NAME_MODIFIED, lPatient.getName().get(0).getFamily());
		assertEquals(FhirResourceDaoR4HistoryRewriteTest.TEST_GIVEN_NAME_MODIFIED, lPatient.getName().get(0).getGiven().get(0).getValue());
		assertThat(lPatient.getIdElement().toString()).endsWith("/_history/" + theExpectedVersion);
	}

	private void assertPatientBirthDateWasModified(IIdType theId, String theExpectedVersion) {
		Patient lPatient = myPatientDao.read(theId, mySrd);
		assertEquals(TEST_BIRTHDATE_MODIFIED, lPatient.getBirthDateElement().getValueAsString());
		assertThat(lPatient.getIdElement().toString()).endsWith("/_history/" + theExpectedVersion);
	}

	private void assertResourceVersionHasNotIncremented(IIdType theId, String theVersionBeforeUpdate, int theInitialNumOfResourceVersions) {
		String versionAfterUpdate = myPatientDao.read(theId.toUnqualifiedVersionless(), mySrd, true).getIdElement().getVersionIdPart();
		assertEquals(theVersionBeforeUpdate, versionAfterUpdate);

		int resourceVersionsSizeAfterUpdate = myResourceHistoryTableDao.findAll().size();
		assertEquals(theInitialNumOfResourceVersions, resourceVersionsSizeAfterUpdate);
	}

	private Parameters createPatchReplaceNamesParams() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.name.family"));
		op.addPart().setName("value").setValue(new StringType(FhirResourceDaoR4HistoryRewriteTest.TEST_FAMILY_NAME_MODIFIED));

		Parameters.ParametersParameterComponent op2 = patch.addParameter().setName("operation");
		op2.addPart().setName("type").setValue(new CodeType("replace"));
		op2.addPart().setName("path").setValue(new CodeType("Patient.name.given"));
		op2.addPart().setName("value").setValue(new StringType(FhirResourceDaoR4HistoryRewriteTest.TEST_GIVEN_NAME_MODIFIED));
		return patch;
	}

	private Parameters createPatchAddBirthDateParams() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("add"));
		op.addPart().setName("path").setValue(new CodeType("Patient"));
		op.addPart().setName("name").setValue(new CodeType("birthDate"));
		op.addPart().setName("value").setValue(new DateType(TEST_BIRTHDATE_MODIFIED));
		return patch;
	}

	@Nonnull
	private IIdType createPatientWithHistoryThreeVersions() {
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
