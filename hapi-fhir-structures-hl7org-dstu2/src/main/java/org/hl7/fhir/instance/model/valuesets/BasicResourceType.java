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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0


public enum BasicResourceType {

        /**
         * An assertion of permission for an activity or set of activities to occur, possibly subject to particular limitations.  E.g. surgical consent, information disclosure consent, etc.
         */
        CONSENT, 
        /**
         * A request that care of a particular type be provided to a patient.  Could involve the transfer of care, a consult, etc.
         */
        REFERRAL, 
        /**
         * A bounded time-period when a particular set of resources (practioners, devices and/or locations) is available for the delivery of healthcare services.  Used for scheduling.
         */
        SLOT, 
        /**
         * An undesired reaction caused by exposure to some agent (e.g., a medication, immunization, food, or environmental agent).
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
         * The specification of a set of food and/or other nutritonal material to be delivered to a patient.
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
         * A record of a transfer of funds between accounts and/or individuals
         */
        PAYMENT, 
        /**
         * A request for a predication of the cost that would be paid under an insurance plan for a hypothetical claim for goods or services
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
        if ("CONSENT".equals(codeString))
          return CONSENT;
        if ("REFERRAL".equals(codeString))
          return REFERRAL;
        if ("SLOT".equals(codeString))
          return SLOT;
        if ("ADVEVENT".equals(codeString))
          return ADVEVENT;
        if ("APTMTREQ".equals(codeString))
          return APTMTREQ;
        if ("TRANSFER".equals(codeString))
          return TRANSFER;
        if ("DIET".equals(codeString))
          return DIET;
        if ("ADMINACT".equals(codeString))
          return ADMINACT;
        if ("EXPOSURE".equals(codeString))
          return EXPOSURE;
        if ("INVESTIGATION".equals(codeString))
          return INVESTIGATION;
        if ("ACCOUNT".equals(codeString))
          return ACCOUNT;
        if ("INVOICE".equals(codeString))
          return INVOICE;
        if ("ADJUDICAT".equals(codeString))
          return ADJUDICAT;
        if ("PAYMENT".equals(codeString))
          return PAYMENT;
        if ("PREDETREQ".equals(codeString))
          return PREDETREQ;
        if ("PREDETERMINE".equals(codeString))
          return PREDETERMINE;
        if ("STUDY".equals(codeString))
          return STUDY;
        if ("PROTOCOL".equals(codeString))
          return PROTOCOL;
        throw new Exception("Unknown BasicResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONSENT: return "CONSENT";
            case REFERRAL: return "REFERRAL";
            case SLOT: return "SLOT";
            case ADVEVENT: return "ADVEVENT";
            case APTMTREQ: return "APTMTREQ";
            case TRANSFER: return "TRANSFER";
            case DIET: return "DIET";
            case ADMINACT: return "ADMINACT";
            case EXPOSURE: return "EXPOSURE";
            case INVESTIGATION: return "INVESTIGATION";
            case ACCOUNT: return "ACCOUNT";
            case INVOICE: return "INVOICE";
            case ADJUDICAT: return "ADJUDICAT";
            case PAYMENT: return "PAYMENT";
            case PREDETREQ: return "PREDETREQ";
            case PREDETERMINE: return "PREDETERMINE";
            case STUDY: return "STUDY";
            case PROTOCOL: return "PROTOCOL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/basic-resource-type";
        }
        public String getDefinition() {
          switch (this) {
            case CONSENT: return "An assertion of permission for an activity or set of activities to occur, possibly subject to particular limitations.  E.g. surgical consent, information disclosure consent, etc.";
            case REFERRAL: return "A request that care of a particular type be provided to a patient.  Could involve the transfer of care, a consult, etc.";
            case SLOT: return "A bounded time-period when a particular set of resources (practioners, devices and/or locations) is available for the delivery of healthcare services.  Used for scheduling.";
            case ADVEVENT: return "An undesired reaction caused by exposure to some agent (e.g., a medication, immunization, food, or environmental agent).";
            case APTMTREQ: return "A request that a time be scheduled for a type of service for a specified patient, potentially subject to other constraints";
            case TRANSFER: return "The transition of a patient or set of material from one location to another";
            case DIET: return "The specification of a set of food and/or other nutritonal material to be delivered to a patient.";
            case ADMINACT: return "An occurrence of a non-care-related event in the healthcare domain, such as approvals, reviews, etc.";
            case EXPOSURE: return "Record of a situation where a subject was exposed to a substance.  Usually of interest to public health.";
            case INVESTIGATION: return "A formalized inquiry into the circumstances surrounding a particular unplanned event or potential event for the purposes of identifying possible causes and contributing factors for the event";
            case ACCOUNT: return "A financial instrument used to track costs, charges or other amounts.";
            case INVOICE: return "A request for payment for goods and/or services.  Includes the idea of a healthcare insurance claim.";
            case ADJUDICAT: return "The determination of what will be paid against a particular invoice based on coverage, plan rules, etc.";
            case PAYMENT: return "A record of a transfer of funds between accounts and/or individuals";
            case PREDETREQ: return "A request for a predication of the cost that would be paid under an insurance plan for a hypothetical claim for goods or services";
            case PREDETERMINE: return "An adjudication of what would be paid under an insurance plan for a hypothetical claim for goods or services";
            case STUDY: return "An investigation to determine information about a particular therapy or product";
            case PROTOCOL: return "A set of (possibly conditional) steps to be taken to achieve some aim.  Includes study protocols, treatment protocols, emergency protocols, etc.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONSENT: return "consent";
            case REFERRAL: return "referral";
            case SLOT: return "resource slot";
            case ADVEVENT: return "adverse event";
            case APTMTREQ: return "appointment request";
            case TRANSFER: return "transfer";
            case DIET: return "diet";
            case ADMINACT: return "administrative activity";
            case EXPOSURE: return "exposure";
            case INVESTIGATION: return "investigation";
            case ACCOUNT: return "account";
            case INVOICE: return "invoice";
            case ADJUDICAT: return "invoice adjudication";
            case PAYMENT: return "payment";
            case PREDETREQ: return "predetermination request";
            case PREDETERMINE: return "predetermination";
            case STUDY: return "study";
            case PROTOCOL: return "protocol";
            default: return "?";
          }
    }


}

