/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.provider;

public class ProviderConstants {
	public static final String SUBSCRIPTION_TRIGGERING_PARAM_RESOURCE_ID = "resourceId";
	public static final String SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL = "searchUrl";

	/**
	 * Operation name: add partition
	 */
	public static final String PARTITION_MANAGEMENT_CREATE_PARTITION = "$partition-management-create-partition";

	/**
	 * Operation name: update partition
	 */
	public static final String PARTITION_MANAGEMENT_UPDATE_PARTITION = "$partition-management-update-partition";

	/**
	 * Operation name: update partition
	 */
	public static final String PARTITION_MANAGEMENT_DELETE_PARTITION = "$partition-management-delete-partition";

	/**
	 * Operation name: read partition
	 */
	public static final String PARTITION_MANAGEMENT_READ_PARTITION = "$partition-management-read-partition";

	/**
	 * Operation name: list partitions
	 */
	public static final String PARTITION_MANAGEMENT_LIST_PARTITIONS = "$partition-management-list-partitions";

	public static final String PARTITION_MANAGEMENT_PARTITION_ID = "id";
	public static final String PARTITION_MANAGEMENT_PARTITION_NAME = "name";
	public static final String PARTITION_MANAGEMENT_PARTITION_DESC = "description";

	public static final String DEFAULT_PARTITION_NAME = "DEFAULT";
	public static final String ALL_PARTITIONS_TENANT_NAME = "_ALL";

	/**
	 * Operation name: diff
	 */
	public static final String DIFF_OPERATION_NAME = "$diff";

	public static final String DIFF_FROM_VERSION_PARAMETER = "fromVersion";

	public static final String DIFF_FROM_PARAMETER = "from";
	public static final String DIFF_TO_PARAMETER = "to";
	public static final String DIFF_INCLUDE_META_PARAMETER = "includeMeta";

	/**
	 * MDM Operations
	 */
	public static final String PATIENT_MATCH = "$match";

	public static final String MDM_MATCH = "$mdm-match";
	public static final String MDM_MATCH_RESOURCE = "resource";
	public static final String MDM_RESOURCE_TYPE = "resourceType";
	public static final String MDM_MERGE_GOLDEN_RESOURCES = "$mdm-merge-golden-resources";
	public static final String MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID = "fromGoldenResourceId";
	public static final String MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID = "toGoldenResourceId";
	public static final String MDM_MERGE_RESOURCE = "resource";

	public static final String MDM_UPDATE_LINK = "$mdm-update-link";
	public static final String MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID = "goldenResourceId";
	public static final String MDM_UPDATE_LINK_RESOURCE_ID = "resourceId";
	public static final String MDM_UPDATE_LINK_MATCH_RESULT = "matchResult";

	public static final String MDM_CREATE_LINK = "$mdm-create-link";
	public static final String MDM_CREATE_LINK_GOLDEN_RESOURCE_ID = "goldenResourceId";
	public static final String MDM_CREATE_LINK_RESOURCE_ID = "resourceId";
	public static final String MDM_CREATE_LINK_MATCH_RESULT = "matchResult";

	public static final String MDM_QUERY_LINKS = "$mdm-query-links";
	public static final String MDM_LINK_HISTORY = "$mdm-link-history";
	public static final String MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID = "goldenResourceId";
	public static final String MDM_QUERY_LINKS_RESOURCE_ID = "resourceId";
	public static final String MDM_QUERY_PARTITION_IDS = "partitionIds";
	public static final String MDM_QUERY_LINKS_MATCH_RESULT = "matchResult";
	public static final String MDM_QUERY_LINKS_LINK_SOURCE = "linkSource";

	public static final String MDM_DUPLICATE_GOLDEN_RESOURCES = "$mdm-duplicate-golden-resources";
	public static final String MDM_NOT_DUPLICATE = "$mdm-not-duplicate";

	public static final String OPERATION_MDM_CLEAR = "$mdm-clear";
	public static final String OPERATION_MDM_CLEAR_RESOURCE_NAME = "resourceType";
	public static final String OPERATION_MDM_BATCH_SIZE = "batchSize";
	public static final String OPERATION_MDM_SUBMIT = "$mdm-submit";
	public static final String OPERATION_MDM_SUBMIT_OUT_PARAM_SUBMITTED = "submitted";
	public static final String MDM_BATCH_RUN_CRITERIA = "criteria";
	public static final String MDM_BATCH_RUN_RESOURCE_TYPE = "resourceType";

	/**
	 * Clinical Reasoning Operations
	 */
	public static final String CR_OPERATION_EVALUATE_MEASURE = "$evaluate-measure";

	public static final String CR_OPERATION_CARE_GAPS = "$care-gaps";
	public static final String CR_OPERATION_SUBMIT_DATA = "$submit-data";
	public static final String CR_OPERATION_EVALUATE = "$evaluate";
	public static final String CR_OPERATION_CQL = "$cql";
	public static final String CR_OPERATION_APPLY = "$apply";
	public static final String CR_OPERATION_R5_APPLY = "$r5.apply";
	public static final String CR_OPERATION_PREPOPULATE = "$prepopulate";
	public static final String CR_OPERATION_POPULATE = "$populate";
	public static final String CR_OPERATION_EXTRACT = "$extract";
	public static final String CR_OPERATION_PACKAGE = "$package";
	public static final String CR_OPERATION_QUESTIONNAIRE = "$questionnaire";
	public static final String CR_OPERATION_COLLECTDATA = "$collect-data";
	public static final String CR_OPERATION_DATAREQUIREMENTS = "$data-requirements";
	/**
	 * Operation name for the $meta operation
	 */
	public static final String OPERATION_META = "$meta";

