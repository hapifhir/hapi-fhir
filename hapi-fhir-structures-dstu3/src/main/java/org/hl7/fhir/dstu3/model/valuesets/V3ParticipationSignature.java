package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ParticipationSignature {

        /**
         * The particpant intends to provide a signature.
         */
        I, 
        /**
         * Signature has been affixed, either written on file, or electronic (incl. digital) signature in Participation.signatureText.
         */
        S, 
        /**
         * A signature for the service is required of this actor.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ParticipationSignature fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("I".equals(codeString))
          return I;
        if ("S".equals(codeString))
          return S;
        if ("X".equals(codeString))
          return X;
        throw new FHIRException("Unknown V3ParticipationSignature code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case I: return "I";
            case S: return "S";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ParticipationSignature";
        }
        public String getDefinition() {
          switch (this) {
            case I: return "The particpant intends to provide a signature.";
            case S: return "Signature has been affixed, either written on file, or electronic (incl. digital) signature in Participation.signatureText.";
            case X: return "A signature for the service is required of this actor.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case I: return "intended";
            case S: return "signed";
            case X: return "required";
            default: return "?";
          }
    }


}

