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
 * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
 */
@ResourceDef(name="MedicinalProductClinicals", profile="http://hl7.org/fhir/Profile/MedicinalProductClinicals")
public class MedicinalProductClinicals extends DomainResource {

    @Block()
    public static class MedicinalProductClinicalsUndesirableEffectsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The symptom, condition or undesirable effect.
         */
        @Child(name = "symptomConditionEffect", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The symptom, condition or undesirable effect", formalDefinition="The symptom, condition or undesirable effect." )
        protected CodeableConcept symptomConditionEffect;

        /**
         * Classification of the effect.
         */
        @Child(name = "classification", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Classification of the effect", formalDefinition="Classification of the effect." )
        protected CodeableConcept classification;

        /**
         * The frequency of occurrence of the effect.
         */
        @Child(name = "frequencyOfOccurrence", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The frequency of occurrence of the effect", formalDefinition="The frequency of occurrence of the effect." )
        protected CodeableConcept frequencyOfOccurrence;

        /**
         * The population group to which this applies.
         */
        @Child(name = "population", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The population group to which this applies", formalDefinition="The population group to which this applies." )
        protected List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> population;

        private static final long serialVersionUID = 2112477180L;

    /**
     * Constructor
     */
      public MedicinalProductClinicalsUndesirableEffectsComponent() {
        super();
      }

        /**
         * @return {@link #symptomConditionEffect} (The symptom, condition or undesirable effect.)
         */
        public CodeableConcept getSymptomConditionEffect() { 
          if (this.symptomConditionEffect == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsUndesirableEffectsComponent.symptomConditionEffect");
            else if (Configuration.doAutoCreate())
              this.symptomConditionEffect = new CodeableConcept(); // cc
          return this.symptomConditionEffect;
        }

        public boolean hasSymptomConditionEffect() { 
          return this.symptomConditionEffect != null && !this.symptomConditionEffect.isEmpty();
        }

        /**
         * @param value {@link #symptomConditionEffect} (The symptom, condition or undesirable effect.)
         */
        public MedicinalProductClinicalsUndesirableEffectsComponent setSymptomConditionEffect(CodeableConcept value) { 
          this.symptomConditionEffect = value;
          return this;
        }

        /**
         * @return {@link #classification} (Classification of the effect.)
         */
        public CodeableConcept getClassification() { 
          if (this.classification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsUndesirableEffectsComponent.classification");
            else if (Configuration.doAutoCreate())
              this.classification = new CodeableConcept(); // cc
          return this.classification;
        }

        public boolean hasClassification() { 
          return this.classification != null && !this.classification.isEmpty();
        }

        /**
         * @param value {@link #classification} (Classification of the effect.)
         */
        public MedicinalProductClinicalsUndesirableEffectsComponent setClassification(CodeableConcept value) { 
          this.classification = value;
          return this;
        }

        /**
         * @return {@link #frequencyOfOccurrence} (The frequency of occurrence of the effect.)
         */
        public CodeableConcept getFrequencyOfOccurrence() { 
          if (this.frequencyOfOccurrence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsUndesirableEffectsComponent.frequencyOfOccurrence");
            else if (Configuration.doAutoCreate())
              this.frequencyOfOccurrence = new CodeableConcept(); // cc
          return this.frequencyOfOccurrence;
        }

        public boolean hasFrequencyOfOccurrence() { 
          return this.frequencyOfOccurrence != null && !this.frequencyOfOccurrence.isEmpty();
        }

        /**
         * @param value {@link #frequencyOfOccurrence} (The frequency of occurrence of the effect.)
         */
        public MedicinalProductClinicalsUndesirableEffectsComponent setFrequencyOfOccurrence(CodeableConcept value) { 
          this.frequencyOfOccurrence = value;
          return this;
        }

        /**
         * @return {@link #population} (The population group to which this applies.)
         */
        public List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          return this.population;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsUndesirableEffectsComponent setPopulation(List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> thePopulation) { 
          this.population = thePopulation;
          return this;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MedicinalProductClinicalsUndesirableEffectsPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent addPopulation() { //3
          MedicinalProductClinicalsUndesirableEffectsPopulationComponent t = new MedicinalProductClinicalsUndesirableEffectsPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          this.population.add(t);
          return t;
        }

        public MedicinalProductClinicalsUndesirableEffectsComponent addPopulation(MedicinalProductClinicalsUndesirableEffectsPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent getPopulationFirstRep() { 
          if (getPopulation().isEmpty()) {
            addPopulation();
          }
          return getPopulation().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("symptomConditionEffect", "CodeableConcept", "The symptom, condition or undesirable effect.", 0, 1, symptomConditionEffect));
          children.add(new Property("classification", "CodeableConcept", "Classification of the effect.", 0, 1, classification));
          children.add(new Property("frequencyOfOccurrence", "CodeableConcept", "The frequency of occurrence of the effect.", 0, 1, frequencyOfOccurrence));
          children.add(new Property("population", "", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -650549981: /*symptomConditionEffect*/  return new Property("symptomConditionEffect", "CodeableConcept", "The symptom, condition or undesirable effect.", 0, 1, symptomConditionEffect);
          case 382350310: /*classification*/  return new Property("classification", "CodeableConcept", "Classification of the effect.", 0, 1, classification);
          case 791175812: /*frequencyOfOccurrence*/  return new Property("frequencyOfOccurrence", "CodeableConcept", "The frequency of occurrence of the effect.", 0, 1, frequencyOfOccurrence);
          case -2023558323: /*population*/  return new Property("population", "", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -650549981: /*symptomConditionEffect*/ return this.symptomConditionEffect == null ? new Base[0] : new Base[] {this.symptomConditionEffect}; // CodeableConcept
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : new Base[] {this.classification}; // CodeableConcept
        case 791175812: /*frequencyOfOccurrence*/ return this.frequencyOfOccurrence == null ? new Base[0] : new Base[] {this.frequencyOfOccurrence}; // CodeableConcept
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MedicinalProductClinicalsUndesirableEffectsPopulationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -650549981: // symptomConditionEffect
          this.symptomConditionEffect = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 382350310: // classification
          this.classification = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 791175812: // frequencyOfOccurrence
          this.frequencyOfOccurrence = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2023558323: // population
          this.getPopulation().add((MedicinalProductClinicalsUndesirableEffectsPopulationComponent) value); // MedicinalProductClinicalsUndesirableEffectsPopulationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("symptomConditionEffect")) {
          this.symptomConditionEffect = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("classification")) {
          this.classification = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("frequencyOfOccurrence")) {
          this.frequencyOfOccurrence = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("population")) {
          this.getPopulation().add((MedicinalProductClinicalsUndesirableEffectsPopulationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -650549981:  return getSymptomConditionEffect(); 
        case 382350310:  return getClassification(); 
        case 791175812:  return getFrequencyOfOccurrence(); 
        case -2023558323:  return addPopulation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -650549981: /*symptomConditionEffect*/ return new String[] {"CodeableConcept"};
        case 382350310: /*classification*/ return new String[] {"CodeableConcept"};
        case 791175812: /*frequencyOfOccurrence*/ return new String[] {"CodeableConcept"};
        case -2023558323: /*population*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("symptomConditionEffect")) {
          this.symptomConditionEffect = new CodeableConcept();
          return this.symptomConditionEffect;
        }
        else if (name.equals("classification")) {
          this.classification = new CodeableConcept();
          return this.classification;
        }
        else if (name.equals("frequencyOfOccurrence")) {
          this.frequencyOfOccurrence = new CodeableConcept();
          return this.frequencyOfOccurrence;
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductClinicalsUndesirableEffectsComponent copy() {
        MedicinalProductClinicalsUndesirableEffectsComponent dst = new MedicinalProductClinicalsUndesirableEffectsComponent();
        copyValues(dst);
        dst.symptomConditionEffect = symptomConditionEffect == null ? null : symptomConditionEffect.copy();
        dst.classification = classification == null ? null : classification.copy();
        dst.frequencyOfOccurrence = frequencyOfOccurrence == null ? null : frequencyOfOccurrence.copy();
        if (population != null) {
          dst.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          for (MedicinalProductClinicalsUndesirableEffectsPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsUndesirableEffectsComponent))
          return false;
        MedicinalProductClinicalsUndesirableEffectsComponent o = (MedicinalProductClinicalsUndesirableEffectsComponent) other_;
        return compareDeep(symptomConditionEffect, o.symptomConditionEffect, true) && compareDeep(classification, o.classification, true)
           && compareDeep(frequencyOfOccurrence, o.frequencyOfOccurrence, true) && compareDeep(population, o.population, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsUndesirableEffectsComponent))
          return false;
        MedicinalProductClinicalsUndesirableEffectsComponent o = (MedicinalProductClinicalsUndesirableEffectsComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(symptomConditionEffect, classification
          , frequencyOfOccurrence, population);
      }

  public String fhirType() {
    return "MedicinalProductClinicals.undesirableEffects";

  }

  }

    @Block()
    public static class MedicinalProductClinicalsUndesirableEffectsPopulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The age of the specific population.
         */
        @Child(name = "age", type = {Range.class, CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The age of the specific population", formalDefinition="The age of the specific population." )
        protected Type age;

        /**
         * The gender of the specific population.
         */
        @Child(name = "gender", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The gender of the specific population", formalDefinition="The gender of the specific population." )
        protected CodeableConcept gender;

        /**
         * Race of the specific population.
         */
        @Child(name = "race", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Race of the specific population", formalDefinition="Race of the specific population." )
        protected CodeableConcept race;

        /**
         * The existing physiological conditions of the specific population to which this applies.
         */
        @Child(name = "physiologicalCondition", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The existing physiological conditions of the specific population to which this applies", formalDefinition="The existing physiological conditions of the specific population to which this applies." )
        protected CodeableConcept physiologicalCondition;

        private static final long serialVersionUID = -394311584L;

    /**
     * Constructor
     */
      public MedicinalProductClinicalsUndesirableEffectsPopulationComponent() {
        super();
      }

        /**
         * @return {@link #age} (The age of the specific population.)
         */
        public Type getAge() { 
          return this.age;
        }

        /**
         * @return {@link #age} (The age of the specific population.)
         */
        public Range getAgeRange() throws FHIRException { 
          if (this.age == null)
            return null;
          if (!(this.age instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.age.getClass().getName()+" was encountered");
          return (Range) this.age;
        }

        public boolean hasAgeRange() { 
          return this != null && this.age instanceof Range;
        }

        /**
         * @return {@link #age} (The age of the specific population.)
         */
        public CodeableConcept getAgeCodeableConcept() throws FHIRException { 
          if (this.age == null)
            return null;
          if (!(this.age instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.age.getClass().getName()+" was encountered");
          return (CodeableConcept) this.age;
        }

        public boolean hasAgeCodeableConcept() { 
          return this != null && this.age instanceof CodeableConcept;
        }

        public boolean hasAge() { 
          return this.age != null && !this.age.isEmpty();
        }

        /**
         * @param value {@link #age} (The age of the specific population.)
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent setAge(Type value) { 
          if (value != null && !(value instanceof Range || value instanceof CodeableConcept))
            throw new Error("Not the right type for MedicinalProductClinicals.undesirableEffects.population.age[x]: "+value.fhirType());
          this.age = value;
          return this;
        }

        /**
         * @return {@link #gender} (The gender of the specific population.)
         */
        public CodeableConcept getGender() { 
          if (this.gender == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsUndesirableEffectsPopulationComponent.gender");
            else if (Configuration.doAutoCreate())
              this.gender = new CodeableConcept(); // cc
          return this.gender;
        }

        public boolean hasGender() { 
          return this.gender != null && !this.gender.isEmpty();
        }

        /**
         * @param value {@link #gender} (The gender of the specific population.)
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent setGender(CodeableConcept value) { 
          this.gender = value;
          return this;
        }

        /**
         * @return {@link #race} (Race of the specific population.)
         */
        public CodeableConcept getRace() { 
          if (this.race == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsUndesirableEffectsPopulationComponent.race");
            else if (Configuration.doAutoCreate())
              this.race = new CodeableConcept(); // cc
          return this.race;
        }

        public boolean hasRace() { 
          return this.race != null && !this.race.isEmpty();
        }

        /**
         * @param value {@link #race} (Race of the specific population.)
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent setRace(CodeableConcept value) { 
          this.race = value;
          return this;
        }

        /**
         * @return {@link #physiologicalCondition} (The existing physiological conditions of the specific population to which this applies.)
         */
        public CodeableConcept getPhysiologicalCondition() { 
          if (this.physiologicalCondition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsUndesirableEffectsPopulationComponent.physiologicalCondition");
            else if (Configuration.doAutoCreate())
              this.physiologicalCondition = new CodeableConcept(); // cc
          return this.physiologicalCondition;
        }

        public boolean hasPhysiologicalCondition() { 
          return this.physiologicalCondition != null && !this.physiologicalCondition.isEmpty();
        }

        /**
         * @param value {@link #physiologicalCondition} (The existing physiological conditions of the specific population to which this applies.)
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent setPhysiologicalCondition(CodeableConcept value) { 
          this.physiologicalCondition = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("age[x]", "Range|CodeableConcept", "The age of the specific population.", 0, 1, age));
          children.add(new Property("gender", "CodeableConcept", "The gender of the specific population.", 0, 1, gender));
          children.add(new Property("race", "CodeableConcept", "Race of the specific population.", 0, 1, race));
          children.add(new Property("physiologicalCondition", "CodeableConcept", "The existing physiological conditions of the specific population to which this applies.", 0, 1, physiologicalCondition));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1419716831: /*age[x]*/  return new Property("age[x]", "Range|CodeableConcept", "The age of the specific population.", 0, 1, age);
          case 96511: /*age*/  return new Property("age[x]", "Range|CodeableConcept", "The age of the specific population.", 0, 1, age);
          case 1442748286: /*ageRange*/  return new Property("age[x]", "Range|CodeableConcept", "The age of the specific population.", 0, 1, age);
          case -1452658526: /*ageCodeableConcept*/  return new Property("age[x]", "Range|CodeableConcept", "The age of the specific population.", 0, 1, age);
          case -1249512767: /*gender*/  return new Property("gender", "CodeableConcept", "The gender of the specific population.", 0, 1, gender);
          case 3492561: /*race*/  return new Property("race", "CodeableConcept", "Race of the specific population.", 0, 1, race);
          case -62715190: /*physiologicalCondition*/  return new Property("physiologicalCondition", "CodeableConcept", "The existing physiological conditions of the specific population to which this applies.", 0, 1, physiologicalCondition);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 96511: /*age*/ return this.age == null ? new Base[0] : new Base[] {this.age}; // Type
        case -1249512767: /*gender*/ return this.gender == null ? new Base[0] : new Base[] {this.gender}; // CodeableConcept
        case 3492561: /*race*/ return this.race == null ? new Base[0] : new Base[] {this.race}; // CodeableConcept
        case -62715190: /*physiologicalCondition*/ return this.physiologicalCondition == null ? new Base[0] : new Base[] {this.physiologicalCondition}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 96511: // age
          this.age = castToType(value); // Type
          return value;
        case -1249512767: // gender
          this.gender = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3492561: // race
          this.race = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -62715190: // physiologicalCondition
          this.physiologicalCondition = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("age[x]")) {
          this.age = castToType(value); // Type
        } else if (name.equals("gender")) {
          this.gender = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("race")) {
          this.race = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("physiologicalCondition")) {
          this.physiologicalCondition = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1419716831:  return getAge(); 
        case 96511:  return getAge(); 
        case -1249512767:  return getGender(); 
        case 3492561:  return getRace(); 
        case -62715190:  return getPhysiologicalCondition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 96511: /*age*/ return new String[] {"Range", "CodeableConcept"};
        case -1249512767: /*gender*/ return new String[] {"CodeableConcept"};
        case 3492561: /*race*/ return new String[] {"CodeableConcept"};
        case -62715190: /*physiologicalCondition*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("ageRange")) {
          this.age = new Range();
          return this.age;
        }
        else if (name.equals("ageCodeableConcept")) {
          this.age = new CodeableConcept();
          return this.age;
        }
        else if (name.equals("gender")) {
          this.gender = new CodeableConcept();
          return this.gender;
        }
        else if (name.equals("race")) {
          this.race = new CodeableConcept();
          return this.race;
        }
        else if (name.equals("physiologicalCondition")) {
          this.physiologicalCondition = new CodeableConcept();
          return this.physiologicalCondition;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductClinicalsUndesirableEffectsPopulationComponent copy() {
        MedicinalProductClinicalsUndesirableEffectsPopulationComponent dst = new MedicinalProductClinicalsUndesirableEffectsPopulationComponent();
        copyValues(dst);
        dst.age = age == null ? null : age.copy();
        dst.gender = gender == null ? null : gender.copy();
        dst.race = race == null ? null : race.copy();
        dst.physiologicalCondition = physiologicalCondition == null ? null : physiologicalCondition.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsUndesirableEffectsPopulationComponent))
          return false;
        MedicinalProductClinicalsUndesirableEffectsPopulationComponent o = (MedicinalProductClinicalsUndesirableEffectsPopulationComponent) other_;
        return compareDeep(age, o.age, true) && compareDeep(gender, o.gender, true) && compareDeep(race, o.race, true)
           && compareDeep(physiologicalCondition, o.physiologicalCondition, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsUndesirableEffectsPopulationComponent))
          return false;
        MedicinalProductClinicalsUndesirableEffectsPopulationComponent o = (MedicinalProductClinicalsUndesirableEffectsPopulationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(age, gender, race, physiologicalCondition
          );
      }

  public String fhirType() {
    return "MedicinalProductClinicals.undesirableEffects.population";

  }

  }

    @Block()
    public static class MedicinalProductClinicalsTherapeuticIndicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The disease, symptom or procedure that is the indication for treatment.
         */
        @Child(name = "diseaseSymptomProcedure", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The disease, symptom or procedure that is the indication for treatment", formalDefinition="The disease, symptom or procedure that is the indication for treatment." )
        protected CodeableConcept diseaseSymptomProcedure;

        /**
         * The status of the disease or symptom for which the indication applies.
         */
        @Child(name = "diseaseStatus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The status of the disease or symptom for which the indication applies", formalDefinition="The status of the disease or symptom for which the indication applies." )
        protected CodeableConcept diseaseStatus;

        /**
         * Comorbidity (concurrent condition) or co-infection as part of the indication.
         */
        @Child(name = "comorbidity", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Comorbidity (concurrent condition) or co-infection as part of the indication", formalDefinition="Comorbidity (concurrent condition) or co-infection as part of the indication." )
        protected List<CodeableConcept> comorbidity;

        /**
         * The intended effect, aim or strategy to be achieved by the indication.
         */
        @Child(name = "intendedEffect", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The intended effect, aim or strategy to be achieved by the indication", formalDefinition="The intended effect, aim or strategy to be achieved by the indication." )
        protected CodeableConcept intendedEffect;

        /**
         * Timing or duration information as part of the indication.
         */
        @Child(name = "duration", type = {Quantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Timing or duration information as part of the indication", formalDefinition="Timing or duration information as part of the indication." )
        protected Quantity duration;

        /**
         * Information about the use of the medicinal product in relation to other therapies as part of the indication.
         */
        @Child(name = "undesirableEffects", type = {MedicinalProductClinicalsUndesirableEffectsComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies as part of the indication", formalDefinition="Information about the use of the medicinal product in relation to other therapies as part of the indication." )
        protected List<MedicinalProductClinicalsUndesirableEffectsComponent> undesirableEffects;

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
         */
        @Child(name = "otherTherapy", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the indication", formalDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the indication." )
        protected List<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent> otherTherapy;

        /**
         * The population group to which this applies.
         */
        @Child(name = "population", type = {MedicinalProductClinicalsUndesirableEffectsPopulationComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The population group to which this applies", formalDefinition="The population group to which this applies." )
        protected List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> population;

        private static final long serialVersionUID = -18331206L;

    /**
     * Constructor
     */
      public MedicinalProductClinicalsTherapeuticIndicationComponent() {
        super();
      }

        /**
         * @return {@link #diseaseSymptomProcedure} (The disease, symptom or procedure that is the indication for treatment.)
         */
        public CodeableConcept getDiseaseSymptomProcedure() { 
          if (this.diseaseSymptomProcedure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsTherapeuticIndicationComponent.diseaseSymptomProcedure");
            else if (Configuration.doAutoCreate())
              this.diseaseSymptomProcedure = new CodeableConcept(); // cc
          return this.diseaseSymptomProcedure;
        }

        public boolean hasDiseaseSymptomProcedure() { 
          return this.diseaseSymptomProcedure != null && !this.diseaseSymptomProcedure.isEmpty();
        }

        /**
         * @param value {@link #diseaseSymptomProcedure} (The disease, symptom or procedure that is the indication for treatment.)
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setDiseaseSymptomProcedure(CodeableConcept value) { 
          this.diseaseSymptomProcedure = value;
          return this;
        }

        /**
         * @return {@link #diseaseStatus} (The status of the disease or symptom for which the indication applies.)
         */
        public CodeableConcept getDiseaseStatus() { 
          if (this.diseaseStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsTherapeuticIndicationComponent.diseaseStatus");
            else if (Configuration.doAutoCreate())
              this.diseaseStatus = new CodeableConcept(); // cc
          return this.diseaseStatus;
        }

        public boolean hasDiseaseStatus() { 
          return this.diseaseStatus != null && !this.diseaseStatus.isEmpty();
        }

        /**
         * @param value {@link #diseaseStatus} (The status of the disease or symptom for which the indication applies.)
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setDiseaseStatus(CodeableConcept value) { 
          this.diseaseStatus = value;
          return this;
        }

        /**
         * @return {@link #comorbidity} (Comorbidity (concurrent condition) or co-infection as part of the indication.)
         */
        public List<CodeableConcept> getComorbidity() { 
          if (this.comorbidity == null)
            this.comorbidity = new ArrayList<CodeableConcept>();
          return this.comorbidity;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setComorbidity(List<CodeableConcept> theComorbidity) { 
          this.comorbidity = theComorbidity;
          return this;
        }

        public boolean hasComorbidity() { 
          if (this.comorbidity == null)
            return false;
          for (CodeableConcept item : this.comorbidity)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addComorbidity() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.comorbidity == null)
            this.comorbidity = new ArrayList<CodeableConcept>();
          this.comorbidity.add(t);
          return t;
        }

        public MedicinalProductClinicalsTherapeuticIndicationComponent addComorbidity(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.comorbidity == null)
            this.comorbidity = new ArrayList<CodeableConcept>();
          this.comorbidity.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #comorbidity}, creating it if it does not already exist
         */
        public CodeableConcept getComorbidityFirstRep() { 
          if (getComorbidity().isEmpty()) {
            addComorbidity();
          }
          return getComorbidity().get(0);
        }

        /**
         * @return {@link #intendedEffect} (The intended effect, aim or strategy to be achieved by the indication.)
         */
        public CodeableConcept getIntendedEffect() { 
          if (this.intendedEffect == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsTherapeuticIndicationComponent.intendedEffect");
            else if (Configuration.doAutoCreate())
              this.intendedEffect = new CodeableConcept(); // cc
          return this.intendedEffect;
        }

        public boolean hasIntendedEffect() { 
          return this.intendedEffect != null && !this.intendedEffect.isEmpty();
        }

        /**
         * @param value {@link #intendedEffect} (The intended effect, aim or strategy to be achieved by the indication.)
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setIntendedEffect(CodeableConcept value) { 
          this.intendedEffect = value;
          return this;
        }

        /**
         * @return {@link #duration} (Timing or duration information as part of the indication.)
         */
        public Quantity getDuration() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsTherapeuticIndicationComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new Quantity(); // cc
          return this.duration;
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (Timing or duration information as part of the indication.)
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setDuration(Quantity value) { 
          this.duration = value;
          return this;
        }

        /**
         * @return {@link #undesirableEffects} (Information about the use of the medicinal product in relation to other therapies as part of the indication.)
         */
        public List<MedicinalProductClinicalsUndesirableEffectsComponent> getUndesirableEffects() { 
          if (this.undesirableEffects == null)
            this.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
          return this.undesirableEffects;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setUndesirableEffects(List<MedicinalProductClinicalsUndesirableEffectsComponent> theUndesirableEffects) { 
          this.undesirableEffects = theUndesirableEffects;
          return this;
        }

        public boolean hasUndesirableEffects() { 
          if (this.undesirableEffects == null)
            return false;
          for (MedicinalProductClinicalsUndesirableEffectsComponent item : this.undesirableEffects)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsUndesirableEffectsComponent addUndesirableEffects() { //3
          MedicinalProductClinicalsUndesirableEffectsComponent t = new MedicinalProductClinicalsUndesirableEffectsComponent();
          if (this.undesirableEffects == null)
            this.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
          this.undesirableEffects.add(t);
          return t;
        }

        public MedicinalProductClinicalsTherapeuticIndicationComponent addUndesirableEffects(MedicinalProductClinicalsUndesirableEffectsComponent t) { //3
          if (t == null)
            return this;
          if (this.undesirableEffects == null)
            this.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
          this.undesirableEffects.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #undesirableEffects}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsUndesirableEffectsComponent getUndesirableEffectsFirstRep() { 
          if (getUndesirableEffects().isEmpty()) {
            addUndesirableEffects();
          }
          return getUndesirableEffects().get(0);
        }

        /**
         * @return {@link #otherTherapy} (Information about the use of the medicinal product in relation to other therapies described as part of the indication.)
         */
        public List<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent> getOtherTherapy() { 
          if (this.otherTherapy == null)
            this.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          return this.otherTherapy;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setOtherTherapy(List<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent> theOtherTherapy) { 
          this.otherTherapy = theOtherTherapy;
          return this;
        }

        public boolean hasOtherTherapy() { 
          if (this.otherTherapy == null)
            return false;
          for (MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent item : this.otherTherapy)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent addOtherTherapy() { //3
          MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent t = new MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent();
          if (this.otherTherapy == null)
            this.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          this.otherTherapy.add(t);
          return t;
        }

        public MedicinalProductClinicalsTherapeuticIndicationComponent addOtherTherapy(MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent t) { //3
          if (t == null)
            return this;
          if (this.otherTherapy == null)
            this.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          this.otherTherapy.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #otherTherapy}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent getOtherTherapyFirstRep() { 
          if (getOtherTherapy().isEmpty()) {
            addOtherTherapy();
          }
          return getOtherTherapy().get(0);
        }

        /**
         * @return {@link #population} (The population group to which this applies.)
         */
        public List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          return this.population;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent setPopulation(List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> thePopulation) { 
          this.population = thePopulation;
          return this;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MedicinalProductClinicalsUndesirableEffectsPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent addPopulation() { //3
          MedicinalProductClinicalsUndesirableEffectsPopulationComponent t = new MedicinalProductClinicalsUndesirableEffectsPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          this.population.add(t);
          return t;
        }

        public MedicinalProductClinicalsTherapeuticIndicationComponent addPopulation(MedicinalProductClinicalsUndesirableEffectsPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent getPopulationFirstRep() { 
          if (getPopulation().isEmpty()) {
            addPopulation();
          }
          return getPopulation().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("diseaseSymptomProcedure", "CodeableConcept", "The disease, symptom or procedure that is the indication for treatment.", 0, 1, diseaseSymptomProcedure));
          children.add(new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for which the indication applies.", 0, 1, diseaseStatus));
          children.add(new Property("comorbidity", "CodeableConcept", "Comorbidity (concurrent condition) or co-infection as part of the indication.", 0, java.lang.Integer.MAX_VALUE, comorbidity));
          children.add(new Property("intendedEffect", "CodeableConcept", "The intended effect, aim or strategy to be achieved by the indication.", 0, 1, intendedEffect));
          children.add(new Property("duration", "Quantity", "Timing or duration information as part of the indication.", 0, 1, duration));
          children.add(new Property("undesirableEffects", "@MedicinalProductClinicals.undesirableEffects", "Information about the use of the medicinal product in relation to other therapies as part of the indication.", 0, java.lang.Integer.MAX_VALUE, undesirableEffects));
          children.add(new Property("otherTherapy", "", "Information about the use of the medicinal product in relation to other therapies described as part of the indication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy));
          children.add(new Property("population", "@MedicinalProductClinicals.undesirableEffects.population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1497395130: /*diseaseSymptomProcedure*/  return new Property("diseaseSymptomProcedure", "CodeableConcept", "The disease, symptom or procedure that is the indication for treatment.", 0, 1, diseaseSymptomProcedure);
          case -505503602: /*diseaseStatus*/  return new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for which the indication applies.", 0, 1, diseaseStatus);
          case -406395211: /*comorbidity*/  return new Property("comorbidity", "CodeableConcept", "Comorbidity (concurrent condition) or co-infection as part of the indication.", 0, java.lang.Integer.MAX_VALUE, comorbidity);
          case 1587112348: /*intendedEffect*/  return new Property("intendedEffect", "CodeableConcept", "The intended effect, aim or strategy to be achieved by the indication.", 0, 1, intendedEffect);
          case -1992012396: /*duration*/  return new Property("duration", "Quantity", "Timing or duration information as part of the indication.", 0, 1, duration);
          case 890492742: /*undesirableEffects*/  return new Property("undesirableEffects", "@MedicinalProductClinicals.undesirableEffects", "Information about the use of the medicinal product in relation to other therapies as part of the indication.", 0, java.lang.Integer.MAX_VALUE, undesirableEffects);
          case -544509127: /*otherTherapy*/  return new Property("otherTherapy", "", "Information about the use of the medicinal product in relation to other therapies described as part of the indication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy);
          case -2023558323: /*population*/  return new Property("population", "@MedicinalProductClinicals.undesirableEffects.population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1497395130: /*diseaseSymptomProcedure*/ return this.diseaseSymptomProcedure == null ? new Base[0] : new Base[] {this.diseaseSymptomProcedure}; // CodeableConcept
        case -505503602: /*diseaseStatus*/ return this.diseaseStatus == null ? new Base[0] : new Base[] {this.diseaseStatus}; // CodeableConcept
        case -406395211: /*comorbidity*/ return this.comorbidity == null ? new Base[0] : this.comorbidity.toArray(new Base[this.comorbidity.size()]); // CodeableConcept
        case 1587112348: /*intendedEffect*/ return this.intendedEffect == null ? new Base[0] : new Base[] {this.intendedEffect}; // CodeableConcept
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // Quantity
        case 890492742: /*undesirableEffects*/ return this.undesirableEffects == null ? new Base[0] : this.undesirableEffects.toArray(new Base[this.undesirableEffects.size()]); // MedicinalProductClinicalsUndesirableEffectsComponent
        case -544509127: /*otherTherapy*/ return this.otherTherapy == null ? new Base[0] : this.otherTherapy.toArray(new Base[this.otherTherapy.size()]); // MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MedicinalProductClinicalsUndesirableEffectsPopulationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1497395130: // diseaseSymptomProcedure
          this.diseaseSymptomProcedure = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -505503602: // diseaseStatus
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -406395211: // comorbidity
          this.getComorbidity().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1587112348: // intendedEffect
          this.intendedEffect = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1992012396: // duration
          this.duration = castToQuantity(value); // Quantity
          return value;
        case 890492742: // undesirableEffects
          this.getUndesirableEffects().add((MedicinalProductClinicalsUndesirableEffectsComponent) value); // MedicinalProductClinicalsUndesirableEffectsComponent
          return value;
        case -544509127: // otherTherapy
          this.getOtherTherapy().add((MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent) value); // MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent
          return value;
        case -2023558323: // population
          this.getPopulation().add((MedicinalProductClinicalsUndesirableEffectsPopulationComponent) value); // MedicinalProductClinicalsUndesirableEffectsPopulationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("diseaseSymptomProcedure")) {
          this.diseaseSymptomProcedure = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("comorbidity")) {
          this.getComorbidity().add(castToCodeableConcept(value));
        } else if (name.equals("intendedEffect")) {
          this.intendedEffect = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("duration")) {
          this.duration = castToQuantity(value); // Quantity
        } else if (name.equals("undesirableEffects")) {
          this.getUndesirableEffects().add((MedicinalProductClinicalsUndesirableEffectsComponent) value);
        } else if (name.equals("otherTherapy")) {
          this.getOtherTherapy().add((MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent) value);
        } else if (name.equals("population")) {
          this.getPopulation().add((MedicinalProductClinicalsUndesirableEffectsPopulationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1497395130:  return getDiseaseSymptomProcedure(); 
        case -505503602:  return getDiseaseStatus(); 
        case -406395211:  return addComorbidity(); 
        case 1587112348:  return getIntendedEffect(); 
        case -1992012396:  return getDuration(); 
        case 890492742:  return addUndesirableEffects(); 
        case -544509127:  return addOtherTherapy(); 
        case -2023558323:  return addPopulation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1497395130: /*diseaseSymptomProcedure*/ return new String[] {"CodeableConcept"};
        case -505503602: /*diseaseStatus*/ return new String[] {"CodeableConcept"};
        case -406395211: /*comorbidity*/ return new String[] {"CodeableConcept"};
        case 1587112348: /*intendedEffect*/ return new String[] {"CodeableConcept"};
        case -1992012396: /*duration*/ return new String[] {"Quantity"};
        case 890492742: /*undesirableEffects*/ return new String[] {"@MedicinalProductClinicals.undesirableEffects"};
        case -544509127: /*otherTherapy*/ return new String[] {};
        case -2023558323: /*population*/ return new String[] {"@MedicinalProductClinicals.undesirableEffects.population"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("diseaseSymptomProcedure")) {
          this.diseaseSymptomProcedure = new CodeableConcept();
          return this.diseaseSymptomProcedure;
        }
        else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = new CodeableConcept();
          return this.diseaseStatus;
        }
        else if (name.equals("comorbidity")) {
          return addComorbidity();
        }
        else if (name.equals("intendedEffect")) {
          this.intendedEffect = new CodeableConcept();
          return this.intendedEffect;
        }
        else if (name.equals("duration")) {
          this.duration = new Quantity();
          return this.duration;
        }
        else if (name.equals("undesirableEffects")) {
          return addUndesirableEffects();
        }
        else if (name.equals("otherTherapy")) {
          return addOtherTherapy();
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductClinicalsTherapeuticIndicationComponent copy() {
        MedicinalProductClinicalsTherapeuticIndicationComponent dst = new MedicinalProductClinicalsTherapeuticIndicationComponent();
        copyValues(dst);
        dst.diseaseSymptomProcedure = diseaseSymptomProcedure == null ? null : diseaseSymptomProcedure.copy();
        dst.diseaseStatus = diseaseStatus == null ? null : diseaseStatus.copy();
        if (comorbidity != null) {
          dst.comorbidity = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : comorbidity)
            dst.comorbidity.add(i.copy());
        };
        dst.intendedEffect = intendedEffect == null ? null : intendedEffect.copy();
        dst.duration = duration == null ? null : duration.copy();
        if (undesirableEffects != null) {
          dst.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
          for (MedicinalProductClinicalsUndesirableEffectsComponent i : undesirableEffects)
            dst.undesirableEffects.add(i.copy());
        };
        if (otherTherapy != null) {
          dst.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          for (MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent i : otherTherapy)
            dst.otherTherapy.add(i.copy());
        };
        if (population != null) {
          dst.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          for (MedicinalProductClinicalsUndesirableEffectsPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsTherapeuticIndicationComponent))
          return false;
        MedicinalProductClinicalsTherapeuticIndicationComponent o = (MedicinalProductClinicalsTherapeuticIndicationComponent) other_;
        return compareDeep(diseaseSymptomProcedure, o.diseaseSymptomProcedure, true) && compareDeep(diseaseStatus, o.diseaseStatus, true)
           && compareDeep(comorbidity, o.comorbidity, true) && compareDeep(intendedEffect, o.intendedEffect, true)
           && compareDeep(duration, o.duration, true) && compareDeep(undesirableEffects, o.undesirableEffects, true)
           && compareDeep(otherTherapy, o.otherTherapy, true) && compareDeep(population, o.population, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsTherapeuticIndicationComponent))
          return false;
        MedicinalProductClinicalsTherapeuticIndicationComponent o = (MedicinalProductClinicalsTherapeuticIndicationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(diseaseSymptomProcedure, diseaseStatus
          , comorbidity, intendedEffect, duration, undesirableEffects, otherTherapy, population
          );
      }

  public String fhirType() {
    return "MedicinalProductClinicals.therapeuticIndication";

  }

  }

    @Block()
    public static class MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of relationship between the medicinal product indication or contraindication and another therapy.
         */
        @Child(name = "therapyRelationshipType", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of relationship between the medicinal product indication or contraindication and another therapy", formalDefinition="The type of relationship between the medicinal product indication or contraindication and another therapy." )
        protected CodeableConcept therapyRelationshipType;

        /**
         * Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.
         */
        @Child(name = "medication", type = {CodeableConcept.class, MedicinalProduct.class, Medication.class, Substance.class, SubstanceSpecification.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication", formalDefinition="Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication." )
        protected Type medication;

        private static final long serialVersionUID = 1438478115L;

    /**
     * Constructor
     */
      public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent(CodeableConcept therapyRelationshipType, Type medication) {
        super();
        this.therapyRelationshipType = therapyRelationshipType;
        this.medication = medication;
      }

        /**
         * @return {@link #therapyRelationshipType} (The type of relationship between the medicinal product indication or contraindication and another therapy.)
         */
        public CodeableConcept getTherapyRelationshipType() { 
          if (this.therapyRelationshipType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent.therapyRelationshipType");
            else if (Configuration.doAutoCreate())
              this.therapyRelationshipType = new CodeableConcept(); // cc
          return this.therapyRelationshipType;
        }

        public boolean hasTherapyRelationshipType() { 
          return this.therapyRelationshipType != null && !this.therapyRelationshipType.isEmpty();
        }

        /**
         * @param value {@link #therapyRelationshipType} (The type of relationship between the medicinal product indication or contraindication and another therapy.)
         */
        public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent setTherapyRelationshipType(CodeableConcept value) { 
          this.therapyRelationshipType = value;
          return this;
        }

        /**
         * @return {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public Type getMedication() { 
          return this.medication;
        }

        /**
         * @return {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public CodeableConcept getMedicationCodeableConcept() throws FHIRException { 
          if (this.medication == null)
            return null;
          if (!(this.medication instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (CodeableConcept) this.medication;
        }

        public boolean hasMedicationCodeableConcept() { 
          return this != null && this.medication instanceof CodeableConcept;
        }

        /**
         * @return {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public Reference getMedicationReference() throws FHIRException { 
          if (this.medication == null)
            return null;
          if (!(this.medication instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (Reference) this.medication;
        }

        public boolean hasMedicationReference() { 
          return this != null && this.medication instanceof Reference;
        }

        public boolean hasMedication() { 
          return this.medication != null && !this.medication.isEmpty();
        }

        /**
         * @param value {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent setMedication(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for MedicinalProductClinicals.therapeuticIndication.otherTherapy.medication[x]: "+value.fhirType());
          this.medication = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("therapyRelationshipType", "CodeableConcept", "The type of relationship between the medicinal product indication or contraindication and another therapy.", 0, 1, therapyRelationshipType));
          children.add(new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -551658469: /*therapyRelationshipType*/  return new Property("therapyRelationshipType", "CodeableConcept", "The type of relationship between the medicinal product indication or contraindication and another therapy.", 0, 1, therapyRelationshipType);
          case 1458402129: /*medication[x]*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          case 1998965455: /*medication*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          case -209845038: /*medicationCodeableConcept*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          case 2104315196: /*medicationReference*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -551658469: /*therapyRelationshipType*/ return this.therapyRelationshipType == null ? new Base[0] : new Base[] {this.therapyRelationshipType}; // CodeableConcept
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -551658469: // therapyRelationshipType
          this.therapyRelationshipType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1998965455: // medication
          this.medication = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("therapyRelationshipType")) {
          this.therapyRelationshipType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("medication[x]")) {
          this.medication = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -551658469:  return getTherapyRelationshipType(); 
        case 1458402129:  return getMedication(); 
        case 1998965455:  return getMedication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -551658469: /*therapyRelationshipType*/ return new String[] {"CodeableConcept"};
        case 1998965455: /*medication*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("therapyRelationshipType")) {
          this.therapyRelationshipType = new CodeableConcept();
          return this.therapyRelationshipType;
        }
        else if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent copy() {
        MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent dst = new MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent();
        copyValues(dst);
        dst.therapyRelationshipType = therapyRelationshipType == null ? null : therapyRelationshipType.copy();
        dst.medication = medication == null ? null : medication.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent))
          return false;
        MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent o = (MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent) other_;
        return compareDeep(therapyRelationshipType, o.therapyRelationshipType, true) && compareDeep(medication, o.medication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent))
          return false;
        MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent o = (MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(therapyRelationshipType, medication
          );
      }

  public String fhirType() {
    return "MedicinalProductClinicals.therapeuticIndication.otherTherapy";

  }

  }

    @Block()
    public static class MedicinalProductClinicalsContraindicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The disease, symptom or procedure for the contraindication.
         */
        @Child(name = "disease", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The disease, symptom or procedure for the contraindication", formalDefinition="The disease, symptom or procedure for the contraindication." )
        protected CodeableConcept disease;

        /**
         * The status of the disease or symptom for the contraindication.
         */
        @Child(name = "diseaseStatus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The status of the disease or symptom for the contraindication", formalDefinition="The status of the disease or symptom for the contraindication." )
        protected CodeableConcept diseaseStatus;

        /**
         * A comorbidity (concurrent condition) or coinfection.
         */
        @Child(name = "comorbidity", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A comorbidity (concurrent condition) or coinfection", formalDefinition="A comorbidity (concurrent condition) or coinfection." )
        protected List<CodeableConcept> comorbidity;

        /**
         * Information about the use of the medicinal product in relation to other therapies as part of the indication.
         */
        @Child(name = "therapeuticIndication", type = {MedicinalProductClinicalsTherapeuticIndicationComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies as part of the indication", formalDefinition="Information about the use of the medicinal product in relation to other therapies as part of the indication." )
        protected List<MedicinalProductClinicalsTherapeuticIndicationComponent> therapeuticIndication;

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the contraindication.
         */
        @Child(name = "otherTherapy", type = {MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the contraindication", formalDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the contraindication." )
        protected List<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent> otherTherapy;

        /**
         * The population group to which this applies.
         */
        @Child(name = "population", type = {MedicinalProductClinicalsUndesirableEffectsPopulationComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The population group to which this applies", formalDefinition="The population group to which this applies." )
        protected List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> population;

        private static final long serialVersionUID = -328954718L;

    /**
     * Constructor
     */
      public MedicinalProductClinicalsContraindicationComponent() {
        super();
      }

        /**
         * @return {@link #disease} (The disease, symptom or procedure for the contraindication.)
         */
        public CodeableConcept getDisease() { 
          if (this.disease == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsContraindicationComponent.disease");
            else if (Configuration.doAutoCreate())
              this.disease = new CodeableConcept(); // cc
          return this.disease;
        }

        public boolean hasDisease() { 
          return this.disease != null && !this.disease.isEmpty();
        }

        /**
         * @param value {@link #disease} (The disease, symptom or procedure for the contraindication.)
         */
        public MedicinalProductClinicalsContraindicationComponent setDisease(CodeableConcept value) { 
          this.disease = value;
          return this;
        }

        /**
         * @return {@link #diseaseStatus} (The status of the disease or symptom for the contraindication.)
         */
        public CodeableConcept getDiseaseStatus() { 
          if (this.diseaseStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsContraindicationComponent.diseaseStatus");
            else if (Configuration.doAutoCreate())
              this.diseaseStatus = new CodeableConcept(); // cc
          return this.diseaseStatus;
        }

        public boolean hasDiseaseStatus() { 
          return this.diseaseStatus != null && !this.diseaseStatus.isEmpty();
        }

        /**
         * @param value {@link #diseaseStatus} (The status of the disease or symptom for the contraindication.)
         */
        public MedicinalProductClinicalsContraindicationComponent setDiseaseStatus(CodeableConcept value) { 
          this.diseaseStatus = value;
          return this;
        }

        /**
         * @return {@link #comorbidity} (A comorbidity (concurrent condition) or coinfection.)
         */
        public List<CodeableConcept> getComorbidity() { 
          if (this.comorbidity == null)
            this.comorbidity = new ArrayList<CodeableConcept>();
          return this.comorbidity;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsContraindicationComponent setComorbidity(List<CodeableConcept> theComorbidity) { 
          this.comorbidity = theComorbidity;
          return this;
        }

        public boolean hasComorbidity() { 
          if (this.comorbidity == null)
            return false;
          for (CodeableConcept item : this.comorbidity)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addComorbidity() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.comorbidity == null)
            this.comorbidity = new ArrayList<CodeableConcept>();
          this.comorbidity.add(t);
          return t;
        }

        public MedicinalProductClinicalsContraindicationComponent addComorbidity(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.comorbidity == null)
            this.comorbidity = new ArrayList<CodeableConcept>();
          this.comorbidity.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #comorbidity}, creating it if it does not already exist
         */
        public CodeableConcept getComorbidityFirstRep() { 
          if (getComorbidity().isEmpty()) {
            addComorbidity();
          }
          return getComorbidity().get(0);
        }

        /**
         * @return {@link #therapeuticIndication} (Information about the use of the medicinal product in relation to other therapies as part of the indication.)
         */
        public List<MedicinalProductClinicalsTherapeuticIndicationComponent> getTherapeuticIndication() { 
          if (this.therapeuticIndication == null)
            this.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
          return this.therapeuticIndication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsContraindicationComponent setTherapeuticIndication(List<MedicinalProductClinicalsTherapeuticIndicationComponent> theTherapeuticIndication) { 
          this.therapeuticIndication = theTherapeuticIndication;
          return this;
        }

        public boolean hasTherapeuticIndication() { 
          if (this.therapeuticIndication == null)
            return false;
          for (MedicinalProductClinicalsTherapeuticIndicationComponent item : this.therapeuticIndication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsTherapeuticIndicationComponent addTherapeuticIndication() { //3
          MedicinalProductClinicalsTherapeuticIndicationComponent t = new MedicinalProductClinicalsTherapeuticIndicationComponent();
          if (this.therapeuticIndication == null)
            this.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
          this.therapeuticIndication.add(t);
          return t;
        }

        public MedicinalProductClinicalsContraindicationComponent addTherapeuticIndication(MedicinalProductClinicalsTherapeuticIndicationComponent t) { //3
          if (t == null)
            return this;
          if (this.therapeuticIndication == null)
            this.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
          this.therapeuticIndication.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #therapeuticIndication}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsTherapeuticIndicationComponent getTherapeuticIndicationFirstRep() { 
          if (getTherapeuticIndication().isEmpty()) {
            addTherapeuticIndication();
          }
          return getTherapeuticIndication().get(0);
        }

        /**
         * @return {@link #otherTherapy} (Information about the use of the medicinal product in relation to other therapies described as part of the contraindication.)
         */
        public List<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent> getOtherTherapy() { 
          if (this.otherTherapy == null)
            this.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          return this.otherTherapy;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsContraindicationComponent setOtherTherapy(List<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent> theOtherTherapy) { 
          this.otherTherapy = theOtherTherapy;
          return this;
        }

        public boolean hasOtherTherapy() { 
          if (this.otherTherapy == null)
            return false;
          for (MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent item : this.otherTherapy)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent addOtherTherapy() { //3
          MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent t = new MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent();
          if (this.otherTherapy == null)
            this.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          this.otherTherapy.add(t);
          return t;
        }

        public MedicinalProductClinicalsContraindicationComponent addOtherTherapy(MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent t) { //3
          if (t == null)
            return this;
          if (this.otherTherapy == null)
            this.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          this.otherTherapy.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #otherTherapy}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent getOtherTherapyFirstRep() { 
          if (getOtherTherapy().isEmpty()) {
            addOtherTherapy();
          }
          return getOtherTherapy().get(0);
        }

        /**
         * @return {@link #population} (The population group to which this applies.)
         */
        public List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> getPopulation() { 
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          return this.population;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsContraindicationComponent setPopulation(List<MedicinalProductClinicalsUndesirableEffectsPopulationComponent> thePopulation) { 
          this.population = thePopulation;
          return this;
        }

        public boolean hasPopulation() { 
          if (this.population == null)
            return false;
          for (MedicinalProductClinicalsUndesirableEffectsPopulationComponent item : this.population)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent addPopulation() { //3
          MedicinalProductClinicalsUndesirableEffectsPopulationComponent t = new MedicinalProductClinicalsUndesirableEffectsPopulationComponent();
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          this.population.add(t);
          return t;
        }

        public MedicinalProductClinicalsContraindicationComponent addPopulation(MedicinalProductClinicalsUndesirableEffectsPopulationComponent t) { //3
          if (t == null)
            return this;
          if (this.population == null)
            this.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          this.population.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
         */
        public MedicinalProductClinicalsUndesirableEffectsPopulationComponent getPopulationFirstRep() { 
          if (getPopulation().isEmpty()) {
            addPopulation();
          }
          return getPopulation().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("disease", "CodeableConcept", "The disease, symptom or procedure for the contraindication.", 0, 1, disease));
          children.add(new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for the contraindication.", 0, 1, diseaseStatus));
          children.add(new Property("comorbidity", "CodeableConcept", "A comorbidity (concurrent condition) or coinfection.", 0, java.lang.Integer.MAX_VALUE, comorbidity));
          children.add(new Property("therapeuticIndication", "@MedicinalProductClinicals.therapeuticIndication", "Information about the use of the medicinal product in relation to other therapies as part of the indication.", 0, java.lang.Integer.MAX_VALUE, therapeuticIndication));
          children.add(new Property("otherTherapy", "@MedicinalProductClinicals.therapeuticIndication.otherTherapy", "Information about the use of the medicinal product in relation to other therapies described as part of the contraindication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy));
          children.add(new Property("population", "@MedicinalProductClinicals.undesirableEffects.population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1671426428: /*disease*/  return new Property("disease", "CodeableConcept", "The disease, symptom or procedure for the contraindication.", 0, 1, disease);
          case -505503602: /*diseaseStatus*/  return new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for the contraindication.", 0, 1, diseaseStatus);
          case -406395211: /*comorbidity*/  return new Property("comorbidity", "CodeableConcept", "A comorbidity (concurrent condition) or coinfection.", 0, java.lang.Integer.MAX_VALUE, comorbidity);
          case -1925150262: /*therapeuticIndication*/  return new Property("therapeuticIndication", "@MedicinalProductClinicals.therapeuticIndication", "Information about the use of the medicinal product in relation to other therapies as part of the indication.", 0, java.lang.Integer.MAX_VALUE, therapeuticIndication);
          case -544509127: /*otherTherapy*/  return new Property("otherTherapy", "@MedicinalProductClinicals.therapeuticIndication.otherTherapy", "Information about the use of the medicinal product in relation to other therapies described as part of the contraindication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy);
          case -2023558323: /*population*/  return new Property("population", "@MedicinalProductClinicals.undesirableEffects.population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1671426428: /*disease*/ return this.disease == null ? new Base[0] : new Base[] {this.disease}; // CodeableConcept
        case -505503602: /*diseaseStatus*/ return this.diseaseStatus == null ? new Base[0] : new Base[] {this.diseaseStatus}; // CodeableConcept
        case -406395211: /*comorbidity*/ return this.comorbidity == null ? new Base[0] : this.comorbidity.toArray(new Base[this.comorbidity.size()]); // CodeableConcept
        case -1925150262: /*therapeuticIndication*/ return this.therapeuticIndication == null ? new Base[0] : this.therapeuticIndication.toArray(new Base[this.therapeuticIndication.size()]); // MedicinalProductClinicalsTherapeuticIndicationComponent
        case -544509127: /*otherTherapy*/ return this.otherTherapy == null ? new Base[0] : this.otherTherapy.toArray(new Base[this.otherTherapy.size()]); // MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MedicinalProductClinicalsUndesirableEffectsPopulationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1671426428: // disease
          this.disease = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -505503602: // diseaseStatus
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -406395211: // comorbidity
          this.getComorbidity().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1925150262: // therapeuticIndication
          this.getTherapeuticIndication().add((MedicinalProductClinicalsTherapeuticIndicationComponent) value); // MedicinalProductClinicalsTherapeuticIndicationComponent
          return value;
        case -544509127: // otherTherapy
          this.getOtherTherapy().add((MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent) value); // MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent
          return value;
        case -2023558323: // population
          this.getPopulation().add((MedicinalProductClinicalsUndesirableEffectsPopulationComponent) value); // MedicinalProductClinicalsUndesirableEffectsPopulationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("disease")) {
          this.disease = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("comorbidity")) {
          this.getComorbidity().add(castToCodeableConcept(value));
        } else if (name.equals("therapeuticIndication")) {
          this.getTherapeuticIndication().add((MedicinalProductClinicalsTherapeuticIndicationComponent) value);
        } else if (name.equals("otherTherapy")) {
          this.getOtherTherapy().add((MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent) value);
        } else if (name.equals("population")) {
          this.getPopulation().add((MedicinalProductClinicalsUndesirableEffectsPopulationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1671426428:  return getDisease(); 
        case -505503602:  return getDiseaseStatus(); 
        case -406395211:  return addComorbidity(); 
        case -1925150262:  return addTherapeuticIndication(); 
        case -544509127:  return addOtherTherapy(); 
        case -2023558323:  return addPopulation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1671426428: /*disease*/ return new String[] {"CodeableConcept"};
        case -505503602: /*diseaseStatus*/ return new String[] {"CodeableConcept"};
        case -406395211: /*comorbidity*/ return new String[] {"CodeableConcept"};
        case -1925150262: /*therapeuticIndication*/ return new String[] {"@MedicinalProductClinicals.therapeuticIndication"};
        case -544509127: /*otherTherapy*/ return new String[] {"@MedicinalProductClinicals.therapeuticIndication.otherTherapy"};
        case -2023558323: /*population*/ return new String[] {"@MedicinalProductClinicals.undesirableEffects.population"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("disease")) {
          this.disease = new CodeableConcept();
          return this.disease;
        }
        else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = new CodeableConcept();
          return this.diseaseStatus;
        }
        else if (name.equals("comorbidity")) {
          return addComorbidity();
        }
        else if (name.equals("therapeuticIndication")) {
          return addTherapeuticIndication();
        }
        else if (name.equals("otherTherapy")) {
          return addOtherTherapy();
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductClinicalsContraindicationComponent copy() {
        MedicinalProductClinicalsContraindicationComponent dst = new MedicinalProductClinicalsContraindicationComponent();
        copyValues(dst);
        dst.disease = disease == null ? null : disease.copy();
        dst.diseaseStatus = diseaseStatus == null ? null : diseaseStatus.copy();
        if (comorbidity != null) {
          dst.comorbidity = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : comorbidity)
            dst.comorbidity.add(i.copy());
        };
        if (therapeuticIndication != null) {
          dst.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
          for (MedicinalProductClinicalsTherapeuticIndicationComponent i : therapeuticIndication)
            dst.therapeuticIndication.add(i.copy());
        };
        if (otherTherapy != null) {
          dst.otherTherapy = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent>();
          for (MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent i : otherTherapy)
            dst.otherTherapy.add(i.copy());
        };
        if (population != null) {
          dst.population = new ArrayList<MedicinalProductClinicalsUndesirableEffectsPopulationComponent>();
          for (MedicinalProductClinicalsUndesirableEffectsPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsContraindicationComponent))
          return false;
        MedicinalProductClinicalsContraindicationComponent o = (MedicinalProductClinicalsContraindicationComponent) other_;
        return compareDeep(disease, o.disease, true) && compareDeep(diseaseStatus, o.diseaseStatus, true)
           && compareDeep(comorbidity, o.comorbidity, true) && compareDeep(therapeuticIndication, o.therapeuticIndication, true)
           && compareDeep(otherTherapy, o.otherTherapy, true) && compareDeep(population, o.population, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsContraindicationComponent))
          return false;
        MedicinalProductClinicalsContraindicationComponent o = (MedicinalProductClinicalsContraindicationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(disease, diseaseStatus, comorbidity
          , therapeuticIndication, otherTherapy, population);
      }

  public String fhirType() {
    return "MedicinalProductClinicals.contraindication";

  }

  }

    @Block()
    public static class MedicinalProductClinicalsInteractionsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The interaction described.
         */
        @Child(name = "interaction", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The interaction described", formalDefinition="The interaction described." )
        protected StringType interaction;

        /**
         * The specific medication, food or laboratory test that interacts.
         */
        @Child(name = "interactant", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The specific medication, food or laboratory test that interacts", formalDefinition="The specific medication, food or laboratory test that interacts." )
        protected List<CodeableConcept> interactant;

        /**
         * The type of the interaction.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of the interaction", formalDefinition="The type of the interaction." )
        protected CodeableConcept type;

        /**
         * The effect of the interaction.
         */
        @Child(name = "effect", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The effect of the interaction", formalDefinition="The effect of the interaction." )
        protected CodeableConcept effect;

        /**
         * The incidence of the interaction.
         */
        @Child(name = "incidence", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The incidence of the interaction", formalDefinition="The incidence of the interaction." )
        protected CodeableConcept incidence;

        /**
         * Actions for managing the interaction.
         */
        @Child(name = "management", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Actions for managing the interaction", formalDefinition="Actions for managing the interaction." )
        protected CodeableConcept management;

        private static final long serialVersionUID = -1375751505L;

    /**
     * Constructor
     */
      public MedicinalProductClinicalsInteractionsComponent() {
        super();
      }

        /**
         * @return {@link #interaction} (The interaction described.). This is the underlying object with id, value and extensions. The accessor "getInteraction" gives direct access to the value
         */
        public StringType getInteractionElement() { 
          if (this.interaction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsInteractionsComponent.interaction");
            else if (Configuration.doAutoCreate())
              this.interaction = new StringType(); // bb
          return this.interaction;
        }

        public boolean hasInteractionElement() { 
          return this.interaction != null && !this.interaction.isEmpty();
        }

        public boolean hasInteraction() { 
          return this.interaction != null && !this.interaction.isEmpty();
        }

        /**
         * @param value {@link #interaction} (The interaction described.). This is the underlying object with id, value and extensions. The accessor "getInteraction" gives direct access to the value
         */
        public MedicinalProductClinicalsInteractionsComponent setInteractionElement(StringType value) { 
          this.interaction = value;
          return this;
        }

        /**
         * @return The interaction described.
         */
        public String getInteraction() { 
          return this.interaction == null ? null : this.interaction.getValue();
        }

        /**
         * @param value The interaction described.
         */
        public MedicinalProductClinicalsInteractionsComponent setInteraction(String value) { 
          if (Utilities.noString(value))
            this.interaction = null;
          else {
            if (this.interaction == null)
              this.interaction = new StringType();
            this.interaction.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #interactant} (The specific medication, food or laboratory test that interacts.)
         */
        public List<CodeableConcept> getInteractant() { 
          if (this.interactant == null)
            this.interactant = new ArrayList<CodeableConcept>();
          return this.interactant;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductClinicalsInteractionsComponent setInteractant(List<CodeableConcept> theInteractant) { 
          this.interactant = theInteractant;
          return this;
        }

        public boolean hasInteractant() { 
          if (this.interactant == null)
            return false;
          for (CodeableConcept item : this.interactant)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addInteractant() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.interactant == null)
            this.interactant = new ArrayList<CodeableConcept>();
          this.interactant.add(t);
          return t;
        }

        public MedicinalProductClinicalsInteractionsComponent addInteractant(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.interactant == null)
            this.interactant = new ArrayList<CodeableConcept>();
          this.interactant.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #interactant}, creating it if it does not already exist
         */
        public CodeableConcept getInteractantFirstRep() { 
          if (getInteractant().isEmpty()) {
            addInteractant();
          }
          return getInteractant().get(0);
        }

        /**
         * @return {@link #type} (The type of the interaction.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsInteractionsComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the interaction.)
         */
        public MedicinalProductClinicalsInteractionsComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #effect} (The effect of the interaction.)
         */
        public CodeableConcept getEffect() { 
          if (this.effect == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsInteractionsComponent.effect");
            else if (Configuration.doAutoCreate())
              this.effect = new CodeableConcept(); // cc
          return this.effect;
        }

        public boolean hasEffect() { 
          return this.effect != null && !this.effect.isEmpty();
        }

        /**
         * @param value {@link #effect} (The effect of the interaction.)
         */
        public MedicinalProductClinicalsInteractionsComponent setEffect(CodeableConcept value) { 
          this.effect = value;
          return this;
        }

        /**
         * @return {@link #incidence} (The incidence of the interaction.)
         */
        public CodeableConcept getIncidence() { 
          if (this.incidence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsInteractionsComponent.incidence");
            else if (Configuration.doAutoCreate())
              this.incidence = new CodeableConcept(); // cc
          return this.incidence;
        }

        public boolean hasIncidence() { 
          return this.incidence != null && !this.incidence.isEmpty();
        }

        /**
         * @param value {@link #incidence} (The incidence of the interaction.)
         */
        public MedicinalProductClinicalsInteractionsComponent setIncidence(CodeableConcept value) { 
          this.incidence = value;
          return this;
        }

        /**
         * @return {@link #management} (Actions for managing the interaction.)
         */
        public CodeableConcept getManagement() { 
          if (this.management == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductClinicalsInteractionsComponent.management");
            else if (Configuration.doAutoCreate())
              this.management = new CodeableConcept(); // cc
          return this.management;
        }

        public boolean hasManagement() { 
          return this.management != null && !this.management.isEmpty();
        }

        /**
         * @param value {@link #management} (Actions for managing the interaction.)
         */
        public MedicinalProductClinicalsInteractionsComponent setManagement(CodeableConcept value) { 
          this.management = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("interaction", "string", "The interaction described.", 0, 1, interaction));
          children.add(new Property("interactant", "CodeableConcept", "The specific medication, food or laboratory test that interacts.", 0, java.lang.Integer.MAX_VALUE, interactant));
          children.add(new Property("type", "CodeableConcept", "The type of the interaction.", 0, 1, type));
          children.add(new Property("effect", "CodeableConcept", "The effect of the interaction.", 0, 1, effect));
          children.add(new Property("incidence", "CodeableConcept", "The incidence of the interaction.", 0, 1, incidence));
          children.add(new Property("management", "CodeableConcept", "Actions for managing the interaction.", 0, 1, management));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1844104722: /*interaction*/  return new Property("interaction", "string", "The interaction described.", 0, 1, interaction);
          case 1844097009: /*interactant*/  return new Property("interactant", "CodeableConcept", "The specific medication, food or laboratory test that interacts.", 0, java.lang.Integer.MAX_VALUE, interactant);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of the interaction.", 0, 1, type);
          case -1306084975: /*effect*/  return new Property("effect", "CodeableConcept", "The effect of the interaction.", 0, 1, effect);
          case -1598467132: /*incidence*/  return new Property("incidence", "CodeableConcept", "The incidence of the interaction.", 0, 1, incidence);
          case -1799980989: /*management*/  return new Property("management", "CodeableConcept", "Actions for managing the interaction.", 0, 1, management);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1844104722: /*interaction*/ return this.interaction == null ? new Base[0] : new Base[] {this.interaction}; // StringType
        case 1844097009: /*interactant*/ return this.interactant == null ? new Base[0] : this.interactant.toArray(new Base[this.interactant.size()]); // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1306084975: /*effect*/ return this.effect == null ? new Base[0] : new Base[] {this.effect}; // CodeableConcept
        case -1598467132: /*incidence*/ return this.incidence == null ? new Base[0] : new Base[] {this.incidence}; // CodeableConcept
        case -1799980989: /*management*/ return this.management == null ? new Base[0] : new Base[] {this.management}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1844104722: // interaction
          this.interaction = castToString(value); // StringType
          return value;
        case 1844097009: // interactant
          this.getInteractant().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1306084975: // effect
          this.effect = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1598467132: // incidence
          this.incidence = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1799980989: // management
          this.management = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("interaction")) {
          this.interaction = castToString(value); // StringType
        } else if (name.equals("interactant")) {
          this.getInteractant().add(castToCodeableConcept(value));
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("effect")) {
          this.effect = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("incidence")) {
          this.incidence = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("management")) {
          this.management = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1844104722:  return getInteractionElement();
        case 1844097009:  return addInteractant(); 
        case 3575610:  return getType(); 
        case -1306084975:  return getEffect(); 
        case -1598467132:  return getIncidence(); 
        case -1799980989:  return getManagement(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1844104722: /*interaction*/ return new String[] {"string"};
        case 1844097009: /*interactant*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1306084975: /*effect*/ return new String[] {"CodeableConcept"};
        case -1598467132: /*incidence*/ return new String[] {"CodeableConcept"};
        case -1799980989: /*management*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("interaction")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductClinicals.interaction");
        }
        else if (name.equals("interactant")) {
          return addInteractant();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("effect")) {
          this.effect = new CodeableConcept();
          return this.effect;
        }
        else if (name.equals("incidence")) {
          this.incidence = new CodeableConcept();
          return this.incidence;
        }
        else if (name.equals("management")) {
          this.management = new CodeableConcept();
          return this.management;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductClinicalsInteractionsComponent copy() {
        MedicinalProductClinicalsInteractionsComponent dst = new MedicinalProductClinicalsInteractionsComponent();
        copyValues(dst);
        dst.interaction = interaction == null ? null : interaction.copy();
        if (interactant != null) {
          dst.interactant = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : interactant)
            dst.interactant.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.effect = effect == null ? null : effect.copy();
        dst.incidence = incidence == null ? null : incidence.copy();
        dst.management = management == null ? null : management.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsInteractionsComponent))
          return false;
        MedicinalProductClinicalsInteractionsComponent o = (MedicinalProductClinicalsInteractionsComponent) other_;
        return compareDeep(interaction, o.interaction, true) && compareDeep(interactant, o.interactant, true)
           && compareDeep(type, o.type, true) && compareDeep(effect, o.effect, true) && compareDeep(incidence, o.incidence, true)
           && compareDeep(management, o.management, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicalsInteractionsComponent))
          return false;
        MedicinalProductClinicalsInteractionsComponent o = (MedicinalProductClinicalsInteractionsComponent) other_;
        return compareValues(interaction, o.interaction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(interaction, interactant, type
          , effect, incidence, management);
      }

  public String fhirType() {
    return "MedicinalProductClinicals.interactions";

  }

  }

    /**
     * Describe the undesirable effects of the medicinal product.
     */
    @Child(name = "undesirableEffects", type = {}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Describe the undesirable effects of the medicinal product", formalDefinition="Describe the undesirable effects of the medicinal product." )
    protected List<MedicinalProductClinicalsUndesirableEffectsComponent> undesirableEffects;

    /**
     * Indication for the Medicinal Product.
     */
    @Child(name = "therapeuticIndication", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Indication for the Medicinal Product", formalDefinition="Indication for the Medicinal Product." )
    protected List<MedicinalProductClinicalsTherapeuticIndicationComponent> therapeuticIndication;

    /**
     * Contraindication for the medicinal product.
     */
    @Child(name = "contraindication", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contraindication for the medicinal product", formalDefinition="Contraindication for the medicinal product." )
    protected List<MedicinalProductClinicalsContraindicationComponent> contraindication;

    /**
     * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
     */
    @Child(name = "interactions", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The interactions of the medicinal product with other medicinal products, or other forms of interactions", formalDefinition="The interactions of the medicinal product with other medicinal products, or other forms of interactions." )
    protected List<MedicinalProductClinicalsInteractionsComponent> interactions;

    private static final long serialVersionUID = 544343962L;

  /**
   * Constructor
   */
    public MedicinalProductClinicals() {
      super();
    }

    /**
     * @return {@link #undesirableEffects} (Describe the undesirable effects of the medicinal product.)
     */
    public List<MedicinalProductClinicalsUndesirableEffectsComponent> getUndesirableEffects() { 
      if (this.undesirableEffects == null)
        this.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
      return this.undesirableEffects;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductClinicals setUndesirableEffects(List<MedicinalProductClinicalsUndesirableEffectsComponent> theUndesirableEffects) { 
      this.undesirableEffects = theUndesirableEffects;
      return this;
    }

    public boolean hasUndesirableEffects() { 
      if (this.undesirableEffects == null)
        return false;
      for (MedicinalProductClinicalsUndesirableEffectsComponent item : this.undesirableEffects)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductClinicalsUndesirableEffectsComponent addUndesirableEffects() { //3
      MedicinalProductClinicalsUndesirableEffectsComponent t = new MedicinalProductClinicalsUndesirableEffectsComponent();
      if (this.undesirableEffects == null)
        this.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
      this.undesirableEffects.add(t);
      return t;
    }

    public MedicinalProductClinicals addUndesirableEffects(MedicinalProductClinicalsUndesirableEffectsComponent t) { //3
      if (t == null)
        return this;
      if (this.undesirableEffects == null)
        this.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
      this.undesirableEffects.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #undesirableEffects}, creating it if it does not already exist
     */
    public MedicinalProductClinicalsUndesirableEffectsComponent getUndesirableEffectsFirstRep() { 
      if (getUndesirableEffects().isEmpty()) {
        addUndesirableEffects();
      }
      return getUndesirableEffects().get(0);
    }

    /**
     * @return {@link #therapeuticIndication} (Indication for the Medicinal Product.)
     */
    public List<MedicinalProductClinicalsTherapeuticIndicationComponent> getTherapeuticIndication() { 
      if (this.therapeuticIndication == null)
        this.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
      return this.therapeuticIndication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductClinicals setTherapeuticIndication(List<MedicinalProductClinicalsTherapeuticIndicationComponent> theTherapeuticIndication) { 
      this.therapeuticIndication = theTherapeuticIndication;
      return this;
    }

    public boolean hasTherapeuticIndication() { 
      if (this.therapeuticIndication == null)
        return false;
      for (MedicinalProductClinicalsTherapeuticIndicationComponent item : this.therapeuticIndication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductClinicalsTherapeuticIndicationComponent addTherapeuticIndication() { //3
      MedicinalProductClinicalsTherapeuticIndicationComponent t = new MedicinalProductClinicalsTherapeuticIndicationComponent();
      if (this.therapeuticIndication == null)
        this.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
      this.therapeuticIndication.add(t);
      return t;
    }

    public MedicinalProductClinicals addTherapeuticIndication(MedicinalProductClinicalsTherapeuticIndicationComponent t) { //3
      if (t == null)
        return this;
      if (this.therapeuticIndication == null)
        this.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
      this.therapeuticIndication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #therapeuticIndication}, creating it if it does not already exist
     */
    public MedicinalProductClinicalsTherapeuticIndicationComponent getTherapeuticIndicationFirstRep() { 
      if (getTherapeuticIndication().isEmpty()) {
        addTherapeuticIndication();
      }
      return getTherapeuticIndication().get(0);
    }

    /**
     * @return {@link #contraindication} (Contraindication for the medicinal product.)
     */
    public List<MedicinalProductClinicalsContraindicationComponent> getContraindication() { 
      if (this.contraindication == null)
        this.contraindication = new ArrayList<MedicinalProductClinicalsContraindicationComponent>();
      return this.contraindication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductClinicals setContraindication(List<MedicinalProductClinicalsContraindicationComponent> theContraindication) { 
      this.contraindication = theContraindication;
      return this;
    }

    public boolean hasContraindication() { 
      if (this.contraindication == null)
        return false;
      for (MedicinalProductClinicalsContraindicationComponent item : this.contraindication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductClinicalsContraindicationComponent addContraindication() { //3
      MedicinalProductClinicalsContraindicationComponent t = new MedicinalProductClinicalsContraindicationComponent();
      if (this.contraindication == null)
        this.contraindication = new ArrayList<MedicinalProductClinicalsContraindicationComponent>();
      this.contraindication.add(t);
      return t;
    }

    public MedicinalProductClinicals addContraindication(MedicinalProductClinicalsContraindicationComponent t) { //3
      if (t == null)
        return this;
      if (this.contraindication == null)
        this.contraindication = new ArrayList<MedicinalProductClinicalsContraindicationComponent>();
      this.contraindication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contraindication}, creating it if it does not already exist
     */
    public MedicinalProductClinicalsContraindicationComponent getContraindicationFirstRep() { 
      if (getContraindication().isEmpty()) {
        addContraindication();
      }
      return getContraindication().get(0);
    }

    /**
     * @return {@link #interactions} (The interactions of the medicinal product with other medicinal products, or other forms of interactions.)
     */
    public List<MedicinalProductClinicalsInteractionsComponent> getInteractions() { 
      if (this.interactions == null)
        this.interactions = new ArrayList<MedicinalProductClinicalsInteractionsComponent>();
      return this.interactions;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductClinicals setInteractions(List<MedicinalProductClinicalsInteractionsComponent> theInteractions) { 
      this.interactions = theInteractions;
      return this;
    }

    public boolean hasInteractions() { 
      if (this.interactions == null)
        return false;
      for (MedicinalProductClinicalsInteractionsComponent item : this.interactions)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductClinicalsInteractionsComponent addInteractions() { //3
      MedicinalProductClinicalsInteractionsComponent t = new MedicinalProductClinicalsInteractionsComponent();
      if (this.interactions == null)
        this.interactions = new ArrayList<MedicinalProductClinicalsInteractionsComponent>();
      this.interactions.add(t);
      return t;
    }

    public MedicinalProductClinicals addInteractions(MedicinalProductClinicalsInteractionsComponent t) { //3
      if (t == null)
        return this;
      if (this.interactions == null)
        this.interactions = new ArrayList<MedicinalProductClinicalsInteractionsComponent>();
      this.interactions.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #interactions}, creating it if it does not already exist
     */
    public MedicinalProductClinicalsInteractionsComponent getInteractionsFirstRep() { 
      if (getInteractions().isEmpty()) {
        addInteractions();
      }
      return getInteractions().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("undesirableEffects", "", "Describe the undesirable effects of the medicinal product.", 0, java.lang.Integer.MAX_VALUE, undesirableEffects));
        children.add(new Property("therapeuticIndication", "", "Indication for the Medicinal Product.", 0, java.lang.Integer.MAX_VALUE, therapeuticIndication));
        children.add(new Property("contraindication", "", "Contraindication for the medicinal product.", 0, java.lang.Integer.MAX_VALUE, contraindication));
        children.add(new Property("interactions", "", "The interactions of the medicinal product with other medicinal products, or other forms of interactions.", 0, java.lang.Integer.MAX_VALUE, interactions));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 890492742: /*undesirableEffects*/  return new Property("undesirableEffects", "", "Describe the undesirable effects of the medicinal product.", 0, java.lang.Integer.MAX_VALUE, undesirableEffects);
        case -1925150262: /*therapeuticIndication*/  return new Property("therapeuticIndication", "", "Indication for the Medicinal Product.", 0, java.lang.Integer.MAX_VALUE, therapeuticIndication);
        case 107135229: /*contraindication*/  return new Property("contraindication", "", "Contraindication for the medicinal product.", 0, java.lang.Integer.MAX_VALUE, contraindication);
        case 1332671649: /*interactions*/  return new Property("interactions", "", "The interactions of the medicinal product with other medicinal products, or other forms of interactions.", 0, java.lang.Integer.MAX_VALUE, interactions);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 890492742: /*undesirableEffects*/ return this.undesirableEffects == null ? new Base[0] : this.undesirableEffects.toArray(new Base[this.undesirableEffects.size()]); // MedicinalProductClinicalsUndesirableEffectsComponent
        case -1925150262: /*therapeuticIndication*/ return this.therapeuticIndication == null ? new Base[0] : this.therapeuticIndication.toArray(new Base[this.therapeuticIndication.size()]); // MedicinalProductClinicalsTherapeuticIndicationComponent
        case 107135229: /*contraindication*/ return this.contraindication == null ? new Base[0] : this.contraindication.toArray(new Base[this.contraindication.size()]); // MedicinalProductClinicalsContraindicationComponent
        case 1332671649: /*interactions*/ return this.interactions == null ? new Base[0] : this.interactions.toArray(new Base[this.interactions.size()]); // MedicinalProductClinicalsInteractionsComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 890492742: // undesirableEffects
          this.getUndesirableEffects().add((MedicinalProductClinicalsUndesirableEffectsComponent) value); // MedicinalProductClinicalsUndesirableEffectsComponent
          return value;
        case -1925150262: // therapeuticIndication
          this.getTherapeuticIndication().add((MedicinalProductClinicalsTherapeuticIndicationComponent) value); // MedicinalProductClinicalsTherapeuticIndicationComponent
          return value;
        case 107135229: // contraindication
          this.getContraindication().add((MedicinalProductClinicalsContraindicationComponent) value); // MedicinalProductClinicalsContraindicationComponent
          return value;
        case 1332671649: // interactions
          this.getInteractions().add((MedicinalProductClinicalsInteractionsComponent) value); // MedicinalProductClinicalsInteractionsComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("undesirableEffects")) {
          this.getUndesirableEffects().add((MedicinalProductClinicalsUndesirableEffectsComponent) value);
        } else if (name.equals("therapeuticIndication")) {
          this.getTherapeuticIndication().add((MedicinalProductClinicalsTherapeuticIndicationComponent) value);
        } else if (name.equals("contraindication")) {
          this.getContraindication().add((MedicinalProductClinicalsContraindicationComponent) value);
        } else if (name.equals("interactions")) {
          this.getInteractions().add((MedicinalProductClinicalsInteractionsComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 890492742:  return addUndesirableEffects(); 
        case -1925150262:  return addTherapeuticIndication(); 
        case 107135229:  return addContraindication(); 
        case 1332671649:  return addInteractions(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 890492742: /*undesirableEffects*/ return new String[] {};
        case -1925150262: /*therapeuticIndication*/ return new String[] {};
        case 107135229: /*contraindication*/ return new String[] {};
        case 1332671649: /*interactions*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("undesirableEffects")) {
          return addUndesirableEffects();
        }
        else if (name.equals("therapeuticIndication")) {
          return addTherapeuticIndication();
        }
        else if (name.equals("contraindication")) {
          return addContraindication();
        }
        else if (name.equals("interactions")) {
          return addInteractions();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductClinicals";

  }

      public MedicinalProductClinicals copy() {
        MedicinalProductClinicals dst = new MedicinalProductClinicals();
        copyValues(dst);
        if (undesirableEffects != null) {
          dst.undesirableEffects = new ArrayList<MedicinalProductClinicalsUndesirableEffectsComponent>();
          for (MedicinalProductClinicalsUndesirableEffectsComponent i : undesirableEffects)
            dst.undesirableEffects.add(i.copy());
        };
        if (therapeuticIndication != null) {
          dst.therapeuticIndication = new ArrayList<MedicinalProductClinicalsTherapeuticIndicationComponent>();
          for (MedicinalProductClinicalsTherapeuticIndicationComponent i : therapeuticIndication)
            dst.therapeuticIndication.add(i.copy());
        };
        if (contraindication != null) {
          dst.contraindication = new ArrayList<MedicinalProductClinicalsContraindicationComponent>();
          for (MedicinalProductClinicalsContraindicationComponent i : contraindication)
            dst.contraindication.add(i.copy());
        };
        if (interactions != null) {
          dst.interactions = new ArrayList<MedicinalProductClinicalsInteractionsComponent>();
          for (MedicinalProductClinicalsInteractionsComponent i : interactions)
            dst.interactions.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductClinicals typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicals))
          return false;
        MedicinalProductClinicals o = (MedicinalProductClinicals) other_;
        return compareDeep(undesirableEffects, o.undesirableEffects, true) && compareDeep(therapeuticIndication, o.therapeuticIndication, true)
           && compareDeep(contraindication, o.contraindication, true) && compareDeep(interactions, o.interactions, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductClinicals))
          return false;
        MedicinalProductClinicals o = (MedicinalProductClinicals) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(undesirableEffects, therapeuticIndication
          , contraindication, interactions);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductClinicals;
   }


}

