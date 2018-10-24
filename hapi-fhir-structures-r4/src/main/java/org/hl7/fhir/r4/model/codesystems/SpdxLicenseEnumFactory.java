package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.r4.model.EnumFactory;

public class SpdxLicenseEnumFactory implements EnumFactory<SpdxLicense> {

  public SpdxLicense fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("not-open-source".equals(codeString))
      return SpdxLicense.NOTOPENSOURCE;
    if ("0BSD".equals(codeString))
      return SpdxLicense._0BSD;
    if ("AAL".equals(codeString))
      return SpdxLicense.AAL;
    if ("Abstyles".equals(codeString))
      return SpdxLicense.ABSTYLES;
    if ("Adobe-2006".equals(codeString))
      return SpdxLicense.ADOBE2006;
    if ("Adobe-Glyph".equals(codeString))
      return SpdxLicense.ADOBEGLYPH;
    if ("ADSL".equals(codeString))
      return SpdxLicense.ADSL;
    if ("AFL-1.1".equals(codeString))
      return SpdxLicense.AFL1_1;
    if ("AFL-1.2".equals(codeString))
      return SpdxLicense.AFL1_2;
    if ("AFL-2.0".equals(codeString))
      return SpdxLicense.AFL2_0;
    if ("AFL-2.1".equals(codeString))
      return SpdxLicense.AFL2_1;
    if ("AFL-3.0".equals(codeString))
      return SpdxLicense.AFL3_0;
    if ("Afmparse".equals(codeString))
      return SpdxLicense.AFMPARSE;
    if ("AGPL-1.0-only".equals(codeString))
      return SpdxLicense.AGPL1_0ONLY;
    if ("AGPL-1.0-or-later".equals(codeString))
      return SpdxLicense.AGPL1_0ORLATER;
    if ("AGPL-3.0-only".equals(codeString))
      return SpdxLicense.AGPL3_0ONLY;
    if ("AGPL-3.0-or-later".equals(codeString))
      return SpdxLicense.AGPL3_0ORLATER;
    if ("Aladdin".equals(codeString))
      return SpdxLicense.ALADDIN;
    if ("AMDPLPA".equals(codeString))
      return SpdxLicense.AMDPLPA;
    if ("AML".equals(codeString))
      return SpdxLicense.AML;
    if ("AMPAS".equals(codeString))
      return SpdxLicense.AMPAS;
    if ("ANTLR-PD".equals(codeString))
      return SpdxLicense.ANTLRPD;
    if ("Apache-1.0".equals(codeString))
      return SpdxLicense.APACHE1_0;
    if ("Apache-1.1".equals(codeString))
      return SpdxLicense.APACHE1_1;
    if ("Apache-2.0".equals(codeString))
      return SpdxLicense.APACHE2_0;
    if ("APAFML".equals(codeString))
      return SpdxLicense.APAFML;
    if ("APL-1.0".equals(codeString))
      return SpdxLicense.APL1_0;
    if ("APSL-1.0".equals(codeString))
      return SpdxLicense.APSL1_0;
    if ("APSL-1.1".equals(codeString))
      return SpdxLicense.APSL1_1;
    if ("APSL-1.2".equals(codeString))
      return SpdxLicense.APSL1_2;
    if ("APSL-2.0".equals(codeString))
      return SpdxLicense.APSL2_0;
    if ("Artistic-1.0-cl8".equals(codeString))
      return SpdxLicense.ARTISTIC1_0CL8;
    if ("Artistic-1.0-Perl".equals(codeString))
      return SpdxLicense.ARTISTIC1_0PERL;
    if ("Artistic-1.0".equals(codeString))
      return SpdxLicense.ARTISTIC1_0;
    if ("Artistic-2.0".equals(codeString))
      return SpdxLicense.ARTISTIC2_0;
    if ("Bahyph".equals(codeString))
      return SpdxLicense.BAHYPH;
    if ("Barr".equals(codeString))
      return SpdxLicense.BARR;
    if ("Beerware".equals(codeString))
      return SpdxLicense.BEERWARE;
    if ("BitTorrent-1.0".equals(codeString))
      return SpdxLicense.BITTORRENT1_0;
    if ("BitTorrent-1.1".equals(codeString))
      return SpdxLicense.BITTORRENT1_1;
    if ("Borceux".equals(codeString))
      return SpdxLicense.BORCEUX;
    if ("BSD-1-Clause".equals(codeString))
      return SpdxLicense.BSD1CLAUSE;
    if ("BSD-2-Clause-FreeBSD".equals(codeString))
      return SpdxLicense.BSD2CLAUSEFREEBSD;
    if ("BSD-2-Clause-NetBSD".equals(codeString))
      return SpdxLicense.BSD2CLAUSENETBSD;
    if ("BSD-2-Clause-Patent".equals(codeString))
      return SpdxLicense.BSD2CLAUSEPATENT;
    if ("BSD-2-Clause".equals(codeString))
      return SpdxLicense.BSD2CLAUSE;
    if ("BSD-3-Clause-Attribution".equals(codeString))
      return SpdxLicense.BSD3CLAUSEATTRIBUTION;
    if ("BSD-3-Clause-Clear".equals(codeString))
      return SpdxLicense.BSD3CLAUSECLEAR;
    if ("BSD-3-Clause-LBNL".equals(codeString))
      return SpdxLicense.BSD3CLAUSELBNL;
    if ("BSD-3-Clause-No-Nuclear-License-2014".equals(codeString))
      return SpdxLicense.BSD3CLAUSENONUCLEARLICENSE2014;
    if ("BSD-3-Clause-No-Nuclear-License".equals(codeString))
      return SpdxLicense.BSD3CLAUSENONUCLEARLICENSE;
    if ("BSD-3-Clause-No-Nuclear-Warranty".equals(codeString))
      return SpdxLicense.BSD3CLAUSENONUCLEARWARRANTY;
    if ("BSD-3-Clause".equals(codeString))
      return SpdxLicense.BSD3CLAUSE;
    if ("BSD-4-Clause-UC".equals(codeString))
      return SpdxLicense.BSD4CLAUSEUC;
    if ("BSD-4-Clause".equals(codeString))
      return SpdxLicense.BSD4CLAUSE;
    if ("BSD-Protection".equals(codeString))
      return SpdxLicense.BSDPROTECTION;
    if ("BSD-Source-Code".equals(codeString))
      return SpdxLicense.BSDSOURCECODE;
    if ("BSL-1.0".equals(codeString))
      return SpdxLicense.BSL1_0;
    if ("bzip2-1.0.5".equals(codeString))
      return SpdxLicense.BZIP21_0_5;
    if ("bzip2-1.0.6".equals(codeString))
      return SpdxLicense.BZIP21_0_6;
    if ("Caldera".equals(codeString))
      return SpdxLicense.CALDERA;
    if ("CATOSL-1.1".equals(codeString))
      return SpdxLicense.CATOSL1_1;
    if ("CC-BY-1.0".equals(codeString))
      return SpdxLicense.CCBY1_0;
    if ("CC-BY-2.0".equals(codeString))
      return SpdxLicense.CCBY2_0;
    if ("CC-BY-2.5".equals(codeString))
      return SpdxLicense.CCBY2_5;
    if ("CC-BY-3.0".equals(codeString))
      return SpdxLicense.CCBY3_0;
    if ("CC-BY-4.0".equals(codeString))
      return SpdxLicense.CCBY4_0;
    if ("CC-BY-NC-1.0".equals(codeString))
      return SpdxLicense.CCBYNC1_0;
    if ("CC-BY-NC-2.0".equals(codeString))
      return SpdxLicense.CCBYNC2_0;
    if ("CC-BY-NC-2.5".equals(codeString))
      return SpdxLicense.CCBYNC2_5;
    if ("CC-BY-NC-3.0".equals(codeString))
      return SpdxLicense.CCBYNC3_0;
    if ("CC-BY-NC-4.0".equals(codeString))
      return SpdxLicense.CCBYNC4_0;
    if ("CC-BY-NC-ND-1.0".equals(codeString))
      return SpdxLicense.CCBYNCND1_0;
    if ("CC-BY-NC-ND-2.0".equals(codeString))
      return SpdxLicense.CCBYNCND2_0;
    if ("CC-BY-NC-ND-2.5".equals(codeString))
      return SpdxLicense.CCBYNCND2_5;
    if ("CC-BY-NC-ND-3.0".equals(codeString))
      return SpdxLicense.CCBYNCND3_0;
    if ("CC-BY-NC-ND-4.0".equals(codeString))
      return SpdxLicense.CCBYNCND4_0;
    if ("CC-BY-NC-SA-1.0".equals(codeString))
      return SpdxLicense.CCBYNCSA1_0;
    if ("CC-BY-NC-SA-2.0".equals(codeString))
      return SpdxLicense.CCBYNCSA2_0;
    if ("CC-BY-NC-SA-2.5".equals(codeString))
      return SpdxLicense.CCBYNCSA2_5;
    if ("CC-BY-NC-SA-3.0".equals(codeString))
      return SpdxLicense.CCBYNCSA3_0;
    if ("CC-BY-NC-SA-4.0".equals(codeString))
      return SpdxLicense.CCBYNCSA4_0;
    if ("CC-BY-ND-1.0".equals(codeString))
      return SpdxLicense.CCBYND1_0;
    if ("CC-BY-ND-2.0".equals(codeString))
      return SpdxLicense.CCBYND2_0;
    if ("CC-BY-ND-2.5".equals(codeString))
      return SpdxLicense.CCBYND2_5;
    if ("CC-BY-ND-3.0".equals(codeString))
      return SpdxLicense.CCBYND3_0;
    if ("CC-BY-ND-4.0".equals(codeString))
      return SpdxLicense.CCBYND4_0;
    if ("CC-BY-SA-1.0".equals(codeString))
      return SpdxLicense.CCBYSA1_0;
    if ("CC-BY-SA-2.0".equals(codeString))
      return SpdxLicense.CCBYSA2_0;
    if ("CC-BY-SA-2.5".equals(codeString))
      return SpdxLicense.CCBYSA2_5;
    if ("CC-BY-SA-3.0".equals(codeString))
      return SpdxLicense.CCBYSA3_0;
    if ("CC-BY-SA-4.0".equals(codeString))
      return SpdxLicense.CCBYSA4_0;
    if ("CC0-1.0".equals(codeString))
      return SpdxLicense.CC01_0;
    if ("CDDL-1.0".equals(codeString))
      return SpdxLicense.CDDL1_0;
    if ("CDDL-1.1".equals(codeString))
      return SpdxLicense.CDDL1_1;
    if ("CDLA-Permissive-1.0".equals(codeString))
      return SpdxLicense.CDLAPERMISSIVE1_0;
    if ("CDLA-Sharing-1.0".equals(codeString))
      return SpdxLicense.CDLASHARING1_0;
    if ("CECILL-1.0".equals(codeString))
      return SpdxLicense.CECILL1_0;
    if ("CECILL-1.1".equals(codeString))
      return SpdxLicense.CECILL1_1;
    if ("CECILL-2.0".equals(codeString))
      return SpdxLicense.CECILL2_0;
    if ("CECILL-2.1".equals(codeString))
      return SpdxLicense.CECILL2_1;
    if ("CECILL-B".equals(codeString))
      return SpdxLicense.CECILLB;
    if ("CECILL-C".equals(codeString))
      return SpdxLicense.CECILLC;
    if ("ClArtistic".equals(codeString))
      return SpdxLicense.CLARTISTIC;
    if ("CNRI-Jython".equals(codeString))
      return SpdxLicense.CNRIJYTHON;
    if ("CNRI-Python-GPL-Compatible".equals(codeString))
      return SpdxLicense.CNRIPYTHONGPLCOMPATIBLE;
    if ("CNRI-Python".equals(codeString))
      return SpdxLicense.CNRIPYTHON;
    if ("Condor-1.1".equals(codeString))
      return SpdxLicense.CONDOR1_1;
    if ("CPAL-1.0".equals(codeString))
      return SpdxLicense.CPAL1_0;
    if ("CPL-1.0".equals(codeString))
      return SpdxLicense.CPL1_0;
    if ("CPOL-1.02".equals(codeString))
      return SpdxLicense.CPOL1_02;
    if ("Crossword".equals(codeString))
      return SpdxLicense.CROSSWORD;
    if ("CrystalStacker".equals(codeString))
      return SpdxLicense.CRYSTALSTACKER;
    if ("CUA-OPL-1.0".equals(codeString))
      return SpdxLicense.CUAOPL1_0;
    if ("Cube".equals(codeString))
      return SpdxLicense.CUBE;
    if ("curl".equals(codeString))
      return SpdxLicense.CURL;
    if ("D-FSL-1.0".equals(codeString))
      return SpdxLicense.DFSL1_0;
    if ("diffmark".equals(codeString))
      return SpdxLicense.DIFFMARK;
    if ("DOC".equals(codeString))
      return SpdxLicense.DOC;
    if ("Dotseqn".equals(codeString))
      return SpdxLicense.DOTSEQN;
    if ("DSDP".equals(codeString))
      return SpdxLicense.DSDP;
    if ("dvipdfm".equals(codeString))
      return SpdxLicense.DVIPDFM;
    if ("ECL-1.0".equals(codeString))
      return SpdxLicense.ECL1_0;
    if ("ECL-2.0".equals(codeString))
      return SpdxLicense.ECL2_0;
    if ("EFL-1.0".equals(codeString))
      return SpdxLicense.EFL1_0;
    if ("EFL-2.0".equals(codeString))
      return SpdxLicense.EFL2_0;
    if ("eGenix".equals(codeString))
      return SpdxLicense.EGENIX;
    if ("Entessa".equals(codeString))
      return SpdxLicense.ENTESSA;
    if ("EPL-1.0".equals(codeString))
      return SpdxLicense.EPL1_0;
    if ("EPL-2.0".equals(codeString))
      return SpdxLicense.EPL2_0;
    if ("ErlPL-1.1".equals(codeString))
      return SpdxLicense.ERLPL1_1;
    if ("EUDatagrid".equals(codeString))
      return SpdxLicense.EUDATAGRID;
    if ("EUPL-1.0".equals(codeString))
      return SpdxLicense.EUPL1_0;
    if ("EUPL-1.1".equals(codeString))
      return SpdxLicense.EUPL1_1;
    if ("EUPL-1.2".equals(codeString))
      return SpdxLicense.EUPL1_2;
    if ("Eurosym".equals(codeString))
      return SpdxLicense.EUROSYM;
    if ("Fair".equals(codeString))
      return SpdxLicense.FAIR;
    if ("Frameworx-1.0".equals(codeString))
      return SpdxLicense.FRAMEWORX1_0;
    if ("FreeImage".equals(codeString))
      return SpdxLicense.FREEIMAGE;
    if ("FSFAP".equals(codeString))
      return SpdxLicense.FSFAP;
    if ("FSFUL".equals(codeString))
      return SpdxLicense.FSFUL;
    if ("FSFULLR".equals(codeString))
      return SpdxLicense.FSFULLR;
    if ("FTL".equals(codeString))
      return SpdxLicense.FTL;
    if ("GFDL-1.1-only".equals(codeString))
      return SpdxLicense.GFDL1_1ONLY;
    if ("GFDL-1.1-or-later".equals(codeString))
      return SpdxLicense.GFDL1_1ORLATER;
    if ("GFDL-1.2-only".equals(codeString))
      return SpdxLicense.GFDL1_2ONLY;
    if ("GFDL-1.2-or-later".equals(codeString))
      return SpdxLicense.GFDL1_2ORLATER;
    if ("GFDL-1.3-only".equals(codeString))
      return SpdxLicense.GFDL1_3ONLY;
    if ("GFDL-1.3-or-later".equals(codeString))
      return SpdxLicense.GFDL1_3ORLATER;
    if ("Giftware".equals(codeString))
      return SpdxLicense.GIFTWARE;
    if ("GL2PS".equals(codeString))
      return SpdxLicense.GL2PS;
    if ("Glide".equals(codeString))
      return SpdxLicense.GLIDE;
    if ("Glulxe".equals(codeString))
      return SpdxLicense.GLULXE;
    if ("gnuplot".equals(codeString))
      return SpdxLicense.GNUPLOT;
    if ("GPL-1.0-only".equals(codeString))
      return SpdxLicense.GPL1_0ONLY;
    if ("GPL-1.0-or-later".equals(codeString))
      return SpdxLicense.GPL1_0ORLATER;
    if ("GPL-2.0-only".equals(codeString))
      return SpdxLicense.GPL2_0ONLY;
    if ("GPL-2.0-or-later".equals(codeString))
      return SpdxLicense.GPL2_0ORLATER;
    if ("GPL-3.0-only".equals(codeString))
      return SpdxLicense.GPL3_0ONLY;
    if ("GPL-3.0-or-later".equals(codeString))
      return SpdxLicense.GPL3_0ORLATER;
    if ("gSOAP-1.3b".equals(codeString))
      return SpdxLicense.GSOAP1_3B;
    if ("HaskellReport".equals(codeString))
      return SpdxLicense.HASKELLREPORT;
    if ("HPND".equals(codeString))
      return SpdxLicense.HPND;
    if ("IBM-pibs".equals(codeString))
      return SpdxLicense.IBMPIBS;
    if ("ICU".equals(codeString))
      return SpdxLicense.ICU;
    if ("IJG".equals(codeString))
      return SpdxLicense.IJG;
    if ("ImageMagick".equals(codeString))
      return SpdxLicense.IMAGEMAGICK;
    if ("iMatix".equals(codeString))
      return SpdxLicense.IMATIX;
    if ("Imlib2".equals(codeString))
      return SpdxLicense.IMLIB2;
    if ("Info-ZIP".equals(codeString))
      return SpdxLicense.INFOZIP;
    if ("Intel-ACPI".equals(codeString))
      return SpdxLicense.INTELACPI;
    if ("Intel".equals(codeString))
      return SpdxLicense.INTEL;
    if ("Interbase-1.0".equals(codeString))
      return SpdxLicense.INTERBASE1_0;
    if ("IPA".equals(codeString))
      return SpdxLicense.IPA;
    if ("IPL-1.0".equals(codeString))
      return SpdxLicense.IPL1_0;
    if ("ISC".equals(codeString))
      return SpdxLicense.ISC;
    if ("JasPer-2.0".equals(codeString))
      return SpdxLicense.JASPER2_0;
    if ("JSON".equals(codeString))
      return SpdxLicense.JSON;
    if ("LAL-1.2".equals(codeString))
      return SpdxLicense.LAL1_2;
    if ("LAL-1.3".equals(codeString))
      return SpdxLicense.LAL1_3;
    if ("Latex2e".equals(codeString))
      return SpdxLicense.LATEX2E;
    if ("Leptonica".equals(codeString))
      return SpdxLicense.LEPTONICA;
    if ("LGPL-2.0-only".equals(codeString))
      return SpdxLicense.LGPL2_0ONLY;
    if ("LGPL-2.0-or-later".equals(codeString))
      return SpdxLicense.LGPL2_0ORLATER;
    if ("LGPL-2.1-only".equals(codeString))
      return SpdxLicense.LGPL2_1ONLY;
    if ("LGPL-2.1-or-later".equals(codeString))
      return SpdxLicense.LGPL2_1ORLATER;
    if ("LGPL-3.0-only".equals(codeString))
      return SpdxLicense.LGPL3_0ONLY;
    if ("LGPL-3.0-or-later".equals(codeString))
      return SpdxLicense.LGPL3_0ORLATER;
    if ("LGPLLR".equals(codeString))
      return SpdxLicense.LGPLLR;
    if ("Libpng".equals(codeString))
      return SpdxLicense.LIBPNG;
    if ("libtiff".equals(codeString))
      return SpdxLicense.LIBTIFF;
    if ("LiLiQ-P-1.1".equals(codeString))
      return SpdxLicense.LILIQP1_1;
    if ("LiLiQ-R-1.1".equals(codeString))
      return SpdxLicense.LILIQR1_1;
    if ("LiLiQ-Rplus-1.1".equals(codeString))
      return SpdxLicense.LILIQRPLUS1_1;
    if ("Linux-OpenIB".equals(codeString))
      return SpdxLicense.LINUXOPENIB;
    if ("LPL-1.0".equals(codeString))
      return SpdxLicense.LPL1_0;
    if ("LPL-1.02".equals(codeString))
      return SpdxLicense.LPL1_02;
    if ("LPPL-1.0".equals(codeString))
      return SpdxLicense.LPPL1_0;
    if ("LPPL-1.1".equals(codeString))
      return SpdxLicense.LPPL1_1;
    if ("LPPL-1.2".equals(codeString))
      return SpdxLicense.LPPL1_2;
    if ("LPPL-1.3a".equals(codeString))
      return SpdxLicense.LPPL1_3A;
    if ("LPPL-1.3c".equals(codeString))
      return SpdxLicense.LPPL1_3C;
    if ("MakeIndex".equals(codeString))
      return SpdxLicense.MAKEINDEX;
    if ("MirOS".equals(codeString))
      return SpdxLicense.MIROS;
    if ("MIT-0".equals(codeString))
      return SpdxLicense.MIT0;
    if ("MIT-advertising".equals(codeString))
      return SpdxLicense.MITADVERTISING;
    if ("MIT-CMU".equals(codeString))
      return SpdxLicense.MITCMU;
    if ("MIT-enna".equals(codeString))
      return SpdxLicense.MITENNA;
    if ("MIT-feh".equals(codeString))
      return SpdxLicense.MITFEH;
    if ("MIT".equals(codeString))
      return SpdxLicense.MIT;
    if ("MITNFA".equals(codeString))
      return SpdxLicense.MITNFA;
    if ("Motosoto".equals(codeString))
      return SpdxLicense.MOTOSOTO;
    if ("mpich2".equals(codeString))
      return SpdxLicense.MPICH2;
    if ("MPL-1.0".equals(codeString))
      return SpdxLicense.MPL1_0;
    if ("MPL-1.1".equals(codeString))
      return SpdxLicense.MPL1_1;
    if ("MPL-2.0-no-copyleft-exception".equals(codeString))
      return SpdxLicense.MPL2_0NOCOPYLEFTEXCEPTION;
    if ("MPL-2.0".equals(codeString))
      return SpdxLicense.MPL2_0;
    if ("MS-PL".equals(codeString))
      return SpdxLicense.MSPL;
    if ("MS-RL".equals(codeString))
      return SpdxLicense.MSRL;
    if ("MTLL".equals(codeString))
      return SpdxLicense.MTLL;
    if ("Multics".equals(codeString))
      return SpdxLicense.MULTICS;
    if ("Mup".equals(codeString))
      return SpdxLicense.MUP;
    if ("NASA-1.3".equals(codeString))
      return SpdxLicense.NASA1_3;
    if ("Naumen".equals(codeString))
      return SpdxLicense.NAUMEN;
    if ("NBPL-1.0".equals(codeString))
      return SpdxLicense.NBPL1_0;
    if ("NCSA".equals(codeString))
      return SpdxLicense.NCSA;
    if ("Net-SNMP".equals(codeString))
      return SpdxLicense.NETSNMP;
    if ("NetCDF".equals(codeString))
      return SpdxLicense.NETCDF;
    if ("Newsletr".equals(codeString))
      return SpdxLicense.NEWSLETR;
    if ("NGPL".equals(codeString))
      return SpdxLicense.NGPL;
    if ("NLOD-1.0".equals(codeString))
      return SpdxLicense.NLOD1_0;
    if ("NLPL".equals(codeString))
      return SpdxLicense.NLPL;
    if ("Nokia".equals(codeString))
      return SpdxLicense.NOKIA;
    if ("NOSL".equals(codeString))
      return SpdxLicense.NOSL;
    if ("Noweb".equals(codeString))
      return SpdxLicense.NOWEB;
    if ("NPL-1.0".equals(codeString))
      return SpdxLicense.NPL1_0;
    if ("NPL-1.1".equals(codeString))
      return SpdxLicense.NPL1_1;
    if ("NPOSL-3.0".equals(codeString))
      return SpdxLicense.NPOSL3_0;
    if ("NRL".equals(codeString))
      return SpdxLicense.NRL;
    if ("NTP".equals(codeString))
      return SpdxLicense.NTP;
    if ("OCCT-PL".equals(codeString))
      return SpdxLicense.OCCTPL;
    if ("OCLC-2.0".equals(codeString))
      return SpdxLicense.OCLC2_0;
    if ("ODbL-1.0".equals(codeString))
      return SpdxLicense.ODBL1_0;
    if ("OFL-1.0".equals(codeString))
      return SpdxLicense.OFL1_0;
    if ("OFL-1.1".equals(codeString))
      return SpdxLicense.OFL1_1;
    if ("OGTSL".equals(codeString))
      return SpdxLicense.OGTSL;
    if ("OLDAP-1.1".equals(codeString))
      return SpdxLicense.OLDAP1_1;
    if ("OLDAP-1.2".equals(codeString))
      return SpdxLicense.OLDAP1_2;
    if ("OLDAP-1.3".equals(codeString))
      return SpdxLicense.OLDAP1_3;
    if ("OLDAP-1.4".equals(codeString))
      return SpdxLicense.OLDAP1_4;
    if ("OLDAP-2.0.1".equals(codeString))
      return SpdxLicense.OLDAP2_0_1;
    if ("OLDAP-2.0".equals(codeString))
      return SpdxLicense.OLDAP2_0;
    if ("OLDAP-2.1".equals(codeString))
      return SpdxLicense.OLDAP2_1;
    if ("OLDAP-2.2.1".equals(codeString))
      return SpdxLicense.OLDAP2_2_1;
    if ("OLDAP-2.2.2".equals(codeString))
      return SpdxLicense.OLDAP2_2_2;
    if ("OLDAP-2.2".equals(codeString))
      return SpdxLicense.OLDAP2_2;
    if ("OLDAP-2.3".equals(codeString))
      return SpdxLicense.OLDAP2_3;
    if ("OLDAP-2.4".equals(codeString))
      return SpdxLicense.OLDAP2_4;
    if ("OLDAP-2.5".equals(codeString))
      return SpdxLicense.OLDAP2_5;
    if ("OLDAP-2.6".equals(codeString))
      return SpdxLicense.OLDAP2_6;
    if ("OLDAP-2.7".equals(codeString))
      return SpdxLicense.OLDAP2_7;
    if ("OLDAP-2.8".equals(codeString))
      return SpdxLicense.OLDAP2_8;
    if ("OML".equals(codeString))
      return SpdxLicense.OML;
    if ("OpenSSL".equals(codeString))
      return SpdxLicense.OPENSSL;
    if ("OPL-1.0".equals(codeString))
      return SpdxLicense.OPL1_0;
    if ("OSET-PL-2.1".equals(codeString))
      return SpdxLicense.OSETPL2_1;
    if ("OSL-1.0".equals(codeString))
      return SpdxLicense.OSL1_0;
    if ("OSL-1.1".equals(codeString))
      return SpdxLicense.OSL1_1;
    if ("OSL-2.0".equals(codeString))
      return SpdxLicense.OSL2_0;
    if ("OSL-2.1".equals(codeString))
      return SpdxLicense.OSL2_1;
    if ("OSL-3.0".equals(codeString))
      return SpdxLicense.OSL3_0;
    if ("PDDL-1.0".equals(codeString))
      return SpdxLicense.PDDL1_0;
    if ("PHP-3.0".equals(codeString))
      return SpdxLicense.PHP3_0;
    if ("PHP-3.01".equals(codeString))
      return SpdxLicense.PHP3_01;
    if ("Plexus".equals(codeString))
      return SpdxLicense.PLEXUS;
    if ("PostgreSQL".equals(codeString))
      return SpdxLicense.POSTGRESQL;
    if ("psfrag".equals(codeString))
      return SpdxLicense.PSFRAG;
    if ("psutils".equals(codeString))
      return SpdxLicense.PSUTILS;
    if ("Python-2.0".equals(codeString))
      return SpdxLicense.PYTHON2_0;
    if ("Qhull".equals(codeString))
      return SpdxLicense.QHULL;
    if ("QPL-1.0".equals(codeString))
      return SpdxLicense.QPL1_0;
    if ("Rdisc".equals(codeString))
      return SpdxLicense.RDISC;
    if ("RHeCos-1.1".equals(codeString))
      return SpdxLicense.RHECOS1_1;
    if ("RPL-1.1".equals(codeString))
      return SpdxLicense.RPL1_1;
    if ("RPL-1.5".equals(codeString))
      return SpdxLicense.RPL1_5;
    if ("RPSL-1.0".equals(codeString))
      return SpdxLicense.RPSL1_0;
    if ("RSA-MD".equals(codeString))
      return SpdxLicense.RSAMD;
    if ("RSCPL".equals(codeString))
      return SpdxLicense.RSCPL;
    if ("Ruby".equals(codeString))
      return SpdxLicense.RUBY;
    if ("SAX-PD".equals(codeString))
      return SpdxLicense.SAXPD;
    if ("Saxpath".equals(codeString))
      return SpdxLicense.SAXPATH;
    if ("SCEA".equals(codeString))
      return SpdxLicense.SCEA;
    if ("Sendmail".equals(codeString))
      return SpdxLicense.SENDMAIL;
    if ("SGI-B-1.0".equals(codeString))
      return SpdxLicense.SGIB1_0;
    if ("SGI-B-1.1".equals(codeString))
      return SpdxLicense.SGIB1_1;
    if ("SGI-B-2.0".equals(codeString))
      return SpdxLicense.SGIB2_0;
    if ("SimPL-2.0".equals(codeString))
      return SpdxLicense.SIMPL2_0;
    if ("SISSL-1.2".equals(codeString))
      return SpdxLicense.SISSL1_2;
    if ("SISSL".equals(codeString))
      return SpdxLicense.SISSL;
    if ("Sleepycat".equals(codeString))
      return SpdxLicense.SLEEPYCAT;
    if ("SMLNJ".equals(codeString))
      return SpdxLicense.SMLNJ;
    if ("SMPPL".equals(codeString))
      return SpdxLicense.SMPPL;
    if ("SNIA".equals(codeString))
      return SpdxLicense.SNIA;
    if ("Spencer-86".equals(codeString))
      return SpdxLicense.SPENCER86;
    if ("Spencer-94".equals(codeString))
      return SpdxLicense.SPENCER94;
    if ("Spencer-99".equals(codeString))
      return SpdxLicense.SPENCER99;
    if ("SPL-1.0".equals(codeString))
      return SpdxLicense.SPL1_0;
    if ("SugarCRM-1.1.3".equals(codeString))
      return SpdxLicense.SUGARCRM1_1_3;
    if ("SWL".equals(codeString))
      return SpdxLicense.SWL;
    if ("TCL".equals(codeString))
      return SpdxLicense.TCL;
    if ("TCP-wrappers".equals(codeString))
      return SpdxLicense.TCPWRAPPERS;
    if ("TMate".equals(codeString))
      return SpdxLicense.TMATE;
    if ("TORQUE-1.1".equals(codeString))
      return SpdxLicense.TORQUE1_1;
    if ("TOSL".equals(codeString))
      return SpdxLicense.TOSL;
    if ("Unicode-DFS-2015".equals(codeString))
      return SpdxLicense.UNICODEDFS2015;
    if ("Unicode-DFS-2016".equals(codeString))
      return SpdxLicense.UNICODEDFS2016;
    if ("Unicode-TOU".equals(codeString))
      return SpdxLicense.UNICODETOU;
    if ("Unlicense".equals(codeString))
      return SpdxLicense.UNLICENSE;
    if ("UPL-1.0".equals(codeString))
      return SpdxLicense.UPL1_0;
    if ("Vim".equals(codeString))
      return SpdxLicense.VIM;
    if ("VOSTROM".equals(codeString))
      return SpdxLicense.VOSTROM;
    if ("VSL-1.0".equals(codeString))
      return SpdxLicense.VSL1_0;
    if ("W3C-19980720".equals(codeString))
      return SpdxLicense.W3C19980720;
    if ("W3C-20150513".equals(codeString))
      return SpdxLicense.W3C20150513;
    if ("W3C".equals(codeString))
      return SpdxLicense.W3C;
    if ("Watcom-1.0".equals(codeString))
      return SpdxLicense.WATCOM1_0;
    if ("Wsuipa".equals(codeString))
      return SpdxLicense.WSUIPA;
    if ("WTFPL".equals(codeString))
      return SpdxLicense.WTFPL;
    if ("X11".equals(codeString))
      return SpdxLicense.X11;
    if ("Xerox".equals(codeString))
      return SpdxLicense.XEROX;
    if ("XFree86-1.1".equals(codeString))
      return SpdxLicense.XFREE861_1;
    if ("xinetd".equals(codeString))
      return SpdxLicense.XINETD;
    if ("Xnet".equals(codeString))
      return SpdxLicense.XNET;
    if ("xpp".equals(codeString))
      return SpdxLicense.XPP;
    if ("XSkat".equals(codeString))
      return SpdxLicense.XSKAT;
    if ("YPL-1.0".equals(codeString))
      return SpdxLicense.YPL1_0;
    if ("YPL-1.1".equals(codeString))
      return SpdxLicense.YPL1_1;
    if ("Zed".equals(codeString))
      return SpdxLicense.ZED;
    if ("Zend-2.0".equals(codeString))
      return SpdxLicense.ZEND2_0;
    if ("Zimbra-1.3".equals(codeString))
      return SpdxLicense.ZIMBRA1_3;
    if ("Zimbra-1.4".equals(codeString))
      return SpdxLicense.ZIMBRA1_4;
    if ("zlib-acknowledgement".equals(codeString))
      return SpdxLicense.ZLIBACKNOWLEDGEMENT;
    if ("Zlib".equals(codeString))
      return SpdxLicense.ZLIB;
    if ("ZPL-1.1".equals(codeString))
      return SpdxLicense.ZPL1_1;
    if ("ZPL-2.0".equals(codeString))
      return SpdxLicense.ZPL2_0;
    if ("ZPL-2.1".equals(codeString))
      return SpdxLicense.ZPL2_1;
    throw new IllegalArgumentException("Unknown SpdxLicense code '"+codeString+"'");
  }

  public String toCode(SpdxLicense code) {
    if (code == SpdxLicense.NOTOPENSOURCE)
      return "not-open-source";
    if (code == SpdxLicense._0BSD)
      return "0BSD";
    if (code == SpdxLicense.AAL)
      return "AAL";
    if (code == SpdxLicense.ABSTYLES)
      return "Abstyles";
    if (code == SpdxLicense.ADOBE2006)
      return "Adobe-2006";
    if (code == SpdxLicense.ADOBEGLYPH)
      return "Adobe-Glyph";
    if (code == SpdxLicense.ADSL)
      return "ADSL";
    if (code == SpdxLicense.AFL1_1)
      return "AFL-1.1";
    if (code == SpdxLicense.AFL1_2)
      return "AFL-1.2";
    if (code == SpdxLicense.AFL2_0)
      return "AFL-2.0";
    if (code == SpdxLicense.AFL2_1)
      return "AFL-2.1";
    if (code == SpdxLicense.AFL3_0)
      return "AFL-3.0";
    if (code == SpdxLicense.AFMPARSE)
      return "Afmparse";
    if (code == SpdxLicense.AGPL1_0ONLY)
      return "AGPL-1.0-only";
    if (code == SpdxLicense.AGPL1_0ORLATER)
      return "AGPL-1.0-or-later";
    if (code == SpdxLicense.AGPL3_0ONLY)
      return "AGPL-3.0-only";
    if (code == SpdxLicense.AGPL3_0ORLATER)
      return "AGPL-3.0-or-later";
    if (code == SpdxLicense.ALADDIN)
      return "Aladdin";
    if (code == SpdxLicense.AMDPLPA)
      return "AMDPLPA";
    if (code == SpdxLicense.AML)
      return "AML";
    if (code == SpdxLicense.AMPAS)
      return "AMPAS";
    if (code == SpdxLicense.ANTLRPD)
      return "ANTLR-PD";
    if (code == SpdxLicense.APACHE1_0)
      return "Apache-1.0";
    if (code == SpdxLicense.APACHE1_1)
      return "Apache-1.1";
    if (code == SpdxLicense.APACHE2_0)
      return "Apache-2.0";
    if (code == SpdxLicense.APAFML)
      return "APAFML";
    if (code == SpdxLicense.APL1_0)
      return "APL-1.0";
    if (code == SpdxLicense.APSL1_0)
      return "APSL-1.0";
    if (code == SpdxLicense.APSL1_1)
      return "APSL-1.1";
    if (code == SpdxLicense.APSL1_2)
      return "APSL-1.2";
    if (code == SpdxLicense.APSL2_0)
      return "APSL-2.0";
    if (code == SpdxLicense.ARTISTIC1_0CL8)
      return "Artistic-1.0-cl8";
    if (code == SpdxLicense.ARTISTIC1_0PERL)
      return "Artistic-1.0-Perl";
    if (code == SpdxLicense.ARTISTIC1_0)
      return "Artistic-1.0";
    if (code == SpdxLicense.ARTISTIC2_0)
      return "Artistic-2.0";
    if (code == SpdxLicense.BAHYPH)
      return "Bahyph";
    if (code == SpdxLicense.BARR)
      return "Barr";
    if (code == SpdxLicense.BEERWARE)
      return "Beerware";
    if (code == SpdxLicense.BITTORRENT1_0)
      return "BitTorrent-1.0";
    if (code == SpdxLicense.BITTORRENT1_1)
      return "BitTorrent-1.1";
    if (code == SpdxLicense.BORCEUX)
      return "Borceux";
    if (code == SpdxLicense.BSD1CLAUSE)
      return "BSD-1-Clause";
    if (code == SpdxLicense.BSD2CLAUSEFREEBSD)
      return "BSD-2-Clause-FreeBSD";
    if (code == SpdxLicense.BSD2CLAUSENETBSD)
      return "BSD-2-Clause-NetBSD";
    if (code == SpdxLicense.BSD2CLAUSEPATENT)
      return "BSD-2-Clause-Patent";
    if (code == SpdxLicense.BSD2CLAUSE)
      return "BSD-2-Clause";
    if (code == SpdxLicense.BSD3CLAUSEATTRIBUTION)
      return "BSD-3-Clause-Attribution";
    if (code == SpdxLicense.BSD3CLAUSECLEAR)
      return "BSD-3-Clause-Clear";
    if (code == SpdxLicense.BSD3CLAUSELBNL)
      return "BSD-3-Clause-LBNL";
    if (code == SpdxLicense.BSD3CLAUSENONUCLEARLICENSE2014)
      return "BSD-3-Clause-No-Nuclear-License-2014";
    if (code == SpdxLicense.BSD3CLAUSENONUCLEARLICENSE)
      return "BSD-3-Clause-No-Nuclear-License";
    if (code == SpdxLicense.BSD3CLAUSENONUCLEARWARRANTY)
      return "BSD-3-Clause-No-Nuclear-Warranty";
    if (code == SpdxLicense.BSD3CLAUSE)
      return "BSD-3-Clause";
    if (code == SpdxLicense.BSD4CLAUSEUC)
      return "BSD-4-Clause-UC";
    if (code == SpdxLicense.BSD4CLAUSE)
      return "BSD-4-Clause";
    if (code == SpdxLicense.BSDPROTECTION)
      return "BSD-Protection";
    if (code == SpdxLicense.BSDSOURCECODE)
      return "BSD-Source-Code";
    if (code == SpdxLicense.BSL1_0)
      return "BSL-1.0";
    if (code == SpdxLicense.BZIP21_0_5)
      return "bzip2-1.0.5";
    if (code == SpdxLicense.BZIP21_0_6)
      return "bzip2-1.0.6";
    if (code == SpdxLicense.CALDERA)
      return "Caldera";
    if (code == SpdxLicense.CATOSL1_1)
      return "CATOSL-1.1";
    if (code == SpdxLicense.CCBY1_0)
      return "CC-BY-1.0";
    if (code == SpdxLicense.CCBY2_0)
      return "CC-BY-2.0";
    if (code == SpdxLicense.CCBY2_5)
      return "CC-BY-2.5";
    if (code == SpdxLicense.CCBY3_0)
      return "CC-BY-3.0";
    if (code == SpdxLicense.CCBY4_0)
      return "CC-BY-4.0";
    if (code == SpdxLicense.CCBYNC1_0)
      return "CC-BY-NC-1.0";
    if (code == SpdxLicense.CCBYNC2_0)
      return "CC-BY-NC-2.0";
    if (code == SpdxLicense.CCBYNC2_5)
      return "CC-BY-NC-2.5";
    if (code == SpdxLicense.CCBYNC3_0)
      return "CC-BY-NC-3.0";
    if (code == SpdxLicense.CCBYNC4_0)
      return "CC-BY-NC-4.0";
    if (code == SpdxLicense.CCBYNCND1_0)
      return "CC-BY-NC-ND-1.0";
    if (code == SpdxLicense.CCBYNCND2_0)
      return "CC-BY-NC-ND-2.0";
    if (code == SpdxLicense.CCBYNCND2_5)
      return "CC-BY-NC-ND-2.5";
    if (code == SpdxLicense.CCBYNCND3_0)
      return "CC-BY-NC-ND-3.0";
    if (code == SpdxLicense.CCBYNCND4_0)
      return "CC-BY-NC-ND-4.0";
    if (code == SpdxLicense.CCBYNCSA1_0)
      return "CC-BY-NC-SA-1.0";
    if (code == SpdxLicense.CCBYNCSA2_0)
      return "CC-BY-NC-SA-2.0";
    if (code == SpdxLicense.CCBYNCSA2_5)
      return "CC-BY-NC-SA-2.5";
    if (code == SpdxLicense.CCBYNCSA3_0)
      return "CC-BY-NC-SA-3.0";
    if (code == SpdxLicense.CCBYNCSA4_0)
      return "CC-BY-NC-SA-4.0";
    if (code == SpdxLicense.CCBYND1_0)
      return "CC-BY-ND-1.0";
    if (code == SpdxLicense.CCBYND2_0)
      return "CC-BY-ND-2.0";
    if (code == SpdxLicense.CCBYND2_5)
      return "CC-BY-ND-2.5";
    if (code == SpdxLicense.CCBYND3_0)
      return "CC-BY-ND-3.0";
    if (code == SpdxLicense.CCBYND4_0)
      return "CC-BY-ND-4.0";
    if (code == SpdxLicense.CCBYSA1_0)
      return "CC-BY-SA-1.0";
    if (code == SpdxLicense.CCBYSA2_0)
      return "CC-BY-SA-2.0";
    if (code == SpdxLicense.CCBYSA2_5)
      return "CC-BY-SA-2.5";
    if (code == SpdxLicense.CCBYSA3_0)
      return "CC-BY-SA-3.0";
    if (code == SpdxLicense.CCBYSA4_0)
      return "CC-BY-SA-4.0";
    if (code == SpdxLicense.CC01_0)
      return "CC0-1.0";
    if (code == SpdxLicense.CDDL1_0)
      return "CDDL-1.0";
    if (code == SpdxLicense.CDDL1_1)
      return "CDDL-1.1";
    if (code == SpdxLicense.CDLAPERMISSIVE1_0)
      return "CDLA-Permissive-1.0";
    if (code == SpdxLicense.CDLASHARING1_0)
      return "CDLA-Sharing-1.0";
    if (code == SpdxLicense.CECILL1_0)
      return "CECILL-1.0";
    if (code == SpdxLicense.CECILL1_1)
      return "CECILL-1.1";
    if (code == SpdxLicense.CECILL2_0)
      return "CECILL-2.0";
    if (code == SpdxLicense.CECILL2_1)
      return "CECILL-2.1";
    if (code == SpdxLicense.CECILLB)
      return "CECILL-B";
    if (code == SpdxLicense.CECILLC)
      return "CECILL-C";
    if (code == SpdxLicense.CLARTISTIC)
      return "ClArtistic";
    if (code == SpdxLicense.CNRIJYTHON)
      return "CNRI-Jython";
    if (code == SpdxLicense.CNRIPYTHONGPLCOMPATIBLE)
      return "CNRI-Python-GPL-Compatible";
    if (code == SpdxLicense.CNRIPYTHON)
      return "CNRI-Python";
    if (code == SpdxLicense.CONDOR1_1)
      return "Condor-1.1";
    if (code == SpdxLicense.CPAL1_0)
      return "CPAL-1.0";
    if (code == SpdxLicense.CPL1_0)
      return "CPL-1.0";
    if (code == SpdxLicense.CPOL1_02)
      return "CPOL-1.02";
    if (code == SpdxLicense.CROSSWORD)
      return "Crossword";
    if (code == SpdxLicense.CRYSTALSTACKER)
      return "CrystalStacker";
    if (code == SpdxLicense.CUAOPL1_0)
      return "CUA-OPL-1.0";
    if (code == SpdxLicense.CUBE)
      return "Cube";
    if (code == SpdxLicense.CURL)
      return "curl";
    if (code == SpdxLicense.DFSL1_0)
      return "D-FSL-1.0";
    if (code == SpdxLicense.DIFFMARK)
      return "diffmark";
    if (code == SpdxLicense.DOC)
      return "DOC";
    if (code == SpdxLicense.DOTSEQN)
      return "Dotseqn";
    if (code == SpdxLicense.DSDP)
      return "DSDP";
    if (code == SpdxLicense.DVIPDFM)
      return "dvipdfm";
    if (code == SpdxLicense.ECL1_0)
      return "ECL-1.0";
    if (code == SpdxLicense.ECL2_0)
      return "ECL-2.0";
    if (code == SpdxLicense.EFL1_0)
      return "EFL-1.0";
    if (code == SpdxLicense.EFL2_0)
      return "EFL-2.0";
    if (code == SpdxLicense.EGENIX)
      return "eGenix";
    if (code == SpdxLicense.ENTESSA)
      return "Entessa";
    if (code == SpdxLicense.EPL1_0)
      return "EPL-1.0";
    if (code == SpdxLicense.EPL2_0)
      return "EPL-2.0";
    if (code == SpdxLicense.ERLPL1_1)
      return "ErlPL-1.1";
    if (code == SpdxLicense.EUDATAGRID)
      return "EUDatagrid";
    if (code == SpdxLicense.EUPL1_0)
      return "EUPL-1.0";
    if (code == SpdxLicense.EUPL1_1)
      return "EUPL-1.1";
    if (code == SpdxLicense.EUPL1_2)
      return "EUPL-1.2";
    if (code == SpdxLicense.EUROSYM)
      return "Eurosym";
    if (code == SpdxLicense.FAIR)
      return "Fair";
    if (code == SpdxLicense.FRAMEWORX1_0)
      return "Frameworx-1.0";
    if (code == SpdxLicense.FREEIMAGE)
      return "FreeImage";
    if (code == SpdxLicense.FSFAP)
      return "FSFAP";
    if (code == SpdxLicense.FSFUL)
      return "FSFUL";
    if (code == SpdxLicense.FSFULLR)
      return "FSFULLR";
    if (code == SpdxLicense.FTL)
      return "FTL";
    if (code == SpdxLicense.GFDL1_1ONLY)
      return "GFDL-1.1-only";
    if (code == SpdxLicense.GFDL1_1ORLATER)
      return "GFDL-1.1-or-later";
    if (code == SpdxLicense.GFDL1_2ONLY)
      return "GFDL-1.2-only";
    if (code == SpdxLicense.GFDL1_2ORLATER)
      return "GFDL-1.2-or-later";
    if (code == SpdxLicense.GFDL1_3ONLY)
      return "GFDL-1.3-only";
    if (code == SpdxLicense.GFDL1_3ORLATER)
      return "GFDL-1.3-or-later";
    if (code == SpdxLicense.GIFTWARE)
      return "Giftware";
    if (code == SpdxLicense.GL2PS)
      return "GL2PS";
    if (code == SpdxLicense.GLIDE)
      return "Glide";
    if (code == SpdxLicense.GLULXE)
      return "Glulxe";
    if (code == SpdxLicense.GNUPLOT)
      return "gnuplot";
    if (code == SpdxLicense.GPL1_0ONLY)
      return "GPL-1.0-only";
    if (code == SpdxLicense.GPL1_0ORLATER)
      return "GPL-1.0-or-later";
    if (code == SpdxLicense.GPL2_0ONLY)
      return "GPL-2.0-only";
    if (code == SpdxLicense.GPL2_0ORLATER)
      return "GPL-2.0-or-later";
    if (code == SpdxLicense.GPL3_0ONLY)
      return "GPL-3.0-only";
    if (code == SpdxLicense.GPL3_0ORLATER)
      return "GPL-3.0-or-later";
    if (code == SpdxLicense.GSOAP1_3B)
      return "gSOAP-1.3b";
    if (code == SpdxLicense.HASKELLREPORT)
      return "HaskellReport";
    if (code == SpdxLicense.HPND)
      return "HPND";
    if (code == SpdxLicense.IBMPIBS)
      return "IBM-pibs";
    if (code == SpdxLicense.ICU)
      return "ICU";
    if (code == SpdxLicense.IJG)
      return "IJG";
    if (code == SpdxLicense.IMAGEMAGICK)
      return "ImageMagick";
    if (code == SpdxLicense.IMATIX)
      return "iMatix";
    if (code == SpdxLicense.IMLIB2)
      return "Imlib2";
    if (code == SpdxLicense.INFOZIP)
      return "Info-ZIP";
    if (code == SpdxLicense.INTELACPI)
      return "Intel-ACPI";
    if (code == SpdxLicense.INTEL)
      return "Intel";
    if (code == SpdxLicense.INTERBASE1_0)
      return "Interbase-1.0";
    if (code == SpdxLicense.IPA)
      return "IPA";
    if (code == SpdxLicense.IPL1_0)
      return "IPL-1.0";
    if (code == SpdxLicense.ISC)
      return "ISC";
    if (code == SpdxLicense.JASPER2_0)
      return "JasPer-2.0";
    if (code == SpdxLicense.JSON)
      return "JSON";
    if (code == SpdxLicense.LAL1_2)
      return "LAL-1.2";
    if (code == SpdxLicense.LAL1_3)
      return "LAL-1.3";
    if (code == SpdxLicense.LATEX2E)
      return "Latex2e";
    if (code == SpdxLicense.LEPTONICA)
      return "Leptonica";
    if (code == SpdxLicense.LGPL2_0ONLY)
      return "LGPL-2.0-only";
    if (code == SpdxLicense.LGPL2_0ORLATER)
      return "LGPL-2.0-or-later";
    if (code == SpdxLicense.LGPL2_1ONLY)
      return "LGPL-2.1-only";
    if (code == SpdxLicense.LGPL2_1ORLATER)
      return "LGPL-2.1-or-later";
    if (code == SpdxLicense.LGPL3_0ONLY)
      return "LGPL-3.0-only";
    if (code == SpdxLicense.LGPL3_0ORLATER)
      return "LGPL-3.0-or-later";
    if (code == SpdxLicense.LGPLLR)
      return "LGPLLR";
    if (code == SpdxLicense.LIBPNG)
      return "Libpng";
    if (code == SpdxLicense.LIBTIFF)
      return "libtiff";
    if (code == SpdxLicense.LILIQP1_1)
      return "LiLiQ-P-1.1";
    if (code == SpdxLicense.LILIQR1_1)
      return "LiLiQ-R-1.1";
    if (code == SpdxLicense.LILIQRPLUS1_1)
      return "LiLiQ-Rplus-1.1";
    if (code == SpdxLicense.LINUXOPENIB)
      return "Linux-OpenIB";
    if (code == SpdxLicense.LPL1_0)
      return "LPL-1.0";
    if (code == SpdxLicense.LPL1_02)
      return "LPL-1.02";
    if (code == SpdxLicense.LPPL1_0)
      return "LPPL-1.0";
    if (code == SpdxLicense.LPPL1_1)
      return "LPPL-1.1";
    if (code == SpdxLicense.LPPL1_2)
      return "LPPL-1.2";
    if (code == SpdxLicense.LPPL1_3A)
      return "LPPL-1.3a";
    if (code == SpdxLicense.LPPL1_3C)
      return "LPPL-1.3c";
    if (code == SpdxLicense.MAKEINDEX)
      return "MakeIndex";
    if (code == SpdxLicense.MIROS)
      return "MirOS";
    if (code == SpdxLicense.MIT0)
      return "MIT-0";
    if (code == SpdxLicense.MITADVERTISING)
      return "MIT-advertising";
    if (code == SpdxLicense.MITCMU)
      return "MIT-CMU";
    if (code == SpdxLicense.MITENNA)
      return "MIT-enna";
    if (code == SpdxLicense.MITFEH)
      return "MIT-feh";
    if (code == SpdxLicense.MIT)
      return "MIT";
    if (code == SpdxLicense.MITNFA)
      return "MITNFA";
    if (code == SpdxLicense.MOTOSOTO)
      return "Motosoto";
    if (code == SpdxLicense.MPICH2)
      return "mpich2";
    if (code == SpdxLicense.MPL1_0)
      return "MPL-1.0";
    if (code == SpdxLicense.MPL1_1)
      return "MPL-1.1";
    if (code == SpdxLicense.MPL2_0NOCOPYLEFTEXCEPTION)
      return "MPL-2.0-no-copyleft-exception";
    if (code == SpdxLicense.MPL2_0)
      return "MPL-2.0";
    if (code == SpdxLicense.MSPL)
      return "MS-PL";
    if (code == SpdxLicense.MSRL)
      return "MS-RL";
    if (code == SpdxLicense.MTLL)
      return "MTLL";
    if (code == SpdxLicense.MULTICS)
      return "Multics";
    if (code == SpdxLicense.MUP)
      return "Mup";
    if (code == SpdxLicense.NASA1_3)
      return "NASA-1.3";
    if (code == SpdxLicense.NAUMEN)
      return "Naumen";
    if (code == SpdxLicense.NBPL1_0)
      return "NBPL-1.0";
    if (code == SpdxLicense.NCSA)
      return "NCSA";
    if (code == SpdxLicense.NETSNMP)
      return "Net-SNMP";
    if (code == SpdxLicense.NETCDF)
      return "NetCDF";
    if (code == SpdxLicense.NEWSLETR)
      return "Newsletr";
    if (code == SpdxLicense.NGPL)
      return "NGPL";
    if (code == SpdxLicense.NLOD1_0)
      return "NLOD-1.0";
    if (code == SpdxLicense.NLPL)
      return "NLPL";
    if (code == SpdxLicense.NOKIA)
      return "Nokia";
    if (code == SpdxLicense.NOSL)
      return "NOSL";
    if (code == SpdxLicense.NOWEB)
      return "Noweb";
    if (code == SpdxLicense.NPL1_0)
      return "NPL-1.0";
    if (code == SpdxLicense.NPL1_1)
      return "NPL-1.1";
    if (code == SpdxLicense.NPOSL3_0)
      return "NPOSL-3.0";
    if (code == SpdxLicense.NRL)
      return "NRL";
    if (code == SpdxLicense.NTP)
      return "NTP";
    if (code == SpdxLicense.OCCTPL)
      return "OCCT-PL";
    if (code == SpdxLicense.OCLC2_0)
      return "OCLC-2.0";
    if (code == SpdxLicense.ODBL1_0)
      return "ODbL-1.0";
    if (code == SpdxLicense.OFL1_0)
      return "OFL-1.0";
    if (code == SpdxLicense.OFL1_1)
      return "OFL-1.1";
    if (code == SpdxLicense.OGTSL)
      return "OGTSL";
    if (code == SpdxLicense.OLDAP1_1)
      return "OLDAP-1.1";
    if (code == SpdxLicense.OLDAP1_2)
      return "OLDAP-1.2";
    if (code == SpdxLicense.OLDAP1_3)
      return "OLDAP-1.3";
    if (code == SpdxLicense.OLDAP1_4)
      return "OLDAP-1.4";
    if (code == SpdxLicense.OLDAP2_0_1)
      return "OLDAP-2.0.1";
    if (code == SpdxLicense.OLDAP2_0)
      return "OLDAP-2.0";
    if (code == SpdxLicense.OLDAP2_1)
      return "OLDAP-2.1";
    if (code == SpdxLicense.OLDAP2_2_1)
      return "OLDAP-2.2.1";
    if (code == SpdxLicense.OLDAP2_2_2)
      return "OLDAP-2.2.2";
    if (code == SpdxLicense.OLDAP2_2)
      return "OLDAP-2.2";
    if (code == SpdxLicense.OLDAP2_3)
      return "OLDAP-2.3";
    if (code == SpdxLicense.OLDAP2_4)
      return "OLDAP-2.4";
    if (code == SpdxLicense.OLDAP2_5)
      return "OLDAP-2.5";
    if (code == SpdxLicense.OLDAP2_6)
      return "OLDAP-2.6";
    if (code == SpdxLicense.OLDAP2_7)
      return "OLDAP-2.7";
    if (code == SpdxLicense.OLDAP2_8)
      return "OLDAP-2.8";
    if (code == SpdxLicense.OML)
      return "OML";
    if (code == SpdxLicense.OPENSSL)
      return "OpenSSL";
    if (code == SpdxLicense.OPL1_0)
      return "OPL-1.0";
    if (code == SpdxLicense.OSETPL2_1)
      return "OSET-PL-2.1";
    if (code == SpdxLicense.OSL1_0)
      return "OSL-1.0";
    if (code == SpdxLicense.OSL1_1)
      return "OSL-1.1";
    if (code == SpdxLicense.OSL2_0)
      return "OSL-2.0";
    if (code == SpdxLicense.OSL2_1)
      return "OSL-2.1";
    if (code == SpdxLicense.OSL3_0)
      return "OSL-3.0";
    if (code == SpdxLicense.PDDL1_0)
      return "PDDL-1.0";
    if (code == SpdxLicense.PHP3_0)
      return "PHP-3.0";
    if (code == SpdxLicense.PHP3_01)
      return "PHP-3.01";
    if (code == SpdxLicense.PLEXUS)
      return "Plexus";
    if (code == SpdxLicense.POSTGRESQL)
      return "PostgreSQL";
    if (code == SpdxLicense.PSFRAG)
      return "psfrag";
    if (code == SpdxLicense.PSUTILS)
      return "psutils";
    if (code == SpdxLicense.PYTHON2_0)
      return "Python-2.0";
    if (code == SpdxLicense.QHULL)
      return "Qhull";
    if (code == SpdxLicense.QPL1_0)
      return "QPL-1.0";
    if (code == SpdxLicense.RDISC)
      return "Rdisc";
    if (code == SpdxLicense.RHECOS1_1)
      return "RHeCos-1.1";
    if (code == SpdxLicense.RPL1_1)
      return "RPL-1.1";
    if (code == SpdxLicense.RPL1_5)
      return "RPL-1.5";
    if (code == SpdxLicense.RPSL1_0)
      return "RPSL-1.0";
    if (code == SpdxLicense.RSAMD)
      return "RSA-MD";
    if (code == SpdxLicense.RSCPL)
      return "RSCPL";
    if (code == SpdxLicense.RUBY)
      return "Ruby";
    if (code == SpdxLicense.SAXPD)
      return "SAX-PD";
    if (code == SpdxLicense.SAXPATH)
      return "Saxpath";
    if (code == SpdxLicense.SCEA)
      return "SCEA";
    if (code == SpdxLicense.SENDMAIL)
      return "Sendmail";
    if (code == SpdxLicense.SGIB1_0)
      return "SGI-B-1.0";
    if (code == SpdxLicense.SGIB1_1)
      return "SGI-B-1.1";
    if (code == SpdxLicense.SGIB2_0)
      return "SGI-B-2.0";
    if (code == SpdxLicense.SIMPL2_0)
      return "SimPL-2.0";
    if (code == SpdxLicense.SISSL1_2)
      return "SISSL-1.2";
    if (code == SpdxLicense.SISSL)
      return "SISSL";
    if (code == SpdxLicense.SLEEPYCAT)
      return "Sleepycat";
    if (code == SpdxLicense.SMLNJ)
      return "SMLNJ";
    if (code == SpdxLicense.SMPPL)
      return "SMPPL";
    if (code == SpdxLicense.SNIA)
      return "SNIA";
    if (code == SpdxLicense.SPENCER86)
      return "Spencer-86";
    if (code == SpdxLicense.SPENCER94)
      return "Spencer-94";
    if (code == SpdxLicense.SPENCER99)
      return "Spencer-99";
    if (code == SpdxLicense.SPL1_0)
      return "SPL-1.0";
    if (code == SpdxLicense.SUGARCRM1_1_3)
      return "SugarCRM-1.1.3";
    if (code == SpdxLicense.SWL)
      return "SWL";
    if (code == SpdxLicense.TCL)
      return "TCL";
    if (code == SpdxLicense.TCPWRAPPERS)
      return "TCP-wrappers";
    if (code == SpdxLicense.TMATE)
      return "TMate";
    if (code == SpdxLicense.TORQUE1_1)
      return "TORQUE-1.1";
    if (code == SpdxLicense.TOSL)
      return "TOSL";
    if (code == SpdxLicense.UNICODEDFS2015)
      return "Unicode-DFS-2015";
    if (code == SpdxLicense.UNICODEDFS2016)
      return "Unicode-DFS-2016";
    if (code == SpdxLicense.UNICODETOU)
      return "Unicode-TOU";
    if (code == SpdxLicense.UNLICENSE)
      return "Unlicense";
    if (code == SpdxLicense.UPL1_0)
      return "UPL-1.0";
    if (code == SpdxLicense.VIM)
      return "Vim";
    if (code == SpdxLicense.VOSTROM)
      return "VOSTROM";
    if (code == SpdxLicense.VSL1_0)
      return "VSL-1.0";
    if (code == SpdxLicense.W3C19980720)
      return "W3C-19980720";
    if (code == SpdxLicense.W3C20150513)
      return "W3C-20150513";
    if (code == SpdxLicense.W3C)
      return "W3C";
    if (code == SpdxLicense.WATCOM1_0)
      return "Watcom-1.0";
    if (code == SpdxLicense.WSUIPA)
      return "Wsuipa";
    if (code == SpdxLicense.WTFPL)
      return "WTFPL";
    if (code == SpdxLicense.X11)
      return "X11";
    if (code == SpdxLicense.XEROX)
      return "Xerox";
    if (code == SpdxLicense.XFREE861_1)
      return "XFree86-1.1";
    if (code == SpdxLicense.XINETD)
      return "xinetd";
    if (code == SpdxLicense.XNET)
      return "Xnet";
    if (code == SpdxLicense.XPP)
      return "xpp";
    if (code == SpdxLicense.XSKAT)
      return "XSkat";
    if (code == SpdxLicense.YPL1_0)
      return "YPL-1.0";
    if (code == SpdxLicense.YPL1_1)
      return "YPL-1.1";
    if (code == SpdxLicense.ZED)
      return "Zed";
    if (code == SpdxLicense.ZEND2_0)
      return "Zend-2.0";
    if (code == SpdxLicense.ZIMBRA1_3)
      return "Zimbra-1.3";
    if (code == SpdxLicense.ZIMBRA1_4)
      return "Zimbra-1.4";
    if (code == SpdxLicense.ZLIBACKNOWLEDGEMENT)
      return "zlib-acknowledgement";
    if (code == SpdxLicense.ZLIB)
      return "Zlib";
    if (code == SpdxLicense.ZPL1_1)
      return "ZPL-1.1";
    if (code == SpdxLicense.ZPL2_0)
      return "ZPL-2.0";
    if (code == SpdxLicense.ZPL2_1)
      return "ZPL-2.1";
    return "?";
  }

    public String toSystem(SpdxLicense code) {
      return code.getSystem();
      }

}

