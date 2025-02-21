package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseHapiFhirResourceDaoTest {

	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Mock
	private IJobPartitionProvider myJobPartitionProvider;

	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@Mock
	private EntityManager myEntityManager;

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Mock
	private IJobCoordinator myJobCoordinator;

	@Mock
	private IJpaStorageResourceParser myJpaStorageResourceParser;

	@Mock
	private ApplicationContext myApplicationContext;

	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	@Mock
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Mock
	private ISearchBuilder<JpaPid> myISearchBuilder;

	@Mock
	private MatchUrlService myMatchUrlService;
	@Mock
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlService;

	@Mock
	private HapiTransactionService myTransactionService;

	@Mock
	private DeleteConflictService myDeleteConflictService;

	@Mock
	private IFhirSystemDao<?, ?> mySystemDao;

	@Mock
	private ResourceSearchUrlSvc myResourceSearchUrlSvc;

	@Mock
	private CacheTagDefinitionDao myCacheTagDefinitionDao;

	@Captor
	private ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	// we won't inject this
	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@InjectMocks
	private TestResourceDao mySvc;

	private TestResourceDao mySpiedSvc;

	@BeforeEach
	public void init() {
		// set our context
		// NB: if other tests need to
		// have access to resourcetype/name
		// the individual tests will have to start
		// by calling setup themselves
		mySvc.setContext(myFhirContext);
		mySpiedSvc = spy(mySvc);
	}

	/**
	 * To be called for tests that require additional
	 * setup
	 *
	 * @param theClazz
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void setup(Class theClazz) {
		mySvc.setResourceType(theClazz);
		mySvc.start();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void searchForResources_withNullResourceReturns_doesNotFail() {
		// setup
		SearchParameterMap map = new SearchParameterMap();
		RequestDetails requestDetails = new SystemRequestDetails();

		MockHapiTransactionService myTransactionService = new MockHapiTransactionService();
		mySvc.setTransactionService(myTransactionService);
		setup(Patient.class);
		List<Patient> resourceList = new ArrayList<>();
		resourceList.add(null);
		resourceList.add(new Patient());

		// when
		when(mySearchBuilderFactory.newSearchBuilder(eq("Patient"), eq(Patient.class)))
			.thenReturn(myISearchBuilder);
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(eq(requestDetails), eq("Patient"), eq(map)))
			.thenReturn(RequestPartitionId.allPartitions());
		when(myISearchBuilder.createQueryStream(any(SearchParameterMap.class), any(SearchRuntimeDetails.class), any(RequestDetails.class), any(RequestPartitionId.class)))
			.thenReturn(Stream.of(JpaPid.fromId(1L), JpaPid.fromId(2L)));
		when(myISearchBuilder.loadResourcesByPid(any(Collection.class), any(RequestDetails.class)))
			.thenReturn(resourceList);

		// test
		List<Patient> resources = mySvc.searchForResources(map, requestDetails);

		// verify
		// list of 2 in (null, resource), list of 1 out (only resource)
		assertEquals(1, resources.size());
	}

	@Test
	public void validateResourceIdCreation_asSystem() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new SystemRequestDetails();
		mySvc.getStorageSettings().setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	@Test
	public void validateResourceIdCreation_asUser() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new ServletRequestDetails();
		when(myStorageSettings.getResourceClientIdStrategy()).thenReturn(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(959) + "failedToCreateWithClientAssignedIdNotAllowed", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withNumber() {
		Patient patient = new Patient();
		patient.setId("2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		when(myStorageSettings.getResourceClientIdStrategy()).thenReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(960) + "failedToCreateWithClientAssignedNumericId", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withAlpha() {
		Patient patient = new Patient();
		patient.setId("P2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getStorageSettings().setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	@Test
	public void delete_nonExistentEntity_doesNotThrow404() {
		// initialize our class
		setup(Patient.class);

		// setup
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);

		IIdType id = new IdType("Patient/123"); // id part is only numbers
		DeleteConflictList deleteConflicts = new DeleteConflictList();
		RequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();

		RequestPartitionId partitionId = Mockito.mock(RequestPartitionId.class);
		JpaPid jpaPid = JpaPid.fromIdAndVersion(123L, 1L);
		ResourceTable entity = new ResourceTable();
		entity.setIdForUnitTest(123L);
		entity.setFhirId("456");

		// set a transactionService that will actually execute the callback
		mySvc.setTransactionService(new MockHapiTransactionService());

		// mock
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
			any(RequestDetails.class),
			anyString(),
			any(IIdType.class)
		)).thenReturn(partitionId);
		when(myIdHelperService.resolveResourceIdentityPid(
			any(RequestPartitionId.class),
			anyString(),
			anyString(),
			any()
		)).thenReturn(jpaPid);
		when(myEntityManager.find(
			any(),
			any()
		)).thenReturn(entity);
		// we don't stub myConfig.getResourceClientIdStrategy()
		// because even a null return isn't ANY...
		// if this changes, though, we will have to stub it.
		// but for now, Mockito will complain, so we'll leave it out

		// test
		DaoMethodOutcome outcome;
		try {
			TransactionSynchronizationManager.setActualTransactionActive(true);
			outcome = mySvc.delete(id, deleteConflicts, requestDetails, transactionDetails);
		} finally {
			TransactionSynchronizationManager.setActualTransactionActive(false);
		}

		// verify
		assertNotNull(outcome);
		assertEquals(id.getValue(), outcome.getId().getValue());
	}

	@Test
	public void requestReindexForRelatedResources_withValidBase_includesUrlsInJobParameters() {
		when(myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange()).thenReturn(true);

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		List<String> base = Lists.newArrayList("Patient", "Group", "Practitioner");

		when(myJobPartitionProvider.getPartitionedUrls(any(), any())).thenAnswer(i -> {
			List<String> urls = i.getArgument(1);
			return urls.stream().map(url -> new PartitionedUrl().setUrl(url).setRequestPartitionId(partitionId)).collect(Collectors.toList());
		});

		mySvc.requestReindexForRelatedResources(false, base, new ServletRequestDetails());

		ArgumentCaptor<JobInstanceStartRequest> requestCaptor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		Mockito.verify(myJobCoordinator).startInstance(isNotNull(), requestCaptor.capture());

		JobInstanceStartRequest actualRequest = requestCaptor.getValue();
		assertNotNull(actualRequest);
		assertNotNull(actualRequest.getParameters());
		ReindexJobParameters actualParameters = actualRequest.getParameters(ReindexJobParameters.class);

		assertThat(actualParameters.getPartitionedUrls()).hasSize(base.size());
		for (int i = 0; i < base.size(); i++) {
			PartitionedUrl partitionedUrl = actualParameters.getPartitionedUrls().get(i);
			assertEquals(base.get(i) + "?", partitionedUrl.getUrl());
			assertEquals(partitionId, partitionedUrl.getRequestPartitionId());
		}
	}

	@Test
	public void requestReindexForRelatedResources_withSpecialBaseResource_doesNotIncludeUrlsInJobParameters() {
		when(myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange()).thenReturn(true);

		List<String> base = Lists.newArrayList("Resource");

		mySvc.requestReindexForRelatedResources(false, base, new ServletRequestDetails());

		ArgumentCaptor<JobInstanceStartRequest> requestCaptor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		Mockito.verify(myJobCoordinator).startInstance(isNotNull(), requestCaptor.capture());

		JobInstanceStartRequest actualRequest = requestCaptor.getValue();
		assertNotNull(actualRequest);
		assertNotNull(actualRequest.getParameters());
		ReindexJobParameters actualParameters = actualRequest.getParameters(ReindexJobParameters.class);

		assertThat(actualParameters.getPartitionedUrls()).isEmpty();
	}

	@ParameterizedTest
	@MethodSource("searchParameterMapProvider")
	public void testMethodSearchForIds_withNullSPMapLoadSynchronousUpTo_defaultsToInternalSynchronousSearchSize(SearchParameterMap theSearchParameterMap, int expectedSearchSize) {
		// setup
		MockHapiTransactionService myTransactionService = new MockHapiTransactionService();
		mySvc.setTransactionService(myTransactionService);

		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), any(), any(), any())).thenReturn(mock(RequestPartitionId.class));
		when(mySearchBuilderFactory.newSearchBuilder(any(), any())).thenReturn(myISearchBuilder);
		when(myISearchBuilder.createQuery(any(), any(), any(), any())).thenReturn(mock(IResultIterator.class));

		lenient().when(myStorageSettings.getInternalSynchronousSearchSize()).thenReturn(5000);

		// execute
		mySvc.searchForIds(theSearchParameterMap, new SystemRequestDetails(), null);

		// verify
		verify(myISearchBuilder).createQuery(mySearchParameterMapCaptor.capture(), any(), any(), any());
		SearchParameterMap capturedSP = mySearchParameterMapCaptor.getValue();
		assertEquals(capturedSP.getLoadSynchronousUpTo(), expectedSearchSize);
	}

	static Stream<Arguments> searchParameterMapProvider() {
		return Stream.of(
			Arguments.of(new SearchParameterMap().setLoadSynchronousUpTo(1000), 1000),
			Arguments.of(new SearchParameterMap(), 5000)
		);
	}

	@Test
	public void testUpdateTags_withDuplicateTags_willNotUpdateEntityTagList(){
		RequestDetails sysRequest = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		TagDefinition duplicateTagDefinition = createTagDefinition(2);

		// given a patient resource with tags stored in the database
		TagDefinition tagDefinition = createTagDefinition(1);
		ResourceTable entity = new ResourceTable();
		entity.setId(new JpaPid(null, 1l));
		entity.addTag(tagDefinition);
		entity.setHasTags(true);

		// given a client update request on the patient resource that is stored in the database
		Patient patient = new Patient();
		patient.getMeta().addTag(tagDefinition.getSystem(), tagDefinition.getCode(), tagDefinition.getDisplay());

		// when we search the database for existing tags, return a duplicate one.
		when(myCacheTagDefinitionDao.getTagOrNull(any(), any(), any(), any(), any(), any(), any())).thenReturn(duplicateTagDefinition);

		boolean updated = mySpiedSvc.updateTags(transactionDetails, sysRequest, patient, entity);

		// assert that the entity was not updated with the duplicate tag
		assertThat(updated).isFalse();
		Collection<ResourceTag> entityTags = entity.getTags();
		assertThat(entityTags).hasSize(1);
		assertThat(entityTags.iterator().next().getTag().getId()).isEqualTo(tagDefinition.getId());
	}

	private TagDefinition createTagDefinition(int theId) {
		TagDefinition tagDefinition = new TagDefinition(TagTypeEnum.TAG, "sytem", "code", null);
		tagDefinition.setId((long) theId);

		return tagDefinition;
	}

	@Nested
	class DeleteThresholds {
		private static final String URL = "Patient?_lastUpdated=gt2024-01-01";
		private static final RequestDetails REQUEST = new SystemRequestDetails();
		private static final DeleteMethodOutcome EXPECTED_DELETE_OUTCOME = new DeleteMethodOutcome();

		@BeforeEach
		void beforeEach() {
			when(myStorageSettings.isDeleteEnabled()).thenReturn(true);
			when(myMatchUrlService.getResourceSearch(URL))
				.thenReturn(new ResourceSearch(mock(RuntimeResourceDefinition.class), SearchParameterMap.newSynchronous(), RequestPartitionId.allPartitions()));

			// mocks for transaction handling:
			final IHapiTransactionService.IExecutionBuilder mockExecutionBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);
			when(mockExecutionBuilder.withTransactionDetails(any(TransactionDetails.class))).thenReturn(mockExecutionBuilder);
			when(myTransactionService.withRequest(REQUEST)).thenReturn(mockExecutionBuilder);
			final Answer<DeleteMethodOutcome> answer = theInvocationOnMock -> {
				final TransactionCallback<DeleteMethodOutcome> arg = theInvocationOnMock.getArgument(0);
				return arg.doInTransaction(mock(TransactionStatus.class));
			};
			when(mockExecutionBuilder.execute(ArgumentMatchers.<TransactionCallback<DeleteMethodOutcome>>any()))
				.thenAnswer(answer);
		}

		@ParameterizedTest
		@MethodSource("thresholdsAndResourceIds_Pass")
		void deleteByUrlConsiderThresholdUnder_Pass(long theThreshold, Set<Long> theResourceIds) {
			if (theResourceIds.size() > 1) {
				when(myStorageSettings.isAllowMultipleDelete()).thenReturn(true);
				when(myStorageSettings.getRestDeleteByUrlResourceIdThreshold()).thenReturn(theThreshold);
			}

			 doReturn(EXPECTED_DELETE_OUTCOME).when(mySpiedSvc).deletePidList(any(), any(), any(), any(), any());

			handleExpectedResourceIds(theResourceIds);

			final DeleteMethodOutcome deleteMethodOutcome = mySpiedSvc.deleteByUrl(URL, REQUEST);
			assertEquals(EXPECTED_DELETE_OUTCOME, deleteMethodOutcome);
		}

		@ParameterizedTest
		@MethodSource("thresholdsAndResourceIds_Fail")
		void deleteByUrlConsiderThreshold_Over_Fail(long theThreshold, Set<Long> theResourceIds) {
			 when(myStorageSettings.isAllowMultipleDelete()).thenReturn(true);
			 when(myStorageSettings.getRestDeleteByUrlResourceIdThreshold()).thenReturn(theThreshold);

			final Set<JpaPid> expectedResourceIds = handleExpectedResourceIds(theResourceIds);

				assertThatThrownBy(() ->
				mySpiedSvc.deleteByUrl(URL, REQUEST))
					.isInstanceOf(PreconditionFailedException.class)
						.hasMessage(String.format("HAPI-2496: Failed to DELETE resources with match URL \"Patient?_lastUpdated=gt2024-01-01\" because the resolved number of resources: %s exceeds the threshold of %s", expectedResourceIds.size(), theThreshold));
		}

		private Set<JpaPid> handleExpectedResourceIds(Set<Long> theResourceIds) {
			final Set<JpaPid> expectedResourceIds = theResourceIds.stream().map(JpaPid::fromId).collect(Collectors.toUnmodifiableSet());
			when(myMatchResourceUrlService.search(any(), any(), any(), any())).thenReturn(expectedResourceIds);
			return expectedResourceIds;
		}

		static Stream<Arguments> thresholdsAndResourceIds_Pass() {
			return Stream.of(
				Arguments.of(0, Collections.emptySet()),
				Arguments.of(1, Collections.emptySet()),
				Arguments.of(2, Collections.emptySet()),
				Arguments.of(3, Collections.emptySet()),
				Arguments.of(4, Collections.emptySet()),
				Arguments.of(5, Collections.emptySet()),
				Arguments.of(1, Set.of(1L)),
				Arguments.of(2, Set.of(1L)),
				Arguments.of(3, Set.of(1L)),
				Arguments.of(4, Set.of(1L)),
				Arguments.of(5, Set.of(1L)),
				Arguments.of(4, Set.of(1L,2L,3L)),
				Arguments.of(5, Set.of(1L,2L,3L))
			);
		}

		static Stream<Arguments> thresholdsAndResourceIds_Fail() {
			return Stream.of(
				Arguments.of(0, Set.of(1L,2L)),
				Arguments.of(1, Set.of(1L,2L)),
				Arguments.of(0, Set.of(1L,2L,3L)),
				Arguments.of(1, Set.of(1L,2L,3L)),
				Arguments.of(2, Set.of(1L,2L,3L))
			);
		}
	}

	static class TestResourceDao extends BaseHapiFhirResourceDao<Patient> {

		@Override
		public JpaStorageSettings getStorageSettings() {
			return myStorageSettings;
		}

		@Override
		protected String getMessageSanitized(String theKey, String theIdPart) {
			return theKey;
		}
	}
}
