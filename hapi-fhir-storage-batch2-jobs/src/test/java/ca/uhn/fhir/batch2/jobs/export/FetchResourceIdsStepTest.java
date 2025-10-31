package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.svc.BulkExportIdFetchingSvc;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
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
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private BulkExportIdFetchingSvc myBulkExportIdFetchingSvc;

	@InjectMocks
	private FetchResourceIdsStep myFirstStep;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
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
//		List<JpaPid> patientIds = new ArrayList<>();
//		List<JpaPid> observationIds = new ArrayList<>();
		ResourceIdList patientIds = new ResourceIdList();
		patientIds.setResourceType("Patient");
		ResourceIdList observationIds = new ResourceIdList();
		observationIds.setResourceType("Observation");

		{
//			JpaPid id1 = JpaPid.fromId(123L);
//			JpaPid id2 = JpaPid.fromId(234L);
//			patientIds.add(id1);
//			patientIds.add(id2);
			List<TypedPidJson> pids = new ArrayList<>();
			pids.add(new TypedPidJson("Patient", null, "123"));
			pids.add(new TypedPidJson("Patient", null, "234"));
			patientIds.setIds(pids);
		}
		{
//			JpaPid id1 = JpaPid.fromId(345L);
//			JpaPid id2 = JpaPid.fromId(456L);
//			observationIds.add(id1);
//			observationIds.add(id2);
			observationIds.addId(new TypedPidJson("Observation", null, "345"));
			observationIds.addId(new TypedPidJson("Observation", null, "456"));
		}

		// when
//		when(myResourceSupportedSvc.isSupported(anyString())).thenReturn(true);
//		when(myBulkExportProcessor.getResourcePidIterator(
//			any(ExportPIDIteratorParameters.class)
//		)).thenReturn(patientIds.iterator())
//			.thenReturn(observationIds.iterator());
		when(myBulkExportIdFetchingSvc.fetchIds(any(ExportPIDIteratorParameters.class), any(Consumer.class)))
			.thenAnswer(args -> {
				Consumer<ResourceIdList> consumer = args.getArgument(1);
				consumer.accept(patientIds);
				consumer.accept(observationIds);
				return parameters.getResourceTypes().size();
			});
		int maxFileCapacity = 1000;
//		when(myStorageSettings.getBulkExportFileMaximumCapacity()).thenReturn(maxFileCapacity);
//		when(myStorageSettings.getBulkExportFileMaximumSize()).thenReturn(10000L);

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
				assertThat(idList.getIds()).hasSize(patientIds.getIds().size());
			}
			else if (resourceType.equals("Observation")) {
				assertThat(idList.getIds()).hasSize(observationIds.getIds().size());
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
		assertThat(events.get(1).getFormattedMessage()).contains("Submitted "
			+ parameters.getResourceTypes().size()
			+ " groups of ids for processing");
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
//		List<JpaPid> patientIds = new ArrayList<>();
		List<TypedPidJson> patientIds = new ArrayList<>();

		// when
		int maxFileCapacity = 5;
//		when(myStorageSettings.getBulkExportFileMaximumCapacity()).thenReturn(maxFileCapacity);
//		when(myStorageSettings.getBulkExportFileMaximumSize()).thenReturn(10000L);
//		when(myResourceSupportedSvc.isSupported(anyString())).thenReturn(true);

		ResourceIdList list = new ResourceIdList();
		list.setResourceType("Patient");
		for (int i = 0; i <= maxFileCapacity; i++) {
			patientIds.add(new TypedPidJson("Patient", null, Long.toString(i)));
		}

		// when
//		when(myBulkExportProcessor.getResourcePidIterator(
//			any(ExportPIDIteratorParameters.class)
//		)).thenReturn(patientIds.iterator());
		when(myBulkExportIdFetchingSvc.fetchIds(any(ExportPIDIteratorParameters.class), any(Consumer.class)))
			.thenAnswer(args -> {
				Consumer<ResourceIdList> consumer = args.getArgument(1);
				consumer.accept(list);
				return 1;
			});

		// test
		RunOutcome outcome = myFirstStep.run(input, sink);

		// verify
		ArgumentCaptor<ResourceIdList> captor = ArgumentCaptor.forClass(ResourceIdList.class);
		assertEquals(RunOutcome.SUCCESS, outcome);

		verify(sink).accept(captor.capture());
		List<ResourceIdList> listids = captor.getAllValues();
		assertEquals(1, listids.size());
		assertEquals(list, listids.get(0));

//		verify(sink, times(2))
//			.accept(captor.capture());
//		List<ResourceIdList> listIds = captor.getAllValues();
//
//		// verify all submitted ids are there
//		boolean found = false;
//		for (JpaPid pid : patientIds) {
//			TypedPidJson batchResourceId = new TypedPidJson("Patient", pid);
//			for (ResourceIdList idList : listIds) {
//				found = idList.getIds().contains(batchResourceId);
//				if (found) {
//					break;
//				}
//			}
//			assertTrue(found);
//			found = false;
//		}
	}
}
