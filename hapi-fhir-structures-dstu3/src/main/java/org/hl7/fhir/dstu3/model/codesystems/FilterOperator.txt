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

public enum FilterOperator {

        /**
         * The specified property of the code equals the provided value.
         */
        EQUAL, 
        /**
         * Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, including the provided concept itself (i.e. include child codes)
         */
        ISA, 
        /**
         * Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, excluding the provided concept itself (i.e. include child codes)
         */
        DESCENDENTOF, 
        /**
         * The specified property of the code does not have an is-a relationship with the provided value.
         */
        ISNOTA, 
        /**
         * The specified property of the code  matches the regex specified in the provided value.
         */
        REGEX, 
        /**
         * The specified property of the code is in the set of codes or concepts specified in the provided value (comma separated list).
         */
        IN, 
        /**
         * The specified property of the code is not in the set of codes or concepts specified in the provided value (comma separated list).
         */
        NOTIN, 
        /**
         * Includes all concept ids that have a transitive is-a relationship from the concept Id provided as the value, including the provided concept itself (e.g. include parent codes)
         */
        GENERALIZES, 
        /**
         * The specified property of the code has at least one value (if the specified value is true; if the specified value is false, then matches when the specified property of the code has no values)
         */
        EXISTS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FilterOperator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("=".equals(codeString))
          return EQUAL;
        if ("is-a".equals(codeString))
          return ISA;
        if ("descendent-of".equals(codeString))
          return DESCENDENTOF;
        if ("is-not-a".equals(codeString))
          return ISNOTA;
        if ("regex".equals(codeString))
          return REGEX;
        if ("in".equals(codeString))
          return IN;
        if ("not-in".equals(codeString))
          return NOTIN;
        if ("generalizes".equals(codeString))
          return GENERALIZES;
        if ("exists".equals(codeString))
          return EXISTS;
        throw new FHIRException("Unknown FilterOperator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EQUAL: return "=";
            case ISA: return "is-a";
            case DESCENDENTOF: return "descendent-of";
            case ISNOTA: return "is-not-a";
            case REGEX: return "regex";
            case IN: return "in";
            case NOTIN: return "not-in";
            case GENERALIZES: return "generalizes";
            case EXISTS: return "exists";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/filter-operator";
        }
        public String getDefinition() {
          switch (this) {
            case EQUAL: return "The specified property of the code equals the provided value.";
            case ISA: return "Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, including the provided concept itself (i.e. include child codes)";
            case DESCENDENTOF: return "Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, excluding the provided concept itself (i.e. include child codes)";
            case ISNOTA: return "The specified property of the code does not have an is-a relationship with the provided value.";
            case REGEX: return "The specified property of the code  matches the regex specified in the provided value.";
            case IN: return "The specified property of the code is in the set of codes or concepts specified in the provided value (comma separated list).";
            case NOTIN: return "The specified property of the code is not in the set of codes or concepts specified in the provided value (comma separated list).";
            case GENERALIZES: return "Includes all concept ids that have a transitive is-a relationship from the concept Id provided as the value, including the provided concept itself (e.g. include parent codes)";
            case EXISTS: return "The specified property of the code has at least one value (if the specified value is true; if the specified value is false, then matches when the specified property of the code has no values)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQUAL: return "Equals";
            case ISA: return "Is A (by subsumption)";
            case DESCENDENTOF: return "Descendent Of (by subsumption)";
            case ISNOTA: return "Not (Is A) (by subsumption)";
            case REGEX: return "Regular Expression";
            case IN: return "In Set";
            case NOTIN: return "Not in Set";
            case GENERALIZES: return "Generalizes (by Subsumption)";
            case EXISTS: return "Exists";
            default: return "?";
          }
    }


}

