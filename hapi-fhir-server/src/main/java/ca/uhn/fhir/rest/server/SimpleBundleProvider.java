package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;

public class SimpleBundleProvider implements IBundleProvider {

	private final List<IBaseResource> myList;
	private final String myUuid;
	private Integer myPreferredPageSize;

	public SimpleBundleProvider(List<IBaseResource> theList) {
		this(theList, null);
	}

	public SimpleBundleProvider(IBaseResource theResource) {
		myList = Collections.singletonList(theResource);
		myUuid = null;
	}

	/**
	 * Create an empty bundle
	 */
	public SimpleBundleProvider() {
		myList = Collections.emptyList();
		myUuid = null;
	}

	public SimpleBundleProvider(List<IBaseResource> theList, String theUuid) {
		myList = theList;
		myUuid = theUuid;
	}

	@Override
	public InstantDt getPublished() {
		return InstantDt.withCurrentTime();
	}

	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		return myList.subList(theFromIndex, Math.min(theToIndex, myList.size()));
	}

	@Override
	public String getUuid() {
		return myUuid;
	}

	/**
	 * Defaults to null
	 */
	@Override
	public Integer preferredPageSize() {
		return myPreferredPageSize;
	}

	/**
	 * Sets the preferred page size to be returned by {@link #preferredPageSize()}.
	 * Default is <code>null</code>.
	 */
	public void setPreferredPageSize(Integer thePreferredPageSize) {
		myPreferredPageSize = thePreferredPageSize;
	}

	@Override
	public Integer size() {
		return myList.size();
	}

}
