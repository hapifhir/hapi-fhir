package ca.uhn.fhir.rest.api;

import java.util.HashMap;

import org.apache.commons.lang3.Validate;

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
 * Validation mode parameter for the $validate operation (DSTU2+ only) 
 */
public enum ValidationModeEnum {
	/**
	 * The server checks the content, and then checks that the content would be acceptable as a create (e.g. that the content would not validate any uniqueness constraints)
	 */
	CREATE("create"),
	
	/**
	 * The server checks the content, and then checks that it would accept it as an update against the nominated specific resource (e.g. that there are no changes to immutable fields the server does not allow to change, and checking version integrity if appropriate)
	 */
	UPDATE("update"),

	/**
	 * The server ignores the content, and checks that the nominated resource is allowed to be deleted (e.g. checking referential integrity rules)
	 */
	DELETE("delete");

	private static HashMap<String, ValidationModeEnum> myCodeToValue;
	private String myCode;

	static {
		myCodeToValue = new HashMap<String, ValidationModeEnum>();
		for (ValidationModeEnum next : values()) {
			myCodeToValue.put(next.getCode(), next);
		}
	}
	
	public static ValidationModeEnum forCode(String theCode) {
		Validate.notBlank(theCode, "theCode must not be blank");
		return myCodeToValue.get(theCode);
	}
	
	public String getCode() {
		return myCode;
	}

	private ValidationModeEnum(String theCode) {
		myCode = theCode;
	}
	
//	@Override
//	public boolean isEmpty() {
//		return false;
//	}
}
