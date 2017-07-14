package org.hl7.fhir.dstu3.model;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
 */
@ResourceDef(name="ActivityDefinition", profile="http://hl7.org/fhir/Profile/ActivityDefinition")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "status", "experimental", "date", "publisher", "description", "purpose", "usage", "approvalDate", "lastReviewDate", "effectivePeriod", "useContext", "jurisdiction", "topic", "contributor", "contact", "copyright", "relatedArtifact", "library", "kind", "code", "timing[x]", "location", "participant", "product[x]", "quantity", "dosage", "bodySite", "transform", "dynamicValue"})
public class ActivityDefinition extends MetadataResource {

    public enum ActivityDefinitionKind {
        /**
         * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.
         */
        ACCOUNT, 
        /**
         * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
         */
        ACTIVITYDEFINITION, 
        /**
         * Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.
         */
        ADVERSEEVENT, 
        /**
         * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
         */
        ALLERGYINTOLERANCE, 
        /**
         * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT, 
        /**
         * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENTRESPONSE, 
        /**
         * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
         */
        AUDITEVENT, 
        /**
         * Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.
         */
        BASIC, 
        /**
         * A binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY, 
        /**
         * Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSITE, 
        /**
         * A container for a collection of resources.
         */
        BUNDLE, 
        /**
         * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        CAPABILITYSTATEMENT, 
        /**
         * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CAREPLAN, 
        /**
         * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.
         */
        CARETEAM, 
        /**
         * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.
         */
        CHARGEITEM, 
        /**
         * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
         */
        CLAIM, 
        /**
         * This resource provides the adjudication details from the processing of a Claim resource.
         */
        CLAIMRESPONSE, 
        /**
         * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools such as Apgar score.
         */
        CLINICALIMPRESSION, 
        /**
         * A code system resource specifies a set of codes drawn from one or more code systems.
         */
        CODESYSTEM, 
        /**
         * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
         */
        COMMUNICATION, 
        /**
         * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATIONREQUEST, 
        /**
         * A compartment definition that defines how resources are accessed on a server.
         */
        COMPARTMENTDEFINITION, 
        /**
         * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.
         */
        COMPOSITION, 
        /**
         * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.
         */
        CONCEPTMAP, 
        /**
         * A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.
         */
        CONDITION, 
        /**
         * A record of a healthcare consumer’s policy choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
         */
        CONSENT, 
        /**
         * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to reimburse or pay for health care products and services.
         */
        COVERAGE, 
        /**
         * The formal description of a single piece of information that can be gathered and reported.
         */
        DATAELEMENT, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies an instance or a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.
         */
        DEVICE, 
        /**
         * The characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICECOMPONENT, 
        /**
         * Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICEMETRIC, 
        /**
         * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
         */
        DEVICEREQUEST, 
        /**
         * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
         */
        DEVICEUSESTATEMENT, 
        /**
         * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A collection of documents compiled for a purpose together with metadata that applies to the collection.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document.
         */
        DOCUMENTREFERENCE, 
        /**
         * A resource that includes narrative, extensions, and contained resources.
         */
        DOMAINRESOURCE, 
        /**
         * The EligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an EligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
         */
        ELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an Eligibility resource.
         */
        ELIGIBILITYRESPONSE, 
        /**
         * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
         */
        ENCOUNTER, 
        /**
         * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
         */
        ENDPOINT, 
        /**
         * This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
         */
        ENROLLMENTREQUEST, 
        /**
         * This resource provides enrollment and plan details from the processing of an Enrollment resource.
         */
        ENROLLMENTRESPONSE, 
        /**
         * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
         */
        EPISODEOFCARE, 
        /**
         * Resource to define constraints on the Expansion of a FHIR ValueSet.
         */
        EXPANSIONPROFILE, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.
         */
        FAMILYMEMBERHISTORY, 
        /**
         * Prospective warnings of potential issues when providing care to the patient.
         */
        FLAG, 
        /**
         * Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
         */
        GOAL, 
        /**
         * A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.
         */
        GRAPHDEFINITION, 
        /**
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
         */
        GROUP, 
        /**
         * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
         */
        GUIDANCERESPONSE, 
        /**
         * The details of a healthcare service available at a location.
         */
        HEALTHCARESERVICE, 
        /**
         * A text description of the DICOM SOP instances selected in the ImagingManifest; or the reason for, or significance of, the selection.
         */
        IMAGINGMANIFEST, 
        /**
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.
         */
        IMMUNIZATION, 
        /**
         * A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.
         */
        LIBRARY, 
        /**
         * Identifies two or more records (resource instances) that are referring to the same real-world "occurrence".
         */
        LINKAGE, 
        /**
         * A set of information summarized from a list of other resources.
         */
        LIST, 
        /**
         * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
         */
        LOCATION, 
        /**
         * The Measure resource provides the definition of a quality measure.
         */
        MEASURE, 
        /**
         * The MeasureReport resource contains the results of evaluating a measure.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.
         */
        MEDICATION, 
        /**
         * Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
         */
        MEDICATIONADMINISTRATION, 
        /**
         * Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.
         */
        MEDICATIONDISPENSE, 
        /**
         * An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.
         */
        MEDICATIONREQUEST, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.
         */
        MESSAGEDEFINITION, 
        /**
         * The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
         */
        MESSAGEHEADER, 
        /**
         * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
         */
        NAMINGSYSTEM, 
        /**
         * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
         */
        NUTRITIONORDER, 
        /**
         * Measurements and simple assertions made about a patient, device or other subject.
         */
        OBSERVATION, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
         */
        ORGANIZATION, 
        /**
         * This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.
         */
        PARAMETERS, 
        /**
         * Demographics and other administrative information about an individual or animal receiving care or other health-related services.
         */
        PATIENT, 
        /**
         * This resource provides the status of the payment for goods and services rendered, and the request and response resource references.
         */
        PAYMENTNOTICE, 
        /**
         * This resource provides payment details and claim references supporting a bulk payment.
         */
        PAYMENTRECONCILIATION, 
        /**
         * Demographics and administrative information about a person independent of a specific health-related context.
         */
        PERSON, 
        /**
         * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.
         */
        PLANDEFINITION, 
        /**
         * A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.
         */
        PRACTITIONERROLE, 
        /**
         * An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.
         */
        PROCEDURE, 
        /**
         * A record of a request for diagnostic investigations, treatments, or operations to be performed.
         */
        PROCEDUREREQUEST, 
        /**
         * This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.
         */
        PROCESSREQUEST, 
        /**
         * This resource provides processing status, errors and notes from the processing of a resource.
         */
        PROCESSRESPONSE, 
        /**
         * Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.
         */
        PROVENANCE, 
        /**
         * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
         */
        QUESTIONNAIRE, 
        /**
         * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.
         */
        REFERRALREQUEST, 
        /**
         * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give this medication after that one".
         */
        REQUESTGROUP, 
        /**
         * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
         */
        RESEARCHSTUDY, 
        /**
         * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
         */
        RESEARCHSUBJECT, 
        /**
         * This is the base resource type for everything.
         */
        RESOURCE, 
        /**
         * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISKASSESSMENT, 
        /**
         * A container for slots of time that may be available for booking appointments.
         */
        SCHEDULE, 
        /**
         * A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCHPARAMETER, 
        /**
         * Raw data describing a biological sequence.
         */
        SEQUENCE, 
        /**
         * The ServiceDefinition describes a unit of decision support functionality that is made available as a service, such as immunization modules or drug-drug interaction checking.
         */
        SERVICEDEFINITION, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system is able to take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Record of delivery of what is supplied.
         */
        SUPPLYDELIVERY, 
        /**
         * A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLYREQUEST, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * A summary of information based on the results of executing a TestScript.
         */
        TESTREPORT, 
        /**
         * A structured set of tests against a FHIR server implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * A value set specifies a set of codes drawn from one or more code systems.
         */
        VALUESET, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActivityDefinitionKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Account".equals(codeString))
          return ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return ADVERSEEVENT;
        if ("AllergyIntolerance".equals(codeString))
          return ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return AUDITEVENT;
        if ("Basic".equals(codeString))
          return BASIC;
        if ("Binary".equals(codeString))
          return BINARY;
        if ("BodySite".equals(codeString))
          return BODYSITE;
        if ("Bundle".equals(codeString))
          return BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("CareTeam".equals(codeString))
          return CARETEAM;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return CODESYSTEM;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Consent".equals(codeString))
          return CONSENT;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("DataElement".equals(codeString))
          return DATAELEMENT;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceComponent".equals(codeString))
          return DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("ExpansionProfile".equals(codeString))
          return EXPANSIONPROFILE;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FLAG;
        if ("Goal".equals(codeString))
          return GOAL;
        if ("GraphDefinition".equals(codeString))
          return GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return GROUP;
        if ("GuidanceResponse".equals(codeString))
          return GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return HEALTHCARESERVICE;
        if ("ImagingManifest".equals(codeString))
          return IMAGINGMANIFEST;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("Library".equals(codeString))
          return LIBRARY;
        if ("Linkage".equals(codeString))
          return LINKAGE;
        if ("List".equals(codeString))
          return LIST;
        if ("Location".equals(codeString))
          return LOCATION;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("MeasureReport".equals(codeString))
          return MEASUREREPORT;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("Medication".equals(codeString))
          return MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MessageDefinition".equals(codeString))
          return MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("Parameters".equals(codeString))
          return PARAMETERS;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("PaymentNotice".equals(codeString))
          return PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return PERSON;
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
        if ("ProcedureRequest".equals(codeString))
          return PROCEDUREREQUEST;
        if ("ProcessRequest".equals(codeString))
          return PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return QUESTIONNAIRERESPONSE;
        if ("ReferralRequest".equals(codeString))
          return REFERRALREQUEST;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return SEQUENCE;
        if ("ServiceDefinition".equals(codeString))
          return SERVICEDEFINITION;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("TestReport".equals(codeString))
          return TESTREPORT;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActivityDefinitionKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BODYSITE: return "BodySite";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CHARGEITEM: return "ChargeItem";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case DATAELEMENT: return "DataElement";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICECOMPONENT: return "DeviceComponent";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ELIGIBILITYREQUEST: return "EligibilityRequest";
            case ELIGIBILITYRESPONSE: return "EligibilityResponse";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPANSIONPROFILE: return "ExpansionProfile";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGMANIFEST: return "ImagingManifest";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCEDUREREQUEST: return "ProcedureRequest";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case REFERRALREQUEST: return "ReferralRequest";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEDEFINITION: return "ServiceDefinition";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case VALUESET: return "ValueSet";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACCOUNT: return "http://hl7.org/fhir/resource-types";
            case ACTIVITYDEFINITION: return "http://hl7.org/fhir/resource-types";
            case ADVERSEEVENT: return "http://hl7.org/fhir/resource-types";
            case ALLERGYINTOLERANCE: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENT: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case AUDITEVENT: return "http://hl7.org/fhir/resource-types";
            case BASIC: return "http://hl7.org/fhir/resource-types";
            case BINARY: return "http://hl7.org/fhir/resource-types";
            case BODYSITE: return "http://hl7.org/fhir/resource-types";
            case BUNDLE: return "http://hl7.org/fhir/resource-types";
            case CAPABILITYSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/resource-types";
            case CARETEAM: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEM: return "http://hl7.org/fhir/resource-types";
            case CLAIM: return "http://hl7.org/fhir/resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/resource-types";
            case CODESYSTEM: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case COMPARTMENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/resource-types";
            case CONDITION: return "http://hl7.org/fhir/resource-types";
            case CONSENT: return "http://hl7.org/fhir/resource-types";
            case CONTRACT: return "http://hl7.org/fhir/resource-types";
            case COVERAGE: return "http://hl7.org/fhir/resource-types";
            case DATAELEMENT: return "http://hl7.org/fhir/resource-types";
            case DETECTEDISSUE: return "http://hl7.org/fhir/resource-types";
            case DEVICE: return "http://hl7.org/fhir/resource-types";
            case DEVICECOMPONENT: return "http://hl7.org/fhir/resource-types";
            case DEVICEMETRIC: return "http://hl7.org/fhir/resource-types";
            case DEVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/resource-types";
            case DOMAINRESOURCE: return "http://hl7.org/fhir/resource-types";
            case ELIGIBILITYREQUEST: return "http://hl7.org/fhir/resource-types";
            case ELIGIBILITYRESPONSE: return "http://hl7.org/fhir/resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/resource-types";
            case ENDPOINT: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EXPANSIONPROFILE: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GRAPHDEFINITION: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case GUIDANCERESPONSE: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGMANIFEST: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case LIBRARY: return "http://hl7.org/fhir/resource-types";
            case LINKAGE: return "http://hl7.org/fhir/resource-types";
            case LIST: return "http://hl7.org/fhir/resource-types";
            case LOCATION: return "http://hl7.org/fhir/resource-types";
            case MEASURE: return "http://hl7.org/fhir/resource-types";
            case MEASUREREPORT: return "http://hl7.org/fhir/resource-types";
            case MEDIA: return "http://hl7.org/fhir/resource-types";
            case MEDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MESSAGEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONERROLE: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCEDUREREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case REFERRALREQUEST: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case REQUESTGROUP: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSTUDY: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSUBJECT: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SEQUENCE: return "http://hl7.org/fhir/resource-types";
            case SERVICEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREMAP: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TASK: return "http://hl7.org/fhir/resource-types";
            case TESTREPORT: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACCOUNT: return "A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.";
            case ACTIVITYDEFINITION: return "This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.";
            case ADVERSEEVENT: return "Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.";
            case ALLERGYINTOLERANCE: return "Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.";
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case AUDITEVENT: return "A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.";
            case BASIC: return "Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.";
            case BINARY: return "A binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BODYSITE: return "Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAPABILITYSTATEMENT: return "A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CARETEAM: return "The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.";
            case CHARGEITEM: return "The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case CODESYSTEM: return "A code system resource specifies a set of codes drawn from one or more code systems.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPARTMENTDEFINITION: return "A compartment definition that defines how resources are accessed on a server.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.";
            case CONDITION: return "A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.";
            case CONSENT: return "A record of a healthcare consumer’s policy choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.";
            case CONTRACT: return "A formal agreement between parties regarding the conduct of business, exchange of information or other matters.";
            case COVERAGE: return "Financial instrument which may be used to reimburse or pay for health care products and services.";
            case DATAELEMENT: return "The formal description of a single piece of information that can be gathered and reported.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies an instance or a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.";
            case DEVICECOMPONENT: return "The characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A collection of documents compiled for a purpose together with metadata that applies to the collection.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case DOMAINRESOURCE: return "A resource that includes narrative, extensions, and contained resources.";
            case ELIGIBILITYREQUEST: return "The EligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an EligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.";
            case ELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an Eligibility resource.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENDPOINT: return "The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EXPANSIONPROFILE: return "Resource to define constraints on the Expansion of a FHIR ValueSet.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GRAPHDEFINITION: return "A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case GUIDANCERESPONSE: return "A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGMANIFEST: return "A text description of the DICOM SOP instances selected in the ImagingManifest; or the reason for, or significance of, the selection.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.";
            case LIBRARY: return "The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.";
            case LINKAGE: return "Identifies two or more records (resource instances) that are referring to the same real-world \"occurrence\".";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.";
            case MEASURE: return "The Measure resource provides the definition of a quality measure.";
            case MEASUREREPORT: return "The MeasureReport resource contains the results of evaluating a measure.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONREQUEST: return "An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationRequest\" rather than \"MedicationPrescription\" or \"MedicationOrder\" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MESSAGEDEFINITION: return "Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PRACTITIONERROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PROCEDURE: return "An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.";
            case PROCEDUREREQUEST: return "A record of a request for diagnostic investigations, treatments, or operations to be performed.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.";
            case REFERRALREQUEST: return "Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case REQUESTGROUP: return "A group of related requests that can be used to capture intended activities that have inter-dependencies such as \"give this medication after that one\".";
            case RESEARCHSTUDY: return "A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.";
            case RESEARCHSUBJECT: return "A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.";
            case RESOURCE: return "This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slots of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SEQUENCE: return "Raw data describing a biological sequence.";
            case SERVICEDEFINITION: return "The ServiceDefinition describes a unit of decision support functionality that is made available as a service, such as immunization modules or drug-drug interaction checking.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system is able to take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TASK: return "A task to be performed.";
            case TESTREPORT: return "A summary of information based on the results of executing a TestScript.";
            case TESTSCRIPT: return "A structured set of tests against a FHIR server implementation to determine compliance against the FHIR specification.";
            case VALUESET: return "A value set specifies a set of codes drawn from one or more code systems.";
            case VISIONPRESCRIPTION: return "An authorization for the supply of glasses and/or contact lenses to a patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BODYSITE: return "BodySite";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CHARGEITEM: return "ChargeItem";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case DATAELEMENT: return "DataElement";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICECOMPONENT: return "DeviceComponent";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ELIGIBILITYREQUEST: return "EligibilityRequest";
            case ELIGIBILITYRESPONSE: return "EligibilityResponse";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPANSIONPROFILE: return "ExpansionProfile";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGMANIFEST: return "ImagingManifest";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCEDUREREQUEST: return "ProcedureRequest";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case REFERRALREQUEST: return "ReferralRequest";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEDEFINITION: return "ServiceDefinition";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case VALUESET: return "ValueSet";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
    }

