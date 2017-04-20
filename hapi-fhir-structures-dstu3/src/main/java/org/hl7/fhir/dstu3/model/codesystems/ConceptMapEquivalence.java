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

public enum ConceptMapEquivalence {

        /**
         * The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is not known
         */
        RELATEDTO, 
        /**
         * The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).
         */
        EQUIVALENT, 
        /**
         * The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identical or irrelevant (i.e. intentionally identical).
         */
        EQUAL, 
        /**
         * The target mapping is wider in meaning than the source concept.
         */
        WIDER, 
        /**
         * The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).
         */
        SUBSUMES, 
        /**
         * The target mapping is narrower in meaning than the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.
         */
        NARROWER, 
        /**
         * The target mapping specializes the meaning of the source concept (e.g. the target is-a source).
         */
        SPECIALIZES, 
        /**
         * The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.
         */
        INEXACT, 
        /**
         * There is no match for this concept in the destination concept system.
         */
        UNMATCHED, 
        /**
         * This is an explicit assertion that there is no mapping between the source and target concept.
         */
        DISJOINT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConceptMapEquivalence fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("relatedto".equals(codeString))
          return RELATEDTO;
        if ("equivalent".equals(codeString))
          return EQUIVALENT;
        if ("equal".equals(codeString))
          return EQUAL;
        if ("wider".equals(codeString))
          return WIDER;
        if ("subsumes".equals(codeString))
          return SUBSUMES;
        if ("narrower".equals(codeString))
          return NARROWER;
        if ("specializes".equals(codeString))
          return SPECIALIZES;
        if ("inexact".equals(codeString))
          return INEXACT;
        if ("unmatched".equals(codeString))
          return UNMATCHED;
        if ("disjoint".equals(codeString))
          return DISJOINT;
        throw new FHIRException("Unknown ConceptMapEquivalence code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RELATEDTO: return "relatedto";
            case EQUIVALENT: return "equivalent";
            case EQUAL: return "equal";
            case WIDER: return "wider";
            case SUBSUMES: return "subsumes";
            case NARROWER: return "narrower";
            case SPECIALIZES: return "specializes";
            case INEXACT: return "inexact";
            case UNMATCHED: return "unmatched";
            case DISJOINT: return "disjoint";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/concept-map-equivalence";
        }
        public String getDefinition() {
          switch (this) {
            case RELATEDTO: return "The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is not known";
            case EQUIVALENT: return "The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).";
            case EQUAL: return "The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identical or irrelevant (i.e. intentionally identical).";
            case WIDER: return "The target mapping is wider in meaning than the source concept.";
            case SUBSUMES: return "The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).";
            case NARROWER: return "The target mapping is narrower in meaning than the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.";
            case SPECIALIZES: return "The target mapping specializes the meaning of the source concept (e.g. the target is-a source).";
            case INEXACT: return "The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.";
            case UNMATCHED: return "There is no match for this concept in the destination concept system.";
            case DISJOINT: return "This is an explicit assertion that there is no mapping between the source and target concept.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RELATEDTO: return "Related To";
            case EQUIVALENT: return "Equivalent";
            case EQUAL: return "Equal";
            case WIDER: return "Wider";
            case SUBSUMES: return "Subsumes";
            case NARROWER: return "Narrower";
            case SPECIALIZES: return "Specializes";
            case INEXACT: return "Inexact";
            case UNMATCHED: return "Unmatched";
            case DISJOINT: return "Disjoint";
            default: return "?";
          }
    }


}

