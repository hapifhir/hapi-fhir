package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3IntegrityCheckAlgorithm {

        /**
         * This algorithm is defined in FIPS PUB 180-1: Secure Hash Standard.  As of April 17, 1995.
         */
        SHA1, 
        /**
         * This algorithm is defined in FIPS PUB 180-2: Secure Hash Standard.
         */
        SHA256, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3IntegrityCheckAlgorithm fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("SHA-1".equals(codeString))
          return SHA1;
        if ("SHA-256".equals(codeString))
          return SHA256;
        throw new FHIRException("Unknown V3IntegrityCheckAlgorithm code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SHA1: return "SHA-1";
            case SHA256: return "SHA-256";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/IntegrityCheckAlgorithm";
        }
        public String getDefinition() {
          switch (this) {
            case SHA1: return "This algorithm is defined in FIPS PUB 180-1: Secure Hash Standard.  As of April 17, 1995.";
            case SHA256: return "This algorithm is defined in FIPS PUB 180-2: Secure Hash Standard.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SHA1: return "secure hash algorithm - 1";
            case SHA256: return "secure hash algorithm - 256";
            default: return "?";
          }
    }


}

