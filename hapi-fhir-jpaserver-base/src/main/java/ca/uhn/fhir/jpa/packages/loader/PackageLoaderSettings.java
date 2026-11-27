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

public class PackageLoaderSettings {

	private final PackageUrlAllowList myPackageUrlAllowList;
	private final boolean myTrustedProxyEnabled;

	public PackageLoaderSettings(PackageUrlAllowList theAllowList) {
		this(theAllowList, false);
	}

	/**
	 * @param theAllowList          the package URL allow-list
	 * @param theTrustedProxyEnabled whether JVM HTTP proxy properties may route remote fetches through a
	 *                              trusted proxy; disabled by default
	 * <p>
	 * When enabled, the loader prevalidates the destination with its local DNS protections before sending the
	 * request, but it cannot enforce the IP address ultimately selected by proxy-side DNS. The trusted proxy must
	 * therefore enforce equivalent destination and private-network restrictions.
	 */
	public PackageLoaderSettings(PackageUrlAllowList theAllowList, boolean theTrustedProxyEnabled) {
		myPackageUrlAllowList = theAllowList;
		myTrustedProxyEnabled = theTrustedProxyEnabled;
	}

	public PackageUrlAllowList getPackageUrlAllowList() {
		return myPackageUrlAllowList;
	}

	public boolean isTrustedProxyEnabled() {
		return myTrustedProxyEnabled;
	}
}
