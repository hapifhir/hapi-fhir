package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.util.BundleBuilder;
import org.hamcrest.Matchers;
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

import javax.persistence.Id;
import java.util.UUID;
import java.util.concurrent.Callable;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirSystemDaoTransactionR5Test extends BaseJpaR5Test {

	@AfterEach
	public void after() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setIndexMissingFields(defaults.getIndexMissingFields());
		myStorageSettings.setMatchUrlCacheEnabled(defaults.isMatchUrlCacheEnabled());
		myStorageSettings.setDeleteEnabled(defaults.isDeleteEnabled());
		myStorageSettings.setInlineResourceTextBelowSize(defaults.getInlineResourceTextBelowSize());
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
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		String firstSelectQuery = myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false);
		assertEquals(1, countMatches(firstSelectQuery, "HASH_SYS_AND_VALUE in (? , ? , ? , ?)"), firstSelectQuery);
		assertEquals(23, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(4, output.getEntry().size());

		IdType patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		IdType observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		Observation actualObs = myObservationDao.read(observationId, mySrd);
		assertEquals(patientId.toUnqualifiedVersionless().getValue(), actualObs.getSubject().getReference());

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

		assertEquals(theMatchUrlCacheEnabled ? 4 : 5, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(4, output.getEntry().size());

		patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		actualObs = myObservationDao.read(observationId, mySrd);
		assertEquals(patientId.toUnqualifiedVersionless().getValue(), actualObs.getSubject().getReference());

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

		assertEquals(theMatchUrlCacheEnabled ? 4 : 5, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(3, output.getEntry().size());

		patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		actualObs = myObservationDao.read(observationId, mySrd);
		assertEquals(patientId.toUnqualifiedVersionless().getValue(), actualObs.getSubject().getReference());

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
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(theTargetAlreadyExists ? 20 : 24, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(4, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(4, output.getEntry().size());

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
		assertEquals(theMatchUrlCacheEnabled ? 0 : 1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(20, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(4, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(4, output.getEntry().size());

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
				assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
				assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
				assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
			} else {
				assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
				assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
				assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
			}
		} else {
			if (theResourceChanges) {
				assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
				assertEquals(7, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
				assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
			} else {
				assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
				assertEquals(7, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
				assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
			}
		}
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(1, output.getEntry().size());

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
		assertEquals("1", createId.getVersionIdPart());
		assertEquals("2", patchId.getVersionIdPart());
		assertEquals(createId.getIdPart(), patchId.getIdPart());

		Patient createdPatient = myPatientDao.read(patchId, mySrd);
		assertEquals("http://system", createdPatient.getIdentifierFirstRep().getSystem());
		assertTrue(createdPatient.getActive());

		assertEquals(2, output.getEntry().size());
	}


	@Test
	public void testTransactionWithConditionalDeleteAndConditionalUpdateOnSameCondition() throws Exception {
		myStorageSettings.setInlineResourceTextBelowSize(10000);

		Callable<Bundle> inputSupplier = ()->{
			// Build a new bundle each time we need it
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			bb.addTransactionDeleteConditionalEntry("Patient?identifier=http://foo|bar");

			Patient patient = new Patient();
			patient.setId("Patient/P");
			patient.getMeta().addTag("http://tag", "tag-code", "tag-display");
			patient.addIdentifier().setSystem("http://foo").setValue("bar");
			bb.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://foo|bar");
			return bb.getBundleTyped();
		};
		Bundle outcome;
		Patient actual;

		// First pass (resource doesn't already exist)

		outcome = mySystemDao.transaction(mySrd, inputSupplier.call());
		assertEquals(null, outcome.getEntry().get(0).getResponse().getLocation());
		assertEquals("204 No Content", outcome.getEntry().get(0).getResponse().getStatus());
		assertThat(outcome.getEntry().get(1).getResponse().getLocation(), endsWith("_history/1"));
		assertEquals("201 Created", outcome.getEntry().get(1).getResponse().getStatus());

		IdType resourceId = new IdType(outcome.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();
		actual = myPatientDao.read(resourceId, mySrd);
		assertEquals("1", actual.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actual.getIdentifierFirstRep().getSystem());
		assertEquals("http://tag", actual.getMeta().getTagFirstRep().getSystem());

		// Second pass (resource already exists)

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, inputSupplier.call());
		myCaptureQueriesListener.logUpdateQueries();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals(null, outcome.getEntry().get(0).getResponse().getLocation());
		assertEquals("204 No Content", outcome.getEntry().get(0).getResponse().getStatus());
		assertThat(outcome.getEntry().get(1).getResponse().getLocation(), endsWith("_history/3"));
		assertEquals("201 Created", outcome.getEntry().get(1).getResponse().getStatus());

		actual = myPatientDao.read(resourceId, mySrd);
		assertEquals("3", actual.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actual.getIdentifierFirstRep().getSystem());
		assertEquals("http://tag", actual.getMeta().getTagFirstRep().getSystem());

		// Third pass (resource already exists)

		outcome = mySystemDao.transaction(mySrd, inputSupplier.call());
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals(null, outcome.getEntry().get(0).getResponse().getLocation());
		assertEquals("204 No Content", outcome.getEntry().get(0).getResponse().getStatus());
		assertThat(outcome.getEntry().get(1).getResponse().getLocation(), endsWith("_history/5"));
		assertEquals("201 Created", outcome.getEntry().get(1).getResponse().getStatus());

		actual = myPatientDao.read(resourceId, mySrd);
		assertEquals("5", actual.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actual.getIdentifierFirstRep().getSystem());
		assertEquals("http://tag", actual.getMeta().getTagFirstRep().getSystem());
	}



}


