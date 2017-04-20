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

public enum LinkType {

        /**
         * The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains this link.
         */
        REPLACEDBY, 
        /**
         * The patient resource containing this link is the current active patient record. The link points back to an inactive patient resource that has been merged into this resource, and should be consulted to retrieve additional referenced information.
         */
        REPLACES, 
        /**
         * The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information.
         */
        REFER, 
        /**
         * The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.
         */
        SEEALSO, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LinkType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replaced-by".equals(codeString))
          return REPLACEDBY;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("refer".equals(codeString))
          return REFER;
        if ("seealso".equals(codeString))
          return SEEALSO;
        throw new FHIRException("Unknown LinkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REPLACEDBY: return "replaced-by";
            case REPLACES: return "replaces";
            case REFER: return "refer";
            case SEEALSO: return "seealso";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/link-type";
        }
        public String getDefinition() {
          switch (this) {
            case REPLACEDBY: return "The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains this link.";
            case REPLACES: return "The patient resource containing this link is the current active patient record. The link points back to an inactive patient resource that has been merged into this resource, and should be consulted to retrieve additional referenced information.";
            case REFER: return "The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information.";
            case SEEALSO: return "The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REPLACEDBY: return "Replaced-by";
            case REPLACES: return "Replaces";
            case REFER: return "Refer";
            case SEEALSO: return "See also";
            default: return "?";
          }
    }


}

