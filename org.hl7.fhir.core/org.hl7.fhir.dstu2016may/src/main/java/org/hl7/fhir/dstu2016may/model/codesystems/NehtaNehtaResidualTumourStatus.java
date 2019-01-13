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

public enum NehtaNehtaResidualTumourStatus {

        /**
         * R0: Complete resection, margins histologically negative, no residual tumour left after resection (primary tumour, regional nodes)
         */
        R0, 
        /**
         * R1: Incomplete resection, margins histologically involved, microscopic tumour remains after resection of gross disease (primary tumour, regional nodes)
         */
        R1, 
        /**
         * R2: Incomplete resection, margins macroscopically involved or gross disease remains after subtotal resection (eg primary tumour, regional nodes, or liver metastasis).
         */
        R2, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NehtaNehtaResidualTumourStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("r0".equals(codeString))
          return R0;
        if ("r1".equals(codeString))
          return R1;
        if ("r2".equals(codeString))
          return R2;
        throw new FHIRException("Unknown NehtaNehtaResidualTumourStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case R0: return "r0";
            case R1: return "r1";
            case R2: return "r2";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/nehta-residual-tumour-status";
        }
        public String getDefinition() {
          switch (this) {
            case R0: return "R0: Complete resection, margins histologically negative, no residual tumour left after resection (primary tumour, regional nodes)";
            case R1: return "R1: Incomplete resection, margins histologically involved, microscopic tumour remains after resection of gross disease (primary tumour, regional nodes)";
            case R2: return "R2: Incomplete resection, margins macroscopically involved or gross disease remains after subtotal resection (eg primary tumour, regional nodes, or liver metastasis).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case R0: return "R0: Complete resection, margins histologically negative, no residual tumour left after resection (primary tumour, regional nodes)";
            case R1: return "R1: Incomplete resection, margins histologically involved, microscopic tumour remains after resection of gross disease (primary tumour, regional nodes)";
            case R2: return "R2: Incomplete resection, margins macroscopically involved or gross disease remains after subtotal resection (eg primary tumour, regional nodes, or liver metastasis).";
            default: return "?";
          }
    }


}

