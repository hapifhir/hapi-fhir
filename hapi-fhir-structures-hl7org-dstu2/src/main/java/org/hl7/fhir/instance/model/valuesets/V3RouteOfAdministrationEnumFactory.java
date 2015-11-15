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

public class V3RouteOfAdministrationEnumFactory implements EnumFactory<V3RouteOfAdministration> {

  public V3RouteOfAdministration fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_RouteByMethod".equals(codeString))
      return V3RouteOfAdministration._ROUTEBYMETHOD;
    if ("SOAK".equals(codeString))
      return V3RouteOfAdministration.SOAK;
    if ("SHAMPOO".equals(codeString))
      return V3RouteOfAdministration.SHAMPOO;
    if ("TRNSLING".equals(codeString))
      return V3RouteOfAdministration.TRNSLING;
    if ("PO".equals(codeString))
      return V3RouteOfAdministration.PO;
    if ("GARGLE".equals(codeString))
      return V3RouteOfAdministration.GARGLE;
    if ("SUCK".equals(codeString))
      return V3RouteOfAdministration.SUCK;
    if ("_Chew".equals(codeString))
      return V3RouteOfAdministration._CHEW;
    if ("CHEW".equals(codeString))
      return V3RouteOfAdministration.CHEW;
    if ("_Diffusion".equals(codeString))
      return V3RouteOfAdministration._DIFFUSION;
    if ("EXTCORPDIF".equals(codeString))
      return V3RouteOfAdministration.EXTCORPDIF;
    if ("HEMODIFF".equals(codeString))
      return V3RouteOfAdministration.HEMODIFF;
    if ("TRNSDERMD".equals(codeString))
      return V3RouteOfAdministration.TRNSDERMD;
    if ("_Dissolve".equals(codeString))
      return V3RouteOfAdministration._DISSOLVE;
    if ("DISSOLVE".equals(codeString))
      return V3RouteOfAdministration.DISSOLVE;
    if ("SL".equals(codeString))
      return V3RouteOfAdministration.SL;
    if ("_Douche".equals(codeString))
      return V3RouteOfAdministration._DOUCHE;
    if ("DOUCHE".equals(codeString))
      return V3RouteOfAdministration.DOUCHE;
    if ("_ElectroOsmosisRoute".equals(codeString))
      return V3RouteOfAdministration._ELECTROOSMOSISROUTE;
    if ("ELECTOSMOS".equals(codeString))
      return V3RouteOfAdministration.ELECTOSMOS;
    if ("_Enema".equals(codeString))
      return V3RouteOfAdministration._ENEMA;
    if ("ENEMA".equals(codeString))
      return V3RouteOfAdministration.ENEMA;
    if ("RETENEMA".equals(codeString))
      return V3RouteOfAdministration.RETENEMA;
    if ("_Flush".equals(codeString))
      return V3RouteOfAdministration._FLUSH;
    if ("IVFLUSH".equals(codeString))
      return V3RouteOfAdministration.IVFLUSH;
    if ("_Implantation".equals(codeString))
      return V3RouteOfAdministration._IMPLANTATION;
    if ("IDIMPLNT".equals(codeString))
      return V3RouteOfAdministration.IDIMPLNT;
    if ("IVITIMPLNT".equals(codeString))
      return V3RouteOfAdministration.IVITIMPLNT;
    if ("SQIMPLNT".equals(codeString))
      return V3RouteOfAdministration.SQIMPLNT;
    if ("_Infusion".equals(codeString))
      return V3RouteOfAdministration._INFUSION;
    if ("EPI".equals(codeString))
      return V3RouteOfAdministration.EPI;
    if ("IA".equals(codeString))
      return V3RouteOfAdministration.IA;
    if ("IC".equals(codeString))
      return V3RouteOfAdministration.IC;
    if ("ICOR".equals(codeString))
      return V3RouteOfAdministration.ICOR;
    if ("IOSSC".equals(codeString))
      return V3RouteOfAdministration.IOSSC;
    if ("IT".equals(codeString))
      return V3RouteOfAdministration.IT;
    if ("IV".equals(codeString))
      return V3RouteOfAdministration.IV;
    if ("IVC".equals(codeString))
      return V3RouteOfAdministration.IVC;
    if ("IVCC".equals(codeString))
      return V3RouteOfAdministration.IVCC;
    if ("IVCI".equals(codeString))
      return V3RouteOfAdministration.IVCI;
    if ("PCA".equals(codeString))
      return V3RouteOfAdministration.PCA;
    if ("IVASCINFUS".equals(codeString))
      return V3RouteOfAdministration.IVASCINFUS;
    if ("SQINFUS".equals(codeString))
      return V3RouteOfAdministration.SQINFUS;
    if ("_Inhalation".equals(codeString))
      return V3RouteOfAdministration._INHALATION;
    if ("IPINHL".equals(codeString))
      return V3RouteOfAdministration.IPINHL;
    if ("ORIFINHL".equals(codeString))
      return V3RouteOfAdministration.ORIFINHL;
    if ("REBREATH".equals(codeString))
      return V3RouteOfAdministration.REBREATH;
    if ("IPPB".equals(codeString))
      return V3RouteOfAdministration.IPPB;
    if ("NASINHL".equals(codeString))
      return V3RouteOfAdministration.NASINHL;
    if ("NASINHLC".equals(codeString))
      return V3RouteOfAdministration.NASINHLC;
    if ("NEB".equals(codeString))
      return V3RouteOfAdministration.NEB;
    if ("NASNEB".equals(codeString))
      return V3RouteOfAdministration.NASNEB;
    if ("ORNEB".equals(codeString))
      return V3RouteOfAdministration.ORNEB;
    if ("TRACH".equals(codeString))
      return V3RouteOfAdministration.TRACH;
    if ("VENT".equals(codeString))
      return V3RouteOfAdministration.VENT;
    if ("VENTMASK".equals(codeString))
      return V3RouteOfAdministration.VENTMASK;
    if ("_Injection".equals(codeString))
      return V3RouteOfAdministration._INJECTION;
    if ("AMNINJ".equals(codeString))
      return V3RouteOfAdministration.AMNINJ;
    if ("BILINJ".equals(codeString))
      return V3RouteOfAdministration.BILINJ;
    if ("CHOLINJ".equals(codeString))
      return V3RouteOfAdministration.CHOLINJ;
    if ("CERVINJ".equals(codeString))
      return V3RouteOfAdministration.CERVINJ;
    if ("EPIDURINJ".equals(codeString))
      return V3RouteOfAdministration.EPIDURINJ;
    if ("EPIINJ".equals(codeString))
      return V3RouteOfAdministration.EPIINJ;
    if ("EPINJSP".equals(codeString))
      return V3RouteOfAdministration.EPINJSP;
    if ("EXTRAMNINJ".equals(codeString))
      return V3RouteOfAdministration.EXTRAMNINJ;
    if ("EXTCORPINJ".equals(codeString))
      return V3RouteOfAdministration.EXTCORPINJ;
    if ("GBINJ".equals(codeString))
      return V3RouteOfAdministration.GBINJ;
    if ("GINGINJ".equals(codeString))
      return V3RouteOfAdministration.GINGINJ;
    if ("BLADINJ".equals(codeString))
      return V3RouteOfAdministration.BLADINJ;
    if ("ENDOSININJ".equals(codeString))
      return V3RouteOfAdministration.ENDOSININJ;
    if ("HEMOPORT".equals(codeString))
      return V3RouteOfAdministration.HEMOPORT;
    if ("IABDINJ".equals(codeString))
      return V3RouteOfAdministration.IABDINJ;
    if ("IAINJ".equals(codeString))
      return V3RouteOfAdministration.IAINJ;
    if ("IAINJP".equals(codeString))
      return V3RouteOfAdministration.IAINJP;
    if ("IAINJSP".equals(codeString))
      return V3RouteOfAdministration.IAINJSP;
    if ("IARTINJ".equals(codeString))
      return V3RouteOfAdministration.IARTINJ;
    if ("IBURSINJ".equals(codeString))
      return V3RouteOfAdministration.IBURSINJ;
    if ("ICARDINJ".equals(codeString))
      return V3RouteOfAdministration.ICARDINJ;
    if ("ICARDINJRP".equals(codeString))
      return V3RouteOfAdministration.ICARDINJRP;
    if ("ICARDINJSP".equals(codeString))
      return V3RouteOfAdministration.ICARDINJSP;
    if ("ICARINJP".equals(codeString))
      return V3RouteOfAdministration.ICARINJP;
    if ("ICARTINJ".equals(codeString))
      return V3RouteOfAdministration.ICARTINJ;
    if ("ICAUDINJ".equals(codeString))
      return V3RouteOfAdministration.ICAUDINJ;
    if ("ICAVINJ".equals(codeString))
      return V3RouteOfAdministration.ICAVINJ;
    if ("ICAVITINJ".equals(codeString))
      return V3RouteOfAdministration.ICAVITINJ;
    if ("ICEREBINJ".equals(codeString))
      return V3RouteOfAdministration.ICEREBINJ;
    if ("ICISTERNINJ".equals(codeString))
      return V3RouteOfAdministration.ICISTERNINJ;
    if ("ICORONINJ".equals(codeString))
      return V3RouteOfAdministration.ICORONINJ;
    if ("ICORONINJP".equals(codeString))
      return V3RouteOfAdministration.ICORONINJP;
    if ("ICORPCAVINJ".equals(codeString))
      return V3RouteOfAdministration.ICORPCAVINJ;
    if ("IDINJ".equals(codeString))
      return V3RouteOfAdministration.IDINJ;
    if ("IDISCINJ".equals(codeString))
      return V3RouteOfAdministration.IDISCINJ;
    if ("IDUCTINJ".equals(codeString))
      return V3RouteOfAdministration.IDUCTINJ;
    if ("IDURINJ".equals(codeString))
      return V3RouteOfAdministration.IDURINJ;
    if ("IEPIDINJ".equals(codeString))
      return V3RouteOfAdministration.IEPIDINJ;
    if ("IEPITHINJ".equals(codeString))
      return V3RouteOfAdministration.IEPITHINJ;
    if ("ILESINJ".equals(codeString))
      return V3RouteOfAdministration.ILESINJ;
    if ("ILUMINJ".equals(codeString))
      return V3RouteOfAdministration.ILUMINJ;
    if ("ILYMPJINJ".equals(codeString))
      return V3RouteOfAdministration.ILYMPJINJ;
    if ("IM".equals(codeString))
      return V3RouteOfAdministration.IM;
    if ("IMD".equals(codeString))
      return V3RouteOfAdministration.IMD;
    if ("IMZ".equals(codeString))
      return V3RouteOfAdministration.IMZ;
    if ("IMEDULINJ".equals(codeString))
      return V3RouteOfAdministration.IMEDULINJ;
    if ("INTERMENINJ".equals(codeString))
      return V3RouteOfAdministration.INTERMENINJ;
    if ("INTERSTITINJ".equals(codeString))
      return V3RouteOfAdministration.INTERSTITINJ;
    if ("IOINJ".equals(codeString))
      return V3RouteOfAdministration.IOINJ;
    if ("IOSSINJ".equals(codeString))
      return V3RouteOfAdministration.IOSSINJ;
    if ("IOVARINJ".equals(codeString))
      return V3RouteOfAdministration.IOVARINJ;
    if ("IPCARDINJ".equals(codeString))
      return V3RouteOfAdministration.IPCARDINJ;
    if ("IPERINJ".equals(codeString))
      return V3RouteOfAdministration.IPERINJ;
    if ("IPINJ".equals(codeString))
      return V3RouteOfAdministration.IPINJ;
    if ("IPLRINJ".equals(codeString))
      return V3RouteOfAdministration.IPLRINJ;
    if ("IPROSTINJ".equals(codeString))
      return V3RouteOfAdministration.IPROSTINJ;
    if ("IPUMPINJ".equals(codeString))
      return V3RouteOfAdministration.IPUMPINJ;
    if ("ISINJ".equals(codeString))
      return V3RouteOfAdministration.ISINJ;
    if ("ISTERINJ".equals(codeString))
      return V3RouteOfAdministration.ISTERINJ;
    if ("ISYNINJ".equals(codeString))
      return V3RouteOfAdministration.ISYNINJ;
    if ("ITENDINJ".equals(codeString))
      return V3RouteOfAdministration.ITENDINJ;
    if ("ITESTINJ".equals(codeString))
      return V3RouteOfAdministration.ITESTINJ;
    if ("ITHORINJ".equals(codeString))
      return V3RouteOfAdministration.ITHORINJ;
    if ("ITINJ".equals(codeString))
      return V3RouteOfAdministration.ITINJ;
    if ("ITUBINJ".equals(codeString))
      return V3RouteOfAdministration.ITUBINJ;
    if ("ITUMINJ".equals(codeString))
      return V3RouteOfAdministration.ITUMINJ;
    if ("ITYMPINJ".equals(codeString))
      return V3RouteOfAdministration.ITYMPINJ;
    if ("IUINJ".equals(codeString))
      return V3RouteOfAdministration.IUINJ;
    if ("IUINJC".equals(codeString))
      return V3RouteOfAdministration.IUINJC;
    if ("IURETINJ".equals(codeString))
      return V3RouteOfAdministration.IURETINJ;
    if ("IVASCINJ".equals(codeString))
      return V3RouteOfAdministration.IVASCINJ;
    if ("IVENTINJ".equals(codeString))
      return V3RouteOfAdministration.IVENTINJ;
    if ("IVESINJ".equals(codeString))
      return V3RouteOfAdministration.IVESINJ;
    if ("IVINJ".equals(codeString))
      return V3RouteOfAdministration.IVINJ;
    if ("IVINJBOL".equals(codeString))
      return V3RouteOfAdministration.IVINJBOL;
    if ("IVPUSH".equals(codeString))
      return V3RouteOfAdministration.IVPUSH;
    if ("IVRPUSH".equals(codeString))
      return V3RouteOfAdministration.IVRPUSH;
    if ("IVSPUSH".equals(codeString))
      return V3RouteOfAdministration.IVSPUSH;
    if ("IVITINJ".equals(codeString))
      return V3RouteOfAdministration.IVITINJ;
    if ("PAINJ".equals(codeString))
      return V3RouteOfAdministration.PAINJ;
    if ("PARENTINJ".equals(codeString))
      return V3RouteOfAdministration.PARENTINJ;
    if ("PDONTINJ".equals(codeString))
      return V3RouteOfAdministration.PDONTINJ;
    if ("PDPINJ".equals(codeString))
      return V3RouteOfAdministration.PDPINJ;
    if ("PDURINJ".equals(codeString))
      return V3RouteOfAdministration.PDURINJ;
    if ("PNINJ".equals(codeString))
      return V3RouteOfAdministration.PNINJ;
    if ("PNSINJ".equals(codeString))
      return V3RouteOfAdministration.PNSINJ;
    if ("RBINJ".equals(codeString))
      return V3RouteOfAdministration.RBINJ;
    if ("SCINJ".equals(codeString))
      return V3RouteOfAdministration.SCINJ;
    if ("SLESINJ".equals(codeString))
      return V3RouteOfAdministration.SLESINJ;
    if ("SOFTISINJ".equals(codeString))
      return V3RouteOfAdministration.SOFTISINJ;
    if ("SQ".equals(codeString))
      return V3RouteOfAdministration.SQ;
    if ("SUBARACHINJ".equals(codeString))
      return V3RouteOfAdministration.SUBARACHINJ;
    if ("SUBMUCINJ".equals(codeString))
      return V3RouteOfAdministration.SUBMUCINJ;
    if ("TRPLACINJ".equals(codeString))
      return V3RouteOfAdministration.TRPLACINJ;
    if ("TRTRACHINJ".equals(codeString))
      return V3RouteOfAdministration.TRTRACHINJ;
    if ("URETHINJ".equals(codeString))
      return V3RouteOfAdministration.URETHINJ;
    if ("URETINJ".equals(codeString))
      return V3RouteOfAdministration.URETINJ;
    if ("_Insertion".equals(codeString))
      return V3RouteOfAdministration._INSERTION;
    if ("CERVINS".equals(codeString))
      return V3RouteOfAdministration.CERVINS;
    if ("IOSURGINS".equals(codeString))
      return V3RouteOfAdministration.IOSURGINS;
    if ("IU".equals(codeString))
      return V3RouteOfAdministration.IU;
    if ("LPINS".equals(codeString))
      return V3RouteOfAdministration.LPINS;
    if ("PR".equals(codeString))
      return V3RouteOfAdministration.PR;
    if ("SQSURGINS".equals(codeString))
      return V3RouteOfAdministration.SQSURGINS;
    if ("URETHINS".equals(codeString))
      return V3RouteOfAdministration.URETHINS;
    if ("VAGINSI".equals(codeString))
      return V3RouteOfAdministration.VAGINSI;
    if ("_Instillation".equals(codeString))
      return V3RouteOfAdministration._INSTILLATION;
    if ("CECINSTL".equals(codeString))
      return V3RouteOfAdministration.CECINSTL;
    if ("EFT".equals(codeString))
      return V3RouteOfAdministration.EFT;
    if ("ENTINSTL".equals(codeString))
      return V3RouteOfAdministration.ENTINSTL;
    if ("GT".equals(codeString))
      return V3RouteOfAdministration.GT;
    if ("NGT".equals(codeString))
      return V3RouteOfAdministration.NGT;
    if ("OGT".equals(codeString))
      return V3RouteOfAdministration.OGT;
    if ("BLADINSTL".equals(codeString))
      return V3RouteOfAdministration.BLADINSTL;
    if ("CAPDINSTL".equals(codeString))
      return V3RouteOfAdministration.CAPDINSTL;
    if ("CTINSTL".equals(codeString))
      return V3RouteOfAdministration.CTINSTL;
    if ("ETINSTL".equals(codeString))
      return V3RouteOfAdministration.ETINSTL;
    if ("GJT".equals(codeString))
      return V3RouteOfAdministration.GJT;
    if ("IBRONCHINSTIL".equals(codeString))
      return V3RouteOfAdministration.IBRONCHINSTIL;
    if ("IDUODINSTIL".equals(codeString))
      return V3RouteOfAdministration.IDUODINSTIL;
    if ("IESOPHINSTIL".equals(codeString))
      return V3RouteOfAdministration.IESOPHINSTIL;
    if ("IGASTINSTIL".equals(codeString))
      return V3RouteOfAdministration.IGASTINSTIL;
    if ("IILEALINJ".equals(codeString))
      return V3RouteOfAdministration.IILEALINJ;
    if ("IOINSTL".equals(codeString))
      return V3RouteOfAdministration.IOINSTL;
    if ("ISININSTIL".equals(codeString))
      return V3RouteOfAdministration.ISININSTIL;
    if ("ITRACHINSTIL".equals(codeString))
      return V3RouteOfAdministration.ITRACHINSTIL;
    if ("IUINSTL".equals(codeString))
      return V3RouteOfAdministration.IUINSTL;
    if ("JJTINSTL".equals(codeString))
      return V3RouteOfAdministration.JJTINSTL;
    if ("LARYNGINSTIL".equals(codeString))
      return V3RouteOfAdministration.LARYNGINSTIL;
    if ("NASALINSTIL".equals(codeString))
      return V3RouteOfAdministration.NASALINSTIL;
    if ("NASOGASINSTIL".equals(codeString))
      return V3RouteOfAdministration.NASOGASINSTIL;
    if ("NTT".equals(codeString))
      return V3RouteOfAdministration.NTT;
    if ("OJJ".equals(codeString))
      return V3RouteOfAdministration.OJJ;
    if ("OT".equals(codeString))
      return V3RouteOfAdministration.OT;
    if ("PDPINSTL".equals(codeString))
      return V3RouteOfAdministration.PDPINSTL;
    if ("PNSINSTL".equals(codeString))
      return V3RouteOfAdministration.PNSINSTL;
    if ("RECINSTL".equals(codeString))
      return V3RouteOfAdministration.RECINSTL;
    if ("RECTINSTL".equals(codeString))
      return V3RouteOfAdministration.RECTINSTL;
    if ("SININSTIL".equals(codeString))
      return V3RouteOfAdministration.SININSTIL;
    if ("SOFTISINSTIL".equals(codeString))
      return V3RouteOfAdministration.SOFTISINSTIL;
    if ("TRACHINSTL".equals(codeString))
      return V3RouteOfAdministration.TRACHINSTL;
    if ("TRTYMPINSTIL".equals(codeString))
      return V3RouteOfAdministration.TRTYMPINSTIL;
    if ("URETHINSTL".equals(codeString))
      return V3RouteOfAdministration.URETHINSTL;
    if ("_IontophoresisRoute".equals(codeString))
      return V3RouteOfAdministration._IONTOPHORESISROUTE;
    if ("IONTO".equals(codeString))
      return V3RouteOfAdministration.IONTO;
    if ("_Irrigation".equals(codeString))
      return V3RouteOfAdministration._IRRIGATION;
    if ("GUIRR".equals(codeString))
      return V3RouteOfAdministration.GUIRR;
    if ("IGASTIRR".equals(codeString))
      return V3RouteOfAdministration.IGASTIRR;
    if ("ILESIRR".equals(codeString))
      return V3RouteOfAdministration.ILESIRR;
    if ("IOIRR".equals(codeString))
      return V3RouteOfAdministration.IOIRR;
    if ("BLADIRR".equals(codeString))
      return V3RouteOfAdministration.BLADIRR;
    if ("BLADIRRC".equals(codeString))
      return V3RouteOfAdministration.BLADIRRC;
    if ("BLADIRRT".equals(codeString))
      return V3RouteOfAdministration.BLADIRRT;
    if ("RECIRR".equals(codeString))
      return V3RouteOfAdministration.RECIRR;
    if ("_LavageRoute".equals(codeString))
      return V3RouteOfAdministration._LAVAGEROUTE;
    if ("IGASTLAV".equals(codeString))
      return V3RouteOfAdministration.IGASTLAV;
    if ("_MucosalAbsorptionRoute".equals(codeString))
      return V3RouteOfAdministration._MUCOSALABSORPTIONROUTE;
    if ("IDOUDMAB".equals(codeString))
      return V3RouteOfAdministration.IDOUDMAB;
    if ("ITRACHMAB".equals(codeString))
      return V3RouteOfAdministration.ITRACHMAB;
    if ("SMUCMAB".equals(codeString))
      return V3RouteOfAdministration.SMUCMAB;
    if ("_Nebulization".equals(codeString))
      return V3RouteOfAdministration._NEBULIZATION;
    if ("ETNEB".equals(codeString))
      return V3RouteOfAdministration.ETNEB;
    if ("_Rinse".equals(codeString))
      return V3RouteOfAdministration._RINSE;
    if ("DENRINSE".equals(codeString))
      return V3RouteOfAdministration.DENRINSE;
    if ("ORRINSE".equals(codeString))
      return V3RouteOfAdministration.ORRINSE;
    if ("_SuppositoryRoute".equals(codeString))
      return V3RouteOfAdministration._SUPPOSITORYROUTE;
    if ("URETHSUP".equals(codeString))
      return V3RouteOfAdministration.URETHSUP;
    if ("_Swish".equals(codeString))
      return V3RouteOfAdministration._SWISH;
    if ("SWISHSPIT".equals(codeString))
      return V3RouteOfAdministration.SWISHSPIT;
    if ("SWISHSWAL".equals(codeString))
      return V3RouteOfAdministration.SWISHSWAL;
    if ("_TopicalAbsorptionRoute".equals(codeString))
      return V3RouteOfAdministration._TOPICALABSORPTIONROUTE;
    if ("TTYMPTABSORP".equals(codeString))
      return V3RouteOfAdministration.TTYMPTABSORP;
    if ("_TopicalApplication".equals(codeString))
      return V3RouteOfAdministration._TOPICALAPPLICATION;
    if ("DRESS".equals(codeString))
      return V3RouteOfAdministration.DRESS;
    if ("SWAB".equals(codeString))
      return V3RouteOfAdministration.SWAB;
    if ("TOPICAL".equals(codeString))
      return V3RouteOfAdministration.TOPICAL;
    if ("BUC".equals(codeString))
      return V3RouteOfAdministration.BUC;
    if ("CERV".equals(codeString))
      return V3RouteOfAdministration.CERV;
    if ("DEN".equals(codeString))
      return V3RouteOfAdministration.DEN;
    if ("GIN".equals(codeString))
      return V3RouteOfAdministration.GIN;
    if ("HAIR".equals(codeString))
      return V3RouteOfAdministration.HAIR;
    if ("ICORNTA".equals(codeString))
      return V3RouteOfAdministration.ICORNTA;
    if ("ICORONTA".equals(codeString))
      return V3RouteOfAdministration.ICORONTA;
    if ("IESOPHTA".equals(codeString))
      return V3RouteOfAdministration.IESOPHTA;
    if ("IILEALTA".equals(codeString))
      return V3RouteOfAdministration.IILEALTA;
    if ("ILTOP".equals(codeString))
      return V3RouteOfAdministration.ILTOP;
    if ("ILUMTA".equals(codeString))
      return V3RouteOfAdministration.ILUMTA;
    if ("IOTOP".equals(codeString))
      return V3RouteOfAdministration.IOTOP;
    if ("LARYNGTA".equals(codeString))
      return V3RouteOfAdministration.LARYNGTA;
    if ("MUC".equals(codeString))
      return V3RouteOfAdministration.MUC;
    if ("NAIL".equals(codeString))
      return V3RouteOfAdministration.NAIL;
    if ("NASAL".equals(codeString))
      return V3RouteOfAdministration.NASAL;
    if ("OPTHALTA".equals(codeString))
      return V3RouteOfAdministration.OPTHALTA;
    if ("ORALTA".equals(codeString))
      return V3RouteOfAdministration.ORALTA;
    if ("ORMUC".equals(codeString))
      return V3RouteOfAdministration.ORMUC;
    if ("OROPHARTA".equals(codeString))
      return V3RouteOfAdministration.OROPHARTA;
    if ("PERIANAL".equals(codeString))
      return V3RouteOfAdministration.PERIANAL;
    if ("PERINEAL".equals(codeString))
      return V3RouteOfAdministration.PERINEAL;
    if ("PDONTTA".equals(codeString))
      return V3RouteOfAdministration.PDONTTA;
    if ("RECTAL".equals(codeString))
      return V3RouteOfAdministration.RECTAL;
    if ("SCALP".equals(codeString))
      return V3RouteOfAdministration.SCALP;
    if ("OCDRESTA".equals(codeString))
      return V3RouteOfAdministration.OCDRESTA;
    if ("SKIN".equals(codeString))
      return V3RouteOfAdministration.SKIN;
    if ("SUBCONJTA".equals(codeString))
      return V3RouteOfAdministration.SUBCONJTA;
    if ("TMUCTA".equals(codeString))
      return V3RouteOfAdministration.TMUCTA;
    if ("VAGINS".equals(codeString))
      return V3RouteOfAdministration.VAGINS;
    if ("INSUF".equals(codeString))
      return V3RouteOfAdministration.INSUF;
    if ("TRNSDERM".equals(codeString))
      return V3RouteOfAdministration.TRNSDERM;
    if ("_RouteBySite".equals(codeString))
      return V3RouteOfAdministration._ROUTEBYSITE;
    if ("_AmnioticFluidSacRoute".equals(codeString))
      return V3RouteOfAdministration._AMNIOTICFLUIDSACROUTE;
    if ("_BiliaryRoute".equals(codeString))
      return V3RouteOfAdministration._BILIARYROUTE;
    if ("_BodySurfaceRoute".equals(codeString))
      return V3RouteOfAdministration._BODYSURFACEROUTE;
    if ("_BuccalMucosaRoute".equals(codeString))
      return V3RouteOfAdministration._BUCCALMUCOSAROUTE;
    if ("_CecostomyRoute".equals(codeString))
      return V3RouteOfAdministration._CECOSTOMYROUTE;
    if ("_CervicalRoute".equals(codeString))
      return V3RouteOfAdministration._CERVICALROUTE;
    if ("_EndocervicalRoute".equals(codeString))
      return V3RouteOfAdministration._ENDOCERVICALROUTE;
    if ("_EnteralRoute".equals(codeString))
      return V3RouteOfAdministration._ENTERALROUTE;
    if ("_EpiduralRoute".equals(codeString))
      return V3RouteOfAdministration._EPIDURALROUTE;
    if ("_ExtraAmnioticRoute".equals(codeString))
      return V3RouteOfAdministration._EXTRAAMNIOTICROUTE;
    if ("_ExtracorporealCirculationRoute".equals(codeString))
      return V3RouteOfAdministration._EXTRACORPOREALCIRCULATIONROUTE;
    if ("_GastricRoute".equals(codeString))
      return V3RouteOfAdministration._GASTRICROUTE;
    if ("_GenitourinaryRoute".equals(codeString))
      return V3RouteOfAdministration._GENITOURINARYROUTE;
    if ("_GingivalRoute".equals(codeString))
      return V3RouteOfAdministration._GINGIVALROUTE;
    if ("_HairRoute".equals(codeString))
      return V3RouteOfAdministration._HAIRROUTE;
    if ("_InterameningealRoute".equals(codeString))
      return V3RouteOfAdministration._INTERAMENINGEALROUTE;
    if ("_InterstitialRoute".equals(codeString))
      return V3RouteOfAdministration._INTERSTITIALROUTE;
    if ("_IntraabdominalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAABDOMINALROUTE;
    if ("_IntraarterialRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAARTERIALROUTE;
    if ("_IntraarticularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAARTICULARROUTE;
    if ("_IntrabronchialRoute".equals(codeString))
      return V3RouteOfAdministration._INTRABRONCHIALROUTE;
    if ("_IntrabursalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRABURSALROUTE;
    if ("_IntracardiacRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACARDIACROUTE;
    if ("_IntracartilaginousRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACARTILAGINOUSROUTE;
    if ("_IntracaudalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACAUDALROUTE;
    if ("_IntracavernosalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACAVERNOSALROUTE;
    if ("_IntracavitaryRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACAVITARYROUTE;
    if ("_IntracerebralRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACEREBRALROUTE;
    if ("_IntracervicalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACERVICALROUTE;
    if ("_IntracisternalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACISTERNALROUTE;
    if ("_IntracornealRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACORNEALROUTE;
    if ("_IntracoronalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACORONALROUTE;
    if ("_IntracoronaryRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACORONARYROUTE;
    if ("_IntracorpusCavernosumRoute".equals(codeString))
      return V3RouteOfAdministration._INTRACORPUSCAVERNOSUMROUTE;
    if ("_IntradermalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRADERMALROUTE;
    if ("_IntradiscalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRADISCALROUTE;
    if ("_IntraductalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRADUCTALROUTE;
    if ("_IntraduodenalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRADUODENALROUTE;
    if ("_IntraduralRoute".equals(codeString))
      return V3RouteOfAdministration._INTRADURALROUTE;
    if ("_IntraepidermalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAEPIDERMALROUTE;
    if ("_IntraepithelialRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAEPITHELIALROUTE;
    if ("_IntraesophagealRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAESOPHAGEALROUTE;
    if ("_IntragastricRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAGASTRICROUTE;
    if ("_IntrailealRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAILEALROUTE;
    if ("_IntralesionalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRALESIONALROUTE;
    if ("_IntraluminalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRALUMINALROUTE;
    if ("_IntralymphaticRoute".equals(codeString))
      return V3RouteOfAdministration._INTRALYMPHATICROUTE;
    if ("_IntramedullaryRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAMEDULLARYROUTE;
    if ("_IntramuscularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAMUSCULARROUTE;
    if ("_IntraocularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAOCULARROUTE;
    if ("_IntraosseousRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAOSSEOUSROUTE;
    if ("_IntraovarianRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAOVARIANROUTE;
    if ("_IntrapericardialRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAPERICARDIALROUTE;
    if ("_IntraperitonealRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAPERITONEALROUTE;
    if ("_IntrapleuralRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAPLEURALROUTE;
    if ("_IntraprostaticRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAPROSTATICROUTE;
    if ("_IntrapulmonaryRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAPULMONARYROUTE;
    if ("_IntrasinalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRASINALROUTE;
    if ("_IntraspinalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRASPINALROUTE;
    if ("_IntrasternalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRASTERNALROUTE;
    if ("_IntrasynovialRoute".equals(codeString))
      return V3RouteOfAdministration._INTRASYNOVIALROUTE;
    if ("_IntratendinousRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATENDINOUSROUTE;
    if ("_IntratesticularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATESTICULARROUTE;
    if ("_IntrathecalRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATHECALROUTE;
    if ("_IntrathoracicRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATHORACICROUTE;
    if ("_IntratrachealRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATRACHEALROUTE;
    if ("_IntratubularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATUBULARROUTE;
    if ("_IntratumorRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATUMORROUTE;
    if ("_IntratympanicRoute".equals(codeString))
      return V3RouteOfAdministration._INTRATYMPANICROUTE;
    if ("_IntrauterineRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAUTERINEROUTE;
    if ("_IntravascularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAVASCULARROUTE;
    if ("_IntravenousRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAVENOUSROUTE;
    if ("_IntraventricularRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAVENTRICULARROUTE;
    if ("_IntravesicleRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAVESICLEROUTE;
    if ("_IntravitrealRoute".equals(codeString))
      return V3RouteOfAdministration._INTRAVITREALROUTE;
    if ("_JejunumRoute".equals(codeString))
      return V3RouteOfAdministration._JEJUNUMROUTE;
    if ("_LacrimalPunctaRoute".equals(codeString))
      return V3RouteOfAdministration._LACRIMALPUNCTAROUTE;
    if ("_LaryngealRoute".equals(codeString))
      return V3RouteOfAdministration._LARYNGEALROUTE;
    if ("_LingualRoute".equals(codeString))
      return V3RouteOfAdministration._LINGUALROUTE;
    if ("_MucousMembraneRoute".equals(codeString))
      return V3RouteOfAdministration._MUCOUSMEMBRANEROUTE;
    if ("_NailRoute".equals(codeString))
      return V3RouteOfAdministration._NAILROUTE;
    if ("_NasalRoute".equals(codeString))
      return V3RouteOfAdministration._NASALROUTE;
    if ("_OphthalmicRoute".equals(codeString))
      return V3RouteOfAdministration._OPHTHALMICROUTE;
    if ("_OralRoute".equals(codeString))
      return V3RouteOfAdministration._ORALROUTE;
    if ("_OromucosalRoute".equals(codeString))
      return V3RouteOfAdministration._OROMUCOSALROUTE;
    if ("_OropharyngealRoute".equals(codeString))
      return V3RouteOfAdministration._OROPHARYNGEALROUTE;
    if ("_OticRoute".equals(codeString))
      return V3RouteOfAdministration._OTICROUTE;
    if ("_ParanasalSinusesRoute".equals(codeString))
      return V3RouteOfAdministration._PARANASALSINUSESROUTE;
    if ("_ParenteralRoute".equals(codeString))
      return V3RouteOfAdministration._PARENTERALROUTE;
    if ("_PerianalRoute".equals(codeString))
      return V3RouteOfAdministration._PERIANALROUTE;
    if ("_PeriarticularRoute".equals(codeString))
      return V3RouteOfAdministration._PERIARTICULARROUTE;
    if ("_PeriduralRoute".equals(codeString))
      return V3RouteOfAdministration._PERIDURALROUTE;
    if ("_PerinealRoute".equals(codeString))
      return V3RouteOfAdministration._PERINEALROUTE;
    if ("_PerineuralRoute".equals(codeString))
      return V3RouteOfAdministration._PERINEURALROUTE;
    if ("_PeriodontalRoute".equals(codeString))
      return V3RouteOfAdministration._PERIODONTALROUTE;
    if ("_PulmonaryRoute".equals(codeString))
      return V3RouteOfAdministration._PULMONARYROUTE;
    if ("_RectalRoute".equals(codeString))
      return V3RouteOfAdministration._RECTALROUTE;
    if ("_RespiratoryTractRoute".equals(codeString))
      return V3RouteOfAdministration._RESPIRATORYTRACTROUTE;
    if ("_RetrobulbarRoute".equals(codeString))
      return V3RouteOfAdministration._RETROBULBARROUTE;
    if ("_ScalpRoute".equals(codeString))
      return V3RouteOfAdministration._SCALPROUTE;
    if ("_SinusUnspecifiedRoute".equals(codeString))
      return V3RouteOfAdministration._SINUSUNSPECIFIEDROUTE;
    if ("_SkinRoute".equals(codeString))
      return V3RouteOfAdministration._SKINROUTE;
    if ("_SoftTissueRoute".equals(codeString))
      return V3RouteOfAdministration._SOFTTISSUEROUTE;
    if ("_SubarachnoidRoute".equals(codeString))
      return V3RouteOfAdministration._SUBARACHNOIDROUTE;
    if ("_SubconjunctivalRoute".equals(codeString))
      return V3RouteOfAdministration._SUBCONJUNCTIVALROUTE;
    if ("_SubcutaneousRoute".equals(codeString))
      return V3RouteOfAdministration._SUBCUTANEOUSROUTE;
    if ("_SublesionalRoute".equals(codeString))
      return V3RouteOfAdministration._SUBLESIONALROUTE;
    if ("_SublingualRoute".equals(codeString))
      return V3RouteOfAdministration._SUBLINGUALROUTE;
    if ("_SubmucosalRoute".equals(codeString))
      return V3RouteOfAdministration._SUBMUCOSALROUTE;
    if ("_TracheostomyRoute".equals(codeString))
      return V3RouteOfAdministration._TRACHEOSTOMYROUTE;
    if ("_TransmucosalRoute".equals(codeString))
      return V3RouteOfAdministration._TRANSMUCOSALROUTE;
    if ("_TransplacentalRoute".equals(codeString))
      return V3RouteOfAdministration._TRANSPLACENTALROUTE;
    if ("_TranstrachealRoute".equals(codeString))
      return V3RouteOfAdministration._TRANSTRACHEALROUTE;
    if ("_TranstympanicRoute".equals(codeString))
      return V3RouteOfAdministration._TRANSTYMPANICROUTE;
    if ("_UreteralRoute".equals(codeString))
      return V3RouteOfAdministration._URETERALROUTE;
    if ("_UrethralRoute".equals(codeString))
      return V3RouteOfAdministration._URETHRALROUTE;
    if ("_UrinaryBladderRoute".equals(codeString))
      return V3RouteOfAdministration._URINARYBLADDERROUTE;
    if ("_UrinaryTractRoute".equals(codeString))
      return V3RouteOfAdministration._URINARYTRACTROUTE;
    if ("_VaginalRoute".equals(codeString))
      return V3RouteOfAdministration._VAGINALROUTE;
    if ("_VitreousHumourRoute".equals(codeString))
      return V3RouteOfAdministration._VITREOUSHUMOURROUTE;
    throw new IllegalArgumentException("Unknown V3RouteOfAdministration code '"+codeString+"'");
  }

  public String toCode(V3RouteOfAdministration code) {
    if (code == V3RouteOfAdministration._ROUTEBYMETHOD)
      return "_RouteByMethod";
    if (code == V3RouteOfAdministration.SOAK)
      return "SOAK";
    if (code == V3RouteOfAdministration.SHAMPOO)
      return "SHAMPOO";
    if (code == V3RouteOfAdministration.TRNSLING)
      return "TRNSLING";
    if (code == V3RouteOfAdministration.PO)
      return "PO";
    if (code == V3RouteOfAdministration.GARGLE)
      return "GARGLE";
    if (code == V3RouteOfAdministration.SUCK)
      return "SUCK";
    if (code == V3RouteOfAdministration._CHEW)
      return "_Chew";
    if (code == V3RouteOfAdministration.CHEW)
      return "CHEW";
    if (code == V3RouteOfAdministration._DIFFUSION)
      return "_Diffusion";
    if (code == V3RouteOfAdministration.EXTCORPDIF)
      return "EXTCORPDIF";
    if (code == V3RouteOfAdministration.HEMODIFF)
      return "HEMODIFF";
    if (code == V3RouteOfAdministration.TRNSDERMD)
      return "TRNSDERMD";
    if (code == V3RouteOfAdministration._DISSOLVE)
      return "_Dissolve";
    if (code == V3RouteOfAdministration.DISSOLVE)
      return "DISSOLVE";
    if (code == V3RouteOfAdministration.SL)
      return "SL";
    if (code == V3RouteOfAdministration._DOUCHE)
      return "_Douche";
    if (code == V3RouteOfAdministration.DOUCHE)
      return "DOUCHE";
    if (code == V3RouteOfAdministration._ELECTROOSMOSISROUTE)
      return "_ElectroOsmosisRoute";
    if (code == V3RouteOfAdministration.ELECTOSMOS)
      return "ELECTOSMOS";
    if (code == V3RouteOfAdministration._ENEMA)
      return "_Enema";
    if (code == V3RouteOfAdministration.ENEMA)
      return "ENEMA";
    if (code == V3RouteOfAdministration.RETENEMA)
      return "RETENEMA";
    if (code == V3RouteOfAdministration._FLUSH)
      return "_Flush";
    if (code == V3RouteOfAdministration.IVFLUSH)
      return "IVFLUSH";
    if (code == V3RouteOfAdministration._IMPLANTATION)
      return "_Implantation";
    if (code == V3RouteOfAdministration.IDIMPLNT)
      return "IDIMPLNT";
    if (code == V3RouteOfAdministration.IVITIMPLNT)
      return "IVITIMPLNT";
    if (code == V3RouteOfAdministration.SQIMPLNT)
      return "SQIMPLNT";
    if (code == V3RouteOfAdministration._INFUSION)
      return "_Infusion";
    if (code == V3RouteOfAdministration.EPI)
      return "EPI";
    if (code == V3RouteOfAdministration.IA)
      return "IA";
    if (code == V3RouteOfAdministration.IC)
      return "IC";
    if (code == V3RouteOfAdministration.ICOR)
      return "ICOR";
    if (code == V3RouteOfAdministration.IOSSC)
      return "IOSSC";
    if (code == V3RouteOfAdministration.IT)
      return "IT";
    if (code == V3RouteOfAdministration.IV)
      return "IV";
    if (code == V3RouteOfAdministration.IVC)
      return "IVC";
    if (code == V3RouteOfAdministration.IVCC)
      return "IVCC";
    if (code == V3RouteOfAdministration.IVCI)
      return "IVCI";
    if (code == V3RouteOfAdministration.PCA)
      return "PCA";
    if (code == V3RouteOfAdministration.IVASCINFUS)
      return "IVASCINFUS";
    if (code == V3RouteOfAdministration.SQINFUS)
      return "SQINFUS";
    if (code == V3RouteOfAdministration._INHALATION)
      return "_Inhalation";
    if (code == V3RouteOfAdministration.IPINHL)
      return "IPINHL";
    if (code == V3RouteOfAdministration.ORIFINHL)
      return "ORIFINHL";
    if (code == V3RouteOfAdministration.REBREATH)
      return "REBREATH";
    if (code == V3RouteOfAdministration.IPPB)
      return "IPPB";
    if (code == V3RouteOfAdministration.NASINHL)
      return "NASINHL";
    if (code == V3RouteOfAdministration.NASINHLC)
      return "NASINHLC";
    if (code == V3RouteOfAdministration.NEB)
      return "NEB";
    if (code == V3RouteOfAdministration.NASNEB)
      return "NASNEB";
    if (code == V3RouteOfAdministration.ORNEB)
      return "ORNEB";
    if (code == V3RouteOfAdministration.TRACH)
      return "TRACH";
    if (code == V3RouteOfAdministration.VENT)
      return "VENT";
    if (code == V3RouteOfAdministration.VENTMASK)
      return "VENTMASK";
    if (code == V3RouteOfAdministration._INJECTION)
      return "_Injection";
    if (code == V3RouteOfAdministration.AMNINJ)
      return "AMNINJ";
    if (code == V3RouteOfAdministration.BILINJ)
      return "BILINJ";
    if (code == V3RouteOfAdministration.CHOLINJ)
      return "CHOLINJ";
    if (code == V3RouteOfAdministration.CERVINJ)
      return "CERVINJ";
    if (code == V3RouteOfAdministration.EPIDURINJ)
      return "EPIDURINJ";
    if (code == V3RouteOfAdministration.EPIINJ)
      return "EPIINJ";
    if (code == V3RouteOfAdministration.EPINJSP)
      return "EPINJSP";
    if (code == V3RouteOfAdministration.EXTRAMNINJ)
      return "EXTRAMNINJ";
    if (code == V3RouteOfAdministration.EXTCORPINJ)
      return "EXTCORPINJ";
    if (code == V3RouteOfAdministration.GBINJ)
      return "GBINJ";
    if (code == V3RouteOfAdministration.GINGINJ)
      return "GINGINJ";
    if (code == V3RouteOfAdministration.BLADINJ)
      return "BLADINJ";
    if (code == V3RouteOfAdministration.ENDOSININJ)
      return "ENDOSININJ";
    if (code == V3RouteOfAdministration.HEMOPORT)
      return "HEMOPORT";
    if (code == V3RouteOfAdministration.IABDINJ)
      return "IABDINJ";
    if (code == V3RouteOfAdministration.IAINJ)
      return "IAINJ";
    if (code == V3RouteOfAdministration.IAINJP)
      return "IAINJP";
    if (code == V3RouteOfAdministration.IAINJSP)
      return "IAINJSP";
    if (code == V3RouteOfAdministration.IARTINJ)
      return "IARTINJ";
    if (code == V3RouteOfAdministration.IBURSINJ)
      return "IBURSINJ";
    if (code == V3RouteOfAdministration.ICARDINJ)
      return "ICARDINJ";
    if (code == V3RouteOfAdministration.ICARDINJRP)
      return "ICARDINJRP";
    if (code == V3RouteOfAdministration.ICARDINJSP)
      return "ICARDINJSP";
    if (code == V3RouteOfAdministration.ICARINJP)
      return "ICARINJP";
    if (code == V3RouteOfAdministration.ICARTINJ)
      return "ICARTINJ";
    if (code == V3RouteOfAdministration.ICAUDINJ)
      return "ICAUDINJ";
    if (code == V3RouteOfAdministration.ICAVINJ)
      return "ICAVINJ";
    if (code == V3RouteOfAdministration.ICAVITINJ)
      return "ICAVITINJ";
    if (code == V3RouteOfAdministration.ICEREBINJ)
      return "ICEREBINJ";
    if (code == V3RouteOfAdministration.ICISTERNINJ)
      return "ICISTERNINJ";
    if (code == V3RouteOfAdministration.ICORONINJ)
      return "ICORONINJ";
    if (code == V3RouteOfAdministration.ICORONINJP)
      return "ICORONINJP";
    if (code == V3RouteOfAdministration.ICORPCAVINJ)
      return "ICORPCAVINJ";
    if (code == V3RouteOfAdministration.IDINJ)
      return "IDINJ";
    if (code == V3RouteOfAdministration.IDISCINJ)
      return "IDISCINJ";
    if (code == V3RouteOfAdministration.IDUCTINJ)
      return "IDUCTINJ";
    if (code == V3RouteOfAdministration.IDURINJ)
      return "IDURINJ";
    if (code == V3RouteOfAdministration.IEPIDINJ)
      return "IEPIDINJ";
    if (code == V3RouteOfAdministration.IEPITHINJ)
      return "IEPITHINJ";
    if (code == V3RouteOfAdministration.ILESINJ)
      return "ILESINJ";
    if (code == V3RouteOfAdministration.ILUMINJ)
      return "ILUMINJ";
    if (code == V3RouteOfAdministration.ILYMPJINJ)
      return "ILYMPJINJ";
    if (code == V3RouteOfAdministration.IM)
      return "IM";
    if (code == V3RouteOfAdministration.IMD)
      return "IMD";
    if (code == V3RouteOfAdministration.IMZ)
      return "IMZ";
    if (code == V3RouteOfAdministration.IMEDULINJ)
      return "IMEDULINJ";
    if (code == V3RouteOfAdministration.INTERMENINJ)
      return "INTERMENINJ";
    if (code == V3RouteOfAdministration.INTERSTITINJ)
      return "INTERSTITINJ";
    if (code == V3RouteOfAdministration.IOINJ)
      return "IOINJ";
    if (code == V3RouteOfAdministration.IOSSINJ)
      return "IOSSINJ";
    if (code == V3RouteOfAdministration.IOVARINJ)
      return "IOVARINJ";
    if (code == V3RouteOfAdministration.IPCARDINJ)
      return "IPCARDINJ";
    if (code == V3RouteOfAdministration.IPERINJ)
      return "IPERINJ";
    if (code == V3RouteOfAdministration.IPINJ)
      return "IPINJ";
    if (code == V3RouteOfAdministration.IPLRINJ)
      return "IPLRINJ";
    if (code == V3RouteOfAdministration.IPROSTINJ)
      return "IPROSTINJ";
    if (code == V3RouteOfAdministration.IPUMPINJ)
      return "IPUMPINJ";
    if (code == V3RouteOfAdministration.ISINJ)
      return "ISINJ";
    if (code == V3RouteOfAdministration.ISTERINJ)
      return "ISTERINJ";
    if (code == V3RouteOfAdministration.ISYNINJ)
      return "ISYNINJ";
    if (code == V3RouteOfAdministration.ITENDINJ)
      return "ITENDINJ";
    if (code == V3RouteOfAdministration.ITESTINJ)
      return "ITESTINJ";
    if (code == V3RouteOfAdministration.ITHORINJ)
      return "ITHORINJ";
    if (code == V3RouteOfAdministration.ITINJ)
      return "ITINJ";
    if (code == V3RouteOfAdministration.ITUBINJ)
      return "ITUBINJ";
    if (code == V3RouteOfAdministration.ITUMINJ)
      return "ITUMINJ";
    if (code == V3RouteOfAdministration.ITYMPINJ)
      return "ITYMPINJ";
    if (code == V3RouteOfAdministration.IUINJ)
      return "IUINJ";
    if (code == V3RouteOfAdministration.IUINJC)
      return "IUINJC";
    if (code == V3RouteOfAdministration.IURETINJ)
      return "IURETINJ";
    if (code == V3RouteOfAdministration.IVASCINJ)
      return "IVASCINJ";
    if (code == V3RouteOfAdministration.IVENTINJ)
      return "IVENTINJ";
    if (code == V3RouteOfAdministration.IVESINJ)
      return "IVESINJ";
    if (code == V3RouteOfAdministration.IVINJ)
      return "IVINJ";
    if (code == V3RouteOfAdministration.IVINJBOL)
      return "IVINJBOL";
    if (code == V3RouteOfAdministration.IVPUSH)
      return "IVPUSH";
    if (code == V3RouteOfAdministration.IVRPUSH)
      return "IVRPUSH";
    if (code == V3RouteOfAdministration.IVSPUSH)
      return "IVSPUSH";
    if (code == V3RouteOfAdministration.IVITINJ)
      return "IVITINJ";
    if (code == V3RouteOfAdministration.PAINJ)
      return "PAINJ";
    if (code == V3RouteOfAdministration.PARENTINJ)
      return "PARENTINJ";
    if (code == V3RouteOfAdministration.PDONTINJ)
      return "PDONTINJ";
    if (code == V3RouteOfAdministration.PDPINJ)
      return "PDPINJ";
    if (code == V3RouteOfAdministration.PDURINJ)
      return "PDURINJ";
    if (code == V3RouteOfAdministration.PNINJ)
      return "PNINJ";
    if (code == V3RouteOfAdministration.PNSINJ)
      return "PNSINJ";
    if (code == V3RouteOfAdministration.RBINJ)
      return "RBINJ";
    if (code == V3RouteOfAdministration.SCINJ)
      return "SCINJ";
    if (code == V3RouteOfAdministration.SLESINJ)
      return "SLESINJ";
    if (code == V3RouteOfAdministration.SOFTISINJ)
      return "SOFTISINJ";
    if (code == V3RouteOfAdministration.SQ)
      return "SQ";
    if (code == V3RouteOfAdministration.SUBARACHINJ)
      return "SUBARACHINJ";
    if (code == V3RouteOfAdministration.SUBMUCINJ)
      return "SUBMUCINJ";
    if (code == V3RouteOfAdministration.TRPLACINJ)
      return "TRPLACINJ";
    if (code == V3RouteOfAdministration.TRTRACHINJ)
      return "TRTRACHINJ";
    if (code == V3RouteOfAdministration.URETHINJ)
      return "URETHINJ";
    if (code == V3RouteOfAdministration.URETINJ)
      return "URETINJ";
    if (code == V3RouteOfAdministration._INSERTION)
      return "_Insertion";
    if (code == V3RouteOfAdministration.CERVINS)
      return "CERVINS";
    if (code == V3RouteOfAdministration.IOSURGINS)
      return "IOSURGINS";
    if (code == V3RouteOfAdministration.IU)
      return "IU";
    if (code == V3RouteOfAdministration.LPINS)
      return "LPINS";
    if (code == V3RouteOfAdministration.PR)
      return "PR";
    if (code == V3RouteOfAdministration.SQSURGINS)
      return "SQSURGINS";
    if (code == V3RouteOfAdministration.URETHINS)
      return "URETHINS";
    if (code == V3RouteOfAdministration.VAGINSI)
      return "VAGINSI";
    if (code == V3RouteOfAdministration._INSTILLATION)
      return "_Instillation";
    if (code == V3RouteOfAdministration.CECINSTL)
      return "CECINSTL";
    if (code == V3RouteOfAdministration.EFT)
      return "EFT";
    if (code == V3RouteOfAdministration.ENTINSTL)
      return "ENTINSTL";
    if (code == V3RouteOfAdministration.GT)
      return "GT";
    if (code == V3RouteOfAdministration.NGT)
      return "NGT";
    if (code == V3RouteOfAdministration.OGT)
      return "OGT";
    if (code == V3RouteOfAdministration.BLADINSTL)
      return "BLADINSTL";
    if (code == V3RouteOfAdministration.CAPDINSTL)
      return "CAPDINSTL";
    if (code == V3RouteOfAdministration.CTINSTL)
      return "CTINSTL";
    if (code == V3RouteOfAdministration.ETINSTL)
      return "ETINSTL";
    if (code == V3RouteOfAdministration.GJT)
      return "GJT";
    if (code == V3RouteOfAdministration.IBRONCHINSTIL)
      return "IBRONCHINSTIL";
    if (code == V3RouteOfAdministration.IDUODINSTIL)
      return "IDUODINSTIL";
    if (code == V3RouteOfAdministration.IESOPHINSTIL)
      return "IESOPHINSTIL";
    if (code == V3RouteOfAdministration.IGASTINSTIL)
      return "IGASTINSTIL";
    if (code == V3RouteOfAdministration.IILEALINJ)
      return "IILEALINJ";
    if (code == V3RouteOfAdministration.IOINSTL)
      return "IOINSTL";
    if (code == V3RouteOfAdministration.ISININSTIL)
      return "ISININSTIL";
    if (code == V3RouteOfAdministration.ITRACHINSTIL)
      return "ITRACHINSTIL";
    if (code == V3RouteOfAdministration.IUINSTL)
      return "IUINSTL";
    if (code == V3RouteOfAdministration.JJTINSTL)
      return "JJTINSTL";
    if (code == V3RouteOfAdministration.LARYNGINSTIL)
      return "LARYNGINSTIL";
    if (code == V3RouteOfAdministration.NASALINSTIL)
      return "NASALINSTIL";
    if (code == V3RouteOfAdministration.NASOGASINSTIL)
      return "NASOGASINSTIL";
    if (code == V3RouteOfAdministration.NTT)
      return "NTT";
    if (code == V3RouteOfAdministration.OJJ)
      return "OJJ";
    if (code == V3RouteOfAdministration.OT)
      return "OT";
    if (code == V3RouteOfAdministration.PDPINSTL)
      return "PDPINSTL";
    if (code == V3RouteOfAdministration.PNSINSTL)
      return "PNSINSTL";
    if (code == V3RouteOfAdministration.RECINSTL)
      return "RECINSTL";
    if (code == V3RouteOfAdministration.RECTINSTL)
      return "RECTINSTL";
    if (code == V3RouteOfAdministration.SININSTIL)
      return "SININSTIL";
    if (code == V3RouteOfAdministration.SOFTISINSTIL)
      return "SOFTISINSTIL";
    if (code == V3RouteOfAdministration.TRACHINSTL)
      return "TRACHINSTL";
    if (code == V3RouteOfAdministration.TRTYMPINSTIL)
      return "TRTYMPINSTIL";
    if (code == V3RouteOfAdministration.URETHINSTL)
      return "URETHINSTL";
    if (code == V3RouteOfAdministration._IONTOPHORESISROUTE)
      return "_IontophoresisRoute";
    if (code == V3RouteOfAdministration.IONTO)
      return "IONTO";
    if (code == V3RouteOfAdministration._IRRIGATION)
      return "_Irrigation";
    if (code == V3RouteOfAdministration.GUIRR)
      return "GUIRR";
    if (code == V3RouteOfAdministration.IGASTIRR)
      return "IGASTIRR";
    if (code == V3RouteOfAdministration.ILESIRR)
      return "ILESIRR";
    if (code == V3RouteOfAdministration.IOIRR)
      return "IOIRR";
    if (code == V3RouteOfAdministration.BLADIRR)
      return "BLADIRR";
    if (code == V3RouteOfAdministration.BLADIRRC)
      return "BLADIRRC";
    if (code == V3RouteOfAdministration.BLADIRRT)
      return "BLADIRRT";
    if (code == V3RouteOfAdministration.RECIRR)
      return "RECIRR";
    if (code == V3RouteOfAdministration._LAVAGEROUTE)
      return "_LavageRoute";
    if (code == V3RouteOfAdministration.IGASTLAV)
      return "IGASTLAV";
    if (code == V3RouteOfAdministration._MUCOSALABSORPTIONROUTE)
      return "_MucosalAbsorptionRoute";
    if (code == V3RouteOfAdministration.IDOUDMAB)
      return "IDOUDMAB";
    if (code == V3RouteOfAdministration.ITRACHMAB)
      return "ITRACHMAB";
    if (code == V3RouteOfAdministration.SMUCMAB)
      return "SMUCMAB";
    if (code == V3RouteOfAdministration._NEBULIZATION)
      return "_Nebulization";
    if (code == V3RouteOfAdministration.ETNEB)
      return "ETNEB";
    if (code == V3RouteOfAdministration._RINSE)
      return "_Rinse";
    if (code == V3RouteOfAdministration.DENRINSE)
      return "DENRINSE";
    if (code == V3RouteOfAdministration.ORRINSE)
      return "ORRINSE";
    if (code == V3RouteOfAdministration._SUPPOSITORYROUTE)
      return "_SuppositoryRoute";
    if (code == V3RouteOfAdministration.URETHSUP)
      return "URETHSUP";
    if (code == V3RouteOfAdministration._SWISH)
      return "_Swish";
    if (code == V3RouteOfAdministration.SWISHSPIT)
      return "SWISHSPIT";
    if (code == V3RouteOfAdministration.SWISHSWAL)
      return "SWISHSWAL";
    if (code == V3RouteOfAdministration._TOPICALABSORPTIONROUTE)
      return "_TopicalAbsorptionRoute";
    if (code == V3RouteOfAdministration.TTYMPTABSORP)
      return "TTYMPTABSORP";
    if (code == V3RouteOfAdministration._TOPICALAPPLICATION)
      return "_TopicalApplication";
    if (code == V3RouteOfAdministration.DRESS)
      return "DRESS";
    if (code == V3RouteOfAdministration.SWAB)
      return "SWAB";
    if (code == V3RouteOfAdministration.TOPICAL)
      return "TOPICAL";
    if (code == V3RouteOfAdministration.BUC)
      return "BUC";
    if (code == V3RouteOfAdministration.CERV)
      return "CERV";
    if (code == V3RouteOfAdministration.DEN)
      return "DEN";
    if (code == V3RouteOfAdministration.GIN)
      return "GIN";
    if (code == V3RouteOfAdministration.HAIR)
      return "HAIR";
    if (code == V3RouteOfAdministration.ICORNTA)
      return "ICORNTA";
    if (code == V3RouteOfAdministration.ICORONTA)
      return "ICORONTA";
    if (code == V3RouteOfAdministration.IESOPHTA)
      return "IESOPHTA";
    if (code == V3RouteOfAdministration.IILEALTA)
      return "IILEALTA";
    if (code == V3RouteOfAdministration.ILTOP)
      return "ILTOP";
    if (code == V3RouteOfAdministration.ILUMTA)
      return "ILUMTA";
    if (code == V3RouteOfAdministration.IOTOP)
      return "IOTOP";
    if (code == V3RouteOfAdministration.LARYNGTA)
      return "LARYNGTA";
    if (code == V3RouteOfAdministration.MUC)
      return "MUC";
    if (code == V3RouteOfAdministration.NAIL)
      return "NAIL";
    if (code == V3RouteOfAdministration.NASAL)
      return "NASAL";
    if (code == V3RouteOfAdministration.OPTHALTA)
      return "OPTHALTA";
    if (code == V3RouteOfAdministration.ORALTA)
      return "ORALTA";
    if (code == V3RouteOfAdministration.ORMUC)
      return "ORMUC";
    if (code == V3RouteOfAdministration.OROPHARTA)
      return "OROPHARTA";
    if (code == V3RouteOfAdministration.PERIANAL)
      return "PERIANAL";
    if (code == V3RouteOfAdministration.PERINEAL)
      return "PERINEAL";
    if (code == V3RouteOfAdministration.PDONTTA)
      return "PDONTTA";
    if (code == V3RouteOfAdministration.RECTAL)
      return "RECTAL";
    if (code == V3RouteOfAdministration.SCALP)
      return "SCALP";
    if (code == V3RouteOfAdministration.OCDRESTA)
      return "OCDRESTA";
    if (code == V3RouteOfAdministration.SKIN)
      return "SKIN";
    if (code == V3RouteOfAdministration.SUBCONJTA)
      return "SUBCONJTA";
    if (code == V3RouteOfAdministration.TMUCTA)
      return "TMUCTA";
    if (code == V3RouteOfAdministration.VAGINS)
      return "VAGINS";
    if (code == V3RouteOfAdministration.INSUF)
      return "INSUF";
    if (code == V3RouteOfAdministration.TRNSDERM)
      return "TRNSDERM";
    if (code == V3RouteOfAdministration._ROUTEBYSITE)
      return "_RouteBySite";
    if (code == V3RouteOfAdministration._AMNIOTICFLUIDSACROUTE)
      return "_AmnioticFluidSacRoute";
    if (code == V3RouteOfAdministration._BILIARYROUTE)
      return "_BiliaryRoute";
    if (code == V3RouteOfAdministration._BODYSURFACEROUTE)
      return "_BodySurfaceRoute";
    if (code == V3RouteOfAdministration._BUCCALMUCOSAROUTE)
      return "_BuccalMucosaRoute";
    if (code == V3RouteOfAdministration._CECOSTOMYROUTE)
      return "_CecostomyRoute";
    if (code == V3RouteOfAdministration._CERVICALROUTE)
      return "_CervicalRoute";
    if (code == V3RouteOfAdministration._ENDOCERVICALROUTE)
      return "_EndocervicalRoute";
    if (code == V3RouteOfAdministration._ENTERALROUTE)
      return "_EnteralRoute";
    if (code == V3RouteOfAdministration._EPIDURALROUTE)
      return "_EpiduralRoute";
    if (code == V3RouteOfAdministration._EXTRAAMNIOTICROUTE)
      return "_ExtraAmnioticRoute";
    if (code == V3RouteOfAdministration._EXTRACORPOREALCIRCULATIONROUTE)
      return "_ExtracorporealCirculationRoute";
    if (code == V3RouteOfAdministration._GASTRICROUTE)
      return "_GastricRoute";
    if (code == V3RouteOfAdministration._GENITOURINARYROUTE)
      return "_GenitourinaryRoute";
    if (code == V3RouteOfAdministration._GINGIVALROUTE)
      return "_GingivalRoute";
    if (code == V3RouteOfAdministration._HAIRROUTE)
      return "_HairRoute";
    if (code == V3RouteOfAdministration._INTERAMENINGEALROUTE)
      return "_InterameningealRoute";
    if (code == V3RouteOfAdministration._INTERSTITIALROUTE)
      return "_InterstitialRoute";
    if (code == V3RouteOfAdministration._INTRAABDOMINALROUTE)
      return "_IntraabdominalRoute";
    if (code == V3RouteOfAdministration._INTRAARTERIALROUTE)
      return "_IntraarterialRoute";
    if (code == V3RouteOfAdministration._INTRAARTICULARROUTE)
      return "_IntraarticularRoute";
    if (code == V3RouteOfAdministration._INTRABRONCHIALROUTE)
      return "_IntrabronchialRoute";
    if (code == V3RouteOfAdministration._INTRABURSALROUTE)
      return "_IntrabursalRoute";
    if (code == V3RouteOfAdministration._INTRACARDIACROUTE)
      return "_IntracardiacRoute";
    if (code == V3RouteOfAdministration._INTRACARTILAGINOUSROUTE)
      return "_IntracartilaginousRoute";
    if (code == V3RouteOfAdministration._INTRACAUDALROUTE)
      return "_IntracaudalRoute";
    if (code == V3RouteOfAdministration._INTRACAVERNOSALROUTE)
      return "_IntracavernosalRoute";
    if (code == V3RouteOfAdministration._INTRACAVITARYROUTE)
      return "_IntracavitaryRoute";
    if (code == V3RouteOfAdministration._INTRACEREBRALROUTE)
      return "_IntracerebralRoute";
    if (code == V3RouteOfAdministration._INTRACERVICALROUTE)
      return "_IntracervicalRoute";
    if (code == V3RouteOfAdministration._INTRACISTERNALROUTE)
      return "_IntracisternalRoute";
    if (code == V3RouteOfAdministration._INTRACORNEALROUTE)
      return "_IntracornealRoute";
    if (code == V3RouteOfAdministration._INTRACORONALROUTE)
      return "_IntracoronalRoute";
    if (code == V3RouteOfAdministration._INTRACORONARYROUTE)
      return "_IntracoronaryRoute";
    if (code == V3RouteOfAdministration._INTRACORPUSCAVERNOSUMROUTE)
      return "_IntracorpusCavernosumRoute";
    if (code == V3RouteOfAdministration._INTRADERMALROUTE)
      return "_IntradermalRoute";
    if (code == V3RouteOfAdministration._INTRADISCALROUTE)
      return "_IntradiscalRoute";
    if (code == V3RouteOfAdministration._INTRADUCTALROUTE)
      return "_IntraductalRoute";
    if (code == V3RouteOfAdministration._INTRADUODENALROUTE)
      return "_IntraduodenalRoute";
    if (code == V3RouteOfAdministration._INTRADURALROUTE)
      return "_IntraduralRoute";
    if (code == V3RouteOfAdministration._INTRAEPIDERMALROUTE)
      return "_IntraepidermalRoute";
    if (code == V3RouteOfAdministration._INTRAEPITHELIALROUTE)
      return "_IntraepithelialRoute";
    if (code == V3RouteOfAdministration._INTRAESOPHAGEALROUTE)
      return "_IntraesophagealRoute";
    if (code == V3RouteOfAdministration._INTRAGASTRICROUTE)
      return "_IntragastricRoute";
    if (code == V3RouteOfAdministration._INTRAILEALROUTE)
      return "_IntrailealRoute";
    if (code == V3RouteOfAdministration._INTRALESIONALROUTE)
      return "_IntralesionalRoute";
    if (code == V3RouteOfAdministration._INTRALUMINALROUTE)
      return "_IntraluminalRoute";
    if (code == V3RouteOfAdministration._INTRALYMPHATICROUTE)
      return "_IntralymphaticRoute";
    if (code == V3RouteOfAdministration._INTRAMEDULLARYROUTE)
      return "_IntramedullaryRoute";
    if (code == V3RouteOfAdministration._INTRAMUSCULARROUTE)
      return "_IntramuscularRoute";
    if (code == V3RouteOfAdministration._INTRAOCULARROUTE)
      return "_IntraocularRoute";
    if (code == V3RouteOfAdministration._INTRAOSSEOUSROUTE)
      return "_IntraosseousRoute";
    if (code == V3RouteOfAdministration._INTRAOVARIANROUTE)
      return "_IntraovarianRoute";
    if (code == V3RouteOfAdministration._INTRAPERICARDIALROUTE)
      return "_IntrapericardialRoute";
    if (code == V3RouteOfAdministration._INTRAPERITONEALROUTE)
      return "_IntraperitonealRoute";
    if (code == V3RouteOfAdministration._INTRAPLEURALROUTE)
      return "_IntrapleuralRoute";
    if (code == V3RouteOfAdministration._INTRAPROSTATICROUTE)
      return "_IntraprostaticRoute";
    if (code == V3RouteOfAdministration._INTRAPULMONARYROUTE)
      return "_IntrapulmonaryRoute";
    if (code == V3RouteOfAdministration._INTRASINALROUTE)
      return "_IntrasinalRoute";
    if (code == V3RouteOfAdministration._INTRASPINALROUTE)
      return "_IntraspinalRoute";
    if (code == V3RouteOfAdministration._INTRASTERNALROUTE)
      return "_IntrasternalRoute";
    if (code == V3RouteOfAdministration._INTRASYNOVIALROUTE)
      return "_IntrasynovialRoute";
    if (code == V3RouteOfAdministration._INTRATENDINOUSROUTE)
      return "_IntratendinousRoute";
    if (code == V3RouteOfAdministration._INTRATESTICULARROUTE)
      return "_IntratesticularRoute";
    if (code == V3RouteOfAdministration._INTRATHECALROUTE)
      return "_IntrathecalRoute";
    if (code == V3RouteOfAdministration._INTRATHORACICROUTE)
      return "_IntrathoracicRoute";
    if (code == V3RouteOfAdministration._INTRATRACHEALROUTE)
      return "_IntratrachealRoute";
    if (code == V3RouteOfAdministration._INTRATUBULARROUTE)
      return "_IntratubularRoute";
    if (code == V3RouteOfAdministration._INTRATUMORROUTE)
      return "_IntratumorRoute";
    if (code == V3RouteOfAdministration._INTRATYMPANICROUTE)
      return "_IntratympanicRoute";
    if (code == V3RouteOfAdministration._INTRAUTERINEROUTE)
      return "_IntrauterineRoute";
    if (code == V3RouteOfAdministration._INTRAVASCULARROUTE)
      return "_IntravascularRoute";
    if (code == V3RouteOfAdministration._INTRAVENOUSROUTE)
      return "_IntravenousRoute";
    if (code == V3RouteOfAdministration._INTRAVENTRICULARROUTE)
      return "_IntraventricularRoute";
    if (code == V3RouteOfAdministration._INTRAVESICLEROUTE)
      return "_IntravesicleRoute";
    if (code == V3RouteOfAdministration._INTRAVITREALROUTE)
      return "_IntravitrealRoute";
    if (code == V3RouteOfAdministration._JEJUNUMROUTE)
      return "_JejunumRoute";
    if (code == V3RouteOfAdministration._LACRIMALPUNCTAROUTE)
      return "_LacrimalPunctaRoute";
    if (code == V3RouteOfAdministration._LARYNGEALROUTE)
      return "_LaryngealRoute";
    if (code == V3RouteOfAdministration._LINGUALROUTE)
      return "_LingualRoute";
    if (code == V3RouteOfAdministration._MUCOUSMEMBRANEROUTE)
      return "_MucousMembraneRoute";
    if (code == V3RouteOfAdministration._NAILROUTE)
      return "_NailRoute";
    if (code == V3RouteOfAdministration._NASALROUTE)
      return "_NasalRoute";
    if (code == V3RouteOfAdministration._OPHTHALMICROUTE)
      return "_OphthalmicRoute";
    if (code == V3RouteOfAdministration._ORALROUTE)
      return "_OralRoute";
    if (code == V3RouteOfAdministration._OROMUCOSALROUTE)
      return "_OromucosalRoute";
    if (code == V3RouteOfAdministration._OROPHARYNGEALROUTE)
      return "_OropharyngealRoute";
    if (code == V3RouteOfAdministration._OTICROUTE)
      return "_OticRoute";
    if (code == V3RouteOfAdministration._PARANASALSINUSESROUTE)
      return "_ParanasalSinusesRoute";
    if (code == V3RouteOfAdministration._PARENTERALROUTE)
      return "_ParenteralRoute";
    if (code == V3RouteOfAdministration._PERIANALROUTE)
      return "_PerianalRoute";
    if (code == V3RouteOfAdministration._PERIARTICULARROUTE)
      return "_PeriarticularRoute";
    if (code == V3RouteOfAdministration._PERIDURALROUTE)
      return "_PeriduralRoute";
    if (code == V3RouteOfAdministration._PERINEALROUTE)
      return "_PerinealRoute";
    if (code == V3RouteOfAdministration._PERINEURALROUTE)
      return "_PerineuralRoute";
    if (code == V3RouteOfAdministration._PERIODONTALROUTE)
      return "_PeriodontalRoute";
    if (code == V3RouteOfAdministration._PULMONARYROUTE)
      return "_PulmonaryRoute";
    if (code == V3RouteOfAdministration._RECTALROUTE)
      return "_RectalRoute";
    if (code == V3RouteOfAdministration._RESPIRATORYTRACTROUTE)
      return "_RespiratoryTractRoute";
    if (code == V3RouteOfAdministration._RETROBULBARROUTE)
      return "_RetrobulbarRoute";
    if (code == V3RouteOfAdministration._SCALPROUTE)
      return "_ScalpRoute";
    if (code == V3RouteOfAdministration._SINUSUNSPECIFIEDROUTE)
      return "_SinusUnspecifiedRoute";
    if (code == V3RouteOfAdministration._SKINROUTE)
      return "_SkinRoute";
    if (code == V3RouteOfAdministration._SOFTTISSUEROUTE)
      return "_SoftTissueRoute";
    if (code == V3RouteOfAdministration._SUBARACHNOIDROUTE)
      return "_SubarachnoidRoute";
    if (code == V3RouteOfAdministration._SUBCONJUNCTIVALROUTE)
      return "_SubconjunctivalRoute";
    if (code == V3RouteOfAdministration._SUBCUTANEOUSROUTE)
      return "_SubcutaneousRoute";
    if (code == V3RouteOfAdministration._SUBLESIONALROUTE)
      return "_SublesionalRoute";
    if (code == V3RouteOfAdministration._SUBLINGUALROUTE)
      return "_SublingualRoute";
    if (code == V3RouteOfAdministration._SUBMUCOSALROUTE)
      return "_SubmucosalRoute";
    if (code == V3RouteOfAdministration._TRACHEOSTOMYROUTE)
      return "_TracheostomyRoute";
    if (code == V3RouteOfAdministration._TRANSMUCOSALROUTE)
      return "_TransmucosalRoute";
    if (code == V3RouteOfAdministration._TRANSPLACENTALROUTE)
      return "_TransplacentalRoute";
    if (code == V3RouteOfAdministration._TRANSTRACHEALROUTE)
      return "_TranstrachealRoute";
    if (code == V3RouteOfAdministration._TRANSTYMPANICROUTE)
      return "_TranstympanicRoute";
    if (code == V3RouteOfAdministration._URETERALROUTE)
      return "_UreteralRoute";
    if (code == V3RouteOfAdministration._URETHRALROUTE)
      return "_UrethralRoute";
    if (code == V3RouteOfAdministration._URINARYBLADDERROUTE)
      return "_UrinaryBladderRoute";
    if (code == V3RouteOfAdministration._URINARYTRACTROUTE)
      return "_UrinaryTractRoute";
    if (code == V3RouteOfAdministration._VAGINALROUTE)
      return "_VaginalRoute";
    if (code == V3RouteOfAdministration._VITREOUSHUMOURROUTE)
      return "_VitreousHumourRoute";
    return "?";
  }


}

