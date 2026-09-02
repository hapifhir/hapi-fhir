package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.ISearchResultConsumer;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.SearchProgressTracker;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchInclude;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static ca.uhn.fhir.rest.api.Constants.EMPTY_STRING_ARRAY;
import static ca.uhn.fhir.test.utilities.SearchTestUtil.toUnqualifiedVersionlessIdValues;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by gemini-3.7-flash
@ExtendWith(MockitoExtension.class)
class BaseCacheAwareJpaSearchBundleProviderTest implements ITestDataBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseCacheAwareJpaSearchBundleProviderTest.class);

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final InterceptorService myInterceptorBroadcaster = new InterceptorService();
	private final MockHapiTransactionService myTxService = new MockHapiTransactionService();

	@Mock
	private IAnonymousInterceptor myAnonymousInterceptor;
	@Mock
	private RequestDetails myRequestDetails;
	@Mock
	private IPagingProvider myPagingProvider;
	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	@Mock
	private EntityManager myEntityManager;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private ISearchCacheSvc mySearchCacheSvc;
	@Mock
	private ISearchResultCacheSvc mySearchResultCacheSvc;
	@Mock
	private ExceptionService myExceptionService;
	@Mock
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	@Mock
	private ISearchBuilder<JpaPid> mySearchBuilder;
	@Mock
	private IAnonymousInterceptor myMockInterceptor;

	@Captor
	private ArgumentCaptor<List<JpaPid>> myPidListCaptor;

	private SearchParameterMap myParams;
	private RequestPartitionId myRequestPartitionId;
	private Search mySearchEntity;
	private TestCacheAwareJpaSearchBundleProvider myBundleProvider;



	@BeforeEach
	void setUp() {
		myParams = new SearchParameterMap();
		myRequestPartitionId = RequestPartitionId.fromPartitionId(null);

		mySearchEntity = new Search();
		mySearchEntity.setUuid(UUID.randomUUID().toString());
		mySearchEntity.setResourceType("Patient");
		mySearchEntity.setSearchType(SearchTypeEnum.SEARCH);
		mySearchEntity.setCreated(new Date());
		mySearchEntity.setStatus(SearchStatusEnum.LOADING);
		mySearchEntity.setSearchParameterMap(myParams);

		lenient().when(mySearchBuilderFactory.newSearchBuilder(anyString(), any())).thenReturn(mySearchBuilder);

		myBundleProvider = new TestCacheAwareJpaSearchBundleProvider(
			myFhirContext,
			myRequestDetails,
			myInterceptorBroadcaster,
			myPagingProvider,
			myStorageSettings,
			myEntityManager,
			myTxService,
			myRequestPartitionHelperSvc,
			mySearchCacheSvc,
			mySearchResultCacheSvc,
			myExceptionService,
			mySearchBuilderFactory,
			myParams,
			myRequestPartitionId,
			mySearchEntity);
	}

	@AfterEach
	void tearDown() {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.clearSynchronization();
		}
		TransactionSynchronizationManager.setActualTransactionActive(false);
	}

	@Test
	void testBasicProperties() {
		assertThat(myBundleProvider.isShouldFetchResourcesBeforeOtherProperties()).isTrue();
		assertThat(myBundleProvider.preferredPageSize()).isNull();

		mySearchEntity.setPreferredPageSize(25);

		mockPerformSearchForPids(1);
		mockLoadResourcesByPid();

		ResponsePage.ResponsePageBuilder responsePageBuilder = new ResponsePage.ResponsePageBuilder();
		myBundleProvider.getResources(0, 1, responsePageBuilder);

		assertThat(myBundleProvider.preferredPageSize()).isEqualTo(25);
		assertThat(myBundleProvider.getUuid()).isEqualTo(mySearchEntity.getUuid());

		IPrimitiveType<Date> published = myBundleProvider.getPublished();
		assertThat(published).isNotNull();
		assertThat(published.getValue()).isEqualTo(mySearchEntity.getCreated());

		assertThat(myBundleProvider.getCacheStatus()).isNull();
	}

	@Test
	void testSize_WhenSearchEntityNotLoaded_TriggersInitialization() {
		myParams.setCount(15);
		myParams.setOffset(5);
		when(myPagingProvider.getDefaultPageSize()).thenReturn(10);

		mockPerformSearchForPids(1);

		Integer size = myBundleProvider.size();

		assertThat(size).isEqualTo(1);
		assertEquals(1, myTxService.getTransactionCount());
	}

	@Test
	void testGetAllResources_Success() {
		mockPerformSearchForPids(2);
		mockLoadResourcesByPid();

		List<IBaseResource> allResources = myBundleProvider.getAllResources();

		assertThat(allResources).hasSize(2);
		assertThat(allResources.get(0).getIdElement().getIdPart()).isEqualTo("0");
		assertThat(allResources.get(1).getIdElement().getIdPart()).isEqualTo("1");
	}

	@Test
	void testGetAllResources_ExceedsLimit_ThrowsException() {
		mockPerformSearchForPids(10_000);
		mockLoadResourcesByPid();

		assertThatThrownBy(() -> myBundleProvider.getAllResources())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Can not call getAllResources on a collection >= 10000 resources");
	}

	@Test
	void testGetResources_BasicSearch_Success() {
		mockPerformSearchForPids(2);
		mockLoadResourcesByPid();

		myInterceptorBroadcaster.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, myAnonymousInterceptor);

		ResponsePage.ResponsePageBuilder responsePageBuilder = mock(ResponsePage.ResponsePageBuilder.class);
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10, responsePageBuilder);

		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactly(
			"Patient/0", "Patient/1"
		);
		verify(responsePageBuilder).setPageSize(2);
		verify(responsePageBuilder).setOmittedResourceCount(0);
		verify(responsePageBuilder).setIncludedResourceCount(0);
		verify(responsePageBuilder).setTotalRequestedResourcesFetched(2);
		assertThat(mySearchEntity.getStatus()).isEqualTo(SearchStatusEnum.FINISHED);
		assertThat(mySearchEntity.getTotalCount()).isEqualTo(2);

		verify(mySearchCacheSvc, times(1)).save(eq(mySearchEntity), eq(myRequestPartitionId));
		verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE), any());

		verify(mySearchResultCacheSvc, times(1))
			.storeResults(eq(mySearchEntity), eq(List.of()), myPidListCaptor.capture(), eq(myRequestDetails), eq(myRequestPartitionId));
		assertThat(myPidListCaptor.getValue()).containsExactly(
			JpaPid.fromId(0L), JpaPid.fromId(1L)
		);
	}

	@Test
	void testGetResources_PassComplete_WhenMoreResultsAvailable() {
		// Setup

		mockPerformSearchForPids(31);
		mockLoadResourcesByPid();

		myInterceptorBroadcaster.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, myAnonymousInterceptor);
		myInterceptorBroadcaster.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, myAnonymousInterceptor);

		ResponsePage.ResponsePageBuilder responsePageBuilder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10, responsePageBuilder);

		assertThat(resources).hasSize(10);
		assertThat(mySearchEntity.getStatus()).isEqualTo(SearchStatusEnum.PASSCMPLET);
		verify(myAnonymousInterceptor, never()).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE), any());
		verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE), any());
	}

	@Test
	void testGetResources_PassComplete_WithTotalCountQuery() {
		// Setup

		myParams.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		mockPerformSearchForPids(31);
		List<String> loadedResources = mockLoadResourcesByPid();

		when(mySearchBuilder.createCountQuery(eq(myParams), eq(mySearchEntity.getUuid()), eq(myRequestDetails), eq(myRequestPartitionId)))
			.thenReturn(150L);

		// Test

		myBundleProvider.getResources(0, 10);

		// Verify

		assertThat(mySearchEntity.getStatus()).isEqualTo(SearchStatusEnum.PASSCMPLET);
		assertThat(mySearchEntity.getTotalCount()).isEqualTo(150);
		assertThat(loadedResources).containsExactly(generateIdRange(0, 10));
	}

	@Test
	void testGetResources_CachedRangeReuse_ExactRange() {
		mockPerformSearchForPids(1);
		mockLoadResourcesByPid();

		ResponsePage.ResponsePageBuilder builder1 = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> firstCall = myBundleProvider.getResources(0, 10, builder1);

		assertThat(toUnqualifiedVersionlessIdValues(firstCall)).containsExactly("Patient/0");
		assertEquals(1, myTxService.getTransactionCount());

		ResponsePage.ResponsePageBuilder builder2 = new ResponsePage.ResponsePageBuilder();
		myBundleProvider.getResources(0, 10, builder2);

		assertThat(toUnqualifiedVersionlessIdValues(firstCall)).containsExactly("Patient/0");
		// Still only 1 transaction execution because cached range was reused
		assertEquals(1, myTxService.getTransactionCount());
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 4, 5, 6, 8, 10})
	void testGetResources_CachedRangeReuse_FinishedSearchWithSmallerTotalCount(int theRequestTo) {
		// Mock search
		mockPerformSearchForPids(5);
		mockLoadResourcesByPid();

		assertThat(toUnqualifiedVersionlessIdValues(myBundleProvider.getResources(0, 10))).containsExactly(generateIdRange(0, 5));
		assertEquals(1, myTxService.getTransactionCount());

		// Test
		List<IBaseResource> secondCall = myBundleProvider.getResources(0, theRequestTo);
		assertThat(toUnqualifiedVersionlessIdValues(secondCall)).containsExactly(generateIdRange(0, Math.min(5, theRequestTo)));

		// No further transactions
		assertEquals(1, myTxService.getTransactionCount());
	}

	@Test
	void testGetResources_CachedRangeSubSliceReuse_NoIncludes() {
		mockPerformSearchForPids(5);
		mockLoadResourcesByPid();

		ResponsePage.ResponsePageBuilder builder1 = new ResponsePage.ResponsePageBuilder();
		myBundleProvider.getResources(0, 5, builder1);
		assertEquals(1, myTxService.getTransactionCount());

		// Sub-slice (1 to 3) within cached range (0 to 5)
		ResponsePage.ResponsePageBuilder builder2 = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> subSlice = myBundleProvider.getResources(1, 3, builder2);

		assertThat(subSlice).hasSize(2);
		assertThat(subSlice.get(0).getIdElement().getIdPart()).isEqualTo("1");
		assertThat(subSlice.get(1).getIdElement().getIdPart()).isEqualTo("2");
		// No additional transaction execution
		assertEquals(1, myTxService.getTransactionCount());
	}

	@SuppressWarnings("resource")
	@Test
	void testGetResources_WantOnlyCount_AlreadyKnown() {
		myParams.setSummaryMode(SummaryEnum.COUNT);
		mySearchEntity.setTotalCount(99);

		// Test
		assertEquals(99, myBundleProvider.size());
		assertThat(myBundleProvider.getResources(0, 10)).isEmpty();

		// No additional transaction because search entity was already loaded with totalCount known
		assertEquals(1, myTxService.getTransactionCount());
		verify(mySearchBuilder, never()).createCountQuery(any(), any(), any(), any());
		verify(mySearchBuilder, never()).createQuery(any(), any(), any(), any());
		verify(mySearchBuilder, never()).createQueryStream(any(), any(), any(), any());
	}

	@Test
	void testGetResources_WantOnlyCount_CalculatesCountInTx() {
		myParams.setSummaryMode(SummaryEnum.COUNT);
		mySearchEntity.setTotalCount(null);

		when(mySearchBuilder.createCountQuery(eq(myParams), eq(mySearchEntity.getUuid()), eq(myRequestDetails), eq(myRequestPartitionId)))
			.thenReturn(88L);

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10, builder);

		assertThat(resources).isEmpty();
		assertThat(mySearchEntity.getTotalCount()).isEqualTo(88);
		assertThat(mySearchEntity.getStatus()).isEqualTo(SearchStatusEnum.FINISHED);
		verify(mySearchCacheSvc, times(1)).save(eq(mySearchEntity), eq(myRequestPartitionId));
	}

	@Test
	void testGetResources_ExistingSearchInDbCache_ReturnsExistingPids() {
		mySearchEntity.setStatus(SearchStatusEnum.FINISHED);
		mySearchEntity.setNumFound(2);

		JpaPid pid1 = JpaPid.fromId(1L);
		JpaPid pid2 = JpaPid.fromId(2L);
		Patient p1 = (Patient) buildPatient(withId("1"));
		Patient p2 = (Patient) buildPatient(withId("2"));

		when(mySearchResultCacheSvc.fetchResultPids(eq(mySearchEntity), eq(0), eq(10), eq(myRequestDetails), eq(myRequestPartitionId)))
			.thenReturn(new ArrayList<>(List.of(pid1, pid2)));

		doAnswer(invocation -> {
			List<IBaseResource> list = invocation.getArgument(2);
			list.add(p1);
			list.add(p2);
			return null;
		}).when(mySearchBuilder).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10, builder);

		assertThat(resources).containsExactly(p1, p2);
		verify(mySearchBuilder, never()).performSearchForPids(any(), any(), any(), any(), any());
	}

	@Test
	void testGetResources_IncrementalContinuation_WhenNumFoundGreaterThanZero() {
		mySearchEntity.setStatus(SearchStatusEnum.PASSCMPLET);
		mySearchEntity.setNumFound(2);

		JpaPid pid1 = JpaPid.fromId(1L);
		JpaPid pid2 = JpaPid.fromId(2L);
		JpaPid pid3 = JpaPid.fromId(3L);
		Patient p3 = (Patient) buildPatient(withId("3"));

		when(mySearchResultCacheSvc.fetchAllResultPids(eq(mySearchEntity), eq(myRequestDetails), eq(myRequestPartitionId)))
			.thenReturn(List.of(pid1, pid2));

		doAnswer(invocation -> {
			ISearchResultConsumer<JpaPid> consumer = invocation.getArgument(0);
			SearchProgressTracker tracker = new SearchProgressTracker(0, 0);
			consumer.consume(tracker, pid3);
			return tracker;
		}).when(mySearchBuilder).performSearchForPids(any(), any(), any(), any(), any());

		doAnswer(invocation -> {
			List<IBaseResource> list = invocation.getArgument(2);
			list.add(p3);
			return null;
		}).when(mySearchBuilder).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> resources = myBundleProvider.getResources(2, 5, builder);

		assertThat(resources).containsExactly(p3);
		verify(mySearchBuilder).setPreviouslyAddedResourcePids(eq(List.of(pid1, pid2)));
	}

	@Test
	void testGetResources_WithIncludesAndRevIncludes() {
		SearchInclude nonIterateRevInclude = new SearchInclude(mySearchEntity, "Observation:subject", true, false);
		SearchInclude nonIterateInclude = new SearchInclude(mySearchEntity, "Patient:organization", false, false);
		SearchInclude iterateRevInclude = new SearchInclude(mySearchEntity, "DiagnosticReport:result", true, true);
		SearchInclude iterateInclude = new SearchInclude(mySearchEntity, "Organization:partof", false, true);

		mySearchEntity.getIncludes().addAll(List.of(nonIterateRevInclude, nonIterateInclude, iterateRevInclude, iterateInclude));

		JpaPid revIncludePid = JpaPid.fromId(10L);
		JpaPid includePid = JpaPid.fromId(20L);
		JpaPid iterRevIncludePid = JpaPid.fromId(30L);
		JpaPid iterIncludePid = JpaPid.fromId(40L);

		mockPerformSearchForPids(1);
		mockLoadResourcesByPid(Map.of(
			revIncludePid, buildObservation(withId("10")),
			includePid, buildOrganization(withId("20")),
			iterRevIncludePid, buildObservation(withId("30")),
			iterIncludePid, buildOrganization(withId("40"))
		));

		when(mySearchBuilder.loadIncludes(any())).thenAnswer(invocation -> {
			SearchBuilderLoadIncludesParameters<JpaPid> params = invocation.getArgument(0);
			if (params.isReverseMode() && !params.getIncludeFilters().isEmpty()) {
				Include inc = params.getIncludeFilters().iterator().next();
				if ("Observation:subject".equals(inc.getValue())) {
					return Set.of(revIncludePid);
				} else if ("DiagnosticReport:result".equals(inc.getValue())) {
					return Set.of(iterRevIncludePid);
				}
			} else if (!params.isReverseMode() && !params.getIncludeFilters().isEmpty()) {
				Include inc = params.getIncludeFilters().iterator().next();
				if ("Patient:organization".equals(inc.getValue())) {
					return Set.of(includePid);
				} else if ("Organization:partof".equals(inc.getValue())) {
					return Set.of(iterIncludePid);
				}
			}
			return Set.of();
		});

		// Test
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactlyInAnyOrder(
			"Patient/0",
			"Observation/10",
			"Organization/20",
			"Observation/30",
			"Organization/40"
		);
	}

	@Test
	void testGetResources_WithStoragePreAccessResourcesInterceptor_BlocksResource() {
		// Setup

		mockPerformSearchForPids(2);
		mockLoadResourcesByPid();

		// Add a PREACCESS interceotor that blocks Patient/0
		IAnonymousInterceptor interceptor = (pointcut, args) -> {
			IPreResourceAccessDetails accessDetails = args.get(IPreResourceAccessDetails.class);
			assertEquals("0", accessDetails.getResource(0).getIdElement().getIdPart());
			accessDetails.setDontReturnResourceAtIndex(0);
		};
		myInterceptorBroadcaster.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Test

		List<IBaseResource> resources = myBundleProvider.getResources(0, 10);

		// Verify

		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactly("Patient/1");
		assertThat(mySearchEntity.getNumBlocked()).isEqualTo(1);
		assertThat(mySearchEntity.getNumFound()).isEqualTo(1);
	}

	@Test
	void testSearchThrowsException_MarksSearchAsFailedAndThrows() {
		doAnswer(invocation -> {
			throw new RuntimeException("Search failed in DB");
		}).when(mySearchBuilder).performSearchForPids(any(), any(), any(), any(), any());

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		assertThatThrownBy(() -> myBundleProvider.getResources(0, 10, builder))
			.isInstanceOf(InternalErrorException.class);

		assertThat(mySearchEntity.getStatus()).isEqualTo(SearchStatusEnum.FAILED);
		assertThat(mySearchEntity.getFailureMessage()).contains("Search failed in DB");
		verify(mySearchCacheSvc).save(eq(mySearchEntity), eq(myRequestPartitionId));
	}

	@Test
	void testRetryOnResourceVersionConflictException_MaxRetriesExceeded_ThrowsException() {
		myTxService.setBeforeExecuteCallback(() -> {
			throw new ResourceVersionConflictException("Optimistic lock conflict");
		});

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		assertThatThrownBy(() -> myBundleProvider.getResources(0, 10, builder))
			.isInstanceOf(ResourceVersionConflictException.class);
	}

	@Test
	void testResourceGoneException_RethrownDirectly() {
		myTxService.setBeforeExecuteCallback(() -> {
			throw new ResourceGoneException("Resource is gone");
		});

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		assertThatThrownBy(() -> myBundleProvider.getResources(0, 10, builder))
			.isInstanceOf(ResourceGoneException.class)
			.hasMessageContaining("Resource is gone");
	}

	@Test
	void testUnexpectedRollbackException_ValidatesSearchEntity() {
		mockPerformSearchForPids(1);
		mockLoadResourcesByPid();

		// First pass loads the search entity
		ResponsePage.ResponsePageBuilder builder1 = new ResponsePage.ResponsePageBuilder();
		myBundleProvider.getResources(0, 1, builder1);

		// Mark search entity as failed
		mySearchEntity.setStatus(SearchStatusEnum.FAILED);
		mySearchEntity.setFailureCode(500);
		mySearchEntity.setFailureMessage("Custom Failure");

		myTxService.setBeforeExecuteCallback(() -> {
			throw new UnexpectedRollbackException("Transaction rolled back");
		});

		ResponsePage.ResponsePageBuilder builder2 = new ResponsePage.ResponsePageBuilder();
		assertThatThrownBy(() -> myBundleProvider.getResources(1, 5, builder2))
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("Custom Failure");
	}

	@Test
	void testSearchExpiryUpdated_WhenMoreThanHalfwayToExpire() {
		long expireAfterMillis = 60 * 60 * 1000L; // 60 minutes
		myStorageSettings.setExpireSearchResultsAfterMillis(expireAfterMillis);
		// Created 50 minutes ago -> cutoff is 10 minutes from now, which is < 30 minutes (halfway)
		Date created = new Date(System.currentTimeMillis() - (50 * 60 * 1000L));
		mySearchEntity.setCreated(created);

		mockPerformSearchForPids(1);
		mockLoadResourcesByPid();

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		myBundleProvider.getResources(0, 10, builder);

		assertThat(mySearchEntity.getExpiryOrNull()).isNotNull();
		assertThat(mySearchEntity.getExpiryOrNull()).isAfter(new Date());
	}

	@Test
	void testSearchExpiryNotUpdated_WhenLessThanHalfwayToExpire() {
		long expireAfterMillis = 60 * 60 * 1000L; // 60 minutes
		myStorageSettings.setExpireSearchResultsAfterMillis(expireAfterMillis);
		// Created 5 minutes ago -> cutoff is 55 minutes from now, which is > 30 minutes (halfway)
		Date created = new Date(System.currentTimeMillis() - (5 * 60 * 1000L));
		mySearchEntity.setCreated(created);
		mySearchEntity.setExpiryOrNull(null);

		mockPerformSearchForPids(1);
		mockLoadResourcesByPid();

		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		myBundleProvider.getResources(0, 10, builder);

		assertThat(mySearchEntity.getExpiryOrNull()).isNull();
	}

	/**
	 * Ensure that even if we load many PIDs, we don't blow out the available
	 * RAM by holding a large number of hydrated resources in memory at any
	 * one time.
	 */
	@Test
	void testFetchManyPids_MaintainSmallLocalMemoryCache_NoConsentService() {
		// Setup

		myStorageSettings.setSearchPreFetchThresholds(List.of(10, -1));

		mockPerformSearchForPids(10_000);
		AtomicReference<Search> search = mockSearchCacheStorage();
		mockSearchResultCacheStorage();
		List<String> fetchedResourceIds = mockLoadResourcesByPid();

		// Test

		/*
		 * Fetch the first page to hit the first (bounded) threshold
		 */

		myBundleProvider.setFetchedResourceLocalCacheMaximumSize(10);
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10);
		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactly(generateIdRange(0, 10));
		verify(mySearchBuilder, times(1)).performSearchForPids(any(), any(), any(), any(), any());
		verify(mySearchBuilder, times(1)).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());
		assertEquals(11, search.get().getNumFound());
		assertEquals(SearchStatusEnum.PASSCMPLET, search.get().getStatus());

		// Only the resources actually being returned should be fetched
		assertEquals(10, myBundleProvider.getFetchedResourceCacheSize());
		assertThat(fetchedResourceIds).containsExactly(generateIdRange(0, 10));
		fetchedResourceIds.clear();

		/*
		 * Fetch the second page to hit the second (unbounded) threshold
		 */

		myBundleProvider.setFetchedResourceLocalCacheMaximumSize(15);
		resources = myBundleProvider.getResources(10, 25);
		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactly(generateIdRange(10, 25));
		verify(mySearchBuilder, times(2)).performSearchForPids(any(), any(), any(), any(), any());
		verify(mySearchBuilder, times(2)).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());
		assertEquals(10_000, search.get().getNumFound());
		assertEquals(SearchStatusEnum.FINISHED, search.get().getStatus());

		// Only the resources actually being returned should be fetched
		assertEquals(15, myBundleProvider.getFetchedResourceCacheSize());
		assertThat(fetchedResourceIds).containsExactly(generateIdRange(10, 25));
	}

	/**
	 * Ensure that even if we load many PIDs, we don't blow out the available
	 * RAM by holding a large number of hydrated resources in memory at any
	 * one time.
	 */
	@Test
	void testFetchManyPids_MaintainSmallLocalMemoryCache_WithConsentService() {
		// Setup

		myStorageSettings.setSearchPreFetchThresholds(List.of(10, -1));

		mockPerformSearchForPids(10_000);
		AtomicReference<Search> search = mockSearchCacheStorage();
		mockSearchResultCacheStorage();
		List<String> fetchedResourceIds = mockLoadResourcesByPid();

		myInterceptorBroadcaster.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, myMockInterceptor);
		myInterceptorBroadcaster.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, myMockInterceptor);

		// Test

		/*
		 * Fetch the first page to hit the first (bounded) threshold
		 */

		myBundleProvider.setFetchedResourceLocalCacheMaximumSize(10);
		List<IBaseResource> resources = myBundleProvider.getResources(0, 10);
		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactly(generateIdRange(0, 10));
		verify(mySearchBuilder, times(1)).performSearchForPids(any(), any(), any(), any(), any());
		verify(mySearchBuilder, times(1)).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());
		assertEquals(11, search.get().getNumFound());
		assertEquals(SearchStatusEnum.PASSCMPLET, search.get().getStatus());

		// Only the resources actually being returned should be fetched
		assertEquals(10, myBundleProvider.getFetchedResourceCacheSize());
		assertThat(fetchedResourceIds).containsExactly(generateIdRange(0, 11));
		fetchedResourceIds.clear();

		/*
		 * Fetch the second page to hit the second (unbounded) threshold
		 */

		myBundleProvider.setFetchedResourceLocalCacheMaximumSize(15);
		resources = myBundleProvider.getResources(10, 25);
		assertThat(toUnqualifiedVersionlessIdValues(resources)).containsExactly(generateIdRange(10, 25));
		verify(mySearchBuilder, times(2)).performSearchForPids(any(), any(), any(), any(), any());
		verify(mySearchBuilder, times(102)).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());
		assertEquals(10_000, search.get().getNumFound());
		assertEquals(SearchStatusEnum.FINISHED, search.get().getStatus());

		// Only the resources actually being returned should be fetched
		assertEquals(15, myBundleProvider.getFetchedResourceCacheSize());
		assertThat(fetchedResourceIds).containsExactlyInAnyOrder(generateIdRange(10, 10_000));

		// Make sure we fetched resources in small batches
		verify(mySearchBuilder, atLeastOnce()).loadResourcesByPid(myPidListCaptor.capture(), anyList(), anyList(), anyBoolean(), any());
		Set<JpaPid> allPids = new HashSet<>();
		for (Collection<JpaPid> nextPidList : myPidListCaptor.getAllValues()) {
			assertThat(nextPidList).hasSizeLessThanOrEqualTo(100);
			allPids.addAll(nextPidList);
		}
		assertEquals(10_000, allPids.size());

	}


	@Nonnull
	private List<String> mockLoadResourcesByPid() {
		return mockLoadResourcesByPid(Map.of());
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	private List<String> mockLoadResourcesByPid(Map<JpaPid, IBaseResource> theDirectMappings) {
		theDirectMappings.values().forEach(r->{
			assertTrue(r.getIdElement().hasResourceType());
			assertTrue(r.getIdElement().hasIdPart());
		});

		List<String> fetchedResourceIds = new ArrayList<>();
		doAnswer(invocation -> {
			List<JpaPid> pids = invocation.getArgument(0, List.class);

			List<IBaseResource> listToPopulate = invocation.getArgument(2, List.class);
			for (JpaPid nextPid : pids) {
				String resourceId;
				IBaseResource resource;

				if (theDirectMappings.containsKey(nextPid)) {
					resource = theDirectMappings.get(nextPid);
					resourceId = resource.getIdElement().toUnqualifiedVersionless().getValue();
				} else {
					resourceId = "Patient/" + nextPid.getId();
					resource = new Patient();
					resource.setId(resourceId);
				}

				fetchedResourceIds.add(resourceId);
				listToPopulate.add(resource);
			}
			ourLog.info("loadResourcesByPid() fetched PIDs {} - {}", pids.get(0).getId(), pids.get(pids.size() - 1).getId());
			return null;
		}).when(mySearchBuilder).loadResourcesByPid(anyList(), anyList(), anyList(), anyBoolean(), any());
		return fetchedResourceIds;
	}

	@SuppressWarnings("unchecked")
	private void mockSearchResultCacheStorage() {
		AtomicReference<List<JpaPid>> storedResults = new AtomicReference<>();
		doAnswer(invocation -> {
			List<JpaPid> results = invocation.getArgument(2, List.class);
			storedResults.set(results);
			return null;
		}).when(mySearchResultCacheSvc).storeResults(any(), anyList(), anyList(), any(), any());
		when(mySearchResultCacheSvc.fetchAllResultPids(any(), any(), any())).thenAnswer(invocation -> storedResults.get());
	}

	@Nonnull
	private AtomicReference<Search> mockSearchCacheStorage() {
		AtomicReference<Search> search = new AtomicReference<>();
		doAnswer(invocation -> {
			Search searchEntity = invocation.getArgument(0, Search.class);
			search.set(searchEntity);
			return null;
		}).when(mySearchCacheSvc).save(any(), any());
		return search;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void mockPerformSearchForPids(int theTotalPids) {
		AtomicReference<Set<JpaPid>> previouslyAddedPids = new AtomicReference<>();
		lenient().doAnswer(invocation -> {
			previouslyAddedPids.set(new HashSet<>(invocation.getArgument(0, List.class)));
			return null;
		}).when(mySearchBuilder).setPreviouslyAddedResourcePids(anyList());
		doAnswer(invocation -> {
			ISearchResultConsumer<JpaPid> consumer = invocation.getArgument(0);
			SearchProgressTracker tracker = new SearchProgressTracker(0, 0);
			Long firstReturnedPid = null;
			Long lastReturnedPid = null;
			for (long i = 0; i < theTotalPids; i++) {
				JpaPid pid = JpaPid.fromId(i);
				if (previouslyAddedPids.get() != null && previouslyAddedPids.get().contains(pid)) {
					continue;
				}
				firstReturnedPid = getIfNull(firstReturnedPid, i);
				lastReturnedPid = i;
				ISearchResultConsumer.Outcome outcome = consumer.consume(tracker, pid);
				if (!outcome.isContinue()) {
					consumer.consumptionComplete();
					ourLog.info("performSearchForPids() Returned PIDs {} - {}", firstReturnedPid, lastReturnedPid);
					return tracker;
				}
			}
			consumer.consumptionComplete();
			ourLog.info("performSearchForPids() Returned PIDs {} - {}", firstReturnedPid, lastReturnedPid);
			return tracker;
		}).when(mySearchBuilder).performSearchForPids(any(), any(), any(), any(), any());
	}

	private String[] generateIdRange(int theStartInclusive, int theEndExclusive) {
		List<String> retVal = new ArrayList<>();
		for (int i = theStartInclusive; i < theEndExclusive; i++) {
			retVal.add("Patient/" + i);
		}
		return retVal.toArray(EMPTY_STRING_ARRAY);
	}


	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	private static class TestCacheAwareJpaSearchBundleProvider extends BaseCacheAwareJpaSearchBundleProvider {
		private final Search mySearch;

		public TestCacheAwareJpaSearchBundleProvider(
			FhirContext theFhirContext,
			RequestDetails theRequestDetails,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IPagingProvider thePagingProvider,
			JpaStorageSettings theStorageSettings,
			EntityManager theEntityManager,
			IHapiTransactionService theTxService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			ExceptionService theExceptionService,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			SearchParameterMap theParams,
			RequestPartitionId theRequestPartitionId,
			Search theSearch) {
			super(
				theFhirContext,
				theRequestDetails,
				theInterceptorBroadcaster,
				thePagingProvider,
				theStorageSettings,
				theEntityManager,
				theTxService,
				theRequestPartitionHelperSvc,
				theSearchCacheSvc,
				theSearchResultCacheSvc,
				theExceptionService,
				theSearchBuilderFactory,
				theParams,
				theRequestPartitionId);
			mySearch = theSearch;
		}

		@Override
		protected Search provideSearchEntity() {
			return mySearch;
		}
	}
}
