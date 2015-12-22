package org.hl7.fhir.instance.model;

public class Enumerations {

// In here: 
//   AdministrativeGender: The gender of a person used for administrative purposes.
//   AgeUnits: A valueSet of UCUM codes for representing age value units.
//   BindingStrength: Indication of the degree of conformance expectations associated with a binding.
//   ConceptMapEquivalence: The degree of equivalence between concepts.
//   ConformanceResourceStatus: The lifecycle status of a Value Set or Concept Map.
//   DataAbsentReason: Used to specify why the normally expected content of the data element is missing.
//   DataType: The type of an element - one of the FHIR data types.
//   DocumentReferenceStatus: The status of the document reference.
//   FHIRDefinedType: Either a resource or a data type.
//   MessageEvent: One of the message events defined as part of FHIR.
//   NoteType: The presentation types of notes.
//   RemittanceOutcome: The outcome of the processing.
//   ResourceType: One of the resource types defined as part of FHIR.
//   SearchParamType: Data types allowed to be used for search parameters.
//   SpecialValues: A set of generally useful codes defined so they can be included in value sets.


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
         * To be conformant, instances of this element SHALL include a code from the specified value set.
         */
        REQUIRED, 
        /**
         * To be conformant, instances of this element SHALL include a code from the specified value set if any of the codes within the value set can apply to the concept being communicated.  If the valueset does not cover the concept (based on human review), alternate codings (or, data type allowing, text) may be included instead.
         */
        EXTENSIBLE, 
        /**
         * Instances are encouraged to draw from the specified codes for interoperability purposes but are not required to do so to be considered conformant.
         */
        PREFERRED, 
        /**
         * Instances are not expected or even encouraged to draw from the specified value set.  The value set merely provides examples of the types of concepts intended to be included.
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
            case REQUIRED: return "To be conformant, instances of this element SHALL include a code from the specified value set.";
            case EXTENSIBLE: return "To be conformant, instances of this element SHALL include a code from the specified value set if any of the codes within the value set can apply to the concept being communicated.  If the valueset does not cover the concept (based on human review), alternate codings (or, data type allowing, text) may be included instead.";
            case PREFERRED: return "Instances are encouraged to draw from the specified codes for interoperability purposes but are not required to do so to be considered conformant.";
            case EXAMPLE: return "Instances are not expected or even encouraged to draw from the specified value set.  The value set merely provides examples of the types of concepts intended to be included.";
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
         * The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.
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
            case EQUIVALENT: return "The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).";
            case EQUAL: return "The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identical or irrelevant (i.e. intentionally identical).";
            case WIDER: return "The target mapping is wider in meaning than the source concept.";
            case SUBSUMES: return "The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).";
            case NARROWER: return "The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.";
            case SPECIALIZES: return "The target mapping specializes the meaning of the source concept (e.g. the target is-a source).";
            case INEXACT: return "The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.";
            case UNMATCHED: return "There is no match for this concept in the destination concept system.";
            case DISJOINT: return "This is an explicit assertion that there is no mapping between the source and target concept.";
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
         * This resource is still under development.
         */
        DRAFT, 
        /**
         * This resource is ready for normal use.
         */
        ACTIVE, 
        /**
         * This resource has been withdrawn or superseded and should no longer be used.
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
            case DRAFT: return "This resource is still under development.";
            case ACTIVE: return "This resource is ready for normal use.";
            case RETIRED: return "This resource has been withdrawn or superseded and should no longer be used.";
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
         * The value is not known.
         */
        UNKNOWN, 
        /**
         * The source human does not know the value.
         */
        ASKED, 
        /**
         * There is reason to expect (from the workflow) that the value may become known.
         */
        TEMP, 
        /**
         * The workflow didn't lead to this value being known.
         */
        NOTASKED, 
        /**
         * The information is not available due to security, privacy or related reasons.
         */
        MASKED, 
        /**
         * The source system wasn't capable of supporting this element.
         */
        UNSUPPORTED, 
        /**
         * The content of the data is represented in the resource narrative.
         */
        ASTEXT, 
        /**
         * Some system or workflow process error means that the information is not available.
         */
        ERROR, 
        /**
         * NaN, standing for not a number, is a numeric data type value representing an undefined or unrepresentable value.
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
            case UNKNOWN: return "The value is not known.";
            case ASKED: return "The source human does not know the value.";
            case TEMP: return "There is reason to expect (from the workflow) that the value may become known.";
            case NOTASKED: return "The workflow didn't lead to this value being known.";
            case MASKED: return "The information is not available due to security, privacy or related reasons.";
            case UNSUPPORTED: return "The source system wasn't capable of supporting this element.";
            case ASTEXT: return "The content of the data is represented in the resource narrative.";
            case ERROR: return "Some system or workflow process error means that the information is not available.";
            case NAN: return "NaN, standing for not a number, is a numeric data type value representing an undefined or unrepresentable value.";
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
         * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.
         */
        ADDRESS, 
        /**
         * null
         */
        AGE, 
        /**
         * A  text note which also  contains information about who made the statement and when.
         */
        ANNOTATION, 
        /**
         * For referring to data content defined in other formats.
         */
        ATTACHMENT, 
        /**
         * Base definition for all elements that are defined inside a resource - but not those in a data type.
         */
        BACKBONEELEMENT, 
        /**
         * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
         */
        CODEABLECONCEPT, 
        /**
         * A reference to a code defined by a terminology system.
         */
        CODING, 
        /**
         * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
         */
        CONTACTPOINT, 
        /**
         * null
         */
        COUNT, 
        /**
         * null
         */
        DISTANCE, 
        /**
         * null
         */
        DURATION, 
        /**
         * Base definition for all elements in a resource.
         */
        ELEMENT, 
        /**
         * Captures constraints on each element within the resource, profile, or extension.
         */
        ELEMENTDEFINITION, 
        /**
         * Optional Extensions Element - found in all resources.
         */
        EXTENSION, 
        /**
         * A human's name with the ability to identify parts and usage.
         */
        HUMANNAME, 
        /**
         * A technical identifier - identifies some entity uniquely and unambiguously.
         */
        IDENTIFIER, 
        /**
         * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.
         */
        META, 
        /**
         * null
         */
        MONEY, 
        /**
         * A human-readable formatted text, including images.
         */
        NARRATIVE, 
        /**
         * A time period defined by a start and end date and optionally time.
         */
        PERIOD, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        QUANTITY, 
        /**
         * A set of ordered Quantities defined by a low and high limit.
         */
        RANGE, 
        /**
         * A relationship of two Quantity values - expressed as a numerator and a denominator.
         */
        RATIO, 
        /**
         * A reference from one resource to another.
         */
        REFERENCE, 
        /**
         * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
         */
        SAMPLEDDATA, 
        /**
         * A digital signature along with supporting context. The signature may be electronic/cryptographic in nature, or a graphical image representing a hand-written signature, or a signature process. Different Signature approaches have different utilities.
         */
        SIGNATURE, 
        /**
         * null
         */
        SIMPLEQUANTITY, 
        /**
         * Specifies an event that may occur multiple times. Timing schedules are used to record when things are expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds.
         */
        TIMING, 
        /**
         * A stream of bytes
         */
        BASE64BINARY, 
        /**
         * Value of "true" or "false"
         */
        BOOLEAN, 
        /**
         * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents
         */
        CODE, 
        /**
         * A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.
         */
        DATE, 
        /**
         * A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.
         */
        DATETIME, 
        /**
         * A rational number with implicit precision
         */
        DECIMAL, 
        /**
         * Any combination of letters, numerals, "-" and ".", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.
         */
        ID, 
        /**
         * An instant in time - known at least to the second
         */
        INSTANT, 
        /**
         * A whole number
         */
        INTEGER, 
        /**
         * A string that may contain markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN, 
        /**
         * An oid represented as a URI
         */
        OID, 
        /**
         * An integer with a value that is positive (e.g. >0)
         */
        POSITIVEINT, 
        /**
         * A sequence of Unicode characters
         */
        STRING, 
        /**
         * A time during the day, with no date specified
         */
        TIME, 
        /**
         * An integer with a value that is not negative (e.g. >= 0)
         */
        UNSIGNEDINT, 
        /**
         * String of characters used to identify a name or a resource
         */
        URI, 
        /**
         * A UUID, represented as a URI
         */
        UUID, 
        /**
         * XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
         */
        XHTML, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Address".equals(codeString))
          return ADDRESS;
        if ("Age".equals(codeString))
          return AGE;
        if ("Annotation".equals(codeString))
          return ANNOTATION;
        if ("Attachment".equals(codeString))
          return ATTACHMENT;
        if ("BackboneElement".equals(codeString))
          return BACKBONEELEMENT;
        if ("CodeableConcept".equals(codeString))
          return CODEABLECONCEPT;
        if ("Coding".equals(codeString))
          return CODING;
        if ("ContactPoint".equals(codeString))
          return CONTACTPOINT;
        if ("Count".equals(codeString))
          return COUNT;
        if ("Distance".equals(codeString))
          return DISTANCE;
        if ("Duration".equals(codeString))
          return DURATION;
        if ("Element".equals(codeString))
          return ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return ELEMENTDEFINITION;
        if ("Extension".equals(codeString))
          return EXTENSION;
        if ("HumanName".equals(codeString))
          return HUMANNAME;
        if ("Identifier".equals(codeString))
          return IDENTIFIER;
        if ("Meta".equals(codeString))
          return META;
        if ("Money".equals(codeString))
          return MONEY;
        if ("Narrative".equals(codeString))
          return NARRATIVE;
        if ("Period".equals(codeString))
          return PERIOD;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("Reference".equals(codeString))
          return REFERENCE;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return SIMPLEQUANTITY;
        if ("Timing".equals(codeString))
          return TIMING;
        if ("base64Binary".equals(codeString))
          return BASE64BINARY;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("code".equals(codeString))
          return CODE;
        if ("date".equals(codeString))
          return DATE;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("decimal".equals(codeString))
          return DECIMAL;
        if ("id".equals(codeString))
          return ID;
        if ("instant".equals(codeString))
          return INSTANT;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("markdown".equals(codeString))
          return MARKDOWN;
        if ("oid".equals(codeString))
          return OID;
        if ("positiveInt".equals(codeString))
          return POSITIVEINT;
        if ("string".equals(codeString))
          return STRING;
        if ("time".equals(codeString))
          return TIME;
        if ("unsignedInt".equals(codeString))
          return UNSIGNEDINT;
        if ("uri".equals(codeString))
          return URI;
        if ("uuid".equals(codeString))
          return UUID;
        if ("xhtml".equals(codeString))
          return XHTML;
        throw new Exception("Unknown DataType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADDRESS: return "Address";
            case AGE: return "Age";
            case ANNOTATION: return "Annotation";
            case ATTACHMENT: return "Attachment";
            case BACKBONEELEMENT: return "BackboneElement";
            case CODEABLECONCEPT: return "CodeableConcept";
            case CODING: return "Coding";
            case CONTACTPOINT: return "ContactPoint";
            case COUNT: return "Count";
            case DISTANCE: return "Distance";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case META: return "Meta";
            case MONEY: return "Money";
            case NARRATIVE: return "Narrative";
            case PERIOD: return "Period";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case TIMING: return "Timing";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CODE: return "code";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            case ID: return "id";
            case INSTANT: return "instant";
            case INTEGER: return "integer";
            case MARKDOWN: return "markdown";
            case OID: return "oid";
            case POSITIVEINT: return "positiveInt";
            case STRING: return "string";
            case TIME: return "time";
            case UNSIGNEDINT: return "unsignedInt";
            case URI: return "uri";
            case UUID: return "uuid";
            case XHTML: return "xhtml";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ADDRESS: return "http://hl7.org/fhir/data-types";
            case AGE: return "http://hl7.org/fhir/data-types";
            case ANNOTATION: return "http://hl7.org/fhir/data-types";
            case ATTACHMENT: return "http://hl7.org/fhir/data-types";
            case BACKBONEELEMENT: return "http://hl7.org/fhir/data-types";
            case CODEABLECONCEPT: return "http://hl7.org/fhir/data-types";
            case CODING: return "http://hl7.org/fhir/data-types";
            case CONTACTPOINT: return "http://hl7.org/fhir/data-types";
            case COUNT: return "http://hl7.org/fhir/data-types";
            case DISTANCE: return "http://hl7.org/fhir/data-types";
            case DURATION: return "http://hl7.org/fhir/data-types";
            case ELEMENT: return "http://hl7.org/fhir/data-types";
            case ELEMENTDEFINITION: return "http://hl7.org/fhir/data-types";
            case EXTENSION: return "http://hl7.org/fhir/data-types";
            case HUMANNAME: return "http://hl7.org/fhir/data-types";
            case IDENTIFIER: return "http://hl7.org/fhir/data-types";
            case META: return "http://hl7.org/fhir/data-types";
            case MONEY: return "http://hl7.org/fhir/data-types";
            case NARRATIVE: return "http://hl7.org/fhir/data-types";
            case PERIOD: return "http://hl7.org/fhir/data-types";
            case QUANTITY: return "http://hl7.org/fhir/data-types";
            case RANGE: return "http://hl7.org/fhir/data-types";
            case RATIO: return "http://hl7.org/fhir/data-types";
            case REFERENCE: return "http://hl7.org/fhir/data-types";
            case SAMPLEDDATA: return "http://hl7.org/fhir/data-types";
            case SIGNATURE: return "http://hl7.org/fhir/data-types";
            case SIMPLEQUANTITY: return "http://hl7.org/fhir/data-types";
            case TIMING: return "http://hl7.org/fhir/data-types";
            case BASE64BINARY: return "http://hl7.org/fhir/data-types";
            case BOOLEAN: return "http://hl7.org/fhir/data-types";
            case CODE: return "http://hl7.org/fhir/data-types";
            case DATE: return "http://hl7.org/fhir/data-types";
            case DATETIME: return "http://hl7.org/fhir/data-types";
            case DECIMAL: return "http://hl7.org/fhir/data-types";
            case ID: return "http://hl7.org/fhir/data-types";
            case INSTANT: return "http://hl7.org/fhir/data-types";
            case INTEGER: return "http://hl7.org/fhir/data-types";
            case MARKDOWN: return "http://hl7.org/fhir/data-types";
            case OID: return "http://hl7.org/fhir/data-types";
            case POSITIVEINT: return "http://hl7.org/fhir/data-types";
            case STRING: return "http://hl7.org/fhir/data-types";
            case TIME: return "http://hl7.org/fhir/data-types";
            case UNSIGNEDINT: return "http://hl7.org/fhir/data-types";
            case URI: return "http://hl7.org/fhir/data-types";
            case UUID: return "http://hl7.org/fhir/data-types";
            case XHTML: return "http://hl7.org/fhir/data-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ADDRESS: return "There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.";
            case AGE: return "";
            case ANNOTATION: return "A  text note which also  contains information about who made the statement and when.";
            case ATTACHMENT: return "For referring to data content defined in other formats.";
            case BACKBONEELEMENT: return "Base definition for all elements that are defined inside a resource - but not those in a data type.";
            case CODEABLECONCEPT: return "A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.";
            case CODING: return "A reference to a code defined by a terminology system.";
            case CONTACTPOINT: return "Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.";
            case COUNT: return "";
            case DISTANCE: return "";
            case DURATION: return "";
            case ELEMENT: return "Base definition for all elements in a resource.";
            case ELEMENTDEFINITION: return "Captures constraints on each element within the resource, profile, or extension.";
            case EXTENSION: return "Optional Extensions Element - found in all resources.";
            case HUMANNAME: return "A human's name with the ability to identify parts and usage.";
            case IDENTIFIER: return "A technical identifier - identifies some entity uniquely and unambiguously.";
            case META: return "The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.";
            case MONEY: return "";
            case NARRATIVE: return "A human-readable formatted text, including images.";
            case PERIOD: return "A time period defined by a start and end date and optionally time.";
            case QUANTITY: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case RANGE: return "A set of ordered Quantities defined by a low and high limit.";
            case RATIO: return "A relationship of two Quantity values - expressed as a numerator and a denominator.";
            case REFERENCE: return "A reference from one resource to another.";
            case SAMPLEDDATA: return "A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.";
            case SIGNATURE: return "A digital signature along with supporting context. The signature may be electronic/cryptographic in nature, or a graphical image representing a hand-written signature, or a signature process. Different Signature approaches have different utilities.";
            case SIMPLEQUANTITY: return "";
            case TIMING: return "Specifies an event that may occur multiple times. Timing schedules are used to record when things are expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds.";
            case BASE64BINARY: return "A stream of bytes";
            case BOOLEAN: return "Value of \"true\" or \"false\"";
            case CODE: return "A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents";
            case DATE: return "A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.";
            case DECIMAL: return "A rational number with implicit precision";
            case ID: return "Any combination of letters, numerals, \"-\" and \".\", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.";
            case INSTANT: return "An instant in time - known at least to the second";
            case INTEGER: return "A whole number";
            case MARKDOWN: return "A string that may contain markdown syntax for optional processing by a mark down presentation engine";
            case OID: return "An oid represented as a URI";
            case POSITIVEINT: return "An integer with a value that is positive (e.g. >0)";
            case STRING: return "A sequence of Unicode characters";
            case TIME: return "A time during the day, with no date specified";
            case UNSIGNEDINT: return "An integer with a value that is not negative (e.g. >= 0)";
            case URI: return "String of characters used to identify a name or a resource";
            case UUID: return "A UUID, represented as a URI";
            case XHTML: return "XHTML format, as defined by W3C, but restricted usage (mainly, no active content)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADDRESS: return "Address";
            case AGE: return "Age";
            case ANNOTATION: return "Annotation";
            case ATTACHMENT: return "Attachment";
            case BACKBONEELEMENT: return "BackboneElement";
            case CODEABLECONCEPT: return "CodeableConcept";
            case CODING: return "Coding";
            case CONTACTPOINT: return "ContactPoint";
            case COUNT: return "Count";
            case DISTANCE: return "Distance";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case META: return "Meta";
            case MONEY: return "Money";
            case NARRATIVE: return "Narrative";
            case PERIOD: return "Period";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case TIMING: return "Timing";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CODE: return "code";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            case ID: return "id";
            case INSTANT: return "instant";
            case INTEGER: return "integer";
            case MARKDOWN: return "markdown";
            case OID: return "oid";
            case POSITIVEINT: return "positiveInt";
            case STRING: return "string";
            case TIME: return "time";
            case UNSIGNEDINT: return "unsignedInt";
            case URI: return "uri";
            case UUID: return "uuid";
            case XHTML: return "XHTML";
            default: return "?";
          }
        }
    }

  public static class DataTypeEnumFactory implements EnumFactory<DataType> {
    public DataType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Address".equals(codeString))
          return DataType.ADDRESS;
        if ("Age".equals(codeString))
          return DataType.AGE;
        if ("Annotation".equals(codeString))
          return DataType.ANNOTATION;
        if ("Attachment".equals(codeString))
          return DataType.ATTACHMENT;
        if ("BackboneElement".equals(codeString))
          return DataType.BACKBONEELEMENT;
        if ("CodeableConcept".equals(codeString))
          return DataType.CODEABLECONCEPT;
        if ("Coding".equals(codeString))
          return DataType.CODING;
        if ("ContactPoint".equals(codeString))
          return DataType.CONTACTPOINT;
        if ("Count".equals(codeString))
          return DataType.COUNT;
        if ("Distance".equals(codeString))
          return DataType.DISTANCE;
        if ("Duration".equals(codeString))
          return DataType.DURATION;
        if ("Element".equals(codeString))
          return DataType.ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return DataType.ELEMENTDEFINITION;
        if ("Extension".equals(codeString))
          return DataType.EXTENSION;
        if ("HumanName".equals(codeString))
          return DataType.HUMANNAME;
        if ("Identifier".equals(codeString))
          return DataType.IDENTIFIER;
        if ("Meta".equals(codeString))
          return DataType.META;
        if ("Money".equals(codeString))
          return DataType.MONEY;
        if ("Narrative".equals(codeString))
          return DataType.NARRATIVE;
        if ("Period".equals(codeString))
          return DataType.PERIOD;
        if ("Quantity".equals(codeString))
          return DataType.QUANTITY;
        if ("Range".equals(codeString))
          return DataType.RANGE;
        if ("Ratio".equals(codeString))
          return DataType.RATIO;
        if ("Reference".equals(codeString))
          return DataType.REFERENCE;
        if ("SampledData".equals(codeString))
          return DataType.SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return DataType.SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return DataType.SIMPLEQUANTITY;
        if ("Timing".equals(codeString))
          return DataType.TIMING;
        if ("base64Binary".equals(codeString))
          return DataType.BASE64BINARY;
        if ("boolean".equals(codeString))
          return DataType.BOOLEAN;
        if ("code".equals(codeString))
          return DataType.CODE;
        if ("date".equals(codeString))
          return DataType.DATE;
        if ("dateTime".equals(codeString))
          return DataType.DATETIME;
        if ("decimal".equals(codeString))
          return DataType.DECIMAL;
        if ("id".equals(codeString))
          return DataType.ID;
        if ("instant".equals(codeString))
          return DataType.INSTANT;
        if ("integer".equals(codeString))
          return DataType.INTEGER;
        if ("markdown".equals(codeString))
          return DataType.MARKDOWN;
        if ("oid".equals(codeString))
          return DataType.OID;
        if ("positiveInt".equals(codeString))
          return DataType.POSITIVEINT;
        if ("string".equals(codeString))
          return DataType.STRING;
        if ("time".equals(codeString))
          return DataType.TIME;
        if ("unsignedInt".equals(codeString))
          return DataType.UNSIGNEDINT;
        if ("uri".equals(codeString))
          return DataType.URI;
        if ("uuid".equals(codeString))
          return DataType.UUID;
        if ("xhtml".equals(codeString))
          return DataType.XHTML;
        throw new IllegalArgumentException("Unknown DataType code '"+codeString+"'");
        }
    public String toCode(DataType code) {
      if (code == DataType.ADDRESS)
        return "Address";
      if (code == DataType.AGE)
        return "Age";
      if (code == DataType.ANNOTATION)
        return "Annotation";
      if (code == DataType.ATTACHMENT)
        return "Attachment";
      if (code == DataType.BACKBONEELEMENT)
        return "BackboneElement";
      if (code == DataType.CODEABLECONCEPT)
        return "CodeableConcept";
      if (code == DataType.CODING)
        return "Coding";
      if (code == DataType.CONTACTPOINT)
        return "ContactPoint";
      if (code == DataType.COUNT)
        return "Count";
      if (code == DataType.DISTANCE)
        return "Distance";
      if (code == DataType.DURATION)
        return "Duration";
      if (code == DataType.ELEMENT)
        return "Element";
      if (code == DataType.ELEMENTDEFINITION)
        return "ElementDefinition";
      if (code == DataType.EXTENSION)
        return "Extension";
      if (code == DataType.HUMANNAME)
        return "HumanName";
      if (code == DataType.IDENTIFIER)
        return "Identifier";
      if (code == DataType.META)
        return "Meta";
      if (code == DataType.MONEY)
        return "Money";
      if (code == DataType.NARRATIVE)
        return "Narrative";
      if (code == DataType.PERIOD)
        return "Period";
      if (code == DataType.QUANTITY)
        return "Quantity";
      if (code == DataType.RANGE)
        return "Range";
      if (code == DataType.RATIO)
        return "Ratio";
      if (code == DataType.REFERENCE)
        return "Reference";
      if (code == DataType.SAMPLEDDATA)
        return "SampledData";
      if (code == DataType.SIGNATURE)
        return "Signature";
      if (code == DataType.SIMPLEQUANTITY)
        return "SimpleQuantity";
      if (code == DataType.TIMING)
        return "Timing";
      if (code == DataType.BASE64BINARY)
        return "base64Binary";
      if (code == DataType.BOOLEAN)
        return "boolean";
      if (code == DataType.CODE)
        return "code";
      if (code == DataType.DATE)
        return "date";
      if (code == DataType.DATETIME)
        return "dateTime";
      if (code == DataType.DECIMAL)
        return "decimal";
      if (code == DataType.ID)
        return "id";
      if (code == DataType.INSTANT)
        return "instant";
      if (code == DataType.INTEGER)
        return "integer";
      if (code == DataType.MARKDOWN)
        return "markdown";
      if (code == DataType.OID)
        return "oid";
      if (code == DataType.POSITIVEINT)
        return "positiveInt";
      if (code == DataType.STRING)
        return "string";
      if (code == DataType.TIME)
        return "time";
      if (code == DataType.UNSIGNEDINT)
        return "unsignedInt";
      if (code == DataType.URI)
        return "uri";
      if (code == DataType.UUID)
        return "uuid";
      if (code == DataType.XHTML)
        return "xhtml";
      return "?";
      }
    }

    public enum DocumentReferenceStatus {
        /**
         * This is the current reference for this document.
         */
        CURRENT, 
        /**
         * This reference has been superseded by another reference.
         */
        SUPERSEDED, 
        /**
         * This reference was created in error.
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
            case CURRENT: return "This is the current reference for this document.";
            case SUPERSEDED: return "This reference has been superseded by another reference.";
            case ENTEREDINERROR: return "This reference was created in error.";
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
         * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.
         */
        ADDRESS, 
        /**
         * null
         */
        AGE, 
        /**
         * A  text note which also  contains information about who made the statement and when.
         */
        ANNOTATION, 
        /**
         * For referring to data content defined in other formats.
         */
        ATTACHMENT, 
        /**
         * Base definition for all elements that are defined inside a resource - but not those in a data type.
         */
        BACKBONEELEMENT, 
        /**
         * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
         */
        CODEABLECONCEPT, 
        /**
         * A reference to a code defined by a terminology system.
         */
        CODING, 
        /**
         * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
         */
        CONTACTPOINT, 
        /**
         * null
         */
        COUNT, 
        /**
         * null
         */
        DISTANCE, 
        /**
         * null
         */
        DURATION, 
        /**
         * Base definition for all elements in a resource.
         */
        ELEMENT, 
        /**
         * Captures constraints on each element within the resource, profile, or extension.
         */
        ELEMENTDEFINITION, 
        /**
         * Optional Extensions Element - found in all resources.
         */
        EXTENSION, 
        /**
         * A human's name with the ability to identify parts and usage.
         */
        HUMANNAME, 
        /**
         * A technical identifier - identifies some entity uniquely and unambiguously.
         */
        IDENTIFIER, 
        /**
         * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.
         */
        META, 
        /**
         * null
         */
        MONEY, 
        /**
         * A human-readable formatted text, including images.
         */
        NARRATIVE, 
        /**
         * A time period defined by a start and end date and optionally time.
         */
        PERIOD, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        QUANTITY, 
        /**
         * A set of ordered Quantities defined by a low and high limit.
         */
        RANGE, 
        /**
         * A relationship of two Quantity values - expressed as a numerator and a denominator.
         */
        RATIO, 
        /**
         * A reference from one resource to another.
         */
        REFERENCE, 
        /**
         * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
         */
        SAMPLEDDATA, 
        /**
         * A digital signature along with supporting context. The signature may be electronic/cryptographic in nature, or a graphical image representing a hand-written signature, or a signature process. Different Signature approaches have different utilities.
         */
        SIGNATURE, 
        /**
         * null
         */
        SIMPLEQUANTITY, 
        /**
         * Specifies an event that may occur multiple times. Timing schedules are used to record when things are expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds.
         */
        TIMING, 
        /**
         * A stream of bytes
         */
        BASE64BINARY, 
        /**
         * Value of "true" or "false"
         */
        BOOLEAN, 
        /**
         * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents
         */
        CODE, 
        /**
         * A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.
         */
        DATE, 
        /**
         * A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.
         */
        DATETIME, 
        /**
         * A rational number with implicit precision
         */
        DECIMAL, 
        /**
         * Any combination of letters, numerals, "-" and ".", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.
         */
        ID, 
        /**
         * An instant in time - known at least to the second
         */
        INSTANT, 
        /**
         * A whole number
         */
        INTEGER, 
        /**
         * A string that may contain markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN, 
        /**
         * An oid represented as a URI
         */
        OID, 
        /**
         * An integer with a value that is positive (e.g. >0)
         */
        POSITIVEINT, 
        /**
         * A sequence of Unicode characters
         */
        STRING, 
        /**
         * A time during the day, with no date specified
         */
        TIME, 
        /**
         * An integer with a value that is not negative (e.g. >= 0)
         */
        UNSIGNEDINT, 
        /**
         * String of characters used to identify a name or a resource
         */
        URI, 
        /**
         * A UUID, represented as a URI
         */
        UUID, 
        /**
         * XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
         */
        XHTML, 
        /**
         * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centres, etc.
         */
        ACCOUNT, 
        /**
         * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
         */
        ALLERGYINTOLERANCE, 
        /**
         * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT, 
        /**
         * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENTRESPONSE, 
        /**
         * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
         */
        AUDITEVENT, 
        /**
         * Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.
         */
        BASIC, 
        /**
         * A binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY, 
        /**
         * Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSITE, 
        /**
         * A container for a collection of resources.
         */
        BUNDLE, 
        /**
         * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CAREPLAN, 
        /**
         * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
         */
        CLAIM, 
        /**
         * This resource provides the adjudication details from the processing of a Claim resource.
         */
        CLAIMRESPONSE, 
        /**
         * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools such as Apgar score.
         */
        CLINICALIMPRESSION, 
        /**
         * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
         */
        COMMUNICATION, 
        /**
         * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATIONREQUEST, 
        /**
         * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.
         */
        COMPOSITION, 
        /**
         * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.
         */
        CONCEPTMAP, 
        /**
         * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a diagnosis during an encounter; populating a problem list or a summary statement, such as a discharge summary.
         */
        CONDITION, 
        /**
         * A conformance statement is a set of capabilities of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        CONFORMANCE, 
        /**
         * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to pay for or reimburse health care products and services.
         */
        COVERAGE, 
        /**
         * The formal description of a single piece of information that can be gathered and reported.
         */
        DATAELEMENT, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies an instance of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices includes durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.
         */
        DEVICE, 
        /**
         * Describes the characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICECOMPONENT, 
        /**
         * Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICEMETRIC, 
        /**
         * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
         */
        DEVICEUSEREQUEST, 
        /**
         * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
         */
        DEVICEUSESTATEMENT, 
        /**
         * A record of a request for a diagnostic investigation service to be performed.
         */
        DIAGNOSTICORDER, 
        /**
         * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A manifest that defines a set of documents.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document .
         */
        DOCUMENTREFERENCE, 
        /**
         * --- Abstract Type! ---A resource that includes narrative, extensions, and contained resources.
         */
        DOMAINRESOURCE, 
        /**
         * This resource provides the insurance eligibility details from the insurer regarding a specified coverage and optionally some class of service.
         */
        ELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an Eligibility resource.
         */
        ELIGIBILITYRESPONSE, 
        /**
         * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
         */
        ENCOUNTER, 
        /**
         * This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
         */
        ENROLLMENTREQUEST, 
        /**
         * This resource provides enrollment and plan details from the processing of an Enrollment resource.
         */
        ENROLLMENTRESPONSE, 
        /**
         * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
         */
        EPISODEOFCARE, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.
         */
        FAMILYMEMBERHISTORY, 
        /**
         * Prospective warnings of potential issues when providing care to the patient.
         */
        FLAG, 
        /**
         * Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
         */
        GOAL, 
        /**
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
         */
        GROUP, 
        /**
         * The details of a healthcare service available at a location.
         */
        HEALTHCARESERVICE, 
        /**
         * A manifest of a set of DICOM Service-Object Pair Instances (SOP Instances).  The referenced SOP Instances (images or other content) are for a single patient, and may be from one or more studies. The referenced SOP Instances have been selected for a purpose, such as quality assurance, conference, or consult. Reflecting that range of purposes, typical ImagingObjectSelection resources may include all SOP Instances in a study (perhaps for sharing through a Health Information Exchange); key images from multiple studies (for reference by a referring or treating physician); a multi-frame ultrasound instance ("cine" video clip) and a set of measurements taken from that instance (for inclusion in a teaching file); and so on.
         */
        IMAGINGOBJECTSELECTION, 
        /**
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.
         */
        IMMUNIZATION, 
        /**
         * A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules or how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole, and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * A set of information summarized from a list of other resources.
         */
        LIST, 
        /**
         * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
         */
        LOCATION, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.
         */
        MEDICATION, 
        /**
         * Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
         */
        MEDICATIONADMINISTRATION, 
        /**
         * Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.
         */
        MEDICATIONDISPENSE, 
        /**
         * An order for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationOrder" rather than "MedicationPrescription" to generalize the use across inpatient and outpatient settings as well as for care plans, etc.
         */
        MEDICATIONORDER, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from e.g. the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
         */
        MESSAGEHEADER, 
        /**
         * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
         */
        NAMINGSYSTEM, 
        /**
         * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
         */
        NUTRITIONORDER, 
        /**
         * Measurements and simple assertions made about a patient, device or other subject.
         */
        OBSERVATION, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A request to perform an action.
         */
        ORDER, 
        /**
         * A response to an order.
         */
        ORDERRESPONSE, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
         */
        ORGANIZATION, 
        /**
         * This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.
         */
        PARAMETERS, 
        /**
         * Demographics and other administrative information about an individual or animal receiving care or other health-related services.
         */
        PATIENT, 
        /**
         * This resource provides the status of the payment for goods and services rendered, and the request and response resource references.
         */
        PAYMENTNOTICE, 
        /**
         * This resource provides payment details and claim references supporting a bulk payment.
         */
        PAYMENTRECONCILIATION, 
        /**
         * Demographics and administrative information about a person independent of a specific health-related context.
         */
        PERSON, 
        /**
         * A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.
         */
        PROCEDURE, 
        /**
         * A request for a procedure to be performed. May be a proposal or an order.
         */
        PROCEDUREREQUEST, 
        /**
         * This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.
         */
        PROCESSREQUEST, 
        /**
         * This resource provides processing status, errors and notes from the processing of a resource.
         */
        PROCESSRESPONSE, 
        /**
         * Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.
         */
        PROVENANCE, 
        /**
         * A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
         */
        QUESTIONNAIRE, 
        /**
         * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.
         */
        REFERRALREQUEST, 
        /**
         * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * --- Abstract Type! ---This is the base resource type for everything.
         */
        RESOURCE, 
        /**
         * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISKASSESSMENT, 
        /**
         * A container for slot(s) of time that may be available for booking appointments.
         */
        SCHEDULE, 
        /**
         * A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCHPARAMETER, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions, and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system is able to take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Record of delivery of what is supplied.
         */
        SUPPLYDELIVERY, 
        /**
         * A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLYREQUEST, 
        /**
         * TestScript is a resource that specifies a suite of tests against a FHIR server implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * A value set specifies a set of codes drawn from one or more code systems.
         */
        VALUESET, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FHIRDefinedType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Address".equals(codeString))
          return ADDRESS;
        if ("Age".equals(codeString))
          return AGE;
        if ("Annotation".equals(codeString))
          return ANNOTATION;
        if ("Attachment".equals(codeString))
          return ATTACHMENT;
        if ("BackboneElement".equals(codeString))
          return BACKBONEELEMENT;
        if ("CodeableConcept".equals(codeString))
          return CODEABLECONCEPT;
        if ("Coding".equals(codeString))
          return CODING;
        if ("ContactPoint".equals(codeString))
          return CONTACTPOINT;
        if ("Count".equals(codeString))
          return COUNT;
        if ("Distance".equals(codeString))
          return DISTANCE;
        if ("Duration".equals(codeString))
          return DURATION;
        if ("Element".equals(codeString))
          return ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return ELEMENTDEFINITION;
        if ("Extension".equals(codeString))
          return EXTENSION;
        if ("HumanName".equals(codeString))
          return HUMANNAME;
        if ("Identifier".equals(codeString))
          return IDENTIFIER;
        if ("Meta".equals(codeString))
          return META;
        if ("Money".equals(codeString))
          return MONEY;
        if ("Narrative".equals(codeString))
          return NARRATIVE;
        if ("Period".equals(codeString))
          return PERIOD;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("Reference".equals(codeString))
          return REFERENCE;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return SIMPLEQUANTITY;
        if ("Timing".equals(codeString))
          return TIMING;
        if ("base64Binary".equals(codeString))
          return BASE64BINARY;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("code".equals(codeString))
          return CODE;
        if ("date".equals(codeString))
          return DATE;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("decimal".equals(codeString))
          return DECIMAL;
        if ("id".equals(codeString))
          return ID;
        if ("instant".equals(codeString))
          return INSTANT;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("markdown".equals(codeString))
          return MARKDOWN;
        if ("oid".equals(codeString))
          return OID;
        if ("positiveInt".equals(codeString))
          return POSITIVEINT;
        if ("string".equals(codeString))
          return STRING;
        if ("time".equals(codeString))
          return TIME;
        if ("unsignedInt".equals(codeString))
          return UNSIGNEDINT;
        if ("uri".equals(codeString))
          return URI;
        if ("uuid".equals(codeString))
          return UUID;
        if ("xhtml".equals(codeString))
          return XHTML;
        if ("Account".equals(codeString))
          return ACCOUNT;
        if ("AllergyIntolerance".equals(codeString))
          return ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return AUDITEVENT;
        if ("Basic".equals(codeString))
          return BASIC;
        if ("Binary".equals(codeString))
          return BINARY;
        if ("BodySite".equals(codeString))
          return BODYSITE;
        if ("Bundle".equals(codeString))
          return BUNDLE;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Conformance".equals(codeString))
          return CONFORMANCE;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("DataElement".equals(codeString))
          return DATAELEMENT;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceComponent".equals(codeString))
          return DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return DEVICEMETRIC;
        if ("DeviceUseRequest".equals(codeString))
          return DEVICEUSEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticOrder".equals(codeString))
          return DIAGNOSTICORDER;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FLAG;
        if ("Goal".equals(codeString))
          return GOAL;
        if ("Group".equals(codeString))
          return GROUP;
        if ("HealthcareService".equals(codeString))
          return HEALTHCARESERVICE;
        if ("ImagingObjectSelection".equals(codeString))
          return IMAGINGOBJECTSELECTION;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("List".equals(codeString))
          return LIST;
        if ("Location".equals(codeString))
          return LOCATION;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("Medication".equals(codeString))
          return MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationOrder".equals(codeString))
          return MEDICATIONORDER;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MessageHeader".equals(codeString))
          return MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Order".equals(codeString))
          return ORDER;
        if ("OrderResponse".equals(codeString))
          return ORDERRESPONSE;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("Parameters".equals(codeString))
          return PARAMETERS;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("PaymentNotice".equals(codeString))
          return PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return PERSON;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
        if ("ProcedureRequest".equals(codeString))
          return PROCEDUREREQUEST;
        if ("ProcessRequest".equals(codeString))
          return PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return QUESTIONNAIRERESPONSE;
        if ("ReferralRequest".equals(codeString))
          return REFERRALREQUEST;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("Resource".equals(codeString))
          return RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return SEARCHPARAMETER;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        throw new Exception("Unknown FHIRDefinedType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADDRESS: return "Address";
            case AGE: return "Age";
            case ANNOTATION: return "Annotation";
            case ATTACHMENT: return "Attachment";
            case BACKBONEELEMENT: return "BackboneElement";
            case CODEABLECONCEPT: return "CodeableConcept";
            case CODING: return "Coding";
            case CONTACTPOINT: return "ContactPoint";
            case COUNT: return "Count";
            case DISTANCE: return "Distance";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case META: return "Meta";
            case MONEY: return "Money";
            case NARRATIVE: return "Narrative";
            case PERIOD: return "Period";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case TIMING: return "Timing";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CODE: return "code";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            case ID: return "id";
            case INSTANT: return "instant";
            case INTEGER: return "integer";
            case MARKDOWN: return "markdown";
            case OID: return "oid";
            case POSITIVEINT: return "positiveInt";
            case STRING: return "string";
            case TIME: return "time";
            case UNSIGNEDINT: return "unsignedInt";
            case URI: return "uri";
            case UUID: return "uuid";
            case XHTML: return "xhtml";
            case ACCOUNT: return "Account";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BODYSITE: return "BodySite";
            case BUNDLE: return "Bundle";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONFORMANCE: return "Conformance";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case DATAELEMENT: return "DataElement";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICECOMPONENT: return "DeviceComponent";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEUSEREQUEST: return "DeviceUseRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICORDER: return "DiagnosticOrder";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ELIGIBILITYREQUEST: return "EligibilityRequest";
            case ELIGIBILITYRESPONSE: return "EligibilityResponse";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GROUP: return "Group";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGOBJECTSELECTION: return "ImagingObjectSelection";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONORDER: return "MedicationOrder";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORDER: return "Order";
            case ORDERRESPONSE: return "OrderResponse";
            case ORGANIZATION: return "Organization";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case PROCEDURE: return "Procedure";
            case PROCEDUREREQUEST: return "ProcedureRequest";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case REFERRALREQUEST: return "ReferralRequest";
            case RELATEDPERSON: return "RelatedPerson";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TESTSCRIPT: return "TestScript";
            case VALUESET: return "ValueSet";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ADDRESS: return "http://hl7.org/fhir/data-types";
            case AGE: return "http://hl7.org/fhir/data-types";
            case ANNOTATION: return "http://hl7.org/fhir/data-types";
            case ATTACHMENT: return "http://hl7.org/fhir/data-types";
            case BACKBONEELEMENT: return "http://hl7.org/fhir/data-types";
            case CODEABLECONCEPT: return "http://hl7.org/fhir/data-types";
            case CODING: return "http://hl7.org/fhir/data-types";
            case CONTACTPOINT: return "http://hl7.org/fhir/data-types";
            case COUNT: return "http://hl7.org/fhir/data-types";
            case DISTANCE: return "http://hl7.org/fhir/data-types";
            case DURATION: return "http://hl7.org/fhir/data-types";
            case ELEMENT: return "http://hl7.org/fhir/data-types";
            case ELEMENTDEFINITION: return "http://hl7.org/fhir/data-types";
            case EXTENSION: return "http://hl7.org/fhir/data-types";
            case HUMANNAME: return "http://hl7.org/fhir/data-types";
            case IDENTIFIER: return "http://hl7.org/fhir/data-types";
            case META: return "http://hl7.org/fhir/data-types";
            case MONEY: return "http://hl7.org/fhir/data-types";
            case NARRATIVE: return "http://hl7.org/fhir/data-types";
            case PERIOD: return "http://hl7.org/fhir/data-types";
            case QUANTITY: return "http://hl7.org/fhir/data-types";
            case RANGE: return "http://hl7.org/fhir/data-types";
            case RATIO: return "http://hl7.org/fhir/data-types";
            case REFERENCE: return "http://hl7.org/fhir/data-types";
            case SAMPLEDDATA: return "http://hl7.org/fhir/data-types";
            case SIGNATURE: return "http://hl7.org/fhir/data-types";
            case SIMPLEQUANTITY: return "http://hl7.org/fhir/data-types";
            case TIMING: return "http://hl7.org/fhir/data-types";
            case BASE64BINARY: return "http://hl7.org/fhir/data-types";
            case BOOLEAN: return "http://hl7.org/fhir/data-types";
            case CODE: return "http://hl7.org/fhir/data-types";
            case DATE: return "http://hl7.org/fhir/data-types";
            case DATETIME: return "http://hl7.org/fhir/data-types";
            case DECIMAL: return "http://hl7.org/fhir/data-types";
            case ID: return "http://hl7.org/fhir/data-types";
            case INSTANT: return "http://hl7.org/fhir/data-types";
            case INTEGER: return "http://hl7.org/fhir/data-types";
            case MARKDOWN: return "http://hl7.org/fhir/data-types";
            case OID: return "http://hl7.org/fhir/data-types";
            case POSITIVEINT: return "http://hl7.org/fhir/data-types";
            case STRING: return "http://hl7.org/fhir/data-types";
            case TIME: return "http://hl7.org/fhir/data-types";
            case UNSIGNEDINT: return "http://hl7.org/fhir/data-types";
            case URI: return "http://hl7.org/fhir/data-types";
            case UUID: return "http://hl7.org/fhir/data-types";
            case XHTML: return "http://hl7.org/fhir/data-types";
            case ACCOUNT: return "http://hl7.org/fhir/resource-types";
            case ALLERGYINTOLERANCE: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENT: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case AUDITEVENT: return "http://hl7.org/fhir/resource-types";
            case BASIC: return "http://hl7.org/fhir/resource-types";
            case BINARY: return "http://hl7.org/fhir/resource-types";
            case BODYSITE: return "http://hl7.org/fhir/resource-types";
            case BUNDLE: return "http://hl7.org/fhir/resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/resource-types";
            case CLAIM: return "http://hl7.org/fhir/resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/resource-types";
            case CONDITION: return "http://hl7.org/fhir/resource-types";
            case CONFORMANCE: return "http://hl7.org/fhir/resource-types";
            case CONTRACT: return "http://hl7.org/fhir/resource-types";
            case COVERAGE: return "http://hl7.org/fhir/resource-types";
            case DATAELEMENT: return "http://hl7.org/fhir/resource-types";
            case DETECTEDISSUE: return "http://hl7.org/fhir/resource-types";
            case DEVICE: return "http://hl7.org/fhir/resource-types";
            case DEVICECOMPONENT: return "http://hl7.org/fhir/resource-types";
            case DEVICEMETRIC: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSEREQUEST: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICORDER: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/resource-types";
            case DOMAINRESOURCE: return "http://hl7.org/fhir/resource-types";
            case ELIGIBILITYREQUEST: return "http://hl7.org/fhir/resource-types";
            case ELIGIBILITYRESPONSE: return "http://hl7.org/fhir/resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGOBJECTSELECTION: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case LIST: return "http://hl7.org/fhir/resource-types";
            case LOCATION: return "http://hl7.org/fhir/resource-types";
            case MEDIA: return "http://hl7.org/fhir/resource-types";
            case MEDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONORDER: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORDER: return "http://hl7.org/fhir/resource-types";
            case ORDERRESPONSE: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCEDUREREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case REFERRALREQUEST: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ADDRESS: return "There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.";
            case AGE: return "";
            case ANNOTATION: return "A  text note which also  contains information about who made the statement and when.";
            case ATTACHMENT: return "For referring to data content defined in other formats.";
            case BACKBONEELEMENT: return "Base definition for all elements that are defined inside a resource - but not those in a data type.";
            case CODEABLECONCEPT: return "A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.";
            case CODING: return "A reference to a code defined by a terminology system.";
            case CONTACTPOINT: return "Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.";
            case COUNT: return "";
            case DISTANCE: return "";
            case DURATION: return "";
            case ELEMENT: return "Base definition for all elements in a resource.";
            case ELEMENTDEFINITION: return "Captures constraints on each element within the resource, profile, or extension.";
            case EXTENSION: return "Optional Extensions Element - found in all resources.";
            case HUMANNAME: return "A human's name with the ability to identify parts and usage.";
            case IDENTIFIER: return "A technical identifier - identifies some entity uniquely and unambiguously.";
            case META: return "The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.";
            case MONEY: return "";
            case NARRATIVE: return "A human-readable formatted text, including images.";
            case PERIOD: return "A time period defined by a start and end date and optionally time.";
            case QUANTITY: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case RANGE: return "A set of ordered Quantities defined by a low and high limit.";
            case RATIO: return "A relationship of two Quantity values - expressed as a numerator and a denominator.";
            case REFERENCE: return "A reference from one resource to another.";
            case SAMPLEDDATA: return "A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.";
            case SIGNATURE: return "A digital signature along with supporting context. The signature may be electronic/cryptographic in nature, or a graphical image representing a hand-written signature, or a signature process. Different Signature approaches have different utilities.";
            case SIMPLEQUANTITY: return "";
            case TIMING: return "Specifies an event that may occur multiple times. Timing schedules are used to record when things are expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds.";
            case BASE64BINARY: return "A stream of bytes";
            case BOOLEAN: return "Value of \"true\" or \"false\"";
            case CODE: return "A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents";
            case DATE: return "A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.";
            case DECIMAL: return "A rational number with implicit precision";
            case ID: return "Any combination of letters, numerals, \"-\" and \".\", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.";
            case INSTANT: return "An instant in time - known at least to the second";
            case INTEGER: return "A whole number";
            case MARKDOWN: return "A string that may contain markdown syntax for optional processing by a mark down presentation engine";
            case OID: return "An oid represented as a URI";
            case POSITIVEINT: return "An integer with a value that is positive (e.g. >0)";
            case STRING: return "A sequence of Unicode characters";
            case TIME: return "A time during the day, with no date specified";
            case UNSIGNEDINT: return "An integer with a value that is not negative (e.g. >= 0)";
            case URI: return "String of characters used to identify a name or a resource";
            case UUID: return "A UUID, represented as a URI";
            case XHTML: return "XHTML format, as defined by W3C, but restricted usage (mainly, no active content)";
            case ACCOUNT: return "A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centres, etc.";
            case ALLERGYINTOLERANCE: return "Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.";
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case AUDITEVENT: return "A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.";
            case BASIC: return "Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.";
            case BINARY: return "A binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BODYSITE: return "Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.";
            case CONDITION: return "Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a diagnosis during an encounter; populating a problem list or a summary statement, such as a discharge summary.";
            case CONFORMANCE: return "A conformance statement is a set of capabilities of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CONTRACT: return "A formal agreement between parties regarding the conduct of business, exchange of information or other matters.";
            case COVERAGE: return "Financial instrument which may be used to pay for or reimburse health care products and services.";
            case DATAELEMENT: return "The formal description of a single piece of information that can be gathered and reported.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies an instance of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices includes durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.";
            case DEVICECOMPONENT: return "Describes the characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEUSEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICORDER: return "A record of a request for a diagnostic investigation service to be performed.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A manifest that defines a set of documents.";
            case DOCUMENTREFERENCE: return "A reference to a document .";
            case DOMAINRESOURCE: return "--- Abstract Type! ---A resource that includes narrative, extensions, and contained resources.";
            case ELIGIBILITYREQUEST: return "This resource provides the insurance eligibility details from the insurer regarding a specified coverage and optionally some class of service.";
            case ELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an Eligibility resource.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGOBJECTSELECTION: return "A manifest of a set of DICOM Service-Object Pair Instances (SOP Instances).  The referenced SOP Instances (images or other content) are for a single patient, and may be from one or more studies. The referenced SOP Instances have been selected for a purpose, such as quality assurance, conference, or consult. Reflecting that range of purposes, typical ImagingObjectSelection resources may include all SOP Instances in a study (perhaps for sharing through a Health Information Exchange); key images from multiple studies (for reference by a referring or treating physician); a multi-frame ultrasound instance (\"cine\" video clip) and a set of measurements taken from that instance (for inclusion in a teaching file); and so on.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules or how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole, and to publish a computable definition of all the parts.";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONORDER: return "An order for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationOrder\" rather than \"MedicationPrescription\" to generalize the use across inpatient and outpatient settings as well as for care plans, etc.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from e.g. the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORDER: return "A request to perform an action.";
            case ORDERRESPONSE: return "A response to an order.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PROCEDURE: return "An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.";
            case PROCEDUREREQUEST: return "A request for a procedure to be performed. May be a proposal or an order.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.";
            case REFERRALREQUEST: return "Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case RESOURCE: return "--- Abstract Type! ---This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slot(s) of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions, and constraints on resources and data types.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system is able to take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TESTSCRIPT: return "TestScript is a resource that specifies a suite of tests against a FHIR server implementation to determine compliance against the FHIR specification.";
            case VALUESET: return "A value set specifies a set of codes drawn from one or more code systems.";
            case VISIONPRESCRIPTION: return "An authorization for the supply of glasses and/or contact lenses to a patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADDRESS: return "Address";
            case AGE: return "Age";
            case ANNOTATION: return "Annotation";
            case ATTACHMENT: return "Attachment";
            case BACKBONEELEMENT: return "BackboneElement";
            case CODEABLECONCEPT: return "CodeableConcept";
            case CODING: return "Coding";
            case CONTACTPOINT: return "ContactPoint";
            case COUNT: return "Count";
            case DISTANCE: return "Distance";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case META: return "Meta";
            case MONEY: return "Money";
            case NARRATIVE: return "Narrative";
            case PERIOD: return "Period";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case TIMING: return "Timing";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CODE: return "code";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            case ID: return "id";
            case INSTANT: return "instant";
            case INTEGER: return "integer";
            case MARKDOWN: return "markdown";
            case OID: return "oid";
            case POSITIVEINT: return "positiveInt";
            case STRING: return "string";
            case TIME: return "time";
            case UNSIGNEDINT: return "unsignedInt";
            case URI: return "uri";
            case UUID: return "uuid";
            case XHTML: return "XHTML";
            case ACCOUNT: return "Account";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BODYSITE: return "BodySite";
            case BUNDLE: return "Bundle";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONFORMANCE: return "Conformance";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case DATAELEMENT: return "DataElement";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICECOMPONENT: return "DeviceComponent";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEUSEREQUEST: return "DeviceUseRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICORDER: return "DiagnosticOrder";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ELIGIBILITYREQUEST: return "EligibilityRequest";
            case ELIGIBILITYRESPONSE: return "EligibilityResponse";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GROUP: return "Group";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGOBJECTSELECTION: return "ImagingObjectSelection";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONORDER: return "MedicationOrder";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORDER: return "Order";
            case ORDERRESPONSE: return "OrderResponse";
            case ORGANIZATION: return "Organization";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case PROCEDURE: return "Procedure";
            case PROCEDUREREQUEST: return "ProcedureRequest";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case REFERRALREQUEST: return "ReferralRequest";
            case RELATEDPERSON: return "RelatedPerson";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TESTSCRIPT: return "TestScript";
            case VALUESET: return "ValueSet";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
    }

  public static class FHIRDefinedTypeEnumFactory implements EnumFactory<FHIRDefinedType> {
    public FHIRDefinedType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Address".equals(codeString))
          return FHIRDefinedType.ADDRESS;
        if ("Age".equals(codeString))
          return FHIRDefinedType.AGE;
        if ("Annotation".equals(codeString))
          return FHIRDefinedType.ANNOTATION;
        if ("Attachment".equals(codeString))
          return FHIRDefinedType.ATTACHMENT;
        if ("BackboneElement".equals(codeString))
          return FHIRDefinedType.BACKBONEELEMENT;
        if ("CodeableConcept".equals(codeString))
          return FHIRDefinedType.CODEABLECONCEPT;
        if ("Coding".equals(codeString))
          return FHIRDefinedType.CODING;
        if ("ContactPoint".equals(codeString))
          return FHIRDefinedType.CONTACTPOINT;
        if ("Count".equals(codeString))
          return FHIRDefinedType.COUNT;
        if ("Distance".equals(codeString))
          return FHIRDefinedType.DISTANCE;
        if ("Duration".equals(codeString))
          return FHIRDefinedType.DURATION;
        if ("Element".equals(codeString))
          return FHIRDefinedType.ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return FHIRDefinedType.ELEMENTDEFINITION;
        if ("Extension".equals(codeString))
          return FHIRDefinedType.EXTENSION;
        if ("HumanName".equals(codeString))
          return FHIRDefinedType.HUMANNAME;
        if ("Identifier".equals(codeString))
          return FHIRDefinedType.IDENTIFIER;
        if ("Meta".equals(codeString))
          return FHIRDefinedType.META;
        if ("Money".equals(codeString))
          return FHIRDefinedType.MONEY;
        if ("Narrative".equals(codeString))
          return FHIRDefinedType.NARRATIVE;
        if ("Period".equals(codeString))
          return FHIRDefinedType.PERIOD;
        if ("Quantity".equals(codeString))
          return FHIRDefinedType.QUANTITY;
        if ("Range".equals(codeString))
          return FHIRDefinedType.RANGE;
        if ("Ratio".equals(codeString))
          return FHIRDefinedType.RATIO;
        if ("Reference".equals(codeString))
          return FHIRDefinedType.REFERENCE;
        if ("SampledData".equals(codeString))
          return FHIRDefinedType.SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return FHIRDefinedType.SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return FHIRDefinedType.SIMPLEQUANTITY;
        if ("Timing".equals(codeString))
          return FHIRDefinedType.TIMING;
        if ("base64Binary".equals(codeString))
          return FHIRDefinedType.BASE64BINARY;
        if ("boolean".equals(codeString))
          return FHIRDefinedType.BOOLEAN;
        if ("code".equals(codeString))
          return FHIRDefinedType.CODE;
        if ("date".equals(codeString))
          return FHIRDefinedType.DATE;
        if ("dateTime".equals(codeString))
          return FHIRDefinedType.DATETIME;
        if ("decimal".equals(codeString))
          return FHIRDefinedType.DECIMAL;
        if ("id".equals(codeString))
          return FHIRDefinedType.ID;
        if ("instant".equals(codeString))
          return FHIRDefinedType.INSTANT;
        if ("integer".equals(codeString))
          return FHIRDefinedType.INTEGER;
        if ("markdown".equals(codeString))
          return FHIRDefinedType.MARKDOWN;
        if ("oid".equals(codeString))
          return FHIRDefinedType.OID;
        if ("positiveInt".equals(codeString))
          return FHIRDefinedType.POSITIVEINT;
        if ("string".equals(codeString))
          return FHIRDefinedType.STRING;
        if ("time".equals(codeString))
          return FHIRDefinedType.TIME;
        if ("unsignedInt".equals(codeString))
          return FHIRDefinedType.UNSIGNEDINT;
        if ("uri".equals(codeString))
          return FHIRDefinedType.URI;
        if ("uuid".equals(codeString))
          return FHIRDefinedType.UUID;
        if ("xhtml".equals(codeString))
          return FHIRDefinedType.XHTML;
        if ("Account".equals(codeString))
          return FHIRDefinedType.ACCOUNT;
        if ("AllergyIntolerance".equals(codeString))
          return FHIRDefinedType.ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return FHIRDefinedType.APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return FHIRDefinedType.APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return FHIRDefinedType.AUDITEVENT;
        if ("Basic".equals(codeString))
          return FHIRDefinedType.BASIC;
        if ("Binary".equals(codeString))
          return FHIRDefinedType.BINARY;
        if ("BodySite".equals(codeString))
          return FHIRDefinedType.BODYSITE;
        if ("Bundle".equals(codeString))
          return FHIRDefinedType.BUNDLE;
        if ("CarePlan".equals(codeString))
          return FHIRDefinedType.CAREPLAN;
        if ("Claim".equals(codeString))
          return FHIRDefinedType.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return FHIRDefinedType.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return FHIRDefinedType.CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return FHIRDefinedType.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return FHIRDefinedType.COMMUNICATIONREQUEST;
        if ("Composition".equals(codeString))
          return FHIRDefinedType.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return FHIRDefinedType.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return FHIRDefinedType.CONDITION;
        if ("Conformance".equals(codeString))
          return FHIRDefinedType.CONFORMANCE;
        if ("Contract".equals(codeString))
          return FHIRDefinedType.CONTRACT;
        if ("Coverage".equals(codeString))
          return FHIRDefinedType.COVERAGE;
        if ("DataElement".equals(codeString))
          return FHIRDefinedType.DATAELEMENT;
        if ("DetectedIssue".equals(codeString))
          return FHIRDefinedType.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return FHIRDefinedType.DEVICE;
        if ("DeviceComponent".equals(codeString))
          return FHIRDefinedType.DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return FHIRDefinedType.DEVICEMETRIC;
        if ("DeviceUseRequest".equals(codeString))
          return FHIRDefinedType.DEVICEUSEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return FHIRDefinedType.DEVICEUSESTATEMENT;
        if ("DiagnosticOrder".equals(codeString))
          return FHIRDefinedType.DIAGNOSTICORDER;
        if ("DiagnosticReport".equals(codeString))
          return FHIRDefinedType.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return FHIRDefinedType.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return FHIRDefinedType.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return FHIRDefinedType.DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return FHIRDefinedType.ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return FHIRDefinedType.ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return FHIRDefinedType.ENCOUNTER;
        if ("EnrollmentRequest".equals(codeString))
          return FHIRDefinedType.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return FHIRDefinedType.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return FHIRDefinedType.EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return FHIRDefinedType.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FHIRDefinedType.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FHIRDefinedType.FLAG;
        if ("Goal".equals(codeString))
          return FHIRDefinedType.GOAL;
        if ("Group".equals(codeString))
          return FHIRDefinedType.GROUP;
        if ("HealthcareService".equals(codeString))
          return FHIRDefinedType.HEALTHCARESERVICE;
        if ("ImagingObjectSelection".equals(codeString))
          return FHIRDefinedType.IMAGINGOBJECTSELECTION;
        if ("ImagingStudy".equals(codeString))
          return FHIRDefinedType.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return FHIRDefinedType.IMMUNIZATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return FHIRDefinedType.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return FHIRDefinedType.IMPLEMENTATIONGUIDE;
        if ("List".equals(codeString))
          return FHIRDefinedType.LIST;
        if ("Location".equals(codeString))
          return FHIRDefinedType.LOCATION;
        if ("Media".equals(codeString))
          return FHIRDefinedType.MEDIA;
        if ("Medication".equals(codeString))
          return FHIRDefinedType.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return FHIRDefinedType.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return FHIRDefinedType.MEDICATIONDISPENSE;
        if ("MedicationOrder".equals(codeString))
          return FHIRDefinedType.MEDICATIONORDER;
        if ("MedicationStatement".equals(codeString))
          return FHIRDefinedType.MEDICATIONSTATEMENT;
        if ("MessageHeader".equals(codeString))
          return FHIRDefinedType.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return FHIRDefinedType.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return FHIRDefinedType.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return FHIRDefinedType.OBSERVATION;
        if ("OperationDefinition".equals(codeString))
          return FHIRDefinedType.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return FHIRDefinedType.OPERATIONOUTCOME;
        if ("Order".equals(codeString))
          return FHIRDefinedType.ORDER;
        if ("OrderResponse".equals(codeString))
          return FHIRDefinedType.ORDERRESPONSE;
        if ("Organization".equals(codeString))
          return FHIRDefinedType.ORGANIZATION;
        if ("Parameters".equals(codeString))
          return FHIRDefinedType.PARAMETERS;
        if ("Patient".equals(codeString))
          return FHIRDefinedType.PATIENT;
        if ("PaymentNotice".equals(codeString))
          return FHIRDefinedType.PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return FHIRDefinedType.PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return FHIRDefinedType.PERSON;
        if ("Practitioner".equals(codeString))
          return FHIRDefinedType.PRACTITIONER;
        if ("Procedure".equals(codeString))
          return FHIRDefinedType.PROCEDURE;
        if ("ProcedureRequest".equals(codeString))
          return FHIRDefinedType.PROCEDUREREQUEST;
        if ("ProcessRequest".equals(codeString))
          return FHIRDefinedType.PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return FHIRDefinedType.PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return FHIRDefinedType.PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return FHIRDefinedType.QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return FHIRDefinedType.QUESTIONNAIRERESPONSE;
        if ("ReferralRequest".equals(codeString))
          return FHIRDefinedType.REFERRALREQUEST;
        if ("RelatedPerson".equals(codeString))
          return FHIRDefinedType.RELATEDPERSON;
        if ("Resource".equals(codeString))
          return FHIRDefinedType.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return FHIRDefinedType.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return FHIRDefinedType.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return FHIRDefinedType.SEARCHPARAMETER;
        if ("Slot".equals(codeString))
          return FHIRDefinedType.SLOT;
        if ("Specimen".equals(codeString))
          return FHIRDefinedType.SPECIMEN;
        if ("StructureDefinition".equals(codeString))
          return FHIRDefinedType.STRUCTUREDEFINITION;
        if ("Subscription".equals(codeString))
          return FHIRDefinedType.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return FHIRDefinedType.SUBSTANCE;
        if ("SupplyDelivery".equals(codeString))
          return FHIRDefinedType.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return FHIRDefinedType.SUPPLYREQUEST;
        if ("TestScript".equals(codeString))
          return FHIRDefinedType.TESTSCRIPT;
        if ("ValueSet".equals(codeString))
          return FHIRDefinedType.VALUESET;
        if ("VisionPrescription".equals(codeString))
          return FHIRDefinedType.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown FHIRDefinedType code '"+codeString+"'");
        }
    public String toCode(FHIRDefinedType code) {
      if (code == FHIRDefinedType.ADDRESS)
        return "Address";
      if (code == FHIRDefinedType.AGE)
        return "Age";
      if (code == FHIRDefinedType.ANNOTATION)
        return "Annotation";
      if (code == FHIRDefinedType.ATTACHMENT)
        return "Attachment";
      if (code == FHIRDefinedType.BACKBONEELEMENT)
        return "BackboneElement";
      if (code == FHIRDefinedType.CODEABLECONCEPT)
        return "CodeableConcept";
      if (code == FHIRDefinedType.CODING)
        return "Coding";
      if (code == FHIRDefinedType.CONTACTPOINT)
        return "ContactPoint";
      if (code == FHIRDefinedType.COUNT)
        return "Count";
      if (code == FHIRDefinedType.DISTANCE)
        return "Distance";
      if (code == FHIRDefinedType.DURATION)
        return "Duration";
      if (code == FHIRDefinedType.ELEMENT)
        return "Element";
      if (code == FHIRDefinedType.ELEMENTDEFINITION)
        return "ElementDefinition";
      if (code == FHIRDefinedType.EXTENSION)
        return "Extension";
      if (code == FHIRDefinedType.HUMANNAME)
        return "HumanName";
      if (code == FHIRDefinedType.IDENTIFIER)
        return "Identifier";
      if (code == FHIRDefinedType.META)
        return "Meta";
      if (code == FHIRDefinedType.MONEY)
        return "Money";
      if (code == FHIRDefinedType.NARRATIVE)
        return "Narrative";
      if (code == FHIRDefinedType.PERIOD)
        return "Period";
      if (code == FHIRDefinedType.QUANTITY)
        return "Quantity";
      if (code == FHIRDefinedType.RANGE)
        return "Range";
      if (code == FHIRDefinedType.RATIO)
        return "Ratio";
      if (code == FHIRDefinedType.REFERENCE)
        return "Reference";
      if (code == FHIRDefinedType.SAMPLEDDATA)
        return "SampledData";
      if (code == FHIRDefinedType.SIGNATURE)
        return "Signature";
      if (code == FHIRDefinedType.SIMPLEQUANTITY)
        return "SimpleQuantity";
      if (code == FHIRDefinedType.TIMING)
        return "Timing";
      if (code == FHIRDefinedType.BASE64BINARY)
        return "base64Binary";
      if (code == FHIRDefinedType.BOOLEAN)
        return "boolean";
      if (code == FHIRDefinedType.CODE)
        return "code";
      if (code == FHIRDefinedType.DATE)
        return "date";
      if (code == FHIRDefinedType.DATETIME)
        return "dateTime";
      if (code == FHIRDefinedType.DECIMAL)
        return "decimal";
      if (code == FHIRDefinedType.ID)
        return "id";
      if (code == FHIRDefinedType.INSTANT)
        return "instant";
      if (code == FHIRDefinedType.INTEGER)
        return "integer";
      if (code == FHIRDefinedType.MARKDOWN)
        return "markdown";
      if (code == FHIRDefinedType.OID)
        return "oid";
      if (code == FHIRDefinedType.POSITIVEINT)
        return "positiveInt";
      if (code == FHIRDefinedType.STRING)
        return "string";
      if (code == FHIRDefinedType.TIME)
        return "time";
      if (code == FHIRDefinedType.UNSIGNEDINT)
        return "unsignedInt";
      if (code == FHIRDefinedType.URI)
        return "uri";
      if (code == FHIRDefinedType.UUID)
        return "uuid";
      if (code == FHIRDefinedType.XHTML)
        return "xhtml";
      if (code == FHIRDefinedType.ACCOUNT)
        return "Account";
      if (code == FHIRDefinedType.ALLERGYINTOLERANCE)
        return "AllergyIntolerance";
      if (code == FHIRDefinedType.APPOINTMENT)
        return "Appointment";
      if (code == FHIRDefinedType.APPOINTMENTRESPONSE)
        return "AppointmentResponse";
      if (code == FHIRDefinedType.AUDITEVENT)
        return "AuditEvent";
      if (code == FHIRDefinedType.BASIC)
        return "Basic";
      if (code == FHIRDefinedType.BINARY)
        return "Binary";
      if (code == FHIRDefinedType.BODYSITE)
        return "BodySite";
      if (code == FHIRDefinedType.BUNDLE)
        return "Bundle";
      if (code == FHIRDefinedType.CAREPLAN)
        return "CarePlan";
      if (code == FHIRDefinedType.CLAIM)
        return "Claim";
      if (code == FHIRDefinedType.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == FHIRDefinedType.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == FHIRDefinedType.COMMUNICATION)
        return "Communication";
      if (code == FHIRDefinedType.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == FHIRDefinedType.COMPOSITION)
        return "Composition";
      if (code == FHIRDefinedType.CONCEPTMAP)
        return "ConceptMap";
      if (code == FHIRDefinedType.CONDITION)
        return "Condition";
      if (code == FHIRDefinedType.CONFORMANCE)
        return "Conformance";
      if (code == FHIRDefinedType.CONTRACT)
        return "Contract";
      if (code == FHIRDefinedType.COVERAGE)
        return "Coverage";
      if (code == FHIRDefinedType.DATAELEMENT)
        return "DataElement";
      if (code == FHIRDefinedType.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == FHIRDefinedType.DEVICE)
        return "Device";
      if (code == FHIRDefinedType.DEVICECOMPONENT)
        return "DeviceComponent";
      if (code == FHIRDefinedType.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == FHIRDefinedType.DEVICEUSEREQUEST)
        return "DeviceUseRequest";
      if (code == FHIRDefinedType.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == FHIRDefinedType.DIAGNOSTICORDER)
        return "DiagnosticOrder";
      if (code == FHIRDefinedType.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == FHIRDefinedType.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == FHIRDefinedType.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == FHIRDefinedType.DOMAINRESOURCE)
        return "DomainResource";
      if (code == FHIRDefinedType.ELIGIBILITYREQUEST)
        return "EligibilityRequest";
      if (code == FHIRDefinedType.ELIGIBILITYRESPONSE)
        return "EligibilityResponse";
      if (code == FHIRDefinedType.ENCOUNTER)
        return "Encounter";
      if (code == FHIRDefinedType.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == FHIRDefinedType.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == FHIRDefinedType.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == FHIRDefinedType.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == FHIRDefinedType.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == FHIRDefinedType.FLAG)
        return "Flag";
      if (code == FHIRDefinedType.GOAL)
        return "Goal";
      if (code == FHIRDefinedType.GROUP)
        return "Group";
      if (code == FHIRDefinedType.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == FHIRDefinedType.IMAGINGOBJECTSELECTION)
        return "ImagingObjectSelection";
      if (code == FHIRDefinedType.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == FHIRDefinedType.IMMUNIZATION)
        return "Immunization";
      if (code == FHIRDefinedType.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == FHIRDefinedType.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == FHIRDefinedType.LIST)
        return "List";
      if (code == FHIRDefinedType.LOCATION)
        return "Location";
      if (code == FHIRDefinedType.MEDIA)
        return "Media";
      if (code == FHIRDefinedType.MEDICATION)
        return "Medication";
      if (code == FHIRDefinedType.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == FHIRDefinedType.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == FHIRDefinedType.MEDICATIONORDER)
        return "MedicationOrder";
      if (code == FHIRDefinedType.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == FHIRDefinedType.MESSAGEHEADER)
        return "MessageHeader";
      if (code == FHIRDefinedType.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == FHIRDefinedType.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == FHIRDefinedType.OBSERVATION)
        return "Observation";
      if (code == FHIRDefinedType.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == FHIRDefinedType.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == FHIRDefinedType.ORDER)
        return "Order";
      if (code == FHIRDefinedType.ORDERRESPONSE)
        return "OrderResponse";
      if (code == FHIRDefinedType.ORGANIZATION)
        return "Organization";
      if (code == FHIRDefinedType.PARAMETERS)
        return "Parameters";
      if (code == FHIRDefinedType.PATIENT)
        return "Patient";
      if (code == FHIRDefinedType.PAYMENTNOTICE)
        return "PaymentNotice";
      if (code == FHIRDefinedType.PAYMENTRECONCILIATION)
        return "PaymentReconciliation";
      if (code == FHIRDefinedType.PERSON)
        return "Person";
      if (code == FHIRDefinedType.PRACTITIONER)
        return "Practitioner";
      if (code == FHIRDefinedType.PROCEDURE)
        return "Procedure";
      if (code == FHIRDefinedType.PROCEDUREREQUEST)
        return "ProcedureRequest";
      if (code == FHIRDefinedType.PROCESSREQUEST)
        return "ProcessRequest";
      if (code == FHIRDefinedType.PROCESSRESPONSE)
        return "ProcessResponse";
      if (code == FHIRDefinedType.PROVENANCE)
        return "Provenance";
      if (code == FHIRDefinedType.QUESTIONNAIRE)
        return "Questionnaire";
      if (code == FHIRDefinedType.QUESTIONNAIRERESPONSE)
        return "QuestionnaireResponse";
      if (code == FHIRDefinedType.REFERRALREQUEST)
        return "ReferralRequest";
      if (code == FHIRDefinedType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == FHIRDefinedType.RESOURCE)
        return "Resource";
      if (code == FHIRDefinedType.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == FHIRDefinedType.SCHEDULE)
        return "Schedule";
      if (code == FHIRDefinedType.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == FHIRDefinedType.SLOT)
        return "Slot";
      if (code == FHIRDefinedType.SPECIMEN)
        return "Specimen";
      if (code == FHIRDefinedType.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == FHIRDefinedType.SUBSCRIPTION)
        return "Subscription";
      if (code == FHIRDefinedType.SUBSTANCE)
        return "Substance";
      if (code == FHIRDefinedType.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == FHIRDefinedType.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == FHIRDefinedType.TESTSCRIPT)
        return "TestScript";
      if (code == FHIRDefinedType.VALUESET)
        return "ValueSet";
      if (code == FHIRDefinedType.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    }

    public enum MessageEvent {
        /**
         * Change the status of a Medication Administration to show that it is complete.
         */
        MEDICATIONADMINISTRATIONCOMPLETE, 
        /**
         * Someone wishes to record that the record of administration of a medication is in error and should be ignored.
         */
        MEDICATIONADMINISTRATIONNULLIFICATION, 
        /**
         * Indicates that a medication has been recorded against the patient's record.
         */
        MEDICATIONADMINISTRATIONRECORDING, 
        /**
         * Update a Medication Administration record.
         */
        MEDICATIONADMINISTRATIONUPDATE, 
        /**
         * Notification of a change to an administrative resource (either create or update). Note that there is no delete, though some administrative resources have status or period elements for this use.
         */
        ADMINNOTIFY, 
        /**
         * Provide a diagnostic report, or update a previously provided diagnostic report.
         */
        DIAGNOSTICREPORTPROVIDE, 
        /**
         * Provide a simple observation or update a previously provided simple observation.
         */
        OBSERVATIONPROVIDE, 
        /**
         * Notification that two patient records actually identify the same patient.
         */
        PATIENTLINK, 
        /**
         * Notification that previous advice that two patient records concern the same patient is now considered incorrect.
         */
        PATIENTUNLINK, 
        /**
         * The definition of a value set is used to create a simple collection of codes suitable for use for data entry or validation. An expanded value set will be returned, or an error message.
         */
        VALUESETEXPAND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MessageEvent fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("MedicationAdministration-Complete".equals(codeString))
          return MEDICATIONADMINISTRATIONCOMPLETE;
        if ("MedicationAdministration-Nullification".equals(codeString))
          return MEDICATIONADMINISTRATIONNULLIFICATION;
        if ("MedicationAdministration-Recording".equals(codeString))
          return MEDICATIONADMINISTRATIONRECORDING;
        if ("MedicationAdministration-Update".equals(codeString))
          return MEDICATIONADMINISTRATIONUPDATE;
        if ("admin-notify".equals(codeString))
          return ADMINNOTIFY;
        if ("diagnosticreport-provide".equals(codeString))
          return DIAGNOSTICREPORTPROVIDE;
        if ("observation-provide".equals(codeString))
          return OBSERVATIONPROVIDE;
        if ("patient-link".equals(codeString))
          return PATIENTLINK;
        if ("patient-unlink".equals(codeString))
          return PATIENTUNLINK;
        if ("valueset-expand".equals(codeString))
          return VALUESETEXPAND;
        throw new Exception("Unknown MessageEvent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MEDICATIONADMINISTRATIONCOMPLETE: return "MedicationAdministration-Complete";
            case MEDICATIONADMINISTRATIONNULLIFICATION: return "MedicationAdministration-Nullification";
            case MEDICATIONADMINISTRATIONRECORDING: return "MedicationAdministration-Recording";
            case MEDICATIONADMINISTRATIONUPDATE: return "MedicationAdministration-Update";
            case ADMINNOTIFY: return "admin-notify";
            case DIAGNOSTICREPORTPROVIDE: return "diagnosticreport-provide";
            case OBSERVATIONPROVIDE: return "observation-provide";
            case PATIENTLINK: return "patient-link";
            case PATIENTUNLINK: return "patient-unlink";
            case VALUESETEXPAND: return "valueset-expand";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MEDICATIONADMINISTRATIONCOMPLETE: return "http://hl7.org/fhir/message-events";
            case MEDICATIONADMINISTRATIONNULLIFICATION: return "http://hl7.org/fhir/message-events";
            case MEDICATIONADMINISTRATIONRECORDING: return "http://hl7.org/fhir/message-events";
            case MEDICATIONADMINISTRATIONUPDATE: return "http://hl7.org/fhir/message-events";
            case ADMINNOTIFY: return "http://hl7.org/fhir/message-events";
            case DIAGNOSTICREPORTPROVIDE: return "http://hl7.org/fhir/message-events";
            case OBSERVATIONPROVIDE: return "http://hl7.org/fhir/message-events";
            case PATIENTLINK: return "http://hl7.org/fhir/message-events";
            case PATIENTUNLINK: return "http://hl7.org/fhir/message-events";
            case VALUESETEXPAND: return "http://hl7.org/fhir/message-events";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MEDICATIONADMINISTRATIONCOMPLETE: return "Change the status of a Medication Administration to show that it is complete.";
            case MEDICATIONADMINISTRATIONNULLIFICATION: return "Someone wishes to record that the record of administration of a medication is in error and should be ignored.";
            case MEDICATIONADMINISTRATIONRECORDING: return "Indicates that a medication has been recorded against the patient's record.";
            case MEDICATIONADMINISTRATIONUPDATE: return "Update a Medication Administration record.";
            case ADMINNOTIFY: return "Notification of a change to an administrative resource (either create or update). Note that there is no delete, though some administrative resources have status or period elements for this use.";
            case DIAGNOSTICREPORTPROVIDE: return "Provide a diagnostic report, or update a previously provided diagnostic report.";
            case OBSERVATIONPROVIDE: return "Provide a simple observation or update a previously provided simple observation.";
            case PATIENTLINK: return "Notification that two patient records actually identify the same patient.";
            case PATIENTUNLINK: return "Notification that previous advice that two patient records concern the same patient is now considered incorrect.";
            case VALUESETEXPAND: return "The definition of a value set is used to create a simple collection of codes suitable for use for data entry or validation. An expanded value set will be returned, or an error message.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MEDICATIONADMINISTRATIONCOMPLETE: return "MedicationAdministration-Complete";
            case MEDICATIONADMINISTRATIONNULLIFICATION: return "MedicationAdministration-Nullification";
            case MEDICATIONADMINISTRATIONRECORDING: return "MedicationAdministration-Recording";
            case MEDICATIONADMINISTRATIONUPDATE: return "MedicationAdministration-Update";
            case ADMINNOTIFY: return "admin-notify";
            case DIAGNOSTICREPORTPROVIDE: return "diagnosticreport-provide";
            case OBSERVATIONPROVIDE: return "observation-provide";
            case PATIENTLINK: return "patient-link";
            case PATIENTUNLINK: return "patient-unlink";
            case VALUESETEXPAND: return "valueset-expand";
            default: return "?";
          }
        }
    }

  public static class MessageEventEnumFactory implements EnumFactory<MessageEvent> {
    public MessageEvent fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("MedicationAdministration-Complete".equals(codeString))
          return MessageEvent.MEDICATIONADMINISTRATIONCOMPLETE;
        if ("MedicationAdministration-Nullification".equals(codeString))
          return MessageEvent.MEDICATIONADMINISTRATIONNULLIFICATION;
        if ("MedicationAdministration-Recording".equals(codeString))
          return MessageEvent.MEDICATIONADMINISTRATIONRECORDING;
        if ("MedicationAdministration-Update".equals(codeString))
          return MessageEvent.MEDICATIONADMINISTRATIONUPDATE;
        if ("admin-notify".equals(codeString))
          return MessageEvent.ADMINNOTIFY;
        if ("diagnosticreport-provide".equals(codeString))
          return MessageEvent.DIAGNOSTICREPORTPROVIDE;
        if ("observation-provide".equals(codeString))
          return MessageEvent.OBSERVATIONPROVIDE;
        if ("patient-link".equals(codeString))
          return MessageEvent.PATIENTLINK;
        if ("patient-unlink".equals(codeString))
          return MessageEvent.PATIENTUNLINK;
        if ("valueset-expand".equals(codeString))
          return MessageEvent.VALUESETEXPAND;
        throw new IllegalArgumentException("Unknown MessageEvent code '"+codeString+"'");
        }
    public String toCode(MessageEvent code) {
      if (code == MessageEvent.MEDICATIONADMINISTRATIONCOMPLETE)
        return "MedicationAdministration-Complete";
      if (code == MessageEvent.MEDICATIONADMINISTRATIONNULLIFICATION)
        return "MedicationAdministration-Nullification";
      if (code == MessageEvent.MEDICATIONADMINISTRATIONRECORDING)
        return "MedicationAdministration-Recording";
      if (code == MessageEvent.MEDICATIONADMINISTRATIONUPDATE)
        return "MedicationAdministration-Update";
      if (code == MessageEvent.ADMINNOTIFY)
        return "admin-notify";
      if (code == MessageEvent.DIAGNOSTICREPORTPROVIDE)
        return "diagnosticreport-provide";
      if (code == MessageEvent.OBSERVATIONPROVIDE)
        return "observation-provide";
      if (code == MessageEvent.PATIENTLINK)
        return "patient-link";
      if (code == MessageEvent.PATIENTUNLINK)
        return "patient-unlink";
      if (code == MessageEvent.VALUESETEXPAND)
        return "valueset-expand";
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
         * The processing identified errors.
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
            case ERROR: return "The processing identified errors.";
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
         * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centres, etc.
         */
        ACCOUNT, 
        /**
         * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
         */
        ALLERGYINTOLERANCE, 
        /**
         * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT, 
        /**
         * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENTRESPONSE, 
        /**
         * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
         */
        AUDITEVENT, 
        /**
         * Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.
         */
        BASIC, 
        /**
         * A binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY, 
        /**
         * Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSITE, 
        /**
         * A container for a collection of resources.
         */
        BUNDLE, 
        /**
         * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CAREPLAN, 
        /**
         * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
         */
        CLAIM, 
        /**
         * This resource provides the adjudication details from the processing of a Claim resource.
         */
        CLAIMRESPONSE, 
        /**
         * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools such as Apgar score.
         */
        CLINICALIMPRESSION, 
        /**
         * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
         */
        COMMUNICATION, 
        /**
         * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATIONREQUEST, 
        /**
         * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.
         */
        COMPOSITION, 
        /**
         * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.
         */
        CONCEPTMAP, 
        /**
         * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a diagnosis during an encounter; populating a problem list or a summary statement, such as a discharge summary.
         */
        CONDITION, 
        /**
         * A conformance statement is a set of capabilities of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        CONFORMANCE, 
        /**
         * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to pay for or reimburse health care products and services.
         */
        COVERAGE, 
        /**
         * The formal description of a single piece of information that can be gathered and reported.
         */
        DATAELEMENT, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies an instance of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices includes durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.
         */
        DEVICE, 
        /**
         * Describes the characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICECOMPONENT, 
        /**
         * Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICEMETRIC, 
        /**
         * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
         */
        DEVICEUSEREQUEST, 
        /**
         * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
         */
        DEVICEUSESTATEMENT, 
        /**
         * A record of a request for a diagnostic investigation service to be performed.
         */
        DIAGNOSTICORDER, 
        /**
         * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A manifest that defines a set of documents.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document .
         */
        DOCUMENTREFERENCE, 
        /**
         * --- Abstract Type! ---A resource that includes narrative, extensions, and contained resources.
         */
        DOMAINRESOURCE, 
        /**
         * This resource provides the insurance eligibility details from the insurer regarding a specified coverage and optionally some class of service.
         */
        ELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an Eligibility resource.
         */
        ELIGIBILITYRESPONSE, 
        /**
         * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
         */
        ENCOUNTER, 
        /**
         * This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
         */
        ENROLLMENTREQUEST, 
        /**
         * This resource provides enrollment and plan details from the processing of an Enrollment resource.
         */
        ENROLLMENTRESPONSE, 
        /**
         * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
         */
        EPISODEOFCARE, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.
         */
        FAMILYMEMBERHISTORY, 
        /**
         * Prospective warnings of potential issues when providing care to the patient.
         */
        FLAG, 
        /**
         * Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
         */
        GOAL, 
        /**
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
         */
        GROUP, 
        /**
         * The details of a healthcare service available at a location.
         */
        HEALTHCARESERVICE, 
        /**
         * A manifest of a set of DICOM Service-Object Pair Instances (SOP Instances).  The referenced SOP Instances (images or other content) are for a single patient, and may be from one or more studies. The referenced SOP Instances have been selected for a purpose, such as quality assurance, conference, or consult. Reflecting that range of purposes, typical ImagingObjectSelection resources may include all SOP Instances in a study (perhaps for sharing through a Health Information Exchange); key images from multiple studies (for reference by a referring or treating physician); a multi-frame ultrasound instance ("cine" video clip) and a set of measurements taken from that instance (for inclusion in a teaching file); and so on.
         */
        IMAGINGOBJECTSELECTION, 
        /**
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.
         */
        IMMUNIZATION, 
        /**
         * A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules or how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole, and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * A set of information summarized from a list of other resources.
         */
        LIST, 
        /**
         * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
         */
        LOCATION, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.
         */
        MEDICATION, 
        /**
         * Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
         */
        MEDICATIONADMINISTRATION, 
        /**
         * Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.
         */
        MEDICATIONDISPENSE, 
        /**
         * An order for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationOrder" rather than "MedicationPrescription" to generalize the use across inpatient and outpatient settings as well as for care plans, etc.
         */
        MEDICATIONORDER, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from e.g. the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
         */
        MESSAGEHEADER, 
        /**
         * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
         */
        NAMINGSYSTEM, 
        /**
         * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
         */
        NUTRITIONORDER, 
        /**
         * Measurements and simple assertions made about a patient, device or other subject.
         */
        OBSERVATION, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A request to perform an action.
         */
        ORDER, 
        /**
         * A response to an order.
         */
        ORDERRESPONSE, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
         */
        ORGANIZATION, 
        /**
         * This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.
         */
        PARAMETERS, 
        /**
         * Demographics and other administrative information about an individual or animal receiving care or other health-related services.
         */
        PATIENT, 
        /**
         * This resource provides the status of the payment for goods and services rendered, and the request and response resource references.
         */
        PAYMENTNOTICE, 
        /**
         * This resource provides payment details and claim references supporting a bulk payment.
         */
        PAYMENTRECONCILIATION, 
        /**
         * Demographics and administrative information about a person independent of a specific health-related context.
         */
        PERSON, 
        /**
         * A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.
         */
        PROCEDURE, 
        /**
         * A request for a procedure to be performed. May be a proposal or an order.
         */
        PROCEDUREREQUEST, 
        /**
         * This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.
         */
        PROCESSREQUEST, 
        /**
         * This resource provides processing status, errors and notes from the processing of a resource.
         */
        PROCESSRESPONSE, 
        /**
         * Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.
         */
        PROVENANCE, 
        /**
         * A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
         */
        QUESTIONNAIRE, 
        /**
         * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.
         */
        REFERRALREQUEST, 
        /**
         * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * --- Abstract Type! ---This is the base resource type for everything.
         */
        RESOURCE, 
        /**
         * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISKASSESSMENT, 
        /**
         * A container for slot(s) of time that may be available for booking appointments.
         */
        SCHEDULE, 
        /**
         * A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCHPARAMETER, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions, and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system is able to take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Record of delivery of what is supplied.
         */
        SUPPLYDELIVERY, 
        /**
         * A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLYREQUEST, 
        /**
         * TestScript is a resource that specifies a suite of tests against a FHIR server implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * A value set specifies a set of codes drawn from one or more code systems.
         */
        VALUESET, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Account".equals(codeString))
          return ACCOUNT;
        if ("AllergyIntolerance".equals(codeString))
          return ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return AUDITEVENT;
        if ("Basic".equals(codeString))
          return BASIC;
        if ("Binary".equals(codeString))
          return BINARY;
        if ("BodySite".equals(codeString))
          return BODYSITE;
        if ("Bundle".equals(codeString))
          return BUNDLE;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Conformance".equals(codeString))
          return CONFORMANCE;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("DataElement".equals(codeString))
          return DATAELEMENT;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceComponent".equals(codeString))
          return DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return DEVICEMETRIC;
        if ("DeviceUseRequest".equals(codeString))
          return DEVICEUSEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticOrder".equals(codeString))
          return DIAGNOSTICORDER;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FLAG;
        if ("Goal".equals(codeString))
          return GOAL;
        if ("Group".equals(codeString))
          return GROUP;
        if ("HealthcareService".equals(codeString))
          return HEALTHCARESERVICE;
        if ("ImagingObjectSelection".equals(codeString))
          return IMAGINGOBJECTSELECTION;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("List".equals(codeString))
          return LIST;
        if ("Location".equals(codeString))
          return LOCATION;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("Medication".equals(codeString))
          return MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationOrder".equals(codeString))
          return MEDICATIONORDER;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MessageHeader".equals(codeString))
          return MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Order".equals(codeString))
          return ORDER;
        if ("OrderResponse".equals(codeString))
          return ORDERRESPONSE;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("Parameters".equals(codeString))
          return PARAMETERS;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("PaymentNotice".equals(codeString))
          return PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return PERSON;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
        if ("ProcedureRequest".equals(codeString))
          return PROCEDUREREQUEST;
        if ("ProcessRequest".equals(codeString))
          return PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return QUESTIONNAIRERESPONSE;
        if ("ReferralRequest".equals(codeString))
          return REFERRALREQUEST;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("Resource".equals(codeString))
          return RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return SEARCHPARAMETER;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        throw new Exception("Unknown ResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCOUNT: return "Account";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BODYSITE: return "BodySite";
            case BUNDLE: return "Bundle";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONFORMANCE: return "Conformance";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case DATAELEMENT: return "DataElement";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICECOMPONENT: return "DeviceComponent";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEUSEREQUEST: return "DeviceUseRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICORDER: return "DiagnosticOrder";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ELIGIBILITYREQUEST: return "EligibilityRequest";
            case ELIGIBILITYRESPONSE: return "EligibilityResponse";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GROUP: return "Group";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGOBJECTSELECTION: return "ImagingObjectSelection";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONORDER: return "MedicationOrder";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORDER: return "Order";
            case ORDERRESPONSE: return "OrderResponse";
            case ORGANIZATION: return "Organization";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case PROCEDURE: return "Procedure";
            case PROCEDUREREQUEST: return "ProcedureRequest";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case REFERRALREQUEST: return "ReferralRequest";
            case RELATEDPERSON: return "RelatedPerson";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TESTSCRIPT: return "TestScript";
            case VALUESET: return "ValueSet";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACCOUNT: return "http://hl7.org/fhir/resource-types";
            case ALLERGYINTOLERANCE: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENT: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case AUDITEVENT: return "http://hl7.org/fhir/resource-types";
            case BASIC: return "http://hl7.org/fhir/resource-types";
            case BINARY: return "http://hl7.org/fhir/resource-types";
            case BODYSITE: return "http://hl7.org/fhir/resource-types";
            case BUNDLE: return "http://hl7.org/fhir/resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/resource-types";
            case CLAIM: return "http://hl7.org/fhir/resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/resource-types";
            case CONDITION: return "http://hl7.org/fhir/resource-types";
            case CONFORMANCE: return "http://hl7.org/fhir/resource-types";
            case CONTRACT: return "http://hl7.org/fhir/resource-types";
            case COVERAGE: return "http://hl7.org/fhir/resource-types";
            case DATAELEMENT: return "http://hl7.org/fhir/resource-types";
            case DETECTEDISSUE: return "http://hl7.org/fhir/resource-types";
            case DEVICE: return "http://hl7.org/fhir/resource-types";
            case DEVICECOMPONENT: return "http://hl7.org/fhir/resource-types";
            case DEVICEMETRIC: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSEREQUEST: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICORDER: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/resource-types";
            case DOMAINRESOURCE: return "http://hl7.org/fhir/resource-types";
            case ELIGIBILITYREQUEST: return "http://hl7.org/fhir/resource-types";
            case ELIGIBILITYRESPONSE: return "http://hl7.org/fhir/resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGOBJECTSELECTION: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case LIST: return "http://hl7.org/fhir/resource-types";
            case LOCATION: return "http://hl7.org/fhir/resource-types";
            case MEDIA: return "http://hl7.org/fhir/resource-types";
            case MEDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONORDER: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORDER: return "http://hl7.org/fhir/resource-types";
            case ORDERRESPONSE: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCEDUREREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case REFERRALREQUEST: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACCOUNT: return "A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centres, etc.";
            case ALLERGYINTOLERANCE: return "Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.";
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case AUDITEVENT: return "A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.";
            case BASIC: return "Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.";
            case BINARY: return "A binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BODYSITE: return "Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.";
            case CONDITION: return "Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a diagnosis during an encounter; populating a problem list or a summary statement, such as a discharge summary.";
            case CONFORMANCE: return "A conformance statement is a set of capabilities of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CONTRACT: return "A formal agreement between parties regarding the conduct of business, exchange of information or other matters.";
            case COVERAGE: return "Financial instrument which may be used to pay for or reimburse health care products and services.";
            case DATAELEMENT: return "The formal description of a single piece of information that can be gathered and reported.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies an instance of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices includes durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.";
            case DEVICECOMPONENT: return "Describes the characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEUSEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICORDER: return "A record of a request for a diagnostic investigation service to be performed.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A manifest that defines a set of documents.";
            case DOCUMENTREFERENCE: return "A reference to a document .";
            case DOMAINRESOURCE: return "--- Abstract Type! ---A resource that includes narrative, extensions, and contained resources.";
            case ELIGIBILITYREQUEST: return "This resource provides the insurance eligibility details from the insurer regarding a specified coverage and optionally some class of service.";
            case ELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an Eligibility resource.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGOBJECTSELECTION: return "A manifest of a set of DICOM Service-Object Pair Instances (SOP Instances).  The referenced SOP Instances (images or other content) are for a single patient, and may be from one or more studies. The referenced SOP Instances have been selected for a purpose, such as quality assurance, conference, or consult. Reflecting that range of purposes, typical ImagingObjectSelection resources may include all SOP Instances in a study (perhaps for sharing through a Health Information Exchange); key images from multiple studies (for reference by a referring or treating physician); a multi-frame ultrasound instance (\"cine\" video clip) and a set of measurements taken from that instance (for inclusion in a teaching file); and so on.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules or how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole, and to publish a computable definition of all the parts.";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONORDER: return "An order for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationOrder\" rather than \"MedicationPrescription\" to generalize the use across inpatient and outpatient settings as well as for care plans, etc.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from e.g. the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORDER: return "A request to perform an action.";
            case ORDERRESPONSE: return "A response to an order.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PROCEDURE: return "An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.";
            case PROCEDUREREQUEST: return "A request for a procedure to be performed. May be a proposal or an order.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.";
            case REFERRALREQUEST: return "Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case RESOURCE: return "--- Abstract Type! ---This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slot(s) of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions, and constraints on resources and data types.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system is able to take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TESTSCRIPT: return "TestScript is a resource that specifies a suite of tests against a FHIR server implementation to determine compliance against the FHIR specification.";
            case VALUESET: return "A value set specifies a set of codes drawn from one or more code systems.";
            case VISIONPRESCRIPTION: return "An authorization for the supply of glasses and/or contact lenses to a patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCOUNT: return "Account";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BODYSITE: return "BodySite";
            case BUNDLE: return "Bundle";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONFORMANCE: return "Conformance";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case DATAELEMENT: return "DataElement";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICECOMPONENT: return "DeviceComponent";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEUSEREQUEST: return "DeviceUseRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICORDER: return "DiagnosticOrder";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ELIGIBILITYREQUEST: return "EligibilityRequest";
            case ELIGIBILITYRESPONSE: return "EligibilityResponse";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GROUP: return "Group";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGOBJECTSELECTION: return "ImagingObjectSelection";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONORDER: return "MedicationOrder";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORDER: return "Order";
            case ORDERRESPONSE: return "OrderResponse";
            case ORGANIZATION: return "Organization";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case PROCEDURE: return "Procedure";
            case PROCEDUREREQUEST: return "ProcedureRequest";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case REFERRALREQUEST: return "ReferralRequest";
            case RELATEDPERSON: return "RelatedPerson";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TESTSCRIPT: return "TestScript";
            case VALUESET: return "ValueSet";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
    }

  public static class ResourceTypeEnumFactory implements EnumFactory<ResourceType> {
    public ResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Account".equals(codeString))
          return ResourceType.ACCOUNT;
        if ("AllergyIntolerance".equals(codeString))
          return ResourceType.ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return ResourceType.APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return ResourceType.APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return ResourceType.AUDITEVENT;
        if ("Basic".equals(codeString))
          return ResourceType.BASIC;
        if ("Binary".equals(codeString))
          return ResourceType.BINARY;
        if ("BodySite".equals(codeString))
          return ResourceType.BODYSITE;
        if ("Bundle".equals(codeString))
          return ResourceType.BUNDLE;
        if ("CarePlan".equals(codeString))
          return ResourceType.CAREPLAN;
        if ("Claim".equals(codeString))
          return ResourceType.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return ResourceType.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return ResourceType.CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return ResourceType.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return ResourceType.COMMUNICATIONREQUEST;
        if ("Composition".equals(codeString))
          return ResourceType.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return ResourceType.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return ResourceType.CONDITION;
        if ("Conformance".equals(codeString))
          return ResourceType.CONFORMANCE;
        if ("Contract".equals(codeString))
          return ResourceType.CONTRACT;
        if ("Coverage".equals(codeString))
          return ResourceType.COVERAGE;
        if ("DataElement".equals(codeString))
          return ResourceType.DATAELEMENT;
        if ("DetectedIssue".equals(codeString))
          return ResourceType.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return ResourceType.DEVICE;
        if ("DeviceComponent".equals(codeString))
          return ResourceType.DEVICECOMPONENT;
        if ("DeviceMetric".equals(codeString))
          return ResourceType.DEVICEMETRIC;
        if ("DeviceUseRequest".equals(codeString))
          return ResourceType.DEVICEUSEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return ResourceType.DEVICEUSESTATEMENT;
        if ("DiagnosticOrder".equals(codeString))
          return ResourceType.DIAGNOSTICORDER;
        if ("DiagnosticReport".equals(codeString))
          return ResourceType.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return ResourceType.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return ResourceType.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return ResourceType.DOMAINRESOURCE;
        if ("EligibilityRequest".equals(codeString))
          return ResourceType.ELIGIBILITYREQUEST;
        if ("EligibilityResponse".equals(codeString))
          return ResourceType.ELIGIBILITYRESPONSE;
        if ("Encounter".equals(codeString))
          return ResourceType.ENCOUNTER;
        if ("EnrollmentRequest".equals(codeString))
          return ResourceType.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ResourceType.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return ResourceType.EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return ResourceType.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return ResourceType.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return ResourceType.FLAG;
        if ("Goal".equals(codeString))
          return ResourceType.GOAL;
        if ("Group".equals(codeString))
          return ResourceType.GROUP;
        if ("HealthcareService".equals(codeString))
          return ResourceType.HEALTHCARESERVICE;
        if ("ImagingObjectSelection".equals(codeString))
          return ResourceType.IMAGINGOBJECTSELECTION;
        if ("ImagingStudy".equals(codeString))
          return ResourceType.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return ResourceType.IMMUNIZATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return ResourceType.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return ResourceType.IMPLEMENTATIONGUIDE;
        if ("List".equals(codeString))
          return ResourceType.LIST;
        if ("Location".equals(codeString))
          return ResourceType.LOCATION;
        if ("Media".equals(codeString))
          return ResourceType.MEDIA;
        if ("Medication".equals(codeString))
          return ResourceType.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return ResourceType.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return ResourceType.MEDICATIONDISPENSE;
        if ("MedicationOrder".equals(codeString))
          return ResourceType.MEDICATIONORDER;
        if ("MedicationStatement".equals(codeString))
          return ResourceType.MEDICATIONSTATEMENT;
        if ("MessageHeader".equals(codeString))
          return ResourceType.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return ResourceType.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return ResourceType.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return ResourceType.OBSERVATION;
        if ("OperationDefinition".equals(codeString))
          return ResourceType.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return ResourceType.OPERATIONOUTCOME;
        if ("Order".equals(codeString))
          return ResourceType.ORDER;
        if ("OrderResponse".equals(codeString))
          return ResourceType.ORDERRESPONSE;
        if ("Organization".equals(codeString))
          return ResourceType.ORGANIZATION;
        if ("Parameters".equals(codeString))
          return ResourceType.PARAMETERS;
        if ("Patient".equals(codeString))
          return ResourceType.PATIENT;
        if ("PaymentNotice".equals(codeString))
          return ResourceType.PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return ResourceType.PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return ResourceType.PERSON;
        if ("Practitioner".equals(codeString))
          return ResourceType.PRACTITIONER;
        if ("Procedure".equals(codeString))
          return ResourceType.PROCEDURE;
        if ("ProcedureRequest".equals(codeString))
          return ResourceType.PROCEDUREREQUEST;
        if ("ProcessRequest".equals(codeString))
          return ResourceType.PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return ResourceType.PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return ResourceType.PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return ResourceType.QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return ResourceType.QUESTIONNAIRERESPONSE;
        if ("ReferralRequest".equals(codeString))
          return ResourceType.REFERRALREQUEST;
        if ("RelatedPerson".equals(codeString))
          return ResourceType.RELATEDPERSON;
        if ("Resource".equals(codeString))
          return ResourceType.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return ResourceType.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return ResourceType.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return ResourceType.SEARCHPARAMETER;
        if ("Slot".equals(codeString))
          return ResourceType.SLOT;
        if ("Specimen".equals(codeString))
          return ResourceType.SPECIMEN;
        if ("StructureDefinition".equals(codeString))
          return ResourceType.STRUCTUREDEFINITION;
        if ("Subscription".equals(codeString))
          return ResourceType.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return ResourceType.SUBSTANCE;
        if ("SupplyDelivery".equals(codeString))
          return ResourceType.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return ResourceType.SUPPLYREQUEST;
        if ("TestScript".equals(codeString))
          return ResourceType.TESTSCRIPT;
        if ("ValueSet".equals(codeString))
          return ResourceType.VALUESET;
        if ("VisionPrescription".equals(codeString))
          return ResourceType.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown ResourceType code '"+codeString+"'");
        }
    public String toCode(ResourceType code) {
      if (code == ResourceType.ACCOUNT)
        return "Account";
      if (code == ResourceType.ALLERGYINTOLERANCE)
        return "AllergyIntolerance";
      if (code == ResourceType.APPOINTMENT)
        return "Appointment";
      if (code == ResourceType.APPOINTMENTRESPONSE)
        return "AppointmentResponse";
      if (code == ResourceType.AUDITEVENT)
        return "AuditEvent";
      if (code == ResourceType.BASIC)
        return "Basic";
      if (code == ResourceType.BINARY)
        return "Binary";
      if (code == ResourceType.BODYSITE)
        return "BodySite";
      if (code == ResourceType.BUNDLE)
        return "Bundle";
      if (code == ResourceType.CAREPLAN)
        return "CarePlan";
      if (code == ResourceType.CLAIM)
        return "Claim";
      if (code == ResourceType.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == ResourceType.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == ResourceType.COMMUNICATION)
        return "Communication";
      if (code == ResourceType.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == ResourceType.COMPOSITION)
        return "Composition";
      if (code == ResourceType.CONCEPTMAP)
        return "ConceptMap";
      if (code == ResourceType.CONDITION)
        return "Condition";
      if (code == ResourceType.CONFORMANCE)
        return "Conformance";
      if (code == ResourceType.CONTRACT)
        return "Contract";
      if (code == ResourceType.COVERAGE)
        return "Coverage";
      if (code == ResourceType.DATAELEMENT)
        return "DataElement";
      if (code == ResourceType.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == ResourceType.DEVICE)
        return "Device";
      if (code == ResourceType.DEVICECOMPONENT)
        return "DeviceComponent";
      if (code == ResourceType.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == ResourceType.DEVICEUSEREQUEST)
        return "DeviceUseRequest";
      if (code == ResourceType.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == ResourceType.DIAGNOSTICORDER)
        return "DiagnosticOrder";
      if (code == ResourceType.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == ResourceType.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == ResourceType.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == ResourceType.DOMAINRESOURCE)
        return "DomainResource";
      if (code == ResourceType.ELIGIBILITYREQUEST)
        return "EligibilityRequest";
      if (code == ResourceType.ELIGIBILITYRESPONSE)
        return "EligibilityResponse";
      if (code == ResourceType.ENCOUNTER)
        return "Encounter";
      if (code == ResourceType.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == ResourceType.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == ResourceType.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == ResourceType.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == ResourceType.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == ResourceType.FLAG)
        return "Flag";
      if (code == ResourceType.GOAL)
        return "Goal";
      if (code == ResourceType.GROUP)
        return "Group";
      if (code == ResourceType.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == ResourceType.IMAGINGOBJECTSELECTION)
        return "ImagingObjectSelection";
      if (code == ResourceType.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == ResourceType.IMMUNIZATION)
        return "Immunization";
      if (code == ResourceType.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == ResourceType.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == ResourceType.LIST)
        return "List";
      if (code == ResourceType.LOCATION)
        return "Location";
      if (code == ResourceType.MEDIA)
        return "Media";
      if (code == ResourceType.MEDICATION)
        return "Medication";
      if (code == ResourceType.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == ResourceType.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == ResourceType.MEDICATIONORDER)
        return "MedicationOrder";
      if (code == ResourceType.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == ResourceType.MESSAGEHEADER)
        return "MessageHeader";
      if (code == ResourceType.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == ResourceType.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == ResourceType.OBSERVATION)
        return "Observation";
      if (code == ResourceType.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == ResourceType.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == ResourceType.ORDER)
        return "Order";
      if (code == ResourceType.ORDERRESPONSE)
        return "OrderResponse";
      if (code == ResourceType.ORGANIZATION)
        return "Organization";
      if (code == ResourceType.PARAMETERS)
        return "Parameters";
      if (code == ResourceType.PATIENT)
        return "Patient";
      if (code == ResourceType.PAYMENTNOTICE)
        return "PaymentNotice";
      if (code == ResourceType.PAYMENTRECONCILIATION)
        return "PaymentReconciliation";
      if (code == ResourceType.PERSON)
        return "Person";
      if (code == ResourceType.PRACTITIONER)
        return "Practitioner";
      if (code == ResourceType.PROCEDURE)
        return "Procedure";
      if (code == ResourceType.PROCEDUREREQUEST)
        return "ProcedureRequest";
      if (code == ResourceType.PROCESSREQUEST)
        return "ProcessRequest";
      if (code == ResourceType.PROCESSRESPONSE)
        return "ProcessResponse";
      if (code == ResourceType.PROVENANCE)
        return "Provenance";
      if (code == ResourceType.QUESTIONNAIRE)
        return "Questionnaire";
      if (code == ResourceType.QUESTIONNAIRERESPONSE)
        return "QuestionnaireResponse";
      if (code == ResourceType.REFERRALREQUEST)
        return "ReferralRequest";
      if (code == ResourceType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == ResourceType.RESOURCE)
        return "Resource";
      if (code == ResourceType.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == ResourceType.SCHEDULE)
        return "Schedule";
      if (code == ResourceType.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == ResourceType.SLOT)
        return "Slot";
      if (code == ResourceType.SPECIMEN)
        return "Specimen";
      if (code == ResourceType.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == ResourceType.SUBSCRIPTION)
        return "Subscription";
      if (code == ResourceType.SUBSTANCE)
        return "Substance";
      if (code == ResourceType.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == ResourceType.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == ResourceType.TESTSCRIPT)
        return "TestScript";
      if (code == ResourceType.VALUESET)
        return "ValueSet";
      if (code == ResourceType.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    }

    public enum SearchParamType {
        /**
         * Search parameter SHALL be a number (a whole number, or a decimal).
         */
        NUMBER, 
        /**
         * Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.
         */
        DATE, 
        /**
         * Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.
         */
        STRING, 
        /**
         * Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "|", depending on the modifier used.
         */
        TOKEN, 
        /**
         * A reference to another resource.
         */
        REFERENCE, 
        /**
         * A composite search parameter that combines a search on two values together.
         */
        COMPOSITE, 
        /**
         * A search parameter that searches on a quantity.
         */
        QUANTITY, 
        /**
         * A search parameter that searches on a URI (RFC 3986).
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
            case NUMBER: return "Search parameter SHALL be a number (a whole number, or a decimal).";
            case DATE: return "Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.";
            case STRING: return "Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.";
            case TOKEN: return "Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a \"|\", depending on the modifier used.";
            case REFERENCE: return "A reference to another resource.";
            case COMPOSITE: return "A composite search parameter that combines a search on two values together.";
            case QUANTITY: return "A search parameter that searches on a quantity.";
            case URI: return "A search parameter that searches on a URI (RFC 3986).";
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
         * Boolean true.
         */
        TRUE, 
        /**
         * Boolean false.
         */
        FALSE, 
        /**
         * The content is greater than zero, but too small to be quantified.
         */
        TRACE, 
        /**
         * The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material.
         */
        SUFFICIENT, 
        /**
         * The value is no longer available.
         */
        WITHDRAWN, 
        /**
         * The are no known applicable values in this context.
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
            case TRUE: return "Boolean true.";
            case FALSE: return "Boolean false.";
            case TRACE: return "The content is greater than zero, but too small to be quantified.";
            case SUFFICIENT: return "The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material.";
            case WITHDRAWN: return "The value is no longer available.";
            case NILKNOWN: return "The are no known applicable values in this context.";
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

