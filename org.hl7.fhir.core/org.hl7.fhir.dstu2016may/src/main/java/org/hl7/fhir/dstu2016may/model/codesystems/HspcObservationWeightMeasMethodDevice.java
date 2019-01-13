package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum HspcObservationWeightMeasMethodDevice {

        /**
         * A scale that measures the weight of a patient by measuring the total mass of a bed and patient, then subtracts the known mass of the bed.
         */
        _1964, 
        /**
         * A scale that measures the weight of a patient by measuring the total mass of a chair and patient, then subtracts the known mass of the chair.
         */
        _2007, 
        /**
         * A scale for patients that are unable to stand, patients sit in a sling attached to a boom.
         */
        _2139, 
        /**
         * A scale upon which an individual will stand to measure body weight
         */
        _2142, 
        /**
         * A measurement of lean body weight made by immersing an individual in water and then measuring the mass of the displacement.
         */
        _2180, 
        /**
         * A measurement of lean body weight made by measuring the thickness of a fold of skin.
         */
        _2181, 
        /**
         * A body weight based on other measurements plotted on a percentile chart.
         */
        _2185, 
        /**
         * A scale designed for use with infants only
         */
        _50550851, 
        /**
         * A measuring tape created to relate a child's height, as measured by the tape to the child's weight
         */
        _521443011, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcObservationWeightMeasMethodDevice fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1964".equals(codeString))
          return _1964;
        if ("2007".equals(codeString))
          return _2007;
        if ("2139".equals(codeString))
          return _2139;
        if ("2142".equals(codeString))
          return _2142;
        if ("2180".equals(codeString))
          return _2180;
        if ("2181".equals(codeString))
          return _2181;
        if ("2185".equals(codeString))
          return _2185;
        if ("50550851".equals(codeString))
          return _50550851;
        if ("521443011".equals(codeString))
          return _521443011;
        throw new FHIRException("Unknown HspcObservationWeightMeasMethodDevice code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1964: return "1964";
            case _2007: return "2007";
            case _2139: return "2139";
            case _2142: return "2142";
            case _2180: return "2180";
            case _2181: return "2181";
            case _2185: return "2185";
            case _50550851: return "50550851";
            case _521443011: return "521443011";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7,org/fhir/observation-hspc-weightMeasMethodDevice";
        }
        public String getDefinition() {
          switch (this) {
            case _1964: return "A scale that measures the weight of a patient by measuring the total mass of a bed and patient, then subtracts the known mass of the bed.";
            case _2007: return "A scale that measures the weight of a patient by measuring the total mass of a chair and patient, then subtracts the known mass of the chair.";
            case _2139: return "A scale for patients that are unable to stand, patients sit in a sling attached to a boom.";
            case _2142: return "A scale upon which an individual will stand to measure body weight";
            case _2180: return "A measurement of lean body weight made by immersing an individual in water and then measuring the mass of the displacement.";
            case _2181: return "A measurement of lean body weight made by measuring the thickness of a fold of skin.";
            case _2185: return "A body weight based on other measurements plotted on a percentile chart.";
            case _50550851: return "A scale designed for use with infants only";
            case _521443011: return "A measuring tape created to relate a child's height, as measured by the tape to the child's weight";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1964: return "Bed scale";
            case _2007: return "Chair scale";
            case _2139: return "Sling scale";
            case _2142: return "Standing scale";
            case _2180: return "Weight, lean; immersion";
            case _2181: return "Weight, lean; skin fold";
            case _2185: return "Weight, percentile";
            case _50550851: return "Infant Scale";
            case _521443011: return "Broselow tape";
            default: return "?";
          }
    }


}

