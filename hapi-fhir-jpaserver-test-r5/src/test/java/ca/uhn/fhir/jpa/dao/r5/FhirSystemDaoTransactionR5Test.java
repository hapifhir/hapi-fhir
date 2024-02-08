package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Quantity;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

public class FhirSystemDaoTransactionR5Test extends BaseJpaR5Test {

	@AfterEach
	public void after() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setIndexMissingFields(defaults.getIndexMissingFields());
		myStorageSettings.setMatchUrlCacheEnabled(defaults.isMatchUrlCacheEnabled());
		myStorageSettings.setDeleteEnabled(defaults.isDeleteEnabled());
		myStorageSettings.setInlineResourceTextBelowSize(defaults.getInlineResourceTextBelowSize());
		myStorageSettings.setAllowExternalReferences(defaults.isAllowExternalReferences());
	}


	/**
	 * If an inline match URL is the same as a conditional create in the same transaction, make sure we
	 * don't issue a select for it
	 */
	@ParameterizedTest(name = "{index}: {0}")
	@CsvSource({
		"Match URL Cache Enabled,  true",
		"Match URL Cache Disabled, false"
	})
	public void testInlineMatchUrlMatchesConditionalUpdate(@SuppressWarnings("unused") String theName, boolean theMatchUrlCacheEnabled) {
		// Setup
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setMatchUrlCacheEnabled(theMatchUrlCacheEnabled);

		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Observation observation1 = new Observation();
		observation1.addIdentifier().setSystem("http://observation").setValue("111");
		observation1.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation1).conditional("Observation?identifier=http://observation|111");

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://patient").setValue("123");
		bb.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://patient|123");

		Observation observation2 = new Observation();
		observation2.addIdentifier().setSystem("http://observation").setValue("222");
		observation2.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation2).conditional("Observation?identifier=http://observation|222");

		Observation observation3 = new Observation();
		observation3.addIdentifier().setSystem("http://observation").setValue("333");
		observation3.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation3).conditional("Observation?identifier=http://observation|333");

		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify

		// One select to resolve the 3 match URLs
		assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(1);
		String firstSelectQuery = myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false);
		assertThat(countMatches(firstSelectQuery, "rispt1_0.HASH_SYS_AND_VALUE in (?,?,?,?)")).as(firstSelectQuery).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(23);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(3);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

		assertThat(output.getEntry().size()).isEqualTo(4);

		IdType patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		IdType observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		Observation actualObs = myObservationDao.read(observationId, mySrd);
		assertThat(actualObs.getSubject().getReference()).isEqualTo(patientId.toUnqualifiedVersionless().getValue());

		myCaptureQueriesListener.logInsertQueries();
		runInTransaction(() -> {
			assertEquals(4, myResourceTableDao.count());
			assertEquals(4, myResourceHistoryTableDao.count());
			assertEquals(6, myResourceLinkDao.count());
			assertEquals(5, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

		/*
		 * Repeat, and make sure we reuse
		 */

		bb = new BundleBuilder(myFhirContext);

		observation1 = new Observation();
		observation1.addIdentifier().setSystem("http://observation").setValue("111");
		observation1.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation1).conditional("Observation?identifier=http://observation|111");

		patient = new Patient();
		patient.addIdentifier().setSystem("http://patient").setValue("123");
		bb.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://patient|123");

		observation2 = new Observation();
		observation2.addIdentifier().setSystem("http://observation").setValue("222");
		observation2.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation2).conditional("Observation?identifier=http://observation|222");

		observation3 = new Observation();
		observation3.addIdentifier().setSystem("http://observation").setValue("333");
		observation3.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation3).conditional("Observation?identifier=http://observation|333");

		input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);

		// Verify

		assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(theMatchUrlCacheEnabled ? 4 : 5);
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

		assertThat(output.getEntry().size()).isEqualTo(4);

		patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		actualObs = myObservationDao.read(observationId, mySrd);
		assertThat(actualObs.getSubject().getReference()).isEqualTo(patientId.toUnqualifiedVersionless().getValue());

		myCaptureQueriesListener.logInsertQueries();
		runInTransaction(() -> {
			assertEquals(4, myResourceTableDao.count());
			assertEquals(4, myResourceHistoryTableDao.count());
			assertEquals(6, myResourceLinkDao.count());
			assertEquals(5, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});


		/*
		 * Repeat once more
		 */

		bb = new BundleBuilder(myFhirContext);

		observation1 = new Observation();
		observation1.addIdentifier().setSystem("http://observation").setValue("111");
		observation1.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation1).conditional("Observation?identifier=http://observation|111");

		patient = new Patient();
		patient.addIdentifier().setSystem("http://patient").setValue("123");
		bb.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://patient|123");

		observation2 = new Observation();
		observation2.addIdentifier().setSystem("http://observation").setValue("222");
		observation2.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionUpdateEntry(observation2).conditional("Observation?identifier=http://observation|222");

		input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);

		// Verify

		assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(theMatchUrlCacheEnabled ? 4 : 5);
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

		assertThat(output.getEntry().size()).isEqualTo(3);

		patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		actualObs = myObservationDao.read(observationId, mySrd);
		assertThat(actualObs.getSubject().getReference()).isEqualTo(patientId.toUnqualifiedVersionless().getValue());

		myCaptureQueriesListener.logInsertQueries();
		runInTransaction(() -> {
			assertEquals(4, myResourceTableDao.count());
			assertEquals(4, myResourceHistoryTableDao.count());
			assertEquals(6, myResourceLinkDao.count());
			assertEquals(5, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

	}


	@ParameterizedTest(name = "{index}: {0}")
	@CsvSource({
		"Pre-existing with cache,    true  ,true",
		"Pre-existing without cache, true  ,false",
		"No match with cache,        false ,true",
		"No match without cache,     false ,false",
	})
	public void testRepeatedInlineMatchUrls(@SuppressWarnings("unused") String theName, boolean theTargetAlreadyExists, boolean theMatchUrlCacheEnabled) {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setMatchUrlCacheEnabled(theMatchUrlCacheEnabled);
		myStorageSettings.setDeleteEnabled(false);

		if (theTargetAlreadyExists) {
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://patient").setValue("123");
			myPatientDao.create(patient, mySrd);
		}

		BundleBuilder bb = new BundleBuilder(myFhirContext);

		for (int i = 0; i < 4; i++) {
			Observation observation1 = new Observation();
			observation1.addIdentifier().setSystem("http://observation").setValue(Integer.toString(i));
			observation1.setSubject(new Reference("Patient?identifier=http://patient|123"));
			bb.addTransactionCreateEntry(observation1);
		}
		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(theTargetAlreadyExists ? 20 : 24);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(4);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

		assertThat(output.getEntry().size()).isEqualTo(4);

		runInTransaction(() -> {
			assertEquals(5, myResourceTableDao.count());
			assertEquals(5, myResourceHistoryTableDao.count());
			assertEquals(8, myResourceLinkDao.count());
			assertEquals(6, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

		/*
		 * Second pass
		 */

		bb = new BundleBuilder(myFhirContext);

		for (int i = 0; i < 4; i++) {
			Observation observation1 = new Observation();
			observation1.addIdentifier().setSystem("http://observation").setValue(Integer.toString(i));
			observation1.setSubject(new Reference("Patient?identifier=http://patient|123"));
			bb.addTransactionCreateEntry(observation1);
		}
		input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(theMatchUrlCacheEnabled ? 0 : 1);
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(20);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(4);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

		assertThat(output.getEntry().size()).isEqualTo(4);

		runInTransaction(() -> {
			assertEquals(9, myResourceTableDao.count());
			assertEquals(9, myResourceHistoryTableDao.count());
			assertEquals(16, myResourceLinkDao.count());
			assertEquals(10, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

	}


	@ParameterizedTest(name = "{index}: {0}")
	@CsvSource({
		"NC Pre-existing with cache,    true  ,true,  false",
		"NC Pre-existing without cache, true  ,false, false",
		"NC No match with cache,        false ,true,  false",
		"NC No match without cache,     false ,false, false",
		"C Pre-existing with cache,    true  ,true,   true",
		"C Pre-existing without cache, true  ,false,  true",
		"C No match with cache,        false ,true,   true",
		"C No match without cache,     false ,false,  true",
	})
	public void testComplexConditionalUpdate(String theName, boolean theTargetAlreadyExists, boolean theMatchUrlCacheEnabled, boolean theResourceChanges) {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setMatchUrlCacheEnabled(theMatchUrlCacheEnabled);
		myStorageSettings.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("Patient/P");
		myPatientDao.update(patient, mySrd);

		if (theTargetAlreadyExists) {
			Observation observation1 = new Observation();
			observation1.setValue(new Quantity(5L));
			observation1.setSubject(new Reference("Patient/P"));
			myObservationDao.create(observation1, mySrd);
		}

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		Observation observation1 = new Observation();
		if (theResourceChanges) {
			observation1.addNote().setText(UUID.randomUUID().toString());
		}
		observation1.setValue(new Quantity(5L));
		observation1.setSubject(new Reference("Patient/P"));
		bb.addTransactionUpdateEntry(observation1).conditional("Observation?subject=Patient/P&value-quantity=5");
		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (theTargetAlreadyExists) {
			if (theResourceChanges) {
				assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(6);
				assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(1);
				assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(1);
			} else {
				assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(6);
				assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(0);
				assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
			}
		} else {
			if (theResourceChanges) {
				assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(2);
				assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(7);
				assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
			} else {
				assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(2);
				assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(7);
				assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
			}
		}
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

		assertThat(output.getEntry().size()).isEqualTo(1);

		runInTransaction(() -> {
			assertEquals(2, myResourceTableDao.count());
			assertEquals((theTargetAlreadyExists && theResourceChanges) ? 3 : 2, myResourceHistoryTableDao.count());
			assertEquals(2, myResourceLinkDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

	}


	/**
	 * A FHIR transaction bundle containing a conditional create as well as a patch that point to the same resource
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testConditionalCreateAndConditionalPatchOnSameResource(boolean thePreviouslyExisting) {

		if (thePreviouslyExisting) {
			Patient patient = new Patient();
			patient.setActive(false);
			patient.addIdentifier().setSystem("http://system").setValue("value");
			myPatientDao.create(patient, mySrd);
		}

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		Patient patient = new Patient();
		patient.setActive(false);
		patient.addIdentifier().setSystem("http://system").setValue("value");
		bb.addTransactionCreateEntry(patient).conditional("Patient?identifier=http://system|value");

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(true));
		bb.addTransactionFhirPatchEntry(patch).conditional("Patient?identifier=http://system|value");

		Bundle input = bb.getBundleTyped();
		ourLog.info("Bundle: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Test
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		IdType createId = new IdType(output.getEntry().get(0).getResponse().getLocation());
		IdType patchId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		assertThat(createId.getVersionIdPart()).isEqualTo("1");
		assertThat(patchId.getVersionIdPart()).isEqualTo("2");
		assertThat(patchId.getIdPart()).isEqualTo(createId.getIdPart());

		Patient createdPatient = myPatientDao.read(patchId, mySrd);
		assertThat(createdPatient.getIdentifierFirstRep().getSystem()).isEqualTo("http://system");
		assertThat(createdPatient.getActive()).isTrue();

		assertThat(output.getEntry().size()).isEqualTo(2);
	}


	/**
	 * If a conditional delete and conditional update are both used on the same condition,
	 * the update should win.
	 * We need to test this scenario with both empty and non-empty RequestDetails.requestId parameter,
	 * as providing RequestDetails.requestId previously caused javax.persistence.EntityExistsException
	 * during persistence of ResourceHistoryProvenanceEntity.
	 *
	 * @param theReturnRequestId if RequestDetails.requestId should return non-null value
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void createBundle_withConditionalDeleteAndConditionalUpdateOnSameResource_updatesResource(boolean theReturnRequestId) {
		Bundle outcome;
		Patient actual;

		if (theReturnRequestId) {
			when(mySrd.getRequestId()).thenReturn("requestId");
		}

		// First pass (resource doesn't already exist)

		outcome = mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalUpdateOnSameResource(myFhirContext));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).endsWith("_history/1");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		IdType resourceId = new IdType(outcome.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();
		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");

		// Second pass (resource already exists)

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalUpdateOnSameResource(myFhirContext));
		myCaptureQueriesListener.logUpdateQueries();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).endsWith("_history/2");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		logAllResources();
		logAllResourceVersions();

		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("2");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");

		// Third pass (resource already exists)

		outcome = mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalUpdateOnSameResource(myFhirContext));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).endsWith("_history/3");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("3");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");
	}


	@Test
	public void testExternalReference() throws IOException {
		myStorageSettings.setAllowExternalReferences(true);

		Bundle input = loadResourceFromClasspath(Bundle.class, "docref-test-bundle.json");
		Bundle output = mySystemDao.transaction(mySrd, input);
		assertThat(output.getEntry().size()).isEqualTo(1);
	}



	@Test
	public void testConditionalDeleteAndConditionalUpdateOnSameResource_MultipleMatchesAlreadyExist() {

		// Setup

		myPatientDao.create(createPatientWithIdentifierAndTag(), mySrd);
		myPatientDao.create(createPatientWithIdentifierAndTag(), mySrd);

		// Test

		try {
			mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalUpdateOnSameResource(myFhirContext));
			fail("");
		} catch (PreconditionFailedException e) {

			// Verify
			assertThat(e.getMessage()).contains("Multiple resources match this search");

		}
	}

	/**
	 * If a conditional delete and conditional update are both used on the same condition,
	 * the update should win.
	 */
	@Test
	public void testConditionalDeleteAndConditionalCreateOnSameResource() {
		Bundle outcome;
		Patient actual;

		// First pass (resource doesn't already exist)

		outcome = mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalCreateOnSameResource(myFhirContext));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).endsWith("_history/1");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		IdType resourceId = new IdType(outcome.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();
		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");

		// Second pass (resource already exists)

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalCreateOnSameResource(myFhirContext));
		myCaptureQueriesListener.logUpdateQueries();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).endsWith("_history/1");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		logAllResources();
		logAllResourceVersions();

		IdType resourceId2 = new IdType(outcome.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();
		assertThat(resourceId2.getIdPart()).isNotEqualTo(resourceId.getIdPart());
		actual = myPatientDao.read(resourceId2, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");

		// Third pass (resource already exists)

		outcome = mySystemDao.transaction(mySrd, createBundleWithConditionalDeleteAndConditionalCreateOnSameResource(myFhirContext));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).endsWith("_history/1");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		IdType resourceId3 = new IdType(outcome.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();
		assertThat(resourceId3.getIdPart()).isNotEqualTo(resourceId2.getIdPart());
		actual = myPatientDao.read(resourceId3, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");
	}

	/**
	 * There's not much point to deleting and updating the same resource in a
	 * transaction, but let's make sure we at least don't end up in a bad state
	 */
	@Test
	public void testDeleteAndUpdateOnSameResource() {

		Bundle outcome;
		Patient actual;

		// First pass (resource doesn't already exist)

		outcome = mySystemDao.transaction(mySrd, createBundleWithDeleteAndUpdateOnSameResource(myFhirContext));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo(null);
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).isEqualTo("Patient/P/_history/1");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		IdType resourceId = new IdType(outcome.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();
		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");

		// Second pass (resource already exists)

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, createBundleWithDeleteAndUpdateOnSameResource(myFhirContext));
		myCaptureQueriesListener.logUpdateQueries();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo("Patient/P/_history/2");
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).isEqualTo("Patient/P/_history/2");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		logAllResources();
		logAllResourceVersions();

		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("2");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");

		// Third pass (resource already exists)

		outcome = mySystemDao.transaction(mySrd, createBundleWithDeleteAndUpdateOnSameResource(myFhirContext));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getEntry().get(0).getResponse().getLocation()).isEqualTo("Patient/P/_history/3");
		assertThat(outcome.getEntry().get(0).getResponse().getStatus()).isEqualTo("204 No Content");
		assertThat(outcome.getEntry().get(1).getResponse().getLocation()).isEqualTo("Patient/P/_history/3");
		assertThat(outcome.getEntry().get(1).getResponse().getStatus()).isEqualTo("201 Created");

		actual = myPatientDao.read(resourceId, mySrd);
		assertThat(actual.getIdElement().getVersionIdPart()).isEqualTo("3");
		assertThat(actual.getIdentifierFirstRep().getSystem()).isEqualTo("http://foo");
		assertThat(actual.getMeta().getTagFirstRep().getSystem()).isEqualTo("http://tag");


	}


	@Nonnull
	private static Bundle createBundleWithConditionalDeleteAndConditionalUpdateOnSameResource(FhirContext theFhirContext) {
		// Build a new bundle each time we need it
		BundleBuilder bb = new BundleBuilder(theFhirContext);
		bb.addTransactionDeleteConditionalEntry("Patient?identifier=http://foo|bar");

		Patient patient = createPatientWithIdentifierAndTag();
		bb.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://foo|bar");
		return bb.getBundleTyped();
	}

	@Nonnull
	private static Bundle createBundleWithConditionalDeleteAndConditionalCreateOnSameResource(FhirContext theFhirContext) {
		// Build a new bundle each time we need it
		BundleBuilder bb = new BundleBuilder(theFhirContext);
		bb.addTransactionDeleteConditionalEntry("Patient?identifier=http://foo|bar");

		Patient patient = createPatientWithIdentifierAndTag();
		patient.setId((String)null);
		bb.addTransactionCreateEntry(patient).conditional("Patient?identifier=http://foo|bar");
		return bb.getBundleTyped();
	}

	@Nonnull
	private static Bundle createBundleWithDeleteAndUpdateOnSameResource(FhirContext theFhirContext) {
		// Build a new bundle each time we need it
		BundleBuilder bb = new BundleBuilder(theFhirContext);
		bb.addTransactionDeleteEntry(new IdType("Patient/P"));
		bb.addTransactionUpdateEntry(createPatientWithIdentifierAndTag());
		return bb.getBundleTyped();
	}

	@Nonnull
	private static Patient createPatientWithIdentifierAndTag() {
		Patient patient = new Patient();
		patient.setId("Patient/P");
		patient.getMeta().addTag("http://tag", "tag-code", "tag-display");
		patient.addIdentifier().setSystem("http://foo").setValue("bar");
		return patient;
	}


}


