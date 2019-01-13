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

public enum HspcObservationWeightPrecond {

        /**
         * Body weight measured while clothed
         */
        _84123, 
        /**
         * Body weight measured while unclothed
         */
        _84124, 
        /**
         * Infant weight while wearing a diaper
         */
        _84127, 
        /**
         * Infant weight without a diaper
         */
        _84128, 
        /**
         * Body weight that includes a cast
         */
        _84129, 
        /**
         * Body weight minus the weight of a cast
         */
        _84130, 
        /**
         * Body weight including a prosthetic limb
         */
        _84131, 
        /**
         * Body weight minus the weight of a prosthetic limb
         */
        _84132, 
        /**
         * Body weight including a brace
         */
        _84133, 
        /**
         * Body weight minus the weight of a brace
         */
        _84134, 
        /**
         * Body weight including a gown or drape
         */
        _84135, 
        /**
         * Body weight the includes some other weight
         */
        _84136, 
        /**
         * Body weight after subtracting some other included weight
         */
        _84137, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcObservationWeightPrecond fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("84123".equals(codeString))
          return _84123;
        if ("84124".equals(codeString))
          return _84124;
        if ("84127".equals(codeString))
          return _84127;
        if ("84128".equals(codeString))
          return _84128;
        if ("84129".equals(codeString))
          return _84129;
        if ("84130".equals(codeString))
          return _84130;
        if ("84131".equals(codeString))
          return _84131;
        if ("84132".equals(codeString))
          return _84132;
        if ("84133".equals(codeString))
          return _84133;
        if ("84134".equals(codeString))
          return _84134;
        if ("84135".equals(codeString))
          return _84135;
        if ("84136".equals(codeString))
          return _84136;
        if ("84137".equals(codeString))
          return _84137;
        throw new FHIRException("Unknown HspcObservationWeightPrecond code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _84123: return "84123";
            case _84124: return "84124";
            case _84127: return "84127";
            case _84128: return "84128";
            case _84129: return "84129";
            case _84130: return "84130";
            case _84131: return "84131";
            case _84132: return "84132";
            case _84133: return "84133";
            case _84134: return "84134";
            case _84135: return "84135";
            case _84136: return "84136";
            case _84137: return "84137";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/observation-hspc-weightPrecond";
        }
        public String getDefinition() {
          switch (this) {
            case _84123: return "Body weight measured while clothed";
            case _84124: return "Body weight measured while unclothed";
            case _84127: return "Infant weight while wearing a diaper";
            case _84128: return "Infant weight without a diaper";
            case _84129: return "Body weight that includes a cast";
            case _84130: return "Body weight minus the weight of a cast";
            case _84131: return "Body weight including a prosthetic limb";
            case _84132: return "Body weight minus the weight of a prosthetic limb";
            case _84133: return "Body weight including a brace";
            case _84134: return "Body weight minus the weight of a brace";
            case _84135: return "Body weight including a gown or drape";
            case _84136: return "Body weight the includes some other weight";
            case _84137: return "Body weight after subtracting some other included weight";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _84123: return "Weight with street clothes";
            case _84124: return "Weight without street clothes";
            case _84127: return "Weight with diapers";
            case _84128: return "Weight without diapers";
            case _84129: return "Weight with cast";
            case _84130: return "Weight without cast";
            case _84131: return "Weight with limb prosthesis";
            case _84132: return "Weight without limb prosthesis";
            case _84133: return "Weight with brace";
            case _84134: return "Weight without brace";
            case _84135: return "Weight with gown or drape";
            case _84136: return "Weight with added-on weight";
            case _84137: return "Weight without added-on weight";
            default: return "?";
          }
    }


}

