package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EmployeeJobClass {

        /**
         * Employment in which the employee is expected to work at least a standard work week (defined by the US Bureau of Labor Statistics as 35-44 hours per week)
         */
        FT, 
        /**
         * Employment in which the employee is expected to work less than a standard work week (defined by the US Bureau of Labor Statistics as 35-44 hours per week)
         */
        PT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EmployeeJobClass fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("FT".equals(codeString))
          return FT;
        if ("PT".equals(codeString))
          return PT;
        throw new FHIRException("Unknown V3EmployeeJobClass code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FT: return "FT";
            case PT: return "PT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EmployeeJobClass";
        }
        public String getDefinition() {
          switch (this) {
            case FT: return "Employment in which the employee is expected to work at least a standard work week (defined by the US Bureau of Labor Statistics as 35-44 hours per week)";
            case PT: return "Employment in which the employee is expected to work less than a standard work week (defined by the US Bureau of Labor Statistics as 35-44 hours per week)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FT: return "full-time";
            case PT: return "part-time";
            default: return "?";
          }
    }


}

