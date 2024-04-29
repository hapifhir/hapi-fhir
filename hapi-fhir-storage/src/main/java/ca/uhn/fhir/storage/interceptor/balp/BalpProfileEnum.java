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
import org.hl7.fhir.r4.model.Coding;

import java.util.function.Supplier;

public enum BalpProfileEnum {
	BASIC_CREATE(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Create",
			AuditEvent.AuditEventAction.C,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),
	PATIENT_CREATE(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientCreate",
			AuditEvent.AuditEventAction.C,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),

	BASIC_UPDATE(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Update",
			AuditEvent.AuditEventAction.U,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),
	PATIENT_UPDATE(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientUpdate",
			AuditEvent.AuditEventAction.U,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),

	BASIC_DELETE(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Delete",
			AuditEvent.AuditEventAction.D,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110150", "Application"),
			() -> new Coding(
					"http://terminology.hl7.org/CodeSystem/provenance-participant-type", "custodian", "Custodian")),
	PATIENT_DELETE(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientDelete",
			AuditEvent.AuditEventAction.D,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110150", "Application"),
			() -> new Coding(
					"http://terminology.hl7.org/CodeSystem/provenance-participant-type", "custodian", "Custodian")),

	BASIC_READ(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Read",
			AuditEvent.AuditEventAction.R,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),
	PATIENT_READ(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientRead",
			AuditEvent.AuditEventAction.R,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),

	BASIC_QUERY(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Query",
			AuditEvent.AuditEventAction.E,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),
	PATIENT_QUERY(
			"https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientQuery",
			AuditEvent.AuditEventAction.E,
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110153", "Source Role ID"),
			() -> new Coding("http://dicom.nema.org/resources/ontology/DCM", "110152", "Destination Role ID")),
	;
	private final String myProfileUrl;
	private final AuditEvent.AuditEventAction myAction;
	private final Supplier<Coding> myAgentClientTypeCoding;
	private final Supplier<Coding> myAgentServerTypeCoding;

	BalpProfileEnum(
			String theProfileUrl,
			AuditEvent.AuditEventAction theAction,
			Supplier<Coding> theAgentClientTypeCoding,
			Supplier<Coding> theAgentServerTypeCoding) {
		myProfileUrl = theProfileUrl;
		myAction = theAction;
		myAgentClientTypeCoding = theAgentClientTypeCoding;
		myAgentServerTypeCoding = theAgentServerTypeCoding;
	}

	public Coding getAgentClientTypeCoding() {
		return myAgentClientTypeCoding.get();
	}

	public Coding getAgentServerTypeCoding() {
		return myAgentServerTypeCoding.get();
	}

	public String getProfileUrl() {
		return myProfileUrl;
	}

	public AuditEvent.AuditEventAction getAction() {
		return myAction;
	}
}
