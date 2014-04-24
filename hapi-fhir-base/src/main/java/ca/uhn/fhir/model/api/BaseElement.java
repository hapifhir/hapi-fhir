package ca.uhn.fhir.model.api;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.primitive.IdDt;

public abstract class BaseElement implements IIdentifiableElement, ISupportsUndeclaredExtensions {

	private IdDt myId;
	private List<ExtensionDt> myUndeclaredExtensions;
	private List<ExtensionDt> myUndeclaredModifierExtensions;

	@Override
	public void addUndeclaredExtension(boolean theIsModifier, String theUrl, IDatatype theValue) {
		Validate.notEmpty(theUrl, "URL must be populated");
		Validate.notNull(theValue, "Value must not be null");
		getUndeclaredExtensions().add(new ExtensionDt(theIsModifier, theUrl, theValue));
	}

	@Override
	public void addUndeclaredExtension(ExtensionDt theExtension) {
		Validate.notNull(theExtension, "Extension can not be null");
		if (theExtension.isModifier()) {
			getUndeclaredModifierExtensions().add(theExtension);
		} else {
			getUndeclaredExtensions().add(theExtension);
		}
	}

	@Override
	public List<ExtensionDt> getAllUndeclaredExtensions() {
		ArrayList<ExtensionDt> retVal = new ArrayList<ExtensionDt>();
		if (myUndeclaredExtensions != null) {
			retVal.addAll(myUndeclaredExtensions);
		}
		if (myUndeclaredModifierExtensions != null) {
			retVal.addAll(myUndeclaredModifierExtensions);
		}
		return Collections.unmodifiableList(retVal);
	}

	@Override
	public IdDt getId() {
		if (myId == null) {
			myId = new IdDt();
		}
		return myId;
	}

	@Override
	public List<ExtensionDt> getUndeclaredExtensions() {
		if (myUndeclaredExtensions == null) {
			myUndeclaredExtensions = new ArrayList<ExtensionDt>();
		}
		return myUndeclaredExtensions;
	}

	@Override
	public List<ExtensionDt> getUndeclaredExtensionsByUrl(String theUrl) {
		org.apache.commons.lang3.Validate.notNull(theUrl, "URL can not be null");
		ArrayList<ExtensionDt> retVal = new ArrayList<ExtensionDt>();
		for (ExtensionDt next : getAllUndeclaredExtensions()) {
			if (theUrl.equals(next.getUrlAsString())) {
				retVal.add(next);
			}
		}
		return Collections.unmodifiableList(retVal);
	}

	@Override
	public List<ExtensionDt> getUndeclaredModifierExtensions() {
		if (myUndeclaredModifierExtensions == null) {
			myUndeclaredModifierExtensions = new ArrayList<ExtensionDt>();
		}
		return myUndeclaredModifierExtensions;
	}

	@Override
	public void setId(IdDt theId) {
		myId = theId;
	}

	/**
	 * Intended to be called by extending classes {@link #isEmpty()}
	 * implementations, returns <code>true</code> if all content in this
	 * superclass instance is empty per the semantics of {@link #isEmpty()}.
	 */
	protected boolean isBaseEmpty() {
		if (myUndeclaredExtensions != null) {
			for (ExtensionDt next : myUndeclaredExtensions) {
				if (!next.isEmpty()) {
					return false;
				}
			}
		}
		if (myUndeclaredModifierExtensions != null) {
			for (ExtensionDt next : myUndeclaredModifierExtensions) {
				if (!next.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

}
