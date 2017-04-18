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

public enum LinkageType {

        /**
         * The record represents the "source of truth" (from the perspective of this Linkage resource) for the underlying event/condition/etc.
         */
        SOURCE, 
        /**
         * The record represents the alternative view of the underlying event/condition/etc.  The record may still be actively maintained, even though it is not considered to be the source of truth.
         */
        ALTERNATE, 
        /**
         * The record represents an obsolete record of the underlyng event/condition/etc.  It is not expected to be actively maintained.
         */
        HISTORICAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LinkageType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return SOURCE;
        if ("alternate".equals(codeString))
          return ALTERNATE;
        if ("historical".equals(codeString))
          return HISTORICAL;
        throw new FHIRException("Unknown LinkageType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SOURCE: return "source";
            case ALTERNATE: return "alternate";
            case HISTORICAL: return "historical";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/linkage-type";
        }
        public String getDefinition() {
          switch (this) {
            case SOURCE: return "The record represents the \"source of truth\" (from the perspective of this Linkage resource) for the underlying event/condition/etc.";
            case ALTERNATE: return "The record represents the alternative view of the underlying event/condition/etc.  The record may still be actively maintained, even though it is not considered to be the source of truth.";
            case HISTORICAL: return "The record represents an obsolete record of the underlyng event/condition/etc.  It is not expected to be actively maintained.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SOURCE: return "Source of truth";
            case ALTERNATE: return "Alternate record";
            case HISTORICAL: return "Historical/obsolete record";
            default: return "?";
          }
    }


}

