package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3Ethnicity {

        /**
         * Hispanic or Latino
         */
        _21352, 
        /**
         * Spaniard
         */
        _21378, 
        /**
         * Andalusian
         */
        _21386, 
        /**
         * Asturian
         */
        _21394, 
        /**
         * Castillian
         */
        _21402, 
        /**
         * Catalonian
         */
        _21410, 
        /**
         * Belearic Islander
         */
        _21428, 
        /**
         * Gallego
         */
        _21436, 
        /**
         * Valencian
         */
        _21444, 
        /**
         * Canarian
         */
        _21451, 
        /**
         * Spanish Basque
         */
        _21469, 
        /**
         * Mexican
         */
        _21485, 
        /**
         * Mexican American
         */
        _21493, 
        /**
         * Mexicano
         */
        _21501, 
        /**
         * Chicano
         */
        _21519, 
        /**
         * La Raza
         */
        _21527, 
        /**
         * Mexican American Indian
         */
        _21535, 
        /**
         * Central American
         */
        _21550, 
        /**
         * Costa Rican
         */
        _21568, 
        /**
         * Guatemalan
         */
        _21576, 
        /**
         * Honduran
         */
        _21584, 
        /**
         * Nicaraguan
         */
        _21592, 
        /**
         * Panamanian
         */
        _21600, 
        /**
         * Salvadoran
         */
        _21618, 
        /**
         * Central American Indian
         */
        _21626, 
        /**
         * Canal Zone
         */
        _21634, 
        /**
         * South American
         */
        _21659, 
        /**
         * Argentinean
         */
        _21667, 
        /**
         * Bolivian
         */
        _21675, 
        /**
         * Chilean
         */
        _21683, 
        /**
         * Colombian
         */
        _21691, 
        /**
         * Ecuadorian
         */
        _21709, 
        /**
         * Paraguayan
         */
        _21717, 
        /**
         * Peruvian
         */
        _21725, 
        /**
         * Uruguayan
         */
        _21733, 
        /**
         * Venezuelan
         */
        _21741, 
        /**
         * South American Indian
         */
        _21758, 
        /**
         * Criollo
         */
        _21766, 
        /**
         * Latin American
         */
        _21782, 
        /**
         * Puerto Rican
         */
        _21808, 
        /**
         * Cuban
         */
        _21824, 
        /**
         * Dominican
         */
        _21840, 
        /**
         * Note that this term remains in the table for completeness, even though within HL7, the notion of "not otherwise coded" term is deprecated.
         */
        _21865, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Ethnicity fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("2135-2".equals(codeString))
          return _21352;
        if ("2137-8".equals(codeString))
          return _21378;
        if ("2138-6".equals(codeString))
          return _21386;
        if ("2139-4".equals(codeString))
          return _21394;
        if ("2140-2".equals(codeString))
          return _21402;
        if ("2141-0".equals(codeString))
          return _21410;
        if ("2142-8".equals(codeString))
          return _21428;
        if ("2143-6".equals(codeString))
          return _21436;
        if ("2144-4".equals(codeString))
          return _21444;
        if ("2145-1".equals(codeString))
          return _21451;
        if ("2146-9".equals(codeString))
          return _21469;
        if ("2148-5".equals(codeString))
          return _21485;
        if ("2149-3".equals(codeString))
          return _21493;
        if ("2150-1".equals(codeString))
          return _21501;
        if ("2151-9".equals(codeString))
          return _21519;
        if ("2152-7".equals(codeString))
          return _21527;
        if ("2153-5".equals(codeString))
          return _21535;
        if ("2155-0".equals(codeString))
          return _21550;
        if ("2156-8".equals(codeString))
          return _21568;
        if ("2157-6".equals(codeString))
          return _21576;
        if ("2158-4".equals(codeString))
          return _21584;
        if ("2159-2".equals(codeString))
          return _21592;
        if ("2160-0".equals(codeString))
          return _21600;
        if ("2161-8".equals(codeString))
          return _21618;
        if ("2162-6".equals(codeString))
          return _21626;
        if ("2163-4".equals(codeString))
          return _21634;
        if ("2165-9".equals(codeString))
          return _21659;
        if ("2166-7".equals(codeString))
          return _21667;
        if ("2167-5".equals(codeString))
          return _21675;
        if ("2168-3".equals(codeString))
          return _21683;
        if ("2169-1".equals(codeString))
          return _21691;
        if ("2170-9".equals(codeString))
          return _21709;
        if ("2171-7".equals(codeString))
          return _21717;
        if ("2172-5".equals(codeString))
          return _21725;
        if ("2173-3".equals(codeString))
          return _21733;
        if ("2174-1".equals(codeString))
          return _21741;
        if ("2175-8".equals(codeString))
          return _21758;
        if ("2176-6".equals(codeString))
          return _21766;
        if ("2178-2".equals(codeString))
          return _21782;
        if ("2180-8".equals(codeString))
          return _21808;
        if ("2182-4".equals(codeString))
          return _21824;
        if ("2184-0".equals(codeString))
          return _21840;
        if ("2186-5".equals(codeString))
          return _21865;
        throw new FHIRException("Unknown V3Ethnicity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _21352: return "2135-2";
            case _21378: return "2137-8";
            case _21386: return "2138-6";
            case _21394: return "2139-4";
            case _21402: return "2140-2";
            case _21410: return "2141-0";
            case _21428: return "2142-8";
            case _21436: return "2143-6";
            case _21444: return "2144-4";
            case _21451: return "2145-1";
            case _21469: return "2146-9";
            case _21485: return "2148-5";
            case _21493: return "2149-3";
            case _21501: return "2150-1";
            case _21519: return "2151-9";
            case _21527: return "2152-7";
            case _21535: return "2153-5";
            case _21550: return "2155-0";
            case _21568: return "2156-8";
            case _21576: return "2157-6";
            case _21584: return "2158-4";
            case _21592: return "2159-2";
            case _21600: return "2160-0";
            case _21618: return "2161-8";
            case _21626: return "2162-6";
            case _21634: return "2163-4";
            case _21659: return "2165-9";
            case _21667: return "2166-7";
            case _21675: return "2167-5";
            case _21683: return "2168-3";
            case _21691: return "2169-1";
            case _21709: return "2170-9";
            case _21717: return "2171-7";
            case _21725: return "2172-5";
            case _21733: return "2173-3";
            case _21741: return "2174-1";
            case _21758: return "2175-8";
            case _21766: return "2176-6";
            case _21782: return "2178-2";
            case _21808: return "2180-8";
            case _21824: return "2182-4";
            case _21840: return "2184-0";
            case _21865: return "2186-5";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/Ethnicity";
        }
        public String getDefinition() {
          switch (this) {
            case _21352: return "Hispanic or Latino";
            case _21378: return "Spaniard";
            case _21386: return "Andalusian";
            case _21394: return "Asturian";
            case _21402: return "Castillian";
            case _21410: return "Catalonian";
            case _21428: return "Belearic Islander";
            case _21436: return "Gallego";
            case _21444: return "Valencian";
            case _21451: return "Canarian";
            case _21469: return "Spanish Basque";
            case _21485: return "Mexican";
            case _21493: return "Mexican American";
            case _21501: return "Mexicano";
            case _21519: return "Chicano";
            case _21527: return "La Raza";
            case _21535: return "Mexican American Indian";
            case _21550: return "Central American";
            case _21568: return "Costa Rican";
            case _21576: return "Guatemalan";
            case _21584: return "Honduran";
            case _21592: return "Nicaraguan";
            case _21600: return "Panamanian";
            case _21618: return "Salvadoran";
            case _21626: return "Central American Indian";
            case _21634: return "Canal Zone";
            case _21659: return "South American";
            case _21667: return "Argentinean";
            case _21675: return "Bolivian";
            case _21683: return "Chilean";
            case _21691: return "Colombian";
            case _21709: return "Ecuadorian";
            case _21717: return "Paraguayan";
            case _21725: return "Peruvian";
            case _21733: return "Uruguayan";
            case _21741: return "Venezuelan";
            case _21758: return "South American Indian";
            case _21766: return "Criollo";
            case _21782: return "Latin American";
            case _21808: return "Puerto Rican";
            case _21824: return "Cuban";
            case _21840: return "Dominican";
            case _21865: return "Note that this term remains in the table for completeness, even though within HL7, the notion of \"not otherwise coded\" term is deprecated.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _21352: return "Hispanic or Latino";
            case _21378: return "Spaniard";
            case _21386: return "Andalusian";
            case _21394: return "Asturian";
            case _21402: return "Castillian";
            case _21410: return "Catalonian";
            case _21428: return "Belearic Islander";
            case _21436: return "Gallego";
            case _21444: return "Valencian";
            case _21451: return "Canarian";
            case _21469: return "Spanish Basque";
            case _21485: return "Mexican";
            case _21493: return "Mexican American";
            case _21501: return "Mexicano";
            case _21519: return "Chicano";
            case _21527: return "La Raza";
            case _21535: return "Mexican American Indian";
            case _21550: return "Central American";
            case _21568: return "Costa Rican";
            case _21576: return "Guatemalan";
            case _21584: return "Honduran";
            case _21592: return "Nicaraguan";
            case _21600: return "Panamanian";
            case _21618: return "Salvadoran";
            case _21626: return "Central American Indian";
            case _21634: return "Canal Zone";
            case _21659: return "South American";
            case _21667: return "Argentinean";
            case _21675: return "Bolivian";
            case _21683: return "Chilean";
            case _21691: return "Colombian";
            case _21709: return "Ecuadorian";
            case _21717: return "Paraguayan";
            case _21725: return "Peruvian";
            case _21733: return "Uruguayan";
            case _21741: return "Venezuelan";
            case _21758: return "South American Indian";
            case _21766: return "Criollo";
            case _21782: return "Latin American";
            case _21808: return "Puerto Rican";
            case _21824: return "Cuban";
            case _21840: return "Dominican";
            case _21865: return "Not Hispanic or Latino";
            default: return "?";
          }
    }


}

