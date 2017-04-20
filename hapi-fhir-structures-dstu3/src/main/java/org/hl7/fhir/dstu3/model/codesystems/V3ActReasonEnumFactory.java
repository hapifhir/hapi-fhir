package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActReasonEnumFactory implements EnumFactory<V3ActReason> {

  public V3ActReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActAccommodationReason".equals(codeString))
      return V3ActReason._ACTACCOMMODATIONREASON;
    if ("ACCREQNA".equals(codeString))
      return V3ActReason.ACCREQNA;
    if ("FLRCNV".equals(codeString))
      return V3ActReason.FLRCNV;
    if ("MEDNEC".equals(codeString))
      return V3ActReason.MEDNEC;
    if ("PAT".equals(codeString))
      return V3ActReason.PAT;
    if ("_ActCoverageReason".equals(codeString))
      return V3ActReason._ACTCOVERAGEREASON;
    if ("_EligibilityActReasonCode".equals(codeString))
      return V3ActReason._ELIGIBILITYACTREASONCODE;
    if ("_ActIneligibilityReason".equals(codeString))
      return V3ActReason._ACTINELIGIBILITYREASON;
    if ("COVSUS".equals(codeString))
      return V3ActReason.COVSUS;
    if ("DECSD".equals(codeString))
      return V3ActReason.DECSD;
    if ("REGERR".equals(codeString))
      return V3ActReason.REGERR;
    if ("_CoverageEligibilityReason".equals(codeString))
      return V3ActReason._COVERAGEELIGIBILITYREASON;
    if ("AGE".equals(codeString))
      return V3ActReason.AGE;
    if ("CRIME".equals(codeString))
      return V3ActReason.CRIME;
    if ("DIS".equals(codeString))
      return V3ActReason.DIS;
    if ("EMPLOY".equals(codeString))
      return V3ActReason.EMPLOY;
    if ("FINAN".equals(codeString))
      return V3ActReason.FINAN;
    if ("HEALTH".equals(codeString))
      return V3ActReason.HEALTH;
    if ("MULTI".equals(codeString))
      return V3ActReason.MULTI;
    if ("PNC".equals(codeString))
      return V3ActReason.PNC;
    if ("STATUTORY".equals(codeString))
      return V3ActReason.STATUTORY;
    if ("VEHIC".equals(codeString))
      return V3ActReason.VEHIC;
    if ("WORK".equals(codeString))
      return V3ActReason.WORK;
    if ("_ActInformationManagementReason".equals(codeString))
      return V3ActReason._ACTINFORMATIONMANAGEMENTREASON;
    if ("_ActHealthInformationManagementReason".equals(codeString))
      return V3ActReason._ACTHEALTHINFORMATIONMANAGEMENTREASON;
    if ("_ActConsentInformationAccessOverrideReason".equals(codeString))
      return V3ActReason._ACTCONSENTINFORMATIONACCESSOVERRIDEREASON;
    if ("OVRER".equals(codeString))
      return V3ActReason.OVRER;
    if ("OVRPJ".equals(codeString))
      return V3ActReason.OVRPJ;
    if ("OVRPS".equals(codeString))
      return V3ActReason.OVRPS;
    if ("OVRTPS".equals(codeString))
      return V3ActReason.OVRTPS;
    if ("PurposeOfUse".equals(codeString))
      return V3ActReason.PURPOSEOFUSE;
    if ("HMARKT".equals(codeString))
      return V3ActReason.HMARKT;
    if ("HOPERAT".equals(codeString))
      return V3ActReason.HOPERAT;
    if ("DONAT".equals(codeString))
      return V3ActReason.DONAT;
    if ("FRAUD".equals(codeString))
      return V3ActReason.FRAUD;
    if ("GOV".equals(codeString))
      return V3ActReason.GOV;
    if ("HACCRED".equals(codeString))
      return V3ActReason.HACCRED;
    if ("HCOMPL".equals(codeString))
      return V3ActReason.HCOMPL;
    if ("HDECD".equals(codeString))
      return V3ActReason.HDECD;
    if ("HDIRECT".equals(codeString))
      return V3ActReason.HDIRECT;
    if ("HLEGAL".equals(codeString))
      return V3ActReason.HLEGAL;
    if ("HOUTCOMS".equals(codeString))
      return V3ActReason.HOUTCOMS;
    if ("HPRGRP".equals(codeString))
      return V3ActReason.HPRGRP;
    if ("HQUALIMP".equals(codeString))
      return V3ActReason.HQUALIMP;
    if ("HSYSADMIN".equals(codeString))
      return V3ActReason.HSYSADMIN;
    if ("MEMADMIN".equals(codeString))
      return V3ActReason.MEMADMIN;
    if ("PATADMIN".equals(codeString))
      return V3ActReason.PATADMIN;
    if ("PATSFTY".equals(codeString))
      return V3ActReason.PATSFTY;
    if ("PERFMSR".equals(codeString))
      return V3ActReason.PERFMSR;
    if ("RECORDMGT".equals(codeString))
      return V3ActReason.RECORDMGT;
    if ("TRAIN".equals(codeString))
      return V3ActReason.TRAIN;
    if ("HPAYMT".equals(codeString))
      return V3ActReason.HPAYMT;
    if ("CLMATTCH".equals(codeString))
      return V3ActReason.CLMATTCH;
    if ("COVAUTH".equals(codeString))
      return V3ActReason.COVAUTH;
    if ("COVERAGE".equals(codeString))
      return V3ActReason.COVERAGE;
    if ("ELIGDTRM".equals(codeString))
      return V3ActReason.ELIGDTRM;
    if ("ELIGVER".equals(codeString))
      return V3ActReason.ELIGVER;
    if ("ENROLLM".equals(codeString))
      return V3ActReason.ENROLLM;
    if ("REMITADV".equals(codeString))
      return V3ActReason.REMITADV;
    if ("HRESCH".equals(codeString))
      return V3ActReason.HRESCH;
    if ("CLINTRCH".equals(codeString))
      return V3ActReason.CLINTRCH;
    if ("PATRQT".equals(codeString))
      return V3ActReason.PATRQT;
    if ("FAMRQT".equals(codeString))
      return V3ActReason.FAMRQT;
    if ("PWATRNY".equals(codeString))
      return V3ActReason.PWATRNY;
    if ("SUPNWK".equals(codeString))
      return V3ActReason.SUPNWK;
    if ("PUBHLTH".equals(codeString))
      return V3ActReason.PUBHLTH;
    if ("DISASTER".equals(codeString))
      return V3ActReason.DISASTER;
    if ("THREAT".equals(codeString))
      return V3ActReason.THREAT;
    if ("TREAT".equals(codeString))
      return V3ActReason.TREAT;
    if ("CAREMGT".equals(codeString))
      return V3ActReason.CAREMGT;
    if ("CLINTRL".equals(codeString))
      return V3ActReason.CLINTRL;
    if ("ETREAT".equals(codeString))
      return V3ActReason.ETREAT;
    if ("POPHLTH".equals(codeString))
      return V3ActReason.POPHLTH;
    if ("_ActInformationPrivacyReason".equals(codeString))
      return V3ActReason._ACTINFORMATIONPRIVACYREASON;
    if ("MARKT".equals(codeString))
      return V3ActReason.MARKT;
    if ("OPERAT".equals(codeString))
      return V3ActReason.OPERAT;
    if ("LEGAL".equals(codeString))
      return V3ActReason.LEGAL;
    if ("ACCRED".equals(codeString))
      return V3ActReason.ACCRED;
    if ("COMPL".equals(codeString))
      return V3ActReason.COMPL;
    if ("ENADMIN".equals(codeString))
      return V3ActReason.ENADMIN;
    if ("OUTCOMS".equals(codeString))
      return V3ActReason.OUTCOMS;
    if ("PRGRPT".equals(codeString))
      return V3ActReason.PRGRPT;
    if ("QUALIMP".equals(codeString))
      return V3ActReason.QUALIMP;
    if ("SYSADMN".equals(codeString))
      return V3ActReason.SYSADMN;
    if ("PAYMT".equals(codeString))
      return V3ActReason.PAYMT;
    if ("RESCH".equals(codeString))
      return V3ActReason.RESCH;
    if ("SRVC".equals(codeString))
      return V3ActReason.SRVC;
    if ("_ActInvalidReason".equals(codeString))
      return V3ActReason._ACTINVALIDREASON;
    if ("ADVSTORAGE".equals(codeString))
      return V3ActReason.ADVSTORAGE;
    if ("COLDCHNBRK".equals(codeString))
      return V3ActReason.COLDCHNBRK;
    if ("EXPLOT".equals(codeString))
      return V3ActReason.EXPLOT;
    if ("OUTSIDESCHED".equals(codeString))
      return V3ActReason.OUTSIDESCHED;
    if ("PRODRECALL".equals(codeString))
      return V3ActReason.PRODRECALL;
    if ("_ActInvoiceCancelReason".equals(codeString))
      return V3ActReason._ACTINVOICECANCELREASON;
    if ("INCCOVPTY".equals(codeString))
      return V3ActReason.INCCOVPTY;
    if ("INCINVOICE".equals(codeString))
      return V3ActReason.INCINVOICE;
    if ("INCPOLICY".equals(codeString))
      return V3ActReason.INCPOLICY;
    if ("INCPROV".equals(codeString))
      return V3ActReason.INCPROV;
    if ("_ActNoImmunizationReason".equals(codeString))
      return V3ActReason._ACTNOIMMUNIZATIONREASON;
    if ("IMMUNE".equals(codeString))
      return V3ActReason.IMMUNE;
    if ("MEDPREC".equals(codeString))
      return V3ActReason.MEDPREC;
    if ("OSTOCK".equals(codeString))
      return V3ActReason.OSTOCK;
    if ("PATOBJ".equals(codeString))
      return V3ActReason.PATOBJ;
    if ("PHILISOP".equals(codeString))
      return V3ActReason.PHILISOP;
    if ("RELIG".equals(codeString))
      return V3ActReason.RELIG;
    if ("VACEFF".equals(codeString))
      return V3ActReason.VACEFF;
    if ("VACSAF".equals(codeString))
      return V3ActReason.VACSAF;
    if ("_ActSupplyFulfillmentRefusalReason".equals(codeString))
      return V3ActReason._ACTSUPPLYFULFILLMENTREFUSALREASON;
    if ("FRR01".equals(codeString))
      return V3ActReason.FRR01;
    if ("FRR02".equals(codeString))
      return V3ActReason.FRR02;
    if ("FRR03".equals(codeString))
      return V3ActReason.FRR03;
    if ("FRR04".equals(codeString))
      return V3ActReason.FRR04;
    if ("FRR05".equals(codeString))
      return V3ActReason.FRR05;
    if ("FRR06".equals(codeString))
      return V3ActReason.FRR06;
    if ("_ClinicalResearchEventReason".equals(codeString))
      return V3ActReason._CLINICALRESEARCHEVENTREASON;
    if ("RET".equals(codeString))
      return V3ActReason.RET;
    if ("SCH".equals(codeString))
      return V3ActReason.SCH;
    if ("TRM".equals(codeString))
      return V3ActReason.TRM;
    if ("UNS".equals(codeString))
      return V3ActReason.UNS;
    if ("_ClinicalResearchObservationReason".equals(codeString))
      return V3ActReason._CLINICALRESEARCHOBSERVATIONREASON;
    if ("NPT".equals(codeString))
      return V3ActReason.NPT;
    if ("PPT".equals(codeString))
      return V3ActReason.PPT;
    if ("UPT".equals(codeString))
      return V3ActReason.UPT;
    if ("_CombinedPharmacyOrderSuspendReasonCode".equals(codeString))
      return V3ActReason._COMBINEDPHARMACYORDERSUSPENDREASONCODE;
    if ("ALTCHOICE".equals(codeString))
      return V3ActReason.ALTCHOICE;
    if ("CLARIF".equals(codeString))
      return V3ActReason.CLARIF;
    if ("DRUGHIGH".equals(codeString))
      return V3ActReason.DRUGHIGH;
    if ("HOSPADM".equals(codeString))
      return V3ActReason.HOSPADM;
    if ("LABINT".equals(codeString))
      return V3ActReason.LABINT;
    if ("NON-AVAIL".equals(codeString))
      return V3ActReason.NONAVAIL;
    if ("PREG".equals(codeString))
      return V3ActReason.PREG;
    if ("SALG".equals(codeString))
      return V3ActReason.SALG;
    if ("SDDI".equals(codeString))
      return V3ActReason.SDDI;
    if ("SDUPTHER".equals(codeString))
      return V3ActReason.SDUPTHER;
    if ("SINTOL".equals(codeString))
      return V3ActReason.SINTOL;
    if ("SURG".equals(codeString))
      return V3ActReason.SURG;
    if ("WASHOUT".equals(codeString))
      return V3ActReason.WASHOUT;
    if ("_ControlActNullificationReasonCode".equals(codeString))
      return V3ActReason._CONTROLACTNULLIFICATIONREASONCODE;
    if ("ALTD".equals(codeString))
      return V3ActReason.ALTD;
    if ("EIE".equals(codeString))
      return V3ActReason.EIE;
    if ("NORECMTCH".equals(codeString))
      return V3ActReason.NORECMTCH;
    if ("_ControlActNullificationRefusalReasonType".equals(codeString))
      return V3ActReason._CONTROLACTNULLIFICATIONREFUSALREASONTYPE;
    if ("INRQSTATE".equals(codeString))
      return V3ActReason.INRQSTATE;
    if ("NOMATCH".equals(codeString))
      return V3ActReason.NOMATCH;
    if ("NOPRODMTCH".equals(codeString))
      return V3ActReason.NOPRODMTCH;
    if ("NOSERMTCH".equals(codeString))
      return V3ActReason.NOSERMTCH;
    if ("NOVERMTCH".equals(codeString))
      return V3ActReason.NOVERMTCH;
    if ("NOPERM".equals(codeString))
      return V3ActReason.NOPERM;
    if ("NOUSERPERM".equals(codeString))
      return V3ActReason.NOUSERPERM;
    if ("NOAGNTPERM".equals(codeString))
      return V3ActReason.NOAGNTPERM;
    if ("NOUSRPERM".equals(codeString))
      return V3ActReason.NOUSRPERM;
    if ("WRNGVER".equals(codeString))
      return V3ActReason.WRNGVER;
    if ("_ControlActReason".equals(codeString))
      return V3ActReason._CONTROLACTREASON;
    if ("_MedicationOrderAbortReasonCode".equals(codeString))
      return V3ActReason._MEDICATIONORDERABORTREASONCODE;
    if ("DISCONT".equals(codeString))
      return V3ActReason.DISCONT;
    if ("INEFFECT".equals(codeString))
      return V3ActReason.INEFFECT;
    if ("MONIT".equals(codeString))
      return V3ActReason.MONIT;
    if ("NOREQ".equals(codeString))
      return V3ActReason.NOREQ;
    if ("NOTCOVER".equals(codeString))
      return V3ActReason.NOTCOVER;
    if ("PREFUS".equals(codeString))
      return V3ActReason.PREFUS;
    if ("RECALL".equals(codeString))
      return V3ActReason.RECALL;
    if ("REPLACE".equals(codeString))
      return V3ActReason.REPLACE;
    if ("DOSECHG".equals(codeString))
      return V3ActReason.DOSECHG;
    if ("REPLACEFIX".equals(codeString))
      return V3ActReason.REPLACEFIX;
    if ("UNABLE".equals(codeString))
      return V3ActReason.UNABLE;
    if ("_MedicationOrderReleaseReasonCode".equals(codeString))
      return V3ActReason._MEDICATIONORDERRELEASEREASONCODE;
    if ("HOLDDONE".equals(codeString))
      return V3ActReason.HOLDDONE;
    if ("HOLDINAP".equals(codeString))
      return V3ActReason.HOLDINAP;
    if ("_ModifyPrescriptionReasonType".equals(codeString))
      return V3ActReason._MODIFYPRESCRIPTIONREASONTYPE;
    if ("ADMINERROR".equals(codeString))
      return V3ActReason.ADMINERROR;
    if ("CLINMOD".equals(codeString))
      return V3ActReason.CLINMOD;
    if ("_PharmacySupplyEventAbortReason".equals(codeString))
      return V3ActReason._PHARMACYSUPPLYEVENTABORTREASON;
    if ("CONTRA".equals(codeString))
      return V3ActReason.CONTRA;
    if ("FOABORT".equals(codeString))
      return V3ActReason.FOABORT;
    if ("FOSUSP".equals(codeString))
      return V3ActReason.FOSUSP;
    if ("NOPICK".equals(codeString))
      return V3ActReason.NOPICK;
    if ("PATDEC".equals(codeString))
      return V3ActReason.PATDEC;
    if ("QUANTCHG".equals(codeString))
      return V3ActReason.QUANTCHG;
    if ("_PharmacySupplyEventStockReasonCode".equals(codeString))
      return V3ActReason._PHARMACYSUPPLYEVENTSTOCKREASONCODE;
    if ("FLRSTCK".equals(codeString))
      return V3ActReason.FLRSTCK;
    if ("LTC".equals(codeString))
      return V3ActReason.LTC;
    if ("OFFICE".equals(codeString))
      return V3ActReason.OFFICE;
    if ("PHARM".equals(codeString))
      return V3ActReason.PHARM;
    if ("PROG".equals(codeString))
      return V3ActReason.PROG;
    if ("_PharmacySupplyRequestRenewalRefusalReasonCode".equals(codeString))
      return V3ActReason._PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE;
    if ("ALREADYRX".equals(codeString))
      return V3ActReason.ALREADYRX;
    if ("FAMPHYS".equals(codeString))
      return V3ActReason.FAMPHYS;
    if ("MODIFY".equals(codeString))
      return V3ActReason.MODIFY;
    if ("NEEDAPMT".equals(codeString))
      return V3ActReason.NEEDAPMT;
    if ("NOTAVAIL".equals(codeString))
      return V3ActReason.NOTAVAIL;
    if ("NOTPAT".equals(codeString))
      return V3ActReason.NOTPAT;
    if ("ONHOLD".equals(codeString))
      return V3ActReason.ONHOLD;
    if ("PRNA".equals(codeString))
      return V3ActReason.PRNA;
    if ("STOPMED".equals(codeString))
      return V3ActReason.STOPMED;
    if ("TOOEARLY".equals(codeString))
      return V3ActReason.TOOEARLY;
    if ("_SupplyOrderAbortReasonCode".equals(codeString))
      return V3ActReason._SUPPLYORDERABORTREASONCODE;
    if ("IMPROV".equals(codeString))
      return V3ActReason.IMPROV;
    if ("INTOL".equals(codeString))
      return V3ActReason.INTOL;
    if ("NEWSTR".equals(codeString))
      return V3ActReason.NEWSTR;
    if ("NEWTHER".equals(codeString))
      return V3ActReason.NEWTHER;
    if ("_GenericUpdateReasonCode".equals(codeString))
      return V3ActReason._GENERICUPDATEREASONCODE;
    if ("CHGDATA".equals(codeString))
      return V3ActReason.CHGDATA;
    if ("FIXDATA".equals(codeString))
      return V3ActReason.FIXDATA;
    if ("MDATA".equals(codeString))
      return V3ActReason.MDATA;
    if ("NEWDATA".equals(codeString))
      return V3ActReason.NEWDATA;
    if ("UMDATA".equals(codeString))
      return V3ActReason.UMDATA;
    if ("_PatientProfileQueryReasonCode".equals(codeString))
      return V3ActReason._PATIENTPROFILEQUERYREASONCODE;
    if ("ADMREV".equals(codeString))
      return V3ActReason.ADMREV;
    if ("PATCAR".equals(codeString))
      return V3ActReason.PATCAR;
    if ("PATREQ".equals(codeString))
      return V3ActReason.PATREQ;
    if ("PRCREV".equals(codeString))
      return V3ActReason.PRCREV;
    if ("REGUL".equals(codeString))
      return V3ActReason.REGUL;
    if ("RSRCH".equals(codeString))
      return V3ActReason.RSRCH;
    if ("VALIDATION".equals(codeString))
      return V3ActReason.VALIDATION;
    if ("_PharmacySupplyRequestFulfillerRevisionRefusalReasonCode".equals(codeString))
      return V3ActReason._PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE;
    if ("LOCKED".equals(codeString))
      return V3ActReason.LOCKED;
    if ("UNKWNTARGET".equals(codeString))
      return V3ActReason.UNKWNTARGET;
    if ("_RefusalReasonCode".equals(codeString))
      return V3ActReason._REFUSALREASONCODE;
    if ("_SchedulingActReason".equals(codeString))
      return V3ActReason._SCHEDULINGACTREASON;
    if ("BLK".equals(codeString))
      return V3ActReason.BLK;
    if ("DEC".equals(codeString))
      return V3ActReason.DEC;
    if ("FIN".equals(codeString))
      return V3ActReason.FIN;
    if ("MED".equals(codeString))
      return V3ActReason.MED;
    if ("MTG".equals(codeString))
      return V3ActReason.MTG;
    if ("PHY".equals(codeString))
      return V3ActReason.PHY;
    if ("_StatusRevisionRefusalReasonCode".equals(codeString))
      return V3ActReason._STATUSREVISIONREFUSALREASONCODE;
    if ("FILLED".equals(codeString))
      return V3ActReason.FILLED;
    if ("_SubstanceAdministrationPermissionRefusalReasonCode".equals(codeString))
      return V3ActReason._SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE;
    if ("PATINELIG".equals(codeString))
      return V3ActReason.PATINELIG;
    if ("PROTUNMET".equals(codeString))
      return V3ActReason.PROTUNMET;
    if ("PROVUNAUTH".equals(codeString))
      return V3ActReason.PROVUNAUTH;
    if ("_SubstanceAdminSubstitutionNotAllowedReason".equals(codeString))
      return V3ActReason._SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON;
    if ("ALGINT".equals(codeString))
      return V3ActReason.ALGINT;
    if ("COMPCON".equals(codeString))
      return V3ActReason.COMPCON;
    if ("THERCHAR".equals(codeString))
      return V3ActReason.THERCHAR;
    if ("TRIAL".equals(codeString))
      return V3ActReason.TRIAL;
    if ("_SubstanceAdminSubstitutionReason".equals(codeString))
      return V3ActReason._SUBSTANCEADMINSUBSTITUTIONREASON;
    if ("CT".equals(codeString))
      return V3ActReason.CT;
    if ("FP".equals(codeString))
      return V3ActReason.FP;
    if ("OS".equals(codeString))
      return V3ActReason.OS;
    if ("RR".equals(codeString))
      return V3ActReason.RR;
    if ("_TransferActReason".equals(codeString))
      return V3ActReason._TRANSFERACTREASON;
    if ("ER".equals(codeString))
      return V3ActReason.ER;
    if ("RQ".equals(codeString))
      return V3ActReason.RQ;
    if ("_ActBillableServiceReason".equals(codeString))
      return V3ActReason._ACTBILLABLESERVICEREASON;
    if ("_ActBillableClinicalServiceReason".equals(codeString))
      return V3ActReason._ACTBILLABLECLINICALSERVICEREASON;
    if ("BONUS".equals(codeString))
      return V3ActReason.BONUS;
    if ("CHD".equals(codeString))
      return V3ActReason.CHD;
    if ("DEP".equals(codeString))
      return V3ActReason.DEP;
    if ("ECH".equals(codeString))
      return V3ActReason.ECH;
    if ("EDU".equals(codeString))
      return V3ActReason.EDU;
    if ("EMP".equals(codeString))
      return V3ActReason.EMP;
    if ("ESP".equals(codeString))
      return V3ActReason.ESP;
    if ("FAM".equals(codeString))
      return V3ActReason.FAM;
    if ("IND".equals(codeString))
      return V3ActReason.IND;
    if ("INVOICE".equals(codeString))
      return V3ActReason.INVOICE;
    if ("PROA".equals(codeString))
      return V3ActReason.PROA;
    if ("RECOV".equals(codeString))
      return V3ActReason.RECOV;
    if ("RETRO".equals(codeString))
      return V3ActReason.RETRO;
    if ("SPC".equals(codeString))
      return V3ActReason.SPC;
    if ("SPO".equals(codeString))
      return V3ActReason.SPO;
    if ("TRAN".equals(codeString))
      return V3ActReason.TRAN;
    throw new IllegalArgumentException("Unknown V3ActReason code '"+codeString+"'");
  }

  public String toCode(V3ActReason code) {
    if (code == V3ActReason._ACTACCOMMODATIONREASON)
      return "_ActAccommodationReason";
    if (code == V3ActReason.ACCREQNA)
      return "ACCREQNA";
    if (code == V3ActReason.FLRCNV)
      return "FLRCNV";
    if (code == V3ActReason.MEDNEC)
      return "MEDNEC";
    if (code == V3ActReason.PAT)
      return "PAT";
    if (code == V3ActReason._ACTCOVERAGEREASON)
      return "_ActCoverageReason";
    if (code == V3ActReason._ELIGIBILITYACTREASONCODE)
      return "_EligibilityActReasonCode";
    if (code == V3ActReason._ACTINELIGIBILITYREASON)
      return "_ActIneligibilityReason";
    if (code == V3ActReason.COVSUS)
      return "COVSUS";
    if (code == V3ActReason.DECSD)
      return "DECSD";
    if (code == V3ActReason.REGERR)
      return "REGERR";
    if (code == V3ActReason._COVERAGEELIGIBILITYREASON)
      return "_CoverageEligibilityReason";
    if (code == V3ActReason.AGE)
      return "AGE";
    if (code == V3ActReason.CRIME)
      return "CRIME";
    if (code == V3ActReason.DIS)
      return "DIS";
    if (code == V3ActReason.EMPLOY)
      return "EMPLOY";
    if (code == V3ActReason.FINAN)
      return "FINAN";
    if (code == V3ActReason.HEALTH)
      return "HEALTH";
    if (code == V3ActReason.MULTI)
      return "MULTI";
    if (code == V3ActReason.PNC)
      return "PNC";
    if (code == V3ActReason.STATUTORY)
      return "STATUTORY";
    if (code == V3ActReason.VEHIC)
      return "VEHIC";
    if (code == V3ActReason.WORK)
      return "WORK";
    if (code == V3ActReason._ACTINFORMATIONMANAGEMENTREASON)
      return "_ActInformationManagementReason";
    if (code == V3ActReason._ACTHEALTHINFORMATIONMANAGEMENTREASON)
      return "_ActHealthInformationManagementReason";
    if (code == V3ActReason._ACTCONSENTINFORMATIONACCESSOVERRIDEREASON)
      return "_ActConsentInformationAccessOverrideReason";
    if (code == V3ActReason.OVRER)
      return "OVRER";
    if (code == V3ActReason.OVRPJ)
      return "OVRPJ";
    if (code == V3ActReason.OVRPS)
      return "OVRPS";
    if (code == V3ActReason.OVRTPS)
      return "OVRTPS";
    if (code == V3ActReason.PURPOSEOFUSE)
      return "PurposeOfUse";
    if (code == V3ActReason.HMARKT)
      return "HMARKT";
    if (code == V3ActReason.HOPERAT)
      return "HOPERAT";
    if (code == V3ActReason.DONAT)
      return "DONAT";
    if (code == V3ActReason.FRAUD)
      return "FRAUD";
    if (code == V3ActReason.GOV)
      return "GOV";
    if (code == V3ActReason.HACCRED)
      return "HACCRED";
    if (code == V3ActReason.HCOMPL)
      return "HCOMPL";
    if (code == V3ActReason.HDECD)
      return "HDECD";
    if (code == V3ActReason.HDIRECT)
      return "HDIRECT";
    if (code == V3ActReason.HLEGAL)
      return "HLEGAL";
    if (code == V3ActReason.HOUTCOMS)
      return "HOUTCOMS";
    if (code == V3ActReason.HPRGRP)
      return "HPRGRP";
    if (code == V3ActReason.HQUALIMP)
      return "HQUALIMP";
    if (code == V3ActReason.HSYSADMIN)
      return "HSYSADMIN";
    if (code == V3ActReason.MEMADMIN)
      return "MEMADMIN";
    if (code == V3ActReason.PATADMIN)
      return "PATADMIN";
    if (code == V3ActReason.PATSFTY)
      return "PATSFTY";
    if (code == V3ActReason.PERFMSR)
      return "PERFMSR";
    if (code == V3ActReason.RECORDMGT)
      return "RECORDMGT";
    if (code == V3ActReason.TRAIN)
      return "TRAIN";
    if (code == V3ActReason.HPAYMT)
      return "HPAYMT";
    if (code == V3ActReason.CLMATTCH)
      return "CLMATTCH";
    if (code == V3ActReason.COVAUTH)
      return "COVAUTH";
    if (code == V3ActReason.COVERAGE)
      return "COVERAGE";
    if (code == V3ActReason.ELIGDTRM)
      return "ELIGDTRM";
    if (code == V3ActReason.ELIGVER)
      return "ELIGVER";
    if (code == V3ActReason.ENROLLM)
      return "ENROLLM";
    if (code == V3ActReason.REMITADV)
      return "REMITADV";
    if (code == V3ActReason.HRESCH)
      return "HRESCH";
    if (code == V3ActReason.CLINTRCH)
      return "CLINTRCH";
    if (code == V3ActReason.PATRQT)
      return "PATRQT";
    if (code == V3ActReason.FAMRQT)
      return "FAMRQT";
    if (code == V3ActReason.PWATRNY)
      return "PWATRNY";
    if (code == V3ActReason.SUPNWK)
      return "SUPNWK";
    if (code == V3ActReason.PUBHLTH)
      return "PUBHLTH";
    if (code == V3ActReason.DISASTER)
      return "DISASTER";
    if (code == V3ActReason.THREAT)
      return "THREAT";
    if (code == V3ActReason.TREAT)
      return "TREAT";
    if (code == V3ActReason.CAREMGT)
      return "CAREMGT";
    if (code == V3ActReason.CLINTRL)
      return "CLINTRL";
    if (code == V3ActReason.ETREAT)
      return "ETREAT";
    if (code == V3ActReason.POPHLTH)
      return "POPHLTH";
    if (code == V3ActReason._ACTINFORMATIONPRIVACYREASON)
      return "_ActInformationPrivacyReason";
    if (code == V3ActReason.MARKT)
      return "MARKT";
    if (code == V3ActReason.OPERAT)
      return "OPERAT";
    if (code == V3ActReason.LEGAL)
      return "LEGAL";
    if (code == V3ActReason.ACCRED)
      return "ACCRED";
    if (code == V3ActReason.COMPL)
      return "COMPL";
    if (code == V3ActReason.ENADMIN)
      return "ENADMIN";
    if (code == V3ActReason.OUTCOMS)
      return "OUTCOMS";
    if (code == V3ActReason.PRGRPT)
      return "PRGRPT";
    if (code == V3ActReason.QUALIMP)
      return "QUALIMP";
    if (code == V3ActReason.SYSADMN)
      return "SYSADMN";
    if (code == V3ActReason.PAYMT)
      return "PAYMT";
    if (code == V3ActReason.RESCH)
      return "RESCH";
    if (code == V3ActReason.SRVC)
      return "SRVC";
    if (code == V3ActReason._ACTINVALIDREASON)
      return "_ActInvalidReason";
    if (code == V3ActReason.ADVSTORAGE)
      return "ADVSTORAGE";
    if (code == V3ActReason.COLDCHNBRK)
      return "COLDCHNBRK";
    if (code == V3ActReason.EXPLOT)
      return "EXPLOT";
    if (code == V3ActReason.OUTSIDESCHED)
      return "OUTSIDESCHED";
    if (code == V3ActReason.PRODRECALL)
      return "PRODRECALL";
    if (code == V3ActReason._ACTINVOICECANCELREASON)
      return "_ActInvoiceCancelReason";
    if (code == V3ActReason.INCCOVPTY)
      return "INCCOVPTY";
    if (code == V3ActReason.INCINVOICE)
      return "INCINVOICE";
    if (code == V3ActReason.INCPOLICY)
      return "INCPOLICY";
    if (code == V3ActReason.INCPROV)
      return "INCPROV";
    if (code == V3ActReason._ACTNOIMMUNIZATIONREASON)
      return "_ActNoImmunizationReason";
    if (code == V3ActReason.IMMUNE)
      return "IMMUNE";
    if (code == V3ActReason.MEDPREC)
      return "MEDPREC";
    if (code == V3ActReason.OSTOCK)
      return "OSTOCK";
    if (code == V3ActReason.PATOBJ)
      return "PATOBJ";
    if (code == V3ActReason.PHILISOP)
      return "PHILISOP";
    if (code == V3ActReason.RELIG)
      return "RELIG";
    if (code == V3ActReason.VACEFF)
      return "VACEFF";
    if (code == V3ActReason.VACSAF)
      return "VACSAF";
    if (code == V3ActReason._ACTSUPPLYFULFILLMENTREFUSALREASON)
      return "_ActSupplyFulfillmentRefusalReason";
    if (code == V3ActReason.FRR01)
      return "FRR01";
    if (code == V3ActReason.FRR02)
      return "FRR02";
    if (code == V3ActReason.FRR03)
      return "FRR03";
    if (code == V3ActReason.FRR04)
      return "FRR04";
    if (code == V3ActReason.FRR05)
      return "FRR05";
    if (code == V3ActReason.FRR06)
      return "FRR06";
    if (code == V3ActReason._CLINICALRESEARCHEVENTREASON)
      return "_ClinicalResearchEventReason";
    if (code == V3ActReason.RET)
      return "RET";
    if (code == V3ActReason.SCH)
      return "SCH";
    if (code == V3ActReason.TRM)
      return "TRM";
    if (code == V3ActReason.UNS)
      return "UNS";
    if (code == V3ActReason._CLINICALRESEARCHOBSERVATIONREASON)
      return "_ClinicalResearchObservationReason";
    if (code == V3ActReason.NPT)
      return "NPT";
    if (code == V3ActReason.PPT)
      return "PPT";
    if (code == V3ActReason.UPT)
      return "UPT";
    if (code == V3ActReason._COMBINEDPHARMACYORDERSUSPENDREASONCODE)
      return "_CombinedPharmacyOrderSuspendReasonCode";
    if (code == V3ActReason.ALTCHOICE)
      return "ALTCHOICE";
    if (code == V3ActReason.CLARIF)
      return "CLARIF";
    if (code == V3ActReason.DRUGHIGH)
      return "DRUGHIGH";
    if (code == V3ActReason.HOSPADM)
      return "HOSPADM";
    if (code == V3ActReason.LABINT)
      return "LABINT";
    if (code == V3ActReason.NONAVAIL)
      return "NON-AVAIL";
    if (code == V3ActReason.PREG)
      return "PREG";
    if (code == V3ActReason.SALG)
      return "SALG";
    if (code == V3ActReason.SDDI)
      return "SDDI";
    if (code == V3ActReason.SDUPTHER)
      return "SDUPTHER";
    if (code == V3ActReason.SINTOL)
      return "SINTOL";
    if (code == V3ActReason.SURG)
      return "SURG";
    if (code == V3ActReason.WASHOUT)
      return "WASHOUT";
    if (code == V3ActReason._CONTROLACTNULLIFICATIONREASONCODE)
      return "_ControlActNullificationReasonCode";
    if (code == V3ActReason.ALTD)
      return "ALTD";
    if (code == V3ActReason.EIE)
      return "EIE";
    if (code == V3ActReason.NORECMTCH)
      return "NORECMTCH";
    if (code == V3ActReason._CONTROLACTNULLIFICATIONREFUSALREASONTYPE)
      return "_ControlActNullificationRefusalReasonType";
    if (code == V3ActReason.INRQSTATE)
      return "INRQSTATE";
    if (code == V3ActReason.NOMATCH)
      return "NOMATCH";
    if (code == V3ActReason.NOPRODMTCH)
      return "NOPRODMTCH";
    if (code == V3ActReason.NOSERMTCH)
      return "NOSERMTCH";
    if (code == V3ActReason.NOVERMTCH)
      return "NOVERMTCH";
    if (code == V3ActReason.NOPERM)
      return "NOPERM";
    if (code == V3ActReason.NOUSERPERM)
      return "NOUSERPERM";
    if (code == V3ActReason.NOAGNTPERM)
      return "NOAGNTPERM";
    if (code == V3ActReason.NOUSRPERM)
      return "NOUSRPERM";
    if (code == V3ActReason.WRNGVER)
      return "WRNGVER";
    if (code == V3ActReason._CONTROLACTREASON)
      return "_ControlActReason";
    if (code == V3ActReason._MEDICATIONORDERABORTREASONCODE)
      return "_MedicationOrderAbortReasonCode";
    if (code == V3ActReason.DISCONT)
      return "DISCONT";
    if (code == V3ActReason.INEFFECT)
      return "INEFFECT";
    if (code == V3ActReason.MONIT)
      return "MONIT";
    if (code == V3ActReason.NOREQ)
      return "NOREQ";
    if (code == V3ActReason.NOTCOVER)
      return "NOTCOVER";
    if (code == V3ActReason.PREFUS)
      return "PREFUS";
    if (code == V3ActReason.RECALL)
      return "RECALL";
    if (code == V3ActReason.REPLACE)
      return "REPLACE";
    if (code == V3ActReason.DOSECHG)
      return "DOSECHG";
    if (code == V3ActReason.REPLACEFIX)
      return "REPLACEFIX";
    if (code == V3ActReason.UNABLE)
      return "UNABLE";
    if (code == V3ActReason._MEDICATIONORDERRELEASEREASONCODE)
      return "_MedicationOrderReleaseReasonCode";
    if (code == V3ActReason.HOLDDONE)
      return "HOLDDONE";
    if (code == V3ActReason.HOLDINAP)
      return "HOLDINAP";
    if (code == V3ActReason._MODIFYPRESCRIPTIONREASONTYPE)
      return "_ModifyPrescriptionReasonType";
    if (code == V3ActReason.ADMINERROR)
      return "ADMINERROR";
    if (code == V3ActReason.CLINMOD)
      return "CLINMOD";
    if (code == V3ActReason._PHARMACYSUPPLYEVENTABORTREASON)
      return "_PharmacySupplyEventAbortReason";
    if (code == V3ActReason.CONTRA)
      return "CONTRA";
    if (code == V3ActReason.FOABORT)
      return "FOABORT";
    if (code == V3ActReason.FOSUSP)
      return "FOSUSP";
    if (code == V3ActReason.NOPICK)
      return "NOPICK";
    if (code == V3ActReason.PATDEC)
      return "PATDEC";
    if (code == V3ActReason.QUANTCHG)
      return "QUANTCHG";
    if (code == V3ActReason._PHARMACYSUPPLYEVENTSTOCKREASONCODE)
      return "_PharmacySupplyEventStockReasonCode";
    if (code == V3ActReason.FLRSTCK)
      return "FLRSTCK";
    if (code == V3ActReason.LTC)
      return "LTC";
    if (code == V3ActReason.OFFICE)
      return "OFFICE";
    if (code == V3ActReason.PHARM)
      return "PHARM";
    if (code == V3ActReason.PROG)
      return "PROG";
    if (code == V3ActReason._PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE)
      return "_PharmacySupplyRequestRenewalRefusalReasonCode";
    if (code == V3ActReason.ALREADYRX)
      return "ALREADYRX";
    if (code == V3ActReason.FAMPHYS)
      return "FAMPHYS";
    if (code == V3ActReason.MODIFY)
      return "MODIFY";
    if (code == V3ActReason.NEEDAPMT)
      return "NEEDAPMT";
    if (code == V3ActReason.NOTAVAIL)
      return "NOTAVAIL";
    if (code == V3ActReason.NOTPAT)
      return "NOTPAT";
    if (code == V3ActReason.ONHOLD)
      return "ONHOLD";
    if (code == V3ActReason.PRNA)
      return "PRNA";
    if (code == V3ActReason.STOPMED)
      return "STOPMED";
    if (code == V3ActReason.TOOEARLY)
      return "TOOEARLY";
    if (code == V3ActReason._SUPPLYORDERABORTREASONCODE)
      return "_SupplyOrderAbortReasonCode";
    if (code == V3ActReason.IMPROV)
      return "IMPROV";
    if (code == V3ActReason.INTOL)
      return "INTOL";
    if (code == V3ActReason.NEWSTR)
      return "NEWSTR";
    if (code == V3ActReason.NEWTHER)
      return "NEWTHER";
    if (code == V3ActReason._GENERICUPDATEREASONCODE)
      return "_GenericUpdateReasonCode";
    if (code == V3ActReason.CHGDATA)
      return "CHGDATA";
    if (code == V3ActReason.FIXDATA)
      return "FIXDATA";
    if (code == V3ActReason.MDATA)
      return "MDATA";
    if (code == V3ActReason.NEWDATA)
      return "NEWDATA";
    if (code == V3ActReason.UMDATA)
      return "UMDATA";
    if (code == V3ActReason._PATIENTPROFILEQUERYREASONCODE)
      return "_PatientProfileQueryReasonCode";
    if (code == V3ActReason.ADMREV)
      return "ADMREV";
    if (code == V3ActReason.PATCAR)
      return "PATCAR";
    if (code == V3ActReason.PATREQ)
      return "PATREQ";
    if (code == V3ActReason.PRCREV)
      return "PRCREV";
    if (code == V3ActReason.REGUL)
      return "REGUL";
    if (code == V3ActReason.RSRCH)
      return "RSRCH";
    if (code == V3ActReason.VALIDATION)
      return "VALIDATION";
    if (code == V3ActReason._PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE)
      return "_PharmacySupplyRequestFulfillerRevisionRefusalReasonCode";
    if (code == V3ActReason.LOCKED)
      return "LOCKED";
    if (code == V3ActReason.UNKWNTARGET)
      return "UNKWNTARGET";
    if (code == V3ActReason._REFUSALREASONCODE)
      return "_RefusalReasonCode";
    if (code == V3ActReason._SCHEDULINGACTREASON)
      return "_SchedulingActReason";
    if (code == V3ActReason.BLK)
      return "BLK";
    if (code == V3ActReason.DEC)
      return "DEC";
    if (code == V3ActReason.FIN)
      return "FIN";
    if (code == V3ActReason.MED)
      return "MED";
    if (code == V3ActReason.MTG)
      return "MTG";
    if (code == V3ActReason.PHY)
      return "PHY";
    if (code == V3ActReason._STATUSREVISIONREFUSALREASONCODE)
      return "_StatusRevisionRefusalReasonCode";
    if (code == V3ActReason.FILLED)
      return "FILLED";
    if (code == V3ActReason._SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE)
      return "_SubstanceAdministrationPermissionRefusalReasonCode";
    if (code == V3ActReason.PATINELIG)
      return "PATINELIG";
    if (code == V3ActReason.PROTUNMET)
      return "PROTUNMET";
    if (code == V3ActReason.PROVUNAUTH)
      return "PROVUNAUTH";
    if (code == V3ActReason._SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON)
      return "_SubstanceAdminSubstitutionNotAllowedReason";
    if (code == V3ActReason.ALGINT)
      return "ALGINT";
    if (code == V3ActReason.COMPCON)
      return "COMPCON";
    if (code == V3ActReason.THERCHAR)
      return "THERCHAR";
    if (code == V3ActReason.TRIAL)
      return "TRIAL";
    if (code == V3ActReason._SUBSTANCEADMINSUBSTITUTIONREASON)
      return "_SubstanceAdminSubstitutionReason";
    if (code == V3ActReason.CT)
      return "CT";
    if (code == V3ActReason.FP)
      return "FP";
    if (code == V3ActReason.OS)
      return "OS";
    if (code == V3ActReason.RR)
      return "RR";
    if (code == V3ActReason._TRANSFERACTREASON)
      return "_TransferActReason";
    if (code == V3ActReason.ER)
      return "ER";
    if (code == V3ActReason.RQ)
      return "RQ";
    if (code == V3ActReason._ACTBILLABLESERVICEREASON)
      return "_ActBillableServiceReason";
    if (code == V3ActReason._ACTBILLABLECLINICALSERVICEREASON)
      return "_ActBillableClinicalServiceReason";
    if (code == V3ActReason.BONUS)
      return "BONUS";
    if (code == V3ActReason.CHD)
      return "CHD";
    if (code == V3ActReason.DEP)
      return "DEP";
    if (code == V3ActReason.ECH)
      return "ECH";
    if (code == V3ActReason.EDU)
      return "EDU";
    if (code == V3ActReason.EMP)
      return "EMP";
    if (code == V3ActReason.ESP)
      return "ESP";
    if (code == V3ActReason.FAM)
      return "FAM";
    if (code == V3ActReason.IND)
      return "IND";
    if (code == V3ActReason.INVOICE)
      return "INVOICE";
    if (code == V3ActReason.PROA)
      return "PROA";
    if (code == V3ActReason.RECOV)
      return "RECOV";
    if (code == V3ActReason.RETRO)
      return "RETRO";
    if (code == V3ActReason.SPC)
      return "SPC";
    if (code == V3ActReason.SPO)
      return "SPO";
    if (code == V3ActReason.TRAN)
      return "TRAN";
    return "?";
  }

    public String toSystem(V3ActReason code) {
      return code.getSystem();
      }

}

