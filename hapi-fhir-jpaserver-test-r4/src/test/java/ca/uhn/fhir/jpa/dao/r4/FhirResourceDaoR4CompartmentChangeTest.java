package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-6
class FhirResourceDaoR4CompartmentChangeTest extends BaseJpaR4Test {

	@Test
	void testCompartmentChange_allPatientsReturnedWhenCompartmentResourcesCreatedAfterDate() {
		Date beforeCreation = new Date();

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
	void testCompartmentLastUpdated_combinedWithMultipleParameters() {
		Date beforeCreation = new Date();

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
	void testCompartmentLastUpdated_combinedWithSort() {
		Date beforeCreation = new Date();

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
		void testCompartmentLastUpdated_withNamedPartitions() {
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
			myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);

			verifyCompartmentLastUpdatedReturnsPatientsFromBothPartitions();
		}

		@Test
		void testCompartmentLastUpdated_withUnnamedPartitionMode() {
			myPartitionSettings.setUnnamedPartitionMode(true);

			verifyCompartmentLastUpdatedReturnsPatientsFromBothPartitions();
		}

		private void verifyCompartmentLastUpdatedReturnsPatientsFromBothPartitions() {
			Date beforeCreation = new Date();

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

		@Interceptor
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
}
