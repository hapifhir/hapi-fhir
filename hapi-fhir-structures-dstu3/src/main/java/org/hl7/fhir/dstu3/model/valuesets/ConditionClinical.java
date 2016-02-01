package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ConditionClinical {

        /**
         * The subject is currently experiencing the symptoms of the condition.
         */
        ACTIVE, 
        /**
         * The subject is re-experiencing the symptoms of the condition after a period of remission or presumed resolution.
         */
        RELAPSE, 
        /**
         * The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning.
         */
        REMISSION, 
        /**
         * The subject is no longer experiencing the symptoms of the condition and there is no perceived risk of the symptoms returning.
         */
        RESOLVED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionClinical fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("relapse".equals(codeString))
          return RELAPSE;
        if ("remission".equals(codeString))
          return REMISSION;
        if ("resolved".equals(codeString))
          return RESOLVED;
        throw new FHIRException("Unknown ConditionClinical code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case RELAPSE: return "relapse";
            case REMISSION: return "remission";
            case RESOLVED: return "resolved";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-clinical";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The subject is currently experiencing the symptoms of the condition.";
            case RELAPSE: return "The subject is re-experiencing the symptoms of the condition after a period of remission or presumed resolution.";
            case REMISSION: return "The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning.";
            case RESOLVED: return "The subject is no longer experiencing the symptoms of the condition and there is no perceived risk of the symptoms returning.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case RELAPSE: return "Relapse";
            case REMISSION: return "Remission";
            case RESOLVED: return "Resolved";
            default: return "?";
          }
    }


}

