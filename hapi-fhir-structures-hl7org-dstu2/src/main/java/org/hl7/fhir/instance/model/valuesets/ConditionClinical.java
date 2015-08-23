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


public enum ConditionClinical {

        /**
         * The subject is currently experiencing the symptoms of the condition
         */
        ACTIVE, 
        /**
         * The subject is re-experiencing the symptoms of the condition after a period of remission or presumed resolution
         */
        RELAPSE, 
        /**
         * The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning
         */
        REMISSION, 
        /**
         * The subject is no longer experiencing the symptoms of the condition and there is no perceived risk of the symptoms returning
         */
        RESOLVED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionClinical fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("relapse".equals(codeString))
          return RELAPSE;
        if ("remission".equals(codeString))
          return REMISSION;
        if ("resolved".equals(codeString))
          return RESOLVED;
        throw new Exception("Unknown ConditionClinical code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case RELAPSE: return "relapse";
            case REMISSION: return "remission";
            case RESOLVED: return "resolved";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-clinical";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The subject is currently experiencing the symptoms of the condition";
            case RELAPSE: return "The subject is re-experiencing the symptoms of the condition after a period of remission or presumed resolution";
            case REMISSION: return "The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning";
            case RESOLVED: return "The subject is no longer experiencing the symptoms of the condition and there is no perceived risk of the symptoms returning";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case RELAPSE: return "Relapse";
            case REMISSION: return "Remission";
            case RESOLVED: return "Resolved";
            default: return "?";
          }
    }


}

