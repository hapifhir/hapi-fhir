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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum ExRevenueCenter {

        /**
         * Anaesthesia.
         */
        _0370, 
        /**
         * Physical Therapy.
         */
        _0420, 
        /**
         * Physical Therapy - visit charge.
         */
        _0421, 
        /**
         * Speech-Language Pathology.
         */
        _0440, 
        /**
         * Speech-Language Pathology- visit charge
         */
        _0441, 
        /**
         * Emergency Room
         */
        _0450, 
        /**
         * Emergency Room - EM/EMTALA
         */
        _0451, 
        /**
         * Emergency Room - beyond EMTALA
         */
        _0452, 
        /**
         * Vision Clinic
         */
        _0010, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExRevenueCenter fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0370".equals(codeString))
          return _0370;
        if ("0420".equals(codeString))
          return _0420;
        if ("0421".equals(codeString))
          return _0421;
        if ("0440".equals(codeString))
          return _0440;
        if ("0441".equals(codeString))
          return _0441;
        if ("0450".equals(codeString))
          return _0450;
        if ("0451".equals(codeString))
          return _0451;
        if ("0452".equals(codeString))
          return _0452;
        if ("0010".equals(codeString))
          return _0010;
        throw new FHIRException("Unknown ExRevenueCenter code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _0370: return "0370";
            case _0420: return "0420";
            case _0421: return "0421";
            case _0440: return "0440";
            case _0441: return "0441";
            case _0450: return "0450";
            case _0451: return "0451";
            case _0452: return "0452";
            case _0010: return "0010";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-revenue-center";
        }
        public String getDefinition() {
          switch (this) {
            case _0370: return "Anaesthesia.";
            case _0420: return "Physical Therapy.";
            case _0421: return "Physical Therapy - visit charge.";
            case _0440: return "Speech-Language Pathology.";
            case _0441: return "Speech-Language Pathology- visit charge";
            case _0450: return "Emergency Room";
            case _0451: return "Emergency Room - EM/EMTALA";
            case _0452: return "Emergency Room - beyond EMTALA";
            case _0010: return "Vision Clinic";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _0370: return "Anaesthesia";
            case _0420: return "Physical Therapy";
            case _0421: return "Physical Therapy - ";
            case _0440: return "Speech-Language Pathology";
            case _0441: return "Speech-Language Pathology - Visit";
            case _0450: return "Emergency Room";
            case _0451: return "Emergency Room - EM/EMTALA";
            case _0452: return "Emergency Room - beyond EMTALA";
            case _0010: return "Vision Clinic";
            default: return "?";
          }
    }


}

