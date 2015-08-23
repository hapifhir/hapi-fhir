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


public enum V3ActRelationshipJoin {

        /**
         * Detach this branch from the other branches so it will not be resynchronized with the other branches.
         */
        D, 
        /**
         * When all other concurrent branches are terminated, interrupt and discontinue this branch.
         */
        K, 
        /**
         * Wait for this branch to terminate.
         */
        W, 
        /**
         * Wait for any one of the branches in the set of exclusive wait branches to terminate, then discontinue all the other exclusive wait branches.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipJoin fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("D".equals(codeString))
          return D;
        if ("K".equals(codeString))
          return K;
        if ("W".equals(codeString))
          return W;
        if ("X".equals(codeString))
          return X;
        throw new Exception("Unknown V3ActRelationshipJoin code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case D: return "D";
            case K: return "K";
            case W: return "W";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipJoin";
        }
        public String getDefinition() {
          switch (this) {
            case D: return "Detach this branch from the other branches so it will not be resynchronized with the other branches.";
            case K: return "When all other concurrent branches are terminated, interrupt and discontinue this branch.";
            case W: return "Wait for this branch to terminate.";
            case X: return "Wait for any one of the branches in the set of exclusive wait branches to terminate, then discontinue all the other exclusive wait branches.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case D: return "detached";
            case K: return "kill";
            case W: return "wait";
            case X: return "exclusive wait";
            default: return "?";
          }
    }


}

