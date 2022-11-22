package ca.uhn.fhir.sl.cache;

import ca.uhn.fhir.i18n.Msg;

import java.util.Iterator;
import java.util.ServiceLoader;

@SuppressWarnings("unchecked")
public class CacheFactory {

	@SuppressWarnings("rawtypes")
	static ServiceLoader<CacheProvider> loader = ServiceLoader.load(CacheProvider.class);

	@SuppressWarnings("rawtypes")
	private static synchronized <K, V> CacheProvider<K, V> getCacheProvider() {
		Iterator<CacheProvider> iterator = loader.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new RuntimeException(Msg.code(2200) + "No Cache Service Providers found. Choose between hapi-fhir-caching-caffeine (Default) and hapi-fhir-caching-guava (Android)");
	}

	public static <K, V> Cache<K, V> build(long theTimeoutMillis) {
		CacheProvider<Object, Object> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis);
	}

	public static  <K, V> LoadingCache<K, V> build(long theTimeoutMillis, CacheLoader<K, V> theCacheLoader) {
		CacheProvider<K, V> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis, theCacheLoader);
	}

	public static  <K, V> Cache<K, V> build(long theTimeoutMillis, long theMaximumSize) {
		CacheProvider<Object, Object> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis, theMaximumSize);
	}

	public static  <K, V> LoadingCache<K, V> build(long theTimeoutMillis, long theMaximumSize, CacheLoader<K, V> cacheLoader) {
		CacheProvider<K, V> cacheProvider = getCacheProvider();
		return cacheProvider.create(theTimeoutMillis, theMaximumSize, cacheLoader);
	}
}
