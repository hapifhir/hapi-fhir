package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ExposureMode {

        /**
         * Code for the mechanism by which the exposure agent was exchanged or potentially exchanged by the participants involved in the exposure.
         */
        _EXPOSUREMODE, 
        /**
         * Description: Communication of an agent from a living subject or environmental source to a living subject through indirect contact via oral or nasal inhalation.
         */
        AIRBORNE, 
        /**
         * Description: Communication of an agent from a living subject or environmental source to a living subject through direct physical contact.
         */
        CONTACT, 
        /**
         * Description: Communication of an agent from a food source to a living subject via oral consumption.
         */
        FOODBORNE, 
        /**
         * Description: Communication of an agent to a living subject by contact and/or consumption via an aqueous medium
         */
        WATERBORNE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ExposureMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ExposureMode".equals(codeString))
          return _EXPOSUREMODE;
        if ("AIRBORNE".equals(codeString))
          return AIRBORNE;
        if ("CONTACT".equals(codeString))
          return CONTACT;
        if ("FOODBORNE".equals(codeString))
          return FOODBORNE;
        if ("WATERBORNE".equals(codeString))
          return WATERBORNE;
        throw new FHIRException("Unknown V3ExposureMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _EXPOSUREMODE: return "_ExposureMode";
            case AIRBORNE: return "AIRBORNE";
            case CONTACT: return "CONTACT";
            case FOODBORNE: return "FOODBORNE";
            case WATERBORNE: return "WATERBORNE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ExposureMode";
        }
        public String getDefinition() {
          switch (this) {
            case _EXPOSUREMODE: return "Code for the mechanism by which the exposure agent was exchanged or potentially exchanged by the participants involved in the exposure.";
            case AIRBORNE: return "Description: Communication of an agent from a living subject or environmental source to a living subject through indirect contact via oral or nasal inhalation.";
            case CONTACT: return "Description: Communication of an agent from a living subject or environmental source to a living subject through direct physical contact.";
            case FOODBORNE: return "Description: Communication of an agent from a food source to a living subject via oral consumption.";
            case WATERBORNE: return "Description: Communication of an agent to a living subject by contact and/or consumption via an aqueous medium";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _EXPOSUREMODE: return "ExposureMode";
            case AIRBORNE: return "airborne";
            case CONTACT: return "contact";
            case FOODBORNE: return "foodborne";
            case WATERBORNE: return "waterborne";
            default: return "?";
          }
    }


}

