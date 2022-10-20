package ca.uhn.fhir.batch2.jobs.export;


import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WriteBinaryStepTest {
	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(WriteBinaryStep.class);

	// inner test class
	private static class TestWriteBinaryStep extends WriteBinaryStep {

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

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private DaoRegistry myDaoRegistry;

	@InjectMocks
	private TestWriteBinaryStep myFinalStep;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
	}

	@AfterEach
	public void after() {
		ourLog.detachAppender(myAppender);
	}

	private StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> createInput(BulkExportExpandedResources theData,
																																  JobInstance theInstance) {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setStartDate(new Date());
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> input = new StepExecutionDetails<>(
			parameters,
			theData,
			theInstance,
			"1"
		);
		return input;
	}

	@Test
	public void run_validInputNoErrors_succeeds() {
		// setup
		BulkExportExpandedResources expandedResources = new BulkExportExpandedResources();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		List<String> stringified = Arrays.asList("first", "second", "third", "forth");
		expandedResources.setStringifiedResources(stringified);
		expandedResources.setResourceType("Patient");
		IFhirResourceDao<IBaseBinary> binaryDao = mock(IFhirResourceDao.class);
		IJobDataSink<BulkExportBinaryFileId> sink = mock(IJobDataSink.class);
		StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> input = createInput(expandedResources, instance);
		IIdType binaryId = new IdType("Binary/123");
		DaoMethodOutcome methodOutcome = new DaoMethodOutcome();
		methodOutcome.setId(binaryId);

		// when
		when(myDaoRegistry.getResourceDao(eq("Binary")))
			.thenReturn(binaryDao);
		when(binaryDao.create(any(IBaseBinary.class), any(RequestDetails.class)))
			.thenReturn(methodOutcome);

		// test
		RunOutcome outcome = myFinalStep.run(input, sink);

		// verify
		assertEquals(RunOutcome.SUCCESS, outcome);

		ArgumentCaptor<IBaseBinary> binaryCaptor = ArgumentCaptor.forClass(IBaseBinary.class);
		verify(binaryDao)
			.create(binaryCaptor.capture(), any(RequestDetails.class));
		String outputString = new String(binaryCaptor.getValue().getContent());
		// post-pending a \n (as this is what the binary does)
		String expected = String.join("\n", stringified) + "\n";
		assertEquals(
			expected,
			outputString,
			outputString + " != " + expected
		);

		ArgumentCaptor<BulkExportBinaryFileId> fileIdArgumentCaptor = ArgumentCaptor.forClass(BulkExportBinaryFileId.class);
		verify(sink)
			.accept(fileIdArgumentCaptor.capture());
		assertEquals(binaryId.getValueAsString(), fileIdArgumentCaptor.getValue().getBinaryId());
	}

	@Test
	public void run_withIOException_throws() throws IOException {
		// setup
		String testException = "I am an exceptional exception.";
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		BulkExportExpandedResources expandedResources = new BulkExportExpandedResources();
		List<String> stringified = Arrays.asList("first", "second", "third", "forth");
		expandedResources.setStringifiedResources(stringified);
		expandedResources.setResourceType("Patient");
		IFhirResourceDao<IBaseBinary> binaryDao = mock(IFhirResourceDao.class);
		IJobDataSink<BulkExportBinaryFileId> sink = mock(IJobDataSink.class);
		StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> input = createInput(expandedResources, instance);

		ourLog.setLevel(Level.ERROR);

		// when
		when(myDaoRegistry.getResourceDao(eq("Binary")))
			.thenReturn(binaryDao);

		// we're gong to mock the writer
		OutputStreamWriter writer = mock(OutputStreamWriter.class);
		when(writer.append(anyString())).thenThrow(new IOException(testException));
		myFinalStep.setWriter(writer);

		// test
		try {
			myFinalStep.run(input, sink);
			fail();
		} catch (JobExecutionFailedException ex) {
			assertTrue(ex.getMessage().contains("Failure to process resource of type"));
		}

		// verify
		ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender).doAppend(logCaptor.capture());
		assertTrue(logCaptor.getValue().getFormattedMessage()
			.contains(
				"Failure to process resource of type "
				+ expandedResources.getResourceType()
				+ " : "
				+ testException
			));

		verify(sink, never())
			.accept(any(BulkExportBinaryFileId.class));
	}
}
