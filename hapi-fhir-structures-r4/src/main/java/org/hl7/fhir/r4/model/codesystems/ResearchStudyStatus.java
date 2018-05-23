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

public enum ResearchStudyStatus {

        /**
         * Study is opened for accrual.
         */
        ACTIVE, 
        /**
         * Study is completed prematurely and will not resume; patients are no longer examined nor treated.
Tagged
         */
        ADMINISTRATIVELYCOMPLETED, 
        /**
         * Protocol is approved by the review board.
         */
        APPROVED, 
        /**
         * Study is closed for accrual; patients can be examined and treated.
         */
        CLOSEDTOACCRUAL, 
        /**
         * Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment or intervention but are still being followed according to the primary objective of the study.
         */
        CLOSEDTOACCRUALANDINTERVENTION, 
        /**
         * Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment
or intervention but are still being followed according to the primary objective of the study.
         */
        COMPLETED, 
        /**
         * Protocol was disapproved by the review board.
         */
        DISAPPROVED, 
        /**
         * Protocol is submitted to the review board for approval.
         */
        INREVIEW, 
        /**
         * Study is temporarily closed for accrual; can be potentially resumed in the future; patients can be examined and treated.
         */
        TEMPORARILYCLOSEDTOACCRUAL, 
        /**
         * Study is temporarily closed for accrual and intervention and potentially can be resumed in the future.
         */
        TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION, 
        /**
         * Protocol was withdrawn by the lead organization.
         */
        WITHDRAWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchStudyStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("administratively-completed".equals(codeString))
          return ADMINISTRATIVELYCOMPLETED;
        if ("approved".equals(codeString))
          return APPROVED;
        if ("closed-to-accrual".equals(codeString))
          return CLOSEDTOACCRUAL;
        if ("closed-to-accrual-and-intervention".equals(codeString))
          return CLOSEDTOACCRUALANDINTERVENTION;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("disapproved".equals(codeString))
          return DISAPPROVED;
        if ("in-review".equals(codeString))
          return INREVIEW;
        if ("temporarily-closed-to-accrual".equals(codeString))
          return TEMPORARILYCLOSEDTOACCRUAL;
        if ("temporarily-closed-to-accrual-and-intervention".equals(codeString))
          return TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        throw new FHIRException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case ADMINISTRATIVELYCOMPLETED: return "administratively-completed";
            case APPROVED: return "approved";
            case CLOSEDTOACCRUAL: return "closed-to-accrual";
            case CLOSEDTOACCRUALANDINTERVENTION: return "closed-to-accrual-and-intervention";
            case COMPLETED: return "completed";
            case DISAPPROVED: return "disapproved";
            case INREVIEW: return "in-review";
            case TEMPORARILYCLOSEDTOACCRUAL: return "temporarily-closed-to-accrual";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "temporarily-closed-to-accrual-and-intervention";
            case WITHDRAWN: return "withdrawn";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-study-status";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "Study is opened for accrual.";
            case ADMINISTRATIVELYCOMPLETED: return "Study is completed prematurely and will not resume; patients are no longer examined nor treated.\nTagged";
            case APPROVED: return "Protocol is approved by the review board.";
            case CLOSEDTOACCRUAL: return "Study is closed for accrual; patients can be examined and treated.";
            case CLOSEDTOACCRUALANDINTERVENTION: return "Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment or intervention but are still being followed according to the primary objective of the study.";
            case COMPLETED: return "Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment\nor intervention but are still being followed according to the primary objective of the study.";
            case DISAPPROVED: return "Protocol was disapproved by the review board.";
            case INREVIEW: return "Protocol is submitted to the review board for approval.";
            case TEMPORARILYCLOSEDTOACCRUAL: return "Study is temporarily closed for accrual; can be potentially resumed in the future; patients can be examined and treated.";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "Study is temporarily closed for accrual and intervention and potentially can be resumed in the future.";
            case WITHDRAWN: return "Protocol was withdrawn by the lead organization.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case ADMINISTRATIVELYCOMPLETED: return "Administratively Completed";
            case APPROVED: return "Approved";
            case CLOSEDTOACCRUAL: return "Closed to Accrual";
            case CLOSEDTOACCRUALANDINTERVENTION: return "Closed to Accrual and Intervention";
            case COMPLETED: return "Completed";
            case DISAPPROVED: return "Disapproved";
            case INREVIEW: return "In Review";
            case TEMPORARILYCLOSEDTOACCRUAL: return "Temporarily Closed to Accrual";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "Temporarily Closed to Accrual and Intervention";
            case WITHDRAWN: return "Withdrawn";
            default: return "?";
          }
    }


}

