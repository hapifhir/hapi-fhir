package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EntityDeterminer {

        /**
         * Description:A determiner that specifies that the Entity object represents a particular physical thing (as opposed to a universal, kind, or class of physical thing).

                        
                           Discussion: It does not matter whether an INSTANCE still exists as a whole at the point in time (or process) when we mention it, for example, a drug product lot is an INSTANCE even though it has been portioned out for retail purpose.
         */
        INSTANCE, 
        /**
         * A determiner that specifies that the Entity object represents a particular collection of physical things (as opposed to a universal, kind, or class of physical thing).  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.
         */
        GROUP, 
        /**
         * Description:A determiner that specifies that the Entity object represents a universal, kind or class of physical thing (as opposed to a particular thing).
         */
        KIND, 
        /**
         * A determiner that specifies that the Entity object represents a universal, kind or class of collections physical things.  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.
         */
        GROUPKIND, 
        /**
         * The described quantified determiner indicates that the given Entity is taken as a general description of a specific amount of a thing. For example, QUANTIFIED_KIND of syringe (quantity = 3,) stands for exactly three syringes.
         */
        QUANTIFIEDKIND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityDeterminer fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("INSTANCE".equals(codeString))
          return INSTANCE;
        if ("GROUP".equals(codeString))
          return GROUP;
        if ("KIND".equals(codeString))
          return KIND;
        if ("GROUPKIND".equals(codeString))
          return GROUPKIND;
        if ("QUANTIFIED_KIND".equals(codeString))
          return QUANTIFIEDKIND;
        throw new FHIRException("Unknown V3EntityDeterminer code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "INSTANCE";
            case GROUP: return "GROUP";
            case KIND: return "KIND";
            case GROUPKIND: return "GROUPKIND";
            case QUANTIFIEDKIND: return "QUANTIFIED_KIND";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityDeterminer";
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "Description:A determiner that specifies that the Entity object represents a particular physical thing (as opposed to a universal, kind, or class of physical thing).\r\n\n                        \n                           Discussion: It does not matter whether an INSTANCE still exists as a whole at the point in time (or process) when we mention it, for example, a drug product lot is an INSTANCE even though it has been portioned out for retail purpose.";
            case GROUP: return "A determiner that specifies that the Entity object represents a particular collection of physical things (as opposed to a universal, kind, or class of physical thing).  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.";
            case KIND: return "Description:A determiner that specifies that the Entity object represents a universal, kind or class of physical thing (as opposed to a particular thing).";
            case GROUPKIND: return "A determiner that specifies that the Entity object represents a universal, kind or class of collections physical things.  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.";
            case QUANTIFIEDKIND: return "The described quantified determiner indicates that the given Entity is taken as a general description of a specific amount of a thing. For example, QUANTIFIED_KIND of syringe (quantity = 3,) stands for exactly three syringes.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "specific";
            case GROUP: return "specific group";
            case KIND: return "described";
            case GROUPKIND: return "described group";
            case QUANTIFIEDKIND: return "described quantified";
            default: return "?";
          }
    }


}

