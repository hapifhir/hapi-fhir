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

import ca.uhn.fhir.model.primitive.IdDt;

public interface IIdentifiableElement extends IElement {

	/**
	 * Used to retrieve an ID for this specific element within a resource. These are used for IDREF referenced between
	 * elements within a single resource, and do not have any other purpose.
	 */
	String getElementSpecificId();

	/**
	 * @deprecated Use {@link #getElementSpecificId()} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@Deprecated
	IdDt getId();

	/**
	 * Used to set an ID for this specific element within a resource. These are used for IDREF referenced between
	 * elements within a single resource, and do not have any other purpose.
	 */
	void setElementSpecificId(String theElementSpecificId);

	/**
	 * @deprecated Use {@link #setElementSpecificId(String)} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@Deprecated
	void setId(IdDt theId);

	/**
	 * @deprecated Use {@link #setElementSpecificId(String)} instead. This method will be removed because it is easily
	 *             confused with other ID methods (such as patient#getIdentifier)
	 */
	@Deprecated
	void setId(String theId);

}
