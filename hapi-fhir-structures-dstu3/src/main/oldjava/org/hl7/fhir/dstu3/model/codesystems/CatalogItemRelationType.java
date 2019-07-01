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

public enum CatalogItemRelationType {

        /**
         * The entry is a package that contains the related entry
         */
        PKGCONTAINS, 
        /**
         * The entry is contained in the related entry
         */
        PKGISCONTAINEDIN, 
        /**
         * The entry contains the related entry as a substance
         */
        CONTAINSSBST, 
        /**
         * The entry combines with the related entry
         */
        COMBINESWITH, 
        /**
         * The entry requires the related entry for use
         */
        REQUIRES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CatalogItemRelationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pkg-contains".equals(codeString))
          return PKGCONTAINS;
        if ("pkg-is-contained-in".equals(codeString))
          return PKGISCONTAINEDIN;
        if ("contains-sbst".equals(codeString))
          return CONTAINSSBST;
        if ("combines-with".equals(codeString))
          return COMBINESWITH;
        if ("requires".equals(codeString))
          return REQUIRES;
        throw new FHIRException("Unknown CatalogItemRelationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PKGCONTAINS: return "pkg-contains";
            case PKGISCONTAINEDIN: return "pkg-is-contained-in";
            case CONTAINSSBST: return "contains-sbst";
            case COMBINESWITH: return "combines-with";
            case REQUIRES: return "requires";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/catalog-item-relation-type";
        }
        public String getDefinition() {
          switch (this) {
            case PKGCONTAINS: return "The entry is a package that contains the related entry";
            case PKGISCONTAINEDIN: return "The entry is contained in the related entry";
            case CONTAINSSBST: return "The entry contains the related entry as a substance";
            case COMBINESWITH: return "The entry combines with the related entry";
            case REQUIRES: return "The entry requires the related entry for use";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PKGCONTAINS: return "Package Contains";
            case PKGISCONTAINEDIN: return "Is contained in Package";
            case CONTAINSSBST: return "Contains Substance";
            case COMBINESWITH: return "Combines with";
            case REQUIRES: return "Requires";
            default: return "?";
          }
    }


}

