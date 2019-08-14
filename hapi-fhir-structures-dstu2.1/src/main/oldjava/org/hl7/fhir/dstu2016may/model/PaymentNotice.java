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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * This resource provides the status of the payment for goods and services rendered, and the request and response resource references.
 */
@ResourceDef(name="PaymentNotice", profile="http://hl7.org/fhir/Profile/PaymentNotice")
public class PaymentNotice extends DomainResource {

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "target", type = {Identifier.class, Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer or Regulatory body", formalDefinition="The Insurer who is target  of the request." )
    protected Type target;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type provider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type organization;

    /**
     * Reference of resource to reverse.
     */
    @Child(name = "request", type = {Identifier.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Request reference", formalDefinition="Reference of resource to reverse." )
    protected Type request;

    /**
     * Reference of response to resource to reverse.
     */
    @Child(name = "response", type = {Identifier.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Response reference", formalDefinition="Reference of response to resource to reverse." )
    protected Type response;

    /**
     * The payment status, typically paid: payment sent, cleared: payment received.
     */
    @Child(name = "paymentStatus", type = {Coding.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Status of the payment", formalDefinition="The payment status, typically paid: payment sent, cleared: payment received." )
    protected Coding paymentStatus;

    /**
     * The date when the above payment action occurrred.
     */
    @Child(name = "statusDate", type = {DateType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment or clearing date", formalDefinition="The date when the above payment action occurrred." )
    protected DateType statusDate;

    private static final long serialVersionUID = -771143315L;

  /**
   * Constructor
   */
    public PaymentNotice() {
      super();
    }

  /**
   * Constructor
   */
    public PaymentNotice(Coding paymentStatus) {
      super();
      this.paymentStatus = paymentStatus;
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
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
     * @return {@link #identifier} (The Response business identifier.)
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
    public PaymentNotice addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create PaymentNotice.ruleset");
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
    public PaymentNotice setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.originalRuleset");
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
    public PaymentNotice setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.created");
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
    public PaymentNotice setCreatedElement(DateTimeType value) { 
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
    public PaymentNotice setCreated(Date value) { 
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
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (The Insurer who is target  of the request.)
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
     * @return {@link #target} (The Insurer who is target  of the request.)
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
     * @param value {@link #target} (The Insurer who is target  of the request.)
     */
    public PaymentNotice setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Type getProvider() { 
      return this.provider;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
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
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
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
     * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentNotice setProvider(Type value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
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
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
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
     * @param value {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public PaymentNotice setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #request} (Reference of resource to reverse.)
     */
    public Type getRequest() { 
      return this.request;
    }

    /**
     * @return {@link #request} (Reference of resource to reverse.)
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
     * @return {@link #request} (Reference of resource to reverse.)
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
     * @param value {@link #request} (Reference of resource to reverse.)
     */
    public PaymentNotice setRequest(Type value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #response} (Reference of response to resource to reverse.)
     */
    public Type getResponse() { 
      return this.response;
    }

    /**
     * @return {@link #response} (Reference of response to resource to reverse.)
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
     * @return {@link #response} (Reference of response to resource to reverse.)
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
     * @param value {@link #response} (Reference of response to resource to reverse.)
     */
    public PaymentNotice setResponse(Type value) { 
      this.response = value;
      return this;
    }

    /**
     * @return {@link #paymentStatus} (The payment status, typically paid: payment sent, cleared: payment received.)
     */
    public Coding getPaymentStatus() { 
      if (this.paymentStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.paymentStatus");
        else if (Configuration.doAutoCreate())
          this.paymentStatus = new Coding(); // cc
      return this.paymentStatus;
    }

    public boolean hasPaymentStatus() { 
      return this.paymentStatus != null && !this.paymentStatus.isEmpty();
    }

    /**
     * @param value {@link #paymentStatus} (The payment status, typically paid: payment sent, cleared: payment received.)
     */
    public PaymentNotice setPaymentStatus(Coding value) { 
      this.paymentStatus = value;
      return this;
    }

    /**
     * @return {@link #statusDate} (The date when the above payment action occurrred.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public DateType getStatusDateElement() { 
      if (this.statusDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.statusDate");
        else if (Configuration.doAutoCreate())
          this.statusDate = new DateType(); // bb
      return this.statusDate;
    }

    public boolean hasStatusDateElement() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    public boolean hasStatusDate() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    /**
     * @param value {@link #statusDate} (The date when the above payment action occurrred.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public PaymentNotice setStatusDateElement(DateType value) { 
      this.statusDate = value;
      return this;
    }

    /**
     * @return The date when the above payment action occurrred.
     */
    public Date getStatusDate() { 
      return this.statusDate == null ? null : this.statusDate.getValue();
    }

    /**
     * @param value The date when the above payment action occurrred.
     */
    public PaymentNotice setStatusDate(Date value) { 
      if (value == null)
        this.statusDate = null;
      else {
        if (this.statusDate == null)
          this.statusDate = new DateType();
        this.statusDate.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("target[x]", "Identifier|Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request[x]", "Identifier|Reference(Any)", "Reference of resource to reverse.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("response[x]", "Identifier|Reference(Any)", "Reference of response to resource to reverse.", 0, java.lang.Integer.MAX_VALUE, response));
        childrenList.add(new Property("paymentStatus", "Coding", "The payment status, typically paid: payment sent, cleared: payment received.", 0, java.lang.Integer.MAX_VALUE, paymentStatus));
        childrenList.add(new Property("statusDate", "date", "The date when the above payment action occurrred.", 0, java.lang.Integer.MAX_VALUE, statusDate));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Type
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Type
        case 1430704536: /*paymentStatus*/ return this.paymentStatus == null ? new Base[0] : new Base[] {this.paymentStatus}; // Coding
        case 247524032: /*statusDate*/ return this.statusDate == null ? new Base[0] : new Base[] {this.statusDate}; // DateType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
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
        case 1430704536: // paymentStatus
          this.paymentStatus = castToCoding(value); // Coding
          break;
        case 247524032: // statusDate
          this.statusDate = castToDate(value); // DateType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
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
        else if (name.equals("paymentStatus"))
          this.paymentStatus = castToCoding(value); // Coding
        else if (name.equals("statusDate"))
          this.statusDate = castToDate(value); // DateType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -815579825:  return getTarget(); // Type
        case 2064698607:  return getProvider(); // Type
        case 1326483053:  return getOrganization(); // Type
        case 37106577:  return getRequest(); // Type
        case 1847549087:  return getResponse(); // Type
        case 1430704536:  return getPaymentStatus(); // Coding
        case 247524032: throw new FHIRException("Cannot make property statusDate as it is not a complex type"); // DateType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
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
          throw new FHIRException("Cannot call addChild on a primitive type PaymentNotice.created");
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
        else if (name.equals("paymentStatus")) {
          this.paymentStatus = new Coding();
          return this.paymentStatus;
        }
        else if (name.equals("statusDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentNotice.statusDate");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "PaymentNotice";

  }

      public PaymentNotice copy() {
        PaymentNotice dst = new PaymentNotice();
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
        dst.paymentStatus = paymentStatus == null ? null : paymentStatus.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        return dst;
      }

      protected PaymentNotice typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PaymentNotice))
          return false;
        PaymentNotice o = (PaymentNotice) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(target, o.target, true) && compareDeep(provider, o.provider, true)
           && compareDeep(organization, o.organization, true) && compareDeep(request, o.request, true) && compareDeep(response, o.response, true)
           && compareDeep(paymentStatus, o.paymentStatus, true) && compareDeep(statusDate, o.statusDate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PaymentNotice))
          return false;
        PaymentNotice o = (PaymentNotice) other;
        return compareValues(created, o.created, true) && compareValues(statusDate, o.statusDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (request == null || request.isEmpty()) && (response == null || response.isEmpty()) && (paymentStatus == null || paymentStatus.isEmpty())
           && (statusDate == null || statusDate.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PaymentNotice;
   }

 /**
   * Search parameter: <b>paymentstatus</b>
   * <p>
   * Description: <b>The type of payment notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.paymentStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="paymentstatus", path="PaymentNotice.paymentStatus", description="The type of payment notice", type="token" )
  public static final String SP_PAYMENTSTATUS = "paymentstatus";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>paymentstatus</b>
   * <p>
   * Description: <b>The type of payment notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.paymentStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PAYMENTSTATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PAYMENTSTATUS);

 /**
   * Search parameter: <b>statusdate</b>
   * <p>
   * Description: <b>The date of the payment action</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentNotice.statusDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="statusdate", path="PaymentNotice.statusDate", description="The date of the payment action", type="date" )
  public static final String SP_STATUSDATE = "statusdate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>statusdate</b>
   * <p>
   * Description: <b>The date of the payment action</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentNotice.statusDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam STATUSDATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_STATUSDATE);

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>Creation date fro the notice</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentNotice.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="PaymentNotice.created", description="Creation date fro the notice", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>Creation date fro the notice</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentNotice.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>requestidentifier</b>
   * <p>
   * Description: <b>The Claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.requestIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestidentifier", path="PaymentNotice.request.as(Identifier)", description="The Claim", type="token" )
  public static final String SP_REQUESTIDENTIFIER = "requestidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestidentifier</b>
   * <p>
   * Description: <b>The Claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.requestIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTIDENTIFIER);

 /**
   * Search parameter: <b>providerreference</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.providerReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="providerreference", path="PaymentNotice.provider.as(Reference)", description="The reference to the provider", type="reference" )
  public static final String SP_PROVIDERREFERENCE = "providerreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>providerreference</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.providerReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:providerreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentNotice:providerreference").toLocked();

 /**
   * Search parameter: <b>requestreference</b>
   * <p>
   * Description: <b>The Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.requestReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestreference", path="PaymentNotice.request.as(Reference)", description="The Claim", type="reference" )
  public static final String SP_REQUESTREFERENCE = "requestreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestreference</b>
   * <p>
   * Description: <b>The Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.requestReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:requestreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentNotice:requestreference").toLocked();

 /**
   * Search parameter: <b>responsereference</b>
   * <p>
   * Description: <b>The ClaimResponse</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.responseReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="responsereference", path="PaymentNotice.response.as(Reference)", description="The ClaimResponse", type="reference" )
  public static final String SP_RESPONSEREFERENCE = "responsereference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>responsereference</b>
   * <p>
   * Description: <b>The ClaimResponse</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.responseReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESPONSEREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESPONSEREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:responsereference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESPONSEREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentNotice:responsereference").toLocked();

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="PaymentNotice.organization.as(Identifier)", description="The organization who generated this resource", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="PaymentNotice.organization.as(Reference)", description="The organization who generated this resource", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentNotice:organizationreference").toLocked();

 /**
   * Search parameter: <b>responseidentifier</b>
   * <p>
   * Description: <b>The ClaimResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.responseIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="responseidentifier", path="PaymentNotice.response.as(Identifier)", description="The ClaimResponse", type="token" )
  public static final String SP_RESPONSEIDENTIFIER = "responseidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>responseidentifier</b>
   * <p>
   * Description: <b>The ClaimResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.responseIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RESPONSEIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RESPONSEIDENTIFIER);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PaymentNotice.identifier", description="The business identifier of the notice", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>provideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.providerIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provideridentifier", path="PaymentNotice.provider.as(Identifier)", description="The reference to the provider", type="token" )
  public static final String SP_PROVIDERIDENTIFIER = "provideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.providerIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PROVIDERIDENTIFIER);


}

