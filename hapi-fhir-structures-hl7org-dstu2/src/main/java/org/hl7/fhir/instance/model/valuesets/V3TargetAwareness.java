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


public enum V3TargetAwareness {

        /**
         * Target person has been informed about the issue but currently denies it.
         */
        D, 
        /**
         * Target person is fully aware of the issue.
         */
        F, 
        /**
         * Target person is not capable of comprehending the issue.
         */
        I, 
        /**
         * Target person is marginally aware of the issue.
         */
        M, 
        /**
         * Target person is partially aware of the issue.
         */
        P, 
        /**
         * Target person has not yet been informed of the issue.
         */
        U, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TargetAwareness fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("D".equals(codeString))
          return D;
        if ("F".equals(codeString))
          return F;
        if ("I".equals(codeString))
          return I;
        if ("M".equals(codeString))
          return M;
        if ("P".equals(codeString))
          return P;
        if ("U".equals(codeString))
          return U;
        throw new Exception("Unknown V3TargetAwareness code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case D: return "D";
            case F: return "F";
            case I: return "I";
            case M: return "M";
            case P: return "P";
            case U: return "U";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TargetAwareness";
        }
        public String getDefinition() {
          switch (this) {
            case D: return "Target person has been informed about the issue but currently denies it.";
            case F: return "Target person is fully aware of the issue.";
            case I: return "Target person is not capable of comprehending the issue.";
            case M: return "Target person is marginally aware of the issue.";
            case P: return "Target person is partially aware of the issue.";
            case U: return "Target person has not yet been informed of the issue.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case D: return "denying";
            case F: return "full awareness";
            case I: return "incapable";
            case M: return "marginal";
            case P: return "partial";
            case U: return "uninformed";
            default: return "?";
          }
    }


}

