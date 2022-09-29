package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.BulkExportUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.LineIterator;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class BulkDataExportTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportTest.class);

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private IBatch2JobRunner myJobRunner;

	@AfterEach
	void afterEach() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
	}

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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId(new IdType("Group", "G"));
		options.setFilters(Sets.newHashSet("Patient?gender=female"));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, Collections.singletonList("Patient/PF"), Collections.singletonList("Patient/PM"));
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId(new IdType("Group", "G2"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId(new IdType("Group", "G2"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
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
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientIds(Sets.newHashSet(new IdType("Patient", "P1")));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		verifyBulkExportResults(options, List.of("Patient/P1", obsId, encId), List.of("Patient/P2", obsId2, encId2, obsId3));
	}

	@Test
	public void testPatientBulkExportWithMultiIds() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientIds(Sets.newHashSet(new IdType("Patient", "P1"), new IdType("Patient", "P2")));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Encounter"));
		options.setGroupId(new IdType("Group", "G1"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", practId, orgId, encId, encId2, locId), List.of("Patient/P2", orgId2, encId3, locId2));
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Encounter", "Observation"));
		options.setGroupId(new IdType("Group", "G1"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Provenance"));
		options.setGroupId(new IdType("Group", "G1"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);
		verifyBulkExportResults(options, List.of("Patient/P1", obsId, provId, devId, devId2), List.of("Patient/P2", provId2, devId3));
	}

	private void verifyBulkExportResults(BulkDataExportOptions theOptions, List<String> theContainedList, List<String> theExcludedList) {
		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(theOptions));

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getJobId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getJobId()).getReport() != null);

		// Iterate over the files
		String report = myJobRunner.getJobInfo(startResponse.getJobId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);

		Set<String> foundIds = new HashSet<>();
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			String resourceType = file.getKey();
			List<String> binaryIds = file.getValue();
			for (var nextBinaryId : binaryIds) {

				Binary binary = myBinaryDao.read(new IdType(nextBinaryId));
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());

				String nextNdJsonFileContent = new String(binary.getContent(), Constants.CHARSET_UTF8);
				try (var iter = new LineIterator(new StringReader(nextNdJsonFileContent))) {
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
					});
				} catch (IOException e) {
					fail(e.toString());
				}

			}
		}

		for (String containedString : theContainedList) {
			assertThat(foundIds, hasItem(containedString));
		}
		for (String excludedString : theExcludedList) {
			assertThat(foundIds, not(hasItem(excludedString)));
		}
	}

}
