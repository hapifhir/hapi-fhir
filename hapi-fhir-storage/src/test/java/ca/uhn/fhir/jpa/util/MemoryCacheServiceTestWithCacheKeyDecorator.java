package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
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
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "HELLO", JpaPid.fromId(1L));
		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "GOODBYE", JpaPid.fromId(2L));

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		assertEquals(JpaPid.fromId(1L), svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "HELLO"));
		assertNull(svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "GOODBYE"));
		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		assertNull(svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "HELLO"));
		assertEquals(JpaPid.fromId(2L), svc.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, "GOODBYE"));
	}

	@Test
	public void testGet(){
		MemoryCacheService svc = newMemoryCacheService();

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		Function<String, JpaPid> supplier = k -> {
			switch (k) {
				case "A1": return JpaPid.fromId(1L);
				case "A2": return JpaPid.fromId(2L);
				case "A3": return JpaPid.fromId(3L);
			}
			return null;
		};
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A3", JpaPid.fromId(333L));
		assertEquals(JpaPid.fromId(1L), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "A1", supplier)); // From supplier
		assertEquals(JpaPid.fromId(333L), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "A3", supplier)); // Previously set

		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		supplier = k -> {
			switch (k) {
				case "B1": return JpaPid.fromId(1L);
				case "B2": return JpaPid.fromId(2L);
				case "B3": return JpaPid.fromId(3L);
			}
			return null;
		};
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B3", JpaPid.fromId(333L));
		assertEquals(JpaPid.fromId(1L), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "B1", supplier)); // From supplier
		assertEquals(JpaPid.fromId(333L), svc.get(MemoryCacheService.CacheEnum.MATCH_URL, "B3", supplier)); // Previously set
	}

	@Test
	public void testGetAllPresent() {
		MemoryCacheService svc = newMemoryCacheService();

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A1", JpaPid.fromId(1L));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A2", JpaPid.fromId(2L));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "A3", JpaPid.fromId(3L));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "Z", JpaPid.fromId(10L));
		svc.setCacheKeyDecorator(new CacheKeyDecorator("B"));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B1", JpaPid.fromId(1L));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B2", JpaPid.fromId(2L));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "B3", JpaPid.fromId(3L));
		svc.put(MemoryCacheService.CacheEnum.MATCH_URL, "Z", JpaPid.fromId(20L));

		svc.setCacheKeyDecorator(new CacheKeyDecorator("A"));
		Map<String, Object> outcome = svc.getAllPresent(MemoryCacheService.CacheEnum.MATCH_URL, Lists.newArrayList("A1", "A2", "Z"));
		assertEquals(3, outcome.size());
		assertEquals(JpaPid.fromId(1L), outcome.get("A1"));
		assertEquals(JpaPid.fromId(2L), outcome.get("A2"));
		assertEquals(JpaPid.fromId(10L), outcome.get("Z"));
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
