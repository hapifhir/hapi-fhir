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
 * This resource provides the request and line items details for the claim which is to be re-adjudicated.
 */
@ResourceDef(name="Readjudicate", profile="http://hl7.org/fhir/Profile/Readjudicate")
public class Readjudicate extends DomainResource {

    @Block()
    public static class ItemsComponent extends BackboneElement {
        /**
         * A service line number.
         */
        @Child(name="sequenceLinkId", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected IntegerType sequenceLinkId;

        private static final long serialVersionUID = -1598360600L;

      public ItemsComponent() {
        super();
      }

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
          return this.sequenceLinkId == null ? 0 : this.sequenceLinkId.getValue();
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

  }

    /**
     * The Response Business Identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response Business Identifier." )
    protected List<Identifier> identifier;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "target", type = {Organization.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who is target  of the request." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (The Insurer who is target  of the request.)
     */
    protected Organization targetTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Practitioner.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Organization.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization organizationTarget;

    /**
     * Reference of resource to reverse.
     */
    @Child(name = "request", type = {}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Request reference", formalDefinition="Reference of resource to reverse." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Reference of resource to reverse.)
     */
    protected Resource requestTarget;

    /**
     * Reference of response to resource to reverse.
     */
    @Child(name = "response", type = {}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Response reference", formalDefinition="Reference of response to resource to reverse." )
    protected Reference response;

    /**
     * The actual object that is the target of the reference (Reference of response to resource to reverse.)
     */
    protected Resource responseTarget;

    /**
     * A reference to supply which authenticated the process.
     */
    @Child(name = "reference", type = {StringType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Reference number/string", formalDefinition="A reference to supply which authenticated the process." )
    protected StringType reference;

    /**
     * List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.
     */
    @Child(name = "item", type = {}, order = 10, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Items to re-adjudicate", formalDefinition="List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated." )
    protected List<ItemsComponent> item;

    private static final long serialVersionUID = 445992972L;

    public Readjudicate() {
      super();
    }

    /**
     * @return {@link #identifier} (The Response Business Identifier.)
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
     * @return {@link #identifier} (The Response Business Identifier.)
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
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.ruleset");
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
    public Readjudicate setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.originalRuleset");
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
    public Readjudicate setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.created");
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
    public Readjudicate setCreatedElement(DateTimeType value) { 
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
    public Readjudicate setCreated(Date value) { 
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
     * @return {@link #target} (The Insurer who is target  of the request.)
     */
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference(); // cc
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The Insurer who is target  of the request.)
     */
    public Readjudicate setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public Organization getTargetTarget() { 
      if (this.targetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.target");
        else if (Configuration.doAutoCreate())
          this.targetTarget = new Organization(); // aa
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public Readjudicate setTargetTarget(Organization value) { 
      this.targetTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Readjudicate setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner(); // aa
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Readjudicate setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Readjudicate setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Readjudicate setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Reference of resource to reverse.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Reference of resource to reverse.)
     */
    public Readjudicate setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference of resource to reverse.)
     */
    public Resource getRequestTarget() { 
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference of resource to reverse.)
     */
    public Readjudicate setRequestTarget(Resource value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #response} (Reference of response to resource to reverse.)
     */
    public Reference getResponse() { 
      if (this.response == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.response");
        else if (Configuration.doAutoCreate())
          this.response = new Reference(); // cc
      return this.response;
    }

    public boolean hasResponse() { 
      return this.response != null && !this.response.isEmpty();
    }

    /**
     * @param value {@link #response} (Reference of response to resource to reverse.)
     */
    public Readjudicate setResponse(Reference value) { 
      this.response = value;
      return this;
    }

    /**
     * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference of response to resource to reverse.)
     */
    public Resource getResponseTarget() { 
      return this.responseTarget;
    }

    /**
     * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference of response to resource to reverse.)
     */
    public Readjudicate setResponseTarget(Resource value) { 
      this.responseTarget = value;
      return this;
    }

    /**
     * @return {@link #reference} (A reference to supply which authenticated the process.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public StringType getReferenceElement() { 
      if (this.reference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Readjudicate.reference");
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
     * @param value {@link #reference} (A reference to supply which authenticated the process.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public Readjudicate setReferenceElement(StringType value) { 
      this.reference = value;
      return this;
    }

    /**
     * @return A reference to supply which authenticated the process.
     */
    public String getReference() { 
      return this.reference == null ? null : this.reference.getValue();
    }

    /**
     * @param value A reference to supply which authenticated the process.
     */
    public Readjudicate setReference(String value) { 
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response Business Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("target", "Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request", "Reference(Any)", "Reference of resource to reverse.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("response", "Reference(Any)", "Reference of response to resource to reverse.", 0, java.lang.Integer.MAX_VALUE, response));
        childrenList.add(new Property("reference", "string", "A reference to supply which authenticated the process.", 0, java.lang.Integer.MAX_VALUE, reference));
        childrenList.add(new Property("item", "", "List of top level items to be re-adjudicated, if none specified then the entire submission is re-adjudicated.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      public Readjudicate copy() {
        Readjudicate dst = new Readjudicate();
        copyValues(dst);
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
        dst.reference = reference == null ? null : reference.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemsComponent>();
          for (ItemsComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected Readjudicate typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Readjudicate))
          return false;
        Readjudicate o = (Readjudicate) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(target, o.target, true) && compareDeep(provider, o.provider, true)
           && compareDeep(organization, o.organization, true) && compareDeep(request, o.request, true) && compareDeep(response, o.response, true)
           && compareDeep(reference, o.reference, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Readjudicate))
          return false;
        Readjudicate o = (Readjudicate) other;
        return compareValues(created, o.created, true) && compareValues(reference, o.reference, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (request == null || request.isEmpty()) && (response == null || response.isEmpty()) && (reference == null || reference.isEmpty())
           && (item == null || item.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Readjudicate;
   }

  @SearchParamDefinition(name="identifier", path="Readjudicate.identifier", description="The business identifier of the Eligibility", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

