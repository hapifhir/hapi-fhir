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
 * The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion, recommendation) is about.
 */
@ResourceDef(name="ResearchElementDefinition", profile="http://hl7.org/fhir/StructureDefinition/ResearchElementDefinition")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "shortTitle", "subtitle", "status", "experimental", "subject[x]", "date", "publisher", "contact", "description", "comment", "useContext", "jurisdiction", "purpose", "usage", "copyright", "approvalDate", "lastReviewDate", "effectivePeriod", "topic", "author", "editor", "reviewer", "endorser", "relatedArtifact", "library", "type", "variableType", "characteristic"})
public class ResearchElementDefinition extends MetadataResource {

    public enum ResearchElementType {
        /**
         * The element defines the population that forms the basis for research.
         */
        POPULATION, 
        /**
         * The element defines an exposure within the population that is being researched.
         */
        EXPOSURE, 
        /**
         * The element defines an outcome within the population that is being researched.
         */
        OUTCOME, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ResearchElementType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("population".equals(codeString))
          return POPULATION;
        if ("exposure".equals(codeString))
          return EXPOSURE;
        if ("outcome".equals(codeString))
          return OUTCOME;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ResearchElementType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case POPULATION: return "population";
            case EXPOSURE: return "exposure";
            case OUTCOME: return "outcome";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case POPULATION: return "http://hl7.org/fhir/research-element-type";
            case EXPOSURE: return "http://hl7.org/fhir/research-element-type";
            case OUTCOME: return "http://hl7.org/fhir/research-element-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case POPULATION: return "The element defines the population that forms the basis for research.";
            case EXPOSURE: return "The element defines an exposure within the population that is being researched.";
            case OUTCOME: return "The element defines an outcome within the population that is being researched.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case POPULATION: return "Population";
            case EXPOSURE: return "Exposure";
            case OUTCOME: return "Outcome";
            default: return "?";
          }
        }
    }

  public static class ResearchElementTypeEnumFactory implements EnumFactory<ResearchElementType> {
    public ResearchElementType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("population".equals(codeString))
          return ResearchElementType.POPULATION;
        if ("exposure".equals(codeString))
          return ResearchElementType.EXPOSURE;
        if ("outcome".equals(codeString))
          return ResearchElementType.OUTCOME;
        throw new IllegalArgumentException("Unknown ResearchElementType code '"+codeString+"'");
        }
        public Enumeration<ResearchElementType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ResearchElementType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("population".equals(codeString))
          return new Enumeration<ResearchElementType>(this, ResearchElementType.POPULATION);
        if ("exposure".equals(codeString))
          return new Enumeration<ResearchElementType>(this, ResearchElementType.EXPOSURE);
        if ("outcome".equals(codeString))
          return new Enumeration<ResearchElementType>(this, ResearchElementType.OUTCOME);
        throw new FHIRException("Unknown ResearchElementType code '"+codeString+"'");
        }
    public String toCode(ResearchElementType code) {
      if (code == ResearchElementType.POPULATION)
        return "population";
      if (code == ResearchElementType.EXPOSURE)
        return "exposure";
      if (code == ResearchElementType.OUTCOME)
        return "outcome";
      return "?";
      }
    public String toSystem(ResearchElementType code) {
      return code.getSystem();
      }
    }

    public enum VariableType {
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
        public static VariableType fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown VariableType code '"+codeString+"'");
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

  public static class VariableTypeEnumFactory implements EnumFactory<VariableType> {
    public VariableType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("dichotomous".equals(codeString))
          return VariableType.DICHOTOMOUS;
        if ("continuous".equals(codeString))
          return VariableType.CONTINUOUS;
        if ("descriptive".equals(codeString))
          return VariableType.DESCRIPTIVE;
        throw new IllegalArgumentException("Unknown VariableType code '"+codeString+"'");
        }
        public Enumeration<VariableType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<VariableType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("dichotomous".equals(codeString))
          return new Enumeration<VariableType>(this, VariableType.DICHOTOMOUS);
        if ("continuous".equals(codeString))
          return new Enumeration<VariableType>(this, VariableType.CONTINUOUS);
        if ("descriptive".equals(codeString))
          return new Enumeration<VariableType>(this, VariableType.DESCRIPTIVE);
        throw new FHIRException("Unknown VariableType code '"+codeString+"'");
        }
    public String toCode(VariableType code) {
      if (code == VariableType.DICHOTOMOUS)
        return "dichotomous";
      if (code == VariableType.CONTINUOUS)
        return "continuous";
      if (code == VariableType.DESCRIPTIVE)
        return "descriptive";
      return "?";
      }
    public String toSystem(VariableType code) {
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
    public static class ResearchElementDefinitionCharacteristicComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).
         */
        @Child(name = "definition", type = {CodeableConcept.class, CanonicalType.class, Expression.class, DataRequirement.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What code or expression defines members?", formalDefinition="Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year)." )
        protected Type definition;

        /**
         * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
         */
        @Child(name = "usageContext", type = {UsageContext.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What code/value pairs define members?", formalDefinition="Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings." )
        protected List<UsageContext> usageContext;

        /**
         * When true, members with this characteristic are excluded from the element.
         */
        @Child(name = "exclude", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the characteristic includes or excludes members", formalDefinition="When true, members with this characteristic are excluded from the element." )
        protected BooleanType exclude;

        /**
         * Specifies the UCUM unit for the outcome.
         */
        @Child(name = "unitOfMeasure", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What unit is the outcome described in?", formalDefinition="Specifies the UCUM unit for the outcome." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ucum-units")
        protected CodeableConcept unitOfMeasure;

        /**
         * A narrative description of the time period the study covers.
         */
        @Child(name = "studyEffectiveDescription", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What time period does the study cover", formalDefinition="A narrative description of the time period the study covers." )
        protected StringType studyEffectiveDescription;

        /**
         * Indicates what effective period the study covers.
         */
        @Child(name = "studyEffective", type = {DateTimeType.class, Period.class, Duration.class, Timing.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What time period does the study cover", formalDefinition="Indicates what effective period the study covers." )
        protected Type studyEffective;

        /**
         * Indicates duration from the study initiation.
         */
        @Child(name = "studyEffectiveTimeFromStart", type = {Duration.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Observation time from study start", formalDefinition="Indicates duration from the study initiation." )
        protected Duration studyEffectiveTimeFromStart;

        /**
         * Indicates how elements are aggregated within the study effective period.
         */
        @Child(name = "studyEffectiveGroupMeasure", type = {CodeType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="mean | median | mean-of-mean | mean-of-median | median-of-mean | median-of-median", formalDefinition="Indicates how elements are aggregated within the study effective period." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/group-measure")
        protected Enumeration<GroupMeasure> studyEffectiveGroupMeasure;

        /**
         * A narrative description of the time period the study covers.
         */
        @Child(name = "participantEffectiveDescription", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What time period do participants cover", formalDefinition="A narrative description of the time period the study covers." )
        protected StringType participantEffectiveDescription;

        /**
         * Indicates what effective period the study covers.
         */
        @Child(name = "participantEffective", type = {DateTimeType.class, Period.class, Duration.class, Timing.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What time period do participants cover", formalDefinition="Indicates what effective period the study covers." )
        protected Type participantEffective;

        /**
         * Indicates duration from the participant's study entry.
         */
        @Child(name = "participantEffectiveTimeFromStart", type = {Duration.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Observation time from study start", formalDefinition="Indicates duration from the participant's study entry." )
        protected Duration participantEffectiveTimeFromStart;

        /**
         * Indicates how elements are aggregated within the study effective period.
         */
        @Child(name = "participantEffectiveGroupMeasure", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="mean | median | mean-of-mean | mean-of-median | median-of-mean | median-of-median", formalDefinition="Indicates how elements are aggregated within the study effective period." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/group-measure")
        protected Enumeration<GroupMeasure> participantEffectiveGroupMeasure;

        private static final long serialVersionUID = -1102952665L;

    /**
     * Constructor
     */
      public ResearchElementDefinitionCharacteristicComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ResearchElementDefinitionCharacteristicComponent(Type definition) {
        super();
        this.definition = definition;
      }

        /**
         * @return {@link #definition} (Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public Type getDefinition() { 
          return this.definition;
        }

        /**
         * @return {@link #definition} (Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
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
         * @return {@link #definition} (Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
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
         * @return {@link #definition} (Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
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
         * @return {@link #definition} (Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
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

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).)
         */
        public ResearchElementDefinitionCharacteristicComponent setDefinition(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof CanonicalType || value instanceof Expression || value instanceof DataRequirement))
            throw new Error("Not the right type for ResearchElementDefinition.characteristic.definition[x]: "+value.fhirType());
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
        public ResearchElementDefinitionCharacteristicComponent setUsageContext(List<UsageContext> theUsageContext) { 
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

        public ResearchElementDefinitionCharacteristicComponent addUsageContext(UsageContext t) { //3
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
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.exclude");
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
        public ResearchElementDefinitionCharacteristicComponent setExcludeElement(BooleanType value) { 
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
        public ResearchElementDefinitionCharacteristicComponent setExclude(boolean value) { 
            if (this.exclude == null)
              this.exclude = new BooleanType();
            this.exclude.setValue(value);
          return this;
        }

        /**
         * @return {@link #unitOfMeasure} (Specifies the UCUM unit for the outcome.)
         */
        public CodeableConcept getUnitOfMeasure() { 
          if (this.unitOfMeasure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.unitOfMeasure");
            else if (Configuration.doAutoCreate())
              this.unitOfMeasure = new CodeableConcept(); // cc
          return this.unitOfMeasure;
        }

        public boolean hasUnitOfMeasure() { 
          return this.unitOfMeasure != null && !this.unitOfMeasure.isEmpty();
        }

        /**
         * @param value {@link #unitOfMeasure} (Specifies the UCUM unit for the outcome.)
         */
        public ResearchElementDefinitionCharacteristicComponent setUnitOfMeasure(CodeableConcept value) { 
          this.unitOfMeasure = value;
          return this;
        }

        /**
         * @return {@link #studyEffectiveDescription} (A narrative description of the time period the study covers.). This is the underlying object with id, value and extensions. The accessor "getStudyEffectiveDescription" gives direct access to the value
         */
        public StringType getStudyEffectiveDescriptionElement() { 
          if (this.studyEffectiveDescription == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.studyEffectiveDescription");
            else if (Configuration.doAutoCreate())
              this.studyEffectiveDescription = new StringType(); // bb
          return this.studyEffectiveDescription;
        }

        public boolean hasStudyEffectiveDescriptionElement() { 
          return this.studyEffectiveDescription != null && !this.studyEffectiveDescription.isEmpty();
        }

        public boolean hasStudyEffectiveDescription() { 
          return this.studyEffectiveDescription != null && !this.studyEffectiveDescription.isEmpty();
        }

        /**
         * @param value {@link #studyEffectiveDescription} (A narrative description of the time period the study covers.). This is the underlying object with id, value and extensions. The accessor "getStudyEffectiveDescription" gives direct access to the value
         */
        public ResearchElementDefinitionCharacteristicComponent setStudyEffectiveDescriptionElement(StringType value) { 
          this.studyEffectiveDescription = value;
          return this;
        }

        /**
         * @return A narrative description of the time period the study covers.
         */
        public String getStudyEffectiveDescription() { 
          return this.studyEffectiveDescription == null ? null : this.studyEffectiveDescription.getValue();
        }

        /**
         * @param value A narrative description of the time period the study covers.
         */
        public ResearchElementDefinitionCharacteristicComponent setStudyEffectiveDescription(String value) { 
          if (Utilities.noString(value))
            this.studyEffectiveDescription = null;
          else {
            if (this.studyEffectiveDescription == null)
              this.studyEffectiveDescription = new StringType();
            this.studyEffectiveDescription.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #studyEffective} (Indicates what effective period the study covers.)
         */
        public Type getStudyEffective() { 
          return this.studyEffective;
        }

        /**
         * @return {@link #studyEffective} (Indicates what effective period the study covers.)
         */
        public DateTimeType getStudyEffectiveDateTimeType() throws FHIRException { 
          if (this.studyEffective == null)
            this.studyEffective = new DateTimeType();
          if (!(this.studyEffective instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.studyEffective.getClass().getName()+" was encountered");
          return (DateTimeType) this.studyEffective;
        }

        public boolean hasStudyEffectiveDateTimeType() { 
          return this != null && this.studyEffective instanceof DateTimeType;
        }

        /**
         * @return {@link #studyEffective} (Indicates what effective period the study covers.)
         */
        public Period getStudyEffectivePeriod() throws FHIRException { 
          if (this.studyEffective == null)
            this.studyEffective = new Period();
          if (!(this.studyEffective instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.studyEffective.getClass().getName()+" was encountered");
          return (Period) this.studyEffective;
        }

        public boolean hasStudyEffectivePeriod() { 
          return this != null && this.studyEffective instanceof Period;
        }

        /**
         * @return {@link #studyEffective} (Indicates what effective period the study covers.)
         */
        public Duration getStudyEffectiveDuration() throws FHIRException { 
          if (this.studyEffective == null)
            this.studyEffective = new Duration();
          if (!(this.studyEffective instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.studyEffective.getClass().getName()+" was encountered");
          return (Duration) this.studyEffective;
        }

        public boolean hasStudyEffectiveDuration() { 
          return this != null && this.studyEffective instanceof Duration;
        }

        /**
         * @return {@link #studyEffective} (Indicates what effective period the study covers.)
         */
        public Timing getStudyEffectiveTiming() throws FHIRException { 
          if (this.studyEffective == null)
            this.studyEffective = new Timing();
          if (!(this.studyEffective instanceof Timing))
            throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.studyEffective.getClass().getName()+" was encountered");
          return (Timing) this.studyEffective;
        }

        public boolean hasStudyEffectiveTiming() { 
          return this != null && this.studyEffective instanceof Timing;
        }

        public boolean hasStudyEffective() { 
          return this.studyEffective != null && !this.studyEffective.isEmpty();
        }

        /**
         * @param value {@link #studyEffective} (Indicates what effective period the study covers.)
         */
        public ResearchElementDefinitionCharacteristicComponent setStudyEffective(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Duration || value instanceof Timing))
            throw new Error("Not the right type for ResearchElementDefinition.characteristic.studyEffective[x]: "+value.fhirType());
          this.studyEffective = value;
          return this;
        }

        /**
         * @return {@link #studyEffectiveTimeFromStart} (Indicates duration from the study initiation.)
         */
        public Duration getStudyEffectiveTimeFromStart() { 
          if (this.studyEffectiveTimeFromStart == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.studyEffectiveTimeFromStart");
            else if (Configuration.doAutoCreate())
              this.studyEffectiveTimeFromStart = new Duration(); // cc
          return this.studyEffectiveTimeFromStart;
        }

        public boolean hasStudyEffectiveTimeFromStart() { 
          return this.studyEffectiveTimeFromStart != null && !this.studyEffectiveTimeFromStart.isEmpty();
        }

        /**
         * @param value {@link #studyEffectiveTimeFromStart} (Indicates duration from the study initiation.)
         */
        public ResearchElementDefinitionCharacteristicComponent setStudyEffectiveTimeFromStart(Duration value) { 
          this.studyEffectiveTimeFromStart = value;
          return this;
        }

        /**
         * @return {@link #studyEffectiveGroupMeasure} (Indicates how elements are aggregated within the study effective period.). This is the underlying object with id, value and extensions. The accessor "getStudyEffectiveGroupMeasure" gives direct access to the value
         */
        public Enumeration<GroupMeasure> getStudyEffectiveGroupMeasureElement() { 
          if (this.studyEffectiveGroupMeasure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.studyEffectiveGroupMeasure");
            else if (Configuration.doAutoCreate())
              this.studyEffectiveGroupMeasure = new Enumeration<GroupMeasure>(new GroupMeasureEnumFactory()); // bb
          return this.studyEffectiveGroupMeasure;
        }

        public boolean hasStudyEffectiveGroupMeasureElement() { 
          return this.studyEffectiveGroupMeasure != null && !this.studyEffectiveGroupMeasure.isEmpty();
        }

        public boolean hasStudyEffectiveGroupMeasure() { 
          return this.studyEffectiveGroupMeasure != null && !this.studyEffectiveGroupMeasure.isEmpty();
        }

        /**
         * @param value {@link #studyEffectiveGroupMeasure} (Indicates how elements are aggregated within the study effective period.). This is the underlying object with id, value and extensions. The accessor "getStudyEffectiveGroupMeasure" gives direct access to the value
         */
        public ResearchElementDefinitionCharacteristicComponent setStudyEffectiveGroupMeasureElement(Enumeration<GroupMeasure> value) { 
          this.studyEffectiveGroupMeasure = value;
          return this;
        }

        /**
         * @return Indicates how elements are aggregated within the study effective period.
         */
        public GroupMeasure getStudyEffectiveGroupMeasure() { 
          return this.studyEffectiveGroupMeasure == null ? null : this.studyEffectiveGroupMeasure.getValue();
        }

        /**
         * @param value Indicates how elements are aggregated within the study effective period.
         */
        public ResearchElementDefinitionCharacteristicComponent setStudyEffectiveGroupMeasure(GroupMeasure value) { 
          if (value == null)
            this.studyEffectiveGroupMeasure = null;
          else {
            if (this.studyEffectiveGroupMeasure == null)
              this.studyEffectiveGroupMeasure = new Enumeration<GroupMeasure>(new GroupMeasureEnumFactory());
            this.studyEffectiveGroupMeasure.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #participantEffectiveDescription} (A narrative description of the time period the study covers.). This is the underlying object with id, value and extensions. The accessor "getParticipantEffectiveDescription" gives direct access to the value
         */
        public StringType getParticipantEffectiveDescriptionElement() { 
          if (this.participantEffectiveDescription == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.participantEffectiveDescription");
            else if (Configuration.doAutoCreate())
              this.participantEffectiveDescription = new StringType(); // bb
          return this.participantEffectiveDescription;
        }

        public boolean hasParticipantEffectiveDescriptionElement() { 
          return this.participantEffectiveDescription != null && !this.participantEffectiveDescription.isEmpty();
        }

        public boolean hasParticipantEffectiveDescription() { 
          return this.participantEffectiveDescription != null && !this.participantEffectiveDescription.isEmpty();
        }

        /**
         * @param value {@link #participantEffectiveDescription} (A narrative description of the time period the study covers.). This is the underlying object with id, value and extensions. The accessor "getParticipantEffectiveDescription" gives direct access to the value
         */
        public ResearchElementDefinitionCharacteristicComponent setParticipantEffectiveDescriptionElement(StringType value) { 
          this.participantEffectiveDescription = value;
          return this;
        }

        /**
         * @return A narrative description of the time period the study covers.
         */
        public String getParticipantEffectiveDescription() { 
          return this.participantEffectiveDescription == null ? null : this.participantEffectiveDescription.getValue();
        }

        /**
         * @param value A narrative description of the time period the study covers.
         */
        public ResearchElementDefinitionCharacteristicComponent setParticipantEffectiveDescription(String value) { 
          if (Utilities.noString(value))
            this.participantEffectiveDescription = null;
          else {
            if (this.participantEffectiveDescription == null)
              this.participantEffectiveDescription = new StringType();
            this.participantEffectiveDescription.setValue(value);
          }
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
        public ResearchElementDefinitionCharacteristicComponent setParticipantEffective(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Duration || value instanceof Timing))
            throw new Error("Not the right type for ResearchElementDefinition.characteristic.participantEffective[x]: "+value.fhirType());
          this.participantEffective = value;
          return this;
        }

        /**
         * @return {@link #participantEffectiveTimeFromStart} (Indicates duration from the participant's study entry.)
         */
        public Duration getParticipantEffectiveTimeFromStart() { 
          if (this.participantEffectiveTimeFromStart == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.participantEffectiveTimeFromStart");
            else if (Configuration.doAutoCreate())
              this.participantEffectiveTimeFromStart = new Duration(); // cc
          return this.participantEffectiveTimeFromStart;
        }

        public boolean hasParticipantEffectiveTimeFromStart() { 
          return this.participantEffectiveTimeFromStart != null && !this.participantEffectiveTimeFromStart.isEmpty();
        }

        /**
         * @param value {@link #participantEffectiveTimeFromStart} (Indicates duration from the participant's study entry.)
         */
        public ResearchElementDefinitionCharacteristicComponent setParticipantEffectiveTimeFromStart(Duration value) { 
          this.participantEffectiveTimeFromStart = value;
          return this;
        }

        /**
         * @return {@link #participantEffectiveGroupMeasure} (Indicates how elements are aggregated within the study effective period.). This is the underlying object with id, value and extensions. The accessor "getParticipantEffectiveGroupMeasure" gives direct access to the value
         */
        public Enumeration<GroupMeasure> getParticipantEffectiveGroupMeasureElement() { 
          if (this.participantEffectiveGroupMeasure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchElementDefinitionCharacteristicComponent.participantEffectiveGroupMeasure");
            else if (Configuration.doAutoCreate())
              this.participantEffectiveGroupMeasure = new Enumeration<GroupMeasure>(new GroupMeasureEnumFactory()); // bb
          return this.participantEffectiveGroupMeasure;
        }

        public boolean hasParticipantEffectiveGroupMeasureElement() { 
          return this.participantEffectiveGroupMeasure != null && !this.participantEffectiveGroupMeasure.isEmpty();
        }

        public boolean hasParticipantEffectiveGroupMeasure() { 
          return this.participantEffectiveGroupMeasure != null && !this.participantEffectiveGroupMeasure.isEmpty();
        }

        /**
         * @param value {@link #participantEffectiveGroupMeasure} (Indicates how elements are aggregated within the study effective period.). This is the underlying object with id, value and extensions. The accessor "getParticipantEffectiveGroupMeasure" gives direct access to the value
         */
        public ResearchElementDefinitionCharacteristicComponent setParticipantEffectiveGroupMeasureElement(Enumeration<GroupMeasure> value) { 
          this.participantEffectiveGroupMeasure = value;
          return this;
        }

        /**
         * @return Indicates how elements are aggregated within the study effective period.
         */
        public GroupMeasure getParticipantEffectiveGroupMeasure() { 
          return this.participantEffectiveGroupMeasure == null ? null : this.participantEffectiveGroupMeasure.getValue();
        }

        /**
         * @param value Indicates how elements are aggregated within the study effective period.
         */
        public ResearchElementDefinitionCharacteristicComponent setParticipantEffectiveGroupMeasure(GroupMeasure value) { 
          if (value == null)
            this.participantEffectiveGroupMeasure = null;
          else {
            if (this.participantEffectiveGroupMeasure == null)
              this.participantEffectiveGroupMeasure = new Enumeration<GroupMeasure>(new GroupMeasureEnumFactory());
            this.participantEffectiveGroupMeasure.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition));
          children.add(new Property("usageContext", "UsageContext", "Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.", 0, java.lang.Integer.MAX_VALUE, usageContext));
          children.add(new Property("exclude", "boolean", "When true, members with this characteristic are excluded from the element.", 0, 1, exclude));
          children.add(new Property("unitOfMeasure", "CodeableConcept", "Specifies the UCUM unit for the outcome.", 0, 1, unitOfMeasure));
          children.add(new Property("studyEffectiveDescription", "string", "A narrative description of the time period the study covers.", 0, 1, studyEffectiveDescription));
          children.add(new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective));
          children.add(new Property("studyEffectiveTimeFromStart", "Duration", "Indicates duration from the study initiation.", 0, 1, studyEffectiveTimeFromStart));
          children.add(new Property("studyEffectiveGroupMeasure", "code", "Indicates how elements are aggregated within the study effective period.", 0, 1, studyEffectiveGroupMeasure));
          children.add(new Property("participantEffectiveDescription", "string", "A narrative description of the time period the study covers.", 0, 1, participantEffectiveDescription));
          children.add(new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective));
          children.add(new Property("participantEffectiveTimeFromStart", "Duration", "Indicates duration from the participant's study entry.", 0, 1, participantEffectiveTimeFromStart));
          children.add(new Property("participantEffectiveGroupMeasure", "code", "Indicates how elements are aggregated within the study effective period.", 0, 1, participantEffectiveGroupMeasure));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1139422643: /*definition[x]*/  return new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -1014418093: /*definition*/  return new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -1446002226: /*definitionCodeableConcept*/  return new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case 933485793: /*definitionCanonical*/  return new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case 1463703627: /*definitionExpression*/  return new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case -660350874: /*definitionDataRequirement*/  return new Property("definition[x]", "CodeableConcept|canonical(ValueSet)|Expression|DataRequirement", "Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the last year).", 0, 1, definition);
          case 907012302: /*usageContext*/  return new Property("usageContext", "UsageContext", "Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.", 0, java.lang.Integer.MAX_VALUE, usageContext);
          case -1321148966: /*exclude*/  return new Property("exclude", "boolean", "When true, members with this characteristic are excluded from the element.", 0, 1, exclude);
          case -750257565: /*unitOfMeasure*/  return new Property("unitOfMeasure", "CodeableConcept", "Specifies the UCUM unit for the outcome.", 0, 1, unitOfMeasure);
          case 237553470: /*studyEffectiveDescription*/  return new Property("studyEffectiveDescription", "string", "A narrative description of the time period the study covers.", 0, 1, studyEffectiveDescription);
          case -1832549918: /*studyEffective[x]*/  return new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective);
          case -836391458: /*studyEffective*/  return new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective);
          case 439780249: /*studyEffectiveDateTime*/  return new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective);
          case -497045185: /*studyEffectivePeriod*/  return new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective);
          case 949985682: /*studyEffectiveDuration*/  return new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective);
          case -378983480: /*studyEffectiveTiming*/  return new Property("studyEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, studyEffective);
          case -2107828915: /*studyEffectiveTimeFromStart*/  return new Property("studyEffectiveTimeFromStart", "Duration", "Indicates duration from the study initiation.", 0, 1, studyEffectiveTimeFromStart);
          case 1284435677: /*studyEffectiveGroupMeasure*/  return new Property("studyEffectiveGroupMeasure", "code", "Indicates how elements are aggregated within the study effective period.", 0, 1, studyEffectiveGroupMeasure);
          case 1333186472: /*participantEffectiveDescription*/  return new Property("participantEffectiveDescription", "string", "A narrative description of the time period the study covers.", 0, 1, participantEffectiveDescription);
          case 1777308748: /*participantEffective[x]*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case 1376306100: /*participantEffective*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -1721146513: /*participantEffectiveDateTime*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -883650923: /*participantEffectivePeriod*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -1210941080: /*participantEffectiveDuration*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -765589218: /*participantEffectiveTiming*/  return new Property("participantEffective[x]", "dateTime|Period|Duration|Timing", "Indicates what effective period the study covers.", 0, 1, participantEffective);
          case -1471501513: /*participantEffectiveTimeFromStart*/  return new Property("participantEffectiveTimeFromStart", "Duration", "Indicates duration from the participant's study entry.", 0, 1, participantEffectiveTimeFromStart);
          case 889320371: /*participantEffectiveGroupMeasure*/  return new Property("participantEffectiveGroupMeasure", "code", "Indicates how elements are aggregated within the study effective period.", 0, 1, participantEffectiveGroupMeasure);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Type
        case 907012302: /*usageContext*/ return this.usageContext == null ? new Base[0] : this.usageContext.toArray(new Base[this.usageContext.size()]); // UsageContext
        case -1321148966: /*exclude*/ return this.exclude == null ? new Base[0] : new Base[] {this.exclude}; // BooleanType
        case -750257565: /*unitOfMeasure*/ return this.unitOfMeasure == null ? new Base[0] : new Base[] {this.unitOfMeasure}; // CodeableConcept
        case 237553470: /*studyEffectiveDescription*/ return this.studyEffectiveDescription == null ? new Base[0] : new Base[] {this.studyEffectiveDescription}; // StringType
        case -836391458: /*studyEffective*/ return this.studyEffective == null ? new Base[0] : new Base[] {this.studyEffective}; // Type
        case -2107828915: /*studyEffectiveTimeFromStart*/ return this.studyEffectiveTimeFromStart == null ? new Base[0] : new Base[] {this.studyEffectiveTimeFromStart}; // Duration
        case 1284435677: /*studyEffectiveGroupMeasure*/ return this.studyEffectiveGroupMeasure == null ? new Base[0] : new Base[] {this.studyEffectiveGroupMeasure}; // Enumeration<GroupMeasure>
        case 1333186472: /*participantEffectiveDescription*/ return this.participantEffectiveDescription == null ? new Base[0] : new Base[] {this.participantEffectiveDescription}; // StringType
        case 1376306100: /*participantEffective*/ return this.participantEffective == null ? new Base[0] : new Base[] {this.participantEffective}; // Type
        case -1471501513: /*participantEffectiveTimeFromStart*/ return this.participantEffectiveTimeFromStart == null ? new Base[0] : new Base[] {this.participantEffectiveTimeFromStart}; // Duration
        case 889320371: /*participantEffectiveGroupMeasure*/ return this.participantEffectiveGroupMeasure == null ? new Base[0] : new Base[] {this.participantEffectiveGroupMeasure}; // Enumeration<GroupMeasure>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1014418093: // definition
          this.definition = castToType(value); // Type
          return value;
        case 907012302: // usageContext
          this.getUsageContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -1321148966: // exclude
          this.exclude = castToBoolean(value); // BooleanType
          return value;
        case -750257565: // unitOfMeasure
          this.unitOfMeasure = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 237553470: // studyEffectiveDescription
          this.studyEffectiveDescription = castToString(value); // StringType
          return value;
        case -836391458: // studyEffective
          this.studyEffective = castToType(value); // Type
          return value;
        case -2107828915: // studyEffectiveTimeFromStart
          this.studyEffectiveTimeFromStart = castToDuration(value); // Duration
          return value;
        case 1284435677: // studyEffectiveGroupMeasure
          value = new GroupMeasureEnumFactory().fromType(castToCode(value));
          this.studyEffectiveGroupMeasure = (Enumeration) value; // Enumeration<GroupMeasure>
          return value;
        case 1333186472: // participantEffectiveDescription
          this.participantEffectiveDescription = castToString(value); // StringType
          return value;
        case 1376306100: // participantEffective
          this.participantEffective = castToType(value); // Type
          return value;
        case -1471501513: // participantEffectiveTimeFromStart
          this.participantEffectiveTimeFromStart = castToDuration(value); // Duration
          return value;
        case 889320371: // participantEffectiveGroupMeasure
          value = new GroupMeasureEnumFactory().fromType(castToCode(value));
          this.participantEffectiveGroupMeasure = (Enumeration) value; // Enumeration<GroupMeasure>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("definition[x]")) {
          this.definition = castToType(value); // Type
        } else if (name.equals("usageContext")) {
          this.getUsageContext().add(castToUsageContext(value));
        } else if (name.equals("exclude")) {
          this.exclude = castToBoolean(value); // BooleanType
        } else if (name.equals("unitOfMeasure")) {
          this.unitOfMeasure = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("studyEffectiveDescription")) {
          this.studyEffectiveDescription = castToString(value); // StringType
        } else if (name.equals("studyEffective[x]")) {
          this.studyEffective = castToType(value); // Type
        } else if (name.equals("studyEffectiveTimeFromStart")) {
          this.studyEffectiveTimeFromStart = castToDuration(value); // Duration
        } else if (name.equals("studyEffectiveGroupMeasure")) {
          value = new GroupMeasureEnumFactory().fromType(castToCode(value));
          this.studyEffectiveGroupMeasure = (Enumeration) value; // Enumeration<GroupMeasure>
        } else if (name.equals("participantEffectiveDescription")) {
          this.participantEffectiveDescription = castToString(value); // StringType
        } else if (name.equals("participantEffective[x]")) {
          this.participantEffective = castToType(value); // Type
        } else if (name.equals("participantEffectiveTimeFromStart")) {
          this.participantEffectiveTimeFromStart = castToDuration(value); // Duration
        } else if (name.equals("participantEffectiveGroupMeasure")) {
          value = new GroupMeasureEnumFactory().fromType(castToCode(value));
          this.participantEffectiveGroupMeasure = (Enumeration) value; // Enumeration<GroupMeasure>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1139422643:  return getDefinition(); 
        case -1014418093:  return getDefinition(); 
        case 907012302:  return addUsageContext(); 
        case -1321148966:  return getExcludeElement();
        case -750257565:  return getUnitOfMeasure(); 
        case 237553470:  return getStudyEffectiveDescriptionElement();
        case -1832549918:  return getStudyEffective(); 
        case -836391458:  return getStudyEffective(); 
        case -2107828915:  return getStudyEffectiveTimeFromStart(); 
        case 1284435677:  return getStudyEffectiveGroupMeasureElement();
        case 1333186472:  return getParticipantEffectiveDescriptionElement();
        case 1777308748:  return getParticipantEffective(); 
        case 1376306100:  return getParticipantEffective(); 
        case -1471501513:  return getParticipantEffectiveTimeFromStart(); 
        case 889320371:  return getParticipantEffectiveGroupMeasureElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1014418093: /*definition*/ return new String[] {"CodeableConcept", "canonical", "Expression", "DataRequirement"};
        case 907012302: /*usageContext*/ return new String[] {"UsageContext"};
        case -1321148966: /*exclude*/ return new String[] {"boolean"};
        case -750257565: /*unitOfMeasure*/ return new String[] {"CodeableConcept"};
        case 237553470: /*studyEffectiveDescription*/ return new String[] {"string"};
        case -836391458: /*studyEffective*/ return new String[] {"dateTime", "Period", "Duration", "Timing"};
        case -2107828915: /*studyEffectiveTimeFromStart*/ return new String[] {"Duration"};
        case 1284435677: /*studyEffectiveGroupMeasure*/ return new String[] {"code"};
        case 1333186472: /*participantEffectiveDescription*/ return new String[] {"string"};
        case 1376306100: /*participantEffective*/ return new String[] {"dateTime", "Period", "Duration", "Timing"};
        case -1471501513: /*participantEffectiveTimeFromStart*/ return new String[] {"Duration"};
        case 889320371: /*participantEffectiveGroupMeasure*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("definitionCodeableConcept")) {
          this.definition = new CodeableConcept();
          return this.definition;
        }
        else if (name.equals("definitionCanonical")) {
          this.definition = new CanonicalType();
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
        else if (name.equals("usageContext")) {
          return addUsageContext();
        }
        else if (name.equals("exclude")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.exclude");
        }
        else if (name.equals("unitOfMeasure")) {
          this.unitOfMeasure = new CodeableConcept();
          return this.unitOfMeasure;
        }
        else if (name.equals("studyEffectiveDescription")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.studyEffectiveDescription");
        }
        else if (name.equals("studyEffectiveDateTime")) {
          this.studyEffective = new DateTimeType();
          return this.studyEffective;
        }
        else if (name.equals("studyEffectivePeriod")) {
          this.studyEffective = new Period();
          return this.studyEffective;
        }
        else if (name.equals("studyEffectiveDuration")) {
          this.studyEffective = new Duration();
          return this.studyEffective;
        }
        else if (name.equals("studyEffectiveTiming")) {
          this.studyEffective = new Timing();
          return this.studyEffective;
        }
        else if (name.equals("studyEffectiveTimeFromStart")) {
          this.studyEffectiveTimeFromStart = new Duration();
          return this.studyEffectiveTimeFromStart;
        }
        else if (name.equals("studyEffectiveGroupMeasure")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.studyEffectiveGroupMeasure");
        }
        else if (name.equals("participantEffectiveDescription")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.participantEffectiveDescription");
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
        else if (name.equals("participantEffectiveTimeFromStart")) {
          this.participantEffectiveTimeFromStart = new Duration();
          return this.participantEffectiveTimeFromStart;
        }
        else if (name.equals("participantEffectiveGroupMeasure")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.participantEffectiveGroupMeasure");
        }
        else
          return super.addChild(name);
      }

      public ResearchElementDefinitionCharacteristicComponent copy() {
        ResearchElementDefinitionCharacteristicComponent dst = new ResearchElementDefinitionCharacteristicComponent();
        copyValues(dst);
        dst.definition = definition == null ? null : definition.copy();
        if (usageContext != null) {
          dst.usageContext = new ArrayList<UsageContext>();
          for (UsageContext i : usageContext)
            dst.usageContext.add(i.copy());
        };
        dst.exclude = exclude == null ? null : exclude.copy();
        dst.unitOfMeasure = unitOfMeasure == null ? null : unitOfMeasure.copy();
        dst.studyEffectiveDescription = studyEffectiveDescription == null ? null : studyEffectiveDescription.copy();
        dst.studyEffective = studyEffective == null ? null : studyEffective.copy();
        dst.studyEffectiveTimeFromStart = studyEffectiveTimeFromStart == null ? null : studyEffectiveTimeFromStart.copy();
        dst.studyEffectiveGroupMeasure = studyEffectiveGroupMeasure == null ? null : studyEffectiveGroupMeasure.copy();
        dst.participantEffectiveDescription = participantEffectiveDescription == null ? null : participantEffectiveDescription.copy();
        dst.participantEffective = participantEffective == null ? null : participantEffective.copy();
        dst.participantEffectiveTimeFromStart = participantEffectiveTimeFromStart == null ? null : participantEffectiveTimeFromStart.copy();
        dst.participantEffectiveGroupMeasure = participantEffectiveGroupMeasure == null ? null : participantEffectiveGroupMeasure.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ResearchElementDefinitionCharacteristicComponent))
          return false;
        ResearchElementDefinitionCharacteristicComponent o = (ResearchElementDefinitionCharacteristicComponent) other_;
        return compareDeep(definition, o.definition, true) && compareDeep(usageContext, o.usageContext, true)
           && compareDeep(exclude, o.exclude, true) && compareDeep(unitOfMeasure, o.unitOfMeasure, true) && compareDeep(studyEffectiveDescription, o.studyEffectiveDescription, true)
           && compareDeep(studyEffective, o.studyEffective, true) && compareDeep(studyEffectiveTimeFromStart, o.studyEffectiveTimeFromStart, true)
           && compareDeep(studyEffectiveGroupMeasure, o.studyEffectiveGroupMeasure, true) && compareDeep(participantEffectiveDescription, o.participantEffectiveDescription, true)
           && compareDeep(participantEffective, o.participantEffective, true) && compareDeep(participantEffectiveTimeFromStart, o.participantEffectiveTimeFromStart, true)
           && compareDeep(participantEffectiveGroupMeasure, o.participantEffectiveGroupMeasure, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ResearchElementDefinitionCharacteristicComponent))
          return false;
        ResearchElementDefinitionCharacteristicComponent o = (ResearchElementDefinitionCharacteristicComponent) other_;
        return compareValues(exclude, o.exclude, true) && compareValues(studyEffectiveDescription, o.studyEffectiveDescription, true)
           && compareValues(studyEffectiveGroupMeasure, o.studyEffectiveGroupMeasure, true) && compareValues(participantEffectiveDescription, o.participantEffectiveDescription, true)
           && compareValues(participantEffectiveGroupMeasure, o.participantEffectiveGroupMeasure, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(definition, usageContext, exclude
          , unitOfMeasure, studyEffectiveDescription, studyEffective, studyEffectiveTimeFromStart
          , studyEffectiveGroupMeasure, participantEffectiveDescription, participantEffective, participantEffectiveTimeFromStart
          , participantEffectiveGroupMeasure);
      }

  public String fhirType() {
    return "ResearchElementDefinition.characteristic";

  }

  }

    /**
     * A formal identifier that is used to identify this research element definition when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the research element definition", formalDefinition="A formal identifier that is used to identify this research element definition when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.
     */
    @Child(name = "shortTitle", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Title for use in informal contexts", formalDefinition="The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary." )
    protected StringType shortTitle;

    /**
     * An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
     */
    @Child(name = "subtitle", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Subordinate title of the ResearchElementDefinition", formalDefinition="An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content." )
    protected StringType subtitle;

    /**
     * The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.
     */
    @Child(name = "subject", type = {CodeableConcept.class, Group.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="E.g. Patient, Practitioner, RelatedPerson, Organization, Location, Device", formalDefinition="The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/subject-type")
    protected Type subject;

    /**
     * A human-readable string to clarify or explain concepts about the resource.
     */
    @Child(name = "comment", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Used for footnotes or explanatory notes", formalDefinition="A human-readable string to clarify or explain concepts about the resource." )
    protected List<StringType> comment;

    /**
     * Explanation of why this research element definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this research element definition is defined", formalDefinition="Explanation of why this research element definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
     */
    @Child(name = "usage", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the ResearchElementDefinition", formalDefinition="A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used." )
    protected StringType usage;

    /**
     * A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition." )
    protected MarkdownType copyright;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the research element definition was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the research element definition was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the research element definition content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the research element definition is expected to be used", formalDefinition="The period during which the research element definition content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The category of the ResearchElementDefinition, such as Education, Treatment, Assessment, etc.", formalDefinition="Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization grouping types of ResearchElementDefinitions that can be useful for filtering and searching." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/definition-topic")
    protected List<CodeableConcept> topic;

    /**
     * An individiual or organization primarily involved in the creation and maintenance of the content.
     */
    @Child(name = "author", type = {ContactDetail.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who authored the content", formalDefinition="An individiual or organization primarily involved in the creation and maintenance of the content." )
    protected List<ContactDetail> author;

    /**
     * An individual or organization primarily responsible for internal coherence of the content.
     */
    @Child(name = "editor", type = {ContactDetail.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who edited the content", formalDefinition="An individual or organization primarily responsible for internal coherence of the content." )
    protected List<ContactDetail> editor;

    /**
     * An individual or organization primarily responsible for review of some aspect of the content.
     */
    @Child(name = "reviewer", type = {ContactDetail.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who reviewed the content", formalDefinition="An individual or organization primarily responsible for review of some aspect of the content." )
    protected List<ContactDetail> reviewer;

    /**
     * An individual or organization responsible for officially endorsing the content for use in some setting.
     */
    @Child(name = "endorser", type = {ContactDetail.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who endorsed the content", formalDefinition="An individual or organization responsible for officially endorsing the content for use in some setting." )
    protected List<ContactDetail> endorser;

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional documentation, citations, etc.", formalDefinition="Related artifacts such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
     */
    @Child(name = "library", type = {CanonicalType.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the ResearchElementDefinition", formalDefinition="A reference to a Library resource containing the formal logic used by the ResearchElementDefinition." )
    protected List<CanonicalType> library;

    /**
     * The type of research element, a population, an exposure, or an outcome.
     */
    @Child(name = "type", type = {CodeType.class}, order=18, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="population | exposure | outcome", formalDefinition="The type of research element, a population, an exposure, or an outcome." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-element-type")
    protected Enumeration<ResearchElementType> type;

    /**
     * The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
     */
    @Child(name = "variableType", type = {CodeType.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="dichotomous | continuous | descriptive", formalDefinition="The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/variable-type")
    protected Enumeration<VariableType> variableType;

    /**
     * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" semantics.
     */
    @Child(name = "characteristic", type = {}, order=20, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What defines the members of the research element", formalDefinition="A characteristic that defines the members of the research element. Multiple characteristics are applied with \"and\" semantics." )
    protected List<ResearchElementDefinitionCharacteristicComponent> characteristic;

    private static final long serialVersionUID = 1483216033L;

  /**
   * Constructor
   */
    public ResearchElementDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ResearchElementDefinition(Enumeration<PublicationStatus> status, Enumeration<ResearchElementType> type) {
      super();
      this.status = status;
      this.type = type;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this research element definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this research element definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element definition is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this research element definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this research element definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element definition is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ResearchElementDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this research element definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this research element definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element definition is stored on different servers.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this research element definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this research element definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element definition is stored on different servers.
     */
    public ResearchElementDefinition setUrl(String value) { 
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
     * @return {@link #identifier} (A formal identifier that is used to identify this research element definition when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setIdentifier(List<Identifier> theIdentifier) { 
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

    public ResearchElementDefinition addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The identifier that is used to identify this version of the research element definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the research element definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the research element definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the research element definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ResearchElementDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the research element definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the research element definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the research element definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the research element definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public ResearchElementDefinition setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the research element definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.name");
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
     * @param value {@link #name} (A natural language name identifying the research element definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ResearchElementDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the research element definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the research element definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ResearchElementDefinition setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the research element definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the research element definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ResearchElementDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the research element definition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the research element definition.
     */
    public ResearchElementDefinition setTitle(String value) { 
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
          throw new Error("Attempt to auto-create ResearchElementDefinition.shortTitle");
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
    public ResearchElementDefinition setShortTitleElement(StringType value) { 
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
    public ResearchElementDefinition setShortTitle(String value) { 
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
     * @return {@link #subtitle} (An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getSubtitle" gives direct access to the value
     */
    public StringType getSubtitleElement() { 
      if (this.subtitle == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.subtitle");
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
     * @param value {@link #subtitle} (An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getSubtitle" gives direct access to the value
     */
    public ResearchElementDefinition setSubtitleElement(StringType value) { 
      this.subtitle = value;
      return this;
    }

    /**
     * @return An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
     */
    public String getSubtitle() { 
      return this.subtitle == null ? null : this.subtitle.getValue();
    }

    /**
     * @param value An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
     */
    public ResearchElementDefinition setSubtitle(String value) { 
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
     * @return {@link #status} (The status of this research element definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.status");
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
     * @param value {@link #status} (The status of this research element definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ResearchElementDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this research element definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this research element definition. Enables tracking the life-cycle of the content.
     */
    public ResearchElementDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A Boolean value to indicate that this research element definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A Boolean value to indicate that this research element definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ResearchElementDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A Boolean value to indicate that this research element definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A Boolean value to indicate that this research element definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public ResearchElementDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.)
     */
    public Type getSubject() { 
      return this.subject;
    }

    /**
     * @return {@link #subject} (The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.)
     */
    public CodeableConcept getSubjectCodeableConcept() throws FHIRException { 
      if (this.subject == null)
        this.subject = new CodeableConcept();
      if (!(this.subject instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.subject.getClass().getName()+" was encountered");
      return (CodeableConcept) this.subject;
    }

    public boolean hasSubjectCodeableConcept() { 
      return this != null && this.subject instanceof CodeableConcept;
    }

    /**
     * @return {@link #subject} (The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.)
     */
    public Reference getSubjectReference() throws FHIRException { 
      if (this.subject == null)
        this.subject = new Reference();
      if (!(this.subject instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.subject.getClass().getName()+" was encountered");
      return (Reference) this.subject;
    }

    public boolean hasSubjectReference() { 
      return this != null && this.subject instanceof Reference;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.)
     */
    public ResearchElementDefinition setSubject(Type value) { 
      if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
        throw new Error("Not the right type for ResearchElementDefinition.subject[x]: "+value.fhirType());
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the research element definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the research element definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the research element definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the research element definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ResearchElementDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the research element definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the research element definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the research element definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the research element definition changes.
     */
    public ResearchElementDefinition setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the organization or individual that published the research element definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.publisher");
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
     * @param value {@link #publisher} (The name of the organization or individual that published the research element definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ResearchElementDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the organization or individual that published the research element definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the organization or individual that published the research element definition.
     */
    public ResearchElementDefinition setPublisher(String value) { 
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
    public ResearchElementDefinition setContact(List<ContactDetail> theContact) { 
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

    public ResearchElementDefinition addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the research element definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the research element definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ResearchElementDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the research element definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the research element definition from a consumer's perspective.
     */
    public ResearchElementDefinition setDescription(String value) { 
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
     * @return {@link #comment} (A human-readable string to clarify or explain concepts about the resource.)
     */
    public List<StringType> getComment() { 
      if (this.comment == null)
        this.comment = new ArrayList<StringType>();
      return this.comment;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setComment(List<StringType> theComment) { 
      this.comment = theComment;
      return this;
    }

    public boolean hasComment() { 
      if (this.comment == null)
        return false;
      for (StringType item : this.comment)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #comment} (A human-readable string to clarify or explain concepts about the resource.)
     */
    public StringType addCommentElement() {//2 
      StringType t = new StringType();
      if (this.comment == null)
        this.comment = new ArrayList<StringType>();
      this.comment.add(t);
      return t;
    }

    /**
     * @param value {@link #comment} (A human-readable string to clarify or explain concepts about the resource.)
     */
    public ResearchElementDefinition addComment(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.comment == null)
        this.comment = new ArrayList<StringType>();
      this.comment.add(t);
      return this;
    }

    /**
     * @param value {@link #comment} (A human-readable string to clarify or explain concepts about the resource.)
     */
    public boolean hasComment(String value) { 
      if (this.comment == null)
        return false;
      for (StringType v : this.comment)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate research element definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setUseContext(List<UsageContext> theUseContext) { 
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

    public ResearchElementDefinition addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the research element definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public ResearchElementDefinition addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #purpose} (Explanation of why this research element definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explanation of why this research element definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public ResearchElementDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explanation of why this research element definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explanation of why this research element definition is needed and why it has been designed as it has.
     */
    public ResearchElementDefinition setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new StringType(); // bb
      return this.usage;
    }

    public boolean hasUsageElement() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public ResearchElementDefinition setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
     */
    public ResearchElementDefinition setUsage(String value) { 
      if (Utilities.noString(value))
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ResearchElementDefinition setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.
     */
    public ResearchElementDefinition setCopyright(String value) { 
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
          throw new Error("Attempt to auto-create ResearchElementDefinition.approvalDate");
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
    public ResearchElementDefinition setApprovalDateElement(DateType value) { 
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
    public ResearchElementDefinition setApprovalDate(Date value) { 
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
          throw new Error("Attempt to auto-create ResearchElementDefinition.lastReviewDate");
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
    public ResearchElementDefinition setLastReviewDateElement(DateType value) { 
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
    public ResearchElementDefinition setLastReviewDate(Date value) { 
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
     * @return {@link #effectivePeriod} (The period during which the research element definition content was or is planned to be in active use.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the research element definition content was or is planned to be in active use.)
     */
    public ResearchElementDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #topic} (Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization grouping types of ResearchElementDefinitions that can be useful for filtering and searching.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setTopic(List<CodeableConcept> theTopic) { 
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

    public ResearchElementDefinition addTopic(CodeableConcept t) { //3
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
    public ResearchElementDefinition setAuthor(List<ContactDetail> theAuthor) { 
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

    public ResearchElementDefinition addAuthor(ContactDetail t) { //3
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
    public ResearchElementDefinition setEditor(List<ContactDetail> theEditor) { 
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

    public ResearchElementDefinition addEditor(ContactDetail t) { //3
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
    public ResearchElementDefinition setReviewer(List<ContactDetail> theReviewer) { 
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

    public ResearchElementDefinition addReviewer(ContactDetail t) { //3
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
    public ResearchElementDefinition setEndorser(List<ContactDetail> theEndorser) { 
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

    public ResearchElementDefinition addEndorser(ContactDetail t) { //3
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
    public ResearchElementDefinition setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
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

    public ResearchElementDefinition addRelatedArtifact(RelatedArtifact t) { //3
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
     * @return {@link #library} (A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.)
     */
    public List<CanonicalType> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<CanonicalType>();
      return this.library;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setLibrary(List<CanonicalType> theLibrary) { 
      this.library = theLibrary;
      return this;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (CanonicalType item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.)
     */
    public CanonicalType addLibraryElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.library == null)
        this.library = new ArrayList<CanonicalType>();
      this.library.add(t);
      return t;
    }

    /**
     * @param value {@link #library} (A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.)
     */
    public ResearchElementDefinition addLibrary(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.library == null)
        this.library = new ArrayList<CanonicalType>();
      this.library.add(t);
      return this;
    }

    /**
     * @param value {@link #library} (A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.)
     */
    public boolean hasLibrary(String value) { 
      if (this.library == null)
        return false;
      for (CanonicalType v : this.library)
        if (v.getValue().equals(value)) // canonical(Library)
          return true;
      return false;
    }

    /**
     * @return {@link #type} (The type of research element, a population, an exposure, or an outcome.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ResearchElementType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ResearchElementType>(new ResearchElementTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of research element, a population, an exposure, or an outcome.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public ResearchElementDefinition setTypeElement(Enumeration<ResearchElementType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of research element, a population, an exposure, or an outcome.
     */
    public ResearchElementType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of research element, a population, an exposure, or an outcome.
     */
    public ResearchElementDefinition setType(ResearchElementType value) { 
        if (this.type == null)
          this.type = new Enumeration<ResearchElementType>(new ResearchElementTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #variableType} (The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).). This is the underlying object with id, value and extensions. The accessor "getVariableType" gives direct access to the value
     */
    public Enumeration<VariableType> getVariableTypeElement() { 
      if (this.variableType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchElementDefinition.variableType");
        else if (Configuration.doAutoCreate())
          this.variableType = new Enumeration<VariableType>(new VariableTypeEnumFactory()); // bb
      return this.variableType;
    }

    public boolean hasVariableTypeElement() { 
      return this.variableType != null && !this.variableType.isEmpty();
    }

    public boolean hasVariableType() { 
      return this.variableType != null && !this.variableType.isEmpty();
    }

    /**
     * @param value {@link #variableType} (The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).). This is the underlying object with id, value and extensions. The accessor "getVariableType" gives direct access to the value
     */
    public ResearchElementDefinition setVariableTypeElement(Enumeration<VariableType> value) { 
      this.variableType = value;
      return this;
    }

    /**
     * @return The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
     */
    public VariableType getVariableType() { 
      return this.variableType == null ? null : this.variableType.getValue();
    }

    /**
     * @param value The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
     */
    public ResearchElementDefinition setVariableType(VariableType value) { 
      if (value == null)
        this.variableType = null;
      else {
        if (this.variableType == null)
          this.variableType = new Enumeration<VariableType>(new VariableTypeEnumFactory());
        this.variableType.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #characteristic} (A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" semantics.)
     */
    public List<ResearchElementDefinitionCharacteristicComponent> getCharacteristic() { 
      if (this.characteristic == null)
        this.characteristic = new ArrayList<ResearchElementDefinitionCharacteristicComponent>();
      return this.characteristic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchElementDefinition setCharacteristic(List<ResearchElementDefinitionCharacteristicComponent> theCharacteristic) { 
      this.characteristic = theCharacteristic;
      return this;
    }

    public boolean hasCharacteristic() { 
      if (this.characteristic == null)
        return false;
      for (ResearchElementDefinitionCharacteristicComponent item : this.characteristic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ResearchElementDefinitionCharacteristicComponent addCharacteristic() { //3
      ResearchElementDefinitionCharacteristicComponent t = new ResearchElementDefinitionCharacteristicComponent();
      if (this.characteristic == null)
        this.characteristic = new ArrayList<ResearchElementDefinitionCharacteristicComponent>();
      this.characteristic.add(t);
      return t;
    }

    public ResearchElementDefinition addCharacteristic(ResearchElementDefinitionCharacteristicComponent t) { //3
      if (t == null)
        return this;
      if (this.characteristic == null)
        this.characteristic = new ArrayList<ResearchElementDefinitionCharacteristicComponent>();
      this.characteristic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #characteristic}, creating it if it does not already exist
     */
    public ResearchElementDefinitionCharacteristicComponent getCharacteristicFirstRep() { 
      if (getCharacteristic().isEmpty()) {
        addCharacteristic();
      }
      return getCharacteristic().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this research element definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this research element definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element definition is stored on different servers.", 0, 1, url));
        children.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this research element definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the research element definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the research element definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the research element definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("title", "string", "A short, descriptive, user-friendly title for the research element definition.", 0, 1, title));
        children.add(new Property("shortTitle", "string", "The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.", 0, 1, shortTitle));
        children.add(new Property("subtitle", "string", "An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.", 0, 1, subtitle));
        children.add(new Property("status", "code", "The status of this research element definition. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A Boolean value to indicate that this research element definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.", 0, 1, subject));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the research element definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the research element definition changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the organization or individual that published the research element definition.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the research element definition from a consumer's perspective.", 0, 1, description));
        children.add(new Property("comment", "string", "A human-readable string to clarify or explain concepts about the resource.", 0, java.lang.Integer.MAX_VALUE, comment));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate research element definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the research element definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("purpose", "markdown", "Explanation of why this research element definition is needed and why it has been designed as it has.", 0, 1, purpose));
        children.add(new Property("usage", "string", "A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.", 0, 1, usage));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.", 0, 1, copyright));
        children.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate));
        children.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate));
        children.add(new Property("effectivePeriod", "Period", "The period during which the research element definition content was or is planned to be in active use.", 0, 1, effectivePeriod));
        children.add(new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization grouping types of ResearchElementDefinitions that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic));
        children.add(new Property("author", "ContactDetail", "An individiual or organization primarily involved in the creation and maintenance of the content.", 0, java.lang.Integer.MAX_VALUE, author));
        children.add(new Property("editor", "ContactDetail", "An individual or organization primarily responsible for internal coherence of the content.", 0, java.lang.Integer.MAX_VALUE, editor));
        children.add(new Property("reviewer", "ContactDetail", "An individual or organization primarily responsible for review of some aspect of the content.", 0, java.lang.Integer.MAX_VALUE, reviewer));
        children.add(new Property("endorser", "ContactDetail", "An individual or organization responsible for officially endorsing the content for use in some setting.", 0, java.lang.Integer.MAX_VALUE, endorser));
        children.add(new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        children.add(new Property("library", "canonical(Library)", "A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.", 0, java.lang.Integer.MAX_VALUE, library));
        children.add(new Property("type", "code", "The type of research element, a population, an exposure, or an outcome.", 0, 1, type));
        children.add(new Property("variableType", "code", "The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).", 0, 1, variableType));
        children.add(new Property("characteristic", "", "A characteristic that defines the members of the research element. Multiple characteristics are applied with \"and\" semantics.", 0, java.lang.Integer.MAX_VALUE, characteristic));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this research element definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this research element definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element definition is stored on different servers.", 0, 1, url);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A formal identifier that is used to identify this research element definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the research element definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the research element definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the research element definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive, user-friendly title for the research element definition.", 0, 1, title);
        case 1555503932: /*shortTitle*/  return new Property("shortTitle", "string", "The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is not necessary.", 0, 1, shortTitle);
        case -2060497896: /*subtitle*/  return new Property("subtitle", "string", "An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.", 0, 1, subtitle);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this research element definition. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A Boolean value to indicate that this research element definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental);
        case -573640748: /*subject[x]*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.", 0, 1, subject);
        case -1867885268: /*subject*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.", 0, 1, subject);
        case -1257122603: /*subjectCodeableConcept*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.", 0, 1, subject);
        case 772938623: /*subjectReference*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is assumed, but the subject of the ResearchElementDefinition can be anything.", 0, 1, subject);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the research element definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the research element definition changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the organization or individual that published the research element definition.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the research element definition from a consumer's perspective.", 0, 1, description);
        case 950398559: /*comment*/  return new Property("comment", "string", "A human-readable string to clarify or explain concepts about the resource.", 0, java.lang.Integer.MAX_VALUE, comment);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate research element definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the research element definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case -220463842: /*purpose*/  return new Property("purpose", "markdown", "Explanation of why this research element definition is needed and why it has been designed as it has.", 0, 1, purpose);
        case 111574433: /*usage*/  return new Property("usage", "string", "A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.", 0, 1, usage);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the research element definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the research element definition.", 0, 1, copyright);
        case 223539345: /*approvalDate*/  return new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate);
        case -1687512484: /*lastReviewDate*/  return new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate);
        case -403934648: /*effectivePeriod*/  return new Property("effectivePeriod", "Period", "The period during which the research element definition content was or is planned to be in active use.", 0, 1, effectivePeriod);
        case 110546223: /*topic*/  return new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization grouping types of ResearchElementDefinitions that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic);
        case -1406328437: /*author*/  return new Property("author", "ContactDetail", "An individiual or organization primarily involved in the creation and maintenance of the content.", 0, java.lang.Integer.MAX_VALUE, author);
        case -1307827859: /*editor*/  return new Property("editor", "ContactDetail", "An individual or organization primarily responsible for internal coherence of the content.", 0, java.lang.Integer.MAX_VALUE, editor);
        case -261190139: /*reviewer*/  return new Property("reviewer", "ContactDetail", "An individual or organization primarily responsible for review of some aspect of the content.", 0, java.lang.Integer.MAX_VALUE, reviewer);
        case 1740277666: /*endorser*/  return new Property("endorser", "ContactDetail", "An individual or organization responsible for officially endorsing the content for use in some setting.", 0, java.lang.Integer.MAX_VALUE, endorser);
        case 666807069: /*relatedArtifact*/  return new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact);
        case 166208699: /*library*/  return new Property("library", "canonical(Library)", "A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.", 0, java.lang.Integer.MAX_VALUE, library);
        case 3575610: /*type*/  return new Property("type", "code", "The type of research element, a population, an exposure, or an outcome.", 0, 1, type);
        case -372820010: /*variableType*/  return new Property("variableType", "code", "The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).", 0, 1, variableType);
        case 366313883: /*characteristic*/  return new Property("characteristic", "", "A characteristic that defines the members of the research element. Multiple characteristics are applied with \"and\" semantics.", 0, java.lang.Integer.MAX_VALUE, characteristic);
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
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Type
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : this.comment.toArray(new Base[this.comment.size()]); // StringType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
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
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // CanonicalType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ResearchElementType>
        case -372820010: /*variableType*/ return this.variableType == null ? new Base[0] : new Base[] {this.variableType}; // Enumeration<VariableType>
        case 366313883: /*characteristic*/ return this.characteristic == null ? new Base[0] : this.characteristic.toArray(new Base[this.characteristic.size()]); // ResearchElementDefinitionCharacteristicComponent
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
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case -1867885268: // subject
          this.subject = castToType(value); // Type
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
        case 950398559: // comment
          this.getComment().add(castToString(value)); // StringType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
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
        case 166208699: // library
          this.getLibrary().add(castToCanonical(value)); // CanonicalType
          return value;
        case 3575610: // type
          value = new ResearchElementTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ResearchElementType>
          return value;
        case -372820010: // variableType
          value = new VariableTypeEnumFactory().fromType(castToCode(value));
          this.variableType = (Enumeration) value; // Enumeration<VariableType>
          return value;
        case 366313883: // characteristic
          this.getCharacteristic().add((ResearchElementDefinitionCharacteristicComponent) value); // ResearchElementDefinitionCharacteristicComponent
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
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("subject[x]")) {
          this.subject = castToType(value); // Type
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("comment")) {
          this.getComment().add(castToString(value));
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("usage")) {
          this.usage = castToString(value); // StringType
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
        } else if (name.equals("library")) {
          this.getLibrary().add(castToCanonical(value));
        } else if (name.equals("type")) {
          value = new ResearchElementTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ResearchElementType>
        } else if (name.equals("variableType")) {
          value = new VariableTypeEnumFactory().fromType(castToCode(value));
          this.variableType = (Enumeration) value; // Enumeration<VariableType>
        } else if (name.equals("characteristic")) {
          this.getCharacteristic().add((ResearchElementDefinitionCharacteristicComponent) value);
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
        case -404562712:  return getExperimentalElement();
        case -573640748:  return getSubject(); 
        case -1867885268:  return getSubject(); 
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case 950398559:  return addCommentElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 111574433:  return getUsageElement();
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
        case 166208699:  return addLibraryElement();
        case 3575610:  return getTypeElement();
        case -372820010:  return getVariableTypeElement();
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
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case -1867885268: /*subject*/ return new String[] {"CodeableConcept", "Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 950398559: /*comment*/ return new String[] {"string"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 111574433: /*usage*/ return new String[] {"string"};
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
        case 166208699: /*library*/ return new String[] {"canonical"};
        case 3575610: /*type*/ return new String[] {"code"};
        case -372820010: /*variableType*/ return new String[] {"code"};
        case 366313883: /*characteristic*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.title");
        }
        else if (name.equals("shortTitle")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.shortTitle");
        }
        else if (name.equals("subtitle")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.subtitle");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.experimental");
        }
        else if (name.equals("subjectCodeableConcept")) {
          this.subject = new CodeableConcept();
          return this.subject;
        }
        else if (name.equals("subjectReference")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.description");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.comment");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.usage");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.copyright");
        }
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.lastReviewDate");
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
        else if (name.equals("library")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.library");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.type");
        }
        else if (name.equals("variableType")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchElementDefinition.variableType");
        }
        else if (name.equals("characteristic")) {
          return addCharacteristic();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ResearchElementDefinition";

  }

      public ResearchElementDefinition copy() {
        ResearchElementDefinition dst = new ResearchElementDefinition();
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
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (comment != null) {
          dst.comment = new ArrayList<StringType>();
          for (StringType i : comment)
            dst.comment.add(i.copy());
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
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
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
        if (library != null) {
          dst.library = new ArrayList<CanonicalType>();
          for (CanonicalType i : library)
            dst.library.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.variableType = variableType == null ? null : variableType.copy();
        if (characteristic != null) {
          dst.characteristic = new ArrayList<ResearchElementDefinitionCharacteristicComponent>();
          for (ResearchElementDefinitionCharacteristicComponent i : characteristic)
            dst.characteristic.add(i.copy());
        };
        return dst;
      }

      protected ResearchElementDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ResearchElementDefinition))
          return false;
        ResearchElementDefinition o = (ResearchElementDefinition) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(shortTitle, o.shortTitle, true)
           && compareDeep(subtitle, o.subtitle, true) && compareDeep(subject, o.subject, true) && compareDeep(comment, o.comment, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(topic, o.topic, true) && compareDeep(author, o.author, true)
           && compareDeep(editor, o.editor, true) && compareDeep(reviewer, o.reviewer, true) && compareDeep(endorser, o.endorser, true)
           && compareDeep(relatedArtifact, o.relatedArtifact, true) && compareDeep(library, o.library, true)
           && compareDeep(type, o.type, true) && compareDeep(variableType, o.variableType, true) && compareDeep(characteristic, o.characteristic, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ResearchElementDefinition))
          return false;
        ResearchElementDefinition o = (ResearchElementDefinition) other_;
        return compareValues(shortTitle, o.shortTitle, true) && compareValues(subtitle, o.subtitle, true) && compareValues(comment, o.comment, true)
           && compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true) && compareValues(copyright, o.copyright, true)
           && compareValues(approvalDate, o.approvalDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
           && compareValues(type, o.type, true) && compareValues(variableType, o.variableType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, shortTitle, subtitle
          , subject, comment, purpose, usage, copyright, approvalDate, lastReviewDate, effectivePeriod
          , topic, author, editor, reviewer, endorser, relatedArtifact, library, type
          , variableType, characteristic);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ResearchElementDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The research element definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchElementDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ResearchElementDefinition.date", description="The research element definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The research element definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchElementDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ResearchElementDefinition.identifier", description="External identifier for the research element definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="successor", path="ResearchElementDefinition.relatedArtifact.where(type='successor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_SUCCESSOR = "successor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUCCESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUCCESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchElementDefinition:successor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUCCESSOR = new ca.uhn.fhir.model.api.Include("ResearchElementDefinition:successor").toLocked();

 /**
   * Search parameter: <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the research element definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-value", path="ResearchElementDefinition.useContext", description="A use context type and value assigned to the research element definition", type="composite", compositeOf={"context-type", "context"} )
  public static final String SP_CONTEXT_TYPE_VALUE = "context-type-value";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the research element definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> CONTEXT_TYPE_VALUE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_CONTEXT_TYPE_VALUE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ResearchElementDefinition.jurisdiction", description="Intended jurisdiction for the research element definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ResearchElementDefinition.description", description="The description of the research element definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="derived-from", path="ResearchElementDefinition.relatedArtifact.where(type='derived-from').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_DERIVED_FROM = "derived-from";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DERIVED_FROM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DERIVED_FROM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchElementDefinition:derived-from</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DERIVED_FROM = new ca.uhn.fhir.model.api.Include("ResearchElementDefinition:derived-from").toLocked();

 /**
   * Search parameter: <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.useContext.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type", path="ResearchElementDefinition.useContext.code", description="A type of use context assigned to the research element definition", type="token" )
  public static final String SP_CONTEXT_TYPE = "context-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.useContext.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT_TYPE);

 /**
   * Search parameter: <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="predecessor", path="ResearchElementDefinition.relatedArtifact.where(type='predecessor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_PREDECESSOR = "predecessor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PREDECESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PREDECESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchElementDefinition:predecessor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PREDECESSOR = new ca.uhn.fhir.model.api.Include("ResearchElementDefinition:predecessor").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ResearchElementDefinition.title", description="The human-friendly name of the research element definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="composed-of", path="ResearchElementDefinition.relatedArtifact.where(type='composed-of').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_COMPOSED_OF = "composed-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPOSED_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPOSED_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchElementDefinition:composed-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPOSED_OF = new ca.uhn.fhir.model.api.Include("ResearchElementDefinition:composed-of").toLocked();

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ResearchElementDefinition.version", description="The business version of the research element definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the research element definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ResearchElementDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ResearchElementDefinition.url", description="The uri that identifies the research element definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the research element definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ResearchElementDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the research element definition</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ResearchElementDefinition.useContext.valueQuantity, ResearchElementDefinition.useContext.valueRange</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-quantity", path="(ResearchElementDefinition.useContext.value as Quantity) | (ResearchElementDefinition.useContext.value as Range)", description="A quantity- or range-valued use context assigned to the research element definition", type="quantity" )
  public static final String SP_CONTEXT_QUANTITY = "context-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the research element definition</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ResearchElementDefinition.useContext.valueQuantity, ResearchElementDefinition.useContext.valueRange</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam CONTEXT_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_CONTEXT_QUANTITY);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the research element definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchElementDefinition.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="ResearchElementDefinition.effectivePeriod", description="The time during which the research element definition is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the research element definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchElementDefinition.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource, ResearchElementDefinition.library</b><br>
   * </p>
   */
  @SearchParamDefinition(name="depends-on", path="ResearchElementDefinition.relatedArtifact.where(type='depends-on').resource | ResearchElementDefinition.library", description="What resource is being referenced", type="reference" )
  public static final String SP_DEPENDS_ON = "depends-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchElementDefinition.relatedArtifact.resource, ResearchElementDefinition.library</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDS_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDS_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchElementDefinition:depends-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDS_ON = new ca.uhn.fhir.model.api.Include("ResearchElementDefinition:depends-on").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ResearchElementDefinition.name", description="Computationally friendly name of the research element definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="(ResearchElementDefinition.useContext.value as CodeableConcept)", description="A use context assigned to the research element definition", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ResearchElementDefinition.publisher", description="Name of the publisher of the research element definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the research element definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchElementDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the ResearchElementDefinition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="ResearchElementDefinition.topic", description="Topics associated with the ResearchElementDefinition", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the ResearchElementDefinition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the research element definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-quantity", path="ResearchElementDefinition.useContext", description="A use context type and quantity- or range-based value assigned to the research element definition", type="composite", compositeOf={"context-type", "context-quantity"} )
  public static final String SP_CONTEXT_TYPE_QUANTITY = "context-type-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the research element definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> CONTEXT_TYPE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_CONTEXT_TYPE_QUANTITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ResearchElementDefinition.status", description="The current status of the research element definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the research element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchElementDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

