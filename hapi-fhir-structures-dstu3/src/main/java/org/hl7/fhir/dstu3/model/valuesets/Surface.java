package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Surface {

        /**
         * The surface of a tooth that is closest to the midline (middle) of the face.
         */
        M, 
        /**
         * The chewing surface of posterior teeth.
         */
        O, 
        /**
         * The biting edge of anterior teeth.
         */
        I, 
        /**
         * The surface of a tooth that faces away from the midline of the face.
         */
        D, 
        /**
         * The surface of a posterior tooth facing the cheeks.
         */
        B, 
        /**
         * The surface of a tooth facing the lips.
         */
        V, 
        /**
         * The surface of a tooth facing the tongue.
         */
        L, 
        /**
         * The Mesioclusal surfaces of a tooth.
         */
        MO, 
        /**
         * The Distoclusal surfaces of a tooth.
         */
        DO, 
        /**
         * The Distoincisal surfaces of a tooth.
         */
        DI, 
        /**
         * The Mesioclusodistal surfaces of a tooth.
         */
        MOD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Surface fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("M".equals(codeString))
          return M;
        if ("O".equals(codeString))
          return O;
        if ("I".equals(codeString))
          return I;
        if ("D".equals(codeString))
          return D;
        if ("B".equals(codeString))
          return B;
        if ("V".equals(codeString))
          return V;
        if ("L".equals(codeString))
          return L;
        if ("MO".equals(codeString))
          return MO;
        if ("DO".equals(codeString))
          return DO;
        if ("DI".equals(codeString))
          return DI;
        if ("MOD".equals(codeString))
          return MOD;
        throw new FHIRException("Unknown Surface code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case M: return "M";
            case O: return "O";
            case I: return "I";
            case D: return "D";
            case B: return "B";
            case V: return "V";
            case L: return "L";
            case MO: return "MO";
            case DO: return "DO";
            case DI: return "DI";
            case MOD: return "MOD";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/FDI-surface";
        }
        public String getDefinition() {
          switch (this) {
            case M: return "The surface of a tooth that is closest to the midline (middle) of the face.";
            case O: return "The chewing surface of posterior teeth.";
            case I: return "The biting edge of anterior teeth.";
            case D: return "The surface of a tooth that faces away from the midline of the face.";
            case B: return "The surface of a posterior tooth facing the cheeks.";
            case V: return "The surface of a tooth facing the lips.";
            case L: return "The surface of a tooth facing the tongue.";
            case MO: return "The Mesioclusal surfaces of a tooth.";
            case DO: return "The Distoclusal surfaces of a tooth.";
            case DI: return "The Distoincisal surfaces of a tooth.";
            case MOD: return "The Mesioclusodistal surfaces of a tooth.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case M: return "Mesial";
            case O: return "Occlusal";
            case I: return "Incisal";
            case D: return "Distal";
            case B: return "Buccal";
            case V: return "Ventral";
            case L: return "Lingual";
            case MO: return "Mesioclusal";
            case DO: return "Distoclusal";
            case DI: return "Distoincisal";
            case MOD: return "Mesioclusodistal";
            default: return "?";
          }
    }


}

