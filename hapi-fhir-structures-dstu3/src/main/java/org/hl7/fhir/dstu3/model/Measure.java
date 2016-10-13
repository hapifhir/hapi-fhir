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

// Generated on Thu, Aug 25, 2016 23:04-0400 for FHIR v1.6.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.*;
/**
 * The Measure resource provides the definition of a quality measure.
 */
@ResourceDef(name="Measure", profile="http://hl7.org/fhir/Profile/Measure")
public class Measure extends DomainResource {

    public enum MeasureStatus {
        /**
         * The module is in draft state
         */
        DRAFT, 
        /**
         * The module is active
         */
        ACTIVE, 
        /**
         * The module is inactive, either rejected before publication, or retired after publication
         */
        INACTIVE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasureStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasureStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/module-metadata-status";
            case ACTIVE: return "http://hl7.org/fhir/module-metadata-status";
            case INACTIVE: return "http://hl7.org/fhir/module-metadata-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The module is in draft state";
            case ACTIVE: return "The module is active";
            case INACTIVE: return "The module is inactive, either rejected before publication, or retired after publication";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            default: return "?";
          }
        }
    }

  public static class MeasureStatusEnumFactory implements EnumFactory<MeasureStatus> {
    public MeasureStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return MeasureStatus.DRAFT;
        if ("active".equals(codeString))
          return MeasureStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return MeasureStatus.INACTIVE;
        throw new IllegalArgumentException("Unknown MeasureStatus code '"+codeString+"'");
        }
        public Enumeration<MeasureStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<MeasureStatus>(this, MeasureStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<MeasureStatus>(this, MeasureStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<MeasureStatus>(this, MeasureStatus.INACTIVE);
        throw new FHIRException("Unknown MeasureStatus code '"+codeString+"'");
        }
    public String toCode(MeasureStatus code) {
      if (code == MeasureStatus.DRAFT)
        return "draft";
      if (code == MeasureStatus.ACTIVE)
        return "active";
      if (code == MeasureStatus.INACTIVE)
        return "inactive";
      return "?";
      }
    public String toSystem(MeasureStatus code) {
      return code.getSystem();
      }
    }

    public enum MeasureScoring {
        /**
         * The measure score is defined using a proportion
         */
        PROPORTION, 
        /**
         * The measure score is defined using a ratio
         */
        RATIO, 
        /**
         * The score is defined by a calculation of some quantity
         */
        CONTINUOUSVARIABLE, 
        /**
         * The measure is a cohort definition
         */
        COHORT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasureScoring fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proportion".equals(codeString))
          return PROPORTION;
        if ("ratio".equals(codeString))
          return RATIO;
        if ("continuous-variable".equals(codeString))
          return CONTINUOUSVARIABLE;
        if ("cohort".equals(codeString))
          return COHORT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasureScoring code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPORTION: return "proportion";
            case RATIO: return "ratio";
            case CONTINUOUSVARIABLE: return "continuous-variable";
            case COHORT: return "cohort";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPORTION: return "http://hl7.org/fhir/measure-scoring";
            case RATIO: return "http://hl7.org/fhir/measure-scoring";
            case CONTINUOUSVARIABLE: return "http://hl7.org/fhir/measure-scoring";
            case COHORT: return "http://hl7.org/fhir/measure-scoring";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPORTION: return "The measure score is defined using a proportion";
            case RATIO: return "The measure score is defined using a ratio";
            case CONTINUOUSVARIABLE: return "The score is defined by a calculation of some quantity";
            case COHORT: return "The measure is a cohort definition";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPORTION: return "Proportion";
            case RATIO: return "Ratio";
            case CONTINUOUSVARIABLE: return "Continuous Variable";
            case COHORT: return "Cohort";
            default: return "?";
          }
        }
    }

  public static class MeasureScoringEnumFactory implements EnumFactory<MeasureScoring> {
    public MeasureScoring fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proportion".equals(codeString))
          return MeasureScoring.PROPORTION;
        if ("ratio".equals(codeString))
          return MeasureScoring.RATIO;
        if ("continuous-variable".equals(codeString))
          return MeasureScoring.CONTINUOUSVARIABLE;
        if ("cohort".equals(codeString))
          return MeasureScoring.COHORT;
        throw new IllegalArgumentException("Unknown MeasureScoring code '"+codeString+"'");
        }
        public Enumeration<MeasureScoring> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proportion".equals(codeString))
          return new Enumeration<MeasureScoring>(this, MeasureScoring.PROPORTION);
        if ("ratio".equals(codeString))
          return new Enumeration<MeasureScoring>(this, MeasureScoring.RATIO);
        if ("continuous-variable".equals(codeString))
          return new Enumeration<MeasureScoring>(this, MeasureScoring.CONTINUOUSVARIABLE);
        if ("cohort".equals(codeString))
          return new Enumeration<MeasureScoring>(this, MeasureScoring.COHORT);
        throw new FHIRException("Unknown MeasureScoring code '"+codeString+"'");
        }
    public String toCode(MeasureScoring code) {
      if (code == MeasureScoring.PROPORTION)
        return "proportion";
      if (code == MeasureScoring.RATIO)
        return "ratio";
      if (code == MeasureScoring.CONTINUOUSVARIABLE)
        return "continuous-variable";
      if (code == MeasureScoring.COHORT)
        return "cohort";
      return "?";
      }
    public String toSystem(MeasureScoring code) {
      return code.getSystem();
      }
    }

    public enum MeasureType {
        /**
         * The measure is a process measure
         */
        PROCESS, 
        /**
         * The measure is an outcome measure
         */
        OUTCOME, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasureType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("process".equals(codeString))
          return PROCESS;
        if ("outcome".equals(codeString))
          return OUTCOME;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasureType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROCESS: return "process";
            case OUTCOME: return "outcome";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROCESS: return "http://hl7.org/fhir/measure-type";
            case OUTCOME: return "http://hl7.org/fhir/measure-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROCESS: return "The measure is a process measure";
            case OUTCOME: return "The measure is an outcome measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROCESS: return "Process";
            case OUTCOME: return "Outcome";
            default: return "?";
          }
        }
    }

  public static class MeasureTypeEnumFactory implements EnumFactory<MeasureType> {
    public MeasureType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("process".equals(codeString))
          return MeasureType.PROCESS;
        if ("outcome".equals(codeString))
          return MeasureType.OUTCOME;
        throw new IllegalArgumentException("Unknown MeasureType code '"+codeString+"'");
        }
        public Enumeration<MeasureType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("process".equals(codeString))
          return new Enumeration<MeasureType>(this, MeasureType.PROCESS);
        if ("outcome".equals(codeString))
          return new Enumeration<MeasureType>(this, MeasureType.OUTCOME);
        throw new FHIRException("Unknown MeasureType code '"+codeString+"'");
        }
    public String toCode(MeasureType code) {
      if (code == MeasureType.PROCESS)
        return "process";
      if (code == MeasureType.OUTCOME)
        return "outcome";
      return "?";
      }
    public String toSystem(MeasureType code) {
      return code.getSystem();
      }
    }

    public enum MeasurePopulationType {
        /**
         * The initial population for the measure
         */
        INITIALPOPULATION, 
        /**
         * The numerator for the measure
         */
        NUMERATOR, 
        /**
         * The numerator exclusion for the measure
         */
        NUMERATOREXCLUSION, 
        /**
         * The denominator for the measure
         */
        DENOMINATOR, 
        /**
         * The denominator exclusion for the measure
         */
        DENOMINATOREXCLUSION, 
        /**
         * The denominator exception for the measure
         */
        DENOMINATOREXCEPTION, 
        /**
         * The measure population for the measure
         */
        MEASUREPOPULATION, 
        /**
         * The measure population exclusion for the measure
         */
        MEASUREPOPULATIONEXCLUSION, 
        /**
         * The measure score for the measure
         */
        MEASURESCORE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasurePopulationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("initial-population".equals(codeString))
          return INITIALPOPULATION;
        if ("numerator".equals(codeString))
          return NUMERATOR;
        if ("numerator-exclusion".equals(codeString))
          return NUMERATOREXCLUSION;
        if ("denominator".equals(codeString))
          return DENOMINATOR;
        if ("denominator-exclusion".equals(codeString))
          return DENOMINATOREXCLUSION;
        if ("denominator-exception".equals(codeString))
          return DENOMINATOREXCEPTION;
        if ("measure-population".equals(codeString))
          return MEASUREPOPULATION;
        if ("measure-population-exclusion".equals(codeString))
          return MEASUREPOPULATIONEXCLUSION;
        if ("measure-score".equals(codeString))
          return MEASURESCORE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasurePopulationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INITIALPOPULATION: return "initial-population";
            case NUMERATOR: return "numerator";
            case NUMERATOREXCLUSION: return "numerator-exclusion";
            case DENOMINATOR: return "denominator";
            case DENOMINATOREXCLUSION: return "denominator-exclusion";
            case DENOMINATOREXCEPTION: return "denominator-exception";
            case MEASUREPOPULATION: return "measure-population";
            case MEASUREPOPULATIONEXCLUSION: return "measure-population-exclusion";
            case MEASURESCORE: return "measure-score";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INITIALPOPULATION: return "http://hl7.org/fhir/measure-population";
            case NUMERATOR: return "http://hl7.org/fhir/measure-population";
            case NUMERATOREXCLUSION: return "http://hl7.org/fhir/measure-population";
            case DENOMINATOR: return "http://hl7.org/fhir/measure-population";
            case DENOMINATOREXCLUSION: return "http://hl7.org/fhir/measure-population";
            case DENOMINATOREXCEPTION: return "http://hl7.org/fhir/measure-population";
            case MEASUREPOPULATION: return "http://hl7.org/fhir/measure-population";
            case MEASUREPOPULATIONEXCLUSION: return "http://hl7.org/fhir/measure-population";
            case MEASURESCORE: return "http://hl7.org/fhir/measure-population";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INITIALPOPULATION: return "The initial population for the measure";
            case NUMERATOR: return "The numerator for the measure";
            case NUMERATOREXCLUSION: return "The numerator exclusion for the measure";
            case DENOMINATOR: return "The denominator for the measure";
            case DENOMINATOREXCLUSION: return "The denominator exclusion for the measure";
            case DENOMINATOREXCEPTION: return "The denominator exception for the measure";
            case MEASUREPOPULATION: return "The measure population for the measure";
            case MEASUREPOPULATIONEXCLUSION: return "The measure population exclusion for the measure";
            case MEASURESCORE: return "The measure score for the measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INITIALPOPULATION: return "Initial Population";
            case NUMERATOR: return "Numerator";
            case NUMERATOREXCLUSION: return "Numerator Exclusion";
            case DENOMINATOR: return "Denominator";
            case DENOMINATOREXCLUSION: return "Denominator Exclusion";
            case DENOMINATOREXCEPTION: return "Denominator Exception";
            case MEASUREPOPULATION: return "Measure Population";
            case MEASUREPOPULATIONEXCLUSION: return "Measure Population Exclusion";
            case MEASURESCORE: return "Measure Score";
            default: return "?";
          }
        }
    }

  public static class MeasurePopulationTypeEnumFactory implements EnumFactory<MeasurePopulationType> {
    public MeasurePopulationType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("initial-population".equals(codeString))
          return MeasurePopulationType.INITIALPOPULATION;
        if ("numerator".equals(codeString))
          return MeasurePopulationType.NUMERATOR;
        if ("numerator-exclusion".equals(codeString))
          return MeasurePopulationType.NUMERATOREXCLUSION;
        if ("denominator".equals(codeString))
          return MeasurePopulationType.DENOMINATOR;
        if ("denominator-exclusion".equals(codeString))
          return MeasurePopulationType.DENOMINATOREXCLUSION;
        if ("denominator-exception".equals(codeString))
          return MeasurePopulationType.DENOMINATOREXCEPTION;
        if ("measure-population".equals(codeString))
          return MeasurePopulationType.MEASUREPOPULATION;
        if ("measure-population-exclusion".equals(codeString))
          return MeasurePopulationType.MEASUREPOPULATIONEXCLUSION;
        if ("measure-score".equals(codeString))
          return MeasurePopulationType.MEASURESCORE;
        throw new IllegalArgumentException("Unknown MeasurePopulationType code '"+codeString+"'");
        }
        public Enumeration<MeasurePopulationType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("initial-population".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.INITIALPOPULATION);
        if ("numerator".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.NUMERATOR);
        if ("numerator-exclusion".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.NUMERATOREXCLUSION);
        if ("denominator".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.DENOMINATOR);
        if ("denominator-exclusion".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.DENOMINATOREXCLUSION);
        if ("denominator-exception".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.DENOMINATOREXCEPTION);
        if ("measure-population".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.MEASUREPOPULATION);
        if ("measure-population-exclusion".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.MEASUREPOPULATIONEXCLUSION);
        if ("measure-score".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.MEASURESCORE);
        throw new FHIRException("Unknown MeasurePopulationType code '"+codeString+"'");
        }
    public String toCode(MeasurePopulationType code) {
      if (code == MeasurePopulationType.INITIALPOPULATION)
        return "initial-population";
      if (code == MeasurePopulationType.NUMERATOR)
        return "numerator";
      if (code == MeasurePopulationType.NUMERATOREXCLUSION)
        return "numerator-exclusion";
      if (code == MeasurePopulationType.DENOMINATOR)
        return "denominator";
      if (code == MeasurePopulationType.DENOMINATOREXCLUSION)
        return "denominator-exclusion";
      if (code == MeasurePopulationType.DENOMINATOREXCEPTION)
        return "denominator-exception";
      if (code == MeasurePopulationType.MEASUREPOPULATION)
        return "measure-population";
      if (code == MeasurePopulationType.MEASUREPOPULATIONEXCLUSION)
        return "measure-population-exclusion";
      if (code == MeasurePopulationType.MEASURESCORE)
        return "measure-score";
      return "?";
      }
    public String toSystem(MeasurePopulationType code) {
      return code.getSystem();
      }
    }

    public enum MeasureDataUsage {
        /**
         * The data is intended to be provided as additional information alongside the measure results
         */
        SUPPLEMENTALDATA, 
        /**
         * The data is intended to be used to calculate and apply a risk adjustment model for the measure
         */
        RISKADJUSTMENTFACTOR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasureDataUsage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("supplemental-data".equals(codeString))
          return SUPPLEMENTALDATA;
        if ("risk-adjustment-factor".equals(codeString))
          return RISKADJUSTMENTFACTOR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasureDataUsage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUPPLEMENTALDATA: return "supplemental-data";
            case RISKADJUSTMENTFACTOR: return "risk-adjustment-factor";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SUPPLEMENTALDATA: return "http://hl7.org/fhir/measure-data-usage";
            case RISKADJUSTMENTFACTOR: return "http://hl7.org/fhir/measure-data-usage";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SUPPLEMENTALDATA: return "The data is intended to be provided as additional information alongside the measure results";
            case RISKADJUSTMENTFACTOR: return "The data is intended to be used to calculate and apply a risk adjustment model for the measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUPPLEMENTALDATA: return "Supplemental Data";
            case RISKADJUSTMENTFACTOR: return "Risk Adjustment Factor";
            default: return "?";
          }
        }
    }

  public static class MeasureDataUsageEnumFactory implements EnumFactory<MeasureDataUsage> {
    public MeasureDataUsage fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("supplemental-data".equals(codeString))
          return MeasureDataUsage.SUPPLEMENTALDATA;
        if ("risk-adjustment-factor".equals(codeString))
          return MeasureDataUsage.RISKADJUSTMENTFACTOR;
        throw new IllegalArgumentException("Unknown MeasureDataUsage code '"+codeString+"'");
        }
        public Enumeration<MeasureDataUsage> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("supplemental-data".equals(codeString))
          return new Enumeration<MeasureDataUsage>(this, MeasureDataUsage.SUPPLEMENTALDATA);
        if ("risk-adjustment-factor".equals(codeString))
          return new Enumeration<MeasureDataUsage>(this, MeasureDataUsage.RISKADJUSTMENTFACTOR);
        throw new FHIRException("Unknown MeasureDataUsage code '"+codeString+"'");
        }
    public String toCode(MeasureDataUsage code) {
      if (code == MeasureDataUsage.SUPPLEMENTALDATA)
        return "supplemental-data";
      if (code == MeasureDataUsage.RISKADJUSTMENTFACTOR)
        return "risk-adjustment-factor";
      return "?";
      }
    public String toSystem(MeasureDataUsage code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MeasureGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A unique identifier for the group. This identifier will used to report data for the group in the measure report.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique identifier", formalDefinition="A unique identifier for the group. This identifier will used to report data for the group in the measure report." )
        protected Identifier identifier;

        /**
         * Optional name or short description of this group.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short name", formalDefinition="Optional name or short description of this group." )
        protected StringType name;

        /**
         * The human readable description of this population group.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Summary description", formalDefinition="The human readable description of this population group." )
        protected StringType description;

        /**
         * A population criteria for the measure.
         */
        @Child(name = "population", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Population criteria", formalDefinition="A population criteria for the measure." )
        protected List<MeasureGroupPopulationComponent> population;

        /**
         * The stratifier criteria for the measure report, specified as either the name of a valid CQL expression defined within a referenced library, or a valid FHIR Resource Path.
         */
        @Child(name = "stratifier", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Stratifier criteria for the measure", formalDefinition="The stratifier criteria for the measure report, specified as either the name of a valid CQL expression defined within a referenced library, or a valid FHIR Resource Path." )
        protected List<MeasureGroupStratifierComponent> stratifier;

        private static final long serialVersionUID = 1287622059L;

    /**
     * Constructor
     */
      public MeasureGroupComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureGroupComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #identifier} (A unique identifier for the group. This identifier will used to report data for the group in the measure report.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (A unique identifier for the group. This identifier will used to report data for the group in the measure report.)
         */
        public MeasureGroupComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #name} (Optional name or short description of this group.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupComponent.name");
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
         * @param value {@link #name} (Optional name or short description of this group.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MeasureGroupComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Optional name or short description of this group.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Optional name or short description of this group.
         */
        public MeasureGroupComponent setName(String value) { 
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
         * @return {@link #description} (The human readable description of this population group.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupComponent.description");
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
         * @param value {@link #description} (The human readable description of this population group.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MeasureGroupComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The human readable description of this population group.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The human readable description of this population group.
         */
        public MeasureGroupComponent setDescription(String value) { 
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
         * @return {@link #population} (A population criteria for the measure.)
         */
        public List<MeasureGroupPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<MeasureGroupPopulationComponent>();
          return this.population;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureGroupComponent setPopulation(List<MeasureGroupPopulationComponent> thePopulation) { 
          this.population = thePopulation;
          return this;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MeasureGroupPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MeasureGroupPopulationComponent addPopulation() { //3
          MeasureGroupPopulationComponent t = new MeasureGroupPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MeasureGroupPopulationComponent>();
          this.population.add(t);
          return t;
        }

        public MeasureGroupComponent addPopulation(MeasureGroupPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MeasureGroupPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
         */
        public MeasureGroupPopulationComponent getPopulationFirstRep() { 
          if (getPopulation().isEmpty()) {
            addPopulation();
          }
          return getPopulation().get(0);
        }

        /**
         * @return {@link #stratifier} (The stratifier criteria for the measure report, specified as either the name of a valid CQL expression defined within a referenced library, or a valid FHIR Resource Path.)
         */
        public List<MeasureGroupStratifierComponent> getStratifier() { 
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureGroupStratifierComponent>();
          return this.stratifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureGroupComponent setStratifier(List<MeasureGroupStratifierComponent> theStratifier) { 
          this.stratifier = theStratifier;
          return this;
        }

        public boolean hasStratifier() { 
          if (this.stratifier == null)
            return false;
          for (MeasureGroupStratifierComponent item : this.stratifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MeasureGroupStratifierComponent addStratifier() { //3
          MeasureGroupStratifierComponent t = new MeasureGroupStratifierComponent();
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureGroupStratifierComponent>();
          this.stratifier.add(t);
          return t;
        }

        public MeasureGroupComponent addStratifier(MeasureGroupStratifierComponent t) { //3
          if (t == null)
            return this;
          if (this.stratifier == null)
            this.stratifier = new ArrayList<MeasureGroupStratifierComponent>();
          this.stratifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #stratifier}, creating it if it does not already exist
         */
        public MeasureGroupStratifierComponent getStratifierFirstRep() { 
          if (getStratifier().isEmpty()) {
            addStratifier();
          }
          return getStratifier().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "A unique identifier for the group. This identifier will used to report data for the group in the measure report.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("name", "string", "Optional name or short description of this group.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "The human readable description of this population group.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("population", "", "A population criteria for the measure.", 0, java.lang.Integer.MAX_VALUE, population));
          childrenList.add(new Property("stratifier", "", "The stratifier criteria for the measure report, specified as either the name of a valid CQL expression defined within a referenced library, or a valid FHIR Resource Path.", 0, java.lang.Integer.MAX_VALUE, stratifier));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MeasureGroupPopulationComponent
        case 90983669: /*stratifier*/ return this.stratifier == null ? new Base[0] : this.stratifier.toArray(new Base[this.stratifier.size()]); // MeasureGroupStratifierComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -2023558323: // population
          this.getPopulation().add((MeasureGroupPopulationComponent) value); // MeasureGroupPopulationComponent
          break;
        case 90983669: // stratifier
          this.getStratifier().add((MeasureGroupStratifierComponent) value); // MeasureGroupStratifierComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("population"))
          this.getPopulation().add((MeasureGroupPopulationComponent) value);
        else if (name.equals("stratifier"))
          this.getStratifier().add((MeasureGroupStratifierComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -2023558323:  return addPopulation(); // MeasureGroupPopulationComponent
        case 90983669:  return addStratifier(); // MeasureGroupStratifierComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.description");
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else if (name.equals("stratifier")) {
          return addStratifier();
        }
        else
          return super.addChild(name);
      }

      public MeasureGroupComponent copy() {
        MeasureGroupComponent dst = new MeasureGroupComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        if (population != null) {
          dst.population = new ArrayList<MeasureGroupPopulationComponent>();
          for (MeasureGroupPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        if (stratifier != null) {
          dst.stratifier = new ArrayList<MeasureGroupStratifierComponent>();
          for (MeasureGroupStratifierComponent i : stratifier)
            dst.stratifier.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureGroupComponent))
          return false;
        MeasureGroupComponent o = (MeasureGroupComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(population, o.population, true) && compareDeep(stratifier, o.stratifier, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureGroupComponent))
          return false;
        MeasureGroupComponent o = (MeasureGroupComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, name, description
          , population, stratifier);
      }

  public String fhirType() {
    return "Measure.group";

  }

  }

    @Block()
    public static class MeasureGroupPopulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of population criteria.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-score", formalDefinition="The type of population criteria." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-population")
        protected Enumeration<MeasurePopulationType> type;

        /**
         * A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique identifier", formalDefinition="A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report." )
        protected Identifier identifier;

        /**
         * Optional name or short description of this population.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short name", formalDefinition="Optional name or short description of this population." )
        protected StringType name;

        /**
         * The human readable description of this population criteria.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The human readable description of this population criteria", formalDefinition="The human readable description of this population criteria." )
        protected StringType description;

        /**
         * The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.
         */
        @Child(name = "criteria", type = {StringType.class}, order=5, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria", formalDefinition="The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria." )
        protected StringType criteria;

        private static final long serialVersionUID = 1158202275L;

    /**
     * Constructor
     */
      public MeasureGroupPopulationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureGroupPopulationComponent(Enumeration<MeasurePopulationType> type, Identifier identifier, StringType criteria) {
        super();
        this.type = type;
        this.identifier = identifier;
        this.criteria = criteria;
      }

        /**
         * @return {@link #type} (The type of population criteria.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<MeasurePopulationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupPopulationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<MeasurePopulationType>(new MeasurePopulationTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of population criteria.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MeasureGroupPopulationComponent setTypeElement(Enumeration<MeasurePopulationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of population criteria.
         */
        public MeasurePopulationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of population criteria.
         */
        public MeasureGroupPopulationComponent setType(MeasurePopulationType value) { 
            if (this.type == null)
              this.type = new Enumeration<MeasurePopulationType>(new MeasurePopulationTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #identifier} (A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupPopulationComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report.)
         */
        public MeasureGroupPopulationComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #name} (Optional name or short description of this population.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupPopulationComponent.name");
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
         * @param value {@link #name} (Optional name or short description of this population.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MeasureGroupPopulationComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Optional name or short description of this population.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Optional name or short description of this population.
         */
        public MeasureGroupPopulationComponent setName(String value) { 
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
         * @return {@link #description} (The human readable description of this population criteria.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupPopulationComponent.description");
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
         * @param value {@link #description} (The human readable description of this population criteria.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MeasureGroupPopulationComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The human readable description of this population criteria.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The human readable description of this population criteria.
         */
        public MeasureGroupPopulationComponent setDescription(String value) { 
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
         * @return {@link #criteria} (The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public StringType getCriteriaElement() { 
          if (this.criteria == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupPopulationComponent.criteria");
            else if (Configuration.doAutoCreate())
              this.criteria = new StringType(); // bb
          return this.criteria;
        }

        public boolean hasCriteriaElement() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        public boolean hasCriteria() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        /**
         * @param value {@link #criteria} (The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public MeasureGroupPopulationComponent setCriteriaElement(StringType value) { 
          this.criteria = value;
          return this;
        }

        /**
         * @return The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.
         */
        public String getCriteria() { 
          return this.criteria == null ? null : this.criteria.getValue();
        }

        /**
         * @param value The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.
         */
        public MeasureGroupPopulationComponent setCriteria(String value) { 
            if (this.criteria == null)
              this.criteria = new StringType();
            this.criteria.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of population criteria.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("identifier", "Identifier", "A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("name", "string", "Optional name or short description of this population.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "The human readable description of this population criteria.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("criteria", "string", "The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.", 0, java.lang.Integer.MAX_VALUE, criteria));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<MeasurePopulationType>
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1952046943: /*criteria*/ return this.criteria == null ? new Base[0] : new Base[] {this.criteria}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new MeasurePopulationTypeEnumFactory().fromType(value); // Enumeration<MeasurePopulationType>
          break;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new MeasurePopulationTypeEnumFactory().fromType(value); // Enumeration<MeasurePopulationType>
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("criteria"))
          this.criteria = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<MeasurePopulationType>
        case -1618432855:  return getIdentifier(); // Identifier
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 1952046943: throw new FHIRException("Cannot make property criteria as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.type");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.description");
        }
        else if (name.equals("criteria")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.criteria");
        }
        else
          return super.addChild(name);
      }

      public MeasureGroupPopulationComponent copy() {
        MeasureGroupPopulationComponent dst = new MeasureGroupPopulationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.criteria = criteria == null ? null : criteria.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureGroupPopulationComponent))
          return false;
        MeasureGroupPopulationComponent o = (MeasureGroupPopulationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true)
           && compareDeep(description, o.description, true) && compareDeep(criteria, o.criteria, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureGroupPopulationComponent))
          return false;
        MeasureGroupPopulationComponent o = (MeasureGroupPopulationComponent) other;
        return compareValues(type, o.type, true) && compareValues(name, o.name, true) && compareValues(description, o.description, true)
           && compareValues(criteria, o.criteria, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, identifier, name, description
          , criteria);
      }

  public String fhirType() {
    return "Measure.group.population";

  }

  }

    @Block()
    public static class MeasureGroupStratifierComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier for the stratifier used to coordinate the reported data back to this stratifier.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The identifier for the stratifier used to coordinate the reported data back to this stratifier", formalDefinition="The identifier for the stratifier used to coordinate the reported data back to this stratifier." )
        protected Identifier identifier;

        /**
         * The criteria for the stratifier. This must be the name of an expression defined within a referenced library.
         */
        @Child(name = "criteria", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Stratifier criteria", formalDefinition="The criteria for the stratifier. This must be the name of an expression defined within a referenced library." )
        protected StringType criteria;

        /**
         * The path to an element that defines the stratifier, specified as a valid FHIR resource path.
         */
        @Child(name = "path", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Path to the stratifier", formalDefinition="The path to an element that defines the stratifier, specified as a valid FHIR resource path." )
        protected StringType path;

        private static final long serialVersionUID = -196134448L;

    /**
     * Constructor
     */
      public MeasureGroupStratifierComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureGroupStratifierComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #identifier} (The identifier for the stratifier used to coordinate the reported data back to this stratifier.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupStratifierComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier for the stratifier used to coordinate the reported data back to this stratifier.)
         */
        public MeasureGroupStratifierComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #criteria} (The criteria for the stratifier. This must be the name of an expression defined within a referenced library.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public StringType getCriteriaElement() { 
          if (this.criteria == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupStratifierComponent.criteria");
            else if (Configuration.doAutoCreate())
              this.criteria = new StringType(); // bb
          return this.criteria;
        }

        public boolean hasCriteriaElement() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        public boolean hasCriteria() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        /**
         * @param value {@link #criteria} (The criteria for the stratifier. This must be the name of an expression defined within a referenced library.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public MeasureGroupStratifierComponent setCriteriaElement(StringType value) { 
          this.criteria = value;
          return this;
        }

        /**
         * @return The criteria for the stratifier. This must be the name of an expression defined within a referenced library.
         */
        public String getCriteria() { 
          return this.criteria == null ? null : this.criteria.getValue();
        }

        /**
         * @param value The criteria for the stratifier. This must be the name of an expression defined within a referenced library.
         */
        public MeasureGroupStratifierComponent setCriteria(String value) { 
          if (Utilities.noString(value))
            this.criteria = null;
          else {
            if (this.criteria == null)
              this.criteria = new StringType();
            this.criteria.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #path} (The path to an element that defines the stratifier, specified as a valid FHIR resource path.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupStratifierComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The path to an element that defines the stratifier, specified as a valid FHIR resource path.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public MeasureGroupStratifierComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to an element that defines the stratifier, specified as a valid FHIR resource path.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to an element that defines the stratifier, specified as a valid FHIR resource path.
         */
        public MeasureGroupStratifierComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "The identifier for the stratifier used to coordinate the reported data back to this stratifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("criteria", "string", "The criteria for the stratifier. This must be the name of an expression defined within a referenced library.", 0, java.lang.Integer.MAX_VALUE, criteria));
          childrenList.add(new Property("path", "string", "The path to an element that defines the stratifier, specified as a valid FHIR resource path.", 0, java.lang.Integer.MAX_VALUE, path));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 1952046943: /*criteria*/ return this.criteria == null ? new Base[0] : new Base[] {this.criteria}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          break;
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("criteria"))
          this.criteria = castToString(value); // StringType
        else if (name.equals("path"))
          this.path = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case 1952046943: throw new FHIRException("Cannot make property criteria as it is not a complex type"); // StringType
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("criteria")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.criteria");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.path");
        }
        else
          return super.addChild(name);
      }

      public MeasureGroupStratifierComponent copy() {
        MeasureGroupStratifierComponent dst = new MeasureGroupStratifierComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.criteria = criteria == null ? null : criteria.copy();
        dst.path = path == null ? null : path.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureGroupStratifierComponent))
          return false;
        MeasureGroupStratifierComponent o = (MeasureGroupStratifierComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(criteria, o.criteria, true) && compareDeep(path, o.path, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureGroupStratifierComponent))
          return false;
        MeasureGroupStratifierComponent o = (MeasureGroupStratifierComponent) other;
        return compareValues(criteria, o.criteria, true) && compareValues(path, o.path, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, criteria, path
          );
      }

  public String fhirType() {
    return "Measure.group.stratifier";

  }

  }

    @Block()
    public static class MeasureSupplementalDataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier for the supplemental data.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier, unique within the measure", formalDefinition="An identifier for the supplemental data." )
        protected Identifier identifier;

        /**
         * An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.
         */
        @Child(name = "usage", type = {CodeType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="supplemental-data | risk-adjustment-factor", formalDefinition="An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-data-usage")
        protected List<Enumeration<MeasureDataUsage>> usage;

        /**
         * The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.
         */
        @Child(name = "criteria", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Supplemental data criteria", formalDefinition="The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element." )
        protected StringType criteria;

        /**
         * The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.
         */
        @Child(name = "path", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Path to the supplemental data element", formalDefinition="The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path." )
        protected StringType path;

        private static final long serialVersionUID = 1666728717L;

    /**
     * Constructor
     */
      public MeasureSupplementalDataComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureSupplementalDataComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #identifier} (An identifier for the supplemental data.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureSupplementalDataComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (An identifier for the supplemental data.)
         */
        public MeasureSupplementalDataComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #usage} (An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.)
         */
        public List<Enumeration<MeasureDataUsage>> getUsage() { 
          if (this.usage == null)
            this.usage = new ArrayList<Enumeration<MeasureDataUsage>>();
          return this.usage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureSupplementalDataComponent setUsage(List<Enumeration<MeasureDataUsage>> theUsage) { 
          this.usage = theUsage;
          return this;
        }

        public boolean hasUsage() { 
          if (this.usage == null)
            return false;
          for (Enumeration<MeasureDataUsage> item : this.usage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #usage} (An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.)
         */
        public Enumeration<MeasureDataUsage> addUsageElement() {//2 
          Enumeration<MeasureDataUsage> t = new Enumeration<MeasureDataUsage>(new MeasureDataUsageEnumFactory());
          if (this.usage == null)
            this.usage = new ArrayList<Enumeration<MeasureDataUsage>>();
          this.usage.add(t);
          return t;
        }

        /**
         * @param value {@link #usage} (An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.)
         */
        public MeasureSupplementalDataComponent addUsage(MeasureDataUsage value) { //1
          Enumeration<MeasureDataUsage> t = new Enumeration<MeasureDataUsage>(new MeasureDataUsageEnumFactory());
          t.setValue(value);
          if (this.usage == null)
            this.usage = new ArrayList<Enumeration<MeasureDataUsage>>();
          this.usage.add(t);
          return this;
        }

        /**
         * @param value {@link #usage} (An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.)
         */
        public boolean hasUsage(MeasureDataUsage value) { 
          if (this.usage == null)
            return false;
          for (Enumeration<MeasureDataUsage> v : this.usage)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #criteria} (The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public StringType getCriteriaElement() { 
          if (this.criteria == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureSupplementalDataComponent.criteria");
            else if (Configuration.doAutoCreate())
              this.criteria = new StringType(); // bb
          return this.criteria;
        }

        public boolean hasCriteriaElement() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        public boolean hasCriteria() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        /**
         * @param value {@link #criteria} (The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public MeasureSupplementalDataComponent setCriteriaElement(StringType value) { 
          this.criteria = value;
          return this;
        }

        /**
         * @return The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.
         */
        public String getCriteria() { 
          return this.criteria == null ? null : this.criteria.getValue();
        }

        /**
         * @param value The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.
         */
        public MeasureSupplementalDataComponent setCriteria(String value) { 
          if (Utilities.noString(value))
            this.criteria = null;
          else {
            if (this.criteria == null)
              this.criteria = new StringType();
            this.criteria.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #path} (The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureSupplementalDataComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public MeasureSupplementalDataComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.
         */
        public MeasureSupplementalDataComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "An identifier for the supplemental data.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("usage", "code", "An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.", 0, java.lang.Integer.MAX_VALUE, usage));
          childrenList.add(new Property("criteria", "string", "The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.", 0, java.lang.Integer.MAX_VALUE, criteria));
          childrenList.add(new Property("path", "string", "The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.", 0, java.lang.Integer.MAX_VALUE, path));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : this.usage.toArray(new Base[this.usage.size()]); // Enumeration<MeasureDataUsage>
        case 1952046943: /*criteria*/ return this.criteria == null ? new Base[0] : new Base[] {this.criteria}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 111574433: // usage
          this.getUsage().add(new MeasureDataUsageEnumFactory().fromType(value)); // Enumeration<MeasureDataUsage>
          break;
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          break;
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("usage"))
          this.getUsage().add(new MeasureDataUsageEnumFactory().fromType(value));
        else if (name.equals("criteria"))
          this.criteria = castToString(value); // StringType
        else if (name.equals("path"))
          this.path = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case 111574433: throw new FHIRException("Cannot make property usage as it is not a complex type"); // Enumeration<MeasureDataUsage>
        case 1952046943: throw new FHIRException("Cannot make property criteria as it is not a complex type"); // StringType
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.usage");
        }
        else if (name.equals("criteria")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.criteria");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.path");
        }
        else
          return super.addChild(name);
      }

      public MeasureSupplementalDataComponent copy() {
        MeasureSupplementalDataComponent dst = new MeasureSupplementalDataComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (usage != null) {
          dst.usage = new ArrayList<Enumeration<MeasureDataUsage>>();
          for (Enumeration<MeasureDataUsage> i : usage)
            dst.usage.add(i.copy());
        };
        dst.criteria = criteria == null ? null : criteria.copy();
        dst.path = path == null ? null : path.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasureSupplementalDataComponent))
          return false;
        MeasureSupplementalDataComponent o = (MeasureSupplementalDataComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(usage, o.usage, true) && compareDeep(criteria, o.criteria, true)
           && compareDeep(path, o.path, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureSupplementalDataComponent))
          return false;
        MeasureSupplementalDataComponent o = (MeasureSupplementalDataComponent) other;
        return compareValues(usage, o.usage, true) && compareValues(criteria, o.criteria, true) && compareValues(path, o.path, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, usage, criteria
          , path);
      }

  public String fhirType() {
    return "Measure.supplementalData";

  }

  }

    /**
     * An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical URL to reference this measure", formalDefinition="An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published." )
    protected UriType url;

    /**
     * A logical identifier for the measure such as the CMS or NQF identifier. Note that at least one identifier is required for non-experimental active artifacts.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier(s) for the measure", formalDefinition="A logical identifier for the measure such as the CMS or NQF identifier. Note that at least one identifier is required for non-experimental active artifacts." )
    protected List<Identifier> identifier;

    /**
     * The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the measure, if any", formalDefinition="The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts." )
    protected StringType version;

    /**
     * A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A machine-friendly name for the measure", formalDefinition="A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation." )
    protected StringType name;

    /**
     * A short, descriptive, user-friendly title for the measure.
     */
    @Child(name = "title", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A user-friendly title for the measure", formalDefinition="A short, descriptive, user-friendly title for the measure." )
    protected StringType title;

    /**
     * The status of the measure.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | inactive", formalDefinition="The status of the measure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/module-metadata-status")
    protected Enumeration<MeasureStatus> status;

    /**
     * Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments." )
    protected BooleanType experimental;

    /**
     * A free text natural language description of the measure from the consumer's perspective.
     */
    @Child(name = "description", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Natural language description of the measure", formalDefinition="A free text natural language description of the measure from the consumer's perspective." )
    protected StringType description;

    /**
     * A brief description of the purpose of the measure.
     */
    @Child(name = "purpose", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the purpose of the measure", formalDefinition="A brief description of the purpose of the measure." )
    protected StringType purpose;

    /**
     * A detailed description of how the measure is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the measure", formalDefinition="A detailed description of how the measure is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the measure was published.
     */
    @Child(name = "publicationDate", type = {DateType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Publication date for this version of the measure", formalDefinition="The date on which the measure was published." )
    protected DateType publicationDate;

    /**
     * The date on which the measure content was last reviewed.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Last review date for the measure", formalDefinition="The date on which the measure content was last reviewed." )
    protected DateType lastReviewDate;

    /**
     * The period during which the measure content is effective.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The effective date range for the measure", formalDefinition="The period during which the measure content is effective." )
    protected Period effectivePeriod;

    /**
     * Specifies various attributes of the patient population for whom and/or environment of care in which, the measure is applicable.
     */
    @Child(name = "coverage", type = {UsageContext.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Describes the context of use for this measure", formalDefinition="Specifies various attributes of the patient population for whom and/or environment of care in which, the measure is applicable." )
    protected List<UsageContext> coverage;

    /**
     * Clinical topics related to the content of the measure.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Descriptional topics for the measure", formalDefinition="Clinical topics related to the content of the measure." )
    protected List<CodeableConcept> topic;

    /**
     * A contributor to the content of the measure, including authors, editors, reviewers, and endorsers.
     */
    @Child(name = "contributor", type = {Contributor.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A content contributor", formalDefinition="A contributor to the content of the measure, including authors, editors, reviewers, and endorsers." )
    protected List<Contributor> contributor;

    /**
     * The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts.
     */
    @Child(name = "publisher", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {ContactDetail.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactDetail> contact;

    /**
     * A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.
     */
    @Child(name = "copyright", type = {StringType.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure." )
    protected StringType copyright;

    /**
     * Related resources such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedResource", type = {RelatedResource.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Related resources for the measure", formalDefinition="Related resources such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedResource> relatedResource;

    /**
     * A reference to a Library resource containing the formal logic used by the measure.
     */
    @Child(name = "library", type = {Library.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the measure", formalDefinition="A reference to a Library resource containing the formal logic used by the measure." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing the formal logic used by the measure.)
     */
    protected List<Library> libraryTarget;


    /**
     * A disclaimer for the use of the measure.
     */
    @Child(name = "disclaimer", type = {MarkdownType.class}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disclaimer for the measure", formalDefinition="A disclaimer for the use of the measure." )
    protected MarkdownType disclaimer;

    /**
     * The measure scoring type, e.g. proportion, CV.
     */
    @Child(name = "scoring", type = {CodeType.class}, order=22, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="proportion | ratio | continuous-variable | cohort", formalDefinition="The measure scoring type, e.g. proportion, CV." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-scoring")
    protected Enumeration<MeasureScoring> scoring;

    /**
     * The measure type, e.g. process, outcome.
     */
    @Child(name = "type", type = {CodeType.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="process | outcome", formalDefinition="The measure type, e.g. process, outcome." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-type")
    protected List<Enumeration<MeasureType>> type;

    /**
     * A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.
     */
    @Child(name = "riskAdjustment", type = {StringType.class}, order=24, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How is risk adjustment applied for this measure", formalDefinition="A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results." )
    protected StringType riskAdjustment;

    /**
     * A description of the rate aggregation for the measure.
     */
    @Child(name = "rateAggregation", type = {StringType.class}, order=25, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How is rate aggregation performed for this measure", formalDefinition="A description of the rate aggregation for the measure." )
    protected StringType rateAggregation;

    /**
     * The rationale for the measure.
     */
    @Child(name = "rationale", type = {MarkdownType.class}, order=26, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why does this measure exist", formalDefinition="The rationale for the measure." )
    protected MarkdownType rationale;

    /**
     * The clinical recommendation statement for the measure.
     */
    @Child(name = "clinicalRecommendationStatement", type = {MarkdownType.class}, order=27, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Clinical recommendation", formalDefinition="The clinical recommendation statement for the measure." )
    protected MarkdownType clinicalRecommendationStatement;

    /**
     * Improvement notation for the measure, e.g. higher score indicates better quality.
     */
    @Child(name = "improvementNotation", type = {StringType.class}, order=28, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Improvement notation for the measure, e.g. higher score indicates better quality", formalDefinition="Improvement notation for the measure, e.g. higher score indicates better quality." )
    protected StringType improvementNotation;

    /**
     * A narrative description of the complete measure calculation.
     */
    @Child(name = "definition", type = {MarkdownType.class}, order=29, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A natural language definition of the measure", formalDefinition="A narrative description of the complete measure calculation." )
    protected MarkdownType definition;

    /**
     * Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.
     */
    @Child(name = "guidance", type = {MarkdownType.class}, order=30, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The guidance for the measure", formalDefinition="Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure." )
    protected MarkdownType guidance;

    /**
     * The measure set, e.g. Preventive Care and Screening.
     */
    @Child(name = "set", type = {StringType.class}, order=31, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The measure set, e.g. Preventive Care and Screening", formalDefinition="The measure set, e.g. Preventive Care and Screening." )
    protected StringType set;

    /**
     * A group of population criteria for the measure.
     */
    @Child(name = "group", type = {}, order=32, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Population criteria group", formalDefinition="A group of population criteria for the measure." )
    protected List<MeasureGroupComponent> group;

    /**
     * The supplemental data criteria for the measure report, specified as either the name of a valid CQL expression within a referenced library, or a valid FHIR Resource Path.
     */
    @Child(name = "supplementalData", type = {}, order=33, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supplemental data", formalDefinition="The supplemental data criteria for the measure report, specified as either the name of a valid CQL expression within a referenced library, or a valid FHIR Resource Path." )
    protected List<MeasureSupplementalDataComponent> supplementalData;

    private static final long serialVersionUID = 100136119L;

  /**
   * Constructor
   */
    public Measure() {
      super();
    }

  /**
   * Constructor
   */
    public Measure(Enumeration<MeasureStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Measure setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published.
     */
    public Measure setUrl(String value) { 
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
     * @return {@link #identifier} (A logical identifier for the measure such as the CMS or NQF identifier. Note that at least one identifier is required for non-experimental active artifacts.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setIdentifier(List<Identifier> theIdentifier) { 
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

    public Measure addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.version");
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
     * @param value {@link #version} (The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Measure setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public Measure setVersion(String value) { 
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
     * @return {@link #name} (A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.name");
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
     * @param value {@link #name} (A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Measure setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation.
     */
    public Measure setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the measure.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the measure.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Measure setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the measure.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the measure.
     */
    public Measure setTitle(String value) { 
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
     * @return {@link #status} (The status of the measure.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MeasureStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MeasureStatus>(new MeasureStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the measure.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Measure setStatusElement(Enumeration<MeasureStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the measure.
     */
    public MeasureStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the measure.
     */
    public Measure setStatus(MeasureStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MeasureStatus>(new MeasureStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.experimental");
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
     * @param value {@link #experimental} (Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public Measure setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    public Measure setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the measure from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.description");
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
     * @param value {@link #description} (A free text natural language description of the measure from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Measure setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the measure from the consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the measure from the consumer's perspective.
     */
    public Measure setDescription(String value) { 
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
     * @return {@link #purpose} (A brief description of the purpose of the measure.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public StringType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new StringType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (A brief description of the purpose of the measure.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public Measure setPurposeElement(StringType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return A brief description of the purpose of the measure.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value A brief description of the purpose of the measure.
     */
    public Measure setPurpose(String value) { 
      if (Utilities.noString(value))
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new StringType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (A detailed description of how the measure is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.usage");
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
     * @param value {@link #usage} (A detailed description of how the measure is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public Measure setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return A detailed description of how the measure is used from a clinical perspective.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value A detailed description of how the measure is used from a clinical perspective.
     */
    public Measure setUsage(String value) { 
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
     * @return {@link #publicationDate} (The date on which the measure was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public DateType getPublicationDateElement() { 
      if (this.publicationDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.publicationDate");
        else if (Configuration.doAutoCreate())
          this.publicationDate = new DateType(); // bb
      return this.publicationDate;
    }

    public boolean hasPublicationDateElement() { 
      return this.publicationDate != null && !this.publicationDate.isEmpty();
    }

    public boolean hasPublicationDate() { 
      return this.publicationDate != null && !this.publicationDate.isEmpty();
    }

    /**
     * @param value {@link #publicationDate} (The date on which the measure was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public Measure setPublicationDateElement(DateType value) { 
      this.publicationDate = value;
      return this;
    }

    /**
     * @return The date on which the measure was published.
     */
    public Date getPublicationDate() { 
      return this.publicationDate == null ? null : this.publicationDate.getValue();
    }

    /**
     * @param value The date on which the measure was published.
     */
    public Measure setPublicationDate(Date value) { 
      if (value == null)
        this.publicationDate = null;
      else {
        if (this.publicationDate == null)
          this.publicationDate = new DateType();
        this.publicationDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the measure content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.lastReviewDate");
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
     * @param value {@link #lastReviewDate} (The date on which the measure content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public Measure setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the measure content was last reviewed.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the measure content was last reviewed.
     */
    public Measure setLastReviewDate(Date value) { 
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
     * @return {@link #effectivePeriod} (The period during which the measure content is effective.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the measure content is effective.)
     */
    public Measure setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Specifies various attributes of the patient population for whom and/or environment of care in which, the measure is applicable.)
     */
    public List<UsageContext> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      return this.coverage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setCoverage(List<UsageContext> theCoverage) { 
      this.coverage = theCoverage;
      return this;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (UsageContext item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addCoverage() { //3
      UsageContext t = new UsageContext();
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      this.coverage.add(t);
      return t;
    }

    public Measure addCoverage(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      this.coverage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #coverage}, creating it if it does not already exist
     */
    public UsageContext getCoverageFirstRep() { 
      if (getCoverage().isEmpty()) {
        addCoverage();
      }
      return getCoverage().get(0);
    }

    /**
     * @return {@link #topic} (Clinical topics related to the content of the measure.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setTopic(List<CodeableConcept> theTopic) { 
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

    public Measure addTopic(CodeableConcept t) { //3
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
     * @return {@link #contributor} (A contributor to the content of the measure, including authors, editors, reviewers, and endorsers.)
     */
    public List<Contributor> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      return this.contributor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setContributor(List<Contributor> theContributor) { 
      this.contributor = theContributor;
      return this;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (Contributor item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Contributor addContributor() { //3
      Contributor t = new Contributor();
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return t;
    }

    public Measure addContributor(Contributor t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contributor}, creating it if it does not already exist
     */
    public Contributor getContributorFirstRep() { 
      if (getContributor().isEmpty()) {
        addContributor();
      }
      return getContributor().get(0);
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public Measure setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts.
     */
    public Measure setPublisher(String value) { 
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
    public Measure setContact(List<ContactDetail> theContact) { 
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

    public Measure addContact(ContactDetail t) { //3
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
     * @return {@link #copyright} (A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public Measure setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.
     */
    public Measure setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedResource} (Related resources such as additional documentation, justification, or bibliographic references.)
     */
    public List<RelatedResource> getRelatedResource() { 
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      return this.relatedResource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setRelatedResource(List<RelatedResource> theRelatedResource) { 
      this.relatedResource = theRelatedResource;
      return this;
    }

    public boolean hasRelatedResource() { 
      if (this.relatedResource == null)
        return false;
      for (RelatedResource item : this.relatedResource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedResource addRelatedResource() { //3
      RelatedResource t = new RelatedResource();
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      this.relatedResource.add(t);
      return t;
    }

    public Measure addRelatedResource(RelatedResource t) { //3
      if (t == null)
        return this;
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      this.relatedResource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedResource}, creating it if it does not already exist
     */
    public RelatedResource getRelatedResourceFirstRep() { 
      if (getRelatedResource().isEmpty()) {
        addRelatedResource();
      }
      return getRelatedResource().get(0);
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing the formal logic used by the measure.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setLibrary(List<Reference> theLibrary) { 
      this.library = theLibrary;
      return this;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    public Measure addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #library}, creating it if it does not already exist
     */
    public Reference getLibraryFirstRep() { 
      if (getLibrary().isEmpty()) {
        addLibrary();
      }
      return getLibrary().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #disclaimer} (A disclaimer for the use of the measure.). This is the underlying object with id, value and extensions. The accessor "getDisclaimer" gives direct access to the value
     */
    public MarkdownType getDisclaimerElement() { 
      if (this.disclaimer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.disclaimer");
        else if (Configuration.doAutoCreate())
          this.disclaimer = new MarkdownType(); // bb
      return this.disclaimer;
    }

    public boolean hasDisclaimerElement() { 
      return this.disclaimer != null && !this.disclaimer.isEmpty();
    }

    public boolean hasDisclaimer() { 
      return this.disclaimer != null && !this.disclaimer.isEmpty();
    }

    /**
     * @param value {@link #disclaimer} (A disclaimer for the use of the measure.). This is the underlying object with id, value and extensions. The accessor "getDisclaimer" gives direct access to the value
     */
    public Measure setDisclaimerElement(MarkdownType value) { 
      this.disclaimer = value;
      return this;
    }

    /**
     * @return A disclaimer for the use of the measure.
     */
    public String getDisclaimer() { 
      return this.disclaimer == null ? null : this.disclaimer.getValue();
    }

    /**
     * @param value A disclaimer for the use of the measure.
     */
    public Measure setDisclaimer(String value) { 
      if (value == null)
        this.disclaimer = null;
      else {
        if (this.disclaimer == null)
          this.disclaimer = new MarkdownType();
        this.disclaimer.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #scoring} (The measure scoring type, e.g. proportion, CV.). This is the underlying object with id, value and extensions. The accessor "getScoring" gives direct access to the value
     */
    public Enumeration<MeasureScoring> getScoringElement() { 
      if (this.scoring == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.scoring");
        else if (Configuration.doAutoCreate())
          this.scoring = new Enumeration<MeasureScoring>(new MeasureScoringEnumFactory()); // bb
      return this.scoring;
    }

    public boolean hasScoringElement() { 
      return this.scoring != null && !this.scoring.isEmpty();
    }

    public boolean hasScoring() { 
      return this.scoring != null && !this.scoring.isEmpty();
    }

    /**
     * @param value {@link #scoring} (The measure scoring type, e.g. proportion, CV.). This is the underlying object with id, value and extensions. The accessor "getScoring" gives direct access to the value
     */
    public Measure setScoringElement(Enumeration<MeasureScoring> value) { 
      this.scoring = value;
      return this;
    }

    /**
     * @return The measure scoring type, e.g. proportion, CV.
     */
    public MeasureScoring getScoring() { 
      return this.scoring == null ? null : this.scoring.getValue();
    }

    /**
     * @param value The measure scoring type, e.g. proportion, CV.
     */
    public Measure setScoring(MeasureScoring value) { 
      if (value == null)
        this.scoring = null;
      else {
        if (this.scoring == null)
          this.scoring = new Enumeration<MeasureScoring>(new MeasureScoringEnumFactory());
        this.scoring.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The measure type, e.g. process, outcome.)
     */
    public List<Enumeration<MeasureType>> getType() { 
      if (this.type == null)
        this.type = new ArrayList<Enumeration<MeasureType>>();
      return this.type;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setType(List<Enumeration<MeasureType>> theType) { 
      this.type = theType;
      return this;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (Enumeration<MeasureType> item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (The measure type, e.g. process, outcome.)
     */
    public Enumeration<MeasureType> addTypeElement() {//2 
      Enumeration<MeasureType> t = new Enumeration<MeasureType>(new MeasureTypeEnumFactory());
      if (this.type == null)
        this.type = new ArrayList<Enumeration<MeasureType>>();
      this.type.add(t);
      return t;
    }

    /**
     * @param value {@link #type} (The measure type, e.g. process, outcome.)
     */
    public Measure addType(MeasureType value) { //1
      Enumeration<MeasureType> t = new Enumeration<MeasureType>(new MeasureTypeEnumFactory());
      t.setValue(value);
      if (this.type == null)
        this.type = new ArrayList<Enumeration<MeasureType>>();
      this.type.add(t);
      return this;
    }

    /**
     * @param value {@link #type} (The measure type, e.g. process, outcome.)
     */
    public boolean hasType(MeasureType value) { 
      if (this.type == null)
        return false;
      for (Enumeration<MeasureType> v : this.type)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #riskAdjustment} (A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.). This is the underlying object with id, value and extensions. The accessor "getRiskAdjustment" gives direct access to the value
     */
    public StringType getRiskAdjustmentElement() { 
      if (this.riskAdjustment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.riskAdjustment");
        else if (Configuration.doAutoCreate())
          this.riskAdjustment = new StringType(); // bb
      return this.riskAdjustment;
    }

    public boolean hasRiskAdjustmentElement() { 
      return this.riskAdjustment != null && !this.riskAdjustment.isEmpty();
    }

    public boolean hasRiskAdjustment() { 
      return this.riskAdjustment != null && !this.riskAdjustment.isEmpty();
    }

    /**
     * @param value {@link #riskAdjustment} (A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.). This is the underlying object with id, value and extensions. The accessor "getRiskAdjustment" gives direct access to the value
     */
    public Measure setRiskAdjustmentElement(StringType value) { 
      this.riskAdjustment = value;
      return this;
    }

    /**
     * @return A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.
     */
    public String getRiskAdjustment() { 
      return this.riskAdjustment == null ? null : this.riskAdjustment.getValue();
    }

    /**
     * @param value A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.
     */
    public Measure setRiskAdjustment(String value) { 
      if (Utilities.noString(value))
        this.riskAdjustment = null;
      else {
        if (this.riskAdjustment == null)
          this.riskAdjustment = new StringType();
        this.riskAdjustment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #rateAggregation} (A description of the rate aggregation for the measure.). This is the underlying object with id, value and extensions. The accessor "getRateAggregation" gives direct access to the value
     */
    public StringType getRateAggregationElement() { 
      if (this.rateAggregation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.rateAggregation");
        else if (Configuration.doAutoCreate())
          this.rateAggregation = new StringType(); // bb
      return this.rateAggregation;
    }

    public boolean hasRateAggregationElement() { 
      return this.rateAggregation != null && !this.rateAggregation.isEmpty();
    }

    public boolean hasRateAggregation() { 
      return this.rateAggregation != null && !this.rateAggregation.isEmpty();
    }

    /**
     * @param value {@link #rateAggregation} (A description of the rate aggregation for the measure.). This is the underlying object with id, value and extensions. The accessor "getRateAggregation" gives direct access to the value
     */
    public Measure setRateAggregationElement(StringType value) { 
      this.rateAggregation = value;
      return this;
    }

    /**
     * @return A description of the rate aggregation for the measure.
     */
    public String getRateAggregation() { 
      return this.rateAggregation == null ? null : this.rateAggregation.getValue();
    }

    /**
     * @param value A description of the rate aggregation for the measure.
     */
    public Measure setRateAggregation(String value) { 
      if (Utilities.noString(value))
        this.rateAggregation = null;
      else {
        if (this.rateAggregation == null)
          this.rateAggregation = new StringType();
        this.rateAggregation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #rationale} (The rationale for the measure.). This is the underlying object with id, value and extensions. The accessor "getRationale" gives direct access to the value
     */
    public MarkdownType getRationaleElement() { 
      if (this.rationale == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.rationale");
        else if (Configuration.doAutoCreate())
          this.rationale = new MarkdownType(); // bb
      return this.rationale;
    }

    public boolean hasRationaleElement() { 
      return this.rationale != null && !this.rationale.isEmpty();
    }

    public boolean hasRationale() { 
      return this.rationale != null && !this.rationale.isEmpty();
    }

    /**
     * @param value {@link #rationale} (The rationale for the measure.). This is the underlying object with id, value and extensions. The accessor "getRationale" gives direct access to the value
     */
    public Measure setRationaleElement(MarkdownType value) { 
      this.rationale = value;
      return this;
    }

    /**
     * @return The rationale for the measure.
     */
    public String getRationale() { 
      return this.rationale == null ? null : this.rationale.getValue();
    }

    /**
     * @param value The rationale for the measure.
     */
    public Measure setRationale(String value) { 
      if (value == null)
        this.rationale = null;
      else {
        if (this.rationale == null)
          this.rationale = new MarkdownType();
        this.rationale.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #clinicalRecommendationStatement} (The clinical recommendation statement for the measure.). This is the underlying object with id, value and extensions. The accessor "getClinicalRecommendationStatement" gives direct access to the value
     */
    public MarkdownType getClinicalRecommendationStatementElement() { 
      if (this.clinicalRecommendationStatement == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.clinicalRecommendationStatement");
        else if (Configuration.doAutoCreate())
          this.clinicalRecommendationStatement = new MarkdownType(); // bb
      return this.clinicalRecommendationStatement;
    }

    public boolean hasClinicalRecommendationStatementElement() { 
      return this.clinicalRecommendationStatement != null && !this.clinicalRecommendationStatement.isEmpty();
    }

    public boolean hasClinicalRecommendationStatement() { 
      return this.clinicalRecommendationStatement != null && !this.clinicalRecommendationStatement.isEmpty();
    }

    /**
     * @param value {@link #clinicalRecommendationStatement} (The clinical recommendation statement for the measure.). This is the underlying object with id, value and extensions. The accessor "getClinicalRecommendationStatement" gives direct access to the value
     */
    public Measure setClinicalRecommendationStatementElement(MarkdownType value) { 
      this.clinicalRecommendationStatement = value;
      return this;
    }

    /**
     * @return The clinical recommendation statement for the measure.
     */
    public String getClinicalRecommendationStatement() { 
      return this.clinicalRecommendationStatement == null ? null : this.clinicalRecommendationStatement.getValue();
    }

    /**
     * @param value The clinical recommendation statement for the measure.
     */
    public Measure setClinicalRecommendationStatement(String value) { 
      if (value == null)
        this.clinicalRecommendationStatement = null;
      else {
        if (this.clinicalRecommendationStatement == null)
          this.clinicalRecommendationStatement = new MarkdownType();
        this.clinicalRecommendationStatement.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #improvementNotation} (Improvement notation for the measure, e.g. higher score indicates better quality.). This is the underlying object with id, value and extensions. The accessor "getImprovementNotation" gives direct access to the value
     */
    public StringType getImprovementNotationElement() { 
      if (this.improvementNotation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.improvementNotation");
        else if (Configuration.doAutoCreate())
          this.improvementNotation = new StringType(); // bb
      return this.improvementNotation;
    }

    public boolean hasImprovementNotationElement() { 
      return this.improvementNotation != null && !this.improvementNotation.isEmpty();
    }

    public boolean hasImprovementNotation() { 
      return this.improvementNotation != null && !this.improvementNotation.isEmpty();
    }

    /**
     * @param value {@link #improvementNotation} (Improvement notation for the measure, e.g. higher score indicates better quality.). This is the underlying object with id, value and extensions. The accessor "getImprovementNotation" gives direct access to the value
     */
    public Measure setImprovementNotationElement(StringType value) { 
      this.improvementNotation = value;
      return this;
    }

    /**
     * @return Improvement notation for the measure, e.g. higher score indicates better quality.
     */
    public String getImprovementNotation() { 
      return this.improvementNotation == null ? null : this.improvementNotation.getValue();
    }

    /**
     * @param value Improvement notation for the measure, e.g. higher score indicates better quality.
     */
    public Measure setImprovementNotation(String value) { 
      if (Utilities.noString(value))
        this.improvementNotation = null;
      else {
        if (this.improvementNotation == null)
          this.improvementNotation = new StringType();
        this.improvementNotation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #definition} (A narrative description of the complete measure calculation.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public MarkdownType getDefinitionElement() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.definition");
        else if (Configuration.doAutoCreate())
          this.definition = new MarkdownType(); // bb
      return this.definition;
    }

    public boolean hasDefinitionElement() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    public boolean hasDefinition() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    /**
     * @param value {@link #definition} (A narrative description of the complete measure calculation.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public Measure setDefinitionElement(MarkdownType value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return A narrative description of the complete measure calculation.
     */
    public String getDefinition() { 
      return this.definition == null ? null : this.definition.getValue();
    }

    /**
     * @param value A narrative description of the complete measure calculation.
     */
    public Measure setDefinition(String value) { 
      if (value == null)
        this.definition = null;
      else {
        if (this.definition == null)
          this.definition = new MarkdownType();
        this.definition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #guidance} (Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.). This is the underlying object with id, value and extensions. The accessor "getGuidance" gives direct access to the value
     */
    public MarkdownType getGuidanceElement() { 
      if (this.guidance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.guidance");
        else if (Configuration.doAutoCreate())
          this.guidance = new MarkdownType(); // bb
      return this.guidance;
    }

    public boolean hasGuidanceElement() { 
      return this.guidance != null && !this.guidance.isEmpty();
    }

    public boolean hasGuidance() { 
      return this.guidance != null && !this.guidance.isEmpty();
    }

    /**
     * @param value {@link #guidance} (Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.). This is the underlying object with id, value and extensions. The accessor "getGuidance" gives direct access to the value
     */
    public Measure setGuidanceElement(MarkdownType value) { 
      this.guidance = value;
      return this;
    }

    /**
     * @return Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.
     */
    public String getGuidance() { 
      return this.guidance == null ? null : this.guidance.getValue();
    }

    /**
     * @param value Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.
     */
    public Measure setGuidance(String value) { 
      if (value == null)
        this.guidance = null;
      else {
        if (this.guidance == null)
          this.guidance = new MarkdownType();
        this.guidance.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #set} (The measure set, e.g. Preventive Care and Screening.). This is the underlying object with id, value and extensions. The accessor "getSet" gives direct access to the value
     */
    public StringType getSetElement() { 
      if (this.set == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.set");
        else if (Configuration.doAutoCreate())
          this.set = new StringType(); // bb
      return this.set;
    }

    public boolean hasSetElement() { 
      return this.set != null && !this.set.isEmpty();
    }

    public boolean hasSet() { 
      return this.set != null && !this.set.isEmpty();
    }

    /**
     * @param value {@link #set} (The measure set, e.g. Preventive Care and Screening.). This is the underlying object with id, value and extensions. The accessor "getSet" gives direct access to the value
     */
    public Measure setSetElement(StringType value) { 
      this.set = value;
      return this;
    }

    /**
     * @return The measure set, e.g. Preventive Care and Screening.
     */
    public String getSet() { 
      return this.set == null ? null : this.set.getValue();
    }

    /**
     * @param value The measure set, e.g. Preventive Care and Screening.
     */
    public Measure setSet(String value) { 
      if (Utilities.noString(value))
        this.set = null;
      else {
        if (this.set == null)
          this.set = new StringType();
        this.set.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #group} (A group of population criteria for the measure.)
     */
    public List<MeasureGroupComponent> getGroup() { 
      if (this.group == null)
        this.group = new ArrayList<MeasureGroupComponent>();
      return this.group;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setGroup(List<MeasureGroupComponent> theGroup) { 
      this.group = theGroup;
      return this;
    }

    public boolean hasGroup() { 
      if (this.group == null)
        return false;
      for (MeasureGroupComponent item : this.group)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MeasureGroupComponent addGroup() { //3
      MeasureGroupComponent t = new MeasureGroupComponent();
      if (this.group == null)
        this.group = new ArrayList<MeasureGroupComponent>();
      this.group.add(t);
      return t;
    }

    public Measure addGroup(MeasureGroupComponent t) { //3
      if (t == null)
        return this;
      if (this.group == null)
        this.group = new ArrayList<MeasureGroupComponent>();
      this.group.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #group}, creating it if it does not already exist
     */
    public MeasureGroupComponent getGroupFirstRep() { 
      if (getGroup().isEmpty()) {
        addGroup();
      }
      return getGroup().get(0);
    }

    /**
     * @return {@link #supplementalData} (The supplemental data criteria for the measure report, specified as either the name of a valid CQL expression within a referenced library, or a valid FHIR Resource Path.)
     */
    public List<MeasureSupplementalDataComponent> getSupplementalData() { 
      if (this.supplementalData == null)
        this.supplementalData = new ArrayList<MeasureSupplementalDataComponent>();
      return this.supplementalData;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setSupplementalData(List<MeasureSupplementalDataComponent> theSupplementalData) { 
      this.supplementalData = theSupplementalData;
      return this;
    }

    public boolean hasSupplementalData() { 
      if (this.supplementalData == null)
        return false;
      for (MeasureSupplementalDataComponent item : this.supplementalData)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MeasureSupplementalDataComponent addSupplementalData() { //3
      MeasureSupplementalDataComponent t = new MeasureSupplementalDataComponent();
      if (this.supplementalData == null)
        this.supplementalData = new ArrayList<MeasureSupplementalDataComponent>();
      this.supplementalData.add(t);
      return t;
    }

    public Measure addSupplementalData(MeasureSupplementalDataComponent t) { //3
      if (t == null)
        return this;
      if (this.supplementalData == null)
        this.supplementalData = new ArrayList<MeasureSupplementalDataComponent>();
      this.supplementalData.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supplementalData}, creating it if it does not already exist
     */
    public MeasureSupplementalDataComponent getSupplementalDataFirstRep() { 
      if (getSupplementalData().isEmpty()) {
        addSupplementalData();
      }
      return getSupplementalData().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this measure when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the measure such as the CMS or NQF identifier. Note that at least one identifier is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the measure, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A machine-friendly name for the measure. This name should be usable as an identifier for the measure by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the measure.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of the measure.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "Determines whether the measure was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("description", "string", "A free text natural language description of the measure from the consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "string", "A brief description of the purpose of the measure.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the measure is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("publicationDate", "date", "The date on which the measure was published.", 0, java.lang.Integer.MAX_VALUE, publicationDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the measure content was last reviewed.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the measure content is effective.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("coverage", "UsageContext", "Specifies various attributes of the patient population for whom and/or environment of care in which, the measure is applicable.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("topic", "CodeableConcept", "Clinical topics related to the content of the measure.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the measure, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the measure (also known as the steward for the measure). This information is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedResource", "RelatedResource", "Related resources such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedResource));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing the formal logic used by the measure.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("disclaimer", "markdown", "A disclaimer for the use of the measure.", 0, java.lang.Integer.MAX_VALUE, disclaimer));
        childrenList.add(new Property("scoring", "code", "The measure scoring type, e.g. proportion, CV.", 0, java.lang.Integer.MAX_VALUE, scoring));
        childrenList.add(new Property("type", "code", "The measure type, e.g. process, outcome.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("riskAdjustment", "string", "A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.", 0, java.lang.Integer.MAX_VALUE, riskAdjustment));
        childrenList.add(new Property("rateAggregation", "string", "A description of the rate aggregation for the measure.", 0, java.lang.Integer.MAX_VALUE, rateAggregation));
        childrenList.add(new Property("rationale", "markdown", "The rationale for the measure.", 0, java.lang.Integer.MAX_VALUE, rationale));
        childrenList.add(new Property("clinicalRecommendationStatement", "markdown", "The clinical recommendation statement for the measure.", 0, java.lang.Integer.MAX_VALUE, clinicalRecommendationStatement));
        childrenList.add(new Property("improvementNotation", "string", "Improvement notation for the measure, e.g. higher score indicates better quality.", 0, java.lang.Integer.MAX_VALUE, improvementNotation));
        childrenList.add(new Property("definition", "markdown", "A narrative description of the complete measure calculation.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("guidance", "markdown", "Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.", 0, java.lang.Integer.MAX_VALUE, guidance));
        childrenList.add(new Property("set", "string", "The measure set, e.g. Preventive Care and Screening.", 0, java.lang.Integer.MAX_VALUE, set));
        childrenList.add(new Property("group", "", "A group of population criteria for the measure.", 0, java.lang.Integer.MAX_VALUE, group));
        childrenList.add(new Property("supplementalData", "", "The supplemental data criteria for the measure report, specified as either the name of a valid CQL expression within a referenced library, or a valid FHIR Resource Path.", 0, java.lang.Integer.MAX_VALUE, supplementalData));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MeasureStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // StringType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case 1470566394: /*publicationDate*/ return this.publicationDate == null ? new Base[0] : new Base[] {this.publicationDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : this.coverage.toArray(new Base[this.coverage.size()]); // UsageContext
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Contributor
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // StringType
        case 1554540889: /*relatedResource*/ return this.relatedResource == null ? new Base[0] : this.relatedResource.toArray(new Base[this.relatedResource.size()]); // RelatedResource
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case 432371099: /*disclaimer*/ return this.disclaimer == null ? new Base[0] : new Base[] {this.disclaimer}; // MarkdownType
        case 1924005583: /*scoring*/ return this.scoring == null ? new Base[0] : new Base[] {this.scoring}; // Enumeration<MeasureScoring>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // Enumeration<MeasureType>
        case 93273500: /*riskAdjustment*/ return this.riskAdjustment == null ? new Base[0] : new Base[] {this.riskAdjustment}; // StringType
        case 1254503906: /*rateAggregation*/ return this.rateAggregation == null ? new Base[0] : new Base[] {this.rateAggregation}; // StringType
        case 345689335: /*rationale*/ return this.rationale == null ? new Base[0] : new Base[] {this.rationale}; // MarkdownType
        case -18631389: /*clinicalRecommendationStatement*/ return this.clinicalRecommendationStatement == null ? new Base[0] : new Base[] {this.clinicalRecommendationStatement}; // MarkdownType
        case -2085456136: /*improvementNotation*/ return this.improvementNotation == null ? new Base[0] : new Base[] {this.improvementNotation}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // MarkdownType
        case -1314002088: /*guidance*/ return this.guidance == null ? new Base[0] : new Base[] {this.guidance}; // MarkdownType
        case 113762: /*set*/ return this.set == null ? new Base[0] : new Base[] {this.set}; // StringType
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // MeasureGroupComponent
        case 1447496814: /*supplementalData*/ return this.supplementalData == null ? new Base[0] : this.supplementalData.toArray(new Base[this.supplementalData.size()]); // MeasureSupplementalDataComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = new MeasureStatusEnumFactory().fromType(value); // Enumeration<MeasureStatus>
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -220463842: // purpose
          this.purpose = castToString(value); // StringType
          break;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          break;
        case 1470566394: // publicationDate
          this.publicationDate = castToDate(value); // DateType
          break;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          break;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          break;
        case -351767064: // coverage
          this.getCoverage().add(castToUsageContext(value)); // UsageContext
          break;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          break;
        case 1522889671: // copyright
          this.copyright = castToString(value); // StringType
          break;
        case 1554540889: // relatedResource
          this.getRelatedResource().add(castToRelatedResource(value)); // RelatedResource
          break;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          break;
        case 432371099: // disclaimer
          this.disclaimer = castToMarkdown(value); // MarkdownType
          break;
        case 1924005583: // scoring
          this.scoring = new MeasureScoringEnumFactory().fromType(value); // Enumeration<MeasureScoring>
          break;
        case 3575610: // type
          this.getType().add(new MeasureTypeEnumFactory().fromType(value)); // Enumeration<MeasureType>
          break;
        case 93273500: // riskAdjustment
          this.riskAdjustment = castToString(value); // StringType
          break;
        case 1254503906: // rateAggregation
          this.rateAggregation = castToString(value); // StringType
          break;
        case 345689335: // rationale
          this.rationale = castToMarkdown(value); // MarkdownType
          break;
        case -18631389: // clinicalRecommendationStatement
          this.clinicalRecommendationStatement = castToMarkdown(value); // MarkdownType
          break;
        case -2085456136: // improvementNotation
          this.improvementNotation = castToString(value); // StringType
          break;
        case -1014418093: // definition
          this.definition = castToMarkdown(value); // MarkdownType
          break;
        case -1314002088: // guidance
          this.guidance = castToMarkdown(value); // MarkdownType
          break;
        case 113762: // set
          this.set = castToString(value); // StringType
          break;
        case 98629247: // group
          this.getGroup().add((MeasureGroupComponent) value); // MeasureGroupComponent
          break;
        case 1447496814: // supplementalData
          this.getSupplementalData().add((MeasureSupplementalDataComponent) value); // MeasureSupplementalDataComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new MeasureStatusEnumFactory().fromType(value); // Enumeration<MeasureStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("purpose"))
          this.purpose = castToString(value); // StringType
        else if (name.equals("usage"))
          this.usage = castToString(value); // StringType
        else if (name.equals("publicationDate"))
          this.publicationDate = castToDate(value); // DateType
        else if (name.equals("lastReviewDate"))
          this.lastReviewDate = castToDate(value); // DateType
        else if (name.equals("effectivePeriod"))
          this.effectivePeriod = castToPeriod(value); // Period
        else if (name.equals("coverage"))
          this.getCoverage().add(castToUsageContext(value));
        else if (name.equals("topic"))
          this.getTopic().add(castToCodeableConcept(value));
        else if (name.equals("contributor"))
          this.getContributor().add(castToContributor(value));
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add(castToContactDetail(value));
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("relatedResource"))
          this.getRelatedResource().add(castToRelatedResource(value));
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("disclaimer"))
          this.disclaimer = castToMarkdown(value); // MarkdownType
        else if (name.equals("scoring"))
          this.scoring = new MeasureScoringEnumFactory().fromType(value); // Enumeration<MeasureScoring>
        else if (name.equals("type"))
          this.getType().add(new MeasureTypeEnumFactory().fromType(value));
        else if (name.equals("riskAdjustment"))
          this.riskAdjustment = castToString(value); // StringType
        else if (name.equals("rateAggregation"))
          this.rateAggregation = castToString(value); // StringType
        else if (name.equals("rationale"))
          this.rationale = castToMarkdown(value); // MarkdownType
        else if (name.equals("clinicalRecommendationStatement"))
          this.clinicalRecommendationStatement = castToMarkdown(value); // MarkdownType
        else if (name.equals("improvementNotation"))
          this.improvementNotation = castToString(value); // StringType
        else if (name.equals("definition"))
          this.definition = castToMarkdown(value); // MarkdownType
        else if (name.equals("guidance"))
          this.guidance = castToMarkdown(value); // MarkdownType
        else if (name.equals("set"))
          this.set = castToString(value); // StringType
        else if (name.equals("group"))
          this.getGroup().add((MeasureGroupComponent) value);
        else if (name.equals("supplementalData"))
          this.getSupplementalData().add((MeasureSupplementalDataComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1618432855:  return addIdentifier(); // Identifier
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MeasureStatus>
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -220463842: throw new FHIRException("Cannot make property purpose as it is not a complex type"); // StringType
        case 111574433: throw new FHIRException("Cannot make property usage as it is not a complex type"); // StringType
        case 1470566394: throw new FHIRException("Cannot make property publicationDate as it is not a complex type"); // DateType
        case -1687512484: throw new FHIRException("Cannot make property lastReviewDate as it is not a complex type"); // DateType
        case -403934648:  return getEffectivePeriod(); // Period
        case -351767064:  return addCoverage(); // UsageContext
        case 110546223:  return addTopic(); // CodeableConcept
        case -1895276325:  return addContributor(); // Contributor
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ContactDetail
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // StringType
        case 1554540889:  return addRelatedResource(); // RelatedResource
        case 166208699:  return addLibrary(); // Reference
        case 432371099: throw new FHIRException("Cannot make property disclaimer as it is not a complex type"); // MarkdownType
        case 1924005583: throw new FHIRException("Cannot make property scoring as it is not a complex type"); // Enumeration<MeasureScoring>
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<MeasureType>
        case 93273500: throw new FHIRException("Cannot make property riskAdjustment as it is not a complex type"); // StringType
        case 1254503906: throw new FHIRException("Cannot make property rateAggregation as it is not a complex type"); // StringType
        case 345689335: throw new FHIRException("Cannot make property rationale as it is not a complex type"); // MarkdownType
        case -18631389: throw new FHIRException("Cannot make property clinicalRecommendationStatement as it is not a complex type"); // MarkdownType
        case -2085456136: throw new FHIRException("Cannot make property improvementNotation as it is not a complex type"); // StringType
        case -1014418093: throw new FHIRException("Cannot make property definition as it is not a complex type"); // MarkdownType
        case -1314002088: throw new FHIRException("Cannot make property guidance as it is not a complex type"); // MarkdownType
        case 113762: throw new FHIRException("Cannot make property set as it is not a complex type"); // StringType
        case 98629247:  return addGroup(); // MeasureGroupComponent
        case 1447496814:  return addSupplementalData(); // MeasureSupplementalDataComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.experimental");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.usage");
        }
        else if (name.equals("publicationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.publicationDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.copyright");
        }
        else if (name.equals("relatedResource")) {
          return addRelatedResource();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("disclaimer")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.disclaimer");
        }
        else if (name.equals("scoring")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.scoring");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.type");
        }
        else if (name.equals("riskAdjustment")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.riskAdjustment");
        }
        else if (name.equals("rateAggregation")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.rateAggregation");
        }
        else if (name.equals("rationale")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.rationale");
        }
        else if (name.equals("clinicalRecommendationStatement")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.clinicalRecommendationStatement");
        }
        else if (name.equals("improvementNotation")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.improvementNotation");
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.definition");
        }
        else if (name.equals("guidance")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.guidance");
        }
        else if (name.equals("set")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.set");
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else if (name.equals("supplementalData")) {
          return addSupplementalData();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Measure";

  }

      public Measure copy() {
        Measure dst = new Measure();
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
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.publicationDate = publicationDate == null ? null : publicationDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (coverage != null) {
          dst.coverage = new ArrayList<UsageContext>();
          for (UsageContext i : coverage)
            dst.coverage.add(i.copy());
        };
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (contributor != null) {
          dst.contributor = new ArrayList<Contributor>();
          for (Contributor i : contributor)
            dst.contributor.add(i.copy());
        };
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        if (relatedResource != null) {
          dst.relatedResource = new ArrayList<RelatedResource>();
          for (RelatedResource i : relatedResource)
            dst.relatedResource.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        dst.disclaimer = disclaimer == null ? null : disclaimer.copy();
        dst.scoring = scoring == null ? null : scoring.copy();
        if (type != null) {
          dst.type = new ArrayList<Enumeration<MeasureType>>();
          for (Enumeration<MeasureType> i : type)
            dst.type.add(i.copy());
        };
        dst.riskAdjustment = riskAdjustment == null ? null : riskAdjustment.copy();
        dst.rateAggregation = rateAggregation == null ? null : rateAggregation.copy();
        dst.rationale = rationale == null ? null : rationale.copy();
        dst.clinicalRecommendationStatement = clinicalRecommendationStatement == null ? null : clinicalRecommendationStatement.copy();
        dst.improvementNotation = improvementNotation == null ? null : improvementNotation.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.guidance = guidance == null ? null : guidance.copy();
        dst.set = set == null ? null : set.copy();
        if (group != null) {
          dst.group = new ArrayList<MeasureGroupComponent>();
          for (MeasureGroupComponent i : group)
            dst.group.add(i.copy());
        };
        if (supplementalData != null) {
          dst.supplementalData = new ArrayList<MeasureSupplementalDataComponent>();
          for (MeasureSupplementalDataComponent i : supplementalData)
            dst.supplementalData.add(i.copy());
        };
        return dst;
      }

      protected Measure typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Measure))
          return false;
        Measure o = (Measure) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(status, o.status, true)
           && compareDeep(experimental, o.experimental, true) && compareDeep(description, o.description, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true) && compareDeep(publicationDate, o.publicationDate, true)
           && compareDeep(lastReviewDate, o.lastReviewDate, true) && compareDeep(effectivePeriod, o.effectivePeriod, true)
           && compareDeep(coverage, o.coverage, true) && compareDeep(topic, o.topic, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(relatedResource, o.relatedResource, true) && compareDeep(library, o.library, true)
           && compareDeep(disclaimer, o.disclaimer, true) && compareDeep(scoring, o.scoring, true) && compareDeep(type, o.type, true)
           && compareDeep(riskAdjustment, o.riskAdjustment, true) && compareDeep(rateAggregation, o.rateAggregation, true)
           && compareDeep(rationale, o.rationale, true) && compareDeep(clinicalRecommendationStatement, o.clinicalRecommendationStatement, true)
           && compareDeep(improvementNotation, o.improvementNotation, true) && compareDeep(definition, o.definition, true)
           && compareDeep(guidance, o.guidance, true) && compareDeep(set, o.set, true) && compareDeep(group, o.group, true)
           && compareDeep(supplementalData, o.supplementalData, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Measure))
          return false;
        Measure o = (Measure) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true)
           && compareValues(description, o.description, true) && compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true)
           && compareValues(publicationDate, o.publicationDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
           && compareValues(publisher, o.publisher, true) && compareValues(copyright, o.copyright, true) && compareValues(disclaimer, o.disclaimer, true)
           && compareValues(scoring, o.scoring, true) && compareValues(type, o.type, true) && compareValues(riskAdjustment, o.riskAdjustment, true)
           && compareValues(rateAggregation, o.rateAggregation, true) && compareValues(rationale, o.rationale, true)
           && compareValues(clinicalRecommendationStatement, o.clinicalRecommendationStatement, true) && compareValues(improvementNotation, o.improvementNotation, true)
           && compareValues(definition, o.definition, true) && compareValues(guidance, o.guidance, true) && compareValues(set, o.set, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(url, identifier, version
          , name, title, status, experimental, description, purpose, usage, publicationDate
          , lastReviewDate, effectivePeriod, coverage, topic, contributor, publisher, contact
          , copyright, relatedResource, library, disclaimer, scoring, type, riskAdjustment
          , rateAggregation, rationale, clinicalRecommendationStatement, improvementNotation, definition
          , guidance, set, group, supplementalData);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Measure;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Measure.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="Measure.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="Measure.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="Measure.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="Measure.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Measure.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

