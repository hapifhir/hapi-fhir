/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.packages.loader;

import jakarta.annotation.Nonnull;

import java.net.URI;

/**
 * Fetches package contents for remote package URLs that need something the built-in HTTP fetch cannot
 * provide, typically credentials (for example a cloud storage identity).
 * <p>
 * Any beans of this type are passed to {@link PackageLoaderSvc}, which consults them in order for each remote
 * package URL. The first fetcher whose {@link #canFetch(URI)} returns true fetches the package; if none does,
 * the built-in HTTP fetch is used. Fetchers are consulted only for URLs that have already passed the package URL
 * allow-list, and never for local ({@code file:} or {@code classpath:}) URLs.
 * <p>
 * A single instance is shared across concurrent package loads, so implementations must be thread-safe.
 */
public interface IPackageUrlContentFetcher {

	/**
	 * Whether this fetcher handles the given URL.
	 * <p>
	 * Any credentials the fetcher holds are presented to every URL it claims, so matching must be strict: compare
	 * the parsed scheme, host and path rather than the URL string, which a look-alike host or embedded user info
	 * can satisfy.
	 *
	 * @param theURL the package URL, already accepted by the allow-list
	 * @return true if {@link #fetch(URI)} should be called for this URL; false to leave it to the next fetcher or
	 *         the built-in HTTP fetch
	 */
	boolean canFetch(URI theURL);

	/**
	 * Fetches the package contents. Called only after {@link #canFetch(URI)} returned true for the same URL.
	 * <p>
	 * Must throw rather than return empty contents or fall back to another fetch method on failure.
	 * <p>
	 * The built-in HTTP fetch's own protections, its private-network DNS screening and its per-hop redirect
	 * allow-list checks, do not apply here. An implementation that follows redirects or resolves hosts itself is
	 * responsible for equivalent protection.
	 *
	 * @param thePackageUrl the package URL
	 * @return the package {@code .tgz} contents
	 */
	byte[] fetch(@Nonnull URI thePackageUrl);
}
