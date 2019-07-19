package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0

import org.hl7.fhir.exceptions.FHIRException;

public class ResourceFactory extends Factory {

    public static Resource createResource(String name) throws FHIRException {
        if ("Condition".equals(name))
            return new Condition();
        if ("DeviceComponent".equals(name))
            return new DeviceComponent();
        if ("Communication".equals(name))
            return new Communication();
        if ("PractitionerRole".equals(name))
            return new PractitionerRole();
        if ("Group".equals(name))
            return new Group();
        if ("ValueSet".equals(name))
            return new ValueSet();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("Appointment".equals(name))
            return new Appointment();
        if ("Library".equals(name))
            return new Library();
        if ("Slot".equals(name))
            return new Slot();
        if ("DecisionSupportRule".equals(name))
            return new DecisionSupportRule();
        if ("Composition".equals(name))
            return new Composition();
        if ("EpisodeOfCare".equals(name))
            return new EpisodeOfCare();
        if ("Conformance".equals(name))
            return new Conformance();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("Linkage".equals(name))
            return new Linkage();
        if ("OrderResponse".equals(name))
            return new OrderResponse();
        if ("Task".equals(name))
            return new Task();
        if ("ConceptMap".equals(name))
            return new ConceptMap();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("CarePlan".equals(name))
            return new CarePlan();
        if ("Substance".equals(name))
            return new Substance();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("DeviceUseRequest".equals(name))
            return new DeviceUseRequest();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("EligibilityRequest".equals(name))
            return new EligibilityRequest();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("OperationDefinition".equals(name))
            return new OperationDefinition();
        if ("ImagingObjectSelection".equals(name))
            return new ImagingObjectSelection();
        if ("ClaimResponse".equals(name))
            return new ClaimResponse();
        if ("BodySite".equals(name))
            return new BodySite();
        if ("Flag".equals(name))
            return new Flag();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("Claim".equals(name))
            return new Claim();
        if ("RiskAssessment".equals(name))
            return new RiskAssessment();
        if ("ExpansionProfile".equals(name))
            return new ExpansionProfile();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("AllergyIntolerance".equals(name))
            return new AllergyIntolerance();
        if ("Observation".equals(name))
            return new Observation();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("ProcessResponse".equals(name))
            return new ProcessResponse();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("EligibilityResponse".equals(name))
            return new EligibilityResponse();
        if ("MedicationOrder".equals(name))
            return new MedicationOrder();
        if ("Person".equals(name))
            return new Person();
        if ("Parameters".equals(name))
            return new Parameters();
        if ("ModuleDefinition".equals(name))
            return new ModuleDefinition();
        if ("ProcedureRequest".equals(name))
            return new ProcedureRequest();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Organization".equals(name))
            return new Organization();
        if ("Measure".equals(name))
            return new Measure();
        if ("ProcessRequest".equals(name))
            return new ProcessRequest();
        if ("ImmunizationRecommendation".equals(name))
            return new ImmunizationRecommendation();
        if ("MedicationDispense".equals(name))
            return new MedicationDispense();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("PaymentNotice".equals(name))
            return new PaymentNotice();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("AppointmentResponse".equals(name))
            return new AppointmentResponse();
        if ("Sequence".equals(name))
            return new Sequence();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
        if ("Protocol".equals(name))
            return new Protocol();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("OperationOutcome".equals(name))
            return new OperationOutcome();
        if ("DecisionSupportServiceModule".equals(name))
            return new DecisionSupportServiceModule();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("Media".equals(name))
            return new Media();
        if ("ImagingExcerpt".equals(name))
            return new ImagingExcerpt();
        if ("Binary".equals(name))
            return new Binary();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("DocumentReference".equals(name))
            return new DocumentReference();
        if ("Immunization".equals(name))
            return new Immunization();
        if ("CareTeam".equals(name))
            return new CareTeam();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("Subscription".equals(name))
            return new Subscription();
        if ("MeasureReport".equals(name))
            return new MeasureReport();
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Device".equals(name))
            return new Device();
        if ("StructureDefinition".equals(name))
            return new StructureDefinition();
        if ("Account".equals(name))
            return new Account();
        if ("Order".equals(name))
            return new Order();
        if ("Procedure".equals(name))
            return new Procedure();
        if ("OrderSet".equals(name))
            return new OrderSet();
        if ("DiagnosticReport".equals(name))
            return new DiagnosticReport();
        if ("Medication".equals(name))
            return new Medication();
        if ("MessageHeader".equals(name))
            return new MessageHeader();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("DataElement".equals(name))
            return new DataElement();
        if ("MedicationAdministration".equals(name))
            return new MedicationAdministration();
        if ("StructureMap".equals(name))
            return new StructureMap();
        if ("CompartmentDefinition".equals(name))
            return new CompartmentDefinition();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("List".equals(name))
            return new ListResource();
        if ("CodeSystem".equals(name))
            return new CodeSystem();
        if ("DeviceUseStatement".equals(name))
            return new DeviceUseStatement();
        if ("Goal".equals(name))
            return new Goal();
        if ("GuidanceResponse".equals(name))
            return new GuidanceResponse();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("NutritionOrder".equals(name))
            return new NutritionOrder();
        if ("ReferralRequest".equals(name))
            return new ReferralRequest();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("EnrollmentRequest".equals(name))
            return new EnrollmentRequest();
        if ("Location".equals(name))
            return new Location();
        if ("Contract".equals(name))
            return new Contract();
        if ("Basic".equals(name))
            return new Basic();
        if ("Specimen".equals(name))
            return new Specimen();
        if ("EnrollmentResponse".equals(name))
            return new EnrollmentResponse();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Patient".equals(name))
            return new Patient();
        if ("DiagnosticOrder".equals(name))
            return new DiagnosticOrder();
        else
            throw new FHIRException("Unknown Resource Name '"+name+"'");
    }

