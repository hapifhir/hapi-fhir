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

public enum LOINC480020Answerlist {

        /**
         * Germline
         */
        LA66832, 
        /**
         * Somatic
         */
        LA66840, 
        /**
         * Prenatal
         */
        LA104291, 
        /**
         * Likely Germline
         */
        LA181943, 
        /**
         * Likely Somatic
         */
        LA181950, 
        /**
         * Likely Prenatal
         */
        LA181968, 
        /**
         * Unknown Genomic Origin
         */
        LA181976, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC480020Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA6683-2".equals(codeString))
          return LA66832;
        if ("LA6684-0".equals(codeString))
          return LA66840;
        if ("LA10429-1".equals(codeString))
          return LA104291;
        if ("LA18194-3".equals(codeString))
          return LA181943;
        if ("LA18195-0".equals(codeString))
          return LA181950;
        if ("LA18196-8".equals(codeString))
          return LA181968;
        if ("LA18197-6".equals(codeString))
          return LA181976;
        throw new FHIRException("Unknown LOINC480020Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA66832: return "LA6683-2";
            case LA66840: return "LA6684-0";
            case LA104291: return "LA10429-1";
            case LA181943: return "LA18194-3";
            case LA181950: return "LA18195-0";
            case LA181968: return "LA18196-8";
            case LA181976: return "LA18197-6";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-48002-0-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA66832: return "Germline";
            case LA66840: return "Somatic";
            case LA104291: return "Prenatal";
            case LA181943: return "Likely Germline";
            case LA181950: return "Likely Somatic";
            case LA181968: return "Likely Prenatal";
            case LA181976: return "Unknown Genomic Origin";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA66832: return "Germline";
            case LA66840: return "Somatic";
            case LA104291: return "Prenatal";
            case LA181943: return "Likely Germline";
            case LA181950: return "Likely Somatic";
            case LA181968: return "Likely Prenatal";
            case LA181976: return "Unknown Genomic Origin";
            default: return "?";
          }
    }


}

