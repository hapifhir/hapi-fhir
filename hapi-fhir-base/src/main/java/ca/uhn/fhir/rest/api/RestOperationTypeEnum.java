
package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

@CoverageIgnore
public enum RestOperationTypeEnum {

	ADD_TAGS("add-tags"),

	DELETE_TAGS("delete-tags"),

	GET_TAGS("get-tags"),

	GET_PAGE("get-page"),

	/**
	 * <b>
	 * Use this value with caution, this may
	 * change as the GraphQL interface matures
	 * </b>
	 */
	GRAPHQL_REQUEST("graphql-request"),

	/**
	 * E.g. $everything, $validate, etc.
	 */
	EXTENDED_OPERATION_SERVER("extended-operation-server"),

	/**
	 * E.g. $everything, $validate, etc.
	 */
	EXTENDED_OPERATION_TYPE("extended-operation-type"),

	/**
	 * E.g. $everything, $validate, etc.
	 */
	EXTENDED_OPERATION_INSTANCE("extended-operation-instance"),

	/**
	 * Code Value: <b>create</b>
	 */
	CREATE("create"),

	/**
	 * Code Value: <b>delete</b>
	 */
	DELETE("delete"),

	/**
	 * Code Value: <b>history-instance</b>
	 */
	HISTORY_INSTANCE("history-instance"),

	/**
	 * Code Value: <b>history-system</b>
	 */
	HISTORY_SYSTEM("history-system"),

	/**
	 * Code Value: <b>history-type</b>
	 */
	HISTORY_TYPE("history-type"),

	/**
	 * Code Value: <b>read</b>
	 */
	READ("read"),

	/**
	 * Code Value: <b>search-system</b>
	 */
	SEARCH_SYSTEM("search-system"),

	/**
	 * Code Value: <b>search-type</b>
	 */
	SEARCH_TYPE("search-type"),

	/**
	 * Code Value: <b>transaction</b>
	 */
	TRANSACTION("transaction"),

	/**
	 * Code Value: <b>update</b>
	 */
	UPDATE("update"),

	/**
	 * Code Value: <b>validate</b>
	 */
	VALIDATE("validate"),

	/**
	 * Code Value: <b>vread</b>
	 */
	VREAD("vread"),

	/**
	 * Load the server's metadata
	 */
	METADATA("metadata"), 
	
	/**
	 * $meta-add extended operation
	 */
	META_ADD("$meta-add"),

	/**
	 * $meta-add extended operation
	 */
	META("$meta"),

	/**
	 * $meta-delete extended operation
	 */
	META_DELETE("$meta-delete"), 
	
	/**
	 * Patch operation
	 */
	PATCH("patch"),

	;

	private static Map<String, RestOperationTypeEnum> CODE_TO_ENUM = new HashMap<String, RestOperationTypeEnum>();

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

	/**
	 * Constructor
	 */
	RestOperationTypeEnum(String theCode) {
		myCode = theCode;
	}

	/**
	 * Returns the enumerated value associated with this code
	 */
	public RestOperationTypeEnum forCode(String theCode) {
		RestOperationTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}

}
