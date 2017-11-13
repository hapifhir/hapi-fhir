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

// Generated on Thu, Feb 9, 2017 08:03-0500 for FHIR v1.9.0


import org.hl7.fhir.exceptions.FHIRException;

public enum CatalogContentStatus {

        /**
         * The catalog is approved
         */
        APPROVED, 
        /**
         * The catalog content is pending some action e.g. confirmation or approval
         */
        PENDING, 
        /**
         * The catalog is discontinued
         */
        DISCONTINUED, 
        /**
         * The catalog content is considered official
         */
        OFFICIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CatalogContentStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("approved".equals(codeString))
          return APPROVED;
        if ("pending".equals(codeString))
          return PENDING;
        if ("discontinued".equals(codeString))
          return DISCONTINUED;
        if ("official".equals(codeString))
          return OFFICIAL;
        throw new FHIRException("Unknown CatalogContentStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPROVED: return "approved";
            case PENDING: return "pending";
            case DISCONTINUED: return "discontinued";
            case OFFICIAL: return "official";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/catalog-content-status";
        }
        public String getDefinition() {
          switch (this) {
            case APPROVED: return "The catalog is approved";
            case PENDING: return "The catalog content is pending some action e.g. confirmation or approval";
            case DISCONTINUED: return "The catalog is discontinued";
            case OFFICIAL: return "The catalog content is considered official";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPROVED: return "Approved";
            case PENDING: return "Pending";
            case DISCONTINUED: return "Discontinued";
            case OFFICIAL: return "Official";
            default: return "?";
          }
    }


}

