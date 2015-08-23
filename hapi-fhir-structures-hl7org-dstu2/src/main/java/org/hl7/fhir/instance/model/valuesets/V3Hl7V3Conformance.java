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


public enum V3Hl7V3Conformance {

        /**
         * Description: Implementers receiving this property must not raise an error if the data is received, but will not perform any useful function with the data.  This conformance level is not used in profiles or other artifacts that are specific to the "sender" or "initiator" of a communication.
         */
        I, 
        /**
         * Description: All implementers are prohibited from transmitting this content, and may raise an error if they receive it.
         */
        NP, 
        /**
         * Description: All implementers must support this property.  I.e. they must be able to transmit, or to receive and usefully handle the concept.
         */
        R, 
        /**
         * Description: The element is considered "required" (i.e. must be supported) from the perspective of systems that consume  instances, but is "undetermined" for systems that generate instances.  Used only as part of specifications that define both initiator and consumer expectations.
         */
        RC, 
        /**
         * Description: The element is considered "required" (i.e. must be supported) from the perspective of systems that generate instances, but is "undetermined" for systems that consume instances.  Used only as part of specifications that define both initiator and consumer expectations.
         */
        RI, 
        /**
         * Description: The conformance expectations for this element have not yet been determined.
         */
        U, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7V3Conformance fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("I".equals(codeString))
          return I;
        if ("NP".equals(codeString))
          return NP;
        if ("R".equals(codeString))
          return R;
        if ("RC".equals(codeString))
          return RC;
        if ("RI".equals(codeString))
          return RI;
        if ("U".equals(codeString))
          return U;
        throw new Exception("Unknown V3Hl7V3Conformance code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case I: return "I";
            case NP: return "NP";
            case R: return "R";
            case RC: return "RC";
            case RI: return "RI";
            case U: return "U";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/hl7V3Conformance";
        }
        public String getDefinition() {
          switch (this) {
            case I: return "Description: Implementers receiving this property must not raise an error if the data is received, but will not perform any useful function with the data.  This conformance level is not used in profiles or other artifacts that are specific to the \"sender\" or \"initiator\" of a communication.";
            case NP: return "Description: All implementers are prohibited from transmitting this content, and may raise an error if they receive it.";
            case R: return "Description: All implementers must support this property.  I.e. they must be able to transmit, or to receive and usefully handle the concept.";
            case RC: return "Description: The element is considered \"required\" (i.e. must be supported) from the perspective of systems that consume  instances, but is \"undetermined\" for systems that generate instances.  Used only as part of specifications that define both initiator and consumer expectations.";
            case RI: return "Description: The element is considered \"required\" (i.e. must be supported) from the perspective of systems that generate instances, but is \"undetermined\" for systems that consume instances.  Used only as part of specifications that define both initiator and consumer expectations.";
            case U: return "Description: The conformance expectations for this element have not yet been determined.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case I: return "ignored";
            case NP: return "not permitted";
            case R: return "required";
            case RC: return "required for consumer";
            case RI: return "required for initiator";
            case U: return "undetermined";
            default: return "?";
          }
    }


}

