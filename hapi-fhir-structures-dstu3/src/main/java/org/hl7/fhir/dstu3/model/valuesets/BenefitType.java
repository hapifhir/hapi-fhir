package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum BenefitType {

        /**
         * Cost to be incurred before benefits are applied
         */
        DEDUCTABLE, 
        /**
         * Service visit
         */
        VISIT, 
        /**
         * Copayment per service
         */
        COPAY, 
        /**
         * Vision Exam
         */
        VISIONEXAM, 
        /**
         * Frames and lenses
         */
        VISIONGLASSES, 
        /**
         * Contact Lenses
         */
        VISIONCONTACTS, 
        /**
         * Medical Primary Health Coverage
         */
        MEDICALPRIMARYCARE, 
        /**
         * Pharmacy Dispense Coverage
         */
        PHARMACYDISPENSE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("deductable".equals(codeString))
          return DEDUCTABLE;
        if ("visit".equals(codeString))
          return VISIT;
        if ("copay".equals(codeString))
          return COPAY;
        if ("vision-exam".equals(codeString))
          return VISIONEXAM;
        if ("vision-glasses".equals(codeString))
          return VISIONGLASSES;
        if ("vision-contacts".equals(codeString))
          return VISIONCONTACTS;
        if ("medical-primarycare".equals(codeString))
          return MEDICALPRIMARYCARE;
        if ("pharmacy-dispense".equals(codeString))
          return PHARMACYDISPENSE;
        throw new FHIRException("Unknown BenefitType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DEDUCTABLE: return "deductable";
            case VISIT: return "visit";
            case COPAY: return "copay";
            case VISIONEXAM: return "vision-exam";
            case VISIONGLASSES: return "vision-glasses";
            case VISIONCONTACTS: return "vision-contacts";
            case MEDICALPRIMARYCARE: return "medical-primarycare";
            case PHARMACYDISPENSE: return "pharmacy-dispense";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-type";
        }
        public String getDefinition() {
          switch (this) {
            case DEDUCTABLE: return "Cost to be incurred before benefits are applied";
            case VISIT: return "Service visit";
            case COPAY: return "Copayment per service";
            case VISIONEXAM: return "Vision Exam";
            case VISIONGLASSES: return "Frames and lenses";
            case VISIONCONTACTS: return "Contact Lenses";
            case MEDICALPRIMARYCARE: return "Medical Primary Health Coverage";
            case PHARMACYDISPENSE: return "Pharmacy Dispense Coverage";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DEDUCTABLE: return "Deductable";
            case VISIT: return "Visit";
            case COPAY: return "Copayment per service";
            case VISIONEXAM: return "Vision Exam";
            case VISIONGLASSES: return "Vision Glasses";
            case VISIONCONTACTS: return "Vision Contacts Coverage";
            case MEDICALPRIMARYCARE: return "Medical Primary Health Coverage";
            case PHARMACYDISPENSE: return "Pharmacy Dispense Coverage";
            default: return "?";
          }
    }


}

