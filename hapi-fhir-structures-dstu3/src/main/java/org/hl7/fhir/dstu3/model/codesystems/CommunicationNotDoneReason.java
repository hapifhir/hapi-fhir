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


import org.hl7.fhir.exceptions.FHIRException;

public enum CommunicationNotDoneReason {

        /**
         * The communication was not done due to an unknown reason.
         */
        UNKNOWN, 
        /**
         * The communication was not done due to a system error.
         */
        SYSTEMERROR, 
        /**
         * The communication was not done due to an invalid phone number.
         */
        INVALIDPHONENUMBER, 
        /**
         * The communication was not done due to the recipient being unavailable.
         */
        RECIPIENTUNAVAILABLE, 
        /**
         * The communication was not done due to a family objection.
         */
        FAMILYOBJECTION, 
        /**
         * The communication was not done due to a patient objection.
         */
        PATIENTOBJECTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CommunicationNotDoneReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("system-error".equals(codeString))
          return SYSTEMERROR;
        if ("invalid-phone-number".equals(codeString))
          return INVALIDPHONENUMBER;
        if ("recipient-unavailable".equals(codeString))
          return RECIPIENTUNAVAILABLE;
        if ("family-objection".equals(codeString))
          return FAMILYOBJECTION;
        if ("patient-objection".equals(codeString))
          return PATIENTOBJECTION;
        throw new FHIRException("Unknown CommunicationNotDoneReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case SYSTEMERROR: return "system-error";
            case INVALIDPHONENUMBER: return "invalid-phone-number";
            case RECIPIENTUNAVAILABLE: return "recipient-unavailable";
            case FAMILYOBJECTION: return "family-objection";
            case PATIENTOBJECTION: return "patient-objection";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/communication-not-done-reason";
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The communication was not done due to an unknown reason.";
            case SYSTEMERROR: return "The communication was not done due to a system error.";
            case INVALIDPHONENUMBER: return "The communication was not done due to an invalid phone number.";
            case RECIPIENTUNAVAILABLE: return "The communication was not done due to the recipient being unavailable.";
            case FAMILYOBJECTION: return "The communication was not done due to a family objection.";
            case PATIENTOBJECTION: return "The communication was not done due to a patient objection.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case SYSTEMERROR: return "System Error";
            case INVALIDPHONENUMBER: return "Invalid Phone Number";
            case RECIPIENTUNAVAILABLE: return "Recipient Unavailable";
            case FAMILYOBJECTION: return "Family Objection";
            case PATIENTOBJECTION: return "Patient Objection";
            default: return "?";
          }
    }


}

