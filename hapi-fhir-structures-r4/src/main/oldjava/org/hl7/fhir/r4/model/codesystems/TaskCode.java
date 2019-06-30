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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum TaskCode {

        /**
         * Take what actions are needed to transition the focus resource from 'draft' to 'active' or 'in-progress', as appropriate for the resource type.  This may involve additing additional content, approval, validation, etc.
         */
        APPROVE, 
        /**
         * Act to perform the actions defined in the focus request.  This might result in a 'more assertive' request (order for a plan or proposal, filler order for a placer order), but is intend to eventually result in events.  The degree of fulfillment requested might be limited by Task.restriction.
         */
        FULFILL, 
        /**
         * Abort, cancel or withdraw the focal resource, as appropriate for the type of resource.
         */
        ABORT, 
        /**
         * Replace the focal resource with the specified input resource
         */
        REPLACE, 
        /**
         * Update the focal resource of the owning system to reflect the content specified as the Task.focus
         */
        CHANGE, 
        /**
         * Transition the focal resource from 'active' or 'in-progress' to 'suspended'
         */
        SUSPEND, 
        /**
         * Transition the focal resource from 'suspended' to 'active' or 'in-progress' as appropriate for the resource type.
         */
        RESUME, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TaskCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("approve".equals(codeString))
          return APPROVE;
        if ("fulfill".equals(codeString))
          return FULFILL;
        if ("abort".equals(codeString))
          return ABORT;
        if ("replace".equals(codeString))
          return REPLACE;
        if ("change".equals(codeString))
          return CHANGE;
        if ("suspend".equals(codeString))
          return SUSPEND;
        if ("resume".equals(codeString))
          return RESUME;
        throw new FHIRException("Unknown TaskCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPROVE: return "approve";
            case FULFILL: return "fulfill";
            case ABORT: return "abort";
            case REPLACE: return "replace";
            case CHANGE: return "change";
            case SUSPEND: return "suspend";
            case RESUME: return "resume";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/CodeSystem/task-code";
        }
        public String getDefinition() {
          switch (this) {
            case APPROVE: return "Take what actions are needed to transition the focus resource from 'draft' to 'active' or 'in-progress', as appropriate for the resource type.  This may involve additing additional content, approval, validation, etc.";
            case FULFILL: return "Act to perform the actions defined in the focus request.  This might result in a 'more assertive' request (order for a plan or proposal, filler order for a placer order), but is intend to eventually result in events.  The degree of fulfillment requested might be limited by Task.restriction.";
            case ABORT: return "Abort, cancel or withdraw the focal resource, as appropriate for the type of resource.";
            case REPLACE: return "Replace the focal resource with the specified input resource";
            case CHANGE: return "Update the focal resource of the owning system to reflect the content specified as the Task.focus";
            case SUSPEND: return "Transition the focal resource from 'active' or 'in-progress' to 'suspended'";
            case RESUME: return "Transition the focal resource from 'suspended' to 'active' or 'in-progress' as appropriate for the resource type.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPROVE: return "Activate/approve the focal resource";
            case FULFILL: return "Fulfill the focal request";
            case ABORT: return "Mark the focal resource as no longer active";
            case REPLACE: return "Replace the focal resource with the input resource";
            case CHANGE: return "Change the focal resource";
            case SUSPEND: return "Suspend the focal resource";
            case RESUME: return "Re-activate the focal resource";
            default: return "?";
          }
    }


}

