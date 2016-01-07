package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.model.api.Bundle;

public interface IHistoryUntyped {

	/**
	 * Request that the method return a DSTU1 style bundle object. This method should only
	 * be called if you are accessing a DSTU1 server.
	 */
	IHistoryTyped<Bundle> andReturnDstu1Bundle();
	
	/**
	 * Request that the method return a Bundle resource (such as <code>ca.uhn.fhir.model.dstu2.resource.Bundle</code>).
	 * Use this method if you are accessing a DSTU2+ server.
	 */
	<T extends IBaseBundle> IHistoryTyped<T> andReturnBundle(Class<T> theType);
	
}
