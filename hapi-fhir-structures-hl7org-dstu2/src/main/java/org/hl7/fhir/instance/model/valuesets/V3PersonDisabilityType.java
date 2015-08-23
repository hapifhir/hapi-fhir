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


public enum V3PersonDisabilityType {

        /**
         * Vision impaired
         */
        _1, 
        /**
         * Hearing impaired
         */
        _2, 
        /**
         * Speech impaired
         */
        _3, 
        /**
         * Mentally impaired
         */
        _4, 
        /**
         * Mobility impaired
         */
        _5, 
        /**
         * A crib is required to move the person
         */
        CB, 
        /**
         * Person requires crutches to ambulate
         */
        CR, 
        /**
         * A gurney is required to move the person
         */
        G, 
        /**
         * Person requires a wheelchair to be ambulatory
         */
        WC, 
        /**
         * Person requires a walker to ambulate
         */
        WK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3PersonDisabilityType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("CB".equals(codeString))
          return CB;
        if ("CR".equals(codeString))
          return CR;
        if ("G".equals(codeString))
          return G;
        if ("WC".equals(codeString))
          return WC;
        if ("WK".equals(codeString))
          return WK;
        throw new Exception("Unknown V3PersonDisabilityType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case CB: return "CB";
            case CR: return "CR";
            case G: return "G";
            case WC: return "WC";
            case WK: return "WK";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/PersonDisabilityType";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Vision impaired";
            case _2: return "Hearing impaired";
            case _3: return "Speech impaired";
            case _4: return "Mentally impaired";
            case _5: return "Mobility impaired";
            case CB: return "A crib is required to move the person";
            case CR: return "Person requires crutches to ambulate";
            case G: return "A gurney is required to move the person";
            case WC: return "Person requires a wheelchair to be ambulatory";
            case WK: return "Person requires a walker to ambulate";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Vision impaired";
            case _2: return "Hearing impaired";
            case _3: return "Speech impaired";
            case _4: return "Mentally impaired";
            case _5: return "Mobility impaired";
            case CB: return "Requires crib";
            case CR: return "Requires crutches";
            case G: return "Requires gurney";
            case WC: return "Requires wheelchair";
            case WK: return "Requires walker";
            default: return "?";
          }
    }


}

