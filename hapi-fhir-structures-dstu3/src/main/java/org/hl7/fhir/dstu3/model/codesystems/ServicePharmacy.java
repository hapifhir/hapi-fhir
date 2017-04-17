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

public enum ServicePharmacy {

        /**
         * Smoking cessation
         */
        SMOKECESS, 
        /**
         * Flu Shot
         */
        FLUSHOT, 
        /**
         * The wholesale price of the medication.
         */
        DRUGCOST, 
        /**
         * The additional cost assessed on the drug.
         */
        MARKUP, 
        /**
         * The professional fee charged for dispensing the product or service.
         */
        DISPENSEFEE, 
        /**
         * The professional fee charged for compounding the medication.
         */
        COMPOUNDFEE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServicePharmacy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("smokecess".equals(codeString))
          return SMOKECESS;
        if ("flushot".equals(codeString))
          return FLUSHOT;
        if ("drugcost".equals(codeString))
          return DRUGCOST;
        if ("markup".equals(codeString))
          return MARKUP;
        if ("dispensefee".equals(codeString))
          return DISPENSEFEE;
        if ("compoundfee".equals(codeString))
          return COMPOUNDFEE;
        throw new FHIRException("Unknown ServicePharmacy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SMOKECESS: return "smokecess";
            case FLUSHOT: return "flushot";
            case DRUGCOST: return "drugcost";
            case MARKUP: return "markup";
            case DISPENSEFEE: return "dispensefee";
            case COMPOUNDFEE: return "compoundfee";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-pharmaservice";
        }
        public String getDefinition() {
          switch (this) {
            case SMOKECESS: return "Smoking cessation";
            case FLUSHOT: return "Flu Shot";
            case DRUGCOST: return "The wholesale price of the medication.";
            case MARKUP: return "The additional cost assessed on the drug.";
            case DISPENSEFEE: return "The professional fee charged for dispensing the product or service.";
            case COMPOUNDFEE: return "The professional fee charged for compounding the medication.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SMOKECESS: return "Smoking cessation";
            case FLUSHOT: return "Flu Shot";
            case DRUGCOST: return "Drug Cost";
            case MARKUP: return "Markup";
            case DISPENSEFEE: return "Dispense Fee";
            case COMPOUNDFEE: return "Compounding Fee";
            default: return "?";
          }
    }


}

