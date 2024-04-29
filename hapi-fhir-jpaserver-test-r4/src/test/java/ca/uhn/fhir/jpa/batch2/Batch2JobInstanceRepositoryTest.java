package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Batch2JobInstanceRepositoryTest extends BaseJpaR4Test {

	@Autowired
	IBatch2JobInstanceRepository myBatch2JobInstanceRepository;

	@ParameterizedTest
	@CsvSource({
		"QUEUED, FAILED, QUEUED, true, normal transition",
		"IN_PROGRESS, FAILED, QUEUED IN_PROGRESS ERRORED, true, normal transition with multiple prior",
		"IN_PROGRESS, IN_PROGRESS, IN_PROGRESS, true, self transition to same state",
		"QUEUED, QUEUED, QUEUED, true, normal transition",
		"QUEUED, FAILED, IN_PROGRESS, false, blocked transition"
	})
	void updateInstance_toState_fromState_whenAllowed(StatusEnum theCurrentState, StatusEnum theTargetState, String theAllowedPriorStatesString, boolean theExpectedSuccessFlag) {
		Set<StatusEnum> theAllowedPriorStates = Arrays.stream(theAllowedPriorStatesString.trim().split(" +")).map(StatusEnum::valueOf).collect(Collectors.toSet());
	    // given
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		String jobId = UUID.randomUUID().toString();
		entity.setId(jobId);
		entity.setStatus(theCurrentState);
		entity.setCreateTime(new Date());
		entity.setDefinitionId("definition_id");
		myBatch2JobInstanceRepository.save(entity);

		// when
		int changeCount =
			runInTransaction(()->
				myBatch2JobInstanceRepository.updateInstanceStatusIfIn(jobId, theTargetState, theAllowedPriorStates));

		// then
		Batch2JobInstanceEntity readBack = runInTransaction(() ->
			myBatch2JobInstanceRepository.findById(jobId).orElseThrow());
		if (theExpectedSuccessFlag) {
			assertEquals(1, changeCount, "The change happened");
			assertEquals(theTargetState, readBack.getStatus());
		} else {
			assertEquals(0, changeCount, "The change did not happened");
			assertEquals(theCurrentState, readBack.getStatus());
		}

	}


}
