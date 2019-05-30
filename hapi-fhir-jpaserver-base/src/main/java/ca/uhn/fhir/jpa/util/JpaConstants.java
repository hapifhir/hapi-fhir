package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.rest.api.Constants;

public class JpaConstants {
	/**
	 * Operation name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE = "$expunge";

	/**
	 * Operation name for the $match operation
	 */
	public static final String OPERATION_MATCH = "$match";

	/**
	 * @deprecated Replace with {@link #OPERATION_EXPUNGE}
	 */
	@Deprecated
	public static final String OPERATION_NAME_EXPUNGE = OPERATION_EXPUNGE;

	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_LIMIT = "limit";
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES = "expungeDeletedResources";
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS = "expungePreviousVersions";
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_EVERYTHING = "expungeEverything";
	/**
	 * Output parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT = "count";
	/**
	 * Header name for the "X-Meta-Snapshot-Mode" header, which
	 * specifies that properties in meta (tags, profiles, security labels)
	 * should be treated as a snapshot, meaning that these things will
	 * be removed if they are nt explicitly included in updates
	 */
	public static final String HEADER_META_SNAPSHOT_MODE = "X-Meta-Snapshot-Mode";

	/**
	 * Operation name for the $lookup operation
	 */
	public static final String OPERATION_LOOKUP = "$lookup";

	/**
	 * Operation name for the $expand operation
	 */
	public static final String OPERATION_EXPAND = "$expand";

	/**
	 * Operation name for the $validate-code operation
	 */
	public static final String OPERATION_VALIDATE_CODE = "$validate-code";

	/**
	 * Operation name for the $get-resource-counts operation
	 */
	public static final String OPERATION_GET_RESOURCE_COUNTS = "$get-resource-counts";

	/**
	 * Operation name for the $meta operation
	 */
	public static final String OPERATION_META = "$meta";

	/**
	 * Operation name for the $validate operation
	 */
	// NB don't delete this, it's used in Smile as well, even though hapi-fhir-server uses the version from Constants.java
	public static final String OPERATION_VALIDATE = Constants.EXTOP_VALIDATE;

	/**
	 * Operation name for the $suggest-keywords operation
	 */
	public static final String OPERATION_SUGGEST_KEYWORDS = "$suggest-keywords";

	/**
	 * Operation name for the $everything operation
	 */
	public static final String OPERATION_EVERYTHING = "$everything";

	/**
	 * Operation name for the $process-message operation
	 */
	public static final String OPERATION_PROCESS_MESSAGE = "$process-message";

	/**
	 * Operation name for the $meta-delete operation
	 */
	public static final String OPERATION_META_DELETE = "$meta-delete";

	/**
	 * Operation name for the $meta-add operation
	 */
	public static final String OPERATION_META_ADD = "$meta-add";

	/**
	 * Operation name for the $translate operation
	 */
	public static final String OPERATION_TRANSLATE = "$translate";

	/**
	 * Operation name for the $document operation
	 */
	public static final String OPERATION_DOCUMENT = "$document";

	/**
	 * Trigger a subscription manually for a given resource
	 */
	public static final String OPERATION_TRIGGER_SUBSCRIPTION = "$trigger-subscription";
}
