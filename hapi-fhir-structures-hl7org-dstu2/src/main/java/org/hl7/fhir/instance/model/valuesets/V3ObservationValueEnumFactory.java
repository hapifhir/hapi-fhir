package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class V3ObservationValueEnumFactory implements EnumFactory<V3ObservationValue> {

  public V3ObservationValue fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActCoverageAssessmentObservationValue".equals(codeString))
      return V3ObservationValue._ACTCOVERAGEASSESSMENTOBSERVATIONVALUE;
    if ("_ActFinancialStatusObservationValue".equals(codeString))
      return V3ObservationValue._ACTFINANCIALSTATUSOBSERVATIONVALUE;
    if ("ASSET".equals(codeString))
      return V3ObservationValue.ASSET;
    if ("ANNUITY".equals(codeString))
      return V3ObservationValue.ANNUITY;
    if ("PROP".equals(codeString))
      return V3ObservationValue.PROP;
    if ("RETACCT".equals(codeString))
      return V3ObservationValue.RETACCT;
    if ("TRUST".equals(codeString))
      return V3ObservationValue.TRUST;
    if ("INCOME".equals(codeString))
      return V3ObservationValue.INCOME;
    if ("CHILD".equals(codeString))
      return V3ObservationValue.CHILD;
    if ("DISABL".equals(codeString))
      return V3ObservationValue.DISABL;
    if ("INVEST".equals(codeString))
      return V3ObservationValue.INVEST;
    if ("PAY".equals(codeString))
      return V3ObservationValue.PAY;
    if ("RETIRE".equals(codeString))
      return V3ObservationValue.RETIRE;
    if ("SPOUSAL".equals(codeString))
      return V3ObservationValue.SPOUSAL;
    if ("SUPPLE".equals(codeString))
      return V3ObservationValue.SUPPLE;
    if ("TAX".equals(codeString))
      return V3ObservationValue.TAX;
    if ("LIVEXP".equals(codeString))
      return V3ObservationValue.LIVEXP;
    if ("CLOTH".equals(codeString))
      return V3ObservationValue.CLOTH;
    if ("FOOD".equals(codeString))
      return V3ObservationValue.FOOD;
    if ("HEALTH".equals(codeString))
      return V3ObservationValue.HEALTH;
    if ("HOUSE".equals(codeString))
      return V3ObservationValue.HOUSE;
    if ("LEGAL".equals(codeString))
      return V3ObservationValue.LEGAL;
    if ("MORTG".equals(codeString))
      return V3ObservationValue.MORTG;
    if ("RENT".equals(codeString))
      return V3ObservationValue.RENT;
    if ("SUNDRY".equals(codeString))
      return V3ObservationValue.SUNDRY;
    if ("TRANS".equals(codeString))
      return V3ObservationValue.TRANS;
    if ("UTIL".equals(codeString))
      return V3ObservationValue.UTIL;
    if ("ELSTAT".equals(codeString))
      return V3ObservationValue.ELSTAT;
    if ("ADOPT".equals(codeString))
      return V3ObservationValue.ADOPT;
    if ("BTHCERT".equals(codeString))
      return V3ObservationValue.BTHCERT;
    if ("CCOC".equals(codeString))
      return V3ObservationValue.CCOC;
    if ("DRLIC".equals(codeString))
      return V3ObservationValue.DRLIC;
    if ("FOSTER".equals(codeString))
      return V3ObservationValue.FOSTER;
    if ("MEMBER".equals(codeString))
      return V3ObservationValue.MEMBER;
    if ("MIL".equals(codeString))
      return V3ObservationValue.MIL;
    if ("MRGCERT".equals(codeString))
      return V3ObservationValue.MRGCERT;
    if ("PASSPORT".equals(codeString))
      return V3ObservationValue.PASSPORT;
    if ("STUDENRL".equals(codeString))
      return V3ObservationValue.STUDENRL;
    if ("HLSTAT".equals(codeString))
      return V3ObservationValue.HLSTAT;
    if ("DISABLE".equals(codeString))
      return V3ObservationValue.DISABLE;
    if ("DRUG".equals(codeString))
      return V3ObservationValue.DRUG;
    if ("IVDRG".equals(codeString))
      return V3ObservationValue.IVDRG;
    if ("PGNT".equals(codeString))
      return V3ObservationValue.PGNT;
    if ("LIVDEP".equals(codeString))
      return V3ObservationValue.LIVDEP;
    if ("RELDEP".equals(codeString))
      return V3ObservationValue.RELDEP;
    if ("SPSDEP".equals(codeString))
      return V3ObservationValue.SPSDEP;
    if ("URELDEP".equals(codeString))
      return V3ObservationValue.URELDEP;
    if ("LIVSIT".equals(codeString))
      return V3ObservationValue.LIVSIT;
    if ("ALONE".equals(codeString))
      return V3ObservationValue.ALONE;
    if ("DEPCHD".equals(codeString))
      return V3ObservationValue.DEPCHD;
    if ("DEPSPS".equals(codeString))
      return V3ObservationValue.DEPSPS;
    if ("DEPYGCHD".equals(codeString))
      return V3ObservationValue.DEPYGCHD;
    if ("FAM".equals(codeString))
      return V3ObservationValue.FAM;
    if ("RELAT".equals(codeString))
      return V3ObservationValue.RELAT;
    if ("SPS".equals(codeString))
      return V3ObservationValue.SPS;
    if ("UNREL".equals(codeString))
      return V3ObservationValue.UNREL;
    if ("SOECSTAT".equals(codeString))
      return V3ObservationValue.SOECSTAT;
    if ("ABUSE".equals(codeString))
      return V3ObservationValue.ABUSE;
    if ("HMLESS".equals(codeString))
      return V3ObservationValue.HMLESS;
    if ("ILGIM".equals(codeString))
      return V3ObservationValue.ILGIM;
    if ("INCAR".equals(codeString))
      return V3ObservationValue.INCAR;
    if ("PROB".equals(codeString))
      return V3ObservationValue.PROB;
    if ("REFUG".equals(codeString))
      return V3ObservationValue.REFUG;
    if ("UNEMPL".equals(codeString))
      return V3ObservationValue.UNEMPL;
    if ("_AllergyTestValue".equals(codeString))
      return V3ObservationValue._ALLERGYTESTVALUE;
    if ("A0".equals(codeString))
      return V3ObservationValue.A0;
    if ("A1".equals(codeString))
      return V3ObservationValue.A1;
    if ("A2".equals(codeString))
      return V3ObservationValue.A2;
    if ("A3".equals(codeString))
      return V3ObservationValue.A3;
    if ("A4".equals(codeString))
      return V3ObservationValue.A4;
    if ("_CoverageLimitObservationValue".equals(codeString))
      return V3ObservationValue._COVERAGELIMITOBSERVATIONVALUE;
    if ("_CoverageLevelObservationValue".equals(codeString))
      return V3ObservationValue._COVERAGELEVELOBSERVATIONVALUE;
    if ("ADC".equals(codeString))
      return V3ObservationValue.ADC;
    if ("CHD".equals(codeString))
      return V3ObservationValue.CHD;
    if ("DEP".equals(codeString))
      return V3ObservationValue.DEP;
    if ("DP".equals(codeString))
      return V3ObservationValue.DP;
    if ("ECH".equals(codeString))
      return V3ObservationValue.ECH;
    if ("FLY".equals(codeString))
      return V3ObservationValue.FLY;
    if ("IND".equals(codeString))
      return V3ObservationValue.IND;
    if ("SSP".equals(codeString))
      return V3ObservationValue.SSP;
    if ("_CriticalityObservationValue".equals(codeString))
      return V3ObservationValue._CRITICALITYOBSERVATIONVALUE;
    if ("CRITH".equals(codeString))
      return V3ObservationValue.CRITH;
    if ("CRITL".equals(codeString))
      return V3ObservationValue.CRITL;
    if ("CRITU".equals(codeString))
      return V3ObservationValue.CRITU;
    if ("_GeneticObservationValue".equals(codeString))
      return V3ObservationValue._GENETICOBSERVATIONVALUE;
    if ("Homozygote".equals(codeString))
      return V3ObservationValue.HOMOZYGOTE;
    if ("_ObservationMeasureScoring".equals(codeString))
      return V3ObservationValue._OBSERVATIONMEASURESCORING;
    if ("COHORT".equals(codeString))
      return V3ObservationValue.COHORT;
    if ("CONTVAR".equals(codeString))
      return V3ObservationValue.CONTVAR;
    if ("PROPOR".equals(codeString))
      return V3ObservationValue.PROPOR;
    if ("RATIO".equals(codeString))
      return V3ObservationValue.RATIO;
    if ("_ObservationMeasureType".equals(codeString))
      return V3ObservationValue._OBSERVATIONMEASURETYPE;
    if ("COMPOSITE".equals(codeString))
      return V3ObservationValue.COMPOSITE;
    if ("EFFICIENCY".equals(codeString))
      return V3ObservationValue.EFFICIENCY;
    if ("EXPERIENCE".equals(codeString))
      return V3ObservationValue.EXPERIENCE;
    if ("OUTCOME".equals(codeString))
      return V3ObservationValue.OUTCOME;
    if ("PROCESS".equals(codeString))
      return V3ObservationValue.PROCESS;
    if ("RESOURCE".equals(codeString))
      return V3ObservationValue.RESOURCE;
    if ("STRUCTURE".equals(codeString))
      return V3ObservationValue.STRUCTURE;
    if ("_ObservationPopulationInclusion".equals(codeString))
      return V3ObservationValue._OBSERVATIONPOPULATIONINCLUSION;
    if ("DENEX".equals(codeString))
      return V3ObservationValue.DENEX;
    if ("DENEXCEP".equals(codeString))
      return V3ObservationValue.DENEXCEP;
    if ("DENOM".equals(codeString))
      return V3ObservationValue.DENOM;
    if ("IP".equals(codeString))
      return V3ObservationValue.IP;
    if ("IPP".equals(codeString))
      return V3ObservationValue.IPP;
    if ("MSRPOPL".equals(codeString))
      return V3ObservationValue.MSRPOPL;
    if ("NUMER".equals(codeString))
      return V3ObservationValue.NUMER;
    if ("NUMEX".equals(codeString))
      return V3ObservationValue.NUMEX;
    if ("_PartialCompletionScale".equals(codeString))
      return V3ObservationValue._PARTIALCOMPLETIONSCALE;
    if ("G".equals(codeString))
      return V3ObservationValue.G;
    if ("LE".equals(codeString))
      return V3ObservationValue.LE;
    if ("ME".equals(codeString))
      return V3ObservationValue.ME;
    if ("MI".equals(codeString))
      return V3ObservationValue.MI;
    if ("N".equals(codeString))
      return V3ObservationValue.N;
    if ("S".equals(codeString))
      return V3ObservationValue.S;
    if ("_SecurityObservationValue".equals(codeString))
      return V3ObservationValue._SECURITYOBSERVATIONVALUE;
    if ("_SECINTOBV".equals(codeString))
      return V3ObservationValue._SECINTOBV;
    if ("_SECALTINTOBV".equals(codeString))
      return V3ObservationValue._SECALTINTOBV;
    if ("ABSTRED".equals(codeString))
      return V3ObservationValue.ABSTRED;
    if ("AGGRED".equals(codeString))
      return V3ObservationValue.AGGRED;
    if ("ANONYED".equals(codeString))
      return V3ObservationValue.ANONYED;
    if ("MAPPED".equals(codeString))
      return V3ObservationValue.MAPPED;
    if ("MASKED".equals(codeString))
      return V3ObservationValue.MASKED;
    if ("PSEUDED".equals(codeString))
      return V3ObservationValue.PSEUDED;
    if ("REDACTED".equals(codeString))
      return V3ObservationValue.REDACTED;
    if ("SUBSETTED".equals(codeString))
      return V3ObservationValue.SUBSETTED;
    if ("SYNTAC".equals(codeString))
      return V3ObservationValue.SYNTAC;
    if ("TRSLT".equals(codeString))
      return V3ObservationValue.TRSLT;
    if ("VERSIONED".equals(codeString))
      return V3ObservationValue.VERSIONED;
    if ("_SECDATINTOBV".equals(codeString))
      return V3ObservationValue._SECDATINTOBV;
    if ("CRYTOHASH".equals(codeString))
      return V3ObservationValue.CRYTOHASH;
    if ("DIGSIG".equals(codeString))
      return V3ObservationValue.DIGSIG;
    if ("_SECINTCONOBV".equals(codeString))
      return V3ObservationValue._SECINTCONOBV;
    if ("HRELIABLE".equals(codeString))
      return V3ObservationValue.HRELIABLE;
    if ("RELIABLE".equals(codeString))
      return V3ObservationValue.RELIABLE;
    if ("UNCERTREL".equals(codeString))
      return V3ObservationValue.UNCERTREL;
    if ("UNRELIABLE".equals(codeString))
      return V3ObservationValue.UNRELIABLE;
    if ("_SECINTPRVOBV".equals(codeString))
      return V3ObservationValue._SECINTPRVOBV;
    if ("_SECINTPRVABOBV".equals(codeString))
      return V3ObservationValue._SECINTPRVABOBV;
    if ("CLINAST".equals(codeString))
      return V3ObservationValue.CLINAST;
    if ("DEVAST".equals(codeString))
      return V3ObservationValue.DEVAST;
    if ("HCPAST".equals(codeString))
      return V3ObservationValue.HCPAST;
    if ("PACQAST".equals(codeString))
      return V3ObservationValue.PACQAST;
    if ("PATAST".equals(codeString))
      return V3ObservationValue.PATAST;
    if ("PAYAST".equals(codeString))
      return V3ObservationValue.PAYAST;
    if ("PROAST".equals(codeString))
      return V3ObservationValue.PROAST;
    if ("SDMAST".equals(codeString))
      return V3ObservationValue.SDMAST;
    if ("_SECINTPRVRBOBV".equals(codeString))
      return V3ObservationValue._SECINTPRVRBOBV;
    if ("CLINRPT".equals(codeString))
      return V3ObservationValue.CLINRPT;
    if ("DEVRPT".equals(codeString))
      return V3ObservationValue.DEVRPT;
    if ("HCPRPT".equals(codeString))
      return V3ObservationValue.HCPRPT;
    if ("PACQRPT".equals(codeString))
      return V3ObservationValue.PACQRPT;
    if ("PATRPT".equals(codeString))
      return V3ObservationValue.PATRPT;
    if ("PAYRPT".equals(codeString))
      return V3ObservationValue.PAYRPT;
    if ("PRORPT".equals(codeString))
      return V3ObservationValue.PRORPT;
    if ("SDMRPT".equals(codeString))
      return V3ObservationValue.SDMRPT;
    if ("SECTRSTOBV".equals(codeString))
      return V3ObservationValue.SECTRSTOBV;
    if ("TRSTACCRDOBV".equals(codeString))
      return V3ObservationValue.TRSTACCRDOBV;
    if ("TRSTAGREOBV".equals(codeString))
      return V3ObservationValue.TRSTAGREOBV;
    if ("TRSTCERTOBV".equals(codeString))
      return V3ObservationValue.TRSTCERTOBV;
    if ("TRSTLOAOBV".equals(codeString))
      return V3ObservationValue.TRSTLOAOBV;
    if ("LOAAN".equals(codeString))
      return V3ObservationValue.LOAAN;
    if ("LOAAN1".equals(codeString))
      return V3ObservationValue.LOAAN1;
    if ("LOAAN2".equals(codeString))
      return V3ObservationValue.LOAAN2;
    if ("LOAAN3".equals(codeString))
      return V3ObservationValue.LOAAN3;
    if ("LOAAN4".equals(codeString))
      return V3ObservationValue.LOAAN4;
    if ("LOAAP".equals(codeString))
      return V3ObservationValue.LOAAP;
    if ("LOAAP1".equals(codeString))
      return V3ObservationValue.LOAAP1;
    if ("LOAAP2".equals(codeString))
      return V3ObservationValue.LOAAP2;
    if ("LOAAP3".equals(codeString))
      return V3ObservationValue.LOAAP3;
    if ("LOAAP4".equals(codeString))
      return V3ObservationValue.LOAAP4;
    if ("LOAAS".equals(codeString))
      return V3ObservationValue.LOAAS;
    if ("LOAAS1".equals(codeString))
      return V3ObservationValue.LOAAS1;
    if ("LOAAS2".equals(codeString))
      return V3ObservationValue.LOAAS2;
    if ("LOAAS3".equals(codeString))
      return V3ObservationValue.LOAAS3;
    if ("LOAAS4".equals(codeString))
      return V3ObservationValue.LOAAS4;
    if ("LOACM".equals(codeString))
      return V3ObservationValue.LOACM;
    if ("LOACM1".equals(codeString))
      return V3ObservationValue.LOACM1;
    if ("LOACM2".equals(codeString))
      return V3ObservationValue.LOACM2;
    if ("LOACM3".equals(codeString))
      return V3ObservationValue.LOACM3;
    if ("LOACM4".equals(codeString))
      return V3ObservationValue.LOACM4;
    if ("LOAID".equals(codeString))
      return V3ObservationValue.LOAID;
    if ("LOAID1".equals(codeString))
      return V3ObservationValue.LOAID1;
    if ("LOAID2".equals(codeString))
      return V3ObservationValue.LOAID2;
    if ("LOAID3".equals(codeString))
      return V3ObservationValue.LOAID3;
    if ("LOAID4".equals(codeString))
      return V3ObservationValue.LOAID4;
    if ("LOANR".equals(codeString))
      return V3ObservationValue.LOANR;
    if ("LOANR1".equals(codeString))
      return V3ObservationValue.LOANR1;
    if ("LOANR2".equals(codeString))
      return V3ObservationValue.LOANR2;
    if ("LOANR3".equals(codeString))
      return V3ObservationValue.LOANR3;
    if ("LOANR4".equals(codeString))
      return V3ObservationValue.LOANR4;
    if ("LOARA".equals(codeString))
      return V3ObservationValue.LOARA;
    if ("LOARA1".equals(codeString))
      return V3ObservationValue.LOARA1;
    if ("LOARA2".equals(codeString))
      return V3ObservationValue.LOARA2;
    if ("LOARA3".equals(codeString))
      return V3ObservationValue.LOARA3;
    if ("LOARA4".equals(codeString))
      return V3ObservationValue.LOARA4;
    if ("LOATK".equals(codeString))
      return V3ObservationValue.LOATK;
    if ("LOATK1".equals(codeString))
      return V3ObservationValue.LOATK1;
    if ("LOATK2".equals(codeString))
      return V3ObservationValue.LOATK2;
    if ("LOATK3".equals(codeString))
      return V3ObservationValue.LOATK3;
    if ("LOATK4".equals(codeString))
      return V3ObservationValue.LOATK4;
    if ("TRSTMECOBV".equals(codeString))
      return V3ObservationValue.TRSTMECOBV;
    if ("_SeverityObservation".equals(codeString))
      return V3ObservationValue._SEVERITYOBSERVATION;
    if ("H".equals(codeString))
      return V3ObservationValue.H;
    if ("L".equals(codeString))
      return V3ObservationValue.L;
    if ("M".equals(codeString))
      return V3ObservationValue.M;
    if ("_SubjectBodyPosition".equals(codeString))
      return V3ObservationValue._SUBJECTBODYPOSITION;
    if ("LLD".equals(codeString))
      return V3ObservationValue.LLD;
    if ("PRN".equals(codeString))
      return V3ObservationValue.PRN;
    if ("RLD".equals(codeString))
      return V3ObservationValue.RLD;
    if ("SFWL".equals(codeString))
      return V3ObservationValue.SFWL;
    if ("SIT".equals(codeString))
      return V3ObservationValue.SIT;
    if ("STN".equals(codeString))
      return V3ObservationValue.STN;
    if ("SUP".equals(codeString))
      return V3ObservationValue.SUP;
    if ("RTRD".equals(codeString))
      return V3ObservationValue.RTRD;
    if ("TRD".equals(codeString))
      return V3ObservationValue.TRD;
    if ("_VerificationOutcomeValue".equals(codeString))
      return V3ObservationValue._VERIFICATIONOUTCOMEVALUE;
    if ("ACT".equals(codeString))
      return V3ObservationValue.ACT;
    if ("ACTPEND".equals(codeString))
      return V3ObservationValue.ACTPEND;
    if ("ELG".equals(codeString))
      return V3ObservationValue.ELG;
    if ("INACT".equals(codeString))
      return V3ObservationValue.INACT;
    if ("INPNDINV".equals(codeString))
      return V3ObservationValue.INPNDINV;
    if ("INPNDUPD".equals(codeString))
      return V3ObservationValue.INPNDUPD;
    if ("NELG".equals(codeString))
      return V3ObservationValue.NELG;
    if ("_AnnotationValue".equals(codeString))
      return V3ObservationValue._ANNOTATIONVALUE;
    if ("_CommonClinicalObservationValue".equals(codeString))
      return V3ObservationValue._COMMONCLINICALOBSERVATIONVALUE;
    if ("_IndividualCaseSafetyReportValueDomains".equals(codeString))
      return V3ObservationValue._INDIVIDUALCASESAFETYREPORTVALUEDOMAINS;
    if ("_IndicationValue".equals(codeString))
      return V3ObservationValue._INDICATIONVALUE;
    throw new IllegalArgumentException("Unknown V3ObservationValue code '"+codeString+"'");
  }

  public String toCode(V3ObservationValue code) {
    if (code == V3ObservationValue._ACTCOVERAGEASSESSMENTOBSERVATIONVALUE)
      return "_ActCoverageAssessmentObservationValue";
    if (code == V3ObservationValue._ACTFINANCIALSTATUSOBSERVATIONVALUE)
      return "_ActFinancialStatusObservationValue";
    if (code == V3ObservationValue.ASSET)
      return "ASSET";
    if (code == V3ObservationValue.ANNUITY)
      return "ANNUITY";
    if (code == V3ObservationValue.PROP)
      return "PROP";
    if (code == V3ObservationValue.RETACCT)
      return "RETACCT";
    if (code == V3ObservationValue.TRUST)
      return "TRUST";
    if (code == V3ObservationValue.INCOME)
      return "INCOME";
    if (code == V3ObservationValue.CHILD)
      return "CHILD";
    if (code == V3ObservationValue.DISABL)
      return "DISABL";
    if (code == V3ObservationValue.INVEST)
      return "INVEST";
    if (code == V3ObservationValue.PAY)
      return "PAY";
    if (code == V3ObservationValue.RETIRE)
      return "RETIRE";
    if (code == V3ObservationValue.SPOUSAL)
      return "SPOUSAL";
    if (code == V3ObservationValue.SUPPLE)
      return "SUPPLE";
    if (code == V3ObservationValue.TAX)
      return "TAX";
    if (code == V3ObservationValue.LIVEXP)
      return "LIVEXP";
    if (code == V3ObservationValue.CLOTH)
      return "CLOTH";
    if (code == V3ObservationValue.FOOD)
      return "FOOD";
    if (code == V3ObservationValue.HEALTH)
      return "HEALTH";
    if (code == V3ObservationValue.HOUSE)
      return "HOUSE";
    if (code == V3ObservationValue.LEGAL)
      return "LEGAL";
    if (code == V3ObservationValue.MORTG)
      return "MORTG";
    if (code == V3ObservationValue.RENT)
      return "RENT";
    if (code == V3ObservationValue.SUNDRY)
      return "SUNDRY";
    if (code == V3ObservationValue.TRANS)
      return "TRANS";
    if (code == V3ObservationValue.UTIL)
      return "UTIL";
    if (code == V3ObservationValue.ELSTAT)
      return "ELSTAT";
    if (code == V3ObservationValue.ADOPT)
      return "ADOPT";
    if (code == V3ObservationValue.BTHCERT)
      return "BTHCERT";
    if (code == V3ObservationValue.CCOC)
      return "CCOC";
    if (code == V3ObservationValue.DRLIC)
      return "DRLIC";
    if (code == V3ObservationValue.FOSTER)
      return "FOSTER";
    if (code == V3ObservationValue.MEMBER)
      return "MEMBER";
    if (code == V3ObservationValue.MIL)
      return "MIL";
    if (code == V3ObservationValue.MRGCERT)
      return "MRGCERT";
    if (code == V3ObservationValue.PASSPORT)
      return "PASSPORT";
    if (code == V3ObservationValue.STUDENRL)
      return "STUDENRL";
    if (code == V3ObservationValue.HLSTAT)
      return "HLSTAT";
    if (code == V3ObservationValue.DISABLE)
      return "DISABLE";
    if (code == V3ObservationValue.DRUG)
      return "DRUG";
    if (code == V3ObservationValue.IVDRG)
      return "IVDRG";
    if (code == V3ObservationValue.PGNT)
      return "PGNT";
    if (code == V3ObservationValue.LIVDEP)
      return "LIVDEP";
    if (code == V3ObservationValue.RELDEP)
      return "RELDEP";
    if (code == V3ObservationValue.SPSDEP)
      return "SPSDEP";
    if (code == V3ObservationValue.URELDEP)
      return "URELDEP";
    if (code == V3ObservationValue.LIVSIT)
      return "LIVSIT";
    if (code == V3ObservationValue.ALONE)
      return "ALONE";
    if (code == V3ObservationValue.DEPCHD)
      return "DEPCHD";
    if (code == V3ObservationValue.DEPSPS)
      return "DEPSPS";
    if (code == V3ObservationValue.DEPYGCHD)
      return "DEPYGCHD";
    if (code == V3ObservationValue.FAM)
      return "FAM";
    if (code == V3ObservationValue.RELAT)
      return "RELAT";
    if (code == V3ObservationValue.SPS)
      return "SPS";
    if (code == V3ObservationValue.UNREL)
      return "UNREL";
    if (code == V3ObservationValue.SOECSTAT)
      return "SOECSTAT";
    if (code == V3ObservationValue.ABUSE)
      return "ABUSE";
    if (code == V3ObservationValue.HMLESS)
      return "HMLESS";
    if (code == V3ObservationValue.ILGIM)
      return "ILGIM";
    if (code == V3ObservationValue.INCAR)
      return "INCAR";
    if (code == V3ObservationValue.PROB)
      return "PROB";
    if (code == V3ObservationValue.REFUG)
      return "REFUG";
    if (code == V3ObservationValue.UNEMPL)
      return "UNEMPL";
    if (code == V3ObservationValue._ALLERGYTESTVALUE)
      return "_AllergyTestValue";
    if (code == V3ObservationValue.A0)
      return "A0";
    if (code == V3ObservationValue.A1)
      return "A1";
    if (code == V3ObservationValue.A2)
      return "A2";
    if (code == V3ObservationValue.A3)
      return "A3";
    if (code == V3ObservationValue.A4)
      return "A4";
    if (code == V3ObservationValue._COVERAGELIMITOBSERVATIONVALUE)
      return "_CoverageLimitObservationValue";
    if (code == V3ObservationValue._COVERAGELEVELOBSERVATIONVALUE)
      return "_CoverageLevelObservationValue";
    if (code == V3ObservationValue.ADC)
      return "ADC";
    if (code == V3ObservationValue.CHD)
      return "CHD";
    if (code == V3ObservationValue.DEP)
      return "DEP";
    if (code == V3ObservationValue.DP)
      return "DP";
    if (code == V3ObservationValue.ECH)
      return "ECH";
    if (code == V3ObservationValue.FLY)
      return "FLY";
    if (code == V3ObservationValue.IND)
      return "IND";
    if (code == V3ObservationValue.SSP)
      return "SSP";
    if (code == V3ObservationValue._CRITICALITYOBSERVATIONVALUE)
      return "_CriticalityObservationValue";
    if (code == V3ObservationValue.CRITH)
      return "CRITH";
    if (code == V3ObservationValue.CRITL)
      return "CRITL";
    if (code == V3ObservationValue.CRITU)
      return "CRITU";
    if (code == V3ObservationValue._GENETICOBSERVATIONVALUE)
      return "_GeneticObservationValue";
    if (code == V3ObservationValue.HOMOZYGOTE)
      return "Homozygote";
    if (code == V3ObservationValue._OBSERVATIONMEASURESCORING)
      return "_ObservationMeasureScoring";
    if (code == V3ObservationValue.COHORT)
      return "COHORT";
    if (code == V3ObservationValue.CONTVAR)
      return "CONTVAR";
    if (code == V3ObservationValue.PROPOR)
      return "PROPOR";
    if (code == V3ObservationValue.RATIO)
      return "RATIO";
    if (code == V3ObservationValue._OBSERVATIONMEASURETYPE)
      return "_ObservationMeasureType";
    if (code == V3ObservationValue.COMPOSITE)
      return "COMPOSITE";
    if (code == V3ObservationValue.EFFICIENCY)
      return "EFFICIENCY";
    if (code == V3ObservationValue.EXPERIENCE)
      return "EXPERIENCE";
    if (code == V3ObservationValue.OUTCOME)
      return "OUTCOME";
    if (code == V3ObservationValue.PROCESS)
      return "PROCESS";
    if (code == V3ObservationValue.RESOURCE)
      return "RESOURCE";
    if (code == V3ObservationValue.STRUCTURE)
      return "STRUCTURE";
    if (code == V3ObservationValue._OBSERVATIONPOPULATIONINCLUSION)
      return "_ObservationPopulationInclusion";
    if (code == V3ObservationValue.DENEX)
      return "DENEX";
    if (code == V3ObservationValue.DENEXCEP)
      return "DENEXCEP";
    if (code == V3ObservationValue.DENOM)
      return "DENOM";
    if (code == V3ObservationValue.IP)
      return "IP";
    if (code == V3ObservationValue.IPP)
      return "IPP";
    if (code == V3ObservationValue.MSRPOPL)
      return "MSRPOPL";
    if (code == V3ObservationValue.NUMER)
      return "NUMER";
    if (code == V3ObservationValue.NUMEX)
      return "NUMEX";
    if (code == V3ObservationValue._PARTIALCOMPLETIONSCALE)
      return "_PartialCompletionScale";
    if (code == V3ObservationValue.G)
      return "G";
    if (code == V3ObservationValue.LE)
      return "LE";
    if (code == V3ObservationValue.ME)
      return "ME";
    if (code == V3ObservationValue.MI)
      return "MI";
    if (code == V3ObservationValue.N)
      return "N";
    if (code == V3ObservationValue.S)
      return "S";
    if (code == V3ObservationValue._SECURITYOBSERVATIONVALUE)
      return "_SecurityObservationValue";
    if (code == V3ObservationValue._SECINTOBV)
      return "_SECINTOBV";
    if (code == V3ObservationValue._SECALTINTOBV)
      return "_SECALTINTOBV";
    if (code == V3ObservationValue.ABSTRED)
      return "ABSTRED";
    if (code == V3ObservationValue.AGGRED)
      return "AGGRED";
    if (code == V3ObservationValue.ANONYED)
      return "ANONYED";
    if (code == V3ObservationValue.MAPPED)
      return "MAPPED";
    if (code == V3ObservationValue.MASKED)
      return "MASKED";
    if (code == V3ObservationValue.PSEUDED)
      return "PSEUDED";
    if (code == V3ObservationValue.REDACTED)
      return "REDACTED";
    if (code == V3ObservationValue.SUBSETTED)
      return "SUBSETTED";
    if (code == V3ObservationValue.SYNTAC)
      return "SYNTAC";
    if (code == V3ObservationValue.TRSLT)
      return "TRSLT";
    if (code == V3ObservationValue.VERSIONED)
      return "VERSIONED";
    if (code == V3ObservationValue._SECDATINTOBV)
      return "_SECDATINTOBV";
    if (code == V3ObservationValue.CRYTOHASH)
      return "CRYTOHASH";
    if (code == V3ObservationValue.DIGSIG)
      return "DIGSIG";
    if (code == V3ObservationValue._SECINTCONOBV)
      return "_SECINTCONOBV";
    if (code == V3ObservationValue.HRELIABLE)
      return "HRELIABLE";
    if (code == V3ObservationValue.RELIABLE)
      return "RELIABLE";
    if (code == V3ObservationValue.UNCERTREL)
      return "UNCERTREL";
    if (code == V3ObservationValue.UNRELIABLE)
      return "UNRELIABLE";
    if (code == V3ObservationValue._SECINTPRVOBV)
      return "_SECINTPRVOBV";
    if (code == V3ObservationValue._SECINTPRVABOBV)
      return "_SECINTPRVABOBV";
    if (code == V3ObservationValue.CLINAST)
      return "CLINAST";
    if (code == V3ObservationValue.DEVAST)
      return "DEVAST";
    if (code == V3ObservationValue.HCPAST)
      return "HCPAST";
    if (code == V3ObservationValue.PACQAST)
      return "PACQAST";
    if (code == V3ObservationValue.PATAST)
      return "PATAST";
    if (code == V3ObservationValue.PAYAST)
      return "PAYAST";
    if (code == V3ObservationValue.PROAST)
      return "PROAST";
    if (code == V3ObservationValue.SDMAST)
      return "SDMAST";
    if (code == V3ObservationValue._SECINTPRVRBOBV)
      return "_SECINTPRVRBOBV";
    if (code == V3ObservationValue.CLINRPT)
      return "CLINRPT";
    if (code == V3ObservationValue.DEVRPT)
      return "DEVRPT";
    if (code == V3ObservationValue.HCPRPT)
      return "HCPRPT";
    if (code == V3ObservationValue.PACQRPT)
      return "PACQRPT";
    if (code == V3ObservationValue.PATRPT)
      return "PATRPT";
    if (code == V3ObservationValue.PAYRPT)
      return "PAYRPT";
    if (code == V3ObservationValue.PRORPT)
      return "PRORPT";
    if (code == V3ObservationValue.SDMRPT)
      return "SDMRPT";
    if (code == V3ObservationValue.SECTRSTOBV)
      return "SECTRSTOBV";
    if (code == V3ObservationValue.TRSTACCRDOBV)
      return "TRSTACCRDOBV";
    if (code == V3ObservationValue.TRSTAGREOBV)
      return "TRSTAGREOBV";
    if (code == V3ObservationValue.TRSTCERTOBV)
      return "TRSTCERTOBV";
    if (code == V3ObservationValue.TRSTLOAOBV)
      return "TRSTLOAOBV";
    if (code == V3ObservationValue.LOAAN)
      return "LOAAN";
    if (code == V3ObservationValue.LOAAN1)
      return "LOAAN1";
    if (code == V3ObservationValue.LOAAN2)
      return "LOAAN2";
    if (code == V3ObservationValue.LOAAN3)
      return "LOAAN3";
    if (code == V3ObservationValue.LOAAN4)
      return "LOAAN4";
    if (code == V3ObservationValue.LOAAP)
      return "LOAAP";
    if (code == V3ObservationValue.LOAAP1)
      return "LOAAP1";
    if (code == V3ObservationValue.LOAAP2)
      return "LOAAP2";
    if (code == V3ObservationValue.LOAAP3)
      return "LOAAP3";
    if (code == V3ObservationValue.LOAAP4)
      return "LOAAP4";
    if (code == V3ObservationValue.LOAAS)
      return "LOAAS";
    if (code == V3ObservationValue.LOAAS1)
      return "LOAAS1";
    if (code == V3ObservationValue.LOAAS2)
      return "LOAAS2";
    if (code == V3ObservationValue.LOAAS3)
      return "LOAAS3";
    if (code == V3ObservationValue.LOAAS4)
      return "LOAAS4";
    if (code == V3ObservationValue.LOACM)
      return "LOACM";
    if (code == V3ObservationValue.LOACM1)
      return "LOACM1";
    if (code == V3ObservationValue.LOACM2)
      return "LOACM2";
    if (code == V3ObservationValue.LOACM3)
      return "LOACM3";
    if (code == V3ObservationValue.LOACM4)
      return "LOACM4";
    if (code == V3ObservationValue.LOAID)
      return "LOAID";
    if (code == V3ObservationValue.LOAID1)
      return "LOAID1";
    if (code == V3ObservationValue.LOAID2)
      return "LOAID2";
    if (code == V3ObservationValue.LOAID3)
      return "LOAID3";
    if (code == V3ObservationValue.LOAID4)
      return "LOAID4";
    if (code == V3ObservationValue.LOANR)
      return "LOANR";
    if (code == V3ObservationValue.LOANR1)
      return "LOANR1";
    if (code == V3ObservationValue.LOANR2)
      return "LOANR2";
    if (code == V3ObservationValue.LOANR3)
      return "LOANR3";
    if (code == V3ObservationValue.LOANR4)
      return "LOANR4";
    if (code == V3ObservationValue.LOARA)
      return "LOARA";
    if (code == V3ObservationValue.LOARA1)
      return "LOARA1";
    if (code == V3ObservationValue.LOARA2)
      return "LOARA2";
    if (code == V3ObservationValue.LOARA3)
      return "LOARA3";
    if (code == V3ObservationValue.LOARA4)
      return "LOARA4";
    if (code == V3ObservationValue.LOATK)
      return "LOATK";
    if (code == V3ObservationValue.LOATK1)
      return "LOATK1";
    if (code == V3ObservationValue.LOATK2)
      return "LOATK2";
    if (code == V3ObservationValue.LOATK3)
      return "LOATK3";
    if (code == V3ObservationValue.LOATK4)
      return "LOATK4";
    if (code == V3ObservationValue.TRSTMECOBV)
      return "TRSTMECOBV";
    if (code == V3ObservationValue._SEVERITYOBSERVATION)
      return "_SeverityObservation";
    if (code == V3ObservationValue.H)
      return "H";
    if (code == V3ObservationValue.L)
      return "L";
    if (code == V3ObservationValue.M)
      return "M";
    if (code == V3ObservationValue._SUBJECTBODYPOSITION)
      return "_SubjectBodyPosition";
    if (code == V3ObservationValue.LLD)
      return "LLD";
    if (code == V3ObservationValue.PRN)
      return "PRN";
    if (code == V3ObservationValue.RLD)
      return "RLD";
    if (code == V3ObservationValue.SFWL)
      return "SFWL";
    if (code == V3ObservationValue.SIT)
      return "SIT";
    if (code == V3ObservationValue.STN)
      return "STN";
    if (code == V3ObservationValue.SUP)
      return "SUP";
    if (code == V3ObservationValue.RTRD)
      return "RTRD";
    if (code == V3ObservationValue.TRD)
      return "TRD";
    if (code == V3ObservationValue._VERIFICATIONOUTCOMEVALUE)
      return "_VerificationOutcomeValue";
    if (code == V3ObservationValue.ACT)
      return "ACT";
    if (code == V3ObservationValue.ACTPEND)
      return "ACTPEND";
    if (code == V3ObservationValue.ELG)
      return "ELG";
    if (code == V3ObservationValue.INACT)
      return "INACT";
    if (code == V3ObservationValue.INPNDINV)
      return "INPNDINV";
    if (code == V3ObservationValue.INPNDUPD)
      return "INPNDUPD";
    if (code == V3ObservationValue.NELG)
      return "NELG";
    if (code == V3ObservationValue._ANNOTATIONVALUE)
      return "_AnnotationValue";
    if (code == V3ObservationValue._COMMONCLINICALOBSERVATIONVALUE)
      return "_CommonClinicalObservationValue";
    if (code == V3ObservationValue._INDIVIDUALCASESAFETYREPORTVALUEDOMAINS)
      return "_IndividualCaseSafetyReportValueDomains";
    if (code == V3ObservationValue._INDICATIONVALUE)
      return "_IndicationValue";
    return "?";
  }


}

