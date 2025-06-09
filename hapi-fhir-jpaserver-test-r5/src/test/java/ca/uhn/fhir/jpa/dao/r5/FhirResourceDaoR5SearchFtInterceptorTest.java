package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.*;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionRequest;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionResponse;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.IdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = TestHSearchAddInConfig.DefaultLuceneHeap.class)
@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5SearchFtInterceptorTest extends BaseJpaR5Test {
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5SearchFtInterceptorTest.class);

    @Mock
    private IAnonymousInterceptor myAnonymousInterceptor;
    @Captor
    private ArgumentCaptor<HookParams> myHookParamsCaptor;

    @Override
    @BeforeEach
    public void before() throws Exception {
        super.before();
        myStorageSettings.setHibernateSearchIndexFullText(true);
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
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.CONTENT));
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.TEXT));
    }

    @Test
    public void testDoNotIndexContent() {
        // Setup
        MyInterceptor interceptor = new MyInterceptor();
        interceptor.doNotIndexContent();
        registerInterceptor(interceptor);

        IIdType id = createPatient(withNarrative("<div>HELLO</div>"));

        // Test
        SearchParameterMap params = SearchParameterMap
                .newSynchronous(PARAM_CONTENT, new StringParam("simpson"));
        IBundleProvider outcome = myPatientDao.search(params, mySrd);

        // Verify
        assertThat(toUnqualifiedVersionlessIdValues(outcome)).isEmpty();
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.CONTENT));
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.TEXT));
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
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.CONTENT));
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.TEXT));
    }

    @Test
    public void testDoNotIndexText() {
        // Setup
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
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.CONTENT));
        assertEquals(1, interceptor.getInvocationCount(FullTextExtractionRequest.IndexTypeEnum.TEXT));
    }

    @Test
    public void testRequest_Create() {
        // Setup
        doAnswer(t->{
            // Verify (this gets called by the framework after the "Test" action below is called)
            FullTextExtractionRequest request = t.getArgument(1, HookParams.class).get(FullTextExtractionRequest.class);
            if (FullTextExtractionRequest.IndexTypeEnum.CONTENT == request.getIndexType()) {
                assertNotNull(request.getResource());
                assertNull(request.getResourceId());
                assertThat(request.getDefaultString()).contains("Simpson", "Homer");
                assertFalse(request.isDelete());
            }
            return null;
        }).when(myAnonymousInterceptor).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT), any());

        // Test
        myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT, myAnonymousInterceptor);
        createPatient(withActiveTrue(), withFamily("Simpson"), withGiven("Homer"), withNarrative("<div>HELLO</div>"));

        // Verify
        verify(myAnonymousInterceptor, times(2)).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT), any());
    }

    @Test
    public void testRequest_Update() {
        // Setup
        IIdType id = createPatient(withActiveTrue());
        doAnswer(t->{
            // Verify (this gets called by the framework after the "Test" action below is called)
            FullTextExtractionRequest request = t.getArgument(1, HookParams.class).get(FullTextExtractionRequest.class);
            if (FullTextExtractionRequest.IndexTypeEnum.CONTENT == request.getIndexType()) {
                assertNotNull(request.getResource());
                assertNotNull(request.getResourceId());
                assertEquals(id.getValue(), request.getResourceId().toUnqualifiedVersionless().getValue());
                assertThat(request.getDefaultString()).contains("Simpson", "Homer");
                assertFalse(request.isDelete());
            }
            return null;
        }).when(myAnonymousInterceptor).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT), any());

        // Test
        myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT, myAnonymousInterceptor);
        createPatient(withId(id.getIdPart()), withActiveTrue(), withFamily("Simpson"), withGiven("Homer"), withNarrative("<div>HELLO</div>"));

        // Verify
        verify(myAnonymousInterceptor, times(2)).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT), any());
    }

    @Test
    public void testRequest_Delete() {
        // Setup
        createPatient(withId("P"), withActiveTrue());

        doAnswer(t->{
            // Verify (this gets called by the framework after the "Test" action below is called)
            FullTextExtractionRequest request = t.getArgument(1, HookParams.class).get(FullTextExtractionRequest.class);
            if (FullTextExtractionRequest.IndexTypeEnum.CONTENT == request.getIndexType()) {
                assertNull(request.getResource());
                assertNotNull(request.getResourceId());
                assertEquals("Patient/P", request.getResourceId().toUnqualifiedVersionless().getValue());
                assertNull(request.getDefaultString());
                assertTrue(request.isDelete());
            }
            return null;
        }).when(myAnonymousInterceptor).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT), any());

        // Test
        myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT, myAnonymousInterceptor);
        myPatientDao.delete(new IdType("Patient/P"), mySrd);

        // Verify
        verify(myAnonymousInterceptor, times(2)).invoke(eq(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT), any());
    }


    @Interceptor
    private static class MyInterceptor {

        private final Map<FullTextExtractionRequest.IndexTypeEnum, Integer> myIndexTypeToInvocationCount = new HashMap<>();
        private String myContentPayload;
        private String myTextPayload;
        private boolean myDoNotIndexContent;
        private boolean myDoNotIndexText;

        @Hook(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT)
        public FullTextExtractionResponse indexPayload(FullTextExtractionRequest theRequest) {

            int invocationCount = myIndexTypeToInvocationCount.getOrDefault(theRequest.getIndexType(), 0);
            invocationCount++;
            myIndexTypeToInvocationCount.put(theRequest.getIndexType(), invocationCount);

            if (theRequest.getIndexType() == FullTextExtractionRequest.IndexTypeEnum.CONTENT) {
                if (myContentPayload != null) {
                    return FullTextExtractionResponse.indexPayload(myContentPayload);
                }
                if (myDoNotIndexContent) {
                    return FullTextExtractionResponse.doNotIndex();
                }
            }

            if (theRequest.getIndexType() == FullTextExtractionRequest.IndexTypeEnum.TEXT) {
                if (myTextPayload != null) {
                    return FullTextExtractionResponse.indexPayload(myTextPayload);
                }
                if (myDoNotIndexText) {
                    return FullTextExtractionResponse.doNotIndex();
                }
            }

            return null;
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
        public int getInvocationCount(FullTextExtractionRequest.IndexTypeEnum theIndexType) {
            return myIndexTypeToInvocationCount.getOrDefault(theIndexType, 0);
        }

        public void doNotIndexContent() {
            myDoNotIndexContent = true;
        }
    }


}
