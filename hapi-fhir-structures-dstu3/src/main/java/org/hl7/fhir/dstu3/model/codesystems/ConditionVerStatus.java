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

public enum ConditionVerStatus {

        /**
         * This is a tentative diagnosis - still a candidate that is under consideration.
         */
        PROVISIONAL, 
        /**
         * One of a set of potential (and typically mutually exclusive) diagnoses asserted to further guide the diagnostic process and preliminary treatment.
         */
        DIFFERENTIAL, 
        /**
         * There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition.
         */
        CONFIRMED, 
        /**
         * This condition has been ruled out by diagnostic and clinical evidence.
         */
        REFUTED, 
        /**
         * The statement was entered in error and is not valid.
         */
        ENTEREDINERROR, 
        /**
         * The condition status is unknown.  Note that "unknown" is a value of last resort and every attempt should be made to provide a meaningful value other than "unknown".
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionVerStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provisional".equals(codeString))
          return PROVISIONAL;
        if ("differential".equals(codeString))
          return DIFFERENTIAL;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown ConditionVerStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROVISIONAL: return "provisional";
            case DIFFERENTIAL: return "differential";
            case CONFIRMED: return "confirmed";
            case REFUTED: return "refuted";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-ver-status";
        }
        public String getDefinition() {
          switch (this) {
            case PROVISIONAL: return "This is a tentative diagnosis - still a candidate that is under consideration.";
            case DIFFERENTIAL: return "One of a set of potential (and typically mutually exclusive) diagnoses asserted to further guide the diagnostic process and preliminary treatment.";
            case CONFIRMED: return "There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition.";
            case REFUTED: return "This condition has been ruled out by diagnostic and clinical evidence.";
            case ENTEREDINERROR: return "The statement was entered in error and is not valid.";
            case UNKNOWN: return "The condition status is unknown.  Note that \"unknown\" is a value of last resort and every attempt should be made to provide a meaningful value other than \"unknown\".";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROVISIONAL: return "Provisional";
            case DIFFERENTIAL: return "Differential";
            case CONFIRMED: return "Confirmed";
            case REFUTED: return "Refuted";
            case ENTEREDINERROR: return "Entered In Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

