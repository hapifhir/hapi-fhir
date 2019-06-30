package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum EffectEstimateType {

        /**
         * relative risk (a type of relative effect estimate).
         */
        RELATIVERR, 
        /**
         * odds ratio (a type of relative effect estimate).
         */
        RELATIVEOR, 
        /**
         * hazard ratio (a type of relative effect estimate).
         */
        RELATIVEHR, 
        /**
         * absolute risk difference (a type of absolute effect estimate).
         */
        ABSOLUTEARD, 
        /**
         * mean difference (a type of absolute effect estimate).
         */
        ABSOLUTEMEANDIFF, 
        /**
         * standardized mean difference (a type of absolute effect estimate).
         */
        ABSOLUTESMD, 
        /**
         * median difference (a type of absolute effect estimate).
         */
        ABSOLUTEMEDIANDIFF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EffectEstimateType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("relative-RR".equals(codeString))
          return RELATIVERR;
        if ("relative-OR".equals(codeString))
          return RELATIVEOR;
        if ("relative-HR".equals(codeString))
          return RELATIVEHR;
        if ("absolute-ARD".equals(codeString))
          return ABSOLUTEARD;
        if ("absolute-MeanDiff".equals(codeString))
          return ABSOLUTEMEANDIFF;
        if ("absolute-SMD".equals(codeString))
          return ABSOLUTESMD;
        if ("absolute-MedianDiff".equals(codeString))
          return ABSOLUTEMEDIANDIFF;
        throw new FHIRException("Unknown EffectEstimateType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RELATIVERR: return "relative-RR";
            case RELATIVEOR: return "relative-OR";
            case RELATIVEHR: return "relative-HR";
            case ABSOLUTEARD: return "absolute-ARD";
            case ABSOLUTEMEANDIFF: return "absolute-MeanDiff";
            case ABSOLUTESMD: return "absolute-SMD";
            case ABSOLUTEMEDIANDIFF: return "absolute-MedianDiff";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/effect-estimate-type";
        }
        public String getDefinition() {
          switch (this) {
            case RELATIVERR: return "relative risk (a type of relative effect estimate).";
            case RELATIVEOR: return "odds ratio (a type of relative effect estimate).";
            case RELATIVEHR: return "hazard ratio (a type of relative effect estimate).";
            case ABSOLUTEARD: return "absolute risk difference (a type of absolute effect estimate).";
            case ABSOLUTEMEANDIFF: return "mean difference (a type of absolute effect estimate).";
            case ABSOLUTESMD: return "standardized mean difference (a type of absolute effect estimate).";
            case ABSOLUTEMEDIANDIFF: return "median difference (a type of absolute effect estimate).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RELATIVERR: return "relative risk";
            case RELATIVEOR: return "odds ratio";
            case RELATIVEHR: return "hazard ratio";
            case ABSOLUTEARD: return "absolute risk difference";
            case ABSOLUTEMEANDIFF: return "mean difference";
            case ABSOLUTESMD: return "standardized mean difference";
            case ABSOLUTEMEDIANDIFF: return "median difference";
            default: return "?";
          }
    }


}

