package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.api.*;

public class Enumerations {

// In here: 
//   AdministrativeGender: The gender of a person used for administrative purposes
//   AgeUnits: A valueSet of UCUM codes for representing age value units
//   BindingStrength: Indication of the degree of conformance expectations associated with a binding
//   ConceptMapEquivalence: The degree of equivalence between concepts
//   ConformanceResourceStatus: The lifecycle status of a Value Set or Concept Map
//   DataAbsentReason: Used to specify why the normally expected content of the data element is missing
//   DataType: The type of an element - one of the FHIR data types
//   DocumentReferenceStatus: The status of the document reference
//   FHIRDefinedType: Either a resource or a data type
//   MessageEvent: One of the message events defined as part of FHIR
//   NoteType: The presentation types of notes
//   RemittanceOutcome: The outcome of the processing.
//   ResourceType: One of the resource types defined as part of FHIR
//   SearchParamType: Data types allowed to be used for search parameters
//   SpecialValues: A set of generally useful codes defined so they can be included in value sets


    public enum AdministrativeGender {
        /**
         * Male
         */
        MALE, 
        /**
         * Female
         */
        FEMALE, 
        /**
         * Other
         */
        OTHER, 
        /**
         * Unknown
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AdministrativeGender fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("male".equals(codeString))
          return MALE;
        if ("female".equals(codeString))
          return FEMALE;
        if ("other".equals(codeString))
          return OTHER;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new Exception("Unknown AdministrativeGender code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MALE: return "male";
            case FEMALE: return "female";
            case OTHER: return "other";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MALE: return "http://hl7.org/fhir/administrative-gender";
            case FEMALE: return "http://hl7.org/fhir/administrative-gender";
            case OTHER: return "http://hl7.org/fhir/administrative-gender";
            case UNKNOWN: return "http://hl7.org/fhir/administrative-gender";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MALE: return "Male";
            case FEMALE: return "Female";
            case OTHER: return "Other";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MALE: return "Male";
            case FEMALE: return "Female";
            case OTHER: return "Other";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class AdministrativeGenderEnumFactory implements EnumFactory<AdministrativeGender> {
    public AdministrativeGender fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("male".equals(codeString))
          return AdministrativeGender.MALE;
        if ("female".equals(codeString))
          return AdministrativeGender.FEMALE;
        if ("other".equals(codeString))
          return AdministrativeGender.OTHER;
        if ("unknown".equals(codeString))
          return AdministrativeGender.UNKNOWN;
        throw new IllegalArgumentException("Unknown AdministrativeGender code '"+codeString+"'");
        }
    public String toCode(AdministrativeGender code) {
      if (code == AdministrativeGender.MALE)
        return "male";
      if (code == AdministrativeGender.FEMALE)
        return "female";
      if (code == AdministrativeGender.OTHER)
        return "other";
      if (code == AdministrativeGender.UNKNOWN)
        return "unknown";
      return "?";
      }
    }

    public enum AgeUnits {
        /**
         * null
         */
        MIN, 
        /**
         * null
         */
        H, 
        /**
         * null
         */
        D, 
        /**
         * null
         */
        WK, 
        /**
         * null
         */
        MO, 
        /**
         * null
         */
        A, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AgeUnits fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("min".equals(codeString))
          return MIN;
        if ("h".equals(codeString))
          return H;
        if ("d".equals(codeString))
          return D;
        if ("wk".equals(codeString))
          return WK;
        if ("mo".equals(codeString))
          return MO;
        if ("a".equals(codeString))
          return A;
        throw new Exception("Unknown AgeUnits code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MIN: return "min";
            case H: return "h";
            case D: return "d";
            case WK: return "wk";
            case MO: return "mo";
            case A: return "a";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MIN: return "http://unitsofmeasure.org";
            case H: return "http://unitsofmeasure.org";
            case D: return "http://unitsofmeasure.org";
            case WK: return "http://unitsofmeasure.org";
            case MO: return "http://unitsofmeasure.org";
            case A: return "http://unitsofmeasure.org";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MIN: return "";
            case H: return "";
            case D: return "";
            case WK: return "";
            case MO: return "";
            case A: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MIN: return "Minute";
            case H: return "Hour";
            case D: return "Day";
            case WK: return "Week";
            case MO: return "Month";
            case A: return "Year";
            default: return "?";
          }
        }
    }

  public static class AgeUnitsEnumFactory implements EnumFactory<AgeUnits> {
    public AgeUnits fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("min".equals(codeString))
          return AgeUnits.MIN;
        if ("h".equals(codeString))
          return AgeUnits.H;
        if ("d".equals(codeString))
          return AgeUnits.D;
        if ("wk".equals(codeString))
          return AgeUnits.WK;
        if ("mo".equals(codeString))
          return AgeUnits.MO;
        if ("a".equals(codeString))
          return AgeUnits.A;
        throw new IllegalArgumentException("Unknown AgeUnits code '"+codeString+"'");
        }
    public String toCode(AgeUnits code) {
      if (code == AgeUnits.MIN)
        return "min";
      if (code == AgeUnits.H)
        return "h";
      if (code == AgeUnits.D)
        return "d";
      if (code == AgeUnits.WK)
        return "wk";
      if (code == AgeUnits.MO)
        return "mo";
      if (code == AgeUnits.A)
        return "a";
      return "?";
      }
    }

    public enum BindingStrength {
        /**
         * To be conformant, instances of this element SHALL include a code from the specified value set
         */
        REQUIRED, 
        /**
         * To be conformant, instances of this element SHALL include a code from the specified value set if any of the codes within the value set can apply to the concept being communicated.  If the valueset does not cover the concept (based on human review), alternate codings (or, data type allowing, text) may be included instead.
         */
        EXTENSIBLE, 
        /**
         * Instances are encouraged to draw from the specified codes for interoperability purposes but are not required to do so to be considered conformant
         */
        PREFERRED, 
        /**
         * Instances are not expected or even encouraged to draw from the specified value set.  The value set merely provides examples of the types of concepts intended to be included
         */
        EXAMPLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BindingStrength fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return REQUIRED;
        if ("extensible".equals(codeString))
          return EXTENSIBLE;
        if ("preferred".equals(codeString))
          return PREFERRED;
        if ("example".equals(codeString))
          return EXAMPLE;
        throw new Exception("Unknown BindingStrength code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUIRED: return "required";
            case EXTENSIBLE: return "extensible";
            case PREFERRED: return "preferred";
            case EXAMPLE: return "example";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUIRED: return "http://hl7.org/fhir/binding-strength";
            case EXTENSIBLE: return "http://hl7.org/fhir/binding-strength";
            case PREFERRED: return "http://hl7.org/fhir/binding-strength";
            case EXAMPLE: return "http://hl7.org/fhir/binding-strength";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUIRED: return "To be conformant, instances of this element SHALL include a code from the specified value set";
            case EXTENSIBLE: return "To be conformant, instances of this element SHALL include a code from the specified value set if any of the codes within the value set can apply to the concept being communicated.  If the valueset does not cover the concept (based on human review), alternate codings (or, data type allowing, text) may be included instead.";
            case PREFERRED: return "Instances are encouraged to draw from the specified codes for interoperability purposes but are not required to do so to be considered conformant";
            case EXAMPLE: return "Instances are not expected or even encouraged to draw from the specified value set.  The value set merely provides examples of the types of concepts intended to be included";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUIRED: return "Required";
            case EXTENSIBLE: return "Extensible";
            case PREFERRED: return "Preferred";
            case EXAMPLE: return "Example";
            default: return "?";
          }
        }
    }

  public static class BindingStrengthEnumFactory implements EnumFactory<BindingStrength> {
    public BindingStrength fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return BindingStrength.REQUIRED;
        if ("extensible".equals(codeString))
          return BindingStrength.EXTENSIBLE;
        if ("preferred".equals(codeString))
          return BindingStrength.PREFERRED;
        if ("example".equals(codeString))
          return BindingStrength.EXAMPLE;
        throw new IllegalArgumentException("Unknown BindingStrength code '"+codeString+"'");
        }
    public String toCode(BindingStrength code) {
      if (code == BindingStrength.REQUIRED)
        return "required";
      if (code == BindingStrength.EXTENSIBLE)
        return "extensible";
      if (code == BindingStrength.PREFERRED)
        return "preferred";
      if (code == BindingStrength.EXAMPLE)
        return "example";
      return "?";
      }
    }

    public enum ConceptMapEquivalence {
        /**
         * The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical)
         */
        EQUIVALENT, 
        /**
         * The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identifical or irrelevant (i.e. intensionally identical)
         */
        EQUAL, 
        /**
         * The target mapping is wider in meaning than the source concept
         */
        WIDER, 
        /**
         * The target mapping subsumes the meaning of the source concept (e.g. the source is-a target)
         */
        SUBSUMES, 
        /**
         * The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally
         */
        NARROWER, 
        /**
         * The target mapping specializes the meaning of the source concept (e.g. the target is-a source)
         */
        SPECIALIZES, 
        /**
         * The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally
         */
        INEXACT, 
        /**
         * There is no match for this concept in the destination concept system
         */
        UNMATCHED, 
        /**
         * This is an explicit assertion that there is no mapping between the source and target concept
         */
        DISJOINT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConceptMapEquivalence fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
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
        throw new Exception("Unknown ConceptMapEquivalence code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
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
          switch (this) {
            case EQUIVALENT: return "http://hl7.org/fhir/concept-map-equivalence";
            case EQUAL: return "http://hl7.org/fhir/concept-map-equivalence";
            case WIDER: return "http://hl7.org/fhir/concept-map-equivalence";
            case SUBSUMES: return "http://hl7.org/fhir/concept-map-equivalence";
            case NARROWER: return "http://hl7.org/fhir/concept-map-equivalence";
            case SPECIALIZES: return "http://hl7.org/fhir/concept-map-equivalence";
            case INEXACT: return "http://hl7.org/fhir/concept-map-equivalence";
            case UNMATCHED: return "http://hl7.org/fhir/concept-map-equivalence";
            case DISJOINT: return "http://hl7.org/fhir/concept-map-equivalence";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EQUIVALENT: return "The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical)";
            case EQUAL: return "The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identifical or irrelevant (i.e. intensionally identical)";
            case WIDER: return "The target mapping is wider in meaning than the source concept";
            case SUBSUMES: return "The target mapping subsumes the meaning of the source concept (e.g. the source is-a target)";
            case NARROWER: return "The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally";
            case SPECIALIZES: return "The target mapping specializes the meaning of the source concept (e.g. the target is-a source)";
            case INEXACT: return "The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally";
            case UNMATCHED: return "There is no match for this concept in the destination concept system";
            case DISJOINT: return "This is an explicit assertion that there is no mapping between the source and target concept";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
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

  public static class ConceptMapEquivalenceEnumFactory implements EnumFactory<ConceptMapEquivalence> {
    public ConceptMapEquivalence fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("equivalent".equals(codeString))
          return ConceptMapEquivalence.EQUIVALENT;
        if ("equal".equals(codeString))
          return ConceptMapEquivalence.EQUAL;
        if ("wider".equals(codeString))
          return ConceptMapEquivalence.WIDER;
        if ("subsumes".equals(codeString))
          return ConceptMapEquivalence.SUBSUMES;
        if ("narrower".equals(codeString))
          return ConceptMapEquivalence.NARROWER;
        if ("specializes".equals(codeString))
          return ConceptMapEquivalence.SPECIALIZES;
        if ("inexact".equals(codeString))
          return ConceptMapEquivalence.INEXACT;
        if ("unmatched".equals(codeString))
          return ConceptMapEquivalence.UNMATCHED;
        if ("disjoint".equals(codeString))
          return ConceptMapEquivalence.DISJOINT;
        throw new IllegalArgumentException("Unknown ConceptMapEquivalence code '"+codeString+"'");
        }
    public String toCode(ConceptMapEquivalence code) {
      if (code == ConceptMapEquivalence.EQUIVALENT)
        return "equivalent";
      if (code == ConceptMapEquivalence.EQUAL)
        return "equal";
      if (code == ConceptMapEquivalence.WIDER)
        return "wider";
      if (code == ConceptMapEquivalence.SUBSUMES)
        return "subsumes";
      if (code == ConceptMapEquivalence.NARROWER)
        return "narrower";
      if (code == ConceptMapEquivalence.SPECIALIZES)
        return "specializes";
      if (code == ConceptMapEquivalence.INEXACT)
        return "inexact";
      if (code == ConceptMapEquivalence.UNMATCHED)
        return "unmatched";
      if (code == ConceptMapEquivalence.DISJOINT)
        return "disjoint";
      return "?";
      }
    }

    public enum ConformanceResourceStatus {
        /**
         * This resource is still under development
         */
        DRAFT, 
        /**
         * This resource is ready for normal use
         */
        ACTIVE, 
        /**
         * This resource has been withdrawn or superseded and should no longer be used
         */
        RETIRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConformanceResourceStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown ConformanceResourceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/conformance-resource-status";
            case ACTIVE: return "http://hl7.org/fhir/conformance-resource-status";
            case RETIRED: return "http://hl7.org/fhir/conformance-resource-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This resource is still under development";
            case ACTIVE: return "This resource is ready for normal use";
            case RETIRED: return "This resource has been withdrawn or superseded and should no longer be used";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case RETIRED: return "Retired";
            default: return "?";
          }
        }
    }

  public static class ConformanceResourceStatusEnumFactory implements EnumFactory<ConformanceResourceStatus> {
    public ConformanceResourceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ConformanceResourceStatus.DRAFT;
        if ("active".equals(codeString))
          return ConformanceResourceStatus.ACTIVE;
        if ("retired".equals(codeString))
          return ConformanceResourceStatus.RETIRED;
        throw new IllegalArgumentException("Unknown ConformanceResourceStatus code '"+codeString+"'");
        }
    public String toCode(ConformanceResourceStatus code) {
      if (code == ConformanceResourceStatus.DRAFT)
        return "draft";
      if (code == ConformanceResourceStatus.ACTIVE)
        return "active";
      if (code == ConformanceResourceStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum DataAbsentReason {
        /**
         * The value is not known
         */
        UNKNOWN, 
        /**
         * The source human does not know the value
         */
        ASKED, 
        /**
         * There is reason to expect (from the workflow) that the value may become known
         */
        TEMP, 
        /**
         * The workflow didn't lead to this value being known
         */
        NOTASKED, 
        /**
         * The information is not available due to security, privacy or related reasons
         */
        MASKED, 
        /**
         * The source system wasn't capable of supporting this element
         */
        UNSUPPORTED, 
        /**
         * The content of the data is represented in the resource narrative
         */
        ASTEXT, 
        /**
         * Some system or workflow process error means that the information is not available
         */
        ERROR, 
        /**
         * NaN, standing for not a number, is a numeric data type value representing an undefined or unrepresentable value
         */
        NAN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataAbsentReason fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("asked".equals(codeString))
          return ASKED;
        if ("temp".equals(codeString))
          return TEMP;
        if ("not-asked".equals(codeString))
          return NOTASKED;
        if ("masked".equals(codeString))
          return MASKED;
        if ("unsupported".equals(codeString))
          return UNSUPPORTED;
        if ("astext".equals(codeString))
          return ASTEXT;
        if ("error".equals(codeString))
          return ERROR;
        if ("NaN".equals(codeString))
          return NAN;
        throw new Exception("Unknown DataAbsentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case ASKED: return "asked";
            case TEMP: return "temp";
            case NOTASKED: return "not-asked";
            case MASKED: return "masked";
            case UNSUPPORTED: return "unsupported";
            case ASTEXT: return "astext";
            case ERROR: return "error";
            case NAN: return "NaN";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNKNOWN: return "http://hl7.org/fhir/data-absent-reason";
            case ASKED: return "http://hl7.org/fhir/data-absent-reason";
            case TEMP: return "http://hl7.org/fhir/data-absent-reason";
            case NOTASKED: return "http://hl7.org/fhir/data-absent-reason";
            case MASKED: return "http://hl7.org/fhir/data-absent-reason";
            case UNSUPPORTED: return "http://hl7.org/fhir/data-absent-reason";
            case ASTEXT: return "http://hl7.org/fhir/data-absent-reason";
            case ERROR: return "http://hl7.org/fhir/data-absent-reason";
            case NAN: return "http://hl7.org/fhir/data-absent-reason";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The value is not known";
            case ASKED: return "The source human does not know the value";
            case TEMP: return "There is reason to expect (from the workflow) that the value may become known";
            case NOTASKED: return "The workflow didn't lead to this value being known";
            case MASKED: return "The information is not available due to security, privacy or related reasons";
            case UNSUPPORTED: return "The source system wasn't capable of supporting this element";
            case ASTEXT: return "The content of the data is represented in the resource narrative";
            case ERROR: return "Some system or workflow process error means that the information is not available";
            case NAN: return "NaN, standing for not a number, is a numeric data type value representing an undefined or unrepresentable value";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case ASKED: return "Asked";
            case TEMP: return "Temp";
            case NOTASKED: return "Not Asked";
            case MASKED: return "Masked";
            case UNSUPPORTED: return "Unsupported";
            case ASTEXT: return "As Text";
            case ERROR: return "Error";
            case NAN: return "Not a Number";
            default: return "?";
          }
        }
    }

  public static class DataAbsentReasonEnumFactory implements EnumFactory<DataAbsentReason> {
    public DataAbsentReason fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return DataAbsentReason.UNKNOWN;
        if ("asked".equals(codeString))
          return DataAbsentReason.ASKED;
        if ("temp".equals(codeString))
          return DataAbsentReason.TEMP;
        if ("not-asked".equals(codeString))
          return DataAbsentReason.NOTASKED;
        if ("masked".equals(codeString))
          return DataAbsentReason.MASKED;
        if ("unsupported".equals(codeString))
          return DataAbsentReason.UNSUPPORTED;
        if ("astext".equals(codeString))
          return DataAbsentReason.ASTEXT;
        if ("error".equals(codeString))
          return DataAbsentReason.ERROR;
        if ("NaN".equals(codeString))
          return DataAbsentReason.NAN;
        throw new IllegalArgumentException("Unknown DataAbsentReason code '"+codeString+"'");
        }
    public String toCode(DataAbsentReason code) {
      if (code == DataAbsentReason.UNKNOWN)
        return "unknown";
      if (code == DataAbsentReason.ASKED)
        return "asked";
      if (code == DataAbsentReason.TEMP)
        return "temp";
      if (code == DataAbsentReason.NOTASKED)
        return "not-asked";
      if (code == DataAbsentReason.MASKED)
        return "masked";
      if (code == DataAbsentReason.UNSUPPORTED)
        return "unsupported";
      if (code == DataAbsentReason.ASTEXT)
        return "astext";
      if (code == DataAbsentReason.ERROR)
        return "error";
      if (code == DataAbsentReason.NAN)
        return "NaN";
      return "?";
      }
    }

    public enum DataType {
        /**
         * added to help the parsers
         */
        NULL;
        public static DataType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        throw new Exception("Unknown DataType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            default: return "?";
          }
        }
    }

  public static class DataTypeEnumFactory implements EnumFactory<DataType> {
    public DataType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        throw new IllegalArgumentException("Unknown DataType code '"+codeString+"'");
        }
    public String toCode(DataType code) {
      return "?";
      }
    }

    public enum DocumentReferenceStatus {
        /**
         * This is the current reference for this document
         */
        CURRENT, 
        /**
         * This reference has been superseded by another reference
         */
        SUPERSEDED, 
        /**
         * This reference was created in error
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DocumentReferenceStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return CURRENT;
        if ("superseded".equals(codeString))
          return SUPERSEDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown DocumentReferenceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CURRENT: return "current";
            case SUPERSEDED: return "superseded";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CURRENT: return "http://hl7.org/fhir/document-reference-status";
            case SUPERSEDED: return "http://hl7.org/fhir/document-reference-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/document-reference-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CURRENT: return "This is the current reference for this document";
            case SUPERSEDED: return "This reference has been superseded by another reference";
            case ENTEREDINERROR: return "This reference was created in error";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CURRENT: return "Current";
            case SUPERSEDED: return "Superseded";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class DocumentReferenceStatusEnumFactory implements EnumFactory<DocumentReferenceStatus> {
    public DocumentReferenceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return DocumentReferenceStatus.CURRENT;
        if ("superseded".equals(codeString))
          return DocumentReferenceStatus.SUPERSEDED;
        if ("entered-in-error".equals(codeString))
          return DocumentReferenceStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown DocumentReferenceStatus code '"+codeString+"'");
        }
    public String toCode(DocumentReferenceStatus code) {
      if (code == DocumentReferenceStatus.CURRENT)
        return "current";
      if (code == DocumentReferenceStatus.SUPERSEDED)
        return "superseded";
      if (code == DocumentReferenceStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    public enum FHIRDefinedType {
        /**
         * added to help the parsers
         */
        NULL;
        public static FHIRDefinedType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        throw new Exception("Unknown FHIRDefinedType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            default: return "?";
          }
        }
    }

  public static class FHIRDefinedTypeEnumFactory implements EnumFactory<FHIRDefinedType> {
    public FHIRDefinedType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        throw new IllegalArgumentException("Unknown FHIRDefinedType code '"+codeString+"'");
        }
    public String toCode(FHIRDefinedType code) {
      return "?";
      }
    }

    public enum MessageEvent {
        /**
         * added to help the parsers
         */
        NULL;
        public static MessageEvent fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        throw new Exception("Unknown MessageEvent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            default: return "?";
          }
        }
    }

  public static class MessageEventEnumFactory implements EnumFactory<MessageEvent> {
    public MessageEvent fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        throw new IllegalArgumentException("Unknown MessageEvent code '"+codeString+"'");
        }
    public String toCode(MessageEvent code) {
      return "?";
      }
    }

    public enum NoteType {
        /**
         * Display the note.
         */
        DISPLAY, 
        /**
         * Print the note on the form.
         */
        PRINT, 
        /**
         * Print the note for the operator.
         */
        PRINTOPER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NoteType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("display".equals(codeString))
          return DISPLAY;
        if ("print".equals(codeString))
          return PRINT;
        if ("printoper".equals(codeString))
          return PRINTOPER;
        throw new Exception("Unknown NoteType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DISPLAY: return "display";
            case PRINT: return "print";
            case PRINTOPER: return "printoper";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DISPLAY: return "http://hl7.org/fhir/note-type";
            case PRINT: return "http://hl7.org/fhir/note-type";
            case PRINTOPER: return "http://hl7.org/fhir/note-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DISPLAY: return "Display the note.";
            case PRINT: return "Print the note on the form.";
            case PRINTOPER: return "Print the note for the operator.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DISPLAY: return "Display";
            case PRINT: return "Print (Form)";
            case PRINTOPER: return "Print (Operator)";
            default: return "?";
          }
        }
    }

  public static class NoteTypeEnumFactory implements EnumFactory<NoteType> {
    public NoteType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("display".equals(codeString))
          return NoteType.DISPLAY;
        if ("print".equals(codeString))
          return NoteType.PRINT;
        if ("printoper".equals(codeString))
          return NoteType.PRINTOPER;
        throw new IllegalArgumentException("Unknown NoteType code '"+codeString+"'");
        }
    public String toCode(NoteType code) {
      if (code == NoteType.DISPLAY)
        return "display";
      if (code == NoteType.PRINT)
        return "print";
      if (code == NoteType.PRINTOPER)
        return "printoper";
      return "?";
      }
    }

    public enum RemittanceOutcome {
        /**
         * The processing completed without errors.
         */
        COMPLETE, 
        /**
         * The processing identified with errors.
         */
        ERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RemittanceOutcome fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("error".equals(codeString))
          return ERROR;
        throw new Exception("Unknown RemittanceOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETE: return "http://hl7.org/fhir/remittance-outcome";
            case ERROR: return "http://hl7.org/fhir/remittance-outcome";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "The processing completed without errors.";
            case ERROR: return "The processing identified with errors.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "Complete";
            case ERROR: return "Error";
            default: return "?";
          }
        }
    }

  public static class RemittanceOutcomeEnumFactory implements EnumFactory<RemittanceOutcome> {
    public RemittanceOutcome fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return RemittanceOutcome.COMPLETE;
        if ("error".equals(codeString))
          return RemittanceOutcome.ERROR;
        throw new IllegalArgumentException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
    public String toCode(RemittanceOutcome code) {
      if (code == RemittanceOutcome.COMPLETE)
        return "complete";
      if (code == RemittanceOutcome.ERROR)
        return "error";
      return "?";
      }
    }

    public enum ResourceType {
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        throw new Exception("Unknown ResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            default: return "?";
          }
        }
    }

  public static class ResourceTypeEnumFactory implements EnumFactory<ResourceType> {
    public ResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        throw new IllegalArgumentException("Unknown ResourceType code '"+codeString+"'");
        }
    public String toCode(ResourceType code) {
      return "?";
      }
    }

    public enum SearchParamType {
        /**
         * Search parameter SHALL be a number (a whole number, or a decimal)
         */
        NUMBER, 
        /**
         * Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported
         */
        DATE, 
        /**
         * Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces
         */
        STRING, 
        /**
         * Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "|", depending on the modifier used
         */
        TOKEN, 
        /**
         * A reference to another resource
         */
        REFERENCE, 
        /**
         * A composite search parameter that combines a search on two values together
         */
        COMPOSITE, 
        /**
         * A search parameter that searches on a quantity
         */
        QUANTITY, 
        /**
         * A search parameter that searches on a URI (RFC 3986)
         */
        URI, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchParamType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("number".equals(codeString))
          return NUMBER;
        if ("date".equals(codeString))
          return DATE;
        if ("string".equals(codeString))
          return STRING;
        if ("token".equals(codeString))
          return TOKEN;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("composite".equals(codeString))
          return COMPOSITE;
        if ("quantity".equals(codeString))
          return QUANTITY;
        if ("uri".equals(codeString))
          return URI;
        throw new Exception("Unknown SearchParamType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NUMBER: return "number";
            case DATE: return "date";
            case STRING: return "string";
            case TOKEN: return "token";
            case REFERENCE: return "reference";
            case COMPOSITE: return "composite";
            case QUANTITY: return "quantity";
            case URI: return "uri";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NUMBER: return "http://hl7.org/fhir/search-param-type";
            case DATE: return "http://hl7.org/fhir/search-param-type";
            case STRING: return "http://hl7.org/fhir/search-param-type";
            case TOKEN: return "http://hl7.org/fhir/search-param-type";
            case REFERENCE: return "http://hl7.org/fhir/search-param-type";
            case COMPOSITE: return "http://hl7.org/fhir/search-param-type";
            case QUANTITY: return "http://hl7.org/fhir/search-param-type";
            case URI: return "http://hl7.org/fhir/search-param-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NUMBER: return "Search parameter SHALL be a number (a whole number, or a decimal)";
            case DATE: return "Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported";
            case STRING: return "Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces";
            case TOKEN: return "Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a \"|\", depending on the modifier used";
            case REFERENCE: return "A reference to another resource";
            case COMPOSITE: return "A composite search parameter that combines a search on two values together";
            case QUANTITY: return "A search parameter that searches on a quantity";
            case URI: return "A search parameter that searches on a URI (RFC 3986)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NUMBER: return "Number";
            case DATE: return "Date/DateTime";
            case STRING: return "String";
            case TOKEN: return "Token";
            case REFERENCE: return "Reference";
            case COMPOSITE: return "Composite";
            case QUANTITY: return "Quantity";
            case URI: return "URI";
            default: return "?";
          }
        }
    }

  public static class SearchParamTypeEnumFactory implements EnumFactory<SearchParamType> {
    public SearchParamType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("number".equals(codeString))
          return SearchParamType.NUMBER;
        if ("date".equals(codeString))
          return SearchParamType.DATE;
        if ("string".equals(codeString))
          return SearchParamType.STRING;
        if ("token".equals(codeString))
          return SearchParamType.TOKEN;
        if ("reference".equals(codeString))
          return SearchParamType.REFERENCE;
        if ("composite".equals(codeString))
          return SearchParamType.COMPOSITE;
        if ("quantity".equals(codeString))
          return SearchParamType.QUANTITY;
        if ("uri".equals(codeString))
          return SearchParamType.URI;
        throw new IllegalArgumentException("Unknown SearchParamType code '"+codeString+"'");
        }
    public String toCode(SearchParamType code) {
      if (code == SearchParamType.NUMBER)
        return "number";
      if (code == SearchParamType.DATE)
        return "date";
      if (code == SearchParamType.STRING)
        return "string";
      if (code == SearchParamType.TOKEN)
        return "token";
      if (code == SearchParamType.REFERENCE)
        return "reference";
      if (code == SearchParamType.COMPOSITE)
        return "composite";
      if (code == SearchParamType.QUANTITY)
        return "quantity";
      if (code == SearchParamType.URI)
        return "uri";
      return "?";
      }
    }

    public enum SpecialValues {
        /**
         * Boolean true
         */
        TRUE, 
        /**
         * Boolean false
         */
        FALSE, 
        /**
         * The content is greater than zero, but too small to be quantified
         */
        TRACE, 
        /**
         * The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material
         */
        SUFFICIENT, 
        /**
         * The value is no longer available
         */
        WITHDRAWN, 
        /**
         * The are no known applicable values in this context
         */
        NILKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SpecialValues fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("true".equals(codeString))
          return TRUE;
        if ("false".equals(codeString))
          return FALSE;
        if ("trace".equals(codeString))
          return TRACE;
        if ("sufficient".equals(codeString))
          return SUFFICIENT;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if ("nil-known".equals(codeString))
          return NILKNOWN;
        throw new Exception("Unknown SpecialValues code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TRUE: return "true";
            case FALSE: return "false";
            case TRACE: return "trace";
            case SUFFICIENT: return "sufficient";
            case WITHDRAWN: return "withdrawn";
            case NILKNOWN: return "nil-known";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case TRUE: return "http://hl7.org/fhir/special-values";
            case FALSE: return "http://hl7.org/fhir/special-values";
            case TRACE: return "http://hl7.org/fhir/special-values";
            case SUFFICIENT: return "http://hl7.org/fhir/special-values";
            case WITHDRAWN: return "http://hl7.org/fhir/special-values";
            case NILKNOWN: return "http://hl7.org/fhir/special-values";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case TRUE: return "Boolean true";
            case FALSE: return "Boolean false";
            case TRACE: return "The content is greater than zero, but too small to be quantified";
            case SUFFICIENT: return "The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material";
            case WITHDRAWN: return "The value is no longer available";
            case NILKNOWN: return "The are no known applicable values in this context";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TRUE: return "true";
            case FALSE: return "false";
            case TRACE: return "Trace Amount Detected";
            case SUFFICIENT: return "Sufficient Quantity";
            case WITHDRAWN: return "Value Withdrawn";
            case NILKNOWN: return "Nil Known";
            default: return "?";
          }
        }
    }

  public static class SpecialValuesEnumFactory implements EnumFactory<SpecialValues> {
    public SpecialValues fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("true".equals(codeString))
          return SpecialValues.TRUE;
        if ("false".equals(codeString))
          return SpecialValues.FALSE;
        if ("trace".equals(codeString))
          return SpecialValues.TRACE;
        if ("sufficient".equals(codeString))
          return SpecialValues.SUFFICIENT;
        if ("withdrawn".equals(codeString))
          return SpecialValues.WITHDRAWN;
        if ("nil-known".equals(codeString))
          return SpecialValues.NILKNOWN;
        throw new IllegalArgumentException("Unknown SpecialValues code '"+codeString+"'");
        }
    public String toCode(SpecialValues code) {
      if (code == SpecialValues.TRUE)
        return "true";
      if (code == SpecialValues.FALSE)
        return "false";
      if (code == SpecialValues.TRACE)
        return "trace";
      if (code == SpecialValues.SUFFICIENT)
        return "sufficient";
      if (code == SpecialValues.WITHDRAWN)
        return "withdrawn";
      if (code == SpecialValues.NILKNOWN)
        return "nil-known";
      return "?";
      }
    }


}

