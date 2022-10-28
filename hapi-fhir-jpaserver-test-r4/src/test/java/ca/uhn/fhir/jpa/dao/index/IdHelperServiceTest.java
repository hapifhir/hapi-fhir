package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IdHelperServiceTest {

	@Mock
	private DaoConfig myDaoConfig;

	@Mock
	private IForcedIdDao myForcedIdDao;

	@Mock
	private MemoryCacheService myMemoryCacheService;

	@Mock
	private PartitionSettings myPartitionSettings;

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
		Map<String, ResourcePersistentId> idToPid = myHelperService.resolveResourcePersistentIds(partitionId,
			resourceType,
			patientIdsToResolve);

		Assertions.assertFalse(idToPid.isEmpty());
		for (String pid : patientIdsToResolve) {
			Assertions.assertTrue(idToPid.containsKey(pid));
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
			"Patient",
			123l,
			"RED",
			new Date()
		};
		Object[] blueView = new Object[] {
			"Patient",
			456l,
			"BLUE",
			new Date()
		};

		// when
		when(myDaoConfig.isDeleteEnabled())
			.thenReturn(true);
		when(myForcedIdDao.findAndResolveByForcedIdWithNoType(Mockito.anyString(),
			Mockito.anyList(), Mockito.anyBoolean()))
			.thenReturn(Collections.singletonList(redView))
			.thenReturn(Collections.singletonList(blueView));

		// test
		Map<String, ResourcePersistentId> map = myHelperService.resolveResourcePersistentIds(
			partitionId,
			resourceType,
			patientIdsToResolve);

		Assertions.assertFalse(map.isEmpty());
		for (String id : patientIdsToResolve) {
			Assertions.assertTrue(map.containsKey(id));
		}
	}

	@Test
	public void resolveResourcePersistenIds_withForcedIdAndDeleteDisabled_returnsMap() {
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = Patient.class.getSimpleName();
		List<String> patientIdsToResolve = new ArrayList<>();
		patientIdsToResolve.add("RED");
		patientIdsToResolve.add("BLUE");

		ResourcePersistentId red = new ResourcePersistentId("Patient", 123l);
		ResourcePersistentId blue = new ResourcePersistentId("Patient", 456l);

		// we will pretend the lookup value is in the cache
		when(myMemoryCacheService.getThenPutAfterCommit(any(MemoryCacheService.CacheEnum.class),
			Mockito.anyString(),
			any(Function.class)))
			.thenReturn(red)
			.thenReturn(blue);

		// test
		Map<String, ResourcePersistentId> map = myHelperService.resolveResourcePersistentIds(
			partitionId,
			resourceType,
			patientIdsToResolve
		);

		Assertions.assertFalse(map.isEmpty());
		for (String id : patientIdsToResolve) {
			Assertions.assertTrue(map.containsKey(id));
		}
		assertEquals(red, map.get("RED"));
		assertEquals(blue, map.get("BLUE"));
	}

	@Test
	public void testResolveResourceIdentity_defaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String resourceForcedId = "AAA";

		Object[] forcedIdView = new Object[4];
		forcedIdView[0] = resourceType;
		forcedIdView[1] = 1L;
		forcedIdView[2] = resourceForcedId;
		forcedIdView[3] = null;

		Collection<Object[]> testForcedIdViews = new ArrayList<>();
		testForcedIdViews.add(forcedIdView);
		when(myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(any(), any(), any(), anyBoolean())).thenReturn(testForcedIdViews);

		IResourceLookup result = myHelperService.resolveResourceIdentity(partitionId, resourceType, resourceForcedId);
		assertEquals(forcedIdView[0], result.getResourceType());
		assertEquals(forcedIdView[1], result.getPersistentId().getId());
		assertEquals(forcedIdView[3], result.getDeleted());
	}

	@Test
	public void testResolveResourcePersistentIds_mapDefaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		List<String> ids = Arrays.asList("A", "B", "C");

		ResourcePersistentId resourcePersistentId1 = new ResourcePersistentId("TEST1");
		ResourcePersistentId resourcePersistentId2 = new ResourcePersistentId("TEST2");
		ResourcePersistentId resourcePersistentId3 = new ResourcePersistentId("TEST3");
		when(myMemoryCacheService.getThenPutAfterCommit(any(), any(), any()))
			.thenReturn(resourcePersistentId1)
			.thenReturn(resourcePersistentId2)
			.thenReturn(resourcePersistentId3);
		Map<String, ResourcePersistentId> result = myHelperService.resolveResourcePersistentIds(partitionId, resourceType, ids);
		assertThat(result.keySet(), hasSize(3));
		assertEquals("TEST1", result.get("A").getId());
		assertEquals("TEST2", result.get("B").getId());
		assertEquals("TEST3", result.get("C").getId());
	}

	@Test
	public void testResolveResourcePersistentIds_resourcePidDefaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String id = "A";

		ResourcePersistentId resourcePersistentId1 = new ResourcePersistentId(id);
		when(myMemoryCacheService.getThenPutAfterCommit(any(), any(), any())).thenReturn(resourcePersistentId1);
		ResourcePersistentId result = myHelperService.resolveResourcePersistentIds(partitionId, resourceType, id);
		assertEquals(id, result.getId());
	}
}
