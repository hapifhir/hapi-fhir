package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum NameAssemblyOrder {

        /**
         * null
         */
        NL1, 
        /**
         * null
         */
        NL2, 
        /**
         * null
         */
        NL3, 
        /**
         * null
         */
        NL4, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NameAssemblyOrder fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("NL1".equals(codeString))
          return NL1;
        if ("NL2".equals(codeString))
          return NL2;
        if ("NL3".equals(codeString))
          return NL3;
        if ("NL4".equals(codeString))
          return NL4;
        throw new FHIRException("Unknown NameAssemblyOrder code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NL1: return "NL1";
            case NL2: return "NL2";
            case NL3: return "NL3";
            case NL4: return "NL4";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/name-assembly-order";
        }
        public String getDefinition() {
          switch (this) {
            case NL1: return "";
            case NL2: return "";
            case NL3: return "";
            case NL4: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NL1: return "Own Name";
            case NL2: return "Partner Name";
            case NL3: return "Partner Name followed by Maiden Name";
            case NL4: return "Own Name followed by Partner Name";
            default: return "?";
          }
    }


}

