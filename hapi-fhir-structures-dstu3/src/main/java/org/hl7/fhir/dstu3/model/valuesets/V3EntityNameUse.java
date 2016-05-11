package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EntityNameUse {

        /**
         * Identifies the different representations of a name.  The representation may affect how the name is used.  (E.g. use of Ideographic for formal communications.)
         */
        _NAMEREPRESENTATIONUSE, 
        /**
         * Alphabetic transcription of name (Japanese: romaji)
         */
        ABC, 
        /**
         * Ideographic representation of name (e.g., Japanese kanji, Chinese characters)
         */
        IDE, 
        /**
         * Syllabic transcription of name (e.g., Japanese kana, Korean hangul)
         */
        SYL, 
        /**
         * A name assigned to a person. Reasons some organizations assign alternate names may include not knowing the person's name, or to maintain anonymity. Some, but not necessarily all, of the name types that people call "alias" may fit into this category.
         */
        ASGN, 
        /**
         * As recorded on a license, record, certificate, etc. (only if different from legal name)
         */
        C, 
        /**
         * e.g. Chief Red Cloud
         */
        I, 
        /**
         * Known as/conventional/the one you use
         */
        L, 
        /**
         * Definition:The formal name as registered in an official (government) registry, but which name might not be commonly used. Particularly used in countries with a law system based on Napoleonic law.
         */
        OR, 
        /**
         * A self asserted name that the person is using or has used.
         */
        P, 
        /**
         * Includes writer's pseudonym, stage name, etc
         */
        A, 
        /**
         * e.g. Sister Mary Francis, Brother John
         */
        R, 
        /**
         * A name intended for use in searching or matching.
         */
        SRCH, 
        /**
         * A name spelled phonetically.

                        There are a variety of phonetic spelling algorithms. This code value does not distinguish between these.Discussion:
         */
        PHON, 
        /**
         * A name spelled according to the SoundEx algorithm.
         */
        SNDX, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNameUse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_NameRepresentationUse".equals(codeString))
          return _NAMEREPRESENTATIONUSE;
        if ("ABC".equals(codeString))
          return ABC;
        if ("IDE".equals(codeString))
          return IDE;
        if ("SYL".equals(codeString))
          return SYL;
        if ("ASGN".equals(codeString))
          return ASGN;
        if ("C".equals(codeString))
          return C;
        if ("I".equals(codeString))
          return I;
        if ("L".equals(codeString))
          return L;
        if ("OR".equals(codeString))
          return OR;
        if ("P".equals(codeString))
          return P;
        if ("A".equals(codeString))
          return A;
        if ("R".equals(codeString))
          return R;
        if ("SRCH".equals(codeString))
          return SRCH;
        if ("PHON".equals(codeString))
          return PHON;
        if ("SNDX".equals(codeString))
          return SNDX;
        throw new FHIRException("Unknown V3EntityNameUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _NAMEREPRESENTATIONUSE: return "_NameRepresentationUse";
            case ABC: return "ABC";
            case IDE: return "IDE";
            case SYL: return "SYL";
            case ASGN: return "ASGN";
            case C: return "C";
            case I: return "I";
            case L: return "L";
            case OR: return "OR";
            case P: return "P";
            case A: return "A";
            case R: return "R";
            case SRCH: return "SRCH";
            case PHON: return "PHON";
            case SNDX: return "SNDX";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNameUse";
        }
        public String getDefinition() {
          switch (this) {
            case _NAMEREPRESENTATIONUSE: return "Identifies the different representations of a name.  The representation may affect how the name is used.  (E.g. use of Ideographic for formal communications.)";
            case ABC: return "Alphabetic transcription of name (Japanese: romaji)";
            case IDE: return "Ideographic representation of name (e.g., Japanese kanji, Chinese characters)";
            case SYL: return "Syllabic transcription of name (e.g., Japanese kana, Korean hangul)";
            case ASGN: return "A name assigned to a person. Reasons some organizations assign alternate names may include not knowing the person's name, or to maintain anonymity. Some, but not necessarily all, of the name types that people call \"alias\" may fit into this category.";
            case C: return "As recorded on a license, record, certificate, etc. (only if different from legal name)";
            case I: return "e.g. Chief Red Cloud";
            case L: return "Known as/conventional/the one you use";
            case OR: return "Definition:The formal name as registered in an official (government) registry, but which name might not be commonly used. Particularly used in countries with a law system based on Napoleonic law.";
            case P: return "A self asserted name that the person is using or has used.";
            case A: return "Includes writer's pseudonym, stage name, etc";
            case R: return "e.g. Sister Mary Francis, Brother John";
            case SRCH: return "A name intended for use in searching or matching.";
            case PHON: return "A name spelled phonetically.\r\n\n                        There are a variety of phonetic spelling algorithms. This code value does not distinguish between these.Discussion:";
            case SNDX: return "A name spelled according to the SoundEx algorithm.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _NAMEREPRESENTATIONUSE: return "NameRepresentationUse";
            case ABC: return "Alphabetic";
            case IDE: return "Ideographic";
            case SYL: return "Syllabic";
            case ASGN: return "assigned";
            case C: return "License";
            case I: return "Indigenous/Tribal";
            case L: return "Legal";
            case OR: return "official registry";
            case P: return "pseudonym";
            case A: return "Artist/Stage";
            case R: return "Religious";
            case SRCH: return "search";
            case PHON: return "phonetic";
            case SNDX: return "Soundex";
            default: return "?";
          }
    }


}

