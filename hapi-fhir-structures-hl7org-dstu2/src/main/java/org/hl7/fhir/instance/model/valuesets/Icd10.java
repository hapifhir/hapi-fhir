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

// Generated on Thu, Jul 23, 2015 16:50-0400 for FHIR v0.5.0


public enum Icd10 {

        /**
         * DIAG-1
         */
        _123456, 
        /**
         * DIAG-1a
         */
        _123457, 
        /**
         * DIAG-2
         */
        _987654, 
        /**
         * DIAG-3
         */
        _123987, 
        /**
         * DIAG-4
         */
        _112233, 
        /**
         * DIAG-5
         */
        _997755, 
        /**
         * DIAG-6
         */
        _321789, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Icd10 fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("123456".equals(codeString))
          return _123456;
        if ("123457".equals(codeString))
          return _123457;
        if ("987654".equals(codeString))
          return _987654;
        if ("123987".equals(codeString))
          return _123987;
        if ("112233".equals(codeString))
          return _112233;
        if ("997755".equals(codeString))
          return _997755;
        if ("321789".equals(codeString))
          return _321789;
        throw new Exception("Unknown Icd10 code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _123456: return "123456";
            case _123457: return "123457";
            case _987654: return "987654";
            case _123987: return "123987";
            case _112233: return "112233";
            case _997755: return "997755";
            case _321789: return "321789";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/sid/icd-10";
        }
        public String getDefinition() {
          switch (this) {
            case _123456: return "DIAG-1";
            case _123457: return "DIAG-1a";
            case _987654: return "DIAG-2";
            case _123987: return "DIAG-3";
            case _112233: return "DIAG-4";
            case _997755: return "DIAG-5";
            case _321789: return "DIAG-6";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _123456: return "DIAG-1";
            case _123457: return "DIAG-1a";
            case _987654: return "DIAG-2";
            case _123987: return "DIAG-3";
            case _112233: return "DIAG-4";
            case _997755: return "DIAG-5";
            case _321789: return "DIAG-6";
            default: return "?";
          }
    }


}

