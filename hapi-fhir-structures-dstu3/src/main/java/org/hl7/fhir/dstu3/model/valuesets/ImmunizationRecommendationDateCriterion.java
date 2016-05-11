package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ImmunizationRecommendationDateCriterion {

        /**
         * Date the next dose is considered due.
         */
        DUE, 
        /**
         * At the recommended date.
         */
        RECOMMENDED, 
        /**
         * As early as possible.
         */
        EARLIEST, 
        /**
         * Date the next dose is considered overdue.
         */
        OVERDUE, 
        /**
         * The latest date the next dose is to be given.
         */
        LATEST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ImmunizationRecommendationDateCriterion fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("due".equals(codeString))
          return DUE;
        if ("recommended".equals(codeString))
          return RECOMMENDED;
        if ("earliest".equals(codeString))
          return EARLIEST;
        if ("overdue".equals(codeString))
          return OVERDUE;
        if ("latest".equals(codeString))
          return LATEST;
        throw new FHIRException("Unknown ImmunizationRecommendationDateCriterion code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DUE: return "due";
            case RECOMMENDED: return "recommended";
            case EARLIEST: return "earliest";
            case OVERDUE: return "overdue";
            case LATEST: return "latest";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/immunization-recommendation-date-criterion";
        }
        public String getDefinition() {
          switch (this) {
            case DUE: return "Date the next dose is considered due.";
            case RECOMMENDED: return "At the recommended date.";
            case EARLIEST: return "As early as possible.";
            case OVERDUE: return "Date the next dose is considered overdue.";
            case LATEST: return "The latest date the next dose is to be given.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DUE: return "Due";
            case RECOMMENDED: return "Recommended";
            case EARLIEST: return "Earliest Date";
            case OVERDUE: return "Past Due Date";
            case LATEST: return "Latest";
            default: return "?";
          }
    }


}