	/**
	 * Operation name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE = "$expunge";

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
	 * Operation name for the $delete-expunge operation
	 */
	public static final String OPERATION_DELETE_EXPUNGE = "$delete-expunge";

	/**
	 * url of resources to delete for the $delete-expunge operation
	 */
	public static final String OPERATION_DELETE_EXPUNGE_URL = "url";

	/**
	 * Number of resources to delete at a time for the $delete-expunge operation
	 */
	public static final String OPERATION_DELETE_BATCH_SIZE = "batchSize";
	/**
	 * Should we cascade the $delete-expunge operation
	 */
	public static final String OPERATION_DELETE_CASCADE = "cascade";
	/**
	 * How many rounds for the $delete-expunge operation
	 */
	public static final String OPERATION_DELETE_CASCADE_MAX_ROUNDS = "cascadeMaxRounds";

	/**
	 * The Spring Batch job id of the delete expunge job created by a $delete-expunge operation
	 */
	public static final String OPERATION_BATCH_RESPONSE_JOB_ID = "jobId";

	/**
	 * Operation name for the $reindex operation
	 */
	public static final String OPERATION_REINDEX = "$reindex";

	/**
	 * Operation name for the $reindex operation
	 */
	public static final String OPERATION_REINDEX_DRYRUN = "$reindex-dryrun";

	/**
	 * Operation name for the $invalidate-expansion operation
	 */
	public static final String OPERATION_INVALIDATE_EXPANSION = "$invalidate-expansion";

	/**
	 * url of resources to delete for the $delete-expunge operation
	 */
	public static final String OPERATION_REINDEX_PARAM_URL = "url";

	/**
	 * Number of resources to delete at a time for the $delete-expunge operation
	 */
	public static final String OPERATION_REINDEX_PARAM_BATCH_SIZE = "batchSize";

	/**
	 * Whether all resource types should be reindexed
	 */
	@Deprecated(since = "7.3.4")
	public static final String OPERATION_REINDEX_PARAM_EVERYTHING = "everything";

	/**
	 * The Spring Batch job id of the delete expunge job created by a $delete-expunge operation
	 */
	public static final String OPERATION_REINDEX_RESPONSE_JOB_ID = "jobId";

	/**
	 * Operation name for the $reindex-terminology operation
	 */
	public static final String OPERATION_REINDEX_TERMINOLOGY = "$reindex-terminology";

	@Deprecated
	public static final String MARK_ALL_RESOURCES_FOR_REINDEXING = "$mark-all-resources-for-reindexing";
	/**
	 * @see ProviderConstants#OPERATION_REINDEX
	 * @deprecated
	 */
	@Deprecated
	public static final String PERFORM_REINDEXING_PASS = "$perform-reindexing-pass";

	/**
	 * Operation name for the "$export-poll-status" operation
	 */
	public static final String OPERATION_EXPORT_POLL_STATUS = "$export-poll-status";

	/**
	 * Operation name for the "$export" operation
	 */
	public static final String OPERATION_EXPORT = "$export";

	/**
	 * Operation name for the "$hapi.fhir.replace-references" operation
	 */
	public static final String OPERATION_REPLACE_REFERENCES = "$hapi.fhir.replace-references";

	/**
	 * Parameter for source reference of the "$hapi.fhir.replace-references" operation
	 */
	public static final String OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID = "source-reference-id";

	/**
	 * Parameter for target reference of the "$hapi.fhir.replace-references" operation
	 */
	public static final String OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID = "target-reference-id";

	/**
	 * If the request is being performed synchronously and the number of resources that need to change
	 * exceeds this amount, the operation will fail with 412 Precondition Failed.
	 */
	public static final String OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT = "resource-limit";

	/**
	 * $hapi.fhir.replace-references output Parameters names
	 */
	public static final String OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK = "task";

	public static final String OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME = "outcome";

	/**
	 * Operation name for the Resource "$merge" operation
	 * Hapi-fhir use is based on https://www.hl7.org/fhir/patient-operation-merge.html
	 */
	public static final String OPERATION_MERGE = "$merge";
	/**
	 * Patient $merge operation parameters
	 */
	public static final String OPERATION_MERGE_PARAM_SOURCE_PATIENT = "source-patient";

	public static final String OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER = "source-patient-identifier";
	public static final String OPERATION_MERGE_PARAM_TARGET_PATIENT = "target-patient";
	public static final String OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER = "target-patient-identifier";
	public static final String OPERATION_MERGE_PARAM_RESULT_PATIENT = "result-patient";
	public static final String OPERATION_MERGE_PARAM_BATCH_SIZE = "batch-size";
	public static final String OPERATION_MERGE_PARAM_PREVIEW = "preview";
	public static final String OPERATION_MERGE_PARAM_DELETE_SOURCE = "delete-source";
	public static final String OPERATION_MERGE_OUTPUT_PARAM_INPUT = "input";
	public static final String OPERATION_MERGE_OUTPUT_PARAM_OUTCOME = OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
	public static final String OPERATION_MERGE_OUTPUT_PARAM_RESULT = "result";
	public static final String OPERATION_MERGE_OUTPUT_PARAM_TASK = OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;

	public static final String HAPI_BATCH_JOB_ID_SYSTEM = "http://hapifhir.io/batch/jobId";
	public static final String OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT_STRING = "512";
	public static final Integer OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT =
			Integer.parseInt(OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT_STRING);
}
