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

public enum EncounterLocationStatus {

        /**
         * The patient is planned to be moved to this location at some point in the future.
         */
        PLANNED, 
        /**
         * The patient is currently at this location, or was between the period specified.A system may update these records when the patient leaves the location to either reserved, or completed
         */
        ACTIVE, 
        /**
         * This location is held empty for this patient.
         */
        RESERVED, 
        /**
         * The patient was at this location during the period specified.Not to be used when the patient is currently at the location
         */
        COMPLETED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterLocationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("reserved".equals(codeString))
          return RESERVED;
        if ("completed".equals(codeString))
          return COMPLETED;
        throw new FHIRException("Unknown EncounterLocationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case ACTIVE: return "active";
            case RESERVED: return "reserved";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-location-status";
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The patient is planned to be moved to this location at some point in the future.";
            case ACTIVE: return "The patient is currently at this location, or was between the period specified.\r\rA system may update these records when the patient leaves the location to either reserved, or completed";
            case RESERVED: return "This location is held empty for this patient.";
            case COMPLETED: return "The patient was at this location during the period specified.\r\rNot to be used when the patient is currently at the location";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case ACTIVE: return "Active";
            case RESERVED: return "Reserved";
            case COMPLETED: return "Completed";
            default: return "?";
          }
    }


}

