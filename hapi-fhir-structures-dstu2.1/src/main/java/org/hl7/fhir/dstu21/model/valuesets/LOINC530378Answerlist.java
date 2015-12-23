package org.hl7.fhir.dstu21.model.valuesets;

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

// Generated on Mon, Dec 21, 2015 19:58-0500 for FHIR v1.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum LOINC530378Answerlist {

        /**
         * Pathogenic
         */
        LA66683, 
        /**
         * Presumed pathogenic
         */
        LA66691, 
        /**
         * Unknown significance
         */
        LA66824, 
        /**
         * Benign
         */
        LA66758, 
        /**
         * Presumed benign
         */
        LA66741, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC530378Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA6668-3".equals(codeString))
          return LA66683;
        if ("LA6669-1".equals(codeString))
          return LA66691;
        if ("LA6682-4".equals(codeString))
          return LA66824;
        if ("LA6675-8".equals(codeString))
          return LA66758;
        if ("LA6674-1".equals(codeString))
          return LA66741;
        throw new FHIRException("Unknown LOINC530378Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA66683: return "LA6668-3";
            case LA66691: return "LA6669-1";
            case LA66824: return "LA6682-4";
            case LA66758: return "LA6675-8";
            case LA66741: return "LA6674-1";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-53037-8-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA66683: return "Pathogenic";
            case LA66691: return "Presumed pathogenic";
            case LA66824: return "Unknown significance";
            case LA66758: return "Benign";
            case LA66741: return "Presumed benign";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA66683: return "Pathogenic";
            case LA66691: return "Presumed pathogenic";
            case LA66824: return "Unknown significance";
            case LA66758: return "Benign";
            case LA66741: return "Presumed benign";
            default: return "?";
          }
    }


}

