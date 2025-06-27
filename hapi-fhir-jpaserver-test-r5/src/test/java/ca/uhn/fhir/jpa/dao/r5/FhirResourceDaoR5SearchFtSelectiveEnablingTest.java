package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionRequest;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionResponse;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.fhir.docs.interceptor.FullTextSelectiveIndexingInterceptor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SynchronizationType;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.projection.dsl.SearchProjectionFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = TestHSearchAddInConfig.DefaultLuceneHeap.class)
@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5SearchFtSelectiveEnablingTest extends BaseJpaR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5SearchFtSelectiveEnablingTest.class);

	@Mock
	private IAnonymousInterceptor myAnonymousInterceptor;
	@Autowired
	private EntityManagerFactory myEntityManagerFactory;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setHibernateSearchIndexFullText(true);
		mySearchParamRegistry.forceRefresh();
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
	}

	@Test
	public void testMassageContent() {
		// Setup
		MyInterceptor interceptor = new MyInterceptor();
		interceptor.replaceContentPayload("hello fake_payload_123 goodbye");
		registerInterceptor(interceptor);

		IIdType id = createPatient(withFamily("Simpson"), withGiven("Homer"));

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(PARAM_CONTENT, new StringParam("fake_payload_123"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactly(id.toUnqualifiedVersionless().getValue());
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT));
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT));
	}

	@Test
	public void testDoNotIndexContent() {
		// Setup
		assertEquals(0, getTotalFullTextIndexedEntities());
		MyInterceptor interceptor = new MyInterceptor();
		interceptor.doNotIndexContent();
		registerInterceptor(interceptor);

		createPatient(withNarrative("<div>HELLO</div>"));

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(PARAM_CONTENT, new StringParam("simpson"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).isEmpty();
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT));
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT));
		assertEquals(1, getTotalFullTextIndexedEntities());
	}

	@Test
	public void testMassageText() {
		// Setup
		MyInterceptor interceptor = new MyInterceptor();
		interceptor.replaceTextPayload("fake_payload_123 goodbye");
		registerInterceptor(interceptor);

		IIdType id = createPatient(withNarrative("<div>HELLO</div>"));

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(PARAM_TEXT, new StringParam("goodbye"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactly(id.toUnqualifiedVersionless().getValue());
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT));
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT));
	}

	@Test
	public void testDoNotIndexText() {
		// Setup
		assertEquals(0, getTotalFullTextIndexedEntities());
		MyInterceptor interceptor = new MyInterceptor();
		interceptor.doNotIndexText();
		registerInterceptor(interceptor);

		createPatient(withNarrative("<div>HELLO</div>"));

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(PARAM_TEXT, new StringParam("hello"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).isEmpty();
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT));
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT));
		assertEquals(1, getTotalFullTextIndexedEntities());
	}

	@Test
	public void testDoNotIndexContentOrText() {
		// Setup
		assertEquals(0, getTotalFullTextIndexedEntities());
		MyInterceptor interceptor = new MyInterceptor();
		interceptor.doNotIndexContent();
		interceptor.doNotIndexText();
		registerInterceptor(interceptor);

		createPatient(withNarrative("<div>HELLO</div>"), withFamily("Simpson"));

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(PARAM_CONTENT, new StringParam("simpson"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).isEmpty();
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT));
		assertEquals(1, interceptor.getInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT));
		assertEquals(0, getTotalFullTextIndexedEntities());
	}


	@Test
	public void testRequest_Create() {
		// Setup
		doAnswer(t -> {
			// Verify (this gets called by the framework after the "Test" action below is called)
			FullTextExtractionRequest request = t.getArgument(1, HookParams.class).get(FullTextExtractionRequest.class);
			assertNotNull(request.getResource());
			assertNull(request.getResourceId());
			assertThat(request.getDefaultString()).contains("Simpson", "Homer");
			assertFalse(request.isDelete());
			return null;
		}).when(myAnonymousInterceptor).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT), any());
		assertEquals(0, getTotalFullTextIndexedEntities());

		// Test
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT, myAnonymousInterceptor);
		createPatient(withActiveTrue(), withFamily("Simpson"), withGiven("Homer"), withNarrative("<div>HELLO</div>"));

		// Verify
		verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT), any());
		assertEquals(1, getTotalFullTextIndexedEntities());
	}

	@Test
	public void testRequest_Update() {
		// Setup
		IIdType id = createPatient(withActiveTrue());
		doAnswer(t -> {
			// Verify (this gets called by the framework after the "Test" action below is called)
			FullTextExtractionRequest request = t.getArgument(1, HookParams.class).get(FullTextExtractionRequest.class);
			assertNotNull(request.getResource());
			assertNotNull(request.getResourceId());
			assertEquals(id.getValue(), request.getResourceId().toUnqualifiedVersionless().getValue());
			assertThat(request.getDefaultString()).contains("Simpson", "Homer");
			assertFalse(request.isDelete());
			return null;
		}).when(myAnonymousInterceptor).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT), any());

		// Test
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT, myAnonymousInterceptor);
		createPatient(withId(id.getIdPart()), withActiveTrue(), withFamily("Simpson"), withGiven("Homer"), withNarrative("<div>HELLO</div>"));

		// Verify
		verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT), any());
	}

	@Test
	public void testRequest_Delete() {
		// Setup
		createPatient(withId("P"), withActiveTrue());

		doAnswer(t -> {
			// Verify (this gets called by the framework after the "Test" action below is called)
			FullTextExtractionRequest request = t.getArgument(1, HookParams.class).get(FullTextExtractionRequest.class);
			assertNull(request.getResource());
			assertNotNull(request.getResourceId());
			assertEquals("Patient/P", request.getResourceId().toUnqualifiedVersionless().getValue());
			assertNull(request.getDefaultString());
			assertTrue(request.isDelete());
			return null;
		}).when(myAnonymousInterceptor).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT), any());

		// Test
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT, myAnonymousInterceptor);
		myPatientDao.delete(new IdType("Patient/P"), mySrd);

		// Verify
		verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT), any());
	}

	/**
	 * Verify that the interceptor we use in the docs actually works as documented
	 */
	@Test
	public void testDocumentationExample_SelectiveEnablingInterceptor() {
		// Setup
		registerInterceptor(new FullTextSelectiveIndexingInterceptor());

		// Test
		createObservation(
			withObservationCategory("http://hl7.org/fhir/codesystem/Observation-category", "vital-signs", "Vital Signs"),
			withObservationValueString("Simpson")
		);
		IIdType observationId2 = createObservation(
			withObservationValueString("Simpson")
		);

		// Verify
		SearchParameterMap contentMap = SearchParameterMap.newSynchronous(PARAM_CONTENT, new StringParam("simpson"));
		List<String> actual = toUnqualifiedVersionlessIdValues(myObservationDao.search(contentMap, mySrd));
		assertThat(actual).containsExactly(observationId2.getValue());
	}

	@ParameterizedTest
	@CsvSource({
		// ParamName, CreateExplicitSp, SpBase       , SpStatus , ExpectMatch , ExpectIndexedDocCount
		"  _text    , false           ,              , active   , true        , 1 ",
		"  _text    , true            , Resource     , active   , true        , 1 ",
		"  _text    , true            , Patient      , active   , true        , 1 ",
		"  _text    , true            , Resource     , retired  , false       , 1 ",
		"  _text    , true            , Observation  , active   , false       , 1 ",
		"  _content , false           ,              , active   , true        , 1 ",
		"  _content , true            , Resource     , active   , true        , 1 ",
		"  _content , true            , Patient      , active   , true        , 1 ",
		"  _content , true            , Resource     , retired  , false       , 1 ",
		"  _content , true            , Observation  , active   , false       , 1 ",
		"  BOTH     , true            , Patient      , active   , true        , 1 ",
		"  BOTH     , true            , Observation  , active   , false       , 0 ",
		"  BOTH     , true            , Resource     , retired  , false       , 0 ",
	})
	public void testControlResourceTypeWithExplicitSearchParameter(String theParam, boolean theCreateExplicitSp, String theSpBase, String theSpStatus, boolean theExpectMatch, long theExpectDocumentCount) {
		boolean isText = PARAM_TEXT.equals(theParam) || "BOTH".equals(theParam);
		boolean isContent = PARAM_CONTENT.equals(theParam) || "BOTH".equals(theParam);

		// Disable fulltext indexing while we create the FT search parameters
		// so that they don't get indexed themselved and affect the doc counts
		myStorageSettings.setHibernateSearchIndexFullText(false);
		if (theCreateExplicitSp) {
			if (isContent) {
				createSearchParameter(PARAM_CONTENT, theSpBase, theSpStatus);
			}
			if (isText) {
				createSearchParameter(PARAM_TEXT, theSpBase, theSpStatus);
			}
		}
		myStorageSettings.setHibernateSearchIndexFullText(true);
		mySearchParamRegistry.forceRefresh();

		IIdType patientId = createPatient(withFamily("Simpson"), withNarrative("<div>Simpson</div>"));

		SearchParameterMap searchMap = SearchParameterMap.newSynchronous();
		if ("BOTH".equals(theParam) || PARAM_TEXT.equals(theParam)) {
			searchMap.add(PARAM_TEXT, new StringParam("simpson"));
		}
		if ("BOTH".equals(theParam) || PARAM_CONTENT.equals(theParam)) {
			searchMap.add(PARAM_CONTENT, new StringParam("simpson"));
		}

		if (theExpectMatch) {
			List<String> actual = toUnqualifiedVersionlessIdValues(myPatientDao.search(searchMap, mySrd));
			assertThat(actual).containsExactly(patientId.getValue());
		} else {
			assertThatThrownBy(() -> myPatientDao.search(searchMap, mySrd))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("Fulltext searching is not enabled on this server for resource type Patient, can not support the parameter(s):");
		}

		assertEquals(theExpectDocumentCount, getTotalFullTextIndexedEntities());
	}

	private void createSearchParameter(String theParam, String theSpBase, String theSpStatus) {
		SearchParameter sp = new SearchParameter();
		if (PARAM_TEXT.equals(theParam)) {
			sp.setId("Resource-text");
			sp.setUrl("http://hl7.org/fhir/SearchParameter/DomainResource-text");
		} else {
			sp.setId("Resource-content");
			sp.setUrl("http://hl7.org/fhir/SearchParameter/Resource-content");
		}
		sp.setName(theParam);
		sp.setCode(theParam);
		sp.setStatus(Enumerations.PublicationStatus.fromCode(theSpStatus));
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.fromCode(theSpBase));
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setProcessingMode(SearchParameter.SearchProcessingModeType.NORMAL);

		ourLog.info("SP:\n{}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.update(sp, mySrd);
	}

	private long getTotalFullTextIndexedEntities() {
		EntityManager entityManager = myEntityManagerFactory.createEntityManager(SynchronizationType.SYNCHRONIZED);
		long totalHits;
		try {
			SearchSession searchSession = Search.session(entityManager);
			totalHits = searchSession
				.search(ResourceTable.class)
				.select(SearchProjectionFactory::id)
				.where(SearchPredicateFactory::matchAll)
				.fetchTotalHitCount();
		} finally {
			entityManager.close();
		}
		return totalHits;
	}

	@Interceptor
	private static class MyInterceptor {

		private final Map<Pointcut, Integer> myIndexTypeToInvocationCount = new HashMap<>();

		private String myContentPayload;
		private String myTextPayload;
		private boolean myDoNotIndexContent;
		private boolean myDoNotIndexText;

		@Hook(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT)
		public FullTextExtractionResponse indexPayloadContent(FullTextExtractionRequest theRequest) {
			incrementInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT);

			if (myContentPayload != null) {
				return FullTextExtractionResponse.indexPayload(myContentPayload);
			}
			if (myDoNotIndexContent) {
				return FullTextExtractionResponse.doNotIndex();
			}

			return null;
		}

		@Hook(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT)
		public FullTextExtractionResponse indexPayloadText(FullTextExtractionRequest theRequest) {
			incrementInvocationCount(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_TEXT);

			if (myTextPayload != null) {
				return FullTextExtractionResponse.indexPayload(myTextPayload);
			}
			if (myDoNotIndexText) {
				return FullTextExtractionResponse.doNotIndex();
			}

			return null;
		}

		private void incrementInvocationCount(Pointcut pointcut) {
			int invocationCount = myIndexTypeToInvocationCount.getOrDefault(pointcut, 0);
			invocationCount++;
			myIndexTypeToInvocationCount.put(pointcut, invocationCount);
		}

		public void replaceContentPayload(String theContentPayload) {
			myContentPayload = theContentPayload;
		}

		public void replaceTextPayload(String theTextPayload) {
			myTextPayload = theTextPayload;
		}

		public void doNotIndexText() {
			myDoNotIndexText = true;
		}

		public int getInvocationCount(Pointcut thePointcut) {
			return myIndexTypeToInvocationCount.getOrDefault(thePointcut, 0);
		}

		public void doNotIndexContent() {
			myDoNotIndexContent = true;
		}


	}

}
