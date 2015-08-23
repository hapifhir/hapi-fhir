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


public enum V3EducationLevel {

        /**
         * Associate's or technical degree complete
         */
        ASSOC, 
        /**
         * College or baccalaureate degree complete
         */
        BD, 
        /**
         * Elementary School
         */
        ELEM, 
        /**
         * Graduate or professional Degree complete
         */
        GD, 
        /**
         * High School or secondary school degree complete
         */
        HS, 
        /**
         * Some post-baccalaureate education
         */
        PB, 
        /**
         * Doctoral or post graduate education
         */
        POSTG, 
        /**
         * Some College education
         */
        SCOL, 
        /**
         * Some secondary or high school education
         */
        SEC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EducationLevel fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ASSOC".equals(codeString))
          return ASSOC;
        if ("BD".equals(codeString))
          return BD;
        if ("ELEM".equals(codeString))
          return ELEM;
        if ("GD".equals(codeString))
          return GD;
        if ("HS".equals(codeString))
          return HS;
        if ("PB".equals(codeString))
          return PB;
        if ("POSTG".equals(codeString))
          return POSTG;
        if ("SCOL".equals(codeString))
          return SCOL;
        if ("SEC".equals(codeString))
          return SEC;
        throw new Exception("Unknown V3EducationLevel code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ASSOC: return "ASSOC";
            case BD: return "BD";
            case ELEM: return "ELEM";
            case GD: return "GD";
            case HS: return "HS";
            case PB: return "PB";
            case POSTG: return "POSTG";
            case SCOL: return "SCOL";
            case SEC: return "SEC";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EducationLevel";
        }
        public String getDefinition() {
          switch (this) {
            case ASSOC: return "Associate's or technical degree complete";
            case BD: return "College or baccalaureate degree complete";
            case ELEM: return "Elementary School";
            case GD: return "Graduate or professional Degree complete";
            case HS: return "High School or secondary school degree complete";
            case PB: return "Some post-baccalaureate education";
            case POSTG: return "Doctoral or post graduate education";
            case SCOL: return "Some College education";
            case SEC: return "Some secondary or high school education";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ASSOC: return "Associate's or technical degree complete";
            case BD: return "College or baccalaureate degree complete";
            case ELEM: return "Elementary School";
            case GD: return "Graduate or professional Degree complete";
            case HS: return "High School or secondary school degree complete";
            case PB: return "Some post-baccalaureate education";
            case POSTG: return "Doctoral or post graduate education";
            case SCOL: return "Some College education";
            case SEC: return "Some secondary or high school education";
            default: return "?";
          }
    }


}

