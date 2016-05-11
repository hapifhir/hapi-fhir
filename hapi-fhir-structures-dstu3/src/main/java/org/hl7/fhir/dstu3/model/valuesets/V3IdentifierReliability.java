package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3IdentifierReliability {

        /**
         * Description: The identifier was issued by the system responsible for constructing the instance.
         */
        ISS, 
        /**
         * Description: The identifier was provided to the system that constructed the instance, but has not been verified. e.g. a Drivers  license entered manually into a system by a user.
         */
        UNV, 
        /**
         * Description: The identifier was not issued by the system responsible for constructing the instance, but the system that captured the id has verified the identifier with the issuing authority, or with another system that has verified the identifier.
         */
        VRF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3IdentifierReliability fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ISS".equals(codeString))
          return ISS;
        if ("UNV".equals(codeString))
          return UNV;
        if ("VRF".equals(codeString))
          return VRF;
        throw new FHIRException("Unknown V3IdentifierReliability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ISS: return "ISS";
            case UNV: return "UNV";
            case VRF: return "VRF";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/IdentifierReliability";
        }
        public String getDefinition() {
          switch (this) {
            case ISS: return "Description: The identifier was issued by the system responsible for constructing the instance.";
            case UNV: return "Description: The identifier was provided to the system that constructed the instance, but has not been verified. e.g. a Drivers  license entered manually into a system by a user.";
            case VRF: return "Description: The identifier was not issued by the system responsible for constructing the instance, but the system that captured the id has verified the identifier with the issuing authority, or with another system that has verified the identifier.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ISS: return "Issued by System";
            case UNV: return "Unverified by system";
            case VRF: return "Verified by system";
            default: return "?";
          }
    }


}

