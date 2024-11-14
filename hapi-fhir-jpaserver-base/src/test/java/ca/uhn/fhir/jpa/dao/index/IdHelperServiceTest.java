package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.JpaResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
        boolean theExcludeDeleted = false;

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);

		final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> myHelperSvc.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted));
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
        boolean theExcludeDeleted = false;

        //prepare results
        Patient expectedPatient = new Patient();
        expectedPatient.setId(ids.get(0));

        // configure mock behaviour
        when(myStorageSettings.isDeleteEnabled()).thenReturn(false);

		Map<String, JpaPid> actualIds = myHelperSvc.resolveResourcePersistentIds(requestPartitionId, resourceType, ids, theExcludeDeleted);

		//verifyResult
		assertFalse(actualIds.isEmpty());
		assertNull(actualIds.get(ids.get(0)));
    }

	@Test
	public void resolveResourcePersistentIds_withValidPids_returnsMap() {
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();

		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("123");
		patientIdsToResolve.add("456");

		// test
		Map<String, JpaPid> idToPid = myHelperSvc.resolveResourcePersistentIds(partitionId,
			resourceType,
			patientIdsToResolve);

		assertFalse(idToPid.isEmpty());
		for (String pid : patientIdsToResolve) {
			assertThat(idToPid).containsKey(pid);
		}
	}

	@Test
	public void resolveResourcePersistentIds_withForcedIdsAndDeleteEnabled_returnsMap() {
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();

		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("RED");
		patientIdsToResolve.add("BLUE");

		Object[] redView = new Object[] {
			123L,
			"Patient",
			"RED",
			new Date(),
			null
		};
		Object[] blueView = new Object[] {
			456L,
			"Patient",
			"BLUE",
			new Date(),
			null
		};

		// when
		when(myStorageSettings.isDeleteEnabled())
			.thenReturn(true);
		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myTypedQuery);
		when(myTypedQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, redView),
			new TupleImpl(null, blueView)
		));

		// test
		Map<String, JpaPid> map = myHelperSvc.resolveResourcePersistentIds(
			partitionId,
			resourceType,
			patientIdsToResolve);

		assertFalse(map.isEmpty());
		for (String id : patientIdsToResolve) {
			assertThat(map).containsKey(id);
		}
	}

	@Test
	public void resolveResourcePersistenIds_withForcedIdAndDeleteDisabled_returnsMap() {
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();

		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("RED");
		patientIdsToResolve.add("BLUE");

		JpaPid red = JpaPid.fromId(123L);
		JpaPid blue = JpaPid.fromId(456L);

		// we will pretend the lookup value is in the cache
		when(myMemoryCacheService.getIfPresent(eq(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID),
			any()))
			.thenReturn(List.of(new JpaResourceLookup("Patient", red.getId(), null, new PartitionablePartitionId())))
			.thenReturn(List.of(new JpaResourceLookup("Patient", blue.getId(), null, new PartitionablePartitionId())));

		// test
		Map<String, JpaPid> map = myHelperSvc.resolveResourcePersistentIds(
			partitionId,
			resourceType,
			patientIdsToResolve
		);

		assertFalse(map.isEmpty());
		for (String id : patientIdsToResolve) {
			assertThat(map).containsKey(id);
		}
		assertThat(map).containsEntry("RED", red);
		assertThat(map).containsEntry("BLUE", blue);
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

		IResourceLookup<JpaPid> result = myHelperSvc.resolveResourceIdentity(partitionId, resourceType, resourceForcedId);
		assertEquals(tuple[0], result.getPersistentId().getId());
		assertEquals(tuple[1], result.getResourceType());
		assertEquals(tuple[3], result.getDeleted());
	}

	@Test
	public void testResolveResourcePersistentIds_mapDefaultFunctionality(){
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		List<String> ids = Arrays.asList("A", "B", "C");

		JpaPid resourcePersistentId1 = JpaPid.fromId(1L);
		JpaPid resourcePersistentId2 = JpaPid.fromId(2L);
		JpaPid resourcePersistentId3 = JpaPid.fromId(3L);
		when(myMemoryCacheService.getIfPresent(eq(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID),
			any()))
			.thenReturn(List.of(new JpaResourceLookup("Patient", resourcePersistentId1.getId(), null, new PartitionablePartitionId())))
			.thenReturn(List.of(new JpaResourceLookup("Patient", resourcePersistentId2.getId(), null, new PartitionablePartitionId())))
			.thenReturn(List.of(new JpaResourceLookup("Patient", resourcePersistentId3.getId(), null, new PartitionablePartitionId())));

		Map<String, JpaPid> result = myHelperSvc.resolveResourcePersistentIds(partitionId, resourceType, ids)
			.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()));
		assertThat(result.keySet()).hasSize(3);
		assertEquals(1L, result.get("A").getId());
		assertEquals(2L, result.get("B").getId());
		assertEquals(3L, result.get("C").getId());
	}
	
}
