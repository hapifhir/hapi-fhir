package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceVersionSvcTest {

	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	IResourceTableDao myResourceTableDao;
	@Mock
	IIdHelperService myIdHelperService;

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
	 *
	 * @param resourceType
	 * @param pid
	 * @param version
	 * @return
	 */
	private Object[] getResourceTableRecordForResourceTypeAndPid(String resourceType, long pid, long version) {
		return new Object[]{
			pid, // long
			resourceType, // string
			version // long
		};
	}

	/**
	 * Helper function to mock out resolveResourcePersistentIdsWithCache
	 * to return empty lists (as if no resources were found).
	 */
	private void mock_resolveResourcePersistentIdsWithCache_toReturnNothing() {
		CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
		CriteriaQuery<ResourceTable> criteriaQuery = Mockito.mock(CriteriaQuery.class);
		Root<ResourceTable> from = Mockito.mock(Root.class);
		Path path = Mockito.mock(Path.class);
	}

	/**
	 * Helper function to mock out getIdsOfExistingResources
	 * to return the matches and resources matching those provided
	 * by parameters.
	 *
	 * @param theResourcePacks
	 */
	private void mockReturnsFor_getIdsOfExistingResources(ResourceIdPackage... theResourcePacks) {
		List<IResourcePersistentId> resourcePersistentIds = new ArrayList<>();
		List<Object[]> matches = new ArrayList<>();

		for (ResourceIdPackage pack : theResourcePacks) {
			resourcePersistentIds.add(pack.myPid);

			matches.add(getResourceTableRecordForResourceTypeAndPid(
				pack.MyResourceId.getResourceType(),
				pack.myPid.getId(),
				pack.MyVersion
			));
		}

		IResourcePersistentId first = resourcePersistentIds.remove(0);
		if (resourcePersistentIds.isEmpty()) {
			when(myIdHelperService.resolveResourcePersistentIdsWithCache(any(), any())).thenReturn(Collections.singletonList(first));
		} else {
			when(myIdHelperService.resolveResourcePersistentIdsWithCache(any(), any())).thenReturn(resourcePersistentIds);
		}
	}

	@Test
	public void getLatestVersionIdsForResourceIds_whenResourceExists_returnsMapWithPIDAndVersion() {
		IIdType type = new IdDt("Patient/RED");
		JpaPid jpaPid = JpaPid.fromId(1L);
		jpaPid.setAssociatedResourceId(type);
		HashMap<IIdType, IResourcePersistentId> map = new HashMap<>();
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

		// when
		mock_resolveResourcePersistentIdsWithCache_toReturnNothing();

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
		mock_resolveResourcePersistentIdsWithCache_toReturnNothing();
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
	private class ResourceIdPackage {
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
