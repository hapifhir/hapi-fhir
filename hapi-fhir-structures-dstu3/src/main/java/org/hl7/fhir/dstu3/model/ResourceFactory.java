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

// Generated on Thu, Aug 25, 2016 23:04-0400 for FHIR v1.6.0

import org.hl7.fhir.exceptions.FHIRException;

public class ResourceFactory extends Factory {

    public static Resource createResource(String name) throws FHIRException {
        if ("Appointment".equals(name))
            return new Appointment();
        if ("ReferralRequest".equals(name))
            return new ReferralRequest();
        if ("Account".equals(name))
            return new Account();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("Goal".equals(name))
            return new Goal();
        if ("Endpoint".equals(name))
            return new Endpoint();
        if ("EnrollmentRequest".equals(name))
            return new EnrollmentRequest();
        if ("Consent".equals(name))
            return new Consent();
        if ("Medication".equals(name))
            return new Medication();
        if ("Measure".equals(name))
            return new Measure();
        if ("Subscription".equals(name))
            return new Subscription();
        if ("DocumentReference".equals(name))
            return new DocumentReference();
        if ("ImagingManifest".equals(name))
            return new ImagingManifest();
        if ("Parameters".equals(name))
            return new Parameters();
        if ("Conformance".equals(name))
            return new Conformance();
        if ("MeasureReport".equals(name))
            return new MeasureReport();
        if ("PractitionerRole".equals(name))
            return new PractitionerRole();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("ExpansionProfile".equals(name))
            return new ExpansionProfile();
        if ("Slot".equals(name))
            return new Slot();
        if ("Person".equals(name))
            return new Person();
        if ("Contract".equals(name))
            return new Contract();
        if ("RiskAssessment".equals(name))
            return new RiskAssessment();
        if ("Group".equals(name))
            return new Group();
        if ("PaymentNotice".equals(name))
            return new PaymentNotice();
        if ("Organization".equals(name))
            return new Organization();
        if ("CareTeam".equals(name))
            return new CareTeam();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("DeviceComponent".equals(name))
            return new DeviceComponent();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("Substance".equals(name))
            return new Substance();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("Communication".equals(name))
            return new Communication();
        if ("ActivityDefinition".equals(name))
            return new ActivityDefinition();
        if ("Linkage".equals(name))
            return new Linkage();
        if ("DeviceUseStatement".equals(name))
            return new DeviceUseStatement();
        if ("MessageHeader".equals(name))
            return new MessageHeader();
        if ("ImmunizationRecommendation".equals(name))
            return new ImmunizationRecommendation();
        if ("BodySite".equals(name))
            return new BodySite();
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Task".equals(name))
            return new Task();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("Specimen".equals(name))
            return new Specimen();
        if ("AllergyIntolerance".equals(name))
            return new AllergyIntolerance();
        if ("CarePlan".equals(name))
            return new CarePlan();
        if ("StructureDefinition".equals(name))
            return new StructureDefinition();
        if ("EpisodeOfCare".equals(name))
            return new EpisodeOfCare();
        if ("OperationOutcome".equals(name))
            return new OperationOutcome();
        if ("Procedure".equals(name))
            return new Procedure();
        if ("List".equals(name))
            return new ListResource();
        if ("ConceptMap".equals(name))
            return new ConceptMap();
        if ("ValueSet".equals(name))
            return new ValueSet();
        if ("OperationDefinition".equals(name))
            return new OperationDefinition();
        if ("DiagnosticRequest".equals(name))
            return new DiagnosticRequest();
        if ("Immunization".equals(name))
            return new Immunization();
        if ("Device".equals(name))
            return new Device();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("Media".equals(name))
            return new Media();
        if ("ProcedureRequest".equals(name))
            return new ProcedureRequest();
        if ("EligibilityResponse".equals(name))
            return new EligibilityResponse();
        if ("DeviceUseRequest".equals(name))
            return new DeviceUseRequest();
        if ("Sequence".equals(name))
            return new Sequence();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Flag".equals(name))
            return new Flag();
        if ("CodeSystem".equals(name))
            return new CodeSystem();
        if ("AppointmentResponse".equals(name))
            return new AppointmentResponse();
        if ("StructureMap".equals(name))
            return new StructureMap();
        if ("GuidanceResponse".equals(name))
            return new GuidanceResponse();
        if ("Observation".equals(name))
            return new Observation();
        if ("MedicationAdministration".equals(name))
            return new MedicationAdministration();
        if ("EnrollmentResponse".equals(name))
            return new EnrollmentResponse();
        if ("Binary".equals(name))
            return new Binary();
        if ("Library".equals(name))
            return new Library();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("Basic".equals(name))
            return new Basic();
        if ("ClaimResponse".equals(name))
            return new ClaimResponse();
        if ("EligibilityRequest".equals(name))
            return new EligibilityRequest();
        if ("ProcessRequest".equals(name))
            return new ProcessRequest();
        if ("MedicationDispense".equals(name))
            return new MedicationDispense();
        if ("DiagnosticReport".equals(name))
            return new DiagnosticReport();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("DataElement".equals(name))
            return new DataElement();
        if ("NutritionRequest".equals(name))
            return new NutritionRequest();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("MedicationOrder".equals(name))
            return new MedicationOrder();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("Condition".equals(name))
            return new Condition();
        if ("Composition".equals(name))
            return new Composition();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("CompartmentDefinition".equals(name))
            return new CompartmentDefinition();
        if ("Patient".equals(name))
            return new Patient();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("ProcessResponse".equals(name))
            return new ProcessResponse();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("DecisionSupportServiceModule".equals(name))
            return new DecisionSupportServiceModule();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("PlanDefinition".equals(name))
            return new PlanDefinition();
        if ("Claim".equals(name))
            return new Claim();
        if ("Location".equals(name))
            return new Location();
        else
            throw new FHIRException("Unknown Resource Name '"+name+"'");
    }