    public static Element createType(String name) throws FHIRException {
        if ("Period".equals(name))
            return new Period();
        if ("Age".equals(name))
            return new Age();
        if ("Attachment".equals(name))
            return new Attachment();
        if ("Count".equals(name))
            return new Count();
        if ("SimpleQuantity".equals(name))
            return new SimpleQuantity();
        if ("Signature".equals(name))
            return new Signature();
        if ("Extension".equals(name))
            return new Extension();
        if ("ModuleMetadata".equals(name))
            return new ModuleMetadata();
        if ("ActionDefinition".equals(name))
            return new ActionDefinition();
        if ("HumanName".equals(name))
            return new HumanName();
        if ("Ratio".equals(name))
            return new Ratio();
        if ("Duration".equals(name))
            return new Duration();
        if ("CodeableConcept".equals(name))
            return new CodeableConcept();
        if ("Identifier".equals(name))
            return new Identifier();
        if ("Timing".equals(name))
            return new Timing();
        if ("Coding".equals(name))
            return new Coding();
        if ("Range".equals(name))
            return new Range();
        if ("Quantity".equals(name))
            return new Quantity();
        if ("Money".equals(name))
            return new Money();
        if ("Distance".equals(name))
            return new Distance();
        if ("DataRequirement".equals(name))
            return new DataRequirement();
        if ("ParameterDefinition".equals(name))
            return new ParameterDefinition();
        if ("ContactPoint".equals(name))
            return new ContactPoint();
        if ("TriggerDefinition".equals(name))
            return new TriggerDefinition();
        if ("ElementDefinition".equals(name))
            return new ElementDefinition();
        if ("Address".equals(name))
            return new Address();
        if ("Annotation".equals(name))
            return new Annotation();
        if ("Meta".equals(name))
            return new Meta();
        if ("SampledData".equals(name))
            return new SampledData();
        if ("Reference".equals(name))
            return new Reference();
        if ("Narrative".equals(name))
            return new Narrative();
        else
            throw new FHIRException("Unknown Type Name '"+name+"'");
    }

