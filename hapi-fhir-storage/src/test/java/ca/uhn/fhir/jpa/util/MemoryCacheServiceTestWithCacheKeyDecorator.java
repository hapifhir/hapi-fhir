package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MemoryCacheServiceTestWithCacheKeyDecorator {

	@Test
	public void testGetIfPresent(){
		MemoryCacheService svc = newMemoryCacheService();

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "HELLO", new ResourcePersistentId(1));
		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "GOODBYE", new ResourcePersistentId(2));

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		assertEquals(new ResourcePersistentId(1), svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "HELLO"));
		assertNull(svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "GOODBYE"));
		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		assertNull(svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "HELLO"));
		assertEquals(new ResourcePersistentId(2), svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "GOODBYE"));
	}

	@Test
	public void testGet(){
		MemoryCacheService svc = newMemoryCacheService();

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		Function<String, ResourcePersistentId> supplier = k -> {
			switch (k) {
				case "A1": return new ResourcePersistentId(1);
				case "A2": return new ResourcePersistentId(2);
				case "A3": return new ResourcePersistentId(3);
			}
			return null;
		};
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A3", new ResourcePersistentId(333));
		assertEquals(new ResourcePersistentId(1), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "A1", supplier)); // From supplier
		assertEquals(new ResourcePersistentId(333), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "A3", supplier)); // Previously set

		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		supplier = k -> {
			switch (k) {
				case "B1": return new ResourcePersistentId(1);
				case "B2": return new ResourcePersistentId(2);
				case "B3": return new ResourcePersistentId(3);
			}
			return null;
		};
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B3", new ResourcePersistentId(333));
		assertEquals(new ResourcePersistentId(1), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "B1", supplier)); // From supplier
		assertEquals(new ResourcePersistentId(333), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "B3", supplier)); // Previously set
	}

	@Test
	public void testGetAllPresent() {
		MemoryCacheService svc = newMemoryCacheService();

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A1", new ResourcePersistentId(1));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A2", new ResourcePersistentId(2));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A3", new ResourcePersistentId(3));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "Z", new ResourcePersistentId(10));
		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B1", new ResourcePersistentId(1));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B2", new ResourcePersistentId(2));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B3", new ResourcePersistentId(3));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "Z", new ResourcePersistentId(20));

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		Map<String, Object> outcome = svc.getAllPresent(MemoryCacheService.CacheEnum.MATCH_URL, Lists.newArrayList("A1", "A2", "Z"));
		assertEquals(3, outcome.size());
		assertEquals(new ResourcePersistentId(1), outcome.get("A1"));
		assertEquals(new ResourcePersistentId(2), outcome.get("A2"));
		assertEquals(new ResourcePersistentId(10), outcome.get("Z"));
	}

	@Nonnull
	private static MemoryCacheService newMemoryCacheService() {
		MemoryCacheService svc = new MemoryCacheService();
		svc.setDaoConfigForUnitTest(new DaoConfig());
		svc.start();
		return svc;
	}


	private static class CacheKeyDecorator implements MemoryCacheService.ICacheKeyDecorator {

		private final Object myDecorator;

		private CacheKeyDecorator(Object theDecorator) {
			myDecorator = theDecorator;
		}

		@Override
		public Object getCacheKeyDecoration() {
			return myDecorator;
		}
	}


}
