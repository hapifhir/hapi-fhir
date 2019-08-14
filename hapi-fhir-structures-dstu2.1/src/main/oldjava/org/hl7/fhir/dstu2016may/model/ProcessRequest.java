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
 * This resource provides the target, request and response, and action details for an action to be performed by the target on or about existing resources.
 */
@ResourceDef(name="ProcessRequest", profile="http://hl7.org/fhir/Profile/ProcessRequest")
public class ProcessRequest extends DomainResource {

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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
        @Child(name = "sequenceLinkId", type = {IntegerType.class}, order=1, min=1, max=1, modifier=false, summary=true)
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.sequenceLinkId = castToInteger(value); // IntegerType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId"))
          this.sequenceLinkId = castToInteger(value); // IntegerType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: throw new FHIRException("Cannot make property sequenceLinkId as it is not a complex type"); // IntegerType
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty());
      }

  public String fhirType() {
    return "ProcessRequest.item";

  }

  }

    /**
     * The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.
     */
    @Child(name = "action", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="cancel | poll | reprocess | status", formalDefinition="The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest." )
    protected Enumeration<ActionList> action;

    /**
     * The ProcessRequest business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The ProcessRequest business identifier." )
    protected List<Identifier> identifier;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The organization which is the target of the request.
     */
    @Child(name = "target", type = {Identifier.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Target of the request", formalDefinition="The organization which is the target of the request." )
    protected Type target;

    /**
     * The practitioner who is responsible for the action specified in thise request.
     */
    @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the action specified in thise request." )
    protected Type provider;

    /**
     * The organization which is responsible for the action speccified in thise request.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the action speccified in thise request." )
    protected Type organization;

    /**
     * Reference of resource which is the target or subject of this action.
     */
    @Child(name = "request", type = {Identifier.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Request reference", formalDefinition="Reference of resource which is the target or subject of this action." )
    protected Type request;

    /**
     * Reference of a prior response to resource which is the target or subject of this action.
     */
    @Child(name = "response", type = {Identifier.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Response reference", formalDefinition="Reference of a prior response to resource which is the target or subject of this action." )
    protected Type response;

    /**
     * If true remove all history excluding audit.
     */
    @Child(name = "nullify", type = {BooleanType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Nullify", formalDefinition="If true remove all history excluding audit." )
    protected BooleanType nullify;

    /**
     * A reference to supply which authenticates the process.
     */
    @Child(name = "reference", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reference number/string", formalDefinition="A reference to supply which authenticates the process." )
    protected StringType reference;

    /**
     * List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.
     */
    @Child(name = "item", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Items to re-adjudicate", formalDefinition="List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated." )
    protected List<ItemsComponent> item;

    /**
     * Names of resource types to include.
     */
    @Child(name = "include", type = {StringType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource type(s) to include", formalDefinition="Names of resource types to include." )
    protected List<StringType> include;

    /**
     * Names of resource types to exclude.
     */
    @Child(name = "exclude", type = {StringType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource type(s) to exclude", formalDefinition="Names of resource types to exclude." )
    protected List<StringType> exclude;

    /**
     * A period of time during which the fulfilling resources would have been created.
     */
    @Child(name = "period", type = {Period.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period", formalDefinition="A period of time during which the fulfilling resources would have been created." )
    protected Period period;

    private static final long serialVersionUID = -1557088159L;

  /**
   * Constructor
   */
    public ProcessRequest() {
      super();
    }

  /**
   * Constructor
   */
    public ProcessRequest(Enumeration<ActionList> action) {
      super();
      this.action = action;
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
        if (this.action == null)
          this.action = new Enumeration<ActionList>(new ActionListEnumFactory());
        this.action.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (The ProcessRequest business identifier.)
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
     * @return {@link #identifier} (The ProcessRequest business identifier.)
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
    public ProcessRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding(); // cc
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public ProcessRequest setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessRequest.originalRuleset");
        else if (Configuration.doAutoCreate())
          this.originalRuleset = new Coding(); // cc
      return this.originalRuleset;
    }

    public boolean hasOriginalRuleset() { 
      return this.originalRuleset != null && !this.originalRuleset.isEmpty();
    }

    /**
     * @param value {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public ProcessRequest setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
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
     * @return {@link #target} (The organization which is the target of the request.)
     */
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (The organization which is the target of the request.)
     */
    public Identifier getTargetIdentifier() throws FHIRException { 
      if (!(this.target instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Identifier) this.target;
    }

    public boolean hasTargetIdentifier() { 
      return this.target instanceof Identifier;
    }

    /**
     * @return {@link #target} (The organization which is the target of the request.)
     */
    public Reference getTargetReference() throws FHIRException { 
      if (!(this.target instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Reference) this.target;
    }

    public boolean hasTargetReference() { 
      return this.target instanceof Reference;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The organization which is the target of the request.)
     */
    public ProcessRequest setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the action specified in thise request.)
     */
    public Type getProvider() { 
      return this.provider;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the action specified in thise request.)
     */
    public Identifier getProviderIdentifier() throws FHIRException { 
      if (!(this.provider instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.provider.getClass().getName()+" was encountered");
      return (Identifier) this.provider;
    }

    public boolean hasProviderIdentifier() { 
      return this.provider instanceof Identifier;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the action specified in thise request.)
     */
    public Reference getProviderReference() throws FHIRException { 
      if (!(this.provider instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.provider.getClass().getName()+" was encountered");
      return (Reference) this.provider;
    }

    public boolean hasProviderReference() { 
      return this.provider instanceof Reference;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the action specified in thise request.)
     */
    public ProcessRequest setProvider(Type value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the action speccified in thise request.)
     */
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the action speccified in thise request.)
     */
    public Identifier getOrganizationIdentifier() throws FHIRException { 
      if (!(this.organization instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.organization.getClass().getName()+" was encountered");
      return (Identifier) this.organization;
    }

    public boolean hasOrganizationIdentifier() { 
      return this.organization instanceof Identifier;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the action speccified in thise request.)
     */
    public Reference getOrganizationReference() throws FHIRException { 
      if (!(this.organization instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.organization.getClass().getName()+" was encountered");
      return (Reference) this.organization;
    }

    public boolean hasOrganizationReference() { 
      return this.organization instanceof Reference;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the action speccified in thise request.)
     */
    public ProcessRequest setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #request} (Reference of resource which is the target or subject of this action.)
     */
    public Type getRequest() { 
      return this.request;
    }

    /**
     * @return {@link #request} (Reference of resource which is the target or subject of this action.)
     */
    public Identifier getRequestIdentifier() throws FHIRException { 
      if (!(this.request instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.request.getClass().getName()+" was encountered");
      return (Identifier) this.request;
    }

    public boolean hasRequestIdentifier() { 
      return this.request instanceof Identifier;
    }

    /**
     * @return {@link #request} (Reference of resource which is the target or subject of this action.)
     */
    public Reference getRequestReference() throws FHIRException { 
      if (!(this.request instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.request.getClass().getName()+" was encountered");
      return (Reference) this.request;
    }

    public boolean hasRequestReference() { 
      return this.request instanceof Reference;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Reference of resource which is the target or subject of this action.)
     */
    public ProcessRequest setRequest(Type value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #response} (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public Type getResponse() { 
      return this.response;
    }

    /**
     * @return {@link #response} (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public Identifier getResponseIdentifier() throws FHIRException { 
      if (!(this.response instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.response.getClass().getName()+" was encountered");
      return (Identifier) this.response;
    }

    public boolean hasResponseIdentifier() { 
      return this.response instanceof Identifier;
    }

    /**
     * @return {@link #response} (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public Reference getResponseReference() throws FHIRException { 
      if (!(this.response instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.response.getClass().getName()+" was encountered");
      return (Reference) this.response;
    }

    public boolean hasResponseReference() { 
      return this.response instanceof Reference;
    }

    public boolean hasResponse() { 
      return this.response != null && !this.response.isEmpty();
    }

    /**
     * @param value {@link #response} (Reference of a prior response to resource which is the target or subject of this action.)
     */
    public ProcessRequest setResponse(Type value) { 
      this.response = value;
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

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (ItemsComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #item} (List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.)
     */
    // syntactic sugar
    public ItemsComponent addItem() { //3
      ItemsComponent t = new ItemsComponent();
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return t;
    }

    // syntactic sugar
    public ProcessRequest addItem(ItemsComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return {@link #include} (Names of resource types to include.)
     */
    public List<StringType> getInclude() { 
      if (this.include == null)
        this.include = new ArrayList<StringType>();
      return this.include;
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
    // syntactic sugar
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
    // syntactic sugar
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
        childrenList.add(new Property("action", "code", "The type of processing action being requested, for example Reversal, Readjudication, StatusRequest,PendedRequest.", 0, java.lang.Integer.MAX_VALUE, action));
        childrenList.add(new Property("identifier", "Identifier", "The ProcessRequest business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("target[x]", "Identifier|Reference(Organization)", "The organization which is the target of the request.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the action specified in thise request.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the action speccified in thise request.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request[x]", "Identifier|Reference(Any)", "Reference of resource which is the target or subject of this action.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("response[x]", "Identifier|Reference(Any)", "Reference of a prior response to resource which is the target or subject of this action.", 0, java.lang.Integer.MAX_VALUE, response));
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
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : new Base[] {this.action}; // Enumeration<ActionList>
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Type
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Type
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.action = new ActionListEnumFactory().fromType(value); // Enumeration<ActionList>
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 1548678118: // ruleset
          this.ruleset = castToCoding(value); // Coding
          break;
        case 1089373397: // originalRuleset
          this.originalRuleset = castToCoding(value); // Coding
          break;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          break;
        case -880905839: // target
          this.target = (Type) value; // Type
          break;
        case -987494927: // provider
          this.provider = (Type) value; // Type
          break;
        case 1178922291: // organization
          this.organization = (Type) value; // Type
          break;
        case 1095692943: // request
          this.request = (Type) value; // Type
          break;
        case -340323263: // response
          this.response = (Type) value; // Type
          break;
        case -2001137643: // nullify
          this.nullify = castToBoolean(value); // BooleanType
          break;
        case -925155509: // reference
          this.reference = castToString(value); // StringType
          break;
        case 3242771: // item
          this.getItem().add((ItemsComponent) value); // ItemsComponent
          break;
        case 1942574248: // include
          this.getInclude().add(castToString(value)); // StringType
          break;
        case -1321148966: // exclude
          this.getExclude().add(castToString(value)); // StringType
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action"))
          this.action = new ActionListEnumFactory().fromType(value); // Enumeration<ActionList>
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("target[x]"))
          this.target = (Type) value; // Type
        else if (name.equals("provider[x]"))
          this.provider = (Type) value; // Type
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("request[x]"))
          this.request = (Type) value; // Type
        else if (name.equals("response[x]"))
          this.response = (Type) value; // Type
        else if (name.equals("nullify"))
          this.nullify = castToBoolean(value); // BooleanType
        else if (name.equals("reference"))
          this.reference = castToString(value); // StringType
        else if (name.equals("item"))
          this.getItem().add((ItemsComponent) value);
        else if (name.equals("include"))
          this.getInclude().add(castToString(value));
        else if (name.equals("exclude"))
          this.getExclude().add(castToString(value));
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858: throw new FHIRException("Cannot make property action as it is not a complex type"); // Enumeration<ActionList>
        case -1618432855:  return addIdentifier(); // Identifier
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -815579825:  return getTarget(); // Type
        case 2064698607:  return getProvider(); // Type
        case 1326483053:  return getOrganization(); // Type
        case 37106577:  return getRequest(); // Type
        case 1847549087:  return getResponse(); // Type
        case -2001137643: throw new FHIRException("Cannot make property nullify as it is not a complex type"); // BooleanType
        case -925155509: throw new FHIRException("Cannot make property reference as it is not a complex type"); // StringType
        case 3242771:  return addItem(); // ItemsComponent
        case 1942574248: throw new FHIRException("Cannot make property include as it is not a complex type"); // StringType
        case -1321148966: throw new FHIRException("Cannot make property exclude as it is not a complex type"); // StringType
        case -991726143:  return getPeriod(); // Period
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("action")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.action");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("ruleset")) {
          this.ruleset = new Coding();
          return this.ruleset;
        }
        else if (name.equals("originalRuleset")) {
          this.originalRuleset = new Coding();
          return this.originalRuleset;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessRequest.created");
        }
        else if (name.equals("targetIdentifier")) {
          this.target = new Identifier();
          return this.target;
        }
        else if (name.equals("targetReference")) {
          this.target = new Reference();
          return this.target;
        }
        else if (name.equals("providerIdentifier")) {
          this.provider = new Identifier();
          return this.provider;
        }
        else if (name.equals("providerReference")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organizationIdentifier")) {
          this.organization = new Identifier();
          return this.organization;
        }
        else if (name.equals("organizationReference")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("requestIdentifier")) {
          this.request = new Identifier();
          return this.request;
        }
        else if (name.equals("requestReference")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("responseIdentifier")) {
          this.response = new Identifier();
          return this.response;
        }
        else if (name.equals("responseReference")) {
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
        dst.action = action == null ? null : action.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.target = target == null ? null : target.copy();
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
        return compareDeep(action, o.action, true) && compareDeep(identifier, o.identifier, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(target, o.target, true) && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true)
           && compareDeep(request, o.request, true) && compareDeep(response, o.response, true) && compareDeep(nullify, o.nullify, true)
           && compareDeep(reference, o.reference, true) && compareDeep(item, o.item, true) && compareDeep(include, o.include, true)
           && compareDeep(exclude, o.exclude, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcessRequest))
          return false;
        ProcessRequest o = (ProcessRequest) other;
        return compareValues(action, o.action, true) && compareValues(created, o.created, true) && compareValues(nullify, o.nullify, true)
           && compareValues(reference, o.reference, true) && compareValues(include, o.include, true) && compareValues(exclude, o.exclude, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (action == null || action.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (ruleset == null || ruleset.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
           && (created == null || created.isEmpty()) && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty())
           && (organization == null || organization.isEmpty()) && (request == null || request.isEmpty())
           && (response == null || response.isEmpty()) && (nullify == null || nullify.isEmpty()) && (reference == null || reference.isEmpty())
           && (item == null || item.isEmpty()) && (include == null || include.isEmpty()) && (exclude == null || exclude.isEmpty())
           && (period == null || period.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcessRequest;
   }

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

 /**
   * Search parameter: <b>providerreference</b>
   * <p>
   * Description: <b>The provider who regenerated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.providerReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="providerreference", path="ProcessRequest.provider.as(Reference)", description="The provider who regenerated this request", type="reference" )
  public static final String SP_PROVIDERREFERENCE = "providerreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>providerreference</b>
   * <p>
   * Description: <b>The provider who regenerated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.providerReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessRequest:providerreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("ProcessRequest:providerreference").toLocked();

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="ProcessRequest.organization.as(Identifier)", description="The organization who generated this request", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="ProcessRequest.organization.as(Reference)", description="The organization who generated this request", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessRequest.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessRequest:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("ProcessRequest:organizationreference").toLocked();

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
   * Search parameter: <b>provideridentifier</b>
   * <p>
   * Description: <b>The provider who regenerated this request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.providerIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provideridentifier", path="ProcessRequest.provider.as(Identifier)", description="The provider who regenerated this request", type="token" )
  public static final String SP_PROVIDERIDENTIFIER = "provideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provideridentifier</b>
   * <p>
   * Description: <b>The provider who regenerated this request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessRequest.providerIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PROVIDERIDENTIFIER);


}

