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

public enum MetricCalibrationState {

        /**
         * The metric has not been calibrated.
         */
        NOTCALIBRATED, 
        /**
         * The metric needs to be calibrated.
         */
        CALIBRATIONREQUIRED, 
        /**
         * The metric has been calibrated.
         */
        CALIBRATED, 
        /**
         * The state of calibration of this metric is unspecified.
         */
        UNSPECIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MetricCalibrationState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-calibrated".equals(codeString))
          return NOTCALIBRATED;
        if ("calibration-required".equals(codeString))
          return CALIBRATIONREQUIRED;
        if ("calibrated".equals(codeString))
          return CALIBRATED;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        throw new FHIRException("Unknown MetricCalibrationState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTCALIBRATED: return "not-calibrated";
            case CALIBRATIONREQUIRED: return "calibration-required";
            case CALIBRATED: return "calibrated";
            case UNSPECIFIED: return "unspecified";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/metric-calibration-state";
        }
        public String getDefinition() {
          switch (this) {
            case NOTCALIBRATED: return "The metric has not been calibrated.";
            case CALIBRATIONREQUIRED: return "The metric needs to be calibrated.";
            case CALIBRATED: return "The metric has been calibrated.";
            case UNSPECIFIED: return "The state of calibration of this metric is unspecified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTCALIBRATED: return "Not Calibrated";
            case CALIBRATIONREQUIRED: return "Calibration Required";
            case CALIBRATED: return "Calibrated";
            case UNSPECIFIED: return "Unspecified";
            default: return "?";
          }
    }


}

