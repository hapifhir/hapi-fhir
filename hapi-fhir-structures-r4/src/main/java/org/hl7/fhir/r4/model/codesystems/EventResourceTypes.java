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


import org.hl7.fhir.exceptions.FHIRException;

public enum EventResourceTypes {

        /**
         * Item containing charge code(s) associated with the provision of healthcare provider products.
         */
        CHARGEITEM, 
        /**
         * Remittance resource.
         */
        CLAIMRESPONSE, 
        /**
         * A clinical assessment performed when planning treatments and management strategies for a patient.
         */
        CLINICALIMPRESSION, 
        /**
         * A record of information transmitted from a sender to a receiver.
         */
        COMMUNICATION, 
        /**
         * A set of resources composed into a single coherent clinical statement with clinical attestation.
         */
        COMPOSITION, 
        /**
         * Detailed information about conditions, problems or diagnoses.
         */
        CONDITION, 
        /**
         * A healthcare consumer's policy choices to permits or denies recipients or roles to perform actions for specific purposes and periods of time.
         */
        CONSENT, 
        /**
         * Insurance or medical plan or a payment agreement.
         */
        COVERAGE, 
        /**
         * Record of use of a device.
         */
        DEVICEUSESTATEMENT, 
        /**
         * A Diagnostic report - a combination of request information, atomic results, images, interpretation, as well as formatted reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A list that defines a set of documents.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document.
         */
        DOCUMENTREFERENCE, 
        /**
         * An interaction during which services are provided to the patient.
         */
        ENCOUNTER, 
        /**
         * EnrollmentResponse resource.
         */
        ENROLLMENTRESPONSE, 
        /**
         * An association of a Patient with an Organization and  Healthcare Provider(s) for a period of time that the Organization assumes some level of responsibility.
         */
        EPISODEOFCARE, 
        /**
         * Explanation of Benefit resource.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Information about patient's relatives, relevant for patient.
         */
        FAMILYMEMBERHISTORY, 
        /**
         * The formal response to a guidance request.
         */
        GUIDANCERESPONSE, 
        /**
         * A set of images produced in single study (one or more series of references images).
         */
        IMAGINGSTUDY, 
        /**
         * Immunization event information.
         */
        IMMUNIZATION, 
        /**
         * Results of a measure evaluation.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * Administration of medication to a patient.
         */
        MEDICATIONADMINISTRATION, 
        /**
         * Dispensing a medication to a named patient.
         */
        MEDICATIONDISPENSE, 
        /**
         * Record of medication being taken by a patient.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Measurements and simple assertions.
         */
        OBSERVATION, 
        /**
         * PaymentNotice request.
         */
        PAYMENTNOTICE, 
        /**
         * PaymentReconciliation resource.
         */
        PAYMENTRECONCILIATION, 
        /**
         * An action that is being or was performed on a patient.
         */
        PROCEDURE, 
        /**
         * ProcessResponse resource.
         */
        PROCESSRESPONSE, 
        /**
         * A structured set of questions and their answers.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Potential outcomes for a subject with likelihood.
         */
        RISKASSESSMENT, 
        /**
         * Delivery of bulk Supplies.
         */
        SUPPLYDELIVERY, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EventResourceTypes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Consent".equals(codeString))
          return CONSENT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("GuidanceResponse".equals(codeString))
          return GUIDANCERESPONSE;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("MeasureReport".equals(codeString))
          return MEASUREREPORT;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("PaymentNotice".equals(codeString))
          return PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return PAYMENTRECONCILIATION;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
        if ("ProcessResponse".equals(codeString))
          return PROCESSRESPONSE;
        if ("QuestionnaireResponse".equals(codeString))
          return QUESTIONNAIRERESPONSE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("Task".equals(codeString))
          return TASK;
        throw new FHIRException("Unknown EventResourceTypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CHARGEITEM: return "ChargeItem";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMPOSITION: return "Composition";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case COVERAGE: return "Coverage";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case OBSERVATION: return "Observation";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PROCEDURE: return "Procedure";
            case PROCESSRESPONSE: return "ProcessResponse";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RISKASSESSMENT: return "RiskAssessment";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case TASK: return "Task";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/event-resource-types";
        }
        public String getDefinition() {
          switch (this) {
            case CHARGEITEM: return "Item containing charge code(s) associated with the provision of healthcare provider products.";
            case CLAIMRESPONSE: return "Remittance resource.";
            case CLINICALIMPRESSION: return "A clinical assessment performed when planning treatments and management strategies for a patient.";
            case COMMUNICATION: return "A record of information transmitted from a sender to a receiver.";
            case COMPOSITION: return "A set of resources composed into a single coherent clinical statement with clinical attestation.";
            case CONDITION: return "Detailed information about conditions, problems or diagnoses.";
            case CONSENT: return "A healthcare consumer's policy choices to permits or denies recipients or roles to perform actions for specific purposes and periods of time.";
            case COVERAGE: return "Insurance or medical plan or a payment agreement.";
            case DEVICEUSESTATEMENT: return "Record of use of a device.";
            case DIAGNOSTICREPORT: return "A Diagnostic report - a combination of request information, atomic results, images, interpretation, as well as formatted reports.";
            case DOCUMENTMANIFEST: return "A list that defines a set of documents.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case ENCOUNTER: return "An interaction during which services are provided to the patient.";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse resource.";
            case EPISODEOFCARE: return "An association of a Patient with an Organization and  Healthcare Provider(s) for a period of time that the Organization assumes some level of responsibility.";
            case EXPLANATIONOFBENEFIT: return "Explanation of Benefit resource.";
            case FAMILYMEMBERHISTORY: return "Information about patient's relatives, relevant for patient.";
            case GUIDANCERESPONSE: return "The formal response to a guidance request.";
            case IMAGINGSTUDY: return "A set of images produced in single study (one or more series of references images).";
            case IMMUNIZATION: return "Immunization event information.";
            case MEASUREREPORT: return "Results of a measure evaluation.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATIONADMINISTRATION: return "Administration of medication to a patient.";
            case MEDICATIONDISPENSE: return "Dispensing a medication to a named patient.";
            case MEDICATIONSTATEMENT: return "Record of medication being taken by a patient.";
            case OBSERVATION: return "Measurements and simple assertions.";
            case PAYMENTNOTICE: return "PaymentNotice request.";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation resource.";
            case PROCEDURE: return "An action that is being or was performed on a patient.";
            case PROCESSRESPONSE: return "ProcessResponse resource.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers.";
            case RISKASSESSMENT: return "Potential outcomes for a subject with likelihood.";
            case SUPPLYDELIVERY: return "Delivery of bulk Supplies.";
            case TASK: return "A task to be performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CHARGEITEM: return "ChargeItem";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMPOSITION: return "Composition";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case COVERAGE: return "Coverage";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case OBSERVATION: return "Observation";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PROCEDURE: return "Procedure";
            case PROCESSRESPONSE: return "ProcessResponse";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RISKASSESSMENT: return "RiskAssessment";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case TASK: return "Task";
            default: return "?";
          }
    }


}

