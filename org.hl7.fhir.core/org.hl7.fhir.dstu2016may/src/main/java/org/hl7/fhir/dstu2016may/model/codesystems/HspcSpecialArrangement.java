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

public enum HspcSpecialArrangement {

        /**
         * A device, comprising a chair with wheels, used for individuals that cannot walk.
         */
        _182850, 
        /**
         * An individual that translates spoken language to a language that is understood by the receiver.
         */
        _528123036, 
        /**
         * A device used to convey an individual from one place to another where the individual is in a lying position and is carried by others.
         */
        _182847, 
        /**
         * A canine that has been trained to lead an idividual that is blind.
         */
        _528123037, 
        /**
         * A suuplimental substance given to patients who cannot obtain appropriate level of environmental oxygen through natural breathing.
         */
        _30719, 
        /**
         * A device that monitors an idividual's heart rate.
         */
        _528123038, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcSpecialArrangement fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("182850".equals(codeString))
          return _182850;
        if ("528123036".equals(codeString))
          return _528123036;
        if ("182847".equals(codeString))
          return _182847;
        if ("528123037".equals(codeString))
          return _528123037;
        if ("30719".equals(codeString))
          return _30719;
        if ("528123038".equals(codeString))
          return _528123038;
        throw new FHIRException("Unknown HspcSpecialArrangement code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _182850: return "182850";
            case _528123036: return "528123036";
            case _182847: return "182847";
            case _528123037: return "528123037";
            case _30719: return "30719";
            case _528123038: return "528123038";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/hspc-specialArrangement";
        }
        public String getDefinition() {
          switch (this) {
            case _182850: return "A device, comprising a chair with wheels, used for individuals that cannot walk.";
            case _528123036: return "An individual that translates spoken language to a language that is understood by the receiver.";
            case _182847: return "A device used to convey an individual from one place to another where the individual is in a lying position and is carried by others.";
            case _528123037: return "A canine that has been trained to lead an idividual that is blind.";
            case _30719: return "A suuplimental substance given to patients who cannot obtain appropriate level of environmental oxygen through natural breathing.";
            case _528123038: return "A device that monitors an idividual's heart rate.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _182850: return "Wheelchair";
            case _528123036: return "Interpreter";
            case _182847: return "Stretcher";
            case _528123037: return "Seeing eye dog";
            case _30719: return "Oxygen";
            case _528123038: return "Heart monitor";
            default: return "?";
          }
    }


}

