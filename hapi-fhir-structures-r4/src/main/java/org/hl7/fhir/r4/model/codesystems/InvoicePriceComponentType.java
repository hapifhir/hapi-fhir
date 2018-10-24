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

public enum InvoicePriceComponentType {

        /**
         * the amount is the base price used for calculating the total price before applying surcharges, discount or taxes.
         */
        BASE, 
        /**
         * the amount is a surcharge applied on the base price.
         */
        SURCHARGE, 
        /**
         * the amount is a deduction applied on the base price.
         */
        DEDUCTION, 
        /**
         * the amount is a discount applied on the base price.
         */
        DISCOUNT, 
        /**
         * the amount is the tax component of the total price.
         */
        TAX, 
        /**
         * the amount is of informational character, it has not been applied in the calculation of the total price.
         */
        INFORMATIONAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static InvoicePriceComponentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("base".equals(codeString))
          return BASE;
        if ("surcharge".equals(codeString))
          return SURCHARGE;
        if ("deduction".equals(codeString))
          return DEDUCTION;
        if ("discount".equals(codeString))
          return DISCOUNT;
        if ("tax".equals(codeString))
          return TAX;
        if ("informational".equals(codeString))
          return INFORMATIONAL;
        throw new FHIRException("Unknown InvoicePriceComponentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BASE: return "base";
            case SURCHARGE: return "surcharge";
            case DEDUCTION: return "deduction";
            case DISCOUNT: return "discount";
            case TAX: return "tax";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/invoice-priceComponentType";
        }
        public String getDefinition() {
          switch (this) {
            case BASE: return "the amount is the base price used for calculating the total price before applying surcharges, discount or taxes.";
            case SURCHARGE: return "the amount is a surcharge applied on the base price.";
            case DEDUCTION: return "the amount is a deduction applied on the base price.";
            case DISCOUNT: return "the amount is a discount applied on the base price.";
            case TAX: return "the amount is the tax component of the total price.";
            case INFORMATIONAL: return "the amount is of informational character, it has not been applied in the calculation of the total price.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BASE: return "base price";
            case SURCHARGE: return "surcharge";
            case DEDUCTION: return "deduction";
            case DISCOUNT: return "discount";
            case TAX: return "tax";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
    }


}

