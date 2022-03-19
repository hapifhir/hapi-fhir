package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReindexJobTest extends BaseJpaR4Test {
	@Autowired
	private IJobCoordinator myJobCoordinator;

	private ReindexTestHelper myReindexTestHelper;

	@PostConstruct
	public void postConstruct() {
		myReindexTestHelper = new ReindexTestHelper(myFhirContext, myDaoRegistry, mySearchParamRegistry);
	}

	@Test
	public void testReindex_ByUrl() {
		// setup

		IIdType obsFinalId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.CANCELLED);

		myReindexTestHelper.createAlleleSearchParameter();

		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The search param value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		// Only reindex one of them
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Observation?status=final");

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		String id = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(id);

		// validate
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		// Now one of them should be indexed
		List<String> alleleObservationIds = myReindexTestHelper.getAlleleObservationIds();
		assertThat(alleleObservationIds, hasSize(1));
		assertEquals(obsFinalId.getIdPart(), alleleObservationIds.get(0));
	}

	@Test
	public void testReindex_Everything() {
		// setup

		for (int i = 0; i < 50; ++i) {
			myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		}

		sleepUntilTimeChanges();

		myReindexTestHelper.createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The search param value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		String id = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(id);

		// validate
		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// Now all of them should be indexed
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(50));
	}

}
