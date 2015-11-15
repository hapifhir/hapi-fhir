package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum XdsRelationshipType {

        /**
         * A separate XDS document that references a prior document, and may extend or alter the observations in the prior document.
         */
        APND, 
        /**
         * A new version of an existing document.
         */
        RPLC, 
        /**
         * A transformed document is derived by a machine translation from some other format.
         */
        XFRM, 
        /**
         * Both a XFRM and a RPLC relationship.
         */
        XFRMRPLC, 
        /**
         * This document signs the target document.
         */
        SIGNS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static XdsRelationshipType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("APND".equals(codeString))
          return APND;
        if ("RPLC".equals(codeString))
          return RPLC;
        if ("XFRM".equals(codeString))
          return XFRM;
        if ("XFRM_RPLC".equals(codeString))
          return XFRMRPLC;
        if ("signs".equals(codeString))
          return SIGNS;
        throw new Exception("Unknown XdsRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APND: return "APND";
            case RPLC: return "RPLC";
            case XFRM: return "XFRM";
            case XFRMRPLC: return "XFRM_RPLC";
            case SIGNS: return "signs";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/xds-relationship-type";
        }
        public String getDefinition() {
          switch (this) {
            case APND: return "A separate XDS document that references a prior document, and may extend or alter the observations in the prior document.";
            case RPLC: return "A new version of an existing document.";
            case XFRM: return "A transformed document is derived by a machine translation from some other format.";
            case XFRMRPLC: return "Both a XFRM and a RPLC relationship.";
            case SIGNS: return "This document signs the target document.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APND: return "APND";
            case RPLC: return "RPLC";
            case XFRM: return "XFRM";
            case XFRMRPLC: return "XFRM_RPLC";
            case SIGNS: return "Signs";
            default: return "?";
          }
    }


}

