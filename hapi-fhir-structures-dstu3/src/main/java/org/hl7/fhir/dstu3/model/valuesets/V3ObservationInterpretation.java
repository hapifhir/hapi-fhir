package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ObservationInterpretation {

        /**
         * Codes that specify interpretation of genetic analysis, such as "positive", "negative", "carrier", "responsive", etc.
         */
        _GENETICOBSERVATIONINTERPRETATION, 
        /**
         * The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.
         */
        CAR, 
        /**
         * The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.

                        
                           
                              Deprecation Comment: 
                           This code is currently the same string as the print name for this concept and is inconsistent with the conventions being used for the other codes in the coding system, as it is a full word with initial capitalization, rather than an all upper case mnemonic.  The recommendation from OO is to deprecate the code "Carrier" and to add "CAR" as the new active code representation for this concept.
         */
        CARRIER, 
        /**
         * Interpretations of change of quantity and/or severity. At most one of B or W and one of U or D allowed.
         */
        _OBSERVATIONINTERPRETATIONCHANGE, 
        /**
         * The current result or observation value has improved compared to the previous result or observation value (the change is significant as defined in the respective test procedure).

                        [Note: This can be applied to quantitative or qualitative observations.]
         */
        B, 
        /**
         * The current result has decreased from the previous result for a quantitative observation (the change is significant as defined in the respective test procedure).
         */
        D, 
        /**
         * The current result has increased from the previous result for a quantitative observation (the change is significant as defined in the respective test procedure).
         */
        U, 
        /**
         * The current result or observation value has degraded compared to the previous result or observation value (the change is significant as defined in the respective test procedure).

                        [Note: This can be applied to quantitative or qualitative observations.]
         */
        W, 
        /**
         * Technical exceptions resulting in the inability to provide an interpretation. At most one allowed. Does not imply normality or severity.
         */
        _OBSERVATIONINTERPRETATIONEXCEPTIONS, 
        /**
         * The result is below the minimum detection limit (the test procedure or equipment is the limiting factor).

                        Synonyms: Below analytical limit, low off scale.
         */
        LESS_THAN, 
        /**
         * The result is above the maximum quantifiable limit (the test procedure or equipment is the limiting factor).

                        Synonyms: Above analytical limit, high off scale.
         */
        GREATER_THAN, 
        /**
         * A valid result cannot be obtained for the specified component / analyte due to the presence of anti-complementary substances in the sample.
         */
        AC, 
        /**
         * There is insufficient evidence that the species in question is a good target for therapy with the drug.  A categorical interpretation is not possible.

                        [Note: A MIC with "IE" and/or a comment may be reported (without an accompanying S, I or R-categorization).]
         */
        IE, 
        /**
         * A result cannot be considered valid for the specified component / analyte or organism due to failure in the quality control testing component.
         */
        QCF, 
        /**
         * A valid result cannot be obtained for the specified organism or cell line due to the presence of cytotoxic substances in the sample or culture.
         */
        TOX, 
        /**
         * Interpretation of normality or degree of abnormality (including critical or "alert" level). Concepts in this category are mutually exclusive, i.e., at most one is allowed.
         */
        _OBSERVATIONINTERPRETATIONNORMALITY, 
        /**
         * The result or observation value is outside the reference range or expected norm (as defined for the respective test procedure).

                        [Note: Typically applies to non-numeric results.]
         */
        A, 
        /**
         * The result or observation value is outside a reference range or expected norm at a level at which immediate action should be considered for patient safety (as defined for the respective test procedure).

                        [Note: Typically applies to non-numeric results.  Analogous to critical/panic limits for numeric results.]
         */
        AA, 
        /**
         * The result for a quantitative observation is above a reference level at which immediate action should be considered for patient safety (as defined for the respective test procedure).

                        Synonym: Above upper panic limits.
         */
        HH, 
        /**
         * The result for a quantitative observation is below a reference level at which immediate action should be considered for patient safety (as defined for the respective test procedure).

                        Synonym: Below lower panic limits.
         */
        LL, 
        /**
         * The result for a quantitative observation is above the upper limit of the reference range (as defined for the respective test procedure).

                        Synonym: Above high normal
         */
        H, 
        /**
         * A test result that is significantly higher than the reference (normal) or therapeutic interval, but has not reached the critically high value and might need special attention, as defined by the laboratory or the clinician.[Note: This level is situated between 'H' and 'HH'.]

                        
                           Deprecation Comment: The code 'H>' is being deprecated in order to align with the use of the code 'HU' for "Very high" in V2 Table 0078 "Interpretation Codes".

                        [Note: The use of code 'H>' is non-preferred, as this code is deprecated and on track to be retired; use code 'HU' instead.
         */
        H_, 
        /**
         * A test result that is significantly higher than the reference (normal) or therapeutic interval, but has not reached the critically high value and might need special attention, as defined by the laboratory or the clinician.
         */
        HU, 
        /**
         * The result for a quantitative observation is below the lower limit of the reference range (as defined for the respective test procedure).

                        Synonym: Below low normal
         */
        L, 
        /**
         * A test result that is significantly lower than the reference (normal) or therapeutic interval, but has not reached the critically low value and might need special attention, as defined by the laboratory or the clinician.[Note: This level is situated between 'L' and 'LL'.]

                        
                           Deprecation Comment: The code 'L<' is being deprecated in order to align with the use of the code 'LU' for "Very low" in V2 Table 0078 "Interpretation Codes".

                        [Note: The use of code 'L<' is non-preferred, as this code is deprecated and on track to be retired; use code 'LU' instead.
         */
        L_, 
        /**
         * A test result that is significantly lower than the reference (normal) or therapeutic interval, but has not reached the critically low value and might need special attention, as defined by the laboratory or the clinician.
         */
        LU, 
        /**
         * The result or observation value is within the reference range or expected norm (as defined for the respective test procedure).

                        [Note: Applies to numeric or non-numeric results.]
         */
        N, 
        /**
         * Interpretations of anti-microbial susceptibility testing results (microbiology). At most one allowed.
         */
        _OBSERVATIONINTERPRETATIONSUSCEPTIBILITY, 
        /**
         * Bacterial strain inhibited in vitro by a concentration of an antimicrobial agent that is associated with uncertain therapeutic effect. Reference: CLSI (http://www.clsi.org/Content/NavigationMenu/Resources/HarmonizedTerminologyDatabase/Harmonized_Terminolo.htm)
Projects: ISO 20776-1, ISO 20776-2

                        [Note 1: Bacterial strains are categorized as intermediate by applying the appropriate breakpoints in a defined phenotypic test system.]

                        [Note 2: This class of susceptibility implies that an infection due to the isolate can be appropriately treated in body sites where the drugs are physiologically concentrated or when a high dosage of drug can be used.]

                        [Note 3: This class also indicates a "buffer zone," to prevent small, uncontrolled, technical factors from causing major discrepancies in interpretations.]

                        [Note 4: These breakpoints can be altered due to changes in circumstances (e.g., changes in commonly used drug dosages, emergence of new resistance mechanisms).]
         */
        I, 
        /**
         * The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.

                        
                           
                              Deprecation Comment: 
                           This antimicrobial susceptibility test interpretation concept is recommended by OO to be deprecated as it is no longer recommended for use in susceptibility testing by CLSI (reference CLSI document M100-S22; Vol. 32 No.3; CLSI Performance Standards for Antimicrobial Susceptibility Testing; Twenty-Second Informational Supplement. Jan 2012).
         */
        MS, 
        /**
         * A category used for isolates for which only a susceptible interpretive criterion has been designated because of the absence or rare occurrence of resistant strains. Isolates that have MICs above or zone diameters below the value indicated for the susceptible breakpoint should be reported as non-susceptible.

                        NOTE 1: An isolate that is interpreted as non-susceptible does not necessarily mean that the isolate has a resistance mechanism. It is possible that isolates with MICs above the susceptible breakpoint that lack resistance mechanisms may be encountered within the wild-type distribution subsequent to the time the susceptible-only breakpoint is set. 

                        NOTE 2: For strains yielding results in the "nonsusceptible" category, organism identification and antimicrobial susceptibility test results should be confirmed.

                        Synonym: decreased susceptibility.
         */
        NS, 
        /**
         * Bacterial strain inhibited in vitro by a concentration of an antimicrobial agent that is associated with a high likelihood of therapeutic failure.
Reference: CLSI (http://www.clsi.org/Content/NavigationMenu/Resources/HarmonizedTerminologyDatabase/Harmonized_Terminolo.htm)  
Projects: ISO 20776-1, ISO 20776-2

                        [Note 1: Bacterial strains are categorized as resistant by applying the appropriate breakpoints in a defined phenotypic test system.]

                        [Note 2: This breakpoint can be altered due to changes in circumstances (e.g., changes in commonly used drug dosages, emergence of new resistance mechanisms).]
         */
        R, 
        /**
         * A category for isolates where the bacteria (e.g. enterococci) are not susceptible in vitro to a combination therapy (e.g., high-level aminoglycoside and cell wall active agent).  This is predictive that this combination therapy will not be effective. 

                        
                           Usage Note: Since the use of penicillin or ampicillin alone often results in treatment failure of serious enterococcal or other bacterial infections, combination therapy is usually indicated to enhance bactericidal activity. The synergy between a cell wall active agent (such as penicillin, ampicillin, or vancomycin) and an aminoglycoside (such as gentamicin, kanamycin or streptomycin) is best predicted by screening for high-level bacterial resistance to the aminoglycoside.

                        
                           Open Issue: The print name of the code is very general and the description is very specific to a pair of classes of agents, which may lead to confusion of these concepts in the future should other synergies be found.
         */
        SYNR, 
        /**
         * Bacterial strain inhibited by in vitro concentration of an antimicrobial agent that is associated with a high likelihood of therapeutic success.
Reference: CLSI (http://www.clsi.org/Content/NavigationMenu/Resources/HarmonizedTerminologyDatabase/Harmonized_Terminolo.htm)
Synonym (earlier term): Sensitive Projects: ISO 20776-1, ISO 20776-2

                        [Note 1: Bacterial strains are categorized as susceptible by applying the appropriate breakpoints in a defined phenotypic system.]

                        [Note 2: This breakpoint can be altered due to changes in circumstances (e.g., changes in commonly used drug dosages, emergence of new resistance mechanisms).]
         */
        S, 
        /**
         * A category that includes isolates with antimicrobial agent minimum inhibitory concentrations (MICs) that approach usually attainable blood and tissue levels and for which response rates may be lower than for susceptible isolates.

                        Reference: CLSI document M44-A2 2009 "Method for antifungal disk diffusion susceptibility testing of yeasts; approved guideline - second edition" - page 2.
         */
        SDD, 
        /**
         * A category for isolates where the bacteria (e.g. enterococci) are susceptible in vitro to a combination therapy (e.g., high-level aminoglycoside and cell wall active agent).  This is predictive that this combination therapy will be effective. 

                        
                           Usage Note: Since the use of penicillin or ampicillin alone often results in treatment failure of serious enterococcal or other bacterial infections, combination therapy is usually indicated to enhance bactericidal activity. The synergy between a cell wall active agent (such as penicillin, ampicillin, or vancomycin) and an aminoglycoside (such as gentamicin, kanamycin or streptomycin) is best predicted by screening for high-level bacterial resistance to the aminoglycoside.

                        
                           Open Issue: The print name of the code is very general and the description is very specific to a pair of classes of agents, which may lead to confusion of these concepts in the future should other synergies be found.
         */
        SYNS, 
        /**
         * The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.

                        
                           
                              Deprecation Comment: 
                           This antimicrobial susceptibility test interpretation concept is recommended by OO to be deprecated as it is no longer recommended for use in susceptibility testing by CLSI (reference CLSI document M100-S22; Vol. 32 No.3; CLSI Performance Standards for Antimicrobial Susceptibility Testing; Twenty-Second Informational Supplement. Jan 2012).
         */
        VS, 
        /**
         * The observation/test result is interpreted as being outside the inclusion range for a particular protocol within which the result is being reported.


                        Example: A positive result on a Hepatitis screening test.
                           Open Issue: EX, HX, LX: These three concepts do not seem to meet a clear need in the vocabulary, and their use in observation interpretation appears likely to be covered by other existing concepts (e.g., A, H, L).  The only apparent significant difference is their reference to use in protocols for exclusion of study subjects.
These concepts/codes were proposed by RCRIM for use in the CTLaboratory message.  They were submitted and approved in the November 2005 Harmonization cycle in proposal "030103C_VOCAB_RCRIM_l_quade_RCRIM Obs Interp_20051028154455".  However, this proposal was not fully implemented in the vocabulary.  The proposal recommended creation of the x_ClinicalResearchExclusion domain in ObservationInterpretation with a value set including those three concepts/codes, but there is no subdomain of that name or equivalent with a binding to either of the value sets that contain these concepts/codes.
Members of the OO WG have recently attempted to contact members of RCRIM regarding these concepts, both by email and at the recent WGM in Atlanta, without response.  It is felt by OO that the best course of action to take at this time is to add this comprehensive Open Issue rather than deprecate these three concepts at this time, until further discussion is held.
         */
        EX, 
        /**
         * The observation/test result is interpreted as being outside the inclusion range for a particular protocol within which the result is being reported.


                        Example: A positive result on a Hepatitis screening test.
                           Open Issue: EX, HX, LX: These three concepts do not seem to meet a clear need in the vocabulary, and their use in observation interpretation appears likely to be covered by other existing concepts (e.g., A, H, L).  The only apparent significant difference is their reference to use in protocols for exclusion of study subjects.  These concepts/codes were proposed by RCRIM for use in the CTLaboratory message.  They were submitted and approved in the November 2005 Harmonization cycle in proposal "030103C_VOCAB_RCRIM_l_quade_RCRIM Obs Interp_20051028154455". However, this proposal was not fully implemented in the vocabulary.  The proposal recommended creation of the x_ClinicalResearchExclusion domain in ObservationInterpretation with a value set including those three concepts/codes, but there is no subdomain of that name or equivalent with a binding to either of the value sets that contain these concepts/codes.  Members of the OO WG have recently attempted to contact members of RCRIM regarding these concepts, both by email and at the recent WGM in Atlanta, without response.  It is felt by OO that the best course of action to take at this time is to add this comprehensive Open Issue rather than deprecate these three concepts at this time, until further discussion is held.
         */
        HX, 
        /**
         * The numeric observation/test result is interpreted as being below the low threshold value for a particular protocol within which the result is being reported.

                        Example: A Total White Blood Cell Count falling below a protocol-defined threshold value of 3000/mm^3
                           Open Issue: EX, HX, LX: These three concepts do not seem to meet a clear need in the vocabulary, and their use in observation interpretation appears likely to be covered by other existing concepts (e.g., A, H, L).  The only apparent significant difference is their reference to use in protocols for exclusion of study subjects.  These concepts/codes were proposed by RCRIM for use in the CTLaboratory message.  They were submitted and approved in the November 2005 Harmonization cycle in proposal "030103C_VOCAB_RCRIM_l_quade_RCRIM Obs Interp_20051028154455".  However, this proposal was not fully implemented in the vocabulary.  The proposal recommended creation of the x_ClinicalResearchExclusion domain in ObservationInterpretation with a value set including those three concepts/codes, but there is no subdomain of that name or equivalent with a binding to either of the value sets that contain these concepts/codes.  Members of the OO WG have recently attempted to contact members of RCRIM regarding these concepts, both by email and at the recent WGM in Atlanta, without response.  It is felt by OO that the best course of action to take at this time is to add this comprehensive Open Issue rather than deprecate these three concepts at this time, until further discussion is held.
         */
        LX, 
        /**
         * Interpretations of the presence or absence of a component / analyte or organism in a test or of a sign in a clinical observation. In keeping with laboratory data processing practice, these concepts provide a categorical interpretation of the "meaning" of the quantitative value for the same observation.
         */
        OBSERVATIONINTERPRETATIONDETECTION, 
        /**
         * The specified component / analyte, organism or clinical sign could neither be declared positive / negative nor detected / not detected by the performed test or procedure.

                        
                           Usage Note: For example, if the specimen was degraded, poorly processed, or was missing the required anatomic structures, then "indeterminate" (i.e. "cannot be determined") is the appropriate response, not "equivocal".
         */
        IND, 
        /**
         * The test or procedure was successfully performed, but the results are borderline and can neither be declared positive / negative nor detected / not detected according to the current established criteria.
         */
        E, 
        /**
         * An absence finding of the specified component / analyte, organism or clinical sign based on the established threshold of the performed test or procedure.

                        [Note: Negative does not necessarily imply the complete absence of the specified item.]
         */
        NEG, 
        /**
         * The presence of the specified component / analyte, organism or clinical sign could not be determined within the limit of detection of the performed test or procedure.
         */
        ND, 
        /**
         * A presence finding of the specified component / analyte, organism or clinical sign based on the established threshold of the performed test or procedure.
         */
        POS, 
        /**
         * The measurement of the specified component / analyte, organism or clinical sign above the limit of detection of the performed test or procedure.
         */
        DET, 
        /**
         * Interpretation of the observed result taking into account additional information (contraindicators) about the patient's situation. Concepts in this category are mutually exclusive, i.e., at most one is allowed.
         */
        OBSERVATIONINTERPRETATIONEXPECTATION, 
        /**
         * This result has been evaluated in light of known contraindicators.  Once those contraindicators have been taken into account the result is determined to be "Expected"  (e.g., presence of drugs in a patient that is taking prescription medication for pain management).
         */
        EXP, 
        /**
         * This result has been evaluated in light of known contraindicators.  Once those contraindicators have been taken into account the result is determined to be "Unexpected" (e.g., presence of non-prescribed drugs in a patient that is taking prescription medication for pain management).
         */
        UNE, 
        /**
         * Interpretations of the presence and level of reactivity of the specified component / analyte with the reagent in the performed laboratory test.
         */
        REACTIVITYOBSERVATIONINTERPRETATION, 
        /**
         * An absence finding used to indicate that the specified component / analyte did not react measurably with the reagent.
         */
        NR, 
        /**
         * A presence finding used to indicate that the specified component / analyte reacted with the reagent above the reliably measurable limit of the performed test.
         */
        RR, 
        /**
         * A weighted presence finding used to indicate that the specified component / analyte reacted with the reagent, but below the reliably measurable limit of the performed test.
         */
        WR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ObservationInterpretation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_GeneticObservationInterpretation".equals(codeString))
          return _GENETICOBSERVATIONINTERPRETATION;
        if ("CAR".equals(codeString))
          return CAR;
        if ("Carrier".equals(codeString))
          return CARRIER;
        if ("_ObservationInterpretationChange".equals(codeString))
          return _OBSERVATIONINTERPRETATIONCHANGE;
        if ("B".equals(codeString))
          return B;
        if ("D".equals(codeString))
          return D;
        if ("U".equals(codeString))
          return U;
        if ("W".equals(codeString))
          return W;
        if ("_ObservationInterpretationExceptions".equals(codeString))
          return _OBSERVATIONINTERPRETATIONEXCEPTIONS;
        if ("<".equals(codeString))
          return LESS_THAN;
        if (">".equals(codeString))
          return GREATER_THAN;
        if ("AC".equals(codeString))
          return AC;
        if ("IE".equals(codeString))
          return IE;
        if ("QCF".equals(codeString))
          return QCF;
        if ("TOX".equals(codeString))
          return TOX;
        if ("_ObservationInterpretationNormality".equals(codeString))
          return _OBSERVATIONINTERPRETATIONNORMALITY;
        if ("A".equals(codeString))
          return A;
        if ("AA".equals(codeString))
          return AA;
        if ("HH".equals(codeString))
          return HH;
        if ("LL".equals(codeString))
          return LL;
        if ("H".equals(codeString))
          return H;
        if ("H>".equals(codeString))
          return H_;
        if ("HU".equals(codeString))
          return HU;
        if ("L".equals(codeString))
          return L;
        if ("L<".equals(codeString))
          return L_;
        if ("LU".equals(codeString))
          return LU;
        if ("N".equals(codeString))
          return N;
        if ("_ObservationInterpretationSusceptibility".equals(codeString))
          return _OBSERVATIONINTERPRETATIONSUSCEPTIBILITY;
        if ("I".equals(codeString))
          return I;
        if ("MS".equals(codeString))
          return MS;
        if ("NS".equals(codeString))
          return NS;
        if ("R".equals(codeString))
          return R;
        if ("SYN-R".equals(codeString))
          return SYNR;
        if ("S".equals(codeString))
          return S;
        if ("SDD".equals(codeString))
          return SDD;
        if ("SYN-S".equals(codeString))
          return SYNS;
        if ("VS".equals(codeString))
          return VS;
        if ("EX".equals(codeString))
          return EX;
        if ("HX".equals(codeString))
          return HX;
        if ("LX".equals(codeString))
          return LX;
        if ("ObservationInterpretationDetection".equals(codeString))
          return OBSERVATIONINTERPRETATIONDETECTION;
        if ("IND".equals(codeString))
          return IND;
        if ("E".equals(codeString))
          return E;
        if ("NEG".equals(codeString))
          return NEG;
        if ("ND".equals(codeString))
          return ND;
        if ("POS".equals(codeString))
          return POS;
        if ("DET".equals(codeString))
          return DET;
        if ("ObservationInterpretationExpectation".equals(codeString))
          return OBSERVATIONINTERPRETATIONEXPECTATION;
        if ("EXP".equals(codeString))
          return EXP;
        if ("UNE".equals(codeString))
          return UNE;
        if ("ReactivityObservationInterpretation".equals(codeString))
          return REACTIVITYOBSERVATIONINTERPRETATION;
        if ("NR".equals(codeString))
          return NR;
        if ("RR".equals(codeString))
          return RR;
        if ("WR".equals(codeString))
          return WR;
        throw new FHIRException("Unknown V3ObservationInterpretation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _GENETICOBSERVATIONINTERPRETATION: return "_GeneticObservationInterpretation";
            case CAR: return "CAR";
            case CARRIER: return "Carrier";
            case _OBSERVATIONINTERPRETATIONCHANGE: return "_ObservationInterpretationChange";
            case B: return "B";
            case D: return "D";
            case U: return "U";
            case W: return "W";
            case _OBSERVATIONINTERPRETATIONEXCEPTIONS: return "_ObservationInterpretationExceptions";
            case LESS_THAN: return "<";
            case GREATER_THAN: return ">";
            case AC: return "AC";
            case IE: return "IE";
            case QCF: return "QCF";
            case TOX: return "TOX";
            case _OBSERVATIONINTERPRETATIONNORMALITY: return "_ObservationInterpretationNormality";
            case A: return "A";
            case AA: return "AA";
            case HH: return "HH";
            case LL: return "LL";
            case H: return "H";
            case H_: return "H>";
            case HU: return "HU";
            case L: return "L";
            case L_: return "L<";
            case LU: return "LU";
            case N: return "N";
            case _OBSERVATIONINTERPRETATIONSUSCEPTIBILITY: return "_ObservationInterpretationSusceptibility";
            case I: return "I";
            case MS: return "MS";
            case NS: return "NS";
            case R: return "R";
            case SYNR: return "SYN-R";
            case S: return "S";
            case SDD: return "SDD";
            case SYNS: return "SYN-S";
            case VS: return "VS";
            case EX: return "EX";
            case HX: return "HX";
            case LX: return "LX";
            case OBSERVATIONINTERPRETATIONDETECTION: return "ObservationInterpretationDetection";
            case IND: return "IND";
            case E: return "E";
            case NEG: return "NEG";
            case ND: return "ND";
            case POS: return "POS";
            case DET: return "DET";
            case OBSERVATIONINTERPRETATIONEXPECTATION: return "ObservationInterpretationExpectation";
            case EXP: return "EXP";
            case UNE: return "UNE";
            case REACTIVITYOBSERVATIONINTERPRETATION: return "ReactivityObservationInterpretation";
            case NR: return "NR";
            case RR: return "RR";
            case WR: return "WR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ObservationInterpretation";
        }
        public String getDefinition() {
          switch (this) {
            case _GENETICOBSERVATIONINTERPRETATION: return "Codes that specify interpretation of genetic analysis, such as \"positive\", \"negative\", \"carrier\", \"responsive\", etc.";
            case CAR: return "The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.";
            case CARRIER: return "The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This code is currently the same string as the print name for this concept and is inconsistent with the conventions being used for the other codes in the coding system, as it is a full word with initial capitalization, rather than an all upper case mnemonic.  The recommendation from OO is to deprecate the code \"Carrier\" and to add \"CAR\" as the new active code representation for this concept.";
            case _OBSERVATIONINTERPRETATIONCHANGE: return "Interpretations of change of quantity and/or severity. At most one of B or W and one of U or D allowed.";
            case B: return "The current result or observation value has improved compared to the previous result or observation value (the change is significant as defined in the respective test procedure).\r\n\n                        [Note: This can be applied to quantitative or qualitative observations.]";
            case D: return "The current result has decreased from the previous result for a quantitative observation (the change is significant as defined in the respective test procedure).";
            case U: return "The current result has increased from the previous result for a quantitative observation (the change is significant as defined in the respective test procedure).";
            case W: return "The current result or observation value has degraded compared to the previous result or observation value (the change is significant as defined in the respective test procedure).\r\n\n                        [Note: This can be applied to quantitative or qualitative observations.]";
            case _OBSERVATIONINTERPRETATIONEXCEPTIONS: return "Technical exceptions resulting in the inability to provide an interpretation. At most one allowed. Does not imply normality or severity.";
            case LESS_THAN: return "The result is below the minimum detection limit (the test procedure or equipment is the limiting factor).\r\n\n                        Synonyms: Below analytical limit, low off scale.";
            case GREATER_THAN: return "The result is above the maximum quantifiable limit (the test procedure or equipment is the limiting factor).\r\n\n                        Synonyms: Above analytical limit, high off scale.";
            case AC: return "A valid result cannot be obtained for the specified component / analyte due to the presence of anti-complementary substances in the sample.";
            case IE: return "There is insufficient evidence that the species in question is a good target for therapy with the drug.  A categorical interpretation is not possible.\r\n\n                        [Note: A MIC with \"IE\" and/or a comment may be reported (without an accompanying S, I or R-categorization).]";
            case QCF: return "A result cannot be considered valid for the specified component / analyte or organism due to failure in the quality control testing component.";
            case TOX: return "A valid result cannot be obtained for the specified organism or cell line due to the presence of cytotoxic substances in the sample or culture.";
            case _OBSERVATIONINTERPRETATIONNORMALITY: return "Interpretation of normality or degree of abnormality (including critical or \"alert\" level). Concepts in this category are mutually exclusive, i.e., at most one is allowed.";
            case A: return "The result or observation value is outside the reference range or expected norm (as defined for the respective test procedure).\r\n\n                        [Note: Typically applies to non-numeric results.]";
            case AA: return "The result or observation value is outside a reference range or expected norm at a level at which immediate action should be considered for patient safety (as defined for the respective test procedure).\r\n\n                        [Note: Typically applies to non-numeric results.  Analogous to critical/panic limits for numeric results.]";
            case HH: return "The result for a quantitative observation is above a reference level at which immediate action should be considered for patient safety (as defined for the respective test procedure).\r\n\n                        Synonym: Above upper panic limits.";
            case LL: return "The result for a quantitative observation is below a reference level at which immediate action should be considered for patient safety (as defined for the respective test procedure).\r\n\n                        Synonym: Below lower panic limits.";
            case H: return "The result for a quantitative observation is above the upper limit of the reference range (as defined for the respective test procedure).\r\n\n                        Synonym: Above high normal";
            case H_: return "A test result that is significantly higher than the reference (normal) or therapeutic interval, but has not reached the critically high value and might need special attention, as defined by the laboratory or the clinician.[Note: This level is situated between 'H' and 'HH'.]\r\n\n                        \n                           Deprecation Comment: The code 'H>' is being deprecated in order to align with the use of the code 'HU' for \"Very high\" in V2 Table 0078 \"Interpretation Codes\".\r\n\n                        [Note: The use of code 'H>' is non-preferred, as this code is deprecated and on track to be retired; use code 'HU' instead.";
            case HU: return "A test result that is significantly higher than the reference (normal) or therapeutic interval, but has not reached the critically high value and might need special attention, as defined by the laboratory or the clinician.";
            case L: return "The result for a quantitative observation is below the lower limit of the reference range (as defined for the respective test procedure).\r\n\n                        Synonym: Below low normal";
            case L_: return "A test result that is significantly lower than the reference (normal) or therapeutic interval, but has not reached the critically low value and might need special attention, as defined by the laboratory or the clinician.[Note: This level is situated between 'L' and 'LL'.]\r\n\n                        \n                           Deprecation Comment: The code 'L<' is being deprecated in order to align with the use of the code 'LU' for \"Very low\" in V2 Table 0078 \"Interpretation Codes\".\r\n\n                        [Note: The use of code 'L<' is non-preferred, as this code is deprecated and on track to be retired; use code 'LU' instead.";
            case LU: return "A test result that is significantly lower than the reference (normal) or therapeutic interval, but has not reached the critically low value and might need special attention, as defined by the laboratory or the clinician.";
            case N: return "The result or observation value is within the reference range or expected norm (as defined for the respective test procedure).\r\n\n                        [Note: Applies to numeric or non-numeric results.]";
            case _OBSERVATIONINTERPRETATIONSUSCEPTIBILITY: return "Interpretations of anti-microbial susceptibility testing results (microbiology). At most one allowed.";
            case I: return "Bacterial strain inhibited in vitro by a concentration of an antimicrobial agent that is associated with uncertain therapeutic effect. Reference: CLSI (http://www.clsi.org/Content/NavigationMenu/Resources/HarmonizedTerminologyDatabase/Harmonized_Terminolo.htm)\nProjects: ISO 20776-1, ISO 20776-2\r\n\n                        [Note 1: Bacterial strains are categorized as intermediate by applying the appropriate breakpoints in a defined phenotypic test system.]\r\n\n                        [Note 2: This class of susceptibility implies that an infection due to the isolate can be appropriately treated in body sites where the drugs are physiologically concentrated or when a high dosage of drug can be used.]\r\n\n                        [Note 3: This class also indicates a \"buffer zone,\" to prevent small, uncontrolled, technical factors from causing major discrepancies in interpretations.]\r\n\n                        [Note 4: These breakpoints can be altered due to changes in circumstances (e.g., changes in commonly used drug dosages, emergence of new resistance mechanisms).]";
            case MS: return "The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This antimicrobial susceptibility test interpretation concept is recommended by OO to be deprecated as it is no longer recommended for use in susceptibility testing by CLSI (reference CLSI document M100-S22; Vol. 32 No.3; CLSI Performance Standards for Antimicrobial Susceptibility Testing; Twenty-Second Informational Supplement. Jan 2012).";
            case NS: return "A category used for isolates for which only a susceptible interpretive criterion has been designated because of the absence or rare occurrence of resistant strains. Isolates that have MICs above or zone diameters below the value indicated for the susceptible breakpoint should be reported as non-susceptible.\r\n\n                        NOTE 1: An isolate that is interpreted as non-susceptible does not necessarily mean that the isolate has a resistance mechanism. It is possible that isolates with MICs above the susceptible breakpoint that lack resistance mechanisms may be encountered within the wild-type distribution subsequent to the time the susceptible-only breakpoint is set. \r\n\n                        NOTE 2: For strains yielding results in the \"nonsusceptible\" category, organism identification and antimicrobial susceptibility test results should be confirmed.\r\n\n                        Synonym: decreased susceptibility.";
            case R: return "Bacterial strain inhibited in vitro by a concentration of an antimicrobial agent that is associated with a high likelihood of therapeutic failure.\nReference: CLSI (http://www.clsi.org/Content/NavigationMenu/Resources/HarmonizedTerminologyDatabase/Harmonized_Terminolo.htm)  \nProjects: ISO 20776-1, ISO 20776-2\r\n\n                        [Note 1: Bacterial strains are categorized as resistant by applying the appropriate breakpoints in a defined phenotypic test system.]\r\n\n                        [Note 2: This breakpoint can be altered due to changes in circumstances (e.g., changes in commonly used drug dosages, emergence of new resistance mechanisms).]";
            case SYNR: return "A category for isolates where the bacteria (e.g. enterococci) are not susceptible in vitro to a combination therapy (e.g., high-level aminoglycoside and cell wall active agent).  This is predictive that this combination therapy will not be effective. \r\n\n                        \n                           Usage Note: Since the use of penicillin or ampicillin alone often results in treatment failure of serious enterococcal or other bacterial infections, combination therapy is usually indicated to enhance bactericidal activity. The synergy between a cell wall active agent (such as penicillin, ampicillin, or vancomycin) and an aminoglycoside (such as gentamicin, kanamycin or streptomycin) is best predicted by screening for high-level bacterial resistance to the aminoglycoside.\r\n\n                        \n                           Open Issue: The print name of the code is very general and the description is very specific to a pair of classes of agents, which may lead to confusion of these concepts in the future should other synergies be found.";
            case S: return "Bacterial strain inhibited by in vitro concentration of an antimicrobial agent that is associated with a high likelihood of therapeutic success.\nReference: CLSI (http://www.clsi.org/Content/NavigationMenu/Resources/HarmonizedTerminologyDatabase/Harmonized_Terminolo.htm)\nSynonym (earlier term): Sensitive Projects: ISO 20776-1, ISO 20776-2\r\n\n                        [Note 1: Bacterial strains are categorized as susceptible by applying the appropriate breakpoints in a defined phenotypic system.]\r\n\n                        [Note 2: This breakpoint can be altered due to changes in circumstances (e.g., changes in commonly used drug dosages, emergence of new resistance mechanisms).]";
            case SDD: return "A category that includes isolates with antimicrobial agent minimum inhibitory concentrations (MICs) that approach usually attainable blood and tissue levels and for which response rates may be lower than for susceptible isolates.\r\n\n                        Reference: CLSI document M44-A2 2009 \"Method for antifungal disk diffusion susceptibility testing of yeasts; approved guideline - second edition\" - page 2.";
            case SYNS: return "A category for isolates where the bacteria (e.g. enterococci) are susceptible in vitro to a combination therapy (e.g., high-level aminoglycoside and cell wall active agent).  This is predictive that this combination therapy will be effective. \r\n\n                        \n                           Usage Note: Since the use of penicillin or ampicillin alone often results in treatment failure of serious enterococcal or other bacterial infections, combination therapy is usually indicated to enhance bactericidal activity. The synergy between a cell wall active agent (such as penicillin, ampicillin, or vancomycin) and an aminoglycoside (such as gentamicin, kanamycin or streptomycin) is best predicted by screening for high-level bacterial resistance to the aminoglycoside.\r\n\n                        \n                           Open Issue: The print name of the code is very general and the description is very specific to a pair of classes of agents, which may lead to confusion of these concepts in the future should other synergies be found.";
            case VS: return "The patient is considered as carrier based on the testing results. A carrier is an individual who carries an altered form of a gene which can lead to having a child or offspring in future generations with a genetic disorder.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This antimicrobial susceptibility test interpretation concept is recommended by OO to be deprecated as it is no longer recommended for use in susceptibility testing by CLSI (reference CLSI document M100-S22; Vol. 32 No.3; CLSI Performance Standards for Antimicrobial Susceptibility Testing; Twenty-Second Informational Supplement. Jan 2012).";
            case EX: return "The observation/test result is interpreted as being outside the inclusion range for a particular protocol within which the result is being reported.\n\r\n\n                        Example: A positive result on a Hepatitis screening test.\n                           Open Issue: EX, HX, LX: These three concepts do not seem to meet a clear need in the vocabulary, and their use in observation interpretation appears likely to be covered by other existing concepts (e.g., A, H, L).  The only apparent significant difference is their reference to use in protocols for exclusion of study subjects.\nThese concepts/codes were proposed by RCRIM for use in the CTLaboratory message.  They were submitted and approved in the November 2005 Harmonization cycle in proposal \"030103C_VOCAB_RCRIM_l_quade_RCRIM Obs Interp_20051028154455\".  However, this proposal was not fully implemented in the vocabulary.  The proposal recommended creation of the x_ClinicalResearchExclusion domain in ObservationInterpretation with a value set including those three concepts/codes, but there is no subdomain of that name or equivalent with a binding to either of the value sets that contain these concepts/codes.\nMembers of the OO WG have recently attempted to contact members of RCRIM regarding these concepts, both by email and at the recent WGM in Atlanta, without response.  It is felt by OO that the best course of action to take at this time is to add this comprehensive Open Issue rather than deprecate these three concepts at this time, until further discussion is held.";
            case HX: return "The observation/test result is interpreted as being outside the inclusion range for a particular protocol within which the result is being reported.\n\r\n\n                        Example: A positive result on a Hepatitis screening test.\n                           Open Issue: EX, HX, LX: These three concepts do not seem to meet a clear need in the vocabulary, and their use in observation interpretation appears likely to be covered by other existing concepts (e.g., A, H, L).  The only apparent significant difference is their reference to use in protocols for exclusion of study subjects.  These concepts/codes were proposed by RCRIM for use in the CTLaboratory message.  They were submitted and approved in the November 2005 Harmonization cycle in proposal \"030103C_VOCAB_RCRIM_l_quade_RCRIM Obs Interp_20051028154455\". However, this proposal was not fully implemented in the vocabulary.  The proposal recommended creation of the x_ClinicalResearchExclusion domain in ObservationInterpretation with a value set including those three concepts/codes, but there is no subdomain of that name or equivalent with a binding to either of the value sets that contain these concepts/codes.  Members of the OO WG have recently attempted to contact members of RCRIM regarding these concepts, both by email and at the recent WGM in Atlanta, without response.  It is felt by OO that the best course of action to take at this time is to add this comprehensive Open Issue rather than deprecate these three concepts at this time, until further discussion is held.";
            case LX: return "The numeric observation/test result is interpreted as being below the low threshold value for a particular protocol within which the result is being reported.\r\n\n                        Example: A Total White Blood Cell Count falling below a protocol-defined threshold value of 3000/mm^3\n                           Open Issue: EX, HX, LX: These three concepts do not seem to meet a clear need in the vocabulary, and their use in observation interpretation appears likely to be covered by other existing concepts (e.g., A, H, L).  The only apparent significant difference is their reference to use in protocols for exclusion of study subjects.  These concepts/codes were proposed by RCRIM for use in the CTLaboratory message.  They were submitted and approved in the November 2005 Harmonization cycle in proposal \"030103C_VOCAB_RCRIM_l_quade_RCRIM Obs Interp_20051028154455\".  However, this proposal was not fully implemented in the vocabulary.  The proposal recommended creation of the x_ClinicalResearchExclusion domain in ObservationInterpretation with a value set including those three concepts/codes, but there is no subdomain of that name or equivalent with a binding to either of the value sets that contain these concepts/codes.  Members of the OO WG have recently attempted to contact members of RCRIM regarding these concepts, both by email and at the recent WGM in Atlanta, without response.  It is felt by OO that the best course of action to take at this time is to add this comprehensive Open Issue rather than deprecate these three concepts at this time, until further discussion is held.";
            case OBSERVATIONINTERPRETATIONDETECTION: return "Interpretations of the presence or absence of a component / analyte or organism in a test or of a sign in a clinical observation. In keeping with laboratory data processing practice, these concepts provide a categorical interpretation of the \"meaning\" of the quantitative value for the same observation.";
            case IND: return "The specified component / analyte, organism or clinical sign could neither be declared positive / negative nor detected / not detected by the performed test or procedure.\r\n\n                        \n                           Usage Note: For example, if the specimen was degraded, poorly processed, or was missing the required anatomic structures, then \"indeterminate\" (i.e. \"cannot be determined\") is the appropriate response, not \"equivocal\".";
            case E: return "The test or procedure was successfully performed, but the results are borderline and can neither be declared positive / negative nor detected / not detected according to the current established criteria.";
            case NEG: return "An absence finding of the specified component / analyte, organism or clinical sign based on the established threshold of the performed test or procedure.\r\n\n                        [Note: Negative does not necessarily imply the complete absence of the specified item.]";
            case ND: return "The presence of the specified component / analyte, organism or clinical sign could not be determined within the limit of detection of the performed test or procedure.";
            case POS: return "A presence finding of the specified component / analyte, organism or clinical sign based on the established threshold of the performed test or procedure.";
            case DET: return "The measurement of the specified component / analyte, organism or clinical sign above the limit of detection of the performed test or procedure.";
            case OBSERVATIONINTERPRETATIONEXPECTATION: return "Interpretation of the observed result taking into account additional information (contraindicators) about the patient's situation. Concepts in this category are mutually exclusive, i.e., at most one is allowed.";
            case EXP: return "This result has been evaluated in light of known contraindicators.  Once those contraindicators have been taken into account the result is determined to be \"Expected\"  (e.g., presence of drugs in a patient that is taking prescription medication for pain management).";
            case UNE: return "This result has been evaluated in light of known contraindicators.  Once those contraindicators have been taken into account the result is determined to be \"Unexpected\" (e.g., presence of non-prescribed drugs in a patient that is taking prescription medication for pain management).";
            case REACTIVITYOBSERVATIONINTERPRETATION: return "Interpretations of the presence and level of reactivity of the specified component / analyte with the reagent in the performed laboratory test.";
            case NR: return "An absence finding used to indicate that the specified component / analyte did not react measurably with the reagent.";
            case RR: return "A presence finding used to indicate that the specified component / analyte reacted with the reagent above the reliably measurable limit of the performed test.";
            case WR: return "A weighted presence finding used to indicate that the specified component / analyte reacted with the reagent, but below the reliably measurable limit of the performed test.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _GENETICOBSERVATIONINTERPRETATION: return "GeneticObservationInterpretation";
            case CAR: return "Carrier";
            case CARRIER: return "Carrier";
            case _OBSERVATIONINTERPRETATIONCHANGE: return "ObservationInterpretationChange";
            case B: return "Better";
            case D: return "Significant change down";
            case U: return "Significant change up";
            case W: return "Worse";
            case _OBSERVATIONINTERPRETATIONEXCEPTIONS: return "ObservationInterpretationExceptions";
            case LESS_THAN: return "Off scale low";
            case GREATER_THAN: return "Off scale high";
            case AC: return "Anti-complementary substances present";
            case IE: return "Insufficient evidence";
            case QCF: return "Quality control failure";
            case TOX: return "Cytotoxic substance present";
            case _OBSERVATIONINTERPRETATIONNORMALITY: return "ObservationInterpretationNormality";
            case A: return "Abnormal";
            case AA: return "Critical abnormal";
            case HH: return "Critical high";
            case LL: return "Critical low";
            case H: return "High";
            case H_: return "Significantly high";
            case HU: return "Significantly high";
            case L: return "Low";
            case L_: return "Significantly low";
            case LU: return "Significantly low";
            case N: return "Normal";
            case _OBSERVATIONINTERPRETATIONSUSCEPTIBILITY: return "ObservationInterpretationSusceptibility";
            case I: return "Intermediate";
            case MS: return "moderately susceptible";
            case NS: return "Non-susceptible";
            case R: return "Resistant";
            case SYNR: return "Synergy - resistant";
            case S: return "Susceptible";
            case SDD: return "Susceptible-dose dependent";
            case SYNS: return "Synergy - susceptible";
            case VS: return "very susceptible";
            case EX: return "outside threshold";
            case HX: return "above high threshold";
            case LX: return "below low threshold";
            case OBSERVATIONINTERPRETATIONDETECTION: return "ObservationInterpretationDetection";
            case IND: return "Indeterminate";
            case E: return "Equivocal";
            case NEG: return "Negative";
            case ND: return "Not detected";
            case POS: return "Positive";
            case DET: return "Detected";
            case OBSERVATIONINTERPRETATIONEXPECTATION: return "ObservationInterpretationExpectation";
            case EXP: return "Expected";
            case UNE: return "Unexpected";
            case REACTIVITYOBSERVATIONINTERPRETATION: return "ReactivityObservationInterpretation";
            case NR: return "Non-reactive";
            case RR: return "Reactive";
            case WR: return "Weakly reactive";
            default: return "?";
          }
    }


}

