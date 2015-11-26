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


public enum BasicResourceType {

        /**
         * An assertion of permission for an activity or set of activities to occur, possibly subject to particular limitations; e.g. surgical consent, information disclosure consent, etc.
         */
        CONSENT, 
        /**
         * A request that care of a particular type be provided to a patient.  Could involve the transfer of care, a consult, etc.
         */
        REFERRAL, 
        /**
         * An undesired reaction caused by exposure to some agent (e.g. a medication, immunization, food, or environmental agent).
         */
        ADVEVENT, 
        /**
         * A request that a time be scheduled for a type of service for a specified patient, potentially subject to other constraints
         */
        APTMTREQ, 
        /**
         * The transition of a patient or set of material from one location to another
         */
        TRANSFER, 
        /**
         * The specification of a set of food and/or other nutritional material to be delivered to a patient.
         */
        DIET, 
        /**
         * An occurrence of a non-care-related event in the healthcare domain, such as approvals, reviews, etc.
         */
        ADMINACT, 
        /**
         * Record of a situation where a subject was exposed to a substance.  Usually of interest to public health.
         */
        EXPOSURE, 
        /**
         * A formalized inquiry into the circumstances surrounding a particular unplanned event or potential event for the purposes of identifying possible causes and contributing factors for the event
         */
        INVESTIGATION, 
        /**
         * A financial instrument used to track costs, charges or other amounts.
         */
        ACCOUNT, 
        /**
         * A request for payment for goods and/or services.  Includes the idea of a healthcare insurance claim.
         */
        INVOICE, 
        /**
         * The determination of what will be paid against a particular invoice based on coverage, plan rules, etc.
         */
        ADJUDICAT, 
        /**
         * A request for a pre-determination of the cost that would be paid under an insurance plan for a hypothetical claim for goods or services
         */
        PREDETREQ, 
        /**
         * An adjudication of what would be paid under an insurance plan for a hypothetical claim for goods or services
         */
        PREDETERMINE, 
        /**
         * An investigation to determine information about a particular therapy or product
         */
        STUDY, 
        /**
         * A set of (possibly conditional) steps to be taken to achieve some aim.  Includes study protocols, treatment protocols, emergency protocols, etc.
         */
        PROTOCOL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BasicResourceType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("consent".equals(codeString))
          return CONSENT;
        if ("referral".equals(codeString))
          return REFERRAL;
        if ("advevent".equals(codeString))
          return ADVEVENT;
        if ("aptmtreq".equals(codeString))
          return APTMTREQ;
        if ("transfer".equals(codeString))
          return TRANSFER;
        if ("diet".equals(codeString))
          return DIET;
        if ("adminact".equals(codeString))
          return ADMINACT;
        if ("exposure".equals(codeString))
          return EXPOSURE;
        if ("investigation".equals(codeString))
          return INVESTIGATION;
        if ("account".equals(codeString))
          return ACCOUNT;
        if ("invoice".equals(codeString))
          return INVOICE;
        if ("adjudicat".equals(codeString))
          return ADJUDICAT;
        if ("predetreq".equals(codeString))
          return PREDETREQ;
        if ("predetermine".equals(codeString))
          return PREDETERMINE;
        if ("study".equals(codeString))
          return STUDY;
        if ("protocol".equals(codeString))
          return PROTOCOL;
        throw new Exception("Unknown BasicResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONSENT: return "consent";
            case REFERRAL: return "referral";
            case ADVEVENT: return "advevent";
            case APTMTREQ: return "aptmtreq";
            case TRANSFER: return "transfer";
            case DIET: return "diet";
            case ADMINACT: return "adminact";
            case EXPOSURE: return "exposure";
            case INVESTIGATION: return "investigation";
            case ACCOUNT: return "account";
            case INVOICE: return "invoice";
            case ADJUDICAT: return "adjudicat";
            case PREDETREQ: return "predetreq";
            case PREDETERMINE: return "predetermine";
            case STUDY: return "study";
            case PROTOCOL: return "protocol";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/basic-resource-type";
        }
        public String getDefinition() {
          switch (this) {
            case CONSENT: return "An assertion of permission for an activity or set of activities to occur, possibly subject to particular limitations; e.g. surgical consent, information disclosure consent, etc.";
            case REFERRAL: return "A request that care of a particular type be provided to a patient.  Could involve the transfer of care, a consult, etc.";
            case ADVEVENT: return "An undesired reaction caused by exposure to some agent (e.g. a medication, immunization, food, or environmental agent).";
            case APTMTREQ: return "A request that a time be scheduled for a type of service for a specified patient, potentially subject to other constraints";
            case TRANSFER: return "The transition of a patient or set of material from one location to another";
            case DIET: return "The specification of a set of food and/or other nutritional material to be delivered to a patient.";
            case ADMINACT: return "An occurrence of a non-care-related event in the healthcare domain, such as approvals, reviews, etc.";
            case EXPOSURE: return "Record of a situation where a subject was exposed to a substance.  Usually of interest to public health.";
            case INVESTIGATION: return "A formalized inquiry into the circumstances surrounding a particular unplanned event or potential event for the purposes of identifying possible causes and contributing factors for the event";
            case ACCOUNT: return "A financial instrument used to track costs, charges or other amounts.";
            case INVOICE: return "A request for payment for goods and/or services.  Includes the idea of a healthcare insurance claim.";
            case ADJUDICAT: return "The determination of what will be paid against a particular invoice based on coverage, plan rules, etc.";
            case PREDETREQ: return "A request for a pre-determination of the cost that would be paid under an insurance plan for a hypothetical claim for goods or services";
            case PREDETERMINE: return "An adjudication of what would be paid under an insurance plan for a hypothetical claim for goods or services";
            case STUDY: return "An investigation to determine information about a particular therapy or product";
            case PROTOCOL: return "A set of (possibly conditional) steps to be taken to achieve some aim.  Includes study protocols, treatment protocols, emergency protocols, etc.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONSENT: return "Consent";
            case REFERRAL: return "Referral";
            case ADVEVENT: return "Adverse Event";
            case APTMTREQ: return "Appointment Request";
            case TRANSFER: return "Transfer";
            case DIET: return "Diet";
            case ADMINACT: return "Administrative Activity";
            case EXPOSURE: return "Exposure";
            case INVESTIGATION: return "Investigation";
            case ACCOUNT: return "Account";
            case INVOICE: return "Invoice";
            case ADJUDICAT: return "Invoice Adjudication";
            case PREDETREQ: return "Pre-determination Request";
            case PREDETERMINE: return "Predetermination";
            case STUDY: return "Study";
            case PROTOCOL: return "Protocol";
            default: return "?";
          }
    }


}

