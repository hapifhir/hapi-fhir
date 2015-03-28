package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseIdentifiableElement extends BaseElement implements IIdentifiableElement {

	private String myElementSpecificId;

	@Override
	public String getElementSpecificId() {
		return myElementSpecificId;
	}

	/**
	 * @deprecated Use {@link #getElementSpecificId()} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@Deprecated
	@Override
	public IdDt getId() {
		if (myElementSpecificId == null) {
			return new LockedId();
		} else {
			return new LockedId(myElementSpecificId);
		}
	}

	@Override
	public void setElementSpecificId(String theElementSpecificId) {
		myElementSpecificId = theElementSpecificId;
	}

	/**
	 * @deprecated Use {@link #setElementSpecificId(String)} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@Deprecated
	@Override
	public void setId(IdDt theId) {
		if (theId == null) {
			myElementSpecificId = null;
		} else {
			myElementSpecificId = theId.getValue();
		}
	}

	/**
	 * @deprecated Use {@link #setElementSpecificId(String)} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@Override
	@Deprecated
	public void setId(String theId) {
		myElementSpecificId = theId;
	}

	private static class LockedId extends IdDt {

		public LockedId() {
		}

		public LockedId(String theElementSpecificId) {
			super(theElementSpecificId);
		}

		@Override
		public IdDt setValue(String theValue) throws DataFormatException {
			throw new UnsupportedOperationException("Use IElement#setElementSpecificId(String) to set the element ID for an element");
		}

		@Override
		public void setValueAsString(String theValue) throws DataFormatException {
			throw new UnsupportedOperationException("Use IElement#setElementSpecificId(String) to set the element ID for an element");
		}

	}

}
