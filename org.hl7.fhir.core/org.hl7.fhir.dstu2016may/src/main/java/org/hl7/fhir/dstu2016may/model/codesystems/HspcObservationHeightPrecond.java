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

public enum HspcObservationHeightPrecond {

        /**
         * The individual's height was measured while the individual was wearing shoes
         */
        _84138, 
        /**
         * The individual's height was measured without the individual wearing shoes.
         */
        _84139, 
        /**
         * The individual's height was measured while the individual was lying down.
         */
        _84140, 
        /**
         * The individual's height was measured while the individual was standing up.
         */
        _84141, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcObservationHeightPrecond fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("84138".equals(codeString))
          return _84138;
        if ("84139".equals(codeString))
          return _84139;
        if ("84140".equals(codeString))
          return _84140;
        if ("84141".equals(codeString))
          return _84141;
        throw new FHIRException("Unknown HspcObservationHeightPrecond code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _84138: return "84138";
            case _84139: return "84139";
            case _84140: return "84140";
            case _84141: return "84141";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7,org/fhir/observation-hspc-heightPrecond";
        }
        public String getDefinition() {
          switch (this) {
            case _84138: return "The individual's height was measured while the individual was wearing shoes";
            case _84139: return "The individual's height was measured without the individual wearing shoes.";
            case _84140: return "The individual's height was measured while the individual was lying down.";
            case _84141: return "The individual's height was measured while the individual was standing up.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _84138: return "Height (body length) with shoes";
            case _84139: return "Height (body length) without shoes";
            case _84140: return "Height (body length) with patient reclining";
            case _84141: return "Height (body length) with patient standing";
            default: return "?";
          }
    }


}

