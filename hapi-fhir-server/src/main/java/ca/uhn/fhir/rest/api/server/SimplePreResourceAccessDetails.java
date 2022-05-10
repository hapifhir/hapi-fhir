package ca.uhn.fhir.rest.api.server;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;

public class SimplePreResourceAccessDetails implements IPreResourceAccessDetails {

	private final List<IBaseResource> myResources;
	private final boolean[] myBlocked;

	public SimplePreResourceAccessDetails(IBaseResource theResource) {
		this(Collections.singletonList(theResource));
	}

	public <T extends IBaseResource> SimplePreResourceAccessDetails(List<T> theResources) {
		//noinspection unchecked
		myResources = (List<IBaseResource>) theResources;
		myBlocked = new boolean[myResources.size()];
	}

	@Override
	public int size() {
		return myResources.size();
	}

	@Override
	public IBaseResource getResource(int theIndex) {
		return myResources.get(theIndex);
	}

	@Override
	public void setDontReturnResourceAtIndex(int theIndex) {
		myBlocked[theIndex] = true;
	}

	public boolean isDontReturnResourceAtIndex(int theIndex) {
		return myBlocked[theIndex];
	}

	/**
	 * Remove any blocked resources from the list that was passed into the constructor
	 */
	public void applyFilterToList() {
		for (int i = size() - 1; i >= 0; i--) {
			if (isDontReturnResourceAtIndex(i)) {
				myResources.remove(i);
			}
		}
	}
}
