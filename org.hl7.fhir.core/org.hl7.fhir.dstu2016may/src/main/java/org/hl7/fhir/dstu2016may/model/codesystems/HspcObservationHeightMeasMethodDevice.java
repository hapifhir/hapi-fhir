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

public enum HspcObservationHeightMeasMethodDevice {

        /**
         * The method of determining body length or height where it is guessed at by the health care provider
         */
        _2057, 
        /**
         * A method of determining body length or height by utilizing a device to measure it.
         */
        _2058, 
        /**
         * A method of determining body length or height by using a growth percentile chart
         */
        _2061, 
        /**
         * The body length or height was given verbally rather than measured.
         */
        _2063, 
        /**
         * A device for measuring body length or height.  Usually constructed of a ruler and a sliding headpiece that, when placed on an individual's head will give a reading of the body length or height.
         */
        _521363688, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcObservationHeightMeasMethodDevice fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("2057".equals(codeString))
          return _2057;
        if ("2058".equals(codeString))
          return _2058;
        if ("2061".equals(codeString))
          return _2061;
        if ("2063".equals(codeString))
          return _2063;
        if ("521363688".equals(codeString))
          return _521363688;
        throw new FHIRException("Unknown HspcObservationHeightMeasMethodDevice code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _2057: return "2057";
            case _2058: return "2058";
            case _2061: return "2061";
            case _2063: return "2063";
            case _521363688: return "521363688";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7,org/fhir/valueset-observation-heightMeasMethodDevice";
        }
        public String getDefinition() {
          switch (this) {
            case _2057: return "The method of determining body length or height where it is guessed at by the health care provider";
            case _2058: return "A method of determining body length or height by utilizing a device to measure it.";
            case _2061: return "A method of determining body length or height by using a growth percentile chart";
            case _2063: return "The body length or height was given verbally rather than measured.";
            case _521363688: return "A device for measuring body length or height.  Usually constructed of a ruler and a sliding headpiece that, when placed on an individual's head will give a reading of the body length or height.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _2057: return "Height, estimated by provider";
            case _2058: return "Height, measured";
            case _2061: return "Height, percentile";
            case _2063: return "Height, stated (reported)";
            case _521363688: return "Stadiometer";
            default: return "?";
          }
    }


}

