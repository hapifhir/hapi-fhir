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


import org.hl7.fhir.r4.model.EnumFactory;

public class AppointmentCancellationReasonEnumFactory implements EnumFactory<AppointmentCancellationReason> {

  public AppointmentCancellationReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("pat".equals(codeString))
      return AppointmentCancellationReason.PAT;
    if ("pat-crs".equals(codeString))
      return AppointmentCancellationReason.PATCRS;
    if ("pat-cpp".equals(codeString))
      return AppointmentCancellationReason.PATCPP;
    if ("pat-dec".equals(codeString))
      return AppointmentCancellationReason.PATDEC;
    if ("pat-fb".equals(codeString))
      return AppointmentCancellationReason.PATFB;
    if ("pat-lt".equals(codeString))
      return AppointmentCancellationReason.PATLT;
    if ("pat-mt".equals(codeString))
      return AppointmentCancellationReason.PATMT;
    if ("pat-mv".equals(codeString))
      return AppointmentCancellationReason.PATMV;
    if ("pat-preg".equals(codeString))
      return AppointmentCancellationReason.PATPREG;
    if ("pat-swl".equals(codeString))
      return AppointmentCancellationReason.PATSWL;
    if ("pat-ucp".equals(codeString))
      return AppointmentCancellationReason.PATUCP;
    if ("prov".equals(codeString))
      return AppointmentCancellationReason.PROV;
    if ("prov-pers".equals(codeString))
      return AppointmentCancellationReason.PROVPERS;
    if ("prov-dch".equals(codeString))
      return AppointmentCancellationReason.PROVDCH;
    if ("prov-edu".equals(codeString))
      return AppointmentCancellationReason.PROVEDU;
    if ("prov-hosp".equals(codeString))
      return AppointmentCancellationReason.PROVHOSP;
    if ("prov-labs".equals(codeString))
      return AppointmentCancellationReason.PROVLABS;
    if ("prov-mri".equals(codeString))
      return AppointmentCancellationReason.PROVMRI;
    if ("prov-onc".equals(codeString))
      return AppointmentCancellationReason.PROVONC;
    if ("maint".equals(codeString))
      return AppointmentCancellationReason.MAINT;
    if ("meds-inc".equals(codeString))
      return AppointmentCancellationReason.MEDSINC;
    if ("other".equals(codeString))
      return AppointmentCancellationReason.OTHER;
    if ("oth-cms".equals(codeString))
      return AppointmentCancellationReason.OTHCMS;
    if ("oth-err".equals(codeString))
      return AppointmentCancellationReason.OTHERR;
    if ("oth-fin".equals(codeString))
      return AppointmentCancellationReason.OTHFIN;
    if ("oth-iv".equals(codeString))
      return AppointmentCancellationReason.OTHIV;
    if ("oth-int".equals(codeString))
      return AppointmentCancellationReason.OTHINT;
    if ("oth-mu".equals(codeString))
      return AppointmentCancellationReason.OTHMU;
    if ("oth-room".equals(codeString))
      return AppointmentCancellationReason.OTHROOM;
    if ("oth-oerr".equals(codeString))
      return AppointmentCancellationReason.OTHOERR;
    if ("oth-swie".equals(codeString))
      return AppointmentCancellationReason.OTHSWIE;
    if ("oth-weath".equals(codeString))
      return AppointmentCancellationReason.OTHWEATH;
    throw new IllegalArgumentException("Unknown AppointmentCancellationReason code '"+codeString+"'");
  }

  public String toCode(AppointmentCancellationReason code) {
    if (code == AppointmentCancellationReason.PAT)
      return "pat";
    if (code == AppointmentCancellationReason.PATCRS)
      return "pat-crs";
    if (code == AppointmentCancellationReason.PATCPP)
      return "pat-cpp";
    if (code == AppointmentCancellationReason.PATDEC)
      return "pat-dec";
    if (code == AppointmentCancellationReason.PATFB)
      return "pat-fb";
    if (code == AppointmentCancellationReason.PATLT)
      return "pat-lt";
    if (code == AppointmentCancellationReason.PATMT)
      return "pat-mt";
    if (code == AppointmentCancellationReason.PATMV)
      return "pat-mv";
    if (code == AppointmentCancellationReason.PATPREG)
      return "pat-preg";
    if (code == AppointmentCancellationReason.PATSWL)
      return "pat-swl";
    if (code == AppointmentCancellationReason.PATUCP)
      return "pat-ucp";
    if (code == AppointmentCancellationReason.PROV)
      return "prov";
    if (code == AppointmentCancellationReason.PROVPERS)
      return "prov-pers";
    if (code == AppointmentCancellationReason.PROVDCH)
      return "prov-dch";
    if (code == AppointmentCancellationReason.PROVEDU)
      return "prov-edu";
    if (code == AppointmentCancellationReason.PROVHOSP)
      return "prov-hosp";
    if (code == AppointmentCancellationReason.PROVLABS)
      return "prov-labs";
    if (code == AppointmentCancellationReason.PROVMRI)
      return "prov-mri";
    if (code == AppointmentCancellationReason.PROVONC)
      return "prov-onc";
    if (code == AppointmentCancellationReason.MAINT)
      return "maint";
    if (code == AppointmentCancellationReason.MEDSINC)
      return "meds-inc";
    if (code == AppointmentCancellationReason.OTHER)
      return "other";
    if (code == AppointmentCancellationReason.OTHCMS)
      return "oth-cms";
    if (code == AppointmentCancellationReason.OTHERR)
      return "oth-err";
    if (code == AppointmentCancellationReason.OTHFIN)
      return "oth-fin";
    if (code == AppointmentCancellationReason.OTHIV)
      return "oth-iv";
    if (code == AppointmentCancellationReason.OTHINT)
      return "oth-int";
    if (code == AppointmentCancellationReason.OTHMU)
      return "oth-mu";
    if (code == AppointmentCancellationReason.OTHROOM)
      return "oth-room";
    if (code == AppointmentCancellationReason.OTHOERR)
      return "oth-oerr";
    if (code == AppointmentCancellationReason.OTHSWIE)
      return "oth-swie";
    if (code == AppointmentCancellationReason.OTHWEATH)
      return "oth-weath";
    return "?";
  }

    public String toSystem(AppointmentCancellationReason code) {
      return code.getSystem();
      }

}

