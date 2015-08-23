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


public enum V3EntityNamePartType {

        /**
         * A delimiter has no meaning other than being literally printed in this name representation.  A delimiter has no implicit leading and trailing white space.
         */
        DEL, 
        /**
         * Family name, this is the name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
         */
        FAM, 
        /**
         * Given name (don't call it "first name" since this given names do not always come first)
         */
        GIV, 
        /**
         * A prefix has a strong association to the immediately following name part. A prefix has no implicit trailing white space (it has implicit leading white space though). Note that prefixes can be inverted.
         */
        PFX, 
        /**
         * Description:A suffix has a strong association to the immediately preceding name part. A suffix has no implicit leading white space (it has implicit trailing white space though). Suffices cannot be inverted.
         */
        SFX, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNamePartType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("DEL".equals(codeString))
          return DEL;
        if ("FAM".equals(codeString))
          return FAM;
        if ("GIV".equals(codeString))
          return GIV;
        if ("PFX".equals(codeString))
          return PFX;
        if ("SFX".equals(codeString))
          return SFX;
        throw new Exception("Unknown V3EntityNamePartType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DEL: return "DEL";
            case FAM: return "FAM";
            case GIV: return "GIV";
            case PFX: return "PFX";
            case SFX: return "SFX";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNamePartType";
        }
        public String getDefinition() {
          switch (this) {
            case DEL: return "A delimiter has no meaning other than being literally printed in this name representation.  A delimiter has no implicit leading and trailing white space.";
            case FAM: return "Family name, this is the name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.";
            case GIV: return "Given name (don't call it \"first name\" since this given names do not always come first)";
            case PFX: return "A prefix has a strong association to the immediately following name part. A prefix has no implicit trailing white space (it has implicit leading white space though). Note that prefixes can be inverted.";
            case SFX: return "Description:A suffix has a strong association to the immediately preceding name part. A suffix has no implicit leading white space (it has implicit trailing white space though). Suffices cannot be inverted.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DEL: return "delimiter";
            case FAM: return "family";
            case GIV: return "given";
            case PFX: return "prefix";
            case SFX: return "suffix";
            default: return "?";
          }
    }


}

