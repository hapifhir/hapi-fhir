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

public enum SearchComparator {

        /**
         * the value for the parameter in the resource is equal to the provided value
         */
        EQ, 
        /**
         * the value for the parameter in the resource is not equal to the provided value
         */
        NE, 
        /**
         * the value for the parameter in the resource is greater than the provided value
         */
        GT, 
        /**
         * the value for the parameter in the resource is less than the provided value
         */
        LT, 
        /**
         * the value for the parameter in the resource is greater or equal to the provided value
         */
        GE, 
        /**
         * the value for the parameter in the resource is less or equal to the provided value
         */
        LE, 
        /**
         * the value for the parameter in the resource starts after the provided value
         */
        SA, 
        /**
         * the value for the parameter in the resource ends before the provided value
         */
        EB, 
        /**
         * the value for the parameter in the resource is approximately the same to the provided value.
         */
        AP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchComparator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("eq".equals(codeString))
          return EQ;
        if ("ne".equals(codeString))
          return NE;
        if ("gt".equals(codeString))
          return GT;
        if ("lt".equals(codeString))
          return LT;
        if ("ge".equals(codeString))
          return GE;
        if ("le".equals(codeString))
          return LE;
        if ("sa".equals(codeString))
          return SA;
        if ("eb".equals(codeString))
          return EB;
        if ("ap".equals(codeString))
          return AP;
        throw new FHIRException("Unknown SearchComparator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EQ: return "eq";
            case NE: return "ne";
            case GT: return "gt";
            case LT: return "lt";
            case GE: return "ge";
            case LE: return "le";
            case SA: return "sa";
            case EB: return "eb";
            case AP: return "ap";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/search-comparator";
        }
        public String getDefinition() {
          switch (this) {
            case EQ: return "the value for the parameter in the resource is equal to the provided value";
            case NE: return "the value for the parameter in the resource is not equal to the provided value";
            case GT: return "the value for the parameter in the resource is greater than the provided value";
            case LT: return "the value for the parameter in the resource is less than the provided value";
            case GE: return "the value for the parameter in the resource is greater or equal to the provided value";
            case LE: return "the value for the parameter in the resource is less or equal to the provided value";
            case SA: return "the value for the parameter in the resource starts after the provided value";
            case EB: return "the value for the parameter in the resource ends before the provided value";
            case AP: return "the value for the parameter in the resource is approximately the same to the provided value.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQ: return "Equals";
            case NE: return "Not Equals";
            case GT: return "Greater Than";
            case LT: return "Less Then";
            case GE: return "Greater or Equals";
            case LE: return "Less of Equal";
            case SA: return "Starts After";
            case EB: return "Ends Before";
            case AP: return "Approximately";
            default: return "?";
          }
    }


}

