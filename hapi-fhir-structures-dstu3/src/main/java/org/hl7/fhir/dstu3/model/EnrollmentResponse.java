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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource provides enrollment and plan details from the processing of an Enrollment resource.
 */
@ResourceDef(name="EnrollmentResponse", profile="http://hl7.org/fhir/Profile/EnrollmentResponse")
public class EnrollmentResponse extends DomainResource {

    public enum EnrollmentResponseStatus {
        /**
         * The resource instance is currently in-force.
         */
        ACTIVE, 
        /**
         * The resource instance is withdrawn, rescinded or reversed.
         */
        CANCELLED, 
        /**
         * A new resource instance the contents of which is not complete.
         */
        DRAFT, 
        /**
         * The resource instance was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EnrollmentResponseStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown EnrollmentResponseStatus code '"+codeString+"'");
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
            case ACTIVE: return "http://hl7.org/fhir/enrollmentresponse-status";
            case CANCELLED: return "http://hl7.org/fhir/enrollmentresponse-status";
            case DRAFT: return "http://hl7.org/fhir/enrollmentresponse-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/enrollmentresponse-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The resource instance is currently in-force.";
            case CANCELLED: return "The resource instance is withdrawn, rescinded or reversed.";
            case DRAFT: return "A new resource instance the contents of which is not complete.";
            case ENTEREDINERROR: return "The resource instance was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case DRAFT: return "Draft";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class EnrollmentResponseStatusEnumFactory implements EnumFactory<EnrollmentResponseStatus> {
    public EnrollmentResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return EnrollmentResponseStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return EnrollmentResponseStatus.CANCELLED;
        if ("draft".equals(codeString))
          return EnrollmentResponseStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return EnrollmentResponseStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown EnrollmentResponseStatus code '"+codeString+"'");
        }
        public Enumeration<EnrollmentResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<EnrollmentResponseStatus>(this, EnrollmentResponseStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<EnrollmentResponseStatus>(this, EnrollmentResponseStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<EnrollmentResponseStatus>(this, EnrollmentResponseStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EnrollmentResponseStatus>(this, EnrollmentResponseStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown EnrollmentResponseStatus code '"+codeString+"'");
        }
    public String toCode(EnrollmentResponseStatus code) {
      if (code == EnrollmentResponseStatus.ACTIVE)
        return "active";
      if (code == EnrollmentResponseStatus.CANCELLED)
        return "cancelled";
      if (code == EnrollmentResponseStatus.DRAFT)
        return "draft";
      if (code == EnrollmentResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(EnrollmentResponseStatus code) {
      return code.getSystem();
      }
    }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/enrollmentresponse-status")
    protected Enumeration<EnrollmentResponseStatus> status;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Identifier.class, EnrollmentRequest.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Claim reference", formalDefinition="Original request resource reference." )
    protected Type request;

    /**
     * Processing status: error, complete.
     */
    @Child(name = "outcome", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="complete | error | partial", formalDefinition="Processing status: error, complete." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected Enumeration<RemittanceOutcome> outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ruleset")
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ruleset")
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Type organization;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Identifier.class, Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type requestProvider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Identifier.class, Organization.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type requestOrganization;

    private static final long serialVersionUID = -1774170871L;

  /**
   * Constructor
   */
    public EnrollmentResponse() {
      super();
    }

  /**
   * Constructor
   */
    public EnrollmentResponse(Enumeration<EnrollmentResponseStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EnrollmentResponse setIdentifier(List<Identifier> theIdentifier) { 
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

    public EnrollmentResponse addIdentifier(Identifier t) { //3
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
    public Enumeration<EnrollmentResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EnrollmentResponseStatus>(new EnrollmentResponseStatusEnumFactory()); // bb
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
    public EnrollmentResponse setStatusElement(Enumeration<EnrollmentResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public EnrollmentResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public EnrollmentResponse setStatus(EnrollmentResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<EnrollmentResponseStatus>(new EnrollmentResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Type getRequest() { 
      return this.request;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
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
     * @return {@link #request} (Original request resource reference.)
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
     * @param value {@link #request} (Original request resource reference.)
     */
    public EnrollmentResponse setRequest(Type value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Processing status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public Enumeration<RemittanceOutcome> getOutcomeElement() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentResponse.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new Enumeration<RemittanceOutcome>(new RemittanceOutcomeEnumFactory()); // bb
      return this.outcome;
    }

    public boolean hasOutcomeElement() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Processing status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public EnrollmentResponse setOutcomeElement(Enumeration<RemittanceOutcome> value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return Processing status: error, complete.
     */
    public RemittanceOutcome getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value Processing status: error, complete.
     */
    public EnrollmentResponse setOutcome(RemittanceOutcome value) { 
      if (value == null)
        this.outcome = null;
      else {
        if (this.outcome == null)
          this.outcome = new Enumeration<RemittanceOutcome>(new RemittanceOutcomeEnumFactory());
        this.outcome.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public StringType getDispositionElement() { 
      if (this.disposition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentResponse.disposition");
        else if (Configuration.doAutoCreate())
          this.disposition = new StringType(); // bb
      return this.disposition;
    }

    public boolean hasDispositionElement() { 
      return this.disposition != null && !this.disposition.isEmpty();
    }

    public boolean hasDisposition() { 
      return this.disposition != null && !this.disposition.isEmpty();
    }

    /**
     * @param value {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public EnrollmentResponse setDispositionElement(StringType value) { 
      this.disposition = value;
      return this;
    }

    /**
     * @return A description of the status of the adjudication.
     */
    public String getDisposition() { 
      return this.disposition == null ? null : this.disposition.getValue();
    }

    /**
     * @param value A description of the status of the adjudication.
     */
    public EnrollmentResponse setDisposition(String value) { 
      if (Utilities.noString(value))
        this.disposition = null;
      else {
        if (this.disposition == null)
          this.disposition = new StringType();
        this.disposition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentResponse.ruleset");
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
    public EnrollmentResponse setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentResponse.originalRuleset");
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
    public EnrollmentResponse setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentResponse.created");
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
     * @param value {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public EnrollmentResponse setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when the enclosed suite of services were performed or completed.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when the enclosed suite of services were performed or completed.
     */
    public EnrollmentResponse setCreated(Date value) { 
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
     * @return {@link #organization} (The Insurer who produced this adjudicated response.)
     */
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The Insurer who produced this adjudicated response.)
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
     * @return {@link #organization} (The Insurer who produced this adjudicated response.)
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
     * @param value {@link #organization} (The Insurer who produced this adjudicated response.)
     */
    public EnrollmentResponse setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Type getRequestProvider() { 
      return this.requestProvider;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Identifier getRequestProviderIdentifier() throws FHIRException { 
      if (!(this.requestProvider instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.requestProvider.getClass().getName()+" was encountered");
      return (Identifier) this.requestProvider;
    }

    public boolean hasRequestProviderIdentifier() { 
      return this.requestProvider instanceof Identifier;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProviderReference() throws FHIRException { 
      if (!(this.requestProvider instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.requestProvider.getClass().getName()+" was encountered");
      return (Reference) this.requestProvider;
    }

    public boolean hasRequestProviderReference() { 
      return this.requestProvider instanceof Reference;
    }

    public boolean hasRequestProvider() { 
      return this.requestProvider != null && !this.requestProvider.isEmpty();
    }

    /**
     * @param value {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EnrollmentResponse setRequestProvider(Type value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Type getRequestOrganization() { 
      return this.requestOrganization;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Identifier getRequestOrganizationIdentifier() throws FHIRException { 
      if (!(this.requestOrganization instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.requestOrganization.getClass().getName()+" was encountered");
      return (Identifier) this.requestOrganization;
    }

    public boolean hasRequestOrganizationIdentifier() { 
      return this.requestOrganization instanceof Identifier;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getRequestOrganizationReference() throws FHIRException { 
      if (!(this.requestOrganization instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.requestOrganization.getClass().getName()+" was encountered");
      return (Reference) this.requestOrganization;
    }

    public boolean hasRequestOrganizationReference() { 
      return this.requestOrganization instanceof Reference;
    }

    public boolean hasRequestOrganization() { 
      return this.requestOrganization != null && !this.requestOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public EnrollmentResponse setRequestOrganization(Type value) { 
      this.requestOrganization = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("request[x]", "Identifier|Reference(EnrollmentRequest)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "code", "Processing status: error, complete.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("requestProvider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EnrollmentResponseStatus>
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Enumeration<RemittanceOutcome>
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Type
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Type
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
          this.status = new EnrollmentResponseStatusEnumFactory().fromType(value); // Enumeration<EnrollmentResponseStatus>
          break;
        case 1095692943: // request
          this.request = castToType(value); // Type
          break;
        case -1106507950: // outcome
          this.outcome = new RemittanceOutcomeEnumFactory().fromType(value); // Enumeration<RemittanceOutcome>
          break;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
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
        case 1178922291: // organization
          this.organization = castToType(value); // Type
          break;
        case 1601527200: // requestProvider
          this.requestProvider = castToType(value); // Type
          break;
        case 599053666: // requestOrganization
          this.requestOrganization = castToType(value); // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new EnrollmentResponseStatusEnumFactory().fromType(value); // Enumeration<EnrollmentResponseStatus>
        else if (name.equals("request[x]"))
          this.request = castToType(value); // Type
        else if (name.equals("outcome"))
          this.outcome = new RemittanceOutcomeEnumFactory().fromType(value); // Enumeration<RemittanceOutcome>
        else if (name.equals("disposition"))
          this.disposition = castToString(value); // StringType
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("organization[x]"))
          this.organization = castToType(value); // Type
        else if (name.equals("requestProvider[x]"))
          this.requestProvider = castToType(value); // Type
        else if (name.equals("requestOrganization[x]"))
          this.requestOrganization = castToType(value); // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EnrollmentResponseStatus>
        case 37106577:  return getRequest(); // Type
        case -1106507950: throw new FHIRException("Cannot make property outcome as it is not a complex type"); // Enumeration<RemittanceOutcome>
        case 583380919: throw new FHIRException("Cannot make property disposition as it is not a complex type"); // StringType
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case 1326483053:  return getOrganization(); // Type
        case -1694784800:  return getRequestProvider(); // Type
        case 818740190:  return getRequestOrganization(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentResponse.status");
        }
        else if (name.equals("requestIdentifier")) {
          this.request = new Identifier();
          return this.request;
        }
        else if (name.equals("requestReference")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentResponse.outcome");
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentResponse.disposition");
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
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentResponse.created");
        }
        else if (name.equals("organizationIdentifier")) {
          this.organization = new Identifier();
          return this.organization;
        }
        else if (name.equals("organizationReference")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("requestProviderIdentifier")) {
          this.requestProvider = new Identifier();
          return this.requestProvider;
        }
        else if (name.equals("requestProviderReference")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
        }
        else if (name.equals("requestOrganizationIdentifier")) {
          this.requestOrganization = new Identifier();
          return this.requestOrganization;
        }
        else if (name.equals("requestOrganizationReference")) {
          this.requestOrganization = new Reference();
          return this.requestOrganization;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EnrollmentResponse";

  }

      public EnrollmentResponse copy() {
        EnrollmentResponse dst = new EnrollmentResponse();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        return dst;
      }

      protected EnrollmentResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EnrollmentResponse))
          return false;
        EnrollmentResponse o = (EnrollmentResponse) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(request, o.request, true)
           && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(organization, o.organization, true) && compareDeep(requestProvider, o.requestProvider, true)
           && compareDeep(requestOrganization, o.requestOrganization, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EnrollmentResponse))
          return false;
        EnrollmentResponse o = (EnrollmentResponse) other;
        return compareValues(status, o.status, true) && compareValues(outcome, o.outcome, true) && compareValues(disposition, o.disposition, true)
           && compareValues(created, o.created, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, request
          , outcome, disposition, ruleset, originalRuleset, created, organization, requestProvider
          , requestOrganization);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EnrollmentResponse;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the EnrollmentResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EnrollmentResponse.identifier", description="The business identifier of the EnrollmentResponse", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the EnrollmentResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

