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

import java.util.List;

public interface ICompositeElement extends IElement {

	/**
	 * Returns a list containing all child elements matching a given type
	 * 
	 * @param theType The type to match. If set to null, all child elements will be returned
	 * 
	 * @deprecated This method is not used by HAPI at this point, so there isn't much
	 * point to keeping it around. We are not deleting it just so that we don't break
	 * existing implementer code, but you do not need to supply an implementation
	 * of this code in your own structures. Deprecated in HAPI FHIR 2.3 (Jan 2017). 
	 * See See <a href="https://groups.google.com/forum/?utm_medium=email&utm_source=footer#!msg/hapi-fhir/AeV2hTDt--E/6EOgRA8YBwAJ"> for
	 * a discussion about this.
	 */
	@Deprecated
	<T extends IElement>
	List<T> getAllPopulatedChildElementsOfType(Class<T> theType);
	
}
