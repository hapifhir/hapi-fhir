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

// Generated on Tue, Mar 29, 2016 03:55+1100 for FHIR v1.3.0


import org.hl7.fhir.exceptions.FHIRException;

public enum Participantstatus {

        /**
         * The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse.
         */
        ACCEPTED, 
        /**
         * The appointment participant has declined the appointment.
         */
        DECLINED, 
        /**
         * The appointment participant has tentatively accepted the appointment.
         */
        TENTATIVE, 
        /**
         * The participant has in-process the appointment.
         */
        INPROCESS, 
        /**
         * The participant has completed the appointment.
         */
        COMPLETED, 
        /**
         * This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment.
         */
        NEEDSACTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Participantstatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("declined".equals(codeString))
          return DECLINED;
        if ("tentative".equals(codeString))
          return TENTATIVE;
        if ("in-process".equals(codeString))
          return INPROCESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("needs-action".equals(codeString))
          return NEEDSACTION;
        throw new FHIRException("Unknown Participantstatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCEPTED: return "accepted";
            case DECLINED: return "declined";
            case TENTATIVE: return "tentative";
            case INPROCESS: return "in-process";
            case COMPLETED: return "completed";
            case NEEDSACTION: return "needs-action";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/participantstatus";
        }
        public String getDefinition() {
          switch (this) {
            case ACCEPTED: return "The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse.";
            case DECLINED: return "The appointment participant has declined the appointment.";
            case TENTATIVE: return "The appointment participant has tentatively accepted the appointment.";
            case INPROCESS: return "The participant has in-process the appointment.";
            case COMPLETED: return "The participant has completed the appointment.";
            case NEEDSACTION: return "This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCEPTED: return "Accepted";
            case DECLINED: return "Declined";
            case TENTATIVE: return "Tentative";
            case INPROCESS: return "In Process";
            case COMPLETED: return "Completed";
            case NEEDSACTION: return "Needs Action";
            default: return "?";
          }
    }


}

