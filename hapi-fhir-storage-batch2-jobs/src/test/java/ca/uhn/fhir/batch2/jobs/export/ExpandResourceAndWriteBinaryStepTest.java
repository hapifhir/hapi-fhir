package ca.uhn.fhir.batch2.jobs.export;


import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpandResourceAndWriteBinaryStepTest {
	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(ExpandResourceAndWriteBinaryStep.class);

	// inner test class
	private static class TestExpandResourceAndWriteBinaryStep extends ExpandResourceAndWriteBinaryStep {

		private OutputStreamWriter myWriter;

		public void setWriter(OutputStreamWriter theWriter) {
			myWriter = theWriter;
		}

		@Override
		protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
			if (myWriter == null) {
				return super.getStreamWriter(theOutputStream);
			}
			else {
				return myWriter;
			}
		}
	}

	@Mock
	private ListAppender<ILoggingEvent> myAppender;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	IIdHelperService<JpaPid> myIdHelperService;

	@Spy
	private InterceptorService myInterceptorService = new InterceptorService();

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	@Spy
	private IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();

	@InjectMocks
	private TestExpandResourceAndWriteBinaryStep myFinalStep;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
		myFinalStep.setIdHelperServiceForUnitTest(myIdHelperService);
	}

	@AfterEach
	public void after() {
		ourLog.detachAppender(myAppender);
	}

	private BulkExportJobParameters createParameters(boolean thePartitioned) {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		parameters.setOutputFormat("json");
		parameters.setSince(new Date());
		if (thePartitioned) {
			parameters.setPartitionId(RequestPartitionId.fromPartitionName("Partition-A"));
		}
		return parameters;
	}

	private StepExecutionDetails<BulkExportJobParameters, ResourceIdList> createInput(ResourceIdList theData,
																												 BulkExportJobParameters theParameters,
																												 JobInstance theInstance) {
		return new StepExecutionDetails<>(
			theParameters,
			theData,
			theInstance,
			new WorkChunk().setId("1")
		);
	}

	private IFhirResourceDao<?> mockOutDaoRegistry() {
		IFhirResourceDao<?> mockDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao(anyString()))
			.thenReturn(mockDao);
		return mockDao;
	}

	private RequestPartitionId getPartitionId(boolean thePartitioned) {
		if (thePartitioned) {
			return RequestPartitionId.fromPartitionName("Partition-A");
		} else {
			return RequestPartitionId.defaultPartition();
		}
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void run_validInputNoErrors_succeeds(boolean thePartitioned) {
		// setup
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		IFhirResourceDao<IBaseBinary> binaryDao = mock(IFhirResourceDao.class);
		IFhirResourceDao<?> patientDao = mockOutDaoRegistry();
		IJobDataSink<BulkExportBinaryFileId> sink = mock(IJobDataSink.class);

		ResourceIdList idList = new ResourceIdList();
		ArrayList<IBaseResource> resources = createResourceList(idList);

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> input = createInput(
			idList,
			createParameters(thePartitioned),
			instance
		);

		IIdType binaryId = new IdType("Binary/123");
		DaoMethodOutcome methodOutcome = new DaoMethodOutcome();
		methodOutcome.setId(binaryId);

		// when
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(resources));
		when(myIdHelperService.newPidFromStringIdAndResourceName(anyString(), anyString())).thenReturn(JpaPid.fromId(1L));
		when(myIdHelperService.translatePidsToForcedIds(any())).thenAnswer(t->{
			Set<IResourcePersistentId<JpaPid>> inputSet = t.getArgument(0, Set.class);
			Map<IResourcePersistentId<?>, Optional<String>> map = new HashMap<>();
			for (var next : inputSet) {
				map.put(next, Optional.empty());
			}
			return new PersistentIdToForcedIdMap<>(map);
		});
		when(myDaoRegistry.getResourceDao(eq("Binary")))
			.thenReturn(binaryDao);
		when(binaryDao.update(any(IBaseBinary.class), any(RequestDetails.class)))
			.thenReturn(methodOutcome);

		// test
		RunOutcome outcome = myFinalStep.run(input, sink);

		// verify
		assertEquals(new RunOutcome(resources.size()).getRecordsProcessed(), outcome.getRecordsProcessed());

		ArgumentCaptor<IBaseBinary> binaryCaptor = ArgumentCaptor.forClass(IBaseBinary.class);
		ArgumentCaptor<SystemRequestDetails> binaryDaoCreateRequestDetailsCaptor = ArgumentCaptor.forClass(SystemRequestDetails.class);
		verify(binaryDao)
			.update(binaryCaptor.capture(), binaryDaoCreateRequestDetailsCaptor.capture());
		String outputString = new String(binaryCaptor.getValue().getContent());
		assertEquals(resources.size(), StringUtils.countOccurrencesOf(outputString, "\n"));
		if (thePartitioned) {
			assertEquals(getPartitionId(thePartitioned), binaryDaoCreateRequestDetailsCaptor.getValue().getRequestPartitionId());
		}

		ArgumentCaptor<BulkExportBinaryFileId> fileIdArgumentCaptor = ArgumentCaptor.forClass(BulkExportBinaryFileId.class);
		verify(sink)
			.accept(fileIdArgumentCaptor.capture());
		assertEquals(binaryId.getValueAsString(), fileIdArgumentCaptor.getValue().getBinaryId());
	}

	@Nonnull
	private static ArrayList<IBaseResource> createResourceList(ResourceIdList idList) {
		idList.setResourceType("Patient");
		ArrayList<IBaseResource> resources = new ArrayList<>();
		ArrayList<BatchResourceId> batchResourceIds = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			String stringId = String.valueOf(i);
			BatchResourceId batchResourceId = new BatchResourceId();
			batchResourceId.setResourceType("Patient");
			batchResourceId.setId(stringId);
			batchResourceIds.add(batchResourceId);

			Patient patient = new Patient();
			patient.setId(stringId);
			resources.add(patient);
		}
		idList.setIds(batchResourceIds);
		return resources;
	}

	@Test
	public void run_withIOException_throws() throws IOException {
		// setup
		String testException = "I am an exceptional exception.";
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		ResourceIdList idList = new ResourceIdList();
		ArrayList<IBaseResource> resources = createResourceList(idList);
		IFhirResourceDao<IBaseBinary> binaryDao = mock(IFhirResourceDao.class);
		IFhirResourceDao<?> patientDao = mockOutDaoRegistry();
		IJobDataSink<BulkExportBinaryFileId> sink = mock(IJobDataSink.class);

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> input = createInput(
			idList,
			createParameters(false),
			instance
		);
		ourLog.setLevel(Level.ERROR);

		// when
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(resources));
		when(myIdHelperService.newPidFromStringIdAndResourceName(anyString(), anyString())).thenReturn(JpaPid.fromId(1L));
		when(myIdHelperService.translatePidsToForcedIds(any())).thenAnswer(t->{
			Set<IResourcePersistentId<JpaPid>> inputSet = t.getArgument(0, Set.class);
			Map<IResourcePersistentId<?>, Optional<String>> map = new HashMap<>();
			for (var next : inputSet) {
				map.put(next, Optional.empty());
			}
			return new PersistentIdToForcedIdMap<>(map);
		});
		when(myDaoRegistry.getResourceDao(eq("Binary")))
			.thenReturn(binaryDao);

		// we're gong to mock the writer
		OutputStreamWriter writer = mock(OutputStreamWriter.class);
		when(writer.append(anyString())).thenThrow(new IOException(testException));
		myFinalStep.setWriter(writer);

		// test
		try {
			myFinalStep.run(input, sink);
			fail("");
		} catch (JobExecutionFailedException ex) {
			assertThat(ex.getMessage()).contains("Failure to process resource of type");
		}

		// verify
		ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender).doAppend(logCaptor.capture());
		assertThat(logCaptor.getValue().getFormattedMessage()).contains("Failure to process resource of type "
			+ idList.getResourceType()
			+ " : "
			+ testException);

		verify(sink, never())
			.accept(any(BulkExportBinaryFileId.class));
	}
}
