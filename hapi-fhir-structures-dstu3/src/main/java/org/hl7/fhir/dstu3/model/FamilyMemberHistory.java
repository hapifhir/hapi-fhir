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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Significant health events and conditions for a person related to the patient relevant in the context of care for the patient.
 */
@ResourceDef(name="FamilyMemberHistory", profile="http://hl7.org/fhir/Profile/FamilyMemberHistory")
public class FamilyMemberHistory extends DomainResource {

    public enum FamilyHistoryStatus {
        /**
         * Some health information is known and captured, but not complete - see notes for details.
         */
        PARTIAL, 
        /**
         * All available related health information is captured as of the date (and possibly time) when the family member history was taken.
         */
        COMPLETED, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * Health information for this individual is unavailable/unknown.
         */
        HEALTHUNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static FamilyHistoryStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("partial".equals(codeString))
          return PARTIAL;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("health-unknown".equals(codeString))
          return HEALTHUNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown FamilyHistoryStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PARTIAL: return "partial";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case HEALTHUNKNOWN: return "health-unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PARTIAL: return "http://hl7.org/fhir/history-status";
            case COMPLETED: return "http://hl7.org/fhir/history-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/history-status";
            case HEALTHUNKNOWN: return "http://hl7.org/fhir/history-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PARTIAL: return "Some health information is known and captured, but not complete - see notes for details.";
            case COMPLETED: return "All available related health information is captured as of the date (and possibly time) when the family member history was taken.";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            case HEALTHUNKNOWN: return "Health information for this individual is unavailable/unknown.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PARTIAL: return "Partial";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in error";
            case HEALTHUNKNOWN: return "Health unknown";
            default: return "?";
          }
        }
    }

  public static class FamilyHistoryStatusEnumFactory implements EnumFactory<FamilyHistoryStatus> {
    public FamilyHistoryStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("partial".equals(codeString))
          return FamilyHistoryStatus.PARTIAL;
        if ("completed".equals(codeString))
          return FamilyHistoryStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return FamilyHistoryStatus.ENTEREDINERROR;
        if ("health-unknown".equals(codeString))
          return FamilyHistoryStatus.HEALTHUNKNOWN;
        throw new IllegalArgumentException("Unknown FamilyHistoryStatus code '"+codeString+"'");
        }
        public Enumeration<FamilyHistoryStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<FamilyHistoryStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("partial".equals(codeString))
          return new Enumeration<FamilyHistoryStatus>(this, FamilyHistoryStatus.PARTIAL);
        if ("completed".equals(codeString))
          return new Enumeration<FamilyHistoryStatus>(this, FamilyHistoryStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<FamilyHistoryStatus>(this, FamilyHistoryStatus.ENTEREDINERROR);
        if ("health-unknown".equals(codeString))
          return new Enumeration<FamilyHistoryStatus>(this, FamilyHistoryStatus.HEALTHUNKNOWN);
        throw new FHIRException("Unknown FamilyHistoryStatus code '"+codeString+"'");
        }
    public String toCode(FamilyHistoryStatus code) {
      if (code == FamilyHistoryStatus.PARTIAL)
        return "partial";
      if (code == FamilyHistoryStatus.COMPLETED)
        return "completed";
      if (code == FamilyHistoryStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == FamilyHistoryStatus.HEALTHUNKNOWN)
        return "health-unknown";
      return "?";
      }
    public String toSystem(FamilyHistoryStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class FamilyMemberHistoryConditionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Condition suffered by relation", formalDefinition="The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
        protected CodeableConcept code;

        /**
         * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
         */
        @Child(name = "outcome", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="deceased | permanent disability | etc.", formalDefinition="Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-outcome")
        protected CodeableConcept outcome;

        /**
         * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
         */
        @Child(name = "onset", type = {Age.class, Range.class, Period.class, StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When condition first manifested", formalDefinition="Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence." )
        protected Type onset;

        /**
         * An area where general notes can be placed about this specific condition.
         */
        @Child(name = "note", type = {Annotation.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Extra information about condition", formalDefinition="An area where general notes can be placed about this specific condition." )
        protected List<Annotation> note;

        private static final long serialVersionUID = 598309281L;

    /**
     * Constructor
     */
      public FamilyMemberHistoryConditionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public FamilyMemberHistoryConditionComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create FamilyMemberHistoryConditionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system.)
         */
        public FamilyMemberHistoryConditionComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #outcome} (Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.)
         */
        public CodeableConcept getOutcome() { 
          if (this.outcome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create FamilyMemberHistoryConditionComponent.outcome");
            else if (Configuration.doAutoCreate())
              this.outcome = new CodeableConcept(); // cc
          return this.outcome;
        }

        public boolean hasOutcome() { 
          return this.outcome != null && !this.outcome.isEmpty();
        }

        /**
         * @param value {@link #outcome} (Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.)
         */
        public FamilyMemberHistoryConditionComponent setOutcome(CodeableConcept value) { 
          this.outcome = value;
          return this;
        }

        /**
         * @return {@link #onset} (Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.)
         */
        public Type getOnset() { 
          return this.onset;
        }

        /**
         * @return {@link #onset} (Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.)
         */
        public Age getOnsetAge() throws FHIRException { 
          if (!(this.onset instanceof Age))
            throw new FHIRException("Type mismatch: the type Age was expected, but "+this.onset.getClass().getName()+" was encountered");
          return (Age) this.onset;
        }

        public boolean hasOnsetAge() { 
          return this.onset instanceof Age;
        }

        /**
         * @return {@link #onset} (Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.)
         */
        public Range getOnsetRange() throws FHIRException { 
          if (!(this.onset instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.onset.getClass().getName()+" was encountered");
          return (Range) this.onset;
        }

        public boolean hasOnsetRange() { 
          return this.onset instanceof Range;
        }

        /**
         * @return {@link #onset} (Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.)
         */
        public Period getOnsetPeriod() throws FHIRException { 
          if (!(this.onset instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.onset.getClass().getName()+" was encountered");
          return (Period) this.onset;
        }

        public boolean hasOnsetPeriod() { 
          return this.onset instanceof Period;
        }

        /**
         * @return {@link #onset} (Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.)
         */
        public StringType getOnsetStringType() throws FHIRException { 
          if (!(this.onset instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.onset.getClass().getName()+" was encountered");
          return (StringType) this.onset;
        }

        public boolean hasOnsetStringType() { 
          return this.onset instanceof StringType;
        }

        public boolean hasOnset() { 
          return this.onset != null && !this.onset.isEmpty();
        }

        /**
         * @param value {@link #onset} (Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.)
         */
        public FamilyMemberHistoryConditionComponent setOnset(Type value) { 
          this.onset = value;
          return this;
        }

        /**
         * @return {@link #note} (An area where general notes can be placed about this specific condition.)
         */
        public List<Annotation> getNote() { 
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          return this.note;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public FamilyMemberHistoryConditionComponent setNote(List<Annotation> theNote) { 
          this.note = theNote;
          return this;
        }

        public boolean hasNote() { 
          if (this.note == null)
            return false;
          for (Annotation item : this.note)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Annotation addNote() { //3
          Annotation t = new Annotation();
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          this.note.add(t);
          return t;
        }

        public FamilyMemberHistoryConditionComponent addNote(Annotation t) { //3
          if (t == null)
            return this;
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          this.note.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
         */
        public Annotation getNoteFirstRep() { 
          if (getNote().isEmpty()) {
            addNote();
          }
          return getNote().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("outcome", "CodeableConcept", "Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.", 0, java.lang.Integer.MAX_VALUE, outcome));
          childrenList.add(new Property("onset[x]", "Age|Range|Period|string", "Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.", 0, java.lang.Integer.MAX_VALUE, onset));
          childrenList.add(new Property("note", "Annotation", "An area where general notes can be placed about this specific condition.", 0, java.lang.Integer.MAX_VALUE, note));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case 105901603: /*onset*/ return this.onset == null ? new Base[0] : new Base[] {this.onset}; // Type
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 105901603: // onset
          this.onset = castToType(value); // Type
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("onset[x]")) {
          this.onset = castToType(value); // Type
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -1106507950:  return getOutcome(); 
        case -1886216323:  return getOnset(); 
        case 105901603:  return getOnset(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case 105901603: /*onset*/ return new String[] {"Age", "Range", "Period", "string"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("onsetAge")) {
          this.onset = new Age();
          return this.onset;
        }
        else if (name.equals("onsetRange")) {
          this.onset = new Range();
          return this.onset;
        }
        else if (name.equals("onsetPeriod")) {
          this.onset = new Period();
          return this.onset;
        }
        else if (name.equals("onsetString")) {
          this.onset = new StringType();
          return this.onset;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

      public FamilyMemberHistoryConditionComponent copy() {
        FamilyMemberHistoryConditionComponent dst = new FamilyMemberHistoryConditionComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.onset = onset == null ? null : onset.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof FamilyMemberHistoryConditionComponent))
          return false;
        FamilyMemberHistoryConditionComponent o = (FamilyMemberHistoryConditionComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(outcome, o.outcome, true) && compareDeep(onset, o.onset, true)
           && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof FamilyMemberHistoryConditionComponent))
          return false;
        FamilyMemberHistoryConditionComponent o = (FamilyMemberHistoryConditionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, outcome, onset, note
          );
      }

  public String fhirType() {
    return "FamilyMemberHistory.condition";

  }

  }

    /**
     * This records identifiers associated with this family member history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Id(s) for this record", formalDefinition="This records identifiers associated with this family member history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * A protocol or questionnaire that was adhered to in whole or in part by this event.
     */
    @Child(name = "definition", type = {PlanDefinition.class, Questionnaire.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Instantiates protocol or definition", formalDefinition="A protocol or questionnaire that was adhered to in whole or in part by this event." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (A protocol or questionnaire that was adhered to in whole or in part by this event.)
     */
    protected List<Resource> definitionTarget;


    /**
     * A code specifying the status of the record of the family history of a specific family member.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="partial | completed | entered-in-error | health-unknown", formalDefinition="A code specifying the status of the record of the family history of a specific family member." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/history-status")
    protected Enumeration<FamilyHistoryStatus> status;

    /**
     * If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition.
     */
    @Child(name = "notDone", type = {BooleanType.class}, order=3, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="The taking of a family member's history did not occur", formalDefinition="If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition." )
    protected BooleanType notDone;

    /**
     * Describes why the family member's history is absent.
     */
    @Child(name = "notDoneReason", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="subject-unknown | withheld | unable-to-obtain | deferred", formalDefinition="Describes why the family member's history is absent." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/history-not-done-reason")
    protected CodeableConcept notDoneReason;

    /**
     * The person who this history concerns.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient history is about", formalDefinition="The person who this history concerns." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person who this history concerns.)
     */
    protected Patient patientTarget;

    /**
     * The date (and possibly time) when the family member history was taken.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When history was captured/updated", formalDefinition="The date (and possibly time) when the family member history was taken." )
    protected DateTimeType date;

    /**
     * This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
     */
    @Child(name = "name", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The family member described", formalDefinition="This will either be a name or a description; e.g. \"Aunt Susan\", \"my cousin with the red hair\"." )
    protected StringType name;

    /**
     * The type of relationship this person has to the patient (father, mother, brother etc.).
     */
    @Child(name = "relationship", type = {CodeableConcept.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Relationship to the subject", formalDefinition="The type of relationship this person has to the patient (father, mother, brother etc.)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-FamilyMember")
    protected CodeableConcept relationship;

    /**
     * Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes.
     */
    @Child(name = "gender", type = {CodeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administrative-gender")
    protected Enumeration<AdministrativeGender> gender;

    /**
     * The actual or approximate date of birth of the relative.
     */
    @Child(name = "born", type = {Period.class, DateType.class, StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="(approximate) date of birth", formalDefinition="The actual or approximate date of birth of the relative." )
    protected Type born;

    /**
     * The age of the relative at the time the family member history is recorded.
     */
    @Child(name = "age", type = {Age.class, Range.class, StringType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="(approximate) age", formalDefinition="The age of the relative at the time the family member history is recorded." )
    protected Type age;

    /**
     * If true, indicates that the age value specified is an estimated value.
     */
    @Child(name = "estimatedAge", type = {BooleanType.class}, order=12, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Age is estimated?", formalDefinition="If true, indicates that the age value specified is an estimated value." )
    protected BooleanType estimatedAge;

    /**
     * Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.
     */
    @Child(name = "deceased", type = {BooleanType.class, Age.class, Range.class, DateType.class, StringType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dead? How old/when?", formalDefinition="Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record." )
    protected Type deceased;

    /**
     * Describes why the family member history occurred in coded or textual form.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Why was family member history performed?", formalDefinition="Describes why the family member history occurred in coded or textual form." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member history event.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class, AllergyIntolerance.class, QuestionnaireResponse.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Why was family member history performed?", formalDefinition="Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member history event." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member history event.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     */
    @Child(name = "note", type = {Annotation.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="General note about related person", formalDefinition="This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible." )
    protected List<Annotation> note;

    /**
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     */
    @Child(name = "condition", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Condition that the related person had", formalDefinition="The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition." )
    protected List<FamilyMemberHistoryConditionComponent> condition;

    private static final long serialVersionUID = 388742645L;

  /**
   * Constructor
   */
    public FamilyMemberHistory() {
      super();
    }

  /**
   * Constructor
   */
    public FamilyMemberHistory(Enumeration<FamilyHistoryStatus> status, Reference patient, CodeableConcept relationship) {
      super();
      this.status = status;
      this.patient = patient;
      this.relationship = relationship;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this family member history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public FamilyMemberHistory setIdentifier(List<Identifier> theIdentifier) { 
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

    public FamilyMemberHistory addIdentifier(Identifier t) { //3
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
     * @return {@link #definition} (A protocol or questionnaire that was adhered to in whole or in part by this event.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public FamilyMemberHistory setDefinition(List<Reference> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (Reference item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDefinition() { //3
      Reference t = new Reference();
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return t;
    }

    public FamilyMemberHistory addDefinition(Reference t) { //3
      if (t == null)
        return this;
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #definition}, creating it if it does not already exist
     */
    public Reference getDefinitionFirstRep() { 
      if (getDefinition().isEmpty()) {
        addDefinition();
      }
      return getDefinition().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getDefinitionTarget() { 
      if (this.definitionTarget == null)
        this.definitionTarget = new ArrayList<Resource>();
      return this.definitionTarget;
    }

    /**
     * @return {@link #status} (A code specifying the status of the record of the family history of a specific family member.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<FamilyHistoryStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<FamilyHistoryStatus>(new FamilyHistoryStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code specifying the status of the record of the family history of a specific family member.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public FamilyMemberHistory setStatusElement(Enumeration<FamilyHistoryStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the status of the record of the family history of a specific family member.
     */
    public FamilyHistoryStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the status of the record of the family history of a specific family member.
     */
    public FamilyMemberHistory setStatus(FamilyHistoryStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<FamilyHistoryStatus>(new FamilyHistoryStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #notDone} (If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition.). This is the underlying object with id, value and extensions. The accessor "getNotDone" gives direct access to the value
     */
    public BooleanType getNotDoneElement() { 
      if (this.notDone == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.notDone");
        else if (Configuration.doAutoCreate())
          this.notDone = new BooleanType(); // bb
      return this.notDone;
    }

    public boolean hasNotDoneElement() { 
      return this.notDone != null && !this.notDone.isEmpty();
    }

    public boolean hasNotDone() { 
      return this.notDone != null && !this.notDone.isEmpty();
    }

    /**
     * @param value {@link #notDone} (If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition.). This is the underlying object with id, value and extensions. The accessor "getNotDone" gives direct access to the value
     */
    public FamilyMemberHistory setNotDoneElement(BooleanType value) { 
      this.notDone = value;
      return this;
    }

    /**
     * @return If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition.
     */
    public boolean getNotDone() { 
      return this.notDone == null || this.notDone.isEmpty() ? false : this.notDone.getValue();
    }

    /**
     * @param value If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition.
     */
    public FamilyMemberHistory setNotDone(boolean value) { 
        if (this.notDone == null)
          this.notDone = new BooleanType();
        this.notDone.setValue(value);
      return this;
    }

    /**
     * @return {@link #notDoneReason} (Describes why the family member's history is absent.)
     */
    public CodeableConcept getNotDoneReason() { 
      if (this.notDoneReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.notDoneReason");
        else if (Configuration.doAutoCreate())
          this.notDoneReason = new CodeableConcept(); // cc
      return this.notDoneReason;
    }

    public boolean hasNotDoneReason() { 
      return this.notDoneReason != null && !this.notDoneReason.isEmpty();
    }

    /**
     * @param value {@link #notDoneReason} (Describes why the family member's history is absent.)
     */
    public FamilyMemberHistory setNotDoneReason(CodeableConcept value) { 
      this.notDoneReason = value;
      return this;
    }

    /**
     * @return {@link #patient} (The person who this history concerns.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person who this history concerns.)
     */
    public FamilyMemberHistory setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who this history concerns.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who this history concerns.)
     */
    public FamilyMemberHistory setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date (and possibly time) when the family member history was taken.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date (and possibly time) when the family member history was taken.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public FamilyMemberHistory setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date (and possibly time) when the family member history was taken.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date (and possibly time) when the family member history was taken.
     */
    public FamilyMemberHistory setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public FamilyMemberHistory setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
     */
    public FamilyMemberHistory setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relationship} (The type of relationship this person has to the patient (father, mother, brother etc.).)
     */
    public CodeableConcept getRelationship() { 
      if (this.relationship == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.relationship");
        else if (Configuration.doAutoCreate())
          this.relationship = new CodeableConcept(); // cc
      return this.relationship;
    }

    public boolean hasRelationship() { 
      return this.relationship != null && !this.relationship.isEmpty();
    }

    /**
     * @param value {@link #relationship} (The type of relationship this person has to the patient (father, mother, brother etc.).)
     */
    public FamilyMemberHistory setRelationship(CodeableConcept value) { 
      this.relationship = value;
      return this;
    }

    /**
     * @return {@link #gender} (Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Enumeration<AdministrativeGender> getGenderElement() { 
      if (this.gender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.gender");
        else if (Configuration.doAutoCreate())
          this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory()); // bb
      return this.gender;
    }

    public boolean hasGenderElement() { 
      return this.gender != null && !this.gender.isEmpty();
    }

    public boolean hasGender() { 
      return this.gender != null && !this.gender.isEmpty();
    }

    /**
     * @param value {@link #gender} (Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public FamilyMemberHistory setGenderElement(Enumeration<AdministrativeGender> value) { 
      this.gender = value;
      return this;
    }

    /**
     * @return Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes.
     */
    public AdministrativeGender getGender() { 
      return this.gender == null ? null : this.gender.getValue();
    }

    /**
     * @param value Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes.
     */
    public FamilyMemberHistory setGender(AdministrativeGender value) { 
      if (value == null)
        this.gender = null;
      else {
        if (this.gender == null)
          this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory());
        this.gender.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #born} (The actual or approximate date of birth of the relative.)
     */
    public Type getBorn() { 
      return this.born;
    }

    /**
     * @return {@link #born} (The actual or approximate date of birth of the relative.)
     */
    public Period getBornPeriod() throws FHIRException { 
      if (!(this.born instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.born.getClass().getName()+" was encountered");
      return (Period) this.born;
    }

    public boolean hasBornPeriod() { 
      return this.born instanceof Period;
    }

    /**
     * @return {@link #born} (The actual or approximate date of birth of the relative.)
     */
    public DateType getBornDateType() throws FHIRException { 
      if (!(this.born instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.born.getClass().getName()+" was encountered");
      return (DateType) this.born;
    }

    public boolean hasBornDateType() { 
      return this.born instanceof DateType;
    }

    /**
     * @return {@link #born} (The actual or approximate date of birth of the relative.)
     */
    public StringType getBornStringType() throws FHIRException { 
      if (!(this.born instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.born.getClass().getName()+" was encountered");
      return (StringType) this.born;
    }

    public boolean hasBornStringType() { 
      return this.born instanceof StringType;
    }

    public boolean hasBorn() { 
      return this.born != null && !this.born.isEmpty();
    }

    /**
     * @param value {@link #born} (The actual or approximate date of birth of the relative.)
     */
    public FamilyMemberHistory setBorn(Type value) { 
      this.born = value;
      return this;
    }

    /**
     * @return {@link #age} (The age of the relative at the time the family member history is recorded.)
     */
    public Type getAge() { 
      return this.age;
    }

    /**
     * @return {@link #age} (The age of the relative at the time the family member history is recorded.)
     */
    public Age getAgeAge() throws FHIRException { 
      if (!(this.age instanceof Age))
        throw new FHIRException("Type mismatch: the type Age was expected, but "+this.age.getClass().getName()+" was encountered");
      return (Age) this.age;
    }

    public boolean hasAgeAge() { 
      return this.age instanceof Age;
    }

    /**
     * @return {@link #age} (The age of the relative at the time the family member history is recorded.)
     */
    public Range getAgeRange() throws FHIRException { 
      if (!(this.age instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.age.getClass().getName()+" was encountered");
      return (Range) this.age;
    }

    public boolean hasAgeRange() { 
      return this.age instanceof Range;
    }

    /**
     * @return {@link #age} (The age of the relative at the time the family member history is recorded.)
     */
    public StringType getAgeStringType() throws FHIRException { 
      if (!(this.age instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.age.getClass().getName()+" was encountered");
      return (StringType) this.age;
    }

    public boolean hasAgeStringType() { 
      return this.age instanceof StringType;
    }

    public boolean hasAge() { 
      return this.age != null && !this.age.isEmpty();
    }

    /**
     * @param value {@link #age} (The age of the relative at the time the family member history is recorded.)
     */
    public FamilyMemberHistory setAge(Type value) { 
      this.age = value;
      return this;
    }

    /**
     * @return {@link #estimatedAge} (If true, indicates that the age value specified is an estimated value.). This is the underlying object with id, value and extensions. The accessor "getEstimatedAge" gives direct access to the value
     */
    public BooleanType getEstimatedAgeElement() { 
      if (this.estimatedAge == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create FamilyMemberHistory.estimatedAge");
        else if (Configuration.doAutoCreate())
          this.estimatedAge = new BooleanType(); // bb
      return this.estimatedAge;
    }

    public boolean hasEstimatedAgeElement() { 
      return this.estimatedAge != null && !this.estimatedAge.isEmpty();
    }

    public boolean hasEstimatedAge() { 
      return this.estimatedAge != null && !this.estimatedAge.isEmpty();
    }

    /**
     * @param value {@link #estimatedAge} (If true, indicates that the age value specified is an estimated value.). This is the underlying object with id, value and extensions. The accessor "getEstimatedAge" gives direct access to the value
     */
    public FamilyMemberHistory setEstimatedAgeElement(BooleanType value) { 
      this.estimatedAge = value;
      return this;
    }

    /**
     * @return If true, indicates that the age value specified is an estimated value.
     */
    public boolean getEstimatedAge() { 
      return this.estimatedAge == null || this.estimatedAge.isEmpty() ? false : this.estimatedAge.getValue();
    }

    /**
     * @param value If true, indicates that the age value specified is an estimated value.
     */
    public FamilyMemberHistory setEstimatedAge(boolean value) { 
        if (this.estimatedAge == null)
          this.estimatedAge = new BooleanType();
        this.estimatedAge.setValue(value);
      return this;
    }

    /**
     * @return {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public Type getDeceased() { 
      return this.deceased;
    }

    /**
     * @return {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public BooleanType getDeceasedBooleanType() throws FHIRException { 
      if (!(this.deceased instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (BooleanType) this.deceased;
    }

    public boolean hasDeceasedBooleanType() { 
      return this.deceased instanceof BooleanType;
    }

    /**
     * @return {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public Age getDeceasedAge() throws FHIRException { 
      if (!(this.deceased instanceof Age))
        throw new FHIRException("Type mismatch: the type Age was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (Age) this.deceased;
    }

    public boolean hasDeceasedAge() { 
      return this.deceased instanceof Age;
    }

    /**
     * @return {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public Range getDeceasedRange() throws FHIRException { 
      if (!(this.deceased instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (Range) this.deceased;
    }

    public boolean hasDeceasedRange() { 
      return this.deceased instanceof Range;
    }

    /**
     * @return {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public DateType getDeceasedDateType() throws FHIRException { 
      if (!(this.deceased instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (DateType) this.deceased;
    }

    public boolean hasDeceasedDateType() { 
      return this.deceased instanceof DateType;
    }

    /**
     * @return {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public StringType getDeceasedStringType() throws FHIRException { 
      if (!(this.deceased instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (StringType) this.deceased;
    }

    public boolean hasDeceasedStringType() { 
      return this.deceased instanceof StringType;
    }

    public boolean hasDeceased() { 
      return this.deceased != null && !this.deceased.isEmpty();
    }

    /**
     * @param value {@link #deceased} (Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.)
     */
    public FamilyMemberHistory setDeceased(Type value) { 
      this.deceased = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (Describes why the family member history occurred in coded or textual form.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public FamilyMemberHistory setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public FamilyMemberHistory addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member history event.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public FamilyMemberHistory setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public FamilyMemberHistory addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
    }

    /**
     * @return {@link #note} (This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public FamilyMemberHistory setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public FamilyMemberHistory addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

    /**
     * @return {@link #condition} (The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.)
     */
    public List<FamilyMemberHistoryConditionComponent> getCondition() { 
      if (this.condition == null)
        this.condition = new ArrayList<FamilyMemberHistoryConditionComponent>();
      return this.condition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public FamilyMemberHistory setCondition(List<FamilyMemberHistoryConditionComponent> theCondition) { 
      this.condition = theCondition;
      return this;
    }

    public boolean hasCondition() { 
      if (this.condition == null)
        return false;
      for (FamilyMemberHistoryConditionComponent item : this.condition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public FamilyMemberHistoryConditionComponent addCondition() { //3
      FamilyMemberHistoryConditionComponent t = new FamilyMemberHistoryConditionComponent();
      if (this.condition == null)
        this.condition = new ArrayList<FamilyMemberHistoryConditionComponent>();
      this.condition.add(t);
      return t;
    }

    public FamilyMemberHistory addCondition(FamilyMemberHistoryConditionComponent t) { //3
      if (t == null)
        return this;
      if (this.condition == null)
        this.condition = new ArrayList<FamilyMemberHistoryConditionComponent>();
      this.condition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #condition}, creating it if it does not already exist
     */
    public FamilyMemberHistoryConditionComponent getConditionFirstRep() { 
      if (getCondition().isEmpty()) {
        addCondition();
      }
      return getCondition().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this family member history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(PlanDefinition|Questionnaire)", "A protocol or questionnaire that was adhered to in whole or in part by this event.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("status", "code", "A code specifying the status of the record of the family history of a specific family member.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("notDone", "boolean", "If true, indicates the taking of an individual family member's history did not occur. The notDone element should not be used to document negated conditions, such as a family member that did not have a condition.", 0, java.lang.Integer.MAX_VALUE, notDone));
        childrenList.add(new Property("notDoneReason", "CodeableConcept", "Describes why the family member's history is absent.", 0, java.lang.Integer.MAX_VALUE, notDoneReason));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person who this history concerns.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("date", "dateTime", "The date (and possibly time) when the family member history was taken.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("name", "string", "This will either be a name or a description; e.g. \"Aunt Susan\", \"my cousin with the red hair\".", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("relationship", "CodeableConcept", "The type of relationship this person has to the patient (father, mother, brother etc.).", 0, java.lang.Integer.MAX_VALUE, relationship));
        childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the relative is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
        childrenList.add(new Property("born[x]", "Period|date|string", "The actual or approximate date of birth of the relative.", 0, java.lang.Integer.MAX_VALUE, born));
        childrenList.add(new Property("age[x]", "Age|Range|string", "The age of the relative at the time the family member history is recorded.", 0, java.lang.Integer.MAX_VALUE, age));
        childrenList.add(new Property("estimatedAge", "boolean", "If true, indicates that the age value specified is an estimated value.", 0, java.lang.Integer.MAX_VALUE, estimatedAge));
        childrenList.add(new Property("deceased[x]", "boolean|Age|Range|date|string", "Deceased flag or the actual or approximate age of the relative at the time of death for the family member history record.", 0, java.lang.Integer.MAX_VALUE, deceased));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "Describes why the family member history occurred in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition|Observation|AllergyIntolerance|QuestionnaireResponse)", "Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member history event.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("note", "Annotation", "This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("condition", "", "The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.", 0, java.lang.Integer.MAX_VALUE, condition));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<FamilyHistoryStatus>
        case 2128257269: /*notDone*/ return this.notDone == null ? new Base[0] : new Base[] {this.notDone}; // BooleanType
        case -1973169255: /*notDoneReason*/ return this.notDoneReason == null ? new Base[0] : new Base[] {this.notDoneReason}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // CodeableConcept
        case -1249512767: /*gender*/ return this.gender == null ? new Base[0] : new Base[] {this.gender}; // Enumeration<AdministrativeGender>
        case 3029833: /*born*/ return this.born == null ? new Base[0] : new Base[] {this.born}; // Type
        case 96511: /*age*/ return this.age == null ? new Base[0] : new Base[] {this.age}; // Type
        case 2130167587: /*estimatedAge*/ return this.estimatedAge == null ? new Base[0] : new Base[] {this.estimatedAge}; // BooleanType
        case 561497972: /*deceased*/ return this.deceased == null ? new Base[0] : new Base[] {this.deceased}; // Type
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : this.condition.toArray(new Base[this.condition.size()]); // FamilyMemberHistoryConditionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1014418093: // definition
          this.getDefinition().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new FamilyHistoryStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<FamilyHistoryStatus>
          return value;
        case 2128257269: // notDone
          this.notDone = castToBoolean(value); // BooleanType
          return value;
        case -1973169255: // notDoneReason
          this.notDoneReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -261851592: // relationship
          this.relationship = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1249512767: // gender
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
          return value;
        case 3029833: // born
          this.born = castToType(value); // Type
          return value;
        case 96511: // age
          this.age = castToType(value); // Type
          return value;
        case 2130167587: // estimatedAge
          this.estimatedAge = castToBoolean(value); // BooleanType
          return value;
        case 561497972: // deceased
          this.deceased = castToType(value); // Type
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -861311717: // condition
          this.getCondition().add((FamilyMemberHistoryConditionComponent) value); // FamilyMemberHistoryConditionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("definition")) {
          this.getDefinition().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new FamilyHistoryStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<FamilyHistoryStatus>
        } else if (name.equals("notDone")) {
          this.notDone = castToBoolean(value); // BooleanType
        } else if (name.equals("notDoneReason")) {
          this.notDoneReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("relationship")) {
          this.relationship = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("gender")) {
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
        } else if (name.equals("born[x]")) {
          this.born = castToType(value); // Type
        } else if (name.equals("age[x]")) {
          this.age = castToType(value); // Type
        } else if (name.equals("estimatedAge")) {
          this.estimatedAge = castToBoolean(value); // BooleanType
        } else if (name.equals("deceased[x]")) {
          this.deceased = castToType(value); // Type
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("condition")) {
          this.getCondition().add((FamilyMemberHistoryConditionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1014418093:  return addDefinition(); 
        case -892481550:  return getStatusElement();
        case 2128257269:  return getNotDoneElement();
        case -1973169255:  return getNotDoneReason(); 
        case -791418107:  return getPatient(); 
        case 3076014:  return getDateElement();
        case 3373707:  return getNameElement();
        case -261851592:  return getRelationship(); 
        case -1249512767:  return getGenderElement();
        case 67532951:  return getBorn(); 
        case 3029833:  return getBorn(); 
        case -1419716831:  return getAge(); 
        case 96511:  return getAge(); 
        case 2130167587:  return getEstimatedAgeElement();
        case -1311442804:  return getDeceased(); 
        case 561497972:  return getDeceased(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 3387378:  return addNote(); 
        case -861311717:  return addCondition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 2128257269: /*notDone*/ return new String[] {"boolean"};
        case -1973169255: /*notDoneReason*/ return new String[] {"CodeableConcept"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -261851592: /*relationship*/ return new String[] {"CodeableConcept"};
        case -1249512767: /*gender*/ return new String[] {"code"};
        case 3029833: /*born*/ return new String[] {"Period", "date", "string"};
        case 96511: /*age*/ return new String[] {"Age", "Range", "string"};
        case 2130167587: /*estimatedAge*/ return new String[] {"boolean"};
        case 561497972: /*deceased*/ return new String[] {"boolean", "Age", "Range", "date", "string"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -861311717: /*condition*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("definition")) {
          return addDefinition();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type FamilyMemberHistory.status");
        }
        else if (name.equals("notDone")) {
          throw new FHIRException("Cannot call addChild on a primitive type FamilyMemberHistory.notDone");
        }
        else if (name.equals("notDoneReason")) {
          this.notDoneReason = new CodeableConcept();
          return this.notDoneReason;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type FamilyMemberHistory.date");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type FamilyMemberHistory.name");
        }
        else if (name.equals("relationship")) {
          this.relationship = new CodeableConcept();
          return this.relationship;
        }
        else if (name.equals("gender")) {
          throw new FHIRException("Cannot call addChild on a primitive type FamilyMemberHistory.gender");
        }
        else if (name.equals("bornPeriod")) {
          this.born = new Period();
          return this.born;
        }
        else if (name.equals("bornDate")) {
          this.born = new DateType();
          return this.born;
        }
        else if (name.equals("bornString")) {
          this.born = new StringType();
          return this.born;
        }
        else if (name.equals("ageAge")) {
          this.age = new Age();
          return this.age;
        }
        else if (name.equals("ageRange")) {
          this.age = new Range();
          return this.age;
        }
        else if (name.equals("ageString")) {
          this.age = new StringType();
          return this.age;
        }
        else if (name.equals("estimatedAge")) {
          throw new FHIRException("Cannot call addChild on a primitive type FamilyMemberHistory.estimatedAge");
        }
        else if (name.equals("deceasedBoolean")) {
          this.deceased = new BooleanType();
          return this.deceased;
        }
        else if (name.equals("deceasedAge")) {
          this.deceased = new Age();
          return this.deceased;
        }
        else if (name.equals("deceasedRange")) {
          this.deceased = new Range();
          return this.deceased;
        }
        else if (name.equals("deceasedDate")) {
          this.deceased = new DateType();
          return this.deceased;
        }
        else if (name.equals("deceasedString")) {
          this.deceased = new StringType();
          return this.deceased;
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("condition")) {
          return addCondition();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "FamilyMemberHistory";

  }

      public FamilyMemberHistory copy() {
        FamilyMemberHistory dst = new FamilyMemberHistory();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (definition != null) {
          dst.definition = new ArrayList<Reference>();
          for (Reference i : definition)
            dst.definition.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.notDone = notDone == null ? null : notDone.copy();
        dst.notDoneReason = notDoneReason == null ? null : notDoneReason.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.date = date == null ? null : date.copy();
        dst.name = name == null ? null : name.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        dst.gender = gender == null ? null : gender.copy();
        dst.born = born == null ? null : born.copy();
        dst.age = age == null ? null : age.copy();
        dst.estimatedAge = estimatedAge == null ? null : estimatedAge.copy();
        dst.deceased = deceased == null ? null : deceased.copy();
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (condition != null) {
          dst.condition = new ArrayList<FamilyMemberHistoryConditionComponent>();
          for (FamilyMemberHistoryConditionComponent i : condition)
            dst.condition.add(i.copy());
        };
        return dst;
      }

      protected FamilyMemberHistory typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof FamilyMemberHistory))
          return false;
        FamilyMemberHistory o = (FamilyMemberHistory) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(status, o.status, true) && compareDeep(notDone, o.notDone, true) && compareDeep(notDoneReason, o.notDoneReason, true)
           && compareDeep(patient, o.patient, true) && compareDeep(date, o.date, true) && compareDeep(name, o.name, true)
           && compareDeep(relationship, o.relationship, true) && compareDeep(gender, o.gender, true) && compareDeep(born, o.born, true)
           && compareDeep(age, o.age, true) && compareDeep(estimatedAge, o.estimatedAge, true) && compareDeep(deceased, o.deceased, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(note, o.note, true) && compareDeep(condition, o.condition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof FamilyMemberHistory))
          return false;
        FamilyMemberHistory o = (FamilyMemberHistory) other;
        return compareValues(status, o.status, true) && compareValues(notDone, o.notDone, true) && compareValues(date, o.date, true)
           && compareValues(name, o.name, true) && compareValues(gender, o.gender, true) && compareValues(estimatedAge, o.estimatedAge, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, status
          , notDone, notDoneReason, patient, date, name, relationship, gender, born, age
          , estimatedAge, deceased, reasonCode, reasonReference, note, condition);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.FamilyMemberHistory;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When history was captured/updated</b><br>
   * Type: <b>date</b><br>
   * Path: <b>FamilyMemberHistory.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="FamilyMemberHistory.date", description="When history was captured/updated", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When history was captured/updated</b><br>
   * Type: <b>date</b><br>
   * Path: <b>FamilyMemberHistory.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A search by a record identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="FamilyMemberHistory.identifier", description="A search by a record identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A search by a record identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A search by a condition code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.condition.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="FamilyMemberHistory.condition.code", description="A search by a condition code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A search by a condition code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.condition.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>gender</b>
   * <p>
   * Description: <b>A search by a gender code of a family member</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.gender</b><br>
   * </p>
   */
  @SearchParamDefinition(name="gender", path="FamilyMemberHistory.gender", description="A search by a gender code of a family member", type="token" )
  public static final String SP_GENDER = "gender";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>gender</b>
   * <p>
   * Description: <b>A search by a gender code of a family member</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.gender</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam GENDER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_GENDER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a subject to list family member history items for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>FamilyMemberHistory.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="FamilyMemberHistory.patient", description="The identity of a subject to list family member history items for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a subject to list family member history items for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>FamilyMemberHistory.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>FamilyMemberHistory:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("FamilyMemberHistory:patient").toLocked();

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Instantiates protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>FamilyMemberHistory.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="FamilyMemberHistory.definition", description="Instantiates protocol or definition", type="reference", target={PlanDefinition.class, Questionnaire.class } )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Instantiates protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>FamilyMemberHistory.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>FamilyMemberHistory:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("FamilyMemberHistory:definition").toLocked();

 /**
   * Search parameter: <b>relationship</b>
   * <p>
   * Description: <b>A search by a relationship type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.relationship</b><br>
   * </p>
   */
  @SearchParamDefinition(name="relationship", path="FamilyMemberHistory.relationship", description="A search by a relationship type", type="token" )
  public static final String SP_RELATIONSHIP = "relationship";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>relationship</b>
   * <p>
   * Description: <b>A search by a relationship type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.relationship</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RELATIONSHIP = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RELATIONSHIP);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>partial | completed | entered-in-error | health-unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="FamilyMemberHistory.status", description="partial | completed | entered-in-error | health-unknown", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>partial | completed | entered-in-error | health-unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>FamilyMemberHistory.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

