package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirSystemDaoTransactionR5Test extends BaseJpaR5Test {

	@AfterEach
	public void after() {
	}


	/**
	 * If an inline match URL is the same as a conditional create in the same transaction, make sure we
	 * don't issue a select for it
	 */
	@ParameterizedTest
	@CsvSource({
		"true",
		"false"
	})
	public void testInlineMatchUrlMatchesConditionalUpdate(boolean theMatchUrlCacheEnabled) {
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

		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify

		// One select to resolve the 3 match URLs
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, countMatches(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), "HASH_SYS_AND_VALUE="));
		assertEquals(17, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(3, output.getEntry().size());

		IdType patientId = new IdType(output.getEntry().get(1).getResponse().getLocation());
		IdType observationId = new IdType(output.getEntry().get(2).getResponse().getLocation());
		Observation actualObs = myObservationDao.read(observationId, mySrd);
		assertEquals(patientId.toUnqualifiedVersionless().getValue(), actualObs.getSubject().getReference());

		myCaptureQueriesListener.logInsertQueries();
		runInTransaction(() -> {
			assertEquals(3, myResourceTableDao.count());
			assertEquals(3, myResourceHistoryTableDao.count());
			assertEquals(4, myResourceLinkDao.count());
			assertEquals(4, myResourceIndexedSearchParamTokenDao.count());
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
			assertEquals(3, myResourceTableDao.count());
			assertEquals(3, myResourceHistoryTableDao.count());
			assertEquals(4, myResourceLinkDao.count());
			assertEquals(4, myResourceIndexedSearchParamTokenDao.count());
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
			assertEquals(3, myResourceTableDao.count());
			assertEquals(3, myResourceHistoryTableDao.count());
			assertEquals(4, myResourceLinkDao.count());
			assertEquals(4, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

	}


	@ParameterizedTest
	@CsvSource({
		"true  ,true",
		"false ,true",
		"true  ,false",
		"false ,false",
	})
	public void testRepeatedInlineMatchUrls(boolean theTargetAlreadyExists, boolean theMatchUrlCacheEnabled) {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setMatchUrlCacheEnabled(theMatchUrlCacheEnabled);

		if (theTargetAlreadyExists) {
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://patient").setValue("123");
			myPatientDao.create(patient, mySrd);
		}

		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Observation observation1 = new Observation();
		observation1.addIdentifier().setSystem("http://observation").setValue("111");
		observation1.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionCreateEntry(observation1);

		Observation observation2 = new Observation();
		observation2.addIdentifier().setSystem("http://observation").setValue("222");
		observation2.setSubject(new Reference("Patient?identifier=http://patient|123"));
		bb.addTransactionCreateEntry(observation2);

		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(17, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertEquals(3, output.getEntry().size());

		runInTransaction(() -> {
			assertEquals(3, myResourceTableDao.count());
			assertEquals(3, myResourceHistoryTableDao.count());
			assertEquals(4, myResourceLinkDao.count());
			assertEquals(4, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
		});

	}

}


