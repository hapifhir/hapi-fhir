package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EntityClass {

        /**
         * Corresponds to the Entity class
         */
        ENT, 
        /**
         * A health chart included to serve as a document receiving entity in the management of medical records.
         */
        HCE, 
        /**
         * Anything that essentially has the property of life, independent of current state (a dead human corpse is still essentially a living subject).
         */
        LIV, 
        /**
         * A subtype of living subject that includes all living things except the species Homo Sapiens.
         */
        NLIV, 
        /**
         * A living subject from the animal kingdom.
         */
        ANM, 
        /**
         * All single celled living organisms including protozoa, bacteria, yeast, viruses, etc.
         */
        MIC, 
        /**
         * A living subject from the order of plants.
         */
        PLNT, 
        /**
         * A living subject of the species homo sapiens.
         */
        PSN, 
        /**
         * Any thing that has extension in space and mass, may be of living or non-living origin.
         */
        MAT, 
        /**
         * A substance that is fully defined by an organic or inorganic chemical formula, includes mixtures of other chemical substances. Refine using, e.g., IUPAC codes.
         */
        CHEM, 
        /**
         * Naturally occurring, processed or manufactured entities that are primarily used as food for humans and animals.
         */
        FOOD, 
        /**
         * Corresponds to the ManufacturedMaterial class
         */
        MMAT, 
        /**
         * A container of other entities.
         */
        CONT, 
        /**
         * A type of container that can hold other containers or other holders.
         */
        HOLD, 
        /**
         * A subtype of ManufacturedMaterial used in an activity, without being substantially changed through that activity.  The kind of device is identified by the code attribute inherited from Entity.

                        
                           Usage: This includes durable (reusable) medical equipment as well as disposable equipment.
         */
        DEV, 
        /**
         * A physical artifact that stores information about the granting of authorization.
         */
        CER, 
        /**
         * Class to contain unique attributes of diagnostic imaging equipment.
         */
        MODDV, 
        /**
         * A social or legal structure formed by human beings.
         */
        ORG, 
        /**
         * An agency of the people of a state often assuming some authority over a certain matter. Includes government, governmental agencies, associations.
         */
        PUB, 
        /**
         * A politically organized body of people bonded by territory, culture, or ethnicity, having sovereignty (to a certain extent) granted by other states (enclosing or neighboring states). This includes countries (nations), provinces (e.g., one of the United States of America or a French departement), counties or municipalities. Refine using, e.g., ISO country codes, FIPS-PUB state codes, etc.
         */
        STATE, 
        /**
         * A politically organized body of people bonded by territory and known as a nation.
         */
        NAT, 
        /**
         * A physical place or site with its containing structure. May be natural or man-made. The geographic position of a place may or may not be constant.
         */
        PLC, 
        /**
         * The territory of a city, town or other municipality.
         */
        CITY, 
        /**
         * The territory of a sovereign nation.
         */
        COUNTRY, 
        /**
         * The territory of a county, parish or other division of a state or province.
         */
        COUNTY, 
        /**
         * The territory of a state, province, department or other division of a sovereign country.
         */
        PROVINCE, 
        /**
         * A grouping of resources (personnel, material, or places) to be used for scheduling purposes.  May be a pool of like-type resources, a team, or combination of personnel, material and places.
         */
        RGRP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityClass fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ENT".equals(codeString))
          return ENT;
        if ("HCE".equals(codeString))
          return HCE;
        if ("LIV".equals(codeString))
          return LIV;
        if ("NLIV".equals(codeString))
          return NLIV;
        if ("ANM".equals(codeString))
          return ANM;
        if ("MIC".equals(codeString))
          return MIC;
        if ("PLNT".equals(codeString))
          return PLNT;
        if ("PSN".equals(codeString))
          return PSN;
        if ("MAT".equals(codeString))
          return MAT;
        if ("CHEM".equals(codeString))
          return CHEM;
        if ("FOOD".equals(codeString))
          return FOOD;
        if ("MMAT".equals(codeString))
          return MMAT;
        if ("CONT".equals(codeString))
          return CONT;
        if ("HOLD".equals(codeString))
          return HOLD;
        if ("DEV".equals(codeString))
          return DEV;
        if ("CER".equals(codeString))
          return CER;
        if ("MODDV".equals(codeString))
          return MODDV;
        if ("ORG".equals(codeString))
          return ORG;
        if ("PUB".equals(codeString))
          return PUB;
        if ("STATE".equals(codeString))
          return STATE;
        if ("NAT".equals(codeString))
          return NAT;
        if ("PLC".equals(codeString))
          return PLC;
        if ("CITY".equals(codeString))
          return CITY;
        if ("COUNTRY".equals(codeString))
          return COUNTRY;
        if ("COUNTY".equals(codeString))
          return COUNTY;
        if ("PROVINCE".equals(codeString))
          return PROVINCE;
        if ("RGRP".equals(codeString))
          return RGRP;
        throw new FHIRException("Unknown V3EntityClass code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ENT: return "ENT";
            case HCE: return "HCE";
            case LIV: return "LIV";
            case NLIV: return "NLIV";
            case ANM: return "ANM";
            case MIC: return "MIC";
            case PLNT: return "PLNT";
            case PSN: return "PSN";
            case MAT: return "MAT";
            case CHEM: return "CHEM";
            case FOOD: return "FOOD";
            case MMAT: return "MMAT";
            case CONT: return "CONT";
            case HOLD: return "HOLD";
            case DEV: return "DEV";
            case CER: return "CER";
            case MODDV: return "MODDV";
            case ORG: return "ORG";
            case PUB: return "PUB";
            case STATE: return "STATE";
            case NAT: return "NAT";
            case PLC: return "PLC";
            case CITY: return "CITY";
            case COUNTRY: return "COUNTRY";
            case COUNTY: return "COUNTY";
            case PROVINCE: return "PROVINCE";
            case RGRP: return "RGRP";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityClass";
        }
        public String getDefinition() {
          switch (this) {
            case ENT: return "Corresponds to the Entity class";
            case HCE: return "A health chart included to serve as a document receiving entity in the management of medical records.";
            case LIV: return "Anything that essentially has the property of life, independent of current state (a dead human corpse is still essentially a living subject).";
            case NLIV: return "A subtype of living subject that includes all living things except the species Homo Sapiens.";
            case ANM: return "A living subject from the animal kingdom.";
            case MIC: return "All single celled living organisms including protozoa, bacteria, yeast, viruses, etc.";
            case PLNT: return "A living subject from the order of plants.";
            case PSN: return "A living subject of the species homo sapiens.";
            case MAT: return "Any thing that has extension in space and mass, may be of living or non-living origin.";
            case CHEM: return "A substance that is fully defined by an organic or inorganic chemical formula, includes mixtures of other chemical substances. Refine using, e.g., IUPAC codes.";
            case FOOD: return "Naturally occurring, processed or manufactured entities that are primarily used as food for humans and animals.";
            case MMAT: return "Corresponds to the ManufacturedMaterial class";
            case CONT: return "A container of other entities.";
            case HOLD: return "A type of container that can hold other containers or other holders.";
            case DEV: return "A subtype of ManufacturedMaterial used in an activity, without being substantially changed through that activity.  The kind of device is identified by the code attribute inherited from Entity.\r\n\n                        \n                           Usage: This includes durable (reusable) medical equipment as well as disposable equipment.";
            case CER: return "A physical artifact that stores information about the granting of authorization.";
            case MODDV: return "Class to contain unique attributes of diagnostic imaging equipment.";
            case ORG: return "A social or legal structure formed by human beings.";
            case PUB: return "An agency of the people of a state often assuming some authority over a certain matter. Includes government, governmental agencies, associations.";
            case STATE: return "A politically organized body of people bonded by territory, culture, or ethnicity, having sovereignty (to a certain extent) granted by other states (enclosing or neighboring states). This includes countries (nations), provinces (e.g., one of the United States of America or a French departement), counties or municipalities. Refine using, e.g., ISO country codes, FIPS-PUB state codes, etc.";
            case NAT: return "A politically organized body of people bonded by territory and known as a nation.";
            case PLC: return "A physical place or site with its containing structure. May be natural or man-made. The geographic position of a place may or may not be constant.";
            case CITY: return "The territory of a city, town or other municipality.";
            case COUNTRY: return "The territory of a sovereign nation.";
            case COUNTY: return "The territory of a county, parish or other division of a state or province.";
            case PROVINCE: return "The territory of a state, province, department or other division of a sovereign country.";
            case RGRP: return "A grouping of resources (personnel, material, or places) to be used for scheduling purposes.  May be a pool of like-type resources, a team, or combination of personnel, material and places.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ENT: return "entity";
            case HCE: return "health chart entity";
            case LIV: return "living subject";
            case NLIV: return "non-person living subject";
            case ANM: return "animal";
            case MIC: return "microorganism";
            case PLNT: return "plant";
            case PSN: return "person";
            case MAT: return "material";
            case CHEM: return "chemical substance";
            case FOOD: return "food";
            case MMAT: return "manufactured material";
            case CONT: return "container";
            case HOLD: return "holder";
            case DEV: return "device";
            case CER: return "certificate representation";
            case MODDV: return "imaging modality";
            case ORG: return "organization";
            case PUB: return "public institution";
            case STATE: return "state";
            case NAT: return "Nation";
            case PLC: return "place";
            case CITY: return "city or town";
            case COUNTRY: return "country";
            case COUNTY: return "county or parish";
            case PROVINCE: return "state or province";
            case RGRP: return "group";
            default: return "?";
          }
    }


}

