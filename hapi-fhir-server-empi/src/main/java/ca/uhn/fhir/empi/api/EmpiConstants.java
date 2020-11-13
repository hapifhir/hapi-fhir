package ca.uhn.fhir.empi.api;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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

public class EmpiConstants {
	/**
	 * TAG system for Person resources which are managed by HAPI EMPI.
	 */

	public static final String SYSTEM_MDM_MANAGED = "https://hapifhir.org/NamingSystem/managing-mdm-system";
	public static final String CODE_HAPI_MDM_MANAGED = "HAPI-MDM";
	public static final String DISPLAY_HAPI_EMPI_MANAGED = "This Person can only be modified by Smile CDR's EMPI system.";
	public static final String CODE_NO_EMPI_MANAGED = "NO-EMPI";
	public static final String HAPI_ENTERPRISE_IDENTIFIER_SYSTEM = "http://hapifhir.io/fhir/NamingSystem/empi-person-enterprise-id";
	public static final String ALL_RESOURCE_SEARCH_PARAM_TYPE = "*";

	public static final String FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE = "http://hl7.org/fhir/StructureDefinition/match-grade";

	public static final String SYSTEM_GOLDEN_RECORD_STATUS = "http://hapifhir.io/fhir/NamingSystem/mdm-record-status";
	public static final String CODE_GOLDEN_RECORD = "GOLDEN_RECORD";
	public static final String CODE_GOLDEN_RECORD_REDIRECTED = "REDIRECTED";
	public static final String DISPLAY_GOLDEN_RECORD = "Golden Record";
	public static final String DISPLAY_GOLDEN_REDIRECT = "This resource was found to be a duplicate and has been redirected.";
}
