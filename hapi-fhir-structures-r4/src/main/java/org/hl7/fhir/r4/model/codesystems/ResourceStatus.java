package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ResourceStatus {

        /**
         * The resource was created in error, and should not be treated as valid (note: in many cases, for various data integrity related reasons, the information cannot be removed from the record)
         */
        ERROR, 
        /**
         * The resource describes an action or plan that is proposed, and not yet approved by the participants
         */
        PROPOSED, 
        /**
         * The resource describes a course of action that is planned and agreed/approved, but at the time of recording was still future
         */
        PLANNED, 
        /**
         * The information in the resource is still being prepared and edited
         */
        DRAFT, 
        /**
         * A fulfiller has been asked to perform this action, but it has not yet occurred
         */
        REQUESTED, 
        /**
         * The fulfiller has received the request, but not yet agreed to carry out the action
         */
        RECEIVED, 
        /**
         * The fulfiller chose not to perform the action
         */
        DECLINED, 
        /**
         * The fulfiller has decided to perform the action, and plans are in train to do this in the future
         */
        ACCEPTED, 
        /**
         * The pre-conditions for the action are all fulfilled, and it is imminent
         */
        ARRIVED, 
        /**
         * The resource describes information that is currently valid or a process that is presently occuring
         */
        ACTIVE, 
        /**
         * The process described/requested in this resource has been halted for some reason
         */
        SUSPENDED, 
        /**
         * The process described/requested in the resource could not be completed, and no further action is planned
         */
        FAILED, 
        /**
         * The information in this resource has been replaced by information in another resource
         */
        REPLACED, 
        /**
         * The process described/requested in the resource has been completed, and no further action is planned
         */
        COMPLETE, 
        /**
         * The resource describes information that is no longer valid or a process that is stopped occurring
         */
        INACTIVE, 
        /**
         * The process described/requested in the resource did not complete - usually due to some workflow error, and no further action is planned
         */
        ABANDONED, 
        /**
         * Authoring system does not know the status
         */
        UNKNOWN, 
        /**
         * The information in this resource is not yet approved
         */
        UNCONFIRMED, 
        /**
         * The information in this resource is approved
         */
        CONFIRMED, 
        /**
         * The issue identified by this resource is no longer of concern
         */
        RESOLVED, 
        /**
         * This information has been ruled out by testing and evaluation
         */
        REFUTED, 
        /**
         * Potentially true?
         */
        DIFFERENTIAL, 
        /**
         * This information is still being assembled
         */
        PARTIAL, 
        /**
         * not available at this time/location
         */
        BUSYUNAVAILABLE, 
        /**
         * Free for scheduling
         */
        FREE, 
        /**
         * Ready to act
         */
        ONTARGET, 
        /**
         * Ahead of the planned timelines
         */
        AHEADOFTARGET, 
        /**
         * 
         */
        BEHINDTARGET, 
        /**
         * Behind the planned timelines
         */
        NOTREADY, 
        /**
         * The device transducer is disconnected
         */
        TRANSDUCDISCON, 
        /**
         * The hardware is disconnected
         */
        HWDISCON, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("error".equals(codeString))
          return ERROR;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("declined".equals(codeString))
          return DECLINED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("arrived".equals(codeString))
          return ARRIVED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("failed".equals(codeString))
          return FAILED;
        if ("replaced".equals(codeString))
          return REPLACED;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("abandoned".equals(codeString))
          return ABANDONED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("unconfirmed".equals(codeString))
          return UNCONFIRMED;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("differential".equals(codeString))
          return DIFFERENTIAL;
        if ("partial".equals(codeString))
          return PARTIAL;
        if ("busy-unavailable".equals(codeString))
          return BUSYUNAVAILABLE;
        if ("free".equals(codeString))
          return FREE;
        if ("on-target".equals(codeString))
          return ONTARGET;
        if ("ahead-of-target".equals(codeString))
          return AHEADOFTARGET;
        if ("behind-target".equals(codeString))
          return BEHINDTARGET;
        if ("not-ready".equals(codeString))
          return NOTREADY;
        if ("transduc-discon".equals(codeString))
          return TRANSDUCDISCON;
        if ("hw-discon".equals(codeString))
          return HWDISCON;
        throw new FHIRException("Unknown ResourceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ERROR: return "error";
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case DRAFT: return "draft";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case DECLINED: return "declined";
            case ACCEPTED: return "accepted";
            case ARRIVED: return "arrived";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case FAILED: return "failed";
            case REPLACED: return "replaced";
            case COMPLETE: return "complete";
            case INACTIVE: return "inactive";
            case ABANDONED: return "abandoned";
            case UNKNOWN: return "unknown";
            case UNCONFIRMED: return "unconfirmed";
            case CONFIRMED: return "confirmed";
            case RESOLVED: return "resolved";
            case REFUTED: return "refuted";
            case DIFFERENTIAL: return "differential";
            case PARTIAL: return "partial";
            case BUSYUNAVAILABLE: return "busy-unavailable";
            case FREE: return "free";
            case ONTARGET: return "on-target";
            case AHEADOFTARGET: return "ahead-of-target";
            case BEHINDTARGET: return "behind-target";
            case NOTREADY: return "not-ready";
            case TRANSDUCDISCON: return "transduc-discon";
            case HWDISCON: return "hw-discon";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/resource-status";
        }
        public String getDefinition() {
          switch (this) {
            case ERROR: return "The resource was created in error, and should not be treated as valid (note: in many cases, for various data integrity related reasons, the information cannot be removed from the record)";
            case PROPOSED: return "The resource describes an action or plan that is proposed, and not yet approved by the participants";
            case PLANNED: return "The resource describes a course of action that is planned and agreed/approved, but at the time of recording was still future";
            case DRAFT: return "The information in the resource is still being prepared and edited";
            case REQUESTED: return "A fulfiller has been asked to perform this action, but it has not yet occurred";
            case RECEIVED: return "The fulfiller has received the request, but not yet agreed to carry out the action";
            case DECLINED: return "The fulfiller chose not to perform the action";
            case ACCEPTED: return "The fulfiller has decided to perform the action, and plans are in train to do this in the future";
            case ARRIVED: return "The pre-conditions for the action are all fulfilled, and it is imminent";
            case ACTIVE: return "The resource describes information that is currently valid or a process that is presently occuring";
            case SUSPENDED: return "The process described/requested in this resource has been halted for some reason";
            case FAILED: return "The process described/requested in the resource could not be completed, and no further action is planned";
            case REPLACED: return "The information in this resource has been replaced by information in another resource";
            case COMPLETE: return "The process described/requested in the resource has been completed, and no further action is planned";
            case INACTIVE: return "The resource describes information that is no longer valid or a process that is stopped occurring";
            case ABANDONED: return "The process described/requested in the resource did not complete - usually due to some workflow error, and no further action is planned";
            case UNKNOWN: return "Authoring system does not know the status";
            case UNCONFIRMED: return "The information in this resource is not yet approved";
            case CONFIRMED: return "The information in this resource is approved";
            case RESOLVED: return "The issue identified by this resource is no longer of concern";
            case REFUTED: return "This information has been ruled out by testing and evaluation";
            case DIFFERENTIAL: return "Potentially true?";
            case PARTIAL: return "This information is still being assembled";
            case BUSYUNAVAILABLE: return "not available at this time/location";
            case FREE: return "Free for scheduling";
            case ONTARGET: return "Ready to act";
            case AHEADOFTARGET: return "Ahead of the planned timelines";
            case BEHINDTARGET: return "";
            case NOTREADY: return "Behind the planned timelines";
            case TRANSDUCDISCON: return "The device transducer is disconnected";
            case HWDISCON: return "The hardware is disconnected";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ERROR: return "error";
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case DRAFT: return "draft";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case DECLINED: return "declined";
            case ACCEPTED: return "accepted";
            case ARRIVED: return "arrived";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case FAILED: return "failed";
            case REPLACED: return "replaced";
            case COMPLETE: return "complete";
            case INACTIVE: return "inactive";
            case ABANDONED: return "abandoned";
            case UNKNOWN: return "unknown";
            case UNCONFIRMED: return "unconfirmed";
            case CONFIRMED: return "confirmed";
            case RESOLVED: return "resolved";
            case REFUTED: return "refuted";
            case DIFFERENTIAL: return "differential";
            case PARTIAL: return "partial";
            case BUSYUNAVAILABLE: return "busy-unavailable";
            case FREE: return "free";
            case ONTARGET: return "on-target";
            case AHEADOFTARGET: return "ahead-of-target";
            case BEHINDTARGET: return "behind-target";
            case NOTREADY: return "not-ready";
            case TRANSDUCDISCON: return "transduc-discon";
            case HWDISCON: return "hw-discon";
            default: return "?";
          }
    }


}

