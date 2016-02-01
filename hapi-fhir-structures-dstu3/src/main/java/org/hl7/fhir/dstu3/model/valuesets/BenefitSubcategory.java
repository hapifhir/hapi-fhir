package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum BenefitSubcategory {

        /**
         * Dental Basic Coverage - exams, fillings, xrays, cleaning, minor procedures
         */
        ORALBASIC, 
        /**
         * Dental Major Coverage - crowns, bridge, major procedures
         */
        ORALMAJOR, 
        /**
         * Dental Orthodontic Coverage - braces, adjustments, ortho exams, related procedures
         */
        ORALORTHO, 
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
        public static BenefitSubcategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oral-basic".equals(codeString))
          return ORALBASIC;
        if ("oral-major".equals(codeString))
          return ORALMAJOR;
        if ("oral-ortho".equals(codeString))
          return ORALORTHO;
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
        throw new FHIRException("Unknown BenefitSubcategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORALBASIC: return "oral-basic";
            case ORALMAJOR: return "oral-major";
            case ORALORTHO: return "oral-ortho";
            case VISIONEXAM: return "vision-exam";
            case VISIONGLASSES: return "vision-glasses";
            case VISIONCONTACTS: return "vision-contacts";
            case MEDICALPRIMARYCARE: return "medical-primarycare";
            case PHARMACYDISPENSE: return "pharmacy-dispense";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-subcategory";
        }
        public String getDefinition() {
          switch (this) {
            case ORALBASIC: return "Dental Basic Coverage - exams, fillings, xrays, cleaning, minor procedures";
            case ORALMAJOR: return "Dental Major Coverage - crowns, bridge, major procedures";
            case ORALORTHO: return "Dental Orthodontic Coverage - braces, adjustments, ortho exams, related procedures";
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
            case ORALBASIC: return "Dental: Basic Coverage";
            case ORALMAJOR: return "Dental: Major Coverage";
            case ORALORTHO: return "Dental: Orthodontic Coverage";
            case VISIONEXAM: return "Vision Exam";
            case VISIONGLASSES: return "Vision Glasses";
            case VISIONCONTACTS: return "Vision Contacts Coverage";
            case MEDICALPRIMARYCARE: return "Medical Primary Health Coverage";
            case PHARMACYDISPENSE: return "Pharmacy Dispense Coverage";
            default: return "?";
          }
    }


}

