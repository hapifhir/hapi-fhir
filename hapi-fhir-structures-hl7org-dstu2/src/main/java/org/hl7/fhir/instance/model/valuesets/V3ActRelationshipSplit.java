package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum V3ActRelationshipSplit {

        /**
         * The pre-condition associated with the branch is evaluated once and if true the branch may be entered. All other exclusive branches compete with each other and only one will be selected. This implements a COND, IF and CASE conditionals, or "XOR-split." The order in which the branches are considered may be specified in the priorityNumber attribute.
         */
        E1, 
        /**
         * A branch is selected as soon as the pre-condition associated with the branch evaluates to true.  If the condition is false, the branch may be entered later, when the condition turns true.  All other exclusive branches compete with each other and only one will be selected.  Each waiting branch executes in parallel with the default join code wait  (see below). The order in which the branches are considered may be specified in the Service_relationship.priority_nmb.
         */
        EW, 
        /**
         * A branch is executed if its associated preconditions permit. If associated preconditions do not permit, the branch is dropped.  Inclusive branches are not suppressed and do not suppress other branches.
         */
        I1, 
        /**
         * A branch is executed as soon as its associated conditions permit.  If the condition is false, the branch may be entered later, when the condition turns true.  Inclusive branches are not suppressed and do not suppress other branches.  Each waiting branch executes in parallel with the default join code wait  (see below).
         */
        IW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipSplit fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E1".equals(codeString))
          return E1;
        if ("EW".equals(codeString))
          return EW;
        if ("I1".equals(codeString))
          return I1;
        if ("IW".equals(codeString))
          return IW;
        throw new Exception("Unknown V3ActRelationshipSplit code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E1: return "E1";
            case EW: return "EW";
            case I1: return "I1";
            case IW: return "IW";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipSplit";
        }
        public String getDefinition() {
          switch (this) {
            case E1: return "The pre-condition associated with the branch is evaluated once and if true the branch may be entered. All other exclusive branches compete with each other and only one will be selected. This implements a COND, IF and CASE conditionals, or \"XOR-split.\" The order in which the branches are considered may be specified in the priorityNumber attribute.";
            case EW: return "A branch is selected as soon as the pre-condition associated with the branch evaluates to true.  If the condition is false, the branch may be entered later, when the condition turns true.  All other exclusive branches compete with each other and only one will be selected.  Each waiting branch executes in parallel with the default join code wait  (see below). The order in which the branches are considered may be specified in the Service_relationship.priority_nmb.";
            case I1: return "A branch is executed if its associated preconditions permit. If associated preconditions do not permit, the branch is dropped.  Inclusive branches are not suppressed and do not suppress other branches.";
            case IW: return "A branch is executed as soon as its associated conditions permit.  If the condition is false, the branch may be entered later, when the condition turns true.  Inclusive branches are not suppressed and do not suppress other branches.  Each waiting branch executes in parallel with the default join code wait  (see below).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E1: return "exclusive try once";
            case EW: return "exclusive wait";
            case I1: return "inclusive try once";
            case IW: return "inclusive wait";
            default: return "?";
          }
    }


}