    public static Base createResourceOrType(String name) throws FHIRException {
        if ("Condition".equals(name))
            return new Condition();
        if ("DeviceComponent".equals(name))
            return new DeviceComponent();
        if ("Communication".equals(name))
            return new Communication();
        if ("PractitionerRole".equals(name))
            return new PractitionerRole();
        if ("Group".equals(name))
            return new Group();
        if ("ValueSet".equals(name))
            return new ValueSet();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("Appointment".equals(name))
            return new Appointment();
        if ("Library".equals(name))
            return new Library();
        if ("Slot".equals(name))
            return new Slot();
        if ("DecisionSupportRule".equals(name))
            return new DecisionSupportRule();
        if ("Composition".equals(name))
            return new Composition();
        if ("EpisodeOfCare".equals(name))
            return new EpisodeOfCare();
        if ("Conformance".equals(name))
            return new Conformance();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("Linkage".equals(name))
            return new Linkage();
        if ("OrderResponse".equals(name))
            return new OrderResponse();
        if ("Task".equals(name))
            return new Task();
        if ("ConceptMap".equals(name))
            return new ConceptMap();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("CarePlan".equals(name))
            return new CarePlan();
        if ("Substance".equals(name))
            return new Substance();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("DeviceUseRequest".equals(name))
            return new DeviceUseRequest();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("EligibilityRequest".equals(name))
            return new EligibilityRequest();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("OperationDefinition".equals(name))
            return new OperationDefinition();
        if ("ImagingObjectSelection".equals(name))
            return new ImagingObjectSelection();
        if ("ClaimResponse".equals(name))
            return new ClaimResponse();
        if ("BodySite".equals(name))
            return new BodySite();
        if ("Flag".equals(name))
            return new Flag();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("Claim".equals(name))
            return new Claim();
        if ("RiskAssessment".equals(name))
            return new RiskAssessment();
        if ("ExpansionProfile".equals(name))
            return new ExpansionProfile();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("AllergyIntolerance".equals(name))
            return new AllergyIntolerance();
        if ("Observation".equals(name))
            return new Observation();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("ProcessResponse".equals(name))
            return new ProcessResponse();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("EligibilityResponse".equals(name))
            return new EligibilityResponse();
        if ("MedicationOrder".equals(name))
            return new MedicationOrder();
        if ("Person".equals(name))
            return new Person();
        if ("Parameters".equals(name))
            return new Parameters();
        if ("ModuleDefinition".equals(name))
            return new ModuleDefinition();
        if ("ProcedureRequest".equals(name))
            return new ProcedureRequest();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Organization".equals(name))
            return new Organization();
        if ("Measure".equals(name))
            return new Measure();
        if ("ProcessRequest".equals(name))
            return new ProcessRequest();
        if ("ImmunizationRecommendation".equals(name))
            return new ImmunizationRecommendation();
        if ("MedicationDispense".equals(name))
            return new MedicationDispense();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("PaymentNotice".equals(name))
            return new PaymentNotice();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("AppointmentResponse".equals(name))
            return new AppointmentResponse();
        if ("Sequence".equals(name))
            return new Sequence();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
        if ("Protocol".equals(name))
            return new Protocol();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("OperationOutcome".equals(name))
            return new OperationOutcome();
        if ("DecisionSupportServiceModule".equals(name))
            return new DecisionSupportServiceModule();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("Media".equals(name))
            return new Media();
        if ("ImagingExcerpt".equals(name))
            return new ImagingExcerpt();
        if ("Binary".equals(name))
            return new Binary();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("DocumentReference".equals(name))
            return new DocumentReference();
        if ("Immunization".equals(name))
            return new Immunization();
        if ("CareTeam".equals(name))
            return new CareTeam();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("Subscription".equals(name))
            return new Subscription();
        if ("MeasureReport".equals(name))
            return new MeasureReport();
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Device".equals(name))
            return new Device();
        if ("StructureDefinition".equals(name))
            return new StructureDefinition();
        if ("Account".equals(name))
            return new Account();
        if ("Order".equals(name))
            return new Order();
        if ("Procedure".equals(name))
            return new Procedure();
        if ("OrderSet".equals(name))
            return new OrderSet();
        if ("DiagnosticReport".equals(name))
            return new DiagnosticReport();
        if ("Medication".equals(name))
            return new Medication();
        if ("MessageHeader".equals(name))
            return new MessageHeader();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("DataElement".equals(name))
            return new DataElement();
        if ("MedicationAdministration".equals(name))
            return new MedicationAdministration();
        if ("StructureMap".equals(name))
            return new StructureMap();
        if ("CompartmentDefinition".equals(name))
            return new CompartmentDefinition();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("List".equals(name))
            return new ListResource();
        if ("CodeSystem".equals(name))
            return new CodeSystem();
        if ("DeviceUseStatement".equals(name))
            return new DeviceUseStatement();
        if ("Goal".equals(name))
            return new Goal();
        if ("GuidanceResponse".equals(name))
            return new GuidanceResponse();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("NutritionOrder".equals(name))
            return new NutritionOrder();
        if ("ReferralRequest".equals(name))
            return new ReferralRequest();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("EnrollmentRequest".equals(name))
            return new EnrollmentRequest();
        if ("Location".equals(name))
            return new Location();
        if ("Contract".equals(name))
            return new Contract();
        if ("Basic".equals(name))
            return new Basic();
        if ("Specimen".equals(name))
            return new Specimen();
        if ("EnrollmentResponse".equals(name))
            return new EnrollmentResponse();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Patient".equals(name))
            return new Patient();
        if ("DiagnosticOrder".equals(name))
            return new DiagnosticOrder();
        if ("Period".equals(name))
            return new Period();
        if ("Age".equals(name))
            return new Age();
        if ("Attachment".equals(name))
            return new Attachment();
        if ("Count".equals(name))
            return new Count();
        if ("SimpleQuantity".equals(name))
            return new SimpleQuantity();
        if ("Signature".equals(name))
            return new Signature();
        if ("Extension".equals(name))
            return new Extension();
        if ("ModuleMetadata".equals(name))
            return new ModuleMetadata();
        if ("ActionDefinition".equals(name))
            return new ActionDefinition();
        if ("HumanName".equals(name))
            return new HumanName();
        if ("Ratio".equals(name))
            return new Ratio();
        if ("Duration".equals(name))
            return new Duration();
        if ("CodeableConcept".equals(name))
            return new CodeableConcept();
        if ("Identifier".equals(name))
            return new Identifier();
        if ("Timing".equals(name))
            return new Timing();
        if ("Coding".equals(name))
            return new Coding();
        if ("Range".equals(name))
            return new Range();
        if ("Quantity".equals(name))
            return new Quantity();
        if ("Money".equals(name))
            return new Money();
        if ("Distance".equals(name))
            return new Distance();
        if ("DataRequirement".equals(name))
            return new DataRequirement();
        if ("ParameterDefinition".equals(name))
            return new ParameterDefinition();
        if ("ContactPoint".equals(name))
            return new ContactPoint();
        if ("TriggerDefinition".equals(name))
            return new TriggerDefinition();
        if ("ElementDefinition".equals(name))
            return new ElementDefinition();
        if ("Address".equals(name))
            return new Address();
        if ("Annotation".equals(name))
            return new Annotation();
        if ("Meta".equals(name))
            return new Meta();
        if ("SampledData".equals(name))
            return new SampledData();
        if ("Reference".equals(name))
            return new Reference();
        if ("Narrative".equals(name))
            return new Narrative();
        else
            throw new FHIRException("Unknown Resource or Type Name '"+name+"'");
    }


}

