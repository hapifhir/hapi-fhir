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

public enum LocationPhysicalType {

        /**
         * A collection of buildings or other locations such as a site or a campus.
         */
        SI, 
        /**
         * Any Building or structure. This may contain rooms, corridors, wings, etc. It may not have walls, or a roof, but is considered a defined/allocated space.
         */
        BU, 
        /**
         * A Wing within a Building, this often contains levels, rooms and corridors.
         */
        WI, 
        /**
         * A Ward is a section of a medical facility that may contain rooms and other types of location.
         */
        WA, 
        /**
         * A Level in a multi-level Building/Structure.
         */
        LVL, 
        /**
         * Any corridor within a Building, that may connect rooms.
         */
        CO, 
        /**
         * A space that is allocated as a room, it may have walls/roof etc., but does not require these.
         */
        RO, 
        /**
         * A space that is allocated for sleeping/laying on. This is not the physical bed/trolley that may be moved about, but the space it may occupy.
         */
        BD, 
        /**
         * A means of transportation.
         */
        VE, 
        /**
         * A residential dwelling. Usually used to reference a location that a person/patient may reside.
         */
        HO, 
        /**
         * A container that can store goods, equipment, medications or other items.
         */
        CA, 
        /**
         * A defined path to travel between 2 points that has a known name.
         */
        RD, 
        /**
         * A defined physical boundary of something, such as a flood risk zone, region, postcode
         */
        AREA, 
        /**
         * A wide scope that covers a conceptual domain, such as a Nation (Country wide community or Federal Government - e.g. Ministry of Health),  Province or State (community or Government), Business (throughout the enterprise), Nation with a business scope of an agency (e.g. CDC, FDA etc.) or a Business segment (UK Pharmacy), not just an physical boundry
         */
        JDN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LocationPhysicalType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("si".equals(codeString))
          return SI;
        if ("bu".equals(codeString))
          return BU;
        if ("wi".equals(codeString))
          return WI;
        if ("wa".equals(codeString))
          return WA;
        if ("lvl".equals(codeString))
          return LVL;
        if ("co".equals(codeString))
          return CO;
        if ("ro".equals(codeString))
          return RO;
        if ("bd".equals(codeString))
          return BD;
        if ("ve".equals(codeString))
          return VE;
        if ("ho".equals(codeString))
          return HO;
        if ("ca".equals(codeString))
          return CA;
        if ("rd".equals(codeString))
          return RD;
        if ("area".equals(codeString))
          return AREA;
        if ("jdn".equals(codeString))
          return JDN;
        throw new FHIRException("Unknown LocationPhysicalType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SI: return "si";
            case BU: return "bu";
            case WI: return "wi";
            case WA: return "wa";
            case LVL: return "lvl";
            case CO: return "co";
            case RO: return "ro";
            case BD: return "bd";
            case VE: return "ve";
            case HO: return "ho";
            case CA: return "ca";
            case RD: return "rd";
            case AREA: return "area";
            case JDN: return "jdn";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/location-physical-type";
        }
        public String getDefinition() {
          switch (this) {
            case SI: return "A collection of buildings or other locations such as a site or a campus.";
            case BU: return "Any Building or structure. This may contain rooms, corridors, wings, etc. It may not have walls, or a roof, but is considered a defined/allocated space.";
            case WI: return "A Wing within a Building, this often contains levels, rooms and corridors.";
            case WA: return "A Ward is a section of a medical facility that may contain rooms and other types of location.";
            case LVL: return "A Level in a multi-level Building/Structure.";
            case CO: return "Any corridor within a Building, that may connect rooms.";
            case RO: return "A space that is allocated as a room, it may have walls/roof etc., but does not require these.";
            case BD: return "A space that is allocated for sleeping/laying on. This is not the physical bed/trolley that may be moved about, but the space it may occupy.";
            case VE: return "A means of transportation.";
            case HO: return "A residential dwelling. Usually used to reference a location that a person/patient may reside.";
            case CA: return "A container that can store goods, equipment, medications or other items.";
            case RD: return "A defined path to travel between 2 points that has a known name.";
            case AREA: return "A defined physical boundary of something, such as a flood risk zone, region, postcode";
            case JDN: return "A wide scope that covers a conceptual domain, such as a Nation (Country wide community or Federal Government - e.g. Ministry of Health),  Province or State (community or Government), Business (throughout the enterprise), Nation with a business scope of an agency (e.g. CDC, FDA etc.) or a Business segment (UK Pharmacy), not just an physical boundry";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SI: return "Site";
            case BU: return "Building";
            case WI: return "Wing";
            case WA: return "Ward";
            case LVL: return "Level";
            case CO: return "Corridor";
            case RO: return "Room";
            case BD: return "Bed";
            case VE: return "Vehicle";
            case HO: return "House";
            case CA: return "Cabinet";
            case RD: return "Road";
            case AREA: return "Area";
            case JDN: return "Jurisdiction";
            default: return "?";
          }
    }


}

