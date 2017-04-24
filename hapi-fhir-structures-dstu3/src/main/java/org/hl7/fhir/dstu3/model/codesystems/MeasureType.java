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

public enum MeasureType {

        /**
         * A measure which focuses on a process which leads to a certain outcome, meaning that a scientific basis exists for believing that the process, when executed well, will increase the probability of achieving a desired outcome
         */
        PROCESS, 
        /**
         * A measure that indicates the result of the performance (or non-performance) of a function or process
         */
        OUTCOME, 
        /**
         * A measure that focuses on a health care provider's capacity, systems, and processes to provide high-quality care
         */
        STRUCTURE, 
        /**
         * A measure that focuses on patient-reported information such as patient engagement or patient experience measures
         */
        PATIENTREPORTEDOUTCOME, 
        /**
         * A measure that combines multiple component measures in to a single quality measure
         */
        COMPOSITE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasureType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("process".equals(codeString))
          return PROCESS;
        if ("outcome".equals(codeString))
          return OUTCOME;
        if ("structure".equals(codeString))
          return STRUCTURE;
        if ("patient-reported-outcome".equals(codeString))
          return PATIENTREPORTEDOUTCOME;
        if ("composite".equals(codeString))
          return COMPOSITE;
        throw new FHIRException("Unknown MeasureType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROCESS: return "process";
            case OUTCOME: return "outcome";
            case STRUCTURE: return "structure";
            case PATIENTREPORTEDOUTCOME: return "patient-reported-outcome";
            case COMPOSITE: return "composite";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/measure-type";
        }
        public String getDefinition() {
          switch (this) {
            case PROCESS: return "A measure which focuses on a process which leads to a certain outcome, meaning that a scientific basis exists for believing that the process, when executed well, will increase the probability of achieving a desired outcome";
            case OUTCOME: return "A measure that indicates the result of the performance (or non-performance) of a function or process";
            case STRUCTURE: return "A measure that focuses on a health care provider's capacity, systems, and processes to provide high-quality care";
            case PATIENTREPORTEDOUTCOME: return "A measure that focuses on patient-reported information such as patient engagement or patient experience measures";
            case COMPOSITE: return "A measure that combines multiple component measures in to a single quality measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROCESS: return "Process";
            case OUTCOME: return "Outcome";
            case STRUCTURE: return "Structure";
            case PATIENTREPORTEDOUTCOME: return "Patient Reported Outcome";
            case COMPOSITE: return "Composite";
            default: return "?";
          }
    }


}

