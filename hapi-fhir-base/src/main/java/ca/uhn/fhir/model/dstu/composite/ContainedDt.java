package ca.uhn.fhir.model.dstu.composite;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "duration")
public class ContainedDt implements IDatatype {

	@Child(name = "resource", type = IResource.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private List<IResource> myContainedResources;

	public List<IResource> getContainedResources() {
		if (myContainedResources == null) {
			myContainedResources = new ArrayList<IResource>();
		}
		return myContainedResources;
	}

	public void setContainedResources(List<IResource> theContainedResources) {
		myContainedResources = theContainedResources;
	}

	@Override
	public boolean isEmpty() {
		return myContainedResources == null || myContainedResources.size() == 0;
	}

}
