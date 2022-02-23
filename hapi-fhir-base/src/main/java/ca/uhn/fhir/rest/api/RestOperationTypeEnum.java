
package ca.uhn.fhir.rest.api;

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

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.util.CoverageIgnore;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

@CoverageIgnore
public enum RestOperationTypeEnum {

	BATCH("batch", true, false, false),

	ADD_TAGS("add-tags", false, false, true),

	DELETE_TAGS("delete-tags", false, false, true),

	GET_TAGS("get-tags", false, true, true),

	GET_PAGE("get-page", false, false, false),

	/**
	 * <b>
	 * Use this value with caution, this may
	 * change as the GraphQL interface matures
	 * </b>
	 */
	GRAPHQL_REQUEST("graphql-request", false, false, false),

	/**
	 * E.g. $everything, $validate, etc.
	 */
	EXTENDED_OPERATION_SERVER("extended-operation-server", false, false, false),

	/**
	 * E.g. $everything, $validate, etc.
	 */
	EXTENDED_OPERATION_TYPE("extended-operation-type", false, false, false),

	/**
	 * E.g. $everything, $validate, etc.
	 */
	EXTENDED_OPERATION_INSTANCE("extended-operation-instance", false, false, false),

	/**
	 * Code Value: <b>create</b>
	 */
	CREATE("create", false, true, false),

	/**
	 * Code Value: <b>delete</b>
	 */
	DELETE("delete", false, false, true),

	/**
	 * Code Value: <b>history-instance</b>
	 */
	HISTORY_INSTANCE("history-instance", false, false, true),

	/**
	 * Code Value: <b>history-system</b>
	 */
	HISTORY_SYSTEM("history-system", true, false, false),

	/**
	 * Code Value: <b>history-type</b>
	 */
	HISTORY_TYPE("history-type", false, true, false),

	/**
	 * Code Value: <b>read</b>
	 */
	READ("read", false, false, true),

	/**
	 * Code Value: <b>search-system</b>
	 */
	SEARCH_SYSTEM("search-system", true, false, false),

	/**
	 * Code Value: <b>search-type</b>
	 */
	SEARCH_TYPE("search-type", false, true, false),

	/**
	 * Code Value: <b>transaction</b>
	 */
	TRANSACTION("transaction", true, false, false),

	/**
	 * Code Value: <b>update</b>
	 */
	UPDATE("update", false, false, true),

	/**
	 * Code Value: <b>validate</b>
	 */
	VALIDATE("validate", false, true, true),

	/**
	 * Code Value: <b>vread</b>
	 */
	VREAD("vread", false, false, true),

	/**
	 * Load the server's metadata
	 */
	METADATA("metadata", false, false, false),
	
	/**
	 * $meta-add extended operation
	 */
	META_ADD("$meta-add", false, false, false),

	/**
	 * $meta-add extended operation
	 */
	META("$meta", false, false, false),

	/**
	 * $meta-delete extended operation
	 */
	META_DELETE("$meta-delete", false, false, false),
	
	/**
	 * Patch operation
	 */
	PATCH("patch", false, false, true),

	;

	private static final Map<String, RestOperationTypeEnum> CODE_TO_ENUM = new HashMap<String, RestOperationTypeEnum>();

	/**
	 * Identifier for this Value Set: http://hl7.org/fhir/vs/type-restful-operation
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/type-restful-operation";

	/**
	 * Name for this Value Set: RestfulOperationType
	 */
	public static final String VALUESET_NAME = "RestfulOperationType";

	static {
		for (RestOperationTypeEnum next : RestOperationTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}

	private final String myCode;
	private final boolean mySystemLevel;
	private final boolean myTypeLevel;
	private final boolean myInstanceLevel;

	/**
	 * Constructor
	 */
	RestOperationTypeEnum(@Nonnull String theCode, boolean theSystemLevel, boolean theTypeLevel, boolean theInstanceLevel) {
		myCode = theCode;
		mySystemLevel = theSystemLevel;
		myTypeLevel = theTypeLevel;
		myInstanceLevel = theInstanceLevel;
	}

	/**
	 * Returns the enumerated value associated with this code
	 */
	public RestOperationTypeEnum forCode(@Nonnull String theCode) {
		Validate.notNull(theCode, "theCode must not be null");
		return CODE_TO_ENUM.get(theCode);
	}

	/**
	 * Returns the code associated with this enumerated value
	 */
	@Nonnull
	public String getCode() {
		return myCode;
	}

	public boolean isSystemLevel() {
		return mySystemLevel;
	}

	public boolean isTypeLevel() {
		return myTypeLevel;
	}

	public boolean isInstanceLevel() {
		return myInstanceLevel;
	}
}
