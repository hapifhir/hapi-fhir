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

public enum ExtraActivityType {

        /**
         * Activity resulting in a structured collection of preexisting content that does not necessarily result in an integral object with semantic context making it more than the sum of component parts, from which components could be disaggregated without loss of semantic context, e.g., the assembly of multiple stand-alone documents.
         */
        AGGREGATE, 
        /**
         * Activity resulting in the structured compilation of new and preexisting content for the purposes of forming an integral object with  semantic context making it more than the sum of component parts, which would be lost if decomposed. For example, the composition of a document that includes in whole or part other documents along with new content that result in a new document that has unique semantic meaning.
         */
        COMPOSE, 
        /**
         * The means used to associate a set of security attributes with a specific information object as part of the data structure for that object. [ISO-10181-3 Access Control]
         */
        LABEL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExtraActivityType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("aggregate".equals(codeString))
          return AGGREGATE;
        if ("compose".equals(codeString))
          return COMPOSE;
        if ("label".equals(codeString))
          return LABEL;
        throw new FHIRException("Unknown ExtraActivityType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AGGREGATE: return "aggregate";
            case COMPOSE: return "compose";
            case LABEL: return "label";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/extra-activity-type";
        }
        public String getDefinition() {
          switch (this) {
            case AGGREGATE: return "Activity resulting in a structured collection of preexisting content that does not necessarily result in an integral object with semantic context making it more than the sum of component parts, from which components could be disaggregated without loss of semantic context, e.g., the assembly of multiple stand-alone documents.";
            case COMPOSE: return "Activity resulting in the structured compilation of new and preexisting content for the purposes of forming an integral object with  semantic context making it more than the sum of component parts, which would be lost if decomposed. For example, the composition of a document that includes in whole or part other documents along with new content that result in a new document that has unique semantic meaning.";
            case LABEL: return "The means used to associate a set of security attributes with a specific information object as part of the data structure for that object. [ISO-10181-3 Access Control]";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AGGREGATE: return "aggregate";
            case COMPOSE: return "compose";
            case LABEL: return "label";
            default: return "?";
          }
    }


}

