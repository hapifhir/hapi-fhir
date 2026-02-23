package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobInstanceRepositoryTest extends BaseJpaR4Test {

	@Autowired
	private IJobPersistence myJobPersistenceSvc;
	private static final String PARAMS = "{\"param1\":\"value1\"}";
	private static final String JOB_DEFINITION_ID = "my-job-def-id";
	private static final String INSTANCE_ID = "abc-123";

	private static final String TRIGGERING_USER_NAME = "triggeringUser";
	private static final String TRIGGERING_CLIENT_ID = "clientId";

	@Test
	public void testSearchByJobParamsAndStatuses_SingleStatus() {
		createInstancesForSomeTests();
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS);
		List<Batch2JobInstanceEntity> instancesByJobIdParamsAndStatus = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(JOB_DEFINITION_ID, PARAMS, statuses, null, PageRequest.of(0, 10)));
		assertThat(instancesByJobIdParamsAndStatus).hasSize(1);
	}

	@Test
	public void testSearchByJobParamsAndStatuses_MultiStatus() {
		createInstancesForSomeTests();
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);
		List<Batch2JobInstanceEntity> instances = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(JOB_DEFINITION_ID, PARAMS, statuses, null, PageRequest.of(0, 10)));
		assertThat(instances).hasSize(2);
	}

	@Test
	public void testSearchByJobParamsWithoutStatuses() {
		createInstancesForSomeTests();
		List<Batch2JobInstanceEntity> instances = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdAndParams(JOB_DEFINITION_ID, PARAMS, PageRequest.of(0, 10)));
		assertThat(instances).hasSize(4);
	}

	@Test
	public void testServiceLogicIsCorrectWhenNoStatusesAreUsed() {
		createInstancesForSomeTests();
		FetchJobInstancesRequest request = new FetchJobInstancesRequest(JOB_DEFINITION_ID, PARAMS);
		List<JobInstance> jobInstances = myJobPersistenceSvc.fetchInstances(request, 0, 1000);
		assertThat(jobInstances).hasSize(4);
	}

	@Test
	public void testServiceLogicIsCorrectWithStatuses() {
		//Given
		createInstancesForSomeTests();
		FetchJobInstancesRequest request = new FetchJobInstancesRequest(JOB_DEFINITION_ID, PARAMS, StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);

		//When
		List<JobInstance> jobInstances = myJobPersistenceSvc.fetchInstances(request, 0, 1000);

		//Then
		assertThat(jobInstances).hasSize(2);
	}

	@Test
	public void testPersistInitiatingUsernameAndClientId() {
		createInstancesForSomeTests();
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS);
		List<Batch2JobInstanceEntity> instancesByJobIdParamsAndStatus = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(JOB_DEFINITION_ID, PARAMS, statuses, null, PageRequest.of(0, 10)));
		assertThat(instancesByJobIdParamsAndStatus).hasSize(1);
		Batch2JobInstanceEntity batch2JobInstanceEntity = instancesByJobIdParamsAndStatus.get(0);
		assertEquals(batch2JobInstanceEntity.getTriggeringUsername(), TRIGGERING_USER_NAME);
		assertEquals(batch2JobInstanceEntity.getTriggeringClientId(), TRIGGERING_CLIENT_ID);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false})
	@NullSource
	public void findInstancesByJobIdParamsAndStatus_withCancelledBoolean_returnsAsExpected(Boolean theCancelledBoolean) {
		// setup
		for (String id : new String[] { "cancelled", "notcancelled" }) {
			Batch2JobInstanceEntity instance = new Batch2JobInstanceEntity();
			instance.setId(id);
			instance.setStatus(StatusEnum.IN_PROGRESS);
			instance.setCreateTime(new Date());
			instance.setDefinitionId(JOB_DEFINITION_ID);
			instance.setParams(PARAMS);
			instance.setTriggeringUsername(TRIGGERING_USER_NAME);
			instance.setTriggeringClientId(TRIGGERING_CLIENT_ID);
			instance.setCancelled(id.equals("cancelled")); // one cancelled, one not
			myJobInstanceRepository.save(instance);
		}

		// test
		List<Batch2JobInstanceEntity> jobInstanceEntities = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(JOB_DEFINITION_ID,
			PARAMS,
			Set.of(StatusEnum.IN_PROGRESS),
			theCancelledBoolean,
			PageRequest.of(0, 10)));

		// validate
		if (theCancelledBoolean == null) {
			// both should be returned
			assertEquals(2, jobInstanceEntities.size());
		} else {
			assertEquals(1, jobInstanceEntities.size());
			Batch2JobInstanceEntity entity = jobInstanceEntities.get(0);
			String expectedId = theCancelledBoolean ? "cancelled" : "notcancelled";
			assertEquals(expectedId, entity.getId());
		}
	}

	private void createInstancesForSomeTests() {
		//Create in-progress job.
		Batch2JobInstanceEntity instance= new Batch2JobInstanceEntity();
		instance.setId(INSTANCE_ID);
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setCreateTime(new Date());
		instance.setDefinitionId(JOB_DEFINITION_ID);
		instance.setParams(PARAMS);
		instance.setTriggeringUsername(TRIGGERING_USER_NAME);
		instance.setTriggeringClientId(TRIGGERING_CLIENT_ID);
		myJobInstanceRepository.save(instance);

		Batch2JobInstanceEntity completedInstance = new Batch2JobInstanceEntity();
		completedInstance.setId(INSTANCE_ID + "-2");
		completedInstance.setStatus(StatusEnum.COMPLETED);
		completedInstance.setCreateTime(new Date());
		completedInstance.setDefinitionId(JOB_DEFINITION_ID);
		completedInstance.setParams(PARAMS);
		myJobInstanceRepository.save(completedInstance);

		Batch2JobInstanceEntity cancelledInstance = new Batch2JobInstanceEntity();
		cancelledInstance.setId(INSTANCE_ID + "-3");
		cancelledInstance.setStatus(StatusEnum.CANCELLED);
		cancelledInstance.setCreateTime(new Date());
		cancelledInstance.setDefinitionId(JOB_DEFINITION_ID);
		cancelledInstance.setParams(PARAMS);
		myJobInstanceRepository.save(cancelledInstance);

		Batch2JobInstanceEntity failedInstance = new Batch2JobInstanceEntity();
		failedInstance.setId(INSTANCE_ID + "-4");
		failedInstance.setStatus(StatusEnum.FAILED);
		failedInstance.setCreateTime(new Date());
		failedInstance.setDefinitionId(JOB_DEFINITION_ID);
		failedInstance.setParams(PARAMS);
		myJobInstanceRepository.save(failedInstance);
	}

	@AfterEach
	public void afterEach() {
		myJobInstanceRepository.deleteAll();
	}

}
