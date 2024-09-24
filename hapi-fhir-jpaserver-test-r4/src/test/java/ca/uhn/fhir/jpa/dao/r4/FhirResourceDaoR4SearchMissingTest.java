package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR4SearchMissingTest {

	@Nested
	class IndexMissingDisabledTests extends MissingTests {
		@BeforeEach
		public void before() {
			myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.DISABLED);
		}

		@Test
		public void testIndexMissingFieldsDisabledDontCreateIndexes() {
			Organization org = new Organization();
			org.setActive(true);
			myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

			runInTransaction(() -> {
				assertThat(mySearchParamPresentDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamStringDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamDateDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamTokenDao.findAll()).hasSize(1);
				assertThat(myResourceIndexedSearchParamQuantityDao.findAll()).isEmpty();
			});

		}

		@Test
		public void testIndexMissingFieldsDisabledDontCreateIndexesWithNormalizedQuantitySearchSupported() {
			myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
			Organization org = new Organization();
			org.setActive(true);
			myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

			runInTransaction(() -> {
				assertThat(mySearchParamPresentDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamStringDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamDateDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamTokenDao.findAll()).hasSize(1);
				assertThat(myResourceIndexedSearchParamQuantityDao.findAll()).isEmpty();
			});

		}

		@Test
		public void testIndexMissingFieldsDisabledDontCreateIndexesWithNormalizedQuantityStorageSupported() {
			myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
			Organization org = new Organization();
			org.setActive(true);
			myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

			runInTransaction(() -> {
				assertThat(mySearchParamPresentDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamStringDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamDateDao.findAll()).isEmpty();
				assertThat(myResourceIndexedSearchParamTokenDao.findAll()).hasSize(1);
				assertThat(myResourceIndexedSearchParamQuantityDao.findAll()).isEmpty();
			});

		}
	}

	@Nested
	class IndexMissingEnabledTests extends MissingTests {
		@BeforeEach
		public void before() {
			myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.ENABLED);
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
			{
				Task task = new Task();
				task.setOwner(new Reference(oid1));
				myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
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
			ids = toUnqualifiedVersionlessIds(myOrganizationDao.search(map, mySrd));
			assertThat(ids).containsExactly(oid1);

			ourLog.info("Starting Search 2");

			map = new SearchParameterMap();
			map.add(Task.SP_REQUESTER, new ReferenceParam("Organization", "name:missing", "true"));
			ids = toUnqualifiedVersionlessIds(myTaskDao.search(map, mySrd));
			assertThat(ids).containsExactly(tid1); // NOT tid2

			map = new SearchParameterMap();
			map.add(Task.SP_REQUESTER, new ReferenceParam("Organization", "name:missing", "false"));
			ids = toUnqualifiedVersionlessIds(myTaskDao.search(map, mySrd));
			assertThat(ids).containsExactly(tid3);

			map = new SearchParameterMap();
			map.add(Patient.SP_ORGANIZATION, new ReferenceParam("Organization", "name:missing", "true"));
			ids = toUnqualifiedVersionlessIds(myPatientDao.search(map, mySrd));
			assertThat(ids).isEmpty();

		}
	}

	static class MissingTests extends BaseJpaR4Test {
		@AfterEach
		public void after() {
			myStorageSettings.setIndexMissingFields(new StorageSettings().getIndexMissingFields());
			myStorageSettings.setNormalizedQuantitySearchLevel(new StorageSettings().getNormalizedQuantitySearchLevel());

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
				List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(notMissing);
				assertThat(patients).doesNotContainSubsequence(missing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				DateParam param = new DateParam();
				param.setMissing(true);
				params.add(Patient.SP_BIRTHDATE, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
			}
		}

		@Test
		public void testSearchWithMissingCoords() {
			String locId = myLocationDao.create(new Location(), mySrd).getId().toUnqualifiedVersionless().getValue();
			String locId2 = myLocationDao.create(new Location().setPosition(new Location.LocationPositionComponent(new DecimalType(10), new DecimalType(10))), mySrd).getId().toUnqualifiedVersionless().getValue();

			runInTransaction(() -> ourLog.info("Coords:\n * {}",
					myResourceIndexedSearchParamCoordsDao.findAll().stream()
							.map(ResourceIndexedSearchParamCoords::toString).collect(Collectors.joining("\n * "))
					)
			);

			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				TokenParam param = new TokenParam();
				param.setMissing(true);
				params.add(Location.SP_NEAR, param);
				myCaptureQueriesListener.clear();
				List<String> patients = toUnqualifiedVersionlessIdValues(myLocationDao.search(params, mySrd));
				myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
				assertThat(patients).containsSubsequence(locId);
				assertThat(patients).doesNotContainSubsequence(locId2);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				TokenParam param = new TokenParam();
				param.setMissing(false);
				params.add(Location.SP_NEAR, param);
				List<String> patients = toUnqualifiedVersionlessIdValues(myLocationDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(locId2);
				assertThat(patients).doesNotContainSubsequence(locId);
			}
		}

		@Test
		public void testSearchWithMissingDate2() {
			MedicationRequest mr1 = new MedicationRequest();
			mr1.addCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
			mr1.addDosageInstruction().getTiming().addEventElement().setValueAsString("2017-01-01");
			myMedicationRequestDao.create(mr1, mySrd).getId().toUnqualifiedVersionless();

			MedicationRequest mr2 = new MedicationRequest();
			mr2.addCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
			IIdType id2 = myMedicationRequestDao.create(mr2, mySrd).getId().toUnqualifiedVersionless();

			SearchParameterMap map = new SearchParameterMap();
			map.add(MedicationRequest.SP_DATE, new DateParam().setMissing(true));
			IBundleProvider results = myMedicationRequestDao.search(map, mySrd);
			List<String> ids = toUnqualifiedVersionlessIdValues(results);

			assertThat(ids).containsExactly(id2.getValue());

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
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).doesNotContainSubsequence(missing);
				assertThat(patients).containsSubsequence(notMissing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				QuantityParam param = new QuantityParam();
				param.setMissing(true);
				params.add(Observation.SP_VALUE_QUANTITY, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
			}
		}

		@Test
		public void testSearchWithMissingQuantityWithNormalizedQuantitySearchSupported() {

			myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
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
				ourLog.info("Quantity Indexes:\n * {}",
						myResourceIndexedSearchParamQuantityDao.findAll().stream()
								.filter(t -> t.getParamName().equals("value-quantity")).map(ResourceIndexedSearchParamQuantity::toString).collect(Collectors.joining("\n * ")
								)
				);
				ourLog.info("Normalized Quantity Indexes:\n * {}",
						myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().
								filter(t -> t.getParamName().equals("value-quantity")).map(ResourceIndexedSearchParamQuantityNormalized::toString).collect(Collectors.joining("\n * ")
								)
				);
			});

			// Quantity Param
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				QuantityParam param = new QuantityParam();
				param.setMissing(false);
				params.add(Observation.SP_VALUE_QUANTITY, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).doesNotContainSubsequence(missing);
				assertThat(patients).containsSubsequence(notMissing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				QuantityParam param = new QuantityParam();
				param.setMissing(true);
				params.add(Observation.SP_VALUE_QUANTITY, param);
				myCaptureQueriesListener.clear();
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				myCaptureQueriesListener.logSelectQueries();
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
			}

		}

		@Test
		public void testSearchWithMissingQuantityWithNormalizedQuantityStorageSupported() {

			myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
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
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).doesNotContainSubsequence(missing);
				assertThat(patients).containsSubsequence(notMissing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				QuantityParam param = new QuantityParam();
				param.setMissing(true);
				params.add(Observation.SP_VALUE_QUANTITY, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
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
				List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
				assertThat(patients).doesNotContainSubsequence(missing);
				assertThat(patients).containsSubsequence(notMissing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				ReferenceParam param = new ReferenceParam();
				param.setMissing(true);
				params.add(Patient.SP_ORGANIZATION, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
				assertThat(patients).doesNotContainSubsequence(orgId);
			}
		}


		@Test
		public void testSearchWithMissingReference_resourceTypeWithMultipleReferences() {
			IIdType patientId = createPatient();
			IIdType observationId = createObservation(withSubject(patientId));

			SearchParameterMap params = new SearchParameterMap();
			params.add(Observation.SP_PERFORMER, new ReferenceParam().setMissing(true));
			IBundleProvider bundleProvider = myObservationDao.search(params, mySrd);
			assertThat(bundleProvider.getAllResourceIds()).containsExactly(observationId.getIdPart());
		}

		@Test
		public void testSearchWithMissingReference_searchParamMultiplePaths() {
			IIdType encounterId = createEncounter();
			createObservation(withEncounter(encounterId.getValue()));

			SearchParameterMap params = new SearchParameterMap();
			params.add(Observation.SP_ENCOUNTER, new ReferenceParam().setMissing(true));
			IBundleProvider bundleProvider = myObservationDao.search(params, mySrd);
			assertThat(bundleProvider.getAllResourceIds()).isEmpty();
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
				List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
				assertThat(patients).doesNotContainSubsequence(missing);
				assertThat(patients).containsSubsequence(notMissing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				StringParam param = new StringParam();
				param.setMissing(true);
				params.add(Patient.SP_FAMILY, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
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
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).doesNotContainSubsequence(missing);
				assertThat(patients).containsSubsequence(notMissing);
			}
			{
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronous(true);
				TokenParam param = new TokenParam();
				param.setMissing(true);
				params.add(Observation.SP_CODE, param);
				List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params, mySrd));
				assertThat(patients).containsSubsequence(missing);
				assertThat(patients).doesNotContainSubsequence(notMissing);
			}
		}
	}

}
