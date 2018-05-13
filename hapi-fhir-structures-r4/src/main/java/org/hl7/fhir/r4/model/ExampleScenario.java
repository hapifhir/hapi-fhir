package org.hl7.fhir.r4.model;

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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Example of workflow instance.
 */
@ResourceDef(name="ExampleScenario", profile="http://hl7.org/fhir/Profile/ExampleScenario")
@ChildOrder(names={"url", "identifier", "version", "name", "status", "experimental", "date", "publisher", "contact", "useContext", "jurisdiction", "copyright", "purpose", "actor", "instance", "process", "workflow"})
public class ExampleScenario extends MetadataResource {

    public enum ExampleScenarioActorType {
        /**
         * A person
         */
        PERSON, 
        /**
         * A system
         */
        ENTITY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ExampleScenarioActorType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("person".equals(codeString))
          return PERSON;
        if ("entity".equals(codeString))
          return ENTITY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ExampleScenarioActorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERSON: return "person";
            case ENTITY: return "entity";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PERSON: return "http://hl7.org/fhir/examplescenario-actor-type";
            case ENTITY: return "http://hl7.org/fhir/examplescenario-actor-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PERSON: return "A person";
            case ENTITY: return "A system";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERSON: return "Person";
            case ENTITY: return "System";
            default: return "?";
          }
        }
    }

  public static class ExampleScenarioActorTypeEnumFactory implements EnumFactory<ExampleScenarioActorType> {
    public ExampleScenarioActorType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("person".equals(codeString))
          return ExampleScenarioActorType.PERSON;
        if ("entity".equals(codeString))
          return ExampleScenarioActorType.ENTITY;
        throw new IllegalArgumentException("Unknown ExampleScenarioActorType code '"+codeString+"'");
        }
        public Enumeration<ExampleScenarioActorType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ExampleScenarioActorType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("person".equals(codeString))
          return new Enumeration<ExampleScenarioActorType>(this, ExampleScenarioActorType.PERSON);
        if ("entity".equals(codeString))
          return new Enumeration<ExampleScenarioActorType>(this, ExampleScenarioActorType.ENTITY);
        throw new FHIRException("Unknown ExampleScenarioActorType code '"+codeString+"'");
        }
    public String toCode(ExampleScenarioActorType code) {
      if (code == ExampleScenarioActorType.PERSON)
        return "person";
      if (code == ExampleScenarioActorType.ENTITY)
        return "entity";
      return "?";
      }
    public String toSystem(ExampleScenarioActorType code) {
      return code.getSystem();
      }
    }

    public enum FHIRResourceType {
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
         * A material substance originating from a biological entity intended to be transplanted or infused
into another (possibly the same) biological entity.
         */
        BIOLOGICALLYDERIVEDPRODUCT, 
        /**
         * Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSTRUCTURE, 
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
         * The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.
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
         * A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).
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
         * A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
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
         * Catalog entries are wrappers that contextualize items included in a catalog.
         */
        ENTRYDEFINITION, 
        /**
         * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
         */
        EPISODEOFCARE, 
        /**
         * The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENTDEFINITION, 
        /**
         * Example of workflow instance.
         */
        EXAMPLESCENARIO, 
        /**
         * Resource to define constraints on the Expansion of a FHIR ValueSet.
         */
        EXPANSIONPROFILE, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health conditions for a person related to the patient relevant in the context of care for the patient.
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
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.
         */
        IMMUNIZATION, 
        /**
         * Describes a comparison of an immunization event against published recommendations to determine if the administration is "valid" in relation to those  recommendations.
         */
        IMMUNIZATIONEVALUATION, 
        /**
         * A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.
         */
        INVOICE, 
        /**
         * A physical, countable instance of an item, for example one box or one unit.
         */
        ITEMINSTANCE, 
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
         * Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.
         */
        LOCATION, 
        /**
         * The Measure resource provides the definition of a quality measure.
         */
        MEASURE, 
        /**
         * The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.
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
         * Information about a medication that is used to support knowledge.
         */
        MEDICATIONKNOWLEDGE, 
        /**
         * An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.
         */
        MEDICATIONREQUEST, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
         */
        MEDICINALPRODUCT, 
        /**
         * The regulatory authorization of a medicinal product.
         */
        MEDICINALPRODUCTAUTHORIZATION, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCLINICALS, 
        /**
         * A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to relace the Device resource, which covers use of device instances.
         */
        MEDICINALPRODUCTDEVICESPEC, 
        /**
         * An ingredient of a manufactured item or pharmaceutical product.
         */
        MEDICINALPRODUCTINGREDIENT, 
        /**
         * A medicinal product in a container or package.
         */
        MEDICINALPRODUCTPACKAGED, 
        /**
         * A pharmaceutical product described in terms of its composition and dose form.
         */
        MEDICINALPRODUCTPHARMACEUTICAL, 
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
         * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.
         */
        OBSERVATIONDEFINITION, 
        /**
         * A person's work information, structured to facilitate individual, population, and public health use; not intended to support billing.
         */
        OCCUPATIONALDATA, 
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
         * A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.
         */
        ORGANIZATIONROLE, 
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
         * This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.
         */
        PROCESSREQUEST, 
        /**
         * This resource provides processing status, errors and notes from the processing of a resource.
         */
        PROCESSRESPONSE, 
        /**
         * Details of a Health Insurance product/plan provided by an organization.
         */
        PRODUCTPLAN, 
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
         * A physical entity which is the primary unit of operational and/or administrative interest in a study.
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
         * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICEREQUEST, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A kind of specimen with associated set of requirements.
         */
        SPECIMENDEFINITION, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Todo.
         */
        SUBSTANCEPOLYMER, 
        /**
         * Todo.
         */
        SUBSTANCEREFERENCEINFORMATION, 
        /**
         * The detailed description of a substance, typically at a level beyond what is used for prescribing.
         */
        SUBSTANCESPECIFICATION, 
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
         * A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        TERMINOLOGYCAPABILITIES, 
        /**
         * A summary of information based on the results of executing a TestScript.
         */
        TESTREPORT, 
        /**
         * A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * Information about a user's current session.
         */
        USERSESSION, 
        /**
         * A ValueSet resource specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).
         */
        VALUESET, 
        /**
         * Describes validation requirements, source(s), status and dates for one or more elements.
         */
        VERIFICATIONRESULT, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static FHIRResourceType fromCode(String codeString) throws FHIRException {
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
        if ("BiologicallyDerivedProduct".equals(codeString))
          return BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return BODYSTRUCTURE;
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
        if ("EntryDefinition".equals(codeString))
          return ENTRYDEFINITION;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return EXAMPLESCENARIO;
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
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("Invoice".equals(codeString))
          return INVOICE;
        if ("ItemInstance".equals(codeString))
          return ITEMINSTANCE;
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
        if ("MedicationKnowledge".equals(codeString))
          return MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIngredient".equals(codeString))
          return MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductPackaged".equals(codeString))
          return MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return MEDICINALPRODUCTPHARMACEUTICAL;
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
        if ("ObservationDefinition".equals(codeString))
          return OBSERVATIONDEFINITION;
        if ("OccupationalData".equals(codeString))
          return OCCUPATIONALDATA;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("OrganizationRole".equals(codeString))
          return ORGANIZATIONROLE;
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
        if ("ProcessRequest".equals(codeString))
          return PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return PROCESSRESPONSE;
        if ("ProductPlan".equals(codeString))
          return PRODUCTPLAN;
        if ("Provenance".equals(codeString))
          return PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return QUESTIONNAIRERESPONSE;
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
        if ("ServiceRequest".equals(codeString))
          return SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return TESTREPORT;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return USERSESSION;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VerificationResult".equals(codeString))
          return VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown FHIRResourceType code '"+codeString+"'");
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
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
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
            case ENTRYDEFINITION: return "EntryDefinition";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPANSIONPROFILE: return "ExpansionProfile";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
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
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OCCUPATIONALDATA: return "OccupationalData";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONROLE: return "OrganizationRole";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PRODUCTPLAN: return "ProductPlan";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
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
            case BIOLOGICALLYDERIVEDPRODUCT: return "http://hl7.org/fhir/resource-types";
            case BODYSTRUCTURE: return "http://hl7.org/fhir/resource-types";
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
            case ENTRYDEFINITION: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EVENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case EXAMPLESCENARIO: return "http://hl7.org/fhir/resource-types";
            case EXPANSIONPROFILE: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GRAPHDEFINITION: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case GUIDANCERESPONSE: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONEVALUATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case INVOICE: return "http://hl7.org/fhir/resource-types";
            case ITEMINSTANCE: return "http://hl7.org/fhir/resource-types";
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
            case MEDICATIONKNOWLEDGE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTAUTHORIZATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCLINICALS: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTDEVICESPEC: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINGREDIENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPACKAGED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "http://hl7.org/fhir/resource-types";
            case MESSAGEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OBSERVATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OCCUPATIONALDATA: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATIONROLE: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONERROLE: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PRODUCTPLAN: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case REQUESTGROUP: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSTUDY: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSUBJECT: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SEQUENCE: return "http://hl7.org/fhir/resource-types";
            case SERVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case SPECIMENDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREMAP: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEPOLYMER: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEREFERENCEINFORMATION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCESPECIFICATION: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TASK: return "http://hl7.org/fhir/resource-types";
            case TERMINOLOGYCAPABILITIES: return "http://hl7.org/fhir/resource-types";
            case TESTREPORT: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case USERSESSION: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VERIFICATIONRESULT: return "http://hl7.org/fhir/resource-types";
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
            case BIOLOGICALLYDERIVEDPRODUCT: return "A material substance originating from a biological entity intended to be transplanted or infused\ninto another (possibly the same) biological entity.";
            case BODYSTRUCTURE: return "Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAPABILITYSTATEMENT: return "A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CARETEAM: return "The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.";
            case CHARGEITEM: return "The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case CODESYSTEM: return "The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPARTMENTDEFINITION: return "A compartment definition that defines how resources are accessed on a server.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.";
            case CONDITION: return "A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.";
            case CONSENT: return "A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.";
            case CONTRACT: return "A formal agreement between parties regarding the conduct of business, exchange of information or other matters.";
            case COVERAGE: return "Financial instrument which may be used to reimburse or pay for health care products and services.";
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
            case ENTRYDEFINITION: return "Catalog entries are wrappers that contextualize items included in a catalog.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EVENTDEFINITION: return "The EventDefinition resource provides a reusable description of when a particular event can occur.";
            case EXAMPLESCENARIO: return "Example of workflow instance.";
            case EXPANSIONPROFILE: return "Resource to define constraints on the Expansion of a FHIR ValueSet.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GRAPHDEFINITION: return "A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case GUIDANCERESPONSE: return "A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.";
            case IMMUNIZATIONEVALUATION: return "Describes a comparison of an immunization event against published recommendations to determine if the administration is \"valid\" in relation to those  recommendations.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.";
            case INVOICE: return "Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.";
            case ITEMINSTANCE: return "A physical, countable instance of an item, for example one box or one unit.";
            case LIBRARY: return "The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.";
            case LINKAGE: return "Identifies two or more records (resource instances) that are referring to the same real-world \"occurrence\".";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.";
            case MEASURE: return "The Measure resource provides the definition of a quality measure.";
            case MEASUREREPORT: return "The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONKNOWLEDGE: return "Information about a medication that is used to support knowledge.";
            case MEDICATIONREQUEST: return "An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationRequest\" rather than \"MedicationPrescription\" or \"MedicationOrder\" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MEDICINALPRODUCT: return "Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).";
            case MEDICINALPRODUCTAUTHORIZATION: return "The regulatory authorization of a medicinal product.";
            case MEDICINALPRODUCTCLINICALS: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTDEVICESPEC: return "A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to relace the Device resource, which covers use of device instances.";
            case MEDICINALPRODUCTINGREDIENT: return "An ingredient of a manufactured item or pharmaceutical product.";
            case MEDICINALPRODUCTPACKAGED: return "A medicinal product in a container or package.";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "A pharmaceutical product described in terms of its composition and dose form.";
            case MESSAGEDEFINITION: return "Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OBSERVATIONDEFINITION: return "Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.";
            case OCCUPATIONALDATA: return "A person's work information, structured to facilitate individual, population, and public health use; not intended to support billing.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.";
            case ORGANIZATIONROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PRACTITIONERROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PROCEDURE: return "An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PRODUCTPLAN: return "Details of a Health Insurance product/plan provided by an organization.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case REQUESTGROUP: return "A group of related requests that can be used to capture intended activities that have inter-dependencies such as \"give this medication after that one\".";
            case RESEARCHSTUDY: return "A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.";
            case RESEARCHSUBJECT: return "A physical entity which is the primary unit of operational and/or administrative interest in a study.";
            case RESOURCE: return "This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slots of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SEQUENCE: return "Raw data describing a biological sequence.";
            case SERVICEREQUEST: return "A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case SPECIMENDEFINITION: return "A kind of specimen with associated set of requirements.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system can take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUBSTANCEPOLYMER: return "Todo.";
            case SUBSTANCEREFERENCEINFORMATION: return "Todo.";
            case SUBSTANCESPECIFICATION: return "The detailed description of a substance, typically at a level beyond what is used for prescribing.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TASK: return "A task to be performed.";
            case TERMINOLOGYCAPABILITIES: return "A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case TESTREPORT: return "A summary of information based on the results of executing a TestScript.";
            case TESTSCRIPT: return "A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.";
            case USERSESSION: return "Information about a user's current session.";
            case VALUESET: return "A ValueSet resource specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).";
            case VERIFICATIONRESULT: return "Describes validation requirements, source(s), status and dates for one or more elements.";
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
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
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
            case ENTRYDEFINITION: return "EntryDefinition";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPANSIONPROFILE: return "ExpansionProfile";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
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
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OCCUPATIONALDATA: return "OccupationalData";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONROLE: return "OrganizationRole";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PRODUCTPLAN: return "ProductPlan";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
    }

  public static class FHIRResourceTypeEnumFactory implements EnumFactory<FHIRResourceType> {
    public FHIRResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Account".equals(codeString))
          return FHIRResourceType.ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return FHIRResourceType.ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return FHIRResourceType.ADVERSEEVENT;
        if ("AllergyIntolerance".equals(codeString))
          return FHIRResourceType.ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return FHIRResourceType.APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return FHIRResourceType.APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return FHIRResourceType.AUDITEVENT;
        if ("Basic".equals(codeString))
          return FHIRResourceType.BASIC;
        if ("Binary".equals(codeString))
          return FHIRResourceType.BINARY;
        if ("BiologicallyDerivedProduct".equals(codeString))
          return FHIRResourceType.BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return FHIRResourceType.BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return FHIRResourceType.BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return FHIRResourceType.CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return FHIRResourceType.CAREPLAN;
        if ("CareTeam".equals(codeString))
          return FHIRResourceType.CARETEAM;
        if ("ChargeItem".equals(codeString))
          return FHIRResourceType.CHARGEITEM;
        if ("Claim".equals(codeString))
          return FHIRResourceType.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return FHIRResourceType.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return FHIRResourceType.CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return FHIRResourceType.CODESYSTEM;
        if ("Communication".equals(codeString))
          return FHIRResourceType.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return FHIRResourceType.COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return FHIRResourceType.COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return FHIRResourceType.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return FHIRResourceType.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return FHIRResourceType.CONDITION;
        if ("Consent".equals(codeString))
          return FHIRResourceType.CONSENT;
        if ("Contract".equals(codeString))
          return FHIRResourceType.CONTRACT;
        if ("Coverage".equals(codeString))
          return FHIRResourceType.COVERAGE;
        if ("DetectedIssue".equals(codeString))
          return FHIRResourceType.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return FHIRResourceType.DEVICE;
        if ("DeviceComponent".equals(codeString))
          return FHIRResourceType.DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return FHIRResourceType.DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return FHIRResourceType.DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return FHIRResourceType.DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return FHIRResourceType.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return FHIRResourceType.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return FHIRResourceType.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return FHIRResourceType.DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return FHIRResourceType.ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return FHIRResourceType.ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return FHIRResourceType.ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return FHIRResourceType.ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return FHIRResourceType.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return FHIRResourceType.ENROLLMENTRESPONSE;
        if ("EntryDefinition".equals(codeString))
          return FHIRResourceType.ENTRYDEFINITION;
        if ("EpisodeOfCare".equals(codeString))
          return FHIRResourceType.EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return FHIRResourceType.EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return FHIRResourceType.EXAMPLESCENARIO;
        if ("ExpansionProfile".equals(codeString))
          return FHIRResourceType.EXPANSIONPROFILE;
        if ("ExplanationOfBenefit".equals(codeString))
          return FHIRResourceType.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FHIRResourceType.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FHIRResourceType.FLAG;
        if ("Goal".equals(codeString))
          return FHIRResourceType.GOAL;
        if ("GraphDefinition".equals(codeString))
          return FHIRResourceType.GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return FHIRResourceType.GROUP;
        if ("GuidanceResponse".equals(codeString))
          return FHIRResourceType.GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return FHIRResourceType.HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return FHIRResourceType.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return FHIRResourceType.IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return FHIRResourceType.IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return FHIRResourceType.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return FHIRResourceType.IMPLEMENTATIONGUIDE;
        if ("Invoice".equals(codeString))
          return FHIRResourceType.INVOICE;
        if ("ItemInstance".equals(codeString))
          return FHIRResourceType.ITEMINSTANCE;
        if ("Library".equals(codeString))
          return FHIRResourceType.LIBRARY;
        if ("Linkage".equals(codeString))
          return FHIRResourceType.LINKAGE;
        if ("List".equals(codeString))
          return FHIRResourceType.LIST;
        if ("Location".equals(codeString))
          return FHIRResourceType.LOCATION;
        if ("Measure".equals(codeString))
          return FHIRResourceType.MEASURE;
        if ("MeasureReport".equals(codeString))
          return FHIRResourceType.MEASUREREPORT;
        if ("Media".equals(codeString))
          return FHIRResourceType.MEDIA;
        if ("Medication".equals(codeString))
          return FHIRResourceType.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return FHIRResourceType.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return FHIRResourceType.MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return FHIRResourceType.MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return FHIRResourceType.MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return FHIRResourceType.MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIngredient".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductPackaged".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return FHIRResourceType.MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MessageDefinition".equals(codeString))
          return FHIRResourceType.MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return FHIRResourceType.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return FHIRResourceType.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return FHIRResourceType.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return FHIRResourceType.OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return FHIRResourceType.OBSERVATIONDEFINITION;
        if ("OccupationalData".equals(codeString))
          return FHIRResourceType.OCCUPATIONALDATA;
        if ("OperationDefinition".equals(codeString))
          return FHIRResourceType.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return FHIRResourceType.OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return FHIRResourceType.ORGANIZATION;
        if ("OrganizationRole".equals(codeString))
          return FHIRResourceType.ORGANIZATIONROLE;
        if ("Parameters".equals(codeString))
          return FHIRResourceType.PARAMETERS;
        if ("Patient".equals(codeString))
          return FHIRResourceType.PATIENT;
        if ("PaymentNotice".equals(codeString))
          return FHIRResourceType.PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return FHIRResourceType.PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return FHIRResourceType.PERSON;
        if ("PlanDefinition".equals(codeString))
          return FHIRResourceType.PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return FHIRResourceType.PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return FHIRResourceType.PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return FHIRResourceType.PROCEDURE;
        if ("ProcessRequest".equals(codeString))
          return FHIRResourceType.PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return FHIRResourceType.PROCESSRESPONSE;
        if ("ProductPlan".equals(codeString))
          return FHIRResourceType.PRODUCTPLAN;
        if ("Provenance".equals(codeString))
          return FHIRResourceType.PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return FHIRResourceType.QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return FHIRResourceType.QUESTIONNAIRERESPONSE;
        if ("RelatedPerson".equals(codeString))
          return FHIRResourceType.RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return FHIRResourceType.REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return FHIRResourceType.RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return FHIRResourceType.RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return FHIRResourceType.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return FHIRResourceType.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return FHIRResourceType.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return FHIRResourceType.SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return FHIRResourceType.SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return FHIRResourceType.SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return FHIRResourceType.SLOT;
        if ("Specimen".equals(codeString))
          return FHIRResourceType.SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return FHIRResourceType.SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return FHIRResourceType.STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return FHIRResourceType.STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return FHIRResourceType.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return FHIRResourceType.SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return FHIRResourceType.SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return FHIRResourceType.SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return FHIRResourceType.SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return FHIRResourceType.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return FHIRResourceType.SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return FHIRResourceType.TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return FHIRResourceType.TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return FHIRResourceType.TESTREPORT;
        if ("TestScript".equals(codeString))
          return FHIRResourceType.TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return FHIRResourceType.USERSESSION;
        if ("ValueSet".equals(codeString))
          return FHIRResourceType.VALUESET;
        if ("VerificationResult".equals(codeString))
          return FHIRResourceType.VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return FHIRResourceType.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown FHIRResourceType code '"+codeString+"'");
        }
        public Enumeration<FHIRResourceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<FHIRResourceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Account".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ACCOUNT);
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ACTIVITYDEFINITION);
        if ("AdverseEvent".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ADVERSEEVENT);
        if ("AllergyIntolerance".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ALLERGYINTOLERANCE);
        if ("Appointment".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.APPOINTMENT);
        if ("AppointmentResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.APPOINTMENTRESPONSE);
        if ("AuditEvent".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.AUDITEVENT);
        if ("Basic".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.BASIC);
        if ("Binary".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.BINARY);
        if ("BiologicallyDerivedProduct".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.BIOLOGICALLYDERIVEDPRODUCT);
        if ("BodyStructure".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.BODYSTRUCTURE);
        if ("Bundle".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.BUNDLE);
        if ("CapabilityStatement".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CAPABILITYSTATEMENT);
        if ("CarePlan".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CAREPLAN);
        if ("CareTeam".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CARETEAM);
        if ("ChargeItem".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CHARGEITEM);
        if ("Claim".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CLAIM);
        if ("ClaimResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CLAIMRESPONSE);
        if ("ClinicalImpression".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CLINICALIMPRESSION);
        if ("CodeSystem".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CODESYSTEM);
        if ("Communication".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.COMMUNICATION);
        if ("CommunicationRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.COMMUNICATIONREQUEST);
        if ("CompartmentDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.COMPARTMENTDEFINITION);
        if ("Composition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.COMPOSITION);
        if ("ConceptMap".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CONCEPTMAP);
        if ("Condition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CONDITION);
        if ("Consent".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CONSENT);
        if ("Contract".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.CONTRACT);
        if ("Coverage".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.COVERAGE);
        if ("DetectedIssue".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DETECTEDISSUE);
        if ("Device".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DEVICE);
        if ("DeviceComponent".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DEVICECOMPONENT);
        if ("DeviceMetric".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DEVICEMETRIC);
        if ("DeviceRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DEVICEREQUEST);
        if ("DeviceUseStatement".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DEVICEUSESTATEMENT);
        if ("DiagnosticReport".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DIAGNOSTICREPORT);
        if ("DocumentManifest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DOCUMENTMANIFEST);
        if ("DocumentReference".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DOCUMENTREFERENCE);
        if ("DomainResource".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.DOMAINRESOURCE);
        if ("EligibilityRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ELIGIBILITYREQUEST);
        if ("EligibilityResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ELIGIBILITYRESPONSE);
        if ("Encounter".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ENCOUNTER);
        if ("Endpoint".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ENDPOINT);
        if ("EnrollmentRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ENROLLMENTREQUEST);
        if ("EnrollmentResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ENROLLMENTRESPONSE);
        if ("EntryDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ENTRYDEFINITION);
        if ("EpisodeOfCare".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.EPISODEOFCARE);
        if ("EventDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.EVENTDEFINITION);
        if ("ExampleScenario".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.EXAMPLESCENARIO);
        if ("ExpansionProfile".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.EXPANSIONPROFILE);
        if ("ExplanationOfBenefit".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.EXPLANATIONOFBENEFIT);
        if ("FamilyMemberHistory".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.FAMILYMEMBERHISTORY);
        if ("Flag".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.FLAG);
        if ("Goal".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.GOAL);
        if ("GraphDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.GRAPHDEFINITION);
        if ("Group".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.GROUP);
        if ("GuidanceResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.GUIDANCERESPONSE);
        if ("HealthcareService".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.HEALTHCARESERVICE);
        if ("ImagingStudy".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.IMAGINGSTUDY);
        if ("Immunization".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.IMMUNIZATION);
        if ("ImmunizationEvaluation".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.IMMUNIZATIONEVALUATION);
        if ("ImmunizationRecommendation".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.IMMUNIZATIONRECOMMENDATION);
        if ("ImplementationGuide".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.IMPLEMENTATIONGUIDE);
        if ("Invoice".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.INVOICE);
        if ("ItemInstance".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ITEMINSTANCE);
        if ("Library".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.LIBRARY);
        if ("Linkage".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.LINKAGE);
        if ("List".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.LIST);
        if ("Location".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.LOCATION);
        if ("Measure".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEASURE);
        if ("MeasureReport".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEASUREREPORT);
        if ("Media".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDIA);
        if ("Medication".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICATION);
        if ("MedicationAdministration".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICATIONADMINISTRATION);
        if ("MedicationDispense".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICATIONDISPENSE);
        if ("MedicationKnowledge".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICATIONKNOWLEDGE);
        if ("MedicationRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICATIONREQUEST);
        if ("MedicationStatement".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICATIONSTATEMENT);
        if ("MedicinalProduct".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCT);
        if ("MedicinalProductAuthorization".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCTAUTHORIZATION);
        if ("MedicinalProductClinicals".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCTCLINICALS);
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCTDEVICESPEC);
        if ("MedicinalProductIngredient".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCTINGREDIENT);
        if ("MedicinalProductPackaged".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCTPACKAGED);
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MEDICINALPRODUCTPHARMACEUTICAL);
        if ("MessageDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MESSAGEDEFINITION);
        if ("MessageHeader".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.MESSAGEHEADER);
        if ("NamingSystem".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.NAMINGSYSTEM);
        if ("NutritionOrder".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.NUTRITIONORDER);
        if ("Observation".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.OBSERVATION);
        if ("ObservationDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.OBSERVATIONDEFINITION);
        if ("OccupationalData".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.OCCUPATIONALDATA);
        if ("OperationDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.OPERATIONDEFINITION);
        if ("OperationOutcome".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.OPERATIONOUTCOME);
        if ("Organization".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ORGANIZATION);
        if ("OrganizationRole".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.ORGANIZATIONROLE);
        if ("Parameters".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PARAMETERS);
        if ("Patient".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PATIENT);
        if ("PaymentNotice".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PAYMENTNOTICE);
        if ("PaymentReconciliation".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PAYMENTRECONCILIATION);
        if ("Person".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PERSON);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PLANDEFINITION);
        if ("Practitioner".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PRACTITIONER);
        if ("PractitionerRole".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PRACTITIONERROLE);
        if ("Procedure".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PROCEDURE);
        if ("ProcessRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PROCESSREQUEST);
        if ("ProcessResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PROCESSRESPONSE);
        if ("ProductPlan".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PRODUCTPLAN);
        if ("Provenance".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.PROVENANCE);
        if ("Questionnaire".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.QUESTIONNAIRE);
        if ("QuestionnaireResponse".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.QUESTIONNAIRERESPONSE);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.RELATEDPERSON);
        if ("RequestGroup".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.REQUESTGROUP);
        if ("ResearchStudy".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.RESEARCHSTUDY);
        if ("ResearchSubject".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.RESEARCHSUBJECT);
        if ("Resource".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.RESOURCE);
        if ("RiskAssessment".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.RISKASSESSMENT);
        if ("Schedule".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SCHEDULE);
        if ("SearchParameter".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SEARCHPARAMETER);
        if ("Sequence".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SEQUENCE);
        if ("ServiceRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SERVICEREQUEST);
        if ("Slot".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SLOT);
        if ("Specimen".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SPECIMEN);
        if ("SpecimenDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SPECIMENDEFINITION);
        if ("StructureDefinition".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.STRUCTUREDEFINITION);
        if ("StructureMap".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.STRUCTUREMAP);
        if ("Subscription".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUBSCRIPTION);
        if ("Substance".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUBSTANCE);
        if ("SubstancePolymer".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUBSTANCEPOLYMER);
        if ("SubstanceReferenceInformation".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUBSTANCEREFERENCEINFORMATION);
        if ("SubstanceSpecification".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUBSTANCESPECIFICATION);
        if ("SupplyDelivery".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUPPLYDELIVERY);
        if ("SupplyRequest".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.SUPPLYREQUEST);
        if ("Task".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.TASK);
        if ("TerminologyCapabilities".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.TERMINOLOGYCAPABILITIES);
        if ("TestReport".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.TESTREPORT);
        if ("TestScript".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.TESTSCRIPT);
        if ("UserSession".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.USERSESSION);
        if ("ValueSet".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.VALUESET);
        if ("VerificationResult".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.VERIFICATIONRESULT);
        if ("VisionPrescription".equals(codeString))
          return new Enumeration<FHIRResourceType>(this, FHIRResourceType.VISIONPRESCRIPTION);
        throw new FHIRException("Unknown FHIRResourceType code '"+codeString+"'");
        }
    public String toCode(FHIRResourceType code) {
      if (code == FHIRResourceType.ACCOUNT)
        return "Account";
      if (code == FHIRResourceType.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == FHIRResourceType.ADVERSEEVENT)
        return "AdverseEvent";
      if (code == FHIRResourceType.ALLERGYINTOLERANCE)
        return "AllergyIntolerance";
      if (code == FHIRResourceType.APPOINTMENT)
        return "Appointment";
      if (code == FHIRResourceType.APPOINTMENTRESPONSE)
        return "AppointmentResponse";
      if (code == FHIRResourceType.AUDITEVENT)
        return "AuditEvent";
      if (code == FHIRResourceType.BASIC)
        return "Basic";
      if (code == FHIRResourceType.BINARY)
        return "Binary";
      if (code == FHIRResourceType.BIOLOGICALLYDERIVEDPRODUCT)
        return "BiologicallyDerivedProduct";
      if (code == FHIRResourceType.BODYSTRUCTURE)
        return "BodyStructure";
      if (code == FHIRResourceType.BUNDLE)
        return "Bundle";
      if (code == FHIRResourceType.CAPABILITYSTATEMENT)
        return "CapabilityStatement";
      if (code == FHIRResourceType.CAREPLAN)
        return "CarePlan";
      if (code == FHIRResourceType.CARETEAM)
        return "CareTeam";
      if (code == FHIRResourceType.CHARGEITEM)
        return "ChargeItem";
      if (code == FHIRResourceType.CLAIM)
        return "Claim";
      if (code == FHIRResourceType.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == FHIRResourceType.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == FHIRResourceType.CODESYSTEM)
        return "CodeSystem";
      if (code == FHIRResourceType.COMMUNICATION)
        return "Communication";
      if (code == FHIRResourceType.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == FHIRResourceType.COMPARTMENTDEFINITION)
        return "CompartmentDefinition";
      if (code == FHIRResourceType.COMPOSITION)
        return "Composition";
      if (code == FHIRResourceType.CONCEPTMAP)
        return "ConceptMap";
      if (code == FHIRResourceType.CONDITION)
        return "Condition";
      if (code == FHIRResourceType.CONSENT)
        return "Consent";
      if (code == FHIRResourceType.CONTRACT)
        return "Contract";
      if (code == FHIRResourceType.COVERAGE)
        return "Coverage";
      if (code == FHIRResourceType.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == FHIRResourceType.DEVICE)
        return "Device";
      if (code == FHIRResourceType.DEVICECOMPONENT)
        return "DeviceComponent";
      if (code == FHIRResourceType.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == FHIRResourceType.DEVICEREQUEST)
        return "DeviceRequest";
      if (code == FHIRResourceType.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == FHIRResourceType.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == FHIRResourceType.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == FHIRResourceType.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == FHIRResourceType.DOMAINRESOURCE)
        return "DomainResource";
      if (code == FHIRResourceType.ELIGIBILITYREQUEST)
        return "EligibilityRequest";
      if (code == FHIRResourceType.ELIGIBILITYRESPONSE)
        return "EligibilityResponse";
      if (code == FHIRResourceType.ENCOUNTER)
        return "Encounter";
      if (code == FHIRResourceType.ENDPOINT)
        return "Endpoint";
      if (code == FHIRResourceType.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == FHIRResourceType.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == FHIRResourceType.ENTRYDEFINITION)
        return "EntryDefinition";
      if (code == FHIRResourceType.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == FHIRResourceType.EVENTDEFINITION)
        return "EventDefinition";
      if (code == FHIRResourceType.EXAMPLESCENARIO)
        return "ExampleScenario";
      if (code == FHIRResourceType.EXPANSIONPROFILE)
        return "ExpansionProfile";
      if (code == FHIRResourceType.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == FHIRResourceType.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == FHIRResourceType.FLAG)
        return "Flag";
      if (code == FHIRResourceType.GOAL)
        return "Goal";
      if (code == FHIRResourceType.GRAPHDEFINITION)
        return "GraphDefinition";
      if (code == FHIRResourceType.GROUP)
        return "Group";
      if (code == FHIRResourceType.GUIDANCERESPONSE)
        return "GuidanceResponse";
      if (code == FHIRResourceType.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == FHIRResourceType.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == FHIRResourceType.IMMUNIZATION)
        return "Immunization";
      if (code == FHIRResourceType.IMMUNIZATIONEVALUATION)
        return "ImmunizationEvaluation";
      if (code == FHIRResourceType.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == FHIRResourceType.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == FHIRResourceType.INVOICE)
        return "Invoice";
      if (code == FHIRResourceType.ITEMINSTANCE)
        return "ItemInstance";
      if (code == FHIRResourceType.LIBRARY)
        return "Library";
      if (code == FHIRResourceType.LINKAGE)
        return "Linkage";
      if (code == FHIRResourceType.LIST)
        return "List";
      if (code == FHIRResourceType.LOCATION)
        return "Location";
      if (code == FHIRResourceType.MEASURE)
        return "Measure";
      if (code == FHIRResourceType.MEASUREREPORT)
        return "MeasureReport";
      if (code == FHIRResourceType.MEDIA)
        return "Media";
      if (code == FHIRResourceType.MEDICATION)
        return "Medication";
      if (code == FHIRResourceType.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == FHIRResourceType.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == FHIRResourceType.MEDICATIONKNOWLEDGE)
        return "MedicationKnowledge";
      if (code == FHIRResourceType.MEDICATIONREQUEST)
        return "MedicationRequest";
      if (code == FHIRResourceType.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == FHIRResourceType.MEDICINALPRODUCT)
        return "MedicinalProduct";
      if (code == FHIRResourceType.MEDICINALPRODUCTAUTHORIZATION)
        return "MedicinalProductAuthorization";
      if (code == FHIRResourceType.MEDICINALPRODUCTCLINICALS)
        return "MedicinalProductClinicals";
      if (code == FHIRResourceType.MEDICINALPRODUCTDEVICESPEC)
        return "MedicinalProductDeviceSpec";
      if (code == FHIRResourceType.MEDICINALPRODUCTINGREDIENT)
        return "MedicinalProductIngredient";
      if (code == FHIRResourceType.MEDICINALPRODUCTPACKAGED)
        return "MedicinalProductPackaged";
      if (code == FHIRResourceType.MEDICINALPRODUCTPHARMACEUTICAL)
        return "MedicinalProductPharmaceutical";
      if (code == FHIRResourceType.MESSAGEDEFINITION)
        return "MessageDefinition";
      if (code == FHIRResourceType.MESSAGEHEADER)
        return "MessageHeader";
      if (code == FHIRResourceType.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == FHIRResourceType.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == FHIRResourceType.OBSERVATION)
        return "Observation";
      if (code == FHIRResourceType.OBSERVATIONDEFINITION)
        return "ObservationDefinition";
      if (code == FHIRResourceType.OCCUPATIONALDATA)
        return "OccupationalData";
      if (code == FHIRResourceType.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == FHIRResourceType.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == FHIRResourceType.ORGANIZATION)
        return "Organization";
      if (code == FHIRResourceType.ORGANIZATIONROLE)
        return "OrganizationRole";
      if (code == FHIRResourceType.PARAMETERS)
        return "Parameters";
      if (code == FHIRResourceType.PATIENT)
        return "Patient";
      if (code == FHIRResourceType.PAYMENTNOTICE)
        return "PaymentNotice";
      if (code == FHIRResourceType.PAYMENTRECONCILIATION)
        return "PaymentReconciliation";
      if (code == FHIRResourceType.PERSON)
        return "Person";
      if (code == FHIRResourceType.PLANDEFINITION)
        return "PlanDefinition";
      if (code == FHIRResourceType.PRACTITIONER)
        return "Practitioner";
      if (code == FHIRResourceType.PRACTITIONERROLE)
        return "PractitionerRole";
      if (code == FHIRResourceType.PROCEDURE)
        return "Procedure";
      if (code == FHIRResourceType.PROCESSREQUEST)
        return "ProcessRequest";
      if (code == FHIRResourceType.PROCESSRESPONSE)
        return "ProcessResponse";
      if (code == FHIRResourceType.PRODUCTPLAN)
        return "ProductPlan";
      if (code == FHIRResourceType.PROVENANCE)
        return "Provenance";
      if (code == FHIRResourceType.QUESTIONNAIRE)
        return "Questionnaire";
      if (code == FHIRResourceType.QUESTIONNAIRERESPONSE)
        return "QuestionnaireResponse";
      if (code == FHIRResourceType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == FHIRResourceType.REQUESTGROUP)
        return "RequestGroup";
      if (code == FHIRResourceType.RESEARCHSTUDY)
        return "ResearchStudy";
      if (code == FHIRResourceType.RESEARCHSUBJECT)
        return "ResearchSubject";
      if (code == FHIRResourceType.RESOURCE)
        return "Resource";
      if (code == FHIRResourceType.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == FHIRResourceType.SCHEDULE)
        return "Schedule";
      if (code == FHIRResourceType.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == FHIRResourceType.SEQUENCE)
        return "Sequence";
      if (code == FHIRResourceType.SERVICEREQUEST)
        return "ServiceRequest";
      if (code == FHIRResourceType.SLOT)
        return "Slot";
      if (code == FHIRResourceType.SPECIMEN)
        return "Specimen";
      if (code == FHIRResourceType.SPECIMENDEFINITION)
        return "SpecimenDefinition";
      if (code == FHIRResourceType.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == FHIRResourceType.STRUCTUREMAP)
        return "StructureMap";
      if (code == FHIRResourceType.SUBSCRIPTION)
        return "Subscription";
      if (code == FHIRResourceType.SUBSTANCE)
        return "Substance";
      if (code == FHIRResourceType.SUBSTANCEPOLYMER)
        return "SubstancePolymer";
      if (code == FHIRResourceType.SUBSTANCEREFERENCEINFORMATION)
        return "SubstanceReferenceInformation";
      if (code == FHIRResourceType.SUBSTANCESPECIFICATION)
        return "SubstanceSpecification";
      if (code == FHIRResourceType.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == FHIRResourceType.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == FHIRResourceType.TASK)
        return "Task";
      if (code == FHIRResourceType.TERMINOLOGYCAPABILITIES)
        return "TerminologyCapabilities";
      if (code == FHIRResourceType.TESTREPORT)
        return "TestReport";
      if (code == FHIRResourceType.TESTSCRIPT)
        return "TestScript";
      if (code == FHIRResourceType.USERSESSION)
        return "UserSession";
      if (code == FHIRResourceType.VALUESET)
        return "ValueSet";
      if (code == FHIRResourceType.VERIFICATIONRESULT)
        return "VerificationResult";
      if (code == FHIRResourceType.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    public String toSystem(FHIRResourceType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ExampleScenarioActorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * ID or acronym of actor.
         */
        @Child(name = "actorId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="ID or acronym of the actor", formalDefinition="ID or acronym of actor." )
        protected StringType actorId;

        /**
         * The type of actor - person or system.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="person | entity", formalDefinition="The type of actor - person or system." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/examplescenario-actor-type")
        protected Enumeration<ExampleScenarioActorType> type;

        /**
         * The name of the actor as shown in the page.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The name of the actor as shown in the page", formalDefinition="The name of the actor as shown in the page." )
        protected StringType name;

        /**
         * The description of the actor.
         */
        @Child(name = "description", type = {MarkdownType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The description of the actor", formalDefinition="The description of the actor." )
        protected MarkdownType description;

        private static final long serialVersionUID = 1348364162L;

    /**
     * Constructor
     */
      public ExampleScenarioActorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioActorComponent(StringType actorId, Enumeration<ExampleScenarioActorType> type) {
        super();
        this.actorId = actorId;
        this.type = type;
      }

        /**
         * @return {@link #actorId} (ID or acronym of actor.). This is the underlying object with id, value and extensions. The accessor "getActorId" gives direct access to the value
         */
        public StringType getActorIdElement() { 
          if (this.actorId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioActorComponent.actorId");
            else if (Configuration.doAutoCreate())
              this.actorId = new StringType(); // bb
          return this.actorId;
        }

        public boolean hasActorIdElement() { 
          return this.actorId != null && !this.actorId.isEmpty();
        }

        public boolean hasActorId() { 
          return this.actorId != null && !this.actorId.isEmpty();
        }

        /**
         * @param value {@link #actorId} (ID or acronym of actor.). This is the underlying object with id, value and extensions. The accessor "getActorId" gives direct access to the value
         */
        public ExampleScenarioActorComponent setActorIdElement(StringType value) { 
          this.actorId = value;
          return this;
        }

        /**
         * @return ID or acronym of actor.
         */
        public String getActorId() { 
          return this.actorId == null ? null : this.actorId.getValue();
        }

        /**
         * @param value ID or acronym of actor.
         */
        public ExampleScenarioActorComponent setActorId(String value) { 
            if (this.actorId == null)
              this.actorId = new StringType();
            this.actorId.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of actor - person or system.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ExampleScenarioActorType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioActorComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ExampleScenarioActorType>(new ExampleScenarioActorTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of actor - person or system.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ExampleScenarioActorComponent setTypeElement(Enumeration<ExampleScenarioActorType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of actor - person or system.
         */
        public ExampleScenarioActorType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of actor - person or system.
         */
        public ExampleScenarioActorComponent setType(ExampleScenarioActorType value) { 
            if (this.type == null)
              this.type = new Enumeration<ExampleScenarioActorType>(new ExampleScenarioActorTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (The name of the actor as shown in the page.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioActorComponent.name");
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
         * @param value {@link #name} (The name of the actor as shown in the page.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ExampleScenarioActorComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the actor as shown in the page.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the actor as shown in the page.
         */
        public ExampleScenarioActorComponent setName(String value) { 
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
         * @return {@link #description} (The description of the actor.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MarkdownType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioActorComponent.description");
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
         * @param value {@link #description} (The description of the actor.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ExampleScenarioActorComponent setDescriptionElement(MarkdownType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The description of the actor.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The description of the actor.
         */
        public ExampleScenarioActorComponent setDescription(String value) { 
          if (value == null)
            this.description = null;
          else {
            if (this.description == null)
              this.description = new MarkdownType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("actorId", "string", "ID or acronym of actor.", 0, 1, actorId));
          children.add(new Property("type", "code", "The type of actor - person or system.", 0, 1, type));
          children.add(new Property("name", "string", "The name of the actor as shown in the page.", 0, 1, name));
          children.add(new Property("description", "markdown", "The description of the actor.", 0, 1, description));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1161623056: /*actorId*/  return new Property("actorId", "string", "ID or acronym of actor.", 0, 1, actorId);
          case 3575610: /*type*/  return new Property("type", "code", "The type of actor - person or system.", 0, 1, type);
          case 3373707: /*name*/  return new Property("name", "string", "The name of the actor as shown in the page.", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "markdown", "The description of the actor.", 0, 1, description);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1161623056: /*actorId*/ return this.actorId == null ? new Base[0] : new Base[] {this.actorId}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ExampleScenarioActorType>
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1161623056: // actorId
          this.actorId = castToString(value); // StringType
          return value;
        case 3575610: // type
          value = new ExampleScenarioActorTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ExampleScenarioActorType>
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actorId")) {
          this.actorId = castToString(value); // StringType
        } else if (name.equals("type")) {
          value = new ExampleScenarioActorTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ExampleScenarioActorType>
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1161623056:  return getActorIdElement();
        case 3575610:  return getTypeElement();
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1161623056: /*actorId*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actorId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.actorId");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.type");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.description");
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioActorComponent copy() {
        ExampleScenarioActorComponent dst = new ExampleScenarioActorComponent();
        copyValues(dst);
        dst.actorId = actorId == null ? null : actorId.copy();
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioActorComponent))
          return false;
        ExampleScenarioActorComponent o = (ExampleScenarioActorComponent) other_;
        return compareDeep(actorId, o.actorId, true) && compareDeep(type, o.type, true) && compareDeep(name, o.name, true)
           && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioActorComponent))
          return false;
        ExampleScenarioActorComponent o = (ExampleScenarioActorComponent) other_;
        return compareValues(actorId, o.actorId, true) && compareValues(type, o.type, true) && compareValues(name, o.name, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actorId, type, name, description
          );
      }

  public String fhirType() {
    return "ExampleScenario.actor";

  }

  }

    @Block()
    public static class ExampleScenarioInstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The id of the resource for referencing.
         */
        @Child(name = "resourceId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The id of the resource for referencing", formalDefinition="The id of the resource for referencing." )
        protected StringType resourceId;

        /**
         * The type of the resource.
         */
        @Child(name = "resourceType", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of the resource", formalDefinition="The type of the resource." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected Enumeration<FHIRResourceType> resourceType;

        /**
         * A short name for the resource instance.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A short name for the resource instance", formalDefinition="A short name for the resource instance." )
        protected StringType name;

        /**
         * Human-friendly description of the resource instance.
         */
        @Child(name = "description", type = {MarkdownType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human-friendly description of the resource instance", formalDefinition="Human-friendly description of the resource instance." )
        protected MarkdownType description;

        /**
         * A specific version of the resource.
         */
        @Child(name = "version", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A specific version of the resource", formalDefinition="A specific version of the resource." )
        protected List<ExampleScenarioInstanceVersionComponent> version;

        /**
         * Resources contained in the instance (e.g. the observations contained in a bundle).
         */
        @Child(name = "containedInstance", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Resources contained in the instance", formalDefinition="Resources contained in the instance (e.g. the observations contained in a bundle)." )
        protected List<ExampleScenarioInstanceContainedInstanceComponent> containedInstance;

        private static final long serialVersionUID = -1131860669L;

    /**
     * Constructor
     */
      public ExampleScenarioInstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioInstanceComponent(StringType resourceId, Enumeration<FHIRResourceType> resourceType) {
        super();
        this.resourceId = resourceId;
        this.resourceType = resourceType;
      }

        /**
         * @return {@link #resourceId} (The id of the resource for referencing.). This is the underlying object with id, value and extensions. The accessor "getResourceId" gives direct access to the value
         */
        public StringType getResourceIdElement() { 
          if (this.resourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceComponent.resourceId");
            else if (Configuration.doAutoCreate())
              this.resourceId = new StringType(); // bb
          return this.resourceId;
        }

        public boolean hasResourceIdElement() { 
          return this.resourceId != null && !this.resourceId.isEmpty();
        }

        public boolean hasResourceId() { 
          return this.resourceId != null && !this.resourceId.isEmpty();
        }

        /**
         * @param value {@link #resourceId} (The id of the resource for referencing.). This is the underlying object with id, value and extensions. The accessor "getResourceId" gives direct access to the value
         */
        public ExampleScenarioInstanceComponent setResourceIdElement(StringType value) { 
          this.resourceId = value;
          return this;
        }

        /**
         * @return The id of the resource for referencing.
         */
        public String getResourceId() { 
          return this.resourceId == null ? null : this.resourceId.getValue();
        }

        /**
         * @param value The id of the resource for referencing.
         */
        public ExampleScenarioInstanceComponent setResourceId(String value) { 
            if (this.resourceId == null)
              this.resourceId = new StringType();
            this.resourceId.setValue(value);
          return this;
        }

        /**
         * @return {@link #resourceType} (The type of the resource.). This is the underlying object with id, value and extensions. The accessor "getResourceType" gives direct access to the value
         */
        public Enumeration<FHIRResourceType> getResourceTypeElement() { 
          if (this.resourceType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceComponent.resourceType");
            else if (Configuration.doAutoCreate())
              this.resourceType = new Enumeration<FHIRResourceType>(new FHIRResourceTypeEnumFactory()); // bb
          return this.resourceType;
        }

        public boolean hasResourceTypeElement() { 
          return this.resourceType != null && !this.resourceType.isEmpty();
        }

        public boolean hasResourceType() { 
          return this.resourceType != null && !this.resourceType.isEmpty();
        }

        /**
         * @param value {@link #resourceType} (The type of the resource.). This is the underlying object with id, value and extensions. The accessor "getResourceType" gives direct access to the value
         */
        public ExampleScenarioInstanceComponent setResourceTypeElement(Enumeration<FHIRResourceType> value) { 
          this.resourceType = value;
          return this;
        }

        /**
         * @return The type of the resource.
         */
        public FHIRResourceType getResourceType() { 
          return this.resourceType == null ? null : this.resourceType.getValue();
        }

        /**
         * @param value The type of the resource.
         */
        public ExampleScenarioInstanceComponent setResourceType(FHIRResourceType value) { 
            if (this.resourceType == null)
              this.resourceType = new Enumeration<FHIRResourceType>(new FHIRResourceTypeEnumFactory());
            this.resourceType.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (A short name for the resource instance.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceComponent.name");
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
         * @param value {@link #name} (A short name for the resource instance.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ExampleScenarioInstanceComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A short name for the resource instance.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A short name for the resource instance.
         */
        public ExampleScenarioInstanceComponent setName(String value) { 
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
         * @return {@link #description} (Human-friendly description of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MarkdownType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceComponent.description");
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
         * @param value {@link #description} (Human-friendly description of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ExampleScenarioInstanceComponent setDescriptionElement(MarkdownType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human-friendly description of the resource instance.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human-friendly description of the resource instance.
         */
        public ExampleScenarioInstanceComponent setDescription(String value) { 
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
         * @return {@link #version} (A specific version of the resource.)
         */
        public List<ExampleScenarioInstanceVersionComponent> getVersion() { 
          if (this.version == null)
            this.version = new ArrayList<ExampleScenarioInstanceVersionComponent>();
          return this.version;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioInstanceComponent setVersion(List<ExampleScenarioInstanceVersionComponent> theVersion) { 
          this.version = theVersion;
          return this;
        }

        public boolean hasVersion() { 
          if (this.version == null)
            return false;
          for (ExampleScenarioInstanceVersionComponent item : this.version)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExampleScenarioInstanceVersionComponent addVersion() { //3
          ExampleScenarioInstanceVersionComponent t = new ExampleScenarioInstanceVersionComponent();
          if (this.version == null)
            this.version = new ArrayList<ExampleScenarioInstanceVersionComponent>();
          this.version.add(t);
          return t;
        }

        public ExampleScenarioInstanceComponent addVersion(ExampleScenarioInstanceVersionComponent t) { //3
          if (t == null)
            return this;
          if (this.version == null)
            this.version = new ArrayList<ExampleScenarioInstanceVersionComponent>();
          this.version.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #version}, creating it if it does not already exist
         */
        public ExampleScenarioInstanceVersionComponent getVersionFirstRep() { 
          if (getVersion().isEmpty()) {
            addVersion();
          }
          return getVersion().get(0);
        }

        /**
         * @return {@link #containedInstance} (Resources contained in the instance (e.g. the observations contained in a bundle).)
         */
        public List<ExampleScenarioInstanceContainedInstanceComponent> getContainedInstance() { 
          if (this.containedInstance == null)
            this.containedInstance = new ArrayList<ExampleScenarioInstanceContainedInstanceComponent>();
          return this.containedInstance;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioInstanceComponent setContainedInstance(List<ExampleScenarioInstanceContainedInstanceComponent> theContainedInstance) { 
          this.containedInstance = theContainedInstance;
          return this;
        }

        public boolean hasContainedInstance() { 
          if (this.containedInstance == null)
            return false;
          for (ExampleScenarioInstanceContainedInstanceComponent item : this.containedInstance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExampleScenarioInstanceContainedInstanceComponent addContainedInstance() { //3
          ExampleScenarioInstanceContainedInstanceComponent t = new ExampleScenarioInstanceContainedInstanceComponent();
          if (this.containedInstance == null)
            this.containedInstance = new ArrayList<ExampleScenarioInstanceContainedInstanceComponent>();
          this.containedInstance.add(t);
          return t;
        }

        public ExampleScenarioInstanceComponent addContainedInstance(ExampleScenarioInstanceContainedInstanceComponent t) { //3
          if (t == null)
            return this;
          if (this.containedInstance == null)
            this.containedInstance = new ArrayList<ExampleScenarioInstanceContainedInstanceComponent>();
          this.containedInstance.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #containedInstance}, creating it if it does not already exist
         */
        public ExampleScenarioInstanceContainedInstanceComponent getContainedInstanceFirstRep() { 
          if (getContainedInstance().isEmpty()) {
            addContainedInstance();
          }
          return getContainedInstance().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("resourceId", "string", "The id of the resource for referencing.", 0, 1, resourceId));
          children.add(new Property("resourceType", "code", "The type of the resource.", 0, 1, resourceType));
          children.add(new Property("name", "string", "A short name for the resource instance.", 0, 1, name));
          children.add(new Property("description", "markdown", "Human-friendly description of the resource instance.", 0, 1, description));
          children.add(new Property("version", "", "A specific version of the resource.", 0, java.lang.Integer.MAX_VALUE, version));
          children.add(new Property("containedInstance", "", "Resources contained in the instance (e.g. the observations contained in a bundle).", 0, java.lang.Integer.MAX_VALUE, containedInstance));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1345650231: /*resourceId*/  return new Property("resourceId", "string", "The id of the resource for referencing.", 0, 1, resourceId);
          case -384364440: /*resourceType*/  return new Property("resourceType", "code", "The type of the resource.", 0, 1, resourceType);
          case 3373707: /*name*/  return new Property("name", "string", "A short name for the resource instance.", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "markdown", "Human-friendly description of the resource instance.", 0, 1, description);
          case 351608024: /*version*/  return new Property("version", "", "A specific version of the resource.", 0, java.lang.Integer.MAX_VALUE, version);
          case -417062360: /*containedInstance*/  return new Property("containedInstance", "", "Resources contained in the instance (e.g. the observations contained in a bundle).", 0, java.lang.Integer.MAX_VALUE, containedInstance);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1345650231: /*resourceId*/ return this.resourceId == null ? new Base[0] : new Base[] {this.resourceId}; // StringType
        case -384364440: /*resourceType*/ return this.resourceType == null ? new Base[0] : new Base[] {this.resourceType}; // Enumeration<FHIRResourceType>
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : this.version.toArray(new Base[this.version.size()]); // ExampleScenarioInstanceVersionComponent
        case -417062360: /*containedInstance*/ return this.containedInstance == null ? new Base[0] : this.containedInstance.toArray(new Base[this.containedInstance.size()]); // ExampleScenarioInstanceContainedInstanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1345650231: // resourceId
          this.resourceId = castToString(value); // StringType
          return value;
        case -384364440: // resourceType
          value = new FHIRResourceTypeEnumFactory().fromType(castToCode(value));
          this.resourceType = (Enumeration) value; // Enumeration<FHIRResourceType>
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case 351608024: // version
          this.getVersion().add((ExampleScenarioInstanceVersionComponent) value); // ExampleScenarioInstanceVersionComponent
          return value;
        case -417062360: // containedInstance
          this.getContainedInstance().add((ExampleScenarioInstanceContainedInstanceComponent) value); // ExampleScenarioInstanceContainedInstanceComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("resourceId")) {
          this.resourceId = castToString(value); // StringType
        } else if (name.equals("resourceType")) {
          value = new FHIRResourceTypeEnumFactory().fromType(castToCode(value));
          this.resourceType = (Enumeration) value; // Enumeration<FHIRResourceType>
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("version")) {
          this.getVersion().add((ExampleScenarioInstanceVersionComponent) value);
        } else if (name.equals("containedInstance")) {
          this.getContainedInstance().add((ExampleScenarioInstanceContainedInstanceComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1345650231:  return getResourceIdElement();
        case -384364440:  return getResourceTypeElement();
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case 351608024:  return addVersion(); 
        case -417062360:  return addContainedInstance(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1345650231: /*resourceId*/ return new String[] {"string"};
        case -384364440: /*resourceType*/ return new String[] {"code"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 351608024: /*version*/ return new String[] {};
        case -417062360: /*containedInstance*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("resourceId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.resourceId");
        }
        else if (name.equals("resourceType")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.resourceType");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.description");
        }
        else if (name.equals("version")) {
          return addVersion();
        }
        else if (name.equals("containedInstance")) {
          return addContainedInstance();
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioInstanceComponent copy() {
        ExampleScenarioInstanceComponent dst = new ExampleScenarioInstanceComponent();
        copyValues(dst);
        dst.resourceId = resourceId == null ? null : resourceId.copy();
        dst.resourceType = resourceType == null ? null : resourceType.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        if (version != null) {
          dst.version = new ArrayList<ExampleScenarioInstanceVersionComponent>();
          for (ExampleScenarioInstanceVersionComponent i : version)
            dst.version.add(i.copy());
        };
        if (containedInstance != null) {
          dst.containedInstance = new ArrayList<ExampleScenarioInstanceContainedInstanceComponent>();
          for (ExampleScenarioInstanceContainedInstanceComponent i : containedInstance)
            dst.containedInstance.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioInstanceComponent))
          return false;
        ExampleScenarioInstanceComponent o = (ExampleScenarioInstanceComponent) other_;
        return compareDeep(resourceId, o.resourceId, true) && compareDeep(resourceType, o.resourceType, true)
           && compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(version, o.version, true)
           && compareDeep(containedInstance, o.containedInstance, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioInstanceComponent))
          return false;
        ExampleScenarioInstanceComponent o = (ExampleScenarioInstanceComponent) other_;
        return compareValues(resourceId, o.resourceId, true) && compareValues(resourceType, o.resourceType, true)
           && compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(resourceId, resourceType, name
          , description, version, containedInstance);
      }

  public String fhirType() {
    return "ExampleScenario.instance";

  }

  }

    @Block()
    public static class ExampleScenarioInstanceVersionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier of a specific version of a resource.
         */
        @Child(name = "versionId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The identifier of a specific version of a resource", formalDefinition="The identifier of a specific version of a resource." )
        protected StringType versionId;

        /**
         * The description of the resource version.
         */
        @Child(name = "description", type = {MarkdownType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The description of the resource version", formalDefinition="The description of the resource version." )
        protected MarkdownType description;

        private static final long serialVersionUID = 960821913L;

    /**
     * Constructor
     */
      public ExampleScenarioInstanceVersionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioInstanceVersionComponent(StringType versionId, MarkdownType description) {
        super();
        this.versionId = versionId;
        this.description = description;
      }

        /**
         * @return {@link #versionId} (The identifier of a specific version of a resource.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public StringType getVersionIdElement() { 
          if (this.versionId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceVersionComponent.versionId");
            else if (Configuration.doAutoCreate())
              this.versionId = new StringType(); // bb
          return this.versionId;
        }

        public boolean hasVersionIdElement() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        public boolean hasVersionId() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        /**
         * @param value {@link #versionId} (The identifier of a specific version of a resource.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public ExampleScenarioInstanceVersionComponent setVersionIdElement(StringType value) { 
          this.versionId = value;
          return this;
        }

        /**
         * @return The identifier of a specific version of a resource.
         */
        public String getVersionId() { 
          return this.versionId == null ? null : this.versionId.getValue();
        }

        /**
         * @param value The identifier of a specific version of a resource.
         */
        public ExampleScenarioInstanceVersionComponent setVersionId(String value) { 
            if (this.versionId == null)
              this.versionId = new StringType();
            this.versionId.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (The description of the resource version.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MarkdownType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceVersionComponent.description");
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
         * @param value {@link #description} (The description of the resource version.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ExampleScenarioInstanceVersionComponent setDescriptionElement(MarkdownType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The description of the resource version.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The description of the resource version.
         */
        public ExampleScenarioInstanceVersionComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new MarkdownType();
            this.description.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("versionId", "string", "The identifier of a specific version of a resource.", 0, 1, versionId));
          children.add(new Property("description", "markdown", "The description of the resource version.", 0, 1, description));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1407102957: /*versionId*/  return new Property("versionId", "string", "The identifier of a specific version of a resource.", 0, 1, versionId);
          case -1724546052: /*description*/  return new Property("description", "markdown", "The description of the resource version.", 0, 1, description);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1407102957: /*versionId*/ return this.versionId == null ? new Base[0] : new Base[] {this.versionId}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1407102957: // versionId
          this.versionId = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("versionId")) {
          this.versionId = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1407102957:  return getVersionIdElement();
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1407102957: /*versionId*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("versionId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.versionId");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.description");
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioInstanceVersionComponent copy() {
        ExampleScenarioInstanceVersionComponent dst = new ExampleScenarioInstanceVersionComponent();
        copyValues(dst);
        dst.versionId = versionId == null ? null : versionId.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioInstanceVersionComponent))
          return false;
        ExampleScenarioInstanceVersionComponent o = (ExampleScenarioInstanceVersionComponent) other_;
        return compareDeep(versionId, o.versionId, true) && compareDeep(description, o.description, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioInstanceVersionComponent))
          return false;
        ExampleScenarioInstanceVersionComponent o = (ExampleScenarioInstanceVersionComponent) other_;
        return compareValues(versionId, o.versionId, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(versionId, description);
      }

  public String fhirType() {
    return "ExampleScenario.instance.version";

  }

  }

    @Block()
    public static class ExampleScenarioInstanceContainedInstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Each resource contained in the instance.
         */
        @Child(name = "resourceId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Each resource contained in the instance", formalDefinition="Each resource contained in the instance." )
        protected StringType resourceId;

        /**
         * A specific version of a resource contained in the instance.
         */
        @Child(name = "versionId", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A specific version of a resource contained in the instance", formalDefinition="A specific version of a resource contained in the instance." )
        protected StringType versionId;

        private static final long serialVersionUID = 908084124L;

    /**
     * Constructor
     */
      public ExampleScenarioInstanceContainedInstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioInstanceContainedInstanceComponent(StringType resourceId) {
        super();
        this.resourceId = resourceId;
      }

        /**
         * @return {@link #resourceId} (Each resource contained in the instance.). This is the underlying object with id, value and extensions. The accessor "getResourceId" gives direct access to the value
         */
        public StringType getResourceIdElement() { 
          if (this.resourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceContainedInstanceComponent.resourceId");
            else if (Configuration.doAutoCreate())
              this.resourceId = new StringType(); // bb
          return this.resourceId;
        }

        public boolean hasResourceIdElement() { 
          return this.resourceId != null && !this.resourceId.isEmpty();
        }

        public boolean hasResourceId() { 
          return this.resourceId != null && !this.resourceId.isEmpty();
        }

        /**
         * @param value {@link #resourceId} (Each resource contained in the instance.). This is the underlying object with id, value and extensions. The accessor "getResourceId" gives direct access to the value
         */
        public ExampleScenarioInstanceContainedInstanceComponent setResourceIdElement(StringType value) { 
          this.resourceId = value;
          return this;
        }

        /**
         * @return Each resource contained in the instance.
         */
        public String getResourceId() { 
          return this.resourceId == null ? null : this.resourceId.getValue();
        }

        /**
         * @param value Each resource contained in the instance.
         */
        public ExampleScenarioInstanceContainedInstanceComponent setResourceId(String value) { 
            if (this.resourceId == null)
              this.resourceId = new StringType();
            this.resourceId.setValue(value);
          return this;
        }

        /**
         * @return {@link #versionId} (A specific version of a resource contained in the instance.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public StringType getVersionIdElement() { 
          if (this.versionId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioInstanceContainedInstanceComponent.versionId");
            else if (Configuration.doAutoCreate())
              this.versionId = new StringType(); // bb
          return this.versionId;
        }

        public boolean hasVersionIdElement() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        public boolean hasVersionId() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        /**
         * @param value {@link #versionId} (A specific version of a resource contained in the instance.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public ExampleScenarioInstanceContainedInstanceComponent setVersionIdElement(StringType value) { 
          this.versionId = value;
          return this;
        }

        /**
         * @return A specific version of a resource contained in the instance.
         */
        public String getVersionId() { 
          return this.versionId == null ? null : this.versionId.getValue();
        }

        /**
         * @param value A specific version of a resource contained in the instance.
         */
        public ExampleScenarioInstanceContainedInstanceComponent setVersionId(String value) { 
          if (Utilities.noString(value))
            this.versionId = null;
          else {
            if (this.versionId == null)
              this.versionId = new StringType();
            this.versionId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("resourceId", "string", "Each resource contained in the instance.", 0, 1, resourceId));
          children.add(new Property("versionId", "string", "A specific version of a resource contained in the instance.", 0, 1, versionId));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1345650231: /*resourceId*/  return new Property("resourceId", "string", "Each resource contained in the instance.", 0, 1, resourceId);
          case -1407102957: /*versionId*/  return new Property("versionId", "string", "A specific version of a resource contained in the instance.", 0, 1, versionId);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1345650231: /*resourceId*/ return this.resourceId == null ? new Base[0] : new Base[] {this.resourceId}; // StringType
        case -1407102957: /*versionId*/ return this.versionId == null ? new Base[0] : new Base[] {this.versionId}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1345650231: // resourceId
          this.resourceId = castToString(value); // StringType
          return value;
        case -1407102957: // versionId
          this.versionId = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("resourceId")) {
          this.resourceId = castToString(value); // StringType
        } else if (name.equals("versionId")) {
          this.versionId = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1345650231:  return getResourceIdElement();
        case -1407102957:  return getVersionIdElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1345650231: /*resourceId*/ return new String[] {"string"};
        case -1407102957: /*versionId*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("resourceId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.resourceId");
        }
        else if (name.equals("versionId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.versionId");
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioInstanceContainedInstanceComponent copy() {
        ExampleScenarioInstanceContainedInstanceComponent dst = new ExampleScenarioInstanceContainedInstanceComponent();
        copyValues(dst);
        dst.resourceId = resourceId == null ? null : resourceId.copy();
        dst.versionId = versionId == null ? null : versionId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioInstanceContainedInstanceComponent))
          return false;
        ExampleScenarioInstanceContainedInstanceComponent o = (ExampleScenarioInstanceContainedInstanceComponent) other_;
        return compareDeep(resourceId, o.resourceId, true) && compareDeep(versionId, o.versionId, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioInstanceContainedInstanceComponent))
          return false;
        ExampleScenarioInstanceContainedInstanceComponent o = (ExampleScenarioInstanceContainedInstanceComponent) other_;
        return compareValues(resourceId, o.resourceId, true) && compareValues(versionId, o.versionId, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(resourceId, versionId);
      }

  public String fhirType() {
    return "ExampleScenario.instance.containedInstance";

  }

  }

    @Block()
    public static class ExampleScenarioProcessComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The diagram title of the group of operations.
         */
        @Child(name = "title", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The diagram title of the group of operations", formalDefinition="The diagram title of the group of operations." )
        protected StringType title;

        /**
         * A longer description of the group of operations.
         */
        @Child(name = "description", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A longer description of the group of operations", formalDefinition="A longer description of the group of operations." )
        protected MarkdownType description;

        /**
         * Description of initial status before the process starts.
         */
        @Child(name = "preConditions", type = {MarkdownType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of initial status before the process starts", formalDefinition="Description of initial status before the process starts." )
        protected MarkdownType preConditions;

        /**
         * Description of final status after the process ends.
         */
        @Child(name = "postConditions", type = {MarkdownType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of final status after the process ends", formalDefinition="Description of final status after the process ends." )
        protected MarkdownType postConditions;

        /**
         * Each step of the process.
         */
        @Child(name = "step", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Each step of the process", formalDefinition="Each step of the process." )
        protected List<ExampleScenarioProcessStepComponent> step;

        private static final long serialVersionUID = 325578043L;

    /**
     * Constructor
     */
      public ExampleScenarioProcessComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioProcessComponent(StringType title) {
        super();
        this.title = title;
      }

        /**
         * @return {@link #title} (The diagram title of the group of operations.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessComponent.title");
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
         * @param value {@link #title} (The diagram title of the group of operations.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ExampleScenarioProcessComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The diagram title of the group of operations.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The diagram title of the group of operations.
         */
        public ExampleScenarioProcessComponent setTitle(String value) { 
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (A longer description of the group of operations.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MarkdownType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessComponent.description");
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
         * @param value {@link #description} (A longer description of the group of operations.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ExampleScenarioProcessComponent setDescriptionElement(MarkdownType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A longer description of the group of operations.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A longer description of the group of operations.
         */
        public ExampleScenarioProcessComponent setDescription(String value) { 
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
         * @return {@link #preConditions} (Description of initial status before the process starts.). This is the underlying object with id, value and extensions. The accessor "getPreConditions" gives direct access to the value
         */
        public MarkdownType getPreConditionsElement() { 
          if (this.preConditions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessComponent.preConditions");
            else if (Configuration.doAutoCreate())
              this.preConditions = new MarkdownType(); // bb
          return this.preConditions;
        }

        public boolean hasPreConditionsElement() { 
          return this.preConditions != null && !this.preConditions.isEmpty();
        }

        public boolean hasPreConditions() { 
          return this.preConditions != null && !this.preConditions.isEmpty();
        }

        /**
         * @param value {@link #preConditions} (Description of initial status before the process starts.). This is the underlying object with id, value and extensions. The accessor "getPreConditions" gives direct access to the value
         */
        public ExampleScenarioProcessComponent setPreConditionsElement(MarkdownType value) { 
          this.preConditions = value;
          return this;
        }

        /**
         * @return Description of initial status before the process starts.
         */
        public String getPreConditions() { 
          return this.preConditions == null ? null : this.preConditions.getValue();
        }

        /**
         * @param value Description of initial status before the process starts.
         */
        public ExampleScenarioProcessComponent setPreConditions(String value) { 
          if (value == null)
            this.preConditions = null;
          else {
            if (this.preConditions == null)
              this.preConditions = new MarkdownType();
            this.preConditions.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #postConditions} (Description of final status after the process ends.). This is the underlying object with id, value and extensions. The accessor "getPostConditions" gives direct access to the value
         */
        public MarkdownType getPostConditionsElement() { 
          if (this.postConditions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessComponent.postConditions");
            else if (Configuration.doAutoCreate())
              this.postConditions = new MarkdownType(); // bb
          return this.postConditions;
        }

        public boolean hasPostConditionsElement() { 
          return this.postConditions != null && !this.postConditions.isEmpty();
        }

        public boolean hasPostConditions() { 
          return this.postConditions != null && !this.postConditions.isEmpty();
        }

        /**
         * @param value {@link #postConditions} (Description of final status after the process ends.). This is the underlying object with id, value and extensions. The accessor "getPostConditions" gives direct access to the value
         */
        public ExampleScenarioProcessComponent setPostConditionsElement(MarkdownType value) { 
          this.postConditions = value;
          return this;
        }

        /**
         * @return Description of final status after the process ends.
         */
        public String getPostConditions() { 
          return this.postConditions == null ? null : this.postConditions.getValue();
        }

        /**
         * @param value Description of final status after the process ends.
         */
        public ExampleScenarioProcessComponent setPostConditions(String value) { 
          if (value == null)
            this.postConditions = null;
          else {
            if (this.postConditions == null)
              this.postConditions = new MarkdownType();
            this.postConditions.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #step} (Each step of the process.)
         */
        public List<ExampleScenarioProcessStepComponent> getStep() { 
          if (this.step == null)
            this.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          return this.step;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioProcessComponent setStep(List<ExampleScenarioProcessStepComponent> theStep) { 
          this.step = theStep;
          return this;
        }

        public boolean hasStep() { 
          if (this.step == null)
            return false;
          for (ExampleScenarioProcessStepComponent item : this.step)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExampleScenarioProcessStepComponent addStep() { //3
          ExampleScenarioProcessStepComponent t = new ExampleScenarioProcessStepComponent();
          if (this.step == null)
            this.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          this.step.add(t);
          return t;
        }

        public ExampleScenarioProcessComponent addStep(ExampleScenarioProcessStepComponent t) { //3
          if (t == null)
            return this;
          if (this.step == null)
            this.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          this.step.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #step}, creating it if it does not already exist
         */
        public ExampleScenarioProcessStepComponent getStepFirstRep() { 
          if (getStep().isEmpty()) {
            addStep();
          }
          return getStep().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("title", "string", "The diagram title of the group of operations.", 0, 1, title));
          children.add(new Property("description", "markdown", "A longer description of the group of operations.", 0, 1, description));
          children.add(new Property("preConditions", "markdown", "Description of initial status before the process starts.", 0, 1, preConditions));
          children.add(new Property("postConditions", "markdown", "Description of final status after the process ends.", 0, 1, postConditions));
          children.add(new Property("step", "", "Each step of the process.", 0, java.lang.Integer.MAX_VALUE, step));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 110371416: /*title*/  return new Property("title", "string", "The diagram title of the group of operations.", 0, 1, title);
          case -1724546052: /*description*/  return new Property("description", "markdown", "A longer description of the group of operations.", 0, 1, description);
          case -1006692933: /*preConditions*/  return new Property("preConditions", "markdown", "Description of initial status before the process starts.", 0, 1, preConditions);
          case 1738302328: /*postConditions*/  return new Property("postConditions", "markdown", "Description of final status after the process ends.", 0, 1, postConditions);
          case 3540684: /*step*/  return new Property("step", "", "Each step of the process.", 0, java.lang.Integer.MAX_VALUE, step);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -1006692933: /*preConditions*/ return this.preConditions == null ? new Base[0] : new Base[] {this.preConditions}; // MarkdownType
        case 1738302328: /*postConditions*/ return this.postConditions == null ? new Base[0] : new Base[] {this.postConditions}; // MarkdownType
        case 3540684: /*step*/ return this.step == null ? new Base[0] : this.step.toArray(new Base[this.step.size()]); // ExampleScenarioProcessStepComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -1006692933: // preConditions
          this.preConditions = castToMarkdown(value); // MarkdownType
          return value;
        case 1738302328: // postConditions
          this.postConditions = castToMarkdown(value); // MarkdownType
          return value;
        case 3540684: // step
          this.getStep().add((ExampleScenarioProcessStepComponent) value); // ExampleScenarioProcessStepComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("preConditions")) {
          this.preConditions = castToMarkdown(value); // MarkdownType
        } else if (name.equals("postConditions")) {
          this.postConditions = castToMarkdown(value); // MarkdownType
        } else if (name.equals("step")) {
          this.getStep().add((ExampleScenarioProcessStepComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110371416:  return getTitleElement();
        case -1724546052:  return getDescriptionElement();
        case -1006692933:  return getPreConditionsElement();
        case 1738302328:  return getPostConditionsElement();
        case 3540684:  return addStep(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110371416: /*title*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -1006692933: /*preConditions*/ return new String[] {"markdown"};
        case 1738302328: /*postConditions*/ return new String[] {"markdown"};
        case 3540684: /*step*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.description");
        }
        else if (name.equals("preConditions")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.preConditions");
        }
        else if (name.equals("postConditions")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.postConditions");
        }
        else if (name.equals("step")) {
          return addStep();
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioProcessComponent copy() {
        ExampleScenarioProcessComponent dst = new ExampleScenarioProcessComponent();
        copyValues(dst);
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.preConditions = preConditions == null ? null : preConditions.copy();
        dst.postConditions = postConditions == null ? null : postConditions.copy();
        if (step != null) {
          dst.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          for (ExampleScenarioProcessStepComponent i : step)
            dst.step.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessComponent))
          return false;
        ExampleScenarioProcessComponent o = (ExampleScenarioProcessComponent) other_;
        return compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(preConditions, o.preConditions, true)
           && compareDeep(postConditions, o.postConditions, true) && compareDeep(step, o.step, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessComponent))
          return false;
        ExampleScenarioProcessComponent o = (ExampleScenarioProcessComponent) other_;
        return compareValues(title, o.title, true) && compareValues(description, o.description, true) && compareValues(preConditions, o.preConditions, true)
           && compareValues(postConditions, o.postConditions, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(title, description, preConditions
          , postConditions, step);
      }

  public String fhirType() {
    return "ExampleScenario.process";

  }

  }

    @Block()
    public static class ExampleScenarioProcessStepComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Nested process.
         */
        @Child(name = "process", type = {ExampleScenarioProcessComponent.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested process", formalDefinition="Nested process." )
        protected List<ExampleScenarioProcessComponent> process;

        /**
         * If there is a pause in the flow.
         */
        @Child(name = "pause", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If there is a pause in the flow", formalDefinition="If there is a pause in the flow." )
        protected BooleanType pause;

        /**
         * Each interaction or action.
         */
        @Child(name = "operation", type = {}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Each interaction or action", formalDefinition="Each interaction or action." )
        protected ExampleScenarioProcessStepOperationComponent operation;

        /**
         * Each interaction in the workflow.
         */
        @Child(name = "alternative", type = {}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Each interaction in the process", formalDefinition="Each interaction in the workflow." )
        protected ExampleScenarioProcessStepAlternativeComponent alternative;

        private static final long serialVersionUID = -939172007L;

    /**
     * Constructor
     */
      public ExampleScenarioProcessStepComponent() {
        super();
      }

        /**
         * @return {@link #process} (Nested process.)
         */
        public List<ExampleScenarioProcessComponent> getProcess() { 
          if (this.process == null)
            this.process = new ArrayList<ExampleScenarioProcessComponent>();
          return this.process;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioProcessStepComponent setProcess(List<ExampleScenarioProcessComponent> theProcess) { 
          this.process = theProcess;
          return this;
        }

        public boolean hasProcess() { 
          if (this.process == null)
            return false;
          for (ExampleScenarioProcessComponent item : this.process)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExampleScenarioProcessComponent addProcess() { //3
          ExampleScenarioProcessComponent t = new ExampleScenarioProcessComponent();
          if (this.process == null)
            this.process = new ArrayList<ExampleScenarioProcessComponent>();
          this.process.add(t);
          return t;
        }

        public ExampleScenarioProcessStepComponent addProcess(ExampleScenarioProcessComponent t) { //3
          if (t == null)
            return this;
          if (this.process == null)
            this.process = new ArrayList<ExampleScenarioProcessComponent>();
          this.process.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #process}, creating it if it does not already exist
         */
        public ExampleScenarioProcessComponent getProcessFirstRep() { 
          if (getProcess().isEmpty()) {
            addProcess();
          }
          return getProcess().get(0);
        }

        /**
         * @return {@link #pause} (If there is a pause in the flow.). This is the underlying object with id, value and extensions. The accessor "getPause" gives direct access to the value
         */
        public BooleanType getPauseElement() { 
          if (this.pause == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepComponent.pause");
            else if (Configuration.doAutoCreate())
              this.pause = new BooleanType(); // bb
          return this.pause;
        }

        public boolean hasPauseElement() { 
          return this.pause != null && !this.pause.isEmpty();
        }

        public boolean hasPause() { 
          return this.pause != null && !this.pause.isEmpty();
        }

        /**
         * @param value {@link #pause} (If there is a pause in the flow.). This is the underlying object with id, value and extensions. The accessor "getPause" gives direct access to the value
         */
        public ExampleScenarioProcessStepComponent setPauseElement(BooleanType value) { 
          this.pause = value;
          return this;
        }

        /**
         * @return If there is a pause in the flow.
         */
        public boolean getPause() { 
          return this.pause == null || this.pause.isEmpty() ? false : this.pause.getValue();
        }

        /**
         * @param value If there is a pause in the flow.
         */
        public ExampleScenarioProcessStepComponent setPause(boolean value) { 
            if (this.pause == null)
              this.pause = new BooleanType();
            this.pause.setValue(value);
          return this;
        }

        /**
         * @return {@link #operation} (Each interaction or action.)
         */
        public ExampleScenarioProcessStepOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new ExampleScenarioProcessStepOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (Each interaction or action.)
         */
        public ExampleScenarioProcessStepComponent setOperation(ExampleScenarioProcessStepOperationComponent value) { 
          this.operation = value;
          return this;
        }

        /**
         * @return {@link #alternative} (Each interaction in the workflow.)
         */
        public ExampleScenarioProcessStepAlternativeComponent getAlternative() { 
          if (this.alternative == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepComponent.alternative");
            else if (Configuration.doAutoCreate())
              this.alternative = new ExampleScenarioProcessStepAlternativeComponent(); // cc
          return this.alternative;
        }

        public boolean hasAlternative() { 
          return this.alternative != null && !this.alternative.isEmpty();
        }

        /**
         * @param value {@link #alternative} (Each interaction in the workflow.)
         */
        public ExampleScenarioProcessStepComponent setAlternative(ExampleScenarioProcessStepAlternativeComponent value) { 
          this.alternative = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("process", "@ExampleScenario.process", "Nested process.", 0, java.lang.Integer.MAX_VALUE, process));
          children.add(new Property("pause", "boolean", "If there is a pause in the flow.", 0, 1, pause));
          children.add(new Property("operation", "", "Each interaction or action.", 0, 1, operation));
          children.add(new Property("alternative", "", "Each interaction in the workflow.", 0, 1, alternative));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -309518737: /*process*/  return new Property("process", "@ExampleScenario.process", "Nested process.", 0, java.lang.Integer.MAX_VALUE, process);
          case 106440182: /*pause*/  return new Property("pause", "boolean", "If there is a pause in the flow.", 0, 1, pause);
          case 1662702951: /*operation*/  return new Property("operation", "", "Each interaction or action.", 0, 1, operation);
          case -196794451: /*alternative*/  return new Property("alternative", "", "Each interaction in the workflow.", 0, 1, alternative);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -309518737: /*process*/ return this.process == null ? new Base[0] : this.process.toArray(new Base[this.process.size()]); // ExampleScenarioProcessComponent
        case 106440182: /*pause*/ return this.pause == null ? new Base[0] : new Base[] {this.pause}; // BooleanType
        case 1662702951: /*operation*/ return this.operation == null ? new Base[0] : new Base[] {this.operation}; // ExampleScenarioProcessStepOperationComponent
        case -196794451: /*alternative*/ return this.alternative == null ? new Base[0] : new Base[] {this.alternative}; // ExampleScenarioProcessStepAlternativeComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -309518737: // process
          this.getProcess().add((ExampleScenarioProcessComponent) value); // ExampleScenarioProcessComponent
          return value;
        case 106440182: // pause
          this.pause = castToBoolean(value); // BooleanType
          return value;
        case 1662702951: // operation
          this.operation = (ExampleScenarioProcessStepOperationComponent) value; // ExampleScenarioProcessStepOperationComponent
          return value;
        case -196794451: // alternative
          this.alternative = (ExampleScenarioProcessStepAlternativeComponent) value; // ExampleScenarioProcessStepAlternativeComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("process")) {
          this.getProcess().add((ExampleScenarioProcessComponent) value);
        } else if (name.equals("pause")) {
          this.pause = castToBoolean(value); // BooleanType
        } else if (name.equals("operation")) {
          this.operation = (ExampleScenarioProcessStepOperationComponent) value; // ExampleScenarioProcessStepOperationComponent
        } else if (name.equals("alternative")) {
          this.alternative = (ExampleScenarioProcessStepAlternativeComponent) value; // ExampleScenarioProcessStepAlternativeComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -309518737:  return addProcess(); 
        case 106440182:  return getPauseElement();
        case 1662702951:  return getOperation(); 
        case -196794451:  return getAlternative(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -309518737: /*process*/ return new String[] {"@ExampleScenario.process"};
        case 106440182: /*pause*/ return new String[] {"boolean"};
        case 1662702951: /*operation*/ return new String[] {};
        case -196794451: /*alternative*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("process")) {
          return addProcess();
        }
        else if (name.equals("pause")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.pause");
        }
        else if (name.equals("operation")) {
          this.operation = new ExampleScenarioProcessStepOperationComponent();
          return this.operation;
        }
        else if (name.equals("alternative")) {
          this.alternative = new ExampleScenarioProcessStepAlternativeComponent();
          return this.alternative;
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioProcessStepComponent copy() {
        ExampleScenarioProcessStepComponent dst = new ExampleScenarioProcessStepComponent();
        copyValues(dst);
        if (process != null) {
          dst.process = new ArrayList<ExampleScenarioProcessComponent>();
          for (ExampleScenarioProcessComponent i : process)
            dst.process.add(i.copy());
        };
        dst.pause = pause == null ? null : pause.copy();
        dst.operation = operation == null ? null : operation.copy();
        dst.alternative = alternative == null ? null : alternative.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepComponent))
          return false;
        ExampleScenarioProcessStepComponent o = (ExampleScenarioProcessStepComponent) other_;
        return compareDeep(process, o.process, true) && compareDeep(pause, o.pause, true) && compareDeep(operation, o.operation, true)
           && compareDeep(alternative, o.alternative, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepComponent))
          return false;
        ExampleScenarioProcessStepComponent o = (ExampleScenarioProcessStepComponent) other_;
        return compareValues(pause, o.pause, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(process, pause, operation
          , alternative);
      }

  public String fhirType() {
    return "ExampleScenario.process.step";

  }

  }

    @Block()
    public static class ExampleScenarioProcessStepOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The sequential number of the interaction.
         */
        @Child(name = "number", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The sequential number of the interaction", formalDefinition="The sequential number of the interaction." )
        protected StringType number;

        /**
         * The type of operation - CRUD.
         */
        @Child(name = "type", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of operation - CRUD", formalDefinition="The type of operation - CRUD." )
        protected StringType type;

        /**
         * The human-friendly name of the interaction.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The human-friendly name of the interaction", formalDefinition="The human-friendly name of the interaction." )
        protected StringType name;

        /**
         * Who starts the transaction.
         */
        @Child(name = "initiator", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who starts the transaction", formalDefinition="Who starts the transaction." )
        protected StringType initiator;

        /**
         * Who receives the transaction.
         */
        @Child(name = "receiver", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who receives the transaction", formalDefinition="Who receives the transaction." )
        protected StringType receiver;

        /**
         * A comment to be inserted in the diagram.
         */
        @Child(name = "description", type = {MarkdownType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A comment to be inserted in the diagram", formalDefinition="A comment to be inserted in the diagram." )
        protected MarkdownType description;

        /**
         * Whether the initiator is deactivated right after the transaction.
         */
        @Child(name = "initiatorActive", type = {BooleanType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the initiator is deactivated right after the transaction", formalDefinition="Whether the initiator is deactivated right after the transaction." )
        protected BooleanType initiatorActive;

        /**
         * Whether the receiver is deactivated right after the transaction.
         */
        @Child(name = "receiverActive", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the receiver is deactivated right after the transaction", formalDefinition="Whether the receiver is deactivated right after the transaction." )
        protected BooleanType receiverActive;

        /**
         * Each resource instance used by the initiator.
         */
        @Child(name = "request", type = {ExampleScenarioInstanceContainedInstanceComponent.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Each resource instance used by the initiator", formalDefinition="Each resource instance used by the initiator." )
        protected ExampleScenarioInstanceContainedInstanceComponent request;

        /**
         * Each resource instance used by the responder.
         */
        @Child(name = "response", type = {ExampleScenarioInstanceContainedInstanceComponent.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Each resource instance used by the responder", formalDefinition="Each resource instance used by the responder." )
        protected ExampleScenarioInstanceContainedInstanceComponent response;

        private static final long serialVersionUID = 911241906L;

    /**
     * Constructor
     */
      public ExampleScenarioProcessStepOperationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioProcessStepOperationComponent(StringType number) {
        super();
        this.number = number;
      }

        /**
         * @return {@link #number} (The sequential number of the interaction.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public StringType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new StringType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (The sequential number of the interaction.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setNumberElement(StringType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return The sequential number of the interaction.
         */
        public String getNumber() { 
          return this.number == null ? null : this.number.getValue();
        }

        /**
         * @param value The sequential number of the interaction.
         */
        public ExampleScenarioProcessStepOperationComponent setNumber(String value) { 
            if (this.number == null)
              this.number = new StringType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of operation - CRUD.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of operation - CRUD.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of operation - CRUD.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of operation - CRUD.
         */
        public ExampleScenarioProcessStepOperationComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #name} (The human-friendly name of the interaction.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.name");
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
         * @param value {@link #name} (The human-friendly name of the interaction.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The human-friendly name of the interaction.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The human-friendly name of the interaction.
         */
        public ExampleScenarioProcessStepOperationComponent setName(String value) { 
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
         * @return {@link #initiator} (Who starts the transaction.). This is the underlying object with id, value and extensions. The accessor "getInitiator" gives direct access to the value
         */
        public StringType getInitiatorElement() { 
          if (this.initiator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.initiator");
            else if (Configuration.doAutoCreate())
              this.initiator = new StringType(); // bb
          return this.initiator;
        }

        public boolean hasInitiatorElement() { 
          return this.initiator != null && !this.initiator.isEmpty();
        }

        public boolean hasInitiator() { 
          return this.initiator != null && !this.initiator.isEmpty();
        }

        /**
         * @param value {@link #initiator} (Who starts the transaction.). This is the underlying object with id, value and extensions. The accessor "getInitiator" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setInitiatorElement(StringType value) { 
          this.initiator = value;
          return this;
        }

        /**
         * @return Who starts the transaction.
         */
        public String getInitiator() { 
          return this.initiator == null ? null : this.initiator.getValue();
        }

        /**
         * @param value Who starts the transaction.
         */
        public ExampleScenarioProcessStepOperationComponent setInitiator(String value) { 
          if (Utilities.noString(value))
            this.initiator = null;
          else {
            if (this.initiator == null)
              this.initiator = new StringType();
            this.initiator.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #receiver} (Who receives the transaction.). This is the underlying object with id, value and extensions. The accessor "getReceiver" gives direct access to the value
         */
        public StringType getReceiverElement() { 
          if (this.receiver == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.receiver");
            else if (Configuration.doAutoCreate())
              this.receiver = new StringType(); // bb
          return this.receiver;
        }

        public boolean hasReceiverElement() { 
          return this.receiver != null && !this.receiver.isEmpty();
        }

        public boolean hasReceiver() { 
          return this.receiver != null && !this.receiver.isEmpty();
        }

        /**
         * @param value {@link #receiver} (Who receives the transaction.). This is the underlying object with id, value and extensions. The accessor "getReceiver" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setReceiverElement(StringType value) { 
          this.receiver = value;
          return this;
        }

        /**
         * @return Who receives the transaction.
         */
        public String getReceiver() { 
          return this.receiver == null ? null : this.receiver.getValue();
        }

        /**
         * @param value Who receives the transaction.
         */
        public ExampleScenarioProcessStepOperationComponent setReceiver(String value) { 
          if (Utilities.noString(value))
            this.receiver = null;
          else {
            if (this.receiver == null)
              this.receiver = new StringType();
            this.receiver.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (A comment to be inserted in the diagram.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MarkdownType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.description");
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
         * @param value {@link #description} (A comment to be inserted in the diagram.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setDescriptionElement(MarkdownType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A comment to be inserted in the diagram.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A comment to be inserted in the diagram.
         */
        public ExampleScenarioProcessStepOperationComponent setDescription(String value) { 
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
         * @return {@link #initiatorActive} (Whether the initiator is deactivated right after the transaction.). This is the underlying object with id, value and extensions. The accessor "getInitiatorActive" gives direct access to the value
         */
        public BooleanType getInitiatorActiveElement() { 
          if (this.initiatorActive == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.initiatorActive");
            else if (Configuration.doAutoCreate())
              this.initiatorActive = new BooleanType(); // bb
          return this.initiatorActive;
        }

        public boolean hasInitiatorActiveElement() { 
          return this.initiatorActive != null && !this.initiatorActive.isEmpty();
        }

        public boolean hasInitiatorActive() { 
          return this.initiatorActive != null && !this.initiatorActive.isEmpty();
        }

        /**
         * @param value {@link #initiatorActive} (Whether the initiator is deactivated right after the transaction.). This is the underlying object with id, value and extensions. The accessor "getInitiatorActive" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setInitiatorActiveElement(BooleanType value) { 
          this.initiatorActive = value;
          return this;
        }

        /**
         * @return Whether the initiator is deactivated right after the transaction.
         */
        public boolean getInitiatorActive() { 
          return this.initiatorActive == null || this.initiatorActive.isEmpty() ? false : this.initiatorActive.getValue();
        }

        /**
         * @param value Whether the initiator is deactivated right after the transaction.
         */
        public ExampleScenarioProcessStepOperationComponent setInitiatorActive(boolean value) { 
            if (this.initiatorActive == null)
              this.initiatorActive = new BooleanType();
            this.initiatorActive.setValue(value);
          return this;
        }

        /**
         * @return {@link #receiverActive} (Whether the receiver is deactivated right after the transaction.). This is the underlying object with id, value and extensions. The accessor "getReceiverActive" gives direct access to the value
         */
        public BooleanType getReceiverActiveElement() { 
          if (this.receiverActive == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.receiverActive");
            else if (Configuration.doAutoCreate())
              this.receiverActive = new BooleanType(); // bb
          return this.receiverActive;
        }

        public boolean hasReceiverActiveElement() { 
          return this.receiverActive != null && !this.receiverActive.isEmpty();
        }

        public boolean hasReceiverActive() { 
          return this.receiverActive != null && !this.receiverActive.isEmpty();
        }

        /**
         * @param value {@link #receiverActive} (Whether the receiver is deactivated right after the transaction.). This is the underlying object with id, value and extensions. The accessor "getReceiverActive" gives direct access to the value
         */
        public ExampleScenarioProcessStepOperationComponent setReceiverActiveElement(BooleanType value) { 
          this.receiverActive = value;
          return this;
        }

        /**
         * @return Whether the receiver is deactivated right after the transaction.
         */
        public boolean getReceiverActive() { 
          return this.receiverActive == null || this.receiverActive.isEmpty() ? false : this.receiverActive.getValue();
        }

        /**
         * @param value Whether the receiver is deactivated right after the transaction.
         */
        public ExampleScenarioProcessStepOperationComponent setReceiverActive(boolean value) { 
            if (this.receiverActive == null)
              this.receiverActive = new BooleanType();
            this.receiverActive.setValue(value);
          return this;
        }

        /**
         * @return {@link #request} (Each resource instance used by the initiator.)
         */
        public ExampleScenarioInstanceContainedInstanceComponent getRequest() { 
          if (this.request == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.request");
            else if (Configuration.doAutoCreate())
              this.request = new ExampleScenarioInstanceContainedInstanceComponent(); // cc
          return this.request;
        }

        public boolean hasRequest() { 
          return this.request != null && !this.request.isEmpty();
        }

        /**
         * @param value {@link #request} (Each resource instance used by the initiator.)
         */
        public ExampleScenarioProcessStepOperationComponent setRequest(ExampleScenarioInstanceContainedInstanceComponent value) { 
          this.request = value;
          return this;
        }

        /**
         * @return {@link #response} (Each resource instance used by the responder.)
         */
        public ExampleScenarioInstanceContainedInstanceComponent getResponse() { 
          if (this.response == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepOperationComponent.response");
            else if (Configuration.doAutoCreate())
              this.response = new ExampleScenarioInstanceContainedInstanceComponent(); // cc
          return this.response;
        }

        public boolean hasResponse() { 
          return this.response != null && !this.response.isEmpty();
        }

        /**
         * @param value {@link #response} (Each resource instance used by the responder.)
         */
        public ExampleScenarioProcessStepOperationComponent setResponse(ExampleScenarioInstanceContainedInstanceComponent value) { 
          this.response = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("number", "string", "The sequential number of the interaction.", 0, 1, number));
          children.add(new Property("type", "string", "The type of operation - CRUD.", 0, 1, type));
          children.add(new Property("name", "string", "The human-friendly name of the interaction.", 0, 1, name));
          children.add(new Property("initiator", "string", "Who starts the transaction.", 0, 1, initiator));
          children.add(new Property("receiver", "string", "Who receives the transaction.", 0, 1, receiver));
          children.add(new Property("description", "markdown", "A comment to be inserted in the diagram.", 0, 1, description));
          children.add(new Property("initiatorActive", "boolean", "Whether the initiator is deactivated right after the transaction.", 0, 1, initiatorActive));
          children.add(new Property("receiverActive", "boolean", "Whether the receiver is deactivated right after the transaction.", 0, 1, receiverActive));
          children.add(new Property("request", "@ExampleScenario.instance.containedInstance", "Each resource instance used by the initiator.", 0, 1, request));
          children.add(new Property("response", "@ExampleScenario.instance.containedInstance", "Each resource instance used by the responder.", 0, 1, response));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1034364087: /*number*/  return new Property("number", "string", "The sequential number of the interaction.", 0, 1, number);
          case 3575610: /*type*/  return new Property("type", "string", "The type of operation - CRUD.", 0, 1, type);
          case 3373707: /*name*/  return new Property("name", "string", "The human-friendly name of the interaction.", 0, 1, name);
          case -248987089: /*initiator*/  return new Property("initiator", "string", "Who starts the transaction.", 0, 1, initiator);
          case -808719889: /*receiver*/  return new Property("receiver", "string", "Who receives the transaction.", 0, 1, receiver);
          case -1724546052: /*description*/  return new Property("description", "markdown", "A comment to be inserted in the diagram.", 0, 1, description);
          case 384339477: /*initiatorActive*/  return new Property("initiatorActive", "boolean", "Whether the initiator is deactivated right after the transaction.", 0, 1, initiatorActive);
          case -285284907: /*receiverActive*/  return new Property("receiverActive", "boolean", "Whether the receiver is deactivated right after the transaction.", 0, 1, receiverActive);
          case 1095692943: /*request*/  return new Property("request", "@ExampleScenario.instance.containedInstance", "Each resource instance used by the initiator.", 0, 1, request);
          case -340323263: /*response*/  return new Property("response", "@ExampleScenario.instance.containedInstance", "Each resource instance used by the responder.", 0, 1, response);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -248987089: /*initiator*/ return this.initiator == null ? new Base[0] : new Base[] {this.initiator}; // StringType
        case -808719889: /*receiver*/ return this.receiver == null ? new Base[0] : new Base[] {this.receiver}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 384339477: /*initiatorActive*/ return this.initiatorActive == null ? new Base[0] : new Base[] {this.initiatorActive}; // BooleanType
        case -285284907: /*receiverActive*/ return this.receiverActive == null ? new Base[0] : new Base[] {this.receiverActive}; // BooleanType
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // ExampleScenarioInstanceContainedInstanceComponent
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // ExampleScenarioInstanceContainedInstanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.number = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -248987089: // initiator
          this.initiator = castToString(value); // StringType
          return value;
        case -808719889: // receiver
          this.receiver = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case 384339477: // initiatorActive
          this.initiatorActive = castToBoolean(value); // BooleanType
          return value;
        case -285284907: // receiverActive
          this.receiverActive = castToBoolean(value); // BooleanType
          return value;
        case 1095692943: // request
          this.request = (ExampleScenarioInstanceContainedInstanceComponent) value; // ExampleScenarioInstanceContainedInstanceComponent
          return value;
        case -340323263: // response
          this.response = (ExampleScenarioInstanceContainedInstanceComponent) value; // ExampleScenarioInstanceContainedInstanceComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number")) {
          this.number = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("initiator")) {
          this.initiator = castToString(value); // StringType
        } else if (name.equals("receiver")) {
          this.receiver = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("initiatorActive")) {
          this.initiatorActive = castToBoolean(value); // BooleanType
        } else if (name.equals("receiverActive")) {
          this.receiverActive = castToBoolean(value); // BooleanType
        } else if (name.equals("request")) {
          this.request = (ExampleScenarioInstanceContainedInstanceComponent) value; // ExampleScenarioInstanceContainedInstanceComponent
        } else if (name.equals("response")) {
          this.response = (ExampleScenarioInstanceContainedInstanceComponent) value; // ExampleScenarioInstanceContainedInstanceComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087:  return getNumberElement();
        case 3575610:  return getTypeElement();
        case 3373707:  return getNameElement();
        case -248987089:  return getInitiatorElement();
        case -808719889:  return getReceiverElement();
        case -1724546052:  return getDescriptionElement();
        case 384339477:  return getInitiatorActiveElement();
        case -285284907:  return getReceiverActiveElement();
        case 1095692943:  return getRequest(); 
        case -340323263:  return getResponse(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -248987089: /*initiator*/ return new String[] {"string"};
        case -808719889: /*receiver*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 384339477: /*initiatorActive*/ return new String[] {"boolean"};
        case -285284907: /*receiverActive*/ return new String[] {"boolean"};
        case 1095692943: /*request*/ return new String[] {"@ExampleScenario.instance.containedInstance"};
        case -340323263: /*response*/ return new String[] {"@ExampleScenario.instance.containedInstance"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.number");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.type");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.name");
        }
        else if (name.equals("initiator")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.initiator");
        }
        else if (name.equals("receiver")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.receiver");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.description");
        }
        else if (name.equals("initiatorActive")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.initiatorActive");
        }
        else if (name.equals("receiverActive")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.receiverActive");
        }
        else if (name.equals("request")) {
          this.request = new ExampleScenarioInstanceContainedInstanceComponent();
          return this.request;
        }
        else if (name.equals("response")) {
          this.response = new ExampleScenarioInstanceContainedInstanceComponent();
          return this.response;
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioProcessStepOperationComponent copy() {
        ExampleScenarioProcessStepOperationComponent dst = new ExampleScenarioProcessStepOperationComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        dst.initiator = initiator == null ? null : initiator.copy();
        dst.receiver = receiver == null ? null : receiver.copy();
        dst.description = description == null ? null : description.copy();
        dst.initiatorActive = initiatorActive == null ? null : initiatorActive.copy();
        dst.receiverActive = receiverActive == null ? null : receiverActive.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepOperationComponent))
          return false;
        ExampleScenarioProcessStepOperationComponent o = (ExampleScenarioProcessStepOperationComponent) other_;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(name, o.name, true)
           && compareDeep(initiator, o.initiator, true) && compareDeep(receiver, o.receiver, true) && compareDeep(description, o.description, true)
           && compareDeep(initiatorActive, o.initiatorActive, true) && compareDeep(receiverActive, o.receiverActive, true)
           && compareDeep(request, o.request, true) && compareDeep(response, o.response, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepOperationComponent))
          return false;
        ExampleScenarioProcessStepOperationComponent o = (ExampleScenarioProcessStepOperationComponent) other_;
        return compareValues(number, o.number, true) && compareValues(type, o.type, true) && compareValues(name, o.name, true)
           && compareValues(initiator, o.initiator, true) && compareValues(receiver, o.receiver, true) && compareValues(description, o.description, true)
           && compareValues(initiatorActive, o.initiatorActive, true) && compareValues(receiverActive, o.receiverActive, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, type, name, initiator
          , receiver, description, initiatorActive, receiverActive, request, response);
      }

  public String fhirType() {
    return "ExampleScenario.process.step.operation";

  }

  }

    @Block()
    public static class ExampleScenarioProcessStepAlternativeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of each alternative.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The name of each alternative", formalDefinition="The name of each alternative." )
        protected StringType name;

        /**
         * Each of the possible options in an alternative.
         */
        @Child(name = "option", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Each of the possible options in an alternative", formalDefinition="Each of the possible options in an alternative." )
        protected List<ExampleScenarioProcessStepAlternativeOptionComponent> option;

        private static final long serialVersionUID = 379920547L;

    /**
     * Constructor
     */
      public ExampleScenarioProcessStepAlternativeComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of each alternative.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepAlternativeComponent.name");
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
         * @param value {@link #name} (The name of each alternative.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ExampleScenarioProcessStepAlternativeComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of each alternative.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of each alternative.
         */
        public ExampleScenarioProcessStepAlternativeComponent setName(String value) { 
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
         * @return {@link #option} (Each of the possible options in an alternative.)
         */
        public List<ExampleScenarioProcessStepAlternativeOptionComponent> getOption() { 
          if (this.option == null)
            this.option = new ArrayList<ExampleScenarioProcessStepAlternativeOptionComponent>();
          return this.option;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioProcessStepAlternativeComponent setOption(List<ExampleScenarioProcessStepAlternativeOptionComponent> theOption) { 
          this.option = theOption;
          return this;
        }

        public boolean hasOption() { 
          if (this.option == null)
            return false;
          for (ExampleScenarioProcessStepAlternativeOptionComponent item : this.option)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExampleScenarioProcessStepAlternativeOptionComponent addOption() { //3
          ExampleScenarioProcessStepAlternativeOptionComponent t = new ExampleScenarioProcessStepAlternativeOptionComponent();
          if (this.option == null)
            this.option = new ArrayList<ExampleScenarioProcessStepAlternativeOptionComponent>();
          this.option.add(t);
          return t;
        }

        public ExampleScenarioProcessStepAlternativeComponent addOption(ExampleScenarioProcessStepAlternativeOptionComponent t) { //3
          if (t == null)
            return this;
          if (this.option == null)
            this.option = new ArrayList<ExampleScenarioProcessStepAlternativeOptionComponent>();
          this.option.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #option}, creating it if it does not already exist
         */
        public ExampleScenarioProcessStepAlternativeOptionComponent getOptionFirstRep() { 
          if (getOption().isEmpty()) {
            addOption();
          }
          return getOption().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The name of each alternative.", 0, 1, name));
          children.add(new Property("option", "", "Each of the possible options in an alternative.", 0, java.lang.Integer.MAX_VALUE, option));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The name of each alternative.", 0, 1, name);
          case -1010136971: /*option*/  return new Property("option", "", "Each of the possible options in an alternative.", 0, java.lang.Integer.MAX_VALUE, option);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1010136971: /*option*/ return this.option == null ? new Base[0] : this.option.toArray(new Base[this.option.size()]); // ExampleScenarioProcessStepAlternativeOptionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1010136971: // option
          this.getOption().add((ExampleScenarioProcessStepAlternativeOptionComponent) value); // ExampleScenarioProcessStepAlternativeOptionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("option")) {
          this.getOption().add((ExampleScenarioProcessStepAlternativeOptionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1010136971:  return addOption(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1010136971: /*option*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.name");
        }
        else if (name.equals("option")) {
          return addOption();
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioProcessStepAlternativeComponent copy() {
        ExampleScenarioProcessStepAlternativeComponent dst = new ExampleScenarioProcessStepAlternativeComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (option != null) {
          dst.option = new ArrayList<ExampleScenarioProcessStepAlternativeOptionComponent>();
          for (ExampleScenarioProcessStepAlternativeOptionComponent i : option)
            dst.option.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepAlternativeComponent))
          return false;
        ExampleScenarioProcessStepAlternativeComponent o = (ExampleScenarioProcessStepAlternativeComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(option, o.option, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepAlternativeComponent))
          return false;
        ExampleScenarioProcessStepAlternativeComponent o = (ExampleScenarioProcessStepAlternativeComponent) other_;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, option);
      }

  public String fhirType() {
    return "ExampleScenario.process.step.alternative";

  }

  }

    @Block()
    public static class ExampleScenarioProcessStepAlternativeOptionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A human-readable description of each option.
         */
        @Child(name = "description", type = {MarkdownType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A human-readable description of each option", formalDefinition="A human-readable description of each option." )
        protected MarkdownType description;

        /**
         * What happens in each alternative option.
         */
        @Child(name = "step", type = {ExampleScenarioProcessStepComponent.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What happens in each alternative option", formalDefinition="What happens in each alternative option." )
        protected List<ExampleScenarioProcessStepComponent> step;

        /**
         * If there is a pause in the flow.
         */
        @Child(name = "pause", type = {BooleanType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="If there is a pause in the flow", formalDefinition="If there is a pause in the flow." )
        protected List<BooleanType> pause;

        private static final long serialVersionUID = -1719991565L;

    /**
     * Constructor
     */
      public ExampleScenarioProcessStepAlternativeOptionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExampleScenarioProcessStepAlternativeOptionComponent(MarkdownType description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #description} (A human-readable description of each option.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MarkdownType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExampleScenarioProcessStepAlternativeOptionComponent.description");
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
         * @param value {@link #description} (A human-readable description of each option.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ExampleScenarioProcessStepAlternativeOptionComponent setDescriptionElement(MarkdownType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A human-readable description of each option.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A human-readable description of each option.
         */
        public ExampleScenarioProcessStepAlternativeOptionComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new MarkdownType();
            this.description.setValue(value);
          return this;
        }

        /**
         * @return {@link #step} (What happens in each alternative option.)
         */
        public List<ExampleScenarioProcessStepComponent> getStep() { 
          if (this.step == null)
            this.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          return this.step;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioProcessStepAlternativeOptionComponent setStep(List<ExampleScenarioProcessStepComponent> theStep) { 
          this.step = theStep;
          return this;
        }

        public boolean hasStep() { 
          if (this.step == null)
            return false;
          for (ExampleScenarioProcessStepComponent item : this.step)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExampleScenarioProcessStepComponent addStep() { //3
          ExampleScenarioProcessStepComponent t = new ExampleScenarioProcessStepComponent();
          if (this.step == null)
            this.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          this.step.add(t);
          return t;
        }

        public ExampleScenarioProcessStepAlternativeOptionComponent addStep(ExampleScenarioProcessStepComponent t) { //3
          if (t == null)
            return this;
          if (this.step == null)
            this.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          this.step.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #step}, creating it if it does not already exist
         */
        public ExampleScenarioProcessStepComponent getStepFirstRep() { 
          if (getStep().isEmpty()) {
            addStep();
          }
          return getStep().get(0);
        }

        /**
         * @return {@link #pause} (If there is a pause in the flow.)
         */
        public List<BooleanType> getPause() { 
          if (this.pause == null)
            this.pause = new ArrayList<BooleanType>();
          return this.pause;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExampleScenarioProcessStepAlternativeOptionComponent setPause(List<BooleanType> thePause) { 
          this.pause = thePause;
          return this;
        }

        public boolean hasPause() { 
          if (this.pause == null)
            return false;
          for (BooleanType item : this.pause)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #pause} (If there is a pause in the flow.)
         */
        public BooleanType addPauseElement() {//2 
          BooleanType t = new BooleanType();
          if (this.pause == null)
            this.pause = new ArrayList<BooleanType>();
          this.pause.add(t);
          return t;
        }

        /**
         * @param value {@link #pause} (If there is a pause in the flow.)
         */
        public ExampleScenarioProcessStepAlternativeOptionComponent addPause(boolean value) { //1
          BooleanType t = new BooleanType();
          t.setValue(value);
          if (this.pause == null)
            this.pause = new ArrayList<BooleanType>();
          this.pause.add(t);
          return this;
        }

        /**
         * @param value {@link #pause} (If there is a pause in the flow.)
         */
        public boolean hasPause(boolean value) { 
          if (this.pause == null)
            return false;
          for (BooleanType v : this.pause)
            if (v.getValue().equals(value)) // boolean
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "markdown", "A human-readable description of each option.", 0, 1, description));
          children.add(new Property("step", "@ExampleScenario.process.step", "What happens in each alternative option.", 0, java.lang.Integer.MAX_VALUE, step));
          children.add(new Property("pause", "boolean", "If there is a pause in the flow.", 0, java.lang.Integer.MAX_VALUE, pause));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "markdown", "A human-readable description of each option.", 0, 1, description);
          case 3540684: /*step*/  return new Property("step", "@ExampleScenario.process.step", "What happens in each alternative option.", 0, java.lang.Integer.MAX_VALUE, step);
          case 106440182: /*pause*/  return new Property("pause", "boolean", "If there is a pause in the flow.", 0, java.lang.Integer.MAX_VALUE, pause);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 3540684: /*step*/ return this.step == null ? new Base[0] : this.step.toArray(new Base[this.step.size()]); // ExampleScenarioProcessStepComponent
        case 106440182: /*pause*/ return this.pause == null ? new Base[0] : this.pause.toArray(new Base[this.pause.size()]); // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case 3540684: // step
          this.getStep().add((ExampleScenarioProcessStepComponent) value); // ExampleScenarioProcessStepComponent
          return value;
        case 106440182: // pause
          this.getPause().add(castToBoolean(value)); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("step")) {
          this.getStep().add((ExampleScenarioProcessStepComponent) value);
        } else if (name.equals("pause")) {
          this.getPause().add(castToBoolean(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case 3540684:  return addStep(); 
        case 106440182:  return addPauseElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 3540684: /*step*/ return new String[] {"@ExampleScenario.process.step"};
        case 106440182: /*pause*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.description");
        }
        else if (name.equals("step")) {
          return addStep();
        }
        else if (name.equals("pause")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.pause");
        }
        else
          return super.addChild(name);
      }

      public ExampleScenarioProcessStepAlternativeOptionComponent copy() {
        ExampleScenarioProcessStepAlternativeOptionComponent dst = new ExampleScenarioProcessStepAlternativeOptionComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        if (step != null) {
          dst.step = new ArrayList<ExampleScenarioProcessStepComponent>();
          for (ExampleScenarioProcessStepComponent i : step)
            dst.step.add(i.copy());
        };
        if (pause != null) {
          dst.pause = new ArrayList<BooleanType>();
          for (BooleanType i : pause)
            dst.pause.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepAlternativeOptionComponent))
          return false;
        ExampleScenarioProcessStepAlternativeOptionComponent o = (ExampleScenarioProcessStepAlternativeOptionComponent) other_;
        return compareDeep(description, o.description, true) && compareDeep(step, o.step, true) && compareDeep(pause, o.pause, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenarioProcessStepAlternativeOptionComponent))
          return false;
        ExampleScenarioProcessStepAlternativeOptionComponent o = (ExampleScenarioProcessStepAlternativeOptionComponent) other_;
        return compareValues(description, o.description, true) && compareValues(pause, o.pause, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, step, pause
          );
      }

  public String fhirType() {
    return "ExampleScenario.process.step.alternative.option";

  }

  }

    /**
     * A formal identifier that is used to identify this example scenario when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the example scenario", formalDefinition="A formal identifier that is used to identify this example scenario when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario." )
    protected MarkdownType copyright;

    /**
     * What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The purpose of the example, e.g. to illustrate a scenario", formalDefinition="What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario." )
    protected MarkdownType purpose;

    /**
     * Actor participating in the resource.
     */
    @Child(name = "actor", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Actor participating in the resource", formalDefinition="Actor participating in the resource." )
    protected List<ExampleScenarioActorComponent> actor;

    /**
     * Each resource and each version that is present in the workflow.
     */
    @Child(name = "instance", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Each resource and each version that is present in the workflow", formalDefinition="Each resource and each version that is present in the workflow." )
    protected List<ExampleScenarioInstanceComponent> instance;

    /**
     * Each major process - a group of operations.
     */
    @Child(name = "process", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Each major process - a group of operations", formalDefinition="Each major process - a group of operations." )
    protected List<ExampleScenarioProcessComponent> process;

    /**
     * Another nested workflow.
     */
    @Child(name = "workflow", type = {CanonicalType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Another nested workflow", formalDefinition="Another nested workflow." )
    protected List<CanonicalType> workflow;

    private static final long serialVersionUID = 758248907L;

  /**
   * Constructor
   */
    public ExampleScenario() {
      super();
    }

  /**
   * Constructor
   */
    public ExampleScenario(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which this example scenario is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which this example scenario is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ExampleScenario setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which this example scenario is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which this example scenario is (or will be) published.
     */
    public ExampleScenario setUrl(String value) { 
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
     * @return {@link #identifier} (A formal identifier that is used to identify this example scenario when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setIdentifier(List<Identifier> theIdentifier) { 
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

    public ExampleScenario addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The identifier that is used to identify this version of the example scenario when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the example scenario when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ExampleScenario setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the example scenario when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the example scenario when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public ExampleScenario setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the example scenario. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.name");
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
     * @param value {@link #name} (A natural language name identifying the example scenario. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ExampleScenario setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the example scenario. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the example scenario. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ExampleScenario setName(String value) { 
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
     * @return {@link #status} (The status of this example scenario. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.status");
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
     * @param value {@link #status} (The status of this example scenario. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ExampleScenario setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this example scenario. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this example scenario. Enables tracking the life-cycle of the content.
     */
    public ExampleScenario setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A Boolean value to indicate that this example scenario is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.experimental");
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
     * @param value {@link #experimental} (A Boolean value to indicate that this example scenario is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ExampleScenario setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A Boolean value to indicate that this example scenario is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A Boolean value to indicate that this example scenario is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public ExampleScenario setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the example scenario was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the example scenario changes. (e.g. the 'content logical definition').). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the example scenario was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the example scenario changes. (e.g. the 'content logical definition').). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ExampleScenario setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the example scenario was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the example scenario changes. (e.g. the 'content logical definition').
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the example scenario was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the example scenario changes. (e.g. the 'content logical definition').
     */
    public ExampleScenario setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the organization or individual that published the example scenario.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.publisher");
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
     * @param value {@link #publisher} (The name of the organization or individual that published the example scenario.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ExampleScenario setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the organization or individual that published the example scenario.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the organization or individual that published the example scenario.
     */
    public ExampleScenario setPublisher(String value) { 
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
    public ExampleScenario setContact(List<ContactDetail> theContact) { 
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

    public ExampleScenario addContact(ContactDetail t) { //3
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate example scenario instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setUseContext(List<UsageContext> theUseContext) { 
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

    public ExampleScenario addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the example scenario is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public ExampleScenario addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #copyright} (A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ExampleScenario setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.
     */
    public ExampleScenario setCopyright(String value) { 
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
     * @return {@link #purpose} (What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExampleScenario.purpose");
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
     * @param value {@link #purpose} (What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public ExampleScenario setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.
     */
    public ExampleScenario setPurpose(String value) { 
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
     * @return {@link #actor} (Actor participating in the resource.)
     */
    public List<ExampleScenarioActorComponent> getActor() { 
      if (this.actor == null)
        this.actor = new ArrayList<ExampleScenarioActorComponent>();
      return this.actor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setActor(List<ExampleScenarioActorComponent> theActor) { 
      this.actor = theActor;
      return this;
    }

    public boolean hasActor() { 
      if (this.actor == null)
        return false;
      for (ExampleScenarioActorComponent item : this.actor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ExampleScenarioActorComponent addActor() { //3
      ExampleScenarioActorComponent t = new ExampleScenarioActorComponent();
      if (this.actor == null)
        this.actor = new ArrayList<ExampleScenarioActorComponent>();
      this.actor.add(t);
      return t;
    }

    public ExampleScenario addActor(ExampleScenarioActorComponent t) { //3
      if (t == null)
        return this;
      if (this.actor == null)
        this.actor = new ArrayList<ExampleScenarioActorComponent>();
      this.actor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #actor}, creating it if it does not already exist
     */
    public ExampleScenarioActorComponent getActorFirstRep() { 
      if (getActor().isEmpty()) {
        addActor();
      }
      return getActor().get(0);
    }

    /**
     * @return {@link #instance} (Each resource and each version that is present in the workflow.)
     */
    public List<ExampleScenarioInstanceComponent> getInstance() { 
      if (this.instance == null)
        this.instance = new ArrayList<ExampleScenarioInstanceComponent>();
      return this.instance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setInstance(List<ExampleScenarioInstanceComponent> theInstance) { 
      this.instance = theInstance;
      return this;
    }

    public boolean hasInstance() { 
      if (this.instance == null)
        return false;
      for (ExampleScenarioInstanceComponent item : this.instance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ExampleScenarioInstanceComponent addInstance() { //3
      ExampleScenarioInstanceComponent t = new ExampleScenarioInstanceComponent();
      if (this.instance == null)
        this.instance = new ArrayList<ExampleScenarioInstanceComponent>();
      this.instance.add(t);
      return t;
    }

    public ExampleScenario addInstance(ExampleScenarioInstanceComponent t) { //3
      if (t == null)
        return this;
      if (this.instance == null)
        this.instance = new ArrayList<ExampleScenarioInstanceComponent>();
      this.instance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #instance}, creating it if it does not already exist
     */
    public ExampleScenarioInstanceComponent getInstanceFirstRep() { 
      if (getInstance().isEmpty()) {
        addInstance();
      }
      return getInstance().get(0);
    }

    /**
     * @return {@link #process} (Each major process - a group of operations.)
     */
    public List<ExampleScenarioProcessComponent> getProcess() { 
      if (this.process == null)
        this.process = new ArrayList<ExampleScenarioProcessComponent>();
      return this.process;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setProcess(List<ExampleScenarioProcessComponent> theProcess) { 
      this.process = theProcess;
      return this;
    }

    public boolean hasProcess() { 
      if (this.process == null)
        return false;
      for (ExampleScenarioProcessComponent item : this.process)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ExampleScenarioProcessComponent addProcess() { //3
      ExampleScenarioProcessComponent t = new ExampleScenarioProcessComponent();
      if (this.process == null)
        this.process = new ArrayList<ExampleScenarioProcessComponent>();
      this.process.add(t);
      return t;
    }

    public ExampleScenario addProcess(ExampleScenarioProcessComponent t) { //3
      if (t == null)
        return this;
      if (this.process == null)
        this.process = new ArrayList<ExampleScenarioProcessComponent>();
      this.process.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #process}, creating it if it does not already exist
     */
    public ExampleScenarioProcessComponent getProcessFirstRep() { 
      if (getProcess().isEmpty()) {
        addProcess();
      }
      return getProcess().get(0);
    }

    /**
     * @return {@link #workflow} (Another nested workflow.)
     */
    public List<CanonicalType> getWorkflow() { 
      if (this.workflow == null)
        this.workflow = new ArrayList<CanonicalType>();
      return this.workflow;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExampleScenario setWorkflow(List<CanonicalType> theWorkflow) { 
      this.workflow = theWorkflow;
      return this;
    }

    public boolean hasWorkflow() { 
      if (this.workflow == null)
        return false;
      for (CanonicalType item : this.workflow)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #workflow} (Another nested workflow.)
     */
    public CanonicalType addWorkflowElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.workflow == null)
        this.workflow = new ArrayList<CanonicalType>();
      this.workflow.add(t);
      return t;
    }

    /**
     * @param value {@link #workflow} (Another nested workflow.)
     */
    public ExampleScenario addWorkflow(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.workflow == null)
        this.workflow = new ArrayList<CanonicalType>();
      this.workflow.add(t);
      return this;
    }

    /**
     * @param value {@link #workflow} (Another nested workflow.)
     */
    public boolean hasWorkflow(String value) { 
      if (this.workflow == null)
        return false;
      for (CanonicalType v : this.workflow)
        if (v.getValue().equals(value)) // canonical(ExampleScenario)
          return true;
      return false;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which this example scenario is (or will be) published.", 0, 1, url));
        children.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this example scenario when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the example scenario when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the example scenario. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("status", "code", "The status of this example scenario. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A Boolean value to indicate that this example scenario is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the example scenario was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the example scenario changes. (e.g. the 'content logical definition').", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the organization or individual that published the example scenario.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate example scenario instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the example scenario is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.", 0, 1, copyright));
        children.add(new Property("purpose", "markdown", "What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.", 0, 1, purpose));
        children.add(new Property("actor", "", "Actor participating in the resource.", 0, java.lang.Integer.MAX_VALUE, actor));
        children.add(new Property("instance", "", "Each resource and each version that is present in the workflow.", 0, java.lang.Integer.MAX_VALUE, instance));
        children.add(new Property("process", "", "Each major process - a group of operations.", 0, java.lang.Integer.MAX_VALUE, process));
        children.add(new Property("workflow", "canonical(ExampleScenario)", "Another nested workflow.", 0, java.lang.Integer.MAX_VALUE, workflow));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which this example scenario is (or will be) published.", 0, 1, url);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A formal identifier that is used to identify this example scenario when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the example scenario when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the example scenario. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this example scenario. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A Boolean value to indicate that this example scenario is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the example scenario was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the example scenario changes. (e.g. the 'content logical definition').", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the organization or individual that published the example scenario.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate example scenario instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the example scenario is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the example scenario.", 0, 1, copyright);
        case -220463842: /*purpose*/  return new Property("purpose", "markdown", "What the example scenario resource is created for. This should not be used to show the business purpose of the scenario itself, but the purpose of documenting a scenario.", 0, 1, purpose);
        case 92645877: /*actor*/  return new Property("actor", "", "Actor participating in the resource.", 0, java.lang.Integer.MAX_VALUE, actor);
        case 555127957: /*instance*/  return new Property("instance", "", "Each resource and each version that is present in the workflow.", 0, java.lang.Integer.MAX_VALUE, instance);
        case -309518737: /*process*/  return new Property("process", "", "Each major process - a group of operations.", 0, java.lang.Integer.MAX_VALUE, process);
        case 35379135: /*workflow*/  return new Property("workflow", "canonical(ExampleScenario)", "Another nested workflow.", 0, java.lang.Integer.MAX_VALUE, workflow);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : this.actor.toArray(new Base[this.actor.size()]); // ExampleScenarioActorComponent
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : this.instance.toArray(new Base[this.instance.size()]); // ExampleScenarioInstanceComponent
        case -309518737: /*process*/ return this.process == null ? new Base[0] : this.process.toArray(new Base[this.process.size()]); // ExampleScenarioProcessComponent
        case 35379135: /*workflow*/ return this.workflow == null ? new Base[0] : this.workflow.toArray(new Base[this.workflow.size()]); // CanonicalType
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
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 92645877: // actor
          this.getActor().add((ExampleScenarioActorComponent) value); // ExampleScenarioActorComponent
          return value;
        case 555127957: // instance
          this.getInstance().add((ExampleScenarioInstanceComponent) value); // ExampleScenarioInstanceComponent
          return value;
        case -309518737: // process
          this.getProcess().add((ExampleScenarioProcessComponent) value); // ExampleScenarioProcessComponent
          return value;
        case 35379135: // workflow
          this.getWorkflow().add(castToCanonical(value)); // CanonicalType
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
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("actor")) {
          this.getActor().add((ExampleScenarioActorComponent) value);
        } else if (name.equals("instance")) {
          this.getInstance().add((ExampleScenarioInstanceComponent) value);
        } else if (name.equals("process")) {
          this.getProcess().add((ExampleScenarioProcessComponent) value);
        } else if (name.equals("workflow")) {
          this.getWorkflow().add(castToCanonical(value));
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
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 1522889671:  return getCopyrightElement();
        case -220463842:  return getPurposeElement();
        case 92645877:  return addActor(); 
        case 555127957:  return addInstance(); 
        case -309518737:  return addProcess(); 
        case 35379135:  return addWorkflowElement();
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
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 92645877: /*actor*/ return new String[] {};
        case 555127957: /*instance*/ return new String[] {};
        case -309518737: /*process*/ return new String[] {};
        case 35379135: /*workflow*/ return new String[] {"canonical"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.copyright");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.purpose");
        }
        else if (name.equals("actor")) {
          return addActor();
        }
        else if (name.equals("instance")) {
          return addInstance();
        }
        else if (name.equals("process")) {
          return addProcess();
        }
        else if (name.equals("workflow")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExampleScenario.workflow");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ExampleScenario";

  }

      public ExampleScenario copy() {
        ExampleScenario dst = new ExampleScenario();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
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
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        if (actor != null) {
          dst.actor = new ArrayList<ExampleScenarioActorComponent>();
          for (ExampleScenarioActorComponent i : actor)
            dst.actor.add(i.copy());
        };
        if (instance != null) {
          dst.instance = new ArrayList<ExampleScenarioInstanceComponent>();
          for (ExampleScenarioInstanceComponent i : instance)
            dst.instance.add(i.copy());
        };
        if (process != null) {
          dst.process = new ArrayList<ExampleScenarioProcessComponent>();
          for (ExampleScenarioProcessComponent i : process)
            dst.process.add(i.copy());
        };
        if (workflow != null) {
          dst.workflow = new ArrayList<CanonicalType>();
          for (CanonicalType i : workflow)
            dst.workflow.add(i.copy());
        };
        return dst;
      }

      protected ExampleScenario typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ExampleScenario))
          return false;
        ExampleScenario o = (ExampleScenario) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(actor, o.actor, true) && compareDeep(instance, o.instance, true)
           && compareDeep(process, o.process, true) && compareDeep(workflow, o.workflow, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ExampleScenario))
          return false;
        ExampleScenario o = (ExampleScenario) other_;
        return compareValues(copyright, o.copyright, true) && compareValues(purpose, o.purpose, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, copyright, purpose
          , actor, instance, process, workflow);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ExampleScenario;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The example scenario publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ExampleScenario.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ExampleScenario.date", description="The example scenario publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The example scenario publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ExampleScenario.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ExampleScenario.identifier", description="External identifier for the example scenario", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ExampleScenario.jurisdiction", description="Intended jurisdiction for the example scenario", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the example scenario</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExampleScenario.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ExampleScenario.name", description="Computationally friendly name of the example scenario", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the example scenario</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExampleScenario.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the example scenario</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExampleScenario.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ExampleScenario.publisher", description="Name of the publisher of the example scenario", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the example scenario</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExampleScenario.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ExampleScenario.version", description="The business version of the example scenario", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the example scenario</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ExampleScenario.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ExampleScenario.url", description="The uri that identifies the example scenario", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the example scenario</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ExampleScenario.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ExampleScenario.status", description="The current status of the example scenario", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the example scenario</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExampleScenario.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

