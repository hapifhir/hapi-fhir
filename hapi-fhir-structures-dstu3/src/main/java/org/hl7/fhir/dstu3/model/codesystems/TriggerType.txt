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

public enum TriggerType {

        /**
         * The trigger occurs in response to a specific named event
         */
        NAMEDEVENT, 
        /**
         * The trigger occurs at a specific time or periodically as described by a timing or schedule
         */
        PERIODIC, 
        /**
         * The trigger occurs whenever data of a particular type is added
         */
        DATAADDED, 
        /**
         * The trigger occurs whenever data of a particular type is modified
         */
        DATAMODIFIED, 
        /**
         * The trigger occurs whenever data of a particular type is removed
         */
        DATAREMOVED, 
        /**
         * The trigger occurs whenever data of a particular type is accessed
         */
        DATAACCESSED, 
        /**
         * The trigger occurs whenever access to data of a particular type is completed
         */
        DATAACCESSENDED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TriggerType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("named-event".equals(codeString))
          return NAMEDEVENT;
        if ("periodic".equals(codeString))
          return PERIODIC;
        if ("data-added".equals(codeString))
          return DATAADDED;
        if ("data-modified".equals(codeString))
          return DATAMODIFIED;
        if ("data-removed".equals(codeString))
          return DATAREMOVED;
        if ("data-accessed".equals(codeString))
          return DATAACCESSED;
        if ("data-access-ended".equals(codeString))
          return DATAACCESSENDED;
        throw new FHIRException("Unknown TriggerType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NAMEDEVENT: return "named-event";
            case PERIODIC: return "periodic";
            case DATAADDED: return "data-added";
            case DATAMODIFIED: return "data-modified";
            case DATAREMOVED: return "data-removed";
            case DATAACCESSED: return "data-accessed";
            case DATAACCESSENDED: return "data-access-ended";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/trigger-type";
        }
        public String getDefinition() {
          switch (this) {
            case NAMEDEVENT: return "The trigger occurs in response to a specific named event";
            case PERIODIC: return "The trigger occurs at a specific time or periodically as described by a timing or schedule";
            case DATAADDED: return "The trigger occurs whenever data of a particular type is added";
            case DATAMODIFIED: return "The trigger occurs whenever data of a particular type is modified";
            case DATAREMOVED: return "The trigger occurs whenever data of a particular type is removed";
            case DATAACCESSED: return "The trigger occurs whenever data of a particular type is accessed";
            case DATAACCESSENDED: return "The trigger occurs whenever access to data of a particular type is completed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NAMEDEVENT: return "Named Event";
            case PERIODIC: return "Periodic";
            case DATAADDED: return "Data Added";
            case DATAMODIFIED: return "Data Modified";
            case DATAREMOVED: return "Data Removed";
            case DATAACCESSED: return "Data Accessed";
            case DATAACCESSENDED: return "Data Access Ended";
            default: return "?";
          }
    }


}

