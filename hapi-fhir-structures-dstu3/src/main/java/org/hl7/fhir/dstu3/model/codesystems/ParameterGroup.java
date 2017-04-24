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

public enum ParameterGroup {

        /**
         * Haemodynamic Parameter Group - MDC_PGRP_HEMO
         */
        HAEMODYNAMIC, 
        /**
         * ECG Parameter Group - MDC_PGRP_ECG
         */
        ECG, 
        /**
         * Respiratory Parameter Group - MDC_PGRP_RESP
         */
        RESPIRATORY, 
        /**
         * Ventilation Parameter Group - MDC_PGRP_VENT
         */
        VENTILATION, 
        /**
         * Neurological Parameter Group - MDC_PGRP_NEURO
         */
        NEUROLOGICAL, 
        /**
         * Drug Delivery Parameter Group - MDC_PGRP_DRUG
         */
        DRUGDELIVERY, 
        /**
         * Fluid Chemistry Parameter Group - MDC_PGRP_FLUID
         */
        FLUIDCHEMISTRY, 
        /**
         * Blood Chemistry Parameter Group - MDC_PGRP_BLOOD_CHEM
         */
        BLOODCHEMISTRY, 
        /**
         * Miscellaneous Parameter Group - MDC_PGRP_MISC
         */
        MISCELLANEOUS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ParameterGroup fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("haemodynamic".equals(codeString))
          return HAEMODYNAMIC;
        if ("ecg".equals(codeString))
          return ECG;
        if ("respiratory".equals(codeString))
          return RESPIRATORY;
        if ("ventilation".equals(codeString))
          return VENTILATION;
        if ("neurological".equals(codeString))
          return NEUROLOGICAL;
        if ("drug-delivery".equals(codeString))
          return DRUGDELIVERY;
        if ("fluid-chemistry".equals(codeString))
          return FLUIDCHEMISTRY;
        if ("blood-chemistry".equals(codeString))
          return BLOODCHEMISTRY;
        if ("miscellaneous".equals(codeString))
          return MISCELLANEOUS;
        throw new FHIRException("Unknown ParameterGroup code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HAEMODYNAMIC: return "haemodynamic";
            case ECG: return "ecg";
            case RESPIRATORY: return "respiratory";
            case VENTILATION: return "ventilation";
            case NEUROLOGICAL: return "neurological";
            case DRUGDELIVERY: return "drug-delivery";
            case FLUIDCHEMISTRY: return "fluid-chemistry";
            case BLOODCHEMISTRY: return "blood-chemistry";
            case MISCELLANEOUS: return "miscellaneous";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/parameter-group";
        }
        public String getDefinition() {
          switch (this) {
            case HAEMODYNAMIC: return "Haemodynamic Parameter Group - MDC_PGRP_HEMO";
            case ECG: return "ECG Parameter Group - MDC_PGRP_ECG";
            case RESPIRATORY: return "Respiratory Parameter Group - MDC_PGRP_RESP";
            case VENTILATION: return "Ventilation Parameter Group - MDC_PGRP_VENT";
            case NEUROLOGICAL: return "Neurological Parameter Group - MDC_PGRP_NEURO";
            case DRUGDELIVERY: return "Drug Delivery Parameter Group - MDC_PGRP_DRUG";
            case FLUIDCHEMISTRY: return "Fluid Chemistry Parameter Group - MDC_PGRP_FLUID";
            case BLOODCHEMISTRY: return "Blood Chemistry Parameter Group - MDC_PGRP_BLOOD_CHEM";
            case MISCELLANEOUS: return "Miscellaneous Parameter Group - MDC_PGRP_MISC";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HAEMODYNAMIC: return "Haemodynamic Parameter Group";
            case ECG: return "ECG Parameter Group";
            case RESPIRATORY: return "Respiratory Parameter Group";
            case VENTILATION: return "Ventilation Parameter Group";
            case NEUROLOGICAL: return "Neurological Parameter Group";
            case DRUGDELIVERY: return "Drug Delivery Parameter Group";
            case FLUIDCHEMISTRY: return "Fluid Chemistry Parameter Group";
            case BLOODCHEMISTRY: return "Blood Chemistry Parameter Group";
            case MISCELLANEOUS: return "Miscellaneous Parameter Group";
            default: return "?";
          }
    }


}

