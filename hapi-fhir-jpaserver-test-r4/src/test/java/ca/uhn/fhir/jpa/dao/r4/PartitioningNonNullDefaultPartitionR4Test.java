package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"ConstantConditions"})
public class PartitioningNonNullDefaultPartitionR4Test extends BasePartitioningR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitioningNonNullDefaultPartitionR4Test.class);

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setDefaultPartitionId(1);

		// This test relies on this interceptor already being in place, which it should be unless
		// another test misbehaved
		assertEquals(1, myInterceptorRegistry.getAllRegisteredInterceptors().stream().filter(t->t instanceof SearchParamValidatingInterceptor).count());
	}

	@AfterEach
	@Override
	public void after() {
		super.after();

		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
	}

	@Test
	public void patchResource_withRegisteredInterceptorAnd_works() {
		// setup
		IParser jsonParser = myFhirContext.newJsonParser();
		IFhirResourceDao<Encounter> encounterDao = myDaoRegistry.getResourceDao(Encounter.class);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		// use a non-default partition
		requestDetails.setRequestPartitionId(
			RequestPartitionId.fromPartitionId(myPartitionId2)
		);

		/*
		 * This interceptor does nothing; but we need it registered
		 * for the RuntimeConfig lookup we're testing to run
		 */
		Object interceptor = new Object() {
			@Hook(Pointcut.STORAGE_PARTITION_SELECTED)
			public void partitionSelected(
				RequestDetails theRequestDetails,
				RequestPartitionId theRequestPartitionId,
				RuntimeResourceDefinition theRuntimeResourceDefinition) {
				// do nothing
			}

			@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY)
			public RequestPartitionId partitionIdentify() {
				return RequestPartitionId.allPartitions();
			}
		};
		myInterceptorRegistry.registerInterceptor(interceptor);

		try {
			Encounter encounter = createEncounter(jsonParser);
			Location location = createLocation(jsonParser);
			Bundle bundle = createPatchEncounterBundle(jsonParser);

			// create resources
			myDaoRegistry.getResourceDao(Location.class)
				.create(location, requestDetails);
			DaoMethodOutcome encounterCreate = encounterDao
				.create(encounter, requestDetails);
			// no location for initial create
			Encounter enc = encounterDao
				.read(encounterCreate.getId().toUnqualifiedVersionless(), requestDetails);
			{
				assertEquals(1, enc.getLocation().size());
				Encounter.EncounterLocationComponent loc = enc.getLocation().get(0);
				assertNotNull(loc.getLocation());
				assertEquals("http://example.com", loc.getLocation().getIdentifier().getSystem());
			}

			// test
			Bundle result = mySystemDao.transaction(requestDetails, bundle);

			assertNotNull(result);
			assertEquals(1, result.getEntry().size());
			assertEquals("200 OK", result.getEntry().get(0)
				.getResponse()
				.getStatus());

			// verify we've updated the location object
			Encounter updated = encounterDao
				.read(encounterCreate.getId().toUnqualifiedVersionless(), requestDetails);
			assertNotNull(updated);
			{
				Location referencedLocation = myLocationDao.read(new IdType(location.getId()), new SystemRequestDetails());
				assertEquals(1, updated.getLocation().size());
				Encounter.EncounterLocationComponent loc = updated.getLocation().get(0);
				assertNotNull(loc.getLocation());
				assertEquals(referencedLocation.getIdElement().toUnqualifiedVersionless().getValue(), loc.getLocation().getReference());
			}
		} finally {
			// cleanup
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	private Bundle createPatchEncounterBundle(IParser jsonParser) {
		Bundle bundle;
		{
			String bundleStr = """
				{
					"entry": [
						{
							"request": {
								"method": "PATCH",
								"url": "Encounter?identifier=urn:th:HS:0075240H:vn|3209505"
							},
							"resource": {
								"parameter": [
									{
										"part": [
											{
												"name": "type",
												"valueCode": "replace"
											},
											{
												"valueString": "Encounter.location.first()",
												"name": "path"
											},
											{
												"part": [
													{
														"valueReference": {
															"reference": "Location?identifier=urn:th:HS:0075240H:bd|SSU-01A"
														},
														"name": "location"
													}
												],
												"name": "value"
											}
										],
										"name": "operation"
									}
								],
								"resourceType": "Parameters"
							}
						}
					],
					"id": "4e867c2a-897b-4540-9dc6-158085765705",
					"type": "transaction",
					"resourceType": "Bundle"
				}
				""";
			bundle = jsonParser.parseResource(Bundle.class, bundleStr);
		}
		return bundle;
	}

	private Location createLocation(IParser jsonParser) {
		Location location;
		{
			@Language("JSON")
			String locationStr = """
				{
					"resourceType": "Location",
					"identifier": [
						{
							"system": "urn:th:HS:0075240H:bd",
							"value": "SSU-01A"
						}
					]
				}
				""";
			location = jsonParser.parseResource(Location.class, locationStr);
		}
		return location;
	}

	private Encounter createEncounter(IParser jsonParser) {
		Encounter encounter;
		{
			@Language("JSON")
			String encString = """
				{
					"resourceType": "Encounter",
					"identifier": [
						{
							"system": "urn:th:HS:0075240H:vn",
							"value": "3209505"
						}
					],
					"location": [{
						"location": {
							"identifier": [{
								"system": "http://example.com",
								"value": "123"
							}]
						}
					}],
					"status": "in-progress"
				}
				""";
			encounter = jsonParser.parseResource(Encounter.class, encString);
		}
		return encounter;
	}

	@Test
	public void testCreateAndSearch_NonPartitionable() {
		addNextTargetPartitionForCreateDefaultPartition();
		// we need two read partition accesses for when the creation of the SP triggers a reindex of Patient
		addNextTargetPartitionForReadDefaultPartition(); // one for search param validation
		addNextTargetPartitionForReadDefaultPartition(); // and one for the reindex job
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		Long id = mySearchParameterDao.create(sp, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addNextTargetPartitionForReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous().add("code", new TokenParam("extpatorg")), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/" + id);

		// Search on All Resources
		addNextTargetPartitionForReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/" + id);

	}

	@Test
	public void testCreateAndSearch_NonPartitionable_ForcedId() {
		addNextTargetPartitionForCreateWithIdDefaultPartition();
		// we need two read partition accesses for when the creation of the SP triggers a reindex of Patient
		addNextTargetPartitionForReadDefaultPartition(); // one for search param validation
		addNextTargetPartitionForReadDefaultPartition(); // and one for the reindex job
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/A");
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		mySearchParameterDao.update(sp, mySrd);

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findAll().get(0);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addNextTargetPartitionForReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous().add("code", new TokenParam("extpatorg")), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/A");

		// Search on All Resources
		addNextTargetPartitionForReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/A");

	}

	@Test
	public void testCreateAndSearch_Partitionable_ForcedId() {
		addNextTargetPartitionForCreateWithIdDefaultPartition();
		Patient patient = new Patient();
		patient.setId("A");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findAll().get(0);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addNextTargetPartitionForReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous().add("identifier", new TokenParam("http://foo", "123")), mySrd));
		assertThat(outcome).containsExactly("Patient/A");

		// Search on All Resources
		addNextTargetPartitionForReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("Patient/A");

	}


	@Test
	public void testCreateAndSearch_Partitionable() {
		addNextTargetPartitionForCreateDefaultPartition();
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem("http://foo").setCode("TAG");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setActive(true);
		Long id = myPatientDao.create(patient, mySrd).getId().getIdPartAsLong();

		logAllResourceTags();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addNextTargetPartitionForReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous().add("identifier", new TokenParam("http://foo", "123")), mySrd));
		assertThat(outcome).containsExactly("Patient/" + id);

		// Search on Tag
		addNextTargetPartitionForReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous().add("_tag", new TokenParam("http://foo", "TAG")), mySrd));
		assertThat(outcome).containsExactly("Patient/" + id);

		// Search on All Resources
		addNextTargetPartitionForReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("Patient/" + id);

	}



	@Test
	public void testRead_Partitionable() {
		addNextTargetPartitionForCreateDefaultPartition();
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem("http://foo").setCode("TAG");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setActive(true);
		Long id = myPatientDao.create(patient, mySrd).getId().getIdPartAsLong();

		addNextTargetPartitionForReadDefaultPartition();
		patient = myPatientDao.read(new IdType("Patient/" + id), mySrd);
		assertTrue(patient.getActive());

		// Wrong partition
		addNextTargetPartitionsForRead(2);
		try {
			myPatientDao.read(new IdType("Patient/" + id), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}


}
