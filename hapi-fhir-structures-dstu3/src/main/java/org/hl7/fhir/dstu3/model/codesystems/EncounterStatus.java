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

public enum EncounterStatus {

        /**
         * The Encounter has not yet started.
         */
        PLANNED, 
        /**
         * The Patient is present for the encounter, however is not currently meeting with a practitioner.
         */
        ARRIVED, 
        /**
         * The patient has been assessed for the priority of their treatment based on the severity of their condition.
         */
        TRIAGED, 
        /**
         * The Encounter has begun and the patient is present / the practitioner and the patient are meeting.
         */
        INPROGRESS, 
        /**
         * The Encounter has begun, but the patient is temporarily on leave.
         */
        ONLEAVE, 
        /**
         * The Encounter has ended.
         */
        FINISHED, 
        /**
         * The Encounter has ended before it has begun.
         */
        CANCELLED, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * The encounter status is unknown. Note that "unknown" is a value of last resort and every attempt should be made to provide a meaningful value other than "unknown".
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("arrived".equals(codeString))
          return ARRIVED;
        if ("triaged".equals(codeString))
          return TRIAGED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("onleave".equals(codeString))
          return ONLEAVE;
        if ("finished".equals(codeString))
          return FINISHED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown EncounterStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case ARRIVED: return "arrived";
            case TRIAGED: return "triaged";
            case INPROGRESS: return "in-progress";
            case ONLEAVE: return "onleave";
            case FINISHED: return "finished";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-status";
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The Encounter has not yet started.";
            case ARRIVED: return "The Patient is present for the encounter, however is not currently meeting with a practitioner.";
            case TRIAGED: return "The patient has been assessed for the priority of their treatment based on the severity of their condition.";
            case INPROGRESS: return "The Encounter has begun and the patient is present / the practitioner and the patient are meeting.";
            case ONLEAVE: return "The Encounter has begun, but the patient is temporarily on leave.";
            case FINISHED: return "The Encounter has ended.";
            case CANCELLED: return "The Encounter has ended before it has begun.";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            case UNKNOWN: return "The encounter status is unknown. Note that \"unknown\" is a value of last resort and every attempt should be made to provide a meaningful value other than \"unknown\".";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case ARRIVED: return "Arrived";
            case TRIAGED: return "Triaged";
            case INPROGRESS: return "In Progress";
            case ONLEAVE: return "On Leave";
            case FINISHED: return "Finished";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

