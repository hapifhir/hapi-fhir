package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

	public static final String PARTITION_MANAGEMENT_PARTITION_ID = "id";
	public static final String PARTITION_MANAGEMENT_PARTITION_NAME = "name";
	public static final String PARTITION_MANAGEMENT_PARTITION_DESC = "description";

	/**
	 * Operation name: diff
	 */
	public static final String DIFF_OPERATION_NAME = "$diff";
	public static final String DIFF_FROM_VERSION_PARAMETER = "fromVersion";

	public static final String DIFF_FROM_PARAMETER = "from";
	public static final String DIFF_TO_PARAMETER = "to";
	public static final String DIFF_INCLUDE_META_PARAMETER = "includeMeta";

	/**
	 * EMPI Operations
	 */
	public static final String EMPI_MATCH = "$match";
	//TODO GGG MDM: implement a server-level MDM match to complement the FHIR-spec $match for /Patient
	public static final String MDM_MATCH = "$mdm-match";
	public static final String MDM_MATCH_RESOURCE = "resource";
	public static final String MDM_RESOURCE_TYPE = "resourceType";

	//TODO GGG MDM: rename all these vars
	public static final String MDM_MERGE_GOLDEN_RESOURCES = "$mdm-merge-golden-resources";
	public static final String MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID = "fromGoldenResourceId";
	public static final String MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID = "toGoldenResourceId";

	public static final String MDM_UPDATE_LINK = "$mdm-update-link";
	public static final String MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID = "goldenResourceId";
	public static final String MDM_UPDATE_LINK_RESOURCE_ID = "resourceId";
	public static final String MDM_UPDATE_LINK_MATCH_RESULT = "matchResult";

	public static final String MDM_QUERY_LINKS = "$mdm-query-links";
	public static final String MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID = "goldenResourceId";
	public static final String MDM_QUERY_LINKS_RESOURCE_ID = "resourceId";
	public static final String MDM_QUERY_LINKS_MATCH_RESULT = "matchResult";
	public static final String MDM_QUERY_LINKS_LINK_SOURCE = "linkSource";

	public static final String MDM_DUPLICATE_GOLDEN_RESOURCES = "$mdm-duplicate-golden-resources";
	public static final String MDM_NOT_DUPLICATE = "$mdm-not-duplicate";

	public static final String MDM_CLEAR = "$mdm-clear";
	public static final String MDM_CLEAR_SOURCE_TYPE = "sourceType";
	public static final String OPERATION_MDM_SUBMIT = "$mdm-submit";
	public static final String MDM_BATCH_RUN_CRITERIA = "criteria" ;
	public static final String OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT = "submitted" ;
   public static final String OPERATION_MDM_CLEAR_OUT_PARAM_DELETED_COUNT = "deleted";
	public static final String MDM_BATCH_RUN_RESOURCE_TYPE = "resourceType";
}
