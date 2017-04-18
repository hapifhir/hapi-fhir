package org.hl7.fhir.dstu3.model;

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

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Measurements and simple assertions made about a patient, device or other subject.
 */
@ResourceDef(name="Observation", profile="http://hl7.org/fhir/Profile/Observation")
public class Observation extends DomainResource {

    public enum ObservationStatus {
        /**
         * The existence of the observation is registered, but there is no result yet available.
         */
        REGISTERED, 
        /**
         * This is an initial or interim observation: data may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * The observation is complete.
         */
        FINAL, 
        /**
         * Subsequent to being Final, the observation has been modified subsequent.  This includes updates/new information and corrections.
         */
        AMENDED, 
        /**
         * Subsequent to being Final, the observation has been modified to correct an error in the test result.
         */
        CORRECTED, 
        /**
         * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The observation has been withdrawn following previous final release.  This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this request. Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring system does not know which.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ObservationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return REGISTERED;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("corrected".equals(codeString))
          return CORRECTED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ObservationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case CORRECTED: return "corrected";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REGISTERED: return "http://hl7.org/fhir/observation-status";
            case PRELIMINARY: return "http://hl7.org/fhir/observation-status";
            case FINAL: return "http://hl7.org/fhir/observation-status";
            case AMENDED: return "http://hl7.org/fhir/observation-status";
            case CORRECTED: return "http://hl7.org/fhir/observation-status";
            case CANCELLED: return "http://hl7.org/fhir/observation-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/observation-status";
            case UNKNOWN: return "http://hl7.org/fhir/observation-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the observation is registered, but there is no result yet available.";
            case PRELIMINARY: return "This is an initial or interim observation: data may be incomplete or unverified.";
            case FINAL: return "The observation is complete.";
            case AMENDED: return "Subsequent to being Final, the observation has been modified subsequent.  This includes updates/new information and corrections.";
            case CORRECTED: return "Subsequent to being Final, the observation has been modified to correct an error in the test result.";
            case CANCELLED: return "The observation is unavailable because the measurement was not started or not completed (also sometimes called \"aborted\").";
            case ENTEREDINERROR: return "The observation has been withdrawn following previous final release.  This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request. Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, but the authoring system does not know which.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "Registered";
            case PRELIMINARY: return "Preliminary";
            case FINAL: return "Final";
            case AMENDED: return "Amended";
            case CORRECTED: return "Corrected";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class ObservationStatusEnumFactory implements EnumFactory<ObservationStatus> {
    public ObservationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return ObservationStatus.REGISTERED;
        if ("preliminary".equals(codeString))
          return ObservationStatus.PRELIMINARY;
        if ("final".equals(codeString))
          return ObservationStatus.FINAL;
        if ("amended".equals(codeString))
          return ObservationStatus.AMENDED;
        if ("corrected".equals(codeString))
          return ObservationStatus.CORRECTED;
        if ("cancelled".equals(codeString))
          return ObservationStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ObservationStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ObservationStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ObservationStatus code '"+codeString+"'");
        }
        public Enumeration<ObservationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ObservationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("registered".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.REGISTERED);
        if ("preliminary".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.PRELIMINARY);
        if ("final".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.FINAL);
        if ("amended".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.AMENDED);
        if ("corrected".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.CORRECTED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ObservationStatus>(this, ObservationStatus.UNKNOWN);
        throw new FHIRException("Unknown ObservationStatus code '"+codeString+"'");
        }
    public String toCode(ObservationStatus code) {
      if (code == ObservationStatus.REGISTERED)
        return "registered";
      if (code == ObservationStatus.PRELIMINARY)
        return "preliminary";
      if (code == ObservationStatus.FINAL)
        return "final";
      if (code == ObservationStatus.AMENDED)
        return "amended";
      if (code == ObservationStatus.CORRECTED)
        return "corrected";
      if (code == ObservationStatus.CANCELLED)
        return "cancelled";
      if (code == ObservationStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ObservationStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ObservationStatus code) {
      return code.getSystem();
      }
    }

    public enum ObservationRelationshipType {
        /**
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.
         */
        HASMEMBER, 
        /**
         * The target resource (Observation or QuestionnaireResponse) is part of the information from which this observation value is derived. (e.g. calculated anion gap, Apgar score)  NOTE:  "derived-from" is the only logical choice when referencing QuestionnaireResponse.
         */
        DERIVEDFROM, 
        /**
         * This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).
         */
        SEQUELTO, 
        /**
         * This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.
         */
        REPLACES, 
        /**
         * The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipemia measure target from a plasma measure).
         */
        QUALIFIEDBY, 
        /**
         * The value of the target observation interferes (degrades quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure, which has no value).
         */
        INTERFEREDBY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ObservationRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("has-member".equals(codeString))
          return HASMEMBER;
        if ("derived-from".equals(codeString))
          return DERIVEDFROM;
        if ("sequel-to".equals(codeString))
          return SEQUELTO;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("qualified-by".equals(codeString))
          return QUALIFIEDBY;
        if ("interfered-by".equals(codeString))
          return INTERFEREDBY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ObservationRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HASMEMBER: return "has-member";
            case DERIVEDFROM: return "derived-from";
            case SEQUELTO: return "sequel-to";
            case REPLACES: return "replaces";
            case QUALIFIEDBY: return "qualified-by";
            case INTERFEREDBY: return "interfered-by";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HASMEMBER: return "http://hl7.org/fhir/observation-relationshiptypes";
            case DERIVEDFROM: return "http://hl7.org/fhir/observation-relationshiptypes";
            case SEQUELTO: return "http://hl7.org/fhir/observation-relationshiptypes";
            case REPLACES: return "http://hl7.org/fhir/observation-relationshiptypes";
            case QUALIFIEDBY: return "http://hl7.org/fhir/observation-relationshiptypes";
            case INTERFEREDBY: return "http://hl7.org/fhir/observation-relationshiptypes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HASMEMBER: return "This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.";
            case DERIVEDFROM: return "The target resource (Observation or QuestionnaireResponse) is part of the information from which this observation value is derived. (e.g. calculated anion gap, Apgar score)  NOTE:  \"derived-from\" is the only logical choice when referencing QuestionnaireResponse.";
            case SEQUELTO: return "This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).";
            case REPLACES: return "This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.";
            case QUALIFIEDBY: return "The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipemia measure target from a plasma measure).";
            case INTERFEREDBY: return "The value of the target observation interferes (degrades quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure, which has no value).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HASMEMBER: return "Has Member";
            case DERIVEDFROM: return "Derived From";
            case SEQUELTO: return "Sequel To";
            case REPLACES: return "Replaces";
            case QUALIFIEDBY: return "Qualified By";
            case INTERFEREDBY: return "Interfered By";
            default: return "?";
          }
        }
    }

  public static class ObservationRelationshipTypeEnumFactory implements EnumFactory<ObservationRelationshipType> {
    public ObservationRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("has-member".equals(codeString))
          return ObservationRelationshipType.HASMEMBER;
        if ("derived-from".equals(codeString))
          return ObservationRelationshipType.DERIVEDFROM;
        if ("sequel-to".equals(codeString))
          return ObservationRelationshipType.SEQUELTO;
        if ("replaces".equals(codeString))
          return ObservationRelationshipType.REPLACES;
        if ("qualified-by".equals(codeString))
          return ObservationRelationshipType.QUALIFIEDBY;
        if ("interfered-by".equals(codeString))
          return ObservationRelationshipType.INTERFEREDBY;
        throw new IllegalArgumentException("Unknown ObservationRelationshipType code '"+codeString+"'");
        }
        public Enumeration<ObservationRelationshipType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ObservationRelationshipType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("has-member".equals(codeString))
          return new Enumeration<ObservationRelationshipType>(this, ObservationRelationshipType.HASMEMBER);
        if ("derived-from".equals(codeString))
          return new Enumeration<ObservationRelationshipType>(this, ObservationRelationshipType.DERIVEDFROM);
        if ("sequel-to".equals(codeString))
          return new Enumeration<ObservationRelationshipType>(this, ObservationRelationshipType.SEQUELTO);
        if ("replaces".equals(codeString))
          return new Enumeration<ObservationRelationshipType>(this, ObservationRelationshipType.REPLACES);
        if ("qualified-by".equals(codeString))
          return new Enumeration<ObservationRelationshipType>(this, ObservationRelationshipType.QUALIFIEDBY);
        if ("interfered-by".equals(codeString))
          return new Enumeration<ObservationRelationshipType>(this, ObservationRelationshipType.INTERFEREDBY);
        throw new FHIRException("Unknown ObservationRelationshipType code '"+codeString+"'");
        }
    public String toCode(ObservationRelationshipType code) {
      if (code == ObservationRelationshipType.HASMEMBER)
        return "has-member";
      if (code == ObservationRelationshipType.DERIVEDFROM)
        return "derived-from";
      if (code == ObservationRelationshipType.SEQUELTO)
        return "sequel-to";
      if (code == ObservationRelationshipType.REPLACES)
        return "replaces";
      if (code == ObservationRelationshipType.QUALIFIEDBY)
        return "qualified-by";
      if (code == ObservationRelationshipType.INTERFEREDBY)
        return "interfered-by";
      return "?";
      }
    public String toSystem(ObservationRelationshipType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ObservationReferenceRangeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The value of the low bound of the reference range.  The low bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the low bound is omitted,  it is assumed to be meaningless (e.g. reference range is <=2.3).
         */
        @Child(name = "low", type = {SimpleQuantity.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Low Range, if relevant", formalDefinition="The value of the low bound of the reference range.  The low bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the low bound is omitted,  it is assumed to be meaningless (e.g. reference range is <=2.3)." )
        protected SimpleQuantity low;

        /**
         * The value of the high bound of the reference range.  The high bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the high bound is omitted,  it is assumed to be meaningless (e.g. reference range is >= 2.3).
         */
        @Child(name = "high", type = {SimpleQuantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="High Range, if relevant", formalDefinition="The value of the high bound of the reference range.  The high bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the high bound is omitted,  it is assumed to be meaningless (e.g. reference range is >= 2.3)." )
        protected SimpleQuantity high;

        /**
         * Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or therapeutic range.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference range qualifier", formalDefinition="Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or therapeutic range." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referencerange-meaning")
        protected CodeableConcept type;

        /**
         * Codes to indicate the target population this reference range applies to.  For example, a reference range may be based on the normal population or a particular sex or race.
         */
        @Child(name = "appliesTo", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Reference range population", formalDefinition="Codes to indicate the target population this reference range applies to.  For example, a reference range may be based on the normal population or a particular sex or race." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referencerange-appliesto")
        protected List<CodeableConcept> appliesTo;

        /**
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.
         */
        @Child(name = "age", type = {Range.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applicable age range, if relevant", formalDefinition="The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so." )
        protected Range age;

        /**
         * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.
         */
        @Child(name = "text", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Text based reference range in an observation", formalDefinition="Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'." )
        protected StringType text;

        private static final long serialVersionUID = -955638831L;

    /**
     * Constructor
     */
      public ObservationReferenceRangeComponent() {
        super();
      }

        /**
         * @return {@link #low} (The value of the low bound of the reference range.  The low bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the low bound is omitted,  it is assumed to be meaningless (e.g. reference range is <=2.3).)
         */
        public SimpleQuantity getLow() { 
          if (this.low == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.low");
            else if (Configuration.doAutoCreate())
              this.low = new SimpleQuantity(); // cc
          return this.low;
        }

        public boolean hasLow() { 
          return this.low != null && !this.low.isEmpty();
        }

        /**
         * @param value {@link #low} (The value of the low bound of the reference range.  The low bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the low bound is omitted,  it is assumed to be meaningless (e.g. reference range is <=2.3).)
         */
        public ObservationReferenceRangeComponent setLow(SimpleQuantity value) { 
          this.low = value;
          return this;
        }

        /**
         * @return {@link #high} (The value of the high bound of the reference range.  The high bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the high bound is omitted,  it is assumed to be meaningless (e.g. reference range is >= 2.3).)
         */
        public SimpleQuantity getHigh() { 
          if (this.high == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.high");
            else if (Configuration.doAutoCreate())
              this.high = new SimpleQuantity(); // cc
          return this.high;
        }

        public boolean hasHigh() { 
          return this.high != null && !this.high.isEmpty();
        }

        /**
         * @param value {@link #high} (The value of the high bound of the reference range.  The high bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the high bound is omitted,  it is assumed to be meaningless (e.g. reference range is >= 2.3).)
         */
        public ObservationReferenceRangeComponent setHigh(SimpleQuantity value) { 
          this.high = value;
          return this;
        }

        /**
         * @return {@link #type} (Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or therapeutic range.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or therapeutic range.)
         */
        public ObservationReferenceRangeComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #appliesTo} (Codes to indicate the target population this reference range applies to.  For example, a reference range may be based on the normal population or a particular sex or race.)
         */
        public List<CodeableConcept> getAppliesTo() { 
          if (this.appliesTo == null)
            this.appliesTo = new ArrayList<CodeableConcept>();
          return this.appliesTo;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ObservationReferenceRangeComponent setAppliesTo(List<CodeableConcept> theAppliesTo) { 
          this.appliesTo = theAppliesTo;
          return this;
        }

        public boolean hasAppliesTo() { 
          if (this.appliesTo == null)
            return false;
          for (CodeableConcept item : this.appliesTo)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAppliesTo() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.appliesTo == null)
            this.appliesTo = new ArrayList<CodeableConcept>();
          this.appliesTo.add(t);
          return t;
        }

        public ObservationReferenceRangeComponent addAppliesTo(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.appliesTo == null)
            this.appliesTo = new ArrayList<CodeableConcept>();
          this.appliesTo.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #appliesTo}, creating it if it does not already exist
         */
        public CodeableConcept getAppliesToFirstRep() { 
          if (getAppliesTo().isEmpty()) {
            addAppliesTo();
          }
          return getAppliesTo().get(0);
        }

        /**
         * @return {@link #age} (The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.)
         */
        public Range getAge() { 
          if (this.age == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.age");
            else if (Configuration.doAutoCreate())
              this.age = new Range(); // cc
          return this.age;
        }

        public boolean hasAge() { 
          return this.age != null && !this.age.isEmpty();
        }

        /**
         * @param value {@link #age} (The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.)
         */
        public ObservationReferenceRangeComponent setAge(Range value) { 
          this.age = value;
          return this;
        }

        /**
         * @return {@link #text} (Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ObservationReferenceRangeComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.
         */
        public ObservationReferenceRangeComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("low", "SimpleQuantity", "The value of the low bound of the reference range.  The low bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the low bound is omitted,  it is assumed to be meaningless (e.g. reference range is <=2.3).", 0, java.lang.Integer.MAX_VALUE, low));
          childrenList.add(new Property("high", "SimpleQuantity", "The value of the high bound of the reference range.  The high bound of the reference range endpoint is inclusive of the value (e.g.  reference range is >=5 - <=9).   If the high bound is omitted,  it is assumed to be meaningless (e.g. reference range is >= 2.3).", 0, java.lang.Integer.MAX_VALUE, high));
          childrenList.add(new Property("type", "CodeableConcept", "Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or therapeutic range.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("appliesTo", "CodeableConcept", "Codes to indicate the target population this reference range applies to.  For example, a reference range may be based on the normal population or a particular sex or race.", 0, java.lang.Integer.MAX_VALUE, appliesTo));
          childrenList.add(new Property("age", "Range", "The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.", 0, java.lang.Integer.MAX_VALUE, age));
          childrenList.add(new Property("text", "string", "Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 107348: /*low*/ return this.low == null ? new Base[0] : new Base[] {this.low}; // SimpleQuantity
        case 3202466: /*high*/ return this.high == null ? new Base[0] : new Base[] {this.high}; // SimpleQuantity
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -2089924569: /*appliesTo*/ return this.appliesTo == null ? new Base[0] : this.appliesTo.toArray(new Base[this.appliesTo.size()]); // CodeableConcept
        case 96511: /*age*/ return this.age == null ? new Base[0] : new Base[] {this.age}; // Range
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 107348: // low
          this.low = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case 3202466: // high
          this.high = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2089924569: // appliesTo
          this.getAppliesTo().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 96511: // age
          this.age = castToRange(value); // Range
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("low")) {
          this.low = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("high")) {
          this.high = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("appliesTo")) {
          this.getAppliesTo().add(castToCodeableConcept(value));
        } else if (name.equals("age")) {
          this.age = castToRange(value); // Range
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 107348:  return getLow(); 
        case 3202466:  return getHigh(); 
        case 3575610:  return getType(); 
        case -2089924569:  return addAppliesTo(); 
        case 96511:  return getAge(); 
        case 3556653:  return getTextElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 107348: /*low*/ return new String[] {"SimpleQuantity"};
        case 3202466: /*high*/ return new String[] {"SimpleQuantity"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -2089924569: /*appliesTo*/ return new String[] {"CodeableConcept"};
        case 96511: /*age*/ return new String[] {"Range"};
        case 3556653: /*text*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("low")) {
          this.low = new SimpleQuantity();
          return this.low;
        }
        else if (name.equals("high")) {
          this.high = new SimpleQuantity();
          return this.high;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("appliesTo")) {
          return addAppliesTo();
        }
        else if (name.equals("age")) {
          this.age = new Range();
          return this.age;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Observation.text");
        }
        else
          return super.addChild(name);
      }

      public ObservationReferenceRangeComponent copy() {
        ObservationReferenceRangeComponent dst = new ObservationReferenceRangeComponent();
        copyValues(dst);
        dst.low = low == null ? null : low.copy();
        dst.high = high == null ? null : high.copy();
        dst.type = type == null ? null : type.copy();
        if (appliesTo != null) {
          dst.appliesTo = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : appliesTo)
            dst.appliesTo.add(i.copy());
        };
        dst.age = age == null ? null : age.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ObservationReferenceRangeComponent))
          return false;
        ObservationReferenceRangeComponent o = (ObservationReferenceRangeComponent) other;
        return compareDeep(low, o.low, true) && compareDeep(high, o.high, true) && compareDeep(type, o.type, true)
           && compareDeep(appliesTo, o.appliesTo, true) && compareDeep(age, o.age, true) && compareDeep(text, o.text, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ObservationReferenceRangeComponent))
          return false;
        ObservationReferenceRangeComponent o = (ObservationReferenceRangeComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(low, high, type, appliesTo
          , age, text);
      }

  public String fhirType() {
    return "Observation.referenceRange";

  }

  }

    @Block()
    public static class ObservationRelatedComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code specifying the kind of relationship that exists with the target resource.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by", formalDefinition="A code specifying the kind of relationship that exists with the target resource." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-relationshiptypes")
        protected Enumeration<ObservationRelationshipType> type;

        /**
         * A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.
         */
        @Child(name = "target", type = {Observation.class, QuestionnaireResponse.class, Sequence.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource that is related to this one", formalDefinition="A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.)
         */
        protected Resource targetTarget;

        private static final long serialVersionUID = 1541802577L;

    /**
     * Constructor
     */
      public ObservationRelatedComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ObservationRelatedComponent(Reference target) {
        super();
        this.target = target;
      }

        /**
         * @return {@link #type} (A code specifying the kind of relationship that exists with the target resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ObservationRelationshipType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationRelatedComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ObservationRelationshipType>(new ObservationRelationshipTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code specifying the kind of relationship that exists with the target resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ObservationRelatedComponent setTypeElement(Enumeration<ObservationRelationshipType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return A code specifying the kind of relationship that exists with the target resource.
         */
        public ObservationRelationshipType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value A code specifying the kind of relationship that exists with the target resource.
         */
        public ObservationRelatedComponent setType(ObservationRelationshipType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<ObservationRelationshipType>(new ObservationRelationshipTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationRelatedComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.)
         */
        public ObservationRelatedComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.)
         */
        public Resource getTargetTarget() { 
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.)
         */
        public ObservationRelatedComponent setTargetTarget(Resource value) { 
          this.targetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "A code specifying the kind of relationship that exists with the target resource.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("target", "Reference(Observation|QuestionnaireResponse|Sequence)", "A reference to the observation or [[[QuestionnaireResponse]]] resource that is related to this observation.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ObservationRelationshipType>
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new ObservationRelationshipTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ObservationRelationshipType>
          return value;
        case -880905839: // target
          this.target = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new ObservationRelationshipTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ObservationRelationshipType>
        } else if (name.equals("target")) {
          this.target = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -880905839:  return getTarget(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -880905839: /*target*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Observation.type");
        }
        else if (name.equals("target")) {
          this.target = new Reference();
          return this.target;
        }
        else
          return super.addChild(name);
      }

      public ObservationRelatedComponent copy() {
        ObservationRelatedComponent dst = new ObservationRelatedComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ObservationRelatedComponent))
          return false;
        ObservationRelatedComponent o = (ObservationRelatedComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ObservationRelatedComponent))
          return false;
        ObservationRelatedComponent o = (ObservationRelatedComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, target);
      }

  public String fhirType() {
    return "Observation.related";

  }

  }

    @Block()
    public static class ObservationComponentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes what was observed. Sometimes this is called the observation "code".
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of component observation (code / type)", formalDefinition="Describes what was observed. Sometimes this is called the observation \"code\"." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
        protected CodeableConcept code;

        /**
         * The information determined as a result of making the observation, if the information has a simple value.
         */
        @Child(name = "value", type = {Quantity.class, CodeableConcept.class, StringType.class, Range.class, Ratio.class, SampledData.class, Attachment.class, TimeType.class, DateTimeType.class, Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Actual component result", formalDefinition="The information determined as a result of making the observation, if the information has a simple value." )
        protected Type value;

        /**
         * Provides a reason why the expected value in the element Observation.value[x] is missing.
         */
        @Child(name = "dataAbsentReason", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Why the component result is missing", formalDefinition="Provides a reason why the expected value in the element Observation.value[x] is missing." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-valueabsentreason")
        protected CodeableConcept dataAbsentReason;

        /**
         * The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.
         */
        @Child(name = "interpretation", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="High, low, normal, etc.", formalDefinition="The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-interpretation")
        protected CodeableConcept interpretation;

        /**
         * Guidance on how to interpret the value by comparison to a normal or recommended range.
         */
        @Child(name = "referenceRange", type = {ObservationReferenceRangeComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Provides guide for interpretation of component result", formalDefinition="Guidance on how to interpret the value by comparison to a normal or recommended range." )
        protected List<ObservationReferenceRangeComponent> referenceRange;

        private static final long serialVersionUID = -846379911L;

    /**
     * Constructor
     */
      public ObservationComponentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ObservationComponentComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Describes what was observed. Sometimes this is called the observation "code".)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationComponentComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Describes what was observed. Sometimes this is called the observation "code".)
         */
        public ObservationComponentComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public CodeableConcept getValueCodeableConcept() throws FHIRException { 
          if (!(this.value instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeableConcept) this.value;
        }

        public boolean hasValueCodeableConcept() { 
          return this.value instanceof CodeableConcept;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public Range getValueRange() throws FHIRException { 
          if (!(this.value instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Range) this.value;
        }

        public boolean hasValueRange() { 
          return this.value instanceof Range;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public Ratio getValueRatio() throws FHIRException { 
          if (!(this.value instanceof Ratio))
            throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Ratio) this.value;
        }

        public boolean hasValueRatio() { 
          return this.value instanceof Ratio;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public SampledData getValueSampledData() throws FHIRException { 
          if (!(this.value instanceof SampledData))
            throw new FHIRException("Type mismatch: the type SampledData was expected, but "+this.value.getClass().getName()+" was encountered");
          return (SampledData) this.value;
        }

        public boolean hasValueSampledData() { 
          return this.value instanceof SampledData;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public Attachment getValueAttachment() throws FHIRException { 
          if (!(this.value instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

        public boolean hasValueAttachment() { 
          return this.value instanceof Attachment;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public TimeType getValueTimeType() throws FHIRException { 
          if (!(this.value instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (TimeType) this.value;
        }

        public boolean hasValueTimeType() { 
          return this.value instanceof TimeType;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public Period getValuePeriod() throws FHIRException { 
          if (!(this.value instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Period) this.value;
        }

        public boolean hasValuePeriod() { 
          return this.value instanceof Period;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
         */
        public ObservationComponentComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #dataAbsentReason} (Provides a reason why the expected value in the element Observation.value[x] is missing.)
         */
        public CodeableConcept getDataAbsentReason() { 
          if (this.dataAbsentReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationComponentComponent.dataAbsentReason");
            else if (Configuration.doAutoCreate())
              this.dataAbsentReason = new CodeableConcept(); // cc
          return this.dataAbsentReason;
        }

        public boolean hasDataAbsentReason() { 
          return this.dataAbsentReason != null && !this.dataAbsentReason.isEmpty();
        }

        /**
         * @param value {@link #dataAbsentReason} (Provides a reason why the expected value in the element Observation.value[x] is missing.)
         */
        public ObservationComponentComponent setDataAbsentReason(CodeableConcept value) { 
          this.dataAbsentReason = value;
          return this;
        }

        /**
         * @return {@link #interpretation} (The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.)
         */
        public CodeableConcept getInterpretation() { 
          if (this.interpretation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationComponentComponent.interpretation");
            else if (Configuration.doAutoCreate())
              this.interpretation = new CodeableConcept(); // cc
          return this.interpretation;
        }

        public boolean hasInterpretation() { 
          return this.interpretation != null && !this.interpretation.isEmpty();
        }

        /**
         * @param value {@link #interpretation} (The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.)
         */
        public ObservationComponentComponent setInterpretation(CodeableConcept value) { 
          this.interpretation = value;
          return this;
        }

        /**
         * @return {@link #referenceRange} (Guidance on how to interpret the value by comparison to a normal or recommended range.)
         */
        public List<ObservationReferenceRangeComponent> getReferenceRange() { 
          if (this.referenceRange == null)
            this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
          return this.referenceRange;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ObservationComponentComponent setReferenceRange(List<ObservationReferenceRangeComponent> theReferenceRange) { 
          this.referenceRange = theReferenceRange;
          return this;
        }

        public boolean hasReferenceRange() { 
          if (this.referenceRange == null)
            return false;
          for (ObservationReferenceRangeComponent item : this.referenceRange)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ObservationReferenceRangeComponent addReferenceRange() { //3
          ObservationReferenceRangeComponent t = new ObservationReferenceRangeComponent();
          if (this.referenceRange == null)
            this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
          this.referenceRange.add(t);
          return t;
        }

        public ObservationComponentComponent addReferenceRange(ObservationReferenceRangeComponent t) { //3
          if (t == null)
            return this;
          if (this.referenceRange == null)
            this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
          this.referenceRange.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #referenceRange}, creating it if it does not already exist
         */
        public ObservationReferenceRangeComponent getReferenceRangeFirstRep() { 
          if (getReferenceRange().isEmpty()) {
            addReferenceRange();
          }
          return getReferenceRange().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Describes what was observed. Sometimes this is called the observation \"code\".", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("value[x]", "Quantity|CodeableConcept|string|Range|Ratio|SampledData|Attachment|time|dateTime|Period", "The information determined as a result of making the observation, if the information has a simple value.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("dataAbsentReason", "CodeableConcept", "Provides a reason why the expected value in the element Observation.value[x] is missing.", 0, java.lang.Integer.MAX_VALUE, dataAbsentReason));
          childrenList.add(new Property("interpretation", "CodeableConcept", "The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.", 0, java.lang.Integer.MAX_VALUE, interpretation));
          childrenList.add(new Property("referenceRange", "@Observation.referenceRange", "Guidance on how to interpret the value by comparison to a normal or recommended range.", 0, java.lang.Integer.MAX_VALUE, referenceRange));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        case 1034315687: /*dataAbsentReason*/ return this.dataAbsentReason == null ? new Base[0] : new Base[] {this.dataAbsentReason}; // CodeableConcept
        case -297950712: /*interpretation*/ return this.interpretation == null ? new Base[0] : new Base[] {this.interpretation}; // CodeableConcept
        case -1912545102: /*referenceRange*/ return this.referenceRange == null ? new Base[0] : this.referenceRange.toArray(new Base[this.referenceRange.size()]); // ObservationReferenceRangeComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        case 1034315687: // dataAbsentReason
          this.dataAbsentReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -297950712: // interpretation
          this.interpretation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1912545102: // referenceRange
          this.getReferenceRange().add((ObservationReferenceRangeComponent) value); // ObservationReferenceRangeComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else if (name.equals("dataAbsentReason")) {
          this.dataAbsentReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("interpretation")) {
          this.interpretation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("referenceRange")) {
          this.getReferenceRange().add((ObservationReferenceRangeComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        case 1034315687:  return getDataAbsentReason(); 
        case -297950712:  return getInterpretation(); 
        case -1912545102:  return addReferenceRange(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"Quantity", "CodeableConcept", "string", "Range", "Ratio", "SampledData", "Attachment", "time", "dateTime", "Period"};
        case 1034315687: /*dataAbsentReason*/ return new String[] {"CodeableConcept"};
        case -297950712: /*interpretation*/ return new String[] {"CodeableConcept"};
        case -1912545102: /*referenceRange*/ return new String[] {"@Observation.referenceRange"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("dataAbsentReason")) {
          this.dataAbsentReason = new CodeableConcept();
          return this.dataAbsentReason;
        }
        else if (name.equals("interpretation")) {
          this.interpretation = new CodeableConcept();
          return this.interpretation;
        }
        else if (name.equals("referenceRange")) {
          return addReferenceRange();
        }
        else
          return super.addChild(name);
      }

      public ObservationComponentComponent copy() {
        ObservationComponentComponent dst = new ObservationComponentComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        dst.dataAbsentReason = dataAbsentReason == null ? null : dataAbsentReason.copy();
        dst.interpretation = interpretation == null ? null : interpretation.copy();
        if (referenceRange != null) {
          dst.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
          for (ObservationReferenceRangeComponent i : referenceRange)
            dst.referenceRange.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ObservationComponentComponent))
          return false;
        ObservationComponentComponent o = (ObservationComponentComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true) && compareDeep(dataAbsentReason, o.dataAbsentReason, true)
           && compareDeep(interpretation, o.interpretation, true) && compareDeep(referenceRange, o.referenceRange, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ObservationComponentComponent))
          return false;
        ObservationComponentComponent o = (ObservationComponentComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, value, dataAbsentReason
          , interpretation, referenceRange);
      }

  public String fhirType() {
    return "Observation.component";

  }

  }

    /**
     * A unique identifier assigned to this observation.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for observation", formalDefinition="A unique identifier assigned to this observation." )
    protected List<Identifier> identifier;

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this event.
     */
    @Child(name = "basedOn", type = {CarePlan.class, DeviceRequest.class, ImmunizationRecommendation.class, MedicationRequest.class, NutritionOrder.class, ProcedureRequest.class, ReferralRequest.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Fulfills plan, proposal or order", formalDefinition="A plan, proposal or order that is fulfilled in whole or in part by this event." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A plan, proposal or order that is fulfilled in whole or in part by this event.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * The status of the result value.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="registered | preliminary | final | amended +", formalDefinition="The status of the result value." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-status")
    protected Enumeration<ObservationStatus> status;

    /**
     * A code that classifies the general type of observation being made.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Classification of  type of observation", formalDefinition="A code that classifies the general type of observation being made." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-category")
    protected List<CodeableConcept> category;

    /**
     * Describes what was observed. Sometimes this is called the observation "name".
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of observation (code / type)", formalDefinition="Describes what was observed. Sometimes this is called the observation \"name\"." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
    protected CodeableConcept code;

    /**
     * The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what this is about", formalDefinition="The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    protected Resource subjectTarget;

    /**
     * The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Healthcare event during which this observation is made", formalDefinition="The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.)
     */
    protected Resource contextTarget;

    /**
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Clinically relevant time/time-period for observation", formalDefinition="The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself." )
    protected Type effective;

    /**
     * The date and time this observation was made available to providers, typically after the results have been reviewed and verified.
     */
    @Child(name = "issued", type = {InstantType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date/Time this was made available", formalDefinition="The date and time this observation was made available to providers, typically after the results have been reviewed and verified." )
    protected InstantType issued;

    /**
     * Who was responsible for asserting the observed value as "true".
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is responsible for the observation", formalDefinition="Who was responsible for asserting the observed value as \"true\"." )
    protected List<Reference> performer;
    /**
     * The actual objects that are the target of the reference (Who was responsible for asserting the observed value as "true".)
     */
    protected List<Resource> performerTarget;


    /**
     * The information determined as a result of making the observation, if the information has a simple value.
     */
    @Child(name = "value", type = {Quantity.class, CodeableConcept.class, StringType.class, BooleanType.class, Range.class, Ratio.class, SampledData.class, Attachment.class, TimeType.class, DateTimeType.class, Period.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Actual result", formalDefinition="The information determined as a result of making the observation, if the information has a simple value." )
    protected Type value;

    /**
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     */
    @Child(name = "dataAbsentReason", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why the result is missing", formalDefinition="Provides a reason why the expected value in the element Observation.value[x] is missing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-valueabsentreason")
    protected CodeableConcept dataAbsentReason;

    /**
     * The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.
     */
    @Child(name = "interpretation", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="High, low, normal, etc.", formalDefinition="The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-interpretation")
    protected CodeableConcept interpretation;

    /**
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     */
    @Child(name = "comment", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Comments about result", formalDefinition="May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result." )
    protected StringType comment;

    /**
     * Indicates the site on the subject's body where the observation was made (i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Observed body part", formalDefinition="Indicates the site on the subject's body where the observation was made (i.e. the target site)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected CodeableConcept bodySite;

    /**
     * Indicates the mechanism used to perform the observation.
     */
    @Child(name = "method", type = {CodeableConcept.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How it was done", formalDefinition="Indicates the mechanism used to perform the observation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-methods")
    protected CodeableConcept method;

    /**
     * The specimen that was used when this observation was made.
     */
    @Child(name = "specimen", type = {Specimen.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Specimen used for this observation", formalDefinition="The specimen that was used when this observation was made." )
    protected Reference specimen;

    /**
     * The actual object that is the target of the reference (The specimen that was used when this observation was made.)
     */
    protected Specimen specimenTarget;

    /**
     * The device used to generate the observation data.
     */
    @Child(name = "device", type = {Device.class, DeviceMetric.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="(Measurement) Device", formalDefinition="The device used to generate the observation data." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The device used to generate the observation data.)
     */
    protected Resource deviceTarget;

    /**
     * Guidance on how to interpret the value by comparison to a normal or recommended range.
     */
    @Child(name = "referenceRange", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Provides guide for interpretation", formalDefinition="Guidance on how to interpret the value by comparison to a normal or recommended range." )
    protected List<ObservationReferenceRangeComponent> referenceRange;

    /**
     * A  reference to another resource (usually another Observation) whose relationship is defined by the relationship type code.
     */
    @Child(name = "related", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource related to this observation", formalDefinition="A  reference to another resource (usually another Observation) whose relationship is defined by the relationship type code." )
    protected List<ObservationRelatedComponent> related;

    /**
     * Some observations have multiple component observations.  These component observations are expressed as separate code value pairs that share the same attributes.  Examples include systolic and diastolic component observations for blood pressure measurement and multiple component observations for genetics observations.
     */
    @Child(name = "component", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Component results", formalDefinition="Some observations have multiple component observations.  These component observations are expressed as separate code value pairs that share the same attributes.  Examples include systolic and diastolic component observations for blood pressure measurement and multiple component observations for genetics observations." )
    protected List<ObservationComponentComponent> component;

    private static final long serialVersionUID = -1771290322L;

  /**
   * Constructor
   */
    public Observation() {
      super();
    }

  /**
   * Constructor
   */
    public Observation(Enumeration<ObservationStatus> status, CodeableConcept code) {
      super();
      this.status = status;
      this.code = code;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this observation.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public Observation addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #basedOn} (A plan, proposal or order that is fulfilled in whole or in part by this event.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public Observation addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #status} (The status of the result value.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ObservationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ObservationStatus>(new ObservationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the result value.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Observation setStatusElement(Enumeration<ObservationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the result value.
     */
    public ObservationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the result value.
     */
    public Observation setStatus(ObservationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ObservationStatus>(new ObservationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (A code that classifies the general type of observation being made.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public Observation addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #code} (Describes what was observed. Sometimes this is called the observation "name".)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Describes what was observed. Sometimes this is called the observation "name".)
     */
    public Observation setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Observation setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Observation setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.)
     */
    public Observation setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.)
     */
    public Observation setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #effective} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
      if (!(this.effective instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (DateTimeType) this.effective;
    }

    public boolean hasEffectiveDateTimeType() { 
      return this.effective instanceof DateTimeType;
    }

    /**
     * @return {@link #effective} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Period getEffectivePeriod() throws FHIRException { 
      if (!(this.effective instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (Period) this.effective;
    }

    public boolean hasEffectivePeriod() { 
      return this.effective instanceof Period;
    }

    public boolean hasEffective() { 
      return this.effective != null && !this.effective.isEmpty();
    }

    /**
     * @param value {@link #effective} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Observation setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #issued} (The date and time this observation was made available to providers, typically after the results have been reviewed and verified.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public InstantType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new InstantType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (The date and time this observation was made available to providers, typically after the results have been reviewed and verified.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Observation setIssuedElement(InstantType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return The date and time this observation was made available to providers, typically after the results have been reviewed and verified.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value The date and time this observation was made available to providers, typically after the results have been reviewed and verified.
     */
    public Observation setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new InstantType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #performer} (Who was responsible for asserting the observed value as "true".)
     */
    public List<Reference> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      return this.performer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setPerformer(List<Reference> thePerformer) { 
      this.performer = thePerformer;
      return this;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (Reference item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPerformer() { //3
      Reference t = new Reference();
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return t;
    }

    public Observation addPerformer(Reference t) { //3
      if (t == null)
        return this;
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #performer}, creating it if it does not already exist
     */
    public Reference getPerformerFirstRep() { 
      if (getPerformer().isEmpty()) {
        addPerformer();
      }
      return getPerformer().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getPerformerTarget() { 
      if (this.performerTarget == null)
        this.performerTarget = new ArrayList<Resource>();
      return this.performerTarget;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Type getValue() { 
      return this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Quantity getValueQuantity() throws FHIRException { 
      if (!(this.value instanceof Quantity))
        throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Quantity) this.value;
    }

    public boolean hasValueQuantity() { 
      return this.value instanceof Quantity;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public CodeableConcept getValueCodeableConcept() throws FHIRException { 
      if (!(this.value instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
      return (CodeableConcept) this.value;
    }

    public boolean hasValueCodeableConcept() { 
      return this.value instanceof CodeableConcept;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public StringType getValueStringType() throws FHIRException { 
      if (!(this.value instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (StringType) this.value;
    }

    public boolean hasValueStringType() { 
      return this.value instanceof StringType;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public BooleanType getValueBooleanType() throws FHIRException { 
      if (!(this.value instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (BooleanType) this.value;
    }

    public boolean hasValueBooleanType() { 
      return this.value instanceof BooleanType;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Range getValueRange() throws FHIRException { 
      if (!(this.value instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Range) this.value;
    }

    public boolean hasValueRange() { 
      return this.value instanceof Range;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Ratio getValueRatio() throws FHIRException { 
      if (!(this.value instanceof Ratio))
        throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Ratio) this.value;
    }

    public boolean hasValueRatio() { 
      return this.value instanceof Ratio;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public SampledData getValueSampledData() throws FHIRException { 
      if (!(this.value instanceof SampledData))
        throw new FHIRException("Type mismatch: the type SampledData was expected, but "+this.value.getClass().getName()+" was encountered");
      return (SampledData) this.value;
    }

    public boolean hasValueSampledData() { 
      return this.value instanceof SampledData;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Attachment getValueAttachment() throws FHIRException { 
      if (!(this.value instanceof Attachment))
        throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Attachment) this.value;
    }

    public boolean hasValueAttachment() { 
      return this.value instanceof Attachment;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public TimeType getValueTimeType() throws FHIRException { 
      if (!(this.value instanceof TimeType))
        throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (TimeType) this.value;
    }

    public boolean hasValueTimeType() { 
      return this.value instanceof TimeType;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public DateTimeType getValueDateTimeType() throws FHIRException { 
      if (!(this.value instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (DateTimeType) this.value;
    }

    public boolean hasValueDateTimeType() { 
      return this.value instanceof DateTimeType;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Period getValuePeriod() throws FHIRException { 
      if (!(this.value instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Period) this.value;
    }

    public boolean hasValuePeriod() { 
      return this.value instanceof Period;
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Observation setValue(Type value) { 
      this.value = value;
      return this;
    }

    /**
     * @return {@link #dataAbsentReason} (Provides a reason why the expected value in the element Observation.value[x] is missing.)
     */
    public CodeableConcept getDataAbsentReason() { 
      if (this.dataAbsentReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.dataAbsentReason");
        else if (Configuration.doAutoCreate())
          this.dataAbsentReason = new CodeableConcept(); // cc
      return this.dataAbsentReason;
    }

    public boolean hasDataAbsentReason() { 
      return this.dataAbsentReason != null && !this.dataAbsentReason.isEmpty();
    }

    /**
     * @param value {@link #dataAbsentReason} (Provides a reason why the expected value in the element Observation.value[x] is missing.)
     */
    public Observation setDataAbsentReason(CodeableConcept value) { 
      this.dataAbsentReason = value;
      return this;
    }

    /**
     * @return {@link #interpretation} (The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.)
     */
    public CodeableConcept getInterpretation() { 
      if (this.interpretation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.interpretation");
        else if (Configuration.doAutoCreate())
          this.interpretation = new CodeableConcept(); // cc
      return this.interpretation;
    }

    public boolean hasInterpretation() { 
      return this.interpretation != null && !this.interpretation.isEmpty();
    }

    /**
     * @param value {@link #interpretation} (The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.)
     */
    public Observation setInterpretation(CodeableConcept value) { 
      this.interpretation = value;
      return this;
    }

    /**
     * @return {@link #comment} (May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public Observation setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     */
    public Observation setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the observation was made (i.e. the target site).)
     */
    public CodeableConcept getBodySite() { 
      if (this.bodySite == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.bodySite");
        else if (Configuration.doAutoCreate())
          this.bodySite = new CodeableConcept(); // cc
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      return this.bodySite != null && !this.bodySite.isEmpty();
    }

    /**
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the observation was made (i.e. the target site).)
     */
    public Observation setBodySite(CodeableConcept value) { 
      this.bodySite = value;
      return this;
    }

    /**
     * @return {@link #method} (Indicates the mechanism used to perform the observation.)
     */
    public CodeableConcept getMethod() { 
      if (this.method == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.method");
        else if (Configuration.doAutoCreate())
          this.method = new CodeableConcept(); // cc
      return this.method;
    }

    public boolean hasMethod() { 
      return this.method != null && !this.method.isEmpty();
    }

    /**
     * @param value {@link #method} (Indicates the mechanism used to perform the observation.)
     */
    public Observation setMethod(CodeableConcept value) { 
      this.method = value;
      return this;
    }

    /**
     * @return {@link #specimen} (The specimen that was used when this observation was made.)
     */
    public Reference getSpecimen() { 
      if (this.specimen == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.specimen");
        else if (Configuration.doAutoCreate())
          this.specimen = new Reference(); // cc
      return this.specimen;
    }

    public boolean hasSpecimen() { 
      return this.specimen != null && !this.specimen.isEmpty();
    }

    /**
     * @param value {@link #specimen} (The specimen that was used when this observation was made.)
     */
    public Observation setSpecimen(Reference value) { 
      this.specimen = value;
      return this;
    }

    /**
     * @return {@link #specimen} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The specimen that was used when this observation was made.)
     */
    public Specimen getSpecimenTarget() { 
      if (this.specimenTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.specimen");
        else if (Configuration.doAutoCreate())
          this.specimenTarget = new Specimen(); // aa
      return this.specimenTarget;
    }

    /**
     * @param value {@link #specimen} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The specimen that was used when this observation was made.)
     */
    public Observation setSpecimenTarget(Specimen value) { 
      this.specimenTarget = value;
      return this;
    }

    /**
     * @return {@link #device} (The device used to generate the observation data.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.device");
        else if (Configuration.doAutoCreate())
          this.device = new Reference(); // cc
      return this.device;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The device used to generate the observation data.)
     */
    public Observation setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device used to generate the observation data.)
     */
    public Resource getDeviceTarget() { 
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device used to generate the observation data.)
     */
    public Observation setDeviceTarget(Resource value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #referenceRange} (Guidance on how to interpret the value by comparison to a normal or recommended range.)
     */
    public List<ObservationReferenceRangeComponent> getReferenceRange() { 
      if (this.referenceRange == null)
        this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
      return this.referenceRange;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setReferenceRange(List<ObservationReferenceRangeComponent> theReferenceRange) { 
      this.referenceRange = theReferenceRange;
      return this;
    }

    public boolean hasReferenceRange() { 
      if (this.referenceRange == null)
        return false;
      for (ObservationReferenceRangeComponent item : this.referenceRange)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ObservationReferenceRangeComponent addReferenceRange() { //3
      ObservationReferenceRangeComponent t = new ObservationReferenceRangeComponent();
      if (this.referenceRange == null)
        this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
      this.referenceRange.add(t);
      return t;
    }

    public Observation addReferenceRange(ObservationReferenceRangeComponent t) { //3
      if (t == null)
        return this;
      if (this.referenceRange == null)
        this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
      this.referenceRange.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #referenceRange}, creating it if it does not already exist
     */
    public ObservationReferenceRangeComponent getReferenceRangeFirstRep() { 
      if (getReferenceRange().isEmpty()) {
        addReferenceRange();
      }
      return getReferenceRange().get(0);
    }

    /**
     * @return {@link #related} (A  reference to another resource (usually another Observation) whose relationship is defined by the relationship type code.)
     */
    public List<ObservationRelatedComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<ObservationRelatedComponent>();
      return this.related;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setRelated(List<ObservationRelatedComponent> theRelated) { 
      this.related = theRelated;
      return this;
    }

    public boolean hasRelated() { 
      if (this.related == null)
        return false;
      for (ObservationRelatedComponent item : this.related)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ObservationRelatedComponent addRelated() { //3
      ObservationRelatedComponent t = new ObservationRelatedComponent();
      if (this.related == null)
        this.related = new ArrayList<ObservationRelatedComponent>();
      this.related.add(t);
      return t;
    }

    public Observation addRelated(ObservationRelatedComponent t) { //3
      if (t == null)
        return this;
      if (this.related == null)
        this.related = new ArrayList<ObservationRelatedComponent>();
      this.related.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #related}, creating it if it does not already exist
     */
    public ObservationRelatedComponent getRelatedFirstRep() { 
      if (getRelated().isEmpty()) {
        addRelated();
      }
      return getRelated().get(0);
    }

    /**
     * @return {@link #component} (Some observations have multiple component observations.  These component observations are expressed as separate code value pairs that share the same attributes.  Examples include systolic and diastolic component observations for blood pressure measurement and multiple component observations for genetics observations.)
     */
    public List<ObservationComponentComponent> getComponent() { 
      if (this.component == null)
        this.component = new ArrayList<ObservationComponentComponent>();
      return this.component;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Observation setComponent(List<ObservationComponentComponent> theComponent) { 
      this.component = theComponent;
      return this;
    }

    public boolean hasComponent() { 
      if (this.component == null)
        return false;
      for (ObservationComponentComponent item : this.component)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ObservationComponentComponent addComponent() { //3
      ObservationComponentComponent t = new ObservationComponentComponent();
      if (this.component == null)
        this.component = new ArrayList<ObservationComponentComponent>();
      this.component.add(t);
      return t;
    }

    public Observation addComponent(ObservationComponentComponent t) { //3
      if (t == null)
        return this;
      if (this.component == null)
        this.component = new ArrayList<ObservationComponentComponent>();
      this.component.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #component}, creating it if it does not already exist
     */
    public ObservationComponentComponent getComponentFirstRep() { 
      if (getComponent().isEmpty()) {
        addComponent();
      }
      return getComponent().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier assigned to this observation.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("basedOn", "Reference(CarePlan|DeviceRequest|ImmunizationRecommendation|MedicationRequest|NutritionOrder|ProcedureRequest|ReferralRequest)", "A plan, proposal or order that is fulfilled in whole or in part by this event.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("status", "code", "The status of the result value.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the general type of observation being made.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "Describes what was observed. Sometimes this is called the observation \"name\".", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus, donor,  other observer (for example a relative or EMT), or any observation made about the subject.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("issued", "instant", "The date and time this observation was made available to providers, typically after the results have been reviewed and verified.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|RelatedPerson)", "Who was responsible for asserting the observed value as \"true\".", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("value[x]", "Quantity|CodeableConcept|string|boolean|Range|Ratio|SampledData|Attachment|time|dateTime|Period", "The information determined as a result of making the observation, if the information has a simple value.", 0, java.lang.Integer.MAX_VALUE, value));
        childrenList.add(new Property("dataAbsentReason", "CodeableConcept", "Provides a reason why the expected value in the element Observation.value[x] is missing.", 0, java.lang.Integer.MAX_VALUE, dataAbsentReason));
        childrenList.add(new Property("interpretation", "CodeableConcept", "The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.", 0, java.lang.Integer.MAX_VALUE, interpretation));
        childrenList.add(new Property("comment", "string", "May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Indicates the site on the subject's body where the observation was made (i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("method", "CodeableConcept", "Indicates the mechanism used to perform the observation.", 0, java.lang.Integer.MAX_VALUE, method));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "The specimen that was used when this observation was made.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("device", "Reference(Device|DeviceMetric)", "The device used to generate the observation data.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("referenceRange", "", "Guidance on how to interpret the value by comparison to a normal or recommended range.", 0, java.lang.Integer.MAX_VALUE, referenceRange));
        childrenList.add(new Property("related", "", "A  reference to another resource (usually another Observation) whose relationship is defined by the relationship type code.", 0, java.lang.Integer.MAX_VALUE, related));
        childrenList.add(new Property("component", "", "Some observations have multiple component observations.  These component observations are expressed as separate code value pairs that share the same attributes.  Examples include systolic and diastolic component observations for blood pressure measurement and multiple component observations for genetics observations.", 0, java.lang.Integer.MAX_VALUE, component));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ObservationStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // InstantType
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // Reference
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        case 1034315687: /*dataAbsentReason*/ return this.dataAbsentReason == null ? new Base[0] : new Base[] {this.dataAbsentReason}; // CodeableConcept
        case -297950712: /*interpretation*/ return this.interpretation == null ? new Base[0] : new Base[] {this.interpretation}; // CodeableConcept
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case -2132868344: /*specimen*/ return this.specimen == null ? new Base[0] : new Base[] {this.specimen}; // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Reference
        case -1912545102: /*referenceRange*/ return this.referenceRange == null ? new Base[0] : this.referenceRange.toArray(new Base[this.referenceRange.size()]); // ObservationReferenceRangeComponent
        case 1090493483: /*related*/ return this.related == null ? new Base[0] : this.related.toArray(new Base[this.related.size()]); // ObservationRelatedComponent
        case -1399907075: /*component*/ return this.component == null ? new Base[0] : this.component.toArray(new Base[this.component.size()]); // ObservationComponentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new ObservationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ObservationStatus>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        case -1179159893: // issued
          this.issued = castToInstant(value); // InstantType
          return value;
        case 481140686: // performer
          this.getPerformer().add(castToReference(value)); // Reference
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        case 1034315687: // dataAbsentReason
          this.dataAbsentReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -297950712: // interpretation
          this.interpretation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case 1702620169: // bodySite
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2132868344: // specimen
          this.specimen = castToReference(value); // Reference
          return value;
        case -1335157162: // device
          this.device = castToReference(value); // Reference
          return value;
        case -1912545102: // referenceRange
          this.getReferenceRange().add((ObservationReferenceRangeComponent) value); // ObservationReferenceRangeComponent
          return value;
        case 1090493483: // related
          this.getRelated().add((ObservationRelatedComponent) value); // ObservationRelatedComponent
          return value;
        case -1399907075: // component
          this.getComponent().add((ObservationComponentComponent) value); // ObservationComponentComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new ObservationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ObservationStatus>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else if (name.equals("issued")) {
          this.issued = castToInstant(value); // InstantType
        } else if (name.equals("performer")) {
          this.getPerformer().add(castToReference(value));
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else if (name.equals("dataAbsentReason")) {
          this.dataAbsentReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("interpretation")) {
          this.interpretation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("bodySite")) {
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("specimen")) {
          this.specimen = castToReference(value); // Reference
        } else if (name.equals("device")) {
          this.device = castToReference(value); // Reference
        } else if (name.equals("referenceRange")) {
          this.getReferenceRange().add((ObservationReferenceRangeComponent) value);
        } else if (name.equals("related")) {
          this.getRelated().add((ObservationRelatedComponent) value);
        } else if (name.equals("component")) {
          this.getComponent().add((ObservationComponentComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -332612366:  return addBasedOn(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return addCategory(); 
        case 3059181:  return getCode(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        case -1179159893:  return getIssuedElement();
        case 481140686:  return addPerformer(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        case 1034315687:  return getDataAbsentReason(); 
        case -297950712:  return getInterpretation(); 
        case 950398559:  return getCommentElement();
        case 1702620169:  return getBodySite(); 
        case -1077554975:  return getMethod(); 
        case -2132868344:  return getSpecimen(); 
        case -1335157162:  return getDevice(); 
        case -1912545102:  return addReferenceRange(); 
        case 1090493483:  return addRelated(); 
        case -1399907075:  return addComponent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        case -1179159893: /*issued*/ return new String[] {"instant"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case 111972721: /*value*/ return new String[] {"Quantity", "CodeableConcept", "string", "boolean", "Range", "Ratio", "SampledData", "Attachment", "time", "dateTime", "Period"};
        case 1034315687: /*dataAbsentReason*/ return new String[] {"CodeableConcept"};
        case -297950712: /*interpretation*/ return new String[] {"CodeableConcept"};
        case 950398559: /*comment*/ return new String[] {"string"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case -2132868344: /*specimen*/ return new String[] {"Reference"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case -1912545102: /*referenceRange*/ return new String[] {};
        case 1090493483: /*related*/ return new String[] {};
        case -1399907075: /*component*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Observation.status");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Observation.issued");
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("dataAbsentReason")) {
          this.dataAbsentReason = new CodeableConcept();
          return this.dataAbsentReason;
        }
        else if (name.equals("interpretation")) {
          this.interpretation = new CodeableConcept();
          return this.interpretation;
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type Observation.comment");
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("specimen")) {
          this.specimen = new Reference();
          return this.specimen;
        }
        else if (name.equals("device")) {
          this.device = new Reference();
          return this.device;
        }
        else if (name.equals("referenceRange")) {
          return addReferenceRange();
        }
        else if (name.equals("related")) {
          return addRelated();
        }
        else if (name.equals("component")) {
          return addComponent();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Observation";

  }

      public Observation copy() {
        Observation dst = new Observation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.issued = issued == null ? null : issued.copy();
        if (performer != null) {
          dst.performer = new ArrayList<Reference>();
          for (Reference i : performer)
            dst.performer.add(i.copy());
        };
        dst.value = value == null ? null : value.copy();
        dst.dataAbsentReason = dataAbsentReason == null ? null : dataAbsentReason.copy();
        dst.interpretation = interpretation == null ? null : interpretation.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.method = method == null ? null : method.copy();
        dst.specimen = specimen == null ? null : specimen.copy();
        dst.device = device == null ? null : device.copy();
        if (referenceRange != null) {
          dst.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
          for (ObservationReferenceRangeComponent i : referenceRange)
            dst.referenceRange.add(i.copy());
        };
        if (related != null) {
          dst.related = new ArrayList<ObservationRelatedComponent>();
          for (ObservationRelatedComponent i : related)
            dst.related.add(i.copy());
        };
        if (component != null) {
          dst.component = new ArrayList<ObservationComponentComponent>();
          for (ObservationComponentComponent i : component)
            dst.component.add(i.copy());
        };
        return dst;
      }

      protected Observation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Observation))
          return false;
        Observation o = (Observation) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(status, o.status, true)
           && compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true)
           && compareDeep(context, o.context, true) && compareDeep(effective, o.effective, true) && compareDeep(issued, o.issued, true)
           && compareDeep(performer, o.performer, true) && compareDeep(value, o.value, true) && compareDeep(dataAbsentReason, o.dataAbsentReason, true)
           && compareDeep(interpretation, o.interpretation, true) && compareDeep(comment, o.comment, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(method, o.method, true) && compareDeep(specimen, o.specimen, true)
           && compareDeep(device, o.device, true) && compareDeep(referenceRange, o.referenceRange, true) && compareDeep(related, o.related, true)
           && compareDeep(component, o.component, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Observation))
          return false;
        Observation o = (Observation) other;
        return compareValues(status, o.status, true) && compareValues(issued, o.issued, true) && compareValues(comment, o.comment, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, status
          , category, code, subject, context, effective, issued, performer, value, dataAbsentReason
          , interpretation, comment, bodySite, method, specimen, device, referenceRange
          , related, component);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Observation;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Observation.effective[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Observation.effective", description="Obtained date/time. If the obtained element is a period, a date that falls in the period", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Observation.effective[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>combo-data-absent-reason</b>
   * <p>
   * Description: <b>The reason why the expected value in the element Observation.value[x] or Observation.component.value[x] is missing.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.dataAbsentReason, Observation.component.dataAbsentReason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="combo-data-absent-reason", path="Observation.dataAbsentReason | Observation.component.dataAbsentReason", description="The reason why the expected value in the element Observation.value[x] or Observation.component.value[x] is missing.", type="token" )
  public static final String SP_COMBO_DATA_ABSENT_REASON = "combo-data-absent-reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>combo-data-absent-reason</b>
   * <p>
   * Description: <b>The reason why the expected value in the element Observation.value[x] or Observation.component.value[x] is missing.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.dataAbsentReason, Observation.component.dataAbsentReason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMBO_DATA_ABSENT_REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMBO_DATA_ABSENT_REASON);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>The code of the observation type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Observation.code", description="The code of the observation type", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>The code of the observation type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>combo-code-value-quantity</b>
   * <p>
   * Description: <b>Code and quantity value parameter pair, including in components</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="combo-code-value-quantity", path="Observation | Observation.component", description="Code and quantity value parameter pair, including in components", type="composite", compositeOf={"combo-code", "combo-value-quantity"} )
  public static final String SP_COMBO_CODE_VALUE_QUANTITY = "combo-code-value-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>combo-code-value-quantity</b>
   * <p>
   * Description: <b>Code and quantity value parameter pair, including in components</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> COMBO_CODE_VALUE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_COMBO_CODE_VALUE_QUANTITY);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Observation.subject", description="The subject that the observation is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Device.class, Group.class, Location.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Observation:subject").toLocked();

 /**
   * Search parameter: <b>component-data-absent-reason</b>
   * <p>
   * Description: <b>The reason why the expected value in the element Observation.component.value[x] is missing.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.component.dataAbsentReason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="component-data-absent-reason", path="Observation.component.dataAbsentReason", description="The reason why the expected value in the element Observation.component.value[x] is missing.", type="token" )
  public static final String SP_COMPONENT_DATA_ABSENT_REASON = "component-data-absent-reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component-data-absent-reason</b>
   * <p>
   * Description: <b>The reason why the expected value in the element Observation.component.value[x] is missing.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.component.dataAbsentReason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMPONENT_DATA_ABSENT_REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMPONENT_DATA_ABSENT_REASON);

 /**
   * Search parameter: <b>value-concept</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a CodeableConcept</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="value-concept", path="Observation.value.as(CodeableConcept)", description="The value of the observation, if the value is a CodeableConcept", type="token" )
  public static final String SP_VALUE_CONCEPT = "value-concept";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>value-concept</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a CodeableConcept</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VALUE_CONCEPT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VALUE_CONCEPT);

 /**
   * Search parameter: <b>value-date</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a date or period of time</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Observation.valueDateTime, Observation.valuePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="value-date", path="Observation.value.as(DateTime) | Observation.value.as(Period)", description="The value of the observation, if the value is a date or period of time", type="date" )
  public static final String SP_VALUE_DATE = "value-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>value-date</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a date or period of time</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Observation.valueDateTime, Observation.valuePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam VALUE_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_VALUE_DATE);

 /**
   * Search parameter: <b>code-value-string</b>
   * <p>
   * Description: <b>Code and string value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="code-value-string", path="Observation", description="Code and string value parameter pair", type="composite", compositeOf={"code", "value-string"} )
  public static final String SP_CODE_VALUE_STRING = "code-value-string";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code-value-string</b>
   * <p>
   * Description: <b>Code and string value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.StringClientParam> CODE_VALUE_STRING = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.StringClientParam>(SP_CODE_VALUE_STRING);

 /**
   * Search parameter: <b>component-code-value-quantity</b>
   * <p>
   * Description: <b>Component code and component quantity value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="component-code-value-quantity", path="Observation.component", description="Component code and component quantity value parameter pair", type="composite", compositeOf={"component-code", "component-value-quantity"} )
  public static final String SP_COMPONENT_CODE_VALUE_QUANTITY = "component-code-value-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component-code-value-quantity</b>
   * <p>
   * Description: <b>Component code and component quantity value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> COMPONENT_CODE_VALUE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_COMPONENT_CODE_VALUE_QUANTITY);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Reference to the test or procedure request.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="Observation.basedOn", description="Reference to the test or procedure request.", type="reference", target={CarePlan.class, DeviceRequest.class, ImmunizationRecommendation.class, MedicationRequest.class, NutritionOrder.class, ProcedureRequest.class, ReferralRequest.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Reference to the test or procedure request.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("Observation:based-on").toLocked();

 /**
   * Search parameter: <b>related</b>
   * <p>
   * Description: <b>Related Observations - search on related-type and related-target together</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="related", path="Observation.related", description="Related Observations - search on related-type and related-target together", type="composite", compositeOf={"related-target", "related-type"} )
  public static final String SP_RELATED = "related";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related</b>
   * <p>
   * Description: <b>Related Observations - search on related-type and related-target together</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.ReferenceClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> RELATED = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.ReferenceClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_RELATED);

 /**
   * Search parameter: <b>code-value-date</b>
   * <p>
   * Description: <b>Code and date/time value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="code-value-date", path="Observation", description="Code and date/time value parameter pair", type="composite", compositeOf={"code", "value-date"} )
  public static final String SP_CODE_VALUE_DATE = "code-value-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code-value-date</b>
   * <p>
   * Description: <b>Code and date/time value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.DateClientParam> CODE_VALUE_DATE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.DateClientParam>(SP_CODE_VALUE_DATE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about (if patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Observation.subject", description="The subject that the observation is about (if patient)", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about (if patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Observation:patient").toLocked();

 /**
   * Search parameter: <b>specimen</b>
   * <p>
   * Description: <b>Specimen used for this observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.specimen</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specimen", path="Observation.specimen", description="Specimen used for this observation", type="reference", target={Specimen.class } )
  public static final String SP_SPECIMEN = "specimen";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
   * <p>
   * Description: <b>Specimen used for this observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.specimen</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SPECIMEN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SPECIMEN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:specimen</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SPECIMEN = new ca.uhn.fhir.model.api.Include("Observation:specimen").toLocked();

 /**
   * Search parameter: <b>component-code</b>
   * <p>
   * Description: <b>The component code of the observation type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.component.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="component-code", path="Observation.component.code", description="The component code of the observation type", type="token" )
  public static final String SP_COMPONENT_CODE = "component-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component-code</b>
   * <p>
   * Description: <b>The component code of the observation type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.component.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMPONENT_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMPONENT_CODE);

 /**
   * Search parameter: <b>code-value-quantity</b>
   * <p>
   * Description: <b>Code and quantity value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="code-value-quantity", path="Observation", description="Code and quantity value parameter pair", type="composite", compositeOf={"code", "value-quantity"} )
  public static final String SP_CODE_VALUE_QUANTITY = "code-value-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code-value-quantity</b>
   * <p>
   * Description: <b>Code and quantity value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> CODE_VALUE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_CODE_VALUE_QUANTITY);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Healthcare event  (Episode-of-care or Encounter) related to the observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="Observation.context", description="Healthcare event  (Episode-of-care or Encounter) related to the observation", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Healthcare event  (Episode-of-care or Encounter) related to the observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("Observation:context").toLocked();

 /**
   * Search parameter: <b>combo-code-value-concept</b>
   * <p>
   * Description: <b>Code and coded value parameter pair, including in components</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="combo-code-value-concept", path="Observation | Observation.component", description="Code and coded value parameter pair, including in components", type="composite", compositeOf={"combo-code", "combo-value-concept"} )
  public static final String SP_COMBO_CODE_VALUE_CONCEPT = "combo-code-value-concept";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>combo-code-value-concept</b>
   * <p>
   * Description: <b>Code and coded value parameter pair, including in components</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> COMBO_CODE_VALUE_CONCEPT = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_COMBO_CODE_VALUE_CONCEPT);

 /**
   * Search parameter: <b>value-string</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a string, and also searches in CodeableConcept.text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Observation.valueString</b><br>
   * </p>
   */
  @SearchParamDefinition(name="value-string", path="Observation.value.as(String)", description="The value of the observation, if the value is a string, and also searches in CodeableConcept.text", type="string" )
  public static final String SP_VALUE_STRING = "value-string";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>value-string</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a string, and also searches in CodeableConcept.text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Observation.valueString</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VALUE_STRING = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VALUE_STRING);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique id for a particular observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Observation.identifier", description="The unique id for a particular observation", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique id for a particular observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Who performed the observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="Observation.performer", description="Who performed the observation", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Who performed the observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("Observation:performer").toLocked();

 /**
   * Search parameter: <b>combo-code</b>
   * <p>
   * Description: <b>The code of the observation type or component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.code, Observation.component.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="combo-code", path="Observation.code | Observation.component.code", description="The code of the observation type or component type", type="token" )
  public static final String SP_COMBO_CODE = "combo-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>combo-code</b>
   * <p>
   * Description: <b>The code of the observation type or component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.code, Observation.component.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMBO_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMBO_CODE);

 /**
   * Search parameter: <b>method</b>
   * <p>
   * Description: <b>The method used for the observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.method</b><br>
   * </p>
   */
  @SearchParamDefinition(name="method", path="Observation.method", description="The method used for the observation", type="token" )
  public static final String SP_METHOD = "method";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>method</b>
   * <p>
   * Description: <b>The method used for the observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.method</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam METHOD = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_METHOD);

 /**
   * Search parameter: <b>value-quantity</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Observation.valueQuantity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="value-quantity", path="Observation.value.as(Quantity)", description="The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)", type="quantity" )
  public static final String SP_VALUE_QUANTITY = "value-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>value-quantity</b>
   * <p>
   * Description: <b>The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Observation.valueQuantity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam VALUE_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_VALUE_QUANTITY);

 /**
   * Search parameter: <b>component-value-quantity</b>
   * <p>
   * Description: <b>The value of the component observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Observation.component.valueQuantity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="component-value-quantity", path="Observation.component.value.as(Quantity)", description="The value of the component observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)", type="quantity" )
  public static final String SP_COMPONENT_VALUE_QUANTITY = "component-value-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component-value-quantity</b>
   * <p>
   * Description: <b>The value of the component observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Observation.component.valueQuantity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam COMPONENT_VALUE_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_COMPONENT_VALUE_QUANTITY);

 /**
   * Search parameter: <b>data-absent-reason</b>
   * <p>
   * Description: <b>The reason why the expected value in the element Observation.value[x] is missing.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.dataAbsentReason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="data-absent-reason", path="Observation.dataAbsentReason", description="The reason why the expected value in the element Observation.value[x] is missing.", type="token" )
  public static final String SP_DATA_ABSENT_REASON = "data-absent-reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>data-absent-reason</b>
   * <p>
   * Description: <b>The reason why the expected value in the element Observation.value[x] is missing.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.dataAbsentReason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam DATA_ABSENT_REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_DATA_ABSENT_REASON);

 /**
   * Search parameter: <b>combo-value-quantity</b>
   * <p>
   * Description: <b>The value or component value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Observation.valueQuantity, Observation.component.valueQuantity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="combo-value-quantity", path="Observation.value.as(Quantity) | Observation.component.value.as(Quantity)", description="The value or component value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)", type="quantity" )
  public static final String SP_COMBO_VALUE_QUANTITY = "combo-value-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>combo-value-quantity</b>
   * <p>
   * Description: <b>The value or component value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Observation.valueQuantity, Observation.component.valueQuantity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam COMBO_VALUE_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_COMBO_VALUE_QUANTITY);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter related to the observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Observation.context", description="Encounter related to the observation", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter related to the observation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Observation:encounter").toLocked();

 /**
   * Search parameter: <b>related-type</b>
   * <p>
   * Description: <b>has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.related.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="related-type", path="Observation.related.type", description="has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by", type="token" )
  public static final String SP_RELATED_TYPE = "related-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related-type</b>
   * <p>
   * Description: <b>has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.related.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RELATED_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RELATED_TYPE);

 /**
   * Search parameter: <b>related-target</b>
   * <p>
   * Description: <b>Resource that is related to this one</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.related.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="related-target", path="Observation.related.target", description="Resource that is related to this one", type="reference", target={Observation.class, QuestionnaireResponse.class, Sequence.class } )
  public static final String SP_RELATED_TARGET = "related-target";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related-target</b>
   * <p>
   * Description: <b>Resource that is related to this one</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.related.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RELATED_TARGET = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RELATED_TARGET);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:related-target</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RELATED_TARGET = new ca.uhn.fhir.model.api.Include("Observation:related-target").toLocked();

 /**
   * Search parameter: <b>code-value-concept</b>
   * <p>
   * Description: <b>Code and coded value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="code-value-concept", path="Observation", description="Code and coded value parameter pair", type="composite", compositeOf={"code", "value-concept"} )
  public static final String SP_CODE_VALUE_CONCEPT = "code-value-concept";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code-value-concept</b>
   * <p>
   * Description: <b>Code and coded value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> CODE_VALUE_CONCEPT = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_CODE_VALUE_CONCEPT);

 /**
   * Search parameter: <b>component-code-value-concept</b>
   * <p>
   * Description: <b>Component code and component coded value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="component-code-value-concept", path="Observation.component", description="Component code and component coded value parameter pair", type="composite", compositeOf={"component-code", "component-value-concept"} )
  public static final String SP_COMPONENT_CODE_VALUE_CONCEPT = "component-code-value-concept";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component-code-value-concept</b>
   * <p>
   * Description: <b>Component code and component coded value parameter pair</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> COMPONENT_CODE_VALUE_CONCEPT = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_COMPONENT_CODE_VALUE_CONCEPT);

 /**
   * Search parameter: <b>component-value-concept</b>
   * <p>
   * Description: <b>The value of the component observation, if the value is a CodeableConcept</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.component.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="component-value-concept", path="Observation.component.value.as(CodeableConcept)", description="The value of the component observation, if the value is a CodeableConcept", type="token" )
  public static final String SP_COMPONENT_VALUE_CONCEPT = "component-value-concept";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component-value-concept</b>
   * <p>
   * Description: <b>The value of the component observation, if the value is a CodeableConcept</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.component.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMPONENT_VALUE_CONCEPT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMPONENT_VALUE_CONCEPT);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>The classification of the type of observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Observation.category", description="The classification of the type of observation", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>The classification of the type of observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>device</b>
   * <p>
   * Description: <b>The Device that generated the observation data.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.device</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device", path="Observation.device", description="The Device that generated the observation data.", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class, DeviceMetric.class } )
  public static final String SP_DEVICE = "device";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device</b>
   * <p>
   * Description: <b>The Device that generated the observation data.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Observation.device</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Observation:device</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEVICE = new ca.uhn.fhir.model.api.Include("Observation:device").toLocked();

 /**
   * Search parameter: <b>combo-value-concept</b>
   * <p>
   * Description: <b>The value or component value of the observation, if the value is a CodeableConcept</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.valueCodeableConcept, Observation.component.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="combo-value-concept", path="Observation.value.as(CodeableConcept) | Observation.component.value.as(CodeableConcept)", description="The value or component value of the observation, if the value is a CodeableConcept", type="token" )
  public static final String SP_COMBO_VALUE_CONCEPT = "combo-value-concept";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>combo-value-concept</b>
   * <p>
   * Description: <b>The value or component value of the observation, if the value is a CodeableConcept</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.valueCodeableConcept, Observation.component.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMBO_VALUE_CONCEPT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMBO_VALUE_CONCEPT);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Observation.status", description="The status of the observation", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the observation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Observation.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

