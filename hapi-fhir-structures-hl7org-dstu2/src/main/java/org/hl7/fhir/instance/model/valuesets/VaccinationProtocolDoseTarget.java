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

// Generated on Thu, Aug 27, 2015 19:45-0400 for FHIR v0.5.0


public enum VaccinationProtocolDoseTarget {

        /**
         * null
         */
        CRS, 
        /**
         * null
         */
        DIP, 
        /**
         * null
         */
        MEA, 
        /**
         * null
         */
        MUM, 
        /**
         * null
         */
        RUB, 
        /**
         * null
         */
        TET, 
        /**
         * null
         */
        HIB, 
        /**
         * null
         */
        PER, 
        /**
         * null
         */
        POL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VaccinationProtocolDoseTarget fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("crs".equals(codeString))
          return CRS;
        if ("dip".equals(codeString))
          return DIP;
        if ("mea".equals(codeString))
          return MEA;
        if ("mum".equals(codeString))
          return MUM;
        if ("rub".equals(codeString))
          return RUB;
        if ("tet".equals(codeString))
          return TET;
        if ("hib".equals(codeString))
          return HIB;
        if ("per".equals(codeString))
          return PER;
        if ("pol".equals(codeString))
          return POL;
        throw new Exception("Unknown VaccinationProtocolDoseTarget code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CRS: return "crs";
            case DIP: return "dip";
            case MEA: return "mea";
            case MUM: return "mum";
            case RUB: return "rub";
            case TET: return "tet";
            case HIB: return "hib";
            case PER: return "per";
            case POL: return "pol";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/vaccination-protocol-dose-target";
        }
        public String getDefinition() {
          switch (this) {
            case CRS: return "";
            case DIP: return "";
            case MEA: return "";
            case MUM: return "";
            case RUB: return "";
            case TET: return "";
            case HIB: return "";
            case PER: return "";
            case POL: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CRS: return "Congenital Rubella Syndrome";
            case DIP: return "Diptheria";
            case MEA: return "Measles";
            case MUM: return "Mumps";
            case RUB: return "Rubella";
            case TET: return "Tetanus";
            case HIB: return "Haemophilus influenzae type b";
            case PER: return "Pertussis";
            case POL: return "Poliomyelitis";
            default: return "?";
          }
    }


}

