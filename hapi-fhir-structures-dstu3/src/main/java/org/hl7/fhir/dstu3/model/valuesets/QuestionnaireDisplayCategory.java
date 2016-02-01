package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum QuestionnaireDisplayCategory {

        /**
         * The text provides guidances on how to populate or use a portion of the questionnaire (or the questionnaire as a whole).
         */
        INSTRUCTIONS, 
        /**
         * The text provides guidance on how the information should be or will be handled from a security/confidentiality/access control perspective when completed
         */
        SECURITY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuestionnaireDisplayCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instructions".equals(codeString))
          return INSTRUCTIONS;
        if ("security".equals(codeString))
          return SECURITY;
        throw new FHIRException("Unknown QuestionnaireDisplayCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTRUCTIONS: return "instructions";
            case SECURITY: return "security";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/questionnaire-display-category";
        }
        public String getDefinition() {
          switch (this) {
            case INSTRUCTIONS: return "The text provides guidances on how to populate or use a portion of the questionnaire (or the questionnaire as a whole).";
            case SECURITY: return "The text provides guidance on how the information should be or will be handled from a security/confidentiality/access control perspective when completed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTRUCTIONS: return "Instructions";
            case SECURITY: return "Security";
            default: return "?";
          }
    }


}

