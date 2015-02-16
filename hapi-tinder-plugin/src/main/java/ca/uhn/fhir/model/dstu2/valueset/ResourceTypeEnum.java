
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ResourceTypeEnum {

	/**
	 * Code Value: <b>Alert</b>
	 *
	 * Prospective warnings of potential issues when providing care to the patient.
	 */
	ALERT("Alert", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>AllergyIntolerance</b>
	 *
	 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
	 */
	ALLERGYINTOLERANCE("AllergyIntolerance", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Appointment</b>
	 *
	 * A scheduled healthcare event for a patient and/or practitioner(s) where a service may take place at a specific date/time.
	 */
	APPOINTMENT("Appointment", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>AppointmentResponse</b>
	 *
	 * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
	 */
	APPOINTMENTRESPONSE("AppointmentResponse", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Basic</b>
	 *
	 * Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.
	 */
	BASIC("Basic", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Binary</b>
	 *
	 * A binary resource can contain any content, whether text, image, pdf, zip archive, etc.
	 */
	BINARY("Binary", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Bundle</b>
	 *
	 * A container for a group of resources.
	 */
	BUNDLE("Bundle", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>CarePlan</b>
	 *
	 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
	 */
	CAREPLAN("CarePlan", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>CarePlan2</b>
	 *
	 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
	 */
	CAREPLAN2("CarePlan2", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ClaimResponse</b>
	 *
	 * This resource provides the adjudication details from the processing of a Claim resource.
	 */
	CLAIMRESPONSE("ClaimResponse", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ClinicalAssessment</b>
	 *
	 * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow.
	 */
	CLINICALASSESSMENT("ClinicalAssessment", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Communication</b>
	 *
	 * An occurrence of information being transmitted. E.g., an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
	 */
	COMMUNICATION("Communication", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>CommunicationRequest</b>
	 *
	 * A request to convey information. E.g., the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
	 */
	COMMUNICATIONREQUEST("CommunicationRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Composition</b>
	 *
	 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement.
	 */
	COMPOSITION("Composition", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ConceptMap</b>
	 *
	 * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.
	 */
	CONCEPTMAP("ConceptMap", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Condition</b>
	 *
	 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary.
	 */
	CONDITION("Condition", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Conformance</b>
	 *
	 * A conformance statement is a set of requirements for a desired implementation or a description of how a target application fulfills those requirements in a particular implementation.
	 */
	CONFORMANCE("Conformance", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Contract</b>
	 *
	 * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
	 */
	CONTRACT("Contract", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Contraindication</b>
	 *
	 * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient.  E.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
	 */
	CONTRAINDICATION("Contraindication", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Coverage</b>
	 *
	 * Financial instrument which may be used to pay for or reimburse for health care products and services.
	 */
	COVERAGE("Coverage", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DataElement</b>
	 *
	 * The formal description of a single piece of information that can be gathered and reported.
	 */
	DATAELEMENT("DataElement", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Device</b>
	 *
	 * This resource identifies an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
	 */
	DEVICE("Device", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DeviceComponent</b>
	 *
	 * Describes the characteristics, operational status and capabilities of a medical-related component of a medical device.
	 */
	DEVICECOMPONENT("DeviceComponent", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DeviceMetric</b>
	 *
	 * Describes a measurement, calculation or setting capability of a medical device.
	 */
	DEVICEMETRIC("DeviceMetric", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DeviceUseRequest</b>
	 *
	 * Represents a request for the use of a device.
	 */
	DEVICEUSEREQUEST("DeviceUseRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DeviceUseStatement</b>
	 *
	 * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
	 */
	DEVICEUSESTATEMENT("DeviceUseStatement", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DiagnosticOrder</b>
	 *
	 * A request for a diagnostic investigation service to be performed.
	 */
	DIAGNOSTICORDER("DiagnosticOrder", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DiagnosticReport</b>
	 *
	 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretation, and formatted representation of diagnostic reports.
	 */
	DIAGNOSTICREPORT("DiagnosticReport", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DocumentManifest</b>
	 *
	 * A manifest that defines a set of documents.
	 */
	DOCUMENTMANIFEST("DocumentManifest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>DocumentReference</b>
	 *
	 * A reference to a document.
	 */
	DOCUMENTREFERENCE("DocumentReference", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>EligibilityRequest</b>
	 *
	 * This resource provides the insurance eligibility details from the insurer regarding a specified coverage and optionally some class of service.
	 */
	ELIGIBILITYREQUEST("EligibilityRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>EligibilityResponse</b>
	 *
	 * This resource provides eligibility and plan details from the processing of an Eligibility resource.
	 */
	ELIGIBILITYRESPONSE("EligibilityResponse", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Encounter</b>
	 *
	 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
	 */
	ENCOUNTER("Encounter", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>EnrollmentRequest</b>
	 *
	 * This resource provides the insurance Enrollment details to the insurer regarding a specified coverage.
	 */
	ENROLLMENTREQUEST("EnrollmentRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>EnrollmentResponse</b>
	 *
	 * This resource provides Enrollment and plan details from the processing of an Enrollment resource.
	 */
	ENROLLMENTRESPONSE("EnrollmentResponse", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>EpisodeOfCare</b>
	 *
	 * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
	 */
	EPISODEOFCARE("EpisodeOfCare", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ExplanationOfBenefit</b>
	 *
	 * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
	 */
	EXPLANATIONOFBENEFIT("ExplanationOfBenefit", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ExtensionDefinition</b>
	 *
	 * Defines an extension that can be used in resources.
	 */
	EXTENSIONDEFINITION("ExtensionDefinition", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>FamilyHistory</b>
	 *
	 * Significant health events and conditions for people related to the subject relevant in the context of care for the subject.
	 */
	FAMILYHISTORY("FamilyHistory", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Goal</b>
	 *
	 * Describes the intended objective(s) of the care.
	 */
	GOAL("Goal", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Group</b>
	 *
	 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized.  I.e. A collection of entities that isn't an Organization.
	 */
	GROUP("Group", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>HealthcareService</b>
	 *
	 * The details of a Healthcare Service available at a location.
	 */
	HEALTHCARESERVICE("HealthcareService", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ImagingObjectSelection</b>
	 *
	 * A set of DICOM SOP Instances of a patient, selected for some application purpose, e.g., quality assurance, teaching, conference, consulting, etc.  Objects selected can be from different studies, but must be of the same patient.
	 */
	IMAGINGOBJECTSELECTION("ImagingObjectSelection", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ImagingStudy</b>
	 *
	 * Representation of the content produced in a DICOM imaging study. A study comprises a set of Series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A Series is of only one modality (e.g., X-ray, CT, MR, ultrasound), but a Study may have multiple Series of different modalities.
	 */
	IMAGINGSTUDY("ImagingStudy", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Immunization</b>
	 *
	 * Immunization event information.
	 */
	IMMUNIZATION("Immunization", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ImmunizationRecommendation</b>
	 *
	 * A patient's point-of-time immunization status and recommendation with optional supporting justification.
	 */
	IMMUNIZATIONRECOMMENDATION("ImmunizationRecommendation", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>InstitutionalClaim</b>
	 *
	 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
	 */
	INSTITUTIONALCLAIM("InstitutionalClaim", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>List</b>
	 *
	 * A set of information summarized from a list of other resources.
	 */
	LIST("List", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Location</b>
	 *
	 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
	 */
	LOCATION("Location", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Media</b>
	 *
	 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
	 */
	MEDIA("Media", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Medication</b>
	 *
	 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging.
	 */
	MEDICATION("Medication", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>MedicationAdministration</b>
	 *
	 * Describes the event of a patient being given a dose of a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
	 */
	MEDICATIONADMINISTRATION("MedicationAdministration", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>MedicationDispense</b>
	 *
	 * Dispensing a medication to a named patient.  This includes a description of the supply provided and the instructions for administering the medication.
	 */
	MEDICATIONDISPENSE("MedicationDispense", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>MedicationPrescription</b>
	 *
	 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
	 */
	MEDICATIONPRESCRIPTION("MedicationPrescription", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>MedicationStatement</b>
	 *
	 * A record of medication being taken by a patient, or that the medication has been given to a patient where the record is the result of a report from the patient or another clinician.
	 */
	MEDICATIONSTATEMENT("MedicationStatement", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>MessageHeader</b>
	 *
	 * The header for a message exchange that is either requesting or responding to an action.  The Reference(s) that are the subject of the action as well as other Information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
	 */
	MESSAGEHEADER("MessageHeader", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>NamingSystem</b>
	 *
	 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
	 */
	NAMINGSYSTEM("NamingSystem", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>NutritionOrder</b>
	 *
	 * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
	 */
	NUTRITIONORDER("NutritionOrder", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Observation</b>
	 *
	 * Measurements and simple assertions made about a patient, device or other subject.
	 */
	OBSERVATION("Observation", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>OperationDefinition</b>
	 *
	 * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
	 */
	OPERATIONDEFINITION("OperationDefinition", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>OperationOutcome</b>
	 *
	 * A collection of error, warning or information messages that result from a system action.
	 */
	OPERATIONOUTCOME("OperationOutcome", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>OralHealthClaim</b>
	 *
	 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
	 */
	ORALHEALTHCLAIM("OralHealthClaim", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Order</b>
	 *
	 * A request to perform an action.
	 */
	ORDER("Order", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>OrderResponse</b>
	 *
	 * A response to an order.
	 */
	ORDERRESPONSE("OrderResponse", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Organization</b>
	 *
	 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
	 */
	ORGANIZATION("Organization", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Other</b>
	 *
	 * Other is a conformant for handling resource concepts not yet defined for FHIR or outside HL7's scope of interest.
	 */
	OTHER("Other", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Patient</b>
	 *
	 * Demographics and other administrative information about a person or animal receiving care or other health-related services.
	 */
	PATIENT("Patient", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>PaymentNotice</b>
	 *
	 * This resource provides the status of the payment for goods and services rendered, and the request and response resource references.
	 */
	PAYMENTNOTICE("PaymentNotice", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>PaymentReconciliation</b>
	 *
	 * This resource provides payment details and claim references supporting a bulk payment.
	 */
	PAYMENTRECONCILIATION("PaymentReconciliation", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>PendedRequest</b>
	 *
	 * This resource provides the request and response details for the resource for which the status is to be checked.
	 */
	PENDEDREQUEST("PendedRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Person</b>
	 *
	 * Demographics and administrative information about a person independent of a specific health-related context.
	 */
	PERSON("Person", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>PharmacyClaim</b>
	 *
	 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
	 */
	PHARMACYCLAIM("PharmacyClaim", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Practitioner</b>
	 *
	 * A person who is directly or indirectly involved in the provisioning of healthcare.
	 */
	PRACTITIONER("Practitioner", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Procedure</b>
	 *
	 * An action that is performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy.
	 */
	PROCEDURE("Procedure", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ProcedureRequest</b>
	 *
	 * A request for a procedure to be performed. May be a proposal or an order.
	 */
	PROCEDUREREQUEST("ProcedureRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ProfessionalClaim</b>
	 *
	 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
	 */
	PROFESSIONALCLAIM("ProfessionalClaim", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Profile</b>
	 *
	 * A Resource Profile - a statement of use of one or more FHIR Resources.  It may include constraints on Resources and Data Types, Terminology Binding Statements and Extension Definitions.
	 */
	PROFILE("Profile", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Provenance</b>
	 *
	 * Provenance information that describes the activity that led to the creation of a set of resources. This information can be used to help determine their reliability or trace where the information in them came from. The focus of the provenance resource is record keeping, audit and traceability, and not explicit statements of clinical significance.
	 */
	PROVENANCE("Provenance", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Questionnaire</b>
	 *
	 * A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
	 */
	QUESTIONNAIRE("Questionnaire", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>QuestionnaireAnswers</b>
	 *
	 * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
	 */
	QUESTIONNAIREANSWERS("QuestionnaireAnswers", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Readjudicate</b>
	 *
	 * This resource provides the request and line items details for the claim which is to be re-adjudicated.
	 */
	READJUDICATE("Readjudicate", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ReferralRequest</b>
	 *
	 * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organisation.
	 */
	REFERRALREQUEST("ReferralRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>RelatedPerson</b>
	 *
	 * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
	 */
	RELATEDPERSON("RelatedPerson", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Reversal</b>
	 *
	 * This resource provides the request and response details for the request for which all actions are to be reversed or terminated.
	 */
	REVERSAL("Reversal", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>RiskAssessment</b>
	 *
	 * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
	 */
	RISKASSESSMENT("RiskAssessment", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Schedule</b>
	 *
	 * A container for slot(s) of time that may be available for booking appointments.
	 */
	SCHEDULE("Schedule", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>SearchParameter</b>
	 *
	 * A Search Parameter that defines a named search item that can be used to search/filter on a resource.
	 */
	SEARCHPARAMETER("SearchParameter", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>SecurityEvent</b>
	 *
	 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
	 */
	SECURITYEVENT("SecurityEvent", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Slot</b>
	 *
	 * A slot of time on a schedule that may be available for booking appointments.
	 */
	SLOT("Slot", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Specimen</b>
	 *
	 * Sample for analysis.
	 */
	SPECIMEN("Specimen", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>StatusRequest</b>
	 *
	 * This resource provides the request and response details for the resource for which the processing status is to be checked.
	 */
	STATUSREQUEST("StatusRequest", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>StatusResponse</b>
	 *
	 * This resource provides processing status, errors and notes from the processing of a resource.
	 */
	STATUSRESPONSE("StatusResponse", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Subscription</b>
	 *
	 * Todo.
	 */
	SUBSCRIPTION("Subscription", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Substance</b>
	 *
	 * A homogeneous material with a definite composition.
	 */
	SUBSTANCE("Substance", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>Supply</b>
	 *
	 * A supply - a  request for something, and provision of what is supplied.
	 */
	SUPPLY("Supply", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>SupportingDocumentation</b>
	 *
	 * This resource provides the supporting information for a process, for example clinical or financial  information related to a claim or pre-authorization.
	 */
	SUPPORTINGDOCUMENTATION("SupportingDocumentation", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>ValueSet</b>
	 *
	 * A value set specifies a set of codes drawn from one or more code systems.
	 */
	VALUESET("ValueSet", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>VisionClaim</b>
	 *
	 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
	 */
	VISIONCLAIM("VisionClaim", "http://hl7.org/fhir/resource-types"),
	
	/**
	 * Code Value: <b>VisionPrescription</b>
	 *
	 * An authorization for the supply of glasses and/or contact lenses to a patient.
	 */
	VISIONPRESCRIPTION("VisionPrescription", "http://hl7.org/fhir/resource-types"),
	
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
	private static Map<String, Map<String, ResourceTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ResourceTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ResourceTypeEnum next : ResourceTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ResourceTypeEnum>());
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
		public String toSystemString(ResourceTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ResourceTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ResourceTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ResourceTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ResourceTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
