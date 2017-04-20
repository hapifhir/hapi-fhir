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

public enum SystemVersionProcessingMode {

        /**
         * Use this version of the code system if a value set doesn't specify a version
         */
        DEFAULT, 
        /**
         * Use this version of the code system. If a value set specifies a different version, the expansion operation should fail
         */
        CHECK, 
        /**
         * Use this version of the code system irrespective of which version is specified by a value set. Note that this has obvious safety issues, in that it may result in a value set expansion giving a different list of codes that is both wrong and unsafe, and implementers should only use this capability reluctantly. It primarily exists to deal with situations where specifications have fallen into decay as time passes. If a  version is override, the version used SHALL explicitly be represented in the expansion parameters
         */
        OVERRIDE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SystemVersionProcessingMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("default".equals(codeString))
          return DEFAULT;
        if ("check".equals(codeString))
          return CHECK;
        if ("override".equals(codeString))
          return OVERRIDE;
        throw new FHIRException("Unknown SystemVersionProcessingMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DEFAULT: return "default";
            case CHECK: return "check";
            case OVERRIDE: return "override";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/system-version-processing-mode";
        }
        public String getDefinition() {
          switch (this) {
            case DEFAULT: return "Use this version of the code system if a value set doesn't specify a version";
            case CHECK: return "Use this version of the code system. If a value set specifies a different version, the expansion operation should fail";
            case OVERRIDE: return "Use this version of the code system irrespective of which version is specified by a value set. Note that this has obvious safety issues, in that it may result in a value set expansion giving a different list of codes that is both wrong and unsafe, and implementers should only use this capability reluctantly. It primarily exists to deal with situations where specifications have fallen into decay as time passes. If a  version is override, the version used SHALL explicitly be represented in the expansion parameters";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DEFAULT: return "Default Version";
            case CHECK: return "Check ValueSet Version";
            case OVERRIDE: return "Override ValueSet Version";
            default: return "?";
          }
    }


}

