package org.hl7.fhir.instance.model;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2

public class ResourceFactory extends Factory {

    public static Resource createReference(String name) throws Exception {
        if ("Appointment".equals(name))
            return new Appointment();
        if ("ReferralRequest".equals(name))
            return new ReferralRequest();
        if ("Account".equals(name))
            return new Account();
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("Specimen".equals(name))
            return new Specimen();
        if ("AllergyIntolerance".equals(name))
            return new AllergyIntolerance();
        if ("CarePlan".equals(name))
            return new CarePlan();
        if ("Goal".equals(name))
            return new Goal();
        if ("StructureDefinition".equals(name))
            return new StructureDefinition();
        if ("EnrollmentRequest".equals(name))
            return new EnrollmentRequest();
        if ("EpisodeOfCare".equals(name))
            return new EpisodeOfCare();
        if ("OperationOutcome".equals(name))
            return new OperationOutcome();
        if ("Medication".equals(name))
            return new Medication();
        if ("Procedure".equals(name))
            return new Procedure();
        if ("List".equals(name))
            return new List_();
        if ("ConceptMap".equals(name))
            return new ConceptMap();
        if ("Subscription".equals(name))
            return new Subscription();
        if ("ValueSet".equals(name))
            return new ValueSet();
        if ("OperationDefinition".equals(name))
            return new OperationDefinition();
        if ("DocumentReference".equals(name))
            return new DocumentReference();
        if ("Order".equals(name))
            return new Order();
        if ("Immunization".equals(name))
            return new Immunization();
        if ("Parameters".equals(name))
            return new Parameters();
        if ("Device".equals(name))
            return new Device();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("Media".equals(name))
            return new Media();
        if ("Conformance".equals(name))
            return new Conformance();
        if ("ProcedureRequest".equals(name))
            return new ProcedureRequest();
        if ("EligibilityResponse".equals(name))
            return new EligibilityResponse();
        if ("DeviceUseRequest".equals(name))
            return new DeviceUseRequest();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Flag".equals(name))
            return new Flag();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("AppointmentResponse".equals(name))
            return new AppointmentResponse();
        if ("Observation".equals(name))
            return new Observation();
        if ("MedicationAdministration".equals(name))
            return new MedicationAdministration();
        if ("Slot".equals(name))
            return new Slot();
        if ("EnrollmentResponse".equals(name))
            return new EnrollmentResponse();
        if ("Binary".equals(name))
            return new Binary();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("Person".equals(name))
            return new Person();
        if ("Contract".equals(name))
            return new Contract();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("RiskAssessment".equals(name))
            return new RiskAssessment();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("Basic".equals(name))
            return new Basic();
        if ("Group".equals(name))
            return new Group();
        if ("PaymentNotice".equals(name))
            return new PaymentNotice();
        if ("Organization".equals(name))
            return new Organization();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
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
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("ImagingObjectSelection".equals(name))
            return new ImagingObjectSelection();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("DataElement".equals(name))
            return new DataElement();
        if ("DeviceComponent".equals(name))
            return new DeviceComponent();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("NutritionOrder".equals(name))
            return new NutritionOrder();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("Substance".equals(name))
            return new Substance();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("MedicationOrder".equals(name))
            return new MedicationOrder();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("Communication".equals(name))
            return new Communication();
        if ("Condition".equals(name))
            return new Condition();
        if ("Composition".equals(name))
            return new Composition();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("DiagnosticOrder".equals(name))
            return new DiagnosticOrder();
        if ("Patient".equals(name))
            return new Patient();
        if ("OrderResponse".equals(name))
            return new OrderResponse();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("DeviceUseStatement".equals(name))
            return new DeviceUseStatement();
        if ("ProcessResponse".equals(name))
            return new ProcessResponse();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("MessageHeader".equals(name))
            return new MessageHeader();
        if ("Claim".equals(name))
            return new Claim();
        if ("ImmunizationRecommendation".equals(name))
            return new ImmunizationRecommendation();
        if ("Location".equals(name))
            return new Location();
        if ("BodySite".equals(name))
            return new BodySite();
        else
            throw new Exception("Unknown Resource Name '"+name+"'");
    }

    public static Element createType(String name) throws Exception {
        if ("Meta".equals(name))
            return new Meta();
        if ("Address".equals(name))
            return new Address();
        if ("Attachment".equals(name))
            return new Attachment();
        if ("Count".equals(name))
            return new Count();
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
        if ("Signature".equals(name))
            return new Signature();
        if ("Timing".equals(name))
            return new Timing();
        if ("CodeableConcept".equals(name))
            return new CodeableConcept();
        else
            throw new Exception("Unknown Type Name '"+name+"'");
    }

}

