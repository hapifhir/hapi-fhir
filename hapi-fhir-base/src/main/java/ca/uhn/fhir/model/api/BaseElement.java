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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.util.*;

public abstract class BaseElement implements /*IElement, */ISupportsUndeclaredExtensions {

	private static final long serialVersionUID = -3092659584634499332L;
	private List<String> myFormatCommentsPost;
	private List<String> myFormatCommentsPre;
	private Map<String, Object> userData;

	@Child(name = "extension", type = {ExtensionDt.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = false)
	@Description(shortDefinition = "Additional Content defined by implementations", formalDefinition = "May be used to represent additional information that is not part of the basic definition of the resource. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.")
	private List<ExtensionDt> myUndeclaredExtensions;

	/**
	 * May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.
	 */
	@Child(name = "modifierExtension", type = {ExtensionDt.class}, order = 1, min = 0, max = Child.MAX_UNLIMITED, modifier = true, summary = false)
	@Description(shortDefinition = "Extensions that cannot be ignored", formalDefinition = "May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.")
	private List<ExtensionDt> myUndeclaredModifierExtensions;

	@Override
	public ExtensionDt addUndeclaredExtension(boolean theIsModifier, String theUrl) {
		Validate.notEmpty(theUrl, "URL must be populated");

		ExtensionDt retVal = new ExtensionDt(theIsModifier, theUrl);
		if (theIsModifier) {
			getUndeclaredModifierExtensions();
			myUndeclaredModifierExtensions.add(retVal);
		} else {
			getUndeclaredExtensions();
			myUndeclaredExtensions.add(retVal);
		}
		return retVal;
	}

	@Override
	public ExtensionDt addUndeclaredExtension(boolean theIsModifier, String theUrl, IBaseDatatype theValue) {
		Validate.notEmpty(theUrl, "URL must be populated");
		Validate.notNull(theValue, "Value must not be null");
		ExtensionDt retVal = new ExtensionDt(theIsModifier, theUrl, theValue);
		if (theIsModifier) {
			getUndeclaredModifierExtensions();
			myUndeclaredModifierExtensions.add(retVal);
		} else {
			getUndeclaredExtensions();
			myUndeclaredExtensions.add(retVal);
		}
		return retVal;
	}

	@Override
	public void addUndeclaredExtension(ExtensionDt theExtension) {
		Validate.notNull(theExtension, "Extension can not be null");
		if (theExtension.isModifier()) {
			getUndeclaredModifierExtensions();
			myUndeclaredModifierExtensions.add(theExtension);
		} else {
			getUndeclaredExtensions();
			myUndeclaredExtensions.add(theExtension);
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
	public List<String> getFormatCommentsPost() {
		if (myFormatCommentsPost == null)
			myFormatCommentsPost = new ArrayList<String>();
		return myFormatCommentsPost;
	}

	@Override
	public List<String> getFormatCommentsPre() {
		if (myFormatCommentsPre == null)
			myFormatCommentsPre = new ArrayList<String>();
		return myFormatCommentsPre;
	}

	@Override
	public List<ExtensionDt> getUndeclaredExtensions() {
		if (myUndeclaredExtensions == null) {
			myUndeclaredExtensions = new ArrayList<ExtensionDt>();
		}
		return (myUndeclaredExtensions);
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
		return (myUndeclaredModifierExtensions);
	}

	@Override
	public boolean hasFormatComment() {
		return (myFormatCommentsPre != null && !myFormatCommentsPre.isEmpty()) || (myFormatCommentsPost != null && !myFormatCommentsPost.isEmpty());
	}

	@Override
	public Object getUserData(String name) {
		if (userData == null)
			return null;
		return userData.get(name);
	}

	@Override
	public void setUserData(String name, Object value) {
		if (userData == null) {
			userData = new HashMap<>();
		}
		userData.put(name, value);
	}

	/**
	 * Intended to be called by extending classes {@link #isEmpty()} implementations, returns <code>true</code> if all
	 * content in this superclass instance is empty per the semantics of {@link #isEmpty()}.
	 */
	protected boolean isBaseEmpty() {
		if (myUndeclaredExtensions != null) {
			for (ExtensionDt next : myUndeclaredExtensions) {
				if (next == null) {
					continue;
				}
				if (!next.isEmpty()) {
					return false;
				}
			}
		}
		if (myUndeclaredModifierExtensions != null) {
			for (ExtensionDt next : myUndeclaredModifierExtensions) {
				if (next == null) {
					continue;
				}
				if (!next.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

}
