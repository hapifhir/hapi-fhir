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

public enum MedicationdispenseStatusReason {

        /**
         * The order has been stopped by the prescriber but this fact has not necessarily captured electronically. Example: A verbal stop, a fax, etc.
         */
        FRR01, 
        /**
         * Order has not been fulfilled within a reasonable amount of time, and might not be current.
         */
        FRR02, 
        /**
         * Data needed to safely act on the order which was expected to become available independent of the order is not yet available. Example: Lab results, diagnostic imaging, etc.
         */
        FRR03, 
        /**
         * Product not available or manufactured. Cannot supply.
         */
        FRR04, 
        /**
         * The dispenser has ethical, religious or moral objections to fulfilling the order/dispensing the product.
         */
        FRR05, 
        /**
         * Fulfiller not able to provide appropriate care associated with fulfilling the order. Example: Therapy requires ongoing monitoring by fulfiller and fulfiller will be ending practice, leaving town, unable to schedule necessary time, etc.
         */
        FRR06, 
        /**
         * This therapy has been ordered as a backup to a preferred therapy. This order will be released when and if the preferred therapy is unsuccessful.
         */
        ALTCHOICE, 
        /**
         * Clarification is required before the order can be acted upon.
         */
        CLARIF, 
        /**
         * The current level of the medication in the patient's system is too high. The medication is suspended to allow the level to subside to a safer level.
         */
        DRUGHIGH, 
        /**
         * The patient has been admitted to a care facility and their community medications are suspended until hospital discharge.
         */
        HOSPADM, 
        /**
         * The therapy would interfere with a planned lab test and the therapy is being withdrawn until the test is completed.
         */
        LABINT, 
        /**
         * Patient not available for a period of time due to a scheduled therapy, leave of absence or other reason.
         */
        NONAVAIL, 
        /**
         * The patient is pregnant or breast feeding. The therapy will be resumed when the pregnancy is complete and the patient is no longer breastfeeding.
         */
        PREG, 
        /**
         * The patient is believed to be allergic to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.
         */
        SAIG, 
        /**
         * The drug interacts with a short-term treatment that is more urgently required. This order will be resumed when the short-term treatment is complete.
         */
        SDDI, 
        /**
         * Another short-term co-occurring therapy fulfills the same purpose as this therapy. This therapy will be resumed when the co-occuring therapy is complete.
         */
        SDUPTHER, 
        /**
         * The patient is believed to have an intolerance to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.
         */
        SINTOL, 
        /**
         * The drug is contraindicated for patients receiving surgery and the patient is scheduled to be admitted for surgery in the near future. The drug will be resumed when the patient has sufficiently recovered from the surgery.
         */
        SURG, 
        /**
         * The patient was previously receiving a medication contraindicated with the current medication. The current medication will remain on hold until the prior medication has been cleansed from their system.
         */
        WASHOUT, 
        /**
         * Drug out of stock. Cannot supply.
         */
        OUTOFSTOCK, 
        /**
         * Drug no longer marketed Cannot supply.
         */
        OFFMARKET, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationdispenseStatusReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("frr01".equals(codeString))
          return FRR01;
        if ("frr02".equals(codeString))
          return FRR02;
        if ("frr03".equals(codeString))
          return FRR03;
        if ("frr04".equals(codeString))
          return FRR04;
        if ("frr05".equals(codeString))
          return FRR05;
        if ("frr06".equals(codeString))
          return FRR06;
        if ("altchoice".equals(codeString))
          return ALTCHOICE;
        if ("clarif".equals(codeString))
          return CLARIF;
        if ("drughigh".equals(codeString))
          return DRUGHIGH;
        if ("hospadm".equals(codeString))
          return HOSPADM;
        if ("labint".equals(codeString))
          return LABINT;
        if ("non-avail".equals(codeString))
          return NONAVAIL;
        if ("preg".equals(codeString))
          return PREG;
        if ("saig".equals(codeString))
          return SAIG;
        if ("sddi".equals(codeString))
          return SDDI;
        if ("sdupther".equals(codeString))
          return SDUPTHER;
        if ("sintol".equals(codeString))
          return SINTOL;
        if ("surg".equals(codeString))
          return SURG;
        if ("washout".equals(codeString))
          return WASHOUT;
        if ("outofstock".equals(codeString))
          return OUTOFSTOCK;
        if ("offmarket".equals(codeString))
          return OFFMARKET;
        throw new FHIRException("Unknown MedicationdispenseStatusReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FRR01: return "frr01";
            case FRR02: return "frr02";
            case FRR03: return "frr03";
            case FRR04: return "frr04";
            case FRR05: return "frr05";
            case FRR06: return "frr06";
            case ALTCHOICE: return "altchoice";
            case CLARIF: return "clarif";
            case DRUGHIGH: return "drughigh";
            case HOSPADM: return "hospadm";
            case LABINT: return "labint";
            case NONAVAIL: return "non-avail";
            case PREG: return "preg";
            case SAIG: return "saig";
            case SDDI: return "sddi";
            case SDUPTHER: return "sdupther";
            case SINTOL: return "sintol";
            case SURG: return "surg";
            case WASHOUT: return "washout";
            case OUTOFSTOCK: return "outofstock";
            case OFFMARKET: return "offmarket";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/fhir/CodeSystem/medicationdispense-status-reason";
        }
        public String getDefinition() {
          switch (this) {
            case FRR01: return "The order has been stopped by the prescriber but this fact has not necessarily captured electronically. Example: A verbal stop, a fax, etc.";
            case FRR02: return "Order has not been fulfilled within a reasonable amount of time, and might not be current.";
            case FRR03: return "Data needed to safely act on the order which was expected to become available independent of the order is not yet available. Example: Lab results, diagnostic imaging, etc.";
            case FRR04: return "Product not available or manufactured. Cannot supply.";
            case FRR05: return "The dispenser has ethical, religious or moral objections to fulfilling the order/dispensing the product.";
            case FRR06: return "Fulfiller not able to provide appropriate care associated with fulfilling the order. Example: Therapy requires ongoing monitoring by fulfiller and fulfiller will be ending practice, leaving town, unable to schedule necessary time, etc.";
            case ALTCHOICE: return "This therapy has been ordered as a backup to a preferred therapy. This order will be released when and if the preferred therapy is unsuccessful.";
            case CLARIF: return "Clarification is required before the order can be acted upon.";
            case DRUGHIGH: return "The current level of the medication in the patient's system is too high. The medication is suspended to allow the level to subside to a safer level.";
            case HOSPADM: return "The patient has been admitted to a care facility and their community medications are suspended until hospital discharge.";
            case LABINT: return "The therapy would interfere with a planned lab test and the therapy is being withdrawn until the test is completed.";
            case NONAVAIL: return "Patient not available for a period of time due to a scheduled therapy, leave of absence or other reason.";
            case PREG: return "The patient is pregnant or breast feeding. The therapy will be resumed when the pregnancy is complete and the patient is no longer breastfeeding.";
            case SAIG: return "The patient is believed to be allergic to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.";
            case SDDI: return "The drug interacts with a short-term treatment that is more urgently required. This order will be resumed when the short-term treatment is complete.";
            case SDUPTHER: return "Another short-term co-occurring therapy fulfills the same purpose as this therapy. This therapy will be resumed when the co-occuring therapy is complete.";
            case SINTOL: return "The patient is believed to have an intolerance to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.";
            case SURG: return "The drug is contraindicated for patients receiving surgery and the patient is scheduled to be admitted for surgery in the near future. The drug will be resumed when the patient has sufficiently recovered from the surgery.";
            case WASHOUT: return "The patient was previously receiving a medication contraindicated with the current medication. The current medication will remain on hold until the prior medication has been cleansed from their system.";
            case OUTOFSTOCK: return "Drug out of stock. Cannot supply.";
            case OFFMARKET: return "Drug no longer marketed Cannot supply.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FRR01: return "Order Stopped";
            case FRR02: return "Stale-dated Order";
            case FRR03: return "Incomplete data";
            case FRR04: return "Product unavailable";
            case FRR05: return "Ethical/religious";
            case FRR06: return "Unable to provide care";
            case ALTCHOICE: return "Try another treatment first";
            case CLARIF: return "Prescription/Request requires clarification";
            case DRUGHIGH: return "Drug level too high";
            case HOSPADM: return "Admission to hospital";
            case LABINT: return "Lab interference issues";
            case NONAVAIL: return "Patient not available";
            case PREG: return "Patient is pregnant or breastfeeding";
            case SAIG: return "Allergy";
            case SDDI: return "Drug interacts with another drug";
            case SDUPTHER: return "Duplicate therapy";
            case SINTOL: return "Suspected intolerance";
            case SURG: return "Patient scheduled for surgery";
            case WASHOUT: return "Washout";
            case OUTOFSTOCK: return "Drug not available - out of stock";
            case OFFMARKET: return "Drug not available - off market";
            default: return "?";
          }
    }


}

