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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3PaymentTerms {

        /**
         * Payment in full for products and/or services is required as soon as the service is performed or goods delivered.
         */
        COD, 
        /**
         * Payment in full for products and/or services is required 30 days from the time the service is performed or goods delivered.
         */
        N30, 
        /**
         * Payment in full for products and/or services is required 60 days from the time the service is performed or goods delivered.
         */
        N60, 
        /**
         * Payment in full for products and/or services is required 90 days from the time the service is performed or goods delivered.
         */
        N90, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3PaymentTerms fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("COD".equals(codeString))
          return COD;
        if ("N30".equals(codeString))
          return N30;
        if ("N60".equals(codeString))
          return N60;
        if ("N90".equals(codeString))
          return N90;
        throw new FHIRException("Unknown V3PaymentTerms code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COD: return "COD";
            case N30: return "N30";
            case N60: return "N60";
            case N90: return "N90";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/PaymentTerms";
        }
        public String getDefinition() {
          switch (this) {
            case COD: return "Payment in full for products and/or services is required as soon as the service is performed or goods delivered.";
            case N30: return "Payment in full for products and/or services is required 30 days from the time the service is performed or goods delivered.";
            case N60: return "Payment in full for products and/or services is required 60 days from the time the service is performed or goods delivered.";
            case N90: return "Payment in full for products and/or services is required 90 days from the time the service is performed or goods delivered.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COD: return "Cash on Delivery";
            case N30: return "Net 30 days";
            case N60: return "Net 60 days";
            case N90: return "Net 90 days";
            default: return "?";
          }
    }


}

