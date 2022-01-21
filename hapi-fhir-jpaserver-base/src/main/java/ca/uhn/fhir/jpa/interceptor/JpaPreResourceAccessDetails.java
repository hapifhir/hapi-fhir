package ca.uhn.fhir.jpa.interceptor;

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

import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.util.ICallable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * THIS CLASS IS NOT THREAD SAFE
 */
@NotThreadSafe
public class JpaPreResourceAccessDetails implements IPreResourceAccessDetails {

	private final List<ResourcePersistentId> myResourcePids;
	private final boolean[] myBlocked;
	private final ICallable<ISearchBuilder> mySearchBuilderSupplier;
	private List<IBaseResource> myResources;

	public JpaPreResourceAccessDetails(List<ResourcePersistentId> theResourcePids, ICallable<ISearchBuilder> theSearchBuilderSupplier) {
		myResourcePids = theResourcePids;
		myBlocked = new boolean[myResourcePids.size()];
		mySearchBuilderSupplier = theSearchBuilderSupplier;
	}

	@Override
	public int size() {
		return myResourcePids.size();
	}

	@Override
	public IBaseResource getResource(int theIndex) {
		if (myResources == null) {
			myResources = new ArrayList<>(myResourcePids.size());
			mySearchBuilderSupplier.call().loadResourcesByPid(myResourcePids, Collections.emptySet(), myResources, false, null);
		}
		return myResources.get(theIndex);
	}

	@Override
	public void setDontReturnResourceAtIndex(int theIndex) {
		myBlocked[theIndex] = true;
	}

	public boolean isDontReturnResourceAtIndex(int theIndex) {
		return myBlocked[theIndex];
	}
}
