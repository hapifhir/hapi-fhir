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

public class V3SpecimenTypeEnumFactory implements EnumFactory<V3SpecimenType> {

  public V3SpecimenType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_SpecimenEntityType".equals(codeString))
      return V3SpecimenType._SPECIMENENTITYTYPE;
    if ("ABS".equals(codeString))
      return V3SpecimenType.ABS;
    if ("AMN".equals(codeString))
      return V3SpecimenType.AMN;
    if ("ASP".equals(codeString))
      return V3SpecimenType.ASP;
    if ("BBL".equals(codeString))
      return V3SpecimenType.BBL;
    if ("BDY".equals(codeString))
      return V3SpecimenType.BDY;
    if ("BIFL".equals(codeString))
      return V3SpecimenType.BIFL;
    if ("BLD".equals(codeString))
      return V3SpecimenType.BLD;
    if ("BLDA".equals(codeString))
      return V3SpecimenType.BLDA;
    if ("BLDC".equals(codeString))
      return V3SpecimenType.BLDC;
    if ("BLDCO".equals(codeString))
      return V3SpecimenType.BLDCO;
    if ("BLDV".equals(codeString))
      return V3SpecimenType.BLDV;
    if ("BON".equals(codeString))
      return V3SpecimenType.BON;
    if ("BPH".equals(codeString))
      return V3SpecimenType.BPH;
    if ("BPU".equals(codeString))
      return V3SpecimenType.BPU;
    if ("BRN".equals(codeString))
      return V3SpecimenType.BRN;
    if ("BRO".equals(codeString))
      return V3SpecimenType.BRO;
    if ("BRTH".equals(codeString))
      return V3SpecimenType.BRTH;
    if ("CALC".equals(codeString))
      return V3SpecimenType.CALC;
    if ("CDM".equals(codeString))
      return V3SpecimenType.CDM;
    if ("CNJT".equals(codeString))
      return V3SpecimenType.CNJT;
    if ("CNL".equals(codeString))
      return V3SpecimenType.CNL;
    if ("COL".equals(codeString))
      return V3SpecimenType.COL;
    if ("CRN".equals(codeString))
      return V3SpecimenType.CRN;
    if ("CSF".equals(codeString))
      return V3SpecimenType.CSF;
    if ("CTP".equals(codeString))
      return V3SpecimenType.CTP;
    if ("CUR".equals(codeString))
      return V3SpecimenType.CUR;
    if ("CVM".equals(codeString))
      return V3SpecimenType.CVM;
    if ("CVX".equals(codeString))
      return V3SpecimenType.CVX;
    if ("CYST".equals(codeString))
      return V3SpecimenType.CYST;
    if ("DIAF".equals(codeString))
      return V3SpecimenType.DIAF;
    if ("DOSE".equals(codeString))
      return V3SpecimenType.DOSE;
    if ("DRN".equals(codeString))
      return V3SpecimenType.DRN;
    if ("DUFL".equals(codeString))
      return V3SpecimenType.DUFL;
    if ("EAR".equals(codeString))
      return V3SpecimenType.EAR;
    if ("EARW".equals(codeString))
      return V3SpecimenType.EARW;
    if ("ELT".equals(codeString))
      return V3SpecimenType.ELT;
    if ("ENDC".equals(codeString))
      return V3SpecimenType.ENDC;
    if ("ENDM".equals(codeString))
      return V3SpecimenType.ENDM;
    if ("EOS".equals(codeString))
      return V3SpecimenType.EOS;
    if ("EYE".equals(codeString))
      return V3SpecimenType.EYE;
    if ("FIB".equals(codeString))
      return V3SpecimenType.FIB;
    if ("FIST".equals(codeString))
      return V3SpecimenType.FIST;
    if ("FLT".equals(codeString))
      return V3SpecimenType.FLT;
    if ("FLU".equals(codeString))
      return V3SpecimenType.FLU;
    if ("FOOD".equals(codeString))
      return V3SpecimenType.FOOD;
    if ("GAS".equals(codeString))
      return V3SpecimenType.GAS;
    if ("GAST".equals(codeString))
      return V3SpecimenType.GAST;
    if ("GEN".equals(codeString))
      return V3SpecimenType.GEN;
    if ("GENC".equals(codeString))
      return V3SpecimenType.GENC;
    if ("GENF".equals(codeString))
      return V3SpecimenType.GENF;
    if ("GENL".equals(codeString))
      return V3SpecimenType.GENL;
    if ("GENV".equals(codeString))
      return V3SpecimenType.GENV;
    if ("HAR".equals(codeString))
      return V3SpecimenType.HAR;
    if ("IHG".equals(codeString))
      return V3SpecimenType.IHG;
    if ("ISLT".equals(codeString))
      return V3SpecimenType.ISLT;
    if ("IT".equals(codeString))
      return V3SpecimenType.IT;
    if ("LAM".equals(codeString))
      return V3SpecimenType.LAM;
    if ("LIQ".equals(codeString))
      return V3SpecimenType.LIQ;
    if ("LN".equals(codeString))
      return V3SpecimenType.LN;
    if ("LNA".equals(codeString))
      return V3SpecimenType.LNA;
    if ("LNV".equals(codeString))
      return V3SpecimenType.LNV;
    if ("LYM".equals(codeString))
      return V3SpecimenType.LYM;
    if ("MAC".equals(codeString))
      return V3SpecimenType.MAC;
    if ("MAR".equals(codeString))
      return V3SpecimenType.MAR;
    if ("MBLD".equals(codeString))
      return V3SpecimenType.MBLD;
    if ("MEC".equals(codeString))
      return V3SpecimenType.MEC;
    if ("MILK".equals(codeString))
      return V3SpecimenType.MILK;
    if ("MLK".equals(codeString))
      return V3SpecimenType.MLK;
    if ("NAIL".equals(codeString))
      return V3SpecimenType.NAIL;
    if ("NOS".equals(codeString))
      return V3SpecimenType.NOS;
    if ("PAFL".equals(codeString))
      return V3SpecimenType.PAFL;
    if ("PAT".equals(codeString))
      return V3SpecimenType.PAT;
    if ("PLAS".equals(codeString))
      return V3SpecimenType.PLAS;
    if ("PLB".equals(codeString))
      return V3SpecimenType.PLB;
    if ("PLC".equals(codeString))
      return V3SpecimenType.PLC;
    if ("PLR".equals(codeString))
      return V3SpecimenType.PLR;
    if ("PMN".equals(codeString))
      return V3SpecimenType.PMN;
    if ("PPP".equals(codeString))
      return V3SpecimenType.PPP;
    if ("PRP".equals(codeString))
      return V3SpecimenType.PRP;
    if ("PRT".equals(codeString))
      return V3SpecimenType.PRT;
    if ("PUS".equals(codeString))
      return V3SpecimenType.PUS;
    if ("RBC".equals(codeString))
      return V3SpecimenType.RBC;
    if ("SAL".equals(codeString))
      return V3SpecimenType.SAL;
    if ("SER".equals(codeString))
      return V3SpecimenType.SER;
    if ("SKM".equals(codeString))
      return V3SpecimenType.SKM;
    if ("SKN".equals(codeString))
      return V3SpecimenType.SKN;
    if ("SMN".equals(codeString))
      return V3SpecimenType.SMN;
    if ("SMPLS".equals(codeString))
      return V3SpecimenType.SMPLS;
    if ("SNV".equals(codeString))
      return V3SpecimenType.SNV;
    if ("SPRM".equals(codeString))
      return V3SpecimenType.SPRM;
    if ("SPT".equals(codeString))
      return V3SpecimenType.SPT;
    if ("SPTC".equals(codeString))
      return V3SpecimenType.SPTC;
    if ("SPTT".equals(codeString))
      return V3SpecimenType.SPTT;
    if ("STL".equals(codeString))
      return V3SpecimenType.STL;
    if ("SWT".equals(codeString))
      return V3SpecimenType.SWT;
    if ("TEAR".equals(codeString))
      return V3SpecimenType.TEAR;
    if ("THRB".equals(codeString))
      return V3SpecimenType.THRB;
    if ("THRT".equals(codeString))
      return V3SpecimenType.THRT;
    if ("TISG".equals(codeString))
      return V3SpecimenType.TISG;
    if ("TISPL".equals(codeString))
      return V3SpecimenType.TISPL;
    if ("TISS".equals(codeString))
      return V3SpecimenType.TISS;
    if ("TISU".equals(codeString))
      return V3SpecimenType.TISU;
    if ("TLGI".equals(codeString))
      return V3SpecimenType.TLGI;
    if ("TLNG".equals(codeString))
      return V3SpecimenType.TLNG;
    if ("TSMI".equals(codeString))
      return V3SpecimenType.TSMI;
    if ("TUB".equals(codeString))
      return V3SpecimenType.TUB;
    if ("ULC".equals(codeString))
      return V3SpecimenType.ULC;
    if ("UMB".equals(codeString))
      return V3SpecimenType.UMB;
    if ("UMED".equals(codeString))
      return V3SpecimenType.UMED;
    if ("UR".equals(codeString))
      return V3SpecimenType.UR;
    if ("URC".equals(codeString))
      return V3SpecimenType.URC;
    if ("URNS".equals(codeString))
      return V3SpecimenType.URNS;
    if ("URT".equals(codeString))
      return V3SpecimenType.URT;
    if ("URTH".equals(codeString))
      return V3SpecimenType.URTH;
    if ("USUB".equals(codeString))
      return V3SpecimenType.USUB;
    if ("VOM".equals(codeString))
      return V3SpecimenType.VOM;
    if ("WAT".equals(codeString))
      return V3SpecimenType.WAT;
    if ("WBC".equals(codeString))
      return V3SpecimenType.WBC;
    if ("WICK".equals(codeString))
      return V3SpecimenType.WICK;
    if ("WND".equals(codeString))
      return V3SpecimenType.WND;
    if ("WNDA".equals(codeString))
      return V3SpecimenType.WNDA;
    if ("WNDD".equals(codeString))
      return V3SpecimenType.WNDD;
    if ("WNDE".equals(codeString))
      return V3SpecimenType.WNDE;
    throw new IllegalArgumentException("Unknown V3SpecimenType code '"+codeString+"'");
  }

  public String toCode(V3SpecimenType code) {
    if (code == V3SpecimenType._SPECIMENENTITYTYPE)
      return "_SpecimenEntityType";
    if (code == V3SpecimenType.ABS)
      return "ABS";
    if (code == V3SpecimenType.AMN)
      return "AMN";
    if (code == V3SpecimenType.ASP)
      return "ASP";
    if (code == V3SpecimenType.BBL)
      return "BBL";
    if (code == V3SpecimenType.BDY)
      return "BDY";
    if (code == V3SpecimenType.BIFL)
      return "BIFL";
    if (code == V3SpecimenType.BLD)
      return "BLD";
    if (code == V3SpecimenType.BLDA)
      return "BLDA";
    if (code == V3SpecimenType.BLDC)
      return "BLDC";
    if (code == V3SpecimenType.BLDCO)
      return "BLDCO";
    if (code == V3SpecimenType.BLDV)
      return "BLDV";
    if (code == V3SpecimenType.BON)
      return "BON";
    if (code == V3SpecimenType.BPH)
      return "BPH";
    if (code == V3SpecimenType.BPU)
      return "BPU";
    if (code == V3SpecimenType.BRN)
      return "BRN";
    if (code == V3SpecimenType.BRO)
      return "BRO";
    if (code == V3SpecimenType.BRTH)
      return "BRTH";
    if (code == V3SpecimenType.CALC)
      return "CALC";
    if (code == V3SpecimenType.CDM)
      return "CDM";
    if (code == V3SpecimenType.CNJT)
      return "CNJT";
    if (code == V3SpecimenType.CNL)
      return "CNL";
    if (code == V3SpecimenType.COL)
      return "COL";
    if (code == V3SpecimenType.CRN)
      return "CRN";
    if (code == V3SpecimenType.CSF)
      return "CSF";
    if (code == V3SpecimenType.CTP)
      return "CTP";
    if (code == V3SpecimenType.CUR)
      return "CUR";
    if (code == V3SpecimenType.CVM)
      return "CVM";
    if (code == V3SpecimenType.CVX)
      return "CVX";
    if (code == V3SpecimenType.CYST)
      return "CYST";
    if (code == V3SpecimenType.DIAF)
      return "DIAF";
    if (code == V3SpecimenType.DOSE)
      return "DOSE";
    if (code == V3SpecimenType.DRN)
      return "DRN";
    if (code == V3SpecimenType.DUFL)
      return "DUFL";
    if (code == V3SpecimenType.EAR)
      return "EAR";
    if (code == V3SpecimenType.EARW)
      return "EARW";
    if (code == V3SpecimenType.ELT)
      return "ELT";
    if (code == V3SpecimenType.ENDC)
      return "ENDC";
    if (code == V3SpecimenType.ENDM)
      return "ENDM";
    if (code == V3SpecimenType.EOS)
      return "EOS";
    if (code == V3SpecimenType.EYE)
      return "EYE";
    if (code == V3SpecimenType.FIB)
      return "FIB";
    if (code == V3SpecimenType.FIST)
      return "FIST";
    if (code == V3SpecimenType.FLT)
      return "FLT";
    if (code == V3SpecimenType.FLU)
      return "FLU";
    if (code == V3SpecimenType.FOOD)
      return "FOOD";
    if (code == V3SpecimenType.GAS)
      return "GAS";
    if (code == V3SpecimenType.GAST)
      return "GAST";
    if (code == V3SpecimenType.GEN)
      return "GEN";
    if (code == V3SpecimenType.GENC)
      return "GENC";
    if (code == V3SpecimenType.GENF)
      return "GENF";
    if (code == V3SpecimenType.GENL)
      return "GENL";
    if (code == V3SpecimenType.GENV)
      return "GENV";
    if (code == V3SpecimenType.HAR)
      return "HAR";
    if (code == V3SpecimenType.IHG)
      return "IHG";
    if (code == V3SpecimenType.ISLT)
      return "ISLT";
    if (code == V3SpecimenType.IT)
      return "IT";
    if (code == V3SpecimenType.LAM)
      return "LAM";
    if (code == V3SpecimenType.LIQ)
      return "LIQ";
    if (code == V3SpecimenType.LN)
      return "LN";
    if (code == V3SpecimenType.LNA)
      return "LNA";
    if (code == V3SpecimenType.LNV)
      return "LNV";
    if (code == V3SpecimenType.LYM)
      return "LYM";
    if (code == V3SpecimenType.MAC)
      return "MAC";
    if (code == V3SpecimenType.MAR)
      return "MAR";
    if (code == V3SpecimenType.MBLD)
      return "MBLD";
    if (code == V3SpecimenType.MEC)
      return "MEC";
    if (code == V3SpecimenType.MILK)
      return "MILK";
    if (code == V3SpecimenType.MLK)
      return "MLK";
    if (code == V3SpecimenType.NAIL)
      return "NAIL";
    if (code == V3SpecimenType.NOS)
      return "NOS";
    if (code == V3SpecimenType.PAFL)
      return "PAFL";
    if (code == V3SpecimenType.PAT)
      return "PAT";
    if (code == V3SpecimenType.PLAS)
      return "PLAS";
    if (code == V3SpecimenType.PLB)
      return "PLB";
    if (code == V3SpecimenType.PLC)
      return "PLC";
    if (code == V3SpecimenType.PLR)
      return "PLR";
    if (code == V3SpecimenType.PMN)
      return "PMN";
    if (code == V3SpecimenType.PPP)
      return "PPP";
    if (code == V3SpecimenType.PRP)
      return "PRP";
    if (code == V3SpecimenType.PRT)
      return "PRT";
    if (code == V3SpecimenType.PUS)
      return "PUS";
    if (code == V3SpecimenType.RBC)
      return "RBC";
    if (code == V3SpecimenType.SAL)
      return "SAL";
    if (code == V3SpecimenType.SER)
      return "SER";
    if (code == V3SpecimenType.SKM)
      return "SKM";
    if (code == V3SpecimenType.SKN)
      return "SKN";
    if (code == V3SpecimenType.SMN)
      return "SMN";
    if (code == V3SpecimenType.SMPLS)
      return "SMPLS";
    if (code == V3SpecimenType.SNV)
      return "SNV";
    if (code == V3SpecimenType.SPRM)
      return "SPRM";
    if (code == V3SpecimenType.SPT)
      return "SPT";
    if (code == V3SpecimenType.SPTC)
      return "SPTC";
    if (code == V3SpecimenType.SPTT)
      return "SPTT";
    if (code == V3SpecimenType.STL)
      return "STL";
    if (code == V3SpecimenType.SWT)
      return "SWT";
    if (code == V3SpecimenType.TEAR)
      return "TEAR";
    if (code == V3SpecimenType.THRB)
      return "THRB";
    if (code == V3SpecimenType.THRT)
      return "THRT";
    if (code == V3SpecimenType.TISG)
      return "TISG";
    if (code == V3SpecimenType.TISPL)
      return "TISPL";
    if (code == V3SpecimenType.TISS)
      return "TISS";
    if (code == V3SpecimenType.TISU)
      return "TISU";
    if (code == V3SpecimenType.TLGI)
      return "TLGI";
    if (code == V3SpecimenType.TLNG)
      return "TLNG";
    if (code == V3SpecimenType.TSMI)
      return "TSMI";
    if (code == V3SpecimenType.TUB)
      return "TUB";
    if (code == V3SpecimenType.ULC)
      return "ULC";
    if (code == V3SpecimenType.UMB)
      return "UMB";
    if (code == V3SpecimenType.UMED)
      return "UMED";
    if (code == V3SpecimenType.UR)
      return "UR";
    if (code == V3SpecimenType.URC)
      return "URC";
    if (code == V3SpecimenType.URNS)
      return "URNS";
    if (code == V3SpecimenType.URT)
      return "URT";
    if (code == V3SpecimenType.URTH)
      return "URTH";
    if (code == V3SpecimenType.USUB)
      return "USUB";
    if (code == V3SpecimenType.VOM)
      return "VOM";
    if (code == V3SpecimenType.WAT)
      return "WAT";
    if (code == V3SpecimenType.WBC)
      return "WBC";
    if (code == V3SpecimenType.WICK)
      return "WICK";
    if (code == V3SpecimenType.WND)
      return "WND";
    if (code == V3SpecimenType.WNDA)
      return "WNDA";
    if (code == V3SpecimenType.WNDD)
      return "WNDD";
    if (code == V3SpecimenType.WNDE)
      return "WNDE";
    return "?";
  }

    public String toSystem(V3SpecimenType code) {
      return code.getSystem();
      }

}

