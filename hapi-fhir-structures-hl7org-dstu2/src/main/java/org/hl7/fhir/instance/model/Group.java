package org.hl7.fhir.instance.model;

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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized.  I.e. A collection of entities that isn't an Organization.
 */
@ResourceDef(name="Group", profile="http://hl7.org/fhir/Profile/Group")
public class Group extends DomainResource {

    public enum GroupType {
        /**
         * Group contains "person" Patient resources.
         */
        PERSON, 
        /**
         * Group contains "animal" Patient resources.
         */
        ANIMAL, 
        /**
         * Group contains healthcare practitioner resources.
         */
        PRACTITIONER, 
        /**
         * Group contains Device resources.
         */
        DEVICE, 
        /**
         * Group contains Medication resources.
         */
        MEDICATION, 
        /**
         * Group contains Substance resources.
         */
        SUBSTANCE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GroupType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("person".equals(codeString))
          return PERSON;
        if ("animal".equals(codeString))
          return ANIMAL;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("device".equals(codeString))
          return DEVICE;
        if ("medication".equals(codeString))
          return MEDICATION;
        if ("substance".equals(codeString))
          return SUBSTANCE;
        throw new Exception("Unknown GroupType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERSON: return "person";
            case ANIMAL: return "animal";
            case PRACTITIONER: return "practitioner";
            case DEVICE: return "device";
            case MEDICATION: return "medication";
            case SUBSTANCE: return "substance";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PERSON: return "";
            case ANIMAL: return "";
            case PRACTITIONER: return "";
            case DEVICE: return "";
            case MEDICATION: return "";
            case SUBSTANCE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PERSON: return "Group contains 'person' Patient resources.";
            case ANIMAL: return "Group contains 'animal' Patient resources.";
            case PRACTITIONER: return "Group contains healthcare practitioner resources.";
            case DEVICE: return "Group contains Device resources.";
            case MEDICATION: return "Group contains Medication resources.";
            case SUBSTANCE: return "Group contains Substance resources.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERSON: return "person";
            case ANIMAL: return "animal";
            case PRACTITIONER: return "practitioner";
            case DEVICE: return "device";
            case MEDICATION: return "medication";
            case SUBSTANCE: return "substance";
            default: return "?";
          }
        }
    }

  public static class GroupTypeEnumFactory implements EnumFactory<GroupType> {
    public GroupType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("person".equals(codeString))
          return GroupType.PERSON;
        if ("animal".equals(codeString))
          return GroupType.ANIMAL;
        if ("practitioner".equals(codeString))
          return GroupType.PRACTITIONER;
        if ("device".equals(codeString))
          return GroupType.DEVICE;
        if ("medication".equals(codeString))
          return GroupType.MEDICATION;
        if ("substance".equals(codeString))
          return GroupType.SUBSTANCE;
        throw new IllegalArgumentException("Unknown GroupType code '"+codeString+"'");
        }
    public String toCode(GroupType code) {
      if (code == GroupType.PERSON)
        return "person";
      if (code == GroupType.ANIMAL)
        return "animal";
      if (code == GroupType.PRACTITIONER)
        return "practitioner";
      if (code == GroupType.DEVICE)
        return "device";
      if (code == GroupType.MEDICATION)
        return "medication";
      if (code == GroupType.SUBSTANCE)
        return "substance";
      return "?";
      }
    }

    @Block()
    public static class GroupCharacteristicComponent extends BackboneElement {
        /**
         * A code that identifies the kind of trait being asserted.
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Kind of characteristic", formalDefinition="A code that identifies the kind of trait being asserted." )
        protected CodeableConcept code;

        /**
         * The value of the trait that holds (or does not hold - see 'exclude') for members of the group.
         */
        @Child(name="value", type={CodeableConcept.class, BooleanType.class, Quantity.class, Range.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Value held by characteristic", formalDefinition="The value of the trait that holds (or does not hold - see 'exclude') for members of the group." )
        protected Type value;

        /**
         * If true, indicates the characteristic is one that is NOT held by members of the group.
         */
        @Child(name="exclude", type={BooleanType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Group includes or excludes", formalDefinition="If true, indicates the characteristic is one that is NOT held by members of the group." )
        protected BooleanType exclude;

        private static final long serialVersionUID = 803478031L;

      public GroupCharacteristicComponent() {
        super();
      }

      public GroupCharacteristicComponent(CodeableConcept code, Type value, BooleanType exclude) {
        super();
        this.code = code;
        this.value = value;
        this.exclude = exclude;
      }

        /**
         * @return {@link #code} (A code that identifies the kind of trait being asserted.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupCharacteristicComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that identifies the kind of trait being asserted.)
         */
        public GroupCharacteristicComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #value} (The value of the trait that holds (or does not hold - see 'exclude') for members of the group.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of the trait that holds (or does not hold - see 'exclude') for members of the group.)
         */
        public CodeableConcept getValueCodeableConcept() throws Exception { 
          if (!(this.value instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeableConcept) this.value;
        }

        /**
         * @return {@link #value} (The value of the trait that holds (or does not hold - see 'exclude') for members of the group.)
         */
        public BooleanType getValueBooleanType() throws Exception { 
          if (!(this.value instanceof BooleanType))
            throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        /**
         * @return {@link #value} (The value of the trait that holds (or does not hold - see 'exclude') for members of the group.)
         */
        public Quantity getValueQuantity() throws Exception { 
          if (!(this.value instanceof Quantity))
            throw new Exception("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        /**
         * @return {@link #value} (The value of the trait that holds (or does not hold - see 'exclude') for members of the group.)
         */
        public Range getValueRange() throws Exception { 
          if (!(this.value instanceof Range))
            throw new Exception("Type mismatch: the type Range was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Range) this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the trait that holds (or does not hold - see 'exclude') for members of the group.)
         */
        public GroupCharacteristicComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #exclude} (If true, indicates the characteristic is one that is NOT held by members of the group.). This is the underlying object with id, value and extensions. The accessor "getExclude" gives direct access to the value
         */
        public BooleanType getExcludeElement() { 
          if (this.exclude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupCharacteristicComponent.exclude");
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
         * @param value {@link #exclude} (If true, indicates the characteristic is one that is NOT held by members of the group.). This is the underlying object with id, value and extensions. The accessor "getExclude" gives direct access to the value
         */
        public GroupCharacteristicComponent setExcludeElement(BooleanType value) { 
          this.exclude = value;
          return this;
        }

        /**
         * @return If true, indicates the characteristic is one that is NOT held by members of the group.
         */
        public boolean getExclude() { 
          return this.exclude == null ? false : this.exclude.getValue();
        }

        /**
         * @param value If true, indicates the characteristic is one that is NOT held by members of the group.
         */
        public GroupCharacteristicComponent setExclude(boolean value) { 
            if (this.exclude == null)
              this.exclude = new BooleanType();
            this.exclude.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "A code that identifies the kind of trait being asserted.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("value[x]", "CodeableConcept|boolean|Quantity|Range", "The value of the trait that holds (or does not hold - see 'exclude') for members of the group.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("exclude", "boolean", "If true, indicates the characteristic is one that is NOT held by members of the group.", 0, java.lang.Integer.MAX_VALUE, exclude));
        }

      public GroupCharacteristicComponent copy() {
        GroupCharacteristicComponent dst = new GroupCharacteristicComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        dst.exclude = exclude == null ? null : exclude.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GroupCharacteristicComponent))
          return false;
        GroupCharacteristicComponent o = (GroupCharacteristicComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true) && compareDeep(exclude, o.exclude, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GroupCharacteristicComponent))
          return false;
        GroupCharacteristicComponent o = (GroupCharacteristicComponent) other;
        return compareValues(exclude, o.exclude, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (value == null || value.isEmpty())
           && (exclude == null || exclude.isEmpty());
      }

  }

    /**
     * A unique business identifier for this group.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Unique id", formalDefinition="A unique business identifier for this group." )
    protected Identifier identifier;

    /**
     * Identifies the broad classification of the kind of resources the group includes.
     */
    @Child(name = "type", type = {CodeType.class}, order = 1, min = 1, max = 1)
    @Description(shortDefinition="person | animal | practitioner | device | medication | substance", formalDefinition="Identifies the broad classification of the kind of resources the group includes." )
    protected Enumeration<GroupType> type;

    /**
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals.
     */
    @Child(name = "actual", type = {BooleanType.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="Descriptive or actual", formalDefinition="If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals." )
    protected BooleanType actual;

    /**
     * Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Kind of Group members", formalDefinition="Provides a specific type of resource the group includes.  E.g. 'cow', 'syringe', etc." )
    protected CodeableConcept code;

    /**
     * A label assigned to the group for human identification and communication.
     */
    @Child(name = "name", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Label for Group", formalDefinition="A label assigned to the group for human identification and communication." )
    protected StringType name;

    /**
     * A count of the number of resource instances that are part of the group.
     */
    @Child(name = "quantity", type = {IntegerType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Number of members", formalDefinition="A count of the number of resource instances that are part of the group." )
    protected IntegerType quantity;

    /**
     * Identifies the traits shared by members of the group.
     */
    @Child(name = "characteristic", type = {}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Trait of group members", formalDefinition="Identifies the traits shared by members of the group." )
    protected List<GroupCharacteristicComponent> characteristic;

    /**
     * Identifies the resource instances that are members of the group.
     */
    @Child(name = "member", type = {Patient.class, Practitioner.class, Device.class, Medication.class, Substance.class}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who is in group", formalDefinition="Identifies the resource instances that are members of the group." )
    protected List<Reference> member;
    /**
     * The actual objects that are the target of the reference (Identifies the resource instances that are members of the group.)
     */
    protected List<Resource> memberTarget;


    private static final long serialVersionUID = 45096653L;

    public Group() {
      super();
    }

    public Group(Enumeration<GroupType> type, BooleanType actual) {
      super();
      this.type = type;
      this.actual = actual;
    }

    /**
     * @return {@link #identifier} (A unique business identifier for this group.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Group.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A unique business identifier for this group.)
     */
    public Group setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #type} (Identifies the broad classification of the kind of resources the group includes.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<GroupType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Group.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<GroupType>(new GroupTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Identifies the broad classification of the kind of resources the group includes.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Group setTypeElement(Enumeration<GroupType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Identifies the broad classification of the kind of resources the group includes.
     */
    public GroupType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Identifies the broad classification of the kind of resources the group includes.
     */
    public Group setType(GroupType value) { 
        if (this.type == null)
          this.type = new Enumeration<GroupType>(new GroupTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #actual} (If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals.). This is the underlying object with id, value and extensions. The accessor "getActual" gives direct access to the value
     */
    public BooleanType getActualElement() { 
      if (this.actual == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Group.actual");
        else if (Configuration.doAutoCreate())
          this.actual = new BooleanType(); // bb
      return this.actual;
    }

    public boolean hasActualElement() { 
      return this.actual != null && !this.actual.isEmpty();
    }

    public boolean hasActual() { 
      return this.actual != null && !this.actual.isEmpty();
    }

    /**
     * @param value {@link #actual} (If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals.). This is the underlying object with id, value and extensions. The accessor "getActual" gives direct access to the value
     */
    public Group setActualElement(BooleanType value) { 
      this.actual = value;
      return this;
    }

    /**
     * @return If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals.
     */
    public boolean getActual() { 
      return this.actual == null ? false : this.actual.getValue();
    }

    /**
     * @param value If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals.
     */
    public Group setActual(boolean value) { 
        if (this.actual == null)
          this.actual = new BooleanType();
        this.actual.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Group.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.)
     */
    public Group setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #name} (A label assigned to the group for human identification and communication.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Group.name");
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
     * @param value {@link #name} (A label assigned to the group for human identification and communication.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Group setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A label assigned to the group for human identification and communication.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A label assigned to the group for human identification and communication.
     */
    public Group setName(String value) { 
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
     * @return {@link #quantity} (A count of the number of resource instances that are part of the group.). This is the underlying object with id, value and extensions. The accessor "getQuantity" gives direct access to the value
     */
    public IntegerType getQuantityElement() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Group.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new IntegerType(); // bb
      return this.quantity;
    }

    public boolean hasQuantityElement() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (A count of the number of resource instances that are part of the group.). This is the underlying object with id, value and extensions. The accessor "getQuantity" gives direct access to the value
     */
    public Group setQuantityElement(IntegerType value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return A count of the number of resource instances that are part of the group.
     */
    public int getQuantity() { 
      return this.quantity == null ? 0 : this.quantity.getValue();
    }

    /**
     * @param value A count of the number of resource instances that are part of the group.
     */
    public Group setQuantity(int value) { 
        if (this.quantity == null)
          this.quantity = new IntegerType();
        this.quantity.setValue(value);
      return this;
    }

    /**
     * @return {@link #characteristic} (Identifies the traits shared by members of the group.)
     */
    public List<GroupCharacteristicComponent> getCharacteristic() { 
      if (this.characteristic == null)
        this.characteristic = new ArrayList<GroupCharacteristicComponent>();
      return this.characteristic;
    }

    public boolean hasCharacteristic() { 
      if (this.characteristic == null)
        return false;
      for (GroupCharacteristicComponent item : this.characteristic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #characteristic} (Identifies the traits shared by members of the group.)
     */
    // syntactic sugar
    public GroupCharacteristicComponent addCharacteristic() { //3
      GroupCharacteristicComponent t = new GroupCharacteristicComponent();
      if (this.characteristic == null)
        this.characteristic = new ArrayList<GroupCharacteristicComponent>();
      this.characteristic.add(t);
      return t;
    }

    /**
     * @return {@link #member} (Identifies the resource instances that are members of the group.)
     */
    public List<Reference> getMember() { 
      if (this.member == null)
        this.member = new ArrayList<Reference>();
      return this.member;
    }

    public boolean hasMember() { 
      if (this.member == null)
        return false;
      for (Reference item : this.member)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #member} (Identifies the resource instances that are members of the group.)
     */
    // syntactic sugar
    public Reference addMember() { //3
      Reference t = new Reference();
      if (this.member == null)
        this.member = new ArrayList<Reference>();
      this.member.add(t);
      return t;
    }

    /**
     * @return {@link #member} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the resource instances that are members of the group.)
     */
    public List<Resource> getMemberTarget() { 
      if (this.memberTarget == null)
        this.memberTarget = new ArrayList<Resource>();
      return this.memberTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique business identifier for this group.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "code", "Identifies the broad classification of the kind of resources the group includes.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("actual", "boolean", "If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals.", 0, java.lang.Integer.MAX_VALUE, actual));
        childrenList.add(new Property("code", "CodeableConcept", "Provides a specific type of resource the group includes.  E.g. 'cow', 'syringe', etc.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("name", "string", "A label assigned to the group for human identification and communication.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("quantity", "integer", "A count of the number of resource instances that are part of the group.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("characteristic", "", "Identifies the traits shared by members of the group.", 0, java.lang.Integer.MAX_VALUE, characteristic));
        childrenList.add(new Property("member", "Reference(Patient|Practitioner|Device|Medication|Substance)", "Identifies the resource instances that are members of the group.", 0, java.lang.Integer.MAX_VALUE, member));
      }

      public Group copy() {
        Group dst = new Group();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.actual = actual == null ? null : actual.copy();
        dst.code = code == null ? null : code.copy();
        dst.name = name == null ? null : name.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (characteristic != null) {
          dst.characteristic = new ArrayList<GroupCharacteristicComponent>();
          for (GroupCharacteristicComponent i : characteristic)
            dst.characteristic.add(i.copy());
        };
        if (member != null) {
          dst.member = new ArrayList<Reference>();
          for (Reference i : member)
            dst.member.add(i.copy());
        };
        return dst;
      }

      protected Group typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Group))
          return false;
        Group o = (Group) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(actual, o.actual, true)
           && compareDeep(code, o.code, true) && compareDeep(name, o.name, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(characteristic, o.characteristic, true) && compareDeep(member, o.member, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Group))
          return false;
        Group o = (Group) other;
        return compareValues(type, o.type, true) && compareValues(actual, o.actual, true) && compareValues(name, o.name, true)
           && compareValues(quantity, o.quantity, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (actual == null || actual.isEmpty()) && (code == null || code.isEmpty()) && (name == null || name.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (characteristic == null || characteristic.isEmpty())
           && (member == null || member.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Group;
   }

  @SearchParamDefinition(name="actual", path="Group.actual", description="Descriptive or actual", type="token" )
  public static final String SP_ACTUAL = "actual";
  @SearchParamDefinition(name = "identifier", path = "Group.identifier", description = "Unique id", type = "token")
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name = "characteristic-value", path = "", description = "A composite of both characteristic and value", type = "composite")
  public static final String SP_CHARACTERISTICVALUE = "characteristic-value";
  @SearchParamDefinition(name="code", path="Group.code", description="The kind of resources contained", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name = "member", path = "Group.member", description = "Who is in group", type = "reference")
  public static final String SP_MEMBER = "member";
  @SearchParamDefinition(name = "exclude", path = "Group.characteristic.exclude", description = "Group includes or excludes", type = "token")
  public static final String SP_EXCLUDE = "exclude";
  @SearchParamDefinition(name="type", path="Group.type", description="The type of resources the group contains", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name = "value", path = "Group.characteristic.value[x]", description = "Value held by characteristic", type = "token")
  public static final String SP_VALUE = "value";
  @SearchParamDefinition(name = "characteristic", path = "Group.characteristic.code", description = "Kind of characteristic", type = "token")
  public static final String SP_CHARACTERISTIC = "characteristic";

}

