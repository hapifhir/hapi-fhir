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

import java.util.Objects;

import static ca.uhn.fhir.jpa.model.util.PackageUrlConstants.WILDCARD;

public class AllowedUrlPrefix {

	/**
	 * Creator for AllowAll for Url Prefix Whitelist.
	 * Do not use in production.
	 */
	public static AllowedUrlPrefix all() {
		return new AllowedUrlPrefix(WILDCARD, false);
	}

	/**
	 * The URL (prefix) to whitelist/allow
	 */
	private final String myUrl;
	/**
	 * Whether or not the url is on a private network
	 */
	private final boolean myIsPrivateNetwork;

	public AllowedUrlPrefix(String theUrl, boolean theIsPrivateNetwork) {
		myUrl = theUrl;
		myIsPrivateNetwork = theIsPrivateNetwork;
	}

	public String getUrl() {
		return myUrl;
	}

	public boolean isPrivateNetwork() {
		return myIsPrivateNetwork;
	}

	@Override
	public String toString() {
		return "url:" + myUrl + "|" + "private:" + myIsPrivateNetwork;
	}

	@Override
	public boolean equals(Object theOther) {
		if (theOther == null) {
			return false;
		}
		if (theOther instanceof AllowedUrlPrefix that) {
			return Objects.equals(getUrl(), that.getUrl()) && this.isPrivateNetwork() == that.isPrivateNetwork();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myUrl, myIsPrivateNetwork);
	}
}
