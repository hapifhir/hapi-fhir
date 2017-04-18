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
 * The Measure resource provides the definition of a quality measure.
 */
@ResourceDef(name="Measure", profile="http://hl7.org/fhir/Profile/Measure")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "status", "experimental", "date", "publisher", "description", "purpose", "usage", "approvalDate", "lastReviewDate", "effectivePeriod", "useContext", "jurisdiction", "topic", "contributor", "contact", "copyright", "relatedArtifact", "library", "disclaimer", "scoring", "compositeScoring", "type", "riskAdjustment", "rateAggregation", "rationale", "clinicalRecommendationStatement", "improvementNotation", "definition", "guidance", "set", "group", "supplementalData"})
public class Measure extends MetadataResource {

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
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -2023558323: // population
          this.getPopulation().add((MeasureGroupPopulationComponent) value); // MeasureGroupPopulationComponent
          return value;
        case 90983669: // stratifier
          this.getStratifier().add((MeasureGroupStratifierComponent) value); // MeasureGroupStratifierComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("population")) {
          this.getPopulation().add((MeasureGroupPopulationComponent) value);
        } else if (name.equals("stratifier")) {
          this.getStratifier().add((MeasureGroupStratifierComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case -2023558323:  return addPopulation(); 
        case 90983669:  return addStratifier(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -2023558323: /*population*/ return new String[] {};
        case 90983669: /*stratifier*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
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
         * A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique identifier", formalDefinition="A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report." )
        protected Identifier identifier;

        /**
         * The type of population criteria.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-observation", formalDefinition="The type of population criteria." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-population")
        protected CodeableConcept code;

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

        private static final long serialVersionUID = -561575429L;

    /**
     * Constructor
     */
      public MeasureGroupPopulationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasureGroupPopulationComponent(StringType criteria) {
        super();
        this.criteria = criteria;
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
         * @return {@link #code} (The type of population criteria.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasureGroupPopulationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The type of population criteria.)
         */
        public MeasureGroupPopulationComponent setCode(CodeableConcept value) { 
          this.code = value;
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
          childrenList.add(new Property("identifier", "Identifier", "A unique identifier for the population criteria. This identifier is used to report data against this criteria within the measure report.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("code", "CodeableConcept", "The type of population criteria.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("name", "string", "Optional name or short description of this population.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "The human readable description of this population criteria.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("criteria", "string", "The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.", 0, java.lang.Integer.MAX_VALUE, criteria));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1952046943: /*criteria*/ return this.criteria == null ? new Base[0] : new Base[] {this.criteria}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("criteria")) {
          this.criteria = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3059181:  return getCode(); 
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case 1952046943:  return getCriteriaElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 1952046943: /*criteria*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
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
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.code = code == null ? null : code.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true) && compareDeep(name, o.name, true)
           && compareDeep(description, o.description, true) && compareDeep(criteria, o.criteria, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasureGroupPopulationComponent))
          return false;
        MeasureGroupPopulationComponent o = (MeasureGroupPopulationComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(criteria, o.criteria, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, code, name, description
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
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The identifier for the stratifier used to coordinate the reported data back to this stratifier", formalDefinition="The identifier for the stratifier used to coordinate the reported data back to this stratifier." )
        protected Identifier identifier;

        /**
         * The criteria for the stratifier. This must be the name of an expression defined within a referenced library.
         */
        @Child(name = "criteria", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the measure should be stratified", formalDefinition="The criteria for the stratifier. This must be the name of an expression defined within a referenced library." )
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
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("criteria")) {
          this.criteria = castToString(value); // StringType
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 1952046943:  return getCriteriaElement();
        case 3433509:  return getPathElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 1952046943: /*criteria*/ return new String[] {"string"};
        case 3433509: /*path*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
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
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier, unique within the measure", formalDefinition="An identifier for the supplemental data." )
        protected Identifier identifier;

        /**
         * An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.
         */
        @Child(name = "usage", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="supplemental-data | risk-adjustment-factor", formalDefinition="An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-data-usage")
        protected List<CodeableConcept> usage;

        /**
         * The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.
         */
        @Child(name = "criteria", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Expression describing additional data to be reported", formalDefinition="The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element." )
        protected StringType criteria;

        /**
         * The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.
         */
        @Child(name = "path", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Path to the supplemental data element", formalDefinition="The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path." )
        protected StringType path;

        private static final long serialVersionUID = -101576770L;

    /**
     * Constructor
     */
      public MeasureSupplementalDataComponent() {
        super();
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
        public List<CodeableConcept> getUsage() { 
          if (this.usage == null)
            this.usage = new ArrayList<CodeableConcept>();
          return this.usage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MeasureSupplementalDataComponent setUsage(List<CodeableConcept> theUsage) { 
          this.usage = theUsage;
          return this;
        }

        public boolean hasUsage() { 
          if (this.usage == null)
            return false;
          for (CodeableConcept item : this.usage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addUsage() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.usage == null)
            this.usage = new ArrayList<CodeableConcept>();
          this.usage.add(t);
          return t;
        }

        public MeasureSupplementalDataComponent addUsage(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.usage == null)
            this.usage = new ArrayList<CodeableConcept>();
          this.usage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #usage}, creating it if it does not already exist
         */
        public CodeableConcept getUsageFirstRep() { 
          if (getUsage().isEmpty()) {
            addUsage();
          }
          return getUsage().get(0);
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
          childrenList.add(new Property("usage", "CodeableConcept", "An indicator of the intended usage for the supplemental data element. Supplemental data indicates the data is additional information requested to augment the measure information. Risk adjustment factor indicates the data is additional information used to calculate risk adjustment factors when applying a risk model to the measure calculation.", 0, java.lang.Integer.MAX_VALUE, usage));
          childrenList.add(new Property("criteria", "string", "The criteria for the supplemental data. This must be the name of a valid expression defined within a referenced library, and defines the data to be returned for this element.", 0, java.lang.Integer.MAX_VALUE, criteria));
          childrenList.add(new Property("path", "string", "The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.", 0, java.lang.Integer.MAX_VALUE, path));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : this.usage.toArray(new Base[this.usage.size()]); // CodeableConcept
        case 1952046943: /*criteria*/ return this.criteria == null ? new Base[0] : new Base[] {this.criteria}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 111574433: // usage
          this.getUsage().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("usage")) {
          this.getUsage().add(castToCodeableConcept(value));
        } else if (name.equals("criteria")) {
          this.criteria = castToString(value); // StringType
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 111574433:  return addUsage(); 
        case 1952046943:  return getCriteriaElement();
        case 3433509:  return getPathElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 111574433: /*usage*/ return new String[] {"CodeableConcept"};
        case 1952046943: /*criteria*/ return new String[] {"string"};
        case 3433509: /*path*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("usage")) {
          return addUsage();
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
          dst.usage = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : usage)
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
        return compareValues(criteria, o.criteria, true) && compareValues(path, o.path, true);
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
     * A formal identifier that is used to identify this measure when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the measure", formalDefinition="A formal identifier that is used to identify this measure when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * Explaination of why this measure is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this measure is defined", formalDefinition="Explaination of why this measure is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A detailed description of how the measure is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the measure", formalDefinition="A detailed description of how the measure is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the measure was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the measure was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the measure content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the measure is expected to be used", formalDefinition="The period during which the measure content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * Descriptive topics related to the content of the measure. Topics provide a high-level categorization of the type of the measure that can be useful for filtering and searching.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="E.g. Education, Treatment, Assessment, etc", formalDefinition="Descriptive topics related to the content of the measure. Topics provide a high-level categorization of the type of the measure that can be useful for filtering and searching." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/definition-topic")
    protected List<CodeableConcept> topic;

    /**
     * A contributor to the content of the measure, including authors, editors, reviewers, and endorsers.
     */
    @Child(name = "contributor", type = {Contributor.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A content contributor", formalDefinition="A contributor to the content of the measure, including authors, editors, reviewers, and endorsers." )
    protected List<Contributor> contributor;

    /**
     * A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure." )
    protected MarkdownType copyright;

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional documentation, citations, etc", formalDefinition="Related artifacts such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * A reference to a Library resource containing the formal logic used by the measure.
     */
    @Child(name = "library", type = {Library.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the measure", formalDefinition="A reference to a Library resource containing the formal logic used by the measure." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing the formal logic used by the measure.)
     */
    protected List<Library> libraryTarget;


    /**
     * Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure.
     */
    @Child(name = "disclaimer", type = {MarkdownType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Disclaimer for use of the measure or its referenced content", formalDefinition="Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure." )
    protected MarkdownType disclaimer;

    /**
     * Indicates how the calculation is performed for the measure, including proportion, ratio, continuous variable, and cohort. The value set is extensible, allowing additional measure scoring types to be represented.
     */
    @Child(name = "scoring", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="proportion | ratio | continuous-variable | cohort", formalDefinition="Indicates how the calculation is performed for the measure, including proportion, ratio, continuous variable, and cohort. The value set is extensible, allowing additional measure scoring types to be represented." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-scoring")
    protected CodeableConcept scoring;

    /**
     * If this is a composite measure, the scoring method used to combine the component measures to determine the composite score.
     */
    @Child(name = "compositeScoring", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="opportunity | all-or-nothing | linear | weighted", formalDefinition="If this is a composite measure, the scoring method used to combine the component measures to determine the composite score." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/composite-measure-scoring")
    protected CodeableConcept compositeScoring;

    /**
     * Indicates whether the measure is used to examine a process, an outcome over time, a patient-reported outcome, or a structure measure such as utilization.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="process | outcome | structure | patient-reported-outcome | composite", formalDefinition="Indicates whether the measure is used to examine a process, an outcome over time, a patient-reported outcome, or a structure measure such as utilization." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measure-type")
    protected List<CodeableConcept> type;

    /**
     * A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.
     */
    @Child(name = "riskAdjustment", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="How is risk adjustment applied for this measure", formalDefinition="A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results." )
    protected StringType riskAdjustment;

    /**
     * Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result.
     */
    @Child(name = "rateAggregation", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="How is rate aggregation performed for this measure", formalDefinition="Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result." )
    protected StringType rateAggregation;

    /**
     * Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence.
     */
    @Child(name = "rationale", type = {MarkdownType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why does this measure exist", formalDefinition="Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence." )
    protected MarkdownType rationale;

    /**
     * Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure.
     */
    @Child(name = "clinicalRecommendationStatement", type = {MarkdownType.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Summary of clinical guidelines", formalDefinition="Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure." )
    protected MarkdownType clinicalRecommendationStatement;

    /**
     * Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range).
     */
    @Child(name = "improvementNotation", type = {StringType.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Improvement notation for the measure, e.g. higher score indicates better quality", formalDefinition="Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range)." )
    protected StringType improvementNotation;

    /**
     * Provides a description of an individual term used within the measure.
     */
    @Child(name = "definition", type = {MarkdownType.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Defined terms used in the measure documentation", formalDefinition="Provides a description of an individual term used within the measure." )
    protected List<MarkdownType> definition;

    /**
     * Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure.
     */
    @Child(name = "guidance", type = {MarkdownType.class}, order=21, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional guidance for implementers", formalDefinition="Additional guidance for the measure including how it can be used in a clinical context, and the intent of the measure." )
    protected MarkdownType guidance;

    /**
     * The measure set, e.g. Preventive Care and Screening.
     */
    @Child(name = "set", type = {StringType.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The measure set, e.g. Preventive Care and Screening", formalDefinition="The measure set, e.g. Preventive Care and Screening." )
    protected StringType set;

    /**
     * A group of population criteria for the measure.
     */
    @Child(name = "group", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Population criteria group", formalDefinition="A group of population criteria for the measure." )
    protected List<MeasureGroupComponent> group;

    /**
     * The supplemental data criteria for the measure report, specified as either the name of a valid CQL expression within a referenced library, or a valid FHIR Resource Path.
     */
    @Child(name = "supplementalData", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What other data should be reported with the measure", formalDefinition="The supplemental data criteria for the measure report, specified as either the name of a valid CQL expression within a referenced library, or a valid FHIR Resource Path." )
    protected List<MeasureSupplementalDataComponent> supplementalData;

    private static final long serialVersionUID = -875918689L;

  /**
   * Constructor
   */
    public Measure() {
      super();
    }

  /**
   * Constructor
   */
    public Measure(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this measure when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published. The URL SHOULD include the major version of the measure. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URI that is used to identify this measure when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published. The URL SHOULD include the major version of the measure. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Measure setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this measure when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published. The URL SHOULD include the major version of the measure. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this measure when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published. The URL SHOULD include the major version of the measure. For more information see [Technical and Business Versions](resource.html#versions).
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
     * @return {@link #identifier} (A formal identifier that is used to identify this measure when it is represented in other formats, or referenced in a specification, model, design or an instance.)
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
     * @return {@link #version} (The identifier that is used to identify this version of the measure when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the measure author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (The identifier that is used to identify this version of the measure when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the measure author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Measure setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the measure when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the measure author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the measure when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the measure author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
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
     * @return {@link #name} (A natural language name identifying the measure. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A natural language name identifying the measure. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Measure setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the measure. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the measure. This name should be usable as an identifier for the module by machine processing applications such as code generation.
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
     * @return {@link #status} (The status of this measure. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.status");
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
     * @param value {@link #status} (The status of this measure. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Measure setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this measure. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this measure. Enables tracking the life-cycle of the content.
     */
    public Measure setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this measure is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (A boolean value to indicate that this measure is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public Measure setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this measure is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this measure is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public Measure setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the measure was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the measure changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the measure was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the measure changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Measure setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the measure was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the measure changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the measure was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the measure changes.
     */
    public Measure setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the measure.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
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
     * @param value {@link #publisher} (The name of the individual or organization that published the measure.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public Measure setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the measure.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the measure.
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
     * @return {@link #description} (A free text natural language description of the measure from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.description");
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
     * @param value {@link #description} (A free text natural language description of the measure from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Measure setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the measure from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the measure from a consumer's perspective.
     */
    public Measure setDescription(String value) { 
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
     * @return {@link #purpose} (Explaination of why this measure is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.purpose");
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
     * @param value {@link #purpose} (Explaination of why this measure is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public Measure setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this measure is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this measure is needed and why it has been designed as it has.
     */
    public Measure setPurpose(String value) { 
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
     * @return {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public DateType getApprovalDateElement() { 
      if (this.approvalDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.approvalDate");
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
    public Measure setApprovalDateElement(DateType value) { 
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
    public Measure setApprovalDate(Date value) { 
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
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
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
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public Measure setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
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
     * @return {@link #effectivePeriod} (The period during which the measure content was or is planned to be in active use.)
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
     * @param value {@link #effectivePeriod} (The period during which the measure content was or is planned to be in active use.)
     */
    public Measure setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate measure instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setUseContext(List<UsageContext> theUseContext) { 
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

    public Measure addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the measure is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public Measure addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #topic} (Descriptive topics related to the content of the measure. Topics provide a high-level categorization of the type of the measure that can be useful for filtering and searching.)
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
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public Measure setCopyrightElement(MarkdownType value) { 
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
    public Measure setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
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

    public Measure addRelatedArtifact(RelatedArtifact t) { //3
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
     * @return {@link #disclaimer} (Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure.). This is the underlying object with id, value and extensions. The accessor "getDisclaimer" gives direct access to the value
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
     * @param value {@link #disclaimer} (Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure.). This is the underlying object with id, value and extensions. The accessor "getDisclaimer" gives direct access to the value
     */
    public Measure setDisclaimerElement(MarkdownType value) { 
      this.disclaimer = value;
      return this;
    }

    /**
     * @return Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure.
     */
    public String getDisclaimer() { 
      return this.disclaimer == null ? null : this.disclaimer.getValue();
    }

    /**
     * @param value Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure.
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
     * @return {@link #scoring} (Indicates how the calculation is performed for the measure, including proportion, ratio, continuous variable, and cohort. The value set is extensible, allowing additional measure scoring types to be represented.)
     */
    public CodeableConcept getScoring() { 
      if (this.scoring == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.scoring");
        else if (Configuration.doAutoCreate())
          this.scoring = new CodeableConcept(); // cc
      return this.scoring;
    }

    public boolean hasScoring() { 
      return this.scoring != null && !this.scoring.isEmpty();
    }

    /**
     * @param value {@link #scoring} (Indicates how the calculation is performed for the measure, including proportion, ratio, continuous variable, and cohort. The value set is extensible, allowing additional measure scoring types to be represented.)
     */
    public Measure setScoring(CodeableConcept value) { 
      this.scoring = value;
      return this;
    }

    /**
     * @return {@link #compositeScoring} (If this is a composite measure, the scoring method used to combine the component measures to determine the composite score.)
     */
    public CodeableConcept getCompositeScoring() { 
      if (this.compositeScoring == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.compositeScoring");
        else if (Configuration.doAutoCreate())
          this.compositeScoring = new CodeableConcept(); // cc
      return this.compositeScoring;
    }

    public boolean hasCompositeScoring() { 
      return this.compositeScoring != null && !this.compositeScoring.isEmpty();
    }

    /**
     * @param value {@link #compositeScoring} (If this is a composite measure, the scoring method used to combine the component measures to determine the composite score.)
     */
    public Measure setCompositeScoring(CodeableConcept value) { 
      this.compositeScoring = value;
      return this;
    }

    /**
     * @return {@link #type} (Indicates whether the measure is used to examine a process, an outcome over time, a patient-reported outcome, or a structure measure such as utilization.)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setType(List<CodeableConcept> theType) { 
      this.type = theType;
      return this;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    public Measure addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
     */
    public CodeableConcept getTypeFirstRep() { 
      if (getType().isEmpty()) {
        addType();
      }
      return getType().get(0);
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
     * @return {@link #rateAggregation} (Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result.). This is the underlying object with id, value and extensions. The accessor "getRateAggregation" gives direct access to the value
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
     * @param value {@link #rateAggregation} (Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result.). This is the underlying object with id, value and extensions. The accessor "getRateAggregation" gives direct access to the value
     */
    public Measure setRateAggregationElement(StringType value) { 
      this.rateAggregation = value;
      return this;
    }

    /**
     * @return Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result.
     */
    public String getRateAggregation() { 
      return this.rateAggregation == null ? null : this.rateAggregation.getValue();
    }

    /**
     * @param value Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result.
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
     * @return {@link #rationale} (Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence.). This is the underlying object with id, value and extensions. The accessor "getRationale" gives direct access to the value
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
     * @param value {@link #rationale} (Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence.). This is the underlying object with id, value and extensions. The accessor "getRationale" gives direct access to the value
     */
    public Measure setRationaleElement(MarkdownType value) { 
      this.rationale = value;
      return this;
    }

    /**
     * @return Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence.
     */
    public String getRationale() { 
      return this.rationale == null ? null : this.rationale.getValue();
    }

    /**
     * @param value Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence.
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
     * @return {@link #clinicalRecommendationStatement} (Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure.). This is the underlying object with id, value and extensions. The accessor "getClinicalRecommendationStatement" gives direct access to the value
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
     * @param value {@link #clinicalRecommendationStatement} (Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure.). This is the underlying object with id, value and extensions. The accessor "getClinicalRecommendationStatement" gives direct access to the value
     */
    public Measure setClinicalRecommendationStatementElement(MarkdownType value) { 
      this.clinicalRecommendationStatement = value;
      return this;
    }

    /**
     * @return Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure.
     */
    public String getClinicalRecommendationStatement() { 
      return this.clinicalRecommendationStatement == null ? null : this.clinicalRecommendationStatement.getValue();
    }

    /**
     * @param value Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure.
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
     * @return {@link #improvementNotation} (Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range).). This is the underlying object with id, value and extensions. The accessor "getImprovementNotation" gives direct access to the value
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
     * @param value {@link #improvementNotation} (Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range).). This is the underlying object with id, value and extensions. The accessor "getImprovementNotation" gives direct access to the value
     */
    public Measure setImprovementNotationElement(StringType value) { 
      this.improvementNotation = value;
      return this;
    }

    /**
     * @return Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range).
     */
    public String getImprovementNotation() { 
      return this.improvementNotation == null ? null : this.improvementNotation.getValue();
    }

    /**
     * @param value Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range).
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
     * @return {@link #definition} (Provides a description of an individual term used within the measure.)
     */
    public List<MarkdownType> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<MarkdownType>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Measure setDefinition(List<MarkdownType> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (MarkdownType item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #definition} (Provides a description of an individual term used within the measure.)
     */
    public MarkdownType addDefinitionElement() {//2 
      MarkdownType t = new MarkdownType();
      if (this.definition == null)
        this.definition = new ArrayList<MarkdownType>();
      this.definition.add(t);
      return t;
    }

    /**
     * @param value {@link #definition} (Provides a description of an individual term used within the measure.)
     */
    public Measure addDefinition(String value) { //1
      MarkdownType t = new MarkdownType();
      t.setValue(value);
      if (this.definition == null)
        this.definition = new ArrayList<MarkdownType>();
      this.definition.add(t);
      return this;
    }

    /**
     * @param value {@link #definition} (Provides a description of an individual term used within the measure.)
     */
    public boolean hasDefinition(String value) { 
      if (this.definition == null)
        return false;
      for (MarkdownType v : this.definition)
        if (v.equals(value)) // markdown
          return true;
      return false;
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
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this measure when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this measure is (or will be) published. The URL SHOULD include the major version of the measure. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this measure when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the measure when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the measure author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the measure. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the measure.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this measure. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this measure is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the measure was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the measure changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the measure.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the measure from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this measure is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the measure is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, java.lang.Integer.MAX_VALUE, approvalDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the measure content was or is planned to be in active use.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate measure instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the measure is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the measure. Topics provide a high-level categorization of the type of the measure that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the measure, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the measure and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the measure.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing the formal logic used by the measure.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("disclaimer", "markdown", "Notices and disclaimers regarding the use of the measure, or related to intellectual property (such as code systems) referenced by the measure.", 0, java.lang.Integer.MAX_VALUE, disclaimer));
        childrenList.add(new Property("scoring", "CodeableConcept", "Indicates how the calculation is performed for the measure, including proportion, ratio, continuous variable, and cohort. The value set is extensible, allowing additional measure scoring types to be represented.", 0, java.lang.Integer.MAX_VALUE, scoring));
        childrenList.add(new Property("compositeScoring", "CodeableConcept", "If this is a composite measure, the scoring method used to combine the component measures to determine the composite score.", 0, java.lang.Integer.MAX_VALUE, compositeScoring));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates whether the measure is used to examine a process, an outcome over time, a patient-reported outcome, or a structure measure such as utilization.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("riskAdjustment", "string", "A description of the risk adjustment factors that may impact the resulting score for the measure and how they may be accounted for when computing and reporting measure results.", 0, java.lang.Integer.MAX_VALUE, riskAdjustment));
        childrenList.add(new Property("rateAggregation", "string", "Describes how to combine the information calculated, based on logic in each of several populations, into one summarized result.", 0, java.lang.Integer.MAX_VALUE, rateAggregation));
        childrenList.add(new Property("rationale", "markdown", "Provides a succint statement of the need for the measure. Usually includes statements pertaining to importance criterion: impact, gap in care, and evidence.", 0, java.lang.Integer.MAX_VALUE, rationale));
        childrenList.add(new Property("clinicalRecommendationStatement", "markdown", "Provides a summary of relevant clinical guidelines or other clinical recommendations supporting the measure.", 0, java.lang.Integer.MAX_VALUE, clinicalRecommendationStatement));
        childrenList.add(new Property("improvementNotation", "string", "Information on whether an increase or decrease in score is the preferred result (e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is whthin a range).", 0, java.lang.Integer.MAX_VALUE, improvementNotation));
        childrenList.add(new Property("definition", "markdown", "Provides a description of an individual term used within the measure.", 0, java.lang.Integer.MAX_VALUE, definition));
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
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Contributor
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case 432371099: /*disclaimer*/ return this.disclaimer == null ? new Base[0] : new Base[] {this.disclaimer}; // MarkdownType
        case 1924005583: /*scoring*/ return this.scoring == null ? new Base[0] : new Base[] {this.scoring}; // CodeableConcept
        case 569347656: /*compositeScoring*/ return this.compositeScoring == null ? new Base[0] : new Base[] {this.compositeScoring}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 93273500: /*riskAdjustment*/ return this.riskAdjustment == null ? new Base[0] : new Base[] {this.riskAdjustment}; // StringType
        case 1254503906: /*rateAggregation*/ return this.rateAggregation == null ? new Base[0] : new Base[] {this.rateAggregation}; // StringType
        case 345689335: /*rationale*/ return this.rationale == null ? new Base[0] : new Base[] {this.rationale}; // MarkdownType
        case -18631389: /*clinicalRecommendationStatement*/ return this.clinicalRecommendationStatement == null ? new Base[0] : new Base[] {this.clinicalRecommendationStatement}; // MarkdownType
        case -2085456136: /*improvementNotation*/ return this.improvementNotation == null ? new Base[0] : new Base[] {this.improvementNotation}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // MarkdownType
        case -1314002088: /*guidance*/ return this.guidance == null ? new Base[0] : new Base[] {this.guidance}; // MarkdownType
        case 113762: /*set*/ return this.set == null ? new Base[0] : new Base[] {this.set}; // StringType
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // MeasureGroupComponent
        case 1447496814: /*supplementalData*/ return this.supplementalData == null ? new Base[0] : this.supplementalData.toArray(new Base[this.supplementalData.size()]); // MeasureSupplementalDataComponent
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
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
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
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          return value;
        case 432371099: // disclaimer
          this.disclaimer = castToMarkdown(value); // MarkdownType
          return value;
        case 1924005583: // scoring
          this.scoring = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 569347656: // compositeScoring
          this.compositeScoring = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 93273500: // riskAdjustment
          this.riskAdjustment = castToString(value); // StringType
          return value;
        case 1254503906: // rateAggregation
          this.rateAggregation = castToString(value); // StringType
          return value;
        case 345689335: // rationale
          this.rationale = castToMarkdown(value); // MarkdownType
          return value;
        case -18631389: // clinicalRecommendationStatement
          this.clinicalRecommendationStatement = castToMarkdown(value); // MarkdownType
          return value;
        case -2085456136: // improvementNotation
          this.improvementNotation = castToString(value); // StringType
          return value;
        case -1014418093: // definition
          this.getDefinition().add(castToMarkdown(value)); // MarkdownType
          return value;
        case -1314002088: // guidance
          this.guidance = castToMarkdown(value); // MarkdownType
          return value;
        case 113762: // set
          this.set = castToString(value); // StringType
          return value;
        case 98629247: // group
          this.getGroup().add((MeasureGroupComponent) value); // MeasureGroupComponent
          return value;
        case 1447496814: // supplementalData
          this.getSupplementalData().add((MeasureSupplementalDataComponent) value); // MeasureSupplementalDataComponent
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
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("usage")) {
          this.usage = castToString(value); // StringType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("topic")) {
          this.getTopic().add(castToCodeableConcept(value));
        } else if (name.equals("contributor")) {
          this.getContributor().add(castToContributor(value));
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("relatedArtifact")) {
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        } else if (name.equals("library")) {
          this.getLibrary().add(castToReference(value));
        } else if (name.equals("disclaimer")) {
          this.disclaimer = castToMarkdown(value); // MarkdownType
        } else if (name.equals("scoring")) {
          this.scoring = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("compositeScoring")) {
          this.compositeScoring = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("riskAdjustment")) {
          this.riskAdjustment = castToString(value); // StringType
        } else if (name.equals("rateAggregation")) {
          this.rateAggregation = castToString(value); // StringType
        } else if (name.equals("rationale")) {
          this.rationale = castToMarkdown(value); // MarkdownType
        } else if (name.equals("clinicalRecommendationStatement")) {
          this.clinicalRecommendationStatement = castToMarkdown(value); // MarkdownType
        } else if (name.equals("improvementNotation")) {
          this.improvementNotation = castToString(value); // StringType
        } else if (name.equals("definition")) {
          this.getDefinition().add(castToMarkdown(value));
        } else if (name.equals("guidance")) {
          this.guidance = castToMarkdown(value); // MarkdownType
        } else if (name.equals("set")) {
          this.set = castToString(value); // StringType
        } else if (name.equals("group")) {
          this.getGroup().add((MeasureGroupComponent) value);
        } else if (name.equals("supplementalData")) {
          this.getSupplementalData().add((MeasureSupplementalDataComponent) value);
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
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case -1724546052:  return getDescriptionElement();
        case -220463842:  return getPurposeElement();
        case 111574433:  return getUsageElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 110546223:  return addTopic(); 
        case -1895276325:  return addContributor(); 
        case 951526432:  return addContact(); 
        case 1522889671:  return getCopyrightElement();
        case 666807069:  return addRelatedArtifact(); 
        case 166208699:  return addLibrary(); 
        case 432371099:  return getDisclaimerElement();
        case 1924005583:  return getScoring(); 
        case 569347656:  return getCompositeScoring(); 
        case 3575610:  return addType(); 
        case 93273500:  return getRiskAdjustmentElement();
        case 1254503906:  return getRateAggregationElement();
        case 345689335:  return getRationaleElement();
        case -18631389:  return getClinicalRecommendationStatementElement();
        case -2085456136:  return getImprovementNotationElement();
        case -1014418093:  return addDefinitionElement();
        case -1314002088:  return getGuidanceElement();
        case 113762:  return getSetElement();
        case 98629247:  return addGroup(); 
        case 1447496814:  return addSupplementalData(); 
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
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 111574433: /*usage*/ return new String[] {"string"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 110546223: /*topic*/ return new String[] {"CodeableConcept"};
        case -1895276325: /*contributor*/ return new String[] {"Contributor"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 666807069: /*relatedArtifact*/ return new String[] {"RelatedArtifact"};
        case 166208699: /*library*/ return new String[] {"Reference"};
        case 432371099: /*disclaimer*/ return new String[] {"markdown"};
        case 1924005583: /*scoring*/ return new String[] {"CodeableConcept"};
        case 569347656: /*compositeScoring*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 93273500: /*riskAdjustment*/ return new String[] {"string"};
        case 1254503906: /*rateAggregation*/ return new String[] {"string"};
        case 345689335: /*rationale*/ return new String[] {"markdown"};
        case -18631389: /*clinicalRecommendationStatement*/ return new String[] {"markdown"};
        case -2085456136: /*improvementNotation*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"markdown"};
        case -1314002088: /*guidance*/ return new String[] {"markdown"};
        case 113762: /*set*/ return new String[] {"string"};
        case 98629247: /*group*/ return new String[] {};
        case 1447496814: /*supplementalData*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
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
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.publisher");
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
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.copyright");
        }
        else if (name.equals("relatedArtifact")) {
          return addRelatedArtifact();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("disclaimer")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.disclaimer");
        }
        else if (name.equals("scoring")) {
          this.scoring = new CodeableConcept();
          return this.scoring;
        }
        else if (name.equals("compositeScoring")) {
          this.compositeScoring = new CodeableConcept();
          return this.compositeScoring;
        }
        else if (name.equals("type")) {
          return addType();
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
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
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
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        if (relatedArtifact != null) {
          dst.relatedArtifact = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : relatedArtifact)
            dst.relatedArtifact.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        dst.disclaimer = disclaimer == null ? null : disclaimer.copy();
        dst.scoring = scoring == null ? null : scoring.copy();
        dst.compositeScoring = compositeScoring == null ? null : compositeScoring.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.riskAdjustment = riskAdjustment == null ? null : riskAdjustment.copy();
        dst.rateAggregation = rateAggregation == null ? null : rateAggregation.copy();
        dst.rationale = rationale == null ? null : rationale.copy();
        dst.clinicalRecommendationStatement = clinicalRecommendationStatement == null ? null : clinicalRecommendationStatement.copy();
        dst.improvementNotation = improvementNotation == null ? null : improvementNotation.copy();
        if (definition != null) {
          dst.definition = new ArrayList<MarkdownType>();
          for (MarkdownType i : definition)
            dst.definition.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true)
           && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(topic, o.topic, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(relatedArtifact, o.relatedArtifact, true)
           && compareDeep(library, o.library, true) && compareDeep(disclaimer, o.disclaimer, true) && compareDeep(scoring, o.scoring, true)
           && compareDeep(compositeScoring, o.compositeScoring, true) && compareDeep(type, o.type, true) && compareDeep(riskAdjustment, o.riskAdjustment, true)
           && compareDeep(rateAggregation, o.rateAggregation, true) && compareDeep(rationale, o.rationale, true)
           && compareDeep(clinicalRecommendationStatement, o.clinicalRecommendationStatement, true) && compareDeep(improvementNotation, o.improvementNotation, true)
           && compareDeep(definition, o.definition, true) && compareDeep(guidance, o.guidance, true) && compareDeep(set, o.set, true)
           && compareDeep(group, o.group, true) && compareDeep(supplementalData, o.supplementalData, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Measure))
          return false;
        Measure o = (Measure) other;
        return compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true) && compareValues(approvalDate, o.approvalDate, true)
           && compareValues(lastReviewDate, o.lastReviewDate, true) && compareValues(copyright, o.copyright, true)
           && compareValues(disclaimer, o.disclaimer, true) && compareValues(riskAdjustment, o.riskAdjustment, true)
           && compareValues(rateAggregation, o.rateAggregation, true) && compareValues(rationale, o.rationale, true)
           && compareValues(clinicalRecommendationStatement, o.clinicalRecommendationStatement, true) && compareValues(improvementNotation, o.improvementNotation, true)
           && compareValues(definition, o.definition, true) && compareValues(guidance, o.guidance, true) && compareValues(set, o.set, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, purpose, usage
          , approvalDate, lastReviewDate, effectivePeriod, topic, contributor, copyright, relatedArtifact
          , library, disclaimer, scoring, compositeScoring, type, riskAdjustment, rateAggregation
          , rationale, clinicalRecommendationStatement, improvementNotation, definition, guidance
          , set, group, supplementalData);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Measure;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The measure publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Measure.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Measure.date", description="The measure publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The measure publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Measure.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Measure.identifier", description="External identifier for the measure", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="successor", path="Measure.relatedArtifact.where(type='successor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_SUCCESSOR = "successor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUCCESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUCCESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Measure:successor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUCCESSOR = new ca.uhn.fhir.model.api.Include("Measure:successor").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="Measure.jurisdiction", description="Intended jurisdiction for the measure", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="Measure.description", description="The description of the measure", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="derived-from", path="Measure.relatedArtifact.where(type='derived-from').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_DERIVED_FROM = "derived-from";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DERIVED_FROM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DERIVED_FROM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Measure:derived-from</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DERIVED_FROM = new ca.uhn.fhir.model.api.Include("Measure:derived-from").toLocked();

 /**
   * Search parameter: <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="predecessor", path="Measure.relatedArtifact.where(type='predecessor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_PREDECESSOR = "predecessor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PREDECESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PREDECESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Measure:predecessor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PREDECESSOR = new ca.uhn.fhir.model.api.Include("Measure:predecessor").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="Measure.title", description="The human-friendly name of the measure", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="composed-of", path="Measure.relatedArtifact.where(type='composed-of').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_COMPOSED_OF = "composed-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPOSED_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPOSED_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Measure:composed-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPOSED_OF = new ca.uhn.fhir.model.api.Include("Measure:composed-of").toLocked();

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="Measure.version", description="The business version of the measure", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the measure</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Measure.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="Measure.url", description="The uri that identifies the measure", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the measure</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Measure.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the measure is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Measure.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="Measure.effectivePeriod", description="The time during which the measure is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the measure is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Measure.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource, Measure.library</b><br>
   * </p>
   */
  @SearchParamDefinition(name="depends-on", path="Measure.relatedArtifact.where(type='depends-on').resource | Measure.library", description="What resource is being referenced", type="reference" )
  public static final String SP_DEPENDS_ON = "depends-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Measure.relatedArtifact.resource, Measure.library</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDS_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDS_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Measure:depends-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDS_ON = new ca.uhn.fhir.model.api.Include("Measure:depends-on").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Measure.name", description="Computationally friendly name of the measure", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="Measure.publisher", description="Name of the publisher of the measure", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the measure</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Measure.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

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
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Measure.status", description="The current status of the measure", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the measure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Measure.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

