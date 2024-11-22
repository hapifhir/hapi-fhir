package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.sql.results.internal.TupleImpl;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

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
	private TypedQuery myTypedQuery;

	@BeforeEach
    void setUp() {
        myHelperSvc.setDontCheckActiveTransactionForUnitTest(true);

		// lenient because some tests require this setup, and others do not
		lenient().doReturn(true).when(myStorageSettings).isDeleteEnabled();
    }

    @Test
    public void testResolveResourcePersistentIds() {
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ANY).when(myStorageSettings).getResourceClientIdStrategy();

        //prepare params
        RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIdAndName(1, "Partition-A");
        String resourceType = "Patient";
        Long id = 123L;
        List<String> ids = List.of(String.valueOf(id));
        ResolveIdentityMode mode = ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled();

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);

		final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> myHelperSvc.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, mode));
		assertEquals("HAPI-2001: Resource Patient/123 is not known", resourceNotFoundException.getMessage());
    }

    @Test
    public void testResolveResourcePersistentIdsDeleteFalse() {
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ANY).when(myStorageSettings).getResourceClientIdStrategy();

        //prepare Params
        RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIdAndName(1, "Partition-A");
        Long id = 123L;
        String resourceType = "Patient";
        List<String> ids = List.of(String.valueOf(id));
        String forcedId = "(all)/" + resourceType + "/" + id;
        ResolveIdentityMode mode = ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled();

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
        when(myStorageSettings.isDeleteEnabled()).thenReturn(false);

		Map<String, JpaPid> actualIds = myHelperSvc.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, mode);

		//verifyResult
		assertFalse(actualIds.isEmpty());
		assertNull(actualIds.get(ids.get(0)));
    }





	@Test
	public void testResolveResourceIdentity_defaultFunctionality(){
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String resourceForcedId = "AAA";

		Object[] tuple = new Object[] {
			1L,
			"Patient",
			"AAA",
			new Date(),
			null
		};

		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myTypedQuery);
		when(myTypedQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, tuple)
		));

		IResourceLookup<JpaPid> result = myHelperSvc.resolveResourceIdentity(partitionId, resourceType, resourceForcedId, ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled());
		assertEquals(tuple[0], result.getPersistentId().getId());
		assertEquals(tuple[1], result.getResourceType());
		assertEquals(tuple[3], result.getDeleted());
	}


}
