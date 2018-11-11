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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ConceptSubsumptionOutcome {

        /**
         * The two concepts are equivalent (have the same properties).
         */
        EQUIVALENT, 
        /**
         * Coding/code "A" subsumes Coding/code "B" (e.g. B has all the properties A has, and some of it's own).
         */
        SUBSUMES, 
        /**
         * Coding/code "A" is subsumed by Coding/code "B" (e.g. A has all the properties B has, and some of it's own).
         */
        SUBSUMEDBY, 
        /**
         * Coding/code "A" and Coding/code "B" are disjoint (e.g. each has propeties that the other doesn't have).
         */
        NOTSUBSUMED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConceptSubsumptionOutcome fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("equivalent".equals(codeString))
          return EQUIVALENT;
        if ("subsumes".equals(codeString))
          return SUBSUMES;
        if ("subsumed-by".equals(codeString))
          return SUBSUMEDBY;
        if ("not-subsumed".equals(codeString))
          return NOTSUBSUMED;
        throw new FHIRException("Unknown ConceptSubsumptionOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EQUIVALENT: return "equivalent";
            case SUBSUMES: return "subsumes";
            case SUBSUMEDBY: return "subsumed-by";
            case NOTSUBSUMED: return "not-subsumed";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/concept-subsumption-outcome";
        }
        public String getDefinition() {
          switch (this) {
            case EQUIVALENT: return "The two concepts are equivalent (have the same properties).";
            case SUBSUMES: return "Coding/code \"A\" subsumes Coding/code \"B\" (e.g. B has all the properties A has, and some of it's own).";
            case SUBSUMEDBY: return "Coding/code \"A\" is subsumed by Coding/code \"B\" (e.g. A has all the properties B has, and some of it's own).";
            case NOTSUBSUMED: return "Coding/code \"A\" and Coding/code \"B\" are disjoint (e.g. each has propeties that the other doesn't have).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQUIVALENT: return "Equivalent";
            case SUBSUMES: return "Subsumes";
            case SUBSUMEDBY: return "Subsumed-By";
            case NOTSUBSUMED: return "Not-Subsumed";
            default: return "?";
          }
    }


}

