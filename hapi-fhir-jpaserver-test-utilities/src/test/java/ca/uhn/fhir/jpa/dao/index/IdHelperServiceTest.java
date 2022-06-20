package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

	@InjectMocks @Spy
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
			new Long(123l),
			"RED",
			new Date()
		};
		Object[] blueView = new Object[] {
			"Patient",
			new Long(456l),
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

		ResourcePersistentId red = new ResourcePersistentId("Patient", new Long(123l));
		ResourcePersistentId blue = new ResourcePersistentId("Patient",  new Long(456l));

		// we will pretend the lookup value is in the cache
		when(myMemoryCacheService.getThenPutAfterCommit(Mockito.any(MemoryCacheService.CacheEnum.class),
			Mockito.anyString(),
			Mockito.any(Function.class)))
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
		Assertions.assertEquals(red, map.get("RED"));
		Assertions.assertEquals(blue, map.get("BLUE"));
	}

	@Test
	public void testResolveResourceIdentity_defaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String resourceId = "AAA";

		IResourceLookup testResult = new ResourceTable();
		doReturn(testResult).when(myHelperService).resolveResourceIdentity(partitionId, resourceType, resourceId, false);
		IResourceLookup result = myHelperService.resolveResourceIdentity(partitionId, resourceType, resourceId);
		verify(myHelperService, times(1)).resolveResourceIdentity(partitionId, resourceType, resourceId, false);
		assertEquals(result, testResult);
	}

	@Test
	public void testResolveResourcePersistentIds_mapDefaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		List<String> ids = Arrays.asList("A", "B", "C");

		Map<String, ResourcePersistentId> testResult = new HashMap();
		doReturn(testResult).when(myHelperService).resolveResourcePersistentIds(partitionId, resourceType, ids, false);
		Map<String, ResourcePersistentId> result = myHelperService.resolveResourcePersistentIds(partitionId, resourceType, ids);
		verify(myHelperService, times(1)).resolveResourcePersistentIds(partitionId, resourceType, ids, false);
		assertEquals(result, testResult);
	}

	@Test
	public void testResolveResourcePersistentIds_resourcePidDefaultFunctionality(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String id = "A";

		ResourcePersistentId testResult = new ResourcePersistentId(new Object(), 1L);
		doReturn(testResult).when(myHelperService).resolveResourcePersistentIds(partitionId, resourceType, id, false);
		ResourcePersistentId result = myHelperService.resolveResourcePersistentIds(partitionId, resourceType, id);
		verify(myHelperService, times(1)).resolveResourcePersistentIds(partitionId, resourceType, id, false);
		assertEquals(result, testResult);
	}
}
