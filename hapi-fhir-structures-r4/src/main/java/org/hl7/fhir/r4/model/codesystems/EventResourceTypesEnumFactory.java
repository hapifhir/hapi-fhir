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


import org.hl7.fhir.r4.model.EnumFactory;

public class EventResourceTypesEnumFactory implements EnumFactory<EventResourceTypes> {

  public EventResourceTypes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ChargeItem".equals(codeString))
      return EventResourceTypes.CHARGEITEM;
    if ("ClaimResponse".equals(codeString))
      return EventResourceTypes.CLAIMRESPONSE;
    if ("ClinicalImpression".equals(codeString))
      return EventResourceTypes.CLINICALIMPRESSION;
    if ("Communication".equals(codeString))
      return EventResourceTypes.COMMUNICATION;
    if ("Composition".equals(codeString))
      return EventResourceTypes.COMPOSITION;
    if ("Condition".equals(codeString))
      return EventResourceTypes.CONDITION;
    if ("Consent".equals(codeString))
      return EventResourceTypes.CONSENT;
    if ("Coverage".equals(codeString))
      return EventResourceTypes.COVERAGE;
    if ("DeviceUseStatement".equals(codeString))
      return EventResourceTypes.DEVICEUSESTATEMENT;
    if ("DiagnosticReport".equals(codeString))
      return EventResourceTypes.DIAGNOSTICREPORT;
    if ("DocumentManifest".equals(codeString))
      return EventResourceTypes.DOCUMENTMANIFEST;
    if ("DocumentReference".equals(codeString))
      return EventResourceTypes.DOCUMENTREFERENCE;
    if ("Encounter".equals(codeString))
      return EventResourceTypes.ENCOUNTER;
    if ("EnrollmentResponse".equals(codeString))
      return EventResourceTypes.ENROLLMENTRESPONSE;
    if ("EpisodeOfCare".equals(codeString))
      return EventResourceTypes.EPISODEOFCARE;
    if ("ExplanationOfBenefit".equals(codeString))
      return EventResourceTypes.EXPLANATIONOFBENEFIT;
    if ("FamilyMemberHistory".equals(codeString))
      return EventResourceTypes.FAMILYMEMBERHISTORY;
    if ("GuidanceResponse".equals(codeString))
      return EventResourceTypes.GUIDANCERESPONSE;
    if ("ImagingStudy".equals(codeString))
      return EventResourceTypes.IMAGINGSTUDY;
    if ("Immunization".equals(codeString))
      return EventResourceTypes.IMMUNIZATION;
    if ("MeasureReport".equals(codeString))
      return EventResourceTypes.MEASUREREPORT;
    if ("Media".equals(codeString))
      return EventResourceTypes.MEDIA;
    if ("MedicationAdministration".equals(codeString))
      return EventResourceTypes.MEDICATIONADMINISTRATION;
    if ("MedicationDispense".equals(codeString))
      return EventResourceTypes.MEDICATIONDISPENSE;
    if ("MedicationStatement".equals(codeString))
      return EventResourceTypes.MEDICATIONSTATEMENT;
    if ("Observation".equals(codeString))
      return EventResourceTypes.OBSERVATION;
    if ("PaymentNotice".equals(codeString))
      return EventResourceTypes.PAYMENTNOTICE;
    if ("PaymentReconciliation".equals(codeString))
      return EventResourceTypes.PAYMENTRECONCILIATION;
    if ("Procedure".equals(codeString))
      return EventResourceTypes.PROCEDURE;
    if ("ProcessResponse".equals(codeString))
      return EventResourceTypes.PROCESSRESPONSE;
    if ("QuestionnaireResponse".equals(codeString))
      return EventResourceTypes.QUESTIONNAIRERESPONSE;
    if ("RiskAssessment".equals(codeString))
      return EventResourceTypes.RISKASSESSMENT;
    if ("SupplyDelivery".equals(codeString))
      return EventResourceTypes.SUPPLYDELIVERY;
    if ("Task".equals(codeString))
      return EventResourceTypes.TASK;
    throw new IllegalArgumentException("Unknown EventResourceTypes code '"+codeString+"'");
  }

  public String toCode(EventResourceTypes code) {
    if (code == EventResourceTypes.CHARGEITEM)
      return "ChargeItem";
    if (code == EventResourceTypes.CLAIMRESPONSE)
      return "ClaimResponse";
    if (code == EventResourceTypes.CLINICALIMPRESSION)
      return "ClinicalImpression";
    if (code == EventResourceTypes.COMMUNICATION)
      return "Communication";
    if (code == EventResourceTypes.COMPOSITION)
      return "Composition";
    if (code == EventResourceTypes.CONDITION)
      return "Condition";
    if (code == EventResourceTypes.CONSENT)
      return "Consent";
    if (code == EventResourceTypes.COVERAGE)
      return "Coverage";
    if (code == EventResourceTypes.DEVICEUSESTATEMENT)
      return "DeviceUseStatement";
    if (code == EventResourceTypes.DIAGNOSTICREPORT)
      return "DiagnosticReport";
    if (code == EventResourceTypes.DOCUMENTMANIFEST)
      return "DocumentManifest";
    if (code == EventResourceTypes.DOCUMENTREFERENCE)
      return "DocumentReference";
    if (code == EventResourceTypes.ENCOUNTER)
      return "Encounter";
    if (code == EventResourceTypes.ENROLLMENTRESPONSE)
      return "EnrollmentResponse";
    if (code == EventResourceTypes.EPISODEOFCARE)
      return "EpisodeOfCare";
    if (code == EventResourceTypes.EXPLANATIONOFBENEFIT)
      return "ExplanationOfBenefit";
    if (code == EventResourceTypes.FAMILYMEMBERHISTORY)
      return "FamilyMemberHistory";
    if (code == EventResourceTypes.GUIDANCERESPONSE)
      return "GuidanceResponse";
    if (code == EventResourceTypes.IMAGINGSTUDY)
      return "ImagingStudy";
    if (code == EventResourceTypes.IMMUNIZATION)
      return "Immunization";
    if (code == EventResourceTypes.MEASUREREPORT)
      return "MeasureReport";
    if (code == EventResourceTypes.MEDIA)
      return "Media";
    if (code == EventResourceTypes.MEDICATIONADMINISTRATION)
      return "MedicationAdministration";
    if (code == EventResourceTypes.MEDICATIONDISPENSE)
      return "MedicationDispense";
    if (code == EventResourceTypes.MEDICATIONSTATEMENT)
      return "MedicationStatement";
    if (code == EventResourceTypes.OBSERVATION)
      return "Observation";
    if (code == EventResourceTypes.PAYMENTNOTICE)
      return "PaymentNotice";
    if (code == EventResourceTypes.PAYMENTRECONCILIATION)
      return "PaymentReconciliation";
    if (code == EventResourceTypes.PROCEDURE)
      return "Procedure";
    if (code == EventResourceTypes.PROCESSRESPONSE)
      return "ProcessResponse";
    if (code == EventResourceTypes.QUESTIONNAIRERESPONSE)
      return "QuestionnaireResponse";
    if (code == EventResourceTypes.RISKASSESSMENT)
      return "RiskAssessment";
    if (code == EventResourceTypes.SUPPLYDELIVERY)
      return "SupplyDelivery";
    if (code == EventResourceTypes.TASK)
      return "Task";
    return "?";
  }

    public String toSystem(EventResourceTypes code) {
      return code.getSystem();
      }

}

