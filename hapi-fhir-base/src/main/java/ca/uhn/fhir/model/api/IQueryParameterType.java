package ca.uhn.fhir.model.api;

import java.io.Serializable;

import ca.uhn.fhir.context.FhirContext;

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

public interface IQueryParameterType extends Serializable {

	/**
	 * This method is generally only called by HAPI itself, and should not need to be called from user code. 
	 * 
	 * <p>
	 * See FHIR specification <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search
	 * SearchParameter Types</a> for information on the <b>token</b> format
	 * </p>
	 * @param theContext TODO
	 * @param theParamName TODO
	 * @param theQualifier
	 *            The parameter name qualifier that accompanied this value. For example, if the complete query was
	 *            <code>http://foo?name:exact=John</code>, qualifier would be ":exact"
	 * @param theValue
	 *            The actual parameter value. For example, if the complete query was
	 *            <code>http://foo?name:exact=John</code>, the value would be "John"
	 */
	public void setValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue);

	/**
	 * Returns a representation of this parameter's value as it will be represented "over the wire". In other
	 * words, how it will be presented in a URL (although not URL escaped) 
	 * 
	 * <p>
	 * See FHIR specification <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search
	 * SearchParameter Types</a> for information on the <b>token</b> format
	 * </p>
	 * @param theContext TODO
	 * 
	 * @return Returns a representation of this parameter's value as it will be represented "over the wire". In other
	 * words, how it will be presented in a URL (although not URL escaped) 
	 */
	public String getValueAsQueryToken(FhirContext theContext);
	
	/**
	 * This method will return any qualifier that should be appended to the parameter name (e.g ":exact").  Returns null if none are present.
	 */
	public String getQueryParameterQualifier();

	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale 
	 * instead of a normal value 
	 */
	Boolean getMissing();

	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale 
	 * instead of a normal value
	 *  
	 * @return Returns a reference to <code>this</code> for easier method chaining
	 */
	IQueryParameterType setMissing(Boolean theMissing);

}
