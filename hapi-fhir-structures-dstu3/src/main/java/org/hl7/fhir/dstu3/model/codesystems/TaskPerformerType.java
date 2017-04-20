package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum TaskPerformerType {

        /**
         * A workflow participant that requests services.
         */
        REQUESTER, 
        /**
         * A workflow participant that dispatches services (assigns another task to a participant).
         */
        DISPATCHER, 
        /**
         * A workflow participant that schedules (dispatches and sets the time or date for performance of) services.
         */
        SCHEDULER, 
        /**
         * A workflow participant that performs services.
         */
        PERFORMER, 
        /**
         * A workflow participant that monitors task activity.
         */
        MONITOR, 
        /**
         * A workflow participant that manages task activity.
         */
        MANAGER, 
        /**
         * A workflow participant that acquires resources (specimens, images, etc) necessary to perform the task.
         */
        ACQUIRER, 
        /**
         * A workflow participant that reviews task inputs or outputs.
         */
        REVIEWER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TaskPerformerType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requester".equals(codeString))
          return REQUESTER;
        if ("dispatcher".equals(codeString))
          return DISPATCHER;
        if ("scheduler".equals(codeString))
          return SCHEDULER;
        if ("performer".equals(codeString))
          return PERFORMER;
        if ("monitor".equals(codeString))
          return MONITOR;
        if ("manager".equals(codeString))
          return MANAGER;
        if ("acquirer".equals(codeString))
          return ACQUIRER;
        if ("reviewer".equals(codeString))
          return REVIEWER;
        throw new FHIRException("Unknown TaskPerformerType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUESTER: return "requester";
            case DISPATCHER: return "dispatcher";
            case SCHEDULER: return "scheduler";
            case PERFORMER: return "performer";
            case MONITOR: return "monitor";
            case MANAGER: return "manager";
            case ACQUIRER: return "acquirer";
            case REVIEWER: return "reviewer";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/task-performer-type";
        }
        public String getDefinition() {
          switch (this) {
            case REQUESTER: return "A workflow participant that requests services.";
            case DISPATCHER: return "A workflow participant that dispatches services (assigns another task to a participant).";
            case SCHEDULER: return "A workflow participant that schedules (dispatches and sets the time or date for performance of) services.";
            case PERFORMER: return "A workflow participant that performs services.";
            case MONITOR: return "A workflow participant that monitors task activity.";
            case MANAGER: return "A workflow participant that manages task activity.";
            case ACQUIRER: return "A workflow participant that acquires resources (specimens, images, etc) necessary to perform the task.";
            case REVIEWER: return "A workflow participant that reviews task inputs or outputs.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTER: return "Requester";
            case DISPATCHER: return "Dispatcher";
            case SCHEDULER: return "Scheduler";
            case PERFORMER: return "Performer";
            case MONITOR: return "Monitor";
            case MANAGER: return "Manager";
            case ACQUIRER: return "Acquirer";
            case REVIEWER: return "Reviewer";
            default: return "?";
          }
    }


}

