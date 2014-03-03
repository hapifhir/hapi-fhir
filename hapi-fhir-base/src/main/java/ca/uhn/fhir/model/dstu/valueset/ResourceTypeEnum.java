
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ResourceTypeEnum {

	/**
	 * AdverseReaction
	 * 
	 *
	 * Records an unexpected reaction suspected to be related to the exposure of the reaction subject to a substance.
	 */
	ADVERSEREACTION("AdverseReaction"),
	
	/**
	 * Alert
	 * 
	 *
	 * Prospective warnings of potential issues when providing care to the patient.
	 */
	ALERT("Alert"),
	
	/**
	 * AllergyIntolerance
	 * 
	 *
	 * Indicates the patient has a susceptibility to an adverse reaction upon exposure to a specified substance.
	 */
	ALLERGYINTOLERANCE("AllergyIntolerance"),
	
	/**
	 * CarePlan
	 * 
	 *
	 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
	 */
	CAREPLAN("CarePlan"),
	
	/**
	 * Composition
	 * 
	 *
	 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement.
	 */
	COMPOSITION("Composition"),
	
	/**
	 * ConceptMap
	 * 
	 *
	 * A statement of relationships from one set of concepts to one or more other concept systems.
	 */
	CONCEPTMAP("ConceptMap"),
	
	/**
	 * Condition
	 * 
	 *
	 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary.
	 */
	CONDITION("Condition"),
	
	/**
	 * Conformance
	 * 
	 *
	 * A conformance statement is a set of requirements for a desired implementation or a description of how a target application fulfills those requirements in a particular implementation.
	 */
	CONFORMANCE("Conformance"),
	
	/**
	 * Device
	 * 
	 *
	 * This resource identifies an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
	 */
	DEVICE("Device"),
	
	/**
	 * DeviceObservationReport
	 * 
	 *
	 * Describes the data produced by a device at a point in time.
	 */
	DEVICEOBSERVATIONREPORT("DeviceObservationReport"),
	
	/**
	 * DiagnosticOrder
	 * 
	 *
	 * A request for a diagnostic investigation service to be performed.
	 */
	DIAGNOSTICORDER("DiagnosticOrder"),
	
	/**
	 * DiagnosticReport
	 * 
	 *
	 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretation, and formatted representation of diagnostic reports.
	 */
	DIAGNOSTICREPORT("DiagnosticReport"),
	
	/**
	 * DocumentManifest
	 * 
	 *
	 * A manifest that defines a set of documents.
	 */
	DOCUMENTMANIFEST("DocumentManifest"),
	
	/**
	 * DocumentReference
	 * 
	 *
	 * A reference to a document.
	 */
	DOCUMENTREFERENCE("DocumentReference"),
	
	/**
	 * Encounter
	 * 
	 *
	 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
	 */
	ENCOUNTER("Encounter"),
	
	/**
	 * FamilyHistory
	 * 
	 *
	 * Significant health events and conditions for people related to the subject relevant in the context of care for the subject.
	 */
	FAMILYHISTORY("FamilyHistory"),
	
	/**
	 * Group
	 * 
	 *
	 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized.  I.e. A collection of entities that isn't an Organization.
	 */
	GROUP("Group"),
	
	/**
	 * ImagingStudy
	 * 
	 *
	 * Manifest of a set of images produced in study. The set of images may include every image in the study, or it may be an incomplete sample, such as a list of key images.
	 */
	IMAGINGSTUDY("ImagingStudy"),
	
	/**
	 * Immunization
	 * 
	 *
	 * Immunization event information.
	 */
	IMMUNIZATION("Immunization"),
	
	/**
	 * ImmunizationRecommendation
	 * 
	 *
	 * A patient's point-of-time immunization status and recommendation with optional supporting justification.
	 */
	IMMUNIZATIONRECOMMENDATION("ImmunizationRecommendation"),
	
	/**
	 * List
	 * 
	 *
	 * A set of information summarized from a list of other resources.
	 */
	LIST("List"),
	
	/**
	 * Location
	 * 
	 *
	 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
	 */
	LOCATION("Location"),
	
	/**
	 * Media
	 * 
	 *
	 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
	 */
	MEDIA("Media"),
	
	/**
	 * Medication
	 * 
	 *
	 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging.
	 */
	MEDICATION("Medication"),
	
	/**
	 * MedicationAdministration
	 * 
	 *
	 * Describes the event of a patient being given a dose of a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
	 */
	MEDICATIONADMINISTRATION("MedicationAdministration"),
	
	/**
	 * MedicationDispense
	 * 
	 *
	 * Dispensing a medication to a named patient.  This includes a description of the supply provided and the instructions for administering the medication.
	 */
	MEDICATIONDISPENSE("MedicationDispense"),
	
	/**
	 * MedicationPrescription
	 * 
	 *
	 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
	 */
	MEDICATIONPRESCRIPTION("MedicationPrescription"),
	
	/**
	 * MedicationStatement
	 * 
	 *
	 * A record of medication being taken by a patient, or that the medication has been given to a patient where the record is the result of a report from the patient or another clinician.
	 */
	MEDICATIONSTATEMENT("MedicationStatement"),
	
	/**
	 * MessageHeader
	 * 
	 *
	 * The header for a message exchange that is either requesting or responding to an action.  The resource(s) that are the subject of the action as well as other Information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
	 */
	MESSAGEHEADER("MessageHeader"),
	
	/**
	 * Observation
	 * 
	 *
	 * Measurements and simple assertions made about a patient, device or other subject.
	 */
	OBSERVATION("Observation"),
	
	/**
	 * OperationOutcome
	 * 
	 *
	 * A collection of error, warning or information messages that result from a system action.
	 */
	OPERATIONOUTCOME("OperationOutcome"),
	
	/**
	 * Order
	 * 
	 *
	 * A request to perform an action.
	 */
	ORDER("Order"),
	
	/**
	 * OrderResponse
	 * 
	 *
	 * A response to an order.
	 */
	ORDERRESPONSE("OrderResponse"),
	
	/**
	 * Organization
	 * 
	 *
	 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
	 */
	ORGANIZATION("Organization"),
	
	/**
	 * Other
	 * 
	 *
	 * Other is a conformant for handling resource concepts not yet defined for FHIR or outside HL7's scope of interest.
	 */
	OTHER("Other"),
	
	/**
	 * Patient
	 * 
	 *
	 * Demographics and other administrative information about a person or animal receiving care or other health-related services.
	 */
	PATIENT("Patient"),
	
	/**
	 * Practitioner
	 * 
	 *
	 * A person who is directly or indirectly involved in the provisioning of healthcare.
	 */
	PRACTITIONER("Practitioner"),
	
	/**
	 * Procedure
	 * 
	 *
	 * An action that is performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy.
	 */
	PROCEDURE("Procedure"),
	
	/**
	 * Profile
	 * 
	 *
	 * A Resource Profile - a statement of use of one or more FHIR Resources.  It may include constraints on Resources and Data Types, Terminology Binding Statements and Extension Definitions.
	 */
	PROFILE("Profile"),
	
	/**
	 * Provenance
	 * 
	 *
	 * Provenance information that describes the activity that led to the creation of a set of resources. This information can be used to help determine their reliability or trace where the information in them came from. The focus of the provenance resource is record keeping, audit and traceability, and not explicit statements of clinical significance.
	 */
	PROVENANCE("Provenance"),
	
	/**
	 * Query
	 * 
	 *
	 * A description of a query with a set of parameters.
	 */
	QUERY("Query"),
	
	/**
	 * Questionnaire
	 * 
	 *
	 * A structured set of questions and their answers. The Questionnaire may contain questions, answers or both. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
	 */
	QUESTIONNAIRE("Questionnaire"),
	
	/**
	 * RelatedPerson
	 * 
	 *
	 * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
	 */
	RELATEDPERSON("RelatedPerson"),
	
	/**
	 * SecurityEvent
	 * 
	 *
	 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
	 */
	SECURITYEVENT("SecurityEvent"),
	
	/**
	 * Specimen
	 * 
	 *
	 * Sample for analysis.
	 */
	SPECIMEN("Specimen"),
	
	/**
	 * Substance
	 * 
	 *
	 * A homogeneous material with a definite composition.
	 */
	SUBSTANCE("Substance"),
	
	/**
	 * Supply
	 * 
	 *
	 * A supply - a  request for something, and provision of what is supplied.
	 */
	SUPPLY("Supply"),
	
	/**
	 * ValueSet
	 * 
	 *
	 * A value set specifies a set of codes drawn from one or more code systems.
	 */
	VALUESET("ValueSet"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/resource-types
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/resource-types";

	/**
	 * Name for this Value Set:
	 * ResourceType
	 */
	public static final String VALUESET_NAME = "ResourceType";

	private static Map<String, ResourceTypeEnum> CODE_TO_ENUM = new HashMap<String, ResourceTypeEnum>();
	private String myCode;
	
	static {
		for (ResourceTypeEnum next : ResourceTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public ResourceTypeEnum forCode(String theCode) {
		ResourceTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ResourceTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<ResourceTypeEnum>() {
		@Override
		public String toCodeString(ResourceTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ResourceTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ResourceTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
