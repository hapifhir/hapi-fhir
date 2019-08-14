package org.hl7.fhir.dstu2016may.model;

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
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A homogeneous material with a definite composition.
 */
@ResourceDef(name="Substance", profile="http://hl7.org/fhir/Profile/Substance")
public class Substance extends DomainResource {

    @Block()
    public static class SubstanceInstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifier associated with the package/container (usually a label affixed directly).
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier of the package/container", formalDefinition="Identifier associated with the package/container (usually a label affixed directly)." )
        protected Identifier identifier;

        /**
         * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
         */
        @Child(name = "expiry", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When no longer valid to use", formalDefinition="When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry." )
        protected DateTimeType expiry;

        /**
         * The amount of the substance.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount of substance in the package", formalDefinition="The amount of the substance." )
        protected SimpleQuantity quantity;

        private static final long serialVersionUID = -794314734L;

    /**
     * Constructor
     */
      public SubstanceInstanceComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Identifier associated with the package/container (usually a label affixed directly).)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceInstanceComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifier associated with the package/container (usually a label affixed directly).)
         */
        public SubstanceInstanceComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #expiry} (When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.). This is the underlying object with id, value and extensions. The accessor "getExpiry" gives direct access to the value
         */
        public DateTimeType getExpiryElement() { 
          if (this.expiry == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceInstanceComponent.expiry");
            else if (Configuration.doAutoCreate())
              this.expiry = new DateTimeType(); // bb
          return this.expiry;
        }

        public boolean hasExpiryElement() { 
          return this.expiry != null && !this.expiry.isEmpty();
        }

        public boolean hasExpiry() { 
          return this.expiry != null && !this.expiry.isEmpty();
        }

        /**
         * @param value {@link #expiry} (When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.). This is the underlying object with id, value and extensions. The accessor "getExpiry" gives direct access to the value
         */
        public SubstanceInstanceComponent setExpiryElement(DateTimeType value) { 
          this.expiry = value;
          return this;
        }

        /**
         * @return When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
         */
        public Date getExpiry() { 
          return this.expiry == null ? null : this.expiry.getValue();
        }

        /**
         * @param value When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
         */
        public SubstanceInstanceComponent setExpiry(Date value) { 
          if (value == null)
            this.expiry = null;
          else {
            if (this.expiry == null)
              this.expiry = new DateTimeType();
            this.expiry.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of the substance.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceInstanceComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of the substance.)
         */
        public SubstanceInstanceComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Identifier associated with the package/container (usually a label affixed directly).", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("expiry", "dateTime", "When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.", 0, java.lang.Integer.MAX_VALUE, expiry));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The amount of the substance.", 0, java.lang.Integer.MAX_VALUE, quantity));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1289159373: /*expiry*/ return this.expiry == null ? new Base[0] : new Base[] {this.expiry}; // DateTimeType
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -1289159373: // expiry
          this.expiry = castToDateTime(value); // DateTimeType
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("expiry"))
          this.expiry = castToDateTime(value); // DateTimeType
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -1289159373: throw new FHIRException("Cannot make property expiry as it is not a complex type"); // DateTimeType
        case -1285004149:  return getQuantity(); // SimpleQuantity
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("expiry")) {
          throw new FHIRException("Cannot call addChild on a primitive type Substance.expiry");
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else
          return super.addChild(name);
      }

      public SubstanceInstanceComponent copy() {
        SubstanceInstanceComponent dst = new SubstanceInstanceComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.expiry = expiry == null ? null : expiry.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SubstanceInstanceComponent))
          return false;
        SubstanceInstanceComponent o = (SubstanceInstanceComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(expiry, o.expiry, true) && compareDeep(quantity, o.quantity, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubstanceInstanceComponent))
          return false;
        SubstanceInstanceComponent o = (SubstanceInstanceComponent) other;
        return compareValues(expiry, o.expiry, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (expiry == null || expiry.isEmpty())
           && (quantity == null || quantity.isEmpty());
      }

  public String fhirType() {
    return "Substance.instance";

  }

  }

    @Block()
    public static class SubstanceIngredientComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The amount of the ingredient in the substance - a concentration ratio.
         */
        @Child(name = "quantity", type = {Ratio.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Optional amount (concentration)", formalDefinition="The amount of the ingredient in the substance - a concentration ratio." )
        protected Ratio quantity;

        /**
         * Another substance that is a component of this substance.
         */
        @Child(name = "substance", type = {Substance.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A component of the substance", formalDefinition="Another substance that is a component of this substance." )
        protected Reference substance;

        /**
         * The actual object that is the target of the reference (Another substance that is a component of this substance.)
         */
        protected Substance substanceTarget;

        private static final long serialVersionUID = -1783242034L;

    /**
     * Constructor
     */
      public SubstanceIngredientComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubstanceIngredientComponent(Reference substance) {
        super();
        this.substance = substance;
      }

        /**
         * @return {@link #quantity} (The amount of the ingredient in the substance - a concentration ratio.)
         */
        public Ratio getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceIngredientComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Ratio(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of the ingredient in the substance - a concentration ratio.)
         */
        public SubstanceIngredientComponent setQuantity(Ratio value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #substance} (Another substance that is a component of this substance.)
         */
        public Reference getSubstance() { 
          if (this.substance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceIngredientComponent.substance");
            else if (Configuration.doAutoCreate())
              this.substance = new Reference(); // cc
          return this.substance;
        }

        public boolean hasSubstance() { 
          return this.substance != null && !this.substance.isEmpty();
        }

        /**
         * @param value {@link #substance} (Another substance that is a component of this substance.)
         */
        public SubstanceIngredientComponent setSubstance(Reference value) { 
          this.substance = value;
          return this;
        }

        /**
         * @return {@link #substance} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Another substance that is a component of this substance.)
         */
        public Substance getSubstanceTarget() { 
          if (this.substanceTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceIngredientComponent.substance");
            else if (Configuration.doAutoCreate())
              this.substanceTarget = new Substance(); // aa
          return this.substanceTarget;
        }

        /**
         * @param value {@link #substance} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Another substance that is a component of this substance.)
         */
        public SubstanceIngredientComponent setSubstanceTarget(Substance value) { 
          this.substanceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("quantity", "Ratio", "The amount of the ingredient in the substance - a concentration ratio.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("substance", "Reference(Substance)", "Another substance that is a component of this substance.", 0, java.lang.Integer.MAX_VALUE, substance));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Ratio
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1285004149: // quantity
          this.quantity = castToRatio(value); // Ratio
          break;
        case 530040176: // substance
          this.substance = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("quantity"))
          this.quantity = castToRatio(value); // Ratio
        else if (name.equals("substance"))
          this.substance = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1285004149:  return getQuantity(); // Ratio
        case 530040176:  return getSubstance(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("quantity")) {
          this.quantity = new Ratio();
          return this.quantity;
        }
        else if (name.equals("substance")) {
          this.substance = new Reference();
          return this.substance;
        }
        else
          return super.addChild(name);
      }

      public SubstanceIngredientComponent copy() {
        SubstanceIngredientComponent dst = new SubstanceIngredientComponent();
        copyValues(dst);
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.substance = substance == null ? null : substance.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SubstanceIngredientComponent))
          return false;
        SubstanceIngredientComponent o = (SubstanceIngredientComponent) other;
        return compareDeep(quantity, o.quantity, true) && compareDeep(substance, o.substance, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubstanceIngredientComponent))
          return false;
        SubstanceIngredientComponent o = (SubstanceIngredientComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (quantity == null || quantity.isEmpty()) && (substance == null || substance.isEmpty())
          ;
      }

  public String fhirType() {
    return "Substance.ingredient";

  }

  }

    /**
     * Unique identifier for the substance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="Unique identifier for the substance." )
    protected List<Identifier> identifier;

    /**
     * A code that classifies the general type of substance.  This is used  for searching, sorting and display purposes.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What class/type of substance this is", formalDefinition="A code that classifies the general type of substance.  This is used  for searching, sorting and display purposes." )
    protected List<CodeableConcept> category;

    /**
     * A code (or set of codes) that identify this substance.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What substance this is", formalDefinition="A code (or set of codes) that identify this substance." )
    protected CodeableConcept code;

    /**
     * A description of the substance - its appearance, handling requirements, and other usage notes.
     */
    @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Textual description of the substance, comments", formalDefinition="A description of the substance - its appearance, handling requirements, and other usage notes." )
    protected StringType description;

    /**
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance.
     */
    @Child(name = "instance", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="If this describes a specific package/container of the substance", formalDefinition="Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance." )
    protected List<SubstanceInstanceComponent> instance;

    /**
     * A substance can be composed of other substances.
     */
    @Child(name = "ingredient", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Composition information about the substance", formalDefinition="A substance can be composed of other substances." )
    protected List<SubstanceIngredientComponent> ingredient;

    private static final long serialVersionUID = -1653977206L;

  /**
   * Constructor
   */
    public Substance() {
      super();
    }

  /**
   * Constructor
   */
    public Substance(CodeableConcept code) {
      super();
      this.code = code;
    }

    /**
     * @return {@link #identifier} (Unique identifier for the substance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Unique identifier for the substance.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public Substance addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #category} (A code that classifies the general type of substance.  This is used  for searching, sorting and display purposes.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #category} (A code that classifies the general type of substance.  This is used  for searching, sorting and display purposes.)
     */
    // syntactic sugar
    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    // syntactic sugar
    public Substance addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return {@link #code} (A code (or set of codes) that identify this substance.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Substance.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code (or set of codes) that identify this substance.)
     */
    public Substance setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #description} (A description of the substance - its appearance, handling requirements, and other usage notes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Substance.description");
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
     * @param value {@link #description} (A description of the substance - its appearance, handling requirements, and other usage notes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Substance setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A description of the substance - its appearance, handling requirements, and other usage notes.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A description of the substance - its appearance, handling requirements, and other usage notes.
     */
    public Substance setDescription(String value) { 
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
     * @return {@link #instance} (Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance.)
     */
    public List<SubstanceInstanceComponent> getInstance() { 
      if (this.instance == null)
        this.instance = new ArrayList<SubstanceInstanceComponent>();
      return this.instance;
    }

    public boolean hasInstance() { 
      if (this.instance == null)
        return false;
      for (SubstanceInstanceComponent item : this.instance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #instance} (Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance.)
     */
    // syntactic sugar
    public SubstanceInstanceComponent addInstance() { //3
      SubstanceInstanceComponent t = new SubstanceInstanceComponent();
      if (this.instance == null)
        this.instance = new ArrayList<SubstanceInstanceComponent>();
      this.instance.add(t);
      return t;
    }

    // syntactic sugar
    public Substance addInstance(SubstanceInstanceComponent t) { //3
      if (t == null)
        return this;
      if (this.instance == null)
        this.instance = new ArrayList<SubstanceInstanceComponent>();
      this.instance.add(t);
      return this;
    }

    /**
     * @return {@link #ingredient} (A substance can be composed of other substances.)
     */
    public List<SubstanceIngredientComponent> getIngredient() { 
      if (this.ingredient == null)
        this.ingredient = new ArrayList<SubstanceIngredientComponent>();
      return this.ingredient;
    }

    public boolean hasIngredient() { 
      if (this.ingredient == null)
        return false;
      for (SubstanceIngredientComponent item : this.ingredient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #ingredient} (A substance can be composed of other substances.)
     */
    // syntactic sugar
    public SubstanceIngredientComponent addIngredient() { //3
      SubstanceIngredientComponent t = new SubstanceIngredientComponent();
      if (this.ingredient == null)
        this.ingredient = new ArrayList<SubstanceIngredientComponent>();
      this.ingredient.add(t);
      return t;
    }

    // syntactic sugar
    public Substance addIngredient(SubstanceIngredientComponent t) { //3
      if (t == null)
        return this;
      if (this.ingredient == null)
        this.ingredient = new ArrayList<SubstanceIngredientComponent>();
      this.ingredient.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for the substance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the general type of substance.  This is used  for searching, sorting and display purposes.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "A code (or set of codes) that identify this substance.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("description", "string", "A description of the substance - its appearance, handling requirements, and other usage notes.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("instance", "", "Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance.", 0, java.lang.Integer.MAX_VALUE, instance));
        childrenList.add(new Property("ingredient", "", "A substance can be composed of other substances.", 0, java.lang.Integer.MAX_VALUE, ingredient));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : this.instance.toArray(new Base[this.instance.size()]); // SubstanceInstanceComponent
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // SubstanceIngredientComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 555127957: // instance
          this.getInstance().add((SubstanceInstanceComponent) value); // SubstanceInstanceComponent
          break;
        case -206409263: // ingredient
          this.getIngredient().add((SubstanceIngredientComponent) value); // SubstanceIngredientComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("category"))
          this.getCategory().add(castToCodeableConcept(value));
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("instance"))
          this.getInstance().add((SubstanceInstanceComponent) value);
        else if (name.equals("ingredient"))
          this.getIngredient().add((SubstanceIngredientComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 50511102:  return addCategory(); // CodeableConcept
        case 3059181:  return getCode(); // CodeableConcept
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 555127957:  return addInstance(); // SubstanceInstanceComponent
        case -206409263:  return addIngredient(); // SubstanceIngredientComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Substance.description");
        }
        else if (name.equals("instance")) {
          return addInstance();
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Substance";

  }

      public Substance copy() {
        Substance dst = new Substance();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.description = description == null ? null : description.copy();
        if (instance != null) {
          dst.instance = new ArrayList<SubstanceInstanceComponent>();
          for (SubstanceInstanceComponent i : instance)
            dst.instance.add(i.copy());
        };
        if (ingredient != null) {
          dst.ingredient = new ArrayList<SubstanceIngredientComponent>();
          for (SubstanceIngredientComponent i : ingredient)
            dst.ingredient.add(i.copy());
        };
        return dst;
      }

      protected Substance typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Substance))
          return false;
        Substance o = (Substance) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(category, o.category, true) && compareDeep(code, o.code, true)
           && compareDeep(description, o.description, true) && compareDeep(instance, o.instance, true) && compareDeep(ingredient, o.ingredient, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Substance))
          return false;
        Substance o = (Substance) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (code == null || code.isEmpty()) && (description == null || description.isEmpty()) && (instance == null || instance.isEmpty())
           && (ingredient == null || ingredient.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Substance;
   }

 /**
   * Search parameter: <b>substance</b>
   * <p>
   * Description: <b>A component of the substance</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Substance.ingredient.substance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="substance", path="Substance.ingredient.substance", description="A component of the substance", type="reference" )
  public static final String SP_SUBSTANCE = "substance";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>substance</b>
   * <p>
   * Description: <b>A component of the substance</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Substance.ingredient.substance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBSTANCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBSTANCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Substance:substance</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBSTANCE = new ca.uhn.fhir.model.api.Include("Substance:substance").toLocked();

 /**
   * Search parameter: <b>container-identifier</b>
   * <p>
   * Description: <b>Identifier of the package/container</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.instance.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="container-identifier", path="Substance.instance.identifier", description="Identifier of the package/container", type="token" )
  public static final String SP_CONTAINER_IDENTIFIER = "container-identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>container-identifier</b>
   * <p>
   * Description: <b>Identifier of the package/container</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.instance.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTAINER_IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTAINER_IDENTIFIER);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>The category of the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Substance.category", description="The category of the substance", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>The category of the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>quantity</b>
   * <p>
   * Description: <b>Amount of substance in the package</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Substance.instance.quantity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="quantity", path="Substance.instance.quantity", description="Amount of substance in the package", type="quantity" )
  public static final String SP_QUANTITY = "quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>quantity</b>
   * <p>
   * Description: <b>Amount of substance in the package</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Substance.instance.quantity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_QUANTITY);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>The code of the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Substance.code", description="The code of the substance", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>The code of the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier for the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Substance.identifier", description="Unique identifier for the substance", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier for the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Substance.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>expiry</b>
   * <p>
   * Description: <b>Expiry date of package or container of substance</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Substance.instance.expiry</b><br>
   * </p>
   */
  @SearchParamDefinition(name="expiry", path="Substance.instance.expiry", description="Expiry date of package or container of substance", type="date" )
  public static final String SP_EXPIRY = "expiry";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>expiry</b>
   * <p>
   * Description: <b>Expiry date of package or container of substance</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Substance.instance.expiry</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EXPIRY = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EXPIRY);


}

