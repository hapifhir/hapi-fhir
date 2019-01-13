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

public enum HspcBpmeasmethoddevice {

        /**
         * A blood pressure cuff that is sized to use on pediatric paitents
         */
        _2102, 
        /**
         * A blood pressure cuff designed to be used on an individual's thigh
         */
        _2162, 
        /**
         * A blood pressure cuff that is sized to use on an adult upper arm
         */
        _31163, 
        /**
         * A blood pressure cuff designed to be used on an individual's ankle
         */
        _50577434, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcBpmeasmethoddevice fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("2102".equals(codeString))
          return _2102;
        if ("2162".equals(codeString))
          return _2162;
        if ("31163".equals(codeString))
          return _31163;
        if ("50577434".equals(codeString))
          return _50577434;
        throw new FHIRException("Unknown HspcBpmeasmethoddevice code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _2102: return "2102";
            case _2162: return "2162";
            case _31163: return "31163";
            case _50577434: return "50577434";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/hspc-bpmeasmethoddevice";
        }
        public String getDefinition() {
          switch (this) {
            case _2102: return "A blood pressure cuff that is sized to use on pediatric paitents";
            case _2162: return "A blood pressure cuff designed to be used on an individual's thigh";
            case _31163: return "A blood pressure cuff that is sized to use on an adult upper arm";
            case _50577434: return "A blood pressure cuff designed to be used on an individual's ankle";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _2102: return "Pediatric cuff";
            case _2162: return "Thigh cuff (24 cm)";
            case _31163: return "Adult Arm Cuff";
            case _50577434: return "Ankle Cuff";
            default: return "?";
          }
    }


}

