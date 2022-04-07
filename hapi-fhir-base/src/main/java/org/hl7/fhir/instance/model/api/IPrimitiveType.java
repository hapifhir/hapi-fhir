package org.hl7.fhir.instance.model.api;


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


import javax.annotation.Nullable;

public interface IPrimitiveType<T> extends IBaseDatatype {

	String getValueAsString();

	void setValueAsString(String theValue) throws IllegalArgumentException;

	T getValue();

	IPrimitiveType<T> setValue(T theValue) throws IllegalArgumentException;

	boolean hasValue();

	/**
	 * If the supplied argument is non-null, returns the results of {@link #getValue()}. If the supplied argument is null, returns null.
	 */
	@Nullable
	static <T> T toValueOrNull(@Nullable IPrimitiveType<T> thePrimitiveType) {
		return thePrimitiveType != null ? thePrimitiveType.getValue() : null;
	}

}
