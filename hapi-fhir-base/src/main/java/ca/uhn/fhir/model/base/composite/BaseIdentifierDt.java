package ca.uhn.fhir.model.base.composite;

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

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.ParameterUtil;

public abstract class BaseIdentifierDt extends BaseIdentifiableElement implements ICompositeDatatype, IQueryParameterType {

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}

	/**
	 * Gets the value(s) for <b>system</b> (The namespace for the identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public abstract UriDt getSystem() ;

	/**
	 * Gets the value(s) for <b>value</b> (The value that is unique).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public abstract StringDt getValue();	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken() {
		if (getSystem().getValueAsString() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(getSystem().getValueAsString())) + '|' + ParameterUtil.escape(getValue().getValueAsString()); 
		} else {
			return ParameterUtil.escape(getValue().getValueAsString());
		}
	}	

	
	/**
	 * Returns true if <code>this</code> identifier has the same {@link IdentifierDt#[[#]]#getValue() value}
	 * and {@link IdentifierDt#[[#]]#getSystem() system} (as compared by simple equals comparison).
	 * Does not compare other values (e.g. {@link IdentifierDt#[[#]]#getUse() use}) or any extensions. 
	 */
	public boolean matchesSystemAndValue(BaseIdentifierDt theIdentifier) {
		if (theIdentifier == null) {
			return false;
		}
		return getValue().equals(theIdentifier.getValue()) && getSystem().equals(theIdentifier.getSystem());
	}

 

 	/**
	 * Sets the value for <b>system</b> (The namespace for the identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public abstract BaseIdentifierDt setSystem( String theUri);
	
	
	/**
	 * Sets the value for <b>value</b> (The value that is unique)
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public abstract BaseIdentifierDt setValue( String theString);
 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theQualifier, String theParameter) {
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter,'|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setValue(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setValue(ParameterUtil.unescape(theParameter));
		}
	}
	
}
