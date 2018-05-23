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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ValidationProcess {

        /**
         * null
         */
        EDITCHECK, 
        /**
         * null
         */
        VALUESET, 
        /**
         * null
         */
        PRIMARY, 
        /**
         * null
         */
        MULTI, 
        /**
         * null
         */
        STANDALONE, 
        /**
         * null
         */
        INCONTEXT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ValidationProcess fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("edit-check".equals(codeString))
          return EDITCHECK;
        if ("valueset".equals(codeString))
          return VALUESET;
        if ("primary".equals(codeString))
          return PRIMARY;
        if ("multi".equals(codeString))
          return MULTI;
        if ("standalone".equals(codeString))
          return STANDALONE;
        if ("in-context".equals(codeString))
          return INCONTEXT;
        throw new FHIRException("Unknown ValidationProcess code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EDITCHECK: return "edit-check";
            case VALUESET: return "valueset";
            case PRIMARY: return "primary";
            case MULTI: return "multi";
            case STANDALONE: return "standalone";
            case INCONTEXT: return "in-context";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/validation-process";
        }
        public String getDefinition() {
          switch (this) {
            case EDITCHECK: return "";
            case VALUESET: return "";
            case PRIMARY: return "";
            case MULTI: return "";
            case STANDALONE: return "";
            case INCONTEXT: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EDITCHECK: return "edit check";
            case VALUESET: return "value set";
            case PRIMARY: return "primary source";
            case MULTI: return "multiple sources";
            case STANDALONE: return "standalone";
            case INCONTEXT: return "in context";
            default: return "?";
          }
    }


}

