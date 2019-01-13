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

public enum UslabDoEvent {

        /**
         * a Provider (.orderer) orders one or more new laboratory tests or scheduled laboratory tests (including future tests) to be performed by a laboratory. 
         */
        NEWREQUEST, 
        /**
         * a Provider (.orderer) adds one or more additional tests to a previously transmitted test requisition.
         */
        ADDONREQUEST, 
        /**
         * The Provider (orderer) requests cancelation of previously ordered test.
         */
        ORDERERCANCEL, 
        /**
         * The laboratory cancels a previously ordered test.
         */
        LABCANCEL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static UslabDoEvent fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("new-request".equals(codeString))
          return NEWREQUEST;
        if ("add-on-request".equals(codeString))
          return ADDONREQUEST;
        if ("orderer-cancel".equals(codeString))
          return ORDERERCANCEL;
        if ("lab-cancel".equals(codeString))
          return LABCANCEL;
        throw new FHIRException("Unknown UslabDoEvent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NEWREQUEST: return "new-request";
            case ADDONREQUEST: return "add-on-request";
            case ORDERERCANCEL: return "orderer-cancel";
            case LABCANCEL: return "lab-cancel";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/uslab-event";
        }
        public String getDefinition() {
          switch (this) {
            case NEWREQUEST: return "a Provider (.orderer) orders one or more new laboratory tests or scheduled laboratory tests (including future tests) to be performed by a laboratory. ";
            case ADDONREQUEST: return "a Provider (.orderer) adds one or more additional tests to a previously transmitted test requisition.";
            case ORDERERCANCEL: return "The Provider (orderer) requests cancelation of previously ordered test.";
            case LABCANCEL: return "The laboratory cancels a previously ordered test.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NEWREQUEST: return "new request";
            case ADDONREQUEST: return "add-on request";
            case ORDERERCANCEL: return "orderer cancel";
            case LABCANCEL: return "laboratory cancel";
            default: return "?";
          }
    }


}

