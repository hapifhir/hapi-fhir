package ca.uhn.fhir.model.api;

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

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.primitive.StringDt;

@DatatypeDef(name="Extension") 
public class ExtensionDt extends BaseIdentifiableElement implements ICompositeDatatype {

	private boolean myModifier;
	
	@Child(name="url", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myUrl;

	@Child(name="value", type=IDatatype.class, order=1, min=0, max=1)	
	private IElement myValue;
	
	public ExtensionDt() {
	}

	public ExtensionDt(boolean theIsModifier) {
		myModifier = theIsModifier;
	}

	public ExtensionDt(boolean theIsModifier, String theUrl) {
		Validate.notEmpty(theUrl, "URL must be populated");

		myModifier = theIsModifier;
		myUrl = new StringDt(theUrl);
	}

	public ExtensionDt(boolean theIsModifier, String theUrl, IDatatype theValue) {
		Validate.notEmpty(theUrl, "URL must be populated");
		Validate.notNull(theValue, "Value must not be null");

		myModifier = theIsModifier;
		myUrl = new StringDt(theUrl);
		myValue=theValue;
	}

	public StringDt getUrl() {
		if (myUrl==null) {
			myUrl=new StringDt();
		}
		return myUrl;
	}

	public String getUrlAsString() {
		return getUrl().getValue();
	}

	/**
	 * Returns the value of this extension, if one exists.
	 * <p>
	 * Note that if this extension contains extensions (instead of a datatype) then <b>this method will return null</b>. In that case, you must use {@link #getUndeclaredExtensions()} and
	 * {@link #getUndeclaredModifierExtensions()} to retrieve the child extensions.
	 * </p>
	 */
	public IElement getValue() {
		return myValue;
	}

	/**
	 * Returns the value of this extension, casted to a primitive datatype. This is a convenience method which should only be called if you are sure that the value for this particular extension will
	 * be a primitive.
	 * <p>
	 * Note that if this extension contains extensions (instead of a datatype) then <b>this method will return null</b>. In that case, you must use {@link #getUndeclaredExtensions()} and
	 * {@link #getUndeclaredModifierExtensions()} to retrieve the child extensions.
	 * </p>
	 * 
	 * @throws ClassCastException
	 *             If the value of this extension is not a primitive datatype
	 */
	public IPrimitiveDatatype<?> getValueAsPrimitive() {
		if (!(getValue() instanceof IPrimitiveDatatype)) {
			throw new ClassCastException("Extension with URL["+myUrl+"] can not be cast to primitive type, type is: "+ getClass().getCanonicalName());
		}
		return (IPrimitiveDatatype<?>) getValue();
	}
	
	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && (myValue == null || myValue.isEmpty());
	}

	public boolean isModifier() {
		return myModifier;
	}

	public void setModifier(boolean theModifier) {
		myModifier = theModifier;
	}

	public ExtensionDt setUrl(String theUrl) {
		myUrl = new StringDt(theUrl);
		return this;
	}

	public ExtensionDt setUrl(StringDt theUrl) {
		myUrl = theUrl;
		return this;
	}

	public void setValue(IElement theValue) {
		myValue = theValue;
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return new ArrayList<T>();
	}

}
