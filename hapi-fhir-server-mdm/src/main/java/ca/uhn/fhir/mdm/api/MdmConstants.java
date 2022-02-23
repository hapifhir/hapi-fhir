package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

public class MdmConstants {

	/**
	 * TAG system for Golden Resources which are managed by HAPI MDM.
	 */

	public static final String SYSTEM_MDM_MANAGED = "https://hapifhir.org/NamingSystem/managing-mdm-system";
	public static final String CODE_HAPI_MDM_MANAGED = "HAPI-MDM";
	public static final String DISPLAY_HAPI_MDM_MANAGED = "This Golden Resource can only be modified by HAPI MDM system.";
	public static final String CODE_NO_MDM_MANAGED = "NO-MDM";
	public static final String HAPI_ENTERPRISE_IDENTIFIER_SYSTEM = "http://hapifhir.io/fhir/NamingSystem/mdm-golden-resource-enterprise-id";
	public static final String ALL_RESOURCE_SEARCH_PARAM_TYPE = "*";

	public static final String FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE = "http://hl7.org/fhir/StructureDefinition/match-grade";

	public static final String SYSTEM_GOLDEN_RECORD_STATUS = "http://hapifhir.io/fhir/NamingSystem/mdm-record-status";
	public static final String CODE_GOLDEN_RECORD = "GOLDEN_RECORD";
	public static final String CODE_GOLDEN_RECORD_REDIRECTED = "REDIRECTED";
	public static final String DISPLAY_GOLDEN_RECORD = "Golden Record";
	public static final String DISPLAY_GOLDEN_REDIRECT = "This resource was found to be a duplicate and has been redirected.";

	public static final String UNKNOWN_MDM_TYPES = "Unknown Resource Types";
}
