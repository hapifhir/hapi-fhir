package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum MapModelMode {

        /**
         * This structure describes an instance passed to the mapping engine that is used a source of data
         */
        SOURCE, 
        /**
         * This structure describes an instance that the mapping engine may ask for that is used a source of data
         */
        QUERIED, 
        /**
         * This structure describes an instance passed to the mapping engine that is used a target of data
         */
        TARGET, 
        /**
         * This structure describes an instance that the mapping engine may ask to create that is used a target of data
         */
        PRODUCED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MapModelMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return SOURCE;
        if ("queried".equals(codeString))
          return QUERIED;
        if ("target".equals(codeString))
          return TARGET;
        if ("produced".equals(codeString))
          return PRODUCED;
        throw new FHIRException("Unknown MapModelMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SOURCE: return "source";
            case QUERIED: return "queried";
            case TARGET: return "target";
            case PRODUCED: return "produced";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/map-model-mode";
        }
        public String getDefinition() {
          switch (this) {
            case SOURCE: return "This structure describes an instance passed to the mapping engine that is used a source of data";
            case QUERIED: return "This structure describes an instance that the mapping engine may ask for that is used a source of data";
            case TARGET: return "This structure describes an instance passed to the mapping engine that is used a target of data";
            case PRODUCED: return "This structure describes an instance that the mapping engine may ask to create that is used a target of data";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SOURCE: return "Source Structure Definition";
            case QUERIED: return "Queried Structure Definition";
            case TARGET: return "Target Structure Definition";
            case PRODUCED: return "Produced Structure Definition";
            default: return "?";
          }
    }


}

