package ca.uhn.fhir.util;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.length;

public class ValidateUtil {

	/**
	 * Throws {@link IllegalArgumentException} if theValue is <= theMinimum
	 */
	public static void isGreaterThan(long theValue, long theMinimum, String theMessage) {
		if (theValue <= theMinimum) {
			throw new IllegalArgumentException(Msg.code(1762) + theMessage);
		}
	}

	/**
	 * Throws {@link IllegalArgumentException} if theValue is < theMinimum
	 */
	public static void isGreaterThanOrEqualTo(long theValue, long theMinimum, String theMessage) {
		if (theValue < theMinimum) {
			throw new IllegalArgumentException(Msg.code(1763) + theMessage);
		}
	}

	public static void isNotBlankOrThrowIllegalArgument(String theString, String theMessage) {
		if (isBlank(theString)) {
			throw new IllegalArgumentException(Msg.code(1764) + theMessage);
		}
	}

	public static void isNotBlankOrThrowInvalidRequest(String theString, String theMessage) {
		if (isBlank(theString)) {
			throw new InvalidRequestException(Msg.code(1765) + theMessage);
		}
	}

	public static void isNotBlankOrThrowUnprocessableEntity(String theString, String theMessage) {
		if (isBlank(theString)) {
			throw new UnprocessableEntityException(Msg.code(1766) + theMessage);
		}
	}

	public static void isNotNullOrThrowUnprocessableEntity(Object theObject, String theMessage, Object... theValues) {
		if (theObject == null) {
			throw new UnprocessableEntityException(Msg.code(1767) + String.format(theMessage, theValues));
		}
	}

	public static void isNotTooLongOrThrowIllegalArgument(String theString, int theMaxLength, String theMessage) {
		if (length(theString) > theMaxLength) {
			throw new IllegalArgumentException(Msg.code(1768) + theMessage);
		}
	}

	public static void isTrueOrThrowInvalidRequest(boolean theSuccess, String theMessage, Object... theValues) {
		if (!theSuccess) {
			throw new InvalidRequestException(Msg.code(1769) + String.format(theMessage, theValues));
		}
	}

	public static void exactlyOneNotNullOrThrowInvalidRequestException(Object[] theObjects, String theMessage) {
		int count = 0;
		for (Object next : theObjects) {
			if (next != null) {
				count++;
			}
		}
		if (count != 1) {
			throw new InvalidRequestException(Msg.code(1770) + theMessage);
		}
	}

}
