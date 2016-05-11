package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3ActStatus {

        /**
         * Encompasses the expected states of an Act, but excludes "nullified" and "obsolete" which represent unusual terminal states for the life-cycle.
         */
        NORMAL, 
        /**
         * The Act has been terminated prior to the originally intended completion.
         */
        ABORTED, 
        /**
         * The Act can be performed or is being performed
         */
        ACTIVE, 
        /**
         * The Act has been abandoned before activation.
         */
        CANCELLED, 
        /**
         * An Act that has terminated normally after all of its constituents have been performed.
         */
        COMPLETED, 
        /**
         * An Act that is still in the preparatory stages has been put aside.  No action can occur until the Act is released.
         */
        HELD, 
        /**
         * An Act that is in the preparatory stages and may not yet be acted upon
         */
        NEW, 
        /**
         * An Act that has been activated (actions could or have been performed against it), but has been temporarily disabled.  No further action should be taken against it until it is released
         */
        SUSPENDED, 
        /**
         * This Act instance was created in error and has been 'removed' and is treated as though it never existed.  A record is retained for audit purposes only.
         */
        NULLIFIED, 
        /**
         * This Act instance has been replaced by a new instance.
         */
        OBSOLETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("held".equals(codeString))
          return HELD;
        if ("new".equals(codeString))
          return NEW;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("nullified".equals(codeString))
          return NULLIFIED;
        if ("obsolete".equals(codeString))
          return OBSOLETE;
        throw new Exception("Unknown V3ActStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case ABORTED: return "aborted";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case HELD: return "held";
            case NEW: return "new";
            case SUSPENDED: return "suspended";
            case NULLIFIED: return "nullified";
            case OBSOLETE: return "obsolete";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "Encompasses the expected states of an Act, but excludes \"nullified\" and \"obsolete\" which represent unusual terminal states for the life-cycle.";
            case ABORTED: return "The Act has been terminated prior to the originally intended completion.";
            case ACTIVE: return "The Act can be performed or is being performed";
            case CANCELLED: return "The Act has been abandoned before activation.";
            case COMPLETED: return "An Act that has terminated normally after all of its constituents have been performed.";
            case HELD: return "An Act that is still in the preparatory stages has been put aside.  No action can occur until the Act is released.";
            case NEW: return "An Act that is in the preparatory stages and may not yet be acted upon";
            case SUSPENDED: return "An Act that has been activated (actions could or have been performed against it), but has been temporarily disabled.  No further action should be taken against it until it is released";
            case NULLIFIED: return "This Act instance was created in error and has been 'removed' and is treated as though it never existed.  A record is retained for audit purposes only.";
            case OBSOLETE: return "This Act instance has been replaced by a new instance.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "normal";
            case ABORTED: return "aborted";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case HELD: return "held";
            case NEW: return "new";
            case SUSPENDED: return "suspended";
            case NULLIFIED: return "nullified";
            case OBSOLETE: return "obsolete";
            default: return "?";
          }
    }


}

