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

public enum CatalogContentType {

        /**
         * This is a product submission
         */
        SINGLESUBMISSION, 
        /**
         * This is a resubmission of a previously submitted item
         */
        RESUBMISSION, 
        /**
         * This is a full catalog transfer
         */
        FULLCATALOG, 
        /**
         * This is a differential update
         */
        CATALOGUPDATE, 
        /**
         * This is a response to a request for catalog information
         */
        CATALOGRESPONSE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CatalogContentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single-submission".equals(codeString))
          return SINGLESUBMISSION;
        if ("resubmission".equals(codeString))
          return RESUBMISSION;
        if ("full-catalog".equals(codeString))
          return FULLCATALOG;
        if ("catalog-update".equals(codeString))
          return CATALOGUPDATE;
        if ("catalog-response".equals(codeString))
          return CATALOGRESPONSE;
        throw new FHIRException("Unknown CatalogContentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SINGLESUBMISSION: return "single-submission";
            case RESUBMISSION: return "resubmission";
            case FULLCATALOG: return "full-catalog";
            case CATALOGUPDATE: return "catalog-update";
            case CATALOGRESPONSE: return "catalog-response";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/catalog-content-type";
        }
        public String getDefinition() {
          switch (this) {
            case SINGLESUBMISSION: return "This is a product submission";
            case RESUBMISSION: return "This is a resubmission of a previously submitted item";
            case FULLCATALOG: return "This is a full catalog transfer";
            case CATALOGUPDATE: return "This is a differential update";
            case CATALOGRESPONSE: return "This is a response to a request for catalog information";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SINGLESUBMISSION: return "Submission";
            case RESUBMISSION: return "Resubmission";
            case FULLCATALOG: return "Full Catalog";
            case CATALOGUPDATE: return "Update";
            case CATALOGRESPONSE: return "Response";
            default: return "?";
          }
    }


}

