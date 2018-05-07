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

public enum MessageReasonEncounter {

        /**
         * The patient has been admitted.
         */
        ADMIT, 
        /**
         * The patient has been discharged.
         */
        DISCHARGE, 
        /**
         * The patient has temporarily left the institution.
         */
        ABSENT, 
        /**
         * The patient has returned from a temporary absence.
         */
        RETURN, 
        /**
         * The patient has been moved to a new location.
         */
        MOVED, 
        /**
         * Encounter details have been updated (e.g. to correct a coding error).
         */
        EDIT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MessageReasonEncounter fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("admit".equals(codeString))
          return ADMIT;
        if ("discharge".equals(codeString))
          return DISCHARGE;
        if ("absent".equals(codeString))
          return ABSENT;
        if ("return".equals(codeString))
          return RETURN;
        if ("moved".equals(codeString))
          return MOVED;
        if ("edit".equals(codeString))
          return EDIT;
        throw new FHIRException("Unknown MessageReasonEncounter code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADMIT: return "admit";
            case DISCHARGE: return "discharge";
            case ABSENT: return "absent";
            case RETURN: return "return";
            case MOVED: return "moved";
            case EDIT: return "edit";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/message-reasons-encounter";
        }
        public String getDefinition() {
          switch (this) {
            case ADMIT: return "The patient has been admitted.";
            case DISCHARGE: return "The patient has been discharged.";
            case ABSENT: return "The patient has temporarily left the institution.";
            case RETURN: return "The patient has returned from a temporary absence.";
            case MOVED: return "The patient has been moved to a new location.";
            case EDIT: return "Encounter details have been updated (e.g. to correct a coding error).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADMIT: return "Admit";
            case DISCHARGE: return "Discharge";
            case ABSENT: return "Absent";
            case RETURN: return "Returned";
            case MOVED: return "Moved";
            case EDIT: return "Edit";
            default: return "?";
          }
    }


}

