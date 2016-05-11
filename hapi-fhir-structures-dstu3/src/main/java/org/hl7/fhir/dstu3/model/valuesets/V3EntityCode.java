package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EntityCode {

        /**
         * Types of Material for EntityClass "MAT"
         */
        _MATERIALENTITYCLASSTYPE, 
        /**
         * Material intended to hold another material for purpose of storage or transport.
         */
        _CONTAINERENTITYTYPE, 
        /**
         * A material intended to hold other materials for purposes of storage or transportation
         */
        PKG, 
        /**
         * A container having dimensions that adjust somewhat based on the amount and shape of the material placed within it.
         */
        _NONRIGIDCONTAINERENTITYTYPE, 
        /**
         * A pouched or pendulous container.
         */
        BAG, 
        /**
         * A paper
         */
        PACKT, 
        /**
         * A small bag or container made of a soft material.
         */
        PCH, 
        /**
         * A small bag or packet containing a small portion of a substance.
         */
        SACH, 
        /**
         * A container having a fixed and inflexible dimensions and volume
         */
        _RIGIDCONTAINERENTITYTYPE, 
        /**
         * Container intended to contain sufficient material for only one use.
         */
        _INDIVIDUALPACKAGEENTITYTYPE, 
        /**
         * A small sealed glass container that holds a measured amount of a medicinal substance.
         */
        AMP, 
        /**
         * Individually dosed ophthalmic solution.  One time eye dropper dispenser.
         */
        MINIM, 
        /**
         * Individually dosed inhalation solution.
         */
        NEBAMP, 
        /**
         * A container either glass or plastic and a narrow neck, for storing liquid.
         */
        OVUL, 
        /**
         * A container intended to contain sufficient material for more than one use.  (I.e. Material is intended to be removed from the container at more than one discrete time period.)
         */
        _MULTIUSECONTAINERENTITYTYPE, 
        /**
         * A container, typically rounded, either glass or plastic with a narrow neck and capable of storing liquid.
         */
        BOT, 
        /**
         * A bottle of yellow to brown color.  Used to store light-sensitive materials
         */
        BOTA, 
        /**
         * A bottle with a cap designed to release the contained liquid in droplets of a specific size.
         */
        BOTD, 
        /**
         * A bottle made of glass
         */
        BOTG, 
        /**
         * A bottle made of plastic
         */
        BOTP, 
        /**
         * A bottle made of polyethylene
         */
        BOTPLY, 
        /**
         * A 6-sided container commonly made from paper or cardboard used for solid forms.
         */
        BOX, 
        /**
         * A metal container in which a material is hermetically sealed to enable storage over long periods.
         */
        CAN, 
        /**
         * A sealed container of liquid or powder intended to be loaded into a device.
         */
        CART, 
        /**
         * A pressurized metal container holding a substance released as a spray or aerosol.
         */
        CNSTR, 
        /**
         * A container of glass, earthenware, plastic, etc.  Top of the container has a diameter of similar size to the diameter of the container as a whole
         */
        JAR, 
        /**
         * A deep vessel  for holding liquids, with a handle and often with a spout or lip shape for pouring.
         */
        JUG, 
        /**
         * A lidded container made of thin sheet metal.
         */
        TIN, 
        /**
         * An open flat bottomed round container.
         */
        TUB, 
        /**
         * A long hollow rigid or flexible cylinder.  Material is extruded by squeezing the container.
         */
        TUBE, 
        /**
         * A small cylindrical glass for holding liquid medicines.
         */
        VIAL, 
        /**
         * A bubblepack.  Medications sealed individually, separated into doses.
         */
        BLSTRPK, 
        /**
         * A bubble pack card.  Multiple individual/separated doses.
         */
        CARD, 
        /**
         * A container intended to contain sufficient material for more than one use, but grouped or organized to provide individual access to sufficient material for a single use.  Often used to ensure that the proper type and amount of material is consumed/expended for each use.
         */
        COMPPKG, 
        /**
         * Rotatable dispenser.  Eg. Birth control package.
         */
        DIALPK, 
        /**
         * Object that is thin, flat, and circular.  Doses of medication often contained in bubbles on the disk.
         */
        DISK, 
        /**
         * Special packaging that will help patients take their medications on a regular basis.
         */
        DOSET, 
        /**
         * A continuous strip of plastic sectioned into individual pouches, each one containing the quantity of 1 or more medications intended to be administered at a specific time
         */
        STRIP, 
        /**
         * A container for a diverse collection of products intended to be used together for some purpose (e.g. Medicinal kits often contain a syringe, a needle and the injectable medication).
         */
        KIT, 
        /**
         * A kit in which the components are interconnected.
         */
        SYSTM, 
        /**
         * A device with direct or indirect therapeutic purpose.  Values for EntityCode when EntityClass = "DEV"
         */
        _MEDICALDEVICE, 
        /**
         * A device used to allow access to a part of a body
         */
        _ACCESSMEDICALDEVICE, 
        /**
         * A hollow tube used to administer a substance into a vein, artery or body cavity
         */
        LINE, 
        /**
         * A line used to administer a substance into an artery
         */
        IALINE, 
        /**
         * A line used to administer a substance into a vein
         */
        IVLINE, 
        /**
         * A device intended to administer a substance to a subject
         */
        _ADMINISTRATIONMEDICALDEVICE, 
        /**
         * A device intended to administer liquid into a subject via a
         */
        _INJECTIONMEDICALDEVICE, 
        /**
         * Automatically injects medication.
         */
        AINJ, 
        /**
         * A device which can contain a cartridge for injection purposes.  Eg. Insulin pen.
         */
        PEN, 
        /**
         * A barrel with a plunger.
         */
        SYR, 
        /**
         * A device used to apply a liquid or powder to a surface.
         */
        APLCTR, 
        /**
         * A small device used for inhaling medicine in the form of a vapour or gas in order to ease a respiratory condition such as asthma or to relieve nasal congestion.
         */
        INH, 
        /**
         * The device used to inhale the doses of medication contained in the disk form.
         */
        DSKS, 
        /**
         * The device used to inhale the doses of medication contained in the disk form.
         */
        DSKUNH, 
        /**
         * Asthma medication delivery device.
         */
        TRBINH, 
        /**
         * A device that is used to raise, compress, or transfer liquids or gases and is operated by a piston or similar mechanism.
         */
        PMP, 
        /**
         * Set of codes related to specimen additives
         */
        _SPECIMENADDITIVEENTITY, 
        /**
         * ACD Solution A of trisodium citrate, 22.0g/L; citric acid, 8.0 g/L; and dextrose 24.5 g/L. Used in Blood banking and histocompatibilty testing
         */
        ACDA, 
        /**
         * ACD Solution B of trisodium citrate, 13.2g/L; citric acid, 4.8 g/L; and dextrose 14.7 g/L. Used in Blood banking and histocompatibilty testing.
         */
        ACDB, 
        /**
         * 50% V/V acetic acid in water.  Used as  a urine preservative
         */
        ACET, 
        /**
         * Sodium Chloride 3.0g, Potassium Chloride 0.2g, Calcium Chloride 0.1g, Magnesium Chloride 0.1g, Monopotassium Phosphate 0.2g, Disodium Phosphate 1.15g, Sodium Thiogly collate 1.0g, Distilled Water 1 liter
         */
        AMIES, 
        /**
         * Any medium used to maintain bacterial viability (e.g. Stuart's, Cary-Blair, Amies)
         */
        BACTM, 
        /**
         * Formaldehyde 4% w/v; methyl alcohol 1% w/v; phosphate buffering salts. Tissue preservative
         */
        BF10, 
        /**
         * Powdered boric acid (usually 10 g) added to 24-hour urine collections as a preservative.
         */
        BOR, 
        /**
         * Picric acid, saturated aqueous solution (750.0 ml), 37-40% formalin (250.0 ml), glacial acetic acid (50.0 ml). Tissue preservative.
         */
        BOUIN, 
        /**
         * 50% skim milk in 0.01 M phosphate-buffered saline.  Maintain virus viability
         */
        BSKM, 
        /**
         * A 3.2% solution of Sodium Citrate in water.  Used as a blood preservative
         */
        C32, 
        /**
         * A 3.8% solution of Sodium Citrate in water. Used as a blood preservative
         */
        C38, 
        /**
         * A modification of buffered 10% formalin used as a general tissue preservative.
         */
        CARS, 
        /**
         * Sodium Thioglycollate 1.5 g, Disodium Hydrogen Phosphate 1.1 g, Sodium Chloride 5.0 g, Calcium Chloride 0.09 g, Agar 5.0 g, per Liter of Water
         */
        CARY, 
        /**
         * Any of a number of non-nutritive buffered media used to maintain Chlamydia viability during transportation to the laboratory
         */
        CHLTM, 
        /**
         * Buffered tri-sodium citrate solution with theophylline, adenosine and dipyridamole
         */
        CTAD, 
        /**
         * Potassium EDTA 15% solution in water
         */
        EDTK15, 
        /**
         * Potassium EDTA 7.5% solution in water
         */
        EDTK75, 
        /**
         * Sodium fluoride and Disodium EDTA
         */
        EDTN, 
        /**
         * Any of a number of non-nutritive buffered media used to maintain enteric bacterial viability during transportation to the laboratory
         */
        ENT, 
        /**
         * A 10% v/v solution in water of formalin( a 37% solution of formaldehyde and water).  Used for tissue preservation.
         */
        F10, 
        /**
         * Thrombin plus soybean trypsin inhibitor.  For use in identifying fibrn degredation products.
         */
        FDP, 
        /**
         * Sodium fluoride, 10mg added as a urine preservative.
         */
        FL10, 
        /**
         * Sodium fluoride, 100mg added as a urine preservative.
         */
        FL100, 
        /**
         * A solution of HCl containing 6moles of hydrogen ion/L. Used as a Urine Preservative.
         */
        HCL6, 
        /**
         * Ammonium heparin
         */
        HEPA, 
        /**
         * Lithium heparin salt
         */
        HEPL, 
        /**
         * Sodium heparin salt
         */
        HEPN, 
        /**
         * 6N Nitric acid used to preserve urine for heavy metal analysis.
         */
        HNO3, 
        /**
         * A transport medium formulated to maintain Bordetella pertussis viability.
         */
        JKM, 
        /**
         * 5% Glutaraldehyde, 4% Formaldehyde in 0.08M buffer. Tissue preservation
         */
        KARN, 
        /**
         * Potassium oxalate and sodium fluoride in a 1.25:1 ratio
         */
        KOX, 
        /**
         * Iodoacetate lithium salt
         */
        LIA, 
        /**
         * Modified Hank's balanced salt solution supplemented with bovine serum albumin, gelatin, sucrose and glutamic acid. It is buffered to pH 7.3+ or - 0.2 with HEPES buffer. Phenol red is used to indicate pH. Vancomycin, Amphotericin B and Colistin are used to
         */
        M4, 
        /**
         * Modified Hank's balanced salt solution supplemented with bovine serum albumin, gelatin, sucrose and glutamic acid. It is buffered to pH 7.3+ or - 0.2 with Hepes buffer. Phenol red is used to indicate pH. Gentamicin and amphotericin B are used to inhibit c
         */
        M4RT, 
        /**
         * Modified Hank's balanced salt solution supplemented with protein stabilizers, sucrose and glutamic acid. It is buffered to pH 7.3+ or - 0.2 with Hepes buffer. Phenol red is used to indicate pH. Vancomycin, Amphotericin B and Colistin are used to inhibit c
         */
        M5, 
        /**
         * 1M potassium citrate, pH 7.0 2.5 ml, 0.1M magnesium sulfate 5.0 ml, 0.1M N-ethyl malemide  5.0 ml, dH2O 87.5 ml, ammonium sulfate 55gm. Preserve antigens for Immunofluorescence procedures
         */
        MICHTM, 
        /**
         * A buffered medium with ammonium sulfate added to preserve antigens for Immunofluorescence procedures
         */
        MMDTM, 
        /**
         * Sodium fluoride
         */
        NAF, 
        /**
         * No additive. Specifically identifes the specimen as having no additives.
         */
        NONE, 
        /**
         * 0.12 g NaCl, 0.004 g MgSO, 0.004 g, CaCl, 0.142 g Na2HPO4 and 0.136 g KH2PO4 per liter of distilled water. Maintain Acanthaoemba viability.
         */
        PAGE, 
        /**
         * Phenol. Urine preservative
         */
        PHENOL, 
        /**
         * Polyvinyl alcohol
         */
        PVA, 
        /**
         * A transport medium formulated to maintain Bordetella pertussis viability.
         */
        RLM, 
        /**
         * Diatomaceous earth. For glucose determination blood samples
         */
        SILICA, 
        /**
         * Sodium polyanethol sulfonate in saline. Anticomplementary and antiphagocytic properties. Used in blood culture collection.
         */
        SPS, 
        /**
         * Polymer separator gel with clot activator
         */
        SST, 
        /**
         * Sodium Glycerophosphate 10.0g, Calcium Chloride 0.1g, Mercaptoacetic Acid 1.0ml, Distilled Water 1 liter
         */
        STUTM, 
        /**
         * Thrombin. Accelerates clotting.
         */
        THROM, 
        /**
         * 2-Isopropyl-5-methyl phenol. A preservative for 24 Hr Urine samples
         */
        THYMOL, 
        /**
         * A nutritive medium with a reducing agent  (sodium thioglycolate) which, due to a chemical reaction, removes oxygen from the broth.
         */
        THYO, 
        /**
         * Also known as Methylbenzene; Toluol; Phenylmethane. A preservative for 24 Hr Urine samples
         */
        TOLU, 
        /**
         * A buffered salt solution with antifungal agents added for the collection and transport of Ureaplasma specimens.
         */
        URETM, 
        /**
         * Sucrose 74.6g, Potassium hydrogenphosphate 0.52g, L-glutamic acid 0.72g, Bovine serum albumin 5.0g, Gentamicin 50mg, Potassium dihydrogenphosphate 1.25g, L-15 medium 9.9L, Water to 10L. Maintain Virus viability.
         */
        VIRTM, 
        /**
         * 3.8% Citrate buffered to a pH of 5.5 for Westergren Sedimentation Rate
         */
        WEST, 
        /**
         * A manufactured product that is produced from the raw blood oi a donor with the intention of using it in a recipient transfusion.
         */
        BLDPRD, 
        /**
         * A Type of medicine that creates an immune protection without the recipient experiencing the disease.
         */
        VCCNE, 
        /**
         * A substance whose therapeutic effect is produced by chemical action within the body.
         */
        _DRUGENTITY, 
        /**
         * Any substance or mixture of substances manufactured, sold or represented for use in: (a) the diagnosis, treatment, mitigation or prevention of a disease, disorder, abnormal physical state, or its symptoms, in human beings or animals; (b) restoring, correcting or modifying organic functions in human beings or animals.
         */
        _CLINICALDRUG, 
        /**
         * Indicates types of allergy and intolerance agents which are non-drugs.  (E.g. foods, latex, etc.)
         */
        _NONDRUGAGENTENTITY, 
        /**
         * egg
         */
        NDA01, 
        /**
         * fish
         */
        NDA02, 
        /**
         * lactose
         */
        NDA03, 
        /**
         * peanut
         */
        NDA04, 
        /**
         * soy
         */
        NDA05, 
        /**
         * sulfites
         */
        NDA06, 
        /**
         * wheat or gluten
         */
        NDA07, 
        /**
         * isocyanates
         */
        NDA08, 
        /**
         * solvents
         */
        NDA09, 
        /**
         * oils
         */
        NDA10, 
        /**
         * venoms
         */
        NDA11, 
        /**
         * latex
         */
        NDA12, 
        /**
         * shellfish
         */
        NDA13, 
        /**
         * strawberries
         */
        NDA14, 
        /**
         * tomatoes
         */
        NDA15, 
        /**
         * dust
         */
        NDA16, 
        /**
         * dust mites
         */
        NDA17, 
        /**
         * Further classifies entities of classCode ORG.
         */
        _ORGANIZATIONENTITYTYPE, 
        /**
         * The group of persons who occupy a single housing unit.
         */
        HHOLD, 
        /**
         * Codes identifying nation states.  Allows for finer grained specification of Entity with classcode <= NAT

                        
                           Example:ISO3166 country codes.
         */
        NAT, 
        /**
         * An organization that provides religious rites of worship.
         */
        RELIG, 
        /**
         * Types of places for EntityClass "PLC"
         */
        _PLACEENTITYTYPE, 
        /**
         * The location of a bed
         */
        BED, 
        /**
         * The location of a building
         */
        BLDG, 
        /**
         * The location of a floor of a building
         */
        FLOOR, 
        /**
         * The location of a room
         */
        ROOM, 
        /**
         * The location of a wing of a building (e.g. East Wing).  The same room number for the same floor number can be distinguished by wing number in some situations
         */
        WING, 
        /**
         * Codes to characterize a Resource Group using categories that typify its membership and/or function

                        .

                        
                           Example: PractitionerGroup
         */
        _RESOURCEGROUPENTITYTYPE, 
        /**
         * PractitionerGroup
         */
        PRAC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_MaterialEntityClassType".equals(codeString))
          return _MATERIALENTITYCLASSTYPE;
        if ("_ContainerEntityType".equals(codeString))
          return _CONTAINERENTITYTYPE;
        if ("PKG".equals(codeString))
          return PKG;
        if ("_NonRigidContainerEntityType".equals(codeString))
          return _NONRIGIDCONTAINERENTITYTYPE;
        if ("BAG".equals(codeString))
          return BAG;
        if ("PACKT".equals(codeString))
          return PACKT;
        if ("PCH".equals(codeString))
          return PCH;
        if ("SACH".equals(codeString))
          return SACH;
        if ("_RigidContainerEntityType".equals(codeString))
          return _RIGIDCONTAINERENTITYTYPE;
        if ("_IndividualPackageEntityType".equals(codeString))
          return _INDIVIDUALPACKAGEENTITYTYPE;
        if ("AMP".equals(codeString))
          return AMP;
        if ("MINIM".equals(codeString))
          return MINIM;
        if ("NEBAMP".equals(codeString))
          return NEBAMP;
        if ("OVUL".equals(codeString))
          return OVUL;
        if ("_MultiUseContainerEntityType".equals(codeString))
          return _MULTIUSECONTAINERENTITYTYPE;
        if ("BOT".equals(codeString))
          return BOT;
        if ("BOTA".equals(codeString))
          return BOTA;
        if ("BOTD".equals(codeString))
          return BOTD;
        if ("BOTG".equals(codeString))
          return BOTG;
        if ("BOTP".equals(codeString))
          return BOTP;
        if ("BOTPLY".equals(codeString))
          return BOTPLY;
        if ("BOX".equals(codeString))
          return BOX;
        if ("CAN".equals(codeString))
          return CAN;
        if ("CART".equals(codeString))
          return CART;
        if ("CNSTR".equals(codeString))
          return CNSTR;
        if ("JAR".equals(codeString))
          return JAR;
        if ("JUG".equals(codeString))
          return JUG;
        if ("TIN".equals(codeString))
          return TIN;
        if ("TUB".equals(codeString))
          return TUB;
        if ("TUBE".equals(codeString))
          return TUBE;
        if ("VIAL".equals(codeString))
          return VIAL;
        if ("BLSTRPK".equals(codeString))
          return BLSTRPK;
        if ("CARD".equals(codeString))
          return CARD;
        if ("COMPPKG".equals(codeString))
          return COMPPKG;
        if ("DIALPK".equals(codeString))
          return DIALPK;
        if ("DISK".equals(codeString))
          return DISK;
        if ("DOSET".equals(codeString))
          return DOSET;
        if ("STRIP".equals(codeString))
          return STRIP;
        if ("KIT".equals(codeString))
          return KIT;
        if ("SYSTM".equals(codeString))
          return SYSTM;
        if ("_MedicalDevice".equals(codeString))
          return _MEDICALDEVICE;
        if ("_AccessMedicalDevice".equals(codeString))
          return _ACCESSMEDICALDEVICE;
        if ("LINE".equals(codeString))
          return LINE;
        if ("IALINE".equals(codeString))
          return IALINE;
        if ("IVLINE".equals(codeString))
          return IVLINE;
        if ("_AdministrationMedicalDevice".equals(codeString))
          return _ADMINISTRATIONMEDICALDEVICE;
        if ("_InjectionMedicalDevice".equals(codeString))
          return _INJECTIONMEDICALDEVICE;
        if ("AINJ".equals(codeString))
          return AINJ;
        if ("PEN".equals(codeString))
          return PEN;
        if ("SYR".equals(codeString))
          return SYR;
        if ("APLCTR".equals(codeString))
          return APLCTR;
        if ("INH".equals(codeString))
          return INH;
        if ("DSKS".equals(codeString))
          return DSKS;
        if ("DSKUNH".equals(codeString))
          return DSKUNH;
        if ("TRBINH".equals(codeString))
          return TRBINH;
        if ("PMP".equals(codeString))
          return PMP;
        if ("_SpecimenAdditiveEntity".equals(codeString))
          return _SPECIMENADDITIVEENTITY;
        if ("ACDA".equals(codeString))
          return ACDA;
        if ("ACDB".equals(codeString))
          return ACDB;
        if ("ACET".equals(codeString))
          return ACET;
        if ("AMIES".equals(codeString))
          return AMIES;
        if ("BACTM".equals(codeString))
          return BACTM;
        if ("BF10".equals(codeString))
          return BF10;
        if ("BOR".equals(codeString))
          return BOR;
        if ("BOUIN".equals(codeString))
          return BOUIN;
        if ("BSKM".equals(codeString))
          return BSKM;
        if ("C32".equals(codeString))
          return C32;
        if ("C38".equals(codeString))
          return C38;
        if ("CARS".equals(codeString))
          return CARS;
        if ("CARY".equals(codeString))
          return CARY;
        if ("CHLTM".equals(codeString))
          return CHLTM;
        if ("CTAD".equals(codeString))
          return CTAD;
        if ("EDTK15".equals(codeString))
          return EDTK15;
        if ("EDTK75".equals(codeString))
          return EDTK75;
        if ("EDTN".equals(codeString))
          return EDTN;
        if ("ENT".equals(codeString))
          return ENT;
        if ("F10".equals(codeString))
          return F10;
        if ("FDP".equals(codeString))
          return FDP;
        if ("FL10".equals(codeString))
          return FL10;
        if ("FL100".equals(codeString))
          return FL100;
        if ("HCL6".equals(codeString))
          return HCL6;
        if ("HEPA".equals(codeString))
          return HEPA;
        if ("HEPL".equals(codeString))
          return HEPL;
        if ("HEPN".equals(codeString))
          return HEPN;
        if ("HNO3".equals(codeString))
          return HNO3;
        if ("JKM".equals(codeString))
          return JKM;
        if ("KARN".equals(codeString))
          return KARN;
        if ("KOX".equals(codeString))
          return KOX;
        if ("LIA".equals(codeString))
          return LIA;
        if ("M4".equals(codeString))
          return M4;
        if ("M4RT".equals(codeString))
          return M4RT;
        if ("M5".equals(codeString))
          return M5;
        if ("MICHTM".equals(codeString))
          return MICHTM;
        if ("MMDTM".equals(codeString))
          return MMDTM;
        if ("NAF".equals(codeString))
          return NAF;
        if ("NONE".equals(codeString))
          return NONE;
        if ("PAGE".equals(codeString))
          return PAGE;
        if ("PHENOL".equals(codeString))
          return PHENOL;
        if ("PVA".equals(codeString))
          return PVA;
        if ("RLM".equals(codeString))
          return RLM;
        if ("SILICA".equals(codeString))
          return SILICA;
        if ("SPS".equals(codeString))
          return SPS;
        if ("SST".equals(codeString))
          return SST;
        if ("STUTM".equals(codeString))
          return STUTM;
        if ("THROM".equals(codeString))
          return THROM;
        if ("THYMOL".equals(codeString))
          return THYMOL;
        if ("THYO".equals(codeString))
          return THYO;
        if ("TOLU".equals(codeString))
          return TOLU;
        if ("URETM".equals(codeString))
          return URETM;
        if ("VIRTM".equals(codeString))
          return VIRTM;
        if ("WEST".equals(codeString))
          return WEST;
        if ("BLDPRD".equals(codeString))
          return BLDPRD;
        if ("VCCNE".equals(codeString))
          return VCCNE;
        if ("_DrugEntity".equals(codeString))
          return _DRUGENTITY;
        if ("_ClinicalDrug".equals(codeString))
          return _CLINICALDRUG;
        if ("_NonDrugAgentEntity".equals(codeString))
          return _NONDRUGAGENTENTITY;
        if ("NDA01".equals(codeString))
          return NDA01;
        if ("NDA02".equals(codeString))
          return NDA02;
        if ("NDA03".equals(codeString))
          return NDA03;
        if ("NDA04".equals(codeString))
          return NDA04;
        if ("NDA05".equals(codeString))
          return NDA05;
        if ("NDA06".equals(codeString))
          return NDA06;
        if ("NDA07".equals(codeString))
          return NDA07;
        if ("NDA08".equals(codeString))
          return NDA08;
        if ("NDA09".equals(codeString))
          return NDA09;
        if ("NDA10".equals(codeString))
          return NDA10;
        if ("NDA11".equals(codeString))
          return NDA11;
        if ("NDA12".equals(codeString))
          return NDA12;
        if ("NDA13".equals(codeString))
          return NDA13;
        if ("NDA14".equals(codeString))
          return NDA14;
        if ("NDA15".equals(codeString))
          return NDA15;
        if ("NDA16".equals(codeString))
          return NDA16;
        if ("NDA17".equals(codeString))
          return NDA17;
        if ("_OrganizationEntityType".equals(codeString))
          return _ORGANIZATIONENTITYTYPE;
        if ("HHOLD".equals(codeString))
          return HHOLD;
        if ("NAT".equals(codeString))
          return NAT;
        if ("RELIG".equals(codeString))
          return RELIG;
        if ("_PlaceEntityType".equals(codeString))
          return _PLACEENTITYTYPE;
        if ("BED".equals(codeString))
          return BED;
        if ("BLDG".equals(codeString))
          return BLDG;
        if ("FLOOR".equals(codeString))
          return FLOOR;
        if ("ROOM".equals(codeString))
          return ROOM;
        if ("WING".equals(codeString))
          return WING;
        if ("_ResourceGroupEntityType".equals(codeString))
          return _RESOURCEGROUPENTITYTYPE;
        if ("PRAC".equals(codeString))
          return PRAC;
        throw new FHIRException("Unknown V3EntityCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _MATERIALENTITYCLASSTYPE: return "_MaterialEntityClassType";
            case _CONTAINERENTITYTYPE: return "_ContainerEntityType";
            case PKG: return "PKG";
            case _NONRIGIDCONTAINERENTITYTYPE: return "_NonRigidContainerEntityType";
            case BAG: return "BAG";
            case PACKT: return "PACKT";
            case PCH: return "PCH";
            case SACH: return "SACH";
            case _RIGIDCONTAINERENTITYTYPE: return "_RigidContainerEntityType";
            case _INDIVIDUALPACKAGEENTITYTYPE: return "_IndividualPackageEntityType";
            case AMP: return "AMP";
            case MINIM: return "MINIM";
            case NEBAMP: return "NEBAMP";
            case OVUL: return "OVUL";
            case _MULTIUSECONTAINERENTITYTYPE: return "_MultiUseContainerEntityType";
            case BOT: return "BOT";
            case BOTA: return "BOTA";
            case BOTD: return "BOTD";
            case BOTG: return "BOTG";
            case BOTP: return "BOTP";
            case BOTPLY: return "BOTPLY";
            case BOX: return "BOX";
            case CAN: return "CAN";
            case CART: return "CART";
            case CNSTR: return "CNSTR";
            case JAR: return "JAR";
            case JUG: return "JUG";
            case TIN: return "TIN";
            case TUB: return "TUB";
            case TUBE: return "TUBE";
            case VIAL: return "VIAL";
            case BLSTRPK: return "BLSTRPK";
            case CARD: return "CARD";
            case COMPPKG: return "COMPPKG";
            case DIALPK: return "DIALPK";
            case DISK: return "DISK";
            case DOSET: return "DOSET";
            case STRIP: return "STRIP";
            case KIT: return "KIT";
            case SYSTM: return "SYSTM";
            case _MEDICALDEVICE: return "_MedicalDevice";
            case _ACCESSMEDICALDEVICE: return "_AccessMedicalDevice";
            case LINE: return "LINE";
            case IALINE: return "IALINE";
            case IVLINE: return "IVLINE";
            case _ADMINISTRATIONMEDICALDEVICE: return "_AdministrationMedicalDevice";
            case _INJECTIONMEDICALDEVICE: return "_InjectionMedicalDevice";
            case AINJ: return "AINJ";
            case PEN: return "PEN";
            case SYR: return "SYR";
            case APLCTR: return "APLCTR";
            case INH: return "INH";
            case DSKS: return "DSKS";
            case DSKUNH: return "DSKUNH";
            case TRBINH: return "TRBINH";
            case PMP: return "PMP";
            case _SPECIMENADDITIVEENTITY: return "_SpecimenAdditiveEntity";
            case ACDA: return "ACDA";
            case ACDB: return "ACDB";
            case ACET: return "ACET";
            case AMIES: return "AMIES";
            case BACTM: return "BACTM";
            case BF10: return "BF10";
            case BOR: return "BOR";
            case BOUIN: return "BOUIN";
            case BSKM: return "BSKM";
            case C32: return "C32";
            case C38: return "C38";
            case CARS: return "CARS";
            case CARY: return "CARY";
            case CHLTM: return "CHLTM";
            case CTAD: return "CTAD";
            case EDTK15: return "EDTK15";
            case EDTK75: return "EDTK75";
            case EDTN: return "EDTN";
            case ENT: return "ENT";
            case F10: return "F10";
            case FDP: return "FDP";
            case FL10: return "FL10";
            case FL100: return "FL100";
            case HCL6: return "HCL6";
            case HEPA: return "HEPA";
            case HEPL: return "HEPL";
            case HEPN: return "HEPN";
            case HNO3: return "HNO3";
            case JKM: return "JKM";
            case KARN: return "KARN";
            case KOX: return "KOX";
            case LIA: return "LIA";
            case M4: return "M4";
            case M4RT: return "M4RT";
            case M5: return "M5";
            case MICHTM: return "MICHTM";
            case MMDTM: return "MMDTM";
            case NAF: return "NAF";
            case NONE: return "NONE";
            case PAGE: return "PAGE";
            case PHENOL: return "PHENOL";
            case PVA: return "PVA";
            case RLM: return "RLM";
            case SILICA: return "SILICA";
            case SPS: return "SPS";
            case SST: return "SST";
            case STUTM: return "STUTM";
            case THROM: return "THROM";
            case THYMOL: return "THYMOL";
            case THYO: return "THYO";
            case TOLU: return "TOLU";
            case URETM: return "URETM";
            case VIRTM: return "VIRTM";
            case WEST: return "WEST";
            case BLDPRD: return "BLDPRD";
            case VCCNE: return "VCCNE";
            case _DRUGENTITY: return "_DrugEntity";
            case _CLINICALDRUG: return "_ClinicalDrug";
            case _NONDRUGAGENTENTITY: return "_NonDrugAgentEntity";
            case NDA01: return "NDA01";
            case NDA02: return "NDA02";
            case NDA03: return "NDA03";
            case NDA04: return "NDA04";
            case NDA05: return "NDA05";
            case NDA06: return "NDA06";
            case NDA07: return "NDA07";
            case NDA08: return "NDA08";
            case NDA09: return "NDA09";
            case NDA10: return "NDA10";
            case NDA11: return "NDA11";
            case NDA12: return "NDA12";
            case NDA13: return "NDA13";
            case NDA14: return "NDA14";
            case NDA15: return "NDA15";
            case NDA16: return "NDA16";
            case NDA17: return "NDA17";
            case _ORGANIZATIONENTITYTYPE: return "_OrganizationEntityType";
            case HHOLD: return "HHOLD";
            case NAT: return "NAT";
            case RELIG: return "RELIG";
            case _PLACEENTITYTYPE: return "_PlaceEntityType";
            case BED: return "BED";
            case BLDG: return "BLDG";
            case FLOOR: return "FLOOR";
            case ROOM: return "ROOM";
            case WING: return "WING";
            case _RESOURCEGROUPENTITYTYPE: return "_ResourceGroupEntityType";
            case PRAC: return "PRAC";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityCode";
        }
        public String getDefinition() {
          switch (this) {
            case _MATERIALENTITYCLASSTYPE: return "Types of Material for EntityClass \"MAT\"";
            case _CONTAINERENTITYTYPE: return "Material intended to hold another material for purpose of storage or transport.";
            case PKG: return "A material intended to hold other materials for purposes of storage or transportation";
            case _NONRIGIDCONTAINERENTITYTYPE: return "A container having dimensions that adjust somewhat based on the amount and shape of the material placed within it.";
            case BAG: return "A pouched or pendulous container.";
            case PACKT: return "A paper";
            case PCH: return "A small bag or container made of a soft material.";
            case SACH: return "A small bag or packet containing a small portion of a substance.";
            case _RIGIDCONTAINERENTITYTYPE: return "A container having a fixed and inflexible dimensions and volume";
            case _INDIVIDUALPACKAGEENTITYTYPE: return "Container intended to contain sufficient material for only one use.";
            case AMP: return "A small sealed glass container that holds a measured amount of a medicinal substance.";
            case MINIM: return "Individually dosed ophthalmic solution.  One time eye dropper dispenser.";
            case NEBAMP: return "Individually dosed inhalation solution.";
            case OVUL: return "A container either glass or plastic and a narrow neck, for storing liquid.";
            case _MULTIUSECONTAINERENTITYTYPE: return "A container intended to contain sufficient material for more than one use.  (I.e. Material is intended to be removed from the container at more than one discrete time period.)";
            case BOT: return "A container, typically rounded, either glass or plastic with a narrow neck and capable of storing liquid.";
            case BOTA: return "A bottle of yellow to brown color.  Used to store light-sensitive materials";
            case BOTD: return "A bottle with a cap designed to release the contained liquid in droplets of a specific size.";
            case BOTG: return "A bottle made of glass";
            case BOTP: return "A bottle made of plastic";
            case BOTPLY: return "A bottle made of polyethylene";
            case BOX: return "A 6-sided container commonly made from paper or cardboard used for solid forms.";
            case CAN: return "A metal container in which a material is hermetically sealed to enable storage over long periods.";
            case CART: return "A sealed container of liquid or powder intended to be loaded into a device.";
            case CNSTR: return "A pressurized metal container holding a substance released as a spray or aerosol.";
            case JAR: return "A container of glass, earthenware, plastic, etc.  Top of the container has a diameter of similar size to the diameter of the container as a whole";
            case JUG: return "A deep vessel  for holding liquids, with a handle and often with a spout or lip shape for pouring.";
            case TIN: return "A lidded container made of thin sheet metal.";
            case TUB: return "An open flat bottomed round container.";
            case TUBE: return "A long hollow rigid or flexible cylinder.  Material is extruded by squeezing the container.";
            case VIAL: return "A small cylindrical glass for holding liquid medicines.";
            case BLSTRPK: return "A bubblepack.  Medications sealed individually, separated into doses.";
            case CARD: return "A bubble pack card.  Multiple individual/separated doses.";
            case COMPPKG: return "A container intended to contain sufficient material for more than one use, but grouped or organized to provide individual access to sufficient material for a single use.  Often used to ensure that the proper type and amount of material is consumed/expended for each use.";
            case DIALPK: return "Rotatable dispenser.  Eg. Birth control package.";
            case DISK: return "Object that is thin, flat, and circular.  Doses of medication often contained in bubbles on the disk.";
            case DOSET: return "Special packaging that will help patients take their medications on a regular basis.";
            case STRIP: return "A continuous strip of plastic sectioned into individual pouches, each one containing the quantity of 1 or more medications intended to be administered at a specific time";
            case KIT: return "A container for a diverse collection of products intended to be used together for some purpose (e.g. Medicinal kits often contain a syringe, a needle and the injectable medication).";
            case SYSTM: return "A kit in which the components are interconnected.";
            case _MEDICALDEVICE: return "A device with direct or indirect therapeutic purpose.  Values for EntityCode when EntityClass = \"DEV\"";
            case _ACCESSMEDICALDEVICE: return "A device used to allow access to a part of a body";
            case LINE: return "A hollow tube used to administer a substance into a vein, artery or body cavity";
            case IALINE: return "A line used to administer a substance into an artery";
            case IVLINE: return "A line used to administer a substance into a vein";
            case _ADMINISTRATIONMEDICALDEVICE: return "A device intended to administer a substance to a subject";
            case _INJECTIONMEDICALDEVICE: return "A device intended to administer liquid into a subject via a";
            case AINJ: return "Automatically injects medication.";
            case PEN: return "A device which can contain a cartridge for injection purposes.  Eg. Insulin pen.";
            case SYR: return "A barrel with a plunger.";
            case APLCTR: return "A device used to apply a liquid or powder to a surface.";
            case INH: return "A small device used for inhaling medicine in the form of a vapour or gas in order to ease a respiratory condition such as asthma or to relieve nasal congestion.";
            case DSKS: return "The device used to inhale the doses of medication contained in the disk form.";
            case DSKUNH: return "The device used to inhale the doses of medication contained in the disk form.";
            case TRBINH: return "Asthma medication delivery device.";
            case PMP: return "A device that is used to raise, compress, or transfer liquids or gases and is operated by a piston or similar mechanism.";
            case _SPECIMENADDITIVEENTITY: return "Set of codes related to specimen additives";
            case ACDA: return "ACD Solution A of trisodium citrate, 22.0g/L; citric acid, 8.0 g/L; and dextrose 24.5 g/L. Used in Blood banking and histocompatibilty testing";
            case ACDB: return "ACD Solution B of trisodium citrate, 13.2g/L; citric acid, 4.8 g/L; and dextrose 14.7 g/L. Used in Blood banking and histocompatibilty testing.";
            case ACET: return "50% V/V acetic acid in water.  Used as  a urine preservative";
            case AMIES: return "Sodium Chloride 3.0g, Potassium Chloride 0.2g, Calcium Chloride 0.1g, Magnesium Chloride 0.1g, Monopotassium Phosphate 0.2g, Disodium Phosphate 1.15g, Sodium Thiogly collate 1.0g, Distilled Water 1 liter";
            case BACTM: return "Any medium used to maintain bacterial viability (e.g. Stuart's, Cary-Blair, Amies)";
            case BF10: return "Formaldehyde 4% w/v; methyl alcohol 1% w/v; phosphate buffering salts. Tissue preservative";
            case BOR: return "Powdered boric acid (usually 10 g) added to 24-hour urine collections as a preservative.";
            case BOUIN: return "Picric acid, saturated aqueous solution (750.0 ml), 37-40% formalin (250.0 ml), glacial acetic acid (50.0 ml). Tissue preservative.";
            case BSKM: return "50% skim milk in 0.01 M phosphate-buffered saline.  Maintain virus viability";
            case C32: return "A 3.2% solution of Sodium Citrate in water.  Used as a blood preservative";
            case C38: return "A 3.8% solution of Sodium Citrate in water. Used as a blood preservative";
            case CARS: return "A modification of buffered 10% formalin used as a general tissue preservative.";
            case CARY: return "Sodium Thioglycollate 1.5 g, Disodium Hydrogen Phosphate 1.1 g, Sodium Chloride 5.0 g, Calcium Chloride 0.09 g, Agar 5.0 g, per Liter of Water";
            case CHLTM: return "Any of a number of non-nutritive buffered media used to maintain Chlamydia viability during transportation to the laboratory";
            case CTAD: return "Buffered tri-sodium citrate solution with theophylline, adenosine and dipyridamole";
            case EDTK15: return "Potassium EDTA 15% solution in water";
            case EDTK75: return "Potassium EDTA 7.5% solution in water";
            case EDTN: return "Sodium fluoride and Disodium EDTA";
            case ENT: return "Any of a number of non-nutritive buffered media used to maintain enteric bacterial viability during transportation to the laboratory";
            case F10: return "A 10% v/v solution in water of formalin( a 37% solution of formaldehyde and water).  Used for tissue preservation.";
            case FDP: return "Thrombin plus soybean trypsin inhibitor.  For use in identifying fibrn degredation products.";
            case FL10: return "Sodium fluoride, 10mg added as a urine preservative.";
            case FL100: return "Sodium fluoride, 100mg added as a urine preservative.";
            case HCL6: return "A solution of HCl containing 6moles of hydrogen ion/L. Used as a Urine Preservative.";
            case HEPA: return "Ammonium heparin";
            case HEPL: return "Lithium heparin salt";
            case HEPN: return "Sodium heparin salt";
            case HNO3: return "6N Nitric acid used to preserve urine for heavy metal analysis.";
            case JKM: return "A transport medium formulated to maintain Bordetella pertussis viability.";
            case KARN: return "5% Glutaraldehyde, 4% Formaldehyde in 0.08M buffer. Tissue preservation";
            case KOX: return "Potassium oxalate and sodium fluoride in a 1.25:1 ratio";
            case LIA: return "Iodoacetate lithium salt";
            case M4: return "Modified Hank's balanced salt solution supplemented with bovine serum albumin, gelatin, sucrose and glutamic acid. It is buffered to pH 7.3+ or - 0.2 with HEPES buffer. Phenol red is used to indicate pH. Vancomycin, Amphotericin B and Colistin are used to";
            case M4RT: return "Modified Hank's balanced salt solution supplemented with bovine serum albumin, gelatin, sucrose and glutamic acid. It is buffered to pH 7.3+ or - 0.2 with Hepes buffer. Phenol red is used to indicate pH. Gentamicin and amphotericin B are used to inhibit c";
            case M5: return "Modified Hank's balanced salt solution supplemented with protein stabilizers, sucrose and glutamic acid. It is buffered to pH 7.3+ or - 0.2 with Hepes buffer. Phenol red is used to indicate pH. Vancomycin, Amphotericin B and Colistin are used to inhibit c";
            case MICHTM: return "1M potassium citrate, pH 7.0 2.5 ml, 0.1M magnesium sulfate 5.0 ml, 0.1M N-ethyl malemide  5.0 ml, dH2O 87.5 ml, ammonium sulfate 55gm. Preserve antigens for Immunofluorescence procedures";
            case MMDTM: return "A buffered medium with ammonium sulfate added to preserve antigens for Immunofluorescence procedures";
            case NAF: return "Sodium fluoride";
            case NONE: return "No additive. Specifically identifes the specimen as having no additives.";
            case PAGE: return "0.12 g NaCl, 0.004 g MgSO, 0.004 g, CaCl, 0.142 g Na2HPO4 and 0.136 g KH2PO4 per liter of distilled water. Maintain Acanthaoemba viability.";
            case PHENOL: return "Phenol. Urine preservative";
            case PVA: return "Polyvinyl alcohol";
            case RLM: return "A transport medium formulated to maintain Bordetella pertussis viability.";
            case SILICA: return "Diatomaceous earth. For glucose determination blood samples";
            case SPS: return "Sodium polyanethol sulfonate in saline. Anticomplementary and antiphagocytic properties. Used in blood culture collection.";
            case SST: return "Polymer separator gel with clot activator";
            case STUTM: return "Sodium Glycerophosphate 10.0g, Calcium Chloride 0.1g, Mercaptoacetic Acid 1.0ml, Distilled Water 1 liter";
            case THROM: return "Thrombin. Accelerates clotting.";
            case THYMOL: return "2-Isopropyl-5-methyl phenol. A preservative for 24 Hr Urine samples";
            case THYO: return "A nutritive medium with a reducing agent  (sodium thioglycolate) which, due to a chemical reaction, removes oxygen from the broth.";
            case TOLU: return "Also known as Methylbenzene; Toluol; Phenylmethane. A preservative for 24 Hr Urine samples";
            case URETM: return "A buffered salt solution with antifungal agents added for the collection and transport of Ureaplasma specimens.";
            case VIRTM: return "Sucrose 74.6g, Potassium hydrogenphosphate 0.52g, L-glutamic acid 0.72g, Bovine serum albumin 5.0g, Gentamicin 50mg, Potassium dihydrogenphosphate 1.25g, L-15 medium 9.9L, Water to 10L. Maintain Virus viability.";
            case WEST: return "3.8% Citrate buffered to a pH of 5.5 for Westergren Sedimentation Rate";
            case BLDPRD: return "A manufactured product that is produced from the raw blood oi a donor with the intention of using it in a recipient transfusion.";
            case VCCNE: return "A Type of medicine that creates an immune protection without the recipient experiencing the disease.";
            case _DRUGENTITY: return "A substance whose therapeutic effect is produced by chemical action within the body.";
            case _CLINICALDRUG: return "Any substance or mixture of substances manufactured, sold or represented for use in: (a) the diagnosis, treatment, mitigation or prevention of a disease, disorder, abnormal physical state, or its symptoms, in human beings or animals; (b) restoring, correcting or modifying organic functions in human beings or animals.";
            case _NONDRUGAGENTENTITY: return "Indicates types of allergy and intolerance agents which are non-drugs.  (E.g. foods, latex, etc.)";
            case NDA01: return "egg";
            case NDA02: return "fish";
            case NDA03: return "lactose";
            case NDA04: return "peanut";
            case NDA05: return "soy";
            case NDA06: return "sulfites";
            case NDA07: return "wheat or gluten";
            case NDA08: return "isocyanates";
            case NDA09: return "solvents";
            case NDA10: return "oils";
            case NDA11: return "venoms";
            case NDA12: return "latex";
            case NDA13: return "shellfish";
            case NDA14: return "strawberries";
            case NDA15: return "tomatoes";
            case NDA16: return "dust";
            case NDA17: return "dust mites";
            case _ORGANIZATIONENTITYTYPE: return "Further classifies entities of classCode ORG.";
            case HHOLD: return "The group of persons who occupy a single housing unit.";
            case NAT: return "Codes identifying nation states.  Allows for finer grained specification of Entity with classcode <= NAT\r\n\n                        \n                           Example:ISO3166 country codes.";
            case RELIG: return "An organization that provides religious rites of worship.";
            case _PLACEENTITYTYPE: return "Types of places for EntityClass \"PLC\"";
            case BED: return "The location of a bed";
            case BLDG: return "The location of a building";
            case FLOOR: return "The location of a floor of a building";
            case ROOM: return "The location of a room";
            case WING: return "The location of a wing of a building (e.g. East Wing).  The same room number for the same floor number can be distinguished by wing number in some situations";
            case _RESOURCEGROUPENTITYTYPE: return "Codes to characterize a Resource Group using categories that typify its membership and/or function\r\n\n                        .\r\n\n                        \n                           Example: PractitionerGroup";
            case PRAC: return "PractitionerGroup";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _MATERIALENTITYCLASSTYPE: return "MaterialEntityClassType";
            case _CONTAINERENTITYTYPE: return "ContainerEntityType";
            case PKG: return "Package";
            case _NONRIGIDCONTAINERENTITYTYPE: return "NonRigidContainerEntityType";
            case BAG: return "Bag";
            case PACKT: return "Packet";
            case PCH: return "Pouch";
            case SACH: return "Sachet";
            case _RIGIDCONTAINERENTITYTYPE: return "RigidContainerEntityType";
            case _INDIVIDUALPACKAGEENTITYTYPE: return "IndividualPackageEntityType";
            case AMP: return "Ampule";
            case MINIM: return "Minim";
            case NEBAMP: return "Nebuamp";
            case OVUL: return "Ovule";
            case _MULTIUSECONTAINERENTITYTYPE: return "MultiUseContainerEntityType";
            case BOT: return "Bottle";
            case BOTA: return "Amber Bottle";
            case BOTD: return "Dropper Bottle";
            case BOTG: return "Glass Bottle";
            case BOTP: return "Plastic Bottle";
            case BOTPLY: return "Polyethylene Bottle";
            case BOX: return "Box";
            case CAN: return "Can";
            case CART: return "Cartridge";
            case CNSTR: return "Canister";
            case JAR: return "Jar";
            case JUG: return "Jug";
            case TIN: return "Tin";
            case TUB: return "Tub";
            case TUBE: return "Tube";
            case VIAL: return "Vial";
            case BLSTRPK: return "Blister Pack";
            case CARD: return "Card";
            case COMPPKG: return "Compliance Package";
            case DIALPK: return "Dial Pack";
            case DISK: return "Disk";
            case DOSET: return "Dosette";
            case STRIP: return "Strip";
            case KIT: return "Kit";
            case SYSTM: return "System";
            case _MEDICALDEVICE: return "MedicalDevice";
            case _ACCESSMEDICALDEVICE: return "AccessMedicalDevice";
            case LINE: return "Line";
            case IALINE: return "Intra-arterial Line";
            case IVLINE: return "Intraveneous Line";
            case _ADMINISTRATIONMEDICALDEVICE: return "AdministrationMedicalDevice";
            case _INJECTIONMEDICALDEVICE: return "InjectionMedicalDevice";
            case AINJ: return "AutoInjector";
            case PEN: return "Pen";
            case SYR: return "Syringe";
            case APLCTR: return "Applicator";
            case INH: return "Inhaler";
            case DSKS: return "Diskus";
            case DSKUNH: return "Diskhaler";
            case TRBINH: return "Turbuhaler";
            case PMP: return "Pump";
            case _SPECIMENADDITIVEENTITY: return "SpecimenAdditiveEntity";
            case ACDA: return "ACD Solution A";
            case ACDB: return "ACD Solution B";
            case ACET: return "Acetic Acid";
            case AMIES: return "Amies transport medium";
            case BACTM: return "Bacterial Transport medium";
            case BF10: return "Buffered 10% formalin";
            case BOR: return "Boric Acid";
            case BOUIN: return "Bouin's solution";
            case BSKM: return "Buffered skim milk";
            case C32: return "3.2% Citrate";
            case C38: return "3.8% Citrate";
            case CARS: return "Carson's Modified 10% formalin";
            case CARY: return "Cary Blair Medium";
            case CHLTM: return "Chlamydia transport medium";
            case CTAD: return "CTAD";
            case EDTK15: return "Potassium/K EDTA 15%";
            case EDTK75: return "Potassium/K EDTA 7.5%";
            case EDTN: return "Sodium/Na EDTA";
            case ENT: return "Enteric bacteria transport medium";
            case F10: return "10% Formalin";
            case FDP: return "Thrombin NIH; soybean trypsin inhibitor";
            case FL10: return "Sodium Fluoride, 10mg";
            case FL100: return "Sodium Fluoride, 100mg";
            case HCL6: return "6N HCL";
            case HEPA: return "Ammonium heparin";
            case HEPL: return "Lithium/Li Heparin";
            case HEPN: return "Sodium/Na Heparin";
            case HNO3: return "Nitric Acid";
            case JKM: return "Jones Kendrick Medium";
            case KARN: return "Karnovsky's fixative";
            case KOX: return "Potassium Oxalate";
            case LIA: return "Lithium iodoacetate";
            case M4: return "M4";
            case M4RT: return "M4-RT";
            case M5: return "M5";
            case MICHTM: return "Michel's transport medium";
            case MMDTM: return "MMD transport medium";
            case NAF: return "Sodium Fluoride";
            case NONE: return "None";
            case PAGE: return "Page's Saline";
            case PHENOL: return "Phenol";
            case PVA: return "Polyvinylalcohol";
            case RLM: return "Reagan Lowe Medium";
            case SILICA: return "Siliceous earth";
            case SPS: return "Sodium polyanethol sulfonate 0.35% in 0.85% sodium chloride";
            case SST: return "Serum Separator Tube";
            case STUTM: return "Stuart transport medium";
            case THROM: return "Thrombin";
            case THYMOL: return "Thymol";
            case THYO: return "Thyoglycolate broth";
            case TOLU: return "Toluene";
            case URETM: return "Ureaplasma transport medium";
            case VIRTM: return "Viral Transport medium";
            case WEST: return "Buffered Citrate";
            case BLDPRD: return "Blood Product";
            case VCCNE: return "Vaccine";
            case _DRUGENTITY: return "DrugEntity";
            case _CLINICALDRUG: return "ClinicalDrug";
            case _NONDRUGAGENTENTITY: return "NonDrugAgentEntity";
            case NDA01: return "egg";
            case NDA02: return "fish";
            case NDA03: return "lactose";
            case NDA04: return "peanut";
            case NDA05: return "soy";
            case NDA06: return "sulfites";
            case NDA07: return "wheat or gluten";
            case NDA08: return "isocyanates";
            case NDA09: return "solvents";
            case NDA10: return "oils";
            case NDA11: return "venoms";
            case NDA12: return "latex";
            case NDA13: return "shellfish";
            case NDA14: return "strawberries";
            case NDA15: return "tomatoes";
            case NDA16: return "dust";
            case NDA17: return "dust mites";
            case _ORGANIZATIONENTITYTYPE: return "OrganizationEntityType";
            case HHOLD: return "household";
            case NAT: return "NationEntityType";
            case RELIG: return "religious institution";
            case _PLACEENTITYTYPE: return "PlaceEntityType";
            case BED: return "Bed Location";
            case BLDG: return "Building Location";
            case FLOOR: return "Floor Location";
            case ROOM: return "Room Location";
            case WING: return "Wing Location";
            case _RESOURCEGROUPENTITYTYPE: return "ResourceGroupEntityType";
            case PRAC: return "PractitionerGroup";
            default: return "?";
          }
    }


}

