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

public enum AppointmentCancellationReason {

        /**
         * null
         */
        PAT, 
        /**
         * null
         */
        PATCRS, 
        /**
         * null
         */
        PATCPP, 
        /**
         * null
         */
        PATDEC, 
        /**
         * null
         */
        PATFB, 
        /**
         * null
         */
        PATLT, 
        /**
         * null
         */
        PATMT, 
        /**
         * null
         */
        PATMV, 
        /**
         * null
         */
        PATPREG, 
        /**
         * null
         */
        PATSWL, 
        /**
         * null
         */
        PATUCP, 
        /**
         * null
         */
        PROV, 
        /**
         * null
         */
        PROVPERS, 
        /**
         * null
         */
        PROVDCH, 
        /**
         * null
         */
        PROVEDU, 
        /**
         * null
         */
        PROVHOSP, 
        /**
         * null
         */
        PROVLABS, 
        /**
         * null
         */
        PROVMRI, 
        /**
         * null
         */
        PROVONC, 
        /**
         * null
         */
        MAINT, 
        /**
         * null
         */
        MEDSINC, 
        /**
         * null
         */
        OTHER, 
        /**
         * null
         */
        OTHCMS, 
        /**
         * null
         */
        OTHERR, 
        /**
         * null
         */
        OTHFIN, 
        /**
         * null
         */
        OTHIV, 
        /**
         * null
         */
        OTHINT, 
        /**
         * null
         */
        OTHMU, 
        /**
         * null
         */
        OTHROOM, 
        /**
         * null
         */
        OTHOERR, 
        /**
         * null
         */
        OTHSWIE, 
        /**
         * null
         */
        OTHWEATH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AppointmentCancellationReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pat".equals(codeString))
          return PAT;
        if ("pat-crs".equals(codeString))
          return PATCRS;
        if ("pat-cpp".equals(codeString))
          return PATCPP;
        if ("pat-dec".equals(codeString))
          return PATDEC;
        if ("pat-fb".equals(codeString))
          return PATFB;
        if ("pat-lt".equals(codeString))
          return PATLT;
        if ("pat-mt".equals(codeString))
          return PATMT;
        if ("pat-mv".equals(codeString))
          return PATMV;
        if ("pat-preg".equals(codeString))
          return PATPREG;
        if ("pat-swl".equals(codeString))
          return PATSWL;
        if ("pat-ucp".equals(codeString))
          return PATUCP;
        if ("prov".equals(codeString))
          return PROV;
        if ("prov-pers".equals(codeString))
          return PROVPERS;
        if ("prov-dch".equals(codeString))
          return PROVDCH;
        if ("prov-edu".equals(codeString))
          return PROVEDU;
        if ("prov-hosp".equals(codeString))
          return PROVHOSP;
        if ("prov-labs".equals(codeString))
          return PROVLABS;
        if ("prov-mri".equals(codeString))
          return PROVMRI;
        if ("prov-onc".equals(codeString))
          return PROVONC;
        if ("maint".equals(codeString))
          return MAINT;
        if ("meds-inc".equals(codeString))
          return MEDSINC;
        if ("other".equals(codeString))
          return OTHER;
        if ("oth-cms".equals(codeString))
          return OTHCMS;
        if ("oth-err".equals(codeString))
          return OTHERR;
        if ("oth-fin".equals(codeString))
          return OTHFIN;
        if ("oth-iv".equals(codeString))
          return OTHIV;
        if ("oth-int".equals(codeString))
          return OTHINT;
        if ("oth-mu".equals(codeString))
          return OTHMU;
        if ("oth-room".equals(codeString))
          return OTHROOM;
        if ("oth-oerr".equals(codeString))
          return OTHOERR;
        if ("oth-swie".equals(codeString))
          return OTHSWIE;
        if ("oth-weath".equals(codeString))
          return OTHWEATH;
        throw new FHIRException("Unknown AppointmentCancellationReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PAT: return "pat";
            case PATCRS: return "pat-crs";
            case PATCPP: return "pat-cpp";
            case PATDEC: return "pat-dec";
            case PATFB: return "pat-fb";
            case PATLT: return "pat-lt";
            case PATMT: return "pat-mt";
            case PATMV: return "pat-mv";
            case PATPREG: return "pat-preg";
            case PATSWL: return "pat-swl";
            case PATUCP: return "pat-ucp";
            case PROV: return "prov";
            case PROVPERS: return "prov-pers";
            case PROVDCH: return "prov-dch";
            case PROVEDU: return "prov-edu";
            case PROVHOSP: return "prov-hosp";
            case PROVLABS: return "prov-labs";
            case PROVMRI: return "prov-mri";
            case PROVONC: return "prov-onc";
            case MAINT: return "maint";
            case MEDSINC: return "meds-inc";
            case OTHER: return "other";
            case OTHCMS: return "oth-cms";
            case OTHERR: return "oth-err";
            case OTHFIN: return "oth-fin";
            case OTHIV: return "oth-iv";
            case OTHINT: return "oth-int";
            case OTHMU: return "oth-mu";
            case OTHROOM: return "oth-room";
            case OTHOERR: return "oth-oerr";
            case OTHSWIE: return "oth-swie";
            case OTHWEATH: return "oth-weath";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/appointment-cancellation-reason";
        }
        public String getDefinition() {
          switch (this) {
            case PAT: return "";
            case PATCRS: return "";
            case PATCPP: return "";
            case PATDEC: return "";
            case PATFB: return "";
            case PATLT: return "";
            case PATMT: return "";
            case PATMV: return "";
            case PATPREG: return "";
            case PATSWL: return "";
            case PATUCP: return "";
            case PROV: return "";
            case PROVPERS: return "";
            case PROVDCH: return "";
            case PROVEDU: return "";
            case PROVHOSP: return "";
            case PROVLABS: return "";
            case PROVMRI: return "";
            case PROVONC: return "";
            case MAINT: return "";
            case MEDSINC: return "";
            case OTHER: return "";
            case OTHCMS: return "";
            case OTHERR: return "";
            case OTHFIN: return "";
            case OTHIV: return "";
            case OTHINT: return "";
            case OTHMU: return "";
            case OTHROOM: return "";
            case OTHOERR: return "";
            case OTHSWIE: return "";
            case OTHWEATH: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PAT: return "Patient";
            case PATCRS: return "Patient: Canceled via automated reminder system";
            case PATCPP: return "Patient: Canceled via Patient Portal";
            case PATDEC: return "Patient: Deceased";
            case PATFB: return "Patient: Feeling Better";
            case PATLT: return "Patient: Lack of Transportation";
            case PATMT: return "Patient: Member Terminated";
            case PATMV: return "Patient: Moved";
            case PATPREG: return "Patient: Pregnant";
            case PATSWL: return "Patient: Scheduled from Wait List";
            case PATUCP: return "Patient: Unhappy/Changed Provider";
            case PROV: return "Provider";
            case PROVPERS: return "Provider: Personal";
            case PROVDCH: return "Provider: Discharged";
            case PROVEDU: return "Provider: Edu/Meeting";
            case PROVHOSP: return "Provider: Hospitalized";
            case PROVLABS: return "Provider: Labs Out of Acceptable Range";
            case PROVMRI: return "Provider: MRI Screening Form Marked Do Not Proceed";
            case PROVONC: return "Provider: Oncology Treatment Plan Changes";
            case MAINT: return "Equipment Maintenance/Repair";
            case MEDSINC: return "Prep/Med Incomplete";
            case OTHER: return "Other";
            case OTHCMS: return "Other: CMS Therapy Cap Service Not Authorized";
            case OTHERR: return "Other: Error";
            case OTHFIN: return "Other: Financial";
            case OTHIV: return "Other: Improper IV Access/Infiltrate IV";
            case OTHINT: return "Other: No Interpreter Available";
            case OTHMU: return "Other: Prep/Med/Results Unavailable";
            case OTHROOM: return "Other: Room/Resource Maintenance";
            case OTHOERR: return "Other: Schedule Order Error";
            case OTHSWIE: return "Other: Silent Walk In Error";
            case OTHWEATH: return "Other: Weather";
            default: return "?";
          }
    }


}

