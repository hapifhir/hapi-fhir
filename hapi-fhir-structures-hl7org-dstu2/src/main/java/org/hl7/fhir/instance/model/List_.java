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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A set of information summarized from a list of other resources.
 */
@ResourceDef(name="List", profile="http://hl7.org/fhir/Profile/List_")
public class List_ extends DomainResource {

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
        public static ListStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return CURRENT;
        if ("retired".equals(codeString))
          return RETIRED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown ListStatus code '"+codeString+"'");
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
    public String toCode(ListStatus code) {
      if (code == ListStatus.CURRENT)
        return "current";
      if (code == ListStatus.RETIRED)
        return "retired";
      if (code == ListStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
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

    /*
     * Constructor
     */
      public ListEntryComponent() {
        super();
      }

    /*
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

  }

    /**
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier for the List assigned for business purposes outside the context of FHIR." )
    protected List<Identifier> identifier;

    /**
     * A label for the list assigned by the author.
     */
    @Child(name = "title", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Descriptive name for the list", formalDefinition="A label for the list assigned by the author." )
    protected StringType title;

    /**
     * This code defines the purpose of the list - why it was created.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What the purpose of this list is", formalDefinition="This code defines the purpose of the list - why it was created." )
    protected CodeableConcept code;

    /**
     * The common subject (or patient) of the resources that are in the list, if there is one.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If all resources have the same subject", formalDefinition="The common subject (or patient) of the resources that are in the list, if there is one." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The common subject (or patient) of the resources that are in the list, if there is one.)
     */
    protected Resource subjectTarget;

    /**
     * The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.
     */
    @Child(name = "source", type = {Practitioner.class, Patient.class, Device.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what defined the list contents (aka Author)", formalDefinition="The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    protected Resource sourceTarget;

    /**
     * The encounter that is the context in which this list was created.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Context in which list created", formalDefinition="The encounter that is the context in which this list was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter that is the context in which this list was created.)
     */
    protected Encounter encounterTarget;

    /**
     * Indicates the current state of this list.
     */
    @Child(name = "status", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="current | retired | entered-in-error", formalDefinition="Indicates the current state of this list." )
    protected Enumeration<ListStatus> status;

    /**
     * The date that the list was prepared.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the list was prepared", formalDefinition="The date that the list was prepared." )
    protected DateTimeType date;

    /**
     * What order applies to the items in the list.
     */
    @Child(name = "orderedBy", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What order the list has", formalDefinition="What order applies to the items in the list." )
    protected CodeableConcept orderedBy;

    /**
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
     */
    @Child(name = "mode", type = {CodeType.class}, order=9, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="working | snapshot | changes", formalDefinition="How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted." )
    protected Enumeration<ListMode> mode;

    /**
     * Comments that apply to the overall list.
     */
    @Child(name = "note", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the list", formalDefinition="Comments that apply to the overall list." )
    protected StringType note;

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

    private static final long serialVersionUID = 1819128642L;

  /*
   * Constructor
   */
    public List_() {
      super();
    }

  /*
   * Constructor
   */
    public List_(Enumeration<ListStatus> status, Enumeration<ListMode> mode) {
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
    public List_ addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #title} (A label for the list assigned by the author.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.title");
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
    public List_ setTitleElement(StringType value) { 
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
    public List_ setTitle(String value) { 
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
     * @return {@link #source} (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
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
     * @param value {@link #source} (The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.)
     */
    public List_ setSource(Reference value) { 
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
    public List_ setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter that is the context in which this list was created.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.encounter");
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
    public List_ setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter that is the context in which this list was created.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter that is the context in which this list was created.)
     */
    public List_ setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (Indicates the current state of this list.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ListStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.status");
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
    public List_ setStatusElement(Enumeration<ListStatus> value) { 
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
    public List_ setStatus(ListStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ListStatus>(new ListStatusEnumFactory());
        this.status.setValue(value);
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
     * @return {@link #orderedBy} (What order applies to the items in the list.)
     */
    public CodeableConcept getOrderedBy() { 
      if (this.orderedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.orderedBy");
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
    public List_ setOrderedBy(CodeableConcept value) { 
      this.orderedBy = value;
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
     * @return {@link #note} (Comments that apply to the overall list.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public StringType getNoteElement() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create List_.note");
        else if (Configuration.doAutoCreate())
          this.note = new StringType(); // bb
      return this.note;
    }

    public boolean hasNoteElement() { 
      return this.note != null && !this.note.isEmpty();
    }

    public boolean hasNote() { 
      return this.note != null && !this.note.isEmpty();
    }

    /**
     * @param value {@link #note} (Comments that apply to the overall list.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public List_ setNoteElement(StringType value) { 
      this.note = value;
      return this;
    }

    /**
     * @return Comments that apply to the overall list.
     */
    public String getNote() { 
      return this.note == null ? null : this.note.getValue();
    }

    /**
     * @param value Comments that apply to the overall list.
     */
    public List_ setNote(String value) { 
      if (Utilities.noString(value))
        this.note = null;
      else {
        if (this.note == null)
          this.note = new StringType();
        this.note.setValue(value);
      }
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
    public List_ addEntry(ListEntryComponent t) { //3
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
        childrenList.add(new Property("title", "string", "A label for the list assigned by the author.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("code", "CodeableConcept", "This code defines the purpose of the list - why it was created.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The common subject (or patient) of the resources that are in the list, if there is one.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("source", "Reference(Practitioner|Patient|Device)", "The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is the same as the author of the list.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter that is the context in which this list was created.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("status", "code", "Indicates the current state of this list.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "The date that the list was prepared.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("orderedBy", "CodeableConcept", "What order applies to the items in the list.", 0, java.lang.Integer.MAX_VALUE, orderedBy));
        childrenList.add(new Property("mode", "code", "How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.", 0, java.lang.Integer.MAX_VALUE, mode));
        childrenList.add(new Property("note", "string", "Comments that apply to the overall list.", 0, java.lang.Integer.MAX_VALUE, note));
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
        dst.title = title == null ? null : title.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.source = source == null ? null : source.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        dst.orderedBy = orderedBy == null ? null : orderedBy.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.note = note == null ? null : note.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(title, o.title, true) && compareDeep(code, o.code, true)
           && compareDeep(subject, o.subject, true) && compareDeep(source, o.source, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(status, o.status, true) && compareDeep(date, o.date, true) && compareDeep(orderedBy, o.orderedBy, true)
           && compareDeep(mode, o.mode, true) && compareDeep(note, o.note, true) && compareDeep(entry, o.entry, true)
           && compareDeep(emptyReason, o.emptyReason, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof List_))
          return false;
        List_ o = (List_) other;
        return compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(date, o.date, true)
           && compareValues(mode, o.mode, true) && compareValues(note, o.note, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (title == null || title.isEmpty())
           && (code == null || code.isEmpty()) && (subject == null || subject.isEmpty()) && (source == null || source.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (status == null || status.isEmpty()) && (date == null || date.isEmpty())
           && (orderedBy == null || orderedBy.isEmpty()) && (mode == null || mode.isEmpty()) && (note == null || note.isEmpty())
           && (entry == null || entry.isEmpty()) && (emptyReason == null || emptyReason.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.List;
   }

  @SearchParamDefinition(name="date", path="List.date", description="When the list was prepared", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="item", path="List.entry.item", description="Actual entry", type="reference" )
  public static final String SP_ITEM = "item";
  @SearchParamDefinition(name="empty-reason", path="List.emptyReason", description="Why list is empty", type="token" )
  public static final String SP_EMPTYREASON = "empty-reason";
  @SearchParamDefinition(name="code", path="List.code", description="What the purpose of this list is", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="notes", path="List.note", description="Comments about the list", type="string" )
  public static final String SP_NOTES = "notes";
  @SearchParamDefinition(name="subject", path="List.subject", description="If all resources have the same subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="List.subject", description="If all resources have the same subject", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="source", path="List.source", description="Who and/or what defined the list contents (aka Author)", type="reference" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="encounter", path="List.encounter", description="Context in which list created", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="title", path="List.title", description="Descriptive name for the list", type="string" )
  public static final String SP_TITLE = "title";
  @SearchParamDefinition(name="status", path="List.status", description="current | retired | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";

}

