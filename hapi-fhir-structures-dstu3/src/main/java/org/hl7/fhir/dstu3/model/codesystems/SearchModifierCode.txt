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

public enum SearchModifierCode {

        /**
         * The search parameter returns resources that have a value or not.
         */
        MISSING, 
        /**
         * The search parameter returns resources that have a value that exactly matches the supplied parameter (the whole string, including casing and accents).
         */
        EXACT, 
        /**
         * The search parameter returns resources that include the supplied parameter value anywhere within the field being searched.
         */
        CONTAINS, 
        /**
         * The search parameter returns resources that do not contain a match.
         */
        NOT, 
        /**
         * The search parameter is processed as a string that searches text associated with the code/value - either CodeableConcept.text, Coding.display, or Identifier.type.text.
         */
        TEXT, 
        /**
         * The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is in the specified value set.
         */
        IN, 
        /**
         * The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is not in the specified value set.
         */
        NOTIN, 
        /**
         * The search parameter tests whether the value in a resource is subsumed by the specified value (is-a, or hierarchical relationships).
         */
        BELOW, 
        /**
         * The search parameter tests whether the value in a resource subsumes the specified value (is-a, or hierarchical relationships).
         */
        ABOVE, 
        /**
         * The search parameter only applies to the Resource Type specified as a modifier (e.g. the modifier is not actually :type, but :Patient etc.).
         */
        TYPE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchModifierCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("missing".equals(codeString))
          return MISSING;
        if ("exact".equals(codeString))
          return EXACT;
        if ("contains".equals(codeString))
          return CONTAINS;
        if ("not".equals(codeString))
          return NOT;
        if ("text".equals(codeString))
          return TEXT;
        if ("in".equals(codeString))
          return IN;
        if ("not-in".equals(codeString))
          return NOTIN;
        if ("below".equals(codeString))
          return BELOW;
        if ("above".equals(codeString))
          return ABOVE;
        if ("type".equals(codeString))
          return TYPE;
        throw new FHIRException("Unknown SearchModifierCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MISSING: return "missing";
            case EXACT: return "exact";
            case CONTAINS: return "contains";
            case NOT: return "not";
            case TEXT: return "text";
            case IN: return "in";
            case NOTIN: return "not-in";
            case BELOW: return "below";
            case ABOVE: return "above";
            case TYPE: return "type";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/search-modifier-code";
        }
        public String getDefinition() {
          switch (this) {
            case MISSING: return "The search parameter returns resources that have a value or not.";
            case EXACT: return "The search parameter returns resources that have a value that exactly matches the supplied parameter (the whole string, including casing and accents).";
            case CONTAINS: return "The search parameter returns resources that include the supplied parameter value anywhere within the field being searched.";
            case NOT: return "The search parameter returns resources that do not contain a match.";
            case TEXT: return "The search parameter is processed as a string that searches text associated with the code/value - either CodeableConcept.text, Coding.display, or Identifier.type.text.";
            case IN: return "The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is in the specified value set.";
            case NOTIN: return "The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is not in the specified value set.";
            case BELOW: return "The search parameter tests whether the value in a resource is subsumed by the specified value (is-a, or hierarchical relationships).";
            case ABOVE: return "The search parameter tests whether the value in a resource subsumes the specified value (is-a, or hierarchical relationships).";
            case TYPE: return "The search parameter only applies to the Resource Type specified as a modifier (e.g. the modifier is not actually :type, but :Patient etc.).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MISSING: return "Missing";
            case EXACT: return "Exact";
            case CONTAINS: return "Contains";
            case NOT: return "Not";
            case TEXT: return "Text";
            case IN: return "In";
            case NOTIN: return "Not In";
            case BELOW: return "Below";
            case ABOVE: return "Above";
            case TYPE: return "Type";
            default: return "?";
          }
    }


}

