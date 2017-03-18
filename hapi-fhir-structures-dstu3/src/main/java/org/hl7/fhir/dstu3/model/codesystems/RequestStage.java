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

// Generated on Mon, Jan 16, 2017 12:12-0500 for FHIR v1.9.0


import org.hl7.fhir.exceptions.FHIRException;

public enum RequestStage {

        /**
         * The request is a proposal
         */
        PROPOSAL, 
        /**
         * The request is a plan
         */
        PLAN, 
        /**
         * The request is an order.
         */
        ORIGINALORDER, 
        /**
         * Represents an order created by a fulfiller as a representation of the specific action(s) they intend to take to fulfill the original order.  Typically these orders are more fully encoded than the original placer order.
         */
        ENCODED, 
        /**
         * Represents a separate order created by a fulfiller as result of fulfilment of an order.
         */
        REFLEXORDER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RequestStage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return PROPOSAL;
        if ("plan".equals(codeString))
          return PLAN;
        if ("original-order".equals(codeString))
          return ORIGINALORDER;
        if ("encoded".equals(codeString))
          return ENCODED;
        if ("reflex-order".equals(codeString))
          return REFLEXORDER;
        throw new FHIRException("Unknown RequestStage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case ORIGINALORDER: return "original-order";
            case ENCODED: return "encoded";
            case REFLEXORDER: return "reflex-order";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/request-stage";
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSAL: return "The request is a proposal";
            case PLAN: return "The request is a plan";
            case ORIGINALORDER: return "The request is an order.";
            case ENCODED: return "Represents an order created by a fulfiller as a representation of the specific action(s) they intend to take to fulfill the original order.  Typically these orders are more fully encoded than the original placer order.";
            case REFLEXORDER: return "Represents a separate order created by a fulfiller as result of fulfilment of an order.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSAL: return "Proposal";
            case PLAN: return "Plan";
            case ORIGINALORDER: return "Original Order";
            case ENCODED: return "Encoded";
            case REFLEXORDER: return "Reflex Order";
            default: return "?";
          }
    }


}

