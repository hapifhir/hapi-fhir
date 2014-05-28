package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR Library
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

import ca.uhn.fhir.model.primitive.IdDt;

public interface IIdentifiableElement extends IElement {

	void setId(IdDt theId);

	IdDt getId();

	/**
	 * Convenience method for {@link #setId(IdDt)} which creates a new IdDt and provides the
	 * given string as the ID.
	 *   
	 * @param theId The ID string. Can be a complete URL, a partial URL or even a simple identifier.
	 */
	void setId(String theId);

}
