package org.hl7.fhir.instance.model.api;

import java.io.Serializable;

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

public interface IBaseEnumFactory<T extends Enum<?>> extends Serializable {

	/**
	 * Read an enumeration value from the string that represents it on the XML or JSON
	 *
	 * @param codeString the value found in the XML or JSON
	 * @return the enumeration value
	 * @throws IllegalArgumentException is the value is not known
	 */
	T fromCode(String codeString) throws IllegalArgumentException;

	/**
	 * Get the XML/JSON representation for an enumerated value
	 *
	 * @param code - the enumeration value
	 * @return the XML/JSON representation
	 */
	String toCode(T code);

	/**
	 * Get the system for a given enum value
	 */
	default String toSystem(T theValue) { return null; }

}
