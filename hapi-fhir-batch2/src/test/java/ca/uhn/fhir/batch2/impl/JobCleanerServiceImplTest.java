package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.rest.server.interceptor.ResponseSizeCapturingInterceptor;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.DateTimeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Date;

import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.INSTANCE_ID;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.STEP_1;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.STEP_2;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.STEP_3;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createInstance;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createWorkChunk;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobCleanerServiceImplTest {

	@Mock
	private ISchedulerService mySchedulerService;
	@Mock
	private IJobPersistence myJobPersistence;
	private JobCleanerServiceImpl mySvc;
	@Captor
	private ArgumentCaptor<JobInstance> myInstanceCaptor;

	@BeforeEach
	public void beforeEach() {
		mySvc = new JobCleanerServiceImpl(mySchedulerService, myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_FirstStepNotYetComplete() {
		when(myJobPersistence.fetchInstances(any(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), any(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunk().setTargetStepId(STEP_1).setStatus(StatusEnum.COMPLETED)
		));

		mySvc.runCleanupPass();

		verify(myJobPersistence, never()).updateInstance(any());
	}

	@Test
	public void testInProgress_CalculateProgress_FirstStepComplete() {
		when(myJobPersistence.fetchInstances(any(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), any(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunk().setTargetStepId(STEP_1).setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			createWorkChunk().setTargetStepId(STEP_2).setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			createWorkChunk().setTargetStepId(STEP_2).setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")),
			createWorkChunk().setTargetStepId(STEP_2).setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")),
			createWorkChunk().setTargetStepId(STEP_2).setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunk().setTargetStepId(STEP_3).setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		));

		mySvc.runCleanupPass();

		verify(myJobPersistence, times(1)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getValue();

		assertEquals(-0.1, instance.getProgress());
		assertEquals(1, instance.getCombinedRecordsProcessed());
		assertEquals(1, instance.getCombinedRecordsProcessedPerSecond());
	}

	private Date parseTime(String theDate) {
		return new DateTimeType(theDate).getValue();
	}

}
