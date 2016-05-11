package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3PersonDisabilityType {

        /**
         * Vision impaired
         */
        _1, 
        /**
         * Hearing impaired
         */
        _2, 
        /**
         * Speech impaired
         */
        _3, 
        /**
         * Mentally impaired
         */
        _4, 
        /**
         * Mobility impaired
         */
        _5, 
        /**
         * A crib is required to move the person
         */
        CB, 
        /**
         * Person requires crutches to ambulate
         */
        CR, 
        /**
         * A gurney is required to move the person
         */
        G, 
        /**
         * Person requires a wheelchair to be ambulatory
         */
        WC, 
        /**
         * Person requires a walker to ambulate
         */
        WK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3PersonDisabilityType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("CB".equals(codeString))
          return CB;
        if ("CR".equals(codeString))
          return CR;
        if ("G".equals(codeString))
          return G;
        if ("WC".equals(codeString))
          return WC;
        if ("WK".equals(codeString))
          return WK;
        throw new FHIRException("Unknown V3PersonDisabilityType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case CB: return "CB";
            case CR: return "CR";
            case G: return "G";
            case WC: return "WC";
            case WK: return "WK";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/PersonDisabilityType";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Vision impaired";
            case _2: return "Hearing impaired";
            case _3: return "Speech impaired";
            case _4: return "Mentally impaired";
            case _5: return "Mobility impaired";
            case CB: return "A crib is required to move the person";
            case CR: return "Person requires crutches to ambulate";
            case G: return "A gurney is required to move the person";
            case WC: return "Person requires a wheelchair to be ambulatory";
            case WK: return "Person requires a walker to ambulate";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Vision impaired";
            case _2: return "Hearing impaired";
            case _3: return "Speech impaired";
            case _4: return "Mentally impaired";
            case _5: return "Mobility impaired";
            case CB: return "Requires crib";
            case CR: return "Requires crutches";
            case G: return "Requires gurney";
            case WC: return "Requires wheelchair";
            case WK: return "Requires walker";
            default: return "?";
          }
    }


}

