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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ResearchStudyPrimPurpType {

        /**
         * One or more interventions are being evaluated for treating a disease, syndrome, or condition.
         */
        TREATMENT, 
        /**
         * One or more interventions are being assessed for preventing the development of a specific disease or health condition.
         */
        PREVENTION, 
        /**
         * One or more interventions are being evaluated for identifying a disease or health condition.
         */
        DIAGNOSTIC, 
        /**
         * One or more interventions are evaluated for maximizing comfort, minimizing side effects, or mitigating against a decline in the participant's health or function.
         */
        SUPPORTIVECARE, 
        /**
         * One or more interventions are assessed or examined for identifying a condition, or risk factors for a condition, in people who are not yet known to have the condition or risk factor.
         */
        SCREENING, 
        /**
         * One or more interventions for evaluating the delivery, processes, management, organization, or financing of healthcare.
         */
        HEALTHSERVICESRESEARCH, 
        /**
         * One or more interventions for examining the basic mechanism of action (for example, physiology or biomechanics of an intervention).
         */
        BASICSCIENCE, 
        /**
         * An intervention of a device product is being evaluated to determine the feasibility of the product or to test a prototype device and not health outcomes. Such studies are conducted to confirm the design and operating specifications of a device before beginning a full clinical trial.
         */
        DEVICEFEASIBILITY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchStudyPrimPurpType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("treatment".equals(codeString))
          return TREATMENT;
        if ("prevention".equals(codeString))
          return PREVENTION;
        if ("diagnostic".equals(codeString))
          return DIAGNOSTIC;
        if ("supportive-care".equals(codeString))
          return SUPPORTIVECARE;
        if ("screening".equals(codeString))
          return SCREENING;
        if ("health-services-research".equals(codeString))
          return HEALTHSERVICESRESEARCH;
        if ("basic-science".equals(codeString))
          return BASICSCIENCE;
        if ("device-feasibility".equals(codeString))
          return DEVICEFEASIBILITY;
        throw new FHIRException("Unknown ResearchStudyPrimPurpType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TREATMENT: return "treatment";
            case PREVENTION: return "prevention";
            case DIAGNOSTIC: return "diagnostic";
            case SUPPORTIVECARE: return "supportive-care";
            case SCREENING: return "screening";
            case HEALTHSERVICESRESEARCH: return "health-services-research";
            case BASICSCIENCE: return "basic-science";
            case DEVICEFEASIBILITY: return "device-feasibility";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-study-prim-purp-type";
        }
        public String getDefinition() {
          switch (this) {
            case TREATMENT: return "One or more interventions are being evaluated for treating a disease, syndrome, or condition.";
            case PREVENTION: return "One or more interventions are being assessed for preventing the development of a specific disease or health condition.";
            case DIAGNOSTIC: return "One or more interventions are being evaluated for identifying a disease or health condition.";
            case SUPPORTIVECARE: return "One or more interventions are evaluated for maximizing comfort, minimizing side effects, or mitigating against a decline in the participant's health or function.";
            case SCREENING: return "One or more interventions are assessed or examined for identifying a condition, or risk factors for a condition, in people who are not yet known to have the condition or risk factor.";
            case HEALTHSERVICESRESEARCH: return "One or more interventions for evaluating the delivery, processes, management, organization, or financing of healthcare.";
            case BASICSCIENCE: return "One or more interventions for examining the basic mechanism of action (for example, physiology or biomechanics of an intervention).";
            case DEVICEFEASIBILITY: return "An intervention of a device product is being evaluated to determine the feasibility of the product or to test a prototype device and not health outcomes. Such studies are conducted to confirm the design and operating specifications of a device before beginning a full clinical trial.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TREATMENT: return "Treatment";
            case PREVENTION: return "Prevention";
            case DIAGNOSTIC: return "Diagnostic";
            case SUPPORTIVECARE: return "Supportive Care";
            case SCREENING: return "Screening";
            case HEALTHSERVICESRESEARCH: return "Health Services Research";
            case BASICSCIENCE: return "Basic Science";
            case DEVICEFEASIBILITY: return "Device Feasibility";
            default: return "?";
          }
    }


}

