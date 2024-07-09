package ca.uhn.fhir.batch2.jobs.export;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
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
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpandResourcesStepTest {

	@Mock
	private IBulkExportProcessor<?> myProcessor;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

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
	private ExpandResourcesStep mySecondStep;

	@BeforeEach
	public void init() {
		mySecondStep.setIdHelperServiceForUnitTest(myIdHelperService);
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

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void jobComplete_withBasicParameters_succeeds(boolean thePartitioned) {
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
			createParameters(thePartitioned),
			instance
		);
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

		// test
		RunOutcome outcome = mySecondStep.run(input, sink);

		// verify
		assertEquals(RunOutcome.SUCCESS, outcome);


		// data sink
		ArgumentCaptor<ExpandedResourcesList> expandedCaptor = ArgumentCaptor.forClass(ExpandedResourcesList.class);
		verify(sink)
			.accept(expandedCaptor.capture());
		ExpandedResourcesList expandedResources = expandedCaptor.getValue();
		assertThat(expandedResources.getStringifiedResources()).hasSize(resources.size());
		// we'll only verify a single element
		// but we want to make sure it's as compact as possible
		String stringifiedElement = expandedResources.getStringifiedResources().get(0);
		assertFalse(stringifiedElement.contains("\t"));
		assertFalse(stringifiedElement.contains("\n"));
		assertFalse(stringifiedElement.contains(" "));

		// Patient Search
		ArgumentCaptor<SystemRequestDetails> patientSearchCaptor = ArgumentCaptor.forClass(SystemRequestDetails.class);
		verify(patientDao).search(any(), patientSearchCaptor.capture());
		assertEquals(input.getParameters().getPartitionId(), patientSearchCaptor.getValue().getRequestPartitionId());

	}
}
