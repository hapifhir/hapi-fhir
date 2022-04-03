package org.hl7.fhir.instance.model.api;

import java.io.Serializable;
import java.util.List;

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

/**
 * This interface is a simple marker for anything which is an HL7
 * structure of some kind. It is provided mostly to simplify convergence
 * between the HL7.org structures and the HAPI ones. 
 */
public interface IBase extends Serializable {

	boolean isEmpty();

	/**
	 * Returns <code>true</code> if any comments would be returned by {@link #getFormatCommentsPre()}
	 * or {@link #getFormatCommentsPost()}
	 * 
	 * @since 1.5
	 */
	boolean hasFormatComment();

	/**
	 * Returns a list of comments appearing immediately before this element within the serialized
	 * form of the resource. Creates the list if it does not exist, so this method will not return <code>null</code>
	 * 
	 * @since 1.5
	 */
	List<String> getFormatCommentsPre();

	/**
	 * Returns a list of comments appearing immediately after this element within the serialized
	 * form of the resource. Creates the list if it does not exist, so this method will not return <code>null</code>
	 * 
	 * @since 1.5
	 */
	List<String> getFormatCommentsPost();

	/**
	 * Returns the FHIR type name for the given element, e.g. "Patient" or "unsignedInt"
	 */
	default String fhirType() { return null; }

	/**
	 * Retrieves any user suplied data in this element
	 */
	Object getUserData(String theName);

	/**
	 * Sets a user supplied data value in this element
	 */
	void setUserData(String theName, Object theValue);

}
