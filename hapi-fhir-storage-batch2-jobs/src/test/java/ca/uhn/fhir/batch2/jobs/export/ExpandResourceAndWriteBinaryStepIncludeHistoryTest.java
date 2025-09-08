package ca.uhn.fhir.batch2.jobs.export;


import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import com.google.common.collect.ArrayListMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpandResourceAndWriteBinaryStepIncludeHistoryTest {

	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	@Spy
	private IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();

	@Mock
	private IBulkDataExportHistoryHelper myExportHelper;

	@Mock
	private Consumer<List<IBaseResource>> myResourceListConsumer;

	@InjectMocks
	private ExpandResourceAndWriteBinaryStep myJobStep;


	public static final int EXPORT_FILE_MAX_CAPACITY = 7;
	public static final int MAXIMUM_BATCH_SIZE_DEFAULT = 5;

	public static final int PATIENT_RESOURCE_COUNT = 37;
	public static final int PATIENT_RESOURCE_VERSIONS_COUNT = 3;

	public static final int OBSERVATION_RESOURCE_COUNT = 71;
	public static final int OBSERVATION_RESOURCE_VERSIONS_COUNT = 2;

	@BeforeEach
	public void init() {
		myJobStep.setIdHelperServiceForUnitTest(myIdHelperService);
	}

	private ArrayListMultimap<String, TypedPidJson> myTypeToIdsMap;

	private ExpandResourceAndWriteBinaryStep myTestedStep;

	@Captor
	private ArgumentCaptor<List<IBaseResource>> resourcesConsumedCaptor;

	@BeforeEach
	void setUp() {
		myTestedStep = spy(myJobStep);
		doReturn(MAXIMUM_BATCH_SIZE_DEFAULT).when(myTestedStep).getMaxSizeBatchDefault();

		myStorageSettings.setBulkExportFileMaximumCapacity(EXPORT_FILE_MAX_CAPACITY);

		setupIdHelperServiceMockAsASpy();

	}

	@Test
	void testHistoryPartitioningAlgorithm() {
		// given
		List<IBaseResource> allResourceVersions = new ArrayList<>();
		setupExportHelperResultsMock(allResourceVersions::addAll);

		myTypeToIdsMap = ArrayListMultimap.create();
		List<TypedPidJson> patientIds = getResourcesTypedPidJsonList("Patient", PATIENT_RESOURCE_COUNT);
		patientIds.forEach(t -> myTypeToIdsMap.put(t.getResourceType(), t));
		List<TypedPidJson> observationIds = getResourcesTypedPidJsonList("Observation", OBSERVATION_RESOURCE_COUNT);
		observationIds.forEach(t -> myTypeToIdsMap.put(t.getResourceType(), t));

		// when
		myTestedStep.processHistoryResources(myResourceListConsumer, myTypeToIdsMap, RequestPartitionId.allPartitions());

		// then
		// all resources should have been consumed in batches of MAXIMUM_BATCH_SIZE_DEFAULT (except last, which could have less)
		int totalPatientBatches = 	(int) Math.ceil((double) PATIENT_RESOURCE_COUNT * PATIENT_RESOURCE_VERSIONS_COUNT / EXPORT_FILE_MAX_CAPACITY);
		int totalObservationBatches = 	(int) Math.ceil((double) OBSERVATION_RESOURCE_COUNT * OBSERVATION_RESOURCE_VERSIONS_COUNT / EXPORT_FILE_MAX_CAPACITY);
		verify(myResourceListConsumer, times(totalPatientBatches + totalObservationBatches)).accept(resourcesConsumedCaptor.capture());

		List<List<IBaseResource>> allProcessedResourceBatches = resourcesConsumedCaptor.getAllValues();
		List<IBaseResource> allExportedResourceVersions = allProcessedResourceBatches.stream().flatMap(Collection::stream).toList();
		assertThat(allExportedResourceVersions).isEqualTo(allResourceVersions);
	}

	private void setupExportHelperResultsMock(Consumer<List<IBaseResource>> theResourceVersionConsumer) {
		when(myExportHelper.fetchHistoryForResourceIds(eq("Patient"), any(), any())).thenAnswer(invocation -> {
			List<String> theResourceIdList = invocation.getArgument(1);
			List<IBaseResource> resourceList = getHistoryResourcesForResourceIds("Patient", theResourceIdList, PATIENT_RESOURCE_VERSIONS_COUNT);
			theResourceVersionConsumer.accept(resourceList);
			return new SimpleBundleProvider(resourceList);
		});

		when(myExportHelper.fetchHistoryForResourceIds(eq("Observation"), any(), any())).thenAnswer(invocation -> {
			List<String> theResourceIdList = invocation.getArgument(1);
			List<IBaseResource> resourceList = getHistoryResourcesForResourceIds("Observation", theResourceIdList, OBSERVATION_RESOURCE_VERSIONS_COUNT);
			theResourceVersionConsumer.accept(resourceList);
			return new SimpleBundleProvider(resourceList);
		});
	}

	/**
	 * Generate and return the indicated number of versions mocks for each received resourceId
	 */
	private List<IBaseResource> getHistoryResourcesForResourceIds(String theResourceType, List<String> theTheResourceIdList, int theVersionCount) {
		return theTheResourceIdList.stream().flatMap(id -> getResourceHistoryMocks(theResourceType, id, theVersionCount)).toList();
	}

	private Stream<IBaseResource> getResourceHistoryMocks(String theResourceType, String theId, int theVersionCount) {
		List<IBaseResource> versions = new ArrayList<>();
		for (int i = 1; i <= theVersionCount; i++) {
			IBaseResource r = createResource(theResourceType);

			String[] splitId = theId.split("/");
			String id = splitId[splitId.length-1];

			r.setId(new IdType(theResourceType, id, String.valueOf(i)));
			versions.add(r);
		}
		return versions.stream();
	}

	private IBaseResource createResource(String theResourceType) {
		return switch(theResourceType) {
			case "Patient" -> new Patient();
			case "Observation" -> new Observation();
			default -> throw new InvalidParameterException("Unexpected resource type: " + theResourceType);
		};
	}

	private List<TypedPidJson> getResourcesTypedPidJsonList(String theResourceType, int theCount) {
		return IntStream.range(1, theCount+1).mapToObj(i -> new TypedPidJson(theResourceType, 1, String.valueOf(i))).toList();
	}

	// setup mock as a Spy for required methods as module doesn't have access to JPA module implementations
	private void setupIdHelperServiceMockAsASpy() {
		when(myIdHelperService.newPidFromStringIdAndResourceName(any(), any(), any())).thenAnswer(invocation -> {
			Integer thePartitionId = invocation.getArgument(0);
			String thePid = invocation.getArgument(1);
			String theResourceType = invocation.getArgument(2);

			JpaPid retVal = JpaPid.fromId(Long.parseLong(thePid), thePartitionId);
			retVal.setResourceType(theResourceType);
			return retVal;
		});

		when(myIdHelperService.translatePidsToForcedIds(any())).thenAnswer(invocation -> {
			Set<JpaPid> theResourceIds = invocation.getArgument(0);
			Map<JpaPid, Optional<String>> theMap = theResourceIds.stream().collect(Collectors.toMap(
				Function.identity(),
				jpaPid -> Optional.of(jpaPid.getResourceType() + jpaPid)
			));

			return new PersistentIdToForcedIdMap<>(theMap);
		});
	}
}
