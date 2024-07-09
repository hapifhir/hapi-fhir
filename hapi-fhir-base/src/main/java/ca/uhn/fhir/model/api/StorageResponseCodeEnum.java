/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.model.api;

/**
 * This enum contains the allowable codes in the HAPI FHIR defined
 * codesystem: https://hapifhir.io/fhir/CodeSystem/hapi-fhir-storage-response-code
 *
 * This is used in CRUD response OperationOutcome resources.
 */
public enum StorageResponseCodeEnum implements ICodingEnum {
	SUCCESSFUL_CREATE("Create succeeded."),
	SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH(
			"Conditional create succeeded: no existing resource matched the conditional URL."),
	SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH(
			"Conditional create succeeded: an existing resource matched the conditional URL so no action was taken."),
	SUCCESSFUL_UPDATE("Update succeeded."),
	SUCCESSFUL_UPDATE_AS_CREATE("Update as create succeeded."),
	SUCCESSFUL_UPDATE_NO_CHANGE("Update succeeded: No changes were detected so no action was taken."),
	SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH(
			"Conditional update succeeded: no existing resource matched the conditional URL so a new resource was created."),
	SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH(
			"Conditional update succeeded: an existing resource matched the conditional URL and was updated."),
	SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE(
			"Conditional update succeeded: an existing resource matched the conditional URL and was updated, but no changes were detected so no action was taken."),
	SUCCESSFUL_DELETE("Delete succeeded."),
	SUCCESSFUL_DELETE_ALREADY_DELETED("Delete succeeded: Resource was already deleted so no action was taken."),
	SUCCESSFUL_DELETE_NOT_FOUND("Delete succeeded: No existing resource was found so no action was taken."),

	SUCCESSFUL_PATCH("Patch succeeded."),

	SUCCESSFUL_PATCH_NO_CHANGE("Patch succeeded: No changes were detected so no action was taken."),
	SUCCESSFUL_CONDITIONAL_PATCH("Conditional patch succeeded."),
	SUCCESSFUL_CONDITIONAL_PATCH_NO_CHANGE(
			"Conditional patch succeeded: No changes were detected so no action was taken.");

	public static final String SYSTEM = "https://hapifhir.io/fhir/CodeSystem/hapi-fhir-storage-response-code";

	private final String myDisplay;

	StorageResponseCodeEnum(String theDisplay) {
		myDisplay = theDisplay;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getSystem() {
		return SYSTEM;
	}

	@Override
	public String getDisplay() {
		return myDisplay;
	}
}
