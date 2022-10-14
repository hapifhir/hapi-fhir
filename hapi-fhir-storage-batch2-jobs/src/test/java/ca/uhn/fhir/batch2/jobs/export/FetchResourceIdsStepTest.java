package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	private IBulkExportProcessor myBulkExportProcessor;

	@InjectMocks
	private FetchResourceIdsStep myFirstStep;
	@Mock
	private DaoConfig myDaoConfig;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
	}

	@AfterEach
	public void after() {
		ourLog.detachAppender(myAppender);
	}

	private BulkExportJobParameters createParameters() {
		BulkExportJobParameters jobParameters = new BulkExportJobParameters();
		jobParameters.setStartDate(new Date());
		jobParameters.setOutputFormat("json");
		jobParameters.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
		jobParameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		return jobParameters;
	}

	private StepExecutionDetails<BulkExportJobParameters, VoidModel> createInput(BulkExportJobParameters theParameters,
																										  JobInstance theInstance) {
		StepExecutionDetails<BulkExportJobParameters, VoidModel> input = new StepExecutionDetails<>(
			theParameters,
			null,
			theInstance,
			"1"
		);
		return input;
	}

	@Test
	public void run_withValidInputs_succeeds() {
		// setup
		IJobDataSink<BulkExportIdList> sink = mock(IJobDataSink.class);
		BulkExportJobParameters parameters = createParameters();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		StepExecutionDetails<BulkExportJobParameters, VoidModel> input = createInput(parameters, instance);
		ourLog.setLevel(Level.INFO);
		List<ResourcePersistentId> patientIds = new ArrayList<>();
		List<ResourcePersistentId> observationIds = new ArrayList<>();

		{
			ResourcePersistentId id1 = new ResourcePersistentId("Patient/123");
			ResourcePersistentId id2 = new ResourcePersistentId("Patient/234");
			patientIds.add(id1);
			patientIds.add(id2);
		}
		{
			ResourcePersistentId id1 = new ResourcePersistentId("Observation/123");
			ResourcePersistentId id2 = new ResourcePersistentId("Observation/234");
			observationIds.add(id1);
			observationIds.add(id2);
		}

		// when
		when(myBulkExportProcessor.getResourcePidIterator(
			any(ExportPIDIteratorParameters.class)
		)).thenReturn(patientIds.iterator())
			.thenReturn(observationIds.iterator());
		int maxFileCapacity = 1000;
		when(myDaoConfig.getBulkExportFileMaximumCapacity()).thenReturn(maxFileCapacity);

		// test
		RunOutcome outcome = myFirstStep.run(input, sink);

		// verify
		assertEquals(RunOutcome.SUCCESS, outcome);
		ArgumentCaptor<BulkExportIdList> resultCaptor = ArgumentCaptor.forClass(BulkExportIdList.class);
		verify(sink, times(parameters.getResourceTypes().size()))
			.accept(resultCaptor.capture());

		List<BulkExportIdList> results = resultCaptor.getAllValues();
		assertEquals(parameters.getResourceTypes().size(), results.size());
		for (int i = 0; i < results.size(); i++) {
			BulkExportIdList idList = results.get(i);

			String resourceType = idList.getResourceType();
			assertTrue(parameters.getResourceTypes().contains(resourceType));

			if (resourceType.equals("Patient")) {
				assertEquals(patientIds.size(), idList.getIds().size());
			}
			else if (resourceType.equals("Observation")) {
				assertEquals(observationIds.size(), idList.getIds().size());
			}
			else {
				// we shouldn't have others
				fail();
			}
		}

		ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender, atLeastOnce()).doAppend(logCaptor.capture());
		List<ILoggingEvent> events = logCaptor.getAllValues();
		assertTrue(events.get(0).getMessage().contains("Starting BatchExport job"));
		assertTrue(events.get(1).getMessage().contains("Running FetchResource"));
		assertTrue(events.get(2).getMessage().contains("Running FetchResource"));
		assertTrue(events.get(3).getFormattedMessage().contains("Submitted "
			+ parameters.getResourceTypes().size()
			+ " groups of ids for processing"
		));
	}

	@Test
	public void run_moreThanTheMaxFileCapacityPatients_hasAtLeastTwoJobs() {
		// setup
		IJobDataSink<BulkExportIdList> sink = mock(IJobDataSink.class);
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		BulkExportJobParameters parameters = createParameters();
		parameters.setResourceTypes(Collections.singletonList("Patient"));
		StepExecutionDetails<BulkExportJobParameters, VoidModel> input = createInput(parameters, instance);
		ourLog.setLevel(Level.INFO);
		List<ResourcePersistentId> patientIds = new ArrayList<>();

		// when
		int maxFileCapacity = 5;
		when(myDaoConfig.getBulkExportFileMaximumCapacity()).thenReturn(maxFileCapacity);

		for (int i = 0; i <= maxFileCapacity; i++) {
			ResourcePersistentId id = new ResourcePersistentId("Patient/RED" + i);
			patientIds.add(id);
		}

		// when
		when(myBulkExportProcessor.getResourcePidIterator(
			any(ExportPIDIteratorParameters.class)
		)).thenReturn(patientIds.iterator());

		// test
		RunOutcome outcome = myFirstStep.run(input, sink);

		// verify
		ArgumentCaptor<BulkExportIdList> captor = ArgumentCaptor.forClass(BulkExportIdList.class);
		assertEquals(RunOutcome.SUCCESS, outcome);

		verify(sink, times(2))
			.accept(captor.capture());
		List<BulkExportIdList> listIds = captor.getAllValues();

		// verify all submitted ids are there
		boolean found = false;
		for (ResourcePersistentId pid : patientIds) {
			Id id = Id.getIdFromPID(pid, "Patient");
			for (BulkExportIdList idList : listIds) {
				found = idList.getIds().contains(id);
				if (found) {
					break;
				}
			}
			assertTrue(found);
			found = false;
		}
	}
}
