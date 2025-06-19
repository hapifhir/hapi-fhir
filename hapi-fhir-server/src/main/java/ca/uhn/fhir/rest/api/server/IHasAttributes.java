/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

// Created by Claude Sonnet 4
public interface IHasAttributes {

	/**
	 * Gets an attribute from the servlet request. Attributes are used for interacting with servlet request
	 * attributes to communicate between servlet filters. These methods should not be used to pass information
	 * between interceptor methods. Use {@link RequestDetails#getUserData()} instead to pass information
	 * between interceptor methods.
	 *
	 * @param theAttributeName The attribute name
	 * @return The attribute value, or null if the attribute is not set
	 */
	Object getAttribute(String theAttributeName);

	/**
	 * Sets an attribute on the servlet request. Attributes are used for interacting with servlet request
	 * attributes to communicate between servlet filters. These methods should not be used to pass information
	 * between interceptor methods. Use {@link RequestDetails#getUserData()} instead to pass information
	 * between interceptor methods.
	 *
	 * @param theAttributeName The attribute name
	 * @param theAttributeValue The attribute value
	 */
	void setAttribute(String theAttributeName, Object theAttributeValue);
}
