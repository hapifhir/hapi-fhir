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

public class V3ActRelationshipTypeEnumFactory implements EnumFactory<V3ActRelationshipType> {

  public V3ActRelationshipType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ART".equals(codeString))
      return V3ActRelationshipType.ART;
    if ("_ActClassTemporallyPertains".equals(codeString))
      return V3ActRelationshipType._ACTCLASSTEMPORALLYPERTAINS;
    if ("_ActRelationshipAccounting".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSHIPACCOUNTING;
    if ("_ActRelationshipCostTracking".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSHIPCOSTTRACKING;
    if ("CHRG".equals(codeString))
      return V3ActRelationshipType.CHRG;
    if ("COST".equals(codeString))
      return V3ActRelationshipType.COST;
    if ("_ActRelationshipPosting".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSHIPPOSTING;
    if ("CREDIT".equals(codeString))
      return V3ActRelationshipType.CREDIT;
    if ("DEBIT".equals(codeString))
      return V3ActRelationshipType.DEBIT;
    if ("_ActRelationshipConditional".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSHIPCONDITIONAL;
    if ("CIND".equals(codeString))
      return V3ActRelationshipType.CIND;
    if ("PRCN".equals(codeString))
      return V3ActRelationshipType.PRCN;
    if ("RSON".equals(codeString))
      return V3ActRelationshipType.RSON;
    if ("BLOCK".equals(codeString))
      return V3ActRelationshipType.BLOCK;
    if ("DIAG".equals(codeString))
      return V3ActRelationshipType.DIAG;
    if ("IMM".equals(codeString))
      return V3ActRelationshipType.IMM;
    if ("ACTIMM".equals(codeString))
      return V3ActRelationshipType.ACTIMM;
    if ("PASSIMM".equals(codeString))
      return V3ActRelationshipType.PASSIMM;
    if ("MITGT".equals(codeString))
      return V3ActRelationshipType.MITGT;
    if ("RCVY".equals(codeString))
      return V3ActRelationshipType.RCVY;
    if ("PRYLX".equals(codeString))
      return V3ActRelationshipType.PRYLX;
    if ("TREAT".equals(codeString))
      return V3ActRelationshipType.TREAT;
    if ("ADJUNCT".equals(codeString))
      return V3ActRelationshipType.ADJUNCT;
    if ("MTREAT".equals(codeString))
      return V3ActRelationshipType.MTREAT;
    if ("PALLTREAT".equals(codeString))
      return V3ActRelationshipType.PALLTREAT;
    if ("SYMP".equals(codeString))
      return V3ActRelationshipType.SYMP;
    if ("TRIG".equals(codeString))
      return V3ActRelationshipType.TRIG;
    if ("_ActRelationshipTemporallyPertains".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSHIPTEMPORALLYPERTAINS;
    if ("_ActRelationshipTemporallyPertainsApproximates".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES;
    if ("ENE".equals(codeString))
      return V3ActRelationshipType.ENE;
    if ("ECW".equals(codeString))
      return V3ActRelationshipType.ECW;
    if ("CONCURRENT".equals(codeString))
      return V3ActRelationshipType.CONCURRENT;
    if ("SBSECWE".equals(codeString))
      return V3ActRelationshipType.SBSECWE;
    if ("ENS".equals(codeString))
      return V3ActRelationshipType.ENS;
    if ("ECWS".equals(codeString))
      return V3ActRelationshipType.ECWS;
    if ("SNE".equals(codeString))
      return V3ActRelationshipType.SNE;
    if ("SCWE".equals(codeString))
      return V3ActRelationshipType.SCWE;
    if ("SNS".equals(codeString))
      return V3ActRelationshipType.SNS;
    if ("SCW".equals(codeString))
      return V3ActRelationshipType.SCW;
    if ("SCWSEBE".equals(codeString))
      return V3ActRelationshipType.SCWSEBE;
    if ("SCWSEAE".equals(codeString))
      return V3ActRelationshipType.SCWSEAE;
    if ("EAS".equals(codeString))
      return V3ActRelationshipType.EAS;
    if ("EAE".equals(codeString))
      return V3ActRelationshipType.EAE;
    if ("SASEAE".equals(codeString))
      return V3ActRelationshipType.SASEAE;
    if ("SBEEAE".equals(codeString))
      return V3ActRelationshipType.SBEEAE;
    if ("SASSBEEAS".equals(codeString))
      return V3ActRelationshipType.SASSBEEAS;
    if ("SBSEAE".equals(codeString))
      return V3ActRelationshipType.SBSEAE;
    if ("SAS".equals(codeString))
      return V3ActRelationshipType.SAS;
    if ("SAE".equals(codeString))
      return V3ActRelationshipType.SAE;
    if ("DURING".equals(codeString))
      return V3ActRelationshipType.DURING;
    if ("SASECWE".equals(codeString))
      return V3ActRelationshipType.SASECWE;
    if ("EASORECWS".equals(codeString))
      return V3ActRelationshipType.EASORECWS;
    if ("EAEORECW".equals(codeString))
      return V3ActRelationshipType.EAEORECW;
    if ("INDEPENDENT".equals(codeString))
      return V3ActRelationshipType.INDEPENDENT;
    if ("SAEORSCWE".equals(codeString))
      return V3ActRelationshipType.SAEORSCWE;
    if ("SASORSCW".equals(codeString))
      return V3ActRelationshipType.SASORSCW;
    if ("SBEORSCWE".equals(codeString))
      return V3ActRelationshipType.SBEORSCWE;
    if ("OVERLAP".equals(codeString))
      return V3ActRelationshipType.OVERLAP;
    if ("EDU".equals(codeString))
      return V3ActRelationshipType.EDU;
    if ("SBSEASEBE".equals(codeString))
      return V3ActRelationshipType.SBSEASEBE;
    if ("SBSEAS".equals(codeString))
      return V3ActRelationshipType.SBSEAS;
    if ("SDU".equals(codeString))
      return V3ActRelationshipType.SDU;
    if ("SBE".equals(codeString))
      return V3ActRelationshipType.SBE;
    if ("EBE".equals(codeString))
      return V3ActRelationshipType.EBE;
    if ("SBSEBE".equals(codeString))
      return V3ActRelationshipType.SBSEBE;
    if ("EBSORECWS".equals(codeString))
      return V3ActRelationshipType.EBSORECWS;
    if ("EBS".equals(codeString))
      return V3ActRelationshipType.EBS;
    if ("EBEORECW".equals(codeString))
      return V3ActRelationshipType.EBEORECW;
    if ("SBSORSCW".equals(codeString))
      return V3ActRelationshipType.SBSORSCW;
    if ("SBS".equals(codeString))
      return V3ActRelationshipType.SBS;
    if ("AUTH".equals(codeString))
      return V3ActRelationshipType.AUTH;
    if ("CAUS".equals(codeString))
      return V3ActRelationshipType.CAUS;
    if ("COMP".equals(codeString))
      return V3ActRelationshipType.COMP;
    if ("CTRLV".equals(codeString))
      return V3ActRelationshipType.CTRLV;
    if ("MBR".equals(codeString))
      return V3ActRelationshipType.MBR;
    if ("STEP".equals(codeString))
      return V3ActRelationshipType.STEP;
    if ("ARR".equals(codeString))
      return V3ActRelationshipType.ARR;
    if ("DEP".equals(codeString))
      return V3ActRelationshipType.DEP;
    if ("PART".equals(codeString))
      return V3ActRelationshipType.PART;
    if ("COVBY".equals(codeString))
      return V3ActRelationshipType.COVBY;
    if ("DRIV".equals(codeString))
      return V3ActRelationshipType.DRIV;
    if ("ELNK".equals(codeString))
      return V3ActRelationshipType.ELNK;
    if ("EVID".equals(codeString))
      return V3ActRelationshipType.EVID;
    if ("EXACBY".equals(codeString))
      return V3ActRelationshipType.EXACBY;
    if ("EXPL".equals(codeString))
      return V3ActRelationshipType.EXPL;
    if ("INTF".equals(codeString))
      return V3ActRelationshipType.INTF;
    if ("ITEMSLOC".equals(codeString))
      return V3ActRelationshipType.ITEMSLOC;
    if ("LIMIT".equals(codeString))
      return V3ActRelationshipType.LIMIT;
    if ("META".equals(codeString))
      return V3ActRelationshipType.META;
    if ("MFST".equals(codeString))
      return V3ActRelationshipType.MFST;
    if ("NAME".equals(codeString))
      return V3ActRelationshipType.NAME;
    if ("OUTC".equals(codeString))
      return V3ActRelationshipType.OUTC;
    if ("_ActRelationsipObjective".equals(codeString))
      return V3ActRelationshipType._ACTRELATIONSIPOBJECTIVE;
    if ("OBJC".equals(codeString))
      return V3ActRelationshipType.OBJC;
    if ("OBJF".equals(codeString))
      return V3ActRelationshipType.OBJF;
    if ("GOAL".equals(codeString))
      return V3ActRelationshipType.GOAL;
    if ("RISK".equals(codeString))
      return V3ActRelationshipType.RISK;
    if ("PERT".equals(codeString))
      return V3ActRelationshipType.PERT;
    if ("PREV".equals(codeString))
      return V3ActRelationshipType.PREV;
    if ("REFR".equals(codeString))
      return V3ActRelationshipType.REFR;
    if ("USE".equals(codeString))
      return V3ActRelationshipType.USE;
    if ("REFV".equals(codeString))
      return V3ActRelationshipType.REFV;
    if ("RELVBY".equals(codeString))
      return V3ActRelationshipType.RELVBY;
    if ("SEQL".equals(codeString))
      return V3ActRelationshipType.SEQL;
    if ("APND".equals(codeString))
      return V3ActRelationshipType.APND;
    if ("BSLN".equals(codeString))
      return V3ActRelationshipType.BSLN;
    if ("COMPLY".equals(codeString))
      return V3ActRelationshipType.COMPLY;
    if ("DOC".equals(codeString))
      return V3ActRelationshipType.DOC;
    if ("FLFS".equals(codeString))
      return V3ActRelationshipType.FLFS;
    if ("OCCR".equals(codeString))
      return V3ActRelationshipType.OCCR;
    if ("OREF".equals(codeString))
      return V3ActRelationshipType.OREF;
    if ("SCH".equals(codeString))
      return V3ActRelationshipType.SCH;
    if ("GEN".equals(codeString))
      return V3ActRelationshipType.GEN;
    if ("GEVL".equals(codeString))
      return V3ActRelationshipType.GEVL;
    if ("INST".equals(codeString))
      return V3ActRelationshipType.INST;
    if ("MOD".equals(codeString))
      return V3ActRelationshipType.MOD;
    if ("MTCH".equals(codeString))
      return V3ActRelationshipType.MTCH;
    if ("OPTN".equals(codeString))
      return V3ActRelationshipType.OPTN;
    if ("RCHAL".equals(codeString))
      return V3ActRelationshipType.RCHAL;
    if ("REV".equals(codeString))
      return V3ActRelationshipType.REV;
    if ("RPLC".equals(codeString))
      return V3ActRelationshipType.RPLC;
    if ("SUCC".equals(codeString))
      return V3ActRelationshipType.SUCC;
    if ("UPDT".equals(codeString))
      return V3ActRelationshipType.UPDT;
    if ("XCRPT".equals(codeString))
      return V3ActRelationshipType.XCRPT;
    if ("VRXCRPT".equals(codeString))
      return V3ActRelationshipType.VRXCRPT;
    if ("XFRM".equals(codeString))
      return V3ActRelationshipType.XFRM;
    if ("SPRT".equals(codeString))
      return V3ActRelationshipType.SPRT;
    if ("SPRTBND".equals(codeString))
      return V3ActRelationshipType.SPRTBND;
    if ("SUBJ".equals(codeString))
      return V3ActRelationshipType.SUBJ;
    if ("QUALF".equals(codeString))
      return V3ActRelationshipType.QUALF;
    if ("SUMM".equals(codeString))
      return V3ActRelationshipType.SUMM;
    if ("VALUE".equals(codeString))
      return V3ActRelationshipType.VALUE;
    if ("CURE".equals(codeString))
      return V3ActRelationshipType.CURE;
    if ("CURE.ADJ".equals(codeString))
      return V3ActRelationshipType.CURE_ADJ;
    if ("MTGT.ADJ".equals(codeString))
      return V3ActRelationshipType.MTGT_ADJ;
    if ("RACT".equals(codeString))
      return V3ActRelationshipType.RACT;
    if ("SUGG".equals(codeString))
      return V3ActRelationshipType.SUGG;
    throw new IllegalArgumentException("Unknown V3ActRelationshipType code '"+codeString+"'");
  }

  public String toCode(V3ActRelationshipType code) {
    if (code == V3ActRelationshipType.ART)
      return "ART";
    if (code == V3ActRelationshipType._ACTCLASSTEMPORALLYPERTAINS)
      return "_ActClassTemporallyPertains";
    if (code == V3ActRelationshipType._ACTRELATIONSHIPACCOUNTING)
      return "_ActRelationshipAccounting";
    if (code == V3ActRelationshipType._ACTRELATIONSHIPCOSTTRACKING)
      return "_ActRelationshipCostTracking";
    if (code == V3ActRelationshipType.CHRG)
      return "CHRG";
    if (code == V3ActRelationshipType.COST)
      return "COST";
    if (code == V3ActRelationshipType._ACTRELATIONSHIPPOSTING)
      return "_ActRelationshipPosting";
    if (code == V3ActRelationshipType.CREDIT)
      return "CREDIT";
    if (code == V3ActRelationshipType.DEBIT)
      return "DEBIT";
    if (code == V3ActRelationshipType._ACTRELATIONSHIPCONDITIONAL)
      return "_ActRelationshipConditional";
    if (code == V3ActRelationshipType.CIND)
      return "CIND";
    if (code == V3ActRelationshipType.PRCN)
      return "PRCN";
    if (code == V3ActRelationshipType.RSON)
      return "RSON";
    if (code == V3ActRelationshipType.BLOCK)
      return "BLOCK";
    if (code == V3ActRelationshipType.DIAG)
      return "DIAG";
    if (code == V3ActRelationshipType.IMM)
      return "IMM";
    if (code == V3ActRelationshipType.ACTIMM)
      return "ACTIMM";
    if (code == V3ActRelationshipType.PASSIMM)
      return "PASSIMM";
    if (code == V3ActRelationshipType.MITGT)
      return "MITGT";
    if (code == V3ActRelationshipType.RCVY)
      return "RCVY";
    if (code == V3ActRelationshipType.PRYLX)
      return "PRYLX";
    if (code == V3ActRelationshipType.TREAT)
      return "TREAT";
    if (code == V3ActRelationshipType.ADJUNCT)
      return "ADJUNCT";
    if (code == V3ActRelationshipType.MTREAT)
      return "MTREAT";
    if (code == V3ActRelationshipType.PALLTREAT)
      return "PALLTREAT";
    if (code == V3ActRelationshipType.SYMP)
      return "SYMP";
    if (code == V3ActRelationshipType.TRIG)
      return "TRIG";
    if (code == V3ActRelationshipType._ACTRELATIONSHIPTEMPORALLYPERTAINS)
      return "_ActRelationshipTemporallyPertains";
    if (code == V3ActRelationshipType._ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES)
      return "_ActRelationshipTemporallyPertainsApproximates";
    if (code == V3ActRelationshipType.ENE)
      return "ENE";
    if (code == V3ActRelationshipType.ECW)
      return "ECW";
    if (code == V3ActRelationshipType.CONCURRENT)
      return "CONCURRENT";
    if (code == V3ActRelationshipType.SBSECWE)
      return "SBSECWE";
    if (code == V3ActRelationshipType.ENS)
      return "ENS";
    if (code == V3ActRelationshipType.ECWS)
      return "ECWS";
    if (code == V3ActRelationshipType.SNE)
      return "SNE";
    if (code == V3ActRelationshipType.SCWE)
      return "SCWE";
    if (code == V3ActRelationshipType.SNS)
      return "SNS";
    if (code == V3ActRelationshipType.SCW)
      return "SCW";
    if (code == V3ActRelationshipType.SCWSEBE)
      return "SCWSEBE";
    if (code == V3ActRelationshipType.SCWSEAE)
      return "SCWSEAE";
    if (code == V3ActRelationshipType.EAS)
      return "EAS";
    if (code == V3ActRelationshipType.EAE)
      return "EAE";
    if (code == V3ActRelationshipType.SASEAE)
      return "SASEAE";
    if (code == V3ActRelationshipType.SBEEAE)
      return "SBEEAE";
    if (code == V3ActRelationshipType.SASSBEEAS)
      return "SASSBEEAS";
    if (code == V3ActRelationshipType.SBSEAE)
      return "SBSEAE";
    if (code == V3ActRelationshipType.SAS)
      return "SAS";
    if (code == V3ActRelationshipType.SAE)
      return "SAE";
    if (code == V3ActRelationshipType.DURING)
      return "DURING";
    if (code == V3ActRelationshipType.SASECWE)
      return "SASECWE";
    if (code == V3ActRelationshipType.EASORECWS)
      return "EASORECWS";
    if (code == V3ActRelationshipType.EAEORECW)
      return "EAEORECW";
    if (code == V3ActRelationshipType.INDEPENDENT)
      return "INDEPENDENT";
    if (code == V3ActRelationshipType.SAEORSCWE)
      return "SAEORSCWE";
    if (code == V3ActRelationshipType.SASORSCW)
      return "SASORSCW";
    if (code == V3ActRelationshipType.SBEORSCWE)
      return "SBEORSCWE";
    if (code == V3ActRelationshipType.OVERLAP)
      return "OVERLAP";
    if (code == V3ActRelationshipType.EDU)
      return "EDU";
    if (code == V3ActRelationshipType.SBSEASEBE)
      return "SBSEASEBE";
    if (code == V3ActRelationshipType.SBSEAS)
      return "SBSEAS";
    if (code == V3ActRelationshipType.SDU)
      return "SDU";
    if (code == V3ActRelationshipType.SBE)
      return "SBE";
    if (code == V3ActRelationshipType.EBE)
      return "EBE";
    if (code == V3ActRelationshipType.SBSEBE)
      return "SBSEBE";
    if (code == V3ActRelationshipType.EBSORECWS)
      return "EBSORECWS";
    if (code == V3ActRelationshipType.EBS)
      return "EBS";
    if (code == V3ActRelationshipType.EBEORECW)
      return "EBEORECW";
    if (code == V3ActRelationshipType.SBSORSCW)
      return "SBSORSCW";
    if (code == V3ActRelationshipType.SBS)
      return "SBS";
    if (code == V3ActRelationshipType.AUTH)
      return "AUTH";
    if (code == V3ActRelationshipType.CAUS)
      return "CAUS";
    if (code == V3ActRelationshipType.COMP)
      return "COMP";
    if (code == V3ActRelationshipType.CTRLV)
      return "CTRLV";
    if (code == V3ActRelationshipType.MBR)
      return "MBR";
    if (code == V3ActRelationshipType.STEP)
      return "STEP";
    if (code == V3ActRelationshipType.ARR)
      return "ARR";
    if (code == V3ActRelationshipType.DEP)
      return "DEP";
    if (code == V3ActRelationshipType.PART)
      return "PART";
    if (code == V3ActRelationshipType.COVBY)
      return "COVBY";
    if (code == V3ActRelationshipType.DRIV)
      return "DRIV";
    if (code == V3ActRelationshipType.ELNK)
      return "ELNK";
    if (code == V3ActRelationshipType.EVID)
      return "EVID";
    if (code == V3ActRelationshipType.EXACBY)
      return "EXACBY";
    if (code == V3ActRelationshipType.EXPL)
      return "EXPL";
    if (code == V3ActRelationshipType.INTF)
      return "INTF";
    if (code == V3ActRelationshipType.ITEMSLOC)
      return "ITEMSLOC";
    if (code == V3ActRelationshipType.LIMIT)
      return "LIMIT";
    if (code == V3ActRelationshipType.META)
      return "META";
    if (code == V3ActRelationshipType.MFST)
      return "MFST";
    if (code == V3ActRelationshipType.NAME)
      return "NAME";
    if (code == V3ActRelationshipType.OUTC)
      return "OUTC";
    if (code == V3ActRelationshipType._ACTRELATIONSIPOBJECTIVE)
      return "_ActRelationsipObjective";
    if (code == V3ActRelationshipType.OBJC)
      return "OBJC";
    if (code == V3ActRelationshipType.OBJF)
      return "OBJF";
    if (code == V3ActRelationshipType.GOAL)
      return "GOAL";
    if (code == V3ActRelationshipType.RISK)
      return "RISK";
    if (code == V3ActRelationshipType.PERT)
      return "PERT";
    if (code == V3ActRelationshipType.PREV)
      return "PREV";
    if (code == V3ActRelationshipType.REFR)
      return "REFR";
    if (code == V3ActRelationshipType.USE)
      return "USE";
    if (code == V3ActRelationshipType.REFV)
      return "REFV";
    if (code == V3ActRelationshipType.RELVBY)
      return "RELVBY";
    if (code == V3ActRelationshipType.SEQL)
      return "SEQL";
    if (code == V3ActRelationshipType.APND)
      return "APND";
    if (code == V3ActRelationshipType.BSLN)
      return "BSLN";
    if (code == V3ActRelationshipType.COMPLY)
      return "COMPLY";
    if (code == V3ActRelationshipType.DOC)
      return "DOC";
    if (code == V3ActRelationshipType.FLFS)
      return "FLFS";
    if (code == V3ActRelationshipType.OCCR)
      return "OCCR";
    if (code == V3ActRelationshipType.OREF)
      return "OREF";
    if (code == V3ActRelationshipType.SCH)
      return "SCH";
    if (code == V3ActRelationshipType.GEN)
      return "GEN";
    if (code == V3ActRelationshipType.GEVL)
      return "GEVL";
    if (code == V3ActRelationshipType.INST)
      return "INST";
    if (code == V3ActRelationshipType.MOD)
      return "MOD";
    if (code == V3ActRelationshipType.MTCH)
      return "MTCH";
    if (code == V3ActRelationshipType.OPTN)
      return "OPTN";
    if (code == V3ActRelationshipType.RCHAL)
      return "RCHAL";
    if (code == V3ActRelationshipType.REV)
      return "REV";
    if (code == V3ActRelationshipType.RPLC)
      return "RPLC";
    if (code == V3ActRelationshipType.SUCC)
      return "SUCC";
    if (code == V3ActRelationshipType.UPDT)
      return "UPDT";
    if (code == V3ActRelationshipType.XCRPT)
      return "XCRPT";
    if (code == V3ActRelationshipType.VRXCRPT)
      return "VRXCRPT";
    if (code == V3ActRelationshipType.XFRM)
      return "XFRM";
    if (code == V3ActRelationshipType.SPRT)
      return "SPRT";
    if (code == V3ActRelationshipType.SPRTBND)
      return "SPRTBND";
    if (code == V3ActRelationshipType.SUBJ)
      return "SUBJ";
    if (code == V3ActRelationshipType.QUALF)
      return "QUALF";
    if (code == V3ActRelationshipType.SUMM)
      return "SUMM";
    if (code == V3ActRelationshipType.VALUE)
      return "VALUE";
    if (code == V3ActRelationshipType.CURE)
      return "CURE";
    if (code == V3ActRelationshipType.CURE_ADJ)
      return "CURE.ADJ";
    if (code == V3ActRelationshipType.MTGT_ADJ)
      return "MTGT.ADJ";
    if (code == V3ActRelationshipType.RACT)
      return "RACT";
    if (code == V3ActRelationshipType.SUGG)
      return "SUGG";
    return "?";
  }

    public String toSystem(V3ActRelationshipType code) {
      return code.getSystem();
      }

}

