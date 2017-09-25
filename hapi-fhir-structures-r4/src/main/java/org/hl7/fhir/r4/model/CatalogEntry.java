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

// Generated on Sat, Sep 23, 2017 17:56-0400 for FHIR v3.1.0

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
 * Catalog entries are wrappers that contextualize items included in a catalog.
 */
@ResourceDef(name="CatalogEntry", profile="http://hl7.org/fhir/Profile/CatalogEntry")
public class CatalogEntry extends DomainResource {

    @Block()
    public static class CatalogEntryRelatedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.
         */
        @Child(name = "relationtype", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of relation to the related item", formalDefinition="The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc." )
        protected CodeableConcept relationtype;

        /**
         * The type of related item.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of related item - medication, devices", formalDefinition="The type of related item." )
        protected CodeableConcept type;

        /**
         * The reference to the related item.
         */
        @Child(name = "item", type = {Medication.class, Device.class, Procedure.class, CarePlan.class, Organization.class, Practitioner.class, HealthcareService.class, ServiceDefinition.class, CatalogEntry.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The reference to the related item", formalDefinition="The reference to the related item." )
        protected Reference item;

        /**
         * The actual object that is the target of the reference (The reference to the related item.)
         */
        protected Resource itemTarget;

        private static final long serialVersionUID = -921406858L;

    /**
     * Constructor
     */
      public CatalogEntryRelatedItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CatalogEntryRelatedItemComponent(CodeableConcept relationtype, Reference item) {
        super();
        this.relationtype = relationtype;
        this.item = item;
      }

        /**
         * @return {@link #relationtype} (The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.)
         */
        public CodeableConcept getRelationtype() { 
          if (this.relationtype == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CatalogEntryRelatedItemComponent.relationtype");
            else if (Configuration.doAutoCreate())
              this.relationtype = new CodeableConcept(); // cc
          return this.relationtype;
        }

        public boolean hasRelationtype() { 
          return this.relationtype != null && !this.relationtype.isEmpty();
        }

        /**
         * @param value {@link #relationtype} (The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.)
         */
        public CatalogEntryRelatedItemComponent setRelationtype(CodeableConcept value) { 
          this.relationtype = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of related item.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CatalogEntryRelatedItemComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of related item.)
         */
        public CatalogEntryRelatedItemComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #item} (The reference to the related item.)
         */
        public Reference getItem() { 
          if (this.item == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CatalogEntryRelatedItemComponent.item");
            else if (Configuration.doAutoCreate())
              this.item = new Reference(); // cc
          return this.item;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (The reference to the related item.)
         */
        public CatalogEntryRelatedItemComponent setItem(Reference value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #item} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The reference to the related item.)
         */
        public Resource getItemTarget() { 
          return this.itemTarget;
        }

        /**
         * @param value {@link #item} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The reference to the related item.)
         */
        public CatalogEntryRelatedItemComponent setItemTarget(Resource value) { 
          this.itemTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("relationtype", "CodeableConcept", "The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.", 0, 1, relationtype));
          children.add(new Property("type", "CodeableConcept", "The type of related item.", 0, 1, type));
          children.add(new Property("item", "Reference(Medication|Device|Procedure|CarePlan|Organization|Practitioner|HealthcareService|ServiceDefinition|CatalogEntry)", "The reference to the related item.", 0, 1, item));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -261805258: /*relationtype*/  return new Property("relationtype", "CodeableConcept", "The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.", 0, 1, relationtype);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of related item.", 0, 1, type);
          case 3242771: /*item*/  return new Property("item", "Reference(Medication|Device|Procedure|CarePlan|Organization|Practitioner|HealthcareService|ServiceDefinition|CatalogEntry)", "The reference to the related item.", 0, 1, item);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -261805258: /*relationtype*/ return this.relationtype == null ? new Base[0] : new Base[] {this.relationtype}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -261805258: // relationtype
          this.relationtype = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3242771: // item
          this.item = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("relationtype")) {
          this.relationtype = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("item")) {
          this.item = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -261805258:  return getRelationtype(); 
        case 3575610:  return getType(); 
        case 3242771:  return getItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -261805258: /*relationtype*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3242771: /*item*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("relationtype")) {
          this.relationtype = new CodeableConcept();
          return this.relationtype;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("item")) {
          this.item = new Reference();
          return this.item;
        }
        else
          return super.addChild(name);
      }

      public CatalogEntryRelatedItemComponent copy() {
        CatalogEntryRelatedItemComponent dst = new CatalogEntryRelatedItemComponent();
        copyValues(dst);
        dst.relationtype = relationtype == null ? null : relationtype.copy();
        dst.type = type == null ? null : type.copy();
        dst.item = item == null ? null : item.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CatalogEntryRelatedItemComponent))
          return false;
        CatalogEntryRelatedItemComponent o = (CatalogEntryRelatedItemComponent) other;
        return compareDeep(relationtype, o.relationtype, true) && compareDeep(type, o.type, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CatalogEntryRelatedItemComponent))
          return false;
        CatalogEntryRelatedItemComponent o = (CatalogEntryRelatedItemComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(relationtype, type, item
          );
      }

  public String fhirType() {
    return "CatalogEntry.relatedItem";

  }

  }

    /**
     * The type of item - medication, device, service, protocol or other.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of item - medication, device, service, protocol or other", formalDefinition="The type of item - medication, device, service, protocol or other." )
    protected CodeableConcept type;

    /**
     * Whether the entry represents an orderable item, or other.
     */
    @Child(name = "purpose", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the entry represents an orderable item, or other", formalDefinition="Whether the entry represents an orderable item, or other." )
    protected CodeableConcept purpose;

    /**
     * Content of the catalog.
     */
    @Child(name = "referencedItem", type = {Medication.class, Device.class, Procedure.class, CarePlan.class, Organization.class, Practitioner.class, HealthcareService.class, ServiceDefinition.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The item itself", formalDefinition="Content of the catalog." )
    protected Reference referencedItem;

    /**
     * The actual object that is the target of the reference (Content of the catalog.)
     */
    protected Resource referencedItemTarget;

    /**
     * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier of the catalog item", formalDefinition="Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code." )
    protected Identifier identifier;

    /**
     * Used in supporting related concepts, e.g. NDC to RxNorm.
     */
    @Child(name = "additionalIdentifier", type = {Identifier.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Any additional identifier(s) for the catalog item, in the same granularity or concept", formalDefinition="Used in supporting related concepts, e.g. NDC to RxNorm." )
    protected List<Identifier> additionalIdentifier;

    /**
     * Classes of devices, or ATC for medication.
     */
    @Child(name = "classification", type = {Identifier.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classification (category or class) of the item entry", formalDefinition="Classes of devices, or ATC for medication." )
    protected List<Identifier> classification;

    /**
     * Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not prescribable.
     */
    @Child(name = "status", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The status of the item, e.g. active, approved, deleted…", formalDefinition="Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not prescribable." )
    protected CodeableConcept status;

    /**
     * The time period in which this catalog entry is expected to be active.
     */
    @Child(name = "validityPeriod", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The time period in which this catalog entry is expected to be active", formalDefinition="The time period in which this catalog entry is expected to be active." )
    protected Period validityPeriod;

    /**
     * Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.
     */
    @Child(name = "lastUpdated", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When was this catalog last updated", formalDefinition="Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated." )
    protected DateTimeType lastUpdated;

    /**
     * Used for examplefor Out of Formulary, or any specifics.
     */
    @Child(name = "additionalCharacteristic", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional characteristics of the catalog entry", formalDefinition="Used for examplefor Out of Formulary, or any specifics." )
    protected List<CodeableConcept> additionalCharacteristic;

    /**
     * User for example for ATC classification, or.
     */
    @Child(name = "additionalClassification", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional classification of the catalog entry", formalDefinition="User for example for ATC classification, or." )
    protected List<CodeableConcept> additionalClassification;

    /**
     * Used for example, to point to a substance, or to a device used to administer a medication.
     */
    @Child(name = "relatedItem", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An item that this catalog entry is related to", formalDefinition="Used for example, to point to a substance, or to a device used to administer a medication." )
    protected List<CatalogEntryRelatedItemComponent> relatedItem;

    private static final long serialVersionUID = 359754478L;

  /**
   * Constructor
   */
    public CatalogEntry() {
      super();
    }

  /**
   * Constructor
   */
    public CatalogEntry(CodeableConcept purpose, Reference referencedItem) {
      super();
      this.purpose = purpose;
      this.referencedItem = referencedItem;
    }

    /**
     * @return {@link #type} (The type of item - medication, device, service, protocol or other.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of item - medication, device, service, protocol or other.)
     */
    public CatalogEntry setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #purpose} (Whether the entry represents an orderable item, or other.)
     */
    public CodeableConcept getPurpose() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new CodeableConcept(); // cc
      return this.purpose;
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Whether the entry represents an orderable item, or other.)
     */
    public CatalogEntry setPurpose(CodeableConcept value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return {@link #referencedItem} (Content of the catalog.)
     */
    public Reference getReferencedItem() { 
      if (this.referencedItem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.referencedItem");
        else if (Configuration.doAutoCreate())
          this.referencedItem = new Reference(); // cc
      return this.referencedItem;
    }

    public boolean hasReferencedItem() { 
      return this.referencedItem != null && !this.referencedItem.isEmpty();
    }

    /**
     * @param value {@link #referencedItem} (Content of the catalog.)
     */
    public CatalogEntry setReferencedItem(Reference value) { 
      this.referencedItem = value;
      return this;
    }

    /**
     * @return {@link #referencedItem} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Content of the catalog.)
     */
    public Resource getReferencedItemTarget() { 
      return this.referencedItemTarget;
    }

    /**
     * @param value {@link #referencedItem} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Content of the catalog.)
     */
    public CatalogEntry setReferencedItemTarget(Resource value) { 
      this.referencedItemTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.)
     */
    public CatalogEntry setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #additionalIdentifier} (Used in supporting related concepts, e.g. NDC to RxNorm.)
     */
    public List<Identifier> getAdditionalIdentifier() { 
      if (this.additionalIdentifier == null)
        this.additionalIdentifier = new ArrayList<Identifier>();
      return this.additionalIdentifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CatalogEntry setAdditionalIdentifier(List<Identifier> theAdditionalIdentifier) { 
      this.additionalIdentifier = theAdditionalIdentifier;
      return this;
    }

    public boolean hasAdditionalIdentifier() { 
      if (this.additionalIdentifier == null)
        return false;
      for (Identifier item : this.additionalIdentifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addAdditionalIdentifier() { //3
      Identifier t = new Identifier();
      if (this.additionalIdentifier == null)
        this.additionalIdentifier = new ArrayList<Identifier>();
      this.additionalIdentifier.add(t);
      return t;
    }

    public CatalogEntry addAdditionalIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.additionalIdentifier == null)
        this.additionalIdentifier = new ArrayList<Identifier>();
      this.additionalIdentifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #additionalIdentifier}, creating it if it does not already exist
     */
    public Identifier getAdditionalIdentifierFirstRep() { 
      if (getAdditionalIdentifier().isEmpty()) {
        addAdditionalIdentifier();
      }
      return getAdditionalIdentifier().get(0);
    }

    /**
     * @return {@link #classification} (Classes of devices, or ATC for medication.)
     */
    public List<Identifier> getClassification() { 
      if (this.classification == null)
        this.classification = new ArrayList<Identifier>();
      return this.classification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CatalogEntry setClassification(List<Identifier> theClassification) { 
      this.classification = theClassification;
      return this;
    }

    public boolean hasClassification() { 
      if (this.classification == null)
        return false;
      for (Identifier item : this.classification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addClassification() { //3
      Identifier t = new Identifier();
      if (this.classification == null)
        this.classification = new ArrayList<Identifier>();
      this.classification.add(t);
      return t;
    }

    public CatalogEntry addClassification(Identifier t) { //3
      if (t == null)
        return this;
      if (this.classification == null)
        this.classification = new ArrayList<Identifier>();
      this.classification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #classification}, creating it if it does not already exist
     */
    public Identifier getClassificationFirstRep() { 
      if (getClassification().isEmpty()) {
        addClassification();
      }
      return getClassification().get(0);
    }

    /**
     * @return {@link #status} (Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not prescribable.)
     */
    public CodeableConcept getStatus() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeableConcept(); // cc
      return this.status;
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not prescribable.)
     */
    public CatalogEntry setStatus(CodeableConcept value) { 
      this.status = value;
      return this;
    }

    /**
     * @return {@link #validityPeriod} (The time period in which this catalog entry is expected to be active.)
     */
    public Period getValidityPeriod() { 
      if (this.validityPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.validityPeriod");
        else if (Configuration.doAutoCreate())
          this.validityPeriod = new Period(); // cc
      return this.validityPeriod;
    }

    public boolean hasValidityPeriod() { 
      return this.validityPeriod != null && !this.validityPeriod.isEmpty();
    }

    /**
     * @param value {@link #validityPeriod} (The time period in which this catalog entry is expected to be active.)
     */
    public CatalogEntry setValidityPeriod(Period value) { 
      this.validityPeriod = value;
      return this;
    }

    /**
     * @return {@link #lastUpdated} (Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.). This is the underlying object with id, value and extensions. The accessor "getLastUpdated" gives direct access to the value
     */
    public DateTimeType getLastUpdatedElement() { 
      if (this.lastUpdated == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CatalogEntry.lastUpdated");
        else if (Configuration.doAutoCreate())
          this.lastUpdated = new DateTimeType(); // bb
      return this.lastUpdated;
    }

    public boolean hasLastUpdatedElement() { 
      return this.lastUpdated != null && !this.lastUpdated.isEmpty();
    }

    public boolean hasLastUpdated() { 
      return this.lastUpdated != null && !this.lastUpdated.isEmpty();
    }

    /**
     * @param value {@link #lastUpdated} (Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.). This is the underlying object with id, value and extensions. The accessor "getLastUpdated" gives direct access to the value
     */
    public CatalogEntry setLastUpdatedElement(DateTimeType value) { 
      this.lastUpdated = value;
      return this;
    }

    /**
     * @return Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.
     */
    public Date getLastUpdated() { 
      return this.lastUpdated == null ? null : this.lastUpdated.getValue();
    }

    /**
     * @param value Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.
     */
    public CatalogEntry setLastUpdated(Date value) { 
      if (value == null)
        this.lastUpdated = null;
      else {
        if (this.lastUpdated == null)
          this.lastUpdated = new DateTimeType();
        this.lastUpdated.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #additionalCharacteristic} (Used for examplefor Out of Formulary, or any specifics.)
     */
    public List<CodeableConcept> getAdditionalCharacteristic() { 
      if (this.additionalCharacteristic == null)
        this.additionalCharacteristic = new ArrayList<CodeableConcept>();
      return this.additionalCharacteristic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CatalogEntry setAdditionalCharacteristic(List<CodeableConcept> theAdditionalCharacteristic) { 
      this.additionalCharacteristic = theAdditionalCharacteristic;
      return this;
    }

    public boolean hasAdditionalCharacteristic() { 
      if (this.additionalCharacteristic == null)
        return false;
      for (CodeableConcept item : this.additionalCharacteristic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addAdditionalCharacteristic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.additionalCharacteristic == null)
        this.additionalCharacteristic = new ArrayList<CodeableConcept>();
      this.additionalCharacteristic.add(t);
      return t;
    }

    public CatalogEntry addAdditionalCharacteristic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.additionalCharacteristic == null)
        this.additionalCharacteristic = new ArrayList<CodeableConcept>();
      this.additionalCharacteristic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #additionalCharacteristic}, creating it if it does not already exist
     */
    public CodeableConcept getAdditionalCharacteristicFirstRep() { 
      if (getAdditionalCharacteristic().isEmpty()) {
        addAdditionalCharacteristic();
      }
      return getAdditionalCharacteristic().get(0);
    }

    /**
     * @return {@link #additionalClassification} (User for example for ATC classification, or.)
     */
    public List<CodeableConcept> getAdditionalClassification() { 
      if (this.additionalClassification == null)
        this.additionalClassification = new ArrayList<CodeableConcept>();
      return this.additionalClassification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CatalogEntry setAdditionalClassification(List<CodeableConcept> theAdditionalClassification) { 
      this.additionalClassification = theAdditionalClassification;
      return this;
    }

    public boolean hasAdditionalClassification() { 
      if (this.additionalClassification == null)
        return false;
      for (CodeableConcept item : this.additionalClassification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addAdditionalClassification() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.additionalClassification == null)
        this.additionalClassification = new ArrayList<CodeableConcept>();
      this.additionalClassification.add(t);
      return t;
    }

    public CatalogEntry addAdditionalClassification(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.additionalClassification == null)
        this.additionalClassification = new ArrayList<CodeableConcept>();
      this.additionalClassification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #additionalClassification}, creating it if it does not already exist
     */
    public CodeableConcept getAdditionalClassificationFirstRep() { 
      if (getAdditionalClassification().isEmpty()) {
        addAdditionalClassification();
      }
      return getAdditionalClassification().get(0);
    }

    /**
     * @return {@link #relatedItem} (Used for example, to point to a substance, or to a device used to administer a medication.)
     */
    public List<CatalogEntryRelatedItemComponent> getRelatedItem() { 
      if (this.relatedItem == null)
        this.relatedItem = new ArrayList<CatalogEntryRelatedItemComponent>();
      return this.relatedItem;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CatalogEntry setRelatedItem(List<CatalogEntryRelatedItemComponent> theRelatedItem) { 
      this.relatedItem = theRelatedItem;
      return this;
    }

    public boolean hasRelatedItem() { 
      if (this.relatedItem == null)
        return false;
      for (CatalogEntryRelatedItemComponent item : this.relatedItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CatalogEntryRelatedItemComponent addRelatedItem() { //3
      CatalogEntryRelatedItemComponent t = new CatalogEntryRelatedItemComponent();
      if (this.relatedItem == null)
        this.relatedItem = new ArrayList<CatalogEntryRelatedItemComponent>();
      this.relatedItem.add(t);
      return t;
    }

    public CatalogEntry addRelatedItem(CatalogEntryRelatedItemComponent t) { //3
      if (t == null)
        return this;
      if (this.relatedItem == null)
        this.relatedItem = new ArrayList<CatalogEntryRelatedItemComponent>();
      this.relatedItem.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedItem}, creating it if it does not already exist
     */
    public CatalogEntryRelatedItemComponent getRelatedItemFirstRep() { 
      if (getRelatedItem().isEmpty()) {
        addRelatedItem();
      }
      return getRelatedItem().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("type", "CodeableConcept", "The type of item - medication, device, service, protocol or other.", 0, 1, type));
        children.add(new Property("purpose", "CodeableConcept", "Whether the entry represents an orderable item, or other.", 0, 1, purpose));
        children.add(new Property("referencedItem", "Reference(Medication|Device|Procedure|CarePlan|Organization|Practitioner|HealthcareService|ServiceDefinition)", "Content of the catalog.", 0, 1, referencedItem));
        children.add(new Property("identifier", "Identifier", "Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.", 0, 1, identifier));
        children.add(new Property("additionalIdentifier", "Identifier", "Used in supporting related concepts, e.g. NDC to RxNorm.", 0, java.lang.Integer.MAX_VALUE, additionalIdentifier));
        children.add(new Property("classification", "Identifier", "Classes of devices, or ATC for medication.", 0, java.lang.Integer.MAX_VALUE, classification));
        children.add(new Property("status", "CodeableConcept", "Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not prescribable.", 0, 1, status));
        children.add(new Property("validityPeriod", "Period", "The time period in which this catalog entry is expected to be active.", 0, 1, validityPeriod));
        children.add(new Property("lastUpdated", "dateTime", "Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.", 0, 1, lastUpdated));
        children.add(new Property("additionalCharacteristic", "CodeableConcept", "Used for examplefor Out of Formulary, or any specifics.", 0, java.lang.Integer.MAX_VALUE, additionalCharacteristic));
        children.add(new Property("additionalClassification", "CodeableConcept", "User for example for ATC classification, or.", 0, java.lang.Integer.MAX_VALUE, additionalClassification));
        children.add(new Property("relatedItem", "", "Used for example, to point to a substance, or to a device used to administer a medication.", 0, java.lang.Integer.MAX_VALUE, relatedItem));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of item - medication, device, service, protocol or other.", 0, 1, type);
        case -220463842: /*purpose*/  return new Property("purpose", "CodeableConcept", "Whether the entry represents an orderable item, or other.", 0, 1, purpose);
        case -1896630996: /*referencedItem*/  return new Property("referencedItem", "Reference(Medication|Device|Procedure|CarePlan|Organization|Practitioner|HealthcareService|ServiceDefinition)", "Content of the catalog.", 0, 1, referencedItem);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.", 0, 1, identifier);
        case 1195162672: /*additionalIdentifier*/  return new Property("additionalIdentifier", "Identifier", "Used in supporting related concepts, e.g. NDC to RxNorm.", 0, java.lang.Integer.MAX_VALUE, additionalIdentifier);
        case 382350310: /*classification*/  return new Property("classification", "Identifier", "Classes of devices, or ATC for medication.", 0, java.lang.Integer.MAX_VALUE, classification);
        case -892481550: /*status*/  return new Property("status", "CodeableConcept", "Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not prescribable.", 0, 1, status);
        case -1434195053: /*validityPeriod*/  return new Property("validityPeriod", "Period", "The time period in which this catalog entry is expected to be active.", 0, 1, validityPeriod);
        case 1649733957: /*lastUpdated*/  return new Property("lastUpdated", "dateTime", "Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last updated.", 0, 1, lastUpdated);
        case -1638369886: /*additionalCharacteristic*/  return new Property("additionalCharacteristic", "CodeableConcept", "Used for examplefor Out of Formulary, or any specifics.", 0, java.lang.Integer.MAX_VALUE, additionalCharacteristic);
        case -1622333459: /*additionalClassification*/  return new Property("additionalClassification", "CodeableConcept", "User for example for ATC classification, or.", 0, java.lang.Integer.MAX_VALUE, additionalClassification);
        case 1112702430: /*relatedItem*/  return new Property("relatedItem", "", "Used for example, to point to a substance, or to a device used to administer a medication.", 0, java.lang.Integer.MAX_VALUE, relatedItem);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // CodeableConcept
        case -1896630996: /*referencedItem*/ return this.referencedItem == null ? new Base[0] : new Base[] {this.referencedItem}; // Reference
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 1195162672: /*additionalIdentifier*/ return this.additionalIdentifier == null ? new Base[0] : this.additionalIdentifier.toArray(new Base[this.additionalIdentifier.size()]); // Identifier
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : this.classification.toArray(new Base[this.classification.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case -1434195053: /*validityPeriod*/ return this.validityPeriod == null ? new Base[0] : new Base[] {this.validityPeriod}; // Period
        case 1649733957: /*lastUpdated*/ return this.lastUpdated == null ? new Base[0] : new Base[] {this.lastUpdated}; // DateTimeType
        case -1638369886: /*additionalCharacteristic*/ return this.additionalCharacteristic == null ? new Base[0] : this.additionalCharacteristic.toArray(new Base[this.additionalCharacteristic.size()]); // CodeableConcept
        case -1622333459: /*additionalClassification*/ return this.additionalClassification == null ? new Base[0] : this.additionalClassification.toArray(new Base[this.additionalClassification.size()]); // CodeableConcept
        case 1112702430: /*relatedItem*/ return this.relatedItem == null ? new Base[0] : this.relatedItem.toArray(new Base[this.relatedItem.size()]); // CatalogEntryRelatedItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1896630996: // referencedItem
          this.referencedItem = castToReference(value); // Reference
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 1195162672: // additionalIdentifier
          this.getAdditionalIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 382350310: // classification
          this.getClassification().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1434195053: // validityPeriod
          this.validityPeriod = castToPeriod(value); // Period
          return value;
        case 1649733957: // lastUpdated
          this.lastUpdated = castToDateTime(value); // DateTimeType
          return value;
        case -1638369886: // additionalCharacteristic
          this.getAdditionalCharacteristic().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1622333459: // additionalClassification
          this.getAdditionalClassification().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1112702430: // relatedItem
          this.getRelatedItem().add((CatalogEntryRelatedItemComponent) value); // CatalogEntryRelatedItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("purpose")) {
          this.purpose = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("referencedItem")) {
          this.referencedItem = castToReference(value); // Reference
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("additionalIdentifier")) {
          this.getAdditionalIdentifier().add(castToIdentifier(value));
        } else if (name.equals("classification")) {
          this.getClassification().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("validityPeriod")) {
          this.validityPeriod = castToPeriod(value); // Period
        } else if (name.equals("lastUpdated")) {
          this.lastUpdated = castToDateTime(value); // DateTimeType
        } else if (name.equals("additionalCharacteristic")) {
          this.getAdditionalCharacteristic().add(castToCodeableConcept(value));
        } else if (name.equals("additionalClassification")) {
          this.getAdditionalClassification().add(castToCodeableConcept(value));
        } else if (name.equals("relatedItem")) {
          this.getRelatedItem().add((CatalogEntryRelatedItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -220463842:  return getPurpose(); 
        case -1896630996:  return getReferencedItem(); 
        case -1618432855:  return getIdentifier(); 
        case 1195162672:  return addAdditionalIdentifier(); 
        case 382350310:  return addClassification(); 
        case -892481550:  return getStatus(); 
        case -1434195053:  return getValidityPeriod(); 
        case 1649733957:  return getLastUpdatedElement();
        case -1638369886:  return addAdditionalCharacteristic(); 
        case -1622333459:  return addAdditionalClassification(); 
        case 1112702430:  return addRelatedItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"CodeableConcept"};
        case -1896630996: /*referencedItem*/ return new String[] {"Reference"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 1195162672: /*additionalIdentifier*/ return new String[] {"Identifier"};
        case 382350310: /*classification*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case -1434195053: /*validityPeriod*/ return new String[] {"Period"};
        case 1649733957: /*lastUpdated*/ return new String[] {"dateTime"};
        case -1638369886: /*additionalCharacteristic*/ return new String[] {"CodeableConcept"};
        case -1622333459: /*additionalClassification*/ return new String[] {"CodeableConcept"};
        case 1112702430: /*relatedItem*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("purpose")) {
          this.purpose = new CodeableConcept();
          return this.purpose;
        }
        else if (name.equals("referencedItem")) {
          this.referencedItem = new Reference();
          return this.referencedItem;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("additionalIdentifier")) {
          return addAdditionalIdentifier();
        }
        else if (name.equals("classification")) {
          return addClassification();
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("validityPeriod")) {
          this.validityPeriod = new Period();
          return this.validityPeriod;
        }
        else if (name.equals("lastUpdated")) {
          throw new FHIRException("Cannot call addChild on a primitive type CatalogEntry.lastUpdated");
        }
        else if (name.equals("additionalCharacteristic")) {
          return addAdditionalCharacteristic();
        }
        else if (name.equals("additionalClassification")) {
          return addAdditionalClassification();
        }
        else if (name.equals("relatedItem")) {
          return addRelatedItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CatalogEntry";

  }

      public CatalogEntry copy() {
        CatalogEntry dst = new CatalogEntry();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.referencedItem = referencedItem == null ? null : referencedItem.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        if (additionalIdentifier != null) {
          dst.additionalIdentifier = new ArrayList<Identifier>();
          for (Identifier i : additionalIdentifier)
            dst.additionalIdentifier.add(i.copy());
        };
        if (classification != null) {
          dst.classification = new ArrayList<Identifier>();
          for (Identifier i : classification)
            dst.classification.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.validityPeriod = validityPeriod == null ? null : validityPeriod.copy();
        dst.lastUpdated = lastUpdated == null ? null : lastUpdated.copy();
        if (additionalCharacteristic != null) {
          dst.additionalCharacteristic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : additionalCharacteristic)
            dst.additionalCharacteristic.add(i.copy());
        };
        if (additionalClassification != null) {
          dst.additionalClassification = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : additionalClassification)
            dst.additionalClassification.add(i.copy());
        };
        if (relatedItem != null) {
          dst.relatedItem = new ArrayList<CatalogEntryRelatedItemComponent>();
          for (CatalogEntryRelatedItemComponent i : relatedItem)
            dst.relatedItem.add(i.copy());
        };
        return dst;
      }

      protected CatalogEntry typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CatalogEntry))
          return false;
        CatalogEntry o = (CatalogEntry) other;
        return compareDeep(type, o.type, true) && compareDeep(purpose, o.purpose, true) && compareDeep(referencedItem, o.referencedItem, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(additionalIdentifier, o.additionalIdentifier, true)
           && compareDeep(classification, o.classification, true) && compareDeep(status, o.status, true) && compareDeep(validityPeriod, o.validityPeriod, true)
           && compareDeep(lastUpdated, o.lastUpdated, true) && compareDeep(additionalCharacteristic, o.additionalCharacteristic, true)
           && compareDeep(additionalClassification, o.additionalClassification, true) && compareDeep(relatedItem, o.relatedItem, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CatalogEntry))
          return false;
        CatalogEntry o = (CatalogEntry) other;
        return compareValues(lastUpdated, o.lastUpdated, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, purpose, referencedItem
          , identifier, additionalIdentifier, classification, status, validityPeriod, lastUpdated
          , additionalCharacteristic, additionalClassification, relatedItem);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CatalogEntry;
   }


}

