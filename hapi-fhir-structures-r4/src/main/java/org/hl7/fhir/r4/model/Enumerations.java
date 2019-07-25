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


import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;

public class Enumerations {

// In here: 
//   AbstractType: A list of the base types defined by this version of the FHIR specification - types that are defined, but for which only specializations actually are created.
//   AdministrativeGender: The gender of a person used for administrative purposes.
//   AgeUnits: A valueSet of UCUM codes for representing age value units.
//   BindingStrength: Indication of the degree of conformance expectations associated with a binding.
//   ConceptMapEquivalence: The degree of equivalence between concepts.
//   DataAbsentReason: Used to specify why the normally expected content of the data element is missing.
//   DataType: A version specific list of the data types defined by the FHIR specification for use as an element  type (any of the FHIR defined data types).
//   DefinitionResourceType: A list of all the definition resource types defined in this version of the FHIR specification.
//   DocumentReferenceStatus: The status of the document reference.
//   EventResourceType: A list of all the event resource types defined in this version of the FHIR specification.
//   FHIRAllTypes: A list of all the concrete types defined in this version of the FHIR specification - Abstract Types, Data Types and Resource Types.
//   FHIRDefinedType: A list of all the concrete types defined in this version of the FHIR specification - Data Types and Resource Types.
//   KnowledgeResourceType: A list of all the knowledge resource types defined in this version of the FHIR specification.
//   MessageEvent: One of the message events defined as part of this version of FHIR.
//   NoteType: The presentation types of notes.
//   PublicationStatus: The lifecycle status of an artifact.
//   RemittanceOutcome: The outcome of the processing.
//   RequestResourceType: A list of all the request resource types defined in this version of the FHIR specification.
//   ResourceType: One of the resource types defined as part of this version of FHIR.
//   SearchParamType: Data types allowed to be used for search parameters.
//   SpecialValues: A set of generally useful codes defined so they can be included in value sets.


