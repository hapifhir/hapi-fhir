package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.BulkExportUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes= TestR4ConfigWithElasticHSearch.class)
public class BulkGroupExportWithIndexedSearchParametersTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkGroupExportWithIndexedSearchParametersTest.class);

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private IBatch2JobRunner myJobRunner;

	@BeforeEach
	void setUp() {
		myDaoConfig.setAdvancedHSearchIndexing(true);
	}


	@Test
	public void groupBulkExportWithIndexedSearchParametersTest() {
		// Create some resources
		createPatient(withId("P1"), withFamily("Hello"));
		createEncounter(withId("E"), withIdentifier("http://foo", "bar"));
		createObservation(withId("O"), withSubject("Patient/P1"), withEncounter("Encounter/E"));

		Group group = new Group();
		group.setId("G");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/P1");
		myClient.update().resource(group).execute();

		// set the export options
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Set.of("Patient", "Observation"));
		options.setGroupId(new IdType("Group", "G"));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		BulkExportJobResults jobResults = getBulkExportJobResults(options);
		assertThat(jobResults.getResourceTypeToBinaryIds().keySet(), containsInAnyOrder("Patient", "Encounter", "Observation"));
	}



	private BulkExportJobResults getBulkExportJobResults(BulkDataExportOptions theOptions) {
		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(theOptions));

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getJobId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getJobId()).getReport() != null);

		// Iterate over the files
		String report = myJobRunner.getJobInfo(startResponse.getJobId()).getReport();
		return  JsonUtil.deserialize(report, BulkExportJobResults.class);
	}

}
