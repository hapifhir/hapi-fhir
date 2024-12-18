package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.JpaResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceVersionSvcTest {

	@Mock
	IResourceTableDao myResourceTableDao;
	@Mock
	IIdHelperService<JpaPid> myIdHelperService;

	// TODO KHS move the methods that use this out to a separate test class
	@InjectMocks
	private ResourceVersionSvcDaoImpl myResourceVersionSvc;

	@BeforeEach
	public void before() {
		myResourceVersionSvc.myIdHelperService = myIdHelperService;
	}

	/**
	 * Gets a ResourceTable record for getResourceVersionsForPid
	 * Order matters!
	 */
	private Object[] getResourceTableRecordForResourceTypeAndPid(JpaPid thePid, String theResourceType, String theFhirId, long theVersion) {
		return new Object[]{
			thePid, // JpaPid
			theResourceType, // String
			theFhirId, // String
			theVersion // Long
		};
	}

	/**
	 * Helper function to mock out getIdsOfExistingResources
	 * to return the matches and resources matching those provided
	 * by parameters.
	 */
	private void mockReturnsFor_getIdsOfExistingResources(ResourceIdPackage... theResourcePacks) {
		List<JpaPid> resourcePersistentIds = new ArrayList<>();
		List<Object[]> matches = new ArrayList<>();

		for (ResourceIdPackage pack : theResourcePacks) {
			resourcePersistentIds.add(pack.myPid);
			pack.myPid.setAssociatedResourceId(pack.MyResourceId);

			matches.add(getResourceTableRecordForResourceTypeAndPid(
				pack.myPid,
				pack.MyResourceId.getResourceType(),
				pack.MyResourceId.getIdPart(),
				pack.MyVersion
			));
		}

		IResourcePersistentId<Long> first = resourcePersistentIds.remove(0);
		if (resourcePersistentIds.isEmpty()) {
			when(myIdHelperService.resolveResourceIdentities(any(), any(), any()))
				.thenReturn(Map.of(first.getAssociatedResourceId(), new JpaResourceLookup(first.getResourceType(), first.getAssociatedResourceId().getIdPart(), (JpaPid) first, null, null)));
		} else {

			HashMap<IIdType, IResourceLookup<JpaPid>> map = new HashMap<>();
			for (var next : resourcePersistentIds) {
				map.put(next.getAssociatedResourceId(), new JpaResourceLookup(next.getResourceType(),next.getAssociatedResourceId().getIdPart() ,(JpaPid) next, null, null));
			}

			when(myIdHelperService.resolveResourceIdentities(any(), any(), any()))
				.thenReturn(map);
		}
	}

	@Test
	public void getLatestVersionIdsForResourceIds_whenResourceExists_returnsMapWithPIDAndVersion() {
		IIdType type = new IdDt("Patient/RED");
		JpaPid jpaPid = JpaPid.fromId(1L);
		jpaPid.setAssociatedResourceId(type);
		HashMap<IIdType, JpaPid> map = new HashMap<>();
		map.put(type, jpaPid);
		ResourceIdPackage pack = new ResourceIdPackage(type, jpaPid, 2L);

		// when
		mockReturnsFor_getIdsOfExistingResources(pack);

		// test
		ResourcePersistentIdMap retMap = myResourceVersionSvc.getLatestVersionIdsForResourceIds(RequestPartitionId.allPartitions(),
			Collections.singletonList(type));

		assertTrue(retMap.containsKey(type));
		assertEquals(jpaPid.getVersion(), map.get(type).getVersion());
	}

	@Test
	public void getLatestVersionIdsForResourceIds_whenResourceDoesNotExist_returnsEmptyMap() {
		IIdType type = new IdDt("Patient/RED");

		// test
		ResourcePersistentIdMap retMap = myResourceVersionSvc.getLatestVersionIdsForResourceIds(RequestPartitionId.allPartitions(),
			Collections.singletonList(type));

		assertTrue(retMap.isEmpty());
	}

	@Test
	public void getLatestVersionIdsForResourceIds_whenSomeResourcesDoNotExist_returnsOnlyExistingElements() {
		// resource to be found
		IIdType type = new IdDt("Patient/RED");
		JpaPid jpaPid = JpaPid.fromId(1L);
		jpaPid.setAssociatedResourceId(type);
		ResourceIdPackage pack = new ResourceIdPackage(type, jpaPid, 2L);

		// resource that won't be found
		IIdType type2 = new IdDt("Patient/BLUE");

		// when
		mockReturnsFor_getIdsOfExistingResources(pack);

		// test
		ResourcePersistentIdMap retMap = myResourceVersionSvc.getLatestVersionIdsForResourceIds(
			RequestPartitionId.allPartitions(),
			Arrays.asList(type, type2)
		);

		// verify
		assertEquals(1, retMap.size());
		assertTrue(retMap.containsKey(type));
		assertFalse(retMap.containsKey(type2));
	}

	@Test
	public void testReplaceDefault_AllPartitions() {

		IdHelperService svc = new IdHelperService();
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setDefaultPartitionId(1);
		svc.setPartitionSettingsForUnitTest(partitionSettings);

		RequestPartitionId outcome = svc.replaceDefault(RequestPartitionId.allPartitions());
		assertThat(outcome).isSameAs(RequestPartitionId.allPartitions());
	}

	@Test
	public void testReplaceDefault_DefaultPartition() {

		IdHelperService svc = new IdHelperService();
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setDefaultPartitionId(1);
		svc.setPartitionSettingsForUnitTest(partitionSettings);

		RequestPartitionId outcome = svc.replaceDefault(RequestPartitionId.defaultPartition());
		assertEquals(1, outcome.getPartitionIds().get(0));
	}

	// helper class to package up data for helper methods
	private static class ResourceIdPackage {
		public IIdType MyResourceId;
		public JpaPid myPid;
		public Long MyVersion;

		public ResourceIdPackage(IIdType id,
										 JpaPid pid,
										 Long version) {
			MyResourceId = id;
			myPid = pid;
			MyVersion = version;
		}
	}

}
