package ca.uhn.fhir.jpa.search.builder.searchquery;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IncludesIterator extends BaseIterator<ResourcePersistentId> implements Iterator<ResourcePersistentId> {

	private final RequestDetails myRequest;
	private final Set<ResourcePersistentId> myCurrentPids;
	private Iterator<ResourcePersistentId> myCurrentIterator;
	private ResourcePersistentId myNext;

	private final SearchBuilder mySearchBuilderParent;

	IncludesIterator(
		Set<ResourcePersistentId> thePidSet,
		RequestDetails theRequest,
		SearchBuilder theSearchBuilderParent
	) {
		myCurrentPids = new HashSet<>(thePidSet);
		myCurrentIterator = null;
		myRequest = theRequest;
		mySearchBuilderParent = theSearchBuilderParent;
	}

	private void fetchNext() {
		while (myNext == null) {

			if (myCurrentIterator == null) {
				Set<Include> includes = Collections.singleton(new Include("*", true));
				Set<ResourcePersistentId> newPids = mySearchBuilderParent.loadIncludes(
					mySearchBuilderParent.getContext(),
					mySearchBuilderParent.getEntityManager(),
					myCurrentPids,
					includes,
					false,
					mySearchBuilderParent.getParams().getLastUpdated(),
					mySearchBuilderParent.getSearchUuid(),
					myRequest,
					null);
				myCurrentIterator = newPids.iterator();
			}

			if (myCurrentIterator.hasNext()) {
				myNext = myCurrentIterator.next();
			} else {
				myNext = QueryConstants.NO_MORE;
			}

		}
	}

	@Override
	public boolean hasNext() {
		fetchNext();
		return !QueryConstants.NO_MORE.equals(myNext);
	}

	@Override
	public ResourcePersistentId next() {
		fetchNext();
		ResourcePersistentId retVal = myNext;
		myNext = null;
		return retVal;
	}

}
