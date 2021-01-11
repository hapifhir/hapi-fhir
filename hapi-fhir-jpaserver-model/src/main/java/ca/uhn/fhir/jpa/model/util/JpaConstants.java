package ca.uhn.fhir.jpa.model.util;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
	 * Userdata key for tracking the fact that a resource ID was assigned by the server
	 */
	public static final String RESOURCE_ID_SERVER_ASSIGNED = JpaConstants.class.getName() + "_RESOURCE_ID_SERVER_ASSIGNED";
	/**
	 * Operation name for the $apply-codesystem-delta-add operation
	 */
	public static final String OPERATION_APPLY_CODESYSTEM_DELTA_ADD = "$apply-codesystem-delta-add";
	/**
	 * Operation name for the $apply-codesystem-delta-remove operation
	 */
	public static final String OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE = "$apply-codesystem-delta-remove";
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
	/**
	 * Operation name for the "$subsumes" operation
	 */
	public static final String OPERATION_SUBSUMES = "$subsumes";
	/**
	 * Operation name for the "$snapshot" operation
	 */
	public static final String OPERATION_SNAPSHOT = "$snapshot";
	/**
	 * Operation name for the "$binary-access" operation
	 */
	public static final String OPERATION_BINARY_ACCESS_READ = "$binary-access-read";
	/**
	 * Operation name for the "$binary-access" operation
	 */
	public static final String OPERATION_BINARY_ACCESS_WRITE = "$binary-access-write";
	/**
	 * Operation name for the "$upload-external-code-system" operation
	 */
	public static final String OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM = "$upload-external-code-system";
	/**
	 * Operation name for the "$export" operation
	 */
	public static final String OPERATION_EXPORT = "$export";
	/**
	 * Operation name for the "$export-poll-status" operation
	 */
	public static final String OPERATION_EXPORT_POLL_STATUS = "$export-poll-status";
	/**
	 * Operation name for the "$lastn" operation
	 */
	public static final String OPERATION_LASTN = "$lastn";

	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_POLL_STATUS_JOB_ID = "_jobId";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_OUTPUT_FORMAT = "_outputFormat";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_TYPE = "_type";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_SINCE = "_since";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_TYPE_FILTER = "_typeFilter";

	/**
	 * Parameter for delete to indicate the deleted resources should also be expunged
	 */

	public static final String PARAM_DELETE_EXPUNGE = "_expunge";

	/**
	 * URL for extension on a SearchParameter indicating that text values should not be indexed
	 */
	public static final String EXTENSION_EXT_SYSTEMDEFINED = JpaConstants.class.getName() + "_EXTENSION_EXT_SYSTEMDEFINED";

	/**
	 * URL for extension on a Phonetic String SearchParameter indicating that text values should be phonetically indexed with the named encoder
	 */
	public static final String EXT_SEARCHPARAM_PHONETIC_ENCODER = "http://hapifhir.io/fhir/StructureDefinition/searchparameter-phonetic-encoder";
	public static final String VALUESET_FILTER_DISPLAY = "display";

	/**
	 * The name of the default partition
	 */
	public static final String DEFAULT_PARTITION_NAME = "DEFAULT";

	/**
	 * Non-instantiable
	 */
	private JpaConstants() {
		// nothing
	}
}
