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

public class DicomDcimEnumFactory implements EnumFactory<DicomDcim> {

  public DicomDcim fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ARCHIVE".equals(codeString))
      return DicomDcim.ARCHIVE;
    if ("AR".equals(codeString))
      return DicomDcim.AR;
    if ("AS".equals(codeString))
      return DicomDcim.AS;
    if ("AU".equals(codeString))
      return DicomDcim.AU;
    if ("BDUS".equals(codeString))
      return DicomDcim.BDUS;
    if ("BI".equals(codeString))
      return DicomDcim.BI;
    if ("BMD".equals(codeString))
      return DicomDcim.BMD;
    if ("CAD".equals(codeString))
      return DicomDcim.CAD;
    if ("CAPTURE".equals(codeString))
      return DicomDcim.CAPTURE;
    if ("CD".equals(codeString))
      return DicomDcim.CD;
    if ("CF".equals(codeString))
      return DicomDcim.CF;
    if ("COMP".equals(codeString))
      return DicomDcim.COMP;
    if ("CP".equals(codeString))
      return DicomDcim.CP;
    if ("CR".equals(codeString))
      return DicomDcim.CR;
    if ("CS".equals(codeString))
      return DicomDcim.CS;
    if ("CT".equals(codeString))
      return DicomDcim.CT;
    if ("DD".equals(codeString))
      return DicomDcim.DD;
    if ("DF".equals(codeString))
      return DicomDcim.DF;
    if ("DG".equals(codeString))
      return DicomDcim.DG;
    if ("DM".equals(codeString))
      return DicomDcim.DM;
    if ("DOCD".equals(codeString))
      return DicomDcim.DOCD;
    if ("DS".equals(codeString))
      return DicomDcim.DS;
    if ("DSS".equals(codeString))
      return DicomDcim.DSS;
    if ("DX".equals(codeString))
      return DicomDcim.DX;
    if ("EC".equals(codeString))
      return DicomDcim.EC;
    if ("ECG".equals(codeString))
      return DicomDcim.ECG;
    if ("EPS".equals(codeString))
      return DicomDcim.EPS;
    if ("ES".equals(codeString))
      return DicomDcim.ES;
    if ("F".equals(codeString))
      return DicomDcim.F;
    if ("FA".equals(codeString))
      return DicomDcim.FA;
    if ("FC".equals(codeString))
      return DicomDcim.FC;
    if ("FILMD".equals(codeString))
      return DicomDcim.FILMD;
    if ("FP".equals(codeString))
      return DicomDcim.FP;
    if ("FS".equals(codeString))
      return DicomDcim.FS;
    if ("GM".equals(codeString))
      return DicomDcim.GM;
    if ("H".equals(codeString))
      return DicomDcim.H;
    if ("HC".equals(codeString))
      return DicomDcim.HC;
    if ("HD".equals(codeString))
      return DicomDcim.HD;
    if ("IO".equals(codeString))
      return DicomDcim.IO;
    if ("IVOCT".equals(codeString))
      return DicomDcim.IVOCT;
    if ("IVUS".equals(codeString))
      return DicomDcim.IVUS;
    if ("KER".equals(codeString))
      return DicomDcim.KER;
    if ("KO".equals(codeString))
      return DicomDcim.KO;
    if ("LEN".equals(codeString))
      return DicomDcim.LEN;
    if ("LOG".equals(codeString))
      return DicomDcim.LOG;
    if ("LP".equals(codeString))
      return DicomDcim.LP;
    if ("LS".equals(codeString))
      return DicomDcim.LS;
    if ("M".equals(codeString))
      return DicomDcim.M;
    if ("MA".equals(codeString))
      return DicomDcim.MA;
    if ("MC".equals(codeString))
      return DicomDcim.MC;
    if ("MCD".equals(codeString))
      return DicomDcim.MCD;
    if ("MEDIM".equals(codeString))
      return DicomDcim.MEDIM;
    if ("MG".equals(codeString))
      return DicomDcim.MG;
    if ("MP".equals(codeString))
      return DicomDcim.MP;
    if ("MR".equals(codeString))
      return DicomDcim.MR;
    if ("MS".equals(codeString))
      return DicomDcim.MS;
    if ("NEARLINE".equals(codeString))
      return DicomDcim.NEARLINE;
    if ("NM".equals(codeString))
      return DicomDcim.NM;
    if ("OAM".equals(codeString))
      return DicomDcim.OAM;
    if ("OCT".equals(codeString))
      return DicomDcim.OCT;
    if ("OFFLINE".equals(codeString))
      return DicomDcim.OFFLINE;
    if ("ONLINE".equals(codeString))
      return DicomDcim.ONLINE;
    if ("OP".equals(codeString))
      return DicomDcim.OP;
    if ("OPM".equals(codeString))
      return DicomDcim.OPM;
    if ("OPR".equals(codeString))
      return DicomDcim.OPR;
    if ("OPT".equals(codeString))
      return DicomDcim.OPT;
    if ("OPV".equals(codeString))
      return DicomDcim.OPV;
    if ("OSS".equals(codeString))
      return DicomDcim.OSS;
    if ("OT".equals(codeString))
      return DicomDcim.OT;
    if ("PR".equals(codeString))
      return DicomDcim.PR;
    if ("PRINT".equals(codeString))
      return DicomDcim.PRINT;
    if ("PT".equals(codeString))
      return DicomDcim.PT;
    if ("PX".equals(codeString))
      return DicomDcim.PX;
    if ("REG".equals(codeString))
      return DicomDcim.REG;
    if ("RF".equals(codeString))
      return DicomDcim.RF;
    if ("RG".equals(codeString))
      return DicomDcim.RG;
    if ("RT".equals(codeString))
      return DicomDcim.RT;
    if ("RTDOSE".equals(codeString))
      return DicomDcim.RTDOSE;
    if ("RTIMAGE".equals(codeString))
      return DicomDcim.RTIMAGE;
    if ("RTPLAN".equals(codeString))
      return DicomDcim.RTPLAN;
    if ("RTRECORD".equals(codeString))
      return DicomDcim.RTRECORD;
    if ("RTSTRUCT".equals(codeString))
      return DicomDcim.RTSTRUCT;
    if ("SEG".equals(codeString))
      return DicomDcim.SEG;
    if ("SM".equals(codeString))
      return DicomDcim.SM;
    if ("SMR".equals(codeString))
      return DicomDcim.SMR;
    if ("SR".equals(codeString))
      return DicomDcim.SR;
    if ("SRF".equals(codeString))
      return DicomDcim.SRF;
    if ("ST".equals(codeString))
      return DicomDcim.ST;
    if ("TG".equals(codeString))
      return DicomDcim.TG;
    if ("U".equals(codeString))
      return DicomDcim.U;
    if ("UNAVAILABLE".equals(codeString))
      return DicomDcim.UNAVAILABLE;
    if ("US".equals(codeString))
      return DicomDcim.US;
    if ("VA".equals(codeString))
      return DicomDcim.VA;
    if ("VF".equals(codeString))
      return DicomDcim.VF;
    if ("VIDD".equals(codeString))
      return DicomDcim.VIDD;
    if ("WSD".equals(codeString))
      return DicomDcim.WSD;
    if ("XA".equals(codeString))
      return DicomDcim.XA;
    if ("XC".equals(codeString))
      return DicomDcim.XC;
    if ("109001".equals(codeString))
      return DicomDcim._109001;
    if ("109002".equals(codeString))
      return DicomDcim._109002;
    if ("109003".equals(codeString))
      return DicomDcim._109003;
    if ("109004".equals(codeString))
      return DicomDcim._109004;
    if ("109005".equals(codeString))
      return DicomDcim._109005;
    if ("109006".equals(codeString))
      return DicomDcim._109006;
    if ("109007".equals(codeString))
      return DicomDcim._109007;
    if ("109008".equals(codeString))
      return DicomDcim._109008;
    if ("109009".equals(codeString))
      return DicomDcim._109009;
    if ("109010".equals(codeString))
      return DicomDcim._109010;
    if ("109011".equals(codeString))
      return DicomDcim._109011;
    if ("109012".equals(codeString))
      return DicomDcim._109012;
    if ("109013".equals(codeString))
      return DicomDcim._109013;
    if ("109014".equals(codeString))
      return DicomDcim._109014;
    if ("109015".equals(codeString))
      return DicomDcim._109015;
    if ("109016".equals(codeString))
      return DicomDcim._109016;
    if ("109017".equals(codeString))
      return DicomDcim._109017;
    if ("109018".equals(codeString))
      return DicomDcim._109018;
    if ("109019".equals(codeString))
      return DicomDcim._109019;
    if ("109020".equals(codeString))
      return DicomDcim._109020;
    if ("109021".equals(codeString))
      return DicomDcim._109021;
    if ("109022".equals(codeString))
      return DicomDcim._109022;
    if ("109023".equals(codeString))
      return DicomDcim._109023;
    if ("109024".equals(codeString))
      return DicomDcim._109024;
    if ("109025".equals(codeString))
      return DicomDcim._109025;
    if ("109026".equals(codeString))
      return DicomDcim._109026;
    if ("109027".equals(codeString))
      return DicomDcim._109027;
    if ("109028".equals(codeString))
      return DicomDcim._109028;
    if ("109029".equals(codeString))
      return DicomDcim._109029;
    if ("109030".equals(codeString))
      return DicomDcim._109030;
    if ("109031".equals(codeString))
      return DicomDcim._109031;
    if ("109032".equals(codeString))
      return DicomDcim._109032;
    if ("109033".equals(codeString))
      return DicomDcim._109033;
    if ("109034".equals(codeString))
      return DicomDcim._109034;
    if ("109035".equals(codeString))
      return DicomDcim._109035;
    if ("109036".equals(codeString))
      return DicomDcim._109036;
    if ("109037".equals(codeString))
      return DicomDcim._109037;
    if ("109038".equals(codeString))
      return DicomDcim._109038;
    if ("109039".equals(codeString))
      return DicomDcim._109039;
    if ("109040".equals(codeString))
      return DicomDcim._109040;
    if ("109041".equals(codeString))
      return DicomDcim._109041;
    if ("109042".equals(codeString))
      return DicomDcim._109042;
    if ("109043".equals(codeString))
      return DicomDcim._109043;
    if ("109044".equals(codeString))
      return DicomDcim._109044;
    if ("109045".equals(codeString))
      return DicomDcim._109045;
    if ("109046".equals(codeString))
      return DicomDcim._109046;
    if ("109047".equals(codeString))
      return DicomDcim._109047;
    if ("109048".equals(codeString))
      return DicomDcim._109048;
    if ("109049".equals(codeString))
      return DicomDcim._109049;
    if ("109050".equals(codeString))
      return DicomDcim._109050;
    if ("109051".equals(codeString))
      return DicomDcim._109051;
    if ("109052".equals(codeString))
      return DicomDcim._109052;
    if ("109053".equals(codeString))
      return DicomDcim._109053;
    if ("109054".equals(codeString))
      return DicomDcim._109054;
    if ("109055".equals(codeString))
      return DicomDcim._109055;
    if ("109056".equals(codeString))
      return DicomDcim._109056;
    if ("109057".equals(codeString))
      return DicomDcim._109057;
    if ("109058".equals(codeString))
      return DicomDcim._109058;
    if ("109059".equals(codeString))
      return DicomDcim._109059;
    if ("109060".equals(codeString))
      return DicomDcim._109060;
    if ("109061".equals(codeString))
      return DicomDcim._109061;
    if ("109063".equals(codeString))
      return DicomDcim._109063;
    if ("109070".equals(codeString))
      return DicomDcim._109070;
    if ("109071".equals(codeString))
      return DicomDcim._109071;
    if ("109072".equals(codeString))
      return DicomDcim._109072;
    if ("109073".equals(codeString))
      return DicomDcim._109073;
    if ("109080".equals(codeString))
      return DicomDcim._109080;
    if ("109081".equals(codeString))
      return DicomDcim._109081;
    if ("109082".equals(codeString))
      return DicomDcim._109082;
    if ("109083".equals(codeString))
      return DicomDcim._109083;
    if ("109091".equals(codeString))
      return DicomDcim._109091;
    if ("109092".equals(codeString))
      return DicomDcim._109092;
    if ("109093".equals(codeString))
      return DicomDcim._109093;
    if ("109094".equals(codeString))
      return DicomDcim._109094;
    if ("109095".equals(codeString))
      return DicomDcim._109095;
    if ("109096".equals(codeString))
      return DicomDcim._109096;
    if ("109101".equals(codeString))
      return DicomDcim._109101;
    if ("109102".equals(codeString))
      return DicomDcim._109102;
    if ("109103".equals(codeString))
      return DicomDcim._109103;
    if ("109104".equals(codeString))
      return DicomDcim._109104;
    if ("109105".equals(codeString))
      return DicomDcim._109105;
    if ("109106".equals(codeString))
      return DicomDcim._109106;
    if ("109110".equals(codeString))
      return DicomDcim._109110;
    if ("109111".equals(codeString))
      return DicomDcim._109111;
    if ("109112".equals(codeString))
      return DicomDcim._109112;
    if ("109113".equals(codeString))
      return DicomDcim._109113;
    if ("109114".equals(codeString))
      return DicomDcim._109114;
    if ("109115".equals(codeString))
      return DicomDcim._109115;
    if ("109116".equals(codeString))
      return DicomDcim._109116;
    if ("109117".equals(codeString))
      return DicomDcim._109117;
    if ("109120".equals(codeString))
      return DicomDcim._109120;
    if ("109121".equals(codeString))
      return DicomDcim._109121;
    if ("109122".equals(codeString))
      return DicomDcim._109122;
    if ("109123".equals(codeString))
      return DicomDcim._109123;
    if ("109124".equals(codeString))
      return DicomDcim._109124;
    if ("109125".equals(codeString))
      return DicomDcim._109125;
    if ("109132".equals(codeString))
      return DicomDcim._109132;
    if ("109133".equals(codeString))
      return DicomDcim._109133;
    if ("109134".equals(codeString))
      return DicomDcim._109134;
    if ("109135".equals(codeString))
      return DicomDcim._109135;
    if ("109136".equals(codeString))
      return DicomDcim._109136;
    if ("109200".equals(codeString))
      return DicomDcim._109200;
    if ("109201".equals(codeString))
      return DicomDcim._109201;
    if ("109202".equals(codeString))
      return DicomDcim._109202;
    if ("109203".equals(codeString))
      return DicomDcim._109203;
    if ("109204".equals(codeString))
      return DicomDcim._109204;
    if ("109205".equals(codeString))
      return DicomDcim._109205;
    if ("109206".equals(codeString))
      return DicomDcim._109206;
    if ("109207".equals(codeString))
      return DicomDcim._109207;
    if ("109208".equals(codeString))
      return DicomDcim._109208;
    if ("109209".equals(codeString))
      return DicomDcim._109209;
    if ("109210".equals(codeString))
      return DicomDcim._109210;
    if ("109211".equals(codeString))
      return DicomDcim._109211;
    if ("109212".equals(codeString))
      return DicomDcim._109212;
    if ("109213".equals(codeString))
      return DicomDcim._109213;
    if ("109214".equals(codeString))
      return DicomDcim._109214;
    if ("109215".equals(codeString))
      return DicomDcim._109215;
    if ("109216".equals(codeString))
      return DicomDcim._109216;
    if ("109217".equals(codeString))
      return DicomDcim._109217;
    if ("109218".equals(codeString))
      return DicomDcim._109218;
    if ("109219".equals(codeString))
      return DicomDcim._109219;
    if ("109220".equals(codeString))
      return DicomDcim._109220;
    if ("109221".equals(codeString))
      return DicomDcim._109221;
    if ("109222".equals(codeString))
      return DicomDcim._109222;
    if ("109701".equals(codeString))
      return DicomDcim._109701;
    if ("109702".equals(codeString))
      return DicomDcim._109702;
    if ("109703".equals(codeString))
      return DicomDcim._109703;
    if ("109704".equals(codeString))
      return DicomDcim._109704;
    if ("109705".equals(codeString))
      return DicomDcim._109705;
    if ("109706".equals(codeString))
      return DicomDcim._109706;
    if ("109707".equals(codeString))
      return DicomDcim._109707;
    if ("109708".equals(codeString))
      return DicomDcim._109708;
    if ("109709".equals(codeString))
      return DicomDcim._109709;
    if ("109710".equals(codeString))
      return DicomDcim._109710;
    if ("109801".equals(codeString))
      return DicomDcim._109801;
    if ("109802".equals(codeString))
      return DicomDcim._109802;
    if ("109803".equals(codeString))
      return DicomDcim._109803;
    if ("109804".equals(codeString))
      return DicomDcim._109804;
    if ("109805".equals(codeString))
      return DicomDcim._109805;
    if ("109806".equals(codeString))
      return DicomDcim._109806;
    if ("109807".equals(codeString))
      return DicomDcim._109807;
    if ("109808".equals(codeString))
      return DicomDcim._109808;
    if ("109809".equals(codeString))
      return DicomDcim._109809;
    if ("109810".equals(codeString))
      return DicomDcim._109810;
    if ("109811".equals(codeString))
      return DicomDcim._109811;
    if ("109812".equals(codeString))
      return DicomDcim._109812;
    if ("109813".equals(codeString))
      return DicomDcim._109813;
    if ("109814".equals(codeString))
      return DicomDcim._109814;
    if ("109815".equals(codeString))
      return DicomDcim._109815;
    if ("109816".equals(codeString))
      return DicomDcim._109816;
    if ("109817".equals(codeString))
      return DicomDcim._109817;
    if ("109818".equals(codeString))
      return DicomDcim._109818;
    if ("109819".equals(codeString))
      return DicomDcim._109819;
    if ("109820".equals(codeString))
      return DicomDcim._109820;
    if ("109821".equals(codeString))
      return DicomDcim._109821;
    if ("109822".equals(codeString))
      return DicomDcim._109822;
    if ("109823".equals(codeString))
      return DicomDcim._109823;
    if ("109824".equals(codeString))
      return DicomDcim._109824;
    if ("109825".equals(codeString))
      return DicomDcim._109825;
    if ("109826".equals(codeString))
      return DicomDcim._109826;
    if ("109827".equals(codeString))
      return DicomDcim._109827;
    if ("109828".equals(codeString))
      return DicomDcim._109828;
    if ("109829".equals(codeString))
      return DicomDcim._109829;
    if ("109830".equals(codeString))
      return DicomDcim._109830;
    if ("109831".equals(codeString))
      return DicomDcim._109831;
    if ("109832".equals(codeString))
      return DicomDcim._109832;
    if ("109833".equals(codeString))
      return DicomDcim._109833;
    if ("109834".equals(codeString))
      return DicomDcim._109834;
    if ("109835".equals(codeString))
      return DicomDcim._109835;
    if ("109836".equals(codeString))
      return DicomDcim._109836;
    if ("109837".equals(codeString))
      return DicomDcim._109837;
    if ("109838".equals(codeString))
      return DicomDcim._109838;
    if ("109839".equals(codeString))
      return DicomDcim._109839;
    if ("109840".equals(codeString))
      return DicomDcim._109840;
    if ("109841".equals(codeString))
      return DicomDcim._109841;
    if ("109842".equals(codeString))
      return DicomDcim._109842;
    if ("109843".equals(codeString))
      return DicomDcim._109843;
    if ("109844".equals(codeString))
      return DicomDcim._109844;
    if ("109845".equals(codeString))
      return DicomDcim._109845;
    if ("109846".equals(codeString))
      return DicomDcim._109846;
    if ("109847".equals(codeString))
      return DicomDcim._109847;
    if ("109848".equals(codeString))
      return DicomDcim._109848;
    if ("109849".equals(codeString))
      return DicomDcim._109849;
    if ("109850".equals(codeString))
      return DicomDcim._109850;
    if ("109851".equals(codeString))
      return DicomDcim._109851;
    if ("109852".equals(codeString))
      return DicomDcim._109852;
    if ("109853".equals(codeString))
      return DicomDcim._109853;
    if ("109854".equals(codeString))
      return DicomDcim._109854;
    if ("109855".equals(codeString))
      return DicomDcim._109855;
    if ("109856".equals(codeString))
      return DicomDcim._109856;
    if ("109857".equals(codeString))
      return DicomDcim._109857;
    if ("109858".equals(codeString))
      return DicomDcim._109858;
    if ("109859".equals(codeString))
      return DicomDcim._109859;
    if ("109860".equals(codeString))
      return DicomDcim._109860;
    if ("109861".equals(codeString))
      return DicomDcim._109861;
    if ("109862".equals(codeString))
      return DicomDcim._109862;
    if ("109863".equals(codeString))
      return DicomDcim._109863;
    if ("109864".equals(codeString))
      return DicomDcim._109864;
    if ("109865".equals(codeString))
      return DicomDcim._109865;
    if ("109866".equals(codeString))
      return DicomDcim._109866;
    if ("109867".equals(codeString))
      return DicomDcim._109867;
    if ("109868".equals(codeString))
      return DicomDcim._109868;
    if ("109869".equals(codeString))
      return DicomDcim._109869;
    if ("109870".equals(codeString))
      return DicomDcim._109870;
    if ("109871".equals(codeString))
      return DicomDcim._109871;
    if ("109872".equals(codeString))
      return DicomDcim._109872;
    if ("109873".equals(codeString))
      return DicomDcim._109873;
    if ("109874".equals(codeString))
      return DicomDcim._109874;
    if ("109875".equals(codeString))
      return DicomDcim._109875;
    if ("109876".equals(codeString))
      return DicomDcim._109876;
    if ("109877".equals(codeString))
      return DicomDcim._109877;
    if ("109878".equals(codeString))
      return DicomDcim._109878;
    if ("109879".equals(codeString))
      return DicomDcim._109879;
    if ("109880".equals(codeString))
      return DicomDcim._109880;
    if ("109881".equals(codeString))
      return DicomDcim._109881;
    if ("109901".equals(codeString))
      return DicomDcim._109901;
    if ("109902".equals(codeString))
      return DicomDcim._109902;
    if ("109903".equals(codeString))
      return DicomDcim._109903;
    if ("109904".equals(codeString))
      return DicomDcim._109904;
    if ("109905".equals(codeString))
      return DicomDcim._109905;
    if ("109906".equals(codeString))
      return DicomDcim._109906;
    if ("109907".equals(codeString))
      return DicomDcim._109907;
    if ("109908".equals(codeString))
      return DicomDcim._109908;
    if ("109909".equals(codeString))
      return DicomDcim._109909;
    if ("109910".equals(codeString))
      return DicomDcim._109910;
    if ("109911".equals(codeString))
      return DicomDcim._109911;
    if ("109912".equals(codeString))
      return DicomDcim._109912;
    if ("109913".equals(codeString))
      return DicomDcim._109913;
    if ("109914".equals(codeString))
      return DicomDcim._109914;
    if ("109915".equals(codeString))
      return DicomDcim._109915;
    if ("109916".equals(codeString))
      return DicomDcim._109916;
    if ("109917".equals(codeString))
      return DicomDcim._109917;
    if ("109918".equals(codeString))
      return DicomDcim._109918;
    if ("109919".equals(codeString))
      return DicomDcim._109919;
    if ("109920".equals(codeString))
      return DicomDcim._109920;
    if ("109921".equals(codeString))
      return DicomDcim._109921;
    if ("109931".equals(codeString))
      return DicomDcim._109931;
    if ("109932".equals(codeString))
      return DicomDcim._109932;
    if ("109933".equals(codeString))
      return DicomDcim._109933;
    if ("109941".equals(codeString))
      return DicomDcim._109941;
    if ("109943".equals(codeString))
      return DicomDcim._109943;
    if ("109991".equals(codeString))
      return DicomDcim._109991;
    if ("109992".equals(codeString))
      return DicomDcim._109992;
    if ("109993".equals(codeString))
      return DicomDcim._109993;
    if ("109994".equals(codeString))
      return DicomDcim._109994;
    if ("109995".equals(codeString))
      return DicomDcim._109995;
    if ("109996".equals(codeString))
      return DicomDcim._109996;
    if ("109997".equals(codeString))
      return DicomDcim._109997;
    if ("109998".equals(codeString))
      return DicomDcim._109998;
    if ("109999".equals(codeString))
      return DicomDcim._109999;
    if ("110001".equals(codeString))
      return DicomDcim._110001;
    if ("110002".equals(codeString))
      return DicomDcim._110002;
    if ("110003".equals(codeString))
      return DicomDcim._110003;
    if ("110004".equals(codeString))
      return DicomDcim._110004;
    if ("110005".equals(codeString))
      return DicomDcim._110005;
    if ("110006".equals(codeString))
      return DicomDcim._110006;
    if ("110007".equals(codeString))
      return DicomDcim._110007;
    if ("110008".equals(codeString))
      return DicomDcim._110008;
    if ("110009".equals(codeString))
      return DicomDcim._110009;
    if ("110010".equals(codeString))
      return DicomDcim._110010;
    if ("110011".equals(codeString))
      return DicomDcim._110011;
    if ("110012".equals(codeString))
      return DicomDcim._110012;
    if ("110013".equals(codeString))
      return DicomDcim._110013;
    if ("110020".equals(codeString))
      return DicomDcim._110020;
    if ("110021".equals(codeString))
      return DicomDcim._110021;
    if ("110022".equals(codeString))
      return DicomDcim._110022;
    if ("110023".equals(codeString))
      return DicomDcim._110023;
    if ("110024".equals(codeString))
      return DicomDcim._110024;
    if ("110025".equals(codeString))
      return DicomDcim._110025;
    if ("110026".equals(codeString))
      return DicomDcim._110026;
    if ("110027".equals(codeString))
      return DicomDcim._110027;
    if ("110028".equals(codeString))
      return DicomDcim._110028;
    if ("110030".equals(codeString))
      return DicomDcim._110030;
    if ("110031".equals(codeString))
      return DicomDcim._110031;
    if ("110032".equals(codeString))
      return DicomDcim._110032;
    if ("110033".equals(codeString))
      return DicomDcim._110033;
    if ("110034".equals(codeString))
      return DicomDcim._110034;
    if ("110035".equals(codeString))
      return DicomDcim._110035;
    if ("110036".equals(codeString))
      return DicomDcim._110036;
    if ("110037".equals(codeString))
      return DicomDcim._110037;
    if ("110038".equals(codeString))
      return DicomDcim._110038;
    if ("110100".equals(codeString))
      return DicomDcim._110100;
    if ("110101".equals(codeString))
      return DicomDcim._110101;
    if ("110102".equals(codeString))
      return DicomDcim._110102;
    if ("110103".equals(codeString))
      return DicomDcim._110103;
    if ("110104".equals(codeString))
      return DicomDcim._110104;
    if ("110105".equals(codeString))
      return DicomDcim._110105;
    if ("110106".equals(codeString))
      return DicomDcim._110106;
    if ("110107".equals(codeString))
      return DicomDcim._110107;
    if ("110108".equals(codeString))
      return DicomDcim._110108;
    if ("110109".equals(codeString))
      return DicomDcim._110109;
    if ("110110".equals(codeString))
      return DicomDcim._110110;
    if ("110111".equals(codeString))
      return DicomDcim._110111;
    if ("110112".equals(codeString))
      return DicomDcim._110112;
    if ("110113".equals(codeString))
      return DicomDcim._110113;
    if ("110114".equals(codeString))
      return DicomDcim._110114;
    if ("110120".equals(codeString))
      return DicomDcim._110120;
    if ("110121".equals(codeString))
      return DicomDcim._110121;
    if ("110122".equals(codeString))
      return DicomDcim._110122;
    if ("110123".equals(codeString))
      return DicomDcim._110123;
    if ("110124".equals(codeString))
      return DicomDcim._110124;
    if ("110125".equals(codeString))
      return DicomDcim._110125;
    if ("110126".equals(codeString))
      return DicomDcim._110126;
    if ("110127".equals(codeString))
      return DicomDcim._110127;
    if ("110128".equals(codeString))
      return DicomDcim._110128;
    if ("110129".equals(codeString))
      return DicomDcim._110129;
    if ("110130".equals(codeString))
      return DicomDcim._110130;
    if ("110131".equals(codeString))
      return DicomDcim._110131;
    if ("110132".equals(codeString))
      return DicomDcim._110132;
    if ("110133".equals(codeString))
      return DicomDcim._110133;
    if ("110134".equals(codeString))
      return DicomDcim._110134;
    if ("110135".equals(codeString))
      return DicomDcim._110135;
    if ("110136".equals(codeString))
      return DicomDcim._110136;
    if ("110137".equals(codeString))
      return DicomDcim._110137;
    if ("110138".equals(codeString))
      return DicomDcim._110138;
    if ("110139".equals(codeString))
      return DicomDcim._110139;
    if ("110140".equals(codeString))
      return DicomDcim._110140;
    if ("110141".equals(codeString))
      return DicomDcim._110141;
    if ("110142".equals(codeString))
      return DicomDcim._110142;
    if ("110150".equals(codeString))
      return DicomDcim._110150;
    if ("110151".equals(codeString))
      return DicomDcim._110151;
    if ("110152".equals(codeString))
      return DicomDcim._110152;
    if ("110153".equals(codeString))
      return DicomDcim._110153;
    if ("110154".equals(codeString))
      return DicomDcim._110154;
    if ("110155".equals(codeString))
      return DicomDcim._110155;
    if ("110180".equals(codeString))
      return DicomDcim._110180;
    if ("110181".equals(codeString))
      return DicomDcim._110181;
    if ("110182".equals(codeString))
      return DicomDcim._110182;
    if ("110190".equals(codeString))
      return DicomDcim._110190;
    if ("110500".equals(codeString))
      return DicomDcim._110500;
    if ("110501".equals(codeString))
      return DicomDcim._110501;
    if ("110502".equals(codeString))
      return DicomDcim._110502;
    if ("110503".equals(codeString))
      return DicomDcim._110503;
    if ("110504".equals(codeString))
      return DicomDcim._110504;
    if ("110505".equals(codeString))
      return DicomDcim._110505;
    if ("110506".equals(codeString))
      return DicomDcim._110506;
    if ("110507".equals(codeString))
      return DicomDcim._110507;
    if ("110508".equals(codeString))
      return DicomDcim._110508;
    if ("110509".equals(codeString))
      return DicomDcim._110509;
    if ("110510".equals(codeString))
      return DicomDcim._110510;
    if ("110511".equals(codeString))
      return DicomDcim._110511;
    if ("110512".equals(codeString))
      return DicomDcim._110512;
    if ("110513".equals(codeString))
      return DicomDcim._110513;
    if ("110514".equals(codeString))
      return DicomDcim._110514;
    if ("110515".equals(codeString))
      return DicomDcim._110515;
    if ("110516".equals(codeString))
      return DicomDcim._110516;
    if ("110518".equals(codeString))
      return DicomDcim._110518;
    if ("110519".equals(codeString))
      return DicomDcim._110519;
    if ("110521".equals(codeString))
      return DicomDcim._110521;
    if ("110522".equals(codeString))
      return DicomDcim._110522;
    if ("110523".equals(codeString))
      return DicomDcim._110523;
    if ("110524".equals(codeString))
      return DicomDcim._110524;
    if ("110526".equals(codeString))
      return DicomDcim._110526;
    if ("110527".equals(codeString))
      return DicomDcim._110527;
    if ("110528".equals(codeString))
      return DicomDcim._110528;
    if ("110529".equals(codeString))
      return DicomDcim._110529;
    if ("110700".equals(codeString))
      return DicomDcim._110700;
    if ("110701".equals(codeString))
      return DicomDcim._110701;
    if ("110702".equals(codeString))
      return DicomDcim._110702;
    if ("110703".equals(codeString))
      return DicomDcim._110703;
    if ("110704".equals(codeString))
      return DicomDcim._110704;
    if ("110705".equals(codeString))
      return DicomDcim._110705;
    if ("110706".equals(codeString))
      return DicomDcim._110706;
    if ("110800".equals(codeString))
      return DicomDcim._110800;
    if ("110801".equals(codeString))
      return DicomDcim._110801;
    if ("110802".equals(codeString))
      return DicomDcim._110802;
    if ("110803".equals(codeString))
      return DicomDcim._110803;
    if ("110804".equals(codeString))
      return DicomDcim._110804;
    if ("110805".equals(codeString))
      return DicomDcim._110805;
    if ("110806".equals(codeString))
      return DicomDcim._110806;
    if ("110807".equals(codeString))
      return DicomDcim._110807;
    if ("110808".equals(codeString))
      return DicomDcim._110808;
    if ("110809".equals(codeString))
      return DicomDcim._110809;
    if ("110810".equals(codeString))
      return DicomDcim._110810;
    if ("110811".equals(codeString))
      return DicomDcim._110811;
    if ("110812".equals(codeString))
      return DicomDcim._110812;
    if ("110813".equals(codeString))
      return DicomDcim._110813;
    if ("110814".equals(codeString))
      return DicomDcim._110814;
    if ("110815".equals(codeString))
      return DicomDcim._110815;
    if ("110816".equals(codeString))
      return DicomDcim._110816;
    if ("110817".equals(codeString))
      return DicomDcim._110817;
    if ("110818".equals(codeString))
      return DicomDcim._110818;
    if ("110819".equals(codeString))
      return DicomDcim._110819;
    if ("110820".equals(codeString))
      return DicomDcim._110820;
    if ("110821".equals(codeString))
      return DicomDcim._110821;
    if ("110822".equals(codeString))
      return DicomDcim._110822;
    if ("110823".equals(codeString))
      return DicomDcim._110823;
    if ("110824".equals(codeString))
      return DicomDcim._110824;
    if ("110825".equals(codeString))
      return DicomDcim._110825;
    if ("110826".equals(codeString))
      return DicomDcim._110826;
    if ("110827".equals(codeString))
      return DicomDcim._110827;
    if ("110828".equals(codeString))
      return DicomDcim._110828;
    if ("110829".equals(codeString))
      return DicomDcim._110829;
    if ("110830".equals(codeString))
      return DicomDcim._110830;
    if ("110831".equals(codeString))
      return DicomDcim._110831;
    if ("110832".equals(codeString))
      return DicomDcim._110832;
    if ("110833".equals(codeString))
      return DicomDcim._110833;
    if ("110834".equals(codeString))
      return DicomDcim._110834;
    if ("110835".equals(codeString))
      return DicomDcim._110835;
    if ("110836".equals(codeString))
      return DicomDcim._110836;
    if ("110837".equals(codeString))
      return DicomDcim._110837;
    if ("110838".equals(codeString))
      return DicomDcim._110838;
    if ("110839".equals(codeString))
      return DicomDcim._110839;
    if ("110840".equals(codeString))
      return DicomDcim._110840;
    if ("110841".equals(codeString))
      return DicomDcim._110841;
    if ("110842".equals(codeString))
      return DicomDcim._110842;
    if ("110843".equals(codeString))
      return DicomDcim._110843;
    if ("110844".equals(codeString))
      return DicomDcim._110844;
    if ("110845".equals(codeString))
      return DicomDcim._110845;
    if ("110846".equals(codeString))
      return DicomDcim._110846;
    if ("110847".equals(codeString))
      return DicomDcim._110847;
    if ("110848".equals(codeString))
      return DicomDcim._110848;
    if ("110849".equals(codeString))
      return DicomDcim._110849;
    if ("110850".equals(codeString))
      return DicomDcim._110850;
    if ("110851".equals(codeString))
      return DicomDcim._110851;
    if ("110852".equals(codeString))
      return DicomDcim._110852;
    if ("110853".equals(codeString))
      return DicomDcim._110853;
    if ("110854".equals(codeString))
      return DicomDcim._110854;
    if ("110855".equals(codeString))
      return DicomDcim._110855;
    if ("110856".equals(codeString))
      return DicomDcim._110856;
    if ("110857".equals(codeString))
      return DicomDcim._110857;
    if ("110858".equals(codeString))
      return DicomDcim._110858;
    if ("110859".equals(codeString))
      return DicomDcim._110859;
    if ("110860".equals(codeString))
      return DicomDcim._110860;
    if ("110861".equals(codeString))
      return DicomDcim._110861;
    if ("110862".equals(codeString))
      return DicomDcim._110862;
    if ("110863".equals(codeString))
      return DicomDcim._110863;
    if ("110864".equals(codeString))
      return DicomDcim._110864;
    if ("110865".equals(codeString))
      return DicomDcim._110865;
    if ("110866".equals(codeString))
      return DicomDcim._110866;
    if ("110867".equals(codeString))
      return DicomDcim._110867;
    if ("110868".equals(codeString))
      return DicomDcim._110868;
    if ("110869".equals(codeString))
      return DicomDcim._110869;
    if ("110870".equals(codeString))
      return DicomDcim._110870;
    if ("110871".equals(codeString))
      return DicomDcim._110871;
    if ("110872".equals(codeString))
      return DicomDcim._110872;
    if ("110873".equals(codeString))
      return DicomDcim._110873;
    if ("110874".equals(codeString))
      return DicomDcim._110874;
    if ("110875".equals(codeString))
      return DicomDcim._110875;
    if ("110876".equals(codeString))
      return DicomDcim._110876;
    if ("110877".equals(codeString))
      return DicomDcim._110877;
    if ("110901".equals(codeString))
      return DicomDcim._110901;
    if ("110902".equals(codeString))
      return DicomDcim._110902;
    if ("110903".equals(codeString))
      return DicomDcim._110903;
    if ("110904".equals(codeString))
      return DicomDcim._110904;
    if ("110905".equals(codeString))
      return DicomDcim._110905;
    if ("110906".equals(codeString))
      return DicomDcim._110906;
    if ("110907".equals(codeString))
      return DicomDcim._110907;
    if ("110908".equals(codeString))
      return DicomDcim._110908;
    if ("110909".equals(codeString))
      return DicomDcim._110909;
    if ("110910".equals(codeString))
      return DicomDcim._110910;
    if ("110911".equals(codeString))
      return DicomDcim._110911;
    if ("111001".equals(codeString))
      return DicomDcim._111001;
    if ("111002".equals(codeString))
      return DicomDcim._111002;
    if ("111003".equals(codeString))
      return DicomDcim._111003;
    if ("111004".equals(codeString))
      return DicomDcim._111004;
    if ("111005".equals(codeString))
      return DicomDcim._111005;
    if ("111006".equals(codeString))
      return DicomDcim._111006;
    if ("111007".equals(codeString))
      return DicomDcim._111007;
    if ("111008".equals(codeString))
      return DicomDcim._111008;
    if ("111009".equals(codeString))
      return DicomDcim._111009;
    if ("111010".equals(codeString))
      return DicomDcim._111010;
    if ("111011".equals(codeString))
      return DicomDcim._111011;
    if ("111012".equals(codeString))
      return DicomDcim._111012;
    if ("111013".equals(codeString))
      return DicomDcim._111013;
    if ("111014".equals(codeString))
      return DicomDcim._111014;
    if ("111015".equals(codeString))
      return DicomDcim._111015;
    if ("111016".equals(codeString))
      return DicomDcim._111016;
    if ("111017".equals(codeString))
      return DicomDcim._111017;
    if ("111018".equals(codeString))
      return DicomDcim._111018;
    if ("111019".equals(codeString))
      return DicomDcim._111019;
    if ("111020".equals(codeString))
      return DicomDcim._111020;
    if ("111021".equals(codeString))
      return DicomDcim._111021;
    if ("111022".equals(codeString))
      return DicomDcim._111022;
    if ("111023".equals(codeString))
      return DicomDcim._111023;
    if ("111024".equals(codeString))
      return DicomDcim._111024;
    if ("111025".equals(codeString))
      return DicomDcim._111025;
    if ("111026".equals(codeString))
      return DicomDcim._111026;
    if ("111027".equals(codeString))
      return DicomDcim._111027;
    if ("111028".equals(codeString))
      return DicomDcim._111028;
    if ("111029".equals(codeString))
      return DicomDcim._111029;
    if ("111030".equals(codeString))
      return DicomDcim._111030;
    if ("111031".equals(codeString))
      return DicomDcim._111031;
    if ("111032".equals(codeString))
      return DicomDcim._111032;
    if ("111033".equals(codeString))
      return DicomDcim._111033;
    if ("111034".equals(codeString))
      return DicomDcim._111034;
    if ("111035".equals(codeString))
      return DicomDcim._111035;
    if ("111036".equals(codeString))
      return DicomDcim._111036;
    if ("111037".equals(codeString))
      return DicomDcim._111037;
    if ("111038".equals(codeString))
      return DicomDcim._111038;
    if ("111039".equals(codeString))
      return DicomDcim._111039;
    if ("111040".equals(codeString))
      return DicomDcim._111040;
    if ("111041".equals(codeString))
      return DicomDcim._111041;
    if ("111042".equals(codeString))
      return DicomDcim._111042;
    if ("111043".equals(codeString))
      return DicomDcim._111043;
    if ("111044".equals(codeString))
      return DicomDcim._111044;
    if ("111045".equals(codeString))
      return DicomDcim._111045;
    if ("111046".equals(codeString))
      return DicomDcim._111046;
    if ("111047".equals(codeString))
      return DicomDcim._111047;
    if ("111048".equals(codeString))
      return DicomDcim._111048;
    if ("111049".equals(codeString))
      return DicomDcim._111049;
    if ("111050".equals(codeString))
      return DicomDcim._111050;
    if ("111051".equals(codeString))
      return DicomDcim._111051;
    if ("111052".equals(codeString))
      return DicomDcim._111052;
    if ("111053".equals(codeString))
      return DicomDcim._111053;
    if ("111054".equals(codeString))
      return DicomDcim._111054;
    if ("111055".equals(codeString))
      return DicomDcim._111055;
    if ("111056".equals(codeString))
      return DicomDcim._111056;
    if ("111057".equals(codeString))
      return DicomDcim._111057;
    if ("111058".equals(codeString))
      return DicomDcim._111058;
    if ("111059".equals(codeString))
      return DicomDcim._111059;
    if ("111060".equals(codeString))
      return DicomDcim._111060;
    if ("111061".equals(codeString))
      return DicomDcim._111061;
    if ("111062".equals(codeString))
      return DicomDcim._111062;
    if ("111063".equals(codeString))
      return DicomDcim._111063;
    if ("111064".equals(codeString))
      return DicomDcim._111064;
    if ("111065".equals(codeString))
      return DicomDcim._111065;
    if ("111066".equals(codeString))
      return DicomDcim._111066;
    if ("111069".equals(codeString))
      return DicomDcim._111069;
    if ("111071".equals(codeString))
      return DicomDcim._111071;
    if ("111072".equals(codeString))
      return DicomDcim._111072;
    if ("111081".equals(codeString))
      return DicomDcim._111081;
    if ("111086".equals(codeString))
      return DicomDcim._111086;
    if ("111087".equals(codeString))
      return DicomDcim._111087;
    if ("111088".equals(codeString))
      return DicomDcim._111088;
    if ("111089".equals(codeString))
      return DicomDcim._111089;
    if ("111090".equals(codeString))
      return DicomDcim._111090;
    if ("111091".equals(codeString))
      return DicomDcim._111091;
    if ("111092".equals(codeString))
      return DicomDcim._111092;
    if ("111093".equals(codeString))
      return DicomDcim._111093;
    if ("111099".equals(codeString))
      return DicomDcim._111099;
    if ("111100".equals(codeString))
      return DicomDcim._111100;
    if ("111101".equals(codeString))
      return DicomDcim._111101;
    if ("111102".equals(codeString))
      return DicomDcim._111102;
    if ("111103".equals(codeString))
      return DicomDcim._111103;
    if ("111104".equals(codeString))
      return DicomDcim._111104;
    if ("111105".equals(codeString))
      return DicomDcim._111105;
    if ("111111".equals(codeString))
      return DicomDcim._111111;
    if ("111112".equals(codeString))
      return DicomDcim._111112;
    if ("111113".equals(codeString))
      return DicomDcim._111113;
    if ("111120".equals(codeString))
      return DicomDcim._111120;
    if ("111121".equals(codeString))
      return DicomDcim._111121;
    if ("111122".equals(codeString))
      return DicomDcim._111122;
    if ("111123".equals(codeString))
      return DicomDcim._111123;
    if ("111124".equals(codeString))
      return DicomDcim._111124;
    if ("111125".equals(codeString))
      return DicomDcim._111125;
    if ("111126".equals(codeString))
      return DicomDcim._111126;
    if ("111127".equals(codeString))
      return DicomDcim._111127;
    if ("111128".equals(codeString))
      return DicomDcim._111128;
    if ("111129".equals(codeString))
      return DicomDcim._111129;
    if ("111130".equals(codeString))
      return DicomDcim._111130;
    if ("111135".equals(codeString))
      return DicomDcim._111135;
    if ("111136".equals(codeString))
      return DicomDcim._111136;
    if ("111137".equals(codeString))
      return DicomDcim._111137;
    if ("111138".equals(codeString))
      return DicomDcim._111138;
    if ("111139".equals(codeString))
      return DicomDcim._111139;
    if ("111140".equals(codeString))
      return DicomDcim._111140;
    if ("111141".equals(codeString))
      return DicomDcim._111141;
    if ("111142".equals(codeString))
      return DicomDcim._111142;
    if ("111143".equals(codeString))
      return DicomDcim._111143;
    if ("111144".equals(codeString))
      return DicomDcim._111144;
    if ("111145".equals(codeString))
      return DicomDcim._111145;
    if ("111146".equals(codeString))
      return DicomDcim._111146;
    if ("111147".equals(codeString))
      return DicomDcim._111147;
    if ("111148".equals(codeString))
      return DicomDcim._111148;
    if ("111149".equals(codeString))
      return DicomDcim._111149;
    if ("111150".equals(codeString))
      return DicomDcim._111150;
    if ("111151".equals(codeString))
      return DicomDcim._111151;
    if ("111152".equals(codeString))
      return DicomDcim._111152;
    if ("111153".equals(codeString))
      return DicomDcim._111153;
    if ("111154".equals(codeString))
      return DicomDcim._111154;
    if ("111155".equals(codeString))
      return DicomDcim._111155;
    if ("111156".equals(codeString))
      return DicomDcim._111156;
    if ("111157".equals(codeString))
      return DicomDcim._111157;
    if ("111158".equals(codeString))
      return DicomDcim._111158;
    if ("111159".equals(codeString))
      return DicomDcim._111159;
    if ("111168".equals(codeString))
      return DicomDcim._111168;
    if ("111170".equals(codeString))
      return DicomDcim._111170;
    if ("111171".equals(codeString))
      return DicomDcim._111171;
    if ("111172".equals(codeString))
      return DicomDcim._111172;
    if ("111173".equals(codeString))
      return DicomDcim._111173;
    if ("111174".equals(codeString))
      return DicomDcim._111174;
    if ("111175".equals(codeString))
      return DicomDcim._111175;
    if ("111176".equals(codeString))
      return DicomDcim._111176;
    if ("111177".equals(codeString))
      return DicomDcim._111177;
    if ("111178".equals(codeString))
      return DicomDcim._111178;
    if ("111179".equals(codeString))
      return DicomDcim._111179;
    if ("111180".equals(codeString))
      return DicomDcim._111180;
    if ("111181".equals(codeString))
      return DicomDcim._111181;
    if ("111182".equals(codeString))
      return DicomDcim._111182;
    if ("111183".equals(codeString))
      return DicomDcim._111183;
    if ("111184".equals(codeString))
      return DicomDcim._111184;
    if ("111185".equals(codeString))
      return DicomDcim._111185;
    if ("111186".equals(codeString))
      return DicomDcim._111186;
    if ("111187".equals(codeString))
      return DicomDcim._111187;
    if ("111188".equals(codeString))
      return DicomDcim._111188;
    if ("111189".equals(codeString))
      return DicomDcim._111189;
    if ("111190".equals(codeString))
      return DicomDcim._111190;
    if ("111191".equals(codeString))
      return DicomDcim._111191;
    if ("111192".equals(codeString))
      return DicomDcim._111192;
    if ("111193".equals(codeString))
      return DicomDcim._111193;
    if ("111194".equals(codeString))
      return DicomDcim._111194;
    if ("111195".equals(codeString))
      return DicomDcim._111195;
    if ("111196".equals(codeString))
      return DicomDcim._111196;
    if ("111197".equals(codeString))
      return DicomDcim._111197;
    if ("111198".equals(codeString))
      return DicomDcim._111198;
    if ("111199".equals(codeString))
      return DicomDcim._111199;
    if ("111200".equals(codeString))
      return DicomDcim._111200;
    if ("111201".equals(codeString))
      return DicomDcim._111201;
    if ("111202".equals(codeString))
      return DicomDcim._111202;
    if ("111203".equals(codeString))
      return DicomDcim._111203;
    if ("111204".equals(codeString))
      return DicomDcim._111204;
    if ("111205".equals(codeString))
      return DicomDcim._111205;
    if ("111206".equals(codeString))
      return DicomDcim._111206;
    if ("111207".equals(codeString))
      return DicomDcim._111207;
    if ("111208".equals(codeString))
      return DicomDcim._111208;
    if ("111209".equals(codeString))
      return DicomDcim._111209;
    if ("111210".equals(codeString))
      return DicomDcim._111210;
    if ("111211".equals(codeString))
      return DicomDcim._111211;
    if ("111212".equals(codeString))
      return DicomDcim._111212;
    if ("111213".equals(codeString))
      return DicomDcim._111213;
    if ("111214".equals(codeString))
      return DicomDcim._111214;
    if ("111215".equals(codeString))
      return DicomDcim._111215;
    if ("111216".equals(codeString))
      return DicomDcim._111216;
    if ("111217".equals(codeString))
      return DicomDcim._111217;
    if ("111218".equals(codeString))
      return DicomDcim._111218;
    if ("111219".equals(codeString))
      return DicomDcim._111219;
    if ("111220".equals(codeString))
      return DicomDcim._111220;
    if ("111221".equals(codeString))
      return DicomDcim._111221;
    if ("111222".equals(codeString))
      return DicomDcim._111222;
    if ("111223".equals(codeString))
      return DicomDcim._111223;
    if ("111224".equals(codeString))
      return DicomDcim._111224;
    if ("111225".equals(codeString))
      return DicomDcim._111225;
    if ("111233".equals(codeString))
      return DicomDcim._111233;
    if ("111234".equals(codeString))
      return DicomDcim._111234;
    if ("111235".equals(codeString))
      return DicomDcim._111235;
    if ("111236".equals(codeString))
      return DicomDcim._111236;
    if ("111237".equals(codeString))
      return DicomDcim._111237;
    if ("111238".equals(codeString))
      return DicomDcim._111238;
    if ("111239".equals(codeString))
      return DicomDcim._111239;
    if ("111240".equals(codeString))
      return DicomDcim._111240;
    if ("111241".equals(codeString))
      return DicomDcim._111241;
    if ("111242".equals(codeString))
      return DicomDcim._111242;
    if ("111243".equals(codeString))
      return DicomDcim._111243;
    if ("111244".equals(codeString))
      return DicomDcim._111244;
    if ("111245".equals(codeString))
      return DicomDcim._111245;
    if ("111248".equals(codeString))
      return DicomDcim._111248;
    if ("111249".equals(codeString))
      return DicomDcim._111249;
    if ("111250".equals(codeString))
      return DicomDcim._111250;
    if ("111251".equals(codeString))
      return DicomDcim._111251;
    if ("111252".equals(codeString))
      return DicomDcim._111252;
    if ("111253".equals(codeString))
      return DicomDcim._111253;
    if ("111254".equals(codeString))
      return DicomDcim._111254;
    if ("111255".equals(codeString))
      return DicomDcim._111255;
    if ("111256".equals(codeString))
      return DicomDcim._111256;
    if ("111257".equals(codeString))
      return DicomDcim._111257;
    if ("111258".equals(codeString))
      return DicomDcim._111258;
    if ("111259".equals(codeString))
      return DicomDcim._111259;
    if ("111260".equals(codeString))
      return DicomDcim._111260;
    if ("111262".equals(codeString))
      return DicomDcim._111262;
    if ("111263".equals(codeString))
      return DicomDcim._111263;
    if ("111264".equals(codeString))
      return DicomDcim._111264;
    if ("111265".equals(codeString))
      return DicomDcim._111265;
    if ("111269".equals(codeString))
      return DicomDcim._111269;
    if ("111271".equals(codeString))
      return DicomDcim._111271;
    if ("111273".equals(codeString))
      return DicomDcim._111273;
    if ("111277".equals(codeString))
      return DicomDcim._111277;
    if ("111278".equals(codeString))
      return DicomDcim._111278;
    if ("111279".equals(codeString))
      return DicomDcim._111279;
    if ("111281".equals(codeString))
      return DicomDcim._111281;
    if ("111283".equals(codeString))
      return DicomDcim._111283;
    if ("111284".equals(codeString))
      return DicomDcim._111284;
    if ("111285".equals(codeString))
      return DicomDcim._111285;
    if ("111286".equals(codeString))
      return DicomDcim._111286;
    if ("111287".equals(codeString))
      return DicomDcim._111287;
    if ("111288".equals(codeString))
      return DicomDcim._111288;
    if ("111290".equals(codeString))
      return DicomDcim._111290;
    if ("111291".equals(codeString))
      return DicomDcim._111291;
    if ("111292".equals(codeString))
      return DicomDcim._111292;
    if ("111293".equals(codeString))
      return DicomDcim._111293;
    if ("111294".equals(codeString))
      return DicomDcim._111294;
    if ("111296".equals(codeString))
      return DicomDcim._111296;
    if ("111297".equals(codeString))
      return DicomDcim._111297;
    if ("111298".equals(codeString))
      return DicomDcim._111298;
    if ("111299".equals(codeString))
      return DicomDcim._111299;
    if ("111300".equals(codeString))
      return DicomDcim._111300;
    if ("111301".equals(codeString))
      return DicomDcim._111301;
    if ("111302".equals(codeString))
      return DicomDcim._111302;
    if ("111303".equals(codeString))
      return DicomDcim._111303;
    if ("111304".equals(codeString))
      return DicomDcim._111304;
    if ("111305".equals(codeString))
      return DicomDcim._111305;
    if ("111306".equals(codeString))
      return DicomDcim._111306;
    if ("111307".equals(codeString))
      return DicomDcim._111307;
    if ("111308".equals(codeString))
      return DicomDcim._111308;
    if ("111309".equals(codeString))
      return DicomDcim._111309;
    if ("111310".equals(codeString))
      return DicomDcim._111310;
    if ("111311".equals(codeString))
      return DicomDcim._111311;
    if ("111312".equals(codeString))
      return DicomDcim._111312;
    if ("111313".equals(codeString))
      return DicomDcim._111313;
    if ("111314".equals(codeString))
      return DicomDcim._111314;
    if ("111315".equals(codeString))
      return DicomDcim._111315;
    if ("111316".equals(codeString))
      return DicomDcim._111316;
    if ("111317".equals(codeString))
      return DicomDcim._111317;
    if ("111318".equals(codeString))
      return DicomDcim._111318;
    if ("111320".equals(codeString))
      return DicomDcim._111320;
    if ("111321".equals(codeString))
      return DicomDcim._111321;
    if ("111322".equals(codeString))
      return DicomDcim._111322;
    if ("111323".equals(codeString))
      return DicomDcim._111323;
    if ("111324".equals(codeString))
      return DicomDcim._111324;
    if ("111325".equals(codeString))
      return DicomDcim._111325;
    if ("111326".equals(codeString))
      return DicomDcim._111326;
    if ("111327".equals(codeString))
      return DicomDcim._111327;
    if ("111328".equals(codeString))
      return DicomDcim._111328;
    if ("111329".equals(codeString))
      return DicomDcim._111329;
    if ("111330".equals(codeString))
      return DicomDcim._111330;
    if ("111331".equals(codeString))
      return DicomDcim._111331;
    if ("111332".equals(codeString))
      return DicomDcim._111332;
    if ("111333".equals(codeString))
      return DicomDcim._111333;
    if ("111334".equals(codeString))
      return DicomDcim._111334;
    if ("111335".equals(codeString))
      return DicomDcim._111335;
    if ("111336".equals(codeString))
      return DicomDcim._111336;
    if ("111338".equals(codeString))
      return DicomDcim._111338;
    if ("111340".equals(codeString))
      return DicomDcim._111340;
    if ("111341".equals(codeString))
      return DicomDcim._111341;
    if ("111342".equals(codeString))
      return DicomDcim._111342;
    if ("111343".equals(codeString))
      return DicomDcim._111343;
    if ("111344".equals(codeString))
      return DicomDcim._111344;
    if ("111345".equals(codeString))
      return DicomDcim._111345;
    if ("111346".equals(codeString))
      return DicomDcim._111346;
    if ("111347".equals(codeString))
      return DicomDcim._111347;
    if ("111350".equals(codeString))
      return DicomDcim._111350;
    if ("111351".equals(codeString))
      return DicomDcim._111351;
    if ("111352".equals(codeString))
      return DicomDcim._111352;
    if ("111353".equals(codeString))
      return DicomDcim._111353;
    if ("111354".equals(codeString))
      return DicomDcim._111354;
    if ("111355".equals(codeString))
      return DicomDcim._111355;
    if ("111356".equals(codeString))
      return DicomDcim._111356;
    if ("111357".equals(codeString))
      return DicomDcim._111357;
    if ("111358".equals(codeString))
      return DicomDcim._111358;
    if ("111359".equals(codeString))
      return DicomDcim._111359;
    if ("111360".equals(codeString))
      return DicomDcim._111360;
    if ("111361".equals(codeString))
      return DicomDcim._111361;
    if ("111362".equals(codeString))
      return DicomDcim._111362;
    if ("111363".equals(codeString))
      return DicomDcim._111363;
    if ("111364".equals(codeString))
      return DicomDcim._111364;
    if ("111365".equals(codeString))
      return DicomDcim._111365;
    if ("111366".equals(codeString))
      return DicomDcim._111366;
    if ("111367".equals(codeString))
      return DicomDcim._111367;
    if ("111368".equals(codeString))
      return DicomDcim._111368;
    if ("111369".equals(codeString))
      return DicomDcim._111369;
    if ("111370".equals(codeString))
      return DicomDcim._111370;
    if ("111371".equals(codeString))
      return DicomDcim._111371;
    if ("111372".equals(codeString))
      return DicomDcim._111372;
    if ("111373".equals(codeString))
      return DicomDcim._111373;
    if ("111374".equals(codeString))
      return DicomDcim._111374;
    if ("111375".equals(codeString))
      return DicomDcim._111375;
    if ("111376".equals(codeString))
      return DicomDcim._111376;
    if ("111377".equals(codeString))
      return DicomDcim._111377;
    if ("111380".equals(codeString))
      return DicomDcim._111380;
    if ("111381".equals(codeString))
      return DicomDcim._111381;
    if ("111382".equals(codeString))
      return DicomDcim._111382;
    if ("111383".equals(codeString))
      return DicomDcim._111383;
    if ("111384".equals(codeString))
      return DicomDcim._111384;
    if ("111385".equals(codeString))
      return DicomDcim._111385;
    if ("111386".equals(codeString))
      return DicomDcim._111386;
    if ("111387".equals(codeString))
      return DicomDcim._111387;
    if ("111388".equals(codeString))
      return DicomDcim._111388;
    if ("111389".equals(codeString))
      return DicomDcim._111389;
    if ("111390".equals(codeString))
      return DicomDcim._111390;
    if ("111391".equals(codeString))
      return DicomDcim._111391;
    if ("111392".equals(codeString))
      return DicomDcim._111392;
    if ("111393".equals(codeString))
      return DicomDcim._111393;
    if ("111394".equals(codeString))
      return DicomDcim._111394;
    if ("111395".equals(codeString))
      return DicomDcim._111395;
    if ("111396".equals(codeString))
      return DicomDcim._111396;
    if ("111397".equals(codeString))
      return DicomDcim._111397;
    if ("111398".equals(codeString))
      return DicomDcim._111398;
    if ("111399".equals(codeString))
      return DicomDcim._111399;
    if ("111400".equals(codeString))
      return DicomDcim._111400;
    if ("111401".equals(codeString))
      return DicomDcim._111401;
    if ("111402".equals(codeString))
      return DicomDcim._111402;
    if ("111403".equals(codeString))
      return DicomDcim._111403;
    if ("111404".equals(codeString))
      return DicomDcim._111404;
    if ("111405".equals(codeString))
      return DicomDcim._111405;
    if ("111406".equals(codeString))
      return DicomDcim._111406;
    if ("111407".equals(codeString))
      return DicomDcim._111407;
    if ("111408".equals(codeString))
      return DicomDcim._111408;
    if ("111409".equals(codeString))
      return DicomDcim._111409;
    if ("111410".equals(codeString))
      return DicomDcim._111410;
    if ("111411".equals(codeString))
      return DicomDcim._111411;
    if ("111412".equals(codeString))
      return DicomDcim._111412;
    if ("111413".equals(codeString))
      return DicomDcim._111413;
    if ("111414".equals(codeString))
      return DicomDcim._111414;
    if ("111415".equals(codeString))
      return DicomDcim._111415;
    if ("111416".equals(codeString))
      return DicomDcim._111416;
    if ("111417".equals(codeString))
      return DicomDcim._111417;
    if ("111418".equals(codeString))
      return DicomDcim._111418;
    if ("111419".equals(codeString))
      return DicomDcim._111419;
    if ("111420".equals(codeString))
      return DicomDcim._111420;
    if ("111421".equals(codeString))
      return DicomDcim._111421;
    if ("111423".equals(codeString))
      return DicomDcim._111423;
    if ("111424".equals(codeString))
      return DicomDcim._111424;
    if ("111425".equals(codeString))
      return DicomDcim._111425;
    if ("111426".equals(codeString))
      return DicomDcim._111426;
    if ("111427".equals(codeString))
      return DicomDcim._111427;
    if ("111428".equals(codeString))
      return DicomDcim._111428;
    if ("111429".equals(codeString))
      return DicomDcim._111429;
    if ("111430".equals(codeString))
      return DicomDcim._111430;
    if ("111431".equals(codeString))
      return DicomDcim._111431;
    if ("111432".equals(codeString))
      return DicomDcim._111432;
    if ("111433".equals(codeString))
      return DicomDcim._111433;
    if ("111434".equals(codeString))
      return DicomDcim._111434;
    if ("111435".equals(codeString))
      return DicomDcim._111435;
    if ("111436".equals(codeString))
      return DicomDcim._111436;
    if ("111437".equals(codeString))
      return DicomDcim._111437;
    if ("111438".equals(codeString))
      return DicomDcim._111438;
    if ("111439".equals(codeString))
      return DicomDcim._111439;
    if ("111440".equals(codeString))
      return DicomDcim._111440;
    if ("111441".equals(codeString))
      return DicomDcim._111441;
    if ("111442".equals(codeString))
      return DicomDcim._111442;
    if ("111443".equals(codeString))
      return DicomDcim._111443;
    if ("111444".equals(codeString))
      return DicomDcim._111444;
    if ("111445".equals(codeString))
      return DicomDcim._111445;
    if ("111446".equals(codeString))
      return DicomDcim._111446;
    if ("111447".equals(codeString))
      return DicomDcim._111447;
    if ("111448".equals(codeString))
      return DicomDcim._111448;
    if ("111449".equals(codeString))
      return DicomDcim._111449;
    if ("111450".equals(codeString))
      return DicomDcim._111450;
    if ("111451".equals(codeString))
      return DicomDcim._111451;
    if ("111452".equals(codeString))
      return DicomDcim._111452;
    if ("111453".equals(codeString))
      return DicomDcim._111453;
    if ("111454".equals(codeString))
      return DicomDcim._111454;
    if ("111455".equals(codeString))
      return DicomDcim._111455;
    if ("111456".equals(codeString))
      return DicomDcim._111456;
    if ("111457".equals(codeString))
      return DicomDcim._111457;
    if ("111458".equals(codeString))
      return DicomDcim._111458;
    if ("111459".equals(codeString))
      return DicomDcim._111459;
    if ("111460".equals(codeString))
      return DicomDcim._111460;
    if ("111461".equals(codeString))
      return DicomDcim._111461;
    if ("111462".equals(codeString))
      return DicomDcim._111462;
    if ("111463".equals(codeString))
      return DicomDcim._111463;
    if ("111464".equals(codeString))
      return DicomDcim._111464;
    if ("111465".equals(codeString))
      return DicomDcim._111465;
    if ("111466".equals(codeString))
      return DicomDcim._111466;
    if ("111467".equals(codeString))
      return DicomDcim._111467;
    if ("111468".equals(codeString))
      return DicomDcim._111468;
    if ("111469".equals(codeString))
      return DicomDcim._111469;
    if ("111470".equals(codeString))
      return DicomDcim._111470;
    if ("111471".equals(codeString))
      return DicomDcim._111471;
    if ("111472".equals(codeString))
      return DicomDcim._111472;
    if ("111473".equals(codeString))
      return DicomDcim._111473;
    if ("111474".equals(codeString))
      return DicomDcim._111474;
    if ("111475".equals(codeString))
      return DicomDcim._111475;
    if ("111476".equals(codeString))
      return DicomDcim._111476;
    if ("111477".equals(codeString))
      return DicomDcim._111477;
    if ("111478".equals(codeString))
      return DicomDcim._111478;
    if ("111479".equals(codeString))
      return DicomDcim._111479;
    if ("111480".equals(codeString))
      return DicomDcim._111480;
    if ("111481".equals(codeString))
      return DicomDcim._111481;
    if ("111482".equals(codeString))
      return DicomDcim._111482;
    if ("111483".equals(codeString))
      return DicomDcim._111483;
    if ("111484".equals(codeString))
      return DicomDcim._111484;
    if ("111485".equals(codeString))
      return DicomDcim._111485;
    if ("111486".equals(codeString))
      return DicomDcim._111486;
    if ("111487".equals(codeString))
      return DicomDcim._111487;
    if ("111488".equals(codeString))
      return DicomDcim._111488;
    if ("111489".equals(codeString))
      return DicomDcim._111489;
    if ("111490".equals(codeString))
      return DicomDcim._111490;
    if ("111491".equals(codeString))
      return DicomDcim._111491;
    if ("111492".equals(codeString))
      return DicomDcim._111492;
    if ("111494".equals(codeString))
      return DicomDcim._111494;
    if ("111495".equals(codeString))
      return DicomDcim._111495;
    if ("111496".equals(codeString))
      return DicomDcim._111496;
    if ("111497".equals(codeString))
      return DicomDcim._111497;
    if ("111498".equals(codeString))
      return DicomDcim._111498;
    if ("111499".equals(codeString))
      return DicomDcim._111499;
    if ("111500".equals(codeString))
      return DicomDcim._111500;
    if ("111501".equals(codeString))
      return DicomDcim._111501;
    if ("111502".equals(codeString))
      return DicomDcim._111502;
    if ("111503".equals(codeString))
      return DicomDcim._111503;
    if ("111504".equals(codeString))
      return DicomDcim._111504;
    if ("111505".equals(codeString))
      return DicomDcim._111505;
    if ("111506".equals(codeString))
      return DicomDcim._111506;
    if ("111507".equals(codeString))
      return DicomDcim._111507;
    if ("111508".equals(codeString))
      return DicomDcim._111508;
    if ("111509".equals(codeString))
      return DicomDcim._111509;
    if ("111510".equals(codeString))
      return DicomDcim._111510;
    if ("111511".equals(codeString))
      return DicomDcim._111511;
    if ("111512".equals(codeString))
      return DicomDcim._111512;
    if ("111513".equals(codeString))
      return DicomDcim._111513;
    if ("111514".equals(codeString))
      return DicomDcim._111514;
    if ("111515".equals(codeString))
      return DicomDcim._111515;
    if ("111516".equals(codeString))
      return DicomDcim._111516;
    if ("111517".equals(codeString))
      return DicomDcim._111517;
    if ("111518".equals(codeString))
      return DicomDcim._111518;
    if ("111519".equals(codeString))
      return DicomDcim._111519;
    if ("111520".equals(codeString))
      return DicomDcim._111520;
    if ("111521".equals(codeString))
      return DicomDcim._111521;
    if ("111522".equals(codeString))
      return DicomDcim._111522;
    if ("111523".equals(codeString))
      return DicomDcim._111523;
    if ("111524".equals(codeString))
      return DicomDcim._111524;
    if ("111525".equals(codeString))
      return DicomDcim._111525;
    if ("111526".equals(codeString))
      return DicomDcim._111526;
    if ("111527".equals(codeString))
      return DicomDcim._111527;
    if ("111528".equals(codeString))
      return DicomDcim._111528;
    if ("111529".equals(codeString))
      return DicomDcim._111529;
    if ("111530".equals(codeString))
      return DicomDcim._111530;
    if ("111531".equals(codeString))
      return DicomDcim._111531;
    if ("111532".equals(codeString))
      return DicomDcim._111532;
    if ("111533".equals(codeString))
      return DicomDcim._111533;
    if ("111534".equals(codeString))
      return DicomDcim._111534;
    if ("111535".equals(codeString))
      return DicomDcim._111535;
    if ("111536".equals(codeString))
      return DicomDcim._111536;
    if ("111537".equals(codeString))
      return DicomDcim._111537;
    if ("111538".equals(codeString))
      return DicomDcim._111538;
    if ("111539".equals(codeString))
      return DicomDcim._111539;
    if ("111540".equals(codeString))
      return DicomDcim._111540;
    if ("111541".equals(codeString))
      return DicomDcim._111541;
    if ("111542".equals(codeString))
      return DicomDcim._111542;
    if ("111543".equals(codeString))
      return DicomDcim._111543;
    if ("111544".equals(codeString))
      return DicomDcim._111544;
    if ("111545".equals(codeString))
      return DicomDcim._111545;
    if ("111546".equals(codeString))
      return DicomDcim._111546;
    if ("111547".equals(codeString))
      return DicomDcim._111547;
    if ("111548".equals(codeString))
      return DicomDcim._111548;
    if ("111549".equals(codeString))
      return DicomDcim._111549;
    if ("111550".equals(codeString))
      return DicomDcim._111550;
    if ("111551".equals(codeString))
      return DicomDcim._111551;
    if ("111552".equals(codeString))
      return DicomDcim._111552;
    if ("111553".equals(codeString))
      return DicomDcim._111553;
    if ("111554".equals(codeString))
      return DicomDcim._111554;
    if ("111555".equals(codeString))
      return DicomDcim._111555;
    if ("111556".equals(codeString))
      return DicomDcim._111556;
    if ("111557".equals(codeString))
      return DicomDcim._111557;
    if ("111558".equals(codeString))
      return DicomDcim._111558;
    if ("111559".equals(codeString))
      return DicomDcim._111559;
    if ("111560".equals(codeString))
      return DicomDcim._111560;
    if ("111561".equals(codeString))
      return DicomDcim._111561;
    if ("111562".equals(codeString))
      return DicomDcim._111562;
    if ("111563".equals(codeString))
      return DicomDcim._111563;
    if ("111564".equals(codeString))
      return DicomDcim._111564;
    if ("111565".equals(codeString))
      return DicomDcim._111565;
    if ("111566".equals(codeString))
      return DicomDcim._111566;
    if ("111567".equals(codeString))
      return DicomDcim._111567;
    if ("111568".equals(codeString))
      return DicomDcim._111568;
    if ("111569".equals(codeString))
      return DicomDcim._111569;
    if ("111570".equals(codeString))
      return DicomDcim._111570;
    if ("111571".equals(codeString))
      return DicomDcim._111571;
    if ("111572".equals(codeString))
      return DicomDcim._111572;
    if ("111573".equals(codeString))
      return DicomDcim._111573;
    if ("111574".equals(codeString))
      return DicomDcim._111574;
    if ("111575".equals(codeString))
      return DicomDcim._111575;
    if ("111576".equals(codeString))
      return DicomDcim._111576;
    if ("111577".equals(codeString))
      return DicomDcim._111577;
    if ("111578".equals(codeString))
      return DicomDcim._111578;
    if ("111579".equals(codeString))
      return DicomDcim._111579;
    if ("111580".equals(codeString))
      return DicomDcim._111580;
    if ("111581".equals(codeString))
      return DicomDcim._111581;
    if ("111582".equals(codeString))
      return DicomDcim._111582;
    if ("111583".equals(codeString))
      return DicomDcim._111583;
    if ("111584".equals(codeString))
      return DicomDcim._111584;
    if ("111585".equals(codeString))
      return DicomDcim._111585;
    if ("111586".equals(codeString))
      return DicomDcim._111586;
    if ("111587".equals(codeString))
      return DicomDcim._111587;
    if ("111590".equals(codeString))
      return DicomDcim._111590;
    if ("111591".equals(codeString))
      return DicomDcim._111591;
    if ("111592".equals(codeString))
      return DicomDcim._111592;
    if ("111593".equals(codeString))
      return DicomDcim._111593;
    if ("111601".equals(codeString))
      return DicomDcim._111601;
    if ("111602".equals(codeString))
      return DicomDcim._111602;
    if ("111603".equals(codeString))
      return DicomDcim._111603;
    if ("111604".equals(codeString))
      return DicomDcim._111604;
    if ("111605".equals(codeString))
      return DicomDcim._111605;
    if ("111606".equals(codeString))
      return DicomDcim._111606;
    if ("111607".equals(codeString))
      return DicomDcim._111607;
    if ("111609".equals(codeString))
      return DicomDcim._111609;
    if ("111621".equals(codeString))
      return DicomDcim._111621;
    if ("111622".equals(codeString))
      return DicomDcim._111622;
    if ("111623".equals(codeString))
      return DicomDcim._111623;
    if ("111625".equals(codeString))
      return DicomDcim._111625;
    if ("111626".equals(codeString))
      return DicomDcim._111626;
    if ("111627".equals(codeString))
      return DicomDcim._111627;
    if ("111628".equals(codeString))
      return DicomDcim._111628;
    if ("111629".equals(codeString))
      return DicomDcim._111629;
    if ("111630".equals(codeString))
      return DicomDcim._111630;
    if ("111631".equals(codeString))
      return DicomDcim._111631;
    if ("111632".equals(codeString))
      return DicomDcim._111632;
    if ("111633".equals(codeString))
      return DicomDcim._111633;
    if ("111634".equals(codeString))
      return DicomDcim._111634;
    if ("111635".equals(codeString))
      return DicomDcim._111635;
    if ("111636".equals(codeString))
      return DicomDcim._111636;
    if ("111637".equals(codeString))
      return DicomDcim._111637;
    if ("111638".equals(codeString))
      return DicomDcim._111638;
    if ("111641".equals(codeString))
      return DicomDcim._111641;
    if ("111642".equals(codeString))
      return DicomDcim._111642;
    if ("111643".equals(codeString))
      return DicomDcim._111643;
    if ("111644".equals(codeString))
      return DicomDcim._111644;
    if ("111645".equals(codeString))
      return DicomDcim._111645;
    if ("111646".equals(codeString))
      return DicomDcim._111646;
    if ("111671".equals(codeString))
      return DicomDcim._111671;
    if ("111672".equals(codeString))
      return DicomDcim._111672;
    if ("111673".equals(codeString))
      return DicomDcim._111673;
    if ("111674".equals(codeString))
      return DicomDcim._111674;
    if ("111675".equals(codeString))
      return DicomDcim._111675;
    if ("111676".equals(codeString))
      return DicomDcim._111676;
    if ("111677".equals(codeString))
      return DicomDcim._111677;
    if ("111678".equals(codeString))
      return DicomDcim._111678;
    if ("111679".equals(codeString))
      return DicomDcim._111679;
    if ("111680".equals(codeString))
      return DicomDcim._111680;
    if ("111685".equals(codeString))
      return DicomDcim._111685;
    if ("111686".equals(codeString))
      return DicomDcim._111686;
    if ("111687".equals(codeString))
      return DicomDcim._111687;
    if ("111688".equals(codeString))
      return DicomDcim._111688;
    if ("111689".equals(codeString))
      return DicomDcim._111689;
    if ("111690".equals(codeString))
      return DicomDcim._111690;
    if ("111691".equals(codeString))
      return DicomDcim._111691;
    if ("111692".equals(codeString))
      return DicomDcim._111692;
    if ("111693".equals(codeString))
      return DicomDcim._111693;
    if ("111694".equals(codeString))
      return DicomDcim._111694;
    if ("111695".equals(codeString))
      return DicomDcim._111695;
    if ("111696".equals(codeString))
      return DicomDcim._111696;
    if ("111697".equals(codeString))
      return DicomDcim._111697;
    if ("111698".equals(codeString))
      return DicomDcim._111698;
    if ("111700".equals(codeString))
      return DicomDcim._111700;
    if ("111701".equals(codeString))
      return DicomDcim._111701;
    if ("111702".equals(codeString))
      return DicomDcim._111702;
    if ("111703".equals(codeString))
      return DicomDcim._111703;
    if ("111704".equals(codeString))
      return DicomDcim._111704;
    if ("111705".equals(codeString))
      return DicomDcim._111705;
    if ("111706".equals(codeString))
      return DicomDcim._111706;
    if ("111707".equals(codeString))
      return DicomDcim._111707;
    if ("111708".equals(codeString))
      return DicomDcim._111708;
    if ("111709".equals(codeString))
      return DicomDcim._111709;
    if ("111710".equals(codeString))
      return DicomDcim._111710;
    if ("111711".equals(codeString))
      return DicomDcim._111711;
    if ("111712".equals(codeString))
      return DicomDcim._111712;
    if ("111718".equals(codeString))
      return DicomDcim._111718;
    if ("111719".equals(codeString))
      return DicomDcim._111719;
    if ("111720".equals(codeString))
      return DicomDcim._111720;
    if ("111721".equals(codeString))
      return DicomDcim._111721;
    if ("111723".equals(codeString))
      return DicomDcim._111723;
    if ("111724".equals(codeString))
      return DicomDcim._111724;
    if ("111726".equals(codeString))
      return DicomDcim._111726;
    if ("111727".equals(codeString))
      return DicomDcim._111727;
    if ("111729".equals(codeString))
      return DicomDcim._111729;
    if ("111741".equals(codeString))
      return DicomDcim._111741;
    if ("111742".equals(codeString))
      return DicomDcim._111742;
    if ("111743".equals(codeString))
      return DicomDcim._111743;
    if ("111744".equals(codeString))
      return DicomDcim._111744;
    if ("111745".equals(codeString))
      return DicomDcim._111745;
    if ("111746".equals(codeString))
      return DicomDcim._111746;
    if ("111747".equals(codeString))
      return DicomDcim._111747;
    if ("111748".equals(codeString))
      return DicomDcim._111748;
    if ("111749".equals(codeString))
      return DicomDcim._111749;
    if ("111750".equals(codeString))
      return DicomDcim._111750;
    if ("111751".equals(codeString))
      return DicomDcim._111751;
    if ("111752".equals(codeString))
      return DicomDcim._111752;
    if ("111753".equals(codeString))
      return DicomDcim._111753;
    if ("111754".equals(codeString))
      return DicomDcim._111754;
    if ("111755".equals(codeString))
      return DicomDcim._111755;
    if ("111756".equals(codeString))
      return DicomDcim._111756;
    if ("111760".equals(codeString))
      return DicomDcim._111760;
    if ("111761".equals(codeString))
      return DicomDcim._111761;
    if ("111762".equals(codeString))
      return DicomDcim._111762;
    if ("111763".equals(codeString))
      return DicomDcim._111763;
    if ("111764".equals(codeString))
      return DicomDcim._111764;
    if ("111765".equals(codeString))
      return DicomDcim._111765;
    if ("111766".equals(codeString))
      return DicomDcim._111766;
    if ("111767".equals(codeString))
      return DicomDcim._111767;
    if ("111768".equals(codeString))
      return DicomDcim._111768;
    if ("111769".equals(codeString))
      return DicomDcim._111769;
    if ("111770".equals(codeString))
      return DicomDcim._111770;
    if ("111771".equals(codeString))
      return DicomDcim._111771;
    if ("111772".equals(codeString))
      return DicomDcim._111772;
    if ("111773".equals(codeString))
      return DicomDcim._111773;
    if ("111776".equals(codeString))
      return DicomDcim._111776;
    if ("111777".equals(codeString))
      return DicomDcim._111777;
    if ("111778".equals(codeString))
      return DicomDcim._111778;
    if ("111779".equals(codeString))
      return DicomDcim._111779;
    if ("111780".equals(codeString))
      return DicomDcim._111780;
    if ("111781".equals(codeString))
      return DicomDcim._111781;
    if ("111782".equals(codeString))
      return DicomDcim._111782;
    if ("111783".equals(codeString))
      return DicomDcim._111783;
    if ("111786".equals(codeString))
      return DicomDcim._111786;
    if ("111787".equals(codeString))
      return DicomDcim._111787;
    if ("111791".equals(codeString))
      return DicomDcim._111791;
    if ("111792".equals(codeString))
      return DicomDcim._111792;
    if ("111800".equals(codeString))
      return DicomDcim._111800;
    if ("111801".equals(codeString))
      return DicomDcim._111801;
    if ("111802".equals(codeString))
      return DicomDcim._111802;
    if ("111803".equals(codeString))
      return DicomDcim._111803;
    if ("111804".equals(codeString))
      return DicomDcim._111804;
    if ("111805".equals(codeString))
      return DicomDcim._111805;
    if ("111806".equals(codeString))
      return DicomDcim._111806;
    if ("111807".equals(codeString))
      return DicomDcim._111807;
    if ("111808".equals(codeString))
      return DicomDcim._111808;
    if ("111809".equals(codeString))
      return DicomDcim._111809;
    if ("111810".equals(codeString))
      return DicomDcim._111810;
    if ("111811".equals(codeString))
      return DicomDcim._111811;
    if ("111812".equals(codeString))
      return DicomDcim._111812;
    if ("111813".equals(codeString))
      return DicomDcim._111813;
    if ("111814".equals(codeString))
      return DicomDcim._111814;
    if ("111815".equals(codeString))
      return DicomDcim._111815;
    if ("111816".equals(codeString))
      return DicomDcim._111816;
    if ("111817".equals(codeString))
      return DicomDcim._111817;
    if ("111818".equals(codeString))
      return DicomDcim._111818;
    if ("111819".equals(codeString))
      return DicomDcim._111819;
    if ("111820".equals(codeString))
      return DicomDcim._111820;
    if ("111821".equals(codeString))
      return DicomDcim._111821;
    if ("111822".equals(codeString))
      return DicomDcim._111822;
    if ("111823".equals(codeString))
      return DicomDcim._111823;
    if ("111824".equals(codeString))
      return DicomDcim._111824;
    if ("111825".equals(codeString))
      return DicomDcim._111825;
    if ("111826".equals(codeString))
      return DicomDcim._111826;
    if ("111827".equals(codeString))
      return DicomDcim._111827;
    if ("111828".equals(codeString))
      return DicomDcim._111828;
    if ("111829".equals(codeString))
      return DicomDcim._111829;
    if ("111830".equals(codeString))
      return DicomDcim._111830;
    if ("111831".equals(codeString))
      return DicomDcim._111831;
    if ("111832".equals(codeString))
      return DicomDcim._111832;
    if ("111833".equals(codeString))
      return DicomDcim._111833;
    if ("111834".equals(codeString))
      return DicomDcim._111834;
    if ("111835".equals(codeString))
      return DicomDcim._111835;
    if ("111836".equals(codeString))
      return DicomDcim._111836;
    if ("111837".equals(codeString))
      return DicomDcim._111837;
    if ("111838".equals(codeString))
      return DicomDcim._111838;
    if ("111839".equals(codeString))
      return DicomDcim._111839;
    if ("111840".equals(codeString))
      return DicomDcim._111840;
    if ("111841".equals(codeString))
      return DicomDcim._111841;
    if ("111842".equals(codeString))
      return DicomDcim._111842;
    if ("111843".equals(codeString))
      return DicomDcim._111843;
    if ("111844".equals(codeString))
      return DicomDcim._111844;
    if ("111845".equals(codeString))
      return DicomDcim._111845;
    if ("111846".equals(codeString))
      return DicomDcim._111846;
    if ("111847".equals(codeString))
      return DicomDcim._111847;
    if ("111848".equals(codeString))
      return DicomDcim._111848;
    if ("111849".equals(codeString))
      return DicomDcim._111849;
    if ("111850".equals(codeString))
      return DicomDcim._111850;
    if ("111851".equals(codeString))
      return DicomDcim._111851;
    if ("111852".equals(codeString))
      return DicomDcim._111852;
    if ("111853".equals(codeString))
      return DicomDcim._111853;
    if ("111854".equals(codeString))
      return DicomDcim._111854;
    if ("111855".equals(codeString))
      return DicomDcim._111855;
    if ("111856".equals(codeString))
      return DicomDcim._111856;
    if ("111900".equals(codeString))
      return DicomDcim._111900;
    if ("111901".equals(codeString))
      return DicomDcim._111901;
    if ("111902".equals(codeString))
      return DicomDcim._111902;
    if ("111903".equals(codeString))
      return DicomDcim._111903;
    if ("111904".equals(codeString))
      return DicomDcim._111904;
    if ("111905".equals(codeString))
      return DicomDcim._111905;
    if ("111906".equals(codeString))
      return DicomDcim._111906;
    if ("111907".equals(codeString))
      return DicomDcim._111907;
    if ("111908".equals(codeString))
      return DicomDcim._111908;
    if ("111909".equals(codeString))
      return DicomDcim._111909;
    if ("111910".equals(codeString))
      return DicomDcim._111910;
    if ("111911".equals(codeString))
      return DicomDcim._111911;
    if ("111912".equals(codeString))
      return DicomDcim._111912;
    if ("111913".equals(codeString))
      return DicomDcim._111913;
    if ("111914".equals(codeString))
      return DicomDcim._111914;
    if ("111915".equals(codeString))
      return DicomDcim._111915;
    if ("111916".equals(codeString))
      return DicomDcim._111916;
    if ("111917".equals(codeString))
      return DicomDcim._111917;
    if ("111918".equals(codeString))
      return DicomDcim._111918;
    if ("111919".equals(codeString))
      return DicomDcim._111919;
    if ("111920".equals(codeString))
      return DicomDcim._111920;
    if ("111921".equals(codeString))
      return DicomDcim._111921;
    if ("111922".equals(codeString))
      return DicomDcim._111922;
    if ("111923".equals(codeString))
      return DicomDcim._111923;
    if ("111924".equals(codeString))
      return DicomDcim._111924;
    if ("111925".equals(codeString))
      return DicomDcim._111925;
    if ("111926".equals(codeString))
      return DicomDcim._111926;
    if ("111927".equals(codeString))
      return DicomDcim._111927;
    if ("111928".equals(codeString))
      return DicomDcim._111928;
    if ("111929".equals(codeString))
      return DicomDcim._111929;
    if ("111930".equals(codeString))
      return DicomDcim._111930;
    if ("111931".equals(codeString))
      return DicomDcim._111931;
    if ("111932".equals(codeString))
      return DicomDcim._111932;
    if ("111933".equals(codeString))
      return DicomDcim._111933;
    if ("111934".equals(codeString))
      return DicomDcim._111934;
    if ("111935".equals(codeString))
      return DicomDcim._111935;
    if ("111936".equals(codeString))
      return DicomDcim._111936;
    if ("111937".equals(codeString))
      return DicomDcim._111937;
    if ("111938".equals(codeString))
      return DicomDcim._111938;
    if ("111939".equals(codeString))
      return DicomDcim._111939;
    if ("111940".equals(codeString))
      return DicomDcim._111940;
    if ("111941".equals(codeString))
      return DicomDcim._111941;
    if ("111942".equals(codeString))
      return DicomDcim._111942;
    if ("111943".equals(codeString))
      return DicomDcim._111943;
    if ("111944".equals(codeString))
      return DicomDcim._111944;
    if ("111945".equals(codeString))
      return DicomDcim._111945;
    if ("111946".equals(codeString))
      return DicomDcim._111946;
    if ("111947".equals(codeString))
      return DicomDcim._111947;
    if ("112000".equals(codeString))
      return DicomDcim._112000;
    if ("112001".equals(codeString))
      return DicomDcim._112001;
    if ("112002".equals(codeString))
      return DicomDcim._112002;
    if ("112003".equals(codeString))
      return DicomDcim._112003;
    if ("112004".equals(codeString))
      return DicomDcim._112004;
    if ("112005".equals(codeString))
      return DicomDcim._112005;
    if ("112006".equals(codeString))
      return DicomDcim._112006;
    if ("112007".equals(codeString))
      return DicomDcim._112007;
    if ("112008".equals(codeString))
      return DicomDcim._112008;
    if ("112009".equals(codeString))
      return DicomDcim._112009;
    if ("112010".equals(codeString))
      return DicomDcim._112010;
    if ("112011".equals(codeString))
      return DicomDcim._112011;
    if ("112012".equals(codeString))
      return DicomDcim._112012;
    if ("112013".equals(codeString))
      return DicomDcim._112013;
    if ("112014".equals(codeString))
      return DicomDcim._112014;
    if ("112015".equals(codeString))
      return DicomDcim._112015;
    if ("112016".equals(codeString))
      return DicomDcim._112016;
    if ("112017".equals(codeString))
      return DicomDcim._112017;
    if ("112018".equals(codeString))
      return DicomDcim._112018;
    if ("112019".equals(codeString))
      return DicomDcim._112019;
    if ("112020".equals(codeString))
      return DicomDcim._112020;
    if ("112021".equals(codeString))
      return DicomDcim._112021;
    if ("112022".equals(codeString))
      return DicomDcim._112022;
    if ("112023".equals(codeString))
      return DicomDcim._112023;
    if ("112024".equals(codeString))
      return DicomDcim._112024;
    if ("112025".equals(codeString))
      return DicomDcim._112025;
    if ("112026".equals(codeString))
      return DicomDcim._112026;
    if ("112027".equals(codeString))
      return DicomDcim._112027;
    if ("112028".equals(codeString))
      return DicomDcim._112028;
    if ("112029".equals(codeString))
      return DicomDcim._112029;
    if ("112030".equals(codeString))
      return DicomDcim._112030;
    if ("112031".equals(codeString))
      return DicomDcim._112031;
    if ("112032".equals(codeString))
      return DicomDcim._112032;
    if ("112033".equals(codeString))
      return DicomDcim._112033;
    if ("112034".equals(codeString))
      return DicomDcim._112034;
    if ("112035".equals(codeString))
      return DicomDcim._112035;
    if ("112036".equals(codeString))
      return DicomDcim._112036;
    if ("112037".equals(codeString))
      return DicomDcim._112037;
    if ("112038".equals(codeString))
      return DicomDcim._112038;
    if ("112039".equals(codeString))
      return DicomDcim._112039;
    if ("112040".equals(codeString))
      return DicomDcim._112040;
    if ("112041".equals(codeString))
      return DicomDcim._112041;
    if ("112042".equals(codeString))
      return DicomDcim._112042;
    if ("112043".equals(codeString))
      return DicomDcim._112043;
    if ("112044".equals(codeString))
      return DicomDcim._112044;
    if ("112045".equals(codeString))
      return DicomDcim._112045;
    if ("112046".equals(codeString))
      return DicomDcim._112046;
    if ("112047".equals(codeString))
      return DicomDcim._112047;
    if ("112048".equals(codeString))
      return DicomDcim._112048;
    if ("112049".equals(codeString))
      return DicomDcim._112049;
    if ("112050".equals(codeString))
      return DicomDcim._112050;
    if ("112051".equals(codeString))
      return DicomDcim._112051;
    if ("112052".equals(codeString))
      return DicomDcim._112052;
    if ("112053".equals(codeString))
      return DicomDcim._112053;
    if ("112054".equals(codeString))
      return DicomDcim._112054;
    if ("112055".equals(codeString))
      return DicomDcim._112055;
    if ("112056".equals(codeString))
      return DicomDcim._112056;
    if ("112057".equals(codeString))
      return DicomDcim._112057;
    if ("112058".equals(codeString))
      return DicomDcim._112058;
    if ("112059".equals(codeString))
      return DicomDcim._112059;
    if ("112060".equals(codeString))
      return DicomDcim._112060;
    if ("112061".equals(codeString))
      return DicomDcim._112061;
    if ("112062".equals(codeString))
      return DicomDcim._112062;
    if ("112063".equals(codeString))
      return DicomDcim._112063;
    if ("112064".equals(codeString))
      return DicomDcim._112064;
    if ("112065".equals(codeString))
      return DicomDcim._112065;
    if ("112066".equals(codeString))
      return DicomDcim._112066;
    if ("112067".equals(codeString))
      return DicomDcim._112067;
    if ("112068".equals(codeString))
      return DicomDcim._112068;
    if ("112069".equals(codeString))
      return DicomDcim._112069;
    if ("112070".equals(codeString))
      return DicomDcim._112070;
    if ("112071".equals(codeString))
      return DicomDcim._112071;
    if ("112072".equals(codeString))
      return DicomDcim._112072;
    if ("112073".equals(codeString))
      return DicomDcim._112073;
    if ("112074".equals(codeString))
      return DicomDcim._112074;
    if ("112075".equals(codeString))
      return DicomDcim._112075;
    if ("112076".equals(codeString))
      return DicomDcim._112076;
    if ("112077".equals(codeString))
      return DicomDcim._112077;
    if ("112078".equals(codeString))
      return DicomDcim._112078;
    if ("112079".equals(codeString))
      return DicomDcim._112079;
    if ("112080".equals(codeString))
      return DicomDcim._112080;
    if ("112081".equals(codeString))
      return DicomDcim._112081;
    if ("112082".equals(codeString))
      return DicomDcim._112082;
    if ("112083".equals(codeString))
      return DicomDcim._112083;
    if ("112084".equals(codeString))
      return DicomDcim._112084;
    if ("112085".equals(codeString))
      return DicomDcim._112085;
    if ("112086".equals(codeString))
      return DicomDcim._112086;
    if ("112087".equals(codeString))
      return DicomDcim._112087;
    if ("112088".equals(codeString))
      return DicomDcim._112088;
    if ("112089".equals(codeString))
      return DicomDcim._112089;
    if ("112090".equals(codeString))
      return DicomDcim._112090;
    if ("112091".equals(codeString))
      return DicomDcim._112091;
    if ("112092".equals(codeString))
      return DicomDcim._112092;
    if ("112093".equals(codeString))
      return DicomDcim._112093;
    if ("112094".equals(codeString))
      return DicomDcim._112094;
    if ("112095".equals(codeString))
      return DicomDcim._112095;
    if ("112096".equals(codeString))
      return DicomDcim._112096;
    if ("112097".equals(codeString))
      return DicomDcim._112097;
    if ("112098".equals(codeString))
      return DicomDcim._112098;
    if ("112099".equals(codeString))
      return DicomDcim._112099;
    if ("112100".equals(codeString))
      return DicomDcim._112100;
    if ("112101".equals(codeString))
      return DicomDcim._112101;
    if ("112102".equals(codeString))
      return DicomDcim._112102;
    if ("112103".equals(codeString))
      return DicomDcim._112103;
    if ("112104".equals(codeString))
      return DicomDcim._112104;
    if ("112105".equals(codeString))
      return DicomDcim._112105;
    if ("112106".equals(codeString))
      return DicomDcim._112106;
    if ("112107".equals(codeString))
      return DicomDcim._112107;
    if ("112108".equals(codeString))
      return DicomDcim._112108;
    if ("112109".equals(codeString))
      return DicomDcim._112109;
    if ("112110".equals(codeString))
      return DicomDcim._112110;
    if ("112111".equals(codeString))
      return DicomDcim._112111;
    if ("112112".equals(codeString))
      return DicomDcim._112112;
    if ("112113".equals(codeString))
      return DicomDcim._112113;
    if ("112114".equals(codeString))
      return DicomDcim._112114;
    if ("112115".equals(codeString))
      return DicomDcim._112115;
    if ("112116".equals(codeString))
      return DicomDcim._112116;
    if ("112117".equals(codeString))
      return DicomDcim._112117;
    if ("112118".equals(codeString))
      return DicomDcim._112118;
    if ("112119".equals(codeString))
      return DicomDcim._112119;
    if ("112120".equals(codeString))
      return DicomDcim._112120;
    if ("112121".equals(codeString))
      return DicomDcim._112121;
    if ("112122".equals(codeString))
      return DicomDcim._112122;
    if ("112123".equals(codeString))
      return DicomDcim._112123;
    if ("112124".equals(codeString))
      return DicomDcim._112124;
    if ("112125".equals(codeString))
      return DicomDcim._112125;
    if ("112126".equals(codeString))
      return DicomDcim._112126;
    if ("112127".equals(codeString))
      return DicomDcim._112127;
    if ("112128".equals(codeString))
      return DicomDcim._112128;
    if ("112129".equals(codeString))
      return DicomDcim._112129;
    if ("112130".equals(codeString))
      return DicomDcim._112130;
    if ("112131".equals(codeString))
      return DicomDcim._112131;
    if ("112132".equals(codeString))
      return DicomDcim._112132;
    if ("112133".equals(codeString))
      return DicomDcim._112133;
    if ("112134".equals(codeString))
      return DicomDcim._112134;
    if ("112135".equals(codeString))
      return DicomDcim._112135;
    if ("112136".equals(codeString))
      return DicomDcim._112136;
    if ("112137".equals(codeString))
      return DicomDcim._112137;
    if ("112138".equals(codeString))
      return DicomDcim._112138;
    if ("112139".equals(codeString))
      return DicomDcim._112139;
    if ("112140".equals(codeString))
      return DicomDcim._112140;
    if ("112141".equals(codeString))
      return DicomDcim._112141;
    if ("112142".equals(codeString))
      return DicomDcim._112142;
    if ("112143".equals(codeString))
      return DicomDcim._112143;
    if ("112144".equals(codeString))
      return DicomDcim._112144;
    if ("112145".equals(codeString))
      return DicomDcim._112145;
    if ("112146".equals(codeString))
      return DicomDcim._112146;
    if ("112147".equals(codeString))
      return DicomDcim._112147;
    if ("112148".equals(codeString))
      return DicomDcim._112148;
    if ("112149".equals(codeString))
      return DicomDcim._112149;
    if ("112150".equals(codeString))
      return DicomDcim._112150;
    if ("112151".equals(codeString))
      return DicomDcim._112151;
    if ("112152".equals(codeString))
      return DicomDcim._112152;
    if ("112153".equals(codeString))
      return DicomDcim._112153;
    if ("112154".equals(codeString))
      return DicomDcim._112154;
    if ("112155".equals(codeString))
      return DicomDcim._112155;
    if ("112156".equals(codeString))
      return DicomDcim._112156;
    if ("112157".equals(codeString))
      return DicomDcim._112157;
    if ("112158".equals(codeString))
      return DicomDcim._112158;
    if ("112159".equals(codeString))
      return DicomDcim._112159;
    if ("112160".equals(codeString))
      return DicomDcim._112160;
    if ("112161".equals(codeString))
      return DicomDcim._112161;
    if ("112162".equals(codeString))
      return DicomDcim._112162;
    if ("112163".equals(codeString))
      return DicomDcim._112163;
    if ("112164".equals(codeString))
      return DicomDcim._112164;
    if ("112165".equals(codeString))
      return DicomDcim._112165;
    if ("112166".equals(codeString))
      return DicomDcim._112166;
    if ("112167".equals(codeString))
      return DicomDcim._112167;
    if ("112168".equals(codeString))
      return DicomDcim._112168;
    if ("112169".equals(codeString))
      return DicomDcim._112169;
    if ("112170".equals(codeString))
      return DicomDcim._112170;
    if ("112171".equals(codeString))
      return DicomDcim._112171;
    if ("112172".equals(codeString))
      return DicomDcim._112172;
    if ("112173".equals(codeString))
      return DicomDcim._112173;
    if ("112174".equals(codeString))
      return DicomDcim._112174;
    if ("112175".equals(codeString))
      return DicomDcim._112175;
    if ("112176".equals(codeString))
      return DicomDcim._112176;
    if ("112177".equals(codeString))
      return DicomDcim._112177;
    if ("112178".equals(codeString))
      return DicomDcim._112178;
    if ("112179".equals(codeString))
      return DicomDcim._112179;
    if ("112180".equals(codeString))
      return DicomDcim._112180;
    if ("112181".equals(codeString))
      return DicomDcim._112181;
    if ("112182".equals(codeString))
      return DicomDcim._112182;
    if ("112183".equals(codeString))
      return DicomDcim._112183;
    if ("112184".equals(codeString))
      return DicomDcim._112184;
    if ("112185".equals(codeString))
      return DicomDcim._112185;
    if ("112186".equals(codeString))
      return DicomDcim._112186;
    if ("112187".equals(codeString))
      return DicomDcim._112187;
    if ("112188".equals(codeString))
      return DicomDcim._112188;
    if ("112189".equals(codeString))
      return DicomDcim._112189;
    if ("112191".equals(codeString))
      return DicomDcim._112191;
    if ("112192".equals(codeString))
      return DicomDcim._112192;
    if ("112193".equals(codeString))
      return DicomDcim._112193;
    if ("112194".equals(codeString))
      return DicomDcim._112194;
    if ("112195".equals(codeString))
      return DicomDcim._112195;
    if ("112196".equals(codeString))
      return DicomDcim._112196;
    if ("112197".equals(codeString))
      return DicomDcim._112197;
    if ("112198".equals(codeString))
      return DicomDcim._112198;
    if ("112199".equals(codeString))
      return DicomDcim._112199;
    if ("112200".equals(codeString))
      return DicomDcim._112200;
    if ("112201".equals(codeString))
      return DicomDcim._112201;
    if ("112220".equals(codeString))
      return DicomDcim._112220;
    if ("112222".equals(codeString))
      return DicomDcim._112222;
    if ("112224".equals(codeString))
      return DicomDcim._112224;
    if ("112225".equals(codeString))
      return DicomDcim._112225;
    if ("112226".equals(codeString))
      return DicomDcim._112226;
    if ("112227".equals(codeString))
      return DicomDcim._112227;
    if ("112228".equals(codeString))
      return DicomDcim._112228;
    if ("112229".equals(codeString))
      return DicomDcim._112229;
    if ("112232".equals(codeString))
      return DicomDcim._112232;
    if ("112233".equals(codeString))
      return DicomDcim._112233;
    if ("112238".equals(codeString))
      return DicomDcim._112238;
    if ("112240".equals(codeString))
      return DicomDcim._112240;
    if ("112241".equals(codeString))
      return DicomDcim._112241;
    if ("112242".equals(codeString))
      return DicomDcim._112242;
    if ("112243".equals(codeString))
      return DicomDcim._112243;
    if ("112244".equals(codeString))
      return DicomDcim._112244;
    if ("112248".equals(codeString))
      return DicomDcim._112248;
    if ("112249".equals(codeString))
      return DicomDcim._112249;
    if ("112300".equals(codeString))
      return DicomDcim._112300;
    if ("112301".equals(codeString))
      return DicomDcim._112301;
    if ("112302".equals(codeString))
      return DicomDcim._112302;
    if ("112303".equals(codeString))
      return DicomDcim._112303;
    if ("112304".equals(codeString))
      return DicomDcim._112304;
    if ("112305".equals(codeString))
      return DicomDcim._112305;
    if ("112306".equals(codeString))
      return DicomDcim._112306;
    if ("112307".equals(codeString))
      return DicomDcim._112307;
    if ("112308".equals(codeString))
      return DicomDcim._112308;
    if ("112309".equals(codeString))
      return DicomDcim._112309;
    if ("112310".equals(codeString))
      return DicomDcim._112310;
    if ("112311".equals(codeString))
      return DicomDcim._112311;
    if ("112312".equals(codeString))
      return DicomDcim._112312;
    if ("112313".equals(codeString))
      return DicomDcim._112313;
    if ("112314".equals(codeString))
      return DicomDcim._112314;
    if ("112315".equals(codeString))
      return DicomDcim._112315;
    if ("112316".equals(codeString))
      return DicomDcim._112316;
    if ("112317".equals(codeString))
      return DicomDcim._112317;
    if ("112318".equals(codeString))
      return DicomDcim._112318;
    if ("112319".equals(codeString))
      return DicomDcim._112319;
    if ("112320".equals(codeString))
      return DicomDcim._112320;
    if ("112321".equals(codeString))
      return DicomDcim._112321;
    if ("112325".equals(codeString))
      return DicomDcim._112325;
    if ("112340".equals(codeString))
      return DicomDcim._112340;
    if ("112341".equals(codeString))
      return DicomDcim._112341;
    if ("112342".equals(codeString))
      return DicomDcim._112342;
    if ("112343".equals(codeString))
      return DicomDcim._112343;
    if ("112344".equals(codeString))
      return DicomDcim._112344;
    if ("112345".equals(codeString))
      return DicomDcim._112345;
    if ("112346".equals(codeString))
      return DicomDcim._112346;
    if ("112347".equals(codeString))
      return DicomDcim._112347;
    if ("112348".equals(codeString))
      return DicomDcim._112348;
    if ("112350".equals(codeString))
      return DicomDcim._112350;
    if ("112351".equals(codeString))
      return DicomDcim._112351;
    if ("112352".equals(codeString))
      return DicomDcim._112352;
    if ("112353".equals(codeString))
      return DicomDcim._112353;
    if ("112354".equals(codeString))
      return DicomDcim._112354;
    if ("112355".equals(codeString))
      return DicomDcim._112355;
    if ("112356".equals(codeString))
      return DicomDcim._112356;
    if ("112357".equals(codeString))
      return DicomDcim._112357;
    if ("112358".equals(codeString))
      return DicomDcim._112358;
    if ("112359".equals(codeString))
      return DicomDcim._112359;
    if ("112360".equals(codeString))
      return DicomDcim._112360;
    if ("112361".equals(codeString))
      return DicomDcim._112361;
    if ("112362".equals(codeString))
      return DicomDcim._112362;
    if ("112363".equals(codeString))
      return DicomDcim._112363;
    if ("112364".equals(codeString))
      return DicomDcim._112364;
    if ("112365".equals(codeString))
      return DicomDcim._112365;
    if ("112366".equals(codeString))
      return DicomDcim._112366;
    if ("112367".equals(codeString))
      return DicomDcim._112367;
    if ("112368".equals(codeString))
      return DicomDcim._112368;
    if ("112369".equals(codeString))
      return DicomDcim._112369;
    if ("112370".equals(codeString))
      return DicomDcim._112370;
    if ("112371".equals(codeString))
      return DicomDcim._112371;
    if ("112372".equals(codeString))
      return DicomDcim._112372;
    if ("112373".equals(codeString))
      return DicomDcim._112373;
    if ("112374".equals(codeString))
      return DicomDcim._112374;
    if ("112375".equals(codeString))
      return DicomDcim._112375;
    if ("112376".equals(codeString))
      return DicomDcim._112376;
    if ("112377".equals(codeString))
      return DicomDcim._112377;
    if ("112378".equals(codeString))
      return DicomDcim._112378;
    if ("112379".equals(codeString))
      return DicomDcim._112379;
    if ("112380".equals(codeString))
      return DicomDcim._112380;
    if ("112381".equals(codeString))
      return DicomDcim._112381;
    if ("112700".equals(codeString))
      return DicomDcim._112700;
    if ("112701".equals(codeString))
      return DicomDcim._112701;
    if ("112702".equals(codeString))
      return DicomDcim._112702;
    if ("112703".equals(codeString))
      return DicomDcim._112703;
    if ("112704".equals(codeString))
      return DicomDcim._112704;
    if ("112705".equals(codeString))
      return DicomDcim._112705;
    if ("112706".equals(codeString))
      return DicomDcim._112706;
    if ("112707".equals(codeString))
      return DicomDcim._112707;
    if ("112708".equals(codeString))
      return DicomDcim._112708;
    if ("112709".equals(codeString))
      return DicomDcim._112709;
    if ("112710".equals(codeString))
      return DicomDcim._112710;
    if ("112711".equals(codeString))
      return DicomDcim._112711;
    if ("112712".equals(codeString))
      return DicomDcim._112712;
    if ("112713".equals(codeString))
      return DicomDcim._112713;
    if ("112714".equals(codeString))
      return DicomDcim._112714;
    if ("112715".equals(codeString))
      return DicomDcim._112715;
    if ("112716".equals(codeString))
      return DicomDcim._112716;
    if ("112717".equals(codeString))
      return DicomDcim._112717;
    if ("112718".equals(codeString))
      return DicomDcim._112718;
    if ("112719".equals(codeString))
      return DicomDcim._112719;
    if ("112720".equals(codeString))
      return DicomDcim._112720;
    if ("112721".equals(codeString))
      return DicomDcim._112721;
    if ("113000".equals(codeString))
      return DicomDcim._113000;
    if ("113001".equals(codeString))
      return DicomDcim._113001;
    if ("113002".equals(codeString))
      return DicomDcim._113002;
    if ("113003".equals(codeString))
      return DicomDcim._113003;
    if ("113004".equals(codeString))
      return DicomDcim._113004;
    if ("113005".equals(codeString))
      return DicomDcim._113005;
    if ("113006".equals(codeString))
      return DicomDcim._113006;
    if ("113007".equals(codeString))
      return DicomDcim._113007;
    if ("113008".equals(codeString))
      return DicomDcim._113008;
    if ("113009".equals(codeString))
      return DicomDcim._113009;
    if ("113010".equals(codeString))
      return DicomDcim._113010;
    if ("113011".equals(codeString))
      return DicomDcim._113011;
    if ("113012".equals(codeString))
      return DicomDcim._113012;
    if ("113013".equals(codeString))
      return DicomDcim._113013;
    if ("113014".equals(codeString))
      return DicomDcim._113014;
    if ("113015".equals(codeString))
      return DicomDcim._113015;
    if ("113016".equals(codeString))
      return DicomDcim._113016;
    if ("113017".equals(codeString))
      return DicomDcim._113017;
    if ("113018".equals(codeString))
      return DicomDcim._113018;
    if ("113020".equals(codeString))
      return DicomDcim._113020;
    if ("113021".equals(codeString))
      return DicomDcim._113021;
    if ("113026".equals(codeString))
      return DicomDcim._113026;
    if ("113030".equals(codeString))
      return DicomDcim._113030;
    if ("113031".equals(codeString))
      return DicomDcim._113031;
    if ("113032".equals(codeString))
      return DicomDcim._113032;
    if ("113033".equals(codeString))
      return DicomDcim._113033;
    if ("113034".equals(codeString))
      return DicomDcim._113034;
    if ("113035".equals(codeString))
      return DicomDcim._113035;
    if ("113036".equals(codeString))
      return DicomDcim._113036;
    if ("113037".equals(codeString))
      return DicomDcim._113037;
    if ("113038".equals(codeString))
      return DicomDcim._113038;
    if ("113039".equals(codeString))
      return DicomDcim._113039;
    if ("113040".equals(codeString))
      return DicomDcim._113040;
    if ("113041".equals(codeString))
      return DicomDcim._113041;
    if ("113042".equals(codeString))
      return DicomDcim._113042;
    if ("113043".equals(codeString))
      return DicomDcim._113043;
    if ("113044".equals(codeString))
      return DicomDcim._113044;
    if ("113045".equals(codeString))
      return DicomDcim._113045;
    if ("113046".equals(codeString))
      return DicomDcim._113046;
    if ("113047".equals(codeString))
      return DicomDcim._113047;
    if ("113048".equals(codeString))
      return DicomDcim._113048;
    if ("113049".equals(codeString))
      return DicomDcim._113049;
    if ("113050".equals(codeString))
      return DicomDcim._113050;
    if ("113051".equals(codeString))
      return DicomDcim._113051;
    if ("113052".equals(codeString))
      return DicomDcim._113052;
    if ("113053".equals(codeString))
      return DicomDcim._113053;
    if ("113054".equals(codeString))
      return DicomDcim._113054;
    if ("113055".equals(codeString))
      return DicomDcim._113055;
    if ("113056".equals(codeString))
      return DicomDcim._113056;
    if ("113057".equals(codeString))
      return DicomDcim._113057;
    if ("113058".equals(codeString))
      return DicomDcim._113058;
    if ("113059".equals(codeString))
      return DicomDcim._113059;
    if ("113060".equals(codeString))
      return DicomDcim._113060;
    if ("113061".equals(codeString))
      return DicomDcim._113061;
    if ("113062".equals(codeString))
      return DicomDcim._113062;
    if ("113063".equals(codeString))
      return DicomDcim._113063;
    if ("113064".equals(codeString))
      return DicomDcim._113064;
    if ("113065".equals(codeString))
      return DicomDcim._113065;
    if ("113066".equals(codeString))
      return DicomDcim._113066;
    if ("113067".equals(codeString))
      return DicomDcim._113067;
    if ("113068".equals(codeString))
      return DicomDcim._113068;
    if ("113069".equals(codeString))
      return DicomDcim._113069;
    if ("113070".equals(codeString))
      return DicomDcim._113070;
    if ("113071".equals(codeString))
      return DicomDcim._113071;
    if ("113072".equals(codeString))
      return DicomDcim._113072;
    if ("113073".equals(codeString))
      return DicomDcim._113073;
    if ("113074".equals(codeString))
      return DicomDcim._113074;
    if ("113075".equals(codeString))
      return DicomDcim._113075;
    if ("113076".equals(codeString))
      return DicomDcim._113076;
    if ("113077".equals(codeString))
      return DicomDcim._113077;
    if ("113078".equals(codeString))
      return DicomDcim._113078;
    if ("113079".equals(codeString))
      return DicomDcim._113079;
    if ("113080".equals(codeString))
      return DicomDcim._113080;
    if ("113081".equals(codeString))
      return DicomDcim._113081;
    if ("113082".equals(codeString))
      return DicomDcim._113082;
    if ("113083".equals(codeString))
      return DicomDcim._113083;
    if ("113085".equals(codeString))
      return DicomDcim._113085;
    if ("113086".equals(codeString))
      return DicomDcim._113086;
    if ("113087".equals(codeString))
      return DicomDcim._113087;
    if ("113088".equals(codeString))
      return DicomDcim._113088;
    if ("113089".equals(codeString))
      return DicomDcim._113089;
    if ("113090".equals(codeString))
      return DicomDcim._113090;
    if ("113091".equals(codeString))
      return DicomDcim._113091;
    if ("113092".equals(codeString))
      return DicomDcim._113092;
    if ("113093".equals(codeString))
      return DicomDcim._113093;
    if ("113094".equals(codeString))
      return DicomDcim._113094;
    if ("113095".equals(codeString))
      return DicomDcim._113095;
    if ("113096".equals(codeString))
      return DicomDcim._113096;
    if ("113097".equals(codeString))
      return DicomDcim._113097;
    if ("113100".equals(codeString))
      return DicomDcim._113100;
    if ("113101".equals(codeString))
      return DicomDcim._113101;
    if ("113102".equals(codeString))
      return DicomDcim._113102;
    if ("113103".equals(codeString))
      return DicomDcim._113103;
    if ("113104".equals(codeString))
      return DicomDcim._113104;
    if ("113105".equals(codeString))
      return DicomDcim._113105;
    if ("113106".equals(codeString))
      return DicomDcim._113106;
    if ("113107".equals(codeString))
      return DicomDcim._113107;
    if ("113108".equals(codeString))
      return DicomDcim._113108;
    if ("113109".equals(codeString))
      return DicomDcim._113109;
    if ("113110".equals(codeString))
      return DicomDcim._113110;
    if ("113111".equals(codeString))
      return DicomDcim._113111;
    if ("113500".equals(codeString))
      return DicomDcim._113500;
    if ("113502".equals(codeString))
      return DicomDcim._113502;
    if ("113503".equals(codeString))
      return DicomDcim._113503;
    if ("113505".equals(codeString))
      return DicomDcim._113505;
    if ("113506".equals(codeString))
      return DicomDcim._113506;
    if ("113507".equals(codeString))
      return DicomDcim._113507;
    if ("113508".equals(codeString))
      return DicomDcim._113508;
    if ("113509".equals(codeString))
      return DicomDcim._113509;
    if ("113510".equals(codeString))
      return DicomDcim._113510;
    if ("113511".equals(codeString))
      return DicomDcim._113511;
    if ("113512".equals(codeString))
      return DicomDcim._113512;
    if ("113513".equals(codeString))
      return DicomDcim._113513;
    if ("113514".equals(codeString))
      return DicomDcim._113514;
    if ("113516".equals(codeString))
      return DicomDcim._113516;
    if ("113517".equals(codeString))
      return DicomDcim._113517;
    if ("113518".equals(codeString))
      return DicomDcim._113518;
    if ("113520".equals(codeString))
      return DicomDcim._113520;
    if ("113521".equals(codeString))
      return DicomDcim._113521;
    if ("113522".equals(codeString))
      return DicomDcim._113522;
    if ("113523".equals(codeString))
      return DicomDcim._113523;
    if ("113526".equals(codeString))
      return DicomDcim._113526;
    if ("113527".equals(codeString))
      return DicomDcim._113527;
    if ("113528".equals(codeString))
      return DicomDcim._113528;
    if ("113529".equals(codeString))
      return DicomDcim._113529;
    if ("113530".equals(codeString))
      return DicomDcim._113530;
    if ("113540".equals(codeString))
      return DicomDcim._113540;
    if ("113541".equals(codeString))
      return DicomDcim._113541;
    if ("113542".equals(codeString))
      return DicomDcim._113542;
    if ("113543".equals(codeString))
      return DicomDcim._113543;
    if ("113550".equals(codeString))
      return DicomDcim._113550;
    if ("113551".equals(codeString))
      return DicomDcim._113551;
    if ("113552".equals(codeString))
      return DicomDcim._113552;
    if ("113560".equals(codeString))
      return DicomDcim._113560;
    if ("113561".equals(codeString))
      return DicomDcim._113561;
    if ("113562".equals(codeString))
      return DicomDcim._113562;
    if ("113563".equals(codeString))
      return DicomDcim._113563;
    if ("113568".equals(codeString))
      return DicomDcim._113568;
    if ("113570".equals(codeString))
      return DicomDcim._113570;
    if ("113571".equals(codeString))
      return DicomDcim._113571;
    if ("113572".equals(codeString))
      return DicomDcim._113572;
    if ("113573".equals(codeString))
      return DicomDcim._113573;
    if ("113574".equals(codeString))
      return DicomDcim._113574;
    if ("113575".equals(codeString))
      return DicomDcim._113575;
    if ("113576".equals(codeString))
      return DicomDcim._113576;
    if ("113577".equals(codeString))
      return DicomDcim._113577;
    if ("113601".equals(codeString))
      return DicomDcim._113601;
    if ("113602".equals(codeString))
      return DicomDcim._113602;
    if ("113603".equals(codeString))
      return DicomDcim._113603;
    if ("113605".equals(codeString))
      return DicomDcim._113605;
    if ("113606".equals(codeString))
      return DicomDcim._113606;
    if ("113607".equals(codeString))
      return DicomDcim._113607;
    if ("113608".equals(codeString))
      return DicomDcim._113608;
    if ("113609".equals(codeString))
      return DicomDcim._113609;
    if ("113611".equals(codeString))
      return DicomDcim._113611;
    if ("113612".equals(codeString))
      return DicomDcim._113612;
    if ("113613".equals(codeString))
      return DicomDcim._113613;
    if ("113620".equals(codeString))
      return DicomDcim._113620;
    if ("113621".equals(codeString))
      return DicomDcim._113621;
    if ("113622".equals(codeString))
      return DicomDcim._113622;
    if ("113630".equals(codeString))
      return DicomDcim._113630;
    if ("113631".equals(codeString))
      return DicomDcim._113631;
    if ("113650".equals(codeString))
      return DicomDcim._113650;
    if ("113651".equals(codeString))
      return DicomDcim._113651;
    if ("113652".equals(codeString))
      return DicomDcim._113652;
    if ("113653".equals(codeString))
      return DicomDcim._113653;
    if ("113661".equals(codeString))
      return DicomDcim._113661;
    if ("113662".equals(codeString))
      return DicomDcim._113662;
    if ("113663".equals(codeString))
      return DicomDcim._113663;
    if ("113664".equals(codeString))
      return DicomDcim._113664;
    if ("113665".equals(codeString))
      return DicomDcim._113665;
    if ("113666".equals(codeString))
      return DicomDcim._113666;
    if ("113669".equals(codeString))
      return DicomDcim._113669;
    if ("113670".equals(codeString))
      return DicomDcim._113670;
    if ("113671".equals(codeString))
      return DicomDcim._113671;
    if ("113680".equals(codeString))
      return DicomDcim._113680;
    if ("113681".equals(codeString))
      return DicomDcim._113681;
    if ("113682".equals(codeString))
      return DicomDcim._113682;
    if ("113683".equals(codeString))
      return DicomDcim._113683;
    if ("113684".equals(codeString))
      return DicomDcim._113684;
    if ("113685".equals(codeString))
      return DicomDcim._113685;
    if ("113686".equals(codeString))
      return DicomDcim._113686;
    if ("113687".equals(codeString))
      return DicomDcim._113687;
    if ("113688".equals(codeString))
      return DicomDcim._113688;
    if ("113689".equals(codeString))
      return DicomDcim._113689;
    if ("113690".equals(codeString))
      return DicomDcim._113690;
    if ("113691".equals(codeString))
      return DicomDcim._113691;
    if ("113692".equals(codeString))
      return DicomDcim._113692;
    if ("113701".equals(codeString))
      return DicomDcim._113701;
    if ("113702".equals(codeString))
      return DicomDcim._113702;
    if ("113704".equals(codeString))
      return DicomDcim._113704;
    if ("113705".equals(codeString))
      return DicomDcim._113705;
    if ("113706".equals(codeString))
      return DicomDcim._113706;
    if ("113710".equals(codeString))
      return DicomDcim._113710;
    if ("113711".equals(codeString))
      return DicomDcim._113711;
    if ("113720".equals(codeString))
      return DicomDcim._113720;
    if ("113721".equals(codeString))
      return DicomDcim._113721;
    if ("113722".equals(codeString))
      return DicomDcim._113722;
    if ("113723".equals(codeString))
      return DicomDcim._113723;
    if ("113724".equals(codeString))
      return DicomDcim._113724;
    if ("113725".equals(codeString))
      return DicomDcim._113725;
    if ("113726".equals(codeString))
      return DicomDcim._113726;
    if ("113727".equals(codeString))
      return DicomDcim._113727;
    if ("113728".equals(codeString))
      return DicomDcim._113728;
    if ("113729".equals(codeString))
      return DicomDcim._113729;
    if ("113730".equals(codeString))
      return DicomDcim._113730;
    if ("113731".equals(codeString))
      return DicomDcim._113731;
    if ("113732".equals(codeString))
      return DicomDcim._113732;
    if ("113733".equals(codeString))
      return DicomDcim._113733;
    if ("113734".equals(codeString))
      return DicomDcim._113734;
    if ("113735".equals(codeString))
      return DicomDcim._113735;
    if ("113736".equals(codeString))
      return DicomDcim._113736;
    if ("113737".equals(codeString))
      return DicomDcim._113737;
    if ("113738".equals(codeString))
      return DicomDcim._113738;
    if ("113739".equals(codeString))
      return DicomDcim._113739;
    if ("113740".equals(codeString))
      return DicomDcim._113740;
    if ("113742".equals(codeString))
      return DicomDcim._113742;
    if ("113743".equals(codeString))
      return DicomDcim._113743;
    if ("113744".equals(codeString))
      return DicomDcim._113744;
    if ("113745".equals(codeString))
      return DicomDcim._113745;
    if ("113748".equals(codeString))
      return DicomDcim._113748;
    if ("113750".equals(codeString))
      return DicomDcim._113750;
    if ("113751".equals(codeString))
      return DicomDcim._113751;
    if ("113752".equals(codeString))
      return DicomDcim._113752;
    if ("113753".equals(codeString))
      return DicomDcim._113753;
    if ("113754".equals(codeString))
      return DicomDcim._113754;
    if ("113755".equals(codeString))
      return DicomDcim._113755;
    if ("113756".equals(codeString))
      return DicomDcim._113756;
    if ("113757".equals(codeString))
      return DicomDcim._113757;
    if ("113758".equals(codeString))
      return DicomDcim._113758;
    if ("113759".equals(codeString))
      return DicomDcim._113759;
    if ("113760".equals(codeString))
      return DicomDcim._113760;
    if ("113761".equals(codeString))
      return DicomDcim._113761;
    if ("113763".equals(codeString))
      return DicomDcim._113763;
    if ("113764".equals(codeString))
      return DicomDcim._113764;
    if ("113766".equals(codeString))
      return DicomDcim._113766;
    if ("113767".equals(codeString))
      return DicomDcim._113767;
    if ("113768".equals(codeString))
      return DicomDcim._113768;
    if ("113769".equals(codeString))
      return DicomDcim._113769;
    if ("113770".equals(codeString))
      return DicomDcim._113770;
    if ("113771".equals(codeString))
      return DicomDcim._113771;
    if ("113772".equals(codeString))
      return DicomDcim._113772;
    if ("113773".equals(codeString))
      return DicomDcim._113773;
    if ("113780".equals(codeString))
      return DicomDcim._113780;
    if ("113788".equals(codeString))
      return DicomDcim._113788;
    if ("113789".equals(codeString))
      return DicomDcim._113789;
    if ("113790".equals(codeString))
      return DicomDcim._113790;
    if ("113791".equals(codeString))
      return DicomDcim._113791;
    if ("113792".equals(codeString))
      return DicomDcim._113792;
    if ("113793".equals(codeString))
      return DicomDcim._113793;
    if ("113794".equals(codeString))
      return DicomDcim._113794;
    if ("113795".equals(codeString))
      return DicomDcim._113795;
    if ("113800".equals(codeString))
      return DicomDcim._113800;
    if ("113801".equals(codeString))
      return DicomDcim._113801;
    if ("113802".equals(codeString))
      return DicomDcim._113802;
    if ("113803".equals(codeString))
      return DicomDcim._113803;
    if ("113804".equals(codeString))
      return DicomDcim._113804;
    if ("113805".equals(codeString))
      return DicomDcim._113805;
    if ("113806".equals(codeString))
      return DicomDcim._113806;
    if ("113807".equals(codeString))
      return DicomDcim._113807;
    if ("113808".equals(codeString))
      return DicomDcim._113808;
    if ("113809".equals(codeString))
      return DicomDcim._113809;
    if ("113810".equals(codeString))
      return DicomDcim._113810;
    if ("113811".equals(codeString))
      return DicomDcim._113811;
    if ("113812".equals(codeString))
      return DicomDcim._113812;
    if ("113813".equals(codeString))
      return DicomDcim._113813;
    if ("113814".equals(codeString))
      return DicomDcim._113814;
    if ("113815".equals(codeString))
      return DicomDcim._113815;
    if ("113816".equals(codeString))
      return DicomDcim._113816;
    if ("113817".equals(codeString))
      return DicomDcim._113817;
    if ("113818".equals(codeString))
      return DicomDcim._113818;
    if ("113819".equals(codeString))
      return DicomDcim._113819;
    if ("113820".equals(codeString))
      return DicomDcim._113820;
    if ("113821".equals(codeString))
      return DicomDcim._113821;
    if ("113822".equals(codeString))
      return DicomDcim._113822;
    if ("113823".equals(codeString))
      return DicomDcim._113823;
    if ("113824".equals(codeString))
      return DicomDcim._113824;
    if ("113825".equals(codeString))
      return DicomDcim._113825;
    if ("113826".equals(codeString))
      return DicomDcim._113826;
    if ("113827".equals(codeString))
      return DicomDcim._113827;
    if ("113828".equals(codeString))
      return DicomDcim._113828;
    if ("113829".equals(codeString))
      return DicomDcim._113829;
    if ("113830".equals(codeString))
      return DicomDcim._113830;
    if ("113831".equals(codeString))
      return DicomDcim._113831;
    if ("113832".equals(codeString))
      return DicomDcim._113832;
    if ("113833".equals(codeString))
      return DicomDcim._113833;
    if ("113834".equals(codeString))
      return DicomDcim._113834;
    if ("113835".equals(codeString))
      return DicomDcim._113835;
    if ("113836".equals(codeString))
      return DicomDcim._113836;
    if ("113837".equals(codeString))
      return DicomDcim._113837;
    if ("113838".equals(codeString))
      return DicomDcim._113838;
    if ("113839".equals(codeString))
      return DicomDcim._113839;
    if ("113840".equals(codeString))
      return DicomDcim._113840;
    if ("113841".equals(codeString))
      return DicomDcim._113841;
    if ("113842".equals(codeString))
      return DicomDcim._113842;
    if ("113845".equals(codeString))
      return DicomDcim._113845;
    if ("113846".equals(codeString))
      return DicomDcim._113846;
    if ("113847".equals(codeString))
      return DicomDcim._113847;
    if ("113850".equals(codeString))
      return DicomDcim._113850;
    if ("113851".equals(codeString))
      return DicomDcim._113851;
    if ("113852".equals(codeString))
      return DicomDcim._113852;
    if ("113853".equals(codeString))
      return DicomDcim._113853;
    if ("113854".equals(codeString))
      return DicomDcim._113854;
    if ("113855".equals(codeString))
      return DicomDcim._113855;
    if ("113856".equals(codeString))
      return DicomDcim._113856;
    if ("113857".equals(codeString))
      return DicomDcim._113857;
    if ("113858".equals(codeString))
      return DicomDcim._113858;
    if ("113859".equals(codeString))
      return DicomDcim._113859;
    if ("113860".equals(codeString))
      return DicomDcim._113860;
    if ("113861".equals(codeString))
      return DicomDcim._113861;
    if ("113862".equals(codeString))
      return DicomDcim._113862;
    if ("113863".equals(codeString))
      return DicomDcim._113863;
    if ("113864".equals(codeString))
      return DicomDcim._113864;
    if ("113865".equals(codeString))
      return DicomDcim._113865;
    if ("113866".equals(codeString))
      return DicomDcim._113866;
    if ("113867".equals(codeString))
      return DicomDcim._113867;
    if ("113868".equals(codeString))
      return DicomDcim._113868;
    if ("113870".equals(codeString))
      return DicomDcim._113870;
    if ("113871".equals(codeString))
      return DicomDcim._113871;
    if ("113872".equals(codeString))
      return DicomDcim._113872;
    if ("113873".equals(codeString))
      return DicomDcim._113873;
    if ("113874".equals(codeString))
      return DicomDcim._113874;
    if ("113875".equals(codeString))
      return DicomDcim._113875;
    if ("113876".equals(codeString))
      return DicomDcim._113876;
    if ("113877".equals(codeString))
      return DicomDcim._113877;
    if ("113878".equals(codeString))
      return DicomDcim._113878;
    if ("113879".equals(codeString))
      return DicomDcim._113879;
    if ("113880".equals(codeString))
      return DicomDcim._113880;
    if ("113890".equals(codeString))
      return DicomDcim._113890;
    if ("113893".equals(codeString))
      return DicomDcim._113893;
    if ("113895".equals(codeString))
      return DicomDcim._113895;
    if ("113896".equals(codeString))
      return DicomDcim._113896;
    if ("113897".equals(codeString))
      return DicomDcim._113897;
    if ("113898".equals(codeString))
      return DicomDcim._113898;
    if ("113899".equals(codeString))
      return DicomDcim._113899;
    if ("113900".equals(codeString))
      return DicomDcim._113900;
    if ("113901".equals(codeString))
      return DicomDcim._113901;
    if ("113902".equals(codeString))
      return DicomDcim._113902;
    if ("113903".equals(codeString))
      return DicomDcim._113903;
    if ("113904".equals(codeString))
      return DicomDcim._113904;
    if ("113905".equals(codeString))
      return DicomDcim._113905;
    if ("113906".equals(codeString))
      return DicomDcim._113906;
    if ("113907".equals(codeString))
      return DicomDcim._113907;
    if ("113908".equals(codeString))
      return DicomDcim._113908;
    if ("113909".equals(codeString))
      return DicomDcim._113909;
    if ("113910".equals(codeString))
      return DicomDcim._113910;
    if ("113911".equals(codeString))
      return DicomDcim._113911;
    if ("113912".equals(codeString))
      return DicomDcim._113912;
    if ("113913".equals(codeString))
      return DicomDcim._113913;
    if ("113914".equals(codeString))
      return DicomDcim._113914;
    if ("113921".equals(codeString))
      return DicomDcim._113921;
    if ("113922".equals(codeString))
      return DicomDcim._113922;
    if ("113923".equals(codeString))
      return DicomDcim._113923;
    if ("113930".equals(codeString))
      return DicomDcim._113930;
    if ("113931".equals(codeString))
      return DicomDcim._113931;
    if ("113932".equals(codeString))
      return DicomDcim._113932;
    if ("113933".equals(codeString))
      return DicomDcim._113933;
    if ("113934".equals(codeString))
      return DicomDcim._113934;
    if ("113935".equals(codeString))
      return DicomDcim._113935;
    if ("113936".equals(codeString))
      return DicomDcim._113936;
    if ("113937".equals(codeString))
      return DicomDcim._113937;
    if ("113940".equals(codeString))
      return DicomDcim._113940;
    if ("113941".equals(codeString))
      return DicomDcim._113941;
    if ("113942".equals(codeString))
      return DicomDcim._113942;
    if ("113943".equals(codeString))
      return DicomDcim._113943;
    if ("113944".equals(codeString))
      return DicomDcim._113944;
    if ("113945".equals(codeString))
      return DicomDcim._113945;
    if ("113946".equals(codeString))
      return DicomDcim._113946;
    if ("113947".equals(codeString))
      return DicomDcim._113947;
    if ("113948".equals(codeString))
      return DicomDcim._113948;
    if ("113949".equals(codeString))
      return DicomDcim._113949;
    if ("113950".equals(codeString))
      return DicomDcim._113950;
    if ("113951".equals(codeString))
      return DicomDcim._113951;
    if ("113952".equals(codeString))
      return DicomDcim._113952;
    if ("113953".equals(codeString))
      return DicomDcim._113953;
    if ("113954".equals(codeString))
      return DicomDcim._113954;
    if ("113955".equals(codeString))
      return DicomDcim._113955;
    if ("113956".equals(codeString))
      return DicomDcim._113956;
    if ("113957".equals(codeString))
      return DicomDcim._113957;
    if ("113958".equals(codeString))
      return DicomDcim._113958;
    if ("113959".equals(codeString))
      return DicomDcim._113959;
    if ("113961".equals(codeString))
      return DicomDcim._113961;
    if ("113962".equals(codeString))
      return DicomDcim._113962;
    if ("113963".equals(codeString))
      return DicomDcim._113963;
    if ("113970".equals(codeString))
      return DicomDcim._113970;
    if ("114000".equals(codeString))
      return DicomDcim._114000;
    if ("114001".equals(codeString))
      return DicomDcim._114001;
    if ("114002".equals(codeString))
      return DicomDcim._114002;
    if ("114003".equals(codeString))
      return DicomDcim._114003;
    if ("114004".equals(codeString))
      return DicomDcim._114004;
    if ("114005".equals(codeString))
      return DicomDcim._114005;
    if ("114006".equals(codeString))
      return DicomDcim._114006;
    if ("114007".equals(codeString))
      return DicomDcim._114007;
    if ("114008".equals(codeString))
      return DicomDcim._114008;
    if ("114009".equals(codeString))
      return DicomDcim._114009;
    if ("114010".equals(codeString))
      return DicomDcim._114010;
    if ("114011".equals(codeString))
      return DicomDcim._114011;
    if ("114201".equals(codeString))
      return DicomDcim._114201;
    if ("114202".equals(codeString))
      return DicomDcim._114202;
    if ("114203".equals(codeString))
      return DicomDcim._114203;
    if ("114204".equals(codeString))
      return DicomDcim._114204;
    if ("114205".equals(codeString))
      return DicomDcim._114205;
    if ("114206".equals(codeString))
      return DicomDcim._114206;
    if ("114207".equals(codeString))
      return DicomDcim._114207;
    if ("114208".equals(codeString))
      return DicomDcim._114208;
    if ("114209".equals(codeString))
      return DicomDcim._114209;
    if ("114210".equals(codeString))
      return DicomDcim._114210;
    if ("114211".equals(codeString))
      return DicomDcim._114211;
    if ("114213".equals(codeString))
      return DicomDcim._114213;
    if ("114215".equals(codeString))
      return DicomDcim._114215;
    if ("114216".equals(codeString))
      return DicomDcim._114216;
    if ("121001".equals(codeString))
      return DicomDcim._121001;
    if ("121002".equals(codeString))
      return DicomDcim._121002;
    if ("121003".equals(codeString))
      return DicomDcim._121003;
    if ("121004".equals(codeString))
      return DicomDcim._121004;
    if ("121005".equals(codeString))
      return DicomDcim._121005;
    if ("121006".equals(codeString))
      return DicomDcim._121006;
    if ("121007".equals(codeString))
      return DicomDcim._121007;
    if ("121008".equals(codeString))
      return DicomDcim._121008;
    if ("121009".equals(codeString))
      return DicomDcim._121009;
    if ("121010".equals(codeString))
      return DicomDcim._121010;
    if ("121011".equals(codeString))
      return DicomDcim._121011;
    if ("121012".equals(codeString))
      return DicomDcim._121012;
    if ("121013".equals(codeString))
      return DicomDcim._121013;
    if ("121014".equals(codeString))
      return DicomDcim._121014;
    if ("121015".equals(codeString))
      return DicomDcim._121015;
    if ("121016".equals(codeString))
      return DicomDcim._121016;
    if ("121017".equals(codeString))
      return DicomDcim._121017;
    if ("121018".equals(codeString))
      return DicomDcim._121018;
    if ("121019".equals(codeString))
      return DicomDcim._121019;
    if ("121020".equals(codeString))
      return DicomDcim._121020;
    if ("121021".equals(codeString))
      return DicomDcim._121021;
    if ("121022".equals(codeString))
      return DicomDcim._121022;
    if ("121023".equals(codeString))
      return DicomDcim._121023;
    if ("121024".equals(codeString))
      return DicomDcim._121024;
    if ("121025".equals(codeString))
      return DicomDcim._121025;
    if ("121026".equals(codeString))
      return DicomDcim._121026;
    if ("121027".equals(codeString))
      return DicomDcim._121027;
    if ("121028".equals(codeString))
      return DicomDcim._121028;
    if ("121029".equals(codeString))
      return DicomDcim._121029;
    if ("121030".equals(codeString))
      return DicomDcim._121030;
    if ("121031".equals(codeString))
      return DicomDcim._121031;
    if ("121032".equals(codeString))
      return DicomDcim._121032;
    if ("121033".equals(codeString))
      return DicomDcim._121033;
    if ("121034".equals(codeString))
      return DicomDcim._121034;
    if ("121035".equals(codeString))
      return DicomDcim._121035;
    if ("121036".equals(codeString))
      return DicomDcim._121036;
    if ("121037".equals(codeString))
      return DicomDcim._121037;
    if ("121038".equals(codeString))
      return DicomDcim._121038;
    if ("121039".equals(codeString))
      return DicomDcim._121039;
    if ("121040".equals(codeString))
      return DicomDcim._121040;
    if ("121041".equals(codeString))
      return DicomDcim._121041;
    if ("121042".equals(codeString))
      return DicomDcim._121042;
    if ("121043".equals(codeString))
      return DicomDcim._121043;
    if ("121044".equals(codeString))
      return DicomDcim._121044;
    if ("121045".equals(codeString))
      return DicomDcim._121045;
    if ("121046".equals(codeString))
      return DicomDcim._121046;
    if ("121047".equals(codeString))
      return DicomDcim._121047;
    if ("121048".equals(codeString))
      return DicomDcim._121048;
    if ("121049".equals(codeString))
      return DicomDcim._121049;
    if ("121050".equals(codeString))
      return DicomDcim._121050;
    if ("121051".equals(codeString))
      return DicomDcim._121051;
    if ("121052".equals(codeString))
      return DicomDcim._121052;
    if ("121053".equals(codeString))
      return DicomDcim._121053;
    if ("121054".equals(codeString))
      return DicomDcim._121054;
    if ("121055".equals(codeString))
      return DicomDcim._121055;
    if ("121056".equals(codeString))
      return DicomDcim._121056;
    if ("121057".equals(codeString))
      return DicomDcim._121057;
    if ("121058".equals(codeString))
      return DicomDcim._121058;
    if ("121059".equals(codeString))
      return DicomDcim._121059;
    if ("121060".equals(codeString))
      return DicomDcim._121060;
    if ("121062".equals(codeString))
      return DicomDcim._121062;
    if ("121064".equals(codeString))
      return DicomDcim._121064;
    if ("121065".equals(codeString))
      return DicomDcim._121065;
    if ("121066".equals(codeString))
      return DicomDcim._121066;
    if ("121068".equals(codeString))
      return DicomDcim._121068;
    if ("121069".equals(codeString))
      return DicomDcim._121069;
    if ("121070".equals(codeString))
      return DicomDcim._121070;
    if ("121071".equals(codeString))
      return DicomDcim._121071;
    if ("121072".equals(codeString))
      return DicomDcim._121072;
    if ("121073".equals(codeString))
      return DicomDcim._121073;
    if ("121074".equals(codeString))
      return DicomDcim._121074;
    if ("121075".equals(codeString))
      return DicomDcim._121075;
    if ("121076".equals(codeString))
      return DicomDcim._121076;
    if ("121077".equals(codeString))
      return DicomDcim._121077;
    if ("121078".equals(codeString))
      return DicomDcim._121078;
    if ("121079".equals(codeString))
      return DicomDcim._121079;
    if ("121080".equals(codeString))
      return DicomDcim._121080;
    if ("121081".equals(codeString))
      return DicomDcim._121081;
    if ("121082".equals(codeString))
      return DicomDcim._121082;
    if ("121083".equals(codeString))
      return DicomDcim._121083;
    if ("121084".equals(codeString))
      return DicomDcim._121084;
    if ("121085".equals(codeString))
      return DicomDcim._121085;
    if ("121086".equals(codeString))
      return DicomDcim._121086;
    if ("121087".equals(codeString))
      return DicomDcim._121087;
    if ("121088".equals(codeString))
      return DicomDcim._121088;
    if ("121089".equals(codeString))
      return DicomDcim._121089;
    if ("121090".equals(codeString))
      return DicomDcim._121090;
    if ("121091".equals(codeString))
      return DicomDcim._121091;
    if ("121092".equals(codeString))
      return DicomDcim._121092;
    if ("121093".equals(codeString))
      return DicomDcim._121093;
    if ("121094".equals(codeString))
      return DicomDcim._121094;
    if ("121095".equals(codeString))
      return DicomDcim._121095;
    if ("121096".equals(codeString))
      return DicomDcim._121096;
    if ("121097".equals(codeString))
      return DicomDcim._121097;
    if ("121098".equals(codeString))
      return DicomDcim._121098;
    if ("121099".equals(codeString))
      return DicomDcim._121099;
    if ("121100".equals(codeString))
      return DicomDcim._121100;
    if ("121101".equals(codeString))
      return DicomDcim._121101;
    if ("121102".equals(codeString))
      return DicomDcim._121102;
    if ("121103".equals(codeString))
      return DicomDcim._121103;
    if ("121104".equals(codeString))
      return DicomDcim._121104;
    if ("121105".equals(codeString))
      return DicomDcim._121105;
    if ("121106".equals(codeString))
      return DicomDcim._121106;
    if ("121109".equals(codeString))
      return DicomDcim._121109;
    if ("121110".equals(codeString))
      return DicomDcim._121110;
    if ("121111".equals(codeString))
      return DicomDcim._121111;
    if ("121112".equals(codeString))
      return DicomDcim._121112;
    if ("121113".equals(codeString))
      return DicomDcim._121113;
    if ("121114".equals(codeString))
      return DicomDcim._121114;
    if ("121115".equals(codeString))
      return DicomDcim._121115;
    if ("121116".equals(codeString))
      return DicomDcim._121116;
    if ("121117".equals(codeString))
      return DicomDcim._121117;
    if ("121118".equals(codeString))
      return DicomDcim._121118;
    if ("121120".equals(codeString))
      return DicomDcim._121120;
    if ("121121".equals(codeString))
      return DicomDcim._121121;
    if ("121122".equals(codeString))
      return DicomDcim._121122;
    if ("121123".equals(codeString))
      return DicomDcim._121123;
    if ("121124".equals(codeString))
      return DicomDcim._121124;
    if ("121125".equals(codeString))
      return DicomDcim._121125;
    if ("121126".equals(codeString))
      return DicomDcim._121126;
    if ("121127".equals(codeString))
      return DicomDcim._121127;
    if ("121128".equals(codeString))
      return DicomDcim._121128;
    if ("121130".equals(codeString))
      return DicomDcim._121130;
    if ("121131".equals(codeString))
      return DicomDcim._121131;
    if ("121132".equals(codeString))
      return DicomDcim._121132;
    if ("121133".equals(codeString))
      return DicomDcim._121133;
    if ("121135".equals(codeString))
      return DicomDcim._121135;
    if ("121136".equals(codeString))
      return DicomDcim._121136;
    if ("121137".equals(codeString))
      return DicomDcim._121137;
    if ("121138".equals(codeString))
      return DicomDcim._121138;
    if ("121139".equals(codeString))
      return DicomDcim._121139;
    if ("121140".equals(codeString))
      return DicomDcim._121140;
    if ("121141".equals(codeString))
      return DicomDcim._121141;
    if ("121142".equals(codeString))
      return DicomDcim._121142;
    if ("121143".equals(codeString))
      return DicomDcim._121143;
    if ("121144".equals(codeString))
      return DicomDcim._121144;
    if ("121145".equals(codeString))
      return DicomDcim._121145;
    if ("121146".equals(codeString))
      return DicomDcim._121146;
    if ("121147".equals(codeString))
      return DicomDcim._121147;
    if ("121148".equals(codeString))
      return DicomDcim._121148;
    if ("121149".equals(codeString))
      return DicomDcim._121149;
    if ("121150".equals(codeString))
      return DicomDcim._121150;
    if ("121151".equals(codeString))
      return DicomDcim._121151;
    if ("121152".equals(codeString))
      return DicomDcim._121152;
    if ("121153".equals(codeString))
      return DicomDcim._121153;
    if ("121154".equals(codeString))
      return DicomDcim._121154;
    if ("121155".equals(codeString))
      return DicomDcim._121155;
    if ("121156".equals(codeString))
      return DicomDcim._121156;
    if ("121157".equals(codeString))
      return DicomDcim._121157;
    if ("121158".equals(codeString))
      return DicomDcim._121158;
    if ("121160".equals(codeString))
      return DicomDcim._121160;
    if ("121161".equals(codeString))
      return DicomDcim._121161;
    if ("121162".equals(codeString))
      return DicomDcim._121162;
    if ("121163".equals(codeString))
      return DicomDcim._121163;
    if ("121165".equals(codeString))
      return DicomDcim._121165;
    if ("121166".equals(codeString))
      return DicomDcim._121166;
    if ("121167".equals(codeString))
      return DicomDcim._121167;
    if ("121168".equals(codeString))
      return DicomDcim._121168;
    if ("121169".equals(codeString))
      return DicomDcim._121169;
    if ("121171".equals(codeString))
      return DicomDcim._121171;
    if ("121172".equals(codeString))
      return DicomDcim._121172;
    if ("121173".equals(codeString))
      return DicomDcim._121173;
    if ("121174".equals(codeString))
      return DicomDcim._121174;
    if ("121180".equals(codeString))
      return DicomDcim._121180;
    if ("121181".equals(codeString))
      return DicomDcim._121181;
    if ("121190".equals(codeString))
      return DicomDcim._121190;
    if ("121191".equals(codeString))
      return DicomDcim._121191;
    if ("121192".equals(codeString))
      return DicomDcim._121192;
    if ("121193".equals(codeString))
      return DicomDcim._121193;
    if ("121194".equals(codeString))
      return DicomDcim._121194;
    if ("121195".equals(codeString))
      return DicomDcim._121195;
    if ("121196".equals(codeString))
      return DicomDcim._121196;
    if ("121197".equals(codeString))
      return DicomDcim._121197;
    if ("121198".equals(codeString))
      return DicomDcim._121198;
    if ("121200".equals(codeString))
      return DicomDcim._121200;
    if ("121201".equals(codeString))
      return DicomDcim._121201;
    if ("121202".equals(codeString))
      return DicomDcim._121202;
    if ("121206".equals(codeString))
      return DicomDcim._121206;
    if ("121207".equals(codeString))
      return DicomDcim._121207;
    if ("121208".equals(codeString))
      return DicomDcim._121208;
    if ("121210".equals(codeString))
      return DicomDcim._121210;
    if ("121211".equals(codeString))
      return DicomDcim._121211;
    if ("121213".equals(codeString))
      return DicomDcim._121213;
    if ("121214".equals(codeString))
      return DicomDcim._121214;
    if ("121216".equals(codeString))
      return DicomDcim._121216;
    if ("121217".equals(codeString))
      return DicomDcim._121217;
    if ("121218".equals(codeString))
      return DicomDcim._121218;
    if ("121219".equals(codeString))
      return DicomDcim._121219;
    if ("121220".equals(codeString))
      return DicomDcim._121220;
    if ("121221".equals(codeString))
      return DicomDcim._121221;
    if ("121222".equals(codeString))
      return DicomDcim._121222;
    if ("121230".equals(codeString))
      return DicomDcim._121230;
    if ("121231".equals(codeString))
      return DicomDcim._121231;
    if ("121232".equals(codeString))
      return DicomDcim._121232;
    if ("121233".equals(codeString))
      return DicomDcim._121233;
    if ("121242".equals(codeString))
      return DicomDcim._121242;
    if ("121243".equals(codeString))
      return DicomDcim._121243;
    if ("121244".equals(codeString))
      return DicomDcim._121244;
    if ("121290".equals(codeString))
      return DicomDcim._121290;
    if ("121291".equals(codeString))
      return DicomDcim._121291;
    if ("121301".equals(codeString))
      return DicomDcim._121301;
    if ("121302".equals(codeString))
      return DicomDcim._121302;
    if ("121303".equals(codeString))
      return DicomDcim._121303;
    if ("121304".equals(codeString))
      return DicomDcim._121304;
    if ("121305".equals(codeString))
      return DicomDcim._121305;
    if ("121306".equals(codeString))
      return DicomDcim._121306;
    if ("121307".equals(codeString))
      return DicomDcim._121307;
    if ("121311".equals(codeString))
      return DicomDcim._121311;
    if ("121312".equals(codeString))
      return DicomDcim._121312;
    if ("121313".equals(codeString))
      return DicomDcim._121313;
    if ("121314".equals(codeString))
      return DicomDcim._121314;
    if ("121315".equals(codeString))
      return DicomDcim._121315;
    if ("121316".equals(codeString))
      return DicomDcim._121316;
    if ("121317".equals(codeString))
      return DicomDcim._121317;
    if ("121318".equals(codeString))
      return DicomDcim._121318;
    if ("121320".equals(codeString))
      return DicomDcim._121320;
    if ("121321".equals(codeString))
      return DicomDcim._121321;
    if ("121322".equals(codeString))
      return DicomDcim._121322;
    if ("121323".equals(codeString))
      return DicomDcim._121323;
    if ("121324".equals(codeString))
      return DicomDcim._121324;
    if ("121325".equals(codeString))
      return DicomDcim._121325;
    if ("121326".equals(codeString))
      return DicomDcim._121326;
    if ("121327".equals(codeString))
      return DicomDcim._121327;
    if ("121328".equals(codeString))
      return DicomDcim._121328;
    if ("121329".equals(codeString))
      return DicomDcim._121329;
    if ("121330".equals(codeString))
      return DicomDcim._121330;
    if ("121331".equals(codeString))
      return DicomDcim._121331;
    if ("121332".equals(codeString))
      return DicomDcim._121332;
    if ("121333".equals(codeString))
      return DicomDcim._121333;
    if ("121334".equals(codeString))
      return DicomDcim._121334;
    if ("121335".equals(codeString))
      return DicomDcim._121335;
    if ("121338".equals(codeString))
      return DicomDcim._121338;
    if ("121339".equals(codeString))
      return DicomDcim._121339;
    if ("121340".equals(codeString))
      return DicomDcim._121340;
    if ("121341".equals(codeString))
      return DicomDcim._121341;
    if ("121342".equals(codeString))
      return DicomDcim._121342;
    if ("121346".equals(codeString))
      return DicomDcim._121346;
    if ("121347".equals(codeString))
      return DicomDcim._121347;
    if ("121348".equals(codeString))
      return DicomDcim._121348;
    if ("121349".equals(codeString))
      return DicomDcim._121349;
    if ("121350".equals(codeString))
      return DicomDcim._121350;
    if ("121351".equals(codeString))
      return DicomDcim._121351;
    if ("121352".equals(codeString))
      return DicomDcim._121352;
    if ("121353".equals(codeString))
      return DicomDcim._121353;
    if ("121354".equals(codeString))
      return DicomDcim._121354;
    if ("121358".equals(codeString))
      return DicomDcim._121358;
    if ("121360".equals(codeString))
      return DicomDcim._121360;
    if ("121361".equals(codeString))
      return DicomDcim._121361;
    if ("121362".equals(codeString))
      return DicomDcim._121362;
    if ("121363".equals(codeString))
      return DicomDcim._121363;
    if ("121370".equals(codeString))
      return DicomDcim._121370;
    if ("121371".equals(codeString))
      return DicomDcim._121371;
    if ("121372".equals(codeString))
      return DicomDcim._121372;
    if ("121380".equals(codeString))
      return DicomDcim._121380;
    if ("121381".equals(codeString))
      return DicomDcim._121381;
    if ("121382".equals(codeString))
      return DicomDcim._121382;
    if ("121383".equals(codeString))
      return DicomDcim._121383;
    if ("121401".equals(codeString))
      return DicomDcim._121401;
    if ("121402".equals(codeString))
      return DicomDcim._121402;
    if ("121403".equals(codeString))
      return DicomDcim._121403;
    if ("121404".equals(codeString))
      return DicomDcim._121404;
    if ("121405".equals(codeString))
      return DicomDcim._121405;
    if ("121406".equals(codeString))
      return DicomDcim._121406;
    if ("121407".equals(codeString))
      return DicomDcim._121407;
    if ("121408".equals(codeString))
      return DicomDcim._121408;
    if ("121410".equals(codeString))
      return DicomDcim._121410;
    if ("121411".equals(codeString))
      return DicomDcim._121411;
    if ("121412".equals(codeString))
      return DicomDcim._121412;
    if ("121414".equals(codeString))
      return DicomDcim._121414;
    if ("121415".equals(codeString))
      return DicomDcim._121415;
    if ("121416".equals(codeString))
      return DicomDcim._121416;
    if ("121417".equals(codeString))
      return DicomDcim._121417;
    if ("121420".equals(codeString))
      return DicomDcim._121420;
    if ("121421".equals(codeString))
      return DicomDcim._121421;
    if ("121422".equals(codeString))
      return DicomDcim._121422;
    if ("121423".equals(codeString))
      return DicomDcim._121423;
    if ("121424".equals(codeString))
      return DicomDcim._121424;
    if ("121425".equals(codeString))
      return DicomDcim._121425;
    if ("121427".equals(codeString))
      return DicomDcim._121427;
    if ("121428".equals(codeString))
      return DicomDcim._121428;
    if ("121430".equals(codeString))
      return DicomDcim._121430;
    if ("121431".equals(codeString))
      return DicomDcim._121431;
    if ("121432".equals(codeString))
      return DicomDcim._121432;
    if ("121433".equals(codeString))
      return DicomDcim._121433;
    if ("121434".equals(codeString))
      return DicomDcim._121434;
    if ("121435".equals(codeString))
      return DicomDcim._121435;
    if ("121436".equals(codeString))
      return DicomDcim._121436;
    if ("121437".equals(codeString))
      return DicomDcim._121437;
    if ("121438".equals(codeString))
      return DicomDcim._121438;
    if ("121439".equals(codeString))
      return DicomDcim._121439;
    if ("121701".equals(codeString))
      return DicomDcim._121701;
    if ("121702".equals(codeString))
      return DicomDcim._121702;
    if ("121703".equals(codeString))
      return DicomDcim._121703;
    if ("121704".equals(codeString))
      return DicomDcim._121704;
    if ("121705".equals(codeString))
      return DicomDcim._121705;
    if ("121706".equals(codeString))
      return DicomDcim._121706;
    if ("121707".equals(codeString))
      return DicomDcim._121707;
    if ("121708".equals(codeString))
      return DicomDcim._121708;
    if ("121709".equals(codeString))
      return DicomDcim._121709;
    if ("121710".equals(codeString))
      return DicomDcim._121710;
    if ("121711".equals(codeString))
      return DicomDcim._121711;
    if ("121712".equals(codeString))
      return DicomDcim._121712;
    if ("121713".equals(codeString))
      return DicomDcim._121713;
    if ("121714".equals(codeString))
      return DicomDcim._121714;
    if ("121715".equals(codeString))
      return DicomDcim._121715;
    if ("121716".equals(codeString))
      return DicomDcim._121716;
    if ("121717".equals(codeString))
      return DicomDcim._121717;
    if ("121718".equals(codeString))
      return DicomDcim._121718;
    if ("121719".equals(codeString))
      return DicomDcim._121719;
    if ("121720".equals(codeString))
      return DicomDcim._121720;
    if ("121721".equals(codeString))
      return DicomDcim._121721;
    if ("121722".equals(codeString))
      return DicomDcim._121722;
    if ("121723".equals(codeString))
      return DicomDcim._121723;
    if ("121724".equals(codeString))
      return DicomDcim._121724;
    if ("121725".equals(codeString))
      return DicomDcim._121725;
    if ("121726".equals(codeString))
      return DicomDcim._121726;
    if ("121727".equals(codeString))
      return DicomDcim._121727;
    if ("121728".equals(codeString))
      return DicomDcim._121728;
    if ("121729".equals(codeString))
      return DicomDcim._121729;
    if ("121730".equals(codeString))
      return DicomDcim._121730;
    if ("121731".equals(codeString))
      return DicomDcim._121731;
    if ("121732".equals(codeString))
      return DicomDcim._121732;
    if ("121733".equals(codeString))
      return DicomDcim._121733;
    if ("121734".equals(codeString))
      return DicomDcim._121734;
    if ("121740".equals(codeString))
      return DicomDcim._121740;
    if ("122001".equals(codeString))
      return DicomDcim._122001;
    if ("122002".equals(codeString))
      return DicomDcim._122002;
    if ("122003".equals(codeString))
      return DicomDcim._122003;
    if ("122004".equals(codeString))
      return DicomDcim._122004;
    if ("122005".equals(codeString))
      return DicomDcim._122005;
    if ("122006".equals(codeString))
      return DicomDcim._122006;
    if ("122007".equals(codeString))
      return DicomDcim._122007;
    if ("122008".equals(codeString))
      return DicomDcim._122008;
    if ("122009".equals(codeString))
      return DicomDcim._122009;
    if ("122010".equals(codeString))
      return DicomDcim._122010;
    if ("122011".equals(codeString))
      return DicomDcim._122011;
    if ("122012".equals(codeString))
      return DicomDcim._122012;
    if ("122020".equals(codeString))
      return DicomDcim._122020;
    if ("122021".equals(codeString))
      return DicomDcim._122021;
    if ("122022".equals(codeString))
      return DicomDcim._122022;
    if ("122023".equals(codeString))
      return DicomDcim._122023;
    if ("122024".equals(codeString))
      return DicomDcim._122024;
    if ("122025".equals(codeString))
      return DicomDcim._122025;
    if ("122026".equals(codeString))
      return DicomDcim._122026;
    if ("122027".equals(codeString))
      return DicomDcim._122027;
    if ("122028".equals(codeString))
      return DicomDcim._122028;
    if ("122029".equals(codeString))
      return DicomDcim._122029;
    if ("122030".equals(codeString))
      return DicomDcim._122030;
    if ("122031".equals(codeString))
      return DicomDcim._122031;
    if ("122032".equals(codeString))
      return DicomDcim._122032;
    if ("122033".equals(codeString))
      return DicomDcim._122033;
    if ("122034".equals(codeString))
      return DicomDcim._122034;
    if ("122035".equals(codeString))
      return DicomDcim._122035;
    if ("122036".equals(codeString))
      return DicomDcim._122036;
    if ("122037".equals(codeString))
      return DicomDcim._122037;
    if ("122038".equals(codeString))
      return DicomDcim._122038;
    if ("122039".equals(codeString))
      return DicomDcim._122039;
    if ("122041".equals(codeString))
      return DicomDcim._122041;
    if ("122042".equals(codeString))
      return DicomDcim._122042;
    if ("122043".equals(codeString))
      return DicomDcim._122043;
    if ("122044".equals(codeString))
      return DicomDcim._122044;
    if ("122045".equals(codeString))
      return DicomDcim._122045;
    if ("122046".equals(codeString))
      return DicomDcim._122046;
    if ("122047".equals(codeString))
      return DicomDcim._122047;
    if ("122048".equals(codeString))
      return DicomDcim._122048;
    if ("122049".equals(codeString))
      return DicomDcim._122049;
    if ("122052".equals(codeString))
      return DicomDcim._122052;
    if ("122053".equals(codeString))
      return DicomDcim._122053;
    if ("122054".equals(codeString))
      return DicomDcim._122054;
    if ("122055".equals(codeString))
      return DicomDcim._122055;
    if ("122056".equals(codeString))
      return DicomDcim._122056;
    if ("122057".equals(codeString))
      return DicomDcim._122057;
    if ("122058".equals(codeString))
      return DicomDcim._122058;
    if ("122059".equals(codeString))
      return DicomDcim._122059;
    if ("122060".equals(codeString))
      return DicomDcim._122060;
    if ("122061".equals(codeString))
      return DicomDcim._122061;
    if ("122062".equals(codeString))
      return DicomDcim._122062;
    if ("122072".equals(codeString))
      return DicomDcim._122072;
    if ("122073".equals(codeString))
      return DicomDcim._122073;
    if ("122075".equals(codeString))
      return DicomDcim._122075;
    if ("122076".equals(codeString))
      return DicomDcim._122076;
    if ("122077".equals(codeString))
      return DicomDcim._122077;
    if ("122078".equals(codeString))
      return DicomDcim._122078;
    if ("122079".equals(codeString))
      return DicomDcim._122079;
    if ("122081".equals(codeString))
      return DicomDcim._122081;
    if ("122082".equals(codeString))
      return DicomDcim._122082;
    if ("122083".equals(codeString))
      return DicomDcim._122083;
    if ("122084".equals(codeString))
      return DicomDcim._122084;
    if ("122085".equals(codeString))
      return DicomDcim._122085;
    if ("122086".equals(codeString))
      return DicomDcim._122086;
    if ("122087".equals(codeString))
      return DicomDcim._122087;
    if ("122088".equals(codeString))
      return DicomDcim._122088;
    if ("122089".equals(codeString))
      return DicomDcim._122089;
    if ("122090".equals(codeString))
      return DicomDcim._122090;
    if ("122091".equals(codeString))
      return DicomDcim._122091;
    if ("122092".equals(codeString))
      return DicomDcim._122092;
    if ("122093".equals(codeString))
      return DicomDcim._122093;
    if ("122094".equals(codeString))
      return DicomDcim._122094;
    if ("122095".equals(codeString))
      return DicomDcim._122095;
    if ("122096".equals(codeString))
      return DicomDcim._122096;
    if ("122097".equals(codeString))
      return DicomDcim._122097;
    if ("122098".equals(codeString))
      return DicomDcim._122098;
    if ("122099".equals(codeString))
      return DicomDcim._122099;
    if ("122101".equals(codeString))
      return DicomDcim._122101;
    if ("122102".equals(codeString))
      return DicomDcim._122102;
    if ("122103".equals(codeString))
      return DicomDcim._122103;
    if ("122104".equals(codeString))
      return DicomDcim._122104;
    if ("122105".equals(codeString))
      return DicomDcim._122105;
    if ("122106".equals(codeString))
      return DicomDcim._122106;
    if ("122107".equals(codeString))
      return DicomDcim._122107;
    if ("122108".equals(codeString))
      return DicomDcim._122108;
    if ("122109".equals(codeString))
      return DicomDcim._122109;
    if ("122110".equals(codeString))
      return DicomDcim._122110;
    if ("122111".equals(codeString))
      return DicomDcim._122111;
    if ("122112".equals(codeString))
      return DicomDcim._122112;
    if ("122113".equals(codeString))
      return DicomDcim._122113;
    if ("122114".equals(codeString))
      return DicomDcim._122114;
    if ("122120".equals(codeString))
      return DicomDcim._122120;
    if ("122121".equals(codeString))
      return DicomDcim._122121;
    if ("122122".equals(codeString))
      return DicomDcim._122122;
    if ("122123".equals(codeString))
      return DicomDcim._122123;
    if ("122124".equals(codeString))
      return DicomDcim._122124;
    if ("122125".equals(codeString))
      return DicomDcim._122125;
    if ("122126".equals(codeString))
      return DicomDcim._122126;
    if ("122127".equals(codeString))
      return DicomDcim._122127;
    if ("122128".equals(codeString))
      return DicomDcim._122128;
    if ("122129".equals(codeString))
      return DicomDcim._122129;
    if ("122130".equals(codeString))
      return DicomDcim._122130;
    if ("122131".equals(codeString))
      return DicomDcim._122131;
    if ("122132".equals(codeString))
      return DicomDcim._122132;
    if ("122133".equals(codeString))
      return DicomDcim._122133;
    if ("122134".equals(codeString))
      return DicomDcim._122134;
    if ("122138".equals(codeString))
      return DicomDcim._122138;
    if ("122139".equals(codeString))
      return DicomDcim._122139;
    if ("122140".equals(codeString))
      return DicomDcim._122140;
    if ("122141".equals(codeString))
      return DicomDcim._122141;
    if ("122142".equals(codeString))
      return DicomDcim._122142;
    if ("122143".equals(codeString))
      return DicomDcim._122143;
    if ("122144".equals(codeString))
      return DicomDcim._122144;
    if ("122145".equals(codeString))
      return DicomDcim._122145;
    if ("122146".equals(codeString))
      return DicomDcim._122146;
    if ("122147".equals(codeString))
      return DicomDcim._122147;
    if ("122148".equals(codeString))
      return DicomDcim._122148;
    if ("122149".equals(codeString))
      return DicomDcim._122149;
    if ("122150".equals(codeString))
      return DicomDcim._122150;
    if ("122151".equals(codeString))
      return DicomDcim._122151;
    if ("122152".equals(codeString))
      return DicomDcim._122152;
    if ("122153".equals(codeString))
      return DicomDcim._122153;
    if ("122154".equals(codeString))
      return DicomDcim._122154;
    if ("122157".equals(codeString))
      return DicomDcim._122157;
    if ("122158".equals(codeString))
      return DicomDcim._122158;
    if ("122159".equals(codeString))
      return DicomDcim._122159;
    if ("122160".equals(codeString))
      return DicomDcim._122160;
    if ("122161".equals(codeString))
      return DicomDcim._122161;
    if ("122162".equals(codeString))
      return DicomDcim._122162;
    if ("122163".equals(codeString))
      return DicomDcim._122163;
    if ("122164".equals(codeString))
      return DicomDcim._122164;
    if ("122165".equals(codeString))
      return DicomDcim._122165;
    if ("122166".equals(codeString))
      return DicomDcim._122166;
    if ("122167".equals(codeString))
      return DicomDcim._122167;
    if ("122170".equals(codeString))
      return DicomDcim._122170;
    if ("122171".equals(codeString))
      return DicomDcim._122171;
    if ("122172".equals(codeString))
      return DicomDcim._122172;
    if ("122173".equals(codeString))
      return DicomDcim._122173;
    if ("122175".equals(codeString))
      return DicomDcim._122175;
    if ("122176".equals(codeString))
      return DicomDcim._122176;
    if ("122177".equals(codeString))
      return DicomDcim._122177;
    if ("122178".equals(codeString))
      return DicomDcim._122178;
    if ("122179".equals(codeString))
      return DicomDcim._122179;
    if ("122180".equals(codeString))
      return DicomDcim._122180;
    if ("122181".equals(codeString))
      return DicomDcim._122181;
    if ("122182".equals(codeString))
      return DicomDcim._122182;
    if ("122183".equals(codeString))
      return DicomDcim._122183;
    if ("122185".equals(codeString))
      return DicomDcim._122185;
    if ("122187".equals(codeString))
      return DicomDcim._122187;
    if ("122188".equals(codeString))
      return DicomDcim._122188;
    if ("122189".equals(codeString))
      return DicomDcim._122189;
    if ("122190".equals(codeString))
      return DicomDcim._122190;
    if ("122191".equals(codeString))
      return DicomDcim._122191;
    if ("122192".equals(codeString))
      return DicomDcim._122192;
    if ("122193".equals(codeString))
      return DicomDcim._122193;
    if ("122194".equals(codeString))
      return DicomDcim._122194;
    if ("122195".equals(codeString))
      return DicomDcim._122195;
    if ("122196".equals(codeString))
      return DicomDcim._122196;
    if ("122197".equals(codeString))
      return DicomDcim._122197;
    if ("122198".equals(codeString))
      return DicomDcim._122198;
    if ("122199".equals(codeString))
      return DicomDcim._122199;
    if ("122201".equals(codeString))
      return DicomDcim._122201;
    if ("122202".equals(codeString))
      return DicomDcim._122202;
    if ("122203".equals(codeString))
      return DicomDcim._122203;
    if ("122204".equals(codeString))
      return DicomDcim._122204;
    if ("122205".equals(codeString))
      return DicomDcim._122205;
    if ("122206".equals(codeString))
      return DicomDcim._122206;
    if ("122207".equals(codeString))
      return DicomDcim._122207;
    if ("122208".equals(codeString))
      return DicomDcim._122208;
    if ("122209".equals(codeString))
      return DicomDcim._122209;
    if ("122210".equals(codeString))
      return DicomDcim._122210;
    if ("122211".equals(codeString))
      return DicomDcim._122211;
    if ("122212".equals(codeString))
      return DicomDcim._122212;
    if ("122213".equals(codeString))
      return DicomDcim._122213;
    if ("122214".equals(codeString))
      return DicomDcim._122214;
    if ("122215".equals(codeString))
      return DicomDcim._122215;
    if ("122216".equals(codeString))
      return DicomDcim._122216;
    if ("122217".equals(codeString))
      return DicomDcim._122217;
    if ("122218".equals(codeString))
      return DicomDcim._122218;
    if ("122219".equals(codeString))
      return DicomDcim._122219;
    if ("122220".equals(codeString))
      return DicomDcim._122220;
    if ("122221".equals(codeString))
      return DicomDcim._122221;
    if ("122222".equals(codeString))
      return DicomDcim._122222;
    if ("122223".equals(codeString))
      return DicomDcim._122223;
    if ("122224".equals(codeString))
      return DicomDcim._122224;
    if ("122225".equals(codeString))
      return DicomDcim._122225;
    if ("122227".equals(codeString))
      return DicomDcim._122227;
    if ("122228".equals(codeString))
      return DicomDcim._122228;
    if ("122229".equals(codeString))
      return DicomDcim._122229;
    if ("122230".equals(codeString))
      return DicomDcim._122230;
    if ("122231".equals(codeString))
      return DicomDcim._122231;
    if ("122232".equals(codeString))
      return DicomDcim._122232;
    if ("122233".equals(codeString))
      return DicomDcim._122233;
    if ("122234".equals(codeString))
      return DicomDcim._122234;
    if ("122235".equals(codeString))
      return DicomDcim._122235;
    if ("122236".equals(codeString))
      return DicomDcim._122236;
    if ("122237".equals(codeString))
      return DicomDcim._122237;
    if ("122238".equals(codeString))
      return DicomDcim._122238;
    if ("122239".equals(codeString))
      return DicomDcim._122239;
    if ("122240".equals(codeString))
      return DicomDcim._122240;
    if ("122241".equals(codeString))
      return DicomDcim._122241;
    if ("122242".equals(codeString))
      return DicomDcim._122242;
    if ("122243".equals(codeString))
      return DicomDcim._122243;
    if ("122244".equals(codeString))
      return DicomDcim._122244;
    if ("122245".equals(codeString))
      return DicomDcim._122245;
    if ("122246".equals(codeString))
      return DicomDcim._122246;
    if ("122247".equals(codeString))
      return DicomDcim._122247;
    if ("122248".equals(codeString))
      return DicomDcim._122248;
    if ("122249".equals(codeString))
      return DicomDcim._122249;
    if ("122250".equals(codeString))
      return DicomDcim._122250;
    if ("122251".equals(codeString))
      return DicomDcim._122251;
    if ("122252".equals(codeString))
      return DicomDcim._122252;
    if ("122253".equals(codeString))
      return DicomDcim._122253;
    if ("122254".equals(codeString))
      return DicomDcim._122254;
    if ("122255".equals(codeString))
      return DicomDcim._122255;
    if ("122256".equals(codeString))
      return DicomDcim._122256;
    if ("122257".equals(codeString))
      return DicomDcim._122257;
    if ("122258".equals(codeString))
      return DicomDcim._122258;
    if ("122259".equals(codeString))
      return DicomDcim._122259;
    if ("122260".equals(codeString))
      return DicomDcim._122260;
    if ("122261".equals(codeString))
      return DicomDcim._122261;
    if ("122262".equals(codeString))
      return DicomDcim._122262;
    if ("122263".equals(codeString))
      return DicomDcim._122263;
    if ("122265".equals(codeString))
      return DicomDcim._122265;
    if ("122266".equals(codeString))
      return DicomDcim._122266;
    if ("122267".equals(codeString))
      return DicomDcim._122267;
    if ("122268".equals(codeString))
      return DicomDcim._122268;
    if ("122269".equals(codeString))
      return DicomDcim._122269;
    if ("122270".equals(codeString))
      return DicomDcim._122270;
    if ("122271".equals(codeString))
      return DicomDcim._122271;
    if ("122272".equals(codeString))
      return DicomDcim._122272;
    if ("122273".equals(codeString))
      return DicomDcim._122273;
    if ("122274".equals(codeString))
      return DicomDcim._122274;
    if ("122275".equals(codeString))
      return DicomDcim._122275;
    if ("122276".equals(codeString))
      return DicomDcim._122276;
    if ("122277".equals(codeString))
      return DicomDcim._122277;
    if ("122278".equals(codeString))
      return DicomDcim._122278;
    if ("122279".equals(codeString))
      return DicomDcim._122279;
    if ("122281".equals(codeString))
      return DicomDcim._122281;
    if ("122282".equals(codeString))
      return DicomDcim._122282;
    if ("122283".equals(codeString))
      return DicomDcim._122283;
    if ("122288".equals(codeString))
      return DicomDcim._122288;
    if ("122291".equals(codeString))
      return DicomDcim._122291;
    if ("122292".equals(codeString))
      return DicomDcim._122292;
    if ("122301".equals(codeString))
      return DicomDcim._122301;
    if ("122302".equals(codeString))
      return DicomDcim._122302;
    if ("122303".equals(codeString))
      return DicomDcim._122303;
    if ("122304".equals(codeString))
      return DicomDcim._122304;
    if ("122305".equals(codeString))
      return DicomDcim._122305;
    if ("122306".equals(codeString))
      return DicomDcim._122306;
    if ("122307".equals(codeString))
      return DicomDcim._122307;
    if ("122308".equals(codeString))
      return DicomDcim._122308;
    if ("122309".equals(codeString))
      return DicomDcim._122309;
    if ("122310".equals(codeString))
      return DicomDcim._122310;
    if ("122311".equals(codeString))
      return DicomDcim._122311;
    if ("122312".equals(codeString))
      return DicomDcim._122312;
    if ("122313".equals(codeString))
      return DicomDcim._122313;
    if ("122319".equals(codeString))
      return DicomDcim._122319;
    if ("122320".equals(codeString))
      return DicomDcim._122320;
    if ("122321".equals(codeString))
      return DicomDcim._122321;
    if ("122322".equals(codeString))
      return DicomDcim._122322;
    if ("122325".equals(codeString))
      return DicomDcim._122325;
    if ("122330".equals(codeString))
      return DicomDcim._122330;
    if ("122331".equals(codeString))
      return DicomDcim._122331;
    if ("122332".equals(codeString))
      return DicomDcim._122332;
    if ("122333".equals(codeString))
      return DicomDcim._122333;
    if ("122334".equals(codeString))
      return DicomDcim._122334;
    if ("122335".equals(codeString))
      return DicomDcim._122335;
    if ("122336".equals(codeString))
      return DicomDcim._122336;
    if ("122337".equals(codeString))
      return DicomDcim._122337;
    if ("122339".equals(codeString))
      return DicomDcim._122339;
    if ("122340".equals(codeString))
      return DicomDcim._122340;
    if ("122341".equals(codeString))
      return DicomDcim._122341;
    if ("122343".equals(codeString))
      return DicomDcim._122343;
    if ("122344".equals(codeString))
      return DicomDcim._122344;
    if ("122345".equals(codeString))
      return DicomDcim._122345;
    if ("122346".equals(codeString))
      return DicomDcim._122346;
    if ("122347".equals(codeString))
      return DicomDcim._122347;
    if ("122348".equals(codeString))
      return DicomDcim._122348;
    if ("122350".equals(codeString))
      return DicomDcim._122350;
    if ("122351".equals(codeString))
      return DicomDcim._122351;
    if ("122352".equals(codeString))
      return DicomDcim._122352;
    if ("122354".equals(codeString))
      return DicomDcim._122354;
    if ("122355".equals(codeString))
      return DicomDcim._122355;
    if ("122356".equals(codeString))
      return DicomDcim._122356;
    if ("122357".equals(codeString))
      return DicomDcim._122357;
    if ("122360".equals(codeString))
      return DicomDcim._122360;
    if ("122361".equals(codeString))
      return DicomDcim._122361;
    if ("122363".equals(codeString))
      return DicomDcim._122363;
    if ("122364".equals(codeString))
      return DicomDcim._122364;
    if ("122367".equals(codeString))
      return DicomDcim._122367;
    if ("122368".equals(codeString))
      return DicomDcim._122368;
    if ("122369".equals(codeString))
      return DicomDcim._122369;
    if ("122370".equals(codeString))
      return DicomDcim._122370;
    if ("122371".equals(codeString))
      return DicomDcim._122371;
    if ("122372".equals(codeString))
      return DicomDcim._122372;
    if ("122374".equals(codeString))
      return DicomDcim._122374;
    if ("122375".equals(codeString))
      return DicomDcim._122375;
    if ("122376".equals(codeString))
      return DicomDcim._122376;
    if ("122380".equals(codeString))
      return DicomDcim._122380;
    if ("122381".equals(codeString))
      return DicomDcim._122381;
    if ("122382".equals(codeString))
      return DicomDcim._122382;
    if ("122383".equals(codeString))
      return DicomDcim._122383;
    if ("122384".equals(codeString))
      return DicomDcim._122384;
    if ("122385".equals(codeString))
      return DicomDcim._122385;
    if ("122386".equals(codeString))
      return DicomDcim._122386;
    if ("122387".equals(codeString))
      return DicomDcim._122387;
    if ("122388".equals(codeString))
      return DicomDcim._122388;
    if ("122389".equals(codeString))
      return DicomDcim._122389;
    if ("122390".equals(codeString))
      return DicomDcim._122390;
    if ("122391".equals(codeString))
      return DicomDcim._122391;
    if ("122393".equals(codeString))
      return DicomDcim._122393;
    if ("122394".equals(codeString))
      return DicomDcim._122394;
    if ("122395".equals(codeString))
      return DicomDcim._122395;
    if ("122398".equals(codeString))
      return DicomDcim._122398;
    if ("122399".equals(codeString))
      return DicomDcim._122399;
    if ("122400".equals(codeString))
      return DicomDcim._122400;
    if ("122401".equals(codeString))
      return DicomDcim._122401;
    if ("122402".equals(codeString))
      return DicomDcim._122402;
    if ("122403".equals(codeString))
      return DicomDcim._122403;
    if ("122404".equals(codeString))
      return DicomDcim._122404;
    if ("122405".equals(codeString))
      return DicomDcim._122405;
    if ("122406".equals(codeString))
      return DicomDcim._122406;
    if ("122407".equals(codeString))
      return DicomDcim._122407;
    if ("122408".equals(codeString))
      return DicomDcim._122408;
    if ("122410".equals(codeString))
      return DicomDcim._122410;
    if ("122411".equals(codeString))
      return DicomDcim._122411;
    if ("122417".equals(codeString))
      return DicomDcim._122417;
    if ("122421".equals(codeString))
      return DicomDcim._122421;
    if ("122422".equals(codeString))
      return DicomDcim._122422;
    if ("122423".equals(codeString))
      return DicomDcim._122423;
    if ("122428".equals(codeString))
      return DicomDcim._122428;
    if ("122429".equals(codeString))
      return DicomDcim._122429;
    if ("122430".equals(codeString))
      return DicomDcim._122430;
    if ("122431".equals(codeString))
      return DicomDcim._122431;
    if ("122432".equals(codeString))
      return DicomDcim._122432;
    if ("122433".equals(codeString))
      return DicomDcim._122433;
    if ("122434".equals(codeString))
      return DicomDcim._122434;
    if ("122435".equals(codeString))
      return DicomDcim._122435;
    if ("122438".equals(codeString))
      return DicomDcim._122438;
    if ("122445".equals(codeString))
      return DicomDcim._122445;
    if ("122446".equals(codeString))
      return DicomDcim._122446;
    if ("122447".equals(codeString))
      return DicomDcim._122447;
    if ("122448".equals(codeString))
      return DicomDcim._122448;
    if ("122449".equals(codeString))
      return DicomDcim._122449;
    if ("122450".equals(codeString))
      return DicomDcim._122450;
    if ("122451".equals(codeString))
      return DicomDcim._122451;
    if ("122452".equals(codeString))
      return DicomDcim._122452;
    if ("122453".equals(codeString))
      return DicomDcim._122453;
    if ("122459".equals(codeString))
      return DicomDcim._122459;
    if ("122461".equals(codeString))
      return DicomDcim._122461;
    if ("122464".equals(codeString))
      return DicomDcim._122464;
    if ("122465".equals(codeString))
      return DicomDcim._122465;
    if ("122466".equals(codeString))
      return DicomDcim._122466;
    if ("122467".equals(codeString))
      return DicomDcim._122467;
    if ("122468".equals(codeString))
      return DicomDcim._122468;
    if ("122469".equals(codeString))
      return DicomDcim._122469;
    if ("122470".equals(codeString))
      return DicomDcim._122470;
    if ("122471".equals(codeString))
      return DicomDcim._122471;
    if ("122472".equals(codeString))
      return DicomDcim._122472;
    if ("122473".equals(codeString))
      return DicomDcim._122473;
    if ("122474".equals(codeString))
      return DicomDcim._122474;
    if ("122475".equals(codeString))
      return DicomDcim._122475;
    if ("122476".equals(codeString))
      return DicomDcim._122476;
    if ("122477".equals(codeString))
      return DicomDcim._122477;
    if ("122480".equals(codeString))
      return DicomDcim._122480;
    if ("122481".equals(codeString))
      return DicomDcim._122481;
    if ("122482".equals(codeString))
      return DicomDcim._122482;
    if ("122485".equals(codeString))
      return DicomDcim._122485;
    if ("122486".equals(codeString))
      return DicomDcim._122486;
    if ("122487".equals(codeString))
      return DicomDcim._122487;
    if ("122488".equals(codeString))
      return DicomDcim._122488;
    if ("122489".equals(codeString))
      return DicomDcim._122489;
    if ("122490".equals(codeString))
      return DicomDcim._122490;
    if ("122491".equals(codeString))
      return DicomDcim._122491;
    if ("122493".equals(codeString))
      return DicomDcim._122493;
    if ("122495".equals(codeString))
      return DicomDcim._122495;
    if ("122496".equals(codeString))
      return DicomDcim._122496;
    if ("122497".equals(codeString))
      return DicomDcim._122497;
    if ("122498".equals(codeString))
      return DicomDcim._122498;
    if ("122499".equals(codeString))
      return DicomDcim._122499;
    if ("122501".equals(codeString))
      return DicomDcim._122501;
    if ("122502".equals(codeString))
      return DicomDcim._122502;
    if ("122503".equals(codeString))
      return DicomDcim._122503;
    if ("122505".equals(codeString))
      return DicomDcim._122505;
    if ("122507".equals(codeString))
      return DicomDcim._122507;
    if ("122508".equals(codeString))
      return DicomDcim._122508;
    if ("122509".equals(codeString))
      return DicomDcim._122509;
    if ("122510".equals(codeString))
      return DicomDcim._122510;
    if ("122511".equals(codeString))
      return DicomDcim._122511;
    if ("122516".equals(codeString))
      return DicomDcim._122516;
    if ("122517".equals(codeString))
      return DicomDcim._122517;
    if ("122528".equals(codeString))
      return DicomDcim._122528;
    if ("122529".equals(codeString))
      return DicomDcim._122529;
    if ("122542".equals(codeString))
      return DicomDcim._122542;
    if ("122544".equals(codeString))
      return DicomDcim._122544;
    if ("122545".equals(codeString))
      return DicomDcim._122545;
    if ("122546".equals(codeString))
      return DicomDcim._122546;
    if ("122547".equals(codeString))
      return DicomDcim._122547;
    if ("122548".equals(codeString))
      return DicomDcim._122548;
    if ("122549".equals(codeString))
      return DicomDcim._122549;
    if ("122550".equals(codeString))
      return DicomDcim._122550;
    if ("122551".equals(codeString))
      return DicomDcim._122551;
    if ("122554".equals(codeString))
      return DicomDcim._122554;
    if ("122555".equals(codeString))
      return DicomDcim._122555;
    if ("122558".equals(codeString))
      return DicomDcim._122558;
    if ("122559".equals(codeString))
      return DicomDcim._122559;
    if ("122560".equals(codeString))
      return DicomDcim._122560;
    if ("122562".equals(codeString))
      return DicomDcim._122562;
    if ("122563".equals(codeString))
      return DicomDcim._122563;
    if ("122564".equals(codeString))
      return DicomDcim._122564;
    if ("122565".equals(codeString))
      return DicomDcim._122565;
    if ("122566".equals(codeString))
      return DicomDcim._122566;
    if ("122572".equals(codeString))
      return DicomDcim._122572;
    if ("122574".equals(codeString))
      return DicomDcim._122574;
    if ("122575".equals(codeString))
      return DicomDcim._122575;
    if ("122582".equals(codeString))
      return DicomDcim._122582;
    if ("122600".equals(codeString))
      return DicomDcim._122600;
    if ("122601".equals(codeString))
      return DicomDcim._122601;
    if ("122602".equals(codeString))
      return DicomDcim._122602;
    if ("122603".equals(codeString))
      return DicomDcim._122603;
    if ("122604".equals(codeString))
      return DicomDcim._122604;
    if ("122605".equals(codeString))
      return DicomDcim._122605;
    if ("122606".equals(codeString))
      return DicomDcim._122606;
    if ("122607".equals(codeString))
      return DicomDcim._122607;
    if ("122608".equals(codeString))
      return DicomDcim._122608;
    if ("122609".equals(codeString))
      return DicomDcim._122609;
    if ("122611".equals(codeString))
      return DicomDcim._122611;
    if ("122612".equals(codeString))
      return DicomDcim._122612;
    if ("122616".equals(codeString))
      return DicomDcim._122616;
    if ("122617".equals(codeString))
      return DicomDcim._122617;
    if ("122618".equals(codeString))
      return DicomDcim._122618;
    if ("122619".equals(codeString))
      return DicomDcim._122619;
    if ("122620".equals(codeString))
      return DicomDcim._122620;
    if ("122621".equals(codeString))
      return DicomDcim._122621;
    if ("122624".equals(codeString))
      return DicomDcim._122624;
    if ("122627".equals(codeString))
      return DicomDcim._122627;
    if ("122628".equals(codeString))
      return DicomDcim._122628;
    if ("122631".equals(codeString))
      return DicomDcim._122631;
    if ("122633".equals(codeString))
      return DicomDcim._122633;
    if ("122634".equals(codeString))
      return DicomDcim._122634;
    if ("122635".equals(codeString))
      return DicomDcim._122635;
    if ("122636".equals(codeString))
      return DicomDcim._122636;
    if ("122637".equals(codeString))
      return DicomDcim._122637;
    if ("122638".equals(codeString))
      return DicomDcim._122638;
    if ("122639".equals(codeString))
      return DicomDcim._122639;
    if ("122640".equals(codeString))
      return DicomDcim._122640;
    if ("122642".equals(codeString))
      return DicomDcim._122642;
    if ("122643".equals(codeString))
      return DicomDcim._122643;
    if ("122645".equals(codeString))
      return DicomDcim._122645;
    if ("122650".equals(codeString))
      return DicomDcim._122650;
    if ("122651".equals(codeString))
      return DicomDcim._122651;
    if ("122652".equals(codeString))
      return DicomDcim._122652;
    if ("122655".equals(codeString))
      return DicomDcim._122655;
    if ("122656".equals(codeString))
      return DicomDcim._122656;
    if ("122657".equals(codeString))
      return DicomDcim._122657;
    if ("122658".equals(codeString))
      return DicomDcim._122658;
    if ("122659".equals(codeString))
      return DicomDcim._122659;
    if ("122660".equals(codeString))
      return DicomDcim._122660;
    if ("122661".equals(codeString))
      return DicomDcim._122661;
    if ("122664".equals(codeString))
      return DicomDcim._122664;
    if ("122665".equals(codeString))
      return DicomDcim._122665;
    if ("122666".equals(codeString))
      return DicomDcim._122666;
    if ("122667".equals(codeString))
      return DicomDcim._122667;
    if ("122668".equals(codeString))
      return DicomDcim._122668;
    if ("122670".equals(codeString))
      return DicomDcim._122670;
    if ("122675".equals(codeString))
      return DicomDcim._122675;
    if ("122680".equals(codeString))
      return DicomDcim._122680;
    if ("122683".equals(codeString))
      return DicomDcim._122683;
    if ("122684".equals(codeString))
      return DicomDcim._122684;
    if ("122685".equals(codeString))
      return DicomDcim._122685;
    if ("122686".equals(codeString))
      return DicomDcim._122686;
    if ("122687".equals(codeString))
      return DicomDcim._122687;
    if ("122698".equals(codeString))
      return DicomDcim._122698;
    if ("122699".equals(codeString))
      return DicomDcim._122699;
    if ("122700".equals(codeString))
      return DicomDcim._122700;
    if ("122701".equals(codeString))
      return DicomDcim._122701;
    if ("122702".equals(codeString))
      return DicomDcim._122702;
    if ("122703".equals(codeString))
      return DicomDcim._122703;
    if ("122704".equals(codeString))
      return DicomDcim._122704;
    if ("122705".equals(codeString))
      return DicomDcim._122705;
    if ("122706".equals(codeString))
      return DicomDcim._122706;
    if ("122707".equals(codeString))
      return DicomDcim._122707;
    if ("122708".equals(codeString))
      return DicomDcim._122708;
    if ("122709".equals(codeString))
      return DicomDcim._122709;
    if ("122710".equals(codeString))
      return DicomDcim._122710;
    if ("122711".equals(codeString))
      return DicomDcim._122711;
    if ("122712".equals(codeString))
      return DicomDcim._122712;
    if ("122713".equals(codeString))
      return DicomDcim._122713;
    if ("122715".equals(codeString))
      return DicomDcim._122715;
    if ("122716".equals(codeString))
      return DicomDcim._122716;
    if ("122717".equals(codeString))
      return DicomDcim._122717;
    if ("122718".equals(codeString))
      return DicomDcim._122718;
    if ("122720".equals(codeString))
      return DicomDcim._122720;
    if ("122721".equals(codeString))
      return DicomDcim._122721;
    if ("122726".equals(codeString))
      return DicomDcim._122726;
    if ("122727".equals(codeString))
      return DicomDcim._122727;
    if ("122728".equals(codeString))
      return DicomDcim._122728;
    if ("122729".equals(codeString))
      return DicomDcim._122729;
    if ("122730".equals(codeString))
      return DicomDcim._122730;
    if ("122731".equals(codeString))
      return DicomDcim._122731;
    if ("122732".equals(codeString))
      return DicomDcim._122732;
    if ("122733".equals(codeString))
      return DicomDcim._122733;
    if ("122734".equals(codeString))
      return DicomDcim._122734;
    if ("122735".equals(codeString))
      return DicomDcim._122735;
    if ("122739".equals(codeString))
      return DicomDcim._122739;
    if ("122740".equals(codeString))
      return DicomDcim._122740;
    if ("122741".equals(codeString))
      return DicomDcim._122741;
    if ("122742".equals(codeString))
      return DicomDcim._122742;
    if ("122743".equals(codeString))
      return DicomDcim._122743;
    if ("122744".equals(codeString))
      return DicomDcim._122744;
    if ("122745".equals(codeString))
      return DicomDcim._122745;
    if ("122748".equals(codeString))
      return DicomDcim._122748;
    if ("122750".equals(codeString))
      return DicomDcim._122750;
    if ("122751".equals(codeString))
      return DicomDcim._122751;
    if ("122752".equals(codeString))
      return DicomDcim._122752;
    if ("122753".equals(codeString))
      return DicomDcim._122753;
    if ("122755".equals(codeString))
      return DicomDcim._122755;
    if ("122756".equals(codeString))
      return DicomDcim._122756;
    if ("122757".equals(codeString))
      return DicomDcim._122757;
    if ("122758".equals(codeString))
      return DicomDcim._122758;
    if ("122759".equals(codeString))
      return DicomDcim._122759;
    if ("122760".equals(codeString))
      return DicomDcim._122760;
    if ("122762".equals(codeString))
      return DicomDcim._122762;
    if ("122764".equals(codeString))
      return DicomDcim._122764;
    if ("122768".equals(codeString))
      return DicomDcim._122768;
    if ("122769".equals(codeString))
      return DicomDcim._122769;
    if ("122770".equals(codeString))
      return DicomDcim._122770;
    if ("122771".equals(codeString))
      return DicomDcim._122771;
    if ("122772".equals(codeString))
      return DicomDcim._122772;
    if ("122773".equals(codeString))
      return DicomDcim._122773;
    if ("122775".equals(codeString))
      return DicomDcim._122775;
    if ("122776".equals(codeString))
      return DicomDcim._122776;
    if ("122781".equals(codeString))
      return DicomDcim._122781;
    if ("122782".equals(codeString))
      return DicomDcim._122782;
    if ("122783".equals(codeString))
      return DicomDcim._122783;
    if ("122784".equals(codeString))
      return DicomDcim._122784;
    if ("122785".equals(codeString))
      return DicomDcim._122785;
    if ("122791".equals(codeString))
      return DicomDcim._122791;
    if ("122792".equals(codeString))
      return DicomDcim._122792;
    if ("122793".equals(codeString))
      return DicomDcim._122793;
    if ("122795".equals(codeString))
      return DicomDcim._122795;
    if ("122796".equals(codeString))
      return DicomDcim._122796;
    if ("122797".equals(codeString))
      return DicomDcim._122797;
    if ("122799".equals(codeString))
      return DicomDcim._122799;
    if ("123001".equals(codeString))
      return DicomDcim._123001;
    if ("123003".equals(codeString))
      return DicomDcim._123003;
    if ("123004".equals(codeString))
      return DicomDcim._123004;
    if ("123005".equals(codeString))
      return DicomDcim._123005;
    if ("123006".equals(codeString))
      return DicomDcim._123006;
    if ("123007".equals(codeString))
      return DicomDcim._123007;
    if ("123009".equals(codeString))
      return DicomDcim._123009;
    if ("123010".equals(codeString))
      return DicomDcim._123010;
    if ("123011".equals(codeString))
      return DicomDcim._123011;
    if ("123012".equals(codeString))
      return DicomDcim._123012;
    if ("123014".equals(codeString))
      return DicomDcim._123014;
    if ("123015".equals(codeString))
      return DicomDcim._123015;
    if ("123016".equals(codeString))
      return DicomDcim._123016;
    if ("123019".equals(codeString))
      return DicomDcim._123019;
    if ("123101".equals(codeString))
      return DicomDcim._123101;
    if ("123102".equals(codeString))
      return DicomDcim._123102;
    if ("123103".equals(codeString))
      return DicomDcim._123103;
    if ("123104".equals(codeString))
      return DicomDcim._123104;
    if ("123105".equals(codeString))
      return DicomDcim._123105;
    if ("123106".equals(codeString))
      return DicomDcim._123106;
    if ("123107".equals(codeString))
      return DicomDcim._123107;
    if ("123108".equals(codeString))
      return DicomDcim._123108;
    if ("123109".equals(codeString))
      return DicomDcim._123109;
    if ("123110".equals(codeString))
      return DicomDcim._123110;
    if ("123111".equals(codeString))
      return DicomDcim._123111;
    if ("125000".equals(codeString))
      return DicomDcim._125000;
    if ("125001".equals(codeString))
      return DicomDcim._125001;
    if ("125002".equals(codeString))
      return DicomDcim._125002;
    if ("125003".equals(codeString))
      return DicomDcim._125003;
    if ("125004".equals(codeString))
      return DicomDcim._125004;
    if ("125005".equals(codeString))
      return DicomDcim._125005;
    if ("125006".equals(codeString))
      return DicomDcim._125006;
    if ("125007".equals(codeString))
      return DicomDcim._125007;
    if ("125008".equals(codeString))
      return DicomDcim._125008;
    if ("125009".equals(codeString))
      return DicomDcim._125009;
    if ("125010".equals(codeString))
      return DicomDcim._125010;
    if ("125011".equals(codeString))
      return DicomDcim._125011;
    if ("125012".equals(codeString))
      return DicomDcim._125012;
    if ("125013".equals(codeString))
      return DicomDcim._125013;
    if ("125015".equals(codeString))
      return DicomDcim._125015;
    if ("125016".equals(codeString))
      return DicomDcim._125016;
    if ("125021".equals(codeString))
      return DicomDcim._125021;
    if ("125022".equals(codeString))
      return DicomDcim._125022;
    if ("125023".equals(codeString))
      return DicomDcim._125023;
    if ("125024".equals(codeString))
      return DicomDcim._125024;
    if ("125025".equals(codeString))
      return DicomDcim._125025;
    if ("125030".equals(codeString))
      return DicomDcim._125030;
    if ("125031".equals(codeString))
      return DicomDcim._125031;
    if ("125032".equals(codeString))
      return DicomDcim._125032;
    if ("125033".equals(codeString))
      return DicomDcim._125033;
    if ("125034".equals(codeString))
      return DicomDcim._125034;
    if ("125035".equals(codeString))
      return DicomDcim._125035;
    if ("125036".equals(codeString))
      return DicomDcim._125036;
    if ("125037".equals(codeString))
      return DicomDcim._125037;
    if ("125038".equals(codeString))
      return DicomDcim._125038;
    if ("125040".equals(codeString))
      return DicomDcim._125040;
    if ("125041".equals(codeString))
      return DicomDcim._125041;
    if ("125100".equals(codeString))
      return DicomDcim._125100;
    if ("125101".equals(codeString))
      return DicomDcim._125101;
    if ("125102".equals(codeString))
      return DicomDcim._125102;
    if ("125105".equals(codeString))
      return DicomDcim._125105;
    if ("125106".equals(codeString))
      return DicomDcim._125106;
    if ("125107".equals(codeString))
      return DicomDcim._125107;
    if ("125195".equals(codeString))
      return DicomDcim._125195;
    if ("125196".equals(codeString))
      return DicomDcim._125196;
    if ("125197".equals(codeString))
      return DicomDcim._125197;
    if ("125200".equals(codeString))
      return DicomDcim._125200;
    if ("125201".equals(codeString))
      return DicomDcim._125201;
    if ("125202".equals(codeString))
      return DicomDcim._125202;
    if ("125203".equals(codeString))
      return DicomDcim._125203;
    if ("125204".equals(codeString))
      return DicomDcim._125204;
    if ("125205".equals(codeString))
      return DicomDcim._125205;
    if ("125206".equals(codeString))
      return DicomDcim._125206;
    if ("125207".equals(codeString))
      return DicomDcim._125207;
    if ("125208".equals(codeString))
      return DicomDcim._125208;
    if ("125209".equals(codeString))
      return DicomDcim._125209;
    if ("125210".equals(codeString))
      return DicomDcim._125210;
    if ("125211".equals(codeString))
      return DicomDcim._125211;
    if ("125212".equals(codeString))
      return DicomDcim._125212;
    if ("125213".equals(codeString))
      return DicomDcim._125213;
    if ("125214".equals(codeString))
      return DicomDcim._125214;
    if ("125215".equals(codeString))
      return DicomDcim._125215;
    if ("125216".equals(codeString))
      return DicomDcim._125216;
    if ("125217".equals(codeString))
      return DicomDcim._125217;
    if ("125218".equals(codeString))
      return DicomDcim._125218;
    if ("125219".equals(codeString))
      return DicomDcim._125219;
    if ("125220".equals(codeString))
      return DicomDcim._125220;
    if ("125221".equals(codeString))
      return DicomDcim._125221;
    if ("125222".equals(codeString))
      return DicomDcim._125222;
    if ("125223".equals(codeString))
      return DicomDcim._125223;
    if ("125224".equals(codeString))
      return DicomDcim._125224;
    if ("125225".equals(codeString))
      return DicomDcim._125225;
    if ("125226".equals(codeString))
      return DicomDcim._125226;
    if ("125227".equals(codeString))
      return DicomDcim._125227;
    if ("125228".equals(codeString))
      return DicomDcim._125228;
    if ("125230".equals(codeString))
      return DicomDcim._125230;
    if ("125231".equals(codeString))
      return DicomDcim._125231;
    if ("125233".equals(codeString))
      return DicomDcim._125233;
    if ("125234".equals(codeString))
      return DicomDcim._125234;
    if ("125235".equals(codeString))
      return DicomDcim._125235;
    if ("125236".equals(codeString))
      return DicomDcim._125236;
    if ("125237".equals(codeString))
      return DicomDcim._125237;
    if ("125238".equals(codeString))
      return DicomDcim._125238;
    if ("125239".equals(codeString))
      return DicomDcim._125239;
    if ("125240".equals(codeString))
      return DicomDcim._125240;
    if ("125241".equals(codeString))
      return DicomDcim._125241;
    if ("125242".equals(codeString))
      return DicomDcim._125242;
    if ("125251".equals(codeString))
      return DicomDcim._125251;
    if ("125252".equals(codeString))
      return DicomDcim._125252;
    if ("125253".equals(codeString))
      return DicomDcim._125253;
    if ("125254".equals(codeString))
      return DicomDcim._125254;
    if ("125255".equals(codeString))
      return DicomDcim._125255;
    if ("125256".equals(codeString))
      return DicomDcim._125256;
    if ("125257".equals(codeString))
      return DicomDcim._125257;
    if ("125258".equals(codeString))
      return DicomDcim._125258;
    if ("125259".equals(codeString))
      return DicomDcim._125259;
    if ("125261".equals(codeString))
      return DicomDcim._125261;
    if ("125262".equals(codeString))
      return DicomDcim._125262;
    if ("125263".equals(codeString))
      return DicomDcim._125263;
    if ("125264".equals(codeString))
      return DicomDcim._125264;
    if ("125265".equals(codeString))
      return DicomDcim._125265;
    if ("125270".equals(codeString))
      return DicomDcim._125270;
    if ("125271".equals(codeString))
      return DicomDcim._125271;
    if ("125272".equals(codeString))
      return DicomDcim._125272;
    if ("125273".equals(codeString))
      return DicomDcim._125273;
    if ("125901".equals(codeString))
      return DicomDcim._125901;
    if ("125902".equals(codeString))
      return DicomDcim._125902;
    if ("125903".equals(codeString))
      return DicomDcim._125903;
    if ("125904".equals(codeString))
      return DicomDcim._125904;
    if ("125905".equals(codeString))
      return DicomDcim._125905;
    if ("125906".equals(codeString))
      return DicomDcim._125906;
    if ("125907".equals(codeString))
      return DicomDcim._125907;
    if ("125908".equals(codeString))
      return DicomDcim._125908;
    if ("126000".equals(codeString))
      return DicomDcim._126000;
    if ("126001".equals(codeString))
      return DicomDcim._126001;
    if ("126002".equals(codeString))
      return DicomDcim._126002;
    if ("126003".equals(codeString))
      return DicomDcim._126003;
    if ("126010".equals(codeString))
      return DicomDcim._126010;
    if ("126011".equals(codeString))
      return DicomDcim._126011;
    if ("126020".equals(codeString))
      return DicomDcim._126020;
    if ("126021".equals(codeString))
      return DicomDcim._126021;
    if ("126022".equals(codeString))
      return DicomDcim._126022;
    if ("126030".equals(codeString))
      return DicomDcim._126030;
    if ("126031".equals(codeString))
      return DicomDcim._126031;
    if ("126032".equals(codeString))
      return DicomDcim._126032;
    if ("126033".equals(codeString))
      return DicomDcim._126033;
    if ("126034".equals(codeString))
      return DicomDcim._126034;
    if ("126035".equals(codeString))
      return DicomDcim._126035;
    if ("126036".equals(codeString))
      return DicomDcim._126036;
    if ("126037".equals(codeString))
      return DicomDcim._126037;
    if ("126038".equals(codeString))
      return DicomDcim._126038;
    if ("126039".equals(codeString))
      return DicomDcim._126039;
    if ("126040".equals(codeString))
      return DicomDcim._126040;
    if ("126050".equals(codeString))
      return DicomDcim._126050;
    if ("126051".equals(codeString))
      return DicomDcim._126051;
    if ("126052".equals(codeString))
      return DicomDcim._126052;
    if ("126060".equals(codeString))
      return DicomDcim._126060;
    if ("126061".equals(codeString))
      return DicomDcim._126061;
    if ("126062".equals(codeString))
      return DicomDcim._126062;
    if ("126063".equals(codeString))
      return DicomDcim._126063;
    if ("126064".equals(codeString))
      return DicomDcim._126064;
    if ("126065".equals(codeString))
      return DicomDcim._126065;
    if ("126066".equals(codeString))
      return DicomDcim._126066;
    if ("126067".equals(codeString))
      return DicomDcim._126067;
    if ("126070".equals(codeString))
      return DicomDcim._126070;
    if ("126071".equals(codeString))
      return DicomDcim._126071;
    if ("126072".equals(codeString))
      return DicomDcim._126072;
    if ("126073".equals(codeString))
      return DicomDcim._126073;
    if ("126074".equals(codeString))
      return DicomDcim._126074;
    if ("126075".equals(codeString))
      return DicomDcim._126075;
    if ("126080".equals(codeString))
      return DicomDcim._126080;
    if ("126081".equals(codeString))
      return DicomDcim._126081;
    if ("126100".equals(codeString))
      return DicomDcim._126100;
    if ("126200".equals(codeString))
      return DicomDcim._126200;
    if ("126201".equals(codeString))
      return DicomDcim._126201;
    if ("126202".equals(codeString))
      return DicomDcim._126202;
    if ("126203".equals(codeString))
      return DicomDcim._126203;
    if ("126220".equals(codeString))
      return DicomDcim._126220;
    if ("126300".equals(codeString))
      return DicomDcim._126300;
    if ("126301".equals(codeString))
      return DicomDcim._126301;
    if ("126302".equals(codeString))
      return DicomDcim._126302;
    if ("126303".equals(codeString))
      return DicomDcim._126303;
    if ("126310".equals(codeString))
      return DicomDcim._126310;
    if ("126311".equals(codeString))
      return DicomDcim._126311;
    if ("126312".equals(codeString))
      return DicomDcim._126312;
    if ("126313".equals(codeString))
      return DicomDcim._126313;
    if ("126314".equals(codeString))
      return DicomDcim._126314;
    if ("126320".equals(codeString))
      return DicomDcim._126320;
    if ("126321".equals(codeString))
      return DicomDcim._126321;
    if ("126322".equals(codeString))
      return DicomDcim._126322;
    if ("126330".equals(codeString))
      return DicomDcim._126330;
    if ("126331".equals(codeString))
      return DicomDcim._126331;
    if ("126340".equals(codeString))
      return DicomDcim._126340;
    if ("126341".equals(codeString))
      return DicomDcim._126341;
    if ("126342".equals(codeString))
      return DicomDcim._126342;
    if ("126343".equals(codeString))
      return DicomDcim._126343;
    if ("126344".equals(codeString))
      return DicomDcim._126344;
    if ("126350".equals(codeString))
      return DicomDcim._126350;
    if ("126351".equals(codeString))
      return DicomDcim._126351;
    if ("126352".equals(codeString))
      return DicomDcim._126352;
    if ("126353".equals(codeString))
      return DicomDcim._126353;
    if ("126360".equals(codeString))
      return DicomDcim._126360;
    if ("126361".equals(codeString))
      return DicomDcim._126361;
    if ("126362".equals(codeString))
      return DicomDcim._126362;
    if ("126363".equals(codeString))
      return DicomDcim._126363;
    if ("126364".equals(codeString))
      return DicomDcim._126364;
    if ("126370".equals(codeString))
      return DicomDcim._126370;
    if ("126371".equals(codeString))
      return DicomDcim._126371;
    if ("126372".equals(codeString))
      return DicomDcim._126372;
    if ("126373".equals(codeString))
      return DicomDcim._126373;
    if ("126374".equals(codeString))
      return DicomDcim._126374;
    if ("126375".equals(codeString))
      return DicomDcim._126375;
    if ("126376".equals(codeString))
      return DicomDcim._126376;
    if ("126377".equals(codeString))
      return DicomDcim._126377;
    if ("126380".equals(codeString))
      return DicomDcim._126380;
    if ("126390".equals(codeString))
      return DicomDcim._126390;
    if ("126391".equals(codeString))
      return DicomDcim._126391;
    if ("126392".equals(codeString))
      return DicomDcim._126392;
    if ("126393".equals(codeString))
      return DicomDcim._126393;
    if ("126394".equals(codeString))
      return DicomDcim._126394;
    if ("126400".equals(codeString))
      return DicomDcim._126400;
    if ("126401".equals(codeString))
      return DicomDcim._126401;
    if ("126402".equals(codeString))
      return DicomDcim._126402;
    if ("126403".equals(codeString))
      return DicomDcim._126403;
    if ("126404".equals(codeString))
      return DicomDcim._126404;
    if ("126410".equals(codeString))
      return DicomDcim._126410;
    if ("126411".equals(codeString))
      return DicomDcim._126411;
    if ("126412".equals(codeString))
      return DicomDcim._126412;
    if ("126413".equals(codeString))
      return DicomDcim._126413;
    if ("126500".equals(codeString))
      return DicomDcim._126500;
    if ("126501".equals(codeString))
      return DicomDcim._126501;
    if ("126502".equals(codeString))
      return DicomDcim._126502;
    if ("126503".equals(codeString))
      return DicomDcim._126503;
    if ("126510".equals(codeString))
      return DicomDcim._126510;
    if ("126511".equals(codeString))
      return DicomDcim._126511;
    if ("126512".equals(codeString))
      return DicomDcim._126512;
    if ("126513".equals(codeString))
      return DicomDcim._126513;
    if ("126514".equals(codeString))
      return DicomDcim._126514;
    if ("126515".equals(codeString))
      return DicomDcim._126515;
    if ("126516".equals(codeString))
      return DicomDcim._126516;
    if ("126517".equals(codeString))
      return DicomDcim._126517;
    if ("126518".equals(codeString))
      return DicomDcim._126518;
    if ("126519".equals(codeString))
      return DicomDcim._126519;
    if ("126520".equals(codeString))
      return DicomDcim._126520;
    if ("126600".equals(codeString))
      return DicomDcim._126600;
    if ("126601".equals(codeString))
      return DicomDcim._126601;
    if ("126602".equals(codeString))
      return DicomDcim._126602;
    if ("126603".equals(codeString))
      return DicomDcim._126603;
    if ("126604".equals(codeString))
      return DicomDcim._126604;
    if ("126605".equals(codeString))
      return DicomDcim._126605;
    if ("126606".equals(codeString))
      return DicomDcim._126606;
    if ("126700".equals(codeString))
      return DicomDcim._126700;
    if ("126701".equals(codeString))
      return DicomDcim._126701;
    if ("126702".equals(codeString))
      return DicomDcim._126702;
    if ("126703".equals(codeString))
      return DicomDcim._126703;
    if ("126704".equals(codeString))
      return DicomDcim._126704;
    if ("126705".equals(codeString))
      return DicomDcim._126705;
    if ("126706".equals(codeString))
      return DicomDcim._126706;
    if ("126707".equals(codeString))
      return DicomDcim._126707;
    if ("126708".equals(codeString))
      return DicomDcim._126708;
    if ("126709".equals(codeString))
      return DicomDcim._126709;
    if ("126710".equals(codeString))
      return DicomDcim._126710;
    if ("126711".equals(codeString))
      return DicomDcim._126711;
    if ("126712".equals(codeString))
      return DicomDcim._126712;
    if ("126713".equals(codeString))
      return DicomDcim._126713;
    if ("126714".equals(codeString))
      return DicomDcim._126714;
    if ("126715".equals(codeString))
      return DicomDcim._126715;
    if ("126716".equals(codeString))
      return DicomDcim._126716;
    if ("126801".equals(codeString))
      return DicomDcim._126801;
    if ("126802".equals(codeString))
      return DicomDcim._126802;
    if ("126803".equals(codeString))
      return DicomDcim._126803;
    if ("126804".equals(codeString))
      return DicomDcim._126804;
    if ("126805".equals(codeString))
      return DicomDcim._126805;
    if ("126806".equals(codeString))
      return DicomDcim._126806;
    if ("126807".equals(codeString))
      return DicomDcim._126807;
    if ("126808".equals(codeString))
      return DicomDcim._126808;
    if ("126809".equals(codeString))
      return DicomDcim._126809;
    if ("126810".equals(codeString))
      return DicomDcim._126810;
    if ("126811".equals(codeString))
      return DicomDcim._126811;
    throw new IllegalArgumentException("Unknown DicomDcim code '"+codeString+"'");
  }

  public String toCode(DicomDcim code) {
    if (code == DicomDcim.ARCHIVE)
      return "ARCHIVE";
    if (code == DicomDcim.AR)
      return "AR";
    if (code == DicomDcim.AS)
      return "AS";
    if (code == DicomDcim.AU)
      return "AU";
    if (code == DicomDcim.BDUS)
      return "BDUS";
    if (code == DicomDcim.BI)
      return "BI";
    if (code == DicomDcim.BMD)
      return "BMD";
    if (code == DicomDcim.CAD)
      return "CAD";
    if (code == DicomDcim.CAPTURE)
      return "CAPTURE";
    if (code == DicomDcim.CD)
      return "CD";
    if (code == DicomDcim.CF)
      return "CF";
    if (code == DicomDcim.COMP)
      return "COMP";
    if (code == DicomDcim.CP)
      return "CP";
    if (code == DicomDcim.CR)
      return "CR";
    if (code == DicomDcim.CS)
      return "CS";
    if (code == DicomDcim.CT)
      return "CT";
    if (code == DicomDcim.DD)
      return "DD";
    if (code == DicomDcim.DF)
      return "DF";
    if (code == DicomDcim.DG)
      return "DG";
    if (code == DicomDcim.DM)
      return "DM";
    if (code == DicomDcim.DOCD)
      return "DOCD";
    if (code == DicomDcim.DS)
      return "DS";
    if (code == DicomDcim.DSS)
      return "DSS";
    if (code == DicomDcim.DX)
      return "DX";
    if (code == DicomDcim.EC)
      return "EC";
    if (code == DicomDcim.ECG)
      return "ECG";
    if (code == DicomDcim.EPS)
      return "EPS";
    if (code == DicomDcim.ES)
      return "ES";
    if (code == DicomDcim.F)
      return "F";
    if (code == DicomDcim.FA)
      return "FA";
    if (code == DicomDcim.FC)
      return "FC";
    if (code == DicomDcim.FILMD)
      return "FILMD";
    if (code == DicomDcim.FP)
      return "FP";
    if (code == DicomDcim.FS)
      return "FS";
    if (code == DicomDcim.GM)
      return "GM";
    if (code == DicomDcim.H)
      return "H";
    if (code == DicomDcim.HC)
      return "HC";
    if (code == DicomDcim.HD)
      return "HD";
    if (code == DicomDcim.IO)
      return "IO";
    if (code == DicomDcim.IVOCT)
      return "IVOCT";
    if (code == DicomDcim.IVUS)
      return "IVUS";
    if (code == DicomDcim.KER)
      return "KER";
    if (code == DicomDcim.KO)
      return "KO";
    if (code == DicomDcim.LEN)
      return "LEN";
    if (code == DicomDcim.LOG)
      return "LOG";
    if (code == DicomDcim.LP)
      return "LP";
    if (code == DicomDcim.LS)
      return "LS";
    if (code == DicomDcim.M)
      return "M";
    if (code == DicomDcim.MA)
      return "MA";
    if (code == DicomDcim.MC)
      return "MC";
    if (code == DicomDcim.MCD)
      return "MCD";
    if (code == DicomDcim.MEDIM)
      return "MEDIM";
    if (code == DicomDcim.MG)
      return "MG";
    if (code == DicomDcim.MP)
      return "MP";
    if (code == DicomDcim.MR)
      return "MR";
    if (code == DicomDcim.MS)
      return "MS";
    if (code == DicomDcim.NEARLINE)
      return "NEARLINE";
    if (code == DicomDcim.NM)
      return "NM";
    if (code == DicomDcim.OAM)
      return "OAM";
    if (code == DicomDcim.OCT)
      return "OCT";
    if (code == DicomDcim.OFFLINE)
      return "OFFLINE";
    if (code == DicomDcim.ONLINE)
      return "ONLINE";
    if (code == DicomDcim.OP)
      return "OP";
    if (code == DicomDcim.OPM)
      return "OPM";
    if (code == DicomDcim.OPR)
      return "OPR";
    if (code == DicomDcim.OPT)
      return "OPT";
    if (code == DicomDcim.OPV)
      return "OPV";
    if (code == DicomDcim.OSS)
      return "OSS";
    if (code == DicomDcim.OT)
      return "OT";
    if (code == DicomDcim.PR)
      return "PR";
    if (code == DicomDcim.PRINT)
      return "PRINT";
    if (code == DicomDcim.PT)
      return "PT";
    if (code == DicomDcim.PX)
      return "PX";
    if (code == DicomDcim.REG)
      return "REG";
    if (code == DicomDcim.RF)
      return "RF";
    if (code == DicomDcim.RG)
      return "RG";
    if (code == DicomDcim.RT)
      return "RT";
    if (code == DicomDcim.RTDOSE)
      return "RTDOSE";
    if (code == DicomDcim.RTIMAGE)
      return "RTIMAGE";
    if (code == DicomDcim.RTPLAN)
      return "RTPLAN";
    if (code == DicomDcim.RTRECORD)
      return "RTRECORD";
    if (code == DicomDcim.RTSTRUCT)
      return "RTSTRUCT";
    if (code == DicomDcim.SEG)
      return "SEG";
    if (code == DicomDcim.SM)
      return "SM";
    if (code == DicomDcim.SMR)
      return "SMR";
    if (code == DicomDcim.SR)
      return "SR";
    if (code == DicomDcim.SRF)
      return "SRF";
    if (code == DicomDcim.ST)
      return "ST";
    if (code == DicomDcim.TG)
      return "TG";
    if (code == DicomDcim.U)
      return "U";
    if (code == DicomDcim.UNAVAILABLE)
      return "UNAVAILABLE";
    if (code == DicomDcim.US)
      return "US";
    if (code == DicomDcim.VA)
      return "VA";
    if (code == DicomDcim.VF)
      return "VF";
    if (code == DicomDcim.VIDD)
      return "VIDD";
    if (code == DicomDcim.WSD)
      return "WSD";
    if (code == DicomDcim.XA)
      return "XA";
    if (code == DicomDcim.XC)
      return "XC";
    if (code == DicomDcim._109001)
      return "109001";
    if (code == DicomDcim._109002)
      return "109002";
    if (code == DicomDcim._109003)
      return "109003";
    if (code == DicomDcim._109004)
      return "109004";
    if (code == DicomDcim._109005)
      return "109005";
    if (code == DicomDcim._109006)
      return "109006";
    if (code == DicomDcim._109007)
      return "109007";
    if (code == DicomDcim._109008)
      return "109008";
    if (code == DicomDcim._109009)
      return "109009";
    if (code == DicomDcim._109010)
      return "109010";
    if (code == DicomDcim._109011)
      return "109011";
    if (code == DicomDcim._109012)
      return "109012";
    if (code == DicomDcim._109013)
      return "109013";
    if (code == DicomDcim._109014)
      return "109014";
    if (code == DicomDcim._109015)
      return "109015";
    if (code == DicomDcim._109016)
      return "109016";
    if (code == DicomDcim._109017)
      return "109017";
    if (code == DicomDcim._109018)
      return "109018";
    if (code == DicomDcim._109019)
      return "109019";
    if (code == DicomDcim._109020)
      return "109020";
    if (code == DicomDcim._109021)
      return "109021";
    if (code == DicomDcim._109022)
      return "109022";
    if (code == DicomDcim._109023)
      return "109023";
    if (code == DicomDcim._109024)
      return "109024";
    if (code == DicomDcim._109025)
      return "109025";
    if (code == DicomDcim._109026)
      return "109026";
    if (code == DicomDcim._109027)
      return "109027";
    if (code == DicomDcim._109028)
      return "109028";
    if (code == DicomDcim._109029)
      return "109029";
    if (code == DicomDcim._109030)
      return "109030";
    if (code == DicomDcim._109031)
      return "109031";
    if (code == DicomDcim._109032)
      return "109032";
    if (code == DicomDcim._109033)
      return "109033";
    if (code == DicomDcim._109034)
      return "109034";
    if (code == DicomDcim._109035)
      return "109035";
    if (code == DicomDcim._109036)
      return "109036";
    if (code == DicomDcim._109037)
      return "109037";
    if (code == DicomDcim._109038)
      return "109038";
    if (code == DicomDcim._109039)
      return "109039";
    if (code == DicomDcim._109040)
      return "109040";
    if (code == DicomDcim._109041)
      return "109041";
    if (code == DicomDcim._109042)
      return "109042";
    if (code == DicomDcim._109043)
      return "109043";
    if (code == DicomDcim._109044)
      return "109044";
    if (code == DicomDcim._109045)
      return "109045";
    if (code == DicomDcim._109046)
      return "109046";
    if (code == DicomDcim._109047)
      return "109047";
    if (code == DicomDcim._109048)
      return "109048";
    if (code == DicomDcim._109049)
      return "109049";
    if (code == DicomDcim._109050)
      return "109050";
    if (code == DicomDcim._109051)
      return "109051";
    if (code == DicomDcim._109052)
      return "109052";
    if (code == DicomDcim._109053)
      return "109053";
    if (code == DicomDcim._109054)
      return "109054";
    if (code == DicomDcim._109055)
      return "109055";
    if (code == DicomDcim._109056)
      return "109056";
    if (code == DicomDcim._109057)
      return "109057";
    if (code == DicomDcim._109058)
      return "109058";
    if (code == DicomDcim._109059)
      return "109059";
    if (code == DicomDcim._109060)
      return "109060";
    if (code == DicomDcim._109061)
      return "109061";
    if (code == DicomDcim._109063)
      return "109063";
    if (code == DicomDcim._109070)
      return "109070";
    if (code == DicomDcim._109071)
      return "109071";
    if (code == DicomDcim._109072)
      return "109072";
    if (code == DicomDcim._109073)
      return "109073";
    if (code == DicomDcim._109080)
      return "109080";
    if (code == DicomDcim._109081)
      return "109081";
    if (code == DicomDcim._109082)
      return "109082";
    if (code == DicomDcim._109083)
      return "109083";
    if (code == DicomDcim._109091)
      return "109091";
    if (code == DicomDcim._109092)
      return "109092";
    if (code == DicomDcim._109093)
      return "109093";
    if (code == DicomDcim._109094)
      return "109094";
    if (code == DicomDcim._109095)
      return "109095";
    if (code == DicomDcim._109096)
      return "109096";
    if (code == DicomDcim._109101)
      return "109101";
    if (code == DicomDcim._109102)
      return "109102";
    if (code == DicomDcim._109103)
      return "109103";
    if (code == DicomDcim._109104)
      return "109104";
    if (code == DicomDcim._109105)
      return "109105";
    if (code == DicomDcim._109106)
      return "109106";
    if (code == DicomDcim._109110)
      return "109110";
    if (code == DicomDcim._109111)
      return "109111";
    if (code == DicomDcim._109112)
      return "109112";
    if (code == DicomDcim._109113)
      return "109113";
    if (code == DicomDcim._109114)
      return "109114";
    if (code == DicomDcim._109115)
      return "109115";
    if (code == DicomDcim._109116)
      return "109116";
    if (code == DicomDcim._109117)
      return "109117";
    if (code == DicomDcim._109120)
      return "109120";
    if (code == DicomDcim._109121)
      return "109121";
    if (code == DicomDcim._109122)
      return "109122";
    if (code == DicomDcim._109123)
      return "109123";
    if (code == DicomDcim._109124)
      return "109124";
    if (code == DicomDcim._109125)
      return "109125";
    if (code == DicomDcim._109132)
      return "109132";
    if (code == DicomDcim._109133)
      return "109133";
    if (code == DicomDcim._109134)
      return "109134";
    if (code == DicomDcim._109135)
      return "109135";
    if (code == DicomDcim._109136)
      return "109136";
    if (code == DicomDcim._109200)
      return "109200";
    if (code == DicomDcim._109201)
      return "109201";
    if (code == DicomDcim._109202)
      return "109202";
    if (code == DicomDcim._109203)
      return "109203";
    if (code == DicomDcim._109204)
      return "109204";
    if (code == DicomDcim._109205)
      return "109205";
    if (code == DicomDcim._109206)
      return "109206";
    if (code == DicomDcim._109207)
      return "109207";
    if (code == DicomDcim._109208)
      return "109208";
    if (code == DicomDcim._109209)
      return "109209";
    if (code == DicomDcim._109210)
      return "109210";
    if (code == DicomDcim._109211)
      return "109211";
    if (code == DicomDcim._109212)
      return "109212";
    if (code == DicomDcim._109213)
      return "109213";
    if (code == DicomDcim._109214)
      return "109214";
    if (code == DicomDcim._109215)
      return "109215";
    if (code == DicomDcim._109216)
      return "109216";
    if (code == DicomDcim._109217)
      return "109217";
    if (code == DicomDcim._109218)
      return "109218";
    if (code == DicomDcim._109219)
      return "109219";
    if (code == DicomDcim._109220)
      return "109220";
    if (code == DicomDcim._109221)
      return "109221";
    if (code == DicomDcim._109222)
      return "109222";
    if (code == DicomDcim._109701)
      return "109701";
    if (code == DicomDcim._109702)
      return "109702";
    if (code == DicomDcim._109703)
      return "109703";
    if (code == DicomDcim._109704)
      return "109704";
    if (code == DicomDcim._109705)
      return "109705";
    if (code == DicomDcim._109706)
      return "109706";
    if (code == DicomDcim._109707)
      return "109707";
    if (code == DicomDcim._109708)
      return "109708";
    if (code == DicomDcim._109709)
      return "109709";
    if (code == DicomDcim._109710)
      return "109710";
    if (code == DicomDcim._109801)
      return "109801";
    if (code == DicomDcim._109802)
      return "109802";
    if (code == DicomDcim._109803)
      return "109803";
    if (code == DicomDcim._109804)
      return "109804";
    if (code == DicomDcim._109805)
      return "109805";
    if (code == DicomDcim._109806)
      return "109806";
    if (code == DicomDcim._109807)
      return "109807";
    if (code == DicomDcim._109808)
      return "109808";
    if (code == DicomDcim._109809)
      return "109809";
    if (code == DicomDcim._109810)
      return "109810";
    if (code == DicomDcim._109811)
      return "109811";
    if (code == DicomDcim._109812)
      return "109812";
    if (code == DicomDcim._109813)
      return "109813";
    if (code == DicomDcim._109814)
      return "109814";
    if (code == DicomDcim._109815)
      return "109815";
    if (code == DicomDcim._109816)
      return "109816";
    if (code == DicomDcim._109817)
      return "109817";
    if (code == DicomDcim._109818)
      return "109818";
    if (code == DicomDcim._109819)
      return "109819";
    if (code == DicomDcim._109820)
      return "109820";
    if (code == DicomDcim._109821)
      return "109821";
    if (code == DicomDcim._109822)
      return "109822";
    if (code == DicomDcim._109823)
      return "109823";
    if (code == DicomDcim._109824)
      return "109824";
    if (code == DicomDcim._109825)
      return "109825";
    if (code == DicomDcim._109826)
      return "109826";
    if (code == DicomDcim._109827)
      return "109827";
    if (code == DicomDcim._109828)
      return "109828";
    if (code == DicomDcim._109829)
      return "109829";
    if (code == DicomDcim._109830)
      return "109830";
    if (code == DicomDcim._109831)
      return "109831";
    if (code == DicomDcim._109832)
      return "109832";
    if (code == DicomDcim._109833)
      return "109833";
    if (code == DicomDcim._109834)
      return "109834";
    if (code == DicomDcim._109835)
      return "109835";
    if (code == DicomDcim._109836)
      return "109836";
    if (code == DicomDcim._109837)
      return "109837";
    if (code == DicomDcim._109838)
      return "109838";
    if (code == DicomDcim._109839)
      return "109839";
    if (code == DicomDcim._109840)
      return "109840";
    if (code == DicomDcim._109841)
      return "109841";
    if (code == DicomDcim._109842)
      return "109842";
    if (code == DicomDcim._109843)
      return "109843";
    if (code == DicomDcim._109844)
      return "109844";
    if (code == DicomDcim._109845)
      return "109845";
    if (code == DicomDcim._109846)
      return "109846";
    if (code == DicomDcim._109847)
      return "109847";
    if (code == DicomDcim._109848)
      return "109848";
    if (code == DicomDcim._109849)
      return "109849";
    if (code == DicomDcim._109850)
      return "109850";
    if (code == DicomDcim._109851)
      return "109851";
    if (code == DicomDcim._109852)
      return "109852";
    if (code == DicomDcim._109853)
      return "109853";
    if (code == DicomDcim._109854)
      return "109854";
    if (code == DicomDcim._109855)
      return "109855";
    if (code == DicomDcim._109856)
      return "109856";
    if (code == DicomDcim._109857)
      return "109857";
    if (code == DicomDcim._109858)
      return "109858";
    if (code == DicomDcim._109859)
      return "109859";
    if (code == DicomDcim._109860)
      return "109860";
    if (code == DicomDcim._109861)
      return "109861";
    if (code == DicomDcim._109862)
      return "109862";
    if (code == DicomDcim._109863)
      return "109863";
    if (code == DicomDcim._109864)
      return "109864";
    if (code == DicomDcim._109865)
      return "109865";
    if (code == DicomDcim._109866)
      return "109866";
    if (code == DicomDcim._109867)
      return "109867";
    if (code == DicomDcim._109868)
      return "109868";
    if (code == DicomDcim._109869)
      return "109869";
    if (code == DicomDcim._109870)
      return "109870";
    if (code == DicomDcim._109871)
      return "109871";
    if (code == DicomDcim._109872)
      return "109872";
    if (code == DicomDcim._109873)
      return "109873";
    if (code == DicomDcim._109874)
      return "109874";
    if (code == DicomDcim._109875)
      return "109875";
    if (code == DicomDcim._109876)
      return "109876";
    if (code == DicomDcim._109877)
      return "109877";
    if (code == DicomDcim._109878)
      return "109878";
    if (code == DicomDcim._109879)
      return "109879";
    if (code == DicomDcim._109880)
      return "109880";
    if (code == DicomDcim._109881)
      return "109881";
    if (code == DicomDcim._109901)
      return "109901";
    if (code == DicomDcim._109902)
      return "109902";
    if (code == DicomDcim._109903)
      return "109903";
    if (code == DicomDcim._109904)
      return "109904";
    if (code == DicomDcim._109905)
      return "109905";
    if (code == DicomDcim._109906)
      return "109906";
    if (code == DicomDcim._109907)
      return "109907";
    if (code == DicomDcim._109908)
      return "109908";
    if (code == DicomDcim._109909)
      return "109909";
    if (code == DicomDcim._109910)
      return "109910";
    if (code == DicomDcim._109911)
      return "109911";
    if (code == DicomDcim._109912)
      return "109912";
    if (code == DicomDcim._109913)
      return "109913";
    if (code == DicomDcim._109914)
      return "109914";
    if (code == DicomDcim._109915)
      return "109915";
    if (code == DicomDcim._109916)
      return "109916";
    if (code == DicomDcim._109917)
      return "109917";
    if (code == DicomDcim._109918)
      return "109918";
    if (code == DicomDcim._109919)
      return "109919";
    if (code == DicomDcim._109920)
      return "109920";
    if (code == DicomDcim._109921)
      return "109921";
    if (code == DicomDcim._109931)
      return "109931";
    if (code == DicomDcim._109932)
      return "109932";
    if (code == DicomDcim._109933)
      return "109933";
    if (code == DicomDcim._109941)
      return "109941";
    if (code == DicomDcim._109943)
      return "109943";
    if (code == DicomDcim._109991)
      return "109991";
    if (code == DicomDcim._109992)
      return "109992";
    if (code == DicomDcim._109993)
      return "109993";
    if (code == DicomDcim._109994)
      return "109994";
    if (code == DicomDcim._109995)
      return "109995";
    if (code == DicomDcim._109996)
      return "109996";
    if (code == DicomDcim._109997)
      return "109997";
    if (code == DicomDcim._109998)
      return "109998";
    if (code == DicomDcim._109999)
      return "109999";
    if (code == DicomDcim._110001)
      return "110001";
    if (code == DicomDcim._110002)
      return "110002";
    if (code == DicomDcim._110003)
      return "110003";
    if (code == DicomDcim._110004)
      return "110004";
    if (code == DicomDcim._110005)
      return "110005";
    if (code == DicomDcim._110006)
      return "110006";
    if (code == DicomDcim._110007)
      return "110007";
    if (code == DicomDcim._110008)
      return "110008";
    if (code == DicomDcim._110009)
      return "110009";
    if (code == DicomDcim._110010)
      return "110010";
    if (code == DicomDcim._110011)
      return "110011";
    if (code == DicomDcim._110012)
      return "110012";
    if (code == DicomDcim._110013)
      return "110013";
    if (code == DicomDcim._110020)
      return "110020";
    if (code == DicomDcim._110021)
      return "110021";
    if (code == DicomDcim._110022)
      return "110022";
    if (code == DicomDcim._110023)
      return "110023";
    if (code == DicomDcim._110024)
      return "110024";
    if (code == DicomDcim._110025)
      return "110025";
    if (code == DicomDcim._110026)
      return "110026";
    if (code == DicomDcim._110027)
      return "110027";
    if (code == DicomDcim._110028)
      return "110028";
    if (code == DicomDcim._110030)
      return "110030";
    if (code == DicomDcim._110031)
      return "110031";
    if (code == DicomDcim._110032)
      return "110032";
    if (code == DicomDcim._110033)
      return "110033";
    if (code == DicomDcim._110034)
      return "110034";
    if (code == DicomDcim._110035)
      return "110035";
    if (code == DicomDcim._110036)
      return "110036";
    if (code == DicomDcim._110037)
      return "110037";
    if (code == DicomDcim._110038)
      return "110038";
    if (code == DicomDcim._110100)
      return "110100";
    if (code == DicomDcim._110101)
      return "110101";
    if (code == DicomDcim._110102)
      return "110102";
    if (code == DicomDcim._110103)
      return "110103";
    if (code == DicomDcim._110104)
      return "110104";
    if (code == DicomDcim._110105)
      return "110105";
    if (code == DicomDcim._110106)
      return "110106";
    if (code == DicomDcim._110107)
      return "110107";
    if (code == DicomDcim._110108)
      return "110108";
    if (code == DicomDcim._110109)
      return "110109";
    if (code == DicomDcim._110110)
      return "110110";
    if (code == DicomDcim._110111)
      return "110111";
    if (code == DicomDcim._110112)
      return "110112";
    if (code == DicomDcim._110113)
      return "110113";
    if (code == DicomDcim._110114)
      return "110114";
    if (code == DicomDcim._110120)
      return "110120";
    if (code == DicomDcim._110121)
      return "110121";
    if (code == DicomDcim._110122)
      return "110122";
    if (code == DicomDcim._110123)
      return "110123";
    if (code == DicomDcim._110124)
      return "110124";
    if (code == DicomDcim._110125)
      return "110125";
    if (code == DicomDcim._110126)
      return "110126";
    if (code == DicomDcim._110127)
      return "110127";
    if (code == DicomDcim._110128)
      return "110128";
    if (code == DicomDcim._110129)
      return "110129";
    if (code == DicomDcim._110130)
      return "110130";
    if (code == DicomDcim._110131)
      return "110131";
    if (code == DicomDcim._110132)
      return "110132";
    if (code == DicomDcim._110133)
      return "110133";
    if (code == DicomDcim._110134)
      return "110134";
    if (code == DicomDcim._110135)
      return "110135";
    if (code == DicomDcim._110136)
      return "110136";
    if (code == DicomDcim._110137)
      return "110137";
    if (code == DicomDcim._110138)
      return "110138";
    if (code == DicomDcim._110139)
      return "110139";
    if (code == DicomDcim._110140)
      return "110140";
    if (code == DicomDcim._110141)
      return "110141";
    if (code == DicomDcim._110142)
      return "110142";
    if (code == DicomDcim._110150)
      return "110150";
    if (code == DicomDcim._110151)
      return "110151";
    if (code == DicomDcim._110152)
      return "110152";
    if (code == DicomDcim._110153)
      return "110153";
    if (code == DicomDcim._110154)
      return "110154";
    if (code == DicomDcim._110155)
      return "110155";
    if (code == DicomDcim._110180)
      return "110180";
    if (code == DicomDcim._110181)
      return "110181";
    if (code == DicomDcim._110182)
      return "110182";
    if (code == DicomDcim._110190)
      return "110190";
    if (code == DicomDcim._110500)
      return "110500";
    if (code == DicomDcim._110501)
      return "110501";
    if (code == DicomDcim._110502)
      return "110502";
    if (code == DicomDcim._110503)
      return "110503";
    if (code == DicomDcim._110504)
      return "110504";
    if (code == DicomDcim._110505)
      return "110505";
    if (code == DicomDcim._110506)
      return "110506";
    if (code == DicomDcim._110507)
      return "110507";
    if (code == DicomDcim._110508)
      return "110508";
    if (code == DicomDcim._110509)
      return "110509";
    if (code == DicomDcim._110510)
      return "110510";
    if (code == DicomDcim._110511)
      return "110511";
    if (code == DicomDcim._110512)
      return "110512";
    if (code == DicomDcim._110513)
      return "110513";
    if (code == DicomDcim._110514)
      return "110514";
    if (code == DicomDcim._110515)
      return "110515";
    if (code == DicomDcim._110516)
      return "110516";
    if (code == DicomDcim._110518)
      return "110518";
    if (code == DicomDcim._110519)
      return "110519";
    if (code == DicomDcim._110521)
      return "110521";
    if (code == DicomDcim._110522)
      return "110522";
    if (code == DicomDcim._110523)
      return "110523";
    if (code == DicomDcim._110524)
      return "110524";
    if (code == DicomDcim._110526)
      return "110526";
    if (code == DicomDcim._110527)
      return "110527";
    if (code == DicomDcim._110528)
      return "110528";
    if (code == DicomDcim._110529)
      return "110529";
    if (code == DicomDcim._110700)
      return "110700";
    if (code == DicomDcim._110701)
      return "110701";
    if (code == DicomDcim._110702)
      return "110702";
    if (code == DicomDcim._110703)
      return "110703";
    if (code == DicomDcim._110704)
      return "110704";
    if (code == DicomDcim._110705)
      return "110705";
    if (code == DicomDcim._110706)
      return "110706";
    if (code == DicomDcim._110800)
      return "110800";
    if (code == DicomDcim._110801)
      return "110801";
    if (code == DicomDcim._110802)
      return "110802";
    if (code == DicomDcim._110803)
      return "110803";
    if (code == DicomDcim._110804)
      return "110804";
    if (code == DicomDcim._110805)
      return "110805";
    if (code == DicomDcim._110806)
      return "110806";
    if (code == DicomDcim._110807)
      return "110807";
    if (code == DicomDcim._110808)
      return "110808";
    if (code == DicomDcim._110809)
      return "110809";
    if (code == DicomDcim._110810)
      return "110810";
    if (code == DicomDcim._110811)
      return "110811";
    if (code == DicomDcim._110812)
      return "110812";
    if (code == DicomDcim._110813)
      return "110813";
    if (code == DicomDcim._110814)
      return "110814";
    if (code == DicomDcim._110815)
      return "110815";
    if (code == DicomDcim._110816)
      return "110816";
    if (code == DicomDcim._110817)
      return "110817";
    if (code == DicomDcim._110818)
      return "110818";
    if (code == DicomDcim._110819)
      return "110819";
    if (code == DicomDcim._110820)
      return "110820";
    if (code == DicomDcim._110821)
      return "110821";
    if (code == DicomDcim._110822)
      return "110822";
    if (code == DicomDcim._110823)
      return "110823";
    if (code == DicomDcim._110824)
      return "110824";
    if (code == DicomDcim._110825)
      return "110825";
    if (code == DicomDcim._110826)
      return "110826";
    if (code == DicomDcim._110827)
      return "110827";
    if (code == DicomDcim._110828)
      return "110828";
    if (code == DicomDcim._110829)
      return "110829";
    if (code == DicomDcim._110830)
      return "110830";
    if (code == DicomDcim._110831)
      return "110831";
    if (code == DicomDcim._110832)
      return "110832";
    if (code == DicomDcim._110833)
      return "110833";
    if (code == DicomDcim._110834)
      return "110834";
    if (code == DicomDcim._110835)
      return "110835";
    if (code == DicomDcim._110836)
      return "110836";
    if (code == DicomDcim._110837)
      return "110837";
    if (code == DicomDcim._110838)
      return "110838";
    if (code == DicomDcim._110839)
      return "110839";
    if (code == DicomDcim._110840)
      return "110840";
    if (code == DicomDcim._110841)
      return "110841";
    if (code == DicomDcim._110842)
      return "110842";
    if (code == DicomDcim._110843)
      return "110843";
    if (code == DicomDcim._110844)
      return "110844";
    if (code == DicomDcim._110845)
      return "110845";
    if (code == DicomDcim._110846)
      return "110846";
    if (code == DicomDcim._110847)
      return "110847";
    if (code == DicomDcim._110848)
      return "110848";
    if (code == DicomDcim._110849)
      return "110849";
    if (code == DicomDcim._110850)
      return "110850";
    if (code == DicomDcim._110851)
      return "110851";
    if (code == DicomDcim._110852)
      return "110852";
    if (code == DicomDcim._110853)
      return "110853";
    if (code == DicomDcim._110854)
      return "110854";
    if (code == DicomDcim._110855)
      return "110855";
    if (code == DicomDcim._110856)
      return "110856";
    if (code == DicomDcim._110857)
      return "110857";
    if (code == DicomDcim._110858)
      return "110858";
    if (code == DicomDcim._110859)
      return "110859";
    if (code == DicomDcim._110860)
      return "110860";
    if (code == DicomDcim._110861)
      return "110861";
    if (code == DicomDcim._110862)
      return "110862";
    if (code == DicomDcim._110863)
      return "110863";
    if (code == DicomDcim._110864)
      return "110864";
    if (code == DicomDcim._110865)
      return "110865";
    if (code == DicomDcim._110866)
      return "110866";
    if (code == DicomDcim._110867)
      return "110867";
    if (code == DicomDcim._110868)
      return "110868";
    if (code == DicomDcim._110869)
      return "110869";
    if (code == DicomDcim._110870)
      return "110870";
    if (code == DicomDcim._110871)
      return "110871";
    if (code == DicomDcim._110872)
      return "110872";
    if (code == DicomDcim._110873)
      return "110873";
    if (code == DicomDcim._110874)
      return "110874";
    if (code == DicomDcim._110875)
      return "110875";
    if (code == DicomDcim._110876)
      return "110876";
    if (code == DicomDcim._110877)
      return "110877";
    if (code == DicomDcim._110901)
      return "110901";
    if (code == DicomDcim._110902)
      return "110902";
    if (code == DicomDcim._110903)
      return "110903";
    if (code == DicomDcim._110904)
      return "110904";
    if (code == DicomDcim._110905)
      return "110905";
    if (code == DicomDcim._110906)
      return "110906";
    if (code == DicomDcim._110907)
      return "110907";
    if (code == DicomDcim._110908)
      return "110908";
    if (code == DicomDcim._110909)
      return "110909";
    if (code == DicomDcim._110910)
      return "110910";
    if (code == DicomDcim._110911)
      return "110911";
    if (code == DicomDcim._111001)
      return "111001";
    if (code == DicomDcim._111002)
      return "111002";
    if (code == DicomDcim._111003)
      return "111003";
    if (code == DicomDcim._111004)
      return "111004";
    if (code == DicomDcim._111005)
      return "111005";
    if (code == DicomDcim._111006)
      return "111006";
    if (code == DicomDcim._111007)
      return "111007";
    if (code == DicomDcim._111008)
      return "111008";
    if (code == DicomDcim._111009)
      return "111009";
    if (code == DicomDcim._111010)
      return "111010";
    if (code == DicomDcim._111011)
      return "111011";
    if (code == DicomDcim._111012)
      return "111012";
    if (code == DicomDcim._111013)
      return "111013";
    if (code == DicomDcim._111014)
      return "111014";
    if (code == DicomDcim._111015)
      return "111015";
    if (code == DicomDcim._111016)
      return "111016";
    if (code == DicomDcim._111017)
      return "111017";
    if (code == DicomDcim._111018)
      return "111018";
    if (code == DicomDcim._111019)
      return "111019";
    if (code == DicomDcim._111020)
      return "111020";
    if (code == DicomDcim._111021)
      return "111021";
    if (code == DicomDcim._111022)
      return "111022";
    if (code == DicomDcim._111023)
      return "111023";
    if (code == DicomDcim._111024)
      return "111024";
    if (code == DicomDcim._111025)
      return "111025";
    if (code == DicomDcim._111026)
      return "111026";
    if (code == DicomDcim._111027)
      return "111027";
    if (code == DicomDcim._111028)
      return "111028";
    if (code == DicomDcim._111029)
      return "111029";
    if (code == DicomDcim._111030)
      return "111030";
    if (code == DicomDcim._111031)
      return "111031";
    if (code == DicomDcim._111032)
      return "111032";
    if (code == DicomDcim._111033)
      return "111033";
    if (code == DicomDcim._111034)
      return "111034";
    if (code == DicomDcim._111035)
      return "111035";
    if (code == DicomDcim._111036)
      return "111036";
    if (code == DicomDcim._111037)
      return "111037";
    if (code == DicomDcim._111038)
      return "111038";
    if (code == DicomDcim._111039)
      return "111039";
    if (code == DicomDcim._111040)
      return "111040";
    if (code == DicomDcim._111041)
      return "111041";
    if (code == DicomDcim._111042)
      return "111042";
    if (code == DicomDcim._111043)
      return "111043";
    if (code == DicomDcim._111044)
      return "111044";
    if (code == DicomDcim._111045)
      return "111045";
    if (code == DicomDcim._111046)
      return "111046";
    if (code == DicomDcim._111047)
      return "111047";
    if (code == DicomDcim._111048)
      return "111048";
    if (code == DicomDcim._111049)
      return "111049";
    if (code == DicomDcim._111050)
      return "111050";
    if (code == DicomDcim._111051)
      return "111051";
    if (code == DicomDcim._111052)
      return "111052";
    if (code == DicomDcim._111053)
      return "111053";
    if (code == DicomDcim._111054)
      return "111054";
    if (code == DicomDcim._111055)
      return "111055";
    if (code == DicomDcim._111056)
      return "111056";
    if (code == DicomDcim._111057)
      return "111057";
    if (code == DicomDcim._111058)
      return "111058";
    if (code == DicomDcim._111059)
      return "111059";
    if (code == DicomDcim._111060)
      return "111060";
    if (code == DicomDcim._111061)
      return "111061";
    if (code == DicomDcim._111062)
      return "111062";
    if (code == DicomDcim._111063)
      return "111063";
    if (code == DicomDcim._111064)
      return "111064";
    if (code == DicomDcim._111065)
      return "111065";
    if (code == DicomDcim._111066)
      return "111066";
    if (code == DicomDcim._111069)
      return "111069";
    if (code == DicomDcim._111071)
      return "111071";
    if (code == DicomDcim._111072)
      return "111072";
    if (code == DicomDcim._111081)
      return "111081";
    if (code == DicomDcim._111086)
      return "111086";
    if (code == DicomDcim._111087)
      return "111087";
    if (code == DicomDcim._111088)
      return "111088";
    if (code == DicomDcim._111089)
      return "111089";
    if (code == DicomDcim._111090)
      return "111090";
    if (code == DicomDcim._111091)
      return "111091";
    if (code == DicomDcim._111092)
      return "111092";
    if (code == DicomDcim._111093)
      return "111093";
    if (code == DicomDcim._111099)
      return "111099";
    if (code == DicomDcim._111100)
      return "111100";
    if (code == DicomDcim._111101)
      return "111101";
    if (code == DicomDcim._111102)
      return "111102";
    if (code == DicomDcim._111103)
      return "111103";
    if (code == DicomDcim._111104)
      return "111104";
    if (code == DicomDcim._111105)
      return "111105";
    if (code == DicomDcim._111111)
      return "111111";
    if (code == DicomDcim._111112)
      return "111112";
    if (code == DicomDcim._111113)
      return "111113";
    if (code == DicomDcim._111120)
      return "111120";
    if (code == DicomDcim._111121)
      return "111121";
    if (code == DicomDcim._111122)
      return "111122";
    if (code == DicomDcim._111123)
      return "111123";
    if (code == DicomDcim._111124)
      return "111124";
    if (code == DicomDcim._111125)
      return "111125";
    if (code == DicomDcim._111126)
      return "111126";
    if (code == DicomDcim._111127)
      return "111127";
    if (code == DicomDcim._111128)
      return "111128";
    if (code == DicomDcim._111129)
      return "111129";
    if (code == DicomDcim._111130)
      return "111130";
    if (code == DicomDcim._111135)
      return "111135";
    if (code == DicomDcim._111136)
      return "111136";
    if (code == DicomDcim._111137)
      return "111137";
    if (code == DicomDcim._111138)
      return "111138";
    if (code == DicomDcim._111139)
      return "111139";
    if (code == DicomDcim._111140)
      return "111140";
    if (code == DicomDcim._111141)
      return "111141";
    if (code == DicomDcim._111142)
      return "111142";
    if (code == DicomDcim._111143)
      return "111143";
    if (code == DicomDcim._111144)
      return "111144";
    if (code == DicomDcim._111145)
      return "111145";
    if (code == DicomDcim._111146)
      return "111146";
    if (code == DicomDcim._111147)
      return "111147";
    if (code == DicomDcim._111148)
      return "111148";
    if (code == DicomDcim._111149)
      return "111149";
    if (code == DicomDcim._111150)
      return "111150";
    if (code == DicomDcim._111151)
      return "111151";
    if (code == DicomDcim._111152)
      return "111152";
    if (code == DicomDcim._111153)
      return "111153";
    if (code == DicomDcim._111154)
      return "111154";
    if (code == DicomDcim._111155)
      return "111155";
    if (code == DicomDcim._111156)
      return "111156";
    if (code == DicomDcim._111157)
      return "111157";
    if (code == DicomDcim._111158)
      return "111158";
    if (code == DicomDcim._111159)
      return "111159";
    if (code == DicomDcim._111168)
      return "111168";
    if (code == DicomDcim._111170)
      return "111170";
    if (code == DicomDcim._111171)
      return "111171";
    if (code == DicomDcim._111172)
      return "111172";
    if (code == DicomDcim._111173)
      return "111173";
    if (code == DicomDcim._111174)
      return "111174";
    if (code == DicomDcim._111175)
      return "111175";
    if (code == DicomDcim._111176)
      return "111176";
    if (code == DicomDcim._111177)
      return "111177";
    if (code == DicomDcim._111178)
      return "111178";
    if (code == DicomDcim._111179)
      return "111179";
    if (code == DicomDcim._111180)
      return "111180";
    if (code == DicomDcim._111181)
      return "111181";
    if (code == DicomDcim._111182)
      return "111182";
    if (code == DicomDcim._111183)
      return "111183";
    if (code == DicomDcim._111184)
      return "111184";
    if (code == DicomDcim._111185)
      return "111185";
    if (code == DicomDcim._111186)
      return "111186";
    if (code == DicomDcim._111187)
      return "111187";
    if (code == DicomDcim._111188)
      return "111188";
    if (code == DicomDcim._111189)
      return "111189";
    if (code == DicomDcim._111190)
      return "111190";
    if (code == DicomDcim._111191)
      return "111191";
    if (code == DicomDcim._111192)
      return "111192";
    if (code == DicomDcim._111193)
      return "111193";
    if (code == DicomDcim._111194)
      return "111194";
    if (code == DicomDcim._111195)
      return "111195";
    if (code == DicomDcim._111196)
      return "111196";
    if (code == DicomDcim._111197)
      return "111197";
    if (code == DicomDcim._111198)
      return "111198";
    if (code == DicomDcim._111199)
      return "111199";
    if (code == DicomDcim._111200)
      return "111200";
    if (code == DicomDcim._111201)
      return "111201";
    if (code == DicomDcim._111202)
      return "111202";
    if (code == DicomDcim._111203)
      return "111203";
    if (code == DicomDcim._111204)
      return "111204";
    if (code == DicomDcim._111205)
      return "111205";
    if (code == DicomDcim._111206)
      return "111206";
    if (code == DicomDcim._111207)
      return "111207";
    if (code == DicomDcim._111208)
      return "111208";
    if (code == DicomDcim._111209)
      return "111209";
    if (code == DicomDcim._111210)
      return "111210";
    if (code == DicomDcim._111211)
      return "111211";
    if (code == DicomDcim._111212)
      return "111212";
    if (code == DicomDcim._111213)
      return "111213";
    if (code == DicomDcim._111214)
      return "111214";
    if (code == DicomDcim._111215)
      return "111215";
    if (code == DicomDcim._111216)
      return "111216";
    if (code == DicomDcim._111217)
      return "111217";
    if (code == DicomDcim._111218)
      return "111218";
    if (code == DicomDcim._111219)
      return "111219";
    if (code == DicomDcim._111220)
      return "111220";
    if (code == DicomDcim._111221)
      return "111221";
    if (code == DicomDcim._111222)
      return "111222";
    if (code == DicomDcim._111223)
      return "111223";
    if (code == DicomDcim._111224)
      return "111224";
    if (code == DicomDcim._111225)
      return "111225";
    if (code == DicomDcim._111233)
      return "111233";
    if (code == DicomDcim._111234)
      return "111234";
    if (code == DicomDcim._111235)
      return "111235";
    if (code == DicomDcim._111236)
      return "111236";
    if (code == DicomDcim._111237)
      return "111237";
    if (code == DicomDcim._111238)
      return "111238";
    if (code == DicomDcim._111239)
      return "111239";
    if (code == DicomDcim._111240)
      return "111240";
    if (code == DicomDcim._111241)
      return "111241";
    if (code == DicomDcim._111242)
      return "111242";
    if (code == DicomDcim._111243)
      return "111243";
    if (code == DicomDcim._111244)
      return "111244";
    if (code == DicomDcim._111245)
      return "111245";
    if (code == DicomDcim._111248)
      return "111248";
    if (code == DicomDcim._111249)
      return "111249";
    if (code == DicomDcim._111250)
      return "111250";
    if (code == DicomDcim._111251)
      return "111251";
    if (code == DicomDcim._111252)
      return "111252";
    if (code == DicomDcim._111253)
      return "111253";
    if (code == DicomDcim._111254)
      return "111254";
    if (code == DicomDcim._111255)
      return "111255";
    if (code == DicomDcim._111256)
      return "111256";
    if (code == DicomDcim._111257)
      return "111257";
    if (code == DicomDcim._111258)
      return "111258";
    if (code == DicomDcim._111259)
      return "111259";
    if (code == DicomDcim._111260)
      return "111260";
    if (code == DicomDcim._111262)
      return "111262";
    if (code == DicomDcim._111263)
      return "111263";
    if (code == DicomDcim._111264)
      return "111264";
    if (code == DicomDcim._111265)
      return "111265";
    if (code == DicomDcim._111269)
      return "111269";
    if (code == DicomDcim._111271)
      return "111271";
    if (code == DicomDcim._111273)
      return "111273";
    if (code == DicomDcim._111277)
      return "111277";
    if (code == DicomDcim._111278)
      return "111278";
    if (code == DicomDcim._111279)
      return "111279";
    if (code == DicomDcim._111281)
      return "111281";
    if (code == DicomDcim._111283)
      return "111283";
    if (code == DicomDcim._111284)
      return "111284";
    if (code == DicomDcim._111285)
      return "111285";
    if (code == DicomDcim._111286)
      return "111286";
    if (code == DicomDcim._111287)
      return "111287";
    if (code == DicomDcim._111288)
      return "111288";
    if (code == DicomDcim._111290)
      return "111290";
    if (code == DicomDcim._111291)
      return "111291";
    if (code == DicomDcim._111292)
      return "111292";
    if (code == DicomDcim._111293)
      return "111293";
    if (code == DicomDcim._111294)
      return "111294";
    if (code == DicomDcim._111296)
      return "111296";
    if (code == DicomDcim._111297)
      return "111297";
    if (code == DicomDcim._111298)
      return "111298";
    if (code == DicomDcim._111299)
      return "111299";
    if (code == DicomDcim._111300)
      return "111300";
    if (code == DicomDcim._111301)
      return "111301";
    if (code == DicomDcim._111302)
      return "111302";
    if (code == DicomDcim._111303)
      return "111303";
    if (code == DicomDcim._111304)
      return "111304";
    if (code == DicomDcim._111305)
      return "111305";
    if (code == DicomDcim._111306)
      return "111306";
    if (code == DicomDcim._111307)
      return "111307";
    if (code == DicomDcim._111308)
      return "111308";
    if (code == DicomDcim._111309)
      return "111309";
    if (code == DicomDcim._111310)
      return "111310";
    if (code == DicomDcim._111311)
      return "111311";
    if (code == DicomDcim._111312)
      return "111312";
    if (code == DicomDcim._111313)
      return "111313";
    if (code == DicomDcim._111314)
      return "111314";
    if (code == DicomDcim._111315)
      return "111315";
    if (code == DicomDcim._111316)
      return "111316";
    if (code == DicomDcim._111317)
      return "111317";
    if (code == DicomDcim._111318)
      return "111318";
    if (code == DicomDcim._111320)
      return "111320";
    if (code == DicomDcim._111321)
      return "111321";
    if (code == DicomDcim._111322)
      return "111322";
    if (code == DicomDcim._111323)
      return "111323";
    if (code == DicomDcim._111324)
      return "111324";
    if (code == DicomDcim._111325)
      return "111325";
    if (code == DicomDcim._111326)
      return "111326";
    if (code == DicomDcim._111327)
      return "111327";
    if (code == DicomDcim._111328)
      return "111328";
    if (code == DicomDcim._111329)
      return "111329";
    if (code == DicomDcim._111330)
      return "111330";
    if (code == DicomDcim._111331)
      return "111331";
    if (code == DicomDcim._111332)
      return "111332";
    if (code == DicomDcim._111333)
      return "111333";
    if (code == DicomDcim._111334)
      return "111334";
    if (code == DicomDcim._111335)
      return "111335";
    if (code == DicomDcim._111336)
      return "111336";
    if (code == DicomDcim._111338)
      return "111338";
    if (code == DicomDcim._111340)
      return "111340";
    if (code == DicomDcim._111341)
      return "111341";
    if (code == DicomDcim._111342)
      return "111342";
    if (code == DicomDcim._111343)
      return "111343";
    if (code == DicomDcim._111344)
      return "111344";
    if (code == DicomDcim._111345)
      return "111345";
    if (code == DicomDcim._111346)
      return "111346";
    if (code == DicomDcim._111347)
      return "111347";
    if (code == DicomDcim._111350)
      return "111350";
    if (code == DicomDcim._111351)
      return "111351";
    if (code == DicomDcim._111352)
      return "111352";
    if (code == DicomDcim._111353)
      return "111353";
    if (code == DicomDcim._111354)
      return "111354";
    if (code == DicomDcim._111355)
      return "111355";
    if (code == DicomDcim._111356)
      return "111356";
    if (code == DicomDcim._111357)
      return "111357";
    if (code == DicomDcim._111358)
      return "111358";
    if (code == DicomDcim._111359)
      return "111359";
    if (code == DicomDcim._111360)
      return "111360";
    if (code == DicomDcim._111361)
      return "111361";
    if (code == DicomDcim._111362)
      return "111362";
    if (code == DicomDcim._111363)
      return "111363";
    if (code == DicomDcim._111364)
      return "111364";
    if (code == DicomDcim._111365)
      return "111365";
    if (code == DicomDcim._111366)
      return "111366";
    if (code == DicomDcim._111367)
      return "111367";
    if (code == DicomDcim._111368)
      return "111368";
    if (code == DicomDcim._111369)
      return "111369";
    if (code == DicomDcim._111370)
      return "111370";
    if (code == DicomDcim._111371)
      return "111371";
    if (code == DicomDcim._111372)
      return "111372";
    if (code == DicomDcim._111373)
      return "111373";
    if (code == DicomDcim._111374)
      return "111374";
    if (code == DicomDcim._111375)
      return "111375";
    if (code == DicomDcim._111376)
      return "111376";
    if (code == DicomDcim._111377)
      return "111377";
    if (code == DicomDcim._111380)
      return "111380";
    if (code == DicomDcim._111381)
      return "111381";
    if (code == DicomDcim._111382)
      return "111382";
    if (code == DicomDcim._111383)
      return "111383";
    if (code == DicomDcim._111384)
      return "111384";
    if (code == DicomDcim._111385)
      return "111385";
    if (code == DicomDcim._111386)
      return "111386";
    if (code == DicomDcim._111387)
      return "111387";
    if (code == DicomDcim._111388)
      return "111388";
    if (code == DicomDcim._111389)
      return "111389";
    if (code == DicomDcim._111390)
      return "111390";
    if (code == DicomDcim._111391)
      return "111391";
    if (code == DicomDcim._111392)
      return "111392";
    if (code == DicomDcim._111393)
      return "111393";
    if (code == DicomDcim._111394)
      return "111394";
    if (code == DicomDcim._111395)
      return "111395";
    if (code == DicomDcim._111396)
      return "111396";
    if (code == DicomDcim._111397)
      return "111397";
    if (code == DicomDcim._111398)
      return "111398";
    if (code == DicomDcim._111399)
      return "111399";
    if (code == DicomDcim._111400)
      return "111400";
    if (code == DicomDcim._111401)
      return "111401";
    if (code == DicomDcim._111402)
      return "111402";
    if (code == DicomDcim._111403)
      return "111403";
    if (code == DicomDcim._111404)
      return "111404";
    if (code == DicomDcim._111405)
      return "111405";
    if (code == DicomDcim._111406)
      return "111406";
    if (code == DicomDcim._111407)
      return "111407";
    if (code == DicomDcim._111408)
      return "111408";
    if (code == DicomDcim._111409)
      return "111409";
    if (code == DicomDcim._111410)
      return "111410";
    if (code == DicomDcim._111411)
      return "111411";
    if (code == DicomDcim._111412)
      return "111412";
    if (code == DicomDcim._111413)
      return "111413";
    if (code == DicomDcim._111414)
      return "111414";
    if (code == DicomDcim._111415)
      return "111415";
    if (code == DicomDcim._111416)
      return "111416";
    if (code == DicomDcim._111417)
      return "111417";
    if (code == DicomDcim._111418)
      return "111418";
    if (code == DicomDcim._111419)
      return "111419";
    if (code == DicomDcim._111420)
      return "111420";
    if (code == DicomDcim._111421)
      return "111421";
    if (code == DicomDcim._111423)
      return "111423";
    if (code == DicomDcim._111424)
      return "111424";
    if (code == DicomDcim._111425)
      return "111425";
    if (code == DicomDcim._111426)
      return "111426";
    if (code == DicomDcim._111427)
      return "111427";
    if (code == DicomDcim._111428)
      return "111428";
    if (code == DicomDcim._111429)
      return "111429";
    if (code == DicomDcim._111430)
      return "111430";
    if (code == DicomDcim._111431)
      return "111431";
    if (code == DicomDcim._111432)
      return "111432";
    if (code == DicomDcim._111433)
      return "111433";
    if (code == DicomDcim._111434)
      return "111434";
    if (code == DicomDcim._111435)
      return "111435";
    if (code == DicomDcim._111436)
      return "111436";
    if (code == DicomDcim._111437)
      return "111437";
    if (code == DicomDcim._111438)
      return "111438";
    if (code == DicomDcim._111439)
      return "111439";
    if (code == DicomDcim._111440)
      return "111440";
    if (code == DicomDcim._111441)
      return "111441";
    if (code == DicomDcim._111442)
      return "111442";
    if (code == DicomDcim._111443)
      return "111443";
    if (code == DicomDcim._111444)
      return "111444";
    if (code == DicomDcim._111445)
      return "111445";
    if (code == DicomDcim._111446)
      return "111446";
    if (code == DicomDcim._111447)
      return "111447";
    if (code == DicomDcim._111448)
      return "111448";
    if (code == DicomDcim._111449)
      return "111449";
    if (code == DicomDcim._111450)
      return "111450";
    if (code == DicomDcim._111451)
      return "111451";
    if (code == DicomDcim._111452)
      return "111452";
    if (code == DicomDcim._111453)
      return "111453";
    if (code == DicomDcim._111454)
      return "111454";
    if (code == DicomDcim._111455)
      return "111455";
    if (code == DicomDcim._111456)
      return "111456";
    if (code == DicomDcim._111457)
      return "111457";
    if (code == DicomDcim._111458)
      return "111458";
    if (code == DicomDcim._111459)
      return "111459";
    if (code == DicomDcim._111460)
      return "111460";
    if (code == DicomDcim._111461)
      return "111461";
    if (code == DicomDcim._111462)
      return "111462";
    if (code == DicomDcim._111463)
      return "111463";
    if (code == DicomDcim._111464)
      return "111464";
    if (code == DicomDcim._111465)
      return "111465";
    if (code == DicomDcim._111466)
      return "111466";
    if (code == DicomDcim._111467)
      return "111467";
    if (code == DicomDcim._111468)
      return "111468";
    if (code == DicomDcim._111469)
      return "111469";
    if (code == DicomDcim._111470)
      return "111470";
    if (code == DicomDcim._111471)
      return "111471";
    if (code == DicomDcim._111472)
      return "111472";
    if (code == DicomDcim._111473)
      return "111473";
    if (code == DicomDcim._111474)
      return "111474";
    if (code == DicomDcim._111475)
      return "111475";
    if (code == DicomDcim._111476)
      return "111476";
    if (code == DicomDcim._111477)
      return "111477";
    if (code == DicomDcim._111478)
      return "111478";
    if (code == DicomDcim._111479)
      return "111479";
    if (code == DicomDcim._111480)
      return "111480";
    if (code == DicomDcim._111481)
      return "111481";
    if (code == DicomDcim._111482)
      return "111482";
    if (code == DicomDcim._111483)
      return "111483";
    if (code == DicomDcim._111484)
      return "111484";
    if (code == DicomDcim._111485)
      return "111485";
    if (code == DicomDcim._111486)
      return "111486";
    if (code == DicomDcim._111487)
      return "111487";
    if (code == DicomDcim._111488)
      return "111488";
    if (code == DicomDcim._111489)
      return "111489";
    if (code == DicomDcim._111490)
      return "111490";
    if (code == DicomDcim._111491)
      return "111491";
    if (code == DicomDcim._111492)
      return "111492";
    if (code == DicomDcim._111494)
      return "111494";
    if (code == DicomDcim._111495)
      return "111495";
    if (code == DicomDcim._111496)
      return "111496";
    if (code == DicomDcim._111497)
      return "111497";
    if (code == DicomDcim._111498)
      return "111498";
    if (code == DicomDcim._111499)
      return "111499";
    if (code == DicomDcim._111500)
      return "111500";
    if (code == DicomDcim._111501)
      return "111501";
    if (code == DicomDcim._111502)
      return "111502";
    if (code == DicomDcim._111503)
      return "111503";
    if (code == DicomDcim._111504)
      return "111504";
    if (code == DicomDcim._111505)
      return "111505";
    if (code == DicomDcim._111506)
      return "111506";
    if (code == DicomDcim._111507)
      return "111507";
    if (code == DicomDcim._111508)
      return "111508";
    if (code == DicomDcim._111509)
      return "111509";
    if (code == DicomDcim._111510)
      return "111510";
    if (code == DicomDcim._111511)
      return "111511";
    if (code == DicomDcim._111512)
      return "111512";
    if (code == DicomDcim._111513)
      return "111513";
    if (code == DicomDcim._111514)
      return "111514";
    if (code == DicomDcim._111515)
      return "111515";
    if (code == DicomDcim._111516)
      return "111516";
    if (code == DicomDcim._111517)
      return "111517";
    if (code == DicomDcim._111518)
      return "111518";
    if (code == DicomDcim._111519)
      return "111519";
    if (code == DicomDcim._111520)
      return "111520";
    if (code == DicomDcim._111521)
      return "111521";
    if (code == DicomDcim._111522)
      return "111522";
    if (code == DicomDcim._111523)
      return "111523";
    if (code == DicomDcim._111524)
      return "111524";
    if (code == DicomDcim._111525)
      return "111525";
    if (code == DicomDcim._111526)
      return "111526";
    if (code == DicomDcim._111527)
      return "111527";
    if (code == DicomDcim._111528)
      return "111528";
    if (code == DicomDcim._111529)
      return "111529";
    if (code == DicomDcim._111530)
      return "111530";
    if (code == DicomDcim._111531)
      return "111531";
    if (code == DicomDcim._111532)
      return "111532";
    if (code == DicomDcim._111533)
      return "111533";
    if (code == DicomDcim._111534)
      return "111534";
    if (code == DicomDcim._111535)
      return "111535";
    if (code == DicomDcim._111536)
      return "111536";
    if (code == DicomDcim._111537)
      return "111537";
    if (code == DicomDcim._111538)
      return "111538";
    if (code == DicomDcim._111539)
      return "111539";
    if (code == DicomDcim._111540)
      return "111540";
    if (code == DicomDcim._111541)
      return "111541";
    if (code == DicomDcim._111542)
      return "111542";
    if (code == DicomDcim._111543)
      return "111543";
    if (code == DicomDcim._111544)
      return "111544";
    if (code == DicomDcim._111545)
      return "111545";
    if (code == DicomDcim._111546)
      return "111546";
    if (code == DicomDcim._111547)
      return "111547";
    if (code == DicomDcim._111548)
      return "111548";
    if (code == DicomDcim._111549)
      return "111549";
    if (code == DicomDcim._111550)
      return "111550";
    if (code == DicomDcim._111551)
      return "111551";
    if (code == DicomDcim._111552)
      return "111552";
    if (code == DicomDcim._111553)
      return "111553";
    if (code == DicomDcim._111554)
      return "111554";
    if (code == DicomDcim._111555)
      return "111555";
    if (code == DicomDcim._111556)
      return "111556";
    if (code == DicomDcim._111557)
      return "111557";
    if (code == DicomDcim._111558)
      return "111558";
    if (code == DicomDcim._111559)
      return "111559";
    if (code == DicomDcim._111560)
      return "111560";
    if (code == DicomDcim._111561)
      return "111561";
    if (code == DicomDcim._111562)
      return "111562";
    if (code == DicomDcim._111563)
      return "111563";
    if (code == DicomDcim._111564)
      return "111564";
    if (code == DicomDcim._111565)
      return "111565";
    if (code == DicomDcim._111566)
      return "111566";
    if (code == DicomDcim._111567)
      return "111567";
    if (code == DicomDcim._111568)
      return "111568";
    if (code == DicomDcim._111569)
      return "111569";
    if (code == DicomDcim._111570)
      return "111570";
    if (code == DicomDcim._111571)
      return "111571";
    if (code == DicomDcim._111572)
      return "111572";
    if (code == DicomDcim._111573)
      return "111573";
    if (code == DicomDcim._111574)
      return "111574";
    if (code == DicomDcim._111575)
      return "111575";
    if (code == DicomDcim._111576)
      return "111576";
    if (code == DicomDcim._111577)
      return "111577";
    if (code == DicomDcim._111578)
      return "111578";
    if (code == DicomDcim._111579)
      return "111579";
    if (code == DicomDcim._111580)
      return "111580";
    if (code == DicomDcim._111581)
      return "111581";
    if (code == DicomDcim._111582)
      return "111582";
    if (code == DicomDcim._111583)
      return "111583";
    if (code == DicomDcim._111584)
      return "111584";
    if (code == DicomDcim._111585)
      return "111585";
    if (code == DicomDcim._111586)
      return "111586";
    if (code == DicomDcim._111587)
      return "111587";
    if (code == DicomDcim._111590)
      return "111590";
    if (code == DicomDcim._111591)
      return "111591";
    if (code == DicomDcim._111592)
      return "111592";
    if (code == DicomDcim._111593)
      return "111593";
    if (code == DicomDcim._111601)
      return "111601";
    if (code == DicomDcim._111602)
      return "111602";
    if (code == DicomDcim._111603)
      return "111603";
    if (code == DicomDcim._111604)
      return "111604";
    if (code == DicomDcim._111605)
      return "111605";
    if (code == DicomDcim._111606)
      return "111606";
    if (code == DicomDcim._111607)
      return "111607";
    if (code == DicomDcim._111609)
      return "111609";
    if (code == DicomDcim._111621)
      return "111621";
    if (code == DicomDcim._111622)
      return "111622";
    if (code == DicomDcim._111623)
      return "111623";
    if (code == DicomDcim._111625)
      return "111625";
    if (code == DicomDcim._111626)
      return "111626";
    if (code == DicomDcim._111627)
      return "111627";
    if (code == DicomDcim._111628)
      return "111628";
    if (code == DicomDcim._111629)
      return "111629";
    if (code == DicomDcim._111630)
      return "111630";
    if (code == DicomDcim._111631)
      return "111631";
    if (code == DicomDcim._111632)
      return "111632";
    if (code == DicomDcim._111633)
      return "111633";
    if (code == DicomDcim._111634)
      return "111634";
    if (code == DicomDcim._111635)
      return "111635";
    if (code == DicomDcim._111636)
      return "111636";
    if (code == DicomDcim._111637)
      return "111637";
    if (code == DicomDcim._111638)
      return "111638";
    if (code == DicomDcim._111641)
      return "111641";
    if (code == DicomDcim._111642)
      return "111642";
    if (code == DicomDcim._111643)
      return "111643";
    if (code == DicomDcim._111644)
      return "111644";
    if (code == DicomDcim._111645)
      return "111645";
    if (code == DicomDcim._111646)
      return "111646";
    if (code == DicomDcim._111671)
      return "111671";
    if (code == DicomDcim._111672)
      return "111672";
    if (code == DicomDcim._111673)
      return "111673";
    if (code == DicomDcim._111674)
      return "111674";
    if (code == DicomDcim._111675)
      return "111675";
    if (code == DicomDcim._111676)
      return "111676";
    if (code == DicomDcim._111677)
      return "111677";
    if (code == DicomDcim._111678)
      return "111678";
    if (code == DicomDcim._111679)
      return "111679";
    if (code == DicomDcim._111680)
      return "111680";
    if (code == DicomDcim._111685)
      return "111685";
    if (code == DicomDcim._111686)
      return "111686";
    if (code == DicomDcim._111687)
      return "111687";
    if (code == DicomDcim._111688)
      return "111688";
    if (code == DicomDcim._111689)
      return "111689";
    if (code == DicomDcim._111690)
      return "111690";
    if (code == DicomDcim._111691)
      return "111691";
    if (code == DicomDcim._111692)
      return "111692";
    if (code == DicomDcim._111693)
      return "111693";
    if (code == DicomDcim._111694)
      return "111694";
    if (code == DicomDcim._111695)
      return "111695";
    if (code == DicomDcim._111696)
      return "111696";
    if (code == DicomDcim._111697)
      return "111697";
    if (code == DicomDcim._111698)
      return "111698";
    if (code == DicomDcim._111700)
      return "111700";
    if (code == DicomDcim._111701)
      return "111701";
    if (code == DicomDcim._111702)
      return "111702";
    if (code == DicomDcim._111703)
      return "111703";
    if (code == DicomDcim._111704)
      return "111704";
    if (code == DicomDcim._111705)
      return "111705";
    if (code == DicomDcim._111706)
      return "111706";
    if (code == DicomDcim._111707)
      return "111707";
    if (code == DicomDcim._111708)
      return "111708";
    if (code == DicomDcim._111709)
      return "111709";
    if (code == DicomDcim._111710)
      return "111710";
    if (code == DicomDcim._111711)
      return "111711";
    if (code == DicomDcim._111712)
      return "111712";
    if (code == DicomDcim._111718)
      return "111718";
    if (code == DicomDcim._111719)
      return "111719";
    if (code == DicomDcim._111720)
      return "111720";
    if (code == DicomDcim._111721)
      return "111721";
    if (code == DicomDcim._111723)
      return "111723";
    if (code == DicomDcim._111724)
      return "111724";
    if (code == DicomDcim._111726)
      return "111726";
    if (code == DicomDcim._111727)
      return "111727";
    if (code == DicomDcim._111729)
      return "111729";
    if (code == DicomDcim._111741)
      return "111741";
    if (code == DicomDcim._111742)
      return "111742";
    if (code == DicomDcim._111743)
      return "111743";
    if (code == DicomDcim._111744)
      return "111744";
    if (code == DicomDcim._111745)
      return "111745";
    if (code == DicomDcim._111746)
      return "111746";
    if (code == DicomDcim._111747)
      return "111747";
    if (code == DicomDcim._111748)
      return "111748";
    if (code == DicomDcim._111749)
      return "111749";
    if (code == DicomDcim._111750)
      return "111750";
    if (code == DicomDcim._111751)
      return "111751";
    if (code == DicomDcim._111752)
      return "111752";
    if (code == DicomDcim._111753)
      return "111753";
    if (code == DicomDcim._111754)
      return "111754";
    if (code == DicomDcim._111755)
      return "111755";
    if (code == DicomDcim._111756)
      return "111756";
    if (code == DicomDcim._111760)
      return "111760";
    if (code == DicomDcim._111761)
      return "111761";
    if (code == DicomDcim._111762)
      return "111762";
    if (code == DicomDcim._111763)
      return "111763";
    if (code == DicomDcim._111764)
      return "111764";
    if (code == DicomDcim._111765)
      return "111765";
    if (code == DicomDcim._111766)
      return "111766";
    if (code == DicomDcim._111767)
      return "111767";
    if (code == DicomDcim._111768)
      return "111768";
    if (code == DicomDcim._111769)
      return "111769";
    if (code == DicomDcim._111770)
      return "111770";
    if (code == DicomDcim._111771)
      return "111771";
    if (code == DicomDcim._111772)
      return "111772";
    if (code == DicomDcim._111773)
      return "111773";
    if (code == DicomDcim._111776)
      return "111776";
    if (code == DicomDcim._111777)
      return "111777";
    if (code == DicomDcim._111778)
      return "111778";
    if (code == DicomDcim._111779)
      return "111779";
    if (code == DicomDcim._111780)
      return "111780";
    if (code == DicomDcim._111781)
      return "111781";
    if (code == DicomDcim._111782)
      return "111782";
    if (code == DicomDcim._111783)
      return "111783";
    if (code == DicomDcim._111786)
      return "111786";
    if (code == DicomDcim._111787)
      return "111787";
    if (code == DicomDcim._111791)
      return "111791";
    if (code == DicomDcim._111792)
      return "111792";
    if (code == DicomDcim._111800)
      return "111800";
    if (code == DicomDcim._111801)
      return "111801";
    if (code == DicomDcim._111802)
      return "111802";
    if (code == DicomDcim._111803)
      return "111803";
    if (code == DicomDcim._111804)
      return "111804";
    if (code == DicomDcim._111805)
      return "111805";
    if (code == DicomDcim._111806)
      return "111806";
    if (code == DicomDcim._111807)
      return "111807";
    if (code == DicomDcim._111808)
      return "111808";
    if (code == DicomDcim._111809)
      return "111809";
    if (code == DicomDcim._111810)
      return "111810";
    if (code == DicomDcim._111811)
      return "111811";
    if (code == DicomDcim._111812)
      return "111812";
    if (code == DicomDcim._111813)
      return "111813";
    if (code == DicomDcim._111814)
      return "111814";
    if (code == DicomDcim._111815)
      return "111815";
    if (code == DicomDcim._111816)
      return "111816";
    if (code == DicomDcim._111817)
      return "111817";
    if (code == DicomDcim._111818)
      return "111818";
    if (code == DicomDcim._111819)
      return "111819";
    if (code == DicomDcim._111820)
      return "111820";
    if (code == DicomDcim._111821)
      return "111821";
    if (code == DicomDcim._111822)
      return "111822";
    if (code == DicomDcim._111823)
      return "111823";
    if (code == DicomDcim._111824)
      return "111824";
    if (code == DicomDcim._111825)
      return "111825";
    if (code == DicomDcim._111826)
      return "111826";
    if (code == DicomDcim._111827)
      return "111827";
    if (code == DicomDcim._111828)
      return "111828";
    if (code == DicomDcim._111829)
      return "111829";
    if (code == DicomDcim._111830)
      return "111830";
    if (code == DicomDcim._111831)
      return "111831";
    if (code == DicomDcim._111832)
      return "111832";
    if (code == DicomDcim._111833)
      return "111833";
    if (code == DicomDcim._111834)
      return "111834";
    if (code == DicomDcim._111835)
      return "111835";
    if (code == DicomDcim._111836)
      return "111836";
    if (code == DicomDcim._111837)
      return "111837";
    if (code == DicomDcim._111838)
      return "111838";
    if (code == DicomDcim._111839)
      return "111839";
    if (code == DicomDcim._111840)
      return "111840";
    if (code == DicomDcim._111841)
      return "111841";
    if (code == DicomDcim._111842)
      return "111842";
    if (code == DicomDcim._111843)
      return "111843";
    if (code == DicomDcim._111844)
      return "111844";
    if (code == DicomDcim._111845)
      return "111845";
    if (code == DicomDcim._111846)
      return "111846";
    if (code == DicomDcim._111847)
      return "111847";
    if (code == DicomDcim._111848)
      return "111848";
    if (code == DicomDcim._111849)
      return "111849";
    if (code == DicomDcim._111850)
      return "111850";
    if (code == DicomDcim._111851)
      return "111851";
    if (code == DicomDcim._111852)
      return "111852";
    if (code == DicomDcim._111853)
      return "111853";
    if (code == DicomDcim._111854)
      return "111854";
    if (code == DicomDcim._111855)
      return "111855";
    if (code == DicomDcim._111856)
      return "111856";
    if (code == DicomDcim._111900)
      return "111900";
    if (code == DicomDcim._111901)
      return "111901";
    if (code == DicomDcim._111902)
      return "111902";
    if (code == DicomDcim._111903)
      return "111903";
    if (code == DicomDcim._111904)
      return "111904";
    if (code == DicomDcim._111905)
      return "111905";
    if (code == DicomDcim._111906)
      return "111906";
    if (code == DicomDcim._111907)
      return "111907";
    if (code == DicomDcim._111908)
      return "111908";
    if (code == DicomDcim._111909)
      return "111909";
    if (code == DicomDcim._111910)
      return "111910";
    if (code == DicomDcim._111911)
      return "111911";
    if (code == DicomDcim._111912)
      return "111912";
    if (code == DicomDcim._111913)
      return "111913";
    if (code == DicomDcim._111914)
      return "111914";
    if (code == DicomDcim._111915)
      return "111915";
    if (code == DicomDcim._111916)
      return "111916";
    if (code == DicomDcim._111917)
      return "111917";
    if (code == DicomDcim._111918)
      return "111918";
    if (code == DicomDcim._111919)
      return "111919";
    if (code == DicomDcim._111920)
      return "111920";
    if (code == DicomDcim._111921)
      return "111921";
    if (code == DicomDcim._111922)
      return "111922";
    if (code == DicomDcim._111923)
      return "111923";
    if (code == DicomDcim._111924)
      return "111924";
    if (code == DicomDcim._111925)
      return "111925";
    if (code == DicomDcim._111926)
      return "111926";
    if (code == DicomDcim._111927)
      return "111927";
    if (code == DicomDcim._111928)
      return "111928";
    if (code == DicomDcim._111929)
      return "111929";
    if (code == DicomDcim._111930)
      return "111930";
    if (code == DicomDcim._111931)
      return "111931";
    if (code == DicomDcim._111932)
      return "111932";
    if (code == DicomDcim._111933)
      return "111933";
    if (code == DicomDcim._111934)
      return "111934";
    if (code == DicomDcim._111935)
      return "111935";
    if (code == DicomDcim._111936)
      return "111936";
    if (code == DicomDcim._111937)
      return "111937";
    if (code == DicomDcim._111938)
      return "111938";
    if (code == DicomDcim._111939)
      return "111939";
    if (code == DicomDcim._111940)
      return "111940";
    if (code == DicomDcim._111941)
      return "111941";
    if (code == DicomDcim._111942)
      return "111942";
    if (code == DicomDcim._111943)
      return "111943";
    if (code == DicomDcim._111944)
      return "111944";
    if (code == DicomDcim._111945)
      return "111945";
    if (code == DicomDcim._111946)
      return "111946";
    if (code == DicomDcim._111947)
      return "111947";
    if (code == DicomDcim._112000)
      return "112000";
    if (code == DicomDcim._112001)
      return "112001";
    if (code == DicomDcim._112002)
      return "112002";
    if (code == DicomDcim._112003)
      return "112003";
    if (code == DicomDcim._112004)
      return "112004";
    if (code == DicomDcim._112005)
      return "112005";
    if (code == DicomDcim._112006)
      return "112006";
    if (code == DicomDcim._112007)
      return "112007";
    if (code == DicomDcim._112008)
      return "112008";
    if (code == DicomDcim._112009)
      return "112009";
    if (code == DicomDcim._112010)
      return "112010";
    if (code == DicomDcim._112011)
      return "112011";
    if (code == DicomDcim._112012)
      return "112012";
    if (code == DicomDcim._112013)
      return "112013";
    if (code == DicomDcim._112014)
      return "112014";
    if (code == DicomDcim._112015)
      return "112015";
    if (code == DicomDcim._112016)
      return "112016";
    if (code == DicomDcim._112017)
      return "112017";
    if (code == DicomDcim._112018)
      return "112018";
    if (code == DicomDcim._112019)
      return "112019";
    if (code == DicomDcim._112020)
      return "112020";
    if (code == DicomDcim._112021)
      return "112021";
    if (code == DicomDcim._112022)
      return "112022";
    if (code == DicomDcim._112023)
      return "112023";
    if (code == DicomDcim._112024)
      return "112024";
    if (code == DicomDcim._112025)
      return "112025";
    if (code == DicomDcim._112026)
      return "112026";
    if (code == DicomDcim._112027)
      return "112027";
    if (code == DicomDcim._112028)
      return "112028";
    if (code == DicomDcim._112029)
      return "112029";
    if (code == DicomDcim._112030)
      return "112030";
    if (code == DicomDcim._112031)
      return "112031";
    if (code == DicomDcim._112032)
      return "112032";
    if (code == DicomDcim._112033)
      return "112033";
    if (code == DicomDcim._112034)
      return "112034";
    if (code == DicomDcim._112035)
      return "112035";
    if (code == DicomDcim._112036)
      return "112036";
    if (code == DicomDcim._112037)
      return "112037";
    if (code == DicomDcim._112038)
      return "112038";
    if (code == DicomDcim._112039)
      return "112039";
    if (code == DicomDcim._112040)
      return "112040";
    if (code == DicomDcim._112041)
      return "112041";
    if (code == DicomDcim._112042)
      return "112042";
    if (code == DicomDcim._112043)
      return "112043";
    if (code == DicomDcim._112044)
      return "112044";
    if (code == DicomDcim._112045)
      return "112045";
    if (code == DicomDcim._112046)
      return "112046";
    if (code == DicomDcim._112047)
      return "112047";
    if (code == DicomDcim._112048)
      return "112048";
    if (code == DicomDcim._112049)
      return "112049";
    if (code == DicomDcim._112050)
      return "112050";
    if (code == DicomDcim._112051)
      return "112051";
    if (code == DicomDcim._112052)
      return "112052";
    if (code == DicomDcim._112053)
      return "112053";
    if (code == DicomDcim._112054)
      return "112054";
    if (code == DicomDcim._112055)
      return "112055";
    if (code == DicomDcim._112056)
      return "112056";
    if (code == DicomDcim._112057)
      return "112057";
    if (code == DicomDcim._112058)
      return "112058";
    if (code == DicomDcim._112059)
      return "112059";
    if (code == DicomDcim._112060)
      return "112060";
    if (code == DicomDcim._112061)
      return "112061";
    if (code == DicomDcim._112062)
      return "112062";
    if (code == DicomDcim._112063)
      return "112063";
    if (code == DicomDcim._112064)
      return "112064";
    if (code == DicomDcim._112065)
      return "112065";
    if (code == DicomDcim._112066)
      return "112066";
    if (code == DicomDcim._112067)
      return "112067";
    if (code == DicomDcim._112068)
      return "112068";
    if (code == DicomDcim._112069)
      return "112069";
    if (code == DicomDcim._112070)
      return "112070";
    if (code == DicomDcim._112071)
      return "112071";
    if (code == DicomDcim._112072)
      return "112072";
    if (code == DicomDcim._112073)
      return "112073";
    if (code == DicomDcim._112074)
      return "112074";
    if (code == DicomDcim._112075)
      return "112075";
    if (code == DicomDcim._112076)
      return "112076";
    if (code == DicomDcim._112077)
      return "112077";
    if (code == DicomDcim._112078)
      return "112078";
    if (code == DicomDcim._112079)
      return "112079";
    if (code == DicomDcim._112080)
      return "112080";
    if (code == DicomDcim._112081)
      return "112081";
    if (code == DicomDcim._112082)
      return "112082";
    if (code == DicomDcim._112083)
      return "112083";
    if (code == DicomDcim._112084)
      return "112084";
    if (code == DicomDcim._112085)
      return "112085";
    if (code == DicomDcim._112086)
      return "112086";
    if (code == DicomDcim._112087)
      return "112087";
    if (code == DicomDcim._112088)
      return "112088";
    if (code == DicomDcim._112089)
      return "112089";
    if (code == DicomDcim._112090)
      return "112090";
    if (code == DicomDcim._112091)
      return "112091";
    if (code == DicomDcim._112092)
      return "112092";
    if (code == DicomDcim._112093)
      return "112093";
    if (code == DicomDcim._112094)
      return "112094";
    if (code == DicomDcim._112095)
      return "112095";
    if (code == DicomDcim._112096)
      return "112096";
    if (code == DicomDcim._112097)
      return "112097";
    if (code == DicomDcim._112098)
      return "112098";
    if (code == DicomDcim._112099)
      return "112099";
    if (code == DicomDcim._112100)
      return "112100";
    if (code == DicomDcim._112101)
      return "112101";
    if (code == DicomDcim._112102)
      return "112102";
    if (code == DicomDcim._112103)
      return "112103";
    if (code == DicomDcim._112104)
      return "112104";
    if (code == DicomDcim._112105)
      return "112105";
    if (code == DicomDcim._112106)
      return "112106";
    if (code == DicomDcim._112107)
      return "112107";
    if (code == DicomDcim._112108)
      return "112108";
    if (code == DicomDcim._112109)
      return "112109";
    if (code == DicomDcim._112110)
      return "112110";
    if (code == DicomDcim._112111)
      return "112111";
    if (code == DicomDcim._112112)
      return "112112";
    if (code == DicomDcim._112113)
      return "112113";
    if (code == DicomDcim._112114)
      return "112114";
    if (code == DicomDcim._112115)
      return "112115";
    if (code == DicomDcim._112116)
      return "112116";
    if (code == DicomDcim._112117)
      return "112117";
    if (code == DicomDcim._112118)
      return "112118";
    if (code == DicomDcim._112119)
      return "112119";
    if (code == DicomDcim._112120)
      return "112120";
    if (code == DicomDcim._112121)
      return "112121";
    if (code == DicomDcim._112122)
      return "112122";
    if (code == DicomDcim._112123)
      return "112123";
    if (code == DicomDcim._112124)
      return "112124";
    if (code == DicomDcim._112125)
      return "112125";
    if (code == DicomDcim._112126)
      return "112126";
    if (code == DicomDcim._112127)
      return "112127";
    if (code == DicomDcim._112128)
      return "112128";
    if (code == DicomDcim._112129)
      return "112129";
    if (code == DicomDcim._112130)
      return "112130";
    if (code == DicomDcim._112131)
      return "112131";
    if (code == DicomDcim._112132)
      return "112132";
    if (code == DicomDcim._112133)
      return "112133";
    if (code == DicomDcim._112134)
      return "112134";
    if (code == DicomDcim._112135)
      return "112135";
    if (code == DicomDcim._112136)
      return "112136";
    if (code == DicomDcim._112137)
      return "112137";
    if (code == DicomDcim._112138)
      return "112138";
    if (code == DicomDcim._112139)
      return "112139";
    if (code == DicomDcim._112140)
      return "112140";
    if (code == DicomDcim._112141)
      return "112141";
    if (code == DicomDcim._112142)
      return "112142";
    if (code == DicomDcim._112143)
      return "112143";
    if (code == DicomDcim._112144)
      return "112144";
    if (code == DicomDcim._112145)
      return "112145";
    if (code == DicomDcim._112146)
      return "112146";
    if (code == DicomDcim._112147)
      return "112147";
    if (code == DicomDcim._112148)
      return "112148";
    if (code == DicomDcim._112149)
      return "112149";
    if (code == DicomDcim._112150)
      return "112150";
    if (code == DicomDcim._112151)
      return "112151";
    if (code == DicomDcim._112152)
      return "112152";
    if (code == DicomDcim._112153)
      return "112153";
    if (code == DicomDcim._112154)
      return "112154";
    if (code == DicomDcim._112155)
      return "112155";
    if (code == DicomDcim._112156)
      return "112156";
    if (code == DicomDcim._112157)
      return "112157";
    if (code == DicomDcim._112158)
      return "112158";
    if (code == DicomDcim._112159)
      return "112159";
    if (code == DicomDcim._112160)
      return "112160";
    if (code == DicomDcim._112161)
      return "112161";
    if (code == DicomDcim._112162)
      return "112162";
    if (code == DicomDcim._112163)
      return "112163";
    if (code == DicomDcim._112164)
      return "112164";
    if (code == DicomDcim._112165)
      return "112165";
    if (code == DicomDcim._112166)
      return "112166";
    if (code == DicomDcim._112167)
      return "112167";
    if (code == DicomDcim._112168)
      return "112168";
    if (code == DicomDcim._112169)
      return "112169";
    if (code == DicomDcim._112170)
      return "112170";
    if (code == DicomDcim._112171)
      return "112171";
    if (code == DicomDcim._112172)
      return "112172";
    if (code == DicomDcim._112173)
      return "112173";
    if (code == DicomDcim._112174)
      return "112174";
    if (code == DicomDcim._112175)
      return "112175";
    if (code == DicomDcim._112176)
      return "112176";
    if (code == DicomDcim._112177)
      return "112177";
    if (code == DicomDcim._112178)
      return "112178";
    if (code == DicomDcim._112179)
      return "112179";
    if (code == DicomDcim._112180)
      return "112180";
    if (code == DicomDcim._112181)
      return "112181";
    if (code == DicomDcim._112182)
      return "112182";
    if (code == DicomDcim._112183)
      return "112183";
    if (code == DicomDcim._112184)
      return "112184";
    if (code == DicomDcim._112185)
      return "112185";
    if (code == DicomDcim._112186)
      return "112186";
    if (code == DicomDcim._112187)
      return "112187";
    if (code == DicomDcim._112188)
      return "112188";
    if (code == DicomDcim._112189)
      return "112189";
    if (code == DicomDcim._112191)
      return "112191";
    if (code == DicomDcim._112192)
      return "112192";
    if (code == DicomDcim._112193)
      return "112193";
    if (code == DicomDcim._112194)
      return "112194";
    if (code == DicomDcim._112195)
      return "112195";
    if (code == DicomDcim._112196)
      return "112196";
    if (code == DicomDcim._112197)
      return "112197";
    if (code == DicomDcim._112198)
      return "112198";
    if (code == DicomDcim._112199)
      return "112199";
    if (code == DicomDcim._112200)
      return "112200";
    if (code == DicomDcim._112201)
      return "112201";
    if (code == DicomDcim._112220)
      return "112220";
    if (code == DicomDcim._112222)
      return "112222";
    if (code == DicomDcim._112224)
      return "112224";
    if (code == DicomDcim._112225)
      return "112225";
    if (code == DicomDcim._112226)
      return "112226";
    if (code == DicomDcim._112227)
      return "112227";
    if (code == DicomDcim._112228)
      return "112228";
    if (code == DicomDcim._112229)
      return "112229";
    if (code == DicomDcim._112232)
      return "112232";
    if (code == DicomDcim._112233)
      return "112233";
    if (code == DicomDcim._112238)
      return "112238";
    if (code == DicomDcim._112240)
      return "112240";
    if (code == DicomDcim._112241)
      return "112241";
    if (code == DicomDcim._112242)
      return "112242";
    if (code == DicomDcim._112243)
      return "112243";
    if (code == DicomDcim._112244)
      return "112244";
    if (code == DicomDcim._112248)
      return "112248";
    if (code == DicomDcim._112249)
      return "112249";
    if (code == DicomDcim._112300)
      return "112300";
    if (code == DicomDcim._112301)
      return "112301";
    if (code == DicomDcim._112302)
      return "112302";
    if (code == DicomDcim._112303)
      return "112303";
    if (code == DicomDcim._112304)
      return "112304";
    if (code == DicomDcim._112305)
      return "112305";
    if (code == DicomDcim._112306)
      return "112306";
    if (code == DicomDcim._112307)
      return "112307";
    if (code == DicomDcim._112308)
      return "112308";
    if (code == DicomDcim._112309)
      return "112309";
    if (code == DicomDcim._112310)
      return "112310";
    if (code == DicomDcim._112311)
      return "112311";
    if (code == DicomDcim._112312)
      return "112312";
    if (code == DicomDcim._112313)
      return "112313";
    if (code == DicomDcim._112314)
      return "112314";
    if (code == DicomDcim._112315)
      return "112315";
    if (code == DicomDcim._112316)
      return "112316";
    if (code == DicomDcim._112317)
      return "112317";
    if (code == DicomDcim._112318)
      return "112318";
    if (code == DicomDcim._112319)
      return "112319";
    if (code == DicomDcim._112320)
      return "112320";
    if (code == DicomDcim._112321)
      return "112321";
    if (code == DicomDcim._112325)
      return "112325";
    if (code == DicomDcim._112340)
      return "112340";
    if (code == DicomDcim._112341)
      return "112341";
    if (code == DicomDcim._112342)
      return "112342";
    if (code == DicomDcim._112343)
      return "112343";
    if (code == DicomDcim._112344)
      return "112344";
    if (code == DicomDcim._112345)
      return "112345";
    if (code == DicomDcim._112346)
      return "112346";
    if (code == DicomDcim._112347)
      return "112347";
    if (code == DicomDcim._112348)
      return "112348";
    if (code == DicomDcim._112350)
      return "112350";
    if (code == DicomDcim._112351)
      return "112351";
    if (code == DicomDcim._112352)
      return "112352";
    if (code == DicomDcim._112353)
      return "112353";
    if (code == DicomDcim._112354)
      return "112354";
    if (code == DicomDcim._112355)
      return "112355";
    if (code == DicomDcim._112356)
      return "112356";
    if (code == DicomDcim._112357)
      return "112357";
    if (code == DicomDcim._112358)
      return "112358";
    if (code == DicomDcim._112359)
      return "112359";
    if (code == DicomDcim._112360)
      return "112360";
    if (code == DicomDcim._112361)
      return "112361";
    if (code == DicomDcim._112362)
      return "112362";
    if (code == DicomDcim._112363)
      return "112363";
    if (code == DicomDcim._112364)
      return "112364";
    if (code == DicomDcim._112365)
      return "112365";
    if (code == DicomDcim._112366)
      return "112366";
    if (code == DicomDcim._112367)
      return "112367";
    if (code == DicomDcim._112368)
      return "112368";
    if (code == DicomDcim._112369)
      return "112369";
    if (code == DicomDcim._112370)
      return "112370";
    if (code == DicomDcim._112371)
      return "112371";
    if (code == DicomDcim._112372)
      return "112372";
    if (code == DicomDcim._112373)
      return "112373";
    if (code == DicomDcim._112374)
      return "112374";
    if (code == DicomDcim._112375)
      return "112375";
    if (code == DicomDcim._112376)
      return "112376";
    if (code == DicomDcim._112377)
      return "112377";
    if (code == DicomDcim._112378)
      return "112378";
    if (code == DicomDcim._112379)
      return "112379";
    if (code == DicomDcim._112380)
      return "112380";
    if (code == DicomDcim._112381)
      return "112381";
    if (code == DicomDcim._112700)
      return "112700";
    if (code == DicomDcim._112701)
      return "112701";
    if (code == DicomDcim._112702)
      return "112702";
    if (code == DicomDcim._112703)
      return "112703";
    if (code == DicomDcim._112704)
      return "112704";
    if (code == DicomDcim._112705)
      return "112705";
    if (code == DicomDcim._112706)
      return "112706";
    if (code == DicomDcim._112707)
      return "112707";
    if (code == DicomDcim._112708)
      return "112708";
    if (code == DicomDcim._112709)
      return "112709";
    if (code == DicomDcim._112710)
      return "112710";
    if (code == DicomDcim._112711)
      return "112711";
    if (code == DicomDcim._112712)
      return "112712";
    if (code == DicomDcim._112713)
      return "112713";
    if (code == DicomDcim._112714)
      return "112714";
    if (code == DicomDcim._112715)
      return "112715";
    if (code == DicomDcim._112716)
      return "112716";
    if (code == DicomDcim._112717)
      return "112717";
    if (code == DicomDcim._112718)
      return "112718";
    if (code == DicomDcim._112719)
      return "112719";
    if (code == DicomDcim._112720)
      return "112720";
    if (code == DicomDcim._112721)
      return "112721";
    if (code == DicomDcim._113000)
      return "113000";
    if (code == DicomDcim._113001)
      return "113001";
    if (code == DicomDcim._113002)
      return "113002";
    if (code == DicomDcim._113003)
      return "113003";
    if (code == DicomDcim._113004)
      return "113004";
    if (code == DicomDcim._113005)
      return "113005";
    if (code == DicomDcim._113006)
      return "113006";
    if (code == DicomDcim._113007)
      return "113007";
    if (code == DicomDcim._113008)
      return "113008";
    if (code == DicomDcim._113009)
      return "113009";
    if (code == DicomDcim._113010)
      return "113010";
    if (code == DicomDcim._113011)
      return "113011";
    if (code == DicomDcim._113012)
      return "113012";
    if (code == DicomDcim._113013)
      return "113013";
    if (code == DicomDcim._113014)
      return "113014";
    if (code == DicomDcim._113015)
      return "113015";
    if (code == DicomDcim._113016)
      return "113016";
    if (code == DicomDcim._113017)
      return "113017";
    if (code == DicomDcim._113018)
      return "113018";
    if (code == DicomDcim._113020)
      return "113020";
    if (code == DicomDcim._113021)
      return "113021";
    if (code == DicomDcim._113026)
      return "113026";
    if (code == DicomDcim._113030)
      return "113030";
    if (code == DicomDcim._113031)
      return "113031";
    if (code == DicomDcim._113032)
      return "113032";
    if (code == DicomDcim._113033)
      return "113033";
    if (code == DicomDcim._113034)
      return "113034";
    if (code == DicomDcim._113035)
      return "113035";
    if (code == DicomDcim._113036)
      return "113036";
    if (code == DicomDcim._113037)
      return "113037";
    if (code == DicomDcim._113038)
      return "113038";
    if (code == DicomDcim._113039)
      return "113039";
    if (code == DicomDcim._113040)
      return "113040";
    if (code == DicomDcim._113041)
      return "113041";
    if (code == DicomDcim._113042)
      return "113042";
    if (code == DicomDcim._113043)
      return "113043";
    if (code == DicomDcim._113044)
      return "113044";
    if (code == DicomDcim._113045)
      return "113045";
    if (code == DicomDcim._113046)
      return "113046";
    if (code == DicomDcim._113047)
      return "113047";
    if (code == DicomDcim._113048)
      return "113048";
    if (code == DicomDcim._113049)
      return "113049";
    if (code == DicomDcim._113050)
      return "113050";
    if (code == DicomDcim._113051)
      return "113051";
    if (code == DicomDcim._113052)
      return "113052";
    if (code == DicomDcim._113053)
      return "113053";
    if (code == DicomDcim._113054)
      return "113054";
    if (code == DicomDcim._113055)
      return "113055";
    if (code == DicomDcim._113056)
      return "113056";
    if (code == DicomDcim._113057)
      return "113057";
    if (code == DicomDcim._113058)
      return "113058";
    if (code == DicomDcim._113059)
      return "113059";
    if (code == DicomDcim._113060)
      return "113060";
    if (code == DicomDcim._113061)
      return "113061";
    if (code == DicomDcim._113062)
      return "113062";
    if (code == DicomDcim._113063)
      return "113063";
    if (code == DicomDcim._113064)
      return "113064";
    if (code == DicomDcim._113065)
      return "113065";
    if (code == DicomDcim._113066)
      return "113066";
    if (code == DicomDcim._113067)
      return "113067";
    if (code == DicomDcim._113068)
      return "113068";
    if (code == DicomDcim._113069)
      return "113069";
    if (code == DicomDcim._113070)
      return "113070";
    if (code == DicomDcim._113071)
      return "113071";
    if (code == DicomDcim._113072)
      return "113072";
    if (code == DicomDcim._113073)
      return "113073";
    if (code == DicomDcim._113074)
      return "113074";
    if (code == DicomDcim._113075)
      return "113075";
    if (code == DicomDcim._113076)
      return "113076";
    if (code == DicomDcim._113077)
      return "113077";
    if (code == DicomDcim._113078)
      return "113078";
    if (code == DicomDcim._113079)
      return "113079";
    if (code == DicomDcim._113080)
      return "113080";
    if (code == DicomDcim._113081)
      return "113081";
    if (code == DicomDcim._113082)
      return "113082";
    if (code == DicomDcim._113083)
      return "113083";
    if (code == DicomDcim._113085)
      return "113085";
    if (code == DicomDcim._113086)
      return "113086";
    if (code == DicomDcim._113087)
      return "113087";
    if (code == DicomDcim._113088)
      return "113088";
    if (code == DicomDcim._113089)
      return "113089";
    if (code == DicomDcim._113090)
      return "113090";
    if (code == DicomDcim._113091)
      return "113091";
    if (code == DicomDcim._113092)
      return "113092";
    if (code == DicomDcim._113093)
      return "113093";
    if (code == DicomDcim._113094)
      return "113094";
    if (code == DicomDcim._113095)
      return "113095";
    if (code == DicomDcim._113096)
      return "113096";
    if (code == DicomDcim._113097)
      return "113097";
    if (code == DicomDcim._113100)
      return "113100";
    if (code == DicomDcim._113101)
      return "113101";
    if (code == DicomDcim._113102)
      return "113102";
    if (code == DicomDcim._113103)
      return "113103";
    if (code == DicomDcim._113104)
      return "113104";
    if (code == DicomDcim._113105)
      return "113105";
    if (code == DicomDcim._113106)
      return "113106";
    if (code == DicomDcim._113107)
      return "113107";
    if (code == DicomDcim._113108)
      return "113108";
    if (code == DicomDcim._113109)
      return "113109";
    if (code == DicomDcim._113110)
      return "113110";
    if (code == DicomDcim._113111)
      return "113111";
    if (code == DicomDcim._113500)
      return "113500";
    if (code == DicomDcim._113502)
      return "113502";
    if (code == DicomDcim._113503)
      return "113503";
    if (code == DicomDcim._113505)
      return "113505";
    if (code == DicomDcim._113506)
      return "113506";
    if (code == DicomDcim._113507)
      return "113507";
    if (code == DicomDcim._113508)
      return "113508";
    if (code == DicomDcim._113509)
      return "113509";
    if (code == DicomDcim._113510)
      return "113510";
    if (code == DicomDcim._113511)
      return "113511";
    if (code == DicomDcim._113512)
      return "113512";
    if (code == DicomDcim._113513)
      return "113513";
    if (code == DicomDcim._113514)
      return "113514";
    if (code == DicomDcim._113516)
      return "113516";
    if (code == DicomDcim._113517)
      return "113517";
    if (code == DicomDcim._113518)
      return "113518";
    if (code == DicomDcim._113520)
      return "113520";
    if (code == DicomDcim._113521)
      return "113521";
    if (code == DicomDcim._113522)
      return "113522";
    if (code == DicomDcim._113523)
      return "113523";
    if (code == DicomDcim._113526)
      return "113526";
    if (code == DicomDcim._113527)
      return "113527";
    if (code == DicomDcim._113528)
      return "113528";
    if (code == DicomDcim._113529)
      return "113529";
    if (code == DicomDcim._113530)
      return "113530";
    if (code == DicomDcim._113540)
      return "113540";
    if (code == DicomDcim._113541)
      return "113541";
    if (code == DicomDcim._113542)
      return "113542";
    if (code == DicomDcim._113543)
      return "113543";
    if (code == DicomDcim._113550)
      return "113550";
    if (code == DicomDcim._113551)
      return "113551";
    if (code == DicomDcim._113552)
      return "113552";
    if (code == DicomDcim._113560)
      return "113560";
    if (code == DicomDcim._113561)
      return "113561";
    if (code == DicomDcim._113562)
      return "113562";
    if (code == DicomDcim._113563)
      return "113563";
    if (code == DicomDcim._113568)
      return "113568";
    if (code == DicomDcim._113570)
      return "113570";
    if (code == DicomDcim._113571)
      return "113571";
    if (code == DicomDcim._113572)
      return "113572";
    if (code == DicomDcim._113573)
      return "113573";
    if (code == DicomDcim._113574)
      return "113574";
    if (code == DicomDcim._113575)
      return "113575";
    if (code == DicomDcim._113576)
      return "113576";
    if (code == DicomDcim._113577)
      return "113577";
    if (code == DicomDcim._113601)
      return "113601";
    if (code == DicomDcim._113602)
      return "113602";
    if (code == DicomDcim._113603)
      return "113603";
    if (code == DicomDcim._113605)
      return "113605";
    if (code == DicomDcim._113606)
      return "113606";
    if (code == DicomDcim._113607)
      return "113607";
    if (code == DicomDcim._113608)
      return "113608";
    if (code == DicomDcim._113609)
      return "113609";
    if (code == DicomDcim._113611)
      return "113611";
    if (code == DicomDcim._113612)
      return "113612";
    if (code == DicomDcim._113613)
      return "113613";
    if (code == DicomDcim._113620)
      return "113620";
    if (code == DicomDcim._113621)
      return "113621";
    if (code == DicomDcim._113622)
      return "113622";
    if (code == DicomDcim._113630)
      return "113630";
    if (code == DicomDcim._113631)
      return "113631";
    if (code == DicomDcim._113650)
      return "113650";
    if (code == DicomDcim._113651)
      return "113651";
    if (code == DicomDcim._113652)
      return "113652";
    if (code == DicomDcim._113653)
      return "113653";
    if (code == DicomDcim._113661)
      return "113661";
    if (code == DicomDcim._113662)
      return "113662";
    if (code == DicomDcim._113663)
      return "113663";
    if (code == DicomDcim._113664)
      return "113664";
    if (code == DicomDcim._113665)
      return "113665";
    if (code == DicomDcim._113666)
      return "113666";
    if (code == DicomDcim._113669)
      return "113669";
    if (code == DicomDcim._113670)
      return "113670";
    if (code == DicomDcim._113671)
      return "113671";
    if (code == DicomDcim._113680)
      return "113680";
    if (code == DicomDcim._113681)
      return "113681";
    if (code == DicomDcim._113682)
      return "113682";
    if (code == DicomDcim._113683)
      return "113683";
    if (code == DicomDcim._113684)
      return "113684";
    if (code == DicomDcim._113685)
      return "113685";
    if (code == DicomDcim._113686)
      return "113686";
    if (code == DicomDcim._113687)
      return "113687";
    if (code == DicomDcim._113688)
      return "113688";
    if (code == DicomDcim._113689)
      return "113689";
    if (code == DicomDcim._113690)
      return "113690";
    if (code == DicomDcim._113691)
      return "113691";
    if (code == DicomDcim._113692)
      return "113692";
    if (code == DicomDcim._113701)
      return "113701";
    if (code == DicomDcim._113702)
      return "113702";
    if (code == DicomDcim._113704)
      return "113704";
    if (code == DicomDcim._113705)
      return "113705";
    if (code == DicomDcim._113706)
      return "113706";
    if (code == DicomDcim._113710)
      return "113710";
    if (code == DicomDcim._113711)
      return "113711";
    if (code == DicomDcim._113720)
      return "113720";
    if (code == DicomDcim._113721)
      return "113721";
    if (code == DicomDcim._113722)
      return "113722";
    if (code == DicomDcim._113723)
      return "113723";
    if (code == DicomDcim._113724)
      return "113724";
    if (code == DicomDcim._113725)
      return "113725";
    if (code == DicomDcim._113726)
      return "113726";
    if (code == DicomDcim._113727)
      return "113727";
    if (code == DicomDcim._113728)
      return "113728";
    if (code == DicomDcim._113729)
      return "113729";
    if (code == DicomDcim._113730)
      return "113730";
    if (code == DicomDcim._113731)
      return "113731";
    if (code == DicomDcim._113732)
      return "113732";
    if (code == DicomDcim._113733)
      return "113733";
    if (code == DicomDcim._113734)
      return "113734";
    if (code == DicomDcim._113735)
      return "113735";
    if (code == DicomDcim._113736)
      return "113736";
    if (code == DicomDcim._113737)
      return "113737";
    if (code == DicomDcim._113738)
      return "113738";
    if (code == DicomDcim._113739)
      return "113739";
    if (code == DicomDcim._113740)
      return "113740";
    if (code == DicomDcim._113742)
      return "113742";
    if (code == DicomDcim._113743)
      return "113743";
    if (code == DicomDcim._113744)
      return "113744";
    if (code == DicomDcim._113745)
      return "113745";
    if (code == DicomDcim._113748)
      return "113748";
    if (code == DicomDcim._113750)
      return "113750";
    if (code == DicomDcim._113751)
      return "113751";
    if (code == DicomDcim._113752)
      return "113752";
    if (code == DicomDcim._113753)
      return "113753";
    if (code == DicomDcim._113754)
      return "113754";
    if (code == DicomDcim._113755)
      return "113755";
    if (code == DicomDcim._113756)
      return "113756";
    if (code == DicomDcim._113757)
      return "113757";
    if (code == DicomDcim._113758)
      return "113758";
    if (code == DicomDcim._113759)
      return "113759";
    if (code == DicomDcim._113760)
      return "113760";
    if (code == DicomDcim._113761)
      return "113761";
    if (code == DicomDcim._113763)
      return "113763";
    if (code == DicomDcim._113764)
      return "113764";
    if (code == DicomDcim._113766)
      return "113766";
    if (code == DicomDcim._113767)
      return "113767";
    if (code == DicomDcim._113768)
      return "113768";
    if (code == DicomDcim._113769)
      return "113769";
    if (code == DicomDcim._113770)
      return "113770";
    if (code == DicomDcim._113771)
      return "113771";
    if (code == DicomDcim._113772)
      return "113772";
    if (code == DicomDcim._113773)
      return "113773";
    if (code == DicomDcim._113780)
      return "113780";
    if (code == DicomDcim._113788)
      return "113788";
    if (code == DicomDcim._113789)
      return "113789";
    if (code == DicomDcim._113790)
      return "113790";
    if (code == DicomDcim._113791)
      return "113791";
    if (code == DicomDcim._113792)
      return "113792";
    if (code == DicomDcim._113793)
      return "113793";
    if (code == DicomDcim._113794)
      return "113794";
    if (code == DicomDcim._113795)
      return "113795";
    if (code == DicomDcim._113800)
      return "113800";
    if (code == DicomDcim._113801)
      return "113801";
    if (code == DicomDcim._113802)
      return "113802";
    if (code == DicomDcim._113803)
      return "113803";
    if (code == DicomDcim._113804)
      return "113804";
    if (code == DicomDcim._113805)
      return "113805";
    if (code == DicomDcim._113806)
      return "113806";
    if (code == DicomDcim._113807)
      return "113807";
    if (code == DicomDcim._113808)
      return "113808";
    if (code == DicomDcim._113809)
      return "113809";
    if (code == DicomDcim._113810)
      return "113810";
    if (code == DicomDcim._113811)
      return "113811";
    if (code == DicomDcim._113812)
      return "113812";
    if (code == DicomDcim._113813)
      return "113813";
    if (code == DicomDcim._113814)
      return "113814";
    if (code == DicomDcim._113815)
      return "113815";
    if (code == DicomDcim._113816)
      return "113816";
    if (code == DicomDcim._113817)
      return "113817";
    if (code == DicomDcim._113818)
      return "113818";
    if (code == DicomDcim._113819)
      return "113819";
    if (code == DicomDcim._113820)
      return "113820";
    if (code == DicomDcim._113821)
      return "113821";
    if (code == DicomDcim._113822)
      return "113822";
    if (code == DicomDcim._113823)
      return "113823";
    if (code == DicomDcim._113824)
      return "113824";
    if (code == DicomDcim._113825)
      return "113825";
    if (code == DicomDcim._113826)
      return "113826";
    if (code == DicomDcim._113827)
      return "113827";
    if (code == DicomDcim._113828)
      return "113828";
    if (code == DicomDcim._113829)
      return "113829";
    if (code == DicomDcim._113830)
      return "113830";
    if (code == DicomDcim._113831)
      return "113831";
    if (code == DicomDcim._113832)
      return "113832";
    if (code == DicomDcim._113833)
      return "113833";
    if (code == DicomDcim._113834)
      return "113834";
    if (code == DicomDcim._113835)
      return "113835";
    if (code == DicomDcim._113836)
      return "113836";
    if (code == DicomDcim._113837)
      return "113837";
    if (code == DicomDcim._113838)
      return "113838";
    if (code == DicomDcim._113839)
      return "113839";
    if (code == DicomDcim._113840)
      return "113840";
    if (code == DicomDcim._113841)
      return "113841";
    if (code == DicomDcim._113842)
      return "113842";
    if (code == DicomDcim._113845)
      return "113845";
    if (code == DicomDcim._113846)
      return "113846";
    if (code == DicomDcim._113847)
      return "113847";
    if (code == DicomDcim._113850)
      return "113850";
    if (code == DicomDcim._113851)
      return "113851";
    if (code == DicomDcim._113852)
      return "113852";
    if (code == DicomDcim._113853)
      return "113853";
    if (code == DicomDcim._113854)
      return "113854";
    if (code == DicomDcim._113855)
      return "113855";
    if (code == DicomDcim._113856)
      return "113856";
    if (code == DicomDcim._113857)
      return "113857";
    if (code == DicomDcim._113858)
      return "113858";
    if (code == DicomDcim._113859)
      return "113859";
    if (code == DicomDcim._113860)
      return "113860";
    if (code == DicomDcim._113861)
      return "113861";
    if (code == DicomDcim._113862)
      return "113862";
    if (code == DicomDcim._113863)
      return "113863";
    if (code == DicomDcim._113864)
      return "113864";
    if (code == DicomDcim._113865)
      return "113865";
    if (code == DicomDcim._113866)
      return "113866";
    if (code == DicomDcim._113867)
      return "113867";
    if (code == DicomDcim._113868)
      return "113868";
    if (code == DicomDcim._113870)
      return "113870";
    if (code == DicomDcim._113871)
      return "113871";
    if (code == DicomDcim._113872)
      return "113872";
    if (code == DicomDcim._113873)
      return "113873";
    if (code == DicomDcim._113874)
      return "113874";
    if (code == DicomDcim._113875)
      return "113875";
    if (code == DicomDcim._113876)
      return "113876";
    if (code == DicomDcim._113877)
      return "113877";
    if (code == DicomDcim._113878)
      return "113878";
    if (code == DicomDcim._113879)
      return "113879";
    if (code == DicomDcim._113880)
      return "113880";
    if (code == DicomDcim._113890)
      return "113890";
    if (code == DicomDcim._113893)
      return "113893";
    if (code == DicomDcim._113895)
      return "113895";
    if (code == DicomDcim._113896)
      return "113896";
    if (code == DicomDcim._113897)
      return "113897";
    if (code == DicomDcim._113898)
      return "113898";
    if (code == DicomDcim._113899)
      return "113899";
    if (code == DicomDcim._113900)
      return "113900";
    if (code == DicomDcim._113901)
      return "113901";
    if (code == DicomDcim._113902)
      return "113902";
    if (code == DicomDcim._113903)
      return "113903";
    if (code == DicomDcim._113904)
      return "113904";
    if (code == DicomDcim._113905)
      return "113905";
    if (code == DicomDcim._113906)
      return "113906";
    if (code == DicomDcim._113907)
      return "113907";
    if (code == DicomDcim._113908)
      return "113908";
    if (code == DicomDcim._113909)
      return "113909";
    if (code == DicomDcim._113910)
      return "113910";
    if (code == DicomDcim._113911)
      return "113911";
    if (code == DicomDcim._113912)
      return "113912";
    if (code == DicomDcim._113913)
      return "113913";
    if (code == DicomDcim._113914)
      return "113914";
    if (code == DicomDcim._113921)
      return "113921";
    if (code == DicomDcim._113922)
      return "113922";
    if (code == DicomDcim._113923)
      return "113923";
    if (code == DicomDcim._113930)
      return "113930";
    if (code == DicomDcim._113931)
      return "113931";
    if (code == DicomDcim._113932)
      return "113932";
    if (code == DicomDcim._113933)
      return "113933";
    if (code == DicomDcim._113934)
      return "113934";
    if (code == DicomDcim._113935)
      return "113935";
    if (code == DicomDcim._113936)
      return "113936";
    if (code == DicomDcim._113937)
      return "113937";
    if (code == DicomDcim._113940)
      return "113940";
    if (code == DicomDcim._113941)
      return "113941";
    if (code == DicomDcim._113942)
      return "113942";
    if (code == DicomDcim._113943)
      return "113943";
    if (code == DicomDcim._113944)
      return "113944";
    if (code == DicomDcim._113945)
      return "113945";
    if (code == DicomDcim._113946)
      return "113946";
    if (code == DicomDcim._113947)
      return "113947";
    if (code == DicomDcim._113948)
      return "113948";
    if (code == DicomDcim._113949)
      return "113949";
    if (code == DicomDcim._113950)
      return "113950";
    if (code == DicomDcim._113951)
      return "113951";
    if (code == DicomDcim._113952)
      return "113952";
    if (code == DicomDcim._113953)
      return "113953";
    if (code == DicomDcim._113954)
      return "113954";
    if (code == DicomDcim._113955)
      return "113955";
    if (code == DicomDcim._113956)
      return "113956";
    if (code == DicomDcim._113957)
      return "113957";
    if (code == DicomDcim._113958)
      return "113958";
    if (code == DicomDcim._113959)
      return "113959";
    if (code == DicomDcim._113961)
      return "113961";
    if (code == DicomDcim._113962)
      return "113962";
    if (code == DicomDcim._113963)
      return "113963";
    if (code == DicomDcim._113970)
      return "113970";
    if (code == DicomDcim._114000)
      return "114000";
    if (code == DicomDcim._114001)
      return "114001";
    if (code == DicomDcim._114002)
      return "114002";
    if (code == DicomDcim._114003)
      return "114003";
    if (code == DicomDcim._114004)
      return "114004";
    if (code == DicomDcim._114005)
      return "114005";
    if (code == DicomDcim._114006)
      return "114006";
    if (code == DicomDcim._114007)
      return "114007";
    if (code == DicomDcim._114008)
      return "114008";
    if (code == DicomDcim._114009)
      return "114009";
    if (code == DicomDcim._114010)
      return "114010";
    if (code == DicomDcim._114011)
      return "114011";
    if (code == DicomDcim._114201)
      return "114201";
    if (code == DicomDcim._114202)
      return "114202";
    if (code == DicomDcim._114203)
      return "114203";
    if (code == DicomDcim._114204)
      return "114204";
    if (code == DicomDcim._114205)
      return "114205";
    if (code == DicomDcim._114206)
      return "114206";
    if (code == DicomDcim._114207)
      return "114207";
    if (code == DicomDcim._114208)
      return "114208";
    if (code == DicomDcim._114209)
      return "114209";
    if (code == DicomDcim._114210)
      return "114210";
    if (code == DicomDcim._114211)
      return "114211";
    if (code == DicomDcim._114213)
      return "114213";
    if (code == DicomDcim._114215)
      return "114215";
    if (code == DicomDcim._114216)
      return "114216";
    if (code == DicomDcim._121001)
      return "121001";
    if (code == DicomDcim._121002)
      return "121002";
    if (code == DicomDcim._121003)
      return "121003";
    if (code == DicomDcim._121004)
      return "121004";
    if (code == DicomDcim._121005)
      return "121005";
    if (code == DicomDcim._121006)
      return "121006";
    if (code == DicomDcim._121007)
      return "121007";
    if (code == DicomDcim._121008)
      return "121008";
    if (code == DicomDcim._121009)
      return "121009";
    if (code == DicomDcim._121010)
      return "121010";
    if (code == DicomDcim._121011)
      return "121011";
    if (code == DicomDcim._121012)
      return "121012";
    if (code == DicomDcim._121013)
      return "121013";
    if (code == DicomDcim._121014)
      return "121014";
    if (code == DicomDcim._121015)
      return "121015";
    if (code == DicomDcim._121016)
      return "121016";
    if (code == DicomDcim._121017)
      return "121017";
    if (code == DicomDcim._121018)
      return "121018";
    if (code == DicomDcim._121019)
      return "121019";
    if (code == DicomDcim._121020)
      return "121020";
    if (code == DicomDcim._121021)
      return "121021";
    if (code == DicomDcim._121022)
      return "121022";
    if (code == DicomDcim._121023)
      return "121023";
    if (code == DicomDcim._121024)
      return "121024";
    if (code == DicomDcim._121025)
      return "121025";
    if (code == DicomDcim._121026)
      return "121026";
    if (code == DicomDcim._121027)
      return "121027";
    if (code == DicomDcim._121028)
      return "121028";
    if (code == DicomDcim._121029)
      return "121029";
    if (code == DicomDcim._121030)
      return "121030";
    if (code == DicomDcim._121031)
      return "121031";
    if (code == DicomDcim._121032)
      return "121032";
    if (code == DicomDcim._121033)
      return "121033";
    if (code == DicomDcim._121034)
      return "121034";
    if (code == DicomDcim._121035)
      return "121035";
    if (code == DicomDcim._121036)
      return "121036";
    if (code == DicomDcim._121037)
      return "121037";
    if (code == DicomDcim._121038)
      return "121038";
    if (code == DicomDcim._121039)
      return "121039";
    if (code == DicomDcim._121040)
      return "121040";
    if (code == DicomDcim._121041)
      return "121041";
    if (code == DicomDcim._121042)
      return "121042";
    if (code == DicomDcim._121043)
      return "121043";
    if (code == DicomDcim._121044)
      return "121044";
    if (code == DicomDcim._121045)
      return "121045";
    if (code == DicomDcim._121046)
      return "121046";
    if (code == DicomDcim._121047)
      return "121047";
    if (code == DicomDcim._121048)
      return "121048";
    if (code == DicomDcim._121049)
      return "121049";
    if (code == DicomDcim._121050)
      return "121050";
    if (code == DicomDcim._121051)
      return "121051";
    if (code == DicomDcim._121052)
      return "121052";
    if (code == DicomDcim._121053)
      return "121053";
    if (code == DicomDcim._121054)
      return "121054";
    if (code == DicomDcim._121055)
      return "121055";
    if (code == DicomDcim._121056)
      return "121056";
    if (code == DicomDcim._121057)
      return "121057";
    if (code == DicomDcim._121058)
      return "121058";
    if (code == DicomDcim._121059)
      return "121059";
    if (code == DicomDcim._121060)
      return "121060";
    if (code == DicomDcim._121062)
      return "121062";
    if (code == DicomDcim._121064)
      return "121064";
    if (code == DicomDcim._121065)
      return "121065";
    if (code == DicomDcim._121066)
      return "121066";
    if (code == DicomDcim._121068)
      return "121068";
    if (code == DicomDcim._121069)
      return "121069";
    if (code == DicomDcim._121070)
      return "121070";
    if (code == DicomDcim._121071)
      return "121071";
    if (code == DicomDcim._121072)
      return "121072";
    if (code == DicomDcim._121073)
      return "121073";
    if (code == DicomDcim._121074)
      return "121074";
    if (code == DicomDcim._121075)
      return "121075";
    if (code == DicomDcim._121076)
      return "121076";
    if (code == DicomDcim._121077)
      return "121077";
    if (code == DicomDcim._121078)
      return "121078";
    if (code == DicomDcim._121079)
      return "121079";
    if (code == DicomDcim._121080)
      return "121080";
    if (code == DicomDcim._121081)
      return "121081";
    if (code == DicomDcim._121082)
      return "121082";
    if (code == DicomDcim._121083)
      return "121083";
    if (code == DicomDcim._121084)
      return "121084";
    if (code == DicomDcim._121085)
      return "121085";
    if (code == DicomDcim._121086)
      return "121086";
    if (code == DicomDcim._121087)
      return "121087";
    if (code == DicomDcim._121088)
      return "121088";
    if (code == DicomDcim._121089)
      return "121089";
    if (code == DicomDcim._121090)
      return "121090";
    if (code == DicomDcim._121091)
      return "121091";
    if (code == DicomDcim._121092)
      return "121092";
    if (code == DicomDcim._121093)
      return "121093";
    if (code == DicomDcim._121094)
      return "121094";
    if (code == DicomDcim._121095)
      return "121095";
    if (code == DicomDcim._121096)
      return "121096";
    if (code == DicomDcim._121097)
      return "121097";
    if (code == DicomDcim._121098)
      return "121098";
    if (code == DicomDcim._121099)
      return "121099";
    if (code == DicomDcim._121100)
      return "121100";
    if (code == DicomDcim._121101)
      return "121101";
    if (code == DicomDcim._121102)
      return "121102";
    if (code == DicomDcim._121103)
      return "121103";
    if (code == DicomDcim._121104)
      return "121104";
    if (code == DicomDcim._121105)
      return "121105";
    if (code == DicomDcim._121106)
      return "121106";
    if (code == DicomDcim._121109)
      return "121109";
    if (code == DicomDcim._121110)
      return "121110";
    if (code == DicomDcim._121111)
      return "121111";
    if (code == DicomDcim._121112)
      return "121112";
    if (code == DicomDcim._121113)
      return "121113";
    if (code == DicomDcim._121114)
      return "121114";
    if (code == DicomDcim._121115)
      return "121115";
    if (code == DicomDcim._121116)
      return "121116";
    if (code == DicomDcim._121117)
      return "121117";
    if (code == DicomDcim._121118)
      return "121118";
    if (code == DicomDcim._121120)
      return "121120";
    if (code == DicomDcim._121121)
      return "121121";
    if (code == DicomDcim._121122)
      return "121122";
    if (code == DicomDcim._121123)
      return "121123";
    if (code == DicomDcim._121124)
      return "121124";
    if (code == DicomDcim._121125)
      return "121125";
    if (code == DicomDcim._121126)
      return "121126";
    if (code == DicomDcim._121127)
      return "121127";
    if (code == DicomDcim._121128)
      return "121128";
    if (code == DicomDcim._121130)
      return "121130";
    if (code == DicomDcim._121131)
      return "121131";
    if (code == DicomDcim._121132)
      return "121132";
    if (code == DicomDcim._121133)
      return "121133";
    if (code == DicomDcim._121135)
      return "121135";
    if (code == DicomDcim._121136)
      return "121136";
    if (code == DicomDcim._121137)
      return "121137";
    if (code == DicomDcim._121138)
      return "121138";
    if (code == DicomDcim._121139)
      return "121139";
    if (code == DicomDcim._121140)
      return "121140";
    if (code == DicomDcim._121141)
      return "121141";
    if (code == DicomDcim._121142)
      return "121142";
    if (code == DicomDcim._121143)
      return "121143";
    if (code == DicomDcim._121144)
      return "121144";
    if (code == DicomDcim._121145)
      return "121145";
    if (code == DicomDcim._121146)
      return "121146";
    if (code == DicomDcim._121147)
      return "121147";
    if (code == DicomDcim._121148)
      return "121148";
    if (code == DicomDcim._121149)
      return "121149";
    if (code == DicomDcim._121150)
      return "121150";
    if (code == DicomDcim._121151)
      return "121151";
    if (code == DicomDcim._121152)
      return "121152";
    if (code == DicomDcim._121153)
      return "121153";
    if (code == DicomDcim._121154)
      return "121154";
    if (code == DicomDcim._121155)
      return "121155";
    if (code == DicomDcim._121156)
      return "121156";
    if (code == DicomDcim._121157)
      return "121157";
    if (code == DicomDcim._121158)
      return "121158";
    if (code == DicomDcim._121160)
      return "121160";
    if (code == DicomDcim._121161)
      return "121161";
    if (code == DicomDcim._121162)
      return "121162";
    if (code == DicomDcim._121163)
      return "121163";
    if (code == DicomDcim._121165)
      return "121165";
    if (code == DicomDcim._121166)
      return "121166";
    if (code == DicomDcim._121167)
      return "121167";
    if (code == DicomDcim._121168)
      return "121168";
    if (code == DicomDcim._121169)
      return "121169";
    if (code == DicomDcim._121171)
      return "121171";
    if (code == DicomDcim._121172)
      return "121172";
    if (code == DicomDcim._121173)
      return "121173";
    if (code == DicomDcim._121174)
      return "121174";
    if (code == DicomDcim._121180)
      return "121180";
    if (code == DicomDcim._121181)
      return "121181";
    if (code == DicomDcim._121190)
      return "121190";
    if (code == DicomDcim._121191)
      return "121191";
    if (code == DicomDcim._121192)
      return "121192";
    if (code == DicomDcim._121193)
      return "121193";
    if (code == DicomDcim._121194)
      return "121194";
    if (code == DicomDcim._121195)
      return "121195";
    if (code == DicomDcim._121196)
      return "121196";
    if (code == DicomDcim._121197)
      return "121197";
    if (code == DicomDcim._121198)
      return "121198";
    if (code == DicomDcim._121200)
      return "121200";
    if (code == DicomDcim._121201)
      return "121201";
    if (code == DicomDcim._121202)
      return "121202";
    if (code == DicomDcim._121206)
      return "121206";
    if (code == DicomDcim._121207)
      return "121207";
    if (code == DicomDcim._121208)
      return "121208";
    if (code == DicomDcim._121210)
      return "121210";
    if (code == DicomDcim._121211)
      return "121211";
    if (code == DicomDcim._121213)
      return "121213";
    if (code == DicomDcim._121214)
      return "121214";
    if (code == DicomDcim._121216)
      return "121216";
    if (code == DicomDcim._121217)
      return "121217";
    if (code == DicomDcim._121218)
      return "121218";
    if (code == DicomDcim._121219)
      return "121219";
    if (code == DicomDcim._121220)
      return "121220";
    if (code == DicomDcim._121221)
      return "121221";
    if (code == DicomDcim._121222)
      return "121222";
    if (code == DicomDcim._121230)
      return "121230";
    if (code == DicomDcim._121231)
      return "121231";
    if (code == DicomDcim._121232)
      return "121232";
    if (code == DicomDcim._121233)
      return "121233";
    if (code == DicomDcim._121242)
      return "121242";
    if (code == DicomDcim._121243)
      return "121243";
    if (code == DicomDcim._121244)
      return "121244";
    if (code == DicomDcim._121290)
      return "121290";
    if (code == DicomDcim._121291)
      return "121291";
    if (code == DicomDcim._121301)
      return "121301";
    if (code == DicomDcim._121302)
      return "121302";
    if (code == DicomDcim._121303)
      return "121303";
    if (code == DicomDcim._121304)
      return "121304";
    if (code == DicomDcim._121305)
      return "121305";
    if (code == DicomDcim._121306)
      return "121306";
    if (code == DicomDcim._121307)
      return "121307";
    if (code == DicomDcim._121311)
      return "121311";
    if (code == DicomDcim._121312)
      return "121312";
    if (code == DicomDcim._121313)
      return "121313";
    if (code == DicomDcim._121314)
      return "121314";
    if (code == DicomDcim._121315)
      return "121315";
    if (code == DicomDcim._121316)
      return "121316";
    if (code == DicomDcim._121317)
      return "121317";
    if (code == DicomDcim._121318)
      return "121318";
    if (code == DicomDcim._121320)
      return "121320";
    if (code == DicomDcim._121321)
      return "121321";
    if (code == DicomDcim._121322)
      return "121322";
    if (code == DicomDcim._121323)
      return "121323";
    if (code == DicomDcim._121324)
      return "121324";
    if (code == DicomDcim._121325)
      return "121325";
    if (code == DicomDcim._121326)
      return "121326";
    if (code == DicomDcim._121327)
      return "121327";
    if (code == DicomDcim._121328)
      return "121328";
    if (code == DicomDcim._121329)
      return "121329";
    if (code == DicomDcim._121330)
      return "121330";
    if (code == DicomDcim._121331)
      return "121331";
    if (code == DicomDcim._121332)
      return "121332";
    if (code == DicomDcim._121333)
      return "121333";
    if (code == DicomDcim._121334)
      return "121334";
    if (code == DicomDcim._121335)
      return "121335";
    if (code == DicomDcim._121338)
      return "121338";
    if (code == DicomDcim._121339)
      return "121339";
    if (code == DicomDcim._121340)
      return "121340";
    if (code == DicomDcim._121341)
      return "121341";
    if (code == DicomDcim._121342)
      return "121342";
    if (code == DicomDcim._121346)
      return "121346";
    if (code == DicomDcim._121347)
      return "121347";
    if (code == DicomDcim._121348)
      return "121348";
    if (code == DicomDcim._121349)
      return "121349";
    if (code == DicomDcim._121350)
      return "121350";
    if (code == DicomDcim._121351)
      return "121351";
    if (code == DicomDcim._121352)
      return "121352";
    if (code == DicomDcim._121353)
      return "121353";
    if (code == DicomDcim._121354)
      return "121354";
    if (code == DicomDcim._121358)
      return "121358";
    if (code == DicomDcim._121360)
      return "121360";
    if (code == DicomDcim._121361)
      return "121361";
    if (code == DicomDcim._121362)
      return "121362";
    if (code == DicomDcim._121363)
      return "121363";
    if (code == DicomDcim._121370)
      return "121370";
    if (code == DicomDcim._121371)
      return "121371";
    if (code == DicomDcim._121372)
      return "121372";
    if (code == DicomDcim._121380)
      return "121380";
    if (code == DicomDcim._121381)
      return "121381";
    if (code == DicomDcim._121382)
      return "121382";
    if (code == DicomDcim._121383)
      return "121383";
    if (code == DicomDcim._121401)
      return "121401";
    if (code == DicomDcim._121402)
      return "121402";
    if (code == DicomDcim._121403)
      return "121403";
    if (code == DicomDcim._121404)
      return "121404";
    if (code == DicomDcim._121405)
      return "121405";
    if (code == DicomDcim._121406)
      return "121406";
    if (code == DicomDcim._121407)
      return "121407";
    if (code == DicomDcim._121408)
      return "121408";
    if (code == DicomDcim._121410)
      return "121410";
    if (code == DicomDcim._121411)
      return "121411";
    if (code == DicomDcim._121412)
      return "121412";
    if (code == DicomDcim._121414)
      return "121414";
    if (code == DicomDcim._121415)
      return "121415";
    if (code == DicomDcim._121416)
      return "121416";
    if (code == DicomDcim._121417)
      return "121417";
    if (code == DicomDcim._121420)
      return "121420";
    if (code == DicomDcim._121421)
      return "121421";
    if (code == DicomDcim._121422)
      return "121422";
    if (code == DicomDcim._121423)
      return "121423";
    if (code == DicomDcim._121424)
      return "121424";
    if (code == DicomDcim._121425)
      return "121425";
    if (code == DicomDcim._121427)
      return "121427";
    if (code == DicomDcim._121428)
      return "121428";
    if (code == DicomDcim._121430)
      return "121430";
    if (code == DicomDcim._121431)
      return "121431";
    if (code == DicomDcim._121432)
      return "121432";
    if (code == DicomDcim._121433)
      return "121433";
    if (code == DicomDcim._121434)
      return "121434";
    if (code == DicomDcim._121435)
      return "121435";
    if (code == DicomDcim._121436)
      return "121436";
    if (code == DicomDcim._121437)
      return "121437";
    if (code == DicomDcim._121438)
      return "121438";
    if (code == DicomDcim._121439)
      return "121439";
    if (code == DicomDcim._121701)
      return "121701";
    if (code == DicomDcim._121702)
      return "121702";
    if (code == DicomDcim._121703)
      return "121703";
    if (code == DicomDcim._121704)
      return "121704";
    if (code == DicomDcim._121705)
      return "121705";
    if (code == DicomDcim._121706)
      return "121706";
    if (code == DicomDcim._121707)
      return "121707";
    if (code == DicomDcim._121708)
      return "121708";
    if (code == DicomDcim._121709)
      return "121709";
    if (code == DicomDcim._121710)
      return "121710";
    if (code == DicomDcim._121711)
      return "121711";
    if (code == DicomDcim._121712)
      return "121712";
    if (code == DicomDcim._121713)
      return "121713";
    if (code == DicomDcim._121714)
      return "121714";
    if (code == DicomDcim._121715)
      return "121715";
    if (code == DicomDcim._121716)
      return "121716";
    if (code == DicomDcim._121717)
      return "121717";
    if (code == DicomDcim._121718)
      return "121718";
    if (code == DicomDcim._121719)
      return "121719";
    if (code == DicomDcim._121720)
      return "121720";
    if (code == DicomDcim._121721)
      return "121721";
    if (code == DicomDcim._121722)
      return "121722";
    if (code == DicomDcim._121723)
      return "121723";
    if (code == DicomDcim._121724)
      return "121724";
    if (code == DicomDcim._121725)
      return "121725";
    if (code == DicomDcim._121726)
      return "121726";
    if (code == DicomDcim._121727)
      return "121727";
    if (code == DicomDcim._121728)
      return "121728";
    if (code == DicomDcim._121729)
      return "121729";
    if (code == DicomDcim._121730)
      return "121730";
    if (code == DicomDcim._121731)
      return "121731";
    if (code == DicomDcim._121732)
      return "121732";
    if (code == DicomDcim._121733)
      return "121733";
    if (code == DicomDcim._121734)
      return "121734";
    if (code == DicomDcim._121740)
      return "121740";
    if (code == DicomDcim._122001)
      return "122001";
    if (code == DicomDcim._122002)
      return "122002";
    if (code == DicomDcim._122003)
      return "122003";
    if (code == DicomDcim._122004)
      return "122004";
    if (code == DicomDcim._122005)
      return "122005";
    if (code == DicomDcim._122006)
      return "122006";
    if (code == DicomDcim._122007)
      return "122007";
    if (code == DicomDcim._122008)
      return "122008";
    if (code == DicomDcim._122009)
      return "122009";
    if (code == DicomDcim._122010)
      return "122010";
    if (code == DicomDcim._122011)
      return "122011";
    if (code == DicomDcim._122012)
      return "122012";
    if (code == DicomDcim._122020)
      return "122020";
    if (code == DicomDcim._122021)
      return "122021";
    if (code == DicomDcim._122022)
      return "122022";
    if (code == DicomDcim._122023)
      return "122023";
    if (code == DicomDcim._122024)
      return "122024";
    if (code == DicomDcim._122025)
      return "122025";
    if (code == DicomDcim._122026)
      return "122026";
    if (code == DicomDcim._122027)
      return "122027";
    if (code == DicomDcim._122028)
      return "122028";
    if (code == DicomDcim._122029)
      return "122029";
    if (code == DicomDcim._122030)
      return "122030";
    if (code == DicomDcim._122031)
      return "122031";
    if (code == DicomDcim._122032)
      return "122032";
    if (code == DicomDcim._122033)
      return "122033";
    if (code == DicomDcim._122034)
      return "122034";
    if (code == DicomDcim._122035)
      return "122035";
    if (code == DicomDcim._122036)
      return "122036";
    if (code == DicomDcim._122037)
      return "122037";
    if (code == DicomDcim._122038)
      return "122038";
    if (code == DicomDcim._122039)
      return "122039";
    if (code == DicomDcim._122041)
      return "122041";
    if (code == DicomDcim._122042)
      return "122042";
    if (code == DicomDcim._122043)
      return "122043";
    if (code == DicomDcim._122044)
      return "122044";
    if (code == DicomDcim._122045)
      return "122045";
    if (code == DicomDcim._122046)
      return "122046";
    if (code == DicomDcim._122047)
      return "122047";
    if (code == DicomDcim._122048)
      return "122048";
    if (code == DicomDcim._122049)
      return "122049";
    if (code == DicomDcim._122052)
      return "122052";
    if (code == DicomDcim._122053)
      return "122053";
    if (code == DicomDcim._122054)
      return "122054";
    if (code == DicomDcim._122055)
      return "122055";
    if (code == DicomDcim._122056)
      return "122056";
    if (code == DicomDcim._122057)
      return "122057";
    if (code == DicomDcim._122058)
      return "122058";
    if (code == DicomDcim._122059)
      return "122059";
    if (code == DicomDcim._122060)
      return "122060";
    if (code == DicomDcim._122061)
      return "122061";
    if (code == DicomDcim._122062)
      return "122062";
    if (code == DicomDcim._122072)
      return "122072";
    if (code == DicomDcim._122073)
      return "122073";
    if (code == DicomDcim._122075)
      return "122075";
    if (code == DicomDcim._122076)
      return "122076";
    if (code == DicomDcim._122077)
      return "122077";
    if (code == DicomDcim._122078)
      return "122078";
    if (code == DicomDcim._122079)
      return "122079";
    if (code == DicomDcim._122081)
      return "122081";
    if (code == DicomDcim._122082)
      return "122082";
    if (code == DicomDcim._122083)
      return "122083";
    if (code == DicomDcim._122084)
      return "122084";
    if (code == DicomDcim._122085)
      return "122085";
    if (code == DicomDcim._122086)
      return "122086";
    if (code == DicomDcim._122087)
      return "122087";
    if (code == DicomDcim._122088)
      return "122088";
    if (code == DicomDcim._122089)
      return "122089";
    if (code == DicomDcim._122090)
      return "122090";
    if (code == DicomDcim._122091)
      return "122091";
    if (code == DicomDcim._122092)
      return "122092";
    if (code == DicomDcim._122093)
      return "122093";
    if (code == DicomDcim._122094)
      return "122094";
    if (code == DicomDcim._122095)
      return "122095";
    if (code == DicomDcim._122096)
      return "122096";
    if (code == DicomDcim._122097)
      return "122097";
    if (code == DicomDcim._122098)
      return "122098";
    if (code == DicomDcim._122099)
      return "122099";
    if (code == DicomDcim._122101)
      return "122101";
    if (code == DicomDcim._122102)
      return "122102";
    if (code == DicomDcim._122103)
      return "122103";
    if (code == DicomDcim._122104)
      return "122104";
    if (code == DicomDcim._122105)
      return "122105";
    if (code == DicomDcim._122106)
      return "122106";
    if (code == DicomDcim._122107)
      return "122107";
    if (code == DicomDcim._122108)
      return "122108";
    if (code == DicomDcim._122109)
      return "122109";
    if (code == DicomDcim._122110)
      return "122110";
    if (code == DicomDcim._122111)
      return "122111";
    if (code == DicomDcim._122112)
      return "122112";
    if (code == DicomDcim._122113)
      return "122113";
    if (code == DicomDcim._122114)
      return "122114";
    if (code == DicomDcim._122120)
      return "122120";
    if (code == DicomDcim._122121)
      return "122121";
    if (code == DicomDcim._122122)
      return "122122";
    if (code == DicomDcim._122123)
      return "122123";
    if (code == DicomDcim._122124)
      return "122124";
    if (code == DicomDcim._122125)
      return "122125";
    if (code == DicomDcim._122126)
      return "122126";
    if (code == DicomDcim._122127)
      return "122127";
    if (code == DicomDcim._122128)
      return "122128";
    if (code == DicomDcim._122129)
      return "122129";
    if (code == DicomDcim._122130)
      return "122130";
    if (code == DicomDcim._122131)
      return "122131";
    if (code == DicomDcim._122132)
      return "122132";
    if (code == DicomDcim._122133)
      return "122133";
    if (code == DicomDcim._122134)
      return "122134";
    if (code == DicomDcim._122138)
      return "122138";
    if (code == DicomDcim._122139)
      return "122139";
    if (code == DicomDcim._122140)
      return "122140";
    if (code == DicomDcim._122141)
      return "122141";
    if (code == DicomDcim._122142)
      return "122142";
    if (code == DicomDcim._122143)
      return "122143";
    if (code == DicomDcim._122144)
      return "122144";
    if (code == DicomDcim._122145)
      return "122145";
    if (code == DicomDcim._122146)
      return "122146";
    if (code == DicomDcim._122147)
      return "122147";
    if (code == DicomDcim._122148)
      return "122148";
    if (code == DicomDcim._122149)
      return "122149";
    if (code == DicomDcim._122150)
      return "122150";
    if (code == DicomDcim._122151)
      return "122151";
    if (code == DicomDcim._122152)
      return "122152";
    if (code == DicomDcim._122153)
      return "122153";
    if (code == DicomDcim._122154)
      return "122154";
    if (code == DicomDcim._122157)
      return "122157";
    if (code == DicomDcim._122158)
      return "122158";
    if (code == DicomDcim._122159)
      return "122159";
    if (code == DicomDcim._122160)
      return "122160";
    if (code == DicomDcim._122161)
      return "122161";
    if (code == DicomDcim._122162)
      return "122162";
    if (code == DicomDcim._122163)
      return "122163";
    if (code == DicomDcim._122164)
      return "122164";
    if (code == DicomDcim._122165)
      return "122165";
    if (code == DicomDcim._122166)
      return "122166";
    if (code == DicomDcim._122167)
      return "122167";
    if (code == DicomDcim._122170)
      return "122170";
    if (code == DicomDcim._122171)
      return "122171";
    if (code == DicomDcim._122172)
      return "122172";
    if (code == DicomDcim._122173)
      return "122173";
    if (code == DicomDcim._122175)
      return "122175";
    if (code == DicomDcim._122176)
      return "122176";
    if (code == DicomDcim._122177)
      return "122177";
    if (code == DicomDcim._122178)
      return "122178";
    if (code == DicomDcim._122179)
      return "122179";
    if (code == DicomDcim._122180)
      return "122180";
    if (code == DicomDcim._122181)
      return "122181";
    if (code == DicomDcim._122182)
      return "122182";
    if (code == DicomDcim._122183)
      return "122183";
    if (code == DicomDcim._122185)
      return "122185";
    if (code == DicomDcim._122187)
      return "122187";
    if (code == DicomDcim._122188)
      return "122188";
    if (code == DicomDcim._122189)
      return "122189";
    if (code == DicomDcim._122190)
      return "122190";
    if (code == DicomDcim._122191)
      return "122191";
    if (code == DicomDcim._122192)
      return "122192";
    if (code == DicomDcim._122193)
      return "122193";
    if (code == DicomDcim._122194)
      return "122194";
    if (code == DicomDcim._122195)
      return "122195";
    if (code == DicomDcim._122196)
      return "122196";
    if (code == DicomDcim._122197)
      return "122197";
    if (code == DicomDcim._122198)
      return "122198";
    if (code == DicomDcim._122199)
      return "122199";
    if (code == DicomDcim._122201)
      return "122201";
    if (code == DicomDcim._122202)
      return "122202";
    if (code == DicomDcim._122203)
      return "122203";
    if (code == DicomDcim._122204)
      return "122204";
    if (code == DicomDcim._122205)
      return "122205";
    if (code == DicomDcim._122206)
      return "122206";
    if (code == DicomDcim._122207)
      return "122207";
    if (code == DicomDcim._122208)
      return "122208";
    if (code == DicomDcim._122209)
      return "122209";
    if (code == DicomDcim._122210)
      return "122210";
    if (code == DicomDcim._122211)
      return "122211";
    if (code == DicomDcim._122212)
      return "122212";
    if (code == DicomDcim._122213)
      return "122213";
    if (code == DicomDcim._122214)
      return "122214";
    if (code == DicomDcim._122215)
      return "122215";
    if (code == DicomDcim._122216)
      return "122216";
    if (code == DicomDcim._122217)
      return "122217";
    if (code == DicomDcim._122218)
      return "122218";
    if (code == DicomDcim._122219)
      return "122219";
    if (code == DicomDcim._122220)
      return "122220";
    if (code == DicomDcim._122221)
      return "122221";
    if (code == DicomDcim._122222)
      return "122222";
    if (code == DicomDcim._122223)
      return "122223";
    if (code == DicomDcim._122224)
      return "122224";
    if (code == DicomDcim._122225)
      return "122225";
    if (code == DicomDcim._122227)
      return "122227";
    if (code == DicomDcim._122228)
      return "122228";
    if (code == DicomDcim._122229)
      return "122229";
    if (code == DicomDcim._122230)
      return "122230";
    if (code == DicomDcim._122231)
      return "122231";
    if (code == DicomDcim._122232)
      return "122232";
    if (code == DicomDcim._122233)
      return "122233";
    if (code == DicomDcim._122234)
      return "122234";
    if (code == DicomDcim._122235)
      return "122235";
    if (code == DicomDcim._122236)
      return "122236";
    if (code == DicomDcim._122237)
      return "122237";
    if (code == DicomDcim._122238)
      return "122238";
    if (code == DicomDcim._122239)
      return "122239";
    if (code == DicomDcim._122240)
      return "122240";
    if (code == DicomDcim._122241)
      return "122241";
    if (code == DicomDcim._122242)
      return "122242";
    if (code == DicomDcim._122243)
      return "122243";
    if (code == DicomDcim._122244)
      return "122244";
    if (code == DicomDcim._122245)
      return "122245";
    if (code == DicomDcim._122246)
      return "122246";
    if (code == DicomDcim._122247)
      return "122247";
    if (code == DicomDcim._122248)
      return "122248";
    if (code == DicomDcim._122249)
      return "122249";
    if (code == DicomDcim._122250)
      return "122250";
    if (code == DicomDcim._122251)
      return "122251";
    if (code == DicomDcim._122252)
      return "122252";
    if (code == DicomDcim._122253)
      return "122253";
    if (code == DicomDcim._122254)
      return "122254";
    if (code == DicomDcim._122255)
      return "122255";
    if (code == DicomDcim._122256)
      return "122256";
    if (code == DicomDcim._122257)
      return "122257";
    if (code == DicomDcim._122258)
      return "122258";
    if (code == DicomDcim._122259)
      return "122259";
    if (code == DicomDcim._122260)
      return "122260";
    if (code == DicomDcim._122261)
      return "122261";
    if (code == DicomDcim._122262)
      return "122262";
    if (code == DicomDcim._122263)
      return "122263";
    if (code == DicomDcim._122265)
      return "122265";
    if (code == DicomDcim._122266)
      return "122266";
    if (code == DicomDcim._122267)
      return "122267";
    if (code == DicomDcim._122268)
      return "122268";
    if (code == DicomDcim._122269)
      return "122269";
    if (code == DicomDcim._122270)
      return "122270";
    if (code == DicomDcim._122271)
      return "122271";
    if (code == DicomDcim._122272)
      return "122272";
    if (code == DicomDcim._122273)
      return "122273";
    if (code == DicomDcim._122274)
      return "122274";
    if (code == DicomDcim._122275)
      return "122275";
    if (code == DicomDcim._122276)
      return "122276";
    if (code == DicomDcim._122277)
      return "122277";
    if (code == DicomDcim._122278)
      return "122278";
    if (code == DicomDcim._122279)
      return "122279";
    if (code == DicomDcim._122281)
      return "122281";
    if (code == DicomDcim._122282)
      return "122282";
    if (code == DicomDcim._122283)
      return "122283";
    if (code == DicomDcim._122288)
      return "122288";
    if (code == DicomDcim._122291)
      return "122291";
    if (code == DicomDcim._122292)
      return "122292";
    if (code == DicomDcim._122301)
      return "122301";
    if (code == DicomDcim._122302)
      return "122302";
    if (code == DicomDcim._122303)
      return "122303";
    if (code == DicomDcim._122304)
      return "122304";
    if (code == DicomDcim._122305)
      return "122305";
    if (code == DicomDcim._122306)
      return "122306";
    if (code == DicomDcim._122307)
      return "122307";
    if (code == DicomDcim._122308)
      return "122308";
    if (code == DicomDcim._122309)
      return "122309";
    if (code == DicomDcim._122310)
      return "122310";
    if (code == DicomDcim._122311)
      return "122311";
    if (code == DicomDcim._122312)
      return "122312";
    if (code == DicomDcim._122313)
      return "122313";
    if (code == DicomDcim._122319)
      return "122319";
    if (code == DicomDcim._122320)
      return "122320";
    if (code == DicomDcim._122321)
      return "122321";
    if (code == DicomDcim._122322)
      return "122322";
    if (code == DicomDcim._122325)
      return "122325";
    if (code == DicomDcim._122330)
      return "122330";
    if (code == DicomDcim._122331)
      return "122331";
    if (code == DicomDcim._122332)
      return "122332";
    if (code == DicomDcim._122333)
      return "122333";
    if (code == DicomDcim._122334)
      return "122334";
    if (code == DicomDcim._122335)
      return "122335";
    if (code == DicomDcim._122336)
      return "122336";
    if (code == DicomDcim._122337)
      return "122337";
    if (code == DicomDcim._122339)
      return "122339";
    if (code == DicomDcim._122340)
      return "122340";
    if (code == DicomDcim._122341)
      return "122341";
    if (code == DicomDcim._122343)
      return "122343";
    if (code == DicomDcim._122344)
      return "122344";
    if (code == DicomDcim._122345)
      return "122345";
    if (code == DicomDcim._122346)
      return "122346";
    if (code == DicomDcim._122347)
      return "122347";
    if (code == DicomDcim._122348)
      return "122348";
    if (code == DicomDcim._122350)
      return "122350";
    if (code == DicomDcim._122351)
      return "122351";
    if (code == DicomDcim._122352)
      return "122352";
    if (code == DicomDcim._122354)
      return "122354";
    if (code == DicomDcim._122355)
      return "122355";
    if (code == DicomDcim._122356)
      return "122356";
    if (code == DicomDcim._122357)
      return "122357";
    if (code == DicomDcim._122360)
      return "122360";
    if (code == DicomDcim._122361)
      return "122361";
    if (code == DicomDcim._122363)
      return "122363";
    if (code == DicomDcim._122364)
      return "122364";
    if (code == DicomDcim._122367)
      return "122367";
    if (code == DicomDcim._122368)
      return "122368";
    if (code == DicomDcim._122369)
      return "122369";
    if (code == DicomDcim._122370)
      return "122370";
    if (code == DicomDcim._122371)
      return "122371";
    if (code == DicomDcim._122372)
      return "122372";
    if (code == DicomDcim._122374)
      return "122374";
    if (code == DicomDcim._122375)
      return "122375";
    if (code == DicomDcim._122376)
      return "122376";
    if (code == DicomDcim._122380)
      return "122380";
    if (code == DicomDcim._122381)
      return "122381";
    if (code == DicomDcim._122382)
      return "122382";
    if (code == DicomDcim._122383)
      return "122383";
    if (code == DicomDcim._122384)
      return "122384";
    if (code == DicomDcim._122385)
      return "122385";
    if (code == DicomDcim._122386)
      return "122386";
    if (code == DicomDcim._122387)
      return "122387";
    if (code == DicomDcim._122388)
      return "122388";
    if (code == DicomDcim._122389)
      return "122389";
    if (code == DicomDcim._122390)
      return "122390";
    if (code == DicomDcim._122391)
      return "122391";
    if (code == DicomDcim._122393)
      return "122393";
    if (code == DicomDcim._122394)
      return "122394";
    if (code == DicomDcim._122395)
      return "122395";
    if (code == DicomDcim._122398)
      return "122398";
    if (code == DicomDcim._122399)
      return "122399";
    if (code == DicomDcim._122400)
      return "122400";
    if (code == DicomDcim._122401)
      return "122401";
    if (code == DicomDcim._122402)
      return "122402";
    if (code == DicomDcim._122403)
      return "122403";
    if (code == DicomDcim._122404)
      return "122404";
    if (code == DicomDcim._122405)
      return "122405";
    if (code == DicomDcim._122406)
      return "122406";
    if (code == DicomDcim._122407)
      return "122407";
    if (code == DicomDcim._122408)
      return "122408";
    if (code == DicomDcim._122410)
      return "122410";
    if (code == DicomDcim._122411)
      return "122411";
    if (code == DicomDcim._122417)
      return "122417";
    if (code == DicomDcim._122421)
      return "122421";
    if (code == DicomDcim._122422)
      return "122422";
    if (code == DicomDcim._122423)
      return "122423";
    if (code == DicomDcim._122428)
      return "122428";
    if (code == DicomDcim._122429)
      return "122429";
    if (code == DicomDcim._122430)
      return "122430";
    if (code == DicomDcim._122431)
      return "122431";
    if (code == DicomDcim._122432)
      return "122432";
    if (code == DicomDcim._122433)
      return "122433";
    if (code == DicomDcim._122434)
      return "122434";
    if (code == DicomDcim._122435)
      return "122435";
    if (code == DicomDcim._122438)
      return "122438";
    if (code == DicomDcim._122445)
      return "122445";
    if (code == DicomDcim._122446)
      return "122446";
    if (code == DicomDcim._122447)
      return "122447";
    if (code == DicomDcim._122448)
      return "122448";
    if (code == DicomDcim._122449)
      return "122449";
    if (code == DicomDcim._122450)
      return "122450";
    if (code == DicomDcim._122451)
      return "122451";
    if (code == DicomDcim._122452)
      return "122452";
    if (code == DicomDcim._122453)
      return "122453";
    if (code == DicomDcim._122459)
      return "122459";
    if (code == DicomDcim._122461)
      return "122461";
    if (code == DicomDcim._122464)
      return "122464";
    if (code == DicomDcim._122465)
      return "122465";
    if (code == DicomDcim._122466)
      return "122466";
    if (code == DicomDcim._122467)
      return "122467";
    if (code == DicomDcim._122468)
      return "122468";
    if (code == DicomDcim._122469)
      return "122469";
    if (code == DicomDcim._122470)
      return "122470";
    if (code == DicomDcim._122471)
      return "122471";
    if (code == DicomDcim._122472)
      return "122472";
    if (code == DicomDcim._122473)
      return "122473";
    if (code == DicomDcim._122474)
      return "122474";
    if (code == DicomDcim._122475)
      return "122475";
    if (code == DicomDcim._122476)
      return "122476";
    if (code == DicomDcim._122477)
      return "122477";
    if (code == DicomDcim._122480)
      return "122480";
    if (code == DicomDcim._122481)
      return "122481";
    if (code == DicomDcim._122482)
      return "122482";
    if (code == DicomDcim._122485)
      return "122485";
    if (code == DicomDcim._122486)
      return "122486";
    if (code == DicomDcim._122487)
      return "122487";
    if (code == DicomDcim._122488)
      return "122488";
    if (code == DicomDcim._122489)
      return "122489";
    if (code == DicomDcim._122490)
      return "122490";
    if (code == DicomDcim._122491)
      return "122491";
    if (code == DicomDcim._122493)
      return "122493";
    if (code == DicomDcim._122495)
      return "122495";
    if (code == DicomDcim._122496)
      return "122496";
    if (code == DicomDcim._122497)
      return "122497";
    if (code == DicomDcim._122498)
      return "122498";
    if (code == DicomDcim._122499)
      return "122499";
    if (code == DicomDcim._122501)
      return "122501";
    if (code == DicomDcim._122502)
      return "122502";
    if (code == DicomDcim._122503)
      return "122503";
    if (code == DicomDcim._122505)
      return "122505";
    if (code == DicomDcim._122507)
      return "122507";
    if (code == DicomDcim._122508)
      return "122508";
    if (code == DicomDcim._122509)
      return "122509";
    if (code == DicomDcim._122510)
      return "122510";
    if (code == DicomDcim._122511)
      return "122511";
    if (code == DicomDcim._122516)
      return "122516";
    if (code == DicomDcim._122517)
      return "122517";
    if (code == DicomDcim._122528)
      return "122528";
    if (code == DicomDcim._122529)
      return "122529";
    if (code == DicomDcim._122542)
      return "122542";
    if (code == DicomDcim._122544)
      return "122544";
    if (code == DicomDcim._122545)
      return "122545";
    if (code == DicomDcim._122546)
      return "122546";
    if (code == DicomDcim._122547)
      return "122547";
    if (code == DicomDcim._122548)
      return "122548";
    if (code == DicomDcim._122549)
      return "122549";
    if (code == DicomDcim._122550)
      return "122550";
    if (code == DicomDcim._122551)
      return "122551";
    if (code == DicomDcim._122554)
      return "122554";
    if (code == DicomDcim._122555)
      return "122555";
    if (code == DicomDcim._122558)
      return "122558";
    if (code == DicomDcim._122559)
      return "122559";
    if (code == DicomDcim._122560)
      return "122560";
    if (code == DicomDcim._122562)
      return "122562";
    if (code == DicomDcim._122563)
      return "122563";
    if (code == DicomDcim._122564)
      return "122564";
    if (code == DicomDcim._122565)
      return "122565";
    if (code == DicomDcim._122566)
      return "122566";
    if (code == DicomDcim._122572)
      return "122572";
    if (code == DicomDcim._122574)
      return "122574";
    if (code == DicomDcim._122575)
      return "122575";
    if (code == DicomDcim._122582)
      return "122582";
    if (code == DicomDcim._122600)
      return "122600";
    if (code == DicomDcim._122601)
      return "122601";
    if (code == DicomDcim._122602)
      return "122602";
    if (code == DicomDcim._122603)
      return "122603";
    if (code == DicomDcim._122604)
      return "122604";
    if (code == DicomDcim._122605)
      return "122605";
    if (code == DicomDcim._122606)
      return "122606";
    if (code == DicomDcim._122607)
      return "122607";
    if (code == DicomDcim._122608)
      return "122608";
    if (code == DicomDcim._122609)
      return "122609";
    if (code == DicomDcim._122611)
      return "122611";
    if (code == DicomDcim._122612)
      return "122612";
    if (code == DicomDcim._122616)
      return "122616";
    if (code == DicomDcim._122617)
      return "122617";
    if (code == DicomDcim._122618)
      return "122618";
    if (code == DicomDcim._122619)
      return "122619";
    if (code == DicomDcim._122620)
      return "122620";
    if (code == DicomDcim._122621)
      return "122621";
    if (code == DicomDcim._122624)
      return "122624";
    if (code == DicomDcim._122627)
      return "122627";
    if (code == DicomDcim._122628)
      return "122628";
    if (code == DicomDcim._122631)
      return "122631";
    if (code == DicomDcim._122633)
      return "122633";
    if (code == DicomDcim._122634)
      return "122634";
    if (code == DicomDcim._122635)
      return "122635";
    if (code == DicomDcim._122636)
      return "122636";
    if (code == DicomDcim._122637)
      return "122637";
    if (code == DicomDcim._122638)
      return "122638";
    if (code == DicomDcim._122639)
      return "122639";
    if (code == DicomDcim._122640)
      return "122640";
    if (code == DicomDcim._122642)
      return "122642";
    if (code == DicomDcim._122643)
      return "122643";
    if (code == DicomDcim._122645)
      return "122645";
    if (code == DicomDcim._122650)
      return "122650";
    if (code == DicomDcim._122651)
      return "122651";
    if (code == DicomDcim._122652)
      return "122652";
    if (code == DicomDcim._122655)
      return "122655";
    if (code == DicomDcim._122656)
      return "122656";
    if (code == DicomDcim._122657)
      return "122657";
    if (code == DicomDcim._122658)
      return "122658";
    if (code == DicomDcim._122659)
      return "122659";
    if (code == DicomDcim._122660)
      return "122660";
    if (code == DicomDcim._122661)
      return "122661";
    if (code == DicomDcim._122664)
      return "122664";
    if (code == DicomDcim._122665)
      return "122665";
    if (code == DicomDcim._122666)
      return "122666";
    if (code == DicomDcim._122667)
      return "122667";
    if (code == DicomDcim._122668)
      return "122668";
    if (code == DicomDcim._122670)
      return "122670";
    if (code == DicomDcim._122675)
      return "122675";
    if (code == DicomDcim._122680)
      return "122680";
    if (code == DicomDcim._122683)
      return "122683";
    if (code == DicomDcim._122684)
      return "122684";
    if (code == DicomDcim._122685)
      return "122685";
    if (code == DicomDcim._122686)
      return "122686";
    if (code == DicomDcim._122687)
      return "122687";
    if (code == DicomDcim._122698)
      return "122698";
    if (code == DicomDcim._122699)
      return "122699";
    if (code == DicomDcim._122700)
      return "122700";
    if (code == DicomDcim._122701)
      return "122701";
    if (code == DicomDcim._122702)
      return "122702";
    if (code == DicomDcim._122703)
      return "122703";
    if (code == DicomDcim._122704)
      return "122704";
    if (code == DicomDcim._122705)
      return "122705";
    if (code == DicomDcim._122706)
      return "122706";
    if (code == DicomDcim._122707)
      return "122707";
    if (code == DicomDcim._122708)
      return "122708";
    if (code == DicomDcim._122709)
      return "122709";
    if (code == DicomDcim._122710)
      return "122710";
    if (code == DicomDcim._122711)
      return "122711";
    if (code == DicomDcim._122712)
      return "122712";
    if (code == DicomDcim._122713)
      return "122713";
    if (code == DicomDcim._122715)
      return "122715";
    if (code == DicomDcim._122716)
      return "122716";
    if (code == DicomDcim._122717)
      return "122717";
    if (code == DicomDcim._122718)
      return "122718";
    if (code == DicomDcim._122720)
      return "122720";
    if (code == DicomDcim._122721)
      return "122721";
    if (code == DicomDcim._122726)
      return "122726";
    if (code == DicomDcim._122727)
      return "122727";
    if (code == DicomDcim._122728)
      return "122728";
    if (code == DicomDcim._122729)
      return "122729";
    if (code == DicomDcim._122730)
      return "122730";
    if (code == DicomDcim._122731)
      return "122731";
    if (code == DicomDcim._122732)
      return "122732";
    if (code == DicomDcim._122733)
      return "122733";
    if (code == DicomDcim._122734)
      return "122734";
    if (code == DicomDcim._122735)
      return "122735";
    if (code == DicomDcim._122739)
      return "122739";
    if (code == DicomDcim._122740)
      return "122740";
    if (code == DicomDcim._122741)
      return "122741";
    if (code == DicomDcim._122742)
      return "122742";
    if (code == DicomDcim._122743)
      return "122743";
    if (code == DicomDcim._122744)
      return "122744";
    if (code == DicomDcim._122745)
      return "122745";
    if (code == DicomDcim._122748)
      return "122748";
    if (code == DicomDcim._122750)
      return "122750";
    if (code == DicomDcim._122751)
      return "122751";
    if (code == DicomDcim._122752)
      return "122752";
    if (code == DicomDcim._122753)
      return "122753";
    if (code == DicomDcim._122755)
      return "122755";
    if (code == DicomDcim._122756)
      return "122756";
    if (code == DicomDcim._122757)
      return "122757";
    if (code == DicomDcim._122758)
      return "122758";
    if (code == DicomDcim._122759)
      return "122759";
    if (code == DicomDcim._122760)
      return "122760";
    if (code == DicomDcim._122762)
      return "122762";
    if (code == DicomDcim._122764)
      return "122764";
    if (code == DicomDcim._122768)
      return "122768";
    if (code == DicomDcim._122769)
      return "122769";
    if (code == DicomDcim._122770)
      return "122770";
    if (code == DicomDcim._122771)
      return "122771";
    if (code == DicomDcim._122772)
      return "122772";
    if (code == DicomDcim._122773)
      return "122773";
    if (code == DicomDcim._122775)
      return "122775";
    if (code == DicomDcim._122776)
      return "122776";
    if (code == DicomDcim._122781)
      return "122781";
    if (code == DicomDcim._122782)
      return "122782";
    if (code == DicomDcim._122783)
      return "122783";
    if (code == DicomDcim._122784)
      return "122784";
    if (code == DicomDcim._122785)
      return "122785";
    if (code == DicomDcim._122791)
      return "122791";
    if (code == DicomDcim._122792)
      return "122792";
    if (code == DicomDcim._122793)
      return "122793";
    if (code == DicomDcim._122795)
      return "122795";
    if (code == DicomDcim._122796)
      return "122796";
    if (code == DicomDcim._122797)
      return "122797";
    if (code == DicomDcim._122799)
      return "122799";
    if (code == DicomDcim._123001)
      return "123001";
    if (code == DicomDcim._123003)
      return "123003";
    if (code == DicomDcim._123004)
      return "123004";
    if (code == DicomDcim._123005)
      return "123005";
    if (code == DicomDcim._123006)
      return "123006";
    if (code == DicomDcim._123007)
      return "123007";
    if (code == DicomDcim._123009)
      return "123009";
    if (code == DicomDcim._123010)
      return "123010";
    if (code == DicomDcim._123011)
      return "123011";
    if (code == DicomDcim._123012)
      return "123012";
    if (code == DicomDcim._123014)
      return "123014";
    if (code == DicomDcim._123015)
      return "123015";
    if (code == DicomDcim._123016)
      return "123016";
    if (code == DicomDcim._123019)
      return "123019";
    if (code == DicomDcim._123101)
      return "123101";
    if (code == DicomDcim._123102)
      return "123102";
    if (code == DicomDcim._123103)
      return "123103";
    if (code == DicomDcim._123104)
      return "123104";
    if (code == DicomDcim._123105)
      return "123105";
    if (code == DicomDcim._123106)
      return "123106";
    if (code == DicomDcim._123107)
      return "123107";
    if (code == DicomDcim._123108)
      return "123108";
    if (code == DicomDcim._123109)
      return "123109";
    if (code == DicomDcim._123110)
      return "123110";
    if (code == DicomDcim._123111)
      return "123111";
    if (code == DicomDcim._125000)
      return "125000";
    if (code == DicomDcim._125001)
      return "125001";
    if (code == DicomDcim._125002)
      return "125002";
    if (code == DicomDcim._125003)
      return "125003";
    if (code == DicomDcim._125004)
      return "125004";
    if (code == DicomDcim._125005)
      return "125005";
    if (code == DicomDcim._125006)
      return "125006";
    if (code == DicomDcim._125007)
      return "125007";
    if (code == DicomDcim._125008)
      return "125008";
    if (code == DicomDcim._125009)
      return "125009";
    if (code == DicomDcim._125010)
      return "125010";
    if (code == DicomDcim._125011)
      return "125011";
    if (code == DicomDcim._125012)
      return "125012";
    if (code == DicomDcim._125013)
      return "125013";
    if (code == DicomDcim._125015)
      return "125015";
    if (code == DicomDcim._125016)
      return "125016";
    if (code == DicomDcim._125021)
      return "125021";
    if (code == DicomDcim._125022)
      return "125022";
    if (code == DicomDcim._125023)
      return "125023";
    if (code == DicomDcim._125024)
      return "125024";
    if (code == DicomDcim._125025)
      return "125025";
    if (code == DicomDcim._125030)
      return "125030";
    if (code == DicomDcim._125031)
      return "125031";
    if (code == DicomDcim._125032)
      return "125032";
    if (code == DicomDcim._125033)
      return "125033";
    if (code == DicomDcim._125034)
      return "125034";
    if (code == DicomDcim._125035)
      return "125035";
    if (code == DicomDcim._125036)
      return "125036";
    if (code == DicomDcim._125037)
      return "125037";
    if (code == DicomDcim._125038)
      return "125038";
    if (code == DicomDcim._125040)
      return "125040";
    if (code == DicomDcim._125041)
      return "125041";
    if (code == DicomDcim._125100)
      return "125100";
    if (code == DicomDcim._125101)
      return "125101";
    if (code == DicomDcim._125102)
      return "125102";
    if (code == DicomDcim._125105)
      return "125105";
    if (code == DicomDcim._125106)
      return "125106";
    if (code == DicomDcim._125107)
      return "125107";
    if (code == DicomDcim._125195)
      return "125195";
    if (code == DicomDcim._125196)
      return "125196";
    if (code == DicomDcim._125197)
      return "125197";
    if (code == DicomDcim._125200)
      return "125200";
    if (code == DicomDcim._125201)
      return "125201";
    if (code == DicomDcim._125202)
      return "125202";
    if (code == DicomDcim._125203)
      return "125203";
    if (code == DicomDcim._125204)
      return "125204";
    if (code == DicomDcim._125205)
      return "125205";
    if (code == DicomDcim._125206)
      return "125206";
    if (code == DicomDcim._125207)
      return "125207";
    if (code == DicomDcim._125208)
      return "125208";
    if (code == DicomDcim._125209)
      return "125209";
    if (code == DicomDcim._125210)
      return "125210";
    if (code == DicomDcim._125211)
      return "125211";
    if (code == DicomDcim._125212)
      return "125212";
    if (code == DicomDcim._125213)
      return "125213";
    if (code == DicomDcim._125214)
      return "125214";
    if (code == DicomDcim._125215)
      return "125215";
    if (code == DicomDcim._125216)
      return "125216";
    if (code == DicomDcim._125217)
      return "125217";
    if (code == DicomDcim._125218)
      return "125218";
    if (code == DicomDcim._125219)
      return "125219";
    if (code == DicomDcim._125220)
      return "125220";
    if (code == DicomDcim._125221)
      return "125221";
    if (code == DicomDcim._125222)
      return "125222";
    if (code == DicomDcim._125223)
      return "125223";
    if (code == DicomDcim._125224)
      return "125224";
    if (code == DicomDcim._125225)
      return "125225";
    if (code == DicomDcim._125226)
      return "125226";
    if (code == DicomDcim._125227)
      return "125227";
    if (code == DicomDcim._125228)
      return "125228";
    if (code == DicomDcim._125230)
      return "125230";
    if (code == DicomDcim._125231)
      return "125231";
    if (code == DicomDcim._125233)
      return "125233";
    if (code == DicomDcim._125234)
      return "125234";
    if (code == DicomDcim._125235)
      return "125235";
    if (code == DicomDcim._125236)
      return "125236";
    if (code == DicomDcim._125237)
      return "125237";
    if (code == DicomDcim._125238)
      return "125238";
    if (code == DicomDcim._125239)
      return "125239";
    if (code == DicomDcim._125240)
      return "125240";
    if (code == DicomDcim._125241)
      return "125241";
    if (code == DicomDcim._125242)
      return "125242";
    if (code == DicomDcim._125251)
      return "125251";
    if (code == DicomDcim._125252)
      return "125252";
    if (code == DicomDcim._125253)
      return "125253";
    if (code == DicomDcim._125254)
      return "125254";
    if (code == DicomDcim._125255)
      return "125255";
    if (code == DicomDcim._125256)
      return "125256";
    if (code == DicomDcim._125257)
      return "125257";
    if (code == DicomDcim._125258)
      return "125258";
    if (code == DicomDcim._125259)
      return "125259";
    if (code == DicomDcim._125261)
      return "125261";
    if (code == DicomDcim._125262)
      return "125262";
    if (code == DicomDcim._125263)
      return "125263";
    if (code == DicomDcim._125264)
      return "125264";
    if (code == DicomDcim._125265)
      return "125265";
    if (code == DicomDcim._125270)
      return "125270";
    if (code == DicomDcim._125271)
      return "125271";
    if (code == DicomDcim._125272)
      return "125272";
    if (code == DicomDcim._125273)
      return "125273";
    if (code == DicomDcim._125901)
      return "125901";
    if (code == DicomDcim._125902)
      return "125902";
    if (code == DicomDcim._125903)
      return "125903";
    if (code == DicomDcim._125904)
      return "125904";
    if (code == DicomDcim._125905)
      return "125905";
    if (code == DicomDcim._125906)
      return "125906";
    if (code == DicomDcim._125907)
      return "125907";
    if (code == DicomDcim._125908)
      return "125908";
    if (code == DicomDcim._126000)
      return "126000";
    if (code == DicomDcim._126001)
      return "126001";
    if (code == DicomDcim._126002)
      return "126002";
    if (code == DicomDcim._126003)
      return "126003";
    if (code == DicomDcim._126010)
      return "126010";
    if (code == DicomDcim._126011)
      return "126011";
    if (code == DicomDcim._126020)
      return "126020";
    if (code == DicomDcim._126021)
      return "126021";
    if (code == DicomDcim._126022)
      return "126022";
    if (code == DicomDcim._126030)
      return "126030";
    if (code == DicomDcim._126031)
      return "126031";
    if (code == DicomDcim._126032)
      return "126032";
    if (code == DicomDcim._126033)
      return "126033";
    if (code == DicomDcim._126034)
      return "126034";
    if (code == DicomDcim._126035)
      return "126035";
    if (code == DicomDcim._126036)
      return "126036";
    if (code == DicomDcim._126037)
      return "126037";
    if (code == DicomDcim._126038)
      return "126038";
    if (code == DicomDcim._126039)
      return "126039";
    if (code == DicomDcim._126040)
      return "126040";
    if (code == DicomDcim._126050)
      return "126050";
    if (code == DicomDcim._126051)
      return "126051";
    if (code == DicomDcim._126052)
      return "126052";
    if (code == DicomDcim._126060)
      return "126060";
    if (code == DicomDcim._126061)
      return "126061";
    if (code == DicomDcim._126062)
      return "126062";
    if (code == DicomDcim._126063)
      return "126063";
    if (code == DicomDcim._126064)
      return "126064";
    if (code == DicomDcim._126065)
      return "126065";
    if (code == DicomDcim._126066)
      return "126066";
    if (code == DicomDcim._126067)
      return "126067";
    if (code == DicomDcim._126070)
      return "126070";
    if (code == DicomDcim._126071)
      return "126071";
    if (code == DicomDcim._126072)
      return "126072";
    if (code == DicomDcim._126073)
      return "126073";
    if (code == DicomDcim._126074)
      return "126074";
    if (code == DicomDcim._126075)
      return "126075";
    if (code == DicomDcim._126080)
      return "126080";
    if (code == DicomDcim._126081)
      return "126081";
    if (code == DicomDcim._126100)
      return "126100";
    if (code == DicomDcim._126200)
      return "126200";
    if (code == DicomDcim._126201)
      return "126201";
    if (code == DicomDcim._126202)
      return "126202";
    if (code == DicomDcim._126203)
      return "126203";
    if (code == DicomDcim._126220)
      return "126220";
    if (code == DicomDcim._126300)
      return "126300";
    if (code == DicomDcim._126301)
      return "126301";
    if (code == DicomDcim._126302)
      return "126302";
    if (code == DicomDcim._126303)
      return "126303";
    if (code == DicomDcim._126310)
      return "126310";
    if (code == DicomDcim._126311)
      return "126311";
    if (code == DicomDcim._126312)
      return "126312";
    if (code == DicomDcim._126313)
      return "126313";
    if (code == DicomDcim._126314)
      return "126314";
    if (code == DicomDcim._126320)
      return "126320";
    if (code == DicomDcim._126321)
      return "126321";
    if (code == DicomDcim._126322)
      return "126322";
    if (code == DicomDcim._126330)
      return "126330";
    if (code == DicomDcim._126331)
      return "126331";
    if (code == DicomDcim._126340)
      return "126340";
    if (code == DicomDcim._126341)
      return "126341";
    if (code == DicomDcim._126342)
      return "126342";
    if (code == DicomDcim._126343)
      return "126343";
    if (code == DicomDcim._126344)
      return "126344";
    if (code == DicomDcim._126350)
      return "126350";
    if (code == DicomDcim._126351)
      return "126351";
    if (code == DicomDcim._126352)
      return "126352";
    if (code == DicomDcim._126353)
      return "126353";
    if (code == DicomDcim._126360)
      return "126360";
    if (code == DicomDcim._126361)
      return "126361";
    if (code == DicomDcim._126362)
      return "126362";
    if (code == DicomDcim._126363)
      return "126363";
    if (code == DicomDcim._126364)
      return "126364";
    if (code == DicomDcim._126370)
      return "126370";
    if (code == DicomDcim._126371)
      return "126371";
    if (code == DicomDcim._126372)
      return "126372";
    if (code == DicomDcim._126373)
      return "126373";
    if (code == DicomDcim._126374)
      return "126374";
    if (code == DicomDcim._126375)
      return "126375";
    if (code == DicomDcim._126376)
      return "126376";
    if (code == DicomDcim._126377)
      return "126377";
    if (code == DicomDcim._126380)
      return "126380";
    if (code == DicomDcim._126390)
      return "126390";
    if (code == DicomDcim._126391)
      return "126391";
    if (code == DicomDcim._126392)
      return "126392";
    if (code == DicomDcim._126393)
      return "126393";
    if (code == DicomDcim._126394)
      return "126394";
    if (code == DicomDcim._126400)
      return "126400";
    if (code == DicomDcim._126401)
      return "126401";
    if (code == DicomDcim._126402)
      return "126402";
    if (code == DicomDcim._126403)
      return "126403";
    if (code == DicomDcim._126404)
      return "126404";
    if (code == DicomDcim._126410)
      return "126410";
    if (code == DicomDcim._126411)
      return "126411";
    if (code == DicomDcim._126412)
      return "126412";
    if (code == DicomDcim._126413)
      return "126413";
    if (code == DicomDcim._126500)
      return "126500";
    if (code == DicomDcim._126501)
      return "126501";
    if (code == DicomDcim._126502)
      return "126502";
    if (code == DicomDcim._126503)
      return "126503";
    if (code == DicomDcim._126510)
      return "126510";
    if (code == DicomDcim._126511)
      return "126511";
    if (code == DicomDcim._126512)
      return "126512";
    if (code == DicomDcim._126513)
      return "126513";
    if (code == DicomDcim._126514)
      return "126514";
    if (code == DicomDcim._126515)
      return "126515";
    if (code == DicomDcim._126516)
      return "126516";
    if (code == DicomDcim._126517)
      return "126517";
    if (code == DicomDcim._126518)
      return "126518";
    if (code == DicomDcim._126519)
      return "126519";
    if (code == DicomDcim._126520)
      return "126520";
    if (code == DicomDcim._126600)
      return "126600";
    if (code == DicomDcim._126601)
      return "126601";
    if (code == DicomDcim._126602)
      return "126602";
    if (code == DicomDcim._126603)
      return "126603";
    if (code == DicomDcim._126604)
      return "126604";
    if (code == DicomDcim._126605)
      return "126605";
    if (code == DicomDcim._126606)
      return "126606";
    if (code == DicomDcim._126700)
      return "126700";
    if (code == DicomDcim._126701)
      return "126701";
    if (code == DicomDcim._126702)
      return "126702";
    if (code == DicomDcim._126703)
      return "126703";
    if (code == DicomDcim._126704)
      return "126704";
    if (code == DicomDcim._126705)
      return "126705";
    if (code == DicomDcim._126706)
      return "126706";
    if (code == DicomDcim._126707)
      return "126707";
    if (code == DicomDcim._126708)
      return "126708";
    if (code == DicomDcim._126709)
      return "126709";
    if (code == DicomDcim._126710)
      return "126710";
    if (code == DicomDcim._126711)
      return "126711";
    if (code == DicomDcim._126712)
      return "126712";
    if (code == DicomDcim._126713)
      return "126713";
    if (code == DicomDcim._126714)
      return "126714";
    if (code == DicomDcim._126715)
      return "126715";
    if (code == DicomDcim._126716)
      return "126716";
    if (code == DicomDcim._126801)
      return "126801";
    if (code == DicomDcim._126802)
      return "126802";
    if (code == DicomDcim._126803)
      return "126803";
    if (code == DicomDcim._126804)
      return "126804";
    if (code == DicomDcim._126805)
      return "126805";
    if (code == DicomDcim._126806)
      return "126806";
    if (code == DicomDcim._126807)
      return "126807";
    if (code == DicomDcim._126808)
      return "126808";
    if (code == DicomDcim._126809)
      return "126809";
    if (code == DicomDcim._126810)
      return "126810";
    if (code == DicomDcim._126811)
      return "126811";
    return "?";
  }

    public String toSystem(DicomDcim code) {
      return code.getSystem();
      }

}

