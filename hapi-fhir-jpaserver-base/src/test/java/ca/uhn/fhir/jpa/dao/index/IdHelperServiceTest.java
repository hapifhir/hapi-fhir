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
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

		final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> subject.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted));
		assertEquals("HAPI-2001: Resource Patient/123 is not known", resourceNotFoundException.getMessage());
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

		Map<String, JpaPid> actualIds = subject.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted);

		//verifyResult
		assertFalse(actualIds.isEmpty());
		assertNull(actualIds.get(ids.get(0)));
    }

    private Root<ResourceTable> getMockedFrom() {
        @SuppressWarnings("unchecked")
        Path<Object> path = mock(Path.class);
        @SuppressWarnings("unchecked")
        Root<ResourceTable> from = mock(Root.class);
        when(from.get(ArgumentMatchers.<String>any())).thenReturn(path);
        return from;
    }

    private List<Tuple> getMockedTupleList(Long idNumber, String resourceType, String id) {
        Tuple tuple = mock(Tuple.class);
        when(tuple.get(eq(0), eq(Long.class))).thenReturn(idNumber);
        when(tuple.get(eq(1), eq(String.class))).thenReturn(resourceType);
        when(tuple.get(eq(2), eq(String.class))).thenReturn(id);
        return List.of(tuple);
    }
}