  public static class ActivityDefinitionKindEnumFactory implements EnumFactory<ActivityDefinitionKind> {
    public ActivityDefinitionKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Account".equals(codeString))
          return ActivityDefinitionKind.ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return ActivityDefinitionKind.ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return ActivityDefinitionKind.ADVERSEEVENT;
        if ("AllergyIntolerance".equals(codeString))
          return ActivityDefinitionKind.ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return ActivityDefinitionKind.APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return ActivityDefinitionKind.APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return ActivityDefinitionKind.AUDITEVENT;
        if ("Basic".equals(codeString))
          return ActivityDefinitionKind.BASIC;
        if ("Binary".equals(codeString))
          return ActivityDefinitionKind.BINARY;
        if ("BodySite".equals(codeString))
          return ActivityDefinitionKind.BODYSITE;
        if ("Bundle".equals(codeString))
          return ActivityDefinitionKind.BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return ActivityDefinitionKind.CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return ActivityDefinitionKind.CAREPLAN;
        if ("CareTeam".equals(codeString))
          return ActivityDefinitionKind.CARETEAM;
        if ("ChargeItem".equals(codeString))
          return ActivityDefinitionKind.CHARGEITEM;
        if ("Claim".equals(codeString))
          return ActivityDefinitionKind.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return ActivityDefinitionKind.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return ActivityDefinitionKind.CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return ActivityDefinitionKind.CODESYSTEM;
        if ("Communication".equals(codeString))
          return ActivityDefinitionKind.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return ActivityDefinitionKind.COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return ActivityDefinitionKind.COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return ActivityDefinitionKind.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return ActivityDefinitionKind.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return ActivityDefinitionKind.CONDITION;
        if ("Consent".equals(codeString))
          return ActivityDefinitionKind.CONSENT;
        if ("Contract".equals(codeString))
          return ActivityDefinitionKind.CONTRACT;
        if ("Coverage".equals(codeString))
          return ActivityDefinitionKind.COVERAGE;
        if ("DataElement".equals(codeString))
          return ActivityDefinitionKind.DATAELEMENT;
        if ("DetectedIssue".equals(codeString))
          return ActivityDefinitionKind.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return ActivityDefinitionKind.DEVICE;
        if ("DeviceComponent".equals(codeString))
          return ActivityDefinitionKind.DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return ActivityDefinitionKind.DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return ActivityDefinitionKind.DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return ActivityDefinitionKind.DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return ActivityDefinitionKind.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return ActivityDefinitionKind.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return ActivityDefinitionKind.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return ActivityDefinitionKind.DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return ActivityDefinitionKind.ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return ActivityDefinitionKind.ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return ActivityDefinitionKind.ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return ActivityDefinitionKind.ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return ActivityDefinitionKind.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ActivityDefinitionKind.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return ActivityDefinitionKind.EPISODEOFCARE;
        if ("ExpansionProfile".equals(codeString))
          return ActivityDefinitionKind.EXPANSIONPROFILE;
        if ("ExplanationOfBenefit".equals(codeString))
          return ActivityDefinitionKind.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return ActivityDefinitionKind.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return ActivityDefinitionKind.FLAG;
        if ("Goal".equals(codeString))
          return ActivityDefinitionKind.GOAL;
        if ("GraphDefinition".equals(codeString))
          return ActivityDefinitionKind.GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return ActivityDefinitionKind.GROUP;
        if ("GuidanceResponse".equals(codeString))
          return ActivityDefinitionKind.GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return ActivityDefinitionKind.HEALTHCARESERVICE;
        if ("ImagingManifest".equals(codeString))
          return ActivityDefinitionKind.IMAGINGMANIFEST;
        if ("ImagingStudy".equals(codeString))
          return ActivityDefinitionKind.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return ActivityDefinitionKind.IMMUNIZATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return ActivityDefinitionKind.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return ActivityDefinitionKind.IMPLEMENTATIONGUIDE;
        if ("Library".equals(codeString))
          return ActivityDefinitionKind.LIBRARY;
        if ("Linkage".equals(codeString))
          return ActivityDefinitionKind.LINKAGE;
        if ("List".equals(codeString))
          return ActivityDefinitionKind.LIST;
        if ("Location".equals(codeString))
          return ActivityDefinitionKind.LOCATION;
        if ("Measure".equals(codeString))
          return ActivityDefinitionKind.MEASURE;
        if ("MeasureReport".equals(codeString))
          return ActivityDefinitionKind.MEASUREREPORT;
        if ("Media".equals(codeString))
          return ActivityDefinitionKind.MEDIA;
        if ("Medication".equals(codeString))
          return ActivityDefinitionKind.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return ActivityDefinitionKind.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return ActivityDefinitionKind.MEDICATIONDISPENSE;
        if ("MedicationRequest".equals(codeString))
          return ActivityDefinitionKind.MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return ActivityDefinitionKind.MEDICATIONSTATEMENT;
        if ("MessageDefinition".equals(codeString))
          return ActivityDefinitionKind.MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return ActivityDefinitionKind.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return ActivityDefinitionKind.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return ActivityDefinitionKind.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return ActivityDefinitionKind.OBSERVATION;
        if ("OperationDefinition".equals(codeString))
          return ActivityDefinitionKind.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return ActivityDefinitionKind.OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ActivityDefinitionKind.ORGANIZATION;
        if ("Parameters".equals(codeString))
          return ActivityDefinitionKind.PARAMETERS;
        if ("Patient".equals(codeString))
          return ActivityDefinitionKind.PATIENT;
        if ("PaymentNotice".equals(codeString))
          return ActivityDefinitionKind.PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return ActivityDefinitionKind.PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return ActivityDefinitionKind.PERSON;
        if ("PlanDefinition".equals(codeString))
          return ActivityDefinitionKind.PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return ActivityDefinitionKind.PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return ActivityDefinitionKind.PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return ActivityDefinitionKind.PROCEDURE;
        if ("ProcedureRequest".equals(codeString))
          return ActivityDefinitionKind.PROCEDUREREQUEST;
        if ("ProcessRequest".equals(codeString))
          return ActivityDefinitionKind.PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return ActivityDefinitionKind.PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return ActivityDefinitionKind.PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return ActivityDefinitionKind.QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return ActivityDefinitionKind.QUESTIONNAIRERESPONSE;
        if ("ReferralRequest".equals(codeString))
          return ActivityDefinitionKind.REFERRALREQUEST;
        if ("RelatedPerson".equals(codeString))
          return ActivityDefinitionKind.RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return ActivityDefinitionKind.REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return ActivityDefinitionKind.RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return ActivityDefinitionKind.RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return ActivityDefinitionKind.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return ActivityDefinitionKind.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return ActivityDefinitionKind.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return ActivityDefinitionKind.SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return ActivityDefinitionKind.SEQUENCE;
        if ("ServiceDefinition".equals(codeString))
          return ActivityDefinitionKind.SERVICEDEFINITION;
        if ("Slot".equals(codeString))
          return ActivityDefinitionKind.SLOT;
        if ("Specimen".equals(codeString))
          return ActivityDefinitionKind.SPECIMEN;
        if ("StructureDefinition".equals(codeString))
          return ActivityDefinitionKind.STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return ActivityDefinitionKind.STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return ActivityDefinitionKind.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return ActivityDefinitionKind.SUBSTANCE;
        if ("SupplyDelivery".equals(codeString))
          return ActivityDefinitionKind.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return ActivityDefinitionKind.SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return ActivityDefinitionKind.TASK;
        if ("TestReport".equals(codeString))
          return ActivityDefinitionKind.TESTREPORT;
        if ("TestScript".equals(codeString))
          return ActivityDefinitionKind.TESTSCRIPT;
        if ("ValueSet".equals(codeString))
          return ActivityDefinitionKind.VALUESET;
        if ("VisionPrescription".equals(codeString))
          return ActivityDefinitionKind.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown ActivityDefinitionKind code '"+codeString+"'");
        }
        public Enumeration<ActivityDefinitionKind> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActivityDefinitionKind>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Account".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ACCOUNT);
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ACTIVITYDEFINITION);
        if ("AdverseEvent".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ADVERSEEVENT);
        if ("AllergyIntolerance".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ALLERGYINTOLERANCE);
        if ("Appointment".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.APPOINTMENT);
        if ("AppointmentResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.APPOINTMENTRESPONSE);
        if ("AuditEvent".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.AUDITEVENT);
        if ("Basic".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.BASIC);
        if ("Binary".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.BINARY);
        if ("BodySite".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.BODYSITE);
        if ("Bundle".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.BUNDLE);
        if ("CapabilityStatement".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CAPABILITYSTATEMENT);
        if ("CarePlan".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CAREPLAN);
        if ("CareTeam".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CARETEAM);
        if ("ChargeItem".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CHARGEITEM);
        if ("Claim".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CLAIM);
        if ("ClaimResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CLAIMRESPONSE);
        if ("ClinicalImpression".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CLINICALIMPRESSION);
        if ("CodeSystem".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CODESYSTEM);
        if ("Communication".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.COMMUNICATION);
        if ("CommunicationRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.COMMUNICATIONREQUEST);
        if ("CompartmentDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.COMPARTMENTDEFINITION);
        if ("Composition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.COMPOSITION);
        if ("ConceptMap".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CONCEPTMAP);
        if ("Condition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CONDITION);
        if ("Consent".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CONSENT);
        if ("Contract".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.CONTRACT);
        if ("Coverage".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.COVERAGE);
        if ("DataElement".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DATAELEMENT);
        if ("DetectedIssue".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DETECTEDISSUE);
        if ("Device".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DEVICE);
        if ("DeviceComponent".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DEVICECOMPONENT);
        if ("DeviceMetric".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DEVICEMETRIC);
        if ("DeviceRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DEVICEREQUEST);
        if ("DeviceUseStatement".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DEVICEUSESTATEMENT);
        if ("DiagnosticReport".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DIAGNOSTICREPORT);
        if ("DocumentManifest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DOCUMENTMANIFEST);
        if ("DocumentReference".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DOCUMENTREFERENCE);
        if ("DomainResource".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.DOMAINRESOURCE);
        if ("EligibilityRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ELIGIBILITYREQUEST);
        if ("EligibilityResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ELIGIBILITYRESPONSE);
        if ("Encounter".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ENCOUNTER);
        if ("Endpoint".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ENDPOINT);
        if ("EnrollmentRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ENROLLMENTREQUEST);
        if ("EnrollmentResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ENROLLMENTRESPONSE);
        if ("EpisodeOfCare".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.EPISODEOFCARE);
        if ("ExpansionProfile".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.EXPANSIONPROFILE);
        if ("ExplanationOfBenefit".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.EXPLANATIONOFBENEFIT);
        if ("FamilyMemberHistory".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.FAMILYMEMBERHISTORY);
        if ("Flag".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.FLAG);
        if ("Goal".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.GOAL);
        if ("GraphDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.GRAPHDEFINITION);
        if ("Group".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.GROUP);
        if ("GuidanceResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.GUIDANCERESPONSE);
        if ("HealthcareService".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.HEALTHCARESERVICE);
        if ("ImagingManifest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.IMAGINGMANIFEST);
        if ("ImagingStudy".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.IMAGINGSTUDY);
        if ("Immunization".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.IMMUNIZATION);
        if ("ImmunizationRecommendation".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.IMMUNIZATIONRECOMMENDATION);
        if ("ImplementationGuide".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.IMPLEMENTATIONGUIDE);
        if ("Library".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.LIBRARY);
        if ("Linkage".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.LINKAGE);
        if ("List".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.LIST);
        if ("Location".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.LOCATION);
        if ("Measure".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEASURE);
        if ("MeasureReport".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEASUREREPORT);
        if ("Media".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEDIA);
        if ("Medication".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEDICATION);
        if ("MedicationAdministration".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEDICATIONADMINISTRATION);
        if ("MedicationDispense".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEDICATIONDISPENSE);
        if ("MedicationRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEDICATIONREQUEST);
        if ("MedicationStatement".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MEDICATIONSTATEMENT);
        if ("MessageDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MESSAGEDEFINITION);
        if ("MessageHeader".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.MESSAGEHEADER);
        if ("NamingSystem".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.NAMINGSYSTEM);
        if ("NutritionOrder".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.NUTRITIONORDER);
        if ("Observation".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.OBSERVATION);
        if ("OperationDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.OPERATIONDEFINITION);
        if ("OperationOutcome".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.OPERATIONOUTCOME);
        if ("Organization".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.ORGANIZATION);
        if ("Parameters".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PARAMETERS);
        if ("Patient".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PATIENT);
        if ("PaymentNotice".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PAYMENTNOTICE);
        if ("PaymentReconciliation".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PAYMENTRECONCILIATION);
        if ("Person".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PERSON);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PLANDEFINITION);
        if ("Practitioner".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PRACTITIONER);
        if ("PractitionerRole".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PRACTITIONERROLE);
        if ("Procedure".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PROCEDURE);
        if ("ProcedureRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PROCEDUREREQUEST);
        if ("ProcessRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PROCESSREQUEST);
        if ("ProcessResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PROCESSRESPONSE);
        if ("Provenance".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.PROVENANCE);
        if ("Questionnaire".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.QUESTIONNAIRE);
        if ("QuestionnaireResponse".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.QUESTIONNAIRERESPONSE);
        if ("ReferralRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.REFERRALREQUEST);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.RELATEDPERSON);
        if ("RequestGroup".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.REQUESTGROUP);
        if ("ResearchStudy".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.RESEARCHSTUDY);
        if ("ResearchSubject".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.RESEARCHSUBJECT);
        if ("Resource".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.RESOURCE);
        if ("RiskAssessment".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.RISKASSESSMENT);
        if ("Schedule".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SCHEDULE);
        if ("SearchParameter".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SEARCHPARAMETER);
        if ("Sequence".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SEQUENCE);
        if ("ServiceDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SERVICEDEFINITION);
        if ("Slot".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SLOT);
        if ("Specimen".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SPECIMEN);
        if ("StructureDefinition".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.STRUCTUREDEFINITION);
        if ("StructureMap".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.STRUCTUREMAP);
        if ("Subscription".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SUBSCRIPTION);
        if ("Substance".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SUBSTANCE);
        if ("SupplyDelivery".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SUPPLYDELIVERY);
        if ("SupplyRequest".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.SUPPLYREQUEST);
        if ("Task".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.TASK);
        if ("TestReport".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.TESTREPORT);
        if ("TestScript".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.TESTSCRIPT);
        if ("ValueSet".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.VALUESET);
        if ("VisionPrescription".equals(codeString))
          return new Enumeration<ActivityDefinitionKind>(this, ActivityDefinitionKind.VISIONPRESCRIPTION);
        throw new FHIRException("Unknown ActivityDefinitionKind code '"+codeString+"'");
        }
    public String toCode(ActivityDefinitionKind code) {
      if (code == ActivityDefinitionKind.ACCOUNT)
        return "Account";
      if (code == ActivityDefinitionKind.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == ActivityDefinitionKind.ADVERSEEVENT)
        return "AdverseEvent";
      if (code == ActivityDefinitionKind.ALLERGYINTOLERANCE)
        return "AllergyIntolerance";
      if (code == ActivityDefinitionKind.APPOINTMENT)
        return "Appointment";
      if (code == ActivityDefinitionKind.APPOINTMENTRESPONSE)
        return "AppointmentResponse";
      if (code == ActivityDefinitionKind.AUDITEVENT)
        return "AuditEvent";
      if (code == ActivityDefinitionKind.BASIC)
        return "Basic";
      if (code == ActivityDefinitionKind.BINARY)
        return "Binary";
      if (code == ActivityDefinitionKind.BODYSITE)
        return "BodySite";
      if (code == ActivityDefinitionKind.BUNDLE)
        return "Bundle";
      if (code == ActivityDefinitionKind.CAPABILITYSTATEMENT)
        return "CapabilityStatement";
      if (code == ActivityDefinitionKind.CAREPLAN)
        return "CarePlan";
      if (code == ActivityDefinitionKind.CARETEAM)
        return "CareTeam";
      if (code == ActivityDefinitionKind.CHARGEITEM)
        return "ChargeItem";
      if (code == ActivityDefinitionKind.CLAIM)
        return "Claim";
      if (code == ActivityDefinitionKind.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == ActivityDefinitionKind.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == ActivityDefinitionKind.CODESYSTEM)
        return "CodeSystem";
      if (code == ActivityDefinitionKind.COMMUNICATION)
        return "Communication";
      if (code == ActivityDefinitionKind.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == ActivityDefinitionKind.COMPARTMENTDEFINITION)
        return "CompartmentDefinition";
      if (code == ActivityDefinitionKind.COMPOSITION)
        return "Composition";
      if (code == ActivityDefinitionKind.CONCEPTMAP)
        return "ConceptMap";
      if (code == ActivityDefinitionKind.CONDITION)
        return "Condition";
      if (code == ActivityDefinitionKind.CONSENT)
        return "Consent";
      if (code == ActivityDefinitionKind.CONTRACT)
        return "Contract";
      if (code == ActivityDefinitionKind.COVERAGE)
        return "Coverage";
      if (code == ActivityDefinitionKind.DATAELEMENT)
        return "DataElement";
      if (code == ActivityDefinitionKind.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == ActivityDefinitionKind.DEVICE)
        return "Device";
      if (code == ActivityDefinitionKind.DEVICECOMPONENT)
        return "DeviceComponent";
      if (code == ActivityDefinitionKind.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == ActivityDefinitionKind.DEVICEREQUEST)
        return "DeviceRequest";
      if (code == ActivityDefinitionKind.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == ActivityDefinitionKind.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == ActivityDefinitionKind.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == ActivityDefinitionKind.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == ActivityDefinitionKind.DOMAINRESOURCE)
        return "DomainResource";
      if (code == ActivityDefinitionKind.ELIGIBILITYREQUEST)
        return "EligibilityRequest";
      if (code == ActivityDefinitionKind.ELIGIBILITYRESPONSE)
        return "EligibilityResponse";
      if (code == ActivityDefinitionKind.ENCOUNTER)
        return "Encounter";
      if (code == ActivityDefinitionKind.ENDPOINT)
        return "Endpoint";
      if (code == ActivityDefinitionKind.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == ActivityDefinitionKind.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == ActivityDefinitionKind.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == ActivityDefinitionKind.EXPANSIONPROFILE)
        return "ExpansionProfile";
      if (code == ActivityDefinitionKind.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == ActivityDefinitionKind.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == ActivityDefinitionKind.FLAG)
        return "Flag";
      if (code == ActivityDefinitionKind.GOAL)
        return "Goal";
      if (code == ActivityDefinitionKind.GRAPHDEFINITION)
        return "GraphDefinition";
      if (code == ActivityDefinitionKind.GROUP)
        return "Group";
      if (code == ActivityDefinitionKind.GUIDANCERESPONSE)
        return "GuidanceResponse";
      if (code == ActivityDefinitionKind.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == ActivityDefinitionKind.IMAGINGMANIFEST)
        return "ImagingManifest";
      if (code == ActivityDefinitionKind.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == ActivityDefinitionKind.IMMUNIZATION)
        return "Immunization";
      if (code == ActivityDefinitionKind.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == ActivityDefinitionKind.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == ActivityDefinitionKind.LIBRARY)
        return "Library";
      if (code == ActivityDefinitionKind.LINKAGE)
        return "Linkage";
      if (code == ActivityDefinitionKind.LIST)
        return "List";
      if (code == ActivityDefinitionKind.LOCATION)
        return "Location";
      if (code == ActivityDefinitionKind.MEASURE)
        return "Measure";
      if (code == ActivityDefinitionKind.MEASUREREPORT)
        return "MeasureReport";
      if (code == ActivityDefinitionKind.MEDIA)
        return "Media";
      if (code == ActivityDefinitionKind.MEDICATION)
        return "Medication";
      if (code == ActivityDefinitionKind.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == ActivityDefinitionKind.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == ActivityDefinitionKind.MEDICATIONREQUEST)
        return "MedicationRequest";
      if (code == ActivityDefinitionKind.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == ActivityDefinitionKind.MESSAGEDEFINITION)
        return "MessageDefinition";
      if (code == ActivityDefinitionKind.MESSAGEHEADER)
        return "MessageHeader";
      if (code == ActivityDefinitionKind.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == ActivityDefinitionKind.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == ActivityDefinitionKind.OBSERVATION)
        return "Observation";
      if (code == ActivityDefinitionKind.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == ActivityDefinitionKind.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == ActivityDefinitionKind.ORGANIZATION)
        return "Organization";
      if (code == ActivityDefinitionKind.PARAMETERS)
        return "Parameters";
      if (code == ActivityDefinitionKind.PATIENT)
        return "Patient";
      if (code == ActivityDefinitionKind.PAYMENTNOTICE)
        return "PaymentNotice";
      if (code == ActivityDefinitionKind.PAYMENTRECONCILIATION)
        return "PaymentReconciliation";
      if (code == ActivityDefinitionKind.PERSON)
        return "Person";
      if (code == ActivityDefinitionKind.PLANDEFINITION)
        return "PlanDefinition";
      if (code == ActivityDefinitionKind.PRACTITIONER)
        return "Practitioner";
      if (code == ActivityDefinitionKind.PRACTITIONERROLE)
        return "PractitionerRole";
      if (code == ActivityDefinitionKind.PROCEDURE)
        return "Procedure";
      if (code == ActivityDefinitionKind.PROCEDUREREQUEST)
        return "ProcedureRequest";
      if (code == ActivityDefinitionKind.PROCESSREQUEST)
        return "ProcessRequest";
      if (code == ActivityDefinitionKind.PROCESSRESPONSE)
        return "ProcessResponse";
      if (code == ActivityDefinitionKind.PROVENANCE)
        return "Provenance";
      if (code == ActivityDefinitionKind.QUESTIONNAIRE)
        return "Questionnaire";
      if (code == ActivityDefinitionKind.QUESTIONNAIRERESPONSE)
        return "QuestionnaireResponse";
      if (code == ActivityDefinitionKind.REFERRALREQUEST)
        return "ReferralRequest";
      if (code == ActivityDefinitionKind.RELATEDPERSON)
        return "RelatedPerson";
      if (code == ActivityDefinitionKind.REQUESTGROUP)
        return "RequestGroup";
      if (code == ActivityDefinitionKind.RESEARCHSTUDY)
        return "ResearchStudy";
      if (code == ActivityDefinitionKind.RESEARCHSUBJECT)
        return "ResearchSubject";
      if (code == ActivityDefinitionKind.RESOURCE)
        return "Resource";
      if (code == ActivityDefinitionKind.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == ActivityDefinitionKind.SCHEDULE)
        return "Schedule";
      if (code == ActivityDefinitionKind.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == ActivityDefinitionKind.SEQUENCE)
        return "Sequence";
      if (code == ActivityDefinitionKind.SERVICEDEFINITION)
        return "ServiceDefinition";
      if (code == ActivityDefinitionKind.SLOT)
        return "Slot";
      if (code == ActivityDefinitionKind.SPECIMEN)
        return "Specimen";
      if (code == ActivityDefinitionKind.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == ActivityDefinitionKind.STRUCTUREMAP)
        return "StructureMap";
      if (code == ActivityDefinitionKind.SUBSCRIPTION)
        return "Subscription";
      if (code == ActivityDefinitionKind.SUBSTANCE)
        return "Substance";
      if (code == ActivityDefinitionKind.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == ActivityDefinitionKind.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == ActivityDefinitionKind.TASK)
        return "Task";
      if (code == ActivityDefinitionKind.TESTREPORT)
        return "TestReport";
      if (code == ActivityDefinitionKind.TESTSCRIPT)
        return "TestScript";
      if (code == ActivityDefinitionKind.VALUESET)
        return "ValueSet";
      if (code == ActivityDefinitionKind.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    public String toSystem(ActivityDefinitionKind code) {
      return code.getSystem();
      }
    }

    public enum ActivityParticipantType {
        /**
         * The participant is the patient under evaluation
         */
        PATIENT, 
        /**
         * The participant is a practitioner involved in the patient's care
         */
        PRACTITIONER, 
        /**
         * The participant is a person related to the patient
         */
        RELATEDPERSON, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActivityParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActivityParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PRACTITIONER: return "practitioner";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/action-participant-type";
            case PRACTITIONER: return "http://hl7.org/fhir/action-participant-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/action-participant-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The participant is the patient under evaluation";
            case PRACTITIONER: return "The participant is a practitioner involved in the patient's care";
            case RELATEDPERSON: return "The participant is a person related to the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case PRACTITIONER: return "Practitioner";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
        }
    }

  public static class ActivityParticipantTypeEnumFactory implements EnumFactory<ActivityParticipantType> {
    public ActivityParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return ActivityParticipantType.PATIENT;
        if ("practitioner".equals(codeString))
          return ActivityParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return ActivityParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown ActivityParticipantType code '"+codeString+"'");
        }
        public Enumeration<ActivityParticipantType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActivityParticipantType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<ActivityParticipantType>(this, ActivityParticipantType.PATIENT);
        if ("practitioner".equals(codeString))
          return new Enumeration<ActivityParticipantType>(this, ActivityParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<ActivityParticipantType>(this, ActivityParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown ActivityParticipantType code '"+codeString+"'");
        }
    public String toCode(ActivityParticipantType code) {
      if (code == ActivityParticipantType.PATIENT)
        return "patient";
      if (code == ActivityParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == ActivityParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(ActivityParticipantType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ActivityDefinitionParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of participant in the action.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="patient | practitioner | related-person", formalDefinition="The type of participant in the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-participant-type")
        protected Enumeration<ActivityParticipantType> type;

        /**
         * The role the participant should play in performing the described action.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. Nurse, Surgeon, Parent, etc", formalDefinition="The role the participant should play in performing the described action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-participant-role")
        protected CodeableConcept role;

        private static final long serialVersionUID = -1450932564L;

    /**
     * Constructor
     */
      public ActivityDefinitionParticipantComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActivityDefinitionParticipantComponent(Enumeration<ActivityParticipantType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of participant in the action.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ActivityParticipantType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionParticipantComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ActivityParticipantType>(new ActivityParticipantTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of participant in the action.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ActivityDefinitionParticipantComponent setTypeElement(Enumeration<ActivityParticipantType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of participant in the action.
         */
        public ActivityParticipantType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of participant in the action.
         */
        public ActivityDefinitionParticipantComponent setType(ActivityParticipantType value) { 
            if (this.type == null)
              this.type = new Enumeration<ActivityParticipantType>(new ActivityParticipantTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #role} (The role the participant should play in performing the described action.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (The role the participant should play in performing the described action.)
         */
        public ActivityDefinitionParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of participant in the action.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("role", "CodeableConcept", "The role the participant should play in performing the described action.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ActivityParticipantType>
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new ActivityParticipantTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ActivityParticipantType>
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new ActivityParticipantTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ActivityParticipantType>
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3506294:  return getRole(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.type");
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else
          return super.addChild(name);
      }

      public ActivityDefinitionParticipantComponent copy() {
        ActivityDefinitionParticipantComponent dst = new ActivityDefinitionParticipantComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.role = role == null ? null : role.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActivityDefinitionParticipantComponent))
          return false;
        ActivityDefinitionParticipantComponent o = (ActivityDefinitionParticipantComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActivityDefinitionParticipantComponent))
          return false;
        ActivityDefinitionParticipantComponent o = (ActivityDefinitionParticipantComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, role);
      }

  public String fhirType() {
    return "ActivityDefinition.participant";

  }

  }

    @Block()
    public static class ActivityDefinitionDynamicValueComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A brief, natural language description of the intended semantics of the dynamic value.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the dynamic value", formalDefinition="A brief, natural language description of the intended semantics of the dynamic value." )
        protected StringType description;

        /**
         * The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        @Child(name = "path", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The path to the element to be set dynamically", formalDefinition="The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression." )
        protected StringType path;

        /**
         * The media type of the language for the expression.
         */
        @Child(name = "language", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression." )
        protected StringType language;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="An expression that provides the dynamic value for the customization", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = 448404361L;

    /**
     * Constructor
     */
      public ActivityDefinitionDynamicValueComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief, natural language description of the intended semantics of the dynamic value.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief, natural language description of the intended semantics of the dynamic value.
         */
        public ActivityDefinitionDynamicValueComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public ActivityDefinitionDynamicValueComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public StringType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new StringType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setLanguageElement(StringType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The media type of the language for the expression.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The media type of the language for the expression.
         */
        public ActivityDefinitionDynamicValueComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new StringType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression specifying the value of the customized element.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression specifying the value of the customized element.
         */
        public ActivityDefinitionDynamicValueComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "A brief, natural language description of the intended semantics of the dynamic value.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("path", "string", "The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("language", "string", "The media type of the language for the expression.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("expression", "string", "An expression specifying the value of the customized element.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case 3433509:  return getPathElement();
        case -1613589672:  return getLanguageElement();
        case -1795452264:  return getExpressionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case 3433509: /*path*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.description");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.path");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public ActivityDefinitionDynamicValueComponent copy() {
        ActivityDefinitionDynamicValueComponent dst = new ActivityDefinitionDynamicValueComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.path = path == null ? null : path.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActivityDefinitionDynamicValueComponent))
          return false;
        ActivityDefinitionDynamicValueComponent o = (ActivityDefinitionDynamicValueComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(path, o.path, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActivityDefinitionDynamicValueComponent))
          return false;
        ActivityDefinitionDynamicValueComponent o = (ActivityDefinitionDynamicValueComponent) other;
        return compareValues(description, o.description, true) && compareValues(path, o.path, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, path, language
          , expression);
      }

  public String fhirType() {
    return "ActivityDefinition.dynamicValue";

  }

  }

    /**
     * A formal identifier that is used to identify this activity definition when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the activity definition", formalDefinition="A formal identifier that is used to identify this activity definition when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * Explaination of why this activity definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this activity definition is defined", formalDefinition="Explaination of why this activity definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A detailed description of how the asset is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the asset", formalDefinition="A detailed description of how the asset is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the activity definition was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the activity definition was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the activity definition content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the activity definition is expected to be used", formalDefinition="The period during which the activity definition content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity that can be useful for filtering and searching.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="E.g. Education, Treatment, Assessment, etc", formalDefinition="Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity that can be useful for filtering and searching." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/definition-topic")
    protected List<CodeableConcept> topic;

    /**
     * A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.
     */
    @Child(name = "contributor", type = {Contributor.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A content contributor", formalDefinition="A contributor to the content of the asset, including authors, editors, reviewers, and endorsers." )
    protected List<Contributor> contributor;

    /**
     * A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition." )
    protected MarkdownType copyright;

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional documentation, citations, etc", formalDefinition="Related artifacts such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * A reference to a Library resource containing any formal logic used by the asset.
     */
    @Child(name = "library", type = {Library.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the asset", formalDefinition="A reference to a Library resource containing any formal logic used by the asset." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing any formal logic used by the asset.)
     */
    protected List<Library> libraryTarget;


    /**
     * A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.
     */
    @Child(name = "kind", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Kind of resource", formalDefinition="A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected Enumeration<ActivityDefinitionKind> kind;

    /**
     * Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-code")
    protected CodeableConcept code;

    /**
     * The period, timing or frequency upon which the described activity is to occur.
     */
    @Child(name = "timing", type = {Timing.class, DateTimeType.class, Period.class, Range.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
    protected Type timing;

    /**
     * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
     */
    @Child(name = "location", type = {Location.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    protected Location locationTarget;

    /**
     * Indicates who should participate in performing the action described.
     */
    @Child(name = "participant", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who should participate in the action", formalDefinition="Indicates who should participate in performing the action described." )
    protected List<ActivityDefinitionParticipantComponent> participant;

    /**
     * Identifies the food, drug or other product being consumed or supplied in the activity.
     */
    @Child(name = "product", type = {Medication.class, Substance.class, CodeableConcept.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What's administered/supplied", formalDefinition="Identifies the food, drug or other product being consumed or supplied in the activity." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected Type product;

    /**
     * Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).
     */
    @Child(name = "quantity", type = {SimpleQuantity.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How much is administered/consumed/supplied", formalDefinition="Identifies the quantity expected to be consumed at once (per dose, per meal, etc.)." )
    protected SimpleQuantity quantity;

    /**
     * Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources.
     */
    @Child(name = "dosage", type = {Dosage.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Detailed dosage instructions", formalDefinition="Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources." )
    protected List<Dosage> dosage;

    /**
     * Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What part of body to perform on", formalDefinition="Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.
     */
    @Child(name = "transform", type = {StructureMap.class}, order=20, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Transform to apply the template", formalDefinition="A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input." )
    protected Reference transform;

    /**
     * The actual object that is the target of the reference (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    protected StructureMap transformTarget;

    /**
     * Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result.
     */
    @Child(name = "dynamicValue", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Dynamic aspects of the definition", formalDefinition="Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result." )
    protected List<ActivityDefinitionDynamicValueComponent> dynamicValue;

    private static final long serialVersionUID = 1741931476L;

  /**
   * Constructor
   */
    public ActivityDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ActivityDefinition(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this activity definition is (or will be) published. The URL SHOULD include the major version of the activity definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this activity definition is (or will be) published. The URL SHOULD include the major version of the activity definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ActivityDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this activity definition is (or will be) published. The URL SHOULD include the major version of the activity definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this activity definition is (or will be) published. The URL SHOULD include the major version of the activity definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public ActivityDefinition setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this activity definition when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public ActivityDefinition addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the activity definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the activity definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ActivityDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the activity definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the activity definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    public ActivityDefinition setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A natural language name identifying the activity definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A natural language name identifying the activity definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ActivityDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the activity definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the activity definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ActivityDefinition setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for the activity definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the activity definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ActivityDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the activity definition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the activity definition.
     */
    public ActivityDefinition setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of this activity definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this activity definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ActivityDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this activity definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this activity definition. Enables tracking the life-cycle of the content.
     */
    public ActivityDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this activity definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A boolean value to indicate that this activity definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ActivityDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this activity definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this activity definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ActivityDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the activity definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the activity definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the activity definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the activity definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ActivityDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the activity definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the activity definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the activity definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the activity definition changes.
     */
    public ActivityDefinition setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the activity definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the activity definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ActivityDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the activity definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the activity definition.
     */
    public ActivityDefinition setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the activity definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the activity definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ActivityDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the activity definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the activity definition from a consumer's perspective.
     */
    public ActivityDefinition setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #purpose} (Explaination of why this activity definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explaination of why this activity definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public ActivityDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this activity definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this activity definition is needed and why it has been designed as it has.
     */
    public ActivityDefinition setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (A detailed description of how the asset is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new StringType(); // bb
      return this.usage;
    }

    public boolean hasUsageElement() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (A detailed description of how the asset is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public ActivityDefinition setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return A detailed description of how the asset is used from a clinical perspective.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value A detailed description of how the asset is used from a clinical perspective.
     */
    public ActivityDefinition setUsage(String value) { 
      if (Utilities.noString(value))
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public DateType getApprovalDateElement() { 
      if (this.approvalDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.approvalDate");
        else if (Configuration.doAutoCreate())
          this.approvalDate = new DateType(); // bb
      return this.approvalDate;
    }

    public boolean hasApprovalDateElement() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    public boolean hasApprovalDate() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    /**
     * @param value {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public ActivityDefinition setApprovalDateElement(DateType value) { 
      this.approvalDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Date getApprovalDate() { 
      return this.approvalDate == null ? null : this.approvalDate.getValue();
    }

    /**
     * @param value The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public ActivityDefinition setApprovalDate(Date value) { 
      if (value == null)
        this.approvalDate = null;
      else {
        if (this.approvalDate == null)
          this.approvalDate = new DateType();
        this.approvalDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.lastReviewDate");
        else if (Configuration.doAutoCreate())
          this.lastReviewDate = new DateType(); // bb
      return this.lastReviewDate;
    }

    public boolean hasLastReviewDateElement() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    public boolean hasLastReviewDate() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    /**
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public ActivityDefinition setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    public ActivityDefinition setLastReviewDate(Date value) { 
      if (value == null)
        this.lastReviewDate = null;
      else {
        if (this.lastReviewDate == null)
          this.lastReviewDate = new DateType();
        this.lastReviewDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #effectivePeriod} (The period during which the activity definition content was or is planned to be in active use.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the activity definition content was or is planned to be in active use.)
     */
    public ActivityDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate activity definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public ActivityDefinition addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the activity definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public ActivityDefinition addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #topic} (Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity that can be useful for filtering and searching.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setTopic(List<CodeableConcept> theTopic) { 
      this.topic = theTopic;
      return this;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (CodeableConcept item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTopic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return t;
    }

    public ActivityDefinition addTopic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
     */
    public CodeableConcept getTopicFirstRep() { 
      if (getTopic().isEmpty()) {
        addTopic();
      }
      return getTopic().get(0);
    }

    /**
     * @return {@link #contributor} (A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.)
     */
    public List<Contributor> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      return this.contributor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setContributor(List<Contributor> theContributor) { 
      this.contributor = theContributor;
      return this;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (Contributor item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Contributor addContributor() { //3
      Contributor t = new Contributor();
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return t;
    }

    public ActivityDefinition addContributor(Contributor t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contributor}, creating it if it does not already exist
     */
    public Contributor getContributorFirstRep() { 
      if (getContributor().isEmpty()) {
        addContributor();
      }
      return getContributor().get(0);
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public ActivityDefinition addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ActivityDefinition setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition.
     */
    public ActivityDefinition setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedArtifact} (Related artifacts such as additional documentation, justification, or bibliographic references.)
     */
    public List<RelatedArtifact> getRelatedArtifact() { 
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      return this.relatedArtifact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
      this.relatedArtifact = theRelatedArtifact;
      return this;
    }

    public boolean hasRelatedArtifact() { 
      if (this.relatedArtifact == null)
        return false;
      for (RelatedArtifact item : this.relatedArtifact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedArtifact addRelatedArtifact() { //3
      RelatedArtifact t = new RelatedArtifact();
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return t;
    }

    public ActivityDefinition addRelatedArtifact(RelatedArtifact t) { //3
      if (t == null)
        return this;
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedArtifact}, creating it if it does not already exist
     */
    public RelatedArtifact getRelatedArtifactFirstRep() { 
      if (getRelatedArtifact().isEmpty()) {
        addRelatedArtifact();
      }
      return getRelatedArtifact().get(0);
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the asset.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setLibrary(List<Reference> theLibrary) { 
      this.library = theLibrary;
      return this;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    public ActivityDefinition addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #library}, creating it if it does not already exist
     */
    public Reference getLibraryFirstRep() { 
      if (getLibrary().isEmpty()) {
        addLibrary();
      }
      return getLibrary().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #kind} (A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<ActivityDefinitionKind> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<ActivityDefinitionKind>(new ActivityDefinitionKindEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public ActivityDefinition setKindElement(Enumeration<ActivityDefinitionKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.
     */
    public ActivityDefinitionKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.
     */
    public ActivityDefinition setKind(ActivityDefinitionKind value) { 
      if (value == null)
        this.kind = null;
      else {
        if (this.kind == null)
          this.kind = new Enumeration<ActivityDefinitionKind>(new ActivityDefinitionKindEnumFactory());
        this.kind.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.)
     */
    public ActivityDefinition setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public Timing getTimingTiming() throws FHIRException { 
      if (!(this.timing instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Timing) this.timing;
    }

    public boolean hasTimingTiming() { 
      return this.timing instanceof Timing;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public DateTimeType getTimingDateTimeType() throws FHIRException { 
      if (!(this.timing instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (DateTimeType) this.timing;
    }

    public boolean hasTimingDateTimeType() { 
      return this.timing instanceof DateTimeType;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public Period getTimingPeriod() throws FHIRException { 
      if (!(this.timing instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Period) this.timing;
    }

    public boolean hasTimingPeriod() { 
      return this.timing instanceof Period;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public Range getTimingRange() throws FHIRException { 
      if (!(this.timing instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Range) this.timing;
    }

    public boolean hasTimingRange() { 
      return this.timing instanceof Range;
    }

    public boolean hasTiming() { 
      return this.timing != null && !this.timing.isEmpty();
    }

    /**
     * @param value {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public ActivityDefinition setTiming(Type value) { 
      this.timing = value;
      return this;
    }

    /**
     * @return {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public ActivityDefinition setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public ActivityDefinition setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #participant} (Indicates who should participate in performing the action described.)
     */
    public List<ActivityDefinitionParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<ActivityDefinitionParticipantComponent>();
      return this.participant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setParticipant(List<ActivityDefinitionParticipantComponent> theParticipant) { 
      this.participant = theParticipant;
      return this;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (ActivityDefinitionParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ActivityDefinitionParticipantComponent addParticipant() { //3
      ActivityDefinitionParticipantComponent t = new ActivityDefinitionParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<ActivityDefinitionParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    public ActivityDefinition addParticipant(ActivityDefinitionParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<ActivityDefinitionParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
     */
    public ActivityDefinitionParticipantComponent getParticipantFirstRep() { 
      if (getParticipant().isEmpty()) {
        addParticipant();
      }
      return getParticipant().get(0);
    }

    /**
     * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public Type getProduct() { 
      return this.product;
    }

    /**
     * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public Reference getProductReference() throws FHIRException { 
      if (!(this.product instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.product.getClass().getName()+" was encountered");
      return (Reference) this.product;
    }

    public boolean hasProductReference() { 
      return this.product instanceof Reference;
    }

    /**
     * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public CodeableConcept getProductCodeableConcept() throws FHIRException { 
      if (!(this.product instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.product.getClass().getName()+" was encountered");
      return (CodeableConcept) this.product;
    }

    public boolean hasProductCodeableConcept() { 
      return this.product instanceof CodeableConcept;
    }

    public boolean hasProduct() { 
      return this.product != null && !this.product.isEmpty();
    }

    /**
     * @param value {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public ActivityDefinition setProduct(Type value) { 
      this.product = value;
      return this;
    }

    /**
     * @return {@link #quantity} (Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).)
     */
    public SimpleQuantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new SimpleQuantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).)
     */
    public ActivityDefinition setQuantity(SimpleQuantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #dosage} (Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources.)
     */
    public List<Dosage> getDosage() { 
      if (this.dosage == null)
        this.dosage = new ArrayList<Dosage>();
      return this.dosage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setDosage(List<Dosage> theDosage) { 
      this.dosage = theDosage;
      return this;
    }

    public boolean hasDosage() { 
      if (this.dosage == null)
        return false;
      for (Dosage item : this.dosage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Dosage addDosage() { //3
      Dosage t = new Dosage();
      if (this.dosage == null)
        this.dosage = new ArrayList<Dosage>();
      this.dosage.add(t);
      return t;
    }

    public ActivityDefinition addDosage(Dosage t) { //3
      if (t == null)
        return this;
      if (this.dosage == null)
        this.dosage = new ArrayList<Dosage>();
      this.dosage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dosage}, creating it if it does not already exist
     */
    public Dosage getDosageFirstRep() { 
      if (getDosage().isEmpty()) {
        addDosage();
      }
      return getDosage().get(0);
    }

    /**
     * @return {@link #bodySite} (Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setBodySite(List<CodeableConcept> theBodySite) { 
      this.bodySite = theBodySite;
      return this;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    public ActivityDefinition addBodySite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #bodySite}, creating it if it does not already exist
     */
    public CodeableConcept getBodySiteFirstRep() { 
      if (getBodySite().isEmpty()) {
        addBodySite();
      }
      return getBodySite().get(0);
    }

    /**
     * @return {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public Reference getTransform() { 
      if (this.transform == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.transform");
        else if (Configuration.doAutoCreate())
          this.transform = new Reference(); // cc
      return this.transform;
    }

    public boolean hasTransform() { 
      return this.transform != null && !this.transform.isEmpty();
    }

    /**
     * @param value {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public ActivityDefinition setTransform(Reference value) { 
      this.transform = value;
      return this;
    }

    /**
     * @return {@link #transform} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public StructureMap getTransformTarget() { 
      if (this.transformTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.transform");
        else if (Configuration.doAutoCreate())
          this.transformTarget = new StructureMap(); // aa
      return this.transformTarget;
    }

    /**
     * @param value {@link #transform} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public ActivityDefinition setTransformTarget(StructureMap value) { 
      this.transformTarget = value;
      return this;
    }

    /**
     * @return {@link #dynamicValue} (Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result.)
     */
    public List<ActivityDefinitionDynamicValueComponent> getDynamicValue() { 
      if (this.dynamicValue == null)
        this.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
      return this.dynamicValue;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setDynamicValue(List<ActivityDefinitionDynamicValueComponent> theDynamicValue) { 
      this.dynamicValue = theDynamicValue;
      return this;
    }

    public boolean hasDynamicValue() { 
      if (this.dynamicValue == null)
        return false;
      for (ActivityDefinitionDynamicValueComponent item : this.dynamicValue)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ActivityDefinitionDynamicValueComponent addDynamicValue() { //3
      ActivityDefinitionDynamicValueComponent t = new ActivityDefinitionDynamicValueComponent();
      if (this.dynamicValue == null)
        this.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
      this.dynamicValue.add(t);
      return t;
    }

    public ActivityDefinition addDynamicValue(ActivityDefinitionDynamicValueComponent t) { //3
      if (t == null)
        return this;
      if (this.dynamicValue == null)
        this.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
      this.dynamicValue.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dynamicValue}, creating it if it does not already exist
     */
    public ActivityDefinitionDynamicValueComponent getDynamicValueFirstRep() { 
      if (getDynamicValue().isEmpty()) {
        addDynamicValue();
      }
      return getDynamicValue().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this activity definition is (or will be) published. The URL SHOULD include the major version of the activity definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this activity definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the activity definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the activity definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the activity definition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this activity definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this activity definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the activity definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the activity definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the activity definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the activity definition from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this activity definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the asset is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, java.lang.Integer.MAX_VALUE, approvalDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the activity definition content was or is planned to be in active use.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate activity definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the activity definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the activity definition.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the asset.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("kind", "code", "A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a ProcedureRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("timing[x]", "Timing|dateTime|Period|Range", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, timing));
        childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("participant", "", "Indicates who should participate in performing the action described.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("product[x]", "Reference(Medication|Substance)|CodeableConcept", "Identifies the food, drug or other product being consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
        childrenList.add(new Property("quantity", "SimpleQuantity", "Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("dosage", "Dosage", "Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources.", 0, java.lang.Integer.MAX_VALUE, dosage));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("transform", "Reference(StructureMap)", "A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.", 0, java.lang.Integer.MAX_VALUE, transform));
        childrenList.add(new Property("dynamicValue", "", "Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result.", 0, java.lang.Integer.MAX_VALUE, dynamicValue));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Contributor
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<ActivityDefinitionKind>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // ActivityDefinitionParticipantComponent
        case -309474065: /*product*/ return this.product == null ? new Base[0] : new Base[] {this.product}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // Dosage
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : this.bodySite.toArray(new Base[this.bodySite.size()]); // CodeableConcept
        case 1052666732: /*transform*/ return this.transform == null ? new Base[0] : new Base[] {this.transform}; // Reference
        case 572625010: /*dynamicValue*/ return this.dynamicValue == null ? new Base[0] : this.dynamicValue.toArray(new Base[this.dynamicValue.size()]); // ActivityDefinitionDynamicValueComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          return value;
        case 223539345: // approvalDate
          this.approvalDate = castToDate(value); // DateType
          return value;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          return value;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          return value;
        case 3292052: // kind
          value = new ActivityDefinitionKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<ActivityDefinitionKind>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case 767422259: // participant
          this.getParticipant().add((ActivityDefinitionParticipantComponent) value); // ActivityDefinitionParticipantComponent
          return value;
        case -309474065: // product
          this.product = castToType(value); // Type
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -1326018889: // dosage
          this.getDosage().add(castToDosage(value)); // Dosage
          return value;
        case 1702620169: // bodySite
          this.getBodySite().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1052666732: // transform
          this.transform = castToReference(value); // Reference
          return value;
        case 572625010: // dynamicValue
          this.getDynamicValue().add((ActivityDefinitionDynamicValueComponent) value); // ActivityDefinitionDynamicValueComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("usage")) {
          this.usage = castToString(value); // StringType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("topic")) {
          this.getTopic().add(castToCodeableConcept(value));
        } else if (name.equals("contributor")) {
          this.getContributor().add(castToContributor(value));
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("relatedArtifact")) {
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        } else if (name.equals("library")) {
          this.getLibrary().add(castToReference(value));
        } else if (name.equals("kind")) {
          value = new ActivityDefinitionKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<ActivityDefinitionKind>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("timing[x]")) {
          this.timing = castToType(value); // Type
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("participant")) {
          this.getParticipant().add((ActivityDefinitionParticipantComponent) value);
        } else if (name.equals("product[x]")) {
          this.product = castToType(value); // Type
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("dosage")) {
          this.getDosage().add(castToDosage(value));
        } else if (name.equals("bodySite")) {
          this.getBodySite().add(castToCodeableConcept(value));
        } else if (name.equals("transform")) {
          this.transform = castToReference(value); // Reference
        } else if (name.equals("dynamicValue")) {
          this.getDynamicValue().add((ActivityDefinitionDynamicValueComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return addIdentifier(); 
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case -1724546052:  return getDescriptionElement();
        case -220463842:  return getPurposeElement();
        case 111574433:  return getUsageElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 110546223:  return addTopic(); 
        case -1895276325:  return addContributor(); 
        case 951526432:  return addContact(); 
        case 1522889671:  return getCopyrightElement();
        case 666807069:  return addRelatedArtifact(); 
        case 166208699:  return addLibrary(); 
        case 3292052:  return getKindElement();
        case 3059181:  return getCode(); 
        case 164632566:  return getTiming(); 
        case -873664438:  return getTiming(); 
        case 1901043637:  return getLocation(); 
        case 767422259:  return addParticipant(); 
        case 1753005361:  return getProduct(); 
        case -309474065:  return getProduct(); 
        case -1285004149:  return getQuantity(); 
        case -1326018889:  return addDosage(); 
        case 1702620169:  return addBodySite(); 
        case 1052666732:  return getTransform(); 
        case 572625010:  return addDynamicValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 111574433: /*usage*/ return new String[] {"string"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 110546223: /*topic*/ return new String[] {"CodeableConcept"};
        case -1895276325: /*contributor*/ return new String[] {"Contributor"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 666807069: /*relatedArtifact*/ return new String[] {"RelatedArtifact"};
        case 166208699: /*library*/ return new String[] {"Reference"};
        case 3292052: /*kind*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -873664438: /*timing*/ return new String[] {"Timing", "dateTime", "Period", "Range"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 767422259: /*participant*/ return new String[] {};
        case -309474065: /*product*/ return new String[] {"Reference", "CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -1326018889: /*dosage*/ return new String[] {"Dosage"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case 1052666732: /*transform*/ return new String[] {"Reference"};
        case 572625010: /*dynamicValue*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.publisher");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.usage");
        }
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.copyright");
        }
        else if (name.equals("relatedArtifact")) {
          return addRelatedArtifact();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.kind");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("timingTiming")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("timingDateTime")) {
          this.timing = new DateTimeType();
          return this.timing;
        }
        else if (name.equals("timingPeriod")) {
          this.timing = new Period();
          return this.timing;
        }
        else if (name.equals("timingRange")) {
          this.timing = new Range();
          return this.timing;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("productReference")) {
          this.product = new Reference();
          return this.product;
        }
        else if (name.equals("productCodeableConcept")) {
          this.product = new CodeableConcept();
          return this.product;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("dosage")) {
          return addDosage();
        }
        else if (name.equals("bodySite")) {
          return addBodySite();
        }
        else if (name.equals("transform")) {
          this.transform = new Reference();
          return this.transform;
        }
        else if (name.equals("dynamicValue")) {
          return addDynamicValue();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ActivityDefinition";

  }

      public ActivityDefinition copy() {
        ActivityDefinition dst = new ActivityDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (contributor != null) {
          dst.contributor = new ArrayList<Contributor>();
          for (Contributor i : contributor)
            dst.contributor.add(i.copy());
        };
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        if (relatedArtifact != null) {
          dst.relatedArtifact = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : relatedArtifact)
            dst.relatedArtifact.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        dst.kind = kind == null ? null : kind.copy();
        dst.code = code == null ? null : code.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.location = location == null ? null : location.copy();
        if (participant != null) {
          dst.participant = new ArrayList<ActivityDefinitionParticipantComponent>();
          for (ActivityDefinitionParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.product = product == null ? null : product.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (dosage != null) {
          dst.dosage = new ArrayList<Dosage>();
          for (Dosage i : dosage)
            dst.dosage.add(i.copy());
        };
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
        };
        dst.transform = transform == null ? null : transform.copy();
        if (dynamicValue != null) {
          dst.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
          for (ActivityDefinitionDynamicValueComponent i : dynamicValue)
            dst.dynamicValue.add(i.copy());
        };
        return dst;
      }

      protected ActivityDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActivityDefinition))
          return false;
        ActivityDefinition o = (ActivityDefinition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true)
           && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(topic, o.topic, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(relatedArtifact, o.relatedArtifact, true)
           && compareDeep(library, o.library, true) && compareDeep(kind, o.kind, true) && compareDeep(code, o.code, true)
           && compareDeep(timing, o.timing, true) && compareDeep(location, o.location, true) && compareDeep(participant, o.participant, true)
           && compareDeep(product, o.product, true) && compareDeep(quantity, o.quantity, true) && compareDeep(dosage, o.dosage, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(transform, o.transform, true) && compareDeep(dynamicValue, o.dynamicValue, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActivityDefinition))
          return false;
        ActivityDefinition o = (ActivityDefinition) other;
        return compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true) && compareValues(approvalDate, o.approvalDate, true)
           && compareValues(lastReviewDate, o.lastReviewDate, true) && compareValues(copyright, o.copyright, true)
           && compareValues(kind, o.kind, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, purpose, usage
          , approvalDate, lastReviewDate, effectivePeriod, topic, contributor, copyright, relatedArtifact
          , library, kind, code, timing, location, participant, product, quantity, dosage
          , bodySite, transform, dynamicValue);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ActivityDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The activity definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ActivityDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ActivityDefinition.date", description="The activity definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The activity definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ActivityDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ActivityDefinition.identifier", description="External identifier for the activity definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="successor", path="ActivityDefinition.relatedArtifact.where(type='successor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_SUCCESSOR = "successor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUCCESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUCCESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ActivityDefinition:successor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUCCESSOR = new ca.uhn.fhir.model.api.Include("ActivityDefinition:successor").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ActivityDefinition.jurisdiction", description="Intended jurisdiction for the activity definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ActivityDefinition.description", description="The description of the activity definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="derived-from", path="ActivityDefinition.relatedArtifact.where(type='derived-from').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_DERIVED_FROM = "derived-from";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DERIVED_FROM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DERIVED_FROM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ActivityDefinition:derived-from</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DERIVED_FROM = new ca.uhn.fhir.model.api.Include("ActivityDefinition:derived-from").toLocked();

 /**
   * Search parameter: <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="predecessor", path="ActivityDefinition.relatedArtifact.where(type='predecessor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_PREDECESSOR = "predecessor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PREDECESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PREDECESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ActivityDefinition:predecessor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PREDECESSOR = new ca.uhn.fhir.model.api.Include("ActivityDefinition:predecessor").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ActivityDefinition.title", description="The human-friendly name of the activity definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="composed-of", path="ActivityDefinition.relatedArtifact.where(type='composed-of').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_COMPOSED_OF = "composed-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPOSED_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPOSED_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ActivityDefinition:composed-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPOSED_OF = new ca.uhn.fhir.model.api.Include("ActivityDefinition:composed-of").toLocked();

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ActivityDefinition.version", description="The business version of the activity definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the activity definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ActivityDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ActivityDefinition.url", description="The uri that identifies the activity definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the activity definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ActivityDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the activity definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ActivityDefinition.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="ActivityDefinition.effectivePeriod", description="The time during which the activity definition is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the activity definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ActivityDefinition.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource, ActivityDefinition.library</b><br>
   * </p>
   */
  @SearchParamDefinition(name="depends-on", path="ActivityDefinition.relatedArtifact.where(type='depends-on').resource | ActivityDefinition.library", description="What resource is being referenced", type="reference" )
  public static final String SP_DEPENDS_ON = "depends-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ActivityDefinition.relatedArtifact.resource, ActivityDefinition.library</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDS_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDS_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ActivityDefinition:depends-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDS_ON = new ca.uhn.fhir.model.api.Include("ActivityDefinition:depends-on").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ActivityDefinition.name", description="Computationally friendly name of the activity definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ActivityDefinition.publisher", description="Name of the publisher of the activity definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the activity definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="ActivityDefinition.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ActivityDefinition.status", description="The current status of the activity definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the activity definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

