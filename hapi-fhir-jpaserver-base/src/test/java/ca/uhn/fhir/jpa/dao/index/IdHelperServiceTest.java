package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IdHelperServiceTest {

    @InjectMocks
    private final IdHelperService myHelperSvc = new IdHelperService();

    @Mock
    protected IResourceTableDao myResourceTableDao;

    @Mock
    private JpaStorageSettings myStorageSettings;

    @Spy
    private FhirContext myFhirCtx = FhirContext.forR4Cached();

    @Mock
    private MemoryCacheService myMemoryCacheService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private EntityManager myEntityManager;

    @Mock
    private PartitionSettings myPartitionSettings;

	@Mock
	private TypedQuery myQuery;

	@Mock
	private Tuple myTuple;

	@BeforeEach
    void setUp() {
        myHelperSvc.setDontCheckActiveTransactionForUnitTest(true);

		// lenient because some tests require this setup, and others do not
		lenient().doReturn(true).when(myStorageSettings).isDeleteEnabled();
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ANY).when(myStorageSettings).getResourceClientIdStrategy();
    }

    @Test
    public void testResolveResourcePersistentIds() {
        //prepare params
        RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIdAndName(1, "Partition-A");
        String resourceType = "Patient";
        Long id = 123L;
        List<String> ids = List.of(String.valueOf(id));
        boolean theExcludeDeleted = false;

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);
		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myQuery);
		when(myQuery.getResultList()).thenReturn(List.of());

		final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> myHelperSvc.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted));
		assertEquals("HAPI-1100: Resource Patient/123 is not known", resourceNotFoundException.getMessage());
    }

    @Test
    public void testResolveResourcePersistentIdsDeleteFalse() {
        //prepare Params
        RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIdAndName(1, "Partition-A");
        Long id = 123L;
        String resourceType = "Patient";
        List<String> ids = List.of(String.valueOf(id));
        String forcedId = "(all)/" + resourceType + "/" + id;
        boolean theExcludeDeleted = false;

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
        when(myStorageSettings.isDeleteEnabled()).thenReturn(false);
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);
		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myQuery);
		when(myQuery.getResultList()).thenReturn(List.of(myTuple));
		when(myTuple.get(eq(0), eq(Long.class))).thenReturn(id);
		when(myTuple.get(eq(1), eq(String.class))).thenReturn(resourceType);
		when(myTuple.get(eq(2), eq(String.class))).thenReturn(Long.toString(id));

		Map<String, JpaPid> actualIds = myHelperSvc.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted);

		//verifyResult
		assertFalse(actualIds.isEmpty());
		assertEquals(JpaPid.fromId(123L), actualIds.get(ids.get(0)));
    }

}
