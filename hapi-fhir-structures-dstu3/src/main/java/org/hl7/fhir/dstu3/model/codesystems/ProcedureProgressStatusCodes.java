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

public enum ProcedureProgressStatusCodes {

        /**
         * A patient is in the Operating Room.
         */
        INOPERATINGROOM, 
        /**
         * The patient is prepared for a procedure.
         */
        PREPARED, 
        /**
         * The patient is under anesthesia.
         */
        ANESTHESIAINDUCED, 
        /**
         * The patient has open incision(s).
         */
        OPENINCISION, 
        /**
         * The patient has incision(s) closed.
         */
        CLOSEDINCISION, 
        /**
         * The patient is in the recovery room.
         */
        INRECOVERYROOM, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureProgressStatusCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-operating-room".equals(codeString))
          return INOPERATINGROOM;
        if ("prepared".equals(codeString))
          return PREPARED;
        if ("anesthesia-induced".equals(codeString))
          return ANESTHESIAINDUCED;
        if ("open-incision".equals(codeString))
          return OPENINCISION;
        if ("closed-incision".equals(codeString))
          return CLOSEDINCISION;
        if ("in-recovery-room".equals(codeString))
          return INRECOVERYROOM;
        throw new FHIRException("Unknown ProcedureProgressStatusCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INOPERATINGROOM: return "in-operating-room";
            case PREPARED: return "prepared";
            case ANESTHESIAINDUCED: return "anesthesia-induced";
            case OPENINCISION: return "open-incision";
            case CLOSEDINCISION: return "closed-incision";
            case INRECOVERYROOM: return "in-recovery-room";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/procedure-progress-status-code";
        }
        public String getDefinition() {
          switch (this) {
            case INOPERATINGROOM: return "A patient is in the Operating Room.";
            case PREPARED: return "The patient is prepared for a procedure.";
            case ANESTHESIAINDUCED: return "The patient is under anesthesia.";
            case OPENINCISION: return "The patient has open incision(s).";
            case CLOSEDINCISION: return "The patient has incision(s) closed.";
            case INRECOVERYROOM: return "The patient is in the recovery room.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INOPERATINGROOM: return "In Operating Room";
            case PREPARED: return "Prepared";
            case ANESTHESIAINDUCED: return "Anesthesia Induced";
            case OPENINCISION: return "Open Incision";
            case CLOSEDINCISION: return "Closed Incision";
            case INRECOVERYROOM: return "In Recovery Room";
            default: return "?";
          }
    }


}

