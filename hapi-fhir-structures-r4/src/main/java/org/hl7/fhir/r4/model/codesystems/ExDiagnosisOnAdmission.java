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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ExDiagnosisOnAdmission {

        /**
         * Diagnosis was present at time of inpatient admission.
         */
        Y, 
        /**
         * Diagnosis was not present at time of inpatient admission.
         */
        N, 
        /**
         * Documentation insufficient to determine if condition was present at the time of inpatient admission.
         */
        U, 
        /**
         * Clinically undetermined. Provider unable to clinically determine whether the condition was present at the time of inpatient admission.
         */
        W, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExDiagnosisOnAdmission fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("y".equals(codeString))
          return Y;
        if ("n".equals(codeString))
          return N;
        if ("u".equals(codeString))
          return U;
        if ("w".equals(codeString))
          return W;
        throw new FHIRException("Unknown ExDiagnosisOnAdmission code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case Y: return "y";
            case N: return "n";
            case U: return "u";
            case W: return "w";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/ex-diagnosis-on-admission";
        }
        public String getDefinition() {
          switch (this) {
            case Y: return "Diagnosis was present at time of inpatient admission.";
            case N: return "Diagnosis was not present at time of inpatient admission.";
            case U: return "Documentation insufficient to determine if condition was present at the time of inpatient admission.";
            case W: return "Clinically undetermined. Provider unable to clinically determine whether the condition was present at the time of inpatient admission.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case Y: return "Yes";
            case N: return "No";
            case U: return "Unknown";
            case W: return "Undetermined";
            default: return "?";
          }
    }


}

