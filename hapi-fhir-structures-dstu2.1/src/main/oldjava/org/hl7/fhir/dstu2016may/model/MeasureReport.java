package org.hl7.fhir.dstu2016may.model;

import java.math.BigDecimal;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * The MeasureReport resource contains the results of evaluating a measure.
 */
@ResourceDef(name="MeasureReport", profile="http://hl7.org/fhir/Profile/MeasureReport")
public class MeasureReport extends DomainResource {

    public enum MeasureReportType {
        /**
         * An individual report that provides information on the performance for a given measure with respect to a single patient
         */
        INDIVIDUAL, 
        /**
         * A patient list report that includes a listing of patients that satisfied each population criteria in the measure
         */
        PATIENTLIST, 
        /**
         * A summary report that returns the number of patients in each population criteria for the measure
         */
        SUMMARY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasureReportType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("individual".equals(codeString))
          return INDIVIDUAL;
        if ("patient-list".equals(codeString))
          return PATIENTLIST;
        if ("summary".equals(codeString))
          return SUMMARY;
        throw new FHIRException("Unknown MeasureReportType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INDIVIDUAL: return "individual";
            case PATIENTLIST: return "patient-list";
            case SUMMARY: return "summary";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INDIVIDUAL: return "http://hl7.org/fhir/measure-report-type";
            case PATIENTLIST: return "http://hl7.org/fhir/measure-report-type";
            case SUMMARY: return "http://hl7.org/fhir/measure-report-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INDIVIDUAL: return "An individual report that provides information on the performance for a given measure with respect to a single patient";
            case PATIENTLIST: return "A patient list report that includes a listing of patients that satisfied each population criteria in the measure";
            case SUMMARY: return "A summary report that returns the number of patients in each population criteria for the measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INDIVIDUAL: return "Individual";
            case PATIENTLIST: return "Patient List";
            case SUMMARY: return "Summary";
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
        if ("patient-list".equals(codeString))
          return MeasureReportType.PATIENTLIST;
        if ("summary".equals(codeString))
          return MeasureReportType.SUMMARY;
        throw new IllegalArgumentException("Unknown MeasureReportType code '"+codeString+"'");
        }
        public Enumeration<MeasureReportType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("individual".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.INDIVIDUAL);
        if ("patient-list".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.PATIENTLIST);
        if ("summary".equals(codeString))
          return new Enumeration<MeasureReportType>(this, MeasureReportType.SUMMARY);
        throw new FHIRException("Unknown MeasureReportType code '"+codeString+"'");
        }
    public String toCode(MeasureReportType code) {
      if (code == MeasureReportType.INDIVIDUAL)
        return "individual";
      if (code == MeasureReportType.PATIENTLIST)
        return "patient-list";
      if (code == MeasureReportType.SUMMARY)
        return "summary";
      return "?";
      }
    public String toSystem(MeasureReportType code) {
      return code.getSystem();
      }
    }

    public enum MeasureReportStatus {
        /**
         * The report is complete and ready for use
         */
        COMPLETE, 
        /**
         * The report is currently being generated
         */
        PENDING, 
        /**
         * An error occurred attempting to generate the report
         */
        ERROR, 
        /**
         * added to help the parsers
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
            case COMPLETE: return "The report is complete and ready for use";
            case PENDING: return "The report is currently being generated";
            case ERROR: return "An error occurred attempting to generate the report";
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
          if (code == null || code.isEmpty())
            return null;
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

    @Block()
    public static class MeasureReportGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier of the population group as defined in the measure definition.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier of the population group being reported", formalDefinition="The identifier of the population group as defined in the measure definition." )
        protected Identifier identifier;

        /**
         * The populations that make up the population group, one for each type of population appropriate for the measure.
         */
        @Child(name = "population", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The populations in the group", formalDefinition="The populations that make up the population group, one for each type of population appropriate for the measure." )
        protected List<MeasureReportGroupPopulationComponent> population;

        /**
         * The measure score.
         */
        @Child(name = "measureScore", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The measure score", formalDefinition="The measure score." )
        protected DecimalType measureScore;

        /**
         * When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.
         */
        @Child(name = "stratifier", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Stratification results", formalDefinition="When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure." )
        protected List<MeasureReportGroupStratifierComponent> stratifier;

        /**
         * Supplemental data elements for the measure provide additional information requested by the measure for each patient involved in the populations.
         */
        @Child(name = "supplementalData", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supplemental data elements for the measure", formalDefinition="Supplemental data elements for the measure provide additional information requested by the measure for each patient involved in the populations." )
        protected List<MeasureReportGroupSupplementalDataComponent> supplementalData;

        private static final long serialVersionUID = -832029515L;

    /**
     * Constructor
     */
      public MeasureReportGroupComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #identifier} (The identifier of the population group as defined in the measure definition.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier of the population group as defined in the measure definition.)
         */
        public MeasureReportGroupComponent setIdentifier(Identifier value) { 
          this.identifier = value;
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

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MeasureReportGroupPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #population} (The populations that make up the population group, one for each type of population appropriate for the measure.)
         */
    // syntactic sugar
        public MeasureReportGroupPopulationComponent addPopulation() { //3
          MeasureReportGroupPopulationComponent t = new MeasureReportGroupPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupPopulationComponent>();
          this.population.add(t);
          return t;
        }

    // syntactic sugar
        public MeasureReportGroupComponent addPopulation(MeasureReportGroupPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return {@link #measureScore} (The measure score.). This is the underlying object with id, value and extensions. The accessor "getMeasureScore" gives direct access to the value
         */
        public DecimalType getMeasureScoreElement() { 
          if (this.measureScore == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupComponent.measureScore");
            else if (Configuration.doAutoCreate())
              this.measureScore = new DecimalType(); // bb
          return this.measureScore;
        }

        public boolean hasMeasureScoreElement() { 
          return this.measureScore != null && !this.measureScore.isEmpty();
        }

        public boolean hasMeasureScore() { 
          return this.measureScore != null && !this.measureScore.isEmpty();
        }

        /**
         * @param value {@link #measureScore} (The measure score.). This is the underlying object with id, value and extensions. The accessor "getMeasureScore" gives direct access to the value
         */
        public MeasureReportGroupComponent setMeasureScoreElement(DecimalType value) { 
          this.measureScore = value;
          return this;
        }

        /**
         * @return The measure score.
         */
        public BigDecimal getMeasureScore() { 
          return this.measureScore == null ? null : this.measureScore.getValue();
        }

        /**
         * @param value The measure score.
         */
        public MeasureReportGroupComponent setMeasureScore(BigDecimal value) { 
          if (value == null)
            this.measureScore = null;
          else {
            if (this.measureScore == null)
              this.measureScore = new DecimalType();
            this.measureScore.setValue(value);
          }
          return this;
        }

        /**
         * @param value The measure score.
         */
        public MeasureReportGroupComponent setMeasureScore(long value) { 
              this.measureScore = new DecimalType();
            this.measureScore.setValue(value);
          return this;
        }

        /**
         * @param value The measure score.
         */
        public MeasureReportGroupComponent setMeasureScore(double value) { 
              this.measureScore = new DecimalType();
            this.measureScore.setValue(value);
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

        public boolean hasStratifier() { 
          if (this.stratifier == null)
            return false;
          for (MeasureReportGroupStratifierComponent item : this.stratifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #stratifier} (When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.)
         */
    // syntactic sugar
        public MeasureReportGroupStratifierComponent addStratifier() { //3
          MeasureReportGroupStratifierComponent t = new MeasureReportGroupStratifierComponent();
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureReportGroupStratifierComponent>();
          this.stratifier.add(t);
          return t;
        }

    // syntactic sugar
        public MeasureReportGroupComponent addStratifier(MeasureReportGroupStratifierComponent t) { //3
          if (t == null)
            return this;
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureReportGroupStratifierComponent>();
          this.stratifier.add(t);
          return this;
        }

        /**
         * @return {@link #supplementalData} (Supplemental data elements for the measure provide additional information requested by the measure for each patient involved in the populations.)
         */
        public List<MeasureReportGroupSupplementalDataComponent> getSupplementalData() { 
          if (this.supplementalData == null)
            this.supplementalData = new ArrayList<MeasureReportGroupSupplementalDataComponent>();
          return this.supplementalData;
        }

        public boolean hasSupplementalData() { 
          if (this.supplementalData == null)
            return false;
          for (MeasureReportGroupSupplementalDataComponent item : this.supplementalData)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #supplementalData} (Supplemental data elements for the measure provide additional information requested by the measure for each patient involved in the populations.)
         */
    // syntactic sugar
        public MeasureReportGroupSupplementalDataComponent addSupplementalData() { //3
          MeasureReportGroupSupplementalDataComponent t = new MeasureReportGroupSupplementalDataComponent();
          if (this.supplementalData == null)
            this.supplementalData = new ArrayList<MeasureReportGroupSupplementalDataComponent>();
          this.supplementalData.add(t);
          return t;
        }

    // syntactic sugar
        public MeasureReportGroupComponent addSupplementalData(MeasureReportGroupSupplementalDataComponent t) { //3
          if (t == null)
            return this;
          if (this.supplementalData == null)
            this.supplementalData = new ArrayList<MeasureReportGroupSupplementalDataComponent>();
          this.supplementalData.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "The identifier of the population group as defined in the measure definition.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("population", "", "The populations that make up the population group, one for each type of population appropriate for the measure.", 0, java.lang.Integer.MAX_VALUE, population));
          childrenList.add(new Property("measureScore", "decimal", "The measure score.", 0, java.lang.Integer.MAX_VALUE, measureScore));
          childrenList.add(new Property("stratifier", "", "When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the measure.", 0, java.lang.Integer.MAX_VALUE, stratifier));
          childrenList.add(new Property("supplementalData", "", "Supplemental data elements for the measure provide additional information requested by the measure for each patient involved in the populations.", 0, java.lang.Integer.MAX_VALUE, supplementalData));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MeasureReportGroupPopulationComponent
        case -386313260: /*measureScore*/ return this.measureScore == null ? new Base[0] : new Base[] {this.measureScore}; // DecimalType
        case 90983669: /*stratifier*/ return this.stratifier == null ? new Base[0] : this.stratifier.toArray(new Base[this.stratifier.size()]); // MeasureReportGroupStratifierComponent
        case 1447496814: /*supplementalData*/ return this.supplementalData == null ? new Base[0] : this.supplementalData.toArray(new Base[this.supplementalData.size()]); // MeasureReportGroupSupplementalDataComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -2023558323: // population
          this.getPopulation().add((MeasureReportGroupPopulationComponent) value); // MeasureReportGroupPopulationComponent
          break;
        case -386313260: // measureScore
          this.measureScore = castToDecimal(value); // DecimalType
          break;
        case 90983669: // stratifier
          this.getStratifier().add((MeasureReportGroupStratifierComponent) value); // MeasureReportGroupStratifierComponent
          break;
        case 1447496814: // supplementalData
          this.getSupplementalData().add((MeasureReportGroupSupplementalDataComponent) value); // MeasureReportGroupSupplementalDataComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("population"))
          this.getPopulation().add((MeasureReportGroupPopulationComponent) value);
        else if (name.equals("measureScore"))
          this.measureScore = castToDecimal(value); // DecimalType
        else if (name.equals("stratifier"))
          this.getStratifier().add((MeasureReportGroupStratifierComponent) value);
        else if (name.equals("supplementalData"))
          this.getSupplementalData().add((MeasureReportGroupSupplementalDataComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -2023558323:  return addPopulation(); // MeasureReportGroupPopulationComponent
        case -386313260: throw new FHIRException("Cannot make property measureScore as it is not a complex type"); // DecimalType
        case 90983669:  return addStratifier(); // MeasureReportGroupStratifierComponent
        case 1447496814:  return addSupplementalData(); // MeasureReportGroupSupplementalDataComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else if (name.equals("measureScore")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.measureScore");
        }
        else if (name.equals("stratifier")) {
          return addStratifier();
        }
        else if (name.equals("supplementalData")) {
          return addSupplementalData();
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupComponent copy() {
        MeasureReportGroupComponent dst = new MeasureReportGroupComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
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
        if (supplementalData != null) {
          dst.supplementalData = new ArrayList<MeasureReportGroupSupplementalDataComponent>();
          for (MeasureReportGroupSupplementalDataComponent i : supplementalData)
            dst.supplementalData.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupComponent))
          return false;
        MeasureReportGroupComponent o = (MeasureReportGroupComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(population, o.population, true)
           && compareDeep(measureScore, o.measureScore, true) && compareDeep(stratifier, o.stratifier, true)
           && compareDeep(supplementalData, o.supplementalData, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupComponent))
          return false;
        MeasureReportGroupComponent o = (MeasureReportGroupComponent) other;
        return compareValues(measureScore, o.measureScore, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (population == null || population.isEmpty())
           && (measureScore == null || measureScore.isEmpty()) && (stratifier == null || stratifier.isEmpty())
           && (supplementalData == null || supplementalData.isEmpty());
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
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-score", formalDefinition="The type of the population." )
        protected CodeType type;

        /**
         * The number of members of the population.
         */
        @Child(name = "count", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Size of the population", formalDefinition="The number of members of the population." )
        protected IntegerType count;

        /**
         * This element refers to a List of patient level MeasureReport resources, one for each patient in this population.
         */
        @Child(name = "patients", type = {ListResource.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For patient-list reports, the patients in this population", formalDefinition="This element refers to a List of patient level MeasureReport resources, one for each patient in this population." )
        protected Reference patients;

        /**
         * The actual object that is the target of the reference (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        protected ListResource patientsTarget;

        private static final long serialVersionUID = 407500224L;

    /**
     * Constructor
     */
      public MeasureReportGroupPopulationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupPopulationComponent(CodeType type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of the population.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the population.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MeasureReportGroupPopulationComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the population.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the population.
         */
        public MeasureReportGroupPopulationComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
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
         * @return {@link #patients} (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public Reference getPatients() { 
          if (this.patients == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.patients");
            else if (Configuration.doAutoCreate())
              this.patients = new Reference(); // cc
          return this.patients;
        }

        public boolean hasPatients() { 
          return this.patients != null && !this.patients.isEmpty();
        }

        /**
         * @param value {@link #patients} (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public MeasureReportGroupPopulationComponent setPatients(Reference value) { 
          this.patients = value;
          return this;
        }

        /**
         * @return {@link #patients} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public ListResource getPatientsTarget() { 
          if (this.patientsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupPopulationComponent.patients");
            else if (Configuration.doAutoCreate())
              this.patientsTarget = new ListResource(); // aa
          return this.patientsTarget;
        }

        /**
         * @param value {@link #patients} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public MeasureReportGroupPopulationComponent setPatientsTarget(ListResource value) { 
          this.patientsTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of the population.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("count", "integer", "The number of members of the population.", 0, java.lang.Integer.MAX_VALUE, count));
          childrenList.add(new Property("patients", "Reference(List)", "This element refers to a List of patient level MeasureReport resources, one for each patient in this population.", 0, java.lang.Integer.MAX_VALUE, patients));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case 1235842574: /*patients*/ return this.patients == null ? new Base[0] : new Base[] {this.patients}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          break;
        case 1235842574: // patients
          this.patients = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("count"))
          this.count = castToInteger(value); // IntegerType
        else if (name.equals("patients"))
          this.patients = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case 94851343: throw new FHIRException("Cannot make property count as it is not a complex type"); // IntegerType
        case 1235842574:  return getPatients(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.type");
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.count");
        }
        else if (name.equals("patients")) {
          this.patients = new Reference();
          return this.patients;
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupPopulationComponent copy() {
        MeasureReportGroupPopulationComponent dst = new MeasureReportGroupPopulationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.count = count == null ? null : count.copy();
        dst.patients = patients == null ? null : patients.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupPopulationComponent))
          return false;
        MeasureReportGroupPopulationComponent o = (MeasureReportGroupPopulationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(count, o.count, true) && compareDeep(patients, o.patients, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupPopulationComponent))
          return false;
        MeasureReportGroupPopulationComponent o = (MeasureReportGroupPopulationComponent) other;
        return compareValues(type, o.type, true) && compareValues(count, o.count, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (count == null || count.isEmpty())
           && (patients == null || patients.isEmpty());
      }

  public String fhirType() {
    return "MeasureReport.group.population";

  }

  }

    @Block()
    public static class MeasureReportGroupStratifierComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier of this stratifier, as defined in the measure definition.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier of the stratifier", formalDefinition="The identifier of this stratifier, as defined in the measure definition." )
        protected Identifier identifier;

        /**
         * This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.
         */
        @Child(name = "group", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Stratum results, one for each unique value in the stratifier", formalDefinition="This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value." )
        protected List<MeasureReportGroupStratifierGroupComponent> group;

        private static final long serialVersionUID = -229867715L;

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #identifier} (The identifier of this stratifier, as defined in the measure definition.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier of this stratifier, as defined in the measure definition.)
         */
        public MeasureReportGroupStratifierComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #group} (This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.)
         */
        public List<MeasureReportGroupStratifierGroupComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<MeasureReportGroupStratifierGroupComponent>();
          return this.group;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (MeasureReportGroupStratifierGroupComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #group} (This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.)
         */
    // syntactic sugar
        public MeasureReportGroupStratifierGroupComponent addGroup() { //3
          MeasureReportGroupStratifierGroupComponent t = new MeasureReportGroupStratifierGroupComponent();
          if (this.group == null)
            this.group = new ArrayList<MeasureReportGroupStratifierGroupComponent>();
          this.group.add(t);
          return t;
        }

    // syntactic sugar
        public MeasureReportGroupStratifierComponent addGroup(MeasureReportGroupStratifierGroupComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<MeasureReportGroupStratifierGroupComponent>();
          this.group.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "The identifier of this stratifier, as defined in the measure definition.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("group", "", "This element contains the results for a single stratum within the stratifier. For example, when stratifying on administrative gender, there will be four strata, one for each possible gender value.", 0, java.lang.Integer.MAX_VALUE, group));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // MeasureReportGroupStratifierGroupComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 98629247: // group
          this.getGroup().add((MeasureReportGroupStratifierGroupComponent) value); // MeasureReportGroupStratifierGroupComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("group"))
          this.getGroup().add((MeasureReportGroupStratifierGroupComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case 98629247:  return addGroup(); // MeasureReportGroupStratifierGroupComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupStratifierComponent copy() {
        MeasureReportGroupStratifierComponent dst = new MeasureReportGroupStratifierComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (group != null) {
          dst.group = new ArrayList<MeasureReportGroupStratifierGroupComponent>();
          for (MeasureReportGroupStratifierGroupComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupStratifierComponent))
          return false;
        MeasureReportGroupStratifierComponent o = (MeasureReportGroupStratifierComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupStratifierComponent))
          return false;
        MeasureReportGroupStratifierComponent o = (MeasureReportGroupStratifierComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (group == null || group.isEmpty())
          ;
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier";

  }

  }

    @Block()
    public static class MeasureReportGroupStratifierGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.
         */
        @Child(name = "value", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The stratum value, e.g. male", formalDefinition="The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique." )
        protected StringType value;

        /**
         * The populations that make up the stratum, one for each type of population appropriate to the measure.
         */
        @Child(name = "population", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Population results in this stratum", formalDefinition="The populations that make up the stratum, one for each type of population appropriate to the measure." )
        protected List<MeasureReportGroupStratifierGroupPopulationComponent> population;

        /**
         * The measure score for this stratum.
         */
        @Child(name = "measureScore", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The measure score", formalDefinition="The measure score for this stratum." )
        protected DecimalType measureScore;

        private static final long serialVersionUID = -1663404087L;

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierGroupComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierGroupComponent(StringType value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierGroupComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public MeasureReportGroupStratifierGroupComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.
         */
        public MeasureReportGroupStratifierGroupComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @return {@link #population} (The populations that make up the stratum, one for each type of population appropriate to the measure.)
         */
        public List<MeasureReportGroupStratifierGroupPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupStratifierGroupPopulationComponent>();
          return this.population;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MeasureReportGroupStratifierGroupPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #population} (The populations that make up the stratum, one for each type of population appropriate to the measure.)
         */
    // syntactic sugar
        public MeasureReportGroupStratifierGroupPopulationComponent addPopulation() { //3
          MeasureReportGroupStratifierGroupPopulationComponent t = new MeasureReportGroupStratifierGroupPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupStratifierGroupPopulationComponent>();
          this.population.add(t);
          return t;
        }

    // syntactic sugar
        public MeasureReportGroupStratifierGroupComponent addPopulation(MeasureReportGroupStratifierGroupPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MeasureReportGroupStratifierGroupPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return {@link #measureScore} (The measure score for this stratum.). This is the underlying object with id, value and extensions. The accessor "getMeasureScore" gives direct access to the value
         */
        public DecimalType getMeasureScoreElement() { 
          if (this.measureScore == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierGroupComponent.measureScore");
            else if (Configuration.doAutoCreate())
              this.measureScore = new DecimalType(); // bb
          return this.measureScore;
        }

        public boolean hasMeasureScoreElement() { 
          return this.measureScore != null && !this.measureScore.isEmpty();
        }

        public boolean hasMeasureScore() { 
          return this.measureScore != null && !this.measureScore.isEmpty();
        }

        /**
         * @param value {@link #measureScore} (The measure score for this stratum.). This is the underlying object with id, value and extensions. The accessor "getMeasureScore" gives direct access to the value
         */
        public MeasureReportGroupStratifierGroupComponent setMeasureScoreElement(DecimalType value) { 
          this.measureScore = value;
          return this;
        }

        /**
         * @return The measure score for this stratum.
         */
        public BigDecimal getMeasureScore() { 
          return this.measureScore == null ? null : this.measureScore.getValue();
        }

        /**
         * @param value The measure score for this stratum.
         */
        public MeasureReportGroupStratifierGroupComponent setMeasureScore(BigDecimal value) { 
          if (value == null)
            this.measureScore = null;
          else {
            if (this.measureScore == null)
              this.measureScore = new DecimalType();
            this.measureScore.setValue(value);
          }
          return this;
        }

        /**
         * @param value The measure score for this stratum.
         */
        public MeasureReportGroupStratifierGroupComponent setMeasureScore(long value) { 
              this.measureScore = new DecimalType();
            this.measureScore.setValue(value);
          return this;
        }

        /**
         * @param value The measure score for this stratum.
         */
        public MeasureReportGroupStratifierGroupComponent setMeasureScore(double value) { 
              this.measureScore = new DecimalType();
            this.measureScore.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value", "string", "The value for this stratum, expressed as a string. When defining stratifiers on complex values, the value must be rendered such that the value for each stratum within the stratifier is unique.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("population", "", "The populations that make up the stratum, one for each type of population appropriate to the measure.", 0, java.lang.Integer.MAX_VALUE, population));
          childrenList.add(new Property("measureScore", "decimal", "The measure score for this stratum.", 0, java.lang.Integer.MAX_VALUE, measureScore));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MeasureReportGroupStratifierGroupPopulationComponent
        case -386313260: /*measureScore*/ return this.measureScore == null ? new Base[0] : new Base[] {this.measureScore}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        case -2023558323: // population
          this.getPopulation().add((MeasureReportGroupStratifierGroupPopulationComponent) value); // MeasureReportGroupStratifierGroupPopulationComponent
          break;
        case -386313260: // measureScore
          this.measureScore = castToDecimal(value); // DecimalType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value"))
          this.value = castToString(value); // StringType
        else if (name.equals("population"))
          this.getPopulation().add((MeasureReportGroupStratifierGroupPopulationComponent) value);
        else if (name.equals("measureScore"))
          this.measureScore = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        case -2023558323:  return addPopulation(); // MeasureReportGroupStratifierGroupPopulationComponent
        case -386313260: throw new FHIRException("Cannot make property measureScore as it is not a complex type"); // DecimalType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.value");
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else if (name.equals("measureScore")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.measureScore");
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupStratifierGroupComponent copy() {
        MeasureReportGroupStratifierGroupComponent dst = new MeasureReportGroupStratifierGroupComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        if (population != null) {
          dst.population = new ArrayList<MeasureReportGroupStratifierGroupPopulationComponent>();
          for (MeasureReportGroupStratifierGroupPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        dst.measureScore = measureScore == null ? null : measureScore.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupStratifierGroupComponent))
          return false;
        MeasureReportGroupStratifierGroupComponent o = (MeasureReportGroupStratifierGroupComponent) other;
        return compareDeep(value, o.value, true) && compareDeep(population, o.population, true) && compareDeep(measureScore, o.measureScore, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupStratifierGroupComponent))
          return false;
        MeasureReportGroupStratifierGroupComponent o = (MeasureReportGroupStratifierGroupComponent) other;
        return compareValues(value, o.value, true) && compareValues(measureScore, o.measureScore, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty()) && (population == null || population.isEmpty())
           && (measureScore == null || measureScore.isEmpty());
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier.group";

  }

  }

    @Block()
    public static class MeasureReportGroupStratifierGroupPopulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of the population.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-score", formalDefinition="The type of the population." )
        protected CodeType type;

        /**
         * The number of members of the population in this stratum.
         */
        @Child(name = "count", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Size of the population", formalDefinition="The number of members of the population in this stratum." )
        protected IntegerType count;

        /**
         * This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.
         */
        @Child(name = "patients", type = {ListResource.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For patient-list reports, the patients in this population", formalDefinition="This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum." )
        protected Reference patients;

        /**
         * The actual object that is the target of the reference (This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.)
         */
        protected ListResource patientsTarget;

        private static final long serialVersionUID = 407500224L;

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierGroupPopulationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupStratifierGroupPopulationComponent(CodeType type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of the population.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierGroupPopulationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the population.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MeasureReportGroupStratifierGroupPopulationComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the population.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the population.
         */
        public MeasureReportGroupStratifierGroupPopulationComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #count} (The number of members of the population in this stratum.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public IntegerType getCountElement() { 
          if (this.count == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierGroupPopulationComponent.count");
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
        public MeasureReportGroupStratifierGroupPopulationComponent setCountElement(IntegerType value) { 
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
        public MeasureReportGroupStratifierGroupPopulationComponent setCount(int value) { 
            if (this.count == null)
              this.count = new IntegerType();
            this.count.setValue(value);
          return this;
        }

        /**
         * @return {@link #patients} (This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.)
         */
        public Reference getPatients() { 
          if (this.patients == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierGroupPopulationComponent.patients");
            else if (Configuration.doAutoCreate())
              this.patients = new Reference(); // cc
          return this.patients;
        }

        public boolean hasPatients() { 
          return this.patients != null && !this.patients.isEmpty();
        }

        /**
         * @param value {@link #patients} (This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.)
         */
        public MeasureReportGroupStratifierGroupPopulationComponent setPatients(Reference value) { 
          this.patients = value;
          return this;
        }

        /**
         * @return {@link #patients} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.)
         */
        public ListResource getPatientsTarget() { 
          if (this.patientsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupStratifierGroupPopulationComponent.patients");
            else if (Configuration.doAutoCreate())
              this.patientsTarget = new ListResource(); // aa
          return this.patientsTarget;
        }

        /**
         * @param value {@link #patients} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.)
         */
        public MeasureReportGroupStratifierGroupPopulationComponent setPatientsTarget(ListResource value) { 
          this.patientsTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of the population.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("count", "integer", "The number of members of the population in this stratum.", 0, java.lang.Integer.MAX_VALUE, count));
          childrenList.add(new Property("patients", "Reference(List)", "This element refers to a List of patient level MeasureReport resources, one for each patient in this population in this stratum.", 0, java.lang.Integer.MAX_VALUE, patients));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case 1235842574: /*patients*/ return this.patients == null ? new Base[0] : new Base[] {this.patients}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          break;
        case 1235842574: // patients
          this.patients = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("count"))
          this.count = castToInteger(value); // IntegerType
        else if (name.equals("patients"))
          this.patients = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case 94851343: throw new FHIRException("Cannot make property count as it is not a complex type"); // IntegerType
        case 1235842574:  return getPatients(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.type");
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.count");
        }
        else if (name.equals("patients")) {
          this.patients = new Reference();
          return this.patients;
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupStratifierGroupPopulationComponent copy() {
        MeasureReportGroupStratifierGroupPopulationComponent dst = new MeasureReportGroupStratifierGroupPopulationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.count = count == null ? null : count.copy();
        dst.patients = patients == null ? null : patients.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupStratifierGroupPopulationComponent))
          return false;
        MeasureReportGroupStratifierGroupPopulationComponent o = (MeasureReportGroupStratifierGroupPopulationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(count, o.count, true) && compareDeep(patients, o.patients, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupStratifierGroupPopulationComponent))
          return false;
        MeasureReportGroupStratifierGroupPopulationComponent o = (MeasureReportGroupStratifierGroupPopulationComponent) other;
        return compareValues(type, o.type, true) && compareValues(count, o.count, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (count == null || count.isEmpty())
           && (patients == null || patients.isEmpty());
      }

  public String fhirType() {
    return "MeasureReport.group.stratifier.group.population";

  }

  }

    @Block()
    public static class MeasureReportGroupSupplementalDataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier of the supplemental data element as defined in the measure.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier of the supplemental data element", formalDefinition="The identifier of the supplemental data element as defined in the measure." )
        protected Identifier identifier;

        /**
         * This element contains the results for a single value within the supplemental data. For example, when reporting supplemental data for administrative gender, there will be four groups, one for each possible gender value.
         */
        @Child(name = "group", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supplemental data results, one for each unique supplemental data value", formalDefinition="This element contains the results for a single value within the supplemental data. For example, when reporting supplemental data for administrative gender, there will be four groups, one for each possible gender value." )
        protected List<MeasureReportGroupSupplementalDataGroupComponent> group;

        private static final long serialVersionUID = -1254392714L;

    /**
     * Constructor
     */
      public MeasureReportGroupSupplementalDataComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupSupplementalDataComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #identifier} (The identifier of the supplemental data element as defined in the measure.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupSupplementalDataComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier of the supplemental data element as defined in the measure.)
         */
        public MeasureReportGroupSupplementalDataComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #group} (This element contains the results for a single value within the supplemental data. For example, when reporting supplemental data for administrative gender, there will be four groups, one for each possible gender value.)
         */
        public List<MeasureReportGroupSupplementalDataGroupComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<MeasureReportGroupSupplementalDataGroupComponent>();
          return this.group;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (MeasureReportGroupSupplementalDataGroupComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #group} (This element contains the results for a single value within the supplemental data. For example, when reporting supplemental data for administrative gender, there will be four groups, one for each possible gender value.)
         */
    // syntactic sugar
        public MeasureReportGroupSupplementalDataGroupComponent addGroup() { //3
          MeasureReportGroupSupplementalDataGroupComponent t = new MeasureReportGroupSupplementalDataGroupComponent();
          if (this.group == null)
            this.group = new ArrayList<MeasureReportGroupSupplementalDataGroupComponent>();
          this.group.add(t);
          return t;
        }

    // syntactic sugar
        public MeasureReportGroupSupplementalDataComponent addGroup(MeasureReportGroupSupplementalDataGroupComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<MeasureReportGroupSupplementalDataGroupComponent>();
          this.group.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "The identifier of the supplemental data element as defined in the measure.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("group", "", "This element contains the results for a single value within the supplemental data. For example, when reporting supplemental data for administrative gender, there will be four groups, one for each possible gender value.", 0, java.lang.Integer.MAX_VALUE, group));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // MeasureReportGroupSupplementalDataGroupComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 98629247: // group
          this.getGroup().add((MeasureReportGroupSupplementalDataGroupComponent) value); // MeasureReportGroupSupplementalDataGroupComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("group"))
          this.getGroup().add((MeasureReportGroupSupplementalDataGroupComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case 98629247:  return addGroup(); // MeasureReportGroupSupplementalDataGroupComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupSupplementalDataComponent copy() {
        MeasureReportGroupSupplementalDataComponent dst = new MeasureReportGroupSupplementalDataComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (group != null) {
          dst.group = new ArrayList<MeasureReportGroupSupplementalDataGroupComponent>();
          for (MeasureReportGroupSupplementalDataGroupComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupSupplementalDataComponent))
          return false;
        MeasureReportGroupSupplementalDataComponent o = (MeasureReportGroupSupplementalDataComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupSupplementalDataComponent))
          return false;
        MeasureReportGroupSupplementalDataComponent o = (MeasureReportGroupSupplementalDataComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (group == null || group.isEmpty())
          ;
      }

  public String fhirType() {
    return "MeasureReport.group.supplementalData";

  }

  }

    @Block()
    public static class MeasureReportGroupSupplementalDataGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique.
         */
        @Child(name = "value", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The data value, e.g. male", formalDefinition="The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique." )
        protected StringType value;

        /**
         * The number of members in the supplemental data group.
         */
        @Child(name = "count", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Number of members in the group", formalDefinition="The number of members in the supplemental data group." )
        protected IntegerType count;

        /**
         * This element refers to a List of patient level MeasureReport resources, one for each patient in this population.
         */
        @Child(name = "patients", type = {ListResource.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For patient-list reports, the patients in this population", formalDefinition="This element refers to a List of patient level MeasureReport resources, one for each patient in this population." )
        protected Reference patients;

        /**
         * The actual object that is the target of the reference (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        protected ListResource patientsTarget;

        private static final long serialVersionUID = 1011446829L;

    /**
     * Constructor
     */
      public MeasureReportGroupSupplementalDataGroupComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureReportGroupSupplementalDataGroupComponent(StringType value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupSupplementalDataGroupComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public MeasureReportGroupSupplementalDataGroupComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique.
         */
        public MeasureReportGroupSupplementalDataGroupComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @return {@link #count} (The number of members in the supplemental data group.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public IntegerType getCountElement() { 
          if (this.count == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupSupplementalDataGroupComponent.count");
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
         * @param value {@link #count} (The number of members in the supplemental data group.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public MeasureReportGroupSupplementalDataGroupComponent setCountElement(IntegerType value) { 
          this.count = value;
          return this;
        }

        /**
         * @return The number of members in the supplemental data group.
         */
        public int getCount() { 
          return this.count == null || this.count.isEmpty() ? 0 : this.count.getValue();
        }

        /**
         * @param value The number of members in the supplemental data group.
         */
        public MeasureReportGroupSupplementalDataGroupComponent setCount(int value) { 
            if (this.count == null)
              this.count = new IntegerType();
            this.count.setValue(value);
          return this;
        }

        /**
         * @return {@link #patients} (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public Reference getPatients() { 
          if (this.patients == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupSupplementalDataGroupComponent.patients");
            else if (Configuration.doAutoCreate())
              this.patients = new Reference(); // cc
          return this.patients;
        }

        public boolean hasPatients() { 
          return this.patients != null && !this.patients.isEmpty();
        }

        /**
         * @param value {@link #patients} (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public MeasureReportGroupSupplementalDataGroupComponent setPatients(Reference value) { 
          this.patients = value;
          return this;
        }

        /**
         * @return {@link #patients} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public ListResource getPatientsTarget() { 
          if (this.patientsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureReportGroupSupplementalDataGroupComponent.patients");
            else if (Configuration.doAutoCreate())
              this.patientsTarget = new ListResource(); // aa
          return this.patientsTarget;
        }

        /**
         * @param value {@link #patients} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This element refers to a List of patient level MeasureReport resources, one for each patient in this population.)
         */
        public MeasureReportGroupSupplementalDataGroupComponent setPatientsTarget(ListResource value) { 
          this.patientsTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value", "string", "The value for this supplemental data element, expressed as a string.  When defining supplemental data  on complex values, the value must be rendered such that the value for each group within the supplemental data element is unique.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("count", "integer", "The number of members in the supplemental data group.", 0, java.lang.Integer.MAX_VALUE, count));
          childrenList.add(new Property("patients", "Reference(List)", "This element refers to a List of patient level MeasureReport resources, one for each patient in this population.", 0, java.lang.Integer.MAX_VALUE, patients));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case 1235842574: /*patients*/ return this.patients == null ? new Base[0] : new Base[] {this.patients}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          break;
        case 1235842574: // patients
          this.patients = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value"))
          this.value = castToString(value); // StringType
        else if (name.equals("count"))
          this.count = castToInteger(value); // IntegerType
        else if (name.equals("patients"))
          this.patients = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        case 94851343: throw new FHIRException("Cannot make property count as it is not a complex type"); // IntegerType
        case 1235842574:  return getPatients(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.value");
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.count");
        }
        else if (name.equals("patients")) {
          this.patients = new Reference();
          return this.patients;
        }
        else
          return super.addChild(name);
      }

      public MeasureReportGroupSupplementalDataGroupComponent copy() {
        MeasureReportGroupSupplementalDataGroupComponent dst = new MeasureReportGroupSupplementalDataGroupComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        dst.count = count == null ? null : count.copy();
        dst.patients = patients == null ? null : patients.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReportGroupSupplementalDataGroupComponent))
          return false;
        MeasureReportGroupSupplementalDataGroupComponent o = (MeasureReportGroupSupplementalDataGroupComponent) other;
        return compareDeep(value, o.value, true) && compareDeep(count, o.count, true) && compareDeep(patients, o.patients, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReportGroupSupplementalDataGroupComponent))
          return false;
        MeasureReportGroupSupplementalDataGroupComponent o = (MeasureReportGroupSupplementalDataGroupComponent) other;
        return compareValues(value, o.value, true) && compareValues(count, o.count, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty()) && (count == null || count.isEmpty())
           && (patients == null || patients.isEmpty());
      }

  public String fhirType() {
    return "MeasureReport.group.supplementalData.group";

  }

  }

    /**
     * A reference to the Measure that was evaluated to produce this report.
     */
    @Child(name = "measure", type = {Measure.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Measure that was evaluated", formalDefinition="A reference to the Measure that was evaluated to produce this report." )
    protected Reference measure;

    /**
     * The actual object that is the target of the reference (A reference to the Measure that was evaluated to produce this report.)
     */
    protected Measure measureTarget;

    /**
     * The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure.
     */
    @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="individual | patient-list | summary", formalDefinition="The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure." )
    protected Enumeration<MeasureReportType> type;

    /**
     * Optional Patient if the report was requested for a single patient.
     */
    @Child(name = "patient", type = {Patient.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Optional Patient", formalDefinition="Optional Patient if the report was requested for a single patient." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Optional Patient if the report was requested for a single patient.)
     */
    protected Patient patientTarget;

    /**
     * The reporting period for which the report was calculated.
     */
    @Child(name = "period", type = {Period.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reporting period", formalDefinition="The reporting period for which the report was calculated." )
    protected Period period;

    /**
     * The report status. No data will be available until the report status is complete.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="complete | pending | error", formalDefinition="The report status. No data will be available until the report status is complete." )
    protected Enumeration<MeasureReportStatus> status;

    /**
     * The date this measure report was generated.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date the report was generated", formalDefinition="The date this measure report was generated." )
    protected DateTimeType date;

    /**
     * Reporting Organization.
     */
    @Child(name = "reportingOrganization", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reporting Organization", formalDefinition="Reporting Organization." )
    protected Reference reportingOrganization;

    /**
     * The actual object that is the target of the reference (Reporting Organization.)
     */
    protected Organization reportingOrganizationTarget;

    /**
     * The results of the calculation, one for each population group in the measure.
     */
    @Child(name = "group", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Measure results for each group", formalDefinition="The results of the calculation, one for each population group in the measure." )
    protected List<MeasureReportGroupComponent> group;

    /**
     * A reference to a Bundle containing the Resources that were used in the evaluation of this report.
     */
    @Child(name = "evaluatedResources", type = {Bundle.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Evaluated Resources", formalDefinition="A reference to a Bundle containing the Resources that were used in the evaluation of this report." )
    protected Reference evaluatedResources;

    /**
     * The actual object that is the target of the reference (A reference to a Bundle containing the Resources that were used in the evaluation of this report.)
     */
    protected Bundle evaluatedResourcesTarget;

    private static final long serialVersionUID = -891268298L;

  /**
   * Constructor
   */
    public MeasureReport() {
      super();
    }

  /**
   * Constructor
   */
    public MeasureReport(Reference measure, Enumeration<MeasureReportType> type, Period period, Enumeration<MeasureReportStatus> status) {
      super();
      this.measure = measure;
      this.type = type;
      this.period = period;
      this.status = status;
    }

    /**
     * @return {@link #measure} (A reference to the Measure that was evaluated to produce this report.)
     */
    public Reference getMeasure() { 
      if (this.measure == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.measure");
        else if (Configuration.doAutoCreate())
          this.measure = new Reference(); // cc
      return this.measure;
    }

    public boolean hasMeasure() { 
      return this.measure != null && !this.measure.isEmpty();
    }

    /**
     * @param value {@link #measure} (A reference to the Measure that was evaluated to produce this report.)
     */
    public MeasureReport setMeasure(Reference value) { 
      this.measure = value;
      return this;
    }

    /**
     * @return {@link #measure} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the Measure that was evaluated to produce this report.)
     */
    public Measure getMeasureTarget() { 
      if (this.measureTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.measure");
        else if (Configuration.doAutoCreate())
          this.measureTarget = new Measure(); // aa
      return this.measureTarget;
    }

    /**
     * @param value {@link #measure} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the Measure that was evaluated to produce this report.)
     */
    public MeasureReport setMeasureTarget(Measure value) { 
      this.measureTarget = value;
      return this;
    }

    /**
     * @return {@link #type} (The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
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
     * @param value {@link #type} (The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public MeasureReport setTypeElement(Enumeration<MeasureReportType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure.
     */
    public MeasureReportType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure.
     */
    public MeasureReport setType(MeasureReportType value) { 
        if (this.type == null)
          this.type = new Enumeration<MeasureReportType>(new MeasureReportTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (Optional Patient if the report was requested for a single patient.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Optional Patient if the report was requested for a single patient.)
     */
    public MeasureReport setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Optional Patient if the report was requested for a single patient.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Optional Patient if the report was requested for a single patient.)
     */
    public MeasureReport setPatientTarget(Patient value) { 
      this.patientTarget = value;
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
     * @return {@link #status} (The report status. No data will be available until the report status is complete.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (The report status. No data will be available until the report status is complete.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MeasureReport setStatusElement(Enumeration<MeasureReportStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The report status. No data will be available until the report status is complete.
     */
    public MeasureReportStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The report status. No data will be available until the report status is complete.
     */
    public MeasureReport setStatus(MeasureReportStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MeasureReportStatus>(new MeasureReportStatusEnumFactory());
        this.status.setValue(value);
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
     * @return {@link #reportingOrganization} (Reporting Organization.)
     */
    public Reference getReportingOrganization() { 
      if (this.reportingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.reportingOrganization");
        else if (Configuration.doAutoCreate())
          this.reportingOrganization = new Reference(); // cc
      return this.reportingOrganization;
    }

    public boolean hasReportingOrganization() { 
      return this.reportingOrganization != null && !this.reportingOrganization.isEmpty();
    }

    /**
     * @param value {@link #reportingOrganization} (Reporting Organization.)
     */
    public MeasureReport setReportingOrganization(Reference value) { 
      this.reportingOrganization = value;
      return this;
    }

    /**
     * @return {@link #reportingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reporting Organization.)
     */
    public Organization getReportingOrganizationTarget() { 
      if (this.reportingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.reportingOrganization");
        else if (Configuration.doAutoCreate())
          this.reportingOrganizationTarget = new Organization(); // aa
      return this.reportingOrganizationTarget;
    }

    /**
     * @param value {@link #reportingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reporting Organization.)
     */
    public MeasureReport setReportingOrganizationTarget(Organization value) { 
      this.reportingOrganizationTarget = value;
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

    public boolean hasGroup() { 
      if (this.group == null)
        return false;
      for (MeasureReportGroupComponent item : this.group)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #group} (The results of the calculation, one for each population group in the measure.)
     */
    // syntactic sugar
    public MeasureReportGroupComponent addGroup() { //3
      MeasureReportGroupComponent t = new MeasureReportGroupComponent();
      if (this.group == null)
        this.group = new ArrayList<MeasureReportGroupComponent>();
      this.group.add(t);
      return t;
    }

    // syntactic sugar
    public MeasureReport addGroup(MeasureReportGroupComponent t) { //3
      if (t == null)
        return this;
      if (this.group == null)
        this.group = new ArrayList<MeasureReportGroupComponent>();
      this.group.add(t);
      return this;
    }

    /**
     * @return {@link #evaluatedResources} (A reference to a Bundle containing the Resources that were used in the evaluation of this report.)
     */
    public Reference getEvaluatedResources() { 
      if (this.evaluatedResources == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.evaluatedResources");
        else if (Configuration.doAutoCreate())
          this.evaluatedResources = new Reference(); // cc
      return this.evaluatedResources;
    }

    public boolean hasEvaluatedResources() { 
      return this.evaluatedResources != null && !this.evaluatedResources.isEmpty();
    }

    /**
     * @param value {@link #evaluatedResources} (A reference to a Bundle containing the Resources that were used in the evaluation of this report.)
     */
    public MeasureReport setEvaluatedResources(Reference value) { 
      this.evaluatedResources = value;
      return this;
    }

    /**
     * @return {@link #evaluatedResources} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a Bundle containing the Resources that were used in the evaluation of this report.)
     */
    public Bundle getEvaluatedResourcesTarget() { 
      if (this.evaluatedResourcesTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MeasureReport.evaluatedResources");
        else if (Configuration.doAutoCreate())
          this.evaluatedResourcesTarget = new Bundle(); // aa
      return this.evaluatedResourcesTarget;
    }

    /**
     * @param value {@link #evaluatedResources} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a Bundle containing the Resources that were used in the evaluation of this report.)
     */
    public MeasureReport setEvaluatedResourcesTarget(Bundle value) { 
      this.evaluatedResourcesTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("measure", "Reference(Measure)", "A reference to the Measure that was evaluated to produce this report.", 0, java.lang.Integer.MAX_VALUE, measure));
        childrenList.add(new Property("type", "code", "The type of measure report. This may be an individual report, which provides a single patient's score for the measure, a patient listing, which returns the list of patients that meet the various criteria in the measure, or a summary report, which returns a population count for each criteria in the measure.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("patient", "Reference(Patient)", "Optional Patient if the report was requested for a single patient.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("period", "Period", "The reporting period for which the report was calculated.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("status", "code", "The report status. No data will be available until the report status is complete.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "The date this measure report was generated.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("reportingOrganization", "Reference(Organization)", "Reporting Organization.", 0, java.lang.Integer.MAX_VALUE, reportingOrganization));
        childrenList.add(new Property("group", "", "The results of the calculation, one for each population group in the measure.", 0, java.lang.Integer.MAX_VALUE, group));
        childrenList.add(new Property("evaluatedResources", "Reference(Bundle)", "A reference to a Bundle containing the Resources that were used in the evaluation of this report.", 0, java.lang.Integer.MAX_VALUE, evaluatedResources));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 938321246: /*measure*/ return this.measure == null ? new Base[0] : new Base[] {this.measure}; // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<MeasureReportType>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MeasureReportStatus>
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -2053950847: /*reportingOrganization*/ return this.reportingOrganization == null ? new Base[0] : new Base[] {this.reportingOrganization}; // Reference
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // MeasureReportGroupComponent
        case 1599836026: /*evaluatedResources*/ return this.evaluatedResources == null ? new Base[0] : new Base[] {this.evaluatedResources}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 938321246: // measure
          this.measure = castToReference(value); // Reference
          break;
        case 3575610: // type
          this.type = new MeasureReportTypeEnumFactory().fromType(value); // Enumeration<MeasureReportType>
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -892481550: // status
          this.status = new MeasureReportStatusEnumFactory().fromType(value); // Enumeration<MeasureReportStatus>
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -2053950847: // reportingOrganization
          this.reportingOrganization = castToReference(value); // Reference
          break;
        case 98629247: // group
          this.getGroup().add((MeasureReportGroupComponent) value); // MeasureReportGroupComponent
          break;
        case 1599836026: // evaluatedResources
          this.evaluatedResources = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("measure"))
          this.measure = castToReference(value); // Reference
        else if (name.equals("type"))
          this.type = new MeasureReportTypeEnumFactory().fromType(value); // Enumeration<MeasureReportType>
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("status"))
          this.status = new MeasureReportStatusEnumFactory().fromType(value); // Enumeration<MeasureReportStatus>
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("reportingOrganization"))
          this.reportingOrganization = castToReference(value); // Reference
        else if (name.equals("group"))
          this.getGroup().add((MeasureReportGroupComponent) value);
        else if (name.equals("evaluatedResources"))
          this.evaluatedResources = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938321246:  return getMeasure(); // Reference
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<MeasureReportType>
        case -791418107:  return getPatient(); // Reference
        case -991726143:  return getPeriod(); // Period
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MeasureReportStatus>
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -2053950847:  return getReportingOrganization(); // Reference
        case 98629247:  return addGroup(); // MeasureReportGroupComponent
        case 1599836026:  return getEvaluatedResources(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("measure")) {
          this.measure = new Reference();
          return this.measure;
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.type");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.status");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type MeasureReport.date");
        }
        else if (name.equals("reportingOrganization")) {
          this.reportingOrganization = new Reference();
          return this.reportingOrganization;
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else if (name.equals("evaluatedResources")) {
          this.evaluatedResources = new Reference();
          return this.evaluatedResources;
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
        dst.measure = measure == null ? null : measure.copy();
        dst.type = type == null ? null : type.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.period = period == null ? null : period.copy();
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        dst.reportingOrganization = reportingOrganization == null ? null : reportingOrganization.copy();
        if (group != null) {
          dst.group = new ArrayList<MeasureReportGroupComponent>();
          for (MeasureReportGroupComponent i : group)
            dst.group.add(i.copy());
        };
        dst.evaluatedResources = evaluatedResources == null ? null : evaluatedResources.copy();
        return dst;
      }

      protected MeasureReport typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureReport))
          return false;
        MeasureReport o = (MeasureReport) other;
        return compareDeep(measure, o.measure, true) && compareDeep(type, o.type, true) && compareDeep(patient, o.patient, true)
           && compareDeep(period, o.period, true) && compareDeep(status, o.status, true) && compareDeep(date, o.date, true)
           && compareDeep(reportingOrganization, o.reportingOrganization, true) && compareDeep(group, o.group, true)
           && compareDeep(evaluatedResources, o.evaluatedResources, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureReport))
          return false;
        MeasureReport o = (MeasureReport) other;
        return compareValues(type, o.type, true) && compareValues(status, o.status, true) && compareValues(date, o.date, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (measure == null || measure.isEmpty()) && (type == null || type.isEmpty())
           && (patient == null || patient.isEmpty()) && (period == null || period.isEmpty()) && (status == null || status.isEmpty())
           && (date == null || date.isEmpty()) && (reportingOrganization == null || reportingOrganization.isEmpty())
           && (group == null || group.isEmpty()) && (evaluatedResources == null || evaluatedResources.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MeasureReport;
   }

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for individual measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MeasureReport.patient", description="The identity of a patient to search for individual measure report results for", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for individual measure report results for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MeasureReport.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MeasureReport:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MeasureReport:patient").toLocked();


}

