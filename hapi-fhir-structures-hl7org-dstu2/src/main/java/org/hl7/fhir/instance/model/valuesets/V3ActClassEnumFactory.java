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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class V3ActClassEnumFactory implements EnumFactory<V3ActClass> {

  public V3ActClass fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ACT".equals(codeString))
      return V3ActClass.ACT;
    if ("_ActClassRecordOrganizer".equals(codeString))
      return V3ActClass._ACTCLASSRECORDORGANIZER;
    if ("COMPOSITION".equals(codeString))
      return V3ActClass.COMPOSITION;
    if ("DOC".equals(codeString))
      return V3ActClass.DOC;
    if ("DOCCLIN".equals(codeString))
      return V3ActClass.DOCCLIN;
    if ("CDALVLONE".equals(codeString))
      return V3ActClass.CDALVLONE;
    if ("CONTAINER".equals(codeString))
      return V3ActClass.CONTAINER;
    if ("CATEGORY".equals(codeString))
      return V3ActClass.CATEGORY;
    if ("DOCBODY".equals(codeString))
      return V3ActClass.DOCBODY;
    if ("DOCSECT".equals(codeString))
      return V3ActClass.DOCSECT;
    if ("TOPIC".equals(codeString))
      return V3ActClass.TOPIC;
    if ("EXTRACT".equals(codeString))
      return V3ActClass.EXTRACT;
    if ("EHR".equals(codeString))
      return V3ActClass.EHR;
    if ("FOLDER".equals(codeString))
      return V3ActClass.FOLDER;
    if ("GROUPER".equals(codeString))
      return V3ActClass.GROUPER;
    if ("CLUSTER".equals(codeString))
      return V3ActClass.CLUSTER;
    if ("ACCM".equals(codeString))
      return V3ActClass.ACCM;
    if ("ACCT".equals(codeString))
      return V3ActClass.ACCT;
    if ("ACSN".equals(codeString))
      return V3ActClass.ACSN;
    if ("ADJUD".equals(codeString))
      return V3ActClass.ADJUD;
    if ("CACT".equals(codeString))
      return V3ActClass.CACT;
    if ("ACTN".equals(codeString))
      return V3ActClass.ACTN;
    if ("INFO".equals(codeString))
      return V3ActClass.INFO;
    if ("STC".equals(codeString))
      return V3ActClass.STC;
    if ("CNTRCT".equals(codeString))
      return V3ActClass.CNTRCT;
    if ("FCNTRCT".equals(codeString))
      return V3ActClass.FCNTRCT;
    if ("COV".equals(codeString))
      return V3ActClass.COV;
    if ("CONC".equals(codeString))
      return V3ActClass.CONC;
    if ("HCASE".equals(codeString))
      return V3ActClass.HCASE;
    if ("OUTBR".equals(codeString))
      return V3ActClass.OUTBR;
    if ("CONS".equals(codeString))
      return V3ActClass.CONS;
    if ("CONTREG".equals(codeString))
      return V3ActClass.CONTREG;
    if ("CTTEVENT".equals(codeString))
      return V3ActClass.CTTEVENT;
    if ("DISPACT".equals(codeString))
      return V3ActClass.DISPACT;
    if ("EXPOS".equals(codeString))
      return V3ActClass.EXPOS;
    if ("AEXPOS".equals(codeString))
      return V3ActClass.AEXPOS;
    if ("TEXPOS".equals(codeString))
      return V3ActClass.TEXPOS;
    if ("INC".equals(codeString))
      return V3ActClass.INC;
    if ("INFRM".equals(codeString))
      return V3ActClass.INFRM;
    if ("INVE".equals(codeString))
      return V3ActClass.INVE;
    if ("LIST".equals(codeString))
      return V3ActClass.LIST;
    if ("MPROT".equals(codeString))
      return V3ActClass.MPROT;
    if ("OBS".equals(codeString))
      return V3ActClass.OBS;
    if ("_ActClassROI".equals(codeString))
      return V3ActClass._ACTCLASSROI;
    if ("ROIBND".equals(codeString))
      return V3ActClass.ROIBND;
    if ("ROIOVL".equals(codeString))
      return V3ActClass.ROIOVL;
    if ("_SubjectPhysicalPosition".equals(codeString))
      return V3ActClass._SUBJECTPHYSICALPOSITION;
    if ("_SubjectBodyPosition".equals(codeString))
      return V3ActClass._SUBJECTBODYPOSITION;
    if ("LLD".equals(codeString))
      return V3ActClass.LLD;
    if ("PRN".equals(codeString))
      return V3ActClass.PRN;
    if ("RLD".equals(codeString))
      return V3ActClass.RLD;
    if ("SFWL".equals(codeString))
      return V3ActClass.SFWL;
    if ("SIT".equals(codeString))
      return V3ActClass.SIT;
    if ("STN".equals(codeString))
      return V3ActClass.STN;
    if ("SUP".equals(codeString))
      return V3ActClass.SUP;
    if ("RTRD".equals(codeString))
      return V3ActClass.RTRD;
    if ("TRD".equals(codeString))
      return V3ActClass.TRD;
    if ("ALRT".equals(codeString))
      return V3ActClass.ALRT;
    if ("BATTERY".equals(codeString))
      return V3ActClass.BATTERY;
    if ("CLNTRL".equals(codeString))
      return V3ActClass.CLNTRL;
    if ("CNOD".equals(codeString))
      return V3ActClass.CNOD;
    if ("COND".equals(codeString))
      return V3ActClass.COND;
    if ("CASE".equals(codeString))
      return V3ActClass.CASE;
    if ("OUTB".equals(codeString))
      return V3ActClass.OUTB;
    if ("DGIMG".equals(codeString))
      return V3ActClass.DGIMG;
    if ("GEN".equals(codeString))
      return V3ActClass.GEN;
    if ("DETPOL".equals(codeString))
      return V3ActClass.DETPOL;
    if ("EXP".equals(codeString))
      return V3ActClass.EXP;
    if ("LOC".equals(codeString))
      return V3ActClass.LOC;
    if ("PHN".equals(codeString))
      return V3ActClass.PHN;
    if ("POL".equals(codeString))
      return V3ActClass.POL;
    if ("SEQ".equals(codeString))
      return V3ActClass.SEQ;
    if ("SEQVAR".equals(codeString))
      return V3ActClass.SEQVAR;
    if ("INVSTG".equals(codeString))
      return V3ActClass.INVSTG;
    if ("OBSSER".equals(codeString))
      return V3ActClass.OBSSER;
    if ("OBSCOR".equals(codeString))
      return V3ActClass.OBSCOR;
    if ("POS".equals(codeString))
      return V3ActClass.POS;
    if ("POSACC".equals(codeString))
      return V3ActClass.POSACC;
    if ("POSCOORD".equals(codeString))
      return V3ActClass.POSCOORD;
    if ("SPCOBS".equals(codeString))
      return V3ActClass.SPCOBS;
    if ("VERIF".equals(codeString))
      return V3ActClass.VERIF;
    if ("PCPR".equals(codeString))
      return V3ActClass.PCPR;
    if ("ENC".equals(codeString))
      return V3ActClass.ENC;
    if ("POLICY".equals(codeString))
      return V3ActClass.POLICY;
    if ("JURISPOL".equals(codeString))
      return V3ActClass.JURISPOL;
    if ("ORGPOL".equals(codeString))
      return V3ActClass.ORGPOL;
    if ("SCOPOL".equals(codeString))
      return V3ActClass.SCOPOL;
    if ("STDPOL".equals(codeString))
      return V3ActClass.STDPOL;
    if ("PROC".equals(codeString))
      return V3ActClass.PROC;
    if ("SBADM".equals(codeString))
      return V3ActClass.SBADM;
    if ("SBEXT".equals(codeString))
      return V3ActClass.SBEXT;
    if ("SPECCOLLECT".equals(codeString))
      return V3ActClass.SPECCOLLECT;
    if ("REG".equals(codeString))
      return V3ActClass.REG;
    if ("REV".equals(codeString))
      return V3ActClass.REV;
    if ("SPCTRT".equals(codeString))
      return V3ActClass.SPCTRT;
    if ("SPLY".equals(codeString))
      return V3ActClass.SPLY;
    if ("DIET".equals(codeString))
      return V3ActClass.DIET;
    if ("STORE".equals(codeString))
      return V3ActClass.STORE;
    if ("SUBST".equals(codeString))
      return V3ActClass.SUBST;
    if ("TRFR".equals(codeString))
      return V3ActClass.TRFR;
    if ("TRNS".equals(codeString))
      return V3ActClass.TRNS;
    if ("XACT".equals(codeString))
      return V3ActClass.XACT;
    throw new IllegalArgumentException("Unknown V3ActClass code '"+codeString+"'");
  }

  public String toCode(V3ActClass code) {
    if (code == V3ActClass.ACT)
      return "ACT";
    if (code == V3ActClass._ACTCLASSRECORDORGANIZER)
      return "_ActClassRecordOrganizer";
    if (code == V3ActClass.COMPOSITION)
      return "COMPOSITION";
    if (code == V3ActClass.DOC)
      return "DOC";
    if (code == V3ActClass.DOCCLIN)
      return "DOCCLIN";
    if (code == V3ActClass.CDALVLONE)
      return "CDALVLONE";
    if (code == V3ActClass.CONTAINER)
      return "CONTAINER";
    if (code == V3ActClass.CATEGORY)
      return "CATEGORY";
    if (code == V3ActClass.DOCBODY)
      return "DOCBODY";
    if (code == V3ActClass.DOCSECT)
      return "DOCSECT";
    if (code == V3ActClass.TOPIC)
      return "TOPIC";
    if (code == V3ActClass.EXTRACT)
      return "EXTRACT";
    if (code == V3ActClass.EHR)
      return "EHR";
    if (code == V3ActClass.FOLDER)
      return "FOLDER";
    if (code == V3ActClass.GROUPER)
      return "GROUPER";
    if (code == V3ActClass.CLUSTER)
      return "CLUSTER";
    if (code == V3ActClass.ACCM)
      return "ACCM";
    if (code == V3ActClass.ACCT)
      return "ACCT";
    if (code == V3ActClass.ACSN)
      return "ACSN";
    if (code == V3ActClass.ADJUD)
      return "ADJUD";
    if (code == V3ActClass.CACT)
      return "CACT";
    if (code == V3ActClass.ACTN)
      return "ACTN";
    if (code == V3ActClass.INFO)
      return "INFO";
    if (code == V3ActClass.STC)
      return "STC";
    if (code == V3ActClass.CNTRCT)
      return "CNTRCT";
    if (code == V3ActClass.FCNTRCT)
      return "FCNTRCT";
    if (code == V3ActClass.COV)
      return "COV";
    if (code == V3ActClass.CONC)
      return "CONC";
    if (code == V3ActClass.HCASE)
      return "HCASE";
    if (code == V3ActClass.OUTBR)
      return "OUTBR";
    if (code == V3ActClass.CONS)
      return "CONS";
    if (code == V3ActClass.CONTREG)
      return "CONTREG";
    if (code == V3ActClass.CTTEVENT)
      return "CTTEVENT";
    if (code == V3ActClass.DISPACT)
      return "DISPACT";
    if (code == V3ActClass.EXPOS)
      return "EXPOS";
    if (code == V3ActClass.AEXPOS)
      return "AEXPOS";
    if (code == V3ActClass.TEXPOS)
      return "TEXPOS";
    if (code == V3ActClass.INC)
      return "INC";
    if (code == V3ActClass.INFRM)
      return "INFRM";
    if (code == V3ActClass.INVE)
      return "INVE";
    if (code == V3ActClass.LIST)
      return "LIST";
    if (code == V3ActClass.MPROT)
      return "MPROT";
    if (code == V3ActClass.OBS)
      return "OBS";
    if (code == V3ActClass._ACTCLASSROI)
      return "_ActClassROI";
    if (code == V3ActClass.ROIBND)
      return "ROIBND";
    if (code == V3ActClass.ROIOVL)
      return "ROIOVL";
    if (code == V3ActClass._SUBJECTPHYSICALPOSITION)
      return "_SubjectPhysicalPosition";
    if (code == V3ActClass._SUBJECTBODYPOSITION)
      return "_SubjectBodyPosition";
    if (code == V3ActClass.LLD)
      return "LLD";
    if (code == V3ActClass.PRN)
      return "PRN";
    if (code == V3ActClass.RLD)
      return "RLD";
    if (code == V3ActClass.SFWL)
      return "SFWL";
    if (code == V3ActClass.SIT)
      return "SIT";
    if (code == V3ActClass.STN)
      return "STN";
    if (code == V3ActClass.SUP)
      return "SUP";
    if (code == V3ActClass.RTRD)
      return "RTRD";
    if (code == V3ActClass.TRD)
      return "TRD";
    if (code == V3ActClass.ALRT)
      return "ALRT";
    if (code == V3ActClass.BATTERY)
      return "BATTERY";
    if (code == V3ActClass.CLNTRL)
      return "CLNTRL";
    if (code == V3ActClass.CNOD)
      return "CNOD";
    if (code == V3ActClass.COND)
      return "COND";
    if (code == V3ActClass.CASE)
      return "CASE";
    if (code == V3ActClass.OUTB)
      return "OUTB";
    if (code == V3ActClass.DGIMG)
      return "DGIMG";
    if (code == V3ActClass.GEN)
      return "GEN";
    if (code == V3ActClass.DETPOL)
      return "DETPOL";
    if (code == V3ActClass.EXP)
      return "EXP";
    if (code == V3ActClass.LOC)
      return "LOC";
    if (code == V3ActClass.PHN)
      return "PHN";
    if (code == V3ActClass.POL)
      return "POL";
    if (code == V3ActClass.SEQ)
      return "SEQ";
    if (code == V3ActClass.SEQVAR)
      return "SEQVAR";
    if (code == V3ActClass.INVSTG)
      return "INVSTG";
    if (code == V3ActClass.OBSSER)
      return "OBSSER";
    if (code == V3ActClass.OBSCOR)
      return "OBSCOR";
    if (code == V3ActClass.POS)
      return "POS";
    if (code == V3ActClass.POSACC)
      return "POSACC";
    if (code == V3ActClass.POSCOORD)
      return "POSCOORD";
    if (code == V3ActClass.SPCOBS)
      return "SPCOBS";
    if (code == V3ActClass.VERIF)
      return "VERIF";
    if (code == V3ActClass.PCPR)
      return "PCPR";
    if (code == V3ActClass.ENC)
      return "ENC";
    if (code == V3ActClass.POLICY)
      return "POLICY";
    if (code == V3ActClass.JURISPOL)
      return "JURISPOL";
    if (code == V3ActClass.ORGPOL)
      return "ORGPOL";
    if (code == V3ActClass.SCOPOL)
      return "SCOPOL";
    if (code == V3ActClass.STDPOL)
      return "STDPOL";
    if (code == V3ActClass.PROC)
      return "PROC";
    if (code == V3ActClass.SBADM)
      return "SBADM";
    if (code == V3ActClass.SBEXT)
      return "SBEXT";
    if (code == V3ActClass.SPECCOLLECT)
      return "SPECCOLLECT";
    if (code == V3ActClass.REG)
      return "REG";
    if (code == V3ActClass.REV)
      return "REV";
    if (code == V3ActClass.SPCTRT)
      return "SPCTRT";
    if (code == V3ActClass.SPLY)
      return "SPLY";
    if (code == V3ActClass.DIET)
      return "DIET";
    if (code == V3ActClass.STORE)
      return "STORE";
    if (code == V3ActClass.SUBST)
      return "SUBST";
    if (code == V3ActClass.TRFR)
      return "TRFR";
    if (code == V3ActClass.TRNS)
      return "TRNS";
    if (code == V3ActClass.XACT)
      return "XACT";
    return "?";
  }


}

