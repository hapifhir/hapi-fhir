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
 * This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
 */
@ResourceDef(name="EnrollmentRequest", profile="http://hl7.org/fhir/Profile/EnrollmentRequest")
public class EnrollmentRequest extends DomainResource {

    public enum EnrollmentRequestStatus {
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
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EnrollmentRequestStatus>(this);
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
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<EnrollmentRequestStatus> status;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "insurer", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Target", formalDefinition="The Insurer who is target  of the request." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who is target  of the request.)
     */
    protected Organization insurerTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Organization.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization organizationTarget;

    /**
     * Patient Resource.
     */
    @Child(name = "subject", type = {Patient.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient subjectTarget;

    /**
     * Reference to the program or plan identification, underwriter or payor.
     */
    @Child(name = "coverage", type = {Coverage.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
    protected Reference coverage;

    /**
     * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
     */
    protected Coverage coverageTarget;

    private static final long serialVersionUID = -214445454L;

  /**
   * Constructor
   */
    public EnrollmentRequest() {
      super();
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
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<EnrollmentRequestStatus>(new EnrollmentRequestStatusEnumFactory());
        this.status.setValue(value);
      }
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
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who is target  of the request.)
     */
    public EnrollmentRequest setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public EnrollmentRequest setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.provider");
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
    public EnrollmentRequest setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner(); // aa
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EnrollmentRequest setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.organization");
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
    public EnrollmentRequest setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public EnrollmentRequest setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #subject} (Patient Resource.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Patient Resource.)
     */
    public EnrollmentRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EnrollmentRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public EnrollmentRequest setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
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
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("insurer", "Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, insurer));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("subject", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EnrollmentRequestStatus>
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
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
          value = new EnrollmentRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EnrollmentRequestStatus>
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case 1957615864: // insurer
          this.insurer = castToReference(value); // Reference
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new EnrollmentRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EnrollmentRequestStatus>
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 1028554472:  return getCreatedElement();
        case 1957615864:  return getInsurer(); 
        case -987494927:  return getProvider(); 
        case 1178922291:  return getOrganization(); 
        case -1867885268:  return getSubject(); 
        case -351767064:  return getCoverage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
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
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type EnrollmentRequest.created");
        }
        else if (name.equals("insurer")) {
          this.insurer = new Reference();
          return this.insurer;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("subject")) {
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(created, o.created, true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, created
          , insurer, provider, organization, subject, coverage);
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
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="EnrollmentRequest.subject", description="The party to be enrolled", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EnrollmentRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("EnrollmentRequest:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="EnrollmentRequest.subject", description="The party to be enrolled", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The party to be enrolled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EnrollmentRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("EnrollmentRequest:patient").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="EnrollmentRequest.organization", description="The organization who generated this resource", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EnrollmentRequest.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EnrollmentRequest:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("EnrollmentRequest:organization").toLocked();


}

