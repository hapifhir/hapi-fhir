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

public enum IHEFormatcodeCs {

        /**
         * null
         */
        URN_IHE_PCC_XPHR_2007, 
        /**
         * null
         */
        URN_IHE_PCC_APS_2007, 
        /**
         * null
         */
        URN_IHE_PCC_XDSMS_2007, 
        /**
         * null
         */
        URN_IHE_PCC_EDR_2007, 
        /**
         * null
         */
        URN_IHE_PCC_EDES_2007, 
        /**
         * null
         */
        URN_IHE_PCC_APR_HANDP_2008, 
        /**
         * null
         */
        URN_IHE_PCC_APR_LAB_2008, 
        /**
         * null
         */
        URN_IHE_PCC_APR_EDU_2008, 
        /**
         * null
         */
        URN_IHE_PCC_CRC_2008, 
        /**
         * null
         */
        URN_IHE_PCC_CM_2008, 
        /**
         * null
         */
        URN_IHE_PCC_IC_2008, 
        /**
         * null
         */
        URN_IHE_PCC_TN_2007, 
        /**
         * null
         */
        URN_IHE_PCC_NN_2007, 
        /**
         * null
         */
        URN_IHE_PCC_CTN_2007, 
        /**
         * null
         */
        URN_IHE_PCC_EDPN_2007, 
        /**
         * null
         */
        URN_IHE_PCC_HP_2008, 
        /**
         * null
         */
        URN_IHE_PCC_LDHP_2009, 
        /**
         * null
         */
        URN_IHE_PCC_LDS_2009, 
        /**
         * null
         */
        URN_IHE_PCC_MDS_2009, 
        /**
         * null
         */
        URN_IHE_PCC_NDS_2010, 
        /**
         * null
         */
        URN_IHE_PCC_PPVS_2010, 
        /**
         * null
         */
        URN_IHE_PCC_TRS_2011, 
        /**
         * null
         */
        URN_IHE_PCC_ETS_2011, 
        /**
         * null
         */
        URN_IHE_PCC_ITS_2011, 
        /**
         * null
         */
        URN_IHE_PCC_RIPT_2017, 
        /**
         * null
         */
        URN_IHE_ITI_BPPC_2007, 
        /**
         * null
         */
        URN_IHE_ITI_BPPCSD_2007, 
        /**
         * null
         */
        URN_IHE_ITI_XDSSD_PDF_2008, 
        /**
         * null
         */
        URN_IHE_ITI_XDSSD_TEXT_2008, 
        /**
         * null
         */
        URN_IHE_ITI_XDW_2011_WORKFLOWDOC, 
        /**
         * null
         */
        URN_IHE_ITI_DSG_DETACHED_2014, 
        /**
         * null
         */
        URN_IHE_ITI_DSG_ENVELOPING_2014, 
        /**
         * null
         */
        URN_IHE_ITI_APPC_2016_CONSENT, 
        /**
         * Code to be used when the mimeType is sufficient to understanding the technical format. May be used when no more specific FormatCode is available and the mimeType is sufficient to identify the technical format
         */
        URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT, 
        /**
         * null
         */
        URN_IHE_LAB_XDLAB_2008, 
        /**
         * null
         */
        URN_IHE_RAD_TEXT, 
        /**
         * null
         */
        URN_IHE_RAD_PDF, 
        /**
         * null
         */
        URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013, 
        /**
         * null
         */
        URN_IHE_CARD_IMAGING_2011, 
        /**
         * null
         */
        URN_IHE_CARD_CRC_2012, 
        /**
         * null
         */
        URN_IHE_CARD_EPRCIE_2014, 
        /**
         * null
         */
        URN_IHE_DENT_TEXT, 
        /**
         * null
         */
        URN_IHE_DENT_PDF, 
        /**
         * null
         */
        URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_ALL_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_ALL_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_BREAST_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_COLON_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_PROSTATE_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_THYROID_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_LUNG_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_SKIN_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_KIDNEY_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_CERVIX_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_OVARY_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_STOMACH_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_LIVER_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_PANCREAS_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_TESTIS_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_PHARYNX_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010, 
        /**
         * null
         */
        URN_IHE_PAT_APSR_CANCER_LARYNX_2010, 
        /**
         * null
         */
        URN_IHE_PHARM_PRE_2010, 
        /**
         * null
         */
        URN_IHE_PHARM_PADV_2010, 
        /**
         * null
         */
        URN_IHE_PHARM_DIS_2010, 
        /**
         * null
         */
        URN_IHE_PHARM_PML_2013, 
        /**
         * null
         */
        URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1, 
        /**
         * null
         */
        URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1, 
        /**
         * null
         */
        URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1, 
        /**
         * null
         */
        URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IHEFormatcodeCs fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("urn:ihe:pcc:xphr:2007".equals(codeString))
          return URN_IHE_PCC_XPHR_2007;
        if ("urn:ihe:pcc:aps:2007".equals(codeString))
          return URN_IHE_PCC_APS_2007;
        if ("urn:ihe:pcc:xds-ms:2007".equals(codeString))
          return URN_IHE_PCC_XDSMS_2007;
        if ("urn:ihe:pcc:edr:2007".equals(codeString))
          return URN_IHE_PCC_EDR_2007;
        if ("urn:ihe:pcc:edes:2007".equals(codeString))
          return URN_IHE_PCC_EDES_2007;
        if ("urn:ihe:pcc:apr:handp:2008".equals(codeString))
          return URN_IHE_PCC_APR_HANDP_2008;
        if ("urn:ihe:pcc:apr:lab:2008".equals(codeString))
          return URN_IHE_PCC_APR_LAB_2008;
        if ("urn:ihe:pcc:apr:edu:2008".equals(codeString))
          return URN_IHE_PCC_APR_EDU_2008;
        if ("urn:ihe:pcc:crc:2008".equals(codeString))
          return URN_IHE_PCC_CRC_2008;
        if ("urn:ihe:pcc:cm:2008".equals(codeString))
          return URN_IHE_PCC_CM_2008;
        if ("urn:ihe:pcc:ic:2008".equals(codeString))
          return URN_IHE_PCC_IC_2008;
        if ("urn:ihe:pcc:tn:2007".equals(codeString))
          return URN_IHE_PCC_TN_2007;
        if ("urn:ihe:pcc:nn:2007".equals(codeString))
          return URN_IHE_PCC_NN_2007;
        if ("urn:ihe:pcc:ctn:2007".equals(codeString))
          return URN_IHE_PCC_CTN_2007;
        if ("urn:ihe:pcc:edpn:2007".equals(codeString))
          return URN_IHE_PCC_EDPN_2007;
        if ("urn:ihe:pcc:hp:2008".equals(codeString))
          return URN_IHE_PCC_HP_2008;
        if ("urn:ihe:pcc:ldhp:2009".equals(codeString))
          return URN_IHE_PCC_LDHP_2009;
        if ("urn:ihe:pcc:lds:2009".equals(codeString))
          return URN_IHE_PCC_LDS_2009;
        if ("urn:ihe:pcc:mds:2009".equals(codeString))
          return URN_IHE_PCC_MDS_2009;
        if ("urn:ihe:pcc:nds:2010".equals(codeString))
          return URN_IHE_PCC_NDS_2010;
        if ("urn:ihe:pcc:ppvs:2010".equals(codeString))
          return URN_IHE_PCC_PPVS_2010;
        if ("urn:ihe:pcc:trs:2011".equals(codeString))
          return URN_IHE_PCC_TRS_2011;
        if ("urn:ihe:pcc:ets:2011".equals(codeString))
          return URN_IHE_PCC_ETS_2011;
        if ("urn:ihe:pcc:its:2011".equals(codeString))
          return URN_IHE_PCC_ITS_2011;
        if ("urn:ihe:pcc:ript:2017".equals(codeString))
          return URN_IHE_PCC_RIPT_2017;
        if ("urn:ihe:iti:bppc:2007".equals(codeString))
          return URN_IHE_ITI_BPPC_2007;
        if ("urn:ihe:iti:bppc-sd:2007".equals(codeString))
          return URN_IHE_ITI_BPPCSD_2007;
        if ("urn:ihe:iti:xds-sd:pdf:2008".equals(codeString))
          return URN_IHE_ITI_XDSSD_PDF_2008;
        if ("urn:ihe:iti:xds-sd:text:2008".equals(codeString))
          return URN_IHE_ITI_XDSSD_TEXT_2008;
        if ("urn:ihe:iti:xdw:2011:workflowDoc".equals(codeString))
          return URN_IHE_ITI_XDW_2011_WORKFLOWDOC;
        if ("urn:ihe:iti:dsg:detached:2014".equals(codeString))
          return URN_IHE_ITI_DSG_DETACHED_2014;
        if ("urn:ihe:iti:dsg:enveloping:2014".equals(codeString))
          return URN_IHE_ITI_DSG_ENVELOPING_2014;
        if ("urn:ihe:iti:appc:2016:consent".equals(codeString))
          return URN_IHE_ITI_APPC_2016_CONSENT;
        if ("urn:ihe:iti:xds:2017:mimeTypeSufficient".equals(codeString))
          return URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT;
        if ("urn:ihe:lab:xd-lab:2008".equals(codeString))
          return URN_IHE_LAB_XDLAB_2008;
        if ("urn:ihe:rad:TEXT".equals(codeString))
          return URN_IHE_RAD_TEXT;
        if ("urn:ihe:rad:PDF".equals(codeString))
          return URN_IHE_RAD_PDF;
        if ("urn:ihe:rad:CDA:ImagingReportStructuredHeadings:2013".equals(codeString))
          return URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013;
        if ("urn:ihe:card:imaging:2011".equals(codeString))
          return URN_IHE_CARD_IMAGING_2011;
        if ("urn:ihe:card:CRC:2012".equals(codeString))
          return URN_IHE_CARD_CRC_2012;
        if ("urn:ihe:card:EPRC-IE:2014".equals(codeString))
          return URN_IHE_CARD_EPRCIE_2014;
        if ("urn:ihe:dent:TEXT".equals(codeString))
          return URN_IHE_DENT_TEXT;
        if ("urn:ihe:dent:PDF".equals(codeString))
          return URN_IHE_DENT_PDF;
        if ("urn:ihe:dent:CDA:ImagingReportStructuredHeadings:2013".equals(codeString))
          return URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013;
        if ("urn:ihe:pat:apsr:all:2010".equals(codeString))
          return URN_IHE_PAT_APSR_ALL_2010;
        if ("urn:ihe:pat:apsr:cancer:all:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_ALL_2010;
        if ("urn:ihe:pat:apsr:cancer:breast:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_BREAST_2010;
        if ("urn:ihe:pat:apsr:cancer:colon:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_COLON_2010;
        if ("urn:ihe:pat:apsr:cancer:prostate:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_PROSTATE_2010;
        if ("urn:ihe:pat:apsr:cancer:thyroid:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_THYROID_2010;
        if ("urn:ihe:pat:apsr:cancer:lung:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_LUNG_2010;
        if ("urn:ihe:pat:apsr:cancer:skin:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_SKIN_2010;
        if ("urn:ihe:pat:apsr:cancer:kidney:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_KIDNEY_2010;
        if ("urn:ihe:pat:apsr:cancer:cervix:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_CERVIX_2010;
        if ("urn:ihe:pat:apsr:cancer:endometrium:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010;
        if ("urn:ihe:pat:apsr:cancer:ovary:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_OVARY_2010;
        if ("urn:ihe:pat:apsr:cancer:esophagus:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010;
        if ("urn:ihe:pat:apsr:cancer:stomach:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_STOMACH_2010;
        if ("urn:ihe:pat:apsr:cancer:liver:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_LIVER_2010;
        if ("urn:ihe:pat:apsr:cancer:pancreas:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_PANCREAS_2010;
        if ("urn:ihe:pat:apsr:cancer:testis:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_TESTIS_2010;
        if ("urn:ihe:pat:apsr:cancer:urinary_bladder:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010;
        if ("urn:ihe:pat:apsr:cancer:lip_oral_cavity:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010;
        if ("urn:ihe:pat:apsr:cancer:pharynx:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_PHARYNX_2010;
        if ("urn:ihe:pat:apsr:cancer:salivary_gland:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010;
        if ("urn:ihe:pat:apsr:cancer:larynx:2010".equals(codeString))
          return URN_IHE_PAT_APSR_CANCER_LARYNX_2010;
        if ("urn:ihe:pharm:pre:2010".equals(codeString))
          return URN_IHE_PHARM_PRE_2010;
        if ("urn:ihe:pharm:padv:2010".equals(codeString))
          return URN_IHE_PHARM_PADV_2010;
        if ("urn:ihe:pharm:dis:2010".equals(codeString))
          return URN_IHE_PHARM_DIS_2010;
        if ("urn:ihe:pharm:pml:2013".equals(codeString))
          return URN_IHE_PHARM_PML_2013;
        if ("urn:hl7-org:sdwg:ccda-structuredBody:1.1".equals(codeString))
          return URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1;
        if ("urn:hl7-org:sdwg:ccda-nonXMLBody:1.1".equals(codeString))
          return URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1;
        if ("urn:hl7-org:sdwg:ccda-structuredBody:2.1".equals(codeString))
          return URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1;
        if ("urn:hl7-org:sdwg:ccda-nonXMLBody:2.1".equals(codeString))
          return URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1;
        throw new FHIRException("Unknown IHEFormatcodeCs code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case URN_IHE_PCC_XPHR_2007: return "urn:ihe:pcc:xphr:2007";
            case URN_IHE_PCC_APS_2007: return "urn:ihe:pcc:aps:2007";
            case URN_IHE_PCC_XDSMS_2007: return "urn:ihe:pcc:xds-ms:2007";
            case URN_IHE_PCC_EDR_2007: return "urn:ihe:pcc:edr:2007";
            case URN_IHE_PCC_EDES_2007: return "urn:ihe:pcc:edes:2007";
            case URN_IHE_PCC_APR_HANDP_2008: return "urn:ihe:pcc:apr:handp:2008";
            case URN_IHE_PCC_APR_LAB_2008: return "urn:ihe:pcc:apr:lab:2008";
            case URN_IHE_PCC_APR_EDU_2008: return "urn:ihe:pcc:apr:edu:2008";
            case URN_IHE_PCC_CRC_2008: return "urn:ihe:pcc:crc:2008";
            case URN_IHE_PCC_CM_2008: return "urn:ihe:pcc:cm:2008";
            case URN_IHE_PCC_IC_2008: return "urn:ihe:pcc:ic:2008";
            case URN_IHE_PCC_TN_2007: return "urn:ihe:pcc:tn:2007";
            case URN_IHE_PCC_NN_2007: return "urn:ihe:pcc:nn:2007";
            case URN_IHE_PCC_CTN_2007: return "urn:ihe:pcc:ctn:2007";
            case URN_IHE_PCC_EDPN_2007: return "urn:ihe:pcc:edpn:2007";
            case URN_IHE_PCC_HP_2008: return "urn:ihe:pcc:hp:2008";
            case URN_IHE_PCC_LDHP_2009: return "urn:ihe:pcc:ldhp:2009";
            case URN_IHE_PCC_LDS_2009: return "urn:ihe:pcc:lds:2009";
            case URN_IHE_PCC_MDS_2009: return "urn:ihe:pcc:mds:2009";
            case URN_IHE_PCC_NDS_2010: return "urn:ihe:pcc:nds:2010";
            case URN_IHE_PCC_PPVS_2010: return "urn:ihe:pcc:ppvs:2010";
            case URN_IHE_PCC_TRS_2011: return "urn:ihe:pcc:trs:2011";
            case URN_IHE_PCC_ETS_2011: return "urn:ihe:pcc:ets:2011";
            case URN_IHE_PCC_ITS_2011: return "urn:ihe:pcc:its:2011";
            case URN_IHE_PCC_RIPT_2017: return "urn:ihe:pcc:ript:2017";
            case URN_IHE_ITI_BPPC_2007: return "urn:ihe:iti:bppc:2007";
            case URN_IHE_ITI_BPPCSD_2007: return "urn:ihe:iti:bppc-sd:2007";
            case URN_IHE_ITI_XDSSD_PDF_2008: return "urn:ihe:iti:xds-sd:pdf:2008";
            case URN_IHE_ITI_XDSSD_TEXT_2008: return "urn:ihe:iti:xds-sd:text:2008";
            case URN_IHE_ITI_XDW_2011_WORKFLOWDOC: return "urn:ihe:iti:xdw:2011:workflowDoc";
            case URN_IHE_ITI_DSG_DETACHED_2014: return "urn:ihe:iti:dsg:detached:2014";
            case URN_IHE_ITI_DSG_ENVELOPING_2014: return "urn:ihe:iti:dsg:enveloping:2014";
            case URN_IHE_ITI_APPC_2016_CONSENT: return "urn:ihe:iti:appc:2016:consent";
            case URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT: return "urn:ihe:iti:xds:2017:mimeTypeSufficient";
            case URN_IHE_LAB_XDLAB_2008: return "urn:ihe:lab:xd-lab:2008";
            case URN_IHE_RAD_TEXT: return "urn:ihe:rad:TEXT";
            case URN_IHE_RAD_PDF: return "urn:ihe:rad:PDF";
            case URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013: return "urn:ihe:rad:CDA:ImagingReportStructuredHeadings:2013";
            case URN_IHE_CARD_IMAGING_2011: return "urn:ihe:card:imaging:2011";
            case URN_IHE_CARD_CRC_2012: return "urn:ihe:card:CRC:2012";
            case URN_IHE_CARD_EPRCIE_2014: return "urn:ihe:card:EPRC-IE:2014";
            case URN_IHE_DENT_TEXT: return "urn:ihe:dent:TEXT";
            case URN_IHE_DENT_PDF: return "urn:ihe:dent:PDF";
            case URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013: return "urn:ihe:dent:CDA:ImagingReportStructuredHeadings:2013";
            case URN_IHE_PAT_APSR_ALL_2010: return "urn:ihe:pat:apsr:all:2010";
            case URN_IHE_PAT_APSR_CANCER_ALL_2010: return "urn:ihe:pat:apsr:cancer:all:2010";
            case URN_IHE_PAT_APSR_CANCER_BREAST_2010: return "urn:ihe:pat:apsr:cancer:breast:2010";
            case URN_IHE_PAT_APSR_CANCER_COLON_2010: return "urn:ihe:pat:apsr:cancer:colon:2010";
            case URN_IHE_PAT_APSR_CANCER_PROSTATE_2010: return "urn:ihe:pat:apsr:cancer:prostate:2010";
            case URN_IHE_PAT_APSR_CANCER_THYROID_2010: return "urn:ihe:pat:apsr:cancer:thyroid:2010";
            case URN_IHE_PAT_APSR_CANCER_LUNG_2010: return "urn:ihe:pat:apsr:cancer:lung:2010";
            case URN_IHE_PAT_APSR_CANCER_SKIN_2010: return "urn:ihe:pat:apsr:cancer:skin:2010";
            case URN_IHE_PAT_APSR_CANCER_KIDNEY_2010: return "urn:ihe:pat:apsr:cancer:kidney:2010";
            case URN_IHE_PAT_APSR_CANCER_CERVIX_2010: return "urn:ihe:pat:apsr:cancer:cervix:2010";
            case URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010: return "urn:ihe:pat:apsr:cancer:endometrium:2010";
            case URN_IHE_PAT_APSR_CANCER_OVARY_2010: return "urn:ihe:pat:apsr:cancer:ovary:2010";
            case URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010: return "urn:ihe:pat:apsr:cancer:esophagus:2010";
            case URN_IHE_PAT_APSR_CANCER_STOMACH_2010: return "urn:ihe:pat:apsr:cancer:stomach:2010";
            case URN_IHE_PAT_APSR_CANCER_LIVER_2010: return "urn:ihe:pat:apsr:cancer:liver:2010";
            case URN_IHE_PAT_APSR_CANCER_PANCREAS_2010: return "urn:ihe:pat:apsr:cancer:pancreas:2010";
            case URN_IHE_PAT_APSR_CANCER_TESTIS_2010: return "urn:ihe:pat:apsr:cancer:testis:2010";
            case URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010: return "urn:ihe:pat:apsr:cancer:urinary_bladder:2010";
            case URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010: return "urn:ihe:pat:apsr:cancer:lip_oral_cavity:2010";
            case URN_IHE_PAT_APSR_CANCER_PHARYNX_2010: return "urn:ihe:pat:apsr:cancer:pharynx:2010";
            case URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010: return "urn:ihe:pat:apsr:cancer:salivary_gland:2010";
            case URN_IHE_PAT_APSR_CANCER_LARYNX_2010: return "urn:ihe:pat:apsr:cancer:larynx:2010";
            case URN_IHE_PHARM_PRE_2010: return "urn:ihe:pharm:pre:2010";
            case URN_IHE_PHARM_PADV_2010: return "urn:ihe:pharm:padv:2010";
            case URN_IHE_PHARM_DIS_2010: return "urn:ihe:pharm:dis:2010";
            case URN_IHE_PHARM_PML_2013: return "urn:ihe:pharm:pml:2013";
            case URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1: return "urn:hl7-org:sdwg:ccda-structuredBody:1.1";
            case URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1: return "urn:hl7-org:sdwg:ccda-nonXMLBody:1.1";
            case URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1: return "urn:hl7-org:sdwg:ccda-structuredBody:2.1";
            case URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1: return "urn:hl7-org:sdwg:ccda-nonXMLBody:2.1";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://ihe.net/fhir/ValueSet/IHE.FormatCode.codesystem";
        }
        public String getDefinition() {
          switch (this) {
            case URN_IHE_PCC_XPHR_2007: return "";
            case URN_IHE_PCC_APS_2007: return "";
            case URN_IHE_PCC_XDSMS_2007: return "";
            case URN_IHE_PCC_EDR_2007: return "";
            case URN_IHE_PCC_EDES_2007: return "";
            case URN_IHE_PCC_APR_HANDP_2008: return "";
            case URN_IHE_PCC_APR_LAB_2008: return "";
            case URN_IHE_PCC_APR_EDU_2008: return "";
            case URN_IHE_PCC_CRC_2008: return "";
            case URN_IHE_PCC_CM_2008: return "";
            case URN_IHE_PCC_IC_2008: return "";
            case URN_IHE_PCC_TN_2007: return "";
            case URN_IHE_PCC_NN_2007: return "";
            case URN_IHE_PCC_CTN_2007: return "";
            case URN_IHE_PCC_EDPN_2007: return "";
            case URN_IHE_PCC_HP_2008: return "";
            case URN_IHE_PCC_LDHP_2009: return "";
            case URN_IHE_PCC_LDS_2009: return "";
            case URN_IHE_PCC_MDS_2009: return "";
            case URN_IHE_PCC_NDS_2010: return "";
            case URN_IHE_PCC_PPVS_2010: return "";
            case URN_IHE_PCC_TRS_2011: return "";
            case URN_IHE_PCC_ETS_2011: return "";
            case URN_IHE_PCC_ITS_2011: return "";
            case URN_IHE_PCC_RIPT_2017: return "";
            case URN_IHE_ITI_BPPC_2007: return "";
            case URN_IHE_ITI_BPPCSD_2007: return "";
            case URN_IHE_ITI_XDSSD_PDF_2008: return "";
            case URN_IHE_ITI_XDSSD_TEXT_2008: return "";
            case URN_IHE_ITI_XDW_2011_WORKFLOWDOC: return "";
            case URN_IHE_ITI_DSG_DETACHED_2014: return "";
            case URN_IHE_ITI_DSG_ENVELOPING_2014: return "";
            case URN_IHE_ITI_APPC_2016_CONSENT: return "";
            case URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT: return "Code to be used when the mimeType is sufficient to understanding the technical format. May be used when no more specific FormatCode is available and the mimeType is sufficient to identify the technical format";
            case URN_IHE_LAB_XDLAB_2008: return "";
            case URN_IHE_RAD_TEXT: return "";
            case URN_IHE_RAD_PDF: return "";
            case URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013: return "";
            case URN_IHE_CARD_IMAGING_2011: return "";
            case URN_IHE_CARD_CRC_2012: return "";
            case URN_IHE_CARD_EPRCIE_2014: return "";
            case URN_IHE_DENT_TEXT: return "";
            case URN_IHE_DENT_PDF: return "";
            case URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013: return "";
            case URN_IHE_PAT_APSR_ALL_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_ALL_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_BREAST_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_COLON_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_PROSTATE_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_THYROID_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_LUNG_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_SKIN_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_KIDNEY_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_CERVIX_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_OVARY_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_STOMACH_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_LIVER_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_PANCREAS_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_TESTIS_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_PHARYNX_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010: return "";
            case URN_IHE_PAT_APSR_CANCER_LARYNX_2010: return "";
            case URN_IHE_PHARM_PRE_2010: return "";
            case URN_IHE_PHARM_PADV_2010: return "";
            case URN_IHE_PHARM_DIS_2010: return "";
            case URN_IHE_PHARM_PML_2013: return "";
            case URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1: return "";
            case URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1: return "";
            case URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1: return "";
            case URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case URN_IHE_PCC_XPHR_2007: return "Personal Health Records. Also known as HL7 CCD and HITSP C32";
            case URN_IHE_PCC_APS_2007: return "IHE Antepartum Summary";
            case URN_IHE_PCC_XDSMS_2007: return "XDS Medical Summaries";
            case URN_IHE_PCC_EDR_2007: return "Emergency Department Referral (EDR)";
            case URN_IHE_PCC_EDES_2007: return "Emergency Department Encounter Summary (EDES)";
            case URN_IHE_PCC_APR_HANDP_2008: return "Antepartum Record (APR) - History and Physical";
            case URN_IHE_PCC_APR_LAB_2008: return "Antepartum Record (APR) - Laboratory";
            case URN_IHE_PCC_APR_EDU_2008: return "Antepartum Record (APR) - Education";
            case URN_IHE_PCC_CRC_2008: return "Cancer Registry Content (CRC)";
            case URN_IHE_PCC_CM_2008: return "Care Management (CM)";
            case URN_IHE_PCC_IC_2008: return "Immunization Content (IC)";
            case URN_IHE_PCC_TN_2007: return "PCC TN";
            case URN_IHE_PCC_NN_2007: return "PCC NN";
            case URN_IHE_PCC_CTN_2007: return "PCC CTN";
            case URN_IHE_PCC_EDPN_2007: return "PCC EDPN";
            case URN_IHE_PCC_HP_2008: return "PCC HP";
            case URN_IHE_PCC_LDHP_2009: return "PCC LDHP";
            case URN_IHE_PCC_LDS_2009: return "PCC LDS";
            case URN_IHE_PCC_MDS_2009: return "PCC MDS";
            case URN_IHE_PCC_NDS_2010: return "PCC NDS";
            case URN_IHE_PCC_PPVS_2010: return "PCC PPVS";
            case URN_IHE_PCC_TRS_2011: return "PCC TRS";
            case URN_IHE_PCC_ETS_2011: return "PCC ETS";
            case URN_IHE_PCC_ITS_2011: return "PCC ITS";
            case URN_IHE_PCC_RIPT_2017: return "Routine Interfacility Patient Transport (RIPT)";
            case URN_IHE_ITI_BPPC_2007: return "Basic Patient Privacy Consents";
            case URN_IHE_ITI_BPPCSD_2007: return "Basic Patient Privacy Consents with Scanned Document";
            case URN_IHE_ITI_XDSSD_PDF_2008: return "PDF embedded in CDA per XDS-SD profile";
            case URN_IHE_ITI_XDSSD_TEXT_2008: return "Text embedded in CDA per XDS-SD profile";
            case URN_IHE_ITI_XDW_2011_WORKFLOWDOC: return "XDW Workflow Document";
            case URN_IHE_ITI_DSG_DETACHED_2014: return "DSG Detached Document";
            case URN_IHE_ITI_DSG_ENVELOPING_2014: return "DSG Enveloping Document";
            case URN_IHE_ITI_APPC_2016_CONSENT: return "Advanced Patient Privacy Consents";
            case URN_IHE_ITI_XDS_2017_MIMETYPESUFFICIENT: return "mimeType Sufficient";
            case URN_IHE_LAB_XDLAB_2008: return "CDA Laboratory Report";
            case URN_IHE_RAD_TEXT: return "Radiology XDS-I Text";
            case URN_IHE_RAD_PDF: return "Radiology XDS-I PDF";
            case URN_IHE_RAD_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013: return "Radiology XDS-I Structured CDA";
            case URN_IHE_CARD_IMAGING_2011: return "Cardiac Imaging Report";
            case URN_IHE_CARD_CRC_2012: return "Cardiology CRC";
            case URN_IHE_CARD_EPRCIE_2014: return "Cardiology EPRC-IE";
            case URN_IHE_DENT_TEXT: return "Dental Text";
            case URN_IHE_DENT_PDF: return "Dental PDF";
            case URN_IHE_DENT_CDA_IMAGINGREPORTSTRUCTUREDHEADINGS_2013: return "Dental CDA";
            case URN_IHE_PAT_APSR_ALL_2010: return "Anatomic Pathology Structured Report All";
            case URN_IHE_PAT_APSR_CANCER_ALL_2010: return "Anatomic Pathology Structured Report Cancer All";
            case URN_IHE_PAT_APSR_CANCER_BREAST_2010: return "Anatomic Pathology Structured Report Cancer Breast";
            case URN_IHE_PAT_APSR_CANCER_COLON_2010: return "Anatomic Pathology Structured Report Cancer Colon";
            case URN_IHE_PAT_APSR_CANCER_PROSTATE_2010: return "Anatomic Pathology Structured Report Cancer Prostate";
            case URN_IHE_PAT_APSR_CANCER_THYROID_2010: return "Anatomic Pathology Structured Report Cancer Thyroid";
            case URN_IHE_PAT_APSR_CANCER_LUNG_2010: return "Anatomic Pathology Structured Report Cancer Lung";
            case URN_IHE_PAT_APSR_CANCER_SKIN_2010: return "Anatomic Pathology Structured Report Cancer Skin";
            case URN_IHE_PAT_APSR_CANCER_KIDNEY_2010: return "Anatomic Pathology Structured Report Cancer Kidney";
            case URN_IHE_PAT_APSR_CANCER_CERVIX_2010: return "Anatomic Pathology Structured Report Cancer Cervix";
            case URN_IHE_PAT_APSR_CANCER_ENDOMETRIUM_2010: return "Anatomic Pathology Structured Report Cancer Endometrium";
            case URN_IHE_PAT_APSR_CANCER_OVARY_2010: return "Anatomic Pathology Structured Report Cancer Ovary";
            case URN_IHE_PAT_APSR_CANCER_ESOPHAGUS_2010: return "Anatomic Pathology Structured Report Cancer Esophagus";
            case URN_IHE_PAT_APSR_CANCER_STOMACH_2010: return "Anatomic Pathology Structured Report Cancer Stomach";
            case URN_IHE_PAT_APSR_CANCER_LIVER_2010: return "Anatomic Pathology Structured Report Cancer Liver";
            case URN_IHE_PAT_APSR_CANCER_PANCREAS_2010: return "Anatomic Pathology Structured Report Cancer Pancreas";
            case URN_IHE_PAT_APSR_CANCER_TESTIS_2010: return "Anatomic Pathology Structured Report Cancer Testis";
            case URN_IHE_PAT_APSR_CANCER_URINARYBLADDER_2010: return "Anatomic Pathology Structured Report Cancer Urinary Bladder";
            case URN_IHE_PAT_APSR_CANCER_LIPORALCAVITY_2010: return "Anatomic Pathology Structured Report Cancer Lip Oral Cavity";
            case URN_IHE_PAT_APSR_CANCER_PHARYNX_2010: return "Anatomic Pathology Structured Report Cancer Pharynx";
            case URN_IHE_PAT_APSR_CANCER_SALIVARYGLAND_2010: return "Anatomic Pathology Structured Report Cancer Salivary Gland";
            case URN_IHE_PAT_APSR_CANCER_LARYNX_2010: return "Anatomic Pathology Structured Report Cancer Larynx";
            case URN_IHE_PHARM_PRE_2010: return "Pharmacy Pre";
            case URN_IHE_PHARM_PADV_2010: return "Pharmacy PADV";
            case URN_IHE_PHARM_DIS_2010: return "Pharmacy DIS";
            case URN_IHE_PHARM_PML_2013: return "Pharmacy PML";
            case URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_1_1: return "For documents following C-CDA 1.1 constraints using a structured body.";
            case URN_HL7ORG_SDWG_CCDANONXMLBODY_1_1: return "For documents following C-CDA 1.1 constraints using a non structured body.";
            case URN_HL7ORG_SDWG_CCDASTRUCTUREDBODY_2_1: return "For documents following C-CDA 2.1 constraints using a structured body.";
            case URN_HL7ORG_SDWG_CCDANONXMLBODY_2_1: return "For documents following C-CDA 2.1 constraints using a non structured body.";
            default: return "?";
          }
    }


}