    public static Element createType(String name) throws FHIRException {
        if ("Meta".equals(name))
            return new Meta();
        if ("RelatedResource".equals(name))
            return new RelatedResource();
        if ("Address".equals(name))
            return new Address();
        if ("Contributor".equals(name))
            return new Contributor();
        if ("Attachment".equals(name))
            return new Attachment();
        if ("Count".equals(name))
            return new Count();
        if ("DataRequirement".equals(name))
            return new DataRequirement();
        if ("Money".equals(name))
            return new Money();
        if ("HumanName".equals(name))
            return new HumanName();
        if ("ContactPoint".equals(name))
            return new ContactPoint();
        if ("Identifier".equals(name))
            return new Identifier();
        if ("Narrative".equals(name))
            return new Narrative();
        if ("Coding".equals(name))
            return new Coding();
        if ("SampledData".equals(name))
            return new SampledData();
        if ("Ratio".equals(name))
            return new Ratio();
        if ("ElementDefinition".equals(name))
            return new ElementDefinition();
        if ("Distance".equals(name))
            return new Distance();
        if ("Age".equals(name))
            return new Age();
        if ("Reference".equals(name))
            return new Reference();
        if ("TriggerDefinition".equals(name))
            return new TriggerDefinition();
        if ("SimpleQuantity".equals(name))
            return new SimpleQuantity();
        if ("Quantity".equals(name))
            return new Quantity();
        if ("Period".equals(name))
            return new Period();
        if ("Duration".equals(name))
            return new Duration();
        if ("Range".equals(name))
            return new Range();
        if ("Annotation".equals(name))
            return new Annotation();
        if ("Extension".equals(name))
            return new Extension();
        if ("ContactDetail".equals(name))
            return new ContactDetail();
        if ("UsageContext".equals(name))
            return new UsageContext();
        if ("Signature".equals(name))
            return new Signature();
        if ("Timing".equals(name))
            return new Timing();
        if ("CodeableConcept".equals(name))
            return new CodeableConcept();
        if ("ParameterDefinition".equals(name))
            return new ParameterDefinition();
        else
            throw new FHIRException("Unknown Type Name '"+name+"'");
    }

