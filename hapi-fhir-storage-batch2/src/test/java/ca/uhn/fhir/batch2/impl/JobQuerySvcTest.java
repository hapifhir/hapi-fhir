package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class JobQuerySvcTest extends BaseBatch2Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JobQuerySvcTest.class);

	@Mock
	IJobPersistence myJobPersistence;
	@Mock
	JobDefinitionRegistry myJobDefinitionRegistry;
	JobQuerySvc mySvc;

	@BeforeEach
	public void beforeEach() {
		mySvc = new JobQuerySvc(myJobPersistence, myJobDefinitionRegistry);
	}

	@Test
	public void testFetchInstance_PasswordsRedacted() {

		// Setup

		JobDefinition<?> definition = createJobDefinition();
		JobInstance instance = createInstance();

		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(definition);
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));

		// Execute

		JobInstance outcome = mySvc.fetchInstance(INSTANCE_ID);
		ourLog.info("Job instance: {}", outcome);
		ourLog.info("Parameters: {}", outcome.getParameters());
		assertEquals(PARAM_1_VALUE, outcome.getParameters(TestJobParameters.class).getParam1());
		assertEquals(PARAM_2_VALUE, outcome.getParameters(TestJobParameters.class).getParam2());
		assertEquals(null, outcome.getParameters(TestJobParameters.class).getPassword());

	}

	@Test
	public void testFetchInstances() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstances(eq(100), eq(0))).thenReturn(Lists.newArrayList(createInstance()));

		// Execute

		List<JobInstance> outcome = mySvc.fetchInstances(100, 0);

		// Verify

		assertEquals(1, outcome.size());

	}

}
