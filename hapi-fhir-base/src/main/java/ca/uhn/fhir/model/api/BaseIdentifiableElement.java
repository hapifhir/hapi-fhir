package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.CoverageIgnore;

public abstract class BaseIdentifiableElement extends BaseElement implements IIdentifiableElement {

	private static final long serialVersionUID = -7816838417076777914L;
	private String myElementSpecificId;

	@Override
	public String getElementSpecificId() {
		return myElementSpecificId;
	}

	/**
	 * @deprecated Use {@link #getElementSpecificId()} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@CoverageIgnore
	@Deprecated
	@Override
	public IdDt getId() {
		if (myElementSpecificId == null) {
			return new LockedId();
		}
		return new LockedId(myElementSpecificId);
	}

	@Override
	public void setElementSpecificId(String theElementSpecificId) {
		myElementSpecificId = theElementSpecificId;
	}

	/**
	 * @deprecated Use {@link #setElementSpecificId(String)} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@CoverageIgnore
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
	@CoverageIgnore
	@Override
	@Deprecated
	public void setId(String theId) {
		myElementSpecificId = theId;
	}

	@CoverageIgnore
	private static class LockedId extends IdDt {

		@CoverageIgnore
		public LockedId() {
		}

		@CoverageIgnore
		public LockedId(String theElementSpecificId) {
			super(theElementSpecificId);
		}

		@Override
		@CoverageIgnore
		public IdDt setValue(String theValue) throws DataFormatException {
			throw new UnsupportedOperationException(Msg.code(1899) + "Use IElement#setElementSpecificId(String) to set the element ID for an element");
		}

		@Override
		@CoverageIgnore
		public void setValueAsString(String theValue) throws DataFormatException {
			throw new UnsupportedOperationException(Msg.code(1900) + "Use IElement#setElementSpecificId(String) to set the element ID for an element");
		}

	}

}
