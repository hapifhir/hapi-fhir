package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ResourceTypes {

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
         * A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
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
         * Catalog entries are wrappers that contextualize items included in a catalog.
         */
        CATALOGENTRY, 
        /**
         * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.
         */
        CHARGEITEM, 
        /**
         * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.
         */
        CHARGEITEMDEFINITION, 
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
         * A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.
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
         * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to reimburse or pay for health care products and services.
         */
        COVERAGE, 
        /**
         * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
         */
        COVERAGEELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
         */
        COVERAGEELIGIBILITYRESPONSE, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).
         */
        DEVICE, 
        /**
         * The characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICEDEFINITION, 
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
         * The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENTDEFINITION, 
        /**
         * Example of workflow instance.
         */
        EXAMPLESCENARIO, 
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
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
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
         * Details of a Health Insurance product/plan provided by an organization.
         */
        INSURANCEPLAN, 
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
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCONTRAINDICATION, 
        /**
         * A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.
         */
        MEDICINALPRODUCTDEVICESPEC, 
        /**
         * Indication for the Medicinal Product.
         */
        MEDICINALPRODUCTINDICATION, 
        /**
         * An ingredient of a manufactured item or pharmaceutical product.
         */
        MEDICINALPRODUCTINGREDIENT, 
        /**
         * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
         */
        MEDICINALPRODUCTINTERACTION, 
        /**
         * The manufactured item as contained in the packaged medicinal product.
         */
        MEDICINALPRODUCTMANUFACTURED, 
        /**
         * A medicinal product in a container or package.
         */
        MEDICINALPRODUCTPACKAGED, 
        /**
         * A pharmaceutical product described in terms of its composition and dose form.
         */
        MEDICINALPRODUCTPHARMACEUTICAL, 
        /**
         * Describe the undesirable effects of the medicinal product.
         */
        MEDICINALPRODUCTUNDESIRABLEEFFECT, 
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
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.
         */
        ORGANIZATION, 
        /**
         * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.
         */
        ORGANIZATIONAFFILIATION, 
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
         * An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.
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
         * A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).
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
         * added to help the parsers
         */
        NULL;
        public static ResourceTypes fromCode(String codeString) throws FHIRException {
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
        if ("CatalogEntry".equals(codeString))
          return CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return CHARGEITEMDEFINITION;
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
        if ("CoverageEligibilityRequest".equals(codeString))
          return COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return DEVICEDEFINITION;
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
        if ("EventDefinition".equals(codeString))
          return EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return EXAMPLESCENARIO;
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
        if ("InsurancePlan".equals(codeString))
          return INSURANCEPLAN;
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
        if ("MedicinalProductContraindication".equals(codeString))
          return MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return MEDICINALPRODUCTUNDESIRABLEEFFECT;
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
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return ORGANIZATIONAFFILIATION;
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
        throw new FHIRException("Unknown ResourceTypes code '"+codeString+"'");
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
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
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
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
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
            case INSURANCEPLAN: return "InsurancePlan";
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
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
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
          return "http://hl7.org/fhir/resource-types";
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
            case BINARY: return "A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BIOLOGICALLYDERIVEDPRODUCT: return "A material substance originating from a biological entity intended to be transplanted or infused\ninto another (possibly the same) biological entity.";
            case BODYSTRUCTURE: return "Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAPABILITYSTATEMENT: return "A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CARETEAM: return "The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.";
            case CATALOGENTRY: return "Catalog entries are wrappers that contextualize items included in a catalog.";
            case CHARGEITEM: return "The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.";
            case CHARGEITEMDEFINITION: return "The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case CODESYSTEM: return "The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPARTMENTDEFINITION: return "A compartment definition that defines how resources are accessed on a server.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.";
            case CONDITION: return "A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.";
            case CONSENT: return "A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.";
            case CONTRACT: return "Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.";
            case COVERAGE: return "Financial instrument which may be used to reimburse or pay for health care products and services.";
            case COVERAGEELIGIBILITYREQUEST: return "The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.";
            case COVERAGEELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).";
            case DEVICEDEFINITION: return "The characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A collection of documents compiled for a purpose together with metadata that applies to the collection.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case DOMAINRESOURCE: return "A resource that includes narrative, extensions, and contained resources.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENDPOINT: return "The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EVENTDEFINITION: return "The EventDefinition resource provides a reusable description of when a particular event can occur.";
            case EXAMPLESCENARIO: return "Example of workflow instance.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GRAPHDEFINITION: return "A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case GUIDANCERESPONSE: return "A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.";
            case IMMUNIZATIONEVALUATION: return "Describes a comparison of an immunization event against published recommendations to determine if the administration is \"valid\" in relation to those  recommendations.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.";
            case INSURANCEPLAN: return "Details of a Health Insurance product/plan provided by an organization.";
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
            case MEDICINALPRODUCTCONTRAINDICATION: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTDEVICESPEC: return "A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.";
            case MEDICINALPRODUCTINDICATION: return "Indication for the Medicinal Product.";
            case MEDICINALPRODUCTINGREDIENT: return "An ingredient of a manufactured item or pharmaceutical product.";
            case MEDICINALPRODUCTINTERACTION: return "The interactions of the medicinal product with other medicinal products, or other forms of interactions.";
            case MEDICINALPRODUCTMANUFACTURED: return "The manufactured item as contained in the packaged medicinal product.";
            case MEDICINALPRODUCTPACKAGED: return "A medicinal product in a container or package.";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "A pharmaceutical product described in terms of its composition and dose form.";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "Describe the undesirable effects of the medicinal product.";
            case MESSAGEDEFINITION: return "Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OBSERVATIONDEFINITION: return "Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.";
            case ORGANIZATIONAFFILIATION: return "Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PRACTITIONERROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PROCEDURE: return "An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
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
            case VALUESET: return "A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).";
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
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
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
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
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
            case INSURANCEPLAN: return "InsurancePlan";
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
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
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

