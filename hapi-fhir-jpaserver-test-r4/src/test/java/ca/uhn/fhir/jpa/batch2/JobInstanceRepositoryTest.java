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
	private String myParams = "{\"param1\":\"value1\"}";
	private String myJobDefinitionId = "my-job-def-id";
	private String myInstanceId = "abc-123";

	@Test
	public void testSearchByJobParamsAndStatuses_SingleStatus() {
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS);
		List<Batch2JobInstanceEntity> instancesByJobIdParamsAndStatus = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(myJobDefinitionId, myParams, statuses, PageRequest.of(0, 10)));
		assertThat(instancesByJobIdParamsAndStatus, hasSize(1));
	}

	@Test
	public void testSearchByJobParamsAndStatuses_MultiStatus() {
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);
		List<Batch2JobInstanceEntity> instances = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(myJobDefinitionId, myParams, statuses, PageRequest.of(0, 10)));
		assertThat(instances, hasSize(2));
	}

	@Test
	public void testSearchByJobParamsWithoutStatuses() {
		List<Batch2JobInstanceEntity> instances = runInTransaction(()->myJobInstanceRepository.findInstancesByJobIdAndParams(myJobDefinitionId, myParams, PageRequest.of(0, 10)));
		assertThat(instances, hasSize(4));
	}

	@Test
	public void testServiceLogicIsCorrectWhenNoStatusesAreUsed() {
		FetchJobInstancesRequest request = new FetchJobInstancesRequest(myJobDefinitionId, myParams);
		List<JobInstance> jobInstances = myJobPersistenceSvc.fetchInstances(request, 0, 1000);
		assertThat(jobInstances, hasSize(4));
	}

	@Test
	public void testServiceLogicIsCorrectWithStatuses() {
		//Given
		FetchJobInstancesRequest request = new FetchJobInstancesRequest(myJobDefinitionId, myParams, StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);

		//When
		List<JobInstance> jobInstances = myJobPersistenceSvc.fetchInstances(request, 0, 1000);

		//Then
		assertThat(jobInstances, hasSize(2));
	}

	@BeforeEach
	private void beforeEach() {
		//Create in-progress job.
		Batch2JobInstanceEntity instance= new Batch2JobInstanceEntity();
		instance.setId(myInstanceId);
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setCreateTime(new Date());
		instance.setDefinitionId(myJobDefinitionId);
		instance.setParams(myParams);
		myJobInstanceRepository.save(instance);

		Batch2JobInstanceEntity completedInstance = new Batch2JobInstanceEntity();
		completedInstance.setId(myInstanceId + "-2");
		completedInstance.setStatus(StatusEnum.COMPLETED);
		completedInstance.setCreateTime(new Date());
		completedInstance.setDefinitionId(myJobDefinitionId);
		completedInstance.setParams(myParams);
		myJobInstanceRepository.save(completedInstance);

		Batch2JobInstanceEntity cancelledInstance = new Batch2JobInstanceEntity();
		cancelledInstance.setId(myInstanceId + "-3");
		cancelledInstance.setStatus(StatusEnum.CANCELLED);
		cancelledInstance.setCreateTime(new Date());
		cancelledInstance.setDefinitionId(myJobDefinitionId);
		cancelledInstance.setParams(myParams);
		myJobInstanceRepository.save(cancelledInstance);

		Batch2JobInstanceEntity failedInstance = new Batch2JobInstanceEntity();
		failedInstance.setId(myInstanceId + "-4");
		failedInstance.setStatus(StatusEnum.FAILED);
		failedInstance.setCreateTime(new Date());
		failedInstance.setDefinitionId(myJobDefinitionId);
		failedInstance.setParams(myParams);
		myJobInstanceRepository.save(failedInstance);
	}

	@AfterEach
	public void afterEach() {
		myJobInstanceRepository.deleteAll();
	}

}
