package ca.uhn.fhir.batch2.jobs.export;


import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

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

	private StepExecutionDetails<BulkExportJobParameters, BulkExportIdList> createInput(BulkExportIdList theData,
																													BulkExportJobParameters theParameters,
																													JobInstance theInstance) {
		StepExecutionDetails<BulkExportJobParameters, BulkExportIdList> input = new StepExecutionDetails<>(
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
		IJobDataSink<BulkExportExpandedResources> sink = mock(IJobDataSink.class);
		IFhirResourceDao<?> patientDao = mockOutDaoRegistry();
		BulkExportIdList idList = new BulkExportIdList();
		idList.setResourceType("Patient");
		ArrayList<IBaseResource> resources = new ArrayList<>();
		ArrayList<Id> ids = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			String stringId = "Patient/" + i;
			Id id = new Id();
			id.setResourceType("Patient");
			id.setId(stringId);
			ids.add(id);

			Patient patient = new Patient();
			patient.setId(stringId);
			resources.add(patient);
		}
		idList.setIds(ids);
		IBundleProvider bundleProvider = mock(IBundleProvider.class);

		StepExecutionDetails<BulkExportJobParameters, BulkExportIdList> input = createInput(
			idList,
			createParameters(),
			instance
		);

		// when
		when(bundleProvider.getAllResources())
			.thenReturn(resources);
		when(patientDao.search(any(SearchParameterMap.class), any()))
			.thenReturn(bundleProvider);

		// test
		RunOutcome outcome = mySecondStep.run(input, sink);

		// verify
		assertEquals(RunOutcome.SUCCESS, outcome);

		// search parameters
		ArgumentCaptor<SearchParameterMap> captor = ArgumentCaptor.forClass(SearchParameterMap.class);
		verify(patientDao)
			.search(captor.capture(), any());
		assertEquals(1, captor.getAllValues().size());
		SearchParameterMap map = captor.getValue();
		Collection<List<List<IQueryParameterType>>> values = map.values();

		Set<String> idValues = new HashSet<>();
		for (List<List<IQueryParameterType>> parameterTypes : values) {
			for (List<IQueryParameterType> param : parameterTypes) {
				for (IQueryParameterType type : param) {
					String value = type.getValueAsQueryToken(myFhirContext);
					idValues.add(value);
					Id findingId = new Id();
					findingId.setId(value);
					findingId.setResourceType("Patient");
					assertTrue(ids.contains(findingId));
				}
			}
		}
		assertEquals(ids.size(), idValues.size());

		// data sink
		ArgumentCaptor<BulkExportExpandedResources> expandedCaptor = ArgumentCaptor.forClass(BulkExportExpandedResources.class);
		verify(sink)
			.accept(expandedCaptor.capture());
		BulkExportExpandedResources expandedResources = expandedCaptor.getValue();
		assertEquals(resources.size(), expandedResources.getStringifiedResources().size());
		// we'll only verify a single element
		// but we want to make sure it's as compact as possible
		String stringifiedElement = expandedResources.getStringifiedResources().get(0);
		assertFalse(stringifiedElement.contains("\t"));
		assertFalse(stringifiedElement.contains("\n"));
		assertFalse(stringifiedElement.contains(" "));
	}
}
