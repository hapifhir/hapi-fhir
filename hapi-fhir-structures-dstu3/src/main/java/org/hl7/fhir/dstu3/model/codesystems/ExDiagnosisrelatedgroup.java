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

public enum ExDiagnosisrelatedgroup {

        /**
         * Normal Vaginal Delivery.
         */
        _100, 
        /**
         * Appendectomy without rupture or other complications.
         */
        _101, 
        /**
         * Emergency department treatment of a tooth abcess.
         */
        _300, 
        /**
         * Head trauma - concussion.
         */
        _400, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExDiagnosisrelatedgroup fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("100".equals(codeString))
          return _100;
        if ("101".equals(codeString))
          return _101;
        if ("300".equals(codeString))
          return _300;
        if ("400".equals(codeString))
          return _400;
        throw new FHIRException("Unknown ExDiagnosisrelatedgroup code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _100: return "100";
            case _101: return "101";
            case _300: return "300";
            case _400: return "400";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-diagnosisrelatedgroup";
        }
        public String getDefinition() {
          switch (this) {
            case _100: return "Normal Vaginal Delivery.";
            case _101: return "Appendectomy without rupture or other complications.";
            case _300: return "Emergency department treatment of a tooth abcess.";
            case _400: return "Head trauma - concussion.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _100: return "Normal Vaginal Delivery";
            case _101: return "Appendectomy - uncomplicated";
            case _300: return "Tooth abcess";
            case _400: return "Head trauma - concussion";
            default: return "?";
          }
    }


}

