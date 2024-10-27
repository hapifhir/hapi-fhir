/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.storage.interceptor.balp;

import org.hl7.fhir.r4.model.AuditEvent;

public class BalpConstants {
	/**
	 * Constant for {@link AuditEvent.AuditEventAgentNetworkType} representing the code
	 * <code>1 - Machine name</code>. This constant is used only for convenience since the
	 * existing Enum uses numerical codes that are not great for readability.
	 */
	public static final AuditEvent.AuditEventAgentNetworkType AUDIT_EVENT_AGENT_NETWORK_TYPE_MACHINE_NAME =
			AuditEvent.AuditEventAgentNetworkType._1;
	/**
	 * Constant for {@link AuditEvent.AuditEventAgentNetworkType} representing the code
	 * <code>2 - IP Address</code>. This constant is used only for convenience since the
	 * existing Enum uses numerical codes that are not great for readability.
	 */
	public static final AuditEvent.AuditEventAgentNetworkType AUDIT_EVENT_AGENT_NETWORK_TYPE_IP_ADDRESS =
			AuditEvent.AuditEventAgentNetworkType._2;
	/**
	 * Constant for {@link AuditEvent.AuditEventAgentNetworkType} representing the code
	 * <code>3 - URI</code>. This constant is used only for convenience since the
	 * existing Enum uses numerical codes that are not great for readability.
	 */
	public static final AuditEvent.AuditEventAgentNetworkType AUDIT_EVENT_AGENT_NETWORK_TYPE_URI =
			AuditEvent.AuditEventAgentNetworkType._5;

	public static final String CS_AUDIT_EVENT_TYPE = "http://terminology.hl7.org/CodeSystem/audit-event-type";
	public static final String CS_AUDIT_ENTITY_TYPE = "http://terminology.hl7.org/CodeSystem/audit-entity-type";
	public static final String CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT = "2";
	public static final String CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT_DISPLAY = "System Object";
	public static final String CS_AUDIT_ENTITY_TYPE_1_PERSON = "1";
	public static final String CS_AUDIT_ENTITY_TYPE_1_PERSON_DISPLAY = "Person";
	public static final String CS_OBJECT_ROLE = "http://terminology.hl7.org/CodeSystem/object-role";
	public static final String CS_OBJECT_ROLE_1_PATIENT = "1";
	public static final String CS_OBJECT_ROLE_1_PATIENT_DISPLAY = "Patient";
	public static final String CS_OBJECT_ROLE_4_DOMAIN_RESOURCE = "4";
	public static final String CS_OBJECT_ROLE_4_DOMAIN_RESOURCE_DISPLAY = "Domain Resource";
	public static final String CS_RESTFUL_INTERACTION = "http://hl7.org/fhir/restful-interaction";
	public static final String CS_OBJECT_ROLE_24_QUERY = "24";
	static final String CS_OBJECT_ROLE_24_QUERY_DISPLAY = "Query";
}
