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

public enum NehtaGlobalStatementValues {

        /**
         * No information about taking any medication is known.
         */
        _01, 
        /**
         * No information about taking any medication is available because the patient was not asked or not able to be asked.
         */
        _02, 
        /**
         * No information about taking any medication is supplied.
         */
        _03, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NehtaGlobalStatementValues fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("01".equals(codeString))
          return _01;
        if ("02".equals(codeString))
          return _02;
        if ("03".equals(codeString))
          return _03;
        throw new FHIRException("Unknown NehtaGlobalStatementValues code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _01: return "01";
            case _02: return "02";
            case _03: return "03";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/global-statement-values";
        }
        public String getDefinition() {
          switch (this) {
            case _01: return "No information about taking any medication is known.";
            case _02: return "No information about taking any medication is available because the patient was not asked or not able to be asked.";
            case _03: return "No information about taking any medication is supplied.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _01: return "None known";
            case _02: return "Not asked";
            case _03: return "None supplied";
            default: return "?";
          }
    }


}

