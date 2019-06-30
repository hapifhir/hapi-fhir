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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the resources involved in that calculation.
 */
@ResourceDef(name="MeasureReport", profile="http://hl7.org/fhir/StructureDefinition/MeasureReport")
public class MeasureReport extends DomainResource {

    public enum MeasureReportStatus {
        /**
         * The report is complete and ready for use.
         */
        COMPLETE, 
        /**
         * The report is currently being generated.
         */
        PENDING, 
        /**
         * An error occurred attempting to generate the report.
         */
        ERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasureReportStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("pending".equals(codeString))
          return PENDING;
        if ("error".equals(codeString))
          return ERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasureReportStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case PENDING: return "pending";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETE: return "http://hl7.org/fhir/measure-report-status";
            case PENDING: return "http://hl7.org/fhir/measure-report-status";
            case ERROR: return "http://hl7.org/fhir/measure-report-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "The report is complete and ready for use.";
            case PENDING: return "The report is currently being generated.";
            case ERROR: return "An error occurred attempting to generate the report.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "Complete";
            case PENDING: return "Pending";
            case ERROR: return "Error";
            default: return "?";
          }
        }
    }

  public static class MeasureReportStatusEnumFactory implements EnumFactory<MeasureReportStatus> {
    public MeasureReportStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return MeasureReportStatus.COMPLETE;
        if ("pending".equals(codeString))
          return MeasureReportStatus.PENDING;
        if ("error".equals(codeString))
          return MeasureReportStatus.ERROR;
        throw new IllegalArgumentException("Unknown MeasureReportStatus code '"+codeString+"'");
        }
        public Enumeration<MeasureReportStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MeasureReportStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("complete".equals(codeString))
          return new Enumeration<MeasureReportStatus>(this, MeasureReportStatus.COMPLETE);
        if ("pending".equals(codeString))
          return new Enumeration<MeasureReportStatus>(this, MeasureReportStatus.PENDING);
        if ("error".equals(codeString))
          return new Enumeration<MeasureReportStatus>(this, MeasureReportStatus.ERROR);
        throw new FHIRException("Unknown MeasureReportStatus code '"+codeString+"'");
        }
    public String toCode(MeasureReportStatus code) {
      if (code == MeasureReportStatus.COMPLETE)
        return "complete";
      if (code == MeasureReportStatus.PENDING)
        return "pending";
      if (code == MeasureReportStatus.ERROR)
        return "error";
      return "?";
      }
    public String toSystem(MeasureReportStatus code) {
      return code.getSystem();
      }
    }

    public enum MeasureReportType {
        /**
         * An individual report that provides information on the performance for a given measure with respect to a single subject.
         */
        INDIVIDUAL, 
        /**
         * A subject list report that includes a listing of subjects that satisfied each population criteria in the measure.
         */
        SUBJECTLIST, 
        /**
         * A summary report that returns the number of members in each population criteria for the measure.
         */
        SUMMARY, 
        /**
         * A data collection report that contains data-of-interest for the measure.
         */
        DATACOLLECTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasureReportType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("individual".equals(codeString))
          return INDIVIDUAL;
        if ("subject-list".equals(codeString))
          return SUBJECTLIST;
        if ("summary".equals(codeString))
          return SUMMARY;
        if ("data-collection".equals(codeString))
          return DATACOLLECTION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasureReportType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INDIVIDUAL: return "individual";
            case SUBJECTLIST: return "subject-list";
            case SUMMARY: return "summary";
            case DATACOLLECTION: return "data-collection";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INDIVIDUAL: return "http://hl7.org/fhir/measure-report-type";
            case SUBJECTLIST: return "http://hl7.org/fhir/measure-report-type";
            case SUMMARY: return "http://hl7.org/fhir/measure-report-type";
            case DATACOLLECTION: return "http://hl7.org/fhir/measure-report-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INDIVIDUAL: return "An individual report that provides information on the performance for a given measure with respect to a single subject.";
            case SUBJECTLIST: return "A subject list report that includes a listing of subjects that satisfied each population criteria in the measure.";
            case SUMMARY: return "A summary report that returns the number of members in each population criteria for the measure.";
            case DATACOLLECTION: return "A data collection report that contains data-of-interest for the measure.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INDIVIDUAL: return "Individual";
            case SUBJECTLIST: return "Subject List";
            case SUMMARY: return "Summary";
            case DATACOLLECTION: return "Data Collection";
            default: return "?";
          }
        }
    }

  public static class MeasureReportTypeEnumFactory implements EnumFactory<MeasureReportType> {
    public MeasureReportType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("individual".equals(codeString))
          return MeasureReportType.INDIVIDUAL;
        if ("subject-list".equals(codeString))
          return MeasureReportType.SUBJECTLIST;
        if ("summary".equals(codeString))
          return MeasureReportType.SUMMARY;
        if ("data-collection".equals(codeString))
          return MeasureReportType.DATACOLLECTION;
        throw new IllegalArgumentException("Unknown MeasureReportType code '"+codeString+"'");
        }
        public Enumeration<MeasureReportType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MeasureReportType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("individual".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.INDIVIDUAL);
        if ("subject-list".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.SUBJECTLIST);
        if ("summary".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.SUMMARY);
        if ("data-collection".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.DATACOLLECTION);
        throw new FHIRException("Unknown MeasureReportType code '"+codeString+"'");
        }
    public String toCode(MeasureReportType code) {
      if (code == MeasureReportType.INDIVIDUAL)
        return "individual";
      if (code == MeasureReportType.SUBJECTLIST)
        return "subject-list";
      if (code == MeasureReportType.SUMMARY)
        return "summary";
      if (code == MeasureReportType.DATACOLLECTION)
        return "data-collection";
      return "?";
      }
    public String toSystem(MeasureReportType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MeasureReportGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The meaning of the population group as defined in the measure definition.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Meaning of the group", formalDefinition="The meaning of the population group as defined in the measure definition." )
        protected CodeableConcept code;

        /**
         * The populations that make up the population group, one for each type of population appropriate for the measure.
         */
        @Child(name = "population", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The populations in the group", formalDefinition="The populations that make up the population group, one for each type of population appropriate for the measure." )
        protected List<MeasureReportGroupPopulationComponent> population;

        /**
         * The measure score for this population group, calculated as appropriate for the measure type and scoring method, and based on the contents of the populations defined in the group.
         */
        @Child(name = "measureScore", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What score this group achieved", formalDefinition="The measure score for this population group, calculated as appropriate for the measure type and scoring method, and based on the contents of the populations defined in the group." )
        protected Quantity measureScore;

        /**
         * When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.
         */
        @Child(name = "stratifier", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Stratification results", formalDefinition="When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure." )
        protected List<MeasureReportGroupStratifierComponent> stratifier;

        private static final long serialVersionUID = 1744426009L;

    /**
     * Constructor
     */
      public MeasureReportGroupComponent() {
        super();
      }

        /**
         * @return {@link #code} (The meaning of the population group as defined in the measure definition.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The meaning of the population group as defined in the measure definition.)
         */
        public MeasureReportGroupComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #population} (The populations that make up the population group, one for each type of population appropriate for the measure.)
         */
        public List<MeasureReportGroupPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupPopulationComponent>();
          return this.population;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureReportGroupComponent setPopulation(List<MeasureReportGroupPopulationComponent> thePopulation) { 
          this.population = thePopulation;
          return this;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MeasureReportGroupPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MeasureReportGroupPopulationComponent addPopulation() { //3
          MeasureReportGroupPopulationComponent t = new MeasureReportGroupPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupPopulationComponent>();
          this.population.add(t);
          return t;
        }

        public MeasureReportGroupComponent addPopulation(MeasureReportGroupPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
         */
        public MeasureReportGroupPopulationComponent getPopulationFirstRep() { 
          if (getPopulation().isEmpty()) {
            addPopulation();
          }
          return getPopulation().get(0);
        }

        /**
         * @return {@link #measureScore} (The measure score for this population group, calculated as appropriate for the measure type and scoring method, and based on the contents of the populations defined in the group.)
         */
        public Quantity getMeasureScore() { 
          if (this.measureScore == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupComponent.measureScore");
            else if (Configuration.doAutoCreate())
              this.measureScore = new Quantity(); // cc
          return this.measureScore;
        }

        public boolean hasMeasureScore() { 
          return this.measureScore != null && !this.measureScore.isEmpty();
        }

        /**
         * @param value {@link #measureScore} (The measure score for this population group, calculated as appropriate for the measure type and scoring method, and based on the contents of the populations defined in the group.)
         */
        public MeasureReportGroupComponent setMeasureScore(Quantity value) { 
          this.measureScore = value;
          return this;
        }

        /**
         * @return {@link #stratifier} (When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.)
         */
        public List<MeasureReportGroupStratifierComponent> getStratifier() { 
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureReportGroupStratifierComponent>();
          return this.stratifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureReportGroupComponent setStratifier(List<MeasureReportGroupStratifierComponent> theStratifier) { 
          this.stratifier = theStratifier;
          return this;
        }

        public boolean hasStratifier() { 
          if (this.stratifier == null)
            return false;
          for (MeasureReportGroupStratifierComponent item : this.stratifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MeasureReportGroupStratifierComponent addStratifier() { //3
          MeasureReportGroupStratifierComponent t = new MeasureReportGroupStratifierComponent();
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureReportGroupStratifierComponent>();
          this.stratifier.add(t);
          return t;
        }

        public MeasureReportGroupComponent addStratifier(MeasureReportGroupStratifierComponent t) { //3
          if (t == null)
            return this;
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureReportGroupStratifierComponent>();
          this.stratifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #stratifier}, creating it if it does not already exist
         */
        public MeasureReportGroupStratifierComponent getStratifierFirstRep() { 
          if (getStratifier().isEmpty()) {
            addStratifier();
          }
          return getStratifier().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The meaning of the population group as defined in the measure definition.", 0, 1, code));
          children.add(new Property("population", "", "The populations that make up the population group, one for each type of population appropriate for the measure.", 0, java.lang.Integer.MAX_VALUE, population));
          children.add(new Property("measureScore", "Quantity", "The measure score for this population group, calculated as appropriate for the measure type and scoring method, and based on the contents of the populations defined in the group.", 0, 1, measureScore));
          children.add(new Property("stratifier", "", "When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.", 0, java.lang.Integer.MAX_VALUE, stratifier));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The meaning of the population group as defined in the measure definition.", 0, 1, code);
          case -2023558323: /*population*/  return new Property("population", "", "The populations that make up the population group, one for each type of population appropriate for the measure.", 0, java.lang.Integer.MAX_VALUE, population);
          case -386313260: /*measureScore*/  return new Property("measureScore", "Quantity", "The measure score for this population group, calculated as appropriate for the measure type and scoring method, and based on the contents of the populations defined in the group.", 0, 1, measureScore);
          case 90983669: /*stratifier*/  return new Property("stratifier", "", "When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.", 0, java.lang.Integer.MAX_VALUE, stratifier);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MeasureReportGroupPopulationComponent
        case -386313260: /*measureScore*/ return this.measureScore == null ? new Base[0] : new Base[] {this.measureScore}; // Quantity
        case 90983669: /*stratifier*/ return this.stratifier == null ? new Base[0] : this.stratifier.toArray(new Base[this.stratifier.size()]); // MeasureReportGroupStratifierComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2023558323: // population
          this.getPopulation().add((MeasureReportGroupPopulationComponent) value); // MeasureReportGroupPopulationComponent
          return value;
        case -386313260: // measureScore
          this.measureScore = castToQuantity(value); // Quantity
          return value;
        case 90983669: // stratifier
          this.getStratifier().add((MeasureReportGroupStratifierComponent) value); // MeasureReportGroupStratifierComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("population")) {
          this.getPopulation().add((MeasureReportGroupPopulationComponent) value);
        } else if (name.equals("measureScore")) {
          this.measureScore = castToQuantity(value); // Quantity
        } else if (name.equals("stratifier")) {
          this.getStratifier().add((MeasureReportGroupStratifierComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -2023558323:  return addPopulation(); 
        case -386313260:  return getMeasureScore(); 
        case 90983669:  return addStratifier(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -2023558323: /*population*/ return new String[] {};
        case -386313260: /*measureScore*/ return new String[] {"Quantity"};
        case 90983669: /*stratifier*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else if (name.equals("measureScore")) {
          this.measureScore = new Quantity();
          return this.measureScore;
        }
        else if (name.equals("stratifier")) {
          return addStratifier();
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupComponent copy() {
        MeasureReportGroupComponent dst = new MeasureReportGroupComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (population != null) {
          dst.population = new ArrayList<MeasureReportGroupPopulationComponent>();
          for (MeasureReportGroupPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        dst.measureScore = measureScore == null ? null : measureScore.copy();
        if (stratifier != null) {
          dst.stratifier = new ArrayList<MeasureReportGroupStratifierComponent>();
          for (MeasureReportGroupStratifierComponent i : stratifier)
            dst.stratifier.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MeasureReportGroupComponent))
          return false;
        MeasureReportGroupComponent o = (MeasureReportGroupComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(population, o.population, true) && compareDeep(measureScore, o.measureScore, true)
           && compareDeep(stratifier, o.stratifier, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MeasureReportGroupComponent))
          return false;
        MeasureReportGroupComponent o = (MeasureReportGroupComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, population, measureScore
          , stratifier);
      }

  public String fhirType() {
    return "MeasureReport.group";

  }

  }

    @Block()
    public static class MeasureReportGroupPopulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of the population.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-observation", formalDefinition="The type of the population." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-population")
        protected CodeableConcept code;

        /**
         * The number of members of the population.
         */
        @Child(name = "count", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Size of the population", formalDefinition="The number of members of the population." )
        protected IntegerType count;

        /**
         * This element refers to a List of subject level MeasureReport resources, one for each subject in this population.
         */
        @Child(name = "subjectResults", type = {ListResource.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="For subject-list reports, the subject results in this population", formalDefinition="This element refers to a List of subject level MeasureReport resources, one for each subject in this population." )
        protected Reference subjectResults;

        /**
         * The actual object that is the target of the reference (This element refers to a List of subject level MeasureReport resources, one for each subject in this population.)
         */
        protected ListResource subjectResultsTarget;

        private static final long serialVersionUID = 210461445L;

    /**
     * Constructor
     */
      public MeasureReportGroupPopulationComponent() {
        super();
      }

        /**
         * @return {@link #code} (The type of the population.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The type of the population.)
         */
        public MeasureReportGroupPopulationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #count} (The number of members of the population.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public IntegerType getCountElement() { 
          if (this.count == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.count");
            else if (Configuration.doAutoCreate())
              this.count = new IntegerType(); // bb
          return this.count;
        }

        public boolean hasCountElement() { 
          return this.count != null && !this.count.isEmpty();
        }

        public boolean hasCount() { 
          return this.count != null && !this.count.isEmpty();
        }

        /**
         * @param value {@link #count} (The number of members of the population.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public MeasureReportGroupPopulationComponent setCountElement(IntegerType value) { 
          this.count = value;
          return this;
        }

        /**
         * @return The number of members of the population.
         */
        public int getCount() { 
          return this.count == null || this.count.isEmpty() ? 0 : this.count.getValue();
        }

        /**
         * @param value The number of members of the population.
         */
        public MeasureReportGroupPopulationComponent setCount(int value) { 
            if (this.count == null)
              this.count = new IntegerType();
            this.count.setValue(value);
          return this;
        }

        /**
         * @return {@link #subjectResults} (This element refers to a List of subject level MeasureReport resources, one for each subject in this population.)
         */
        public Reference getSubjectResults() { 
          if (this.subjectResults == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.subjectResults");
            else if (Configuration.doAutoCreate())
              this.subjectResults = new Reference(); // cc
          return this.subjectResults;
        }

        public boolean hasSubjectResults() { 
          return this.subjectResults != null && !this.subjectResults.isEmpty();
        }

        /**
         * @param value {@link #subjectResults} (This element refers to a List of subject level MeasureReport resources, one for each subject in this population.)
         */
        public MeasureReportGroupPopulationComponent setSubjectResults(Reference value) { 
          this.subjectResults = value;
          return this;
        }

        /**
         * @return {@link #subjectResults} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This element refers to a List of subject level MeasureReport resources, one for each subject in this population.)
         */
        public ListResource getSubjectResultsTarget() { 
          if (this.subjectResultsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.subjectResults");
            else if (Configuration.doAutoCreate())
              this.subjectResultsTarget = new ListResource(); // aa
          return this.subjectResultsTarget;
        }

        /**
         * @param value {@link #subjectResults} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This element refers to a List of subject level MeasureReport resources, one for each subject in this population.)
         */
        public MeasureReportGroupPopulationComponent setSubjectResultsTarget(ListResource value) { 
          this.subjectResultsTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The type of the population.", 0, 1, code));
          children.add(new Property("count", "integer", "The number of members of the population.", 0, 1, count));
          children.add(new Property("subjectResults", "Reference(List)", "This element refers to a List of subject level MeasureReport resources, one for each subject in this population.", 0, 1, subjectResults));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The type of the population.", 0, 1, code);
          case 94851343: /*count*/  return new Property("count", "integer", "The number of members of the population.", 0, 1, count);
          case 2136184106: /*subjectResults*/  return new Property("subjectResults", "Reference(List)", "This element refers to a List of subject level MeasureReport resources, one for each subject in this population.", 0, 1, subjectResults);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case 2136184106: /*subjectResults*/ return this.subjectResults == null ? new Base[0] : new Base[] {this.subjectResults}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          return value;
        case 2136184106: // subjectResults
          this.subjectResults = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("count")) {
          this.count = castToInteger(value); // IntegerType
        } else if (name.equals("subjectResults")) {
          this.subjectResults = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 94851343:  return getCountElement();
        case 2136184106:  return getSubjectResults(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 94851343: /*count*/ return new String[] {"integer"};
        case 2136184106: /*subjectResults*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.count");
        }
        else if (name.equals("subjectResults")) {
          this.subjectResults = new Reference();
          return this.subjectResults;
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupPopulationComponent copy() {
        MeasureReportGroupPopulationComponent dst = new MeasureReportGroupPopulationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.count = count == null ? null : count.copy();
        dst.subjectResults = subjectResults == null ? null : subjectResults.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MeasureReportGroupPopulationComponent))
          return false;
        MeasureReportGroupPopulationComponent o = (MeasureReportGroupPopulationComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(count, o.count, true) && compareDeep(subjectResults, o.subjectResults, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MeasureReportGroupPopulationComponent))
          return false;
        MeasureReportGroupPopulationComponent o = (MeasureReportGroupPopulationComponent) other_;
        return compareValues(count, o.count, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, count, subjectResults
          );
      }

  public String fhirType() {
    return "MeasureReport.group.population";

  }

  }

    @Block()
    public static class MeasureReportGroupStratifierComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The meaning of this stratifier, as defined in the measure definition.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What stratifier of the group", formalDefinition="The meaning of this stratifier, as defined in the measure definition." )
        protected List<CodeableConcept> code;

        /**
         * This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.
         */
        @Child(name = "stratum", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Stratum results, one for each unique value, or set of values, in the stratifier, or stratifier components", formalDefinition="This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value." )
        protected List<StratifierGroupComponent> stratum;

        private static final long serialVersionUID = 259550185L;

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierComponent() {
        super();
      }

        /**
         * @return {@link #code} (The meaning of this stratifier, as defined in the measure definition.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureReportGroupStratifierComponent setCode(List<CodeableConcept> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        public MeasureReportGroupStratifierComponent addCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public CodeableConcept getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #stratum} (This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.)
         */
        public List<StratifierGroupComponent> getStratum() { 
          if (this.stratum == null)
            this.stratum = new ArrayList<StratifierGroupComponent>();
          return this.stratum;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureReportGroupStratifierComponent setStratum(List<StratifierGroupComponent> theStratum) { 
          this.stratum = theStratum;
          return this;
        }

        public boolean hasStratum() { 
          if (this.stratum == null)
            return false;
          for (StratifierGroupComponent item : this.stratum)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public StratifierGroupComponent addStratum() { //3
          StratifierGroupComponent t = new StratifierGroupComponent();
          if (this.stratum == null)
            this.stratum = new ArrayList<StratifierGroupComponent>();
          this.stratum.add(t);
          return t;
        }

        public MeasureReportGroupStratifierComponent addStratum(StratifierGroupComponent t) { //3
          if (t == null)
            return this;
          if (this.stratum == null)
            this.stratum = new ArrayList<StratifierGroupComponent>();
          this.stratum.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #stratum}, creating it if it does not already exist
         */
        public StratifierGroupComponent getStratumFirstRep() { 
          if (getStratum().isEmpty()) {
            addStratum();
          }
          return getStratum().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The meaning of this stratifier, as defined in the measure definition.", 0, java.lang.Integer.MAX_VALUE, code));
          children.add(new Property("stratum", "", "This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.", 0, java.lang.Integer.MAX_VALUE, stratum));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The meaning of this stratifier, as defined in the measure definition.", 0, java.lang.Integer.MAX_VALUE, code);
          case -1881991236: /*stratum*/  return new Property("stratum", "", "This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.", 0, java.lang.Integer.MAX_VALUE, stratum);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case -1881991236: /*stratum*/ return this.stratum == null ? new Base[0] : this.stratum.toArray(new Base[this.stratum.size()]); // StratifierGroupComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1881991236: // stratum
          this.getStratum().add((StratifierGroupComponent) value); // StratifierGroupComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("stratum")) {
          this.getStratum().add((StratifierGroupComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return addCode(); 
        case -1881991236:  return addStratum(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1881991236: /*stratum*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("stratum")) {
          return addStratum();
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupStratifierComponent copy() {
        MeasureReportGroupStratifierComponent dst = new MeasureReportGroupStratifierComponent();
        copyValues(dst);
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (stratum != null) {
          dst.stratum = new ArrayList<StratifierGroupComponent>();
          for (StratifierGroupComponent i : stratum)
            dst.stratum.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MeasureReportGroupStratifierComponent))
          return false;
        MeasureReportGroupStratifierComponent o = (MeasureReportGroupStratifierComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(stratum, o.stratum, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MeasureReportGroupStratifierComponent))
          return false;
        MeasureReportGroupStratifierComponent o = (MeasureReportGroupStratifierComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, stratum);
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier";

  }

  }

    @Block()
    public static class StratifierGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.
         */
        @Child(name = "value", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The stratum value, e.g. male", formalDefinition="The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique." )
        protected CodeableConcept value;

        /**
         * A stratifier component value.
         */
        @Child(name = "component", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Stratifier component values", formalDefinition="A stratifier component value." )
        protected List<StratifierGroupComponentComponent> component;

        /**
         * The populations that make up the stratum, one for each type of population appropriate to the measure.
         */
        @Child(name = "population", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Population results in this stratum", formalDefinition="The populations that make up the stratum, one for each type of population appropriate to the measure." )
        protected List<StratifierGroupPopulationComponent> population;

        /**
         * The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on only the members of this stratum.
         */
        @Child(name = "measureScore", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What score this stratum achieved", formalDefinition="The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on only the members of this stratum." )
        protected Quantity measureScore;

        private static final long serialVersionUID = 892251179L;

    /**
     * Constructor
     */
      public StratifierGroupComponent() {
        super();
      }

        /**
         * @return {@link #value} (The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.)
         */
        public CodeableConcept getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new CodeableConcept(); // cc
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.)
         */
        public StratifierGroupComponent setValue(CodeableConcept value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #component} (A stratifier component value.)
         */
        public List<StratifierGroupComponentComponent> getComponent() { 
          if (this.component == null)
            this.component = new ArrayList<StratifierGroupComponentComponent>();
          return this.component;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public StratifierGroupComponent setComponent(List<StratifierGroupComponentComponent> theComponent) { 
          this.component = theComponent;
          return this;
        }

        public boolean hasComponent() { 
          if (this.component == null)
            return false;
          for (StratifierGroupComponentComponent item : this.component)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public StratifierGroupComponentComponent addComponent() { //3
          StratifierGroupComponentComponent t = new StratifierGroupComponentComponent();
          if (this.component == null)
            this.component = new ArrayList<StratifierGroupComponentComponent>();
          this.component.add(t);
          return t;
        }

        public StratifierGroupComponent addComponent(StratifierGroupComponentComponent t) { //3
          if (t == null)
            return this;
          if (this.component == null)
            this.component = new ArrayList<StratifierGroupComponentComponent>();
          this.component.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #component}, creating it if it does not already exist
         */
        public StratifierGroupComponentComponent getComponentFirstRep() { 
          if (getComponent().isEmpty()) {
            addComponent();
          }
          return getComponent().get(0);
        }

        /**
         * @return {@link #population} (The populations that make up the stratum, one for each type of population appropriate to the measure.)
         */
        public List<StratifierGroupPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<StratifierGroupPopulationComponent>();
          return this.population;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public StratifierGroupComponent setPopulation(List<StratifierGroupPopulationComponent> thePopulation) { 
          this.population = thePopulation;
          return this;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (StratifierGroupPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public StratifierGroupPopulationComponent addPopulation() { //3
          StratifierGroupPopulationComponent t = new StratifierGroupPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<StratifierGroupPopulationComponent>();
          this.population.add(t);
          return t;
        }

        public StratifierGroupComponent addPopulation(StratifierGroupPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<StratifierGroupPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
         */
        public StratifierGroupPopulationComponent getPopulationFirstRep() { 
          if (getPopulation().isEmpty()) {
            addPopulation();
          }
          return getPopulation().get(0);
        }

        /**
         * @return {@link #measureScore} (The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on only the members of this stratum.)
         */
        public Quantity getMeasureScore() { 
          if (this.measureScore == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupComponent.measureScore");
            else if (Configuration.doAutoCreate())
              this.measureScore = new Quantity(); // cc
          return this.measureScore;
        }

        public boolean hasMeasureScore() { 
          return this.measureScore != null && !this.measureScore.isEmpty();
        }

        /**
         * @param value {@link #measureScore} (The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on only the members of this stratum.)
         */
        public StratifierGroupComponent setMeasureScore(Quantity value) { 
          this.measureScore = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("value", "CodeableConcept", "The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.", 0, 1, value));
          children.add(new Property("component", "", "A stratifier component value.", 0, java.lang.Integer.MAX_VALUE, component));
          children.add(new Property("population", "", "The populations that make up the stratum, one for each type of population appropriate to the measure.", 0, java.lang.Integer.MAX_VALUE, population));
          children.add(new Property("measureScore", "Quantity", "The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on only the members of this stratum.", 0, 1, measureScore));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 111972721: /*value*/  return new Property("value", "CodeableConcept", "The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.", 0, 1, value);
          case -1399907075: /*component*/  return new Property("component", "", "A stratifier component value.", 0, java.lang.Integer.MAX_VALUE, component);
          case -2023558323: /*population*/  return new Property("population", "", "The populations that make up the stratum, one for each type of population appropriate to the measure.", 0, java.lang.Integer.MAX_VALUE, population);
          case -386313260: /*measureScore*/  return new Property("measureScore", "Quantity", "The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on only the members of this stratum.", 0, 1, measureScore);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // CodeableConcept
        case -1399907075: /*component*/ return this.component == null ? new Base[0] : this.component.toArray(new Base[this.component.size()]); // StratifierGroupComponentComponent
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // StratifierGroupPopulationComponent
        case -386313260: /*measureScore*/ return this.measureScore == null ? new Base[0] : new Base[] {this.measureScore}; // Quantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1399907075: // component
          this.getComponent().add((StratifierGroupComponentComponent) value); // StratifierGroupComponentComponent
          return value;
        case -2023558323: // population
          this.getPopulation().add((StratifierGroupPopulationComponent) value); // StratifierGroupPopulationComponent
          return value;
        case -386313260: // measureScore
          this.measureScore = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value")) {
          this.value = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("component")) {
          this.getComponent().add((StratifierGroupComponentComponent) value);
        } else if (name.equals("population")) {
          this.getPopulation().add((StratifierGroupPopulationComponent) value);
        } else if (name.equals("measureScore")) {
          this.measureScore = castToQuantity(value); // Quantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721:  return getValue(); 
        case -1399907075:  return addComponent(); 
        case -2023558323:  return addPopulation(); 
        case -386313260:  return getMeasureScore(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return new String[] {"CodeableConcept"};
        case -1399907075: /*component*/ return new String[] {};
        case -2023558323: /*population*/ return new String[] {};
        case -386313260: /*measureScore*/ return new String[] {"Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("value")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("component")) {
          return addComponent();
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else if (name.equals("measureScore")) {
          this.measureScore = new Quantity();
          return this.measureScore;
        }
        else
          return super.addChild(name);
      }

      public StratifierGroupComponent copy() {
        StratifierGroupComponent dst = new StratifierGroupComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        if (component != null) {
          dst.component = new ArrayList<StratifierGroupComponentComponent>();
          for (StratifierGroupComponentComponent i : component)
            dst.component.add(i.copy());
        };
        if (population != null) {
          dst.population = new ArrayList<StratifierGroupPopulationComponent>();
          for (StratifierGroupPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        dst.measureScore = measureScore == null ? null : measureScore.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof StratifierGroupComponent))
          return false;
        StratifierGroupComponent o = (StratifierGroupComponent) other_;
        return compareDeep(value, o.value, true) && compareDeep(component, o.component, true) && compareDeep(population, o.population, true)
           && compareDeep(measureScore, o.measureScore, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof StratifierGroupComponent))
          return false;
        StratifierGroupComponent o = (StratifierGroupComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value, component, population
          , measureScore);
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier.stratum";

  }

  }

    @Block()
    public static class StratifierGroupComponentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The code for the stratum component value.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What stratifier component of the group", formalDefinition="The code for the stratum component value." )
        protected CodeableConcept code;

        /**
         * The stratum component value.
         */
        @Child(name = "value", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The stratum component value, e.g. male", formalDefinition="The stratum component value." )
        protected CodeableConcept value;

        private static final long serialVersionUID = 1750253426L;

    /**
     * Constructor
     */
      public StratifierGroupComponentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StratifierGroupComponentComponent(CodeableConcept code, CodeableConcept value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (The code for the stratum component value.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupComponentComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The code for the stratum component value.)
         */
        public StratifierGroupComponentComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #value} (The stratum component value.)
         */
        public CodeableConcept getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupComponentComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new CodeableConcept(); // cc
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The stratum component value.)
         */
        public StratifierGroupComponentComponent setValue(CodeableConcept value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The code for the stratum component value.", 0, 1, code));
          children.add(new Property("value", "CodeableConcept", "The stratum component value.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The code for the stratum component value.", 0, 1, code);
          case 111972721: /*value*/  return new Property("value", "CodeableConcept", "The stratum component value.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // CodeableConcept
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
          this.value = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value")) {
          this.value = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("value")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public StratifierGroupComponentComponent copy() {
        StratifierGroupComponentComponent dst = new StratifierGroupComponentComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof StratifierGroupComponentComponent))
          return false;
        StratifierGroupComponentComponent o = (StratifierGroupComponentComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof StratifierGroupComponentComponent))
          return false;
        StratifierGroupComponentComponent o = (StratifierGroupComponentComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, value);
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier.stratum.component";

  }

  }

    @Block()
    public static class StratifierGroupPopulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of the population.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-observation", formalDefinition="The type of the population." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-population")
        protected CodeableConcept code;

        /**
         * The number of members of the population in this stratum.
         */
        @Child(name = "count", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Size of the population", formalDefinition="The number of members of the population in this stratum." )
        protected IntegerType count;

        /**
         * This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.
         */
        @Child(name = "subjectResults", type = {ListResource.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="For subject-list reports, the subject results in this population", formalDefinition="This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum." )
        protected Reference subjectResults;

        /**
         * The actual object that is the target of the reference (This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.)
         */
        protected ListResource subjectResultsTarget;

        private static final long serialVersionUID = 210461445L;

    /**
     * Constructor
     */
      public StratifierGroupPopulationComponent() {
        super();
      }

        /**
         * @return {@link #code} (The type of the population.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupPopulationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The type of the population.)
         */
        public StratifierGroupPopulationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #count} (The number of members of the population in this stratum.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public IntegerType getCountElement() { 
          if (this.count == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupPopulationComponent.count");
            else if (Configuration.doAutoCreate())
              this.count = new IntegerType(); // bb
          return this.count;
        }

        public boolean hasCountElement() { 
          return this.count != null && !this.count.isEmpty();
        }

        public boolean hasCount() { 
          return this.count != null && !this.count.isEmpty();
        }

        /**
         * @param value {@link #count} (The number of members of the population in this stratum.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public StratifierGroupPopulationComponent setCountElement(IntegerType value) { 
          this.count = value;
          return this;
        }

        /**
         * @return The number of members of the population in this stratum.
         */
        public int getCount() { 
          return this.count == null || this.count.isEmpty() ? 0 : this.count.getValue();
        }

        /**
         * @param value The number of members of the population in this stratum.
         */
        public StratifierGroupPopulationComponent setCount(int value) { 
            if (this.count == null)
              this.count = new IntegerType();
            this.count.setValue(value);
          return this;
        }

        /**
         * @return {@link #subjectResults} (This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.)
         */
        public Reference getSubjectResults() { 
          if (this.subjectResults == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupPopulationComponent.subjectResults");
            else if (Configuration.doAutoCreate())
              this.subjectResults = new Reference(); // cc
          return this.subjectResults;
        }

        public boolean hasSubjectResults() { 
          return this.subjectResults != null && !this.subjectResults.isEmpty();
        }

        /**
         * @param value {@link #subjectResults} (This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.)
         */
        public StratifierGroupPopulationComponent setSubjectResults(Reference value) { 
          this.subjectResults = value;
          return this;
        }

        /**
         * @return {@link #subjectResults} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.)
         */
        public ListResource getSubjectResultsTarget() { 
          if (this.subjectResultsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StratifierGroupPopulationComponent.subjectResults");
            else if (Configuration.doAutoCreate())
              this.subjectResultsTarget = new ListResource(); // aa
          return this.subjectResultsTarget;
        }

        /**
         * @param value {@link #subjectResults} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.)
         */
        public StratifierGroupPopulationComponent setSubjectResultsTarget(ListResource value) { 
          this.subjectResultsTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The type of the population.", 0, 1, code));
          children.add(new Property("count", "integer", "The number of members of the population in this stratum.", 0, 1, count));
          children.add(new Property("subjectResults", "Reference(List)", "This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.", 0, 1, subjectResults));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The type of the population.", 0, 1, code);
          case 94851343: /*count*/  return new Property("count", "integer", "The number of members of the population in this stratum.", 0, 1, count);
          case 2136184106: /*subjectResults*/  return new Property("subjectResults", "Reference(List)", "This element refers to a List of subject level MeasureReport resources, one for each subject in this population in this stratum.", 0, 1, subjectResults);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case 2136184106: /*subjectResults*/ return this.subjectResults == null ? new Base[0] : new Base[] {this.subjectResults}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          return value;
        case 2136184106: // subjectResults
          this.subjectResults = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("count")) {
          this.count = castToInteger(value); // IntegerType
        } else if (name.equals("subjectResults")) {
          this.subjectResults = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 94851343:  return getCountElement();
        case 2136184106:  return getSubjectResults(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 94851343: /*count*/ return new String[] {"integer"};
        case 2136184106: /*subjectResults*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.count");
        }
        else if (name.equals("subjectResults")) {
          this.subjectResults = new Reference();
          return this.subjectResults;
        }
        else
          return super.addChild(name);
      }

      public StratifierGroupPopulationComponent copy() {
        StratifierGroupPopulationComponent dst = new StratifierGroupPopulationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.count = count == null ? null : count.copy();
        dst.subjectResults = subjectResults == null ? null : subjectResults.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof StratifierGroupPopulationComponent))
          return false;
        StratifierGroupPopulationComponent o = (StratifierGroupPopulationComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(count, o.count, true) && compareDeep(subjectResults, o.subjectResults, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof StratifierGroupPopulationComponent))
          return false;
        StratifierGroupPopulationComponent o = (StratifierGroupPopulationComponent) other_;
        return compareValues(count, o.count, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, count, subjectResults
          );
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier.stratum.population";

  }

  }

    /**
     * A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the MeasureReport", formalDefinition="A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The MeasureReport status. No data will be available until the MeasureReport status is complete.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="complete | pending | error", formalDefinition="The MeasureReport status. No data will be available until the MeasureReport status is complete." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-report-status")
    protected Enumeration<MeasureReportStatus> status;

    /**
     * The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.
     */
    @Child(name = "type", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="individual | subject-list | summary | data-collection", formalDefinition="The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-report-type")
    protected Enumeration<MeasureReportType> type;

    /**
     * A reference to the Measure that was calculated to produce this report.
     */
    @Child(name = "measure", type = {CanonicalType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What measure was calculated", formalDefinition="A reference to the Measure that was calculated to produce this report." )
    protected CanonicalType measure;

    /**
     * Optional subject identifying the individual or individuals the report is for.
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, PractitionerRole.class, Location.class, Device.class, RelatedPerson.class, Group.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What individual(s) the report is for", formalDefinition="Optional subject identifying the individual or individuals the report is for." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Optional subject identifying the individual or individuals the report is for.)
     */
    protected Resource subjectTarget;

    /**
     * The date this measure report was generated.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the report was generated", formalDefinition="The date this measure report was generated." )
    protected DateTimeType date;

    /**
     * The individual, location, or organization that is reporting the data.
     */
    @Child(name = "reporter", type = {Practitioner.class, PractitionerRole.class, Location.class, Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is reporting the data", formalDefinition="The individual, location, or organization that is reporting the data." )
    protected Reference reporter;

    /**
     * The actual object that is the target of the reference (The individual, location, or organization that is reporting the data.)
     */
    protected Resource reporterTarget;

    /**
     * The reporting period for which the report was calculated.
     */
    @Child(name = "period", type = {Period.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What period the report covers", formalDefinition="The reporting period for which the report was calculated." )
    protected Period period;

    /**
     * Whether improvement in the measure is noted by an increase or decrease in the measure score.
     */
    @Child(name = "improvementNotation", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="increase | decrease", formalDefinition="Whether improvement in the measure is noted by an increase or decrease in the measure score." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-improvement-notation")
    protected CodeableConcept improvementNotation;

    /**
     * The results of the calculation, one for each population group in the measure.
     */
    @Child(name = "group", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Measure results for each group", formalDefinition="The results of the calculation, one for each population group in the measure." )
    protected List<MeasureReportGroupComponent> group;

    /**
     * A reference to a Bundle containing the Resources that were used in the calculation of this measure.
     */
    @Child(name = "evaluatedResource", type = {Reference.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What data was used to calculate the measure score", formalDefinition="A reference to a Bundle containing the Resources that were used in the calculation of this measure." )
    protected List<Reference> evaluatedResource;
    /**
     * The actual objects that are the target of the reference (A reference to a Bundle containing the Resources that were used in the calculation of this measure.)
     */
    protected List<Resource> evaluatedResourceTarget;


    private static final long serialVersionUID = 355307999L;

  /**
   * Constructor
   */
    public MeasureReport() {
      super();
    }

  /**
   * Constructor
   */
    public MeasureReport(Enumeration<MeasureReportStatus> status, Enumeration<MeasureReportType> type, CanonicalType measure, Period period) {
      super();
      this.status = status;
      this.type = type;
      this.measure = measure;
      this.period = period;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MeasureReport setIdentifier(List<Identifier> theIdentifier) { 
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

    public MeasureReport addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The MeasureReport status. No data will be available until the MeasureReport status is complete.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MeasureReportStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MeasureReportStatus>(new MeasureReportStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The MeasureReport status. No data will be available until the MeasureReport status is complete.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MeasureReport setStatusElement(Enumeration<MeasureReportStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The MeasureReport status. No data will be available until the MeasureReport status is complete.
     */
    public MeasureReportStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The MeasureReport status. No data will be available until the MeasureReport status is complete.
     */
    public MeasureReport setStatus(MeasureReportStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MeasureReportStatus>(new MeasureReportStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<MeasureReportType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<MeasureReportType>(new MeasureReportTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public MeasureReport setTypeElement(Enumeration<MeasureReportType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.
     */
    public MeasureReportType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.
     */
    public MeasureReport setType(MeasureReportType value) { 
        if (this.type == null)
          this.type = new Enumeration<MeasureReportType>(new MeasureReportTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #measure} (A reference to the Measure that was calculated to produce this report.). This is the underlying object with id, value and extensions. The accessor "getMeasure" gives direct access to the value
     */
    public CanonicalType getMeasureElement() { 
      if (this.measure == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.measure");
        else if (Configuration.doAutoCreate())
          this.measure = new CanonicalType(); // bb
      return this.measure;
    }

    public boolean hasMeasureElement() { 
      return this.measure != null && !this.measure.isEmpty();
    }

    public boolean hasMeasure() { 
      return this.measure != null && !this.measure.isEmpty();
    }

    /**
     * @param value {@link #measure} (A reference to the Measure that was calculated to produce this report.). This is the underlying object with id, value and extensions. The accessor "getMeasure" gives direct access to the value
     */
    public MeasureReport setMeasureElement(CanonicalType value) { 
      this.measure = value;
      return this;
    }

    /**
     * @return A reference to the Measure that was calculated to produce this report.
     */
    public String getMeasure() { 
      return this.measure == null ? null : this.measure.getValue();
    }

    /**
     * @param value A reference to the Measure that was calculated to produce this report.
     */
    public MeasureReport setMeasure(String value) { 
        if (this.measure == null)
          this.measure = new CanonicalType();
        this.measure.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (Optional subject identifying the individual or individuals the report is for.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Optional subject identifying the individual or individuals the report is for.)
     */
    public MeasureReport setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Optional subject identifying the individual or individuals the report is for.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Optional subject identifying the individual or individuals the report is for.)
     */
    public MeasureReport setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date this measure report was generated.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.date");
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
     * @param value {@link #date} (The date this measure report was generated.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public MeasureReport setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this measure report was generated.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this measure report was generated.
     */
    public MeasureReport setDate(Date value) { 
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
     * @return {@link #reporter} (The individual, location, or organization that is reporting the data.)
     */
    public Reference getReporter() { 
      if (this.reporter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.reporter");
        else if (Configuration.doAutoCreate())
          this.reporter = new Reference(); // cc
      return this.reporter;
    }

    public boolean hasReporter() { 
      return this.reporter != null && !this.reporter.isEmpty();
    }

    /**
     * @param value {@link #reporter} (The individual, location, or organization that is reporting the data.)
     */
    public MeasureReport setReporter(Reference value) { 
      this.reporter = value;
      return this;
    }

    /**
     * @return {@link #reporter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual, location, or organization that is reporting the data.)
     */
    public Resource getReporterTarget() { 
      return this.reporterTarget;
    }

    /**
     * @param value {@link #reporter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual, location, or organization that is reporting the data.)
     */
    public MeasureReport setReporterTarget(Resource value) { 
      this.reporterTarget = value;
      return this;
    }

    /**
     * @return {@link #period} (The reporting period for which the report was calculated.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The reporting period for which the report was calculated.)
     */
    public MeasureReport setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #improvementNotation} (Whether improvement in the measure is noted by an increase or decrease in the measure score.)
     */
    public CodeableConcept getImprovementNotation() { 
      if (this.improvementNotation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.improvementNotation");
        else if (Configuration.doAutoCreate())
          this.improvementNotation = new CodeableConcept(); // cc
      return this.improvementNotation;
    }

    public boolean hasImprovementNotation() { 
      return this.improvementNotation != null && !this.improvementNotation.isEmpty();
    }

    /**
     * @param value {@link #improvementNotation} (Whether improvement in the measure is noted by an increase or decrease in the measure score.)
     */
    public MeasureReport setImprovementNotation(CodeableConcept value) { 
      this.improvementNotation = value;
      return this;
    }

    /**
     * @return {@link #group} (The results of the calculation, one for each population group in the measure.)
     */
    public List<MeasureReportGroupComponent> getGroup() { 
      if (this.group == null)
        this.group = new ArrayList<MeasureReportGroupComponent>();
      return this.group;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MeasureReport setGroup(List<MeasureReportGroupComponent> theGroup) { 
      this.group = theGroup;
      return this;
    }

    public boolean hasGroup() { 
      if (this.group == null)
        return false;
      for (MeasureReportGroupComponent item : this.group)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MeasureReportGroupComponent addGroup() { //3
      MeasureReportGroupComponent t = new MeasureReportGroupComponent();
      if (this.group == null)
        this.group = new ArrayList<MeasureReportGroupComponent>();
      this.group.add(t);
      return t;
    }

    public MeasureReport addGroup(MeasureReportGroupComponent t) { //3
      if (t == null)
        return this;
      if (this.group == null)
        this.group = new ArrayList<MeasureReportGroupComponent>();
      this.group.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #group}, creating it if it does not already exist
     */
    public MeasureReportGroupComponent getGroupFirstRep() { 
      if (getGroup().isEmpty()) {
        addGroup();
      }
      return getGroup().get(0);
    }

    /**
     * @return {@link #evaluatedResource} (A reference to a Bundle containing the Resources that were used in the calculation of this measure.)
     */
    public List<Reference> getEvaluatedResource() { 
      if (this.evaluatedResource == null)
        this.evaluatedResource = new ArrayList<Reference>();
      return this.evaluatedResource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MeasureReport setEvaluatedResource(List<Reference> theEvaluatedResource) { 
      this.evaluatedResource = theEvaluatedResource;
      return this;
    }

    public boolean hasEvaluatedResource() { 
      if (this.evaluatedResource == null)
        return false;
      for (Reference item : this.evaluatedResource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEvaluatedResource() { //3
      Reference t = new Reference();
      if (this.evaluatedResource == null)
        this.evaluatedResource = new ArrayList<Reference>();
      this.evaluatedResource.add(t);
      return t;
    }

    public MeasureReport addEvaluatedResource(Reference t) { //3
      if (t == null)
        return this;
      if (this.evaluatedResource == null)
        this.evaluatedResource = new ArrayList<Reference>();
      this.evaluatedResource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #evaluatedResource}, creating it if it does not already exist
     */
    public Reference getEvaluatedResourceFirstRep() { 
      if (getEvaluatedResource().isEmpty()) {
        addEvaluatedResource();
      }
      return getEvaluatedResource().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getEvaluatedResourceTarget() { 
      if (this.evaluatedResourceTarget == null)
        this.evaluatedResourceTarget = new ArrayList<Resource>();
      return this.evaluatedResourceTarget;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The MeasureReport status. No data will be available until the MeasureReport status is complete.", 0, 1, status));
        children.add(new Property("type", "code", "The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.", 0, 1, type));
        children.add(new Property("measure", "canonical(Measure)", "A reference to the Measure that was calculated to produce this report.", 0, 1, measure));
        children.add(new Property("subject", "Reference(Patient|Practitioner|PractitionerRole|Location|Device|RelatedPerson|Group)", "Optional subject identifying the individual or individuals the report is for.", 0, 1, subject));
        children.add(new Property("date", "dateTime", "The date this measure report was generated.", 0, 1, date));
        children.add(new Property("reporter", "Reference(Practitioner|PractitionerRole|Location|Organization)", "The individual, location, or organization that is reporting the data.", 0, 1, reporter));
        children.add(new Property("period", "Period", "The reporting period for which the report was calculated.", 0, 1, period));
        children.add(new Property("improvementNotation", "CodeableConcept", "Whether improvement in the measure is noted by an increase or decrease in the measure score.", 0, 1, improvementNotation));
        children.add(new Property("group", "", "The results of the calculation, one for each population group in the measure.", 0, java.lang.Integer.MAX_VALUE, group));
        children.add(new Property("evaluatedResource", "Reference(Any)", "A reference to a Bundle containing the Resources that were used in the calculation of this measure.", 0, java.lang.Integer.MAX_VALUE, evaluatedResource));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The MeasureReport status. No data will be available until the MeasureReport status is complete.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "code", "The type of measure report. This may be an individual report, which provides the score for the measure for an individual member of the population; a subject-listing, which returns the list of members that meet the various criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality measure.", 0, 1, type);
        case 938321246: /*measure*/  return new Property("measure", "canonical(Measure)", "A reference to the Measure that was calculated to produce this report.", 0, 1, measure);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Practitioner|PractitionerRole|Location|Device|RelatedPerson|Group)", "Optional subject identifying the individual or individuals the report is for.", 0, 1, subject);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date this measure report was generated.", 0, 1, date);
        case -427039519: /*reporter*/  return new Property("reporter", "Reference(Practitioner|PractitionerRole|Location|Organization)", "The individual, location, or organization that is reporting the data.", 0, 1, reporter);
        case -991726143: /*period*/  return new Property("period", "Period", "The reporting period for which the report was calculated.", 0, 1, period);
        case -2085456136: /*improvementNotation*/  return new Property("improvementNotation", "CodeableConcept", "Whether improvement in the measure is noted by an increase or decrease in the measure score.", 0, 1, improvementNotation);
        case 98629247: /*group*/  return new Property("group", "", "The results of the calculation, one for each population group in the measure.", 0, java.lang.Integer.MAX_VALUE, group);
        case -1056771047: /*evaluatedResource*/  return new Property("evaluatedResource", "Reference(Any)", "A reference to a Bundle containing the Resources that were used in the calculation of this measure.", 0, java.lang.Integer.MAX_VALUE, evaluatedResource);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MeasureReportStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<MeasureReportType>
        case 938321246: /*measure*/ return this.measure == null ? new Base[0] : new Base[] {this.measure}; // CanonicalType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -427039519: /*reporter*/ return this.reporter == null ? new Base[0] : new Base[] {this.reporter}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -2085456136: /*improvementNotation*/ return this.improvementNotation == null ? new Base[0] : new Base[] {this.improvementNotation}; // CodeableConcept
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // MeasureReportGroupComponent
        case -1056771047: /*evaluatedResource*/ return this.evaluatedResource == null ? new Base[0] : this.evaluatedResource.toArray(new Base[this.evaluatedResource.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new MeasureReportStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MeasureReportStatus>
          return value;
        case 3575610: // type
          value = new MeasureReportTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<MeasureReportType>
          return value;
        case 938321246: // measure
          this.measure = castToCanonical(value); // CanonicalType
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -427039519: // reporter
          this.reporter = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -2085456136: // improvementNotation
          this.improvementNotation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 98629247: // group
          this.getGroup().add((MeasureReportGroupComponent) value); // MeasureReportGroupComponent
          return value;
        case -1056771047: // evaluatedResource
          this.getEvaluatedResource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new MeasureReportStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MeasureReportStatus>
        } else if (name.equals("type")) {
          value = new MeasureReportTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<MeasureReportType>
        } else if (name.equals("measure")) {
          this.measure = castToCanonical(value); // CanonicalType
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("reporter")) {
          this.reporter = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("improvementNotation")) {
          this.improvementNotation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("group")) {
          this.getGroup().add((MeasureReportGroupComponent) value);
        } else if (name.equals("evaluatedResource")) {
          this.getEvaluatedResource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getTypeElement();
        case 938321246:  return getMeasureElement();
        case -1867885268:  return getSubject(); 
        case 3076014:  return getDateElement();
        case -427039519:  return getReporter(); 
        case -991726143:  return getPeriod(); 
        case -2085456136:  return getImprovementNotation(); 
        case 98629247:  return addGroup(); 
        case -1056771047:  return addEvaluatedResource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 938321246: /*measure*/ return new String[] {"canonical"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -427039519: /*reporter*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -2085456136: /*improvementNotation*/ return new String[] {"CodeableConcept"};
        case 98629247: /*group*/ return new String[] {};
        case -1056771047: /*evaluatedResource*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.status");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.type");
        }
        else if (name.equals("measure")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.measure");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.date");
        }
        else if (name.equals("reporter")) {
          this.reporter = new Reference();
          return this.reporter;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("improvementNotation")) {
          this.improvementNotation = new CodeableConcept();
          return this.improvementNotation;
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else if (name.equals("evaluatedResource")) {
          return addEvaluatedResource();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MeasureReport";

  }

      public MeasureReport copy() {
        MeasureReport dst = new MeasureReport();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.measure = measure == null ? null : measure.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.date = date == null ? null : date.copy();
        dst.reporter = reporter == null ? null : reporter.copy();
        dst.period = period == null ? null : period.copy();
        dst.improvementNotation = improvementNotation == null ? null : improvementNotation.copy();
        if (group != null) {
          dst.group = new ArrayList<MeasureReportGroupComponent>();
          for (MeasureReportGroupComponent i : group)
            dst.group.add(i.copy());
        };
        if (evaluatedResource != null) {
          dst.evaluatedResource = new ArrayList<Reference>();
          for (Reference i : evaluatedResource)
            dst.evaluatedResource.add(i.copy());
        };
        return dst;
      }

      protected MeasureReport typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MeasureReport))
          return false;
        MeasureReport o = (MeasureReport) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(measure, o.measure, true) && compareDeep(subject, o.subject, true) && compareDeep(date, o.date, true)
           && compareDeep(reporter, o.reporter, true) && compareDeep(period, o.period, true) && compareDeep(improvementNotation, o.improvementNotation, true)
           && compareDeep(group, o.group, true) && compareDeep(evaluatedResource, o.evaluatedResource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MeasureReport))
          return false;
        MeasureReport o = (MeasureReport) other_;
        return compareValues(status, o.status, true) && compareValues(type, o.type, true) && compareValues(date, o.date, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , measure, subject, date, reporter, period, improvementNotation, group, evaluatedResource
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MeasureReport;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The date of the measure report</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MeasureReport.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="MeasureReport.date", description="The date of the measure report", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The date of the measure report</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MeasureReport.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier of the measure report to be returned</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MeasureReport.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MeasureReport.identifier", description="External identifier of the measure report to be returned", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier of the measure report to be returned</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MeasureReport.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>The period of the measure report</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MeasureReport.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="MeasureReport.period", description="The period of the measure report", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>The period of the measure report</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MeasureReport.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

 /**
   * Search parameter: <b>measure</b>
   * <p>
   * Description: <b>The measure to return measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.measure</b><br>
   * </p>
   */
  @SearchParamDefinition(name="measure", path="MeasureReport.measure", description="The measure to return measure report results for", type="reference", target={Measure.class } )
  public static final String SP_MEASURE = "measure";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>measure</b>
   * <p>
   * Description: <b>The measure to return measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.measure</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MEASURE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MEASURE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MeasureReport:measure</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MEASURE = new ca.uhn.fhir.model.api.Include("MeasureReport:measure").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for individual measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MeasureReport.subject.where(resolve() is Patient)", description="The identity of a patient to search for individual measure report results for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for individual measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MeasureReport:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MeasureReport:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identity of a subject to search for individual measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MeasureReport.subject", description="The identity of a subject to search for individual measure report results for", type="reference", target={Device.class, Group.class, Location.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identity of a subject to search for individual measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MeasureReport:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MeasureReport:subject").toLocked();

 /**
   * Search parameter: <b>reporter</b>
   * <p>
   * Description: <b>The reporter to return measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.reporter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reporter", path="MeasureReport.reporter", description="The reporter to return measure report results for", type="reference", target={Location.class, Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_REPORTER = "reporter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reporter</b>
   * <p>
   * Description: <b>The reporter to return measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.reporter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPORTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPORTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MeasureReport:reporter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPORTER = new ca.uhn.fhir.model.api.Include("MeasureReport:reporter").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the measure report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MeasureReport.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MeasureReport.status", description="The status of the measure report", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the measure report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MeasureReport.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>evaluated-resource</b>
   * <p>
   * Description: <b>An evaluated resource referenced by the measure report</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.evaluatedResource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="evaluated-resource", path="MeasureReport.evaluatedResource", description="An evaluated resource referenced by the measure report", type="reference" )
  public static final String SP_EVALUATED_RESOURCE = "evaluated-resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>evaluated-resource</b>
   * <p>
   * Description: <b>An evaluated resource referenced by the measure report</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.evaluatedResource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam EVALUATED_RESOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_EVALUATED_RESOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MeasureReport:evaluated-resource</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_EVALUATED_RESOURCE = new ca.uhn.fhir.model.api.Include("MeasureReport:evaluated-resource").toLocked();


}

