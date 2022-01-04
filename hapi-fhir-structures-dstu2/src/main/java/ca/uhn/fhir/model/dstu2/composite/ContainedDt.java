package ca.uhn.fhir.model.dstu2.composite;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
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

import ca.uhn.fhir.i18n.Msg;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;

@DatatypeDef(name = "contained")
public class ContainedDt extends BaseContainedDt {

	@Child(name = "resource", type = IResource.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private List<IResource> myContainedResources;

	@Override
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

	@Override
	public Object getUserData(String theName) {
		throw new UnsupportedOperationException(Msg.code(580));
	}

	@Override
	public void setUserData(String theName, Object theValue) {
		throw new UnsupportedOperationException(Msg.code(581));
	}

}
