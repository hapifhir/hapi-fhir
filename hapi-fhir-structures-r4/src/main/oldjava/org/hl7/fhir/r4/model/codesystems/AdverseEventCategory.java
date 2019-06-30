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

public enum AdverseEventCategory {

        /**
         * The adverse event pertains to a product problem.
         */
        PRODUCTPROBLEM, 
        /**
         * The adverse event pertains to product quality.
         */
        PRODUCTQUALITY, 
        /**
         * The adverse event pertains to a product use error.
         */
        PRODUCTUSEERROR, 
        /**
         * The adverse event pertains to a wrong dose.
         */
        WRONGDOSE, 
        /**
         * The adverse event pertains to incorrect perscribing information.
         */
        INCORRECTPRESCRIBINGINFORMATION, 
        /**
         * The adverse event pertains to a wrong technique.
         */
        WRONGTECHNIQUE, 
        /**
         * The adverse event pertains to a wrong route of administration.
         */
        WRONGROUTEOFADMINISTRATION, 
        /**
         * The adverse event pertains to a wrong rate.
         */
        WRONGRATE, 
        /**
         * The adverse event pertains to a wrong duration.
         */
        WRONGDURATION, 
        /**
         * The adverse event pertains to a wrong time.
         */
        WRONGTIME, 
        /**
         * The adverse event pertains to an expired drug.
         */
        EXPIREDDRUG, 
        /**
         * The adverse event pertains to a medical device use error.
         */
        MEDICALDEVICEUSEERROR, 
        /**
         * The adverse event pertains to a problem with a different manufacturer of the same medication.
         */
        PROBLEMDIFFERENTMANUFACTURER, 
        /**
         * The adverse event pertains to an unsafe physical environment.
         */
        UNSAFEPHYSICALENVIRONMENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AdverseEventCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("product-problem".equals(codeString))
          return PRODUCTPROBLEM;
        if ("product-quality".equals(codeString))
          return PRODUCTQUALITY;
        if ("product-use-error".equals(codeString))
          return PRODUCTUSEERROR;
        if ("wrong-dose".equals(codeString))
          return WRONGDOSE;
        if ("incorrect-prescribing-information".equals(codeString))
          return INCORRECTPRESCRIBINGINFORMATION;
        if ("wrong-technique".equals(codeString))
          return WRONGTECHNIQUE;
        if ("wrong-route-of-administration".equals(codeString))
          return WRONGROUTEOFADMINISTRATION;
        if ("wrong-rate".equals(codeString))
          return WRONGRATE;
        if ("wrong-duration".equals(codeString))
          return WRONGDURATION;
        if ("wrong-time".equals(codeString))
          return WRONGTIME;
        if ("expired-drug".equals(codeString))
          return EXPIREDDRUG;
        if ("medical-device-use-error".equals(codeString))
          return MEDICALDEVICEUSEERROR;
        if ("problem-different-manufacturer".equals(codeString))
          return PROBLEMDIFFERENTMANUFACTURER;
        if ("unsafe-physical-environment".equals(codeString))
          return UNSAFEPHYSICALENVIRONMENT;
        throw new FHIRException("Unknown AdverseEventCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRODUCTPROBLEM: return "product-problem";
            case PRODUCTQUALITY: return "product-quality";
            case PRODUCTUSEERROR: return "product-use-error";
            case WRONGDOSE: return "wrong-dose";
            case INCORRECTPRESCRIBINGINFORMATION: return "incorrect-prescribing-information";
            case WRONGTECHNIQUE: return "wrong-technique";
            case WRONGROUTEOFADMINISTRATION: return "wrong-route-of-administration";
            case WRONGRATE: return "wrong-rate";
            case WRONGDURATION: return "wrong-duration";
            case WRONGTIME: return "wrong-time";
            case EXPIREDDRUG: return "expired-drug";
            case MEDICALDEVICEUSEERROR: return "medical-device-use-error";
            case PROBLEMDIFFERENTMANUFACTURER: return "problem-different-manufacturer";
            case UNSAFEPHYSICALENVIRONMENT: return "unsafe-physical-environment";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/adverse-event-category";
        }
        public String getDefinition() {
          switch (this) {
            case PRODUCTPROBLEM: return "The adverse event pertains to a product problem.";
            case PRODUCTQUALITY: return "The adverse event pertains to product quality.";
            case PRODUCTUSEERROR: return "The adverse event pertains to a product use error.";
            case WRONGDOSE: return "The adverse event pertains to a wrong dose.";
            case INCORRECTPRESCRIBINGINFORMATION: return "The adverse event pertains to incorrect perscribing information.";
            case WRONGTECHNIQUE: return "The adverse event pertains to a wrong technique.";
            case WRONGROUTEOFADMINISTRATION: return "The adverse event pertains to a wrong route of administration.";
            case WRONGRATE: return "The adverse event pertains to a wrong rate.";
            case WRONGDURATION: return "The adverse event pertains to a wrong duration.";
            case WRONGTIME: return "The adverse event pertains to a wrong time.";
            case EXPIREDDRUG: return "The adverse event pertains to an expired drug.";
            case MEDICALDEVICEUSEERROR: return "The adverse event pertains to a medical device use error.";
            case PROBLEMDIFFERENTMANUFACTURER: return "The adverse event pertains to a problem with a different manufacturer of the same medication.";
            case UNSAFEPHYSICALENVIRONMENT: return "The adverse event pertains to an unsafe physical environment.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRODUCTPROBLEM: return "Product Problem";
            case PRODUCTQUALITY: return "Product Quality";
            case PRODUCTUSEERROR: return "Product Use Error";
            case WRONGDOSE: return "Wrong Dose";
            case INCORRECTPRESCRIBINGINFORMATION: return "Incorrect Prescribing Information";
            case WRONGTECHNIQUE: return "Wrong Technique";
            case WRONGROUTEOFADMINISTRATION: return "Wrong Route of Administration";
            case WRONGRATE: return "Wrong Rate";
            case WRONGDURATION: return "Wrong Duration";
            case WRONGTIME: return "Wrong Time";
            case EXPIREDDRUG: return "Expired Drug";
            case MEDICALDEVICEUSEERROR: return "Medical Device Use Error";
            case PROBLEMDIFFERENTMANUFACTURER: return "Problem with Different Manufacturer of Same Medicine";
            case UNSAFEPHYSICALENVIRONMENT: return "Unsafe Physical Environment";
            default: return "?";
          }
    }


}

