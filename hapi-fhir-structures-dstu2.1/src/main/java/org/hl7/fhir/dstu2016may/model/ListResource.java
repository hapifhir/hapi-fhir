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
 * A set of information summarized from a list of other resources.
 */
@ResourceDef(name="List", profile="http://hl7.org/fhir/Profile/ListResource")
public class ListResource extends DomainResource {

    public enum ListStatus {
        /**
         * The list is considered to be an active part of the patient's record.
         */
        CURRENT, 
        /**
         * The list is "old" and should no longer be considered accurate or relevant.
         */
        RETIRED, 
        /**
         * The list was never accurate.  It is retained for medico-legal purposes only.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return CURRENT;
        if ("retired".equals(codeString))
          return RETIRED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown ListStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CURRENT: return "current";
            case RETIRED: return "retired";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CURRENT: return "http://hl7.org/fhir/list-status";
            case RETIRED: return "http://hl7.org/fhir/list-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/list-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CURRENT: return "The list is considered to be an active part of the patient's record.";
            case RETIRED: return "The list is \"old\" and should no longer be considered accurate or relevant.";
            case ENTEREDINERROR: return "The list was never accurate.  It is retained for medico-legal purposes only.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CURRENT: return "Current";
            case RETIRED: return "Retired";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class ListStatusEnumFactory implements EnumFactory<ListStatus> {
    public ListStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return ListStatus.CURRENT;
        if ("retired".equals(codeString))
          return ListStatus.RETIRED;
        if ("entered-in-error".equals(codeString))
          return ListStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ListStatus code '"+codeString+"'");
        }
        public Enumeration<ListStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("current".equals(codeString))
          return new Enumeration<ListStatus>(this, ListStatus.CURRENT);
        if ("retired".equals(codeString))
          return new Enumeration<ListStatus>(this, ListStatus.RETIRED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ListStatus>(this, ListStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ListStatus code '"+codeString+"'");
        }
    public String toCode(ListStatus code) {
      if (code == ListStatus.CURRENT)
        return "current";
      if (code == ListStatus.RETIRED)
        return "retired";
      if (code == ListStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ListStatus code) {
      return code.getSystem();
      }
    }

    public enum ListMode {
        /**
         * This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes
         */
        WORKING, 
        /**
         * This list was prepared as a snapshot. It should not be assumed to be current
         */
        SNAPSHOT, 
        /**
         * A list that indicates where changes have been made or recommended
         */
        CHANGES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("working".equals(codeString))
          return WORKING;
        if ("snapshot".equals(codeString))
          return SNAPSHOT;
        if ("changes".equals(codeString))
          return CHANGES;
        throw new FHIRException("Unknown ListMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WORKING: return "working";
            case SNAPSHOT: return "snapshot";
            case CHANGES: return "changes";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case WORKING: return "http://hl7.org/fhir/list-mode";
            case SNAPSHOT: return "http://hl7.org/fhir/list-mode";
            case CHANGES: return "http://hl7.org/fhir/list-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case WORKING: return "This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes";
            case SNAPSHOT: return "This list was prepared as a snapshot. It should not be assumed to be current";
            case CHANGES: return "A list that indicates where changes have been made or recommended";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WORKING: return "Working List";
            case SNAPSHOT: return "Snapshot List";
            case CHANGES: return "Change List";
            default: return "?";
          }
        }
    }

  public static class ListModeEnumFactory implements EnumFactory<ListMode> {
    public ListMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("working".equals(codeString))
          return ListMode.WORKING;
        if ("snapshot".equals(codeString))
          return ListMode.SNAPSHOT;
        if ("changes".equals(codeString))
          return ListMode.CHANGES;
        throw new IllegalArgumentException("Unknown ListMode code '"+codeString+"'");
        }
        public Enumeration<ListMode> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("working".equals(codeString))
          return new Enumeration<ListMode>(this, ListMode.WORKING);
        if ("snapshot".equals(codeString))
          return new Enumeration<ListMode>(this, ListMode.SNAPSHOT);
        if ("changes".equals(codeString))
          return new Enumeration<ListMode>(this, ListMode.CHANGES);
        throw new FHIRException("Unknown ListMode code '"+codeString+"'");
        }
    public String toCode(ListMode code) {
      if (code == ListMode.WORKING)
        return "working";
      if (code == ListMode.SNAPSHOT)
        return "snapshot";
      if (code == ListMode.CHANGES)
        return "changes";
      return "?";
      }
    public String toSystem(ListMode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ListEntryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The flag allows the system constructing the list to indicate the role and significance of the item in the list.
         */
        @Child(name = "flag", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Status/Workflow information about this item", formalDefinition="The flag allows the system constructing the list to indicate the role and significance of the item in the list." )
        protected CodeableConcept flag;

        /**
         * True if this item is marked as deleted in the list.
         */
        @Child(name = "deleted", type = {BooleanType.class}, order=2, min=0, max=1, modifier=true, summary=false)
        @Description(shortDefinition="If this item is actually marked as deleted", formalDefinition="True if this item is marked as deleted in the list." )
        protected BooleanType deleted;

        /**
         * When this item was added to the list.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When item added to list", formalDefinition="When this item was added to the list." )
        protected DateTimeType date;

        /**
         * A reference to the actual resource from which data was derived.
         */
        @Child(name = "item", type = {}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Actual entry", formalDefinition="A reference to the actual resource from which data was derived." )
        protected Reference item;

        /**
         * The actual object that is the target of the reference (A reference to the actual resource from which data was derived.)
         */
        protected Resource itemTarget;

        private static final long serialVersionUID = -758164425L;

    /**
     * Constructor
     */
      public ListEntryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ListEntryComponent(Reference item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #flag} (The flag allows the system constructing the list to indicate the role and significance of the item in the list.)
         */
        public CodeableConcept getFlag() { 
          if (this.flag == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ListEntryComponent.flag");
            else if (Configuration.doAutoCreate())
              this.flag = new CodeableConcept(); // cc
          return this.flag;
        }

        public boolean hasFlag() { 
          return this.flag != null && !this.flag.isEmpty();
        }

        /**
         * @param value {@link #flag} (The flag allows the system constructing the list to indicate the role and significance of the item in the list.)
         */
        public ListEntryComponent setFlag(CodeableConcept value) { 
          this.flag = value;
          return this;
        }

        /**
         * @return {@link #deleted} (True if this item is marked as deleted in the list.). This is the underlying object with id, value and extensions. The accessor "getDeleted" gives direct access to the value
         */
        public BooleanType getDeletedElement() { 
          if (this.deleted == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ListEntryComponent.deleted");
            else if (Configuration.doAutoCreate())
              this.deleted = new BooleanType(); // bb
          return this.deleted;
        }

        public boolean hasDeletedElement() { 
          return this.deleted != null && !this.deleted.isEmpty();
        }

        public boolean hasDeleted() { 
          return this.deleted != null && !this.deleted.isEmpty();
        }

        /**
         * @param value {@link #deleted} (True if this item is marked as deleted in the list.). This is the underlying object with id, value and extensions. The accessor "getDeleted" gives direct access to the value
         */
        public ListEntryComponent setDeletedElement(BooleanType value) { 
          this.deleted = value;
          return this;
        }

        /**
         * @return True if this item is marked as deleted in the list.
         */
        public boolean getDeleted() { 
          return this.deleted == null || this.deleted.isEmpty() ? false : this.deleted.getValue();
        }

        /**
         * @param value True if this item is marked as deleted in the list.
         */
        public ListEntryComponent setDeleted(boolean value) { 
            if (this.deleted == null)
              this.deleted = new BooleanType();
            this.deleted.setValue(value);
          return this;
        }

        /**
         * @return {@link #date} (When this item was added to the list.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ListEntryComponent.date");
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
         * @param value {@link #date} (When this item was added to the list.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public ListEntryComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return When this item was added to the list.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value When this item was added to the list.
         */
        public ListEntryComponent setDate(Date value) { 
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
         * @return {@link #item} (A reference to the actual resource from which data was derived.)
         */
        public Reference getItem() { 
          if (this.item == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ListEntryComponent.item");
            else if (Configuration.doAutoCreate())
              this.item = new Reference(); // cc
          return this.item;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (A reference to the actual resource from which data was derived.)
         */
        public ListEntryComponent setItem(Reference value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #item} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the actual resource from which data was derived.)
         */
        public Resource getItemTarget() { 
          return this.itemTarget;
        }

        /**
         * @param value {@link #item} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the actual resource from which data was derived.)
         */
        public ListEntryComponent setItemTarget(Resource value) { 
          this.itemTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("flag", "CodeableConcept", "The flag allows the system constructing the list to indicate the role and significance of the item in the list.", 0, java.lang.Integer.MAX_VALUE, flag));
          childrenList.add(new Property("deleted", "boolean", "True if this item is marked as deleted in the list.", 0, java.lang.Integer.MAX_VALUE, deleted));
          childrenList.add(new Property("date", "dateTime", "When this item was added to the list.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("item", "Reference(Any)", "A reference to the actual resource from which data was derived.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3145580: /*flag*/ return this.flag == null ? new Base[0] : new Base[] {this.flag}; // CodeableConcept
        case 1550463001: /*deleted*/ return this.deleted == null ? new Base[0] : new Base[] {this.deleted}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3145580: // flag
          this.flag = castToCodeableConcept(value); // CodeableConcept
          break;
        case 1550463001: // deleted
          this.deleted = castToBoolean(value); // BooleanType
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case 3242771: // item
          this.item = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("flag"))
          this.flag = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("deleted"))
          this.deleted = castToBoolean(value); // BooleanType
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("item"))
          this.item = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3145580:  return getFlag(); // CodeableConcept
        case 1550463001: throw new FHIRException("Cannot make property deleted as it is not a complex type"); // BooleanType
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case 3242771:  return getItem(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("flag")) {
          this.flag = new CodeableConcept();
          return this.flag;
        }
        else if (name.equals("deleted")) {
          throw new FHIRException("Cannot call addChild on a primitive type ListResource.deleted");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ListResource.date");
        }
        else if (name.equals("item")) {
          this.item = new Reference();
          return this.item;
        }
        else
          return super.addChild(name);
      }

      public ListEntryComponent copy() {
        ListEntryComponent dst = new ListEntryComponent();
        copyValues(dst);
        dst.flag = flag == null ? null : flag.copy();
        dst.deleted = deleted == null ? null : deleted.copy();
        dst.date = date == null ? null : date.copy();
        dst.item = item == null ? null : item.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ListEntryComponent))
          return false;
        ListEntryComponent o = (ListEntryComponent) other;
        return compareDeep(flag, o.flag, true) && compareDeep(deleted, o.deleted, true) && compareDeep(date, o.date, true)
           && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ListEntryComponent))
          return false;
        ListEntryComponent o = (ListEntryComponent) other;
        return compareValues(deleted, o.deleted, true) && compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (flag == null || flag.isEmpty()) && (deleted == null || deleted.isEmpty())
           && (date == null || date.isEmpty()) && (item == null || item.isEmpty());
      }

  public String fhirType() {
    return "List.entry";

  }

  }

    /**
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier for the List assigned for business purposes outside the context of FHIR." )
    protected List<Identifier> identifier;

    /**
     * Indicates the current state of this list.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="current | retired | entered-in-error", formalDefinition="Indicates the current state of this list." )
    protected Enumeration<ListStatus> status;

    /**
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
     */
    @Child(name = "mode", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="working | snapshot | changes", formalDefinition="How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted." )
    protected Enumeration<ListMode> mode;

    /**
     * A label for the list assigned by the author.
     */
    @Child(name = "title", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Descriptive name for the list", formalDefinition="A label for the list assigned by the author." )
    protected StringType title;

    /**
     * This code defines the purpose of the list - why it was created.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What the purpose of this list is", formalDefinition="This code defines the purpose of the list - why it was created." )
    protected CodeableConcept code;

    /**
     * The common subject (or patient) of the resources that are in the list, if there is one.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If all resources have the same subject", formalDefinition="The common subject (or patient) of the resources that are in the list, if there is one." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter that is the context in which this list was created.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Context in which list created", formalDefinition="The encounter that is the context in which this list was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter that is the context in which this list was created.)
     */
    protected Encounter encounterTarget;

    /**
     * The date that the list was prepared.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the list was prepared", formalDefinition="The date that the list was prepared." )
    protected DateTimeType date;

    /**
     * The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.
     */
    @Child(name = "source", type = {Practitioner.class, Patient.class, Device.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what defined the list contents (aka Author)", formalDefinition="The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    protected Resource sourceTarget;

    /**
     * What order applies to the items in the list.
     */
    @Child(name = "orderedBy", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What order the list has", formalDefinition="What order applies to the items in the list." )
    protected CodeableConcept orderedBy;

    /**
     * Comments that apply to the overall list.
     */
    @Child(name = "note", type = {Annotation.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the list", formalDefinition="Comments that apply to the overall list." )
    protected List<Annotation> note;

    /**
     * Entries in this list.
     */
    @Child(name = "entry", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Entries in the list", formalDefinition="Entries in this list." )
    protected List<ListEntryComponent> entry;

    /**
     * If the list is empty, why the list is empty.
     */
    @Child(name = "emptyReason", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why list is empty", formalDefinition="If the list is empty, why the list is empty." )
    protected CodeableConcept emptyReason;

    private static final long serialVersionUID = 2071342704L;

  /**
   * Constructor
   */
    public ListResource() {
      super();
    }

  /**
   * Constructor
   */
    public ListResource(Enumeration<ListStatus> status, Enumeration<ListMode> mode) {
      super();
      this.status = status;
      this.mode = mode;
    }

    /**
     * @return {@link #identifier} (Identifier for the List assigned for business purposes outside the context of FHIR.)
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
     * @return {@link #identifier} (Identifier for the List assigned for business purposes outside the context of FHIR.)
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
    public ListResource addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (Indicates the current state of this list.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ListStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ListStatus>(new ListStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the current state of this list.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ListResource setStatusElement(Enumeration<ListStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the current state of this list.
     */
    public ListStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the current state of this list.
     */
    public ListResource setStatus(ListStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ListStatus>(new ListStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #mode} (How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public Enumeration<ListMode> getModeElement() { 
      if (this.mode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.mode");
        else if (Configuration.doAutoCreate())
          this.mode = new Enumeration<ListMode>(new ListModeEnumFactory()); // bb
      return this.mode;
    }

    public boolean hasModeElement() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    public boolean hasMode() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    /**
     * @param value {@link #mode} (How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public ListResource setModeElement(Enumeration<ListMode> value) { 
      this.mode = value;
      return this;
    }

    /**
     * @return How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
     */
    public ListMode getMode() { 
      return this.mode == null ? null : this.mode.getValue();
    }

    /**
     * @param value How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
     */
    public ListResource setMode(ListMode value) { 
        if (this.mode == null)
          this.mode = new Enumeration<ListMode>(new ListModeEnumFactory());
        this.mode.setValue(value);
      return this;
    }

    /**
     * @return {@link #title} (A label for the list assigned by the author.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.title");
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
     * @param value {@link #title} (A label for the list assigned by the author.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ListResource setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A label for the list assigned by the author.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A label for the list assigned by the author.
     */
    public ListResource setTitle(String value) { 
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
     * @return {@link #code} (This code defines the purpose of the list - why it was created.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (This code defines the purpose of the list - why it was created.)
     */
    public ListResource setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    public ListResource setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    public ListResource setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter that is the context in which this list was created.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter that is the context in which this list was created.)
     */
    public ListResource setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter that is the context in which this list was created.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter that is the context in which this list was created.)
     */
    public ListResource setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date that the list was prepared.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.date");
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
     * @param value {@link #date} (The date that the list was prepared.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ListResource setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the list was prepared.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the list was prepared.
     */
    public ListResource setDate(Date value) { 
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
     * @return {@link #source} (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    public ListResource setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    public Resource getSourceTarget() { 
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    public ListResource setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #orderedBy} (What order applies to the items in the list.)
     */
    public CodeableConcept getOrderedBy() { 
      if (this.orderedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.orderedBy");
        else if (Configuration.doAutoCreate())
          this.orderedBy = new CodeableConcept(); // cc
      return this.orderedBy;
    }

    public boolean hasOrderedBy() { 
      return this.orderedBy != null && !this.orderedBy.isEmpty();
    }

    /**
     * @param value {@link #orderedBy} (What order applies to the items in the list.)
     */
    public ListResource setOrderedBy(CodeableConcept value) { 
      this.orderedBy = value;
      return this;
    }

    /**
     * @return {@link #note} (Comments that apply to the overall list.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Comments that apply to the overall list.)
     */
    // syntactic sugar
    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    // syntactic sugar
    public ListResource addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return {@link #entry} (Entries in this list.)
     */
    public List<ListEntryComponent> getEntry() { 
      if (this.entry == null)
        this.entry = new ArrayList<ListEntryComponent>();
      return this.entry;
    }

    public boolean hasEntry() { 
      if (this.entry == null)
        return false;
      for (ListEntryComponent item : this.entry)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #entry} (Entries in this list.)
     */
    // syntactic sugar
    public ListEntryComponent addEntry() { //3
      ListEntryComponent t = new ListEntryComponent();
      if (this.entry == null)
        this.entry = new ArrayList<ListEntryComponent>();
      this.entry.add(t);
      return t;
    }

    // syntactic sugar
    public ListResource addEntry(ListEntryComponent t) { //3
      if (t == null)
        return this;
      if (this.entry == null)
        this.entry = new ArrayList<ListEntryComponent>();
      this.entry.add(t);
      return this;
    }

    /**
     * @return {@link #emptyReason} (If the list is empty, why the list is empty.)
     */
    public CodeableConcept getEmptyReason() { 
      if (this.emptyReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ListResource.emptyReason");
        else if (Configuration.doAutoCreate())
          this.emptyReason = new CodeableConcept(); // cc
      return this.emptyReason;
    }

    public boolean hasEmptyReason() { 
      return this.emptyReason != null && !this.emptyReason.isEmpty();
    }

    /**
     * @param value {@link #emptyReason} (If the list is empty, why the list is empty.)
     */
    public ListResource setEmptyReason(CodeableConcept value) { 
      this.emptyReason = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for the List assigned for business purposes outside the context of FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Indicates the current state of this list.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("mode", "code", "How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.", 0, java.lang.Integer.MAX_VALUE, mode));
        childrenList.add(new Property("title", "string", "A label for the list assigned by the author.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("code", "CodeableConcept", "This code defines the purpose of the list - why it was created.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The common subject (or patient) of the resources that are in the list, if there is one.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter that is the context in which this list was created.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("date", "dateTime", "The date that the list was prepared.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("source", "Reference(Practitioner|Patient|Device)", "The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("orderedBy", "CodeableConcept", "What order applies to the items in the list.", 0, java.lang.Integer.MAX_VALUE, orderedBy));
        childrenList.add(new Property("note", "Annotation", "Comments that apply to the overall list.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("entry", "", "Entries in this list.", 0, java.lang.Integer.MAX_VALUE, entry));
        childrenList.add(new Property("emptyReason", "CodeableConcept", "If the list is empty, why the list is empty.", 0, java.lang.Integer.MAX_VALUE, emptyReason));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ListStatus>
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<ListMode>
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case -391079516: /*orderedBy*/ return this.orderedBy == null ? new Base[0] : new Base[] {this.orderedBy}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 96667762: /*entry*/ return this.entry == null ? new Base[0] : this.entry.toArray(new Base[this.entry.size()]); // ListEntryComponent
        case 1140135409: /*emptyReason*/ return this.emptyReason == null ? new Base[0] : new Base[] {this.emptyReason}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = new ListStatusEnumFactory().fromType(value); // Enumeration<ListStatus>
          break;
        case 3357091: // mode
          this.mode = new ListModeEnumFactory().fromType(value); // Enumeration<ListMode>
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          break;
        case -391079516: // orderedBy
          this.orderedBy = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case 96667762: // entry
          this.getEntry().add((ListEntryComponent) value); // ListEntryComponent
          break;
        case 1140135409: // emptyReason
          this.emptyReason = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new ListStatusEnumFactory().fromType(value); // Enumeration<ListStatus>
        else if (name.equals("mode"))
          this.mode = new ListModeEnumFactory().fromType(value); // Enumeration<ListMode>
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("source"))
          this.source = castToReference(value); // Reference
        else if (name.equals("orderedBy"))
          this.orderedBy = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("entry"))
          this.getEntry().add((ListEntryComponent) value);
        else if (name.equals("emptyReason"))
          this.emptyReason = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ListStatus>
        case 3357091: throw new FHIRException("Cannot make property mode as it is not a complex type"); // Enumeration<ListMode>
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case 3059181:  return getCode(); // CodeableConcept
        case -1867885268:  return getSubject(); // Reference
        case 1524132147:  return getEncounter(); // Reference
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -896505829:  return getSource(); // Reference
        case -391079516:  return getOrderedBy(); // CodeableConcept
        case 3387378:  return addNote(); // Annotation
        case 96667762:  return addEntry(); // ListEntryComponent
        case 1140135409:  return getEmptyReason(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ListResource.status");
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type ListResource.mode");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ListResource.title");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ListResource.date");
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("orderedBy")) {
          this.orderedBy = new CodeableConcept();
          return this.orderedBy;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("entry")) {
          return addEntry();
        }
        else if (name.equals("emptyReason")) {
          this.emptyReason = new CodeableConcept();
          return this.emptyReason;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "List";

  }

      public ListResource copy() {
        ListResource dst = new ListResource();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.title = title == null ? null : title.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.date = date == null ? null : date.copy();
        dst.source = source == null ? null : source.copy();
        dst.orderedBy = orderedBy == null ? null : orderedBy.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (entry != null) {
          dst.entry = new ArrayList<ListEntryComponent>();
          for (ListEntryComponent i : entry)
            dst.entry.add(i.copy());
        };
        dst.emptyReason = emptyReason == null ? null : emptyReason.copy();
        return dst;
      }

      protected ListResource typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ListResource))
          return false;
        ListResource o = (ListResource) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(mode, o.mode, true)
           && compareDeep(title, o.title, true) && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(date, o.date, true) && compareDeep(source, o.source, true)
           && compareDeep(orderedBy, o.orderedBy, true) && compareDeep(note, o.note, true) && compareDeep(entry, o.entry, true)
           && compareDeep(emptyReason, o.emptyReason, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ListResource))
          return false;
        ListResource o = (ListResource) other;
        return compareValues(status, o.status, true) && compareValues(mode, o.mode, true) && compareValues(title, o.title, true)
           && compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (mode == null || mode.isEmpty()) && (title == null || title.isEmpty()) && (code == null || code.isEmpty())
           && (subject == null || subject.isEmpty()) && (encounter == null || encounter.isEmpty()) && (date == null || date.isEmpty())
           && (source == null || source.isEmpty()) && (orderedBy == null || orderedBy.isEmpty()) && (note == null || note.isEmpty())
           && (entry == null || entry.isEmpty()) && (emptyReason == null || emptyReason.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.List;
   }

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Descriptive name for the list</b><br>
   * Type: <b>string</b><br>
   * Path: <b>List.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="List.title", description="Descriptive name for the list", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Descriptive name for the list</b><br>
   * Type: <b>string</b><br>
   * Path: <b>List.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>If all resources have the same subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="List.subject", description="If all resources have the same subject", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>If all resources have the same subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ListResource:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ListResource:patient").toLocked();

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Who and/or what defined the list contents (aka Author)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="List.source", description="Who and/or what defined the list contents (aka Author)", type="reference" )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>Who and/or what defined the list contents (aka Author)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.source</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ListResource:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("ListResource:source").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>current | retired | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="List.status", description="current | retired | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>current | retired | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>If all resources have the same subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="List.subject", description="If all resources have the same subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>If all resources have the same subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ListResource:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ListResource:subject").toLocked();

 /**
   * Search parameter: <b>item</b>
   * <p>
   * Description: <b>Actual entry</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.entry.item</b><br>
   * </p>
   */
  @SearchParamDefinition(name="item", path="List.entry.item", description="Actual entry", type="reference" )
  public static final String SP_ITEM = "item";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>item</b>
   * <p>
   * Description: <b>Actual entry</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.entry.item</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ITEM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ITEM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ListResource:item</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ITEM = new ca.uhn.fhir.model.api.Include("ListResource:item").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Context in which list created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="List.encounter", description="Context in which list created", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Context in which list created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>List.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ListResource:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("ListResource:encounter").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>What the purpose of this list is</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="List.code", description="What the purpose of this list is", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>What the purpose of this list is</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>notes</b>
   * <p>
   * Description: <b>The annotation  - text content</b><br>
   * Type: <b>string</b><br>
   * Path: <b>List.note.text</b><br>
   * </p>
   */
  @SearchParamDefinition(name="notes", path="List.note.text", description="The annotation  - text content", type="string" )
  public static final String SP_NOTES = "notes";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>notes</b>
   * <p>
   * Description: <b>The annotation  - text content</b><br>
   * Type: <b>string</b><br>
   * Path: <b>List.note.text</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NOTES = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NOTES);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the list was prepared</b><br>
   * Type: <b>date</b><br>
   * Path: <b>List.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="List.date", description="When the list was prepared", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the list was prepared</b><br>
   * Type: <b>date</b><br>
   * Path: <b>List.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>empty-reason</b>
   * <p>
   * Description: <b>Why list is empty</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.emptyReason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="empty-reason", path="List.emptyReason", description="Why list is empty", type="token" )
  public static final String SP_EMPTY_REASON = "empty-reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>empty-reason</b>
   * <p>
   * Description: <b>Why list is empty</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.emptyReason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EMPTY_REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EMPTY_REASON);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="List.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>List.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

