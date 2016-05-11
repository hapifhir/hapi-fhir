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


public enum V3EntityHandling {

        /**
         * Keep at ambient temperature, 22 +/- 2C
         */
        AMB, 
        /**
         * Critical to keep at body temperature 36-38C
         */
        C37, 
        /**
         * Critical ambient - must not be refrigerated or frozen.
         */
        CAMB, 
        /**
         * Critical. Do not expose to atmosphere.  Do not uncap.
         */
        CATM, 
        /**
         * Critical frozen. Specimen must not be allowed to thaw until immediately prior to testing.
         */
        CFRZ, 
        /**
         * Critical refrigerated - must not be allowed to freeze or warm until imediately prior to testing.
         */
        CREF, 
        /**
         * Deep Frozen -16 to -20C.
         */
        DFRZ, 
        /**
         * Keep in a dry environment
         */
        DRY, 
        /**
         * Keep frozen below 0 ?C
         */
        FRZ, 
        /**
         * Container is free of heavy metals, including lead.
         */
        MTLF, 
        /**
         * Keep in liquid nitrogen
         */
        NTR, 
        /**
         * Protect from light (eg. Wrap in aluminum foil).
         */
        PRTL, 
        /**
         * Do not shake
         */
        PSA, 
        /**
         * Protect against shock
         */
        PSO, 
        /**
         * Keep at refrigerated temperature:4-8C Accidental warming or freezing is of little consequence.
         */
        REF, 
        /**
         * Shake thoroughly before using
         */
        SBU, 
        /**
         * Ultra cold frozen -75 to -85C.  Ultra cold freezer is typically at temperature of dry ice.
         */
        UFRZ, 
        /**
         * Keep upright, do not turn upside down
         */
        UPR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityHandling fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AMB".equals(codeString))
          return AMB;
        if ("C37".equals(codeString))
          return C37;
        if ("CAMB".equals(codeString))
          return CAMB;
        if ("CATM".equals(codeString))
          return CATM;
        if ("CFRZ".equals(codeString))
          return CFRZ;
        if ("CREF".equals(codeString))
          return CREF;
        if ("DFRZ".equals(codeString))
          return DFRZ;
        if ("DRY".equals(codeString))
          return DRY;
        if ("FRZ".equals(codeString))
          return FRZ;
        if ("MTLF".equals(codeString))
          return MTLF;
        if ("NTR".equals(codeString))
          return NTR;
        if ("PRTL".equals(codeString))
          return PRTL;
        if ("PSA".equals(codeString))
          return PSA;
        if ("PSO".equals(codeString))
          return PSO;
        if ("REF".equals(codeString))
          return REF;
        if ("SBU".equals(codeString))
          return SBU;
        if ("UFRZ".equals(codeString))
          return UFRZ;
        if ("UPR".equals(codeString))
          return UPR;
        throw new Exception("Unknown V3EntityHandling code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AMB: return "AMB";
            case C37: return "C37";
            case CAMB: return "CAMB";
            case CATM: return "CATM";
            case CFRZ: return "CFRZ";
            case CREF: return "CREF";
            case DFRZ: return "DFRZ";
            case DRY: return "DRY";
            case FRZ: return "FRZ";
            case MTLF: return "MTLF";
            case NTR: return "NTR";
            case PRTL: return "PRTL";
            case PSA: return "PSA";
            case PSO: return "PSO";
            case REF: return "REF";
            case SBU: return "SBU";
            case UFRZ: return "UFRZ";
            case UPR: return "UPR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityHandling";
        }
        public String getDefinition() {
          switch (this) {
            case AMB: return "Keep at ambient temperature, 22 +/- 2C";
            case C37: return "Critical to keep at body temperature 36-38C";
            case CAMB: return "Critical ambient - must not be refrigerated or frozen.";
            case CATM: return "Critical. Do not expose to atmosphere.  Do not uncap.";
            case CFRZ: return "Critical frozen. Specimen must not be allowed to thaw until immediately prior to testing.";
            case CREF: return "Critical refrigerated - must not be allowed to freeze or warm until imediately prior to testing.";
            case DFRZ: return "Deep Frozen -16 to -20C.";
            case DRY: return "Keep in a dry environment";
            case FRZ: return "Keep frozen below 0 ?C";
            case MTLF: return "Container is free of heavy metals, including lead.";
            case NTR: return "Keep in liquid nitrogen";
            case PRTL: return "Protect from light (eg. Wrap in aluminum foil).";
            case PSA: return "Do not shake";
            case PSO: return "Protect against shock";
            case REF: return "Keep at refrigerated temperature:4-8C Accidental warming or freezing is of little consequence.";
            case SBU: return "Shake thoroughly before using";
            case UFRZ: return "Ultra cold frozen -75 to -85C.  Ultra cold freezer is typically at temperature of dry ice.";
            case UPR: return "Keep upright, do not turn upside down";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AMB: return "Ambient Temperature";
            case C37: return "Body Temperature";
            case CAMB: return "Critical Ambient temperature";
            case CATM: return "Protect from Air";
            case CFRZ: return "Critical frozen";
            case CREF: return "Critical refrigerated temperature";
            case DFRZ: return "Deep Frozen";
            case DRY: return "dry";
            case FRZ: return "frozen";
            case MTLF: return "Metal Free";
            case NTR: return "nitrogen";
            case PRTL: return "Protect from Light";
            case PSA: return "do not shake";
            case PSO: return "no shock";
            case REF: return "Refrigerated temperature";
            case SBU: return "Shake before use";
            case UFRZ: return "Ultra frozen";
            case UPR: return "upright";
            default: return "?";
          }
    }


}

