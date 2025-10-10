package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.config.ThreadPoolFactoryConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.TransactionSemanticsHeader;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationKnowledge;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collection;
import java.util.List;

import static ca.uhn.fhir.jpa.test.BaseJpaTest.newSrd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TransactionProcessorTest.MyConfig.class)
public class TransactionProcessorTest {

	public static final String PRACTITIONER_MATCH_URL_FOO_123 = "Practitioner?identifier=http://foo|123";
	public static final long PRACTITIONER_MATCH_URL_FOO_123_HASH = 5166745101503280662L;
	public static final String PRACTITIONER_MATCH_URL_FOO_456 = "Practitioner?identifier=http://foo|456";
	public static final SearchParameterMap PRACTITIONER_MATCH_URL_FOO_123_SP_MAP = new SearchParameterMap()
		.add(Practitioner.SP_IDENTIFIER, new TokenParam("http://foo", "123"));
	public static final SearchParameterMap PRACTITIONER_MATCH_URL_FOO_456_SP_MAP = new SearchParameterMap()
		.add(Practitioner.SP_IDENTIFIER, new TokenParam("http://foo", "456"));
	private static final long PRACTITIONER_MATCH_URL_FOO_456_HASH = 9212997248395834704L;

	@RegisterExtension
	private final LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(BaseTransactionProcessor.class);

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessorTest.class);

	@Mock
	private IFhirResourceDao<Practitioner> myPractitionerDao;
	@Mock
	private IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	private TransactionProcessor myTransactionProcessor;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private NonTransactionalHapiTransactionService myHapiTransactionService;
	@MockBean
	private EntityManagerFactory myEntityManagerFactory;
	@MockBean(answer = Answers.RETURNS_DEEP_STUBS)
	private EntityManager myEntityManager;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private CriteriaBuilder myCriteriaBuilder;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Root<ResourceIndexedSearchParamToken> myRootToken;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private CriteriaQuery<Tuple> myCriteriaQuery;
	@Mock
	private Path myHashSystemAndValuePath;
	@MockBean
	private PlatformTransactionManager myPlatformTransactionManager;
	@MockBean
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlService;
	@MockBean
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	private IIdHelperService<JpaPid> myIdHelperService;
	@MockBean
	private PartitionSettings myPartitionSettings;
	@MockBean
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private SearchParamMatcher mySearchParamMatcher;
	@MockBean(answer = Answers.RETURNS_DEEP_STUBS)
	private SessionImpl mySession;
	@MockBean
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@MockBean
	private ResourceSearchUrlSvc myResourceSearchUrlSvc;
	@MockBean
	private MemoryCacheService myMemoryCacheService;
	@Captor
	private ArgumentCaptor<Long> myLongCaptor;
	@Captor
	private ArgumentCaptor<Collection<Long>> myLongCollectionCaptor;
	@Captor
	private ArgumentCaptor<ReadPartitionIdRequestDetails> myReadPartitionRequestDetailsCaptor;
	@Autowired
	private MatchUrlService myMatchUrlService;

	@BeforeEach
	void before() {
		myTransactionProcessor.setEntityManagerForUnitTest(myEntityManager);
		when(myEntityManager.unwrap(eq(Session.class))).thenReturn(mySession);

		when(myPractitionerDao.getResourceType()).thenReturn(Practitioner.class);
		when(myPractitionerDao.getContext()).thenReturn(myFhirContext);
		myDaoRegistry.register(myPractitionerDao);

		when(myPatientDao.getResourceType()).thenReturn(Patient.class);
		when(myPatientDao.getContext()).thenReturn(myFhirContext);
		myDaoRegistry.register(myPatientDao);
	}

	@AfterEach
	void after() {
		myHapiTransactionService.clearNonCompatiblePartitions();
	}


	@Test
	public void testTransactionWithDisabledResourceType() {

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		MedicationKnowledge medKnowledge = new MedicationKnowledge();
		medKnowledge.setStatus(MedicationKnowledge.MedicationKnowledgeStatus.ACTIVE);
		input
			.addEntry()
			.setResource(medKnowledge)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/MedicationKnowledge");

		try {
			myTransactionProcessor.transaction(null, input, false);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(544) + "Resource MedicationKnowledge is not supported on this server. Supported resource types: [Patient, Practitioner]", e.getMessage());
		}
	}


	@Test
	public void testAppropriatePartitionForConditionalDelete() {
		// Setup
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequest(any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionDeleteEntry("Patient?identifier=http://foo|123");
		Bundle input = bb.getBundleTyped();

		DeleteMethodOutcome value = new DeleteMethodOutcome();
		value.setDeletedEntities(List.of());
		when(myPatientDao.deleteByUrl(any(), any(), any(), any())).thenReturn(value);

		// Test
		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify
		verify(myRequestPartitionHelperSvc, times(1)).determineReadPartitionForRequest(any(), myReadPartitionRequestDetailsCaptor.capture());
		assertEquals(RestOperationTypeEnum.DELETE, myReadPartitionRequestDetailsCaptor.getValue().getRestOperationType());
		assertEquals("?identifier=http%3A//foo%7C123", myReadPartitionRequestDetailsCaptor.getValue().getSearchParams().toNormalizedQueryString(myFhirContext));
	}

	@Test
	public void testAppropriatePartitionForConditionalPatch() {
		// Setup
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequest(any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionFhirPatchEntry(new Parameters()).conditional("Patient?identifier=http://foo|123");
		Bundle input = bb.getBundleTyped();

		DaoMethodOutcome value = new DaoMethodOutcome();
		value.setId(new IdType("Patient", "123"));
		value.setCreated(false);
		when(myPatientDao.patchInTransaction(any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any())).thenReturn(value);

		// Test
		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify
		verify(myRequestPartitionHelperSvc, times(1)).determineReadPartitionForRequest(any(), myReadPartitionRequestDetailsCaptor.capture());
		assertEquals(RestOperationTypeEnum.PATCH, myReadPartitionRequestDetailsCaptor.getValue().getRestOperationType());
		assertEquals("?identifier=http%3A//foo%7C123", myReadPartitionRequestDetailsCaptor.getValue().getSearchParams().toNormalizedQueryString(myFhirContext));
	}



	@Test
	public void testTransactionWithAutoRetry() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.setType("batch");
		Patient p = new Patient();
		p.setId("P");
		bb.addTransactionUpdateEntry(p);
		Bundle input = bb.getBundleTyped();
		assertEquals(Bundle.BundleType.BATCH, input.getType());

		String headerValue = TransactionSemanticsHeader
			.newBuilder()
			.withRetryCount(2)
			.withMinRetryDelay(20)
			.withMaxRetryDelay(50)
			.withTryBatchAsTransactionFirst(true)
			.build()
			.toHeaderValue();
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.addHeader(TransactionSemanticsHeader.HEADER_NAME, headerValue);

		when(myPatientDao.update(any(), any(), anyBoolean(), anyBoolean(), any(), any())).thenAnswer(t -> {
			throw new InternalErrorException("Expected error");
		});

		// Test
		Bundle outcome = myTransactionProcessor.transaction(requestDetails, input, false);
		ourLog.info("Response: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		verify(myPatientDao, times(3)).update(any(), any(), anyBoolean(), anyBoolean(), any(), any());

		TransactionUtil.TransactionResponse resp = TransactionUtil.parseTransactionResponse(myFhirContext, input, outcome);
		assertEquals("Expected error", resp.getStorageOutcomes().get(0).getErrorMessage());
		assertEquals(500, resp.getStorageOutcomes().get(0).getStatusCode());

		List<ILoggingEvent> logs = myLogbackTestExtension.getLogEvents(t -> t.getMessage().startsWith("Transaction processing attempt"));
		assertThat(logs.get(0).getFormattedMessage()).matches("Transaction processing attempt 1/3 failed. Sleeping for [0-9]+ ms. $");
		assertThat(logs.get(1).getFormattedMessage()).matches("Transaction processing attempt 2/3 failed. Sleeping for [0-9]+ ms. Performing final retry using batch semantics. $");
	}


	@Test
	public void testPreFetch_CollapseDuplicates() {
		// Setup
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		Bundle input = bb.getBundleTyped();

		mockPreFetchHashCapture();
		mockPatientDaoCreate();

		// Test

		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify

		// Only 1 pre-fetch covering all 3 URLs
		verify(myEntityManager, times(1)).createQuery(anyCriteriaQuery());
		verify(myCriteriaBuilder, times(1)).equal(any(), myLongCaptor.capture());
		assertEquals(PRACTITIONER_MATCH_URL_FOO_123_HASH, myLongCaptor.getValue());
	}

	@Test
	public void testPreFetch_ConditionalCreate_IncludePayloadInPartitionRequest() {
		// Setup
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);
		when(myRequestPartitionHelperSvc.determineCreatePartitionForRequest(any(), any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), any(), any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(
			new Patient().setActive(true)
		).conditional("Patient?identifier=http://foo|123");
		Bundle input = bb.getBundleTyped();

		when(myInMemoryResourceMatcher.canBeEvaluatedInMemory(any())).thenReturn(InMemoryMatchResult.unsupportedFromReason("Foo"));

		mockPreFetchHashCapture();
		mockPatientDaoCreate();

		// Test

		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify
		verify(myRequestPartitionHelperSvc, times(1)).determineCreatePartitionForRequest(any(), notNull(), eq("Patient"));
		verify(myRequestPartitionHelperSvc, times(1)).determineReadPartitionForRequestForSearchType(any(), eq("Patient"), any(), notNull());
		verify(myRequestPartitionHelperSvc, atLeastOnce()).isDefaultPartition(any());
		verifyNoMoreInteractions(myRequestPartitionHelperSvc);

		// Only 1 pre-fetch covering all 3 URLs
		verify(myEntityManager, times(1)).createQuery(anyCriteriaQuery());
		verify(myCriteriaBuilder, times(1)).equal(any(), myLongCaptor.capture());
		assertEquals(-4132452001562191669L, myLongCaptor.getValue());
	}

	@Test
	public void testPreFetch_ConditionalUpdate_IncludePayloadInPartitionRequest() {
		// Setup
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);
		when(myRequestPartitionHelperSvc.determineCreatePartitionForRequest(any(), any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), any(), any(), any())).thenReturn(RequestPartitionId.fromPartitionId(100));

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(
			new Patient().setActive(true)
		).conditional("Patient?identifier=http://foo|123");
		Bundle input = bb.getBundleTyped();

		when(myInMemoryResourceMatcher.canBeEvaluatedInMemory(any())).thenReturn(InMemoryMatchResult.unsupportedFromReason("Foo"));

		mockPreFetchHashCapture();
		mockPatientDaoUpdate();

		// Test

		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify
		verify(myRequestPartitionHelperSvc, times(1)).determineCreatePartitionForRequest(any(), notNull(), eq("Patient"));
		verify(myRequestPartitionHelperSvc, times(1)).determineReadPartitionForRequestForSearchType(any(), eq("Patient"), any(), notNull());
		verify(myRequestPartitionHelperSvc, atLeastOnce()).isDefaultPartition(any());
		verifyNoMoreInteractions(myRequestPartitionHelperSvc);

		// Only 1 pre-fetch covering all 3 URLs
		verify(myEntityManager, times(1)).createQuery(anyCriteriaQuery());
		verify(myCriteriaBuilder, times(1)).equal(any(), myLongCaptor.capture());
		assertEquals(-4132452001562191669L, myLongCaptor.getValue());
	}

	@Test
	public void testPreFetch_CollapseDuplicates_MultiplePartitions_CompatiblePartitions() {
		// Setup
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_456)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_456)));
		Bundle input = bb.getBundleTyped();

		when(myRequestPartitionHelperSvc.determineCreatePartitionForRequest(any(), any(), eq("Patient"))).thenReturn(RequestPartitionId.fromPartitionId(100));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), eq("Practitioner"), eq(PRACTITIONER_MATCH_URL_FOO_123_SP_MAP))).thenReturn(RequestPartitionId.fromPartitionId(123));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), eq("Practitioner"), eq(PRACTITIONER_MATCH_URL_FOO_456_SP_MAP))).thenReturn(RequestPartitionId.fromPartitionId(456));

		mockPreFetchHashCapture();
		mockPatientDaoCreate();

		// Test

		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify

		// One pre-fetch for each partition
		verify(myEntityManager, times(1)).createQuery(anyCriteriaQuery());

		verify(myHashSystemAndValuePath, times(1)).in(myLongCollectionCaptor.capture());
		Collection<Long> systemAndValuePreFetchHashes = myLongCollectionCaptor.getValue();
		assertThat(systemAndValuePreFetchHashes).containsExactlyInAnyOrder(
			PRACTITIONER_MATCH_URL_FOO_123_HASH,
			PRACTITIONER_MATCH_URL_FOO_456_HASH
		);
	}

	@Test
	public void testPreFetch_CollapseDuplicates_MultiplePartitions_NonCompatiblePartitions() {
		// Setup
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_123)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_456)));
		bb.addTransactionCreateEntry(new Patient().addGeneralPractitioner(new Reference(PRACTITIONER_MATCH_URL_FOO_456)));
		Bundle input = bb.getBundleTyped();

		when(myRequestPartitionHelperSvc.determineCreatePartitionForRequest(any(), any(), eq("Patient"))).thenReturn(RequestPartitionId.fromPartitionId(100));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), eq("Practitioner"), eq(PRACTITIONER_MATCH_URL_FOO_123_SP_MAP))).thenReturn(RequestPartitionId.fromPartitionId(123));
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), eq("Practitioner"), eq(PRACTITIONER_MATCH_URL_FOO_456_SP_MAP))).thenReturn(RequestPartitionId.fromPartitionId(456));

		myHapiTransactionService.addNonCompatiblePartition(RequestPartitionId.fromPartitionId(123), RequestPartitionId.fromPartitionId(456));

		mockPreFetchHashCapture();
		mockPatientDaoCreate();

		// Test

		myTransactionProcessor.transaction(newSrd(), input, false);

		// Verify

		// One pre-fetch for each partition
		verify(myEntityManager, times(2)).createQuery(anyCriteriaQuery());
		verify(myCriteriaBuilder, times(2)).equal(any(), myLongCaptor.capture());

		assertThat(myLongCaptor.getAllValues()).containsExactlyInAnyOrder(
			PRACTITIONER_MATCH_URL_FOO_123_HASH,
			PRACTITIONER_MATCH_URL_FOO_456_HASH
		);
	}

	private void mockPreFetchHashCapture() {
		when(myEntityManager.getCriteriaBuilder()).thenReturn(myCriteriaBuilder);
		when(myCriteriaBuilder.createTupleQuery()).thenReturn(myCriteriaQuery);
		when(myCriteriaQuery.from(ResourceIndexedSearchParamToken.class)).thenReturn(myRootToken);
		when(myRootToken.get(eq("myHashSystemAndValue"))).thenReturn(myHashSystemAndValuePath);
	}

	private void mockPatientDaoCreate() {
		DaoMethodOutcome outcome = new DaoMethodOutcome();
		outcome.setId(new IdType("Patient/A"));
		outcome.setCreated(true);
		when(myPatientDao.create(any(), any(), anyBoolean(), any(), any())).thenReturn(outcome);
	}

	private void mockPatientDaoUpdate() {
		DaoMethodOutcome outcome = new DaoMethodOutcome();
		outcome.setId(new IdType("Patient/A/_history/1"));
		outcome.setCreated(true);
		when(myPatientDao.update(any(), any(), anyBoolean(), anyBoolean(), any(), any())).thenReturn(outcome);
	}

	private static CriteriaQuery<?> anyCriteriaQuery() {
		return any(CriteriaQuery.class);
	}

	@Configuration
	@Import(ThreadPoolFactoryConfig.class)
	public static class MyConfig {

		@Bean
		public DaoRegistry daoRegistry() {
			DaoRegistry retVal = new DaoRegistry(fhirContext());
			retVal.setResourceDaos(List.of());
			return retVal;
		}

		@Bean
		public ISearchParamRegistry myISearchParamRegistry(FhirContext theFhirContext) {
			return new FhirContextSearchParamRegistry(theFhirContext);
		}

		@Bean
		public MatchUrlService myMatchUrlService() {
			return new MatchUrlService();
		}

		@Bean
		public FhirContext fhirContext() {
			return FhirContext.forR4Cached();
		}

		@Bean
		public JpaStorageSettings storageSettings() {
			return new JpaStorageSettings();
		}

		@Bean
		public TransactionProcessor transactionProcessor() {
			return new TransactionProcessor();
		}

		@Bean
		public InterceptorService interceptorService() {
			return new InterceptorService();
		}

		@Bean
		public ITransactionProcessorVersionAdapter<Bundle, Bundle.BundleEntryComponent> versionAdapter() {
			return new TransactionProcessorVersionAdapterR4();
		}

		@Bean
		public IHapiTransactionService hapiTransactionService() {
			return new NonTransactionalHapiTransactionService();
		}
	}
}
