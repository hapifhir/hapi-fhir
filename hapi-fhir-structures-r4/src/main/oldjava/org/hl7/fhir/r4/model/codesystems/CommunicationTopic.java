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

public enum CommunicationTopic {

        /**
         * The purpose or content of the communication is a prescription refill request.
         */
        PRESCRIPTIONREFILLREQUEST, 
        /**
         * The purpose or content of the communication is a progress update.
         */
        PROGRESSUPDATE, 
        /**
         * The purpose or content of the communication is to report labs.
         */
        REPORTLABS, 
        /**
         * The purpose or content of the communication is an appointment reminder.
         */
        APPOINTMENTREMINDER, 
        /**
         * The purpose or content of the communication is a phone consult.
         */
        PHONECONSULT, 
        /**
         * The purpose or content of the communication is a summary report.
         */
        SUMMARYREPORT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CommunicationTopic fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("prescription-refill-request".equals(codeString))
          return PRESCRIPTIONREFILLREQUEST;
        if ("progress-update".equals(codeString))
          return PROGRESSUPDATE;
        if ("report-labs".equals(codeString))
          return REPORTLABS;
        if ("appointment-reminder".equals(codeString))
          return APPOINTMENTREMINDER;
        if ("phone-consult".equals(codeString))
          return PHONECONSULT;
        if ("summary-report".equals(codeString))
          return SUMMARYREPORT;
        throw new FHIRException("Unknown CommunicationTopic code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRESCRIPTIONREFILLREQUEST: return "prescription-refill-request";
            case PROGRESSUPDATE: return "progress-update";
            case REPORTLABS: return "report-labs";
            case APPOINTMENTREMINDER: return "appointment-reminder";
            case PHONECONSULT: return "phone-consult";
            case SUMMARYREPORT: return "summary-report";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/communication-topic";
        }
        public String getDefinition() {
          switch (this) {
            case PRESCRIPTIONREFILLREQUEST: return "The purpose or content of the communication is a prescription refill request.";
            case PROGRESSUPDATE: return "The purpose or content of the communication is a progress update.";
            case REPORTLABS: return "The purpose or content of the communication is to report labs.";
            case APPOINTMENTREMINDER: return "The purpose or content of the communication is an appointment reminder.";
            case PHONECONSULT: return "The purpose or content of the communication is a phone consult.";
            case SUMMARYREPORT: return "The purpose or content of the communication is a summary report.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRESCRIPTIONREFILLREQUEST: return "Prescription Refill Request";
            case PROGRESSUPDATE: return "Progress Update";
            case REPORTLABS: return "Report Labs";
            case APPOINTMENTREMINDER: return "Appointment Reminder";
            case PHONECONSULT: return "Phone Consult";
            case SUMMARYREPORT: return "Summary Report";
            default: return "?";
          }
    }


}

