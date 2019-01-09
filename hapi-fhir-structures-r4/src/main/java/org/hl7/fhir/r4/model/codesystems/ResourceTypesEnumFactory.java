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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

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
    if ("BiologicallyDerivedProduct".equals(codeString))
      return ResourceTypes.BIOLOGICALLYDERIVEDPRODUCT;
    if ("BodyStructure".equals(codeString))
      return ResourceTypes.BODYSTRUCTURE;
    if ("Bundle".equals(codeString))
      return ResourceTypes.BUNDLE;
    if ("CapabilityStatement".equals(codeString))
      return ResourceTypes.CAPABILITYSTATEMENT;
    if ("CarePlan".equals(codeString))
      return ResourceTypes.CAREPLAN;
    if ("CareTeam".equals(codeString))
      return ResourceTypes.CARETEAM;
    if ("CatalogEntry".equals(codeString))
      return ResourceTypes.CATALOGENTRY;
    if ("ChargeItem".equals(codeString))
      return ResourceTypes.CHARGEITEM;
    if ("ChargeItemDefinition".equals(codeString))
      return ResourceTypes.CHARGEITEMDEFINITION;
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
    if ("CoverageEligibilityRequest".equals(codeString))
      return ResourceTypes.COVERAGEELIGIBILITYREQUEST;
    if ("CoverageEligibilityResponse".equals(codeString))
      return ResourceTypes.COVERAGEELIGIBILITYRESPONSE;
    if ("DetectedIssue".equals(codeString))
      return ResourceTypes.DETECTEDISSUE;
    if ("Device".equals(codeString))
      return ResourceTypes.DEVICE;
    if ("DeviceDefinition".equals(codeString))
      return ResourceTypes.DEVICEDEFINITION;
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
    if ("EffectEvidenceSynthesis".equals(codeString))
      return ResourceTypes.EFFECTEVIDENCESYNTHESIS;
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
    if ("EventDefinition".equals(codeString))
      return ResourceTypes.EVENTDEFINITION;
    if ("Evidence".equals(codeString))
      return ResourceTypes.EVIDENCE;
    if ("EvidenceVariable".equals(codeString))
      return ResourceTypes.EVIDENCEVARIABLE;
    if ("ExampleScenario".equals(codeString))
      return ResourceTypes.EXAMPLESCENARIO;
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
    if ("ImagingStudy".equals(codeString))
      return ResourceTypes.IMAGINGSTUDY;
    if ("Immunization".equals(codeString))
      return ResourceTypes.IMMUNIZATION;
    if ("ImmunizationEvaluation".equals(codeString))
      return ResourceTypes.IMMUNIZATIONEVALUATION;
    if ("ImmunizationRecommendation".equals(codeString))
      return ResourceTypes.IMMUNIZATIONRECOMMENDATION;
    if ("ImplementationGuide".equals(codeString))
      return ResourceTypes.IMPLEMENTATIONGUIDE;
    if ("InsurancePlan".equals(codeString))
      return ResourceTypes.INSURANCEPLAN;
    if ("Invoice".equals(codeString))
      return ResourceTypes.INVOICE;
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
    if ("MedicationKnowledge".equals(codeString))
      return ResourceTypes.MEDICATIONKNOWLEDGE;
    if ("MedicationRequest".equals(codeString))
      return ResourceTypes.MEDICATIONREQUEST;
    if ("MedicationStatement".equals(codeString))
      return ResourceTypes.MEDICATIONSTATEMENT;
    if ("MedicinalProduct".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCT;
    if ("MedicinalProductAuthorization".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTAUTHORIZATION;
    if ("MedicinalProductContraindication".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTCONTRAINDICATION;
    if ("MedicinalProductIndication".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTINDICATION;
    if ("MedicinalProductIngredient".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTINGREDIENT;
    if ("MedicinalProductInteraction".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTINTERACTION;
    if ("MedicinalProductManufactured".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTMANUFACTURED;
    if ("MedicinalProductPackaged".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTPACKAGED;
    if ("MedicinalProductPharmaceutical".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTPHARMACEUTICAL;
    if ("MedicinalProductUndesirableEffect".equals(codeString))
      return ResourceTypes.MEDICINALPRODUCTUNDESIRABLEEFFECT;
    if ("MessageDefinition".equals(codeString))
      return ResourceTypes.MESSAGEDEFINITION;
    if ("MessageHeader".equals(codeString))
      return ResourceTypes.MESSAGEHEADER;
    if ("MolecularSequence".equals(codeString))
      return ResourceTypes.MOLECULARSEQUENCE;
    if ("NamingSystem".equals(codeString))
      return ResourceTypes.NAMINGSYSTEM;
    if ("NutritionOrder".equals(codeString))
      return ResourceTypes.NUTRITIONORDER;
    if ("Observation".equals(codeString))
      return ResourceTypes.OBSERVATION;
    if ("ObservationDefinition".equals(codeString))
      return ResourceTypes.OBSERVATIONDEFINITION;
    if ("OperationDefinition".equals(codeString))
      return ResourceTypes.OPERATIONDEFINITION;
    if ("OperationOutcome".equals(codeString))
      return ResourceTypes.OPERATIONOUTCOME;
    if ("Organization".equals(codeString))
      return ResourceTypes.ORGANIZATION;
    if ("OrganizationAffiliation".equals(codeString))
      return ResourceTypes.ORGANIZATIONAFFILIATION;
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
    if ("Provenance".equals(codeString))
      return ResourceTypes.PROVENANCE;
    if ("Questionnaire".equals(codeString))
      return ResourceTypes.QUESTIONNAIRE;
    if ("QuestionnaireResponse".equals(codeString))
      return ResourceTypes.QUESTIONNAIRERESPONSE;
    if ("RelatedPerson".equals(codeString))
      return ResourceTypes.RELATEDPERSON;
    if ("RequestGroup".equals(codeString))
      return ResourceTypes.REQUESTGROUP;
    if ("ResearchDefinition".equals(codeString))
      return ResourceTypes.RESEARCHDEFINITION;
    if ("ResearchElementDefinition".equals(codeString))
      return ResourceTypes.RESEARCHELEMENTDEFINITION;
    if ("ResearchStudy".equals(codeString))
      return ResourceTypes.RESEARCHSTUDY;
    if ("ResearchSubject".equals(codeString))
      return ResourceTypes.RESEARCHSUBJECT;
    if ("Resource".equals(codeString))
      return ResourceTypes.RESOURCE;
    if ("RiskAssessment".equals(codeString))
      return ResourceTypes.RISKASSESSMENT;
    if ("RiskEvidenceSynthesis".equals(codeString))
      return ResourceTypes.RISKEVIDENCESYNTHESIS;
    if ("Schedule".equals(codeString))
      return ResourceTypes.SCHEDULE;
    if ("SearchParameter".equals(codeString))
      return ResourceTypes.SEARCHPARAMETER;
    if ("ServiceRequest".equals(codeString))
      return ResourceTypes.SERVICEREQUEST;
    if ("Slot".equals(codeString))
      return ResourceTypes.SLOT;
    if ("Specimen".equals(codeString))
      return ResourceTypes.SPECIMEN;
    if ("SpecimenDefinition".equals(codeString))
      return ResourceTypes.SPECIMENDEFINITION;
    if ("StructureDefinition".equals(codeString))
      return ResourceTypes.STRUCTUREDEFINITION;
    if ("StructureMap".equals(codeString))
      return ResourceTypes.STRUCTUREMAP;
    if ("Subscription".equals(codeString))
      return ResourceTypes.SUBSCRIPTION;
    if ("Substance".equals(codeString))
      return ResourceTypes.SUBSTANCE;
    if ("SubstanceNucleicAcid".equals(codeString))
      return ResourceTypes.SUBSTANCENUCLEICACID;
    if ("SubstancePolymer".equals(codeString))
      return ResourceTypes.SUBSTANCEPOLYMER;
    if ("SubstanceProtein".equals(codeString))
      return ResourceTypes.SUBSTANCEPROTEIN;
    if ("SubstanceReferenceInformation".equals(codeString))
      return ResourceTypes.SUBSTANCEREFERENCEINFORMATION;
    if ("SubstanceSourceMaterial".equals(codeString))
      return ResourceTypes.SUBSTANCESOURCEMATERIAL;
    if ("SubstanceSpecification".equals(codeString))
      return ResourceTypes.SUBSTANCESPECIFICATION;
    if ("SupplyDelivery".equals(codeString))
      return ResourceTypes.SUPPLYDELIVERY;
    if ("SupplyRequest".equals(codeString))
      return ResourceTypes.SUPPLYREQUEST;
    if ("Task".equals(codeString))
      return ResourceTypes.TASK;
    if ("TerminologyCapabilities".equals(codeString))
      return ResourceTypes.TERMINOLOGYCAPABILITIES;
    if ("TestReport".equals(codeString))
      return ResourceTypes.TESTREPORT;
    if ("TestScript".equals(codeString))
      return ResourceTypes.TESTSCRIPT;
    if ("ValueSet".equals(codeString))
      return ResourceTypes.VALUESET;
    if ("VerificationResult".equals(codeString))
      return ResourceTypes.VERIFICATIONRESULT;
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
    if (code == ResourceTypes.BIOLOGICALLYDERIVEDPRODUCT)
      return "BiologicallyDerivedProduct";
    if (code == ResourceTypes.BODYSTRUCTURE)
      return "BodyStructure";
    if (code == ResourceTypes.BUNDLE)
      return "Bundle";
    if (code == ResourceTypes.CAPABILITYSTATEMENT)
      return "CapabilityStatement";
    if (code == ResourceTypes.CAREPLAN)
      return "CarePlan";
    if (code == ResourceTypes.CARETEAM)
      return "CareTeam";
    if (code == ResourceTypes.CATALOGENTRY)
      return "CatalogEntry";
    if (code == ResourceTypes.CHARGEITEM)
      return "ChargeItem";
    if (code == ResourceTypes.CHARGEITEMDEFINITION)
      return "ChargeItemDefinition";
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
    if (code == ResourceTypes.COVERAGEELIGIBILITYREQUEST)
      return "CoverageEligibilityRequest";
    if (code == ResourceTypes.COVERAGEELIGIBILITYRESPONSE)
      return "CoverageEligibilityResponse";
    if (code == ResourceTypes.DETECTEDISSUE)
      return "DetectedIssue";
    if (code == ResourceTypes.DEVICE)
      return "Device";
    if (code == ResourceTypes.DEVICEDEFINITION)
      return "DeviceDefinition";
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
    if (code == ResourceTypes.EFFECTEVIDENCESYNTHESIS)
      return "EffectEvidenceSynthesis";
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
    if (code == ResourceTypes.EVENTDEFINITION)
      return "EventDefinition";
    if (code == ResourceTypes.EVIDENCE)
      return "Evidence";
    if (code == ResourceTypes.EVIDENCEVARIABLE)
      return "EvidenceVariable";
    if (code == ResourceTypes.EXAMPLESCENARIO)
      return "ExampleScenario";
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
    if (code == ResourceTypes.IMAGINGSTUDY)
      return "ImagingStudy";
    if (code == ResourceTypes.IMMUNIZATION)
      return "Immunization";
    if (code == ResourceTypes.IMMUNIZATIONEVALUATION)
      return "ImmunizationEvaluation";
    if (code == ResourceTypes.IMMUNIZATIONRECOMMENDATION)
      return "ImmunizationRecommendation";
    if (code == ResourceTypes.IMPLEMENTATIONGUIDE)
      return "ImplementationGuide";
    if (code == ResourceTypes.INSURANCEPLAN)
      return "InsurancePlan";
    if (code == ResourceTypes.INVOICE)
      return "Invoice";
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
    if (code == ResourceTypes.MEDICATIONKNOWLEDGE)
      return "MedicationKnowledge";
    if (code == ResourceTypes.MEDICATIONREQUEST)
      return "MedicationRequest";
    if (code == ResourceTypes.MEDICATIONSTATEMENT)
      return "MedicationStatement";
    if (code == ResourceTypes.MEDICINALPRODUCT)
      return "MedicinalProduct";
    if (code == ResourceTypes.MEDICINALPRODUCTAUTHORIZATION)
      return "MedicinalProductAuthorization";
    if (code == ResourceTypes.MEDICINALPRODUCTCONTRAINDICATION)
      return "MedicinalProductContraindication";
    if (code == ResourceTypes.MEDICINALPRODUCTINDICATION)
      return "MedicinalProductIndication";
    if (code == ResourceTypes.MEDICINALPRODUCTINGREDIENT)
      return "MedicinalProductIngredient";
    if (code == ResourceTypes.MEDICINALPRODUCTINTERACTION)
      return "MedicinalProductInteraction";
    if (code == ResourceTypes.MEDICINALPRODUCTMANUFACTURED)
      return "MedicinalProductManufactured";
    if (code == ResourceTypes.MEDICINALPRODUCTPACKAGED)
      return "MedicinalProductPackaged";
    if (code == ResourceTypes.MEDICINALPRODUCTPHARMACEUTICAL)
      return "MedicinalProductPharmaceutical";
    if (code == ResourceTypes.MEDICINALPRODUCTUNDESIRABLEEFFECT)
      return "MedicinalProductUndesirableEffect";
    if (code == ResourceTypes.MESSAGEDEFINITION)
      return "MessageDefinition";
    if (code == ResourceTypes.MESSAGEHEADER)
      return "MessageHeader";
    if (code == ResourceTypes.MOLECULARSEQUENCE)
      return "MolecularSequence";
    if (code == ResourceTypes.NAMINGSYSTEM)
      return "NamingSystem";
    if (code == ResourceTypes.NUTRITIONORDER)
      return "NutritionOrder";
    if (code == ResourceTypes.OBSERVATION)
      return "Observation";
    if (code == ResourceTypes.OBSERVATIONDEFINITION)
      return "ObservationDefinition";
    if (code == ResourceTypes.OPERATIONDEFINITION)
      return "OperationDefinition";
    if (code == ResourceTypes.OPERATIONOUTCOME)
      return "OperationOutcome";
    if (code == ResourceTypes.ORGANIZATION)
      return "Organization";
    if (code == ResourceTypes.ORGANIZATIONAFFILIATION)
      return "OrganizationAffiliation";
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
    if (code == ResourceTypes.PROVENANCE)
      return "Provenance";
    if (code == ResourceTypes.QUESTIONNAIRE)
      return "Questionnaire";
    if (code == ResourceTypes.QUESTIONNAIRERESPONSE)
      return "QuestionnaireResponse";
    if (code == ResourceTypes.RELATEDPERSON)
      return "RelatedPerson";
    if (code == ResourceTypes.REQUESTGROUP)
      return "RequestGroup";
    if (code == ResourceTypes.RESEARCHDEFINITION)
      return "ResearchDefinition";
    if (code == ResourceTypes.RESEARCHELEMENTDEFINITION)
      return "ResearchElementDefinition";
    if (code == ResourceTypes.RESEARCHSTUDY)
      return "ResearchStudy";
    if (code == ResourceTypes.RESEARCHSUBJECT)
      return "ResearchSubject";
    if (code == ResourceTypes.RESOURCE)
      return "Resource";
    if (code == ResourceTypes.RISKASSESSMENT)
      return "RiskAssessment";
    if (code == ResourceTypes.RISKEVIDENCESYNTHESIS)
      return "RiskEvidenceSynthesis";
    if (code == ResourceTypes.SCHEDULE)
      return "Schedule";
    if (code == ResourceTypes.SEARCHPARAMETER)
      return "SearchParameter";
    if (code == ResourceTypes.SERVICEREQUEST)
      return "ServiceRequest";
    if (code == ResourceTypes.SLOT)
      return "Slot";
    if (code == ResourceTypes.SPECIMEN)
      return "Specimen";
    if (code == ResourceTypes.SPECIMENDEFINITION)
      return "SpecimenDefinition";
    if (code == ResourceTypes.STRUCTUREDEFINITION)
      return "StructureDefinition";
    if (code == ResourceTypes.STRUCTUREMAP)
      return "StructureMap";
    if (code == ResourceTypes.SUBSCRIPTION)
      return "Subscription";
    if (code == ResourceTypes.SUBSTANCE)
      return "Substance";
    if (code == ResourceTypes.SUBSTANCENUCLEICACID)
      return "SubstanceNucleicAcid";
    if (code == ResourceTypes.SUBSTANCEPOLYMER)
      return "SubstancePolymer";
    if (code == ResourceTypes.SUBSTANCEPROTEIN)
      return "SubstanceProtein";
    if (code == ResourceTypes.SUBSTANCEREFERENCEINFORMATION)
      return "SubstanceReferenceInformation";
    if (code == ResourceTypes.SUBSTANCESOURCEMATERIAL)
      return "SubstanceSourceMaterial";
    if (code == ResourceTypes.SUBSTANCESPECIFICATION)
      return "SubstanceSpecification";
    if (code == ResourceTypes.SUPPLYDELIVERY)
      return "SupplyDelivery";
    if (code == ResourceTypes.SUPPLYREQUEST)
      return "SupplyRequest";
    if (code == ResourceTypes.TASK)
      return "Task";
    if (code == ResourceTypes.TERMINOLOGYCAPABILITIES)
      return "TerminologyCapabilities";
    if (code == ResourceTypes.TESTREPORT)
      return "TestReport";
    if (code == ResourceTypes.TESTSCRIPT)
      return "TestScript";
    if (code == ResourceTypes.VALUESET)
      return "ValueSet";
    if (code == ResourceTypes.VERIFICATIONRESULT)
      return "VerificationResult";
    if (code == ResourceTypes.VISIONPRESCRIPTION)
      return "VisionPrescription";
    return "?";
  }

    public String toSystem(ResourceTypes code) {
      return code.getSystem();
      }

}

