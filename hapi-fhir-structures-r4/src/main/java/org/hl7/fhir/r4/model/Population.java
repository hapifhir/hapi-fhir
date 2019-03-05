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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A populatioof people with some set of grouping criteria.
 */
@DatatypeDef(name="Population")
public class Population extends BackboneType implements ICompositeType {

    /**
     * The age of the specific population.
     */
    @Child(name = "age", type = {Range.class, CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The age of the specific population", formalDefinition="The age of the specific population." )
    protected Type age;

    /**
     * The gender of the specific population.
     */
    @Child(name = "gender", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The gender of the specific population", formalDefinition="The gender of the specific population." )
    protected CodeableConcept gender;

    /**
     * Race of the specific population.
     */
    @Child(name = "race", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Race of the specific population", formalDefinition="Race of the specific population." )
    protected CodeableConcept race;

    /**
     * The existing physiological conditions of the specific population to which this applies.
     */
    @Child(name = "physiologicalCondition", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The existing physiological conditions of the specific population to which this applies", formalDefinition="The existing physiological conditions of the specific population to which this applies." )
    protected CodeableConcept physiologicalCondition;

    private static final long serialVersionUID = 495777760L;

  /**
   * Constructor
   */
    public Population() {
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
        this.age = new Range();
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
        this.age = new CodeableConcept();
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
    public Population setAge(Type value) { 
      if (value != null && !(value instanceof Range || value instanceof CodeableConcept))
        throw new Error("Not the right type for Population.age[x]: "+value.fhirType());
      this.age = value;
      return this;
    }

    /**
     * @return {@link #gender} (The gender of the specific population.)
     */
    public CodeableConcept getGender() { 
      if (this.gender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Population.gender");
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
    public Population setGender(CodeableConcept value) { 
      this.gender = value;
      return this;
    }

    /**
     * @return {@link #race} (Race of the specific population.)
     */
    public CodeableConcept getRace() { 
      if (this.race == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Population.race");
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
    public Population setRace(CodeableConcept value) { 
      this.race = value;
      return this;
    }

    /**
     * @return {@link #physiologicalCondition} (The existing physiological conditions of the specific population to which this applies.)
     */
    public CodeableConcept getPhysiologicalCondition() { 
      if (this.physiologicalCondition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Population.physiologicalCondition");
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
    public Population setPhysiologicalCondition(CodeableConcept value) { 
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

  public String fhirType() {
    return "Population";

  }

      public Population copy() {
        Population dst = new Population();
        copyValues(dst);
        dst.age = age == null ? null : age.copy();
        dst.gender = gender == null ? null : gender.copy();
        dst.race = race == null ? null : race.copy();
        dst.physiologicalCondition = physiologicalCondition == null ? null : physiologicalCondition.copy();
        return dst;
      }

      protected Population typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Population))
          return false;
        Population o = (Population) other_;
        return compareDeep(age, o.age, true) && compareDeep(gender, o.gender, true) && compareDeep(race, o.race, true)
           && compareDeep(physiologicalCondition, o.physiologicalCondition, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Population))
          return false;
        Population o = (Population) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(age, gender, race, physiologicalCondition
          );
      }


}

