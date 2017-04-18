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

public enum ResourceAggregationMode {

        /**
         * The reference is a local reference to a contained resource.
         */
        CONTAINED, 
        /**
         * The reference to a resource that has to be resolved externally to the resource that includes the reference.
         */
        REFERENCED, 
        /**
         * The resource the reference points to will be found in the same bundle as the resource that includes the reference.
         */
        BUNDLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceAggregationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return CONTAINED;
        if ("referenced".equals(codeString))
          return REFERENCED;
        if ("bundled".equals(codeString))
          return BUNDLED;
        throw new FHIRException("Unknown ResourceAggregationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONTAINED: return "contained";
            case REFERENCED: return "referenced";
            case BUNDLED: return "bundled";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/resource-aggregation-mode";
        }
        public String getDefinition() {
          switch (this) {
            case CONTAINED: return "The reference is a local reference to a contained resource.";
            case REFERENCED: return "The reference to a resource that has to be resolved externally to the resource that includes the reference.";
            case BUNDLED: return "The resource the reference points to will be found in the same bundle as the resource that includes the reference.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTAINED: return "Contained";
            case REFERENCED: return "Referenced";
            case BUNDLED: return "Bundled";
            default: return "?";
          }
    }


}

