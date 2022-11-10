package ca.uhn.fhir.sl.cache;

import ca.uhn.fhir.i18n.Msg;

import java.util.Iterator;
import java.util.ServiceLoader;

@SuppressWarnings("unchecked")
public class CacheFactory {

	static ServiceLoader<CacheProvider> loader = ServiceLoader.load(CacheProvider.class);

	public static Iterator<CacheProvider> providers(boolean refresh) {
		if (refresh) {
			loader.reload();
		}
		return loader.iterator();
	}

	public static <K, V> Cache<K, V> build(long timeoutMillis) {
		if (providers(false).hasNext()) {
			return providers(false).next().create(timeoutMillis);
		}
		throw new RuntimeException(Msg.code(2200) + "No Cache Service Providers found. Choose between hapi-fhir-caching-caffeine (Default) and hapi-fhir-caching-guava (Android)");
	}

	public static <K, V> LoadingCache<K, V> build(long timeoutMillis, CacheLoader<K, V> cacheLoader) {
		if (providers(false).hasNext()) {
			return providers(false).next().create(timeoutMillis, cacheLoader);
		}
		throw new RuntimeException(Msg.code(2201) + "No Cache Service Providers found. Choose between hapi-fhir-caching-caffeine (Default) and hapi-fhir-caching-guava (Android)");
	}

	public static <K, V> Cache<K, V> build(long timeoutMillis, long maximumSize) {
		if (providers(false).hasNext()) {
			return providers(false).next().create(timeoutMillis, maximumSize);
		}
		throw new RuntimeException(Msg.code(2202) + "No Cache Service Providers found. Choose between hapi-fhir-caching-caffeine (Default) and hapi-fhir-caching-guava (Android)");
	}

	public static <K, V> LoadingCache<K, V> build(long timeoutMillis, long maximumSize, CacheLoader<K, V> cacheLoader) {
		if (providers(false).hasNext()) {
			return providers(false).next().create(timeoutMillis, maximumSize, cacheLoader);
		}
		throw new RuntimeException(Msg.code(2203) + "No Cache Service Providers found. Choose between hapi-fhir-caching-caffeine (Default) and hapi-fhir-caching-guava (Android)");
	}
}
