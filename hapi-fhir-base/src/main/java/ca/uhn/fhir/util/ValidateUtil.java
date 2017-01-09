package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isBlank;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ValidateUtil {

	public static void isTrueOrThrowInvalidRequest(boolean theSuccess, String theMessage) {
		if (theSuccess == false) {
			throw new InvalidRequestException(theMessage);
		}
	}

	public static void isNotBlankOrThrowInvalidRequest(String theString, String theMessage) {
		if (isBlank(theString)) {
			throw new InvalidRequestException(theMessage);
		}
	}

	/**
	 * Throws {@link IllegalArgumentException} if theValue is <= theMinimum
	 */
	public static void isGreaterThan(long theValue, long theMinimum, String theMessage) {
		if (theValue <= theMinimum) {
			throw new IllegalArgumentException(theMessage);
		}
	}

	/**
	 * Throws {@link IllegalArgumentException} if theValue is <= theMinimum
	 */
	public static void isGreaterThanOrEqualTo(long theValue, long theMinimum, String theMessage) {
		if (theValue < theMinimum) {
			throw new IllegalArgumentException(theMessage);
		}
	}

}
