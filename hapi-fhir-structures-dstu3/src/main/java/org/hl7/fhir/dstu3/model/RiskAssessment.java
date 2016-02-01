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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
 */
@ResourceDef(name="RiskAssessment", profile="http://hl7.org/fhir/Profile/RiskAssessment")
public class RiskAssessment extends DomainResource {

    @Block()
    public static class RiskAssessmentPredictionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * One of the potential outcomes for the patient (e.g. remission, death,  a particular condition).
         */
        @Child(name = "outcome", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Possible outcome for the subject", formalDefinition="One of the potential outcomes for the patient (e.g. remission, death,  a particular condition)." )
        protected CodeableConcept outcome;

        /**
         * How likely is the outcome (in the specified timeframe).
         */
        @Child(name = "probability", type = {DecimalType.class, Range.class, CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Likelihood of specified outcome", formalDefinition="How likely is the outcome (in the specified timeframe)." )
        protected Type probability;

        /**
         * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).
         */
        @Child(name = "relativeRisk", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Relative likelihood", formalDefinition="Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)." )
        protected DecimalType relativeRisk;

        /**
         * Indicates the period of time or age range of the subject to which the specified probability applies.
         */
        @Child(name = "when", type = {Period.class, Range.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Timeframe or age range", formalDefinition="Indicates the period of time or age range of the subject to which the specified probability applies." )
        protected Type when;

        /**
         * Additional information explaining the basis for the prediction.
         */
        @Child(name = "rationale", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation of prediction", formalDefinition="Additional information explaining the basis for the prediction." )
        protected StringType rationale;

        private static final long serialVersionUID = 647967428L;

    /**
     * Constructor
     */
      public RiskAssessmentPredictionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public RiskAssessmentPredictionComponent(CodeableConcept outcome) {
        super();
        this.outcome = outcome;
      }

        /**
         * @return {@link #outcome} (One of the potential outcomes for the patient (e.g. remission, death,  a particular condition).)
         */
        public CodeableConcept getOutcome() { 
          if (this.outcome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RiskAssessmentPredictionComponent.outcome");
            else if (Configuration.doAutoCreate())
              this.outcome = new CodeableConcept(); // cc
          return this.outcome;
        }

        public boolean hasOutcome() { 
          return this.outcome != null && !this.outcome.isEmpty();
        }

        /**
         * @param value {@link #outcome} (One of the potential outcomes for the patient (e.g. remission, death,  a particular condition).)
         */
        public RiskAssessmentPredictionComponent setOutcome(CodeableConcept value) { 
          this.outcome = value;
          return this;
        }

        /**
         * @return {@link #probability} (How likely is the outcome (in the specified timeframe).)
         */
        public Type getProbability() { 
          return this.probability;
        }

        /**
         * @return {@link #probability} (How likely is the outcome (in the specified timeframe).)
         */
        public DecimalType getProbabilityDecimalType() throws FHIRException { 
          if (!(this.probability instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.probability.getClass().getName()+" was encountered");
          return (DecimalType) this.probability;
        }

        public boolean hasProbabilityDecimalType() { 
          return this.probability instanceof DecimalType;
        }

        /**
         * @return {@link #probability} (How likely is the outcome (in the specified timeframe).)
         */
        public Range getProbabilityRange() throws FHIRException { 
          if (!(this.probability instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.probability.getClass().getName()+" was encountered");
          return (Range) this.probability;
        }

        public boolean hasProbabilityRange() { 
          return this.probability instanceof Range;
        }

        /**
         * @return {@link #probability} (How likely is the outcome (in the specified timeframe).)
         */
        public CodeableConcept getProbabilityCodeableConcept() throws FHIRException { 
          if (!(this.probability instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.probability.getClass().getName()+" was encountered");
          return (CodeableConcept) this.probability;
        }

        public boolean hasProbabilityCodeableConcept() { 
          return this.probability instanceof CodeableConcept;
        }

        public boolean hasProbability() { 
          return this.probability != null && !this.probability.isEmpty();
        }

        /**
         * @param value {@link #probability} (How likely is the outcome (in the specified timeframe).)
         */
        public RiskAssessmentPredictionComponent setProbability(Type value) { 
          this.probability = value;
          return this;
        }

        /**
         * @return {@link #relativeRisk} (Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).). This is the underlying object with id, value and extensions. The accessor "getRelativeRisk" gives direct access to the value
         */
        public DecimalType getRelativeRiskElement() { 
          if (this.relativeRisk == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RiskAssessmentPredictionComponent.relativeRisk");
            else if (Configuration.doAutoCreate())
              this.relativeRisk = new DecimalType(); // bb
          return this.relativeRisk;
        }

        public boolean hasRelativeRiskElement() { 
          return this.relativeRisk != null && !this.relativeRisk.isEmpty();
        }

        public boolean hasRelativeRisk() { 
          return this.relativeRisk != null && !this.relativeRisk.isEmpty();
        }

        /**
         * @param value {@link #relativeRisk} (Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).). This is the underlying object with id, value and extensions. The accessor "getRelativeRisk" gives direct access to the value
         */
        public RiskAssessmentPredictionComponent setRelativeRiskElement(DecimalType value) { 
          this.relativeRisk = value;
          return this;
        }

        /**
         * @return Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).
         */
        public BigDecimal getRelativeRisk() { 
          return this.relativeRisk == null ? null : this.relativeRisk.getValue();
        }

        /**
         * @param value Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).
         */
        public RiskAssessmentPredictionComponent setRelativeRisk(BigDecimal value) { 
          if (value == null)
            this.relativeRisk = null;
          else {
            if (this.relativeRisk == null)
              this.relativeRisk = new DecimalType();
            this.relativeRisk.setValue(value);
          }
          return this;
        }

        /**
         * @param value Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).
         */
        public RiskAssessmentPredictionComponent setRelativeRisk(long value) { 
              this.relativeRisk = new DecimalType();
            this.relativeRisk.setValue(value);
          return this;
        }

        /**
         * @param value Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).
         */
        public RiskAssessmentPredictionComponent setRelativeRisk(double value) { 
              this.relativeRisk = new DecimalType();
            this.relativeRisk.setValue(value);
          return this;
        }

        /**
         * @return {@link #when} (Indicates the period of time or age range of the subject to which the specified probability applies.)
         */
        public Type getWhen() { 
          return this.when;
        }

        /**
         * @return {@link #when} (Indicates the period of time or age range of the subject to which the specified probability applies.)
         */
        public Period getWhenPeriod() throws FHIRException { 
          if (!(this.when instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.when.getClass().getName()+" was encountered");
          return (Period) this.when;
        }

        public boolean hasWhenPeriod() { 
          return this.when instanceof Period;
        }

        /**
         * @return {@link #when} (Indicates the period of time or age range of the subject to which the specified probability applies.)
         */
        public Range getWhenRange() throws FHIRException { 
          if (!(this.when instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.when.getClass().getName()+" was encountered");
          return (Range) this.when;
        }

        public boolean hasWhenRange() { 
          return this.when instanceof Range;
        }

        public boolean hasWhen() { 
          return this.when != null && !this.when.isEmpty();
        }

        /**
         * @param value {@link #when} (Indicates the period of time or age range of the subject to which the specified probability applies.)
         */
        public RiskAssessmentPredictionComponent setWhen(Type value) { 
          this.when = value;
          return this;
        }

        /**
         * @return {@link #rationale} (Additional information explaining the basis for the prediction.). This is the underlying object with id, value and extensions. The accessor "getRationale" gives direct access to the value
         */
        public StringType getRationaleElement() { 
          if (this.rationale == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RiskAssessmentPredictionComponent.rationale");
            else if (Configuration.doAutoCreate())
              this.rationale = new StringType(); // bb
          return this.rationale;
        }

        public boolean hasRationaleElement() { 
          return this.rationale != null && !this.rationale.isEmpty();
        }

        public boolean hasRationale() { 
          return this.rationale != null && !this.rationale.isEmpty();
        }

        /**
         * @param value {@link #rationale} (Additional information explaining the basis for the prediction.). This is the underlying object with id, value and extensions. The accessor "getRationale" gives direct access to the value
         */
        public RiskAssessmentPredictionComponent setRationaleElement(StringType value) { 
          this.rationale = value;
          return this;
        }

        /**
         * @return Additional information explaining the basis for the prediction.
         */
        public String getRationale() { 
          return this.rationale == null ? null : this.rationale.getValue();
        }

        /**
         * @param value Additional information explaining the basis for the prediction.
         */
        public RiskAssessmentPredictionComponent setRationale(String value) { 
          if (Utilities.noString(value))
            this.rationale = null;
          else {
            if (this.rationale == null)
              this.rationale = new StringType();
            this.rationale.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("outcome", "CodeableConcept", "One of the potential outcomes for the patient (e.g. remission, death,  a particular condition).", 0, java.lang.Integer.MAX_VALUE, outcome));
          childrenList.add(new Property("probability[x]", "decimal|Range|CodeableConcept", "How likely is the outcome (in the specified timeframe).", 0, java.lang.Integer.MAX_VALUE, probability));
          childrenList.add(new Property("relativeRisk", "decimal", "Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.).", 0, java.lang.Integer.MAX_VALUE, relativeRisk));
          childrenList.add(new Property("when[x]", "Period|Range", "Indicates the period of time or age range of the subject to which the specified probability applies.", 0, java.lang.Integer.MAX_VALUE, when));
          childrenList.add(new Property("rationale", "string", "Additional information explaining the basis for the prediction.", 0, java.lang.Integer.MAX_VALUE, rationale));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("outcome"))
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("probability[x]"))
          this.probability = (Type) value; // Type
        else if (name.equals("relativeRisk"))
          this.relativeRisk = castToDecimal(value); // DecimalType
        else if (name.equals("when[x]"))
          this.when = (Type) value; // Type
        else if (name.equals("rationale"))
          this.rationale = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("probabilityDecimal")) {
          this.probability = new DecimalType();
          return this.probability;
        }
        else if (name.equals("probabilityRange")) {
          this.probability = new Range();
          return this.probability;
        }
        else if (name.equals("probabilityCodeableConcept")) {
          this.probability = new CodeableConcept();
          return this.probability;
        }
        else if (name.equals("relativeRisk")) {
          throw new FHIRException("Cannot call addChild on a primitive type RiskAssessment.relativeRisk");
        }
        else if (name.equals("whenPeriod")) {
          this.when = new Period();
          return this.when;
        }
        else if (name.equals("whenRange")) {
          this.when = new Range();
          return this.when;
        }
        else if (name.equals("rationale")) {
          throw new FHIRException("Cannot call addChild on a primitive type RiskAssessment.rationale");
        }
        else
          return super.addChild(name);
      }

      public RiskAssessmentPredictionComponent copy() {
        RiskAssessmentPredictionComponent dst = new RiskAssessmentPredictionComponent();
        copyValues(dst);
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.probability = probability == null ? null : probability.copy();
        dst.relativeRisk = relativeRisk == null ? null : relativeRisk.copy();
        dst.when = when == null ? null : when.copy();
        dst.rationale = rationale == null ? null : rationale.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RiskAssessmentPredictionComponent))
          return false;
        RiskAssessmentPredictionComponent o = (RiskAssessmentPredictionComponent) other;
        return compareDeep(outcome, o.outcome, true) && compareDeep(probability, o.probability, true) && compareDeep(relativeRisk, o.relativeRisk, true)
           && compareDeep(when, o.when, true) && compareDeep(rationale, o.rationale, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RiskAssessmentPredictionComponent))
          return false;
        RiskAssessmentPredictionComponent o = (RiskAssessmentPredictionComponent) other;
        return compareValues(relativeRisk, o.relativeRisk, true) && compareValues(rationale, o.rationale, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (outcome == null || outcome.isEmpty()) && (probability == null || probability.isEmpty())
           && (relativeRisk == null || relativeRisk.isEmpty()) && (when == null || when.isEmpty()) && (rationale == null || rationale.isEmpty())
          ;
      }

  public String fhirType() {
    return "RiskAssessment.prediction";

  }

  }

    /**
     * The patient or group the risk assessment applies to.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/what does assessment apply to?", formalDefinition="The patient or group the risk assessment applies to." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient or group the risk assessment applies to.)
     */
    protected Resource subjectTarget;

    /**
     * The date (and possibly time) the risk assessment was performed.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When was assessment made?", formalDefinition="The date (and possibly time) the risk assessment was performed." )
    protected DateTimeType date;

    /**
     * For assessments or prognosis specific to a particular condition, indicates the condition being assessed.
     */
    @Child(name = "condition", type = {Condition.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Condition assessed", formalDefinition="For assessments or prognosis specific to a particular condition, indicates the condition being assessed." )
    protected Reference condition;

    /**
     * The actual object that is the target of the reference (For assessments or prognosis specific to a particular condition, indicates the condition being assessed.)
     */
    protected Condition conditionTarget;

    /**
     * The encounter where the assessment was performed.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where was assessment performed?", formalDefinition="The encounter where the assessment was performed." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter where the assessment was performed.)
     */
    protected Encounter encounterTarget;

    /**
     * The provider or software application that performed the assessment.
     */
    @Child(name = "performer", type = {Practitioner.class, Device.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who did assessment?", formalDefinition="The provider or software application that performed the assessment." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The provider or software application that performed the assessment.)
     */
    protected Resource performerTarget;

    /**
     * Business identifier assigned to the risk assessment.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier for the assessment", formalDefinition="Business identifier assigned to the risk assessment." )
    protected Identifier identifier;

    /**
     * The algorithm, process or mechanism used to evaluate the risk.
     */
    @Child(name = "method", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Evaluation mechanism", formalDefinition="The algorithm, process or mechanism used to evaluate the risk." )
    protected CodeableConcept method;

    /**
     * Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.).
     */
    @Child(name = "basis", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information used in assessment", formalDefinition="Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.)." )
    protected List<Reference> basis;
    /**
     * The actual objects that are the target of the reference (Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.).)
     */
    protected List<Resource> basisTarget;


    /**
     * Describes the expected outcome for the subject.
     */
    @Child(name = "prediction", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Outcome predicted", formalDefinition="Describes the expected outcome for the subject." )
    protected List<RiskAssessmentPredictionComponent> prediction;

    /**
     * A description of the steps that might be taken to reduce the identified risk(s).
     */
    @Child(name = "mitigation", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How to reduce risk", formalDefinition="A description of the steps that might be taken to reduce the identified risk(s)." )
    protected StringType mitigation;

    private static final long serialVersionUID = 724306293L;

  /**
   * Constructor
   */
    public RiskAssessment() {
      super();
    }

    /**
     * @return {@link #subject} (The patient or group the risk assessment applies to.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient or group the risk assessment applies to.)
     */
    public RiskAssessment setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient or group the risk assessment applies to.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient or group the risk assessment applies to.)
     */
    public RiskAssessment setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date (and possibly time) the risk assessment was performed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.date");
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
     * @param value {@link #date} (The date (and possibly time) the risk assessment was performed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public RiskAssessment setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date (and possibly time) the risk assessment was performed.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date (and possibly time) the risk assessment was performed.
     */
    public RiskAssessment setDate(Date value) { 
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
     * @return {@link #condition} (For assessments or prognosis specific to a particular condition, indicates the condition being assessed.)
     */
    public Reference getCondition() { 
      if (this.condition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.condition");
        else if (Configuration.doAutoCreate())
          this.condition = new Reference(); // cc
      return this.condition;
    }

    public boolean hasCondition() { 
      return this.condition != null && !this.condition.isEmpty();
    }

    /**
     * @param value {@link #condition} (For assessments or prognosis specific to a particular condition, indicates the condition being assessed.)
     */
    public RiskAssessment setCondition(Reference value) { 
      this.condition = value;
      return this;
    }

    /**
     * @return {@link #condition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (For assessments or prognosis specific to a particular condition, indicates the condition being assessed.)
     */
    public Condition getConditionTarget() { 
      if (this.conditionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.condition");
        else if (Configuration.doAutoCreate())
          this.conditionTarget = new Condition(); // aa
      return this.conditionTarget;
    }

    /**
     * @param value {@link #condition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (For assessments or prognosis specific to a particular condition, indicates the condition being assessed.)
     */
    public RiskAssessment setConditionTarget(Condition value) { 
      this.conditionTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter where the assessment was performed.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter where the assessment was performed.)
     */
    public RiskAssessment setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter where the assessment was performed.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter where the assessment was performed.)
     */
    public RiskAssessment setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (The provider or software application that performed the assessment.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (The provider or software application that performed the assessment.)
     */
    public RiskAssessment setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider or software application that performed the assessment.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider or software application that performed the assessment.)
     */
    public RiskAssessment setPerformerTarget(Resource value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Business identifier assigned to the risk assessment.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifier assigned to the risk assessment.)
     */
    public RiskAssessment setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #method} (The algorithm, process or mechanism used to evaluate the risk.)
     */
    public CodeableConcept getMethod() { 
      if (this.method == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.method");
        else if (Configuration.doAutoCreate())
          this.method = new CodeableConcept(); // cc
      return this.method;
    }

    public boolean hasMethod() { 
      return this.method != null && !this.method.isEmpty();
    }

    /**
     * @param value {@link #method} (The algorithm, process or mechanism used to evaluate the risk.)
     */
    public RiskAssessment setMethod(CodeableConcept value) { 
      this.method = value;
      return this;
    }

    /**
     * @return {@link #basis} (Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.).)
     */
    public List<Reference> getBasis() { 
      if (this.basis == null)
        this.basis = new ArrayList<Reference>();
      return this.basis;
    }

    public boolean hasBasis() { 
      if (this.basis == null)
        return false;
      for (Reference item : this.basis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #basis} (Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.).)
     */
    // syntactic sugar
    public Reference addBasis() { //3
      Reference t = new Reference();
      if (this.basis == null)
        this.basis = new ArrayList<Reference>();
      this.basis.add(t);
      return t;
    }

    // syntactic sugar
    public RiskAssessment addBasis(Reference t) { //3
      if (t == null)
        return this;
      if (this.basis == null)
        this.basis = new ArrayList<Reference>();
      this.basis.add(t);
      return this;
    }

    /**
     * @return {@link #basis} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.).)
     */
    public List<Resource> getBasisTarget() { 
      if (this.basisTarget == null)
        this.basisTarget = new ArrayList<Resource>();
      return this.basisTarget;
    }

    /**
     * @return {@link #prediction} (Describes the expected outcome for the subject.)
     */
    public List<RiskAssessmentPredictionComponent> getPrediction() { 
      if (this.prediction == null)
        this.prediction = new ArrayList<RiskAssessmentPredictionComponent>();
      return this.prediction;
    }

    public boolean hasPrediction() { 
      if (this.prediction == null)
        return false;
      for (RiskAssessmentPredictionComponent item : this.prediction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #prediction} (Describes the expected outcome for the subject.)
     */
    // syntactic sugar
    public RiskAssessmentPredictionComponent addPrediction() { //3
      RiskAssessmentPredictionComponent t = new RiskAssessmentPredictionComponent();
      if (this.prediction == null)
        this.prediction = new ArrayList<RiskAssessmentPredictionComponent>();
      this.prediction.add(t);
      return t;
    }

    // syntactic sugar
    public RiskAssessment addPrediction(RiskAssessmentPredictionComponent t) { //3
      if (t == null)
        return this;
      if (this.prediction == null)
        this.prediction = new ArrayList<RiskAssessmentPredictionComponent>();
      this.prediction.add(t);
      return this;
    }

    /**
     * @return {@link #mitigation} (A description of the steps that might be taken to reduce the identified risk(s).). This is the underlying object with id, value and extensions. The accessor "getMitigation" gives direct access to the value
     */
    public StringType getMitigationElement() { 
      if (this.mitigation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RiskAssessment.mitigation");
        else if (Configuration.doAutoCreate())
          this.mitigation = new StringType(); // bb
      return this.mitigation;
    }

    public boolean hasMitigationElement() { 
      return this.mitigation != null && !this.mitigation.isEmpty();
    }

    public boolean hasMitigation() { 
      return this.mitigation != null && !this.mitigation.isEmpty();
    }

    /**
     * @param value {@link #mitigation} (A description of the steps that might be taken to reduce the identified risk(s).). This is the underlying object with id, value and extensions. The accessor "getMitigation" gives direct access to the value
     */
    public RiskAssessment setMitigationElement(StringType value) { 
      this.mitigation = value;
      return this;
    }

    /**
     * @return A description of the steps that might be taken to reduce the identified risk(s).
     */
    public String getMitigation() { 
      return this.mitigation == null ? null : this.mitigation.getValue();
    }

    /**
     * @param value A description of the steps that might be taken to reduce the identified risk(s).
     */
    public RiskAssessment setMitigation(String value) { 
      if (Utilities.noString(value))
        this.mitigation = null;
      else {
        if (this.mitigation == null)
          this.mitigation = new StringType();
        this.mitigation.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The patient or group the risk assessment applies to.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("date", "dateTime", "The date (and possibly time) the risk assessment was performed.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("condition", "Reference(Condition)", "For assessments or prognosis specific to a particular condition, indicates the condition being assessed.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter where the assessment was performed.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("performer", "Reference(Practitioner|Device)", "The provider or software application that performed the assessment.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("identifier", "Identifier", "Business identifier assigned to the risk assessment.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("method", "CodeableConcept", "The algorithm, process or mechanism used to evaluate the risk.", 0, java.lang.Integer.MAX_VALUE, method));
        childrenList.add(new Property("basis", "Reference(Any)", "Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.).", 0, java.lang.Integer.MAX_VALUE, basis));
        childrenList.add(new Property("prediction", "", "Describes the expected outcome for the subject.", 0, java.lang.Integer.MAX_VALUE, prediction));
        childrenList.add(new Property("mitigation", "string", "A description of the steps that might be taken to reduce the identified risk(s).", 0, java.lang.Integer.MAX_VALUE, mitigation));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("condition"))
          this.condition = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("method"))
          this.method = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("basis"))
          this.getBasis().add(castToReference(value));
        else if (name.equals("prediction"))
          this.getPrediction().add((RiskAssessmentPredictionComponent) value);
        else if (name.equals("mitigation"))
          this.mitigation = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type RiskAssessment.date");
        }
        else if (name.equals("condition")) {
          this.condition = new Reference();
          return this.condition;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("basis")) {
          return addBasis();
        }
        else if (name.equals("prediction")) {
          return addPrediction();
        }
        else if (name.equals("mitigation")) {
          throw new FHIRException("Cannot call addChild on a primitive type RiskAssessment.mitigation");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "RiskAssessment";

  }

      public RiskAssessment copy() {
        RiskAssessment dst = new RiskAssessment();
        copyValues(dst);
        dst.subject = subject == null ? null : subject.copy();
        dst.date = date == null ? null : date.copy();
        dst.condition = condition == null ? null : condition.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.method = method == null ? null : method.copy();
        if (basis != null) {
          dst.basis = new ArrayList<Reference>();
          for (Reference i : basis)
            dst.basis.add(i.copy());
        };
        if (prediction != null) {
          dst.prediction = new ArrayList<RiskAssessmentPredictionComponent>();
          for (RiskAssessmentPredictionComponent i : prediction)
            dst.prediction.add(i.copy());
        };
        dst.mitigation = mitigation == null ? null : mitigation.copy();
        return dst;
      }

      protected RiskAssessment typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RiskAssessment))
          return false;
        RiskAssessment o = (RiskAssessment) other;
        return compareDeep(subject, o.subject, true) && compareDeep(date, o.date, true) && compareDeep(condition, o.condition, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(performer, o.performer, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(method, o.method, true) && compareDeep(basis, o.basis, true) && compareDeep(prediction, o.prediction, true)
           && compareDeep(mitigation, o.mitigation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RiskAssessment))
          return false;
        RiskAssessment o = (RiskAssessment) other;
        return compareValues(date, o.date, true) && compareValues(mitigation, o.mitigation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (subject == null || subject.isEmpty()) && (date == null || date.isEmpty())
           && (condition == null || condition.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (performer == null || performer.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (method == null || method.isEmpty()) && (basis == null || basis.isEmpty()) && (prediction == null || prediction.isEmpty())
           && (mitigation == null || mitigation.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.RiskAssessment;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When was assessment made?</b><br>
   * Type: <b>date</b><br>
   * Path: <b>RiskAssessment.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="RiskAssessment.date", description="When was assessment made?", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When was assessment made?</b><br>
   * Type: <b>date</b><br>
   * Path: <b>RiskAssessment.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier for the assessment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>RiskAssessment.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="RiskAssessment.identifier", description="Unique identifier for the assessment", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier for the assessment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>RiskAssessment.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>condition</b>
   * <p>
   * Description: <b>Condition assessed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.condition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="condition", path="RiskAssessment.condition", description="Condition assessed", type="reference" )
  public static final String SP_CONDITION = "condition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>condition</b>
   * <p>
   * Description: <b>Condition assessed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.condition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONDITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONDITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RiskAssessment:condition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONDITION = new ca.uhn.fhir.model.api.Include("RiskAssessment:condition").toLocked();

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Who did assessment?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="RiskAssessment.performer", description="Who did assessment?", type="reference" )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Who did assessment?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RiskAssessment:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("RiskAssessment:performer").toLocked();

 /**
   * Search parameter: <b>method</b>
   * <p>
   * Description: <b>Evaluation mechanism</b><br>
   * Type: <b>token</b><br>
   * Path: <b>RiskAssessment.method</b><br>
   * </p>
   */
  @SearchParamDefinition(name="method", path="RiskAssessment.method", description="Evaluation mechanism", type="token" )
  public static final String SP_METHOD = "method";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>method</b>
   * <p>
   * Description: <b>Evaluation mechanism</b><br>
   * Type: <b>token</b><br>
   * Path: <b>RiskAssessment.method</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam METHOD = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_METHOD);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who/what does assessment apply to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="RiskAssessment.subject", description="Who/what does assessment apply to?", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who/what does assessment apply to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RiskAssessment:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("RiskAssessment:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who/what does assessment apply to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="RiskAssessment.subject", description="Who/what does assessment apply to?", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who/what does assessment apply to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RiskAssessment:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("RiskAssessment:patient").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Where was assessment performed?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="RiskAssessment.encounter", description="Where was assessment performed?", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Where was assessment performed?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RiskAssessment.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RiskAssessment:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("RiskAssessment:encounter").toLocked();


}

