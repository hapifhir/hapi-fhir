package ca.uhn.fhir.jpa.bulk;

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
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
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
	private IBatch2JobRunner myJobRunner;

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

		verifyBulkExportResults("G", Sets.newHashSet("Patient?gender=female"), Collections.singletonList("\"PF\""), Collections.singletonList("\"PM\""));
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

		verifyBulkExportResults("G2", new HashSet<>(), List.of("\"PING1\"", "\"PING2\""), Collections.singletonList("\"PNING3\""));
	}

	@Test
	public void testTwoBulkExportsInArow() {
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
		myCaptureQueriesListener.clear();
		verifyBulkExportResults("G2", new HashSet<>(), List.of("\"PING1\""), Collections.singletonList("\"PNING3\""));
		myCaptureQueriesListener.logSelectQueries();
		ourLog.error("************");
		myCaptureQueriesListener.clear();
		try {
			verifyBulkExportResults("G2", new HashSet<>(), List.of("\"PING1\""), Collections.singletonList("\"PNING3\""));
		} finally {
			myCaptureQueriesListener.logSelectQueries();

		}
	}


	private void verifyBulkExportResults(String theGroupId, HashSet<String> theFilters, List<String> theContainedList, List<String> theExcludedList) {
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId(new IdType("Group", theGroupId));
		options.setFilters(theFilters);
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(options));

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getJobId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getJobId()).getReport() != null);

		// Iterate over the files
		String report = myJobRunner.getJobInfo(startResponse.getJobId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			List<String> binaryIds = file.getValue();
			assertEquals(1, binaryIds.size());
			for (String binaryId : binaryIds) {
				Binary binary = myBinaryDao.read(new IdType(binaryId));
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String contents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Next contents for type {} :\n{}", binary.getResourceType(), contents);
				for (String containedString : theContainedList) {
					assertThat(contents, Matchers.containsString(containedString));

				}
				for (String excludedString : theExcludedList) {
					assertThat(contents, not(Matchers.containsString(excludedString)));
				}
			}
		}
	}

}
