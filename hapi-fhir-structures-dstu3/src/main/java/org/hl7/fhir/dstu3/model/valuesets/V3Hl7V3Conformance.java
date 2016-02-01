package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3Hl7V3Conformance {

        /**
         * Description: Implementers receiving this property must not raise an error if the data is received, but will not perform any useful function with the data.  This conformance level is not used in profiles or other artifacts that are specific to the "sender" or "initiator" of a communication.
         */
        I, 
        /**
         * Description: All implementers are prohibited from transmitting this content, and may raise an error if they receive it.
         */
        NP, 
        /**
         * Description: All implementers must support this property.  I.e. they must be able to transmit, or to receive and usefully handle the concept.
         */
        R, 
        /**
         * Description: The element is considered "required" (i.e. must be supported) from the perspective of systems that consume  instances, but is "undetermined" for systems that generate instances.  Used only as part of specifications that define both initiator and consumer expectations.
         */
        RC, 
        /**
         * Description: The element is considered "required" (i.e. must be supported) from the perspective of systems that generate instances, but is "undetermined" for systems that consume instances.  Used only as part of specifications that define both initiator and consumer expectations.
         */
        RI, 
        /**
         * Description: The conformance expectations for this element have not yet been determined.
         */
        U, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7V3Conformance fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("I".equals(codeString))
          return I;
        if ("NP".equals(codeString))
          return NP;
        if ("R".equals(codeString))
          return R;
        if ("RC".equals(codeString))
          return RC;
        if ("RI".equals(codeString))
          return RI;
        if ("U".equals(codeString))
          return U;
        throw new FHIRException("Unknown V3Hl7V3Conformance code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case I: return "I";
            case NP: return "NP";
            case R: return "R";
            case RC: return "RC";
            case RI: return "RI";
            case U: return "U";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/hl7V3Conformance";
        }
        public String getDefinition() {
          switch (this) {
            case I: return "Description: Implementers receiving this property must not raise an error if the data is received, but will not perform any useful function with the data.  This conformance level is not used in profiles or other artifacts that are specific to the \"sender\" or \"initiator\" of a communication.";
            case NP: return "Description: All implementers are prohibited from transmitting this content, and may raise an error if they receive it.";
            case R: return "Description: All implementers must support this property.  I.e. they must be able to transmit, or to receive and usefully handle the concept.";
            case RC: return "Description: The element is considered \"required\" (i.e. must be supported) from the perspective of systems that consume  instances, but is \"undetermined\" for systems that generate instances.  Used only as part of specifications that define both initiator and consumer expectations.";
            case RI: return "Description: The element is considered \"required\" (i.e. must be supported) from the perspective of systems that generate instances, but is \"undetermined\" for systems that consume instances.  Used only as part of specifications that define both initiator and consumer expectations.";
            case U: return "Description: The conformance expectations for this element have not yet been determined.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case I: return "ignored";
            case NP: return "not permitted";
            case R: return "required";
            case RC: return "required for consumer";
            case RI: return "required for initiator";
            case U: return "undetermined";
            default: return "?";
          }
    }


}

