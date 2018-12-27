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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum FHIRVersion {

        /**
         * Oldest archived version of FHIR.
         */
        _0_01, 
        /**
         * 1st Draft for Comment (Sept 2012 Ballot).
         */
        _0_05, 
        /**
         * 2nd Draft for Comment (January 2013 Ballot).
         */
        _0_06, 
        /**
         * DSTU 1 Ballot version.
         */
        _0_11, 
        /**
         * DSTU 1 Official version.
         */
        _0_0_80, 
        /**
         * DSTU 1 Official version Technical Errata #1.
         */
        _0_0_81, 
        /**
         * DSTU 1 Official version Technical Errata #2.
         */
        _0_0_82, 
        /**
         * Draft For Comment (January 2015 Ballot).
         */
        _0_4_0, 
        /**
         * DSTU 2 Ballot version (May 2015 Ballot).
         */
        _0_5_0, 
        /**
         * DSTU 2 QA Preview + CQIF Ballot (Sep 2015).
         */
        _1_0_0, 
        /**
         * DSTU 2 (Official version).
         */
        _1_0_1, 
        /**
         * DSTU 2 (Official version) with 1 technical errata.
         */
        _1_0_2, 
        /**
         * GAO Ballot + draft changes to main FHIR standard.
         */
        _1_1_0, 
        /**
         * CQF on FHIR Ballot + Connectathon 12 (Montreal).
         */
        _1_4_0, 
        /**
         * FHIR STU3 Ballot + Connectathon 13 (Baltimore).
         */
        _1_6_0, 
        /**
         * FHIR STU3 Candidate + Connectathon 14 (San Antonio).
         */
        _1_8_0, 
        /**
         * FHIR Release 3 (STU).
         */
        _3_0_0, 
        /**
         * FHIR Release 3 (STU) with 1 technical errata.
         */
        _3_0_1, 
        /**
         * R4 Ballot #1.
         */
        _3_3_0, 
        /**
         * R4 Ballot #2.
         */
        _3_5_0, 
        /**
         * FHIR Release 4 (Normative + STU).
         */
        _4_0_0, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FHIRVersion fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0.01".equals(codeString))
          return _0_01;
        if ("0.05".equals(codeString))
          return _0_05;
        if ("0.06".equals(codeString))
          return _0_06;
        if ("0.11".equals(codeString))
          return _0_11;
        if ("0.0.80".equals(codeString))
          return _0_0_80;
        if ("0.0.81".equals(codeString))
          return _0_0_81;
        if ("0.0.82".equals(codeString))
          return _0_0_82;
        if ("0.4.0".equals(codeString))
          return _0_4_0;
        if ("0.5.0".equals(codeString))
          return _0_5_0;
        if ("1.0.0".equals(codeString))
          return _1_0_0;
        if ("1.0.1".equals(codeString))
          return _1_0_1;
        if ("1.0.2".equals(codeString))
          return _1_0_2;
        if ("1.1.0".equals(codeString))
          return _1_1_0;
        if ("1.4.0".equals(codeString))
          return _1_4_0;
        if ("1.6.0".equals(codeString))
          return _1_6_0;
        if ("1.8.0".equals(codeString))
          return _1_8_0;
        if ("3.0.0".equals(codeString))
          return _3_0_0;
        if ("3.0.1".equals(codeString))
          return _3_0_1;
        if ("3.3.0".equals(codeString))
          return _3_3_0;
        if ("3.5.0".equals(codeString))
          return _3_5_0;
        if ("4.0.0".equals(codeString))
          return _4_0_0;
        throw new FHIRException("Unknown FHIRVersion code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _0_01: return "0.01";
            case _0_05: return "0.05";
            case _0_06: return "0.06";
            case _0_11: return "0.11";
            case _0_0_80: return "0.0.80";
            case _0_0_81: return "0.0.81";
            case _0_0_82: return "0.0.82";
            case _0_4_0: return "0.4.0";
            case _0_5_0: return "0.5.0";
            case _1_0_0: return "1.0.0";
            case _1_0_1: return "1.0.1";
            case _1_0_2: return "1.0.2";
            case _1_1_0: return "1.1.0";
            case _1_4_0: return "1.4.0";
            case _1_6_0: return "1.6.0";
            case _1_8_0: return "1.8.0";
            case _3_0_0: return "3.0.0";
            case _3_0_1: return "3.0.1";
            case _3_3_0: return "3.3.0";
            case _3_5_0: return "3.5.0";
            case _4_0_0: return "4.0.0";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/FHIR-version";
        }
        public String getDefinition() {
          switch (this) {
            case _0_01: return "Oldest archived version of FHIR.";
            case _0_05: return "1st Draft for Comment (Sept 2012 Ballot).";
            case _0_06: return "2nd Draft for Comment (January 2013 Ballot).";
            case _0_11: return "DSTU 1 Ballot version.";
            case _0_0_80: return "DSTU 1 Official version.";
            case _0_0_81: return "DSTU 1 Official version Technical Errata #1.";
            case _0_0_82: return "DSTU 1 Official version Technical Errata #2.";
            case _0_4_0: return "Draft For Comment (January 2015 Ballot).";
            case _0_5_0: return "DSTU 2 Ballot version (May 2015 Ballot).";
            case _1_0_0: return "DSTU 2 QA Preview + CQIF Ballot (Sep 2015).";
            case _1_0_1: return "DSTU 2 (Official version).";
            case _1_0_2: return "DSTU 2 (Official version) with 1 technical errata.";
            case _1_1_0: return "GAO Ballot + draft changes to main FHIR standard.";
            case _1_4_0: return "CQF on FHIR Ballot + Connectathon 12 (Montreal).";
            case _1_6_0: return "FHIR STU3 Ballot + Connectathon 13 (Baltimore).";
            case _1_8_0: return "FHIR STU3 Candidate + Connectathon 14 (San Antonio).";
            case _3_0_0: return "FHIR Release 3 (STU).";
            case _3_0_1: return "FHIR Release 3 (STU) with 1 technical errata.";
            case _3_3_0: return "R4 Ballot #1.";
            case _3_5_0: return "R4 Ballot #2.";
            case _4_0_0: return "FHIR Release 4 (Normative + STU).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _0_01: return "0.01";
            case _0_05: return "0.05";
            case _0_06: return "0.06";
            case _0_11: return "0.11";
            case _0_0_80: return "0.0.80";
            case _0_0_81: return "0.0.81";
            case _0_0_82: return "0.0.82";
            case _0_4_0: return "0.4.0";
            case _0_5_0: return "0.5.0";
            case _1_0_0: return "1.0.0";
            case _1_0_1: return "1.0.1";
            case _1_0_2: return "1.0.2";
            case _1_1_0: return "1.1.0";
            case _1_4_0: return "1.4.0";
            case _1_6_0: return "1.6.0";
            case _1_8_0: return "1.8.0";
            case _3_0_0: return "3.0.0";
            case _3_0_1: return "3.0.1";
            case _3_3_0: return "3.3.0";
            case _3_5_0: return "3.5.0";
            case _4_0_0: return "4.0.0";
            default: return "?";
          }
    }


}

