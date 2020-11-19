package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceVersionCacheTest {
	private static String RESOURCE_NAME = "Patient";
	private static String RESOURCE_ID = "p123";
	public static final int ENTITY_COUNT = 7;

	private ResourceVersionCache myResourceVersionCache;

	@BeforeEach
	private void before() {
		myResourceVersionCache = new ResourceVersionCache();
	}

	@Test
	public void testPut() {
		IdDt id = buildIdDt(RESOURCE_ID);
		assertFalse(myResourceVersionCache.hasEntriesForResourceName(RESOURCE_NAME));
		String version1 = "1";
		myResourceVersionCache.put(id, version1);
		assertTrue(myResourceVersionCache.hasEntriesForResourceName(RESOURCE_NAME));
		assertEquals(version1, myResourceVersionCache.getVersionForResourceId(buildIdDt(RESOURCE_ID)));
		String version2 = "2";
		myResourceVersionCache.put(id, version2);
		assertTrue(myResourceVersionCache.hasEntriesForResourceName(RESOURCE_NAME));
		assertEquals(version2, myResourceVersionCache.getVersionForResourceId(buildIdDt(RESOURCE_ID)));
		myResourceVersionCache.removeResourceId(buildIdDt(RESOURCE_ID));
		assertFalse(myResourceVersionCache.hasEntriesForResourceName(RESOURCE_NAME));
		assertNull(myResourceVersionCache.getVersionForResourceId(buildIdDt(RESOURCE_ID)));
	}

	@Test
	public void initializeAndClear() {
				myResourceVersionCache.initialize(buildResourceVersionMap());
		for (int i = 0; i < ENTITY_COUNT; ++i) {
			assertEquals("" + i, myResourceVersionCache.getVersionForResourceId(buildIdDt(i)));
		}

		Map<IIdType, String> map = myResourceVersionCache.getMapForResourceName(RESOURCE_NAME);
		assertEquals(ENTITY_COUNT, map.size());
		for (int i = 0; i < ENTITY_COUNT; ++i) {
			assertEquals("" + i, map.get(buildIdDt(i)));
		}

		myResourceVersionCache.clear();
		assertFalse(myResourceVersionCache.hasEntriesForResourceName(RESOURCE_NAME));
		for (int i = 0; i < ENTITY_COUNT; ++i) {
			assertNull(myResourceVersionCache.getVersionForResourceId(buildIdDt(i)));
		}
	}

	@Nonnull
	private ResourceVersionMap buildResourceVersionMap() {
		List<ResourceTable> ids = new ArrayList<>();
		for (int i = 0; i < ENTITY_COUNT; ++i) {
			ids.add(createEntity(i, i));
		}
		ResourceVersionMap resourceVersionMap = ResourceVersionMap.fromResourceIds(ids);
		return resourceVersionMap;
	}

	private IdDt buildIdDt(int theId) {
		return buildIdDt("" + theId);
	}

	private IdDt buildIdDt(String theId) {
		return new IdDt(RESOURCE_NAME, theId);
	}

	private static ResourceTable createEntity(long theId, int theVersion) {
		ResourceTable searchParamEntity = new ResourceTable();
		searchParamEntity.setResourceType(RESOURCE_NAME);
		searchParamEntity.setId(theId);
		searchParamEntity.setVersion(theVersion);
		return searchParamEntity;
	}

}
