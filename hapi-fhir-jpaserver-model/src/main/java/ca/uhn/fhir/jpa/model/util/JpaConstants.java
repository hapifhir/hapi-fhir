package ca.uhn.fhir.jpa.model.util;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.HapiExtensions;

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
	 *
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE}
	 */
	@Deprecated
	public static final String OPERATION_EXPUNGE = ProviderConstants.OPERATION_EXPUNGE;
	/**
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE}
	 */
	@Deprecated
	public static final String OPERATION_NAME_EXPUNGE = ProviderConstants.OPERATION_EXPUNGE;
	/**
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT}
	 */
	@Deprecated
	public static final String OPERATION_EXPUNGE_PARAM_LIMIT = ProviderConstants.OPERATION_EXPUNGE_PARAM_LIMIT;
	/**
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT}
	 */
	@Deprecated
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES = ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES;
	/**
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT}
	 */
	@Deprecated
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS = ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS;
	/**
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT}
	 */
	@Deprecated
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_EVERYTHING = ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_EVERYTHING;
	/**
	 * @deprecated Replace with {@link ProviderConstants#OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT}
	 */
	@Deprecated
	public static final String OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT = ProviderConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT;
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
	 * Operation name for the "$import" operation
	 */
	public static final String OPERATION_IMPORT = "$import";
	/**
	 * Operation name for the "$import-poll-status" operation
	 */
	public static final String OPERATION_IMPORT_POLL_STATUS = "$import-poll-status";
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
	 * Parameter for the $import operation
	 */
	public static final String PARAM_IMPORT_POLL_STATUS_JOB_ID = "_jobId";
	/**
	 * Parameter for the $import operation
	 */
	public static final String PARAM_IMPORT_JOB_DESCRIPTION = "_jobDescription";
	/**
	 * Parameter for the $import operation
	 */
	public static final String PARAM_IMPORT_PROCESSING_MODE = "_processingMode";
	/**
	 * Parameter for the $import operation
	 */
	public static final String PARAM_IMPORT_FILE_COUNT = "_fileCount";
	/**
	 * Parameter for the $import operation
	 */
	public static final String PARAM_IMPORT_BATCH_SIZE = "_batchSize";

	/**
	 * The [id] of the group when $export is called on /Group/[id]/$export
	 */
	public static final String PARAM_EXPORT_GROUP_ID = "_groupId";

	/**
	 * Whether mdm should be performed on group export items to expand the group items to linked items before performing the export
	 */
	public static final String PARAM_EXPORT_MDM = "_mdm";

	/**
	 * Parameter for delete to indicate the deleted resources should also be expunged
	 */

	public static final String PARAM_DELETE_EXPUNGE = "_expunge";

	/**
	 * URL for extension on a SearchParameter indicating that text values should not be indexed
	 */
	public static final String EXTENSION_EXT_SYSTEMDEFINED = JpaConstants.class.getName() + "_EXTENSION_EXT_SYSTEMDEFINED";

	/**
	 * Deprecated.  Please use {@link HapiExtensions#EXT_SEARCHPARAM_PHONETIC_ENCODER} instead.
	 */
	@Deprecated
	public static final String EXT_SEARCHPARAM_PHONETIC_ENCODER = HapiExtensions.EXT_SEARCHPARAM_PHONETIC_ENCODER;

	public static final String VALUESET_FILTER_DISPLAY = "display";

	/**
	 * The name of the default partition
	 */
	public static final String DEFAULT_PARTITION_NAME = "DEFAULT";

	/**
	 * The name of the collection of all partitions
	 */
	public static final String ALL_PARTITIONS_NAME = "ALL_PARTITIONS";

	/**
	 * Parameter for the $expand operation
	 */
	public static final String OPERATION_EXPAND_PARAM_INCLUDE_HIERARCHY = "includeHierarchy";
	public static final String HEADER_UPSERT_EXISTENCE_CHECK = "X-Upsert-Extistence-Check";
	public static final String HEADER_UPSERT_EXISTENCE_CHECK_DISABLED = "disabled";

	/**
	 * Non-instantiable
	 */
	private JpaConstants() {
		// nothing
	}
}
