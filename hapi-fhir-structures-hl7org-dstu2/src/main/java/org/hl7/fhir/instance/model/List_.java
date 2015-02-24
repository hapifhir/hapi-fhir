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
 * A set of information summarized from a list of other resources.
 */
@ResourceDef(name = "List_", profile = "http://hl7.org/fhir/Profile/List_")
public class List_ extends DomainResource {

    public enum ListMode {
        /**
         * This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes.
         */
        WORKING, 
        /**
         * This list was prepared as a snapshot. It should not be assumed to be current.
         */
        SNAPSHOT, 
        /**
         * The list is prepared as a statement of changes that have been made or recommended.
         */
        CHANGES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("working".equals(codeString))
          return WORKING;
        if ("snapshot".equals(codeString))
          return SNAPSHOT;
        if ("changes".equals(codeString))
          return CHANGES;
        throw new Exception("Unknown ListMode code '"+codeString+"'");
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
            case WORKING: return "";
            case SNAPSHOT: return "";
            case CHANGES: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case WORKING: return "This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes.";
            case SNAPSHOT: return "This list was prepared as a snapshot. It should not be assumed to be current.";
            case CHANGES: return "The list is prepared as a statement of changes that have been made or recommended.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WORKING: return "working";
            case SNAPSHOT: return "snapshot";
            case CHANGES: return "changes";
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
    public String toCode(ListMode code) {
      if (code == ListMode.WORKING)
        return "working";
      if (code == ListMode.SNAPSHOT)
        return "snapshot";
      if (code == ListMode.CHANGES)
        return "changes";
      return "?";
      }
    }

