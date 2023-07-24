package ca.uhn.fhir.jpa.bulk.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourcesStep;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
				p.getMeta().addTag().setSystem("http://static").setCode("tag").setUserSelected(true).setVersion("1");
				p.getMeta().addTag().setSystem("http://dynamic").setCode("tag" + t).setUserSelected(true).setVersion("1");
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
		assertThat(expandedResourceList.getStringifiedResources().get(0), containsString("{\"system\":\"http://static\",\"version\":\"1\",\"code\":\"tag\",\"userSelected\":true}"));
		assertThat(expandedResourceList.getStringifiedResources().get(1), containsString("{\"system\":\"http://static\",\"version\":\"1\",\"code\":\"tag\",\"userSelected\":true}"));

		// Verify query counts
		assertEquals(theExpectedSelectQueries, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(2, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

	}


	@Test
	public void testPostFetchFiltering() {
		List<Long> matchingIds = new ArrayList<>();
		List<Long> allIds = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			// Create a patient that will match the post-fetch filtering
			Patient matchingPatient = new Patient();
			matchingPatient.getMeta().addTag().setSystem("http://tag-system").setCode("tag-value");
			Long matchingId = myPatientDao.create(matchingPatient, mySrd).getId().getIdPartAsLong();
			matchingIds.add(matchingId);
			allIds.add(matchingId);

			// Create a patient that will not match the post-fetch filtering
			Patient nonMatchingPatient = new Patient();
			nonMatchingPatient.setActive(true);
			nonMatchingPatient.getMeta().addTag().setSystem("http://tag-system").setCode("other-tag-value");
			Long nonMatchingId = myPatientDao.create(nonMatchingPatient, mySrd).getId().getIdPartAsLong();
			allIds.add(nonMatchingId);
		}

		// Setup

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(allIds.stream().map(t -> new BatchResourceId().setResourceType("Patient").setId(Long.toString(t))).toList());

		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setPostFetchFilterUrls(List.of("Patient?_tag=http://tag-system|tag-value"));
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, chunkId);

		// Test

		myCaptureQueriesListener.clear();
		myExpandResourcesStep.run(details, mySink);

		// Verify
		verify(mySink, times(1)).accept(myWorkChunkCaptor.capture());
		List<Long> resourceIds = myWorkChunkCaptor
			.getValue()
			.getStringifiedResources()
			.stream()
			.map(t -> myFhirContext.newJsonParser().parseResource(t).getIdElement().getIdPartAsLong())
			.toList();
		assertThat(resourceIds.toString(), resourceIds, containsInAnyOrder(matchingIds.toArray(new Long[0])));

	}


	@Test
	public void testPostFetchFiltering_NoFiltersForGivenResourceType() {
		List<Long> allIds = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			// Create a patient that will match the post-fetch filtering
			Patient matchingPatient = new Patient();
			matchingPatient.setActive(true);
			Long matchingId = myPatientDao.create(matchingPatient, mySrd).getId().getIdPartAsLong();
			allIds.add(matchingId);
		}

		// Setup

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(allIds.stream().map(t -> new BatchResourceId().setResourceType("Patient").setId(Long.toString(t))).toList());

		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setPostFetchFilterUrls(List.of("Observation?status=final"));
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, chunkId);

		// Test

		myCaptureQueriesListener.clear();
		myExpandResourcesStep.run(details, mySink);

		// Verify
		verify(mySink, times(1)).accept(myWorkChunkCaptor.capture());
		List<Long> resourceIds = myWorkChunkCaptor
			.getValue()
			.getStringifiedResources()
			.stream()
			.map(t -> myFhirContext.newJsonParser().parseResource(t).getIdElement().getIdPartAsLong())
			.toList();
		assertThat(resourceIds.toString(), resourceIds, containsInAnyOrder(allIds.toArray(new Long[0])));

	}

}
