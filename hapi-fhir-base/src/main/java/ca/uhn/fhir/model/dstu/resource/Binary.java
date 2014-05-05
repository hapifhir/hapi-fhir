package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
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

import java.util.List;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;

@ResourceDef(name="Binary", profile="http://hl7.org/fhir/profiles/Binary", id="binary")
public class Binary  extends BaseResource implements IResource {

	// TODO: implement binary
	
	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public List<IElement> getAllPopulatedChildElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContainedDt getContained() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NarrativeDt getText() {
		throw new IllegalStateException();
	}

}
