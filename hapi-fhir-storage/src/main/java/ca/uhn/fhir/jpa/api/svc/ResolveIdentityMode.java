/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.svc;

/**
 * Resolution mode parameter for methods on {@link IIdHelperService}
 */
public class ResolveIdentityMode {

	private final boolean myIncludeDeleted;
	private final boolean myUseCache;
	private final boolean myFailOnDeleted;

	/**
	 * Non-instantiable. Use the factory methods on this class.
	 */
	private ResolveIdentityMode(boolean theIncludeDeleted, boolean theFailOnDeleted, boolean theUseCache) {
		myIncludeDeleted = theIncludeDeleted;
		myUseCache = theUseCache;
		myFailOnDeleted = theFailOnDeleted;
	}

	public boolean isUseCache(boolean theDeleteEnabled) {
		if (myUseCache) {
			return true;
		} else {
			return !theDeleteEnabled;
		}
	}

	public boolean isIncludeDeleted() {
		return myIncludeDeleted;
	}

	public boolean isFailOnDeleted() {
		return myFailOnDeleted;
	}

	/**
	 * Deleted resource identities can be included in the results
	 */
	public static Builder includeDeleted() {
		return new Builder(true, false);
	}

	/**
	 * Deleted resource identities should be excluded from the results
	 */
	public static Builder excludeDeleted() {
		return new Builder(false, false);
	}

	/**
	 * Throw a {@link ca.uhn.fhir.rest.server.exceptions.ResourceGoneException} if
	 * any of the supplied IDs corresponds to a deleted resource
	 */
	public static Builder failOnDeleted() {
		return new Builder(false, true);
	}

	public static class Builder {

		private final boolean myIncludeDeleted;
		private final boolean myFailOnDeleted;

		private Builder(boolean theIncludeDeleted, boolean theFailOnDeleted) {
			myIncludeDeleted = theIncludeDeleted;
			myFailOnDeleted = theFailOnDeleted;
		}

		/**
		 * Cached results are acceptable. This mode is obviously more efficient since it'll always
		 * try the cache first, but it should not be used in cases where it matters whether the
		 * deleted status has changed for a resource.
		 */
		public ResolveIdentityMode cacheOk() {
			return new ResolveIdentityMode(myIncludeDeleted, myFailOnDeleted, true);
		}

		/**
		 * In this mode, the cache won't be used unless deletes are disabled on this server
		 * (meaning that the deleted status of a resource is not able to change)
		 */
		public ResolveIdentityMode noCacheUnlessDeletesDisabled() {
			return new ResolveIdentityMode(myIncludeDeleted, myFailOnDeleted, false);
		}
	}
}
