package ca.uhn.fhir.batch2.jobs.export;


import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.checkerframework.checker.units.qual.A;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

	@Mock
	private ListAppender<ILoggingEvent> myAppender;

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IBulkExportProcessor myBulkExportProcessor;

	@InjectMocks
	private WriteBinaryStep myFinalStep;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
	}

	@AfterEach
	public void after() {
		ourLog.detachAppender(myAppender);
	}

	private StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> createInput(BulkExportExpandedResources theData) {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setStartDate(new Date());
		parameters.setJobId("jobId");
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> input = new StepExecutionDetails<>(
			parameters,
			theData,
			"1",
			"1"
		);
		return input;
	}

	@Test
	public void run_validInputNoErrors_succeeds() {
		// setup
		BulkExportExpandedResources expandedResources = new BulkExportExpandedResources();
		expandedResources.setJobId("jobId");
		List<String> stringified = Arrays.asList("first", "second", "third", "forth");
		expandedResources.setStringifiedResources(stringified);
		expandedResources.setResourceType("Patient");
		IFhirResourceDao<IBaseBinary> binaryDao = mock(IFhirResourceDao.class);
		IJobDataSink<VoidModel> sink = mock(IJobDataSink.class);
		StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> input = createInput(expandedResources);
		IIdType binaryId = new IdType("Binary/123");
		DaoMethodOutcome methodOutcome = new DaoMethodOutcome();
		methodOutcome.setId(binaryId);

		// when
		when(myDaoRegistry.getResourceDao(anyString()))
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

		verify(myBulkExportProcessor)
			.addFileToCollection(eq(expandedResources.getJobId()),
				eq(expandedResources.getResourceType()),
				any(IIdType.class));

		verify(myBulkExportProcessor, never())
			.setJobStatus(any(), any());
	}
}
