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

// Generated on Sun, Mar 12, 2017 20:35-0400 for FHIR v1.9.0


import org.hl7.fhir.exceptions.FHIRException;

public enum CarePlanRelationship {

        /**
         * The referenced plan is considered to be part of this plan.
         */
        INCLUDES, 
        /**
         * This plan takes the places of the referenced plan.
         */
        REPLACES, 
        /**
         * This plan provides details about how to perform activities defined at a higher level by the referenced plan.
         */
        FULFILLS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanRelationship fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("includes".equals(codeString))
          return INCLUDES;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("fulfills".equals(codeString))
          return FULFILLS;
        throw new FHIRException("Unknown CarePlanRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INCLUDES: return "includes";
            case REPLACES: return "replaces";
            case FULFILLS: return "fulfills";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-plan-relationship";
        }
        public String getDefinition() {
          switch (this) {
            case INCLUDES: return "The referenced plan is considered to be part of this plan.";
            case REPLACES: return "This plan takes the places of the referenced plan.";
            case FULFILLS: return "This plan provides details about how to perform activities defined at a higher level by the referenced plan.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INCLUDES: return "Includes";
            case REPLACES: return "Replaces";
            case FULFILLS: return "Fulfills";
            default: return "?";
          }
    }


}

