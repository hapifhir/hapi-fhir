package ca.uhn.fhir.jpa.packages.loader;

import jakarta.annotation.Nonnull;

import java.net.URI;

public interface IPackageUrlContentFetcher {
	/**
	 * Return true if this fetcher handles the URL.
	 * Must match strictly (scheme adn exact host suffix).
	 * @param theURL - package URL from which fetching will occur
	 */
	boolean canFetch(URI theURL);

	/**
	 * Called only after allow-list validation.
	 * Must throw rather than fall back on failure.
	 *
	 * NB: implementers of this interface must handle any security themselves!
	 * If you elect to override hapi handling of package fetching, you must
	 * handle any security (authentication, dns spoofing, etc) as well.
	 *
	 * @param thePackageUrl - package URL from which to fetch
	 */
	byte[] fetch(@Nonnull URI thePackageUrl);
}
