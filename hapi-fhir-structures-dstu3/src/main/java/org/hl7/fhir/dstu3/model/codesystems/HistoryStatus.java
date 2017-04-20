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

public enum HistoryStatus {

        /**
         * Some health information is known and captured, but not complete - see notes for details.
         */
        PARTIAL, 
        /**
         * All available related health information is captured as of the date (and possibly time) when the family member history was taken.
         */
        COMPLETED, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * Health information for this individual is unavailable/unknown.
         */
        HEALTHUNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HistoryStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("partial".equals(codeString))
          return PARTIAL;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("health-unknown".equals(codeString))
          return HEALTHUNKNOWN;
        throw new FHIRException("Unknown HistoryStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PARTIAL: return "partial";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case HEALTHUNKNOWN: return "health-unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/history-status";
        }
        public String getDefinition() {
          switch (this) {
            case PARTIAL: return "Some health information is known and captured, but not complete - see notes for details.";
            case COMPLETED: return "All available related health information is captured as of the date (and possibly time) when the family member history was taken.";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            case HEALTHUNKNOWN: return "Health information for this individual is unavailable/unknown.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PARTIAL: return "Partial";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in error";
            case HEALTHUNKNOWN: return "Health unknown";
            default: return "?";
          }
    }


}

