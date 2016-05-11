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


public enum ServiceUscls {

        /**
         * Exam, comp, primary
         */
        _1101, 
        /**
         * Exam, comp, mixed
         */
        _1102, 
        /**
         * Exam, comp, permanent
         */
        _1103, 
        /**
         * Exam, recall
         */
        _1201, 
        /**
         * Exam, emergency
         */
        _1205, 
        /**
         * Radiograph, series (12)
         */
        _2101, 
        /**
         * Radiograph, series (16)
         */
        _2102, 
        /**
         * Radiograph, bytewing
         */
        _2141, 
        /**
         * Radiograph, panoramic
         */
        _2601, 
        /**
         * Polishing, 1 unit
         */
        _11101, 
        /**
         * Polishing, 2 unit
         */
        _11102, 
        /**
         * Polishing, 3 unit
         */
        _11103, 
        /**
         * Polishing, 4 unit
         */
        _11104, 
        /**
         * Amalgam, 1 surface
         */
        _21211, 
        /**
         * Amalgam, 2 surface
         */
        _21212, 
        /**
         * Crown, PFM
         */
        _27211, 
        /**
         * Lab, commercial
         */
        _99111, 
        /**
         * Lab, in office
         */
        _99333, 
        /**
         * Expense
         */
        _99555, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceUscls fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1101".equals(codeString))
          return _1101;
        if ("1102".equals(codeString))
          return _1102;
        if ("1103".equals(codeString))
          return _1103;
        if ("1201".equals(codeString))
          return _1201;
        if ("1205".equals(codeString))
          return _1205;
        if ("2101".equals(codeString))
          return _2101;
        if ("2102".equals(codeString))
          return _2102;
        if ("2141".equals(codeString))
          return _2141;
        if ("2601".equals(codeString))
          return _2601;
        if ("11101".equals(codeString))
          return _11101;
        if ("11102".equals(codeString))
          return _11102;
        if ("11103".equals(codeString))
          return _11103;
        if ("11104".equals(codeString))
          return _11104;
        if ("21211".equals(codeString))
          return _21211;
        if ("21212".equals(codeString))
          return _21212;
        if ("27211".equals(codeString))
          return _27211;
        if ("99111".equals(codeString))
          return _99111;
        if ("99333".equals(codeString))
          return _99333;
        if ("99555".equals(codeString))
          return _99555;
        throw new Exception("Unknown ServiceUscls code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1101: return "1101";
            case _1102: return "1102";
            case _1103: return "1103";
            case _1201: return "1201";
            case _1205: return "1205";
            case _2101: return "2101";
            case _2102: return "2102";
            case _2141: return "2141";
            case _2601: return "2601";
            case _11101: return "11101";
            case _11102: return "11102";
            case _11103: return "11103";
            case _11104: return "11104";
            case _21211: return "21211";
            case _21212: return "21212";
            case _27211: return "27211";
            case _99111: return "99111";
            case _99333: return "99333";
            case _99555: return "99555";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-USCLS";
        }
        public String getDefinition() {
          switch (this) {
            case _1101: return "Exam, comp, primary";
            case _1102: return "Exam, comp, mixed";
            case _1103: return "Exam, comp, permanent";
            case _1201: return "Exam, recall";
            case _1205: return "Exam, emergency";
            case _2101: return "Radiograph, series (12)";
            case _2102: return "Radiograph, series (16)";
            case _2141: return "Radiograph, bytewing";
            case _2601: return "Radiograph, panoramic";
            case _11101: return "Polishing, 1 unit";
            case _11102: return "Polishing, 2 unit";
            case _11103: return "Polishing, 3 unit";
            case _11104: return "Polishing, 4 unit";
            case _21211: return "Amalgam, 1 surface";
            case _21212: return "Amalgam, 2 surface";
            case _27211: return "Crown, PFM";
            case _99111: return "Lab, commercial";
            case _99333: return "Lab, in office";
            case _99555: return "Expense";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1101: return "Exam, comp, primary";
            case _1102: return "Exam, comp, mixed";
            case _1103: return "Exam, comp, permanent";
            case _1201: return "Exam, recall";
            case _1205: return "Exam, emergency";
            case _2101: return "Radiograph, series (12)";
            case _2102: return "Radiograph, series (16)";
            case _2141: return "Radiograph, bytewing";
            case _2601: return "Radiograph, panoramic";
            case _11101: return "Polishing, 1 unit";
            case _11102: return "Polishing, 2 unit";
            case _11103: return "Polishing, 3 unit";
            case _11104: return "Polishing, 4 unit";
            case _21211: return "Amalgam, 1 surface";
            case _21212: return "Amalgam, 2 surface";
            case _27211: return "Crown, PFM";
            case _99111: return "Lab, commercial";
            case _99333: return "Lab, in office";
            case _99555: return "Expense";
            default: return "?";
          }
    }


}

