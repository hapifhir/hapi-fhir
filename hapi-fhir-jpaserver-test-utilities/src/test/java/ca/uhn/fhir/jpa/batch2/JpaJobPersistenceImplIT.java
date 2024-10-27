package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaJobPersistenceImplIT extends BaseJpaR4Test {

	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	@Autowired
	private IJobPersistence myJobPersistence;

	@Test
	public void onWorkChunkPollDelay_withValidData_updatesDeadlineAndPollAttemptCount() {
		// setup
		Date nextPollTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
		JobInstance jobInstance = new JobInstance();
		jobInstance.setJobDefinitionId("job-def-id");
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		jobInstance.setJobDefinitionVersion(1);
		String instanceId = myJobPersistence.storeNewInstance(jobInstance);

		Batch2WorkChunkEntity workChunk = new Batch2WorkChunkEntity();
		workChunk.setId("id");
		workChunk.setInstanceId(instanceId);
		workChunk.setJobDefinitionId(jobInstance.getJobDefinitionId());
		workChunk.setJobDefinitionVersion(jobInstance.getJobDefinitionVersion());
		workChunk.setStatus(WorkChunkStatusEnum.IN_PROGRESS);
		workChunk.setCreateTime(new Date());
		workChunk.setTargetStepId("step-id");
		myWorkChunkRepository.save(workChunk);

		// test
		myJobPersistence.onWorkChunkPollDelay(workChunk.getId(), nextPollTime);

		// verify
		List<Batch2WorkChunkEntity> allChunks = myWorkChunkRepository.findAll();
		assertEquals(1, allChunks.size());
		Batch2WorkChunkEntity chunk = allChunks.get(0);
		assertEquals(nextPollTime, chunk.getNextPollTime());
		assertEquals(1, chunk.getPollAttempts());
	}
}
