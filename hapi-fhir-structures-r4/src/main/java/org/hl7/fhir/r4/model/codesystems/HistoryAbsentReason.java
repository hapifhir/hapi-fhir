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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum HistoryAbsentReason {

        /**
         * Patient does not know the subject, e.g. the biological parent of an adopted patient.
         */
        SUBJECTUNKNOWN, 
        /**
         * The patient withheld or refused to share the information.
         */
        WITHHELD, 
        /**
         * Information cannot be obtained; e.g. unconscious patient
         */
        UNABLETOOBTAIN, 
        /**
         * Patient does not have the information now, but can provide the information at a later date.
         */
        DEFERRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HistoryAbsentReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("subject-unknown".equals(codeString))
          return SUBJECTUNKNOWN;
        if ("withheld".equals(codeString))
          return WITHHELD;
        if ("unable-to-obtain".equals(codeString))
          return UNABLETOOBTAIN;
        if ("deferred".equals(codeString))
          return DEFERRED;
        throw new FHIRException("Unknown HistoryAbsentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUBJECTUNKNOWN: return "subject-unknown";
            case WITHHELD: return "withheld";
            case UNABLETOOBTAIN: return "unable-to-obtain";
            case DEFERRED: return "deferred";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/history-absent-reason";
        }
        public String getDefinition() {
          switch (this) {
            case SUBJECTUNKNOWN: return "Patient does not know the subject, e.g. the biological parent of an adopted patient.";
            case WITHHELD: return "The patient withheld or refused to share the information.";
            case UNABLETOOBTAIN: return "Information cannot be obtained; e.g. unconscious patient";
            case DEFERRED: return "Patient does not have the information now, but can provide the information at a later date.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUBJECTUNKNOWN: return "Subject Unknown";
            case WITHHELD: return "Information Withheld";
            case UNABLETOOBTAIN: return "Unable To Obtain";
            case DEFERRED: return "Deferred";
            default: return "?";
          }
    }


}

