/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SimplePreResourceShowDetails implements IPreResourceShowDetails {

	private static final IBaseResource[] EMPTY_RESOURCE_ARRAY = new IBaseResource[0];

	private final IBaseResource[] myResources;
	private final boolean[] myResourceMarkedAsSubset;

	/**
	 * Constructor for a single resource
	 */
	public SimplePreResourceShowDetails(IBaseResource theResource) {
		this(Lists.newArrayList(theResource));
	}

	/**
	 * Constructor for a collection of resources
	 */
	public <T extends IBaseResource> SimplePreResourceShowDetails(Collection<T> theResources) {
		myResources = theResources.toArray(EMPTY_RESOURCE_ARRAY);
		myResourceMarkedAsSubset = new boolean[myResources.length];
	}

	@Override
	public int size() {
		return myResources.length;
	}

	@Override
	public IBaseResource getResource(int theIndex) {
		return myResources[theIndex];
	}

	@Override
	public void setResource(int theIndex, IBaseResource theResource) {
		Validate.isTrue(theIndex >= 0, "Invalid index %d - theIndex must not be < 0", theIndex);
		Validate.isTrue(
				theIndex < myResources.length,
				"Invalid index {} - theIndex must be < %d",
				theIndex,
				myResources.length);
		myResources[theIndex] = theResource;
	}

	@Override
	public void markResourceAtIndexAsSubset(int theIndex) {
		Validate.isTrue(theIndex >= 0, "Invalid index %d - theIndex must not be < 0", theIndex);
		Validate.isTrue(
				theIndex < myResources.length,
				"Invalid index {} - theIndex must be < %d",
				theIndex,
				myResources.length);
		myResourceMarkedAsSubset[theIndex] = true;
	}

	@Override
	public List<IBaseResource> getAllResources() {
		ArrayList<IBaseResource> retVal = new ArrayList<>(myResources.length);
		Arrays.stream(myResources).filter(Objects::nonNull).forEach(retVal::add);
		return Collections.unmodifiableList(retVal);
	}

	@Override
	public Iterator<IBaseResource> iterator() {
		return Arrays.asList(myResources).iterator();
	}

	public List<IBaseResource> toList() {
		return Lists.newArrayList(myResources);
	}
}
