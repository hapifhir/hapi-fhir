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


public enum V3OrderableDrugForm {

        /**
         * AdministrableDrugForm
         */
        _ADMINISTRABLEDRUGFORM, 
        /**
         * Applicatorful
         */
        APPFUL, 
        /**
         * Drops
         */
        DROP, 
        /**
         * Nasal Drops
         */
        NDROP, 
        /**
         * Ophthalmic Drops
         */
        OPDROP, 
        /**
         * Oral Drops
         */
        ORDROP, 
        /**
         * Otic Drops
         */
        OTDROP, 
        /**
         * Puff
         */
        PUFF, 
        /**
         * Scoops
         */
        SCOOP, 
        /**
         * Sprays
         */
        SPRY, 
        /**
         * DispensableDrugForm
         */
        _DISPENSABLEDRUGFORM, 
        /**
         * Any elastic aeriform fluid in which the molecules are separated from one another and have free paths.
         */
        _GASDRUGFORM, 
        /**
         * Gas for Inhalation
         */
        GASINHL, 
        /**
         * GasLiquidMixture
         */
        _GASLIQUIDMIXTURE, 
        /**
         * Aerosol
         */
        AER, 
        /**
         * Breath Activated Inhaler
         */
        BAINHL, 
        /**
         * Inhalant Solution
         */
        INHLSOL, 
        /**
         * Metered Dose Inhaler
         */
        MDINHL, 
        /**
         * Nasal Spray
         */
        NASSPRY, 
        /**
         * Dermal Spray
         */
        DERMSPRY, 
        /**
         * Foam
         */
        FOAM, 
        /**
         * Foam with Applicator
         */
        FOAMAPL, 
        /**
         * Rectal foam
         */
        RECFORM, 
        /**
         * Vaginal foam
         */
        VAGFOAM, 
        /**
         * Vaginal foam with applicator
         */
        VAGFOAMAPL, 
        /**
         * Rectal Spray
         */
        RECSPRY, 
        /**
         * Vaginal Spray
         */
        VAGSPRY, 
        /**
         * GasSolidSpray
         */
        _GASSOLIDSPRAY, 
        /**
         * Inhalant
         */
        INHL, 
        /**
         * Breath Activated Powder Inhaler
         */
        BAINHLPWD, 
        /**
         * Inhalant Powder
         */
        INHLPWD, 
        /**
         * Metered Dose Powder Inhaler
         */
        MDINHLPWD, 
        /**
         * Nasal Inhalant
         */
        NASINHL, 
        /**
         * Oral Inhalant
         */
        ORINHL, 
        /**
         * Powder Spray
         */
        PWDSPRY, 
        /**
         * Spray with Adaptor
         */
        SPRYADAPT, 
        /**
         * A state of substance that is an intermediate one entered into as matter goes from solid to gas; liquids are also intermediate in that they have neither the orderliness of a crystal nor the randomness of a gas. (Note: This term should not be used to describe solutions, only pure chemicals in their liquid state.)
         */
        _LIQUID, 
        /**
         * Liquid Cleanser
         */
        LIQCLN, 
        /**
         * Medicated Liquid Soap
         */
        LIQSOAP, 
        /**
         * A liquid soap or detergent used to clean the hair and scalp and is often used as a vehicle for dermatologic agents.
         */
        SHMP, 
        /**
         * An unctuous, combustible substance which is liquid, or easily liquefiable, on warming, and is soluble in ether but insoluble in water. Such substances, depending on their origin, are classified as animal, mineral, or vegetable oils.
         */
        OIL, 
        /**
         * Topical Oil
         */
        TOPOIL, 
        /**
         * A liquid preparation that contains one or more chemical substances dissolved, i.e., molecularly dispersed, in a suitable solvent or mixture of mutually miscible solvents.
         */
        SOL, 
        /**
         * Intraperitoneal Solution
         */
        IPSOL, 
        /**
         * A sterile solution intended to bathe or flush open wounds or body cavities; they're used topically, never parenterally.
         */
        IRSOL, 
        /**
         * A liquid preparation, intended for the irrigative cleansing of the vagina, that is prepared from powders, liquid solutions, or liquid concentrates and contains one or more chemical substances dissolved in a suitable solvent or mutually miscible solvents.
         */
        DOUCHE, 
        /**
         * A rectal preparation for therapeutic, diagnostic, or nutritive purposes.
         */
        ENEMA, 
        /**
         * Ophthalmic Irrigation Solution
         */
        OPIRSOL, 
        /**
         * Intravenous Solution
         */
        IVSOL, 
        /**
         * Oral Solution
         */
        ORALSOL, 
        /**
         * A clear, pleasantly flavored, sweetened hydroalcoholic liquid containing dissolved medicinal agents; it is intended for oral use.
         */
        ELIXIR, 
        /**
         * An aqueous solution which is most often used for its deodorant, refreshing, or antiseptic effect.
         */
        RINSE, 
        /**
         * An oral solution containing high concentrations of sucrose or other sugars; the term has also been used to include any other liquid dosage form prepared in a sweet and viscid vehicle, including oral suspensions.
         */
        SYRUP, 
        /**
         * Rectal Solution
         */
        RECSOL, 
        /**
         * Topical Solution
         */
        TOPSOL, 
        /**
         * A solution or mixture of various substances in oil, alcoholic solutions of soap, or emulsions intended for external application.
         */
        LIN, 
        /**
         * Mucous Membrane Topical Solution
         */
        MUCTOPSOL, 
        /**
         * Tincture
         */
        TINC, 
        /**
         * A two-phase system in which one liquid is dispersed throughout another liquid in the form of small droplets.
         */
        _LIQUIDLIQUIDEMULSION, 
        /**
         * A semisolid dosage form containing one or more drug substances dissolved or dispersed in a suitable base; more recently, the term has been restricted to products consisting of oil-in-water emulsions or aqueous microcrystalline dispersions of long chain fatty acids or alcohols that are water washable and more cosmetically and aesthetically acceptable.
         */
        CRM, 
        /**
         * Nasal Cream
         */
        NASCRM, 
        /**
         * Ophthalmic Cream
         */
        OPCRM, 
        /**
         * Oral Cream
         */
        ORCRM, 
        /**
         * Otic Cream
         */
        OTCRM, 
        /**
         * Rectal Cream
         */
        RECCRM, 
        /**
         * Topical Cream
         */
        TOPCRM, 
        /**
         * Vaginal Cream
         */
        VAGCRM, 
        /**
         * Vaginal Cream with Applicator
         */
        VAGCRMAPL, 
        /**
         * The term "lotion" has been used to categorize many topical suspensions, solutions and emulsions intended for application to the skin.
         */
        LTN, 
        /**
         * Topical Lotion
         */
        TOPLTN, 
        /**
         * A semisolid preparation intended for external application to the skin or mucous membranes.
         */
        OINT, 
        /**
         * Nasal Ointment
         */
        NASOINT, 
        /**
         * Ointment with Applicator
         */
        OINTAPL, 
        /**
         * Ophthalmic Ointment
         */
        OPOINT, 
        /**
         * Otic Ointment
         */
        OTOINT, 
        /**
         * Rectal Ointment
         */
        RECOINT, 
        /**
         * Topical Ointment
         */
        TOPOINT, 
        /**
         * Vaginal Ointment
         */
        VAGOINT, 
        /**
         * Vaginal Ointment with Applicator
         */
        VAGOINTAPL, 
        /**
         * A liquid preparation which consists of solid particles dispersed throughout a liquid phase in which the particles are not soluble.
         */
        _LIQUIDSOLIDSUSPENSION, 
        /**
         * A semisolid system consisting of either suspensions made up of small inorganic particles or large organic molecules interpenetrated by a liquid.
         */
        GEL, 
        /**
         * Gel with Applicator
         */
        GELAPL, 
        /**
         * Nasal Gel
         */
        NASGEL, 
        /**
         * Ophthalmic Gel
         */
        OPGEL, 
        /**
         * Otic Gel
         */
        OTGEL, 
        /**
         * Topical Gel
         */
        TOPGEL, 
        /**
         * Urethral Gel
         */
        URETHGEL, 
        /**
         * Vaginal Gel
         */
        VAGGEL, 
        /**
         * Vaginal Gel with Applicator
         */
        VGELAPL, 
        /**
         * A semisolid dosage form that contains one or more drug substances intended for topical application.
         */
        PASTE, 
        /**
         * Pudding
         */
        PUD, 
        /**
         * A paste formulation intended to clean and/or polish the teeth, and which may contain certain additional agents.
         */
        TPASTE, 
        /**
         * Suspension
         */
        SUSP, 
        /**
         * Intrathecal Suspension
         */
        ITSUSP, 
        /**
         * Ophthalmic Suspension
         */
        OPSUSP, 
        /**
         * Oral Suspension
         */
        ORSUSP, 
        /**
         * Extended-Release Suspension
         */
        ERSUSP, 
        /**
         * 12 Hour Extended-Release Suspension
         */
        ERSUSP12, 
        /**
         * 24 Hour Extended Release Suspension
         */
        ERSUSP24, 
        /**
         * Otic Suspension
         */
        OTSUSP, 
        /**
         * Rectal Suspension
         */
        RECSUSP, 
        /**
         * SolidDrugForm
         */
        _SOLIDDRUGFORM, 
        /**
         * Bar
         */
        BAR, 
        /**
         * Bar Soap
         */
        BARSOAP, 
        /**
         * Medicated Bar Soap
         */
        MEDBAR, 
        /**
         * A solid dosage form usually in the form of a rectangle that is meant to be chewed.
         */
        CHEWBAR, 
        /**
         * A solid dosage form in the shape of a small ball.
         */
        BEAD, 
        /**
         * Cake
         */
        CAKE, 
        /**
         * A substance that serves to produce solid union between two surfaces.
         */
        CEMENT, 
        /**
         * A naturally produced angular solid of definite form in which the ultimate units from which it is built up are systematically arranged; they are usually evenly spaced on a regular space lattice.
         */
        CRYS, 
        /**
         * A circular plate-like organ or structure.
         */
        DISK, 
        /**
         * Flakes
         */
        FLAKE, 
        /**
         * A small particle or grain.
         */
        GRAN, 
        /**
         * A sweetened and flavored insoluble plastic material of various shapes which when chewed, releases a drug substance into the oral cavity.
         */
        GUM, 
        /**
         * Pad
         */
        PAD, 
        /**
         * Medicated Pad
         */
        MEDPAD, 
        /**
         * A drug delivery system that contains an adhesived backing and that permits its ingredients to diffuse from some portion of it (e.g. the backing itself, a reservoir, the adhesive, or some other component) into the body from the external site where it is applied.
         */
        PATCH, 
        /**
         * Transdermal Patch
         */
        TPATCH, 
        /**
         * 16 Hour Transdermal Patch
         */
        TPATH16, 
        /**
         * 24 Hour Transdermal Patch
         */
        TPATH24, 
        /**
         * Biweekly Transdermal Patch
         */
        TPATH2WK, 
        /**
         * 72 Hour Transdermal Patch
         */
        TPATH72, 
        /**
         * Weekly Transdermal Patch
         */
        TPATHWK, 
        /**
         * A small sterile solid mass consisting of a highly purified drug (with or without excipients) made by the formation of granules, or by compression and molding.
         */
        PELLET, 
        /**
         * A small, round solid dosage form containing a medicinal agent intended for oral administration.
         */
        PILL, 
        /**
         * A solid dosage form in which the drug is enclosed within either a hard or soft soluble container or "shell" made from a suitable form of gelatin.
         */
        CAP, 
        /**
         * Oral Capsule
         */
        ORCAP, 
        /**
         * Enteric Coated Capsule
         */
        ENTCAP, 
        /**
         * Extended Release Enteric Coated Capsule
         */
        ERENTCAP, 
        /**
         * A solid dosage form in which the drug is enclosed within either a hard or soft soluble container made from a suitable form of gelatin, and which releases a drug (or drugs) in such a manner to allow a reduction in dosing frequency as compared to that drug (or drugs) presented as a conventional dosage form.
         */
        ERCAP, 
        /**
         * 12 Hour Extended Release Capsule
         */
        ERCAP12, 
        /**
         * 24 Hour Extended Release Capsule
         */
        ERCAP24, 
        /**
         * Rationale: Duplicate of code ERENTCAP. Use code ERENTCAP instead.
         */
        ERECCAP, 
        /**
         * A solid dosage form containing medicinal substances with or without suitable diluents.
         */
        TAB, 
        /**
         * Oral Tablet
         */
        ORTAB, 
        /**
         * Buccal Tablet
         */
        BUCTAB, 
        /**
         * Sustained Release Buccal Tablet
         */
        SRBUCTAB, 
        /**
         * Caplet
         */
        CAPLET, 
        /**
         * A solid dosage form containing medicinal substances with or without suitable diluents that is intended to be chewed, producing a pleasant tasting residue in the oral cavity that is easily swallowed and does not leave a bitter or unpleasant after-taste.
         */
        CHEWTAB, 
        /**
         * Coated Particles Tablet
         */
        CPTAB, 
        /**
         * A solid dosage form containing medicinal substances which disintegrates rapidly, usually within a matter of seconds, when placed upon the tongue.
         */
        DISINTAB, 
        /**
         * Delayed Release Tablet
         */
        DRTAB, 
        /**
         * Enteric Coated Tablet
         */
        ECTAB, 
        /**
         * Extended Release Enteric Coated Tablet
         */
        ERECTAB, 
        /**
         * A solid dosage form containing a drug which allows at least a reduction in dosing frequency as compared to that drug presented in conventional dosage form.
         */
        ERTAB, 
        /**
         * 12 Hour Extended Release Tablet
         */
        ERTAB12, 
        /**
         * 24 Hour Extended Release Tablet
         */
        ERTAB24, 
        /**
         * A solid preparation containing one or more medicaments, usually in a flavored, sweetened base which is intended to dissolve or disintegrate slowly in the mouth.
         */
        ORTROCHE, 
        /**
         * Sublingual Tablet
         */
        SLTAB, 
        /**
         * Vaginal Tablet
         */
        VAGTAB, 
        /**
         * An intimate mixture of dry, finely divided drugs and/or chemicals that may be intended for internal or external use.
         */
        POWD, 
        /**
         * Topical Powder
         */
        TOPPWD, 
        /**
         * Rectal Powder
         */
        RECPWD, 
        /**
         * Vaginal Powder
         */
        VAGPWD, 
        /**
         * A solid body of various weights and shapes, adapted for introduction into the rectal, vaginal, or urethral orifice of the human body; they usually melt, soften, or dissolve at body temperature.
         */
        SUPP, 
        /**
         * Rectal Suppository
         */
        RECSUPP, 
        /**
         * Urethral suppository
         */
        URETHSUPP, 
        /**
         * Vaginal Suppository
         */
        VAGSUPP, 
        /**
         * A wad of absorbent material usually wound around one end of a small stick and used for applying medication or for removing material from an area.
         */
        SWAB, 
        /**
         * Medicated swab
         */
        MEDSWAB, 
        /**
         * A thin slice of material containing a medicinal agent.
         */
        WAFER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3OrderableDrugForm fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_AdministrableDrugForm".equals(codeString))
          return _ADMINISTRABLEDRUGFORM;
        if ("APPFUL".equals(codeString))
          return APPFUL;
        if ("DROP".equals(codeString))
          return DROP;
        if ("NDROP".equals(codeString))
          return NDROP;
        if ("OPDROP".equals(codeString))
          return OPDROP;
        if ("ORDROP".equals(codeString))
          return ORDROP;
        if ("OTDROP".equals(codeString))
          return OTDROP;
        if ("PUFF".equals(codeString))
          return PUFF;
        if ("SCOOP".equals(codeString))
          return SCOOP;
        if ("SPRY".equals(codeString))
          return SPRY;
        if ("_DispensableDrugForm".equals(codeString))
          return _DISPENSABLEDRUGFORM;
        if ("_GasDrugForm".equals(codeString))
          return _GASDRUGFORM;
        if ("GASINHL".equals(codeString))
          return GASINHL;
        if ("_GasLiquidMixture".equals(codeString))
          return _GASLIQUIDMIXTURE;
        if ("AER".equals(codeString))
          return AER;
        if ("BAINHL".equals(codeString))
          return BAINHL;
        if ("INHLSOL".equals(codeString))
          return INHLSOL;
        if ("MDINHL".equals(codeString))
          return MDINHL;
        if ("NASSPRY".equals(codeString))
          return NASSPRY;
        if ("DERMSPRY".equals(codeString))
          return DERMSPRY;
        if ("FOAM".equals(codeString))
          return FOAM;
        if ("FOAMAPL".equals(codeString))
          return FOAMAPL;
        if ("RECFORM".equals(codeString))
          return RECFORM;
        if ("VAGFOAM".equals(codeString))
          return VAGFOAM;
        if ("VAGFOAMAPL".equals(codeString))
          return VAGFOAMAPL;
        if ("RECSPRY".equals(codeString))
          return RECSPRY;
        if ("VAGSPRY".equals(codeString))
          return VAGSPRY;
        if ("_GasSolidSpray".equals(codeString))
          return _GASSOLIDSPRAY;
        if ("INHL".equals(codeString))
          return INHL;
        if ("BAINHLPWD".equals(codeString))
          return BAINHLPWD;
        if ("INHLPWD".equals(codeString))
          return INHLPWD;
        if ("MDINHLPWD".equals(codeString))
          return MDINHLPWD;
        if ("NASINHL".equals(codeString))
          return NASINHL;
        if ("ORINHL".equals(codeString))
          return ORINHL;
        if ("PWDSPRY".equals(codeString))
          return PWDSPRY;
        if ("SPRYADAPT".equals(codeString))
          return SPRYADAPT;
        if ("_Liquid".equals(codeString))
          return _LIQUID;
        if ("LIQCLN".equals(codeString))
          return LIQCLN;
        if ("LIQSOAP".equals(codeString))
          return LIQSOAP;
        if ("SHMP".equals(codeString))
          return SHMP;
        if ("OIL".equals(codeString))
          return OIL;
        if ("TOPOIL".equals(codeString))
          return TOPOIL;
        if ("SOL".equals(codeString))
          return SOL;
        if ("IPSOL".equals(codeString))
          return IPSOL;
        if ("IRSOL".equals(codeString))
          return IRSOL;
        if ("DOUCHE".equals(codeString))
          return DOUCHE;
        if ("ENEMA".equals(codeString))
          return ENEMA;
        if ("OPIRSOL".equals(codeString))
          return OPIRSOL;
        if ("IVSOL".equals(codeString))
          return IVSOL;
        if ("ORALSOL".equals(codeString))
          return ORALSOL;
        if ("ELIXIR".equals(codeString))
          return ELIXIR;
        if ("RINSE".equals(codeString))
          return RINSE;
        if ("SYRUP".equals(codeString))
          return SYRUP;
        if ("RECSOL".equals(codeString))
          return RECSOL;
        if ("TOPSOL".equals(codeString))
          return TOPSOL;
        if ("LIN".equals(codeString))
          return LIN;
        if ("MUCTOPSOL".equals(codeString))
          return MUCTOPSOL;
        if ("TINC".equals(codeString))
          return TINC;
        if ("_LiquidLiquidEmulsion".equals(codeString))
          return _LIQUIDLIQUIDEMULSION;
        if ("CRM".equals(codeString))
          return CRM;
        if ("NASCRM".equals(codeString))
          return NASCRM;
        if ("OPCRM".equals(codeString))
          return OPCRM;
        if ("ORCRM".equals(codeString))
          return ORCRM;
        if ("OTCRM".equals(codeString))
          return OTCRM;
        if ("RECCRM".equals(codeString))
          return RECCRM;
        if ("TOPCRM".equals(codeString))
          return TOPCRM;
        if ("VAGCRM".equals(codeString))
          return VAGCRM;
        if ("VAGCRMAPL".equals(codeString))
          return VAGCRMAPL;
        if ("LTN".equals(codeString))
          return LTN;
        if ("TOPLTN".equals(codeString))
          return TOPLTN;
        if ("OINT".equals(codeString))
          return OINT;
        if ("NASOINT".equals(codeString))
          return NASOINT;
        if ("OINTAPL".equals(codeString))
          return OINTAPL;
        if ("OPOINT".equals(codeString))
          return OPOINT;
        if ("OTOINT".equals(codeString))
          return OTOINT;
        if ("RECOINT".equals(codeString))
          return RECOINT;
        if ("TOPOINT".equals(codeString))
          return TOPOINT;
        if ("VAGOINT".equals(codeString))
          return VAGOINT;
        if ("VAGOINTAPL".equals(codeString))
          return VAGOINTAPL;
        if ("_LiquidSolidSuspension".equals(codeString))
          return _LIQUIDSOLIDSUSPENSION;
        if ("GEL".equals(codeString))
          return GEL;
        if ("GELAPL".equals(codeString))
          return GELAPL;
        if ("NASGEL".equals(codeString))
          return NASGEL;
        if ("OPGEL".equals(codeString))
          return OPGEL;
        if ("OTGEL".equals(codeString))
          return OTGEL;
        if ("TOPGEL".equals(codeString))
          return TOPGEL;
        if ("URETHGEL".equals(codeString))
          return URETHGEL;
        if ("VAGGEL".equals(codeString))
          return VAGGEL;
        if ("VGELAPL".equals(codeString))
          return VGELAPL;
        if ("PASTE".equals(codeString))
          return PASTE;
        if ("PUD".equals(codeString))
          return PUD;
        if ("TPASTE".equals(codeString))
          return TPASTE;
        if ("SUSP".equals(codeString))
          return SUSP;
        if ("ITSUSP".equals(codeString))
          return ITSUSP;
        if ("OPSUSP".equals(codeString))
          return OPSUSP;
        if ("ORSUSP".equals(codeString))
          return ORSUSP;
        if ("ERSUSP".equals(codeString))
          return ERSUSP;
        if ("ERSUSP12".equals(codeString))
          return ERSUSP12;
        if ("ERSUSP24".equals(codeString))
          return ERSUSP24;
        if ("OTSUSP".equals(codeString))
          return OTSUSP;
        if ("RECSUSP".equals(codeString))
          return RECSUSP;
        if ("_SolidDrugForm".equals(codeString))
          return _SOLIDDRUGFORM;
        if ("BAR".equals(codeString))
          return BAR;
        if ("BARSOAP".equals(codeString))
          return BARSOAP;
        if ("MEDBAR".equals(codeString))
          return MEDBAR;
        if ("CHEWBAR".equals(codeString))
          return CHEWBAR;
        if ("BEAD".equals(codeString))
          return BEAD;
        if ("CAKE".equals(codeString))
          return CAKE;
        if ("CEMENT".equals(codeString))
          return CEMENT;
        if ("CRYS".equals(codeString))
          return CRYS;
        if ("DISK".equals(codeString))
          return DISK;
        if ("FLAKE".equals(codeString))
          return FLAKE;
        if ("GRAN".equals(codeString))
          return GRAN;
        if ("GUM".equals(codeString))
          return GUM;
        if ("PAD".equals(codeString))
          return PAD;
        if ("MEDPAD".equals(codeString))
          return MEDPAD;
        if ("PATCH".equals(codeString))
          return PATCH;
        if ("TPATCH".equals(codeString))
          return TPATCH;
        if ("TPATH16".equals(codeString))
          return TPATH16;
        if ("TPATH24".equals(codeString))
          return TPATH24;
        if ("TPATH2WK".equals(codeString))
          return TPATH2WK;
        if ("TPATH72".equals(codeString))
          return TPATH72;
        if ("TPATHWK".equals(codeString))
          return TPATHWK;
        if ("PELLET".equals(codeString))
          return PELLET;
        if ("PILL".equals(codeString))
          return PILL;
        if ("CAP".equals(codeString))
          return CAP;
        if ("ORCAP".equals(codeString))
          return ORCAP;
        if ("ENTCAP".equals(codeString))
          return ENTCAP;
        if ("ERENTCAP".equals(codeString))
          return ERENTCAP;
        if ("ERCAP".equals(codeString))
          return ERCAP;
        if ("ERCAP12".equals(codeString))
          return ERCAP12;
        if ("ERCAP24".equals(codeString))
          return ERCAP24;
        if ("ERECCAP".equals(codeString))
          return ERECCAP;
        if ("TAB".equals(codeString))
          return TAB;
        if ("ORTAB".equals(codeString))
          return ORTAB;
        if ("BUCTAB".equals(codeString))
          return BUCTAB;
        if ("SRBUCTAB".equals(codeString))
          return SRBUCTAB;
        if ("CAPLET".equals(codeString))
          return CAPLET;
        if ("CHEWTAB".equals(codeString))
          return CHEWTAB;
        if ("CPTAB".equals(codeString))
          return CPTAB;
        if ("DISINTAB".equals(codeString))
          return DISINTAB;
        if ("DRTAB".equals(codeString))
          return DRTAB;
        if ("ECTAB".equals(codeString))
          return ECTAB;
        if ("ERECTAB".equals(codeString))
          return ERECTAB;
        if ("ERTAB".equals(codeString))
          return ERTAB;
        if ("ERTAB12".equals(codeString))
          return ERTAB12;
        if ("ERTAB24".equals(codeString))
          return ERTAB24;
        if ("ORTROCHE".equals(codeString))
          return ORTROCHE;
        if ("SLTAB".equals(codeString))
          return SLTAB;
        if ("VAGTAB".equals(codeString))
          return VAGTAB;
        if ("POWD".equals(codeString))
          return POWD;
        if ("TOPPWD".equals(codeString))
          return TOPPWD;
        if ("RECPWD".equals(codeString))
          return RECPWD;
        if ("VAGPWD".equals(codeString))
          return VAGPWD;
        if ("SUPP".equals(codeString))
          return SUPP;
        if ("RECSUPP".equals(codeString))
          return RECSUPP;
        if ("URETHSUPP".equals(codeString))
          return URETHSUPP;
        if ("VAGSUPP".equals(codeString))
          return VAGSUPP;
        if ("SWAB".equals(codeString))
          return SWAB;
        if ("MEDSWAB".equals(codeString))
          return MEDSWAB;
        if ("WAFER".equals(codeString))
          return WAFER;
        throw new Exception("Unknown V3OrderableDrugForm code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ADMINISTRABLEDRUGFORM: return "_AdministrableDrugForm";
            case APPFUL: return "APPFUL";
            case DROP: return "DROP";
            case NDROP: return "NDROP";
            case OPDROP: return "OPDROP";
            case ORDROP: return "ORDROP";
            case OTDROP: return "OTDROP";
            case PUFF: return "PUFF";
            case SCOOP: return "SCOOP";
            case SPRY: return "SPRY";
            case _DISPENSABLEDRUGFORM: return "_DispensableDrugForm";
            case _GASDRUGFORM: return "_GasDrugForm";
            case GASINHL: return "GASINHL";
            case _GASLIQUIDMIXTURE: return "_GasLiquidMixture";
            case AER: return "AER";
            case BAINHL: return "BAINHL";
            case INHLSOL: return "INHLSOL";
            case MDINHL: return "MDINHL";
            case NASSPRY: return "NASSPRY";
            case DERMSPRY: return "DERMSPRY";
            case FOAM: return "FOAM";
            case FOAMAPL: return "FOAMAPL";
            case RECFORM: return "RECFORM";
            case VAGFOAM: return "VAGFOAM";
            case VAGFOAMAPL: return "VAGFOAMAPL";
            case RECSPRY: return "RECSPRY";
            case VAGSPRY: return "VAGSPRY";
            case _GASSOLIDSPRAY: return "_GasSolidSpray";
            case INHL: return "INHL";
            case BAINHLPWD: return "BAINHLPWD";
            case INHLPWD: return "INHLPWD";
            case MDINHLPWD: return "MDINHLPWD";
            case NASINHL: return "NASINHL";
            case ORINHL: return "ORINHL";
            case PWDSPRY: return "PWDSPRY";
            case SPRYADAPT: return "SPRYADAPT";
            case _LIQUID: return "_Liquid";
            case LIQCLN: return "LIQCLN";
            case LIQSOAP: return "LIQSOAP";
            case SHMP: return "SHMP";
            case OIL: return "OIL";
            case TOPOIL: return "TOPOIL";
            case SOL: return "SOL";
            case IPSOL: return "IPSOL";
            case IRSOL: return "IRSOL";
            case DOUCHE: return "DOUCHE";
            case ENEMA: return "ENEMA";
            case OPIRSOL: return "OPIRSOL";
            case IVSOL: return "IVSOL";
            case ORALSOL: return "ORALSOL";
            case ELIXIR: return "ELIXIR";
            case RINSE: return "RINSE";
            case SYRUP: return "SYRUP";
            case RECSOL: return "RECSOL";
            case TOPSOL: return "TOPSOL";
            case LIN: return "LIN";
            case MUCTOPSOL: return "MUCTOPSOL";
            case TINC: return "TINC";
            case _LIQUIDLIQUIDEMULSION: return "_LiquidLiquidEmulsion";
            case CRM: return "CRM";
            case NASCRM: return "NASCRM";
            case OPCRM: return "OPCRM";
            case ORCRM: return "ORCRM";
            case OTCRM: return "OTCRM";
            case RECCRM: return "RECCRM";
            case TOPCRM: return "TOPCRM";
            case VAGCRM: return "VAGCRM";
            case VAGCRMAPL: return "VAGCRMAPL";
            case LTN: return "LTN";
            case TOPLTN: return "TOPLTN";
            case OINT: return "OINT";
            case NASOINT: return "NASOINT";
            case OINTAPL: return "OINTAPL";
            case OPOINT: return "OPOINT";
            case OTOINT: return "OTOINT";
            case RECOINT: return "RECOINT";
            case TOPOINT: return "TOPOINT";
            case VAGOINT: return "VAGOINT";
            case VAGOINTAPL: return "VAGOINTAPL";
            case _LIQUIDSOLIDSUSPENSION: return "_LiquidSolidSuspension";
            case GEL: return "GEL";
            case GELAPL: return "GELAPL";
            case NASGEL: return "NASGEL";
            case OPGEL: return "OPGEL";
            case OTGEL: return "OTGEL";
            case TOPGEL: return "TOPGEL";
            case URETHGEL: return "URETHGEL";
            case VAGGEL: return "VAGGEL";
            case VGELAPL: return "VGELAPL";
            case PASTE: return "PASTE";
            case PUD: return "PUD";
            case TPASTE: return "TPASTE";
            case SUSP: return "SUSP";
            case ITSUSP: return "ITSUSP";
            case OPSUSP: return "OPSUSP";
            case ORSUSP: return "ORSUSP";
            case ERSUSP: return "ERSUSP";
            case ERSUSP12: return "ERSUSP12";
            case ERSUSP24: return "ERSUSP24";
            case OTSUSP: return "OTSUSP";
            case RECSUSP: return "RECSUSP";
            case _SOLIDDRUGFORM: return "_SolidDrugForm";
            case BAR: return "BAR";
            case BARSOAP: return "BARSOAP";
            case MEDBAR: return "MEDBAR";
            case CHEWBAR: return "CHEWBAR";
            case BEAD: return "BEAD";
            case CAKE: return "CAKE";
            case CEMENT: return "CEMENT";
            case CRYS: return "CRYS";
            case DISK: return "DISK";
            case FLAKE: return "FLAKE";
            case GRAN: return "GRAN";
            case GUM: return "GUM";
            case PAD: return "PAD";
            case MEDPAD: return "MEDPAD";
            case PATCH: return "PATCH";
            case TPATCH: return "TPATCH";
            case TPATH16: return "TPATH16";
            case TPATH24: return "TPATH24";
            case TPATH2WK: return "TPATH2WK";
            case TPATH72: return "TPATH72";
            case TPATHWK: return "TPATHWK";
            case PELLET: return "PELLET";
            case PILL: return "PILL";
            case CAP: return "CAP";
            case ORCAP: return "ORCAP";
            case ENTCAP: return "ENTCAP";
            case ERENTCAP: return "ERENTCAP";
            case ERCAP: return "ERCAP";
            case ERCAP12: return "ERCAP12";
            case ERCAP24: return "ERCAP24";
            case ERECCAP: return "ERECCAP";
            case TAB: return "TAB";
            case ORTAB: return "ORTAB";
            case BUCTAB: return "BUCTAB";
            case SRBUCTAB: return "SRBUCTAB";
            case CAPLET: return "CAPLET";
            case CHEWTAB: return "CHEWTAB";
            case CPTAB: return "CPTAB";
            case DISINTAB: return "DISINTAB";
            case DRTAB: return "DRTAB";
            case ECTAB: return "ECTAB";
            case ERECTAB: return "ERECTAB";
            case ERTAB: return "ERTAB";
            case ERTAB12: return "ERTAB12";
            case ERTAB24: return "ERTAB24";
            case ORTROCHE: return "ORTROCHE";
            case SLTAB: return "SLTAB";
            case VAGTAB: return "VAGTAB";
            case POWD: return "POWD";
            case TOPPWD: return "TOPPWD";
            case RECPWD: return "RECPWD";
            case VAGPWD: return "VAGPWD";
            case SUPP: return "SUPP";
            case RECSUPP: return "RECSUPP";
            case URETHSUPP: return "URETHSUPP";
            case VAGSUPP: return "VAGSUPP";
            case SWAB: return "SWAB";
            case MEDSWAB: return "MEDSWAB";
            case WAFER: return "WAFER";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/orderableDrugForm";
        }
        public String getDefinition() {
          switch (this) {
            case _ADMINISTRABLEDRUGFORM: return "AdministrableDrugForm";
            case APPFUL: return "Applicatorful";
            case DROP: return "Drops";
            case NDROP: return "Nasal Drops";
            case OPDROP: return "Ophthalmic Drops";
            case ORDROP: return "Oral Drops";
            case OTDROP: return "Otic Drops";
            case PUFF: return "Puff";
            case SCOOP: return "Scoops";
            case SPRY: return "Sprays";
            case _DISPENSABLEDRUGFORM: return "DispensableDrugForm";
            case _GASDRUGFORM: return "Any elastic aeriform fluid in which the molecules are separated from one another and have free paths.";
            case GASINHL: return "Gas for Inhalation";
            case _GASLIQUIDMIXTURE: return "GasLiquidMixture";
            case AER: return "Aerosol";
            case BAINHL: return "Breath Activated Inhaler";
            case INHLSOL: return "Inhalant Solution";
            case MDINHL: return "Metered Dose Inhaler";
            case NASSPRY: return "Nasal Spray";
            case DERMSPRY: return "Dermal Spray";
            case FOAM: return "Foam";
            case FOAMAPL: return "Foam with Applicator";
            case RECFORM: return "Rectal foam";
            case VAGFOAM: return "Vaginal foam";
            case VAGFOAMAPL: return "Vaginal foam with applicator";
            case RECSPRY: return "Rectal Spray";
            case VAGSPRY: return "Vaginal Spray";
            case _GASSOLIDSPRAY: return "GasSolidSpray";
            case INHL: return "Inhalant";
            case BAINHLPWD: return "Breath Activated Powder Inhaler";
            case INHLPWD: return "Inhalant Powder";
            case MDINHLPWD: return "Metered Dose Powder Inhaler";
            case NASINHL: return "Nasal Inhalant";
            case ORINHL: return "Oral Inhalant";
            case PWDSPRY: return "Powder Spray";
            case SPRYADAPT: return "Spray with Adaptor";
            case _LIQUID: return "A state of substance that is an intermediate one entered into as matter goes from solid to gas; liquids are also intermediate in that they have neither the orderliness of a crystal nor the randomness of a gas. (Note: This term should not be used to describe solutions, only pure chemicals in their liquid state.)";
            case LIQCLN: return "Liquid Cleanser";
            case LIQSOAP: return "Medicated Liquid Soap";
            case SHMP: return "A liquid soap or detergent used to clean the hair and scalp and is often used as a vehicle for dermatologic agents.";
            case OIL: return "An unctuous, combustible substance which is liquid, or easily liquefiable, on warming, and is soluble in ether but insoluble in water. Such substances, depending on their origin, are classified as animal, mineral, or vegetable oils.";
            case TOPOIL: return "Topical Oil";
            case SOL: return "A liquid preparation that contains one or more chemical substances dissolved, i.e., molecularly dispersed, in a suitable solvent or mixture of mutually miscible solvents.";
            case IPSOL: return "Intraperitoneal Solution";
            case IRSOL: return "A sterile solution intended to bathe or flush open wounds or body cavities; they're used topically, never parenterally.";
            case DOUCHE: return "A liquid preparation, intended for the irrigative cleansing of the vagina, that is prepared from powders, liquid solutions, or liquid concentrates and contains one or more chemical substances dissolved in a suitable solvent or mutually miscible solvents.";
            case ENEMA: return "A rectal preparation for therapeutic, diagnostic, or nutritive purposes.";
            case OPIRSOL: return "Ophthalmic Irrigation Solution";
            case IVSOL: return "Intravenous Solution";
            case ORALSOL: return "Oral Solution";
            case ELIXIR: return "A clear, pleasantly flavored, sweetened hydroalcoholic liquid containing dissolved medicinal agents; it is intended for oral use.";
            case RINSE: return "An aqueous solution which is most often used for its deodorant, refreshing, or antiseptic effect.";
            case SYRUP: return "An oral solution containing high concentrations of sucrose or other sugars; the term has also been used to include any other liquid dosage form prepared in a sweet and viscid vehicle, including oral suspensions.";
            case RECSOL: return "Rectal Solution";
            case TOPSOL: return "Topical Solution";
            case LIN: return "A solution or mixture of various substances in oil, alcoholic solutions of soap, or emulsions intended for external application.";
            case MUCTOPSOL: return "Mucous Membrane Topical Solution";
            case TINC: return "Tincture";
            case _LIQUIDLIQUIDEMULSION: return "A two-phase system in which one liquid is dispersed throughout another liquid in the form of small droplets.";
            case CRM: return "A semisolid dosage form containing one or more drug substances dissolved or dispersed in a suitable base; more recently, the term has been restricted to products consisting of oil-in-water emulsions or aqueous microcrystalline dispersions of long chain fatty acids or alcohols that are water washable and more cosmetically and aesthetically acceptable.";
            case NASCRM: return "Nasal Cream";
            case OPCRM: return "Ophthalmic Cream";
            case ORCRM: return "Oral Cream";
            case OTCRM: return "Otic Cream";
            case RECCRM: return "Rectal Cream";
            case TOPCRM: return "Topical Cream";
            case VAGCRM: return "Vaginal Cream";
            case VAGCRMAPL: return "Vaginal Cream with Applicator";
            case LTN: return "The term \"lotion\" has been used to categorize many topical suspensions, solutions and emulsions intended for application to the skin.";
            case TOPLTN: return "Topical Lotion";
            case OINT: return "A semisolid preparation intended for external application to the skin or mucous membranes.";
            case NASOINT: return "Nasal Ointment";
            case OINTAPL: return "Ointment with Applicator";
            case OPOINT: return "Ophthalmic Ointment";
            case OTOINT: return "Otic Ointment";
            case RECOINT: return "Rectal Ointment";
            case TOPOINT: return "Topical Ointment";
            case VAGOINT: return "Vaginal Ointment";
            case VAGOINTAPL: return "Vaginal Ointment with Applicator";
            case _LIQUIDSOLIDSUSPENSION: return "A liquid preparation which consists of solid particles dispersed throughout a liquid phase in which the particles are not soluble.";
            case GEL: return "A semisolid system consisting of either suspensions made up of small inorganic particles or large organic molecules interpenetrated by a liquid.";
            case GELAPL: return "Gel with Applicator";
            case NASGEL: return "Nasal Gel";
            case OPGEL: return "Ophthalmic Gel";
            case OTGEL: return "Otic Gel";
            case TOPGEL: return "Topical Gel";
            case URETHGEL: return "Urethral Gel";
            case VAGGEL: return "Vaginal Gel";
            case VGELAPL: return "Vaginal Gel with Applicator";
            case PASTE: return "A semisolid dosage form that contains one or more drug substances intended for topical application.";
            case PUD: return "Pudding";
            case TPASTE: return "A paste formulation intended to clean and/or polish the teeth, and which may contain certain additional agents.";
            case SUSP: return "Suspension";
            case ITSUSP: return "Intrathecal Suspension";
            case OPSUSP: return "Ophthalmic Suspension";
            case ORSUSP: return "Oral Suspension";
            case ERSUSP: return "Extended-Release Suspension";
            case ERSUSP12: return "12 Hour Extended-Release Suspension";
            case ERSUSP24: return "24 Hour Extended Release Suspension";
            case OTSUSP: return "Otic Suspension";
            case RECSUSP: return "Rectal Suspension";
            case _SOLIDDRUGFORM: return "SolidDrugForm";
            case BAR: return "Bar";
            case BARSOAP: return "Bar Soap";
            case MEDBAR: return "Medicated Bar Soap";
            case CHEWBAR: return "A solid dosage form usually in the form of a rectangle that is meant to be chewed.";
            case BEAD: return "A solid dosage form in the shape of a small ball.";
            case CAKE: return "Cake";
            case CEMENT: return "A substance that serves to produce solid union between two surfaces.";
            case CRYS: return "A naturally produced angular solid of definite form in which the ultimate units from which it is built up are systematically arranged; they are usually evenly spaced on a regular space lattice.";
            case DISK: return "A circular plate-like organ or structure.";
            case FLAKE: return "Flakes";
            case GRAN: return "A small particle or grain.";
            case GUM: return "A sweetened and flavored insoluble plastic material of various shapes which when chewed, releases a drug substance into the oral cavity.";
            case PAD: return "Pad";
            case MEDPAD: return "Medicated Pad";
            case PATCH: return "A drug delivery system that contains an adhesived backing and that permits its ingredients to diffuse from some portion of it (e.g. the backing itself, a reservoir, the adhesive, or some other component) into the body from the external site where it is applied.";
            case TPATCH: return "Transdermal Patch";
            case TPATH16: return "16 Hour Transdermal Patch";
            case TPATH24: return "24 Hour Transdermal Patch";
            case TPATH2WK: return "Biweekly Transdermal Patch";
            case TPATH72: return "72 Hour Transdermal Patch";
            case TPATHWK: return "Weekly Transdermal Patch";
            case PELLET: return "A small sterile solid mass consisting of a highly purified drug (with or without excipients) made by the formation of granules, or by compression and molding.";
            case PILL: return "A small, round solid dosage form containing a medicinal agent intended for oral administration.";
            case CAP: return "A solid dosage form in which the drug is enclosed within either a hard or soft soluble container or \"shell\" made from a suitable form of gelatin.";
            case ORCAP: return "Oral Capsule";
            case ENTCAP: return "Enteric Coated Capsule";
            case ERENTCAP: return "Extended Release Enteric Coated Capsule";
            case ERCAP: return "A solid dosage form in which the drug is enclosed within either a hard or soft soluble container made from a suitable form of gelatin, and which releases a drug (or drugs) in such a manner to allow a reduction in dosing frequency as compared to that drug (or drugs) presented as a conventional dosage form.";
            case ERCAP12: return "12 Hour Extended Release Capsule";
            case ERCAP24: return "24 Hour Extended Release Capsule";
            case ERECCAP: return "Rationale: Duplicate of code ERENTCAP. Use code ERENTCAP instead.";
            case TAB: return "A solid dosage form containing medicinal substances with or without suitable diluents.";
            case ORTAB: return "Oral Tablet";
            case BUCTAB: return "Buccal Tablet";
            case SRBUCTAB: return "Sustained Release Buccal Tablet";
            case CAPLET: return "Caplet";
            case CHEWTAB: return "A solid dosage form containing medicinal substances with or without suitable diluents that is intended to be chewed, producing a pleasant tasting residue in the oral cavity that is easily swallowed and does not leave a bitter or unpleasant after-taste.";
            case CPTAB: return "Coated Particles Tablet";
            case DISINTAB: return "A solid dosage form containing medicinal substances which disintegrates rapidly, usually within a matter of seconds, when placed upon the tongue.";
            case DRTAB: return "Delayed Release Tablet";
            case ECTAB: return "Enteric Coated Tablet";
            case ERECTAB: return "Extended Release Enteric Coated Tablet";
            case ERTAB: return "A solid dosage form containing a drug which allows at least a reduction in dosing frequency as compared to that drug presented in conventional dosage form.";
            case ERTAB12: return "12 Hour Extended Release Tablet";
            case ERTAB24: return "24 Hour Extended Release Tablet";
            case ORTROCHE: return "A solid preparation containing one or more medicaments, usually in a flavored, sweetened base which is intended to dissolve or disintegrate slowly in the mouth.";
            case SLTAB: return "Sublingual Tablet";
            case VAGTAB: return "Vaginal Tablet";
            case POWD: return "An intimate mixture of dry, finely divided drugs and/or chemicals that may be intended for internal or external use.";
            case TOPPWD: return "Topical Powder";
            case RECPWD: return "Rectal Powder";
            case VAGPWD: return "Vaginal Powder";
            case SUPP: return "A solid body of various weights and shapes, adapted for introduction into the rectal, vaginal, or urethral orifice of the human body; they usually melt, soften, or dissolve at body temperature.";
            case RECSUPP: return "Rectal Suppository";
            case URETHSUPP: return "Urethral suppository";
            case VAGSUPP: return "Vaginal Suppository";
            case SWAB: return "A wad of absorbent material usually wound around one end of a small stick and used for applying medication or for removing material from an area.";
            case MEDSWAB: return "Medicated swab";
            case WAFER: return "A thin slice of material containing a medicinal agent.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ADMINISTRABLEDRUGFORM: return "AdministrableDrugForm";
            case APPFUL: return "Applicatorful";
            case DROP: return "Drops";
            case NDROP: return "Nasal Drops";
            case OPDROP: return "Ophthalmic Drops";
            case ORDROP: return "Oral Drops";
            case OTDROP: return "Otic Drops";
            case PUFF: return "Puff";
            case SCOOP: return "Scoops";
            case SPRY: return "Sprays";
            case _DISPENSABLEDRUGFORM: return "DispensableDrugForm";
            case _GASDRUGFORM: return "GasDrugForm";
            case GASINHL: return "Gas for Inhalation";
            case _GASLIQUIDMIXTURE: return "GasLiquidMixture";
            case AER: return "Aerosol";
            case BAINHL: return "Breath Activated Inhaler";
            case INHLSOL: return "Inhalant Solution";
            case MDINHL: return "Metered Dose Inhaler";
            case NASSPRY: return "Nasal Spray";
            case DERMSPRY: return "Dermal Spray";
            case FOAM: return "Foam";
            case FOAMAPL: return "Foam with Applicator";
            case RECFORM: return "Rectal foam";
            case VAGFOAM: return "Vaginal foam";
            case VAGFOAMAPL: return "Vaginal foam with applicator";
            case RECSPRY: return "Rectal Spray";
            case VAGSPRY: return "Vaginal Spray";
            case _GASSOLIDSPRAY: return "GasSolidSpray";
            case INHL: return "Inhalant";
            case BAINHLPWD: return "Breath Activated Powder Inhaler";
            case INHLPWD: return "Inhalant Powder";
            case MDINHLPWD: return "Metered Dose Powder Inhaler";
            case NASINHL: return "Nasal Inhalant";
            case ORINHL: return "Oral Inhalant";
            case PWDSPRY: return "Powder Spray";
            case SPRYADAPT: return "Spray with Adaptor";
            case _LIQUID: return "Liquid";
            case LIQCLN: return "Liquid Cleanser";
            case LIQSOAP: return "Medicated Liquid Soap";
            case SHMP: return "Shampoo";
            case OIL: return "Oil";
            case TOPOIL: return "Topical Oil";
            case SOL: return "Solution";
            case IPSOL: return "Intraperitoneal Solution";
            case IRSOL: return "Irrigation Solution";
            case DOUCHE: return "Douche";
            case ENEMA: return "Enema";
            case OPIRSOL: return "Ophthalmic Irrigation Solution";
            case IVSOL: return "Intravenous Solution";
            case ORALSOL: return "Oral Solution";
            case ELIXIR: return "Elixir";
            case RINSE: return "Mouthwash/Rinse";
            case SYRUP: return "Syrup";
            case RECSOL: return "Rectal Solution";
            case TOPSOL: return "Topical Solution";
            case LIN: return "Liniment";
            case MUCTOPSOL: return "Mucous Membrane Topical Solution";
            case TINC: return "Tincture";
            case _LIQUIDLIQUIDEMULSION: return "LiquidLiquidEmulsion";
            case CRM: return "Cream";
            case NASCRM: return "Nasal Cream";
            case OPCRM: return "Ophthalmic Cream";
            case ORCRM: return "Oral Cream";
            case OTCRM: return "Otic Cream";
            case RECCRM: return "Rectal Cream";
            case TOPCRM: return "Topical Cream";
            case VAGCRM: return "Vaginal Cream";
            case VAGCRMAPL: return "Vaginal Cream with Applicator";
            case LTN: return "Lotion";
            case TOPLTN: return "Topical Lotion";
            case OINT: return "Ointment";
            case NASOINT: return "Nasal Ointment";
            case OINTAPL: return "Ointment with Applicator";
            case OPOINT: return "Ophthalmic Ointment";
            case OTOINT: return "Otic Ointment";
            case RECOINT: return "Rectal Ointment";
            case TOPOINT: return "Topical Ointment";
            case VAGOINT: return "Vaginal Ointment";
            case VAGOINTAPL: return "Vaginal Ointment with Applicator";
            case _LIQUIDSOLIDSUSPENSION: return "LiquidSolidSuspension";
            case GEL: return "Gel";
            case GELAPL: return "Gel with Applicator";
            case NASGEL: return "Nasal Gel";
            case OPGEL: return "Ophthalmic Gel";
            case OTGEL: return "Otic Gel";
            case TOPGEL: return "Topical Gel";
            case URETHGEL: return "Urethral Gel";
            case VAGGEL: return "Vaginal Gel";
            case VGELAPL: return "Vaginal Gel with Applicator";
            case PASTE: return "Paste";
            case PUD: return "Pudding";
            case TPASTE: return "Toothpaste";
            case SUSP: return "Suspension";
            case ITSUSP: return "Intrathecal Suspension";
            case OPSUSP: return "Ophthalmic Suspension";
            case ORSUSP: return "Oral Suspension";
            case ERSUSP: return "Extended-Release Suspension";
            case ERSUSP12: return "12 Hour Extended-Release Suspension";
            case ERSUSP24: return "24 Hour Extended Release Suspension";
            case OTSUSP: return "Otic Suspension";
            case RECSUSP: return "Rectal Suspension";
            case _SOLIDDRUGFORM: return "SolidDrugForm";
            case BAR: return "Bar";
            case BARSOAP: return "Bar Soap";
            case MEDBAR: return "Medicated Bar Soap";
            case CHEWBAR: return "Chewable Bar";
            case BEAD: return "Beads";
            case CAKE: return "Cake";
            case CEMENT: return "Cement";
            case CRYS: return "Crystals";
            case DISK: return "Disk";
            case FLAKE: return "Flakes";
            case GRAN: return "Granules";
            case GUM: return "ChewingGum";
            case PAD: return "Pad";
            case MEDPAD: return "Medicated Pad";
            case PATCH: return "Patch";
            case TPATCH: return "Transdermal Patch";
            case TPATH16: return "16 Hour Transdermal Patch";
            case TPATH24: return "24 Hour Transdermal Patch";
            case TPATH2WK: return "Biweekly Transdermal Patch";
            case TPATH72: return "72 Hour Transdermal Patch";
            case TPATHWK: return "Weekly Transdermal Patch";
            case PELLET: return "Pellet";
            case PILL: return "Pill";
            case CAP: return "Capsule";
            case ORCAP: return "Oral Capsule";
            case ENTCAP: return "Enteric Coated Capsule";
            case ERENTCAP: return "Extended Release Enteric Coated Capsule";
            case ERCAP: return "Extended Release Capsule";
            case ERCAP12: return "12 Hour Extended Release Capsule";
            case ERCAP24: return "24 Hour Extended Release Capsule";
            case ERECCAP: return "Extended Release Enteric Coated Capsule";
            case TAB: return "Tablet";
            case ORTAB: return "Oral Tablet";
            case BUCTAB: return "Buccal Tablet";
            case SRBUCTAB: return "Sustained Release Buccal Tablet";
            case CAPLET: return "Caplet";
            case CHEWTAB: return "Chewable Tablet";
            case CPTAB: return "Coated Particles Tablet";
            case DISINTAB: return "Disintegrating Tablet";
            case DRTAB: return "Delayed Release Tablet";
            case ECTAB: return "Enteric Coated Tablet";
            case ERECTAB: return "Extended Release Enteric Coated Tablet";
            case ERTAB: return "Extended Release Tablet";
            case ERTAB12: return "12 Hour Extended Release Tablet";
            case ERTAB24: return "24 Hour Extended Release Tablet";
            case ORTROCHE: return "Lozenge/Oral Troche";
            case SLTAB: return "Sublingual Tablet";
            case VAGTAB: return "Vaginal Tablet";
            case POWD: return "Powder";
            case TOPPWD: return "Topical Powder";
            case RECPWD: return "Rectal Powder";
            case VAGPWD: return "Vaginal Powder";
            case SUPP: return "Suppository";
            case RECSUPP: return "Rectal Suppository";
            case URETHSUPP: return "Urethral suppository";
            case VAGSUPP: return "Vaginal Suppository";
            case SWAB: return "Swab";
            case MEDSWAB: return "Medicated swab";
            case WAFER: return "Wafer";
            default: return "?";
          }
    }


}

