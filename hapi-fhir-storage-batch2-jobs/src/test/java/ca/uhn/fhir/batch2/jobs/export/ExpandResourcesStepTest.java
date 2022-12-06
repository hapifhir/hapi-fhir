package ca.uhn.fhir.batch2.jobs.export;


import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpandResourcesStepTest {

	@Mock
	private IBulkExportProcessor myProcessor;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Mock
	IIdHelperService myIdHelperService;

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Spy
	private ModelConfig myModelConfig = new ModelConfig();

	@InjectMocks
	private ExpandResourcesStep mySecondStep;

	private BulkExportJobParameters createParameters() {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
		parameters.setOutputFormat("json");
		parameters.setStartDate(new Date());
		return parameters;
	}

	private StepExecutionDetails<BulkExportJobParameters, ResourceIdList> createInput(ResourceIdList theData,
                                                                                      BulkExportJobParameters theParameters,
                                                                                      JobInstance theInstance) {
		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> input = new StepExecutionDetails<>(
			theParameters,
			theData,
			theInstance,
			"1"
		);
		return input;
	}

	private IFhirResourceDao<?> mockOutDaoRegistry() {
		IFhirResourceDao mockDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao(anyString()))
			.thenReturn(mockDao);
		return mockDao;
	}

	@Test
	public void jobComplete_withBasicParameters_succeeds() {
		//setup
		JobInstance instance = new JobInstance();
		instance.setInstanceId("1");
		IJobDataSink<ExpandedResourcesList> sink = mock(IJobDataSink.class);
		IFhirResourceDao<?> patientDao = mockOutDaoRegistry();
		ResourceIdList idList = new ResourceIdList();
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

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> input = createInput(
			idList,
			createParameters(),
			instance
		);
		ArrayList<IBaseResource> clone = new ArrayList<>(resources);
		when(patientDao.readByPid(any(BaseResourcePersistentId.class))).thenAnswer(i -> clone.remove(0));
		when(myIdHelperService.newPidFromStringIdAndResourceName(anyString(), anyString())).thenReturn(new JpaPid(1L));
		// test
		RunOutcome outcome = mySecondStep.run(input, sink);

		// verify
		assertEquals(RunOutcome.SUCCESS, outcome);


		// data sink
		ArgumentCaptor<ExpandedResourcesList> expandedCaptor = ArgumentCaptor.forClass(ExpandedResourcesList.class);
		verify(sink)
			.accept(expandedCaptor.capture());
		ExpandedResourcesList expandedResources = expandedCaptor.getValue();
		assertEquals(resources.size(), expandedResources.getStringifiedResources().size());
		// we'll only verify a single element
		// but we want to make sure it's as compact as possible
		String stringifiedElement = expandedResources.getStringifiedResources().get(0);
		assertFalse(stringifiedElement.contains("\t"));
		assertFalse(stringifiedElement.contains("\n"));
		assertFalse(stringifiedElement.contains(" "));
	}
}
