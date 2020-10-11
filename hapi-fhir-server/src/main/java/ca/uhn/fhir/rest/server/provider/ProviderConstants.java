package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
	public static final String EMPI_MATCH_RESOURCE = "resource";

	public static final String EMPI_MERGE_PERSONS = "$empi-merge-persons";
	public static final String EMPI_MERGE_PERSONS_FROM_PERSON_ID = "fromPersonId";
	public static final String EMPI_MERGE_PERSONS_TO_PERSON_ID = "toPersonId";

	public static final String EMPI_UPDATE_LINK = "$empi-update-link";
	public static final String EMPI_UPDATE_LINK_PERSON_ID = "personId";
	public static final String EMPI_UPDATE_LINK_TARGET_ID = "targetId";
	public static final String EMPI_UPDATE_LINK_MATCH_RESULT = "matchResult";

	public static final String EMPI_QUERY_LINKS = "$empi-query-links";
	public static final String EMPI_QUERY_LINKS_PERSON_ID = "personId";
	public static final String EMPI_QUERY_LINKS_TARGET_ID = "targetId";
	public static final String EMPI_QUERY_LINKS_MATCH_RESULT = "matchResult";
	public static final String EMPI_QUERY_LINKS_LINK_SOURCE = "linkSource";

	public static final String EMPI_DUPLICATE_PERSONS = "$empi-duplicate-persons";
	public static final String EMPI_NOT_DUPLICATE = "$empi-not-duplicate";

	public static final String EMPI_CLEAR = "$empi-clear";
	public static final String EMPI_CLEAR_TARGET_TYPE = "targetType";
	public static final String OPERATION_EMPI_SUBMIT = "$empi-submit";
	public static final String EMPI_BATCH_RUN_CRITERIA= "criteria" ;
	public static final String OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT = "submitted" ;
   public static final String OPERATION_EMPI_CLEAR_OUT_PARAM_DELETED_COUNT = "deleted";
}