    public enum AbstractType {
        /**
         * A place holder that means any kind of data type
         */
        TYPE, 
        /**
         * A place holder that means any kind of resource
         */
        ANY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AbstractType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Type".equals(codeString))
          return TYPE;
        if ("Any".equals(codeString))
          return ANY;
        throw new FHIRException("Unknown AbstractType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TYPE: return "Type";
            case ANY: return "Any";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case TYPE: return "http://hl7.org/fhir/abstract-types";
            case ANY: return "http://hl7.org/fhir/abstract-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case TYPE: return "A place holder that means any kind of data type";
            case ANY: return "A place holder that means any kind of resource";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TYPE: return "Type";
            case ANY: return "Any";
            default: return "?";
          }
        }
    }

  public static class AbstractTypeEnumFactory implements EnumFactory<AbstractType> {
    public AbstractType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Type".equals(codeString))
          return AbstractType.TYPE;
        if ("Any".equals(codeString))
          return AbstractType.ANY;
        throw new IllegalArgumentException("Unknown AbstractType code '"+codeString+"'");
        }
        public Enumeration<AbstractType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AbstractType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Type".equals(codeString))
          return new Enumeration<AbstractType>(this, AbstractType.TYPE);
        if ("Any".equals(codeString))
          return new Enumeration<AbstractType>(this, AbstractType.ANY);
        throw new FHIRException("Unknown AbstractType code '"+codeString+"'");
        }
    public String toCode(AbstractType code) {
      if (code == AbstractType.TYPE)
        return "Type";
      if (code == AbstractType.ANY)
        return "Any";
      return "?";
      }
    public String toSystem(AbstractType code) {
      return code.getSystem();
      }
    }

    public enum AdministrativeGender {
        /**
         * Male.
         */
        MALE, 
        /**
         * Female.
         */
        FEMALE, 
        /**
         * Other.
         */
        OTHER, 
        /**
         * Unknown.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AdministrativeGender fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown AdministrativeGender code '"+codeString+"'");
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
            case MALE: return "Male.";
            case FEMALE: return "Female.";
            case OTHER: return "Other.";
            case UNKNOWN: return "Unknown.";
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
        public Enumeration<AdministrativeGender> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AdministrativeGender>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("male".equals(codeString))
          return new Enumeration<AdministrativeGender>(this, AdministrativeGender.MALE);
        if ("female".equals(codeString))
          return new Enumeration<AdministrativeGender>(this, AdministrativeGender.FEMALE);
        if ("other".equals(codeString))
          return new Enumeration<AdministrativeGender>(this, AdministrativeGender.OTHER);
        if ("unknown".equals(codeString))
          return new Enumeration<AdministrativeGender>(this, AdministrativeGender.UNKNOWN);
        throw new FHIRException("Unknown AdministrativeGender code '"+codeString+"'");
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
    public String toSystem(AdministrativeGender code) {
      return code.getSystem();
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
        public static AgeUnits fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown AgeUnits code '"+codeString+"'");
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
        public Enumeration<AgeUnits> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AgeUnits>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("min".equals(codeString))
          return new Enumeration<AgeUnits>(this, AgeUnits.MIN);
        if ("h".equals(codeString))
          return new Enumeration<AgeUnits>(this, AgeUnits.H);
        if ("d".equals(codeString))
          return new Enumeration<AgeUnits>(this, AgeUnits.D);
        if ("wk".equals(codeString))
          return new Enumeration<AgeUnits>(this, AgeUnits.WK);
        if ("mo".equals(codeString))
          return new Enumeration<AgeUnits>(this, AgeUnits.MO);
        if ("a".equals(codeString))
          return new Enumeration<AgeUnits>(this, AgeUnits.A);
        throw new FHIRException("Unknown AgeUnits code '"+codeString+"'");
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
    public String toSystem(AgeUnits code) {
      return code.getSystem();
      }
    }

    public enum BindingStrength {
        /**
         * To be conformant, the concept in this element SHALL be from the specified value set.
         */
        REQUIRED, 
        /**
         * To be conformant, the concept in this element SHALL be from the specified value set if any of the codes within the value set can apply to the concept being communicated.  If the value set does not cover the concept (based on human review), alternate codings (or, data type allowing, text) may be included instead.
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
        public static BindingStrength fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown BindingStrength code '"+codeString+"'");
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
            case REQUIRED: return "To be conformant, the concept in this element SHALL be from the specified value set.";
            case EXTENSIBLE: return "To be conformant, the concept in this element SHALL be from the specified value set if any of the codes within the value set can apply to the concept being communicated.  If the value set does not cover the concept (based on human review), alternate codings (or, data type allowing, text) may be included instead.";
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
        public Enumeration<BindingStrength> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<BindingStrength>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("required".equals(codeString))
          return new Enumeration<BindingStrength>(this, BindingStrength.REQUIRED);
        if ("extensible".equals(codeString))
          return new Enumeration<BindingStrength>(this, BindingStrength.EXTENSIBLE);
        if ("preferred".equals(codeString))
          return new Enumeration<BindingStrength>(this, BindingStrength.PREFERRED);
        if ("example".equals(codeString))
          return new Enumeration<BindingStrength>(this, BindingStrength.EXAMPLE);
        throw new FHIRException("Unknown BindingStrength code '"+codeString+"'");
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
    public String toSystem(BindingStrength code) {
      return code.getSystem();
      }
    }

    public enum ConceptMapEquivalence {
        /**
         * The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is not known.
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
         * The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is inexact SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.
         */
        INEXACT, 
        /**
         * There is no match for this concept in the target code system.
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
          switch (this) {
            case RELATEDTO: return "http://hl7.org/fhir/concept-map-equivalence";
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
            case RELATEDTO: return "The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is not known.";
            case EQUIVALENT: return "The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).";
            case EQUAL: return "The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identical or irrelevant (i.e. intentionally identical).";
            case WIDER: return "The target mapping is wider in meaning than the source concept.";
            case SUBSUMES: return "The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).";
            case NARROWER: return "The target mapping is narrower in meaning than the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.";
            case SPECIALIZES: return "The target mapping specializes the meaning of the source concept (e.g. the target is-a source).";
            case INEXACT: return "The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is inexact SHALL be described in the comments in this case, and applications should be careful when attempting to use these mappings operationally.";
            case UNMATCHED: return "There is no match for this concept in the target code system.";
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

  public static class ConceptMapEquivalenceEnumFactory implements EnumFactory<ConceptMapEquivalence> {
    public ConceptMapEquivalence fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("relatedto".equals(codeString))
          return ConceptMapEquivalence.RELATEDTO;
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
        public Enumeration<ConceptMapEquivalence> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConceptMapEquivalence>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("relatedto".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.RELATEDTO);
        if ("equivalent".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.EQUIVALENT);
        if ("equal".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.EQUAL);
        if ("wider".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.WIDER);
        if ("subsumes".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.SUBSUMES);
        if ("narrower".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.NARROWER);
        if ("specializes".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.SPECIALIZES);
        if ("inexact".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.INEXACT);
        if ("unmatched".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.UNMATCHED);
        if ("disjoint".equals(codeString))
          return new Enumeration<ConceptMapEquivalence>(this, ConceptMapEquivalence.DISJOINT);
        throw new FHIRException("Unknown ConceptMapEquivalence code '"+codeString+"'");
        }
    public String toCode(ConceptMapEquivalence code) {
      if (code == ConceptMapEquivalence.RELATEDTO)
        return "relatedto";
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
    public String toSystem(ConceptMapEquivalence code) {
      return code.getSystem();
      }
    }

    public enum DataAbsentReason {
        /**
         * The value is expected to exist but is not known.
         */
        UNKNOWN, 
        /**
         * The source was asked but does not know the value.
         */
        ASKEDUNKNOWN, 
        /**
         * There is reason to expect (from the workflow) that the value may become known.
         */
        TEMPUNKNOWN, 
        /**
         * The workflow didn't lead to this value being known.
         */
        NOTASKED, 
        /**
         * The source was asked but declined to answer.
         */
        ASKEDDECLINED, 
        /**
         * The information is not available due to security, privacy or related reasons.
         */
        MASKED, 
        /**
         * There is no proper value for this element (e.g. last menstrual period for a male).
         */
        NOTAPPLICABLE, 
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
         * The numeric value is undefined or unrepresentable due to a floating point processing error.
         */
        NOTANUMBER, 
        /**
         * The numeric value is excessively low and unrepresentable due to a floating point processing error.
         */
        NEGATIVEINFINITY, 
        /**
         * The numeric value is excessively high and unrepresentable due to a floating point processing error.
         */
        POSITIVEINFINITY, 
        /**
         * The value is not available because the observation procedure (test, etc.) was not performed.
         */
        NOTPERFORMED, 
        /**
         * The value is not permitted in this context (e.g. due to profiles, or the base data types).
         */
        NOTPERMITTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataAbsentReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("asked-unknown".equals(codeString))
          return ASKEDUNKNOWN;
        if ("temp-unknown".equals(codeString))
          return TEMPUNKNOWN;
        if ("not-asked".equals(codeString))
          return NOTASKED;
        if ("asked-declined".equals(codeString))
          return ASKEDDECLINED;
        if ("masked".equals(codeString))
          return MASKED;
        if ("not-applicable".equals(codeString))
          return NOTAPPLICABLE;
        if ("unsupported".equals(codeString))
          return UNSUPPORTED;
        if ("as-text".equals(codeString))
          return ASTEXT;
        if ("error".equals(codeString))
          return ERROR;
        if ("not-a-number".equals(codeString))
          return NOTANUMBER;
        if ("negative-infinity".equals(codeString))
          return NEGATIVEINFINITY;
        if ("positive-infinity".equals(codeString))
          return POSITIVEINFINITY;
        if ("not-performed".equals(codeString))
          return NOTPERFORMED;
        if ("not-permitted".equals(codeString))
          return NOTPERMITTED;
        throw new FHIRException("Unknown DataAbsentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case ASKEDUNKNOWN: return "asked-unknown";
            case TEMPUNKNOWN: return "temp-unknown";
            case NOTASKED: return "not-asked";
            case ASKEDDECLINED: return "asked-declined";
            case MASKED: return "masked";
            case NOTAPPLICABLE: return "not-applicable";
            case UNSUPPORTED: return "unsupported";
            case ASTEXT: return "as-text";
            case ERROR: return "error";
            case NOTANUMBER: return "not-a-number";
            case NEGATIVEINFINITY: return "negative-infinity";
            case POSITIVEINFINITY: return "positive-infinity";
            case NOTPERFORMED: return "not-performed";
            case NOTPERMITTED: return "not-permitted";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNKNOWN: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case ASKEDUNKNOWN: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case TEMPUNKNOWN: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case NOTASKED: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case ASKEDDECLINED: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case MASKED: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case NOTAPPLICABLE: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case UNSUPPORTED: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case ASTEXT: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case ERROR: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case NOTANUMBER: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case NEGATIVEINFINITY: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case POSITIVEINFINITY: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case NOTPERFORMED: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            case NOTPERMITTED: return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The value is expected to exist but is not known.";
            case ASKEDUNKNOWN: return "The source was asked but does not know the value.";
            case TEMPUNKNOWN: return "There is reason to expect (from the workflow) that the value may become known.";
            case NOTASKED: return "The workflow didn't lead to this value being known.";
            case ASKEDDECLINED: return "The source was asked but declined to answer.";
            case MASKED: return "The information is not available due to security, privacy or related reasons.";
            case NOTAPPLICABLE: return "There is no proper value for this element (e.g. last menstrual period for a male).";
            case UNSUPPORTED: return "The source system wasn't capable of supporting this element.";
            case ASTEXT: return "The content of the data is represented in the resource narrative.";
            case ERROR: return "Some system or workflow process error means that the information is not available.";
            case NOTANUMBER: return "The numeric value is undefined or unrepresentable due to a floating point processing error.";
            case NEGATIVEINFINITY: return "The numeric value is excessively low and unrepresentable due to a floating point processing error.";
            case POSITIVEINFINITY: return "The numeric value is excessively high and unrepresentable due to a floating point processing error.";
            case NOTPERFORMED: return "The value is not available because the observation procedure (test, etc.) was not performed.";
            case NOTPERMITTED: return "The value is not permitted in this context (e.g. due to profiles, or the base data types).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case ASKEDUNKNOWN: return "Asked But Unknown";
            case TEMPUNKNOWN: return "Temporarily Unknown";
            case NOTASKED: return "Not Asked";
            case ASKEDDECLINED: return "Asked But Declined";
            case MASKED: return "Masked";
            case NOTAPPLICABLE: return "Not Applicable";
            case UNSUPPORTED: return "Unsupported";
            case ASTEXT: return "As Text";
            case ERROR: return "Error";
            case NOTANUMBER: return "Not a Number (NaN)";
            case NEGATIVEINFINITY: return "Negative Infinity (NINF)";
            case POSITIVEINFINITY: return "Positive Infinity (PINF)";
            case NOTPERFORMED: return "Not Performed";
            case NOTPERMITTED: return "Not Permitted";
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
        if ("asked-unknown".equals(codeString))
          return DataAbsentReason.ASKEDUNKNOWN;
        if ("temp-unknown".equals(codeString))
          return DataAbsentReason.TEMPUNKNOWN;
        if ("not-asked".equals(codeString))
          return DataAbsentReason.NOTASKED;
        if ("asked-declined".equals(codeString))
          return DataAbsentReason.ASKEDDECLINED;
        if ("masked".equals(codeString))
          return DataAbsentReason.MASKED;
        if ("not-applicable".equals(codeString))
          return DataAbsentReason.NOTAPPLICABLE;
        if ("unsupported".equals(codeString))
          return DataAbsentReason.UNSUPPORTED;
        if ("as-text".equals(codeString))
          return DataAbsentReason.ASTEXT;
        if ("error".equals(codeString))
          return DataAbsentReason.ERROR;
        if ("not-a-number".equals(codeString))
          return DataAbsentReason.NOTANUMBER;
        if ("negative-infinity".equals(codeString))
          return DataAbsentReason.NEGATIVEINFINITY;
        if ("positive-infinity".equals(codeString))
          return DataAbsentReason.POSITIVEINFINITY;
        if ("not-performed".equals(codeString))
          return DataAbsentReason.NOTPERFORMED;
        if ("not-permitted".equals(codeString))
          return DataAbsentReason.NOTPERMITTED;
        throw new IllegalArgumentException("Unknown DataAbsentReason code '"+codeString+"'");
        }
        public Enumeration<DataAbsentReason> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DataAbsentReason>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("unknown".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.UNKNOWN);
        if ("asked-unknown".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.ASKEDUNKNOWN);
        if ("temp-unknown".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.TEMPUNKNOWN);
        if ("not-asked".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.NOTASKED);
        if ("asked-declined".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.ASKEDDECLINED);
        if ("masked".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.MASKED);
        if ("not-applicable".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.NOTAPPLICABLE);
        if ("unsupported".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.UNSUPPORTED);
        if ("as-text".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.ASTEXT);
        if ("error".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.ERROR);
        if ("not-a-number".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.NOTANUMBER);
        if ("negative-infinity".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.NEGATIVEINFINITY);
        if ("positive-infinity".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.POSITIVEINFINITY);
        if ("not-performed".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.NOTPERFORMED);
        if ("not-permitted".equals(codeString))
          return new Enumeration<DataAbsentReason>(this, DataAbsentReason.NOTPERMITTED);
        throw new FHIRException("Unknown DataAbsentReason code '"+codeString+"'");
        }
    public String toCode(DataAbsentReason code) {
      if (code == DataAbsentReason.UNKNOWN)
        return "unknown";
      if (code == DataAbsentReason.ASKEDUNKNOWN)
        return "asked-unknown";
      if (code == DataAbsentReason.TEMPUNKNOWN)
        return "temp-unknown";
      if (code == DataAbsentReason.NOTASKED)
        return "not-asked";
      if (code == DataAbsentReason.ASKEDDECLINED)
        return "asked-declined";
      if (code == DataAbsentReason.MASKED)
        return "masked";
      if (code == DataAbsentReason.NOTAPPLICABLE)
        return "not-applicable";
      if (code == DataAbsentReason.UNSUPPORTED)
        return "unsupported";
      if (code == DataAbsentReason.ASTEXT)
        return "as-text";
      if (code == DataAbsentReason.ERROR)
        return "error";
      if (code == DataAbsentReason.NOTANUMBER)
        return "not-a-number";
      if (code == DataAbsentReason.NEGATIVEINFINITY)
        return "negative-infinity";
      if (code == DataAbsentReason.POSITIVEINFINITY)
        return "positive-infinity";
      if (code == DataAbsentReason.NOTPERFORMED)
        return "not-performed";
      if (code == DataAbsentReason.NOTPERMITTED)
        return "not-permitted";
      return "?";
      }
    public String toSystem(DataAbsentReason code) {
      return code.getSystem();
      }
    }

    public enum DataType {
        /**
         * An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.
         */
        ADDRESS, 
        /**
         * A duration of time during which an organism (or a process) has existed.
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
         * Specifies contact information for a person or organization.
         */
        CONTACTDETAIL, 
        /**
         * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
         */
        CONTACTPOINT, 
        /**
         * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
         */
        CONTRIBUTOR, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        COUNT, 
        /**
         * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.
         */
        DATAREQUIREMENT, 
        /**
         * A length - a value with a unit that is a physical distance.
         */
        DISTANCE, 
        /**
         * Indicates how the medication is/was taken or should be taken by the patient.
         */
        DOSAGE, 
        /**
         * A length of time.
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
         * A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.
         */
        EXPRESSION, 
        /**
         * Optional Extension Element - found in all resources.
         */
        EXTENSION, 
        /**
         * A human's name with the ability to identify parts and usage.
         */
        HUMANNAME, 
        /**
         * An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
         */
        IDENTIFIER, 
        /**
         * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
         */
        MARKETINGSTATUS, 
        /**
         * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.
         */
        META, 
        /**
         * An amount of economic utility in some recognized currency.
         */
        MONEY, 
        /**
         * null
         */
        MONEYQUANTITY, 
        /**
         * A human-readable formatted text, including images.
         */
        NARRATIVE, 
        /**
         * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
         */
        PARAMETERDEFINITION, 
        /**
         * A time period defined by a start and end date and optionally time.
         */
        PERIOD, 
        /**
         * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
         */
        PRODCHARACTERISTIC, 
        /**
         * The shelf-life and storage information for a medicinal product item or container can be described using this class.
         */
        PRODUCTSHELFLIFE, 
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
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         */
        RELATEDARTIFACT, 
        /**
         * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
         */
        SAMPLEDDATA, 
        /**
         * A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
         */
        SIGNATURE, 
        /**
         * null
         */
        SIMPLEQUANTITY, 
        /**
         * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
         */
        SUBSTANCEAMOUNT, 
        /**
         * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
         */
        SUBSTANCEMOIETY, 
        /**
         * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.
         */
        TIMING, 
        /**
         * A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.
         */
        TRIGGERDEFINITION, 
        /**
         * Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).
         */
        USAGECONTEXT, 
        /**
         * A stream of bytes
         */
        BASE64BINARY, 
        /**
         * Value of "true" or "false"
         */
        BOOLEAN, 
        /**
         * A URI that is a reference to a canonical URI on a FHIR resource
         */
        CANONICAL, 
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
         * A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN, 
        /**
         * An OID represented as a URI
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
         * A URI that is a literal reference
         */
        URL, 
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
        public static DataType fromCode(String codeString) throws FHIRException {
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
        if ("ContactDetail".equals(codeString))
          return CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return CONTRIBUTOR;
        if ("Count".equals(codeString))
          return COUNT;
        if ("DataRequirement".equals(codeString))
          return DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return DISTANCE;
        if ("Dosage".equals(codeString))
          return DOSAGE;
        if ("Duration".equals(codeString))
          return DURATION;
        if ("Element".equals(codeString))
          return ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return ELEMENTDEFINITION;
        if ("Expression".equals(codeString))
          return EXPRESSION;
        if ("Extension".equals(codeString))
          return EXTENSION;
        if ("HumanName".equals(codeString))
          return HUMANNAME;
        if ("Identifier".equals(codeString))
          return IDENTIFIER;
        if ("MarketingStatus".equals(codeString))
          return MARKETINGSTATUS;
        if ("Meta".equals(codeString))
          return META;
        if ("Money".equals(codeString))
          return MONEY;
        if ("MoneyQuantity".equals(codeString))
          return MONEYQUANTITY;
        if ("Narrative".equals(codeString))
          return NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return PERIOD;
        if ("ProdCharacteristic".equals(codeString))
          return PRODCHARACTERISTIC;
        if ("ProductShelfLife".equals(codeString))
          return PRODUCTSHELFLIFE;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("Reference".equals(codeString))
          return REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return SIMPLEQUANTITY;
        if ("SubstanceAmount".equals(codeString))
          return SUBSTANCEAMOUNT;
        if ("SubstanceMoiety".equals(codeString))
          return SUBSTANCEMOIETY;
        if ("Timing".equals(codeString))
          return TIMING;
        if ("TriggerDefinition".equals(codeString))
          return TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return BASE64BINARY;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("canonical".equals(codeString))
          return CANONICAL;
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
        if ("url".equals(codeString))
          return URL;
        if ("uuid".equals(codeString))
          return UUID;
        if ("xhtml".equals(codeString))
          return XHTML;
        throw new FHIRException("Unknown DataType code '"+codeString+"'");
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
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXPRESSION: return "Expression";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case MARKETINGSTATUS: return "MarketingStatus";
            case META: return "Meta";
            case MONEY: return "Money";
            case MONEYQUANTITY: return "MoneyQuantity";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case PRODCHARACTERISTIC: return "ProdCharacteristic";
            case PRODUCTSHELFLIFE: return "ProductShelfLife";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case SUBSTANCEAMOUNT: return "SubstanceAmount";
            case SUBSTANCEMOIETY: return "SubstanceMoiety";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CANONICAL: return "canonical";
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
            case URL: return "url";
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
            case CONTACTDETAIL: return "http://hl7.org/fhir/data-types";
            case CONTACTPOINT: return "http://hl7.org/fhir/data-types";
            case CONTRIBUTOR: return "http://hl7.org/fhir/data-types";
            case COUNT: return "http://hl7.org/fhir/data-types";
            case DATAREQUIREMENT: return "http://hl7.org/fhir/data-types";
            case DISTANCE: return "http://hl7.org/fhir/data-types";
            case DOSAGE: return "http://hl7.org/fhir/data-types";
            case DURATION: return "http://hl7.org/fhir/data-types";
            case ELEMENT: return "http://hl7.org/fhir/data-types";
            case ELEMENTDEFINITION: return "http://hl7.org/fhir/data-types";
            case EXPRESSION: return "http://hl7.org/fhir/data-types";
            case EXTENSION: return "http://hl7.org/fhir/data-types";
            case HUMANNAME: return "http://hl7.org/fhir/data-types";
            case IDENTIFIER: return "http://hl7.org/fhir/data-types";
            case MARKETINGSTATUS: return "http://hl7.org/fhir/data-types";
            case META: return "http://hl7.org/fhir/data-types";
            case MONEY: return "http://hl7.org/fhir/data-types";
            case MONEYQUANTITY: return "http://hl7.org/fhir/data-types";
            case NARRATIVE: return "http://hl7.org/fhir/data-types";
            case PARAMETERDEFINITION: return "http://hl7.org/fhir/data-types";
            case PERIOD: return "http://hl7.org/fhir/data-types";
            case PRODCHARACTERISTIC: return "http://hl7.org/fhir/data-types";
            case PRODUCTSHELFLIFE: return "http://hl7.org/fhir/data-types";
            case QUANTITY: return "http://hl7.org/fhir/data-types";
            case RANGE: return "http://hl7.org/fhir/data-types";
            case RATIO: return "http://hl7.org/fhir/data-types";
            case REFERENCE: return "http://hl7.org/fhir/data-types";
            case RELATEDARTIFACT: return "http://hl7.org/fhir/data-types";
            case SAMPLEDDATA: return "http://hl7.org/fhir/data-types";
            case SIGNATURE: return "http://hl7.org/fhir/data-types";
            case SIMPLEQUANTITY: return "http://hl7.org/fhir/data-types";
            case SUBSTANCEAMOUNT: return "http://hl7.org/fhir/data-types";
            case SUBSTANCEMOIETY: return "http://hl7.org/fhir/data-types";
            case TIMING: return "http://hl7.org/fhir/data-types";
            case TRIGGERDEFINITION: return "http://hl7.org/fhir/data-types";
            case USAGECONTEXT: return "http://hl7.org/fhir/data-types";
            case BASE64BINARY: return "http://hl7.org/fhir/data-types";
            case BOOLEAN: return "http://hl7.org/fhir/data-types";
            case CANONICAL: return "http://hl7.org/fhir/data-types";
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
            case URL: return "http://hl7.org/fhir/data-types";
            case UUID: return "http://hl7.org/fhir/data-types";
            case XHTML: return "http://hl7.org/fhir/data-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ADDRESS: return "An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.";
            case AGE: return "A duration of time during which an organism (or a process) has existed.";
            case ANNOTATION: return "A  text note which also  contains information about who made the statement and when.";
            case ATTACHMENT: return "For referring to data content defined in other formats.";
            case BACKBONEELEMENT: return "Base definition for all elements that are defined inside a resource - but not those in a data type.";
            case CODEABLECONCEPT: return "A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.";
            case CODING: return "A reference to a code defined by a terminology system.";
            case CONTACTDETAIL: return "Specifies contact information for a person or organization.";
            case CONTACTPOINT: return "Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.";
            case CONTRIBUTOR: return "A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.";
            case COUNT: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case DATAREQUIREMENT: return "Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.";
            case DISTANCE: return "A length - a value with a unit that is a physical distance.";
            case DOSAGE: return "Indicates how the medication is/was taken or should be taken by the patient.";
            case DURATION: return "A length of time.";
            case ELEMENT: return "Base definition for all elements in a resource.";
            case ELEMENTDEFINITION: return "Captures constraints on each element within the resource, profile, or extension.";
            case EXPRESSION: return "A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.";
            case EXTENSION: return "Optional Extension Element - found in all resources.";
            case HUMANNAME: return "A human's name with the ability to identify parts and usage.";
            case IDENTIFIER: return "An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.";
            case MARKETINGSTATUS: return "The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.";
            case META: return "The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.";
            case MONEY: return "An amount of economic utility in some recognized currency.";
            case MONEYQUANTITY: return "";
            case NARRATIVE: return "A human-readable formatted text, including images.";
            case PARAMETERDEFINITION: return "The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.";
            case PERIOD: return "A time period defined by a start and end date and optionally time.";
            case PRODCHARACTERISTIC: return "The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.";
            case PRODUCTSHELFLIFE: return "The shelf-life and storage information for a medicinal product item or container can be described using this class.";
            case QUANTITY: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case RANGE: return "A set of ordered Quantities defined by a low and high limit.";
            case RATIO: return "A relationship of two Quantity values - expressed as a numerator and a denominator.";
            case REFERENCE: return "A reference from one resource to another.";
            case RELATEDARTIFACT: return "Related artifacts such as additional documentation, justification, or bibliographic references.";
            case SAMPLEDDATA: return "A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.";
            case SIGNATURE: return "A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.";
            case SIMPLEQUANTITY: return "";
            case SUBSTANCEAMOUNT: return "Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.";
            case SUBSTANCEMOIETY: return "Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.";
            case TIMING: return "Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.";
            case TRIGGERDEFINITION: return "A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.";
            case USAGECONTEXT: return "Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).";
            case BASE64BINARY: return "A stream of bytes";
            case BOOLEAN: return "Value of \"true\" or \"false\"";
            case CANONICAL: return "A URI that is a reference to a canonical URI on a FHIR resource";
            case CODE: return "A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents";
            case DATE: return "A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.";
            case DECIMAL: return "A rational number with implicit precision";
            case ID: return "Any combination of letters, numerals, \"-\" and \".\", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.";
            case INSTANT: return "An instant in time - known at least to the second";
            case INTEGER: return "A whole number";
            case MARKDOWN: return "A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine";
            case OID: return "An OID represented as a URI";
            case POSITIVEINT: return "An integer with a value that is positive (e.g. >0)";
            case STRING: return "A sequence of Unicode characters";
            case TIME: return "A time during the day, with no date specified";
            case UNSIGNEDINT: return "An integer with a value that is not negative (e.g. >= 0)";
            case URI: return "String of characters used to identify a name or a resource";
            case URL: return "A URI that is a literal reference";
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
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXPRESSION: return "Expression";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case MARKETINGSTATUS: return "MarketingStatus";
            case META: return "Meta";
            case MONEY: return "Money";
            case MONEYQUANTITY: return "MoneyQuantity";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case PRODCHARACTERISTIC: return "ProdCharacteristic";
            case PRODUCTSHELFLIFE: return "ProductShelfLife";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case SUBSTANCEAMOUNT: return "SubstanceAmount";
            case SUBSTANCEMOIETY: return "SubstanceMoiety";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CANONICAL: return "canonical";
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
            case URL: return "url";
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
        if ("ContactDetail".equals(codeString))
          return DataType.CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return DataType.CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return DataType.CONTRIBUTOR;
        if ("Count".equals(codeString))
          return DataType.COUNT;
        if ("DataRequirement".equals(codeString))
          return DataType.DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return DataType.DISTANCE;
        if ("Dosage".equals(codeString))
          return DataType.DOSAGE;
        if ("Duration".equals(codeString))
          return DataType.DURATION;
        if ("Element".equals(codeString))
          return DataType.ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return DataType.ELEMENTDEFINITION;
        if ("Expression".equals(codeString))
          return DataType.EXPRESSION;
        if ("Extension".equals(codeString))
          return DataType.EXTENSION;
        if ("HumanName".equals(codeString))
          return DataType.HUMANNAME;
        if ("Identifier".equals(codeString))
          return DataType.IDENTIFIER;
        if ("MarketingStatus".equals(codeString))
          return DataType.MARKETINGSTATUS;
        if ("Meta".equals(codeString))
          return DataType.META;
        if ("Money".equals(codeString))
          return DataType.MONEY;
        if ("MoneyQuantity".equals(codeString))
          return DataType.MONEYQUANTITY;
        if ("Narrative".equals(codeString))
          return DataType.NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return DataType.PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return DataType.PERIOD;
        if ("ProdCharacteristic".equals(codeString))
          return DataType.PRODCHARACTERISTIC;
        if ("ProductShelfLife".equals(codeString))
          return DataType.PRODUCTSHELFLIFE;
        if ("Quantity".equals(codeString))
          return DataType.QUANTITY;
        if ("Range".equals(codeString))
          return DataType.RANGE;
        if ("Ratio".equals(codeString))
          return DataType.RATIO;
        if ("Reference".equals(codeString))
          return DataType.REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return DataType.RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return DataType.SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return DataType.SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return DataType.SIMPLEQUANTITY;
        if ("SubstanceAmount".equals(codeString))
          return DataType.SUBSTANCEAMOUNT;
        if ("SubstanceMoiety".equals(codeString))
          return DataType.SUBSTANCEMOIETY;
        if ("Timing".equals(codeString))
          return DataType.TIMING;
        if ("TriggerDefinition".equals(codeString))
          return DataType.TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return DataType.USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return DataType.BASE64BINARY;
        if ("boolean".equals(codeString))
          return DataType.BOOLEAN;
        if ("canonical".equals(codeString))
          return DataType.CANONICAL;
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
        if ("url".equals(codeString))
          return DataType.URL;
        if ("uuid".equals(codeString))
          return DataType.UUID;
        if ("xhtml".equals(codeString))
          return DataType.XHTML;
        throw new IllegalArgumentException("Unknown DataType code '"+codeString+"'");
        }
        public Enumeration<DataType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DataType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Address".equals(codeString))
          return new Enumeration<DataType>(this, DataType.ADDRESS);
        if ("Age".equals(codeString))
          return new Enumeration<DataType>(this, DataType.AGE);
        if ("Annotation".equals(codeString))
          return new Enumeration<DataType>(this, DataType.ANNOTATION);
        if ("Attachment".equals(codeString))
          return new Enumeration<DataType>(this, DataType.ATTACHMENT);
        if ("BackboneElement".equals(codeString))
          return new Enumeration<DataType>(this, DataType.BACKBONEELEMENT);
        if ("CodeableConcept".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CODEABLECONCEPT);
        if ("Coding".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CODING);
        if ("ContactDetail".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CONTACTDETAIL);
        if ("ContactPoint".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CONTACTPOINT);
        if ("Contributor".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CONTRIBUTOR);
        if ("Count".equals(codeString))
          return new Enumeration<DataType>(this, DataType.COUNT);
        if ("DataRequirement".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DATAREQUIREMENT);
        if ("Distance".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DISTANCE);
        if ("Dosage".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DOSAGE);
        if ("Duration".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DURATION);
        if ("Element".equals(codeString))
          return new Enumeration<DataType>(this, DataType.ELEMENT);
        if ("ElementDefinition".equals(codeString))
          return new Enumeration<DataType>(this, DataType.ELEMENTDEFINITION);
        if ("Expression".equals(codeString))
          return new Enumeration<DataType>(this, DataType.EXPRESSION);
        if ("Extension".equals(codeString))
          return new Enumeration<DataType>(this, DataType.EXTENSION);
        if ("HumanName".equals(codeString))
          return new Enumeration<DataType>(this, DataType.HUMANNAME);
        if ("Identifier".equals(codeString))
          return new Enumeration<DataType>(this, DataType.IDENTIFIER);
        if ("MarketingStatus".equals(codeString))
          return new Enumeration<DataType>(this, DataType.MARKETINGSTATUS);
        if ("Meta".equals(codeString))
          return new Enumeration<DataType>(this, DataType.META);
        if ("Money".equals(codeString))
          return new Enumeration<DataType>(this, DataType.MONEY);
        if ("MoneyQuantity".equals(codeString))
          return new Enumeration<DataType>(this, DataType.MONEYQUANTITY);
        if ("Narrative".equals(codeString))
          return new Enumeration<DataType>(this, DataType.NARRATIVE);
        if ("ParameterDefinition".equals(codeString))
          return new Enumeration<DataType>(this, DataType.PARAMETERDEFINITION);
        if ("Period".equals(codeString))
          return new Enumeration<DataType>(this, DataType.PERIOD);
        if ("ProdCharacteristic".equals(codeString))
          return new Enumeration<DataType>(this, DataType.PRODCHARACTERISTIC);
        if ("ProductShelfLife".equals(codeString))
          return new Enumeration<DataType>(this, DataType.PRODUCTSHELFLIFE);
        if ("Quantity".equals(codeString))
          return new Enumeration<DataType>(this, DataType.QUANTITY);
        if ("Range".equals(codeString))
          return new Enumeration<DataType>(this, DataType.RANGE);
        if ("Ratio".equals(codeString))
          return new Enumeration<DataType>(this, DataType.RATIO);
        if ("Reference".equals(codeString))
          return new Enumeration<DataType>(this, DataType.REFERENCE);
        if ("RelatedArtifact".equals(codeString))
          return new Enumeration<DataType>(this, DataType.RELATEDARTIFACT);
        if ("SampledData".equals(codeString))
          return new Enumeration<DataType>(this, DataType.SAMPLEDDATA);
        if ("Signature".equals(codeString))
          return new Enumeration<DataType>(this, DataType.SIGNATURE);
        if ("SimpleQuantity".equals(codeString))
          return new Enumeration<DataType>(this, DataType.SIMPLEQUANTITY);
        if ("SubstanceAmount".equals(codeString))
          return new Enumeration<DataType>(this, DataType.SUBSTANCEAMOUNT);
        if ("SubstanceMoiety".equals(codeString))
          return new Enumeration<DataType>(this, DataType.SUBSTANCEMOIETY);
        if ("Timing".equals(codeString))
          return new Enumeration<DataType>(this, DataType.TIMING);
        if ("TriggerDefinition".equals(codeString))
          return new Enumeration<DataType>(this, DataType.TRIGGERDEFINITION);
        if ("UsageContext".equals(codeString))
          return new Enumeration<DataType>(this, DataType.USAGECONTEXT);
        if ("base64Binary".equals(codeString))
          return new Enumeration<DataType>(this, DataType.BASE64BINARY);
        if ("boolean".equals(codeString))
          return new Enumeration<DataType>(this, DataType.BOOLEAN);
        if ("canonical".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CANONICAL);
        if ("code".equals(codeString))
          return new Enumeration<DataType>(this, DataType.CODE);
        if ("date".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DATE);
        if ("dateTime".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DATETIME);
        if ("decimal".equals(codeString))
          return new Enumeration<DataType>(this, DataType.DECIMAL);
        if ("id".equals(codeString))
          return new Enumeration<DataType>(this, DataType.ID);
        if ("instant".equals(codeString))
          return new Enumeration<DataType>(this, DataType.INSTANT);
        if ("integer".equals(codeString))
          return new Enumeration<DataType>(this, DataType.INTEGER);
        if ("markdown".equals(codeString))
          return new Enumeration<DataType>(this, DataType.MARKDOWN);
        if ("oid".equals(codeString))
          return new Enumeration<DataType>(this, DataType.OID);
        if ("positiveInt".equals(codeString))
          return new Enumeration<DataType>(this, DataType.POSITIVEINT);
        if ("string".equals(codeString))
          return new Enumeration<DataType>(this, DataType.STRING);
        if ("time".equals(codeString))
          return new Enumeration<DataType>(this, DataType.TIME);
        if ("unsignedInt".equals(codeString))
          return new Enumeration<DataType>(this, DataType.UNSIGNEDINT);
        if ("uri".equals(codeString))
          return new Enumeration<DataType>(this, DataType.URI);
        if ("url".equals(codeString))
          return new Enumeration<DataType>(this, DataType.URL);
        if ("uuid".equals(codeString))
          return new Enumeration<DataType>(this, DataType.UUID);
        if ("xhtml".equals(codeString))
          return new Enumeration<DataType>(this, DataType.XHTML);
        throw new FHIRException("Unknown DataType code '"+codeString+"'");
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
      if (code == DataType.CONTACTDETAIL)
        return "ContactDetail";
      if (code == DataType.CONTACTPOINT)
        return "ContactPoint";
      if (code == DataType.CONTRIBUTOR)
        return "Contributor";
      if (code == DataType.COUNT)
        return "Count";
      if (code == DataType.DATAREQUIREMENT)
        return "DataRequirement";
      if (code == DataType.DISTANCE)
        return "Distance";
      if (code == DataType.DOSAGE)
        return "Dosage";
      if (code == DataType.DURATION)
        return "Duration";
      if (code == DataType.ELEMENT)
        return "Element";
      if (code == DataType.ELEMENTDEFINITION)
        return "ElementDefinition";
      if (code == DataType.EXPRESSION)
        return "Expression";
      if (code == DataType.EXTENSION)
        return "Extension";
      if (code == DataType.HUMANNAME)
        return "HumanName";
      if (code == DataType.IDENTIFIER)
        return "Identifier";
      if (code == DataType.MARKETINGSTATUS)
        return "MarketingStatus";
      if (code == DataType.META)
        return "Meta";
      if (code == DataType.MONEY)
        return "Money";
      if (code == DataType.MONEYQUANTITY)
        return "MoneyQuantity";
      if (code == DataType.NARRATIVE)
        return "Narrative";
      if (code == DataType.PARAMETERDEFINITION)
        return "ParameterDefinition";
      if (code == DataType.PERIOD)
        return "Period";
      if (code == DataType.PRODCHARACTERISTIC)
        return "ProdCharacteristic";
      if (code == DataType.PRODUCTSHELFLIFE)
        return "ProductShelfLife";
      if (code == DataType.QUANTITY)
        return "Quantity";
      if (code == DataType.RANGE)
        return "Range";
      if (code == DataType.RATIO)
        return "Ratio";
      if (code == DataType.REFERENCE)
        return "Reference";
      if (code == DataType.RELATEDARTIFACT)
        return "RelatedArtifact";
      if (code == DataType.SAMPLEDDATA)
        return "SampledData";
      if (code == DataType.SIGNATURE)
        return "Signature";
      if (code == DataType.SIMPLEQUANTITY)
        return "SimpleQuantity";
      if (code == DataType.SUBSTANCEAMOUNT)
        return "SubstanceAmount";
      if (code == DataType.SUBSTANCEMOIETY)
        return "SubstanceMoiety";
      if (code == DataType.TIMING)
        return "Timing";
      if (code == DataType.TRIGGERDEFINITION)
        return "TriggerDefinition";
      if (code == DataType.USAGECONTEXT)
        return "UsageContext";
      if (code == DataType.BASE64BINARY)
        return "base64Binary";
      if (code == DataType.BOOLEAN)
        return "boolean";
      if (code == DataType.CANONICAL)
        return "canonical";
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
      if (code == DataType.URL)
        return "url";
      if (code == DataType.UUID)
        return "uuid";
      if (code == DataType.XHTML)
        return "xhtml";
      return "?";
      }
    public String toSystem(DataType code) {
      return code.getSystem();
      }
    }

    public enum DefinitionResourceType {
        /**
         * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
         */
        ACTIVITYDEFINITION, 
        /**
         * The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENTDEFINITION, 
        /**
         * The Measure resource provides the definition of a quality measure.
         */
        MEASURE, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.
         */
        PLANDEFINITION, 
        /**
         * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
         */
        QUESTIONNAIRE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DefinitionResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("EventDefinition".equals(codeString))
          return EVENTDEFINITION;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("Questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        throw new FHIRException("Unknown DefinitionResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case EVENTDEFINITION: return "EventDefinition";
            case MEASURE: return "Measure";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case PLANDEFINITION: return "PlanDefinition";
            case QUESTIONNAIRE: return "Questionnaire";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVITYDEFINITION: return "http://hl7.org/fhir/definition-resource-types";
            case EVENTDEFINITION: return "http://hl7.org/fhir/definition-resource-types";
            case MEASURE: return "http://hl7.org/fhir/definition-resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/definition-resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/definition-resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/definition-resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVITYDEFINITION: return "This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.";
            case EVENTDEFINITION: return "The EventDefinition resource provides a reusable description of when a particular event can occur.";
            case MEASURE: return "The Measure resource provides the definition of a quality measure.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case EVENTDEFINITION: return "EventDefinition";
            case MEASURE: return "Measure";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case PLANDEFINITION: return "PlanDefinition";
            case QUESTIONNAIRE: return "Questionnaire";
            default: return "?";
          }
        }
    }

  public static class DefinitionResourceTypeEnumFactory implements EnumFactory<DefinitionResourceType> {
    public DefinitionResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ActivityDefinition".equals(codeString))
          return DefinitionResourceType.ACTIVITYDEFINITION;
        if ("EventDefinition".equals(codeString))
          return DefinitionResourceType.EVENTDEFINITION;
        if ("Measure".equals(codeString))
          return DefinitionResourceType.MEASURE;
        if ("OperationDefinition".equals(codeString))
          return DefinitionResourceType.OPERATIONDEFINITION;
        if ("PlanDefinition".equals(codeString))
          return DefinitionResourceType.PLANDEFINITION;
        if ("Questionnaire".equals(codeString))
          return DefinitionResourceType.QUESTIONNAIRE;
        throw new IllegalArgumentException("Unknown DefinitionResourceType code '"+codeString+"'");
        }
        public Enumeration<DefinitionResourceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DefinitionResourceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<DefinitionResourceType>(this, DefinitionResourceType.ACTIVITYDEFINITION);
        if ("EventDefinition".equals(codeString))
          return new Enumeration<DefinitionResourceType>(this, DefinitionResourceType.EVENTDEFINITION);
        if ("Measure".equals(codeString))
          return new Enumeration<DefinitionResourceType>(this, DefinitionResourceType.MEASURE);
        if ("OperationDefinition".equals(codeString))
          return new Enumeration<DefinitionResourceType>(this, DefinitionResourceType.OPERATIONDEFINITION);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<DefinitionResourceType>(this, DefinitionResourceType.PLANDEFINITION);
        if ("Questionnaire".equals(codeString))
          return new Enumeration<DefinitionResourceType>(this, DefinitionResourceType.QUESTIONNAIRE);
        throw new FHIRException("Unknown DefinitionResourceType code '"+codeString+"'");
        }
    public String toCode(DefinitionResourceType code) {
      if (code == DefinitionResourceType.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == DefinitionResourceType.EVENTDEFINITION)
        return "EventDefinition";
      if (code == DefinitionResourceType.MEASURE)
        return "Measure";
      if (code == DefinitionResourceType.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == DefinitionResourceType.PLANDEFINITION)
        return "PlanDefinition";
      if (code == DefinitionResourceType.QUESTIONNAIRE)
        return "Questionnaire";
      return "?";
      }
    public String toSystem(DefinitionResourceType code) {
      return code.getSystem();
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
        public static DocumentReferenceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return CURRENT;
        if ("superseded".equals(codeString))
          return SUPERSEDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown DocumentReferenceStatus code '"+codeString+"'");
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
        public Enumeration<DocumentReferenceStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DocumentReferenceStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("current".equals(codeString))
          return new Enumeration<DocumentReferenceStatus>(this, DocumentReferenceStatus.CURRENT);
        if ("superseded".equals(codeString))
          return new Enumeration<DocumentReferenceStatus>(this, DocumentReferenceStatus.SUPERSEDED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DocumentReferenceStatus>(this, DocumentReferenceStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown DocumentReferenceStatus code '"+codeString+"'");
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
    public String toSystem(DocumentReferenceStatus code) {
      return code.getSystem();
      }
    }

    public enum EventResourceType {
        /**
         * Item containing charge code(s) associated with the provision of healthcare provider products.
         */
        CHARGEITEM, 
        /**
         * Remittance resource.
         */
        CLAIMRESPONSE, 
        /**
         * A clinical assessment performed when planning treatments and management strategies for a patient.
         */
        CLINICALIMPRESSION, 
        /**
         * A record of information transmitted from a sender to a receiver.
         */
        COMMUNICATION, 
        /**
         * A set of resources composed into a single coherent clinical statement with clinical attestation.
         */
        COMPOSITION, 
        /**
         * Detailed information about conditions, problems or diagnoses.
         */
        CONDITION, 
        /**
         * A healthcare consumer's policy choices to permits or denies recipients or roles to perform actions for specific purposes and periods of time.
         */
        CONSENT, 
        /**
         * Insurance or medical plan or a payment agreement.
         */
        COVERAGE, 
        /**
         * Record of use of a device.
         */
        DEVICEUSESTATEMENT, 
        /**
         * A Diagnostic report - a combination of request information, atomic results, images, interpretation, as well as formatted reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A list that defines a set of documents.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document.
         */
        DOCUMENTREFERENCE, 
        /**
         * An interaction during which services are provided to the patient.
         */
        ENCOUNTER, 
        /**
         * EnrollmentResponse resource.
         */
        ENROLLMENTRESPONSE, 
        /**
         * An association of a Patient with an Organization and  Healthcare Provider(s) for a period of time that the Organization assumes some level of responsibility.
         */
        EPISODEOFCARE, 
        /**
         * Explanation of Benefit resource.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Information about patient's relatives, relevant for patient.
         */
        FAMILYMEMBERHISTORY, 
        /**
         * The formal response to a guidance request.
         */
        GUIDANCERESPONSE, 
        /**
         * A set of images produced in single study (one or more series of references images).
         */
        IMAGINGSTUDY, 
        /**
         * Immunization event information.
         */
        IMMUNIZATION, 
        /**
         * Results of a measure evaluation.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * Administration of medication to a patient.
         */
        MEDICATIONADMINISTRATION, 
        /**
         * Dispensing a medication to a named patient.
         */
        MEDICATIONDISPENSE, 
        /**
         * Record of medication being taken by a patient.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Measurements and simple assertions.
         */
        OBSERVATION, 
        /**
         * PaymentNotice request.
         */
        PAYMENTNOTICE, 
        /**
         * PaymentReconciliation resource.
         */
        PAYMENTRECONCILIATION, 
        /**
         * An action that is being or was performed on a patient.
         */
        PROCEDURE, 
        /**
         * ProcessResponse resource.
         */
        PROCESSRESPONSE, 
        /**
         * A structured set of questions and their answers.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Potential outcomes for a subject with likelihood.
         */
        RISKASSESSMENT, 
        /**
         * Delivery of bulk Supplies.
         */
        SUPPLYDELIVERY, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EventResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Consent".equals(codeString))
          return CONSENT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("GuidanceResponse".equals(codeString))
          return GUIDANCERESPONSE;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("MeasureReport".equals(codeString))
          return MEASUREREPORT;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("PaymentNotice".equals(codeString))
          return PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return PAYMENTRECONCILIATION;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
        if ("ProcessResponse".equals(codeString))
          return PROCESSRESPONSE;
        if ("QuestionnaireResponse".equals(codeString))
          return QUESTIONNAIRERESPONSE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("Task".equals(codeString))
          return TASK;
        throw new FHIRException("Unknown EventResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CHARGEITEM: return "ChargeItem";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMPOSITION: return "Composition";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case COVERAGE: return "Coverage";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case OBSERVATION: return "Observation";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PROCEDURE: return "Procedure";
            case PROCESSRESPONSE: return "ProcessResponse";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RISKASSESSMENT: return "RiskAssessment";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case TASK: return "Task";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CHARGEITEM: return "http://hl7.org/fhir/event-resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/event-resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/event-resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/event-resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/event-resource-types";
            case CONDITION: return "http://hl7.org/fhir/event-resource-types";
            case CONSENT: return "http://hl7.org/fhir/event-resource-types";
            case COVERAGE: return "http://hl7.org/fhir/event-resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/event-resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/event-resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/event-resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/event-resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/event-resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/event-resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/event-resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/event-resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/event-resource-types";
            case GUIDANCERESPONSE: return "http://hl7.org/fhir/event-resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/event-resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/event-resource-types";
            case MEASUREREPORT: return "http://hl7.org/fhir/event-resource-types";
            case MEDIA: return "http://hl7.org/fhir/event-resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/event-resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/event-resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/event-resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/event-resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/event-resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/event-resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/event-resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/event-resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/event-resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/event-resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/event-resource-types";
            case TASK: return "http://hl7.org/fhir/event-resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CHARGEITEM: return "Item containing charge code(s) associated with the provision of healthcare provider products.";
            case CLAIMRESPONSE: return "Remittance resource.";
            case CLINICALIMPRESSION: return "A clinical assessment performed when planning treatments and management strategies for a patient.";
            case COMMUNICATION: return "A record of information transmitted from a sender to a receiver.";
            case COMPOSITION: return "A set of resources composed into a single coherent clinical statement with clinical attestation.";
            case CONDITION: return "Detailed information about conditions, problems or diagnoses.";
            case CONSENT: return "A healthcare consumer's policy choices to permits or denies recipients or roles to perform actions for specific purposes and periods of time.";
            case COVERAGE: return "Insurance or medical plan or a payment agreement.";
            case DEVICEUSESTATEMENT: return "Record of use of a device.";
            case DIAGNOSTICREPORT: return "A Diagnostic report - a combination of request information, atomic results, images, interpretation, as well as formatted reports.";
            case DOCUMENTMANIFEST: return "A list that defines a set of documents.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case ENCOUNTER: return "An interaction during which services are provided to the patient.";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse resource.";
            case EPISODEOFCARE: return "An association of a Patient with an Organization and  Healthcare Provider(s) for a period of time that the Organization assumes some level of responsibility.";
            case EXPLANATIONOFBENEFIT: return "Explanation of Benefit resource.";
            case FAMILYMEMBERHISTORY: return "Information about patient's relatives, relevant for patient.";
            case GUIDANCERESPONSE: return "The formal response to a guidance request.";
            case IMAGINGSTUDY: return "A set of images produced in single study (one or more series of references images).";
            case IMMUNIZATION: return "Immunization event information.";
            case MEASUREREPORT: return "Results of a measure evaluation.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATIONADMINISTRATION: return "Administration of medication to a patient.";
            case MEDICATIONDISPENSE: return "Dispensing a medication to a named patient.";
            case MEDICATIONSTATEMENT: return "Record of medication being taken by a patient.";
            case OBSERVATION: return "Measurements and simple assertions.";
            case PAYMENTNOTICE: return "PaymentNotice request.";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation resource.";
            case PROCEDURE: return "An action that is being or was performed on a patient.";
            case PROCESSRESPONSE: return "ProcessResponse resource.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers.";
            case RISKASSESSMENT: return "Potential outcomes for a subject with likelihood.";
            case SUPPLYDELIVERY: return "Delivery of bulk Supplies.";
            case TASK: return "A task to be performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CHARGEITEM: return "ChargeItem";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case COMMUNICATION: return "Communication";
            case COMPOSITION: return "Composition";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case COVERAGE: return "Coverage";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case ENCOUNTER: return "Encounter";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case OBSERVATION: return "Observation";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PROCEDURE: return "Procedure";
            case PROCESSRESPONSE: return "ProcessResponse";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RISKASSESSMENT: return "RiskAssessment";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case TASK: return "Task";
            default: return "?";
          }
        }
    }

  public static class EventResourceTypeEnumFactory implements EnumFactory<EventResourceType> {
    public EventResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ChargeItem".equals(codeString))
          return EventResourceType.CHARGEITEM;
        if ("ClaimResponse".equals(codeString))
          return EventResourceType.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return EventResourceType.CLINICALIMPRESSION;
        if ("Communication".equals(codeString))
          return EventResourceType.COMMUNICATION;
        if ("Composition".equals(codeString))
          return EventResourceType.COMPOSITION;
        if ("Condition".equals(codeString))
          return EventResourceType.CONDITION;
        if ("Consent".equals(codeString))
          return EventResourceType.CONSENT;
        if ("Coverage".equals(codeString))
          return EventResourceType.COVERAGE;
        if ("DeviceUseStatement".equals(codeString))
          return EventResourceType.DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return EventResourceType.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return EventResourceType.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return EventResourceType.DOCUMENTREFERENCE;
        if ("Encounter".equals(codeString))
          return EventResourceType.ENCOUNTER;
        if ("EnrollmentResponse".equals(codeString))
          return EventResourceType.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EventResourceType.EPISODEOFCARE;
        if ("ExplanationOfBenefit".equals(codeString))
          return EventResourceType.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return EventResourceType.FAMILYMEMBERHISTORY;
        if ("GuidanceResponse".equals(codeString))
          return EventResourceType.GUIDANCERESPONSE;
        if ("ImagingStudy".equals(codeString))
          return EventResourceType.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return EventResourceType.IMMUNIZATION;
        if ("MeasureReport".equals(codeString))
          return EventResourceType.MEASUREREPORT;
        if ("Media".equals(codeString))
          return EventResourceType.MEDIA;
        if ("MedicationAdministration".equals(codeString))
          return EventResourceType.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return EventResourceType.MEDICATIONDISPENSE;
        if ("MedicationStatement".equals(codeString))
          return EventResourceType.MEDICATIONSTATEMENT;
        if ("Observation".equals(codeString))
          return EventResourceType.OBSERVATION;
        if ("PaymentNotice".equals(codeString))
          return EventResourceType.PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return EventResourceType.PAYMENTRECONCILIATION;
        if ("Procedure".equals(codeString))
          return EventResourceType.PROCEDURE;
        if ("ProcessResponse".equals(codeString))
          return EventResourceType.PROCESSRESPONSE;
        if ("QuestionnaireResponse".equals(codeString))
          return EventResourceType.QUESTIONNAIRERESPONSE;
        if ("RiskAssessment".equals(codeString))
          return EventResourceType.RISKASSESSMENT;
        if ("SupplyDelivery".equals(codeString))
          return EventResourceType.SUPPLYDELIVERY;
        if ("Task".equals(codeString))
          return EventResourceType.TASK;
        throw new IllegalArgumentException("Unknown EventResourceType code '"+codeString+"'");
        }
        public Enumeration<EventResourceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EventResourceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("ChargeItem".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.CHARGEITEM);
        if ("ClaimResponse".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.CLAIMRESPONSE);
        if ("ClinicalImpression".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.CLINICALIMPRESSION);
        if ("Communication".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.COMMUNICATION);
        if ("Composition".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.COMPOSITION);
        if ("Condition".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.CONDITION);
        if ("Consent".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.CONSENT);
        if ("Coverage".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.COVERAGE);
        if ("DeviceUseStatement".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.DEVICEUSESTATEMENT);
        if ("DiagnosticReport".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.DIAGNOSTICREPORT);
        if ("DocumentManifest".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.DOCUMENTMANIFEST);
        if ("DocumentReference".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.DOCUMENTREFERENCE);
        if ("Encounter".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.ENCOUNTER);
        if ("EnrollmentResponse".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.ENROLLMENTRESPONSE);
        if ("EpisodeOfCare".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.EPISODEOFCARE);
        if ("ExplanationOfBenefit".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.EXPLANATIONOFBENEFIT);
        if ("FamilyMemberHistory".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.FAMILYMEMBERHISTORY);
        if ("GuidanceResponse".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.GUIDANCERESPONSE);
        if ("ImagingStudy".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.IMAGINGSTUDY);
        if ("Immunization".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.IMMUNIZATION);
        if ("MeasureReport".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.MEASUREREPORT);
        if ("Media".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.MEDIA);
        if ("MedicationAdministration".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.MEDICATIONADMINISTRATION);
        if ("MedicationDispense".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.MEDICATIONDISPENSE);
        if ("MedicationStatement".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.MEDICATIONSTATEMENT);
        if ("Observation".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.OBSERVATION);
        if ("PaymentNotice".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.PAYMENTNOTICE);
        if ("PaymentReconciliation".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.PAYMENTRECONCILIATION);
        if ("Procedure".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.PROCEDURE);
        if ("ProcessResponse".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.PROCESSRESPONSE);
        if ("QuestionnaireResponse".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.QUESTIONNAIRERESPONSE);
        if ("RiskAssessment".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.RISKASSESSMENT);
        if ("SupplyDelivery".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.SUPPLYDELIVERY);
        if ("Task".equals(codeString))
          return new Enumeration<EventResourceType>(this, EventResourceType.TASK);
        throw new FHIRException("Unknown EventResourceType code '"+codeString+"'");
        }
    public String toCode(EventResourceType code) {
      if (code == EventResourceType.CHARGEITEM)
        return "ChargeItem";
      if (code == EventResourceType.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == EventResourceType.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == EventResourceType.COMMUNICATION)
        return "Communication";
      if (code == EventResourceType.COMPOSITION)
        return "Composition";
      if (code == EventResourceType.CONDITION)
        return "Condition";
      if (code == EventResourceType.CONSENT)
        return "Consent";
      if (code == EventResourceType.COVERAGE)
        return "Coverage";
      if (code == EventResourceType.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == EventResourceType.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == EventResourceType.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == EventResourceType.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == EventResourceType.ENCOUNTER)
        return "Encounter";
      if (code == EventResourceType.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == EventResourceType.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == EventResourceType.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == EventResourceType.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == EventResourceType.GUIDANCERESPONSE)
        return "GuidanceResponse";
      if (code == EventResourceType.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == EventResourceType.IMMUNIZATION)
        return "Immunization";
      if (code == EventResourceType.MEASUREREPORT)
        return "MeasureReport";
      if (code == EventResourceType.MEDIA)
        return "Media";
      if (code == EventResourceType.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == EventResourceType.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == EventResourceType.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == EventResourceType.OBSERVATION)
        return "Observation";
      if (code == EventResourceType.PAYMENTNOTICE)
        return "PaymentNotice";
      if (code == EventResourceType.PAYMENTRECONCILIATION)
        return "PaymentReconciliation";
      if (code == EventResourceType.PROCEDURE)
        return "Procedure";
      if (code == EventResourceType.PROCESSRESPONSE)
        return "ProcessResponse";
      if (code == EventResourceType.QUESTIONNAIRERESPONSE)
        return "QuestionnaireResponse";
      if (code == EventResourceType.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == EventResourceType.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == EventResourceType.TASK)
        return "Task";
      return "?";
      }
    public String toSystem(EventResourceType code) {
      return code.getSystem();
      }
    }

    public enum FHIRAllTypes {
        /**
         * An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.
         */
        ADDRESS, 
        /**
         * A duration of time during which an organism (or a process) has existed.
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
         * Specifies contact information for a person or organization.
         */
        CONTACTDETAIL, 
        /**
         * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
         */
        CONTACTPOINT, 
        /**
         * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
         */
        CONTRIBUTOR, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        COUNT, 
        /**
         * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.
         */
        DATAREQUIREMENT, 
        /**
         * A length - a value with a unit that is a physical distance.
         */
        DISTANCE, 
        /**
         * Indicates how the medication is/was taken or should be taken by the patient.
         */
        DOSAGE, 
        /**
         * A length of time.
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
         * A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.
         */
        EXPRESSION, 
        /**
         * Optional Extension Element - found in all resources.
         */
        EXTENSION, 
        /**
         * A human's name with the ability to identify parts and usage.
         */
        HUMANNAME, 
        /**
         * An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
         */
        IDENTIFIER, 
        /**
         * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
         */
        MARKETINGSTATUS, 
        /**
         * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.
         */
        META, 
        /**
         * An amount of economic utility in some recognized currency.
         */
        MONEY, 
        /**
         * null
         */
        MONEYQUANTITY, 
        /**
         * A human-readable formatted text, including images.
         */
        NARRATIVE, 
        /**
         * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
         */
        PARAMETERDEFINITION, 
        /**
         * A time period defined by a start and end date and optionally time.
         */
        PERIOD, 
        /**
         * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
         */
        PRODCHARACTERISTIC, 
        /**
         * The shelf-life and storage information for a medicinal product item or container can be described using this class.
         */
        PRODUCTSHELFLIFE, 
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
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         */
        RELATEDARTIFACT, 
        /**
         * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
         */
        SAMPLEDDATA, 
        /**
         * A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
         */
        SIGNATURE, 
        /**
         * null
         */
        SIMPLEQUANTITY, 
        /**
         * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
         */
        SUBSTANCEAMOUNT, 
        /**
         * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
         */
        SUBSTANCEMOIETY, 
        /**
         * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.
         */
        TIMING, 
        /**
         * A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.
         */
        TRIGGERDEFINITION, 
        /**
         * Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).
         */
        USAGECONTEXT, 
        /**
         * A stream of bytes
         */
        BASE64BINARY, 
        /**
         * Value of "true" or "false"
         */
        BOOLEAN, 
        /**
         * A URI that is a reference to a canonical URI on a FHIR resource
         */
        CANONICAL, 
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
         * A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN, 
        /**
         * An OID represented as a URI
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
         * A URI that is a literal reference
         */
        URL, 
        /**
         * A UUID, represented as a URI
         */
        UUID, 
        /**
         * XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
         */
        XHTML, 
        /**
         * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.
         */
        ACCOUNT, 
        /**
         * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
         */
        ACTIVITYDEFINITION, 
        /**
         * Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.
         */
        ADVERSEEVENT, 
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
         * A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY, 
        /**
         * A material substance originating from a biological entity intended to be transplanted or infused
into another (possibly the same) biological entity.
         */
        BIOLOGICALLYDERIVEDPRODUCT, 
        /**
         * Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSTRUCTURE, 
        /**
         * A container for a collection of resources.
         */
        BUNDLE, 
        /**
         * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        CAPABILITYSTATEMENT, 
        /**
         * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CAREPLAN, 
        /**
         * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.
         */
        CARETEAM, 
        /**
         * Catalog entries are wrappers that contextualize items included in a catalog.
         */
        CATALOGENTRY, 
        /**
         * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.
         */
        CHARGEITEM, 
        /**
         * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.
         */
        CHARGEITEMDEFINITION, 
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
         * The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.
         */
        CODESYSTEM, 
        /**
         * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
         */
        COMMUNICATION, 
        /**
         * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATIONREQUEST, 
        /**
         * A compartment definition that defines how resources are accessed on a server.
         */
        COMPARTMENTDEFINITION, 
        /**
         * A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).
         */
        COMPOSITION, 
        /**
         * A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.
         */
        CONCEPTMAP, 
        /**
         * A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.
         */
        CONDITION, 
        /**
         * A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
         */
        CONSENT, 
        /**
         * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to reimburse or pay for health care products and services.
         */
        COVERAGE, 
        /**
         * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
         */
        COVERAGEELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
         */
        COVERAGEELIGIBILITYRESPONSE, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).
         */
        DEVICE, 
        /**
         * The characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICEDEFINITION, 
        /**
         * Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICEMETRIC, 
        /**
         * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
         */
        DEVICEREQUEST, 
        /**
         * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
         */
        DEVICEUSESTATEMENT, 
        /**
         * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A collection of documents compiled for a purpose together with metadata that applies to the collection.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document.
         */
        DOCUMENTREFERENCE, 
        /**
         * A resource that includes narrative, extensions, and contained resources.
         */
        DOMAINRESOURCE, 
        /**
         * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
         */
        ENCOUNTER, 
        /**
         * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
         */
        ENDPOINT, 
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
         * The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENTDEFINITION, 
        /**
         * Example of workflow instance.
         */
        EXAMPLESCENARIO, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health conditions for a person related to the patient relevant in the context of care for the patient.
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
         * A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.
         */
        GRAPHDEFINITION, 
        /**
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
         */
        GROUP, 
        /**
         * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
         */
        GUIDANCERESPONSE, 
        /**
         * The details of a healthcare service available at a location.
         */
        HEALTHCARESERVICE, 
        /**
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.
         */
        IMMUNIZATION, 
        /**
         * Describes a comparison of an immunization event against published recommendations to determine if the administration is "valid" in relation to those  recommendations.
         */
        IMMUNIZATIONEVALUATION, 
        /**
         * A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * Details of a Health Insurance product/plan provided by an organization.
         */
        INSURANCEPLAN, 
        /**
         * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.
         */
        INVOICE, 
        /**
         * A physical, countable instance of an item, for example one box or one unit.
         */
        ITEMINSTANCE, 
        /**
         * The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.
         */
        LIBRARY, 
        /**
         * Identifies two or more records (resource instances) that are referring to the same real-world "occurrence".
         */
        LINKAGE, 
        /**
         * A set of information summarized from a list of other resources.
         */
        LIST, 
        /**
         * Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.
         */
        LOCATION, 
        /**
         * The Measure resource provides the definition of a quality measure.
         */
        MEASURE, 
        /**
         * The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.
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
         * Information about a medication that is used to support knowledge.
         */
        MEDICATIONKNOWLEDGE, 
        /**
         * An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.
         */
        MEDICATIONREQUEST, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
         */
        MEDICINALPRODUCT, 
        /**
         * The regulatory authorization of a medicinal product.
         */
        MEDICINALPRODUCTAUTHORIZATION, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCLINICALS, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCONTRAINDICATION, 
        /**
         * A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.
         */
        MEDICINALPRODUCTDEVICESPEC, 
        /**
         * Indication for the Medicinal Product.
         */
        MEDICINALPRODUCTINDICATION, 
        /**
         * An ingredient of a manufactured item or pharmaceutical product.
         */
        MEDICINALPRODUCTINGREDIENT, 
        /**
         * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
         */
        MEDICINALPRODUCTINTERACTION, 
        /**
         * The manufactured item as contained in the packaged medicinal product.
         */
        MEDICINALPRODUCTMANUFACTURED, 
        /**
         * A medicinal product in a container or package.
         */
        MEDICINALPRODUCTPACKAGED, 
        /**
         * A pharmaceutical product described in terms of its composition and dose form.
         */
        MEDICINALPRODUCTPHARMACEUTICAL, 
        /**
         * Describe the undesirable effects of the medicinal product.
         */
        MEDICINALPRODUCTUNDESIRABLEEFFECT, 
        /**
         * Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.
         */
        MESSAGEDEFINITION, 
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
         * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.
         */
        OBSERVATIONDEFINITION, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.
         */
        ORGANIZATION, 
        /**
         * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.
         */
        ORGANIZATIONAFFILIATION, 
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
         * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.
         */
        PLANDEFINITION, 
        /**
         * A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.
         */
        PRACTITIONERROLE, 
        /**
         * An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.
         */
        PROCEDURE, 
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
         * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
         */
        QUESTIONNAIRE, 
        /**
         * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give this medication after that one".
         */
        REQUESTGROUP, 
        /**
         * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
         */
        RESEARCHSTUDY, 
        /**
         * A physical entity which is the primary unit of operational and/or administrative interest in a study.
         */
        RESEARCHSUBJECT, 
        /**
         * This is the base resource type for everything.
         */
        RESOURCE, 
        /**
         * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISKASSESSMENT, 
        /**
         * A container for slots of time that may be available for booking appointments.
         */
        SCHEDULE, 
        /**
         * A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCHPARAMETER, 
        /**
         * Raw data describing a biological sequence.
         */
        SEQUENCE, 
        /**
         * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICEREQUEST, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A kind of specimen with associated set of requirements.
         */
        SPECIMENDEFINITION, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Todo.
         */
        SUBSTANCEPOLYMER, 
        /**
         * Todo.
         */
        SUBSTANCEREFERENCEINFORMATION, 
        /**
         * The detailed description of a substance, typically at a level beyond what is used for prescribing.
         */
        SUBSTANCESPECIFICATION, 
        /**
         * Record of delivery of what is supplied.
         */
        SUPPLYDELIVERY, 
        /**
         * A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLYREQUEST, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        TERMINOLOGYCAPABILITIES, 
        /**
         * A summary of information based on the results of executing a TestScript.
         */
        TESTREPORT, 
        /**
         * A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * Information about a user's current session.
         */
        USERSESSION, 
        /**
         * A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).
         */
        VALUESET, 
        /**
         * Describes validation requirements, source(s), status and dates for one or more elements.
         */
        VERIFICATIONRESULT, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * A place holder that means any kind of data type
         */
        TYPE, 
        /**
         * A place holder that means any kind of resource
         */
        ANY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FHIRAllTypes fromCode(String codeString) throws FHIRException {
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
        if ("ContactDetail".equals(codeString))
          return CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return CONTRIBUTOR;
        if ("Count".equals(codeString))
          return COUNT;
        if ("DataRequirement".equals(codeString))
          return DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return DISTANCE;
        if ("Dosage".equals(codeString))
          return DOSAGE;
        if ("Duration".equals(codeString))
          return DURATION;
        if ("Element".equals(codeString))
          return ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return ELEMENTDEFINITION;
        if ("Expression".equals(codeString))
          return EXPRESSION;
        if ("Extension".equals(codeString))
          return EXTENSION;
        if ("HumanName".equals(codeString))
          return HUMANNAME;
        if ("Identifier".equals(codeString))
          return IDENTIFIER;
        if ("MarketingStatus".equals(codeString))
          return MARKETINGSTATUS;
        if ("Meta".equals(codeString))
          return META;
        if ("Money".equals(codeString))
          return MONEY;
        if ("MoneyQuantity".equals(codeString))
          return MONEYQUANTITY;
        if ("Narrative".equals(codeString))
          return NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return PERIOD;
        if ("ProdCharacteristic".equals(codeString))
          return PRODCHARACTERISTIC;
        if ("ProductShelfLife".equals(codeString))
          return PRODUCTSHELFLIFE;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("Reference".equals(codeString))
          return REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return SIMPLEQUANTITY;
        if ("SubstanceAmount".equals(codeString))
          return SUBSTANCEAMOUNT;
        if ("SubstanceMoiety".equals(codeString))
          return SUBSTANCEMOIETY;
        if ("Timing".equals(codeString))
          return TIMING;
        if ("TriggerDefinition".equals(codeString))
          return TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return BASE64BINARY;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("canonical".equals(codeString))
          return CANONICAL;
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
        if ("url".equals(codeString))
          return URL;
        if ("uuid".equals(codeString))
          return UUID;
        if ("xhtml".equals(codeString))
          return XHTML;
        if ("Account".equals(codeString))
          return ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return ADVERSEEVENT;
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
        if ("BiologicallyDerivedProduct".equals(codeString))
          return BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("CareTeam".equals(codeString))
          return CARETEAM;
        if ("CatalogEntry".equals(codeString))
          return CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return CHARGEITEMDEFINITION;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return CODESYSTEM;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Consent".equals(codeString))
          return CONSENT;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("CoverageEligibilityRequest".equals(codeString))
          return COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return DEVICEDEFINITION;
        if ("DeviceMetric".equals(codeString))
          return DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return DOMAINRESOURCE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return EXAMPLESCENARIO;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FLAG;
        if ("Goal".equals(codeString))
          return GOAL;
        if ("GraphDefinition".equals(codeString))
          return GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return GROUP;
        if ("GuidanceResponse".equals(codeString))
          return GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("InsurancePlan".equals(codeString))
          return INSURANCEPLAN;
        if ("Invoice".equals(codeString))
          return INVOICE;
        if ("ItemInstance".equals(codeString))
          return ITEMINSTANCE;
        if ("Library".equals(codeString))
          return LIBRARY;
        if ("Linkage".equals(codeString))
          return LINKAGE;
        if ("List".equals(codeString))
          return LIST;
        if ("Location".equals(codeString))
          return LOCATION;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("MeasureReport".equals(codeString))
          return MEASUREREPORT;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("Medication".equals(codeString))
          return MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductContraindication".equals(codeString))
          return MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return MEDICINALPRODUCTUNDESIRABLEEFFECT;
        if ("MessageDefinition".equals(codeString))
          return MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return OBSERVATIONDEFINITION;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return ORGANIZATIONAFFILIATION;
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
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
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
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return TESTREPORT;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return USERSESSION;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VerificationResult".equals(codeString))
          return VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        if ("Type".equals(codeString))
          return TYPE;
        if ("Any".equals(codeString))
          return ANY;
        throw new FHIRException("Unknown FHIRAllTypes code '"+codeString+"'");
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
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXPRESSION: return "Expression";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case MARKETINGSTATUS: return "MarketingStatus";
            case META: return "Meta";
            case MONEY: return "Money";
            case MONEYQUANTITY: return "MoneyQuantity";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case PRODCHARACTERISTIC: return "ProdCharacteristic";
            case PRODUCTSHELFLIFE: return "ProductShelfLife";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case SUBSTANCEAMOUNT: return "SubstanceAmount";
            case SUBSTANCEMOIETY: return "SubstanceMoiety";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CANONICAL: return "canonical";
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
            case URL: return "url";
            case UUID: return "uuid";
            case XHTML: return "xhtml";
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INSURANCEPLAN: return "InsurancePlan";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            case TYPE: return "Type";
            case ANY: return "Any";
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
            case CONTACTDETAIL: return "http://hl7.org/fhir/data-types";
            case CONTACTPOINT: return "http://hl7.org/fhir/data-types";
            case CONTRIBUTOR: return "http://hl7.org/fhir/data-types";
            case COUNT: return "http://hl7.org/fhir/data-types";
            case DATAREQUIREMENT: return "http://hl7.org/fhir/data-types";
            case DISTANCE: return "http://hl7.org/fhir/data-types";
            case DOSAGE: return "http://hl7.org/fhir/data-types";
            case DURATION: return "http://hl7.org/fhir/data-types";
            case ELEMENT: return "http://hl7.org/fhir/data-types";
            case ELEMENTDEFINITION: return "http://hl7.org/fhir/data-types";
            case EXPRESSION: return "http://hl7.org/fhir/data-types";
            case EXTENSION: return "http://hl7.org/fhir/data-types";
            case HUMANNAME: return "http://hl7.org/fhir/data-types";
            case IDENTIFIER: return "http://hl7.org/fhir/data-types";
            case MARKETINGSTATUS: return "http://hl7.org/fhir/data-types";
            case META: return "http://hl7.org/fhir/data-types";
            case MONEY: return "http://hl7.org/fhir/data-types";
            case MONEYQUANTITY: return "http://hl7.org/fhir/data-types";
            case NARRATIVE: return "http://hl7.org/fhir/data-types";
            case PARAMETERDEFINITION: return "http://hl7.org/fhir/data-types";
            case PERIOD: return "http://hl7.org/fhir/data-types";
            case PRODCHARACTERISTIC: return "http://hl7.org/fhir/data-types";
            case PRODUCTSHELFLIFE: return "http://hl7.org/fhir/data-types";
            case QUANTITY: return "http://hl7.org/fhir/data-types";
            case RANGE: return "http://hl7.org/fhir/data-types";
            case RATIO: return "http://hl7.org/fhir/data-types";
            case REFERENCE: return "http://hl7.org/fhir/data-types";
            case RELATEDARTIFACT: return "http://hl7.org/fhir/data-types";
            case SAMPLEDDATA: return "http://hl7.org/fhir/data-types";
            case SIGNATURE: return "http://hl7.org/fhir/data-types";
            case SIMPLEQUANTITY: return "http://hl7.org/fhir/data-types";
            case SUBSTANCEAMOUNT: return "http://hl7.org/fhir/data-types";
            case SUBSTANCEMOIETY: return "http://hl7.org/fhir/data-types";
            case TIMING: return "http://hl7.org/fhir/data-types";
            case TRIGGERDEFINITION: return "http://hl7.org/fhir/data-types";
            case USAGECONTEXT: return "http://hl7.org/fhir/data-types";
            case BASE64BINARY: return "http://hl7.org/fhir/data-types";
            case BOOLEAN: return "http://hl7.org/fhir/data-types";
            case CANONICAL: return "http://hl7.org/fhir/data-types";
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
            case URL: return "http://hl7.org/fhir/data-types";
            case UUID: return "http://hl7.org/fhir/data-types";
            case XHTML: return "http://hl7.org/fhir/data-types";
            case ACCOUNT: return "http://hl7.org/fhir/resource-types";
            case ACTIVITYDEFINITION: return "http://hl7.org/fhir/resource-types";
            case ADVERSEEVENT: return "http://hl7.org/fhir/resource-types";
            case ALLERGYINTOLERANCE: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENT: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case AUDITEVENT: return "http://hl7.org/fhir/resource-types";
            case BASIC: return "http://hl7.org/fhir/resource-types";
            case BINARY: return "http://hl7.org/fhir/resource-types";
            case BIOLOGICALLYDERIVEDPRODUCT: return "http://hl7.org/fhir/resource-types";
            case BODYSTRUCTURE: return "http://hl7.org/fhir/resource-types";
            case BUNDLE: return "http://hl7.org/fhir/resource-types";
            case CAPABILITYSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/resource-types";
            case CARETEAM: return "http://hl7.org/fhir/resource-types";
            case CATALOGENTRY: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEM: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEMDEFINITION: return "http://hl7.org/fhir/resource-types";
            case CLAIM: return "http://hl7.org/fhir/resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/resource-types";
            case CODESYSTEM: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case COMPARTMENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/resource-types";
            case CONDITION: return "http://hl7.org/fhir/resource-types";
            case CONSENT: return "http://hl7.org/fhir/resource-types";
            case CONTRACT: return "http://hl7.org/fhir/resource-types";
            case COVERAGE: return "http://hl7.org/fhir/resource-types";
            case COVERAGEELIGIBILITYREQUEST: return "http://hl7.org/fhir/resource-types";
            case COVERAGEELIGIBILITYRESPONSE: return "http://hl7.org/fhir/resource-types";
            case DETECTEDISSUE: return "http://hl7.org/fhir/resource-types";
            case DEVICE: return "http://hl7.org/fhir/resource-types";
            case DEVICEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case DEVICEMETRIC: return "http://hl7.org/fhir/resource-types";
            case DEVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/resource-types";
            case DOMAINRESOURCE: return "http://hl7.org/fhir/resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/resource-types";
            case ENDPOINT: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EVENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case EXAMPLESCENARIO: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GRAPHDEFINITION: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case GUIDANCERESPONSE: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONEVALUATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case INSURANCEPLAN: return "http://hl7.org/fhir/resource-types";
            case INVOICE: return "http://hl7.org/fhir/resource-types";
            case ITEMINSTANCE: return "http://hl7.org/fhir/resource-types";
            case LIBRARY: return "http://hl7.org/fhir/resource-types";
            case LINKAGE: return "http://hl7.org/fhir/resource-types";
            case LIST: return "http://hl7.org/fhir/resource-types";
            case LOCATION: return "http://hl7.org/fhir/resource-types";
            case MEASURE: return "http://hl7.org/fhir/resource-types";
            case MEASUREREPORT: return "http://hl7.org/fhir/resource-types";
            case MEDIA: return "http://hl7.org/fhir/resource-types";
            case MEDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONKNOWLEDGE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTAUTHORIZATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCLINICALS: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCONTRAINDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTDEVICESPEC: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINGREDIENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINTERACTION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTMANUFACTURED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPACKAGED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "http://hl7.org/fhir/resource-types";
            case MESSAGEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OBSERVATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATIONAFFILIATION: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONERROLE: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case REQUESTGROUP: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSTUDY: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSUBJECT: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SEQUENCE: return "http://hl7.org/fhir/resource-types";
            case SERVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case SPECIMENDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREMAP: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEPOLYMER: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEREFERENCEINFORMATION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCESPECIFICATION: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TASK: return "http://hl7.org/fhir/resource-types";
            case TERMINOLOGYCAPABILITIES: return "http://hl7.org/fhir/resource-types";
            case TESTREPORT: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case USERSESSION: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VERIFICATIONRESULT: return "http://hl7.org/fhir/resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/resource-types";
            case TYPE: return "http://hl7.org/fhir/abstract-types";
            case ANY: return "http://hl7.org/fhir/abstract-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ADDRESS: return "An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.";
            case AGE: return "A duration of time during which an organism (or a process) has existed.";
            case ANNOTATION: return "A  text note which also  contains information about who made the statement and when.";
            case ATTACHMENT: return "For referring to data content defined in other formats.";
            case BACKBONEELEMENT: return "Base definition for all elements that are defined inside a resource - but not those in a data type.";
            case CODEABLECONCEPT: return "A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.";
            case CODING: return "A reference to a code defined by a terminology system.";
            case CONTACTDETAIL: return "Specifies contact information for a person or organization.";
            case CONTACTPOINT: return "Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.";
            case CONTRIBUTOR: return "A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.";
            case COUNT: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case DATAREQUIREMENT: return "Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.";
            case DISTANCE: return "A length - a value with a unit that is a physical distance.";
            case DOSAGE: return "Indicates how the medication is/was taken or should be taken by the patient.";
            case DURATION: return "A length of time.";
            case ELEMENT: return "Base definition for all elements in a resource.";
            case ELEMENTDEFINITION: return "Captures constraints on each element within the resource, profile, or extension.";
            case EXPRESSION: return "A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.";
            case EXTENSION: return "Optional Extension Element - found in all resources.";
            case HUMANNAME: return "A human's name with the ability to identify parts and usage.";
            case IDENTIFIER: return "An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.";
            case MARKETINGSTATUS: return "The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.";
            case META: return "The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.";
            case MONEY: return "An amount of economic utility in some recognized currency.";
            case MONEYQUANTITY: return "";
            case NARRATIVE: return "A human-readable formatted text, including images.";
            case PARAMETERDEFINITION: return "The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.";
            case PERIOD: return "A time period defined by a start and end date and optionally time.";
            case PRODCHARACTERISTIC: return "The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.";
            case PRODUCTSHELFLIFE: return "The shelf-life and storage information for a medicinal product item or container can be described using this class.";
            case QUANTITY: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case RANGE: return "A set of ordered Quantities defined by a low and high limit.";
            case RATIO: return "A relationship of two Quantity values - expressed as a numerator and a denominator.";
            case REFERENCE: return "A reference from one resource to another.";
            case RELATEDARTIFACT: return "Related artifacts such as additional documentation, justification, or bibliographic references.";
            case SAMPLEDDATA: return "A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.";
            case SIGNATURE: return "A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.";
            case SIMPLEQUANTITY: return "";
            case SUBSTANCEAMOUNT: return "Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.";
            case SUBSTANCEMOIETY: return "Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.";
            case TIMING: return "Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.";
            case TRIGGERDEFINITION: return "A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.";
            case USAGECONTEXT: return "Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).";
            case BASE64BINARY: return "A stream of bytes";
            case BOOLEAN: return "Value of \"true\" or \"false\"";
            case CANONICAL: return "A URI that is a reference to a canonical URI on a FHIR resource";
            case CODE: return "A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents";
            case DATE: return "A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.";
            case DECIMAL: return "A rational number with implicit precision";
            case ID: return "Any combination of letters, numerals, \"-\" and \".\", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.";
            case INSTANT: return "An instant in time - known at least to the second";
            case INTEGER: return "A whole number";
            case MARKDOWN: return "A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine";
            case OID: return "An OID represented as a URI";
            case POSITIVEINT: return "An integer with a value that is positive (e.g. >0)";
            case STRING: return "A sequence of Unicode characters";
            case TIME: return "A time during the day, with no date specified";
            case UNSIGNEDINT: return "An integer with a value that is not negative (e.g. >= 0)";
            case URI: return "String of characters used to identify a name or a resource";
            case URL: return "A URI that is a literal reference";
            case UUID: return "A UUID, represented as a URI";
            case XHTML: return "XHTML format, as defined by W3C, but restricted usage (mainly, no active content)";
            case ACCOUNT: return "A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.";
            case ACTIVITYDEFINITION: return "This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.";
            case ADVERSEEVENT: return "Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.";
            case ALLERGYINTOLERANCE: return "Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.";
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case AUDITEVENT: return "A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.";
            case BASIC: return "Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.";
            case BINARY: return "A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BIOLOGICALLYDERIVEDPRODUCT: return "A material substance originating from a biological entity intended to be transplanted or infused\ninto another (possibly the same) biological entity.";
            case BODYSTRUCTURE: return "Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAPABILITYSTATEMENT: return "A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CARETEAM: return "The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.";
            case CATALOGENTRY: return "Catalog entries are wrappers that contextualize items included in a catalog.";
            case CHARGEITEM: return "The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.";
            case CHARGEITEMDEFINITION: return "The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case CODESYSTEM: return "The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPARTMENTDEFINITION: return "A compartment definition that defines how resources are accessed on a server.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.";
            case CONDITION: return "A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.";
            case CONSENT: return "A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.";
            case CONTRACT: return "Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.";
            case COVERAGE: return "Financial instrument which may be used to reimburse or pay for health care products and services.";
            case COVERAGEELIGIBILITYREQUEST: return "The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.";
            case COVERAGEELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).";
            case DEVICEDEFINITION: return "The characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A collection of documents compiled for a purpose together with metadata that applies to the collection.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case DOMAINRESOURCE: return "A resource that includes narrative, extensions, and contained resources.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENDPOINT: return "The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EVENTDEFINITION: return "The EventDefinition resource provides a reusable description of when a particular event can occur.";
            case EXAMPLESCENARIO: return "Example of workflow instance.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GRAPHDEFINITION: return "A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case GUIDANCERESPONSE: return "A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.";
            case IMMUNIZATIONEVALUATION: return "Describes a comparison of an immunization event against published recommendations to determine if the administration is \"valid\" in relation to those  recommendations.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.";
            case INSURANCEPLAN: return "Details of a Health Insurance product/plan provided by an organization.";
            case INVOICE: return "Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.";
            case ITEMINSTANCE: return "A physical, countable instance of an item, for example one box or one unit.";
            case LIBRARY: return "The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.";
            case LINKAGE: return "Identifies two or more records (resource instances) that are referring to the same real-world \"occurrence\".";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.";
            case MEASURE: return "The Measure resource provides the definition of a quality measure.";
            case MEASUREREPORT: return "The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONKNOWLEDGE: return "Information about a medication that is used to support knowledge.";
            case MEDICATIONREQUEST: return "An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationRequest\" rather than \"MedicationPrescription\" or \"MedicationOrder\" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MEDICINALPRODUCT: return "Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).";
            case MEDICINALPRODUCTAUTHORIZATION: return "The regulatory authorization of a medicinal product.";
            case MEDICINALPRODUCTCLINICALS: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTCONTRAINDICATION: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTDEVICESPEC: return "A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.";
            case MEDICINALPRODUCTINDICATION: return "Indication for the Medicinal Product.";
            case MEDICINALPRODUCTINGREDIENT: return "An ingredient of a manufactured item or pharmaceutical product.";
            case MEDICINALPRODUCTINTERACTION: return "The interactions of the medicinal product with other medicinal products, or other forms of interactions.";
            case MEDICINALPRODUCTMANUFACTURED: return "The manufactured item as contained in the packaged medicinal product.";
            case MEDICINALPRODUCTPACKAGED: return "A medicinal product in a container or package.";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "A pharmaceutical product described in terms of its composition and dose form.";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "Describe the undesirable effects of the medicinal product.";
            case MESSAGEDEFINITION: return "Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OBSERVATIONDEFINITION: return "Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.";
            case ORGANIZATIONAFFILIATION: return "Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PRACTITIONERROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PROCEDURE: return "An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case REQUESTGROUP: return "A group of related requests that can be used to capture intended activities that have inter-dependencies such as \"give this medication after that one\".";
            case RESEARCHSTUDY: return "A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.";
            case RESEARCHSUBJECT: return "A physical entity which is the primary unit of operational and/or administrative interest in a study.";
            case RESOURCE: return "This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slots of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SEQUENCE: return "Raw data describing a biological sequence.";
            case SERVICEREQUEST: return "A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case SPECIMENDEFINITION: return "A kind of specimen with associated set of requirements.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system can take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUBSTANCEPOLYMER: return "Todo.";
            case SUBSTANCEREFERENCEINFORMATION: return "Todo.";
            case SUBSTANCESPECIFICATION: return "The detailed description of a substance, typically at a level beyond what is used for prescribing.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TASK: return "A task to be performed.";
            case TERMINOLOGYCAPABILITIES: return "A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case TESTREPORT: return "A summary of information based on the results of executing a TestScript.";
            case TESTSCRIPT: return "A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.";
            case USERSESSION: return "Information about a user's current session.";
            case VALUESET: return "A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).";
            case VERIFICATIONRESULT: return "Describes validation requirements, source(s), status and dates for one or more elements.";
            case VISIONPRESCRIPTION: return "An authorization for the supply of glasses and/or contact lenses to a patient.";
            case TYPE: return "A place holder that means any kind of data type";
            case ANY: return "A place holder that means any kind of resource";
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
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXPRESSION: return "Expression";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case MARKETINGSTATUS: return "MarketingStatus";
            case META: return "Meta";
            case MONEY: return "Money";
            case MONEYQUANTITY: return "MoneyQuantity";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case PRODCHARACTERISTIC: return "ProdCharacteristic";
            case PRODUCTSHELFLIFE: return "ProductShelfLife";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case SUBSTANCEAMOUNT: return "SubstanceAmount";
            case SUBSTANCEMOIETY: return "SubstanceMoiety";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CANONICAL: return "canonical";
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
            case URL: return "url";
            case UUID: return "uuid";
            case XHTML: return "XHTML";
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INSURANCEPLAN: return "InsurancePlan";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            case TYPE: return "Type";
            case ANY: return "Any";
            default: return "?";
          }
        }
    }

  public static class FHIRAllTypesEnumFactory implements EnumFactory<FHIRAllTypes> {
    public FHIRAllTypes fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Address".equals(codeString))
          return FHIRAllTypes.ADDRESS;
        if ("Age".equals(codeString))
          return FHIRAllTypes.AGE;
        if ("Annotation".equals(codeString))
          return FHIRAllTypes.ANNOTATION;
        if ("Attachment".equals(codeString))
          return FHIRAllTypes.ATTACHMENT;
        if ("BackboneElement".equals(codeString))
          return FHIRAllTypes.BACKBONEELEMENT;
        if ("CodeableConcept".equals(codeString))
          return FHIRAllTypes.CODEABLECONCEPT;
        if ("Coding".equals(codeString))
          return FHIRAllTypes.CODING;
        if ("ContactDetail".equals(codeString))
          return FHIRAllTypes.CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return FHIRAllTypes.CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return FHIRAllTypes.CONTRIBUTOR;
        if ("Count".equals(codeString))
          return FHIRAllTypes.COUNT;
        if ("DataRequirement".equals(codeString))
          return FHIRAllTypes.DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return FHIRAllTypes.DISTANCE;
        if ("Dosage".equals(codeString))
          return FHIRAllTypes.DOSAGE;
        if ("Duration".equals(codeString))
          return FHIRAllTypes.DURATION;
        if ("Element".equals(codeString))
          return FHIRAllTypes.ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return FHIRAllTypes.ELEMENTDEFINITION;
        if ("Expression".equals(codeString))
          return FHIRAllTypes.EXPRESSION;
        if ("Extension".equals(codeString))
          return FHIRAllTypes.EXTENSION;
        if ("HumanName".equals(codeString))
          return FHIRAllTypes.HUMANNAME;
        if ("Identifier".equals(codeString))
          return FHIRAllTypes.IDENTIFIER;
        if ("MarketingStatus".equals(codeString))
          return FHIRAllTypes.MARKETINGSTATUS;
        if ("Meta".equals(codeString))
          return FHIRAllTypes.META;
        if ("Money".equals(codeString))
          return FHIRAllTypes.MONEY;
        if ("MoneyQuantity".equals(codeString))
          return FHIRAllTypes.MONEYQUANTITY;
        if ("Narrative".equals(codeString))
          return FHIRAllTypes.NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return FHIRAllTypes.PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return FHIRAllTypes.PERIOD;
        if ("ProdCharacteristic".equals(codeString))
          return FHIRAllTypes.PRODCHARACTERISTIC;
        if ("ProductShelfLife".equals(codeString))
          return FHIRAllTypes.PRODUCTSHELFLIFE;
        if ("Quantity".equals(codeString))
          return FHIRAllTypes.QUANTITY;
        if ("Range".equals(codeString))
          return FHIRAllTypes.RANGE;
        if ("Ratio".equals(codeString))
          return FHIRAllTypes.RATIO;
        if ("Reference".equals(codeString))
          return FHIRAllTypes.REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return FHIRAllTypes.RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return FHIRAllTypes.SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return FHIRAllTypes.SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return FHIRAllTypes.SIMPLEQUANTITY;
        if ("SubstanceAmount".equals(codeString))
          return FHIRAllTypes.SUBSTANCEAMOUNT;
        if ("SubstanceMoiety".equals(codeString))
          return FHIRAllTypes.SUBSTANCEMOIETY;
        if ("Timing".equals(codeString))
          return FHIRAllTypes.TIMING;
        if ("TriggerDefinition".equals(codeString))
          return FHIRAllTypes.TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return FHIRAllTypes.USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return FHIRAllTypes.BASE64BINARY;
        if ("boolean".equals(codeString))
          return FHIRAllTypes.BOOLEAN;
        if ("canonical".equals(codeString))
          return FHIRAllTypes.CANONICAL;
        if ("code".equals(codeString))
          return FHIRAllTypes.CODE;
        if ("date".equals(codeString))
          return FHIRAllTypes.DATE;
        if ("dateTime".equals(codeString))
          return FHIRAllTypes.DATETIME;
        if ("decimal".equals(codeString))
          return FHIRAllTypes.DECIMAL;
        if ("id".equals(codeString))
          return FHIRAllTypes.ID;
        if ("instant".equals(codeString))
          return FHIRAllTypes.INSTANT;
        if ("integer".equals(codeString))
          return FHIRAllTypes.INTEGER;
        if ("markdown".equals(codeString))
          return FHIRAllTypes.MARKDOWN;
        if ("oid".equals(codeString))
          return FHIRAllTypes.OID;
        if ("positiveInt".equals(codeString))
          return FHIRAllTypes.POSITIVEINT;
        if ("string".equals(codeString))
          return FHIRAllTypes.STRING;
        if ("time".equals(codeString))
          return FHIRAllTypes.TIME;
        if ("unsignedInt".equals(codeString))
          return FHIRAllTypes.UNSIGNEDINT;
        if ("uri".equals(codeString))
          return FHIRAllTypes.URI;
        if ("url".equals(codeString))
          return FHIRAllTypes.URL;
        if ("uuid".equals(codeString))
          return FHIRAllTypes.UUID;
        if ("xhtml".equals(codeString))
          return FHIRAllTypes.XHTML;
        if ("Account".equals(codeString))
          return FHIRAllTypes.ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return FHIRAllTypes.ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return FHIRAllTypes.ADVERSEEVENT;
        if ("AllergyIntolerance".equals(codeString))
          return FHIRAllTypes.ALLERGYINTOLERANCE;
        if ("Appointment".equals(codeString))
          return FHIRAllTypes.APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return FHIRAllTypes.APPOINTMENTRESPONSE;
        if ("AuditEvent".equals(codeString))
          return FHIRAllTypes.AUDITEVENT;
        if ("Basic".equals(codeString))
          return FHIRAllTypes.BASIC;
        if ("Binary".equals(codeString))
          return FHIRAllTypes.BINARY;
        if ("BiologicallyDerivedProduct".equals(codeString))
          return FHIRAllTypes.BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return FHIRAllTypes.BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return FHIRAllTypes.BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return FHIRAllTypes.CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return FHIRAllTypes.CAREPLAN;
        if ("CareTeam".equals(codeString))
          return FHIRAllTypes.CARETEAM;
        if ("CatalogEntry".equals(codeString))
          return FHIRAllTypes.CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return FHIRAllTypes.CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return FHIRAllTypes.CHARGEITEMDEFINITION;
        if ("Claim".equals(codeString))
          return FHIRAllTypes.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return FHIRAllTypes.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return FHIRAllTypes.CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return FHIRAllTypes.CODESYSTEM;
        if ("Communication".equals(codeString))
          return FHIRAllTypes.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return FHIRAllTypes.COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return FHIRAllTypes.COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return FHIRAllTypes.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return FHIRAllTypes.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return FHIRAllTypes.CONDITION;
        if ("Consent".equals(codeString))
          return FHIRAllTypes.CONSENT;
        if ("Contract".equals(codeString))
          return FHIRAllTypes.CONTRACT;
        if ("Coverage".equals(codeString))
          return FHIRAllTypes.COVERAGE;
        if ("CoverageEligibilityRequest".equals(codeString))
          return FHIRAllTypes.COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return FHIRAllTypes.COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return FHIRAllTypes.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return FHIRAllTypes.DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return FHIRAllTypes.DEVICEDEFINITION;
        if ("DeviceMetric".equals(codeString))
          return FHIRAllTypes.DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return FHIRAllTypes.DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return FHIRAllTypes.DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return FHIRAllTypes.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return FHIRAllTypes.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return FHIRAllTypes.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return FHIRAllTypes.DOMAINRESOURCE;
        if ("Encounter".equals(codeString))
          return FHIRAllTypes.ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return FHIRAllTypes.ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return FHIRAllTypes.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return FHIRAllTypes.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return FHIRAllTypes.EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return FHIRAllTypes.EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return FHIRAllTypes.EXAMPLESCENARIO;
        if ("ExplanationOfBenefit".equals(codeString))
          return FHIRAllTypes.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FHIRAllTypes.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FHIRAllTypes.FLAG;
        if ("Goal".equals(codeString))
          return FHIRAllTypes.GOAL;
        if ("GraphDefinition".equals(codeString))
          return FHIRAllTypes.GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return FHIRAllTypes.GROUP;
        if ("GuidanceResponse".equals(codeString))
          return FHIRAllTypes.GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return FHIRAllTypes.HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return FHIRAllTypes.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return FHIRAllTypes.IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return FHIRAllTypes.IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return FHIRAllTypes.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return FHIRAllTypes.IMPLEMENTATIONGUIDE;
        if ("InsurancePlan".equals(codeString))
          return FHIRAllTypes.INSURANCEPLAN;
        if ("Invoice".equals(codeString))
          return FHIRAllTypes.INVOICE;
        if ("ItemInstance".equals(codeString))
          return FHIRAllTypes.ITEMINSTANCE;
        if ("Library".equals(codeString))
          return FHIRAllTypes.LIBRARY;
        if ("Linkage".equals(codeString))
          return FHIRAllTypes.LINKAGE;
        if ("List".equals(codeString))
          return FHIRAllTypes.LIST;
        if ("Location".equals(codeString))
          return FHIRAllTypes.LOCATION;
        if ("Measure".equals(codeString))
          return FHIRAllTypes.MEASURE;
        if ("MeasureReport".equals(codeString))
          return FHIRAllTypes.MEASUREREPORT;
        if ("Media".equals(codeString))
          return FHIRAllTypes.MEDIA;
        if ("Medication".equals(codeString))
          return FHIRAllTypes.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return FHIRAllTypes.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return FHIRAllTypes.MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return FHIRAllTypes.MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return FHIRAllTypes.MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return FHIRAllTypes.MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductContraindication".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return FHIRAllTypes.MEDICINALPRODUCTUNDESIRABLEEFFECT;
        if ("MessageDefinition".equals(codeString))
          return FHIRAllTypes.MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return FHIRAllTypes.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return FHIRAllTypes.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return FHIRAllTypes.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return FHIRAllTypes.OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return FHIRAllTypes.OBSERVATIONDEFINITION;
        if ("OperationDefinition".equals(codeString))
          return FHIRAllTypes.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return FHIRAllTypes.OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return FHIRAllTypes.ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return FHIRAllTypes.ORGANIZATIONAFFILIATION;
        if ("Parameters".equals(codeString))
          return FHIRAllTypes.PARAMETERS;
        if ("Patient".equals(codeString))
          return FHIRAllTypes.PATIENT;
        if ("PaymentNotice".equals(codeString))
          return FHIRAllTypes.PAYMENTNOTICE;
        if ("PaymentReconciliation".equals(codeString))
          return FHIRAllTypes.PAYMENTRECONCILIATION;
        if ("Person".equals(codeString))
          return FHIRAllTypes.PERSON;
        if ("PlanDefinition".equals(codeString))
          return FHIRAllTypes.PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return FHIRAllTypes.PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return FHIRAllTypes.PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return FHIRAllTypes.PROCEDURE;
        if ("ProcessRequest".equals(codeString))
          return FHIRAllTypes.PROCESSREQUEST;
        if ("ProcessResponse".equals(codeString))
          return FHIRAllTypes.PROCESSRESPONSE;
        if ("Provenance".equals(codeString))
          return FHIRAllTypes.PROVENANCE;
        if ("Questionnaire".equals(codeString))
          return FHIRAllTypes.QUESTIONNAIRE;
        if ("QuestionnaireResponse".equals(codeString))
          return FHIRAllTypes.QUESTIONNAIRERESPONSE;
        if ("RelatedPerson".equals(codeString))
          return FHIRAllTypes.RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return FHIRAllTypes.REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return FHIRAllTypes.RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return FHIRAllTypes.RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return FHIRAllTypes.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return FHIRAllTypes.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return FHIRAllTypes.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return FHIRAllTypes.SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return FHIRAllTypes.SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return FHIRAllTypes.SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return FHIRAllTypes.SLOT;
        if ("Specimen".equals(codeString))
          return FHIRAllTypes.SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return FHIRAllTypes.SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return FHIRAllTypes.STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return FHIRAllTypes.STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return FHIRAllTypes.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return FHIRAllTypes.SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return FHIRAllTypes.SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return FHIRAllTypes.SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return FHIRAllTypes.SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return FHIRAllTypes.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return FHIRAllTypes.SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return FHIRAllTypes.TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return FHIRAllTypes.TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return FHIRAllTypes.TESTREPORT;
        if ("TestScript".equals(codeString))
          return FHIRAllTypes.TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return FHIRAllTypes.USERSESSION;
        if ("ValueSet".equals(codeString))
          return FHIRAllTypes.VALUESET;
        if ("VerificationResult".equals(codeString))
          return FHIRAllTypes.VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return FHIRAllTypes.VISIONPRESCRIPTION;
        if ("Type".equals(codeString))
          return FHIRAllTypes.TYPE;
        if ("Any".equals(codeString))
          return FHIRAllTypes.ANY;
        throw new IllegalArgumentException("Unknown FHIRAllTypes code '"+codeString+"'");
        }
        public Enumeration<FHIRAllTypes> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<FHIRAllTypes>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Address".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ADDRESS);
        if ("Age".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.AGE);
        if ("Annotation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ANNOTATION);
        if ("Attachment".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ATTACHMENT);
        if ("BackboneElement".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BACKBONEELEMENT);
        if ("CodeableConcept".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CODEABLECONCEPT);
        if ("Coding".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CODING);
        if ("ContactDetail".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONTACTDETAIL);
        if ("ContactPoint".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONTACTPOINT);
        if ("Contributor".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONTRIBUTOR);
        if ("Count".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COUNT);
        if ("DataRequirement".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DATAREQUIREMENT);
        if ("Distance".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DISTANCE);
        if ("Dosage".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DOSAGE);
        if ("Duration".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DURATION);
        if ("Element".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ELEMENT);
        if ("ElementDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ELEMENTDEFINITION);
        if ("Expression".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.EXPRESSION);
        if ("Extension".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.EXTENSION);
        if ("HumanName".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.HUMANNAME);
        if ("Identifier".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.IDENTIFIER);
        if ("MarketingStatus".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MARKETINGSTATUS);
        if ("Meta".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.META);
        if ("Money".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MONEY);
        if ("MoneyQuantity".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MONEYQUANTITY);
        if ("Narrative".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.NARRATIVE);
        if ("ParameterDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PARAMETERDEFINITION);
        if ("Period".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PERIOD);
        if ("ProdCharacteristic".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PRODCHARACTERISTIC);
        if ("ProductShelfLife".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PRODUCTSHELFLIFE);
        if ("Quantity".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.QUANTITY);
        if ("Range".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RANGE);
        if ("Ratio".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RATIO);
        if ("Reference".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.REFERENCE);
        if ("RelatedArtifact".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RELATEDARTIFACT);
        if ("SampledData".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SAMPLEDDATA);
        if ("Signature".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SIGNATURE);
        if ("SimpleQuantity".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SIMPLEQUANTITY);
        if ("SubstanceAmount".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSTANCEAMOUNT);
        if ("SubstanceMoiety".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSTANCEMOIETY);
        if ("Timing".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TIMING);
        if ("TriggerDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TRIGGERDEFINITION);
        if ("UsageContext".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.USAGECONTEXT);
        if ("base64Binary".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BASE64BINARY);
        if ("boolean".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BOOLEAN);
        if ("canonical".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CANONICAL);
        if ("code".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CODE);
        if ("date".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DATE);
        if ("dateTime".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DATETIME);
        if ("decimal".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DECIMAL);
        if ("id".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ID);
        if ("instant".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.INSTANT);
        if ("integer".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.INTEGER);
        if ("markdown".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MARKDOWN);
        if ("oid".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.OID);
        if ("positiveInt".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.POSITIVEINT);
        if ("string".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.STRING);
        if ("time".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TIME);
        if ("unsignedInt".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.UNSIGNEDINT);
        if ("uri".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.URI);
        if ("url".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.URL);
        if ("uuid".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.UUID);
        if ("xhtml".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.XHTML);
        if ("Account".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ACCOUNT);
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ACTIVITYDEFINITION);
        if ("AdverseEvent".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ADVERSEEVENT);
        if ("AllergyIntolerance".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ALLERGYINTOLERANCE);
        if ("Appointment".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.APPOINTMENT);
        if ("AppointmentResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.APPOINTMENTRESPONSE);
        if ("AuditEvent".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.AUDITEVENT);
        if ("Basic".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BASIC);
        if ("Binary".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BINARY);
        if ("BiologicallyDerivedProduct".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BIOLOGICALLYDERIVEDPRODUCT);
        if ("BodyStructure".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BODYSTRUCTURE);
        if ("Bundle".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.BUNDLE);
        if ("CapabilityStatement".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CAPABILITYSTATEMENT);
        if ("CarePlan".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CAREPLAN);
        if ("CareTeam".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CARETEAM);
        if ("CatalogEntry".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CATALOGENTRY);
        if ("ChargeItem".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CHARGEITEM);
        if ("ChargeItemDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CHARGEITEMDEFINITION);
        if ("Claim".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CLAIM);
        if ("ClaimResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CLAIMRESPONSE);
        if ("ClinicalImpression".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CLINICALIMPRESSION);
        if ("CodeSystem".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CODESYSTEM);
        if ("Communication".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COMMUNICATION);
        if ("CommunicationRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COMMUNICATIONREQUEST);
        if ("CompartmentDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COMPARTMENTDEFINITION);
        if ("Composition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COMPOSITION);
        if ("ConceptMap".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONCEPTMAP);
        if ("Condition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONDITION);
        if ("Consent".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONSENT);
        if ("Contract".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.CONTRACT);
        if ("Coverage".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COVERAGE);
        if ("CoverageEligibilityRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COVERAGEELIGIBILITYREQUEST);
        if ("CoverageEligibilityResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.COVERAGEELIGIBILITYRESPONSE);
        if ("DetectedIssue".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DETECTEDISSUE);
        if ("Device".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DEVICE);
        if ("DeviceDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DEVICEDEFINITION);
        if ("DeviceMetric".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DEVICEMETRIC);
        if ("DeviceRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DEVICEREQUEST);
        if ("DeviceUseStatement".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DEVICEUSESTATEMENT);
        if ("DiagnosticReport".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DIAGNOSTICREPORT);
        if ("DocumentManifest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DOCUMENTMANIFEST);
        if ("DocumentReference".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DOCUMENTREFERENCE);
        if ("DomainResource".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.DOMAINRESOURCE);
        if ("Encounter".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ENCOUNTER);
        if ("Endpoint".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ENDPOINT);
        if ("EnrollmentRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ENROLLMENTREQUEST);
        if ("EnrollmentResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ENROLLMENTRESPONSE);
        if ("EpisodeOfCare".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.EPISODEOFCARE);
        if ("EventDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.EVENTDEFINITION);
        if ("ExampleScenario".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.EXAMPLESCENARIO);
        if ("ExplanationOfBenefit".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.EXPLANATIONOFBENEFIT);
        if ("FamilyMemberHistory".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.FAMILYMEMBERHISTORY);
        if ("Flag".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.FLAG);
        if ("Goal".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.GOAL);
        if ("GraphDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.GRAPHDEFINITION);
        if ("Group".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.GROUP);
        if ("GuidanceResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.GUIDANCERESPONSE);
        if ("HealthcareService".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.HEALTHCARESERVICE);
        if ("ImagingStudy".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.IMAGINGSTUDY);
        if ("Immunization".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.IMMUNIZATION);
        if ("ImmunizationEvaluation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.IMMUNIZATIONEVALUATION);
        if ("ImmunizationRecommendation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.IMMUNIZATIONRECOMMENDATION);
        if ("ImplementationGuide".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.IMPLEMENTATIONGUIDE);
        if ("InsurancePlan".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.INSURANCEPLAN);
        if ("Invoice".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.INVOICE);
        if ("ItemInstance".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ITEMINSTANCE);
        if ("Library".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.LIBRARY);
        if ("Linkage".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.LINKAGE);
        if ("List".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.LIST);
        if ("Location".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.LOCATION);
        if ("Measure".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEASURE);
        if ("MeasureReport".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEASUREREPORT);
        if ("Media".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDIA);
        if ("Medication".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICATION);
        if ("MedicationAdministration".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICATIONADMINISTRATION);
        if ("MedicationDispense".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICATIONDISPENSE);
        if ("MedicationKnowledge".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICATIONKNOWLEDGE);
        if ("MedicationRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICATIONREQUEST);
        if ("MedicationStatement".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICATIONSTATEMENT);
        if ("MedicinalProduct".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCT);
        if ("MedicinalProductAuthorization".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTAUTHORIZATION);
        if ("MedicinalProductClinicals".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTCLINICALS);
        if ("MedicinalProductContraindication".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTCONTRAINDICATION);
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTDEVICESPEC);
        if ("MedicinalProductIndication".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTINDICATION);
        if ("MedicinalProductIngredient".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTINGREDIENT);
        if ("MedicinalProductInteraction".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTINTERACTION);
        if ("MedicinalProductManufactured".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTMANUFACTURED);
        if ("MedicinalProductPackaged".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTPACKAGED);
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTPHARMACEUTICAL);
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MEDICINALPRODUCTUNDESIRABLEEFFECT);
        if ("MessageDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MESSAGEDEFINITION);
        if ("MessageHeader".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.MESSAGEHEADER);
        if ("NamingSystem".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.NAMINGSYSTEM);
        if ("NutritionOrder".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.NUTRITIONORDER);
        if ("Observation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.OBSERVATION);
        if ("ObservationDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.OBSERVATIONDEFINITION);
        if ("OperationDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.OPERATIONDEFINITION);
        if ("OperationOutcome".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.OPERATIONOUTCOME);
        if ("Organization".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ORGANIZATION);
        if ("OrganizationAffiliation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ORGANIZATIONAFFILIATION);
        if ("Parameters".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PARAMETERS);
        if ("Patient".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PATIENT);
        if ("PaymentNotice".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PAYMENTNOTICE);
        if ("PaymentReconciliation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PAYMENTRECONCILIATION);
        if ("Person".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PERSON);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PLANDEFINITION);
        if ("Practitioner".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PRACTITIONER);
        if ("PractitionerRole".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PRACTITIONERROLE);
        if ("Procedure".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PROCEDURE);
        if ("ProcessRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PROCESSREQUEST);
        if ("ProcessResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PROCESSRESPONSE);
        if ("Provenance".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.PROVENANCE);
        if ("Questionnaire".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.QUESTIONNAIRE);
        if ("QuestionnaireResponse".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.QUESTIONNAIRERESPONSE);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RELATEDPERSON);
        if ("RequestGroup".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.REQUESTGROUP);
        if ("ResearchStudy".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RESEARCHSTUDY);
        if ("ResearchSubject".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RESEARCHSUBJECT);
        if ("Resource".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RESOURCE);
        if ("RiskAssessment".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.RISKASSESSMENT);
        if ("Schedule".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SCHEDULE);
        if ("SearchParameter".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SEARCHPARAMETER);
        if ("Sequence".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SEQUENCE);
        if ("ServiceRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SERVICEREQUEST);
        if ("Slot".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SLOT);
        if ("Specimen".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SPECIMEN);
        if ("SpecimenDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SPECIMENDEFINITION);
        if ("StructureDefinition".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.STRUCTUREDEFINITION);
        if ("StructureMap".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.STRUCTUREMAP);
        if ("Subscription".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSCRIPTION);
        if ("Substance".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSTANCE);
        if ("SubstancePolymer".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSTANCEPOLYMER);
        if ("SubstanceReferenceInformation".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSTANCEREFERENCEINFORMATION);
        if ("SubstanceSpecification".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUBSTANCESPECIFICATION);
        if ("SupplyDelivery".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUPPLYDELIVERY);
        if ("SupplyRequest".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.SUPPLYREQUEST);
        if ("Task".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TASK);
        if ("TerminologyCapabilities".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TERMINOLOGYCAPABILITIES);
        if ("TestReport".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TESTREPORT);
        if ("TestScript".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TESTSCRIPT);
        if ("UserSession".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.USERSESSION);
        if ("ValueSet".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.VALUESET);
        if ("VerificationResult".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.VERIFICATIONRESULT);
        if ("VisionPrescription".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.VISIONPRESCRIPTION);
        if ("Type".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.TYPE);
        if ("Any".equals(codeString))
          return new Enumeration<FHIRAllTypes>(this, FHIRAllTypes.ANY);
        throw new FHIRException("Unknown FHIRAllTypes code '"+codeString+"'");
        }
    public String toCode(FHIRAllTypes code) {
      if (code == FHIRAllTypes.ADDRESS)
        return "Address";
      if (code == FHIRAllTypes.AGE)
        return "Age";
      if (code == FHIRAllTypes.ANNOTATION)
        return "Annotation";
      if (code == FHIRAllTypes.ATTACHMENT)
        return "Attachment";
      if (code == FHIRAllTypes.BACKBONEELEMENT)
        return "BackboneElement";
      if (code == FHIRAllTypes.CODEABLECONCEPT)
        return "CodeableConcept";
      if (code == FHIRAllTypes.CODING)
        return "Coding";
      if (code == FHIRAllTypes.CONTACTDETAIL)
        return "ContactDetail";
      if (code == FHIRAllTypes.CONTACTPOINT)
        return "ContactPoint";
      if (code == FHIRAllTypes.CONTRIBUTOR)
        return "Contributor";
      if (code == FHIRAllTypes.COUNT)
        return "Count";
      if (code == FHIRAllTypes.DATAREQUIREMENT)
        return "DataRequirement";
      if (code == FHIRAllTypes.DISTANCE)
        return "Distance";
      if (code == FHIRAllTypes.DOSAGE)
        return "Dosage";
      if (code == FHIRAllTypes.DURATION)
        return "Duration";
      if (code == FHIRAllTypes.ELEMENT)
        return "Element";
      if (code == FHIRAllTypes.ELEMENTDEFINITION)
        return "ElementDefinition";
      if (code == FHIRAllTypes.EXPRESSION)
        return "Expression";
      if (code == FHIRAllTypes.EXTENSION)
        return "Extension";
      if (code == FHIRAllTypes.HUMANNAME)
        return "HumanName";
      if (code == FHIRAllTypes.IDENTIFIER)
        return "Identifier";
      if (code == FHIRAllTypes.MARKETINGSTATUS)
        return "MarketingStatus";
      if (code == FHIRAllTypes.META)
        return "Meta";
      if (code == FHIRAllTypes.MONEY)
        return "Money";
      if (code == FHIRAllTypes.MONEYQUANTITY)
        return "MoneyQuantity";
      if (code == FHIRAllTypes.NARRATIVE)
        return "Narrative";
      if (code == FHIRAllTypes.PARAMETERDEFINITION)
        return "ParameterDefinition";
      if (code == FHIRAllTypes.PERIOD)
        return "Period";
      if (code == FHIRAllTypes.PRODCHARACTERISTIC)
        return "ProdCharacteristic";
      if (code == FHIRAllTypes.PRODUCTSHELFLIFE)
        return "ProductShelfLife";
      if (code == FHIRAllTypes.QUANTITY)
        return "Quantity";
      if (code == FHIRAllTypes.RANGE)
        return "Range";
      if (code == FHIRAllTypes.RATIO)
        return "Ratio";
      if (code == FHIRAllTypes.REFERENCE)
        return "Reference";
      if (code == FHIRAllTypes.RELATEDARTIFACT)
        return "RelatedArtifact";
      if (code == FHIRAllTypes.SAMPLEDDATA)
        return "SampledData";
      if (code == FHIRAllTypes.SIGNATURE)
        return "Signature";
      if (code == FHIRAllTypes.SIMPLEQUANTITY)
        return "SimpleQuantity";
      if (code == FHIRAllTypes.SUBSTANCEAMOUNT)
        return "SubstanceAmount";
      if (code == FHIRAllTypes.SUBSTANCEMOIETY)
        return "SubstanceMoiety";
      if (code == FHIRAllTypes.TIMING)
        return "Timing";
      if (code == FHIRAllTypes.TRIGGERDEFINITION)
        return "TriggerDefinition";
      if (code == FHIRAllTypes.USAGECONTEXT)
        return "UsageContext";
      if (code == FHIRAllTypes.BASE64BINARY)
        return "base64Binary";
      if (code == FHIRAllTypes.BOOLEAN)
        return "boolean";
      if (code == FHIRAllTypes.CANONICAL)
        return "canonical";
      if (code == FHIRAllTypes.CODE)
        return "code";
      if (code == FHIRAllTypes.DATE)
        return "date";
      if (code == FHIRAllTypes.DATETIME)
        return "dateTime";
      if (code == FHIRAllTypes.DECIMAL)
        return "decimal";
      if (code == FHIRAllTypes.ID)
        return "id";
      if (code == FHIRAllTypes.INSTANT)
        return "instant";
      if (code == FHIRAllTypes.INTEGER)
        return "integer";
      if (code == FHIRAllTypes.MARKDOWN)
        return "markdown";
      if (code == FHIRAllTypes.OID)
        return "oid";
      if (code == FHIRAllTypes.POSITIVEINT)
        return "positiveInt";
      if (code == FHIRAllTypes.STRING)
        return "string";
      if (code == FHIRAllTypes.TIME)
        return "time";
      if (code == FHIRAllTypes.UNSIGNEDINT)
        return "unsignedInt";
      if (code == FHIRAllTypes.URI)
        return "uri";
      if (code == FHIRAllTypes.URL)
        return "url";
      if (code == FHIRAllTypes.UUID)
        return "uuid";
      if (code == FHIRAllTypes.XHTML)
        return "xhtml";
      if (code == FHIRAllTypes.ACCOUNT)
        return "Account";
      if (code == FHIRAllTypes.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == FHIRAllTypes.ADVERSEEVENT)
        return "AdverseEvent";
      if (code == FHIRAllTypes.ALLERGYINTOLERANCE)
        return "AllergyIntolerance";
      if (code == FHIRAllTypes.APPOINTMENT)
        return "Appointment";
      if (code == FHIRAllTypes.APPOINTMENTRESPONSE)
        return "AppointmentResponse";
      if (code == FHIRAllTypes.AUDITEVENT)
        return "AuditEvent";
      if (code == FHIRAllTypes.BASIC)
        return "Basic";
      if (code == FHIRAllTypes.BINARY)
        return "Binary";
      if (code == FHIRAllTypes.BIOLOGICALLYDERIVEDPRODUCT)
        return "BiologicallyDerivedProduct";
      if (code == FHIRAllTypes.BODYSTRUCTURE)
        return "BodyStructure";
      if (code == FHIRAllTypes.BUNDLE)
        return "Bundle";
      if (code == FHIRAllTypes.CAPABILITYSTATEMENT)
        return "CapabilityStatement";
      if (code == FHIRAllTypes.CAREPLAN)
        return "CarePlan";
      if (code == FHIRAllTypes.CARETEAM)
        return "CareTeam";
      if (code == FHIRAllTypes.CATALOGENTRY)
        return "CatalogEntry";
      if (code == FHIRAllTypes.CHARGEITEM)
        return "ChargeItem";
      if (code == FHIRAllTypes.CHARGEITEMDEFINITION)
        return "ChargeItemDefinition";
      if (code == FHIRAllTypes.CLAIM)
        return "Claim";
      if (code == FHIRAllTypes.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == FHIRAllTypes.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == FHIRAllTypes.CODESYSTEM)
        return "CodeSystem";
      if (code == FHIRAllTypes.COMMUNICATION)
        return "Communication";
      if (code == FHIRAllTypes.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == FHIRAllTypes.COMPARTMENTDEFINITION)
        return "CompartmentDefinition";
      if (code == FHIRAllTypes.COMPOSITION)
        return "Composition";
      if (code == FHIRAllTypes.CONCEPTMAP)
        return "ConceptMap";
      if (code == FHIRAllTypes.CONDITION)
        return "Condition";
      if (code == FHIRAllTypes.CONSENT)
        return "Consent";
      if (code == FHIRAllTypes.CONTRACT)
        return "Contract";
      if (code == FHIRAllTypes.COVERAGE)
        return "Coverage";
      if (code == FHIRAllTypes.COVERAGEELIGIBILITYREQUEST)
        return "CoverageEligibilityRequest";
      if (code == FHIRAllTypes.COVERAGEELIGIBILITYRESPONSE)
        return "CoverageEligibilityResponse";
      if (code == FHIRAllTypes.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == FHIRAllTypes.DEVICE)
        return "Device";
      if (code == FHIRAllTypes.DEVICEDEFINITION)
        return "DeviceDefinition";
      if (code == FHIRAllTypes.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == FHIRAllTypes.DEVICEREQUEST)
        return "DeviceRequest";
      if (code == FHIRAllTypes.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == FHIRAllTypes.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == FHIRAllTypes.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == FHIRAllTypes.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == FHIRAllTypes.DOMAINRESOURCE)
        return "DomainResource";
      if (code == FHIRAllTypes.ENCOUNTER)
        return "Encounter";
      if (code == FHIRAllTypes.ENDPOINT)
        return "Endpoint";
      if (code == FHIRAllTypes.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == FHIRAllTypes.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == FHIRAllTypes.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == FHIRAllTypes.EVENTDEFINITION)
        return "EventDefinition";
      if (code == FHIRAllTypes.EXAMPLESCENARIO)
        return "ExampleScenario";
      if (code == FHIRAllTypes.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == FHIRAllTypes.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == FHIRAllTypes.FLAG)
        return "Flag";
      if (code == FHIRAllTypes.GOAL)
        return "Goal";
      if (code == FHIRAllTypes.GRAPHDEFINITION)
        return "GraphDefinition";
      if (code == FHIRAllTypes.GROUP)
        return "Group";
      if (code == FHIRAllTypes.GUIDANCERESPONSE)
        return "GuidanceResponse";
      if (code == FHIRAllTypes.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == FHIRAllTypes.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == FHIRAllTypes.IMMUNIZATION)
        return "Immunization";
      if (code == FHIRAllTypes.IMMUNIZATIONEVALUATION)
        return "ImmunizationEvaluation";
      if (code == FHIRAllTypes.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == FHIRAllTypes.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == FHIRAllTypes.INSURANCEPLAN)
        return "InsurancePlan";
      if (code == FHIRAllTypes.INVOICE)
        return "Invoice";
      if (code == FHIRAllTypes.ITEMINSTANCE)
        return "ItemInstance";
      if (code == FHIRAllTypes.LIBRARY)
        return "Library";
      if (code == FHIRAllTypes.LINKAGE)
        return "Linkage";
      if (code == FHIRAllTypes.LIST)
        return "List";
      if (code == FHIRAllTypes.LOCATION)
        return "Location";
      if (code == FHIRAllTypes.MEASURE)
        return "Measure";
      if (code == FHIRAllTypes.MEASUREREPORT)
        return "MeasureReport";
      if (code == FHIRAllTypes.MEDIA)
        return "Media";
      if (code == FHIRAllTypes.MEDICATION)
        return "Medication";
      if (code == FHIRAllTypes.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == FHIRAllTypes.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == FHIRAllTypes.MEDICATIONKNOWLEDGE)
        return "MedicationKnowledge";
      if (code == FHIRAllTypes.MEDICATIONREQUEST)
        return "MedicationRequest";
      if (code == FHIRAllTypes.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == FHIRAllTypes.MEDICINALPRODUCT)
        return "MedicinalProduct";
      if (code == FHIRAllTypes.MEDICINALPRODUCTAUTHORIZATION)
        return "MedicinalProductAuthorization";
      if (code == FHIRAllTypes.MEDICINALPRODUCTCLINICALS)
        return "MedicinalProductClinicals";
      if (code == FHIRAllTypes.MEDICINALPRODUCTCONTRAINDICATION)
        return "MedicinalProductContraindication";
      if (code == FHIRAllTypes.MEDICINALPRODUCTDEVICESPEC)
        return "MedicinalProductDeviceSpec";
      if (code == FHIRAllTypes.MEDICINALPRODUCTINDICATION)
        return "MedicinalProductIndication";
      if (code == FHIRAllTypes.MEDICINALPRODUCTINGREDIENT)
        return "MedicinalProductIngredient";
      if (code == FHIRAllTypes.MEDICINALPRODUCTINTERACTION)
        return "MedicinalProductInteraction";
      if (code == FHIRAllTypes.MEDICINALPRODUCTMANUFACTURED)
        return "MedicinalProductManufactured";
      if (code == FHIRAllTypes.MEDICINALPRODUCTPACKAGED)
        return "MedicinalProductPackaged";
      if (code == FHIRAllTypes.MEDICINALPRODUCTPHARMACEUTICAL)
        return "MedicinalProductPharmaceutical";
      if (code == FHIRAllTypes.MEDICINALPRODUCTUNDESIRABLEEFFECT)
        return "MedicinalProductUndesirableEffect";
      if (code == FHIRAllTypes.MESSAGEDEFINITION)
        return "MessageDefinition";
      if (code == FHIRAllTypes.MESSAGEHEADER)
        return "MessageHeader";
      if (code == FHIRAllTypes.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == FHIRAllTypes.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == FHIRAllTypes.OBSERVATION)
        return "Observation";
      if (code == FHIRAllTypes.OBSERVATIONDEFINITION)
        return "ObservationDefinition";
      if (code == FHIRAllTypes.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == FHIRAllTypes.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == FHIRAllTypes.ORGANIZATION)
        return "Organization";
      if (code == FHIRAllTypes.ORGANIZATIONAFFILIATION)
        return "OrganizationAffiliation";
      if (code == FHIRAllTypes.PARAMETERS)
        return "Parameters";
      if (code == FHIRAllTypes.PATIENT)
        return "Patient";
      if (code == FHIRAllTypes.PAYMENTNOTICE)
        return "PaymentNotice";
      if (code == FHIRAllTypes.PAYMENTRECONCILIATION)
        return "PaymentReconciliation";
      if (code == FHIRAllTypes.PERSON)
        return "Person";
      if (code == FHIRAllTypes.PLANDEFINITION)
        return "PlanDefinition";
      if (code == FHIRAllTypes.PRACTITIONER)
        return "Practitioner";
      if (code == FHIRAllTypes.PRACTITIONERROLE)
        return "PractitionerRole";
      if (code == FHIRAllTypes.PROCEDURE)
        return "Procedure";
      if (code == FHIRAllTypes.PROCESSREQUEST)
        return "ProcessRequest";
      if (code == FHIRAllTypes.PROCESSRESPONSE)
        return "ProcessResponse";
      if (code == FHIRAllTypes.PROVENANCE)
        return "Provenance";
      if (code == FHIRAllTypes.QUESTIONNAIRE)
        return "Questionnaire";
      if (code == FHIRAllTypes.QUESTIONNAIRERESPONSE)
        return "QuestionnaireResponse";
      if (code == FHIRAllTypes.RELATEDPERSON)
        return "RelatedPerson";
      if (code == FHIRAllTypes.REQUESTGROUP)
        return "RequestGroup";
      if (code == FHIRAllTypes.RESEARCHSTUDY)
        return "ResearchStudy";
      if (code == FHIRAllTypes.RESEARCHSUBJECT)
        return "ResearchSubject";
      if (code == FHIRAllTypes.RESOURCE)
        return "Resource";
      if (code == FHIRAllTypes.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == FHIRAllTypes.SCHEDULE)
        return "Schedule";
      if (code == FHIRAllTypes.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == FHIRAllTypes.SEQUENCE)
        return "Sequence";
      if (code == FHIRAllTypes.SERVICEREQUEST)
        return "ServiceRequest";
      if (code == FHIRAllTypes.SLOT)
        return "Slot";
      if (code == FHIRAllTypes.SPECIMEN)
        return "Specimen";
      if (code == FHIRAllTypes.SPECIMENDEFINITION)
        return "SpecimenDefinition";
      if (code == FHIRAllTypes.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == FHIRAllTypes.STRUCTUREMAP)
        return "StructureMap";
      if (code == FHIRAllTypes.SUBSCRIPTION)
        return "Subscription";
      if (code == FHIRAllTypes.SUBSTANCE)
        return "Substance";
      if (code == FHIRAllTypes.SUBSTANCEPOLYMER)
        return "SubstancePolymer";
      if (code == FHIRAllTypes.SUBSTANCEREFERENCEINFORMATION)
        return "SubstanceReferenceInformation";
      if (code == FHIRAllTypes.SUBSTANCESPECIFICATION)
        return "SubstanceSpecification";
      if (code == FHIRAllTypes.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == FHIRAllTypes.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == FHIRAllTypes.TASK)
        return "Task";
      if (code == FHIRAllTypes.TERMINOLOGYCAPABILITIES)
        return "TerminologyCapabilities";
      if (code == FHIRAllTypes.TESTREPORT)
        return "TestReport";
      if (code == FHIRAllTypes.TESTSCRIPT)
        return "TestScript";
      if (code == FHIRAllTypes.USERSESSION)
        return "UserSession";
      if (code == FHIRAllTypes.VALUESET)
        return "ValueSet";
      if (code == FHIRAllTypes.VERIFICATIONRESULT)
        return "VerificationResult";
      if (code == FHIRAllTypes.VISIONPRESCRIPTION)
        return "VisionPrescription";
      if (code == FHIRAllTypes.TYPE)
        return "Type";
      if (code == FHIRAllTypes.ANY)
        return "Any";
      return "?";
      }
    public String toSystem(FHIRAllTypes code) {
      return code.getSystem();
      }
    }

    public enum FHIRDefinedType {
        /**
         * An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.
         */
        ADDRESS, 
        /**
         * A duration of time during which an organism (or a process) has existed.
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
         * Specifies contact information for a person or organization.
         */
        CONTACTDETAIL, 
        /**
         * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
         */
        CONTACTPOINT, 
        /**
         * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
         */
        CONTRIBUTOR, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        COUNT, 
        /**
         * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.
         */
        DATAREQUIREMENT, 
        /**
         * A length - a value with a unit that is a physical distance.
         */
        DISTANCE, 
        /**
         * Indicates how the medication is/was taken or should be taken by the patient.
         */
        DOSAGE, 
        /**
         * A length of time.
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
         * A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.
         */
        EXPRESSION, 
        /**
         * Optional Extension Element - found in all resources.
         */
        EXTENSION, 
        /**
         * A human's name with the ability to identify parts and usage.
         */
        HUMANNAME, 
        /**
         * An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
         */
        IDENTIFIER, 
        /**
         * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
         */
        MARKETINGSTATUS, 
        /**
         * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.
         */
        META, 
        /**
         * An amount of economic utility in some recognized currency.
         */
        MONEY, 
        /**
         * null
         */
        MONEYQUANTITY, 
        /**
         * A human-readable formatted text, including images.
         */
        NARRATIVE, 
        /**
         * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
         */
        PARAMETERDEFINITION, 
        /**
         * A time period defined by a start and end date and optionally time.
         */
        PERIOD, 
        /**
         * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
         */
        PRODCHARACTERISTIC, 
        /**
         * The shelf-life and storage information for a medicinal product item or container can be described using this class.
         */
        PRODUCTSHELFLIFE, 
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
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         */
        RELATEDARTIFACT, 
        /**
         * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
         */
        SAMPLEDDATA, 
        /**
         * A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
         */
        SIGNATURE, 
        /**
         * null
         */
        SIMPLEQUANTITY, 
        /**
         * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
         */
        SUBSTANCEAMOUNT, 
        /**
         * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
         */
        SUBSTANCEMOIETY, 
        /**
         * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.
         */
        TIMING, 
        /**
         * A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.
         */
        TRIGGERDEFINITION, 
        /**
         * Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).
         */
        USAGECONTEXT, 
        /**
         * A stream of bytes
         */
        BASE64BINARY, 
        /**
         * Value of "true" or "false"
         */
        BOOLEAN, 
        /**
         * A URI that is a reference to a canonical URI on a FHIR resource
         */
        CANONICAL, 
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
         * A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN, 
        /**
         * An OID represented as a URI
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
         * A URI that is a literal reference
         */
        URL, 
        /**
         * A UUID, represented as a URI
         */
        UUID, 
        /**
         * XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
         */
        XHTML, 
        /**
         * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.
         */
        ACCOUNT, 
        /**
         * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
         */
        ACTIVITYDEFINITION, 
        /**
         * Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.
         */
        ADVERSEEVENT, 
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
         * A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY, 
        /**
         * A material substance originating from a biological entity intended to be transplanted or infused
into another (possibly the same) biological entity.
         */
        BIOLOGICALLYDERIVEDPRODUCT, 
        /**
         * Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSTRUCTURE, 
        /**
         * A container for a collection of resources.
         */
        BUNDLE, 
        /**
         * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        CAPABILITYSTATEMENT, 
        /**
         * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CAREPLAN, 
        /**
         * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.
         */
        CARETEAM, 
        /**
         * Catalog entries are wrappers that contextualize items included in a catalog.
         */
        CATALOGENTRY, 
        /**
         * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.
         */
        CHARGEITEM, 
        /**
         * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.
         */
        CHARGEITEMDEFINITION, 
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
         * The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.
         */
        CODESYSTEM, 
        /**
         * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
         */
        COMMUNICATION, 
        /**
         * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATIONREQUEST, 
        /**
         * A compartment definition that defines how resources are accessed on a server.
         */
        COMPARTMENTDEFINITION, 
        /**
         * A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).
         */
        COMPOSITION, 
        /**
         * A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.
         */
        CONCEPTMAP, 
        /**
         * A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.
         */
        CONDITION, 
        /**
         * A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
         */
        CONSENT, 
        /**
         * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to reimburse or pay for health care products and services.
         */
        COVERAGE, 
        /**
         * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
         */
        COVERAGEELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
         */
        COVERAGEELIGIBILITYRESPONSE, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).
         */
        DEVICE, 
        /**
         * The characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICEDEFINITION, 
        /**
         * Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICEMETRIC, 
        /**
         * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
         */
        DEVICEREQUEST, 
        /**
         * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
         */
        DEVICEUSESTATEMENT, 
        /**
         * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A collection of documents compiled for a purpose together with metadata that applies to the collection.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document.
         */
        DOCUMENTREFERENCE, 
        /**
         * A resource that includes narrative, extensions, and contained resources.
         */
        DOMAINRESOURCE, 
        /**
         * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
         */
        ENCOUNTER, 
        /**
         * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
         */
        ENDPOINT, 
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
         * The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENTDEFINITION, 
        /**
         * Example of workflow instance.
         */
        EXAMPLESCENARIO, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health conditions for a person related to the patient relevant in the context of care for the patient.
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
         * A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.
         */
        GRAPHDEFINITION, 
        /**
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
         */
        GROUP, 
        /**
         * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
         */
        GUIDANCERESPONSE, 
        /**
         * The details of a healthcare service available at a location.
         */
        HEALTHCARESERVICE, 
        /**
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.
         */
        IMMUNIZATION, 
        /**
         * Describes a comparison of an immunization event against published recommendations to determine if the administration is "valid" in relation to those  recommendations.
         */
        IMMUNIZATIONEVALUATION, 
        /**
         * A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * Details of a Health Insurance product/plan provided by an organization.
         */
        INSURANCEPLAN, 
        /**
         * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.
         */
        INVOICE, 
        /**
         * A physical, countable instance of an item, for example one box or one unit.
         */
        ITEMINSTANCE, 
        /**
         * The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.
         */
        LIBRARY, 
        /**
         * Identifies two or more records (resource instances) that are referring to the same real-world "occurrence".
         */
        LINKAGE, 
        /**
         * A set of information summarized from a list of other resources.
         */
        LIST, 
        /**
         * Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.
         */
        LOCATION, 
        /**
         * The Measure resource provides the definition of a quality measure.
         */
        MEASURE, 
        /**
         * The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.
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
         * Information about a medication that is used to support knowledge.
         */
        MEDICATIONKNOWLEDGE, 
        /**
         * An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.
         */
        MEDICATIONREQUEST, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
         */
        MEDICINALPRODUCT, 
        /**
         * The regulatory authorization of a medicinal product.
         */
        MEDICINALPRODUCTAUTHORIZATION, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCLINICALS, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCONTRAINDICATION, 
        /**
         * A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.
         */
        MEDICINALPRODUCTDEVICESPEC, 
        /**
         * Indication for the Medicinal Product.
         */
        MEDICINALPRODUCTINDICATION, 
        /**
         * An ingredient of a manufactured item or pharmaceutical product.
         */
        MEDICINALPRODUCTINGREDIENT, 
        /**
         * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
         */
        MEDICINALPRODUCTINTERACTION, 
        /**
         * The manufactured item as contained in the packaged medicinal product.
         */
        MEDICINALPRODUCTMANUFACTURED, 
        /**
         * A medicinal product in a container or package.
         */
        MEDICINALPRODUCTPACKAGED, 
        /**
         * A pharmaceutical product described in terms of its composition and dose form.
         */
        MEDICINALPRODUCTPHARMACEUTICAL, 
        /**
         * Describe the undesirable effects of the medicinal product.
         */
        MEDICINALPRODUCTUNDESIRABLEEFFECT, 
        /**
         * Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.
         */
        MESSAGEDEFINITION, 
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
         * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.
         */
        OBSERVATIONDEFINITION, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.
         */
        ORGANIZATION, 
        /**
         * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.
         */
        ORGANIZATIONAFFILIATION, 
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
         * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.
         */
        PLANDEFINITION, 
        /**
         * A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.
         */
        PRACTITIONERROLE, 
        /**
         * An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.
         */
        PROCEDURE, 
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
         * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
         */
        QUESTIONNAIRE, 
        /**
         * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give this medication after that one".
         */
        REQUESTGROUP, 
        /**
         * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
         */
        RESEARCHSTUDY, 
        /**
         * A physical entity which is the primary unit of operational and/or administrative interest in a study.
         */
        RESEARCHSUBJECT, 
        /**
         * This is the base resource type for everything.
         */
        RESOURCE, 
        /**
         * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISKASSESSMENT, 
        /**
         * A container for slots of time that may be available for booking appointments.
         */
        SCHEDULE, 
        /**
         * A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCHPARAMETER, 
        /**
         * Raw data describing a biological sequence.
         */
        SEQUENCE, 
        /**
         * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICEREQUEST, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A kind of specimen with associated set of requirements.
         */
        SPECIMENDEFINITION, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Todo.
         */
        SUBSTANCEPOLYMER, 
        /**
         * Todo.
         */
        SUBSTANCEREFERENCEINFORMATION, 
        /**
         * The detailed description of a substance, typically at a level beyond what is used for prescribing.
         */
        SUBSTANCESPECIFICATION, 
        /**
         * Record of delivery of what is supplied.
         */
        SUPPLYDELIVERY, 
        /**
         * A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLYREQUEST, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        TERMINOLOGYCAPABILITIES, 
        /**
         * A summary of information based on the results of executing a TestScript.
         */
        TESTREPORT, 
        /**
         * A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * Information about a user's current session.
         */
        USERSESSION, 
        /**
         * A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).
         */
        VALUESET, 
        /**
         * Describes validation requirements, source(s), status and dates for one or more elements.
         */
        VERIFICATIONRESULT, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FHIRDefinedType fromCode(String codeString) throws FHIRException {
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
        if ("ContactDetail".equals(codeString))
          return CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return CONTRIBUTOR;
        if ("Count".equals(codeString))
          return COUNT;
        if ("DataRequirement".equals(codeString))
          return DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return DISTANCE;
        if ("Dosage".equals(codeString))
          return DOSAGE;
        if ("Duration".equals(codeString))
          return DURATION;
        if ("Element".equals(codeString))
          return ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return ELEMENTDEFINITION;
        if ("Expression".equals(codeString))
          return EXPRESSION;
        if ("Extension".equals(codeString))
          return EXTENSION;
        if ("HumanName".equals(codeString))
          return HUMANNAME;
        if ("Identifier".equals(codeString))
          return IDENTIFIER;
        if ("MarketingStatus".equals(codeString))
          return MARKETINGSTATUS;
        if ("Meta".equals(codeString))
          return META;
        if ("Money".equals(codeString))
          return MONEY;
        if ("MoneyQuantity".equals(codeString))
          return MONEYQUANTITY;
        if ("Narrative".equals(codeString))
          return NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return PERIOD;
        if ("ProdCharacteristic".equals(codeString))
          return PRODCHARACTERISTIC;
        if ("ProductShelfLife".equals(codeString))
          return PRODUCTSHELFLIFE;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("Reference".equals(codeString))
          return REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return SIMPLEQUANTITY;
        if ("SubstanceAmount".equals(codeString))
          return SUBSTANCEAMOUNT;
        if ("SubstanceMoiety".equals(codeString))
          return SUBSTANCEMOIETY;
        if ("Timing".equals(codeString))
          return TIMING;
        if ("TriggerDefinition".equals(codeString))
          return TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return BASE64BINARY;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("canonical".equals(codeString))
          return CANONICAL;
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
        if ("url".equals(codeString))
          return URL;
        if ("uuid".equals(codeString))
          return UUID;
        if ("xhtml".equals(codeString))
          return XHTML;
        if ("Account".equals(codeString))
          return ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return ADVERSEEVENT;
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
        if ("BiologicallyDerivedProduct".equals(codeString))
          return BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("CareTeam".equals(codeString))
          return CARETEAM;
        if ("CatalogEntry".equals(codeString))
          return CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return CHARGEITEMDEFINITION;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return CODESYSTEM;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Consent".equals(codeString))
          return CONSENT;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("CoverageEligibilityRequest".equals(codeString))
          return COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return DEVICEDEFINITION;
        if ("DeviceMetric".equals(codeString))
          return DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return DOMAINRESOURCE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return EXAMPLESCENARIO;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FLAG;
        if ("Goal".equals(codeString))
          return GOAL;
        if ("GraphDefinition".equals(codeString))
          return GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return GROUP;
        if ("GuidanceResponse".equals(codeString))
          return GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("InsurancePlan".equals(codeString))
          return INSURANCEPLAN;
        if ("Invoice".equals(codeString))
          return INVOICE;
        if ("ItemInstance".equals(codeString))
          return ITEMINSTANCE;
        if ("Library".equals(codeString))
          return LIBRARY;
        if ("Linkage".equals(codeString))
          return LINKAGE;
        if ("List".equals(codeString))
          return LIST;
        if ("Location".equals(codeString))
          return LOCATION;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("MeasureReport".equals(codeString))
          return MEASUREREPORT;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("Medication".equals(codeString))
          return MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductContraindication".equals(codeString))
          return MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return MEDICINALPRODUCTUNDESIRABLEEFFECT;
        if ("MessageDefinition".equals(codeString))
          return MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return OBSERVATIONDEFINITION;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return ORGANIZATIONAFFILIATION;
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
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
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
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return TESTREPORT;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return USERSESSION;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VerificationResult".equals(codeString))
          return VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        throw new FHIRException("Unknown FHIRDefinedType code '"+codeString+"'");
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
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXPRESSION: return "Expression";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case MARKETINGSTATUS: return "MarketingStatus";
            case META: return "Meta";
            case MONEY: return "Money";
            case MONEYQUANTITY: return "MoneyQuantity";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case PRODCHARACTERISTIC: return "ProdCharacteristic";
            case PRODUCTSHELFLIFE: return "ProductShelfLife";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case SUBSTANCEAMOUNT: return "SubstanceAmount";
            case SUBSTANCEMOIETY: return "SubstanceMoiety";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CANONICAL: return "canonical";
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
            case URL: return "url";
            case UUID: return "uuid";
            case XHTML: return "xhtml";
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INSURANCEPLAN: return "InsurancePlan";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
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
            case CONTACTDETAIL: return "http://hl7.org/fhir/data-types";
            case CONTACTPOINT: return "http://hl7.org/fhir/data-types";
            case CONTRIBUTOR: return "http://hl7.org/fhir/data-types";
            case COUNT: return "http://hl7.org/fhir/data-types";
            case DATAREQUIREMENT: return "http://hl7.org/fhir/data-types";
            case DISTANCE: return "http://hl7.org/fhir/data-types";
            case DOSAGE: return "http://hl7.org/fhir/data-types";
            case DURATION: return "http://hl7.org/fhir/data-types";
            case ELEMENT: return "http://hl7.org/fhir/data-types";
            case ELEMENTDEFINITION: return "http://hl7.org/fhir/data-types";
            case EXPRESSION: return "http://hl7.org/fhir/data-types";
            case EXTENSION: return "http://hl7.org/fhir/data-types";
            case HUMANNAME: return "http://hl7.org/fhir/data-types";
            case IDENTIFIER: return "http://hl7.org/fhir/data-types";
            case MARKETINGSTATUS: return "http://hl7.org/fhir/data-types";
            case META: return "http://hl7.org/fhir/data-types";
            case MONEY: return "http://hl7.org/fhir/data-types";
            case MONEYQUANTITY: return "http://hl7.org/fhir/data-types";
            case NARRATIVE: return "http://hl7.org/fhir/data-types";
            case PARAMETERDEFINITION: return "http://hl7.org/fhir/data-types";
            case PERIOD: return "http://hl7.org/fhir/data-types";
            case PRODCHARACTERISTIC: return "http://hl7.org/fhir/data-types";
            case PRODUCTSHELFLIFE: return "http://hl7.org/fhir/data-types";
            case QUANTITY: return "http://hl7.org/fhir/data-types";
            case RANGE: return "http://hl7.org/fhir/data-types";
            case RATIO: return "http://hl7.org/fhir/data-types";
            case REFERENCE: return "http://hl7.org/fhir/data-types";
            case RELATEDARTIFACT: return "http://hl7.org/fhir/data-types";
            case SAMPLEDDATA: return "http://hl7.org/fhir/data-types";
            case SIGNATURE: return "http://hl7.org/fhir/data-types";
            case SIMPLEQUANTITY: return "http://hl7.org/fhir/data-types";
            case SUBSTANCEAMOUNT: return "http://hl7.org/fhir/data-types";
            case SUBSTANCEMOIETY: return "http://hl7.org/fhir/data-types";
            case TIMING: return "http://hl7.org/fhir/data-types";
            case TRIGGERDEFINITION: return "http://hl7.org/fhir/data-types";
            case USAGECONTEXT: return "http://hl7.org/fhir/data-types";
            case BASE64BINARY: return "http://hl7.org/fhir/data-types";
            case BOOLEAN: return "http://hl7.org/fhir/data-types";
            case CANONICAL: return "http://hl7.org/fhir/data-types";
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
            case URL: return "http://hl7.org/fhir/data-types";
            case UUID: return "http://hl7.org/fhir/data-types";
            case XHTML: return "http://hl7.org/fhir/data-types";
            case ACCOUNT: return "http://hl7.org/fhir/resource-types";
            case ACTIVITYDEFINITION: return "http://hl7.org/fhir/resource-types";
            case ADVERSEEVENT: return "http://hl7.org/fhir/resource-types";
            case ALLERGYINTOLERANCE: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENT: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case AUDITEVENT: return "http://hl7.org/fhir/resource-types";
            case BASIC: return "http://hl7.org/fhir/resource-types";
            case BINARY: return "http://hl7.org/fhir/resource-types";
            case BIOLOGICALLYDERIVEDPRODUCT: return "http://hl7.org/fhir/resource-types";
            case BODYSTRUCTURE: return "http://hl7.org/fhir/resource-types";
            case BUNDLE: return "http://hl7.org/fhir/resource-types";
            case CAPABILITYSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/resource-types";
            case CARETEAM: return "http://hl7.org/fhir/resource-types";
            case CATALOGENTRY: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEM: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEMDEFINITION: return "http://hl7.org/fhir/resource-types";
            case CLAIM: return "http://hl7.org/fhir/resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/resource-types";
            case CODESYSTEM: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case COMPARTMENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/resource-types";
            case CONDITION: return "http://hl7.org/fhir/resource-types";
            case CONSENT: return "http://hl7.org/fhir/resource-types";
            case CONTRACT: return "http://hl7.org/fhir/resource-types";
            case COVERAGE: return "http://hl7.org/fhir/resource-types";
            case COVERAGEELIGIBILITYREQUEST: return "http://hl7.org/fhir/resource-types";
            case COVERAGEELIGIBILITYRESPONSE: return "http://hl7.org/fhir/resource-types";
            case DETECTEDISSUE: return "http://hl7.org/fhir/resource-types";
            case DEVICE: return "http://hl7.org/fhir/resource-types";
            case DEVICEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case DEVICEMETRIC: return "http://hl7.org/fhir/resource-types";
            case DEVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/resource-types";
            case DOMAINRESOURCE: return "http://hl7.org/fhir/resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/resource-types";
            case ENDPOINT: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EVENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case EXAMPLESCENARIO: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GRAPHDEFINITION: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case GUIDANCERESPONSE: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONEVALUATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case INSURANCEPLAN: return "http://hl7.org/fhir/resource-types";
            case INVOICE: return "http://hl7.org/fhir/resource-types";
            case ITEMINSTANCE: return "http://hl7.org/fhir/resource-types";
            case LIBRARY: return "http://hl7.org/fhir/resource-types";
            case LINKAGE: return "http://hl7.org/fhir/resource-types";
            case LIST: return "http://hl7.org/fhir/resource-types";
            case LOCATION: return "http://hl7.org/fhir/resource-types";
            case MEASURE: return "http://hl7.org/fhir/resource-types";
            case MEASUREREPORT: return "http://hl7.org/fhir/resource-types";
            case MEDIA: return "http://hl7.org/fhir/resource-types";
            case MEDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONKNOWLEDGE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTAUTHORIZATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCLINICALS: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCONTRAINDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTDEVICESPEC: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINGREDIENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINTERACTION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTMANUFACTURED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPACKAGED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "http://hl7.org/fhir/resource-types";
            case MESSAGEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OBSERVATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATIONAFFILIATION: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONERROLE: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case REQUESTGROUP: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSTUDY: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSUBJECT: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SEQUENCE: return "http://hl7.org/fhir/resource-types";
            case SERVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case SPECIMENDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREMAP: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEPOLYMER: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEREFERENCEINFORMATION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCESPECIFICATION: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TASK: return "http://hl7.org/fhir/resource-types";
            case TERMINOLOGYCAPABILITIES: return "http://hl7.org/fhir/resource-types";
            case TESTREPORT: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case USERSESSION: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VERIFICATIONRESULT: return "http://hl7.org/fhir/resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ADDRESS: return "An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.";
            case AGE: return "A duration of time during which an organism (or a process) has existed.";
            case ANNOTATION: return "A  text note which also  contains information about who made the statement and when.";
            case ATTACHMENT: return "For referring to data content defined in other formats.";
            case BACKBONEELEMENT: return "Base definition for all elements that are defined inside a resource - but not those in a data type.";
            case CODEABLECONCEPT: return "A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.";
            case CODING: return "A reference to a code defined by a terminology system.";
            case CONTACTDETAIL: return "Specifies contact information for a person or organization.";
            case CONTACTPOINT: return "Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.";
            case CONTRIBUTOR: return "A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.";
            case COUNT: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case DATAREQUIREMENT: return "Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.";
            case DISTANCE: return "A length - a value with a unit that is a physical distance.";
            case DOSAGE: return "Indicates how the medication is/was taken or should be taken by the patient.";
            case DURATION: return "A length of time.";
            case ELEMENT: return "Base definition for all elements in a resource.";
            case ELEMENTDEFINITION: return "Captures constraints on each element within the resource, profile, or extension.";
            case EXPRESSION: return "A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.";
            case EXTENSION: return "Optional Extension Element - found in all resources.";
            case HUMANNAME: return "A human's name with the ability to identify parts and usage.";
            case IDENTIFIER: return "An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.";
            case MARKETINGSTATUS: return "The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.";
            case META: return "The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.";
            case MONEY: return "An amount of economic utility in some recognized currency.";
            case MONEYQUANTITY: return "";
            case NARRATIVE: return "A human-readable formatted text, including images.";
            case PARAMETERDEFINITION: return "The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.";
            case PERIOD: return "A time period defined by a start and end date and optionally time.";
            case PRODCHARACTERISTIC: return "The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.";
            case PRODUCTSHELFLIFE: return "The shelf-life and storage information for a medicinal product item or container can be described using this class.";
            case QUANTITY: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case RANGE: return "A set of ordered Quantities defined by a low and high limit.";
            case RATIO: return "A relationship of two Quantity values - expressed as a numerator and a denominator.";
            case REFERENCE: return "A reference from one resource to another.";
            case RELATEDARTIFACT: return "Related artifacts such as additional documentation, justification, or bibliographic references.";
            case SAMPLEDDATA: return "A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.";
            case SIGNATURE: return "A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.";
            case SIMPLEQUANTITY: return "";
            case SUBSTANCEAMOUNT: return "Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.";
            case SUBSTANCEMOIETY: return "Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.";
            case TIMING: return "Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.";
            case TRIGGERDEFINITION: return "A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.";
            case USAGECONTEXT: return "Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).";
            case BASE64BINARY: return "A stream of bytes";
            case BOOLEAN: return "Value of \"true\" or \"false\"";
            case CANONICAL: return "A URI that is a reference to a canonical URI on a FHIR resource";
            case CODE: return "A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents";
            case DATE: return "A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.";
            case DECIMAL: return "A rational number with implicit precision";
            case ID: return "Any combination of letters, numerals, \"-\" and \".\", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.";
            case INSTANT: return "An instant in time - known at least to the second";
            case INTEGER: return "A whole number";
            case MARKDOWN: return "A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine";
            case OID: return "An OID represented as a URI";
            case POSITIVEINT: return "An integer with a value that is positive (e.g. >0)";
            case STRING: return "A sequence of Unicode characters";
            case TIME: return "A time during the day, with no date specified";
            case UNSIGNEDINT: return "An integer with a value that is not negative (e.g. >= 0)";
            case URI: return "String of characters used to identify a name or a resource";
            case URL: return "A URI that is a literal reference";
            case UUID: return "A UUID, represented as a URI";
            case XHTML: return "XHTML format, as defined by W3C, but restricted usage (mainly, no active content)";
            case ACCOUNT: return "A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.";
            case ACTIVITYDEFINITION: return "This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.";
            case ADVERSEEVENT: return "Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.";
            case ALLERGYINTOLERANCE: return "Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.";
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case AUDITEVENT: return "A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.";
            case BASIC: return "Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.";
            case BINARY: return "A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BIOLOGICALLYDERIVEDPRODUCT: return "A material substance originating from a biological entity intended to be transplanted or infused\ninto another (possibly the same) biological entity.";
            case BODYSTRUCTURE: return "Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAPABILITYSTATEMENT: return "A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CARETEAM: return "The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.";
            case CATALOGENTRY: return "Catalog entries are wrappers that contextualize items included in a catalog.";
            case CHARGEITEM: return "The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.";
            case CHARGEITEMDEFINITION: return "The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case CODESYSTEM: return "The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPARTMENTDEFINITION: return "A compartment definition that defines how resources are accessed on a server.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.";
            case CONDITION: return "A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.";
            case CONSENT: return "A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.";
            case CONTRACT: return "Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.";
            case COVERAGE: return "Financial instrument which may be used to reimburse or pay for health care products and services.";
            case COVERAGEELIGIBILITYREQUEST: return "The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.";
            case COVERAGEELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).";
            case DEVICEDEFINITION: return "The characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A collection of documents compiled for a purpose together with metadata that applies to the collection.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case DOMAINRESOURCE: return "A resource that includes narrative, extensions, and contained resources.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENDPOINT: return "The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EVENTDEFINITION: return "The EventDefinition resource provides a reusable description of when a particular event can occur.";
            case EXAMPLESCENARIO: return "Example of workflow instance.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GRAPHDEFINITION: return "A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case GUIDANCERESPONSE: return "A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.";
            case IMMUNIZATIONEVALUATION: return "Describes a comparison of an immunization event against published recommendations to determine if the administration is \"valid\" in relation to those  recommendations.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.";
            case INSURANCEPLAN: return "Details of a Health Insurance product/plan provided by an organization.";
            case INVOICE: return "Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.";
            case ITEMINSTANCE: return "A physical, countable instance of an item, for example one box or one unit.";
            case LIBRARY: return "The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.";
            case LINKAGE: return "Identifies two or more records (resource instances) that are referring to the same real-world \"occurrence\".";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.";
            case MEASURE: return "The Measure resource provides the definition of a quality measure.";
            case MEASUREREPORT: return "The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONKNOWLEDGE: return "Information about a medication that is used to support knowledge.";
            case MEDICATIONREQUEST: return "An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationRequest\" rather than \"MedicationPrescription\" or \"MedicationOrder\" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MEDICINALPRODUCT: return "Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).";
            case MEDICINALPRODUCTAUTHORIZATION: return "The regulatory authorization of a medicinal product.";
            case MEDICINALPRODUCTCLINICALS: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTCONTRAINDICATION: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTDEVICESPEC: return "A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.";
            case MEDICINALPRODUCTINDICATION: return "Indication for the Medicinal Product.";
            case MEDICINALPRODUCTINGREDIENT: return "An ingredient of a manufactured item or pharmaceutical product.";
            case MEDICINALPRODUCTINTERACTION: return "The interactions of the medicinal product with other medicinal products, or other forms of interactions.";
            case MEDICINALPRODUCTMANUFACTURED: return "The manufactured item as contained in the packaged medicinal product.";
            case MEDICINALPRODUCTPACKAGED: return "A medicinal product in a container or package.";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "A pharmaceutical product described in terms of its composition and dose form.";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "Describe the undesirable effects of the medicinal product.";
            case MESSAGEDEFINITION: return "Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OBSERVATIONDEFINITION: return "Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.";
            case ORGANIZATIONAFFILIATION: return "Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PRACTITIONERROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PROCEDURE: return "An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case REQUESTGROUP: return "A group of related requests that can be used to capture intended activities that have inter-dependencies such as \"give this medication after that one\".";
            case RESEARCHSTUDY: return "A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.";
            case RESEARCHSUBJECT: return "A physical entity which is the primary unit of operational and/or administrative interest in a study.";
            case RESOURCE: return "This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slots of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SEQUENCE: return "Raw data describing a biological sequence.";
            case SERVICEREQUEST: return "A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case SPECIMENDEFINITION: return "A kind of specimen with associated set of requirements.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system can take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUBSTANCEPOLYMER: return "Todo.";
            case SUBSTANCEREFERENCEINFORMATION: return "Todo.";
            case SUBSTANCESPECIFICATION: return "The detailed description of a substance, typically at a level beyond what is used for prescribing.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TASK: return "A task to be performed.";
            case TERMINOLOGYCAPABILITIES: return "A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case TESTREPORT: return "A summary of information based on the results of executing a TestScript.";
            case TESTSCRIPT: return "A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.";
            case USERSESSION: return "Information about a user's current session.";
            case VALUESET: return "A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).";
            case VERIFICATIONRESULT: return "Describes validation requirements, source(s), status and dates for one or more elements.";
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
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXPRESSION: return "Expression";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case MARKETINGSTATUS: return "MarketingStatus";
            case META: return "Meta";
            case MONEY: return "Money";
            case MONEYQUANTITY: return "MoneyQuantity";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case PRODCHARACTERISTIC: return "ProdCharacteristic";
            case PRODUCTSHELFLIFE: return "ProductShelfLife";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case SUBSTANCEAMOUNT: return "SubstanceAmount";
            case SUBSTANCEMOIETY: return "SubstanceMoiety";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CANONICAL: return "canonical";
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
            case URL: return "url";
            case UUID: return "uuid";
            case XHTML: return "XHTML";
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INSURANCEPLAN: return "InsurancePlan";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
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
        if ("ContactDetail".equals(codeString))
          return FHIRDefinedType.CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return FHIRDefinedType.CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return FHIRDefinedType.CONTRIBUTOR;
        if ("Count".equals(codeString))
          return FHIRDefinedType.COUNT;
        if ("DataRequirement".equals(codeString))
          return FHIRDefinedType.DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return FHIRDefinedType.DISTANCE;
        if ("Dosage".equals(codeString))
          return FHIRDefinedType.DOSAGE;
        if ("Duration".equals(codeString))
          return FHIRDefinedType.DURATION;
        if ("Element".equals(codeString))
          return FHIRDefinedType.ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return FHIRDefinedType.ELEMENTDEFINITION;
        if ("Expression".equals(codeString))
          return FHIRDefinedType.EXPRESSION;
        if ("Extension".equals(codeString))
          return FHIRDefinedType.EXTENSION;
        if ("HumanName".equals(codeString))
          return FHIRDefinedType.HUMANNAME;
        if ("Identifier".equals(codeString))
          return FHIRDefinedType.IDENTIFIER;
        if ("MarketingStatus".equals(codeString))
          return FHIRDefinedType.MARKETINGSTATUS;
        if ("Meta".equals(codeString))
          return FHIRDefinedType.META;
        if ("Money".equals(codeString))
          return FHIRDefinedType.MONEY;
        if ("MoneyQuantity".equals(codeString))
          return FHIRDefinedType.MONEYQUANTITY;
        if ("Narrative".equals(codeString))
          return FHIRDefinedType.NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return FHIRDefinedType.PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return FHIRDefinedType.PERIOD;
        if ("ProdCharacteristic".equals(codeString))
          return FHIRDefinedType.PRODCHARACTERISTIC;
        if ("ProductShelfLife".equals(codeString))
          return FHIRDefinedType.PRODUCTSHELFLIFE;
        if ("Quantity".equals(codeString))
          return FHIRDefinedType.QUANTITY;
        if ("Range".equals(codeString))
          return FHIRDefinedType.RANGE;
        if ("Ratio".equals(codeString))
          return FHIRDefinedType.RATIO;
        if ("Reference".equals(codeString))
          return FHIRDefinedType.REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return FHIRDefinedType.RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return FHIRDefinedType.SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return FHIRDefinedType.SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return FHIRDefinedType.SIMPLEQUANTITY;
        if ("SubstanceAmount".equals(codeString))
          return FHIRDefinedType.SUBSTANCEAMOUNT;
        if ("SubstanceMoiety".equals(codeString))
          return FHIRDefinedType.SUBSTANCEMOIETY;
        if ("Timing".equals(codeString))
          return FHIRDefinedType.TIMING;
        if ("TriggerDefinition".equals(codeString))
          return FHIRDefinedType.TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return FHIRDefinedType.USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return FHIRDefinedType.BASE64BINARY;
        if ("boolean".equals(codeString))
          return FHIRDefinedType.BOOLEAN;
        if ("canonical".equals(codeString))
          return FHIRDefinedType.CANONICAL;
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
        if ("url".equals(codeString))
          return FHIRDefinedType.URL;
        if ("uuid".equals(codeString))
          return FHIRDefinedType.UUID;
        if ("xhtml".equals(codeString))
          return FHIRDefinedType.XHTML;
        if ("Account".equals(codeString))
          return FHIRDefinedType.ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return FHIRDefinedType.ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return FHIRDefinedType.ADVERSEEVENT;
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
        if ("BiologicallyDerivedProduct".equals(codeString))
          return FHIRDefinedType.BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return FHIRDefinedType.BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return FHIRDefinedType.BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return FHIRDefinedType.CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return FHIRDefinedType.CAREPLAN;
        if ("CareTeam".equals(codeString))
          return FHIRDefinedType.CARETEAM;
        if ("CatalogEntry".equals(codeString))
          return FHIRDefinedType.CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return FHIRDefinedType.CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return FHIRDefinedType.CHARGEITEMDEFINITION;
        if ("Claim".equals(codeString))
          return FHIRDefinedType.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return FHIRDefinedType.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return FHIRDefinedType.CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return FHIRDefinedType.CODESYSTEM;
        if ("Communication".equals(codeString))
          return FHIRDefinedType.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return FHIRDefinedType.COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return FHIRDefinedType.COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return FHIRDefinedType.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return FHIRDefinedType.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return FHIRDefinedType.CONDITION;
        if ("Consent".equals(codeString))
          return FHIRDefinedType.CONSENT;
        if ("Contract".equals(codeString))
          return FHIRDefinedType.CONTRACT;
        if ("Coverage".equals(codeString))
          return FHIRDefinedType.COVERAGE;
        if ("CoverageEligibilityRequest".equals(codeString))
          return FHIRDefinedType.COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return FHIRDefinedType.COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return FHIRDefinedType.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return FHIRDefinedType.DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return FHIRDefinedType.DEVICEDEFINITION;
        if ("DeviceMetric".equals(codeString))
          return FHIRDefinedType.DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return FHIRDefinedType.DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return FHIRDefinedType.DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return FHIRDefinedType.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return FHIRDefinedType.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return FHIRDefinedType.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return FHIRDefinedType.DOMAINRESOURCE;
        if ("Encounter".equals(codeString))
          return FHIRDefinedType.ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return FHIRDefinedType.ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return FHIRDefinedType.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return FHIRDefinedType.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return FHIRDefinedType.EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return FHIRDefinedType.EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return FHIRDefinedType.EXAMPLESCENARIO;
        if ("ExplanationOfBenefit".equals(codeString))
          return FHIRDefinedType.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FHIRDefinedType.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FHIRDefinedType.FLAG;
        if ("Goal".equals(codeString))
          return FHIRDefinedType.GOAL;
        if ("GraphDefinition".equals(codeString))
          return FHIRDefinedType.GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return FHIRDefinedType.GROUP;
        if ("GuidanceResponse".equals(codeString))
          return FHIRDefinedType.GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return FHIRDefinedType.HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return FHIRDefinedType.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return FHIRDefinedType.IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return FHIRDefinedType.IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return FHIRDefinedType.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return FHIRDefinedType.IMPLEMENTATIONGUIDE;
        if ("InsurancePlan".equals(codeString))
          return FHIRDefinedType.INSURANCEPLAN;
        if ("Invoice".equals(codeString))
          return FHIRDefinedType.INVOICE;
        if ("ItemInstance".equals(codeString))
          return FHIRDefinedType.ITEMINSTANCE;
        if ("Library".equals(codeString))
          return FHIRDefinedType.LIBRARY;
        if ("Linkage".equals(codeString))
          return FHIRDefinedType.LINKAGE;
        if ("List".equals(codeString))
          return FHIRDefinedType.LIST;
        if ("Location".equals(codeString))
          return FHIRDefinedType.LOCATION;
        if ("Measure".equals(codeString))
          return FHIRDefinedType.MEASURE;
        if ("MeasureReport".equals(codeString))
          return FHIRDefinedType.MEASUREREPORT;
        if ("Media".equals(codeString))
          return FHIRDefinedType.MEDIA;
        if ("Medication".equals(codeString))
          return FHIRDefinedType.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return FHIRDefinedType.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return FHIRDefinedType.MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return FHIRDefinedType.MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return FHIRDefinedType.MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return FHIRDefinedType.MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductContraindication".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return FHIRDefinedType.MEDICINALPRODUCTUNDESIRABLEEFFECT;
        if ("MessageDefinition".equals(codeString))
          return FHIRDefinedType.MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return FHIRDefinedType.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return FHIRDefinedType.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return FHIRDefinedType.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return FHIRDefinedType.OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return FHIRDefinedType.OBSERVATIONDEFINITION;
        if ("OperationDefinition".equals(codeString))
          return FHIRDefinedType.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return FHIRDefinedType.OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return FHIRDefinedType.ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return FHIRDefinedType.ORGANIZATIONAFFILIATION;
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
        if ("PlanDefinition".equals(codeString))
          return FHIRDefinedType.PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return FHIRDefinedType.PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return FHIRDefinedType.PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return FHIRDefinedType.PROCEDURE;
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
        if ("RelatedPerson".equals(codeString))
          return FHIRDefinedType.RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return FHIRDefinedType.REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return FHIRDefinedType.RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return FHIRDefinedType.RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return FHIRDefinedType.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return FHIRDefinedType.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return FHIRDefinedType.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return FHIRDefinedType.SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return FHIRDefinedType.SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return FHIRDefinedType.SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return FHIRDefinedType.SLOT;
        if ("Specimen".equals(codeString))
          return FHIRDefinedType.SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return FHIRDefinedType.SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return FHIRDefinedType.STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return FHIRDefinedType.STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return FHIRDefinedType.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return FHIRDefinedType.SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return FHIRDefinedType.SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return FHIRDefinedType.SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return FHIRDefinedType.SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return FHIRDefinedType.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return FHIRDefinedType.SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return FHIRDefinedType.TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return FHIRDefinedType.TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return FHIRDefinedType.TESTREPORT;
        if ("TestScript".equals(codeString))
          return FHIRDefinedType.TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return FHIRDefinedType.USERSESSION;
        if ("ValueSet".equals(codeString))
          return FHIRDefinedType.VALUESET;
        if ("VerificationResult".equals(codeString))
          return FHIRDefinedType.VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return FHIRDefinedType.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown FHIRDefinedType code '"+codeString+"'");
        }
        public Enumeration<FHIRDefinedType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<FHIRDefinedType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Address".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ADDRESS);
        if ("Age".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.AGE);
        if ("Annotation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ANNOTATION);
        if ("Attachment".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ATTACHMENT);
        if ("BackboneElement".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BACKBONEELEMENT);
        if ("CodeableConcept".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CODEABLECONCEPT);
        if ("Coding".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CODING);
        if ("ContactDetail".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONTACTDETAIL);
        if ("ContactPoint".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONTACTPOINT);
        if ("Contributor".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONTRIBUTOR);
        if ("Count".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COUNT);
        if ("DataRequirement".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DATAREQUIREMENT);
        if ("Distance".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DISTANCE);
        if ("Dosage".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DOSAGE);
        if ("Duration".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DURATION);
        if ("Element".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ELEMENT);
        if ("ElementDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ELEMENTDEFINITION);
        if ("Expression".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.EXPRESSION);
        if ("Extension".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.EXTENSION);
        if ("HumanName".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.HUMANNAME);
        if ("Identifier".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.IDENTIFIER);
        if ("MarketingStatus".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MARKETINGSTATUS);
        if ("Meta".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.META);
        if ("Money".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MONEY);
        if ("MoneyQuantity".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MONEYQUANTITY);
        if ("Narrative".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.NARRATIVE);
        if ("ParameterDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PARAMETERDEFINITION);
        if ("Period".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PERIOD);
        if ("ProdCharacteristic".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PRODCHARACTERISTIC);
        if ("ProductShelfLife".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PRODUCTSHELFLIFE);
        if ("Quantity".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.QUANTITY);
        if ("Range".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RANGE);
        if ("Ratio".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RATIO);
        if ("Reference".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.REFERENCE);
        if ("RelatedArtifact".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RELATEDARTIFACT);
        if ("SampledData".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SAMPLEDDATA);
        if ("Signature".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SIGNATURE);
        if ("SimpleQuantity".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SIMPLEQUANTITY);
        if ("SubstanceAmount".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSTANCEAMOUNT);
        if ("SubstanceMoiety".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSTANCEMOIETY);
        if ("Timing".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TIMING);
        if ("TriggerDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TRIGGERDEFINITION);
        if ("UsageContext".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.USAGECONTEXT);
        if ("base64Binary".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BASE64BINARY);
        if ("boolean".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BOOLEAN);
        if ("canonical".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CANONICAL);
        if ("code".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CODE);
        if ("date".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DATE);
        if ("dateTime".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DATETIME);
        if ("decimal".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DECIMAL);
        if ("id".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ID);
        if ("instant".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.INSTANT);
        if ("integer".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.INTEGER);
        if ("markdown".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MARKDOWN);
        if ("oid".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.OID);
        if ("positiveInt".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.POSITIVEINT);
        if ("string".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.STRING);
        if ("time".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TIME);
        if ("unsignedInt".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.UNSIGNEDINT);
        if ("uri".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.URI);
        if ("url".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.URL);
        if ("uuid".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.UUID);
        if ("xhtml".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.XHTML);
        if ("Account".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ACCOUNT);
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ACTIVITYDEFINITION);
        if ("AdverseEvent".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ADVERSEEVENT);
        if ("AllergyIntolerance".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ALLERGYINTOLERANCE);
        if ("Appointment".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.APPOINTMENT);
        if ("AppointmentResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.APPOINTMENTRESPONSE);
        if ("AuditEvent".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.AUDITEVENT);
        if ("Basic".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BASIC);
        if ("Binary".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BINARY);
        if ("BiologicallyDerivedProduct".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BIOLOGICALLYDERIVEDPRODUCT);
        if ("BodyStructure".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BODYSTRUCTURE);
        if ("Bundle".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.BUNDLE);
        if ("CapabilityStatement".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CAPABILITYSTATEMENT);
        if ("CarePlan".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CAREPLAN);
        if ("CareTeam".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CARETEAM);
        if ("CatalogEntry".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CATALOGENTRY);
        if ("ChargeItem".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CHARGEITEM);
        if ("ChargeItemDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CHARGEITEMDEFINITION);
        if ("Claim".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CLAIM);
        if ("ClaimResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CLAIMRESPONSE);
        if ("ClinicalImpression".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CLINICALIMPRESSION);
        if ("CodeSystem".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CODESYSTEM);
        if ("Communication".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COMMUNICATION);
        if ("CommunicationRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COMMUNICATIONREQUEST);
        if ("CompartmentDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COMPARTMENTDEFINITION);
        if ("Composition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COMPOSITION);
        if ("ConceptMap".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONCEPTMAP);
        if ("Condition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONDITION);
        if ("Consent".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONSENT);
        if ("Contract".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.CONTRACT);
        if ("Coverage".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COVERAGE);
        if ("CoverageEligibilityRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COVERAGEELIGIBILITYREQUEST);
        if ("CoverageEligibilityResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.COVERAGEELIGIBILITYRESPONSE);
        if ("DetectedIssue".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DETECTEDISSUE);
        if ("Device".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DEVICE);
        if ("DeviceDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DEVICEDEFINITION);
        if ("DeviceMetric".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DEVICEMETRIC);
        if ("DeviceRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DEVICEREQUEST);
        if ("DeviceUseStatement".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DEVICEUSESTATEMENT);
        if ("DiagnosticReport".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DIAGNOSTICREPORT);
        if ("DocumentManifest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DOCUMENTMANIFEST);
        if ("DocumentReference".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DOCUMENTREFERENCE);
        if ("DomainResource".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.DOMAINRESOURCE);
        if ("Encounter".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ENCOUNTER);
        if ("Endpoint".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ENDPOINT);
        if ("EnrollmentRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ENROLLMENTREQUEST);
        if ("EnrollmentResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ENROLLMENTRESPONSE);
        if ("EpisodeOfCare".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.EPISODEOFCARE);
        if ("EventDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.EVENTDEFINITION);
        if ("ExampleScenario".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.EXAMPLESCENARIO);
        if ("ExplanationOfBenefit".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.EXPLANATIONOFBENEFIT);
        if ("FamilyMemberHistory".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.FAMILYMEMBERHISTORY);
        if ("Flag".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.FLAG);
        if ("Goal".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.GOAL);
        if ("GraphDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.GRAPHDEFINITION);
        if ("Group".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.GROUP);
        if ("GuidanceResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.GUIDANCERESPONSE);
        if ("HealthcareService".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.HEALTHCARESERVICE);
        if ("ImagingStudy".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.IMAGINGSTUDY);
        if ("Immunization".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.IMMUNIZATION);
        if ("ImmunizationEvaluation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.IMMUNIZATIONEVALUATION);
        if ("ImmunizationRecommendation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.IMMUNIZATIONRECOMMENDATION);
        if ("ImplementationGuide".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.IMPLEMENTATIONGUIDE);
        if ("InsurancePlan".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.INSURANCEPLAN);
        if ("Invoice".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.INVOICE);
        if ("ItemInstance".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ITEMINSTANCE);
        if ("Library".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.LIBRARY);
        if ("Linkage".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.LINKAGE);
        if ("List".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.LIST);
        if ("Location".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.LOCATION);
        if ("Measure".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEASURE);
        if ("MeasureReport".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEASUREREPORT);
        if ("Media".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDIA);
        if ("Medication".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICATION);
        if ("MedicationAdministration".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICATIONADMINISTRATION);
        if ("MedicationDispense".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICATIONDISPENSE);
        if ("MedicationKnowledge".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICATIONKNOWLEDGE);
        if ("MedicationRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICATIONREQUEST);
        if ("MedicationStatement".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICATIONSTATEMENT);
        if ("MedicinalProduct".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCT);
        if ("MedicinalProductAuthorization".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTAUTHORIZATION);
        if ("MedicinalProductClinicals".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTCLINICALS);
        if ("MedicinalProductContraindication".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTCONTRAINDICATION);
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTDEVICESPEC);
        if ("MedicinalProductIndication".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTINDICATION);
        if ("MedicinalProductIngredient".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTINGREDIENT);
        if ("MedicinalProductInteraction".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTINTERACTION);
        if ("MedicinalProductManufactured".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTMANUFACTURED);
        if ("MedicinalProductPackaged".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTPACKAGED);
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTPHARMACEUTICAL);
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MEDICINALPRODUCTUNDESIRABLEEFFECT);
        if ("MessageDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MESSAGEDEFINITION);
        if ("MessageHeader".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.MESSAGEHEADER);
        if ("NamingSystem".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.NAMINGSYSTEM);
        if ("NutritionOrder".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.NUTRITIONORDER);
        if ("Observation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.OBSERVATION);
        if ("ObservationDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.OBSERVATIONDEFINITION);
        if ("OperationDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.OPERATIONDEFINITION);
        if ("OperationOutcome".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.OPERATIONOUTCOME);
        if ("Organization".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ORGANIZATION);
        if ("OrganizationAffiliation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.ORGANIZATIONAFFILIATION);
        if ("Parameters".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PARAMETERS);
        if ("Patient".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PATIENT);
        if ("PaymentNotice".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PAYMENTNOTICE);
        if ("PaymentReconciliation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PAYMENTRECONCILIATION);
        if ("Person".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PERSON);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PLANDEFINITION);
        if ("Practitioner".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PRACTITIONER);
        if ("PractitionerRole".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PRACTITIONERROLE);
        if ("Procedure".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PROCEDURE);
        if ("ProcessRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PROCESSREQUEST);
        if ("ProcessResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PROCESSRESPONSE);
        if ("Provenance".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.PROVENANCE);
        if ("Questionnaire".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.QUESTIONNAIRE);
        if ("QuestionnaireResponse".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.QUESTIONNAIRERESPONSE);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RELATEDPERSON);
        if ("RequestGroup".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.REQUESTGROUP);
        if ("ResearchStudy".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RESEARCHSTUDY);
        if ("ResearchSubject".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RESEARCHSUBJECT);
        if ("Resource".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RESOURCE);
        if ("RiskAssessment".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.RISKASSESSMENT);
        if ("Schedule".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SCHEDULE);
        if ("SearchParameter".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SEARCHPARAMETER);
        if ("Sequence".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SEQUENCE);
        if ("ServiceRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SERVICEREQUEST);
        if ("Slot".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SLOT);
        if ("Specimen".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SPECIMEN);
        if ("SpecimenDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SPECIMENDEFINITION);
        if ("StructureDefinition".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.STRUCTUREDEFINITION);
        if ("StructureMap".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.STRUCTUREMAP);
        if ("Subscription".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSCRIPTION);
        if ("Substance".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSTANCE);
        if ("SubstancePolymer".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSTANCEPOLYMER);
        if ("SubstanceReferenceInformation".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSTANCEREFERENCEINFORMATION);
        if ("SubstanceSpecification".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUBSTANCESPECIFICATION);
        if ("SupplyDelivery".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUPPLYDELIVERY);
        if ("SupplyRequest".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.SUPPLYREQUEST);
        if ("Task".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TASK);
        if ("TerminologyCapabilities".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TERMINOLOGYCAPABILITIES);
        if ("TestReport".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TESTREPORT);
        if ("TestScript".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.TESTSCRIPT);
        if ("UserSession".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.USERSESSION);
        if ("ValueSet".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.VALUESET);
        if ("VerificationResult".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.VERIFICATIONRESULT);
        if ("VisionPrescription".equals(codeString))
          return new Enumeration<FHIRDefinedType>(this, FHIRDefinedType.VISIONPRESCRIPTION);
        throw new FHIRException("Unknown FHIRDefinedType code '"+codeString+"'");
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
      if (code == FHIRDefinedType.CONTACTDETAIL)
        return "ContactDetail";
      if (code == FHIRDefinedType.CONTACTPOINT)
        return "ContactPoint";
      if (code == FHIRDefinedType.CONTRIBUTOR)
        return "Contributor";
      if (code == FHIRDefinedType.COUNT)
        return "Count";
      if (code == FHIRDefinedType.DATAREQUIREMENT)
        return "DataRequirement";
      if (code == FHIRDefinedType.DISTANCE)
        return "Distance";
      if (code == FHIRDefinedType.DOSAGE)
        return "Dosage";
      if (code == FHIRDefinedType.DURATION)
        return "Duration";
      if (code == FHIRDefinedType.ELEMENT)
        return "Element";
      if (code == FHIRDefinedType.ELEMENTDEFINITION)
        return "ElementDefinition";
      if (code == FHIRDefinedType.EXPRESSION)
        return "Expression";
      if (code == FHIRDefinedType.EXTENSION)
        return "Extension";
      if (code == FHIRDefinedType.HUMANNAME)
        return "HumanName";
      if (code == FHIRDefinedType.IDENTIFIER)
        return "Identifier";
      if (code == FHIRDefinedType.MARKETINGSTATUS)
        return "MarketingStatus";
      if (code == FHIRDefinedType.META)
        return "Meta";
      if (code == FHIRDefinedType.MONEY)
        return "Money";
      if (code == FHIRDefinedType.MONEYQUANTITY)
        return "MoneyQuantity";
      if (code == FHIRDefinedType.NARRATIVE)
        return "Narrative";
      if (code == FHIRDefinedType.PARAMETERDEFINITION)
        return "ParameterDefinition";
      if (code == FHIRDefinedType.PERIOD)
        return "Period";
      if (code == FHIRDefinedType.PRODCHARACTERISTIC)
        return "ProdCharacteristic";
      if (code == FHIRDefinedType.PRODUCTSHELFLIFE)
        return "ProductShelfLife";
      if (code == FHIRDefinedType.QUANTITY)
        return "Quantity";
      if (code == FHIRDefinedType.RANGE)
        return "Range";
      if (code == FHIRDefinedType.RATIO)
        return "Ratio";
      if (code == FHIRDefinedType.REFERENCE)
        return "Reference";
      if (code == FHIRDefinedType.RELATEDARTIFACT)
        return "RelatedArtifact";
      if (code == FHIRDefinedType.SAMPLEDDATA)
        return "SampledData";
      if (code == FHIRDefinedType.SIGNATURE)
        return "Signature";
      if (code == FHIRDefinedType.SIMPLEQUANTITY)
        return "SimpleQuantity";
      if (code == FHIRDefinedType.SUBSTANCEAMOUNT)
        return "SubstanceAmount";
      if (code == FHIRDefinedType.SUBSTANCEMOIETY)
        return "SubstanceMoiety";
      if (code == FHIRDefinedType.TIMING)
        return "Timing";
      if (code == FHIRDefinedType.TRIGGERDEFINITION)
        return "TriggerDefinition";
      if (code == FHIRDefinedType.USAGECONTEXT)
        return "UsageContext";
      if (code == FHIRDefinedType.BASE64BINARY)
        return "base64Binary";
      if (code == FHIRDefinedType.BOOLEAN)
        return "boolean";
      if (code == FHIRDefinedType.CANONICAL)
        return "canonical";
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
      if (code == FHIRDefinedType.URL)
        return "url";
      if (code == FHIRDefinedType.UUID)
        return "uuid";
      if (code == FHIRDefinedType.XHTML)
        return "xhtml";
      if (code == FHIRDefinedType.ACCOUNT)
        return "Account";
      if (code == FHIRDefinedType.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == FHIRDefinedType.ADVERSEEVENT)
        return "AdverseEvent";
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
      if (code == FHIRDefinedType.BIOLOGICALLYDERIVEDPRODUCT)
        return "BiologicallyDerivedProduct";
      if (code == FHIRDefinedType.BODYSTRUCTURE)
        return "BodyStructure";
      if (code == FHIRDefinedType.BUNDLE)
        return "Bundle";
      if (code == FHIRDefinedType.CAPABILITYSTATEMENT)
        return "CapabilityStatement";
      if (code == FHIRDefinedType.CAREPLAN)
        return "CarePlan";
      if (code == FHIRDefinedType.CARETEAM)
        return "CareTeam";
      if (code == FHIRDefinedType.CATALOGENTRY)
        return "CatalogEntry";
      if (code == FHIRDefinedType.CHARGEITEM)
        return "ChargeItem";
      if (code == FHIRDefinedType.CHARGEITEMDEFINITION)
        return "ChargeItemDefinition";
      if (code == FHIRDefinedType.CLAIM)
        return "Claim";
      if (code == FHIRDefinedType.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == FHIRDefinedType.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == FHIRDefinedType.CODESYSTEM)
        return "CodeSystem";
      if (code == FHIRDefinedType.COMMUNICATION)
        return "Communication";
      if (code == FHIRDefinedType.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == FHIRDefinedType.COMPARTMENTDEFINITION)
        return "CompartmentDefinition";
      if (code == FHIRDefinedType.COMPOSITION)
        return "Composition";
      if (code == FHIRDefinedType.CONCEPTMAP)
        return "ConceptMap";
      if (code == FHIRDefinedType.CONDITION)
        return "Condition";
      if (code == FHIRDefinedType.CONSENT)
        return "Consent";
      if (code == FHIRDefinedType.CONTRACT)
        return "Contract";
      if (code == FHIRDefinedType.COVERAGE)
        return "Coverage";
      if (code == FHIRDefinedType.COVERAGEELIGIBILITYREQUEST)
        return "CoverageEligibilityRequest";
      if (code == FHIRDefinedType.COVERAGEELIGIBILITYRESPONSE)
        return "CoverageEligibilityResponse";
      if (code == FHIRDefinedType.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == FHIRDefinedType.DEVICE)
        return "Device";
      if (code == FHIRDefinedType.DEVICEDEFINITION)
        return "DeviceDefinition";
      if (code == FHIRDefinedType.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == FHIRDefinedType.DEVICEREQUEST)
        return "DeviceRequest";
      if (code == FHIRDefinedType.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == FHIRDefinedType.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == FHIRDefinedType.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == FHIRDefinedType.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == FHIRDefinedType.DOMAINRESOURCE)
        return "DomainResource";
      if (code == FHIRDefinedType.ENCOUNTER)
        return "Encounter";
      if (code == FHIRDefinedType.ENDPOINT)
        return "Endpoint";
      if (code == FHIRDefinedType.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == FHIRDefinedType.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == FHIRDefinedType.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == FHIRDefinedType.EVENTDEFINITION)
        return "EventDefinition";
      if (code == FHIRDefinedType.EXAMPLESCENARIO)
        return "ExampleScenario";
      if (code == FHIRDefinedType.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == FHIRDefinedType.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == FHIRDefinedType.FLAG)
        return "Flag";
      if (code == FHIRDefinedType.GOAL)
        return "Goal";
      if (code == FHIRDefinedType.GRAPHDEFINITION)
        return "GraphDefinition";
      if (code == FHIRDefinedType.GROUP)
        return "Group";
      if (code == FHIRDefinedType.GUIDANCERESPONSE)
        return "GuidanceResponse";
      if (code == FHIRDefinedType.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == FHIRDefinedType.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == FHIRDefinedType.IMMUNIZATION)
        return "Immunization";
      if (code == FHIRDefinedType.IMMUNIZATIONEVALUATION)
        return "ImmunizationEvaluation";
      if (code == FHIRDefinedType.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == FHIRDefinedType.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == FHIRDefinedType.INSURANCEPLAN)
        return "InsurancePlan";
      if (code == FHIRDefinedType.INVOICE)
        return "Invoice";
      if (code == FHIRDefinedType.ITEMINSTANCE)
        return "ItemInstance";
      if (code == FHIRDefinedType.LIBRARY)
        return "Library";
      if (code == FHIRDefinedType.LINKAGE)
        return "Linkage";
      if (code == FHIRDefinedType.LIST)
        return "List";
      if (code == FHIRDefinedType.LOCATION)
        return "Location";
      if (code == FHIRDefinedType.MEASURE)
        return "Measure";
      if (code == FHIRDefinedType.MEASUREREPORT)
        return "MeasureReport";
      if (code == FHIRDefinedType.MEDIA)
        return "Media";
      if (code == FHIRDefinedType.MEDICATION)
        return "Medication";
      if (code == FHIRDefinedType.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == FHIRDefinedType.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == FHIRDefinedType.MEDICATIONKNOWLEDGE)
        return "MedicationKnowledge";
      if (code == FHIRDefinedType.MEDICATIONREQUEST)
        return "MedicationRequest";
      if (code == FHIRDefinedType.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == FHIRDefinedType.MEDICINALPRODUCT)
        return "MedicinalProduct";
      if (code == FHIRDefinedType.MEDICINALPRODUCTAUTHORIZATION)
        return "MedicinalProductAuthorization";
      if (code == FHIRDefinedType.MEDICINALPRODUCTCLINICALS)
        return "MedicinalProductClinicals";
      if (code == FHIRDefinedType.MEDICINALPRODUCTCONTRAINDICATION)
        return "MedicinalProductContraindication";
      if (code == FHIRDefinedType.MEDICINALPRODUCTDEVICESPEC)
        return "MedicinalProductDeviceSpec";
      if (code == FHIRDefinedType.MEDICINALPRODUCTINDICATION)
        return "MedicinalProductIndication";
      if (code == FHIRDefinedType.MEDICINALPRODUCTINGREDIENT)
        return "MedicinalProductIngredient";
      if (code == FHIRDefinedType.MEDICINALPRODUCTINTERACTION)
        return "MedicinalProductInteraction";
      if (code == FHIRDefinedType.MEDICINALPRODUCTMANUFACTURED)
        return "MedicinalProductManufactured";
      if (code == FHIRDefinedType.MEDICINALPRODUCTPACKAGED)
        return "MedicinalProductPackaged";
      if (code == FHIRDefinedType.MEDICINALPRODUCTPHARMACEUTICAL)
        return "MedicinalProductPharmaceutical";
      if (code == FHIRDefinedType.MEDICINALPRODUCTUNDESIRABLEEFFECT)
        return "MedicinalProductUndesirableEffect";
      if (code == FHIRDefinedType.MESSAGEDEFINITION)
        return "MessageDefinition";
      if (code == FHIRDefinedType.MESSAGEHEADER)
        return "MessageHeader";
      if (code == FHIRDefinedType.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == FHIRDefinedType.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == FHIRDefinedType.OBSERVATION)
        return "Observation";
      if (code == FHIRDefinedType.OBSERVATIONDEFINITION)
        return "ObservationDefinition";
      if (code == FHIRDefinedType.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == FHIRDefinedType.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == FHIRDefinedType.ORGANIZATION)
        return "Organization";
      if (code == FHIRDefinedType.ORGANIZATIONAFFILIATION)
        return "OrganizationAffiliation";
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
      if (code == FHIRDefinedType.PLANDEFINITION)
        return "PlanDefinition";
      if (code == FHIRDefinedType.PRACTITIONER)
        return "Practitioner";
      if (code == FHIRDefinedType.PRACTITIONERROLE)
        return "PractitionerRole";
      if (code == FHIRDefinedType.PROCEDURE)
        return "Procedure";
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
      if (code == FHIRDefinedType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == FHIRDefinedType.REQUESTGROUP)
        return "RequestGroup";
      if (code == FHIRDefinedType.RESEARCHSTUDY)
        return "ResearchStudy";
      if (code == FHIRDefinedType.RESEARCHSUBJECT)
        return "ResearchSubject";
      if (code == FHIRDefinedType.RESOURCE)
        return "Resource";
      if (code == FHIRDefinedType.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == FHIRDefinedType.SCHEDULE)
        return "Schedule";
      if (code == FHIRDefinedType.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == FHIRDefinedType.SEQUENCE)
        return "Sequence";
      if (code == FHIRDefinedType.SERVICEREQUEST)
        return "ServiceRequest";
      if (code == FHIRDefinedType.SLOT)
        return "Slot";
      if (code == FHIRDefinedType.SPECIMEN)
        return "Specimen";
      if (code == FHIRDefinedType.SPECIMENDEFINITION)
        return "SpecimenDefinition";
      if (code == FHIRDefinedType.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == FHIRDefinedType.STRUCTUREMAP)
        return "StructureMap";
      if (code == FHIRDefinedType.SUBSCRIPTION)
        return "Subscription";
      if (code == FHIRDefinedType.SUBSTANCE)
        return "Substance";
      if (code == FHIRDefinedType.SUBSTANCEPOLYMER)
        return "SubstancePolymer";
      if (code == FHIRDefinedType.SUBSTANCEREFERENCEINFORMATION)
        return "SubstanceReferenceInformation";
      if (code == FHIRDefinedType.SUBSTANCESPECIFICATION)
        return "SubstanceSpecification";
      if (code == FHIRDefinedType.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == FHIRDefinedType.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == FHIRDefinedType.TASK)
        return "Task";
      if (code == FHIRDefinedType.TERMINOLOGYCAPABILITIES)
        return "TerminologyCapabilities";
      if (code == FHIRDefinedType.TESTREPORT)
        return "TestReport";
      if (code == FHIRDefinedType.TESTSCRIPT)
        return "TestScript";
      if (code == FHIRDefinedType.USERSESSION)
        return "UserSession";
      if (code == FHIRDefinedType.VALUESET)
        return "ValueSet";
      if (code == FHIRDefinedType.VERIFICATIONRESULT)
        return "VerificationResult";
      if (code == FHIRDefinedType.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    public String toSystem(FHIRDefinedType code) {
      return code.getSystem();
      }
    }

    public enum KnowledgeResourceType {
        /**
         * The definition of a specific activity to be taken, independent of any particular patient or context.
         */
        ACTIVITYDEFINITION, 
        /**
         * A set of codes drawn from one or more code systems.
         */
        CODESYSTEM, 
        /**
         * A map from one set of concepts to one or more other concepts.
         */
        CONCEPTMAP, 
        /**
         * Represents a library of quality improvement components.
         */
        LIBRARY, 
        /**
         * A quality measure definition.
         */
        MEASURE, 
        /**
         * The definition of a plan for a series of actions, independent of any specific patient or context.
         */
        PLANDEFINITION, 
        /**
         * Structural Definition.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * A set of codes drawn from one or more code systems.
         */
        VALUESET, 
        /**
         * added to help the parsers
         */
        NULL;
        public static KnowledgeResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("CodeSystem".equals(codeString))
          return CODESYSTEM;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Library".equals(codeString))
          return LIBRARY;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        throw new FHIRException("Unknown KnowledgeResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case CODESYSTEM: return "CodeSystem";
            case CONCEPTMAP: return "ConceptMap";
            case LIBRARY: return "Library";
            case MEASURE: return "Measure";
            case PLANDEFINITION: return "PlanDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case VALUESET: return "ValueSet";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVITYDEFINITION: return "http://hl7.org/fhir/knowledge-resource-types";
            case CODESYSTEM: return "http://hl7.org/fhir/knowledge-resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/knowledge-resource-types";
            case LIBRARY: return "http://hl7.org/fhir/knowledge-resource-types";
            case MEASURE: return "http://hl7.org/fhir/knowledge-resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/knowledge-resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/knowledge-resource-types";
            case STRUCTUREMAP: return "http://hl7.org/fhir/knowledge-resource-types";
            case VALUESET: return "http://hl7.org/fhir/knowledge-resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVITYDEFINITION: return "The definition of a specific activity to be taken, independent of any particular patient or context.";
            case CODESYSTEM: return "A set of codes drawn from one or more code systems.";
            case CONCEPTMAP: return "A map from one set of concepts to one or more other concepts.";
            case LIBRARY: return "Represents a library of quality improvement components.";
            case MEASURE: return "A quality measure definition.";
            case PLANDEFINITION: return "The definition of a plan for a series of actions, independent of any specific patient or context.";
            case STRUCTUREDEFINITION: return "Structural Definition.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case VALUESET: return "A set of codes drawn from one or more code systems.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case CODESYSTEM: return "CodeSystem";
            case CONCEPTMAP: return "ConceptMap";
            case LIBRARY: return "Library";
            case MEASURE: return "Measure";
            case PLANDEFINITION: return "PlanDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case VALUESET: return "ValueSet";
            default: return "?";
          }
        }
    }

  public static class KnowledgeResourceTypeEnumFactory implements EnumFactory<KnowledgeResourceType> {
    public KnowledgeResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ActivityDefinition".equals(codeString))
          return KnowledgeResourceType.ACTIVITYDEFINITION;
        if ("CodeSystem".equals(codeString))
          return KnowledgeResourceType.CODESYSTEM;
        if ("ConceptMap".equals(codeString))
          return KnowledgeResourceType.CONCEPTMAP;
        if ("Library".equals(codeString))
          return KnowledgeResourceType.LIBRARY;
        if ("Measure".equals(codeString))
          return KnowledgeResourceType.MEASURE;
        if ("PlanDefinition".equals(codeString))
          return KnowledgeResourceType.PLANDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return KnowledgeResourceType.STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return KnowledgeResourceType.STRUCTUREMAP;
        if ("ValueSet".equals(codeString))
          return KnowledgeResourceType.VALUESET;
        throw new IllegalArgumentException("Unknown KnowledgeResourceType code '"+codeString+"'");
        }
        public Enumeration<KnowledgeResourceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<KnowledgeResourceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.ACTIVITYDEFINITION);
        if ("CodeSystem".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.CODESYSTEM);
        if ("ConceptMap".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.CONCEPTMAP);
        if ("Library".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.LIBRARY);
        if ("Measure".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.MEASURE);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.PLANDEFINITION);
        if ("StructureDefinition".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.STRUCTUREDEFINITION);
        if ("StructureMap".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.STRUCTUREMAP);
        if ("ValueSet".equals(codeString))
          return new Enumeration<KnowledgeResourceType>(this, KnowledgeResourceType.VALUESET);
        throw new FHIRException("Unknown KnowledgeResourceType code '"+codeString+"'");
        }
    public String toCode(KnowledgeResourceType code) {
      if (code == KnowledgeResourceType.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == KnowledgeResourceType.CODESYSTEM)
        return "CodeSystem";
      if (code == KnowledgeResourceType.CONCEPTMAP)
        return "ConceptMap";
      if (code == KnowledgeResourceType.LIBRARY)
        return "Library";
      if (code == KnowledgeResourceType.MEASURE)
        return "Measure";
      if (code == KnowledgeResourceType.PLANDEFINITION)
        return "PlanDefinition";
      if (code == KnowledgeResourceType.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == KnowledgeResourceType.STRUCTUREMAP)
        return "StructureMap";
      if (code == KnowledgeResourceType.VALUESET)
        return "ValueSet";
      return "?";
      }
    public String toSystem(KnowledgeResourceType code) {
      return code.getSystem();
      }
    }

    public enum MessageEvent {
        /**
         * added to help the parsers
         */
        NULL;
        public static MessageEvent fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        throw new FHIRException("Unknown MessageEvent code '"+codeString+"'");
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
        public Enumeration<MessageEvent> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MessageEvent>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        throw new FHIRException("Unknown MessageEvent code '"+codeString+"'");
        }
    public String toCode(MessageEvent code) {
      return "?";
      }
    public String toSystem(MessageEvent code) {
      return code.getSystem();
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
        public static NoteType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("display".equals(codeString))
          return DISPLAY;
        if ("print".equals(codeString))
          return PRINT;
        if ("printoper".equals(codeString))
          return PRINTOPER;
        throw new FHIRException("Unknown NoteType code '"+codeString+"'");
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
        public Enumeration<NoteType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<NoteType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("display".equals(codeString))
          return new Enumeration<NoteType>(this, NoteType.DISPLAY);
        if ("print".equals(codeString))
          return new Enumeration<NoteType>(this, NoteType.PRINT);
        if ("printoper".equals(codeString))
          return new Enumeration<NoteType>(this, NoteType.PRINTOPER);
        throw new FHIRException("Unknown NoteType code '"+codeString+"'");
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
    public String toSystem(NoteType code) {
      return code.getSystem();
      }
    }

    public enum PublicationStatus {
        /**
         * This resource is still under development and is not yet considered to be ready for normal use.
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
         * The authoring system does not know which of the status values currently applies for this resource.  Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PublicationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown PublicationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/publication-status";
            case ACTIVE: return "http://hl7.org/fhir/publication-status";
            case RETIRED: return "http://hl7.org/fhir/publication-status";
            case UNKNOWN: return "http://hl7.org/fhir/publication-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This resource is still under development and is not yet considered to be ready for normal use.";
            case ACTIVE: return "This resource is ready for normal use.";
            case RETIRED: return "This resource has been withdrawn or superseded and should no longer be used.";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this resource.  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case RETIRED: return "Retired";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class PublicationStatusEnumFactory implements EnumFactory<PublicationStatus> {
    public PublicationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return PublicationStatus.DRAFT;
        if ("active".equals(codeString))
          return PublicationStatus.ACTIVE;
        if ("retired".equals(codeString))
          return PublicationStatus.RETIRED;
        if ("unknown".equals(codeString))
          return PublicationStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown PublicationStatus code '"+codeString+"'");
        }
        public Enumeration<PublicationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<PublicationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<PublicationStatus>(this, PublicationStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<PublicationStatus>(this, PublicationStatus.ACTIVE);
        if ("retired".equals(codeString))
          return new Enumeration<PublicationStatus>(this, PublicationStatus.RETIRED);
        if ("unknown".equals(codeString))
          return new Enumeration<PublicationStatus>(this, PublicationStatus.UNKNOWN);
        throw new FHIRException("Unknown PublicationStatus code '"+codeString+"'");
        }
    public String toCode(PublicationStatus code) {
      if (code == PublicationStatus.DRAFT)
        return "draft";
      if (code == PublicationStatus.ACTIVE)
        return "active";
      if (code == PublicationStatus.RETIRED)
        return "retired";
      if (code == PublicationStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(PublicationStatus code) {
      return code.getSystem();
      }
    }

    public enum RemittanceOutcome {
        /**
         * The Claim/Pre-authorization/Pre-determination has been received but processing has not begun.
         */
        QUEUED, 
        /**
         * The processing has completed without errors
         */
        COMPLETE, 
        /**
         * One or more errors have been detected in the Claim
         */
        ERROR, 
        /**
         * No errors have been detected in the Claim and some of the adjudication has been performed.
         */
        PARTIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RemittanceOutcome fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("queued".equals(codeString))
          return QUEUED;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("error".equals(codeString))
          return ERROR;
        if ("partial".equals(codeString))
          return PARTIAL;
        throw new FHIRException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case QUEUED: return "queued";
            case COMPLETE: return "complete";
            case ERROR: return "error";
            case PARTIAL: return "partial";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case QUEUED: return "http://hl7.org/fhir/remittance-outcome";
            case COMPLETE: return "http://hl7.org/fhir/remittance-outcome";
            case ERROR: return "http://hl7.org/fhir/remittance-outcome";
            case PARTIAL: return "http://hl7.org/fhir/remittance-outcome";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case QUEUED: return "The Claim/Pre-authorization/Pre-determination has been received but processing has not begun.";
            case COMPLETE: return "The processing has completed without errors";
            case ERROR: return "One or more errors have been detected in the Claim";
            case PARTIAL: return "No errors have been detected in the Claim and some of the adjudication has been performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case QUEUED: return "Queued";
            case COMPLETE: return "Processing Complete";
            case ERROR: return "Error";
            case PARTIAL: return "Partial Processing";
            default: return "?";
          }
        }
    }

  public static class RemittanceOutcomeEnumFactory implements EnumFactory<RemittanceOutcome> {
    public RemittanceOutcome fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("queued".equals(codeString))
          return RemittanceOutcome.QUEUED;
        if ("complete".equals(codeString))
          return RemittanceOutcome.COMPLETE;
        if ("error".equals(codeString))
          return RemittanceOutcome.ERROR;
        if ("partial".equals(codeString))
          return RemittanceOutcome.PARTIAL;
        throw new IllegalArgumentException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
        public Enumeration<RemittanceOutcome> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RemittanceOutcome>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("queued".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.QUEUED);
        if ("complete".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.COMPLETE);
        if ("error".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.ERROR);
        if ("partial".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.PARTIAL);
        throw new FHIRException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
    public String toCode(RemittanceOutcome code) {
      if (code == RemittanceOutcome.QUEUED)
        return "queued";
      if (code == RemittanceOutcome.COMPLETE)
        return "complete";
      if (code == RemittanceOutcome.ERROR)
        return "error";
      if (code == RemittanceOutcome.PARTIAL)
        return "partial";
      return "?";
      }
    public String toSystem(RemittanceOutcome code) {
      return code.getSystem();
      }
    }

    public enum RequestResourceType {
        /**
         * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT, 
        /**
         * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENTRESPONSE, 
        /**
         * Healthcare plan for patient or group.
         */
        CAREPLAN, 
        /**
         * Claim, Pre-determination or Pre-authorization.
         */
        CLAIM, 
        /**
         * A request for information to be sent to a receiver.
         */
        COMMUNICATIONREQUEST, 
        /**
         * Legal Agreement.
         */
        CONTRACT, 
        /**
         * Medical device request.
         */
        DEVICEREQUEST, 
        /**
         * Enrollment request.
         */
        ENROLLMENTREQUEST, 
        /**
         * Guidance or advice relating to an immunization.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * Ordering of medication for patient or group.
         */
        MEDICATIONREQUEST, 
        /**
         * Diet, formula or nutritional supplement request.
         */
        NUTRITIONORDER, 
        /**
         * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICEREQUEST, 
        /**
         * Request for a medication, substance or device.
         */
        SUPPLYREQUEST, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * Prescription for vision correction products for a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RequestResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Appointment".equals(codeString))
          return APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return APPOINTMENTRESPONSE;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("DeviceRequest".equals(codeString))
          return DEVICEREQUEST;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("ServiceRequest".equals(codeString))
          return SERVICEREQUEST;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        throw new FHIRException("Unknown RequestResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case CONTRACT: return "Contract";
            case DEVICEREQUEST: return "DeviceRequest";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case NUTRITIONORDER: return "NutritionOrder";
            case SERVICEREQUEST: return "ServiceRequest";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case APPOINTMENT: return "http://hl7.org/fhir/request-resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/request-resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/request-resource-types";
            case CLAIM: return "http://hl7.org/fhir/request-resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/request-resource-types";
            case CONTRACT: return "http://hl7.org/fhir/request-resource-types";
            case DEVICEREQUEST: return "http://hl7.org/fhir/request-resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/request-resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/request-resource-types";
            case MEDICATIONREQUEST: return "http://hl7.org/fhir/request-resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/request-resource-types";
            case SERVICEREQUEST: return "http://hl7.org/fhir/request-resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/request-resource-types";
            case TASK: return "http://hl7.org/fhir/request-resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/request-resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case CAREPLAN: return "Healthcare plan for patient or group.";
            case CLAIM: return "Claim, Pre-determination or Pre-authorization.";
            case COMMUNICATIONREQUEST: return "A request for information to be sent to a receiver.";
            case CONTRACT: return "Legal Agreement.";
            case DEVICEREQUEST: return "Medical device request.";
            case ENROLLMENTREQUEST: return "Enrollment request.";
            case IMMUNIZATIONRECOMMENDATION: return "Guidance or advice relating to an immunization.";
            case MEDICATIONREQUEST: return "Ordering of medication for patient or group.";
            case NUTRITIONORDER: return "Diet, formula or nutritional supplement request.";
            case SERVICEREQUEST: return "A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.";
            case SUPPLYREQUEST: return "Request for a medication, substance or device.";
            case TASK: return "A task to be performed.";
            case VISIONPRESCRIPTION: return "Prescription for vision correction products for a patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case CAREPLAN: return "CarePlan";
            case CLAIM: return "Claim";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case CONTRACT: return "Contract";
            case DEVICEREQUEST: return "DeviceRequest";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case NUTRITIONORDER: return "NutritionOrder";
            case SERVICEREQUEST: return "ServiceRequest";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
    }

  public static class RequestResourceTypeEnumFactory implements EnumFactory<RequestResourceType> {
    public RequestResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Appointment".equals(codeString))
          return RequestResourceType.APPOINTMENT;
        if ("AppointmentResponse".equals(codeString))
          return RequestResourceType.APPOINTMENTRESPONSE;
        if ("CarePlan".equals(codeString))
          return RequestResourceType.CAREPLAN;
        if ("Claim".equals(codeString))
          return RequestResourceType.CLAIM;
        if ("CommunicationRequest".equals(codeString))
          return RequestResourceType.COMMUNICATIONREQUEST;
        if ("Contract".equals(codeString))
          return RequestResourceType.CONTRACT;
        if ("DeviceRequest".equals(codeString))
          return RequestResourceType.DEVICEREQUEST;
        if ("EnrollmentRequest".equals(codeString))
          return RequestResourceType.ENROLLMENTREQUEST;
        if ("ImmunizationRecommendation".equals(codeString))
          return RequestResourceType.IMMUNIZATIONRECOMMENDATION;
        if ("MedicationRequest".equals(codeString))
          return RequestResourceType.MEDICATIONREQUEST;
        if ("NutritionOrder".equals(codeString))
          return RequestResourceType.NUTRITIONORDER;
        if ("ServiceRequest".equals(codeString))
          return RequestResourceType.SERVICEREQUEST;
        if ("SupplyRequest".equals(codeString))
          return RequestResourceType.SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return RequestResourceType.TASK;
        if ("VisionPrescription".equals(codeString))
          return RequestResourceType.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown RequestResourceType code '"+codeString+"'");
        }
        public Enumeration<RequestResourceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RequestResourceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Appointment".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.APPOINTMENT);
        if ("AppointmentResponse".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.APPOINTMENTRESPONSE);
        if ("CarePlan".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.CAREPLAN);
        if ("Claim".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.CLAIM);
        if ("CommunicationRequest".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.COMMUNICATIONREQUEST);
        if ("Contract".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.CONTRACT);
        if ("DeviceRequest".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.DEVICEREQUEST);
        if ("EnrollmentRequest".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.ENROLLMENTREQUEST);
        if ("ImmunizationRecommendation".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.IMMUNIZATIONRECOMMENDATION);
        if ("MedicationRequest".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.MEDICATIONREQUEST);
        if ("NutritionOrder".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.NUTRITIONORDER);
        if ("ServiceRequest".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.SERVICEREQUEST);
        if ("SupplyRequest".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.SUPPLYREQUEST);
        if ("Task".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.TASK);
        if ("VisionPrescription".equals(codeString))
          return new Enumeration<RequestResourceType>(this, RequestResourceType.VISIONPRESCRIPTION);
        throw new FHIRException("Unknown RequestResourceType code '"+codeString+"'");
        }
    public String toCode(RequestResourceType code) {
      if (code == RequestResourceType.APPOINTMENT)
        return "Appointment";
      if (code == RequestResourceType.APPOINTMENTRESPONSE)
        return "AppointmentResponse";
      if (code == RequestResourceType.CAREPLAN)
        return "CarePlan";
      if (code == RequestResourceType.CLAIM)
        return "Claim";
      if (code == RequestResourceType.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == RequestResourceType.CONTRACT)
        return "Contract";
      if (code == RequestResourceType.DEVICEREQUEST)
        return "DeviceRequest";
      if (code == RequestResourceType.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == RequestResourceType.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == RequestResourceType.MEDICATIONREQUEST)
        return "MedicationRequest";
      if (code == RequestResourceType.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == RequestResourceType.SERVICEREQUEST)
        return "ServiceRequest";
      if (code == RequestResourceType.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == RequestResourceType.TASK)
        return "Task";
      if (code == RequestResourceType.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    public String toSystem(RequestResourceType code) {
      return code.getSystem();
      }
    }

    public enum ResourceType {
        /**
         * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.
         */
        ACCOUNT, 
        /**
         * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
         */
        ACTIVITYDEFINITION, 
        /**
         * Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.
         */
        ADVERSEEVENT, 
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
         * A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY, 
        /**
         * A material substance originating from a biological entity intended to be transplanted or infused
into another (possibly the same) biological entity.
         */
        BIOLOGICALLYDERIVEDPRODUCT, 
        /**
         * Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
         */
        BODYSTRUCTURE, 
        /**
         * A container for a collection of resources.
         */
        BUNDLE, 
        /**
         * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        CAPABILITYSTATEMENT, 
        /**
         * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CAREPLAN, 
        /**
         * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.
         */
        CARETEAM, 
        /**
         * Catalog entries are wrappers that contextualize items included in a catalog.
         */
        CATALOGENTRY, 
        /**
         * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.
         */
        CHARGEITEM, 
        /**
         * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.
         */
        CHARGEITEMDEFINITION, 
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
         * The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.
         */
        CODESYSTEM, 
        /**
         * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
         */
        COMMUNICATION, 
        /**
         * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATIONREQUEST, 
        /**
         * A compartment definition that defines how resources are accessed on a server.
         */
        COMPARTMENTDEFINITION, 
        /**
         * A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).
         */
        COMPOSITION, 
        /**
         * A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.
         */
        CONCEPTMAP, 
        /**
         * A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.
         */
        CONDITION, 
        /**
         * A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
         */
        CONSENT, 
        /**
         * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
         */
        CONTRACT, 
        /**
         * Financial instrument which may be used to reimburse or pay for health care products and services.
         */
        COVERAGE, 
        /**
         * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
         */
        COVERAGEELIGIBILITYREQUEST, 
        /**
         * This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
         */
        COVERAGEELIGIBILITYRESPONSE, 
        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTEDISSUE, 
        /**
         * This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).
         */
        DEVICE, 
        /**
         * The characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICEDEFINITION, 
        /**
         * Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICEMETRIC, 
        /**
         * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
         */
        DEVICEREQUEST, 
        /**
         * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
         */
        DEVICEUSESTATEMENT, 
        /**
         * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
         */
        DIAGNOSTICREPORT, 
        /**
         * A collection of documents compiled for a purpose together with metadata that applies to the collection.
         */
        DOCUMENTMANIFEST, 
        /**
         * A reference to a document.
         */
        DOCUMENTREFERENCE, 
        /**
         * A resource that includes narrative, extensions, and contained resources.
         */
        DOMAINRESOURCE, 
        /**
         * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
         */
        ENCOUNTER, 
        /**
         * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
         */
        ENDPOINT, 
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
         * The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENTDEFINITION, 
        /**
         * Example of workflow instance.
         */
        EXAMPLESCENARIO, 
        /**
         * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATIONOFBENEFIT, 
        /**
         * Significant health conditions for a person related to the patient relevant in the context of care for the patient.
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
         * A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.
         */
        GRAPHDEFINITION, 
        /**
         * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.
         */
        GROUP, 
        /**
         * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
         */
        GUIDANCERESPONSE, 
        /**
         * The details of a healthcare service available at a location.
         */
        HEALTHCARESERVICE, 
        /**
         * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
         */
        IMAGINGSTUDY, 
        /**
         * Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.
         */
        IMMUNIZATION, 
        /**
         * Describes a comparison of an immunization event against published recommendations to determine if the administration is "valid" in relation to those  recommendations.
         */
        IMMUNIZATIONEVALUATION, 
        /**
         * A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.
         */
        IMMUNIZATIONRECOMMENDATION, 
        /**
         * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
         */
        IMPLEMENTATIONGUIDE, 
        /**
         * Details of a Health Insurance product/plan provided by an organization.
         */
        INSURANCEPLAN, 
        /**
         * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.
         */
        INVOICE, 
        /**
         * A physical, countable instance of an item, for example one box or one unit.
         */
        ITEMINSTANCE, 
        /**
         * The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.
         */
        LIBRARY, 
        /**
         * Identifies two or more records (resource instances) that are referring to the same real-world "occurrence".
         */
        LINKAGE, 
        /**
         * A set of information summarized from a list of other resources.
         */
        LIST, 
        /**
         * Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.
         */
        LOCATION, 
        /**
         * The Measure resource provides the definition of a quality measure.
         */
        MEASURE, 
        /**
         * The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.
         */
        MEASUREREPORT, 
        /**
         * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
         */
        MEDIA, 
        /**
         * This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.
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
         * Information about a medication that is used to support knowledge.
         */
        MEDICATIONKNOWLEDGE, 
        /**
         * An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.
         */
        MEDICATIONREQUEST, 
        /**
         * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
         */
        MEDICATIONSTATEMENT, 
        /**
         * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
         */
        MEDICINALPRODUCT, 
        /**
         * The regulatory authorization of a medicinal product.
         */
        MEDICINALPRODUCTAUTHORIZATION, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCLINICALS, 
        /**
         * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
         */
        MEDICINALPRODUCTCONTRAINDICATION, 
        /**
         * A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.
         */
        MEDICINALPRODUCTDEVICESPEC, 
        /**
         * Indication for the Medicinal Product.
         */
        MEDICINALPRODUCTINDICATION, 
        /**
         * An ingredient of a manufactured item or pharmaceutical product.
         */
        MEDICINALPRODUCTINGREDIENT, 
        /**
         * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
         */
        MEDICINALPRODUCTINTERACTION, 
        /**
         * The manufactured item as contained in the packaged medicinal product.
         */
        MEDICINALPRODUCTMANUFACTURED, 
        /**
         * A medicinal product in a container or package.
         */
        MEDICINALPRODUCTPACKAGED, 
        /**
         * A pharmaceutical product described in terms of its composition and dose form.
         */
        MEDICINALPRODUCTPHARMACEUTICAL, 
        /**
         * Describe the undesirable effects of the medicinal product.
         */
        MEDICINALPRODUCTUNDESIRABLEEFFECT, 
        /**
         * Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.
         */
        MESSAGEDEFINITION, 
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
         * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.
         */
        OBSERVATIONDEFINITION, 
        /**
         * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
         */
        OPERATIONDEFINITION, 
        /**
         * A collection of error, warning or information messages that result from a system action.
         */
        OPERATIONOUTCOME, 
        /**
         * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.
         */
        ORGANIZATION, 
        /**
         * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.
         */
        ORGANIZATIONAFFILIATION, 
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
         * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.
         */
        PLANDEFINITION, 
        /**
         * A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.
         */
        PRACTITIONERROLE, 
        /**
         * An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.
         */
        PROCEDURE, 
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
         * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
         */
        QUESTIONNAIRE, 
        /**
         * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.
         */
        QUESTIONNAIRERESPONSE, 
        /**
         * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give this medication after that one".
         */
        REQUESTGROUP, 
        /**
         * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
         */
        RESEARCHSTUDY, 
        /**
         * A physical entity which is the primary unit of operational and/or administrative interest in a study.
         */
        RESEARCHSUBJECT, 
        /**
         * This is the base resource type for everything.
         */
        RESOURCE, 
        /**
         * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISKASSESSMENT, 
        /**
         * A container for slots of time that may be available for booking appointments.
         */
        SCHEDULE, 
        /**
         * A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCHPARAMETER, 
        /**
         * Raw data describing a biological sequence.
         */
        SEQUENCE, 
        /**
         * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICEREQUEST, 
        /**
         * A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT, 
        /**
         * A sample to be used for analysis.
         */
        SPECIMEN, 
        /**
         * A kind of specimen with associated set of requirements.
         */
        SPECIMENDEFINITION, 
        /**
         * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an appropriate action.
         */
        SUBSCRIPTION, 
        /**
         * A homogeneous material with a definite composition.
         */
        SUBSTANCE, 
        /**
         * Todo.
         */
        SUBSTANCEPOLYMER, 
        /**
         * Todo.
         */
        SUBSTANCEREFERENCEINFORMATION, 
        /**
         * The detailed description of a substance, typically at a level beyond what is used for prescribing.
         */
        SUBSTANCESPECIFICATION, 
        /**
         * Record of delivery of what is supplied.
         */
        SUPPLYDELIVERY, 
        /**
         * A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLYREQUEST, 
        /**
         * A task to be performed.
         */
        TASK, 
        /**
         * A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        TERMINOLOGYCAPABILITIES, 
        /**
         * A summary of information based on the results of executing a TestScript.
         */
        TESTREPORT, 
        /**
         * A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.
         */
        TESTSCRIPT, 
        /**
         * Information about a user's current session.
         */
        USERSESSION, 
        /**
         * A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).
         */
        VALUESET, 
        /**
         * Describes validation requirements, source(s), status and dates for one or more elements.
         */
        VERIFICATIONRESULT, 
        /**
         * An authorization for the supply of glasses and/or contact lenses to a patient.
         */
        VISIONPRESCRIPTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Account".equals(codeString))
          return ACCOUNT;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return ADVERSEEVENT;
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
        if ("BiologicallyDerivedProduct".equals(codeString))
          return BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return CAREPLAN;
        if ("CareTeam".equals(codeString))
          return CARETEAM;
        if ("CatalogEntry".equals(codeString))
          return CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return CHARGEITEMDEFINITION;
        if ("Claim".equals(codeString))
          return CLAIM;
        if ("ClaimResponse".equals(codeString))
          return CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return CODESYSTEM;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Condition".equals(codeString))
          return CONDITION;
        if ("Consent".equals(codeString))
          return CONSENT;
        if ("Contract".equals(codeString))
          return CONTRACT;
        if ("Coverage".equals(codeString))
          return COVERAGE;
        if ("CoverageEligibilityRequest".equals(codeString))
          return COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return DETECTEDISSUE;
        if ("Device".equals(codeString))
          return DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return DEVICEDEFINITION;
        if ("DeviceMetric".equals(codeString))
          return DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return DOMAINRESOURCE;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return EXAMPLESCENARIO;
        if ("ExplanationOfBenefit".equals(codeString))
          return EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return FLAG;
        if ("Goal".equals(codeString))
          return GOAL;
        if ("GraphDefinition".equals(codeString))
          return GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return GROUP;
        if ("GuidanceResponse".equals(codeString))
          return GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return IMPLEMENTATIONGUIDE;
        if ("InsurancePlan".equals(codeString))
          return INSURANCEPLAN;
        if ("Invoice".equals(codeString))
          return INVOICE;
        if ("ItemInstance".equals(codeString))
          return ITEMINSTANCE;
        if ("Library".equals(codeString))
          return LIBRARY;
        if ("Linkage".equals(codeString))
          return LINKAGE;
        if ("List".equals(codeString))
          return LIST;
        if ("Location".equals(codeString))
          return LOCATION;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("MeasureReport".equals(codeString))
          return MEASUREREPORT;
        if ("Media".equals(codeString))
          return MEDIA;
        if ("Medication".equals(codeString))
          return MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductContraindication".equals(codeString))
          return MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return MEDICINALPRODUCTUNDESIRABLEEFFECT;
        if ("MessageDefinition".equals(codeString))
          return MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return OBSERVATIONDEFINITION;
        if ("OperationDefinition".equals(codeString))
          return OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return ORGANIZATIONAFFILIATION;
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
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return PROCEDURE;
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
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return SLOT;
        if ("Specimen".equals(codeString))
          return SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return TESTREPORT;
        if ("TestScript".equals(codeString))
          return TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return USERSESSION;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        if ("VerificationResult".equals(codeString))
          return VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return VISIONPRESCRIPTION;
        throw new FHIRException("Unknown ResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INSURANCEPLAN: return "InsurancePlan";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
            case VISIONPRESCRIPTION: return "VisionPrescription";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACCOUNT: return "http://hl7.org/fhir/resource-types";
            case ACTIVITYDEFINITION: return "http://hl7.org/fhir/resource-types";
            case ADVERSEEVENT: return "http://hl7.org/fhir/resource-types";
            case ALLERGYINTOLERANCE: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENT: return "http://hl7.org/fhir/resource-types";
            case APPOINTMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case AUDITEVENT: return "http://hl7.org/fhir/resource-types";
            case BASIC: return "http://hl7.org/fhir/resource-types";
            case BINARY: return "http://hl7.org/fhir/resource-types";
            case BIOLOGICALLYDERIVEDPRODUCT: return "http://hl7.org/fhir/resource-types";
            case BODYSTRUCTURE: return "http://hl7.org/fhir/resource-types";
            case BUNDLE: return "http://hl7.org/fhir/resource-types";
            case CAPABILITYSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case CAREPLAN: return "http://hl7.org/fhir/resource-types";
            case CARETEAM: return "http://hl7.org/fhir/resource-types";
            case CATALOGENTRY: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEM: return "http://hl7.org/fhir/resource-types";
            case CHARGEITEMDEFINITION: return "http://hl7.org/fhir/resource-types";
            case CLAIM: return "http://hl7.org/fhir/resource-types";
            case CLAIMRESPONSE: return "http://hl7.org/fhir/resource-types";
            case CLINICALIMPRESSION: return "http://hl7.org/fhir/resource-types";
            case CODESYSTEM: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATION: return "http://hl7.org/fhir/resource-types";
            case COMMUNICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case COMPARTMENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case COMPOSITION: return "http://hl7.org/fhir/resource-types";
            case CONCEPTMAP: return "http://hl7.org/fhir/resource-types";
            case CONDITION: return "http://hl7.org/fhir/resource-types";
            case CONSENT: return "http://hl7.org/fhir/resource-types";
            case CONTRACT: return "http://hl7.org/fhir/resource-types";
            case COVERAGE: return "http://hl7.org/fhir/resource-types";
            case COVERAGEELIGIBILITYREQUEST: return "http://hl7.org/fhir/resource-types";
            case COVERAGEELIGIBILITYRESPONSE: return "http://hl7.org/fhir/resource-types";
            case DETECTEDISSUE: return "http://hl7.org/fhir/resource-types";
            case DEVICE: return "http://hl7.org/fhir/resource-types";
            case DEVICEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case DEVICEMETRIC: return "http://hl7.org/fhir/resource-types";
            case DEVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case DEVICEUSESTATEMENT: return "http://hl7.org/fhir/resource-types";
            case DIAGNOSTICREPORT: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTMANIFEST: return "http://hl7.org/fhir/resource-types";
            case DOCUMENTREFERENCE: return "http://hl7.org/fhir/resource-types";
            case DOMAINRESOURCE: return "http://hl7.org/fhir/resource-types";
            case ENCOUNTER: return "http://hl7.org/fhir/resource-types";
            case ENDPOINT: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTREQUEST: return "http://hl7.org/fhir/resource-types";
            case ENROLLMENTRESPONSE: return "http://hl7.org/fhir/resource-types";
            case EPISODEOFCARE: return "http://hl7.org/fhir/resource-types";
            case EVENTDEFINITION: return "http://hl7.org/fhir/resource-types";
            case EXAMPLESCENARIO: return "http://hl7.org/fhir/resource-types";
            case EXPLANATIONOFBENEFIT: return "http://hl7.org/fhir/resource-types";
            case FAMILYMEMBERHISTORY: return "http://hl7.org/fhir/resource-types";
            case FLAG: return "http://hl7.org/fhir/resource-types";
            case GOAL: return "http://hl7.org/fhir/resource-types";
            case GRAPHDEFINITION: return "http://hl7.org/fhir/resource-types";
            case GROUP: return "http://hl7.org/fhir/resource-types";
            case GUIDANCERESPONSE: return "http://hl7.org/fhir/resource-types";
            case HEALTHCARESERVICE: return "http://hl7.org/fhir/resource-types";
            case IMAGINGSTUDY: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONEVALUATION: return "http://hl7.org/fhir/resource-types";
            case IMMUNIZATIONRECOMMENDATION: return "http://hl7.org/fhir/resource-types";
            case IMPLEMENTATIONGUIDE: return "http://hl7.org/fhir/resource-types";
            case INSURANCEPLAN: return "http://hl7.org/fhir/resource-types";
            case INVOICE: return "http://hl7.org/fhir/resource-types";
            case ITEMINSTANCE: return "http://hl7.org/fhir/resource-types";
            case LIBRARY: return "http://hl7.org/fhir/resource-types";
            case LINKAGE: return "http://hl7.org/fhir/resource-types";
            case LIST: return "http://hl7.org/fhir/resource-types";
            case LOCATION: return "http://hl7.org/fhir/resource-types";
            case MEASURE: return "http://hl7.org/fhir/resource-types";
            case MEASUREREPORT: return "http://hl7.org/fhir/resource-types";
            case MEDIA: return "http://hl7.org/fhir/resource-types";
            case MEDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONADMINISTRATION: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONDISPENSE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONKNOWLEDGE: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONREQUEST: return "http://hl7.org/fhir/resource-types";
            case MEDICATIONSTATEMENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTAUTHORIZATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCLINICALS: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTCONTRAINDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTDEVICESPEC: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINDICATION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINGREDIENT: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTINTERACTION: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTMANUFACTURED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPACKAGED: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "http://hl7.org/fhir/resource-types";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "http://hl7.org/fhir/resource-types";
            case MESSAGEDEFINITION: return "http://hl7.org/fhir/resource-types";
            case MESSAGEHEADER: return "http://hl7.org/fhir/resource-types";
            case NAMINGSYSTEM: return "http://hl7.org/fhir/resource-types";
            case NUTRITIONORDER: return "http://hl7.org/fhir/resource-types";
            case OBSERVATION: return "http://hl7.org/fhir/resource-types";
            case OBSERVATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONDEFINITION: return "http://hl7.org/fhir/resource-types";
            case OPERATIONOUTCOME: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATION: return "http://hl7.org/fhir/resource-types";
            case ORGANIZATIONAFFILIATION: return "http://hl7.org/fhir/resource-types";
            case PARAMETERS: return "http://hl7.org/fhir/resource-types";
            case PATIENT: return "http://hl7.org/fhir/resource-types";
            case PAYMENTNOTICE: return "http://hl7.org/fhir/resource-types";
            case PAYMENTRECONCILIATION: return "http://hl7.org/fhir/resource-types";
            case PERSON: return "http://hl7.org/fhir/resource-types";
            case PLANDEFINITION: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONER: return "http://hl7.org/fhir/resource-types";
            case PRACTITIONERROLE: return "http://hl7.org/fhir/resource-types";
            case PROCEDURE: return "http://hl7.org/fhir/resource-types";
            case PROCESSREQUEST: return "http://hl7.org/fhir/resource-types";
            case PROCESSRESPONSE: return "http://hl7.org/fhir/resource-types";
            case PROVENANCE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRE: return "http://hl7.org/fhir/resource-types";
            case QUESTIONNAIRERESPONSE: return "http://hl7.org/fhir/resource-types";
            case RELATEDPERSON: return "http://hl7.org/fhir/resource-types";
            case REQUESTGROUP: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSTUDY: return "http://hl7.org/fhir/resource-types";
            case RESEARCHSUBJECT: return "http://hl7.org/fhir/resource-types";
            case RESOURCE: return "http://hl7.org/fhir/resource-types";
            case RISKASSESSMENT: return "http://hl7.org/fhir/resource-types";
            case SCHEDULE: return "http://hl7.org/fhir/resource-types";
            case SEARCHPARAMETER: return "http://hl7.org/fhir/resource-types";
            case SEQUENCE: return "http://hl7.org/fhir/resource-types";
            case SERVICEREQUEST: return "http://hl7.org/fhir/resource-types";
            case SLOT: return "http://hl7.org/fhir/resource-types";
            case SPECIMEN: return "http://hl7.org/fhir/resource-types";
            case SPECIMENDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREDEFINITION: return "http://hl7.org/fhir/resource-types";
            case STRUCTUREMAP: return "http://hl7.org/fhir/resource-types";
            case SUBSCRIPTION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCE: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEPOLYMER: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCEREFERENCEINFORMATION: return "http://hl7.org/fhir/resource-types";
            case SUBSTANCESPECIFICATION: return "http://hl7.org/fhir/resource-types";
            case SUPPLYDELIVERY: return "http://hl7.org/fhir/resource-types";
            case SUPPLYREQUEST: return "http://hl7.org/fhir/resource-types";
            case TASK: return "http://hl7.org/fhir/resource-types";
            case TERMINOLOGYCAPABILITIES: return "http://hl7.org/fhir/resource-types";
            case TESTREPORT: return "http://hl7.org/fhir/resource-types";
            case TESTSCRIPT: return "http://hl7.org/fhir/resource-types";
            case USERSESSION: return "http://hl7.org/fhir/resource-types";
            case VALUESET: return "http://hl7.org/fhir/resource-types";
            case VERIFICATIONRESULT: return "http://hl7.org/fhir/resource-types";
            case VISIONPRESCRIPTION: return "http://hl7.org/fhir/resource-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACCOUNT: return "A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centers, etc.";
            case ACTIVITYDEFINITION: return "This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.";
            case ADVERSEEVENT: return "Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.";
            case ALLERGYINTOLERANCE: return "Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.";
            case APPOINTMENT: return "A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).";
            case APPOINTMENTRESPONSE: return "A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.";
            case AUDITEVENT: return "A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.";
            case BASIC: return "Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.";
            case BINARY: return "A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.";
            case BIOLOGICALLYDERIVEDPRODUCT: return "A material substance originating from a biological entity intended to be transplanted or infused\ninto another (possibly the same) biological entity.";
            case BODYSTRUCTURE: return "Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.";
            case BUNDLE: return "A container for a collection of resources.";
            case CAPABILITYSTATEMENT: return "A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case CAREPLAN: return "Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.";
            case CARETEAM: return "The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.";
            case CATALOGENTRY: return "Catalog entries are wrappers that contextualize items included in a catalog.";
            case CHARGEITEM: return "The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.";
            case CHARGEITEMDEFINITION: return "The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.";
            case CLAIM: return "A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.";
            case CLAIMRESPONSE: return "This resource provides the adjudication details from the processing of a Claim resource.";
            case CLINICALIMPRESSION: return "A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called \"ClinicalImpression\" rather than \"ClinicalAssessment\" to avoid confusion with the recording of assessment tools such as Apgar score.";
            case CODESYSTEM: return "The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and its key properties, and optionally define a part or all of its content.";
            case COMMUNICATION: return "An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.";
            case COMMUNICATIONREQUEST: return "A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.";
            case COMPARTMENTDEFINITION: return "A compartment definition that defines how resources are accessed on a server.";
            case COMPOSITION: return "A set of healthcare-related information that is assembled together into a single logical package that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. A Composition defines the structure and narrative content necessary for a document. However, a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in the Bundle (for example Patient, Practitioner, Encounter, etc.).";
            case CONCEPTMAP: return "A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, or data element/data element concepts, or classes in class models.";
            case CONDITION: return "A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.";
            case CONSENT: return "A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.";
            case CONTRACT: return "Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.";
            case COVERAGE: return "Financial instrument which may be used to reimburse or pay for health care products and services.";
            case COVERAGEELIGIBILITYREQUEST: return "The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.";
            case COVERAGEELIGIBILITYRESPONSE: return "This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.";
            case DETECTEDISSUE: return "Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.";
            case DEVICE: return "This resource identifies a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc. This is the catalog description of a device (not the specific instance).";
            case DEVICEDEFINITION: return "The characteristics, operational status and capabilities of a medical-related component of a medical device.";
            case DEVICEMETRIC: return "Describes a measurement, calculation or setting capability of a medical device.";
            case DEVICEREQUEST: return "Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.";
            case DEVICEUSESTATEMENT: return "A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.";
            case DIAGNOSTICREPORT: return "The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.";
            case DOCUMENTMANIFEST: return "A collection of documents compiled for a purpose together with metadata that applies to the collection.";
            case DOCUMENTREFERENCE: return "A reference to a document.";
            case DOMAINRESOURCE: return "A resource that includes narrative, extensions, and contained resources.";
            case ENCOUNTER: return "An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.";
            case ENDPOINT: return "The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.";
            case ENROLLMENTREQUEST: return "This resource provides the insurance enrollment details to the insurer regarding a specified coverage.";
            case ENROLLMENTRESPONSE: return "This resource provides enrollment and plan details from the processing of an Enrollment resource.";
            case EPISODEOFCARE: return "An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.";
            case EVENTDEFINITION: return "The EventDefinition resource provides a reusable description of when a particular event can occur.";
            case EXAMPLESCENARIO: return "Example of workflow instance.";
            case EXPLANATIONOFBENEFIT: return "This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.";
            case FAMILYMEMBERHISTORY: return "Significant health conditions for a person related to the patient relevant in the context of care for the patient.";
            case FLAG: return "Prospective warnings of potential issues when providing care to the patient.";
            case GOAL: return "Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.";
            case GRAPHDEFINITION: return "A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.";
            case GROUP: return "Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an Organization.";
            case GUIDANCERESPONSE: return "A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.";
            case HEALTHCARESERVICE: return "The details of a healthcare service available at a location.";
            case IMAGINGSTUDY: return "Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.";
            case IMMUNIZATION: return "Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, a clinician or another party.";
            case IMMUNIZATIONEVALUATION: return "Describes a comparison of an immunization event against published recommendations to determine if the administration is \"valid\" in relation to those  recommendations.";
            case IMMUNIZATIONRECOMMENDATION: return "A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional supporting justification.";
            case IMPLEMENTATIONGUIDE: return "A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.";
            case INSURANCEPLAN: return "Details of a Health Insurance product/plan provided by an organization.";
            case INVOICE: return "Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.";
            case ITEMINSTANCE: return "A physical, countable instance of an item, for example one box or one unit.";
            case LIBRARY: return "The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a collection of knowledge assets.";
            case LINKAGE: return "Identifies two or more records (resource instances) that are referring to the same real-world \"occurrence\".";
            case LIST: return "A set of information summarized from a list of other resources.";
            case LOCATION: return "Details and position information for a physical place where services are provided and resources and participants may be stored, found, contained, or accommodated.";
            case MEASURE: return "The Measure resource provides the definition of a quality measure.";
            case MEASUREREPORT: return "The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.";
            case MEDIA: return "A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.";
            case MEDICATION: return "This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.";
            case MEDICATIONADMINISTRATION: return "Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.";
            case MEDICATIONDISPENSE: return "Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.";
            case MEDICATIONKNOWLEDGE: return "Information about a medication that is used to support knowledge.";
            case MEDICATIONREQUEST: return "An order or request for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called \"MedicationRequest\" rather than \"MedicationPrescription\" or \"MedicationOrder\" to generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with workflow patterns.";
            case MEDICATIONSTATEMENT: return "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. \r\rThe primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
            case MEDICINALPRODUCT: return "Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).";
            case MEDICINALPRODUCTAUTHORIZATION: return "The regulatory authorization of a medicinal product.";
            case MEDICINALPRODUCTCLINICALS: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTCONTRAINDICATION: return "The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.";
            case MEDICINALPRODUCTDEVICESPEC: return "A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to replace the Device resource, which covers use of device instances.";
            case MEDICINALPRODUCTINDICATION: return "Indication for the Medicinal Product.";
            case MEDICINALPRODUCTINGREDIENT: return "An ingredient of a manufactured item or pharmaceutical product.";
            case MEDICINALPRODUCTINTERACTION: return "The interactions of the medicinal product with other medicinal products, or other forms of interactions.";
            case MEDICINALPRODUCTMANUFACTURED: return "The manufactured item as contained in the packaged medicinal product.";
            case MEDICINALPRODUCTPACKAGED: return "A medicinal product in a container or package.";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "A pharmaceutical product described in terms of its composition and dose form.";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "Describe the undesirable effects of the medicinal product.";
            case MESSAGEDEFINITION: return "Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.";
            case MESSAGEHEADER: return "The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.";
            case NAMINGSYSTEM: return "A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.";
            case NUTRITIONORDER: return "A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.";
            case OBSERVATION: return "Measurements and simple assertions made about a patient, device or other subject.";
            case OBSERVATIONDEFINITION: return "Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.";
            case OPERATIONDEFINITION: return "A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).";
            case OPERATIONOUTCOME: return "A collection of error, warning or information messages that result from a system action.";
            case ORGANIZATION: return "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, payer/insurer, etc.";
            case ORGANIZATIONAFFILIATION: return "Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.";
            case PARAMETERS: return "This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.";
            case PATIENT: return "Demographics and other administrative information about an individual or animal receiving care or other health-related services.";
            case PAYMENTNOTICE: return "This resource provides the status of the payment for goods and services rendered, and the request and response resource references.";
            case PAYMENTRECONCILIATION: return "This resource provides payment details and claim references supporting a bulk payment.";
            case PERSON: return "Demographics and administrative information about a person independent of a specific health-related context.";
            case PLANDEFINITION: return "This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.";
            case PRACTITIONER: return "A person who is directly or indirectly involved in the provisioning of healthcare.";
            case PRACTITIONERROLE: return "A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.";
            case PROCEDURE: return "An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.";
            case PROCESSREQUEST: return "This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.";
            case PROCESSRESPONSE: return "This resource provides processing status, errors and notes from the processing of a resource.";
            case PROVENANCE: return "Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.";
            case QUESTIONNAIRE: return "A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.";
            case QUESTIONNAIRERESPONSE: return "A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.";
            case RELATEDPERSON: return "Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            case REQUESTGROUP: return "A group of related requests that can be used to capture intended activities that have inter-dependencies such as \"give this medication after that one\".";
            case RESEARCHSTUDY: return "A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.";
            case RESEARCHSUBJECT: return "A physical entity which is the primary unit of operational and/or administrative interest in a study.";
            case RESOURCE: return "This is the base resource type for everything.";
            case RISKASSESSMENT: return "An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.";
            case SCHEDULE: return "A container for slots of time that may be available for booking appointments.";
            case SEARCHPARAMETER: return "A search parameter that defines a named search item that can be used to search/filter on a resource.";
            case SEQUENCE: return "Raw data describing a biological sequence.";
            case SERVICEREQUEST: return "A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.";
            case SLOT: return "A slot of time on a schedule that may be available for booking appointments.";
            case SPECIMEN: return "A sample to be used for analysis.";
            case SPECIMENDEFINITION: return "A kind of specimen with associated set of requirements.";
            case STRUCTUREDEFINITION: return "A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions and constraints on resources and data types.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case SUBSCRIPTION: return "The subscription resource is used to define a push-based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined \"channel\" so that another system can take an appropriate action.";
            case SUBSTANCE: return "A homogeneous material with a definite composition.";
            case SUBSTANCEPOLYMER: return "Todo.";
            case SUBSTANCEREFERENCEINFORMATION: return "Todo.";
            case SUBSTANCESPECIFICATION: return "The detailed description of a substance, typically at a level beyond what is used for prescribing.";
            case SUPPLYDELIVERY: return "Record of delivery of what is supplied.";
            case SUPPLYREQUEST: return "A record of a request for a medication, substance or device used in the healthcare setting.";
            case TASK: return "A task to be performed.";
            case TERMINOLOGYCAPABILITIES: return "A Terminology Capabilities documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.";
            case TESTREPORT: return "A summary of information based on the results of executing a TestScript.";
            case TESTSCRIPT: return "A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR specification.";
            case USERSESSION: return "Information about a user's current session.";
            case VALUESET: return "A ValueSet resource instances specifies a set of codes drawn from one or more code systems, intended for use in a particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements](terminologies.html).";
            case VERIFICATIONRESULT: return "Describes validation requirements, source(s), status and dates for one or more elements.";
            case VISIONPRESCRIPTION: return "An authorization for the supply of glasses and/or contact lenses to a patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCOUNT: return "Account";
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case ADVERSEEVENT: return "AdverseEvent";
            case ALLERGYINTOLERANCE: return "AllergyIntolerance";
            case APPOINTMENT: return "Appointment";
            case APPOINTMENTRESPONSE: return "AppointmentResponse";
            case AUDITEVENT: return "AuditEvent";
            case BASIC: return "Basic";
            case BINARY: return "Binary";
            case BIOLOGICALLYDERIVEDPRODUCT: return "BiologicallyDerivedProduct";
            case BODYSTRUCTURE: return "BodyStructure";
            case BUNDLE: return "Bundle";
            case CAPABILITYSTATEMENT: return "CapabilityStatement";
            case CAREPLAN: return "CarePlan";
            case CARETEAM: return "CareTeam";
            case CATALOGENTRY: return "CatalogEntry";
            case CHARGEITEM: return "ChargeItem";
            case CHARGEITEMDEFINITION: return "ChargeItemDefinition";
            case CLAIM: return "Claim";
            case CLAIMRESPONSE: return "ClaimResponse";
            case CLINICALIMPRESSION: return "ClinicalImpression";
            case CODESYSTEM: return "CodeSystem";
            case COMMUNICATION: return "Communication";
            case COMMUNICATIONREQUEST: return "CommunicationRequest";
            case COMPARTMENTDEFINITION: return "CompartmentDefinition";
            case COMPOSITION: return "Composition";
            case CONCEPTMAP: return "ConceptMap";
            case CONDITION: return "Condition";
            case CONSENT: return "Consent";
            case CONTRACT: return "Contract";
            case COVERAGE: return "Coverage";
            case COVERAGEELIGIBILITYREQUEST: return "CoverageEligibilityRequest";
            case COVERAGEELIGIBILITYRESPONSE: return "CoverageEligibilityResponse";
            case DETECTEDISSUE: return "DetectedIssue";
            case DEVICE: return "Device";
            case DEVICEDEFINITION: return "DeviceDefinition";
            case DEVICEMETRIC: return "DeviceMetric";
            case DEVICEREQUEST: return "DeviceRequest";
            case DEVICEUSESTATEMENT: return "DeviceUseStatement";
            case DIAGNOSTICREPORT: return "DiagnosticReport";
            case DOCUMENTMANIFEST: return "DocumentManifest";
            case DOCUMENTREFERENCE: return "DocumentReference";
            case DOMAINRESOURCE: return "DomainResource";
            case ENCOUNTER: return "Encounter";
            case ENDPOINT: return "Endpoint";
            case ENROLLMENTREQUEST: return "EnrollmentRequest";
            case ENROLLMENTRESPONSE: return "EnrollmentResponse";
            case EPISODEOFCARE: return "EpisodeOfCare";
            case EVENTDEFINITION: return "EventDefinition";
            case EXAMPLESCENARIO: return "ExampleScenario";
            case EXPLANATIONOFBENEFIT: return "ExplanationOfBenefit";
            case FAMILYMEMBERHISTORY: return "FamilyMemberHistory";
            case FLAG: return "Flag";
            case GOAL: return "Goal";
            case GRAPHDEFINITION: return "GraphDefinition";
            case GROUP: return "Group";
            case GUIDANCERESPONSE: return "GuidanceResponse";
            case HEALTHCARESERVICE: return "HealthcareService";
            case IMAGINGSTUDY: return "ImagingStudy";
            case IMMUNIZATION: return "Immunization";
            case IMMUNIZATIONEVALUATION: return "ImmunizationEvaluation";
            case IMMUNIZATIONRECOMMENDATION: return "ImmunizationRecommendation";
            case IMPLEMENTATIONGUIDE: return "ImplementationGuide";
            case INSURANCEPLAN: return "InsurancePlan";
            case INVOICE: return "Invoice";
            case ITEMINSTANCE: return "ItemInstance";
            case LIBRARY: return "Library";
            case LINKAGE: return "Linkage";
            case LIST: return "List";
            case LOCATION: return "Location";
            case MEASURE: return "Measure";
            case MEASUREREPORT: return "MeasureReport";
            case MEDIA: return "Media";
            case MEDICATION: return "Medication";
            case MEDICATIONADMINISTRATION: return "MedicationAdministration";
            case MEDICATIONDISPENSE: return "MedicationDispense";
            case MEDICATIONKNOWLEDGE: return "MedicationKnowledge";
            case MEDICATIONREQUEST: return "MedicationRequest";
            case MEDICATIONSTATEMENT: return "MedicationStatement";
            case MEDICINALPRODUCT: return "MedicinalProduct";
            case MEDICINALPRODUCTAUTHORIZATION: return "MedicinalProductAuthorization";
            case MEDICINALPRODUCTCLINICALS: return "MedicinalProductClinicals";
            case MEDICINALPRODUCTCONTRAINDICATION: return "MedicinalProductContraindication";
            case MEDICINALPRODUCTDEVICESPEC: return "MedicinalProductDeviceSpec";
            case MEDICINALPRODUCTINDICATION: return "MedicinalProductIndication";
            case MEDICINALPRODUCTINGREDIENT: return "MedicinalProductIngredient";
            case MEDICINALPRODUCTINTERACTION: return "MedicinalProductInteraction";
            case MEDICINALPRODUCTMANUFACTURED: return "MedicinalProductManufactured";
            case MEDICINALPRODUCTPACKAGED: return "MedicinalProductPackaged";
            case MEDICINALPRODUCTPHARMACEUTICAL: return "MedicinalProductPharmaceutical";
            case MEDICINALPRODUCTUNDESIRABLEEFFECT: return "MedicinalProductUndesirableEffect";
            case MESSAGEDEFINITION: return "MessageDefinition";
            case MESSAGEHEADER: return "MessageHeader";
            case NAMINGSYSTEM: return "NamingSystem";
            case NUTRITIONORDER: return "NutritionOrder";
            case OBSERVATION: return "Observation";
            case OBSERVATIONDEFINITION: return "ObservationDefinition";
            case OPERATIONDEFINITION: return "OperationDefinition";
            case OPERATIONOUTCOME: return "OperationOutcome";
            case ORGANIZATION: return "Organization";
            case ORGANIZATIONAFFILIATION: return "OrganizationAffiliation";
            case PARAMETERS: return "Parameters";
            case PATIENT: return "Patient";
            case PAYMENTNOTICE: return "PaymentNotice";
            case PAYMENTRECONCILIATION: return "PaymentReconciliation";
            case PERSON: return "Person";
            case PLANDEFINITION: return "PlanDefinition";
            case PRACTITIONER: return "Practitioner";
            case PRACTITIONERROLE: return "PractitionerRole";
            case PROCEDURE: return "Procedure";
            case PROCESSREQUEST: return "ProcessRequest";
            case PROCESSRESPONSE: return "ProcessResponse";
            case PROVENANCE: return "Provenance";
            case QUESTIONNAIRE: return "Questionnaire";
            case QUESTIONNAIRERESPONSE: return "QuestionnaireResponse";
            case RELATEDPERSON: return "RelatedPerson";
            case REQUESTGROUP: return "RequestGroup";
            case RESEARCHSTUDY: return "ResearchStudy";
            case RESEARCHSUBJECT: return "ResearchSubject";
            case RESOURCE: return "Resource";
            case RISKASSESSMENT: return "RiskAssessment";
            case SCHEDULE: return "Schedule";
            case SEARCHPARAMETER: return "SearchParameter";
            case SEQUENCE: return "Sequence";
            case SERVICEREQUEST: return "ServiceRequest";
            case SLOT: return "Slot";
            case SPECIMEN: return "Specimen";
            case SPECIMENDEFINITION: return "SpecimenDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case SUBSCRIPTION: return "Subscription";
            case SUBSTANCE: return "Substance";
            case SUBSTANCEPOLYMER: return "SubstancePolymer";
            case SUBSTANCEREFERENCEINFORMATION: return "SubstanceReferenceInformation";
            case SUBSTANCESPECIFICATION: return "SubstanceSpecification";
            case SUPPLYDELIVERY: return "SupplyDelivery";
            case SUPPLYREQUEST: return "SupplyRequest";
            case TASK: return "Task";
            case TERMINOLOGYCAPABILITIES: return "TerminologyCapabilities";
            case TESTREPORT: return "TestReport";
            case TESTSCRIPT: return "TestScript";
            case USERSESSION: return "UserSession";
            case VALUESET: return "ValueSet";
            case VERIFICATIONRESULT: return "VerificationResult";
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
        if ("ActivityDefinition".equals(codeString))
          return ResourceType.ACTIVITYDEFINITION;
        if ("AdverseEvent".equals(codeString))
          return ResourceType.ADVERSEEVENT;
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
        if ("BiologicallyDerivedProduct".equals(codeString))
          return ResourceType.BIOLOGICALLYDERIVEDPRODUCT;
        if ("BodyStructure".equals(codeString))
          return ResourceType.BODYSTRUCTURE;
        if ("Bundle".equals(codeString))
          return ResourceType.BUNDLE;
        if ("CapabilityStatement".equals(codeString))
          return ResourceType.CAPABILITYSTATEMENT;
        if ("CarePlan".equals(codeString))
          return ResourceType.CAREPLAN;
        if ("CareTeam".equals(codeString))
          return ResourceType.CARETEAM;
        if ("CatalogEntry".equals(codeString))
          return ResourceType.CATALOGENTRY;
        if ("ChargeItem".equals(codeString))
          return ResourceType.CHARGEITEM;
        if ("ChargeItemDefinition".equals(codeString))
          return ResourceType.CHARGEITEMDEFINITION;
        if ("Claim".equals(codeString))
          return ResourceType.CLAIM;
        if ("ClaimResponse".equals(codeString))
          return ResourceType.CLAIMRESPONSE;
        if ("ClinicalImpression".equals(codeString))
          return ResourceType.CLINICALIMPRESSION;
        if ("CodeSystem".equals(codeString))
          return ResourceType.CODESYSTEM;
        if ("Communication".equals(codeString))
          return ResourceType.COMMUNICATION;
        if ("CommunicationRequest".equals(codeString))
          return ResourceType.COMMUNICATIONREQUEST;
        if ("CompartmentDefinition".equals(codeString))
          return ResourceType.COMPARTMENTDEFINITION;
        if ("Composition".equals(codeString))
          return ResourceType.COMPOSITION;
        if ("ConceptMap".equals(codeString))
          return ResourceType.CONCEPTMAP;
        if ("Condition".equals(codeString))
          return ResourceType.CONDITION;
        if ("Consent".equals(codeString))
          return ResourceType.CONSENT;
        if ("Contract".equals(codeString))
          return ResourceType.CONTRACT;
        if ("Coverage".equals(codeString))
          return ResourceType.COVERAGE;
        if ("CoverageEligibilityRequest".equals(codeString))
          return ResourceType.COVERAGEELIGIBILITYREQUEST;
        if ("CoverageEligibilityResponse".equals(codeString))
          return ResourceType.COVERAGEELIGIBILITYRESPONSE;
        if ("DetectedIssue".equals(codeString))
          return ResourceType.DETECTEDISSUE;
        if ("Device".equals(codeString))
          return ResourceType.DEVICE;
        if ("DeviceDefinition".equals(codeString))
          return ResourceType.DEVICEDEFINITION;
        if ("DeviceMetric".equals(codeString))
          return ResourceType.DEVICEMETRIC;
        if ("DeviceRequest".equals(codeString))
          return ResourceType.DEVICEREQUEST;
        if ("DeviceUseStatement".equals(codeString))
          return ResourceType.DEVICEUSESTATEMENT;
        if ("DiagnosticReport".equals(codeString))
          return ResourceType.DIAGNOSTICREPORT;
        if ("DocumentManifest".equals(codeString))
          return ResourceType.DOCUMENTMANIFEST;
        if ("DocumentReference".equals(codeString))
          return ResourceType.DOCUMENTREFERENCE;
        if ("DomainResource".equals(codeString))
          return ResourceType.DOMAINRESOURCE;
        if ("Encounter".equals(codeString))
          return ResourceType.ENCOUNTER;
        if ("Endpoint".equals(codeString))
          return ResourceType.ENDPOINT;
        if ("EnrollmentRequest".equals(codeString))
          return ResourceType.ENROLLMENTREQUEST;
        if ("EnrollmentResponse".equals(codeString))
          return ResourceType.ENROLLMENTRESPONSE;
        if ("EpisodeOfCare".equals(codeString))
          return ResourceType.EPISODEOFCARE;
        if ("EventDefinition".equals(codeString))
          return ResourceType.EVENTDEFINITION;
        if ("ExampleScenario".equals(codeString))
          return ResourceType.EXAMPLESCENARIO;
        if ("ExplanationOfBenefit".equals(codeString))
          return ResourceType.EXPLANATIONOFBENEFIT;
        if ("FamilyMemberHistory".equals(codeString))
          return ResourceType.FAMILYMEMBERHISTORY;
        if ("Flag".equals(codeString))
          return ResourceType.FLAG;
        if ("Goal".equals(codeString))
          return ResourceType.GOAL;
        if ("GraphDefinition".equals(codeString))
          return ResourceType.GRAPHDEFINITION;
        if ("Group".equals(codeString))
          return ResourceType.GROUP;
        if ("GuidanceResponse".equals(codeString))
          return ResourceType.GUIDANCERESPONSE;
        if ("HealthcareService".equals(codeString))
          return ResourceType.HEALTHCARESERVICE;
        if ("ImagingStudy".equals(codeString))
          return ResourceType.IMAGINGSTUDY;
        if ("Immunization".equals(codeString))
          return ResourceType.IMMUNIZATION;
        if ("ImmunizationEvaluation".equals(codeString))
          return ResourceType.IMMUNIZATIONEVALUATION;
        if ("ImmunizationRecommendation".equals(codeString))
          return ResourceType.IMMUNIZATIONRECOMMENDATION;
        if ("ImplementationGuide".equals(codeString))
          return ResourceType.IMPLEMENTATIONGUIDE;
        if ("InsurancePlan".equals(codeString))
          return ResourceType.INSURANCEPLAN;
        if ("Invoice".equals(codeString))
          return ResourceType.INVOICE;
        if ("ItemInstance".equals(codeString))
          return ResourceType.ITEMINSTANCE;
        if ("Library".equals(codeString))
          return ResourceType.LIBRARY;
        if ("Linkage".equals(codeString))
          return ResourceType.LINKAGE;
        if ("List".equals(codeString))
          return ResourceType.LIST;
        if ("Location".equals(codeString))
          return ResourceType.LOCATION;
        if ("Measure".equals(codeString))
          return ResourceType.MEASURE;
        if ("MeasureReport".equals(codeString))
          return ResourceType.MEASUREREPORT;
        if ("Media".equals(codeString))
          return ResourceType.MEDIA;
        if ("Medication".equals(codeString))
          return ResourceType.MEDICATION;
        if ("MedicationAdministration".equals(codeString))
          return ResourceType.MEDICATIONADMINISTRATION;
        if ("MedicationDispense".equals(codeString))
          return ResourceType.MEDICATIONDISPENSE;
        if ("MedicationKnowledge".equals(codeString))
          return ResourceType.MEDICATIONKNOWLEDGE;
        if ("MedicationRequest".equals(codeString))
          return ResourceType.MEDICATIONREQUEST;
        if ("MedicationStatement".equals(codeString))
          return ResourceType.MEDICATIONSTATEMENT;
        if ("MedicinalProduct".equals(codeString))
          return ResourceType.MEDICINALPRODUCT;
        if ("MedicinalProductAuthorization".equals(codeString))
          return ResourceType.MEDICINALPRODUCTAUTHORIZATION;
        if ("MedicinalProductClinicals".equals(codeString))
          return ResourceType.MEDICINALPRODUCTCLINICALS;
        if ("MedicinalProductContraindication".equals(codeString))
          return ResourceType.MEDICINALPRODUCTCONTRAINDICATION;
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return ResourceType.MEDICINALPRODUCTDEVICESPEC;
        if ("MedicinalProductIndication".equals(codeString))
          return ResourceType.MEDICINALPRODUCTINDICATION;
        if ("MedicinalProductIngredient".equals(codeString))
          return ResourceType.MEDICINALPRODUCTINGREDIENT;
        if ("MedicinalProductInteraction".equals(codeString))
          return ResourceType.MEDICINALPRODUCTINTERACTION;
        if ("MedicinalProductManufactured".equals(codeString))
          return ResourceType.MEDICINALPRODUCTMANUFACTURED;
        if ("MedicinalProductPackaged".equals(codeString))
          return ResourceType.MEDICINALPRODUCTPACKAGED;
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return ResourceType.MEDICINALPRODUCTPHARMACEUTICAL;
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return ResourceType.MEDICINALPRODUCTUNDESIRABLEEFFECT;
        if ("MessageDefinition".equals(codeString))
          return ResourceType.MESSAGEDEFINITION;
        if ("MessageHeader".equals(codeString))
          return ResourceType.MESSAGEHEADER;
        if ("NamingSystem".equals(codeString))
          return ResourceType.NAMINGSYSTEM;
        if ("NutritionOrder".equals(codeString))
          return ResourceType.NUTRITIONORDER;
        if ("Observation".equals(codeString))
          return ResourceType.OBSERVATION;
        if ("ObservationDefinition".equals(codeString))
          return ResourceType.OBSERVATIONDEFINITION;
        if ("OperationDefinition".equals(codeString))
          return ResourceType.OPERATIONDEFINITION;
        if ("OperationOutcome".equals(codeString))
          return ResourceType.OPERATIONOUTCOME;
        if ("Organization".equals(codeString))
          return ResourceType.ORGANIZATION;
        if ("OrganizationAffiliation".equals(codeString))
          return ResourceType.ORGANIZATIONAFFILIATION;
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
        if ("PlanDefinition".equals(codeString))
          return ResourceType.PLANDEFINITION;
        if ("Practitioner".equals(codeString))
          return ResourceType.PRACTITIONER;
        if ("PractitionerRole".equals(codeString))
          return ResourceType.PRACTITIONERROLE;
        if ("Procedure".equals(codeString))
          return ResourceType.PROCEDURE;
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
        if ("RelatedPerson".equals(codeString))
          return ResourceType.RELATEDPERSON;
        if ("RequestGroup".equals(codeString))
          return ResourceType.REQUESTGROUP;
        if ("ResearchStudy".equals(codeString))
          return ResourceType.RESEARCHSTUDY;
        if ("ResearchSubject".equals(codeString))
          return ResourceType.RESEARCHSUBJECT;
        if ("Resource".equals(codeString))
          return ResourceType.RESOURCE;
        if ("RiskAssessment".equals(codeString))
          return ResourceType.RISKASSESSMENT;
        if ("Schedule".equals(codeString))
          return ResourceType.SCHEDULE;
        if ("SearchParameter".equals(codeString))
          return ResourceType.SEARCHPARAMETER;
        if ("Sequence".equals(codeString))
          return ResourceType.SEQUENCE;
        if ("ServiceRequest".equals(codeString))
          return ResourceType.SERVICEREQUEST;
        if ("Slot".equals(codeString))
          return ResourceType.SLOT;
        if ("Specimen".equals(codeString))
          return ResourceType.SPECIMEN;
        if ("SpecimenDefinition".equals(codeString))
          return ResourceType.SPECIMENDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return ResourceType.STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return ResourceType.STRUCTUREMAP;
        if ("Subscription".equals(codeString))
          return ResourceType.SUBSCRIPTION;
        if ("Substance".equals(codeString))
          return ResourceType.SUBSTANCE;
        if ("SubstancePolymer".equals(codeString))
          return ResourceType.SUBSTANCEPOLYMER;
        if ("SubstanceReferenceInformation".equals(codeString))
          return ResourceType.SUBSTANCEREFERENCEINFORMATION;
        if ("SubstanceSpecification".equals(codeString))
          return ResourceType.SUBSTANCESPECIFICATION;
        if ("SupplyDelivery".equals(codeString))
          return ResourceType.SUPPLYDELIVERY;
        if ("SupplyRequest".equals(codeString))
          return ResourceType.SUPPLYREQUEST;
        if ("Task".equals(codeString))
          return ResourceType.TASK;
        if ("TerminologyCapabilities".equals(codeString))
          return ResourceType.TERMINOLOGYCAPABILITIES;
        if ("TestReport".equals(codeString))
          return ResourceType.TESTREPORT;
        if ("TestScript".equals(codeString))
          return ResourceType.TESTSCRIPT;
        if ("UserSession".equals(codeString))
          return ResourceType.USERSESSION;
        if ("ValueSet".equals(codeString))
          return ResourceType.VALUESET;
        if ("VerificationResult".equals(codeString))
          return ResourceType.VERIFICATIONRESULT;
        if ("VisionPrescription".equals(codeString))
          return ResourceType.VISIONPRESCRIPTION;
        throw new IllegalArgumentException("Unknown ResourceType code '"+codeString+"'");
        }
        public Enumeration<ResourceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ResourceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Account".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ACCOUNT);
        if ("ActivityDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ACTIVITYDEFINITION);
        if ("AdverseEvent".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ADVERSEEVENT);
        if ("AllergyIntolerance".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ALLERGYINTOLERANCE);
        if ("Appointment".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.APPOINTMENT);
        if ("AppointmentResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.APPOINTMENTRESPONSE);
        if ("AuditEvent".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.AUDITEVENT);
        if ("Basic".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.BASIC);
        if ("Binary".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.BINARY);
        if ("BiologicallyDerivedProduct".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.BIOLOGICALLYDERIVEDPRODUCT);
        if ("BodyStructure".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.BODYSTRUCTURE);
        if ("Bundle".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.BUNDLE);
        if ("CapabilityStatement".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CAPABILITYSTATEMENT);
        if ("CarePlan".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CAREPLAN);
        if ("CareTeam".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CARETEAM);
        if ("CatalogEntry".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CATALOGENTRY);
        if ("ChargeItem".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CHARGEITEM);
        if ("ChargeItemDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CHARGEITEMDEFINITION);
        if ("Claim".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CLAIM);
        if ("ClaimResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CLAIMRESPONSE);
        if ("ClinicalImpression".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CLINICALIMPRESSION);
        if ("CodeSystem".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CODESYSTEM);
        if ("Communication".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COMMUNICATION);
        if ("CommunicationRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COMMUNICATIONREQUEST);
        if ("CompartmentDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COMPARTMENTDEFINITION);
        if ("Composition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COMPOSITION);
        if ("ConceptMap".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CONCEPTMAP);
        if ("Condition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CONDITION);
        if ("Consent".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CONSENT);
        if ("Contract".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.CONTRACT);
        if ("Coverage".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COVERAGE);
        if ("CoverageEligibilityRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COVERAGEELIGIBILITYREQUEST);
        if ("CoverageEligibilityResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.COVERAGEELIGIBILITYRESPONSE);
        if ("DetectedIssue".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DETECTEDISSUE);
        if ("Device".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DEVICE);
        if ("DeviceDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DEVICEDEFINITION);
        if ("DeviceMetric".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DEVICEMETRIC);
        if ("DeviceRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DEVICEREQUEST);
        if ("DeviceUseStatement".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DEVICEUSESTATEMENT);
        if ("DiagnosticReport".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DIAGNOSTICREPORT);
        if ("DocumentManifest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DOCUMENTMANIFEST);
        if ("DocumentReference".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DOCUMENTREFERENCE);
        if ("DomainResource".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.DOMAINRESOURCE);
        if ("Encounter".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ENCOUNTER);
        if ("Endpoint".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ENDPOINT);
        if ("EnrollmentRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ENROLLMENTREQUEST);
        if ("EnrollmentResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ENROLLMENTRESPONSE);
        if ("EpisodeOfCare".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.EPISODEOFCARE);
        if ("EventDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.EVENTDEFINITION);
        if ("ExampleScenario".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.EXAMPLESCENARIO);
        if ("ExplanationOfBenefit".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.EXPLANATIONOFBENEFIT);
        if ("FamilyMemberHistory".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.FAMILYMEMBERHISTORY);
        if ("Flag".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.FLAG);
        if ("Goal".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.GOAL);
        if ("GraphDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.GRAPHDEFINITION);
        if ("Group".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.GROUP);
        if ("GuidanceResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.GUIDANCERESPONSE);
        if ("HealthcareService".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.HEALTHCARESERVICE);
        if ("ImagingStudy".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.IMAGINGSTUDY);
        if ("Immunization".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.IMMUNIZATION);
        if ("ImmunizationEvaluation".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.IMMUNIZATIONEVALUATION);
        if ("ImmunizationRecommendation".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.IMMUNIZATIONRECOMMENDATION);
        if ("ImplementationGuide".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.IMPLEMENTATIONGUIDE);
        if ("InsurancePlan".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.INSURANCEPLAN);
        if ("Invoice".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.INVOICE);
        if ("ItemInstance".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ITEMINSTANCE);
        if ("Library".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.LIBRARY);
        if ("Linkage".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.LINKAGE);
        if ("List".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.LIST);
        if ("Location".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.LOCATION);
        if ("Measure".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEASURE);
        if ("MeasureReport".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEASUREREPORT);
        if ("Media".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDIA);
        if ("Medication".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICATION);
        if ("MedicationAdministration".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICATIONADMINISTRATION);
        if ("MedicationDispense".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICATIONDISPENSE);
        if ("MedicationKnowledge".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICATIONKNOWLEDGE);
        if ("MedicationRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICATIONREQUEST);
        if ("MedicationStatement".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICATIONSTATEMENT);
        if ("MedicinalProduct".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCT);
        if ("MedicinalProductAuthorization".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTAUTHORIZATION);
        if ("MedicinalProductClinicals".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTCLINICALS);
        if ("MedicinalProductContraindication".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTCONTRAINDICATION);
        if ("MedicinalProductDeviceSpec".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTDEVICESPEC);
        if ("MedicinalProductIndication".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTINDICATION);
        if ("MedicinalProductIngredient".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTINGREDIENT);
        if ("MedicinalProductInteraction".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTINTERACTION);
        if ("MedicinalProductManufactured".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTMANUFACTURED);
        if ("MedicinalProductPackaged".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTPACKAGED);
        if ("MedicinalProductPharmaceutical".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTPHARMACEUTICAL);
        if ("MedicinalProductUndesirableEffect".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MEDICINALPRODUCTUNDESIRABLEEFFECT);
        if ("MessageDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MESSAGEDEFINITION);
        if ("MessageHeader".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.MESSAGEHEADER);
        if ("NamingSystem".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.NAMINGSYSTEM);
        if ("NutritionOrder".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.NUTRITIONORDER);
        if ("Observation".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.OBSERVATION);
        if ("ObservationDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.OBSERVATIONDEFINITION);
        if ("OperationDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.OPERATIONDEFINITION);
        if ("OperationOutcome".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.OPERATIONOUTCOME);
        if ("Organization".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ORGANIZATION);
        if ("OrganizationAffiliation".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.ORGANIZATIONAFFILIATION);
        if ("Parameters".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PARAMETERS);
        if ("Patient".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PATIENT);
        if ("PaymentNotice".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PAYMENTNOTICE);
        if ("PaymentReconciliation".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PAYMENTRECONCILIATION);
        if ("Person".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PERSON);
        if ("PlanDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PLANDEFINITION);
        if ("Practitioner".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PRACTITIONER);
        if ("PractitionerRole".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PRACTITIONERROLE);
        if ("Procedure".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PROCEDURE);
        if ("ProcessRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PROCESSREQUEST);
        if ("ProcessResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PROCESSRESPONSE);
        if ("Provenance".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.PROVENANCE);
        if ("Questionnaire".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.QUESTIONNAIRE);
        if ("QuestionnaireResponse".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.QUESTIONNAIRERESPONSE);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.RELATEDPERSON);
        if ("RequestGroup".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.REQUESTGROUP);
        if ("ResearchStudy".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.RESEARCHSTUDY);
        if ("ResearchSubject".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.RESEARCHSUBJECT);
        if ("Resource".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.RESOURCE);
        if ("RiskAssessment".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.RISKASSESSMENT);
        if ("Schedule".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SCHEDULE);
        if ("SearchParameter".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SEARCHPARAMETER);
        if ("Sequence".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SEQUENCE);
        if ("ServiceRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SERVICEREQUEST);
        if ("Slot".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SLOT);
        if ("Specimen".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SPECIMEN);
        if ("SpecimenDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SPECIMENDEFINITION);
        if ("StructureDefinition".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.STRUCTUREDEFINITION);
        if ("StructureMap".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.STRUCTUREMAP);
        if ("Subscription".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUBSCRIPTION);
        if ("Substance".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUBSTANCE);
        if ("SubstancePolymer".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUBSTANCEPOLYMER);
        if ("SubstanceReferenceInformation".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUBSTANCEREFERENCEINFORMATION);
        if ("SubstanceSpecification".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUBSTANCESPECIFICATION);
        if ("SupplyDelivery".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUPPLYDELIVERY);
        if ("SupplyRequest".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.SUPPLYREQUEST);
        if ("Task".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.TASK);
        if ("TerminologyCapabilities".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.TERMINOLOGYCAPABILITIES);
        if ("TestReport".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.TESTREPORT);
        if ("TestScript".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.TESTSCRIPT);
        if ("UserSession".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.USERSESSION);
        if ("ValueSet".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.VALUESET);
        if ("VerificationResult".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.VERIFICATIONRESULT);
        if ("VisionPrescription".equals(codeString))
          return new Enumeration<ResourceType>(this, ResourceType.VISIONPRESCRIPTION);
        throw new FHIRException("Unknown ResourceType code '"+codeString+"'");
        }
    public String toCode(ResourceType code) {
      if (code == ResourceType.ACCOUNT)
        return "Account";
      if (code == ResourceType.ACTIVITYDEFINITION)
        return "ActivityDefinition";
      if (code == ResourceType.ADVERSEEVENT)
        return "AdverseEvent";
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
      if (code == ResourceType.BIOLOGICALLYDERIVEDPRODUCT)
        return "BiologicallyDerivedProduct";
      if (code == ResourceType.BODYSTRUCTURE)
        return "BodyStructure";
      if (code == ResourceType.BUNDLE)
        return "Bundle";
      if (code == ResourceType.CAPABILITYSTATEMENT)
        return "CapabilityStatement";
      if (code == ResourceType.CAREPLAN)
        return "CarePlan";
      if (code == ResourceType.CARETEAM)
        return "CareTeam";
      if (code == ResourceType.CATALOGENTRY)
        return "CatalogEntry";
      if (code == ResourceType.CHARGEITEM)
        return "ChargeItem";
      if (code == ResourceType.CHARGEITEMDEFINITION)
        return "ChargeItemDefinition";
      if (code == ResourceType.CLAIM)
        return "Claim";
      if (code == ResourceType.CLAIMRESPONSE)
        return "ClaimResponse";
      if (code == ResourceType.CLINICALIMPRESSION)
        return "ClinicalImpression";
      if (code == ResourceType.CODESYSTEM)
        return "CodeSystem";
      if (code == ResourceType.COMMUNICATION)
        return "Communication";
      if (code == ResourceType.COMMUNICATIONREQUEST)
        return "CommunicationRequest";
      if (code == ResourceType.COMPARTMENTDEFINITION)
        return "CompartmentDefinition";
      if (code == ResourceType.COMPOSITION)
        return "Composition";
      if (code == ResourceType.CONCEPTMAP)
        return "ConceptMap";
      if (code == ResourceType.CONDITION)
        return "Condition";
      if (code == ResourceType.CONSENT)
        return "Consent";
      if (code == ResourceType.CONTRACT)
        return "Contract";
      if (code == ResourceType.COVERAGE)
        return "Coverage";
      if (code == ResourceType.COVERAGEELIGIBILITYREQUEST)
        return "CoverageEligibilityRequest";
      if (code == ResourceType.COVERAGEELIGIBILITYRESPONSE)
        return "CoverageEligibilityResponse";
      if (code == ResourceType.DETECTEDISSUE)
        return "DetectedIssue";
      if (code == ResourceType.DEVICE)
        return "Device";
      if (code == ResourceType.DEVICEDEFINITION)
        return "DeviceDefinition";
      if (code == ResourceType.DEVICEMETRIC)
        return "DeviceMetric";
      if (code == ResourceType.DEVICEREQUEST)
        return "DeviceRequest";
      if (code == ResourceType.DEVICEUSESTATEMENT)
        return "DeviceUseStatement";
      if (code == ResourceType.DIAGNOSTICREPORT)
        return "DiagnosticReport";
      if (code == ResourceType.DOCUMENTMANIFEST)
        return "DocumentManifest";
      if (code == ResourceType.DOCUMENTREFERENCE)
        return "DocumentReference";
      if (code == ResourceType.DOMAINRESOURCE)
        return "DomainResource";
      if (code == ResourceType.ENCOUNTER)
        return "Encounter";
      if (code == ResourceType.ENDPOINT)
        return "Endpoint";
      if (code == ResourceType.ENROLLMENTREQUEST)
        return "EnrollmentRequest";
      if (code == ResourceType.ENROLLMENTRESPONSE)
        return "EnrollmentResponse";
      if (code == ResourceType.EPISODEOFCARE)
        return "EpisodeOfCare";
      if (code == ResourceType.EVENTDEFINITION)
        return "EventDefinition";
      if (code == ResourceType.EXAMPLESCENARIO)
        return "ExampleScenario";
      if (code == ResourceType.EXPLANATIONOFBENEFIT)
        return "ExplanationOfBenefit";
      if (code == ResourceType.FAMILYMEMBERHISTORY)
        return "FamilyMemberHistory";
      if (code == ResourceType.FLAG)
        return "Flag";
      if (code == ResourceType.GOAL)
        return "Goal";
      if (code == ResourceType.GRAPHDEFINITION)
        return "GraphDefinition";
      if (code == ResourceType.GROUP)
        return "Group";
      if (code == ResourceType.GUIDANCERESPONSE)
        return "GuidanceResponse";
      if (code == ResourceType.HEALTHCARESERVICE)
        return "HealthcareService";
      if (code == ResourceType.IMAGINGSTUDY)
        return "ImagingStudy";
      if (code == ResourceType.IMMUNIZATION)
        return "Immunization";
      if (code == ResourceType.IMMUNIZATIONEVALUATION)
        return "ImmunizationEvaluation";
      if (code == ResourceType.IMMUNIZATIONRECOMMENDATION)
        return "ImmunizationRecommendation";
      if (code == ResourceType.IMPLEMENTATIONGUIDE)
        return "ImplementationGuide";
      if (code == ResourceType.INSURANCEPLAN)
        return "InsurancePlan";
      if (code == ResourceType.INVOICE)
        return "Invoice";
      if (code == ResourceType.ITEMINSTANCE)
        return "ItemInstance";
      if (code == ResourceType.LIBRARY)
        return "Library";
      if (code == ResourceType.LINKAGE)
        return "Linkage";
      if (code == ResourceType.LIST)
        return "List";
      if (code == ResourceType.LOCATION)
        return "Location";
      if (code == ResourceType.MEASURE)
        return "Measure";
      if (code == ResourceType.MEASUREREPORT)
        return "MeasureReport";
      if (code == ResourceType.MEDIA)
        return "Media";
      if (code == ResourceType.MEDICATION)
        return "Medication";
      if (code == ResourceType.MEDICATIONADMINISTRATION)
        return "MedicationAdministration";
      if (code == ResourceType.MEDICATIONDISPENSE)
        return "MedicationDispense";
      if (code == ResourceType.MEDICATIONKNOWLEDGE)
        return "MedicationKnowledge";
      if (code == ResourceType.MEDICATIONREQUEST)
        return "MedicationRequest";
      if (code == ResourceType.MEDICATIONSTATEMENT)
        return "MedicationStatement";
      if (code == ResourceType.MEDICINALPRODUCT)
        return "MedicinalProduct";
      if (code == ResourceType.MEDICINALPRODUCTAUTHORIZATION)
        return "MedicinalProductAuthorization";
      if (code == ResourceType.MEDICINALPRODUCTCLINICALS)
        return "MedicinalProductClinicals";
      if (code == ResourceType.MEDICINALPRODUCTCONTRAINDICATION)
        return "MedicinalProductContraindication";
      if (code == ResourceType.MEDICINALPRODUCTDEVICESPEC)
        return "MedicinalProductDeviceSpec";
      if (code == ResourceType.MEDICINALPRODUCTINDICATION)
        return "MedicinalProductIndication";
      if (code == ResourceType.MEDICINALPRODUCTINGREDIENT)
        return "MedicinalProductIngredient";
      if (code == ResourceType.MEDICINALPRODUCTINTERACTION)
        return "MedicinalProductInteraction";
      if (code == ResourceType.MEDICINALPRODUCTMANUFACTURED)
        return "MedicinalProductManufactured";
      if (code == ResourceType.MEDICINALPRODUCTPACKAGED)
        return "MedicinalProductPackaged";
      if (code == ResourceType.MEDICINALPRODUCTPHARMACEUTICAL)
        return "MedicinalProductPharmaceutical";
      if (code == ResourceType.MEDICINALPRODUCTUNDESIRABLEEFFECT)
        return "MedicinalProductUndesirableEffect";
      if (code == ResourceType.MESSAGEDEFINITION)
        return "MessageDefinition";
      if (code == ResourceType.MESSAGEHEADER)
        return "MessageHeader";
      if (code == ResourceType.NAMINGSYSTEM)
        return "NamingSystem";
      if (code == ResourceType.NUTRITIONORDER)
        return "NutritionOrder";
      if (code == ResourceType.OBSERVATION)
        return "Observation";
      if (code == ResourceType.OBSERVATIONDEFINITION)
        return "ObservationDefinition";
      if (code == ResourceType.OPERATIONDEFINITION)
        return "OperationDefinition";
      if (code == ResourceType.OPERATIONOUTCOME)
        return "OperationOutcome";
      if (code == ResourceType.ORGANIZATION)
        return "Organization";
      if (code == ResourceType.ORGANIZATIONAFFILIATION)
        return "OrganizationAffiliation";
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
      if (code == ResourceType.PLANDEFINITION)
        return "PlanDefinition";
      if (code == ResourceType.PRACTITIONER)
        return "Practitioner";
      if (code == ResourceType.PRACTITIONERROLE)
        return "PractitionerRole";
      if (code == ResourceType.PROCEDURE)
        return "Procedure";
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
      if (code == ResourceType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == ResourceType.REQUESTGROUP)
        return "RequestGroup";
      if (code == ResourceType.RESEARCHSTUDY)
        return "ResearchStudy";
      if (code == ResourceType.RESEARCHSUBJECT)
        return "ResearchSubject";
      if (code == ResourceType.RESOURCE)
        return "Resource";
      if (code == ResourceType.RISKASSESSMENT)
        return "RiskAssessment";
      if (code == ResourceType.SCHEDULE)
        return "Schedule";
      if (code == ResourceType.SEARCHPARAMETER)
        return "SearchParameter";
      if (code == ResourceType.SEQUENCE)
        return "Sequence";
      if (code == ResourceType.SERVICEREQUEST)
        return "ServiceRequest";
      if (code == ResourceType.SLOT)
        return "Slot";
      if (code == ResourceType.SPECIMEN)
        return "Specimen";
      if (code == ResourceType.SPECIMENDEFINITION)
        return "SpecimenDefinition";
      if (code == ResourceType.STRUCTUREDEFINITION)
        return "StructureDefinition";
      if (code == ResourceType.STRUCTUREMAP)
        return "StructureMap";
      if (code == ResourceType.SUBSCRIPTION)
        return "Subscription";
      if (code == ResourceType.SUBSTANCE)
        return "Substance";
      if (code == ResourceType.SUBSTANCEPOLYMER)
        return "SubstancePolymer";
      if (code == ResourceType.SUBSTANCEREFERENCEINFORMATION)
        return "SubstanceReferenceInformation";
      if (code == ResourceType.SUBSTANCESPECIFICATION)
        return "SubstanceSpecification";
      if (code == ResourceType.SUPPLYDELIVERY)
        return "SupplyDelivery";
      if (code == ResourceType.SUPPLYREQUEST)
        return "SupplyRequest";
      if (code == ResourceType.TASK)
        return "Task";
      if (code == ResourceType.TERMINOLOGYCAPABILITIES)
        return "TerminologyCapabilities";
      if (code == ResourceType.TESTREPORT)
        return "TestReport";
      if (code == ResourceType.TESTSCRIPT)
        return "TestScript";
      if (code == ResourceType.USERSESSION)
        return "UserSession";
      if (code == ResourceType.VALUESET)
        return "ValueSet";
      if (code == ResourceType.VERIFICATIONRESULT)
        return "VerificationResult";
      if (code == ResourceType.VISIONPRESCRIPTION)
        return "VisionPrescription";
      return "?";
      }
    public String toSystem(ResourceType code) {
      return code.getSystem();
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
         * Search parameter on a coded element or identifier. May be used to search through the text, display, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "|", depending on the modifier used.
         */
        TOKEN, 
        /**
         * A reference to another resource (Reference or canonical).
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
         * Special logic applies to this parameter per the description of the search parameter.
         */
        SPECIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchParamType fromCode(String codeString) throws FHIRException {
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
        if ("special".equals(codeString))
          return SPECIAL;
        throw new FHIRException("Unknown SearchParamType code '"+codeString+"'");
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
            case SPECIAL: return "special";
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
            case SPECIAL: return "http://hl7.org/fhir/search-param-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NUMBER: return "Search parameter SHALL be a number (a whole number, or a decimal).";
            case DATE: return "Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.";
            case STRING: return "Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.";
            case TOKEN: return "Search parameter on a coded element or identifier. May be used to search through the text, display, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a \"|\", depending on the modifier used.";
            case REFERENCE: return "A reference to another resource (Reference or canonical).";
            case COMPOSITE: return "A composite search parameter that combines a search on two values together.";
            case QUANTITY: return "A search parameter that searches on a quantity.";
            case URI: return "A search parameter that searches on a URI (RFC 3986).";
            case SPECIAL: return "Special logic applies to this parameter per the description of the search parameter.";
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
            case SPECIAL: return "Special";
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
        if ("special".equals(codeString))
          return SearchParamType.SPECIAL;
        throw new IllegalArgumentException("Unknown SearchParamType code '"+codeString+"'");
        }
        public Enumeration<SearchParamType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SearchParamType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("number".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.NUMBER);
        if ("date".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.DATE);
        if ("string".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.STRING);
        if ("token".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.TOKEN);
        if ("reference".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.REFERENCE);
        if ("composite".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.COMPOSITE);
        if ("quantity".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.QUANTITY);
        if ("uri".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.URI);
        if ("special".equals(codeString))
          return new Enumeration<SearchParamType>(this, SearchParamType.SPECIAL);
        throw new FHIRException("Unknown SearchParamType code '"+codeString+"'");
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
      if (code == SearchParamType.SPECIAL)
        return "special";
      return "?";
      }
    public String toSystem(SearchParamType code) {
      return code.getSystem();
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
        public static SpecialValues fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown SpecialValues code '"+codeString+"'");
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
            case TRUE: return "http://terminology.hl7.org/CodeSystem/special-values";
            case FALSE: return "http://terminology.hl7.org/CodeSystem/special-values";
            case TRACE: return "http://terminology.hl7.org/CodeSystem/special-values";
            case SUFFICIENT: return "http://terminology.hl7.org/CodeSystem/special-values";
            case WITHDRAWN: return "http://terminology.hl7.org/CodeSystem/special-values";
            case NILKNOWN: return "http://terminology.hl7.org/CodeSystem/special-values";
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
        public Enumeration<SpecialValues> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SpecialValues>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("true".equals(codeString))
          return new Enumeration<SpecialValues>(this, SpecialValues.TRUE);
        if ("false".equals(codeString))
          return new Enumeration<SpecialValues>(this, SpecialValues.FALSE);
        if ("trace".equals(codeString))
          return new Enumeration<SpecialValues>(this, SpecialValues.TRACE);
        if ("sufficient".equals(codeString))
          return new Enumeration<SpecialValues>(this, SpecialValues.SUFFICIENT);
        if ("withdrawn".equals(codeString))
          return new Enumeration<SpecialValues>(this, SpecialValues.WITHDRAWN);
        if ("nil-known".equals(codeString))
          return new Enumeration<SpecialValues>(this, SpecialValues.NILKNOWN);
        throw new FHIRException("Unknown SpecialValues code '"+codeString+"'");
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
    public String toSystem(SpecialValues code) {
      return code.getSystem();
      }
    }


}

