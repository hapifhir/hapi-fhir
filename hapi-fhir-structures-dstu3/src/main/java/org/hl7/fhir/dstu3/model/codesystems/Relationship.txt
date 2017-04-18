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

public enum Relationship {

        /**
         * The patient is the subscriber (policy holder)
         */
        _1, 
        /**
         * The patient is the spouse or equivalent of the subscriber (policy holder)
         */
        _2, 
        /**
         * The patient is the child of the subscriber (policy holder)
         */
        _3, 
        /**
         * The patient is the common law spouse of the subscriber (policy holder)
         */
        _4, 
        /**
         * The patient has some other relationship, such as parent, to the subscriber (policy holder)
         */
        _5, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Relationship fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        throw new FHIRException("Unknown Relationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/relationship";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "The patient is the subscriber (policy holder)";
            case _2: return "The patient is the spouse or equivalent of the subscriber (policy holder)";
            case _3: return "The patient is the child of the subscriber (policy holder)";
            case _4: return "The patient is the common law spouse of the subscriber (policy holder)";
            case _5: return "The patient has some other relationship, such as parent, to the subscriber (policy holder)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Self";
            case _2: return "Spouse";
            case _3: return "Child";
            case _4: return "Common Law Spouse";
            case _5: return "Other";
            default: return "?";
          }
    }


}

