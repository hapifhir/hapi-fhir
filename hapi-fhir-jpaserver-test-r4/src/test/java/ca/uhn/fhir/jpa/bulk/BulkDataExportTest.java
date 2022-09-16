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
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
		verifyBulkExportResults(options, Collections.singletonList("\"PF\""), Collections.singletonList("\"PM\""));
	}

	@Test
	public void testGroupBulkExportNotInGroup_DoeNotShowUp() {
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
		verifyBulkExportResults(options, List.of("\"PING1\"", "\"PING2\""), Collections.singletonList("\"PNING3\""));
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
		verifyBulkExportResults(options, List.of("\"PING1\""), Collections.singletonList("\"PNING3\""));
		myCaptureQueriesListener.logSelectQueries();
		ourLog.error("************");
		myCaptureQueriesListener.clear();
		try {
			verifyBulkExportResults(options, List.of("\"PING1\""), Collections.singletonList("\"PNING3\""));
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
		String obsId = myClient.create().resource(observation).execute().getId().getIdPart();

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P1"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId = myClient.create().resource(encounter).execute().getId().getIdPart();

		// diff patient
		patient = new Patient();
		patient.setId("P2");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P2"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId2 = myClient.create().resource(observation).execute().getId().getIdPart();

		encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P2"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId2 = myClient.create().resource(encounter).execute().getId().getIdPart();

		observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId3 = myClient.create().resource(observation).execute().getId().getIdPart();

		// set the export options
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientId(Sets.newHashSet(new IdType("Patient", "P1")));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		verifyBulkExportResults(options, List.of("\"P1\"", "\"" + obsId + "\"", "\"" + encId + "\""), List.of("\"P2\"", "\"" + obsId2 + "\"", "\"" + encId2 + "\"", "\"" + obsId3 + "\""));
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
		String obsId = myClient.create().resource(observation).execute().getId().getIdPart();

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P1"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId = myClient.create().resource(encounter).execute().getId().getIdPart();

		// diff patient
		patient = new Patient();
		patient.setId("P2");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P2"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId2 = myClient.create().resource(observation).execute().getId().getIdPart();

		encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P2"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId2 = myClient.create().resource(encounter).execute().getId().getIdPart();

		// yet another diff patient
		patient = new Patient();
		patient.setId("P3");
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		observation = new Observation();
		observation.setSubject(new Reference().setReference("Patient/P3"));
		observation.setStatus(Observation.ObservationStatus.PRELIMINARY);
		String obsId3 = myClient.create().resource(observation).execute().getId().getIdPart();

		encounter = new Encounter();
		encounter.setSubject(new Reference().setReference("Patient/P3"));
		encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
		String encId3 = myClient.create().resource(encounter).execute().getId().getIdPart();

		// set the export options
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation", "Encounter"));
		options.setPatientId(Sets.newHashSet(new IdType("Patient", "P1"), new IdType("Patient", "P2")));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		verifyBulkExportResults(options, List.of("\"P1\"", "\"" + obsId + "\"", "\"" + encId + "\"", "\"P2\"", "\"" + obsId2 + "\"", "\"" + encId2 + "\""), List.of("\"P3\"", "\"" + obsId3 + "\"", "\"" + encId3 + "\""));
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
		String contents = "";
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			List<String> binaryIds = file.getValue();
			assertEquals(1, binaryIds.size());
			Binary binary = myBinaryDao.read(new IdType(binaryIds.get(0)));
			assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
			contents += new String(binary.getContent(), Constants.CHARSET_UTF8) + "\n";
			ourLog.info("Next contents for type {} :\n{}", binary.getResourceType(), new String(binary.getContent(), Constants.CHARSET_UTF8));
		}

		for (String containedString : theContainedList) {
			assertThat(contents, Matchers.containsString(containedString));
		}
		for (String excludedString : theExcludedList) {
			assertThat(contents, not(Matchers.containsString(excludedString)));
		}
	}

}
