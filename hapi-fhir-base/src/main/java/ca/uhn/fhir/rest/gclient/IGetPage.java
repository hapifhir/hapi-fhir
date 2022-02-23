package ca.uhn.fhir.rest.gclient;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hl7.fhir.instance.model.api.IBaseBundle;

public interface IGetPage {

	/**
	 * Load the next page of results using the link with relation "next" in the bundle. This
	 * method accepts a DSTU2 Bundle resource
	 * 
	 * @since 1.1
	 */
	<T extends IBaseBundle> IGetPageTyped<T> next(T theBundle);

	/**
	 * Load the previous page of results using the link with relation "prev" in the bundle. This
	 * method accepts a DSTU2+ Bundle resource
	 * 
	 * @since 1.1
	 */
	<T extends IBaseBundle> IGetPageTyped<T> previous(T theBundle);

	/**
	 * Load a page of results using the a given URL and return a DSTU1 Atom bundle
	 */
	IGetPageUntyped byUrl(String thePageUrl);

}
