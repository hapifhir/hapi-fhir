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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * THIS CLASS IS NOT THREAD SAFE
 */
@NotThreadSafe
public class JpaPreResourceAccessDetails implements IPreResourceAccessDetails {

	private final List<JpaPid> myResourcePids;
	private final boolean[] myBlocked;
	private List<IBaseResource> myResources;

	/**
	 * Constructor
	 */
	public JpaPreResourceAccessDetails(List<JpaPid> theResourcePids, List<IBaseResource> theUnsyncedResources) {
		Validate.isTrue(
				theResourcePids.size() == theUnsyncedResources.size(),
				"Size mismatch - theResourcePids.size() %d != theUnsyncedResources.size() %d",
				theResourcePids.size(),
				theUnsyncedResources.size());
		myResourcePids = theResourcePids;
		myBlocked = new boolean[theResourcePids.size()];
		myResources = theUnsyncedResources;
	}

	@Override
	public int size() {
		return myResourcePids.size();
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
}
