package org.hl7.fhir.r4.model;

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

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
 */
@ResourceDef(name="ImplementationGuide", profile="http://hl7.org/fhir/Profile/ImplementationGuide")
@ChildOrder(names={"url", "version", "name", "title", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "copyright", "packageId", "license", "fhirVersion", "dependsOn", "global", "definition", "manifest"})
public class ImplementationGuide extends MetadataResource {

    public enum SPDXLicense {
        /**
         * Not an open source license.
         */
        NOTOPENSOURCE, 
        /**
         * BSD Zero Clause License.
         */
        _0BSD, 
        /**
         * Attribution Assurance License.
         */
        AAL, 
        /**
         * Abstyles License.
         */
        ABSTYLES, 
        /**
         * Adobe Systems Incorporated Source Code License Agreement.
         */
        ADOBE2006, 
        /**
         * Adobe Glyph List License.
         */
        ADOBEGLYPH, 
        /**
         * Amazon Digital Services License.
         */
        ADSL, 
        /**
         * Academic Free License v1.1.
         */
        AFL1_1, 
        /**
         * Academic Free License v1.2.
         */
        AFL1_2, 
        /**
         * Academic Free License v2.0.
         */
        AFL2_0, 
        /**
         * Academic Free License v2.1.
         */
        AFL2_1, 
        /**
         * Academic Free License v3.0.
         */
        AFL3_0, 
        /**
         * Afmparse License.
         */
        AFMPARSE, 
        /**
         * Affero General Public License v1.0 only.
         */
        AGPL1_0ONLY, 
        /**
         * Affero General Public License v1.0 or later.
         */
        AGPL1_0ORLATER, 
        /**
         * GNU Affero General Public License v3.0 only.
         */
        AGPL3_0ONLY, 
        /**
         * GNU Affero General Public License v3.0 or later.
         */
        AGPL3_0ORLATER, 
        /**
         * Aladdin Free Public License.
         */
        ALADDIN, 
        /**
         * AMD's plpa_map.c License.
         */
        AMDPLPA, 
        /**
         * Apple MIT License.
         */
        AML, 
        /**
         * Academy of Motion Picture Arts and Sciences BSD.
         */
        AMPAS, 
        /**
         * ANTLR Software Rights Notice.
         */
        ANTLRPD, 
        /**
         * Apache License 1.0.
         */
        APACHE1_0, 
        /**
         * Apache License 1.1.
         */
        APACHE1_1, 
        /**
         * Apache License 2.0.
         */
        APACHE2_0, 
        /**
         * Adobe Postscript AFM License.
         */
        APAFML, 
        /**
         * Adaptive Public License 1.0.
         */
        APL1_0, 
        /**
         * Apple Public Source License 1.0.
         */
        APSL1_0, 
        /**
         * Apple Public Source License 1.1.
         */
        APSL1_1, 
        /**
         * Apple Public Source License 1.2.
         */
        APSL1_2, 
        /**
         * Apple Public Source License 2.0.
         */
        APSL2_0, 
        /**
         * Artistic License 1.0 w/clause 8.
         */
        ARTISTIC1_0CL8, 
        /**
         * Artistic License 1.0 (Perl).
         */
        ARTISTIC1_0PERL, 
        /**
         * Artistic License 1.0.
         */
        ARTISTIC1_0, 
        /**
         * Artistic License 2.0.
         */
        ARTISTIC2_0, 
        /**
         * Bahyph License.
         */
        BAHYPH, 
        /**
         * Barr License.
         */
        BARR, 
        /**
         * Beerware License.
         */
        BEERWARE, 
        /**
         * BitTorrent Open Source License v1.0.
         */
        BITTORRENT1_0, 
        /**
         * BitTorrent Open Source License v1.1.
         */
        BITTORRENT1_1, 
        /**
         * Borceux license.
         */
        BORCEUX, 
        /**
         * BSD 1-Clause License.
         */
        BSD1CLAUSE, 
        /**
         * BSD 2-Clause FreeBSD License.
         */
        BSD2CLAUSEFREEBSD, 
        /**
         * BSD 2-Clause NetBSD License.
         */
        BSD2CLAUSENETBSD, 
        /**
         * BSD-2-Clause Plus Patent License.
         */
        BSD2CLAUSEPATENT, 
        /**
         * BSD 2-Clause "Simplified" License.
         */
        BSD2CLAUSE, 
        /**
         * BSD with attribution.
         */
        BSD3CLAUSEATTRIBUTION, 
        /**
         * BSD 3-Clause Clear License.
         */
        BSD3CLAUSECLEAR, 
        /**
         * Lawrence Berkeley National Labs BSD variant license.
         */
        BSD3CLAUSELBNL, 
        /**
         * BSD 3-Clause No Nuclear License 2014.
         */
        BSD3CLAUSENONUCLEARLICENSE2014, 
        /**
         * BSD 3-Clause No Nuclear License.
         */
        BSD3CLAUSENONUCLEARLICENSE, 
        /**
         * BSD 3-Clause No Nuclear Warranty.
         */
        BSD3CLAUSENONUCLEARWARRANTY, 
        /**
         * BSD 3-Clause "New" or "Revised" License.
         */
        BSD3CLAUSE, 
        /**
         * BSD-4-Clause (University of California-Specific).
         */
        BSD4CLAUSEUC, 
        /**
         * BSD 4-Clause "Original" or "Old" License.
         */
        BSD4CLAUSE, 
        /**
         * BSD Protection License.
         */
        BSDPROTECTION, 
        /**
         * BSD Source Code Attribution.
         */
        BSDSOURCECODE, 
        /**
         * Boost Software License 1.0.
         */
        BSL1_0, 
        /**
         * bzip2 and libbzip2 License v1.0.5.
         */
        BZIP21_0_5, 
        /**
         * bzip2 and libbzip2 License v1.0.6.
         */
        BZIP21_0_6, 
        /**
         * Caldera License.
         */
        CALDERA, 
        /**
         * Computer Associates Trusted Open Source License 1.1.
         */
        CATOSL1_1, 
        /**
         * Creative Commons Attribution 1.0 Generic.
         */
        CCBY1_0, 
        /**
         * Creative Commons Attribution 2.0 Generic.
         */
        CCBY2_0, 
        /**
         * Creative Commons Attribution 2.5 Generic.
         */
        CCBY2_5, 
        /**
         * Creative Commons Attribution 3.0 Unported.
         */
        CCBY3_0, 
        /**
         * Creative Commons Attribution 4.0 International.
         */
        CCBY4_0, 
        /**
         * Creative Commons Attribution Non Commercial 1.0 Generic.
         */
        CCBYNC1_0, 
        /**
         * Creative Commons Attribution Non Commercial 2.0 Generic.
         */
        CCBYNC2_0, 
        /**
         * Creative Commons Attribution Non Commercial 2.5 Generic.
         */
        CCBYNC2_5, 
        /**
         * Creative Commons Attribution Non Commercial 3.0 Unported.
         */
        CCBYNC3_0, 
        /**
         * Creative Commons Attribution Non Commercial 4.0 International.
         */
        CCBYNC4_0, 
        /**
         * Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic.
         */
        CCBYNCND1_0, 
        /**
         * Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic.
         */
        CCBYNCND2_0, 
        /**
         * Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic.
         */
        CCBYNCND2_5, 
        /**
         * Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported.
         */
        CCBYNCND3_0, 
        /**
         * Creative Commons Attribution Non Commercial No Derivatives 4.0 International.
         */
        CCBYNCND4_0, 
        /**
         * Creative Commons Attribution Non Commercial Share Alike 1.0 Generic.
         */
        CCBYNCSA1_0, 
        /**
         * Creative Commons Attribution Non Commercial Share Alike 2.0 Generic.
         */
        CCBYNCSA2_0, 
        /**
         * Creative Commons Attribution Non Commercial Share Alike 2.5 Generic.
         */
        CCBYNCSA2_5, 
        /**
         * Creative Commons Attribution Non Commercial Share Alike 3.0 Unported.
         */
        CCBYNCSA3_0, 
        /**
         * Creative Commons Attribution Non Commercial Share Alike 4.0 International.
         */
        CCBYNCSA4_0, 
        /**
         * Creative Commons Attribution No Derivatives 1.0 Generic.
         */
        CCBYND1_0, 
        /**
         * Creative Commons Attribution No Derivatives 2.0 Generic.
         */
        CCBYND2_0, 
        /**
         * Creative Commons Attribution No Derivatives 2.5 Generic.
         */
        CCBYND2_5, 
        /**
         * Creative Commons Attribution No Derivatives 3.0 Unported.
         */
        CCBYND3_0, 
        /**
         * Creative Commons Attribution No Derivatives 4.0 International.
         */
        CCBYND4_0, 
        /**
         * Creative Commons Attribution Share Alike 1.0 Generic.
         */
        CCBYSA1_0, 
        /**
         * Creative Commons Attribution Share Alike 2.0 Generic.
         */
        CCBYSA2_0, 
        /**
         * Creative Commons Attribution Share Alike 2.5 Generic.
         */
        CCBYSA2_5, 
        /**
         * Creative Commons Attribution Share Alike 3.0 Unported.
         */
        CCBYSA3_0, 
        /**
         * Creative Commons Attribution Share Alike 4.0 International.
         */
        CCBYSA4_0, 
        /**
         * Creative Commons Zero v1.0 Universal.
         */
        CC01_0, 
        /**
         * Common Development and Distribution License 1.0.
         */
        CDDL1_0, 
        /**
         * Common Development and Distribution License 1.1.
         */
        CDDL1_1, 
        /**
         * Community Data License Agreement Permissive 1.0.
         */
        CDLAPERMISSIVE1_0, 
        /**
         * Community Data License Agreement Sharing 1.0.
         */
        CDLASHARING1_0, 
        /**
         * CeCILL Free Software License Agreement v1.0.
         */
        CECILL1_0, 
        /**
         * CeCILL Free Software License Agreement v1.1.
         */
        CECILL1_1, 
        /**
         * CeCILL Free Software License Agreement v2.0.
         */
        CECILL2_0, 
        /**
         * CeCILL Free Software License Agreement v2.1.
         */
        CECILL2_1, 
        /**
         * CeCILL-B Free Software License Agreement.
         */
        CECILLB, 
        /**
         * CeCILL-C Free Software License Agreement.
         */
        CECILLC, 
        /**
         * Clarified Artistic License.
         */
        CLARTISTIC, 
        /**
         * CNRI Jython License.
         */
        CNRIJYTHON, 
        /**
         * CNRI Python Open Source GPL Compatible License Agreement.
         */
        CNRIPYTHONGPLCOMPATIBLE, 
        /**
         * CNRI Python License.
         */
        CNRIPYTHON, 
        /**
         * Condor Public License v1.1.
         */
        CONDOR1_1, 
        /**
         * Common Public Attribution License 1.0.
         */
        CPAL1_0, 
        /**
         * Common Public License 1.0.
         */
        CPL1_0, 
        /**
         * Code Project Open License 1.02.
         */
        CPOL1_02, 
        /**
         * Crossword License.
         */
        CROSSWORD, 
        /**
         * CrystalStacker License.
         */
        CRYSTALSTACKER, 
        /**
         * CUA Office Public License v1.0.
         */
        CUAOPL1_0, 
        /**
         * Cube License.
         */
        CUBE, 
        /**
         * curl License.
         */
        CURL, 
        /**
         * Deutsche Freie Software Lizenz.
         */
        DFSL1_0, 
        /**
         * diffmark license.
         */
        DIFFMARK, 
        /**
         * DOC License.
         */
        DOC, 
        /**
         * Dotseqn License.
         */
        DOTSEQN, 
        /**
         * DSDP License.
         */
        DSDP, 
        /**
         * dvipdfm License.
         */
        DVIPDFM, 
        /**
         * Educational Community License v1.0.
         */
        ECL1_0, 
        /**
         * Educational Community License v2.0.
         */
        ECL2_0, 
        /**
         * Eiffel Forum License v1.0.
         */
        EFL1_0, 
        /**
         * Eiffel Forum License v2.0.
         */
        EFL2_0, 
        /**
         * eGenix.com Public License 1.1.0.
         */
        EGENIX, 
        /**
         * Entessa Public License v1.0.
         */
        ENTESSA, 
        /**
         * Eclipse Public License 1.0.
         */
        EPL1_0, 
        /**
         * Eclipse Public License 2.0.
         */
        EPL2_0, 
        /**
         * Erlang Public License v1.1.
         */
        ERLPL1_1, 
        /**
         * EU DataGrid Software License.
         */
        EUDATAGRID, 
        /**
         * European Union Public License 1.0.
         */
        EUPL1_0, 
        /**
         * European Union Public License 1.1.
         */
        EUPL1_1, 
        /**
         * European Union Public License 1.2.
         */
        EUPL1_2, 
        /**
         * Eurosym License.
         */
        EUROSYM, 
        /**
         * Fair License.
         */
        FAIR, 
        /**
         * Frameworx Open License 1.0.
         */
        FRAMEWORX1_0, 
        /**
         * FreeImage Public License v1.0.
         */
        FREEIMAGE, 
        /**
         * FSF All Permissive License.
         */
        FSFAP, 
        /**
         * FSF Unlimited License.
         */
        FSFUL, 
        /**
         * FSF Unlimited License (with License Retention).
         */
        FSFULLR, 
        /**
         * Freetype Project License.
         */
        FTL, 
        /**
         * GNU Free Documentation License v1.1 only.
         */
        GFDL1_1ONLY, 
        /**
         * GNU Free Documentation License v1.1 or later.
         */
        GFDL1_1ORLATER, 
        /**
         * GNU Free Documentation License v1.2 only.
         */
        GFDL1_2ONLY, 
        /**
         * GNU Free Documentation License v1.2 or later.
         */
        GFDL1_2ORLATER, 
        /**
         * GNU Free Documentation License v1.3 only.
         */
        GFDL1_3ONLY, 
        /**
         * GNU Free Documentation License v1.3 or later.
         */
        GFDL1_3ORLATER, 
        /**
         * Giftware License.
         */
        GIFTWARE, 
        /**
         * GL2PS License.
         */
        GL2PS, 
        /**
         * 3dfx Glide License.
         */
        GLIDE, 
        /**
         * Glulxe License.
         */
        GLULXE, 
        /**
         * gnuplot License.
         */
        GNUPLOT, 
        /**
         * GNU General Public License v1.0 only.
         */
        GPL1_0ONLY, 
        /**
         * GNU General Public License v1.0 or later.
         */
        GPL1_0ORLATER, 
        /**
         * GNU General Public License v2.0 only.
         */
        GPL2_0ONLY, 
        /**
         * GNU General Public License v2.0 or later.
         */
        GPL2_0ORLATER, 
        /**
         * GNU General Public License v3.0 only.
         */
        GPL3_0ONLY, 
        /**
         * GNU General Public License v3.0 or later.
         */
        GPL3_0ORLATER, 
        /**
         * gSOAP Public License v1.3b.
         */
        GSOAP1_3B, 
        /**
         * Haskell Language Report License.
         */
        HASKELLREPORT, 
        /**
         * Historical Permission Notice and Disclaimer.
         */
        HPND, 
        /**
         * IBM PowerPC Initialization and Boot Software.
         */
        IBMPIBS, 
        /**
         * ICU License.
         */
        ICU, 
        /**
         * Independent JPEG Group License.
         */
        IJG, 
        /**
         * ImageMagick License.
         */
        IMAGEMAGICK, 
        /**
         * iMatix Standard Function Library Agreement.
         */
        IMATIX, 
        /**
         * Imlib2 License.
         */
        IMLIB2, 
        /**
         * Info-ZIP License.
         */
        INFOZIP, 
        /**
         * Intel ACPI Software License Agreement.
         */
        INTELACPI, 
        /**
         * Intel Open Source License.
         */
        INTEL, 
        /**
         * Interbase Public License v1.0.
         */
        INTERBASE1_0, 
        /**
         * IPA Font License.
         */
        IPA, 
        /**
         * IBM Public License v1.0.
         */
        IPL1_0, 
        /**
         * ISC License.
         */
        ISC, 
        /**
         * JasPer License.
         */
        JASPER2_0, 
        /**
         * JSON License.
         */
        JSON, 
        /**
         * Licence Art Libre 1.2.
         */
        LAL1_2, 
        /**
         * Licence Art Libre 1.3.
         */
        LAL1_3, 
        /**
         * Latex2e License.
         */
        LATEX2E, 
        /**
         * Leptonica License.
         */
        LEPTONICA, 
        /**
         * GNU Library General Public License v2 only.
         */
        LGPL2_0ONLY, 
        /**
         * GNU Library General Public License v2 or later.
         */
        LGPL2_0ORLATER, 
        /**
         * GNU Lesser General Public License v2.1 only.
         */
        LGPL2_1ONLY, 
        /**
         * GNU Lesser General Public License v2.1 or later.
         */
        LGPL2_1ORLATER, 
        /**
         * GNU Lesser General Public License v3.0 only.
         */
        LGPL3_0ONLY, 
        /**
         * GNU Lesser General Public License v3.0 or later.
         */
        LGPL3_0ORLATER, 
        /**
         * Lesser General Public License For Linguistic Resources.
         */
        LGPLLR, 
        /**
         * libpng License.
         */
        LIBPNG, 
        /**
         * libtiff License.
         */
        LIBTIFF, 
        /**
         * Licence Libre du Québec – Permissive version 1.1.
         */
        LILIQP1_1, 
        /**
         * Licence Libre du Québec – Réciprocité version 1.1.
         */
        LILIQR1_1, 
        /**
         * Licence Libre du Québec – Réciprocité forte version 1.1.
         */
        LILIQRPLUS1_1, 
        /**
         * Linux Kernel Variant of OpenIB.org license.
         */
        LINUXOPENIB, 
        /**
         * Lucent Public License Version 1.0.
         */
        LPL1_0, 
        /**
         * Lucent Public License v1.02.
         */
        LPL1_02, 
        /**
         * LaTeX Project Public License v1.0.
         */
        LPPL1_0, 
        /**
         * LaTeX Project Public License v1.1.
         */
        LPPL1_1, 
        /**
         * LaTeX Project Public License v1.2.
         */
        LPPL1_2, 
        /**
         * LaTeX Project Public License v1.3a.
         */
        LPPL1_3A, 
        /**
         * LaTeX Project Public License v1.3c.
         */
        LPPL1_3C, 
        /**
         * MakeIndex License.
         */
        MAKEINDEX, 
        /**
         * MirOS License.
         */
        MIROS, 
        /**
         * MIT No Attribution.
         */
        MIT0, 
        /**
         * Enlightenment License (e16).
         */
        MITADVERTISING, 
        /**
         * CMU License.
         */
        MITCMU, 
        /**
         * enna License.
         */
        MITENNA, 
        /**
         * feh License.
         */
        MITFEH, 
        /**
         * MIT License.
         */
        MIT, 
        /**
         * MIT +no-false-attribs license.
         */
        MITNFA, 
        /**
         * Motosoto License.
         */
        MOTOSOTO, 
        /**
         * mpich2 License.
         */
        MPICH2, 
        /**
         * Mozilla Public License 1.0.
         */
        MPL1_0, 
        /**
         * Mozilla Public License 1.1.
         */
        MPL1_1, 
        /**
         * Mozilla Public License 2.0 (no copyleft exception).
         */
        MPL2_0NOCOPYLEFTEXCEPTION, 
        /**
         * Mozilla Public License 2.0.
         */
        MPL2_0, 
        /**
         * Microsoft Public License.
         */
        MSPL, 
        /**
         * Microsoft Reciprocal License.
         */
        MSRL, 
        /**
         * Matrix Template Library License.
         */
        MTLL, 
        /**
         * Multics License.
         */
        MULTICS, 
        /**
         * Mup License.
         */
        MUP, 
        /**
         * NASA Open Source Agreement 1.3.
         */
        NASA1_3, 
        /**
         * Naumen Public License.
         */
        NAUMEN, 
        /**
         * Net Boolean Public License v1.
         */
        NBPL1_0, 
        /**
         * University of Illinois/NCSA Open Source License.
         */
        NCSA, 
        /**
         * Net-SNMP License.
         */
        NETSNMP, 
        /**
         * NetCDF license.
         */
        NETCDF, 
        /**
         * Newsletr License.
         */
        NEWSLETR, 
        /**
         * Nethack General Public License.
         */
        NGPL, 
        /**
         * Norwegian Licence for Open Government Data.
         */
        NLOD1_0, 
        /**
         * No Limit Public License.
         */
        NLPL, 
        /**
         * Nokia Open Source License.
         */
        NOKIA, 
        /**
         * Netizen Open Source License.
         */
        NOSL, 
        /**
         * Noweb License.
         */
        NOWEB, 
        /**
         * Netscape Public License v1.0.
         */
        NPL1_0, 
        /**
         * Netscape Public License v1.1.
         */
        NPL1_1, 
        /**
         * Non-Profit Open Software License 3.0.
         */
        NPOSL3_0, 
        /**
         * NRL License.
         */
        NRL, 
        /**
         * NTP License.
         */
        NTP, 
        /**
         * Open CASCADE Technology Public License.
         */
        OCCTPL, 
        /**
         * OCLC Research Public License 2.0.
         */
        OCLC2_0, 
        /**
         * ODC Open Database License v1.0.
         */
        ODBL1_0, 
        /**
         * SIL Open Font License 1.0.
         */
        OFL1_0, 
        /**
         * SIL Open Font License 1.1.
         */
        OFL1_1, 
        /**
         * Open Group Test Suite License.
         */
        OGTSL, 
        /**
         * Open LDAP Public License v1.1.
         */
        OLDAP1_1, 
        /**
         * Open LDAP Public License v1.2.
         */
        OLDAP1_2, 
        /**
         * Open LDAP Public License v1.3.
         */
        OLDAP1_3, 
        /**
         * Open LDAP Public License v1.4.
         */
        OLDAP1_4, 
        /**
         * Open LDAP Public License v2.0.1.
         */
        OLDAP2_0_1, 
        /**
         * Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B).
         */
        OLDAP2_0, 
        /**
         * Open LDAP Public License v2.1.
         */
        OLDAP2_1, 
        /**
         * Open LDAP Public License v2.2.1.
         */
        OLDAP2_2_1, 
        /**
         * Open LDAP Public License 2.2.2.
         */
        OLDAP2_2_2, 
        /**
         * Open LDAP Public License v2.2.
         */
        OLDAP2_2, 
        /**
         * Open LDAP Public License v2.3.
         */
        OLDAP2_3, 
        /**
         * Open LDAP Public License v2.4.
         */
        OLDAP2_4, 
        /**
         * Open LDAP Public License v2.5.
         */
        OLDAP2_5, 
        /**
         * Open LDAP Public License v2.6.
         */
        OLDAP2_6, 
        /**
         * Open LDAP Public License v2.7.
         */
        OLDAP2_7, 
        /**
         * Open LDAP Public License v2.8.
         */
        OLDAP2_8, 
        /**
         * Open Market License.
         */
        OML, 
        /**
         * OpenSSL License.
         */
        OPENSSL, 
        /**
         * Open Public License v1.0.
         */
        OPL1_0, 
        /**
         * OSET Public License version 2.1.
         */
        OSETPL2_1, 
        /**
         * Open Software License 1.0.
         */
        OSL1_0, 
        /**
         * Open Software License 1.1.
         */
        OSL1_1, 
        /**
         * Open Software License 2.0.
         */
        OSL2_0, 
        /**
         * Open Software License 2.1.
         */
        OSL2_1, 
        /**
         * Open Software License 3.0.
         */
        OSL3_0, 
        /**
         * ODC Public Domain Dedication & License 1.0.
         */
        PDDL1_0, 
        /**
         * PHP License v3.0.
         */
        PHP3_0, 
        /**
         * PHP License v3.01.
         */
        PHP3_01, 
        /**
         * Plexus Classworlds License.
         */
        PLEXUS, 
        /**
         * PostgreSQL License.
         */
        POSTGRESQL, 
        /**
         * psfrag License.
         */
        PSFRAG, 
        /**
         * psutils License.
         */
        PSUTILS, 
        /**
         * Python License 2.0.
         */
        PYTHON2_0, 
        /**
         * Qhull License.
         */
        QHULL, 
        /**
         * Q Public License 1.0.
         */
        QPL1_0, 
        /**
         * Rdisc License.
         */
        RDISC, 
        /**
         * Red Hat eCos Public License v1.1.
         */
        RHECOS1_1, 
        /**
         * Reciprocal Public License 1.1.
         */
        RPL1_1, 
        /**
         * Reciprocal Public License 1.5.
         */
        RPL1_5, 
        /**
         * RealNetworks Public Source License v1.0.
         */
        RPSL1_0, 
        /**
         * RSA Message-Digest License.
         */
        RSAMD, 
        /**
         * Ricoh Source Code Public License.
         */
        RSCPL, 
        /**
         * Ruby License.
         */
        RUBY, 
        /**
         * Sax Public Domain Notice.
         */
        SAXPD, 
        /**
         * Saxpath License.
         */
        SAXPATH, 
        /**
         * SCEA Shared Source License.
         */
        SCEA, 
        /**
         * Sendmail License.
         */
        SENDMAIL, 
        /**
         * SGI Free Software License B v1.0.
         */
        SGIB1_0, 
        /**
         * SGI Free Software License B v1.1.
         */
        SGIB1_1, 
        /**
         * SGI Free Software License B v2.0.
         */
        SGIB2_0, 
        /**
         * Simple Public License 2.0.
         */
        SIMPL2_0, 
        /**
         * Sun Industry Standards Source License v1.2.
         */
        SISSL1_2, 
        /**
         * Sun Industry Standards Source License v1.1.
         */
        SISSL, 
        /**
         * Sleepycat License.
         */
        SLEEPYCAT, 
        /**
         * Standard ML of New Jersey License.
         */
        SMLNJ, 
        /**
         * Secure Messaging Protocol Public License.
         */
        SMPPL, 
        /**
         * SNIA Public License 1.1.
         */
        SNIA, 
        /**
         * Spencer License 86.
         */
        SPENCER86, 
        /**
         * Spencer License 94.
         */
        SPENCER94, 
        /**
         * Spencer License 99.
         */
        SPENCER99, 
        /**
         * Sun Public License v1.0.
         */
        SPL1_0, 
        /**
         * SugarCRM Public License v1.1.3.
         */
        SUGARCRM1_1_3, 
        /**
         * Scheme Widget Library (SWL) Software License Agreement.
         */
        SWL, 
        /**
         * TCL/TK License.
         */
        TCL, 
        /**
         * TCP Wrappers License.
         */
        TCPWRAPPERS, 
        /**
         * TMate Open Source License.
         */
        TMATE, 
        /**
         * TORQUE v2.5+ Software License v1.1.
         */
        TORQUE1_1, 
        /**
         * Trusster Open Source License.
         */
        TOSL, 
        /**
         * Unicode License Agreement - Data Files and Software (2015).
         */
        UNICODEDFS2015, 
        /**
         * Unicode License Agreement - Data Files and Software (2016).
         */
        UNICODEDFS2016, 
        /**
         * Unicode Terms of Use.
         */
        UNICODETOU, 
        /**
         * The Unlicense.
         */
        UNLICENSE, 
        /**
         * Universal Permissive License v1.0.
         */
        UPL1_0, 
        /**
         * Vim License.
         */
        VIM, 
        /**
         * VOSTROM Public License for Open Source.
         */
        VOSTROM, 
        /**
         * Vovida Software License v1.0.
         */
        VSL1_0, 
        /**
         * W3C Software Notice and License (1998-07-20).
         */
        W3C19980720, 
        /**
         * W3C Software Notice and Document License (2015-05-13).
         */
        W3C20150513, 
        /**
         * W3C Software Notice and License (2002-12-31).
         */
        W3C, 
        /**
         * Sybase Open Watcom Public License 1.0.
         */
        WATCOM1_0, 
        /**
         * Wsuipa License.
         */
        WSUIPA, 
        /**
         * Do What The F*ck You Want To Public License.
         */
        WTFPL, 
        /**
         * X11 License.
         */
        X11, 
        /**
         * Xerox License.
         */
        XEROX, 
        /**
         * XFree86 License 1.1.
         */
        XFREE861_1, 
        /**
         * xinetd License.
         */
        XINETD, 
        /**
         * X.Net License.
         */
        XNET, 
        /**
         * XPP License.
         */
        XPP, 
        /**
         * XSkat License.
         */
        XSKAT, 
        /**
         * Yahoo! Public License v1.0.
         */
        YPL1_0, 
        /**
         * Yahoo! Public License v1.1.
         */
        YPL1_1, 
        /**
         * Zed License.
         */
        ZED, 
        /**
         * Zend License v2.0.
         */
        ZEND2_0, 
        /**
         * Zimbra Public License v1.3.
         */
        ZIMBRA1_3, 
        /**
         * Zimbra Public License v1.4.
         */
        ZIMBRA1_4, 
        /**
         * zlib/libpng License with Acknowledgement.
         */
        ZLIBACKNOWLEDGEMENT, 
        /**
         * zlib License.
         */
        ZLIB, 
        /**
         * Zope Public License 1.1.
         */
        ZPL1_1, 
        /**
         * Zope Public License 2.0.
         */
        ZPL2_0, 
        /**
         * Zope Public License 2.1.
         */
        ZPL2_1, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SPDXLicense fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-open-source".equals(codeString))
          return NOTOPENSOURCE;
        if ("0BSD".equals(codeString))
          return _0BSD;
        if ("AAL".equals(codeString))
          return AAL;
        if ("Abstyles".equals(codeString))
          return ABSTYLES;
        if ("Adobe-2006".equals(codeString))
          return ADOBE2006;
        if ("Adobe-Glyph".equals(codeString))
          return ADOBEGLYPH;
        if ("ADSL".equals(codeString))
          return ADSL;
        if ("AFL-1.1".equals(codeString))
          return AFL1_1;
        if ("AFL-1.2".equals(codeString))
          return AFL1_2;
        if ("AFL-2.0".equals(codeString))
          return AFL2_0;
        if ("AFL-2.1".equals(codeString))
          return AFL2_1;
        if ("AFL-3.0".equals(codeString))
          return AFL3_0;
        if ("Afmparse".equals(codeString))
          return AFMPARSE;
        if ("AGPL-1.0-only".equals(codeString))
          return AGPL1_0ONLY;
        if ("AGPL-1.0-or-later".equals(codeString))
          return AGPL1_0ORLATER;
        if ("AGPL-3.0-only".equals(codeString))
          return AGPL3_0ONLY;
        if ("AGPL-3.0-or-later".equals(codeString))
          return AGPL3_0ORLATER;
        if ("Aladdin".equals(codeString))
          return ALADDIN;
        if ("AMDPLPA".equals(codeString))
          return AMDPLPA;
        if ("AML".equals(codeString))
          return AML;
        if ("AMPAS".equals(codeString))
          return AMPAS;
        if ("ANTLR-PD".equals(codeString))
          return ANTLRPD;
        if ("Apache-1.0".equals(codeString))
          return APACHE1_0;
        if ("Apache-1.1".equals(codeString))
          return APACHE1_1;
        if ("Apache-2.0".equals(codeString))
          return APACHE2_0;
        if ("APAFML".equals(codeString))
          return APAFML;
        if ("APL-1.0".equals(codeString))
          return APL1_0;
        if ("APSL-1.0".equals(codeString))
          return APSL1_0;
        if ("APSL-1.1".equals(codeString))
          return APSL1_1;
        if ("APSL-1.2".equals(codeString))
          return APSL1_2;
        if ("APSL-2.0".equals(codeString))
          return APSL2_0;
        if ("Artistic-1.0-cl8".equals(codeString))
          return ARTISTIC1_0CL8;
        if ("Artistic-1.0-Perl".equals(codeString))
          return ARTISTIC1_0PERL;
        if ("Artistic-1.0".equals(codeString))
          return ARTISTIC1_0;
        if ("Artistic-2.0".equals(codeString))
          return ARTISTIC2_0;
        if ("Bahyph".equals(codeString))
          return BAHYPH;
        if ("Barr".equals(codeString))
          return BARR;
        if ("Beerware".equals(codeString))
          return BEERWARE;
        if ("BitTorrent-1.0".equals(codeString))
          return BITTORRENT1_0;
        if ("BitTorrent-1.1".equals(codeString))
          return BITTORRENT1_1;
        if ("Borceux".equals(codeString))
          return BORCEUX;
        if ("BSD-1-Clause".equals(codeString))
          return BSD1CLAUSE;
        if ("BSD-2-Clause-FreeBSD".equals(codeString))
          return BSD2CLAUSEFREEBSD;
        if ("BSD-2-Clause-NetBSD".equals(codeString))
          return BSD2CLAUSENETBSD;
        if ("BSD-2-Clause-Patent".equals(codeString))
          return BSD2CLAUSEPATENT;
        if ("BSD-2-Clause".equals(codeString))
          return BSD2CLAUSE;
        if ("BSD-3-Clause-Attribution".equals(codeString))
          return BSD3CLAUSEATTRIBUTION;
        if ("BSD-3-Clause-Clear".equals(codeString))
          return BSD3CLAUSECLEAR;
        if ("BSD-3-Clause-LBNL".equals(codeString))
          return BSD3CLAUSELBNL;
        if ("BSD-3-Clause-No-Nuclear-License-2014".equals(codeString))
          return BSD3CLAUSENONUCLEARLICENSE2014;
        if ("BSD-3-Clause-No-Nuclear-License".equals(codeString))
          return BSD3CLAUSENONUCLEARLICENSE;
        if ("BSD-3-Clause-No-Nuclear-Warranty".equals(codeString))
          return BSD3CLAUSENONUCLEARWARRANTY;
        if ("BSD-3-Clause".equals(codeString))
          return BSD3CLAUSE;
        if ("BSD-4-Clause-UC".equals(codeString))
          return BSD4CLAUSEUC;
        if ("BSD-4-Clause".equals(codeString))
          return BSD4CLAUSE;
        if ("BSD-Protection".equals(codeString))
          return BSDPROTECTION;
        if ("BSD-Source-Code".equals(codeString))
          return BSDSOURCECODE;
        if ("BSL-1.0".equals(codeString))
          return BSL1_0;
        if ("bzip2-1.0.5".equals(codeString))
          return BZIP21_0_5;
        if ("bzip2-1.0.6".equals(codeString))
          return BZIP21_0_6;
        if ("Caldera".equals(codeString))
          return CALDERA;
        if ("CATOSL-1.1".equals(codeString))
          return CATOSL1_1;
        if ("CC-BY-1.0".equals(codeString))
          return CCBY1_0;
        if ("CC-BY-2.0".equals(codeString))
          return CCBY2_0;
        if ("CC-BY-2.5".equals(codeString))
          return CCBY2_5;
        if ("CC-BY-3.0".equals(codeString))
          return CCBY3_0;
        if ("CC-BY-4.0".equals(codeString))
          return CCBY4_0;
        if ("CC-BY-NC-1.0".equals(codeString))
          return CCBYNC1_0;
        if ("CC-BY-NC-2.0".equals(codeString))
          return CCBYNC2_0;
        if ("CC-BY-NC-2.5".equals(codeString))
          return CCBYNC2_5;
        if ("CC-BY-NC-3.0".equals(codeString))
          return CCBYNC3_0;
        if ("CC-BY-NC-4.0".equals(codeString))
          return CCBYNC4_0;
        if ("CC-BY-NC-ND-1.0".equals(codeString))
          return CCBYNCND1_0;
        if ("CC-BY-NC-ND-2.0".equals(codeString))
          return CCBYNCND2_0;
        if ("CC-BY-NC-ND-2.5".equals(codeString))
          return CCBYNCND2_5;
        if ("CC-BY-NC-ND-3.0".equals(codeString))
          return CCBYNCND3_0;
        if ("CC-BY-NC-ND-4.0".equals(codeString))
          return CCBYNCND4_0;
        if ("CC-BY-NC-SA-1.0".equals(codeString))
          return CCBYNCSA1_0;
        if ("CC-BY-NC-SA-2.0".equals(codeString))
          return CCBYNCSA2_0;
        if ("CC-BY-NC-SA-2.5".equals(codeString))
          return CCBYNCSA2_5;
        if ("CC-BY-NC-SA-3.0".equals(codeString))
          return CCBYNCSA3_0;
        if ("CC-BY-NC-SA-4.0".equals(codeString))
          return CCBYNCSA4_0;
        if ("CC-BY-ND-1.0".equals(codeString))
          return CCBYND1_0;
        if ("CC-BY-ND-2.0".equals(codeString))
          return CCBYND2_0;
        if ("CC-BY-ND-2.5".equals(codeString))
          return CCBYND2_5;
        if ("CC-BY-ND-3.0".equals(codeString))
          return CCBYND3_0;
        if ("CC-BY-ND-4.0".equals(codeString))
          return CCBYND4_0;
        if ("CC-BY-SA-1.0".equals(codeString))
          return CCBYSA1_0;
        if ("CC-BY-SA-2.0".equals(codeString))
          return CCBYSA2_0;
        if ("CC-BY-SA-2.5".equals(codeString))
          return CCBYSA2_5;
        if ("CC-BY-SA-3.0".equals(codeString))
          return CCBYSA3_0;
        if ("CC-BY-SA-4.0".equals(codeString))
          return CCBYSA4_0;
        if ("CC0-1.0".equals(codeString))
          return CC01_0;
        if ("CDDL-1.0".equals(codeString))
          return CDDL1_0;
        if ("CDDL-1.1".equals(codeString))
          return CDDL1_1;
        if ("CDLA-Permissive-1.0".equals(codeString))
          return CDLAPERMISSIVE1_0;
        if ("CDLA-Sharing-1.0".equals(codeString))
          return CDLASHARING1_0;
        if ("CECILL-1.0".equals(codeString))
          return CECILL1_0;
        if ("CECILL-1.1".equals(codeString))
          return CECILL1_1;
        if ("CECILL-2.0".equals(codeString))
          return CECILL2_0;
        if ("CECILL-2.1".equals(codeString))
          return CECILL2_1;
        if ("CECILL-B".equals(codeString))
          return CECILLB;
        if ("CECILL-C".equals(codeString))
          return CECILLC;
        if ("ClArtistic".equals(codeString))
          return CLARTISTIC;
        if ("CNRI-Jython".equals(codeString))
          return CNRIJYTHON;
        if ("CNRI-Python-GPL-Compatible".equals(codeString))
          return CNRIPYTHONGPLCOMPATIBLE;
        if ("CNRI-Python".equals(codeString))
          return CNRIPYTHON;
        if ("Condor-1.1".equals(codeString))
          return CONDOR1_1;
        if ("CPAL-1.0".equals(codeString))
          return CPAL1_0;
        if ("CPL-1.0".equals(codeString))
          return CPL1_0;
        if ("CPOL-1.02".equals(codeString))
          return CPOL1_02;
        if ("Crossword".equals(codeString))
          return CROSSWORD;
        if ("CrystalStacker".equals(codeString))
          return CRYSTALSTACKER;
        if ("CUA-OPL-1.0".equals(codeString))
          return CUAOPL1_0;
        if ("Cube".equals(codeString))
          return CUBE;
        if ("curl".equals(codeString))
          return CURL;
        if ("D-FSL-1.0".equals(codeString))
          return DFSL1_0;
        if ("diffmark".equals(codeString))
          return DIFFMARK;
        if ("DOC".equals(codeString))
          return DOC;
        if ("Dotseqn".equals(codeString))
          return DOTSEQN;
        if ("DSDP".equals(codeString))
          return DSDP;
        if ("dvipdfm".equals(codeString))
          return DVIPDFM;
        if ("ECL-1.0".equals(codeString))
          return ECL1_0;
        if ("ECL-2.0".equals(codeString))
          return ECL2_0;
        if ("EFL-1.0".equals(codeString))
          return EFL1_0;
        if ("EFL-2.0".equals(codeString))
          return EFL2_0;
        if ("eGenix".equals(codeString))
          return EGENIX;
        if ("Entessa".equals(codeString))
          return ENTESSA;
        if ("EPL-1.0".equals(codeString))
          return EPL1_0;
        if ("EPL-2.0".equals(codeString))
          return EPL2_0;
        if ("ErlPL-1.1".equals(codeString))
          return ERLPL1_1;
        if ("EUDatagrid".equals(codeString))
          return EUDATAGRID;
        if ("EUPL-1.0".equals(codeString))
          return EUPL1_0;
        if ("EUPL-1.1".equals(codeString))
          return EUPL1_1;
        if ("EUPL-1.2".equals(codeString))
          return EUPL1_2;
        if ("Eurosym".equals(codeString))
          return EUROSYM;
        if ("Fair".equals(codeString))
          return FAIR;
        if ("Frameworx-1.0".equals(codeString))
          return FRAMEWORX1_0;
        if ("FreeImage".equals(codeString))
          return FREEIMAGE;
        if ("FSFAP".equals(codeString))
          return FSFAP;
        if ("FSFUL".equals(codeString))
          return FSFUL;
        if ("FSFULLR".equals(codeString))
          return FSFULLR;
        if ("FTL".equals(codeString))
          return FTL;
        if ("GFDL-1.1-only".equals(codeString))
          return GFDL1_1ONLY;
        if ("GFDL-1.1-or-later".equals(codeString))
          return GFDL1_1ORLATER;
        if ("GFDL-1.2-only".equals(codeString))
          return GFDL1_2ONLY;
        if ("GFDL-1.2-or-later".equals(codeString))
          return GFDL1_2ORLATER;
        if ("GFDL-1.3-only".equals(codeString))
          return GFDL1_3ONLY;
        if ("GFDL-1.3-or-later".equals(codeString))
          return GFDL1_3ORLATER;
        if ("Giftware".equals(codeString))
          return GIFTWARE;
        if ("GL2PS".equals(codeString))
          return GL2PS;
        if ("Glide".equals(codeString))
          return GLIDE;
        if ("Glulxe".equals(codeString))
          return GLULXE;
        if ("gnuplot".equals(codeString))
          return GNUPLOT;
        if ("GPL-1.0-only".equals(codeString))
          return GPL1_0ONLY;
        if ("GPL-1.0-or-later".equals(codeString))
          return GPL1_0ORLATER;
        if ("GPL-2.0-only".equals(codeString))
          return GPL2_0ONLY;
        if ("GPL-2.0-or-later".equals(codeString))
          return GPL2_0ORLATER;
        if ("GPL-3.0-only".equals(codeString))
          return GPL3_0ONLY;
        if ("GPL-3.0-or-later".equals(codeString))
          return GPL3_0ORLATER;
        if ("gSOAP-1.3b".equals(codeString))
          return GSOAP1_3B;
        if ("HaskellReport".equals(codeString))
          return HASKELLREPORT;
        if ("HPND".equals(codeString))
          return HPND;
        if ("IBM-pibs".equals(codeString))
          return IBMPIBS;
        if ("ICU".equals(codeString))
          return ICU;
        if ("IJG".equals(codeString))
          return IJG;
        if ("ImageMagick".equals(codeString))
          return IMAGEMAGICK;
        if ("iMatix".equals(codeString))
          return IMATIX;
        if ("Imlib2".equals(codeString))
          return IMLIB2;
        if ("Info-ZIP".equals(codeString))
          return INFOZIP;
        if ("Intel-ACPI".equals(codeString))
          return INTELACPI;
        if ("Intel".equals(codeString))
          return INTEL;
        if ("Interbase-1.0".equals(codeString))
          return INTERBASE1_0;
        if ("IPA".equals(codeString))
          return IPA;
        if ("IPL-1.0".equals(codeString))
          return IPL1_0;
        if ("ISC".equals(codeString))
          return ISC;
        if ("JasPer-2.0".equals(codeString))
          return JASPER2_0;
        if ("JSON".equals(codeString))
          return JSON;
        if ("LAL-1.2".equals(codeString))
          return LAL1_2;
        if ("LAL-1.3".equals(codeString))
          return LAL1_3;
        if ("Latex2e".equals(codeString))
          return LATEX2E;
        if ("Leptonica".equals(codeString))
          return LEPTONICA;
        if ("LGPL-2.0-only".equals(codeString))
          return LGPL2_0ONLY;
        if ("LGPL-2.0-or-later".equals(codeString))
          return LGPL2_0ORLATER;
        if ("LGPL-2.1-only".equals(codeString))
          return LGPL2_1ONLY;
        if ("LGPL-2.1-or-later".equals(codeString))
          return LGPL2_1ORLATER;
        if ("LGPL-3.0-only".equals(codeString))
          return LGPL3_0ONLY;
        if ("LGPL-3.0-or-later".equals(codeString))
          return LGPL3_0ORLATER;
        if ("LGPLLR".equals(codeString))
          return LGPLLR;
        if ("Libpng".equals(codeString))
          return LIBPNG;
        if ("libtiff".equals(codeString))
          return LIBTIFF;
        if ("LiLiQ-P-1.1".equals(codeString))
          return LILIQP1_1;
        if ("LiLiQ-R-1.1".equals(codeString))
          return LILIQR1_1;
        if ("LiLiQ-Rplus-1.1".equals(codeString))
          return LILIQRPLUS1_1;
        if ("Linux-OpenIB".equals(codeString))
          return LINUXOPENIB;
        if ("LPL-1.0".equals(codeString))
          return LPL1_0;
        if ("LPL-1.02".equals(codeString))
          return LPL1_02;
        if ("LPPL-1.0".equals(codeString))
          return LPPL1_0;
        if ("LPPL-1.1".equals(codeString))
          return LPPL1_1;
        if ("LPPL-1.2".equals(codeString))
          return LPPL1_2;
        if ("LPPL-1.3a".equals(codeString))
          return LPPL1_3A;
        if ("LPPL-1.3c".equals(codeString))
          return LPPL1_3C;
        if ("MakeIndex".equals(codeString))
          return MAKEINDEX;
        if ("MirOS".equals(codeString))
          return MIROS;
        if ("MIT-0".equals(codeString))
          return MIT0;
        if ("MIT-advertising".equals(codeString))
          return MITADVERTISING;
        if ("MIT-CMU".equals(codeString))
          return MITCMU;
        if ("MIT-enna".equals(codeString))
          return MITENNA;
        if ("MIT-feh".equals(codeString))
          return MITFEH;
        if ("MIT".equals(codeString))
          return MIT;
        if ("MITNFA".equals(codeString))
          return MITNFA;
        if ("Motosoto".equals(codeString))
          return MOTOSOTO;
        if ("mpich2".equals(codeString))
          return MPICH2;
        if ("MPL-1.0".equals(codeString))
          return MPL1_0;
        if ("MPL-1.1".equals(codeString))
          return MPL1_1;
        if ("MPL-2.0-no-copyleft-exception".equals(codeString))
          return MPL2_0NOCOPYLEFTEXCEPTION;
        if ("MPL-2.0".equals(codeString))
          return MPL2_0;
        if ("MS-PL".equals(codeString))
          return MSPL;
        if ("MS-RL".equals(codeString))
          return MSRL;
        if ("MTLL".equals(codeString))
          return MTLL;
        if ("Multics".equals(codeString))
          return MULTICS;
        if ("Mup".equals(codeString))
          return MUP;
        if ("NASA-1.3".equals(codeString))
          return NASA1_3;
        if ("Naumen".equals(codeString))
          return NAUMEN;
        if ("NBPL-1.0".equals(codeString))
          return NBPL1_0;
        if ("NCSA".equals(codeString))
          return NCSA;
        if ("Net-SNMP".equals(codeString))
          return NETSNMP;
        if ("NetCDF".equals(codeString))
          return NETCDF;
        if ("Newsletr".equals(codeString))
          return NEWSLETR;
        if ("NGPL".equals(codeString))
          return NGPL;
        if ("NLOD-1.0".equals(codeString))
          return NLOD1_0;
        if ("NLPL".equals(codeString))
          return NLPL;
        if ("Nokia".equals(codeString))
          return NOKIA;
        if ("NOSL".equals(codeString))
          return NOSL;
        if ("Noweb".equals(codeString))
          return NOWEB;
        if ("NPL-1.0".equals(codeString))
          return NPL1_0;
        if ("NPL-1.1".equals(codeString))
          return NPL1_1;
        if ("NPOSL-3.0".equals(codeString))
          return NPOSL3_0;
        if ("NRL".equals(codeString))
          return NRL;
        if ("NTP".equals(codeString))
          return NTP;
        if ("OCCT-PL".equals(codeString))
          return OCCTPL;
        if ("OCLC-2.0".equals(codeString))
          return OCLC2_0;
        if ("ODbL-1.0".equals(codeString))
          return ODBL1_0;
        if ("OFL-1.0".equals(codeString))
          return OFL1_0;
        if ("OFL-1.1".equals(codeString))
          return OFL1_1;
        if ("OGTSL".equals(codeString))
          return OGTSL;
        if ("OLDAP-1.1".equals(codeString))
          return OLDAP1_1;
        if ("OLDAP-1.2".equals(codeString))
          return OLDAP1_2;
        if ("OLDAP-1.3".equals(codeString))
          return OLDAP1_3;
        if ("OLDAP-1.4".equals(codeString))
          return OLDAP1_4;
        if ("OLDAP-2.0.1".equals(codeString))
          return OLDAP2_0_1;
        if ("OLDAP-2.0".equals(codeString))
          return OLDAP2_0;
        if ("OLDAP-2.1".equals(codeString))
          return OLDAP2_1;
        if ("OLDAP-2.2.1".equals(codeString))
          return OLDAP2_2_1;
        if ("OLDAP-2.2.2".equals(codeString))
          return OLDAP2_2_2;
        if ("OLDAP-2.2".equals(codeString))
          return OLDAP2_2;
        if ("OLDAP-2.3".equals(codeString))
          return OLDAP2_3;
        if ("OLDAP-2.4".equals(codeString))
          return OLDAP2_4;
        if ("OLDAP-2.5".equals(codeString))
          return OLDAP2_5;
        if ("OLDAP-2.6".equals(codeString))
          return OLDAP2_6;
        if ("OLDAP-2.7".equals(codeString))
          return OLDAP2_7;
        if ("OLDAP-2.8".equals(codeString))
          return OLDAP2_8;
        if ("OML".equals(codeString))
          return OML;
        if ("OpenSSL".equals(codeString))
          return OPENSSL;
        if ("OPL-1.0".equals(codeString))
          return OPL1_0;
        if ("OSET-PL-2.1".equals(codeString))
          return OSETPL2_1;
        if ("OSL-1.0".equals(codeString))
          return OSL1_0;
        if ("OSL-1.1".equals(codeString))
          return OSL1_1;
        if ("OSL-2.0".equals(codeString))
          return OSL2_0;
        if ("OSL-2.1".equals(codeString))
          return OSL2_1;
        if ("OSL-3.0".equals(codeString))
          return OSL3_0;
        if ("PDDL-1.0".equals(codeString))
          return PDDL1_0;
        if ("PHP-3.0".equals(codeString))
          return PHP3_0;
        if ("PHP-3.01".equals(codeString))
          return PHP3_01;
        if ("Plexus".equals(codeString))
          return PLEXUS;
        if ("PostgreSQL".equals(codeString))
          return POSTGRESQL;
        if ("psfrag".equals(codeString))
          return PSFRAG;
        if ("psutils".equals(codeString))
          return PSUTILS;
        if ("Python-2.0".equals(codeString))
          return PYTHON2_0;
        if ("Qhull".equals(codeString))
          return QHULL;
        if ("QPL-1.0".equals(codeString))
          return QPL1_0;
        if ("Rdisc".equals(codeString))
          return RDISC;
        if ("RHeCos-1.1".equals(codeString))
          return RHECOS1_1;
        if ("RPL-1.1".equals(codeString))
          return RPL1_1;
        if ("RPL-1.5".equals(codeString))
          return RPL1_5;
        if ("RPSL-1.0".equals(codeString))
          return RPSL1_0;
        if ("RSA-MD".equals(codeString))
          return RSAMD;
        if ("RSCPL".equals(codeString))
          return RSCPL;
        if ("Ruby".equals(codeString))
          return RUBY;
        if ("SAX-PD".equals(codeString))
          return SAXPD;
        if ("Saxpath".equals(codeString))
          return SAXPATH;
        if ("SCEA".equals(codeString))
          return SCEA;
        if ("Sendmail".equals(codeString))
          return SENDMAIL;
        if ("SGI-B-1.0".equals(codeString))
          return SGIB1_0;
        if ("SGI-B-1.1".equals(codeString))
          return SGIB1_1;
        if ("SGI-B-2.0".equals(codeString))
          return SGIB2_0;
        if ("SimPL-2.0".equals(codeString))
          return SIMPL2_0;
        if ("SISSL-1.2".equals(codeString))
          return SISSL1_2;
        if ("SISSL".equals(codeString))
          return SISSL;
        if ("Sleepycat".equals(codeString))
          return SLEEPYCAT;
        if ("SMLNJ".equals(codeString))
          return SMLNJ;
        if ("SMPPL".equals(codeString))
          return SMPPL;
        if ("SNIA".equals(codeString))
          return SNIA;
        if ("Spencer-86".equals(codeString))
          return SPENCER86;
        if ("Spencer-94".equals(codeString))
          return SPENCER94;
        if ("Spencer-99".equals(codeString))
          return SPENCER99;
        if ("SPL-1.0".equals(codeString))
          return SPL1_0;
        if ("SugarCRM-1.1.3".equals(codeString))
          return SUGARCRM1_1_3;
        if ("SWL".equals(codeString))
          return SWL;
        if ("TCL".equals(codeString))
          return TCL;
        if ("TCP-wrappers".equals(codeString))
          return TCPWRAPPERS;
        if ("TMate".equals(codeString))
          return TMATE;
        if ("TORQUE-1.1".equals(codeString))
          return TORQUE1_1;
        if ("TOSL".equals(codeString))
          return TOSL;
        if ("Unicode-DFS-2015".equals(codeString))
          return UNICODEDFS2015;
        if ("Unicode-DFS-2016".equals(codeString))
          return UNICODEDFS2016;
        if ("Unicode-TOU".equals(codeString))
          return UNICODETOU;
        if ("Unlicense".equals(codeString))
          return UNLICENSE;
        if ("UPL-1.0".equals(codeString))
          return UPL1_0;
        if ("Vim".equals(codeString))
          return VIM;
        if ("VOSTROM".equals(codeString))
          return VOSTROM;
        if ("VSL-1.0".equals(codeString))
          return VSL1_0;
        if ("W3C-19980720".equals(codeString))
          return W3C19980720;
        if ("W3C-20150513".equals(codeString))
          return W3C20150513;
        if ("W3C".equals(codeString))
          return W3C;
        if ("Watcom-1.0".equals(codeString))
          return WATCOM1_0;
        if ("Wsuipa".equals(codeString))
          return WSUIPA;
        if ("WTFPL".equals(codeString))
          return WTFPL;
        if ("X11".equals(codeString))
          return X11;
        if ("Xerox".equals(codeString))
          return XEROX;
        if ("XFree86-1.1".equals(codeString))
          return XFREE861_1;
        if ("xinetd".equals(codeString))
          return XINETD;
        if ("Xnet".equals(codeString))
          return XNET;
        if ("xpp".equals(codeString))
          return XPP;
        if ("XSkat".equals(codeString))
          return XSKAT;
        if ("YPL-1.0".equals(codeString))
          return YPL1_0;
        if ("YPL-1.1".equals(codeString))
          return YPL1_1;
        if ("Zed".equals(codeString))
          return ZED;
        if ("Zend-2.0".equals(codeString))
          return ZEND2_0;
        if ("Zimbra-1.3".equals(codeString))
          return ZIMBRA1_3;
        if ("Zimbra-1.4".equals(codeString))
          return ZIMBRA1_4;
        if ("zlib-acknowledgement".equals(codeString))
          return ZLIBACKNOWLEDGEMENT;
        if ("Zlib".equals(codeString))
          return ZLIB;
        if ("ZPL-1.1".equals(codeString))
          return ZPL1_1;
        if ("ZPL-2.0".equals(codeString))
          return ZPL2_0;
        if ("ZPL-2.1".equals(codeString))
          return ZPL2_1;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SPDXLicense code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTOPENSOURCE: return "not-open-source";
            case _0BSD: return "0BSD";
            case AAL: return "AAL";
            case ABSTYLES: return "Abstyles";
            case ADOBE2006: return "Adobe-2006";
            case ADOBEGLYPH: return "Adobe-Glyph";
            case ADSL: return "ADSL";
            case AFL1_1: return "AFL-1.1";
            case AFL1_2: return "AFL-1.2";
            case AFL2_0: return "AFL-2.0";
            case AFL2_1: return "AFL-2.1";
            case AFL3_0: return "AFL-3.0";
            case AFMPARSE: return "Afmparse";
            case AGPL1_0ONLY: return "AGPL-1.0-only";
            case AGPL1_0ORLATER: return "AGPL-1.0-or-later";
            case AGPL3_0ONLY: return "AGPL-3.0-only";
            case AGPL3_0ORLATER: return "AGPL-3.0-or-later";
            case ALADDIN: return "Aladdin";
            case AMDPLPA: return "AMDPLPA";
            case AML: return "AML";
            case AMPAS: return "AMPAS";
            case ANTLRPD: return "ANTLR-PD";
            case APACHE1_0: return "Apache-1.0";
            case APACHE1_1: return "Apache-1.1";
            case APACHE2_0: return "Apache-2.0";
            case APAFML: return "APAFML";
            case APL1_0: return "APL-1.0";
            case APSL1_0: return "APSL-1.0";
            case APSL1_1: return "APSL-1.1";
            case APSL1_2: return "APSL-1.2";
            case APSL2_0: return "APSL-2.0";
            case ARTISTIC1_0CL8: return "Artistic-1.0-cl8";
            case ARTISTIC1_0PERL: return "Artistic-1.0-Perl";
            case ARTISTIC1_0: return "Artistic-1.0";
            case ARTISTIC2_0: return "Artistic-2.0";
            case BAHYPH: return "Bahyph";
            case BARR: return "Barr";
            case BEERWARE: return "Beerware";
            case BITTORRENT1_0: return "BitTorrent-1.0";
            case BITTORRENT1_1: return "BitTorrent-1.1";
            case BORCEUX: return "Borceux";
            case BSD1CLAUSE: return "BSD-1-Clause";
            case BSD2CLAUSEFREEBSD: return "BSD-2-Clause-FreeBSD";
            case BSD2CLAUSENETBSD: return "BSD-2-Clause-NetBSD";
            case BSD2CLAUSEPATENT: return "BSD-2-Clause-Patent";
            case BSD2CLAUSE: return "BSD-2-Clause";
            case BSD3CLAUSEATTRIBUTION: return "BSD-3-Clause-Attribution";
            case BSD3CLAUSECLEAR: return "BSD-3-Clause-Clear";
            case BSD3CLAUSELBNL: return "BSD-3-Clause-LBNL";
            case BSD3CLAUSENONUCLEARLICENSE2014: return "BSD-3-Clause-No-Nuclear-License-2014";
            case BSD3CLAUSENONUCLEARLICENSE: return "BSD-3-Clause-No-Nuclear-License";
            case BSD3CLAUSENONUCLEARWARRANTY: return "BSD-3-Clause-No-Nuclear-Warranty";
            case BSD3CLAUSE: return "BSD-3-Clause";
            case BSD4CLAUSEUC: return "BSD-4-Clause-UC";
            case BSD4CLAUSE: return "BSD-4-Clause";
            case BSDPROTECTION: return "BSD-Protection";
            case BSDSOURCECODE: return "BSD-Source-Code";
            case BSL1_0: return "BSL-1.0";
            case BZIP21_0_5: return "bzip2-1.0.5";
            case BZIP21_0_6: return "bzip2-1.0.6";
            case CALDERA: return "Caldera";
            case CATOSL1_1: return "CATOSL-1.1";
            case CCBY1_0: return "CC-BY-1.0";
            case CCBY2_0: return "CC-BY-2.0";
            case CCBY2_5: return "CC-BY-2.5";
            case CCBY3_0: return "CC-BY-3.0";
            case CCBY4_0: return "CC-BY-4.0";
            case CCBYNC1_0: return "CC-BY-NC-1.0";
            case CCBYNC2_0: return "CC-BY-NC-2.0";
            case CCBYNC2_5: return "CC-BY-NC-2.5";
            case CCBYNC3_0: return "CC-BY-NC-3.0";
            case CCBYNC4_0: return "CC-BY-NC-4.0";
            case CCBYNCND1_0: return "CC-BY-NC-ND-1.0";
            case CCBYNCND2_0: return "CC-BY-NC-ND-2.0";
            case CCBYNCND2_5: return "CC-BY-NC-ND-2.5";
            case CCBYNCND3_0: return "CC-BY-NC-ND-3.0";
            case CCBYNCND4_0: return "CC-BY-NC-ND-4.0";
            case CCBYNCSA1_0: return "CC-BY-NC-SA-1.0";
            case CCBYNCSA2_0: return "CC-BY-NC-SA-2.0";
            case CCBYNCSA2_5: return "CC-BY-NC-SA-2.5";
            case CCBYNCSA3_0: return "CC-BY-NC-SA-3.0";
            case CCBYNCSA4_0: return "CC-BY-NC-SA-4.0";
            case CCBYND1_0: return "CC-BY-ND-1.0";
            case CCBYND2_0: return "CC-BY-ND-2.0";
            case CCBYND2_5: return "CC-BY-ND-2.5";
            case CCBYND3_0: return "CC-BY-ND-3.0";
            case CCBYND4_0: return "CC-BY-ND-4.0";
            case CCBYSA1_0: return "CC-BY-SA-1.0";
            case CCBYSA2_0: return "CC-BY-SA-2.0";
            case CCBYSA2_5: return "CC-BY-SA-2.5";
            case CCBYSA3_0: return "CC-BY-SA-3.0";
            case CCBYSA4_0: return "CC-BY-SA-4.0";
            case CC01_0: return "CC0-1.0";
            case CDDL1_0: return "CDDL-1.0";
            case CDDL1_1: return "CDDL-1.1";
            case CDLAPERMISSIVE1_0: return "CDLA-Permissive-1.0";
            case CDLASHARING1_0: return "CDLA-Sharing-1.0";
            case CECILL1_0: return "CECILL-1.0";
            case CECILL1_1: return "CECILL-1.1";
            case CECILL2_0: return "CECILL-2.0";
            case CECILL2_1: return "CECILL-2.1";
            case CECILLB: return "CECILL-B";
            case CECILLC: return "CECILL-C";
            case CLARTISTIC: return "ClArtistic";
            case CNRIJYTHON: return "CNRI-Jython";
            case CNRIPYTHONGPLCOMPATIBLE: return "CNRI-Python-GPL-Compatible";
            case CNRIPYTHON: return "CNRI-Python";
            case CONDOR1_1: return "Condor-1.1";
            case CPAL1_0: return "CPAL-1.0";
            case CPL1_0: return "CPL-1.0";
            case CPOL1_02: return "CPOL-1.02";
            case CROSSWORD: return "Crossword";
            case CRYSTALSTACKER: return "CrystalStacker";
            case CUAOPL1_0: return "CUA-OPL-1.0";
            case CUBE: return "Cube";
            case CURL: return "curl";
            case DFSL1_0: return "D-FSL-1.0";
            case DIFFMARK: return "diffmark";
            case DOC: return "DOC";
            case DOTSEQN: return "Dotseqn";
            case DSDP: return "DSDP";
            case DVIPDFM: return "dvipdfm";
            case ECL1_0: return "ECL-1.0";
            case ECL2_0: return "ECL-2.0";
            case EFL1_0: return "EFL-1.0";
            case EFL2_0: return "EFL-2.0";
            case EGENIX: return "eGenix";
            case ENTESSA: return "Entessa";
            case EPL1_0: return "EPL-1.0";
            case EPL2_0: return "EPL-2.0";
            case ERLPL1_1: return "ErlPL-1.1";
            case EUDATAGRID: return "EUDatagrid";
            case EUPL1_0: return "EUPL-1.0";
            case EUPL1_1: return "EUPL-1.1";
            case EUPL1_2: return "EUPL-1.2";
            case EUROSYM: return "Eurosym";
            case FAIR: return "Fair";
            case FRAMEWORX1_0: return "Frameworx-1.0";
            case FREEIMAGE: return "FreeImage";
            case FSFAP: return "FSFAP";
            case FSFUL: return "FSFUL";
            case FSFULLR: return "FSFULLR";
            case FTL: return "FTL";
            case GFDL1_1ONLY: return "GFDL-1.1-only";
            case GFDL1_1ORLATER: return "GFDL-1.1-or-later";
            case GFDL1_2ONLY: return "GFDL-1.2-only";
            case GFDL1_2ORLATER: return "GFDL-1.2-or-later";
            case GFDL1_3ONLY: return "GFDL-1.3-only";
            case GFDL1_3ORLATER: return "GFDL-1.3-or-later";
            case GIFTWARE: return "Giftware";
            case GL2PS: return "GL2PS";
            case GLIDE: return "Glide";
            case GLULXE: return "Glulxe";
            case GNUPLOT: return "gnuplot";
            case GPL1_0ONLY: return "GPL-1.0-only";
            case GPL1_0ORLATER: return "GPL-1.0-or-later";
            case GPL2_0ONLY: return "GPL-2.0-only";
            case GPL2_0ORLATER: return "GPL-2.0-or-later";
            case GPL3_0ONLY: return "GPL-3.0-only";
            case GPL3_0ORLATER: return "GPL-3.0-or-later";
            case GSOAP1_3B: return "gSOAP-1.3b";
            case HASKELLREPORT: return "HaskellReport";
            case HPND: return "HPND";
            case IBMPIBS: return "IBM-pibs";
            case ICU: return "ICU";
            case IJG: return "IJG";
            case IMAGEMAGICK: return "ImageMagick";
            case IMATIX: return "iMatix";
            case IMLIB2: return "Imlib2";
            case INFOZIP: return "Info-ZIP";
            case INTELACPI: return "Intel-ACPI";
            case INTEL: return "Intel";
            case INTERBASE1_0: return "Interbase-1.0";
            case IPA: return "IPA";
            case IPL1_0: return "IPL-1.0";
            case ISC: return "ISC";
            case JASPER2_0: return "JasPer-2.0";
            case JSON: return "JSON";
            case LAL1_2: return "LAL-1.2";
            case LAL1_3: return "LAL-1.3";
            case LATEX2E: return "Latex2e";
            case LEPTONICA: return "Leptonica";
            case LGPL2_0ONLY: return "LGPL-2.0-only";
            case LGPL2_0ORLATER: return "LGPL-2.0-or-later";
            case LGPL2_1ONLY: return "LGPL-2.1-only";
            case LGPL2_1ORLATER: return "LGPL-2.1-or-later";
            case LGPL3_0ONLY: return "LGPL-3.0-only";
            case LGPL3_0ORLATER: return "LGPL-3.0-or-later";
            case LGPLLR: return "LGPLLR";
            case LIBPNG: return "Libpng";
            case LIBTIFF: return "libtiff";
            case LILIQP1_1: return "LiLiQ-P-1.1";
            case LILIQR1_1: return "LiLiQ-R-1.1";
            case LILIQRPLUS1_1: return "LiLiQ-Rplus-1.1";
            case LINUXOPENIB: return "Linux-OpenIB";
            case LPL1_0: return "LPL-1.0";
            case LPL1_02: return "LPL-1.02";
            case LPPL1_0: return "LPPL-1.0";
            case LPPL1_1: return "LPPL-1.1";
            case LPPL1_2: return "LPPL-1.2";
            case LPPL1_3A: return "LPPL-1.3a";
            case LPPL1_3C: return "LPPL-1.3c";
            case MAKEINDEX: return "MakeIndex";
            case MIROS: return "MirOS";
            case MIT0: return "MIT-0";
            case MITADVERTISING: return "MIT-advertising";
            case MITCMU: return "MIT-CMU";
            case MITENNA: return "MIT-enna";
            case MITFEH: return "MIT-feh";
            case MIT: return "MIT";
            case MITNFA: return "MITNFA";
            case MOTOSOTO: return "Motosoto";
            case MPICH2: return "mpich2";
            case MPL1_0: return "MPL-1.0";
            case MPL1_1: return "MPL-1.1";
            case MPL2_0NOCOPYLEFTEXCEPTION: return "MPL-2.0-no-copyleft-exception";
            case MPL2_0: return "MPL-2.0";
            case MSPL: return "MS-PL";
            case MSRL: return "MS-RL";
            case MTLL: return "MTLL";
            case MULTICS: return "Multics";
            case MUP: return "Mup";
            case NASA1_3: return "NASA-1.3";
            case NAUMEN: return "Naumen";
            case NBPL1_0: return "NBPL-1.0";
            case NCSA: return "NCSA";
            case NETSNMP: return "Net-SNMP";
            case NETCDF: return "NetCDF";
            case NEWSLETR: return "Newsletr";
            case NGPL: return "NGPL";
            case NLOD1_0: return "NLOD-1.0";
            case NLPL: return "NLPL";
            case NOKIA: return "Nokia";
            case NOSL: return "NOSL";
            case NOWEB: return "Noweb";
            case NPL1_0: return "NPL-1.0";
            case NPL1_1: return "NPL-1.1";
            case NPOSL3_0: return "NPOSL-3.0";
            case NRL: return "NRL";
            case NTP: return "NTP";
            case OCCTPL: return "OCCT-PL";
            case OCLC2_0: return "OCLC-2.0";
            case ODBL1_0: return "ODbL-1.0";
            case OFL1_0: return "OFL-1.0";
            case OFL1_1: return "OFL-1.1";
            case OGTSL: return "OGTSL";
            case OLDAP1_1: return "OLDAP-1.1";
            case OLDAP1_2: return "OLDAP-1.2";
            case OLDAP1_3: return "OLDAP-1.3";
            case OLDAP1_4: return "OLDAP-1.4";
            case OLDAP2_0_1: return "OLDAP-2.0.1";
            case OLDAP2_0: return "OLDAP-2.0";
            case OLDAP2_1: return "OLDAP-2.1";
            case OLDAP2_2_1: return "OLDAP-2.2.1";
            case OLDAP2_2_2: return "OLDAP-2.2.2";
            case OLDAP2_2: return "OLDAP-2.2";
            case OLDAP2_3: return "OLDAP-2.3";
            case OLDAP2_4: return "OLDAP-2.4";
            case OLDAP2_5: return "OLDAP-2.5";
            case OLDAP2_6: return "OLDAP-2.6";
            case OLDAP2_7: return "OLDAP-2.7";
            case OLDAP2_8: return "OLDAP-2.8";
            case OML: return "OML";
            case OPENSSL: return "OpenSSL";
            case OPL1_0: return "OPL-1.0";
            case OSETPL2_1: return "OSET-PL-2.1";
            case OSL1_0: return "OSL-1.0";
            case OSL1_1: return "OSL-1.1";
            case OSL2_0: return "OSL-2.0";
            case OSL2_1: return "OSL-2.1";
            case OSL3_0: return "OSL-3.0";
            case PDDL1_0: return "PDDL-1.0";
            case PHP3_0: return "PHP-3.0";
            case PHP3_01: return "PHP-3.01";
            case PLEXUS: return "Plexus";
            case POSTGRESQL: return "PostgreSQL";
            case PSFRAG: return "psfrag";
            case PSUTILS: return "psutils";
            case PYTHON2_0: return "Python-2.0";
            case QHULL: return "Qhull";
            case QPL1_0: return "QPL-1.0";
            case RDISC: return "Rdisc";
            case RHECOS1_1: return "RHeCos-1.1";
            case RPL1_1: return "RPL-1.1";
            case RPL1_5: return "RPL-1.5";
            case RPSL1_0: return "RPSL-1.0";
            case RSAMD: return "RSA-MD";
            case RSCPL: return "RSCPL";
            case RUBY: return "Ruby";
            case SAXPD: return "SAX-PD";
            case SAXPATH: return "Saxpath";
            case SCEA: return "SCEA";
            case SENDMAIL: return "Sendmail";
            case SGIB1_0: return "SGI-B-1.0";
            case SGIB1_1: return "SGI-B-1.1";
            case SGIB2_0: return "SGI-B-2.0";
            case SIMPL2_0: return "SimPL-2.0";
            case SISSL1_2: return "SISSL-1.2";
            case SISSL: return "SISSL";
            case SLEEPYCAT: return "Sleepycat";
            case SMLNJ: return "SMLNJ";
            case SMPPL: return "SMPPL";
            case SNIA: return "SNIA";
            case SPENCER86: return "Spencer-86";
            case SPENCER94: return "Spencer-94";
            case SPENCER99: return "Spencer-99";
            case SPL1_0: return "SPL-1.0";
            case SUGARCRM1_1_3: return "SugarCRM-1.1.3";
            case SWL: return "SWL";
            case TCL: return "TCL";
            case TCPWRAPPERS: return "TCP-wrappers";
            case TMATE: return "TMate";
            case TORQUE1_1: return "TORQUE-1.1";
            case TOSL: return "TOSL";
            case UNICODEDFS2015: return "Unicode-DFS-2015";
            case UNICODEDFS2016: return "Unicode-DFS-2016";
            case UNICODETOU: return "Unicode-TOU";
            case UNLICENSE: return "Unlicense";
            case UPL1_0: return "UPL-1.0";
            case VIM: return "Vim";
            case VOSTROM: return "VOSTROM";
            case VSL1_0: return "VSL-1.0";
            case W3C19980720: return "W3C-19980720";
            case W3C20150513: return "W3C-20150513";
            case W3C: return "W3C";
            case WATCOM1_0: return "Watcom-1.0";
            case WSUIPA: return "Wsuipa";
            case WTFPL: return "WTFPL";
            case X11: return "X11";
            case XEROX: return "Xerox";
            case XFREE861_1: return "XFree86-1.1";
            case XINETD: return "xinetd";
            case XNET: return "Xnet";
            case XPP: return "xpp";
            case XSKAT: return "XSkat";
            case YPL1_0: return "YPL-1.0";
            case YPL1_1: return "YPL-1.1";
            case ZED: return "Zed";
            case ZEND2_0: return "Zend-2.0";
            case ZIMBRA1_3: return "Zimbra-1.3";
            case ZIMBRA1_4: return "Zimbra-1.4";
            case ZLIBACKNOWLEDGEMENT: return "zlib-acknowledgement";
            case ZLIB: return "Zlib";
            case ZPL1_1: return "ZPL-1.1";
            case ZPL2_0: return "ZPL-2.0";
            case ZPL2_1: return "ZPL-2.1";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTOPENSOURCE: return "http://hl7.org/fhir/spdx-license";
            case _0BSD: return "http://hl7.org/fhir/spdx-license";
            case AAL: return "http://hl7.org/fhir/spdx-license";
            case ABSTYLES: return "http://hl7.org/fhir/spdx-license";
            case ADOBE2006: return "http://hl7.org/fhir/spdx-license";
            case ADOBEGLYPH: return "http://hl7.org/fhir/spdx-license";
            case ADSL: return "http://hl7.org/fhir/spdx-license";
            case AFL1_1: return "http://hl7.org/fhir/spdx-license";
            case AFL1_2: return "http://hl7.org/fhir/spdx-license";
            case AFL2_0: return "http://hl7.org/fhir/spdx-license";
            case AFL2_1: return "http://hl7.org/fhir/spdx-license";
            case AFL3_0: return "http://hl7.org/fhir/spdx-license";
            case AFMPARSE: return "http://hl7.org/fhir/spdx-license";
            case AGPL1_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case AGPL1_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case AGPL3_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case AGPL3_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case ALADDIN: return "http://hl7.org/fhir/spdx-license";
            case AMDPLPA: return "http://hl7.org/fhir/spdx-license";
            case AML: return "http://hl7.org/fhir/spdx-license";
            case AMPAS: return "http://hl7.org/fhir/spdx-license";
            case ANTLRPD: return "http://hl7.org/fhir/spdx-license";
            case APACHE1_0: return "http://hl7.org/fhir/spdx-license";
            case APACHE1_1: return "http://hl7.org/fhir/spdx-license";
            case APACHE2_0: return "http://hl7.org/fhir/spdx-license";
            case APAFML: return "http://hl7.org/fhir/spdx-license";
            case APL1_0: return "http://hl7.org/fhir/spdx-license";
            case APSL1_0: return "http://hl7.org/fhir/spdx-license";
            case APSL1_1: return "http://hl7.org/fhir/spdx-license";
            case APSL1_2: return "http://hl7.org/fhir/spdx-license";
            case APSL2_0: return "http://hl7.org/fhir/spdx-license";
            case ARTISTIC1_0CL8: return "http://hl7.org/fhir/spdx-license";
            case ARTISTIC1_0PERL: return "http://hl7.org/fhir/spdx-license";
            case ARTISTIC1_0: return "http://hl7.org/fhir/spdx-license";
            case ARTISTIC2_0: return "http://hl7.org/fhir/spdx-license";
            case BAHYPH: return "http://hl7.org/fhir/spdx-license";
            case BARR: return "http://hl7.org/fhir/spdx-license";
            case BEERWARE: return "http://hl7.org/fhir/spdx-license";
            case BITTORRENT1_0: return "http://hl7.org/fhir/spdx-license";
            case BITTORRENT1_1: return "http://hl7.org/fhir/spdx-license";
            case BORCEUX: return "http://hl7.org/fhir/spdx-license";
            case BSD1CLAUSE: return "http://hl7.org/fhir/spdx-license";
            case BSD2CLAUSEFREEBSD: return "http://hl7.org/fhir/spdx-license";
            case BSD2CLAUSENETBSD: return "http://hl7.org/fhir/spdx-license";
            case BSD2CLAUSEPATENT: return "http://hl7.org/fhir/spdx-license";
            case BSD2CLAUSE: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSEATTRIBUTION: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSECLEAR: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSELBNL: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSENONUCLEARLICENSE2014: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSENONUCLEARLICENSE: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSENONUCLEARWARRANTY: return "http://hl7.org/fhir/spdx-license";
            case BSD3CLAUSE: return "http://hl7.org/fhir/spdx-license";
            case BSD4CLAUSEUC: return "http://hl7.org/fhir/spdx-license";
            case BSD4CLAUSE: return "http://hl7.org/fhir/spdx-license";
            case BSDPROTECTION: return "http://hl7.org/fhir/spdx-license";
            case BSDSOURCECODE: return "http://hl7.org/fhir/spdx-license";
            case BSL1_0: return "http://hl7.org/fhir/spdx-license";
            case BZIP21_0_5: return "http://hl7.org/fhir/spdx-license";
            case BZIP21_0_6: return "http://hl7.org/fhir/spdx-license";
            case CALDERA: return "http://hl7.org/fhir/spdx-license";
            case CATOSL1_1: return "http://hl7.org/fhir/spdx-license";
            case CCBY1_0: return "http://hl7.org/fhir/spdx-license";
            case CCBY2_0: return "http://hl7.org/fhir/spdx-license";
            case CCBY2_5: return "http://hl7.org/fhir/spdx-license";
            case CCBY3_0: return "http://hl7.org/fhir/spdx-license";
            case CCBY4_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNC1_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNC2_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNC2_5: return "http://hl7.org/fhir/spdx-license";
            case CCBYNC3_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNC4_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCND1_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCND2_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCND2_5: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCND3_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCND4_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCSA1_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCSA2_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCSA2_5: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCSA3_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYNCSA4_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYND1_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYND2_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYND2_5: return "http://hl7.org/fhir/spdx-license";
            case CCBYND3_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYND4_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYSA1_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYSA2_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYSA2_5: return "http://hl7.org/fhir/spdx-license";
            case CCBYSA3_0: return "http://hl7.org/fhir/spdx-license";
            case CCBYSA4_0: return "http://hl7.org/fhir/spdx-license";
            case CC01_0: return "http://hl7.org/fhir/spdx-license";
            case CDDL1_0: return "http://hl7.org/fhir/spdx-license";
            case CDDL1_1: return "http://hl7.org/fhir/spdx-license";
            case CDLAPERMISSIVE1_0: return "http://hl7.org/fhir/spdx-license";
            case CDLASHARING1_0: return "http://hl7.org/fhir/spdx-license";
            case CECILL1_0: return "http://hl7.org/fhir/spdx-license";
            case CECILL1_1: return "http://hl7.org/fhir/spdx-license";
            case CECILL2_0: return "http://hl7.org/fhir/spdx-license";
            case CECILL2_1: return "http://hl7.org/fhir/spdx-license";
            case CECILLB: return "http://hl7.org/fhir/spdx-license";
            case CECILLC: return "http://hl7.org/fhir/spdx-license";
            case CLARTISTIC: return "http://hl7.org/fhir/spdx-license";
            case CNRIJYTHON: return "http://hl7.org/fhir/spdx-license";
            case CNRIPYTHONGPLCOMPATIBLE: return "http://hl7.org/fhir/spdx-license";
            case CNRIPYTHON: return "http://hl7.org/fhir/spdx-license";
            case CONDOR1_1: return "http://hl7.org/fhir/spdx-license";
            case CPAL1_0: return "http://hl7.org/fhir/spdx-license";
            case CPL1_0: return "http://hl7.org/fhir/spdx-license";
            case CPOL1_02: return "http://hl7.org/fhir/spdx-license";
            case CROSSWORD: return "http://hl7.org/fhir/spdx-license";
            case CRYSTALSTACKER: return "http://hl7.org/fhir/spdx-license";
            case CUAOPL1_0: return "http://hl7.org/fhir/spdx-license";
            case CUBE: return "http://hl7.org/fhir/spdx-license";
            case CURL: return "http://hl7.org/fhir/spdx-license";
            case DFSL1_0: return "http://hl7.org/fhir/spdx-license";
            case DIFFMARK: return "http://hl7.org/fhir/spdx-license";
            case DOC: return "http://hl7.org/fhir/spdx-license";
            case DOTSEQN: return "http://hl7.org/fhir/spdx-license";
            case DSDP: return "http://hl7.org/fhir/spdx-license";
            case DVIPDFM: return "http://hl7.org/fhir/spdx-license";
            case ECL1_0: return "http://hl7.org/fhir/spdx-license";
            case ECL2_0: return "http://hl7.org/fhir/spdx-license";
            case EFL1_0: return "http://hl7.org/fhir/spdx-license";
            case EFL2_0: return "http://hl7.org/fhir/spdx-license";
            case EGENIX: return "http://hl7.org/fhir/spdx-license";
            case ENTESSA: return "http://hl7.org/fhir/spdx-license";
            case EPL1_0: return "http://hl7.org/fhir/spdx-license";
            case EPL2_0: return "http://hl7.org/fhir/spdx-license";
            case ERLPL1_1: return "http://hl7.org/fhir/spdx-license";
            case EUDATAGRID: return "http://hl7.org/fhir/spdx-license";
            case EUPL1_0: return "http://hl7.org/fhir/spdx-license";
            case EUPL1_1: return "http://hl7.org/fhir/spdx-license";
            case EUPL1_2: return "http://hl7.org/fhir/spdx-license";
            case EUROSYM: return "http://hl7.org/fhir/spdx-license";
            case FAIR: return "http://hl7.org/fhir/spdx-license";
            case FRAMEWORX1_0: return "http://hl7.org/fhir/spdx-license";
            case FREEIMAGE: return "http://hl7.org/fhir/spdx-license";
            case FSFAP: return "http://hl7.org/fhir/spdx-license";
            case FSFUL: return "http://hl7.org/fhir/spdx-license";
            case FSFULLR: return "http://hl7.org/fhir/spdx-license";
            case FTL: return "http://hl7.org/fhir/spdx-license";
            case GFDL1_1ONLY: return "http://hl7.org/fhir/spdx-license";
            case GFDL1_1ORLATER: return "http://hl7.org/fhir/spdx-license";
            case GFDL1_2ONLY: return "http://hl7.org/fhir/spdx-license";
            case GFDL1_2ORLATER: return "http://hl7.org/fhir/spdx-license";
            case GFDL1_3ONLY: return "http://hl7.org/fhir/spdx-license";
            case GFDL1_3ORLATER: return "http://hl7.org/fhir/spdx-license";
            case GIFTWARE: return "http://hl7.org/fhir/spdx-license";
            case GL2PS: return "http://hl7.org/fhir/spdx-license";
            case GLIDE: return "http://hl7.org/fhir/spdx-license";
            case GLULXE: return "http://hl7.org/fhir/spdx-license";
            case GNUPLOT: return "http://hl7.org/fhir/spdx-license";
            case GPL1_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case GPL1_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case GPL2_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case GPL2_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case GPL3_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case GPL3_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case GSOAP1_3B: return "http://hl7.org/fhir/spdx-license";
            case HASKELLREPORT: return "http://hl7.org/fhir/spdx-license";
            case HPND: return "http://hl7.org/fhir/spdx-license";
            case IBMPIBS: return "http://hl7.org/fhir/spdx-license";
            case ICU: return "http://hl7.org/fhir/spdx-license";
            case IJG: return "http://hl7.org/fhir/spdx-license";
            case IMAGEMAGICK: return "http://hl7.org/fhir/spdx-license";
            case IMATIX: return "http://hl7.org/fhir/spdx-license";
            case IMLIB2: return "http://hl7.org/fhir/spdx-license";
            case INFOZIP: return "http://hl7.org/fhir/spdx-license";
            case INTELACPI: return "http://hl7.org/fhir/spdx-license";
            case INTEL: return "http://hl7.org/fhir/spdx-license";
            case INTERBASE1_0: return "http://hl7.org/fhir/spdx-license";
            case IPA: return "http://hl7.org/fhir/spdx-license";
            case IPL1_0: return "http://hl7.org/fhir/spdx-license";
            case ISC: return "http://hl7.org/fhir/spdx-license";
            case JASPER2_0: return "http://hl7.org/fhir/spdx-license";
            case JSON: return "http://hl7.org/fhir/spdx-license";
            case LAL1_2: return "http://hl7.org/fhir/spdx-license";
            case LAL1_3: return "http://hl7.org/fhir/spdx-license";
            case LATEX2E: return "http://hl7.org/fhir/spdx-license";
            case LEPTONICA: return "http://hl7.org/fhir/spdx-license";
            case LGPL2_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case LGPL2_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case LGPL2_1ONLY: return "http://hl7.org/fhir/spdx-license";
            case LGPL2_1ORLATER: return "http://hl7.org/fhir/spdx-license";
            case LGPL3_0ONLY: return "http://hl7.org/fhir/spdx-license";
            case LGPL3_0ORLATER: return "http://hl7.org/fhir/spdx-license";
            case LGPLLR: return "http://hl7.org/fhir/spdx-license";
            case LIBPNG: return "http://hl7.org/fhir/spdx-license";
            case LIBTIFF: return "http://hl7.org/fhir/spdx-license";
            case LILIQP1_1: return "http://hl7.org/fhir/spdx-license";
            case LILIQR1_1: return "http://hl7.org/fhir/spdx-license";
            case LILIQRPLUS1_1: return "http://hl7.org/fhir/spdx-license";
            case LINUXOPENIB: return "http://hl7.org/fhir/spdx-license";
            case LPL1_0: return "http://hl7.org/fhir/spdx-license";
            case LPL1_02: return "http://hl7.org/fhir/spdx-license";
            case LPPL1_0: return "http://hl7.org/fhir/spdx-license";
            case LPPL1_1: return "http://hl7.org/fhir/spdx-license";
            case LPPL1_2: return "http://hl7.org/fhir/spdx-license";
            case LPPL1_3A: return "http://hl7.org/fhir/spdx-license";
            case LPPL1_3C: return "http://hl7.org/fhir/spdx-license";
            case MAKEINDEX: return "http://hl7.org/fhir/spdx-license";
            case MIROS: return "http://hl7.org/fhir/spdx-license";
            case MIT0: return "http://hl7.org/fhir/spdx-license";
            case MITADVERTISING: return "http://hl7.org/fhir/spdx-license";
            case MITCMU: return "http://hl7.org/fhir/spdx-license";
            case MITENNA: return "http://hl7.org/fhir/spdx-license";
            case MITFEH: return "http://hl7.org/fhir/spdx-license";
            case MIT: return "http://hl7.org/fhir/spdx-license";
            case MITNFA: return "http://hl7.org/fhir/spdx-license";
            case MOTOSOTO: return "http://hl7.org/fhir/spdx-license";
            case MPICH2: return "http://hl7.org/fhir/spdx-license";
            case MPL1_0: return "http://hl7.org/fhir/spdx-license";
            case MPL1_1: return "http://hl7.org/fhir/spdx-license";
            case MPL2_0NOCOPYLEFTEXCEPTION: return "http://hl7.org/fhir/spdx-license";
            case MPL2_0: return "http://hl7.org/fhir/spdx-license";
            case MSPL: return "http://hl7.org/fhir/spdx-license";
            case MSRL: return "http://hl7.org/fhir/spdx-license";
            case MTLL: return "http://hl7.org/fhir/spdx-license";
            case MULTICS: return "http://hl7.org/fhir/spdx-license";
            case MUP: return "http://hl7.org/fhir/spdx-license";
            case NASA1_3: return "http://hl7.org/fhir/spdx-license";
            case NAUMEN: return "http://hl7.org/fhir/spdx-license";
            case NBPL1_0: return "http://hl7.org/fhir/spdx-license";
            case NCSA: return "http://hl7.org/fhir/spdx-license";
            case NETSNMP: return "http://hl7.org/fhir/spdx-license";
            case NETCDF: return "http://hl7.org/fhir/spdx-license";
            case NEWSLETR: return "http://hl7.org/fhir/spdx-license";
            case NGPL: return "http://hl7.org/fhir/spdx-license";
            case NLOD1_0: return "http://hl7.org/fhir/spdx-license";
            case NLPL: return "http://hl7.org/fhir/spdx-license";
            case NOKIA: return "http://hl7.org/fhir/spdx-license";
            case NOSL: return "http://hl7.org/fhir/spdx-license";
            case NOWEB: return "http://hl7.org/fhir/spdx-license";
            case NPL1_0: return "http://hl7.org/fhir/spdx-license";
            case NPL1_1: return "http://hl7.org/fhir/spdx-license";
            case NPOSL3_0: return "http://hl7.org/fhir/spdx-license";
            case NRL: return "http://hl7.org/fhir/spdx-license";
            case NTP: return "http://hl7.org/fhir/spdx-license";
            case OCCTPL: return "http://hl7.org/fhir/spdx-license";
            case OCLC2_0: return "http://hl7.org/fhir/spdx-license";
            case ODBL1_0: return "http://hl7.org/fhir/spdx-license";
            case OFL1_0: return "http://hl7.org/fhir/spdx-license";
            case OFL1_1: return "http://hl7.org/fhir/spdx-license";
            case OGTSL: return "http://hl7.org/fhir/spdx-license";
            case OLDAP1_1: return "http://hl7.org/fhir/spdx-license";
            case OLDAP1_2: return "http://hl7.org/fhir/spdx-license";
            case OLDAP1_3: return "http://hl7.org/fhir/spdx-license";
            case OLDAP1_4: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_0_1: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_0: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_1: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_2_1: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_2_2: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_2: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_3: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_4: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_5: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_6: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_7: return "http://hl7.org/fhir/spdx-license";
            case OLDAP2_8: return "http://hl7.org/fhir/spdx-license";
            case OML: return "http://hl7.org/fhir/spdx-license";
            case OPENSSL: return "http://hl7.org/fhir/spdx-license";
            case OPL1_0: return "http://hl7.org/fhir/spdx-license";
            case OSETPL2_1: return "http://hl7.org/fhir/spdx-license";
            case OSL1_0: return "http://hl7.org/fhir/spdx-license";
            case OSL1_1: return "http://hl7.org/fhir/spdx-license";
            case OSL2_0: return "http://hl7.org/fhir/spdx-license";
            case OSL2_1: return "http://hl7.org/fhir/spdx-license";
            case OSL3_0: return "http://hl7.org/fhir/spdx-license";
            case PDDL1_0: return "http://hl7.org/fhir/spdx-license";
            case PHP3_0: return "http://hl7.org/fhir/spdx-license";
            case PHP3_01: return "http://hl7.org/fhir/spdx-license";
            case PLEXUS: return "http://hl7.org/fhir/spdx-license";
            case POSTGRESQL: return "http://hl7.org/fhir/spdx-license";
            case PSFRAG: return "http://hl7.org/fhir/spdx-license";
            case PSUTILS: return "http://hl7.org/fhir/spdx-license";
            case PYTHON2_0: return "http://hl7.org/fhir/spdx-license";
            case QHULL: return "http://hl7.org/fhir/spdx-license";
            case QPL1_0: return "http://hl7.org/fhir/spdx-license";
            case RDISC: return "http://hl7.org/fhir/spdx-license";
            case RHECOS1_1: return "http://hl7.org/fhir/spdx-license";
            case RPL1_1: return "http://hl7.org/fhir/spdx-license";
            case RPL1_5: return "http://hl7.org/fhir/spdx-license";
            case RPSL1_0: return "http://hl7.org/fhir/spdx-license";
            case RSAMD: return "http://hl7.org/fhir/spdx-license";
            case RSCPL: return "http://hl7.org/fhir/spdx-license";
            case RUBY: return "http://hl7.org/fhir/spdx-license";
            case SAXPD: return "http://hl7.org/fhir/spdx-license";
            case SAXPATH: return "http://hl7.org/fhir/spdx-license";
            case SCEA: return "http://hl7.org/fhir/spdx-license";
            case SENDMAIL: return "http://hl7.org/fhir/spdx-license";
            case SGIB1_0: return "http://hl7.org/fhir/spdx-license";
            case SGIB1_1: return "http://hl7.org/fhir/spdx-license";
            case SGIB2_0: return "http://hl7.org/fhir/spdx-license";
            case SIMPL2_0: return "http://hl7.org/fhir/spdx-license";
            case SISSL1_2: return "http://hl7.org/fhir/spdx-license";
            case SISSL: return "http://hl7.org/fhir/spdx-license";
            case SLEEPYCAT: return "http://hl7.org/fhir/spdx-license";
            case SMLNJ: return "http://hl7.org/fhir/spdx-license";
            case SMPPL: return "http://hl7.org/fhir/spdx-license";
            case SNIA: return "http://hl7.org/fhir/spdx-license";
            case SPENCER86: return "http://hl7.org/fhir/spdx-license";
            case SPENCER94: return "http://hl7.org/fhir/spdx-license";
            case SPENCER99: return "http://hl7.org/fhir/spdx-license";
            case SPL1_0: return "http://hl7.org/fhir/spdx-license";
            case SUGARCRM1_1_3: return "http://hl7.org/fhir/spdx-license";
            case SWL: return "http://hl7.org/fhir/spdx-license";
            case TCL: return "http://hl7.org/fhir/spdx-license";
            case TCPWRAPPERS: return "http://hl7.org/fhir/spdx-license";
            case TMATE: return "http://hl7.org/fhir/spdx-license";
            case TORQUE1_1: return "http://hl7.org/fhir/spdx-license";
            case TOSL: return "http://hl7.org/fhir/spdx-license";
            case UNICODEDFS2015: return "http://hl7.org/fhir/spdx-license";
            case UNICODEDFS2016: return "http://hl7.org/fhir/spdx-license";
            case UNICODETOU: return "http://hl7.org/fhir/spdx-license";
            case UNLICENSE: return "http://hl7.org/fhir/spdx-license";
            case UPL1_0: return "http://hl7.org/fhir/spdx-license";
            case VIM: return "http://hl7.org/fhir/spdx-license";
            case VOSTROM: return "http://hl7.org/fhir/spdx-license";
            case VSL1_0: return "http://hl7.org/fhir/spdx-license";
            case W3C19980720: return "http://hl7.org/fhir/spdx-license";
            case W3C20150513: return "http://hl7.org/fhir/spdx-license";
            case W3C: return "http://hl7.org/fhir/spdx-license";
            case WATCOM1_0: return "http://hl7.org/fhir/spdx-license";
            case WSUIPA: return "http://hl7.org/fhir/spdx-license";
            case WTFPL: return "http://hl7.org/fhir/spdx-license";
            case X11: return "http://hl7.org/fhir/spdx-license";
            case XEROX: return "http://hl7.org/fhir/spdx-license";
            case XFREE861_1: return "http://hl7.org/fhir/spdx-license";
            case XINETD: return "http://hl7.org/fhir/spdx-license";
            case XNET: return "http://hl7.org/fhir/spdx-license";
            case XPP: return "http://hl7.org/fhir/spdx-license";
            case XSKAT: return "http://hl7.org/fhir/spdx-license";
            case YPL1_0: return "http://hl7.org/fhir/spdx-license";
            case YPL1_1: return "http://hl7.org/fhir/spdx-license";
            case ZED: return "http://hl7.org/fhir/spdx-license";
            case ZEND2_0: return "http://hl7.org/fhir/spdx-license";
            case ZIMBRA1_3: return "http://hl7.org/fhir/spdx-license";
            case ZIMBRA1_4: return "http://hl7.org/fhir/spdx-license";
            case ZLIBACKNOWLEDGEMENT: return "http://hl7.org/fhir/spdx-license";
            case ZLIB: return "http://hl7.org/fhir/spdx-license";
            case ZPL1_1: return "http://hl7.org/fhir/spdx-license";
            case ZPL2_0: return "http://hl7.org/fhir/spdx-license";
            case ZPL2_1: return "http://hl7.org/fhir/spdx-license";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOTOPENSOURCE: return "Not an open source license.";
            case _0BSD: return "BSD Zero Clause License.";
            case AAL: return "Attribution Assurance License.";
            case ABSTYLES: return "Abstyles License.";
            case ADOBE2006: return "Adobe Systems Incorporated Source Code License Agreement.";
            case ADOBEGLYPH: return "Adobe Glyph List License.";
            case ADSL: return "Amazon Digital Services License.";
            case AFL1_1: return "Academic Free License v1.1.";
            case AFL1_2: return "Academic Free License v1.2.";
            case AFL2_0: return "Academic Free License v2.0.";
            case AFL2_1: return "Academic Free License v2.1.";
            case AFL3_0: return "Academic Free License v3.0.";
            case AFMPARSE: return "Afmparse License.";
            case AGPL1_0ONLY: return "Affero General Public License v1.0 only.";
            case AGPL1_0ORLATER: return "Affero General Public License v1.0 or later.";
            case AGPL3_0ONLY: return "GNU Affero General Public License v3.0 only.";
            case AGPL3_0ORLATER: return "GNU Affero General Public License v3.0 or later.";
            case ALADDIN: return "Aladdin Free Public License.";
            case AMDPLPA: return "AMD's plpa_map.c License.";
            case AML: return "Apple MIT License.";
            case AMPAS: return "Academy of Motion Picture Arts and Sciences BSD.";
            case ANTLRPD: return "ANTLR Software Rights Notice.";
            case APACHE1_0: return "Apache License 1.0.";
            case APACHE1_1: return "Apache License 1.1.";
            case APACHE2_0: return "Apache License 2.0.";
            case APAFML: return "Adobe Postscript AFM License.";
            case APL1_0: return "Adaptive Public License 1.0.";
            case APSL1_0: return "Apple Public Source License 1.0.";
            case APSL1_1: return "Apple Public Source License 1.1.";
            case APSL1_2: return "Apple Public Source License 1.2.";
            case APSL2_0: return "Apple Public Source License 2.0.";
            case ARTISTIC1_0CL8: return "Artistic License 1.0 w/clause 8.";
            case ARTISTIC1_0PERL: return "Artistic License 1.0 (Perl).";
            case ARTISTIC1_0: return "Artistic License 1.0.";
            case ARTISTIC2_0: return "Artistic License 2.0.";
            case BAHYPH: return "Bahyph License.";
            case BARR: return "Barr License.";
            case BEERWARE: return "Beerware License.";
            case BITTORRENT1_0: return "BitTorrent Open Source License v1.0.";
            case BITTORRENT1_1: return "BitTorrent Open Source License v1.1.";
            case BORCEUX: return "Borceux license.";
            case BSD1CLAUSE: return "BSD 1-Clause License.";
            case BSD2CLAUSEFREEBSD: return "BSD 2-Clause FreeBSD License.";
            case BSD2CLAUSENETBSD: return "BSD 2-Clause NetBSD License.";
            case BSD2CLAUSEPATENT: return "BSD-2-Clause Plus Patent License.";
            case BSD2CLAUSE: return "BSD 2-Clause \"Simplified\" License.";
            case BSD3CLAUSEATTRIBUTION: return "BSD with attribution.";
            case BSD3CLAUSECLEAR: return "BSD 3-Clause Clear License.";
            case BSD3CLAUSELBNL: return "Lawrence Berkeley National Labs BSD variant license.";
            case BSD3CLAUSENONUCLEARLICENSE2014: return "BSD 3-Clause No Nuclear License 2014.";
            case BSD3CLAUSENONUCLEARLICENSE: return "BSD 3-Clause No Nuclear License.";
            case BSD3CLAUSENONUCLEARWARRANTY: return "BSD 3-Clause No Nuclear Warranty.";
            case BSD3CLAUSE: return "BSD 3-Clause \"New\" or \"Revised\" License.";
            case BSD4CLAUSEUC: return "BSD-4-Clause (University of California-Specific).";
            case BSD4CLAUSE: return "BSD 4-Clause \"Original\" or \"Old\" License.";
            case BSDPROTECTION: return "BSD Protection License.";
            case BSDSOURCECODE: return "BSD Source Code Attribution.";
            case BSL1_0: return "Boost Software License 1.0.";
            case BZIP21_0_5: return "bzip2 and libbzip2 License v1.0.5.";
            case BZIP21_0_6: return "bzip2 and libbzip2 License v1.0.6.";
            case CALDERA: return "Caldera License.";
            case CATOSL1_1: return "Computer Associates Trusted Open Source License 1.1.";
            case CCBY1_0: return "Creative Commons Attribution 1.0 Generic.";
            case CCBY2_0: return "Creative Commons Attribution 2.0 Generic.";
            case CCBY2_5: return "Creative Commons Attribution 2.5 Generic.";
            case CCBY3_0: return "Creative Commons Attribution 3.0 Unported.";
            case CCBY4_0: return "Creative Commons Attribution 4.0 International.";
            case CCBYNC1_0: return "Creative Commons Attribution Non Commercial 1.0 Generic.";
            case CCBYNC2_0: return "Creative Commons Attribution Non Commercial 2.0 Generic.";
            case CCBYNC2_5: return "Creative Commons Attribution Non Commercial 2.5 Generic.";
            case CCBYNC3_0: return "Creative Commons Attribution Non Commercial 3.0 Unported.";
            case CCBYNC4_0: return "Creative Commons Attribution Non Commercial 4.0 International.";
            case CCBYNCND1_0: return "Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic.";
            case CCBYNCND2_0: return "Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic.";
            case CCBYNCND2_5: return "Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic.";
            case CCBYNCND3_0: return "Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported.";
            case CCBYNCND4_0: return "Creative Commons Attribution Non Commercial No Derivatives 4.0 International.";
            case CCBYNCSA1_0: return "Creative Commons Attribution Non Commercial Share Alike 1.0 Generic.";
            case CCBYNCSA2_0: return "Creative Commons Attribution Non Commercial Share Alike 2.0 Generic.";
            case CCBYNCSA2_5: return "Creative Commons Attribution Non Commercial Share Alike 2.5 Generic.";
            case CCBYNCSA3_0: return "Creative Commons Attribution Non Commercial Share Alike 3.0 Unported.";
            case CCBYNCSA4_0: return "Creative Commons Attribution Non Commercial Share Alike 4.0 International.";
            case CCBYND1_0: return "Creative Commons Attribution No Derivatives 1.0 Generic.";
            case CCBYND2_0: return "Creative Commons Attribution No Derivatives 2.0 Generic.";
            case CCBYND2_5: return "Creative Commons Attribution No Derivatives 2.5 Generic.";
            case CCBYND3_0: return "Creative Commons Attribution No Derivatives 3.0 Unported.";
            case CCBYND4_0: return "Creative Commons Attribution No Derivatives 4.0 International.";
            case CCBYSA1_0: return "Creative Commons Attribution Share Alike 1.0 Generic.";
            case CCBYSA2_0: return "Creative Commons Attribution Share Alike 2.0 Generic.";
            case CCBYSA2_5: return "Creative Commons Attribution Share Alike 2.5 Generic.";
            case CCBYSA3_0: return "Creative Commons Attribution Share Alike 3.0 Unported.";
            case CCBYSA4_0: return "Creative Commons Attribution Share Alike 4.0 International.";
            case CC01_0: return "Creative Commons Zero v1.0 Universal.";
            case CDDL1_0: return "Common Development and Distribution License 1.0.";
            case CDDL1_1: return "Common Development and Distribution License 1.1.";
            case CDLAPERMISSIVE1_0: return "Community Data License Agreement Permissive 1.0.";
            case CDLASHARING1_0: return "Community Data License Agreement Sharing 1.0.";
            case CECILL1_0: return "CeCILL Free Software License Agreement v1.0.";
            case CECILL1_1: return "CeCILL Free Software License Agreement v1.1.";
            case CECILL2_0: return "CeCILL Free Software License Agreement v2.0.";
            case CECILL2_1: return "CeCILL Free Software License Agreement v2.1.";
            case CECILLB: return "CeCILL-B Free Software License Agreement.";
            case CECILLC: return "CeCILL-C Free Software License Agreement.";
            case CLARTISTIC: return "Clarified Artistic License.";
            case CNRIJYTHON: return "CNRI Jython License.";
            case CNRIPYTHONGPLCOMPATIBLE: return "CNRI Python Open Source GPL Compatible License Agreement.";
            case CNRIPYTHON: return "CNRI Python License.";
            case CONDOR1_1: return "Condor Public License v1.1.";
            case CPAL1_0: return "Common Public Attribution License 1.0.";
            case CPL1_0: return "Common Public License 1.0.";
            case CPOL1_02: return "Code Project Open License 1.02.";
            case CROSSWORD: return "Crossword License.";
            case CRYSTALSTACKER: return "CrystalStacker License.";
            case CUAOPL1_0: return "CUA Office Public License v1.0.";
            case CUBE: return "Cube License.";
            case CURL: return "curl License.";
            case DFSL1_0: return "Deutsche Freie Software Lizenz.";
            case DIFFMARK: return "diffmark license.";
            case DOC: return "DOC License.";
            case DOTSEQN: return "Dotseqn License.";
            case DSDP: return "DSDP License.";
            case DVIPDFM: return "dvipdfm License.";
            case ECL1_0: return "Educational Community License v1.0.";
            case ECL2_0: return "Educational Community License v2.0.";
            case EFL1_0: return "Eiffel Forum License v1.0.";
            case EFL2_0: return "Eiffel Forum License v2.0.";
            case EGENIX: return "eGenix.com Public License 1.1.0.";
            case ENTESSA: return "Entessa Public License v1.0.";
            case EPL1_0: return "Eclipse Public License 1.0.";
            case EPL2_0: return "Eclipse Public License 2.0.";
            case ERLPL1_1: return "Erlang Public License v1.1.";
            case EUDATAGRID: return "EU DataGrid Software License.";
            case EUPL1_0: return "European Union Public License 1.0.";
            case EUPL1_1: return "European Union Public License 1.1.";
            case EUPL1_2: return "European Union Public License 1.2.";
            case EUROSYM: return "Eurosym License.";
            case FAIR: return "Fair License.";
            case FRAMEWORX1_0: return "Frameworx Open License 1.0.";
            case FREEIMAGE: return "FreeImage Public License v1.0.";
            case FSFAP: return "FSF All Permissive License.";
            case FSFUL: return "FSF Unlimited License.";
            case FSFULLR: return "FSF Unlimited License (with License Retention).";
            case FTL: return "Freetype Project License.";
            case GFDL1_1ONLY: return "GNU Free Documentation License v1.1 only.";
            case GFDL1_1ORLATER: return "GNU Free Documentation License v1.1 or later.";
            case GFDL1_2ONLY: return "GNU Free Documentation License v1.2 only.";
            case GFDL1_2ORLATER: return "GNU Free Documentation License v1.2 or later.";
            case GFDL1_3ONLY: return "GNU Free Documentation License v1.3 only.";
            case GFDL1_3ORLATER: return "GNU Free Documentation License v1.3 or later.";
            case GIFTWARE: return "Giftware License.";
            case GL2PS: return "GL2PS License.";
            case GLIDE: return "3dfx Glide License.";
            case GLULXE: return "Glulxe License.";
            case GNUPLOT: return "gnuplot License.";
            case GPL1_0ONLY: return "GNU General Public License v1.0 only.";
            case GPL1_0ORLATER: return "GNU General Public License v1.0 or later.";
            case GPL2_0ONLY: return "GNU General Public License v2.0 only.";
            case GPL2_0ORLATER: return "GNU General Public License v2.0 or later.";
            case GPL3_0ONLY: return "GNU General Public License v3.0 only.";
            case GPL3_0ORLATER: return "GNU General Public License v3.0 or later.";
            case GSOAP1_3B: return "gSOAP Public License v1.3b.";
            case HASKELLREPORT: return "Haskell Language Report License.";
            case HPND: return "Historical Permission Notice and Disclaimer.";
            case IBMPIBS: return "IBM PowerPC Initialization and Boot Software.";
            case ICU: return "ICU License.";
            case IJG: return "Independent JPEG Group License.";
            case IMAGEMAGICK: return "ImageMagick License.";
            case IMATIX: return "iMatix Standard Function Library Agreement.";
            case IMLIB2: return "Imlib2 License.";
            case INFOZIP: return "Info-ZIP License.";
            case INTELACPI: return "Intel ACPI Software License Agreement.";
            case INTEL: return "Intel Open Source License.";
            case INTERBASE1_0: return "Interbase Public License v1.0.";
            case IPA: return "IPA Font License.";
            case IPL1_0: return "IBM Public License v1.0.";
            case ISC: return "ISC License.";
            case JASPER2_0: return "JasPer License.";
            case JSON: return "JSON License.";
            case LAL1_2: return "Licence Art Libre 1.2.";
            case LAL1_3: return "Licence Art Libre 1.3.";
            case LATEX2E: return "Latex2e License.";
            case LEPTONICA: return "Leptonica License.";
            case LGPL2_0ONLY: return "GNU Library General Public License v2 only.";
            case LGPL2_0ORLATER: return "GNU Library General Public License v2 or later.";
            case LGPL2_1ONLY: return "GNU Lesser General Public License v2.1 only.";
            case LGPL2_1ORLATER: return "GNU Lesser General Public License v2.1 or later.";
            case LGPL3_0ONLY: return "GNU Lesser General Public License v3.0 only.";
            case LGPL3_0ORLATER: return "GNU Lesser General Public License v3.0 or later.";
            case LGPLLR: return "Lesser General Public License For Linguistic Resources.";
            case LIBPNG: return "libpng License.";
            case LIBTIFF: return "libtiff License.";
            case LILIQP1_1: return "Licence Libre du Québec – Permissive version 1.1.";
            case LILIQR1_1: return "Licence Libre du Québec – Réciprocité version 1.1.";
            case LILIQRPLUS1_1: return "Licence Libre du Québec – Réciprocité forte version 1.1.";
            case LINUXOPENIB: return "Linux Kernel Variant of OpenIB.org license.";
            case LPL1_0: return "Lucent Public License Version 1.0.";
            case LPL1_02: return "Lucent Public License v1.02.";
            case LPPL1_0: return "LaTeX Project Public License v1.0.";
            case LPPL1_1: return "LaTeX Project Public License v1.1.";
            case LPPL1_2: return "LaTeX Project Public License v1.2.";
            case LPPL1_3A: return "LaTeX Project Public License v1.3a.";
            case LPPL1_3C: return "LaTeX Project Public License v1.3c.";
            case MAKEINDEX: return "MakeIndex License.";
            case MIROS: return "MirOS License.";
            case MIT0: return "MIT No Attribution.";
            case MITADVERTISING: return "Enlightenment License (e16).";
            case MITCMU: return "CMU License.";
            case MITENNA: return "enna License.";
            case MITFEH: return "feh License.";
            case MIT: return "MIT License.";
            case MITNFA: return "MIT +no-false-attribs license.";
            case MOTOSOTO: return "Motosoto License.";
            case MPICH2: return "mpich2 License.";
            case MPL1_0: return "Mozilla Public License 1.0.";
            case MPL1_1: return "Mozilla Public License 1.1.";
            case MPL2_0NOCOPYLEFTEXCEPTION: return "Mozilla Public License 2.0 (no copyleft exception).";
            case MPL2_0: return "Mozilla Public License 2.0.";
            case MSPL: return "Microsoft Public License.";
            case MSRL: return "Microsoft Reciprocal License.";
            case MTLL: return "Matrix Template Library License.";
            case MULTICS: return "Multics License.";
            case MUP: return "Mup License.";
            case NASA1_3: return "NASA Open Source Agreement 1.3.";
            case NAUMEN: return "Naumen Public License.";
            case NBPL1_0: return "Net Boolean Public License v1.";
            case NCSA: return "University of Illinois/NCSA Open Source License.";
            case NETSNMP: return "Net-SNMP License.";
            case NETCDF: return "NetCDF license.";
            case NEWSLETR: return "Newsletr License.";
            case NGPL: return "Nethack General Public License.";
            case NLOD1_0: return "Norwegian Licence for Open Government Data.";
            case NLPL: return "No Limit Public License.";
            case NOKIA: return "Nokia Open Source License.";
            case NOSL: return "Netizen Open Source License.";
            case NOWEB: return "Noweb License.";
            case NPL1_0: return "Netscape Public License v1.0.";
            case NPL1_1: return "Netscape Public License v1.1.";
            case NPOSL3_0: return "Non-Profit Open Software License 3.0.";
            case NRL: return "NRL License.";
            case NTP: return "NTP License.";
            case OCCTPL: return "Open CASCADE Technology Public License.";
            case OCLC2_0: return "OCLC Research Public License 2.0.";
            case ODBL1_0: return "ODC Open Database License v1.0.";
            case OFL1_0: return "SIL Open Font License 1.0.";
            case OFL1_1: return "SIL Open Font License 1.1.";
            case OGTSL: return "Open Group Test Suite License.";
            case OLDAP1_1: return "Open LDAP Public License v1.1.";
            case OLDAP1_2: return "Open LDAP Public License v1.2.";
            case OLDAP1_3: return "Open LDAP Public License v1.3.";
            case OLDAP1_4: return "Open LDAP Public License v1.4.";
            case OLDAP2_0_1: return "Open LDAP Public License v2.0.1.";
            case OLDAP2_0: return "Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B).";
            case OLDAP2_1: return "Open LDAP Public License v2.1.";
            case OLDAP2_2_1: return "Open LDAP Public License v2.2.1.";
            case OLDAP2_2_2: return "Open LDAP Public License 2.2.2.";
            case OLDAP2_2: return "Open LDAP Public License v2.2.";
            case OLDAP2_3: return "Open LDAP Public License v2.3.";
            case OLDAP2_4: return "Open LDAP Public License v2.4.";
            case OLDAP2_5: return "Open LDAP Public License v2.5.";
            case OLDAP2_6: return "Open LDAP Public License v2.6.";
            case OLDAP2_7: return "Open LDAP Public License v2.7.";
            case OLDAP2_8: return "Open LDAP Public License v2.8.";
            case OML: return "Open Market License.";
            case OPENSSL: return "OpenSSL License.";
            case OPL1_0: return "Open Public License v1.0.";
            case OSETPL2_1: return "OSET Public License version 2.1.";
            case OSL1_0: return "Open Software License 1.0.";
            case OSL1_1: return "Open Software License 1.1.";
            case OSL2_0: return "Open Software License 2.0.";
            case OSL2_1: return "Open Software License 2.1.";
            case OSL3_0: return "Open Software License 3.0.";
            case PDDL1_0: return "ODC Public Domain Dedication & License 1.0.";
            case PHP3_0: return "PHP License v3.0.";
            case PHP3_01: return "PHP License v3.01.";
            case PLEXUS: return "Plexus Classworlds License.";
            case POSTGRESQL: return "PostgreSQL License.";
            case PSFRAG: return "psfrag License.";
            case PSUTILS: return "psutils License.";
            case PYTHON2_0: return "Python License 2.0.";
            case QHULL: return "Qhull License.";
            case QPL1_0: return "Q Public License 1.0.";
            case RDISC: return "Rdisc License.";
            case RHECOS1_1: return "Red Hat eCos Public License v1.1.";
            case RPL1_1: return "Reciprocal Public License 1.1.";
            case RPL1_5: return "Reciprocal Public License 1.5.";
            case RPSL1_0: return "RealNetworks Public Source License v1.0.";
            case RSAMD: return "RSA Message-Digest License.";
            case RSCPL: return "Ricoh Source Code Public License.";
            case RUBY: return "Ruby License.";
            case SAXPD: return "Sax Public Domain Notice.";
            case SAXPATH: return "Saxpath License.";
            case SCEA: return "SCEA Shared Source License.";
            case SENDMAIL: return "Sendmail License.";
            case SGIB1_0: return "SGI Free Software License B v1.0.";
            case SGIB1_1: return "SGI Free Software License B v1.1.";
            case SGIB2_0: return "SGI Free Software License B v2.0.";
            case SIMPL2_0: return "Simple Public License 2.0.";
            case SISSL1_2: return "Sun Industry Standards Source License v1.2.";
            case SISSL: return "Sun Industry Standards Source License v1.1.";
            case SLEEPYCAT: return "Sleepycat License.";
            case SMLNJ: return "Standard ML of New Jersey License.";
            case SMPPL: return "Secure Messaging Protocol Public License.";
            case SNIA: return "SNIA Public License 1.1.";
            case SPENCER86: return "Spencer License 86.";
            case SPENCER94: return "Spencer License 94.";
            case SPENCER99: return "Spencer License 99.";
            case SPL1_0: return "Sun Public License v1.0.";
            case SUGARCRM1_1_3: return "SugarCRM Public License v1.1.3.";
            case SWL: return "Scheme Widget Library (SWL) Software License Agreement.";
            case TCL: return "TCL/TK License.";
            case TCPWRAPPERS: return "TCP Wrappers License.";
            case TMATE: return "TMate Open Source License.";
            case TORQUE1_1: return "TORQUE v2.5+ Software License v1.1.";
            case TOSL: return "Trusster Open Source License.";
            case UNICODEDFS2015: return "Unicode License Agreement - Data Files and Software (2015).";
            case UNICODEDFS2016: return "Unicode License Agreement - Data Files and Software (2016).";
            case UNICODETOU: return "Unicode Terms of Use.";
            case UNLICENSE: return "The Unlicense.";
            case UPL1_0: return "Universal Permissive License v1.0.";
            case VIM: return "Vim License.";
            case VOSTROM: return "VOSTROM Public License for Open Source.";
            case VSL1_0: return "Vovida Software License v1.0.";
            case W3C19980720: return "W3C Software Notice and License (1998-07-20).";
            case W3C20150513: return "W3C Software Notice and Document License (2015-05-13).";
            case W3C: return "W3C Software Notice and License (2002-12-31).";
            case WATCOM1_0: return "Sybase Open Watcom Public License 1.0.";
            case WSUIPA: return "Wsuipa License.";
            case WTFPL: return "Do What The F*ck You Want To Public License.";
            case X11: return "X11 License.";
            case XEROX: return "Xerox License.";
            case XFREE861_1: return "XFree86 License 1.1.";
            case XINETD: return "xinetd License.";
            case XNET: return "X.Net License.";
            case XPP: return "XPP License.";
            case XSKAT: return "XSkat License.";
            case YPL1_0: return "Yahoo! Public License v1.0.";
            case YPL1_1: return "Yahoo! Public License v1.1.";
            case ZED: return "Zed License.";
            case ZEND2_0: return "Zend License v2.0.";
            case ZIMBRA1_3: return "Zimbra Public License v1.3.";
            case ZIMBRA1_4: return "Zimbra Public License v1.4.";
            case ZLIBACKNOWLEDGEMENT: return "zlib/libpng License with Acknowledgement.";
            case ZLIB: return "zlib License.";
            case ZPL1_1: return "Zope Public License 1.1.";
            case ZPL2_0: return "Zope Public License 2.0.";
            case ZPL2_1: return "Zope Public License 2.1.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTOPENSOURCE: return "Not open source";
            case _0BSD: return "BSD Zero Clause License";
            case AAL: return "Attribution Assurance License";
            case ABSTYLES: return "Abstyles License";
            case ADOBE2006: return "Adobe Systems Incorporated Source Code License Agreement";
            case ADOBEGLYPH: return "Adobe Glyph List License";
            case ADSL: return "Amazon Digital Services License";
            case AFL1_1: return "Academic Free License v1.1";
            case AFL1_2: return "Academic Free License v1.2";
            case AFL2_0: return "Academic Free License v2.0";
            case AFL2_1: return "Academic Free License v2.1";
            case AFL3_0: return "Academic Free License v3.0";
            case AFMPARSE: return "Afmparse License";
            case AGPL1_0ONLY: return "Affero General Public License v1.0 only";
            case AGPL1_0ORLATER: return "Affero General Public License v1.0 or later";
            case AGPL3_0ONLY: return "GNU Affero General Public License v3.0 only";
            case AGPL3_0ORLATER: return "GNU Affero General Public License v3.0 or later";
            case ALADDIN: return "Aladdin Free Public License";
            case AMDPLPA: return "AMD's plpa_map.c License";
            case AML: return "Apple MIT License";
            case AMPAS: return "Academy of Motion Picture Arts and Sciences BSD";
            case ANTLRPD: return "ANTLR Software Rights Notice";
            case APACHE1_0: return "Apache License 1.0";
            case APACHE1_1: return "Apache License 1.1";
            case APACHE2_0: return "Apache License 2.0";
            case APAFML: return "Adobe Postscript AFM License";
            case APL1_0: return "Adaptive Public License 1.0";
            case APSL1_0: return "Apple Public Source License 1.0";
            case APSL1_1: return "Apple Public Source License 1.1";
            case APSL1_2: return "Apple Public Source License 1.2";
            case APSL2_0: return "Apple Public Source License 2.0";
            case ARTISTIC1_0CL8: return "Artistic License 1.0 w/clause 8";
            case ARTISTIC1_0PERL: return "Artistic License 1.0 (Perl)";
            case ARTISTIC1_0: return "Artistic License 1.0";
            case ARTISTIC2_0: return "Artistic License 2.0";
            case BAHYPH: return "Bahyph License";
            case BARR: return "Barr License";
            case BEERWARE: return "Beerware License";
            case BITTORRENT1_0: return "BitTorrent Open Source License v1.0";
            case BITTORRENT1_1: return "BitTorrent Open Source License v1.1";
            case BORCEUX: return "Borceux license";
            case BSD1CLAUSE: return "BSD 1-Clause License";
            case BSD2CLAUSEFREEBSD: return "BSD 2-Clause FreeBSD License";
            case BSD2CLAUSENETBSD: return "BSD 2-Clause NetBSD License";
            case BSD2CLAUSEPATENT: return "BSD-2-Clause Plus Patent License";
            case BSD2CLAUSE: return "BSD 2-Clause \"Simplified\" License";
            case BSD3CLAUSEATTRIBUTION: return "BSD with attribution";
            case BSD3CLAUSECLEAR: return "BSD 3-Clause Clear License";
            case BSD3CLAUSELBNL: return "Lawrence Berkeley National Labs BSD variant license";
            case BSD3CLAUSENONUCLEARLICENSE2014: return "BSD 3-Clause No Nuclear License 2014";
            case BSD3CLAUSENONUCLEARLICENSE: return "BSD 3-Clause No Nuclear License";
            case BSD3CLAUSENONUCLEARWARRANTY: return "BSD 3-Clause No Nuclear Warranty";
            case BSD3CLAUSE: return "BSD 3-Clause \"New\" or \"Revised\" License";
            case BSD4CLAUSEUC: return "BSD-4-Clause (University of California-Specific)";
            case BSD4CLAUSE: return "BSD 4-Clause \"Original\" or \"Old\" License";
            case BSDPROTECTION: return "BSD Protection License";
            case BSDSOURCECODE: return "BSD Source Code Attribution";
            case BSL1_0: return "Boost Software License 1.0";
            case BZIP21_0_5: return "bzip2 and libbzip2 License v1.0.5";
            case BZIP21_0_6: return "bzip2 and libbzip2 License v1.0.6";
            case CALDERA: return "Caldera License";
            case CATOSL1_1: return "Computer Associates Trusted Open Source License 1.1";
            case CCBY1_0: return "Creative Commons Attribution 1.0 Generic";
            case CCBY2_0: return "Creative Commons Attribution 2.0 Generic";
            case CCBY2_5: return "Creative Commons Attribution 2.5 Generic";
            case CCBY3_0: return "Creative Commons Attribution 3.0 Unported";
            case CCBY4_0: return "Creative Commons Attribution 4.0 International";
            case CCBYNC1_0: return "Creative Commons Attribution Non Commercial 1.0 Generic";
            case CCBYNC2_0: return "Creative Commons Attribution Non Commercial 2.0 Generic";
            case CCBYNC2_5: return "Creative Commons Attribution Non Commercial 2.5 Generic";
            case CCBYNC3_0: return "Creative Commons Attribution Non Commercial 3.0 Unported";
            case CCBYNC4_0: return "Creative Commons Attribution Non Commercial 4.0 International";
            case CCBYNCND1_0: return "Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic";
            case CCBYNCND2_0: return "Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic";
            case CCBYNCND2_5: return "Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic";
            case CCBYNCND3_0: return "Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported";
            case CCBYNCND4_0: return "Creative Commons Attribution Non Commercial No Derivatives 4.0 International";
            case CCBYNCSA1_0: return "Creative Commons Attribution Non Commercial Share Alike 1.0 Generic";
            case CCBYNCSA2_0: return "Creative Commons Attribution Non Commercial Share Alike 2.0 Generic";
            case CCBYNCSA2_5: return "Creative Commons Attribution Non Commercial Share Alike 2.5 Generic";
            case CCBYNCSA3_0: return "Creative Commons Attribution Non Commercial Share Alike 3.0 Unported";
            case CCBYNCSA4_0: return "Creative Commons Attribution Non Commercial Share Alike 4.0 International";
            case CCBYND1_0: return "Creative Commons Attribution No Derivatives 1.0 Generic";
            case CCBYND2_0: return "Creative Commons Attribution No Derivatives 2.0 Generic";
            case CCBYND2_5: return "Creative Commons Attribution No Derivatives 2.5 Generic";
            case CCBYND3_0: return "Creative Commons Attribution No Derivatives 3.0 Unported";
            case CCBYND4_0: return "Creative Commons Attribution No Derivatives 4.0 International";
            case CCBYSA1_0: return "Creative Commons Attribution Share Alike 1.0 Generic";
            case CCBYSA2_0: return "Creative Commons Attribution Share Alike 2.0 Generic";
            case CCBYSA2_5: return "Creative Commons Attribution Share Alike 2.5 Generic";
            case CCBYSA3_0: return "Creative Commons Attribution Share Alike 3.0 Unported";
            case CCBYSA4_0: return "Creative Commons Attribution Share Alike 4.0 International";
            case CC01_0: return "Creative Commons Zero v1.0 Universal";
            case CDDL1_0: return "Common Development and Distribution License 1.0";
            case CDDL1_1: return "Common Development and Distribution License 1.1";
            case CDLAPERMISSIVE1_0: return "Community Data License Agreement Permissive 1.0";
            case CDLASHARING1_0: return "Community Data License Agreement Sharing 1.0";
            case CECILL1_0: return "CeCILL Free Software License Agreement v1.0";
            case CECILL1_1: return "CeCILL Free Software License Agreement v1.1";
            case CECILL2_0: return "CeCILL Free Software License Agreement v2.0";
            case CECILL2_1: return "CeCILL Free Software License Agreement v2.1";
            case CECILLB: return "CeCILL-B Free Software License Agreement";
            case CECILLC: return "CeCILL-C Free Software License Agreement";
            case CLARTISTIC: return "Clarified Artistic License";
            case CNRIJYTHON: return "CNRI Jython License";
            case CNRIPYTHONGPLCOMPATIBLE: return "CNRI Python Open Source GPL Compatible License Agreement";
            case CNRIPYTHON: return "CNRI Python License";
            case CONDOR1_1: return "Condor Public License v1.1";
            case CPAL1_0: return "Common Public Attribution License 1.0";
            case CPL1_0: return "Common Public License 1.0";
            case CPOL1_02: return "Code Project Open License 1.02";
            case CROSSWORD: return "Crossword License";
            case CRYSTALSTACKER: return "CrystalStacker License";
            case CUAOPL1_0: return "CUA Office Public License v1.0";
            case CUBE: return "Cube License";
            case CURL: return "curl License";
            case DFSL1_0: return "Deutsche Freie Software Lizenz";
            case DIFFMARK: return "diffmark license";
            case DOC: return "DOC License";
            case DOTSEQN: return "Dotseqn License";
            case DSDP: return "DSDP License";
            case DVIPDFM: return "dvipdfm License";
            case ECL1_0: return "Educational Community License v1.0";
            case ECL2_0: return "Educational Community License v2.0";
            case EFL1_0: return "Eiffel Forum License v1.0";
            case EFL2_0: return "Eiffel Forum License v2.0";
            case EGENIX: return "eGenix.com Public License 1.1.0";
            case ENTESSA: return "Entessa Public License v1.0";
            case EPL1_0: return "Eclipse Public License 1.0";
            case EPL2_0: return "Eclipse Public License 2.0";
            case ERLPL1_1: return "Erlang Public License v1.1";
            case EUDATAGRID: return "EU DataGrid Software License";
            case EUPL1_0: return "European Union Public License 1.0";
            case EUPL1_1: return "European Union Public License 1.1";
            case EUPL1_2: return "European Union Public License 1.2";
            case EUROSYM: return "Eurosym License";
            case FAIR: return "Fair License";
            case FRAMEWORX1_0: return "Frameworx Open License 1.0";
            case FREEIMAGE: return "FreeImage Public License v1.0";
            case FSFAP: return "FSF All Permissive License";
            case FSFUL: return "FSF Unlimited License";
            case FSFULLR: return "FSF Unlimited License (with License Retention)";
            case FTL: return "Freetype Project License";
            case GFDL1_1ONLY: return "GNU Free Documentation License v1.1 only";
            case GFDL1_1ORLATER: return "GNU Free Documentation License v1.1 or later";
            case GFDL1_2ONLY: return "GNU Free Documentation License v1.2 only";
            case GFDL1_2ORLATER: return "GNU Free Documentation License v1.2 or later";
            case GFDL1_3ONLY: return "GNU Free Documentation License v1.3 only";
            case GFDL1_3ORLATER: return "GNU Free Documentation License v1.3 or later";
            case GIFTWARE: return "Giftware License";
            case GL2PS: return "GL2PS License";
            case GLIDE: return "3dfx Glide License";
            case GLULXE: return "Glulxe License";
            case GNUPLOT: return "gnuplot License";
            case GPL1_0ONLY: return "GNU General Public License v1.0 only";
            case GPL1_0ORLATER: return "GNU General Public License v1.0 or later";
            case GPL2_0ONLY: return "GNU General Public License v2.0 only";
            case GPL2_0ORLATER: return "GNU General Public License v2.0 or later";
            case GPL3_0ONLY: return "GNU General Public License v3.0 only";
            case GPL3_0ORLATER: return "GNU General Public License v3.0 or later";
            case GSOAP1_3B: return "gSOAP Public License v1.3b";
            case HASKELLREPORT: return "Haskell Language Report License";
            case HPND: return "Historical Permission Notice and Disclaimer";
            case IBMPIBS: return "IBM PowerPC Initialization and Boot Software";
            case ICU: return "ICU License";
            case IJG: return "Independent JPEG Group License";
            case IMAGEMAGICK: return "ImageMagick License";
            case IMATIX: return "iMatix Standard Function Library Agreement";
            case IMLIB2: return "Imlib2 License";
            case INFOZIP: return "Info-ZIP License";
            case INTELACPI: return "Intel ACPI Software License Agreement";
            case INTEL: return "Intel Open Source License";
            case INTERBASE1_0: return "Interbase Public License v1.0";
            case IPA: return "IPA Font License";
            case IPL1_0: return "IBM Public License v1.0";
            case ISC: return "ISC License";
            case JASPER2_0: return "JasPer License";
            case JSON: return "JSON License";
            case LAL1_2: return "Licence Art Libre 1.2";
            case LAL1_3: return "Licence Art Libre 1.3";
            case LATEX2E: return "Latex2e License";
            case LEPTONICA: return "Leptonica License";
            case LGPL2_0ONLY: return "GNU Library General Public License v2 only";
            case LGPL2_0ORLATER: return "GNU Library General Public License v2 or later";
            case LGPL2_1ONLY: return "GNU Lesser General Public License v2.1 only";
            case LGPL2_1ORLATER: return "GNU Lesser General Public License v2.1 or later";
            case LGPL3_0ONLY: return "GNU Lesser General Public License v3.0 only";
            case LGPL3_0ORLATER: return "GNU Lesser General Public License v3.0 or later";
            case LGPLLR: return "Lesser General Public License For Linguistic Resources";
            case LIBPNG: return "libpng License";
            case LIBTIFF: return "libtiff License";
            case LILIQP1_1: return "Licence Libre du Québec – Permissive version 1.1";
            case LILIQR1_1: return "Licence Libre du Québec – Réciprocité version 1.1";
            case LILIQRPLUS1_1: return "Licence Libre du Québec – Réciprocité forte version 1.1";
            case LINUXOPENIB: return "Linux Kernel Variant of OpenIB.org license";
            case LPL1_0: return "Lucent Public License Version 1.0";
            case LPL1_02: return "Lucent Public License v1.02";
            case LPPL1_0: return "LaTeX Project Public License v1.0";
            case LPPL1_1: return "LaTeX Project Public License v1.1";
            case LPPL1_2: return "LaTeX Project Public License v1.2";
            case LPPL1_3A: return "LaTeX Project Public License v1.3a";
            case LPPL1_3C: return "LaTeX Project Public License v1.3c";
            case MAKEINDEX: return "MakeIndex License";
            case MIROS: return "MirOS License";
            case MIT0: return "MIT No Attribution";
            case MITADVERTISING: return "Enlightenment License (e16)";
            case MITCMU: return "CMU License";
            case MITENNA: return "enna License";
            case MITFEH: return "feh License";
            case MIT: return "MIT License";
            case MITNFA: return "MIT +no-false-attribs license";
            case MOTOSOTO: return "Motosoto License";
            case MPICH2: return "mpich2 License";
            case MPL1_0: return "Mozilla Public License 1.0";
            case MPL1_1: return "Mozilla Public License 1.1";
            case MPL2_0NOCOPYLEFTEXCEPTION: return "Mozilla Public License 2.0 (no copyleft exception)";
            case MPL2_0: return "Mozilla Public License 2.0";
            case MSPL: return "Microsoft Public License";
            case MSRL: return "Microsoft Reciprocal License";
            case MTLL: return "Matrix Template Library License";
            case MULTICS: return "Multics License";
            case MUP: return "Mup License";
            case NASA1_3: return "NASA Open Source Agreement 1.3";
            case NAUMEN: return "Naumen Public License";
            case NBPL1_0: return "Net Boolean Public License v1";
            case NCSA: return "University of Illinois/NCSA Open Source License";
            case NETSNMP: return "Net-SNMP License";
            case NETCDF: return "NetCDF license";
            case NEWSLETR: return "Newsletr License";
            case NGPL: return "Nethack General Public License";
            case NLOD1_0: return "Norwegian Licence for Open Government Data";
            case NLPL: return "No Limit Public License";
            case NOKIA: return "Nokia Open Source License";
            case NOSL: return "Netizen Open Source License";
            case NOWEB: return "Noweb License";
            case NPL1_0: return "Netscape Public License v1.0";
            case NPL1_1: return "Netscape Public License v1.1";
            case NPOSL3_0: return "Non-Profit Open Software License 3.0";
            case NRL: return "NRL License";
            case NTP: return "NTP License";
            case OCCTPL: return "Open CASCADE Technology Public License";
            case OCLC2_0: return "OCLC Research Public License 2.0";
            case ODBL1_0: return "ODC Open Database License v1.0";
            case OFL1_0: return "SIL Open Font License 1.0";
            case OFL1_1: return "SIL Open Font License 1.1";
            case OGTSL: return "Open Group Test Suite License";
            case OLDAP1_1: return "Open LDAP Public License v1.1";
            case OLDAP1_2: return "Open LDAP Public License v1.2";
            case OLDAP1_3: return "Open LDAP Public License v1.3";
            case OLDAP1_4: return "Open LDAP Public License v1.4";
            case OLDAP2_0_1: return "Open LDAP Public License v2.0.1";
            case OLDAP2_0: return "Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B)";
            case OLDAP2_1: return "Open LDAP Public License v2.1";
            case OLDAP2_2_1: return "Open LDAP Public License v2.2.1";
            case OLDAP2_2_2: return "Open LDAP Public License 2.2.2";
            case OLDAP2_2: return "Open LDAP Public License v2.2";
            case OLDAP2_3: return "Open LDAP Public License v2.3";
            case OLDAP2_4: return "Open LDAP Public License v2.4";
            case OLDAP2_5: return "Open LDAP Public License v2.5";
            case OLDAP2_6: return "Open LDAP Public License v2.6";
            case OLDAP2_7: return "Open LDAP Public License v2.7";
            case OLDAP2_8: return "Open LDAP Public License v2.8";
            case OML: return "Open Market License";
            case OPENSSL: return "OpenSSL License";
            case OPL1_0: return "Open Public License v1.0";
            case OSETPL2_1: return "OSET Public License version 2.1";
            case OSL1_0: return "Open Software License 1.0";
            case OSL1_1: return "Open Software License 1.1";
            case OSL2_0: return "Open Software License 2.0";
            case OSL2_1: return "Open Software License 2.1";
            case OSL3_0: return "Open Software License 3.0";
            case PDDL1_0: return "ODC Public Domain Dedication & License 1.0";
            case PHP3_0: return "PHP License v3.0";
            case PHP3_01: return "PHP License v3.01";
            case PLEXUS: return "Plexus Classworlds License";
            case POSTGRESQL: return "PostgreSQL License";
            case PSFRAG: return "psfrag License";
            case PSUTILS: return "psutils License";
            case PYTHON2_0: return "Python License 2.0";
            case QHULL: return "Qhull License";
            case QPL1_0: return "Q Public License 1.0";
            case RDISC: return "Rdisc License";
            case RHECOS1_1: return "Red Hat eCos Public License v1.1";
            case RPL1_1: return "Reciprocal Public License 1.1";
            case RPL1_5: return "Reciprocal Public License 1.5";
            case RPSL1_0: return "RealNetworks Public Source License v1.0";
            case RSAMD: return "RSA Message-Digest License";
            case RSCPL: return "Ricoh Source Code Public License";
            case RUBY: return "Ruby License";
            case SAXPD: return "Sax Public Domain Notice";
            case SAXPATH: return "Saxpath License";
            case SCEA: return "SCEA Shared Source License";
            case SENDMAIL: return "Sendmail License";
            case SGIB1_0: return "SGI Free Software License B v1.0";
            case SGIB1_1: return "SGI Free Software License B v1.1";
            case SGIB2_0: return "SGI Free Software License B v2.0";
            case SIMPL2_0: return "Simple Public License 2.0";
            case SISSL1_2: return "Sun Industry Standards Source License v1.2";
            case SISSL: return "Sun Industry Standards Source License v1.1";
            case SLEEPYCAT: return "Sleepycat License";
            case SMLNJ: return "Standard ML of New Jersey License";
            case SMPPL: return "Secure Messaging Protocol Public License";
            case SNIA: return "SNIA Public License 1.1";
            case SPENCER86: return "Spencer License 86";
            case SPENCER94: return "Spencer License 94";
            case SPENCER99: return "Spencer License 99";
            case SPL1_0: return "Sun Public License v1.0";
            case SUGARCRM1_1_3: return "SugarCRM Public License v1.1.3";
            case SWL: return "Scheme Widget Library (SWL) Software License Agreement";
            case TCL: return "TCL/TK License";
            case TCPWRAPPERS: return "TCP Wrappers License";
            case TMATE: return "TMate Open Source License";
            case TORQUE1_1: return "TORQUE v2.5+ Software License v1.1";
            case TOSL: return "Trusster Open Source License";
            case UNICODEDFS2015: return "Unicode License Agreement - Data Files and Software (2015)";
            case UNICODEDFS2016: return "Unicode License Agreement - Data Files and Software (2016)";
            case UNICODETOU: return "Unicode Terms of Use";
            case UNLICENSE: return "The Unlicense";
            case UPL1_0: return "Universal Permissive License v1.0";
            case VIM: return "Vim License";
            case VOSTROM: return "VOSTROM Public License for Open Source";
            case VSL1_0: return "Vovida Software License v1.0";
            case W3C19980720: return "W3C Software Notice and License (1998-07-20)";
            case W3C20150513: return "W3C Software Notice and Document License (2015-05-13)";
            case W3C: return "W3C Software Notice and License (2002-12-31)";
            case WATCOM1_0: return "Sybase Open Watcom Public License 1.0";
            case WSUIPA: return "Wsuipa License";
            case WTFPL: return "Do What The F*ck You Want To Public License";
            case X11: return "X11 License";
            case XEROX: return "Xerox License";
            case XFREE861_1: return "XFree86 License 1.1";
            case XINETD: return "xinetd License";
            case XNET: return "X.Net License";
            case XPP: return "XPP License";
            case XSKAT: return "XSkat License";
            case YPL1_0: return "Yahoo! Public License v1.0";
            case YPL1_1: return "Yahoo! Public License v1.1";
            case ZED: return "Zed License";
            case ZEND2_0: return "Zend License v2.0";
            case ZIMBRA1_3: return "Zimbra Public License v1.3";
            case ZIMBRA1_4: return "Zimbra Public License v1.4";
            case ZLIBACKNOWLEDGEMENT: return "zlib/libpng License with Acknowledgement";
            case ZLIB: return "zlib License";
            case ZPL1_1: return "Zope Public License 1.1";
            case ZPL2_0: return "Zope Public License 2.0";
            case ZPL2_1: return "Zope Public License 2.1";
            default: return "?";
          }
        }
    }

  public static class SPDXLicenseEnumFactory implements EnumFactory<SPDXLicense> {
    public SPDXLicense fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-open-source".equals(codeString))
          return SPDXLicense.NOTOPENSOURCE;
        if ("0BSD".equals(codeString))
          return SPDXLicense._0BSD;
        if ("AAL".equals(codeString))
          return SPDXLicense.AAL;
        if ("Abstyles".equals(codeString))
          return SPDXLicense.ABSTYLES;
        if ("Adobe-2006".equals(codeString))
          return SPDXLicense.ADOBE2006;
        if ("Adobe-Glyph".equals(codeString))
          return SPDXLicense.ADOBEGLYPH;
        if ("ADSL".equals(codeString))
          return SPDXLicense.ADSL;
        if ("AFL-1.1".equals(codeString))
          return SPDXLicense.AFL1_1;
        if ("AFL-1.2".equals(codeString))
          return SPDXLicense.AFL1_2;
        if ("AFL-2.0".equals(codeString))
          return SPDXLicense.AFL2_0;
        if ("AFL-2.1".equals(codeString))
          return SPDXLicense.AFL2_1;
        if ("AFL-3.0".equals(codeString))
          return SPDXLicense.AFL3_0;
        if ("Afmparse".equals(codeString))
          return SPDXLicense.AFMPARSE;
        if ("AGPL-1.0-only".equals(codeString))
          return SPDXLicense.AGPL1_0ONLY;
        if ("AGPL-1.0-or-later".equals(codeString))
          return SPDXLicense.AGPL1_0ORLATER;
        if ("AGPL-3.0-only".equals(codeString))
          return SPDXLicense.AGPL3_0ONLY;
        if ("AGPL-3.0-or-later".equals(codeString))
          return SPDXLicense.AGPL3_0ORLATER;
        if ("Aladdin".equals(codeString))
          return SPDXLicense.ALADDIN;
        if ("AMDPLPA".equals(codeString))
          return SPDXLicense.AMDPLPA;
        if ("AML".equals(codeString))
          return SPDXLicense.AML;
        if ("AMPAS".equals(codeString))
          return SPDXLicense.AMPAS;
        if ("ANTLR-PD".equals(codeString))
          return SPDXLicense.ANTLRPD;
        if ("Apache-1.0".equals(codeString))
          return SPDXLicense.APACHE1_0;
        if ("Apache-1.1".equals(codeString))
          return SPDXLicense.APACHE1_1;
        if ("Apache-2.0".equals(codeString))
          return SPDXLicense.APACHE2_0;
        if ("APAFML".equals(codeString))
          return SPDXLicense.APAFML;
        if ("APL-1.0".equals(codeString))
          return SPDXLicense.APL1_0;
        if ("APSL-1.0".equals(codeString))
          return SPDXLicense.APSL1_0;
        if ("APSL-1.1".equals(codeString))
          return SPDXLicense.APSL1_1;
        if ("APSL-1.2".equals(codeString))
          return SPDXLicense.APSL1_2;
        if ("APSL-2.0".equals(codeString))
          return SPDXLicense.APSL2_0;
        if ("Artistic-1.0-cl8".equals(codeString))
          return SPDXLicense.ARTISTIC1_0CL8;
        if ("Artistic-1.0-Perl".equals(codeString))
          return SPDXLicense.ARTISTIC1_0PERL;
        if ("Artistic-1.0".equals(codeString))
          return SPDXLicense.ARTISTIC1_0;
        if ("Artistic-2.0".equals(codeString))
          return SPDXLicense.ARTISTIC2_0;
        if ("Bahyph".equals(codeString))
          return SPDXLicense.BAHYPH;
        if ("Barr".equals(codeString))
          return SPDXLicense.BARR;
        if ("Beerware".equals(codeString))
          return SPDXLicense.BEERWARE;
        if ("BitTorrent-1.0".equals(codeString))
          return SPDXLicense.BITTORRENT1_0;
        if ("BitTorrent-1.1".equals(codeString))
          return SPDXLicense.BITTORRENT1_1;
        if ("Borceux".equals(codeString))
          return SPDXLicense.BORCEUX;
        if ("BSD-1-Clause".equals(codeString))
          return SPDXLicense.BSD1CLAUSE;
        if ("BSD-2-Clause-FreeBSD".equals(codeString))
          return SPDXLicense.BSD2CLAUSEFREEBSD;
        if ("BSD-2-Clause-NetBSD".equals(codeString))
          return SPDXLicense.BSD2CLAUSENETBSD;
        if ("BSD-2-Clause-Patent".equals(codeString))
          return SPDXLicense.BSD2CLAUSEPATENT;
        if ("BSD-2-Clause".equals(codeString))
          return SPDXLicense.BSD2CLAUSE;
        if ("BSD-3-Clause-Attribution".equals(codeString))
          return SPDXLicense.BSD3CLAUSEATTRIBUTION;
        if ("BSD-3-Clause-Clear".equals(codeString))
          return SPDXLicense.BSD3CLAUSECLEAR;
        if ("BSD-3-Clause-LBNL".equals(codeString))
          return SPDXLicense.BSD3CLAUSELBNL;
        if ("BSD-3-Clause-No-Nuclear-License-2014".equals(codeString))
          return SPDXLicense.BSD3CLAUSENONUCLEARLICENSE2014;
        if ("BSD-3-Clause-No-Nuclear-License".equals(codeString))
          return SPDXLicense.BSD3CLAUSENONUCLEARLICENSE;
        if ("BSD-3-Clause-No-Nuclear-Warranty".equals(codeString))
          return SPDXLicense.BSD3CLAUSENONUCLEARWARRANTY;
        if ("BSD-3-Clause".equals(codeString))
          return SPDXLicense.BSD3CLAUSE;
        if ("BSD-4-Clause-UC".equals(codeString))
          return SPDXLicense.BSD4CLAUSEUC;
        if ("BSD-4-Clause".equals(codeString))
          return SPDXLicense.BSD4CLAUSE;
        if ("BSD-Protection".equals(codeString))
          return SPDXLicense.BSDPROTECTION;
        if ("BSD-Source-Code".equals(codeString))
          return SPDXLicense.BSDSOURCECODE;
        if ("BSL-1.0".equals(codeString))
          return SPDXLicense.BSL1_0;
        if ("bzip2-1.0.5".equals(codeString))
          return SPDXLicense.BZIP21_0_5;
        if ("bzip2-1.0.6".equals(codeString))
          return SPDXLicense.BZIP21_0_6;
        if ("Caldera".equals(codeString))
          return SPDXLicense.CALDERA;
        if ("CATOSL-1.1".equals(codeString))
          return SPDXLicense.CATOSL1_1;
        if ("CC-BY-1.0".equals(codeString))
          return SPDXLicense.CCBY1_0;
        if ("CC-BY-2.0".equals(codeString))
          return SPDXLicense.CCBY2_0;
        if ("CC-BY-2.5".equals(codeString))
          return SPDXLicense.CCBY2_5;
        if ("CC-BY-3.0".equals(codeString))
          return SPDXLicense.CCBY3_0;
        if ("CC-BY-4.0".equals(codeString))
          return SPDXLicense.CCBY4_0;
        if ("CC-BY-NC-1.0".equals(codeString))
          return SPDXLicense.CCBYNC1_0;
        if ("CC-BY-NC-2.0".equals(codeString))
          return SPDXLicense.CCBYNC2_0;
        if ("CC-BY-NC-2.5".equals(codeString))
          return SPDXLicense.CCBYNC2_5;
        if ("CC-BY-NC-3.0".equals(codeString))
          return SPDXLicense.CCBYNC3_0;
        if ("CC-BY-NC-4.0".equals(codeString))
          return SPDXLicense.CCBYNC4_0;
        if ("CC-BY-NC-ND-1.0".equals(codeString))
          return SPDXLicense.CCBYNCND1_0;
        if ("CC-BY-NC-ND-2.0".equals(codeString))
          return SPDXLicense.CCBYNCND2_0;
        if ("CC-BY-NC-ND-2.5".equals(codeString))
          return SPDXLicense.CCBYNCND2_5;
        if ("CC-BY-NC-ND-3.0".equals(codeString))
          return SPDXLicense.CCBYNCND3_0;
        if ("CC-BY-NC-ND-4.0".equals(codeString))
          return SPDXLicense.CCBYNCND4_0;
        if ("CC-BY-NC-SA-1.0".equals(codeString))
          return SPDXLicense.CCBYNCSA1_0;
        if ("CC-BY-NC-SA-2.0".equals(codeString))
          return SPDXLicense.CCBYNCSA2_0;
        if ("CC-BY-NC-SA-2.5".equals(codeString))
          return SPDXLicense.CCBYNCSA2_5;
        if ("CC-BY-NC-SA-3.0".equals(codeString))
          return SPDXLicense.CCBYNCSA3_0;
        if ("CC-BY-NC-SA-4.0".equals(codeString))
          return SPDXLicense.CCBYNCSA4_0;
        if ("CC-BY-ND-1.0".equals(codeString))
          return SPDXLicense.CCBYND1_0;
        if ("CC-BY-ND-2.0".equals(codeString))
          return SPDXLicense.CCBYND2_0;
        if ("CC-BY-ND-2.5".equals(codeString))
          return SPDXLicense.CCBYND2_5;
        if ("CC-BY-ND-3.0".equals(codeString))
          return SPDXLicense.CCBYND3_0;
        if ("CC-BY-ND-4.0".equals(codeString))
          return SPDXLicense.CCBYND4_0;
        if ("CC-BY-SA-1.0".equals(codeString))
          return SPDXLicense.CCBYSA1_0;
        if ("CC-BY-SA-2.0".equals(codeString))
          return SPDXLicense.CCBYSA2_0;
        if ("CC-BY-SA-2.5".equals(codeString))
          return SPDXLicense.CCBYSA2_5;
        if ("CC-BY-SA-3.0".equals(codeString))
          return SPDXLicense.CCBYSA3_0;
        if ("CC-BY-SA-4.0".equals(codeString))
          return SPDXLicense.CCBYSA4_0;
        if ("CC0-1.0".equals(codeString))
          return SPDXLicense.CC01_0;
        if ("CDDL-1.0".equals(codeString))
          return SPDXLicense.CDDL1_0;
        if ("CDDL-1.1".equals(codeString))
          return SPDXLicense.CDDL1_1;
        if ("CDLA-Permissive-1.0".equals(codeString))
          return SPDXLicense.CDLAPERMISSIVE1_0;
        if ("CDLA-Sharing-1.0".equals(codeString))
          return SPDXLicense.CDLASHARING1_0;
        if ("CECILL-1.0".equals(codeString))
          return SPDXLicense.CECILL1_0;
        if ("CECILL-1.1".equals(codeString))
          return SPDXLicense.CECILL1_1;
        if ("CECILL-2.0".equals(codeString))
          return SPDXLicense.CECILL2_0;
        if ("CECILL-2.1".equals(codeString))
          return SPDXLicense.CECILL2_1;
        if ("CECILL-B".equals(codeString))
          return SPDXLicense.CECILLB;
        if ("CECILL-C".equals(codeString))
          return SPDXLicense.CECILLC;
        if ("ClArtistic".equals(codeString))
          return SPDXLicense.CLARTISTIC;
        if ("CNRI-Jython".equals(codeString))
          return SPDXLicense.CNRIJYTHON;
        if ("CNRI-Python-GPL-Compatible".equals(codeString))
          return SPDXLicense.CNRIPYTHONGPLCOMPATIBLE;
        if ("CNRI-Python".equals(codeString))
          return SPDXLicense.CNRIPYTHON;
        if ("Condor-1.1".equals(codeString))
          return SPDXLicense.CONDOR1_1;
        if ("CPAL-1.0".equals(codeString))
          return SPDXLicense.CPAL1_0;
        if ("CPL-1.0".equals(codeString))
          return SPDXLicense.CPL1_0;
        if ("CPOL-1.02".equals(codeString))
          return SPDXLicense.CPOL1_02;
        if ("Crossword".equals(codeString))
          return SPDXLicense.CROSSWORD;
        if ("CrystalStacker".equals(codeString))
          return SPDXLicense.CRYSTALSTACKER;
        if ("CUA-OPL-1.0".equals(codeString))
          return SPDXLicense.CUAOPL1_0;
        if ("Cube".equals(codeString))
          return SPDXLicense.CUBE;
        if ("curl".equals(codeString))
          return SPDXLicense.CURL;
        if ("D-FSL-1.0".equals(codeString))
          return SPDXLicense.DFSL1_0;
        if ("diffmark".equals(codeString))
          return SPDXLicense.DIFFMARK;
        if ("DOC".equals(codeString))
          return SPDXLicense.DOC;
        if ("Dotseqn".equals(codeString))
          return SPDXLicense.DOTSEQN;
        if ("DSDP".equals(codeString))
          return SPDXLicense.DSDP;
        if ("dvipdfm".equals(codeString))
          return SPDXLicense.DVIPDFM;
        if ("ECL-1.0".equals(codeString))
          return SPDXLicense.ECL1_0;
        if ("ECL-2.0".equals(codeString))
          return SPDXLicense.ECL2_0;
        if ("EFL-1.0".equals(codeString))
          return SPDXLicense.EFL1_0;
        if ("EFL-2.0".equals(codeString))
          return SPDXLicense.EFL2_0;
        if ("eGenix".equals(codeString))
          return SPDXLicense.EGENIX;
        if ("Entessa".equals(codeString))
          return SPDXLicense.ENTESSA;
        if ("EPL-1.0".equals(codeString))
          return SPDXLicense.EPL1_0;
        if ("EPL-2.0".equals(codeString))
          return SPDXLicense.EPL2_0;
        if ("ErlPL-1.1".equals(codeString))
          return SPDXLicense.ERLPL1_1;
        if ("EUDatagrid".equals(codeString))
          return SPDXLicense.EUDATAGRID;
        if ("EUPL-1.0".equals(codeString))
          return SPDXLicense.EUPL1_0;
        if ("EUPL-1.1".equals(codeString))
          return SPDXLicense.EUPL1_1;
        if ("EUPL-1.2".equals(codeString))
          return SPDXLicense.EUPL1_2;
        if ("Eurosym".equals(codeString))
          return SPDXLicense.EUROSYM;
        if ("Fair".equals(codeString))
          return SPDXLicense.FAIR;
        if ("Frameworx-1.0".equals(codeString))
          return SPDXLicense.FRAMEWORX1_0;
        if ("FreeImage".equals(codeString))
          return SPDXLicense.FREEIMAGE;
        if ("FSFAP".equals(codeString))
          return SPDXLicense.FSFAP;
        if ("FSFUL".equals(codeString))
          return SPDXLicense.FSFUL;
        if ("FSFULLR".equals(codeString))
          return SPDXLicense.FSFULLR;
        if ("FTL".equals(codeString))
          return SPDXLicense.FTL;
        if ("GFDL-1.1-only".equals(codeString))
          return SPDXLicense.GFDL1_1ONLY;
        if ("GFDL-1.1-or-later".equals(codeString))
          return SPDXLicense.GFDL1_1ORLATER;
        if ("GFDL-1.2-only".equals(codeString))
          return SPDXLicense.GFDL1_2ONLY;
        if ("GFDL-1.2-or-later".equals(codeString))
          return SPDXLicense.GFDL1_2ORLATER;
        if ("GFDL-1.3-only".equals(codeString))
          return SPDXLicense.GFDL1_3ONLY;
        if ("GFDL-1.3-or-later".equals(codeString))
          return SPDXLicense.GFDL1_3ORLATER;
        if ("Giftware".equals(codeString))
          return SPDXLicense.GIFTWARE;
        if ("GL2PS".equals(codeString))
          return SPDXLicense.GL2PS;
        if ("Glide".equals(codeString))
          return SPDXLicense.GLIDE;
        if ("Glulxe".equals(codeString))
          return SPDXLicense.GLULXE;
        if ("gnuplot".equals(codeString))
          return SPDXLicense.GNUPLOT;
        if ("GPL-1.0-only".equals(codeString))
          return SPDXLicense.GPL1_0ONLY;
        if ("GPL-1.0-or-later".equals(codeString))
          return SPDXLicense.GPL1_0ORLATER;
        if ("GPL-2.0-only".equals(codeString))
          return SPDXLicense.GPL2_0ONLY;
        if ("GPL-2.0-or-later".equals(codeString))
          return SPDXLicense.GPL2_0ORLATER;
        if ("GPL-3.0-only".equals(codeString))
          return SPDXLicense.GPL3_0ONLY;
        if ("GPL-3.0-or-later".equals(codeString))
          return SPDXLicense.GPL3_0ORLATER;
        if ("gSOAP-1.3b".equals(codeString))
          return SPDXLicense.GSOAP1_3B;
        if ("HaskellReport".equals(codeString))
          return SPDXLicense.HASKELLREPORT;
        if ("HPND".equals(codeString))
          return SPDXLicense.HPND;
        if ("IBM-pibs".equals(codeString))
          return SPDXLicense.IBMPIBS;
        if ("ICU".equals(codeString))
          return SPDXLicense.ICU;
        if ("IJG".equals(codeString))
          return SPDXLicense.IJG;
        if ("ImageMagick".equals(codeString))
          return SPDXLicense.IMAGEMAGICK;
        if ("iMatix".equals(codeString))
          return SPDXLicense.IMATIX;
        if ("Imlib2".equals(codeString))
          return SPDXLicense.IMLIB2;
        if ("Info-ZIP".equals(codeString))
          return SPDXLicense.INFOZIP;
        if ("Intel-ACPI".equals(codeString))
          return SPDXLicense.INTELACPI;
        if ("Intel".equals(codeString))
          return SPDXLicense.INTEL;
        if ("Interbase-1.0".equals(codeString))
          return SPDXLicense.INTERBASE1_0;
        if ("IPA".equals(codeString))
          return SPDXLicense.IPA;
        if ("IPL-1.0".equals(codeString))
          return SPDXLicense.IPL1_0;
        if ("ISC".equals(codeString))
          return SPDXLicense.ISC;
        if ("JasPer-2.0".equals(codeString))
          return SPDXLicense.JASPER2_0;
        if ("JSON".equals(codeString))
          return SPDXLicense.JSON;
        if ("LAL-1.2".equals(codeString))
          return SPDXLicense.LAL1_2;
        if ("LAL-1.3".equals(codeString))
          return SPDXLicense.LAL1_3;
        if ("Latex2e".equals(codeString))
          return SPDXLicense.LATEX2E;
        if ("Leptonica".equals(codeString))
          return SPDXLicense.LEPTONICA;
        if ("LGPL-2.0-only".equals(codeString))
          return SPDXLicense.LGPL2_0ONLY;
        if ("LGPL-2.0-or-later".equals(codeString))
          return SPDXLicense.LGPL2_0ORLATER;
        if ("LGPL-2.1-only".equals(codeString))
          return SPDXLicense.LGPL2_1ONLY;
        if ("LGPL-2.1-or-later".equals(codeString))
          return SPDXLicense.LGPL2_1ORLATER;
        if ("LGPL-3.0-only".equals(codeString))
          return SPDXLicense.LGPL3_0ONLY;
        if ("LGPL-3.0-or-later".equals(codeString))
          return SPDXLicense.LGPL3_0ORLATER;
        if ("LGPLLR".equals(codeString))
          return SPDXLicense.LGPLLR;
        if ("Libpng".equals(codeString))
          return SPDXLicense.LIBPNG;
        if ("libtiff".equals(codeString))
          return SPDXLicense.LIBTIFF;
        if ("LiLiQ-P-1.1".equals(codeString))
          return SPDXLicense.LILIQP1_1;
        if ("LiLiQ-R-1.1".equals(codeString))
          return SPDXLicense.LILIQR1_1;
        if ("LiLiQ-Rplus-1.1".equals(codeString))
          return SPDXLicense.LILIQRPLUS1_1;
        if ("Linux-OpenIB".equals(codeString))
          return SPDXLicense.LINUXOPENIB;
        if ("LPL-1.0".equals(codeString))
          return SPDXLicense.LPL1_0;
        if ("LPL-1.02".equals(codeString))
          return SPDXLicense.LPL1_02;
        if ("LPPL-1.0".equals(codeString))
          return SPDXLicense.LPPL1_0;
        if ("LPPL-1.1".equals(codeString))
          return SPDXLicense.LPPL1_1;
        if ("LPPL-1.2".equals(codeString))
          return SPDXLicense.LPPL1_2;
        if ("LPPL-1.3a".equals(codeString))
          return SPDXLicense.LPPL1_3A;
        if ("LPPL-1.3c".equals(codeString))
          return SPDXLicense.LPPL1_3C;
        if ("MakeIndex".equals(codeString))
          return SPDXLicense.MAKEINDEX;
        if ("MirOS".equals(codeString))
          return SPDXLicense.MIROS;
        if ("MIT-0".equals(codeString))
          return SPDXLicense.MIT0;
        if ("MIT-advertising".equals(codeString))
          return SPDXLicense.MITADVERTISING;
        if ("MIT-CMU".equals(codeString))
          return SPDXLicense.MITCMU;
        if ("MIT-enna".equals(codeString))
          return SPDXLicense.MITENNA;
        if ("MIT-feh".equals(codeString))
          return SPDXLicense.MITFEH;
        if ("MIT".equals(codeString))
          return SPDXLicense.MIT;
        if ("MITNFA".equals(codeString))
          return SPDXLicense.MITNFA;
        if ("Motosoto".equals(codeString))
          return SPDXLicense.MOTOSOTO;
        if ("mpich2".equals(codeString))
          return SPDXLicense.MPICH2;
        if ("MPL-1.0".equals(codeString))
          return SPDXLicense.MPL1_0;
        if ("MPL-1.1".equals(codeString))
          return SPDXLicense.MPL1_1;
        if ("MPL-2.0-no-copyleft-exception".equals(codeString))
          return SPDXLicense.MPL2_0NOCOPYLEFTEXCEPTION;
        if ("MPL-2.0".equals(codeString))
          return SPDXLicense.MPL2_0;
        if ("MS-PL".equals(codeString))
          return SPDXLicense.MSPL;
        if ("MS-RL".equals(codeString))
          return SPDXLicense.MSRL;
        if ("MTLL".equals(codeString))
          return SPDXLicense.MTLL;
        if ("Multics".equals(codeString))
          return SPDXLicense.MULTICS;
        if ("Mup".equals(codeString))
          return SPDXLicense.MUP;
        if ("NASA-1.3".equals(codeString))
          return SPDXLicense.NASA1_3;
        if ("Naumen".equals(codeString))
          return SPDXLicense.NAUMEN;
        if ("NBPL-1.0".equals(codeString))
          return SPDXLicense.NBPL1_0;
        if ("NCSA".equals(codeString))
          return SPDXLicense.NCSA;
        if ("Net-SNMP".equals(codeString))
          return SPDXLicense.NETSNMP;
        if ("NetCDF".equals(codeString))
          return SPDXLicense.NETCDF;
        if ("Newsletr".equals(codeString))
          return SPDXLicense.NEWSLETR;
        if ("NGPL".equals(codeString))
          return SPDXLicense.NGPL;
        if ("NLOD-1.0".equals(codeString))
          return SPDXLicense.NLOD1_0;
        if ("NLPL".equals(codeString))
          return SPDXLicense.NLPL;
        if ("Nokia".equals(codeString))
          return SPDXLicense.NOKIA;
        if ("NOSL".equals(codeString))
          return SPDXLicense.NOSL;
        if ("Noweb".equals(codeString))
          return SPDXLicense.NOWEB;
        if ("NPL-1.0".equals(codeString))
          return SPDXLicense.NPL1_0;
        if ("NPL-1.1".equals(codeString))
          return SPDXLicense.NPL1_1;
        if ("NPOSL-3.0".equals(codeString))
          return SPDXLicense.NPOSL3_0;
        if ("NRL".equals(codeString))
          return SPDXLicense.NRL;
        if ("NTP".equals(codeString))
          return SPDXLicense.NTP;
        if ("OCCT-PL".equals(codeString))
          return SPDXLicense.OCCTPL;
        if ("OCLC-2.0".equals(codeString))
          return SPDXLicense.OCLC2_0;
        if ("ODbL-1.0".equals(codeString))
          return SPDXLicense.ODBL1_0;
        if ("OFL-1.0".equals(codeString))
          return SPDXLicense.OFL1_0;
        if ("OFL-1.1".equals(codeString))
          return SPDXLicense.OFL1_1;
        if ("OGTSL".equals(codeString))
          return SPDXLicense.OGTSL;
        if ("OLDAP-1.1".equals(codeString))
          return SPDXLicense.OLDAP1_1;
        if ("OLDAP-1.2".equals(codeString))
          return SPDXLicense.OLDAP1_2;
        if ("OLDAP-1.3".equals(codeString))
          return SPDXLicense.OLDAP1_3;
        if ("OLDAP-1.4".equals(codeString))
          return SPDXLicense.OLDAP1_4;
        if ("OLDAP-2.0.1".equals(codeString))
          return SPDXLicense.OLDAP2_0_1;
        if ("OLDAP-2.0".equals(codeString))
          return SPDXLicense.OLDAP2_0;
        if ("OLDAP-2.1".equals(codeString))
          return SPDXLicense.OLDAP2_1;
        if ("OLDAP-2.2.1".equals(codeString))
          return SPDXLicense.OLDAP2_2_1;
        if ("OLDAP-2.2.2".equals(codeString))
          return SPDXLicense.OLDAP2_2_2;
        if ("OLDAP-2.2".equals(codeString))
          return SPDXLicense.OLDAP2_2;
        if ("OLDAP-2.3".equals(codeString))
          return SPDXLicense.OLDAP2_3;
        if ("OLDAP-2.4".equals(codeString))
          return SPDXLicense.OLDAP2_4;
        if ("OLDAP-2.5".equals(codeString))
          return SPDXLicense.OLDAP2_5;
        if ("OLDAP-2.6".equals(codeString))
          return SPDXLicense.OLDAP2_6;
        if ("OLDAP-2.7".equals(codeString))
          return SPDXLicense.OLDAP2_7;
        if ("OLDAP-2.8".equals(codeString))
          return SPDXLicense.OLDAP2_8;
        if ("OML".equals(codeString))
          return SPDXLicense.OML;
        if ("OpenSSL".equals(codeString))
          return SPDXLicense.OPENSSL;
        if ("OPL-1.0".equals(codeString))
          return SPDXLicense.OPL1_0;
        if ("OSET-PL-2.1".equals(codeString))
          return SPDXLicense.OSETPL2_1;
        if ("OSL-1.0".equals(codeString))
          return SPDXLicense.OSL1_0;
        if ("OSL-1.1".equals(codeString))
          return SPDXLicense.OSL1_1;
        if ("OSL-2.0".equals(codeString))
          return SPDXLicense.OSL2_0;
        if ("OSL-2.1".equals(codeString))
          return SPDXLicense.OSL2_1;
        if ("OSL-3.0".equals(codeString))
          return SPDXLicense.OSL3_0;
        if ("PDDL-1.0".equals(codeString))
          return SPDXLicense.PDDL1_0;
        if ("PHP-3.0".equals(codeString))
          return SPDXLicense.PHP3_0;
        if ("PHP-3.01".equals(codeString))
          return SPDXLicense.PHP3_01;
        if ("Plexus".equals(codeString))
          return SPDXLicense.PLEXUS;
        if ("PostgreSQL".equals(codeString))
          return SPDXLicense.POSTGRESQL;
        if ("psfrag".equals(codeString))
          return SPDXLicense.PSFRAG;
        if ("psutils".equals(codeString))
          return SPDXLicense.PSUTILS;
        if ("Python-2.0".equals(codeString))
          return SPDXLicense.PYTHON2_0;
        if ("Qhull".equals(codeString))
          return SPDXLicense.QHULL;
        if ("QPL-1.0".equals(codeString))
          return SPDXLicense.QPL1_0;
        if ("Rdisc".equals(codeString))
          return SPDXLicense.RDISC;
        if ("RHeCos-1.1".equals(codeString))
          return SPDXLicense.RHECOS1_1;
        if ("RPL-1.1".equals(codeString))
          return SPDXLicense.RPL1_1;
        if ("RPL-1.5".equals(codeString))
          return SPDXLicense.RPL1_5;
        if ("RPSL-1.0".equals(codeString))
          return SPDXLicense.RPSL1_0;
        if ("RSA-MD".equals(codeString))
          return SPDXLicense.RSAMD;
        if ("RSCPL".equals(codeString))
          return SPDXLicense.RSCPL;
        if ("Ruby".equals(codeString))
          return SPDXLicense.RUBY;
        if ("SAX-PD".equals(codeString))
          return SPDXLicense.SAXPD;
        if ("Saxpath".equals(codeString))
          return SPDXLicense.SAXPATH;
        if ("SCEA".equals(codeString))
          return SPDXLicense.SCEA;
        if ("Sendmail".equals(codeString))
          return SPDXLicense.SENDMAIL;
        if ("SGI-B-1.0".equals(codeString))
          return SPDXLicense.SGIB1_0;
        if ("SGI-B-1.1".equals(codeString))
          return SPDXLicense.SGIB1_1;
        if ("SGI-B-2.0".equals(codeString))
          return SPDXLicense.SGIB2_0;
        if ("SimPL-2.0".equals(codeString))
          return SPDXLicense.SIMPL2_0;
        if ("SISSL-1.2".equals(codeString))
          return SPDXLicense.SISSL1_2;
        if ("SISSL".equals(codeString))
          return SPDXLicense.SISSL;
        if ("Sleepycat".equals(codeString))
          return SPDXLicense.SLEEPYCAT;
        if ("SMLNJ".equals(codeString))
          return SPDXLicense.SMLNJ;
        if ("SMPPL".equals(codeString))
          return SPDXLicense.SMPPL;
        if ("SNIA".equals(codeString))
          return SPDXLicense.SNIA;
        if ("Spencer-86".equals(codeString))
          return SPDXLicense.SPENCER86;
        if ("Spencer-94".equals(codeString))
          return SPDXLicense.SPENCER94;
        if ("Spencer-99".equals(codeString))
          return SPDXLicense.SPENCER99;
        if ("SPL-1.0".equals(codeString))
          return SPDXLicense.SPL1_0;
        if ("SugarCRM-1.1.3".equals(codeString))
          return SPDXLicense.SUGARCRM1_1_3;
        if ("SWL".equals(codeString))
          return SPDXLicense.SWL;
        if ("TCL".equals(codeString))
          return SPDXLicense.TCL;
        if ("TCP-wrappers".equals(codeString))
          return SPDXLicense.TCPWRAPPERS;
        if ("TMate".equals(codeString))
          return SPDXLicense.TMATE;
        if ("TORQUE-1.1".equals(codeString))
          return SPDXLicense.TORQUE1_1;
        if ("TOSL".equals(codeString))
          return SPDXLicense.TOSL;
        if ("Unicode-DFS-2015".equals(codeString))
          return SPDXLicense.UNICODEDFS2015;
        if ("Unicode-DFS-2016".equals(codeString))
          return SPDXLicense.UNICODEDFS2016;
        if ("Unicode-TOU".equals(codeString))
          return SPDXLicense.UNICODETOU;
        if ("Unlicense".equals(codeString))
          return SPDXLicense.UNLICENSE;
        if ("UPL-1.0".equals(codeString))
          return SPDXLicense.UPL1_0;
        if ("Vim".equals(codeString))
          return SPDXLicense.VIM;
        if ("VOSTROM".equals(codeString))
          return SPDXLicense.VOSTROM;
        if ("VSL-1.0".equals(codeString))
          return SPDXLicense.VSL1_0;
        if ("W3C-19980720".equals(codeString))
          return SPDXLicense.W3C19980720;
        if ("W3C-20150513".equals(codeString))
          return SPDXLicense.W3C20150513;
        if ("W3C".equals(codeString))
          return SPDXLicense.W3C;
        if ("Watcom-1.0".equals(codeString))
          return SPDXLicense.WATCOM1_0;
        if ("Wsuipa".equals(codeString))
          return SPDXLicense.WSUIPA;
        if ("WTFPL".equals(codeString))
          return SPDXLicense.WTFPL;
        if ("X11".equals(codeString))
          return SPDXLicense.X11;
        if ("Xerox".equals(codeString))
          return SPDXLicense.XEROX;
        if ("XFree86-1.1".equals(codeString))
          return SPDXLicense.XFREE861_1;
        if ("xinetd".equals(codeString))
          return SPDXLicense.XINETD;
        if ("Xnet".equals(codeString))
          return SPDXLicense.XNET;
        if ("xpp".equals(codeString))
          return SPDXLicense.XPP;
        if ("XSkat".equals(codeString))
          return SPDXLicense.XSKAT;
        if ("YPL-1.0".equals(codeString))
          return SPDXLicense.YPL1_0;
        if ("YPL-1.1".equals(codeString))
          return SPDXLicense.YPL1_1;
        if ("Zed".equals(codeString))
          return SPDXLicense.ZED;
        if ("Zend-2.0".equals(codeString))
          return SPDXLicense.ZEND2_0;
        if ("Zimbra-1.3".equals(codeString))
          return SPDXLicense.ZIMBRA1_3;
        if ("Zimbra-1.4".equals(codeString))
          return SPDXLicense.ZIMBRA1_4;
        if ("zlib-acknowledgement".equals(codeString))
          return SPDXLicense.ZLIBACKNOWLEDGEMENT;
        if ("Zlib".equals(codeString))
          return SPDXLicense.ZLIB;
        if ("ZPL-1.1".equals(codeString))
          return SPDXLicense.ZPL1_1;
        if ("ZPL-2.0".equals(codeString))
          return SPDXLicense.ZPL2_0;
        if ("ZPL-2.1".equals(codeString))
          return SPDXLicense.ZPL2_1;
        throw new IllegalArgumentException("Unknown SPDXLicense code '"+codeString+"'");
        }
        public Enumeration<SPDXLicense> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SPDXLicense>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("not-open-source".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NOTOPENSOURCE);
        if ("0BSD".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense._0BSD);
        if ("AAL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AAL);
        if ("Abstyles".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ABSTYLES);
        if ("Adobe-2006".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ADOBE2006);
        if ("Adobe-Glyph".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ADOBEGLYPH);
        if ("ADSL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ADSL);
        if ("AFL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AFL1_1);
        if ("AFL-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AFL1_2);
        if ("AFL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AFL2_0);
        if ("AFL-2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AFL2_1);
        if ("AFL-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AFL3_0);
        if ("Afmparse".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AFMPARSE);
        if ("AGPL-1.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AGPL1_0ONLY);
        if ("AGPL-1.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AGPL1_0ORLATER);
        if ("AGPL-3.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AGPL3_0ONLY);
        if ("AGPL-3.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AGPL3_0ORLATER);
        if ("Aladdin".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ALADDIN);
        if ("AMDPLPA".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AMDPLPA);
        if ("AML".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AML);
        if ("AMPAS".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.AMPAS);
        if ("ANTLR-PD".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ANTLRPD);
        if ("Apache-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APACHE1_0);
        if ("Apache-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APACHE1_1);
        if ("Apache-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APACHE2_0);
        if ("APAFML".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APAFML);
        if ("APL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APL1_0);
        if ("APSL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APSL1_0);
        if ("APSL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APSL1_1);
        if ("APSL-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APSL1_2);
        if ("APSL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.APSL2_0);
        if ("Artistic-1.0-cl8".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ARTISTIC1_0CL8);
        if ("Artistic-1.0-Perl".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ARTISTIC1_0PERL);
        if ("Artistic-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ARTISTIC1_0);
        if ("Artistic-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ARTISTIC2_0);
        if ("Bahyph".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BAHYPH);
        if ("Barr".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BARR);
        if ("Beerware".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BEERWARE);
        if ("BitTorrent-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BITTORRENT1_0);
        if ("BitTorrent-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BITTORRENT1_1);
        if ("Borceux".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BORCEUX);
        if ("BSD-1-Clause".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD1CLAUSE);
        if ("BSD-2-Clause-FreeBSD".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD2CLAUSEFREEBSD);
        if ("BSD-2-Clause-NetBSD".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD2CLAUSENETBSD);
        if ("BSD-2-Clause-Patent".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD2CLAUSEPATENT);
        if ("BSD-2-Clause".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD2CLAUSE);
        if ("BSD-3-Clause-Attribution".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSEATTRIBUTION);
        if ("BSD-3-Clause-Clear".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSECLEAR);
        if ("BSD-3-Clause-LBNL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSELBNL);
        if ("BSD-3-Clause-No-Nuclear-License-2014".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSENONUCLEARLICENSE2014);
        if ("BSD-3-Clause-No-Nuclear-License".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSENONUCLEARLICENSE);
        if ("BSD-3-Clause-No-Nuclear-Warranty".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSENONUCLEARWARRANTY);
        if ("BSD-3-Clause".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD3CLAUSE);
        if ("BSD-4-Clause-UC".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD4CLAUSEUC);
        if ("BSD-4-Clause".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSD4CLAUSE);
        if ("BSD-Protection".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSDPROTECTION);
        if ("BSD-Source-Code".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSDSOURCECODE);
        if ("BSL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BSL1_0);
        if ("bzip2-1.0.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BZIP21_0_5);
        if ("bzip2-1.0.6".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.BZIP21_0_6);
        if ("Caldera".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CALDERA);
        if ("CATOSL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CATOSL1_1);
        if ("CC-BY-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBY1_0);
        if ("CC-BY-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBY2_0);
        if ("CC-BY-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBY2_5);
        if ("CC-BY-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBY3_0);
        if ("CC-BY-4.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBY4_0);
        if ("CC-BY-NC-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNC1_0);
        if ("CC-BY-NC-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNC2_0);
        if ("CC-BY-NC-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNC2_5);
        if ("CC-BY-NC-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNC3_0);
        if ("CC-BY-NC-4.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNC4_0);
        if ("CC-BY-NC-ND-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCND1_0);
        if ("CC-BY-NC-ND-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCND2_0);
        if ("CC-BY-NC-ND-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCND2_5);
        if ("CC-BY-NC-ND-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCND3_0);
        if ("CC-BY-NC-ND-4.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCND4_0);
        if ("CC-BY-NC-SA-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCSA1_0);
        if ("CC-BY-NC-SA-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCSA2_0);
        if ("CC-BY-NC-SA-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCSA2_5);
        if ("CC-BY-NC-SA-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCSA3_0);
        if ("CC-BY-NC-SA-4.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYNCSA4_0);
        if ("CC-BY-ND-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYND1_0);
        if ("CC-BY-ND-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYND2_0);
        if ("CC-BY-ND-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYND2_5);
        if ("CC-BY-ND-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYND3_0);
        if ("CC-BY-ND-4.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYND4_0);
        if ("CC-BY-SA-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYSA1_0);
        if ("CC-BY-SA-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYSA2_0);
        if ("CC-BY-SA-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYSA2_5);
        if ("CC-BY-SA-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYSA3_0);
        if ("CC-BY-SA-4.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CCBYSA4_0);
        if ("CC0-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CC01_0);
        if ("CDDL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CDDL1_0);
        if ("CDDL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CDDL1_1);
        if ("CDLA-Permissive-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CDLAPERMISSIVE1_0);
        if ("CDLA-Sharing-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CDLASHARING1_0);
        if ("CECILL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CECILL1_0);
        if ("CECILL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CECILL1_1);
        if ("CECILL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CECILL2_0);
        if ("CECILL-2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CECILL2_1);
        if ("CECILL-B".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CECILLB);
        if ("CECILL-C".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CECILLC);
        if ("ClArtistic".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CLARTISTIC);
        if ("CNRI-Jython".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CNRIJYTHON);
        if ("CNRI-Python-GPL-Compatible".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CNRIPYTHONGPLCOMPATIBLE);
        if ("CNRI-Python".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CNRIPYTHON);
        if ("Condor-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CONDOR1_1);
        if ("CPAL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CPAL1_0);
        if ("CPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CPL1_0);
        if ("CPOL-1.02".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CPOL1_02);
        if ("Crossword".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CROSSWORD);
        if ("CrystalStacker".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CRYSTALSTACKER);
        if ("CUA-OPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CUAOPL1_0);
        if ("Cube".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CUBE);
        if ("curl".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.CURL);
        if ("D-FSL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.DFSL1_0);
        if ("diffmark".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.DIFFMARK);
        if ("DOC".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.DOC);
        if ("Dotseqn".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.DOTSEQN);
        if ("DSDP".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.DSDP);
        if ("dvipdfm".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.DVIPDFM);
        if ("ECL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ECL1_0);
        if ("ECL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ECL2_0);
        if ("EFL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EFL1_0);
        if ("EFL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EFL2_0);
        if ("eGenix".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EGENIX);
        if ("Entessa".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ENTESSA);
        if ("EPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EPL1_0);
        if ("EPL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EPL2_0);
        if ("ErlPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ERLPL1_1);
        if ("EUDatagrid".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EUDATAGRID);
        if ("EUPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EUPL1_0);
        if ("EUPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EUPL1_1);
        if ("EUPL-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EUPL1_2);
        if ("Eurosym".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.EUROSYM);
        if ("Fair".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FAIR);
        if ("Frameworx-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FRAMEWORX1_0);
        if ("FreeImage".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FREEIMAGE);
        if ("FSFAP".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FSFAP);
        if ("FSFUL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FSFUL);
        if ("FSFULLR".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FSFULLR);
        if ("FTL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.FTL);
        if ("GFDL-1.1-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GFDL1_1ONLY);
        if ("GFDL-1.1-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GFDL1_1ORLATER);
        if ("GFDL-1.2-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GFDL1_2ONLY);
        if ("GFDL-1.2-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GFDL1_2ORLATER);
        if ("GFDL-1.3-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GFDL1_3ONLY);
        if ("GFDL-1.3-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GFDL1_3ORLATER);
        if ("Giftware".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GIFTWARE);
        if ("GL2PS".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GL2PS);
        if ("Glide".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GLIDE);
        if ("Glulxe".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GLULXE);
        if ("gnuplot".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GNUPLOT);
        if ("GPL-1.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GPL1_0ONLY);
        if ("GPL-1.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GPL1_0ORLATER);
        if ("GPL-2.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GPL2_0ONLY);
        if ("GPL-2.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GPL2_0ORLATER);
        if ("GPL-3.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GPL3_0ONLY);
        if ("GPL-3.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GPL3_0ORLATER);
        if ("gSOAP-1.3b".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.GSOAP1_3B);
        if ("HaskellReport".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.HASKELLREPORT);
        if ("HPND".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.HPND);
        if ("IBM-pibs".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IBMPIBS);
        if ("ICU".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ICU);
        if ("IJG".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IJG);
        if ("ImageMagick".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IMAGEMAGICK);
        if ("iMatix".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IMATIX);
        if ("Imlib2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IMLIB2);
        if ("Info-ZIP".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.INFOZIP);
        if ("Intel-ACPI".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.INTELACPI);
        if ("Intel".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.INTEL);
        if ("Interbase-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.INTERBASE1_0);
        if ("IPA".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IPA);
        if ("IPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.IPL1_0);
        if ("ISC".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ISC);
        if ("JasPer-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.JASPER2_0);
        if ("JSON".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.JSON);
        if ("LAL-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LAL1_2);
        if ("LAL-1.3".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LAL1_3);
        if ("Latex2e".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LATEX2E);
        if ("Leptonica".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LEPTONICA);
        if ("LGPL-2.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPL2_0ONLY);
        if ("LGPL-2.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPL2_0ORLATER);
        if ("LGPL-2.1-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPL2_1ONLY);
        if ("LGPL-2.1-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPL2_1ORLATER);
        if ("LGPL-3.0-only".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPL3_0ONLY);
        if ("LGPL-3.0-or-later".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPL3_0ORLATER);
        if ("LGPLLR".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LGPLLR);
        if ("Libpng".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LIBPNG);
        if ("libtiff".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LIBTIFF);
        if ("LiLiQ-P-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LILIQP1_1);
        if ("LiLiQ-R-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LILIQR1_1);
        if ("LiLiQ-Rplus-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LILIQRPLUS1_1);
        if ("Linux-OpenIB".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LINUXOPENIB);
        if ("LPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPL1_0);
        if ("LPL-1.02".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPL1_02);
        if ("LPPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPPL1_0);
        if ("LPPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPPL1_1);
        if ("LPPL-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPPL1_2);
        if ("LPPL-1.3a".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPPL1_3A);
        if ("LPPL-1.3c".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.LPPL1_3C);
        if ("MakeIndex".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MAKEINDEX);
        if ("MirOS".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MIROS);
        if ("MIT-0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MIT0);
        if ("MIT-advertising".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MITADVERTISING);
        if ("MIT-CMU".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MITCMU);
        if ("MIT-enna".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MITENNA);
        if ("MIT-feh".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MITFEH);
        if ("MIT".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MIT);
        if ("MITNFA".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MITNFA);
        if ("Motosoto".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MOTOSOTO);
        if ("mpich2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MPICH2);
        if ("MPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MPL1_0);
        if ("MPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MPL1_1);
        if ("MPL-2.0-no-copyleft-exception".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MPL2_0NOCOPYLEFTEXCEPTION);
        if ("MPL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MPL2_0);
        if ("MS-PL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MSPL);
        if ("MS-RL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MSRL);
        if ("MTLL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MTLL);
        if ("Multics".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MULTICS);
        if ("Mup".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.MUP);
        if ("NASA-1.3".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NASA1_3);
        if ("Naumen".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NAUMEN);
        if ("NBPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NBPL1_0);
        if ("NCSA".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NCSA);
        if ("Net-SNMP".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NETSNMP);
        if ("NetCDF".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NETCDF);
        if ("Newsletr".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NEWSLETR);
        if ("NGPL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NGPL);
        if ("NLOD-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NLOD1_0);
        if ("NLPL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NLPL);
        if ("Nokia".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NOKIA);
        if ("NOSL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NOSL);
        if ("Noweb".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NOWEB);
        if ("NPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NPL1_0);
        if ("NPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NPL1_1);
        if ("NPOSL-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NPOSL3_0);
        if ("NRL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NRL);
        if ("NTP".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.NTP);
        if ("OCCT-PL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OCCTPL);
        if ("OCLC-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OCLC2_0);
        if ("ODbL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ODBL1_0);
        if ("OFL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OFL1_0);
        if ("OFL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OFL1_1);
        if ("OGTSL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OGTSL);
        if ("OLDAP-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP1_1);
        if ("OLDAP-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP1_2);
        if ("OLDAP-1.3".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP1_3);
        if ("OLDAP-1.4".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP1_4);
        if ("OLDAP-2.0.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_0_1);
        if ("OLDAP-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_0);
        if ("OLDAP-2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_1);
        if ("OLDAP-2.2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_2_1);
        if ("OLDAP-2.2.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_2_2);
        if ("OLDAP-2.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_2);
        if ("OLDAP-2.3".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_3);
        if ("OLDAP-2.4".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_4);
        if ("OLDAP-2.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_5);
        if ("OLDAP-2.6".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_6);
        if ("OLDAP-2.7".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_7);
        if ("OLDAP-2.8".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OLDAP2_8);
        if ("OML".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OML);
        if ("OpenSSL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OPENSSL);
        if ("OPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OPL1_0);
        if ("OSET-PL-2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OSETPL2_1);
        if ("OSL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OSL1_0);
        if ("OSL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OSL1_1);
        if ("OSL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OSL2_0);
        if ("OSL-2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OSL2_1);
        if ("OSL-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.OSL3_0);
        if ("PDDL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PDDL1_0);
        if ("PHP-3.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PHP3_0);
        if ("PHP-3.01".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PHP3_01);
        if ("Plexus".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PLEXUS);
        if ("PostgreSQL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.POSTGRESQL);
        if ("psfrag".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PSFRAG);
        if ("psutils".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PSUTILS);
        if ("Python-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.PYTHON2_0);
        if ("Qhull".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.QHULL);
        if ("QPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.QPL1_0);
        if ("Rdisc".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RDISC);
        if ("RHeCos-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RHECOS1_1);
        if ("RPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RPL1_1);
        if ("RPL-1.5".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RPL1_5);
        if ("RPSL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RPSL1_0);
        if ("RSA-MD".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RSAMD);
        if ("RSCPL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RSCPL);
        if ("Ruby".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.RUBY);
        if ("SAX-PD".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SAXPD);
        if ("Saxpath".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SAXPATH);
        if ("SCEA".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SCEA);
        if ("Sendmail".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SENDMAIL);
        if ("SGI-B-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SGIB1_0);
        if ("SGI-B-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SGIB1_1);
        if ("SGI-B-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SGIB2_0);
        if ("SimPL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SIMPL2_0);
        if ("SISSL-1.2".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SISSL1_2);
        if ("SISSL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SISSL);
        if ("Sleepycat".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SLEEPYCAT);
        if ("SMLNJ".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SMLNJ);
        if ("SMPPL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SMPPL);
        if ("SNIA".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SNIA);
        if ("Spencer-86".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SPENCER86);
        if ("Spencer-94".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SPENCER94);
        if ("Spencer-99".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SPENCER99);
        if ("SPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SPL1_0);
        if ("SugarCRM-1.1.3".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SUGARCRM1_1_3);
        if ("SWL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.SWL);
        if ("TCL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.TCL);
        if ("TCP-wrappers".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.TCPWRAPPERS);
        if ("TMate".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.TMATE);
        if ("TORQUE-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.TORQUE1_1);
        if ("TOSL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.TOSL);
        if ("Unicode-DFS-2015".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.UNICODEDFS2015);
        if ("Unicode-DFS-2016".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.UNICODEDFS2016);
        if ("Unicode-TOU".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.UNICODETOU);
        if ("Unlicense".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.UNLICENSE);
        if ("UPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.UPL1_0);
        if ("Vim".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.VIM);
        if ("VOSTROM".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.VOSTROM);
        if ("VSL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.VSL1_0);
        if ("W3C-19980720".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.W3C19980720);
        if ("W3C-20150513".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.W3C20150513);
        if ("W3C".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.W3C);
        if ("Watcom-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.WATCOM1_0);
        if ("Wsuipa".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.WSUIPA);
        if ("WTFPL".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.WTFPL);
        if ("X11".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.X11);
        if ("Xerox".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.XEROX);
        if ("XFree86-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.XFREE861_1);
        if ("xinetd".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.XINETD);
        if ("Xnet".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.XNET);
        if ("xpp".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.XPP);
        if ("XSkat".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.XSKAT);
        if ("YPL-1.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.YPL1_0);
        if ("YPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.YPL1_1);
        if ("Zed".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZED);
        if ("Zend-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZEND2_0);
        if ("Zimbra-1.3".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZIMBRA1_3);
        if ("Zimbra-1.4".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZIMBRA1_4);
        if ("zlib-acknowledgement".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZLIBACKNOWLEDGEMENT);
        if ("Zlib".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZLIB);
        if ("ZPL-1.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZPL1_1);
        if ("ZPL-2.0".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZPL2_0);
        if ("ZPL-2.1".equals(codeString))
          return new Enumeration<SPDXLicense>(this, SPDXLicense.ZPL2_1);
        throw new FHIRException("Unknown SPDXLicense code '"+codeString+"'");
        }
    public String toCode(SPDXLicense code) {
      if (code == SPDXLicense.NOTOPENSOURCE)
        return "not-open-source";
      if (code == SPDXLicense._0BSD)
        return "0BSD";
      if (code == SPDXLicense.AAL)
        return "AAL";
      if (code == SPDXLicense.ABSTYLES)
        return "Abstyles";
      if (code == SPDXLicense.ADOBE2006)
        return "Adobe-2006";
      if (code == SPDXLicense.ADOBEGLYPH)
        return "Adobe-Glyph";
      if (code == SPDXLicense.ADSL)
        return "ADSL";
      if (code == SPDXLicense.AFL1_1)
        return "AFL-1.1";
      if (code == SPDXLicense.AFL1_2)
        return "AFL-1.2";
      if (code == SPDXLicense.AFL2_0)
        return "AFL-2.0";
      if (code == SPDXLicense.AFL2_1)
        return "AFL-2.1";
      if (code == SPDXLicense.AFL3_0)
        return "AFL-3.0";
      if (code == SPDXLicense.AFMPARSE)
        return "Afmparse";
      if (code == SPDXLicense.AGPL1_0ONLY)
        return "AGPL-1.0-only";
      if (code == SPDXLicense.AGPL1_0ORLATER)
        return "AGPL-1.0-or-later";
      if (code == SPDXLicense.AGPL3_0ONLY)
        return "AGPL-3.0-only";
      if (code == SPDXLicense.AGPL3_0ORLATER)
        return "AGPL-3.0-or-later";
      if (code == SPDXLicense.ALADDIN)
        return "Aladdin";
      if (code == SPDXLicense.AMDPLPA)
        return "AMDPLPA";
      if (code == SPDXLicense.AML)
        return "AML";
      if (code == SPDXLicense.AMPAS)
        return "AMPAS";
      if (code == SPDXLicense.ANTLRPD)
        return "ANTLR-PD";
      if (code == SPDXLicense.APACHE1_0)
        return "Apache-1.0";
      if (code == SPDXLicense.APACHE1_1)
        return "Apache-1.1";
      if (code == SPDXLicense.APACHE2_0)
        return "Apache-2.0";
      if (code == SPDXLicense.APAFML)
        return "APAFML";
      if (code == SPDXLicense.APL1_0)
        return "APL-1.0";
      if (code == SPDXLicense.APSL1_0)
        return "APSL-1.0";
      if (code == SPDXLicense.APSL1_1)
        return "APSL-1.1";
      if (code == SPDXLicense.APSL1_2)
        return "APSL-1.2";
      if (code == SPDXLicense.APSL2_0)
        return "APSL-2.0";
      if (code == SPDXLicense.ARTISTIC1_0CL8)
        return "Artistic-1.0-cl8";
      if (code == SPDXLicense.ARTISTIC1_0PERL)
        return "Artistic-1.0-Perl";
      if (code == SPDXLicense.ARTISTIC1_0)
        return "Artistic-1.0";
      if (code == SPDXLicense.ARTISTIC2_0)
        return "Artistic-2.0";
      if (code == SPDXLicense.BAHYPH)
        return "Bahyph";
      if (code == SPDXLicense.BARR)
        return "Barr";
      if (code == SPDXLicense.BEERWARE)
        return "Beerware";
      if (code == SPDXLicense.BITTORRENT1_0)
        return "BitTorrent-1.0";
      if (code == SPDXLicense.BITTORRENT1_1)
        return "BitTorrent-1.1";
      if (code == SPDXLicense.BORCEUX)
        return "Borceux";
      if (code == SPDXLicense.BSD1CLAUSE)
        return "BSD-1-Clause";
      if (code == SPDXLicense.BSD2CLAUSEFREEBSD)
        return "BSD-2-Clause-FreeBSD";
      if (code == SPDXLicense.BSD2CLAUSENETBSD)
        return "BSD-2-Clause-NetBSD";
      if (code == SPDXLicense.BSD2CLAUSEPATENT)
        return "BSD-2-Clause-Patent";
      if (code == SPDXLicense.BSD2CLAUSE)
        return "BSD-2-Clause";
      if (code == SPDXLicense.BSD3CLAUSEATTRIBUTION)
        return "BSD-3-Clause-Attribution";
      if (code == SPDXLicense.BSD3CLAUSECLEAR)
        return "BSD-3-Clause-Clear";
      if (code == SPDXLicense.BSD3CLAUSELBNL)
        return "BSD-3-Clause-LBNL";
      if (code == SPDXLicense.BSD3CLAUSENONUCLEARLICENSE2014)
        return "BSD-3-Clause-No-Nuclear-License-2014";
      if (code == SPDXLicense.BSD3CLAUSENONUCLEARLICENSE)
        return "BSD-3-Clause-No-Nuclear-License";
      if (code == SPDXLicense.BSD3CLAUSENONUCLEARWARRANTY)
        return "BSD-3-Clause-No-Nuclear-Warranty";
      if (code == SPDXLicense.BSD3CLAUSE)
        return "BSD-3-Clause";
      if (code == SPDXLicense.BSD4CLAUSEUC)
        return "BSD-4-Clause-UC";
      if (code == SPDXLicense.BSD4CLAUSE)
        return "BSD-4-Clause";
      if (code == SPDXLicense.BSDPROTECTION)
        return "BSD-Protection";
      if (code == SPDXLicense.BSDSOURCECODE)
        return "BSD-Source-Code";
      if (code == SPDXLicense.BSL1_0)
        return "BSL-1.0";
      if (code == SPDXLicense.BZIP21_0_5)
        return "bzip2-1.0.5";
      if (code == SPDXLicense.BZIP21_0_6)
        return "bzip2-1.0.6";
      if (code == SPDXLicense.CALDERA)
        return "Caldera";
      if (code == SPDXLicense.CATOSL1_1)
        return "CATOSL-1.1";
      if (code == SPDXLicense.CCBY1_0)
        return "CC-BY-1.0";
      if (code == SPDXLicense.CCBY2_0)
        return "CC-BY-2.0";
      if (code == SPDXLicense.CCBY2_5)
        return "CC-BY-2.5";
      if (code == SPDXLicense.CCBY3_0)
        return "CC-BY-3.0";
      if (code == SPDXLicense.CCBY4_0)
        return "CC-BY-4.0";
      if (code == SPDXLicense.CCBYNC1_0)
        return "CC-BY-NC-1.0";
      if (code == SPDXLicense.CCBYNC2_0)
        return "CC-BY-NC-2.0";
      if (code == SPDXLicense.CCBYNC2_5)
        return "CC-BY-NC-2.5";
      if (code == SPDXLicense.CCBYNC3_0)
        return "CC-BY-NC-3.0";
      if (code == SPDXLicense.CCBYNC4_0)
        return "CC-BY-NC-4.0";
      if (code == SPDXLicense.CCBYNCND1_0)
        return "CC-BY-NC-ND-1.0";
      if (code == SPDXLicense.CCBYNCND2_0)
        return "CC-BY-NC-ND-2.0";
      if (code == SPDXLicense.CCBYNCND2_5)
        return "CC-BY-NC-ND-2.5";
      if (code == SPDXLicense.CCBYNCND3_0)
        return "CC-BY-NC-ND-3.0";
      if (code == SPDXLicense.CCBYNCND4_0)
        return "CC-BY-NC-ND-4.0";
      if (code == SPDXLicense.CCBYNCSA1_0)
        return "CC-BY-NC-SA-1.0";
      if (code == SPDXLicense.CCBYNCSA2_0)
        return "CC-BY-NC-SA-2.0";
      if (code == SPDXLicense.CCBYNCSA2_5)
        return "CC-BY-NC-SA-2.5";
      if (code == SPDXLicense.CCBYNCSA3_0)
        return "CC-BY-NC-SA-3.0";
      if (code == SPDXLicense.CCBYNCSA4_0)
        return "CC-BY-NC-SA-4.0";
      if (code == SPDXLicense.CCBYND1_0)
        return "CC-BY-ND-1.0";
      if (code == SPDXLicense.CCBYND2_0)
        return "CC-BY-ND-2.0";
      if (code == SPDXLicense.CCBYND2_5)
        return "CC-BY-ND-2.5";
      if (code == SPDXLicense.CCBYND3_0)
        return "CC-BY-ND-3.0";
      if (code == SPDXLicense.CCBYND4_0)
        return "CC-BY-ND-4.0";
      if (code == SPDXLicense.CCBYSA1_0)
        return "CC-BY-SA-1.0";
      if (code == SPDXLicense.CCBYSA2_0)
        return "CC-BY-SA-2.0";
      if (code == SPDXLicense.CCBYSA2_5)
        return "CC-BY-SA-2.5";
      if (code == SPDXLicense.CCBYSA3_0)
        return "CC-BY-SA-3.0";
      if (code == SPDXLicense.CCBYSA4_0)
        return "CC-BY-SA-4.0";
      if (code == SPDXLicense.CC01_0)
        return "CC0-1.0";
      if (code == SPDXLicense.CDDL1_0)
        return "CDDL-1.0";
      if (code == SPDXLicense.CDDL1_1)
        return "CDDL-1.1";
      if (code == SPDXLicense.CDLAPERMISSIVE1_0)
        return "CDLA-Permissive-1.0";
      if (code == SPDXLicense.CDLASHARING1_0)
        return "CDLA-Sharing-1.0";
      if (code == SPDXLicense.CECILL1_0)
        return "CECILL-1.0";
      if (code == SPDXLicense.CECILL1_1)
        return "CECILL-1.1";
      if (code == SPDXLicense.CECILL2_0)
        return "CECILL-2.0";
      if (code == SPDXLicense.CECILL2_1)
        return "CECILL-2.1";
      if (code == SPDXLicense.CECILLB)
        return "CECILL-B";
      if (code == SPDXLicense.CECILLC)
        return "CECILL-C";
      if (code == SPDXLicense.CLARTISTIC)
        return "ClArtistic";
      if (code == SPDXLicense.CNRIJYTHON)
        return "CNRI-Jython";
      if (code == SPDXLicense.CNRIPYTHONGPLCOMPATIBLE)
        return "CNRI-Python-GPL-Compatible";
      if (code == SPDXLicense.CNRIPYTHON)
        return "CNRI-Python";
      if (code == SPDXLicense.CONDOR1_1)
        return "Condor-1.1";
      if (code == SPDXLicense.CPAL1_0)
        return "CPAL-1.0";
      if (code == SPDXLicense.CPL1_0)
        return "CPL-1.0";
      if (code == SPDXLicense.CPOL1_02)
        return "CPOL-1.02";
      if (code == SPDXLicense.CROSSWORD)
        return "Crossword";
      if (code == SPDXLicense.CRYSTALSTACKER)
        return "CrystalStacker";
      if (code == SPDXLicense.CUAOPL1_0)
        return "CUA-OPL-1.0";
      if (code == SPDXLicense.CUBE)
        return "Cube";
      if (code == SPDXLicense.CURL)
        return "curl";
      if (code == SPDXLicense.DFSL1_0)
        return "D-FSL-1.0";
      if (code == SPDXLicense.DIFFMARK)
        return "diffmark";
      if (code == SPDXLicense.DOC)
        return "DOC";
      if (code == SPDXLicense.DOTSEQN)
        return "Dotseqn";
      if (code == SPDXLicense.DSDP)
        return "DSDP";
      if (code == SPDXLicense.DVIPDFM)
        return "dvipdfm";
      if (code == SPDXLicense.ECL1_0)
        return "ECL-1.0";
      if (code == SPDXLicense.ECL2_0)
        return "ECL-2.0";
      if (code == SPDXLicense.EFL1_0)
        return "EFL-1.0";
      if (code == SPDXLicense.EFL2_0)
        return "EFL-2.0";
      if (code == SPDXLicense.EGENIX)
        return "eGenix";
      if (code == SPDXLicense.ENTESSA)
        return "Entessa";
      if (code == SPDXLicense.EPL1_0)
        return "EPL-1.0";
      if (code == SPDXLicense.EPL2_0)
        return "EPL-2.0";
      if (code == SPDXLicense.ERLPL1_1)
        return "ErlPL-1.1";
      if (code == SPDXLicense.EUDATAGRID)
        return "EUDatagrid";
      if (code == SPDXLicense.EUPL1_0)
        return "EUPL-1.0";
      if (code == SPDXLicense.EUPL1_1)
        return "EUPL-1.1";
      if (code == SPDXLicense.EUPL1_2)
        return "EUPL-1.2";
      if (code == SPDXLicense.EUROSYM)
        return "Eurosym";
      if (code == SPDXLicense.FAIR)
        return "Fair";
      if (code == SPDXLicense.FRAMEWORX1_0)
        return "Frameworx-1.0";
      if (code == SPDXLicense.FREEIMAGE)
        return "FreeImage";
      if (code == SPDXLicense.FSFAP)
        return "FSFAP";
      if (code == SPDXLicense.FSFUL)
        return "FSFUL";
      if (code == SPDXLicense.FSFULLR)
        return "FSFULLR";
      if (code == SPDXLicense.FTL)
        return "FTL";
      if (code == SPDXLicense.GFDL1_1ONLY)
        return "GFDL-1.1-only";
      if (code == SPDXLicense.GFDL1_1ORLATER)
        return "GFDL-1.1-or-later";
      if (code == SPDXLicense.GFDL1_2ONLY)
        return "GFDL-1.2-only";
      if (code == SPDXLicense.GFDL1_2ORLATER)
        return "GFDL-1.2-or-later";
      if (code == SPDXLicense.GFDL1_3ONLY)
        return "GFDL-1.3-only";
      if (code == SPDXLicense.GFDL1_3ORLATER)
        return "GFDL-1.3-or-later";
      if (code == SPDXLicense.GIFTWARE)
        return "Giftware";
      if (code == SPDXLicense.GL2PS)
        return "GL2PS";
      if (code == SPDXLicense.GLIDE)
        return "Glide";
      if (code == SPDXLicense.GLULXE)
        return "Glulxe";
      if (code == SPDXLicense.GNUPLOT)
        return "gnuplot";
      if (code == SPDXLicense.GPL1_0ONLY)
        return "GPL-1.0-only";
      if (code == SPDXLicense.GPL1_0ORLATER)
        return "GPL-1.0-or-later";
      if (code == SPDXLicense.GPL2_0ONLY)
        return "GPL-2.0-only";
      if (code == SPDXLicense.GPL2_0ORLATER)
        return "GPL-2.0-or-later";
      if (code == SPDXLicense.GPL3_0ONLY)
        return "GPL-3.0-only";
      if (code == SPDXLicense.GPL3_0ORLATER)
        return "GPL-3.0-or-later";
      if (code == SPDXLicense.GSOAP1_3B)
        return "gSOAP-1.3b";
      if (code == SPDXLicense.HASKELLREPORT)
        return "HaskellReport";
      if (code == SPDXLicense.HPND)
        return "HPND";
      if (code == SPDXLicense.IBMPIBS)
        return "IBM-pibs";
      if (code == SPDXLicense.ICU)
        return "ICU";
      if (code == SPDXLicense.IJG)
        return "IJG";
      if (code == SPDXLicense.IMAGEMAGICK)
        return "ImageMagick";
      if (code == SPDXLicense.IMATIX)
        return "iMatix";
      if (code == SPDXLicense.IMLIB2)
        return "Imlib2";
      if (code == SPDXLicense.INFOZIP)
        return "Info-ZIP";
      if (code == SPDXLicense.INTELACPI)
        return "Intel-ACPI";
      if (code == SPDXLicense.INTEL)
        return "Intel";
      if (code == SPDXLicense.INTERBASE1_0)
        return "Interbase-1.0";
      if (code == SPDXLicense.IPA)
        return "IPA";
      if (code == SPDXLicense.IPL1_0)
        return "IPL-1.0";
      if (code == SPDXLicense.ISC)
        return "ISC";
      if (code == SPDXLicense.JASPER2_0)
        return "JasPer-2.0";
      if (code == SPDXLicense.JSON)
        return "JSON";
      if (code == SPDXLicense.LAL1_2)
        return "LAL-1.2";
      if (code == SPDXLicense.LAL1_3)
        return "LAL-1.3";
      if (code == SPDXLicense.LATEX2E)
        return "Latex2e";
      if (code == SPDXLicense.LEPTONICA)
        return "Leptonica";
      if (code == SPDXLicense.LGPL2_0ONLY)
        return "LGPL-2.0-only";
      if (code == SPDXLicense.LGPL2_0ORLATER)
        return "LGPL-2.0-or-later";
      if (code == SPDXLicense.LGPL2_1ONLY)
        return "LGPL-2.1-only";
      if (code == SPDXLicense.LGPL2_1ORLATER)
        return "LGPL-2.1-or-later";
      if (code == SPDXLicense.LGPL3_0ONLY)
        return "LGPL-3.0-only";
      if (code == SPDXLicense.LGPL3_0ORLATER)
        return "LGPL-3.0-or-later";
      if (code == SPDXLicense.LGPLLR)
        return "LGPLLR";
      if (code == SPDXLicense.LIBPNG)
        return "Libpng";
      if (code == SPDXLicense.LIBTIFF)
        return "libtiff";
      if (code == SPDXLicense.LILIQP1_1)
        return "LiLiQ-P-1.1";
      if (code == SPDXLicense.LILIQR1_1)
        return "LiLiQ-R-1.1";
      if (code == SPDXLicense.LILIQRPLUS1_1)
        return "LiLiQ-Rplus-1.1";
      if (code == SPDXLicense.LINUXOPENIB)
        return "Linux-OpenIB";
      if (code == SPDXLicense.LPL1_0)
        return "LPL-1.0";
      if (code == SPDXLicense.LPL1_02)
        return "LPL-1.02";
      if (code == SPDXLicense.LPPL1_0)
        return "LPPL-1.0";
      if (code == SPDXLicense.LPPL1_1)
        return "LPPL-1.1";
      if (code == SPDXLicense.LPPL1_2)
        return "LPPL-1.2";
      if (code == SPDXLicense.LPPL1_3A)
        return "LPPL-1.3a";
      if (code == SPDXLicense.LPPL1_3C)
        return "LPPL-1.3c";
      if (code == SPDXLicense.MAKEINDEX)
        return "MakeIndex";
      if (code == SPDXLicense.MIROS)
        return "MirOS";
      if (code == SPDXLicense.MIT0)
        return "MIT-0";
      if (code == SPDXLicense.MITADVERTISING)
        return "MIT-advertising";
      if (code == SPDXLicense.MITCMU)
        return "MIT-CMU";
      if (code == SPDXLicense.MITENNA)
        return "MIT-enna";
      if (code == SPDXLicense.MITFEH)
        return "MIT-feh";
      if (code == SPDXLicense.MIT)
        return "MIT";
      if (code == SPDXLicense.MITNFA)
        return "MITNFA";
      if (code == SPDXLicense.MOTOSOTO)
        return "Motosoto";
      if (code == SPDXLicense.MPICH2)
        return "mpich2";
      if (code == SPDXLicense.MPL1_0)
        return "MPL-1.0";
      if (code == SPDXLicense.MPL1_1)
        return "MPL-1.1";
      if (code == SPDXLicense.MPL2_0NOCOPYLEFTEXCEPTION)
        return "MPL-2.0-no-copyleft-exception";
      if (code == SPDXLicense.MPL2_0)
        return "MPL-2.0";
      if (code == SPDXLicense.MSPL)
        return "MS-PL";
      if (code == SPDXLicense.MSRL)
        return "MS-RL";
      if (code == SPDXLicense.MTLL)
        return "MTLL";
      if (code == SPDXLicense.MULTICS)
        return "Multics";
      if (code == SPDXLicense.MUP)
        return "Mup";
      if (code == SPDXLicense.NASA1_3)
        return "NASA-1.3";
      if (code == SPDXLicense.NAUMEN)
        return "Naumen";
      if (code == SPDXLicense.NBPL1_0)
        return "NBPL-1.0";
      if (code == SPDXLicense.NCSA)
        return "NCSA";
      if (code == SPDXLicense.NETSNMP)
        return "Net-SNMP";
      if (code == SPDXLicense.NETCDF)
        return "NetCDF";
      if (code == SPDXLicense.NEWSLETR)
        return "Newsletr";
      if (code == SPDXLicense.NGPL)
        return "NGPL";
      if (code == SPDXLicense.NLOD1_0)
        return "NLOD-1.0";
      if (code == SPDXLicense.NLPL)
        return "NLPL";
      if (code == SPDXLicense.NOKIA)
        return "Nokia";
      if (code == SPDXLicense.NOSL)
        return "NOSL";
      if (code == SPDXLicense.NOWEB)
        return "Noweb";
      if (code == SPDXLicense.NPL1_0)
        return "NPL-1.0";
      if (code == SPDXLicense.NPL1_1)
        return "NPL-1.1";
      if (code == SPDXLicense.NPOSL3_0)
        return "NPOSL-3.0";
      if (code == SPDXLicense.NRL)
        return "NRL";
      if (code == SPDXLicense.NTP)
        return "NTP";
      if (code == SPDXLicense.OCCTPL)
        return "OCCT-PL";
      if (code == SPDXLicense.OCLC2_0)
        return "OCLC-2.0";
      if (code == SPDXLicense.ODBL1_0)
        return "ODbL-1.0";
      if (code == SPDXLicense.OFL1_0)
        return "OFL-1.0";
      if (code == SPDXLicense.OFL1_1)
        return "OFL-1.1";
      if (code == SPDXLicense.OGTSL)
        return "OGTSL";
      if (code == SPDXLicense.OLDAP1_1)
        return "OLDAP-1.1";
      if (code == SPDXLicense.OLDAP1_2)
        return "OLDAP-1.2";
      if (code == SPDXLicense.OLDAP1_3)
        return "OLDAP-1.3";
      if (code == SPDXLicense.OLDAP1_4)
        return "OLDAP-1.4";
      if (code == SPDXLicense.OLDAP2_0_1)
        return "OLDAP-2.0.1";
      if (code == SPDXLicense.OLDAP2_0)
        return "OLDAP-2.0";
      if (code == SPDXLicense.OLDAP2_1)
        return "OLDAP-2.1";
      if (code == SPDXLicense.OLDAP2_2_1)
        return "OLDAP-2.2.1";
      if (code == SPDXLicense.OLDAP2_2_2)
        return "OLDAP-2.2.2";
      if (code == SPDXLicense.OLDAP2_2)
        return "OLDAP-2.2";
      if (code == SPDXLicense.OLDAP2_3)
        return "OLDAP-2.3";
      if (code == SPDXLicense.OLDAP2_4)
        return "OLDAP-2.4";
      if (code == SPDXLicense.OLDAP2_5)
        return "OLDAP-2.5";
      if (code == SPDXLicense.OLDAP2_6)
        return "OLDAP-2.6";
      if (code == SPDXLicense.OLDAP2_7)
        return "OLDAP-2.7";
      if (code == SPDXLicense.OLDAP2_8)
        return "OLDAP-2.8";
      if (code == SPDXLicense.OML)
        return "OML";
      if (code == SPDXLicense.OPENSSL)
        return "OpenSSL";
      if (code == SPDXLicense.OPL1_0)
        return "OPL-1.0";
      if (code == SPDXLicense.OSETPL2_1)
        return "OSET-PL-2.1";
      if (code == SPDXLicense.OSL1_0)
        return "OSL-1.0";
      if (code == SPDXLicense.OSL1_1)
        return "OSL-1.1";
      if (code == SPDXLicense.OSL2_0)
        return "OSL-2.0";
      if (code == SPDXLicense.OSL2_1)
        return "OSL-2.1";
      if (code == SPDXLicense.OSL3_0)
        return "OSL-3.0";
      if (code == SPDXLicense.PDDL1_0)
        return "PDDL-1.0";
      if (code == SPDXLicense.PHP3_0)
        return "PHP-3.0";
      if (code == SPDXLicense.PHP3_01)
        return "PHP-3.01";
      if (code == SPDXLicense.PLEXUS)
        return "Plexus";
      if (code == SPDXLicense.POSTGRESQL)
        return "PostgreSQL";
      if (code == SPDXLicense.PSFRAG)
        return "psfrag";
      if (code == SPDXLicense.PSUTILS)
        return "psutils";
      if (code == SPDXLicense.PYTHON2_0)
        return "Python-2.0";
      if (code == SPDXLicense.QHULL)
        return "Qhull";
      if (code == SPDXLicense.QPL1_0)
        return "QPL-1.0";
      if (code == SPDXLicense.RDISC)
        return "Rdisc";
      if (code == SPDXLicense.RHECOS1_1)
        return "RHeCos-1.1";
      if (code == SPDXLicense.RPL1_1)
        return "RPL-1.1";
      if (code == SPDXLicense.RPL1_5)
        return "RPL-1.5";
      if (code == SPDXLicense.RPSL1_0)
        return "RPSL-1.0";
      if (code == SPDXLicense.RSAMD)
        return "RSA-MD";
      if (code == SPDXLicense.RSCPL)
        return "RSCPL";
      if (code == SPDXLicense.RUBY)
        return "Ruby";
      if (code == SPDXLicense.SAXPD)
        return "SAX-PD";
      if (code == SPDXLicense.SAXPATH)
        return "Saxpath";
      if (code == SPDXLicense.SCEA)
        return "SCEA";
      if (code == SPDXLicense.SENDMAIL)
        return "Sendmail";
      if (code == SPDXLicense.SGIB1_0)
        return "SGI-B-1.0";
      if (code == SPDXLicense.SGIB1_1)
        return "SGI-B-1.1";
      if (code == SPDXLicense.SGIB2_0)
        return "SGI-B-2.0";
      if (code == SPDXLicense.SIMPL2_0)
        return "SimPL-2.0";
      if (code == SPDXLicense.SISSL1_2)
        return "SISSL-1.2";
      if (code == SPDXLicense.SISSL)
        return "SISSL";
      if (code == SPDXLicense.SLEEPYCAT)
        return "Sleepycat";
      if (code == SPDXLicense.SMLNJ)
        return "SMLNJ";
      if (code == SPDXLicense.SMPPL)
        return "SMPPL";
      if (code == SPDXLicense.SNIA)
        return "SNIA";
      if (code == SPDXLicense.SPENCER86)
        return "Spencer-86";
      if (code == SPDXLicense.SPENCER94)
        return "Spencer-94";
      if (code == SPDXLicense.SPENCER99)
        return "Spencer-99";
      if (code == SPDXLicense.SPL1_0)
        return "SPL-1.0";
      if (code == SPDXLicense.SUGARCRM1_1_3)
        return "SugarCRM-1.1.3";
      if (code == SPDXLicense.SWL)
        return "SWL";
      if (code == SPDXLicense.TCL)
        return "TCL";
      if (code == SPDXLicense.TCPWRAPPERS)
        return "TCP-wrappers";
      if (code == SPDXLicense.TMATE)
        return "TMate";
      if (code == SPDXLicense.TORQUE1_1)
        return "TORQUE-1.1";
      if (code == SPDXLicense.TOSL)
        return "TOSL";
      if (code == SPDXLicense.UNICODEDFS2015)
        return "Unicode-DFS-2015";
      if (code == SPDXLicense.UNICODEDFS2016)
        return "Unicode-DFS-2016";
      if (code == SPDXLicense.UNICODETOU)
        return "Unicode-TOU";
      if (code == SPDXLicense.UNLICENSE)
        return "Unlicense";
      if (code == SPDXLicense.UPL1_0)
        return "UPL-1.0";
      if (code == SPDXLicense.VIM)
        return "Vim";
      if (code == SPDXLicense.VOSTROM)
        return "VOSTROM";
      if (code == SPDXLicense.VSL1_0)
        return "VSL-1.0";
      if (code == SPDXLicense.W3C19980720)
        return "W3C-19980720";
      if (code == SPDXLicense.W3C20150513)
        return "W3C-20150513";
      if (code == SPDXLicense.W3C)
        return "W3C";
      if (code == SPDXLicense.WATCOM1_0)
        return "Watcom-1.0";
      if (code == SPDXLicense.WSUIPA)
        return "Wsuipa";
      if (code == SPDXLicense.WTFPL)
        return "WTFPL";
      if (code == SPDXLicense.X11)
        return "X11";
      if (code == SPDXLicense.XEROX)
        return "Xerox";
      if (code == SPDXLicense.XFREE861_1)
        return "XFree86-1.1";
      if (code == SPDXLicense.XINETD)
        return "xinetd";
      if (code == SPDXLicense.XNET)
        return "Xnet";
      if (code == SPDXLicense.XPP)
        return "xpp";
      if (code == SPDXLicense.XSKAT)
        return "XSkat";
      if (code == SPDXLicense.YPL1_0)
        return "YPL-1.0";
      if (code == SPDXLicense.YPL1_1)
        return "YPL-1.1";
      if (code == SPDXLicense.ZED)
        return "Zed";
      if (code == SPDXLicense.ZEND2_0)
        return "Zend-2.0";
      if (code == SPDXLicense.ZIMBRA1_3)
        return "Zimbra-1.3";
      if (code == SPDXLicense.ZIMBRA1_4)
        return "Zimbra-1.4";
      if (code == SPDXLicense.ZLIBACKNOWLEDGEMENT)
        return "zlib-acknowledgement";
      if (code == SPDXLicense.ZLIB)
        return "Zlib";
      if (code == SPDXLicense.ZPL1_1)
        return "ZPL-1.1";
      if (code == SPDXLicense.ZPL2_0)
        return "ZPL-2.0";
      if (code == SPDXLicense.ZPL2_1)
        return "ZPL-2.1";
      return "?";
      }
    public String toSystem(SPDXLicense code) {
      return code.getSystem();
      }
    }

    public enum GuidePageGeneration {
        /**
         * Page is proper xhtml with no templating.  Will be brought across unchanged for standard post-processing.
         */
        HTML, 
        /**
         * Page is markdown with templating.  Will use the template to create a file that imports the markdown file prior to post-processing.
         */
        MARKDOWN, 
        /**
         * Page is xml with templating.  Will use the template to create a file that imports the source file and run the nominated XSLT transform (see parameters) if present prior to post-processing.
         */
        XML, 
        /**
         * Page will be generated by the publication process - no source to bring across.
         */
        GENERATED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuidePageGeneration fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("html".equals(codeString))
          return HTML;
        if ("markdown".equals(codeString))
          return MARKDOWN;
        if ("xml".equals(codeString))
          return XML;
        if ("generated".equals(codeString))
          return GENERATED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuidePageGeneration code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HTML: return "html";
            case MARKDOWN: return "markdown";
            case XML: return "xml";
            case GENERATED: return "generated";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HTML: return "http://hl7.org/fhir/guide-page-generation";
            case MARKDOWN: return "http://hl7.org/fhir/guide-page-generation";
            case XML: return "http://hl7.org/fhir/guide-page-generation";
            case GENERATED: return "http://hl7.org/fhir/guide-page-generation";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HTML: return "Page is proper xhtml with no templating.  Will be brought across unchanged for standard post-processing.";
            case MARKDOWN: return "Page is markdown with templating.  Will use the template to create a file that imports the markdown file prior to post-processing.";
            case XML: return "Page is xml with templating.  Will use the template to create a file that imports the source file and run the nominated XSLT transform (see parameters) if present prior to post-processing.";
            case GENERATED: return "Page will be generated by the publication process - no source to bring across.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HTML: return "HTML";
            case MARKDOWN: return "Markdown";
            case XML: return "XML";
            case GENERATED: return "Generated";
            default: return "?";
          }
        }
    }

  public static class GuidePageGenerationEnumFactory implements EnumFactory<GuidePageGeneration> {
    public GuidePageGeneration fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("html".equals(codeString))
          return GuidePageGeneration.HTML;
        if ("markdown".equals(codeString))
          return GuidePageGeneration.MARKDOWN;
        if ("xml".equals(codeString))
          return GuidePageGeneration.XML;
        if ("generated".equals(codeString))
          return GuidePageGeneration.GENERATED;
        throw new IllegalArgumentException("Unknown GuidePageGeneration code '"+codeString+"'");
        }
        public Enumeration<GuidePageGeneration> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuidePageGeneration>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("html".equals(codeString))
          return new Enumeration<GuidePageGeneration>(this, GuidePageGeneration.HTML);
        if ("markdown".equals(codeString))
          return new Enumeration<GuidePageGeneration>(this, GuidePageGeneration.MARKDOWN);
        if ("xml".equals(codeString))
          return new Enumeration<GuidePageGeneration>(this, GuidePageGeneration.XML);
        if ("generated".equals(codeString))
          return new Enumeration<GuidePageGeneration>(this, GuidePageGeneration.GENERATED);
        throw new FHIRException("Unknown GuidePageGeneration code '"+codeString+"'");
        }
    public String toCode(GuidePageGeneration code) {
      if (code == GuidePageGeneration.HTML)
        return "html";
      if (code == GuidePageGeneration.MARKDOWN)
        return "markdown";
      if (code == GuidePageGeneration.XML)
        return "xml";
      if (code == GuidePageGeneration.GENERATED)
        return "generated";
      return "?";
      }
    public String toSystem(GuidePageGeneration code) {
      return code.getSystem();
      }
    }

    public enum GuideParameterCode {
        /**
         * If the value of this boolean 0..1 parameter is "true" then all conformance resources will have any specified [Resource].version overwritten with the ImplementationGuide.version.
         */
        APPLYBUSINESSVERSION, 
        /**
         * If the value of this boolean 0..1 parameter is "true" then all conformance resources will have any specified [Resource].jurisdiction overwritten with the ImplementationGuide.jurisdiction.
         */
        APPLYJURISDICTION, 
        /**
         * The value of this string 0..* parameter is a subfolder of the build context's location that is to be scanned to load resources. Scope is (if present) a particular resource type.
         */
        PATHRESOURCE, 
        /**
         * The value of this string 0..1 parameter is a subfolder of the build context's location that contains files that are part of the html content processed by the builder.
         */
        PATHPAGES, 
        /**
         * The value of this string 0..1 parameter is a subfolder of the build context's location that is used as the terminology cache. If this is not present, the terminology cache is on the local system, not under version control.
         */
        PATHTXCACHE, 
        /**
         * The value of this string 0..* parameter is a parameter (name=value) when expanding value sets for this implementation guide. This is particularly used to specify the versions of published terminologies such as SNOMED CT.
         */
        EXPANSIONPARAMETER, 
        /**
         * The value of this string 0..1 parameter is either "warning" or "error" (default = "error"). If the value is "warning" then IG build tools allow the IG to be considered successfully build even when there is no internal broken links.
         */
        RULEBROKENLINKS, 
        /**
         * The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in XML format. If not present, the Publication Tool decides whether to generate XML.
         */
        GENERATEXML, 
        /**
         * The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in JSON format. If not present, the Publication Tool decides whether to generate JSON.
         */
        GENERATEJSON, 
        /**
         * The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in Turtle format. If not present, the Publication Tool decides whether to generate Turtle.
         */
        GENERATETURTLE, 
        /**
         * The value of this string singleton parameter is the name of the file to use as the builder template for each generated page (see templating).
         */
        HTMLTEMPLATE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuideParameterCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("apply-business-version".equals(codeString))
          return APPLYBUSINESSVERSION;
        if ("apply-jurisdiction".equals(codeString))
          return APPLYJURISDICTION;
        if ("path-resource".equals(codeString))
          return PATHRESOURCE;
        if ("path-pages".equals(codeString))
          return PATHPAGES;
        if ("path-tx-cache".equals(codeString))
          return PATHTXCACHE;
        if ("expansion-parameter".equals(codeString))
          return EXPANSIONPARAMETER;
        if ("rule-broken-links".equals(codeString))
          return RULEBROKENLINKS;
        if ("generate-xml".equals(codeString))
          return GENERATEXML;
        if ("generate-json".equals(codeString))
          return GENERATEJSON;
        if ("generate-turtle".equals(codeString))
          return GENERATETURTLE;
        if ("html-template".equals(codeString))
          return HTMLTEMPLATE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuideParameterCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "apply-business-version";
            case APPLYJURISDICTION: return "apply-jurisdiction";
            case PATHRESOURCE: return "path-resource";
            case PATHPAGES: return "path-pages";
            case PATHTXCACHE: return "path-tx-cache";
            case EXPANSIONPARAMETER: return "expansion-parameter";
            case RULEBROKENLINKS: return "rule-broken-links";
            case GENERATEXML: return "generate-xml";
            case GENERATEJSON: return "generate-json";
            case GENERATETURTLE: return "generate-turtle";
            case HTMLTEMPLATE: return "html-template";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "http://hl7.org/fhir/guide-parameter-code";
            case APPLYJURISDICTION: return "http://hl7.org/fhir/guide-parameter-code";
            case PATHRESOURCE: return "http://hl7.org/fhir/guide-parameter-code";
            case PATHPAGES: return "http://hl7.org/fhir/guide-parameter-code";
            case PATHTXCACHE: return "http://hl7.org/fhir/guide-parameter-code";
            case EXPANSIONPARAMETER: return "http://hl7.org/fhir/guide-parameter-code";
            case RULEBROKENLINKS: return "http://hl7.org/fhir/guide-parameter-code";
            case GENERATEXML: return "http://hl7.org/fhir/guide-parameter-code";
            case GENERATEJSON: return "http://hl7.org/fhir/guide-parameter-code";
            case GENERATETURTLE: return "http://hl7.org/fhir/guide-parameter-code";
            case HTMLTEMPLATE: return "http://hl7.org/fhir/guide-parameter-code";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "If the value of this boolean 0..1 parameter is \"true\" then all conformance resources will have any specified [Resource].version overwritten with the ImplementationGuide.version.";
            case APPLYJURISDICTION: return "If the value of this boolean 0..1 parameter is \"true\" then all conformance resources will have any specified [Resource].jurisdiction overwritten with the ImplementationGuide.jurisdiction.";
            case PATHRESOURCE: return "The value of this string 0..* parameter is a subfolder of the build context's location that is to be scanned to load resources. Scope is (if present) a particular resource type.";
            case PATHPAGES: return "The value of this string 0..1 parameter is a subfolder of the build context's location that contains files that are part of the html content processed by the builder.";
            case PATHTXCACHE: return "The value of this string 0..1 parameter is a subfolder of the build context's location that is used as the terminology cache. If this is not present, the terminology cache is on the local system, not under version control.";
            case EXPANSIONPARAMETER: return "The value of this string 0..* parameter is a parameter (name=value) when expanding value sets for this implementation guide. This is particularly used to specify the versions of published terminologies such as SNOMED CT.";
            case RULEBROKENLINKS: return "The value of this string 0..1 parameter is either \"warning\" or \"error\" (default = \"error\"). If the value is \"warning\" then IG build tools allow the IG to be considered successfully build even when there is no internal broken links.";
            case GENERATEXML: return "The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in XML format. If not present, the Publication Tool decides whether to generate XML.";
            case GENERATEJSON: return "The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in JSON format. If not present, the Publication Tool decides whether to generate JSON.";
            case GENERATETURTLE: return "The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in Turtle format. If not present, the Publication Tool decides whether to generate Turtle.";
            case HTMLTEMPLATE: return "The value of this string singleton parameter is the name of the file to use as the builder template for each generated page (see templating).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "Apply Business Version";
            case APPLYJURISDICTION: return "Apply Jurisdiction";
            case PATHRESOURCE: return "Resource Path";
            case PATHPAGES: return "Pages Path";
            case PATHTXCACHE: return "Terminology Cache Path";
            case EXPANSIONPARAMETER: return "Expansion Profile";
            case RULEBROKENLINKS: return "Broken Links Rule";
            case GENERATEXML: return "Generate XML";
            case GENERATEJSON: return "Generate JSON";
            case GENERATETURTLE: return "Generate Turtle";
            case HTMLTEMPLATE: return "HTML Template";
            default: return "?";
          }
        }
    }

  public static class GuideParameterCodeEnumFactory implements EnumFactory<GuideParameterCode> {
    public GuideParameterCode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("apply-business-version".equals(codeString))
          return GuideParameterCode.APPLYBUSINESSVERSION;
        if ("apply-jurisdiction".equals(codeString))
          return GuideParameterCode.APPLYJURISDICTION;
        if ("path-resource".equals(codeString))
          return GuideParameterCode.PATHRESOURCE;
        if ("path-pages".equals(codeString))
          return GuideParameterCode.PATHPAGES;
        if ("path-tx-cache".equals(codeString))
          return GuideParameterCode.PATHTXCACHE;
        if ("expansion-parameter".equals(codeString))
          return GuideParameterCode.EXPANSIONPARAMETER;
        if ("rule-broken-links".equals(codeString))
          return GuideParameterCode.RULEBROKENLINKS;
        if ("generate-xml".equals(codeString))
          return GuideParameterCode.GENERATEXML;
        if ("generate-json".equals(codeString))
          return GuideParameterCode.GENERATEJSON;
        if ("generate-turtle".equals(codeString))
          return GuideParameterCode.GENERATETURTLE;
        if ("html-template".equals(codeString))
          return GuideParameterCode.HTMLTEMPLATE;
        throw new IllegalArgumentException("Unknown GuideParameterCode code '"+codeString+"'");
        }
        public Enumeration<GuideParameterCode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuideParameterCode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("apply-business-version".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.APPLYBUSINESSVERSION);
        if ("apply-jurisdiction".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.APPLYJURISDICTION);
        if ("path-resource".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.PATHRESOURCE);
        if ("path-pages".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.PATHPAGES);
        if ("path-tx-cache".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.PATHTXCACHE);
        if ("expansion-parameter".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.EXPANSIONPARAMETER);
        if ("rule-broken-links".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.RULEBROKENLINKS);
        if ("generate-xml".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.GENERATEXML);
        if ("generate-json".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.GENERATEJSON);
        if ("generate-turtle".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.GENERATETURTLE);
        if ("html-template".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.HTMLTEMPLATE);
        throw new FHIRException("Unknown GuideParameterCode code '"+codeString+"'");
        }
    public String toCode(GuideParameterCode code) {
      if (code == GuideParameterCode.APPLYBUSINESSVERSION)
        return "apply-business-version";
      if (code == GuideParameterCode.APPLYJURISDICTION)
        return "apply-jurisdiction";
      if (code == GuideParameterCode.PATHRESOURCE)
        return "path-resource";
      if (code == GuideParameterCode.PATHPAGES)
        return "path-pages";
      if (code == GuideParameterCode.PATHTXCACHE)
        return "path-tx-cache";
      if (code == GuideParameterCode.EXPANSIONPARAMETER)
        return "expansion-parameter";
      if (code == GuideParameterCode.RULEBROKENLINKS)
        return "rule-broken-links";
      if (code == GuideParameterCode.GENERATEXML)
        return "generate-xml";
      if (code == GuideParameterCode.GENERATEJSON)
        return "generate-json";
      if (code == GuideParameterCode.GENERATETURTLE)
        return "generate-turtle";
      if (code == GuideParameterCode.HTMLTEMPLATE)
        return "html-template";
      return "?";
      }
    public String toSystem(GuideParameterCode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ImplementationGuideDependsOnComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The canonical URL of the Implementation guide for the dependency.
         */
        @Child(name = "uri", type = {CanonicalType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identity of the IG that this depends on", formalDefinition="The canonical URL of the Implementation guide for the dependency." )
        protected CanonicalType uri;

        /**
         * The NPM package name for the Implementation Guide that this IG depends on.
         */
        @Child(name = "packageId", type = {IdType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="NPM Package name for IG this depends on", formalDefinition="The NPM package name for the Implementation Guide that this IG depends on." )
        protected IdType packageId;

        /**
         * The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Version of the IG", formalDefinition="The version of the IG that is depended on, when the correct version is required to understand the IG correctly." )
        protected StringType version;

        private static final long serialVersionUID = -215808797L;

    /**
     * Constructor
     */
      public ImplementationGuideDependsOnComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDependsOnComponent(CanonicalType uri) {
        super();
        this.uri = uri;
      }

        /**
         * @return {@link #uri} (The canonical URL of the Implementation guide for the dependency.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public CanonicalType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDependsOnComponent.uri");
            else if (Configuration.doAutoCreate())
              this.uri = new CanonicalType(); // bb
          return this.uri;
        }

        public boolean hasUriElement() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        public boolean hasUri() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        /**
         * @param value {@link #uri} (The canonical URL of the Implementation guide for the dependency.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public ImplementationGuideDependsOnComponent setUriElement(CanonicalType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return The canonical URL of the Implementation guide for the dependency.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value The canonical URL of the Implementation guide for the dependency.
         */
        public ImplementationGuideDependsOnComponent setUri(String value) { 
            if (this.uri == null)
              this.uri = new CanonicalType();
            this.uri.setValue(value);
          return this;
        }

        /**
         * @return {@link #packageId} (The NPM package name for the Implementation Guide that this IG depends on.). This is the underlying object with id, value and extensions. The accessor "getPackageId" gives direct access to the value
         */
        public IdType getPackageIdElement() { 
          if (this.packageId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDependsOnComponent.packageId");
            else if (Configuration.doAutoCreate())
              this.packageId = new IdType(); // bb
          return this.packageId;
        }

        public boolean hasPackageIdElement() { 
          return this.packageId != null && !this.packageId.isEmpty();
        }

        public boolean hasPackageId() { 
          return this.packageId != null && !this.packageId.isEmpty();
        }

        /**
         * @param value {@link #packageId} (The NPM package name for the Implementation Guide that this IG depends on.). This is the underlying object with id, value and extensions. The accessor "getPackageId" gives direct access to the value
         */
        public ImplementationGuideDependsOnComponent setPackageIdElement(IdType value) { 
          this.packageId = value;
          return this;
        }

        /**
         * @return The NPM package name for the Implementation Guide that this IG depends on.
         */
        public String getPackageId() { 
          return this.packageId == null ? null : this.packageId.getValue();
        }

        /**
         * @param value The NPM package name for the Implementation Guide that this IG depends on.
         */
        public ImplementationGuideDependsOnComponent setPackageId(String value) { 
          if (Utilities.noString(value))
            this.packageId = null;
          else {
            if (this.packageId == null)
              this.packageId = new IdType();
            this.packageId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #version} (The version of the IG that is depended on, when the correct version is required to understand the IG correctly.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDependsOnComponent.version");
            else if (Configuration.doAutoCreate())
              this.version = new StringType(); // bb
          return this.version;
        }

        public boolean hasVersionElement() { 
          return this.version != null && !this.version.isEmpty();
        }

        public boolean hasVersion() { 
          return this.version != null && !this.version.isEmpty();
        }

        /**
         * @param value {@link #version} (The version of the IG that is depended on, when the correct version is required to understand the IG correctly.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ImplementationGuideDependsOnComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
         */
        public ImplementationGuideDependsOnComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("uri", "canonical(ImplementationGuide)", "The canonical URL of the Implementation guide for the dependency.", 0, 1, uri));
          children.add(new Property("packageId", "id", "The NPM package name for the Implementation Guide that this IG depends on.", 0, 1, packageId));
          children.add(new Property("version", "string", "The version of the IG that is depended on, when the correct version is required to understand the IG correctly.", 0, 1, version));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 116076: /*uri*/  return new Property("uri", "canonical(ImplementationGuide)", "The canonical URL of the Implementation guide for the dependency.", 0, 1, uri);
          case 1802060801: /*packageId*/  return new Property("packageId", "id", "The NPM package name for the Implementation Guide that this IG depends on.", 0, 1, packageId);
          case 351608024: /*version*/  return new Property("version", "string", "The version of the IG that is depended on, when the correct version is required to understand the IG correctly.", 0, 1, version);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116076: /*uri*/ return this.uri == null ? new Base[0] : new Base[] {this.uri}; // CanonicalType
        case 1802060801: /*packageId*/ return this.packageId == null ? new Base[0] : new Base[] {this.packageId}; // IdType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116076: // uri
          this.uri = castToCanonical(value); // CanonicalType
          return value;
        case 1802060801: // packageId
          this.packageId = castToId(value); // IdType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uri")) {
          this.uri = castToCanonical(value); // CanonicalType
        } else if (name.equals("packageId")) {
          this.packageId = castToId(value); // IdType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116076:  return getUriElement();
        case 1802060801:  return getPackageIdElement();
        case 351608024:  return getVersionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116076: /*uri*/ return new String[] {"canonical"};
        case 1802060801: /*packageId*/ return new String[] {"id"};
        case 351608024: /*version*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.uri");
        }
        else if (name.equals("packageId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.packageId");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.version");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDependsOnComponent copy() {
        ImplementationGuideDependsOnComponent dst = new ImplementationGuideDependsOnComponent();
        copyValues(dst);
        dst.uri = uri == null ? null : uri.copy();
        dst.packageId = packageId == null ? null : packageId.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDependsOnComponent))
          return false;
        ImplementationGuideDependsOnComponent o = (ImplementationGuideDependsOnComponent) other_;
        return compareDeep(uri, o.uri, true) && compareDeep(packageId, o.packageId, true) && compareDeep(version, o.version, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDependsOnComponent))
          return false;
        ImplementationGuideDependsOnComponent o = (ImplementationGuideDependsOnComponent) other_;
        return compareValues(packageId, o.packageId, true) && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(uri, packageId, version
          );
      }

  public String fhirType() {
    return "ImplementationGuide.dependsOn";

  }

  }

    @Block()
    public static class ImplementationGuideGlobalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of resource that all instances must conform to.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type this profile applies to", formalDefinition="The type of resource that all instances must conform to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType type;

        /**
         * A reference to the profile that all instances must conform to.
         */
        @Child(name = "profile", type = {CanonicalType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile that all resources must conform to", formalDefinition="A reference to the profile that all instances must conform to." )
        protected CanonicalType profile;

        private static final long serialVersionUID = 33894666L;

    /**
     * Constructor
     */
      public ImplementationGuideGlobalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideGlobalComponent(CodeType type, CanonicalType profile) {
        super();
        this.type = type;
        this.profile = profile;
      }

        /**
         * @return {@link #type} (The type of resource that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideGlobalComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of resource that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImplementationGuideGlobalComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of resource that all instances must conform to.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of resource that all instances must conform to.
         */
        public ImplementationGuideGlobalComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (A reference to the profile that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public CanonicalType getProfileElement() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideGlobalComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new CanonicalType(); // bb
          return this.profile;
        }

        public boolean hasProfileElement() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A reference to the profile that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public ImplementationGuideGlobalComponent setProfileElement(CanonicalType value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return A reference to the profile that all instances must conform to.
         */
        public String getProfile() { 
          return this.profile == null ? null : this.profile.getValue();
        }

        /**
         * @param value A reference to the profile that all instances must conform to.
         */
        public ImplementationGuideGlobalComponent setProfile(String value) { 
            if (this.profile == null)
              this.profile = new CanonicalType();
            this.profile.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "The type of resource that all instances must conform to.", 0, 1, type));
          children.add(new Property("profile", "canonical(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, 1, profile));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "The type of resource that all instances must conform to.", 0, 1, type);
          case -309425751: /*profile*/  return new Property("profile", "canonical(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, 1, profile);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // CanonicalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToCanonical(value); // CanonicalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToCanonical(value); // CanonicalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -309425751:  return getProfileElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"canonical"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.type");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.profile");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideGlobalComponent copy() {
        ImplementationGuideGlobalComponent dst = new ImplementationGuideGlobalComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideGlobalComponent))
          return false;
        ImplementationGuideGlobalComponent o = (ImplementationGuideGlobalComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideGlobalComponent))
          return false;
        ImplementationGuideGlobalComponent o = (ImplementationGuideGlobalComponent) other_;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile);
      }

  public String fhirType() {
    return "ImplementationGuide.global";

  }

  }

    @Block()
    public static class ImplementationGuideDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A logical group of resources. Logical groups can be used when building pages.
         */
        @Child(name = "package", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Grouping used to present related resources in the IG", formalDefinition="A logical group of resources. Logical groups can be used when building pages." )
        protected List<ImplementationGuideDefinitionPackageComponent> package_;

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.
         */
        @Child(name = "resource", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Resource in the implementation guide", formalDefinition="A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource." )
        protected List<ImplementationGuideDefinitionResourceComponent> resource;

        /**
         * A page / section in the implementation guide. The root page is the implementation guide home page.
         */
        @Child(name = "page", type = {}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Page/Section in the Guide", formalDefinition="A page / section in the implementation guide. The root page is the implementation guide home page." )
        protected ImplementationGuideDefinitionPageComponent page;

        /**
         * Defines how IG is built by tools.
         */
        @Child(name = "parameter", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Defines how IG is built by tools", formalDefinition="Defines how IG is built by tools." )
        protected List<ImplementationGuideDefinitionParameterComponent> parameter;

        /**
         * A template for building resources.
         */
        @Child(name = "template", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A template for building resources", formalDefinition="A template for building resources." )
        protected List<ImplementationGuideDefinitionTemplateComponent> template;

        private static final long serialVersionUID = 1395079915L;

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionComponent() {
        super();
      }

        /**
         * @return {@link #package_} (A logical group of resources. Logical groups can be used when building pages.)
         */
        public List<ImplementationGuideDefinitionPackageComponent> getPackage() { 
          if (this.package_ == null)
            this.package_ = new ArrayList<ImplementationGuideDefinitionPackageComponent>();
          return this.package_;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideDefinitionComponent setPackage(List<ImplementationGuideDefinitionPackageComponent> thePackage) { 
          this.package_ = thePackage;
          return this;
        }

        public boolean hasPackage() { 
          if (this.package_ == null)
            return false;
          for (ImplementationGuideDefinitionPackageComponent item : this.package_)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideDefinitionPackageComponent addPackage() { //3
          ImplementationGuideDefinitionPackageComponent t = new ImplementationGuideDefinitionPackageComponent();
          if (this.package_ == null)
            this.package_ = new ArrayList<ImplementationGuideDefinitionPackageComponent>();
          this.package_.add(t);
          return t;
        }

        public ImplementationGuideDefinitionComponent addPackage(ImplementationGuideDefinitionPackageComponent t) { //3
          if (t == null)
            return this;
          if (this.package_ == null)
            this.package_ = new ArrayList<ImplementationGuideDefinitionPackageComponent>();
          this.package_.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #package_}, creating it if it does not already exist
         */
        public ImplementationGuideDefinitionPackageComponent getPackageFirstRep() { 
          if (getPackage().isEmpty()) {
            addPackage();
          }
          return getPackage().get(0);
        }

        /**
         * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
         */
        public List<ImplementationGuideDefinitionResourceComponent> getResource() { 
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideDefinitionResourceComponent>();
          return this.resource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideDefinitionComponent setResource(List<ImplementationGuideDefinitionResourceComponent> theResource) { 
          this.resource = theResource;
          return this;
        }

        public boolean hasResource() { 
          if (this.resource == null)
            return false;
          for (ImplementationGuideDefinitionResourceComponent item : this.resource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideDefinitionResourceComponent addResource() { //3
          ImplementationGuideDefinitionResourceComponent t = new ImplementationGuideDefinitionResourceComponent();
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideDefinitionResourceComponent>();
          this.resource.add(t);
          return t;
        }

        public ImplementationGuideDefinitionComponent addResource(ImplementationGuideDefinitionResourceComponent t) { //3
          if (t == null)
            return this;
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideDefinitionResourceComponent>();
          this.resource.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
         */
        public ImplementationGuideDefinitionResourceComponent getResourceFirstRep() { 
          if (getResource().isEmpty()) {
            addResource();
          }
          return getResource().get(0);
        }

        /**
         * @return {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
         */
        public ImplementationGuideDefinitionPageComponent getPage() { 
          if (this.page == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionComponent.page");
            else if (Configuration.doAutoCreate())
              this.page = new ImplementationGuideDefinitionPageComponent(); // cc
          return this.page;
        }

        public boolean hasPage() { 
          return this.page != null && !this.page.isEmpty();
        }

        /**
         * @param value {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
         */
        public ImplementationGuideDefinitionComponent setPage(ImplementationGuideDefinitionPageComponent value) { 
          this.page = value;
          return this;
        }

        /**
         * @return {@link #parameter} (Defines how IG is built by tools.)
         */
        public List<ImplementationGuideDefinitionParameterComponent> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<ImplementationGuideDefinitionParameterComponent>();
          return this.parameter;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideDefinitionComponent setParameter(List<ImplementationGuideDefinitionParameterComponent> theParameter) { 
          this.parameter = theParameter;
          return this;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (ImplementationGuideDefinitionParameterComponent item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideDefinitionParameterComponent addParameter() { //3
          ImplementationGuideDefinitionParameterComponent t = new ImplementationGuideDefinitionParameterComponent();
          if (this.parameter == null)
            this.parameter = new ArrayList<ImplementationGuideDefinitionParameterComponent>();
          this.parameter.add(t);
          return t;
        }

        public ImplementationGuideDefinitionComponent addParameter(ImplementationGuideDefinitionParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.parameter == null)
            this.parameter = new ArrayList<ImplementationGuideDefinitionParameterComponent>();
          this.parameter.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #parameter}, creating it if it does not already exist
         */
        public ImplementationGuideDefinitionParameterComponent getParameterFirstRep() { 
          if (getParameter().isEmpty()) {
            addParameter();
          }
          return getParameter().get(0);
        }

        /**
         * @return {@link #template} (A template for building resources.)
         */
        public List<ImplementationGuideDefinitionTemplateComponent> getTemplate() { 
          if (this.template == null)
            this.template = new ArrayList<ImplementationGuideDefinitionTemplateComponent>();
          return this.template;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideDefinitionComponent setTemplate(List<ImplementationGuideDefinitionTemplateComponent> theTemplate) { 
          this.template = theTemplate;
          return this;
        }

        public boolean hasTemplate() { 
          if (this.template == null)
            return false;
          for (ImplementationGuideDefinitionTemplateComponent item : this.template)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideDefinitionTemplateComponent addTemplate() { //3
          ImplementationGuideDefinitionTemplateComponent t = new ImplementationGuideDefinitionTemplateComponent();
          if (this.template == null)
            this.template = new ArrayList<ImplementationGuideDefinitionTemplateComponent>();
          this.template.add(t);
          return t;
        }

        public ImplementationGuideDefinitionComponent addTemplate(ImplementationGuideDefinitionTemplateComponent t) { //3
          if (t == null)
            return this;
          if (this.template == null)
            this.template = new ArrayList<ImplementationGuideDefinitionTemplateComponent>();
          this.template.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #template}, creating it if it does not already exist
         */
        public ImplementationGuideDefinitionTemplateComponent getTemplateFirstRep() { 
          if (getTemplate().isEmpty()) {
            addTemplate();
          }
          return getTemplate().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_));
          children.add(new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource));
          children.add(new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, 1, page));
          children.add(new Property("parameter", "", "Defines how IG is built by tools.", 0, java.lang.Integer.MAX_VALUE, parameter));
          children.add(new Property("template", "", "A template for building resources.", 0, java.lang.Integer.MAX_VALUE, template));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -807062458: /*package*/  return new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_);
          case -341064690: /*resource*/  return new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource);
          case 3433103: /*page*/  return new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, 1, page);
          case 1954460585: /*parameter*/  return new Property("parameter", "", "Defines how IG is built by tools.", 0, java.lang.Integer.MAX_VALUE, parameter);
          case -1321546630: /*template*/  return new Property("template", "", "A template for building resources.", 0, java.lang.Integer.MAX_VALUE, template);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : this.package_.toArray(new Base[this.package_.size()]); // ImplementationGuideDefinitionPackageComponent
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // ImplementationGuideDefinitionResourceComponent
        case 3433103: /*page*/ return this.page == null ? new Base[0] : new Base[] {this.page}; // ImplementationGuideDefinitionPageComponent
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // ImplementationGuideDefinitionParameterComponent
        case -1321546630: /*template*/ return this.template == null ? new Base[0] : this.template.toArray(new Base[this.template.size()]); // ImplementationGuideDefinitionTemplateComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -807062458: // package
          this.getPackage().add((ImplementationGuideDefinitionPackageComponent) value); // ImplementationGuideDefinitionPackageComponent
          return value;
        case -341064690: // resource
          this.getResource().add((ImplementationGuideDefinitionResourceComponent) value); // ImplementationGuideDefinitionResourceComponent
          return value;
        case 3433103: // page
          this.page = (ImplementationGuideDefinitionPageComponent) value; // ImplementationGuideDefinitionPageComponent
          return value;
        case 1954460585: // parameter
          this.getParameter().add((ImplementationGuideDefinitionParameterComponent) value); // ImplementationGuideDefinitionParameterComponent
          return value;
        case -1321546630: // template
          this.getTemplate().add((ImplementationGuideDefinitionTemplateComponent) value); // ImplementationGuideDefinitionTemplateComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("package")) {
          this.getPackage().add((ImplementationGuideDefinitionPackageComponent) value);
        } else if (name.equals("resource")) {
          this.getResource().add((ImplementationGuideDefinitionResourceComponent) value);
        } else if (name.equals("page")) {
          this.page = (ImplementationGuideDefinitionPageComponent) value; // ImplementationGuideDefinitionPageComponent
        } else if (name.equals("parameter")) {
          this.getParameter().add((ImplementationGuideDefinitionParameterComponent) value);
        } else if (name.equals("template")) {
          this.getTemplate().add((ImplementationGuideDefinitionTemplateComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -807062458:  return addPackage(); 
        case -341064690:  return addResource(); 
        case 3433103:  return getPage(); 
        case 1954460585:  return addParameter(); 
        case -1321546630:  return addTemplate(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -807062458: /*package*/ return new String[] {};
        case -341064690: /*resource*/ return new String[] {};
        case 3433103: /*page*/ return new String[] {};
        case 1954460585: /*parameter*/ return new String[] {};
        case -1321546630: /*template*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("package")) {
          return addPackage();
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else if (name.equals("page")) {
          this.page = new ImplementationGuideDefinitionPageComponent();
          return this.page;
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else if (name.equals("template")) {
          return addTemplate();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDefinitionComponent copy() {
        ImplementationGuideDefinitionComponent dst = new ImplementationGuideDefinitionComponent();
        copyValues(dst);
        if (package_ != null) {
          dst.package_ = new ArrayList<ImplementationGuideDefinitionPackageComponent>();
          for (ImplementationGuideDefinitionPackageComponent i : package_)
            dst.package_.add(i.copy());
        };
        if (resource != null) {
          dst.resource = new ArrayList<ImplementationGuideDefinitionResourceComponent>();
          for (ImplementationGuideDefinitionResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        dst.page = page == null ? null : page.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<ImplementationGuideDefinitionParameterComponent>();
          for (ImplementationGuideDefinitionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        if (template != null) {
          dst.template = new ArrayList<ImplementationGuideDefinitionTemplateComponent>();
          for (ImplementationGuideDefinitionTemplateComponent i : template)
            dst.template.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionComponent))
          return false;
        ImplementationGuideDefinitionComponent o = (ImplementationGuideDefinitionComponent) other_;
        return compareDeep(package_, o.package_, true) && compareDeep(resource, o.resource, true) && compareDeep(page, o.page, true)
           && compareDeep(parameter, o.parameter, true) && compareDeep(template, o.template, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionComponent))
          return false;
        ImplementationGuideDefinitionComponent o = (ImplementationGuideDefinitionComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(package_, resource, page
          , parameter, template);
      }

  public String fhirType() {
    return "ImplementationGuide.definition";

  }

  }

    @Block()
    public static class ImplementationGuideDefinitionPackageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The human-readable title to display for the package of resources when rendering the implementation guide.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Descriptive name for the package", formalDefinition="The human-readable title to display for the package of resources when rendering the implementation guide." )
        protected StringType name;

        /**
         * Human readable text describing the package.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human readable text describing the package", formalDefinition="Human readable text describing the package." )
        protected StringType description;

        private static final long serialVersionUID = -1105523499L;

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionPackageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionPackageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The human-readable title to display for the package of resources when rendering the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionPackageComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The human-readable title to display for the package of resources when rendering the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideDefinitionPackageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The human-readable title to display for the package of resources when rendering the implementation guide.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The human-readable title to display for the package of resources when rendering the implementation guide.
         */
        public ImplementationGuideDefinitionPackageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Human readable text describing the package.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionPackageComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Human readable text describing the package.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuideDefinitionPackageComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human readable text describing the package.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human readable text describing the package.
         */
        public ImplementationGuideDefinitionPackageComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The human-readable title to display for the package of resources when rendering the implementation guide.", 0, 1, name));
          children.add(new Property("description", "string", "Human readable text describing the package.", 0, 1, description));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The human-readable title to display for the package of resources when rendering the implementation guide.", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "string", "Human readable text describing the package.", 0, 1, description);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.description");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDefinitionPackageComponent copy() {
        ImplementationGuideDefinitionPackageComponent dst = new ImplementationGuideDefinitionPackageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionPackageComponent))
          return false;
        ImplementationGuideDefinitionPackageComponent o = (ImplementationGuideDefinitionPackageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionPackageComponent))
          return false;
        ImplementationGuideDefinitionPackageComponent o = (ImplementationGuideDefinitionPackageComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, description);
      }

  public String fhirType() {
    return "ImplementationGuide.definition.package";

  }

  }

    @Block()
    public static class ImplementationGuideDefinitionResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Where this resource is found.
         */
        @Child(name = "reference", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Location of the resource", formalDefinition="Where this resource is found." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Where this resource is found.)
         */
        protected Resource referenceTarget;

        /**
         * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human Name for the resource", formalDefinition="A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name)." )
        protected StringType name;

        /**
         * A description of the reason that a resource has been included in the implementation guide.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason why included in guide", formalDefinition="A description of the reason that a resource has been included in the implementation guide." )
        protected StringType description;

        /**
         * If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.
         */
        @Child(name = "example", type = {BooleanType.class, CanonicalType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is an example/What is this an example of?", formalDefinition="If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile." )
        protected Type example;

        /**
         * Reference to the id of the pack this resource appears in.
         */
        @Child(name = "package", type = {IdType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Pack this is part of", formalDefinition="Reference to the id of the pack this resource appears in." )
        protected IdType package_;

        private static final long serialVersionUID = 1199251259L;

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionResourceComponent(Reference reference) {
        super();
        this.reference = reference;
      }

        /**
         * @return {@link #reference} (Where this resource is found.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionResourceComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Where this resource is found.)
         */
        public ImplementationGuideDefinitionResourceComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public ImplementationGuideDefinitionResourceComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionResourceComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideDefinitionResourceComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        public ImplementationGuideDefinitionResourceComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (A description of the reason that a resource has been included in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionResourceComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A description of the reason that a resource has been included in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuideDefinitionResourceComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of the reason that a resource has been included in the implementation guide.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of the reason that a resource has been included in the implementation guide.
         */
        public ImplementationGuideDefinitionResourceComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public Type getExample() { 
          return this.example;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public BooleanType getExampleBooleanType() throws FHIRException { 
          if (this.example == null)
            return null;
          if (!(this.example instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.example.getClass().getName()+" was encountered");
          return (BooleanType) this.example;
        }

        public boolean hasExampleBooleanType() { 
          return this != null && this.example instanceof BooleanType;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public CanonicalType getExampleCanonicalType() throws FHIRException { 
          if (this.example == null)
            return null;
          if (!(this.example instanceof CanonicalType))
            throw new FHIRException("Type mismatch: the type CanonicalType was expected, but "+this.example.getClass().getName()+" was encountered");
          return (CanonicalType) this.example;
        }

        public boolean hasExampleCanonicalType() { 
          return this != null && this.example instanceof CanonicalType;
        }

        public boolean hasExample() { 
          return this.example != null && !this.example.isEmpty();
        }

        /**
         * @param value {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public ImplementationGuideDefinitionResourceComponent setExample(Type value) { 
          if (value != null && !(value instanceof BooleanType || value instanceof CanonicalType))
            throw new Error("Not the right type for ImplementationGuide.definition.resource.example[x]: "+value.fhirType());
          this.example = value;
          return this;
        }

        /**
         * @return {@link #package_} (Reference to the id of the pack this resource appears in.). This is the underlying object with id, value and extensions. The accessor "getPackage" gives direct access to the value
         */
        public IdType getPackageElement() { 
          if (this.package_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionResourceComponent.package_");
            else if (Configuration.doAutoCreate())
              this.package_ = new IdType(); // bb
          return this.package_;
        }

        public boolean hasPackageElement() { 
          return this.package_ != null && !this.package_.isEmpty();
        }

        public boolean hasPackage() { 
          return this.package_ != null && !this.package_.isEmpty();
        }

        /**
         * @param value {@link #package_} (Reference to the id of the pack this resource appears in.). This is the underlying object with id, value and extensions. The accessor "getPackage" gives direct access to the value
         */
        public ImplementationGuideDefinitionResourceComponent setPackageElement(IdType value) { 
          this.package_ = value;
          return this;
        }

        /**
         * @return Reference to the id of the pack this resource appears in.
         */
        public String getPackage() { 
          return this.package_ == null ? null : this.package_.getValue();
        }

        /**
         * @param value Reference to the id of the pack this resource appears in.
         */
        public ImplementationGuideDefinitionResourceComponent setPackage(String value) { 
          if (Utilities.noString(value))
            this.package_ = null;
          else {
            if (this.package_ == null)
              this.package_ = new IdType();
            this.package_.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference));
          children.add(new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, 1, name));
          children.add(new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, 1, description));
          children.add(new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example));
          children.add(new Property("package", "id", "Reference to the id of the pack this resource appears in.", 0, 1, package_));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference);
          case 3373707: /*name*/  return new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, 1, description);
          case -2002328874: /*example[x]*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -1322970774: /*example*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 159803230: /*exampleBoolean*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 2016979626: /*exampleCanonical*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -807062458: /*package*/  return new Property("package", "id", "Reference to the id of the pack this resource appears in.", 0, 1, package_);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : new Base[] {this.example}; // Type
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : new Base[] {this.package_}; // IdType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1322970774: // example
          this.example = castToType(value); // Type
          return value;
        case -807062458: // package
          this.package_ = castToId(value); // IdType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("example[x]")) {
          this.example = castToType(value); // Type
        } else if (name.equals("package")) {
          this.package_ = castToId(value); // IdType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return getReference(); 
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case -2002328874:  return getExample(); 
        case -1322970774:  return getExample(); 
        case -807062458:  return getPackageElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1322970774: /*example*/ return new String[] {"boolean", "canonical"};
        case -807062458: /*package*/ return new String[] {"id"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.description");
        }
        else if (name.equals("exampleBoolean")) {
          this.example = new BooleanType();
          return this.example;
        }
        else if (name.equals("exampleCanonical")) {
          this.example = new CanonicalType();
          return this.example;
        }
        else if (name.equals("package")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.package");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDefinitionResourceComponent copy() {
        ImplementationGuideDefinitionResourceComponent dst = new ImplementationGuideDefinitionResourceComponent();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.example = example == null ? null : example.copy();
        dst.package_ = package_ == null ? null : package_.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionResourceComponent))
          return false;
        ImplementationGuideDefinitionResourceComponent o = (ImplementationGuideDefinitionResourceComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(example, o.example, true) && compareDeep(package_, o.package_, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionResourceComponent))
          return false;
        ImplementationGuideDefinitionResourceComponent o = (ImplementationGuideDefinitionResourceComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(package_, o.package_, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, name, description
          , example, package_);
      }

  public String fhirType() {
    return "ImplementationGuide.definition.resource";

  }

  }

    @Block()
    public static class ImplementationGuideDefinitionPageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The source address for the page.
         */
        @Child(name = "name", type = {UrlType.class, Binary.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where to find that page", formalDefinition="The source address for the page." )
        protected Type name;

        /**
         * A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short title shown for navigational assistance", formalDefinition="A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc." )
        protected StringType title;

        /**
         * A code that indicates how the page is generated.
         */
        @Child(name = "generation", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="html | markdown | xml | generated", formalDefinition="A code that indicates how the page is generated." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guide-page-generation")
        protected Enumeration<GuidePageGeneration> generation;

        /**
         * Nested Pages/Sections under this page.
         */
        @Child(name = "page", type = {ImplementationGuideDefinitionPageComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Pages / Sections", formalDefinition="Nested Pages/Sections under this page." )
        protected List<ImplementationGuideDefinitionPageComponent> page;

        private static final long serialVersionUID = -365655658L;

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionPageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionPageComponent(Type name, StringType title, Enumeration<GuidePageGeneration> generation) {
        super();
        this.name = name;
        this.title = title;
        this.generation = generation;
      }

        /**
         * @return {@link #name} (The source address for the page.)
         */
        public Type getName() { 
          return this.name;
        }

        /**
         * @return {@link #name} (The source address for the page.)
         */
        public UrlType getNameUrlType() throws FHIRException { 
          if (this.name == null)
            return null;
          if (!(this.name instanceof UrlType))
            throw new FHIRException("Type mismatch: the type UrlType was expected, but "+this.name.getClass().getName()+" was encountered");
          return (UrlType) this.name;
        }

        public boolean hasNameUrlType() { 
          return this != null && this.name instanceof UrlType;
        }

        /**
         * @return {@link #name} (The source address for the page.)
         */
        public Reference getNameReference() throws FHIRException { 
          if (this.name == null)
            return null;
          if (!(this.name instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.name.getClass().getName()+" was encountered");
          return (Reference) this.name;
        }

        public boolean hasNameReference() { 
          return this != null && this.name instanceof Reference;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The source address for the page.)
         */
        public ImplementationGuideDefinitionPageComponent setName(Type value) { 
          if (value != null && !(value instanceof UrlType || value instanceof Reference))
            throw new Error("Not the right type for ImplementationGuide.definition.page.name[x]: "+value.fhirType());
          this.name = value;
          return this;
        }

        /**
         * @return {@link #title} (A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionPageComponent.title");
            else if (Configuration.doAutoCreate())
              this.title = new StringType(); // bb
          return this.title;
        }

        public boolean hasTitleElement() { 
          return this.title != null && !this.title.isEmpty();
        }

        public boolean hasTitle() { 
          return this.title != null && !this.title.isEmpty();
        }

        /**
         * @param value {@link #title} (A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ImplementationGuideDefinitionPageComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public ImplementationGuideDefinitionPageComponent setTitle(String value) { 
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          return this;
        }

        /**
         * @return {@link #generation} (A code that indicates how the page is generated.). This is the underlying object with id, value and extensions. The accessor "getGeneration" gives direct access to the value
         */
        public Enumeration<GuidePageGeneration> getGenerationElement() { 
          if (this.generation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionPageComponent.generation");
            else if (Configuration.doAutoCreate())
              this.generation = new Enumeration<GuidePageGeneration>(new GuidePageGenerationEnumFactory()); // bb
          return this.generation;
        }

        public boolean hasGenerationElement() { 
          return this.generation != null && !this.generation.isEmpty();
        }

        public boolean hasGeneration() { 
          return this.generation != null && !this.generation.isEmpty();
        }

        /**
         * @param value {@link #generation} (A code that indicates how the page is generated.). This is the underlying object with id, value and extensions. The accessor "getGeneration" gives direct access to the value
         */
        public ImplementationGuideDefinitionPageComponent setGenerationElement(Enumeration<GuidePageGeneration> value) { 
          this.generation = value;
          return this;
        }

        /**
         * @return A code that indicates how the page is generated.
         */
        public GuidePageGeneration getGeneration() { 
          return this.generation == null ? null : this.generation.getValue();
        }

        /**
         * @param value A code that indicates how the page is generated.
         */
        public ImplementationGuideDefinitionPageComponent setGeneration(GuidePageGeneration value) { 
            if (this.generation == null)
              this.generation = new Enumeration<GuidePageGeneration>(new GuidePageGenerationEnumFactory());
            this.generation.setValue(value);
          return this;
        }

        /**
         * @return {@link #page} (Nested Pages/Sections under this page.)
         */
        public List<ImplementationGuideDefinitionPageComponent> getPage() { 
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideDefinitionPageComponent>();
          return this.page;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideDefinitionPageComponent setPage(List<ImplementationGuideDefinitionPageComponent> thePage) { 
          this.page = thePage;
          return this;
        }

        public boolean hasPage() { 
          if (this.page == null)
            return false;
          for (ImplementationGuideDefinitionPageComponent item : this.page)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideDefinitionPageComponent addPage() { //3
          ImplementationGuideDefinitionPageComponent t = new ImplementationGuideDefinitionPageComponent();
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideDefinitionPageComponent>();
          this.page.add(t);
          return t;
        }

        public ImplementationGuideDefinitionPageComponent addPage(ImplementationGuideDefinitionPageComponent t) { //3
          if (t == null)
            return this;
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideDefinitionPageComponent>();
          this.page.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #page}, creating it if it does not already exist
         */
        public ImplementationGuideDefinitionPageComponent getPageFirstRep() { 
          if (getPage().isEmpty()) {
            addPage();
          }
          return getPage().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name[x]", "url|Reference(Binary)", "The source address for the page.", 0, 1, name));
          children.add(new Property("title", "string", "A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, 1, title));
          children.add(new Property("generation", "code", "A code that indicates how the page is generated.", 0, 1, generation));
          children.add(new Property("page", "@ImplementationGuide.definition.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1721948693: /*name[x]*/  return new Property("name[x]", "url|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 3373707: /*name*/  return new Property("name[x]", "url|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 1721942756: /*nameUrl*/  return new Property("name[x]", "url|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 1833144576: /*nameReference*/  return new Property("name[x]", "url|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 110371416: /*title*/  return new Property("title", "string", "A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, 1, title);
          case 305703192: /*generation*/  return new Property("generation", "code", "A code that indicates how the page is generated.", 0, 1, generation);
          case 3433103: /*page*/  return new Property("page", "@ImplementationGuide.definition.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // Type
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 305703192: /*generation*/ return this.generation == null ? new Base[0] : new Base[] {this.generation}; // Enumeration<GuidePageGeneration>
        case 3433103: /*page*/ return this.page == null ? new Base[0] : this.page.toArray(new Base[this.page.size()]); // ImplementationGuideDefinitionPageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToType(value); // Type
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 305703192: // generation
          value = new GuidePageGenerationEnumFactory().fromType(castToCode(value));
          this.generation = (Enumeration) value; // Enumeration<GuidePageGeneration>
          return value;
        case 3433103: // page
          this.getPage().add((ImplementationGuideDefinitionPageComponent) value); // ImplementationGuideDefinitionPageComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name[x]")) {
          this.name = castToType(value); // Type
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("generation")) {
          value = new GuidePageGenerationEnumFactory().fromType(castToCode(value));
          this.generation = (Enumeration) value; // Enumeration<GuidePageGeneration>
        } else if (name.equals("page")) {
          this.getPage().add((ImplementationGuideDefinitionPageComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1721948693:  return getName(); 
        case 3373707:  return getName(); 
        case 110371416:  return getTitleElement();
        case 305703192:  return getGenerationElement();
        case 3433103:  return addPage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"url", "Reference"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 305703192: /*generation*/ return new String[] {"code"};
        case 3433103: /*page*/ return new String[] {"@ImplementationGuide.definition.page"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("nameUrl")) {
          this.name = new UrlType();
          return this.name;
        }
        else if (name.equals("nameReference")) {
          this.name = new Reference();
          return this.name;
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.title");
        }
        else if (name.equals("generation")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.generation");
        }
        else if (name.equals("page")) {
          return addPage();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDefinitionPageComponent copy() {
        ImplementationGuideDefinitionPageComponent dst = new ImplementationGuideDefinitionPageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.generation = generation == null ? null : generation.copy();
        if (page != null) {
          dst.page = new ArrayList<ImplementationGuideDefinitionPageComponent>();
          for (ImplementationGuideDefinitionPageComponent i : page)
            dst.page.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionPageComponent))
          return false;
        ImplementationGuideDefinitionPageComponent o = (ImplementationGuideDefinitionPageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(generation, o.generation, true)
           && compareDeep(page, o.page, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionPageComponent))
          return false;
        ImplementationGuideDefinitionPageComponent o = (ImplementationGuideDefinitionPageComponent) other_;
        return compareValues(title, o.title, true) && compareValues(generation, o.generation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, title, generation
          , page);
      }

  public String fhirType() {
    return "ImplementationGuide.definition.page";

  }

  }

    @Block()
    public static class ImplementationGuideDefinitionParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template", formalDefinition="apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guide-parameter-code")
        protected Enumeration<GuideParameterCode> code;

        /**
         * Value for named type.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value for named type", formalDefinition="Value for named type." )
        protected StringType value;

        private static final long serialVersionUID = 1188999138L;

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionParameterComponent(Enumeration<GuideParameterCode> code, StringType value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<GuideParameterCode> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionParameterComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<GuideParameterCode>(new GuideParameterCodeEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ImplementationGuideDefinitionParameterComponent setCodeElement(Enumeration<GuideParameterCode> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.
         */
        public GuideParameterCode getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.
         */
        public ImplementationGuideDefinitionParameterComponent setCode(GuideParameterCode value) { 
            if (this.code == null)
              this.code = new Enumeration<GuideParameterCode>(new GuideParameterCodeEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (Value for named type.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionParameterComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Value for named type.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ImplementationGuideDefinitionParameterComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return Value for named type.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value Value for named type.
         */
        public ImplementationGuideDefinitionParameterComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "code", "apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.", 0, 1, code));
          children.add(new Property("value", "string", "Value for named type.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "code", "apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.", 0, 1, code);
          case 111972721: /*value*/  return new Property("value", "string", "Value for named type.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<GuideParameterCode>
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new GuideParameterCodeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<GuideParameterCode>
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new GuideParameterCodeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<GuideParameterCode>
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.code");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.value");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDefinitionParameterComponent copy() {
        ImplementationGuideDefinitionParameterComponent dst = new ImplementationGuideDefinitionParameterComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionParameterComponent))
          return false;
        ImplementationGuideDefinitionParameterComponent o = (ImplementationGuideDefinitionParameterComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionParameterComponent))
          return false;
        ImplementationGuideDefinitionParameterComponent o = (ImplementationGuideDefinitionParameterComponent) other_;
        return compareValues(code, o.code, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, value);
      }

  public String fhirType() {
    return "ImplementationGuide.definition.parameter";

  }

  }

    @Block()
    public static class ImplementationGuideDefinitionTemplateComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of template specified.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of template specified", formalDefinition="Type of template specified." )
        protected CodeType code;

        /**
         * The source location for the template.
         */
        @Child(name = "source", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The source location for the template", formalDefinition="The source location for the template." )
        protected StringType source;

        /**
         * The scope in which the template applies.
         */
        @Child(name = "scope", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The scope in which the template applies", formalDefinition="The scope in which the template applies." )
        protected StringType scope;

        private static final long serialVersionUID = 923832457L;

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionTemplateComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDefinitionTemplateComponent(CodeType code, StringType source) {
        super();
        this.code = code;
        this.source = source;
      }

        /**
         * @return {@link #code} (Type of template specified.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionTemplateComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Type of template specified.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ImplementationGuideDefinitionTemplateComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Type of template specified.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Type of template specified.
         */
        public ImplementationGuideDefinitionTemplateComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The source location for the template.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public StringType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionTemplateComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new StringType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The source location for the template.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public ImplementationGuideDefinitionTemplateComponent setSourceElement(StringType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The source location for the template.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The source location for the template.
         */
        public ImplementationGuideDefinitionTemplateComponent setSource(String value) { 
            if (this.source == null)
              this.source = new StringType();
            this.source.setValue(value);
          return this;
        }

        /**
         * @return {@link #scope} (The scope in which the template applies.). This is the underlying object with id, value and extensions. The accessor "getScope" gives direct access to the value
         */
        public StringType getScopeElement() { 
          if (this.scope == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefinitionTemplateComponent.scope");
            else if (Configuration.doAutoCreate())
              this.scope = new StringType(); // bb
          return this.scope;
        }

        public boolean hasScopeElement() { 
          return this.scope != null && !this.scope.isEmpty();
        }

        public boolean hasScope() { 
          return this.scope != null && !this.scope.isEmpty();
        }

        /**
         * @param value {@link #scope} (The scope in which the template applies.). This is the underlying object with id, value and extensions. The accessor "getScope" gives direct access to the value
         */
        public ImplementationGuideDefinitionTemplateComponent setScopeElement(StringType value) { 
          this.scope = value;
          return this;
        }

        /**
         * @return The scope in which the template applies.
         */
        public String getScope() { 
          return this.scope == null ? null : this.scope.getValue();
        }

        /**
         * @param value The scope in which the template applies.
         */
        public ImplementationGuideDefinitionTemplateComponent setScope(String value) { 
          if (Utilities.noString(value))
            this.scope = null;
          else {
            if (this.scope == null)
              this.scope = new StringType();
            this.scope.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "code", "Type of template specified.", 0, 1, code));
          children.add(new Property("source", "string", "The source location for the template.", 0, 1, source));
          children.add(new Property("scope", "string", "The scope in which the template applies.", 0, 1, scope));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "code", "Type of template specified.", 0, 1, code);
          case -896505829: /*source*/  return new Property("source", "string", "The source location for the template.", 0, 1, source);
          case 109264468: /*scope*/  return new Property("scope", "string", "The scope in which the template applies.", 0, 1, scope);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // StringType
        case 109264468: /*scope*/ return this.scope == null ? new Base[0] : new Base[] {this.scope}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          return value;
        case -896505829: // source
          this.source = castToString(value); // StringType
          return value;
        case 109264468: // scope
          this.scope = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCode(value); // CodeType
        } else if (name.equals("source")) {
          this.source = castToString(value); // StringType
        } else if (name.equals("scope")) {
          this.scope = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case -896505829:  return getSourceElement();
        case 109264468:  return getScopeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case -896505829: /*source*/ return new String[] {"string"};
        case 109264468: /*scope*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.code");
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.source");
        }
        else if (name.equals("scope")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.scope");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDefinitionTemplateComponent copy() {
        ImplementationGuideDefinitionTemplateComponent dst = new ImplementationGuideDefinitionTemplateComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.source = source == null ? null : source.copy();
        dst.scope = scope == null ? null : scope.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionTemplateComponent))
          return false;
        ImplementationGuideDefinitionTemplateComponent o = (ImplementationGuideDefinitionTemplateComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(source, o.source, true) && compareDeep(scope, o.scope, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideDefinitionTemplateComponent))
          return false;
        ImplementationGuideDefinitionTemplateComponent o = (ImplementationGuideDefinitionTemplateComponent) other_;
        return compareValues(code, o.code, true) && compareValues(source, o.source, true) && compareValues(scope, o.scope, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, source, scope);
      }

  public String fhirType() {
    return "ImplementationGuide.definition.template";

  }

  }

    @Block()
    public static class ImplementationGuideManifestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A pointer to official web page, PDF or other rendering of the implementation guide.
         */
        @Child(name = "rendering", type = {UrlType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Location of rendered implementation guide", formalDefinition="A pointer to official web page, PDF or other rendering of the implementation guide." )
        protected UrlType rendering;

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.
         */
        @Child(name = "resource", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Resource in the implementation guide", formalDefinition="A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource." )
        protected List<ManifestResourceComponent> resource;

        /**
         * Information about a page within the IG.
         */
        @Child(name = "page", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="HTML page within the parent IG", formalDefinition="Information about a page within the IG." )
        protected List<ManifestPageComponent> page;

        /**
         * Indicates a relative path to an image that exists within the IG.
         */
        @Child(name = "image", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Image within the IG", formalDefinition="Indicates a relative path to an image that exists within the IG." )
        protected List<StringType> image;

        /**
         * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.
         */
        @Child(name = "other", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional linkable file in IG", formalDefinition="Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG." )
        protected List<StringType> other;

        private static final long serialVersionUID = 1881327712L;

    /**
     * Constructor
     */
      public ImplementationGuideManifestComponent() {
        super();
      }

        /**
         * @return {@link #rendering} (A pointer to official web page, PDF or other rendering of the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getRendering" gives direct access to the value
         */
        public UrlType getRenderingElement() { 
          if (this.rendering == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideManifestComponent.rendering");
            else if (Configuration.doAutoCreate())
              this.rendering = new UrlType(); // bb
          return this.rendering;
        }

        public boolean hasRenderingElement() { 
          return this.rendering != null && !this.rendering.isEmpty();
        }

        public boolean hasRendering() { 
          return this.rendering != null && !this.rendering.isEmpty();
        }

        /**
         * @param value {@link #rendering} (A pointer to official web page, PDF or other rendering of the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getRendering" gives direct access to the value
         */
        public ImplementationGuideManifestComponent setRenderingElement(UrlType value) { 
          this.rendering = value;
          return this;
        }

        /**
         * @return A pointer to official web page, PDF or other rendering of the implementation guide.
         */
        public String getRendering() { 
          return this.rendering == null ? null : this.rendering.getValue();
        }

        /**
         * @param value A pointer to official web page, PDF or other rendering of the implementation guide.
         */
        public ImplementationGuideManifestComponent setRendering(String value) { 
          if (Utilities.noString(value))
            this.rendering = null;
          else {
            if (this.rendering == null)
              this.rendering = new UrlType();
            this.rendering.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
         */
        public List<ManifestResourceComponent> getResource() { 
          if (this.resource == null)
            this.resource = new ArrayList<ManifestResourceComponent>();
          return this.resource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideManifestComponent setResource(List<ManifestResourceComponent> theResource) { 
          this.resource = theResource;
          return this;
        }

        public boolean hasResource() { 
          if (this.resource == null)
            return false;
          for (ManifestResourceComponent item : this.resource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ManifestResourceComponent addResource() { //3
          ManifestResourceComponent t = new ManifestResourceComponent();
          if (this.resource == null)
            this.resource = new ArrayList<ManifestResourceComponent>();
          this.resource.add(t);
          return t;
        }

        public ImplementationGuideManifestComponent addResource(ManifestResourceComponent t) { //3
          if (t == null)
            return this;
          if (this.resource == null)
            this.resource = new ArrayList<ManifestResourceComponent>();
          this.resource.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
         */
        public ManifestResourceComponent getResourceFirstRep() { 
          if (getResource().isEmpty()) {
            addResource();
          }
          return getResource().get(0);
        }

        /**
         * @return {@link #page} (Information about a page within the IG.)
         */
        public List<ManifestPageComponent> getPage() { 
          if (this.page == null)
            this.page = new ArrayList<ManifestPageComponent>();
          return this.page;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideManifestComponent setPage(List<ManifestPageComponent> thePage) { 
          this.page = thePage;
          return this;
        }

        public boolean hasPage() { 
          if (this.page == null)
            return false;
          for (ManifestPageComponent item : this.page)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ManifestPageComponent addPage() { //3
          ManifestPageComponent t = new ManifestPageComponent();
          if (this.page == null)
            this.page = new ArrayList<ManifestPageComponent>();
          this.page.add(t);
          return t;
        }

        public ImplementationGuideManifestComponent addPage(ManifestPageComponent t) { //3
          if (t == null)
            return this;
          if (this.page == null)
            this.page = new ArrayList<ManifestPageComponent>();
          this.page.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #page}, creating it if it does not already exist
         */
        public ManifestPageComponent getPageFirstRep() { 
          if (getPage().isEmpty()) {
            addPage();
          }
          return getPage().get(0);
        }

        /**
         * @return {@link #image} (Indicates a relative path to an image that exists within the IG.)
         */
        public List<StringType> getImage() { 
          if (this.image == null)
            this.image = new ArrayList<StringType>();
          return this.image;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideManifestComponent setImage(List<StringType> theImage) { 
          this.image = theImage;
          return this;
        }

        public boolean hasImage() { 
          if (this.image == null)
            return false;
          for (StringType item : this.image)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #image} (Indicates a relative path to an image that exists within the IG.)
         */
        public StringType addImageElement() {//2 
          StringType t = new StringType();
          if (this.image == null)
            this.image = new ArrayList<StringType>();
          this.image.add(t);
          return t;
        }

        /**
         * @param value {@link #image} (Indicates a relative path to an image that exists within the IG.)
         */
        public ImplementationGuideManifestComponent addImage(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.image == null)
            this.image = new ArrayList<StringType>();
          this.image.add(t);
          return this;
        }

        /**
         * @param value {@link #image} (Indicates a relative path to an image that exists within the IG.)
         */
        public boolean hasImage(String value) { 
          if (this.image == null)
            return false;
          for (StringType v : this.image)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
         */
        public List<StringType> getOther() { 
          if (this.other == null)
            this.other = new ArrayList<StringType>();
          return this.other;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideManifestComponent setOther(List<StringType> theOther) { 
          this.other = theOther;
          return this;
        }

        public boolean hasOther() { 
          if (this.other == null)
            return false;
          for (StringType item : this.other)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
         */
        public StringType addOtherElement() {//2 
          StringType t = new StringType();
          if (this.other == null)
            this.other = new ArrayList<StringType>();
          this.other.add(t);
          return t;
        }

        /**
         * @param value {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
         */
        public ImplementationGuideManifestComponent addOther(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.other == null)
            this.other = new ArrayList<StringType>();
          this.other.add(t);
          return this;
        }

        /**
         * @param value {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
         */
        public boolean hasOther(String value) { 
          if (this.other == null)
            return false;
          for (StringType v : this.other)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("rendering", "url", "A pointer to official web page, PDF or other rendering of the implementation guide.", 0, 1, rendering));
          children.add(new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource));
          children.add(new Property("page", "", "Information about a page within the IG.", 0, java.lang.Integer.MAX_VALUE, page));
          children.add(new Property("image", "string", "Indicates a relative path to an image that exists within the IG.", 0, java.lang.Integer.MAX_VALUE, image));
          children.add(new Property("other", "string", "Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.", 0, java.lang.Integer.MAX_VALUE, other));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1839654540: /*rendering*/  return new Property("rendering", "url", "A pointer to official web page, PDF or other rendering of the implementation guide.", 0, 1, rendering);
          case -341064690: /*resource*/  return new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource);
          case 3433103: /*page*/  return new Property("page", "", "Information about a page within the IG.", 0, java.lang.Integer.MAX_VALUE, page);
          case 100313435: /*image*/  return new Property("image", "string", "Indicates a relative path to an image that exists within the IG.", 0, java.lang.Integer.MAX_VALUE, image);
          case 106069776: /*other*/  return new Property("other", "string", "Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.", 0, java.lang.Integer.MAX_VALUE, other);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1839654540: /*rendering*/ return this.rendering == null ? new Base[0] : new Base[] {this.rendering}; // UrlType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // ManifestResourceComponent
        case 3433103: /*page*/ return this.page == null ? new Base[0] : this.page.toArray(new Base[this.page.size()]); // ManifestPageComponent
        case 100313435: /*image*/ return this.image == null ? new Base[0] : this.image.toArray(new Base[this.image.size()]); // StringType
        case 106069776: /*other*/ return this.other == null ? new Base[0] : this.other.toArray(new Base[this.other.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1839654540: // rendering
          this.rendering = castToUrl(value); // UrlType
          return value;
        case -341064690: // resource
          this.getResource().add((ManifestResourceComponent) value); // ManifestResourceComponent
          return value;
        case 3433103: // page
          this.getPage().add((ManifestPageComponent) value); // ManifestPageComponent
          return value;
        case 100313435: // image
          this.getImage().add(castToString(value)); // StringType
          return value;
        case 106069776: // other
          this.getOther().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("rendering")) {
          this.rendering = castToUrl(value); // UrlType
        } else if (name.equals("resource")) {
          this.getResource().add((ManifestResourceComponent) value);
        } else if (name.equals("page")) {
          this.getPage().add((ManifestPageComponent) value);
        } else if (name.equals("image")) {
          this.getImage().add(castToString(value));
        } else if (name.equals("other")) {
          this.getOther().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1839654540:  return getRenderingElement();
        case -341064690:  return addResource(); 
        case 3433103:  return addPage(); 
        case 100313435:  return addImageElement();
        case 106069776:  return addOtherElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1839654540: /*rendering*/ return new String[] {"url"};
        case -341064690: /*resource*/ return new String[] {};
        case 3433103: /*page*/ return new String[] {};
        case 100313435: /*image*/ return new String[] {"string"};
        case 106069776: /*other*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("rendering")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.rendering");
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else if (name.equals("page")) {
          return addPage();
        }
        else if (name.equals("image")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.image");
        }
        else if (name.equals("other")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.other");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideManifestComponent copy() {
        ImplementationGuideManifestComponent dst = new ImplementationGuideManifestComponent();
        copyValues(dst);
        dst.rendering = rendering == null ? null : rendering.copy();
        if (resource != null) {
          dst.resource = new ArrayList<ManifestResourceComponent>();
          for (ManifestResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        if (page != null) {
          dst.page = new ArrayList<ManifestPageComponent>();
          for (ManifestPageComponent i : page)
            dst.page.add(i.copy());
        };
        if (image != null) {
          dst.image = new ArrayList<StringType>();
          for (StringType i : image)
            dst.image.add(i.copy());
        };
        if (other != null) {
          dst.other = new ArrayList<StringType>();
          for (StringType i : other)
            dst.other.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideManifestComponent))
          return false;
        ImplementationGuideManifestComponent o = (ImplementationGuideManifestComponent) other_;
        return compareDeep(rendering, o.rendering, true) && compareDeep(resource, o.resource, true) && compareDeep(page, o.page, true)
           && compareDeep(image, o.image, true) && compareDeep(other, o.other, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideManifestComponent))
          return false;
        ImplementationGuideManifestComponent o = (ImplementationGuideManifestComponent) other_;
        return compareValues(rendering, o.rendering, true) && compareValues(image, o.image, true) && compareValues(other, o.other, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(rendering, resource, page
          , image, other);
      }

  public String fhirType() {
    return "ImplementationGuide.manifest";

  }

  }

    @Block()
    public static class ManifestResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Where this resource is found.
         */
        @Child(name = "reference", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Location of the resource", formalDefinition="Where this resource is found." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Where this resource is found.)
         */
        protected Resource referenceTarget;

        /**
         * If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.
         */
        @Child(name = "example", type = {BooleanType.class, CanonicalType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is an example/What is this an example of?", formalDefinition="If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile." )
        protected Type example;

        /**
         * The relative path for primary page for this resource within the IG.
         */
        @Child(name = "relativePath", type = {UrlType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Relative path for page in IG", formalDefinition="The relative path for primary page for this resource within the IG." )
        protected UrlType relativePath;

        private static final long serialVersionUID = 1150095716L;

    /**
     * Constructor
     */
      public ManifestResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ManifestResourceComponent(Reference reference) {
        super();
        this.reference = reference;
      }

        /**
         * @return {@link #reference} (Where this resource is found.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ManifestResourceComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Where this resource is found.)
         */
        public ManifestResourceComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public ManifestResourceComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public Type getExample() { 
          return this.example;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public BooleanType getExampleBooleanType() throws FHIRException { 
          if (this.example == null)
            return null;
          if (!(this.example instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.example.getClass().getName()+" was encountered");
          return (BooleanType) this.example;
        }

        public boolean hasExampleBooleanType() { 
          return this != null && this.example instanceof BooleanType;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public CanonicalType getExampleCanonicalType() throws FHIRException { 
          if (this.example == null)
            return null;
          if (!(this.example instanceof CanonicalType))
            throw new FHIRException("Type mismatch: the type CanonicalType was expected, but "+this.example.getClass().getName()+" was encountered");
          return (CanonicalType) this.example;
        }

        public boolean hasExampleCanonicalType() { 
          return this != null && this.example instanceof CanonicalType;
        }

        public boolean hasExample() { 
          return this.example != null && !this.example.isEmpty();
        }

        /**
         * @param value {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public ManifestResourceComponent setExample(Type value) { 
          if (value != null && !(value instanceof BooleanType || value instanceof CanonicalType))
            throw new Error("Not the right type for ImplementationGuide.manifest.resource.example[x]: "+value.fhirType());
          this.example = value;
          return this;
        }

        /**
         * @return {@link #relativePath} (The relative path for primary page for this resource within the IG.). This is the underlying object with id, value and extensions. The accessor "getRelativePath" gives direct access to the value
         */
        public UrlType getRelativePathElement() { 
          if (this.relativePath == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ManifestResourceComponent.relativePath");
            else if (Configuration.doAutoCreate())
              this.relativePath = new UrlType(); // bb
          return this.relativePath;
        }

        public boolean hasRelativePathElement() { 
          return this.relativePath != null && !this.relativePath.isEmpty();
        }

        public boolean hasRelativePath() { 
          return this.relativePath != null && !this.relativePath.isEmpty();
        }

        /**
         * @param value {@link #relativePath} (The relative path for primary page for this resource within the IG.). This is the underlying object with id, value and extensions. The accessor "getRelativePath" gives direct access to the value
         */
        public ManifestResourceComponent setRelativePathElement(UrlType value) { 
          this.relativePath = value;
          return this;
        }

        /**
         * @return The relative path for primary page for this resource within the IG.
         */
        public String getRelativePath() { 
          return this.relativePath == null ? null : this.relativePath.getValue();
        }

        /**
         * @param value The relative path for primary page for this resource within the IG.
         */
        public ManifestResourceComponent setRelativePath(String value) { 
          if (Utilities.noString(value))
            this.relativePath = null;
          else {
            if (this.relativePath == null)
              this.relativePath = new UrlType();
            this.relativePath.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference));
          children.add(new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example));
          children.add(new Property("relativePath", "url", "The relative path for primary page for this resource within the IG.", 0, 1, relativePath));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference);
          case -2002328874: /*example[x]*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -1322970774: /*example*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 159803230: /*exampleBoolean*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 2016979626: /*exampleCanonical*/  return new Property("example[x]", "boolean|canonical(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -70808303: /*relativePath*/  return new Property("relativePath", "url", "The relative path for primary page for this resource within the IG.", 0, 1, relativePath);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : new Base[] {this.example}; // Type
        case -70808303: /*relativePath*/ return this.relativePath == null ? new Base[0] : new Base[] {this.relativePath}; // UrlType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case -1322970774: // example
          this.example = castToType(value); // Type
          return value;
        case -70808303: // relativePath
          this.relativePath = castToUrl(value); // UrlType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("example[x]")) {
          this.example = castToType(value); // Type
        } else if (name.equals("relativePath")) {
          this.relativePath = castToUrl(value); // UrlType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return getReference(); 
        case -2002328874:  return getExample(); 
        case -1322970774:  return getExample(); 
        case -70808303:  return getRelativePathElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case -1322970774: /*example*/ return new String[] {"boolean", "canonical"};
        case -70808303: /*relativePath*/ return new String[] {"url"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("exampleBoolean")) {
          this.example = new BooleanType();
          return this.example;
        }
        else if (name.equals("exampleCanonical")) {
          this.example = new CanonicalType();
          return this.example;
        }
        else if (name.equals("relativePath")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.relativePath");
        }
        else
          return super.addChild(name);
      }

      public ManifestResourceComponent copy() {
        ManifestResourceComponent dst = new ManifestResourceComponent();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        dst.example = example == null ? null : example.copy();
        dst.relativePath = relativePath == null ? null : relativePath.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ManifestResourceComponent))
          return false;
        ManifestResourceComponent o = (ManifestResourceComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(example, o.example, true) && compareDeep(relativePath, o.relativePath, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ManifestResourceComponent))
          return false;
        ManifestResourceComponent o = (ManifestResourceComponent) other_;
        return compareValues(relativePath, o.relativePath, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, example, relativePath
          );
      }

  public String fhirType() {
    return "ImplementationGuide.manifest.resource";

  }

  }

    @Block()
    public static class ManifestPageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Relative path to the page.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTML page name", formalDefinition="Relative path to the page." )
        protected StringType name;

        /**
         * Label for the page intended for human display.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Title of the page, for references", formalDefinition="Label for the page intended for human display." )
        protected StringType title;

        /**
         * The name of an anchor available on the page.
         */
        @Child(name = "anchor", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Anchor available on the page", formalDefinition="The name of an anchor available on the page." )
        protected List<StringType> anchor;

        private static final long serialVersionUID = 1920576611L;

    /**
     * Constructor
     */
      public ManifestPageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ManifestPageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Relative path to the page.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ManifestPageComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Relative path to the page.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ManifestPageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Relative path to the page.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Relative path to the page.
         */
        public ManifestPageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #title} (Label for the page intended for human display.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ManifestPageComponent.title");
            else if (Configuration.doAutoCreate())
              this.title = new StringType(); // bb
          return this.title;
        }

        public boolean hasTitleElement() { 
          return this.title != null && !this.title.isEmpty();
        }

        public boolean hasTitle() { 
          return this.title != null && !this.title.isEmpty();
        }

        /**
         * @param value {@link #title} (Label for the page intended for human display.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ManifestPageComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return Label for the page intended for human display.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value Label for the page intended for human display.
         */
        public ManifestPageComponent setTitle(String value) { 
          if (Utilities.noString(value))
            this.title = null;
          else {
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #anchor} (The name of an anchor available on the page.)
         */
        public List<StringType> getAnchor() { 
          if (this.anchor == null)
            this.anchor = new ArrayList<StringType>();
          return this.anchor;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ManifestPageComponent setAnchor(List<StringType> theAnchor) { 
          this.anchor = theAnchor;
          return this;
        }

        public boolean hasAnchor() { 
          if (this.anchor == null)
            return false;
          for (StringType item : this.anchor)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #anchor} (The name of an anchor available on the page.)
         */
        public StringType addAnchorElement() {//2 
          StringType t = new StringType();
          if (this.anchor == null)
            this.anchor = new ArrayList<StringType>();
          this.anchor.add(t);
          return t;
        }

        /**
         * @param value {@link #anchor} (The name of an anchor available on the page.)
         */
        public ManifestPageComponent addAnchor(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.anchor == null)
            this.anchor = new ArrayList<StringType>();
          this.anchor.add(t);
          return this;
        }

        /**
         * @param value {@link #anchor} (The name of an anchor available on the page.)
         */
        public boolean hasAnchor(String value) { 
          if (this.anchor == null)
            return false;
          for (StringType v : this.anchor)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "Relative path to the page.", 0, 1, name));
          children.add(new Property("title", "string", "Label for the page intended for human display.", 0, 1, title));
          children.add(new Property("anchor", "string", "The name of an anchor available on the page.", 0, java.lang.Integer.MAX_VALUE, anchor));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "Relative path to the page.", 0, 1, name);
          case 110371416: /*title*/  return new Property("title", "string", "Label for the page intended for human display.", 0, 1, title);
          case -1413299531: /*anchor*/  return new Property("anchor", "string", "The name of an anchor available on the page.", 0, java.lang.Integer.MAX_VALUE, anchor);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1413299531: /*anchor*/ return this.anchor == null ? new Base[0] : this.anchor.toArray(new Base[this.anchor.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1413299531: // anchor
          this.getAnchor().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("anchor")) {
          this.getAnchor().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -1413299531:  return addAnchorElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -1413299531: /*anchor*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.title");
        }
        else if (name.equals("anchor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.anchor");
        }
        else
          return super.addChild(name);
      }

      public ManifestPageComponent copy() {
        ManifestPageComponent dst = new ManifestPageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        if (anchor != null) {
          dst.anchor = new ArrayList<StringType>();
          for (StringType i : anchor)
            dst.anchor.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ManifestPageComponent))
          return false;
        ManifestPageComponent o = (ManifestPageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(anchor, o.anchor, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ManifestPageComponent))
          return false;
        ManifestPageComponent o = (ManifestPageComponent) other_;
        return compareValues(name, o.name, true) && compareValues(title, o.title, true) && compareValues(anchor, o.anchor, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, title, anchor);
      }

  public String fhirType() {
    return "ImplementationGuide.manifest.page";

  }

  }

    /**
     * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide." )
    protected MarkdownType copyright;

    /**
     * The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.
     */
    @Child(name = "packageId", type = {IdType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="NPM Package name for IG", formalDefinition="The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care." )
    protected IdType packageId;

    /**
     * The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
     */
    @Child(name = "license", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="SPDX license code for this IG (or not-open-source)", formalDefinition="The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/spdx-license")
    protected Enumeration<SPDXLicense> license;

    /**
     * The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="FHIR Version this Implementation Guide targets", formalDefinition="The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fhir-versions")
    protected IdType fhirVersion;

    /**
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.
     */
    @Child(name = "dependsOn", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Another Implementation guide this depends on", formalDefinition="Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides." )
    protected List<ImplementationGuideDependsOnComponent> dependsOn;

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     */
    @Child(name = "global", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles that apply globally", formalDefinition="A set of profiles that all resources covered by this implementation guide must conform to." )
    protected List<ImplementationGuideGlobalComponent> global;

    /**
     * The information needed by an IG publisher tool to publish the whole implementation guide.
     */
    @Child(name = "definition", type = {}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Information needed to build the IG", formalDefinition="The information needed by an IG publisher tool to publish the whole implementation guide." )
    protected ImplementationGuideDefinitionComponent definition;

    /**
     * Information about an assembled implementation guide, created by the publication tooling.
     */
    @Child(name = "manifest", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Information about an assembled IG", formalDefinition="Information about an assembled implementation guide, created by the publication tooling." )
    protected ImplementationGuideManifestComponent manifest;

    private static final long serialVersionUID = 1764764818L;

  /**
   * Constructor
   */
    public ImplementationGuide() {
      super();
    }

  /**
   * Constructor
   */
    public ImplementationGuide(UriType url, StringType name, Enumeration<PublicationStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImplementationGuide setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on different servers.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on different servers.
     */
    public ImplementationGuide setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the implementation guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the implementation guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ImplementationGuide setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the implementation guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the implementation guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public ImplementationGuide setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A natural language name identifying the implementation guide. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A natural language name identifying the implementation guide. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ImplementationGuide setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the implementation guide. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the implementation guide. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ImplementationGuide setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ImplementationGuide setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the implementation guide.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the implementation guide.
     */
    public ImplementationGuide setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of this implementation guide. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this implementation guide. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ImplementationGuide setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this implementation guide. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this implementation guide. Enables tracking the life-cycle of the content.
     */
    public ImplementationGuide setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A Boolean value to indicate that this implementation guide is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A Boolean value to indicate that this implementation guide is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ImplementationGuide setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A Boolean value to indicate that this implementation guide is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A Boolean value to indicate that this implementation guide is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public ImplementationGuide setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the implementation guide was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the implementation guide was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImplementationGuide setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the implementation guide was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the implementation guide was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.
     */
    public ImplementationGuide setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the organization or individual that published the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the organization or individual that published the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ImplementationGuide setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the organization or individual that published the implementation guide.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the organization or individual that published the implementation guide.
     */
    public ImplementationGuide setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuide setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public ImplementationGuide addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the implementation guide from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the implementation guide from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImplementationGuide setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the implementation guide from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the implementation guide from a consumer's perspective.
     */
    public ImplementationGuide setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuide setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public ImplementationGuide addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the implementation guide is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuide setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public ImplementationGuide addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ImplementationGuide setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.
     */
    public ImplementationGuide setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #packageId} (The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.). This is the underlying object with id, value and extensions. The accessor "getPackageId" gives direct access to the value
     */
    public IdType getPackageIdElement() { 
      if (this.packageId == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.packageId");
        else if (Configuration.doAutoCreate())
          this.packageId = new IdType(); // bb
      return this.packageId;
    }

    public boolean hasPackageIdElement() { 
      return this.packageId != null && !this.packageId.isEmpty();
    }

    public boolean hasPackageId() { 
      return this.packageId != null && !this.packageId.isEmpty();
    }

    /**
     * @param value {@link #packageId} (The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.). This is the underlying object with id, value and extensions. The accessor "getPackageId" gives direct access to the value
     */
    public ImplementationGuide setPackageIdElement(IdType value) { 
      this.packageId = value;
      return this;
    }

    /**
     * @return The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.
     */
    public String getPackageId() { 
      return this.packageId == null ? null : this.packageId.getValue();
    }

    /**
     * @param value The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.
     */
    public ImplementationGuide setPackageId(String value) { 
      if (Utilities.noString(value))
        this.packageId = null;
      else {
        if (this.packageId == null)
          this.packageId = new IdType();
        this.packageId.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #license} (The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.). This is the underlying object with id, value and extensions. The accessor "getLicense" gives direct access to the value
     */
    public Enumeration<SPDXLicense> getLicenseElement() { 
      if (this.license == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.license");
        else if (Configuration.doAutoCreate())
          this.license = new Enumeration<SPDXLicense>(new SPDXLicenseEnumFactory()); // bb
      return this.license;
    }

    public boolean hasLicenseElement() { 
      return this.license != null && !this.license.isEmpty();
    }

    public boolean hasLicense() { 
      return this.license != null && !this.license.isEmpty();
    }

    /**
     * @param value {@link #license} (The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.). This is the underlying object with id, value and extensions. The accessor "getLicense" gives direct access to the value
     */
    public ImplementationGuide setLicenseElement(Enumeration<SPDXLicense> value) { 
      this.license = value;
      return this;
    }

    /**
     * @return The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
     */
    public SPDXLicense getLicense() { 
      return this.license == null ? null : this.license.getValue();
    }

    /**
     * @param value The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
     */
    public ImplementationGuide setLicense(SPDXLicense value) { 
      if (value == null)
        this.license = null;
      else {
        if (this.license == null)
          this.license = new Enumeration<SPDXLicense>(new SPDXLicenseEnumFactory());
        this.license.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public IdType getFhirVersionElement() { 
      if (this.fhirVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.fhirVersion");
        else if (Configuration.doAutoCreate())
          this.fhirVersion = new IdType(); // bb
      return this.fhirVersion;
    }

    public boolean hasFhirVersionElement() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    public boolean hasFhirVersion() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    /**
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public ImplementationGuide setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.
     */
    public ImplementationGuide setFhirVersion(String value) { 
      if (Utilities.noString(value))
        this.fhirVersion = null;
      else {
        if (this.fhirVersion == null)
          this.fhirVersion = new IdType();
        this.fhirVersion.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #dependsOn} (Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.)
     */
    public List<ImplementationGuideDependsOnComponent> getDependsOn() { 
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<ImplementationGuideDependsOnComponent>();
      return this.dependsOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuide setDependsOn(List<ImplementationGuideDependsOnComponent> theDependsOn) { 
      this.dependsOn = theDependsOn;
      return this;
    }

    public boolean hasDependsOn() { 
      if (this.dependsOn == null)
        return false;
      for (ImplementationGuideDependsOnComponent item : this.dependsOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideDependsOnComponent addDependsOn() { //3
      ImplementationGuideDependsOnComponent t = new ImplementationGuideDependsOnComponent();
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<ImplementationGuideDependsOnComponent>();
      this.dependsOn.add(t);
      return t;
    }

    public ImplementationGuide addDependsOn(ImplementationGuideDependsOnComponent t) { //3
      if (t == null)
        return this;
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<ImplementationGuideDependsOnComponent>();
      this.dependsOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dependsOn}, creating it if it does not already exist
     */
    public ImplementationGuideDependsOnComponent getDependsOnFirstRep() { 
      if (getDependsOn().isEmpty()) {
        addDependsOn();
      }
      return getDependsOn().get(0);
    }

    /**
     * @return {@link #global} (A set of profiles that all resources covered by this implementation guide must conform to.)
     */
    public List<ImplementationGuideGlobalComponent> getGlobal() { 
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideGlobalComponent>();
      return this.global;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuide setGlobal(List<ImplementationGuideGlobalComponent> theGlobal) { 
      this.global = theGlobal;
      return this;
    }

    public boolean hasGlobal() { 
      if (this.global == null)
        return false;
      for (ImplementationGuideGlobalComponent item : this.global)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideGlobalComponent addGlobal() { //3
      ImplementationGuideGlobalComponent t = new ImplementationGuideGlobalComponent();
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideGlobalComponent>();
      this.global.add(t);
      return t;
    }

    public ImplementationGuide addGlobal(ImplementationGuideGlobalComponent t) { //3
      if (t == null)
        return this;
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideGlobalComponent>();
      this.global.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #global}, creating it if it does not already exist
     */
    public ImplementationGuideGlobalComponent getGlobalFirstRep() { 
      if (getGlobal().isEmpty()) {
        addGlobal();
      }
      return getGlobal().get(0);
    }

    /**
     * @return {@link #definition} (The information needed by an IG publisher tool to publish the whole implementation guide.)
     */
    public ImplementationGuideDefinitionComponent getDefinition() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.definition");
        else if (Configuration.doAutoCreate())
          this.definition = new ImplementationGuideDefinitionComponent(); // cc
      return this.definition;
    }

    public boolean hasDefinition() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    /**
     * @param value {@link #definition} (The information needed by an IG publisher tool to publish the whole implementation guide.)
     */
    public ImplementationGuide setDefinition(ImplementationGuideDefinitionComponent value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return {@link #manifest} (Information about an assembled implementation guide, created by the publication tooling.)
     */
    public ImplementationGuideManifestComponent getManifest() { 
      if (this.manifest == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.manifest");
        else if (Configuration.doAutoCreate())
          this.manifest = new ImplementationGuideManifestComponent(); // cc
      return this.manifest;
    }

    public boolean hasManifest() { 
      return this.manifest != null && !this.manifest.isEmpty();
    }

    /**
     * @param value {@link #manifest} (Information about an assembled implementation guide, created by the publication tooling.)
     */
    public ImplementationGuide setManifest(ImplementationGuideManifestComponent value) { 
      this.manifest = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on different servers.", 0, 1, url));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the implementation guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the implementation guide. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("title", "string", "A short, descriptive, user-friendly title for the implementation guide.", 0, 1, title));
        children.add(new Property("status", "code", "The status of this implementation guide. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A Boolean value to indicate that this implementation guide is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the organization or individual that published the implementation guide.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the implementation guide from a consumer's perspective.", 0, 1, description));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.", 0, 1, copyright));
        children.add(new Property("packageId", "id", "The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.", 0, 1, packageId));
        children.add(new Property("license", "code", "The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.", 0, 1, license));
        children.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.", 0, 1, fhirVersion));
        children.add(new Property("dependsOn", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependsOn));
        children.add(new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global));
        children.add(new Property("definition", "", "The information needed by an IG publisher tool to publish the whole implementation guide.", 0, 1, definition));
        children.add(new Property("manifest", "", "Information about an assembled implementation guide, created by the publication tooling.", 0, 1, manifest));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on different servers.", 0, 1, url);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the implementation guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the implementation guide. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive, user-friendly title for the implementation guide.", 0, 1, title);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this implementation guide. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A Boolean value to indicate that this implementation guide is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the organization or individual that published the implementation guide.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the implementation guide from a consumer's perspective.", 0, 1, description);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide.", 0, 1, copyright);
        case 1802060801: /*packageId*/  return new Property("packageId", "id", "The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be assigned with care.", 0, 1, packageId);
        case 166757441: /*license*/  return new Property("license", "code", "The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.", 0, 1, license);
        case 461006061: /*fhirVersion*/  return new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.5.0. for this version.", 0, 1, fhirVersion);
        case -1109214266: /*dependsOn*/  return new Property("dependsOn", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependsOn);
        case -1243020381: /*global*/  return new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global);
        case -1014418093: /*definition*/  return new Property("definition", "", "The information needed by an IG publisher tool to publish the whole implementation guide.", 0, 1, definition);
        case 130625071: /*manifest*/  return new Property("manifest", "", "Information about an assembled implementation guide, created by the publication tooling.", 0, 1, manifest);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 1802060801: /*packageId*/ return this.packageId == null ? new Base[0] : new Base[] {this.packageId}; // IdType
        case 166757441: /*license*/ return this.license == null ? new Base[0] : new Base[] {this.license}; // Enumeration<SPDXLicense>
        case 461006061: /*fhirVersion*/ return this.fhirVersion == null ? new Base[0] : new Base[] {this.fhirVersion}; // IdType
        case -1109214266: /*dependsOn*/ return this.dependsOn == null ? new Base[0] : this.dependsOn.toArray(new Base[this.dependsOn.size()]); // ImplementationGuideDependsOnComponent
        case -1243020381: /*global*/ return this.global == null ? new Base[0] : this.global.toArray(new Base[this.global.size()]); // ImplementationGuideGlobalComponent
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // ImplementationGuideDefinitionComponent
        case 130625071: /*manifest*/ return this.manifest == null ? new Base[0] : new Base[] {this.manifest}; // ImplementationGuideManifestComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 1802060801: // packageId
          this.packageId = castToId(value); // IdType
          return value;
        case 166757441: // license
          value = new SPDXLicenseEnumFactory().fromType(castToCode(value));
          this.license = (Enumeration) value; // Enumeration<SPDXLicense>
          return value;
        case 461006061: // fhirVersion
          this.fhirVersion = castToId(value); // IdType
          return value;
        case -1109214266: // dependsOn
          this.getDependsOn().add((ImplementationGuideDependsOnComponent) value); // ImplementationGuideDependsOnComponent
          return value;
        case -1243020381: // global
          this.getGlobal().add((ImplementationGuideGlobalComponent) value); // ImplementationGuideGlobalComponent
          return value;
        case -1014418093: // definition
          this.definition = (ImplementationGuideDefinitionComponent) value; // ImplementationGuideDefinitionComponent
          return value;
        case 130625071: // manifest
          this.manifest = (ImplementationGuideManifestComponent) value; // ImplementationGuideManifestComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("packageId")) {
          this.packageId = castToId(value); // IdType
        } else if (name.equals("license")) {
          value = new SPDXLicenseEnumFactory().fromType(castToCode(value));
          this.license = (Enumeration) value; // Enumeration<SPDXLicense>
        } else if (name.equals("fhirVersion")) {
          this.fhirVersion = castToId(value); // IdType
        } else if (name.equals("dependsOn")) {
          this.getDependsOn().add((ImplementationGuideDependsOnComponent) value);
        } else if (name.equals("global")) {
          this.getGlobal().add((ImplementationGuideGlobalComponent) value);
        } else if (name.equals("definition")) {
          this.definition = (ImplementationGuideDefinitionComponent) value; // ImplementationGuideDefinitionComponent
        } else if (name.equals("manifest")) {
          this.manifest = (ImplementationGuideManifestComponent) value; // ImplementationGuideManifestComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 1522889671:  return getCopyrightElement();
        case 1802060801:  return getPackageIdElement();
        case 166757441:  return getLicenseElement();
        case 461006061:  return getFhirVersionElement();
        case -1109214266:  return addDependsOn(); 
        case -1243020381:  return addGlobal(); 
        case -1014418093:  return getDefinition(); 
        case 130625071:  return getManifest(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 1802060801: /*packageId*/ return new String[] {"id"};
        case 166757441: /*license*/ return new String[] {"code"};
        case 461006061: /*fhirVersion*/ return new String[] {"id"};
        case -1109214266: /*dependsOn*/ return new String[] {};
        case -1243020381: /*global*/ return new String[] {};
        case -1014418093: /*definition*/ return new String[] {};
        case 130625071: /*manifest*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.copyright");
        }
        else if (name.equals("packageId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.packageId");
        }
        else if (name.equals("license")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.license");
        }
        else if (name.equals("fhirVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.fhirVersion");
        }
        else if (name.equals("dependsOn")) {
          return addDependsOn();
        }
        else if (name.equals("global")) {
          return addGlobal();
        }
        else if (name.equals("definition")) {
          this.definition = new ImplementationGuideDefinitionComponent();
          return this.definition;
        }
        else if (name.equals("manifest")) {
          this.manifest = new ImplementationGuideManifestComponent();
          return this.manifest;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImplementationGuide";

  }

      public ImplementationGuide copy() {
        ImplementationGuide dst = new ImplementationGuide();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.packageId = packageId == null ? null : packageId.copy();
        dst.license = license == null ? null : license.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (dependsOn != null) {
          dst.dependsOn = new ArrayList<ImplementationGuideDependsOnComponent>();
          for (ImplementationGuideDependsOnComponent i : dependsOn)
            dst.dependsOn.add(i.copy());
        };
        if (global != null) {
          dst.global = new ArrayList<ImplementationGuideGlobalComponent>();
          for (ImplementationGuideGlobalComponent i : global)
            dst.global.add(i.copy());
        };
        dst.definition = definition == null ? null : definition.copy();
        dst.manifest = manifest == null ? null : manifest.copy();
        return dst;
      }

      protected ImplementationGuide typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuide))
          return false;
        ImplementationGuide o = (ImplementationGuide) other_;
        return compareDeep(copyright, o.copyright, true) && compareDeep(packageId, o.packageId, true) && compareDeep(license, o.license, true)
           && compareDeep(fhirVersion, o.fhirVersion, true) && compareDeep(dependsOn, o.dependsOn, true) && compareDeep(global, o.global, true)
           && compareDeep(definition, o.definition, true) && compareDeep(manifest, o.manifest, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuide))
          return false;
        ImplementationGuide o = (ImplementationGuide) other_;
        return compareValues(copyright, o.copyright, true) && compareValues(packageId, o.packageId, true) && compareValues(license, o.license, true)
           && compareValues(fhirVersion, o.fhirVersion, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(copyright, packageId, license
          , fhirVersion, dependsOn, global, definition, manifest);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImplementationGuide;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The implementation guide publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuide.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImplementationGuide.date", description="The implementation guide publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The implementation guide publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuide.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the implementation guide</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-value", path="ImplementationGuide.useContext", description="A use context type and value assigned to the implementation guide", type="composite", compositeOf={"context-type", "context"} )
  public static final String SP_CONTEXT_TYPE_VALUE = "context-type-value";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the implementation guide</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> CONTEXT_TYPE_VALUE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_CONTEXT_TYPE_VALUE);

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.definition.resource.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="ImplementationGuide.definition.resource.reference", description="Location of the resource", type="reference" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.definition.resource.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuide:resource</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESOURCE = new ca.uhn.fhir.model.api.Include("ImplementationGuide:resource").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ImplementationGuide.jurisdiction", description="Intended jurisdiction for the implementation guide", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ImplementationGuide.description", description="The description of the implementation guide", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.useContext.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type", path="ImplementationGuide.useContext.code", description="A type of use context assigned to the implementation guide", type="token" )
  public static final String SP_CONTEXT_TYPE = "context-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.useContext.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT_TYPE);

 /**
   * Search parameter: <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.experimental</b><br>
   * </p>
   */
  @SearchParamDefinition(name="experimental", path="ImplementationGuide.experimental", description="For testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.experimental</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EXPERIMENTAL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EXPERIMENTAL);

 /**
   * Search parameter: <b>global</b>
   * <p>
   * Description: <b>Profile that all resources must conform to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.global.profile</b><br>
   * </p>
   */
  @SearchParamDefinition(name="global", path="ImplementationGuide.global.profile", description="Profile that all resources must conform to", type="reference", target={StructureDefinition.class } )
  public static final String SP_GLOBAL = "global";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>global</b>
   * <p>
   * Description: <b>Profile that all resources must conform to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.global.profile</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam GLOBAL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_GLOBAL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuide:global</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_GLOBAL = new ca.uhn.fhir.model.api.Include("ImplementationGuide:global").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ImplementationGuide.title", description="The human-friendly name of the implementation guide", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ImplementationGuide.version", description="The business version of the implementation guide", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuide.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ImplementationGuide.url", description="The uri that identifies the implementation guide", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuide.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the implementation guide</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ImplementationGuide.useContext.valueQuantity, ImplementationGuide.useContext.valueRange</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-quantity", path="(ImplementationGuide.useContext.value as Quantity) | (ImplementationGuide.useContext.value as Range)", description="A quantity- or range-valued use context assigned to the implementation guide", type="quantity" )
  public static final String SP_CONTEXT_QUANTITY = "context-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the implementation guide</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ImplementationGuide.useContext.valueQuantity, ImplementationGuide.useContext.valueRange</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam CONTEXT_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_CONTEXT_QUANTITY);

 /**
   * Search parameter: <b>depends-on</b>
   * <p>
   * Description: <b>Identity of the IG that this depends on</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.dependsOn.uri</b><br>
   * </p>
   */
  @SearchParamDefinition(name="depends-on", path="ImplementationGuide.dependsOn.uri", description="Identity of the IG that this depends on", type="reference", target={ImplementationGuide.class } )
  public static final String SP_DEPENDS_ON = "depends-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>depends-on</b>
   * <p>
   * Description: <b>Identity of the IG that this depends on</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.dependsOn.uri</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDS_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDS_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuide:depends-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDS_ON = new ca.uhn.fhir.model.api.Include("ImplementationGuide:depends-on").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ImplementationGuide.name", description="Computationally friendly name of the implementation guide", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="(ImplementationGuide.useContext.value as CodeableConcept)", description="A use context assigned to the implementation guide", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ImplementationGuide.publisher", description="Name of the publisher of the implementation guide", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the implementation guide</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-quantity", path="ImplementationGuide.useContext", description="A use context type and quantity- or range-based value assigned to the implementation guide", type="composite", compositeOf={"context-type", "context-quantity"} )
  public static final String SP_CONTEXT_TYPE_QUANTITY = "context-type-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the implementation guide</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> CONTEXT_TYPE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_CONTEXT_TYPE_QUANTITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImplementationGuide.status", description="The current status of the implementation guide", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

