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

public enum V3ActInvoiceElementModifier {

        /**
         * Electronic form with supporting information to follow.
         */
        EFORM, 
        /**
         * Fax with supporting information to follow.
         */
        FAX, 
        /**
         * Represents the last invoice from the perspective of the provider.
         */
        LINV, 
        /**
         * Paper documentation (or other physical format) with supporting information to follow.
         */
        PAPER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActInvoiceElementModifier fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("EFORM".equals(codeString))
          return EFORM;
        if ("FAX".equals(codeString))
          return FAX;
        if ("LINV".equals(codeString))
          return LINV;
        if ("PAPER".equals(codeString))
          return PAPER;
        throw new FHIRException("Unknown V3ActInvoiceElementModifier code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EFORM: return "EFORM";
            case FAX: return "FAX";
            case LINV: return "LINV";
            case PAPER: return "PAPER";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActInvoiceElementModifier";
        }
        public String getDefinition() {
          switch (this) {
            case EFORM: return "Electronic form with supporting information to follow.";
            case FAX: return "Fax with supporting information to follow.";
            case LINV: return "Represents the last invoice from the perspective of the provider.";
            case PAPER: return "Paper documentation (or other physical format) with supporting information to follow.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EFORM: return "Electronic Form To Follow";
            case FAX: return "Fax To Follow";
            case LINV: return "Last Invoice";
            case PAPER: return "Paper Documentation To Follow";
            default: return "?";
          }
    }


}

