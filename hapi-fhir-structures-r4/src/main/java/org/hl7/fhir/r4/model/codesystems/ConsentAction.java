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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ConsentAction {

        /**
         * Gather/acquire information by an entity to achieve a stated purpose
         */
        COLLECT, 
        /**
         * Provide an entity access to information to achieve a stated purpose
         */
        ACCESS, 
        /**
         * Use of stored information by an entity fo the stated purpose
         */
        USE, 
        /**
         * Release / transfer of information to an entity 
         */
        DISCLOSE, 
        /**
         * Allowing an entity to correct a patient's information
         */
        CORRECT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConsentAction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("collect".equals(codeString))
          return COLLECT;
        if ("access".equals(codeString))
          return ACCESS;
        if ("use".equals(codeString))
          return USE;
        if ("disclose".equals(codeString))
          return DISCLOSE;
        if ("correct".equals(codeString))
          return CORRECT;
        throw new FHIRException("Unknown ConsentAction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COLLECT: return "collect";
            case ACCESS: return "access";
            case USE: return "use";
            case DISCLOSE: return "disclose";
            case CORRECT: return "correct";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/consentaction";
        }
        public String getDefinition() {
          switch (this) {
            case COLLECT: return "Gather/acquire information by an entity to achieve a stated purpose";
            case ACCESS: return "Provide an entity access to information to achieve a stated purpose";
            case USE: return "Use of stored information by an entity fo the stated purpose";
            case DISCLOSE: return "Release / transfer of information to an entity ";
            case CORRECT: return "Allowing an entity to correct a patient's information";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COLLECT: return "Collect";
            case ACCESS: return "Access";
            case USE: return "Use";
            case DISCLOSE: return "Disclose";
            case CORRECT: return "Access and Correct";
            default: return "?";
          }
    }


}

