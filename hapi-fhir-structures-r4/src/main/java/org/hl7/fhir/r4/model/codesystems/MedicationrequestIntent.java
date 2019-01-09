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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum MedicationrequestIntent {

        /**
         * The request is a suggestion made by someone/something that doesn't have an intention to ensure it occurs and without providing an authorization to act
         */
        PROPOSAL, 
        /**
         * The request represents an intention to ensure something occurs without providing an authorization for others to act.
         */
        PLAN, 
        /**
         * The request represents a request/demand and authorization for action
         */
        ORDER, 
        /**
         * The request represents the original authorization for the medication request.
         */
        ORIGINALORDER, 
        /**
         * The request represents an automatically generated supplemental authorization for action based on a parent authorization together with initial results of the action taken against that parent authorization..
         */
        REFLEXORDER, 
        /**
         * The request represents the view of an authorization instantiated by a fulfilling system representing the details of the fulfiller's intention to act upon a submitted order.
         */
        FILLERORDER, 
        /**
         * The request represents an instance for the particular order, for example a medication administration record.
         */
        INSTANCEORDER, 
        /**
         * The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or  other constraints among a set of requests.
         */
        OPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationrequestIntent fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return PROPOSAL;
        if ("plan".equals(codeString))
          return PLAN;
        if ("order".equals(codeString))
          return ORDER;
        if ("original-order".equals(codeString))
          return ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return REFLEXORDER;
        if ("filler-order".equals(codeString))
          return FILLERORDER;
        if ("instance-order".equals(codeString))
          return INSTANCEORDER;
        if ("option".equals(codeString))
          return OPTION;
        throw new FHIRException("Unknown MedicationrequestIntent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case ORDER: return "order";
            case ORIGINALORDER: return "original-order";
            case REFLEXORDER: return "reflex-order";
            case FILLERORDER: return "filler-order";
            case INSTANCEORDER: return "instance-order";
            case OPTION: return "option";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/CodeSystem/medicationrequest-intent";
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSAL: return "The request is a suggestion made by someone/something that doesn't have an intention to ensure it occurs and without providing an authorization to act";
            case PLAN: return "The request represents an intention to ensure something occurs without providing an authorization for others to act.";
            case ORDER: return "The request represents a request/demand and authorization for action";
            case ORIGINALORDER: return "The request represents the original authorization for the medication request.";
            case REFLEXORDER: return "The request represents an automatically generated supplemental authorization for action based on a parent authorization together with initial results of the action taken against that parent authorization..";
            case FILLERORDER: return "The request represents the view of an authorization instantiated by a fulfilling system representing the details of the fulfiller's intention to act upon a submitted order.";
            case INSTANCEORDER: return "The request represents an instance for the particular order, for example a medication administration record.";
            case OPTION: return "The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or  other constraints among a set of requests.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSAL: return "Proposal";
            case PLAN: return "Plan";
            case ORDER: return "Order";
            case ORIGINALORDER: return "Original Order";
            case REFLEXORDER: return "Reflex Order";
            case FILLERORDER: return "Filler Order";
            case INSTANCEORDER: return "Instance Order";
            case OPTION: return "Option";
            default: return "?";
          }
    }


}

