package ca.uhn.fhir.jpa.model.util;

/*-
 * #%L
 * HAPI FHIR Model
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

}
