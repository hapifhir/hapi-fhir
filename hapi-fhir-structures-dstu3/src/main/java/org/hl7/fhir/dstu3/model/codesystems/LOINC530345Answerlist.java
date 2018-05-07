package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Sun, Mar 12, 2017 20:35-0400 for FHIR v1.9.0


import org.hl7.fhir.exceptions.FHIRException;

public enum LOINC530345Answerlist {

        /**
         * Heteroplasmic
         */
        LA67038, 
        /**
         * Homoplasmic
         */
        LA67046, 
        /**
         * Homozygous
         */
        LA67053, 
        /**
         * Heterozygous
         */
        LA67061, 
        /**
         * Hemizygous
         */
        LA67079, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC530345Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA6703-8".equals(codeString))
          return LA67038;
        if ("LA6704-6".equals(codeString))
          return LA67046;
        if ("LA6705-3".equals(codeString))
          return LA67053;
        if ("LA6706-1".equals(codeString))
          return LA67061;
        if ("LA6707-9".equals(codeString))
          return LA67079;
        throw new FHIRException("Unknown LOINC530345Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA67038: return "LA6703-8";
            case LA67046: return "LA6704-6";
            case LA67053: return "LA6705-3";
            case LA67061: return "LA6706-1";
            case LA67079: return "LA6707-9";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-53034-5-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA67038: return "Heteroplasmic";
            case LA67046: return "Homoplasmic";
            case LA67053: return "Homozygous";
            case LA67061: return "Heterozygous";
            case LA67079: return "Hemizygous";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA67038: return "Heteroplasmic";
            case LA67046: return "Homoplasmic";
            case LA67053: return "Homozygous";
            case LA67061: return "Heterozygous";
            case LA67079: return "Hemizygous";
            default: return "?";
          }
    }


}

