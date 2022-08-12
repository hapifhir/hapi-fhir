package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.config.BaseBatch2Config;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
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

	@Test
	public void testQueryWorks() {

		Batch2JobInstanceEntity instance= new Batch2JobInstanceEntity();
		String instanceId = "abc-123";
		String definitionId = "my-job-def-id";
		String params = "{\"param1\":\"value1\"}";

		instance.setId(instanceId);
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setCreateTime(new Date());
		instance.setDefinitionId(definitionId);
		instance.setParams(params);


		myJobInstanceRepository.save(instance);
		Set<StatusEnum> statuses = Set.of(StatusEnum.IN_PROGRESS);
		List<JobInstance> instancesByJobIdParamsAndStatus = myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(definitionId, params, statuses, PageRequest.of(0, 10));

		assertThat(instancesByJobIdParamsAndStatus, hasSize(1));

	}

}
