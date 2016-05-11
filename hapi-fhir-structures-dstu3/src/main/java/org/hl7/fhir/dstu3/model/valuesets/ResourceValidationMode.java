package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ResourceValidationMode {

        /**
         * The server checks the content, and then checks that the content would be acceptable as a create (e.g. that the content would not violate any uniqueness constraints).
         */
        CREATE, 
        /**
         * The server checks the content, and then checks that it would accept it as an update against the nominated specific resource (e.g. that there are no changes to immutable fields the server does not allow to change, and checking version integrity if appropriate).
         */
        UPDATE, 
        /**
         * The server ignores the content, and checks that the nominated resource is allowed to be deleted (e.g. checking referential integrity rules).
         */
        DELETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceValidationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("update".equals(codeString))
          return UPDATE;
        if ("delete".equals(codeString))
          return DELETE;
        throw new FHIRException("Unknown ResourceValidationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case UPDATE: return "update";
            case DELETE: return "delete";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/resource-validation-mode";
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "The server checks the content, and then checks that the content would be acceptable as a create (e.g. that the content would not violate any uniqueness constraints).";
            case UPDATE: return "The server checks the content, and then checks that it would accept it as an update against the nominated specific resource (e.g. that there are no changes to immutable fields the server does not allow to change, and checking version integrity if appropriate).";
            case DELETE: return "The server ignores the content, and checks that the nominated resource is allowed to be deleted (e.g. checking referential integrity rules).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "Validate for Create";
            case UPDATE: return "Validate for Update";
            case DELETE: return "Validate for Delete";
            default: return "?";
          }
    }


}

