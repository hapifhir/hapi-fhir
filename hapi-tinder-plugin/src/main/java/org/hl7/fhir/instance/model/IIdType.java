package org.hl7.fhir.dstu2.model;


/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


public interface IIdType extends IBase {

	@Override
	boolean isEmpty();

	/**
	 * Returns true if this ID represents a local reference (i.e. a reference beginning with the character '#')
	 */
	boolean isLocal();

	/**
	 * Returns the value of this ID. Note that this value may be a fully qualified URL, a relative/partial URL, or a simple ID. Use {@link #getIdPart()} to get just the ID portion.
	 * 
	 * @see #getIdPart()
	 */
	String getValue();

	/**
	 * Returns only the logical ID part of this ID. For example, given the ID
	 * "http://example,.com/fhir/Patient/123/_history/456", this method would
	 * return "123".
	 */
	String getIdPart();

	boolean hasIdPart();

	String getBaseUrl();

	/**
	 * Returns a copy of this ID without the base URL or the version 
	 */
	IIdType toUnqualifiedVersionless();

	/**
	 * Returns a copy of this ID without the version 
	 */
	IIdType toVersionless();

	IIdType setValue(String theString);

	boolean hasVersionIdPart();

	String getVersionIdPart();

	IIdType toUnqualified();

	boolean hasResourceType();

	IIdType withResourceType(String theResName);

	String getResourceType();

	IIdType withServerBase(String theServerBase, String theResourceName);

	boolean isAbsolute();

	boolean isIdPartValidLong();

	Long getIdPartAsLong();

}
