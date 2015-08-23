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


public enum EncounterType {

        /**
         * null
         */
        ADMS, 
        /**
         * null
         */
        BD_BMCLIN, 
        /**
         * null
         */
        CCS60, 
        /**
         * null
         */
        OKI, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ADMS".equals(codeString))
          return ADMS;
        if ("BD/BM-clin".equals(codeString))
          return BD_BMCLIN;
        if ("CCS60".equals(codeString))
          return CCS60;
        if ("OKI".equals(codeString))
          return OKI;
        throw new Exception("Unknown EncounterType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADMS: return "ADMS";
            case BD_BMCLIN: return "BD/BM-clin";
            case CCS60: return "CCS60";
            case OKI: return "OKI";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-type";
        }
        public String getDefinition() {
          switch (this) {
            case ADMS: return "";
            case BD_BMCLIN: return "";
            case CCS60: return "";
            case OKI: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADMS: return "Annual diabetes mellitus screening";
            case BD_BMCLIN: return "Bone drilling/bone marrow punction in clinic";
            case CCS60: return "Infant colon screening - 60 minutes";
            case OKI: return "Outpatient Kenacort injection";
            default: return "?";
          }
    }


}

