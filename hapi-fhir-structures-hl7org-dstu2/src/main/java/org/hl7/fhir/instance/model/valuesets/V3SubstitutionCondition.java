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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3SubstitutionCondition {

        /**
         * Some conditions may be attached to an allowable substitution.  An allowable substitution is based on a match to any other attributes that may be specified.
         */
        _CONDITIONAL, 
        /**
         * Confirmation with Contact Person prior to making any substitutions has or will occur.
         */
        CONFIRM, 
        /**
         * Notification to the Contact Person, prior to substitution and through normal institutional procedures, has or will be made.
         */
        NOTIFY, 
        /**
         * Substitution is not permitted.
         */
        NOSUB, 
        /**
         * No conditions are required.
         */
        UNCOND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3SubstitutionCondition fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_Conditional".equals(codeString))
          return _CONDITIONAL;
        if ("CONFIRM".equals(codeString))
          return CONFIRM;
        if ("NOTIFY".equals(codeString))
          return NOTIFY;
        if ("NOSUB".equals(codeString))
          return NOSUB;
        if ("UNCOND".equals(codeString))
          return UNCOND;
        throw new Exception("Unknown V3SubstitutionCondition code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _CONDITIONAL: return "_Conditional";
            case CONFIRM: return "CONFIRM";
            case NOTIFY: return "NOTIFY";
            case NOSUB: return "NOSUB";
            case UNCOND: return "UNCOND";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/SubstitutionCondition";
        }
        public String getDefinition() {
          switch (this) {
            case _CONDITIONAL: return "Some conditions may be attached to an allowable substitution.  An allowable substitution is based on a match to any other attributes that may be specified.";
            case CONFIRM: return "Confirmation with Contact Person prior to making any substitutions has or will occur.";
            case NOTIFY: return "Notification to the Contact Person, prior to substitution and through normal institutional procedures, has or will be made.";
            case NOSUB: return "Substitution is not permitted.";
            case UNCOND: return "No conditions are required.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _CONDITIONAL: return "Conditional";
            case CONFIRM: return "Confirm first";
            case NOTIFY: return "Notify first";
            case NOSUB: return "No substitution";
            case UNCOND: return "Unconditional";
            default: return "?";
          }
    }


}

