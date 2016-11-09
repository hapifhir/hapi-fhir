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
 * This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
 */
@ResourceDef(name="EnrollmentRequest", profile="http://hl7.org/fhir/Profile/EnrollmentRequest")
public class EnrollmentRequest extends DomainResource {

    public enum EnrollmentRequestStatus {
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
        public static EnrollmentRequestStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown EnrollmentRequestStatus code '"+codeString+"'");
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
            case ACTIVE: return "http://hl7.org/fhir/enrollmentrequest-status";
            case CANCELLED: return "http://hl7.org/fhir/enrollmentrequest-status";
            case DRAFT: return "http://hl7.org/fhir/enrollmentrequest-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/enrollmentrequest-status";
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

  public static class EnrollmentRequestStatusEnumFactory implements EnumFactory<EnrollmentRequestStatus> {
    public EnrollmentRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return EnrollmentRequestStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return EnrollmentRequestStatus.CANCELLED;
        if ("draft".equals(codeString))
          return EnrollmentRequestStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return EnrollmentRequestStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown EnrollmentRequestStatus code '"+codeString+"'");
        }
        public Enumeration<EnrollmentRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<EnrollmentRequestStatus>(this, EnrollmentRequestStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<EnrollmentRequestStatus>(this, EnrollmentRequestStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<EnrollmentRequestStatus>(this, EnrollmentRequestStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EnrollmentRequestStatus>(this, EnrollmentRequestStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown EnrollmentRequestStatus code '"+codeString+"'");
        }
    public String toCode(EnrollmentRequestStatus code) {
      if (code == EnrollmentRequestStatus.ACTIVE)
        return "active";
      if (code == EnrollmentRequestStatus.CANCELLED)
        return "cancelled";
      if (code == EnrollmentRequestStatus.DRAFT)
        return "draft";
      if (code == EnrollmentRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(EnrollmentRequestStatus code) {
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
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/enrollmentrequest-status")
    protected Enumeration<EnrollmentRequestStatus> status;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ruleset")
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ruleset")
    protected Coding originalRuleset;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "insurer", type = {Identifier.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Target", formalDefinition="The Insurer who is target  of the request." )
    protected Type insurer;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type provider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type organization;

    /**
     * Patient Resource.
     */
    @Child(name = "subject", type = {Identifier.class, Patient.class}, order=8, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Type subject;

    /**
     * Reference to the program or plan identification, underwriter or payor.
     */
    @Child(name = "coverage", type = {Coverage.class}, order=9, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
    protected Reference coverage;

    /**
     * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
     */
    protected Coverage coverageTarget;

    private static final long serialVersionUID = -1340468824L;

  /**
   * Constructor
   */
    public EnrollmentRequest() {
      super();
    }

  /**
   * Constructor
   */
    public EnrollmentRequest(Enumeration<EnrollmentRequestStatus> status, Type subject, Reference coverage) {
      super();
      this.status = status;
      this.subject = subject;
      this.coverage = coverage;
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
    public EnrollmentRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public EnrollmentRequest addIdentifier(Identifier t) { //3
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
    public Enumeration<EnrollmentRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EnrollmentRequestStatus>(new EnrollmentRequestStatusEnumFactory()); // bb
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
    public EnrollmentRequest setStatusElement(Enumeration<EnrollmentRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public EnrollmentRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public EnrollmentRequest setStatus(EnrollmentRequestStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<EnrollmentRequestStatus>(new EnrollmentRequestStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.ruleset");
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
    public EnrollmentRequest setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.originalRuleset");
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
    public EnrollmentRequest setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.created");
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
    public EnrollmentRequest setCreatedElement(DateTimeType value) { 
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
    public EnrollmentRequest setCreated(Date value) { 
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
     * @return {@link #insurer} (The Insurer who is target  of the request.)
     */
    public Type getInsurer() { 
      return this.insurer;
    }

    /**
     * @return {@link #insurer} (The Insurer who is target  of the request.)
     */
    public Identifier getInsurerIdentifier() throws FHIRException { 
      if (!(this.insurer instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.insurer.getClass().getName()+" was encountered");
      return (Identifier) this.insurer;
    }

    public boolean hasInsurerIdentifier() { 
      return this.insurer instanceof Identifier;
    }

    /**
     * @return {@link #insurer} (The Insurer who is target  of the request.)
     */
    public Reference getInsurerReference() throws FHIRException { 
      if (!(this.insurer instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.insurer.getClass().getName()+" was encountered");
      return (Reference) this.insurer;
    }

    public boolean hasInsurerReference() { 
      return this.insurer instanceof Reference;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who is target  of the request.)
     */
    public EnrollmentRequest setInsurer(Type value) { 
      this.insurer = value;
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
    public EnrollmentRequest setProvider(Type value) { 
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
    public EnrollmentRequest setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #subject} (Patient Resource.)
     */
    public Type getSubject() { 
      return this.subject;
    }

    /**
     * @return {@link #subject} (Patient Resource.)
     */
    public Identifier getSubjectIdentifier() throws FHIRException { 
      if (!(this.subject instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.subject.getClass().getName()+" was encountered");
      return (Identifier) this.subject;
    }

    public boolean hasSubjectIdentifier() { 
      return this.subject instanceof Identifier;
    }

    /**
     * @return {@link #subject} (Patient Resource.)
     */
    public Reference getSubjectReference() throws FHIRException { 
      if (!(this.subject instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.subject.getClass().getName()+" was encountered");
      return (Reference) this.subject;
    }

    public boolean hasSubjectReference() { 
      return this.subject instanceof Reference;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Patient Resource.)
     */
    public EnrollmentRequest setSubject(Type value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
     */
    public Reference getCoverage() { 
      if (this.coverage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.coverage");
        else if (Configuration.doAutoCreate())
          this.coverage = new Reference(); // cc
      return this.coverage;
    }

    public boolean hasCoverage() { 
      return this.coverage != null && !this.coverage.isEmpty();
    }

    /**
     * @param value {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
     */
    public EnrollmentRequest setCoverage(Reference value) { 
      this.coverage = value;
      return this;
    }

    /**
     * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
     */
    public Coverage getCoverageTarget() { 
      if (this.coverageTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.coverage");
        else if (Configuration.doAutoCreate())
          this.coverageTarget = new Coverage(); // aa
      return this.coverageTarget;
    }

    /**
     * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
     */
    public EnrollmentRequest setCoverageTarget(Coverage value) { 
      this.coverageTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("insurer[x]", "Identifier|Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, insurer));
        childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("subject[x]", "Identifier|Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EnrollmentRequestStatus>
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Type
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Type
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
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
          this.status = new EnrollmentRequestStatusEnumFactory().fromType(value); // Enumeration<EnrollmentRequestStatus>
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
        case 1957615864: // insurer
          this.insurer = castToType(value); // Type
          break;
        case -987494927: // provider
          this.provider = castToType(value); // Type
          break;
        case 1178922291: // organization
          this.organization = castToType(value); // Type
          break;
        case -1867885268: // subject
          this.subject = castToType(value); // Type
          break;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new EnrollmentRequestStatusEnumFactory().fromType(value); // Enumeration<EnrollmentRequestStatus>
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("insurer[x]"))
          this.insurer = castToType(value); // Type
        else if (name.equals("provider[x]"))
          this.provider = castToType(value); // Type
        else if (name.equals("organization[x]"))
          this.organization = castToType(value); // Type
        else if (name.equals("subject[x]"))
          this.subject = castToType(value); // Type
        else if (name.equals("coverage"))
          this.coverage = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EnrollmentRequestStatus>
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -2026616696:  return getInsurer(); // Type
        case 2064698607:  return getProvider(); // Type
        case 1326483053:  return getOrganization(); // Type
        case -573640748:  return getSubject(); // Type
        case -351767064:  return getCoverage(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentRequest.status");
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
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentRequest.created");
        }
        else if (name.equals("insurerIdentifier")) {
          this.insurer = new Identifier();
          return this.insurer;
        }
        else if (name.equals("insurerReference")) {
          this.insurer = new Reference();
          return this.insurer;
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
        else if (name.equals("subjectIdentifier")) {
          this.subject = new Identifier();
          return this.subject;
        }
        else if (name.equals("subjectReference")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EnrollmentRequest";

  }

      public EnrollmentRequest copy() {
        EnrollmentRequest dst = new EnrollmentRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        return dst;
      }

      protected EnrollmentRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EnrollmentRequest))
          return false;
        EnrollmentRequest o = (EnrollmentRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(insurer, o.insurer, true) && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true)
           && compareDeep(subject, o.subject, true) && compareDeep(coverage, o.coverage, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EnrollmentRequest))
          return false;
        EnrollmentRequest o = (EnrollmentRequest) other;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, ruleset
          , originalRuleset, created, insurer, provider, organization, subject, coverage
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EnrollmentRequest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Enrollment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EnrollmentRequest.identifier", description="The business identifier of the Enrollment", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Enrollment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient-reference</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subjectreference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient-reference", path="EnrollmentRequest.subject.as(reference)", description="The party to be enrolled", type="reference", target={Patient.class } )
  public static final String SP_PATIENT_REFERENCE = "patient-reference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient-reference</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subjectreference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT_REFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT_REFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EnrollmentRequest:patient-reference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT_REFERENCE = new ca.uhn.fhir.model.api.Include("EnrollmentRequest:patient-reference").toLocked();

 /**
   * Search parameter: <b>subject-identifier</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentRequest.subjectidentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject-identifier", path="EnrollmentRequest.subject.as(identifier)", description="The party to be enrolled", type="token" )
  public static final String SP_SUBJECT_IDENTIFIER = "subject-identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject-identifier</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentRequest.subjectidentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SUBJECT_IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SUBJECT_IDENTIFIER);

 /**
   * Search parameter: <b>subject-reference</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subjectreference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject-reference", path="EnrollmentRequest.subject.as(reference)", description="The party to be enrolled", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_SUBJECT_REFERENCE = "subject-reference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject-reference</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subjectreference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT_REFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT_REFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EnrollmentRequest:subject-reference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT_REFERENCE = new ca.uhn.fhir.model.api.Include("EnrollmentRequest:subject-reference").toLocked();

 /**
   * Search parameter: <b>patient-identifier</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentRequest.subjectidentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient-identifier", path="EnrollmentRequest.subject.as(identifier)", description="The party to be enrolled", type="token" )
  public static final String SP_PATIENT_IDENTIFIER = "patient-identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient-identifier</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EnrollmentRequest.subjectidentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PATIENT_IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PATIENT_IDENTIFIER);


}

