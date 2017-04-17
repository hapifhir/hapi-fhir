package org.hl7.fhir.dstu3.model.codesystems;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum V3AddressPartType {

        /**
         * This can be a unit designator, such as apartment number, suite number, or floor. There may be several unit designators in an address (e.g., "3rd floor, Appt. 342"). This can also be a designator pointing away from the location, rather than specifying a smaller location within some larger one (e.g., Dutch "t.o." means "opposite to" for house boats located across the street facing houses).
         */
        ADL, 
        /**
         * Description: An address line is for either an additional locator, a delivery address or a street address.
         */
        AL, 
        /**
         * A delivery address line is frequently used instead of breaking out delivery mode, delivery installation, etc.  An address generally has only a delivery address line or a street address line, but not both.
         */
        DAL, 
        /**
         * Description: A street address line is frequently used instead of breaking out build number, street name, street type, etc. An address generally has only a delivery address line or a street address line, but not both.
         */
        SAL, 
        /**
         * The numeric portion of a building number
         */
        BNN, 
        /**
         * The number of a building, house or lot alongside the street.  Also known as "primary street number".  This does not number the street but rather the building.
         */
        BNR, 
        /**
         * Any alphabetic character, fraction or other text that may appear after the numeric portion of a building number
         */
        BNS, 
        /**
         * The name of the party who will take receipt at the specified address, and will take on responsibility for ensuring delivery to the target recipient
         */
        CAR, 
        /**
         * A geographic sub-unit delineated for demographic purposes.
         */
        CEN, 
        /**
         * Country
         */
        CNT, 
        /**
         * A sub-unit of a state or province. (49 of the United States of America use the term "county;" Louisiana uses the term "parish".)
         */
        CPA, 
        /**
         * The name of the city, town, village, or other community or delivery center
         */
        CTY, 
        /**
         * Delimiters are printed without framing white space.  If no value component is provided, the delimiter appears as a line break.
         */
        DEL, 
        /**
         * Indicates the type of delivery installation (the facility to which the mail will be delivered prior to final shipping via the delivery mode.) Example: post office, letter carrier depot, community mail center, station, etc.
         */
        DINST, 
        /**
         * The location of the delivery installation, usually a town or city, and is only required if the area is different from the municipality. Area to which mail delivery service is provided from any postal facility or service such as an individual letter carrier, rural route, or postal route.
         */
        DINSTA, 
        /**
         * A number, letter or name identifying a delivery installation.  E.g., for Station A, the delivery installation qualifier would be 'A'.
         */
        DINSTQ, 
        /**
         * Direction (e.g., N, S, W, E)
         */
        DIR, 
        /**
         * Indicates the type of service offered, method of delivery.  For example: post office box, rural route, general delivery, etc.
         */
        DMOD, 
        /**
         * Represents the routing information such as a letter carrier route number.  It is the identifying number of the designator (the box number or rural route number).
         */
        DMODID, 
        /**
         * A value that uniquely identifies the postal address.
         */
        DPID, 
        /**
         * Description:An intersection denotes that the actual address is located AT or CLOSE TO the intersection OF two or more streets.
         */
        INT, 
        /**
         * A numbered box located in a post station.
         */
        POB, 
        /**
         * A subsection of a municipality
         */
        PRE, 
        /**
         * A sub-unit of a country with limited sovereignty in a federally organized country.
         */
        STA, 
        /**
         * The base name of a roadway or artery recognized by a municipality (excluding street type and direction)
         */
        STB, 
        /**
         * street name
         */
        STR, 
        /**
         * The designation given to the street.  (e.g. Street, Avenue, Crescent, etc.)
         */
        STTYP, 
        /**
         * The number or name of a specific unit contained within a building or complex, as assigned by that building or complex.
         */
        UNID, 
        /**
         * Indicates the type of specific unit contained within a building or complex.  E.g. Appartment, Floor
         */
        UNIT, 
        /**
         * A postal code designating a region defined by the postal service.
         */
        ZIP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AddressPartType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ADL".equals(codeString))
          return ADL;
        if ("AL".equals(codeString))
          return AL;
        if ("DAL".equals(codeString))
          return DAL;
        if ("SAL".equals(codeString))
          return SAL;
        if ("BNN".equals(codeString))
          return BNN;
        if ("BNR".equals(codeString))
          return BNR;
        if ("BNS".equals(codeString))
          return BNS;
        if ("CAR".equals(codeString))
          return CAR;
        if ("CEN".equals(codeString))
          return CEN;
        if ("CNT".equals(codeString))
          return CNT;
        if ("CPA".equals(codeString))
          return CPA;
        if ("CTY".equals(codeString))
          return CTY;
        if ("DEL".equals(codeString))
          return DEL;
        if ("DINST".equals(codeString))
          return DINST;
        if ("DINSTA".equals(codeString))
          return DINSTA;
        if ("DINSTQ".equals(codeString))
          return DINSTQ;
        if ("DIR".equals(codeString))
          return DIR;
        if ("DMOD".equals(codeString))
          return DMOD;
        if ("DMODID".equals(codeString))
          return DMODID;
        if ("DPID".equals(codeString))
          return DPID;
        if ("INT".equals(codeString))
          return INT;
        if ("POB".equals(codeString))
          return POB;
        if ("PRE".equals(codeString))
          return PRE;
        if ("STA".equals(codeString))
          return STA;
        if ("STB".equals(codeString))
          return STB;
        if ("STR".equals(codeString))
          return STR;
        if ("STTYP".equals(codeString))
          return STTYP;
        if ("UNID".equals(codeString))
          return UNID;
        if ("UNIT".equals(codeString))
          return UNIT;
        if ("ZIP".equals(codeString))
          return ZIP;
        throw new FHIRException("Unknown V3AddressPartType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADL: return "ADL";
            case AL: return "AL";
            case DAL: return "DAL";
            case SAL: return "SAL";
            case BNN: return "BNN";
            case BNR: return "BNR";
            case BNS: return "BNS";
            case CAR: return "CAR";
            case CEN: return "CEN";
            case CNT: return "CNT";
            case CPA: return "CPA";
            case CTY: return "CTY";
            case DEL: return "DEL";
            case DINST: return "DINST";
            case DINSTA: return "DINSTA";
            case DINSTQ: return "DINSTQ";
            case DIR: return "DIR";
            case DMOD: return "DMOD";
            case DMODID: return "DMODID";
            case DPID: return "DPID";
            case INT: return "INT";
            case POB: return "POB";
            case PRE: return "PRE";
            case STA: return "STA";
            case STB: return "STB";
            case STR: return "STR";
            case STTYP: return "STTYP";
            case UNID: return "UNID";
            case UNIT: return "UNIT";
            case ZIP: return "ZIP";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AddressPartType";
        }
        public String getDefinition() {
          switch (this) {
            case ADL: return "This can be a unit designator, such as apartment number, suite number, or floor. There may be several unit designators in an address (e.g., \"3rd floor, Appt. 342\"). This can also be a designator pointing away from the location, rather than specifying a smaller location within some larger one (e.g., Dutch \"t.o.\" means \"opposite to\" for house boats located across the street facing houses).";
            case AL: return "Description: An address line is for either an additional locator, a delivery address or a street address.";
            case DAL: return "A delivery address line is frequently used instead of breaking out delivery mode, delivery installation, etc.  An address generally has only a delivery address line or a street address line, but not both.";
            case SAL: return "Description: A street address line is frequently used instead of breaking out build number, street name, street type, etc. An address generally has only a delivery address line or a street address line, but not both.";
            case BNN: return "The numeric portion of a building number";
            case BNR: return "The number of a building, house or lot alongside the street.  Also known as \"primary street number\".  This does not number the street but rather the building.";
            case BNS: return "Any alphabetic character, fraction or other text that may appear after the numeric portion of a building number";
            case CAR: return "The name of the party who will take receipt at the specified address, and will take on responsibility for ensuring delivery to the target recipient";
            case CEN: return "A geographic sub-unit delineated for demographic purposes.";
            case CNT: return "Country";
            case CPA: return "A sub-unit of a state or province. (49 of the United States of America use the term \"county;\" Louisiana uses the term \"parish\".)";
            case CTY: return "The name of the city, town, village, or other community or delivery center";
            case DEL: return "Delimiters are printed without framing white space.  If no value component is provided, the delimiter appears as a line break.";
            case DINST: return "Indicates the type of delivery installation (the facility to which the mail will be delivered prior to final shipping via the delivery mode.) Example: post office, letter carrier depot, community mail center, station, etc.";
            case DINSTA: return "The location of the delivery installation, usually a town or city, and is only required if the area is different from the municipality. Area to which mail delivery service is provided from any postal facility or service such as an individual letter carrier, rural route, or postal route.";
            case DINSTQ: return "A number, letter or name identifying a delivery installation.  E.g., for Station A, the delivery installation qualifier would be 'A'.";
            case DIR: return "Direction (e.g., N, S, W, E)";
            case DMOD: return "Indicates the type of service offered, method of delivery.  For example: post office box, rural route, general delivery, etc.";
            case DMODID: return "Represents the routing information such as a letter carrier route number.  It is the identifying number of the designator (the box number or rural route number).";
            case DPID: return "A value that uniquely identifies the postal address.";
            case INT: return "Description:An intersection denotes that the actual address is located AT or CLOSE TO the intersection OF two or more streets.";
            case POB: return "A numbered box located in a post station.";
            case PRE: return "A subsection of a municipality";
            case STA: return "A sub-unit of a country with limited sovereignty in a federally organized country.";
            case STB: return "The base name of a roadway or artery recognized by a municipality (excluding street type and direction)";
            case STR: return "street name";
            case STTYP: return "The designation given to the street.  (e.g. Street, Avenue, Crescent, etc.)";
            case UNID: return "The number or name of a specific unit contained within a building or complex, as assigned by that building or complex.";
            case UNIT: return "Indicates the type of specific unit contained within a building or complex.  E.g. Appartment, Floor";
            case ZIP: return "A postal code designating a region defined by the postal service.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADL: return "additional locator";
            case AL: return "address line";
            case DAL: return "delivery address line";
            case SAL: return "street address line";
            case BNN: return "building number numeric";
            case BNR: return "building number";
            case BNS: return "building number suffix";
            case CAR: return "care of";
            case CEN: return "census tract";
            case CNT: return "country";
            case CPA: return "county or parish";
            case CTY: return "municipality";
            case DEL: return "delimiter";
            case DINST: return "delivery installation type";
            case DINSTA: return "delivery installation area";
            case DINSTQ: return "delivery installation qualifier";
            case DIR: return "direction";
            case DMOD: return "delivery mode";
            case DMODID: return "delivery mode identifier";
            case DPID: return "delivery point identifier";
            case INT: return "intersection";
            case POB: return "post box";
            case PRE: return "precinct";
            case STA: return "state or province";
            case STB: return "street name base";
            case STR: return "street name";
            case STTYP: return "street type";
            case UNID: return "unit identifier";
            case UNIT: return "unit designator";
            case ZIP: return "postal code";
            default: return "?";
          }
    }


}

