package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class JobInstanceRepositoryTest extends BaseJpaR4Test {

	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	private IJobPersistence myJobPersistenceSvc;
	private static final String PARAMS = "{\"param1\":\"value1\"}";
	private static final String JOB_DEFINITION_ID = "my-job-def-id";
	private static final String INSTANCE_ID = "abc-123";

	@Test
	public void testSearchByJobParamsAndStatuses_SingleStatus() {
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS);
		List<Batch2JobInstanceEntity> instancesByJobIdParamsAndStatus = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(JOB_DEFINITION_ID, PARAMS, statuses, PageRequest.of(0, 10)));
		assertThat(instancesByJobIdParamsAndStatus, hasSize(1));
	}

	@Test
	public void testSearchByJobParamsAndStatuses_MultiStatus() {
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);
		List<Batch2JobInstanceEntity> instances = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(JOB_DEFINITION_ID, PARAMS, statuses, PageRequest.of(0, 10)));
		assertThat(instances, hasSize(2));
	}

	@Test
	public void testSearchByJobParamsWithoutStatuses() {
		List<Batch2JobInstanceEntity> instances = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdAndParams(JOB_DEFINITION_ID, PARAMS, PageRequest.of(0, 10)));
		assertThat(instances, hasSize(4));
	}

	@Test
	public void testServiceLogicIsCorrectWhenNoStatusesAreUsed() {
		FetchJobInstancesRequest request = new FetchJobInstancesRequest(JOB_DEFINITION_ID, PARAMS);
		List<JobInstance> jobInstances = myJobPersistenceSvc.fetchInstances(request, 0, 1000);
		assertThat(jobInstances, hasSize(4));
	}

	@Test
	public void testServiceLogicIsCorrectWithStatuses() {
		//Given
		FetchJobInstancesRequest request = new FetchJobInstancesRequest(JOB_DEFINITION_ID, PARAMS, StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);

		//When
		List<JobInstance> jobInstances = myJobPersistenceSvc.fetchInstances(request, 0, 1000);

		//Then
		assertThat(jobInstances, hasSize(2));
	}

	@BeforeEach
	public void beforeEach() {
		//Create in-progress job.
		Batch2JobInstanceEntity instance= new Batch2JobInstanceEntity();
		instance.setId(INSTANCE_ID);
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setCreateTime(new Date());
		instance.setDefinitionId(JOB_DEFINITION_ID);
		instance.setParams(PARAMS);
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
