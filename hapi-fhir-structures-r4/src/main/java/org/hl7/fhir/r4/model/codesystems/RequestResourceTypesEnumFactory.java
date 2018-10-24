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

public class RequestResourceTypesEnumFactory implements EnumFactory<RequestResourceTypes> {

  public RequestResourceTypes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Appointment".equals(codeString))
      return RequestResourceTypes.APPOINTMENT;
    if ("AppointmentResponse".equals(codeString))
      return RequestResourceTypes.APPOINTMENTRESPONSE;
    if ("CarePlan".equals(codeString))
      return RequestResourceTypes.CAREPLAN;
    if ("Claim".equals(codeString))
      return RequestResourceTypes.CLAIM;
    if ("CommunicationRequest".equals(codeString))
      return RequestResourceTypes.COMMUNICATIONREQUEST;
    if ("Contract".equals(codeString))
      return RequestResourceTypes.CONTRACT;
    if ("DeviceRequest".equals(codeString))
      return RequestResourceTypes.DEVICEREQUEST;
    if ("EnrollmentRequest".equals(codeString))
      return RequestResourceTypes.ENROLLMENTREQUEST;
    if ("ImmunizationRecommendation".equals(codeString))
      return RequestResourceTypes.IMMUNIZATIONRECOMMENDATION;
    if ("MedicationRequest".equals(codeString))
      return RequestResourceTypes.MEDICATIONREQUEST;
    if ("NutritionOrder".equals(codeString))
      return RequestResourceTypes.NUTRITIONORDER;
    if ("ServiceRequest".equals(codeString))
      return RequestResourceTypes.SERVICEREQUEST;
    if ("SupplyRequest".equals(codeString))
      return RequestResourceTypes.SUPPLYREQUEST;
    if ("Task".equals(codeString))
      return RequestResourceTypes.TASK;
    if ("VisionPrescription".equals(codeString))
      return RequestResourceTypes.VISIONPRESCRIPTION;
    throw new IllegalArgumentException("Unknown RequestResourceTypes code '"+codeString+"'");
  }

  public String toCode(RequestResourceTypes code) {
    if (code == RequestResourceTypes.APPOINTMENT)
      return "Appointment";
    if (code == RequestResourceTypes.APPOINTMENTRESPONSE)
      return "AppointmentResponse";
    if (code == RequestResourceTypes.CAREPLAN)
      return "CarePlan";
    if (code == RequestResourceTypes.CLAIM)
      return "Claim";
    if (code == RequestResourceTypes.COMMUNICATIONREQUEST)
      return "CommunicationRequest";
    if (code == RequestResourceTypes.CONTRACT)
      return "Contract";
    if (code == RequestResourceTypes.DEVICEREQUEST)
      return "DeviceRequest";
    if (code == RequestResourceTypes.ENROLLMENTREQUEST)
      return "EnrollmentRequest";
    if (code == RequestResourceTypes.IMMUNIZATIONRECOMMENDATION)
      return "ImmunizationRecommendation";
    if (code == RequestResourceTypes.MEDICATIONREQUEST)
      return "MedicationRequest";
    if (code == RequestResourceTypes.NUTRITIONORDER)
      return "NutritionOrder";
    if (code == RequestResourceTypes.SERVICEREQUEST)
      return "ServiceRequest";
    if (code == RequestResourceTypes.SUPPLYREQUEST)
      return "SupplyRequest";
    if (code == RequestResourceTypes.TASK)
      return "Task";
    if (code == RequestResourceTypes.VISIONPRESCRIPTION)
      return "VisionPrescription";
    return "?";
  }

    public String toSystem(RequestResourceTypes code) {
      return code.getSystem();
      }

}

