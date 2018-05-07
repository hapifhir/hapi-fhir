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

// Generated on Sun, Mar 12, 2017 20:35-0400 for FHIR v1.9.0


import org.hl7.fhir.exceptions.FHIRException;

public enum LOINC480194Answerlist {

        /**
         * Wild type
         */
        LA96581, 
        /**
         * Deletion
         */
        LA66923, 
        /**
         * Duplication
         */
        LA66865, 
        /**
         * Insertion
         */
        LA66873, 
        /**
         * Insertion/Deletion
         */
        LA66881, 
        /**
         * Inversion
         */
        LA66899, 
        /**
         * Substitution
         */
        LA66907, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC480194Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA9658-1".equals(codeString))
          return LA96581;
        if ("LA6692-3".equals(codeString))
          return LA66923;
        if ("LA6686-5".equals(codeString))
          return LA66865;
        if ("LA6687-3".equals(codeString))
          return LA66873;
        if ("LA6688-1".equals(codeString))
          return LA66881;
        if ("LA6689-9".equals(codeString))
          return LA66899;
        if ("LA6690-7".equals(codeString))
          return LA66907;
        throw new FHIRException("Unknown LOINC480194Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA96581: return "LA9658-1";
            case LA66923: return "LA6692-3";
            case LA66865: return "LA6686-5";
            case LA66873: return "LA6687-3";
            case LA66881: return "LA6688-1";
            case LA66899: return "LA6689-9";
            case LA66907: return "LA6690-7";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-48019-4-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA96581: return "Wild type";
            case LA66923: return "Deletion";
            case LA66865: return "Duplication";
            case LA66873: return "Insertion";
            case LA66881: return "Insertion/Deletion";
            case LA66899: return "Inversion";
            case LA66907: return "Substitution";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA96581: return "Wild type";
            case LA66923: return "Deletion";
            case LA66865: return "Duplication";
            case LA66873: return "Insertion";
            case LA66881: return "Insertion/Deletion";
            case LA66899: return "Inversion";
            case LA66907: return "Substitution";
            default: return "?";
          }
    }


}

