package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IdHelperServiceTest {

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Mock
	private IResourceTableDao myResourceTableDao;

	@Mock
	private MemoryCacheService myMemoryCacheService;

	@Mock
	private PartitionSettings myPartitionSettings;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private EntityManager myEntityManager;

	@Mock
	private TypedQuery myQuery;

	@Mock
	private Tuple myTuple;

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@InjectMocks
	private IdHelperService myHelperService;

	@BeforeEach
	public void beforeEach() {
		myHelperService.setDontCheckActiveTransactionForUnitTest(true);
	}

	@Test
	public void resolveResourcePersistentIds_withValidPids_returnsMap() {
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("123");
		patientIdsToResolve.add("456");

		// test
		Map<String, JpaPid> idToPid = myHelperService.resolveResourcePersistentIds(partitionId,
			resourceType,
			patientIdsToResolve);

		assertFalse(idToPid.isEmpty());
		for (String pid : patientIdsToResolve) {
			assertThat(idToPid).containsKey(pid);
		}
	}

	@Test
	public void resolveResourcePersistentIds_withForcedIdsAndDeleteEnabled_returnsMap() {
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("RED");
		patientIdsToResolve.add("BLUE");

		Object[] redView = new Object[] {
			123l,
			"Patient",
			"RED"
		};
		Object[] blueView = new Object[] {
			456l,
			"Patient",
			"BLUE"
		};

		// when
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);
		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myQuery);
		when(myQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, redView),
			new TupleImpl(null, blueView)
		));

		// test
		Map<String, JpaPid> map = myHelperService.resolveResourcePersistentIds(
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
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("RED");
		patientIdsToResolve.add("BLUE");

		JpaPid red = JpaPid.fromIdAndVersion(123L, 123L);
		JpaPid blue = JpaPid.fromIdAndVersion(456L, 456L);

		// we will pretend the lookup value is in the cache
		when(myMemoryCacheService.getIfPresent(any(MemoryCacheService.CacheEnum.class),
			Mockito.anyString()))
			.thenReturn(red)
			.thenReturn(blue);

		// test
		Map<String, JpaPid> map = myHelperService.resolveResourcePersistentIds(
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
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String resourceForcedId = "AAA";

		Object[] forcedIdView = new Object[6];
		forcedIdView[0] = resourceType;
		forcedIdView[1] = 1L;
		forcedIdView[2] = resourceForcedId;
		forcedIdView[3] = null;
		forcedIdView[4] = null;
		forcedIdView[5] = null;

		Collection<Object[]> testForcedIdViews = new ArrayList<>();
		testForcedIdViews.add(forcedIdView);
		when(myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartition(any(), any(), any(), anyBoolean())).thenReturn(testForcedIdViews);

		IResourceLookup<JpaPid> result = myHelperService.resolveResourceIdentity(partitionId, resourceType, resourceForcedId);
		assertEquals(forcedIdView[0], result.getResourceType());
		assertEquals(forcedIdView[1], result.getPersistentId().getId());
		assertEquals(forcedIdView[3], result.getDeleted());
	}

	@Test
	public void testResolveResourcePersistentIds_mapDefaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		List<String> ids = Arrays.asList("A", "B", "C");

		JpaPid resourcePersistentId1 = JpaPid.fromId(1L);
		JpaPid resourcePersistentId2 = JpaPid.fromId(2L);
		JpaPid resourcePersistentId3 = JpaPid.fromId(3L);
		when(myMemoryCacheService.getIfPresent(any(), any()))
			.thenReturn(resourcePersistentId1)
			.thenReturn(resourcePersistentId2)
			.thenReturn(resourcePersistentId3);
		Map<String, JpaPid> result = myHelperService.resolveResourcePersistentIds(partitionId, resourceType, ids)
			.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()));
		assertThat(result.keySet()).hasSize(3);
		assertEquals(1L, result.get("A").getId());
		assertEquals(2L, result.get("B").getId());
		assertEquals(3L, result.get("C").getId());
	}

	@Test
	public void testResolveResourcePersistentIds_resourcePidDefaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		Long id = 1L;

		JpaPid jpaPid1 = JpaPid.fromId(id);
		when(myStorageSettings.getResourceClientIdStrategy()).thenReturn(JpaStorageSettings.ClientIdStrategyEnum.ANY);
		when(myMemoryCacheService.getIfPresent(any(), any())).thenReturn(jpaPid1);
		JpaPid result = myHelperService.resolveResourcePersistentIds(partitionId, resourceType, id.toString());
		assertEquals(id, result.getId());
	}
}
