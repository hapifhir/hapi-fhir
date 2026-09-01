/// This package contains classes which manage the execution of searches in the JPA server.
/// There are two styles of search service:
/// * **Stateless Search Service**: ({@link IStatelessJpaSearchSvc})
///   Executes the search without ever looking at the query cache or
///   storing anything in it. Paging in this case is only ever handled using offsets. This can potentially
///   be faster since it avoids the overhead of cache lookups and writes, but it means that the search can
///   be unstable (i.e. results can be skipped or duplicated) if matching resources are added or removed between
///   page loads.
/// * **Cache Aware Search Service**: ({@link ICacheAwareJpaSearchSvc})
///   Consults to the query cache to see if a previously executed search matches the one being
///   performed. Search results are cached and reused if possible.
///
/// # Cache Aware Search Service
///
/// The cache-aware search service first checks with the {@link ISearchCacheSvc} to see if
/// there is a recent search matching the given parameters. If so, it is reused. If not, a
/// new search is created.
///
/// The cache holds PIDs of search results found when executing the search. If there are
/// enough PIDs to satisfy the requested number of results, these PIDs are fetched and returned.
/// If more PIDs are needed, the search is executed again with a larger number of maximum results
/// to fetch and add more PIDs to the cache.
///
/// The Cache Aware Search Service returns a specialized {@link ca.uhn.fhir.rest.api.server.IBundleProvider}
/// which is able to consult the query cache, and write to it if new results are found.
/// * {@link CacheAwareJpaSearchBundleProviderFirstPage} is returned for the first page of search
///   results, meaning when the initial search with its parameters is requested of the server. This
///   class is supplied with a candidate {@link ca.uhn.fhir.jpa.entity.Search} entity populated
///   with the search parameters, _include values, _sort values, etc. As soon as it is queried
///   for results, it first checks if any existing searches can be reused, and starts a new search if not.
/// * {@link CacheAwareJpaSearchBundleProviderSubsequentPage} is returned for subsequent page fetches.
///   It is supplied with a search UUID, and loads the corresponding search from the search cache.
/// Both of these providers extend {@link BaseCacheAwareJpaSearchBundleProvider}, which handles
/// actually performing the search and writing new results to the cache.

package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
