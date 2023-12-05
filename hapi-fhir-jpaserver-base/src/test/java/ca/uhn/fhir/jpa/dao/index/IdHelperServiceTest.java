package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import org.hibernate.jpa.spi.TupleBuilderTransformer;
import org.hl7.fhir.r4.hapi.ctx.FhirR4;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdHelperServiceTest {

    @InjectMocks
    private final IdHelperService subject = new IdHelperService();

    @Mock
    protected IResourceTableDao myResourceTableDao;

    @Mock
    private JpaStorageSettings myStorageSettings;

    @Mock
    private FhirContext myFhirCtx;

    @Mock
    private MemoryCacheService myMemoryCacheService;

    @Mock
    private EntityManager myEntityManager;

    @Mock
    private PartitionSettings myPartitionSettings;

    @BeforeEach
    void setUp() {
        subject.setDontCheckActiveTransactionForUnitTest(true);

        when(myStorageSettings.isDeleteEnabled()).thenReturn(true);
        when(myStorageSettings.getResourceClientIdStrategy()).thenReturn(JpaStorageSettings.ClientIdStrategyEnum.ANY);
        when(myPartitionSettings.isAllowUnqualifiedCrossPartitionReference()).thenReturn(true);
    }

    @Test
    public void testResolveResourcePersistentIds() {
        //prepare params
        RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionName("Partition-A");
        String resourceType = "Patient";
        Long id = 123L;
        List<String> ids = List.of(String.valueOf(id));
        boolean theExcludeDeleted = false;

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));
        Object[] obj = new Object[] {resourceType, Long.parseLong(ids.get(0)), ids.get(0), Date.from(Instant.now())};

        // configure mock behaviour
        when(myStorageSettings.isDeleteEnabled()).thenReturn(true);
        when(myResourceTableDao
                .findAndResolveByForcedIdWithNoType(eq(resourceType), eq(ids), eq(theExcludeDeleted)))
                .thenReturn(Collections.singletonList(obj));

        Map<String, JpaPid> actualIds = subject.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted);

        //verify results
        assertFalse(actualIds.isEmpty());
        assertEquals(id, actualIds.get(ids.get(0)).getId());
    }

    @Test
    public void testResolveResourcePersistentIdsDeleteFalse() {
        //prepare Params
        RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionName("Partition-A");
        Long id = 123L;
        String resourceType = "Patient";
        List<String> ids = List.of(String.valueOf(id));
        String forcedId = "(all)/" + resourceType + "/" + id;
        boolean theExcludeDeleted = false;

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
        configureCacheBehaviour(forcedId);
        configureEntityManagerBehaviour(id, resourceType, ids.get(0));
        when(myStorageSettings.isDeleteEnabled()).thenReturn(false);
        when(myFhirCtx.getVersion()).thenReturn(new FhirR4());

        Map<String, JpaPid> actualIds = subject.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted);

        //verifyResult
        assertFalse(actualIds.isEmpty());
        assertEquals(id, actualIds.get(ids.get(0)).getId());
    }

    private void configureCacheBehaviour(String resourceUrl) {
        when(myMemoryCacheService.getThenPutAfterCommit(eq(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID), eq(resourceUrl), any())).thenCallRealMethod();
        doNothing().when(myMemoryCacheService).putAfterCommit(eq(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID), eq(resourceUrl), any(JpaPid.class));
        when(myMemoryCacheService.getIfPresent(eq(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID), eq(resourceUrl))).thenReturn(null);
    }

    private void configureEntityManagerBehaviour(Long idNumber, String resourceType, String id) {
        List<Tuple> mockedTupleList = getMockedTupleList(idNumber, resourceType, id);
        CriteriaBuilder builder = getMockedCriteriaBuilder();
        Root<ResourceTable> from = getMockedFrom();

        @SuppressWarnings("unchecked")
        TypedQuery<Tuple> query = (TypedQuery<Tuple>) mock(TypedQuery.class);
        @SuppressWarnings("unchecked")
        CriteriaQuery<Tuple> cq = mock(CriteriaQuery.class);

        when(builder.createTupleQuery()).thenReturn(cq);
        when(cq.from(ArgumentMatchers.<Class<ResourceTable>>any())).thenReturn(from);
        when(query.getResultList()).thenReturn(mockedTupleList);

        when(myEntityManager.getCriteriaBuilder()).thenReturn(builder);
        when(myEntityManager.createQuery(ArgumentMatchers.<CriteriaQuery<Tuple>>any())).thenReturn(query);
    }

    private CriteriaBuilder getMockedCriteriaBuilder() {
        Predicate pred = mock(Predicate.class);
        CriteriaBuilder builder = mock(CriteriaBuilder.class);
        lenient().when(builder.equal(any(), any())).thenReturn(pred);
        return builder;
    }
    private Root<ResourceTable> getMockedFrom() {
        @SuppressWarnings("unchecked")
        Path<Object> path = mock(Path.class);
        @SuppressWarnings("unchecked")
        Root<ResourceTable> from = mock(Root.class);
        lenient().when(from.get(anyString())).thenReturn(path);
        return from;
    }

    private List<Tuple> getMockedTupleList(Long idNumber, String resourceType, String id) {
        Tuple tuple = mock(TupleBuilderTransformer.HqlTupleImpl.class);
        when(tuple.get(eq(0), eq(Long.class))).thenReturn(idNumber);
        when(tuple.get(eq(1), eq(String.class))).thenReturn(resourceType);
        when(tuple.get(eq(2), eq(String.class))).thenReturn(id);
        return List.of(tuple);
    }
}
