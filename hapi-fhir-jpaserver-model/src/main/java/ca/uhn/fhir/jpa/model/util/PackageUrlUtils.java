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

import okhttp3.Dns;
import org.hl7.fhir.utilities.http.ManagedWebAccessUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

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
public class PackageUrlUtils {

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
	 * Check if a given URL is a private network url or not.
	 * *
	 * NB: do not use this method if it's to be called frequently.
	 * Try to minimize to only low-traffic paths
	 *
	 * @param theUrl - the url to check
	 * @return - true if a local address; false otherwise
	 */
	public static boolean isUrlPrivateNetwork(String theUrl) {
		try {
			/*
			 * underlying method uses "throw exception"...
			 * we're doing a try catch which is an anti-pattern.
			 *
			 * but the only caller of this method is done at setup so maybe ok
			 */
			List<InetAddress> addresses = Dns.SYSTEM.lookup(theUrl);
			for (InetAddress address : addresses) {
				ManagedWebAccessUtils.throwExceptionIfNonPublicAddress(address, theUrl);
			}
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	private PackageUrlUtils() {}
}
