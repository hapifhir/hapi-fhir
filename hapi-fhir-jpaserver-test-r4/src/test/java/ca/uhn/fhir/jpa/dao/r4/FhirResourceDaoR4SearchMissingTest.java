package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR4SearchMissingTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchMissingTest.class);

	@BeforeEach
	public void beforeResetMissing() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
	}

	@AfterEach
	public void afterResetSearch() {
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
	}

	@Test
	public void testIndexMissingFieldsDisabledDontAllowInSearch_NonReference() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_ACTIVE, new StringParam().setMissing(true));
		try {
			myPatientDao.search(params);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(985) + ":missing modifier is disabled on this server", e.getMessage());
		}
	}

	@Test
	public void testIndexMissingFieldsDisabledDontAllowInSearch_Reference() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_ORGANIZATION, new StringParam().setMissing(true));
		try {
			myPatientDao.search(params);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(985) + ":missing modifier is disabled on this server", e.getMessage());
		}
	}

	@Test
	public void testIndexMissingFieldsDisabledDontCreateIndexes() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		Organization org = new Organization();
		org.setActive(true);
		myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			assertThat(mySearchParamPresentDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamStringDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamDateDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamTokenDao.findAll(), hasSize(1));
			assertThat(myResourceIndexedSearchParamQuantityDao.findAll(), empty());
		});

	}

	@Test
	public void testIndexMissingFieldsDisabledDontCreateIndexesWithNormalizedQuantitySearchSupported() {

		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Organization org = new Organization();
		org.setActive(true);
		myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			assertThat(mySearchParamPresentDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamStringDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamDateDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamTokenDao.findAll(), hasSize(1));
			assertThat(myResourceIndexedSearchParamQuantityDao.findAll(), empty());
		});

	}

	@Test
	public void testIndexMissingFieldsDisabledDontCreateIndexesWithNormalizedQuantityStorageSupported() {

		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		Organization org = new Organization();
		org.setActive(true);
		myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			assertThat(mySearchParamPresentDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamStringDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamDateDao.findAll(), empty());
			assertThat(myResourceIndexedSearchParamTokenDao.findAll(), hasSize(1));
			assertThat(myResourceIndexedSearchParamQuantityDao.findAll(), empty());
		});

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchResourceReferenceMissingChain() {
		IIdType oid1;
		{
			Organization org = new Organization();
			org.setActive(true);
			oid1 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType tid1;
		{
			Task task = new Task();
			task.setRequester(new Reference(oid1));
			tid1 = myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType tid2;
		{
			Task task = new Task();
			task.setOwner(new Reference(oid1));
			tid2 = myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
		}

		IIdType oid2;
		{
			Organization org = new Organization();
			org.setActive(true);
			org.setName("NAME");
			oid2 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType tid3;
		{
			Task task = new Task();
			task.setRequester(new Reference(oid2));
			tid3 = myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap map;
		List<IIdType> ids;

		map = new SearchParameterMap();
		map.add(Organization.SP_NAME, new StringParam().setMissing(true));
		ids = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(ids, contains(oid1));

		ourLog.info("Starting Search 2");

		map = new SearchParameterMap();
		map.add(Task.SP_REQUESTER, new ReferenceParam("Organization", "name:missing", "true"));
		ids = toUnqualifiedVersionlessIds(myTaskDao.search(map));
		assertThat(ids, contains(tid1)); // NOT tid2

		map = new SearchParameterMap();
		map.add(Task.SP_REQUESTER, new ReferenceParam("Organization", "name:missing", "false"));
		ids = toUnqualifiedVersionlessIds(myTaskDao.search(map));
		assertThat(ids, contains(tid3));

		map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("Organization", "name:missing", "true"));
		ids = toUnqualifiedVersionlessIds(myPatientDao.search(map));
		assertThat(ids, empty());

	}

	@Test
	public void testSearchWithMissingDate() {
		IIdType orgId = myOrganizationDao.create(new Organization(), mySrd).getId();
		IIdType notMissing;
		IIdType missing;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			notMissing = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		// Date Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			DateParam param = new DateParam();
			param.setMissing(false);
			params.add(Patient.SP_BIRTHDATE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(notMissing));
			assertThat(patients, not(containsInRelativeOrder(missing)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			DateParam param = new DateParam();
			param.setMissing(true);
			params.add(Patient.SP_BIRTHDATE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithMissingCoords() {
		String locId = myLocationDao.create(new Location(), mySrd).getId().toUnqualifiedVersionless().getValue();
		String locId2 = myLocationDao.create(new Location().setPosition(new Location.LocationPositionComponent(new DecimalType(10), new DecimalType(10))), mySrd).getId().toUnqualifiedVersionless().getValue();

		runInTransaction(() -> {
			ourLog.info("Coords:\n * {}", myResourceIndexedSearchParamCoordsDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			TokenParam param = new TokenParam();
			param.setMissing(true);
			params.add(Location.SP_NEAR, param);
			myCaptureQueriesListener.clear();
			List<String> patients = toUnqualifiedVersionlessIdValues(myLocationDao.search(params));
			myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
			assertThat(patients, containsInRelativeOrder(locId));
			assertThat(patients, not(containsInRelativeOrder(locId2)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			TokenParam param = new TokenParam();
			param.setMissing(false);
			params.add(Location.SP_NEAR, param);
			List<String> patients = toUnqualifiedVersionlessIdValues(myLocationDao.search(params));
			assertThat(patients, containsInRelativeOrder(locId2));
			assertThat(patients, not(containsInRelativeOrder(locId)));
		}
	}

	@Test
	public void testSearchWithMissingDate2() {
		MedicationRequest mr1 = new MedicationRequest();
		mr1.addCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
		mr1.addDosageInstruction().getTiming().addEventElement().setValueAsString("2017-01-01");
		IIdType id1 = myMedicationRequestDao.create(mr1).getId().toUnqualifiedVersionless();

		MedicationRequest mr2 = new MedicationRequest();
		mr2.addCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
		IIdType id2 = myMedicationRequestDao.create(mr2).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(MedicationRequest.SP_DATE, new DateParam().setMissing(true));
		IBundleProvider results = myMedicationRequestDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);

		assertThat(ids, contains(id2.getValue()));

	}

	@Test
	public void testSearchWithMissingQuantity() {
		IIdType notMissing;
		IIdType missing;
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("002");
			obs.setValue(new Quantity(123));
			notMissing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		// Quantity Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			QuantityParam param = new QuantityParam();
			param.setMissing(false);
			params.add(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			QuantityParam param = new QuantityParam();
			param.setMissing(true);
			params.add(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithMissingQuantityWithNormalizedQuantitySearchSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		IIdType notMissing;
		IIdType missing;
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("002");
			obs.setValue(new Quantity(123));
			notMissing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		runInTransaction(() -> {
			ourLog.info("Quantity Indexes:\n * {}", myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).map(t -> t.toString()).collect(Collectors.joining("\n * ")));
			ourLog.info("Normalized Quantity Indexes:\n * {}", myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		// Quantity Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			QuantityParam param = new QuantityParam();
			param.setMissing(false);
			params.add(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			QuantityParam param = new QuantityParam();
			param.setMissing(true);
			params.add(Observation.SP_VALUE_QUANTITY, param);
			myCaptureQueriesListener.clear();
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			myCaptureQueriesListener.logSelectQueries();
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}

	}

	@Test
	public void testSearchWithMissingQuantityWithNormalizedQuantityStorageSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		IIdType notMissing;
		IIdType missing;
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("002");
			obs.setValue(new Quantity(123));
			notMissing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		// Quantity Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			QuantityParam param = new QuantityParam();
			param.setMissing(false);
			params.add(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			QuantityParam param = new QuantityParam();
			param.setMissing(true);
			params.add(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}

	}


	@Test
	public void testSearchWithMissingReference() {
		IIdType orgId = myOrganizationDao.create(new Organization(), mySrd).getId().toUnqualifiedVersionless();
		IIdType notMissing;
		IIdType missing;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			notMissing = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		// Reference Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			ReferenceParam param = new ReferenceParam();
			param.setMissing(false);
			params.add(Patient.SP_ORGANIZATION, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			ReferenceParam param = new ReferenceParam();
			param.setMissing(true);
			params.add(Patient.SP_ORGANIZATION, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
			assertThat(patients, not(containsInRelativeOrder(orgId)));
		}
	}

	@Test
	public void testSearchWithMissingString() {
		IIdType orgId = myOrganizationDao.create(new Organization(), mySrd).getId();
		IIdType notMissing;
		IIdType missing;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			notMissing = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		// String Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			StringParam param = new StringParam();
			param.setMissing(false);
			params.add(Patient.SP_FAMILY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			StringParam param = new StringParam();
			param.setMissing(true);
			params.add(Patient.SP_FAMILY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithToken() {
		IIdType notMissing;
		IIdType missing;
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("002");
			obs.getCode().addCoding().setSystem("urn:system").setCode("002");
			notMissing = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}
		// Token Param
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			TokenParam param = new TokenParam();
			param.setMissing(false);
			params.add(Observation.SP_CODE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			TokenParam param = new TokenParam();
			param.setMissing(true);
			params.add(Observation.SP_CODE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

}
