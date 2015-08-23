package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum ServiceProvisionConditions {

        /**
         * This service is available for no patient cost
         */
        FREE, 
        /**
         * There are discounts available on this service for qualifying patients
         */
        DISC, 
        /**
         * Fees apply for this service
         */
        COST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceProvisionConditions fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("free".equals(codeString))
          return FREE;
        if ("disc".equals(codeString))
          return DISC;
        if ("cost".equals(codeString))
          return COST;
        throw new Exception("Unknown ServiceProvisionConditions code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FREE: return "free";
            case DISC: return "disc";
            case COST: return "cost";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/service-provision-conditions";
        }
        public String getDefinition() {
          switch (this) {
            case FREE: return "This service is available for no patient cost";
            case DISC: return "There are discounts available on this service for qualifying patients";
            case COST: return "Fees apply for this service";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FREE: return "Free";
            case DISC: return "Discounts Available";
            case COST: return "Fees apply";
            default: return "?";
          }
    }


}

