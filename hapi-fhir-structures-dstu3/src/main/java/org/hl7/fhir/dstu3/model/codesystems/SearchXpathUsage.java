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

public enum SearchXpathUsage {

        /**
         * The search parameter is derived directly from the selected nodes based on the type definitions.
         */
        NORMAL, 
        /**
         * The search parameter is derived by a phonetic transform from the selected nodes.
         */
        PHONETIC, 
        /**
         * The search parameter is based on a spatial transform of the selected nodes.
         */
        NEARBY, 
        /**
         * The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle.
         */
        DISTANCE, 
        /**
         * The interpretation of the xpath statement is unknown (and can't be automated).
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchXpathUsage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("phonetic".equals(codeString))
          return PHONETIC;
        if ("nearby".equals(codeString))
          return NEARBY;
        if ("distance".equals(codeString))
          return DISTANCE;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown SearchXpathUsage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case PHONETIC: return "phonetic";
            case NEARBY: return "nearby";
            case DISTANCE: return "distance";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/search-xpath-usage";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "The search parameter is derived directly from the selected nodes based on the type definitions.";
            case PHONETIC: return "The search parameter is derived by a phonetic transform from the selected nodes.";
            case NEARBY: return "The search parameter is based on a spatial transform of the selected nodes.";
            case DISTANCE: return "The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle.";
            case OTHER: return "The interpretation of the xpath statement is unknown (and can't be automated).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "Normal";
            case PHONETIC: return "Phonetic";
            case NEARBY: return "Nearby";
            case DISTANCE: return "Distance";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

