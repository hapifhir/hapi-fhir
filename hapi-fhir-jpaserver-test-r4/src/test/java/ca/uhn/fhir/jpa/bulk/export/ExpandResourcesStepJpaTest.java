package ca.uhn.fhir.jpa.bulk.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourcesStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExpandResourcesStepJpaTest extends BaseJpaR4Test {

	@Autowired
	private ExpandResourcesStep myExpandResourcesStep;

	@Mock
	private IJobDataSink<ExpandedResourcesList> mySink;
	@Captor
	private ArgumentCaptor<ExpandedResourcesList> myWorkChunkCaptor;

	@Override
	public void afterCleanupDao() {
		super.afterCleanupDao();

		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
	}

	/**
	 * Make sure we load inline tags efficiently when generating bulk export
	 */
	@ParameterizedTest
	@CsvSource({"INLINE,2", "NON_VERSIONED,3", "VERSIONED,3"})
	public void testBulkExportExpandResourcesStep(JpaStorageSettings.TagStorageModeEnum theTagStorageMode, int theExpectedSelectQueries) {
		// Setup

		myStorageSettings.setTagStorageMode(theTagStorageMode);

		int count = 10;
		List<Long> ids = IntStream.range(0, count)
			.boxed()
			.map(t -> {
				Patient p = new Patient();
				p.getMeta().addTag().setSystem("http://static").setCode("tag");
				p.getMeta().addTag().setSystem("http://dynamic").setCode("tag" + t);
				return myPatientDao.create(p, mySrd).getId().getIdPartAsLong();
			}).toList();
		assertEquals(count, ids.size());

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(ids.stream().map(t -> new BatchResourceId().setResourceType("Patient").setId(Long.toString(t))).toList());

		BulkExportJobParameters params = new BulkExportJobParameters();
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, chunkId);

		// Test

		myCaptureQueriesListener.clear();
		myExpandResourcesStep.run(details, mySink);

		// Verify

		verify(mySink, times(1)).accept(myWorkChunkCaptor.capture());
		ExpandedResourcesList expandedResourceList = myWorkChunkCaptor.getValue();
		assertEquals(10, expandedResourceList.getStringifiedResources().size());
		assertThat(expandedResourceList.getStringifiedResources().get(0), containsString("{\"system\":\"http://static\",\"code\":\"tag\"}"));
		assertThat(expandedResourceList.getStringifiedResources().get(0), containsString("{\"system\":\"http://dynamic\",\"code\":\"tag0\"}"));
		assertThat(expandedResourceList.getStringifiedResources().get(1), containsString("{\"system\":\"http://static\",\"code\":\"tag\"}"));
		assertThat(expandedResourceList.getStringifiedResources().get(1), containsString("{\"system\":\"http://dynamic\",\"code\":\"tag1\"}"));

		// Verify query counts
		assertEquals(theExpectedSelectQueries, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(2, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

	}


}
