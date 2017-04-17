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


import org.hl7.fhir.exceptions.FHIRException;

public enum V3VaccineManufacturer {

        /**
         * Abbott Laboratories (includes Ross Products Division)
         */
        AB, 
        /**
         * Adams Laboratories
         */
        AD, 
        /**
         * Alpha Therapeutic Corporation
         */
        ALP, 
        /**
         * Armour [Inactive-use CEN]
         */
        AR, 
        /**
         * Aviron
         */
        AVI, 
        /**
         * Baxter Healthcare Corporation
         */
        BA, 
        /**
         * Bayer Corporation (includes Miles, Inc. and Cutter Laboratories)
         */
        BAY, 
        /**
         * Berna Products [Inactive-use BPC]
         */
        BP, 
        /**
         * Berna Products Corporation (includes Swiss Serum and Vaccine Institute Berne)
         */
        BPC, 
        /**
         * Centeon L.L.C. (includes Armour Pharmaceutical Company)
         */
        CEN, 
        /**
         * Chiron Corporation
         */
        CHI, 
        /**
         * Connaught [Inactive-use PMC]
         */
        CON, 
        /**
         * Evans Medical Limited (an affiliate of Medeva Pharmaceuticals, Inc.)
         */
        EVN, 
        /**
         * Greer Laboratories, Inc.
         */
        GRE, 
        /**
         * Immuno International AG
         */
        IAG, 
        /**
         * Merieux [Inactive-use PMC]
         */
        IM, 
        /**
         * Immuno-U.S., Inc.
         */
        IUS, 
        /**
         * The Research Foundation for Microbial Diseases of Osaka University (BIKEN)
         */
        JPN, 
        /**
         * Korea Green Cross Corporation
         */
        KGC, 
        /**
         * Lederle [Inactive-use WAL]
         */
        LED, 
        /**
         * Massachusetts Public Health Biologic Laboratories
         */
        MA, 
        /**
         * MedImmune, Inc.
         */
        MED, 
        /**
         * Miles [Inactive-use BAY]
         */
        MIL, 
        /**
         * Bioport Corporation (formerly Michigan Biologic Products Institute)
         */
        MIP, 
        /**
         * Merck & Co., Inc.
         */
        MSD, 
        /**
         * NABI (formerly North American Biologicals, Inc.)
         */
        NAB, 
        /**
         * North American Vaccine, Inc.
         */
        NAV, 
        /**
         * Novartis Pharmaceutical Corporation (includes Ciba-Geigy Limited and Sandoz Limited)
         */
        NOV, 
        /**
         * New York Blood Center
         */
        NYB, 
        /**
         * Ortho Diagnostic Systems, Inc.
         */
        ORT, 
        /**
         * Organon Teknika Corporation
         */
        OTC, 
        /**
         * Parkedale Pharmaceuticals (formerly Parke-Davis)
         */
        PD, 
        /**
         * Aventis Pasteur Inc. (formerly Pasteur Merieux Connaught; includes Connaught Laboratories and Pasteur Merieux)
         */
        PMC, 
        /**
         * Praxis Biologics [Inactive-use WAL]
         */
        PRX, 
        /**
         * Sclavo, Inc.
         */
        SCL, 
        /**
         * Swiss Serum and Vaccine Inst. [Inactive-use BPC]
         */
        SI, 
        /**
         * SmithKline Beecham
         */
        SKB, 
        /**
         * United States Army Medical Research and Materiel Command
         */
        USA, 
        /**
         * Wyeth-Ayerst [Inactive-use WAL]
         */
        WA, 
        /**
         * Wyeth-Ayerst (includes Wyeth-Lederle Vaccines and Pediatrics, Wyeth Laboratories, Lederle Laboratories, and Praxis Biologics)
         */
        WAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3VaccineManufacturer fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AB".equals(codeString))
          return AB;
        if ("AD".equals(codeString))
          return AD;
        if ("ALP".equals(codeString))
          return ALP;
        if ("AR".equals(codeString))
          return AR;
        if ("AVI".equals(codeString))
          return AVI;
        if ("BA".equals(codeString))
          return BA;
        if ("BAY".equals(codeString))
          return BAY;
        if ("BP".equals(codeString))
          return BP;
        if ("BPC".equals(codeString))
          return BPC;
        if ("CEN".equals(codeString))
          return CEN;
        if ("CHI".equals(codeString))
          return CHI;
        if ("CON".equals(codeString))
          return CON;
        if ("EVN".equals(codeString))
          return EVN;
        if ("GRE".equals(codeString))
          return GRE;
        if ("IAG".equals(codeString))
          return IAG;
        if ("IM".equals(codeString))
          return IM;
        if ("IUS".equals(codeString))
          return IUS;
        if ("JPN".equals(codeString))
          return JPN;
        if ("KGC".equals(codeString))
          return KGC;
        if ("LED".equals(codeString))
          return LED;
        if ("MA".equals(codeString))
          return MA;
        if ("MED".equals(codeString))
          return MED;
        if ("MIL".equals(codeString))
          return MIL;
        if ("MIP".equals(codeString))
          return MIP;
        if ("MSD".equals(codeString))
          return MSD;
        if ("NAB".equals(codeString))
          return NAB;
        if ("NAV".equals(codeString))
          return NAV;
        if ("NOV".equals(codeString))
          return NOV;
        if ("NYB".equals(codeString))
          return NYB;
        if ("ORT".equals(codeString))
          return ORT;
        if ("OTC".equals(codeString))
          return OTC;
        if ("PD".equals(codeString))
          return PD;
        if ("PMC".equals(codeString))
          return PMC;
        if ("PRX".equals(codeString))
          return PRX;
        if ("SCL".equals(codeString))
          return SCL;
        if ("SI".equals(codeString))
          return SI;
        if ("SKB".equals(codeString))
          return SKB;
        if ("USA".equals(codeString))
          return USA;
        if ("WA".equals(codeString))
          return WA;
        if ("WAL".equals(codeString))
          return WAL;
        throw new FHIRException("Unknown V3VaccineManufacturer code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AB: return "AB";
            case AD: return "AD";
            case ALP: return "ALP";
            case AR: return "AR";
            case AVI: return "AVI";
            case BA: return "BA";
            case BAY: return "BAY";
            case BP: return "BP";
            case BPC: return "BPC";
            case CEN: return "CEN";
            case CHI: return "CHI";
            case CON: return "CON";
            case EVN: return "EVN";
            case GRE: return "GRE";
            case IAG: return "IAG";
            case IM: return "IM";
            case IUS: return "IUS";
            case JPN: return "JPN";
            case KGC: return "KGC";
            case LED: return "LED";
            case MA: return "MA";
            case MED: return "MED";
            case MIL: return "MIL";
            case MIP: return "MIP";
            case MSD: return "MSD";
            case NAB: return "NAB";
            case NAV: return "NAV";
            case NOV: return "NOV";
            case NYB: return "NYB";
            case ORT: return "ORT";
            case OTC: return "OTC";
            case PD: return "PD";
            case PMC: return "PMC";
            case PRX: return "PRX";
            case SCL: return "SCL";
            case SI: return "SI";
            case SKB: return "SKB";
            case USA: return "USA";
            case WA: return "WA";
            case WAL: return "WAL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/VaccineManufacturer";
        }
        public String getDefinition() {
          switch (this) {
            case AB: return "Abbott Laboratories (includes Ross Products Division)";
            case AD: return "Adams Laboratories";
            case ALP: return "Alpha Therapeutic Corporation";
            case AR: return "Armour [Inactive-use CEN]";
            case AVI: return "Aviron";
            case BA: return "Baxter Healthcare Corporation";
            case BAY: return "Bayer Corporation (includes Miles, Inc. and Cutter Laboratories)";
            case BP: return "Berna Products [Inactive-use BPC]";
            case BPC: return "Berna Products Corporation (includes Swiss Serum and Vaccine Institute Berne)";
            case CEN: return "Centeon L.L.C. (includes Armour Pharmaceutical Company)";
            case CHI: return "Chiron Corporation";
            case CON: return "Connaught [Inactive-use PMC]";
            case EVN: return "Evans Medical Limited (an affiliate of Medeva Pharmaceuticals, Inc.)";
            case GRE: return "Greer Laboratories, Inc.";
            case IAG: return "Immuno International AG";
            case IM: return "Merieux [Inactive-use PMC]";
            case IUS: return "Immuno-U.S., Inc.";
            case JPN: return "The Research Foundation for Microbial Diseases of Osaka University (BIKEN)";
            case KGC: return "Korea Green Cross Corporation";
            case LED: return "Lederle [Inactive-use WAL]";
            case MA: return "Massachusetts Public Health Biologic Laboratories";
            case MED: return "MedImmune, Inc.";
            case MIL: return "Miles [Inactive-use BAY]";
            case MIP: return "Bioport Corporation (formerly Michigan Biologic Products Institute)";
            case MSD: return "Merck & Co., Inc.";
            case NAB: return "NABI (formerly North American Biologicals, Inc.)";
            case NAV: return "North American Vaccine, Inc.";
            case NOV: return "Novartis Pharmaceutical Corporation (includes Ciba-Geigy Limited and Sandoz Limited)";
            case NYB: return "New York Blood Center";
            case ORT: return "Ortho Diagnostic Systems, Inc.";
            case OTC: return "Organon Teknika Corporation";
            case PD: return "Parkedale Pharmaceuticals (formerly Parke-Davis)";
            case PMC: return "Aventis Pasteur Inc. (formerly Pasteur Merieux Connaught; includes Connaught Laboratories and Pasteur Merieux)";
            case PRX: return "Praxis Biologics [Inactive-use WAL]";
            case SCL: return "Sclavo, Inc.";
            case SI: return "Swiss Serum and Vaccine Inst. [Inactive-use BPC]";
            case SKB: return "SmithKline Beecham";
            case USA: return "United States Army Medical Research and Materiel Command";
            case WA: return "Wyeth-Ayerst [Inactive-use WAL]";
            case WAL: return "Wyeth-Ayerst (includes Wyeth-Lederle Vaccines and Pediatrics, Wyeth Laboratories, Lederle Laboratories, and Praxis Biologics)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AB: return "Abbott Laboratories (includes Ross Products Division)";
            case AD: return "Adams Laboratories";
            case ALP: return "Alpha Therapeutic Corporation";
            case AR: return "Armour [Inactive - use CEN]";
            case AVI: return "Aviron";
            case BA: return "Baxter Healthcare Corporation";
            case BAY: return "Bayer Corporation (includes Miles, Inc. and Cutter Laboratories)";
            case BP: return "Berna Products [Inactive - use BPC]";
            case BPC: return "Berna Products Corporation (includes Swiss Serum and Vaccine Institute Berne)";
            case CEN: return "Centeon L.L.C. (includes Armour Pharmaceutical Company)";
            case CHI: return "Chiron Corporation";
            case CON: return "Connaught [Inactive - use PMC]";
            case EVN: return "Evans Medical Limited (an affiliate of Medeva Pharmaceuticals, Inc.)";
            case GRE: return "Greer Laboratories, Inc.";
            case IAG: return "Immuno International AG";
            case IM: return "Merieux [Inactive - use PMC]";
            case IUS: return "Immuno-U.S., Inc.";
            case JPN: return "The Research Foundation for Microbial Diseases of Osaka University (BIKEN)";
            case KGC: return "Korea Green Cross Corporation";
            case LED: return "Lederle [Inactive - use WAL]";
            case MA: return "Massachusetts Public Health Biologic Laboratories";
            case MED: return "MedImmune, Inc.";
            case MIL: return "Miles [Inactive - use BAY]";
            case MIP: return "Bioport Corporation (formerly Michigan Biologic Products Institute)";
            case MSD: return "Merck and Co., Inc.";
            case NAB: return "NABI (formerly North American Biologicals, Inc.)";
            case NAV: return "North American Vaccine, Inc.";
            case NOV: return "Novartis Pharmaceutical Corporation (includes Ciba-Geigy Limited and Sandoz Limited)";
            case NYB: return "New York Blood Center";
            case ORT: return "Ortho Diagnostic Systems, Inc.";
            case OTC: return "Organon Teknika Corporation";
            case PD: return "Parkedale Pharmaceuticals (formerly Parke-Davis)";
            case PMC: return "Aventis Pasteur Inc. (formerly Pasteur Merieux Connaught; includes Connaught Laboratories and Pasteur Merieux)";
            case PRX: return "Praxis Biologics [Inactive - use WAL]";
            case SCL: return "Sclavo, Inc.";
            case SI: return "Swiss Serum and Vaccine Inst. [Inactive - use BPC]";
            case SKB: return "SmithKline Beecham";
            case USA: return "United States Army Medical Research and Materiel Command";
            case WA: return "Wyeth-Ayerst [Inactive - use WAL]";
            case WAL: return "Wyeth-Ayerst (includes Wyeth-Lederle Vaccines and Pediatrics, Wyeth Laboratories, Lederle Laboratories, and Praxis Biologics)";
            default: return "?";
          }
    }


}

