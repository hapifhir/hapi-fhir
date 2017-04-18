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

public enum ResearchStudyStatus {

        /**
         * The study is undergoing design but the process of selecting study subjects and capturing data has not yet begun.
         */
        DRAFT, 
        /**
         * The study is currently being executed
         */
        INPROGRESS, 
        /**
         * Execution of the study has been temporarily paused
         */
        SUSPENDED, 
        /**
         * The study was terminated prior to the final determination of results
         */
        STOPPED, 
        /**
         * The information sought by the study has been gathered and compiled and no further work is being performed
         */
        COMPLETED, 
        /**
         * This study never actually existed.  The record is retained for tracking purposes in the event decisions may have been made based on this erroneous information.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchStudyStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case INPROGRESS: return "in-progress";
            case SUSPENDED: return "suspended";
            case STOPPED: return "stopped";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-study-status";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The study is undergoing design but the process of selecting study subjects and capturing data has not yet begun.";
            case INPROGRESS: return "The study is currently being executed";
            case SUSPENDED: return "Execution of the study has been temporarily paused";
            case STOPPED: return "The study was terminated prior to the final determination of results";
            case COMPLETED: return "The information sought by the study has been gathered and compiled and no further work is being performed";
            case ENTEREDINERROR: return "This study never actually existed.  The record is retained for tracking purposes in the event decisions may have been made based on this erroneous information.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case INPROGRESS: return "In-progress";
            case SUSPENDED: return "Suspended";
            case STOPPED: return "Stopped";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in error";
            default: return "?";
          }
    }


}

