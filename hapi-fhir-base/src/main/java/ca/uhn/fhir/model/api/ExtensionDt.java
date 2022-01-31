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
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.primitive.StringDt;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;

import java.util.ArrayList;
import java.util.List;

@DatatypeDef(name = "Extension")
public class ExtensionDt extends BaseIdentifiableElement implements ICompositeDatatype, IBaseExtension<ExtensionDt, IDatatype> {

	private static final long serialVersionUID = 6399491332783085935L;

	private boolean myModifier;
	
	@Child(name="url", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myUrl;

	@Child(name = "value", type = IDatatype.class, order = 1, min = 0, max = 1)
	private IBaseDatatype myValue;
	
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

	public ExtensionDt(boolean theIsModifier, String theUrl, IBaseDatatype theValue) {
		Validate.notEmpty(theUrl, "URL must be populated");
		Validate.notNull(theValue, "Value must not be null");

		myModifier = theIsModifier;
		myUrl = new StringDt(theUrl);
		myValue=theValue;
	}

	/**
	 * Returns the URL for this extension.
	 * <p>
	 * Note that before HAPI 0.9 this method returned a {@link StringDt} but as of
	 * HAPI 0.9 this method returns a plain string. This was changed because it does not make sense to use a StringDt here
	 * since the URL itself can not contain extensions and it was therefore misleading.
	 * </p>
	 */
	@Override
	public String getUrl() {
		return myUrl != null ? myUrl.getValue() : null;
	}

	/**
	 * Retained for backward compatibility
	 *
	 * @see ExtensionDt#getUrl()
	 */
	public String getUrlAsString() {
		return getUrl();
	}

	/**
	 * Returns the value of this extension, if one exists.
	 * <p>
	 * Note that if this extension contains extensions (instead of a datatype) then <b>this method will return null</b>. In that case, you must use {@link #getUndeclaredExtensions()} and
	 * {@link #getUndeclaredModifierExtensions()} to retrieve the child extensions.
	 * </p>
	 */
	@Override
	public IBaseDatatype getValue() {
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
			throw new ClassCastException(Msg.code(1887) + "Extension with URL["+myUrl+"] can not be cast to primitive type, type is: "+ getClass().getCanonicalName());
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

	@Override
	public ExtensionDt setUrl(String theUrl) {
		myUrl = theUrl != null ? new StringDt(theUrl) : myUrl;
		return this;
	}

	public ExtensionDt setUrl(StringDt theUrl) {
		myUrl = theUrl;
		return this;
	}

	@Override
	public ExtensionDt setValue(IBaseDatatype theValue) {
		myValue = theValue;
		return this;
	}

	@Override
	@Deprecated //override deprecated method
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return new ArrayList<T>();
	}

	@Override
	public List<ExtensionDt> getExtension() {
		return getAllUndeclaredExtensions();
	}

	@Override
	public String toString() {
		ToStringBuilder retVal = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		retVal.append("url", getUrl());
		retVal.append("value", getValue());
		return retVal.build();
	}
	
	

}
