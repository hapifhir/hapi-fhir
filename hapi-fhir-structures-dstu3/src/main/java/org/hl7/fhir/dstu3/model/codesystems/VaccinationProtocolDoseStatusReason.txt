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

public enum VaccinationProtocolDoseStatusReason {

        /**
         * The product was stored in a manner inconsistent with manufacturer guidelines potentially reducing the effectiveness of the product.
         */
        ADVSTORAGE, 
        /**
         * The product was stored at a temperature inconsistent with manufacturer guidelines potentially reducing the effectiveness of the product.
         */
        COLDCHBRK, 
        /**
         * The product was administered after the expiration date associated with lot of vaccine.
         */
        EXPLOT, 
        /**
         * The product was administered at a time inconsistent with the documented schedule.
         */
        OUTSIDESCHED, 
        /**
         * The product administered has been recalled by the manufacturer.
         */
        PRODRECALL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VaccinationProtocolDoseStatusReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("advstorage".equals(codeString))
          return ADVSTORAGE;
        if ("coldchbrk".equals(codeString))
          return COLDCHBRK;
        if ("explot".equals(codeString))
          return EXPLOT;
        if ("outsidesched".equals(codeString))
          return OUTSIDESCHED;
        if ("prodrecall".equals(codeString))
          return PRODRECALL;
        throw new FHIRException("Unknown VaccinationProtocolDoseStatusReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADVSTORAGE: return "advstorage";
            case COLDCHBRK: return "coldchbrk";
            case EXPLOT: return "explot";
            case OUTSIDESCHED: return "outsidesched";
            case PRODRECALL: return "prodrecall";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/vaccination-protocol-dose-status-reason";
        }
        public String getDefinition() {
          switch (this) {
            case ADVSTORAGE: return "The product was stored in a manner inconsistent with manufacturer guidelines potentially reducing the effectiveness of the product.";
            case COLDCHBRK: return "The product was stored at a temperature inconsistent with manufacturer guidelines potentially reducing the effectiveness of the product.";
            case EXPLOT: return "The product was administered after the expiration date associated with lot of vaccine.";
            case OUTSIDESCHED: return "The product was administered at a time inconsistent with the documented schedule.";
            case PRODRECALL: return "The product administered has been recalled by the manufacturer.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADVSTORAGE: return "Adverse storage condition";
            case COLDCHBRK: return "Cold chain break";
            case EXPLOT: return "Expired lot";
            case OUTSIDESCHED: return "Administered outside recommended schedule";
            case PRODRECALL: return "Product recall";
            default: return "?";
          }
    }


}

