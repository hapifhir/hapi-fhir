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


public enum V3EntityNamePartTypeR2 {

        /**
         * Description:A delimiter has no meaning other than being literally printed in this name representation. A delimiter has no implicit leading and trailing white space.
         */
        DEL, 
        /**
         * Description:Family name, this is the name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
         */
        FAM, 
        /**
         * Description:Given name. Note: don't call it "first name" since the given names do not always come first.
         */
        GIV, 
        /**
         * Description:Part of the name that is acquired as a title due to academic, legal, employment or nobility status etc.

                        
                           Note:Title name parts include name parts that come after the name such as qualifications, even if they are not always considered to be titles.
         */
        TITLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNamePartTypeR2 fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("DEL".equals(codeString))
          return DEL;
        if ("FAM".equals(codeString))
          return FAM;
        if ("GIV".equals(codeString))
          return GIV;
        if ("TITLE".equals(codeString))
          return TITLE;
        throw new Exception("Unknown V3EntityNamePartTypeR2 code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DEL: return "DEL";
            case FAM: return "FAM";
            case GIV: return "GIV";
            case TITLE: return "TITLE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNamePartTypeR2";
        }
        public String getDefinition() {
          switch (this) {
            case DEL: return "Description:A delimiter has no meaning other than being literally printed in this name representation. A delimiter has no implicit leading and trailing white space.";
            case FAM: return "Description:Family name, this is the name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.";
            case GIV: return "Description:Given name. Note: don't call it \"first name\" since the given names do not always come first.";
            case TITLE: return "Description:Part of the name that is acquired as a title due to academic, legal, employment or nobility status etc.\r\n\n                        \n                           Note:Title name parts include name parts that come after the name such as qualifications, even if they are not always considered to be titles.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DEL: return "delimiter";
            case FAM: return "family";
            case GIV: return "given";
            case TITLE: return "title";
            default: return "?";
          }
    }


}

