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

public enum RequestResourceTypes {

        /**
         * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT, 
        /**
         * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENTRESPONSE, 
        /**
         * Healthcare plan for patient or group.
         */
        CAREPLAN, 
        /**
         * Claim, Pre-determination or Pre-authorization.
         */
        CLAIM, 
        /**
         * A request for information to be sent to a receiver.
         */
        COMMUNICATIONREQUEST, 
        /**
         * Legal Agreement.
         */
        CONTRACT, 
        /**
         * Medical device request.
         */
        DEVICEREQUEST, 
        /**
         * Enrollment request.
         */
        ENROLLMENTREQUEST, 
        /**
         * Guidance or advice relating to an immunization.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * Ordering of medication for patient or group.
         */
        MEDICATIONREQUEST, 
        /**
         * Diet, formula or nutritional supplement request.
         */
        NUTRITIONORDER, 
        /**
         * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICEREQUEST, 
        /**
         * Request for a medication, substance or device.
         */
        SUPPLYREQUEST, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * Prescription for vision correction products for a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RequestResourceTypes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Appointment".equals(codeString))
          return APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return APPOINTMENTRESPONSE;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("DeviceRequest".equals(codeString))
          return DEVICEREQUEST;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("ServiceRequest".equals(codeString))
          return SERVICEREQUEST;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        throw new FHIRException("Unknown RequestResourceTypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case CONTRACT: return "Contract";
            case DEVICEREQUEST: return "DeviceRequest";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case NUTRITIONORDER: return "NutritionOrder";
            case SERVICEREQUEST: return "ServiceRequest";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/request-resource-types";
        }
        public String getDefinition() {
          switch (this) {
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case CAREPLAN: return "Healthcare plan for patient or group.";
            case CLAIM: return "Claim, Pre-determination or Pre-authorization.";
            case COMMUNICATIONREQUEST: return "A request for information to be sent to a receiver.";
            case CONTRACT: return "Legal Agreement.";
            case DEVICEREQUEST: return "Medical device request.";
            case ENROLLMENTREQUEST: return "Enrollment request.";
            case IMMUNIZATIONRECOMMENDATION: return "Guidance or advice relating to an immunization.";
            case MEDICATIONREQUEST: return "Ordering of medication for patient or group.";
            case NUTRITIONORDER: return "Diet, formula or nutritional supplement request.";
            case SERVICEREQUEST: return "A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.";
            case SUPPLYREQUEST: return "Request for a medication, substance or device.";
            case TASK: return "A task to be performed.";
            case VISIONPRESCRIPTION: return "Prescription for vision correction products for a patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case CONTRACT: return "Contract";
            case DEVICEREQUEST: return "DeviceRequest";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case NUTRITIONORDER: return "NutritionOrder";
            case SERVICEREQUEST: return "ServiceRequest";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
    }


}