    public static Base createResourceOrType(String name) throws FHIRException {
        if ("Appointment".equals(name))
            return new Appointment();
        if ("ReferralRequest".equals(name))
            return new ReferralRequest();
        if ("Account".equals(name))
            return new Account();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("Goal".equals(name))
            return new Goal();
        if ("Endpoint".equals(name))
            return new Endpoint();
        if ("EnrollmentRequest".equals(name))
            return new EnrollmentRequest();
        if ("Consent".equals(name))
            return new Consent();
        if ("Medication".equals(name))
            return new Medication();
        if ("Measure".equals(name))
            return new Measure();
        if ("Subscription".equals(name))
            return new Subscription();
        if ("DocumentReference".equals(name))
            return new DocumentReference();
        if ("ImagingManifest".equals(name))
            return new ImagingManifest();
        if ("Parameters".equals(name))
            return new Parameters();
        if ("Conformance".equals(name))
            return new Conformance();
        if ("MeasureReport".equals(name))
            return new MeasureReport();
        if ("PractitionerRole".equals(name))
            return new PractitionerRole();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("ExpansionProfile".equals(name))
            return new ExpansionProfile();
        if ("Slot".equals(name))
            return new Slot();
        if ("Person".equals(name))
            return new Person();
        if ("Contract".equals(name))
            return new Contract();
        if ("RiskAssessment".equals(name))
            return new RiskAssessment();
        if ("Group".equals(name))
            return new Group();
        if ("PaymentNotice".equals(name))
            return new PaymentNotice();
        if ("Organization".equals(name))
            return new Organization();
        if ("CareTeam".equals(name))
            return new CareTeam();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("DeviceComponent".equals(name))
            return new DeviceComponent();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("Substance".equals(name))
            return new Substance();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("Communication".equals(name))
            return new Communication();
        if ("ActivityDefinition".equals(name))
            return new ActivityDefinition();
        if ("Linkage".equals(name))
            return new Linkage();
        if ("DeviceUseStatement".equals(name))
            return new DeviceUseStatement();
        if ("MessageHeader".equals(name))
            return new MessageHeader();
        if ("ImmunizationRecommendation".equals(name))
            return new ImmunizationRecommendation();
        if ("BodySite".equals(name))
            return new BodySite();
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Task".equals(name))
            return new Task();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("Specimen".equals(name))
            return new Specimen();
        if ("AllergyIntolerance".equals(name))
            return new AllergyIntolerance();
        if ("CarePlan".equals(name))
            return new CarePlan();
        if ("StructureDefinition".equals(name))
            return new StructureDefinition();
        if ("EpisodeOfCare".equals(name))
            return new EpisodeOfCare();
        if ("OperationOutcome".equals(name))
            return new OperationOutcome();
        if ("Procedure".equals(name))
            return new Procedure();
        if ("List".equals(name))
            return new ListResource();
        if ("ConceptMap".equals(name))
            return new ConceptMap();
        if ("ValueSet".equals(name))
            return new ValueSet();
        if ("OperationDefinition".equals(name))
            return new OperationDefinition();
        if ("DiagnosticRequest".equals(name))
            return new DiagnosticRequest();
        if ("Immunization".equals(name))
            return new Immunization();
        if ("Device".equals(name))
            return new Device();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("Media".equals(name))
            return new Media();
        if ("ProcedureRequest".equals(name))
            return new ProcedureRequest();
        if ("EligibilityResponse".equals(name))
            return new EligibilityResponse();
        if ("DeviceUseRequest".equals(name))
            return new DeviceUseRequest();
        if ("Sequence".equals(name))
            return new Sequence();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Flag".equals(name))
            return new Flag();
        if ("CodeSystem".equals(name))
            return new CodeSystem();
        if ("AppointmentResponse".equals(name))
            return new AppointmentResponse();
        if ("StructureMap".equals(name))
            return new StructureMap();
        if ("GuidanceResponse".equals(name))
            return new GuidanceResponse();
        if ("Observation".equals(name))
            return new Observation();
        if ("MedicationAdministration".equals(name))
            return new MedicationAdministration();
        if ("EnrollmentResponse".equals(name))
            return new EnrollmentResponse();
        if ("Binary".equals(name))
            return new Binary();
        if ("Library".equals(name))
            return new Library();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("Basic".equals(name))
            return new Basic();
        if ("ClaimResponse".equals(name))
            return new ClaimResponse();
        if ("EligibilityRequest".equals(name))
            return new EligibilityRequest();
        if ("ProcessRequest".equals(name))
            return new ProcessRequest();
        if ("MedicationDispense".equals(name))
            return new MedicationDispense();
        if ("DiagnosticReport".equals(name))
            return new DiagnosticReport();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("DataElement".equals(name))
            return new DataElement();
        if ("NutritionRequest".equals(name))
            return new NutritionRequest();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("MedicationOrder".equals(name))
            return new MedicationOrder();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("Condition".equals(name))
            return new Condition();
        if ("Composition".equals(name))
            return new Composition();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("CompartmentDefinition".equals(name))
            return new CompartmentDefinition();
        if ("Patient".equals(name))
            return new Patient();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("ProcessResponse".equals(name))
            return new ProcessResponse();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("DecisionSupportServiceModule".equals(name))
            return new DecisionSupportServiceModule();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("PlanDefinition".equals(name))
            return new PlanDefinition();
        if ("Claim".equals(name))
            return new Claim();
        if ("Location".equals(name))
            return new Location();
        if ("Meta".equals(name))
            return new Meta();
        if ("RelatedResource".equals(name))
            return new RelatedResource();
        if ("Address".equals(name))
            return new Address();
        if ("Contributor".equals(name))
            return new Contributor();
        if ("Attachment".equals(name))
            return new Attachment();
        if ("Count".equals(name))
            return new Count();
        if ("DataRequirement".equals(name))
            return new DataRequirement();
        if ("Money".equals(name))
            return new Money();
        if ("HumanName".equals(name))
            return new HumanName();
        if ("ContactPoint".equals(name))
            return new ContactPoint();
        if ("Identifier".equals(name))
            return new Identifier();
        if ("Narrative".equals(name))
            return new Narrative();
        if ("Coding".equals(name))
            return new Coding();
        if ("SampledData".equals(name))
            return new SampledData();
        if ("Ratio".equals(name))
            return new Ratio();
        if ("ElementDefinition".equals(name))
            return new ElementDefinition();
        if ("Distance".equals(name))
            return new Distance();
        if ("Age".equals(name))
            return new Age();
        if ("Reference".equals(name))
            return new Reference();
        if ("TriggerDefinition".equals(name))
            return new TriggerDefinition();
        if ("SimpleQuantity".equals(name))
            return new SimpleQuantity();
        if ("Quantity".equals(name))
            return new Quantity();
        if ("Period".equals(name))
            return new Period();
        if ("Duration".equals(name))
            return new Duration();
        if ("Range".equals(name))
            return new Range();
        if ("Annotation".equals(name))
            return new Annotation();
        if ("Extension".equals(name))
            return new Extension();
        if ("ContactDetail".equals(name))
            return new ContactDetail();
        if ("UsageContext".equals(name))
            return new UsageContext();
        if ("Signature".equals(name))
            return new Signature();
        if ("Timing".equals(name))
            return new Timing();
        if ("CodeableConcept".equals(name))
            return new CodeableConcept();
        if ("ParameterDefinition".equals(name))
            return new ParameterDefinition();
        else
            throw new FHIRException("Unknown Resource or Type Name '"+name+"'");
    }


}

