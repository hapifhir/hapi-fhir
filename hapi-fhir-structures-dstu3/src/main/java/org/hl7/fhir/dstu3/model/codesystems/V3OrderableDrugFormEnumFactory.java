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

public class V3OrderableDrugFormEnumFactory implements EnumFactory<V3OrderableDrugForm> {

  public V3OrderableDrugForm fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_AdministrableDrugForm".equals(codeString))
      return V3OrderableDrugForm._ADMINISTRABLEDRUGFORM;
    if ("APPFUL".equals(codeString))
      return V3OrderableDrugForm.APPFUL;
    if ("DROP".equals(codeString))
      return V3OrderableDrugForm.DROP;
    if ("NDROP".equals(codeString))
      return V3OrderableDrugForm.NDROP;
    if ("OPDROP".equals(codeString))
      return V3OrderableDrugForm.OPDROP;
    if ("ORDROP".equals(codeString))
      return V3OrderableDrugForm.ORDROP;
    if ("OTDROP".equals(codeString))
      return V3OrderableDrugForm.OTDROP;
    if ("PUFF".equals(codeString))
      return V3OrderableDrugForm.PUFF;
    if ("SCOOP".equals(codeString))
      return V3OrderableDrugForm.SCOOP;
    if ("SPRY".equals(codeString))
      return V3OrderableDrugForm.SPRY;
    if ("_DispensableDrugForm".equals(codeString))
      return V3OrderableDrugForm._DISPENSABLEDRUGFORM;
    if ("_GasDrugForm".equals(codeString))
      return V3OrderableDrugForm._GASDRUGFORM;
    if ("GASINHL".equals(codeString))
      return V3OrderableDrugForm.GASINHL;
    if ("_GasLiquidMixture".equals(codeString))
      return V3OrderableDrugForm._GASLIQUIDMIXTURE;
    if ("AER".equals(codeString))
      return V3OrderableDrugForm.AER;
    if ("BAINHL".equals(codeString))
      return V3OrderableDrugForm.BAINHL;
    if ("INHLSOL".equals(codeString))
      return V3OrderableDrugForm.INHLSOL;
    if ("MDINHL".equals(codeString))
      return V3OrderableDrugForm.MDINHL;
    if ("NASSPRY".equals(codeString))
      return V3OrderableDrugForm.NASSPRY;
    if ("DERMSPRY".equals(codeString))
      return V3OrderableDrugForm.DERMSPRY;
    if ("FOAM".equals(codeString))
      return V3OrderableDrugForm.FOAM;
    if ("FOAMAPL".equals(codeString))
      return V3OrderableDrugForm.FOAMAPL;
    if ("RECFORM".equals(codeString))
      return V3OrderableDrugForm.RECFORM;
    if ("VAGFOAM".equals(codeString))
      return V3OrderableDrugForm.VAGFOAM;
    if ("VAGFOAMAPL".equals(codeString))
      return V3OrderableDrugForm.VAGFOAMAPL;
    if ("RECSPRY".equals(codeString))
      return V3OrderableDrugForm.RECSPRY;
    if ("VAGSPRY".equals(codeString))
      return V3OrderableDrugForm.VAGSPRY;
    if ("_GasSolidSpray".equals(codeString))
      return V3OrderableDrugForm._GASSOLIDSPRAY;
    if ("INHL".equals(codeString))
      return V3OrderableDrugForm.INHL;
    if ("BAINHLPWD".equals(codeString))
      return V3OrderableDrugForm.BAINHLPWD;
    if ("INHLPWD".equals(codeString))
      return V3OrderableDrugForm.INHLPWD;
    if ("MDINHLPWD".equals(codeString))
      return V3OrderableDrugForm.MDINHLPWD;
    if ("NASINHL".equals(codeString))
      return V3OrderableDrugForm.NASINHL;
    if ("ORINHL".equals(codeString))
      return V3OrderableDrugForm.ORINHL;
    if ("PWDSPRY".equals(codeString))
      return V3OrderableDrugForm.PWDSPRY;
    if ("SPRYADAPT".equals(codeString))
      return V3OrderableDrugForm.SPRYADAPT;
    if ("_Liquid".equals(codeString))
      return V3OrderableDrugForm._LIQUID;
    if ("LIQCLN".equals(codeString))
      return V3OrderableDrugForm.LIQCLN;
    if ("LIQSOAP".equals(codeString))
      return V3OrderableDrugForm.LIQSOAP;
    if ("SHMP".equals(codeString))
      return V3OrderableDrugForm.SHMP;
    if ("OIL".equals(codeString))
      return V3OrderableDrugForm.OIL;
    if ("TOPOIL".equals(codeString))
      return V3OrderableDrugForm.TOPOIL;
    if ("SOL".equals(codeString))
      return V3OrderableDrugForm.SOL;
    if ("IPSOL".equals(codeString))
      return V3OrderableDrugForm.IPSOL;
    if ("IRSOL".equals(codeString))
      return V3OrderableDrugForm.IRSOL;
    if ("DOUCHE".equals(codeString))
      return V3OrderableDrugForm.DOUCHE;
    if ("ENEMA".equals(codeString))
      return V3OrderableDrugForm.ENEMA;
    if ("OPIRSOL".equals(codeString))
      return V3OrderableDrugForm.OPIRSOL;
    if ("IVSOL".equals(codeString))
      return V3OrderableDrugForm.IVSOL;
    if ("ORALSOL".equals(codeString))
      return V3OrderableDrugForm.ORALSOL;
    if ("ELIXIR".equals(codeString))
      return V3OrderableDrugForm.ELIXIR;
    if ("RINSE".equals(codeString))
      return V3OrderableDrugForm.RINSE;
    if ("SYRUP".equals(codeString))
      return V3OrderableDrugForm.SYRUP;
    if ("RECSOL".equals(codeString))
      return V3OrderableDrugForm.RECSOL;
    if ("TOPSOL".equals(codeString))
      return V3OrderableDrugForm.TOPSOL;
    if ("LIN".equals(codeString))
      return V3OrderableDrugForm.LIN;
    if ("MUCTOPSOL".equals(codeString))
      return V3OrderableDrugForm.MUCTOPSOL;
    if ("TINC".equals(codeString))
      return V3OrderableDrugForm.TINC;
    if ("_LiquidLiquidEmulsion".equals(codeString))
      return V3OrderableDrugForm._LIQUIDLIQUIDEMULSION;
    if ("CRM".equals(codeString))
      return V3OrderableDrugForm.CRM;
    if ("NASCRM".equals(codeString))
      return V3OrderableDrugForm.NASCRM;
    if ("OPCRM".equals(codeString))
      return V3OrderableDrugForm.OPCRM;
    if ("ORCRM".equals(codeString))
      return V3OrderableDrugForm.ORCRM;
    if ("OTCRM".equals(codeString))
      return V3OrderableDrugForm.OTCRM;
    if ("RECCRM".equals(codeString))
      return V3OrderableDrugForm.RECCRM;
    if ("TOPCRM".equals(codeString))
      return V3OrderableDrugForm.TOPCRM;
    if ("VAGCRM".equals(codeString))
      return V3OrderableDrugForm.VAGCRM;
    if ("VAGCRMAPL".equals(codeString))
      return V3OrderableDrugForm.VAGCRMAPL;
    if ("LTN".equals(codeString))
      return V3OrderableDrugForm.LTN;
    if ("TOPLTN".equals(codeString))
      return V3OrderableDrugForm.TOPLTN;
    if ("OINT".equals(codeString))
      return V3OrderableDrugForm.OINT;
    if ("NASOINT".equals(codeString))
      return V3OrderableDrugForm.NASOINT;
    if ("OINTAPL".equals(codeString))
      return V3OrderableDrugForm.OINTAPL;
    if ("OPOINT".equals(codeString))
      return V3OrderableDrugForm.OPOINT;
    if ("OTOINT".equals(codeString))
      return V3OrderableDrugForm.OTOINT;
    if ("RECOINT".equals(codeString))
      return V3OrderableDrugForm.RECOINT;
    if ("TOPOINT".equals(codeString))
      return V3OrderableDrugForm.TOPOINT;
    if ("VAGOINT".equals(codeString))
      return V3OrderableDrugForm.VAGOINT;
    if ("VAGOINTAPL".equals(codeString))
      return V3OrderableDrugForm.VAGOINTAPL;
    if ("_LiquidSolidSuspension".equals(codeString))
      return V3OrderableDrugForm._LIQUIDSOLIDSUSPENSION;
    if ("GEL".equals(codeString))
      return V3OrderableDrugForm.GEL;
    if ("GELAPL".equals(codeString))
      return V3OrderableDrugForm.GELAPL;
    if ("NASGEL".equals(codeString))
      return V3OrderableDrugForm.NASGEL;
    if ("OPGEL".equals(codeString))
      return V3OrderableDrugForm.OPGEL;
    if ("OTGEL".equals(codeString))
      return V3OrderableDrugForm.OTGEL;
    if ("TOPGEL".equals(codeString))
      return V3OrderableDrugForm.TOPGEL;
    if ("URETHGEL".equals(codeString))
      return V3OrderableDrugForm.URETHGEL;
    if ("VAGGEL".equals(codeString))
      return V3OrderableDrugForm.VAGGEL;
    if ("VGELAPL".equals(codeString))
      return V3OrderableDrugForm.VGELAPL;
    if ("PASTE".equals(codeString))
      return V3OrderableDrugForm.PASTE;
    if ("PUD".equals(codeString))
      return V3OrderableDrugForm.PUD;
    if ("TPASTE".equals(codeString))
      return V3OrderableDrugForm.TPASTE;
    if ("SUSP".equals(codeString))
      return V3OrderableDrugForm.SUSP;
    if ("ITSUSP".equals(codeString))
      return V3OrderableDrugForm.ITSUSP;
    if ("OPSUSP".equals(codeString))
      return V3OrderableDrugForm.OPSUSP;
    if ("ORSUSP".equals(codeString))
      return V3OrderableDrugForm.ORSUSP;
    if ("ERSUSP".equals(codeString))
      return V3OrderableDrugForm.ERSUSP;
    if ("ERSUSP12".equals(codeString))
      return V3OrderableDrugForm.ERSUSP12;
    if ("ERSUSP24".equals(codeString))
      return V3OrderableDrugForm.ERSUSP24;
    if ("OTSUSP".equals(codeString))
      return V3OrderableDrugForm.OTSUSP;
    if ("RECSUSP".equals(codeString))
      return V3OrderableDrugForm.RECSUSP;
    if ("_SolidDrugForm".equals(codeString))
      return V3OrderableDrugForm._SOLIDDRUGFORM;
    if ("BAR".equals(codeString))
      return V3OrderableDrugForm.BAR;
    if ("BARSOAP".equals(codeString))
      return V3OrderableDrugForm.BARSOAP;
    if ("MEDBAR".equals(codeString))
      return V3OrderableDrugForm.MEDBAR;
    if ("CHEWBAR".equals(codeString))
      return V3OrderableDrugForm.CHEWBAR;
    if ("BEAD".equals(codeString))
      return V3OrderableDrugForm.BEAD;
    if ("CAKE".equals(codeString))
      return V3OrderableDrugForm.CAKE;
    if ("CEMENT".equals(codeString))
      return V3OrderableDrugForm.CEMENT;
    if ("CRYS".equals(codeString))
      return V3OrderableDrugForm.CRYS;
    if ("DISK".equals(codeString))
      return V3OrderableDrugForm.DISK;
    if ("FLAKE".equals(codeString))
      return V3OrderableDrugForm.FLAKE;
    if ("GRAN".equals(codeString))
      return V3OrderableDrugForm.GRAN;
    if ("GUM".equals(codeString))
      return V3OrderableDrugForm.GUM;
    if ("PAD".equals(codeString))
      return V3OrderableDrugForm.PAD;
    if ("MEDPAD".equals(codeString))
      return V3OrderableDrugForm.MEDPAD;
    if ("PATCH".equals(codeString))
      return V3OrderableDrugForm.PATCH;
    if ("TPATCH".equals(codeString))
      return V3OrderableDrugForm.TPATCH;
    if ("TPATH16".equals(codeString))
      return V3OrderableDrugForm.TPATH16;
    if ("TPATH24".equals(codeString))
      return V3OrderableDrugForm.TPATH24;
    if ("TPATH2WK".equals(codeString))
      return V3OrderableDrugForm.TPATH2WK;
    if ("TPATH72".equals(codeString))
      return V3OrderableDrugForm.TPATH72;
    if ("TPATHWK".equals(codeString))
      return V3OrderableDrugForm.TPATHWK;
    if ("PELLET".equals(codeString))
      return V3OrderableDrugForm.PELLET;
    if ("PILL".equals(codeString))
      return V3OrderableDrugForm.PILL;
    if ("CAP".equals(codeString))
      return V3OrderableDrugForm.CAP;
    if ("ORCAP".equals(codeString))
      return V3OrderableDrugForm.ORCAP;
    if ("ENTCAP".equals(codeString))
      return V3OrderableDrugForm.ENTCAP;
    if ("ERENTCAP".equals(codeString))
      return V3OrderableDrugForm.ERENTCAP;
    if ("ERCAP".equals(codeString))
      return V3OrderableDrugForm.ERCAP;
    if ("ERCAP12".equals(codeString))
      return V3OrderableDrugForm.ERCAP12;
    if ("ERCAP24".equals(codeString))
      return V3OrderableDrugForm.ERCAP24;
    if ("ERECCAP".equals(codeString))
      return V3OrderableDrugForm.ERECCAP;
    if ("TAB".equals(codeString))
      return V3OrderableDrugForm.TAB;
    if ("ORTAB".equals(codeString))
      return V3OrderableDrugForm.ORTAB;
    if ("BUCTAB".equals(codeString))
      return V3OrderableDrugForm.BUCTAB;
    if ("SRBUCTAB".equals(codeString))
      return V3OrderableDrugForm.SRBUCTAB;
    if ("CAPLET".equals(codeString))
      return V3OrderableDrugForm.CAPLET;
    if ("CHEWTAB".equals(codeString))
      return V3OrderableDrugForm.CHEWTAB;
    if ("CPTAB".equals(codeString))
      return V3OrderableDrugForm.CPTAB;
    if ("DISINTAB".equals(codeString))
      return V3OrderableDrugForm.DISINTAB;
    if ("DRTAB".equals(codeString))
      return V3OrderableDrugForm.DRTAB;
    if ("ECTAB".equals(codeString))
      return V3OrderableDrugForm.ECTAB;
    if ("ERECTAB".equals(codeString))
      return V3OrderableDrugForm.ERECTAB;
    if ("ERTAB".equals(codeString))
      return V3OrderableDrugForm.ERTAB;
    if ("ERTAB12".equals(codeString))
      return V3OrderableDrugForm.ERTAB12;
    if ("ERTAB24".equals(codeString))
      return V3OrderableDrugForm.ERTAB24;
    if ("ORTROCHE".equals(codeString))
      return V3OrderableDrugForm.ORTROCHE;
    if ("SLTAB".equals(codeString))
      return V3OrderableDrugForm.SLTAB;
    if ("VAGTAB".equals(codeString))
      return V3OrderableDrugForm.VAGTAB;
    if ("POWD".equals(codeString))
      return V3OrderableDrugForm.POWD;
    if ("TOPPWD".equals(codeString))
      return V3OrderableDrugForm.TOPPWD;
    if ("RECPWD".equals(codeString))
      return V3OrderableDrugForm.RECPWD;
    if ("VAGPWD".equals(codeString))
      return V3OrderableDrugForm.VAGPWD;
    if ("SUPP".equals(codeString))
      return V3OrderableDrugForm.SUPP;
    if ("RECSUPP".equals(codeString))
      return V3OrderableDrugForm.RECSUPP;
    if ("URETHSUPP".equals(codeString))
      return V3OrderableDrugForm.URETHSUPP;
    if ("VAGSUPP".equals(codeString))
      return V3OrderableDrugForm.VAGSUPP;
    if ("SWAB".equals(codeString))
      return V3OrderableDrugForm.SWAB;
    if ("MEDSWAB".equals(codeString))
      return V3OrderableDrugForm.MEDSWAB;
    if ("WAFER".equals(codeString))
      return V3OrderableDrugForm.WAFER;
    throw new IllegalArgumentException("Unknown V3OrderableDrugForm code '"+codeString+"'");
  }

  public String toCode(V3OrderableDrugForm code) {
    if (code == V3OrderableDrugForm._ADMINISTRABLEDRUGFORM)
      return "_AdministrableDrugForm";
    if (code == V3OrderableDrugForm.APPFUL)
      return "APPFUL";
    if (code == V3OrderableDrugForm.DROP)
      return "DROP";
    if (code == V3OrderableDrugForm.NDROP)
      return "NDROP";
    if (code == V3OrderableDrugForm.OPDROP)
      return "OPDROP";
    if (code == V3OrderableDrugForm.ORDROP)
      return "ORDROP";
    if (code == V3OrderableDrugForm.OTDROP)
      return "OTDROP";
    if (code == V3OrderableDrugForm.PUFF)
      return "PUFF";
    if (code == V3OrderableDrugForm.SCOOP)
      return "SCOOP";
    if (code == V3OrderableDrugForm.SPRY)
      return "SPRY";
    if (code == V3OrderableDrugForm._DISPENSABLEDRUGFORM)
      return "_DispensableDrugForm";
    if (code == V3OrderableDrugForm._GASDRUGFORM)
      return "_GasDrugForm";
    if (code == V3OrderableDrugForm.GASINHL)
      return "GASINHL";
    if (code == V3OrderableDrugForm._GASLIQUIDMIXTURE)
      return "_GasLiquidMixture";
    if (code == V3OrderableDrugForm.AER)
      return "AER";
    if (code == V3OrderableDrugForm.BAINHL)
      return "BAINHL";
    if (code == V3OrderableDrugForm.INHLSOL)
      return "INHLSOL";
    if (code == V3OrderableDrugForm.MDINHL)
      return "MDINHL";
    if (code == V3OrderableDrugForm.NASSPRY)
      return "NASSPRY";
    if (code == V3OrderableDrugForm.DERMSPRY)
      return "DERMSPRY";
    if (code == V3OrderableDrugForm.FOAM)
      return "FOAM";
    if (code == V3OrderableDrugForm.FOAMAPL)
      return "FOAMAPL";
    if (code == V3OrderableDrugForm.RECFORM)
      return "RECFORM";
    if (code == V3OrderableDrugForm.VAGFOAM)
      return "VAGFOAM";
    if (code == V3OrderableDrugForm.VAGFOAMAPL)
      return "VAGFOAMAPL";
    if (code == V3OrderableDrugForm.RECSPRY)
      return "RECSPRY";
    if (code == V3OrderableDrugForm.VAGSPRY)
      return "VAGSPRY";
    if (code == V3OrderableDrugForm._GASSOLIDSPRAY)
      return "_GasSolidSpray";
    if (code == V3OrderableDrugForm.INHL)
      return "INHL";
    if (code == V3OrderableDrugForm.BAINHLPWD)
      return "BAINHLPWD";
    if (code == V3OrderableDrugForm.INHLPWD)
      return "INHLPWD";
    if (code == V3OrderableDrugForm.MDINHLPWD)
      return "MDINHLPWD";
    if (code == V3OrderableDrugForm.NASINHL)
      return "NASINHL";
    if (code == V3OrderableDrugForm.ORINHL)
      return "ORINHL";
    if (code == V3OrderableDrugForm.PWDSPRY)
      return "PWDSPRY";
    if (code == V3OrderableDrugForm.SPRYADAPT)
      return "SPRYADAPT";
    if (code == V3OrderableDrugForm._LIQUID)
      return "_Liquid";
    if (code == V3OrderableDrugForm.LIQCLN)
      return "LIQCLN";
    if (code == V3OrderableDrugForm.LIQSOAP)
      return "LIQSOAP";
    if (code == V3OrderableDrugForm.SHMP)
      return "SHMP";
    if (code == V3OrderableDrugForm.OIL)
      return "OIL";
    if (code == V3OrderableDrugForm.TOPOIL)
      return "TOPOIL";
    if (code == V3OrderableDrugForm.SOL)
      return "SOL";
    if (code == V3OrderableDrugForm.IPSOL)
      return "IPSOL";
    if (code == V3OrderableDrugForm.IRSOL)
      return "IRSOL";
    if (code == V3OrderableDrugForm.DOUCHE)
      return "DOUCHE";
    if (code == V3OrderableDrugForm.ENEMA)
      return "ENEMA";
    if (code == V3OrderableDrugForm.OPIRSOL)
      return "OPIRSOL";
    if (code == V3OrderableDrugForm.IVSOL)
      return "IVSOL";
    if (code == V3OrderableDrugForm.ORALSOL)
      return "ORALSOL";
    if (code == V3OrderableDrugForm.ELIXIR)
      return "ELIXIR";
    if (code == V3OrderableDrugForm.RINSE)
      return "RINSE";
    if (code == V3OrderableDrugForm.SYRUP)
      return "SYRUP";
    if (code == V3OrderableDrugForm.RECSOL)
      return "RECSOL";
    if (code == V3OrderableDrugForm.TOPSOL)
      return "TOPSOL";
    if (code == V3OrderableDrugForm.LIN)
      return "LIN";
    if (code == V3OrderableDrugForm.MUCTOPSOL)
      return "MUCTOPSOL";
    if (code == V3OrderableDrugForm.TINC)
      return "TINC";
    if (code == V3OrderableDrugForm._LIQUIDLIQUIDEMULSION)
      return "_LiquidLiquidEmulsion";
    if (code == V3OrderableDrugForm.CRM)
      return "CRM";
    if (code == V3OrderableDrugForm.NASCRM)
      return "NASCRM";
    if (code == V3OrderableDrugForm.OPCRM)
      return "OPCRM";
    if (code == V3OrderableDrugForm.ORCRM)
      return "ORCRM";
    if (code == V3OrderableDrugForm.OTCRM)
      return "OTCRM";
    if (code == V3OrderableDrugForm.RECCRM)
      return "RECCRM";
    if (code == V3OrderableDrugForm.TOPCRM)
      return "TOPCRM";
    if (code == V3OrderableDrugForm.VAGCRM)
      return "VAGCRM";
    if (code == V3OrderableDrugForm.VAGCRMAPL)
      return "VAGCRMAPL";
    if (code == V3OrderableDrugForm.LTN)
      return "LTN";
    if (code == V3OrderableDrugForm.TOPLTN)
      return "TOPLTN";
    if (code == V3OrderableDrugForm.OINT)
      return "OINT";
    if (code == V3OrderableDrugForm.NASOINT)
      return "NASOINT";
    if (code == V3OrderableDrugForm.OINTAPL)
      return "OINTAPL";
    if (code == V3OrderableDrugForm.OPOINT)
      return "OPOINT";
    if (code == V3OrderableDrugForm.OTOINT)
      return "OTOINT";
    if (code == V3OrderableDrugForm.RECOINT)
      return "RECOINT";
    if (code == V3OrderableDrugForm.TOPOINT)
      return "TOPOINT";
    if (code == V3OrderableDrugForm.VAGOINT)
      return "VAGOINT";
    if (code == V3OrderableDrugForm.VAGOINTAPL)
      return "VAGOINTAPL";
    if (code == V3OrderableDrugForm._LIQUIDSOLIDSUSPENSION)
      return "_LiquidSolidSuspension";
    if (code == V3OrderableDrugForm.GEL)
      return "GEL";
    if (code == V3OrderableDrugForm.GELAPL)
      return "GELAPL";
    if (code == V3OrderableDrugForm.NASGEL)
      return "NASGEL";
    if (code == V3OrderableDrugForm.OPGEL)
      return "OPGEL";
    if (code == V3OrderableDrugForm.OTGEL)
      return "OTGEL";
    if (code == V3OrderableDrugForm.TOPGEL)
      return "TOPGEL";
    if (code == V3OrderableDrugForm.URETHGEL)
      return "URETHGEL";
    if (code == V3OrderableDrugForm.VAGGEL)
      return "VAGGEL";
    if (code == V3OrderableDrugForm.VGELAPL)
      return "VGELAPL";
    if (code == V3OrderableDrugForm.PASTE)
      return "PASTE";
    if (code == V3OrderableDrugForm.PUD)
      return "PUD";
    if (code == V3OrderableDrugForm.TPASTE)
      return "TPASTE";
    if (code == V3OrderableDrugForm.SUSP)
      return "SUSP";
    if (code == V3OrderableDrugForm.ITSUSP)
      return "ITSUSP";
    if (code == V3OrderableDrugForm.OPSUSP)
      return "OPSUSP";
    if (code == V3OrderableDrugForm.ORSUSP)
      return "ORSUSP";
    if (code == V3OrderableDrugForm.ERSUSP)
      return "ERSUSP";
    if (code == V3OrderableDrugForm.ERSUSP12)
      return "ERSUSP12";
    if (code == V3OrderableDrugForm.ERSUSP24)
      return "ERSUSP24";
    if (code == V3OrderableDrugForm.OTSUSP)
      return "OTSUSP";
    if (code == V3OrderableDrugForm.RECSUSP)
      return "RECSUSP";
    if (code == V3OrderableDrugForm._SOLIDDRUGFORM)
      return "_SolidDrugForm";
    if (code == V3OrderableDrugForm.BAR)
      return "BAR";
    if (code == V3OrderableDrugForm.BARSOAP)
      return "BARSOAP";
    if (code == V3OrderableDrugForm.MEDBAR)
      return "MEDBAR";
    if (code == V3OrderableDrugForm.CHEWBAR)
      return "CHEWBAR";
    if (code == V3OrderableDrugForm.BEAD)
      return "BEAD";
    if (code == V3OrderableDrugForm.CAKE)
      return "CAKE";
    if (code == V3OrderableDrugForm.CEMENT)
      return "CEMENT";
    if (code == V3OrderableDrugForm.CRYS)
      return "CRYS";
    if (code == V3OrderableDrugForm.DISK)
      return "DISK";
    if (code == V3OrderableDrugForm.FLAKE)
      return "FLAKE";
    if (code == V3OrderableDrugForm.GRAN)
      return "GRAN";
    if (code == V3OrderableDrugForm.GUM)
      return "GUM";
    if (code == V3OrderableDrugForm.PAD)
      return "PAD";
    if (code == V3OrderableDrugForm.MEDPAD)
      return "MEDPAD";
    if (code == V3OrderableDrugForm.PATCH)
      return "PATCH";
    if (code == V3OrderableDrugForm.TPATCH)
      return "TPATCH";
    if (code == V3OrderableDrugForm.TPATH16)
      return "TPATH16";
    if (code == V3OrderableDrugForm.TPATH24)
      return "TPATH24";
    if (code == V3OrderableDrugForm.TPATH2WK)
      return "TPATH2WK";
    if (code == V3OrderableDrugForm.TPATH72)
      return "TPATH72";
    if (code == V3OrderableDrugForm.TPATHWK)
      return "TPATHWK";
    if (code == V3OrderableDrugForm.PELLET)
      return "PELLET";
    if (code == V3OrderableDrugForm.PILL)
      return "PILL";
    if (code == V3OrderableDrugForm.CAP)
      return "CAP";
    if (code == V3OrderableDrugForm.ORCAP)
      return "ORCAP";
    if (code == V3OrderableDrugForm.ENTCAP)
      return "ENTCAP";
    if (code == V3OrderableDrugForm.ERENTCAP)
      return "ERENTCAP";
    if (code == V3OrderableDrugForm.ERCAP)
      return "ERCAP";
    if (code == V3OrderableDrugForm.ERCAP12)
      return "ERCAP12";
    if (code == V3OrderableDrugForm.ERCAP24)
      return "ERCAP24";
    if (code == V3OrderableDrugForm.ERECCAP)
      return "ERECCAP";
    if (code == V3OrderableDrugForm.TAB)
      return "TAB";
    if (code == V3OrderableDrugForm.ORTAB)
      return "ORTAB";
    if (code == V3OrderableDrugForm.BUCTAB)
      return "BUCTAB";
    if (code == V3OrderableDrugForm.SRBUCTAB)
      return "SRBUCTAB";
    if (code == V3OrderableDrugForm.CAPLET)
      return "CAPLET";
    if (code == V3OrderableDrugForm.CHEWTAB)
      return "CHEWTAB";
    if (code == V3OrderableDrugForm.CPTAB)
      return "CPTAB";
    if (code == V3OrderableDrugForm.DISINTAB)
      return "DISINTAB";
    if (code == V3OrderableDrugForm.DRTAB)
      return "DRTAB";
    if (code == V3OrderableDrugForm.ECTAB)
      return "ECTAB";
    if (code == V3OrderableDrugForm.ERECTAB)
      return "ERECTAB";
    if (code == V3OrderableDrugForm.ERTAB)
      return "ERTAB";
    if (code == V3OrderableDrugForm.ERTAB12)
      return "ERTAB12";
    if (code == V3OrderableDrugForm.ERTAB24)
      return "ERTAB24";
    if (code == V3OrderableDrugForm.ORTROCHE)
      return "ORTROCHE";
    if (code == V3OrderableDrugForm.SLTAB)
      return "SLTAB";
    if (code == V3OrderableDrugForm.VAGTAB)
      return "VAGTAB";
    if (code == V3OrderableDrugForm.POWD)
      return "POWD";
    if (code == V3OrderableDrugForm.TOPPWD)
      return "TOPPWD";
    if (code == V3OrderableDrugForm.RECPWD)
      return "RECPWD";
    if (code == V3OrderableDrugForm.VAGPWD)
      return "VAGPWD";
    if (code == V3OrderableDrugForm.SUPP)
      return "SUPP";
    if (code == V3OrderableDrugForm.RECSUPP)
      return "RECSUPP";
    if (code == V3OrderableDrugForm.URETHSUPP)
      return "URETHSUPP";
    if (code == V3OrderableDrugForm.VAGSUPP)
      return "VAGSUPP";
    if (code == V3OrderableDrugForm.SWAB)
      return "SWAB";
    if (code == V3OrderableDrugForm.MEDSWAB)
      return "MEDSWAB";
    if (code == V3OrderableDrugForm.WAFER)
      return "WAFER";
    return "?";
  }

    public String toSystem(V3OrderableDrugForm code) {
      return code.getSystem();
      }

}

