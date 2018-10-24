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


import org.hl7.fhir.exceptions.FHIRException;

public enum ListEmptyReason {

        /**
         * Clinical judgment that there are no known items for this list after reasonable investigation. Note that this a positive statement by a clinical user, and not a default position asserted by a computer system in the lack of other information. Example uses:  * For allergies: the patient or patient's agent/guardian has asserted that he/she is not aware of any allergies (NKA - nil known allergies)  * For medications: the patient or patient's agent/guardian has asserted that the patient is known to be taking no medications  * For diagnoses, problems and procedures: the patient or patient's agent/guardian has asserted that there is no known event to record.
         */
        NILKNOWN, 
        /**
         * The investigation to find out whether there are items for this list has not occurred.
         */
        NOTASKED, 
        /**
         * The content of the list was not provided due to privacy or confidentiality concerns. Note that it should not be assumed that this means that the particular information in question was withheld due to its contents - it can also be a policy decision.
         */
        WITHHELD, 
        /**
         * Information to populate this list cannot be obtained; e.g. unconscious patient.
         */
        UNAVAILABLE, 
        /**
         * The work to populate this list has not yet begun.
         */
        NOTSTARTED, 
        /**
         * This list has now closed or has ceased to be relevant or useful.
         */
        CLOSED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListEmptyReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("nilknown".equals(codeString))
          return NILKNOWN;
        if ("notasked".equals(codeString))
          return NOTASKED;
        if ("withheld".equals(codeString))
          return WITHHELD;
        if ("unavailable".equals(codeString))
          return UNAVAILABLE;
        if ("notstarted".equals(codeString))
          return NOTSTARTED;
        if ("closed".equals(codeString))
          return CLOSED;
        throw new FHIRException("Unknown ListEmptyReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NILKNOWN: return "nilknown";
            case NOTASKED: return "notasked";
            case WITHHELD: return "withheld";
            case UNAVAILABLE: return "unavailable";
            case NOTSTARTED: return "notstarted";
            case CLOSED: return "closed";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/list-empty-reason";
        }
        public String getDefinition() {
          switch (this) {
            case NILKNOWN: return "Clinical judgment that there are no known items for this list after reasonable investigation. Note that this a positive statement by a clinical user, and not a default position asserted by a computer system in the lack of other information. Example uses:  * For allergies: the patient or patient's agent/guardian has asserted that he/she is not aware of any allergies (NKA - nil known allergies)  * For medications: the patient or patient's agent/guardian has asserted that the patient is known to be taking no medications  * For diagnoses, problems and procedures: the patient or patient's agent/guardian has asserted that there is no known event to record.";
            case NOTASKED: return "The investigation to find out whether there are items for this list has not occurred.";
            case WITHHELD: return "The content of the list was not provided due to privacy or confidentiality concerns. Note that it should not be assumed that this means that the particular information in question was withheld due to its contents - it can also be a policy decision.";
            case UNAVAILABLE: return "Information to populate this list cannot be obtained; e.g. unconscious patient.";
            case NOTSTARTED: return "The work to populate this list has not yet begun.";
            case CLOSED: return "This list has now closed or has ceased to be relevant or useful.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NILKNOWN: return "Nil Known";
            case NOTASKED: return "Not Asked";
            case WITHHELD: return "Information Withheld";
            case UNAVAILABLE: return "Unavailable";
            case NOTSTARTED: return "Not Started";
            case CLOSED: return "Closed";
            default: return "?";
          }
    }


}

