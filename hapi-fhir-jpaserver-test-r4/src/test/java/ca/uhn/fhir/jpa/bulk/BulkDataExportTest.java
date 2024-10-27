package ca.uhn.fhir.jpa.bulk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.JpaJobPersistenceImpl;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.LineIterator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TagsInlineTest.createSearchParameterForInlineSecurity;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.await;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BulkDataExportTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportTest.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	@Autowired
	private IJobPersistence myJobPersistence;
	private JpaJobPersistenceImpl myJobPersistenceImpl;

	@AfterEach
	void afterEach() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setTagStorageMode(defaults.getTagStorageMode());
		myStorageSettings.setResourceClientIdStrategy(defaults.getResourceClientIdStrategy());
		myStorageSettings.setBulkExportFileMaximumSize(defaults.getBulkExportFileMaximumSize());
	}

	@BeforeEach
	public void beforeEach() {
		myStorageSettings.setJobFastTrackingEnabled(false);
		myJobPersistenceImpl = ProxyUtil.getSingletonTarget(myJobPersistence, JpaJobPersistenceImpl.class);
	}

	@Spy
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@RegisterExtension
	private final HttpClientExtension mySender = new HttpClientExtension();

	@Test
	public void testGroupBulkExportWithTypeFilter() {
		// Create some resources
		Patient patient = new Patient();
		patient.setId("PF");
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("PM");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/PF");
		group.addMember().getEntity().setReference("Patient/PM");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId("Group/G");
		options.setFilters(Sets.newHashSet("Patient?gender=female"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, Collections.singletonList("Patient/PF"), Collections.singletonList("Patient/PM"));
	}

	@Test
	public void testGroupBulkExportWithMissingObservationSearchParams() {
		mySearchParameterDao.update(createDisabledObservationPatientSearchParameter(), mySrd);
		mySearchParamRegistry.forceRefresh();

		// Create some resources
		Patient patient = new Patient();
		patient.setId("PF");
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("PM");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/PF");
		group.addMember().getEntity().setReference("Patient/PM");
		myClient.update().resource(group).execute();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.setSubject(new Reference("Patient/PF"));
		String obsId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation"));
		options.setGroupId("Group/G");
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/PF", "Patient/PM"), Collections.singletonList(obsId));
	}

	@Test
	public void testGroupBulkExportWithMissingPatientSearchParams() {
		mySearchParameterDao.update(createDisabledPatientPractitionerSearchParameter(), mySrd);
		mySearchParamRegistry.forceRefresh();

		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		String practId = myClient.create().resource(practitioner).execute().getId().toUnqualifiedVersionless().getValue();

		// Create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		patient.addGeneralPractitioner().setReference(practId);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/P1");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Practitioner"));
		options.setGroupId("Group/G");
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, Collections.singletonList("Patient/P1"), Collections.singletonList(practId));
	}

	private SearchParameter createDisabledObservationPatientSearchParameter() {
		SearchParameter observation_patient = new SearchParameter();
		observation_patient.setId("clinical-patient");
		observation_patient.addBase("Observation");
		observation_patient.setStatus(Enumerations.PublicationStatus.RETIRED);
		observation_patient.setCode("patient");
		observation_patient.setType(Enumerations.SearchParamType.REFERENCE);
		observation_patient.addTarget("Patient");
		observation_patient.setExpression("Observation.subject.where(resolve() is Patient)");
		return observation_patient;
	}

	private SearchParameter createDisabledPatientPractitionerSearchParameter() {
		SearchParameter patient_practitioner = new SearchParameter();
		patient_practitioner.setId("Patient-general-practitioner");
		patient_practitioner.addBase("Patient");
		patient_practitioner.setStatus(Enumerations.PublicationStatus.RETIRED);
		patient_practitioner.setCode("general-practitioner");
		patient_practitioner.setType(Enumerations.SearchParamType.REFERENCE);
		patient_practitioner.addTarget("Practitioner");
		patient_practitioner.setExpression("Patient.generalPractitioner");
		return patient_practitioner;
	}

	@Test
	public void testGroupBulkExportWithTypeFilter_OnTags_InlineTagMode() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);
		mySearchParameterDao.update(createSearchParameterForInlineSecurity(), mySrd);
		mySearchParamRegistry.forceRefresh();

		// Create some resources
		Patient patient = new Patient();
		patient.setId("PF");
		patient.getMeta().addSecurity("http://security", "val0", null);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("PM");
		patient.getMeta().addSecurity("http://security", "val1", null);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/PF");
		group.addMember().getEntity().setReference("Patient/PM");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId("Group/G");
		options.setFilters(Sets.newHashSet("Patient?_security=http://security|val1"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, Collections.singletonList("Patient/PM"), Collections.singletonList("Patient/PF"));
	}


	@Test
	public void testGroupBulkExportNotInGroup_DoesNotShowUp() {
		// Create some resources
		Patient patient = new Patient();
		patient.setId("PING1");
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("PING2");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("PNING3");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G2");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/PING1");
		group.addMember().getEntity().setReference("Patient/PING2");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId("Group/G2");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/PING1", "Patient/PING2"), Collections.singletonList("Patient/PNING3"));
	}

	@Test
	public void testTwoBulkExportsInARow() {
		// Create some resources
		Patient patient = new Patient();
		patient.setId("PING1");
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G2");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/PING1");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId("Group/G2");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		myCaptureQueriesListener.clear();
		verifyBulkExportResults(options, List.of("Patient/PING1"), Collections.singletonList("Patient/PNING3"));
		myCaptureQueriesListener.logSelectQueries();
		ourLog.error("************");
		myCaptureQueriesListener.clear();
		try {
			verifyBulkExportResults(options, List.of("Patient/PING1"), Collections.singletonList("Patient/PNING3"));
		} finally {
			myCaptureQueriesListener.logSelectQueries();
		}
	}

	@Test
	public void testPatientBulkExportWithSingleId() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		// create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Observation observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P1"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P1"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		// diff patient
		patient = new Patient();
		patient.setId("P2");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P2"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId2 = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P2"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId2 = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId3 = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientIds(Set.of("Patient/P1"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		verifyBulkExportResults(options, List.of("Patient/P1", obsId, encId), List.of("Patient/P2", obsId2, encId2, obsId3));
	}

	@Test
	public void testBulkExportParametersPersistExtraData() {
		// setup
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		List<String> ids = new ArrayList<>();

		// create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		ids.add("Patient/P1");

		Observation observation;
		for (int i = 0; i < 5; i++) {
			observation = new Observation();
			observation.setSubject(new Reference().setReference("Patient/P1"));
			observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
			String obsId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();
			ids.add(obsId);
		}

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P1"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();
		ids.add(encId);

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientIds(Set.of("Patient/P1"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		String key = "counter";
		String value = "value_";
		options.setUserData(key, value);

		List<String> valueSet = new ArrayList<>();
		Object interceptor = new Object() {
			@Hook(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)
			public void onExpandResources(IBaseResource theBaseResource, BulkExportJobParameters theParams) {
				// this will be called once per every resource
				String value = (String) theParams.getUserData().get(key);
				valueSet.add(value + theBaseResource.getIdElement().toUnqualifiedVersionless().getValue());
			}
		};
		myInterceptorRegistry.registerInterceptor(interceptor);

		try {
			verifyBulkExportResults(options, ids, new ArrayList<>());

			assertFalse(valueSet.isEmpty());
			assertEquals(ids.size(), valueSet.size(),
				"Expected " + String.join(", ", ids) + ". Actual : " + String.join(", ", valueSet));
			for (String id : valueSet) {
				// should start with our value from the key-value pairs
				assertThat(id).startsWith(value);
				assertThat(ids).contains(id.substring(value.length()));
			}
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testPatientBulkExportWithMultiIds() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		// create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Observation observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P1"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P1"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		// diff patient
		patient = new Patient();
		patient.setId("P2");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P2"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId2 = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P2"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId2 = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		// yet another diff patient
		patient = new Patient();
		patient.setId("P3");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P3"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId3 = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P3"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId3 = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientIds(Set.of("Patient/P1", "Patient/P2"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		verifyBulkExportResults(options, List.of("Patient/P1", obsId, encId, "Patient/P2", obsId2, encId2), List.of("Patient/P3", obsId3, encId3));
	}

	@Test
	public void testGroupBulkExportGroupIncludePractitionerOrganizationLocation_ShouldShowUp() {
		// Create some resources
		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		String practId = myClient.create().resource(practitioner).execute().getId().toUnqualifiedVersionless().getValue();

		Organization organization = new Organization();
		organization.setActive(true);
		String orgId = myClient.create().resource(organization).execute().getId().toUnqualifiedVersionless().getValue();

		organization = new Organization();
		organization.setActive(true);
		String orgId2 = myClient.create().resource(organization).execute().getId().toUnqualifiedVersionless().getValue();

		Location location = new Location();
		location.setStatus(Location.LocationStatus.ACTIVE);
		String locId = myClient.create().resource(location).execute().getId().toUnqualifiedVersionless().getValue();

		location = new Location();
		location.setStatus(Location.LocationStatus.ACTIVE);
		String locId2 = myClient.create().resource(location).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("P2");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Encounter encounter = new Encounter();
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		encounter.setSubject(new Reference("Patient/P1"));
		Encounter.EncounterParticipantComponent encounterParticipantComponent = new Encounter.EncounterParticipantComponent();
		encounterParticipantComponent.setIndividual(new Reference(practId));
		encounter.addParticipant(encounterParticipantComponent);
		Encounter.EncounterLocationComponent encounterLocationComponent = new Encounter.EncounterLocationComponent();
		encounterLocationComponent.setLocation(new Reference(locId));
		encounter.addLocation(encounterLocationComponent);
		encounter.setServiceProvider(new Reference(orgId));
		String encId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		// Second encounter that links to the same location and practitioner
		Encounter encounter2 = new Encounter();
		encounter2.setStatus(Encounter.EncounterStatus.INPROGRESS);
		encounter2.setSubject(new Reference("Patient/P1"));
		Encounter.EncounterParticipantComponent encounterParticipantComponent2 = new Encounter.EncounterParticipantComponent();
		encounterParticipantComponent2.setIndividual(new Reference(practId));
		encounter2.addParticipant(encounterParticipantComponent2);
		Encounter.EncounterLocationComponent encounterLocationComponent2 = new Encounter.EncounterLocationComponent();
		encounterLocationComponent2.setLocation(new Reference(locId));
		encounter2.addLocation(encounterLocationComponent2);
		encounter2.setServiceProvider(new Reference(orgId));
		String encId2 = myClient.create().resource(encounter2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter encounter3 = new Encounter();
		encounter3.setStatus(Encounter.EncounterStatus.INPROGRESS);
		encounter3.setSubject(new Reference("Patient/P2"));
		Encounter.EncounterLocationComponent encounterLocationComponent3 = encounter3.getLocationFirstRep();
		encounterLocationComponent3.setLocation(new Reference(locId2));
		String encId3 = myClient.create().resource(encounter3).execute().getId().toUnqualifiedVersionless().getValue();

		Group group = new Group();
		group.setId("Group/G1");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/P1");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Encounter", "Practitioner", "Organization"));
		options.setGroupId("Group/G1");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", practId, orgId, encId, encId2), List.of("Patient/P2", orgId2, encId3));
	}

	@Test
	public void testGroupBulkExportGroupIncludePractitionerLinkedFromTwoResourceTypes() {
		// Create some resources
		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		String practId = myClient.create().resource(practitioner).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		patient.addGeneralPractitioner().setReference(practId);
		myClient.update().resource(patient).execute();

		Encounter encounter = new Encounter();
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		encounter.setSubject(new Reference("Patient/P1"));
		encounter.addParticipant().setIndividual(new Reference(practId));
		String encId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.setSubject(new Reference("Patient/P1"));
		observation.getPerformerFirstRep().setReference(practId);
		String obsId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		Group group = new Group();
		group.setId("Group/G1");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/P1");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Encounter", "Observation", "Practitioner"));
		options.setGroupId("Group/G1");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", practId, encId, obsId), Collections.emptyList());
	}

	@Test
	public void testGroupBulkExportGroupIncludeDevice_ShouldShowUp() {
		// Create some resources
		Device device = new Device();
		device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		String devId = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless().getValue();

		device = new Device();
		device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		String devId2 = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless().getValue();

		device = new Device();
		device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		String devId3 = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("P2");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.setDevice(new Reference(devId));
		observation.setSubject(new Reference("Patient/P1"));
		String obsId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

		Provenance provenance = new Provenance();
		provenance.addAgent().setWho(new Reference(devId2));
		provenance.addTarget(new Reference("Patient/P1"));
		String provId = myClient.create().resource(provenance).execute().getId().toUnqualifiedVersionless().getValue();

		provenance = new Provenance();
		provenance.addAgent().setWho(new Reference(devId3));
		provenance.addTarget(new Reference("Patient/P2"));
		String provId2 = myClient.create().resource(provenance).execute().getId().toUnqualifiedVersionless().getValue();

		Group group = new Group();
		group.setId("Group/G1");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/P1");
		myClient.update().resource(group).execute();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Provenance", "Device"));
		options.setGroupId("Group/G1");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", obsId, provId, devId, devId2), List.of("Patient/P2", provId2, devId3));
	}

	@Test
	public void testGroupBulkExport_QualifiedSubResourcesOfUnqualifiedPatientShouldShowUp() throws InterruptedException {

		// Patient with lastUpdated before _since
		Patient patient = new Patient();
		patient.setId("A");
		myClient.update().resource(patient).execute();

		// Sleep for 1 sec
		ourLog.info("Patient lastUpdated: " + InstantType.withCurrentTime().getValueAsString());
		Thread.sleep(1000);

		// LastUpdated since now
		Date timeDate = InstantType.now().getValue();
		ourLog.info(timeDate.toString());

		// Group references to Patient/A
		Group group = new Group();
		group.setId("B");
		group.addMember().getEntity().setReference("Patient/A");
		myClient.update().resource(group).execute();

		// Observation references to Patient/A
		Observation observation = new Observation();
		observation.setId("C");
		observation.setSubject(new Reference("Patient/A"));
		myClient.update().resource(observation).execute();

		// Set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Group"));
		options.setGroupId("Group/B");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setSince(timeDate);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		// Should get the sub-resource (Observation) even the patient hasn't been updated after the _since param
		verifyBulkExportResults(options, List.of("Observation/C", "Group/B"), List.of("Patient/A"));
	}

	/**
	* This interceptor was needed so that similar GET and POST export requests return the same jobID
	* The test testBulkExportReuse_withGetAndPost_expectSameJobIds() tests this functionality
	*/
	private class BulkExportReuseInterceptor{
		@Hook(Pointcut.STORAGE_INITIATE_BULK_EXPORT)
		public void initiateBulkExport(RequestDetails theRequestDetails, BulkExportJobParameters theBulkExportOptions){
				if(theRequestDetails.getRequestType().equals(RequestTypeEnum.GET)) {
					theBulkExportOptions.getPatientIds();
				}
		}
	}

	@Test
	public void testBulkExportReuse_withGetAndPost_expectSameJobIds() throws IOException {
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		BulkExportReuseInterceptor newInterceptor = new  BulkExportReuseInterceptor();
		myInterceptorRegistry.registerInterceptor(newInterceptor);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient"));

		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		HttpGet get = new HttpGet(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT + "?_outputFormat=application%2Ffhir%2Bndjson&_type=Patient");
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try(CloseableHttpResponse postResponse = mySender.execute(post)){
			ourLog.info("Response: {}",postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());

			try(CloseableHttpResponse getResponse = mySender.execute(get)){
				ourLog.info("Get Response: {}", getResponse);
				assertEquals(202, getResponse.getStatusLine().getStatusCode());
				assertEquals("Accepted", getResponse.getStatusLine().getReasonPhrase());
				assertEquals(postResponse.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue(), getResponse.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
			}
		}
		myInterceptorRegistry.unregisterInterceptor(newInterceptor);
	}

	@Test
	public void testPatientBulkExportWithReferenceToAuthor_ShouldShowUp() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		// Create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Basic basic = new Basic();
		basic.setAuthor(new Reference("Patient/P1"));
		String basicId = myClient.create().resource(basic).execute().getId().toUnqualifiedVersionless().getValue();

		DocumentReference documentReference = new DocumentReference();
		documentReference.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		documentReference.addAuthor(new Reference("Patient/P1"));
		String docRefId = myClient.create().resource(documentReference).execute().getId().toUnqualifiedVersionless().getValue();

		QuestionnaireResponse questionnaireResponseSub = new QuestionnaireResponse();
		questionnaireResponseSub.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		questionnaireResponseSub.setSubject(new Reference("Patient/P1"));
		String questRespSubId = myClient.create().resource(questionnaireResponseSub).execute().getId().toUnqualifiedVersionless().getValue();

		QuestionnaireResponse questionnaireResponseAuth = new QuestionnaireResponse();
		questionnaireResponseAuth.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		questionnaireResponseAuth.setAuthor(new Reference("Patient/P1"));
		String questRespAuthId = myClient.create().resource(questionnaireResponseAuth).execute().getId().toUnqualifiedVersionless().getValue();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Basic", "DocumentReference", "QuestionnaireResponse"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", basicId, docRefId, questRespAuthId, questRespSubId), Collections.emptyList());
	}

	@Test
	public void testPatientBulkExportWithReferenceToPerformer_ShouldShowUp() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		// Create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		CarePlan carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.COMPLETED);
		CarePlan.CarePlanActivityComponent carePlanActivityComponent = new CarePlan.CarePlanActivityComponent();
		CarePlan.CarePlanActivityDetailComponent carePlanActivityDetailComponent = new CarePlan.CarePlanActivityDetailComponent();
		carePlanActivityDetailComponent.addPerformer(new Reference("Patient/P1"));
		carePlanActivityComponent.setDetail(carePlanActivityDetailComponent);
		carePlan.addActivity(carePlanActivityComponent);
		String carePlanId = myClient.create().resource(carePlan).execute().getId().toUnqualifiedVersionless().getValue();

		MedicationAdministration medicationAdministration = new MedicationAdministration();
		medicationAdministration.setStatus(MedicationAdministration.MedicationAdministrationStatus.COMPLETED);
		MedicationAdministration.MedicationAdministrationPerformerComponent medicationAdministrationPerformerComponent = new MedicationAdministration.MedicationAdministrationPerformerComponent();
		medicationAdministrationPerformerComponent.setActor(new Reference("Patient/P1"));
		medicationAdministration.addPerformer(medicationAdministrationPerformerComponent);
		String medAdminId = myClient.create().resource(medicationAdministration).execute().getId().toUnqualifiedVersionless().getValue();

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setStatus(ServiceRequest.ServiceRequestStatus.COMPLETED);
		serviceRequest.addPerformer(new Reference("Patient/P1"));
		String sevReqId = myClient.create().resource(serviceRequest).execute().getId().toUnqualifiedVersionless().getValue();

		Observation observationSub = new Observation();
		observationSub.setStatus(Observation.ObservationStatus.AMENDED);
		observationSub.setSubject(new Reference("Patient/P1"));
		String obsSubId = myClient.create().resource(observationSub).execute().getId().toUnqualifiedVersionless().getValue();

		Observation observationPer = new Observation();
		observationPer.setStatus(Observation.ObservationStatus.AMENDED);
		observationPer.addPerformer(new Reference("Patient/P1"));
		String obsPerId = myClient.create().resource(observationPer).execute().getId().toUnqualifiedVersionless().getValue();

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "CarePlan", "MedicationAdministration", "ServiceRequest"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		verifyBulkExportResults(options, List.of("Patient/P1", carePlanId, medAdminId, sevReqId, obsSubId, obsPerId), Collections.emptyList());
	}

	@Test
	public void testPatientBulkExportWithResourceNotInCompartment_ShouldShowUp() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		// Create some resources
		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Device device = new Device();
		device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		device.setPatient(new Reference("Patient/P1"));
		String deviceId = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless().getValue();


		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient", "Device"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", deviceId), Collections.emptyList());
	}

	@ParameterizedTest
	@MethodSource("bulkExportOptionsResourceTypes")
	public void testDeviceBulkExportWithPatientPartOfGroup(Set<String> resourceTypesForExport) {
		final Patient patient = new Patient();
		patient.setId("A");
		patient.setActive(true);
		final IIdType patientIdType = myClient.update().resource(patient).execute().getId().toUnqualifiedVersionless();

		final Group group = new Group();
		group.setId("B");
		final Group.GroupMemberComponent groupMemberComponent = new Group.GroupMemberComponent();
		final Reference patientReference = new Reference(patientIdType.getValue());
		groupMemberComponent.setEntity(patientReference);
		group.getMember().add(groupMemberComponent);
		final IIdType groupIdType = myClient.update().resource(group).execute().getId().toUnqualifiedVersionless();

		final Device device = new Device();
		device.setId("C");
		device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		device.setPatient(patientReference);
		final IIdType deviceIdType = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless();

		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(resourceTypesForExport);
		options.setGroupId("Group/B");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		final List<String> expectedContainedIds;
		if (resourceTypesForExport.contains("Device")) {
			expectedContainedIds = List.of(patientIdType.getValue(), groupIdType.getValue(), deviceIdType.getValue());
		} else {
			expectedContainedIds = List.of(patientIdType.getValue(), groupIdType.getValue());
		}

		verifyBulkExportResults(options, expectedContainedIds, Collections.emptyList());
	}

	@Test
	public void testSystemBulkExport() {
		List<String> expectedIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			expectedIds.add(createPatient(withActiveTrue()).getValue());
			expectedIds.add(createObservation(withStatus("final")).getValue());
		}

		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Set.of("Patient", "Observation"));
		options.setFilters(Set.of("Patient?active=true", "Patient?active=false", "Observation?status=final"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		JobInstance finalJobInstance = verifyBulkExportResults(options, expectedIds, List.of());
		assertEquals(40, finalJobInstance.getCombinedRecordsProcessed());
	}

	@Test
	public void testSystemBulkExport_ClientIdModeNone() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);

		List<String> expectedIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			expectedIds.add(createPatient(withActiveTrue()).getValue());
			expectedIds.add(createObservation(withStatus("final")).getValue());
		}

		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Set.of("Patient", "Observation"));
		options.setFilters(Set.of("Patient?active=true", "Patient?active=false", "Observation?status=final"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		JobInstance finalJobInstance = verifyBulkExportResults(options, expectedIds, List.of());
		assertEquals(40, finalJobInstance.getCombinedRecordsProcessed());
	}



	@Test
	public void testSystemBulkExport_WithBulkExportInclusionInterceptor() {

		class BoysOnlyInterceptor {

			@Hook(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)
			public boolean include(IBaseResource theResource) {
				if (((Patient) theResource).getGender() == Enumerations.AdministrativeGender.FEMALE) {
					return false;
				}
				return true;
			}

		}
		myInterceptorRegistry.registerInterceptor(new BoysOnlyInterceptor());
		try {

			List<String> expectedIds = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				expectedIds.add(createPatient(withActiveTrue(), withGender("male")).getValue());
			}
			for (int i = 0; i < 10; i++) {
				createPatient(withActiveTrue(), withGender("female"));
			}

			final BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Set.of("Patient", "Observation"));
			options.setFilters(Set.of("Patient?active=true", "Patient?active=false", "Observation?status=final"));
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstance finalJobInstance = verifyBulkExportResults(options, expectedIds, List.of());
			assertEquals(10, finalJobInstance.getCombinedRecordsProcessed());

		} finally {
			myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof BoysOnlyInterceptor);
		}
	}

	@Test
	public void testSystemBulkExport_WithSecurityContext() {
		List<String> expectedIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			expectedIds.add(createPatient(withActiveTrue()).getValue());
			expectedIds.add(createObservation(withStatus("final")).getValue());
		}

		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Set.of("Patient", "Observation"));
		options.setFilters(Set.of("Patient?active=true", "Patient?active=false", "Observation?status=final"));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		options.setBinarySecurityContextIdentifierSystem("http://foo");
		options.setBinarySecurityContextIdentifierValue("bar");

		JobInstance finalJobInstance = verifyBulkExportResults(options, expectedIds, List.of());
		BulkExportJobResults results = JsonUtil.deserialize(finalJobInstance.getReport(), BulkExportJobResults.class);
		List<String> binaryIds = results.getResourceTypeToBinaryIds().values().stream().flatMap(Collection::stream).toList();
		assertThat(binaryIds).hasSize(2);
		for (String next : binaryIds) {
			Binary binary = myBinaryDao.read(new IdType(next), new SystemRequestDetails());
			assertEquals("http://foo", binary.getSecurityContext().getIdentifier().getSystem());
			assertEquals("bar", binary.getSecurityContext().getIdentifier().getValue());
		}
	}


	private JobInstance verifyBulkExportResults(BulkExportJobParameters theOptions, List<String> theContainedList, List<String> theExcludedList) {
		Batch2JobStartResponse startResponse = startNewJob(theOptions);

		assertNotNull(startResponse);
		assertFalse(startResponse.isUsesCachedResult());

		// Run a scheduled pass to build the export
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId(), 120);

		await()
			.atMost(200, TimeUnit.SECONDS)
			.until(() -> myJobCoordinator.getInstance(startResponse.getInstanceId()).getStatus() == StatusEnum.COMPLETED);

		await()
			.atMost(200, TimeUnit.SECONDS)
			.until(() -> myJobCoordinator.getInstance(startResponse.getInstanceId()).getReport() != null);

		// Iterate over the files
		String report = myJobCoordinator.getInstance(startResponse.getInstanceId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);

		Set<String> foundIds = new HashSet<>();
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			String resourceType = file.getKey();
			List<String> binaryIds = file.getValue();
			for (var nextBinaryId : binaryIds) {
				String nextBinaryIdPart = new IdType(nextBinaryId).getIdPart();
				assertThat(nextBinaryIdPart).matches("[a-zA-Z0-9]{32}");

				Binary binary = myBinaryDao.read(new IdType(nextBinaryId));
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());

				String nextNdJsonFileContent = new String(binary.getContent(), Constants.CHARSET_UTF8);
				try (var iter = new LineIterator(new StringReader(nextNdJsonFileContent))) {
					AtomicBoolean gate = new AtomicBoolean(false);
					iter.forEachRemaining(t -> {
						if (isNotBlank(t)) {
							IBaseResource next = myFhirContext.newJsonParser().parseResource(t);
							IIdType nextId = next.getIdElement().toUnqualifiedVersionless();
							if (!resourceType.equals(nextId.getResourceType())) {
								fail("Found resource of type " + nextId.getResourceType() + " in file for type " + resourceType);
							} else {
								if (!foundIds.add(nextId.getValue())) {
									fail("Found duplicate ID: " + nextId.getValue());
								}
							}
						}
						gate.set(true);
					});
					await().atMost(400, TimeUnit.MILLISECONDS)
						.until(gate::get);
				} catch (IOException e) {
					fail(e.toString());
				}
			}
		}

		for (String containedString : theContainedList) {
			assertThat(foundIds).as("Didn't find expected ID " + containedString + " in IDS: " + foundIds).contains(containedString);
		}
		for (String excludedString : theExcludedList) {
			assertThat(foundIds).as("Didn't want unexpected ID " + excludedString + " in IDS: " + foundIds).doesNotContain(excludedString);
		}
		return jobInstance;
	}


	@Test
	public void testValidateParameters_InvalidPostFetch_NoParams() {
		// Setup
		final BulkExportJobParameters options = createOptionsWithPostFetchFilterUrl("foo");

		// Test
		try {
			startNewJob(options);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Invalid post-fetch filter URL, must be in the format [resourceType]?[parameters]: foo");

		}
	}

	@Test
	public void testValidateParameters_InvalidPostFetch_NoParamsAfterQuestionMark() {
		// Setup
		final BulkExportJobParameters options = createOptionsWithPostFetchFilterUrl("Patient?");

		// Test
		try {
			startNewJob(options);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Invalid post-fetch filter URL, must be in the format [resourceType]?[parameters]: Patient?");

		}
	}

	@Test
	public void testValidateParameters_InvalidPostFetch_InvalidResourceType() {
		// Setup
		final BulkExportJobParameters options = createOptionsWithPostFetchFilterUrl("Foo?active=true");

		// Test
		try {
			startNewJob(options);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Invalid post-fetch filter URL, unknown resource type: Foo");

		}
	}

	@Test
	public void testValidateParameters_InvalidPostFetch_UnsupportedParam() {
		// Setup
		final BulkExportJobParameters options = createOptionsWithPostFetchFilterUrl("Observation?subject.identifier=blah");

		// Test
		try {
			startNewJob(options);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Chained parameters are not supported");

		}
	}

	@Test
	public void testValidateParameters_InvalidPostFetch_UnknownParam() {
		// Setup
		final BulkExportJobParameters options = createOptionsWithPostFetchFilterUrl("Observation?foo=blah");

		// Test
		try {
			startNewJob(options);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Invalid post-fetch filter URL.");
			assertThat(e.getMessage()).contains("Resource type Observation does not have a parameter with name: foo");

		}
	}

	private Batch2JobStartResponse startNewJob(BulkExportJobParameters theParameters) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		startRequest.setUseCache(false);
		startRequest.setParameters(theParameters);
		return myJobCoordinator.startInstance(mySrd, startRequest);
	}

	@Nonnull
	private static BulkExportJobParameters createOptionsWithPostFetchFilterUrl(String postFetchUrl) {
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Set.of("Patient"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		options.setPostFetchFilterUrls(Set.of(postFetchUrl));
		return options;
	}


	private static Stream<Set<String>> bulkExportOptionsResourceTypes() {
		return Stream.of(Set.of("Patient", "Group"), Set.of("Patient", "Group", "Device"));
	}

}
