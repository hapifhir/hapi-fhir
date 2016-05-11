package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

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
        public static V3EducationLevel fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown V3EducationLevel code '"+codeString+"'");
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

