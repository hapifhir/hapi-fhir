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
 * This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.
 */
@ResourceDef(name="ProcessRequest", profile="http://hl7.org/fhir/Profile/ProcessRequest")
public class ProcessRequest extends DomainResource {

    public enum ProcessRequestStatus {
        /**
         * The instance is currently in-force.
         */
        ACTIVE, 
        /**
         * The instance is withdrawn, rescinded or reversed.
         */
        CANCELLED, 
        /**
         * A new instance the contents of which is not complete.
         */
        DRAFT, 
        /**
         * The instance was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ProcessRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ProcessRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case DRAFT: return "draft";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/fm-status";
            case CANCELLED: return "http://hl7.org/fhir/fm-status";
            case DRAFT: return "http://hl7.org/fhir/fm-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/fm-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The instance is currently in-force.";
            case CANCELLED: return "The instance is withdrawn, rescinded or reversed.";
            case DRAFT: return "A new instance the contents of which is not complete.";
            case ENTEREDINERROR: return "The instance was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case DRAFT: return "Draft";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class ProcessRequestStatusEnumFactory implements EnumFactory<ProcessRequestStatus> {
    public ProcessRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ProcessRequestStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return ProcessRequestStatus.CANCELLED;
        if ("draft".equals(codeString))
          return ProcessRequestStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return ProcessRequestStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ProcessRequestStatus code '"+codeString+"'");
        }
        public Enumeration<ProcessRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProcessRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<ProcessRequestStatus>(this, ProcessRequestStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<ProcessRequestStatus>(this, ProcessRequestStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<ProcessRequestStatus>(this, ProcessRequestStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ProcessRequestStatus>(this, ProcessRequestStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ProcessRequestStatus code '"+codeString+"'");
        }
    public String toCode(ProcessRequestStatus code) {
      if (code == ProcessRequestStatus.ACTIVE)
        return "active";
      if (code == ProcessRequestStatus.CANCELLED)
        return "cancelled";
      if (code == ProcessRequestStatus.DRAFT)
        return "draft";
      if (code == ProcessRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ProcessRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum ActionList {
        /**
         * Cancel, reverse or nullify the target resource.
         */
        CANCEL, 
        /**
         * Check for previously un-read/ not-retrieved resources.
         */
        POLL, 
        /**
         * Re-process the target resource.
         */
        REPROCESS, 
        /**
         * Retrieve the processing status of the target resource.
         */
        STATUS, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionList fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cancel".equals(codeString))
          return CANCEL;
        if ("poll".equals(codeString))
          return POLL;
        if ("reprocess".equals(codeString))
          return REPROCESS;
        if ("status".equals(codeString))
          return STATUS;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionList code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANCEL: return "cancel";
            case POLL: return "poll";
            case REPROCESS: return "reprocess";
            case STATUS: return "status";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CANCEL: return "http://hl7.org/fhir/actionlist";
            case POLL: return "http://hl7.org/fhir/actionlist";
            case REPROCESS: return "http://hl7.org/fhir/actionlist";
            case STATUS: return "http://hl7.org/fhir/actionlist";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CANCEL: return "Cancel, reverse or nullify the target resource.";
            case POLL: return "Check for previously un-read/ not-retrieved resources.";
            case REPROCESS: return "Re-process the target resource.";
            case STATUS: return "Retrieve the processing status of the target resource.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANCEL: return "Cancel, Reverse or Nullify";
            case POLL: return "Poll";
            case REPROCESS: return "Re-Process";
            case STATUS: return "Status Check";
            default: return "?";
          }
        }
    }

  public static class ActionListEnumFactory implements EnumFactory<ActionList> {
    public ActionList fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cancel".equals(codeString))
          return ActionList.CANCEL;
        if ("poll".equals(codeString))
          return ActionList.POLL;
        if ("reprocess".equals(codeString))
          return ActionList.REPROCESS;
        if ("status".equals(codeString))
          return ActionList.STATUS;
        throw new IllegalArgumentException("Unknown ActionList code '"+codeString+"'");
        }
        public Enumeration<ActionList> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionList>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("cancel".equals(codeString))
          return new Enumeration<ActionList>(this, ActionList.CANCEL);
        if ("poll".equals(codeString))
          return new Enumeration<ActionList>(this, ActionList.POLL);
        if ("reprocess".equals(codeString))
          return new Enumeration<ActionList>(this, ActionList.REPROCESS);
        if ("status".equals(codeString))
          return new Enumeration<ActionList>(this, ActionList.STATUS);
        throw new FHIRException("Unknown ActionList code '"+codeString+"'");
        }
    public String toCode(ActionList code) {
      if (code == ActionList.CANCEL)
        return "cancel";
      if (code == ActionList.POLL)
        return "poll";
      if (code == ActionList.REPROCESS)
        return "reprocess";
      if (code == ActionList.STATUS)
        return "status";
      return "?";
      }
    public String toSystem(ActionList code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ItemsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequenceLinkId", type = {IntegerType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected IntegerType sequenceLinkId;

        private static final long serialVersionUID = -1598360600L;

    /**
     * Constructor
     */
      public ItemsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemsComponent(IntegerType sequenceLinkId) {
        super();
        this.sequenceLinkId = sequenceLinkId;
      }

        /**
         * @return {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public IntegerType getSequenceLinkIdElement() { 
          if (this.sequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.sequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.sequenceLinkId = new IntegerType(); // bb
          return this.sequenceLinkId;
        }

        public boolean hasSequenceLinkIdElement() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        public boolean hasSequenceLinkId() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public ItemsComponent setSequenceLinkIdElement(IntegerType value) { 
          this.sequenceLinkId = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequenceLinkId() { 
          return this.sequenceLinkId == null || this.sequenceLinkId.isEmpty() ? 0 : this.sequenceLinkId.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemsComponent setSequenceLinkId(int value) { 
            if (this.sequenceLinkId == null)
              this.sequenceLinkId = new IntegerType();
            this.sequenceLinkId.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "integer", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : new Base[] {this.sequenceLinkId}; // IntegerType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.sequenceLinkId = castToInteger(value); // IntegerType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          this.sequenceLinkId = castToInteger(value); // IntegerType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666:  return getSequenceLinkIdElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return new String[] {"integer"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.sequenceLinkId");
        }
        else
          return super.addChild(name);
      }

      public ItemsComponent copy() {
        ItemsComponent dst = new ItemsComponent();
        copyValues(dst);
        dst.sequenceLinkId = sequenceLinkId == null ? null : sequenceLinkId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequenceLinkId);
      }

  public String fhirType() {
    return "ProcessRequest.item";

  }

  }

    /**
     * The ProcessRequest business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The ProcessRequest business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<ProcessRequestStatus> status;

    /**
     * The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.
     */
    @Child(name = "action", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="cancel | poll | reprocess | status", formalDefinition="The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/actionlist")
    protected Enumeration<ActionList> action;

    /**
     * The organization which is the target of the request.
     */
    @Child(name = "target", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party which is the target of the request", formalDefinition="The organization which is the target of the request." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (The organization which is the target of the request.)
     */
    protected Organization targetTarget;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The practitioner who is responsible for the action specified in this request.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the action specified in this request." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the action specified in this request.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the action speccified in this request.
     */
    @Child(name = "organization", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the action speccified in this request." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the action speccified in this request.)
     */
    protected Organization organizationTarget;

    /**
     * Reference of resource which is the target or subject of this action.
     */
    @Child(name = "request", type = {Reference.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reference to the Request resource", formalDefinition="Reference of resource which is the target or subject of this action." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Reference of resource which is the target or subject of this action.)
     */
    protected Resource requestTarget;

    /**
     * Reference of a prior response to resource which is the target or subject of this action.
     */
    @Child(name = "response", type = {Reference.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reference to the Response resource", formalDefinition="Reference of a prior response to resource which is the target or subject of this action." )
    protected Reference response;

    /**
     * The actual object that is the target of the reference (Reference of a prior response to resource which is the target or subject of this action.)
     */
    protected Resource responseTarget;

    /**
     * If true remove all history excluding audit.
     */
    @Child(name = "nullify", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Remove history", formalDefinition="If true remove all history excluding audit." )
    protected BooleanType nullify;

    /**
     * A reference to supply which authenticates the process.
     */
    @Child(name = "reference", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reference number/string", formalDefinition="A reference to supply which authenticates the process." )
    protected StringType reference;

    /**
     * List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.
     */
    @Child(name = "item", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Items to re-adjudicate", formalDefinition="List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated." )
    protected List<ItemsComponent> item;

    /**
     * Names of resource types to include.
     */
    @Child(name = "include", type = {StringType.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Resource type(s) to include", formalDefinition="Names of resource types to include." )
    protected List<StringType> include;

    /**
     * Names of resource types to exclude.
     */
    @Child(name = "exclude", type = {StringType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Resource type(s) to exclude", formalDefinition="Names of resource types to exclude." )
    protected List<StringType> exclude;

    /**
     * A period of time during which the fulfilling resources would have been created.
     */
    @Child(name = "period", type = {Period.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Selection period", formalDefinition="A period of time during which the fulfilling resources would have been created." )
    protected Period period;

    private static final long serialVersionUID = -346692020L;

  /**
   * Constructor
   */
    public ProcessRequest() {
      super();
    }

    /**
     * @return {@link #identifier} (The ProcessRequest business identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessRequest setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public ProcessRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ProcessRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ProcessRequestStatus>(new ProcessRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ProcessRequest setStatusElement(Enumeration<ProcessRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ProcessRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public ProcessRequest setStatus(ProcessRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ProcessRequestStatus>(new ProcessRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #action} (The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.). This is the underlying object with id, value and extensions. The accessor "getAction" gives direct access to the value
     */
    public Enumeration<ActionList> getActionElement() { 
      if (this.action == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.action");
        else if (Configuration.doAutoCreate())
          this.action = new Enumeration<ActionList>(new ActionListEnumFactory()); // bb
      return this.action;
    }

    public boolean hasActionElement() { 
      return this.action != null && !this.action.isEmpty();
    }

    public boolean hasAction() { 
      return this.action != null && !this.action.isEmpty();
    }

    /**
     * @param value {@link #action} (The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.). This is the underlying object with id, value and extensions. The accessor "getAction" gives direct access to the value
     */
    public ProcessRequest setActionElement(Enumeration<ActionList> value) { 
      this.action = value;
      return this;
    }

    /**
     * @return The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.
     */
    public ActionList getAction() { 
      return this.action == null ? null : this.action.getValue();
    }

    /**
     * @param value The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.
     */
    public ProcessRequest setAction(ActionList value) { 
      if (value == null)
        this.action = null;
      else {
        if (this.action == null)
          this.action = new Enumeration<ActionList>(new ActionListEnumFactory());
        this.action.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #target} (The organization which is the target of the request.)
     */
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference(); // cc
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The organization which is the target of the request.)
     */
    public ProcessRequest setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is the target of the request.)
     */
    public Organization getTargetTarget() { 
      if (this.targetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.target");
        else if (Configuration.doAutoCreate())
          this.targetTarget = new Organization(); // aa
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is the target of the request.)
     */
    public ProcessRequest setTargetTarget(Organization value) { 
      this.targetTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public ProcessRequest setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when this resource was created.
     */
    public ProcessRequest setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the action specified in this request.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the action specified in this request.)
     */
    public ProcessRequest setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the action specified in this request.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner(); // aa
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the action specified in this request.)
     */
    public ProcessRequest setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the action speccified in this request.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the action speccified in this request.)
     */
    public ProcessRequest setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the action speccified in this request.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the action speccified in this request.)
     */
    public ProcessRequest setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Reference of resource which is the target or subject of this action.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Reference of resource which is the target or subject of this action.)
     */
    public ProcessRequest setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference of resource which is the target or subject of this action.)
     */
    public Resource getRequestTarget() { 
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference of resource which is the target or subject of this action.)
     */
    public ProcessRequest setRequestTarget(Resource value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #response} (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public Reference getResponse() { 
      if (this.response == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.response");
        else if (Configuration.doAutoCreate())
          this.response = new Reference(); // cc
      return this.response;
    }

    public boolean hasResponse() { 
      return this.response != null && !this.response.isEmpty();
    }

    /**
     * @param value {@link #response} (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public ProcessRequest setResponse(Reference value) { 
      this.response = value;
      return this;
    }

    /**
     * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public Resource getResponseTarget() { 
      return this.responseTarget;
    }

    /**
     * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public ProcessRequest setResponseTarget(Resource value) { 
      this.responseTarget = value;
      return this;
    }

    /**
     * @return {@link #nullify} (If true remove all history excluding audit.). This is the underlying object with id, value and extensions. The accessor "getNullify" gives direct access to the value
     */
    public BooleanType getNullifyElement() { 
      if (this.nullify == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.nullify");
        else if (Configuration.doAutoCreate())
          this.nullify = new BooleanType(); // bb
      return this.nullify;
    }

    public boolean hasNullifyElement() { 
      return this.nullify != null && !this.nullify.isEmpty();
    }

    public boolean hasNullify() { 
      return this.nullify != null && !this.nullify.isEmpty();
    }

    /**
     * @param value {@link #nullify} (If true remove all history excluding audit.). This is the underlying object with id, value and extensions. The accessor "getNullify" gives direct access to the value
     */
    public ProcessRequest setNullifyElement(BooleanType value) { 
      this.nullify = value;
      return this;
    }

    /**
     * @return If true remove all history excluding audit.
     */
    public boolean getNullify() { 
      return this.nullify == null || this.nullify.isEmpty() ? false : this.nullify.getValue();
    }

    /**
     * @param value If true remove all history excluding audit.
     */
    public ProcessRequest setNullify(boolean value) { 
        if (this.nullify == null)
          this.nullify = new BooleanType();
        this.nullify.setValue(value);
      return this;
    }

    /**
     * @return {@link #reference} (A reference to supply which authenticates the process.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public StringType getReferenceElement() { 
      if (this.reference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.reference");
        else if (Configuration.doAutoCreate())
          this.reference = new StringType(); // bb
      return this.reference;
    }

    public boolean hasReferenceElement() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    public boolean hasReference() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    /**
     * @param value {@link #reference} (A reference to supply which authenticates the process.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public ProcessRequest setReferenceElement(StringType value) { 
      this.reference = value;
      return this;
    }

    /**
     * @return A reference to supply which authenticates the process.
     */
    public String getReference() { 
      return this.reference == null ? null : this.reference.getValue();
    }

    /**
     * @param value A reference to supply which authenticates the process.
     */
    public ProcessRequest setReference(String value) { 
      if (Utilities.noString(value))
        this.reference = null;
      else {
        if (this.reference == null)
          this.reference = new StringType();
        this.reference.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #item} (List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.)
     */
    public List<ItemsComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessRequest setItem(List<ItemsComponent> theItem) { 
      this.item = theItem;
      return this;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (ItemsComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ItemsComponent addItem() { //3
      ItemsComponent t = new ItemsComponent();
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return t;
    }

    public ProcessRequest addItem(ItemsComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
     */
    public ItemsComponent getItemFirstRep() { 
      if (getItem().isEmpty()) {
        addItem();
      }
      return getItem().get(0);
    }

    /**
     * @return {@link #include} (Names of resource types to include.)
     */
    public List<StringType> getInclude() { 
      if (this.include == null)
        this.include = new ArrayList<StringType>();
      return this.include;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessRequest setInclude(List<StringType> theInclude) { 
      this.include = theInclude;
      return this;
    }

    public boolean hasInclude() { 
      if (this.include == null)
        return false;
      for (StringType item : this.include)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #include} (Names of resource types to include.)
     */
    public StringType addIncludeElement() {//2 
      StringType t = new StringType();
      if (this.include == null)
        this.include = new ArrayList<StringType>();
      this.include.add(t);
      return t;
    }

    /**
     * @param value {@link #include} (Names of resource types to include.)
     */
    public ProcessRequest addInclude(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.include == null)
        this.include = new ArrayList<StringType>();
      this.include.add(t);
      return this;
    }

    /**
     * @param value {@link #include} (Names of resource types to include.)
     */
    public boolean hasInclude(String value) { 
      if (this.include == null)
        return false;
      for (StringType v : this.include)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #exclude} (Names of resource types to exclude.)
     */
    public List<StringType> getExclude() { 
      if (this.exclude == null)
        this.exclude = new ArrayList<StringType>();
      return this.exclude;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessRequest setExclude(List<StringType> theExclude) { 
      this.exclude = theExclude;
      return this;
    }

    public boolean hasExclude() { 
      if (this.exclude == null)
        return false;
      for (StringType item : this.exclude)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #exclude} (Names of resource types to exclude.)
     */
    public StringType addExcludeElement() {//2 
      StringType t = new StringType();
      if (this.exclude == null)
        this.exclude = new ArrayList<StringType>();
      this.exclude.add(t);
      return t;
    }

    /**
     * @param value {@link #exclude} (Names of resource types to exclude.)
     */
    public ProcessRequest addExclude(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.exclude == null)
        this.exclude = new ArrayList<StringType>();
      this.exclude.add(t);
      return this;
    }

    /**
     * @param value {@link #exclude} (Names of resource types to exclude.)
     */
    public boolean hasExclude(String value) { 
      if (this.exclude == null)
        return false;
      for (StringType v : this.exclude)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #period} (A period of time during which the fulfilling resources would have been created.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (A period of time during which the fulfilling resources would have been created.)
     */
    public ProcessRequest setPeriod(Period value) { 
      this.period = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The ProcessRequest business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("action", "code", "The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.", 0, java.lang.Integer.MAX_VALUE, action));
        childrenList.add(new Property("target", "Reference(Organization)", "The organization which is the target of the request.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the action specified in this request.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the action speccified in this request.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request", "Reference(Any)", "Reference of resource which is the target or subject of this action.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("response", "Reference(Any)", "Reference of a prior response to resource which is the target or subject of this action.", 0, java.lang.Integer.MAX_VALUE, response));
        childrenList.add(new Property("nullify", "boolean", "If true remove all history excluding audit.", 0, java.lang.Integer.MAX_VALUE, nullify));
        childrenList.add(new Property("reference", "string", "A reference to supply which authenticates the process.", 0, java.lang.Integer.MAX_VALUE, reference));
        childrenList.add(new Property("item", "", "List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("include", "string", "Names of resource types to include.", 0, java.lang.Integer.MAX_VALUE, include));
        childrenList.add(new Property("exclude", "string", "Names of resource types to exclude.", 0, java.lang.Integer.MAX_VALUE, exclude));
        childrenList.add(new Property("period", "Period", "A period of time during which the fulfilling resources would have been created.", 0, java.lang.Integer.MAX_VALUE, period));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcessRequestStatus>
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : new Base[] {this.action}; // Enumeration<ActionList>
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Reference
        case -2001137643: /*nullify*/ return this.nullify == null ? new Base[0] : new Base[] {this.nullify}; // BooleanType
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // StringType
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemsComponent
        case 1942574248: /*include*/ return this.include == null ? new Base[0] : this.include.toArray(new Base[this.include.size()]); // StringType
        case -1321148966: /*exclude*/ return this.exclude == null ? new Base[0] : this.exclude.toArray(new Base[this.exclude.size()]); // StringType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new ProcessRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcessRequestStatus>
          return value;
        case -1422950858: // action
          value = new ActionListEnumFactory().fromType(castToCode(value));
          this.action = (Enumeration) value; // Enumeration<ActionList>
          return value;
        case -880905839: // target
          this.target = castToReference(value); // Reference
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -340323263: // response
          this.response = castToReference(value); // Reference
          return value;
        case -2001137643: // nullify
          this.nullify = castToBoolean(value); // BooleanType
          return value;
        case -925155509: // reference
          this.reference = castToString(value); // StringType
          return value;
        case 3242771: // item
          this.getItem().add((ItemsComponent) value); // ItemsComponent
          return value;
        case 1942574248: // include
          this.getInclude().add(castToString(value)); // StringType
          return value;
        case -1321148966: // exclude
          this.getExclude().add(castToString(value)); // StringType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ProcessRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcessRequestStatus>
        } else if (name.equals("action")) {
          value = new ActionListEnumFactory().fromType(castToCode(value));
          this.action = (Enumeration) value; // Enumeration<ActionList>
        } else if (name.equals("target")) {
          this.target = castToReference(value); // Reference
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("response")) {
          this.response = castToReference(value); // Reference
        } else if (name.equals("nullify")) {
          this.nullify = castToBoolean(value); // BooleanType
        } else if (name.equals("reference")) {
          this.reference = castToString(value); // StringType
        } else if (name.equals("item")) {
          this.getItem().add((ItemsComponent) value);
        } else if (name.equals("include")) {
          this.getInclude().add(castToString(value));
        } else if (name.equals("exclude")) {
          this.getExclude().add(castToString(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -1422950858:  return getActionElement();
        case -880905839:  return getTarget(); 
        case 1028554472:  return getCreatedElement();
        case -987494927:  return getProvider(); 
        case 1178922291:  return getOrganization(); 
        case 1095692943:  return getRequest(); 
        case -340323263:  return getResponse(); 
        case -2001137643:  return getNullifyElement();
        case -925155509:  return getReferenceElement();
        case 3242771:  return addItem(); 
        case 1942574248:  return addIncludeElement();
        case -1321148966:  return addExcludeElement();
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1422950858: /*action*/ return new String[] {"code"};
        case -880905839: /*target*/ return new String[] {"Reference"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -340323263: /*response*/ return new String[] {"Reference"};
        case -2001137643: /*nullify*/ return new String[] {"boolean"};
        case -925155509: /*reference*/ return new String[] {"string"};
        case 3242771: /*item*/ return new String[] {};
        case 1942574248: /*include*/ return new String[] {"string"};
        case -1321148966: /*exclude*/ return new String[] {"string"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.status");
        }
        else if (name.equals("action")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.action");
        }
        else if (name.equals("target")) {
          this.target = new Reference();
          return this.target;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.created");
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("response")) {
          this.response = new Reference();
          return this.response;
        }
        else if (name.equals("nullify")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.nullify");
        }
        else if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.reference");
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else if (name.equals("include")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.include");
        }
        else if (name.equals("exclude")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.exclude");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ProcessRequest";

  }

      public ProcessRequest copy() {
        ProcessRequest dst = new ProcessRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.action = action == null ? null : action.copy();
        dst.target = target == null ? null : target.copy();
        dst.created = created == null ? null : created.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        dst.nullify = nullify == null ? null : nullify.copy();
        dst.reference = reference == null ? null : reference.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemsComponent>();
          for (ItemsComponent i : item)
            dst.item.add(i.copy());
        };
        if (include != null) {
          dst.include = new ArrayList<StringType>();
          for (StringType i : include)
            dst.include.add(i.copy());
        };
        if (exclude != null) {
          dst.exclude = new ArrayList<StringType>();
          for (StringType i : exclude)
            dst.exclude.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      protected ProcessRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcessRequest))
          return false;
        ProcessRequest o = (ProcessRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(action, o.action, true)
           && compareDeep(target, o.target, true) && compareDeep(created, o.created, true) && compareDeep(provider, o.provider, true)
           && compareDeep(organization, o.organization, true) && compareDeep(request, o.request, true) && compareDeep(response, o.response, true)
           && compareDeep(nullify, o.nullify, true) && compareDeep(reference, o.reference, true) && compareDeep(item, o.item, true)
           && compareDeep(include, o.include, true) && compareDeep(exclude, o.exclude, true) && compareDeep(period, o.period, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcessRequest))
          return false;
        ProcessRequest o = (ProcessRequest) other;
        return compareValues(status, o.status, true) && compareValues(action, o.action, true) && compareValues(created, o.created, true)
           && compareValues(nullify, o.nullify, true) && compareValues(reference, o.reference, true) && compareValues(include, o.include, true)
           && compareValues(exclude, o.exclude, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, action
          , target, created, provider, organization, request, response, nullify, reference
          , item, include, exclude, period);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcessRequest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the ProcessRequest</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ProcessRequest.identifier", description="The business identifier of the ProcessRequest", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the ProcessRequest</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The provider who regenerated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="ProcessRequest.provider", description="The provider who regenerated this request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The provider who regenerated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessRequest:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("ProcessRequest:provider").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="ProcessRequest.organization", description="The organization who generated this request", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessRequest:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("ProcessRequest:organization").toLocked();

 /**
   * Search parameter: <b>action</b>
   * <p>
   * Description: <b>The action requested by this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.action</b><br>
   * </p>
   */
  @SearchParamDefinition(name="action", path="ProcessRequest.action", description="The action requested by this resource", type="token" )
  public static final String SP_ACTION = "action";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>action</b>
   * <p>
   * Description: <b>The action requested by this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.action</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTION);


}

