package ca.uhn.fhir.model.primitive;

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

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "idref")
public class IdrefDt extends BasePrimitive<String> {

	private IElement myTarget;
	private String myValue;

	public IElement getTarget() {
		return myTarget;
	}

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() throws DataFormatException {
		return myValue;
	}

	public void setTarget(IElement theTarget) {
		myTarget = theTarget;
	}

	@Override
	public void setValue(String theValue) throws DataFormatException {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		myValue = theValue;
	}

}
