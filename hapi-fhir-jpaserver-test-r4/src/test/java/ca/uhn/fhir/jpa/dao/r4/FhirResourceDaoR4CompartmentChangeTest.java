package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-6
class FhirResourceDaoR4CompartmentChangeTest extends BaseJpaR4Test {

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	@Test
	void testCompartmentChange_allPatientsReturnedWhenCompartmentResourcesCreatedAfterDate() throws InterruptedException {
		Date beforeCreation = new Date();
		Thread.sleep(2);

		String patient1Id = createPatientWithObservation();
		String patient2Id = createPatientWithObservation();

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patient1Id, patient2Id);
	}

	@Test
	void testCompartmentChange_onlyChangedPatientsReturnedAfterSecondDate() throws InterruptedException {
		createPatientWithObservation();
		String patient2Id = createPatientWithObservation();

		// Record time after initial creation
		Thread.sleep(2);
		Date afterInitialCreation = new Date();

		// Create new observation only for patient2
		Observation obs2 = new Observation();
		obs2.setSubject(new Reference(patient2Id));
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obs2, mySrd);

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(afterInitialCreation));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patient2Id);
	}

	@Test
	void testCompartmentChange_patientDirectlyUpdatedIsReturned() throws InterruptedException {
		Patient patient1 = new Patient();
		patient1.setActive(true);
		String patient1Id = myPatientDao.create(patient1, mySrd).getId().toUnqualifiedVersionless().getValue();

		Patient patient2 = new Patient();
		patient2.setActive(true);
		myPatientDao.create(patient2, mySrd);

		Thread.sleep(2);
		Date afterInitialCreation = new Date();

		// Directly update patient1
		patient1.setActive(false);
		patient1.setId(patient1Id);
		myPatientDao.update(patient1, mySrd);

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(afterInitialCreation));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patient1Id);
	}

	@Test
	void testCompartmentChange_nonPatientResourceTypeThrowsException() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN, "2024-01-01")));

		assertThatThrownBy(() -> myObservationDao.search(map, mySrd))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(Constants.PARAM_COMPARTMENT_LAST_UPDATED)
				.hasMessageContaining("only supported for Patient");
	}

	@Test
	void testCompartmentChange_dateRangeWithUpperBound() throws InterruptedException {
		createPatientWithObservation();

		Thread.sleep(2);
		Date rangeStart = new Date();

		String patientDuringId = createPatientWithObservation();

		Thread.sleep(2);
		Date rangeEnd = new Date();
		Thread.sleep(2);

		createPatientWithObservation();

		DateRangeParam range = new DateRangeParam();
		range.setLowerBound(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, rangeStart));
		range.setUpperBound(new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, rangeEnd));

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(range);
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patientDuringId);
	}

	@Test
	void testCompartmentChange_notEqualsPrefix_excludesCompartmentResourceUpdatedAtExactDate() throws InterruptedException {
		// Create patient1 with observation at time T1
		String patient1Id = createPatientWithObservation();

		Thread.sleep(2);

		// Create patient2 (no observation) — its sole RES_UPDATED becomes the excluded date.
		// Without compartment resources, the EXISTS subquery returns no rows, so exclusion
		// depends only on the outer ne condition matching patient2's exact RES_UPDATED.
		Patient patient2 = new Patient();
		patient2.setActive(true);
		String patient2Id = myPatientDao.create(patient2, mySrd).getId().toUnqualifiedVersionless().getValue();
		Date excludedDate = myPatientDao.read(new IdType(patient2Id), mySrd).getMeta().getLastUpdated();

		Thread.sleep(2);

		// Create patient3 with observation at time T3
		String patient3Id = createPatientWithObservation();

		// Search: ne (not-equals) excludedDate — should return patient1 and patient3 but NOT patient2
		// ne produces: (RES_UPDATED < excludedDate OR RES_UPDATED > excludedDate)
		DateRangeParam neRange = new DateRangeParam();
		neRange.setLowerBound(new DateParam(ParamPrefixEnum.NOT_EQUAL, excludedDate));
		neRange.setUpperBound(new DateParam(ParamPrefixEnum.NOT_EQUAL, excludedDate));

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(neRange);
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		// Patient1 (before) and patient3 (after) match via ne; patient2 excluded at exact timestamp
		assertThat(ids).containsExactlyInAnyOrder(patient1Id, patient3Id);
	}

	@Test
	void testCompartmentChange_patientWithNoChangesAfterDateNotReturned() throws InterruptedException {
		Patient patient1 = new Patient();
		patient1.setActive(true);
		myPatientDao.create(patient1, mySrd);

		Thread.sleep(2);
		Date afterCreation = new Date();

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(afterCreation));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).isEmpty();
	}

	@Test
	// This test is verifying an unfortunate limitation. If you fix this limitation,
	// make sure to update the documentation.
	void testCompartmentChange_deletedCompartmentResourceIsNotDetected() throws InterruptedException {
		// Deleting a compartment resource (e.g. Observation) removes its HFJ_RES_LINK rows,
		// so the link-based subquery can no longer associate it with the Patient.
		Patient patient1 = new Patient();
		patient1.setActive(true);
		String patient1Id = myPatientDao.create(patient1, mySrd).getId().toUnqualifiedVersionless().getValue();

		Observation obs1 = new Observation();
		obs1.setSubject(new Reference(patient1Id));
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		var obs1Id = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Thread.sleep(2);
		Date afterCreation = new Date();

		// Delete the observation — this removes the HFJ_RES_LINK row
		myObservationDao.delete(obs1Id, mySrd);

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(afterCreation));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		// The deletion is NOT detected because link rows are cleaned up on delete
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).doesNotContain(patient1Id);
	}

	private String createPatientWithObservation() {
		Patient patient = new Patient();
		patient.setActive(true);
		String patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

		Observation obs = new Observation();
		obs.setSubject(new Reference(patientId));
		obs.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd);

		return patientId;
	}

	@Test
	void testCompartmentLastUpdated_combinedWithMultipleParameters() throws InterruptedException {
		Date beforeCreation = new Date();
		Thread.sleep(2);

		// Create male patient born 1990 with observation
		Patient malePatient1990 = new Patient();
		malePatient1990.setGender(Enumerations.AdministrativeGender.MALE);
		malePatient1990.setBirthDateElement(new org.hl7.fhir.r4.model.DateType("1990-01-01"));
		String malePatient1990Id = myPatientDao.create(malePatient1990, mySrd).getId().toUnqualifiedVersionless().getValue();
		Observation obs1 = new Observation();
		obs1.setSubject(new Reference(malePatient1990Id));
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obs1, mySrd);

		// Create male patient born 2000 with observation
		Patient malePatient2000 = new Patient();
		malePatient2000.setGender(Enumerations.AdministrativeGender.MALE);
		malePatient2000.setBirthDateElement(new org.hl7.fhir.r4.model.DateType("2000-01-01"));
		String malePatient2000Id = myPatientDao.create(malePatient2000, mySrd).getId().toUnqualifiedVersionless().getValue();
		Observation obs2 = new Observation();
		obs2.setSubject(new Reference(malePatient2000Id));
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obs2, mySrd);

		// Create female patient born 1990 with observation
		Patient femalePatient = new Patient();
		femalePatient.setGender(Enumerations.AdministrativeGender.FEMALE);
		femalePatient.setBirthDateElement(new org.hl7.fhir.r4.model.DateType("1990-01-01"));
		myPatientDao.create(femalePatient, mySrd);

		// Search: _compartmentLastUpdated + gender=male + birthdate=1990
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
		map.add(Patient.SP_GENDER, new TokenParam("male"));
		map.add(Patient.SP_BIRTHDATE, new DateParam(ParamPrefixEnum.EQUAL, "1990-01-01"));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(malePatient1990Id);
	}

	@Test
	void testCompartmentLastUpdated_combinedWithSort() throws InterruptedException {
		Date beforeCreation = new Date();
		Thread.sleep(2);

		// Create patients with different family names
		Patient patientA = new Patient();
		patientA.addName().setFamily("Adams");
		String patientAId = myPatientDao.create(patientA, mySrd).getId().toUnqualifiedVersionless().getValue();
		Observation obsA = new Observation();
		obsA.setSubject(new Reference(patientAId));
		obsA.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obsA, mySrd);

		Patient patientZ = new Patient();
		patientZ.addName().setFamily("Zimmerman");
		String patientZId = myPatientDao.create(patientZ, mySrd).getId().toUnqualifiedVersionless().getValue();
		Observation obsZ = new Observation();
		obsZ.setSubject(new Reference(patientZId));
		obsZ.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obsZ, mySrd);

		// Search with _compartmentLastUpdated + _sort=family (ascending)
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
		map.setSort(new SortSpec(Patient.SP_FAMILY, SortOrderEnum.ASC));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(patientAId, patientZId);
	}

	@Test
	void testCompartmentLastUpdated_rawParamWithGePrefix_returnsOnlyLaterPatients() throws InterruptedException {
		// Create patient before the cutoff date
		createPatientWithObservation();

		Thread.sleep(2);
		Date cutoff = new Date();

		// Create patient after the cutoff date
		String patient2Id = createPatientWithObservation();

		// Add _compartmentLastUpdated as a raw parameter (via map.add) rather than setCompartmentLastUpdated.
		// This exercises the raw parameter extraction path in SearchBuilder.searchForIdsWithAndOr.
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_COMPARTMENT_LAST_UPDATED,
				new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, cutoff));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patient2Id);
	}

	@Test
	void testCompartmentLastUpdated_rawParamWithGePlusLeRange_returnsPatientsInRange() throws InterruptedException {
		// Create patient before the range
		createPatientWithObservation();

		Thread.sleep(2);
		Date rangeStart = new Date();

		// Create patient inside the range
		String patientInRangeId = createPatientWithObservation();

		Thread.sleep(2);
		Date rangeEnd = new Date();
		Thread.sleep(2);

		// Create patient after the range
		createPatientWithObservation();

		// Add both ge and le as raw parameters — two separate map.add calls produce two AND groups.
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_COMPARTMENT_LAST_UPDATED,
				new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, rangeStart));
		map.add(Constants.PARAM_COMPARTMENT_LAST_UPDATED,
				new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, rangeEnd));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patientInRangeId);
	}

	@Test
	void testCompartmentLastUpdated_rawParamWithLePrefix_returnsOnlyEarlierPatients() throws InterruptedException {
		// Create patient before the cutoff date
		String patient1Id = createPatientWithObservation();

		Thread.sleep(2);
		Date cutoff = new Date();
		Thread.sleep(2);

		// Create patient after the cutoff date
		createPatientWithObservation();

		// Add _compartmentLastUpdated=le[cutoff] as a raw parameter
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_COMPARTMENT_LAST_UPDATED,
				new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, cutoff));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patient1Id);
	}

	@Test
	void testCompartmentLastUpdated_gtPrefix_excludesBoundaryDate() {
		// Create patient with observation and capture the observation's exact timestamp
		Patient patient = new Patient();
		patient.setActive(true);
		String patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

		Observation obs = new Observation();
		obs.setSubject(new Reference(patientId));
		obs.setStatus(Observation.ObservationStatus.FINAL);
		var obsOutcome = myObservationDao.create(obs, mySrd);
		Date obsTimestamp = obsOutcome.getResource().getMeta().getLastUpdated();

		// Search with gt (strict greater-than) at the exact observation timestamp
		// The observation was created AT obsTimestamp, so gt should exclude it
		DateRangeParam range = new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN, obsTimestamp));
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(range);
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).doesNotContain(patientId);
	}

	@Test
	void testCompartmentLastUpdated_ltPrefix_excludesBoundaryDate() {
		// Create patient and capture its exact timestamp
		Patient patient = new Patient();
		patient.setActive(true);
		var patientOutcome = myPatientDao.create(patient, mySrd);
		String patientId = patientOutcome.getId().toUnqualifiedVersionless().getValue();
		Date patientTimestamp = patientOutcome.getResource().getMeta().getLastUpdated();

		// Add a compartment resource (observation) — created at or after patientTimestamp
		Observation obs = new Observation();
		obs.setSubject(new Reference(patientId));
		obs.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd);

		// Search with lt (strict less-than) at the patient's exact timestamp.
		// Patient was created AT patientTimestamp, so lt should exclude it.
		// Observation was created at or after patientTimestamp, so lt also excludes
		// it from the EXISTS subquery.
		DateRangeParam range = new DateRangeParam(new DateParam(ParamPrefixEnum.LESSTHAN, patientTimestamp));
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentLastUpdated(range);
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).doesNotContain(patientId);
	}

	private DateRangeParam compartmentChangeGt(Date theDate) {
		return new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, theDate));
	}

	@Nested
	// Created by claude-opus-4-6
	class PartitionedCompartmentLastUpdatedTest {

		private PartitionInterceptor myPartitionInterceptor;

		@BeforeEach
		void setUp() {
			myPartitionSettings.setPartitioningEnabled(true);
			myPartitionInterceptor = new PartitionInterceptor();
			mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);
		}

		@AfterEach
		void tearDown() {
			myPartitionSettings.setPartitioningEnabled(false);
			myPartitionSettings.setUnnamedPartitionMode(false);
			myPartitionSettings.setDefaultPartitionId(null);
			myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.NOT_ALLOWED);
			mySrdInterceptorService.unregisterInterceptor(myPartitionInterceptor);
		}

		@Test
		void testCompartmentLastUpdated_withNamedPartitions() throws InterruptedException {
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);

			verifyCompartmentLastUpdatedReturnsPatientsFromBothPartitions();
		}

		@Test
		void testCompartmentLastUpdated_withUnnamedPartitionMode() throws InterruptedException {
			myPartitionSettings.setUnnamedPartitionMode(true);

			verifyCompartmentLastUpdatedReturnsPatientsFromBothPartitions();
		}

		private void verifyCompartmentLastUpdatedReturnsPatientsFromBothPartitions() throws InterruptedException {
			Date beforeCreation = new Date();
			Thread.sleep(2);

			// Create Patient+Observation in partition 1
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(1));
			String patient1Id = createPatientWithObservation();

			// Create Patient+Observation in partition 2
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(2));
			String patient2Id = createPatientWithObservation();

			// Search with allPartitions read
			myPartitionInterceptor.setReadPartition(RequestPartitionId.allPartitions());
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patient1Id, patient2Id);
		}

		@Test
		void testCompartmentLastUpdated_singlePartitionRead_doesNotReturnCrossPartitionData() throws InterruptedException {
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);
			myPartitionSettings.setAllowReferencesAcrossPartitions(
					PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

			Date beforeCreation = new Date();
			Thread.sleep(2);

			// Create Patient+Observation in partition 1
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(1));
			String patient1Id = createPatientWithObservation();

			// Create Patient+Observation in partition 2
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(2));
			createPatientWithObservation();

			// Read ONLY partition 1 — must NOT see partition 2 data
			myPartitionInterceptor.setReadPartition(RequestPartitionId.fromPartitionId(1));
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patient1Id);
		}

		@Test
		void testCompartmentLastUpdated_compartmentResourceOnDifferentPartition_notVisibleFromPatientPartition() throws InterruptedException {
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);
			myPartitionSettings.setAllowReferencesAcrossPartitions(
					PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

			// Create Patient in partition 1 BEFORE the cutoff so the direct-update
			// condition does not match — only the EXISTS subquery can surface it.
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(1));
			Patient patient = new Patient();
			patient.setActive(true);
			String patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

			Thread.sleep(2);
			Date afterPatientCreation = new Date();

			// Create Observation referencing that Patient, but stored in partition 2
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(2));
			Observation obs = new Observation();
			obs.setSubject(new Reference(patientId));
			obs.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(obs, mySrd);

			// Read partition 1 only — the subquery restricts HFJ_RESOURCE to partition 1,
			// so the Observation in partition 2 is not found by the EXISTS subquery.
			// The Patient was created before the cutoff, so the direct-update condition
			// also excludes it. Net result: Patient is NOT returned.
			myPartitionInterceptor.setReadPartition(RequestPartitionId.fromPartitionId(1));
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(afterPatientCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).doesNotContain(patientId);
		}

		@Test
		void testCompartmentLastUpdated_compartmentResourceOnDifferentPartition_visibleWithAllPartitionsRead() throws InterruptedException {
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);
			myPartitionSettings.setAllowReferencesAcrossPartitions(
					PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

			// Create Patient in partition 1
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(1));
			Patient patient = new Patient();
			patient.setActive(true);
			String patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

			Thread.sleep(2);
			Date afterPatientCreation = new Date();

			// Create Observation referencing that Patient, but stored in partition 2
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(2));
			Observation obs = new Observation();
			obs.setSubject(new Reference(patientId));
			obs.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(obs, mySrd);

			// Read all partitions — the subquery has no partition restriction,
			// so the cross-partition Observation IS found and the Patient is returned.
			myPartitionInterceptor.setReadPartition(RequestPartitionId.allPartitions());
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(afterPatientCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patientId);
		}

		@Test
		void testCompartmentLastUpdated_nonCompartmentResourceOnDifferentPartition_doesNotAffectResults() throws InterruptedException {
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);
			myPartitionSettings.setAllowReferencesAcrossPartitions(
					PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

			Date beforeCreation = new Date();
			Thread.sleep(2);

			// Create Patient + Observation in partition 1
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(1));
			String patientId = createPatientWithObservation();

			// Create Practitioner in partition 2 — a non-compartment resource on a different partition.
			// The compartment last updated query should be entirely unaffected by non-compartment resources
			// regardless of their partition.
			myPartitionInterceptor.setCreatePartition(RequestPartitionId.fromPartitionId(2));
			Practitioner practitioner = new Practitioner();
			practitioner.addName().setFamily("Smith");
			myPractitionerDao.create(practitioner, mySrd);

			// Read partition 1 — Practitioner on partition 2 is irrelevant to compartment change
			myPartitionInterceptor.setReadPartition(RequestPartitionId.fromPartitionId(1));
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patientId);
		}

		@Interceptor
		@SuppressWarnings("unused")
		static class PartitionInterceptor {
			private RequestPartitionId myCreatePartition = RequestPartitionId.fromPartitionId(null);
			private RequestPartitionId myReadPartition = RequestPartitionId.allPartitions();

			void setCreatePartition(RequestPartitionId thePartitionId) {
				myCreatePartition = thePartitionId;
			}

			void setReadPartition(RequestPartitionId thePartitionId) {
				myReadPartition = thePartitionId;
			}

			@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
			public RequestPartitionId identifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
				return myCreatePartition;
			}

			@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
			public RequestPartitionId identifyRead(ServletRequestDetails theRequestDetails) {
				return myReadPartition;
			}
		}
	}

	@Nested
	// Created by claude-opus-4-6
	class PatientIdModeCompartmentLastUpdatedTest {

		private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;

		@BeforeEach
		void setUp() {
			myPartitionSettings.setPartitioningEnabled(true);
			myPartitionSettings.setUnnamedPartitionMode(true);
			myPartitionSettings.setDefaultPartitionId(-1);
			// PATIENT_ID mode: partition derived from patient ID via default algorithm
			myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(
					getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
			myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);
		}

		@AfterEach
		void tearDown() {
			myPartitionSettings.setPartitioningEnabled(false);
			myPartitionSettings.setUnnamedPartitionMode(false);
			myPartitionSettings.setDefaultPartitionId(null);
			myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		}

		@Test
		void testCompartmentLastUpdated_withPatientIdMode() throws InterruptedException {
			Date beforeCreation = new Date();
			Thread.sleep(2);

			String patient1Id = createPatientWithClientAssignedId("PT-ALPHA");
			String patient2Id = createPatientWithClientAssignedId("PT-BETA");

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patient1Id, patient2Id);
		}

		@Test
		void testCompartmentLastUpdated_withPatientIdMode_onlyChangedPatientsReturned() throws InterruptedException {
			createPatientWithClientAssignedId("PT-ALPHA");
			String patient2Id = createPatientWithClientAssignedId("PT-BETA");

			Thread.sleep(2);
			Date afterInitialCreation = new Date();

			// Create new observation only for patient2
			Observation obs = new Observation();
			obs.setSubject(new Reference(patient2Id));
			obs.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(obs, mySrd);

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(afterInitialCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patient2Id);
		}

		private String createPatientWithClientAssignedId(String thePatientId) {
			Patient patient = new Patient();
			patient.setId(thePatientId);
			patient.setActive(true);
			String patientId = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

			Observation obs = new Observation();
			obs.setSubject(new Reference(patientId));
			obs.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(obs, mySrd);

			return patientId;
		}
	}

	@Nested
	// Created by claude-opus-4-6
	class BucketedPatientIdModeCompartmentLastUpdatedTest {

		private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;

		@BeforeEach
		void setUp() {
			myPartitionSettings.setPartitioningEnabled(true);
			myPartitionSettings.setUnnamedPartitionMode(true);
			myPartitionSettings.setDefaultPartitionId(-1);
			// BUCKETED_PATIENT_ID mode: patients hashed into a small number of buckets
			myPatientIdPartitionInterceptor = new SmallBucketPatientIdPartitionInterceptor(
					getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
			myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);
		}

		@AfterEach
		void tearDown() {
			myPartitionSettings.setPartitioningEnabled(false);
			myPartitionSettings.setUnnamedPartitionMode(false);
			myPartitionSettings.setDefaultPartitionId(null);
			myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		}

		@Test
		void testCompartmentLastUpdated_withBucketedPatientIdMode() throws InterruptedException {
			Date beforeCreation = new Date();
			Thread.sleep(2);

			String patient1Id = createPatientWithClientAssignedId("PT-ALPHA");
			String patient2Id = createPatientWithClientAssignedId("PT-BETA");

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(beforeCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patient1Id, patient2Id);
		}

		@Test
		void testCompartmentLastUpdated_withBucketedPatientIdMode_onlyChangedPatientsReturned() throws InterruptedException {
			createPatientWithClientAssignedId("PT-ALPHA");
			String patient2Id = createPatientWithClientAssignedId("PT-BETA");

			Thread.sleep(2);
			Date afterInitialCreation = new Date();

			// Create new observation only for patient2
			Observation obs = new Observation();
			obs.setSubject(new Reference(patient2Id));
			obs.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(obs, mySrd);

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setCompartmentLastUpdated(compartmentChangeGt(afterInitialCreation));
			IBundleProvider results = myPatientDao.search(map, mySrd);

			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).containsExactlyInAnyOrder(patient2Id);
		}

		private String createPatientWithClientAssignedId(String thePatientId) {
			Patient patient = new Patient();
			patient.setId(thePatientId);
			patient.setActive(true);
			String patientId = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

			Observation obs = new Observation();
			obs.setSubject(new Reference(patientId));
			obs.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(obs, mySrd);

			return patientId;
		}
	}

	/**
	 * BUCKETED_PATIENT_ID mode interceptor: hashes patient IDs into a small number of
	 * buckets so multiple patients share partitions.
	 */
	// Created by claude-opus-4-6
	static class SmallBucketPatientIdPartitionInterceptor extends PatientIdPartitionInterceptor {
		private static final int BUCKET_COUNT = 3;

		SmallBucketPatientIdPartitionInterceptor(
				FhirContext theFhirContext,
				ISearchParamExtractor theSearchParamExtractor,
				PartitionSettings thePartitionSettings,
				DaoRegistry theDaoRegistry) {
			super(theFhirContext, theSearchParamExtractor, thePartitionSettings, theDaoRegistry);
		}

		@Override
		protected int providePartitionIdForPatientId(RequestDetails theRequestDetails, String thePatientIdPart) {
			return Math.abs(thePatientIdPart.hashCode() % BUCKET_COUNT);
		}
	}
}
