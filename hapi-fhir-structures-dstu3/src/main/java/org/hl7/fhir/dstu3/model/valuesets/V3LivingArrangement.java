package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3LivingArrangement {

        /**
         * Definition: Living arrangements lacking a permanent residence.
         */
        HL, 
        /**
         * Nomadic
         */
        M, 
        /**
         * Transient
         */
        T, 
        /**
         * Institution
         */
        I, 
        /**
         * Definition: A group living arrangement specifically for the care of those in need of temporary and crisis housing assistance.  Examples include domestic violence shelters, shelters for displaced or homeless individuals, Salvation Army, Jesus House, etc.  Community based services may be provided.
         */
        CS, 
        /**
         * Group Home
         */
        G, 
        /**
         * Nursing Home
         */
        N, 
        /**
         * Extended care facility
         */
        X, 
        /**
         * Definition:  A living arrangement within a private residence for single family.
         */
        PR, 
        /**
         * Independent Household
         */
        H, 
        /**
         * Retirement Community
         */
        R, 
        /**
         * Definition: Assisted living in a single family residence for persons with physical, behavioral, or functional health, or socio-economic challenges.  There may or may not be on-site supervision but the housing is designed to assist the client with developing independent living skills. Community based services may be provided.
         */
        SL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LivingArrangement fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HL".equals(codeString))
          return HL;
        if ("M".equals(codeString))
          return M;
        if ("T".equals(codeString))
          return T;
        if ("I".equals(codeString))
          return I;
        if ("CS".equals(codeString))
          return CS;
        if ("G".equals(codeString))
          return G;
        if ("N".equals(codeString))
          return N;
        if ("X".equals(codeString))
          return X;
        if ("PR".equals(codeString))
          return PR;
        if ("H".equals(codeString))
          return H;
        if ("R".equals(codeString))
          return R;
        if ("SL".equals(codeString))
          return SL;
        throw new FHIRException("Unknown V3LivingArrangement code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HL: return "HL";
            case M: return "M";
            case T: return "T";
            case I: return "I";
            case CS: return "CS";
            case G: return "G";
            case N: return "N";
            case X: return "X";
            case PR: return "PR";
            case H: return "H";
            case R: return "R";
            case SL: return "SL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LivingArrangement";
        }
        public String getDefinition() {
          switch (this) {
            case HL: return "Definition: Living arrangements lacking a permanent residence.";
            case M: return "Nomadic";
            case T: return "Transient";
            case I: return "Institution";
            case CS: return "Definition: A group living arrangement specifically for the care of those in need of temporary and crisis housing assistance.  Examples include domestic violence shelters, shelters for displaced or homeless individuals, Salvation Army, Jesus House, etc.  Community based services may be provided.";
            case G: return "Group Home";
            case N: return "Nursing Home";
            case X: return "Extended care facility";
            case PR: return "Definition:  A living arrangement within a private residence for single family.";
            case H: return "Independent Household";
            case R: return "Retirement Community";
            case SL: return "Definition: Assisted living in a single family residence for persons with physical, behavioral, or functional health, or socio-economic challenges.  There may or may not be on-site supervision but the housing is designed to assist the client with developing independent living skills. Community based services may be provided.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HL: return "homeless";
            case M: return "Nomadic";
            case T: return "Transient";
            case I: return "Institution";
            case CS: return "community shelter";
            case G: return "Group Home";
            case N: return "Nursing Home";
            case X: return "Extended care facility";
            case PR: return "private residence";
            case H: return "Independent Household";
            case R: return "Retirement Community";
            case SL: return "supported living";
            default: return "?";
          }
    }


}

