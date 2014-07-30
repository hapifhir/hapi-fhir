
package ca.uhn.fhir.model.dstu.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum FHIRDefinedTypeEnum {

	/**
	 * Code Value: <b>Address</b>
	 *
	 * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.
	 */
	ADDRESS("Address", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Age</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.  If value is present, it SHALL be positive.
	 */
	AGE("Age", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Attachment</b>
	 *
	 * For referring to data content defined in other formats.
	 */
	ATTACHMENT("Attachment", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>CodeableConcept</b>
	 *
	 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
	 */
	CODEABLECONCEPT("CodeableConcept", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Coding</b>
	 *
	 * A reference to a code defined by a terminology system.
	 */
	CODING("Coding", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Contact</b>
	 *
	 * All kinds of technology mediated contact details for a person or organization, including telephone, email, etc.
	 */
	CONTACT("Contact", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Count</b>
	 *
	 * There SHALL be a code with a value of "1" if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.  If present, the value SHALL a whole number.
	 */
	COUNT("Count", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Distance</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.
	 */
	DISTANCE("Distance", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Duration</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.
	 */
	DURATION("Duration", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Extension</b>
	 *
	 * Optional Extensions Element - found in all resources.
	 */
	EXTENSION("Extension", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>HumanName</b>
	 *
	 * A human's name with the ability to identify parts and usage.
	 */
	HUMANNAME("HumanName", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Identifier</b>
	 *
	 * A technical identifier - identifies some entity uniquely and unambiguously.
	 */
	IDENTIFIER("Identifier", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Money</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = "urn:std:iso:4217" - currency).
	 */
	MONEY("Money", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Narrative</b>
	 *
	 * A human-readable formatted text, including images.
	 */
	NARRATIVE("Narrative", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Period</b>
	 *
	 * A time period defined by a start and end date and optionally time.
	 */
	PERIOD("Period", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Quantity</b>
	 *
	 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
	 */
	QUANTITY("Quantity", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Range</b>
	 *
	 * A set of ordered Quantities defined by a low and high limit.
	 */
	RANGE("Range", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Ratio</b>
	 *
	 * A relationship of two Quantity values - expressed as a numerator and a denominator.
	 */
	RATIO("Ratio", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>ResourceReference</b>
	 *
	 * A reference from one resource to another.
	 */
	RESOURCEREFERENCE("ResourceReference", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>SampledData</b>
	 *
	 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
	 */
	SAMPLEDDATA("SampledData", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Schedule</b>
	 *
	 * Specifies an event that may occur multiple times. Schedules are used for to reord when things are expected or requested to occur.
	 */
	SCHEDULE("Schedule", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>base64Binary</b>
	 *
	 * A stream of bytes
	 */
	BASE64BINARY("base64Binary", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>boolean</b>
	 *
	 * Value of "true" or "false"
	 */
	BOOLEAN("boolean", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>code</b>
	 *
	 * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents
	 */
	CODE("code", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>date</b>
	 *
	 * A date, or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.
	 */
	DATE("date", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>dateTime</b>
	 *
	 * A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds may be provided but may also be ignored.  Dates SHALL be valid dates.
	 */
	DATETIME("dateTime", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>decimal</b>
	 *
	 * A rational number with implicit precision
	 */
	DECIMAL("decimal", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>id</b>
	 *
	 * A whole number in the range 0 to 2^64-1, optionally represented in hex, a uuid, an oid or any other combination of lower-case letters a-z, numerals, "-" and ".", with a length limit of 36 characters
	 */
	ID("id", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>instant</b>
	 *
	 * An instant in time - known at least to the second
	 */
	INSTANT("instant", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>integer</b>
	 *
	 * A whole number
	 */
	INTEGER("integer", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>oid</b>
	 *
	 * An oid represented as a URI
	 */
	OID("oid", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>string</b>
	 *
	 * A sequence of Unicode characters
	 */
	STRING("string", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>uri</b>
	 *
	 * String of characters used to identify a name or a resource
	 */
	URI("uri", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>uuid</b>
	 *
	 * A UUID, represented as a URI
	 */
	UUID("uuid", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>AdverseReaction</b>
	 *
	 * Records an unexpected reaction suspected to be related to the exposure of the reaction subject to a substance.
	 */
	ADVERSEREACTION("AdverseReaction", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Alert</b>
	 *
	 * Prospective warnings of potential issues when providing care to the patient.
	 */
	ALERT("Alert", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>AllergyIntolerance</b>
	 *
	 * Indicates the patient has a susceptibility to an adverse reaction upon exposure to a specified substance.
	 */
	ALLERGYINTOLERANCE("AllergyIntolerance", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>CarePlan</b>
	 *
	 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
	 */
	CAREPLAN("CarePlan", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Composition</b>
	 *
	 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement.
	 */
	COMPOSITION("Composition", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>ConceptMap</b>
	 *
	 * A statement of relationships from one set of concepts to one or more other concept systems.
	 */
	CONCEPTMAP("ConceptMap", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Condition</b>
	 *
	 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary.
	 */
	CONDITION("Condition", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Conformance</b>
	 *
	 * A conformance statement is a set of requirements for a desired implementation or a description of how a target application fulfills those requirements in a particular implementation.
	 */
	CONFORMANCE("Conformance", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Device</b>
	 *
	 * This resource identifies an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
	 */
	DEVICE("Device", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>DeviceObservationReport</b>
	 *
	 * Describes the data produced by a device at a point in time.
	 */
	DEVICEOBSERVATIONREPORT("DeviceObservationReport", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>DiagnosticOrder</b>
	 *
	 * A request for a diagnostic investigation service to be performed.
	 */
	DIAGNOSTICORDER("DiagnosticOrder", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>DiagnosticReport</b>
	 *
	 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretation, and formatted representation of diagnostic reports.
	 */
	DIAGNOSTICREPORT("DiagnosticReport", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>DocumentManifest</b>
	 *
	 * A manifest that defines a set of documents.
	 */
	DOCUMENTMANIFEST("DocumentManifest", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>DocumentReference</b>
	 *
	 * A reference to a document.
	 */
	DOCUMENTREFERENCE("DocumentReference", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Encounter</b>
	 *
	 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
	 */
	ENCOUNTER("Encounter", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>FamilyHistory</b>
	 *
	 * Significant health events and conditions for people related to the subject relevant in the context of care for the subject.
	 */
	FAMILYHISTORY("FamilyHistory", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Group</b>
	 *
	 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized.  I.e. A collection of entities that isn't an Organization.
	 */
	GROUP("Group", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>ImagingStudy</b>
	 *
	 * Manifest of a set of images produced in study. The set of images may include every image in the study, or it may be an incomplete sample, such as a list of key images.
	 */
	IMAGINGSTUDY("ImagingStudy", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Immunization</b>
	 *
	 * Immunization event information.
	 */
	IMMUNIZATION("Immunization", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>ImmunizationRecommendation</b>
	 *
	 * A patient's point-of-time immunization status and recommendation with optional supporting justification.
	 */
	IMMUNIZATIONRECOMMENDATION("ImmunizationRecommendation", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>List</b>
	 *
	 * A set of information summarized from a list of other resources.
	 */
	LIST("List", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Location</b>
	 *
	 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
	 */
	LOCATION("Location", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Media</b>
	 *
	 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
	 */
	MEDIA("Media", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Medication</b>
	 *
	 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging.
	 */
	MEDICATION("Medication", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>MedicationAdministration</b>
	 *
	 * Describes the event of a patient being given a dose of a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
	 */
	MEDICATIONADMINISTRATION("MedicationAdministration", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>MedicationDispense</b>
	 *
	 * Dispensing a medication to a named patient.  This includes a description of the supply provided and the instructions for administering the medication.
	 */
	MEDICATIONDISPENSE("MedicationDispense", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>MedicationPrescription</b>
	 *
	 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
	 */
	MEDICATIONPRESCRIPTION("MedicationPrescription", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>MedicationStatement</b>
	 *
	 * A record of medication being taken by a patient, or that the medication has been given to a patient where the record is the result of a report from the patient or another clinician.
	 */
	MEDICATIONSTATEMENT("MedicationStatement", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>MessageHeader</b>
	 *
	 * The header for a message exchange that is either requesting or responding to an action.  The resource(s) that are the subject of the action as well as other Information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
	 */
	MESSAGEHEADER("MessageHeader", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Observation</b>
	 *
	 * Measurements and simple assertions made about a patient, device or other subject.
	 */
	OBSERVATION("Observation", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>OperationOutcome</b>
	 *
	 * A collection of error, warning or information messages that result from a system action.
	 */
	OPERATIONOUTCOME("OperationOutcome", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Order</b>
	 *
	 * A request to perform an action.
	 */
	ORDER("Order", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>OrderResponse</b>
	 *
	 * A response to an order.
	 */
	ORDERRESPONSE("OrderResponse", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Organization</b>
	 *
	 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
	 */
	ORGANIZATION("Organization", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Other</b>
	 *
	 * Other is a conformant for handling resource concepts not yet defined for FHIR or outside HL7's scope of interest.
	 */
	OTHER("Other", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Patient</b>
	 *
	 * Demographics and other administrative information about a person or animal receiving care or other health-related services.
	 */
	PATIENT("Patient", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Practitioner</b>
	 *
	 * A person who is directly or indirectly involved in the provisioning of healthcare.
	 */
	PRACTITIONER("Practitioner", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Procedure</b>
	 *
	 * An action that is performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy.
	 */
	PROCEDURE("Procedure", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Profile</b>
	 *
	 * A Resource Profile - a statement of use of one or more FHIR Resources.  It may include constraints on Resources and Data Types, Terminology Binding Statements and Extension Definitions.
	 */
	PROFILE("Profile", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Provenance</b>
	 *
	 * Provenance information that describes the activity that led to the creation of a set of resources. This information can be used to help determine their reliability or trace where the information in them came from. The focus of the provenance resource is record keeping, audit and traceability, and not explicit statements of clinical significance.
	 */
	PROVENANCE("Provenance", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Query</b>
	 *
	 * A description of a query with a set of parameters.
	 */
	QUERY("Query", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Questionnaire</b>
	 *
	 * A structured set of questions and their answers. The Questionnaire may contain questions, answers or both. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
	 */
	QUESTIONNAIRE("Questionnaire", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>RelatedPerson</b>
	 *
	 * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
	 */
	RELATEDPERSON("RelatedPerson", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>SecurityEvent</b>
	 *
	 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
	 */
	SECURITYEVENT("SecurityEvent", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Specimen</b>
	 *
	 * Sample for analysis.
	 */
	SPECIMEN("Specimen", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Substance</b>
	 *
	 * A homogeneous material with a definite composition.
	 */
	SUBSTANCE("Substance", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>Supply</b>
	 *
	 * A supply - a  request for something, and provision of what is supplied.
	 */
	SUPPLY("Supply", "http://hl7.org/fhir/defined-types"),
	
	/**
	 * Code Value: <b>ValueSet</b>
	 *
	 * A value set specifies a set of codes drawn from one or more code systems.
	 */
	VALUESET("ValueSet", "http://hl7.org/fhir/defined-types"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/defined-types
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/defined-types";

	/**
	 * Name for this Value Set:
	 * FHIRDefinedType
	 */
	public static final String VALUESET_NAME = "FHIRDefinedType";

	private static Map<String, FHIRDefinedTypeEnum> CODE_TO_ENUM = new HashMap<String, FHIRDefinedTypeEnum>();
	private static Map<String, Map<String, FHIRDefinedTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, FHIRDefinedTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (FHIRDefinedTypeEnum next : FHIRDefinedTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, FHIRDefinedTypeEnum>());
			}
			SYSTEM_TO_CODE_TO_ENUM.get(next.getSystem()).put(next.getCode(), next);			
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the code system associated with this enumerated value
	 */
	public String getSystem() {
		return mySystem;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public FHIRDefinedTypeEnum forCode(String theCode) {
		FHIRDefinedTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<FHIRDefinedTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<FHIRDefinedTypeEnum>() {
		@Override
		public String toCodeString(FHIRDefinedTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(FHIRDefinedTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public FHIRDefinedTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public FHIRDefinedTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, FHIRDefinedTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	FHIRDefinedTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
