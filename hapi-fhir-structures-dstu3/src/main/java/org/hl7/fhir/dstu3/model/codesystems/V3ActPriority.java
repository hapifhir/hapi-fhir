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

public enum V3ActPriority {

        /**
         * As soon as possible, next highest priority after stat.
         */
        A, 
        /**
         * Filler should contact the placer as soon as results are available, even for preliminary results.  (Was "C" in HL7 version 2.3's reporting priority.)
         */
        CR, 
        /**
         * Filler should contact the placer (or target) to schedule the service.  (Was "C" in HL7 version 2.3's TQ-priority component.)
         */
        CS, 
        /**
         * Filler should contact the placer to schedule the service.  (Was "C" in HL7 version 2.3's TQ-priority component.)
         */
        CSP, 
        /**
         * Filler should contact the service recipient (target) to schedule the service.  (Was "C" in HL7 version 2.3's TQ-priority component.)
         */
        CSR, 
        /**
         * Beneficial to the patient but not essential for survival.
         */
        EL, 
        /**
         * An unforeseen combination of circumstances or the resulting state that calls for immediate action.
         */
        EM, 
        /**
         * Used to indicate that a service is to be performed prior to a scheduled surgery.  When ordering a service and using the pre-op priority, a check is done to see the amount of time that must be allowed for performance of the service.  When the order is placed, a message can be generated indicating the time needed for the service so that it is not ordered in conflict with a scheduled operation.
         */
        P, 
        /**
         * An "as needed" order should be accompanied by a description of what constitutes a need. This description is represented by an observation service predicate as a precondition.
         */
        PRN, 
        /**
         * Routine service, do at usual work hours.
         */
        R, 
        /**
         * A report should be prepared and sent as quickly as possible.
         */
        RR, 
        /**
         * With highest priority (e.g., emergency).
         */
        S, 
        /**
         * It is critical to come as close as possible to the requested time (e.g., for a through antimicrobial level).
         */
        T, 
        /**
         * Drug is to be used as directed by the prescriber.
         */
        UD, 
        /**
         * Calls for prompt action.
         */
        UR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("CR".equals(codeString))
          return CR;
        if ("CS".equals(codeString))
          return CS;
        if ("CSP".equals(codeString))
          return CSP;
        if ("CSR".equals(codeString))
          return CSR;
        if ("EL".equals(codeString))
          return EL;
        if ("EM".equals(codeString))
          return EM;
        if ("P".equals(codeString))
          return P;
        if ("PRN".equals(codeString))
          return PRN;
        if ("R".equals(codeString))
          return R;
        if ("RR".equals(codeString))
          return RR;
        if ("S".equals(codeString))
          return S;
        if ("T".equals(codeString))
          return T;
        if ("UD".equals(codeString))
          return UD;
        if ("UR".equals(codeString))
          return UR;
        throw new FHIRException("Unknown V3ActPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case CR: return "CR";
            case CS: return "CS";
            case CSP: return "CSP";
            case CSR: return "CSR";
            case EL: return "EL";
            case EM: return "EM";
            case P: return "P";
            case PRN: return "PRN";
            case R: return "R";
            case RR: return "RR";
            case S: return "S";
            case T: return "T";
            case UD: return "UD";
            case UR: return "UR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActPriority";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "As soon as possible, next highest priority after stat.";
            case CR: return "Filler should contact the placer as soon as results are available, even for preliminary results.  (Was \"C\" in HL7 version 2.3's reporting priority.)";
            case CS: return "Filler should contact the placer (or target) to schedule the service.  (Was \"C\" in HL7 version 2.3's TQ-priority component.)";
            case CSP: return "Filler should contact the placer to schedule the service.  (Was \"C\" in HL7 version 2.3's TQ-priority component.)";
            case CSR: return "Filler should contact the service recipient (target) to schedule the service.  (Was \"C\" in HL7 version 2.3's TQ-priority component.)";
            case EL: return "Beneficial to the patient but not essential for survival.";
            case EM: return "An unforeseen combination of circumstances or the resulting state that calls for immediate action.";
            case P: return "Used to indicate that a service is to be performed prior to a scheduled surgery.  When ordering a service and using the pre-op priority, a check is done to see the amount of time that must be allowed for performance of the service.  When the order is placed, a message can be generated indicating the time needed for the service so that it is not ordered in conflict with a scheduled operation.";
            case PRN: return "An \"as needed\" order should be accompanied by a description of what constitutes a need. This description is represented by an observation service predicate as a precondition.";
            case R: return "Routine service, do at usual work hours.";
            case RR: return "A report should be prepared and sent as quickly as possible.";
            case S: return "With highest priority (e.g., emergency).";
            case T: return "It is critical to come as close as possible to the requested time (e.g., for a through antimicrobial level).";
            case UD: return "Drug is to be used as directed by the prescriber.";
            case UR: return "Calls for prompt action.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "ASAP";
            case CR: return "callback results";
            case CS: return "callback for scheduling";
            case CSP: return "callback placer for scheduling";
            case CSR: return "contact recipient for scheduling";
            case EL: return "elective";
            case EM: return "emergency";
            case P: return "preop";
            case PRN: return "as needed";
            case R: return "routine";
            case RR: return "rush reporting";
            case S: return "stat";
            case T: return "timing critical";
            case UD: return "use as directed";
            case UR: return "urgent";
            default: return "?";
          }
    }


}

