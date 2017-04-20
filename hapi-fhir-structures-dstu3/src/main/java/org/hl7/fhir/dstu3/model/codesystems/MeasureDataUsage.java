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

public enum MeasureDataUsage {

        /**
         * The data is intended to be provided as additional information alongside the measure results
         */
        SUPPLEMENTALDATA, 
        /**
         * The data is intended to be used to calculate and apply a risk adjustment model for the measure
         */
        RISKADJUSTMENTFACTOR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasureDataUsage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("supplemental-data".equals(codeString))
          return SUPPLEMENTALDATA;
        if ("risk-adjustment-factor".equals(codeString))
          return RISKADJUSTMENTFACTOR;
        throw new FHIRException("Unknown MeasureDataUsage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUPPLEMENTALDATA: return "supplemental-data";
            case RISKADJUSTMENTFACTOR: return "risk-adjustment-factor";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/measure-data-usage";
        }
        public String getDefinition() {
          switch (this) {
            case SUPPLEMENTALDATA: return "The data is intended to be provided as additional information alongside the measure results";
            case RISKADJUSTMENTFACTOR: return "The data is intended to be used to calculate and apply a risk adjustment model for the measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUPPLEMENTALDATA: return "Supplemental Data";
            case RISKADJUSTMENTFACTOR: return "Risk Adjustment Factor";
            default: return "?";
          }
    }


}

