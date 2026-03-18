package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
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
		map.setCompartmentChange(compartmentChangeGt(beforeCreation));
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
		map.setCompartmentChange(compartmentChangeGt(afterInitialCreation));
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
		map.setCompartmentChange(compartmentChangeGt(afterInitialCreation));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder(patient1Id);
	}

	@Test
	void testCompartmentChange_nonPatientResourceTypeThrowsException() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setCompartmentChange(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN, "2024-01-01")));

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
		map.setCompartmentChange(range);
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
		map.setCompartmentChange(compartmentChangeGt(afterCreation));
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

	private DateRangeParam compartmentChangeGt(Date theDate) {
		return new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, theDate));
	}
}