    @Block()
    public static class ListEntryComponent extends BackboneElement {
        /**
         * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list.
         */
        @Child(name="flag", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Workflow information about this item", formalDefinition="The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list." )
        protected List<CodeableConcept> flag;

        /**
         * True if this item is marked as deleted in the list.
         */
        @Child(name="deleted", type={BooleanType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="If this item is actually marked as deleted", formalDefinition="True if this item is marked as deleted in the list." )
        protected BooleanType deleted;

        /**
         * When this item was added to the list.
         */
        @Child(name="date", type={DateTimeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="When item added to list", formalDefinition="When this item was added to the list." )
        protected DateTimeType date;

        /**
         * A reference to the actual resource from which data was derived.
         */
        @Child(name="item", type={}, order=4, min=1, max=1)
        @Description(shortDefinition="Actual entry", formalDefinition="A reference to the actual resource from which data was derived." )
        protected Reference item;

        /**
         * The actual object that is the target of the reference (A reference to the actual resource from which data was derived.)
         */
        protected Resource itemTarget;

        private static final long serialVersionUID = -27973283L;

      public ListEntryComponent() {
        super();
      }

      public ListEntryComponent(Reference item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #flag} (The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list.)
         */
        public List<CodeableConcept> getFlag() { 
          if (this.flag == null)
            this.flag = new ArrayList<CodeableConcept>();
          return this.flag;
        }

        public boolean hasFlag() { 
          if (this.flag == null)
            return false;
          for (CodeableConcept item : this.flag)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #flag} (The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list.)
         */
    // syntactic sugar
        public CodeableConcept addFlag() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.flag == null)
            this.flag = new ArrayList<CodeableConcept>();
          this.flag.add(t);
          return t;
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
          return this.deleted == null ? false : this.deleted.getValue();
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
          childrenList.add(new Property("flag", "CodeableConcept", "The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list.", 0, java.lang.Integer.MAX_VALUE, flag));
          childrenList.add(new Property("deleted", "boolean", "True if this item is marked as deleted in the list.", 0, java.lang.Integer.MAX_VALUE, deleted));
          childrenList.add(new Property("date", "dateTime", "When this item was added to the list.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("item", "Reference(Any)", "A reference to the actual resource from which data was derived.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      public ListEntryComponent copy() {
        ListEntryComponent dst = new ListEntryComponent();
        copyValues(dst);
        if (flag != null) {
          dst.flag = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : flag)
            dst.flag.add(i.copy());
        };
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

  }

    /**
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier for the List assigned for business purposes outside the context of FHIR." )
    protected List<Identifier> identifier;

    /**
     * This code defines the purpose of the list - why it was created.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="What the purpose of this list is", formalDefinition="This code defines the purpose of the list - why it was created." )
    protected CodeableConcept code;

    /**
     * The common subject (or patient) of the resources that are in the list, if there is one.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="If all resources have the same subject", formalDefinition="The common subject (or patient) of the resources that are in the list, if there is one." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    protected Resource subjectTarget;

    /**
     * The entity responsible for deciding what the contents of the list were.
     */
    @Child(name = "source", type = {Practitioner.class, Patient.class, Device.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Who and/or what defined the list contents", formalDefinition="The entity responsible for deciding what the contents of the list were." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (The entity responsible for deciding what the contents of the list were.)
     */
    protected Resource sourceTarget;

    /**
     * The date that the list was prepared.
     */
    @Child(name = "date", type = {DateTimeType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="When the list was prepared", formalDefinition="The date that the list was prepared." )
    protected DateTimeType date;

    /**
     * Whether items in the list have a meaningful order.
     */
    @Child(name = "ordered", type = {BooleanType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Whether items in the list have a meaningful order", formalDefinition="Whether items in the list have a meaningful order." )
    protected BooleanType ordered;

    /**
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
     */
    @Child(name = "mode", type = {CodeType.class}, order = 6, min = 1, max = 1)
    @Description(shortDefinition="working | snapshot | changes", formalDefinition="How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted." )
    protected Enumeration<ListMode> mode;

    /**
     * Entries in this list.
     */
    @Child(name = "entry", type = {}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Entries in the list", formalDefinition="Entries in this list." )
    protected List<ListEntryComponent> entry;

    /**
     * If the list is empty, why the list is empty.
     */
    @Child(name = "emptyReason", type = {CodeableConcept.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Why list is empty", formalDefinition="If the list is empty, why the list is empty." )
    protected CodeableConcept emptyReason;

    private static final long serialVersionUID = 1475323442L;

    public List_() {
      super();
    }

    public List_(Enumeration<ListMode> mode) {
      super();
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

    /**
     * @return {@link #code} (This code defines the purpose of the list - why it was created.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.code");
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
    public List_ setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.subject");
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
    public List_ setSubject(Reference value) { 
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
    public List_ setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #source} (The entity responsible for deciding what the contents of the list were.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The entity responsible for deciding what the contents of the list were.)
     */
    public List_ setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The entity responsible for deciding what the contents of the list were.)
     */
    public Resource getSourceTarget() { 
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The entity responsible for deciding what the contents of the list were.)
     */
    public List_ setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date that the list was prepared.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.date");
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
    public List_ setDateElement(DateTimeType value) { 
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
    public List_ setDate(Date value) { 
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
     * @return {@link #ordered} (Whether items in the list have a meaningful order.). This is the underlying object with id, value and extensions. The accessor "getOrdered" gives direct access to the value
     */
    public BooleanType getOrderedElement() { 
      if (this.ordered == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.ordered");
        else if (Configuration.doAutoCreate())
          this.ordered = new BooleanType(); // bb
      return this.ordered;
    }

    public boolean hasOrderedElement() { 
      return this.ordered != null && !this.ordered.isEmpty();
    }

    public boolean hasOrdered() { 
      return this.ordered != null && !this.ordered.isEmpty();
    }

    /**
     * @param value {@link #ordered} (Whether items in the list have a meaningful order.). This is the underlying object with id, value and extensions. The accessor "getOrdered" gives direct access to the value
     */
    public List_ setOrderedElement(BooleanType value) { 
      this.ordered = value;
      return this;
    }

    /**
     * @return Whether items in the list have a meaningful order.
     */
    public boolean getOrdered() { 
      return this.ordered == null ? false : this.ordered.getValue();
    }

    /**
     * @param value Whether items in the list have a meaningful order.
     */
    public List_ setOrdered(boolean value) { 
        if (this.ordered == null)
          this.ordered = new BooleanType();
        this.ordered.setValue(value);
      return this;
    }

    /**
     * @return {@link #mode} (How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public Enumeration<ListMode> getModeElement() { 
      if (this.mode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.mode");
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
    public List_ setModeElement(Enumeration<ListMode> value) { 
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
    public List_ setMode(ListMode value) { 
        if (this.mode == null)
          this.mode = new Enumeration<ListMode>(new ListModeEnumFactory());
        this.mode.setValue(value);
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

    /**
     * @return {@link #emptyReason} (If the list is empty, why the list is empty.)
     */
    public CodeableConcept getEmptyReason() { 
      if (this.emptyReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.emptyReason");
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
    public List_ setEmptyReason(CodeableConcept value) { 
      this.emptyReason = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for the List assigned for business purposes outside the context of FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("code", "CodeableConcept", "This code defines the purpose of the list - why it was created.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The common subject (or patient) of the resources that are in the list, if there is one.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("source", "Reference(Practitioner|Patient|Device)", "The entity responsible for deciding what the contents of the list were.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("date", "dateTime", "The date that the list was prepared.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("ordered", "boolean", "Whether items in the list have a meaningful order.", 0, java.lang.Integer.MAX_VALUE, ordered));
        childrenList.add(new Property("mode", "code", "How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.", 0, java.lang.Integer.MAX_VALUE, mode));
        childrenList.add(new Property("entry", "", "Entries in this list.", 0, java.lang.Integer.MAX_VALUE, entry));
        childrenList.add(new Property("emptyReason", "CodeableConcept", "If the list is empty, why the list is empty.", 0, java.lang.Integer.MAX_VALUE, emptyReason));
      }

      public List_ copy() {
        List_ dst = new List_();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.source = source == null ? null : source.copy();
        dst.date = date == null ? null : date.copy();
        dst.ordered = ordered == null ? null : ordered.copy();
        dst.mode = mode == null ? null : mode.copy();
        if (entry != null) {
          dst.entry = new ArrayList<ListEntryComponent>();
          for (ListEntryComponent i : entry)
            dst.entry.add(i.copy());
        };
        dst.emptyReason = emptyReason == null ? null : emptyReason.copy();
        return dst;
      }

      protected List_ typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof List_))
          return false;
        List_ o = (List_) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true)
           && compareDeep(source, o.source, true) && compareDeep(date, o.date, true) && compareDeep(ordered, o.ordered, true)
           && compareDeep(mode, o.mode, true) && compareDeep(entry, o.entry, true) && compareDeep(emptyReason, o.emptyReason, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof List_))
          return false;
        List_ o = (List_) other;
        return compareValues(date, o.date, true) && compareValues(ordered, o.ordered, true) && compareValues(mode, o.mode, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (code == null || code.isEmpty())
           && (subject == null || subject.isEmpty()) && (source == null || source.isEmpty()) && (date == null || date.isEmpty())
           && (ordered == null || ordered.isEmpty()) && (mode == null || mode.isEmpty()) && (entry == null || entry.isEmpty())
           && (emptyReason == null || emptyReason.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.List;
   }

  @SearchParamDefinition(name="date", path="List.date", description="When the list was prepared", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name = "item", path = "List.entry.item", description = "Actual entry", type = "reference")
  public static final String SP_ITEM = "item";
  @SearchParamDefinition(name="empty-reason", path="List.emptyReason", description="Why list is empty", type="token" )
  public static final String SP_EMPTYREASON = "empty-reason";
  @SearchParamDefinition(name = "code", path = "List.code", description = "What the purpose of this list is", type = "token")
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name = "subject", path = "List.subject", description = "If all resources have the same subject", type = "reference")
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name = "patient", path = "List.subject", description = "If all resources have the same subject", type = "reference")
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name = "source", path = "List.source", description = "Who and/or what defined the list contents", type = "reference")
  public static final String SP_SOURCE = "source";

}

