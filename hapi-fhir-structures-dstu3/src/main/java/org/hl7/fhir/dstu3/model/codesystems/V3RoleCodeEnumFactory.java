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

public class V3RoleCodeEnumFactory implements EnumFactory<V3RoleCode> {

  public V3RoleCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_AffiliationRoleType".equals(codeString))
      return V3RoleCode._AFFILIATIONROLETYPE;
    if ("_AgentRoleType".equals(codeString))
      return V3RoleCode._AGENTROLETYPE;
    if ("AMENDER".equals(codeString))
      return V3RoleCode.AMENDER;
    if ("CLASSIFIER".equals(codeString))
      return V3RoleCode.CLASSIFIER;
    if ("CONSENTER".equals(codeString))
      return V3RoleCode.CONSENTER;
    if ("CONSWIT".equals(codeString))
      return V3RoleCode.CONSWIT;
    if ("COPART".equals(codeString))
      return V3RoleCode.COPART;
    if ("DECLASSIFIER".equals(codeString))
      return V3RoleCode.DECLASSIFIER;
    if ("DELEGATEE".equals(codeString))
      return V3RoleCode.DELEGATEE;
    if ("DELEGATOR".equals(codeString))
      return V3RoleCode.DELEGATOR;
    if ("DOWNGRDER".equals(codeString))
      return V3RoleCode.DOWNGRDER;
    if ("DRIVCLASSIFIER".equals(codeString))
      return V3RoleCode.DRIVCLASSIFIER;
    if ("GRANTEE".equals(codeString))
      return V3RoleCode.GRANTEE;
    if ("GRANTOR".equals(codeString))
      return V3RoleCode.GRANTOR;
    if ("INTPRTER".equals(codeString))
      return V3RoleCode.INTPRTER;
    if ("REVIEWER".equals(codeString))
      return V3RoleCode.REVIEWER;
    if ("VALIDATOR".equals(codeString))
      return V3RoleCode.VALIDATOR;
    if ("_CoverageSponsorRoleType".equals(codeString))
      return V3RoleCode._COVERAGESPONSORROLETYPE;
    if ("FULLINS".equals(codeString))
      return V3RoleCode.FULLINS;
    if ("SELFINS".equals(codeString))
      return V3RoleCode.SELFINS;
    if ("_PayorRoleType".equals(codeString))
      return V3RoleCode._PAYORROLETYPE;
    if ("ENROLBKR".equals(codeString))
      return V3RoleCode.ENROLBKR;
    if ("TPA".equals(codeString))
      return V3RoleCode.TPA;
    if ("UMO".equals(codeString))
      return V3RoleCode.UMO;
    if ("RESPRSN".equals(codeString))
      return V3RoleCode.RESPRSN;
    if ("EXCEST".equals(codeString))
      return V3RoleCode.EXCEST;
    if ("GUADLTM".equals(codeString))
      return V3RoleCode.GUADLTM;
    if ("GUARD".equals(codeString))
      return V3RoleCode.GUARD;
    if ("POWATT".equals(codeString))
      return V3RoleCode.POWATT;
    if ("DPOWATT".equals(codeString))
      return V3RoleCode.DPOWATT;
    if ("HPOWATT".equals(codeString))
      return V3RoleCode.HPOWATT;
    if ("SPOWATT".equals(codeString))
      return V3RoleCode.SPOWATT;
    if ("_AssignedRoleType".equals(codeString))
      return V3RoleCode._ASSIGNEDROLETYPE;
    if ("_AssignedNonPersonLivingSubjectRoleType".equals(codeString))
      return V3RoleCode._ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE;
    if ("ASSIST".equals(codeString))
      return V3RoleCode.ASSIST;
    if ("BIOTH".equals(codeString))
      return V3RoleCode.BIOTH;
    if ("ANTIBIOT".equals(codeString))
      return V3RoleCode.ANTIBIOT;
    if ("DEBR".equals(codeString))
      return V3RoleCode.DEBR;
    if ("CCO".equals(codeString))
      return V3RoleCode.CCO;
    if ("SEE".equals(codeString))
      return V3RoleCode.SEE;
    if ("SNIFF".equals(codeString))
      return V3RoleCode.SNIFF;
    if ("_CertifiedEntityType".equals(codeString))
      return V3RoleCode._CERTIFIEDENTITYTYPE;
    if ("_CitizenRoleType".equals(codeString))
      return V3RoleCode._CITIZENROLETYPE;
    if ("CAS".equals(codeString))
      return V3RoleCode.CAS;
    if ("CASM".equals(codeString))
      return V3RoleCode.CASM;
    if ("CN".equals(codeString))
      return V3RoleCode.CN;
    if ("CNRP".equals(codeString))
      return V3RoleCode.CNRP;
    if ("CNRPM".equals(codeString))
      return V3RoleCode.CNRPM;
    if ("CPCA".equals(codeString))
      return V3RoleCode.CPCA;
    if ("CRP".equals(codeString))
      return V3RoleCode.CRP;
    if ("CRPM".equals(codeString))
      return V3RoleCode.CRPM;
    if ("_ContactRoleType".equals(codeString))
      return V3RoleCode._CONTACTROLETYPE;
    if ("_AdministrativeContactRoleType".equals(codeString))
      return V3RoleCode._ADMINISTRATIVECONTACTROLETYPE;
    if ("BILL".equals(codeString))
      return V3RoleCode.BILL;
    if ("ORG".equals(codeString))
      return V3RoleCode.ORG;
    if ("PAYOR".equals(codeString))
      return V3RoleCode.PAYOR;
    if ("ECON".equals(codeString))
      return V3RoleCode.ECON;
    if ("NOK".equals(codeString))
      return V3RoleCode.NOK;
    if ("_IdentifiedEntityType".equals(codeString))
      return V3RoleCode._IDENTIFIEDENTITYTYPE;
    if ("_LocationIdentifiedEntityRoleCode".equals(codeString))
      return V3RoleCode._LOCATIONIDENTIFIEDENTITYROLECODE;
    if ("ACHFID".equals(codeString))
      return V3RoleCode.ACHFID;
    if ("JURID".equals(codeString))
      return V3RoleCode.JURID;
    if ("LOCHFID".equals(codeString))
      return V3RoleCode.LOCHFID;
    if ("_LivingSubjectProductionClass".equals(codeString))
      return V3RoleCode._LIVINGSUBJECTPRODUCTIONCLASS;
    if ("BF".equals(codeString))
      return V3RoleCode.BF;
    if ("BL".equals(codeString))
      return V3RoleCode.BL;
    if ("BR".equals(codeString))
      return V3RoleCode.BR;
    if ("CO".equals(codeString))
      return V3RoleCode.CO;
    if ("DA".equals(codeString))
      return V3RoleCode.DA;
    if ("DR".equals(codeString))
      return V3RoleCode.DR;
    if ("DU".equals(codeString))
      return V3RoleCode.DU;
    if ("FI".equals(codeString))
      return V3RoleCode.FI;
    if ("LY".equals(codeString))
      return V3RoleCode.LY;
    if ("MT".equals(codeString))
      return V3RoleCode.MT;
    if ("MU".equals(codeString))
      return V3RoleCode.MU;
    if ("PL".equals(codeString))
      return V3RoleCode.PL;
    if ("RC".equals(codeString))
      return V3RoleCode.RC;
    if ("SH".equals(codeString))
      return V3RoleCode.SH;
    if ("VL".equals(codeString))
      return V3RoleCode.VL;
    if ("WL".equals(codeString))
      return V3RoleCode.WL;
    if ("WO".equals(codeString))
      return V3RoleCode.WO;
    if ("_MedicationGeneralizationRoleType".equals(codeString))
      return V3RoleCode._MEDICATIONGENERALIZATIONROLETYPE;
    if ("DC".equals(codeString))
      return V3RoleCode.DC;
    if ("GD".equals(codeString))
      return V3RoleCode.GD;
    if ("GDF".equals(codeString))
      return V3RoleCode.GDF;
    if ("GDS".equals(codeString))
      return V3RoleCode.GDS;
    if ("GDSF".equals(codeString))
      return V3RoleCode.GDSF;
    if ("MGDSF".equals(codeString))
      return V3RoleCode.MGDSF;
    if ("_MemberRoleType".equals(codeString))
      return V3RoleCode._MEMBERROLETYPE;
    if ("TRB".equals(codeString))
      return V3RoleCode.TRB;
    if ("_PersonalRelationshipRoleType".equals(codeString))
      return V3RoleCode._PERSONALRELATIONSHIPROLETYPE;
    if ("FAMMEMB".equals(codeString))
      return V3RoleCode.FAMMEMB;
    if ("CHILD".equals(codeString))
      return V3RoleCode.CHILD;
    if ("CHLDADOPT".equals(codeString))
      return V3RoleCode.CHLDADOPT;
    if ("DAUADOPT".equals(codeString))
      return V3RoleCode.DAUADOPT;
    if ("SONADOPT".equals(codeString))
      return V3RoleCode.SONADOPT;
    if ("CHLDFOST".equals(codeString))
      return V3RoleCode.CHLDFOST;
    if ("DAUFOST".equals(codeString))
      return V3RoleCode.DAUFOST;
    if ("SONFOST".equals(codeString))
      return V3RoleCode.SONFOST;
    if ("DAUC".equals(codeString))
      return V3RoleCode.DAUC;
    if ("DAU".equals(codeString))
      return V3RoleCode.DAU;
    if ("STPDAU".equals(codeString))
      return V3RoleCode.STPDAU;
    if ("NCHILD".equals(codeString))
      return V3RoleCode.NCHILD;
    if ("SON".equals(codeString))
      return V3RoleCode.SON;
    if ("SONC".equals(codeString))
      return V3RoleCode.SONC;
    if ("STPSON".equals(codeString))
      return V3RoleCode.STPSON;
    if ("STPCHLD".equals(codeString))
      return V3RoleCode.STPCHLD;
    if ("EXT".equals(codeString))
      return V3RoleCode.EXT;
    if ("AUNT".equals(codeString))
      return V3RoleCode.AUNT;
    if ("MAUNT".equals(codeString))
      return V3RoleCode.MAUNT;
    if ("PAUNT".equals(codeString))
      return V3RoleCode.PAUNT;
    if ("COUSN".equals(codeString))
      return V3RoleCode.COUSN;
    if ("MCOUSN".equals(codeString))
      return V3RoleCode.MCOUSN;
    if ("PCOUSN".equals(codeString))
      return V3RoleCode.PCOUSN;
    if ("GGRPRN".equals(codeString))
      return V3RoleCode.GGRPRN;
    if ("GGRFTH".equals(codeString))
      return V3RoleCode.GGRFTH;
    if ("MGGRFTH".equals(codeString))
      return V3RoleCode.MGGRFTH;
    if ("PGGRFTH".equals(codeString))
      return V3RoleCode.PGGRFTH;
    if ("GGRMTH".equals(codeString))
      return V3RoleCode.GGRMTH;
    if ("MGGRMTH".equals(codeString))
      return V3RoleCode.MGGRMTH;
    if ("PGGRMTH".equals(codeString))
      return V3RoleCode.PGGRMTH;
    if ("MGGRPRN".equals(codeString))
      return V3RoleCode.MGGRPRN;
    if ("PGGRPRN".equals(codeString))
      return V3RoleCode.PGGRPRN;
    if ("GRNDCHILD".equals(codeString))
      return V3RoleCode.GRNDCHILD;
    if ("GRNDDAU".equals(codeString))
      return V3RoleCode.GRNDDAU;
    if ("GRNDSON".equals(codeString))
      return V3RoleCode.GRNDSON;
    if ("GRPRN".equals(codeString))
      return V3RoleCode.GRPRN;
    if ("GRFTH".equals(codeString))
      return V3RoleCode.GRFTH;
    if ("MGRFTH".equals(codeString))
      return V3RoleCode.MGRFTH;
    if ("PGRFTH".equals(codeString))
      return V3RoleCode.PGRFTH;
    if ("GRMTH".equals(codeString))
      return V3RoleCode.GRMTH;
    if ("MGRMTH".equals(codeString))
      return V3RoleCode.MGRMTH;
    if ("PGRMTH".equals(codeString))
      return V3RoleCode.PGRMTH;
    if ("MGRPRN".equals(codeString))
      return V3RoleCode.MGRPRN;
    if ("PGRPRN".equals(codeString))
      return V3RoleCode.PGRPRN;
    if ("INLAW".equals(codeString))
      return V3RoleCode.INLAW;
    if ("CHLDINLAW".equals(codeString))
      return V3RoleCode.CHLDINLAW;
    if ("DAUINLAW".equals(codeString))
      return V3RoleCode.DAUINLAW;
    if ("SONINLAW".equals(codeString))
      return V3RoleCode.SONINLAW;
    if ("PRNINLAW".equals(codeString))
      return V3RoleCode.PRNINLAW;
    if ("FTHINLAW".equals(codeString))
      return V3RoleCode.FTHINLAW;
    if ("MTHINLAW".equals(codeString))
      return V3RoleCode.MTHINLAW;
    if ("SIBINLAW".equals(codeString))
      return V3RoleCode.SIBINLAW;
    if ("BROINLAW".equals(codeString))
      return V3RoleCode.BROINLAW;
    if ("SISINLAW".equals(codeString))
      return V3RoleCode.SISINLAW;
    if ("NIENEPH".equals(codeString))
      return V3RoleCode.NIENEPH;
    if ("NEPHEW".equals(codeString))
      return V3RoleCode.NEPHEW;
    if ("NIECE".equals(codeString))
      return V3RoleCode.NIECE;
    if ("UNCLE".equals(codeString))
      return V3RoleCode.UNCLE;
    if ("MUNCLE".equals(codeString))
      return V3RoleCode.MUNCLE;
    if ("PUNCLE".equals(codeString))
      return V3RoleCode.PUNCLE;
    if ("PRN".equals(codeString))
      return V3RoleCode.PRN;
    if ("ADOPTP".equals(codeString))
      return V3RoleCode.ADOPTP;
    if ("ADOPTF".equals(codeString))
      return V3RoleCode.ADOPTF;
    if ("ADOPTM".equals(codeString))
      return V3RoleCode.ADOPTM;
    if ("FTH".equals(codeString))
      return V3RoleCode.FTH;
    if ("FTHFOST".equals(codeString))
      return V3RoleCode.FTHFOST;
    if ("NFTH".equals(codeString))
      return V3RoleCode.NFTH;
    if ("NFTHF".equals(codeString))
      return V3RoleCode.NFTHF;
    if ("STPFTH".equals(codeString))
      return V3RoleCode.STPFTH;
    if ("MTH".equals(codeString))
      return V3RoleCode.MTH;
    if ("GESTM".equals(codeString))
      return V3RoleCode.GESTM;
    if ("MTHFOST".equals(codeString))
      return V3RoleCode.MTHFOST;
    if ("NMTH".equals(codeString))
      return V3RoleCode.NMTH;
    if ("NMTHF".equals(codeString))
      return V3RoleCode.NMTHF;
    if ("STPMTH".equals(codeString))
      return V3RoleCode.STPMTH;
    if ("NPRN".equals(codeString))
      return V3RoleCode.NPRN;
    if ("PRNFOST".equals(codeString))
      return V3RoleCode.PRNFOST;
    if ("STPPRN".equals(codeString))
      return V3RoleCode.STPPRN;
    if ("SIB".equals(codeString))
      return V3RoleCode.SIB;
    if ("BRO".equals(codeString))
      return V3RoleCode.BRO;
    if ("HBRO".equals(codeString))
      return V3RoleCode.HBRO;
    if ("NBRO".equals(codeString))
      return V3RoleCode.NBRO;
    if ("TWINBRO".equals(codeString))
      return V3RoleCode.TWINBRO;
    if ("FTWINBRO".equals(codeString))
      return V3RoleCode.FTWINBRO;
    if ("ITWINBRO".equals(codeString))
      return V3RoleCode.ITWINBRO;
    if ("STPBRO".equals(codeString))
      return V3RoleCode.STPBRO;
    if ("HSIB".equals(codeString))
      return V3RoleCode.HSIB;
    if ("HSIS".equals(codeString))
      return V3RoleCode.HSIS;
    if ("NSIB".equals(codeString))
      return V3RoleCode.NSIB;
    if ("NSIS".equals(codeString))
      return V3RoleCode.NSIS;
    if ("TWINSIS".equals(codeString))
      return V3RoleCode.TWINSIS;
    if ("FTWINSIS".equals(codeString))
      return V3RoleCode.FTWINSIS;
    if ("ITWINSIS".equals(codeString))
      return V3RoleCode.ITWINSIS;
    if ("TWIN".equals(codeString))
      return V3RoleCode.TWIN;
    if ("FTWIN".equals(codeString))
      return V3RoleCode.FTWIN;
    if ("ITWIN".equals(codeString))
      return V3RoleCode.ITWIN;
    if ("SIS".equals(codeString))
      return V3RoleCode.SIS;
    if ("STPSIS".equals(codeString))
      return V3RoleCode.STPSIS;
    if ("STPSIB".equals(codeString))
      return V3RoleCode.STPSIB;
    if ("SIGOTHR".equals(codeString))
      return V3RoleCode.SIGOTHR;
    if ("DOMPART".equals(codeString))
      return V3RoleCode.DOMPART;
    if ("FMRSPS".equals(codeString))
      return V3RoleCode.FMRSPS;
    if ("SPS".equals(codeString))
      return V3RoleCode.SPS;
    if ("HUSB".equals(codeString))
      return V3RoleCode.HUSB;
    if ("WIFE".equals(codeString))
      return V3RoleCode.WIFE;
    if ("FRND".equals(codeString))
      return V3RoleCode.FRND;
    if ("NBOR".equals(codeString))
      return V3RoleCode.NBOR;
    if ("ONESELF".equals(codeString))
      return V3RoleCode.ONESELF;
    if ("ROOM".equals(codeString))
      return V3RoleCode.ROOM;
    if ("_PolicyOrProgramCoverageRoleType".equals(codeString))
      return V3RoleCode._POLICYORPROGRAMCOVERAGEROLETYPE;
    if ("_CoverageRoleType".equals(codeString))
      return V3RoleCode._COVERAGEROLETYPE;
    if ("FAMDEP".equals(codeString))
      return V3RoleCode.FAMDEP;
    if ("HANDIC".equals(codeString))
      return V3RoleCode.HANDIC;
    if ("INJ".equals(codeString))
      return V3RoleCode.INJ;
    if ("SELF".equals(codeString))
      return V3RoleCode.SELF;
    if ("SPON".equals(codeString))
      return V3RoleCode.SPON;
    if ("STUD".equals(codeString))
      return V3RoleCode.STUD;
    if ("FSTUD".equals(codeString))
      return V3RoleCode.FSTUD;
    if ("PSTUD".equals(codeString))
      return V3RoleCode.PSTUD;
    if ("ADOPT".equals(codeString))
      return V3RoleCode.ADOPT;
    if ("GCHILD".equals(codeString))
      return V3RoleCode.GCHILD;
    if ("GPARNT".equals(codeString))
      return V3RoleCode.GPARNT;
    if ("NAT".equals(codeString))
      return V3RoleCode.NAT;
    if ("NIENE".equals(codeString))
      return V3RoleCode.NIENE;
    if ("PARNT".equals(codeString))
      return V3RoleCode.PARNT;
    if ("SPSE".equals(codeString))
      return V3RoleCode.SPSE;
    if ("STEP".equals(codeString))
      return V3RoleCode.STEP;
    if ("_CoveredPartyRoleType".equals(codeString))
      return V3RoleCode._COVEREDPARTYROLETYPE;
    if ("_ClaimantCoveredPartyRoleType".equals(codeString))
      return V3RoleCode._CLAIMANTCOVEREDPARTYROLETYPE;
    if ("CRIMEVIC".equals(codeString))
      return V3RoleCode.CRIMEVIC;
    if ("INJWKR".equals(codeString))
      return V3RoleCode.INJWKR;
    if ("_DependentCoveredPartyRoleType".equals(codeString))
      return V3RoleCode._DEPENDENTCOVEREDPARTYROLETYPE;
    if ("COCBEN".equals(codeString))
      return V3RoleCode.COCBEN;
    if ("DIFFABL".equals(codeString))
      return V3RoleCode.DIFFABL;
    if ("WARD".equals(codeString))
      return V3RoleCode.WARD;
    if ("_IndividualInsuredPartyRoleType".equals(codeString))
      return V3RoleCode._INDIVIDUALINSUREDPARTYROLETYPE;
    if ("RETIREE".equals(codeString))
      return V3RoleCode.RETIREE;
    if ("_ProgramEligiblePartyRoleType".equals(codeString))
      return V3RoleCode._PROGRAMELIGIBLEPARTYROLETYPE;
    if ("INDIG".equals(codeString))
      return V3RoleCode.INDIG;
    if ("MIL".equals(codeString))
      return V3RoleCode.MIL;
    if ("ACTMIL".equals(codeString))
      return V3RoleCode.ACTMIL;
    if ("RETMIL".equals(codeString))
      return V3RoleCode.RETMIL;
    if ("VET".equals(codeString))
      return V3RoleCode.VET;
    if ("_SubscriberCoveredPartyRoleType".equals(codeString))
      return V3RoleCode._SUBSCRIBERCOVEREDPARTYROLETYPE;
    if ("_ResearchSubjectRoleBasis".equals(codeString))
      return V3RoleCode._RESEARCHSUBJECTROLEBASIS;
    if ("ERL".equals(codeString))
      return V3RoleCode.ERL;
    if ("SCN".equals(codeString))
      return V3RoleCode.SCN;
    if ("_ServiceDeliveryLocationRoleType".equals(codeString))
      return V3RoleCode._SERVICEDELIVERYLOCATIONROLETYPE;
    if ("_DedicatedServiceDeliveryLocationRoleType".equals(codeString))
      return V3RoleCode._DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE;
    if ("_DedicatedClinicalLocationRoleType".equals(codeString))
      return V3RoleCode._DEDICATEDCLINICALLOCATIONROLETYPE;
    if ("DX".equals(codeString))
      return V3RoleCode.DX;
    if ("CVDX".equals(codeString))
      return V3RoleCode.CVDX;
    if ("CATH".equals(codeString))
      return V3RoleCode.CATH;
    if ("ECHO".equals(codeString))
      return V3RoleCode.ECHO;
    if ("GIDX".equals(codeString))
      return V3RoleCode.GIDX;
    if ("ENDOS".equals(codeString))
      return V3RoleCode.ENDOS;
    if ("RADDX".equals(codeString))
      return V3RoleCode.RADDX;
    if ("RADO".equals(codeString))
      return V3RoleCode.RADO;
    if ("RNEU".equals(codeString))
      return V3RoleCode.RNEU;
    if ("HOSP".equals(codeString))
      return V3RoleCode.HOSP;
    if ("CHR".equals(codeString))
      return V3RoleCode.CHR;
    if ("GACH".equals(codeString))
      return V3RoleCode.GACH;
    if ("MHSP".equals(codeString))
      return V3RoleCode.MHSP;
    if ("PSYCHF".equals(codeString))
      return V3RoleCode.PSYCHF;
    if ("RH".equals(codeString))
      return V3RoleCode.RH;
    if ("RHAT".equals(codeString))
      return V3RoleCode.RHAT;
    if ("RHII".equals(codeString))
      return V3RoleCode.RHII;
    if ("RHMAD".equals(codeString))
      return V3RoleCode.RHMAD;
    if ("RHPI".equals(codeString))
      return V3RoleCode.RHPI;
    if ("RHPIH".equals(codeString))
      return V3RoleCode.RHPIH;
    if ("RHPIMS".equals(codeString))
      return V3RoleCode.RHPIMS;
    if ("RHPIVS".equals(codeString))
      return V3RoleCode.RHPIVS;
    if ("RHYAD".equals(codeString))
      return V3RoleCode.RHYAD;
    if ("HU".equals(codeString))
      return V3RoleCode.HU;
    if ("BMTU".equals(codeString))
      return V3RoleCode.BMTU;
    if ("CCU".equals(codeString))
      return V3RoleCode.CCU;
    if ("CHEST".equals(codeString))
      return V3RoleCode.CHEST;
    if ("EPIL".equals(codeString))
      return V3RoleCode.EPIL;
    if ("ER".equals(codeString))
      return V3RoleCode.ER;
    if ("ETU".equals(codeString))
      return V3RoleCode.ETU;
    if ("HD".equals(codeString))
      return V3RoleCode.HD;
    if ("HLAB".equals(codeString))
      return V3RoleCode.HLAB;
    if ("INLAB".equals(codeString))
      return V3RoleCode.INLAB;
    if ("OUTLAB".equals(codeString))
      return V3RoleCode.OUTLAB;
    if ("HRAD".equals(codeString))
      return V3RoleCode.HRAD;
    if ("HUSCS".equals(codeString))
      return V3RoleCode.HUSCS;
    if ("ICU".equals(codeString))
      return V3RoleCode.ICU;
    if ("PEDICU".equals(codeString))
      return V3RoleCode.PEDICU;
    if ("PEDNICU".equals(codeString))
      return V3RoleCode.PEDNICU;
    if ("INPHARM".equals(codeString))
      return V3RoleCode.INPHARM;
    if ("MBL".equals(codeString))
      return V3RoleCode.MBL;
    if ("NCCS".equals(codeString))
      return V3RoleCode.NCCS;
    if ("NS".equals(codeString))
      return V3RoleCode.NS;
    if ("OUTPHARM".equals(codeString))
      return V3RoleCode.OUTPHARM;
    if ("PEDU".equals(codeString))
      return V3RoleCode.PEDU;
    if ("PHU".equals(codeString))
      return V3RoleCode.PHU;
    if ("RHU".equals(codeString))
      return V3RoleCode.RHU;
    if ("SLEEP".equals(codeString))
      return V3RoleCode.SLEEP;
    if ("NCCF".equals(codeString))
      return V3RoleCode.NCCF;
    if ("SNF".equals(codeString))
      return V3RoleCode.SNF;
    if ("OF".equals(codeString))
      return V3RoleCode.OF;
    if ("ALL".equals(codeString))
      return V3RoleCode.ALL;
    if ("AMPUT".equals(codeString))
      return V3RoleCode.AMPUT;
    if ("BMTC".equals(codeString))
      return V3RoleCode.BMTC;
    if ("BREAST".equals(codeString))
      return V3RoleCode.BREAST;
    if ("CANC".equals(codeString))
      return V3RoleCode.CANC;
    if ("CAPC".equals(codeString))
      return V3RoleCode.CAPC;
    if ("CARD".equals(codeString))
      return V3RoleCode.CARD;
    if ("PEDCARD".equals(codeString))
      return V3RoleCode.PEDCARD;
    if ("COAG".equals(codeString))
      return V3RoleCode.COAG;
    if ("CRS".equals(codeString))
      return V3RoleCode.CRS;
    if ("DERM".equals(codeString))
      return V3RoleCode.DERM;
    if ("ENDO".equals(codeString))
      return V3RoleCode.ENDO;
    if ("PEDE".equals(codeString))
      return V3RoleCode.PEDE;
    if ("ENT".equals(codeString))
      return V3RoleCode.ENT;
    if ("FMC".equals(codeString))
      return V3RoleCode.FMC;
    if ("GI".equals(codeString))
      return V3RoleCode.GI;
    if ("PEDGI".equals(codeString))
      return V3RoleCode.PEDGI;
    if ("GIM".equals(codeString))
      return V3RoleCode.GIM;
    if ("GYN".equals(codeString))
      return V3RoleCode.GYN;
    if ("HEM".equals(codeString))
      return V3RoleCode.HEM;
    if ("PEDHEM".equals(codeString))
      return V3RoleCode.PEDHEM;
    if ("HTN".equals(codeString))
      return V3RoleCode.HTN;
    if ("IEC".equals(codeString))
      return V3RoleCode.IEC;
    if ("INFD".equals(codeString))
      return V3RoleCode.INFD;
    if ("PEDID".equals(codeString))
      return V3RoleCode.PEDID;
    if ("INV".equals(codeString))
      return V3RoleCode.INV;
    if ("LYMPH".equals(codeString))
      return V3RoleCode.LYMPH;
    if ("MGEN".equals(codeString))
      return V3RoleCode.MGEN;
    if ("NEPH".equals(codeString))
      return V3RoleCode.NEPH;
    if ("PEDNEPH".equals(codeString))
      return V3RoleCode.PEDNEPH;
    if ("NEUR".equals(codeString))
      return V3RoleCode.NEUR;
    if ("OB".equals(codeString))
      return V3RoleCode.OB;
    if ("OMS".equals(codeString))
      return V3RoleCode.OMS;
    if ("ONCL".equals(codeString))
      return V3RoleCode.ONCL;
    if ("PEDHO".equals(codeString))
      return V3RoleCode.PEDHO;
    if ("OPH".equals(codeString))
      return V3RoleCode.OPH;
    if ("OPTC".equals(codeString))
      return V3RoleCode.OPTC;
    if ("ORTHO".equals(codeString))
      return V3RoleCode.ORTHO;
    if ("HAND".equals(codeString))
      return V3RoleCode.HAND;
    if ("PAINCL".equals(codeString))
      return V3RoleCode.PAINCL;
    if ("PC".equals(codeString))
      return V3RoleCode.PC;
    if ("PEDC".equals(codeString))
      return V3RoleCode.PEDC;
    if ("PEDRHEUM".equals(codeString))
      return V3RoleCode.PEDRHEUM;
    if ("POD".equals(codeString))
      return V3RoleCode.POD;
    if ("PREV".equals(codeString))
      return V3RoleCode.PREV;
    if ("PROCTO".equals(codeString))
      return V3RoleCode.PROCTO;
    if ("PROFF".equals(codeString))
      return V3RoleCode.PROFF;
    if ("PROS".equals(codeString))
      return V3RoleCode.PROS;
    if ("PSI".equals(codeString))
      return V3RoleCode.PSI;
    if ("PSY".equals(codeString))
      return V3RoleCode.PSY;
    if ("RHEUM".equals(codeString))
      return V3RoleCode.RHEUM;
    if ("SPMED".equals(codeString))
      return V3RoleCode.SPMED;
    if ("SU".equals(codeString))
      return V3RoleCode.SU;
    if ("PLS".equals(codeString))
      return V3RoleCode.PLS;
    if ("URO".equals(codeString))
      return V3RoleCode.URO;
    if ("TR".equals(codeString))
      return V3RoleCode.TR;
    if ("TRAVEL".equals(codeString))
      return V3RoleCode.TRAVEL;
    if ("WND".equals(codeString))
      return V3RoleCode.WND;
    if ("RTF".equals(codeString))
      return V3RoleCode.RTF;
    if ("PRC".equals(codeString))
      return V3RoleCode.PRC;
    if ("SURF".equals(codeString))
      return V3RoleCode.SURF;
    if ("_DedicatedNonClinicalLocationRoleType".equals(codeString))
      return V3RoleCode._DEDICATEDNONCLINICALLOCATIONROLETYPE;
    if ("DADDR".equals(codeString))
      return V3RoleCode.DADDR;
    if ("MOBL".equals(codeString))
      return V3RoleCode.MOBL;
    if ("AMB".equals(codeString))
      return V3RoleCode.AMB;
    if ("PHARM".equals(codeString))
      return V3RoleCode.PHARM;
    if ("_IncidentalServiceDeliveryLocationRoleType".equals(codeString))
      return V3RoleCode._INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE;
    if ("ACC".equals(codeString))
      return V3RoleCode.ACC;
    if ("COMM".equals(codeString))
      return V3RoleCode.COMM;
    if ("CSC".equals(codeString))
      return V3RoleCode.CSC;
    if ("PTRES".equals(codeString))
      return V3RoleCode.PTRES;
    if ("SCHOOL".equals(codeString))
      return V3RoleCode.SCHOOL;
    if ("UPC".equals(codeString))
      return V3RoleCode.UPC;
    if ("WORK".equals(codeString))
      return V3RoleCode.WORK;
    if ("_SpecimenRoleType".equals(codeString))
      return V3RoleCode._SPECIMENROLETYPE;
    if ("C".equals(codeString))
      return V3RoleCode.C;
    if ("G".equals(codeString))
      return V3RoleCode.G;
    if ("L".equals(codeString))
      return V3RoleCode.L;
    if ("P".equals(codeString))
      return V3RoleCode.P;
    if ("Q".equals(codeString))
      return V3RoleCode.Q;
    if ("B".equals(codeString))
      return V3RoleCode.B;
    if ("E".equals(codeString))
      return V3RoleCode.E;
    if ("F".equals(codeString))
      return V3RoleCode.F;
    if ("O".equals(codeString))
      return V3RoleCode.O;
    if ("V".equals(codeString))
      return V3RoleCode.V;
    if ("R".equals(codeString))
      return V3RoleCode.R;
    if ("CLAIM".equals(codeString))
      return V3RoleCode.CLAIM;
    if ("communityLaboratory".equals(codeString))
      return V3RoleCode.COMMUNITYLABORATORY;
    if ("GT".equals(codeString))
      return V3RoleCode.GT;
    if ("homeHealth".equals(codeString))
      return V3RoleCode.HOMEHEALTH;
    if ("laboratory".equals(codeString))
      return V3RoleCode.LABORATORY;
    if ("pathologist".equals(codeString))
      return V3RoleCode.PATHOLOGIST;
    if ("PH".equals(codeString))
      return V3RoleCode.PH;
    if ("phlebotomist".equals(codeString))
      return V3RoleCode.PHLEBOTOMIST;
    if ("PROG".equals(codeString))
      return V3RoleCode.PROG;
    if ("PT".equals(codeString))
      return V3RoleCode.PT;
    if ("subject".equals(codeString))
      return V3RoleCode.SUBJECT;
    if ("thirdParty".equals(codeString))
      return V3RoleCode.THIRDPARTY;
    if ("DEP".equals(codeString))
      return V3RoleCode.DEP;
    if ("DEPEN".equals(codeString))
      return V3RoleCode.DEPEN;
    if ("FM".equals(codeString))
      return V3RoleCode.FM;
    if ("INDIV".equals(codeString))
      return V3RoleCode.INDIV;
    if ("NAMED".equals(codeString))
      return V3RoleCode.NAMED;
    if ("PSYCHCF".equals(codeString))
      return V3RoleCode.PSYCHCF;
    if ("SUBSCR".equals(codeString))
      return V3RoleCode.SUBSCR;
    throw new IllegalArgumentException("Unknown V3RoleCode code '"+codeString+"'");
  }

  public String toCode(V3RoleCode code) {
    if (code == V3RoleCode._AFFILIATIONROLETYPE)
      return "_AffiliationRoleType";
    if (code == V3RoleCode._AGENTROLETYPE)
      return "_AgentRoleType";
    if (code == V3RoleCode.AMENDER)
      return "AMENDER";
    if (code == V3RoleCode.CLASSIFIER)
      return "CLASSIFIER";
    if (code == V3RoleCode.CONSENTER)
      return "CONSENTER";
    if (code == V3RoleCode.CONSWIT)
      return "CONSWIT";
    if (code == V3RoleCode.COPART)
      return "COPART";
    if (code == V3RoleCode.DECLASSIFIER)
      return "DECLASSIFIER";
    if (code == V3RoleCode.DELEGATEE)
      return "DELEGATEE";
    if (code == V3RoleCode.DELEGATOR)
      return "DELEGATOR";
    if (code == V3RoleCode.DOWNGRDER)
      return "DOWNGRDER";
    if (code == V3RoleCode.DRIVCLASSIFIER)
      return "DRIVCLASSIFIER";
    if (code == V3RoleCode.GRANTEE)
      return "GRANTEE";
    if (code == V3RoleCode.GRANTOR)
      return "GRANTOR";
    if (code == V3RoleCode.INTPRTER)
      return "INTPRTER";
    if (code == V3RoleCode.REVIEWER)
      return "REVIEWER";
    if (code == V3RoleCode.VALIDATOR)
      return "VALIDATOR";
    if (code == V3RoleCode._COVERAGESPONSORROLETYPE)
      return "_CoverageSponsorRoleType";
    if (code == V3RoleCode.FULLINS)
      return "FULLINS";
    if (code == V3RoleCode.SELFINS)
      return "SELFINS";
    if (code == V3RoleCode._PAYORROLETYPE)
      return "_PayorRoleType";
    if (code == V3RoleCode.ENROLBKR)
      return "ENROLBKR";
    if (code == V3RoleCode.TPA)
      return "TPA";
    if (code == V3RoleCode.UMO)
      return "UMO";
    if (code == V3RoleCode.RESPRSN)
      return "RESPRSN";
    if (code == V3RoleCode.EXCEST)
      return "EXCEST";
    if (code == V3RoleCode.GUADLTM)
      return "GUADLTM";
    if (code == V3RoleCode.GUARD)
      return "GUARD";
    if (code == V3RoleCode.POWATT)
      return "POWATT";
    if (code == V3RoleCode.DPOWATT)
      return "DPOWATT";
    if (code == V3RoleCode.HPOWATT)
      return "HPOWATT";
    if (code == V3RoleCode.SPOWATT)
      return "SPOWATT";
    if (code == V3RoleCode._ASSIGNEDROLETYPE)
      return "_AssignedRoleType";
    if (code == V3RoleCode._ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE)
      return "_AssignedNonPersonLivingSubjectRoleType";
    if (code == V3RoleCode.ASSIST)
      return "ASSIST";
    if (code == V3RoleCode.BIOTH)
      return "BIOTH";
    if (code == V3RoleCode.ANTIBIOT)
      return "ANTIBIOT";
    if (code == V3RoleCode.DEBR)
      return "DEBR";
    if (code == V3RoleCode.CCO)
      return "CCO";
    if (code == V3RoleCode.SEE)
      return "SEE";
    if (code == V3RoleCode.SNIFF)
      return "SNIFF";
    if (code == V3RoleCode._CERTIFIEDENTITYTYPE)
      return "_CertifiedEntityType";
    if (code == V3RoleCode._CITIZENROLETYPE)
      return "_CitizenRoleType";
    if (code == V3RoleCode.CAS)
      return "CAS";
    if (code == V3RoleCode.CASM)
      return "CASM";
    if (code == V3RoleCode.CN)
      return "CN";
    if (code == V3RoleCode.CNRP)
      return "CNRP";
    if (code == V3RoleCode.CNRPM)
      return "CNRPM";
    if (code == V3RoleCode.CPCA)
      return "CPCA";
    if (code == V3RoleCode.CRP)
      return "CRP";
    if (code == V3RoleCode.CRPM)
      return "CRPM";
    if (code == V3RoleCode._CONTACTROLETYPE)
      return "_ContactRoleType";
    if (code == V3RoleCode._ADMINISTRATIVECONTACTROLETYPE)
      return "_AdministrativeContactRoleType";
    if (code == V3RoleCode.BILL)
      return "BILL";
    if (code == V3RoleCode.ORG)
      return "ORG";
    if (code == V3RoleCode.PAYOR)
      return "PAYOR";
    if (code == V3RoleCode.ECON)
      return "ECON";
    if (code == V3RoleCode.NOK)
      return "NOK";
    if (code == V3RoleCode._IDENTIFIEDENTITYTYPE)
      return "_IdentifiedEntityType";
    if (code == V3RoleCode._LOCATIONIDENTIFIEDENTITYROLECODE)
      return "_LocationIdentifiedEntityRoleCode";
    if (code == V3RoleCode.ACHFID)
      return "ACHFID";
    if (code == V3RoleCode.JURID)
      return "JURID";
    if (code == V3RoleCode.LOCHFID)
      return "LOCHFID";
    if (code == V3RoleCode._LIVINGSUBJECTPRODUCTIONCLASS)
      return "_LivingSubjectProductionClass";
    if (code == V3RoleCode.BF)
      return "BF";
    if (code == V3RoleCode.BL)
      return "BL";
    if (code == V3RoleCode.BR)
      return "BR";
    if (code == V3RoleCode.CO)
      return "CO";
    if (code == V3RoleCode.DA)
      return "DA";
    if (code == V3RoleCode.DR)
      return "DR";
    if (code == V3RoleCode.DU)
      return "DU";
    if (code == V3RoleCode.FI)
      return "FI";
    if (code == V3RoleCode.LY)
      return "LY";
    if (code == V3RoleCode.MT)
      return "MT";
    if (code == V3RoleCode.MU)
      return "MU";
    if (code == V3RoleCode.PL)
      return "PL";
    if (code == V3RoleCode.RC)
      return "RC";
    if (code == V3RoleCode.SH)
      return "SH";
    if (code == V3RoleCode.VL)
      return "VL";
    if (code == V3RoleCode.WL)
      return "WL";
    if (code == V3RoleCode.WO)
      return "WO";
    if (code == V3RoleCode._MEDICATIONGENERALIZATIONROLETYPE)
      return "_MedicationGeneralizationRoleType";
    if (code == V3RoleCode.DC)
      return "DC";
    if (code == V3RoleCode.GD)
      return "GD";
    if (code == V3RoleCode.GDF)
      return "GDF";
    if (code == V3RoleCode.GDS)
      return "GDS";
    if (code == V3RoleCode.GDSF)
      return "GDSF";
    if (code == V3RoleCode.MGDSF)
      return "MGDSF";
    if (code == V3RoleCode._MEMBERROLETYPE)
      return "_MemberRoleType";
    if (code == V3RoleCode.TRB)
      return "TRB";
    if (code == V3RoleCode._PERSONALRELATIONSHIPROLETYPE)
      return "_PersonalRelationshipRoleType";
    if (code == V3RoleCode.FAMMEMB)
      return "FAMMEMB";
    if (code == V3RoleCode.CHILD)
      return "CHILD";
    if (code == V3RoleCode.CHLDADOPT)
      return "CHLDADOPT";
    if (code == V3RoleCode.DAUADOPT)
      return "DAUADOPT";
    if (code == V3RoleCode.SONADOPT)
      return "SONADOPT";
    if (code == V3RoleCode.CHLDFOST)
      return "CHLDFOST";
    if (code == V3RoleCode.DAUFOST)
      return "DAUFOST";
    if (code == V3RoleCode.SONFOST)
      return "SONFOST";
    if (code == V3RoleCode.DAUC)
      return "DAUC";
    if (code == V3RoleCode.DAU)
      return "DAU";
    if (code == V3RoleCode.STPDAU)
      return "STPDAU";
    if (code == V3RoleCode.NCHILD)
      return "NCHILD";
    if (code == V3RoleCode.SON)
      return "SON";
    if (code == V3RoleCode.SONC)
      return "SONC";
    if (code == V3RoleCode.STPSON)
      return "STPSON";
    if (code == V3RoleCode.STPCHLD)
      return "STPCHLD";
    if (code == V3RoleCode.EXT)
      return "EXT";
    if (code == V3RoleCode.AUNT)
      return "AUNT";
    if (code == V3RoleCode.MAUNT)
      return "MAUNT";
    if (code == V3RoleCode.PAUNT)
      return "PAUNT";
    if (code == V3RoleCode.COUSN)
      return "COUSN";
    if (code == V3RoleCode.MCOUSN)
      return "MCOUSN";
    if (code == V3RoleCode.PCOUSN)
      return "PCOUSN";
    if (code == V3RoleCode.GGRPRN)
      return "GGRPRN";
    if (code == V3RoleCode.GGRFTH)
      return "GGRFTH";
    if (code == V3RoleCode.MGGRFTH)
      return "MGGRFTH";
    if (code == V3RoleCode.PGGRFTH)
      return "PGGRFTH";
    if (code == V3RoleCode.GGRMTH)
      return "GGRMTH";
    if (code == V3RoleCode.MGGRMTH)
      return "MGGRMTH";
    if (code == V3RoleCode.PGGRMTH)
      return "PGGRMTH";
    if (code == V3RoleCode.MGGRPRN)
      return "MGGRPRN";
    if (code == V3RoleCode.PGGRPRN)
      return "PGGRPRN";
    if (code == V3RoleCode.GRNDCHILD)
      return "GRNDCHILD";
    if (code == V3RoleCode.GRNDDAU)
      return "GRNDDAU";
    if (code == V3RoleCode.GRNDSON)
      return "GRNDSON";
    if (code == V3RoleCode.GRPRN)
      return "GRPRN";
    if (code == V3RoleCode.GRFTH)
      return "GRFTH";
    if (code == V3RoleCode.MGRFTH)
      return "MGRFTH";
    if (code == V3RoleCode.PGRFTH)
      return "PGRFTH";
    if (code == V3RoleCode.GRMTH)
      return "GRMTH";
    if (code == V3RoleCode.MGRMTH)
      return "MGRMTH";
    if (code == V3RoleCode.PGRMTH)
      return "PGRMTH";
    if (code == V3RoleCode.MGRPRN)
      return "MGRPRN";
    if (code == V3RoleCode.PGRPRN)
      return "PGRPRN";
    if (code == V3RoleCode.INLAW)
      return "INLAW";
    if (code == V3RoleCode.CHLDINLAW)
      return "CHLDINLAW";
    if (code == V3RoleCode.DAUINLAW)
      return "DAUINLAW";
    if (code == V3RoleCode.SONINLAW)
      return "SONINLAW";
    if (code == V3RoleCode.PRNINLAW)
      return "PRNINLAW";
    if (code == V3RoleCode.FTHINLAW)
      return "FTHINLAW";
    if (code == V3RoleCode.MTHINLAW)
      return "MTHINLAW";
    if (code == V3RoleCode.SIBINLAW)
      return "SIBINLAW";
    if (code == V3RoleCode.BROINLAW)
      return "BROINLAW";
    if (code == V3RoleCode.SISINLAW)
      return "SISINLAW";
    if (code == V3RoleCode.NIENEPH)
      return "NIENEPH";
    if (code == V3RoleCode.NEPHEW)
      return "NEPHEW";
    if (code == V3RoleCode.NIECE)
      return "NIECE";
    if (code == V3RoleCode.UNCLE)
      return "UNCLE";
    if (code == V3RoleCode.MUNCLE)
      return "MUNCLE";
    if (code == V3RoleCode.PUNCLE)
      return "PUNCLE";
    if (code == V3RoleCode.PRN)
      return "PRN";
    if (code == V3RoleCode.ADOPTP)
      return "ADOPTP";
    if (code == V3RoleCode.ADOPTF)
      return "ADOPTF";
    if (code == V3RoleCode.ADOPTM)
      return "ADOPTM";
    if (code == V3RoleCode.FTH)
      return "FTH";
    if (code == V3RoleCode.FTHFOST)
      return "FTHFOST";
    if (code == V3RoleCode.NFTH)
      return "NFTH";
    if (code == V3RoleCode.NFTHF)
      return "NFTHF";
    if (code == V3RoleCode.STPFTH)
      return "STPFTH";
    if (code == V3RoleCode.MTH)
      return "MTH";
    if (code == V3RoleCode.GESTM)
      return "GESTM";
    if (code == V3RoleCode.MTHFOST)
      return "MTHFOST";
    if (code == V3RoleCode.NMTH)
      return "NMTH";
    if (code == V3RoleCode.NMTHF)
      return "NMTHF";
    if (code == V3RoleCode.STPMTH)
      return "STPMTH";
    if (code == V3RoleCode.NPRN)
      return "NPRN";
    if (code == V3RoleCode.PRNFOST)
      return "PRNFOST";
    if (code == V3RoleCode.STPPRN)
      return "STPPRN";
    if (code == V3RoleCode.SIB)
      return "SIB";
    if (code == V3RoleCode.BRO)
      return "BRO";
    if (code == V3RoleCode.HBRO)
      return "HBRO";
    if (code == V3RoleCode.NBRO)
      return "NBRO";
    if (code == V3RoleCode.TWINBRO)
      return "TWINBRO";
    if (code == V3RoleCode.FTWINBRO)
      return "FTWINBRO";
    if (code == V3RoleCode.ITWINBRO)
      return "ITWINBRO";
    if (code == V3RoleCode.STPBRO)
      return "STPBRO";
    if (code == V3RoleCode.HSIB)
      return "HSIB";
    if (code == V3RoleCode.HSIS)
      return "HSIS";
    if (code == V3RoleCode.NSIB)
      return "NSIB";
    if (code == V3RoleCode.NSIS)
      return "NSIS";
    if (code == V3RoleCode.TWINSIS)
      return "TWINSIS";
    if (code == V3RoleCode.FTWINSIS)
      return "FTWINSIS";
    if (code == V3RoleCode.ITWINSIS)
      return "ITWINSIS";
    if (code == V3RoleCode.TWIN)
      return "TWIN";
    if (code == V3RoleCode.FTWIN)
      return "FTWIN";
    if (code == V3RoleCode.ITWIN)
      return "ITWIN";
    if (code == V3RoleCode.SIS)
      return "SIS";
    if (code == V3RoleCode.STPSIS)
      return "STPSIS";
    if (code == V3RoleCode.STPSIB)
      return "STPSIB";
    if (code == V3RoleCode.SIGOTHR)
      return "SIGOTHR";
    if (code == V3RoleCode.DOMPART)
      return "DOMPART";
    if (code == V3RoleCode.FMRSPS)
      return "FMRSPS";
    if (code == V3RoleCode.SPS)
      return "SPS";
    if (code == V3RoleCode.HUSB)
      return "HUSB";
    if (code == V3RoleCode.WIFE)
      return "WIFE";
    if (code == V3RoleCode.FRND)
      return "FRND";
    if (code == V3RoleCode.NBOR)
      return "NBOR";
    if (code == V3RoleCode.ONESELF)
      return "ONESELF";
    if (code == V3RoleCode.ROOM)
      return "ROOM";
    if (code == V3RoleCode._POLICYORPROGRAMCOVERAGEROLETYPE)
      return "_PolicyOrProgramCoverageRoleType";
    if (code == V3RoleCode._COVERAGEROLETYPE)
      return "_CoverageRoleType";
    if (code == V3RoleCode.FAMDEP)
      return "FAMDEP";
    if (code == V3RoleCode.HANDIC)
      return "HANDIC";
    if (code == V3RoleCode.INJ)
      return "INJ";
    if (code == V3RoleCode.SELF)
      return "SELF";
    if (code == V3RoleCode.SPON)
      return "SPON";
    if (code == V3RoleCode.STUD)
      return "STUD";
    if (code == V3RoleCode.FSTUD)
      return "FSTUD";
    if (code == V3RoleCode.PSTUD)
      return "PSTUD";
    if (code == V3RoleCode.ADOPT)
      return "ADOPT";
    if (code == V3RoleCode.GCHILD)
      return "GCHILD";
    if (code == V3RoleCode.GPARNT)
      return "GPARNT";
    if (code == V3RoleCode.NAT)
      return "NAT";
    if (code == V3RoleCode.NIENE)
      return "NIENE";
    if (code == V3RoleCode.PARNT)
      return "PARNT";
    if (code == V3RoleCode.SPSE)
      return "SPSE";
    if (code == V3RoleCode.STEP)
      return "STEP";
    if (code == V3RoleCode._COVEREDPARTYROLETYPE)
      return "_CoveredPartyRoleType";
    if (code == V3RoleCode._CLAIMANTCOVEREDPARTYROLETYPE)
      return "_ClaimantCoveredPartyRoleType";
    if (code == V3RoleCode.CRIMEVIC)
      return "CRIMEVIC";
    if (code == V3RoleCode.INJWKR)
      return "INJWKR";
    if (code == V3RoleCode._DEPENDENTCOVEREDPARTYROLETYPE)
      return "_DependentCoveredPartyRoleType";
    if (code == V3RoleCode.COCBEN)
      return "COCBEN";
    if (code == V3RoleCode.DIFFABL)
      return "DIFFABL";
    if (code == V3RoleCode.WARD)
      return "WARD";
    if (code == V3RoleCode._INDIVIDUALINSUREDPARTYROLETYPE)
      return "_IndividualInsuredPartyRoleType";
    if (code == V3RoleCode.RETIREE)
      return "RETIREE";
    if (code == V3RoleCode._PROGRAMELIGIBLEPARTYROLETYPE)
      return "_ProgramEligiblePartyRoleType";
    if (code == V3RoleCode.INDIG)
      return "INDIG";
    if (code == V3RoleCode.MIL)
      return "MIL";
    if (code == V3RoleCode.ACTMIL)
      return "ACTMIL";
    if (code == V3RoleCode.RETMIL)
      return "RETMIL";
    if (code == V3RoleCode.VET)
      return "VET";
    if (code == V3RoleCode._SUBSCRIBERCOVEREDPARTYROLETYPE)
      return "_SubscriberCoveredPartyRoleType";
    if (code == V3RoleCode._RESEARCHSUBJECTROLEBASIS)
      return "_ResearchSubjectRoleBasis";
    if (code == V3RoleCode.ERL)
      return "ERL";
    if (code == V3RoleCode.SCN)
      return "SCN";
    if (code == V3RoleCode._SERVICEDELIVERYLOCATIONROLETYPE)
      return "_ServiceDeliveryLocationRoleType";
    if (code == V3RoleCode._DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE)
      return "_DedicatedServiceDeliveryLocationRoleType";
    if (code == V3RoleCode._DEDICATEDCLINICALLOCATIONROLETYPE)
      return "_DedicatedClinicalLocationRoleType";
    if (code == V3RoleCode.DX)
      return "DX";
    if (code == V3RoleCode.CVDX)
      return "CVDX";
    if (code == V3RoleCode.CATH)
      return "CATH";
    if (code == V3RoleCode.ECHO)
      return "ECHO";
    if (code == V3RoleCode.GIDX)
      return "GIDX";
    if (code == V3RoleCode.ENDOS)
      return "ENDOS";
    if (code == V3RoleCode.RADDX)
      return "RADDX";
    if (code == V3RoleCode.RADO)
      return "RADO";
    if (code == V3RoleCode.RNEU)
      return "RNEU";
    if (code == V3RoleCode.HOSP)
      return "HOSP";
    if (code == V3RoleCode.CHR)
      return "CHR";
    if (code == V3RoleCode.GACH)
      return "GACH";
    if (code == V3RoleCode.MHSP)
      return "MHSP";
    if (code == V3RoleCode.PSYCHF)
      return "PSYCHF";
    if (code == V3RoleCode.RH)
      return "RH";
    if (code == V3RoleCode.RHAT)
      return "RHAT";
    if (code == V3RoleCode.RHII)
      return "RHII";
    if (code == V3RoleCode.RHMAD)
      return "RHMAD";
    if (code == V3RoleCode.RHPI)
      return "RHPI";
    if (code == V3RoleCode.RHPIH)
      return "RHPIH";
    if (code == V3RoleCode.RHPIMS)
      return "RHPIMS";
    if (code == V3RoleCode.RHPIVS)
      return "RHPIVS";
    if (code == V3RoleCode.RHYAD)
      return "RHYAD";
    if (code == V3RoleCode.HU)
      return "HU";
    if (code == V3RoleCode.BMTU)
      return "BMTU";
    if (code == V3RoleCode.CCU)
      return "CCU";
    if (code == V3RoleCode.CHEST)
      return "CHEST";
    if (code == V3RoleCode.EPIL)
      return "EPIL";
    if (code == V3RoleCode.ER)
      return "ER";
    if (code == V3RoleCode.ETU)
      return "ETU";
    if (code == V3RoleCode.HD)
      return "HD";
    if (code == V3RoleCode.HLAB)
      return "HLAB";
    if (code == V3RoleCode.INLAB)
      return "INLAB";
    if (code == V3RoleCode.OUTLAB)
      return "OUTLAB";
    if (code == V3RoleCode.HRAD)
      return "HRAD";
    if (code == V3RoleCode.HUSCS)
      return "HUSCS";
    if (code == V3RoleCode.ICU)
      return "ICU";
    if (code == V3RoleCode.PEDICU)
      return "PEDICU";
    if (code == V3RoleCode.PEDNICU)
      return "PEDNICU";
    if (code == V3RoleCode.INPHARM)
      return "INPHARM";
    if (code == V3RoleCode.MBL)
      return "MBL";
    if (code == V3RoleCode.NCCS)
      return "NCCS";
    if (code == V3RoleCode.NS)
      return "NS";
    if (code == V3RoleCode.OUTPHARM)
      return "OUTPHARM";
    if (code == V3RoleCode.PEDU)
      return "PEDU";
    if (code == V3RoleCode.PHU)
      return "PHU";
    if (code == V3RoleCode.RHU)
      return "RHU";
    if (code == V3RoleCode.SLEEP)
      return "SLEEP";
    if (code == V3RoleCode.NCCF)
      return "NCCF";
    if (code == V3RoleCode.SNF)
      return "SNF";
    if (code == V3RoleCode.OF)
      return "OF";
    if (code == V3RoleCode.ALL)
      return "ALL";
    if (code == V3RoleCode.AMPUT)
      return "AMPUT";
    if (code == V3RoleCode.BMTC)
      return "BMTC";
    if (code == V3RoleCode.BREAST)
      return "BREAST";
    if (code == V3RoleCode.CANC)
      return "CANC";
    if (code == V3RoleCode.CAPC)
      return "CAPC";
    if (code == V3RoleCode.CARD)
      return "CARD";
    if (code == V3RoleCode.PEDCARD)
      return "PEDCARD";
    if (code == V3RoleCode.COAG)
      return "COAG";
    if (code == V3RoleCode.CRS)
      return "CRS";
    if (code == V3RoleCode.DERM)
      return "DERM";
    if (code == V3RoleCode.ENDO)
      return "ENDO";
    if (code == V3RoleCode.PEDE)
      return "PEDE";
    if (code == V3RoleCode.ENT)
      return "ENT";
    if (code == V3RoleCode.FMC)
      return "FMC";
    if (code == V3RoleCode.GI)
      return "GI";
    if (code == V3RoleCode.PEDGI)
      return "PEDGI";
    if (code == V3RoleCode.GIM)
      return "GIM";
    if (code == V3RoleCode.GYN)
      return "GYN";
    if (code == V3RoleCode.HEM)
      return "HEM";
    if (code == V3RoleCode.PEDHEM)
      return "PEDHEM";
    if (code == V3RoleCode.HTN)
      return "HTN";
    if (code == V3RoleCode.IEC)
      return "IEC";
    if (code == V3RoleCode.INFD)
      return "INFD";
    if (code == V3RoleCode.PEDID)
      return "PEDID";
    if (code == V3RoleCode.INV)
      return "INV";
    if (code == V3RoleCode.LYMPH)
      return "LYMPH";
    if (code == V3RoleCode.MGEN)
      return "MGEN";
    if (code == V3RoleCode.NEPH)
      return "NEPH";
    if (code == V3RoleCode.PEDNEPH)
      return "PEDNEPH";
    if (code == V3RoleCode.NEUR)
      return "NEUR";
    if (code == V3RoleCode.OB)
      return "OB";
    if (code == V3RoleCode.OMS)
      return "OMS";
    if (code == V3RoleCode.ONCL)
      return "ONCL";
    if (code == V3RoleCode.PEDHO)
      return "PEDHO";
    if (code == V3RoleCode.OPH)
      return "OPH";
    if (code == V3RoleCode.OPTC)
      return "OPTC";
    if (code == V3RoleCode.ORTHO)
      return "ORTHO";
    if (code == V3RoleCode.HAND)
      return "HAND";
    if (code == V3RoleCode.PAINCL)
      return "PAINCL";
    if (code == V3RoleCode.PC)
      return "PC";
    if (code == V3RoleCode.PEDC)
      return "PEDC";
    if (code == V3RoleCode.PEDRHEUM)
      return "PEDRHEUM";
    if (code == V3RoleCode.POD)
      return "POD";
    if (code == V3RoleCode.PREV)
      return "PREV";
    if (code == V3RoleCode.PROCTO)
      return "PROCTO";
    if (code == V3RoleCode.PROFF)
      return "PROFF";
    if (code == V3RoleCode.PROS)
      return "PROS";
    if (code == V3RoleCode.PSI)
      return "PSI";
    if (code == V3RoleCode.PSY)
      return "PSY";
    if (code == V3RoleCode.RHEUM)
      return "RHEUM";
    if (code == V3RoleCode.SPMED)
      return "SPMED";
    if (code == V3RoleCode.SU)
      return "SU";
    if (code == V3RoleCode.PLS)
      return "PLS";
    if (code == V3RoleCode.URO)
      return "URO";
    if (code == V3RoleCode.TR)
      return "TR";
    if (code == V3RoleCode.TRAVEL)
      return "TRAVEL";
    if (code == V3RoleCode.WND)
      return "WND";
    if (code == V3RoleCode.RTF)
      return "RTF";
    if (code == V3RoleCode.PRC)
      return "PRC";
    if (code == V3RoleCode.SURF)
      return "SURF";
    if (code == V3RoleCode._DEDICATEDNONCLINICALLOCATIONROLETYPE)
      return "_DedicatedNonClinicalLocationRoleType";
    if (code == V3RoleCode.DADDR)
      return "DADDR";
    if (code == V3RoleCode.MOBL)
      return "MOBL";
    if (code == V3RoleCode.AMB)
      return "AMB";
    if (code == V3RoleCode.PHARM)
      return "PHARM";
    if (code == V3RoleCode._INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE)
      return "_IncidentalServiceDeliveryLocationRoleType";
    if (code == V3RoleCode.ACC)
      return "ACC";
    if (code == V3RoleCode.COMM)
      return "COMM";
    if (code == V3RoleCode.CSC)
      return "CSC";
    if (code == V3RoleCode.PTRES)
      return "PTRES";
    if (code == V3RoleCode.SCHOOL)
      return "SCHOOL";
    if (code == V3RoleCode.UPC)
      return "UPC";
    if (code == V3RoleCode.WORK)
      return "WORK";
    if (code == V3RoleCode._SPECIMENROLETYPE)
      return "_SpecimenRoleType";
    if (code == V3RoleCode.C)
      return "C";
    if (code == V3RoleCode.G)
      return "G";
    if (code == V3RoleCode.L)
      return "L";
    if (code == V3RoleCode.P)
      return "P";
    if (code == V3RoleCode.Q)
      return "Q";
    if (code == V3RoleCode.B)
      return "B";
    if (code == V3RoleCode.E)
      return "E";
    if (code == V3RoleCode.F)
      return "F";
    if (code == V3RoleCode.O)
      return "O";
    if (code == V3RoleCode.V)
      return "V";
    if (code == V3RoleCode.R)
      return "R";
    if (code == V3RoleCode.CLAIM)
      return "CLAIM";
    if (code == V3RoleCode.COMMUNITYLABORATORY)
      return "communityLaboratory";
    if (code == V3RoleCode.GT)
      return "GT";
    if (code == V3RoleCode.HOMEHEALTH)
      return "homeHealth";
    if (code == V3RoleCode.LABORATORY)
      return "laboratory";
    if (code == V3RoleCode.PATHOLOGIST)
      return "pathologist";
    if (code == V3RoleCode.PH)
      return "PH";
    if (code == V3RoleCode.PHLEBOTOMIST)
      return "phlebotomist";
    if (code == V3RoleCode.PROG)
      return "PROG";
    if (code == V3RoleCode.PT)
      return "PT";
    if (code == V3RoleCode.SUBJECT)
      return "subject";
    if (code == V3RoleCode.THIRDPARTY)
      return "thirdParty";
    if (code == V3RoleCode.DEP)
      return "DEP";
    if (code == V3RoleCode.DEPEN)
      return "DEPEN";
    if (code == V3RoleCode.FM)
      return "FM";
    if (code == V3RoleCode.INDIV)
      return "INDIV";
    if (code == V3RoleCode.NAMED)
      return "NAMED";
    if (code == V3RoleCode.PSYCHCF)
      return "PSYCHCF";
    if (code == V3RoleCode.SUBSCR)
      return "SUBSCR";
    return "?";
  }

    public String toSystem(V3RoleCode code) {
      return code.getSystem();
      }

}

