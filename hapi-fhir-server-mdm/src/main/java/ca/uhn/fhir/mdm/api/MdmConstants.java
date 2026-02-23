/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.api;

public class MdmConstants {

	/**
	 * TAG system for Golden Resources which are managed by HAPI MDM.
	 */
	public static final String SYSTEM_MDM_MANAGED = "https://hapifhir.org/NamingSystem/managing-mdm-system";

	public static final String CODE_HAPI_MDM_MANAGED = "HAPI-MDM";
	public static final String DISPLAY_HAPI_MDM_MANAGED =
			"This Golden Resource can only be modified by HAPI MDM system.";
	public static final String CODE_NO_MDM_MANAGED = "NO-MDM";
	public static final String HAPI_ENTERPRISE_IDENTIFIER_SYSTEM =
			"http://hapifhir.io/fhir/NamingSystem/mdm-golden-resource-enterprise-id";
	public static final String ALL_RESOURCE_SEARCH_PARAM_TYPE = "*";

	/**
	 * Blocked resource tag info
	 */
	public static final String CODE_BLOCKED = "BLOCKED_RESOURCE";

	public static final String CODE_BLOCKED_DISPLAY = "Source Resource is omitted from MDM matching.";

	public static final String FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE =
			"http://hl7.org/fhir/StructureDefinition/match-grade";

	public static final String SYSTEM_GOLDEN_RECORD_STATUS = "http://hapifhir.io/fhir/NamingSystem/mdm-record-status";
	public static final String SUBSCRIPTION_TOPIC_URL = "http://hapifhir.io/fhir/r5/SubscriptionTopic/mdm";
	public static final String CODE_GOLDEN_RECORD = "GOLDEN_RECORD";
	public static final String CODE_GOLDEN_RECORD_REDIRECTED = "REDIRECTED";
	public static final String DISPLAY_GOLDEN_RECORD = "Golden Record";
	public static final String DISPLAY_GOLDEN_REDIRECT =
			"This resource was found to be a duplicate and has been redirected.";

	public static final String UNKNOWN_MDM_TYPES = "Unknown Resource Types";

	/**
	 * Interceptor order constant for {@link ca.uhn.fhir.mdm.interceptor.MdmReadVirtualizationInterceptor}, which
	 * should fire before {@link ca.uhn.fhir.mdm.interceptor.MdmSearchExpandingInterceptor} since it is a
	 * superset of the same functionality and only one should run if they are both registered for whatever
	 * reason.
	 */
	public static final int STORAGE_PRESEARCH_PARTITION_SELECTED_MDM_READ_VIRTUALIZATION_INTERCEPTOR = 0;

	/**
	 * @see #STORAGE_PRESEARCH_PARTITION_SELECTED_MDM_READ_VIRTUALIZATION_INTERCEPTOR
	 */
	public static final int STORAGE_PRESEARCH_PARTITION_SELECTED_MDM_SEARCH_EXPANDING_INTERCEPTOR = 1;
}
