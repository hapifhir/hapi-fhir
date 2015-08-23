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


public enum V3CodingRationale {

        /**
         * Description: Originally produced code.
         */
        O, 
        /**
         * Originally produced code, required by the specification describing the use of the coded concept.
         */
        OR, 
        /**
         * Description: Post-coded from free text source</description>
         */
        P, 
        /**
         * Post-coded from free text source, required by the specification describing the use of the coded concept.
         */
        PR, 
        /**
         * Description: Required standard code for HL7.
         */
        R, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3CodingRationale fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("O".equals(codeString))
          return O;
        if ("OR".equals(codeString))
          return OR;
        if ("P".equals(codeString))
          return P;
        if ("PR".equals(codeString))
          return PR;
        if ("R".equals(codeString))
          return R;
        throw new Exception("Unknown V3CodingRationale code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case O: return "O";
            case OR: return "OR";
            case P: return "P";
            case PR: return "PR";
            case R: return "R";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/CodingRationale";
        }
        public String getDefinition() {
          switch (this) {
            case O: return "Description: Originally produced code.";
            case OR: return "Originally produced code, required by the specification describing the use of the coded concept.";
            case P: return "Description: Post-coded from free text source</description>";
            case PR: return "Post-coded from free text source, required by the specification describing the use of the coded concept.";
            case R: return "Description: Required standard code for HL7.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case O: return "originally produced code";
            case OR: return "original and required";
            case P: return "post-coded";
            case PR: return "post-coded and required";
            case R: return "required";
            default: return "?";
          }
    }


}

