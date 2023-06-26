package ca.uhn.fhir.batch2.jobs.services;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.Batch2JobInfo;
import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2BaseJobParameters;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Batch2JobRunnerImplTest {

	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(IBatch2JobRunner.class);

	@Mock
	private ListAppender<ILoggingEvent> myAppender;

	@Mock
	private IJobCoordinator myJobCoordinator;

	@InjectMocks
	private Batch2JobRunnerImpl myJobRunner;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
	}

	@AfterEach
	public void end() {
		ourLog.detachAppender(myAppender);
	}

	@Test
	public void startJob_invalidJobDefinitionId_logsAndDoesNothing() {
		// setup
		String jobId = "invalid";
		ourLog.setLevel(Level.ERROR);

		// test
		myJobRunner.startNewJob(new SystemRequestDetails(), new Batch2BaseJobParameters(jobId));

		// verify
		ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender).doAppend(captor.capture());
		assertTrue(captor.getValue().getMessage()
			.contains("Invalid JobDefinitionId " + jobId));
		verify(myJobCoordinator, never())
			.startInstance(any(JobInstanceStartRequest.class));
	}

	@Test
	public void startJob_invalidParametersForExport_logsAndDoesNothing() {
		// setup
		ourLog.setLevel(Level.ERROR);

		// test
		myJobRunner.startNewJob(new SystemRequestDetails(), new Batch2BaseJobParameters(Batch2JobDefinitionConstants.BULK_EXPORT));

		// verify
		ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender).doAppend(captor.capture());
		String msg = captor.getValue().getMessage();
		String expectedMsg = "Invalid parameters for " + Batch2JobDefinitionConstants.BULK_EXPORT;
		assertTrue(msg
			.contains(expectedMsg),
			msg + " != " + expectedMsg);
		verify(myJobCoordinator, never())
			.startInstance(any(JobInstanceStartRequest.class));
	}

	@Test
	public void startJob_bulkExport_callsAsExpected() {
		// setup
		BulkExportParameters parameters = new BulkExportParameters(Batch2JobDefinitionConstants.BULK_EXPORT);
		parameters.setResourceTypes(Collections.singletonList("Patient"));

		// when
		String jobInstanceId = "test_job_instance";
		Date end = new Date();
		Date start = new Date(end.getTime()-100);
		JobInstance mockJobInstance = createMockJobInstance(parameters, jobInstanceId, start, end);
		when(myJobCoordinator.getInstance(eq(jobInstanceId))).thenReturn(mockJobInstance);

		// test
		myJobRunner.startNewJob(new SystemRequestDetails(), parameters);

		// verify
		ArgumentCaptor<JobInstanceStartRequest> captor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		verify(myJobCoordinator)
			.startInstance(isNotNull(), captor.capture());
		JobInstanceStartRequest val = captor.getValue();
		// we need to verify something in the parameters
		ourLog.info(val.getParameters());
		assertTrue(val.getParameters().contains("Patient"));
		assertFalse(val.getParameters().contains("allPartitions"));
		assertFalse(val.getParameters().contains("Partition-A"));

		Batch2JobInfo jobInfo = myJobRunner.getJobInfo(jobInstanceId);
		verifyBatch2JobInfo(jobInfo, jobInstanceId, start, end, null);
	}

	private JobInstance createMockJobInstance(BulkExportParameters theParameters, String theJobInstanceId, Date start, Date end) {
		JobInstance mockJobInstance = new JobInstance();
		mockJobInstance.setInstanceId(theJobInstanceId);
		mockJobInstance.setStatus(StatusEnum.COMPLETED);
		mockJobInstance.setCancelled(false);
		mockJobInstance.setStartTime(start);
		mockJobInstance.setEndTime(end);
		mockJobInstance.setReport("test report");
		mockJobInstance.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		mockJobInstance.setParameters(BulkExportJobParameters.createFromExportJobParameters(theParameters));
		return mockJobInstance;
	}

	private void verifyBatch2JobInfo(Batch2JobInfo jobInfo, String theJobId, Date start, Date end, RequestPartitionId partitionId) {
		assertEquals(jobInfo.getJobId(), theJobId );
		assertFalse(jobInfo.isCancelled());
		assertEquals(jobInfo.getStartTime(), start);
		assertEquals(jobInfo.getEndTime(), end);
		assertEquals(jobInfo.getReport(), "test report");
		assertEquals(jobInfo.getStatus(), BulkExportJobStatusEnum.COMPLETE);
		if (partitionId != null) {
			assertEquals(jobInfo.getRequestPartitionId(), partitionId);
		} else {
			assertNull(jobInfo.getRequestPartitionId());
		}
	}

	@Test
	public void startJob_bulkExport_partitioned() {
		// setup
		BulkExportParameters parameters = new BulkExportParameters(Batch2JobDefinitionConstants.BULK_EXPORT);
		parameters.setResourceTypes(Collections.singletonList("Patient"));
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionName("Partition-A");
		parameters.setPartitionId(partitionId);

		// when
		String jobInstanceId = "test_job_instance";
		Date end = new Date();
		Date start = new Date(end.getTime()-100);
		JobInstance mockJobInstance = createMockJobInstance(parameters, jobInstanceId, start, end);
		when(myJobCoordinator.getInstance(eq(jobInstanceId))).thenReturn(mockJobInstance);

		// test
		myJobRunner.startNewJob(new SystemRequestDetails(), parameters);

		// verify
		ArgumentCaptor<JobInstanceStartRequest> captor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		verify(myJobCoordinator)
			.startInstance(isNotNull(), captor.capture());
		JobInstanceStartRequest val = captor.getValue();
		// we need to verify something in the parameters
		ourLog.info(val.getParameters());
		assertTrue(val.getParameters().contains("Patient"));
		assertTrue(val.getParameters().contains("Partition-A"));
		assertTrue(val.getParameters().contains("\"allPartitions\":false"));

		Batch2JobInfo jobInfo = myJobRunner.getJobInfo(jobInstanceId);
		verifyBatch2JobInfo(jobInfo, jobInstanceId, start, end, partitionId);

	}

}
