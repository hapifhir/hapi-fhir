package org.hl7.fhir.dstu3.model.codesystems;

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


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ResourceTypesEnumFactory implements EnumFactory<ResourceTypes> {

  public ResourceTypes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Account".equals(codeString))
      return ResourceTypes.ACCOUNT;
    if ("ActivityDefinition".equals(codeString))
      return ResourceTypes.ACTIVITYDEFINITION;
    if ("AdverseEvent".equals(codeString))
      return ResourceTypes.ADVERSEEVENT;
    if ("AllergyIntolerance".equals(codeString))
      return ResourceTypes.ALLERGYINTOLERANCE;
    if ("Appointment".equals(codeString))
      return ResourceTypes.APPOINTMENT;
    if ("AppointmentResponse".equals(codeString))
      return ResourceTypes.APPOINTMENTRESPONSE;
    if ("AuditEvent".equals(codeString))
      return ResourceTypes.AUDITEVENT;
    if ("Basic".equals(codeString))
      return ResourceTypes.BASIC;
    if ("Binary".equals(codeString))
      return ResourceTypes.BINARY;
    if ("BodySite".equals(codeString))
      return ResourceTypes.BODYSITE;
    if ("Bundle".equals(codeString))
      return ResourceTypes.BUNDLE;
    if ("CapabilityStatement".equals(codeString))
      return ResourceTypes.CAPABILITYSTATEMENT;
    if ("CarePlan".equals(codeString))
      return ResourceTypes.CAREPLAN;
    if ("CareTeam".equals(codeString))
      return ResourceTypes.CARETEAM;
    if ("ChargeItem".equals(codeString))
      return ResourceTypes.CHARGEITEM;
    if ("Claim".equals(codeString))
      return ResourceTypes.CLAIM;
    if ("ClaimResponse".equals(codeString))
      return ResourceTypes.CLAIMRESPONSE;
    if ("ClinicalImpression".equals(codeString))
      return ResourceTypes.CLINICALIMPRESSION;
    if ("CodeSystem".equals(codeString))
      return ResourceTypes.CODESYSTEM;
    if ("Communication".equals(codeString))
      return ResourceTypes.COMMUNICATION;
    if ("CommunicationRequest".equals(codeString))
      return ResourceTypes.COMMUNICATIONREQUEST;
    if ("CompartmentDefinition".equals(codeString))
      return ResourceTypes.COMPARTMENTDEFINITION;
    if ("Composition".equals(codeString))
      return ResourceTypes.COMPOSITION;
    if ("ConceptMap".equals(codeString))
      return ResourceTypes.CONCEPTMAP;
    if ("Condition".equals(codeString))
      return ResourceTypes.CONDITION;
    if ("Consent".equals(codeString))
      return ResourceTypes.CONSENT;
    if ("Contract".equals(codeString))
      return ResourceTypes.CONTRACT;
    if ("Coverage".equals(codeString))
      return ResourceTypes.COVERAGE;
    if ("DataElement".equals(codeString))
      return ResourceTypes.DATAELEMENT;
    if ("DetectedIssue".equals(codeString))
      return ResourceTypes.DETECTEDISSUE;
    if ("Device".equals(codeString))
      return ResourceTypes.DEVICE;
    if ("DeviceComponent".equals(codeString))
      return ResourceTypes.DEVICECOMPONENT;
    if ("DeviceMetric".equals(codeString))
      return ResourceTypes.DEVICEMETRIC;
    if ("DeviceRequest".equals(codeString))
      return ResourceTypes.DEVICEREQUEST;
    if ("DeviceUseStatement".equals(codeString))
      return ResourceTypes.DEVICEUSESTATEMENT;
    if ("DiagnosticReport".equals(codeString))
      return ResourceTypes.DIAGNOSTICREPORT;
    if ("DocumentManifest".equals(codeString))
      return ResourceTypes.DOCUMENTMANIFEST;
    if ("DocumentReference".equals(codeString))
      return ResourceTypes.DOCUMENTREFERENCE;
    if ("DomainResource".equals(codeString))
      return ResourceTypes.DOMAINRESOURCE;
    if ("EligibilityRequest".equals(codeString))
      return ResourceTypes.ELIGIBILITYREQUEST;
    if ("EligibilityResponse".equals(codeString))
      return ResourceTypes.ELIGIBILITYRESPONSE;
    if ("Encounter".equals(codeString))
      return ResourceTypes.ENCOUNTER;
    if ("Endpoint".equals(codeString))
      return ResourceTypes.ENDPOINT;
    if ("EnrollmentRequest".equals(codeString))
      return ResourceTypes.ENROLLMENTREQUEST;
    if ("EnrollmentResponse".equals(codeString))
      return ResourceTypes.ENROLLMENTRESPONSE;
    if ("EpisodeOfCare".equals(codeString))
      return ResourceTypes.EPISODEOFCARE;
    if ("ExpansionProfile".equals(codeString))
      return ResourceTypes.EXPANSIONPROFILE;
    if ("ExplanationOfBenefit".equals(codeString))
      return ResourceTypes.EXPLANATIONOFBENEFIT;
    if ("FamilyMemberHistory".equals(codeString))
      return ResourceTypes.FAMILYMEMBERHISTORY;
    if ("Flag".equals(codeString))
      return ResourceTypes.FLAG;
    if ("Goal".equals(codeString))
      return ResourceTypes.GOAL;
    if ("GraphDefinition".equals(codeString))
      return ResourceTypes.GRAPHDEFINITION;
    if ("Group".equals(codeString))
      return ResourceTypes.GROUP;
    if ("GuidanceResponse".equals(codeString))
      return ResourceTypes.GUIDANCERESPONSE;
    if ("HealthcareService".equals(codeString))
      return ResourceTypes.HEALTHCARESERVICE;
    if ("ImagingManifest".equals(codeString))
      return ResourceTypes.IMAGINGMANIFEST;
    if ("ImagingStudy".equals(codeString))
      return ResourceTypes.IMAGINGSTUDY;
    if ("Immunization".equals(codeString))
      return ResourceTypes.IMMUNIZATION;
    if ("ImmunizationRecommendation".equals(codeString))
      return ResourceTypes.IMMUNIZATIONRECOMMENDATION;
    if ("ImplementationGuide".equals(codeString))
      return ResourceTypes.IMPLEMENTATIONGUIDE;
    if ("Library".equals(codeString))
      return ResourceTypes.LIBRARY;
    if ("Linkage".equals(codeString))
      return ResourceTypes.LINKAGE;
    if ("List".equals(codeString))
      return ResourceTypes.LIST;
    if ("Location".equals(codeString))
      return ResourceTypes.LOCATION;
    if ("Measure".equals(codeString))
      return ResourceTypes.MEASURE;
    if ("MeasureReport".equals(codeString))
      return ResourceTypes.MEASUREREPORT;
    if ("Media".equals(codeString))
      return ResourceTypes.MEDIA;
    if ("Medication".equals(codeString))
      return ResourceTypes.MEDICATION;
    if ("MedicationAdministration".equals(codeString))
      return ResourceTypes.MEDICATIONADMINISTRATION;
    if ("MedicationDispense".equals(codeString))
      return ResourceTypes.MEDICATIONDISPENSE;
    if ("MedicationRequest".equals(codeString))
      return ResourceTypes.MEDICATIONREQUEST;
    if ("MedicationStatement".equals(codeString))
      return ResourceTypes.MEDICATIONSTATEMENT;
    if ("MessageDefinition".equals(codeString))
      return ResourceTypes.MESSAGEDEFINITION;
    if ("MessageHeader".equals(codeString))
      return ResourceTypes.MESSAGEHEADER;
    if ("NamingSystem".equals(codeString))
      return ResourceTypes.NAMINGSYSTEM;
    if ("NutritionOrder".equals(codeString))
      return ResourceTypes.NUTRITIONORDER;
    if ("Observation".equals(codeString))
      return ResourceTypes.OBSERVATION;
    if ("OperationDefinition".equals(codeString))
      return ResourceTypes.OPERATIONDEFINITION;
    if ("OperationOutcome".equals(codeString))
      return ResourceTypes.OPERATIONOUTCOME;
    if ("Organization".equals(codeString))
      return ResourceTypes.ORGANIZATION;
    if ("Parameters".equals(codeString))
      return ResourceTypes.PARAMETERS;
    if ("Patient".equals(codeString))
      return ResourceTypes.PATIENT;
    if ("PaymentNotice".equals(codeString))
      return ResourceTypes.PAYMENTNOTICE;
    if ("PaymentReconciliation".equals(codeString))
      return ResourceTypes.PAYMENTRECONCILIATION;
    if ("Person".equals(codeString))
      return ResourceTypes.PERSON;
    if ("PlanDefinition".equals(codeString))
      return ResourceTypes.PLANDEFINITION;
    if ("Practitioner".equals(codeString))
      return ResourceTypes.PRACTITIONER;
    if ("PractitionerRole".equals(codeString))
      return ResourceTypes.PRACTITIONERROLE;
    if ("Procedure".equals(codeString))
      return ResourceTypes.PROCEDURE;
    if ("ProcedureRequest".equals(codeString))
      return ResourceTypes.PROCEDUREREQUEST;
    if ("ProcessRequest".equals(codeString))
      return ResourceTypes.PROCESSREQUEST;
    if ("ProcessResponse".equals(codeString))
      return ResourceTypes.PROCESSRESPONSE;
    if ("Provenance".equals(codeString))
      return ResourceTypes.PROVENANCE;
    if ("Questionnaire".equals(codeString))
      return ResourceTypes.QUESTIONNAIRE;
    if ("QuestionnaireResponse".equals(codeString))
      return ResourceTypes.QUESTIONNAIRERESPONSE;
    if ("ReferralRequest".equals(codeString))
      return ResourceTypes.REFERRALREQUEST;
    if ("RelatedPerson".equals(codeString))
      return ResourceTypes.RELATEDPERSON;
    if ("RequestGroup".equals(codeString))
      return ResourceTypes.REQUESTGROUP;
    if ("ResearchStudy".equals(codeString))
      return ResourceTypes.RESEARCHSTUDY;
    if ("ResearchSubject".equals(codeString))
      return ResourceTypes.RESEARCHSUBJECT;
    if ("Resource".equals(codeString))
      return ResourceTypes.RESOURCE;
    if ("RiskAssessment".equals(codeString))
      return ResourceTypes.RISKASSESSMENT;
    if ("Schedule".equals(codeString))
      return ResourceTypes.SCHEDULE;
    if ("SearchParameter".equals(codeString))
      return ResourceTypes.SEARCHPARAMETER;
    if ("Sequence".equals(codeString))
      return ResourceTypes.SEQUENCE;
    if ("ServiceDefinition".equals(codeString))
      return ResourceTypes.SERVICEDEFINITION;
    if ("Slot".equals(codeString))
      return ResourceTypes.SLOT;
    if ("Specimen".equals(codeString))
      return ResourceTypes.SPECIMEN;
    if ("StructureDefinition".equals(codeString))
      return ResourceTypes.STRUCTUREDEFINITION;
    if ("StructureMap".equals(codeString))
      return ResourceTypes.STRUCTUREMAP;
    if ("Subscription".equals(codeString))
      return ResourceTypes.SUBSCRIPTION;
    if ("Substance".equals(codeString))
      return ResourceTypes.SUBSTANCE;
    if ("SupplyDelivery".equals(codeString))
      return ResourceTypes.SUPPLYDELIVERY;
    if ("SupplyRequest".equals(codeString))
      return ResourceTypes.SUPPLYREQUEST;
    if ("Task".equals(codeString))
      return ResourceTypes.TASK;
    if ("TestReport".equals(codeString))
      return ResourceTypes.TESTREPORT;
    if ("TestScript".equals(codeString))
      return ResourceTypes.TESTSCRIPT;
    if ("ValueSet".equals(codeString))
      return ResourceTypes.VALUESET;
    if ("VisionPrescription".equals(codeString))
      return ResourceTypes.VISIONPRESCRIPTION;
    throw new IllegalArgumentException("Unknown ResourceTypes code '"+codeString+"'");
  }

  public String toCode(ResourceTypes code) {
    if (code == ResourceTypes.ACCOUNT)
      return "Account";
    if (code == ResourceTypes.ACTIVITYDEFINITION)
      return "ActivityDefinition";
    if (code == ResourceTypes.ADVERSEEVENT)
      return "AdverseEvent";
    if (code == ResourceTypes.ALLERGYINTOLERANCE)
      return "AllergyIntolerance";
    if (code == ResourceTypes.APPOINTMENT)
      return "Appointment";
    if (code == ResourceTypes.APPOINTMENTRESPONSE)
      return "AppointmentResponse";
    if (code == ResourceTypes.AUDITEVENT)
      return "AuditEvent";
    if (code == ResourceTypes.BASIC)
      return "Basic";
    if (code == ResourceTypes.BINARY)
      return "Binary";
    if (code == ResourceTypes.BODYSITE)
      return "BodySite";
    if (code == ResourceTypes.BUNDLE)
      return "Bundle";
    if (code == ResourceTypes.CAPABILITYSTATEMENT)
      return "CapabilityStatement";
    if (code == ResourceTypes.CAREPLAN)
      return "CarePlan";
    if (code == ResourceTypes.CARETEAM)
      return "CareTeam";
    if (code == ResourceTypes.CHARGEITEM)
      return "ChargeItem";
    if (code == ResourceTypes.CLAIM)
      return "Claim";
    if (code == ResourceTypes.CLAIMRESPONSE)
      return "ClaimResponse";
    if (code == ResourceTypes.CLINICALIMPRESSION)
      return "ClinicalImpression";
    if (code == ResourceTypes.CODESYSTEM)
      return "CodeSystem";
    if (code == ResourceTypes.COMMUNICATION)
      return "Communication";
    if (code == ResourceTypes.COMMUNICATIONREQUEST)
      return "CommunicationRequest";
    if (code == ResourceTypes.COMPARTMENTDEFINITION)
      return "CompartmentDefinition";
    if (code == ResourceTypes.COMPOSITION)
      return "Composition";
    if (code == ResourceTypes.CONCEPTMAP)
      return "ConceptMap";
    if (code == ResourceTypes.CONDITION)
      return "Condition";
    if (code == ResourceTypes.CONSENT)
      return "Consent";
    if (code == ResourceTypes.CONTRACT)
      return "Contract";
    if (code == ResourceTypes.COVERAGE)
      return "Coverage";
    if (code == ResourceTypes.DATAELEMENT)
      return "DataElement";
    if (code == ResourceTypes.DETECTEDISSUE)
      return "DetectedIssue";
    if (code == ResourceTypes.DEVICE)
      return "Device";
    if (code == ResourceTypes.DEVICECOMPONENT)
      return "DeviceComponent";
    if (code == ResourceTypes.DEVICEMETRIC)
      return "DeviceMetric";
    if (code == ResourceTypes.DEVICEREQUEST)
      return "DeviceRequest";
    if (code == ResourceTypes.DEVICEUSESTATEMENT)
      return "DeviceUseStatement";
    if (code == ResourceTypes.DIAGNOSTICREPORT)
      return "DiagnosticReport";
    if (code == ResourceTypes.DOCUMENTMANIFEST)
      return "DocumentManifest";
    if (code == ResourceTypes.DOCUMENTREFERENCE)
      return "DocumentReference";
    if (code == ResourceTypes.DOMAINRESOURCE)
      return "DomainResource";
    if (code == ResourceTypes.ELIGIBILITYREQUEST)
      return "EligibilityRequest";
    if (code == ResourceTypes.ELIGIBILITYRESPONSE)
      return "EligibilityResponse";
    if (code == ResourceTypes.ENCOUNTER)
      return "Encounter";
    if (code == ResourceTypes.ENDPOINT)
      return "Endpoint";
    if (code == ResourceTypes.ENROLLMENTREQUEST)
      return "EnrollmentRequest";
    if (code == ResourceTypes.ENROLLMENTRESPONSE)
      return "EnrollmentResponse";
    if (code == ResourceTypes.EPISODEOFCARE)
      return "EpisodeOfCare";
    if (code == ResourceTypes.EXPANSIONPROFILE)
      return "ExpansionProfile";
    if (code == ResourceTypes.EXPLANATIONOFBENEFIT)
      return "ExplanationOfBenefit";
    if (code == ResourceTypes.FAMILYMEMBERHISTORY)
      return "FamilyMemberHistory";
    if (code == ResourceTypes.FLAG)
      return "Flag";
    if (code == ResourceTypes.GOAL)
      return "Goal";
    if (code == ResourceTypes.GRAPHDEFINITION)
      return "GraphDefinition";
    if (code == ResourceTypes.GROUP)
      return "Group";
    if (code == ResourceTypes.GUIDANCERESPONSE)
      return "GuidanceResponse";
    if (code == ResourceTypes.HEALTHCARESERVICE)
      return "HealthcareService";
    if (code == ResourceTypes.IMAGINGMANIFEST)
      return "ImagingManifest";
    if (code == ResourceTypes.IMAGINGSTUDY)
      return "ImagingStudy";
    if (code == ResourceTypes.IMMUNIZATION)
      return "Immunization";
    if (code == ResourceTypes.IMMUNIZATIONRECOMMENDATION)
      return "ImmunizationRecommendation";
    if (code == ResourceTypes.IMPLEMENTATIONGUIDE)
      return "ImplementationGuide";
    if (code == ResourceTypes.LIBRARY)
      return "Library";
    if (code == ResourceTypes.LINKAGE)
      return "Linkage";
    if (code == ResourceTypes.LIST)
      return "List";
    if (code == ResourceTypes.LOCATION)
      return "Location";
    if (code == ResourceTypes.MEASURE)
      return "Measure";
    if (code == ResourceTypes.MEASUREREPORT)
      return "MeasureReport";
    if (code == ResourceTypes.MEDIA)
      return "Media";
    if (code == ResourceTypes.MEDICATION)
      return "Medication";
    if (code == ResourceTypes.MEDICATIONADMINISTRATION)
      return "MedicationAdministration";
    if (code == ResourceTypes.MEDICATIONDISPENSE)
      return "MedicationDispense";
    if (code == ResourceTypes.MEDICATIONREQUEST)
      return "MedicationRequest";
    if (code == ResourceTypes.MEDICATIONSTATEMENT)
      return "MedicationStatement";
    if (code == ResourceTypes.MESSAGEDEFINITION)
      return "MessageDefinition";
    if (code == ResourceTypes.MESSAGEHEADER)
      return "MessageHeader";
    if (code == ResourceTypes.NAMINGSYSTEM)
      return "NamingSystem";
    if (code == ResourceTypes.NUTRITIONORDER)
      return "NutritionOrder";
    if (code == ResourceTypes.OBSERVATION)
      return "Observation";
    if (code == ResourceTypes.OPERATIONDEFINITION)
      return "OperationDefinition";
    if (code == ResourceTypes.OPERATIONOUTCOME)
      return "OperationOutcome";
    if (code == ResourceTypes.ORGANIZATION)
      return "Organization";
    if (code == ResourceTypes.PARAMETERS)
      return "Parameters";
    if (code == ResourceTypes.PATIENT)
      return "Patient";
    if (code == ResourceTypes.PAYMENTNOTICE)
      return "PaymentNotice";
    if (code == ResourceTypes.PAYMENTRECONCILIATION)
      return "PaymentReconciliation";
    if (code == ResourceTypes.PERSON)
      return "Person";
    if (code == ResourceTypes.PLANDEFINITION)
      return "PlanDefinition";
    if (code == ResourceTypes.PRACTITIONER)
      return "Practitioner";
    if (code == ResourceTypes.PRACTITIONERROLE)
      return "PractitionerRole";
    if (code == ResourceTypes.PROCEDURE)
      return "Procedure";
    if (code == ResourceTypes.PROCEDUREREQUEST)
      return "ProcedureRequest";
    if (code == ResourceTypes.PROCESSREQUEST)
      return "ProcessRequest";
    if (code == ResourceTypes.PROCESSRESPONSE)
      return "ProcessResponse";
    if (code == ResourceTypes.PROVENANCE)
      return "Provenance";
    if (code == ResourceTypes.QUESTIONNAIRE)
      return "Questionnaire";
    if (code == ResourceTypes.QUESTIONNAIRERESPONSE)
      return "QuestionnaireResponse";
    if (code == ResourceTypes.REFERRALREQUEST)
      return "ReferralRequest";
    if (code == ResourceTypes.RELATEDPERSON)
      return "RelatedPerson";
    if (code == ResourceTypes.REQUESTGROUP)
      return "RequestGroup";
    if (code == ResourceTypes.RESEARCHSTUDY)
      return "ResearchStudy";
    if (code == ResourceTypes.RESEARCHSUBJECT)
      return "ResearchSubject";
    if (code == ResourceTypes.RESOURCE)
      return "Resource";
    if (code == ResourceTypes.RISKASSESSMENT)
      return "RiskAssessment";
    if (code == ResourceTypes.SCHEDULE)
      return "Schedule";
    if (code == ResourceTypes.SEARCHPARAMETER)
      return "SearchParameter";
    if (code == ResourceTypes.SEQUENCE)
      return "Sequence";
    if (code == ResourceTypes.SERVICEDEFINITION)
      return "ServiceDefinition";
    if (code == ResourceTypes.SLOT)
      return "Slot";
    if (code == ResourceTypes.SPECIMEN)
      return "Specimen";
    if (code == ResourceTypes.STRUCTUREDEFINITION)
      return "StructureDefinition";
    if (code == ResourceTypes.STRUCTUREMAP)
      return "StructureMap";
    if (code == ResourceTypes.SUBSCRIPTION)
      return "Subscription";
    if (code == ResourceTypes.SUBSTANCE)
      return "Substance";
    if (code == ResourceTypes.SUPPLYDELIVERY)
      return "SupplyDelivery";
    if (code == ResourceTypes.SUPPLYREQUEST)
      return "SupplyRequest";
    if (code == ResourceTypes.TASK)
      return "Task";
    if (code == ResourceTypes.TESTREPORT)
      return "TestReport";
    if (code == ResourceTypes.TESTSCRIPT)
      return "TestScript";
    if (code == ResourceTypes.VALUESET)
      return "ValueSet";
    if (code == ResourceTypes.VISIONPRESCRIPTION)
      return "VisionPrescription";
    return "?";
  }

    public String toSystem(ResourceTypes code) {
      return code.getSystem();
      }

}

