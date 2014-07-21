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

public interface IQueryParameterType {

	/**
	 * This method is generally only called by HAPI itself, and should not need to be called from user code. 
	 * 
	 * <p>
	 * See FHIR specification <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search
	 * SearchParameter Types</a> for information on the <b>token</b> format
	 * </p>
	 * 
	 * @param theQualifier
	 *            The parameter name qualifier that accompanied this value. For example, if the complete query was
	 *            <code>http://foo?name:exact=John</code>, qualifier would be ":exact"
	 * @param theValue
	 *            The actual parameter value. For example, if the complete query was
	 *            <code>http://foo?name:exact=John</code>, the value would be "John"
	 */
	public void setValueAsQueryToken(String theQualifier, String theValue);

	/**
	 * This method is generally only called by HAPI itself, and should not need to be called from user code. 
	 * 
	 * <p>
	 * See FHIR specification <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search
	 * SearchParameter Types</a> for information on the <b>token</b> format
	 * </p>
	 */
	public String getValueAsQueryToken();
	
	/**
	 * This method is generally only called by HAPI itself, and should not need to be called from user code. 
	 *
	 * This method will return any qualifier that should be appended to the parameter name (e.g ":exact")
	 */
	public String getQueryParameterQualifier();

}
