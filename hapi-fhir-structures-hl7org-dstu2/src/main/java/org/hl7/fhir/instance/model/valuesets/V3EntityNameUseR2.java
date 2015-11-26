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


public enum V3EntityNameUseR2 {

        /**
         * Description:A name that a person has assumed or has been assumed to them.
         */
        ASSUMED, 
        /**
         * Description:A name used in a Professional or Business context .  Examples: Continuing to use a maiden name in a professional context, or using a stage performing name (some of these names are also pseudonyms)
         */
        A, 
        /**
         * Description:Anonymous assigned name (used to protect a persons identity for privacy reasons)
         */
        ANON, 
        /**
         * Description:e.g. .  Chief Red Cloud
         */
        I, 
        /**
         * Description:A non-official name by which the person is sometimes known.  (This may also be used to record informal names such as a nickname)
         */
        P, 
        /**
         * Description:A name assumed as part of a religious vocation .  e.g. .  Sister Mary Francis, Brother John
         */
        R, 
        /**
         * Description:Known as/conventional/the one you normally use
         */
        C, 
        /**
         * Description:A name used prior to marriage.Note that marriage naming customs vary greatly around the world.  This name use is for use by applications that collect and store maiden names.  Though the concept of maiden name is often gender specific, the use of this term is not gender specific.  The use of this term does not imply any particular history for a person's name, nor should the maiden name be determined algorithmically
         */
        M, 
        /**
         * Description:Identifies the different representations of a name .  The representation may affect how the name is used .  (E.g. .  use of Ideographic for formal communications.)
         */
        NAMEREPRESENTATIONUSE, 
        /**
         * Description:Alphabetic transcription of name in latin alphabet (Japanese: romaji)
         */
        ABC, 
        /**
         * Description:Ideographic representation of name (e.g. Japanese kanji, Chinese characters)
         */
        IDE, 
        /**
         * Description:Syllabic transcription of name (e.g. Japanese kana, Korean hangul)
         */
        SYL, 
        /**
         * Description:This name is no longer in use (note: Names may also carry valid time ranges .  This code is used to cover the situations where it is known that the name is no longer valid, but no particular time range for its use is known)
         */
        OLD, 
        /**
         * Description:This name should no longer be used when interacting with the person (i.e .  in addition to no longer being used, the name should not be even mentioned when interacting with the person)Note: applications are not required to compare names labeled "Do Not Use" and other names in order to eliminate name parts that are common between the other name and a name labeled "Do Not Use".
         */
        DN, 
        /**
         * Description:The formal name as registered in an official (government) registry, but which name might not be commonly used .  May correspond to the concept of legal name
         */
        OR, 
        /**
         * Description:The name as understood by the data enterer, i.e. a close approximation of a phonetic spelling of the name, not based on a phonetic algorithm.
         */
        PHON, 
        /**
         * Description:A name intended for use in searching or matching.  This is used when the name is incomplete and contains enough details for search matching, but not enough for other uses.
         */
        SRCH, 
        /**
         * Description:A temporary name.  Note that a name valid time can provide more detailed information.  This may also be used for temporary names assigned at birth or in emergency situations.
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNameUseR2 fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Assumed".equals(codeString))
          return ASSUMED;
        if ("A".equals(codeString))
          return A;
        if ("ANON".equals(codeString))
          return ANON;
        if ("I".equals(codeString))
          return I;
        if ("P".equals(codeString))
          return P;
        if ("R".equals(codeString))
          return R;
        if ("C".equals(codeString))
          return C;
        if ("M".equals(codeString))
          return M;
        if ("NameRepresentationUse".equals(codeString))
          return NAMEREPRESENTATIONUSE;
        if ("ABC".equals(codeString))
          return ABC;
        if ("IDE".equals(codeString))
          return IDE;
        if ("SYL".equals(codeString))
          return SYL;
        if ("OLD".equals(codeString))
          return OLD;
        if ("DN".equals(codeString))
          return DN;
        if ("OR".equals(codeString))
          return OR;
        if ("PHON".equals(codeString))
          return PHON;
        if ("SRCH".equals(codeString))
          return SRCH;
        if ("T".equals(codeString))
          return T;
        throw new Exception("Unknown V3EntityNameUseR2 code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ASSUMED: return "Assumed";
            case A: return "A";
            case ANON: return "ANON";
            case I: return "I";
            case P: return "P";
            case R: return "R";
            case C: return "C";
            case M: return "M";
            case NAMEREPRESENTATIONUSE: return "NameRepresentationUse";
            case ABC: return "ABC";
            case IDE: return "IDE";
            case SYL: return "SYL";
            case OLD: return "OLD";
            case DN: return "DN";
            case OR: return "OR";
            case PHON: return "PHON";
            case SRCH: return "SRCH";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNameUseR2";
        }
        public String getDefinition() {
          switch (this) {
            case ASSUMED: return "Description:A name that a person has assumed or has been assumed to them.";
            case A: return "Description:A name used in a Professional or Business context .  Examples: Continuing to use a maiden name in a professional context, or using a stage performing name (some of these names are also pseudonyms)";
            case ANON: return "Description:Anonymous assigned name (used to protect a persons identity for privacy reasons)";
            case I: return "Description:e.g. .  Chief Red Cloud";
            case P: return "Description:A non-official name by which the person is sometimes known.  (This may also be used to record informal names such as a nickname)";
            case R: return "Description:A name assumed as part of a religious vocation .  e.g. .  Sister Mary Francis, Brother John";
            case C: return "Description:Known as/conventional/the one you normally use";
            case M: return "Description:A name used prior to marriage.Note that marriage naming customs vary greatly around the world.  This name use is for use by applications that collect and store maiden names.  Though the concept of maiden name is often gender specific, the use of this term is not gender specific.  The use of this term does not imply any particular history for a person's name, nor should the maiden name be determined algorithmically";
            case NAMEREPRESENTATIONUSE: return "Description:Identifies the different representations of a name .  The representation may affect how the name is used .  (E.g. .  use of Ideographic for formal communications.)";
            case ABC: return "Description:Alphabetic transcription of name in latin alphabet (Japanese: romaji)";
            case IDE: return "Description:Ideographic representation of name (e.g. Japanese kanji, Chinese characters)";
            case SYL: return "Description:Syllabic transcription of name (e.g. Japanese kana, Korean hangul)";
            case OLD: return "Description:This name is no longer in use (note: Names may also carry valid time ranges .  This code is used to cover the situations where it is known that the name is no longer valid, but no particular time range for its use is known)";
            case DN: return "Description:This name should no longer be used when interacting with the person (i.e .  in addition to no longer being used, the name should not be even mentioned when interacting with the person)Note: applications are not required to compare names labeled \"Do Not Use\" and other names in order to eliminate name parts that are common between the other name and a name labeled \"Do Not Use\".";
            case OR: return "Description:The formal name as registered in an official (government) registry, but which name might not be commonly used .  May correspond to the concept of legal name";
            case PHON: return "Description:The name as understood by the data enterer, i.e. a close approximation of a phonetic spelling of the name, not based on a phonetic algorithm.";
            case SRCH: return "Description:A name intended for use in searching or matching.  This is used when the name is incomplete and contains enough details for search matching, but not enough for other uses.";
            case T: return "Description:A temporary name.  Note that a name valid time can provide more detailed information.  This may also be used for temporary names assigned at birth or in emergency situations.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ASSUMED: return "Assumed";
            case A: return "business name";
            case ANON: return "Anonymous";
            case I: return "Indigenous/Tribal";
            case P: return "Other/Pseudonym/Alias";
            case R: return "religious";
            case C: return "customary";
            case M: return "maiden name";
            case NAMEREPRESENTATIONUSE: return "NameRepresentationUse";
            case ABC: return "alphabetic";
            case IDE: return "ideographic";
            case SYL: return "syllabic";
            case OLD: return "no longer in use";
            case DN: return "do not use";
            case OR: return "official registry name";
            case PHON: return "phonetic";
            case SRCH: return "search";
            case T: return "temporary";
            default: return "?";
          }
    }


}

