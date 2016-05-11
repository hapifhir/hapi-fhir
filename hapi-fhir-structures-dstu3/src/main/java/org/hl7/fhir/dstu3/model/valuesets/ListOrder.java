package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ListOrder {

        /**
         * The list was sorted by a user. The criteria the user used are not specified.
         */
        USER, 
        /**
         * The list was sorted by the system. The criteria the user used are not specified; define additional codes to specify a particular order (or use other defined codes).
         */
        SYSTEM, 
        /**
         * The list is sorted by the data of the event. This can be used when the list has items which are dates with past or future events.
         */
        EVENTDATE, 
        /**
         * The list is sorted by the date the item was added to the list. Note that the date added to the list is not explicit in the list itself.
         */
        ENTRYDATE, 
        /**
         * The list is sorted by priority. The exact method in which priority has been determined is not specified.
         */
        PRIORITY, 
        /**
         * The list is sorted alphabetically by an unspecified property of the items in the list.
         */
        ALPHABETIC, 
        /**
         * The list is sorted categorically by an unspecified property of the items in the list.
         */
        CATEGORY, 
        /**
         * The list is sorted by patient, with items for each patient grouped together.
         */
        PATIENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListOrder fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("user".equals(codeString))
          return USER;
        if ("system".equals(codeString))
          return SYSTEM;
        if ("event-date".equals(codeString))
          return EVENTDATE;
        if ("entry-date".equals(codeString))
          return ENTRYDATE;
        if ("priority".equals(codeString))
          return PRIORITY;
        if ("alphabetic".equals(codeString))
          return ALPHABETIC;
        if ("category".equals(codeString))
          return CATEGORY;
        if ("patient".equals(codeString))
          return PATIENT;
        throw new FHIRException("Unknown ListOrder code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case USER: return "user";
            case SYSTEM: return "system";
            case EVENTDATE: return "event-date";
            case ENTRYDATE: return "entry-date";
            case PRIORITY: return "priority";
            case ALPHABETIC: return "alphabetic";
            case CATEGORY: return "category";
            case PATIENT: return "patient";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/list-order";
        }
        public String getDefinition() {
          switch (this) {
            case USER: return "The list was sorted by a user. The criteria the user used are not specified.";
            case SYSTEM: return "The list was sorted by the system. The criteria the user used are not specified; define additional codes to specify a particular order (or use other defined codes).";
            case EVENTDATE: return "The list is sorted by the data of the event. This can be used when the list has items which are dates with past or future events.";
            case ENTRYDATE: return "The list is sorted by the date the item was added to the list. Note that the date added to the list is not explicit in the list itself.";
            case PRIORITY: return "The list is sorted by priority. The exact method in which priority has been determined is not specified.";
            case ALPHABETIC: return "The list is sorted alphabetically by an unspecified property of the items in the list.";
            case CATEGORY: return "The list is sorted categorically by an unspecified property of the items in the list.";
            case PATIENT: return "The list is sorted by patient, with items for each patient grouped together.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case USER: return "Sorted by User";
            case SYSTEM: return "Sorted by System";
            case EVENTDATE: return "Sorted by Event Date";
            case ENTRYDATE: return "Sorted by Item Date";
            case PRIORITY: return "Sorted by Priority";
            case ALPHABETIC: return "Sorted Alphabetically";
            case CATEGORY: return "Sorted by Category";
            case PATIENT: return "Sorted by Patient";
            default: return "?";
          }
    }


}

