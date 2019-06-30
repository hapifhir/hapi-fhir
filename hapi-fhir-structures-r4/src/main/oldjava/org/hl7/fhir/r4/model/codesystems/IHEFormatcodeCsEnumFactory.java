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

public class IHEFormatcodeCsEnumFactory implements EnumFactory<IHEFormatcodeCs> {

  public IHEFormatcodeCs fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("urn:ihe:pcc:xphr:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_XPHR_2007;
    if ("urn:ihe:pcc:aps:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_APS_2007;
    if ("urn:ihe:pcc:xds-ms:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_XDSMS_2007;
    if ("urn:ihe:pcc:edr:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_EDR_2007;
    if ("urn:ihe:pcc:edes:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_EDES_2007;
    if ("urn:ihe:pcc:apr:handp:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_APR_HANDP_2008;
    if ("urn:ihe:pcc:apr:lab:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_APR_LAB_2008;
    if ("urn:ihe:pcc:apr:edu:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_APR_EDU_2008;
    if ("urn:ihe:pcc:crc:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_CRC_2008;
    if ("urn:ihe:pcc:cm:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_CM_2008;
    if ("urn:ihe:pcc:ic:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_IC_2008;
    if ("urn:ihe:pcc:tn:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_TN_2007;
    if ("urn:ihe:pcc:nn:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_NN_2007;
    if ("urn:ihe:pcc:ctn:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_CTN_2007;
    if ("urn:ihe:pcc:edpn:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_EDPN_2007;
    if ("urn:ihe:pcc:hp:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_HP_2008;
    if ("urn:ihe:pcc:ldhp:2009".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_LDHP_2009;
    if ("urn:ihe:pcc:lds:2009".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_LDS_2009;
    if ("urn:ihe:pcc:mds:2009".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_MDS_2009;
    if ("urn:ihe:pcc:nds:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_NDS_2010;
    if ("urn:ihe:pcc:ppvs:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_PPVS_2010;
    if ("urn:ihe:pcc:trs:2011".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_TRS_2011;
    if ("urn:ihe:pcc:ets:2011".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_ETS_2011;
    if ("urn:ihe:pcc:its:2011".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_ITS_2011;
    if ("urn:ihe:pcc:ript:2017".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PCC_RIPT_2017;
    if ("urn:ihe:iti:bppc:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_BPPC_2007;
    if ("urn:ihe:iti:bppc-sd:2007".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_BPPCSD_2007;
    if ("urn:ihe:iti:xds-sd:pdf:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_XDSSD_PDF_2008;
    if ("urn:ihe:iti:xds-sd:text:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_XDSSD_TEXT_2008;
    if ("urn:ihe:iti:xdw:2011:workflowDoc".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_XDW_2011_WORKFLOWDOC;
    if ("urn:ihe:iti:dsg:detached:2014".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_DSG_DETACHED_2014;
    if ("urn:ihe:iti:dsg:enveloping:2014".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_DSG_ENVELOPING_2014;
    if ("urn:ihe:iti:appc:2016:consent".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_APPC_2016_CONSENT;
    if ("urn:ihe:iti:xds:2017:mimeTypeSufficient".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT;
    if ("urn:ihe:lab:xd-lab:2008".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_LAB_XDLAB_2008;
    if ("urn:ihe:rad:TEXT".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_RAD_TEXT;
    if ("urn:ihe:rad:PDF".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_RAD_PDF;
    if ("urn:ihe:rad:CDA:ImagingReportStructuredHeadings:2013".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013;
    if ("urn:ihe:card:imaging:2011".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_CARD_IMAGING_2011;
    if ("urn:ihe:card:CRC:2012".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_CARD_CRC_2012;
    if ("urn:ihe:card:EPRC-IE:2014".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_CARD_EPRCIE_2014;
    if ("urn:ihe:dent:TEXT".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_DENT_TEXT;
    if ("urn:ihe:dent:PDF".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_DENT_PDF;
    if ("urn:ihe:dent:CDA:ImagingReportStructuredHeadings:2013".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013;
    if ("urn:ihe:pat:apsr:all:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_ALL_2010;
    if ("urn:ihe:pat:apsr:cancer:all:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_ALL_2010;
    if ("urn:ihe:pat:apsr:cancer:breast:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_BREAST_2010;
    if ("urn:ihe:pat:apsr:cancer:colon:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_COLON_2010;
    if ("urn:ihe:pat:apsr:cancer:prostate:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_PROSTATE_2010;
    if ("urn:ihe:pat:apsr:cancer:thyroid:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_THYROID_2010;
    if ("urn:ihe:pat:apsr:cancer:lung:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LUNG_2010;
    if ("urn:ihe:pat:apsr:cancer:skin:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_SKIN_2010;
    if ("urn:ihe:pat:apsr:cancer:kidney:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_KIDNEY_2010;
    if ("urn:ihe:pat:apsr:cancer:cervix:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_CERVIX_2010;
    if ("urn:ihe:pat:apsr:cancer:endometrium:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010;
    if ("urn:ihe:pat:apsr:cancer:ovary:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_OVARY_2010;
    if ("urn:ihe:pat:apsr:cancer:esophagus:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010;
    if ("urn:ihe:pat:apsr:cancer:stomach:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_STOMACH_2010;
    if ("urn:ihe:pat:apsr:cancer:liver:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LIVER_2010;
    if ("urn:ihe:pat:apsr:cancer:pancreas:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_PANCREAS_2010;
    if ("urn:ihe:pat:apsr:cancer:testis:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_TESTIS_2010;
    if ("urn:ihe:pat:apsr:cancer:urinary_bladder:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010;
    if ("urn:ihe:pat:apsr:cancer:lip_oral_cavity:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010;
    if ("urn:ihe:pat:apsr:cancer:pharynx:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_PHARYNX_2010;
    if ("urn:ihe:pat:apsr:cancer:salivary_gland:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010;
    if ("urn:ihe:pat:apsr:cancer:larynx:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LARYNX_2010;
    if ("urn:ihe:pharm:pre:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PHARM_PRE_2010;
    if ("urn:ihe:pharm:padv:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PHARM_PADV_2010;
    if ("urn:ihe:pharm:dis:2010".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PHARM_DIS_2010;
    if ("urn:ihe:pharm:pml:2013".equals(codeString))
      return IHEFormatcodeCs.URN_IHE_PHARM_PML_2013;
    if ("urn:hl7-org:sdwg:ccda-structuredBody:1.1".equals(codeString))
      return IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1;
    if ("urn:hl7-org:sdwg:ccda-nonXMLBody:1.1".equals(codeString))
      return IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1;
    if ("urn:hl7-org:sdwg:ccda-structuredBody:2.1".equals(codeString))
      return IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1;
    if ("urn:hl7-org:sdwg:ccda-nonXMLBody:2.1".equals(codeString))
      return IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1;
    throw new IllegalArgumentException("Unknown IHEFormatcodeCs code '"+codeString+"'");
  }

  public String toCode(IHEFormatcodeCs code) {
    if (code == IHEFormatcodeCs.URN_IHE_PCC_XPHR_2007)
      return "urn:ihe:pcc:xphr:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_APS_2007)
      return "urn:ihe:pcc:aps:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_XDSMS_2007)
      return "urn:ihe:pcc:xds-ms:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_EDR_2007)
      return "urn:ihe:pcc:edr:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_EDES_2007)
      return "urn:ihe:pcc:edes:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_APR_HANDP_2008)
      return "urn:ihe:pcc:apr:handp:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_APR_LAB_2008)
      return "urn:ihe:pcc:apr:lab:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_APR_EDU_2008)
      return "urn:ihe:pcc:apr:edu:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_CRC_2008)
      return "urn:ihe:pcc:crc:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_CM_2008)
      return "urn:ihe:pcc:cm:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_IC_2008)
      return "urn:ihe:pcc:ic:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_TN_2007)
      return "urn:ihe:pcc:tn:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_NN_2007)
      return "urn:ihe:pcc:nn:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_CTN_2007)
      return "urn:ihe:pcc:ctn:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_EDPN_2007)
      return "urn:ihe:pcc:edpn:2007";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_HP_2008)
      return "urn:ihe:pcc:hp:2008";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_LDHP_2009)
      return "urn:ihe:pcc:ldhp:2009";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_LDS_2009)
      return "urn:ihe:pcc:lds:2009";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_MDS_2009)
      return "urn:ihe:pcc:mds:2009";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_NDS_2010)
      return "urn:ihe:pcc:nds:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_PPVS_2010)
      return "urn:ihe:pcc:ppvs:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_TRS_2011)
      return "urn:ihe:pcc:trs:2011";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_ETS_2011)
      return "urn:ihe:pcc:ets:2011";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_ITS_2011)
      return "urn:ihe:pcc:its:2011";
    if (code == IHEFormatcodeCs.URN_IHE_PCC_RIPT_2017)
      return "urn:ihe:pcc:ript:2017";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_BPPC_2007)
      return "urn:ihe:iti:bppc:2007";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_BPPCSD_2007)
      return "urn:ihe:iti:bppc-sd:2007";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_XDSSD_PDF_2008)
      return "urn:ihe:iti:xds-sd:pdf:2008";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_XDSSD_TEXT_2008)
      return "urn:ihe:iti:xds-sd:text:2008";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_XDW_2011_WORKFLOWDOC)
      return "urn:ihe:iti:xdw:2011:workflowDoc";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_DSG_DETACHED_2014)
      return "urn:ihe:iti:dsg:detached:2014";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_DSG_ENVELOPING_2014)
      return "urn:ihe:iti:dsg:enveloping:2014";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_APPC_2016_CONSENT)
      return "urn:ihe:iti:appc:2016:consent";
    if (code == IHEFormatcodeCs.URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT)
      return "urn:ihe:iti:xds:2017:mimeTypeSufficient";
    if (code == IHEFormatcodeCs.URN_IHE_LAB_XDLAB_2008)
      return "urn:ihe:lab:xd-lab:2008";
    if (code == IHEFormatcodeCs.URN_IHE_RAD_TEXT)
      return "urn:ihe:rad:TEXT";
    if (code == IHEFormatcodeCs.URN_IHE_RAD_PDF)
      return "urn:ihe:rad:PDF";
    if (code == IHEFormatcodeCs.URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013)
      return "urn:ihe:rad:CDA:ImagingReportStructuredHeadings:2013";
    if (code == IHEFormatcodeCs.URN_IHE_CARD_IMAGING_2011)
      return "urn:ihe:card:imaging:2011";
    if (code == IHEFormatcodeCs.URN_IHE_CARD_CRC_2012)
      return "urn:ihe:card:CRC:2012";
    if (code == IHEFormatcodeCs.URN_IHE_CARD_EPRCIE_2014)
      return "urn:ihe:card:EPRC-IE:2014";
    if (code == IHEFormatcodeCs.URN_IHE_DENT_TEXT)
      return "urn:ihe:dent:TEXT";
    if (code == IHEFormatcodeCs.URN_IHE_DENT_PDF)
      return "urn:ihe:dent:PDF";
    if (code == IHEFormatcodeCs.URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013)
      return "urn:ihe:dent:CDA:ImagingReportStructuredHeadings:2013";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_ALL_2010)
      return "urn:ihe:pat:apsr:all:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_ALL_2010)
      return "urn:ihe:pat:apsr:cancer:all:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_BREAST_2010)
      return "urn:ihe:pat:apsr:cancer:breast:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_COLON_2010)
      return "urn:ihe:pat:apsr:cancer:colon:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_PROSTATE_2010)
      return "urn:ihe:pat:apsr:cancer:prostate:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_THYROID_2010)
      return "urn:ihe:pat:apsr:cancer:thyroid:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LUNG_2010)
      return "urn:ihe:pat:apsr:cancer:lung:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_SKIN_2010)
      return "urn:ihe:pat:apsr:cancer:skin:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_KIDNEY_2010)
      return "urn:ihe:pat:apsr:cancer:kidney:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_CERVIX_2010)
      return "urn:ihe:pat:apsr:cancer:cervix:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010)
      return "urn:ihe:pat:apsr:cancer:endometrium:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_OVARY_2010)
      return "urn:ihe:pat:apsr:cancer:ovary:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010)
      return "urn:ihe:pat:apsr:cancer:esophagus:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_STOMACH_2010)
      return "urn:ihe:pat:apsr:cancer:stomach:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LIVER_2010)
      return "urn:ihe:pat:apsr:cancer:liver:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_PANCREAS_2010)
      return "urn:ihe:pat:apsr:cancer:pancreas:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_TESTIS_2010)
      return "urn:ihe:pat:apsr:cancer:testis:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010)
      return "urn:ihe:pat:apsr:cancer:urinary_bladder:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010)
      return "urn:ihe:pat:apsr:cancer:lip_oral_cavity:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_PHARYNX_2010)
      return "urn:ihe:pat:apsr:cancer:pharynx:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010)
      return "urn:ihe:pat:apsr:cancer:salivary_gland:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PAT_APSR_CANCER_LARYNX_2010)
      return "urn:ihe:pat:apsr:cancer:larynx:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PHARM_PRE_2010)
      return "urn:ihe:pharm:pre:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PHARM_PADV_2010)
      return "urn:ihe:pharm:padv:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PHARM_DIS_2010)
      return "urn:ihe:pharm:dis:2010";
    if (code == IHEFormatcodeCs.URN_IHE_PHARM_PML_2013)
      return "urn:ihe:pharm:pml:2013";
    if (code == IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1)
      return "urn:hl7-org:sdwg:ccda-structuredBody:1.1";
    if (code == IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1)
      return "urn:hl7-org:sdwg:ccda-nonXMLBody:1.1";
    if (code == IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1)
      return "urn:hl7-org:sdwg:ccda-structuredBody:2.1";
    if (code == IHEFormatcodeCs.URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1)
      return "urn:hl7-org:sdwg:ccda-nonXMLBody:2.1";
    return "?";
  }

    public String toSystem(IHEFormatcodeCs code) {
      return code.getSystem();
      }

}

