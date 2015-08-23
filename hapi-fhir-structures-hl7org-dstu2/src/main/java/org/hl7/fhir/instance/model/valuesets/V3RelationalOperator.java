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


public enum V3RelationalOperator {

        /**
         * Specified set of things includes value being evaluated.
         */
        CT, 
        /**
         * Equal condition applied to comparisons.
         */
        EQ, 
        /**
         * Greater than or equal condition applied to comparisons.
         */
        GE, 
        /**
         * A generic comparison selects a record for inclusion in the response if the beginning of the designated element value matches the select string.
         */
        GN, 
        /**
         * Greater than condition applied to comparisons.
         */
        GT, 
        /**
         * Less than or equal condition applied to comparisons.
         */
        LE, 
        /**
         * Less than condition applied to comparisons.
         */
        LT, 
        /**
         * Not equal condition applied to comparisons.
         */
        NE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RelationalOperator fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("CT".equals(codeString))
          return CT;
        if ("EQ".equals(codeString))
          return EQ;
        if ("GE".equals(codeString))
          return GE;
        if ("GN".equals(codeString))
          return GN;
        if ("GT".equals(codeString))
          return GT;
        if ("LE".equals(codeString))
          return LE;
        if ("LT".equals(codeString))
          return LT;
        if ("NE".equals(codeString))
          return NE;
        throw new Exception("Unknown V3RelationalOperator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CT: return "CT";
            case EQ: return "EQ";
            case GE: return "GE";
            case GN: return "GN";
            case GT: return "GT";
            case LE: return "LE";
            case LT: return "LT";
            case NE: return "NE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RelationalOperator";
        }
        public String getDefinition() {
          switch (this) {
            case CT: return "Specified set of things includes value being evaluated.";
            case EQ: return "Equal condition applied to comparisons.";
            case GE: return "Greater than or equal condition applied to comparisons.";
            case GN: return "A generic comparison selects a record for inclusion in the response if the beginning of the designated element value matches the select string.";
            case GT: return "Greater than condition applied to comparisons.";
            case LE: return "Less than or equal condition applied to comparisons.";
            case LT: return "Less than condition applied to comparisons.";
            case NE: return "Not equal condition applied to comparisons.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CT: return "Contains";
            case EQ: return "Equal";
            case GE: return "Greater than or equal";
            case GN: return "Generic";
            case GT: return "Greater than";
            case LE: return "Less than or equal";
            case LT: return "Less than";
            case NE: return "Not Equal";
            default: return "?";
          }
    }


}

