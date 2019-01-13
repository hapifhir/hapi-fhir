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

public enum NehtaServiceBookingStatusValues {

        /**
         * Planned act for specific time and place
         */
        APT, 
        /**
         * Request for Booking of an Appointment
         */
        ARQ, 
        /**
         * Service actually happens or happened or is ongoing
         */
        EVN, 
        /**
         * Plan to perform a service
         */
        INT, 
        /**
         * An intent to perform a service
         */
        PRMS, 
        /**
         * Non-mandated intent to perform an act
         */
        PRP, 
        /**
         * Request or Order for a service
         */
        RQO, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NehtaServiceBookingStatusValues fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("APT".equals(codeString))
          return APT;
        if ("ARQ".equals(codeString))
          return ARQ;
        if ("EVN".equals(codeString))
          return EVN;
        if ("INT".equals(codeString))
          return INT;
        if ("PRMS".equals(codeString))
          return PRMS;
        if ("PRP".equals(codeString))
          return PRP;
        if ("RQO".equals(codeString))
          return RQO;
        throw new FHIRException("Unknown NehtaServiceBookingStatusValues code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APT: return "APT";
            case ARQ: return "ARQ";
            case EVN: return "EVN";
            case INT: return "INT";
            case PRMS: return "PRMS";
            case PRP: return "PRP";
            case RQO: return "RQO";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/service-booking-status-values";
        }
        public String getDefinition() {
          switch (this) {
            case APT: return "Planned act for specific time and place";
            case ARQ: return "Request for Booking of an Appointment";
            case EVN: return "Service actually happens or happened or is ongoing";
            case INT: return "Plan to perform a service";
            case PRMS: return "An intent to perform a service";
            case PRP: return "Non-mandated intent to perform an act";
            case RQO: return "Request or Order for a service";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APT: return "Appointment";
            case ARQ: return "Appointment Request";
            case EVN: return "Event";
            case INT: return "Intent";
            case PRMS: return "Promise";
            case PRP: return "Proposal";
            case RQO: return "Request";
            default: return "?";
          }
    }


}

