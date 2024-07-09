package ca.uhn.fhir.batch2.jobs.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchResourceIdsStepTest {
	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(FetchResourceIdsStep.class);

	@Mock
	private ListAppender<ILoggingEvent> myAppender;

	@Mock
	private IBulkExportProcessor<JpaPid> myBulkExportProcessor;

	@InjectMocks
	private FetchResourceIdsStep myFirstStep;
	@Mock
	private JpaStorageSettings myStorageSettings;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
		myFirstStep.setBulkExportProcessorForUnitTest(myBulkExportProcessor);
	}

	@AfterEach
	public void after() {
		ourLog.detachAppender(myAppender);
	}

	private BulkExportJobParameters createParameters(boolean thePartitioned) {
		BulkExportJobParameters jobParameters = new BulkExportJobParameters();
		jobParameters.setSince(new Date());
		jobParameters.setOutputFormat("json");
		jobParameters.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		jobParameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		if (thePartitioned) {
			jobParameters.setPartitionId(RequestPartitionId.fromPartitionName("Partition-A"));
		} else {
			jobParameters.setPartitionId(RequestPartitionId.allPartitions());
		}
		return jobParameters;
	}

	private StepExecutionDetails<BulkExportJobParameters, VoidModel> createInput(BulkExportJobParameters theParameters,
																										  JobInstance theInstance) {
		return new StepExecutionDetails<>(
			theParameters,
			null,
			theInstance,
			new WorkChunk().setId("1")
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void run_withValidInputs_succeeds(boolean thePartitioned) {
		// setup
		IJobDataSink<ResourceIdList> sink = mock(IJobDataSink.class);
		BulkExportJobParameters parameters = createParameters(thePartitioned);
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		StepExecutionDetails<BulkExportJobParameters, VoidModel> input = createInput(parameters, instance);
		ourLog.setLevel(Level.INFO);
		List<JpaPid> patientIds = new ArrayList<>();
		List<JpaPid> observationIds = new ArrayList<>();

		{
			JpaPid id1 = JpaPid.fromId(123L);
			JpaPid id2 = JpaPid.fromId(234L);
			patientIds.add(id1);
			patientIds.add(id2);
		}
		{
			JpaPid id1 = JpaPid.fromId(345L);
			JpaPid id2 = JpaPid.fromId(456L);
			observationIds.add(id1);
			observationIds.add(id2);
		}

		// when
		when(myBulkExportProcessor.getResourcePidIterator(
			any(ExportPIDIteratorParameters.class)
		)).thenReturn(patientIds.iterator())
			.thenReturn(observationIds.iterator());
		int maxFileCapacity = 1000;
		when(myStorageSettings.getBulkExportFileMaximumCapacity()).thenReturn(maxFileCapacity);
		when(myStorageSettings.getBulkExportFileMaximumSize()).thenReturn(10000L);

		// test
		RunOutcome outcome = myFirstStep.run(input, sink);

		// verify
		assertEquals(RunOutcome.SUCCESS, outcome);
		ArgumentCaptor<ResourceIdList> resultCaptor = ArgumentCaptor.forClass(ResourceIdList.class);
		verify(sink, times(parameters.getResourceTypes().size()))
			.accept(resultCaptor.capture());

		List<ResourceIdList> results = resultCaptor.getAllValues();
		assertThat(results).hasSize(parameters.getResourceTypes().size());
		for (ResourceIdList idList: results) {
			String resourceType = idList.getResourceType();
			assertThat(parameters.getResourceTypes()).contains(resourceType);

			if (resourceType.equals("Patient")) {
				assertThat(idList.getIds()).hasSize(patientIds.size());
			}
			else if (resourceType.equals("Observation")) {
				assertThat(idList.getIds()).hasSize(observationIds.size());
			}
			else {
				// we shouldn't have others
				fail("");
			}
		}

		ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender, atLeastOnce()).doAppend(logCaptor.capture());
		List<ILoggingEvent> events = logCaptor.getAllValues();
		assertThat(events.get(0).getMessage()).contains("Fetching resource IDs for bulk export job instance");
		assertThat(events.get(1).getMessage()).contains("Running FetchResource");
		assertThat(events.get(2).getMessage()).contains("Running FetchResource");
		assertThat(events.get(3).getFormattedMessage()).contains("Submitted "
			+ parameters.getResourceTypes().size()
			+ " groups of ids for processing");

		ArgumentCaptor<ExportPIDIteratorParameters> mapppedParamsCaptor = ArgumentCaptor.forClass(ExportPIDIteratorParameters.class);
		verify(myBulkExportProcessor, times(2)).getResourcePidIterator(mapppedParamsCaptor.capture());
		List<ExportPIDIteratorParameters> capturedParameters = mapppedParamsCaptor.getAllValues();
		assertEquals(parameters.getPartitionId(), capturedParameters.get(0).getPartitionIdOrAllPartitions());
		assertEquals(parameters.getPartitionId(), capturedParameters.get(1).getPartitionIdOrAllPartitions());
	}

	@Test
	public void run_moreThanTheMaxFileCapacityPatients_hasAtLeastTwoJobs() {
		// setup
		IJobDataSink<ResourceIdList> sink = mock(IJobDataSink.class);
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		BulkExportJobParameters parameters = createParameters(false);
		parameters.setResourceTypes(Collections.singletonList("Patient"));
		StepExecutionDetails<BulkExportJobParameters, VoidModel> input = createInput(parameters, instance);
		ourLog.setLevel(Level.INFO);
		List<JpaPid> patientIds = new ArrayList<>();

		// when
		int maxFileCapacity = 5;
		when(myStorageSettings.getBulkExportFileMaximumCapacity()).thenReturn(maxFileCapacity);
		when(myStorageSettings.getBulkExportFileMaximumSize()).thenReturn(10000L);

		for (int i = 0; i <= maxFileCapacity; i++) {
			JpaPid id = JpaPid.fromId((long) i);
			patientIds.add(id);
		}

		// when
		when(myBulkExportProcessor.getResourcePidIterator(
			any(ExportPIDIteratorParameters.class)
		)).thenReturn(patientIds.iterator());

		// test
		RunOutcome outcome = myFirstStep.run(input, sink);

		// verify
		ArgumentCaptor<ResourceIdList> captor = ArgumentCaptor.forClass(ResourceIdList.class);
		assertEquals(RunOutcome.SUCCESS, outcome);

		verify(sink, times(2))
			.accept(captor.capture());
		List<ResourceIdList> listIds = captor.getAllValues();

		// verify all submitted ids are there
		boolean found = false;
		for (JpaPid pid : patientIds) {
			BatchResourceId batchResourceId = BatchResourceId.getIdFromPID(pid, "Patient");
			for (ResourceIdList idList : listIds) {
				found = idList.getIds().contains(batchResourceId);
				if (found) {
					break;
				}
			}
			assertTrue(found);
			found = false;
		}
	}
}
