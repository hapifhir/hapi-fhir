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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Describe the undesirable effects of the medicinal product.
 */
@ResourceDef(name="MedicinalProductUndesirableEffect", profile="http://hl7.org/fhir/StructureDefinition/MedicinalProductUndesirableEffect")
public class MedicinalProductUndesirableEffect extends DomainResource {

    @Block()
    public static class MedicinalProductUndesirableEffectPopulationComponent extends BackboneElement implements IBaseBackboneElement {
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
      public MedicinalProductUndesirableEffectPopulationComponent() {
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
        public MedicinalProductUndesirableEffectPopulationComponent setAge(Type value) { 
          if (value != null && !(value instanceof Range || value instanceof CodeableConcept))
            throw new Error("Not the right type for MedicinalProductUndesirableEffect.population.age[x]: "+value.fhirType());
          this.age = value;
          return this;
        }

        /**
         * @return {@link #gender} (The gender of the specific population.)
         */
        public CodeableConcept getGender() { 
          if (this.gender == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductUndesirableEffectPopulationComponent.gender");
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
        public MedicinalProductUndesirableEffectPopulationComponent setGender(CodeableConcept value) { 
          this.gender = value;
          return this;
        }

        /**
         * @return {@link #race} (Race of the specific population.)
         */
        public CodeableConcept getRace() { 
          if (this.race == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductUndesirableEffectPopulationComponent.race");
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
        public MedicinalProductUndesirableEffectPopulationComponent setRace(CodeableConcept value) { 
          this.race = value;
          return this;
        }

        /**
         * @return {@link #physiologicalCondition} (The existing physiological conditions of the specific population to which this applies.)
         */
        public CodeableConcept getPhysiologicalCondition() { 
          if (this.physiologicalCondition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductUndesirableEffectPopulationComponent.physiologicalCondition");
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
        public MedicinalProductUndesirableEffectPopulationComponent setPhysiologicalCondition(CodeableConcept value) { 
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

      public MedicinalProductUndesirableEffectPopulationComponent copy() {
        MedicinalProductUndesirableEffectPopulationComponent dst = new MedicinalProductUndesirableEffectPopulationComponent();
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
        if (!(other_ instanceof MedicinalProductUndesirableEffectPopulationComponent))
          return false;
        MedicinalProductUndesirableEffectPopulationComponent o = (MedicinalProductUndesirableEffectPopulationComponent) other_;
        return compareDeep(age, o.age, true) && compareDeep(gender, o.gender, true) && compareDeep(race, o.race, true)
           && compareDeep(physiologicalCondition, o.physiologicalCondition, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductUndesirableEffectPopulationComponent))
          return false;
        MedicinalProductUndesirableEffectPopulationComponent o = (MedicinalProductUndesirableEffectPopulationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(age, gender, race, physiologicalCondition
          );
      }

  public String fhirType() {
    return "MedicinalProductUndesirableEffect.population";

  }

  }

    /**
     * The medication for which this is an indication.
     */
    @Child(name = "subject", type = {MedicinalProduct.class, Medication.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The medication for which this is an indication", formalDefinition="The medication for which this is an indication." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (The medication for which this is an indication.)
     */
    protected List<Resource> subjectTarget;


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
    protected List<MedicinalProductUndesirableEffectPopulationComponent> population;

    private static final long serialVersionUID = 522261133L;

  /**
   * Constructor
   */
    public MedicinalProductUndesirableEffect() {
      super();
    }

    /**
     * @return {@link #subject} (The medication for which this is an indication.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductUndesirableEffect setSubject(List<Reference> theSubject) { 
      this.subject = theSubject;
      return this;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    public MedicinalProductUndesirableEffect addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
     */
    public Reference getSubjectFirstRep() { 
      if (getSubject().isEmpty()) {
        addSubject();
      }
      return getSubject().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #symptomConditionEffect} (The symptom, condition or undesirable effect.)
     */
    public CodeableConcept getSymptomConditionEffect() { 
      if (this.symptomConditionEffect == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductUndesirableEffect.symptomConditionEffect");
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
    public MedicinalProductUndesirableEffect setSymptomConditionEffect(CodeableConcept value) { 
      this.symptomConditionEffect = value;
      return this;
    }

    /**
     * @return {@link #classification} (Classification of the effect.)
     */
    public CodeableConcept getClassification() { 
      if (this.classification == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductUndesirableEffect.classification");
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
    public MedicinalProductUndesirableEffect setClassification(CodeableConcept value) { 
      this.classification = value;
      return this;
    }

    /**
     * @return {@link #frequencyOfOccurrence} (The frequency of occurrence of the effect.)
     */
    public CodeableConcept getFrequencyOfOccurrence() { 
      if (this.frequencyOfOccurrence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductUndesirableEffect.frequencyOfOccurrence");
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
    public MedicinalProductUndesirableEffect setFrequencyOfOccurrence(CodeableConcept value) { 
      this.frequencyOfOccurrence = value;
      return this;
    }

    /**
     * @return {@link #population} (The population group to which this applies.)
     */
    public List<MedicinalProductUndesirableEffectPopulationComponent> getPopulation() { 
      if (this.population == null)
        this.population = new ArrayList<MedicinalProductUndesirableEffectPopulationComponent>();
      return this.population;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductUndesirableEffect setPopulation(List<MedicinalProductUndesirableEffectPopulationComponent> thePopulation) { 
      this.population = thePopulation;
      return this;
    }

    public boolean hasPopulation() { 
      if (this.population == null)
        return false;
      for (MedicinalProductUndesirableEffectPopulationComponent item : this.population)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductUndesirableEffectPopulationComponent addPopulation() { //3
      MedicinalProductUndesirableEffectPopulationComponent t = new MedicinalProductUndesirableEffectPopulationComponent();
      if (this.population == null)
        this.population = new ArrayList<MedicinalProductUndesirableEffectPopulationComponent>();
      this.population.add(t);
      return t;
    }

    public MedicinalProductUndesirableEffect addPopulation(MedicinalProductUndesirableEffectPopulationComponent t) { //3
      if (t == null)
        return this;
      if (this.population == null)
        this.population = new ArrayList<MedicinalProductUndesirableEffectPopulationComponent>();
      this.population.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
     */
    public MedicinalProductUndesirableEffectPopulationComponent getPopulationFirstRep() { 
      if (getPopulation().isEmpty()) {
        addPopulation();
      }
      return getPopulation().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject));
        children.add(new Property("symptomConditionEffect", "CodeableConcept", "The symptom, condition or undesirable effect.", 0, 1, symptomConditionEffect));
        children.add(new Property("classification", "CodeableConcept", "Classification of the effect.", 0, 1, classification));
        children.add(new Property("frequencyOfOccurrence", "CodeableConcept", "The frequency of occurrence of the effect.", 0, 1, frequencyOfOccurrence));
        children.add(new Property("population", "", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1867885268: /*subject*/  return new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject);
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
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case -650549981: /*symptomConditionEffect*/ return this.symptomConditionEffect == null ? new Base[0] : new Base[] {this.symptomConditionEffect}; // CodeableConcept
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : new Base[] {this.classification}; // CodeableConcept
        case 791175812: /*frequencyOfOccurrence*/ return this.frequencyOfOccurrence == null ? new Base[0] : new Base[] {this.frequencyOfOccurrence}; // CodeableConcept
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // MedicinalProductUndesirableEffectPopulationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1867885268: // subject
          this.getSubject().add(castToReference(value)); // Reference
          return value;
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
          this.getPopulation().add((MedicinalProductUndesirableEffectPopulationComponent) value); // MedicinalProductUndesirableEffectPopulationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("subject")) {
          this.getSubject().add(castToReference(value));
        } else if (name.equals("symptomConditionEffect")) {
          this.symptomConditionEffect = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("classification")) {
          this.classification = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("frequencyOfOccurrence")) {
          this.frequencyOfOccurrence = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("population")) {
          this.getPopulation().add((MedicinalProductUndesirableEffectPopulationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867885268:  return addSubject(); 
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
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -650549981: /*symptomConditionEffect*/ return new String[] {"CodeableConcept"};
        case 382350310: /*classification*/ return new String[] {"CodeableConcept"};
        case 791175812: /*frequencyOfOccurrence*/ return new String[] {"CodeableConcept"};
        case -2023558323: /*population*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("symptomConditionEffect")) {
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

  public String fhirType() {
    return "MedicinalProductUndesirableEffect";

  }

      public MedicinalProductUndesirableEffect copy() {
        MedicinalProductUndesirableEffect dst = new MedicinalProductUndesirableEffect();
        copyValues(dst);
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        dst.symptomConditionEffect = symptomConditionEffect == null ? null : symptomConditionEffect.copy();
        dst.classification = classification == null ? null : classification.copy();
        dst.frequencyOfOccurrence = frequencyOfOccurrence == null ? null : frequencyOfOccurrence.copy();
        if (population != null) {
          dst.population = new ArrayList<MedicinalProductUndesirableEffectPopulationComponent>();
          for (MedicinalProductUndesirableEffectPopulationComponent i : population)
            dst.population.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductUndesirableEffect typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductUndesirableEffect))
          return false;
        MedicinalProductUndesirableEffect o = (MedicinalProductUndesirableEffect) other_;
        return compareDeep(subject, o.subject, true) && compareDeep(symptomConditionEffect, o.symptomConditionEffect, true)
           && compareDeep(classification, o.classification, true) && compareDeep(frequencyOfOccurrence, o.frequencyOfOccurrence, true)
           && compareDeep(population, o.population, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductUndesirableEffect))
          return false;
        MedicinalProductUndesirableEffect o = (MedicinalProductUndesirableEffect) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subject, symptomConditionEffect
          , classification, frequencyOfOccurrence, population);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductUndesirableEffect;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an undesirable effect</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductUndesirableEffect.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicinalProductUndesirableEffect.subject", description="The medication for which this is an undesirable effect", type="reference", target={Medication.class, MedicinalProduct.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an undesirable effect</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductUndesirableEffect.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicinalProductUndesirableEffect:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicinalProductUndesirableEffect:subject").toLocked();


}

