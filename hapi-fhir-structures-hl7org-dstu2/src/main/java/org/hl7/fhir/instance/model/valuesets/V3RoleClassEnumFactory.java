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

public class V3RoleClassEnumFactory implements EnumFactory<V3RoleClass> {

  public V3RoleClass fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ROL".equals(codeString))
      return V3RoleClass.ROL;
    if ("_RoleClassAssociative".equals(codeString))
      return V3RoleClass._ROLECLASSASSOCIATIVE;
    if ("_RoleClassMutualRelationship".equals(codeString))
      return V3RoleClass._ROLECLASSMUTUALRELATIONSHIP;
    if ("_RoleClassRelationshipFormal".equals(codeString))
      return V3RoleClass._ROLECLASSRELATIONSHIPFORMAL;
    if ("AFFL".equals(codeString))
      return V3RoleClass.AFFL;
    if ("AGNT".equals(codeString))
      return V3RoleClass.AGNT;
    if ("ASSIGNED".equals(codeString))
      return V3RoleClass.ASSIGNED;
    if ("COMPAR".equals(codeString))
      return V3RoleClass.COMPAR;
    if ("SGNOFF".equals(codeString))
      return V3RoleClass.SGNOFF;
    if ("CON".equals(codeString))
      return V3RoleClass.CON;
    if ("ECON".equals(codeString))
      return V3RoleClass.ECON;
    if ("NOK".equals(codeString))
      return V3RoleClass.NOK;
    if ("GUARD".equals(codeString))
      return V3RoleClass.GUARD;
    if ("CIT".equals(codeString))
      return V3RoleClass.CIT;
    if ("COVPTY".equals(codeString))
      return V3RoleClass.COVPTY;
    if ("CLAIM".equals(codeString))
      return V3RoleClass.CLAIM;
    if ("NAMED".equals(codeString))
      return V3RoleClass.NAMED;
    if ("DEPEN".equals(codeString))
      return V3RoleClass.DEPEN;
    if ("INDIV".equals(codeString))
      return V3RoleClass.INDIV;
    if ("SUBSCR".equals(codeString))
      return V3RoleClass.SUBSCR;
    if ("PROG".equals(codeString))
      return V3RoleClass.PROG;
    if ("CRINV".equals(codeString))
      return V3RoleClass.CRINV;
    if ("CRSPNSR".equals(codeString))
      return V3RoleClass.CRSPNSR;
    if ("EMP".equals(codeString))
      return V3RoleClass.EMP;
    if ("MIL".equals(codeString))
      return V3RoleClass.MIL;
    if ("GUAR".equals(codeString))
      return V3RoleClass.GUAR;
    if ("INVSBJ".equals(codeString))
      return V3RoleClass.INVSBJ;
    if ("CASEBJ".equals(codeString))
      return V3RoleClass.CASEBJ;
    if ("RESBJ".equals(codeString))
      return V3RoleClass.RESBJ;
    if ("LIC".equals(codeString))
      return V3RoleClass.LIC;
    if ("NOT".equals(codeString))
      return V3RoleClass.NOT;
    if ("PROV".equals(codeString))
      return V3RoleClass.PROV;
    if ("PAT".equals(codeString))
      return V3RoleClass.PAT;
    if ("PAYEE".equals(codeString))
      return V3RoleClass.PAYEE;
    if ("PAYOR".equals(codeString))
      return V3RoleClass.PAYOR;
    if ("POLHOLD".equals(codeString))
      return V3RoleClass.POLHOLD;
    if ("QUAL".equals(codeString))
      return V3RoleClass.QUAL;
    if ("SPNSR".equals(codeString))
      return V3RoleClass.SPNSR;
    if ("STD".equals(codeString))
      return V3RoleClass.STD;
    if ("UNDWRT".equals(codeString))
      return V3RoleClass.UNDWRT;
    if ("CAREGIVER".equals(codeString))
      return V3RoleClass.CAREGIVER;
    if ("PRS".equals(codeString))
      return V3RoleClass.PRS;
    if ("SELF".equals(codeString))
      return V3RoleClass.SELF;
    if ("_RoleClassPassive".equals(codeString))
      return V3RoleClass._ROLECLASSPASSIVE;
    if ("ACCESS".equals(codeString))
      return V3RoleClass.ACCESS;
    if ("ADJY".equals(codeString))
      return V3RoleClass.ADJY;
    if ("CONC".equals(codeString))
      return V3RoleClass.CONC;
    if ("BOND".equals(codeString))
      return V3RoleClass.BOND;
    if ("CONY".equals(codeString))
      return V3RoleClass.CONY;
    if ("ADMM".equals(codeString))
      return V3RoleClass.ADMM;
    if ("BIRTHPL".equals(codeString))
      return V3RoleClass.BIRTHPL;
    if ("DEATHPLC".equals(codeString))
      return V3RoleClass.DEATHPLC;
    if ("DST".equals(codeString))
      return V3RoleClass.DST;
    if ("RET".equals(codeString))
      return V3RoleClass.RET;
    if ("EXLOC".equals(codeString))
      return V3RoleClass.EXLOC;
    if ("SDLOC".equals(codeString))
      return V3RoleClass.SDLOC;
    if ("DSDLOC".equals(codeString))
      return V3RoleClass.DSDLOC;
    if ("ISDLOC".equals(codeString))
      return V3RoleClass.ISDLOC;
    if ("EXPR".equals(codeString))
      return V3RoleClass.EXPR;
    if ("HLD".equals(codeString))
      return V3RoleClass.HLD;
    if ("HLTHCHRT".equals(codeString))
      return V3RoleClass.HLTHCHRT;
    if ("IDENT".equals(codeString))
      return V3RoleClass.IDENT;
    if ("MANU".equals(codeString))
      return V3RoleClass.MANU;
    if ("THER".equals(codeString))
      return V3RoleClass.THER;
    if ("MNT".equals(codeString))
      return V3RoleClass.MNT;
    if ("OWN".equals(codeString))
      return V3RoleClass.OWN;
    if ("RGPR".equals(codeString))
      return V3RoleClass.RGPR;
    if ("TERR".equals(codeString))
      return V3RoleClass.TERR;
    if ("USED".equals(codeString))
      return V3RoleClass.USED;
    if ("WRTE".equals(codeString))
      return V3RoleClass.WRTE;
    if ("_RoleClassOntological".equals(codeString))
      return V3RoleClass._ROLECLASSONTOLOGICAL;
    if ("EQUIV".equals(codeString))
      return V3RoleClass.EQUIV;
    if ("SAME".equals(codeString))
      return V3RoleClass.SAME;
    if ("SUBY".equals(codeString))
      return V3RoleClass.SUBY;
    if ("GEN".equals(codeString))
      return V3RoleClass.GEN;
    if ("GRIC".equals(codeString))
      return V3RoleClass.GRIC;
    if ("INST".equals(codeString))
      return V3RoleClass.INST;
    if ("SUBS".equals(codeString))
      return V3RoleClass.SUBS;
    if ("_RoleClassPartitive".equals(codeString))
      return V3RoleClass._ROLECLASSPARTITIVE;
    if ("CONT".equals(codeString))
      return V3RoleClass.CONT;
    if ("EXPAGTCAR".equals(codeString))
      return V3RoleClass.EXPAGTCAR;
    if ("EXPVECTOR".equals(codeString))
      return V3RoleClass.EXPVECTOR;
    if ("FOMITE".equals(codeString))
      return V3RoleClass.FOMITE;
    if ("INGR".equals(codeString))
      return V3RoleClass.INGR;
    if ("ACTI".equals(codeString))
      return V3RoleClass.ACTI;
    if ("ACTIB".equals(codeString))
      return V3RoleClass.ACTIB;
    if ("ACTIM".equals(codeString))
      return V3RoleClass.ACTIM;
    if ("ACTIR".equals(codeString))
      return V3RoleClass.ACTIR;
    if ("ADJV".equals(codeString))
      return V3RoleClass.ADJV;
    if ("ADTV".equals(codeString))
      return V3RoleClass.ADTV;
    if ("BASE".equals(codeString))
      return V3RoleClass.BASE;
    if ("CNTM".equals(codeString))
      return V3RoleClass.CNTM;
    if ("IACT".equals(codeString))
      return V3RoleClass.IACT;
    if ("COLR".equals(codeString))
      return V3RoleClass.COLR;
    if ("FLVR".equals(codeString))
      return V3RoleClass.FLVR;
    if ("PRSV".equals(codeString))
      return V3RoleClass.PRSV;
    if ("STBL".equals(codeString))
      return V3RoleClass.STBL;
    if ("MECH".equals(codeString))
      return V3RoleClass.MECH;
    if ("LOCE".equals(codeString))
      return V3RoleClass.LOCE;
    if ("STOR".equals(codeString))
      return V3RoleClass.STOR;
    if ("MBR".equals(codeString))
      return V3RoleClass.MBR;
    if ("PART".equals(codeString))
      return V3RoleClass.PART;
    if ("ACTM".equals(codeString))
      return V3RoleClass.ACTM;
    if ("SPEC".equals(codeString))
      return V3RoleClass.SPEC;
    if ("ALQT".equals(codeString))
      return V3RoleClass.ALQT;
    if ("ISLT".equals(codeString))
      return V3RoleClass.ISLT;
    if ("CHILD".equals(codeString))
      return V3RoleClass.CHILD;
    if ("CRED".equals(codeString))
      return V3RoleClass.CRED;
    if ("NURPRAC".equals(codeString))
      return V3RoleClass.NURPRAC;
    if ("NURS".equals(codeString))
      return V3RoleClass.NURS;
    if ("PA".equals(codeString))
      return V3RoleClass.PA;
    if ("PHYS".equals(codeString))
      return V3RoleClass.PHYS;
    throw new IllegalArgumentException("Unknown V3RoleClass code '"+codeString+"'");
  }

  public String toCode(V3RoleClass code) {
    if (code == V3RoleClass.ROL)
      return "ROL";
    if (code == V3RoleClass._ROLECLASSASSOCIATIVE)
      return "_RoleClassAssociative";
    if (code == V3RoleClass._ROLECLASSMUTUALRELATIONSHIP)
      return "_RoleClassMutualRelationship";
    if (code == V3RoleClass._ROLECLASSRELATIONSHIPFORMAL)
      return "_RoleClassRelationshipFormal";
    if (code == V3RoleClass.AFFL)
      return "AFFL";
    if (code == V3RoleClass.AGNT)
      return "AGNT";
    if (code == V3RoleClass.ASSIGNED)
      return "ASSIGNED";
    if (code == V3RoleClass.COMPAR)
      return "COMPAR";
    if (code == V3RoleClass.SGNOFF)
      return "SGNOFF";
    if (code == V3RoleClass.CON)
      return "CON";
    if (code == V3RoleClass.ECON)
      return "ECON";
    if (code == V3RoleClass.NOK)
      return "NOK";
    if (code == V3RoleClass.GUARD)
      return "GUARD";
    if (code == V3RoleClass.CIT)
      return "CIT";
    if (code == V3RoleClass.COVPTY)
      return "COVPTY";
    if (code == V3RoleClass.CLAIM)
      return "CLAIM";
    if (code == V3RoleClass.NAMED)
      return "NAMED";
    if (code == V3RoleClass.DEPEN)
      return "DEPEN";
    if (code == V3RoleClass.INDIV)
      return "INDIV";
    if (code == V3RoleClass.SUBSCR)
      return "SUBSCR";
    if (code == V3RoleClass.PROG)
      return "PROG";
    if (code == V3RoleClass.CRINV)
      return "CRINV";
    if (code == V3RoleClass.CRSPNSR)
      return "CRSPNSR";
    if (code == V3RoleClass.EMP)
      return "EMP";
    if (code == V3RoleClass.MIL)
      return "MIL";
    if (code == V3RoleClass.GUAR)
      return "GUAR";
    if (code == V3RoleClass.INVSBJ)
      return "INVSBJ";
    if (code == V3RoleClass.CASEBJ)
      return "CASEBJ";
    if (code == V3RoleClass.RESBJ)
      return "RESBJ";
    if (code == V3RoleClass.LIC)
      return "LIC";
    if (code == V3RoleClass.NOT)
      return "NOT";
    if (code == V3RoleClass.PROV)
      return "PROV";
    if (code == V3RoleClass.PAT)
      return "PAT";
    if (code == V3RoleClass.PAYEE)
      return "PAYEE";
    if (code == V3RoleClass.PAYOR)
      return "PAYOR";
    if (code == V3RoleClass.POLHOLD)
      return "POLHOLD";
    if (code == V3RoleClass.QUAL)
      return "QUAL";
    if (code == V3RoleClass.SPNSR)
      return "SPNSR";
    if (code == V3RoleClass.STD)
      return "STD";
    if (code == V3RoleClass.UNDWRT)
      return "UNDWRT";
    if (code == V3RoleClass.CAREGIVER)
      return "CAREGIVER";
    if (code == V3RoleClass.PRS)
      return "PRS";
    if (code == V3RoleClass.SELF)
      return "SELF";
    if (code == V3RoleClass._ROLECLASSPASSIVE)
      return "_RoleClassPassive";
    if (code == V3RoleClass.ACCESS)
      return "ACCESS";
    if (code == V3RoleClass.ADJY)
      return "ADJY";
    if (code == V3RoleClass.CONC)
      return "CONC";
    if (code == V3RoleClass.BOND)
      return "BOND";
    if (code == V3RoleClass.CONY)
      return "CONY";
    if (code == V3RoleClass.ADMM)
      return "ADMM";
    if (code == V3RoleClass.BIRTHPL)
      return "BIRTHPL";
    if (code == V3RoleClass.DEATHPLC)
      return "DEATHPLC";
    if (code == V3RoleClass.DST)
      return "DST";
    if (code == V3RoleClass.RET)
      return "RET";
    if (code == V3RoleClass.EXLOC)
      return "EXLOC";
    if (code == V3RoleClass.SDLOC)
      return "SDLOC";
    if (code == V3RoleClass.DSDLOC)
      return "DSDLOC";
    if (code == V3RoleClass.ISDLOC)
      return "ISDLOC";
    if (code == V3RoleClass.EXPR)
      return "EXPR";
    if (code == V3RoleClass.HLD)
      return "HLD";
    if (code == V3RoleClass.HLTHCHRT)
      return "HLTHCHRT";
    if (code == V3RoleClass.IDENT)
      return "IDENT";
    if (code == V3RoleClass.MANU)
      return "MANU";
    if (code == V3RoleClass.THER)
      return "THER";
    if (code == V3RoleClass.MNT)
      return "MNT";
    if (code == V3RoleClass.OWN)
      return "OWN";
    if (code == V3RoleClass.RGPR)
      return "RGPR";
    if (code == V3RoleClass.TERR)
      return "TERR";
    if (code == V3RoleClass.USED)
      return "USED";
    if (code == V3RoleClass.WRTE)
      return "WRTE";
    if (code == V3RoleClass._ROLECLASSONTOLOGICAL)
      return "_RoleClassOntological";
    if (code == V3RoleClass.EQUIV)
      return "EQUIV";
    if (code == V3RoleClass.SAME)
      return "SAME";
    if (code == V3RoleClass.SUBY)
      return "SUBY";
    if (code == V3RoleClass.GEN)
      return "GEN";
    if (code == V3RoleClass.GRIC)
      return "GRIC";
    if (code == V3RoleClass.INST)
      return "INST";
    if (code == V3RoleClass.SUBS)
      return "SUBS";
    if (code == V3RoleClass._ROLECLASSPARTITIVE)
      return "_RoleClassPartitive";
    if (code == V3RoleClass.CONT)
      return "CONT";
    if (code == V3RoleClass.EXPAGTCAR)
      return "EXPAGTCAR";
    if (code == V3RoleClass.EXPVECTOR)
      return "EXPVECTOR";
    if (code == V3RoleClass.FOMITE)
      return "FOMITE";
    if (code == V3RoleClass.INGR)
      return "INGR";
    if (code == V3RoleClass.ACTI)
      return "ACTI";
    if (code == V3RoleClass.ACTIB)
      return "ACTIB";
    if (code == V3RoleClass.ACTIM)
      return "ACTIM";
    if (code == V3RoleClass.ACTIR)
      return "ACTIR";
    if (code == V3RoleClass.ADJV)
      return "ADJV";
    if (code == V3RoleClass.ADTV)
      return "ADTV";
    if (code == V3RoleClass.BASE)
      return "BASE";
    if (code == V3RoleClass.CNTM)
      return "CNTM";
    if (code == V3RoleClass.IACT)
      return "IACT";
    if (code == V3RoleClass.COLR)
      return "COLR";
    if (code == V3RoleClass.FLVR)
      return "FLVR";
    if (code == V3RoleClass.PRSV)
      return "PRSV";
    if (code == V3RoleClass.STBL)
      return "STBL";
    if (code == V3RoleClass.MECH)
      return "MECH";
    if (code == V3RoleClass.LOCE)
      return "LOCE";
    if (code == V3RoleClass.STOR)
      return "STOR";
    if (code == V3RoleClass.MBR)
      return "MBR";
    if (code == V3RoleClass.PART)
      return "PART";
    if (code == V3RoleClass.ACTM)
      return "ACTM";
    if (code == V3RoleClass.SPEC)
      return "SPEC";
    if (code == V3RoleClass.ALQT)
      return "ALQT";
    if (code == V3RoleClass.ISLT)
      return "ISLT";
    if (code == V3RoleClass.CHILD)
      return "CHILD";
    if (code == V3RoleClass.CRED)
      return "CRED";
    if (code == V3RoleClass.NURPRAC)
      return "NURPRAC";
    if (code == V3RoleClass.NURS)
      return "NURS";
    if (code == V3RoleClass.PA)
      return "PA";
    if (code == V3RoleClass.PHYS)
      return "PHYS";
    return "?";
  }


}

