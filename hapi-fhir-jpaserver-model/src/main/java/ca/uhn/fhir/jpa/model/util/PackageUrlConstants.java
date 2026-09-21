/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.util;

/**
 * Vocabulary for the entries of a package URL allow list, as consumed by the
 * package loader.
 * <p>
 * These constants live in this module rather than alongside the allow list itself
 * so that callers which only need to recognize or validate allow list entries -
 * such as configuration parsing - can do so without depending on the package
 * loader implementation.
 */
// Created by Claude Opus 5
public class PackageUrlConstants {

	/**
	 * An allow list entry which permits every URL, both local and remote.
	 * <p>
	 * This is not a URL scheme, and it will not be parsed as one. It is a distinct
	 * entry value which short circuits allow list matching entirely, and so it
	 * disables the protection the allow list is there to provide.
	 */
	public static final String WILDCARD = "*";

	/**
	 * Prefix identifying an allow list entry which refers to the local filesystem.
	 */
	public static final String FILE_PREFIX = "file:";

	/**
	 * Prefix identifying an allow list entry which refers to the classpath.
	 */
	public static final String CLASSPATH_PREFIX = "classpath:";

	/**
	 * Prefix identifying an allow list entry which refers to a remote server over TLS.
	 * <p>
	 * A remote entry which does not carry this prefix is served over plain HTTP, which is
	 * how the loader decides whether plain HTTP is permitted for that entry.
	 */
	public static final String HTTPS_PREFIX = "https:";

	/**
	 * Max redirects allowed for package url imports
	 */
	public static final int MAX_REDIRECTS = 5;

	private PackageUrlConstants() {}
}
