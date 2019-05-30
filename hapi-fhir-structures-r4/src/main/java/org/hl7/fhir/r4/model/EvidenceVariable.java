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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The EvidenceVariable resource describes a "PICO" element that knowledge (evidence, assertion, recommendation) is about.
 */
@ResourceDef(name="EvidenceVariable", profile="http://hl7.org/fhir/StructureDefinition/EvidenceVariable")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "shortTitle", "subtitle", "status", "date", "publisher", "contact", "description", "note", "useContext", "jurisdiction", "copyright", "approvalDate", "lastReviewDate", "effectivePeriod", "topic", "author", "editor", "reviewer", "endorser", "relatedArtifact", "type", "characteristic"})
public class EvidenceVariable extends MetadataResource {

    public enum EvidenceVariableType {
        /**
         * The variable is dichotomous, such as present or absent.
         */
        DICHOTOMOUS, 
        /**
         * The variable is a continuous result such as a quantity.
         */
        CONTINUOUS, 
        /**
         * The variable is described narratively rather than quantitatively.
         */
        DESCRIPTIVE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EvidenceVariableType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("dichotomous".equals(codeString))
          return DICHOTOMOUS;
        if ("continuous".equals(codeString))
          return CONTINUOUS;
        if ("descriptive".equals(codeString))
          return DESCRIPTIVE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EvidenceVariableType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DICHOTOMOUS: return "dichotomous";
            case CONTINUOUS: return "continuous";
            case DESCRIPTIVE: return "descriptive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DICHOTOMOUS: return "http://hl7.org/fhir/variable-type";
            case CONTINUOUS: return "http://hl7.org/fhir/variable-type";
            case DESCRIPTIVE: return "http://hl7.org/fhir/variable-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DICHOTOMOUS: return "The variable is dichotomous, such as present or absent.";
            case CONTINUOUS: return "The variable is a continuous result such as a quantity.";
            case DESCRIPTIVE: return "The variable is described narratively rather than quantitatively.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DICHOTOMOUS: return "Dichotomous";
            case CONTINUOUS: return "Continuous";
            case DESCRIPTIVE: return "Descriptive";
            default: return "?";
          }
        }
    }

  public static class EvidenceVariableTypeEnumFactory implements EnumFactory<EvidenceVariableType> {
    public EvidenceVariableType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("dichotomous".equals(codeString))
          return EvidenceVariableType.DICHOTOMOUS;
        if ("continuous".equals(codeString))
          return EvidenceVariableType.CONTINUOUS;
        if ("descriptive".equals(codeString))
          return EvidenceVariableType.DESCRIPTIVE;
        throw new IllegalArgumentException("Unknown EvidenceVariableType code '"+codeString+"'");
        }
        public Enumeration<EvidenceVariableType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EvidenceVariableType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("dichotomous".equals(codeString))
          return new Enumeration<EvidenceVariableType>(this, EvidenceVariableType.DICHOTOMOUS);
        if ("continuous".equals(codeString))
          return new Enumeration<EvidenceVariableType>(this, EvidenceVariableType.CONTINUOUS);
        if ("descriptive".equals(codeString))
          return new Enumeration<EvidenceVariableType>(this, EvidenceVariableType.DESCRIPTIVE);
        throw new FHIRException("Unknown EvidenceVariableType code '"+codeString+"'");
        }
    public String toCode(EvidenceVariableType code) {
      if (code == EvidenceVariableType.DICHOTOMOUS)
        return "dichotomous";
      if (code == EvidenceVariableType.CONTINUOUS)
        return "continuous";
      if (code == EvidenceVariableType.DESCRIPTIVE)
        return "descriptive";
      return "?";
      }
    public String toSystem(EvidenceVariableType code) {
      return code.getSystem();
      }
    }

    public enum GroupMeasure {
        /**
         * Aggregated using Mean of participant values.
         */
        MEAN, 
        /**
         * Aggregated using Median of participant values.
         */
        MEDIAN, 
        /**
         * Aggregated using Mean of study mean values.
         */
        MEANOFMEAN, 
        /**
         * Aggregated using Mean of study median values.
         */
        MEANOFMEDIAN, 
        /**
         * Aggregated using Median of study mean values.
         */
        MEDIANOFMEAN, 
        /**
         * Aggregated using Median of study median values.
         */
        MEDIANOFMEDIAN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GroupMeasure fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mean".equals(codeString))
          return MEAN;
        if ("median".equals(codeString))
          return MEDIAN;
        if ("mean-of-mean".equals(codeString))
          return MEANOFMEAN;
        if ("mean-of-median".equals(codeString))
          return MEANOFMEDIAN;
        if ("median-of-mean".equals(codeString))
          return MEDIANOFMEAN;
        if ("median-of-median".equals(codeString))
          return MEDIANOFMEDIAN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GroupMeasure code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MEAN: return "mean";
            case MEDIAN: return "median";
            case MEANOFMEAN: return "mean-of-mean";
            case MEANOFMEDIAN: return "mean-of-median";
            case MEDIANOFMEAN: return "median-of-mean";
            case MEDIANOFMEDIAN: return "median-of-median";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MEAN: return "http://hl7.org/fhir/group-measure";
            case MEDIAN: return "http://hl7.org/fhir/group-measure";
            case MEANOFMEAN: return "http://hl7.org/fhir/group-measure";
            case MEANOFMEDIAN: return "http://hl7.org/fhir/group-measure";
            case MEDIANOFMEAN: return "http://hl7.org/fhir/group-measure";
            case MEDIANOFMEDIAN: return "http://hl7.org/fhir/group-measure";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MEAN: return "Aggregated using Mean of participant values.";
            case MEDIAN: return "Aggregated using Median of participant values.";
            case MEANOFMEAN: return "Aggregated using Mean of study mean values.";
            case MEANOFMEDIAN: return "Aggregated using Mean of study median values.";
            case MEDIANOFMEAN: return "Aggregated using Median of study mean values.";
            case MEDIANOFMEDIAN: return "Aggregated using Median of study median values.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MEAN: return "Mean";
            case MEDIAN: return "Median";
            case MEANOFMEAN: return "Mean of Study Means";
            case MEANOFMEDIAN: return "Mean of Study Medins";
            case MEDIANOFMEAN: return "Median of Study Means";
            case MEDIANOFMEDIAN: return "Median of Study Medians";
            default: return "?";
          }
        }
    }

  public static class GroupMeasureEnumFactory implements EnumFactory<GroupMeasure> {
    public GroupMeasure fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mean".equals(codeString))
          return GroupMeasure.MEAN;
        if ("median".equals(codeString))
          return GroupMeasure.MEDIAN;
        if ("mean-of-mean".equals(codeString))
          return GroupMeasure.MEANOFMEAN;
        if ("mean-of-median".equals(codeString))
          return GroupMeasure.MEANOFMEDIAN;
        if ("median-of-mean".equals(codeString))
          return GroupMeasure.MEDIANOFMEAN;
        if ("median-of-median".equals(codeString))
          return GroupMeasure.MEDIANOFMEDIAN;
        throw new IllegalArgumentException("Unknown GroupMeasure code '"+codeString+"'");
        }
        public Enumeration<GroupMeasure> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GroupMeasure>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("mean".equals(codeString))
          return new Enumeration<GroupMeasure>(this, GroupMeasure.MEAN);
        if ("median".equals(codeString))
          return new Enumeration<GroupMeasure>(this, GroupMeasure.MEDIAN);
        if ("mean-of-mean".equals(codeString))
          return new Enumeration<GroupMeasure>(this, GroupMeasure.MEANOFMEAN);
        if ("mean-of-median".equals(codeString))
          return new Enumeration<GroupMeasure>(this, GroupMeasure.MEANOFMEDIAN);
        if ("median-of-mean".equals(codeString))
          return new Enumeration<GroupMeasure>(this, GroupMeasure.MEDIANOFMEAN);
        if ("median-of-median".equals(codeString))
          return new Enumeration<GroupMeasure>(this, GroupMeasure.MEDIANOFMEDIAN);
        throw new FHIRException("Unknown GroupMeasure code '"+codeString+"'");
        }
    public String toCode(GroupMeasure code) {
      if (code == GroupMeasure.MEAN)
        return "mean";
      if (code == GroupMeasure.MEDIAN)
        return "median";
      if (code == GroupMeasure.MEANOFMEAN)
        return "mean-of-mean";
      if (code == GroupMeasure.MEANOFMEDIAN)
        return "mean-of-median";
      if (code == GroupMeasure.MEDIANOFMEAN)
        return "median-of-mean";
      if (code == GroupMeasure.MEDIANOFMEDIAN)
        return "median-of-median";
      return "?";
      }
    public String toSystem(GroupMeasure code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class EvidenceVariableCharacteristicComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the characteristic", formalDefinition="A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user." )
        protected StringType description;

        /**
         * Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).
         */
        @Child(name = "definition", type = {Group.class, CanonicalType.class, CodeableConcept.class, Expression.class, DataRequirement.class, TriggerDefinition.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What code or expression defines members?", formalDefinition="Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year)." )
        protected Type definition;

        /**
         * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
         */
        @Child(name = "usageContext", type = {UsageContext.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What code/value pairs define members?", formalDefinition="Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings." )
        protected List<UsageContext> usageContext;

        /**
         * When true, members with this characteristic are excluded from the element.
         */
        @Child(name = "exclude", type = {BooleanType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the characteristic includes or excludes members", formalDefinition="When true, members with this characteristic are excluded from the element." )
        protected BooleanType exclude;

        /**
         * Indicates what effective period the study covers.
         */
        @Child(name = "participantEffective", type = {DateTimeType.class, Period.class, Duration.class, Timing.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What time period do participants cover", formalDefinition="Indicates what effective period the study covers." )
        protected Type participantEffective;

        /**
         * Indicates duration from the participant's study entry.
         */
        @Child(name = "timeFromStart", type = {Duration.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Observation time from study start", formalDefinition="Indicates duration from the participant's study entry." )
        protected Duration timeFromStart;

        /**
         * Indicates how elements are aggregated within the study effective period.
         */
        @Child(name = "groupMeasure", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="mean | median | mean-of-mean | mean-of-median | median-of-mean | median-of-median", formalDefinition="Indicates how elements are aggregated within the study effective period." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/group-measure")
        protected Enumeration<GroupMeasure> groupMeasure;

        private static final long serialVersionUID = 1901961318L;

    /**
     * Constructor
     */
      public EvidenceVariableCharacteristicComponent() {
        super();
      }

    /**
     * Constructor
     */
      public EvidenceVariableCharacteristicComponent(Type definition) {
        super();
        this.definition = definition;
      }

        /**
         * @return {@link #description} (A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EvidenceVariableCharacteristicComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public EvidenceVariableCharacteristicComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.
         */
        public EvidenceVariableCharacteristicComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public Type getDefinition() { 
          return this.definition;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public Reference getDefinitionReference() throws FHIRException { 
          if (this.definition == null)
            this.definition = new Reference();
          if (!(this.definition instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.definition.getClass().getName()+" was encountered");
          return (Reference) this.definition;
        }

        public boolean hasDefinitionReference() { 
          return this != null && this.definition instanceof Reference;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public CanonicalType getDefinitionCanonicalType() throws FHIRException { 
          if (this.definition == null)
            this.definition = new CanonicalType();
          if (!(this.definition instanceof CanonicalType))
            throw new FHIRException("Type mismatch: the type CanonicalType was expected, but "+this.definition.getClass().getName()+" was encountered");
          return (CanonicalType) this.definition;
        }

        public boolean hasDefinitionCanonicalType() { 
          return this != null && this.definition instanceof CanonicalType;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public CodeableConcept getDefinitionCodeableConcept() throws FHIRException { 
          if (this.definition == null)
            this.definition = new CodeableConcept();
          if (!(this.definition instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.definition.getClass().getName()+" was encountered");
          return (CodeableConcept) this.definition;
        }

        public boolean hasDefinitionCodeableConcept() { 
          return this != null && this.definition instanceof CodeableConcept;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public Expression getDefinitionExpression() throws FHIRException { 
          if (this.definition == null)
            this.definition = new Expression();
          if (!(this.definition instanceof Expression))
            throw new FHIRException("Type mismatch: the type Expression was expected, but "+this.definition.getClass().getName()+" was encountered");
          return (Expression) this.definition;
        }

        public boolean hasDefinitionExpression() { 
          return this != null && this.definition instanceof Expression;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public DataRequirement getDefinitionDataRequirement() throws FHIRException { 
          if (this.definition == null)
            this.definition = new DataRequirement();
          if (!(this.definition instanceof DataRequirement))
            throw new FHIRException("Type mismatch: the type DataRequirement was expected, but "+this.definition.getClass().getName()+" was encountered");
          return (DataRequirement) this.definition;
        }

        public boolean hasDefinitionDataRequirement() { 
          return this != null && this.definition instanceof DataRequirement;
        }

        /**
         * @return {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public TriggerDefinition getDefinitionTriggerDefinition() throws FHIRException { 
          if (this.definition == null)
            this.definition = new TriggerDefinition();
          if (!(this.definition instanceof TriggerDefinition))
            throw new FHIRException("Type mismatch: the type TriggerDefinition was expected, but "+this.definition.getClass().getName()+" was encountered");
          return (TriggerDefinition) this.definition;
        }

        public boolean hasDefinitionTriggerDefinition() { 
          return this != null && this.definition instanceof TriggerDefinition;
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public EvidenceVariableCharacteristicComponent setDefinition(Type value) { 
          if (value != null && !(value instanceof Reference || value instanceof CanonicalType || value instanceof CodeableConcept || value instanceof Expression || value instanceof DataRequirement || value instanceof TriggerDefinition))
            throw new Error("Not the right type for EvidenceVariable.characteristic.definition[x]: "+value.fhirType());
          this.definition = value;
          return this;
        }

        /**
         * @return {@link #usageContext} (Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.)
         */
        public List<UsageContext> getUsageContext() { 
          if (this.usageContext == null)
            this.usageContext = new ArrayList<UsageContext>();
          return this.usageContext;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public EvidenceVariableCharacteristicComponent setUsageContext(List<UsageContext> theUsageContext) { 
          this.usageContext = theUsageContext;
          return this;
        }

        public boolean hasUsageContext() { 
          if (this.usageContext == null)
            return false;
          for (UsageContext item : this.usageContext)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public UsageContext addUsageContext() { //3
          UsageContext t = new UsageContext();
          if (this.usageContext == null)
            this.usageContext = new ArrayList<UsageContext>();
          this.usageContext.add(t);
          return t;
        }

        public EvidenceVariableCharacteristicComponent addUsageContext(UsageContext t) { //3
          if (t == null)
            return this;
          if (this.usageContext == null)
            this.usageContext = new ArrayList<UsageContext>();
          this.usageContext.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #usageContext}, creating it if it does not already exist
         */
        public UsageContext getUsageContextFirstRep() { 
          if (getUsageContext().isEmpty()) {
            addUsageContext();
          }
          return getUsageContext().get(0);
        }

        /**
         * @return {@link #exclude} (When true, members with this characteristic are excluded from the element.). This is the underlying object with id, value and extensions. The accessor "getExclude" gives direct access to the value
         */
        public BooleanType getExcludeElement() { 
          if (this.exclude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EvidenceVariableCharacteristicComponent.exclude");
            else if (Configuration.doAutoCreate())
              this.exclude = new BooleanType(); // bb
          return this.exclude;
        }

        public boolean hasExcludeElement() { 
          return this.exclude != null && !this.exclude.isEmpty();
        }

        public boolean hasExclude() { 
          return this.exclude != null && !this.exclude.isEmpty();
        }

        /**
         * @param value {@link #exclude} (When true, members with this characteristic are excluded from the element.). This is the underlying object with id, value and extensions. The accessor "getExclude" gives direct access to the value
         */
        public EvidenceVariableCharacteristicComponent setExcludeElement(BooleanType value) { 
          this.exclude = value;
          return this;
        }

        /**
         * @return When true, members with this characteristic are excluded from the element.
         */
        public boolean getExclude() { 
          return this.exclude == null || this.exclude.isEmpty() ? false : this.exclude.getValue();
        }

        /**
         * @param value When true, members with this characteristic are excluded from the element.
         */
        public EvidenceVariableCharacteristicComponent setExclude(boolean value) { 
            if (this.exclude == null)
              this.exclude = new BooleanType();
            this.exclude.setValue(value);
          return this;
        }

        /**
         * @return {@link #participantEffective} (Indicates what effective period the study covers.)
         */
        public Type getParticipantEffective() { 
          return this.participantEffective;
        }

        /**
         * @return {@link #participantEffective} (Indicates what effective period the study covers.)
         */
        public DateTimeType getParticipantEffectiveDateTimeType() throws FHIRException { 
          if (this.participantEffective == null)
            this.participantEffective = new DateTimeType();
          if (!(this.participantEffective instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.participantEffective.getClass().getName()+" was encountered");
          return (DateTimeType) this.participantEffective;
        }

        public boolean hasParticipantEffectiveDateTimeType() { 
          return this != null && this.participantEffective instanceof DateTimeType;
        }

        /**
         * @return {@link #participantEffective} (Indicates what effective period the study covers.)
         */
        public Period getParticipantEffectivePeriod() throws FHIRException { 
          if (this.participantEffective == null)
            this.participantEffective = new Period();
          if (!(this.participantEffective instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.participantEffective.getClass().getName()+" was encountered");
          return (Period) this.participantEffective;
        }

        public boolean hasParticipantEffectivePeriod() { 
          return this != null && this.participantEffective instanceof Period;
        }

        /**
         * @return {@link #participantEffective} (Indicates what effective period the study covers.)
         */
        public Duration getParticipantEffectiveDuration() throws FHIRException { 
          if (this.participantEffective == null)
            this.participantEffective = new Duration();
          if (!(this.participantEffective instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.participantEffective.getClass().getName()+" was encountered");
          return (Duration) this.participantEffective;
        }

        public boolean hasParticipantEffectiveDuration() { 
          return this != null && this.participantEffective instanceof Duration;
        }

        /**
         * @return {@link #participantEffective} (Indicates what effective period the study covers.)
         */
        public Timing getParticipantEffectiveTiming() throws FHIRException { 
          if (this.participantEffective == null)
            this.participantEffective = new Timing();
          if (!(this.participantEffective instanceof Timing))
            throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.participantEffective.getClass().getName()+" was encountered");
          return (Timing) this.participantEffective;
        }

        public boolean hasParticipantEffectiveTiming() { 
          return this != null && this.participantEffective instanceof Timing;
        }

        public boolean hasParticipantEffective() { 
          return this.participantEffective != null && !this.participantEffective.isEmpty();
        }

        /**
         * @param value {@link #participantEffective} (Indicates what effective period the study covers.)
         */
        public EvidenceVariableCharacteristicComponent setParticipantEffective(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Duration || value instanceof Timing))
            throw new Error("Not the right type for EvidenceVariable.characteristic.participantEffective[x]: "+value.fhirType());
          this.participantEffective = value;
          return this;
        }

        /**
         * @return {@link #timeFromStart} (Indicates duration from the participant's study entry.)
         */
        public Duration getTimeFromStart() { 
          if (this.timeFromStart == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EvidenceVariableCharacteristicComponent.timeFromStart");
            else if (Configuration.doAutoCreate())
              this.timeFromStart = new Duration(); // cc
          return this.timeFromStart;
        }

        public boolean hasTimeFromStart() { 
          return this.timeFromStart != null && !this.timeFromStart.isEmpty();
        }

        /**
         * @param value {@link #timeFromStart} (Indicates duration from the participant's study entry.)
         */
        public EvidenceVariableCharacteristicComponent setTimeFromStart(Duration value) { 
          this.timeFromStart = value;
          return this;
        }

        /**
         * @return {@link #groupMeasure} (Indicates how elements are aggregated within the study effective period.). This is the underlying object with id, value and extensions. The accessor "getGroupMeasure" gives direct access to the value
         */
        public Enumeration<GroupMeasure> getGroupMeasureElement() { 
          if (this.groupMeasure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EvidenceVariableCharacteristicComponent.groupMeasure");
            else if (Configuration.doAutoCreate())
              this.groupMeasure = new Enumeration<GroupMeasure>(new GroupMeasureEnumFactory()); // bb
          return this.groupMeasure;
        }

        public boolean hasGroupMeasureElement() { 
          return this.groupMeasure != null && !this.groupMeasure.isEmpty();
        }

        public boolean hasGroupMeasure() { 
          return this.groupMeasure != null && !this.groupMeasure.isEmpty();
        }

        /**
         * @param value {@link #groupMeasure} (Indicates how elements are aggregated within the study effective period.). This is the underlying object with id, value and extensions. The accessor "getGroupMeasure" gives direct access to the value
         */
        public EvidenceVariableCharacteristicComponent setGroupMeasureElement(Enumeration<GroupMeasure> value) { 
          this.groupMeasure = value;
          return this;
        }

        /**
         * @return Indicates how elements are aggregated within the study effective period.
         */
        public GroupMeasure getGroupMeasure() { 
          return this.groupMeasure == null ? null : this.groupMeasure.getValue();
        }

        /**
         * @param value Indicates how elements are aggregated within the study effective period.
         */
        public EvidenceVariableCharacteristicComponent setGroupMeasure(GroupMeasure value) { 
          if (value == null)
            this.groupMeasure = null;
          else {
            if (this.groupMeasure == null)
              this.groupMeasure = new Enumeration<GroupMeasure>(new GroupMeasureEnumFactory());
            this.groupMeasure.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "string", "A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.", 0, 1, description));
          children.add(new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition));
          children.add(new Property("usageContext", "UsageContext", "Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.", 0, java.lang.Integer.MAX_VALUE, usageContext));
          children.add(new Property("exclude", "boolean", "When true, members with this characteristic are excluded from the element.", 0, 1, exclude));
          children.add(new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective));
          children.add(new Property("timeFromStart", "Duration", "Indicates duration from the participant's study entry.", 0, 1, timeFromStart));
          children.add(new Property("groupMeasure", "code", "Indicates how elements are aggregated within the study effective period.", 0, 1, groupMeasure));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "string", "A short, natural language description of the characteristic that could be used to communicate the criteria to an end-user.", 0, 1, description);
          case -1139422643: /*definition[x]*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -1014418093: /*definition*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -820021448: /*definitionReference*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case 933485793: /*definitionCanonical*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -1446002226: /*definitionCodeableConcept*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case 1463703627: /*definitionExpression*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -660350874: /*definitionDataRequirement*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -1130324968: /*definitionTriggerDefinition*/  return new Property("definition[x]", "Reference(Group)|canonical(ActivityDefinition)|CodeableConcept|Expression|DataRequirement|TriggerDefinition", "Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case 907012302: /*usageContext*/  return new Property("usageContext", "UsageContext", "Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.", 0, java.lang.Integer.MAX_VALUE, usageContext);
          case -1321148966: /*exclude*/  return new Property("exclude", "boolean", "When true, members with this characteristic are excluded from the element.", 0, 1, exclude);
          case 1777308748: /*participantEffective[x]*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case 1376306100: /*participantEffective*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -1721146513: /*participantEffectiveDateTime*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -883650923: /*participantEffectivePeriod*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -1210941080: /*participantEffectiveDuration*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -765589218: /*participantEffectiveTiming*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case 2100140683: /*timeFromStart*/  return new Property("timeFromStart", "Duration", "Indicates duration from the participant's study entry.", 0, 1, timeFromStart);
          case 588892639: /*groupMeasure*/  return new Property("groupMeasure", "code", "Indicates how elements are aggregated within the study effective period.", 0, 1, groupMeasure);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Type
        case 907012302: /*usageContext*/ return this.usageContext == null ? new Base[0] : this.usageContext.toArray(new Base[this.usageContext.size()]); // UsageContext
        case -1321148966: /*exclude*/ return this.exclude == null ? new Base[0] : new Base[] {this.exclude}; // BooleanType
        case 1376306100: /*participantEffective*/ return this.participantEffective == null ? new Base[0] : new Base[] {this.participantEffective}; // Type
        case 2100140683: /*timeFromStart*/ return this.timeFromStart == null ? new Base[0] : new Base[] {this.timeFromStart}; // Duration
        case 588892639: /*groupMeasure*/ return this.groupMeasure == null ? new Base[0] : new Base[] {this.groupMeasure}; // Enumeration<GroupMeasure>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1014418093: // definition
          this.definition = castToType(value); // Type
          return value;
        case 907012302: // usageContext
          this.getUsageContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -1321148966: // exclude
          this.exclude = castToBoolean(value); // BooleanType
          return value;
        case 1376306100: // participantEffective
          this.participantEffective = castToType(value); // Type
          return value;
        case 2100140683: // timeFromStart
          this.timeFromStart = castToDuration(value); // Duration
          return value;
        case 588892639: // groupMeasure
          value = new GroupMeasureEnumFactory().fromType(castToCode(value));
          this.groupMeasure = (Enumeration) value; // Enumeration<GroupMeasure>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("definition[x]")) {
          this.definition = castToType(value); // Type
        } else if (name.equals("usageContext")) {
          this.getUsageContext().add(castToUsageContext(value));
        } else if (name.equals("exclude")) {
          this.exclude = castToBoolean(value); // BooleanType
        } else if (name.equals("participantEffective[x]")) {
          this.participantEffective = castToType(value); // Type
        } else if (name.equals("timeFromStart")) {
          this.timeFromStart = castToDuration(value); // Duration
        } else if (name.equals("groupMeasure")) {
          value = new GroupMeasureEnumFactory().fromType(castToCode(value));
          this.groupMeasure = (Enumeration) value; // Enumeration<GroupMeasure>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case -1139422643:  return getDefinition(); 
        case -1014418093:  return getDefinition(); 
        case 907012302:  return addUsageContext(); 
        case -1321148966:  return getExcludeElement();
        case 1777308748:  return getParticipantEffective(); 
        case 1376306100:  return getParticipantEffective(); 
        case 2100140683:  return getTimeFromStart(); 
        case 588892639:  return getGroupMeasureElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"Reference", "canonical", "CodeableConcept", "Expression", "DataRequirement", "TriggerDefinition"};
        case 907012302: /*usageContext*/ return new String[] {"UsageContext"};
        case -1321148966: /*exclude*/ return new String[] {"boolean"};
        case 1376306100: /*participantEffective*/ return new String[] {"dateTime", "Period", "Duration", "Timing"};
        case 2100140683: /*timeFromStart*/ return new String[] {"Duration"};
        case 588892639: /*groupMeasure*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.description");
        }
        else if (name.equals("definitionReference")) {
          this.definition = new Reference();
          return this.definition;
        }
        else if (name.equals("definitionCanonical")) {
          this.definition = new CanonicalType();
          return this.definition;
        }
        else if (name.equals("definitionCodeableConcept")) {
          this.definition = new CodeableConcept();
          return this.definition;
        }
        else if (name.equals("definitionExpression")) {
          this.definition = new Expression();
          return this.definition;
        }
        else if (name.equals("definitionDataRequirement")) {
          this.definition = new DataRequirement();
          return this.definition;
        }
        else if (name.equals("definitionTriggerDefinition")) {
          this.definition = new TriggerDefinition();
          return this.definition;
        }
        else if (name.equals("usageContext")) {
          return addUsageContext();
        }
        else if (name.equals("exclude")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.exclude");
        }
        else if (name.equals("participantEffectiveDateTime")) {
          this.participantEffective = new DateTimeType();
          return this.participantEffective;
        }
        else if (name.equals("participantEffectivePeriod")) {
          this.participantEffective = new Period();
          return this.participantEffective;
        }
        else if (name.equals("participantEffectiveDuration")) {
          this.participantEffective = new Duration();
          return this.participantEffective;
        }
        else if (name.equals("participantEffectiveTiming")) {
          this.participantEffective = new Timing();
          return this.participantEffective;
        }
        else if (name.equals("timeFromStart")) {
          this.timeFromStart = new Duration();
          return this.timeFromStart;
        }
        else if (name.equals("groupMeasure")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.groupMeasure");
        }
        else
          return super.addChild(name);
      }

      public EvidenceVariableCharacteristicComponent copy() {
        EvidenceVariableCharacteristicComponent dst = new EvidenceVariableCharacteristicComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.definition = definition == null ? null : definition.copy();
        if (usageContext != null) {
          dst.usageContext = new ArrayList<UsageContext>();
          for (UsageContext i : usageContext)
            dst.usageContext.add(i.copy());
        };
        dst.exclude = exclude == null ? null : exclude.copy();
        dst.participantEffective = participantEffective == null ? null : participantEffective.copy();
        dst.timeFromStart = timeFromStart == null ? null : timeFromStart.copy();
        dst.groupMeasure = groupMeasure == null ? null : groupMeasure.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof EvidenceVariableCharacteristicComponent))
          return false;
        EvidenceVariableCharacteristicComponent o = (EvidenceVariableCharacteristicComponent) other_;
        return compareDeep(description, o.description, true) && compareDeep(definition, o.definition, true)
           && compareDeep(usageContext, o.usageContext, true) && compareDeep(exclude, o.exclude, true) && compareDeep(participantEffective, o.participantEffective, true)
           && compareDeep(timeFromStart, o.timeFromStart, true) && compareDeep(groupMeasure, o.groupMeasure, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof EvidenceVariableCharacteristicComponent))
          return false;
        EvidenceVariableCharacteristicComponent o = (EvidenceVariableCharacteristicComponent) other_;
        return compareValues(description, o.description, true) && compareValues(exclude, o.exclude, true) && compareValues(groupMeasure, o.groupMeasure, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, definition, usageContext
          , exclude, participantEffective, timeFromStart, groupMeasure);
      }

  public String fhirType() {
    return "EvidenceVariable.characteristic";

  }

  }

    /**
     * A formal identifier that is used to identify this evidence variable when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the evidence variable", formalDefinition="A formal identifier that is used to identify this evidence variable when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.
     */
    @Child(name = "shortTitle", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Title for use in informal contexts", formalDefinition="The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary." )
    protected StringType shortTitle;

    /**
     * An explanatory or alternate title for the EvidenceVariable giving additional information about its content.
     */
    @Child(name = "subtitle", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Subordinate title of the EvidenceVariable", formalDefinition="An explanatory or alternate title for the EvidenceVariable giving additional information about its content." )
    protected StringType subtitle;

    /**
     * A human-readable string to clarify or explain concepts about the resource.
     */
    @Child(name = "note", type = {Annotation.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Used for footnotes or explanatory notes", formalDefinition="A human-readable string to clarify or explain concepts about the resource." )
    protected List<Annotation> note;

    /**
     * A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable." )
    protected MarkdownType copyright;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the evidence variable was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the evidence variable was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the evidence variable content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the evidence variable is expected to be used", formalDefinition="The period during which the evidence variable content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * Descriptive topics related to the content of the EvidenceVariable. Topics provide a high-level categorization grouping types of EvidenceVariables that can be useful for filtering and searching.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The category of the EvidenceVariable, such as Education, Treatment, Assessment, etc.", formalDefinition="Descriptive topics related to the content of the EvidenceVariable. Topics provide a high-level categorization grouping types of EvidenceVariables that can be useful for filtering and searching." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/definition-topic")
    protected List<CodeableConcept> topic;

    /**
     * An individiual or organization primarily involved in the creation and maintenance of the content.
     */
    @Child(name = "author", type = {ContactDetail.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who authored the content", formalDefinition="An individiual or organization primarily involved in the creation and maintenance of the content." )
    protected List<ContactDetail> author;

    /**
     * An individual or organization primarily responsible for internal coherence of the content.
     */
    @Child(name = "editor", type = {ContactDetail.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who edited the content", formalDefinition="An individual or organization primarily responsible for internal coherence of the content." )
    protected List<ContactDetail> editor;

    /**
     * An individual or organization primarily responsible for review of some aspect of the content.
     */
    @Child(name = "reviewer", type = {ContactDetail.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who reviewed the content", formalDefinition="An individual or organization primarily responsible for review of some aspect of the content." )
    protected List<ContactDetail> reviewer;

    /**
     * An individual or organization responsible for officially endorsing the content for use in some setting.
     */
    @Child(name = "endorser", type = {ContactDetail.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who endorsed the content", formalDefinition="An individual or organization responsible for officially endorsing the content for use in some setting." )
    protected List<ContactDetail> endorser;

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional documentation, citations, etc.", formalDefinition="Related artifacts such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * The type of evidence element, a population, an exposure, or an outcome.
     */
    @Child(name = "type", type = {CodeType.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="dichotomous | continuous | descriptive", formalDefinition="The type of evidence element, a population, an exposure, or an outcome." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/variable-type")
    protected Enumeration<EvidenceVariableType> type;

    /**
     * A characteristic that defines the members of the evidence element. Multiple characteristics are applied with "and" semantics.
     */
    @Child(name = "characteristic", type = {}, order=15, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What defines the members of the evidence element", formalDefinition="A characteristic that defines the members of the evidence element. Multiple characteristics are applied with \"and\" semantics." )
    protected List<EvidenceVariableCharacteristicComponent> characteristic;

    private static final long serialVersionUID = -317280154L;

  /**
   * Constructor
   */
    public EvidenceVariable() {
      super();
    }

  /**
   * Constructor
   */
    public EvidenceVariable(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public EvidenceVariable setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different servers.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different servers.
     */
    public EvidenceVariable setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this evidence variable when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setIdentifier(List<Identifier> theIdentifier) { 
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

    public EvidenceVariable addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The identifier that is used to identify this version of the evidence variable when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the evidence variable when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public EvidenceVariable setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the evidence variable when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the evidence variable when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public EvidenceVariable setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A natural language name identifying the evidence variable. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.name");
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
     * @param value {@link #name} (A natural language name identifying the evidence variable. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public EvidenceVariable setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the evidence variable. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the evidence variable. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public EvidenceVariable setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the evidence variable.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the evidence variable.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public EvidenceVariable setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the evidence variable.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the evidence variable.
     */
    public EvidenceVariable setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #shortTitle} (The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.). This is the underlying object with id, value and extensions. The accessor "getShortTitle" gives direct access to the value
     */
    public StringType getShortTitleElement() { 
      if (this.shortTitle == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.shortTitle");
        else if (Configuration.doAutoCreate())
          this.shortTitle = new StringType(); // bb
      return this.shortTitle;
    }

    public boolean hasShortTitleElement() { 
      return this.shortTitle != null && !this.shortTitle.isEmpty();
    }

    public boolean hasShortTitle() { 
      return this.shortTitle != null && !this.shortTitle.isEmpty();
    }

    /**
     * @param value {@link #shortTitle} (The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.). This is the underlying object with id, value and extensions. The accessor "getShortTitle" gives direct access to the value
     */
    public EvidenceVariable setShortTitleElement(StringType value) { 
      this.shortTitle = value;
      return this;
    }

    /**
     * @return The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.
     */
    public String getShortTitle() { 
      return this.shortTitle == null ? null : this.shortTitle.getValue();
    }

    /**
     * @param value The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.
     */
    public EvidenceVariable setShortTitle(String value) { 
      if (Utilities.noString(value))
        this.shortTitle = null;
      else {
        if (this.shortTitle == null)
          this.shortTitle = new StringType();
        this.shortTitle.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #subtitle} (An explanatory or alternate title for the EvidenceVariable giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getSubtitle" gives direct access to the value
     */
    public StringType getSubtitleElement() { 
      if (this.subtitle == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.subtitle");
        else if (Configuration.doAutoCreate())
          this.subtitle = new StringType(); // bb
      return this.subtitle;
    }

    public boolean hasSubtitleElement() { 
      return this.subtitle != null && !this.subtitle.isEmpty();
    }

    public boolean hasSubtitle() { 
      return this.subtitle != null && !this.subtitle.isEmpty();
    }

    /**
     * @param value {@link #subtitle} (An explanatory or alternate title for the EvidenceVariable giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getSubtitle" gives direct access to the value
     */
    public EvidenceVariable setSubtitleElement(StringType value) { 
      this.subtitle = value;
      return this;
    }

    /**
     * @return An explanatory or alternate title for the EvidenceVariable giving additional information about its content.
     */
    public String getSubtitle() { 
      return this.subtitle == null ? null : this.subtitle.getValue();
    }

    /**
     * @param value An explanatory or alternate title for the EvidenceVariable giving additional information about its content.
     */
    public EvidenceVariable setSubtitle(String value) { 
      if (Utilities.noString(value))
        this.subtitle = null;
      else {
        if (this.subtitle == null)
          this.subtitle = new StringType();
        this.subtitle.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of this evidence variable. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this evidence variable. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public EvidenceVariable setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this evidence variable. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this evidence variable. Enables tracking the life-cycle of the content.
     */
    public EvidenceVariable setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the evidence variable was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the evidence variable changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the evidence variable was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the evidence variable changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public EvidenceVariable setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the evidence variable was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the evidence variable changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the evidence variable was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the evidence variable changes.
     */
    public EvidenceVariable setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the organization or individual that published the evidence variable.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the organization or individual that published the evidence variable.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public EvidenceVariable setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the organization or individual that published the evidence variable.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the organization or individual that published the evidence variable.
     */
    public EvidenceVariable setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public EvidenceVariable addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the evidence variable from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the evidence variable from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public EvidenceVariable setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the evidence variable from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the evidence variable from a consumer's perspective.
     */
    public EvidenceVariable setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #note} (A human-readable string to clarify or explain concepts about the resource.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setNote(List<Annotation> theNote) { 
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

    public EvidenceVariable addNote(Annotation t) { //3
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate evidence variable instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public EvidenceVariable addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the evidence variable is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public EvidenceVariable addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public EvidenceVariable setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.
     */
    public EvidenceVariable setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public DateType getApprovalDateElement() { 
      if (this.approvalDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.approvalDate");
        else if (Configuration.doAutoCreate())
          this.approvalDate = new DateType(); // bb
      return this.approvalDate;
    }

    public boolean hasApprovalDateElement() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    public boolean hasApprovalDate() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    /**
     * @param value {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public EvidenceVariable setApprovalDateElement(DateType value) { 
      this.approvalDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Date getApprovalDate() { 
      return this.approvalDate == null ? null : this.approvalDate.getValue();
    }

    /**
     * @param value The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public EvidenceVariable setApprovalDate(Date value) { 
      if (value == null)
        this.approvalDate = null;
      else {
        if (this.approvalDate == null)
          this.approvalDate = new DateType();
        this.approvalDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.lastReviewDate");
        else if (Configuration.doAutoCreate())
          this.lastReviewDate = new DateType(); // bb
      return this.lastReviewDate;
    }

    public boolean hasLastReviewDateElement() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    public boolean hasLastReviewDate() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    /**
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public EvidenceVariable setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    public EvidenceVariable setLastReviewDate(Date value) { 
      if (value == null)
        this.lastReviewDate = null;
      else {
        if (this.lastReviewDate == null)
          this.lastReviewDate = new DateType();
        this.lastReviewDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #effectivePeriod} (The period during which the evidence variable content was or is planned to be in active use.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the evidence variable content was or is planned to be in active use.)
     */
    public EvidenceVariable setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #topic} (Descriptive topics related to the content of the EvidenceVariable. Topics provide a high-level categorization grouping types of EvidenceVariables that can be useful for filtering and searching.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setTopic(List<CodeableConcept> theTopic) { 
      this.topic = theTopic;
      return this;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (CodeableConcept item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTopic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return t;
    }

    public EvidenceVariable addTopic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
     */
    public CodeableConcept getTopicFirstRep() { 
      if (getTopic().isEmpty()) {
        addTopic();
      }
      return getTopic().get(0);
    }

    /**
     * @return {@link #author} (An individiual or organization primarily involved in the creation and maintenance of the content.)
     */
    public List<ContactDetail> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<ContactDetail>();
      return this.author;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setAuthor(List<ContactDetail> theAuthor) { 
      this.author = theAuthor;
      return this;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (ContactDetail item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addAuthor() { //3
      ContactDetail t = new ContactDetail();
      if (this.author == null)
        this.author = new ArrayList<ContactDetail>();
      this.author.add(t);
      return t;
    }

    public EvidenceVariable addAuthor(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.author == null)
        this.author = new ArrayList<ContactDetail>();
      this.author.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #author}, creating it if it does not already exist
     */
    public ContactDetail getAuthorFirstRep() { 
      if (getAuthor().isEmpty()) {
        addAuthor();
      }
      return getAuthor().get(0);
    }

    /**
     * @return {@link #editor} (An individual or organization primarily responsible for internal coherence of the content.)
     */
    public List<ContactDetail> getEditor() { 
      if (this.editor == null)
        this.editor = new ArrayList<ContactDetail>();
      return this.editor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setEditor(List<ContactDetail> theEditor) { 
      this.editor = theEditor;
      return this;
    }

    public boolean hasEditor() { 
      if (this.editor == null)
        return false;
      for (ContactDetail item : this.editor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addEditor() { //3
      ContactDetail t = new ContactDetail();
      if (this.editor == null)
        this.editor = new ArrayList<ContactDetail>();
      this.editor.add(t);
      return t;
    }

    public EvidenceVariable addEditor(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.editor == null)
        this.editor = new ArrayList<ContactDetail>();
      this.editor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #editor}, creating it if it does not already exist
     */
    public ContactDetail getEditorFirstRep() { 
      if (getEditor().isEmpty()) {
        addEditor();
      }
      return getEditor().get(0);
    }

    /**
     * @return {@link #reviewer} (An individual or organization primarily responsible for review of some aspect of the content.)
     */
    public List<ContactDetail> getReviewer() { 
      if (this.reviewer == null)
        this.reviewer = new ArrayList<ContactDetail>();
      return this.reviewer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setReviewer(List<ContactDetail> theReviewer) { 
      this.reviewer = theReviewer;
      return this;
    }

    public boolean hasReviewer() { 
      if (this.reviewer == null)
        return false;
      for (ContactDetail item : this.reviewer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addReviewer() { //3
      ContactDetail t = new ContactDetail();
      if (this.reviewer == null)
        this.reviewer = new ArrayList<ContactDetail>();
      this.reviewer.add(t);
      return t;
    }

    public EvidenceVariable addReviewer(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.reviewer == null)
        this.reviewer = new ArrayList<ContactDetail>();
      this.reviewer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reviewer}, creating it if it does not already exist
     */
    public ContactDetail getReviewerFirstRep() { 
      if (getReviewer().isEmpty()) {
        addReviewer();
      }
      return getReviewer().get(0);
    }

    /**
     * @return {@link #endorser} (An individual or organization responsible for officially endorsing the content for use in some setting.)
     */
    public List<ContactDetail> getEndorser() { 
      if (this.endorser == null)
        this.endorser = new ArrayList<ContactDetail>();
      return this.endorser;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setEndorser(List<ContactDetail> theEndorser) { 
      this.endorser = theEndorser;
      return this;
    }

    public boolean hasEndorser() { 
      if (this.endorser == null)
        return false;
      for (ContactDetail item : this.endorser)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addEndorser() { //3
      ContactDetail t = new ContactDetail();
      if (this.endorser == null)
        this.endorser = new ArrayList<ContactDetail>();
      this.endorser.add(t);
      return t;
    }

    public EvidenceVariable addEndorser(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.endorser == null)
        this.endorser = new ArrayList<ContactDetail>();
      this.endorser.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #endorser}, creating it if it does not already exist
     */
    public ContactDetail getEndorserFirstRep() { 
      if (getEndorser().isEmpty()) {
        addEndorser();
      }
      return getEndorser().get(0);
    }

    /**
     * @return {@link #relatedArtifact} (Related artifacts such as additional documentation, justification, or bibliographic references.)
     */
    public List<RelatedArtifact> getRelatedArtifact() { 
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      return this.relatedArtifact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
      this.relatedArtifact = theRelatedArtifact;
      return this;
    }

    public boolean hasRelatedArtifact() { 
      if (this.relatedArtifact == null)
        return false;
      for (RelatedArtifact item : this.relatedArtifact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedArtifact addRelatedArtifact() { //3
      RelatedArtifact t = new RelatedArtifact();
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return t;
    }

    public EvidenceVariable addRelatedArtifact(RelatedArtifact t) { //3
      if (t == null)
        return this;
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedArtifact}, creating it if it does not already exist
     */
    public RelatedArtifact getRelatedArtifactFirstRep() { 
      if (getRelatedArtifact().isEmpty()) {
        addRelatedArtifact();
      }
      return getRelatedArtifact().get(0);
    }

    /**
     * @return {@link #type} (The type of evidence element, a population, an exposure, or an outcome.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<EvidenceVariableType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EvidenceVariable.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<EvidenceVariableType>(new EvidenceVariableTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of evidence element, a population, an exposure, or an outcome.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public EvidenceVariable setTypeElement(Enumeration<EvidenceVariableType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of evidence element, a population, an exposure, or an outcome.
     */
    public EvidenceVariableType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of evidence element, a population, an exposure, or an outcome.
     */
    public EvidenceVariable setType(EvidenceVariableType value) { 
      if (value == null)
        this.type = null;
      else {
        if (this.type == null)
          this.type = new Enumeration<EvidenceVariableType>(new EvidenceVariableTypeEnumFactory());
        this.type.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #characteristic} (A characteristic that defines the members of the evidence element. Multiple characteristics are applied with "and" semantics.)
     */
    public List<EvidenceVariableCharacteristicComponent> getCharacteristic() { 
      if (this.characteristic == null)
        this.characteristic = new ArrayList<EvidenceVariableCharacteristicComponent>();
      return this.characteristic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EvidenceVariable setCharacteristic(List<EvidenceVariableCharacteristicComponent> theCharacteristic) { 
      this.characteristic = theCharacteristic;
      return this;
    }

    public boolean hasCharacteristic() { 
      if (this.characteristic == null)
        return false;
      for (EvidenceVariableCharacteristicComponent item : this.characteristic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public EvidenceVariableCharacteristicComponent addCharacteristic() { //3
      EvidenceVariableCharacteristicComponent t = new EvidenceVariableCharacteristicComponent();
      if (this.characteristic == null)
        this.characteristic = new ArrayList<EvidenceVariableCharacteristicComponent>();
      this.characteristic.add(t);
      return t;
    }

    public EvidenceVariable addCharacteristic(EvidenceVariableCharacteristicComponent t) { //3
      if (t == null)
        return this;
      if (this.characteristic == null)
        this.characteristic = new ArrayList<EvidenceVariableCharacteristicComponent>();
      this.characteristic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #characteristic}, creating it if it does not already exist
     */
    public EvidenceVariableCharacteristicComponent getCharacteristicFirstRep() { 
      if (getCharacteristic().isEmpty()) {
        addCharacteristic();
      }
      return getCharacteristic().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different servers.", 0, 1, url));
        children.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this evidence variable when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the evidence variable when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the evidence variable. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("title", "string", "A short, descriptive, user-friendly title for the evidence variable.", 0, 1, title));
        children.add(new Property("shortTitle", "string", "The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.", 0, 1, shortTitle));
        children.add(new Property("subtitle", "string", "An explanatory or alternate title for the EvidenceVariable giving additional information about its content.", 0, 1, subtitle));
        children.add(new Property("status", "code", "The status of this evidence variable. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the evidence variable was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the evidence variable changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the organization or individual that published the evidence variable.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the evidence variable from a consumer's perspective.", 0, 1, description));
        children.add(new Property("note", "Annotation", "A human-readable string to clarify or explain concepts about the resource.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate evidence variable instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the evidence variable is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.", 0, 1, copyright));
        children.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate));
        children.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate));
        children.add(new Property("effectivePeriod", "Period", "The period during which the evidence variable content was or is planned to be in active use.", 0, 1, effectivePeriod));
        children.add(new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the EvidenceVariable. Topics provide a high-level categorization grouping types of EvidenceVariables that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic));
        children.add(new Property("author", "ContactDetail", "An individiual or organization primarily involved in the creation and maintenance of the content.", 0, java.lang.Integer.MAX_VALUE, author));
        children.add(new Property("editor", "ContactDetail", "An individual or organization primarily responsible for internal coherence of the content.", 0, java.lang.Integer.MAX_VALUE, editor));
        children.add(new Property("reviewer", "ContactDetail", "An individual or organization primarily responsible for review of some aspect of the content.", 0, java.lang.Integer.MAX_VALUE, reviewer));
        children.add(new Property("endorser", "ContactDetail", "An individual or organization responsible for officially endorsing the content for use in some setting.", 0, java.lang.Integer.MAX_VALUE, endorser));
        children.add(new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        children.add(new Property("type", "code", "The type of evidence element, a population, an exposure, or an outcome.", 0, 1, type));
        children.add(new Property("characteristic", "", "A characteristic that defines the members of the evidence element. Multiple characteristics are applied with \"and\" semantics.", 0, java.lang.Integer.MAX_VALUE, characteristic));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different servers.", 0, 1, url);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A formal identifier that is used to identify this evidence variable when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the evidence variable when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the evidence variable. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive, user-friendly title for the evidence variable.", 0, 1, title);
        case 1555503932: /*shortTitle*/  return new Property("shortTitle", "string", "The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.", 0, 1, shortTitle);
        case -2060497896: /*subtitle*/  return new Property("subtitle", "string", "An explanatory or alternate title for the EvidenceVariable giving additional information about its content.", 0, 1, subtitle);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this evidence variable. Enables tracking the life-cycle of the content.", 0, 1, status);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the evidence variable was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the evidence variable changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the organization or individual that published the evidence variable.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the evidence variable from a consumer's perspective.", 0, 1, description);
        case 3387378: /*note*/  return new Property("note", "Annotation", "A human-readable string to clarify or explain concepts about the resource.", 0, java.lang.Integer.MAX_VALUE, note);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate evidence variable instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the evidence variable is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the evidence variable and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the evidence variable.", 0, 1, copyright);
        case 223539345: /*approvalDate*/  return new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate);
        case -1687512484: /*lastReviewDate*/  return new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate);
        case -403934648: /*effectivePeriod*/  return new Property("effectivePeriod", "Period", "The period during which the evidence variable content was or is planned to be in active use.", 0, 1, effectivePeriod);
        case 110546223: /*topic*/  return new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the EvidenceVariable. Topics provide a high-level categorization grouping types of EvidenceVariables that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic);
        case -1406328437: /*author*/  return new Property("author", "ContactDetail", "An individiual or organization primarily involved in the creation and maintenance of the content.", 0, java.lang.Integer.MAX_VALUE, author);
        case -1307827859: /*editor*/  return new Property("editor", "ContactDetail", "An individual or organization primarily responsible for internal coherence of the content.", 0, java.lang.Integer.MAX_VALUE, editor);
        case -261190139: /*reviewer*/  return new Property("reviewer", "ContactDetail", "An individual or organization primarily responsible for review of some aspect of the content.", 0, java.lang.Integer.MAX_VALUE, reviewer);
        case 1740277666: /*endorser*/  return new Property("endorser", "ContactDetail", "An individual or organization responsible for officially endorsing the content for use in some setting.", 0, java.lang.Integer.MAX_VALUE, endorser);
        case 666807069: /*relatedArtifact*/  return new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact);
        case 3575610: /*type*/  return new Property("type", "code", "The type of evidence element, a population, an exposure, or an outcome.", 0, 1, type);
        case 366313883: /*characteristic*/  return new Property("characteristic", "", "A characteristic that defines the members of the evidence element. Multiple characteristics are applied with \"and\" semantics.", 0, java.lang.Integer.MAX_VALUE, characteristic);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 1555503932: /*shortTitle*/ return this.shortTitle == null ? new Base[0] : new Base[] {this.shortTitle}; // StringType
        case -2060497896: /*subtitle*/ return this.subtitle == null ? new Base[0] : new Base[] {this.subtitle}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : this.author.toArray(new Base[this.author.size()]); // ContactDetail
        case -1307827859: /*editor*/ return this.editor == null ? new Base[0] : this.editor.toArray(new Base[this.editor.size()]); // ContactDetail
        case -261190139: /*reviewer*/ return this.reviewer == null ? new Base[0] : this.reviewer.toArray(new Base[this.reviewer.size()]); // ContactDetail
        case 1740277666: /*endorser*/ return this.endorser == null ? new Base[0] : this.endorser.toArray(new Base[this.endorser.size()]); // ContactDetail
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<EvidenceVariableType>
        case 366313883: /*characteristic*/ return this.characteristic == null ? new Base[0] : this.characteristic.toArray(new Base[this.characteristic.size()]); // EvidenceVariableCharacteristicComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 1555503932: // shortTitle
          this.shortTitle = castToString(value); // StringType
          return value;
        case -2060497896: // subtitle
          this.subtitle = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 223539345: // approvalDate
          this.approvalDate = castToDate(value); // DateType
          return value;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          return value;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          return value;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1406328437: // author
          this.getAuthor().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1307827859: // editor
          this.getEditor().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -261190139: // reviewer
          this.getReviewer().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 1740277666: // endorser
          this.getEndorser().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case 3575610: // type
          value = new EvidenceVariableTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<EvidenceVariableType>
          return value;
        case 366313883: // characteristic
          this.getCharacteristic().add((EvidenceVariableCharacteristicComponent) value); // EvidenceVariableCharacteristicComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("shortTitle")) {
          this.shortTitle = castToString(value); // StringType
        } else if (name.equals("subtitle")) {
          this.subtitle = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("topic")) {
          this.getTopic().add(castToCodeableConcept(value));
        } else if (name.equals("author")) {
          this.getAuthor().add(castToContactDetail(value));
        } else if (name.equals("editor")) {
          this.getEditor().add(castToContactDetail(value));
        } else if (name.equals("reviewer")) {
          this.getReviewer().add(castToContactDetail(value));
        } else if (name.equals("endorser")) {
          this.getEndorser().add(castToContactDetail(value));
        } else if (name.equals("relatedArtifact")) {
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        } else if (name.equals("type")) {
          value = new EvidenceVariableTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<EvidenceVariableType>
        } else if (name.equals("characteristic")) {
          this.getCharacteristic().add((EvidenceVariableCharacteristicComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return addIdentifier(); 
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case 1555503932:  return getShortTitleElement();
        case -2060497896:  return getSubtitleElement();
        case -892481550:  return getStatusElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case 3387378:  return addNote(); 
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 1522889671:  return getCopyrightElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case 110546223:  return addTopic(); 
        case -1406328437:  return addAuthor(); 
        case -1307827859:  return addEditor(); 
        case -261190139:  return addReviewer(); 
        case 1740277666:  return addEndorser(); 
        case 666807069:  return addRelatedArtifact(); 
        case 3575610:  return getTypeElement();
        case 366313883:  return addCharacteristic(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 1555503932: /*shortTitle*/ return new String[] {"string"};
        case -2060497896: /*subtitle*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case 110546223: /*topic*/ return new String[] {"CodeableConcept"};
        case -1406328437: /*author*/ return new String[] {"ContactDetail"};
        case -1307827859: /*editor*/ return new String[] {"ContactDetail"};
        case -261190139: /*reviewer*/ return new String[] {"ContactDetail"};
        case 1740277666: /*endorser*/ return new String[] {"ContactDetail"};
        case 666807069: /*relatedArtifact*/ return new String[] {"RelatedArtifact"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 366313883: /*characteristic*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.title");
        }
        else if (name.equals("shortTitle")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.shortTitle");
        }
        else if (name.equals("subtitle")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.subtitle");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.status");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.description");
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.copyright");
        }
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("author")) {
          return addAuthor();
        }
        else if (name.equals("editor")) {
          return addEditor();
        }
        else if (name.equals("reviewer")) {
          return addReviewer();
        }
        else if (name.equals("endorser")) {
          return addEndorser();
        }
        else if (name.equals("relatedArtifact")) {
          return addRelatedArtifact();
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type EvidenceVariable.type");
        }
        else if (name.equals("characteristic")) {
          return addCharacteristic();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EvidenceVariable";

  }

      public EvidenceVariable copy() {
        EvidenceVariable dst = new EvidenceVariable();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.shortTitle = shortTitle == null ? null : shortTitle.copy();
        dst.subtitle = subtitle == null ? null : subtitle.copy();
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (author != null) {
          dst.author = new ArrayList<ContactDetail>();
          for (ContactDetail i : author)
            dst.author.add(i.copy());
        };
        if (editor != null) {
          dst.editor = new ArrayList<ContactDetail>();
          for (ContactDetail i : editor)
            dst.editor.add(i.copy());
        };
        if (reviewer != null) {
          dst.reviewer = new ArrayList<ContactDetail>();
          for (ContactDetail i : reviewer)
            dst.reviewer.add(i.copy());
        };
        if (endorser != null) {
          dst.endorser = new ArrayList<ContactDetail>();
          for (ContactDetail i : endorser)
            dst.endorser.add(i.copy());
        };
        if (relatedArtifact != null) {
          dst.relatedArtifact = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : relatedArtifact)
            dst.relatedArtifact.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (characteristic != null) {
          dst.characteristic = new ArrayList<EvidenceVariableCharacteristicComponent>();
          for (EvidenceVariableCharacteristicComponent i : characteristic)
            dst.characteristic.add(i.copy());
        };
        return dst;
      }

      protected EvidenceVariable typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof EvidenceVariable))
          return false;
        EvidenceVariable o = (EvidenceVariable) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(shortTitle, o.shortTitle, true)
           && compareDeep(subtitle, o.subtitle, true) && compareDeep(note, o.note, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(topic, o.topic, true) && compareDeep(author, o.author, true)
           && compareDeep(editor, o.editor, true) && compareDeep(reviewer, o.reviewer, true) && compareDeep(endorser, o.endorser, true)
           && compareDeep(relatedArtifact, o.relatedArtifact, true) && compareDeep(type, o.type, true) && compareDeep(characteristic, o.characteristic, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof EvidenceVariable))
          return false;
        EvidenceVariable o = (EvidenceVariable) other_;
        return compareValues(shortTitle, o.shortTitle, true) && compareValues(subtitle, o.subtitle, true) && compareValues(copyright, o.copyright, true)
           && compareValues(approvalDate, o.approvalDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
           && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, shortTitle, subtitle
          , note, copyright, approvalDate, lastReviewDate, effectivePeriod, topic, author
          , editor, reviewer, endorser, relatedArtifact, type, characteristic);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EvidenceVariable;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The evidence variable publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EvidenceVariable.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="EvidenceVariable.date", description="The evidence variable publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The evidence variable publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EvidenceVariable.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EvidenceVariable.identifier", description="External identifier for the evidence variable", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="successor", path="EvidenceVariable.relatedArtifact.where(type='successor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_SUCCESSOR = "successor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUCCESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUCCESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EvidenceVariable:successor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUCCESSOR = new ca.uhn.fhir.model.api.Include("EvidenceVariable:successor").toLocked();

 /**
   * Search parameter: <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the evidence variable</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-value", path="EvidenceVariable.useContext", description="A use context type and value assigned to the evidence variable", type="composite", compositeOf={"context-type", "context"} )
  public static final String SP_CONTEXT_TYPE_VALUE = "context-type-value";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the evidence variable</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> CONTEXT_TYPE_VALUE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_CONTEXT_TYPE_VALUE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="EvidenceVariable.jurisdiction", description="Intended jurisdiction for the evidence variable", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="EvidenceVariable.description", description="The description of the evidence variable", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="derived-from", path="EvidenceVariable.relatedArtifact.where(type='derived-from').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_DERIVED_FROM = "derived-from";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DERIVED_FROM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DERIVED_FROM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EvidenceVariable:derived-from</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DERIVED_FROM = new ca.uhn.fhir.model.api.Include("EvidenceVariable:derived-from").toLocked();

 /**
   * Search parameter: <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.useContext.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type", path="EvidenceVariable.useContext.code", description="A type of use context assigned to the evidence variable", type="token" )
  public static final String SP_CONTEXT_TYPE = "context-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.useContext.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT_TYPE);

 /**
   * Search parameter: <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="predecessor", path="EvidenceVariable.relatedArtifact.where(type='predecessor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_PREDECESSOR = "predecessor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PREDECESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PREDECESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EvidenceVariable:predecessor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PREDECESSOR = new ca.uhn.fhir.model.api.Include("EvidenceVariable:predecessor").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="EvidenceVariable.title", description="The human-friendly name of the evidence variable", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="composed-of", path="EvidenceVariable.relatedArtifact.where(type='composed-of').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_COMPOSED_OF = "composed-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPOSED_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPOSED_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EvidenceVariable:composed-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPOSED_OF = new ca.uhn.fhir.model.api.Include("EvidenceVariable:composed-of").toLocked();

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="EvidenceVariable.version", description="The business version of the evidence variable", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the evidence variable</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>EvidenceVariable.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="EvidenceVariable.url", description="The uri that identifies the evidence variable", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the evidence variable</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>EvidenceVariable.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the evidence variable</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>EvidenceVariable.useContext.valueQuantity, EvidenceVariable.useContext.valueRange</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-quantity", path="(EvidenceVariable.useContext.value as Quantity) | (EvidenceVariable.useContext.value as Range)", description="A quantity- or range-valued use context assigned to the evidence variable", type="quantity" )
  public static final String SP_CONTEXT_QUANTITY = "context-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the evidence variable</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>EvidenceVariable.useContext.valueQuantity, EvidenceVariable.useContext.valueRange</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam CONTEXT_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_CONTEXT_QUANTITY);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the evidence variable is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EvidenceVariable.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="EvidenceVariable.effectivePeriod", description="The time during which the evidence variable is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the evidence variable is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EvidenceVariable.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="depends-on", path="EvidenceVariable.relatedArtifact.where(type='depends-on').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_DEPENDS_ON = "depends-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EvidenceVariable.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDS_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDS_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EvidenceVariable:depends-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDS_ON = new ca.uhn.fhir.model.api.Include("EvidenceVariable:depends-on").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="EvidenceVariable.name", description="Computationally friendly name of the evidence variable", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="(EvidenceVariable.useContext.value as CodeableConcept)", description="A use context assigned to the evidence variable", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="EvidenceVariable.publisher", description="Name of the publisher of the evidence variable", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the evidence variable</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EvidenceVariable.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the EvidenceVariable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="EvidenceVariable.topic", description="Topics associated with the EvidenceVariable", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the EvidenceVariable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the evidence variable</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-quantity", path="EvidenceVariable.useContext", description="A use context type and quantity- or range-based value assigned to the evidence variable", type="composite", compositeOf={"context-type", "context-quantity"} )
  public static final String SP_CONTEXT_TYPE_QUANTITY = "context-type-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the evidence variable</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> CONTEXT_TYPE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_CONTEXT_TYPE_QUANTITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="EvidenceVariable.status", description="The current status of the evidence variable", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the evidence variable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EvidenceVariable.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

