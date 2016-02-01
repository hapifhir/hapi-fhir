package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3PatientImportance {

        /**
         * Board member of health care organization
         */
        BM, 
        /**
         * Family member of staff physician
         */
        DFM, 
        /**
         * Member of the health care organization physician staff
         */
        DR, 
        /**
         * Financial donor to the health care organization
         */
        FD, 
        /**
         * Foreign citizen dignitary of interest to the health care organization
         */
        FOR, 
        /**
         * Government dignitary of interest to the organization
         */
        GOVT, 
        /**
         * Family member of staff member
         */
        SFM, 
        /**
         * Staff member of the health care organization
         */
        STF, 
        /**
         * Very important person of interest to the health care organization
         */
        VIP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3PatientImportance fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BM".equals(codeString))
          return BM;
        if ("DFM".equals(codeString))
          return DFM;
        if ("DR".equals(codeString))
          return DR;
        if ("FD".equals(codeString))
          return FD;
        if ("FOR".equals(codeString))
          return FOR;
        if ("GOVT".equals(codeString))
          return GOVT;
        if ("SFM".equals(codeString))
          return SFM;
        if ("STF".equals(codeString))
          return STF;
        if ("VIP".equals(codeString))
          return VIP;
        throw new FHIRException("Unknown V3PatientImportance code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BM: return "BM";
            case DFM: return "DFM";
            case DR: return "DR";
            case FD: return "FD";
            case FOR: return "FOR";
            case GOVT: return "GOVT";
            case SFM: return "SFM";
            case STF: return "STF";
            case VIP: return "VIP";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/PatientImportance";
        }
        public String getDefinition() {
          switch (this) {
            case BM: return "Board member of health care organization";
            case DFM: return "Family member of staff physician";
            case DR: return "Member of the health care organization physician staff";
            case FD: return "Financial donor to the health care organization";
            case FOR: return "Foreign citizen dignitary of interest to the health care organization";
            case GOVT: return "Government dignitary of interest to the organization";
            case SFM: return "Family member of staff member";
            case STF: return "Staff member of the health care organization";
            case VIP: return "Very important person of interest to the health care organization";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BM: return "Board Member";
            case DFM: return "Physician Family Member";
            case DR: return "Staff Physician";
            case FD: return "Financial Donor";
            case FOR: return "Foreign Dignitary";
            case GOVT: return "Government Dignitary";
            case SFM: return "Staff Family Member";
            case STF: return "Staff Member";
            case VIP: return "Very Important Person";
            default: return "?";
          }
    }


}

