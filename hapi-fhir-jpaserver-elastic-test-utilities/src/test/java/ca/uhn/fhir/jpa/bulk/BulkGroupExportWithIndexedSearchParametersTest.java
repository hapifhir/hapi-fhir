package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Meta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	 TestR4Config.class
	// pick up elastic or lucene engine:
	,TestHSearchAddInConfig.LuceneFilesystem.class
})
public class BulkGroupExportWithIndexedSearchParametersTest extends BaseJpaTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();
	@Autowired private PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("mySystemDaoR4")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@BeforeEach
	void setUp() {
		myStorageSettings.setAdvancedHSearchIndexing(true);
	}

	@BeforeEach
	public void beforeEach() {
		myStorageSettings.setJobFastTrackingEnabled(false);
	}


	@Test
	public void groupBulkExportWithIndexedSearchParametersTest() throws Exception {
		// Create Group and associated resources from json input
		File jsonInputUrl = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "bulk-group-export/bundle-group-upload.json");
		String jsonBundle = Files.readString(Paths.get(jsonInputUrl.toURI()), StandardCharsets.UTF_8);
		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, jsonBundle);
		mySystemDao.transaction(mySrd, inputBundle);

		// set the export options
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Set.of("Patient", "Observation", "Group"));
		options.setGroupId("Group/G1");
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		BulkExportJobResults jobResults = getBulkExportJobResults(options);
		assertThat(jobResults.getResourceTypeToBinaryIds().keySet()).containsExactlyInAnyOrder("Patient", "Observation", "Group");
	}

	private BulkExportJobResults getBulkExportJobResults(BulkExportJobParameters theOptions) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		startRequest.setParameters(theOptions);

		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId());

		await().until(() -> myJobCoordinator.getInstance(startResponse.getInstanceId()).getReport() != null);

		// Iterate over the files
		String report = myJobCoordinator.getInstance(startResponse.getInstanceId()).getReport();
		return  JsonUtil.deserialize(report, BulkExportJobResults.class);
	}


	@Override
	protected FhirContext getFhirContext() { return myCtx; }

	@Override
	protected PlatformTransactionManager getTxManager() { return myTxManager; }


}

