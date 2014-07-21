package ca.uhn.fhir.rest.param;

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

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.rest.method.QualifiedParamList;

/**
 * @deprecated Use {@link TokenOrListParam} instead. That class provides similar functionality but is named to be consistent with other types
 */
public class IdentifierListParam implements IQueryParameterOr<IQueryParameterType> {

	private List<IdentifierDt> myIdentifiers = new ArrayList<IdentifierDt>();

	/**
	 * Returns all identifiers associated with this list
	 */
	public List<IdentifierDt> getIdentifiers() {
		return myIdentifiers;
	}

	@Override
	public List<IQueryParameterType> getValuesAsQueryTokens() {
		ArrayList<IQueryParameterType> retVal = new ArrayList<IQueryParameterType>();
		for (IdentifierDt next : myIdentifiers) {
			retVal.add(next);
		}
		return retVal;
	}

	/**
	 * Returns all identifiers associated with this list
	 */
	public void setIdentifiers(List<IdentifierDt> theIdentifiers) {
		myIdentifiers = theIdentifiers;
	}

	@Override
	public void setValuesAsQueryTokens(QualifiedParamList theParameters) {
		for (String string : theParameters) {
			IdentifierDt dt = new IdentifierDt();
			dt.setValueAsQueryToken(null, string);
			myIdentifiers.add(dt);
		}
	}

	public void addIdentifier(IdentifierDt theIdentifierDt) {
		if (theIdentifierDt != null && theIdentifierDt.isEmpty() == false) {
			getIdentifiers().add(theIdentifierDt);
		}
	}

}
