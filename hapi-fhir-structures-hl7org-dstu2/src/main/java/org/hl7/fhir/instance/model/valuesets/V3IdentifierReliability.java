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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum V3IdentifierReliability {

        /**
         * Description: The identifier was issued by the system responsible for constructing the instance.
         */
        ISS, 
        /**
         * Description: The identifier was provided to the system that constructed the instance, but has not been verified. e.g. a Drivers  license entered manually into a system by a user.
         */
        UNV, 
        /**
         * Description: The identifier was not issued by the system responsible for constructing the instance, but the system that captured the id has verified the identifier with the issuing authority, or with another system that has verified the identifier.
         */
        VRF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3IdentifierReliability fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ISS".equals(codeString))
          return ISS;
        if ("UNV".equals(codeString))
          return UNV;
        if ("VRF".equals(codeString))
          return VRF;
        throw new Exception("Unknown V3IdentifierReliability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ISS: return "ISS";
            case UNV: return "UNV";
            case VRF: return "VRF";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/IdentifierReliability";
        }
        public String getDefinition() {
          switch (this) {
            case ISS: return "Description: The identifier was issued by the system responsible for constructing the instance.";
            case UNV: return "Description: The identifier was provided to the system that constructed the instance, but has not been verified. e.g. a Drivers  license entered manually into a system by a user.";
            case VRF: return "Description: The identifier was not issued by the system responsible for constructing the instance, but the system that captured the id has verified the identifier with the issuing authority, or with another system that has verified the identifier.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ISS: return "Issued by System";
            case UNV: return "Unverified by system";
            case VRF: return "Verified by system";
            default: return "?";
          }
    }


}

