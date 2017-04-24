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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Identifies two or more records (resource instances) that are referring to the same real-world "occurrence".
 */
@ResourceDef(name="Linkage", profile="http://hl7.org/fhir/Profile/Linkage")
public class Linkage extends DomainResource {

    public enum LinkageType {
        /**
         * The record represents the "source of truth" (from the perspective of this Linkage resource) for the underlying event/condition/etc.
         */
        SOURCE, 
        /**
         * The record represents the alternative view of the underlying event/condition/etc.  The record may still be actively maintained, even though it is not considered to be the source of truth.
         */
        ALTERNATE, 
        /**
         * The record represents an obsolete record of the underlyng event/condition/etc.  It is not expected to be actively maintained.
         */
        HISTORICAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static LinkageType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return SOURCE;
        if ("alternate".equals(codeString))
          return ALTERNATE;
        if ("historical".equals(codeString))
          return HISTORICAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown LinkageType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SOURCE: return "source";
            case ALTERNATE: return "alternate";
            case HISTORICAL: return "historical";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SOURCE: return "http://hl7.org/fhir/linkage-type";
            case ALTERNATE: return "http://hl7.org/fhir/linkage-type";
            case HISTORICAL: return "http://hl7.org/fhir/linkage-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SOURCE: return "The record represents the \"source of truth\" (from the perspective of this Linkage resource) for the underlying event/condition/etc.";
            case ALTERNATE: return "The record represents the alternative view of the underlying event/condition/etc.  The record may still be actively maintained, even though it is not considered to be the source of truth.";
            case HISTORICAL: return "The record represents an obsolete record of the underlyng event/condition/etc.  It is not expected to be actively maintained.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SOURCE: return "Source of truth";
            case ALTERNATE: return "Alternate record";
            case HISTORICAL: return "Historical/obsolete record";
            default: return "?";
          }
        }
    }

  public static class LinkageTypeEnumFactory implements EnumFactory<LinkageType> {
    public LinkageType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return LinkageType.SOURCE;
        if ("alternate".equals(codeString))
          return LinkageType.ALTERNATE;
        if ("historical".equals(codeString))
          return LinkageType.HISTORICAL;
        throw new IllegalArgumentException("Unknown LinkageType code '"+codeString+"'");
        }
        public Enumeration<LinkageType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<LinkageType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("source".equals(codeString))
          return new Enumeration<LinkageType>(this, LinkageType.SOURCE);
        if ("alternate".equals(codeString))
          return new Enumeration<LinkageType>(this, LinkageType.ALTERNATE);
        if ("historical".equals(codeString))
          return new Enumeration<LinkageType>(this, LinkageType.HISTORICAL);
        throw new FHIRException("Unknown LinkageType code '"+codeString+"'");
        }
    public String toCode(LinkageType code) {
      if (code == LinkageType.SOURCE)
        return "source";
      if (code == LinkageType.ALTERNATE)
        return "alternate";
      if (code == LinkageType.HISTORICAL)
        return "historical";
      return "?";
      }
    public String toSystem(LinkageType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class LinkageItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Distinguishes which item is "source of truth" (if any) and which items are no longer considered to be current representations.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="source | alternate | historical", formalDefinition="Distinguishes which item is \"source of truth\" (if any) and which items are no longer considered to be current representations." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/linkage-type")
        protected Enumeration<LinkageType> type;

        /**
         * The resource instance being linked as part of the group.
         */
        @Child(name = "resource", type = {Reference.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Resource being linked", formalDefinition="The resource instance being linked as part of the group." )
        protected Reference resource;

        private static final long serialVersionUID = 527428511L;

    /**
     * Constructor
     */
      public LinkageItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LinkageItemComponent(Enumeration<LinkageType> type, Reference resource) {
        super();
        this.type = type;
        this.resource = resource;
      }

        /**
         * @return {@link #type} (Distinguishes which item is "source of truth" (if any) and which items are no longer considered to be current representations.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<LinkageType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LinkageItemComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<LinkageType>(new LinkageTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Distinguishes which item is "source of truth" (if any) and which items are no longer considered to be current representations.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public LinkageItemComponent setTypeElement(Enumeration<LinkageType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Distinguishes which item is "source of truth" (if any) and which items are no longer considered to be current representations.
         */
        public LinkageType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Distinguishes which item is "source of truth" (if any) and which items are no longer considered to be current representations.
         */
        public LinkageItemComponent setType(LinkageType value) { 
            if (this.type == null)
              this.type = new Enumeration<LinkageType>(new LinkageTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #resource} (The resource instance being linked as part of the group.)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LinkageItemComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The resource instance being linked as part of the group.)
         */
        public LinkageItemComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Distinguishes which item is \"source of truth\" (if any) and which items are no longer considered to be current representations.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("resource", "Reference", "The resource instance being linked as part of the group.", 0, java.lang.Integer.MAX_VALUE, resource));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<LinkageType>
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new LinkageTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<LinkageType>
          return value;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new LinkageTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<LinkageType>
        } else if (name.equals("resource")) {
          this.resource = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -341064690:  return getResource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -341064690: /*resource*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Linkage.type");
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else
          return super.addChild(name);
      }

      public LinkageItemComponent copy() {
        LinkageItemComponent dst = new LinkageItemComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.resource = resource == null ? null : resource.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LinkageItemComponent))
          return false;
        LinkageItemComponent o = (LinkageItemComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(resource, o.resource, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LinkageItemComponent))
          return false;
        LinkageItemComponent o = (LinkageItemComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, resource);
      }

  public String fhirType() {
    return "Linkage.item";

  }

  }

    /**
     * Indicates whether the asserted set of linkages are considered to be "in effect".
     */
    @Child(name = "active", type = {BooleanType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether this linkage assertion is active or not", formalDefinition="Indicates whether the asserted set of linkages are considered to be \"in effect\"." )
    protected BooleanType active;

    /**
     * Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.
     */
    @Child(name = "author", type = {Practitioner.class, Organization.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is responsible for linkages", formalDefinition="Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.)
     */
    protected Resource authorTarget;

    /**
     * Identifies one of the records that is considered to refer to the same real-world occurrence as well as how the items hould be evaluated within the collection of linked items.
     */
    @Child(name = "item", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Item to be linked", formalDefinition="Identifies one of the records that is considered to refer to the same real-world occurrence as well as how the items hould be evaluated within the collection of linked items." )
    protected List<LinkageItemComponent> item;

    private static final long serialVersionUID = 25900306L;

  /**
   * Constructor
   */
    public Linkage() {
      super();
    }

    /**
     * @return {@link #active} (Indicates whether the asserted set of linkages are considered to be "in effect".). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Linkage.active");
        else if (Configuration.doAutoCreate())
          this.active = new BooleanType(); // bb
      return this.active;
    }

    public boolean hasActiveElement() { 
      return this.active != null && !this.active.isEmpty();
    }

    public boolean hasActive() { 
      return this.active != null && !this.active.isEmpty();
    }

    /**
     * @param value {@link #active} (Indicates whether the asserted set of linkages are considered to be "in effect".). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public Linkage setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Indicates whether the asserted set of linkages are considered to be "in effect".
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Indicates whether the asserted set of linkages are considered to be "in effect".
     */
    public Linkage setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #author} (Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Linkage.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.)
     */
    public Linkage setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.)
     */
    public Linkage setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #item} (Identifies one of the records that is considered to refer to the same real-world occurrence as well as how the items hould be evaluated within the collection of linked items.)
     */
    public List<LinkageItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<LinkageItemComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Linkage setItem(List<LinkageItemComponent> theItem) { 
      this.item = theItem;
      return this;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (LinkageItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public LinkageItemComponent addItem() { //3
      LinkageItemComponent t = new LinkageItemComponent();
      if (this.item == null)
        this.item = new ArrayList<LinkageItemComponent>();
      this.item.add(t);
      return t;
    }

    public Linkage addItem(LinkageItemComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<LinkageItemComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
     */
    public LinkageItemComponent getItemFirstRep() { 
      if (getItem().isEmpty()) {
        addItem();
      }
      return getItem().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("active", "boolean", "Indicates whether the asserted set of linkages are considered to be \"in effect\".", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("author", "Reference(Practitioner|Organization)", "Identifies the user or organization responsible for asserting the linkages and who establishes the context for evaluating the nature of each linkage.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("item", "", "Identifies one of the records that is considered to refer to the same real-world occurrence as well as how the items hould be evaluated within the collection of linked items.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // LinkageItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950650: // active
          this.active = castToBoolean(value); // BooleanType
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        case 3242771: // item
          this.getItem().add((LinkageItemComponent) value); // LinkageItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("active")) {
          this.active = castToBoolean(value); // BooleanType
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else if (name.equals("item")) {
          this.getItem().add((LinkageItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950650:  return getActiveElement();
        case -1406328437:  return getAuthor(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950650: /*active*/ return new String[] {"boolean"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case 3242771: /*item*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("active")) {
          throw new FHIRException("Cannot call addChild on a primitive type Linkage.active");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Linkage";

  }

      public Linkage copy() {
        Linkage dst = new Linkage();
        copyValues(dst);
        dst.active = active == null ? null : active.copy();
        dst.author = author == null ? null : author.copy();
        if (item != null) {
          dst.item = new ArrayList<LinkageItemComponent>();
          for (LinkageItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected Linkage typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Linkage))
          return false;
        Linkage o = (Linkage) other;
        return compareDeep(active, o.active, true) && compareDeep(author, o.author, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Linkage))
          return false;
        Linkage o = (Linkage) other;
        return compareValues(active, o.active, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(active, author, item);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Linkage;
   }

 /**
   * Search parameter: <b>item</b>
   * <p>
   * Description: <b>Matches on any item in the Linkage</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Linkage.item.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="item", path="Linkage.item.resource", description="Matches on any item in the Linkage", type="reference" )
  public static final String SP_ITEM = "item";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>item</b>
   * <p>
   * Description: <b>Matches on any item in the Linkage</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Linkage.item.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ITEM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ITEM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Linkage:item</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ITEM = new ca.uhn.fhir.model.api.Include("Linkage:item").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>Author of the Linkage</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Linkage.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="Linkage.author", description="Author of the Linkage", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>Author of the Linkage</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Linkage.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Linkage:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("Linkage:author").toLocked();

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Matches on any item in the Linkage with a type of 'source'</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Linkage.item.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="Linkage.item.resource", description="Matches on any item in the Linkage with a type of 'source'", type="reference" )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>Matches on any item in the Linkage with a type of 'source'</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Linkage.item.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Linkage:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("Linkage:source").toLocked();


}

